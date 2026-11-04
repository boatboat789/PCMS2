# Backend Patterns (Java / Spring)

## DAO + JdbcTemplate Standard

```java
@Repository
public class FooDaoImpl implements FooDao {

    @Autowired
    @Qualifier("pcmsDatabase")   // ← pcmsDatabase / ppmmDatabase / sorDatabase / erpDatabase
    private JdbcTemplate jdbc;

    @Override
    public List<Foo> getList(String param) {
        String sql = "SELECT ... WHERE col = ?";
        return jdbc.query(sql, new Object[]{param}, new BeanPropertyRowMapper<>(Foo.class));
    }
}
```

## Temp Table — Pattern A (SqlStatementHandler.queryList)

ใช้เมื่อ SQL มี `SELECT INTO #temp` หรือ `CREATE INDEX` ใน batch เดียวกัน:

```java
// ✅ ถูกต้อง — drop-before + drop-after + รองรับ DDL (CREATE INDEX)
List<Map<String, Object>> datas =
    SqlStatementHandler.queryList(this.jdbc, PCMSSqlService.dropAllTemp, sql);

// ❌ ห้าม — executeQuery() expect result set เป็น response แรก; DDL done packet ทำให้ error
jdbc.queryForList(multiStatementSql);
```

`SqlStatementHandler.queryList()` ใช้ `stmt.execute()` + iterate `getMoreResults()` แทน `executeQuery()` — จึงรองรับ batch ที่มี `CREATE INDEX` ซึ่งส่ง done packet ก่อน result set

> ⚠️ PCMS2: ห้ามรวม `PCMSSearch` tables ใน `PCMSSqlService.dropAllTemp`:
> `#tempLotNoList`, `#tempUserStatusList`, `#tempCustomerList`, `#tempCustomerShortList`

## Temp Table — Pattern B (explicit connection, try/finally DROP)

ใช้เมื่อ upsert/write ด้วย explicit connection:

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
        cleanup.execute("IF OBJECT_ID('tempdb..#MyTemp') IS NOT NULL DROP TABLE #MyTemp");
    } catch (Exception ignored) {}
    try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
    DataSourceUtils.releaseConnection(conn, dataSource);
}
```

## Stored Procedure ที่ต้องการ Output Parameter

ใช้ `ConnectionCallback` (ไม่ใช่ `SimpleJdbcCall` หรือ `DriverManager`):

```java
jdbc.execute((ConnectionCallback<Void>) conn -> {
    try (CallableStatement cs = conn.prepareCall("{call sp_Name(?, ?, ?)}")) {
        cs.setString(1, input);
        cs.registerOutParameter(2, Types.VARCHAR);
        cs.execute();
        result = cs.getString(2);
    }
    return null;
});
```

## SP ที่ดึง Connection จาก DataSource โดยตรง

ใช้เมื่อต้องการจัดการ connection lifecycle เอง (เช่น SP ใน background job):

```java
Connection conn = DataSourceUtils.getConnection(dataSource);
try {
    // ... CallableStatement ...
} finally {
    DataSourceUtils.releaseConnection(conn, dataSource);
}
```

## JSON Response

```java
@ResponseBody
public String getFoo() {
    List<Foo> list = fooService.getList();
    return new Gson().toJson(list);
}
```

## ThreadLocal ใน Singleton Service

ถ้า service เป็น singleton และใช้ per-request state เช่น `SimpleDateFormat`:

```java
private static final ThreadLocal<SimpleDateFormat> SDF =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
```

อย่าเก็บ mutable state เป็น field ของ singleton — จะ race condition ระหว่าง threads

## Auth — PCMS2 ใช้ FilterLogin เท่านั้น

PCMS2 **ไม่ใช้ Spring Security** — ไม่มี `@PreAuthorize`, ไม่มี SecurityContext  
ดูรายละเอียดใน `.claude/rules/security.md`
