# Deployment — [PROJECT_NAME]

---

## Prerequisites

| Tool | Version | หมายเหตุ |
|---|---|---|
| JDK | 1.8 | machine default อาจเป็น JDK 25 — ต้อง set JAVA_HOME ก่อน |
| Maven | 3.x | — |
| Tomcat | 8.5 / 9 | deploy แบบ WAR |
| SQL Server | — | `[DB_NAME]` ต้อง accessible |
| AD / HR API | — | [ถ้ามี — required for auth] |

---

## Build

```powershell
# Set JDK 1.8 ก่อนเสมอ
$env:JAVA_HOME = "C:\Program Files\Java\jdk-1.8"

mvn clean package -DskipTests   # → target/[PROJECT_NAME].war
mvn clean compile                # ตรวจ compile error เท่านั้น
mvn test                         # JUnit 4
```

> ถ้า `javax.annotation` error → JAVA_HOME ไม่ใช่ jdk-1.8

---

## Deploy

**Development (Eclipse WTP — แนะนำ):**
- Eclipse → Run on Server → Tomcat
- Context path: `/[CONTEXT_PATH]`
- Hot deploy: Eclipse WTP incremental

**Production:**
```
copy target/[PROJECT_NAME].war  →  [TOMCAT_HOME]/webapps/
```
WAR ต้องชื่อ `[PROJECT_NAME].war` — context path จะตาม filename

---

## First-Time Setup

```
1. copy src/main/resources/database.properties.example → database.properties
2. แก้ไข credentials ใน database.properties
3. mvn clean package -DskipTests
4. รัน SQL scripts ใน sql/ ตามลำดับวันที่ใน SSMS
5. Deploy WAR → Tomcat
```

`database.properties` ห้าม commit — อยู่ใน `.gitignore`

---

## Configuration Files

| ไฟล์ | เนื้อหา |
|---|---|
| `src/main/resources/database.properties` | DB credentials (ไม่ commit) |
| `WEB-INF/applicationContext.xml` | DataSource, HikariCP, Caffeine, beans |
| `WEB-INF/[APP]-servlet.xml` | MVC config, component scan, view resolver |
| `WEB-INF/spring-security.xml` | intercept-url, form-login, roles [Spring Security] |
| `WEB-INF/web.xml` | Servlet + filter declarations |

---

## Database Migration

ไม่มี framework — รัน SQL patches ด้วยมือใน SSMS:

```
sql/
  YYYY-MM-DD_initial_schema.sql
  YYYY-MM-DD_add_feature_x.sql
  ...
```

รันตามลำดับวันที่ — ดู `NEXT_SESSION.md` สำหรับ scripts รอรัน

---

## Troubleshooting

| อาการ | สาเหตุ | วิธีแก้ |
|---|---|---|
| `javax.annotation` compile error | JDK > 1.8 | `$env:JAVA_HOME = "C:\Program Files\Java\jdk-1.8"` |
| HikariPool connection timeout | DB offline / firewall | ตรวจ database.properties, เชื่อม VPN |
| `#temp table already exists` | stale temp บน reused connection | ใช้ Pattern A/B ใน DAO |
| 404 after deploy | WAR ชื่อไม่ตรง | rename WAR ให้ตรง context path |
| 403 on new URL | intercept-url ไม่ครอบ | เพิ่มใน spring-security.xml ก่อน catch-all |
| Blank page / no response | Exception หลุดออก controller | ตรวจ Tomcat log, wrap ด้วย try/catch |
| Cache stale | Master data เปลี่ยนหลัง deploy | restart Tomcat หรือ call refresh() |
