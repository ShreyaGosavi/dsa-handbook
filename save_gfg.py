#!/usr/bin/env python3
"""
save_gfg.py — GfG problem saver for dsa-handbook (mixed into the same
topic/pattern folders as your LeetCode solutions).

Why this script is different from save.py:
GeeksforGeeks doesn't expose your submitted CODE through any API, only
the list of problems you've solved (title + link). So this script:
  1. Auto-fetches your full solved list from GfG (no login needed)
  2. Cross-checks it against files already in this repo, so it only
     shows you problems that AREN'T saved yet
  3. For each one you pick: asks topic + pattern (same folders as
     LeetCode), opens an editor for you to paste your code, saves it,
     and pushes — updating the same README index tables

Usage:
  python3 save_gfg.py            -> interactive: shows unsaved problems, pick some
  python3 save_gfg.py list       -> just print unsaved problems, don't save anything

Setup (one time):
  Put your GfG username in a file named .gfg_username in the repo root.
  (Your username is the part after /user/ in your GfG profile URL.)
"""

import json
import os
import re
import subprocess
import sys
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parent
USERNAME_FILE = ROOT / ".gfg_username"
SKIP_DIRS = {"templates", "docs", ".git", "__pycache__"}
MARK_START = "<!-- AUTO:START -->"
MARK_END = "<!-- AUTO:END -->"

# Same extension choices as save.py, GfG mostly used for C++/Java/Python
EXT_CHOICES = ["java", "cpp", "py", "c", "js"]

COMMENT_STYLES = {
    "java": ("/**", " * ", " */"), "cpp": ("/**", " * ", " */"),
    "c": ("/**", " * ", " */"), "js": ("/**", " * ", " */"),
    "py": ('"""', "", '"""'),
}

API_URL = "https://gfg-stats.tashif.codes/{username}/solved-problems"
FALLBACK_API_URL = "https://geeks-for-geeks-api.vercel.app/{username}"


def die(msg):
    print(f"\n❌ {msg}")
    sys.exit(1)


def read_username():
    if not USERNAME_FILE.exists():
        username = input("GfG username (from your profile URL): ").strip()
        if not username:
            die("No username given.")
        USERNAME_FILE.write_text(username + "\n", encoding="utf-8")
        return username
    return USERNAME_FILE.read_text().strip()


def _fetch_json(url):
    req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
    with urllib.request.urlopen(req, timeout=20) as r:
        return json.loads(r.read())


def fetch_solved(username):
    # Primary: uses GfG's current JSON endpoints (new profile page format)
    try:
        data = _fetch_json(API_URL.format(username=username))
        problems = []
        for p in data.get("problems", []):
            title = (p.get("question") or "").strip()
            link = (p.get("questionUrl") or p.get("link") or "").strip()
            difficulty = (p.get("difficulty") or "unknown").strip().lower()
            if title and link:
                problems.append({"title": title, "link": link, "difficulty": difficulty})
        if problems:
            return problems
    except Exception:
        pass  # fall through to the fallback API

    # Fallback: older API, scrapes the legacy profile page (may be empty for newer profiles)
    try:
        data = _fetch_json(FALLBACK_API_URL.format(username=username))
        stats = data.get("solvedStats", {})
        problems = []
        for difficulty, bucket in stats.items():
            for q in bucket.get("questions", []):
                title = q.get("question", "").strip()
                link = q.get("link", "").strip()
                if title and link:
                    problems.append({"title": title, "link": link, "difficulty": difficulty})
        if problems:
            return problems
    except Exception:
        pass

    die("Could not fetch GfG profile from either API. Check your username in .gfg_username, "
        "or both public APIs may be temporarily down — try again shortly.")


def slug_from_link(link):
    """GfG links often end in /problems/<slug>/1 — the trailing /1 is a
    variant id, not part of the slug, so strip pure-numeric trailing parts."""
    parts = [p for p in link.rstrip("/").split("/") if p]
    while parts and parts[-1].split("?")[0].isdigit():
        parts.pop()
    return parts[-1].split("?")[0] if parts else link


def already_saved_slugs():
    """Scan every file in the repo for GfG links already present, so we don't
    show problems that were already saved."""
    slugs = set()
    pattern = re.compile(r"geeksforgeeks\.org/problems/([a-zA-Z0-9\-]+)")
    for path in ROOT.rglob("*"):
        if not path.is_file() or path.suffix not in {".java", ".cpp", ".py", ".c", ".js", ".sql", ".md"}:
            continue
        if any(part in SKIP_DIRS or part.startswith(".") for part in path.parts):
            continue
        try:
            text = path.read_text(encoding="utf-8", errors="ignore")
        except Exception:
            continue
        for m in pattern.finditer(text):
            slugs.add(m.group(1))
    return slugs


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
        c = input("Pick a number: ").strip()
        if c.isdigit():
            n = int(c)
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
    (parent / name).mkdir(exist_ok=True)
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


def pick_ext():
    return choose("💻 Language?", EXT_CHOICES, [])


def slugify(title):
    s = re.sub(r"[^a-zA-Z0-9]+", "-", title.strip().lower()).strip("-")
    return s


def build_header(ext, title, link, difficulty, topic, pattern):
    opener, prefix, closer = COMMENT_STYLES.get(ext, ("//", "// ", "//"))
    where = f"{topic} -> {pattern}" if pattern else topic
    lines = [
        opener,
        f"{prefix}{title}",
        f"{prefix}{link}",
        f"{prefix}Source: GeeksforGeeks ({difficulty.title()})",
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


def open_editor(filepath):
    editor_cmd = None
    if subprocess.run(["which", "code"], capture_output=True).returncode == 0:
        editor_cmd = ["code", "--wait", str(filepath)]
    else:
        editor_cmd = [os.environ.get("EDITOR", "nano"), str(filepath)]
    subprocess.run(editor_cmd)


# ---------------------------------------------------------------- README auto-gen (shared logic with save.py)

def solution_files(directory):
    exts = {".java", ".cpp", ".py", ".c", ".js"}
    return sorted(p for p in directory.iterdir() if p.is_file() and p.suffix in exts)


def parse_solution(path):
    number, title, url = None, None, None
    try:
        for line in path.read_text(encoding="utf-8", errors="ignore").splitlines()[:12]:
            clean = line.strip().lstrip("/*#\"'=- ").strip()
            m = re.match(r"^(\d+)\.\s+(.+)$", clean)
            if m and not title:
                number, title = m.group(1), m.group(2).strip()
            if ("leetcode.com/problems/" in clean or "geeksforgeeks.org/problems/" in clean) and not url:
                u = re.search(r"https?://\S+", clean)
                url = u.group(0).rstrip("/") + "/" if u else None
            if not title and clean and not clean.startswith(("http", "Pattern:", "Source:", "Approach", "Time:")):
                title = clean
    except Exception:
        pass
    if not title:
        title = path.stem.replace("-", " ").title()
    return number, title, url or ""


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
        safe_title = title.replace("|", "\\|")
        cell = f"[{safe_title}]({url})" if url else safe_title
        lines.append(f"| {num} | {cell} | [{f.name}]({link_path}) |")
    lines.append("")
    return lines


def topic_counts(topic_dir):
    count = len(solution_files(topic_dir))
    for sub in list_dirs(topic_dir):
        count += len(solution_files(topic_dir / sub))
    return count


def inject(path, generated):
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
    lines = ["## Solved problems", ""]
    direct = solution_files(topic_dir)
    total = len(direct)
    if direct:
        lines += problem_table(direct, topic_dir)
    for pattern in list_dirs(topic_dir):
        files = solution_files(topic_dir / pattern)
        if not files:
            continue
        total += len(files)
        lines += problem_table(files, topic_dir, indent_heading=pattern)
    if total == 0:
        lines.append("_No problems saved yet._")
        lines.append("")
    lines.append(f"**Total: {total}**")
    inject(topic_dir / "README.md", "\n".join(lines))
    return topic_dir / "README.md"


def gen_root_stats():
    lines = ["## Progress", "", "| Topic | Patterns | Solved |", "|-------|----------|--------|"]
    grand = 0
    for topic in list_dirs(ROOT):
        topic_dir = ROOT / topic
        n = topic_counts(topic_dir)
        grand += n
        patterns = ", ".join(list_dirs(topic_dir)) or "—"
        lines.append(f"| [{topic}]({urllib.request.quote(topic)}) | {patterns} | {n} |")
    lines.append(f"| **Total** | | **{grand}** |")
    inject(ROOT / "README.md", "\n".join(lines))
    return ROOT / "README.md"


def update_readmes(topics):
    paths = [gen_topic_readme(t) for t in sorted(set(topics))]
    paths.append(gen_root_stats())
    rels = [p.relative_to(ROOT).as_posix() for p in paths]
    for r in rels:
        print(f"📄 Updated: {r}")
    return rels


# ---------------------------------------------------------------- main flow

def save_one(problem):
    print(f"\n➡️  {problem['title']}  [{problem['difficulty'].title()}]")
    print(f"   {problem['link']}")
    topic, pattern = pick_topic_and_pattern()
    ext = pick_ext()
    default = f"gfg-{slugify(problem['title'])}"
    name = input(f"📝 File name [{default}]: ").strip() or default
    target_dir = ROOT / topic / pattern if pattern else ROOT / topic
    filepath = target_dir / f"{name}.{ext}"

    if filepath.exists():
        if input(f"⚠️  {filepath.name} already exists. Overwrite? (y/n): ").strip().lower() != "y":
            print("⏭️  Skipped.")
            return None, None

    filepath.write_text(
        build_header(ext, problem["title"], problem["link"], problem["difficulty"], topic, pattern),
        encoding="utf-8",
    )
    print(f"📝 Paste your code into the editor that opens (save & close when done)...")
    open_editor(filepath)

    rel = filepath.relative_to(ROOT).as_posix()
    print(f"💾 Saved: {rel}")
    return rel, topic


def commit_push(rels, msg):
    for rel in rels:
        run(["git", "add", rel])
    run(["git", "commit", "-m", msg])
    print("⬆️  Pushing to GitHub...")
    run(["git", "push"])
    remote = run(["git", "remote", "get-url", "origin"])
    remote = remote.replace("git@github.com:", "https://github.com/").removesuffix(".git")
    branch = run(["git", "rev-parse", "--abbrev-ref", "HEAD"])
    print("\n🎉 Done! Pushed:")
    for rel in rels:
        if not rel.endswith("README.md"):
            print(f"  {remote}/blob/{branch}/{urllib.request.quote(rel)}")


def main():
    username = read_username()
    print(f"🔍 Fetching solved problems for '{username}' from GfG...")
    solved = fetch_solved(username)
    saved_slugs = already_saved_slugs()
    unsaved = [p for p in solved if slug_from_link(p["link"]) not in saved_slugs]

    print(f"\n✅ {len(solved)} solved on GfG, {len(unsaved)} not yet in this repo.")
    if not unsaved:
        print("Nothing new to save — you're fully synced.")
        return

    for i, p in enumerate(unsaved, 1):
        print(f"  {i:3d}) {p['title']}  [{p['difficulty'].title()}]")

    if len(sys.argv) > 1 and sys.argv[1] == "list":
        return

    pick = input("\nEnter numbers to SAVE (comma separated), or 'all': ").strip()
    if pick.lower() == "all":
        chosen = unsaved
    else:
        idx = {int(x) for x in pick.replace(" ", "").split(",") if x.isdigit()}
        chosen = [p for i, p in enumerate(unsaved, 1) if i in idx]

    if not chosen:
        print("Nothing selected.")
        return

    rels, topics = [], set()
    for p in chosen:
        rel, topic = save_one(p)
        if rel:
            rels.append(rel)
            topics.add(topic)

    if not rels:
        print("Nothing saved.")
        return

    readme_rels = update_readmes(topics)
    default_msg = f"add {len(rels)} GfG problem(s)"
    msg = input(f"💬 Commit message [{default_msg}]: ").strip() or default_msg
    commit_push(rels + readme_rels, msg)


if __name__ == "__main__":
    main()
