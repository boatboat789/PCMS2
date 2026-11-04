# แผนทดสอบ: การแก้ SQL injection / temp table บน branch `claude/vigilant-ptolemy-g4o3sm`

ไฟล์นี้เขียนให้ Claude session บนเครื่องที่มี Eclipse + เครือข่ายถึง DB/เซิร์ฟเวอร์ PCMS2 ใช้ทดสอบ
(session ที่เขียนไฟล์นี้คอมไพล์และเชื่อมต่อ DB ไม่ได้ จึง **ยังไม่มีการทดสอบใด ๆ** ผลทุกข้อต้องมาจากการรันจริง)
รายละเอียดช่องโหว่และเหตุผลอยู่ใน `SQL_INJECTION_REVIEW.md`

## 0. กฎความปลอดภัย (ต้องทำตามก่อนเริ่ม)
- ทดสอบกับ **DB ทดสอบ/สำเนา** เท่านั้น ห้ามรันกับ production ถ้าแยกไม่ได้ให้หยุดและถามผู้ใช้
- ใช้ payload แบบ **อ่านอย่างเดียว** (ดูหัวข้อ 3) ห้ามใช้ payload ที่ `DROP`/`DELETE`/`UPDATE`/`xp_cmdshell` หรือรูปแบบทำลายข้อมูลอื่น แม้เพื่อพิสูจน์ช่องโหว่
- ห้ามเปลี่ยนโค้ดเพื่อให้ทดสอบผ่าน ถ้าพบปัญหาให้รายงานตามหัวข้อ 6 แล้วถามผู้ใช้
- ห้ามสร้าง PR / push โดยไม่ได้รับคำสั่ง ห้ามใส่รหัสผ่านหรือ secret ลงในรายงาน
- ถ้าข้อมูลที่ต้องใช้ (ProductionOrder ตัวอย่าง, บัญชีผู้ใช้) ไม่มี ให้ถามผู้ใช้ อย่าเดา

## 1. เตรียมการ
1. `git fetch origin claude/vigilant-ptolemy-g4o3sm && git checkout claude/vigilant-ptolemy-g4o3sm && git pull`
2. commit ที่เกี่ยวข้อง (ใหม่ → เก่า) เทียบกับ baseline `c6e6109` (ก่อนแก้):
   - `389aac3` escape ใน DAO ที่เหลือ 62 จุด / 28 ไฟล์ (FromSap*, Inspect*, LBMS, PPMM, Switch/Replaced ProdOrder, Plan*, Permits/Users/ConfigCustomerUser/SearchSetting/ColumnSetting/EmployeePermits)
   - `ecf785f` escape `CustomerNo` (4 จุด)
   - `3cb5bf0` `buildDateClause` whitelist `dd/MM/yyyy` (V1 + V2)
   - `cecf693` escape ใน `buildLikeClause`, `buildSaleStatusClause`, Log API 4 DAO, `Z_ATT_CustomerConfirm2DaoImpl`
   - `23b74b6` `escSql` ใน `PCMSDetailDaoImpl` (6 จุด)
   - commit `fix: getPrdDetailByRow drop temp tables and escape ProductionOrder` (`PCMSMainDaoImpl` บรรทัด ~884, ~913)
   - ที่เหลือเป็นเอกสาร (`.md`)
3. **Build ใน Eclipse ก่อน** (ผู้ใช้ใช้ Eclipse; session ต้นทางคอมไพล์ไม่ได้) ต้อง compile ผ่านโดยไม่มี error ใหม่ ถ้าไม่ผ่านให้หยุดและรายงาน error ตามจริง
4. รัน baseline และ branch นี้เทียบกัน: deploy `c6e6109` (หรือใช้ worktree แยก) บันทึกผลของกรณีปกติ (หัวข้อ 2) แล้ว deploy HEAD ของ branch และเทียบ **ผลลัพธ์ต้องเหมือนกันทุก field** (ยกเว้นกรณีที่ระบุว่าตั้งใจเปลี่ยน)
5. จดชื่อ DB, เซิร์ฟเวอร์, บัญชีผู้ใช้ที่ใช้ทดสอบ (ไม่ต้องจดรหัสผ่าน) และขอข้อมูลตัวอย่างจากผู้ใช้: ProductionOrder ที่มีจริง 2–3 ตัว (รวมที่มี Switch / OrderPuang / WaitLot ถ้ามี), SaleOrder+SaleLine, ชื่อลูกค้าจริง, CustomerNo ที่มี config

Base URL: `http://<host>:8080/PCMS2` · ทุก endpoint ต้อง login ก่อน (`FilterLogin` ครอบ `/*`) · ใช้ session cookie เดียวกันกับเบราว์เซอร์ หรือ login ผ่าน `/login/loginAuth`

## 2. กรณีปกติ (regression) — ผลต้องเหมือน baseline

| # | หน้า/endpoint | ทำอะไร | คาดหวัง |
|---|---|---|---|
| R1 | `/Main` หน้า search หลัก | ค้นหาด้วยลูกค้า/สถานะที่เลือกตามปกติ (ไม่ใส่วันที่) | ผลเท่า baseline, ไม่มี popup Fail |
| R2 | `/Main` | ใส่ช่วงวันที่ Sale Order Date / Prd Order Date / Due Date (daterangepicker) | ผลเท่า baseline |
| R3 | `/Main` | ใส่ข้อความ LIKE: PO, Sale Number, Article, Design, Lab No, Prd Order, Material, Sale Order | ผลเท่า baseline |
| R4 | `/Main` | ลอง Sale Status แต่ละค่า (O / X / C / อื่นๆ ที่หน้ามีให้เลือก) | ผลเท่า baseline |
| R5 | `/Main` | คลิกแถวเพื่อเปิด modal รายละเอียด (เรียก `POST /Main/getPrdDetailByRow`) | ข้อมูลครบเท่า baseline |
| R6 | `/Detail` และ `/DetailV2` | ค้นหา แล้วขยายแถว/เปิดรายละเอียด (Switch Prod Order, OrderPuang, WaitLot, Normal) | ผลเท่า baseline (ครอบคลุม `getNormalCaseByProdOrder`, `getWaitLotCaseBySaleOrder`, `getSwitchProdOrderListByPrd`, `getOrderPuangListByPrd`, `getOrderPuangSWListByPrd` ตาม `PCMSDetailProcess`) |
| R7 | `/Log/ProductionOrderLog/{dataType}` และ `/Log/SaleOrderLog/{dataType}` | ใช้ทั้ง `dataType=ERP365` และค่าอื่น (SAP) พร้อมช่วงวันที่และเลขออเดอร์ | ผลเท่า baseline |
| R8 | `/Report/CFM/Detail` | ค้นด้วย Prod ID, Lot, Customer, SO, Send Date, Reply Date | ผลเท่า baseline |
| R9 | `/Main` และ `/Detail*` (โหลดหน้า) | login ด้วยผู้ใช้ที่มี `ConfigCustomerUser` | dropdown ลูกค้าแสดงถูก (ครอบคลุม `getCustomerNameDetail` / `getCustomerShortNameDetail` ที่แก้ escape `CustomerNo`) |
| R10 | ทุกหน้า | วันที่ว่าง (Clear) | ไม่ error ไม่กรองวันที่ |
| R11 | `POST /Main/getPrdDetailByRow` ด้วย ProductionOrder จริง **ที่มีข้อมูลครบทุกแหล่ง** | เปิด modal รายละเอียดแล้วดูทุกส่วน: Roll/PO (RollFromSap), Packing, Import/Lab (LBMS ImportDetail), CFM, Sale, Submit Date, NC (Inspect), Shop Floor (SFC), Inspect Orders, Dyeing, Preset, Receipe, WaitTest, WorkInLab, FromSapMainProd | ข้อมูลทุกส่วนเท่า baseline (ถ้าส่วนไหนว่างทั้ง baseline และ branch ให้จดว่า "ไม่มีข้อมูลให้เทียบ") |
| R12 | `/Detail` และ `/DetailV2` | ในแต่ละแถวเปิดช่องวันที่แผน: CFM Plan Date, CFM Plan Lab Date, Delivery Plan Date, Send CFM Cus Date (เรียก `POST .../getCFMPlanDateDetail`, `getCFMPlanLabDateDetail`, `getDeliveryPlanDateDetail`, `getSendCFMCusDateDetail`) | ประวัติวันที่เท่า baseline |
| R13 | `/Detail` และ `/DetailV2` | **บันทึกวันที่แผน** (`POST .../saveInputDate`) ทั้ง `cfmPlanDate`, `cfmPlanLabDate`, `deliveryDate` ด้วยแถวทดสอบ แล้วบันทึกซ้ำวันเดิม | ครั้งแรกสำเร็จ (`Update Success.`) ครั้งที่สอง `Date : ... already confirm.` เหมือน baseline และจำนวน count เพิ่มตามปกติ — **เขียนข้อมูล ใช้ DB ทดสอบเท่านั้น** |
| R14 | `/Detail` และ `/DetailV2` | **บันทึก Remark** (`POST .../saveInputDetail`) กรณี `switchRemark` และ `replacedRemark` ที่ผ่าน `SwitchProdOrderDaoImpl` / `ReplacedProdOrderDaoImpl`, และ `POST .../getSwitchProdOrderListByRowProd` | ผลเท่า baseline; ข้อมูลที่เขียนลงตาราง `SwitchProdOrder` / `ReplacedProdOrder` ถูกต้อง — **เขียนข้อมูล ใช้ DB ทดสอบเท่านั้น** |
| R15 | login ปกติ (ทั้งผู้ใช้ที่ผ่าน AD และผู้ใช้ในตาราง `Users`) | login แล้วเข้า `/Main` `/Detail` `/DetailV2` | ผ่านโดยไม่ error; โหลดสิทธิ์ (`Permits`, `EmployeePermits`, `Users`), ตัวเลือกลูกค้า (`ConfigCustomerUser`), ตั้งค่าคอลัมน์ (`ColumnSetting`: โหลดและ `POST .../saveColSettingToServer`), ค่าเริ่มต้นค้นหา (`SearchSetting`: `POST .../saveDefault` แล้ว `POST .../loadDefault`) ทำงานเท่า baseline |

## 3. การเปลี่ยนพฤติกรรมที่ตั้งใจ — ต้องตรวจ

| # | กรณี | คาดหวัง |
|---|---|---|
| C1 | `buildDateClause`: ช่วงวันที่ปกติ `01/01/2026 - 31/01/2026` | ทำงานเหมือนเดิม |
| C2 | วันที่เดียว `15/01/2026` (ไม่มี `-`) | ถูกตีความเป็นช่วงวันเดียว ไม่เกิด `ArrayIndexOutOfBoundsException` |
| C3 | วันที่รูปแบบอื่น เช่น `2026-01-15 - 2026-01-31` หรือข้อความมั่ว | request ล้มเหลว (exception `Invalid date range: <column>` ใน log ของ server) หน้าเว็บแสดง popup `Fail / Please contact to IT` |
| C4 | หน้าไหนส่งวันที่ผิดรูปแบบ `dd/MM/yyyy` ในการใช้งานปกติ? | ไล่ทุกหน้าที่มี daterangepicker (Main, Detail, DetailV2, CFMReport) ว่าค่าที่ส่งตรง `dd/MM/yyyy` เสมอ ถ้ามีหน้าที่ส่งรูปแบบอื่นให้ **รายงานเป็นปัญหา** |
| C5 | พิมพ์วันที่เองในช่อง daterangepicker (วันเดียว/ผิดรูปแบบ) แล้วค้นหา | จดว่าช่องส่งค่าอะไรไป server จริง (ดูใน DevTools Network) และผลที่ผู้ใช้เห็น |
| C6 | เมื่อ C3 เกิดขึ้น (request POST) ดูว่าผู้ใช้เห็นอะไร: popup Fail เฉย ๆ หรือ HTTP 405 / หน้า error อื่น (`/handleError` รับเฉพาะ GET) | บันทึก HTTP status จริง, body, และข้อความใน log |
| C7 | หลัง C3 ลอง login/redirect: `ErrorController` เก็บ `originalUrl` ใน session | บันทึกว่ามีผลกับการ redirect หลัง login ครั้งถัดไปหรือไม่ |

## 4. ทดสอบ escape / injection (read-only เท่านั้น)

ใช้ค่าต่อไปนี้ในแต่ละช่องที่ระบุ แล้วเทียบกับค่าปกติ

- **P1:** `O'Brien` (มี `'` ตัวเดียว) — ต้อง **ไม่เกิด SQL error** (เดิมจะเกิด syntax error ที่ baseline: ใช้ยืนยันว่าแก้แล้ว) ผลลัพธ์ว่างหรือไม่ตรงเป็นเรื่องปกติ
- **P2:** `x' OR '1'='1` — ต้อง **ไม่คืนแถวทั้งหมด** (ที่ baseline ช่องที่ไม่ escape อาจคืนทุกแถว) เทียบจำนวนแถวกับการค้นด้วยค่าที่ไม่มีอยู่จริง ต้องเท่ากัน (ปกติ 0)
- **P3:** `x'; SELECT 1 --` — ต้องไม่เกิด error SQL ที่เกี่ยวกับ syntax และไม่มีผลข้างเคียง

ช่อง/endpoint ที่ต้องทดสอบด้วย P1–P3 (ทุกข้อ: เทียบกับ baseline `c6e6109` ว่าเดิมพังหรือรั่ว และที่ branch นี้ต้องปลอดภัย):

| # | endpoint / ช่อง | ฟิลด์ |
|---|---|---|
| I1 | `POST /Main/getPrdDetailByRow` body `[{"productionOrder": "<P>"}]` | `productionOrder` |
| I2 | `POST /Main/searchByDetail` (ต้องมี `customerNameList`, `customerShortNameList`, `userStatusList`, `divisionList`, `distChannel`, `saleStatus` ตามที่หน้า Main ส่งจริง — ให้ดูตัวอย่าง payload จาก DevTools ของการค้นปกติแล้วแทนค่าทีละฟิลด์) | `saleNumber`, `materialNo`, `saleOrder`, `labNo`, `articleFG`, `designFG`, `purchaseOrder`, `productionOrder`, `deliveryStatus`, `saleStatus` |
| I3 | `POST /Detail/searchByDetail`, `POST /DetailV2/searchByDetail` | ฟิลด์เดียวกับ I2 |
| I4 | endpoint Detail ที่ใช้ `PCMSSecondTableDetail` (ดู `PCMSDetailProcess`: เปิดรายละเอียดแถว) และ `POST /Detail/getSwitchProdOrderListByPrd`, `POST /DetailV2/getSwitchProdOrderListByPrd` | `productionOrder`, `productionOrderRP`, `saleOrder`, `saleLine` |
| I5 | `POST /Log/ProductionOrderLog/{dataType}/getProductionOrderLogBySearch` body `{productionOrder, changeDateStart, changeDateEnd}` | `productionOrder` (วันที่ใช้รูปแบบ `dd/MM/yyyy` ปกติ; ทดสอบ P1 ที่วันที่ด้วย: ค่าที่มี `'` ต้องไม่ทำให้ SQL พัง แต่จะ error เรื่องแปลงวันที่ได้) |
| I6 | `GET /Log/SaleOrderLog/{dataType}/getSaleOrderLogBySearch?saleOrder=<P>&changeDateStart=..&changeDateEnd=..` | `saleOrder` |
| I8 | `POST /Main/getPrdDetailByRow` (ต่อจาก I1 แต่เน้นว่า payload ไม่ทำให้ **query ย่อยใด ๆ** พัง) | `productionOrder` — ดู log ของ server ว่าไม่มี SQL error จาก DAO ใด ๆ ใน R11 (FromSap*, Inspect*, LBMS ImportDetail, PPMM, SubmitDate, FromSapMainProd) |
| I9 | `POST /Detail/getCFMPlanDateDetail`, `getCFMPlanLabDateDetail`, `getDeliveryPlanDateDetail`, `getSendCFMCusDateDetail` และของ `/DetailV2` (body `PCMSSecondTableDetail[]`) | `productionOrder`, `saleOrder`, `saleLine` และวันที่ `cfmPlanDate` / `cfmPlanLabDate` / `deliveryDate` (วันที่ทดสอบด้วย P1: ต้องไม่ทำให้ SQL syntax พัง; error แปลงวันที่เป็นพฤติกรรมที่ยอมรับได้) |
| I10 | `POST /Detail/saveInputDate` และ `/DetailV2/saveInputDate` | `productionOrder`, `saleOrder`, `saleLine` — **ใช้ค่าที่ไม่มีอยู่จริงในแถวทดสอบ ห้ามให้ INSERT เขียนข้อมูลขยะลงตารางจริง**; ตรวจว่าแถวที่เขียน (ถ้ามี) เก็บค่าที่มี `'` ตามตัวอักษร ไม่ใช่ถูกตัด/ตีความเป็น SQL และลบแถวทดสอบทิ้งหลังจบ (ถ้าผู้ใช้อนุญาต) |
| I11 | `POST /Detail/saveInputDetail`, `/DetailV2/saveInputDetail` กรณี `switchRemark`, `replacedRemark` | `productionOrder`, `productionOrderSW`/`productionOrderRP`, `saleOrder`, `saleLine` (ผ่าน `SwitchProdOrderDaoImpl`, `ReplacedProdOrderDaoImpl`) — ข้อควรระวังเดียวกับ I10 |
| I12 | `POST /Detail/getSwitchProdOrderListByRowProd`, `/DetailV2/...` | `productionOrder` |
| I7 | `GET /Report/CFM/getReportDetail?data=<json>` ที่ `json` = `[{"prodID":"<P>","lotNo":"","replyDateRange":"","customerName":"","so":"","sendDateRange":""}]` แทนค่า P ทีละฟิลด์ `prodID`, `lotNo`, `customerName`, `so` | `prodID`, `lotNo`, `customerName`, `so` |

เกณฑ์ผ่านข้อ I: (ก) ไม่มี SQL syntax error จาก P1 (ข) P2/P3 ไม่ให้ผลลัพธ์มากกว่าการค้นด้วยค่าที่ไม่มีอยู่ (ค) ไม่มีผลข้างเคียงต่อข้อมูล ตรวจ log ของ server ด้วย

## 5. Temp table (Pattern A) — `getPrdDetailByRow`

1. บน DB ที่ทดสอบ ก่อนเริ่มรัน:
   ```sql
   SELECT name FROM tempdb.sys.tables WHERE name LIKE '#temp%' OR name LIKE '#tmp%' OR name LIKE '#BillBatchFlag%' OR name LIKE '#FlagHas%';
   ```
   (temp table ท้องถิ่นของ connection อื่นอาจมองไม่เห็นจาก session ของคุณ ถ้าเห็นไม่ชัด ให้ใช้ `sys.dm_exec_sessions`/`sys.dm_db_session_space_usage` หรือเปิด trace/Extended Events ตามที่ DBA สะดวก — ถ้าทำไม่ได้ ให้บันทึกว่า "ตรวจ temp table ที่ค้างไม่ได้" ห้ามเดา)
2. เรียก `POST /Main/getPrdDetailByRow` ติดกัน 20 ครั้ง สลับกับ `POST /Main/searchByDetail` และ `POST /Detail/searchByDetail` หลายครั้ง
3. คาดหวัง: ไม่มี error "There is already an object named '#...'" ไม่มี temp table จาก method นี้ค้างหลังจบ (เทียบ baseline ที่ค้างหลัง `getPrdDetailByRow`)
4. เทียบเวลาตอบสนองของ `getPrdDetailByRow` กับ baseline (ควรไม่ช้าลงอย่างมีนัยสำคัญ)

## 6. การตรวจสิทธิ์ลูกค้า (ยังไม่ได้แก้ — ตรวจเพื่อยืนยันข้อค้นพบ)

ทำเมื่อมีบัญชีผู้ใช้ประเภทลูกค้า (`Users.isCustomer`) และ config `ConfigCustomerUser` เท่านั้น ถ้าไม่มีให้ **ถามผู้ใช้** อย่าสร้างบัญชีเอง

| # | กรณี | คาดหวังตามที่คาดไว้ (ยังไม่ยืนยัน) |
|---|---|---|
| A1 | login เป็นลูกค้า A แล้วส่ง `POST /Main/searchByDetail` โดยใส่ `customerNameList` / `customerShortNameList` ของลูกค้า B | ถ้าได้ข้อมูลของ B = สิทธิ์ไม่ถูกบังคับฝั่งเซิร์ฟเวอร์ (บันทึกจำนวนแถว ไม่ต้องแนบข้อมูลจริงลงรายงาน) |
| A2 | ลูกค้า A เรียก `POST /Main/getPrdDetailByRow` ด้วย ProductionOrder ของลูกค้า B | ถ้าได้ข้อมูล = ไม่มีการตรวจสิทธิ์ระดับ ProductionOrder |
| A3 | ลูกค้า A เปิด `/Detail` และ `/DetailV2` ได้หรือไม่ และเห็นข้อมูลต่างจากหน้า Main อย่างไร | บันทึกตามจริง |
| A4 | ผู้ใช้ภายใน (ไม่ใช่ลูกค้า) เห็นทุกลูกค้า | ยืนยันพฤติกรรม |

## 6.5 ข้อควรรู้เรื่องค่า `userId` / `permitId` / `forPage`
รอบ `389aac3` เพิ่ม escape ให้ DAO ที่ต่อ `userId` (จาก session), `permitId` (จากแถวใน DB), `forPage` — **ทดสอบแค่การใช้งานปกติ (R15)** ห้ามพยายาม login ด้วย username ที่มี `'` หรืออักขระพิเศษ LDAP กับ AD จริง (ดูหัวข้อ 7)

## 7. ที่ตั้งใจไม่ทดสอบ / ทดสอบไม่ได้ (บอกผู้ใช้ตามจริง)
- LDAP injection ใน `AdInfo.getSearchFilter` — ยังไม่ได้แก้ และไม่มีซอร์ส `ActiveDirectory` (ไลบรารี `core` build ใน Eclipse ของผู้ใช้); **ห้ามลองยิง payload ใส่ AD จริง** ถ้าผู้ใช้อยากตรวจให้ขอ `core` source แล้วอ่านโค้ดก่อน
- รหัสผ่าน AD ที่ฝังใน `AdInfo.java` — ไม่ต้องทดสอบ ต้องให้เจ้าของระบบเปลี่ยนรหัส
- `FromSapCFMDaoImpl:190,205,218` ยังต่อ `systemUser` โดยไม่ escape (ค่าภายในระบบ ที่มายังไม่ยืนยัน) — ไม่มีเคสทดสอบ ถ้าสงสัยให้รายงาน ไม่ต้องลองยิง
- `/Main/getEncrypted/{userId}` (key/salt ฝังโค้ด, IV เป็นศูนย์, ใช้ userId ใดก็ได้) — ยังไม่ได้แก้ และยังไม่ทราบว่าระบบ LBMS/INSPECT/SFC/QCMS ใช้ค่านี้ยืนยันตัวตนหรือไม่; ถ้าต้องตรวจให้ถามผู้ใช้ก่อน ห้ามใช้ค่าที่เข้ารหัสไปเข้าระบบอื่นในนามผู้ใช้อื่น
- การสแกนหา SQL injection เป็น regex จับรูปแบบ `'" + ตัวแปร`; รูปแบบอื่น (เช่น `String.format` กับค่าจาก request, ตัวเลขที่ต่อโดยไม่มี `'`) อาจหลุด — ถ้า Claude ฝั่งทดสอบเจอ error SQL ที่ไม่คาดคิดจาก payload ใด ให้รายงานไฟล์:บรรทัดจาก stack trace
- `startsWith("/resources/")` ใน `FilterLogin` กับ path แปลก ๆ — ยังไม่ยืนยัน; ถ้าทดสอบให้ทำกับ environment ทดสอบและรายงานผลดิบ

## 8. รูปแบบรายงานผล (ให้ใส่ในไฟล์ `TEST_RESULTS.md` ที่ root ของ `PCMS2/` โดยไม่ commit/push จนกว่าผู้ใช้สั่ง)

สำหรับทุกข้อ (R*, C*, I*, หัวข้อ 5, A*):
- สถานะ: **PASS / FAIL / ทดสอบไม่ได้ (พร้อมเหตุผล)**
- หลักฐาน: คำสั่ง/URL/payload (ปิดบังข้อมูลลูกค้าและ secret), HTTP status, ข้อความ error, ข้อความจาก log ที่เกี่ยวข้อง
- เทียบ baseline `c6e6109` เมื่อทำได้
- ปัญหาที่พบแยกเป็นหัวข้อ "ปัญหา" พร้อมไฟล์:บรรทัดที่สงสัย — **ไม่แก้โค้ดเอง** ให้ถามผู้ใช้ก่อน
- ท้ายรายงาน: สรุปจำนวน PASS/FAIL/ไม่ได้ทดสอบ และรายการที่ยังไม่แน่ใจ

อย่าสรุปว่า "ผ่านทั้งหมด" ถ้ามีข้อใดทดสอบไม่ได้ ให้ระบุว่าไม่ได้ทดสอบข้อใดบ้าง
