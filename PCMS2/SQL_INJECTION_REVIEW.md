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
