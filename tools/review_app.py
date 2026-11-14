"""Collect local license evidence and start a manual app monetization review."""

import argparse
from datetime import datetime, timezone
import html
import os
from pathlib import Path
import re
import subprocess


BASE = Path(__file__).resolve().parents[1]
HEADER_BYTES = 32768
HEADER_LINES = 80
LICENSE_NAME = re.compile(
    r"(?:^|[-_.])(?:licen[cs]e|copying|copyright|notices?)(?:$|[-_.])", re.I
)
SPDX = re.compile(
    r"^[ \t]*(?:(?://|#+|/\*+|\*|<!--|;|--)[ \t]*)?"
    r"SPDX-License-Identifier:[ \t]*([^\r\n]+)", re.M
)
TEXT_SUFFIXES = {
    ".java", ".kt", ".kts", ".gradle", ".py", ".js", ".jsx", ".ts", ".tsx",
    ".dart", ".c", ".cc", ".cpp", ".h", ".hpp", ".rs", ".go", ".swift",
    ".sh", ".xml", ".json", ".toml", ".yaml", ".yml", ".md",
}
MANIFESTS = {
    "build.gradle", "build.gradle.kts", "settings.gradle", "settings.gradle.kts",
    "libs.versions.toml", "gradle.lockfile", "package.json", "package-lock.json",
    "yarn.lock", "pnpm-lock.yaml", "pubspec.yaml", "pubspec.lock", "Cargo.toml",
    "Cargo.lock", "go.mod", "go.sum", "pyproject.toml", "requirements.txt",
    "poetry.lock", "uv.lock", "Podfile", "Podfile.lock", "Package.swift",
}


def git(repo, *args, required=True):
    result = subprocess.run(
        ["git", "-C", str(repo), *args], capture_output=True, check=False
    )
    if required and result.returncode:
        raise ValueError("Indique um checkout Git local acessível em --repo.")
    return result.stdout if result.returncode == 0 else b""


def scan(repo):
    root = Path(os.fsdecode(git(repo, "rev-parse", "--show-toplevel")).rstrip("\r\n")).resolve()
    reference = git(root, "rev-parse", "--verify", "HEAD", required=False).decode().strip()
    paths = sorted({os.fsdecode(p) for p in git(root, "ls-files", "--cached", "-z").split(b"\0") if p})
    evidence = {
        "app": root.name, "reference": reference or "sem commit", "tracked": len(paths),
        "licenses": [], "spdx": [], "manifests": [], "funding": [], "skipped": [],
    }
    for relative in paths:
        path = root / relative
        try:
            if path.is_symlink() or not path.resolve().is_relative_to(root) or not path.is_file():
                evidence["skipped"].append(relative)
                continue
            is_license = bool(LICENSE_NAME.search(path.name))
            if is_license:
                evidence["licenses"].append(relative)
            if path.name in MANIFESTS:
                evidence["manifests"].append(relative)
            if relative in (".github/FUNDING.yml", ".github/FUNDING.yaml"):
                evidence["funding"].append(relative)
            if is_license or path.suffix.lower() in TEXT_SUFFIXES:
                with path.open("rb") as stream:
                    data = stream.read(HEADER_BYTES)
                if b"\0" in data:
                    continue
                header = "\n".join(data.decode("utf-8", errors="replace").splitlines()[:HEADER_LINES])
                expressions = {re.sub(r"(?:\*/|-->)\s*$", "", m.group(1)).strip()
                               for m in SPDX.finditer(header)}
                for expression in sorted(expressions - {""}):
                    display = expression if len(expression) <= 180 else expression[:180] + " [trecho truncado]"
                    evidence["spdx"].append((relative, display))
        except OSError:
            evidence["skipped"].append(relative)
    return evidence


def markdown(value):
    value = html.escape(str(value), quote=False).replace("\r", " ").replace("\n", " ")
    return re.sub(r"([\\`*_{}\[\]|])", r"\\\1", value)


def render(evidence):
    lines = [
        "# Revisão de " + markdown(evidence["app"]), "",
        "**Resultado: PENDENTE DE REVISÃO MANUAL.**", "",
        "Referência HEAD: " + markdown(evidence["reference"]) + ".",
        "Gerado em: " + datetime.now(timezone.utc).isoformat(timespec="seconds") + ".", "",
        "As evidências vêm dos arquivos rastreados presentes no checkout. HEAD é uma",
        "referência; esta coleta não comprova correspondência com o commit ou com um APK.",
        "Não determina compatibilidade, autoria, permissão de reuso ou pagamento.", "",
        "## Evidências coletadas", "",
        f"Arquivos rastreados: {evidence['tracked']}.", "",
        "### Textos e avisos de licença", "",
    ]
    lines += ["- " + markdown(p) for p in evidence["licenses"]] or ["Nenhum nome de arquivo de licença reconhecido."]
    root_licenses = [p for p in evidence["licenses"] if "/" not in p and re.match(r"(?:licen[cs]e|copying)(?:$|[-_.])", p, re.I)]
    if not root_licenses:
        lines += ["", "Não foi encontrado LICENSE/COPYING principal com nome reconhecido na raiz.",
                  "Isso não prova ausência de termos em outro arquivo; conferir manualmente."]
    lines += ["", "### Declarações SPDX em cabeçalhos", "", "| Arquivo | Declaração encontrada |", "| --- | --- |"]
    lines += [f"| {markdown(p)} | {markdown(expr)} |" for p, expr in evidence["spdx"]]
    if not evidence["spdx"]:
        lines.append("| — | Nenhuma declaração reconhecida no trecho examinado |")
    lines += ["", f"Leitura limitada aos primeiros {HEADER_BYTES} bytes e {HEADER_LINES} linhas de arquivos de texto",
              "com extensões reconhecidas ou nomes de licença. As declarações não são validadas",
              "contra um catálogo SPDX; textos completos e arquivos não reconhecidos exigem inspeção.",
              "Binários e licenças embutidas não foram abertos.", "", "### Manifests/lockfiles a conferir", ""]
    lines += ["- " + markdown(p) for p in evidence["manifests"]] or ["Nenhum manifest com nome reconhecido."]
    lines += ["", "A lista não resolve versões nem licenças das dependências. Conferir o que entra",
              "no artefato, separando runtime, compile-only, teste e build.", "", "### Apoio no GitHub", ""]
    lines += ["- " + markdown(p) for p in evidence["funding"]] or ["Nenhum FUNDING próprio rastreado; isso não exclui configuração herdada do perfil."]
    lines += ["", "A presença do arquivo não comprova conta ativa, recebedor ou checkout.", "", "### Arquivos não lidos", ""]
    lines += ["- " + markdown(p) for p in evidence["skipped"]] or ["Nenhum arquivo ausente, inacessível ou link simbólico identificado."]
    lines += ["", "Links simbólicos e arquivos fora da raiz não são lidos. Arquivos não rastreados",
              "não entram nesta coleta. Não são executados builds ou scripts do app.", "",
              (BASE / "reviews/modelo-app.md").read_text(encoding="utf-8").split("\n", 1)[1]]
    return "\n".join(lines).rstrip() + "\n"


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--repo", required=True, type=Path, help="Checkout Git do app; nenhuma busca remota")
    parser.add_argument("--output", type=Path, help="Novo arquivo .md; nunca sobrescreve uma revisão")
    args = parser.parse_args(argv)
    try:
        evidence = scan(args.repo.resolve())
        slug = re.sub(r"[^a-z0-9-]+", "-", evidence["app"].lower()).strip("-")[:60] or "app"
        stamp = datetime.now(timezone.utc).strftime("%Y%m%d-%H%M%S-%f")
        output = args.output or BASE / "reviews/local" / f"{slug}-{stamp}.md"
        if output.suffix.lower() != ".md" or ".git" in output.parts:
            raise ValueError("Use um novo arquivo .md fora dos metadados .git.")
        content = render(evidence)
        output.parent.mkdir(parents=True, exist_ok=True)
        with output.open("x", encoding="utf-8") as stream:
            stream.write(content)
    except (OSError, ValueError) as error:
        parser.error(str(error))
    print(f"Ficha criada: {output.resolve()}")
    print("Estado: revisão manual pendente. Nenhuma licença ou cobrança foi ativada.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
