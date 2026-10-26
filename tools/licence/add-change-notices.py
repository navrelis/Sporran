#!/usr/bin/env python3
"""Add or refresh the "Changed by the Sporran project" notice in modified upstream source files.

LGPL-2.1 section 2(a) requires modified files to carry prominent notices stating that they were changed and the
date of the change. Sporran's public source snapshot has no git history, so every source file that came from
upstream and was changed gets a dated notice in its header. This script finds those files with git, works out the
first and last date they were changed by us, and writes (or updates) the notice.

Which files count as modified upstream files:
  src/, compat/          *.java / *.kt changed or renamed since the Kilt tag v21.1.13. Git pairs files with their
                         upstream file at 30% similarity (short renamed files fall below the default 50%), and a file
                         that git still sees as added counts as upstream if a file of the same name exists in the
                         tag (Sporran -> Kilt in the name is allowed). Over-marking is safe, under-marking is not.
                         The remaining files are listed as "Sporran-only" for review; files identical to upstream
                         are skipped.
  forge/, fml/, loader/  *.java / *.kt / *.kts changed since the commit that vendored them (see VENDORED.md).
                         Files added after vendoring are skipped.

Run from anywhere inside the repository (the script finds the repository root itself):

    python tools/licence/add-change-notices.py            # write the notices
    python tools/licence/add-change-notices.py --dry-run  # only report what would change
    python tools/licence/add-change-notices.py --check    # exit 1 if any notice is missing or out of date

The script is idempotent: a file that already has a notice starting with "Changed by the Sporran project" gets
that notice rewritten with the current dates instead of a second one. An existing licence/copyright comment at
the top of a file is never changed; the notice is inserted directly after it (or at the very top if the file has
no such comment). Line endings, BOM and encoding of each file are preserved.

The commit that adds the notices would itself count as a change and move every "last changed" date to its own
day. To avoid that, commits whose message contains the --skip-grep text (default "change notices") are ignored
when the dates are computed, so give that commit a message such as "Add LGPL change notices".

Only the Python 3 standard library and the git command line are used.
"""

import argparse
import re
import subprocess
import sys
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

MARKER = "Changed by the Sporran project"
KILT_ORIGIN = ("Kilt by KiltMC (https://github.com/KiltMC/Kilt)", "the GNU LGPL 2.1")

# name -> (git paths, extensions, base kind, origin, licence)
TREES = {
    "src": (["src"], (".java", ".kt"), "kilt", *KILT_ORIGIN),
    "compat": (["compat"], (".java", ".kt"), "kilt", *KILT_ORIGIN),
    "forge": (["forge"], (".java", ".kt", ".kts"), "vendor",
              "NeoForge by NeoForged, as ported by KiltMC (see VENDORED.md)", "the GNU LGPL 2.1"),
    "fml": (["fml"], (".java", ".kt", ".kts"), "vendor",
            "FancyModLoader by NeoForged, as ported by KiltMC (see VENDORED.md)", "the GNU LGPL 2.1"),
    "loader": (["loader"], (".java", ".kt", ".kts"), "vendor",
               "Knit Loader by KiltMC (see VENDORED.md)", "the MIT licence (loader/LICENSE)"),
}

LICENCE_WORDS = re.compile(r"copyright|licen[cs]e|spdx|permission|warranty", re.IGNORECASE)
WHITESPACE = re.compile(r"\s*")
# The Gradle task "tagPatches" expects this line to be the first line of an inject class, so the notice goes below it.
TRACKED_HASH = re.compile(r"// TRACKED HASH: [^\r\n]*(?:\r\n|\n)")
BLANK_LINES = re.compile(r"(?:[ \t]*\r?\n)*")


def git(*args, root):
    result = subprocess.run(["git", *args], cwd=root, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if result.returncode != 0:
        sys.exit("git %s failed: %s" % (" ".join(args), result.stderr.decode("utf-8", "replace").strip()))
    return result.stdout.decode("utf-8", "surrogateescape")


def find_vendoring_commit(root):
    out = git("log", "--diff-filter=A", "--format=%H", "--", "forge/LICENSE.txt", "fml/LICENSE.txt",
              "loader/LICENSE", root=root).split()
    if not out:
        sys.exit("Could not find the vendoring commit (no commit adds forge/LICENSE.txt).")
    return out[-1]  # oldest


def changed_files(root, base, paths, extensions):
    """Return (modified paths as they are in HEAD, added paths, skipped [(path, reason)]).

    Renames are detected at a low similarity (30%), because short files whose package and class names were renamed
    fall below git's default 50%. Added files are returned separately; the caller decides whether they are really
    new or just too different from their upstream file for git to notice.
    """
    out = git("diff", "--find-renames=30%", "--name-status", "-z", base, "HEAD", "--", *paths, root=root)
    parts = out.split("\0")
    modified, added, skipped = [], [], []
    i = 0
    while i < len(parts) and parts[i]:
        status = parts[i]
        if status[0] in "RC":
            new = parts[i + 2]
            i += 3
            if not new.endswith(extensions):
                continue
            if status[0] == "R" and status[1:] == "100":
                skipped.append((new, "pure rename, identical to upstream"))
            else:
                modified.append(new)
        else:
            path = parts[i + 1]
            i += 2
            if not path.endswith(extensions):
                continue
            if status[0] == "M":
                modified.append(path)
            elif status[0] == "A":
                added.append(path)
            # D: the file no longer exists, nothing to mark
    return modified, added, skipped


def upstream_basenames(root, base, paths, extensions):
    """File names of all source files in the upstream tree."""
    out = git("ls-tree", "-r", "--name-only", base, "--", *paths, root=root)
    return {line.rsplit("/", 1)[-1] for line in out.splitlines() if line.endswith(extensions)}


def basename_matches(path, upstream_names):
    """True if the file has the name of an upstream file, allowing for the Kilt -> Sporran rename of the name."""
    name = path.rsplit("/", 1)[-1]
    candidates = {name, name.replace("Sporran", "Kilt"), name.replace("sporran", "kilt")}
    if name.startswith("Sporran"):
        candidates.add("Kilt" + name[len("Sporran"):])
    return bool(candidates & upstream_names)


def creation_info(root, rev_range, path):
    """(date, short hash, subject) of the commit that created the file at its current path, or None."""
    out = git("log", "--diff-filter=A", "--format=%ad|%h|%s", "--date=short", rev_range, "--", path, root=root)
    lines = [l for l in out.splitlines() if l]
    if not lines:
        return None
    date, short, subject = lines[-1].split("|", 2)  # oldest
    return date, short, subject


def change_dates(root, rev_range, path, skip_grep):
    args = ["log", "--follow", "--format=%ad", "--date=short"]
    if skip_grep:
        args += ["--invert-grep", "--grep=" + skip_grep]
    args += [rev_range, "--", path]
    dates = sorted(set(git(*args, root=root).split()))
    return (dates[0], dates[-1]) if dates else None


def notice_text(first, last, origin, licence, eol):
    when = "on %s" % first if first == last else "between %s and %s" % (first, last)
    lines = ["/*",
             " * %s %s; see docs/CHANGES.md." % (MARKER, when),
             " * Based on %s. Licensed under %s." % (origin, licence),
             " */"]
    return eol.join(lines)


def detect_eol(text):
    crlf = text.count("\r\n")
    lf = text.count("\n") - crlf
    return "\r\n" if crlf > lf else "\n"


def leading_blocks(text, start):
    """Block comments at the top of the file, separated only by whitespace: [(start, end)]."""
    blocks = []
    pos = start
    while True:
        pos = WHITESPACE.match(text, pos).end()
        if not text.startswith("/*", pos) or text.startswith("/**", pos):  # javadoc is never a header
            break
        end = text.find("*/", pos + 2)
        if end < 0:
            break
        blocks.append((pos, end + 2))
        pos = end + 2
    return blocks


def apply_notice(text, notice_for):
    """Return the text with the notice added or refreshed. notice_for(eol) builds it for the file's line ending."""
    eol = detect_eol(text)
    notice = notice_for(eol)
    start = 1 if text.startswith("\ufeff") else 0
    tracked = TRACKED_HASH.match(text, start)
    if tracked:
        start = tracked.end()
    blocks = leading_blocks(text, start)

    # An existing notice is rewritten in place.
    for b_start, b_end in blocks:
        if MARKER in text[b_start:b_end]:
            return text[:b_start] + notice + text[b_end:]

    # Insert directly after an existing licence/copyright header (the licence blocks at the very top).
    header_end = None
    for b_start, b_end in blocks:
        if not LICENCE_WORDS.search(text[b_start:b_end]):
            break
        header_end = b_end
    if header_end is not None:
        rest = text[header_end:]
        # Whatever follows the header stays as it is; only make sure one blank line separates it from the notice.
        blank = BLANK_LINES.match(rest).group(0).count("\n")
        pad = eol * (2 - blank) if blank < 2 else ""
        return text[:header_end] + eol + eol + notice + pad + rest

    # Otherwise at the very top.
    return text[:start] + notice + eol + eol + text[start:]


def process(root, name, path, first, last, dry_run):
    origin, licence = TREES[name][3], TREES[name][4]
    file = root / path
    text = file.read_bytes().decode("utf-8", "surrogateescape")
    new = apply_notice(text, lambda eol: notice_text(first, last, origin, licence, eol))
    if new == text:
        return False
    if not dry_run:
        file.write_bytes(new.encode("utf-8", "surrogateescape"))
    return True


def main():
    ap = argparse.ArgumentParser(description="Add or refresh the Sporran change notice in modified upstream files.")
    ap.add_argument("--dry-run", action="store_true", help="report what would change, write nothing")
    ap.add_argument("--check", action="store_true", help="like --dry-run, exit status 1 if anything is out of date")
    ap.add_argument("--kilt-base", default="v21.1.13", help="upstream Kilt tag or commit (default v21.1.13)")
    ap.add_argument("--vendor-base", help="vendoring commit for forge/, fml/, loader/ (default: found with git log)")
    ap.add_argument("--skip-grep", default="change notices",
                    help="ignore commits whose message contains this text when computing dates")
    ap.add_argument("--verbose", action="store_true", help="list every file")
    args = ap.parse_args()

    root = Path(git("rev-parse", "--show-toplevel", root=Path(__file__).resolve().parent).strip())
    dry = args.dry_run or args.check
    vendor_base = args.vendor_base or find_vendoring_commit(root)
    bases = {"kilt": args.kilt_base, "vendor": vendor_base}
    print("Kilt base: %s, vendoring commit: %s" % (args.kilt_base, vendor_base[:10]))

    total_changed = 0
    for name, (paths, exts, kind, _origin, _licence) in TREES.items():
        base = bases[kind]
        modified, added, skipped = changed_files(root, base, paths, exts)
        by_name = []
        if kind == "kilt":
            names = upstream_basenames(root, base, paths, exts)
            by_name = [p for p in added if basename_matches(p, names)]
        new_files = [p for p in added if p not in by_name]
        modified = [p for p in modified + by_name if (root / p).is_file()]
        new_files = [p for p in new_files if (root / p).is_file()]
        rev_range = "%s..HEAD" % base

        def work(path):
            dates = change_dates(root, rev_range, path, args.skip_grep)
            if dates is None:
                return path, None, False
            if path in by_name:  # first change = the commit that created the file
                created = creation_info(root, rev_range, path)
                if created:
                    dates = (min(created[0], dates[0]), dates[1])
            return path, dates, process(root, name, path, dates[0], dates[1], dry)

        with ThreadPoolExecutor(max_workers=8) as pool:
            results = list(pool.map(work, modified))
            created = list(pool.map(lambda p: (p, creation_info(root, rev_range, p)), new_files))
        no_dates = [p for p, d, _ in results if d is None]
        changed = [p for p, d, c in results if c]
        print("%-7s modified upstream files: %4d (of which matched by file name only: %d), notices %s: %4d, "
              "Sporran-only files: %d%s" % (
                  name, len(modified), len(by_name), "to write" if dry else "written", len(changed),
                  len(new_files), ", no change date found: %d" % len(no_dates) if no_dates else ""))
        for p in no_dates:
            print("   WARNING no change date for " + p)
        if args.verbose:
            for p, d, c in results:
                print("   %s %s %s%s" % ("*" if c else " ", d, p, "  [matched by name]" if p in by_name else ""))
            for p, why in skipped:
                print("   skipped %s (%s)" % (p, why))
        if new_files:
            print("   Treated as Sporran-only (no upstream counterpart by content or file name):")
            for p, info in created:
                print("      %s  [%s]" % (p, "%s %s %s" % (info[0], info[1], info[2]) if info else "creating commit not found"))
        total_changed += len(changed)

    if args.check and total_changed:
        print("%d file(s) need a notice or a date refresh." % total_changed)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
