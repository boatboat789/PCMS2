#!/usr/bin/env python
"""
เทียบผล PCMSRegressionHarness ก่อน/หลังแก้ (A/B) — ใช้ตอนทดสอบ PERF_SEARCH บน PRD
ดูวิธีใช้เต็มใน docs/prd-verification-perf-search.md

  python -I tools/harness-ab-diff.py diff   <baseline_เก่า> <baseline_ใหม่>
  python -I tools/harness-ab-diff.py timing --old log1.txt log2.txt log3.txt --new log4.txt log5.txt log6.txt

diff   : เทียบไฟล์ baseline ทีละเคส ระดับแถว/เซลล์ (คีย์ = saleOrder+saleLine+productionOrder+typePrdRemark+typePrd+lotNo)
         exit 1 ถ้ามี "ค่าไม่ว่างถูกเปลี่ยน" หรือ "แถวเพิ่ม" (ต้องหยุดและตรวจ)
timing : หา median ms ต่อเคสจาก log หลายรอบ แล้วเทียบเก่า/ใหม่ · exit 1 ถ้าเคสไหนช้าลง > 20%
"""
import argparse
import collections
import glob
import json
import os
import re
import statistics
import sys

SLOW_RATIO = 1.20


def load_rows(path):
    with open(path, encoding="utf-8", errors="replace") as f:
        return [json.loads(line) for line in f if line.startswith("{")]


def key(r):
    return (r.get("saleOrder"), r.get("saleLine"), r.get("productionOrder"),
            r.get("typePrdRemark"), r.get("typePrd"), r.get("lotNo"))


def group(rows):
    g = collections.defaultdict(list)
    for r in rows:
        g[key(r)].append(r)
    return g


def cmd_diff(old_dir, new_dir):
    files = sorted(f for f in glob.glob(os.path.join(old_dir, "*.txt")) if not f.endswith(".actual.txt"))
    if not files:
        print("ไม่พบไฟล์ baseline ใน", old_dir)
        return 2
    tot = collections.Counter()
    filled = collections.Counter()
    problems = []
    removed_rows = set()
    for old_path in files:
        name = os.path.basename(old_path)[:-4]
        new_path = os.path.join(new_dir, os.path.basename(old_path))
        if not os.path.exists(new_path):
            print("ไม่มีไฟล์ฝั่งใหม่:", name)
            tot["missing_case_in_new"] += 1
            continue
        old, new = group(load_rows(old_path)), group(load_rows(new_path))
        for k in new:
            if k not in old:
                tot["rows_added"] += 1
                problems.append((name, "แถวเพิ่ม", k))
        for k in old:
            if k not in new:
                tot["rows_removed"] += 1
                removed_rows.add(k)
                continue
            if len(old[k]) != len(new[k]):
                tot["dup_count_changed"] += 1
                problems.append((name, "จำนวนแถวซ้ำคีย์เดียวกันเปลี่ยน", k, len(old[k]), len(new[k])))
                continue
            for a, b in zip(sorted(old[k], key=json.dumps), sorted(new[k], key=json.dumps)):
                for f, va in a.items():
                    vb = b.get(f)
                    if va == vb:
                        continue
                    if va in ("", None, "0.00") and vb not in ("", None):
                        filled[f] += 1
                        tot["cells_filled"] += 1
                    else:
                        tot["cells_changed_nonempty"] += 1
                        problems.append((name, "ค่าไม่ว่างเปลี่ยน", k, f, va, vb))
    print("== สรุป ==")
    for k in ("cells_changed_nonempty", "rows_added", "dup_count_changed", "cells_filled",
              "rows_removed", "missing_case_in_new"):
        print("  %-28s %d" % (k, tot[k]))
    if filled:
        print("  เซลล์ว่างที่มีค่า แยกตามฟิลด์:", dict(filled.most_common(12)))
    if removed_rows:
        print("== แถวที่หาย (ไม่ซ้ำ %d) — ต้องเป็น Main ที่ PO อยู่ใน SwitchProdOrder เท่านั้น ==" % len(removed_rows))
        for k in sorted(removed_rows, key=str):
            print("  SO=%s line=%s PO=%s typePrdRemark=%s typePrd=%s lot=%s" % k)
    if problems:
        print("== ต้องตรวจ (%d) ==" % len(problems))
        for p in problems[:40]:
            print(" ", p)
    bad = tot["cells_changed_nonempty"] + tot["rows_added"] + tot["dup_count_changed"]
    print("\nผล:", "❌ หยุด — มีการเปลี่ยนที่ไม่คาดหมาย" if bad else "✅ ไม่มีค่าเดิมถูกเปลี่ยน/แถวเพิ่ม")
    return 1 if bad else 0


LINE = re.compile(r"\[harness\] (\S+)\s+rows=(\d+)\s+(\d+)ms")


def read_times(paths):
    t = collections.defaultdict(list)
    for p in paths:
        with open(p, encoding="utf-8", errors="replace") as f:
            for m in LINE.finditer(f.read()):
                t[m.group(1)].append(int(m.group(3)))
    return t


def cmd_timing(old_logs, new_logs):
    o, n = read_times(old_logs), read_times(new_logs)
    slow = 0
    print("%-62s %9s %9s %7s" % ("เคส", "เก่า(ms)", "ใหม่(ms)", "ใหม่/เก่า"))
    for name in sorted(set(o) & set(n)):
        mo, mn = statistics.median(o[name]), statistics.median(n[name])
        ratio = mn / mo if mo else 0
        flag = ""
        if ratio > SLOW_RATIO and mn - mo > 300:
            flag = "  <-- ช้าลง"
            slow += 1
        print("%-62s %9.0f %9.0f %7.2f%s" % (name[:62], mo, mn, ratio, flag))
    tot_o = sum(statistics.median(o[k]) for k in o if k in n)
    tot_n = sum(statistics.median(n[k]) for k in n if k in o)
    print("\nรวม (median ต่อเคส): เก่า %.0f ms · ใหม่ %.0f ms · ใหม่/เก่า %.2f" % (tot_o, tot_n, tot_n / tot_o if tot_o else 0))
    print("รอบที่ใช้: เก่า %d log · ใหม่ %d log" % (len(old_logs), len(new_logs)))
    print("ผล:", "❌ มีเคสช้าลงเกิน %d%% (และ >300ms)" % round((SLOW_RATIO - 1) * 100) if slow else "✅ ไม่มีเคสช้าลงเกินเกณฑ์")
    return 1 if slow else 0


def main():
    ap = argparse.ArgumentParser()
    sub = ap.add_subparsers(dest="cmd", required=True)
    d = sub.add_parser("diff")
    d.add_argument("old_dir")
    d.add_argument("new_dir")
    t = sub.add_parser("timing")
    t.add_argument("--old", nargs="+", required=True)
    t.add_argument("--new", nargs="+", required=True)
    a = ap.parse_args()
    sys.exit(cmd_diff(a.old_dir, a.new_dir) if a.cmd == "diff" else cmd_timing(a.old, a.new))


if __name__ == "__main__":
    main()
