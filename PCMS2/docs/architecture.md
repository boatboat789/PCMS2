# Architecture — [PROJECT_NAME]

---

## System Overview

**Project:** [PROJECT_NAME]
**Purpose:** [จุดประสงค์หลัก เช่น บริหารกระบวนการ X ตั้งแต่ Y ถึง Z]
**Users:** [กลุ่มผู้ใช้ เช่น BKK Center, Factory, Management]

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 1.8 |
| Framework | Spring MVC 5.2 |
| Security | [Spring Security 5.2 / FilterLogin + AD] |
| Build | Maven (WAR) |
| Server | Apache Tomcat 8.5/9 |
| Database | SQL Server — `[DB_NAME]` |
| ORM | JdbcTemplate + NamedParameterJdbcTemplate (no JPA/Hibernate) |
| JSON | [Jackson + ApiResponse<T> / Gson + @ResponseBody String] |
| Connection pool | HikariCP |
| Cache | Caffeine (TTL=60min, max=500 per cache) |
| Frontend | JSP + JSTL + Bootstrap 4 + jQuery + DataTables + Bootstrap Select |
| Reports | [JasperReports 6.21 / N/A] |

---

## Layer Structure

```
Request
  │
  ▼
[Spring Security / FilterLogin]   ← auth check
  │
  ▼
[@Controller / @RestController]   ← input validation, view/JSON
  │
  ▼
[@Service]                        ← business logic, @Transactional
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
src/main/java/th/co/wacoal/[app]/
├── config/          DataSource, Cache, Jackson, GlobalExceptionHandler
├── security/        CustomAuthenticationProvider, CustomUserDetails  [Spring Security]
├── filter/          FilterLogin  [FilterLogin auth]
├── controller/
│   ├── api/         @RestController (JSON endpoints)
│   └── [feature]/   @Controller (MVC pages)
├── service/
│   └── [feature]/
├── dao/
│   ├── [feature]/   interfaces
│   └── implement/[feature]/
├── entities/        POJOs, DTOs, ApiResponse<T>, WorkflowResult
└── utilities/       SecurityUtils, FormatUtils, PoStatusCodeUtils

src/main/webapp/
├── WEB-INF/
│   ├── pages/[feature]/   JSP files
│   ├── web.xml
│   ├── [APP]-servlet.xml  (spring-mvc.xml)
│   ├── applicationContext.xml
│   └── spring-security.xml  [Spring Security only]
└── resources/
    ├── css/
    ├── js/[feature]/
    └── plugins/    DataTables, FlatPickr, SweetAlert2, Bootstrap Select
```

---

## Database Layout

| Qualifier | Database | Purpose |
|---|---|---|
| `[prefix]JdbcTemplate` | `[DB_NAME]` | [primary DB] |
| `[prefix2]JdbcTemplate` | `[DB2_NAME]` | [secondary DB — ถ้ามี] |

---

## Key Design Decisions

| Decision | Reason |
|---|---|
| JdbcTemplate แทน JPA | control raw SQL, avoid lazy-load pitfalls, ตรงกับทีม |
| [Jackson/Gson] | [เหตุผล] |
| [Spring Security/FilterLogin] | [เหตุผล] |
| Caffeine cache | ลด DB round-trips สำหรับ master data |
| In-memory status provider | status lookup ไม่ hit DB ต่อ request |

---

## External Integrations

| System | Purpose | Method |
|---|---|---|
| Active Directory | Authentication | [LDAP / AD API] |
| [SAP / ERP] | [purpose] | [REST API / direct DB] |

---

## Known Constraints

- Cannot run offline — requires live SQL Server [+ AD]
- JDK must be 1.8 (machine default may be newer — set JAVA_HOME)
- Temp tables `#xxx` must be dropped within same connection
- [ข้อจำกัดเฉพาะ project]
