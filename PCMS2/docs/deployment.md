# Deployment — PCMS2

---

## Prerequisites

| Tool | Version | หมายเหตุ |
|---|---|---|
| JDK | 1.8 | machine default อาจเป็น JDK 25 — ต้อง set JAVA_HOME ก่อน |
| Maven | 3.x | ใช้ `-o` (offline) ได้ ถ้า `.m2` มี dependency ครบ |
| Tomcat | 9 (เครื่องนี้ทดสอบกับ `D:\apache-tomcat-9.0.100`) | deploy แบบ WAR · เวอร์ชันบน PRD ให้ admin ยืนยันด้วย `bin\version.bat` |
| SQL Server | PCMS / PPMM (`10.11.44.101`) · SOR_PRODUCTION (`10.11.44.220`) · ERP_PROD (`10.11.44.219\ATECHERP`) | 4 datasource ใน `database.properties` |

---

## Build

```powershell
# Set JDK 1.8 ก่อนเสมอ
$env:JAVA_HOME = "C:\Program Files\Java\jdk-1.8"

mvn clean package -DskipTests   # → target/PCMS2.war
mvn clean compile                # ตรวจ compile error เท่านั้น
```

> ถ้า `javax.annotation` error → JAVA_HOME ไม่ใช่ jdk-1.8
> ⚠️ ห้ามรัน `mvn clean`/harness พร้อม Tomcat หรือ Eclipse ที่ใช้ `target/classes` — build ใน worktree แยก (`git worktree add`) ถ้า Eclipse เปิดอยู่

---

## Deploy

**Development:** Eclipse WTP → Run on Server → Tomcat · context path `/PCMS2`
ทดสอบหน้าเว็บชี้ DEV โดยไม่แตะ Tomcat เดิม: `tools/dev-web-local/README.md` (พอร์ต 8082)

**Production:**
```
copy target/PCMS2.war  →  [TOMCAT_HOME]/webapps/
```
WAR ต้องชื่อ `PCMS2.war` — context path จะตาม filename

### ✅ Checklist ก่อนขึ้น PRD (ทำทุกครั้ง)

1. **build จาก `gitlab_master/main` ล่าสุด** (`git fetch gitlab_master` ก่อน) — ไม่ใช่ branch งาน
2. **`src/main/resources/database.properties` ต้องชี้ PRD ทั้ง 4 ตัว** (PCMS/PPMM `10.11.44.101` ไม่มี `\PCMSDEV` · `SOR_PRODUCTION` · `ERP_PROD`)
   — ไฟล์นี้ไม่อยู่ใน git (worktree ใหม่ต้อง copy เอง) · ตรวจใน WAR: `unzip -p target/PCMS2.war WEB-INF/classes/database.properties | findstr url`
3. ตรวจว่า WAR ไม่มี `DevTestLoginFilter` (ของ `tools/dev-web-local` ใส่เฉพาะสำเนาทดสอบ): `unzip -p target/PCMS2.war WEB-INF/web.xml | findstr DevTest` ต้องว่าง
4. SQL patch ที่ต้องรันบน PRD ก่อน deploy: ดูหัวข้อ Database Migration ด้านล่าง (ณ 2026-11-03 = **ไม่มี**)
5. **อย่า deploy ช่วง job** — `TaskService` รัน 01:00 และนาทีที่ :14/:44 ทุกชั่วโมง · ช่วงปลอดภัย :19–:38 และ :49–:08
6. หลัง deploy: เปิด `/PCMS2/Main` และ `/PCMS2/Detail` ค้น SO 1 ใบ · Download Excel 1 ครั้ง · ดู log ของ Tomcat ว่าไม่มี SQL error
7. ⚠️ redeploy ซ้ำหลายครั้งโดยไม่ restart อาจทำ Metaspace โต — ดู `.claude/rules/webapp-classloader-leak-redeploy.md`

---

## First-Time Setup

```
1. copy database.properties (ขอจากทีม — ไม่มี .example ใน repo) → src/main/resources/
2. mvn clean package -DskipTests
3. รัน SQL scripts ใน sql/ ที่ระบุว่ารันบน PRD (ดูหัวไฟล์) ใน SSMS
4. Deploy WAR → Tomcat
```

`database.properties` ห้าม commit — อยู่ใน `.gitignore` · ถ้าจะ commit ต้องให้ user สั่ง และ push เฉพาะ `gitlab_master` (ดู `.claude/rules/database-properties-company-remote-only.md`)

---

## Configuration Files

| ไฟล์ | เนื้อหา |
|---|---|
| `src/main/resources/database.properties` | DB credentials 4 datasource (ไม่ commit) · โหลดผ่าน `config/DatabaseConfig.java` (`@PropertySource`) |
| `src/test/resources/database.properties` | ชี้ DEV `10.11.44.101\PCMSDEV,1435` — ใช้กับ harness/`tools/dev-web-local` (ไม่ commit) |
| `WEB-INF/applicationContext.xml` | beans |
| `WEB-INF/PCMS2-servlet.xml` | MVC config, component scan, view resolver |
| `WEB-INF/web.xml` | Servlet + `FilterLogin` |

---

## Database Migration

ไม่มี framework — รัน SQL patches ด้วยมือใน SSMS (ตรวจหัวไฟล์ว่ารันที่ DEV หรือ PRD)

| ไฟล์ | รันที่ | สถานะ |
|---|---|---|
| `sql/2026-06-23_add_admin_itsupp_permits_pcms2.sql` | ดูหัวไฟล์ | — |
| `sql/2026-11-01_01_create_Z_ATT_CustomerConfirm2_dev.sql` | **DEV เท่านั้น** (PRD มีตารางอยู่แล้ว) | รันบน DEV แล้ว |

ตาราง/proc ที่โค้ดใน `main` อ้างถึงแต่ไม่มีใน `main` เดิม (`b150c87`): `SumBillCache`, `SumGRCache`, `spd_SumBillAndGoodReceive` — มีอยู่บน PRD แล้ว (สร้าง 2026-01-22/23 ดู `docs/cache-fix-plan-2026-11-03.md`) ไม่ต้องรันอะไรเพิ่ม

---

## Troubleshooting

| อาการ | สาเหตุ | วิธีแก้ |
|---|---|---|
| `javax.annotation` compile error | JDK > 1.8 | `$env:JAVA_HOME = "C:\Program Files\Java\jdk-1.8"` |
| TLS error ต่อ DB DEV ตอนรัน Tomcat | `JRE_HOME` ไม่ใช่ jdk-1.8 | ตั้ง `JRE_HOME` = jdk-1.8 |
| HikariPool connection timeout | DB offline / firewall | ตรวจ database.properties, เชื่อม VPN |
| `#temp table already exists` | stale temp บน reused connection | ใช้ Pattern A/B ใน DAO |
| 404 after deploy | WAR ชื่อไม่ตรง | rename WAR เป็น `PCMS2.war` |
| Blank page / no response | Exception หลุดออก controller | ตรวจ Tomcat log |
| Report CFM ตอบ 405 | วันที่ผิดรูปแบบ (ต้อง `MMM d, yyyy`) ทับ error จริง | ส่งวันที่ให้ถูกรูปแบบ |
