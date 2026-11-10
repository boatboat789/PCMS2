# Workflow — PCMS2

> อัปเดต 2026-11-10 · PCMS2 **ไม่มี status machine / config_po_workflow** (ต่างจาก Dyeing) — "workflow" ของระบบนี้คือ **การไหลของข้อมูลจาก ERP → หน้าจอ** และ **การแก้ข้อมูลของผู้ใช้** เอกสารนี้อธิบายทั้งสองอย่าง
> รายละเอียดชนิดแถว/สูตรตัวเลข: `detail-row-taxonomy-2026-11-03.md` · `logic-map-detail-2026-11-03.md`

## 1) Pipeline การ sync (อัตโนมัติ — `service/TaskService.java`)
### งาน A `sortBackGroundAfterGetERPDataProcedure` (lock `ERP_SYNC_JOB`, cron `0 14,44 * * * *`)
| ขั้น | ทำอะไร | เขียนลง |
|---|---|---|
| 1 | ดึง `Z_ATT_CustomerConfirm2Detail` จาก ERP → upsert → `spd_UpsertToZ_ATT_CustomerConfirm2` | `Z_ATT_CustomerConfirm2` |
| 2a | ลูกค้า (ไม่มี SP) | `CustomerDetail` |
| 2b | Sale order: `spd_UpsertToMainSale`, `spd_UpsertToSale` | `FromSapMainSale`, `FromSapSale` |
| 2c | Production order 7 ชุด: `spd_UpsertToMainProd` · `CFM` · `MainProdSale` (พ่วง) · `Packing` · `SubmitDate` · `GoodReceive` · `MainBillBatch` (ใบส่ง) | `FromSapMainProd` ฯลฯ |
| 3 | `spd_UpsertToTEMP_ProdWorkDate` · `spd_UpsertToTEMP_UserStatusOnWeb` · `spd_SumBillAndGoodReceive` | `TEMP_ProdWorkDate`, `TEMP_UserStatusAuto`, `SumGRCache`, `SumBillCache` |
- หน้าต่างดึงข้อมูล: ย้อนหลัง 1 ชม.ล่าสุด (`timeFocus`) — ใบ/PO ที่ไม่อยู่ในชุดนี้จะไม่ถูกแตะ
- ⚠️ **ตั้ง `DataStatus='X'` ของใบส่ง** (`ERPAtechDaoImpl.getFromErpMainBillBatchDetail`): PO อยู่ในชุดซิงก์แต่ LEFT JOIN หาใบใน staging ไม่เจอ → แถว O ของ PO นั้นถูกเปลี่ยนเป็น X (กลับเป็น O ได้ถ้าใบกลับมา) — ที่มาของ "X ปลอม" (ดู `rules.md` §3)
### งาน B `bgJobHandlerDataFromOrgatex` (lock `ORGATEX_IMPORT`, 01:00 ทุกวัน)
`SOR_PRODUCTION` (`SELECT DISTINCT`) → `FromSORCFM` (upsert SQL ตรง ไม่มี SP)
### ช่วงปลอดภัยเมื่อต้องรัน SQL/ทดสอบบน PRD
:19–:38 และ :49–:08 (หลบ sync :14/:44 และ 01:00)
### สั่งเอง
`/Setting/JobManagement` → รันช่วงวันที่ (≤7 วัน) ใช้ lock เดียวกับงาน A · เปิด/ปิด schedule ทั้งสองงานพร้อมกัน · เรียกจาก BGJOB ด้วย `X-Internal-Token`

## 2) Pipeline การแสดงผล (ตอนผู้ใช้กด "ค้นหา")
```
ตัวกรอง (ลูกค้า/สถานะ/Division/Sale/PO/SO/วันที่) → POST /Main|Detail/searchByDetail
  1. PCMSSearch: สร้าง #temp ลูกค้า/สถานะ/LotNo บน connection เดียว (ห้าม drop ก่อน main query)
  2. #tempMainSale = sale line ในขอบเขต (FromSapMainSale 'O')
  3. สร้างชุดย่อย: Main / พ่วง / switch / สวม / WaitLot (+ #tmpSumVolOP/RP, #tempSumGR, #tempProdWorkDate)
  4. #tempFinalResult = UNION 6 ชุด ⟕ ช่อง Input* → JSON (Gson) → DataTables
Excel: Detail/Summary ส่งออกจากข้อมูลที่ค้นไว้ · Report CFM ส่ง "id" กลับมาแล้ว server ดึงข้อมูลจริงเอง (ไม่เชื่อตัวเลขจาก client)
```
- queryList = `SqlStatementHandler.queryList(jdbc, PCMSSqlService.dropAllTemp, sql)` — DROP ก่อน/หลังบน connection เดียว (`SET NOCOUNT OFF` ใน finally)
- ก่อนค้นถ้ายังมีตัวกรองค้าง (เช่น UserStatus ที่ล้างแล้ว) ระบบเตือนด้วย Swal และไม่ยิงคำขอ

## 3) Flow การกรอกข้อมูลของผู้ใช้ (หน้า Detail)
```
Enter/blur ในช่อง → handlerInputField (JS, ตัวกัน check1)
   → POST /Detail/saveInputDetail  { caseSave, PO, SO, line, ค่า }
       1. updateLogRemark*   : ปิดแถวเดิมเป็น DataStatus='X'
       2. upSertRemark*      : insert ค่าใหม่ 'O'
       3. ผลข้างเคียงตาม caseSave:
            stockLoad      → exec spd_UpsertToTEMP_UserStatusOnWeb (เฉพาะ PO นั้น) → คืน UserStatus ใหม่
            replacedRemark → handlerReplacedProdOrder (ดู §3.1)
            switchRemark   → handlerSwitchProdOrder
   → ตอบแถวที่ต้องรีเฟรช + iconStatus (I=สำเร็จ, E=ผิดพลาด) + systemStatus (ข้อความ)
```
### 3.1 Replaced PO (สวม)
- รูปแบบ `PO=ปริมาณ,PO=ปริมาณ` (ปริมาณ 0/ว่าง = ใช้ปริมาณของ PO นั้น)
- **ปฏิเสธ** ถ้า PO ของแถวนี้ถูก switch อยู่แล้ว (ข้อความ "Prod.Order already switch between …")
- ค่าว่าง = ล้างการสวม: ปิด `ReplacedProdOrder` เป็น X แล้วโหลดแถวปกติของ PO ที่เคยถูกสวมกลับมา · ถ้าไม่มีแถวสวมเดิมให้ล้าง → error "Something happen. Please contact IT."
- ⚠️ เมื่อล้างจนไม่มีแถวต้องรีเฟรช endpoint อาจตอบ `[]` → หน้าเพิ่มแจ้ง Swal แล้ว (`PCMSDetail.jsp` 2026-11-08)
- หลังบันทึกสวมระบบเรียก `getWaitLotCaseBySaleOrder` รีเฟรชแถว WaitLot ของ sale order

## 4) การบันทึกการตั้งค่าหน้าจอรายคน
| ปุ่ม | ตาราง | หมายเหตุ |
|---|---|---|
| เลือกคอลัมน์ที่เห็น (Main/Detail) | `ColumnSetting` (`ColVisibleSummary` / `ColVisibleDetail`) | ต่อ user |
| บันทึกชุดตัวกรอง "Default" | `SearchSetting` | ค่ากรองคั่นด้วย `|` · โหลดอัตโนมัติผ่าน `loadDefault` |
| จำกัดลูกค้าของ user | `ConfigCustomerUser` | ตั้งโดย IT ใน DB (ไม่มีหน้าจอ) |

## 5) Deploy / Release (สรุป — เต็มที่ `docs/deployment.md`)
build JDK 1.8 → `target/PCMS2.war` → วางทับบน Tomcat PRD ช่วงปลอดภัย (:19–:38 / :49–:08) → ค้น SO บน Main/Detail + Download Excel + ดู log Tomcat · สำรอง WAR เดิมก่อนวางทับ · SQL patch รันมือใน SSMS ก่อนหรือพร้อม deploy
