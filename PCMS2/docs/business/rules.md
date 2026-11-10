# Business Rules — PCMS2

> อัปเดต 2026-11-10 · "กฎเหล็ก" ที่ต้องอ่านก่อนแตะ logic · แหล่ง: โค้ด + ข้อมูลจริง PRD/DEV (อ่านอย่างเดียว) + บทเรียนใน `NEXT_SESSION.md`
> 🟡 = ตีความจากโค้ด ยังไม่มีเจ้าของงานยืนยัน · **หลักการที่ user กำหนด:** PERF_SEARCH (โค้ดปัจจุบัน) คือ source of truth (2026-07-03) แต่ **ค่าในแถวต้องไม่ขึ้นกับตัวกรอง** · **ห้ามถือคำยืนยันของ user เป็นข้อเท็จจริง** ต้องชนกับข้อมูลจริง (2026-11-03)

## 1) กฎที่ต้องห้ามผิด (Critical)
1. **คิวรีตาราง `FromSap*` ต้องกรอง `DataStatus = 'O'` เสมอ** — `'X'` = แถวที่ถูกปิด/ยกเลิก ไม่ลบทิ้ง
2. **ห้ามซ่อม/ลบ/แก้ข้อมูลในตาราง `FromSap*` ด้วยมือ** — ระบบดึงจากตัวใหญ่สุดลงมา งานที่จบแล้วจะไม่ถูกส่งมาใหม่ → ข้อมูลที่ลบไปไม่กลับมา (user 2026-11-03)
3. **ห้ามเพิ่ม PCMSSearch temp tables ใน `PCMSSqlService.dropAllTemp`** (`#tempLotNoList`, `#tempUserStatusList`, `#tempCustomerList`, `#tempCustomerShortList`) — สร้างก่อน main query บน connection เดียว drop-before จะลบก่อนใช้
4. **เพิ่ม `#temp` ใหม่ใน read query → เพิ่มใน `dropAllTemp` + ใช้ `SqlStatementHandler.queryList()`** (HikariCP reuse connection → temp ค้างทำ schema เก่าผิด)
5. **ทุก `DataSourceUtils.getConnection` ต้องคู่ `releaseConnection`** ใน finally (pool หมดแล้วแอปค้าง)
6. **ค่าที่ผู้ใช้กรอก = แก้ด้วยการปิดแถวเดิมเป็น `X` + insert ใหม่ `O`** — ห้าม UPDATE ทับ (history ใช้ตรวจย้อนหลัง)
7. **ห้ามอ่านยอดส่งสดแทน `SumBillCache` ด้วยการกรองเฉพาะใบ `O`** — เคยทำ (MR !17) ซ่อนยอดส่งจริงเพราะ X ปลอม → revert (!19) · ต้องแก้ที่ตัวตั้ง X ไม่ใช่ที่หน้าจอ
8. **Excel/Report ห้ามเชื่อตัวเลขที่ client ส่งกลับ** — ส่งแค่ id แล้ว server ดึงเอง (เพดาน 20,000 แถว) · วันที่ผิดรูป → 400
9. **ก่อนรัน SQL บน PRD:** เขียนไฟล์ชื่อเฉพาะในคำสั่งเดียวกับที่รัน · นับคำสั่งเขียนต้อง 0 · ช่วงปลอดภัย :19–:38/:49–:08 · `-f 65001` · อย่ารันไฟล์ค้างจากโปรเจกต์อื่นใน `/tmp`
10. **`database.properties` ชี้ PRD ใน checkout หลัก** — ห้ามรันตรง ห้าม push ขึ้น `origin` GitHub (ดู `.claude/rules/database-properties-company-remote-only.md`)

## 2) ความหมายทางธุรกิจของสถานะ/ชนิดแถว (ย่อ — เต็มที่ taxonomy)
| คำ | ความหมาย |
|---|---|
| **Main** | 1 แถวต่อ sale line + PO หลัก (ถ้ามี) |
| **พ่วง (OP/SUB)** | PO ผลิตให้ SO หนึ่งแบ่ง Volumn ไปให้ SO อื่น — แสดงใต้ SO ปลายทาง |
| **สวม (Replaced/RP)** | ผู้ใช้ระบุว่า sale line ที่ยังรอจัด Lot ใช้ PO อื่น — เก็บใน `ReplacedProdOrder` |
| **Switch** | สลับ PO ระหว่าง SO · self-switch (PO=SW) ตัดแถว Main ของ PO นั้นออก |
| **WaitLot** | sale line ที่ไม่มีแถว Main แต่ปริมาณถูกพ่วง/สวม · ป้าย: `พ่วงแล้วรอสวม` / `รอสวมเคยมี Lot` / `รอจัด Lot` |
| **Main ไม่มี PO** (ลำดับตัดสิน) | `MaterialNo LIKE 'V%'`=รับจ้างถัก → มีใบส่ง=Lot ขายแล้ว → SaleStatus C=ขาย stock → นอกนั้นรอจัด Lot 🟡 |
| **Special** | `viewUserStatusMappingPCMS.Special=1` — มีผลต่อการหักปริมาณและการแสดงแถว OP/RP (DEV 70 จาก 73 สถานะ) |

## 3) สูตร/ตัวเลขที่ต้องรู้
| ตัวเลข | สูตร/แหล่ง | ข้อควรระวัง |
|---|---|---|
| Prod. Qty (Main) | `PO.Volumn − SumVolRP − SumVolOP` เฉพาะ Grade A/ว่าง · จำนวนเงิน = `Price × ปริมาณ` | ⚠️ ดู §4.1 |
| Prod. Qty (OP/OPSW) | `FromSapMainProdSale.Volumn` | |
| Prod. Qty (SW) | `Volumn_PO − SumVol(พ่วงของ PO)` | |
| Prod. Qty (RP) | `ReplacedProdOrder.Volume` (0 = ใช้ปริมาณ PO) | |
| GR | `SumGRCache` ต่อ (PO,Grade) | PRD ตรงกับของสด 100% · DEV แคชค้าง |
| ยอดส่ง | `SumBillCache` ต่อ (PO,SO,line,Grade) | ⚠️ ดู §4.2 |
| Due Date | แสดง "เลย N วัน" เทียบวันนี้ | **sale line ที่ปิด/ยกเลิก (`saleStatus` ≠ O) ไม่แสดง "เลยกำหนด"** — เติม `saleStatus` จาก `FromSapMainSale` หลังค้นหา (`fillSaleStatus` 2026-11-04) |
| Excel ค่า 0 | แสดง 0 (ไม่ใช่ว่าง) · สี 3 ทศนิยม · Qty 2 ทศนิยม | แก้ 2026-11-08 |

## 4) ปัญหาที่รู้แล้ว/ยังเปิดอยู่ (ห้ามแก้เองโดยไม่ถาม — ต้องให้ user ตัดสิน)
1. 🔴 **Prod. Qty ของ Main เปลี่ยนตามตัวกรอง** — `#tmpSumVolOP/RP` กรองด้วย SO ปลายทางของการพ่วง/สวม ถ้า SO ปลายทางอยู่นอกขอบเขตจะไม่หักยอด (ตัวอย่าง SO 1107010287/200/S1K190: 300 ↔ 200) · บน PRD ≈ 151 แถวใน 13 เคส · ทางแก้ที่น่าจะถูก: กรองด้วย PO ฝั่ง Main (เหมือน `createTempSumGRFiltered`) — **ยังไม่ทำ รอยืนยัน**
2. 🟠 **ยอดส่งจากใบที่ยกเลิก** — `SumBillCache` ไม่ลบกลุ่มที่ใบส่งถูก X: ค้าง 2,166 กลุ่ม (~189,869 kg) เพิ่มทุกวัน · 389 กลุ่มบน sale line ที่ยังเปิด · **X บางส่วนเป็น "X ปลอม"** (ใบยังมีใน `ERP_PROD`) → ต้องหากลไกที่ตั้ง X แล้วแก้ที่ต้นทาง (แผน `docs/cache-fix-plan-2026-11-03.md`)
3. 🟡 **เกณฑ์ WaitLot** เดิม "ต่อ PO" ปัจจุบัน "ต่อ sale line" (`c6e6109`) — PRD เดิม 459 → 676 แถว (+217) · ยังไม่รู้เจตนา
4. 🟡 OP กับ OPSW ใช้ป้าย `OrderPuang/SUB` เหมือนกัน แยกจากหน้าจอไม่ได้ · ความหมาย Switch (4 แถวไม่ซ้ำบน PRD)
5. 🟡 ช่องกรอกที่กุญแจเป็น **PO** (Cause of Delay / Delayed Dep / Switch Remark) ใช้ค่าร่วมกันทุกแถวของ PO (หลาย SO) — ถูกต้องหรือไม่
6. แถวซ้ำ 1 แถวบน PRD (SO 1000003411 line 2 PO P6I000) ยังไม่ตรวจ
7. 2 PO ที่คีย์ใบส่งไม่ตรงกับ ERP (P6D095, P6H130) · ผลต่อ "Lot ขายแล้ว" (`#BillBatchFlag` ใช้ O อย่างเดียว)
8. `TEMP_UserStatusAuto` คีย์ซ้ำ — (A2) ติดตั้งบน PRD แล้ว 2026-11-09 · เหลือซ้ำ 3 คีย์ (P6E076/2R, P6H142, S6I012) · (B2) ล้างซ้ำไม่บังคับ · ผู้เรียก proc ตัวที่สองยังไม่พบ · (C2) unique index รอเฝ้า ~1 สัปดาห์

## 5) กฎเรื่องข้อมูล
| กฎ | รายละเอียด |
|---|---|
| วันที่ที่ผู้ใช้เห็น/กรอก | `dd/MM/yyyy` (`.claude/rules/date-format-dd-mm-yyyy.md`) · `buildDateClause`: null/ว่าง = ไม่กรอง · ผิดรูป = ปฏิเสธ (กัน SQL injection ด้วย regex) |
| ค่าที่ผู้ใช้พิมพ์ลง SQL | **escape/ใช้ PreparedStatement เสมอ** — ผ่านการ sweep 2026-11-02 (`SQL_INJECTION_REVIEW.md`) · เคส regression: ค่ามี `'` ใน saleOrder/articleFG |
| maxlength ช่องกรอก | ตรงกับคอลัมน์ DB จริง (`.claude/rules/input-validation.md`) |
| บัญชีลูกค้า `IsCustomer` | ซ่อน CFM Detail + Roll No Remark จากผลค้นหา Main |
| Log | ATT = ตาราง PCMS · ERP365 = อ่านจาก `ERP_PROD` · เกิน 50,000 แถวตัด + เตือน |

## 6) Edge cases ที่ต้องระวัง
1. **DEV ≠ PRD** — DEV ไม่มีข้อมูลสวม (0 vs 7,846), ไม่มี `FromErpMainProd/Sale`, แคช GR/Bill คนละชุด, proc `TEMP_UserStatus` เวอร์ชันเก่ากว่า → ผล "ผ่านบน DEV" ไม่พิสูจน์ PRD (บั๊ก WaitLot รอบก่อนไม่เห็นบน DEV)
2. Replaced PO ที่ PO ถูก switch แล้ว → ปฏิเสธ · ล้างสวมจนไม่มีแถว → error
3. Grade ≠ A/ว่าง → จำนวนเงินเป็น NULL
4. ผู้ใช้ไม่มีแถว `EmployeePermits` → `VIEWONLY` (ไม่ผ่าน `IsPCMSMain` ถ้า Permits ไม่ตั้ง)
5. เมื่อสั่งรันงาน sync ซ้อน → ถูก lock (`jobLocks`) ปฏิเสธ · สั่งเองได้ ≤7 วัน

## 7) กฎที่เคยสับสน (Historical)
| เรื่อง | เข้าใจผิดว่า | จริงๆ คือ |
|---|---|---|
| แถว `FromSapMainBillBatch` เป็น X | ใบส่งถูกยกเลิกใน ERP | **ไม่เสมอ** — `ERPAtechDaoImpl` ตั้ง X เมื่อ PO อยู่ในชุดซิงก์แต่ LEFT JOIN หาใบใน staging ไม่เจอ (ตัวอย่าง 10 PO ใบยังอยู่ใน ERP_PROD) |
| อ่านสดเฉพาะ O = แก้ปัญหาแคชได้ | ใช่ | ผิด — ซ่อนยอดจริง 2,166 กลุ่ม/~190,000 kg → revert |
| `IsPCMSDetail` คุมหน้า Detail | ใช่ | เดิมคุมแค่เมนู (controller เช็ค `IsPCMSMain`) · ตรงกันแล้วตั้งแต่ 2026-11-10 |
| `/SapToWeb` คือทางสั่ง sync | ใช่ | ซากหน้าทดสอบ (JSP ไม่มี) — สั่งผ่าน Job Management |
| เทสเขียว = ใช้ได้ | ใช่ | regex ใน JSP เคยพังเพราะ backslash ถูกยุบ (500) ทั้งที่แก้ "ถูก" — ยิงหน้าจริงเสมอ |
| ระบบ ERP = SAP | ใช่ | ตอนนี้ ERP365 (ชื่อ `Sap` ยังค้างในตาราง/โค้ด) |

## 8) อ้างอิง
`docs/business/workflow.md` · `docs/business/roles.md` · `docs/detail-row-taxonomy-2026-11-03.md` · `docs/logic-map-detail-2026-11-03.md` · `docs/prd-findings-2026-11-03.md` · `docs/cache-fix-plan-2026-11-03.md` · `docs/orig-vs-main-compare-2026-11-03.md`
