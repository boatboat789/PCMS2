# Database — [PROJECT_NAME]

---

## Connection Config

| Qualifier | Database | Purpose |
|---|---|---|
| `[prefix]JdbcTemplate` | `[DB_NAME]` | ทุก DAO ทั่วไป |
| `[prefix]NamedParameterJdbcTemplate` | `[DB_NAME]` | query มี `IN (...)` clause |
| `[prefix]DataSource` | `[DB_NAME]` | SP ที่ต้องการ Connection โดยตรง |

Server: `[SERVER_NAME]` · Port: `[PORT]` (default 1433)

---

## Core Tables

| Table | คำอธิบาย |
|---|---|
| `[main_table]` | [description] |
| `master_po_status` | สถานะทั้งหมด (status_code, order_no, color_class) |
| `config_po_workflow` | transition rules (from → to) |
| `po_process_tracking` | บันทึกประวัติการเคลื่อนไหว |
| `master_role` | role definitions |
| `config_role_permission_mapping` | role → permission codes |
| `config_role_status_mapping` | role → status visibility |
| `config_role_workflow_permission` | role → step |
| `config_role_page_permission` | role → page access |

---

## Naming Conventions

| Concept | Convention |
|---|---|
| Table / Column | `snake_case` |
| PK | `[table]_id INT IDENTITY(1,1)` |
| Soft delete | `active_flag BIT NOT NULL DEFAULT 1` |
| Timestamps | `created_on DATETIME`, `updated_on DATETIME` |
| Created/Updated by | `created_by NVARCHAR(50)`, `updated_by NVARCHAR(50)` |
| Status code | `UPPER_SNAKE_CASE` |
| Process code | `MS_` (milestone) / `PC_` (process) prefix |
| Index | `IX_TableName_ColumnName` |

---

## Temp Table Rules

HikariCP reuses connections — #temp table จาก request ก่อนอาจยังอยู่

**Pattern A** (helper DROP):
```java
String dropSql = "IF OBJECT_ID('tempdb..#MyTemp') IS NOT NULL DROP TABLE #MyTemp";
return queryList(jdbc, dropSql, sql);
```

**Pattern B** (try/finally):
```java
try {
    return jdbc.query(sql, new BeanPropertyRowMapper<>(Foo.class));
} finally {
    jdbc.execute("IF OBJECT_ID('tempdb..#MyTemp') IS NOT NULL DROP TABLE #MyTemp");
}
```

---

## SQL Patch Rules

- ไฟล์อยู่ใน `sql/` — ชื่อ `YYYY-MM-DD_short_description.sql`
- รันด้วยมือใน SSMS เสมอ — ไม่มี migration framework
- ทุก statement ต้องมี IF NOT EXISTS guard
- บันทึกใน `NEXT_SESSION.md` ว่ารันหรือยัง

---

## Status Lookup

ห้าม query `master_po_status` ต่อ request:

```java
// ✅ CORRECT — in-memory
Integer statusId = statusProvider.getIdByCode("CANCELLED");

// ❌ WRONG — DB hit ทุก request
Integer statusId = dao.findStatusIdByCode("CANCELLED");
```

`PoStatusProvider` โหลด `master_po_status` เข้า ConcurrentHashMap ตอน startup

---

## Adding New Status / Workflow Step

```sql
-- 1. สถานะใหม่
INSERT INTO dbo.master_po_status (type, focus_by, order_no, status_code, name, color_class, selectable_flag, active_flag, created_on, created_by, updated_on, updated_by)
VALUES ('MAIN', 'ALL', [ORDER_NO], '[STATUS_CODE]', '[NAME]', '[COLOR_CLASS]', 1, 1, GETDATE(), 'SYSTEM', GETDATE(), 'SYSTEM');

-- 2. Transition
INSERT INTO dbo.config_po_workflow (from_po_status_id, to_po_status_id, responsible_step_id, action_name, transition_type, active_flag, ...)
VALUES ([FROM_ID], [TO_ID], [STEP_ID], '[action]', 'NORMAL', 1, ...);

-- 3. Role permission
INSERT INTO dbo.config_role_workflow_permission (role_id, responsible_step_id, active_flag)
VALUES ([ROLE_ID], [STEP_ID], 1);
```

---

## Verify Workflow Config (diagnostic query)

```sql
SELECT cpw.*, mps_from.status_code AS from_code, mps_to.status_code AS to_code
FROM config_po_workflow cpw
JOIN master_po_status mps_from ON cpw.from_po_status_id = mps_from.po_status_id
JOIN master_po_status mps_to   ON cpw.to_po_status_id   = mps_to.po_status_id
WHERE mps_to.status_code = '[TARGET_STATUS]';
```
