# Backend — Java / Spring (PCMS2)

> Code snippets อยู่ใน `.claude/rules/backend-patterns.md` (auto-load)
> ไฟล์นี้คือ context + กฎที่ต้องรู้ก่อนแตะ backend

---

## Stack

| รายการ | ค่า |
|---|---|
| Java | 1.8 |
| Spring MVC | 5.2.x |
| Auth | FilterLogin + AD |
| JSON | Gson + `@ResponseBody String` |
| DB access | `th.in.totemplate.core.sql.Database` (core library) — ไม่ใช่ JdbcTemplate |
| Reports | Apache POI 5.2.5 + JXLS 2.13.0 (Excel templates from classpath) |

---

## Qualifiers

PCMS2 ใช้ `Database` bean (core library) ไม่ใช่ JdbcTemplate:

| Qualifier | DB | ใช้ใน |
|---|---|---|
| `@Qualifier("pcmsDatabase")` (@Primary) | PCMS | main production/sales data |
| `@Qualifier("ppmmDatabase")` | PPMM | planning data |
| `@Qualifier("sorDatabase")` | SOR | sale order data |
| `@Qualifier("erpDatabase")` | ERP Atech | SAP integration |

> ตรวจ qualifier ก่อนแตะ datasource config เสมอ — ผิด qualifier = ผิด DB

---

## Rules

- ห้าม JPA/Hibernate — `Database.queryList()` + raw SQL เท่านั้น
- ห้าม DriverManager.getConnection() — ใช้ `database.getConnection()` จาก core library
- ห้าม hardcode status ID
- ทุก `@ResponseBody` return `new Gson().toJson(list)` — ไม่ใช่ Jackson/ApiResponse
- User object ดึงจาก HTTP session: `(User) session.getAttribute("user")`
- ไม่มี Spring Security ใน project นี้ — ไม่ใช้ `@PreAuthorize` หรือ SecurityContext

---

## DAO Pattern

```java
@Repository
public class FooDaoImpl implements FooDao {

    @Autowired
    @Qualifier("pcmsDatabase")
    private Database database;

    @Override
    public List<Map<String, Object>> findAll() {
        String sql = "SELECT foo_id, name FROM dbo.foo WHERE active_flag = 1";
        return database.queryList(sql);
    }
}
```

---

## Temp Table Rules

PCMS2 ไม่มี JdbcTemplate — ใช้ `Database` core lib ซึ่ง reuse connection จาก pool:

**Pattern A — Read queries** (drop-before + drop-after):
```java
List<Map<String, Object>> datas =
    SqlStatementHandler.queryList(this.database, PCMSSqlService.dropAllTemp, sql);
```

**Pattern B — Upsert/write** (explicit connection, DROP in finally):
```java
Connection conn = this.database.getConnection();
try {
    conn.setAutoCommit(false);
    // ... PreparedStatement INSERT/UPDATE ...
    conn.commit();
} catch (Exception e) {
    conn.rollback();
    throw e;
} finally {
    try (java.sql.Statement cleanup = conn.createStatement()) {
        cleanup.execute("IF OBJECT_ID('tempdb..#TempXxx') IS NOT NULL DROP TABLE #TempXxx");
    } catch (Exception ignored) {}
    try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
}
```

⚠️ ห้ามเพิ่ม `#tempLotNoList`, `#tempUserStatusList`, `#tempCustomerList`, `#tempCustomerShortList` ใน `PCMSSqlService.dropAllTemp`

---

## Stored Procedure

PCMS2 execute SP ผ่าน `PreparedStatement` บน `Database.getConnection()`:

```java
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;
try {
    prepared = connection.prepareStatement("EXEC [dbo].[sp_Name] ?");
    prepared.setString(1, param);
    prepared.execute();
} catch (SQLException e) {
    throw new RuntimeException(e);
} finally {
    if (prepared != null) try { prepared.close(); } catch (Exception ignored) {}
}
```

---

## ThreadLocal สำหรับ Singleton Service

```java
private static final ThreadLocal<SimpleDateFormat> SDF =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
```

อย่าเก็บ mutable state เป็น field ของ singleton — race condition ระหว่าง threads
