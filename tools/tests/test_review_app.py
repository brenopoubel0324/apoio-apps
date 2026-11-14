import contextlib
import importlib.util
import io
import os
from pathlib import Path
import subprocess
import tempfile
import unittest


SPEC = importlib.util.spec_from_file_location("review_app", Path(__file__).parents[1] / "review_app.py")
review_app = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(review_app)


class AppReviewTest(unittest.TestCase):
    def setUp(self):
        self.directory = tempfile.TemporaryDirectory()
        self.addCleanup(self.directory.cleanup)
        self.base = Path(self.directory.name)
        self.repo = self.base / "app com espaços"
        self.repo.mkdir()
        self.git("init", "-q")

    def git(self, *args):
        return subprocess.check_output(["git", "-C", str(self.repo), *args]).decode().strip()

    def write(self, relative, text, tracked=True):
        path = self.repo / relative
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(text, encoding="utf-8")
        if tracked:
            self.git("add", "--", relative)
        return path

    def test_inventory_preserves_spdx_and_dependency_scopes_remain_manual(self):
        self.write("LICENSE", "Example license text; inspect in full.\n")
        self.write("app/Main.kt", "// SPDX-License-Identifier: GPL-3.0-or-later\nclass Main\n")
        self.write("app/build.gradle.kts", 'dependencies { compileOnly("example:api:1.0") }\n')
        self.write(".github/FUNDING.yml", "# No configured account\n")
        result = review_app.scan(self.repo)
        self.assertEqual(["LICENSE"], result["licenses"])
        self.assertEqual([("app/Main.kt", "GPL-3.0-or-later")], result["spdx"])
        self.assertEqual(["app/build.gradle.kts"], result["manifests"])
        self.assertEqual([".github/FUNDING.yml"], result["funding"])
        self.assertEqual("sem commit", result["reference"])
        self.assertIn("PENDENTE DE REVISÃO MANUAL", review_app.render(result))

    def test_code_strings_are_not_license_headers_and_document_refs_survive(self):
        self.write("example.py", 'message = "SPDX-License-Identifier: MIT"\n')
        self.write("Main.java", "// SPDX-License-Identifier: DocumentRef-origin:LicenseRef-Custom\n")
        self.assertEqual(
            [("Main.java", "DocumentRef-origin:LicenseRef-Custom")],
            review_app.scan(self.repo)["spdx"],
        )

    def test_untracked_missing_and_external_links_do_not_leak_contents(self):
        self.write("untracked.kt", "// SPDX-License-Identifier: LicenseRef-Untracked\n", tracked=False)
        missing = self.write("missing.kt", "// SPDX-License-Identifier: MIT\n")
        missing.unlink()
        external = self.base / "external.kt"
        external.write_text("// SPDX-License-Identifier: LicenseRef-PrivateSentinel\n")
        try:
            (self.repo / "link.kt").symlink_to(external)
        except (OSError, NotImplementedError):
            self.skipTest("Symlinks unavailable")
        self.git("add", "--", "link.kt")
        result = review_app.scan(self.repo)
        self.assertEqual([], result["spdx"])
        self.assertEqual(["link.kt", "missing.kt"], result["skipped"])
        self.assertNotIn("PrivateSentinel", review_app.render(result))

    def test_header_limit_and_binary_exclusion_are_explicit(self):
        self.write("late.kt", "\n" * 100 + "// SPDX-License-Identifier: MIT\n")
        self.write("binary.jar", "\0SPDX-License-Identifier: MIT\n")
        result = review_app.scan(self.repo)
        self.assertEqual([], result["spdx"])
        report = review_app.render(result)
        self.assertIn("80 linhas", report)
        self.assertIn("Binários e licenças embutidas não foram abertos", report)
        self.assertIn("PENDENTE DE REVISÃO MANUAL", report)

    def test_cli_preserves_consumer_and_never_overwrites_human_review(self):
        self.write("Main.kt", "// SPDX-License-Identifier: MIT\n")
        before = self.git("status", "--porcelain")
        output = self.base / "review.md"
        with contextlib.redirect_stdout(io.StringIO()):
            self.assertEqual(0, review_app.main(["--repo", str(self.repo), "--output", str(output)]))
        self.assertEqual(before, self.git("status", "--porcelain"))
        output.write_text(output.read_text(encoding="utf-8") + "Decisão humana preservada.\n", encoding="utf-8")
        saved = output.read_bytes()
        with contextlib.redirect_stderr(io.StringIO()), self.assertRaises(SystemExit) as error:
            review_app.main(["--repo", str(self.repo), "--output", str(output)])
        self.assertEqual(2, error.exception.code)
        self.assertEqual(saved, output.read_bytes())

    def test_non_git_input_creates_no_report(self):
        no_git = self.base / "no-git"
        no_git.mkdir()
        output = self.base / "invalid.md"
        with contextlib.redirect_stderr(io.StringIO()), self.assertRaises(SystemExit) as error:
            review_app.main(["--repo", str(no_git), "--output", str(output)])
        self.assertEqual(2, error.exception.code)
        self.assertFalse(output.exists())

    @unittest.skipIf(os.name == "nt", "Windows does not allow these filename characters")
    def test_markdown_data_cannot_add_html_or_table_cells(self):
        self.write("unsafe|<b>.kt", "// SPDX-License-Identifier: MIT\n")
        report = review_app.render(review_app.scan(self.repo))
        self.assertNotIn("<b>", report)
        self.assertIn("unsafe\\|&lt;b&gt;.kt", report)

    def test_reference_is_not_used_as_a_substitute_for_worktree_evidence(self):
        path = self.write("Main.kt", "// SPDX-License-Identifier: MIT\n")
        self.git("-c", "user.name=Fixture", "-c", "user.email=fixture@example.invalid", "commit", "-qm", "Fixture")
        reference = self.git("rev-parse", "HEAD")
        path.write_text("// SPDX-License-Identifier: Apache-2.0\n")
        result = review_app.scan(self.repo)
        self.assertEqual(reference, result["reference"])
        self.assertEqual([("Main.kt", "Apache-2.0")], result["spdx"])
        self.assertIn("não comprova correspondência com o commit", review_app.render(result))


if __name__ == "__main__":
    unittest.main()
