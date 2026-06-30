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
| DB access | HikariCP + `JdbcTemplate` (migrated 2026-06-19) — ไม่ใช่ `Database` core lib |
| Reports | Apache POI 5.2.5 + JXLS 2.13.0 (Excel templates from classpath) |

---

## Qualifiers

PCMS2 ใช้ JdbcTemplate (หลัง migrate 2026-06-19):

| Qualifier | DB | ใช้ใน |
|---|---|---|
| `@Qualifier("pcmsDatabase")` (@Primary) | PCMS | main production/sales data |
| `@Qualifier("ppmmDatabase")` | PPMM | planning data |
| `@Qualifier("sorDatabase")` | SOR | sale order data |
| `@Qualifier("erpDatabase")` | ERP Atech | SAP integration |

> ตรวจ qualifier ก่อนแตะ datasource config เสมอ — ผิด qualifier = ผิด DB

---

## Rules

- ห้าม JPA/Hibernate — `JdbcTemplate` + raw SQL เท่านั้น
- ห้าม `DriverManager.getConnection()` — ใช้ `DataSourceUtils.getConnection(dataSource)` เสมอ
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
    private JdbcTemplate jdbc;

    @Override
    public List<Map<String, Object>> findAll() {
        String sql = "SELECT foo_id, name FROM dbo.foo WHERE active_flag = 1";
        return jdbc.queryForList(sql);
    }
}
```

---

## Temp Table Rules

PCMS2 ใช้ HikariCP + JdbcTemplate — connection reuse จาก pool:

**Pattern A — Read queries** (drop-before + drop-after, รองรับ CREATE INDEX ใน batch):
```java
List<Map<String, Object>> datas =
    SqlStatementHandler.queryList(this.jdbc, PCMSSqlService.dropAllTemp, sql);
```

`queryList()` ใช้ `stmt.execute()` + iterate results — ไม่ใช่ `queryForList()` ซึ่ง expect result set เป็น response แรก

**queryList() internals (อย่า replicate โดยไม่อ่านก่อน):**
```java
// DROP-before, main SQL, DROP-after ทั้งหมดวิ่งบน connection เดียวกัน (StatementCallback)
// finally รัน SET NOCOUNT OFF + DROP-after เสมอ แม้ main SQL throw
// guard=50 ป้องกัน infinite loop ถ้า driver คืน 0 แทน -1
jdbc.execute((StatementCallback<...>) stmt -> {
    try { stmt.execute(dropSql); } catch (Exception ignored) {}
    try {
        boolean hasResult = stmt.execute("SET NOCOUNT ON;\n" + sql);
        while (guard-- > 0) { ... }
    } finally {
        try { stmt.execute("SET NOCOUNT OFF;"); } catch (Exception ignored) {}
        try { stmt.execute(dropSql); } catch (Exception ignored) {}
    }
    return rows;
});
```

⚠️ **สาเหตุ try-finally**: ถ้า main SQL throw แล้วไม่มี finally → `#temp` ค้างบน connection → request ถัดไปได้ "Object already exists"
⚠️ **สาเหตุ SET NOCOUNT OFF**: SQL Server session state ติดค้างข้าม connection reuse → `jdbc.update()` อื่นอาจได้ rows-affected = 0

**Pattern B — Upsert/write** (explicit connection, DROP in finally):
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

⚠️ ห้ามเพิ่ม `#tempLotNoList`, `#tempUserStatusList`, `#tempCustomerList`, `#tempCustomerShortList` ใน `PCMSSqlService.dropAllTemp`

---

## Stored Procedure

PCMS2 execute SP ผ่าน `ConnectionCallback` บน JdbcTemplate:

```java
jdbc.execute((ConnectionCallback<Void>) conn -> {
    try (CallableStatement cs = conn.prepareCall("{call dbo.sp_Name(?, ?)}")) {
        cs.setString(1, input);
        cs.registerOutParameter(2, Types.VARCHAR);
        cs.execute();
        result = cs.getString(2);
    }
    return null;
});
```

SP ที่ไม่มี output parameter ใช้ `PreparedStatement.execute()` ตรง ๆ ผ่าน JdbcTemplate.execute()

---

## ThreadLocal สำหรับ Singleton Service

```java
private static final ThreadLocal<SimpleDateFormat> SDF =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
```

อย่าเก็บ mutable state เป็น field ของ singleton — race condition ระหว่าง threads

**Instance field ก็ต้องใช้ ThreadLocal ถ้า object นั้นถูกเก็บใน singleton:**
```java
// SqlStatementHandler.sshUtl เก็บใน PCMSDetailDaoImpl (singleton)
// ดังนั้น instance field ต้องเป็น ThreadLocal ด้วย
public final ThreadLocal<SimpleDateFormat> sdf4 =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
// ใช้: sdf4.get().parse(str)
```

---

## Credentials

DB credentials อยู่ใน `src/main/resources/database.properties` (gitignored — ไม่อยู่ใน source control)
`DatabaseConfig.java` โหลดผ่าน `@PropertySource("classpath:database.properties")` + `@Value("${key}")`

⚠️ บน machine ใหม่หลัง clone ต้องสร้าง `database.properties` ใหม่ — ดูค่าจากเอกสาร infra หรือ DBA
