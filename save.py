#!/usr/bin/env python3
"""
save.py — DSA Handbook auto-saver

Modes:
  python3 save.py          -> save your latest Accepted submission (single mode)
  python3 save.py 12       -> BULK: save last 12 Accepted problems into ONE topic/pattern
  python3 save.py readme   -> rebuild ALL topic READMEs + root stats (no fetching)

After every save, the script also auto-updates:
  - the topic's README.md   (table of all problems in that topic, grouped by pattern)
  - the root README.md      (solved-count stats per topic)
Both are updated ONLY between <!-- AUTO:START --> and <!-- AUTO:END --> markers,
so anything you write outside the markers is preserved.

Setup (one time):
  Put your LEETCODE_SESSION cookie value in a file named .leetcode_session
  in the repo root, and add .leetcode_session to .gitignore.
"""

import json
import re
import subprocess
import sys
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parent
SESSION_FILE = ROOT / ".leetcode_session"
SKIP_DIRS = {"templates", "docs", ".git", "__pycache__"}
MAX_SCAN = 200

MARK_START = "<!-- AUTO:START -->"
MARK_END = "<!-- AUTO:END -->"

EXT = {
    "java": "java", "python3": "py", "python": "py", "cpp": "cpp", "c": "c",
    "javascript": "js", "typescript": "ts", "golang": "go", "csharp": "cs",
    "kotlin": "kt", "rust": "rs", "swift": "swift", "scala": "scala",
    "ruby": "rb", "mysql": "sql", "mssql": "sql", "oraclesql": "sql",
}
SOLUTION_EXTS = set(EXT.values())

COMMENT_STYLES = {
    "java": ("/**", " * ", " */"), "cpp": ("/**", " * ", " */"),
    "c": ("/**", " * ", " */"), "js": ("/**", " * ", " */"),
    "ts": ("/**", " * ", " */"), "cs": ("/**", " * ", " */"),
    "go": ("/*", " * ", " */"), "kt": ("/**", " * ", " */"),
    "rs": ("/*", " * ", " */"), "swift": ("/**", " * ", " */"),
    "py": ('"""', "", '"""'), "rb": ("=begin", "", "=end"),
    "sql": ("/*", " * ", " */"),
}


def die(msg):
    print(f"\n❌ {msg}")
    sys.exit(1)


def read_session():
    if not SESSION_FILE.exists():
        die(f"Missing {SESSION_FILE.name} — paste your LEETCODE_SESSION cookie value into it.")
    session = SESSION_FILE.read_text().strip()
    if not session:
        die(f"{SESSION_FILE.name} is empty.")
    return session


def http_get(url, session):
    req = urllib.request.Request(url, headers={
        "Cookie": f"LEETCODE_SESSION={session}",
        "Referer": "https://leetcode.com/",
        "User-Agent": "Mozilla/5.0",
    })
    with urllib.request.urlopen(req, timeout=15) as r:
        return json.loads(r.read())


def fetch_accepted(session, want):
    found = {}
    offset = 0
    while len(found) < want and offset < MAX_SCAN:
        try:
            data = http_get(
                f"https://leetcode.com/api/submissions/?offset={offset}&limit=20",
                session,
            )
        except Exception as e:
            die(f"Could not reach LeetCode API ({e}). Cookie may have expired — re-copy it from the browser.")
        subs = data.get("submissions_dump", [])
        if not subs:
            break
        for s in subs:
            if s.get("status_display") == "Accepted" and s["title_slug"] not in found:
                found[s["title_slug"]] = s
                if len(found) == want:
                    break
        if not data.get("has_next", False):
            break
        offset += 20
    if not found:
        die("No Accepted submissions found.")
    return list(found.values())


def question_number(slug):
    body = json.dumps({
        "query": "query q($s: String!) { question(titleSlug: $s) { questionFrontendId } }",
        "variables": {"s": slug},
    }).encode()
    req = urllib.request.Request(
        "https://leetcode.com/graphql",
        data=body,
        headers={
            "Content-Type": "application/json",
            "Referer": "https://leetcode.com/",
            "User-Agent": "Mozilla/5.0",
        },
    )
    try:
        with urllib.request.urlopen(req, timeout=15) as r:
            data = json.loads(r.read())
        return data["data"]["question"]["questionFrontendId"]
    except Exception:
        return None


def list_dirs(parent):
    return sorted(
        p.name for p in parent.iterdir()
        if p.is_dir() and p.name not in SKIP_DIRS and not p.name.startswith(".")
    )


def choose(prompt, options, extras):
    print(f"\n{prompt}")
    for i, name in enumerate(options, 1):
        print(f"  {i:2d}) {name}")
    for j, (label, _) in enumerate(extras, len(options) + 1):
        print(f"  {j:2d}) {label}")
    while True:
        choice = input("Pick a number: ").strip()
        if choice.isdigit():
            n = int(choice)
            if 1 <= n <= len(options):
                return options[n - 1]
            k = n - len(options) - 1
            if 0 <= k < len(extras):
                return extras[k][1]()
        print("Invalid choice, try again.")


def new_folder(parent, what):
    name = input(f"New {what} name: ").strip()
    if not name:
        die("Empty name.")
    (parent / name).mkdir(parents=True, exist_ok=True)
    return name


def pick_topic_and_pattern():
    topics = list_dirs(ROOT)
    topic = choose("📚 Which TOPIC?", topics,
                   [("➕ new topic", lambda: new_folder(ROOT, "topic"))])
    topic_dir = ROOT / topic
    patterns = list_dirs(topic_dir)
    extras = [
        ("➕ new pattern", lambda: new_folder(topic_dir, "pattern")),
        ("📁 save directly in this topic (no pattern)", lambda: None),
    ]
    pattern = choose(f"🧩 Which PATTERN in '{topic}'?", patterns, extras)
    return topic, pattern


def build_header(ext, number, title, slug, topic, pattern):
    opener, prefix, closer = COMMENT_STYLES.get(ext, ("//", "// ", "//"))
    num = f"{number}. " if number else ""
    where = f"{topic} -> {pattern}" if pattern else topic
    lines = [
        opener,
        f"{prefix}{num}{title}",
        f"{prefix}https://leetcode.com/problems/{slug}/",
        f"{prefix}Pattern: {where}",
        f"{prefix}",
        f"{prefix}Approach:",
        f"{prefix}Time:  O( )   Space: O( )",
        closer,
        "",
    ]
    return "\n".join(lines)


def run(cmd):
    result = subprocess.run(cmd, cwd=ROOT, capture_output=True, text=True)
    if result.returncode != 0:
        die(f"`{' '.join(cmd)}` failed:\n{result.stderr.strip()}")
    return result.stdout.strip()


def default_name(number, slug):
    return f"{int(number):04d}-{slug}" if number and str(number).isdigit() else slug


# ---------------------------------------------------------------- README auto-generation

def solution_files(directory):
    return sorted(
        p for p in directory.iterdir()
        if p.is_file() and p.suffix.lstrip(".") in SOLUTION_EXTS
    )


def parse_solution(path):
    """Extract (number, title, url) from a solution file's header, with filename fallback."""
    number, title, url = None, None, None
    try:
        for line in path.read_text(encoding="utf-8", errors="ignore").splitlines()[:12]:
            clean = line.strip().lstrip("/*#\"'=- ").strip()
            m = re.match(r"^(\d+)\.\s+(.+)$", clean)
            if m and not title:
                number, title = m.group(1), m.group(2).strip()
            if "leetcode.com/problems/" in clean and not url:
                url = re.search(r"https?://\S+", clean)
                url = url.group(0).rstrip("/") + "/" if url else None
    except Exception:
        pass
    # Fallbacks from filename like 0209-minimum-size-subarray-sum
    stem = path.stem
    m = re.match(r"^0*(\d+)-(.+)$", stem)
    slug = m.group(2) if m else stem
    if not number and m:
        number = m.group(1)
    if not title:
        title = slug.replace("-", " ").title()
    if not url:
        url = f"https://leetcode.com/problems/{slug}/"
    return number, title, url


def problem_table(files, base_dir, indent_heading=None):
    lines = []
    if indent_heading:
        lines.append(f"### {indent_heading}")
        lines.append("")
    lines.append("| # | Problem | Solution |")
    lines.append("|---|---------|----------|")
    rows = []
    for f in files:
        number, title, url = parse_solution(f)
        rows.append((int(number) if number and number.isdigit() else 10**9, number or "—", title, url, f))
    for _, num, title, url, f in sorted(rows):
        rel = f.relative_to(base_dir).as_posix()
        link_path = "/".join(urllib.request.quote(part) for part in rel.split("/"))
        rows_titles = title.replace("|", "\\|")
        lines.append(f"| {num} | [{rows_titles}]({url}) | [{f.name}]({link_path}) |")
    lines.append("")
    return lines


def topic_counts(topic_dir):
    count = len(solution_files(topic_dir))
    for sub in list_dirs(topic_dir):
        count += len(solution_files(topic_dir / sub))
    return count


def inject(path, generated):
    """Write `generated` between AUTO markers in `path`, preserving everything outside."""
    block = f"{MARK_START}\n{generated.rstrip()}\n{MARK_END}"
    if path.exists():
        text = path.read_text(encoding="utf-8")
        if MARK_START in text and MARK_END in text:
            pre = text.split(MARK_START)[0]
            post = text.split(MARK_END)[-1]
            new = pre + block + post
        else:
            new = text.rstrip() + "\n\n" + block + "\n"
    else:
        new = block + "\n"
    path.write_text(new, encoding="utf-8")


def gen_topic_readme(topic):
    topic_dir = ROOT / topic
    lines = [f"## 📌 Solved problems", ""]
    direct = solution_files(topic_dir)
    total = len(direct)
    if direct:
        lines += problem_table(direct, topic_dir)
    for pattern in list_dirs(topic_dir):
        files = solution_files(topic_dir / pattern)
        if not files:
            continue
        total += len(files)
        lines += problem_table(files, topic_dir, indent_heading=f"🧩 {pattern}")
    if total == 0:
        lines.append("_No problems saved yet._")
        lines.append("")
    lines.append(f"**Total: {total}**")
    inject(topic_dir / "README.md", "\n".join(lines))
    return topic_dir / "README.md"


def gen_root_stats():
    lines = ["## 📊 Progress", "", "| Topic | Patterns | Solved |", "|-------|----------|--------|"]
    grand = 0
    for topic in list_dirs(ROOT):
        topic_dir = ROOT / topic
        n = topic_counts(topic_dir)
        grand += n
        patterns = ", ".join(list_dirs(topic_dir)) or "—"
        topic_link = urllib.request.quote(topic)
        lines.append(f"| [{topic}]({topic_link}) | {patterns} | {n} |")
    lines.append(f"| **Total** | | **{grand}** |")
    inject(ROOT / "README.md", "\n".join(lines))
    return ROOT / "README.md"


def update_readmes(topics):
    """Regenerate READMEs for the given topics + root stats. Returns relative paths."""
    paths = []
    for topic in sorted(set(topics)):
        paths.append(gen_topic_readme(topic))
    paths.append(gen_root_stats())
    rels = [p.relative_to(ROOT).as_posix() for p in paths]
    for r in rels:
        print(f"📄 Updated: {r}")
    return rels


# ---------------------------------------------------------------- saving & git

def save_file(sub, topic, pattern, ask_name=True):
    title, slug, lang, code = sub["title"], sub["title_slug"], sub["lang"], sub["code"]
    ext = EXT.get(lang, "txt")
    number = question_number(slug)
    target_dir = ROOT / topic / pattern if pattern else ROOT / topic

    name = default_name(number, slug)
    if ask_name:
        name = input(f"📝 File name [{name}]: ").strip() or name
    filepath = target_dir / f"{name}.{ext}"

    if filepath.exists():
        if input(f"⚠️  {filepath.name} already exists there. Overwrite? (y/n): ").strip().lower() != "y":
            print(f"⏭️  Skipped {title}")
            return None

    filepath.write_text(build_header(ext, number, title, slug, topic, pattern) + code + "\n", encoding="utf-8")
    rel = filepath.relative_to(ROOT).as_posix()
    print(f"💾 Saved: {rel}")
    return rel


def commit_push(rels, msg_default):
    for rel in rels:
        run(["git", "add", rel])
    msg = input(f"💬 Commit message [{msg_default}]: ").strip() or msg_default
    run(["git", "commit", "-m", msg])
    print("⬆️  Pushing to GitHub...")
    run(["git", "push"])
    remote = run(["git", "remote", "get-url", "origin"])
    remote = remote.replace("git@github.com:", "https://github.com/").removesuffix(".git")
    branch = run(["git", "rev-parse", "--abbrev-ref", "HEAD"])
    solution_rels = [r for r in rels if not r.endswith("README.md")]
    print("\n🎉 Done! Links for your LeetCode notes:")
    for rel in solution_rels:
        print(f"  {remote}/blob/{branch}/{urllib.request.quote(rel)}")


def single_mode(session):
    print("🔍 Fetching your latest accepted submission...")
    sub = fetch_accepted(session, 1)[0]
    number = question_number(sub["title_slug"])
    print(f"\n✅ Latest Accepted: {f'{number}. ' if number else ''}{sub['title']}  [{sub['lang']}]")
    if input("Save this one? (y/n): ").strip().lower() != "y":
        print("Aborted — nothing saved.")
        return
    topic, pattern = pick_topic_and_pattern()
    rel = save_file(sub, topic, pattern, ask_name=True)
    if rel:
        readme_rels = update_readmes([topic])
        where = f"{topic}/{pattern}" if pattern else topic
        commit_push([rel] + readme_rels, f"add {Path(rel).stem} ({where})")


def bulk_mode(session, count):
    print(f"🔍 Fetching your last {count} accepted problems...")
    subs = fetch_accepted(session, count)

    print(f"\n✅ Found {len(subs)} accepted problems (most recent first):")
    for i, s in enumerate(subs, 1):
        print(f"  {i:2d}) {s['title']}  [{s['lang']}]")

    skip = input("\nEnter numbers to SKIP (comma separated), or press Enter to save all: ").strip()
    if skip:
        skip_idx = {int(x) for x in skip.replace(" ", "").split(",") if x.isdigit()}
        subs = [s for i, s in enumerate(subs, 1) if i not in skip_idx]
    if not subs:
        print("Nothing left to save.")
        return

    topic, pattern = pick_topic_and_pattern()
    where = f"{topic}/{pattern}" if pattern else topic
    print(f"\nSaving {len(subs)} files into {where}/ (auto filenames)...\n")

    rels = []
    for s in subs:
        rel = save_file(s, topic, pattern, ask_name=False)
        if rel:
            rels.append(rel)
    if not rels:
        print("Nothing saved.")
        return
    readme_rels = update_readmes([topic])
    commit_push(rels + readme_rels, f"add {len(rels)} problems ({where})")


def readme_mode():
    """Rebuild every topic README + root stats, then optionally commit & push."""
    print("🔄 Rebuilding all READMEs...")
    rels = update_readmes(list_dirs(ROOT))
    if input("\nCommit & push these README updates? (y/n): ").strip().lower() == "y":
        for rel in rels:
            run(["git", "add", rel])
        run(["git", "commit", "-m", "docs: rebuild READMEs"])
        run(["git", "push"])
        print("⬆️  Pushed.")
    else:
        print("Left as local changes (not committed).")


def main():
    if len(sys.argv) > 1 and sys.argv[1] == "readme":
        readme_mode()
        return
    session = read_session()
    if len(sys.argv) > 1 and sys.argv[1].isdigit():
        bulk_mode(session, int(sys.argv[1]))
    else:
        single_mode(session)


if __name__ == "__main__":
    main()