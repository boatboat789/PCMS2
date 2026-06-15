# PCMS2 — Project Guide

Production/sales order monitoring with SAP integration.

**Package root:** `src/main/java/th/co/wacoal/atech/pcms2/`

## Package Structure

| Package | Role |
|---|---|
| `config/` | `DatabaseConfig` (4 DB qualifiers), `AppConfig`, `SchedulerConfig` |
| `controller/` | Spring MVC `@Controller` classes — one per feature area |
| `service/` | Business logic layer |
| `dao/` + `dao/Impl/` | Data access interfaces + implementations |
| `entities/` | POJOs used as DTOs |
| `logic/` | Complex business process logic separated from controllers |
| `filter/` | `FilterLogin` authentication enforcement |
| `info/` | DB connection config holder classes |
| `utilities/` | Shared helpers |

## Databases (4)

PPMM, PCMS, SOR, ERP Atech — qualifier names defined in `DatabaseConfig`.

## Key Controllers

- `LoginController` — LDAP/AD authentication, session setup
- `PCMSMainController` — main dashboard, AES encryption/decryption of sensitive data
- `PCMSDetailController` / `PCMSDetailV2Controller` — detailed production order views
- `ReportController` — report generation via Apache POI 5.2.5 + JXLS 2.13.0 (templates loaded from classpath)
- `SapToWebController` — SAP integration
- `ProductionOrderLogController` / `SaleOrderLogController` — audit logging

## Libraries

- Reports: Apache POI 5.2.5 + JXLS 2.13.0 (Excel templates from classpath)
- AES encryption/decryption in `PCMSMainController`
- Context path: `/PCMS2` (server: `10.11.44.100:8080`)

---

## Temp Table Rules (✅ 2026-06-04)

PCMS2 มี 2 ประเภท DAO ที่ใช้ temp table — แต่ละประเภทมี pattern ต่างกัน

### Pattern A — Read queries (`PCMSMain`, `PCMSDetail`, `PCMSDetailV2`)

DAO เหล่านี้ใช้ `this.database.queryList(sql)` (core library `Database`) ซึ่งอาจ reuse connection จาก pool — temp table จาก request ก่อนค้างได้

**วิธีที่ถูกต้อง** (drop-before + drop-after):
```java
List<Map<String, Object>> datas =
    SqlStatementHandler.queryList(this.database, PCMSSqlService.dropAllTemp, sql);
```

**ไม่ต้องใช้** `this.database.queryList(sql)` ตรง ๆ ในทุก method ที่ SQL มี `SELECT INTO #temp`

**`PCMSSqlService.dropAllTemp`** — ครอบคลุม 39 temp tables ที่ใช้ใน read queries

**⚠️ ห้ามเพิ่ม PCMSSearch temp tables ใน `dropAllTemp`:**
- `#tempLotNoList`, `#tempUserStatusList`, `#tempCustomerList`, `#tempCustomerShortList`
- PCMSSearchDaoImpl สร้าง 4 tables นี้บน connection เดียวกัน **ก่อน** main query — drop-before จะลบก่อน query ใช้

**Checklist เมื่อเพิ่ม `#temp` ใหม่ใน read query:**
1. เพิ่ม entry ใน `PCMSSqlService.dropAllTemp`
2. ใช้ `SqlStatementHandler.queryList(this.database, PCMSSqlService.dropAllTemp, sql)` ไม่ใช่ `database.queryList(sql)`

### Pattern B — Upsert/write operations (FromSap* DAOs)

DAO เหล่านี้ใช้ `conn = this.database.getConnection()`, `setAutoCommit(false)`, explicit transaction

**วิธีที่ถูกต้อง** — DROP ใน `finally` block ก่อน `setAutoCommit(true)`:
```java
} finally {
    try (java.sql.Statement cleanup = conn.createStatement()) {
        cleanup.execute("IF OBJECT_ID('tempdb..#TempXxx') IS NOT NULL DROP TABLE #TempXxx");
    } catch (Exception ignored) {}
    try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
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
