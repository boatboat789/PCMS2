# Next Session — PCMS2

> อัปเดตไฟล์นี้ทุกครั้งที่จบ session

## 🔥 2026-11-09 — ตรวจ SQL `2026-11-04_01_fix_userstatusauto_duplicate_key.sql` ให้ DBA (แก้ไฟล์แล้ว · ยังไม่ได้รันอะไรบน PRD · ไฟล์ยัง untracked)

### ที่พบ (ตรวจ PRD อ่านอย่างเดียว + ทดสอบใน tempdb)
- (A) ฉบับเดิม **รันบน PRD ไปแล้ว** (proc modify_date 2026-11-04 16:58:41, นิยามตรงไฟล์เดิมทุกบรรทัด) และ (B) ล้างซ้ำ 9 คีย์รันไปแล้ว (16:58:17) — ไฟล์เดิมเขียนว่า "ยังไม่รัน" ผิดสถานะ
- ปัญหา**ยังเกิดซ้ำ**: คีย์ซ้ำใหม่ 3 คีย์หลัง (B) (P6E076/2R, P6H142 ← 11-06 13:44 · S6I012 ← 11-07 16:44) · ตาราง 106,302 แถว · 29 คีย์มี >1 แถว (O+O 3 · O+X 9 · X+X 17)
- 🔴 (C) เดิม **สร้างไม่ได้** (Msg 10618: filtered index + IGNORE_DUP_KEY) · ถ้าเปลี่ยนเป็น unique ธรรมดา **proc จะ error 2601** เพราะ UPDATE ขั้น 2 พลิกทุกแถวของคีย์เป็น O (9 คีย์ O+X เข้าข่าย) และ proc ไม่อยู่ใน transaction → ขั้น 1 ที่ปิด O→X ทั้ง batch commit ไปแล้ว สถานะว่างทั้ง batch จนรอบถัดไป · (A) เดิมก็ทำให้แถว X ที่ (B) ปิดไว้ถูกพลิกกลับเป็น O ได้
- ต้นเหตุซ้ำ = race: CreateDate ของแถวซ้ำเท่ากันถึง ms · ทดสอบ 2 session ใน tempdb: **ไม่มี lock → 2 แถวซ้ำ, ใช้ sp_getapplock → 1 แถว**
- SQL Server 2014 (compat 120): ไม่มี STRING_AGG WITHIN GROUP · filtered index ต้อง QUOTED_IDENTIFIER ON ทุกผู้เขียน (proc ปัจจุบัน ON)

### ไฟล์ใหม่ (เขียนทับของเดิม สำรองไว้ที่ scratchpad)
(A2) ALTER proc = นิยามจาก PRD + 2 จุด (ค้น "FIX 2026-11-09"): UPDATE ฟื้นเฉพาะแถว Id สูงสุดต่อคีย์ · ครอบ NOT EXISTS+INSERT ด้วย applock+TRY/CATCH · (B2) ตรวจ/ปิดแถว O ซ้ำที่เหลือ (UPDATE ถูก comment) · (C2) unique index ธรรมดา **ไม่บังคับ แนะนำรอ** จนซ้ำ = 0 ต่อเนื่อง
- ตรวจแล้ว: PARSEONLY ผ่านทั้งไฟล์ · diff กับ proc บน PRD = เพิ่ม 23 บรรทัด ไม่มีบรรทัดเดิมเปลี่ยน · ลอจิก UPDATE/applock ทดสอบใน #temp/##temp
- **ยังไม่ได้ทดสอบ:** รัน proc ทั้งตัว (ควรลองบน DEV ก่อน) · ผลของ applock ต่อเวลาที่ proc รอกัน (timeout 120 วิ)

### ✅ ทดสอบ proc ใหม่บน DEV 2026-11-09 (`PCMSDEV` · สำรองตารางก่อน คืนครบหลังทดสอบ: EXCEPT สองทาง = 0, 34,597 แถว)
- DEV มี proc รุ่นเก่ากว่า PRD (ก่อน (A), หน้าต่าง 40 นาที) และคีย์ O ซ้ำ 288 กลุ่ม → ติดตั้งไฟล์ใหม่ (A2) บน DEV แล้ว **ยังคงติดตั้งอยู่** (นิยามเดิมของ DEV เก็บที่ scratchpad `dev_proc_ORIGINAL_backup.txt`)
- รันจริง 3 รอบ × 6 พร้อมกัน (PO ที่ไม่มีแถวใน TEMP): ไม่มี error (มีแต่ warning "Null value is eliminated") · ได้ 6 แถวต่อรอบ · คีย์ซ้ำ 0 · ค่า UserStatusCal เท่าที่สำรองไว้ (0 ต่าง)
- คีย์ที่มี X+O: ฟื้นเฉพาะแถว Id สูงสุด (23447 / 23425) ที่เหลือคง X · ไม่ซ้ำคืนชีพ
- ติดตั้ง (C2) unique index ชั่วคราวหลัง (B2): proc ใหม่ 3 รอบ × 6 พร้อมกัน **ไม่ชน 2601** · ใส่ proc เดิมคืนภายใต้ index เดียวกัน → **Msg 2601 ที่ UPDATE (line 403)** และคีย์ C2J042 กลายเป็น X ทั้งสองแถว (สถานะหาย) ตรงกับที่วิเคราะห์ · แล้วคืน proc ใหม่ + ลบ index
- ⚠️ ทดสอบ race ของ INSERT บน DEV ด้วย proc เดิมไม่ซ้ำเอง (proc รัน 1–2 วิ ช่องแคบ) — กลไกพิสูจน์ด้วย ##temp ใน tempdb แล้ว (ไม่มี lock 2 แถว / applock 1 แถว)
- ยังไม่ได้ทดสอบ: ผลกับ PRD จริง (ข้อมูลใหญ่กว่า, ผู้เรียกจริง) · DEV ไม่ได้มีผู้เรียกที่สอง

### ✅ (A2) ติดตั้งบน PRD แล้ว 2026-11-09 08:58:12 (modify_date ของ proc) — ตรวจนิยามบน PRD เทียบไฟล์ใน repo: **ตรงกันทุกบรรทัด (466)**
- ผล SELECT (B2) หลังติดตั้ง: ยังเหลือ O ซ้ำ 3 คีย์ (P6E076/2R · P6H142 · S6I012 — แถวเก่าก่อนติดตั้ง, ChangeDate เท่ากันทั้งคู่) → (A2) ไม่ล้างแถวที่ซ้ำอยู่แล้ว · UPDATE ของ (B2) ยังไม่ได้รัน (comment อยู่)
- ไม่ต้องรีบ: เมื่อ PO เหล่านี้ถูกประมวลผลอีก proc ใหม่จะยุบคู่ซ้ำเอง (ทดสอบบน DEV แล้ว) · ถ้าอยากให้เป็น 0 ทันที ให้ DBA ยกเลิก comment UPDATE ใน (B2) แล้วรัน (ไม่ลบข้อมูล แค่ปิดแถวที่ไม่ใช่ Id สูงสุด)

### 🔲 ครั้งหน้า
- [x] (A2) ลง DEV แล้ว + ลง PRD แล้ว (08:58 วันที่ 11-09) · [ ] (B2) UPDATE ล้างซ้ำ 3 คีย์ (ไม่บังคับ)
- [ ] หาผู้เรียก proc ตัวที่สอง (job สองตัว? DEV ชี้ PRD?) — เวลาเกิดซ้ำไม่ตรง :14/:44 เสมอ
- [ ] หลัง deploy เฝ้า `dup_key_groups_O` ทุกวันประมาณ 1 สัปดาห์ → ค่อยพิจารณา (C2)
- [ ] commit ไฟล์ SQL เข้า repo เมื่อ user สั่ง (ตอนนี้ untracked)

## 🔥 2026-11-08 — code-review หน้า Report/Log + แก้ (ทดสอบ DEV+PRD อ่านอย่างเดียวแล้ว · commit `72d41c9` push 2 remote แล้ว · **MR !24 https://twcgit.wacoal.co.th/twc_att/pcms2/-/merge_requests/24 รอ review/merge** · ยังไม่ deploy)

branch `fix/report-log-review` (แตกจาก `gitlab_master/main` ที่ดึงไว้ 11-04 — VPN ยังไม่ต่อ ดึงใหม่ไม่ได้) · VPN: Fortinet ตัดอยู่ (twcgit/DB ต่อไม่ได้) ต้องต่อเองแล้ว fetch

### แก้แล้ว
- `ReportController`: เช็คสิทธิ์ `permit.isReport()` ที่ endpoint ข้อมูล · guard list ว่าง/JSON เสีย/reportType ไม่รู้จัก/วันที่ผิดรูป → 400/403 ข้อความภาษาไทย · **Excel ส่งแค่ id แล้ว server ดึงข้อมูลจริงเอง** (ไม่เขียนตัวเลขที่ client ส่งกลับ) เพดาน 20,000 แถว
- `CFMReport.jsp`: ปุ่ม Download ส่งออกเฉพาะแถวที่เห็นตามตัวกรองคอลัมน์ (`rows({search:'applied'})`, ทุกหน้า) ตารางว่าง → เตือน · ใช้ `fetch` อ่านข้อความ error · **ตัดสินใจเอง: เดิมส่งทั้งตาราง ถ้าต้องการทั้งตารางให้เปลี่ยนกลับเป็น `mainTable.rows().data()`**
- `PCMSSqlService.buildDateClause`: null/ว่าง → ไม่กรอง (main แก้ SQL injection ไปแล้วด้วย regex)
- `ExportExcelCFMReportService`: ค่า 0 แสดงเป็น 0 (ไม่ใช่ว่าง) · คอลัมน์สี 3 ทศนิยม (Qty คง 2)
- `CustomDateDeserializer`: Locale.US, รองรับรูปแบบมีเวลา (ไม่ตัดเป็นเที่ยงคืน), คืน null เมื่อว่าง
- `Z_ATT_CustomerConfirm2DaoImpl`: `ById` รายการว่าง → คืนว่าง (เดิมคืนทุกแถว) · CTE `OperationMax`/`CTE_Data` กรองเฉพาะ PO ที่อยู่ในตาราง CFM (ผลลัพธ์เท่าเดิม แต่ไม่ aggregate `DataFromSap` ทั้งตาราง)
- `ERPAtechDaoImpl`: `CustomerDue` เช็ค `[CustomerDue] = ''` (เดิมพิมพ์เป็น `[DueDate]`) · ERP365 log `TOP (50001)` + null-safe
- Log controllers (Production/Sale): ข้อความ "ค้นหาสำเร็จ" แทน "บันทึกข้อมูลสำเร็จ" · จับ error ส่ง JSON `error` · เกิน 50,000 แถวตัด+เตือน · JSP ขึ้น Swal เมื่อ error/ถูกตัด
- `PCMSDetail.jsp`: Swal เมื่อล้าง Replaced Remark ตอบ `[]`

### ✅ ผลทดสอบ DEV 2026-11-09 (Tomcat 8082 สร้างจากสำเนา `C:/temp/pcms2_build` ไม่แตะ target/ ของ Eclipse · shutdown port ใน `pcms2-dev-tomcat/conf/server.xml` เปลี่ยน 8007→8017 เพราะชน)
- 🔴 เจอและแก้บั๊กที่ตัวเองสร้าง: regex ใน `CFMReport.jsp` ถูกสคริปต์แปลง `` เป็นอักขระ 0x02 → JSP parse ไม่ได้ (500) — **เครื่องมือ Bash ยุบ `\` เป็น `\` ใน heredoc: เขียน backslash ผ่านสคริปต์ให้ใช้ `chr(92)`** และตรวจ control char หลังแก้ไฟล์
- Report: 400/403/404 ข้อความไทยถูก (JSON ว่าง/เสีย, วันที่ฉีด SQL, id ไม่มี) · ค้นด้วยแถวทดสอบ ZZTEST 4 แถว (ผูก PO จริง S2K252/S2J385/P3C137/P3C138) ได้ครบ ค่าสี Color Check มาถูก · กรองคอลัมน์ prodID เหลือ 2 แถว → Download ส่ง `[{id:3},{id:4}]` ได้ xlsx · xlsx 4 แถว 39 คอลัมน์ ค่า 0 แสดงเป็น 0 รูปแบบ `#,##0.000` · ตารางว่าง/กรองเหลือ 0 → Swal เตือน · พิมพ์วันที่ผิด → Swal ข้อความรูปแบบวันที่
- SQL CTE ใหม่ vs เก่า: ผลเท่ากัน (EXCEPT สองทาง = 0) · **เวลาเท่ากันบน DEV (ผลลัพธ์ 4 แถว optimizer ผลัก predicate เองอยู่แล้ว) — ยังไม่พิสูจน์ว่าเร็วขึ้น ต้องวัดบน PRD ที่ผลลัพธ์ใหญ่ (อ่านอย่างเดียว)**
- Log: ATT Production/Sale ค้นได้ (33,436 แถวไม่กรอง, PO เดียว 1 แถว) · ค่า null ไม่ NPE · ERP365 **ทดสอบ SQL ไม่ได้บน DEV** (ไม่มี `FromErpMainProd`/`FromErpMainSale` ใน DEV PCMS) ได้ status=error + Swal แทนหน้า 500 → ตรวจกับ ERP_PROD จริงแล้ว ดูหัวข้อ PRD
- Replaced Remark: ตั้ง/ล้างบน DEV ผ่านปกติ (S1C340 ↔ S2K252, ปิดเป็น X เหลือ history) · เคสตอบ `[]` ไม่มีข้อมูลทำให้เกิดจริง ทดสอบสาขาใหม่ด้วยจำลอง `$.ajax` → Swal ขึ้น ตารางไม่ถูกแตะ ฟิลด์อื่นไม่ขึ้น
### ✅ ผลทดสอบ PRD อ่านอย่างเดียว 2026-11-09 08:35 (SELECT ล้วน · ผลเก็บ `#temp` ใน tempdb เท่านั้น · ก่อนรอบ sync :44)
- SQL CFM เก่า vs ใหม่ (14,175 แถว, 11,339 PO): ผลเท่ากัน (EXCEPT สองทาง = 0) · **เวลา 610 ms → 418 ms (เร็วขึ้น ~30%)** วัด 2 รอบคงที่
- แถวซ้ำจาก `ShopFloorControlDetail` (PO+Operation ของ Dye/ColorCheck) และ `FromSapMainProd` ซ้ำต่อ PO: **0 ทั้งหมด** → ความกังวลเรื่อง join คูณแถวไม่เกิดกับข้อมูลปัจจุบัน (ความเสี่ยงแฝงเท่านั้น ไม่ต้องแก้ตอนนี้)
- Log ERP365: จับ SQL ที่ DAO สร้างจริง (`SELECT TOP (50001) … Order by SyncDate desc`) รันบน `ERP_PROD` ได้ถูก syntax — Production 2 วัน 11 แถว · Sale ตั้งแต่ 1 ต.ค. 1,655 แถว (มี CustomerDue 1,630) · บั๊ก `CustomerDue` ไม่มีแถวที่เข้าเงื่อนไขผิดในช่วงนี้ จึงยืนยันได้แค่ว่าแก้แล้วไม่พัง
- เก็บกวาด: ลบแถว ZZTEST ใน `Z_ATT_CustomerConfirm2` แล้ว (0 แถว) · Tomcat DEV หยุดแล้ว

### 🔲 ต้องทำ
- [x] วัดเวลา SQL CFM บน PRD + ตรวจแถวซ้ำ/ตรวจ SQL ERP365 กับ ERP_PROD จริง — เสร็จ (ผลอยู่ในหัวข้อ "ผลทดสอบ PRD")
- [x] ต่อ VPN → fetch → push 2 remote → MR !24 (เปิดแล้ว 2026-11-09; `main` ไม่ขยับระหว่างทำงาน)
- [ ] SQL `sql/2026-11-04_01_fix_userstatusauto_duplicate_key.sql` (untracked) รอ DBA · deploy PRD รอ user สั่ง

## ✅ 2026-11-04 — ทดสอบหน้าเว็บบน PRD (รันบนเครื่อง ชี้ DB PRD, VIEWONLY, job ปิด, อ่านอย่างเดียว) + แก้ Due Date ของ line ที่ปิด

- ผ่าน: ค้น SO (Detail) · ค้นทั้งหมด In-Process 6,294 แถว (server ~8.4s) · แถว WaitLot มีค่า Replaced Remark 64 / PC Remark 7 · Excel Detail 14MB ไม่มี error · Summary 6,276 แถว · log Tomcat ไม่มี SQL error
- **บั๊กที่เจอ:** Due Date ของ sale line ที่ปิด/ยกเลิกยังขึ้น "เลย N วัน" — `renderDueDate` (0547839) เช็ค `row.saleStatus` แต่ SQL หลักของ Detail ไม่ส่งคอลัมน์นี้ออกมา เงื่อนไขจึงไม่เคยทำงาน (DEV ไม่เห็น เพราะเทียบแค่เลขวัน)
- **แก้:** `PCMSDetailDaoImpl.fillSaleStatus` query แยก (อ่านอย่างเดียว) เติม `saleStatus` จาก `FromSapMainSale` ตาม SaleOrder+SaleLine หลังค้นหา — ไม่แตะ SQL กรอง/คำนวณ · ถ้า query ล้มจะข้าม (หน้าจอทำงานเหมือนเดิม) · field ใหม่ `PCMSSecondTableDetail.saleStatus`
- ตรวจบน PRD หลังแก้: SO 1107010287 radio Closed → saleStatus=C ทั้ง 2 แถว วันที่แสดงปกติไม่มีป้ายเลย · In-Process ทั้งหมด saleStatus=O · ค้นทั้งหมด 8.6s (เดิม 8.4s)
- ไม่ได้ทดสอบเขียนข้อมูลบน PRD · ยังไม่ deploy

## ✅ 2026-11-03 (เย็น) — ตรวจความพร้อมขึ้น PRD ของ `main` (e9e5324) — พร้อม · รอ user สั่ง deploy

> user สั่ง commit/push/merge (2026-11-03) → branch `release/prd-readiness-2026-11-03` merge เข้า `main` ผ่าน MR (เอกสารล้วน ไม่มีโค้ด) · **deploy ยังไม่ได้ทำ**

ตรวจบน worktree แยก `C:/temp/pcms2_release` (ไม่แตะ `target/` ของ Eclipse) · ทุกอย่างบน PRD เป็น SELECT ล้วน (ไฟล์ `prd_readiness_check_20261103.sql` นับคำสั่งเขียน = 0, รัน 16:50 ช่วงปลอดภัย)

| ตรวจ | ผล |
|---|---|
| branch ทั้งหมดเข้า `main` แล้ว | ✅ ทุก branch ahead=0 ยกเว้น `docs/readme-pcms2` (เอกสารล้วน) · !19 (revert SumBill) merge แล้ว |
| build JDK 1.8 `mvn -o clean package -DskipTests` | ✅ `target/PCMS2.war` 47.7MB |
| `database.properties` ใน checkout หลัก | ✅ ชี้ PRD ทั้ง 4 ตัว (PCMS/PPMM `10.11.44.101` · SOR_PRODUCTION · ERP_PROD) |
| Permits ADMIN/ITSUPP (`sql/2026-06-23_...`) ที่หน้า Job/Permit Management ใช้ | ✅ มีบน PRD แล้ว + คอลัมน์ `Is*` ครบ 9 ตัว → ไม่ต้องรัน |
| ตาราง/view/proc ที่โค้ดเพิ่มอ้างหลัง `b150c87` (57 ตัว) | ✅ มีบน PRD ครบ ยกเว้น `spd_HandlerCustomerDetail` — **ไม่ใช่ปัญหา** `execHandlerCustomerDetail()` ไม่มีผู้เรียก (ใน `b150c87` ก็ comment ไว้) |
| SQL patch ที่ต้องรันบน PRD ก่อน deploy | **ไม่มี** (`2026-11-01_01_..._dev.sql` = DEV เท่านั้น) |
| บันทึก Replaced PO บน DEV (ค้างมานาน) | ✅ POST `/Detail/saveInputDetail` caseSave=replacedRemark: W5D002/1000000043/00010 → `W4H010` ได้ "อัพเดตข้อมูลสำเร็จ" ผ่าน `getWaitLotCaseBySaleOrder` ไม่มี SQL error · ล้างกลับด้วยค่าว่าง: ReplacedProdOrder=X, InputReplacedRemark ล่าสุด ''/O (ทิ้ง history ของ ZZTEST บน DEV) |

### ตรวจ feature ที่เข้า main ช่วง 10-30 → 11-03 ทีละตัว (DEV Tomcat 8082, build e9e5324)
| commit | feature | ผล |
|---|---|---|
| `336eb0f` `f317d48` `33860c6` | anti-join self-switch · `#tempProdWorkDate`/`#tempSumGR` filtered | ✅ ตรวจไว้แล้วด้วย A/B บน PRD (ค่าเดิมเปลี่ยน 0) — ไม่ได้รันซ้ำรอบนี้ |
| `01a64b3` `790d62e` | WaitLot syntax + คืนค่าหมายเหตุ | ✅ A/B PRD (เดิม) + รอบนี้บันทึก Replaced PO ผ่าน `getWaitLotCaseBySaleOrder` ไม่มี error |
| `f35756b` | navbar แบบ SFC | ✅ `aria-current` = หน้าปัจจุบัน |
| `6e6d553` `92b8e93` | ช่องกรอกในตาราง Detail | ✅ 200 ช่องสูง 34px ทุกช่อง · aria-label ขาด 0 · placeholder `dd/MM/yyyy`/`พิมพ์ที่นี่` · ✎ 10 หัวคอลัมน์ · พื้นอ่อน |
| `0547839` | Due Date แสดงสถานะ | ✅ "12/03/2021 เลย 2062 วัน" (คิดมือถึง 2026-11-03 = 2062 ตรง) · Excel ช่อง Due Date เป็นวันที่ล้วน ไม่มี HTML ปน |
| `5d452b1` `58f2590` | Swal ซ้าย/ขวา · หน้าล้น | ✅ ปุ่ม cancel ก่อน confirm ทั้ง Main/Detail · ล้นแนวนอน 0 ที่ 800/992/1024/1366 (Main, Detail, Report CFM) · ตัวอักษร <12px = 0 |
| `95fcb0b` | เตือนค้นหาไม่ได้ | ✅ ล้าง UserStatus แล้วกดค้นหา: Swal บอก "UserStatus" ยังค้างหลัง 3.5 วิ · ไม่ยิงคำขอค้นหา (Main+Detail) |
| `0cac80f` → `09c1911` | SumBill อ่านสด → revert | ✅ โค้ดเท่า `b708715` (ตรวจไว้ในบล็อกด้านล่าง) |
| — | ค้นหา/ส่งออก ภาพรวม | ✅ Detail 4,525 แถว · Summary 3,684 แถว · Excel 4,525 แถว 52 คอลัมน์ · log Tomcat: SQL error/NPE = 0 |

### ข้อสังเกต (ไม่บล็อก deploy · ยังไม่แก้)
- ล้าง Replaced Remark (ค่าว่าง) แล้ว endpoint ตอบ `[]` → หน้าเว็บไม่ขึ้น Swal แจ้งผล (ข้อมูลใน DB ถูก) · ยังไม่ได้เทียบว่า `b150c87` เป็นแบบเดียวกันไหม
- ~~ช่องค้นหาหน้า Detail บน DEV ค้นได้ 0 แถว~~ **ไม่ใช่บั๊ก** — SO ที่ลองไม่มีอยู่ในกริดของ DEV · ค้นค่าเริ่มต้นได้ 4,525 แถว และค้น SO ที่มีจริง (1107009548) ได้ตรง 1/1
- ไม่รู้ว่า PRD ตอนนี้รันโค้ดเวอร์ชันไหน (เอกสารเรียก `b150c87` ว่า "main เดิม") — diff จาก `b150c87` = 123 ไฟล์ java · admin ควรเก็บ WAR เดิมไว้ย้อนกลับ
- `docs/deployment.md` เขียนใหม่ด้วยข้อมูลจริง + checklist ก่อนขึ้น PRD (เดิมเป็น placeholder ของ template)

### 🔲 ครั้งหน้า
- [ ] user สั่ง deploy → build จาก `gitlab_master/main` ตาม checklist ใน `docs/deployment.md` (deploy ช่วง :19–:38 หรือ :49–:08)
- [ ] หลัง deploy: ค้น SO บน Main/Detail · Download Excel · ดู log Tomcat
- [ ] สำรอง WAR เดิมบน PRD ก่อนวางทับ

## 📌 สรุปงาน 2026-11-03 (จบวัน) — อ่านบล็อกนี้ก่อน

### สถานะ MR
| MR | เรื่อง | สถานะ |
|---|---|---|
| !17 | อ่านยอดส่งสดเฉพาะใบ O (แทน SumBillCache) | **merge แล้ว แต่ผิด** — ซ่อนยอดส่งจริง (X ปลอม) |
| **!19** | revert !17 + เอกสารคำเตือน | **เปิดรอ merge — ต้อง merge ก่อน deploy main ใดๆ** |
| !14 / !15 / !16 | ข้อความเตือนค้นหา / NEXT_SESSION / แผนแคช | รอ review |
- main ยังไม่ deploy (user ยืนยัน) · หลัง revert โค้ดเท่ากับ `b708715` ทุกบรรทัด, compile ผ่าน

### ข้อเท็จจริงที่พิสูจน์แล้ว (PRD + staging ERP อ่านอย่างเดียว)
- SumBillCache มีกลุ่มยอดส่ง > 0 ที่ไม่มีใบ O 2,166 กลุ่ม (~190,000 kg)
- X ใน `FromSapMainBillBatch` ไม่ได้มาจาก ERP ตรงๆ: `ERPAtechDaoImpl.getFromErpMainBillBatchDetail` ตั้ง X เมื่อ PO อยู่ในชุดซิงก์ 1 ชม.ล่าสุดแต่ LEFT JOIN หาใบใน staging ไม่เจอ → upsert ขั้น 3 เปลี่ยนทุกแถว O ของ PO เป็น X · แถวกลับเป็น O ได้ถ้าใบกลับมา
- ระบบดึงจากตัวใหญ่สุด (Sale → ProdOrder → bill/GR) งานที่จบแล้วจะไม่ถูกส่งมาใหม่ (user) ⇒ **ซ่อม/ลบข้อมูลในตารางไม่ได้และไม่ควร**
- **X ปลอม:** ตัวอย่าง 10 PO ทุกตัวยังมีใบใน `ERP_PROD.FromErpMainBillBatch` · 8/10 ตรงทุกคีย์ 117 แถว · SyncDate ฝั่ง ERP ก่อนวันที่ PCMS ตั้ง X ทุกตัว
- ม้วนในกลุ่มค้างแทบไม่ได้ย้ายไปออกใบใหม่ (84/16,129) · X เกิดเป็นก้อนรายวัน (เช่น 2025-02-10 = 17,987 แถว/1,918 PO) มีทั้งช่วงย้าย SAP→ERP365 และปี 2026

### 🔲 งานต่อ (เรียงตามลำดับ)
1. user: merge !19 (ห้าม deploy main ก่อน)
2. นับทั้งระบบ: แถว X ใน PCMS ที่ยังมีใบใน staging ERP กี่แถว (ดึงคีย์สองฝั่งมาเทียบใน Python — คนละเซิร์ฟเวอร์ JOIN ตรงไม่ได้)
3. หากลไก X ปลอม: เทียบเวลาที่ตั้ง X กับประวัติโหลด staging (สมมติฐาน: staging ถูกโหลดใหม่ระหว่าง job อ่าน — ยังไม่พิสูจน์) แล้วแก้ที่ตัวตั้ง X ไม่ใช่ที่หน้าจอ
4. 2 PO ที่คีย์ไม่ตรง (P6D095, P6H130 — ใบใน ERP 19 แถวไม่ตรง PCMS) · ผลต่อ "Lot ขายแล้ว" (`#BillBatchFlag` ใช้ O อย่างเดียว)
5. ค้างเดิม (รอ user ตัดสิน): Prod. Qty (กรอง #tmpSumVolOP/RP ด้วย PO) · กติกา WaitLot · หน้า Detail Input · Summary Due Date

### ⚠️ กติกาทำงาน (บทเรียนวันนี้)
- **ห้ามถือคำยืนยันของ user เป็นข้อเท็จจริง** ต้องชนกับข้อมูลจริง PRD/ERP เสมอ (user สั่ง)
- รัน SQL บน PRD: เขียนไฟล์ชื่อเฉพาะใน scratchpad ในคำสั่งเดียวกับที่รัน · นับคำสั่งเขียนต้อง 0 · ตัวกันเวลาต้อง exit จริง · ช่วงปลอดภัย :19–:38 และ :49–:08 (sync :14/:44, 01:00) · `-f 65001` · aggregate ครอบ subquery ไม่ได้ (Msg 130)
- (เกิดจริง) รัน `/tmp/q8.sql` ที่เป็นไฟล์ค้างของ LBMS บน PRD — เป็น SELECT และ error ตอนคอมไพล์ ไม่กระทบข้อมูล
- staging ERP: `10.11.44.219\ATECHERP` DB `ERP_PROD` (คีย์ `erp.db.*`) อ่านอย่างเดียว
- DEV Tomcat: ต้องตั้ง `JRE_HOME`=jdk-1.8 ไม่งั้น TLS ต่อ DB DEV ไม่ได้ · push option MR: คำอธิบายบรรทัดเดียว และต้องมีคอมมิตใหม่
- hook บล็อก commit ถ้าคำสั่ง bash มีชื่อไฟล์ค่าเชื่อมต่อแม้อยู่ในข้อความ → แยกคำสั่ง

## 🔴 2026-11-03 (เย็น) — SumBillCache Phase 1 **ถูก revert** (MR !17 merge แล้ว → branch `fix/revert-sumbill-live-read` คืนโค้ดเป็น `b708715`)

> **สรุปสั้น (อ่านก่อน):** แพตช์ `0cac80f` (อ่านยอดส่งสดเฉพาะใบ O) ถูก merge ใน MR !17 แต่ตรวจกับ staging ERP แล้วพบ "X ปลอม" — ใบยังอยู่ใน ERP แต่ PCMS ตั้ง X → แพตช์ซ่อนยอดส่งจริง · **revert แล้ว** โค้ดเท่ากับ `b708715` ทุกบรรทัด (`git diff b708715 HEAD -- *.java *.jsp *.js *.xml *.css` = 0) = พฤติกรรมเดิมก่อนแพตช์ · main ยังไม่ถูก deploy (user ยืนยัน 2026-11-03) · **ห้าม deploy main จนกว่า revert จะ merge** · งานถัดไป: นับ X ปลอมทั้งระบบ + หากลไก (ดูรายละเอียดด้านล่าง)

### (บันทึกเดิม) แก้ยอดส่งค้างจาก SumBillCache Phase 1 (branch `fix/sumbill-live-read`, commit 0cac80f)

**ทำอะไร:** `PCMSSqlService.createTempSumBill` อ่านยอดส่งสดจาก `FromSapMainBillBatch` (DataStatus='O') เฉพาะ sale line ที่ค้น แทนอ่าน `SumBillCache` (ซึ่งไม่เคยลบกลุ่มของใบที่ยกเลิก 'X') — แก้จุดเดียว ไม่แตะ proc/ตาราง/แคช

**หลักฐาน (PRD อ่านอย่างเดียว):**
- SQL: แถวใหม่ 40,440 ตรงแคชทุกแถว (ต่าง 0) · หายเฉพาะกลุ่มค้าง 2,166
- A/B 34 เคส x 3 รอบ (main b708715 vs patch): แถวเพิ่ม/หาย 0 · ช่องอื่นนอก billSend เปลี่ยน 0 · ค่าเปลี่ยนเป็นค่าอื่น 0 · เปลี่ยนเฉพาะ billSendWeight/billSendQuantity 565+565 ช่อง (มีค่า -> ว่าง) · CONTROL (เก่า vs เก่า) 0
- 383 (PO,SO,line) ที่ยอดหาย: มีใบ O จริงในต้นทาง = 0
- เวลารวม median 1.03x · 5 เคสถูกธง >20% แต่ก็มีเคสเร็วขึ้นสวนกัน (0.63x) น่าจะแกว่ง **ยังไม่พิสูจน์** — ถ้าห่วงให้รัน A/B ซ้ำ
- DEV: Detail/Summary ค้น SO 1107009273 ได้ 5 แถว ยอดส่งของกลุ่มค้างว่าง ไม่มี SQL error จากโค้ดใหม่ (NPE `buildLikeClause` 2 ครั้งมาจาก body ที่ประกอบมือไม่มี list ไม่เกี่ยวกับแพตช์)
- ยังไม่ได้: กดหน้าจอแบบผู้ใช้จริง, ทดสอบ Excel export บน DEV

**⚠️ หลักฐาน PRD เรื่อง "X = ยกเลิกจริงไหม" (อ่านอย่างเดียว, สุ่มตัวอย่าง 2026-11-03 14:50) — ยังไม่ยืนยัน แพตช์ยังไม่ควร merge:**
- ที่มาของ X (โค้ด `ERPAtechDaoImpl.getFromErpMainBillBatchDetail` + upsert ขั้น 3): คำนวณเอง — PO ที่ซิงก์แล้วฝั่ง ERP ไม่มีแถวใบส่งเหลือ => ทุกแถว O ของ PO นั้นเป็น X · แถวกลับมาเป็น O ได้ (upsert ขั้น 4 เขียนทับด้วยสถานะใหม่) — user ยืนยันเคส saleline/bill ถูกลบแล้วเปิดกลับ
- สุ่ม X 3,000 ม้วน: 307 (10%) กลับมาเป็น O ภายใต้คีย์เดิม · 2,682 (89%) ไม่กลับ
- X เปลี่ยนเป็นก้อนใหญ่รายวัน ไม่ใช่ทีละ PO: 2025-02-10 = 17,987 แถว/1,918 PO · 2024-07-26 = 13,507/1,553 · 2025-03-27 = 6,572/1,167 · 2024-07-01 = 5,893/420 (8 วันแรกรวม ~61k จาก ~110k) → แยกไม่ออกว่าเป็นยกเลิกรวบยอดหรือล้าง/เก็บถาวรตาราง ERP
- DeliveryStatus ของบรรทัดขายที่ยอดค้างในแคช (สุ่ม 400): C 81% · O 18% · B 0.5% เทียบบรรทัดที่มีใบ O จริง (229): C 74% · B 23% · O 3% → บรรทัดที่ไม่มีใบ O ส่วนใหญ่ยังเป็น "ส่งครบ (C)" จึงไม่สนับสนุนว่าทั้งหมดเป็นการยกเลิกการส่ง
- ⚠️ ตัวอย่างเป็น TOP/โมดูโล ไม่ใช่สุ่มสมบูรณ์ ใช้เป็นสัญญาณ ไม่ใช่ข้อสรุป
**📊 แยกกลุ่มค้างตามวันที่เป็น X (PRD, อ่านอย่างเดียว, 2026-11-03 ~14:53)** — กลุ่มค้าง = (PO,SO,line,เกรด) ใน SumBillCache ที่ไม่มีใบ O · "วันก้อนใหญ่" = วันที่มี X >= 1,000 แถว
| กลุ่ม | จำนวนกลุ่ม | kg | DeliveryStatus C / ไม่ใช่ C |
|---|---|---|---|
| A: X ในวันก้อนใหญ่ | 1,393 | 122,385.6 | 1,149 / 244 |
| B: X ในวันปกติ | 794 | 69,062.5 | 622 / 172 |
- รวม 2,187 (ต่างจาก 2,166 เดิมเล็กน้อย เพราะนับรวมกลุ่มที่ยอด 0/เวลาข้อมูลขยับ ไม่ได้ตรวจสาเหตุ)
- ⚠️ **วันก้อนใหญ่ไม่ได้มีแค่ช่วงย้าย SAP→ERP365 (2024-07..2025-03)** — ตัวอย่างกลุ่ม A มีวัน X เป็น 2026-01-13, 2026-04-24, 2026-08-21, 2026-09-25 (ปี 2026) จึงแยกด้วยวันที่อย่างเดียวไม่ได้ว่าเป็น "ล้างตาราง" หรือ "ยกเลิก"
- DeliveryStatus ไม่แยกสองกลุ่มชัด (C 83% vs 78%)
- ตัวอย่างให้ไปเช็คใน ERP365 (ว่ายอดส่งเคยส่งจริงไหม): A: P5G088/1000000426/4 (326.0kg, X 2026-08-21), P5F006/1109012158/1 (307.6, X 2026-01-13), P5K043/1000001311/1 (271.7, X 2026-04-24), P5I104/1000001005/1 (270.6, X 2026-01-13), P6H130/1000004233/3 (261.3, X 2026-09-25) · B: P6C009/1000002333/1 (516.0, X 2026-08-24, line=O), P6D095/1000002804/6 (352.0, X 2026-08-24, O), P6D055/1000002594/5 (306.0, X 2026-08-24, O), T6G029/1R/1000004034/1 (258.8, X 2026-08-08, C), T6H039/1000004241/6 (250.0, X 2026-09-10, C)
- 🔍 **ม้วนในกลุ่มค้าง "ย้ายไปอยู่ที่อื่น" หรือไม่ (PRD 14:58, จับคู่ด้วย RollNumber อย่างเดียว):** ม้วน X ในกลุ่มค้าง 16,129 ม้วน → พบเป็นใบ O ที่ไหนก็ได้ (PO/SO/line อื่น) แค่ **84 ม้วน (0.5%)** · กลุ่ม 2,186 กลุ่ม: ไม่มีม้วนย้ายเลย 2,166 · ย้ายบางส่วน 14 · ย้ายครบ 6 ⇒ ยอดค้างเกือบทั้งหมด **ไม่ได้ย้ายไปออกใบใหม่** ม้วนเหล่านี้ไม่มีสถานะ O ที่ใดเลย — แยกไม่ได้จากข้อมูลว่า "ยกเลิกจริง" หรือ "ERP ลบแล้วไม่ส่งใหม่ (งานจบแล้ว)" ต้องเช็ค ERP365 ตามตัวอย่างด้านบน
- ✅ จำนวนกลุ่ม: 2,166 = กลุ่มค้างที่ยอด > 0 (ตรงกับตัวเลขเดิม) · 2,187 ในตารางแยกกลุ่มรวมกลุ่มที่ยอด 0 ด้วย
- 🔴 **(15:04) เทียบกับ staging ฝั่ง ERP (`10.11.44.219\ATECHERP` DB `ERP_PROD` ตาราง `FromErpMainBillBatch`, อ่านอย่างเดียว) — X ส่วนใหญ่ "ไม่จริง":** ตัวอย่าง 10 PO ทุกตัวยังมีใบส่งใน ERP ตอนนี้ · 8/10 PO ใบใน ERP ตรงกับแถวใน PCMS ทุกคีย์ (PO, BillDoc, BillItem, SO, line, RollNumber) รวม 117 แถว แต่ PCMS ตั้งเป็น X · SyncDate ฝั่ง ERP **ก่อน** วันที่ PCMS ตั้ง X ทุกตัว (เช่น P5F006: ERP 2025-05-24 / X 2026-01-13 · P5G088: ERP 2026-08-15 / X 2026-08-21) — ใบไม่ได้หายจาก ERP · อีก 2 PO (P6D095, P6H130) มีใบใน ERP 19 แถวที่คีย์ไม่ตรงกับ PCMS (ยังไม่ได้ดูว่าต่างตรงไหน)
  - ⇒ **แพตช์ MR !17 ผิดสำหรับเคสนี้: จะซ่อนยอดส่งที่ยังมีใบอยู่จริง** · แคชเดิม (แสดงยอด) ถูกกว่าสำหรับตัวอย่างนี้ · **ห้าม merge !17 — ควรปิด**
  - ปัญหาจริงอยู่ที่ "X ปลอม" ใน `FromSapMainBillBatch` (ตัวตั้ง X: `ERPAtechDaoImpl.getFromErpMainBillBatchDetail` LEFT JOIN ไม่เจอใบ ⇒ X ทั้ง PO + upsert ขั้น 3) — ยังไม่รู้กลไกที่ทำให้ LEFT JOIN ไม่เจอใบ ณ วันนั้น (สมมติฐาน: staging ถูกโหลดใหม่ช่วงที่ job อ่าน; ยังไม่พิสูจน์) · กระทบ `#BillBatchFlag` (Lot ขายแล้ว) ด้วยเพราะใช้ O อย่างเดียว
  - ⚠️ ห้ามซ่อมข้อมูลเอง (user: งานจบแล้วไม่ส่งใหม่) · 🔲 ถัดไป: นับทั้งระบบว่า X กี่แถวยังมีใน ERP (ดึงคีย์สองฝั่งมาเทียบนอกช่วง sync) แล้วนำผลให้ user/เจ้าของ ERP ตัดสิน
- 🛑 **ข้อจำกัดจาก user (2026-11-03):** งานที่จบแล้วใน ERP จะ **ไม่ถูกส่งมาใหม่** → ข้อมูลที่ระบบเรามีอาจเป็นสำเนาเดียวที่เหลือ ซ่อม/ลบ/สร้างใหม่ไม่ได้ และไม่ควรทำ ⇒ **ตัด Phase 2 (ลบ 2,166+ แถวใน SumBillCache, rebuild จากต้นทาง) ออกจากแผน** · ห้ามแก้ข้อมูลในตาราง · ถ้าจะซ่อนยอด ทำได้เฉพาะที่ชั้นแสดงผล (อ่าน) และเฉพาะกลุ่มที่พิสูจน์ได้ว่ายกเลิกจริง (เช่นบรรทัดที่เปิดใหม่ DeliveryStatus=O + X ล่าสุด) · ค่าเริ่มต้นถ้าไม่พิสูจน์ = ปล่อยพฤติกรรมเดิม
- ⚠️ push option ตั้ง Draft ให้ MR !17 ยังไม่ยืนยัน (GitLab ตอบว่า "To create a merge request…" ไม่ใช่ว่าอัปเดต !17) — ให้ user ตรวจใน GitLab ว่า !17 ยังเปิด/เป็น Draft และกด Draft เอง ถ้ายังไม่ใช่
- สถานะ MR !17: **ถูก merge แล้ว** (ebce467) → revert ใน branch `fix/revert-sumbill-live-read`
- 🔲 ต้องตรวจต่อก่อน merge: (1) 739 กลุ่มค้างที่อยู่บรรทัดที่มีใบ O จริง — ยอดย้ายไปอยู่ใต้ PO/คีย์อื่นหรือไม่ (2) สุ่ม PO ในก้อน 2025-02-10 เทียบกับสถานะ PO/ใบส่งในระบบต้นทาง (ขอให้เจ้าของ ERP ยืนยันว่าก้อนนั้นคืออะไร)

**🔲 ค้าง (ต้องให้ user/DBA ตัดสิน):**
- ยืนยันว่า "ยอดส่งจากใบที่ยกเลิกไม่ควรแสดง" ก่อน merge · ยังไม่ deploy
- Phase 2: แก้ `spd_SumBillAndGoodReceive` + backup แล้วลบ 2,166 แถวค้างใน SumBillCache + dedupe `TEMP_UserStatusAuto` (12 key ซ้ำ) + unique index — ต้อง DBA + อนุมัติ ยังไม่เริ่ม
- ค้างเดิม: Prod. Qty (กรอง #tmpSumVolOP/RP ด้วย PO) · กติกา WaitLot · หน้า Detail Input · Summary Due Date
- รัน A/B/ทดสอบ DEV ต้องเลี่ยงนาที :12–18 / :42–48 และ 01:00–01:20 (cron sync) · DEV Tomcat ต้องตั้ง `JRE_HOME`=jdk-1.8 ไม่งั้น TLS ต่อ DB DEV ไม่ได้

## 🟢 2026-11-03 (บ่าย) — สถานะล่าสุด + งานค้าง (อ่านบล็อกนี้ก่อน บล็อกเก่าด้านล่างบางส่วนล้าสมัย)

**merge เข้า `main` แล้ว:** !9 (เอกสารผลตรวจ PRD) · !10 Due Date หน้า Detail (เลย N วัน/วันนี้/อีก N วัน) · !11 ช่องกรอกในตาราง Detail (34px, placeholder, ✎ หัวคอลัมน์) · !12 **WaitLot คืนค่าหมายเหตุที่ผู้ใช้กรอก + Swal ซ้าย/ขวา + หน้าล้น 1024px** · !13 แผงค้นหาไม่ล้นที่ <992px
**เปิดรอ review:** !14 `ux/search-validation-message` (เตือนค้นหาไม่ได้บอกว่าขาดช่องไหน ไม่ปิดเอง)

**แก้ความเข้าใจเดิมในไฟล์นี้:**
- หัวข้อ "ส่งต่อ session ถัดไป … แก้บั๊ก WaitLot ก่อน" ด้านล่าง **ล้าสมัย** — WaitLot แก้แล้ว (บั๊ก `where`/temp table และบั๊กค่าหมายเหตุหาย) และ merge แล้ว
- ข้อ "เมนู Report ชื่อซ้ำ 2 คู่ (ATT vs ERP365)" **overstated** — ในเมนูจริงมีหัวกลุ่ม `DataFromERP` และ `ERP365` คั่นอยู่แล้ว (เห็นเป็นชื่อซ้ำเฉพาะตอนอ่านจาก DOM) · ที่เหลือคือหัวกลุ่มแรกเขียน `DataFromERP` ไม่ใช่ `ATT` — ไม่ได้แก้
- DEV **ไม่มีแถว WaitLot ในกริดเลย (0)** และไม่มีข้อมูลสวม (`ReplacedProdOrder` 0) → ทดสอบหน้าจอส่วน WaitLot/RP บน DEV ไม่ได้ (PRD: สวม 7,846 แถว)

**ตรวจแล้วผ่านบน DEV (หน้าจอจริง, main = b627b05):** ล้นแนวนอน 0 ที่ 375/800/992/1024/1100/1366px (Summary, Detail, Report CFM, Log) · Download Excel หน้า Detail ตรงกับตาราง (169 แถว 52 คอลัมน์ ไม่มี HTML ของ pill ปน) · Due Date เรียง/ค้นหา/ส่งออกใช้ค่าเดิม · ไม่มี console error
**ข้อสังเกตเล็ก (ยังไม่แก้):** ลิงก์ใน navbar สูง 42px ที่ 1024–1366px (เกณฑ์แท็บเล็ต ≥44px) · วงโฟกัสของช่องกรอกตรวจจาก CSS เท่านั้น (pane ไม่มี OS focus) · ถ่ายภาพหน้าจอจาก pane ไม่ได้ (ยืนยันด้วยการวัดค่า)

**งานค้างที่ต้องรอคนตัดสินใจ (ไม่แตะ logic จนกว่าจะสั่ง):**
1. Prod. Qty ของ Main ไม่ควรขึ้นกับตัวกรอง — ทางแก้เสนอไว้ใน `prd-findings-2026-11-03.md` (ภาคผนวก): กรอง `#tmpSumVolOP/RP` ด้วย PO แทน SO ปลายทาง
2. `SumBillCache` ค้าง 2,166 กลุ่ม/189,869 kg (โตทุกวัน) — ยอดส่งจากใบยกเลิก
3. เกณฑ์ WaitLot ต่อ PO หรือต่อ sale line (PRD ต่าง +217 แถว)
4. หน้า "Detail Input" แยก (ยังไม่ได้ทำ — ต้องรู้สิทธิ์การกรอกแต่ละช่อง)
5. Summary Due Date มีกติกา "เลย ≥7 วัน" ของตัวเอง — ถามฝ่ายวางแผนก่อนปรับให้เหมือน Detail

## 📬 2026-11-03 — นำ commit ของ PERF_SEARCH ที่ยังไม่อยู่ใน main เข้า main (MR นี้)

- `790d62e` **WaitLot คืนค่า Replaced/PC Remark, Cause of Delay, Delayed Dep, Stock Load** (เดิมถูกแทนเป็น NULL — ผู้ใช้กรอกที่แถว "รอจัด Lot" แล้วไม่เห็นค่า) · A/B บน PRD แล้ว (บันทึกในข้อความ commit)
- `5d452b1` UX: Swal ยกเลิกซ้าย/ยืนยันขวาทั้งระบบ · แก้หน้าล้นที่ 1024px (ปุ่ม Save/Load Default) · ช่องในตารางหน้า Detail สูง ≥32px
- `ef8ed10`, `265105e` บันทึกผล A/B บน PRD (เอกสาร)
- ตรวจ: merge จำลองเข้า main สะอาด (ไม่มี conflict) · compile ผ่าน · ที่ main ปัจจุบันยังขาด 2 อย่างแรก (วัดบน DEV: ล้น 32px ที่ 1024px)

## 🔴 2026-11-03 — ทำความเข้าใจ logic หน้า Detail ก่อนแก้ (อ่านก่อน!) — เอกสาร 3 ฉบับใน `docs/`

- `detail-row-taxonomy-2026-11-03.md` — **จัดประเภทครบ**: แถว 6 ชุด (Main/OP/OPSW/SW/RP/WaitLot), Main ไม่มี PO 4 แบบ, Switch 3 แบบ, WaitLot 3 ป้าย, สูตร Prod. Qty 5 สูตร, ช่องกรอก 10 ช่อง + ขนาดข้อมูล DEV/PRD + คำถามให้เจ้าของงาน 6 ข้อ
- `logic-map-detail-2026-11-03.md` — ข้อค้นพบ: **Prod. Qty ของแถว Main เปลี่ยนตามตัวกรองลูกค้า** (SO 1107010287/200 PO S1K190: G TECH อย่างเดียว=300, +TW/PLC=200) สาเหตุ `createTempForMainAndWaitLotFiltered` กรอง `#tmpSumVolOP/RP` ด้วย SO ปลายทาง (`c6e6109`) · PRD กระทบ 151 แถว · ทางแก้ที่น่าจะถูก = กรองด้วย PO (ยังไม่ได้แก้)
- `orig-vs-main-compare-2026-11-03.md` + `tools/orig-vs-main-compare/` — เทียบกับ main เดิม `b150c87` (⚠️ user สั่ง 2026-07-03 ว่า main เดิมไม่ใช่ reference — ใช้ดูอาการ ไม่ใช่ตัดสินถูกผิด)

ข้อเท็จจริงที่ไม่ขึ้นกับว่าใครถูก: (1) `SumBillCache` บน PRD ค้าง 2,154 กลุ่มของใบส่ง 'X' → 377 ชุดโชว์ยอดจากใบยกเลิก (2) **DEV ไม่มีข้อมูลสวม (ReplacedProdOrder 0 แถว) PRD มี 7,846** → ชุด RP/WaitLot ทดสอบบน DEV ไม่ได้ (3) A/B "ค่าเดิมเปลี่ยน 0" ก่อนหน้านี้เทียบกับ `8371211` ที่ refactor แล้ว

MR เอกสาร/เครื่องมือชุดนี้ (ไม่แก้โค้ด production): เปิดเมื่อ 2026-11-03 จาก branch `docs/orig-vs-main-compare` → main = https://twcgit.wacoal.co.th/twc_att/pcms2/-/merge_requests/8 (ยังไม่ merge)

🔲 ครั้งหน้า: ให้เจ้าของงานตอบคำถามท้าย taxonomy (ข้อ 1 สำคัญสุด) → แก้ `#tmpSumVolOP/RP` ให้กรองด้วย PO แล้วรัน `OrigVsCurrentDetailCheck` ด้วยชุดตัวกรองหลายแบบ (ค่าแถวเดียวกันต้องเท่ากันทุกตัวกรอง) → ตรวจ `#tempInput*` (กรองด้วย SO/line), แถว SUB ที่หายบน DEV, แถวซ้ำบน PRD, WaitLot บน PRD

## ➡️ ส่งต่อ session ถัดไป (สรุปบรรทัดเดียว: **แก้บั๊ก WaitLot ก่อนอย่างอื่น**)

### 🔥 2026-11-02 (สาย) — WaitLot: A/B บน PRD เทียบ main เดิม (b150c87) เจอ regression เพิ่ม → แก้แล้ว (MR !6)
- **ต้นเหตุจริง:** `selectWaitLot` ใน `PCMSDetailDaoImpl` ถูกเปลี่ยนเป็น NULL ตั้งแต่ `c6e6109` (2026-06-15 "backup") — คอลัมน์ `ReplacedRemark/PCRemark/DelayedDep/CauseOfDelay/StockLoad` ของแถว WaitLot (`ProductionOrder='รอจัด Lot'`) หายเป็นว่างทั้งเส้นทางค้นหา (`buildSqlMainAndWaitLot`) และหลังบันทึก (`getWaitLotCaseBySaleOrder`) ทั้งที่ main เดิมมีค่า · หน้า Detail มีช่องกรอก Replaced Remark ให้แถว WaitLot จึงเป็นค่าที่ผู้ใช้กรอกแล้วหาย · harness เดิมมองไม่เห็นเพราะเคส WaitLot ถูก SKIP
- **แก้:** คืนคอลัมน์ทั้ง 5 ใน `selectWaitLot` + เพิ่ม left join `#tempInputRR/PCR/COD/DD/SL` ใน `buildSqlMainAndWaitLot` · harness เพิ่ม `discoverWaitLotSale` (หา SO/Line จริงจาก DB)
- **ผลทดสอบ PRD (อ่านอย่างเดียว):** `PCMSWaitLotAbCheck` (ไฟล์เดียวรันได้ทั้ง b150c87 และโค้ดใหม่ ผ่าน reflection เพราะเมธอด private ใน main เดิม) — 8 SO/Line, 9 กลุ่มผล: **ก่อนแก้ replacedRemark ต่างทุกแถว · หลังแก้ ไม่ตรงกัน 0** · worktree ของ b150c87 อยู่ที่ `C:/temp/pcms2_orig` (ลบได้: `git worktree remove --force C:/temp/pcms2_orig`)
- **A/B เต็ม `run-harness-ab.ps1 -Target PRD -Rounds 3` (10:50–11:03, หลังแก้ WaitLot):** CONTROL 0/0/0/0/0 (ข้อมูลนิ่ง) · RESULT: ค่าเดิมเปลี่ยน **0** / แถวเพิ่ม **0** / เซลล์ว่างที่มีค่า 3,565 / แถวหาย 7 แถวไม่ซ้ำ (ชุดเดียวกับรอบก่อน = Main ที่ PO อยู่ใน SwitchProdOrder) · TIMING รวม **1.03×** (103.7s→106.3s) · ธงเดียว `searchByDetail__userstatus_lotno` 2723→4693ms (1.72×): เก่านิ่ง 2.7/2.7/2.8s แต่ใหม่ 4.7/2.8/5.7s (2 จาก 3 รอบช้า) → **ยังไม่รู้ว่าเป็น noise หรือเพราะ left join `#tempInput*` ที่เพิ่มใน `buildSqlMainAndWaitLot`** · ✅ **รันซ้ำ 4 รอบ (11:20–11:37): ไม่มีเคสช้าลงเกินเกณฑ์ รวม 1.01× (105.5s→106.8s)** · เคสนี้ใหม่ 3.2/2.8/4.3/2.7s vs เก่า 2.8/3.0/3.0/2.8s → รอบก่อนเป็น noise (มี outlier สลับรอบ) · ค่าเดิมเปลี่ยน 0 / แถวเพิ่ม 0 · ไม่ต้องย้าย join
- เคส harness `getWaitLotCaseBySaleOrder` ถูก discover บน PRD แล้ว (rows=1, 1.7s) · ฝั่งเก่า 8371211 พังตามคาด (`-AllowFail`)
- ทดสอบ DEV แล้ว: Download Excel ของ Detail (52 คอลัมน์ ตรงกับแถวในตาราง) และ Report CFM (xlsx ถูกต้อง 39 คอลัมน์) · ข้อสังเกต: วันที่ผิดรูปแบบ (ต้อง `MMM d, yyyy`) ที่ `/Report/CFM/createExcelReportDetail` ตอบ 405 ทับ error จริง

### ✅ 2026-11-02 (สาย) — UX ต่อ: ลำดับปุ่ม Swal · หน้าล้น 1024px · ความสูงช่องในตาราง (ทดสอบ DEV ผ่าน · MR !6)
- Swal ไม่เคยตั้ง `reverseButtons` (ยืนยันซ้าย/ยกเลิกขวา) → ตั้งที่เดียวใน `config/css/baseCSS.jsp` (`Swal = Swal.mixin({reverseButtons:true})`) · วัดจริง: cancel ซ้าย / confirm ขวา
- หน้าล้นแนวนอนที่ 1024px: สาเหตุคือปุ่ม Save/Load Default ใน `searchDiv.jsp` (nowrap กว้างกว่าคอลัมน์ lg=2) — แก้ด้วย CSS ใน theme-claude.css · วัด: 1024/1366 = 0px ทั้ง Detail และ Main · ช่อง/select ในตาราง Detail สูง 32px (เฉพาะ `#MainTable td`, ไม่แตะ `.form-control` ทั้งระบบ)
- 🔲 ค้าง: ที่ 800px ยังล้น ~80px (แท็บเล็ตแนวตั้ง — ระบบนี้ใช้เดสก์ท็อป ยังไม่แก้) · `.form-control{height:30px}` ของช่องนอกตารางยังเดิม · ตัวกัน `check1` ยังไม่ได้กดทดสอบ

### ✅ 2026-11-02 (สาย) — UX ช่องกรอกในตารางหน้า Detail (ทดสอบ DEV ผ่าน)
- ที่มา: ช่องหมายเหตุต่อสตริง HTML ตรงๆ (โค้ดปี 2022) ไม่ escape → ค่าที่มี `"` ถูกตัดและเด้ง Swal ยืนยันเองตอน blur · แก้: `escAttr()` ใน `General.js` ครอบ `value=`/`data-search=` 20 จุดใน `PCMSDetail.jsp` · Switch Remark `name` จาก StockRemark → SwitchRemark · `aria-label` ทุกช่อง · `cursor:text` (theme-claude.css) · ชื่อฟิลด์ใน Swal เป็นภาษาคน
- ตรวจแล้วไม่ต้องแก้: `maxlength=200` ตรงกับ varchar(200) ของ DEV · focus ring มีอยู่แล้ว (ที่เคยวัดว่าไม่มีเกิดจาก pane ไม่ได้โฟกัส)
- 🔲 ค้าง: `.form-control{height:30px}` < 32px และหน้าล้น 20px ที่ 1024px (กระทบทุกหน้า) · เช็ค `reverseButtons` ของ Swal · ตัวกัน `check1` ยังไม่ได้กดทดสอบ

### ✅ 2026-11-02 (สาย) — แก้บั๊ก WaitLot แล้ว (code-complete · ทดสอบ DEV แบบ syntax-only · **ยังไม่ทดสอบ PRD**)
- `PCMSDetailDaoImpl.getWaitLotCaseBySaleOrder` มี **2 บั๊กซ้อน**: (1) `createTempMainSale` เปิดค้างแต่มี fragment คั่นก่อน `where` → ปิดด้วย `where` + `;` ก่อนต่อ fragment อื่น (2) join helper อ่าน `#tempTAPP/#tempInputRR/PCR/COD/DD/SL` ที่เมธอดไม่ได้สร้าง (`Invalid object name '#tempTAPP'`) → เพิ่ม `createTemp*` ที่ขาด
- เทสต์ใหม่ `PCMSWaitLotSyntaxCheck` (SO ปลอม ได้ 0 แถว ไม่ error) ผ่านบน DEV · **ยังไม่ได้เทียบผลกับ main เดิม** → 🔲 รัน `run-harness-ab.ps1 -Target PRD` (เริ่ม :20/:50) ให้เคส WaitLot ถูก discover ก่อน merge/deploy

**สถานะ ณ 2026-11-02:** PERF_SEARCH (3 fix + navbar + เครื่องมือทดสอบ) merge เข้า `main` แล้ว · ทดสอบ DEV + PRD (อ่านอย่างเดียว) ผ่าน: ค่าเดิมเปลี่ยน 0, ความเร็ว 1.01× · **เหลือบั๊ก 1 ตัวใน main**

ลำดับงาน:
1. 🔴 **แก้ `PCMSDetailDaoImpl.getWaitLotCaseBySaleOrder`** (syntax error `near 'where'` บน PRD) — รายละเอียด/สาเหตุ/ทางแก้ที่น่าจะถูกอยู่ในหัวข้อ 🔴 ด้านล่าง · ย้าย `createTempFromSORCFM` ไปก่อน `createTempMainSale` แล้วตรวจว่า `createTempPrepWaitLot` พึ่ง `#tempMainSale` ไหม · เทียบลำดับกับ `PCMSMainDaoImpl.buildCommonLookupTables` · อ่านโค้ด `main` เดิม `b150c87` เป็นตัวอ้างอิงโครงสร้างที่ถูก
2. เพิ่มเคส harness ให้ WaitLot ถูก discover บน PRD (ตอนนี้ DEV ไม่มีข้อมูล WaitLot เคสถูก SKIP จึงมองไม่เห็นบั๊กนี้)
3. รัน `powershell -ExecutionPolicy Bypass -File tools\run-harness-ab.ps1 -Target PRD -Rounds 3 -Out C:\temp\pcms2_ab_prd -Yes` (เริ่มตอน :20/:50, ปิด Eclipse/Tomcat ก่อน) — เคส WaitLot ต้องผ่านเฉพาะโค้ดใหม่ · คู่มือเต็ม `docs/prd-verification-perf-search.md`
4. push **ทั้ง 2 remote** (`origin` + `gitlab_master`) แล้วเปิด MR: `git push gitlab_master <branch> -o merge_request.create -o merge_request.target=main -o "merge_request.title=..."` (ต้องมี commit ใหม่ใน push ถึงจะสร้าง MR · ไม่มี glab/gh)
5. ยังไม่ได้ทดสอบ: ปุ่มบันทึก Replaced PO บนหน้า Detail จริง (ต้องทำบน DEV เท่านั้น ห้ามบน PRD — เขียนข้อมูล) · Download Excel (Detail/Report CFM) · Report CFM กับข้อมูลจริง (ตาราง DEV ว่าง) · เมนู Report ชื่อซ้ำ ATT/ERP365 (รอ user เลือกคำ)

**ข้อควรระวัง:**
- อย่ารัน `mvn test`/harness พร้อม Tomcat หรือ Eclipse ที่ใช้ `target/classes` · `mvn clean` ลบ `target/` → Eclipse ต้อง Maven > Update Project
- `PCMS/` ในโฟลเดอร์ repo root เป็น **git repo ซ้อน** ห้าม `git add` · `target/m2e-wtp/.../MANIFEST.MF` ที่ถูกลบ = ผลจาก `mvn clean` ไม่ต้อง commit
- สิทธิ์ PRD: user อนุญาตให้ Claude ทดสอบเฉพาะที่ **ไม่ update/delete** (SELECT, harness) เท่านั้น
- scheduler ของ PCMS2 รัน 01:00 และ :14/:44 ทุกชั่วโมง — อย่าสตาร์ต Tomcat ทดสอบ (`tools/dev-web-local`) ใกล้เวลานั้น

## 🔴 2026-11-02 — ทดสอบบน PRD (อ่านอย่างเดียว) เสร็จ: ผลเทียบผ่าน · ความเร็วเท่าเดิม · **เจอบั๊กใหม่ที่ DEV มองไม่เห็น**

⚠️ **MR !4 ถูก merge เข้า `gitlab_master/main` แล้ว** (`18b621f`) ก่อนทดสอบ PRD — โค้ดด้านล่างที่ "พัง" จึงอยู่ใน main แล้ว (commit ของรอบนี้ยังไม่อยู่ใน main)

วิธี: `tools/run-harness-ab.ps1 -Target PRD -Rounds 3` (โค้ดเก่า `8371211` vs ใหม่ สลับกัน 3 รอบ, 08:54–09:07, ก่อน sync :14) · ผู้ทดสอบ = Claude ตามที่ user อนุญาต "ทดสอบสิ่งที่ไม่ update/delete บน PRD" · ผลดิบ `C:\temp\pcms2_ab_prd` · query บน PRD เป็น `SELECT` ล้วน · harness สร้างแค่ `#temp` ใน tempdb

| ผล | ค่า |
|---|---|
| CONTROL (เก่า r1 vs เก่า r2) | 0/0/0/0/0 — ข้อมูลนิ่งตลอดรอบ ผลเชื่อได้ |
| ค่าไม่ว่างถูกเปลี่ยน / แถวเพิ่ม | **0 / 0** |
| เซลล์ว่างที่มีค่า | 3,565 (วันที่ย้อม/CFM/GR/ฯลฯ) |
| แถวหาย | 16 ครั้ง = **7 แถวไม่ซ้ำ** (Main): SO 1000000909 W5J019 · 1000001214 W5J080/2R · 1107011020 S2F167 · 1107011676 S2K244 · 1111002704 W2B083 · 1111003105 W3B033/1R และ W3B034 — **ตรวจกับ `SwitchProdOrder` บน PRD แล้ว ทุก PO มีแถว self-switch (DataStatus='O')** ตรงกติกา `main` เดิม |
| ความเร็ว (median 3 รอบ, 33 เคส) | รวม **1.01×** (เก่า 104.0s / ใหม่ 105.3s) · มีธงเดียว `getPCMSSumaryDetail__single_saleorder_switch_target` 1156→1621ms (+465ms, 1.40×; ฝั่ง Detail ของ SO เดียวกัน 1.08×) → **ข้อกังวล "ช้าลง ~11%" จาก DEV ตกไป** (DEV รอบเดียวแกว่งเกินจริง) |
| คู่ switch จริงบน PRD ที่มี sale | 2 คู่: SO 1109009155 (เคส 16 = Summary 6 / Detail 6 แถวบน PRD; DEV 6/10) และ **SO 1000003047 TW/2URM (เคส 17 ใหม่ — 15 แถวบน PRD)** — ทั้งคู่ไม่มีค่าเดิมเปลี่ยน |

### 🔴 บั๊ก: `PCMSDetailDaoImpl.getWaitLotCaseBySaleOrder` พังบน PRD — `Incorrect syntax near the keyword 'where'`
- เกิดทั้ง 6 รอบ ทั้งโค้ดเก่า (`8371211`) และใหม่ (HEAD = ที่อยู่ใน main ตอนนี้) · **DEV มองไม่เห็นเพราะไม่มีแถว WaitLot → harness SKIP เคสนี้**
- เมธอดนี้ในโค้ด `main` เดิม (`b150c87`) ไม่มีปัญหาโครงสร้างนี้ · ใน PERF_SEARCH มีการแทรก `createTempForMainAndWaitLot`, `createTempPrepWaitLot`, `createTempFromSORCFM` เข้าไปในสตริง SQL
- สาเหตุที่เห็นจากโค้ด (ยังไม่ได้แก้/ยังไม่ได้ทดสอบแก้): `pss.createTempMainSale` ลงท้าย **เปิดค้าง** (`... left join ConfigCustomerEX ... on ...` ไม่มี `where`) เพื่อให้ `where` ที่ตามมาเป็นตัวกรองของมัน แต่ HEAD วาง `createTempFromSORCFM` (ลงท้าย `CREATE CLUSTERED INDEX ...;`) **คั่นกลาง** → `where` ลอยหลังคำสั่งที่จบแล้ว = syntax error · (และถ้า parse ผ่าน `#tempMainSale` จะไม่ถูกกรองด้วย where) — ทางแก้ที่น่าจะถูก: ย้าย fragment ที่ลงท้ายครบคำสั่ง (`createTempFromSORCFM`, อาจรวม `createTempPrepWaitLot` — ต้องตรวจว่าพึ่ง `#tempMainSale` ไหม) ไปไว้ **ก่อน** `createTempMainSale` · เทียบกับลำดับใน `PCMSMainDaoImpl.buildCommonLookupTables`
- ผู้ใช้เจอเมื่อไหร่: `PCMSDetailProcess` (บรรทัด ~252) เรียกเมธอดนี้ **หลัง `upsertReplacedProdOrder` สำเร็จ** = ตอนผู้ใช้บันทึก Replaced PO บนหน้า Detail → ข้อมูลถูกเขียนแล้วแต่ขั้นอ่านต่อ error · ยังไม่ได้ลองกดจริงบนหน้าเว็บ (ห้ามกดบน PRD เพราะเขียนข้อมูล)
- 🔲 **ครั้งหน้า:** แก้ลำดับ fragment → เพิ่มเคส harness ที่ให้ WaitLot discover ได้บน PRD → รัน `run-harness-ab.ps1` ซ้ำ (เคสนี้ต้องผ่านเฉพาะโค้ดใหม่) → push ทั้ง 2 remote + เปิด MR (วิธีเปิด MR ดู memory) · **ถ้ามี deploy จาก main ขึ้น PRD แล้ว ถือเป็นเรื่องเร่งด่วน**

### บทเรียนเครื่องมือ (กันเสียเวลาซ้ำ)
- `run-harness-ab.ps1` รุ่นแรก **"รัน PRD" แต่ต่อ DEV จริง**: `Copy-Item` คงวันที่แก้ไขเดิม (ไฟล์ PRD เก่า 2026-07-06) → maven-resources-plugin ข้ามการคัดลอกเข้า `target/test-classes` → harness อ่านไฟล์ DEV เดิม · จับได้เพราะตัวเลข PRD เหมือน DEV ทุกตัว และเคส 17 ได้ 0 แถว · แก้แล้ว: คัดลอกตรงเข้า `target/test-classes` + touch + ตรวจ URL ก่อนรันทุกรอบ + คืน DEV ทั้ง `src` และ `target` ตอนจบ
- เพิ่ม `-AllowFail <ชื่อเคส>` ให้เดินต่อเมื่อมีเคสที่รู้ว่าพัง (เคสอื่นที่พังยังหยุด)

## 📌 2026-11-01 — สรุปสถานะ PERF_SEARCH (หยุดเพิ่มงานใหม่ — อ่านอันนี้ก่อนเริ่ม session ถัดไป)

ทดสอบทั้งหมดบน **DEV** เท่านั้น (Tomcat ทดสอบ `tools/dev-web-local` พอร์ต 8082 · harness) · **ยังไม่มีอะไรทดสอบบน PRD**

### ✅ แก้แล้ว + ทดสอบแล้ว
| งาน | commit | หลักฐาน |
|---|---|---|
| anti-join self-switch ใช้ `#tempSPOAnti` | `336eb0f` | A/B harness: แถว Main self-switch 4 แถวไม่ซ้ำ (SO 1107011300/1107011676/1111002704/1107011020) หายตามโค้ด `main` เดิม · หน้า Summary SO 1107011323 = 4 แถวตรง baseline |
| `#tempProdWorkDate` filtered ครอบ PO ของ OP/SW/OPSW | `f317d48` | A/B 22 เคส: ค่าไม่ว่างเปลี่ยน **0** · แถวเพิ่ม **0** · เซลล์ว่างมีค่า 2,205 · หน้า Detail/Summary แถว S2K032H มีวันที่ย้อม · เคส 16 (คู่ switch จริง) แถว SUB ได้วันที่ |
| `#tempSumGR` filtered ครอบ PO คู่ switch | `33860c6` | เคส 16: P2K069 ได้ GR 124.20 KG / 331.00 MR (เดิมว่าง) |
| เคส harness 10/10b/10c/12/13/14/14b/15/16 + `PCMSServiceGsonCheck` | `9d1c840` `5c0d39a` `ebb83af` | harness verify ผ่านทุกเคส (exit 0) |
| navbar แบบ SFC (`aria-current`, เป้ากด ≥44px) | `f35756b` | 7 หน้า (Main/Detail/Report CFM/Log ×4) `aria-current` ถูกหน้า · 1366px ไม่ล้น · 800/375px ตรวจบน Summary |
| เครื่องมือ `tools/dev-web-local` | `daba1cb` | รันจริง scheduler OFF |
| ตัวกรอง Summary + Detail: userStatus / SO Date / Prod.Date / Due Date | — | ในช่วง/นอกช่วง ให้ผลถูก (SO 1107011323) |
| Report CFM บน DEV (ตารางเปล่า) | `d33affa` | ตอบ 200 (0 แถว) — user รัน script บน DEV แล้ว |
| rules 8 ไฟล์ + Document Index | `4630ca3` | — |

### 🔲 ค้างไว้ (บันทึกแทนการทำ — ยังไม่ได้เริ่ม)
เรียงตามความเสี่ยง:
1. **PRD ยังไม่ได้ทดสอบเลย** (ทั้ง 3 fix) · DEV ล้าหลัง PRD · ถ้าจะตรวจต้องตกลงสิทธิ์อ่าน PRD ก่อน (รอบนี้ระบบบล็อกการอ่านข้อมูลระดับแถวของ PRD)
2. **ข้อมูลทดสอบ switch น้อย:** DEV มี switch ไม่ใช่ self 17 คู่ มี sale แค่ 1 คู่ · ไม่มีคู่ที่ sale ต่างใบทั้งสองฝั่ง (เส้นทาง "PO คู่ OPSW ที่ sale ปลายทางอยู่นอก scope" ทดสอบกับข้อมูลจริงได้แค่ 1 เคส)
3. **แถวที่หาย 4 แถวไม่ซ้ำ** ยืนยันจากการอ่านโค้ด `gitlab_master/main` ไม่ได้รัน `main` จริง
4. **ความเร็ว:** ช้าลง ~11% บน DEV วัดครั้งเดียว ยังไม่วัดซ้ำ/บน PRD
5. เคส harness ที่ SKIP (หาข้อมูลตัวอย่างไม่เจอ): `...ProductionOrderRP` ×2, `getWaitLotCaseBySaleOrder`
6. **ยังไม่ได้ทดสอบ:** Download Excel ของ Detail · Download Excel ของ Report CFM · Report CFM กับข้อมูลจริง (ตาราง DEV ว่าง) · ปุ่ม Search ของ Report ไม่ยิง request เมื่อไม่กรอกตัวกรอง (ยังไม่ไล่ว่า validation ตั้งใจ)
7. **UX (ยังไม่แก้ ต้องให้ user เลือกคำ):** เมนู Report มีข้อความซ้ำ 2 คู่ "Log - Production Order"/"Log - Sale Order" (ATT vs ERP365) — `config/navbar.jsp`
8. ล้นแนวนอน ~75px ที่จอ ≤800px หน้า Summary (ช่องกรอง bootstrap-select) — แก้เมื่อต้องรองรับแท็บเล็ต
9. TaskService ลำดับ sync (ข้อ 4 จาก code-review) ยังไม่ได้ตรวจ — น่าจะตั้งใจ
10. job ERP sync ช่วง 21:14 (รอบแรก) อาจรันกับ DEV ยืนยันไม่ได้ (log เป็น NOP)
11. ✅ **(อัปเดต 2026-11-02 ดึก) MR !4 ถูก MERGE เข้า `gitlab_master/main` แล้ว** (`18b621f`, https://twcgit.wacoal.co.th/twc_att/pcms2/-/merge_requests/4) ทั้งที่เปิดเป็น Draft และยังไม่ทดสอบ PRD · ขนาด: 41 commit, 2,379 ไฟล์ รวมลบ SAP JCo `sapjco3.dll/jar` + JSP เก่า 5 ไฟล์ + `FilterLogin20250526.java` · ต่อมาทดสอบ PRD แล้ว (ดูหัวข้อ 🔴 ด้านบน) · งานรอบถัดมา (docs/tools/เคส 17/ผลทดสอบ PRD — **ไม่แก้โค้ด production**) เปิดเป็น **MR !5**: https://twcgit.wacoal.co.th/twc_att/pcms2/-/merge_requests/5 (เปิด 2026-11-02, ยังไม่ merge) · MR ถัดไปที่แก้บั๊ก WaitLot ให้แยกเป็นอีกใบ · ไฟล์ที่ **จงใจไม่ commit**: `PCMS/` เป็น git repo ซ้อน (มี `.git` ของตัวเอง 85MB) ไม่ใช่ของ repo นี้ · `target/m2e-wtp/.../MANIFEST.MF` ถูกลบจาก `mvn clean` (ไฟล์ generated ที่ถูก track) · ไฟล์ค้างใน working tree: โฟลเดอร์ `PCMS/` (untracked ไม่รู้ที่มา), `target/m2e-wtp/.../MANIFEST.MF` ถูกลบ (ผลจาก `mvn clean`)
12. เก่าจาก 07-17: guard schedule toggle ของ Orgatex ยังเขียนว่ารอ deploy+ทดสอบ — ไม่ทราบสถานะปัจจุบัน

## ✅ 2026-11-01 — ตรวจงาน PERF_SEARCH + navbar บนหน้าจอจริง (เครื่องมือ tools/dev-web-local ใหม่)

### ทำอะไร
- **harness** (`PCMSRegressionHarness` verify, DEV) ผ่านทุกเคส (exit 0) — baseline ถูกแช่แข็ง 2026-10-31 จึงยืนยันแค่ "ผลคงที่" ไม่ใช่เทียบก่อนแก้ · เคสที่ SKIP: `...ProductionOrderRP` ×2, `getWaitLotCaseBySaleOrder` (หาข้อมูลตัวอย่างไม่เจอ)
- **`PCMSServiceGsonCheck`** รัน 11 เคส → JSON 33 ไฟล์ ไม่ว่าง parse ได้ (ไม่ได้เทียบ A/B รอบนี้)
- **เครื่องมือใหม่ `tools/dev-web-local/`** (ดัดแปลงจาก LBMS): รัน WAR บน Tomcat แยก `C:/Users/94687/pcms2-dev-tomcat` พอร์ต 8082 ชี้ DEV ทั้ง 4 datasource
  ใช้ `src/test/resources/database.properties` (gitignored) · `DevTestLoginFilter` ล็อกอิน ZZTEST สิทธิ์ VIEWONLY + ปิด `TaskService` scheduler · README ในโฟลเดอร์มีขั้นตอน
- **ทดสอบหน้าจอจริง:** Summary ค้น G TECH + SO 1107011323 (เลือก radio All) ได้ 4 แถว ตรง baseline เห็น S2K032H (self-switch) · `/Main/*` ตอบ 200
- **navbar `f35756b`:** `aria-current`+เส้นใต้ถูกหน้า · 1366px แถวเดียว · 800px แถวเดียว ปุ่ม 44px · 375px 2 แถว ปุ่ม 44px หน้าไม่ล้น

### กับดัก/บทเรียน
- **Tomcat อยู่ `D:\apache-tomcat-9.0.100`** (เคยสรุปผิดว่าไม่มีเพราะหาแค่ C:)
- DEV PCMS2 มีสิทธิ์แค่ CUST01/CUST02/VIEWONLY (ไม่มี ADMIN/ITSUPP)
- bean ของ controller/service อยู่ใน context ของ DispatcherServlet (`FrameworkServlet.CONTEXT.PCMS2`) ไม่ใช่ root — filter รุ่นแรกหาผิดที่ ปิด scheduler ไม่ได้
- 🔴 สตาร์ตครั้งแรก 21:13 แล้ว stop 21:14:14 หลัง cron `:14` ผ่านไป 14 วินาที → **job ERP sync อาจรันกับ DEV (ไม่ใช่ PRD) ช่วงสั้นๆ ยืนยันไม่ได้** เพราะ log แอปเป็น NOP (ไม่มี slf4j binding) · อย่าสตาร์ตใกล้ 01:00 / :14 / :44
- หน้า Summary ค่าเริ่มต้นตัวกรองสถานะ = In-Process → SO ที่ปิดแล้วต้องเลือก All
- ที่ 800px หน้า Summary ล้นแนวนอน ~75px มาจากช่องกรอง bootstrap-select ในเนื้อหา (ไม่ใช่ navbar)

### 🔲 ครั้งหน้า
- [ ] ตรวจ DEV ว่ามีแถว job/log ช่วง 21:14 วันที่ 2026-11-01 หรือไม่ (ถ้าห่วง)
- [ ] ทดสอบหน้าจอที่ยังไม่ได้ไล่: Detail, Report, ตัวกรองวันที่/userStatus, เคส ProdWorkDate/SumGR ผ่านหน้าเว็บ
- [ ] แก้ล้นแนวนอนของช่องกรอง bootstrap-select ที่จอ ≤800px (ถ้าต้องรองรับแท็บเล็ต)
- [ ] push `gitlab_master` (วันที่ 2026-11-01 resolve host `twcgit.wacoal.co.th` ไม่ได้ — ต้องต่อ VPN/เครือข่ายบริษัท)
- [ ] หยุด Tomcat ทดสอบ (`catalina.bat stop`, CATALINA_BASE=`C:\Users\94687\pcms2-dev-tomcat`) เมื่อเลิกใช้

## ✅ 2026-11-01 (ค่ำ) — ทดสอบหน้าเว็บบน dev-web-local ต่อ: Detail + ตัวกรอง ผ่าน · Report CFM พังเพราะ DEV ขาดตาราง

สตาร์ต Tomcat 21:34 (ก่อน cron :44, log ยืนยัน `[DevTest] scheduled jobs OFF`) · หยุดแล้ว 21:40 (พอร์ต 8082 ปิด) · ทดสอบแบบอ่านอย่างเดียว ไม่เขียน DB

| หน้า/เคส | ผล |
|---|---|
| Detail ค้น SO 1107011323 (เลือก All) | ✅ 4 แถว ตรงกับ Summary · แถว self-switch `S2K032H` มีวันที่ย้อม (17/10, 22/10/2022) = fix `f317d48` ทำงานบนหน้าจริง |
| Detail userStatus = "ขายแล้ว" | ✅ 3 จาก 4 แถว (แถวที่ 4 สถานะ "ปิดเพื่อแก้ไข" ถูกกรองออกถูกต้อง) |
| Detail SO Date ในช่วง / นอกช่วง | ✅ 4 แถว / 0 แถว |
| Detail Prod.Date ปี 2022 / 2024 | ✅ 4 แถว / 0 แถว |
| Report CFM (`/Report/CFM/getReportDetail`) | ❌ HTTP 500 `Invalid object name 'PCMS.dbo.Z_ATT_CustomerConfirm2'` — **DEV ไม่มีตารางนี้** · branch นี้ไม่ได้แตะ DAO/Report นี้ (`git log 8371211..HEAD` ว่าง) → ไม่ใช่ regression · ยังไม่รู้ว่า PRD มีตารางหรือไม่ |

### กับดักที่เจอตอนทดสอบ (กันเสียเวลาซ้ำ)
- ปุ่ม **Clear Search ของหน้า Detail ล้างการเลือกลูกค้า/Cust.(Name4)/UserStatus/Division ทั้งหมด** แล้วปุ่ม Search จะไม่ยิงเลย (validation บรรทัด ~1442 ของ `PCMSDetail.jsp` ต้องมีครบทั้ง 4 กลุ่ม) → ได้ "0 แถว" แบบเงียบ ๆ ดูเหมือนบั๊ก · ทดสอบอย่ากด Clear ให้เซ็ตค่าทับเอง
- `getReportDetail` รับ **JSON array** (`[{...}]` แล้ว `list.get(0)`) ไม่ใช่ object เดี่ยว — ส่ง object ได้ 500 จาก Gson
- `javascript_tool` ตอน pane ซ่อน timeout 45 วินาที → แยกการค้นหาทีละเคส (รอ ≤8 วินาที/call)

### 🔲 ครั้งหน้า
- [x] ตรวจแล้ว (read-only metadata, 2026-11-01): **PRD มี** `PCMS.dbo.Z_ATT_CustomerConfirm2` (17,567 แถว, สร้าง 2025-05-13, แก้ล่าสุด 2026-04-23) · **DEV ไม่มีตาราง** แต่มี SP `spd_UpsertToZ_ATT_CustomerConfirm2` · DEV ล้าหลัง PRD: 76 vs 77 ตาราง, 1 vs 5 view, 30 vs 33 proc (proc ล่าสุดแก้ 2026-07-02 vs 2026-07-17) · SP `spd_*` ชุดเดียวกัน (DEV มีเกิน 2 ตัว: `spd_HandlerCustomerDetail`, `spd_OrderImport_20190408`)
- [x] สร้างตารางเปล่าบน DEV แล้ว (user รัน `sql/2026-11-01_01_create_Z_ATT_CustomerConfirm2_dev.sql`) · ทดสอบ 21:51: `/Report/CFM/getReportDetail` ตอบ **200 (0 แถว — ตารางว่าง)** ทั้งไม่กรองและกรอง SO · ไม่มี SEVERE ใหม่ · หยุด Tomcat แล้ว
- [ ] Report CFM ยังไม่ได้ทดสอบกับ "ข้อมูลจริง" และ Download Excel (`createExcelReportDetail`) — ตารางว่าง ต้องมีข้อมูลก่อนถึงจะตรวจ layout/ตัวเลขได้ · ปุ่ม Search ใน UI ไม่ยิง request เมื่อไม่ได้กรอกตัวกรอง (ยังไม่ได้ไล่ว่าเป็น validation ตามตั้งใจ)
- [ ] ยังไม่ได้ทดสอบ: Download Excel ของ Detail, หน้า Log, PRD
- [ ] push `gitlab_master` (ยังต้องต่อ VPN) · commit `PCMS2/CLAUDE.md` + `.claude/rules/*` ที่ค้าง

## ✅ 2026-11-01 (ดึก) — A/B ก่อน-หลัง fix ของ PERF_SEARCH ด้วย harness (DEV) — ผ่าน ไม่มีค่าเดิมถูกเปลี่ยน

วิธี: worktree โค้ดเก่า `8371211` (ก่อน 336eb0f/f317d48/33860c6) capture → ใช้เป็น baseline → verify กับโค้ดปัจจุบัน (`4630ca3`) · 22 เคส (Summary+Detail) · เพิ่มเคส 16 (`16_single_saleorder_switch_target.json`: PT/KB SO 1109009155 = คู่ switch จริงเดียวที่ DEV มีและมี sale)

| ผลต่าง old → new | จำนวน |
|---|---|
| ค่าไม่ว่างถูกเปลี่ยนเป็นค่าอื่น | **0** |
| แถวเพิ่ม | **0** |
| เซลล์ว่างที่มีค่า (วันที่ย้อม/CFM/GR/ฯลฯ) | 2,205 |
| แถวหาย | 12 ครั้งรวมทุกเคส (Summary+Detail หลายเคสซ้ำกัน) = **4 แถวไม่ซ้ำ** (แก้จากที่เคยบันทึกผิดว่า 6): Main self-switch `S2K032H` SO 1107011300, `S2K244` 1107011676, `W2B083/2` 1111002704, `S2F167` 1107011020) |

- แถวที่หาย = Main ที่ PO อยู่ใน SwitchProdOrder (รวม self-switch) ซึ่งโค้ด `main` เดิมก็ตัดออก (`getLeftJoinSwitchProdOrder` + `SPO.ProductionOrderSW IS NULL`) แต่ c6e6109 ทำให้หลุดกลับมา — ตรวจจาก **อ่านโค้ด `gitlab_master/main`** ไม่ได้รัน `main` จริง (ไม่มี harness)
- เคส 16: แถว SUB ได้วันที่ย้อม/สถานะย้อม และ P2K069 (ปลายทาง switch) ได้ GR Qty 124.20 KG / 331.00 MR (เดิมว่าง) = fix `f317d48`+`33860c6` ทำงานกับข้อมูลจริง
- ข้อสังเกตข้อมูล (ไม่ใช่ผลของ fix): `P2I114/3R` dyePlan 17/04/2025 แต่ dyeActual 12/10/2022 · Detail มีแถว SUB ซ้ำคู่ (เหมือนเดิมทั้ง old/new)
- ⚠️ **ข้อจำกัด:** DEV มี switch ไม่เป็น self แค่ 17 คู่ และมี sale แค่ 1 คู่ · ไม่มีคู่ที่ sale ต่างใบทั้งสองฝั่ง (PRD ก็ไม่มีตามจำนวนนับ: 2 จาก 30 มี sale) → เส้นทาง "PO คู่ OPSW ที่ sale ปลายทางอยู่นอก scope" ยังทดสอบด้วยข้อมูลจริงได้แค่ 1 เคส · ไม่ได้ดูข้อมูลระดับแถวของ PRD (ถูกบล็อกสิทธิ์อ่าน PRD)
- baseline ใน `src/test/resources/pcms2/regression/baseline/` (gitignored) ถูก re-freeze เป็นโค้ดปัจจุบันแล้ว (29 ไฟล์ รวมเคส 16) · เก็บ baseline เก่าไว้ใน scratchpad

## ✅ 2026-11-01 (ดึก 2) — ตรวจ Summary + ตัวกรอง และ navbar ทุกหน้าบน dev-web-local (DEV) — ผ่าน

สตาร์ต 22:08 (scheduler OFF ก่อน :14) · หยุดแล้ว · อ่านอย่างเดียว

- **Summary (`/Main`)** SO 1107011323 เลือก All: userStatus "ขายแล้ว" → 3 แถว (ตรงกับ Detail) · SO Date ในช่วง 4 / นอกช่วง 0 · Prod.Date ปี 2022 → 4 / 2024 → 0 · Due Date ในช่วง 4 / นอกช่วง 0
- **navbar** (`aria-current` ตั้งด้วย JS ฝั่ง client ไม่อยู่ใน HTML ที่ server ส่ง — ต้องตรวจใน DOM จริง ไม่ใช่ fetch): Main, Detail, Report/CFM, Log Production/Sale × ATT/ERP365 รวม 7 หน้า → `aria-current=page` ถูกลิงก์ทุกหน้า · ทุกหน้า HTTP 200 · ไม่ล้นแนวนอนที่ 1366px · หน้า Log ทั้ง 4 โหลดได้บน DEV
- ⚠️ **UX:** เมนู Report มี 2 คู่ที่ข้อความเหมือนกันเป๊ะ — "Log - Production Order" (ATT / ERP365) และ "Log - Sale Order" (ATT / ERP365) แยกไม่ออกจากชื่อ (ผู้ใช้ต้องรู้ว่าตัวไหนคือ ATT/ERP365 จากลำดับ) · **ยังไม่ได้แก้** — ต้องถาม user ว่าอยากให้ต่อท้ายชื่อว่าอะไร (`config/navbar.jsp`)
- ไม่ได้ตรวจ navbar ที่ 800/375px รอบนี้ (ตรวจไปแล้วรอบก่อนบน Summary)

## ✅ 2026-07-17 (ต่อ) — เพิ่ม ActivityCorrelator ThreadLocal sweeper เชิงป้องกัน

เพิ่ม `sweepThreadLocals()` + `sweepMap()` ใน `AppShutdownListener.java` — verify `mvn compile` BUILD SUCCESS
ดูรายละเอียดที่ memory `classloader_leak_checklist_new_projects.md` ข้อ 6a

**✅ DEPLOYED PROD แล้ว 2026-07-17** (fix ทั้ง aalto-xml exclusion + sweeper — ยืนยันด้วย full local-repro
ครั้งสุดท้ายก่อน deploy: deploy→request หลายครั้ง→undeploy→ไม่มี SEVERE→heap dump rootset ว่างเปล่า 100%)
— **🔲 ครั้งหน้า:** Find Leaks บน PROD ยืนยันอีกชั้น + สังเกตผลระยะยาว

## ✅ 2026-07-17 — Classloader leak: aalto-xml (จาก fastexcel-reader — คนละทางกับที่เคยแก้ผ่าน BGJobLib) — FIX + LOCAL-REPRO VERIFIED

**สำคัญ:** memory เดิม (`memory_leak_investigation.md`) เขียนว่า "PCMS2 ไม่ bundle BGJobLib เลย ไม่ต้องกังวล
aalto-xml" — **ถูกแค่ครึ่งเดียว** ไม่ bundle ผ่าน BGJobLib จริง แต่มี aalto-xml เข้ามาทางอื่น: **`org.dhatim:
fastexcel-reader`** (ประกาศตรงใน pom.xml) ลาก aalto-xml 1.3.2 มาด้วย — และ **fastexcel-reader เองก็ไม่มีใคร
เรียกใช้เลยในโค้ดทั้งระบบ** (grep dhatim/fastexcel = 0) เป็น dead-weight ซ้อน dead-weight

**Fix:** เพิ่ม `<exclusions>` ตัด `aalto-xml` ออกจาก `fastexcel-reader` ใน `pom.xml`

**Verify ครบ:** `mvn dependency:tree` ยืนยัน aalto-xml หายจริง + `mvn compile` BUILD SUCCESS (240 ไฟล์) +
**full local-repro** (deploy WAR จริงบน Tomcat 8.5.99 → ยิง request follow-redirect ถึงหน้า login → ยืนยัน
Jasper compile JSP จริง → undeploy → ไม่มี SEVERE เลย → heap dump + jhat → GC roots exclude-weak ว่างเปล่า
100%) — ปิดเคสระดับ local ได้ ดู memory ส่วนกลาง `inspectsystem_qcms_leak.md`

**🔲 ครั้งหน้า:** deploy DEV จริง → Find Leaks ยืนยันอีกชั้น → PRD

---

## ✅ 2026-07-17 — FIX: schedule toggle ครอบไม่ครบทั้ง 2 jobs (BUILD SUCCESS)

### อาการ / เป้าหมาย
สืบเนื่องจากพบใน PPMM2 ว่าปุ่ม "ปิด Schedule" ในหน้า JobManagement ครอบไม่ครบทุก `@Scheduled` — สำรวจ PCMS2 พบปัญหาเดียวกัน: `TaskService.java` มี `scheduleEnabled` + ปุ่ม toggle จริง แต่เช็คแค่ `sortBackGroundAfterGetERPDataProcedure` (1 ใน 2 jobs) ส่วน `bgJobHandlerDataFromOrgatex` (cron เที่ยงคืน, sync ข้อมูล Orgatex) ไม่เช็ค flag เลย

### แก้ / ทำอะไร
เพิ่ม guard `if (!scheduleEnabled) { log.info(...); return; }` ให้ `bgJobHandlerDataFromOrgatex()` เหมือนกับอีก job — ใช้ mechanism เดิมที่มีอยู่แล้ว (ปุ่ม `Setting/JobManagement` เดิม) ไม่ต้องเพิ่มอะไรใหม่

### 🔲 ครั้งหน้า
- [ ] deploy + ทดสอบว่ากด "ปิด Schedule" แล้ว Orgatex sync ข้ามจริงตอนเที่ยงคืน
- บริบทเต็ม (ทำไมต้องมีปุ่มนี้ครบ — DEV ชี้ DB PRD เป็นลิงค์สำรอง) ดู memory ส่วนกลาง `scheduled_jobs_toggle_audit.md`

---

## ✅ 2026-07-17 — Find Leaks entry ของ PCMS2 = false alarm ยืนยันด้วย heap dump (ปิดเคส ไม่ต้องแก้โค้ด)

### บริบท
user ให้ดู Tomcat Manager "Find Leaks" ที่ขึ้นชื่อ PCMS2/QCMS/PPMM2 (x2)/InspectSystem(x7) ค้างข้ามคืน — สืบ InspectSystem/QCMS/SFC เจอ root cause จริง (aalto-xml จาก BGJobLib ดู memory `memory_leak_investigation` + `BGJobApi/NEXT_SESSION.md`) **แต่ PCMS2 ไม่ได้ bundle BGJobLib เลย** ต้องหาแยก

### วิธีตรวจ (local repro บน Tomcat 8.5.69/JDK8 เดียวกับที่ยืนยัน InspectSystem)
1. Comment `@Scheduled` 2 ตัวใน `TaskService.java` ชั่วคราวก่อนเทส (กัน cron job จริงยิง API/DB) — **revert กลับเรียบร้อยแล้วหลังเทส** ยืนยัน `mvn compile` BUILD SUCCESS
2. `mvn clean package` → deploy local Tomcat → เจอ TLS1.0 negotiation error ตอน deploy (JDK 8 update ล่าสุดของเครื่อง test ปิด TLSv1/1.1 default — เป็นปัญหาสภาพแวดล้อมเครื่อง test ไม่เกี่ยวกับโค้ด) → ใช้ `-Djava.security.properties=` override ชั่วคราวแก้ปัญหานี้เฉพาะรอบทดสอบ → deploy สำเร็จ (HTTP 302)
3. Stop context → `jcmd GC.class_histogram` เจอ `ParallelWebappClassLoader` ค้าง 5 (baseline 4) แม้ force GC แล้ว
4. `jmap` heap dump → `jhat` → หา classloader ผ่าน `heap.findClass("...DatabaseConfig")` → เช็ค **"Exclude weak refs" (Path to GC Roots) = ว่างเปล่า 100%** — ไม่มี strong reference เลย
5. เช็ค "Include weak refs" — เจอแต่ `WeakHashMap`/`ClassLoaderLogManager` (JULI logging) ปกติ ไม่ใช่บั๊ก

### สรุป
**PCMS2 ไม่มี classloader leak จริงในโค้ด** — ที่ Tomcat Manager ขึ้นชื่อ PCMS2 ใน Find Leaks เป็น **false positive ปกติของเครื่องมือ** (Tomcat เตือนแค่ "ยังอยู่ในหน่วยความจำ" ไม่ได้แยกแยะว่า pin จริงหรือรอ GC cycle — ตัว warning เขียนกำกับไว้เองว่า "use a profiler to confirm") ไม่ต้องแก้โค้ดอะไรเพิ่ม

### 🔲 ครั้งหน้า (ไม่เร่งด่วน)
- [ ] ถ้า admin ยังกังวล PCMS2 ให้ลองกด Find Leaks ซ้ำหลัง full GC หรือรอเวลาผ่านไปสักพัก — น่าจะหายเอง (ไม่ใช่ pin ถาวร)

## ✅ 2026-07-16 (รอบ 2 ค่ำ) — Re-verify อิสระ (ต่อจาก LBMS/SFC jasper sweep): ตรงกับ audit รอบแรกทุกจุด ไม่เจอเพิ่ม

### บริบท
หลังเจอ Jasper Metaspace churn ใน LBMS (19 จุด) + SFC (1 จุด) ไล่ตรวจ PCMS2 ด้วย checklist เดียวกัน (connection release, thread/static/ThreadLocal, library ที่ generate class)

### ผลตรวจ (ยืนยันซ้ำ ตรงกับ entry ด้านล่างที่ทำไว้ก่อนหน้าในวันเดียวกัน)
- **Jasper: ไม่มีความเสี่ยง** — pom ไม่มี `net.sf.jasperreports` dependency เลย; property `jasper.version=6.16.0` เป็นค่าที่ไม่ได้ใช้จริง (เหลือค้าง ไม่ใช่ JasperReports)
- **POI (Excel):** `ExportExcelCFMReportService` — per-request `new`, `workbook.close()` ใน finally ครบ (ยืนยัน fix @Controller ด้านล่างยังอยู่ — ไม่มี annotation กลับมา)
- **jxls:** ใน pom แต่ไม่มีโค้ดเรียก `JxlsHelper` เลย = dead dependency ไม่มี churn
- **Connection release:** count-comparison `getConnection`/`releaseConnection` ครบ **7/7 ไฟล์** ไม่มีรั่ว (pattern เดียวกับที่เจอใน SFC EmployeeDetailDaoImpl — PCMS2 ไม่มี)
- **DatabaseConfig:** 4 HikariDataSource ครบ `destroyMethod=close`
- **SchedulerConfig:** `destroyMethod=shutdown` + daemon ครบ
- **web.xml:** AppShutdownListener ประกาศก่อน ContextLoaderListener (destroyed last) ถูกต้อง
- **ThreadLocal ทั้งหมด:** `FormatUtils`/`SqlStatementHandler` = SimpleDateFormat/DecimalFormat (JDK class, ไม่ pin classloader); `ERPAtechDaoImpl.timeFocusOverride` มี set/remove คู่ใน try-finally ที่ `BackGroundJobDaoImpl:255-265` — ปลอดภัย
- **TaskService.jobLocks** (ConcurrentHashMap) — key คงที่ 2 ตัว (`ORGATEX_IMPORT`/`ERP_SYNC_JOB`) ไม่ใช่ unbounded (ไม่ใช่ pattern แบบ SFC DashboardController ที่ key ผูก session id)
- **JobManagementController** — `setDaemon(true)` ที่แก้ไปตอนเช้ายังอยู่ครบ
- **kong.unirest** — เรียกแค่ `Unirest.shutDown()` ใน listener (defensive no-op, ไม่มีจุดสร้าง client จริงเพราะ BGJobApiService bean comment ออกหมดใน AppConfig) ไม่ใช่ปัญหา

### สรุป: PCMS2 ไม่มีอะไรต้องแก้เพิ่มจาก session นี้ — ครบทั้ง connection/lifecycle/thread/jasper/POI

---

## ✅ 2026-07-16 (ค่ำ) — Deep hunt leak audit + FIX กับดัก latent แล้ว (BUILD SUCCESS, รอ deploy)

### FIX ที่ทำ (2026-07-16 ค่ำ)
- `service/ExportExcelCFMReportService.java` — **เอา `@Controller` ออก** + ลบ import + ใส่ comment ห้ามทำเป็น Spring bean (ต้อง `new` ต่อ request เท่านั้น)
- ตรวจก่อนแก้: caller เดียวคือ `ReportController:130` ใช้ `new` (ไม่มี @Autowired/XML bean ที่ไหน) → เอา annotation ออกปลอดภัย
- ตรวจหลังแก้: `mvn compile` BUILD SUCCESS; กวาดซ้ำทั้งโปรเจกต์ไม่มี bean+workbook ตัวอื่น; `workbook.close()` ใน finally (:148-150) มีอยู่แล้วถูกต้อง
- ผล: Spring ไม่สร้าง singleton ถือ workbook เปล่าตอน startup อีก + ปิดกับดัก refactor เป็น @Autowired แล้วได้บั๊กแบบ PPMM2

### ผลตรวจ (ไม่มี active leak ใหม่นอก checklist เดิม)
- ThreadLocal ทุกจุดปลอดภัย (`ERPAtechDaoImpl.timeFocusOverride` มี set/remove คู่ใน try-finally ที่ `BackGroundJobDaoImpl:255-265`; ที่เหลือ value เป็น JDK class)
- `SqlStatementHandler` มี setQueryTimeout(300) + temp table DROP before/after ครบ + SET NOCOUNT reset
- ไม่มี live `new Database(` จาก core เลย — import ที่เห็นใน FromSap*/getDocStep เป็น dead code comment หมด
- session เก็บแต่ object เล็ก, ไม่มี Unirest instance ค้าง, ไม่มี addShutdownHook/MBean/Introspector
- pom: jxls/fastexcel/jasper property = dead weight ไม่มี import (ลบได้), ไม่มี SXSSF

### ⚠️ กับดัก latent: `service/ExportExcelCFMReportService.java`
Pattern เดียวกับบั๊ก PPMM2 (workbook+rowCount เป็น instance field สร้างใน constructor ไม่ reset) **แต่ยังไม่แสดงอาการ** เพราะ caller เดียว (`ReportController.java:130`) ใช้ `new ExportExcelCFMReportService()` ต่อ request + close ใน finally ถูกต้อง — ปัญหาคือ class ติด `@Controller` (ทั้งที่ไม่มี @RequestMapping) + PCMS2-servlet.xml component-scan package service → Spring สร้าง singleton ถือ workbook เปล่า 1 ใบตลอดอายุแอป และ**ถ้าวันไหนใครเปลี่ยน ReportController เป็น @Autowired ตัว bean นี้ (refactor ที่ดูถูกต้อง) จะได้บั๊กไฟล์ export ปนข้อมูลเก่าแบบ PPMM2 ทันที**

### 🔲 ครั้งหน้า
- [ ] เอา `@Controller` ออกจาก ExportExcelCFMReportService (หรือ refactor เป็น initWorkbook ต่อ request แบบ PPMM2) — แก้เล็ก ปลอดภัย
- [ ] (ไม่เกี่ยว leak) `info/*Info.java` hardcode DB/LDAP credentials — ควรย้ายเข้า properties ที่ไม่ commit

## ✅ 2026-06-26 — UX/UI Improvements

### ไฟล์ที่แก้
| ไฟล์ | สิ่งที่เปลี่ยน |
|---|---|
| `webapp/WEB-INF/pages/config/baseCSS.jsp` | Swal 2.1.2 → Swal2 11.1.7; FA 4.7 → FA 6.7.2 (`all.min.css`); load `tokens.css` ก่อน `style.css` |
| `webapp/WEB-INF/pages/config/footer.jsp` | `©2022` → `©` + `new Date().getFullYear()` |
| `webapp/WEB-INF/pages/config/searchDiv.jsp` | เพิ่ม `title="เลือกคอลัมน์..."` บน Column Setting button |
| `webapp/WEB-INF/pages/config/jquery.jsp` | ลบ `jquery-3.3.1.js` ซ้ำ (เหลือ 3.4.1 ตัวเดียว) |
| `webapp/WEB-INF/pages/PCMSMain.jsp` | Swal2 migrate + button disable AJAX + ลบ `button:"confirm"` (×2) + ลบ `done:` callbacks (×4) |
| `webapp/WEB-INF/pages/PCMSDetail.jsp` | Swal2 migrate + button disable AJAX + ลบ `button:"confirm"` (×10) + ลบ `done:` callbacks (×6) |
| `webapp/WEB-INF/pages/SaleOrderLog.jsp` | Swal2 migrate + button disable AJAX |
| `webapp/WEB-INF/pages/ProductionOrderLog.jsp` | Swal2 migrate + button disable AJAX |
| `webapp/WEB-INF/pages/CFMReport.jsp` | Swal2 migrate + button disable AJAX |
| `webapp/WEB-INF/pages/PermitManagement.jsp` | Swal2 migrate |
| `webapp/WEB-INF/pages/Setting/JobManagement.jsp` | Swal2 migrate |
| `webapp/resources/js/web-app.js` | Swal2 migrate (×4) + ลบ `done:` callbacks (×4) + `VIRTUAL_PRD_ORDERS` constant refactor |
| `webapp/resources/vendor/fontawesome-free-6.7.2/` | **NEW** — FA 6.7.2 local copy (solid + v4-shims) |
| `webapp/resources/css/tokens.css` | **NEW** — 40+ CSS custom properties (status colors, DataTable, group rows G1–G8, etc.) |
| `webapp/resources/css/style.css` | 36 hardcoded colors → `var(--pcms-*)` |
| `webapp/resources/css/datatable.overide.css` | 10 hardcoded colors → `var(--pcms-*)` |
| `webapp/resources/css/style_overide.css` | 54 hardcoded colors → `var(--pcms-*)` |
| `webapp/resources/css/alert.css` | 2 hardcoded colors → `var(--pcms-*)` |
| 8 JSP ไฟล์ (FA icon classes) | `fa fa-X` → `fas fa-X` (23 occurrences) |

### ⚠️ หมายเหตุสำคัญ
- **Swal2 API ใหม่** — ห้ามใช้ `swal()` อีก; ใช้ `Swal.fire({ ... })` เสมอ; `PCMSDetailV2.jsp` ข้ามตามคำสั่ง user
- **CSS tokens** — แก้สี → แก้ใน `tokens.css` ที่เดียว (`resources/css/tokens.css`)
- **FA 6.7.2** — ใช้ `fas fa-X` (solid); v4-shims ใน all.min.css handle icon renames อัตโนมัติ
- แก้ typo `.bg-planning { background-color: sandyrown }` → `var(--pcms-status-planning)` ไปด้วย

### 🔲 ต้องทำ
- [ ] Build + functional test ครบ flow (PCMSMain, PCMSDetail, Report, icons ทุกหน้า)

---

## ✅ 2026-06-24 — Migrate LDAP login → `ActiveDirectory` ตัวใหม่

### `controller/LoginController.java`
- เปลี่ยนจาก 2-step `ActiveDirectory.getContext(info,..) + getAttributes(LdapContext,..)` → **1-step** `ActiveDirectory.getAttributes(userId, password, "dc=atech,dc=co,dc=th", "(&(cn="+userId+")(objectClass=*))", provider)`
- ลบ `import AdInfo` + outer `try-catch NamingException` (ตัวใหม่จัดการภายใน + try-with-resources)
- เพิ่ม `user.setPassword("")` ใน callback → ไม่เก็บ password ใน session
- **local-user DB fallback คงเดิม** (ตรวจ `temp.getStatus()` หลัง LDAP — logic ไม่เปลี่ยน)
- core class: เดิม `ActiveDirectory20260427` → rename เป็น `ActiveDirectory` (ดู core/NEXT_SESSION.md)
- `mvn clean compile` (JDK 1.8) → **BUILD SUCCESS**

### 🔲 ต้องทำ
- [ ] Rebuild + redeploy → หยิบ core.jar ใหม่ (pool=false leak fix)
- [ ] ทดสอบ login AD จริง + login local-user (IsLocalUser=1) ต้องเข้าได้ (fallback ยังทำงาน); password ผิด → error

---

## ✅ 2026-06-20 — Code Review Fixes (HikariCP migration — thread-safety / connection cleanup / credentials)

### Fix 1 — SimpleDateFormat instance fields → ThreadLocal (CRITICAL)
**ไฟล์**: `utilities/SqlStatementHandler.java` (lines 77-84)
- 8 fields `sdf1–sdf4`, `sdf10–sdf12`, `sdfFullDatetime` เดิมเป็น `public SimpleDateFormat` ใน class ที่ถูก `new SqlStatementHandler()` เก็บใน singleton `PCMSDetailDaoImpl.sshUtl` → หลาย thread ใช้ `SimpleDateFormat` เดียวกัน → **corrupt dates**
- แก้: เปลี่ยนทุก 8 fields เป็น `ThreadLocal.withInitial(() -> new SimpleDateFormat(...))`
- อัปเดต callers ใน `setSqlDate()` / `setSqlTimeStamp()` → `.get().parse()` / `.get().format()`

### Fix 2+3 — queryList() rewrite: DROP บน connection เดียวกัน + while-guard (HIGH)
**ไฟล์**: `utilities/SqlStatementHandler.java` (lines 46-78)
- เดิม: `jdbc.execute(dropSql)` (line 47) กับ `jdbc.execute(StatementCallback)` (line 49) checkout connection แยกกันจาก HikariCP — DROP บน connection A แต่ main SQL วิ่งบน connection B → stale `#temp` ไม่ถูก clear ภายใต้ concurrent load
- แก้: ย้าย DROP เข้าใน `StatementCallback` ด้วย `stmt.execute(dropSql)` — รับประกัน same connection
- เพิ่ม `int guard = 50` กัน `while(true)` infinite loop ถ้า JDBC driver คืน `0` แทน `-1`

### Fix 4 — Credentials ออกจาก source code (HIGH)
- สร้าง `src/main/resources/database.properties` (gitignored) — credentials ทั้ง 4 DB (PCMS/PPMM/SOR/ERP)
- `config/DatabaseConfig.java` → `@PropertySource` + `@Value("${key}")` แทน hardcode strings
- `.gitignore` เพิ่ม `src/main/resources/database.properties`
- ⚠️ ไฟล์ `database.properties` ต้องสร้างใหม่บนทุก machine หลัง clone — ไม่อยู่ใน git

### Fix 5 — HandlerListLog.java: SDF ซ้ำกับ FormatUtils (LOW)
- ลบ `SDF_DDMMYYY_HHMMSS_1` ThreadLocal (`dd/MM/yyyy HH:mm:ss`) ออก — ซ้ำกับ `FormatUtils.DAY_MONTH_YEAR_TIME_FORMAT`
- `formatValue()` → ใช้ `FormatUtils.DAY_MONTH_YEAR_TIME_FORMAT.get().format(...)` แทน
- คง `df` ("#.##") ไว้ — format ต่างจาก `FormatUtils.TWO_DECIMAL_FORMAT` ("###,###,##0.00")

### Bug fix เพิ่มเติม (ตรวจพบหลัง code review) — queryList() connection cleanup
- **DROP-after ไม่รันถ้า main SQL throw**: เพิ่ม `try-finally` รอบ main SQL → `finally` block รัน DROP-after เสมอแม้ SQL ผิดพลาด → connection กลับ pool สะอาด
- **`SET NOCOUNT ON` ติดค้างที่ connection**: HikariCP ไม่ reset session state → connection ถัดไปที่ใช้ connection เดียวกันอาจได้ `jdbc.update()` rows-affected = 0 → เพิ่ม `stmt.execute("SET NOCOUNT OFF;")` ใน `finally` block ก่อน DROP-after

### Build
- ✅ `mvn clean compile` (JDK 1.8) ผ่าน exit code 0

---

## ✅ 2026-06-20 — thread-safety (ThreadLocal) + ลบ jtds

### แก้ thread-safety (static SimpleDateFormat/DecimalFormat ไม่ thread-safe → race ตอนหลาย request)
- `utilities/FormatUtils.java` — 10 SimpleDateFormat + 4 DecimalFormat → `ThreadLocal.withInitial(...)`; format methods ใช้ `.get().format(...)`; `getXxxFromMap` ส่ง `FMT.get()` (เช็คแล้วไม่มี external ref ตรง field)
- `utilities/HandlerListLog.java` — `SDF_DDMMYYY_HHMMSS_1` + `df` → ThreadLocal (เดิม `public static` ไม่ thread-safe; เช็คแล้วไม่มีใครเรียกจากนอก จึงเปลี่ยนเป็น private ThreadLocal)

### ลบ jtds (dead weight)
- `pom.xml` — comment `net.sourceforge.jtds:jtds:1.2.4` ออก (เช็คแล้ว: ไม่มี code ใช้ `jdbc:jtds:` / ไม่มี SPI auto-register) → war เล็กลง ✅ ยืนยัน war ไม่มี jtds แล้ว

### Build
- ✅ war 22:32 — jtds หาย, core + mssql 9.4.1 ครบ, ThreadLocal compile ผ่าน

---

## ✅ 2026-06-20 — Security review (เต็มสูบ): แก้ SQL injection ทั่ว DAO + qualifier

รีวิว PCMS2 ทั้งหมด (4 มิติ: thread/scheduler, ThreadLocal/static, lifecycle, SQL) — **leak โค้ดสะอาด** (รายละเอียดในหัวข้อ leak ด้านล่าง) แต่เจอ SQL injection กระจายทั้ง DAO layer

### แก้ SQL injection
- **pre-auth (ร้ายแรงสุด)**: `EmployeePermitsDaoImpl.getEmployeePermitsDetailByUserId` — `userId` จากฟอร์ม login (ยังไม่ auth) ต่อ string เข้า SQL → เปลี่ยนเป็น **bind param `?`** (`queryForList(sql, userId)`)
- **ทั่ว DAO (~28 ไฟล์)**: prodOrder/saleOrder/saleLine/customerNo/date strings ต่อ string ตรงๆ → escape ด้วย `.replace("'","''")` (convention ที่ codebase ใช้อยู่แล้ว) — แก้ทั้ง injection + บั๊ก apostrophe; ครอบคลุม FromSap*/PCMSDetail/Plan*Date/Inspect*/ShopFloorControl/RollFromSap/SwitchProdOrder/ReplacedProdOrder/ERPAtech/FromSapSubmitDate/Z_ATT_CustomerConfirm2
- **column-name injection**: `PCMSDetailDaoImpl.getReplacedCaseByProdOrder` ใช้ `this.C_PRODORDER`/`this.C_PRODORDERRP` (ค่าคงที่) แทน param `prdOrderType` ตรงที่ต่อเป็นชื่อคอลัมน์ → ป้องกัน identifier injection

### แก้ qualifier
- `PPMM/UserStatusDetailDaoImpl` — query `[PPMM].dbo` แต่เดิม `@Qualifier("pcmsDatabase")` → เปลี่ยนเป็น **constructor injection `@Qualifier("ppmmDatabase")`** + ลบ constructor เปล่าที่ค้าง

### ยังไม่แก้ (รอตัดสินใจ — correctness ไม่ใช่ security/leak)
- `FormatUtils.java` + `HandlerListLog.java` — static `SimpleDateFormat`/`DecimalFormat` ไม่ thread-safe (ควรเป็น ThreadLocal) — fields เป็น `public` ต้องเช็ค external ref ก่อนแก้
- jtds 1.2.4 ใน pom ไม่ถูกใช้ (ลบได้ hygiene)

### Build
- ✅ `mvn clean package -DskipTests` (JDK 1.8) ผ่าน → war 22:21 (มี core + mssql 9.4.1 + ทุก fix)

---

## ✅ 2026-06-20 — แก้ classloader leak: mssql-jdbc 6.1.0 → 9.4.1.jre8

### อาการ
หลัง reload/undeploy Tombcat log: `/PCMS2 ... classes from previous runs are still loaded ... memory leak` (PCMS2 ค้าง ×3 reload)

### สาเหตุ (จุดที่ AppShutdownListener ครอบไม่ถึง)
PCMS2 ยังเป็น **mssql-jdbc 6.1.0.jre8** — 6.x สร้าง **non-daemon Timer thread** ถือ webapp classloader ค้างแม้ปิด pool + deregister driver แล้ว (PPMM2/BGJOB อัปไป 9.4.1 แก้ตัวนี้ตั้งแต่ migration แต่ PCMS2 ตกหล่น)

### แก้
- `pom.xml` — mssql-jdbc **6.1.0.jre8 → 9.4.1.jre8** (ไม่ใช้ 10+/12.6.1 เพราะ default `encrypt=true` ชน SSL; 9.4.1 default `encrypt=false` → connection string เดิมใช้ได้)
- ✅ verify: HikariCP 4 pool มี `@Bean(destroyMethod="close")` ครบ (Spring ปิดให้ — ไม่ใช่ตัว leak), AppShutdownListener ปิด Unirest + deregister driver อยู่แล้ว
- ✅ rebuild war 14:34 → มี mssql 9.4.1 (ไม่เหลือ 6.x) + core + PATINDEX fix ครบ

### 🔲 ต้องทำ
- [ ] redeploy war 14:34 → reload/undeploy แล้วดู log ต้อง**ไม่มี** PCMS2 ใน leak warning อีก
- [ ] regression: ต่อ DB ทั้ง 4 (PCMS/PPMM/SOR/ERP) ได้ปกติ (9.4.1 encrypt=false)

> InspectSystem ที่ยังขึ้น leak = คนละเคส (plain-Servlet/scheduler — ดู memory inspectsystem_qcms_leak) ยังไม่แตะ

---

## ✅ 2026-06-20 — แก้บั๊ก SQL PATINDEX `' ''` ซ้ำหลายไฟล์ (หลัง deploy คลิก getPrdDetailByRow error)

### อาการ
หลัง redeploy WAR ใหม่สำเร็จ (405 หายแล้ว — controller ทำงาน POST) คลิก row → `getPrdDetailByRow` → `Incorrect syntax` จาก SQL ของ `RollFromSapService` (stack: PCMSMainDaoImpl:926 → RollFromSapDaoImpl:52)

### สาเหตุ = บั๊กเดียวกับ bug #3 (FromSapMainSale) แต่ copy-paste กระจายหลาย DAO
`CASE PATINDEX('%[^0 ]%', col + ' '')` มี **3 single-quote** (ที่ถูก = `' '` 2 quote เหมือน `ELSE SUBSTRING` บรรทัดถัดไป)

### แก้ 4 ไฟล์ใน PCMS2
- `dao/master/implement/PPMM/RollFromSapDaoImpl.java` (PurchaseOrderLine)
- `dao/master/implement/FromSapCFMDaoImpl.java` (SaleLine)
- `dao/master/implement/FromSapSaleDaoImpl.java` (SaleLine)
- `dao/master/implement/FromSapSaleInputDaoImpl.java` (SaleLine)
- ✅ grep ทั้ง workspace `+ ' '')` = CLEAN (SFC มี FromSapCFM ตัวเดียวกัน แก้ใน repo SFC แล้ว)

### 🔲 ต้องทำ
- [ ] rebuild PCMS2 + redeploy (WAR) อีกรอบ

### ⚠️ บทเรียน
- `' ''` เป็น pattern บั๊กซ้ำ เจอครั้งใด **grep `+ ' '')` ทั้ง workspace ทันที** อย่าแก้เฉพาะจุดที่ error ชี้

---

## ✅ 2026-06-20 — core dependency กลับเข้า pom (แก้ deploy พัง: loginController NoClassDefFoundError)

### อาการ
deploy WAR ขึ้น Tomcat แล้ว `Servlet.init() for servlet [PCMS2] threw exception` → root cause `BeanCreationException: 'loginController' ... Failed to introspect Class LoginController` = `NoClassDefFoundError` ของ `th.in.totemplate.core.authen.ActiveDirectory`

### สาเหตุ
`pom.xml` comment dependency `core` (`th.co.wacoal:core:1.0-SNAPSHOT`) ไว้ แต่ `.classpath` ยังมี Eclipse project ref `/core` → **compile ผ่านใน Eclipse แต่ WAR ไม่มี core** → runtime หา class AD ไม่เจอ (LoginController ใช้ `ActiveDirectory` / `AuthenAttributes`)

### แก้
- `pom.xml` — uncomment `<dependency> core </dependency>` (พร้อม exclusion `javax.servlet-api`)
- โปรเจกต์เป็น m2e-wtp → `mvn package` bundle maven dep เข้า `WEB-INF/lib` อัตโนมัติ
- ✅ **verify แล้ว**: `target/PCMS2.war` มี `WEB-INF/lib/core-1.0-SNAPSHOT.jar` — deploy WAR ได้เลย error หาย

### ⚠️ ห้ามทำ
- **ห้าม comment/ลบ dependency `core` ใน pom** — core (package `th.in.totemplate.core`) ให้ AD/SQL/FTP ที่ LoginController ใช้ Eclipse compile ผ่าน (เพราะ .classpath มี `/core` ref) จะหลอกว่าไม่จำเป็น แต่ WAR จะพังตอน runtime

---

## ✅ 2026-06-20 — Classloader Leak Fix + Data-Retrieval Bug

### Leak fix (undeploy/reload)
- `listener/AppShutdownListener.java` — เพิ่ม `kong.unirest.Unirest.shutDown()` ก่อน JDBC driver deregister (Unirest 3.11.11 อยู่ใน classpath แต่ไม่เคยปิด → Apache HttpClient pool ค้างหลัง undeploy)

### Functional fix — "เรียกข้อมูลมีปัญหา"
- `dao/implement/PCMSMainDaoImpl.java::getPrdDetailByRow` — **ขาด `SET NOCOUNT ON`** ตอนต้น SQL ที่สร้าง temp table หลายตัว → update-count packets มาก่อน ResultSet ทำ JDBC คว้า result ผิด → เพิ่ม `SET NOCOUNT ON;` (mirror `getPCMSSumaryDetail`)
- จุดเดียวกัน: escape single-quote ของ `prdOrder` (`SqlStatementHandler.queryList` helper ไม่รับ bind param) กัน SQL injection

### หมายเหตุ (ตรวจแล้ว — ไม่แก้ เพราะปลอดภัยอยู่แล้ว)
- `PCMSDetailDaoImpl.upSertRemarkCaseThree` concat `tableName`/`caseSave` เป็น SQL identifier แต่ caller `PCMSDetailProcess.saveInputDetail` บังคับเป็น literal whitelist ผ่าน if/else → ไม่ exploitable ผ่าน path จริง

### Verify หลัง deploy
- [ ] undeploy/reload ผ่าน Tomcat Manager → log ไม่มี "PCMS2 ... classes from previous runs are still loaded"
- [ ] คลิก row ใน dashboard เปิด popup (getPrdDetailByRow) → ต้องได้ข้อมูลกลับครบ (ก่อนแก้อาจ blank/error)

---

## ✅ 2026-06-23 — Job Management UI (ERP Sync)

### สิ่งที่ทำ
- `dao/master/erp/atech/ERPAtechDao.java` — เพิ่ม `setTimeFocusForCurrentThread` + `clearTimeFocusForCurrentThread`
- `dao/master/implement/erp/atech/ERPAtechDaoImpl.java` — ThreadLocal `timeFocusOverride`; `getActiveTimeFocus()` แทน field; apply ทุก method
- `service/master/erp/atech/ERPAtechService.java` — delegation `setRepairDateFrom` / `clearRepairDate`
- `dao/BackGroundJobDao.java` + `impl` — `runFullErpSyncWithDateRange(from,to)` (set override → 5 steps no-arg → finally clear)
- `service/BackGroundJobService.java` — delegation
- `service/TaskService.java` — `scheduleEnabled` flag + `runManual(from,to)` + getters
- `controller/JobManagementController.java` — **NEW**: GET page + `/api/status` + `/api/run` + `/api/schedule/toggle` + `validateRange` ≤7 วัน
- `webapp/WEB-INF/pages/Setting/JobManagement.jsp` — **NEW** (bootstrap-datetimepicker inline)
- `webapp/WEB-INF/pages/config/navbar.jsp` — เพิ่ม Setting dropdown (ADMIN/ITSUPP) + Job Management link

### ✅ 2026-06-23 — Permit Management UI

- `dao/master/PermitsDao.java` — เพิ่ม `updatePermit`
- `dao/master/implement/PermitsDaoImpl.java` — implement `getPermitsDetail()` (fix DB: `[PCMS2]` → `[PCMS]`) + `updatePermit`
- `dao/master/UsersDao.java` — เพิ่ม `updateUserPermit`
- `dao/master/implement/UsersDaoImpl.java` — UPSERT EmployeePermits (IF EXISTS UPDATE ELSE INSERT)
- `service/master/PermitsService.java` — inject UsersDao; เพิ่ม `updatePermit`, `getUsers`, `updateUserPermit`
- `controller/PermitManagementController.java` — **NEW**: GET page + `/api/permits` + `/api/users` + `/api/permit/update` + `/api/user/permit`
- `webapp/WEB-INF/pages/Setting/PermitManagement.jsp` — **NEW**: 2 tab (permit flags + user assignment)
- `webapp/WEB-INF/pages/config/navbar.jsp` — เพิ่ม Permit Management ใน Setting dropdown
- Rule: ADMIN ลบ/แก้ flag ของ ADMIN row เองไม่ได้; ITSUPP view-only

### SQL
- [x] ✅ รัน `sql/2026-06-23_add_admin_itsupp_permits_pcms2.sql` (DEV)
- [ ] ยืนยันรัน PRD แล้วหรือยัง

### 🔲 ต้องทำ
- [ ] `mvn clean package -DskipTests` (JDK 1.8)
- [ ] Functional test: login ADMIN/ITSUPP → Setting → Job Management; toggle schedule + Run Now (from/to ≤7 วัน) + ตรวจ log `[ERP-sync-manual]`; ยืนยัน runManual ครบ 5 ขั้น (ZATT/customer/saleorder/productionorder/SP)
- [ ] Functional test: Setting → Permit Management; Tab1 แก้ flag save; Tab2 assign permitId ให้ user
- [ ] ทดสอบ lock: กด Run ขณะ schedule กำลังรัน → "job กำลังทำงานอยู่"

---

## 🔥 ลำดับงานครั้งหน้า

### 1. SQL รอรันใน SSMS
- ไม่มี SQL patch ค้าง

### 2. Deploy + ทดสอบ

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-1.8"
mvn clean package -DskipTests
```

**Feature BUILD OK แต่ยังไม่ทดสอบ:**
- [ ] HikariCP migration — functional test ครบ flow (PCMSMain dashboard, PCMSDetail popup, PCMSDetailV2, Report)
- [ ] `searchByDetail` — test ค้นหาแบบมีเงื่อนไขหลายตัว (CREATE INDEX batch fix)

---

## 🐛 Open Bugs

ไม่มี bug เปิดอยู่ (runtime errors ทั้ง 5 จุด แก้แล้ว 2026-06-19)

---

## 📋 งานที่เสร็จแล้ว session นี้ (2026-06-19)

### HikariCP Migration — Runtime Bugs Fixed (5 จุด)

| # | Error | ไฟล์ | แก้อย่างไร |
|---|---|---|---|
| 1 | `Failed to get driver instance for jdbcUrl=jdbc:sqlserver://` | `DatabaseConfig.java` | เพิ่ม `cfg.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")` ทุก 4 bean |
| 2 | `No qualifying bean of type 'th.in.totemplate.core.sql.Database'` | `PCMSMainProcess.java` | ลบ dead `Database database` parameter ออกจาก constructor |
| 3 | `Incorrect syntax near ':'` | `FromSapMainSaleDaoImpl.java` line 231 | แก้ `' ''` (3 quotes) → `' '` (2 quotes) ใน PATINDEX |
| 4 | `The statement did not return a result set` (batch with CREATE TABLE + INSERT) | `SqlStatementHandler.java` | เพิ่ม `SET NOCOUNT ON` + เปลี่ยน 7 `queryForList()` → `queryList()` |
| 5 | `The statement did not return a result set` (batch with CREATE INDEX) | `SqlStatementHandler.java` | เปลี่ยน `queryForList()` → `stmt.execute()` + iterate `getMoreResults()` |

### Docs อัปเดต
- `CLAUDE.md`, `backend/CLAUDE.md`, `database/CLAUDE.md` — อัปเดต Database core lib → JdbcTemplate
- `docs/architecture.md` — เติม PCMS2 values แทน template placeholders
- `.claude/rules/api-contract.md` — trim เหลือ Gson-only (ตัด Jackson/ApiResponse ออก)
- `.claude/rules/backend-patterns.md` — เพิ่ม `stmt.execute()` note + แก้ qualifier ตัวอย่าง
- `.claude/rules/security.md` — trim เหลือ FilterLogin-only (ตัด Spring Security ออก)
- `.claude/rules/git-workflow.md` — อัปเดต pre-commit checklist ให้ PCMS2-specific

---

## 🔖 Context สำหรับ session ถัดไป

- HikariCP 4.0.3 + mssql-jdbc **9.4.1.jre8** (อัปจาก 6.1.0 — แก้ non-daemon Timer thread leak)
- Qualifiers คงเดิม: `pcmsDatabase` (@Primary), `ppmmDatabase`, `sorDatabase`, `erpDatabase`
- Credentials: `src/main/resources/database.properties` (gitignored) — **ต้องสร้างใหม่บนทุก machine**
- `PCMSDetailV2DaoImpl` — ยังไม่ได้ migrate (ข้ามไว้ตอน migrate เพราะ logic ซับซ้อน)
- `SqlStatementHandler.queryList()` — ทุก operation (DROP-before, main SQL, DROP-after) อยู่ใน `StatementCallback` เดียวกัน; `finally` รัน `SET NOCOUNT OFF` + DROP-after เสมอแม้ SQL throw
- `SqlStatementHandler.sdf1-sdf4, sdf10-12, sdfFullDatetime` เป็น `ThreadLocal<SimpleDateFormat>` (instance field ใน singleton sshUtl) — ใช้ `.get().parse()` / `.get().format()`
