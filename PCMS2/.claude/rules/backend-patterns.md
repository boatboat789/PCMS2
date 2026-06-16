# Backend Patterns (Java / Spring)

## DAO + JdbcTemplate Standard

```java
@Repository
public class FooDaoImpl implements FooDao {

    @Autowired
    @Qualifier("xxxJdbcTemplate")   // ← ตรวจให้ถูก qualifier ก่อนเสมอ
    private JdbcTemplate jdbc;

    @Override
    public List<Foo> getList(String param) {
        String sql = "SELECT ... WHERE col = ?";
        return jdbc.query(sql, new Object[]{param}, new BeanPropertyRowMapper<>(Foo.class));
    }
}
```

## Temp Table — Pattern A (2-arg queryList)

ใช้เมื่อ DAO มี helper `queryList(db, dropSql, sql)` ที่ DROP ใน finally:

```java
String dropSql = "IF OBJECT_ID('tempdb..#MyTemp') IS NOT NULL DROP TABLE #MyTemp";
String sql = "SELECT * INTO #MyTemp FROM ... ; SELECT * FROM #MyTemp";
return queryList(jdbc, dropSql, sql);
```

## Temp Table — Pattern B (try/finally DROP)

ใช้เมื่อไม่มี helper หรือต้องการควบคุมเอง:

```java
String dropSql = "IF OBJECT_ID('tempdb..#MyTemp') IS NOT NULL DROP TABLE #MyTemp";
try {
    String sql = "SELECT * INTO #MyTemp FROM ...; SELECT * FROM #MyTemp";
    return jdbc.query(sql, new BeanPropertyRowMapper<>(Foo.class));
} finally {
    jdbc.execute(dropSql);
}
```

> ⚠️ PCMS2: ห้ามรวม `PCMSSearch` tables ใน dropAllTemp — มี lifecycle ต่างออกไป

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

## Security (Spring Security — ใช้เฉพาะโปรเจกต์ที่ระบุ)

3 โปรเจกต์หลัก (PCMS2/SFC/PPMM2) **ไม่ใช้ Spring Security** — ใช้ `FilterLogin` + AD แทน  
ดู `docs/<project>.md` ถ้าโปรเจกต์นั้นใช้ Spring Security
