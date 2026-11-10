# PCMS2 — ภาพรวมระบบ (ทำความเข้าใจจากโค้ด + ข้อมูลจริง)

> อัปเดต 2026-11-10 · ที่มา: อ่านโค้ด `fix/report-log-review` + เอกสารวิเคราะห์ 2026-11-03 (`logic-map-detail`, `detail-row-taxonomy`, `prd-findings`) + `NEXT_SESSION.md`
> 🟡 = ความหมายทางธุรกิจที่ตีความจากโค้ด ยังไม่มีเจ้าของงานยืนยัน · ไม่ติดป้าย = อ่านได้ตรงจากโค้ด/ข้อมูล

## 1) PCMS2 คืออะไร
ระบบ **ติดตามใบสั่งผลิต (Production Order) และใบสั่งขาย (Sale Order)** ของ Wacoal/Atech (งานย้อม-ถัก-ตกแต่ง) — ดึงข้อมูลจาก ERP (เดิม SAP ตอนนี้ ERP365) มาเก็บใน DB ของตัวเอง แล้วให้ฝ่ายขาย/วางแผน/โรงงานดู **สถานะของแต่ละ sale line ↔ PO** พร้อมกรอกหมายเหตุ/วันที่วางแผนเพิ่มเอง
- **ไม่มี workflow อนุมัติ/state machine ของตัวเอง** — สถานะ (`UserStatus`) มาจาก ERP + คำนวณใน stored procedure · ผู้ใช้กรอกได้เฉพาะ "ช่อง Input" (หมายเหตุ/วันที่) และ "สวม PO" (Replaced)
- Java 1.8 · Spring MVC 5.2 (WAR, ไม่ใช่ Boot) · JSP + jQuery + DataTables · SQL Server · ดู `docs/architecture.md`

## 2) ภาพรวมการไหลของข้อมูล
```
ERP365 (staging ERP_PROD @10.11.44.219\ATECHERP)   SOR_PRODUCTION (@10.11.44.220)
        │  JDBC อ่านอย่างเดียว (erpDatabase)              │ Orgatex → CFM
        ▼                                                  ▼
  job ERP_SYNC_JOB  :14/:44 ทุกชั่วโมง            job ORGATEX_IMPORT 01:00 ทุกวัน
        │  ดึง → upsert ลงตาราง FromSap* → EXEC stored proc spd_UpsertTo*
        ▼
  PCMS DB (pcmsDatabase @10.11.44.101)
   FromSapMainSale / MainProd / MainProdSale / MainBillBatch / GoodReceive / CFM / Packing / SubmitDate ...
   + แคช SumGRCache / SumBillCache (spd_SumBillAndGoodReceive) + TEMP_ProdWorkDate / TEMP_UserStatusOnWeb
        │
        ▼
  หน้า Main (สรุป) / Detail (รายละเอียด) / Report CFM / Log  ← ผู้ใช้กรอก Input* + Replaced + Plan*Date ลง PCMS DB
```
- ตาราง `FromSap*` มี `DataStatus` `'O'` = ใช้งาน · `'X'` = ปิด/ยกเลิก (แถวเก่าไม่ถูกลบ) — คิวรีทุกตัวต้องกรอง `'O'`
- ระบบดึงจาก "ตัวใหญ่สุด" ลงมา (Sale → ProdOrder → bill/GR) งานที่จบแล้วจะไม่ถูกส่งมาใหม่ ⇒ **ซ่อม/ลบข้อมูลในตารางไม่ได้และไม่ควร** (user 2026-11-03)
- รายละเอียด job: `docs/background-jobs.md` · การเรียกจาก BGJOB: `docs/webapi-integration.md`

## 3) หน้าจอและเมนู (`src/main/webapp/WEB-INF/pages/`)
| URL | JSP | ทำอะไร | สิทธิ์ที่ต้องมี |
|---|---|---|---|
| `/Main` (และ `/`) | `PCMSMain/PCMSMain.jsp` | **Summary** — 1 แถวต่อ PO/SO/line สรุป · ตัวกรองลูกค้า/สถานะ/Division/Sale · เลือกคอลัมน์ · บันทึกชุดตัวกรอง (default) · ปุ่มเปิดระบบอื่น | `IsPCMSMain` |
| `/Detail` | `PCMSDetail/PCMSDetail.jsp` | **Detail** — ตารางกว้าง ~7,600px ครบ 6 ชนิดแถว + ช่องกรอกในตาราง (10 ช่อง) | `IsPCMSMain` (เช็คตัวเดียวกับ Main — ไม่ได้ใช้ `IsPCMSDetail`) |
| `/Report/CFM/Detail` | `Report/CFMReport.jsp` | รายงาน CFM · ส่งออก Excel (JXLS template) | `IsReport` |
| `/Log/ProductionOrderLog/{dataType}` · `/Log/SaleOrderLog/{dataType}` | `LogInformation/*.jsp` | ประวัติ sync ของ PO/SO (ATT = ตารางใน PCMS · ERP365 = อ่านจาก ERP_PROD, จำกัด 50,000 แถว) | login |
| `/Setting/PermitManagement` | `Setting/PermitManagement.jsp` | แก้สิทธิ์ของ Permit/ผู้ใช้ | `ADMIN` / `ITSUPP` |
| `/Setting/JobManagement` | `Setting/JobManagement.jsp` | ดูสถานะ/สั่งรัน/เปิด-ปิด schedule | `ADMIN` / `ITSUPP` (หรือ header `X-Internal-Token` จาก BGJOB) |
| `/SapToWeb` | `Test/SapToWeb` (ไม่มีไฟล์ JSP นี้ใน repo — view หาไม่เจอ) | ซากหน้าทดสอบ ไม่ได้ใช้งาน | — |
รายการ endpoint เต็ม: `docs/api.md`

## 4) แนวคิดหลักที่ต้องรู้ก่อนแตะ logic
### 4.1 ชนิดแถวในหน้า Detail (UNION 6 ชุด)
Main · OP (พ่วง `OrderPuang`) · OPSW (พ่วง+switch) · SW (switch) · RP (สวม `Replaced`) · WaitLot — ตารางเต็มที่ `docs/detail-row-taxonomy-2026-11-03.md`
- **พ่วง (OrderPuang)** = PO หนึ่งผลิตให้ SO หนึ่งแล้วแบ่งปริมาณไปให้ SO อื่น (`FromSapMainProdSale`) 98% เป็นการพ่วงข้าม SO
- **สวม (Replaced)** = sale line ที่ยังรอจัด Lot ถูก "สวม" ด้วย PO อื่น — **ผู้ใช้บันทึกเอง** (`ReplacedProdOrder`) · DEV ไม่มีข้อมูลสวมเลย PRD มี 7,846 แถว
- **Switch** = สลับ PO (`SwitchProdOrder`) แบบ self (PO=SW) / ข้าม SO / OPSW
- **WaitLot** = sale line ที่ไม่มีแถว Main แต่ปริมาณถูกพ่วง/สวมแล้ว
- เงื่อนไข "ปริมาณที่นับ" ใช้เฉพาะ PO ที่ `UserStatus` อยู่ใน `viewUserStatusMappingPCMS.Special = 1`

### 4.2 UserStatus
สถานะของ PO ที่แสดง/กรอง — คำนวณจาก ERP + กฎใน SP `spd_UpsertToTEMP_UserStatusOnWeb` เก็บที่ `TEMP_UserStatusAuto` (`UserStatusCal` / `UserStatusCalRP` เมื่อ PO ถูกสวม) · เมื่อผู้ใช้แก้ `Stock Load` ระบบเรียก SP คำนวณใหม่ทันทีแล้วคืนสถานะในแถว
⚠️ SP นี้เคยสร้างคีย์ซ้ำ (race) — แก้เป็น (A2) แล้วบน PRD 2026-11-09 (applock) ดู `NEXT_SESSION.md`

### 4.3 ช่องที่ผู้ใช้กรอก (บันทึกทีละช่อง Enter/blur → `POST /Detail/saveInputDetail` หรือ `saveInputDate`)
| ช่อง (`caseSave`) | ตาราง | กุญแจ | หมายเหตุ |
|---|---|---|---|
| stockRemark | `InputStockRemark` | PO+SO+line+Grade | |
| pcRemark | `InputPCRemark` | PO+SO+line | |
| stockLoad | `InputStockLoad` | PO+SO+line | แล้วคำนวณ UserStatus ใหม่ |
| replacedRemark | `InputReplacedRemark` + `ReplacedProdOrder` | PO+SO+line | ใส่ `PO=ปริมาณ,PO=ปริมาณ` · ว่าง = ล้าง (ปิดเป็น X) · ถ้า PO ถูก switch แล้วจะ error |
| switchRemark | `InputSwitchRemark` | **PO** | ใช้ร่วมกันทุกแถวของ PO · แล้วจัด Switch ใหม่ |
| delayedDep / causeOfDelay | `InputDelayedDep` / `InputCauseOfDelay` | **PO** | ใช้ร่วมกันทุกแถวของ PO |
| sendCFMCusDate / Plan CFM LAB / Plan Delivery / Plan CFM | `PlanSendCFMCusDate` / `PlanCFMLabDate` / `PlanDeliveryDate` / `PlanCFMDate` | | วันที่ dd/MM/yyyy |
ทุกครั้งที่แก้: แถวเดิมถูก **ปิดเป็น `X`** (เก็บเป็น history) แล้ว insert ค่าใหม่ — ไม่มีการ UPDATE ทับ

### 4.4 แคชยอดรวม (ทำให้ตัวเลขเร็ว แต่ต้องระวัง)
`SumGRCache` (GR ต่อ PO+Grade) · `SumBillCache` (ยอดส่งต่อ PO+SO+line+Grade) — เติมโดย `spd_SumBillAndGoodReceive` ทุก :14/:44
⚠️ `SumBillCache` ไม่ลบกลุ่มที่ใบส่งถูกยกเลิก (ดู `docs/business/rules.md`) · ความพยายามอ่านสดแทนแคช (MR !17) **ผิดและถูก revert แล้ว (!19)** — อย่าทำซ้ำโดยไม่อ่าน `docs/cache-fix-plan-2026-11-03.md`

## 5) Login และสิทธิ์ (สรุป — ละเอียดที่ `docs/business/roles.md`)
Login = AD (`dc=atech,dc=co,dc=th`) → ถ้า AD ไม่ผ่านใช้รหัสในตาราง `Users` → โหลด Permit จาก `EmployeePermits` (ไม่มี = `VIEWONLY`) → เก็บ `session["user"]`, `["userObject"]`, `["permit"]` · `FilterLogin` ตรวจ session ทุก request

## 6) การทดสอบ/รันบนเครื่อง
- หน้าเว็บชี้ DEV: `tools/dev-web-local/README.md` (Tomcat พอร์ต 8082) · ต้องตั้ง `JRE_HOME`=jdk-1.8 ไม่งั้น TLS ต่อ DB DEV ไม่ได้
- A/B เทียบผลโค้ดเก่า/ใหม่: `tools/run-harness-ab.ps1` + `tools/harness-ab-diff.py` (`docs/prd-verification-perf-search.md`)
- ⚠️ config ใน checkout หลักชี้ **PRD** — ห้ามรันตรง · DEV ไม่มีข้อมูลสวม/ไม่มี `FromErpMainProd` จึงทดสอบบางเคสไม่ได้
- แผนทดสอบ: `TEST_PLAN.md` · รีวิว SQL injection: `SQL_INJECTION_REVIEW.md`

## 7) Known Quirks (ย่อ)
- `PCMSSearch` temp (`#tempLotNoList`/`#tempUserStatusList`/`#tempCustomerList`/`#tempCustomerShortList`) ห้ามอยู่ใน `dropAllTemp` — สร้างบน connection เดียวกัน**ก่อน** main query
- เขียน backslash ผ่านสคริปต์/heredoc ใน Bash ถูกยุบ — ใช้ `chr(92)` แล้วตรวจ control char หลังแก้ไฟล์ (เคยทำ regex ใน JSP พัง → 500)
- DataStatus `'X'` ≠ ลบ — คิวรีใหม่ลืมกรอง `'O'` = ได้ข้อมูลซ้ำ/ผี
