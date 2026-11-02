# Dual-Remote Push Rule — PCMS2 / LBMS / BGJOB / BGJobApi / QCMS / InspectSystem / SFC / PPMM2 / core

เพิ่ม 2026-09-23 หลังตรวจสอบ remote จริงของ 9 repo นี้ — ทุก repo มี **2 remotes พร้อมกัน**:

- **`origin`** → personal backup บน GitHub (`github.com/boatboat789/<repo>`)
- **gitlab remote** → ต้นทางจริงที่ IT/ทีม deploy ใช้ (`twcgit.wacoal.co.th/twc_att/<repo>`)

**Commit เดียวกันต้อง push ทั้ง 2 remote เสมอ — push แค่ `origin` อย่างเดียวไม่พอ** เพราะ deploy/CI
(ถ้ามี) วิ่งจาก gitlab เท่านั้น ส่วน `origin` เป็นแค่ personal backup

## ⚠️ ชื่อ gitlab remote ไม่เหมือนกันทุก repo — เช็คก่อน push เสมอ

อย่าพิมพ์ `git push gitlab_master <branch>` เดาไปตรงๆ — เช็คด้วย `git remote -v` ก่อน เพราะชื่อจริงต่างกัน:

| Repo | ชื่อ gitlab remote | คำสั่ง push |
|---|---|---|
| PPMM2 | `gitlab_master` | `git push gitlab_master <branch>` |
| QCMS | `gitlab_master` | `git push gitlab_master <branch>` |
| BGJobApi | `gitlab_master` | `git push gitlab_master <branch>` |
| InspectSystem | `gitlab_master` | `git push gitlab_master <branch>` |
| LBMS | `gitlab_master` | `git push gitlab_master <branch>` |
| PCMS2 | `gitlab_master` | `git push gitlab_master <branch>` |
| core | `gitlab_master` | `git push gitlab_master <branch>` |
| SFC | `gitlabmaster` (ไม่มี underscore) | `git push gitlabmaster <branch>` |
| BGJOB | `gitlab` (สั้นสุด) | `git push gitlab <branch>` |

## ลำดับที่แนะนำ

1. `git push origin <branch>` (personal backup)
2. `git remote -v` เช็คชื่อ gitlab remote ของ repo นั้นจากตารางด้านบน (หรือเช็คสดถ้าตารางนี้ล้าสมัย)
3. `git push <ชื่อ gitlab remote> <branch>`

ห้ามข้ามขั้นตอน 3 — ถ้า push แค่ origin แล้วบอกว่า "push เสร็จแล้ว" โดยไม่ push gitlab remote ด้วย
ถือว่างานยังไม่เสร็จจริง เพราะฝั่งที่ต้อง deploy จริงยังไม่มีการเปลี่ยนแปลงนี้

## ข้อยกเว้น — DyeingRecord ไม่เข้ากฎนี้

DyeingRecord มี remote เดียว: `origin` ชี้ตรงไปที่ gitlab company (`twcgit.wacoal.co.th/94687/dyeing-record.git`)
ไม่มี personal GitHub remote แยก — push `origin` ครั้งเดียวพอ ไม่ต้องทำ 2 ที

## ตรวจสอบ path ก่อนเข้าถึง repo (บั๊กที่เจอจริง 2026-09-23)

โฟลเดอร์โปรเจกต์บางตัวซ้อนอยู่ลึกกว่าที่คิด — `.git` จริงอยู่ที่ parent directory ไม่ใช่ subfolder
ที่ดูเหมือนเป็น project root:

- `GitHub/LBMS/.git` (ไม่ใช่ `GitHub/LBMS/LBMS/.git`) — project files อยู่ใต้ `GitHub/LBMS/LBMS/`
- `GitHub/PCMS2/.git` (ไม่ใช่ `GitHub/PCMS2/PCMS2/.git`) — project files อยู่ใต้ `GitHub/PCMS2/PCMS2/`
- `GitHub/SFC/.git` (ไม่ใช่ `GitHub/SFC/SFC/.git`) — project files อยู่ใต้ `GitHub/SFC/SFC/`
- `GitHub/BGJOB/.git` (ไม่ใช่ `GitHub/BGJOB/BGJOB/.git`) — project files อยู่ใต้ `GitHub/BGJOB/BGJOB/`

ถ้า `ls` เจอ `.gitignore` แต่ไม่เจอ `.git` ใน folder ที่คาดว่าเป็น repo — **อย่าสรุปว่าไม่ใช่ git repo**
ให้เช็ค parent directory ก่อนเสมอ (`git -C <path> rev-parse --show-toplevel` เช็คได้ตรงๆ ว่า toplevel
จริงอยู่ที่ไหน)
