# Database — SQL Server (PCMS2)

---

## Connection Config

PCMS2 ใช้ `Database` bean (core library) ไม่ใช่ JdbcTemplate:

| Qualifier | DB | Server |
|---|---|---|
| `pcmsDatabase` (@Primary) | `PCMS` | `10.11.44.101` |
| `ppmmDatabase` | PPMM | `10.11.44.101` |
| `sorDatabase` | SOR | `10.11.44.101` |
| `erpDatabase` | ERP Atech | (SAP-side server) |

Connection config: `src/main/java/th/co/wacoal/atech/pcms2/info/Sql*Info.java`

---

## Naming Conventions

| Concept | Convention |
|---|---|
| Table / Column | `snake_case` |
| Primary Key | `[table]_id INT IDENTITY(1,1)` |
| Soft delete | `active_flag BIT NOT NULL DEFAULT 1` |
| Timestamps | `created_on DATETIME`, `updated_on DATETIME` |
| Status code | `UPPER_SNAKE_CASE` (string) |
| Index | `IX_TableName_ColumnName` |

---

## Schema Change Pattern

ทุก SQL patch ต้องมี guard — อย่า run script ซ้ำแล้ว error:

```sql
-- เพิ่ม column
IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.my_table') AND name = 'new_col')
    ALTER TABLE dbo.my_table ADD new_col NVARCHAR(200) NULL;

-- เพิ่ม table
IF NOT EXISTS (SELECT 1 FROM sys.objects WHERE object_id = OBJECT_ID('dbo.new_table'))
BEGIN
    CREATE TABLE dbo.new_table (
        new_table_id INT IDENTITY(1,1) PRIMARY KEY,
        name         NVARCHAR(200) NOT NULL,
        active_flag  BIT NOT NULL DEFAULT 1,
        created_on   DATETIME NOT NULL DEFAULT GETDATE(),
        created_by   NVARCHAR(50) NOT NULL,
        updated_on   DATETIME NOT NULL DEFAULT GETDATE(),
        updated_by   NVARCHAR(50) NOT NULL
    );
END

-- เพิ่ม index
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.new_table') AND name = 'IX_new_table_name')
    CREATE INDEX IX_new_table_name ON dbo.new_table (name);

-- INSERT ข้อมูล
IF NOT EXISTS (SELECT 1 FROM dbo.new_table WHERE name = 'value')
    INSERT INTO dbo.new_table (name, active_flag, created_on, created_by, updated_on, updated_by)
    VALUES ('value', 1, GETDATE(), 'SYSTEM', GETDATE(), 'SYSTEM');
```

---

## Temp Table Rules

PCMS2 ใช้ `Database` core lib (ไม่ใช่ JdbcTemplate) — connection จาก pool reused:

**ห้ามทิ้ง #temp table ข้ามการเชื่อมต่อ — ต้อง DROP ในทุก path:**

```java
// Pattern A — Read queries (ใช้ SqlStatementHandler)
List<Map<String, Object>> datas =
    SqlStatementHandler.queryList(this.database, PCMSSqlService.dropAllTemp, sql);

// Pattern B — Upsert (try/finally บน explicit connection)
Connection conn = this.database.getConnection();
try {
    // ... INSERT/UPDATE ...
} finally {
    try (java.sql.Statement cleanup = conn.createStatement()) {
        cleanup.execute("IF OBJECT_ID('tempdb..#TempXxx') IS NOT NULL DROP TABLE #TempXxx");
    } catch (Exception ignored) {}
    try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
}
```

⚠️ ห้ามเพิ่มใน `PCMSSqlService.dropAllTemp`: `#tempLotNoList`, `#tempUserStatusList`, `#tempCustomerList`, `#tempCustomerShortList` (PCMSSearch lifecycle ต่างกัน)

---

## SQL Patch Rules

- เก็บใน `sql/` directory
- ชื่อไฟล์: `YYYY-MM-DD_short_description.sql`
- รันด้วยมือใน SSMS เสมอ — ไม่มี migration framework
- ตรวจ `NEXT_SESSION.md` ก่อน run ทุกครั้ง
- ใช้ IF NOT EXISTS guard ทุก statement

---

## Background Job Stored Procedures

SPs เหล่านี้ถูก exec ผ่าน `BackGroundJobDaoImpl` (scheduled):

| SP | ทำอะไร |
|---|---|
| `spd_UpsertToMainProd` | sync production orders จาก SAP staging |
| `spd_UpsertToCFM` | sync CFM data |
| `spd_UpsertToMainProdSale` | sync production sales |
| `spd_UpsertToPacking` | sync packing data |
| `spd_UpsertToSubmitDate` | sync submit dates |
| `spd_UpsertToGoodReceive` | sync good receive |
| `spd_UpsertToMainBillBatch` | sync billing batch |
| `spd_UpsertToMainSale` | sync sale orders |
| `spd_UpsertToSale` | sync sale data |
| `spd_UpsertToZ_ATT_CustomerConfirm2` | sync customer confirmations |
| `spd_UpsertToTEMP_ProdWorkDate` | refresh temp production work dates |
| `spd_UpsertToTEMP_UserStatusOnWeb` | refresh temp user status |
| `spd_SumBillAndGoodReceive` | summarize billing + goods receipt |

SPs เหล่านี้ไม่ return output parameter — execute ผ่าน `PreparedStatement.execute()` ตรง ๆ
