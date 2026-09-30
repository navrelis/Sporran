#!/usr/bin/env python3
"""Draws the Sporran branding: CurseForge avatar, 400px avatar, wide banner and a 128px mod icon.

The motif is one Minecraft-style block cut in two: the left face is glowing, freshly forged
metal (NeoForge), the right face is woven teal-and-cream cloth (Fabric), and the halves are
stitched together down the front edge and across the top. A needle and thread finish the seam,
sparks rise off the hot side. It reads as "NeoForge mods, running on Fabric".

Everything is procedural pixel art drawn cell by cell by this script: it uses no external
images and no external or system fonts (the two pixel fonts are hand-built bitmaps defined
below). There is no randomness beyond a fixed integer hash, so every run writes byte-identical
PNGs. Pillow is a dev-time tool only.

Usage:  uv run --with pillow tools/icon/make_logo.py [--preview DIR]
Writes: branding/logo.png       512x512 avatar (64x64 grid x8)
        branding/logo-400.png   400x400 avatar (64x64 grid x6, 8px transparent margin)
        branding/banner.png     1600x400 banner
        branding/icon-128.png   128x128 mod icon (32x32 grid x4; pixel-exact at 32px)
        DIR/contact-sheet.png   optional: avatar at 400/128/64/32, icon, banner at 100%/50%
"""
import argparse
from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / "branding"


def hexc(s):
    return tuple(int(s[i:i + 2], 16) for i in (1, 3, 5))


def mix(a, b, t):
    t = max(0.0, min(1.0, t))
    return tuple(round(a[i] + (b[i] - a[i]) * t) for i in range(3))


def h01(x, y, seed=0):
    n = (x * 374761393 + y * 668265263 + seed * 982451653) & 0xFFFFFFFF
    n = ((n ^ (n >> 13)) * 1274126177) & 0xFFFFFFFF
    n ^= n >> 16
    return n / 4294967296.0


BAYER4 = ((0, 8, 2, 10), (12, 4, 14, 6), (3, 11, 1, 9), (15, 7, 13, 5))


def bayer(x, y):
    return (BAYER4[y % 4][x % 4] + 0.5) / 16.0


# ---- palette ----------------------------------------------------------------
INK = hexc("#120b0a")
FORGE = [hexc(c) for c in ("#3d1106", "#7c230b", "#b8420f", "#e86f17", "#ff9f2e", "#ffd35e", "#fff4c4")]
TEAL = [hexc(c) for c in ("#0b2230", "#134159", "#1c6a80", "#2a98a3", "#63c9c0", "#b5ece2")]
CREAM = [hexc(c) for c in ("#6e5f47", "#a8966f", "#d8c89f", "#f4e8c6", "#fffaea")]
STEEL = [hexc(c) for c in ("#2c3038", "#565d6b", "#8f97a6", "#c9cfd9", "#f2f4f8")]
STITCH = hexc("#fff3b8")
SEAM = hexc("#22130e")


# ---- grid -------------------------------------------------------------------
class Grid:
    def __init__(self, w, h):
        self.w, self.h = w, h
        self.c = [[None] * w for _ in range(h)]

    def get(self, x, y):
        if 0 <= x < self.w and 0 <= y < self.h:
            return self.c[y][x]
        return None

    def set(self, x, y, col):
        if 0 <= x < self.w and 0 <= y < self.h:
            self.c[y][x] = col

    def blit(self, o, ox, oy):
        for y in range(o.h):
            for x in range(o.w):
                v = o.c[y][x]
                if v is not None:
                    self.set(ox + x, oy + y, v)

    def outline(self, col, diag=False):
        nb = [(1, 0), (-1, 0), (0, 1), (0, -1)]
        if diag:
            nb += [(1, 1), (1, -1), (-1, 1), (-1, -1)]
        add = []
        for y in range(self.h):
            for x in range(self.w):
                if self.c[y][x] is None and any(self.get(x + dx, y + dy) is not None for dx, dy in nb):
                    add.append((x, y))
        for x, y in add:
            self.c[y][x] = col

    def image(self, scale):
        img = Image.new("RGBA", (self.w, self.h), (0, 0, 0, 0))
        img.putdata([(c[0], c[1], c[2], 255) if c else (0, 0, 0, 0) for row in self.c for c in row])
        return img.resize((self.w * scale, self.h * scale), Image.NEAREST)


def pal(p, level, x, y):
    """Pick from palette p at a fractional level, ordered-dithered between neighbours."""
    level = max(0.0, min(len(p) - 1.0, level))
    lo = int(level)
    if lo >= len(p) - 1:
        return p[-1]
    return p[lo + 1] if (level - lo) > bayer(x, y) else p[lo]


# ---- textures (texel coordinates u, v on an s x s face) ---------------------
def forge_tex(u, v, s, boost=0.0, noise=0.3, strata=4):
    """Glowing hot metal: white-hot core, cooler rim, hammered strata."""
    cx = (u + 0.5) / s - 0.5
    cy = (v + 0.5) / s - 0.5
    d = max(abs(cx), abs(cy))
    heat = 4.9 - d * 6.0 + boost
    if strata and v % strata == strata - 1 and 0 < u < s - 1:
        heat -= 0.8
    heat += (h01(u, v, 7) - 0.5) * noise
    if u == 0 or v == 0 or u == s - 1 or v == s - 1:
        heat = min(heat - 0.6, 2.6 + boost)
    return pal(FORGE, heat, u, v)


CLOTH_DARK = hexc("#081821")


def cloth_tex(u, v, lift=0, w=2, dark=0.0, sub=None):
    """Plain basket weave: teal weft over cream warp in w x w blocks.
    With w == 1, sub = (su, sv) shades inside a 2x2-cell texel instead."""
    bu, bv = u // w, v // w
    if w == 1 and sub is not None:
        ru, rv = sub
    else:
        ru, rv = u % w, v % w
    if (bu + bv) % 2 == 0:
        col = TEAL[3 + lift] if rv == 0 else TEAL[2 + lift]
    else:
        col = CREAM[2 + lift] if ru == 0 else CREAM[1 + lift]
    return mix(col, CLOTH_DARK, dark)


def line(g, x0, y0, x1, y1, col):
    dx, dy = abs(x1 - x0), -abs(y1 - y0)
    sx, sy = (1 if x0 < x1 else -1), (1 if y0 < y1 else -1)
    err = dx + dy
    while True:
        g.set(x0, y0, col)
        if x0 == x1 and y0 == y1:
            return
        e2 = 2 * err
        if e2 >= dy:
            err += dy
            x0 += sx
        if e2 <= dx:
            err += dx
            y0 += sy


# =============================================================================
# The motif: one Minecraft block, left face forged hot metal, right face woven
# cloth, stitched together down the front edge and across the top.
# =============================================================================
def cube(s, weave=2, period=5, chevron=True, noise=0.3, strata=4, stitch_h=2):
    g = Grid(4 * s, 4 * s)
    for px in range(4 * s):
        half = px if px < 2 * s else 4 * s - 1 - px
        k = half // 2
        t0, t1 = s - 1 - k, s + k
        left = px < 2 * s
        for py in range(t0, t1 + 1):                       # top face
            xx = px + 0.5 - 2 * s
            yy = py + 0.5
            a = min(max(int((yy + xx / 2) / 2), 0), s - 1)
            b = min(max(int((yy - xx / 2) / 2), 0), s - 1)
            if left:
                g.set(px, py, forge_tex(a, b, s, boost=2.0, noise=noise, strata=0 if weave == 1 else strata))
            else:
                if weave == 1:   # too small for a weave: threads running one way
                    g.set(px, py, CREAM[3] if a % 2 == 0 else TEAL[4])
                else:
                    g.set(px, py, cloth_tex(b, a, lift=2, w=weave))
        for py in range(t1 + 1, t1 + 1 + 2 * s):           # side faces
            v = (py - t1 - 1) // 2
            if left:
                g.set(px, py, forge_tex(px // 2, v, s, boost=-0.9, noise=noise, strata=strata))
            else:
                sub = ((px - 2 * s) % 2, (py - t1 - 1) % 2)
                g.set(px, py, cloth_tex((px - 2 * s) // 2, v, lift=0, w=weave, dark=0.25, sub=sub))
        # light rim along the top edge of each side face
        if px not in (2 * s - 1, 2 * s):
            g.set(px, t1 + 1, FORGE[5] if left else TEAL[4])
    # the seam: dark gap down the middle, bright stitches across it
    m = 2 * s
    for py in range(4 * s):
        for px in (m - 1, m):
            if g.get(px, py) is not None:
                g.set(px, py, SEAM)

    def put(px, py):
        if g.get(px, py) is not None:
            g.set(px, py, STITCH)

    if chevron:
        for y0 in range(2, 2 * s - 1, period):             # top: arms run down-and-out
            for dx, dy in ((-4, 1), (-3, 1), (-2, 0), (-1, 0), (0, 0), (1, 0), (2, 1), (3, 1)):
                put(m + dx, y0 + dy)
        for y0 in range(2 * s + 2, 4 * s - 1, period):     # sides: arms run up-and-out
            for dx, dy in ((-4, -1), (-3, -1), (-2, 0), (-1, 0), (0, 0), (1, 0), (2, -1), (3, -1)):
                put(m + dx, y0 + dy)
    else:
        for py in range(1, 4 * s - 1):
            if py % period < stitch_h:
                for px in range(m - 2, m + 2):
                    put(px, py)
    return g


def motif(n):
    """Transparent n x n motif grid (n = 64 for the avatar and banner, 32 for the icon)."""
    g = Grid(n, n)
    if n == 64:
        s, ox, oy = 12, 8, 10
        c = cube(s)
    else:
        s, ox, oy = 7, 2, 2
        c = cube(s, weave=1, period=3, chevron=False, noise=0.0, strata=3, stitch_h=1)
    c2 = Grid(c.w + 2, c.h + 2)
    c2.blit(c, 1, 1)
    c2.outline(INK)
    g.blit(c2, ox - 1, oy - 1)
    if n == 64:
        # sparks rising off the hot side
        for x, y, i in ((10, 12, 5), (6, 17, 4), (18, 5, 4), (12, 3, 5), (4, 10, 3), (21, 9, 3)):
            g.set(x, y, FORGE[i])
        for cx, cy in ((13, 8),):
            g.set(cx, cy, FORGE[6])
            for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                g.set(cx + dx, cy + dy, FORGE[4])
        # a needle stitching into the cloth side, thread trailing from its eye
        pts = [(55, 4), (50, 4), (46, 5), (42, 6), (38, 8), (35, 9), (33, 10)]
        for (x0, y0), (x1, y1) in zip(pts, pts[1:]):
            line(g, x0, y0, x1, y1, TEAL[4])
        for i in range(13):
            g.set(43 + i, 16 - i, STEEL[4] if i > 1 else STEEL[2])
            if 0 < i < 12:
                g.set(44 + i, 16 - i, STEEL[1])
    return g


# =============================================================================
# Backgrounds
# =============================================================================
BG_WARM = hexc("#23140f")
BG_COOL = hexc("#0f1c26")


def rounded_inside(x, y, w, h, r):
    cx = min(max(x + 0.5, r), w - r)
    cy = min(max(y + 0.5, r), h - r)
    return (x + 0.5 - cx) ** 2 + (y + 0.5 - cy) ** 2 <= r * r


def avatar_bg(n, radius):
    g = Grid(n, n)
    for y in range(n):
        for x in range(n):
            if not rounded_inside(x, y, n, n, radius):
                continue
            t = (x + 0.5) / n
            col = mix(BG_WARM, BG_COOL, (t - 0.3) / 0.4)
            # soft glow behind the motif
            d = ((x + 0.5 - n / 2) ** 2 + (y + 0.5 - n * 0.55) ** 2) ** 0.5 / (n / 2)
            col = mix(col, mix(hexc("#4a2a1a"), hexc("#1d3a48"), t), max(0.0, 0.55 - d) * 1.2)
            g.set(x, y, col)
    # inner rim and ink border
    for y in range(n):
        for x in range(n):
            if g.get(x, y) is None:
                continue
            edge = any(g.get(x + dx, y + dy) is None for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)))
            if edge:
                g.set(x, y, INK)
    for y in range(n):
        for x in range(n):
            c = g.get(x, y)
            if c is None or c == INK:
                continue
            nb = [g.get(x + dx, y + dy) for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1))]
            if INK in nb:
                t = (x + 0.5) / n
                g.set(x, y, mix(hexc("#5a3422"), hexc("#23485a"), (t - 0.3) / 0.4))
    return g


def avatar(motif_fn, n=64):
    """Dark rounded square, warm on the left and cool on the right, with the motif on top."""
    g = avatar_bg(n, 9 if n == 64 else 5)
    m = motif_fn(n)
    g.blit(m, 0, 0)
    return g


# =============================================================================
# Pixel fonts (hand-built)
# =============================================================================
# Regular: x-height 7, cap/ascender 10, descender 3.  kinds: x = x-height only,
# a = ascender (10 rows from cap line), d = descender (10 rows from x-height).
REG = {
    "a": ("x", [".###.", "....#", "....#", ".####", "#...#", "#...#", ".####"]),
    "b": ("a", ["#....", "#....", "#....", "####.", "#...#", "#...#", "#...#", "#...#", "#...#", "####."]),
    "c": ("x", [".###.", "#...#", "#....", "#....", "#....", "#...#", ".###."]),
    "d": ("a", ["....#", "....#", "....#", ".####", "#...#", "#...#", "#...#", "#...#", "#...#", ".####"]),
    "e": ("x", [".###.", "#...#", "#...#", "#####", "#....", "#....", ".####"]),
    "f": ("a", ["..##", ".#..", ".#..", "####", ".#..", ".#..", ".#..", ".#..", ".#..", ".#.."]),
    "g": ("d", [".####", "#...#", "#...#", "#...#", "#...#", "#...#", ".####", "....#", "....#", "####."]),
    "i": ("a", [".", "#", ".", "#", "#", "#", "#", "#", "#", "#"]),
    "m": ("x", ["###.##.", "#..#..#", "#..#..#", "#..#..#", "#..#..#", "#..#..#", "#..#..#"]),
    "n": ("x", ["####.", "#...#", "#...#", "#...#", "#...#", "#...#", "#...#"]),
    "o": ("x", [".###.", "#...#", "#...#", "#...#", "#...#", "#...#", ".###."]),
    "p": ("d", ["####.", "#...#", "#...#", "#...#", "#...#", "#...#", "####.", "#....", "#....", "#...."]),
    "r": ("x", ["#.##", "##..", "#...", "#...", "#...", "#...", "#..."]),
    "s": ("x", [".####", "#....", "#....", ".###.", "....#", "....#", "####."]),
    "t": ("a", ["....", ".#..", ".#..", "####", ".#..", ".#..", ".#..", ".#..", ".#..", "..##"]),
    "F": ("a", ["#####", "#....", "#....", "#....", "####.", "#....", "#....", "#....", "#....", "#...."]),
    "M": ("a", ["#.....#", "##...##", "#.#.#.#", "#..#..#", "#.....#", "#.....#", "#.....#", "#.....#", "#.....#", "#.....#"]),
    "N": ("a", ["#....#", "##...#", "##...#", "#.#..#", "#.#..#", "#..#.#", "#..#.#", "#...##", "#...##", "#....#"]),
    "S": ("a", [".###.", "#...#", "#....", "#....", ".###.", "....#", "....#", "....#", "#...#", ".###."]),
    "1": ("a", [".#", "##", ".#", ".#", ".#", ".#", ".#", ".#", ".#", ".#"]),
    "2": ("a", [".###.", "#...#", "....#", "....#", "...#.", "..#..", ".#...", "#....", "#....", "#####"]),
    ".": ("a", [".", ".", ".", ".", ".", ".", ".", ".", ".", "#"]),
}
REG_METRICS = dict(asc=3, x=7, desc=3, gap=1, space=3)

# Bold title: stroke 2, x-height 10, cap 14, descender 4.
BOLD = {
    "S": ("a", ["..#####..", ".#######.", "###...###", "##.....##", "##.......", "###......", ".######..",
                "..######.", "......###", ".......##", "##.....##", "###...###", ".#######.", "..#####.."]),
    "o": ("x", ["..#####..", ".#######.", "###...###", "##.....##", "##.....##", "##.....##", "##.....##",
                "###...###", ".#######.", "..#####.."]),
    "n": ("x", ["##.####..", "########.", "###...###", "##.....##", "##.....##", "##.....##", "##.....##",
                "##.....##", "##.....##", "##.....##"]),
    "p": ("d", ["##.####..", "########.", "###...###", "##.....##", "##.....##", "##.....##", "##.....##",
                "###...###", "########.", "##.####..", "##.......", "##.......", "##.......", "##......."]),
    "r": ("x", ["##..####", "##.#####", "#####...", "####....", "###.....", "##......", "##......",
                "##......", "##......", "##......"]),
    "a": ("x", [".######..", ".#######.", "......###", ".......##", "..#######", ".########", "###....##",
                "##.....##", "###...###", ".#####.##"]),
}
BOLD_METRICS = dict(asc=4, x=10, desc=4, gap=2, space=5)


def glyph(font, metrics, ch):
    kind, rows = font[ch]
    w = len(rows[0])
    assert all(len(r) == w for r in rows), ch
    asc, xh, desc = metrics["asc"], metrics["x"], metrics["desc"]
    top = {"x": asc, "a": 0, "d": asc}[kind]
    full = [["."] * w for _ in range(asc + xh + desc)]
    for r, row in enumerate(rows):
        for c, p in enumerate(row):
            if p == "#":
                full[top + r][c] = "#"
    return full


NO_KERN = set(".0123456789")


def layout(text, font, metrics, max_tighten=1):
    """Return list of (glyph rows, x) with simple optical kerning."""
    placed = []
    x = 0
    prev = None
    prev_ch = ""
    for ch in text:
        if ch == " ":
            x += metrics["space"]
            prev = None
            continue
        gl = glyph(font, metrics, ch)
        w = len(gl[0])
        if prev is not None and not (set(ch + prev_ch) & NO_KERN):
            pg, px_ = prev
            pw = len(pg[0])
            nx = px_ + pw + metrics["gap"]
            for _ in range(max_tighten):
                cand = nx - 1
                if min_gap(pg, px_, gl, cand) >= metrics["gap"]:
                    nx = cand
                else:
                    break
            x = nx
        elif prev is not None:
            x = prev[1] + len(prev[0][0]) + metrics["gap"]
        placed.append((gl, x))
        prev = (gl, x)
        prev_ch = ch
        x += w + metrics["gap"]
    width = max(xx + len(g[0]) for g, xx in placed)
    return placed, width


def min_gap(ga, xa, gb, xb):
    h = len(ga)
    best = 99
    for y in range(h):
        rb = [c for c, p in enumerate(gb[y]) if p == "#"]
        if not rb:
            continue
        lb = xb + rb[0]
        for yy in (y - 1, y, y + 1):
            if 0 <= yy < h:
                ra = [c for c, p in enumerate(ga[yy]) if p == "#"]
                if ra:
                    best = min(best, lb - (xa + ra[-1]) - 1)
    return best


def text_grid(text, font, metrics, color_fn, pad=2):
    placed, width = layout(text, font, metrics)
    h = len(placed[0][0])
    g = Grid(width + 2 * pad, h + 2 * pad)
    for gl, x in placed:
        for y, row in enumerate(gl):
            for c, p in enumerate(row):
                if p == "#":
                    g.set(pad + x + c, pad + y, color_fn(x + c, y, width, h))
    return g


def shadowed(g, col, dx, dy):
    out = Grid(g.w, g.h)
    for y in range(g.h):
        for x in range(g.w):
            if g.get(x, y) is not None:
                out.set(x + dx, y + dy, col)
    out.blit(g, 0, 0)
    return out


# =============================================================================
# Banner
# =============================================================================
def banner_bg(W, H):
    g = Grid(W, H)
    for y in range(H):
        for x in range(W):
            t = x / (W - 1)
            col = mix(hexc("#1e120e"), hexc("#0d1821"), (t - 0.15) / 0.7)
            # faint embers on the far left, rising from the bottom
            if t < 0.26:
                dens = (0.26 - t) / 0.26 * (0.3 + 0.7 * y / H)
                if h01(x, y, 3) < 0.03 * dens:
                    col = mix(col, FORGE[3 + int(h01(x, y, 4) * 2)], 0.3 + 0.35 * dens)
            # faint weave on the right
            if t > 0.5:
                a = min(1.0, (t - 0.5) / 0.4)
                if (x // 2 + y // 2) % 2 == 0:
                    col = mix(col, hexc("#1f4250"), (0.32 if y % 2 == 0 else 0.18) * a)
                else:
                    col = mix(col, hexc("#33413c"), (0.28 if x % 2 == 0 else 0.14) * a)
            vy = abs(y + 0.5 - H / 2) / (H / 2)
            col = mix(col, hexc("#07090b"), max(0.0, vy - 0.55) * 0.6)
            g.set(x, y, col)
    for x in range(W):
        t = x / (W - 1)
        g.set(x, 0, INK)
        g.set(x, H - 1, INK)
        g.set(x, 1, mix(hexc("#5a3422"), hexc("#23485a"), (t - 0.2) / 0.6))
        g.set(x, H - 2, mix(hexc("#3a2218"), hexc("#172f3b"), (t - 0.2) / 0.6))
    for y in range(H):
        g.set(0, y, INK)
        g.set(W - 1, y, INK)
        if 0 < y < H - 1:
            g.set(1, y, hexc("#5a3422"))
            g.set(W - 2, y, hexc("#23485a"))
    # soft glow behind the motif
    cx, cy = 37, 41
    for y in range(2, H - 2):
        for x in range(2, 90):
            d = ((x - cx) ** 2 + (y - cy) ** 2) ** 0.5 / 36
            if d < 1:
                g.set(x, y, mix(g.get(x, y), hexc("#3a2a24"), (1 - d) * 0.55))
    return g


TITLE_WARM = hexc("#ffdca0")
TITLE_COOL = hexc("#c8f0e8")


def title_col(x, y, w, h):
    base = mix(TITLE_WARM, TITLE_COOL, x / w)
    top = mix(base, hexc("#ffffff"), 0.45)
    bot = mix(base, hexc("#a88b6c"), 0.35)
    return mix(top, bot, (y - 2) / 12)


def banner(motif_fn):
    img = banner_bg(320, 80).image(5)
    MS = 6
    img.alpha_composite(motif_fn(64).image(MS), (16, 8))
    tx = 404
    TS, GS, VS = 11, 6, 4
    # title
    tg = text_grid("Sporran", BOLD, BOLD_METRICS, title_col, pad=2)
    tg.outline(INK)
    tg = shadowed(tg, hexc("#0a0706"), 1, 1)
    title_top = 44
    img.alpha_composite(tg.image(TS), (tx - 2 * TS, title_top - 2 * TS))
    base_title = title_top + 14 * TS
    # tagline, colour-coded
    words = [("NeoForge", FORGE[4]), ("mods on", hexc("#ddd6cc")), ("Fabric", TEAL[4])]
    x = 0
    tag_top = base_title + 4 * TS + 14
    for word, col in words:
        wg = text_grid(word, REG, REG_METRICS, lambda *_a, c=col: c, pad=1)
        wg = shadowed(wg, hexc("#050404"), 1, 1)
        img.alpha_composite(wg.image(GS), (tx + x * GS - GS, tag_top - GS))
        x += wg.w - 2 + REG_METRICS["space"]
    tag_base = tag_top + 10 * GS
    # version pill, bottom right, sitting on the tagline baseline
    vg = text_grid("Minecraft 1.21.1", REG, REG_METRICS, lambda *_a: hexc("#a39c94"), pad=0)
    pw, ph = vg.w + 8, 10 + 6
    pill = Grid(pw, ph)
    for yy in range(ph):
        for xx in range(pw):
            if rounded_inside(xx, yy, pw, ph, 4):
                pill.set(xx, yy, hexc("#131c22"))
    for yy in range(ph):
        for xx in range(pw):
            if pill.get(xx, yy) and any(pill.get(xx + a, yy + b) is None for a, b in ((1, 0), (-1, 0), (0, 1), (0, -1))):
                pill.set(xx, yy, hexc("#34464f"))
    for yy in range(vg.h):
        for xx in range(vg.w):
            if vg.get(xx, yy):
                pill.set(xx + 4, yy + 3, vg.get(xx, yy))
    px0 = 1600 - 40 - pw * VS
    img.alpha_composite(pill.image(VS), (px0, tag_base - 13 * VS + 0))
    return img


# =============================================================================
# Output
# =============================================================================
def logo_images():
    av = avatar(motif, 64)
    logo = av.image(8)
    l400 = Image.new("RGBA", (400, 400), (0, 0, 0, 0))
    l400.alpha_composite(av.image(6), (8, 8))
    icon = motif(32).image(4)
    return logo, l400, icon


def contact_sheet(logo, l400, icon, ban):
    W = 1680
    sheet = Image.new("RGBA", (W, 1100), (40, 40, 44, 255))
    sheet.alpha_composite(Image.new("RGBA", (W // 2, 440), (236, 236, 232, 255)), (W // 2, 0))
    sheet.alpha_composite(l400, (20, 20))
    for x0 in (440, W // 2 + 20):
        x = x0
        for sz in (128, 64, 32):
            sheet.alpha_composite(logo.resize((sz, sz), Image.LANCZOS), (x, 20))
            sheet.alpha_composite(icon.resize((sz, sz), Image.LANCZOS) if sz != 128 else icon, (x, 200))
            x += sz + 20
    sheet.alpha_composite(ban, (40, 440))
    sheet.alpha_composite(ban.resize((800, 200), Image.LANCZOS), (40, 860))
    sheet.alpha_composite(ban.resize((400, 100), Image.LANCZOS), (880, 860))
    return sheet


def main():
    ap = argparse.ArgumentParser(description=__doc__.splitlines()[1])
    ap.add_argument("--preview", type=Path, help="also write a contact sheet into this directory")
    args = ap.parse_args()
    logo, l400, icon = logo_images()
    ban = banner(motif)
    OUT.mkdir(parents=True, exist_ok=True)
    logo.save(OUT / "logo.png", optimize=True)
    l400.save(OUT / "logo-400.png", optimize=True)
    ban.save(OUT / "banner.png", optimize=True)
    icon.save(OUT / "icon-128.png", optimize=True)
    if args.preview:
        args.preview.mkdir(parents=True, exist_ok=True)
        contact_sheet(logo, l400, icon, ban).save(args.preview / "contact-sheet.png")


if __name__ == "__main__":
    main()
