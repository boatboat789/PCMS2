# Core Behavior Rules

กฎเหล่านี้ใช้กับทุกโปรเจกต์ใน workspace (PCMS2 / SFC / PPMM2)

## ห้ามทำ

- **ห้ามใช้ JPA / Hibernate** — ทุก DB access ใช้ `JdbcTemplate` กับ raw SQL หรือ Stored Procedure เท่านั้น
- **ห้ามใช้ Jackson / ObjectMapper** — JSON ใช้ `new Gson().toJson()` / `new Gson().fromJson()` เสมอ
- **ห้ามเพิ่ม auth check ใน controller** — `FilterLogin` จัดการทั้งหมดแล้ว
- **ห้ามใช้ `DriverManager.getConnection()`** — ใช้ HikariCP pool ผ่าน `DataSourceUtils` เท่านั้น
- **ห้ามทิ้ง `#temp` table ข้ามการเชื่อมต่อ** — ต้องใช้ Pattern A หรือ B เสมอ (ดู backend-patterns.md)

## ต้องทำ

- **ตรวจ `@Qualifier` ก่อนแตะ datasource config เสมอ** — แต่ละ DAO ผูกกับ DB เฉพาะ ผิด qualifier = ผิด DB
- **ตั้ง JAVA_HOME=JDK 1.8 ก่อน build** — `$env:JAVA_HOME = "C:\Program Files\Java\jdk-1.8"`
- **User object ดึงจาก HTTP session** — ไม่ใช่ SecurityContext (ไม่ได้ใช้ Spring Security ใน 3 โปรเจกต์นี้)
- **`core` library = LDAP + FTP เท่านั้น** — อย่าใช้ core สำหรับงานอื่น

## เมื่อแก้ SQL

- Run SQL patch ใน SSMS ด้วยมือ — ไม่มี migration framework
- ตรวจ pending scripts ใน `NEXT_SESSION.md` ก่อนเริ่มงานที่แตะ DB
- Column naming: `snake_case` ใน DB → `camelCase` ใน Java (BeanPropertyRowMapper)
