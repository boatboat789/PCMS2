# Architecture — PCMS2

---

## System Overview

**Project:** PCMS2 (Production Control Management System 2)
**Purpose:** ติดตามและบริหาร production/sales order ตั้งแต่รับออเดอร์จาก SAP จนถึงส่งสินค้า
**Users:** BKK Center (Dyeing), Factory, Management, Sale team

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 1.8 |
| Framework | Spring MVC 5.2 |
| Security | FilterLogin + AD (ไม่ใช่ Spring Security) |
| Build | Maven (WAR) |
| Server | Apache Tomcat 9 · context path `/PCMS2` · `10.11.44.100:8080` |
| Database | SQL Server — 4 DB (PCMS, PPMM, SOR, ERP) |
| ORM | JdbcTemplate + raw SQL (no JPA/Hibernate) |
| JSON | Gson + `@ResponseBody String` |
| Connection pool | HikariCP 4.0.3 + mssql-jdbc 6.1.0.jre8 (migrated 2026-06-19) |
| Frontend | JSP + JSTL + Bootstrap 4 + jQuery + DataTables + Bootstrap Select |
| Reports | Apache POI 5.2.5 + JXLS 2.13.0 (Excel templates from classpath) |

---

## Layer Structure

```
Request
  │
  ▼
[FilterLogin]                     ← session check, AD auth
  │
  ▼
[@Controller / @RestController]   ← input validation, view/JSON (Gson)
  │
  ▼
[@Service]                        ← business logic
  │
  ▼
[@Repository DaoImpl]             ← raw SQL, JdbcTemplate
  │
  ▼
[SQL Server — HikariCP]
```

---

## Package Structure

```
src/main/java/th/co/wacoal/atech/pcms2/
├── config/          DatabaseConfig (4 HikariDataSource + 4 JdbcTemplate), AppConfig, SchedulerConfig
├── filter/          FilterLogin (session + AD auth)
├── controller/      PCMSMainController, PCMSDetailController, PCMSDetailV2Controller,
│                    ReportController, SapToWebController, LoginController, ...
├── service/         business logic layer
├── dao/             interfaces
├── dao/implement/   DaoImpl classes
├── entities/        POJOs / DTOs
├── logic/           PCMSMainProcess (complex multi-step logic)
├── utilities/       SqlStatementHandler, PCMSSqlService
├── info/            Sql*Info connection config holders
└── listener/        AppShutdownListener (HikariCP + JDBC deregister on undeploy)

src/main/webapp/
├── WEB-INF/
│   ├── pages/[feature]/   JSP files
│   ├── web.xml
│   ├── pcms2-servlet.xml
│   └── applicationContext.xml
└── resources/
    ├── css/
    ├── js/[feature]/
    └── plugins/    DataTables, FlatPickr, SweetAlert2, Bootstrap Select
```

---

## Database Layout

| Qualifier | Database | Purpose |
|---|---|---|
| `pcmsDatabase` (@Primary) | `PCMS` @ `10.11.44.101` | main production/sales data |
| `ppmmDatabase` | `PPMM` @ `10.11.44.101` | planning data (อ่านอย่างเดียว) |
| `sorDatabase` | `SOR` @ `10.11.44.101` | sale order data |
| `erpDatabase` | ERP Atech (SAP-side) | SAP integration — read only |

---

## Key Design Decisions

| Decision | Reason |
|---|---|
| JdbcTemplate แทน JPA | control raw SQL, หลีกเลี่ยง lazy-load issues, ตรงกับทีม |
| Gson แทน Jackson | legacy choice — ไม่ mix กับ Jackson ใน project นี้ |
| FilterLogin แทน Spring Security | ระบบเก่า + ต้องการ custom AD flow โดยตรง |
| HikariCP | migrate จาก core/Database เพื่อ connection management ที่ดีกว่า (2026-06-19) |
| SqlStatementHandler.queryList() | รองรับ multi-statement batch (DDL + SELECT) ที่ queryForList() ทำไม่ได้ |
| PCMSSearch temp lifecycle | 4 PCMSSearch temps สร้างก่อน main query บน connection เดียวกัน → ห้าม drop ใน dropAllTemp |

---

## External Integrations

| System | Purpose | Method |
|---|---|---|
| Active Directory | Authentication | LDAP ผ่าน core library |
| SAP / ERP | Sync production/sales data | Stored Procedures + linked server (erpDatabase) |
| BGJob | Background job coordination | HTTP call ไปยัง BGJobApi |

---

## Known Constraints

- Cannot run offline — requires live SQL Server + AD
- JDK must be 1.8 (`$env:JAVA_HOME = "C:\Program Files\Java\jdk-1.8"`)
- Temp tables `#xxx` ต้อง DROP ก่อนคืน connection กลับ pool (Pattern A/B)
- PCMSSearch temp tables (`#tempLotNoList` ฯลฯ) ห้ามรวมใน dropAllTemp
- `PCMSDetailV2DaoImpl` ยังไม่ migrate ไป JdbcTemplate (ข้ามไว้ตอน migrate)
