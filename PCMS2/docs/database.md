# Database — PCMS2

> อัปเดต 2026-11-10 · ที่มา: `DatabaseConfig`, DaoImpl ทั้งหมด (`dao/implement`, `dao/master/implement`), `sql/`, `database/CLAUDE.md`, ผลตรวจ PRD/DEV ใน `NEXT_SESSION.md`
> SQL Server (PRD เป็น 2014, compat 120 — **ไม่มี** `STRING_AGG … WITHIN GROUP`) · ไม่มี migration framework · รัน SQL patch ด้วยมือใน SSMS

## 1) Datasource (4 ตัว — `config/DatabaseConfig.java`, HikariCP 4.0.3 + mssql-jdbc 9.4.1.jre8)
| Qualifier | DB | Server | ใช้ทำอะไร | เขียนได้? |
|---|---|---|---|---|
| `pcmsDatabase` (@Primary) | `PCMS` | `10.11.44.101` | ข้อมูลหลักทั้งหมดของ PCMS2 | ✅ |
| `ppmmDatabase` | `PPMM` | `10.11.44.101` | ข้อมูลแผนผลิต | ❌ อ่านอย่างเดียว |
| `sorDatabase` | `SOR_PRODUCTION` | `10.11.44.220` | Orgatex → `FromSORCFM` | ❌ อ่านอย่างเดียว |
| `erpDatabase` | `ERP_PROD` (staging ERP365) | `10.11.44.219\ATECHERP` | ต้นทางข้อมูล sync (คีย์ `erp.db.*`) | ❌ อ่านอย่างเดียว |
- Credentials: `src/main/resources/database.properties` (`.gitignore` — PCMS2 ไม่ติดตาม · ห้ามขึ้น `origin` GitHub) · checkout หลักชี้ **PRD ทั้ง 4 ตัว**
- DEV: `PCMSDEV` (`tools/dev-web-local`) — มี proc/ข้อมูลไม่ตรง PRD (ดู `business/rules.md` §6.1)
- ⚠️ ชื่อตาราง/คอลัมน์ของระบบนี้เป็น **PascalCase** (`FromSapMainProd`, `ProductionOrder`) ไม่ใช่ `snake_case` ตามแม่แบบ template — ใช้ตามของจริง · Java map ผ่าน `BeanPropertyRowMapper`/BeanCreateService

## 2) ตารางหลัก (DB `PCMS`)
### 2.1 ข้อมูลที่ sync มาจาก ERP (`DataStatus` `'O'`=ใช้ `'X'`=ปิด)
| ตาราง | ความหมาย | โหลดโดย |
|---|---|---|
| `FromSapMainSale` | sale order line (ตัวขับของ Main) · มี `SaleStatus` | `spd_UpsertToMainSale` |
| `FromSapSale` · `FromSapSaleInput` | รายละเอียด sale / ช่องที่ ERP ส่งมา | `spd_UpsertToSale` |
| `FromSapMainProd` | production order (PO) · `Volumn`, `UserStatus`, `MaterialNo` | `spd_UpsertToMainProd` |
| `FromSapMainProdSale` | การพ่วง PO → SO (`Volumn` ที่แบ่งให้ SO) | `spd_UpsertToMainProdSale` |
| `FromSapMainBillBatch` | ใบส่ง/ม้วนที่ส่ง | `spd_UpsertToMainBillBatch` |
| `FromSapGoodReceive` | รับเข้า GR (KG/MR/YD) | `spd_UpsertToGoodReceive` |
| `FromSapCFM` · `FromSORCFM` | CFM จาก ERP / Orgatex | `spd_UpsertToCFM` / upsert SQL ตรง |
| `FromSapPacking` · `FromSapSubmitDate` · `FromSapReceipe` · `FromSapDyeing`(`DataFromSap`) | แพ็ก / วันที่ส่ง / สูตร / ย้อม | `spd_UpsertTo*` |
| `Z_ATT_CustomerConfirm2` | ยืนยันลูกค้า (รายงาน CFM) | `spd_UpsertToZ_ATT_CustomerConfirm2` |
| `CustomerDetail` | ลูกค้า | sync (ไม่มี SP) |

### 2.2 แคช/ตารางคำนวณ (เติมทุก :14/:44)
| ตาราง | เนื้อหา | ข้อควรระวัง |
|---|---|---|
| `SumGRCache` | GR ต่อ (PO,Grade) | PRD ตรงของสด 100% |
| `SumBillCache` | ยอดส่งต่อ (PO,SO,line,Grade) | ⚠️ ไม่ลบกลุ่มที่ใบถูก X (ค้าง 2,166 กลุ่ม) |
| `TEMP_ProdWorkDate` | วันที่ผลิตต่อ PO | |
| `TEMP_UserStatusAuto` | `UserStatusCal` / `UserStatusCalRP` ต่อ PO | ⚠️ เคยมีคีย์ซ้ำ (race) → proc ใหม่ (A2) |
| `viewUserStatusMappingPCMS` (view) | แมป UserStatus → กลุ่ม + `Special` | `Special=1` มีผลต่อการหัก/แสดงแถว |

### 2.3 ข้อมูลที่ผู้ใช้กรอก (แก้ = ปิดแถวเดิมเป็น X + insert ใหม่)
`InputStockRemark` · `InputPCRemark` · `InputStockLoad` · `InputReplacedRemark` · `InputSwitchRemark` · `InputDelayedDep` · `InputCauseOfDelay` · `PlanCFMDate` · `PlanCFMLabDate` · `PlanDeliveryDate` · `PlanSendCFMCusDate` · `ReplacedProdOrder` (สวม) · `SwitchProdOrder` (สลับ PO)
สถานะ **ไม่มี** `config_po_workflow`/`master_po_status` — ห้ามเขียนโค้ดที่คาดว่ามี (`statusProvider.getIdByCode` ใช้ไม่ได้ที่นี่)

### 2.4 ผู้ใช้/สิทธิ์/การตั้งค่า
| ตาราง | เนื้อหา |
|---|---|
| `Users` | `UserId`, `Password` (fallback เมื่อ AD ไม่ผ่าน), `IsAdmin`, `IsSystem`, `IsCustomer`, `PermitId`, `Responsible` |
| `Permits` | `WebApp='PCMS2'` + `PermitId` + flag 9 ตัว (`IsPCMSMain`…`IsUserManagement`) + `DataStatus` |
| `EmployeePermits` | UserId → PermitId (ไม่มีแถว = `VIEWONLY`) |
| `ConfigCustomerUser` · `ConfigDepartment` | จำกัดลูกค้าต่อ user (ตั้งใน DB) / แผนกที่ล่าช้า (ตัวเลือก Delayed Dep) |
| `ColumnSetting` · `SearchSetting` | คอลัมน์ที่เห็น / ชุดตัวกรอง default ต่อ user |
| `ShopFloorControlDetail` | ข้อมูลโรงงาน (Dye/ColorCheck) ที่รายงาน CFM อ่านร่วม — ⚠️ ยังไม่ยืนยันว่าอยู่ DB ไหน |

หน้า Log ฝั่ง **ATT** ไม่มีตาราง log แยก — อ่านจาก `FromSapMainProd`/`FromSapMainSale` ตามช่วง `ChangeDate` (ฝั่ง `ERP365` อ่านจาก view ใน `ERP_PROD`)

## 3) Stored Procedures (เรียกผ่าน `BackGroundJobDaoImpl` — `PreparedStatement.execute()` ไม่มี output param)
`spd_UpsertToMainProd` · `ToCFM` · `ToMainProdSale` · `ToPacking` · `ToSubmitDate` · `ToGoodReceive` · `ToMainBillBatch` · `ToMainSale` · `ToSale` · `ToZ_ATT_CustomerConfirm2` · `ToTEMP_ProdWorkDate` · `ToTEMP_UserStatusOnWeb` · `spd_SumBillAndGoodReceive`
- `spd_UpsertToTEMP_UserStatusOnWeb` ถูกเรียกจาก **2 ที่**: job :14/:44 (ทั้งตาราง) และตอนผู้ใช้แก้ Stock Load (เฉพาะ PO) → ทำให้ proc นี้ถูกเรียกพร้อมกันได้ — ฉบับ (A2) ใช้ `sp_getapplock` + TRY/CATCH กันซ้ำ
- ⚠️ SP ไม่อยู่ใน transaction เดียวทั้ง batch — ล้มกลางทางแล้วขั้นก่อนหน้า commit ไปแล้ว

## 4) กติกา SQL patch
- ไฟล์อยู่ `sql/` ชื่อ `YYYY-MM-DD_NN_รายละเอียด.sql` (`NN` = ลำดับวันนั้น) · มี `IF NOT EXISTS` guard ทุก statement · รันมือใน SSMS
- ตัวที่มีใน repo: `2026-06-23_add_admin_itsupp_permits_pcms2.sql` (มีบน PRD แล้ว) · `2026-11-01_01_create_Z_ATT_CustomerConfirm2_dev.sql` (**DEV เท่านั้น**) · `2026-11-04_01_fix_userstatusauto_duplicate_key.sql` (A2 ติดตั้ง PRD แล้ว 11-09 · ไฟล์ยัง untracked/รอ commit)
- ก่อน deploy: SQL patch ที่ต้องรัน = ดู `NEXT_SESSION.md` · ลำดับ: SQL ก่อนหรือพร้อมโค้ดเสมอ
- รัน SQL บน PRD: อ่านอย่างเดียวเท่านั้นถ้าไม่ได้รับอนุญาต · ผล `#temp` เก็บใน tempdb · filtered index ต้อง `QUOTED_IDENTIFIER ON` ทุกผู้เขียน

## 5) Temp table (สรุป — เต็มที่ `CLAUDE.md` §Temp Table Rules + `backend/CLAUDE.md`)
- Read: `SqlStatementHandler.queryList(jdbc, PCMSSqlService.dropAllTemp, sql)` (รองรับ DDL/CREATE INDEX · `SET NOCOUNT OFF` ใน finally)
- Write/Upsert: try/finally บน connection ที่ยืมเอง + `DataSourceUtils.releaseConnection`
- ห้ามรวม `#tempLotNoList`/`#tempUserStatusList`/`#tempCustomerList`/`#tempCustomerShortList` ใน `dropAllTemp`
