# Next Session — PCMS2

> อัปเดตไฟล์นี้ทุกครั้งที่จบ session

---

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
