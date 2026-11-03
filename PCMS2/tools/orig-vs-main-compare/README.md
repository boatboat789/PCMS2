# เทียบผล "main เดิม (b150c87)" กับ "main ปัจจุบัน" บน DEV (อ่านอย่างเดียว)

ทำไมต้องมี: A/B harness ของ PERF_SEARCH (`tools/run-harness-ab.ps1`) เทียบ **8371211 vs ใหม่** — 8371211 มี refactor อยู่แล้ว
จึง **ไม่เห็นความต่างจาก main ที่ใช้งานจริงก่อน PERF_SEARCH (`b150c87`)** ชุดนี้เทียบกับ `b150c87` ตรง ๆ

## วิธีใช้ (2 worktree แยก ไม่แตะ working tree หลัก)
1. `git worktree add --detach C:\temp\wl_orig b150c87` และ `git worktree add --detach C:\temp\wl_fixed <commit ปัจจุบัน>`
2. ในแต่ละ worktree: สร้าง `PCMS2/src/test/resources/` ใส่ `database.properties` (**DEV**) กับ `pcms2-harness-context.xml` (คัดลอกจาก repo หลัก) และคัดลอก `*.java` ในโฟลเดอร์นี้ไป `PCMS2/src/test/java/th/co/wacoal/atech/pcms2/regression/`
3. ⚠️ **b150c87 ต่อฐานข้อมูลจาก `info/Sql*Info.java` ที่ฮาร์ดโค้ดเป็น PRD (ไม่อ่าน database.properties)** — ต้องรัน `patch_info.py` ใน worktree `wl_orig` (แก้ path ในสคริปต์ก่อน) ให้ชี้ DEV ครบ 4 ไฟล์ก่อนรันเสมอ · ตัวทดสอบมี guard `SELECT @@SERVERNAME` ต้องมี `PCMSDEV` ไม่งั้นหยุด
4. `mvn -o -q test -Dtest=WaitLotCompareCheck -Dwl.enabled=true -Dwl.out=C:/temp/<ชื่อ>.out.txt` (เทียบ `getWaitLotCaseBySaleOrder` ทุก sale line) และ
   `mvn -o -q test -Dtest=OrigVsCurrentDetailCheck -Dwl.enabled=true -Dwl.cases=<โฟลเดอร์ cases json> -Dwl.outdir=C:/temp/cmp_<ชื่อ>` (เทียบ `searchByDetail` ทุกเคส)
5. เทียบ: `python -I tools/harness-ab-diff.py diff C:/temp/cmp_wl_orig C:/temp/cmp_wl_fixed`
