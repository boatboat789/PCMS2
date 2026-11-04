# database.properties ขึ้นได้เฉพาะ remote ของบริษัท (twcgit) — ห้ามขึ้น GitHub ส่วนตัว

user สั่ง 2026-11-02 (ยืนยันแล้ว): *"database.properties อนุญาตให้ขึ้นได้เฉพาะ twcgit ที่เป็นของบริษัท ส่วน github ส่วนตัวไม่ต้องขึ้น"*
ใช้กับ **PPMM2 · PCMS2 · LBMS · InspectSystem · QCMS** (repo ที่มี 2 remotes ตาม `dual-remote-push.md`)

## กติกา

1. **`database.properties` (และไฟล์ config ที่มีรหัสผ่าน/โทเคนของบริษัทแบบเดียวกัน) push ได้เฉพาะ gitlab remote ของบริษัท**
   (`gitlab_master` / `gitlabmaster` / `gitlab` — ดูชื่อจริงจาก `git remote -v`) · **ห้าม push ขึ้น `origin` (github.com/boatboat789/…)**
2. ผลต่อกฎเดิม (user ตัดสินแล้ว — ตาม `adr-conflict-notification.md`):
   - `dual-remote-push.md` "commit เดียวกันต้อง push ทั้ง 2 remote" → **ยกเว้น commit ที่แตะ `database.properties`** (ลง twcgit อย่างเดียว)
   - `git-workflow.md` "ห้าม commit database.properties" → ผ่อนเป็น "commit ได้เมื่อ user สั่งเท่านั้น และ push เฉพาะ twcgit"
3. **ก่อน push `origin` ทุกครั้ง ตรวจว่าไม่มี commit ที่แตะไฟล์นี้ปนไปด้วย:**
   ```bash
   git log origin/<branch>..HEAD --oneline -- '*database.properties'   # ต้องว่าง
   ```
   ถ้ามี → **ห้าม push branch นั้นขึ้น origin** (history ปน — push branch เดียวกันจะพา commit ติดไปด้วย)
4. วิธีแยกที่ใช้ได้: commit การตั้งค่าไว้ **branch แยก** (เช่น `config/<ชื่อ>`) push ขึ้น twcgit เท่านั้น · branch งานปกติไม่แตะไฟล์นี้ จึง push ได้ทั้งสอง remote
5. **ห้ามเขียนทับ history ที่ push ไปแล้ว (force push/rebase) เพื่อแก้เรื่องนี้ โดยไม่ถาม user ก่อน**

## สิ่งที่ยังค้าง (ตรวจ 2026-11-02 — ต้องให้ user ตัดสิน)

- **LBMS ติดตามไฟล์นี้อยู่แล้ว** (`src/main/resources/database.properties`, commit `dcb15b9`/`0054794`) และมีบรรทัด `password` อยู่บน `origin` (GitHub ส่วนตัว) มาก่อนกฎนี้ — กฎนี้ไม่ย้อนหลัง ·
  ทางเลือก: ปล่อยไว้ / ถอดไฟล์ออกจาก index (`git rm --cached` + `.gitignore`) / ล้าง history ฝั่ง origin (ต้อง force push) · อีก 4 repo ไม่ได้ติดตามไฟล์นี้
- **hook `.claude/hooks/block-credential-commit.sh` ยังบล็อก commit ไฟล์นี้อยู่** — ยังไม่ผ่อน (การผ่อน hook คือการผ่อนความปลอดภัย รอ user สั่ง) ·
  ถ้าจะ commit จริง ต้องตัดสินวิธีข้าม hook ก่อน
- ค่า DEV ที่ user แก้ค้างใน LBMS working tree (บล็อก `## DEV`) **ยังไม่ commit**

⚠️ **หมายเหตุ**: ไฟล์นี้อยู่ใน template repo — ต้อง copy ไป `.claude/rules/` ของแต่ละโปรเจกต์เอง ไม่ auto-sync
