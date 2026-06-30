# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

# CLAUDE.md — PCMS2

Production/sales order monitoring system with SAP integration.

## Role

Act as a senior software engineer and technical reviewer.
Prioritize: **Correctness → Simplicity → Maintainability → Performance → Security**

---

## Document Index

### Layer Rules (auto-load → workspace `.claude/rules/`)
| ไฟล์ | เนื้อหา |
|---|---|
| `.claude/rules/core-behavior.md` | ห้าม JPA/Gson rules, JAVA_HOME, user session |
| `.claude/rules/backend-patterns.md` | DAO, SP, temp table snippets |
| `.claude/rules/frontend-patterns.md` | JSP/jQuery/SweetAlert2 snippets |
| `.claude/rules/security.md` | FilterLogin, auth guards |
| `.claude/rules/api-contract.md` | Gson response pattern, URL convention |
| `.claude/rules/git-workflow.md` | commit convention, pre-commit checklist |

### Prompts Library (`.claude/prompts/`)
| ไฟล์ | ใช้เมื่อ |
|---|---|
| `.claude/prompts/review.md` | review code ก่อน commit |
| `.claude/prompts/fix.md` | diagnose + fix bug |
| `.claude/prompts/refactor.md` | ปรับโครงสร้างโดยไม่เปลี่ยน behavior |
| `.claude/prompts/implement-feature.md` | เพิ่ม feature ใหม่ |

### Business (อ่านก่อนแตะ logic)
| ไฟล์ | เนื้อหา |
|---|---|
| `docs/business/roles.md` | role registry, visibility, permission matrix |
| `docs/business/workflow.md` | status machine, transitions, process codes |
| `docs/business/rules.md` | business rules, constraints, edge cases |

### Technical Reference
| ไฟล์ | เนื้อหา |
|---|---|
| `docs/architecture.md` | tech stack, layer design, package structure |
| `docs/database.md` | qualifier, tables, SQL patch rules, temp table |
| `docs/api.md` | Gson response, endpoint patterns |
| `docs/deployment.md` | build, deploy, troubleshoot |

### Design Decisions
| ไฟล์ | สรุป |
|---|---|
| `docs/decision-records/ADR-000-template.md` | ADR template สำหรับบันทึกการตัดสินใจ |

### Shared Reference (workspace `docs/` และ `*/CLAUDE.md`)
| ไฟล์ | เนื้อหา |
|---|---|
| `docs/pcms2.md` | SAP integration quirks, PCMSSearch lifecycle, new feature checklist |
| `docs/coding-standards.md` | naming convention, review criteria, comment policy |
| `backend/CLAUDE.md` | qualifier rules, DAO pattern, temp table rules, SP pattern |
| `frontend/CLAUDE.md` | Bootstrap 4 rules, selectpicker pitfalls, div-edit-mode pattern |
| `database/CLAUDE.md` | schema convention, SQL patch guard pattern, SQL patch rules |

### Session Management
| ไฟล์ | เนื้อหา |
|---|---|
| `NEXT_SESSION.md` | งานค้าง + SQL รอรัน — **อ่านตอนเริ่ม session** |
| `SKILL.md` | recipes, JS patterns, field checklists |

---

## Quick Facts

| เรื่อง | สรุป |
|---|---|
| DB qualifier | `pcmsDatabase` (@Primary), `ppmmDatabase`, `sorDatabase`, `erpDatabase` — ตรวจทุก DAO |
| JSON | Gson — `new Gson().toJson(list)` / `new Gson().fromJson(json, Type)` เสมอ |
| Auth | FilterLogin + AD — ไม่มี Spring Security ใน project นี้ |
| URL ใหม่ | ไม่ต้องเพิ่ม intercept-url — FilterLogin จัดการทั้งหมด |
| DB access | HikariCP 4.0.3 + `JdbcTemplate` + mssql-jdbc 9.4.1 (migrated 2026-06-19) — ไม่ใช่ `Database` core lib |
| User session | `(User) session.getAttribute("user")` — ไม่ใช่ SecurityContext |
| Context path | `/PCMS2` (server: `10.11.44.100:8080`) |

---

## Build

```powershell
# Machine default อาจเป็น JDK สูงกว่า — set ก่อนเสมอ
$env:JAVA_HOME = "C:\Program Files\Java\jdk-1.8"

mvn clean package -DskipTests   # → target/PCMS2.war
mvn clean compile                # ตรวจ compile error เท่านั้น
mvn test                         # JUnit 4
```

Deploy: Eclipse WTP → Run on Server (Tomcat) | context path: `/PCMS2`

---

## Architecture

```
controller/ → service/ → dao/implement/ → JdbcTemplate / HikariCP → SQL Server
```

**Package root:** `src/main/java/th/co/wacoal/atech/pcms2/`

| Package | Role |
|---|---|
| `config/` | `DatabaseConfig` (4 DB qualifiers), `AppConfig`, `SchedulerConfig` |
| `controller/` | Spring MVC `@Controller` — one per feature area |
| `service/` | Business logic layer |
| `dao/` + `dao/implement/` | Data access interfaces + implementations |
| `entities/` | POJOs used as DTOs |
| `logic/` | Complex business process logic separated from controllers |
| `filter/` | `FilterLogin` authentication enforcement |
| `info/` | DB connection config holder classes |
| `utilities/` | Shared helpers (`SqlStatementHandler`, `PCMSSqlService`) |

- JSON: Gson (`@ResponseBody String`) — ดู `backend/CLAUDE.md`
- Auth: FilterLogin — ดู `.claude/rules/security.md`
- JSPs: `src/main/webapp/WEB-INF/pages/`
- Static: `src/main/webapp/resources/`
- Spring wiring: `web.xml` → `pcms2-servlet.xml` → `applicationContext.xml`

### Databases (4)

| Qualifier | DB | ใช้ใน |
|---|---|---|
| `pcmsDatabase` (@Primary) | PCMS | main production/sales data |
| `ppmmDatabase` | PPMM | planning data |
| `sorDatabase` | SOR | sale order data |
| `erpDatabase` | ERP Atech | SAP integration |

### Key Controllers

- `LoginController` — LDAP/AD authentication, session setup
- `PCMSMainController` — main dashboard, AES encryption/decryption of sensitive data
- `PCMSDetailController` / `PCMSDetailV2Controller` — detailed production order views
- `ReportController` — Excel via Apache POI 5.2.5 + JXLS 2.13.0 (templates from classpath)
- `SapToWebController` — SAP→Web sync trigger
- `ProductionOrderLogController` / `SaleOrderLogController` — audit logging
- `BackGroundJobDaoImpl` — scheduled SP execution (no #temp tables; SP-internal only)

---

## SAP Integration

- SAP data ดึงมาผ่าน stored procedures / linked server — ไม่ใช่ REST API โดยตรง
- SAP-side tables อยู่ใน DB คนละตัวกับ PCMS DB (`erpDatabase`) — อย่าเขียน cross-DB query โดยไม่ตรวจ qualifier

---

## Adding a New Feature Page

1. Entity → DAO interface + Impl (`@Qualifier` ให้ถูก DB) → Service → Controller → JSP + JS → Sidebar
2. ดู shared pattern ใน `SKILL.md`

---

## Temp Table Rules (✅ updated 2026-06-20)

PCMS2 ใช้ HikariCP + JdbcTemplate (migrated 2026-06-19) — connection reuse จาก pool
เมื่อ `#temp` จาก request ก่อนค้างบน connection → SQL Server compile batch ถัดไปด้วย schema เก่า → runtime error

### Pattern A — Read queries (`PCMSMain`, `PCMSDetail`, `PCMSDetailV2`)

```java
// ✅ ถูกต้อง — drop-before + drop-after; รองรับ batch ที่มี CREATE INDEX
List<Map<String, Object>> datas =
    SqlStatementHandler.queryList(this.jdbc, PCMSSqlService.dropAllTemp, sql);

// ❌ ห้ามใช้ตรง ๆ เมื่อ SQL มี SELECT INTO #temp หรือ CREATE INDEX
jdbc.queryForList(multiStatementSql);
```

`PCMSSqlService.dropAllTemp` — ครอบคลุม 39+ temp tables จาก read queries

`SqlStatementHandler.queryList()` ใช้ `stmt.execute()` + iterate results — รองรับ DDL (CREATE INDEX) ซึ่ง `queryForList()` (ใช้ `executeQuery()`) ทำไม่ได้

**queryList() guarantees (2026-06-20):** DROP-before + main SQL + DROP-after วิ่งบน connection เดียวกัน · `finally` รัน `SET NOCOUNT OFF` + DROP-after เสมอแม้ SQL throw · `guard=50` ป้องกัน infinite loop

**⚠️ ห้ามเพิ่ม PCMSSearch tables ใน `dropAllTemp`:**
`#tempLotNoList`, `#tempUserStatusList`, `#tempCustomerList`, `#tempCustomerShortList`
— PCMSSearchDaoImpl สร้าง 4 tables นี้บน connection เดียวกัน **ก่อน** main query วิ่ง; drop-before จะลบก่อน query ใช้

**Checklist เมื่อเพิ่ม `#temp` ใหม่ใน read query:**
1. เพิ่ม entry ใน `PCMSSqlService.dropAllTemp`
2. ใช้ `SqlStatementHandler.queryList(this.jdbc, PCMSSqlService.dropAllTemp, sql)`

### Pattern B — Upsert/write operations (FromSap* DAOs)

```java
Connection conn = DataSourceUtils.getConnection(dataSource);
try {
    conn.setAutoCommit(false);
    // ... PreparedStatement INSERT/UPDATE ...
    conn.commit();
} catch (Exception e) {
    conn.rollback(); throw e;
} finally {
    try (java.sql.Statement cleanup = conn.createStatement()) {
        cleanup.execute("IF OBJECT_ID('tempdb..#TempXxx') IS NOT NULL DROP TABLE #TempXxx");
    } catch (Exception ignored) {}
    try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
    DataSourceUtils.releaseConnection(conn, dataSource);
}
```

**Files ที่แก้แล้ว (2026-06-04):** `FromSapCFMDaoImpl`, `FromSapGoodReceiveDaoImpl`, `FromSapMainBillBatchDaoImpl`, `FromSapMainProdDaoImpl`, `FromSapMainProdSaleDaoImpl`, `FromSapMainSaleDaoImpl`, `FromSapPackingDaoImpl`, `FromSapReceipeDaoImpl`, `FromSapSaleDaoImpl`, `FromSapSaleInputDaoImpl`, `FromSapSubmitDateDaoImpl`, `FromSORCFMDaoImpl`, `Z_ATT_CustomerConfirm2DaoImpl`

---

## UI Color Palette

สีมาตรฐานที่ใช้ร่วมกันทุกระบบ (PPMM2 / SFC / PCMS2 / LBMS)

### Dark Card / Page Header
| ชื่อ | Hex | ใช้ที่ |
|---|---|---|
| Dark navy (base) | `#2d3748` | `.setting-page-header` bg, section badge, DataTable header เฉพาะหน้า |
| Dark navy (deep) | `#1a202c` | fixed column header |
| Steel blue (hover) | `#3a506b` | hover บน dark header |
| Border accent blue | `#4a90e2` | border-left User section badge |
| Border accent green | `#48bb78` | border-left Lab section badge |

### Global DataTable Header (`datatable.overide.css`)
| ชื่อ | Hex | ใช้ที่ |
|---|---|---|
| Steel blue (header) | `#4a6fa5` | `table.dataTable thead` — global default |
| Steel blue (hover) | `#3d5d8a` | header hover + FixedColumns |
| Header text | `#f0f4f8` | header text color |

### Body / Content
| ชื่อ | Hex | ใช้ที่ |
|---|---|---|
| Row even | `#ffffff` | dataTable row even |
| Row odd | `#f7fafc` | dataTable row odd |
| Row hover | `#edf2f7` | dataTable row hover |
| Card bg | `#ffffff` | card background |
| Card border | `#dee2e6` | card + tab border |
| Card shadow | `rgba(0,0,0,0.06)` | card box-shadow |

### Icon / Badge Colors
| ชื่อ | Hex | ใช้ที่ |
|---|---|---|
| Info icon (blue) | `#90cdf4` | `fa-info-circle` tooltip icon |
| User badge text | `#90cdf4` | section badge User |
| Lab badge text | `#9ae6b4` | section badge Lab |
| Pair-to-lab icon | `#68d391` | `fa-flask` pair indicator |
| Pair-to-user icon | `#76b9e8` | `fa-user` pair indicator |
