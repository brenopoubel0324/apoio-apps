"""Validate release tags and produce checksums for the exact library artifacts."""
from pathlib import Path
import hashlib
import re
import sys

root = Path(__file__).resolve().parents[1]
version = next(line.split("=", 1)[1].strip() for line in
               (root / "gradle.properties").read_text().splitlines()
               if line.startswith("VERSION_NAME="))
if len(sys.argv) != 2 or sys.argv[1] != f"v{version}" or not re.fullmatch(r"\d+\.\d+\.\d+", version):
    raise SystemExit(f"Expected release tag v{version}")
artifacts = []
for module in ("core", "android"):
    for suffix in ("", "-sources"):
        path = root / module / "build/libs" / f"{module}-{version}{suffix}.jar"
        if not path.is_file():
            raise SystemExit(f"Missing artifact: {path.relative_to(root)}")
        artifacts.append(path)
checksums = root / "build/release-SHA256SUMS.txt"
checksums.parent.mkdir(exist_ok=True)
checksums.write_text("".join(f"{hashlib.sha256(p.read_bytes()).hexdigest()}  {p.name}\n" for p in artifacts))
print(f"Release v{version}: {len(artifacts)} artifacts checked")
