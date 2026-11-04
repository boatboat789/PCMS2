# SQL Injection / LDAP Injection Review (กลุ่ม B + LDAP + ผลตรวจเพิ่มเติม)

วันที่ตรวจ: 2026-11-04 · branch: `claude/vigilant-ptolemy-g4o3sm`
วิธีตรวจ: อ่านโค้ดและไล่ call chain เท่านั้น (ไม่ได้รันระบบ ไม่ได้ทดสอบโจมตีจริง ไม่ได้คอมไพล์)
พื้นฐาน: ทุกข้อด้านล่างอ้างอิงบรรทัดที่อ่านจริงในรีโพนี้ ข้อที่ยืนยันไม่ได้ถูกแยกไว้ในหัวข้อ "ยืนยันไม่ได้"

path ย่อ: `S = PCMS2/src/main/java/th/co/wacoal/atech/pcms2`

## 1. สถานะการแก้ไขที่ทำไปแล้ว

| Commit | ไฟล์ | สิ่งที่แก้ |
|---|---|---|
| fix: getPrdDetailByRow ... | `S/dao/implement/PCMSMainDaoImpl.java` (884, 913) | escape `prdOrder` + ใช้ `SqlStatementHandler.queryList(..., dropAllTemp, ...)` |
| fix: escape request-supplied values ... | `S/dao/implement/PCMSDetailDaoImpl.java` | helper `escSql` ครอบ SaleOrder/SaleLine/ProductionOrder 6 จุด |

## 2. กลุ่ม B: ผลที่ไล่ตาม call chain แล้ว (ยืนยันจากโค้ด)

### B1. Log API: ProductionOrderLog / SaleOrderLog — เข้าถึงจาก request ได้ (ยังไม่แก้)
- `S/controller/ProductionOrderLogController.java:86-96` รับ `@RequestBody SearchCriteria` แล้วส่ง `changeDateStart`, `changeDateEnd`, `productionOrder` ต่อไป
- `S/controller/SaleOrderLogController.java:60-70` รับ `@RequestParam saleOrder, changeDateStart, changeDateEnd`
- DAO ที่ต่อ string เข้า SQL ตรง ๆ (ทั้งวันที่และเลขออเดอร์):
  - `S/dao/master/implement/erp/atech/ERPAtechDaoImpl.java:551-558` (`getFromErpMainProdDetailWithRangeOfChangeDate`), `:737-744` (`getFromErpMainSaleDetailWithRangeOfChangeDate`)
  - `S/dao/master/implement/FromSapMainProdDaoImpl.java:78-93`, `S/dao/master/implement/FromSapMainSaleDaoImpl.java:~100`
- วันที่ถูกต่อเป็น `convert(date,'" + changeDateStart + "', 103)` โดยไม่ escape
- ผล: เข้าถึงได้เมื่อ login แล้ว (ผ่าน `FilterLogin`) ความเสี่ยงสูง

### B2. Report CFM — เข้าถึงจาก request ได้ (ยังไม่แก้)
- `S/controller/ReportController.java:84-107` `GET /{ReportType}/getReportDetail?data=...` แปลง JSON เป็น `Z_ATT_CustomerConfirm2Detail` แล้วเรียก `zccService.getZ_ATT_CustomerConfirm2DetailByProductionOrder(prodID, lotNo, replyDateRange, customerName, so, sendDateRange)`
- Service `S/service/master/Z_ATT_CustomerConfirm2Service.java:20-26` ส่งต่อไป `Z_ATT_CustomerConfirm2DaoImpl.getZ_ATT_CustomerConfirm2Detail`
- DAO `S/dao/master/implement/Z_ATT_CustomerConfirm2DaoImpl.java:141-150` ต่อ `custName`, `so`, `prodOrder`, `lotNubmer` ใน `LIKE '...%'` ตรง ๆ และเรียก `PCMSSqlService.buildDateClause` สำหรับวันที่
- (ที่ผมเคยรายงานก่อนหน้านี้ว่าอาจเป็นโค้ดไม่ได้ใช้ ผิด: มี caller จริงที่ `ReportController`)

### B3. `PCMSSqlService.buildDateClause` และ `buildLikeClause` ไม่ escape — กระทบ search หลัก (ยังไม่แก้) ⚠️ สำคัญสุด
- `S/service/PCMSSqlService.java`: `buildDateClause` ใช้ `String.format` ต่อ `dateArray[0]/[1]` ลง SQL ตรง ๆ, `buildLikeClause` ต่อ `value` ลง `like '%s%%'` ตรง ๆ
- ใช้ 24 จุดใน `PCMSSqlService.java`, 24 จุดใน `PCMSSqlServiceV2.java`, 2 จุดใน `Z_ATT_CustomerConfirm2DaoImpl.java`
- ค่าที่ส่งเข้ามา เช่น `saleNumber, materialNo, saleOrder, saleCreateDate, labNo, articleFG, designFG, po, prdOrder, prdCreateDate, dueDate, deliveryStatus` มาจาก `bean.getXxx()` ของ `PCMSTableDetail` ที่ controller รับด้วย `@RequestBody` (อ่านที่ `PCMSSqlService.java:~1060-1090`)
- ข้อสรุปเดิมของผมที่ว่า `PCMSSqlService`/`V2` escape แล้ว ถูกเฉพาะ `categorizeUserStatus` และ `buildListClause`/`buildListClauseByArray` (แปลง `'` เป็น `''`) ไม่รวม `buildLikeClause` กับ `buildDateClause`
- เพิ่มเติม `buildDateClause` ใช้ `dateRange.split("-")` แล้วเข้าถึง `dateArray[1]` ถ้ารูปแบบไม่ใช่ `a - b` จะเกิด `ArrayIndexOutOfBoundsException` (ปัญหาความเสถียร ไม่เกี่ยวกับ injection)
- ผล: เส้นทางที่ใช้บ่อยที่สุดของระบบ (หน้า search หลัก) ยังไม่ปลอดภัย

### B4. จุดที่ตรวจแล้วไม่ใช่ request โดยตรง (เสี่ยงต่ำกว่า)
- `S/dao/master/implement/CustomerDetailDaoImpl.java:100-112` ต่อ `CustomerNo` ที่ split จาก `ConfigCustomerUserDetail.getCustomerNo()` ซึ่งอ่านจากตาราง config ใน DB (`ConfigCustomerUserDaoImpl.java:47`) ไม่ใช่จาก request ผมยังไม่ได้ตรวจว่าใครเขียนค่านี้ลง DB (ดูหัวข้อ "ยืนยันไม่ได้")
- `ColumnSettingDaoImpl.java:54`, `SearchSettingDaoImpl.java:57`, `EmployeePermitsDaoImpl.java:69`, `UsersDaoImpl.java:92`, `ConfigCustomerUserDaoImpl.java:51` ต่อ `userId` ที่มาจาก `session.getAttribute("user")` (ตั้งใน `LoginController`) เส้นทางนี้เชื่อมกับข้อ LDAP ด้านล่าง
- `PermitsDaoImpl.java:94,137-138` ต่อ `permitId`/`userId` — `permitId` มาจากแถวใน DB (`LoginController:182`)

## 3. LDAP / การ login

ยืนยันจากโค้ด:
- `S/info/AdInfo.java:46` `getSearchFilter(username)` คืน `"(&(cn=" + username + ")(objectClass=*))"` โดยไม่ escape อักขระพิเศษของ LDAP (`* ( ) \ NUL`)
- `S/controller/LoginController.java:109-111` เรียก `ActiveDirectory.getContext(info, user.getUserId(), user.getPassword())` แล้วค้นด้วย filter ข้างบน โดย `user.getUserId()` คือ `userId` ที่ผู้ใช้พิมพ์
- ถ้าผ่าน AD (`temp.setStatus(true)`) โค้ดตั้ง `session.setAttribute("user", userId)` ด้วยค่าที่ผู้ใช้พิมพ์ดิบ ๆ (`LoginController:~124`) แล้วค่านี้ถูกนำไปต่อ SQL ในข้อ B4 (userId ฝั่ง SQL)
- เส้นทาง login ด้วยตาราง `Users` ใช้ `PreparedStatement` (`LogInDaoImpl.java:36,96-100`) ปลอดภัย
- **พบข้อมูลลับในซอร์ส:** `AdInfo.java` มี username/password ของ AD service account เขียนฝังไว้ (บรรทัด 22-23) ไม่ได้ใช้ในไฟล์ที่ผมค้นพบนอกจาก `AdInfo` เอง ผมไม่ได้คัดลอกค่าลงเอกสารนี้ ควรถือว่ารหัสรั่วไหลแล้ว (อยู่ใน git history) และควรเปลี่ยนรหัส + ย้ายไป config/env

## 4. ยืนยันไม่ได้ (ต้องการข้อมูลหรือการตัดสินใจจากคุณ)

1. **`ActiveDirectory.getContext` / `ActiveDirectory.getAttributes`** อยู่ในไลบรารี `th.co.wacoal:core:1.0-SNAPSHOT` (pom.xml) ซึ่งไม่มีใน repo นี้และไม่มี `~/.m2` ในเครื่องผม จึงไม่รู้ว่า (ก) `getContext` bind ด้วย userId/password ที่ผู้ใช้พิมพ์จริงหรือ bind ด้วยบัญชี service แล้วค้นหา (ข) ค่า `userId` ถูก escape ก่อนหรือไม่ — ต้องการซอร์สหรือ jar ของ `core` เพื่อสรุปว่า LDAP injection และการเข้า session ด้วย `userId` แปลก ๆ ทำได้จริงไหม
2. **ใครเขียนค่า `ConfigCustomerUserDetail.CustomerNo`** ลง DB — ผมไม่ได้ไล่หา endpoint ที่ insert/update ตารางนี้ ถ้าเขียนจาก request ผู้ใช้ จะกลายเป็น second-order injection ที่ `CustomerDetailDaoImpl`
3. **`FilterLogin`** ครอบคลุม URL ทั้งหมดที่เกี่ยวข้อง (รวม `/getReportDetail`, log API) หรือไม่ — ผมเห็นเพียงบรรทัดตรวจ session ที่ `FilterLogin.java:31` ยังไม่ได้อ่าน url-pattern ใน `web.xml`/config
4. **Database class ของ core library** (`th.in.totemplate.core.sql.Database`) มี API แบบ parameterized ที่ทีมใช้ได้หรือไม่ (`queryList(sql, args...)` ถูกใช้ที่ `PCMSDetailDaoImpl:1403` แสดงว่ามี) ผมยังไม่ได้ดูซิกเนเจอร์จริง

## 5. ข้อเสนอลำดับการแก้ (ยังไม่ได้ทำ รอการอนุมัติ)

1. `PCMSSqlService` + `PCMSSqlServiceV2`: เพิ่ม escape ใน `buildLikeClause` และ `buildDateClause` (จุดเดียว กระทบ 48+ call site) และกัน `split("-")` ที่ไม่มีส่วนที่สอง
2. Log API (B1): escape `productionOrder`/`saleOrder` และวันที่ ใน 4 DAO
3. `Z_ATT_CustomerConfirm2DaoImpl` (B2): escape 4 ค่าที่ใช้ใน `LIKE`
4. เปลี่ยนรหัส AD service account และย้ายออกจากซอร์ส (ต้องทำโดยเจ้าของระบบ)
5. LDAP: escape `userId` ใน `AdInfo.getSearchFilter` (ตาม RFC 4515) หลังตอบข้อ 4.1
6. ระยะยาว: เปลี่ยนเป็น parameterized query แทน escape

## 6. ข้อจำกัด
- สแกนด้วย regex และอ่านเฉพาะโค้ด Java ใน `src/main/java` ไม่ได้ดู JSP/JS และ config
- ไม่ได้คอมไพล์ ไม่ได้รัน ไม่ได้ทดสอบกับ DB

## 7. สถานะหลังแก้ (อัปเดตรอบที่ 2)

แก้แล้ว (escape `'` เป็น `''`, ไม่เปลี่ยนโครงสร้าง SQL):
- B3: `PCMSSqlService` และ `PCMSSqlServiceV2` — `buildLikeClause`, `buildDateClause` (ทั้งสองส่วนของช่วงวันที่) และกรณี `default` ของ `buildSaleStatusClause` ที่ต่อ `saleStatus` จาก request ตรง ๆ (เจอเพิ่มระหว่างแก้)
- B1: `ERPAtechDaoImpl` (2 เมธอด), `FromSapMainProdDaoImpl`, `FromSapMainSaleDaoImpl` — วันที่เริ่ม/สิ้นสุด และ productionOrder/saleOrder (null-safe)
- B2: `Z_ATT_CustomerConfirm2DaoImpl` — `custName`, `so`, `prodOrder`, `lotNubmer`

ยังไม่ได้ทำ / ไม่ได้เปลี่ยน:
- ข้อ LDAP (`AdInfo.getSearchFilter`) และรหัสผ่านใน `AdInfo.java` — ยังไม่แก้ เพราะไลบรารี `core` (build ใน Eclipse ของผู้ใช้) ไม่อยู่ใน repo จึงยืนยันพฤติกรรมของ `ActiveDirectory.getContext` ไม่ได้
- ยังไม่ได้ compile / ทดสอบกับ DB จริง

### อัปเดตรอบที่ 3: `buildDateClause`
- แก้ใน `PCMSSqlService` และ `PCMSSqlServiceV2` ตามตัวเลือก C ที่ผู้ใช้เลือก: whitelist รูปแบบ `dd/MM/yyyy` (regex `\d{2}/\d{2}/\d{4}`), วันที่เดียว = ช่วงวันเดียว, รูปแบบอื่นหรือเกิน 2 ส่วนโยน `IllegalArgumentException("Invalid date range: <column>")`
- แทนที่การ escape ในรอบก่อน (whitelist แน่นกว่า) และแก้ `ArrayIndexOutOfBoundsException`
- ยังไม่ได้ตรวจว่า exception นี้ไปถึงผู้ใช้อย่างไร (controller/JS) และยังไม่ได้ตรวจทุกหน้าว่าส่งรูปแบบ `dd/MM/yyyy` เท่านั้น (ดูเฉพาะ JSP ที่ใช้ daterangepicker)

### เส้นทาง exception ของ `buildDateClause` (ตรวจจากโค้ด ไม่ได้รันระบบ)
- `buildDateClause` ถูกเรียกจาก `PCMSSqlService.buildWhereClauses` (ใช้ใน `PCMSMainDaoImpl:576`, `PCMSDetailV2DaoImpl:163,1103`, `PCMSDetailDaoImpl:951`) และ `Z_ATT_CustomerConfirm2DaoImpl:152-153`
- ไม่มี try/catch บนเส้นทาง controller → logic/service → dao ของ search ทั้ง 3 controller (`try` ที่พบคือ `PCMSMainController:246-265` เป็นเมธอดอื่น และ `PCMSDetailProcess:209` เป็น `NumberFormatException`)
- ไม่มี handler ที่ใช้งานได้: `MyExceptionController` มี `@ControllerAdvice` ถูกคอมเมนต์ไว้ และจัดการเฉพาะ `NoHandlerFoundException`; ไม่พบ reference ถึงคลาสนี้ที่อื่น
- exception จึงหลุดไปที่ servlet container → `web.xml` `<error-page><location>/handleError</location>` → `ErrorController.customError` (`@RequestMapping` แบบ GET เท่านั้น) ซึ่งแสดงหน้า `error/404` พร้อมข้อความ "Wrong Path." เมื่อ login แล้ว และบันทึก `originalUrl` ลง session
- ฝั่ง JS: ทุก `$.ajax` ในหน้า Main, Detail, DetailV2, CFMReport, Log มี `error:` handler (จำนวนเท่ากับ ajax) แสดง `swal("Fail", "Please contact to IT", "error")` — ผู้ใช้เห็น popup ทั่วไป ไม่เห็นข้อมูลผิด
- ยังไม่ได้ยืนยัน (ต้องทดสอบจริง): (ก) request แบบ POST ที่ error-dispatch ไป `/handleError` ซึ่งรับเฉพาะ GET จะได้ 405 แทนหน้า error หรือไม่ (ข) stack trace/ข้อความ `Invalid date range: <column>` ไปอยู่ใน log ของ container ตามปกติหรือไม่ (ค) daterangepicker ที่ผู้ใช้พิมพ์ค่าเองจะส่งรูปแบบใดมาจริง (ง) `originalUrl` ใน session ถูกเขียนทับด้วย URL ของ AJAX ที่ล้มเหลว อาจกระทบ redirect หลัง login ครั้งถัดไป (`LoginController` ใช้ค่านี้ — ยังไม่ได้ไล่)

## 8. ผลไล่ `CustomerNo` และ `FilterLogin` (อัปเดตรอบที่ 4, อ่านโค้ดเท่านั้น)

### 8.1 `CustomerNo` (second-order)
- อ่านจากตาราง `[PCMS].[dbo].[ConfigCustomerUser]` ผ่าน `ConfigCustomerUserDaoImpl:47-50` (SELECT อย่างเดียว) — `ConfigCustomerUserDao` มีเมธอดเดียวคือ `getConfigCustomerUserDetail(userId)`
- ค้นทั้งโปรเจกต์ไม่พบ INSERT/UPDATE/DELETE/MERGE ที่เขียนตารางนี้ → ผู้เขียนค่านี้อยู่นอกโค้ดนี้ (ไม่ทราบ: ผู้ดูแล DB โดยตรงหรือระบบอื่นที่ใช้ DB เดียวกัน) **ยืนยันไม่ได้ว่าใครเขียน**
- จุดที่ใช้ `CustomerNo` ต่อ SQL ตรง ๆ โดยไม่ escape (แยกด้วย `,`):
  - `FromSapMainSaleDaoImpl.java:154-163` (`getCustomerNameDetail(list)`) และ `:182-190` (`getCustomerShortNameDetail(list)`) — ถูกเรียกจาก `PCMSMainController:107-117` และ controller Detail/DetailV2 ที่ใช้รูปแบบเดียวกัน (ใช้ `user` จาก session เป็น key)
  - `CustomerDetailDaoImpl.java:105-112` และ `:136-143` — `getCustomerNameForOption(poList)` ไม่พบ caller นอกชั้น service (น่าจะไม่ถูกใช้)
- ความเสี่ยง: ผู้ใช้เว็บแก้ค่านี้ไม่ได้ ต้องมีสิทธิ์เขียนตาราง config จึงเสี่ยงต่ำกว่ากลุ่ม A/B แต่ escape ได้ง่าย (4 จุด) — **ยังไม่ได้แก้ รอคำตัดสินจากผู้ใช้**

### 8.2 `FilterLogin`
- `web.xml`: map `/*` (ครอบทุก URL ในแอป รวม `/Report/CFM/getReportDetail`, log API, `/Main/searchByDetail`, `/DetailV2/*`)
- ผ่านโดยไม่ต้อง login เฉพาะ: `/login`, `/login/loginAuth` (เทียบ `getRequestURI()` แบบตรงตัว) และ URI ที่ขึ้นต้นด้วย `/resources/`
- เงื่อนไข login = มี session attribute `user` และ `userObject` เท่านั้น → ตรวจ **authentication** อย่างเดียว ไม่มีการตรวจสิทธิ์รายเส้นทาง (permit) ดังนั้นผู้ใช้ที่ login แล้วทุกคนเรียก endpoint ที่มีช่องโหว่ในหัวข้อ 2 ได้ (ยืนยันว่าความเสี่ยงของกลุ่ม A/B คือ "ผู้ใช้ที่ login แล้ว")
- `startsWith("/resources/")` ใช้กับ URI ดิบ ผมยังไม่ได้ทดสอบว่ารูปแบบ path แปลก ๆ (เช่น `..;`) ทำให้เลี่ยง filter ได้หรือไม่ — **ยืนยันไม่ได้**
- error-page dispatch (`/handleError`) ไม่ผ่าน filter ตามค่าเริ่มต้นของ `<filter-mapping>` ที่ไม่ได้ระบุ `<dispatcher>` (ตามสเปค Servlet; ไม่ได้ทดสอบ)

### 8.3 ข้อค้นพบเพิ่ม: การจำกัดข้อมูลตามลูกค้า (authorization) ฝั่งเซิร์ฟเวอร์ ⚠️
- ผู้ใช้ประเภทลูกค้า (`isCustomer`) ถูกจำกัดแค่ "ตัวเลือกใน dropdown" (`PCMSMainController:107-117` เรียก `getCustomerNameDetail(listConfigCus)`)
- ตอน search: `PCMSMainController.doGetSearchByDetail` (`:170-185`) ส่ง `poList` จาก request ไปที่ `PCMSMainProcess.searchByDetail` (`:33-43`) ซึ่งเมื่อ `isCustomer` เป็นจริง **ทำแค่ล้างฟิลด์ `cfmDetailAll` และ `rollNoRemarkAll`** — ไม่พบการบังคับกรองตาม `CustomerNo` ของผู้ใช้จาก session ในเส้นทางนี้ (ใน `dao` มีการอ้างถึง `ConfigCustomerUser` เพียงที่เดียวคือ `ConfigCustomerUserDaoImpl`)
- ผลที่คาด (จากการอ่านโค้ด ยังไม่ได้ทดสอบ): ผู้ใช้ลูกค้าที่ส่ง request เอง (ไม่ใส่ filter ลูกค้า หรือใส่ชื่อลูกค้าอื่น) อาจเห็นข้อมูลของลูกค้ารายอื่นได้ — ต้องยืนยันด้วยการทดสอบด้วยบัญชีลูกค้าจริง และตรวจ `PCMSDetail*` ว่าเป็นแบบเดียวกันหรือไม่ (ยังไม่ได้ตรวจ)

### 8.4 ผลตรวจต่อเรื่องการจำกัดข้อมูลตามลูกค้า (อ่านโค้ด ยังไม่ได้ทดสอบ)
**แก้คำคาดการณ์ใน 8.3:** ผมเคยคาดว่า "ไม่ใส่ filter ลูกค้า = เห็นทั้งหมด" ไม่ถูก — จากโค้ดจริง ตัวกรองลูกค้าเป็น `INNER JOIN #tempCustomerList` และ `#tempCustomerShortList` (`PCMSSqlService.java:928-931`, `PCMSSqlServiceV2.java:134-137, 1159-1162`) ถ้า request ส่งรายการว่างจะได้ผลว่าง ไม่ใช่ทั้งหมด (และหน้า Main ฝั่ง JS บังคับเลือกลูกค้าก่อนค้นหา `PCMSMain.jsp:~834`)

ที่ยืนยันจากโค้ด:
- รายชื่อลูกค้าที่ใช้กรองมาจาก `bean.getCustomerNameList()` / `getCustomerShortNameList()` ที่มากับ request body (`PCMSMainDaoImpl:619`, `PCMSDetailDaoImpl:1003`, `PCMSDetailV2DaoImpl:169,1109`) ผ่าน `PCMSSearchDaoImpl.handlerTempTableCustomerSearchList` (escape `'` แล้ว)
- ไม่พบการเทียบรายชื่อนั้นกับสิทธิ์ลูกค้าของผู้ใช้ (`ConfigCustomerUser.CustomerNo` ของ session) ฝั่งเซิร์ฟเวอร์ — สิทธิ์ใช้แค่เติมตัวเลือกใน dropdown (`PCMSMainController:107-117` และเทียบเท่าใน Detail/DetailV2)
- `isCustomer` ถูกใช้ใน `PCMSMainController` เท่านั้น (ล้าง `cfmDetailAll`, `rollNoRemarkAll`) ไม่ถูกใช้ใน `PCMSDetailController` / `PCMSDetailV2Controller`
- endpoint ระดับ ProductionOrder เช่น `getPrdDetailByRow` (`PCMSMainDaoImpl:880-884`) กรองด้วย `ProductionOrder` ที่ส่งมาเท่านั้น ไม่มีเงื่อนไขลูกค้า

ผลที่ตามมา (ถ้าผู้ใช้ลูกค้าส่ง request เอง): ใส่ชื่อลูกค้ารายอื่นใน `customerNameList`/`customerShortNameList` หรือระบุ ProductionOrder อื่น อาจได้ข้อมูลของลูกค้ารายอื่น — **ยังไม่ได้ทดสอบ**

ที่ยังไม่รู้ (ต้องถามผู้ใช้/ทดสอบ):
1. ผู้ใช้ประเภทลูกค้า (`Users.isCustomer`) เข้าหน้า Detail/DetailV2 ได้จริงหรือไม่ (ยังไม่ได้ดูเมนู/permit)
2. `CustomerNo` ใน `ConfigCustomerUser` ผูกกับ `CustomerName`/`CustomerShortName` ใน `FromSapMainSale` อย่างไร (ใช้ `[CustomerNo]` ใน `FromSapMainSaleDaoImpl:154-190`) — ต้องรู้ก่อนออกแบบการบังคับกรองฝั่งเซิร์ฟเวอร์
3. ผู้ใช้ภายใน (ไม่ใช่ลูกค้า) ต้องเห็นทุกลูกค้าหรือไม่ — `getConfigCustomerUserDetail` ว่างหมายถึงเห็นทั้งหมด (`PCMSMainController:108-113`)

### อัปเดตรอบที่ 5: escape `CustomerNo`
- แก้แล้ว 4 จุด (escape `'` เป็น `''` ต่อ element หลัง `split(",")`): `FromSapMainSaleDaoImpl` (`getCustomerNameDetail`, `getCustomerShortNameDetail`) และ `CustomerDetailDaoImpl` (2 จุดใน `getCustomerNameForOption`) — แก้ข้อ 8.1 ว่า "ยังไม่ได้แก้"
- ยังไม่ได้คอมไพล์/ทดสอบ

## 9. ข้อค้นพบรอบที่ 6: การสแกนก่อนหน้าพลาดไปหลายไฟล์ ⚠️ (ยังไม่ได้แก้ รอการอนุมัติ)

**แก้ไขข้อสรุปเดิม:** รอบก่อนๆ ผมรายงานว่า "ไล่กลุ่ม A/B ครบ" แต่การตรวจละเอียดเปิดอ่านเฉพาะไฟล์ที่อยู่ในอันดับต้นของการนับ regex (ซึ่งนับรวมข้อความ `'` ใน literal ด้วย) จึงข้าม DAO อีกหลายไฟล์ที่ต่อค่าเข้า SQL จริง รอบนี้สแกนใหม่ด้วยรูปแบบ `'" + ตัวแปร` (รวมกรณีต่อหลายบรรทัด) ทั่ว `dao/service/logic/utilities/controller` และได้รายการด้านล่าง

### 9.1 `getPrdDetailByRow` ยังไม่ปลอดภัยทั้งเมธอด (การแก้รอบแรกไม่ครบ)
- รอบแรกผม escape `prdOrder` เฉพาะ query แรกที่บรรทัด 884 (`PCMSMainDaoImpl`)
- แต่เมธอดเดียวกันนี้ส่ง `productionOrder` / `prdOrder` ดิบต่อไปยัง service/DAO อีกชุด (`PCMSMainDaoImpl` ~บรรทัด 915-975) ซึ่งแต่ละตัวต่อ SQL เอง:
  - `FromSapCFMDaoImpl:51`, `FromSapDyeingDaoImpl:62`, `FromSapPackingDaoImpl:57`, `FromSapPresetDaoImpl:52`, `FromSapReceipeDaoImpl:47`, `FromSapSaleDaoImpl:50`, `FromSapSaleInputDaoImpl:50`, `FromSapWaitTestDaoImpl:44`, `FromSapWorkInLabDaoImpl:44`
  - `InspectSystem/InspectNcDaoImpl:45`, `InspectSystem/InspectOrdersDaoImpl:45`, `LBMS/ImportDetailDaoImpl:44`, `PPMM/RollFromSapDaoImpl:45`, `PPMM/ShopFloorControlDaoImpl:32`
  - ฐานข้อมูลที่เกี่ยวข้องอาจเป็นคนละ DB (LBMS, PPMM, Inspect) — **ยังไม่ได้ตรวจ** ว่าบัญชี DB ที่ใช้มีสิทธิ์อะไรบ้าง
- `SwitchProdOrderDaoImpl:60-63,160` และ `ReplacedProdOrderDaoImpl:~110-120` ต่อ `prodOrder`/`saleOrder`/`saleLine` แบบหลายบรรทัด (regex ตัวแรกจับไม่ได้)

### 9.2 เส้นทางบันทึก/ตรวจวันที่ใน Detail
`PCMSDetailDaoImpl.saveInputDate` เองใช้ `PreparedStatement` (ปลอดภัย) แต่เรียก service ที่ใช้ DAO ต่อไปนี้ซึ่งต่อค่าจาก request ตรง ๆ:
- `PlanCFMDateDaoImpl:58-60, 86, 112-115`
- `PlanCFMLabDateDaoImpl:58-60, 87-90, 113`
- `PlanDeliveryDateDaoImpl:54, 81-84`
- `PlanSendCFMCusDateDaoImpl:56`
ค่าที่ต่อ: `productionOrder`, `saleOrder`, `saleLine` และวันที่ (`cfmPlanDate`, `cfmPlanLabDate`, `deliveryDate`) ใน `CONVERT(DATE,'...',103)`

### 9.3 อื่น ๆ ที่เจอ
- `PermitsDaoImpl:94,137-138`, `UsersDaoImpl:92`, `ConfigCustomerUserDaoImpl:51` — ต่อ `permitId`/`userId` (มาจาก session/DB; เกี่ยวกับข้อ LDAP)
- `FromSapCFMDaoImpl:190,205,218` ต่อ `systemUser` (ค่าภายในระบบ ไม่น่ามาจาก request — ยังไม่ได้ยืนยันที่มา)
- ตรวจเพิ่ม: write endpoint ที่ใช้ `PreparedStatement` ครบ — `ColumnSettingDaoImpl.upsertColumnSettingDetail/upsertColumnVisibleSummary`, `PCMSDetailDaoImpl.saveInputDate` (INSERT) เป็นพารามิเตอร์ ไม่พบปัญหา (หมายเหตุ: `upsertColumnVisibleSummary` เรียก `connection.prepareStatement(sql)` ซ้ำโดยไม่ใช้ผล เป็นโค้ดซ้ำที่ไม่เกี่ยวกับความปลอดภัย)
- **`/Main/getEncrypted/{userId}` (`PCMSMainController:239-270`):** ผู้ใช้ที่ login แล้วเรียกเพื่อขอ "userId ที่เข้ารหัส" ได้กับ userId ใดก็ได้ (ไม่ใช้ session) โดย key, salt ฝังในโค้ด (`secretPass`, `secretSalt`) และใช้ IV เป็นศูนย์ทั้งหมด (AES/CBC เหมือนกันทุกครั้ง); JS ใช้เรียกก่อนสร้างลิงก์ไประบบ LBMS / INSPECT / SFC / QCMS (`web-app.js:42-175`) — ถ้าระบบปลายทางยอมรับค่านี้เป็นตัวตนผู้ใช้ ผู้ใช้ที่ login แล้วอาจสร้างลิงก์ในนามผู้ใช้อื่นได้ **ยืนยันไม่ได้** เพราะไม่เห็นฝั่งปลายทาง ต้องถามผู้ใช้
