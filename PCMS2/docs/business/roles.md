# Roles & สิทธิ์ — PCMS2

> อัปเดต 2026-11-10 · ที่มา: `LoginController`, `PermitDetail`, `PermitManagementController`, `JobManagementController`, `navbar.jsp`, `searchDiv.jsp`
> PCMS2 **ไม่ใช้ Spring Security** — สิทธิ์ = flag ในตาราง `Permits` ผูกกับผู้ใช้ผ่าน `EmployeePermits` · ตรวจใน controller/JSP เอง (`FilterLogin` ตรวจแค่ว่า login แล้ว)

## 1) ผู้ใช้ล็อกอินอย่างไร → ได้สิทธิ์อะไร
```
POST /login/loginAuth
  ├─ AD (dc=atech,dc=co,dc=th) ผ่าน → ดึงชื่อ/แผนก/อีเมลจาก AD + แถวใน Users (UserId)
  └─ AD ไม่ผ่าน → ตรวจ Users (UserId + Password) ← บัญชีภายใน/bypass
        ↓
  EmployeePermits (UserId) → PermitId     (ไม่มีแถว = "VIEWONLY")
        ↓
  Permits (WebApp='PCMS2', PermitId) → flag 9 ตัว → session["permit"]
  (มี override รายคนใน EmployeePermits ถ้า getEmployeePermitsDetailByPermitId คืนค่า)
session: "user" (String) · "userObject" (UserDetail) · "permit" (PermitDetail)
```
- ถ้าไม่มี permit ใน session → หน้า `error/AccessDenied` ("Contact IT for set permission first.")
- `Users.IsCustomer = 1` (บัญชีลูกค้า) → หน้า Main **ซ่อนคอลัมน์ CFM Detail และ Roll No Remark** (`PCMSMainProcess.searchByDetail` เซ็ตเป็นค่าว่างฝั่ง server) · หน้าอื่นไม่ได้กรองเพิ่มตามลูกค้า
- ลูกค้าเห็นเฉพาะลูกค้าที่ผูกไว้ใน `ConfigCustomerUser` (ถ้ามีแถว → รายการลูกค้าในตัวกรองจำกัดตามนั้น; ไม่มี → เห็นทุกลูกค้า) — ⚠️ เมื่อผู้ใช้ไม่ใช่ลูกค้าและไม่มีแถว config จะเห็นทั้งหมด

## 2) Permit flags (ตาราง `Permits`, 9 ตัว)
| flag | ใช้ที่ไหน | ผลเมื่อเป็น 1 |
|---|---|---|
| `IsPCMSMain` | `PCMSMainController`, **`PCMSDetailController`**, navbar "PCMS - Summary" | เปิดหน้า Main **และ Detail** (ทั้งสองหน้าเช็คตัวนี้) |
| `IsPCMSDetail` | navbar "PCMS - Detail" เท่านั้น | แสดงลิงก์ Detail ในเมนู — ⚠️ **controller ไม่ได้เช็คตัวนี้** (ดู §5) |
| `IsPCMSMainToProd` | `searchDiv.jsp` | ปุ่ม "Production Detail" |
| `IsPCMSMainToLBMS` | `searchDiv.jsp` | ปุ่ม "LBMS Detail" |
| `IsPCMSMainToQCMS` | `searchDiv.jsp` | ปุ่ม "QCMS Result" |
| `IsPCMSMainToInspect` | `searchDiv.jsp` | ปุ่ม "Inspect Result" |
| `IsPCMSMainToSFC` | `searchDiv.jsp` | ปุ่ม "SFC Detail" |
| `IsReport` | `ReportController` + navbar "Report" | เปิดรายงาน CFM (+ endpoint ข้อมูล/Excel หลัง `72d41c9`) · เมนู Log อยู่ใต้ Report |
| `IsUserManagement` | เก็บ/แก้ได้ใน Permit Management | **ยังไม่มีโค้ดไหนอ่านค่านี้ตัดสินสิทธิ์** |
ปุ่ม "…ToXXX" เปิดระบบอื่นโดยส่ง userId เข้ารหัส AES (`POST /Main/getEncrypted/{userId}` + `resources/js/Encrypt.js`) — ปลายทางใช้ถอดรหัส/bypass login (ดูหมายเหตุบัญชี bypass §4)

## 3) Permit ที่โค้ดรู้จักตรงๆ
| PermitId | ความหมาย | อ้างในโค้ด |
|---|---|---|
| `VIEWONLY` | ค่าเริ่มต้นของผู้ใช้ที่ไม่มีแถวใน `EmployeePermits` | `LoginController` |
| `ADMIN` | ผู้ดูแล — ทุก flag = 1 · แก้ permit อื่นได้ · **แก้ permit ADMIN ไม่ได้** | `PermitManagementController` |
| `ITSUPP` | IT Support — ทุก flag = 1 · ใช้ Job/Permit Management · ตั้ง permit ให้ผู้ใช้ได้ **ยกเว้นตั้งเป็น ADMIN** | `PermitManagementController` |
| อื่นๆ | ตามที่ตั้งในตาราง (ดูหน้า Permit Management) — โค้ดไม่ hardcode | — |
SQL สร้าง ADMIN/ITSUPP: `sql/2026-06-23_add_admin_itsupp_permits_pcms2.sql` (ยืนยันแล้วว่ามีบน PRD)

## 4) Permission matrix (ระดับหน้า/การกระทำ)
| การกระทำ | VIEWONLY/Permit ทั่วไป | `IsPCMSMain` | `IsReport` | ITSUPP | ADMIN |
|---|---|---|---|---|---|
| เปิด Main / Detail | ❌ (ตาม flag) | ✅ | — | ✅ | ✅ |
| กรอกช่อง Input / วันที่ / Replaced ใน Detail | ⚠️ endpoint `saveInputDetail`/`saveInputDate` **ไม่เช็ค permit** (ต้อง login เท่านั้น) | ✅ | — | ✅ | ✅ |
| รายงาน CFM + Excel | ❌ | — | ✅ (400/403 ข้อความไทยถ้าไม่ผ่าน) | ✅ | ✅ |
| หน้า Log (ATT/ERP365) | login เท่านั้น (ไม่เช็ค `IsReport` ที่ endpoint) | — | เมนูโผล่เมื่อมี `IsReport` | ✅ | ✅ |
| Job Management (ดู/รัน/toggle) | ❌ | — | — | ✅ | ✅ (หรือ header `X-Internal-Token` จาก BGJOB) |
| Permit Management: ดูรายการ/ผู้ใช้ | ❌ | — | — | ✅ | ✅ |
| แก้ flag ของ Permit | ❌ | — | — | ❌ | ✅ (ยกเว้น ADMIN) |
| ตั้ง Permit ให้ผู้ใช้ | ❌ | — | — | ✅ (ไม่ใช่ ADMIN) | ✅ |

## 5) ช่องว่าง/ข้อสังเกตที่พบตอนไล่โค้ด (ยังไม่แก้ — รอ user ตัดสิน 🟡)
1. **`IsPCMSDetail` กับ Detail controller ไม่ตรงกัน** — เมนู Detail ใช้ `IsPCMSDetail` แต่ controller เช็ค `IsPCMSMain` → คนที่มี Detail=1/Main=0 เห็นเมนูแล้วเข้าไม่ได้ · คนที่มี Main=1/Detail=0 ไม่เห็นเมนูแต่พิมพ์ `/Detail` เข้าได้
2. **endpoint บันทึกใน Detail ไม่เช็คสิทธิ์** — ผู้ใช้ที่ login แล้วยิง `POST /Detail/saveInputDetail` ตรงได้แม้ไม่มี flag (ข้อมูลจริงผ่านทางหน้าเว็บมีแต่คนมี Main) · ก่อนเปลี่ยนต้องถามว่า VIEWONLY ควรกรอกได้หรือไม่
3. **Log controllers ไม่เช็ค `IsReport`** (แต่เมนูซ่อนอยู่หลัง `IsReport`)
4. `IsUserManagement` ไม่ถูกใช้ — การเข้า Setting ใช้ `permitId == ADMIN/ITSUPP` แทน
5. `Users.IsAdmin`/`IsSystem` ถูกโหลดเข้า session แต่ไม่ถูกใช้ตัดสินสิทธิ์
6. รหัส AES (`PCMSDISPLAY`/salt/IV ศูนย์) hardcode ใน `PCMSMainController.getEncrypted` — เป็นกลไกเดิมที่ระบบอื่นถอดรหัสด้วยกัน อย่าเปลี่ยนฝั่งเดียว

## 6) บัญชี bypass ของ PCMS ที่ระบบอื่นพึ่งอยู่
`PCMSDISPLAY` และ `PCMSCUS` (ตาราง `Users` ของ QCMS) เป็นทางที่ PCMS เปิด QCMS แบบ bypass — **ห้ามปิด/จำกัดสิทธิ์/แก้** (user สั่ง 2026-10-21) · กฎใหม่ที่กระทบ "ผู้ใช้ไม่มี permit" ต้องยกเว้นสองบัญชีนี้ก่อนเสมอ

## 7) อ้างอิง
`docs/business/workflow.md` (การไหลของข้อมูล) · `docs/business/rules.md` (กฎ/กับดัก) · `docs/api.md` (endpoint) · `.claude/rules/security.md` (FilterLogin)
