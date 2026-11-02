# WAR บน Tomcat ภายนอก: Metaspace ค้างหลัง redeploy — หลักคิด + วิธีพิสูจน์ + สถานะของ PCMS2 (เพิ่ม 2026-10-21)

ปรับจาก `template_claude/.claude/rules/webapp-classloader-leak-redeploy.md` (บทเรียน Dyeing — Boot 3/Java 17/Tomcat 10) และ `QCMS/.claude/rules/webapp-classloader-leak-redeploy.md` ให้ตรงกับ PCMS2: **Spring MVC + JSP · JDK 1.8 · Tomcat 8.5 · HikariCP · fastexcel-reader · Unirest (kong) · ThreadPoolTaskScheduler**
อาการที่ต้องสงสัย: redeploy แล้ว Metaspace ไม่ลด / Tomcat ต้อง restart / log `SEVERE ... created a ThreadLocal ... failed to remove it`

## สถานะของ PCMS2 (ตรวจจากโค้ด+pom 2026-10-21 — ยังไม่ได้รัน harness รอบนี้)

| จุด | สถานะ |
|---|---|
| `AppShutdownListener` | ✅ sweepThreadLocals + `kong.unirest.Unirest.shutDown()` + deregister driver (กรอง classloader) · บรรทัดแรกใน `web.xml` |
| pom: fastexcel-reader | ✅ exclude `aalto-xml` |
| jtds | ✅ comment ออกแล้ว (2026-06-20) |
| `SchedulerConfig` | ✅ `destroyMethod=shutdown` + daemon + ไม่เรียก `initialize()` |
| mssql-jdbc | ✅ 9.4.1.jre8 ใน pom (เคยอยู่ 6.1.0 ที่ deploy — ยืนยันเวอร์ชันใน WAR จริงด้วย `unzip -l`) |
| deploy PROD | ✅ ตามบันทึก 2026-07-17 |

## เฝ้าระวังเฉพาะ PCMS2

- ⚠️ `mvn` CLI build ใช้ verify ไม่ได้ (artifact `core` ไม่อยู่ใน `.m2`) — build ผ่าน Eclipse แล้ว **ตรวจ WAR จริง** ว่าไม่มี `aalto-xml*.jar` ใน `WEB-INF/lib` (เคยเจอ WAR export ค้างของเก่า)
- ห้ามรวม `PCMSSearch` temp table ใน cleanup (lifecycle ต่างออกไป — ดู backend-patterns)

## หลักคิด

- **ขนาด WAR ไม่ใช่สาเหตุ** — Metaspace ค้างเพราะมีของ "นอกแอป" (thread ของ Tomcat, static/cache ของ JDK, daemon thread ของไลบรารี) ถือ webapp classloader เก่าไว้
- **อย่าเดาจากโค้ด พิสูจน์ด้วยการ deploy/undeploy ซ้ำแล้ววัด** — แก้ตัวแรกแล้วยังค้าง ≠ แก้ผิด มักมีตัวที่สอง/สามซ้อน
- **ต้อง "ใช้งานจริงก่อน undeploy"** (ล็อกอินผ่านหน้า JSP จริงด้วย `curl -L`, พิมพ์ PDF, เรียก HTTP client, รอ scheduler 1 รอบ) — leak เกิดหลังใช้งาน ไม่ใช่ตอนเปิดหน้า login · ยืนยันว่า JSP compile เกิดจริงจากไฟล์ `.class` ใหม่ใน `Tomcat/work/Catalina/localhost/<app>/org/apache/jsp/`
- เป้าหมาย = loader เก่า **0 ตัวทุกรอบ โดยไม่ต้องรอ memory ตึง** · ผล "ฟันปลา" (ค้างรอบ 1–3 แล้วหายรอบ 4) = SoftReference ยึดอยู่ ต้องแก้ ไม่ใช่ปล่อยผ่าน

## ตัวการที่เจอซ้ำในระบบ Java 8 / Tomcat 8.5 ของ workspace นี้ (เช็คทุกครั้งที่แก้ pom หรือเพิ่มไลบรารี)

| ตัวการ | วิธีแก้ที่ใช้อยู่ |
|---|---|
| StAX implementation ลากมาเป็น transitive: `jasperreports → jackson-dataformat-xml → woodstox-core`, `fastexcel-reader → aalto-xml` — Tomcat `EncodingDetector.XML_INPUT_FACTORY` (static) แคชไว้ผ่าน `META-INF/services/javax.xml.stream.XMLInputFactory` | `<exclusions>` ตัด `jackson-dataformat-xml` ออกจาก `jasperreports` / ตัด `aalto-xml` ออกจาก `fastexcel-reader` (ถ้าไม่มีโค้ดใช้ `JRXmlDataSource`/`JRJsonDataSource`/StAX) — เกิดจาก **แค่ jar อยู่ใน `WEB-INF/lib`** ไม่เกี่ยวว่าโค้ดเรียกหรือไม่ · **ต้องเช็ค `mvn dependency:tree` หาทุก StAX implementation ไม่ใช่แค่ตัวที่เคยเจอ** |
| ThreadLocal ของ mssql-jdbc (`ActivityCorrelator`) บน thread pool ของ Tomcat | `sweepThreadLocals()` ใน `AppShutdownListener.contextDestroyed` (JDK 8 ไม่ต้อง `--add-opens`) — ทดสอบจริงแล้วไม่เกิดบน Java 8/Tomcat 8.5 แต่ใส่กันไว้ต้นทุนเป็นศูนย์ |
| mssql-jdbc 6.x สร้าง non-daemon Timer thread | ใช้ `9.4.1.jre8` (ไม่ใช้ 10+/12.x เพราะ default `encrypt=true` ชน connection string เดิม) |
| jtds / mysql-connector ที่ไม่มีโค้ดใช้ — SPI auto-register driver ผูก classloader | ตัดออกจาก pom |
| `ThreadPoolTaskScheduler` ถูก `initialize()` ซ้ำ → pool แรกหลุดไม่มีใคร shutdown | `@Bean(destroyMethod = "shutdown")` + `setDaemon(true)` + **ห้ามเรียก `initialize()` เอง** |
| `JasperReport` โหลด/คอมไพล์ใหม่ทุก request (`JRLoader.loadObject`/`compileReport` ในเมธอด) → generate evaluator class ใหม่ทุกครั้ง | cache `JasperReport` ต่อ template (`ConcurrentHashMap`) + `clear()` ใน listener |
| Unirest global static (ทั้ง `kong.unirest` และ `com.mashape`) | `Unirest.shutDown()` ใน listener หรือใช้ `spawnInstance()` + `shutDown()` ต่อการเรียก |
| static `Database`/cache/executor ที่ไม่มีใครปิด | มี `clear()`/`reset()`/`shutdown()` แล้วเรียกจาก `AppShutdownListener` |
| thread ยาวที่ยังถือ webapp loader | `setDaemon(true)` + `setContextClassLoader(parentCl)` · listener ไล่ทุก thread (ยกเว้นตัวเอง) แล้วตั้ง parent loader |
| deregister JDBC driver แบบ blind | **กรอง `driver.getClass().getClassLoader() == webappCl` เสมอ** — ไม่งั้นดึง driver ของแอปอื่นบน Tomcat เดียวกัน |
| `AppShutdownListener` ลงทะเบียนไม่ใช่บรรทัดแรกใน `web.xml` | ต้องอยู่แรกสุด (destroyed ลำดับสุดท้าย) |

หลักฐาน runtime ที่ผ่านมา: 2026-07-17 deploy/undeploy จริงบน Tomcat 8.5.99 / JDK 1.8.0_202 + heap dump + jhat ไล่ GC root → rootset ว่างเปล่า (ทั้ง 7 ระบบ Java 8 + Dyeing) · **ผลใช้ได้เฉพาะเวอร์ชันที่ทดสอบ — ระบุเวอร์ชันเสมอ** (บน Java 17/Tomcat 10.1 พบ ThreadLocal ของ mssql-jdbc จริงที่ Dyeing)

## วิธีพิสูจน์ (เมื่อ admin แจ้ง "redeploy แล้ว Metaspace ไม่ลด")

1. Tomcat 8.5.x เวอร์ชันตรง prod บน JDK 1.8 (หรือ harness `tomcat-embed-core` เวอร์ชันตรงกัน): deploy WAR จริง → ใช้งานจริง → undeploy → `System.gc()` ซ้ำ → heap dump (`jmap -dump:format=b` **ห้าม `:live`** เพราะ force GC ทำลายหลักฐาน) → jhat/MAT ไล่ GC root ของ classloader (เดินผ่าน SoftReference ด้วย)
2. ต้องมี **control** (รันโค้ดเดิมเทียบ) และรันซ้ำ ≥4 รอบ ไม่งั้นแยกไม่ออกว่า fix ได้ผลหรือแค่ GC ทัน
3. บน Tomcat จริง: `jstat -gc <pid>` ดูคอลัมน์ **MU** (Metaspace used) ก่อน/หลัง redeploy หลายรอบ + Manager → "Find Leaks"
4. ใช้ DEV เท่านั้น ห้ามชี้ PRD · อย่าฆ่า `java.exe` ที่ไม่ใช่ของเรา
5. **ตรวจ WAR จริงที่จะ deploy** (`unzip -l` ดู `WEB-INF/lib/`) — ยืนยันว่า fix อยู่ใน artifact จริง (เคส jar ค้างเก่าผ่าน Eclipse `.classpath`)

## เขียนโค้ดใหม่ — เช็คลิสต์กัน leak

- class ที่ถือ `Database`/connection เป็น field และอยู่ใน lifecycle ของ Tomcat (Servlet/Filter/Listener/bean) → ปิดใน `destroy()`/`contextDestroyed` (เช็ค null)
- static ที่ถือ `Database`/thread/executor/cache → ต้องมีเมธอดล้างและเรียกจาก `AppShutdownListener`
- thread ใหม่ → `setDaemon(true)` + `setContextClassLoader(parent)` หรือจบเองเมื่อ `contextDestroyed` · `new Timer()` เป็น non-daemon → ใช้ `new Timer(true)`
- ไลบรารีที่ generate class ต่อการเรียก (Jasper, Groovy, cglib) → cache ผลลัพธ์
- **ก่อนตัด jar ที่ "ไม่มีโค้ดเรียก"** — grep `import` ไม่พอ (barcode4j/zxing/batik ถูกใช้ผ่าน `.jrxml`) · ต้องสร้างรายงาน/ฟีเจอร์จริงทุกตัวหลังตัด
- ถ้าย้ายไป JDK 17 / Tomcat 10 ต้องกลับมาทำเพิ่ม: sweep ThreadLocal ด้วย `Unsafe` fallback (ไม่พึ่ง `--add-opens`), `ObjectMethods.OBJECT_EQ.asTypeCache`, `ResourceBundle.clearCache(webappCl)`, Batik CleanerThread — ดู `template_claude/.claude/rules/webapp-classloader-leak-redeploy.md` และ Dyeing `docs/metaspace-leak-redeploy-2026-10-21.md`

## กับดักที่พลาดมาแล้ว (อย่าทำซ้ำ)

- ❌ เจอ StAX library ตัวเดียวแล้วปิดเคส — มีหลาย implementation (aalto-xml, woodstox-core) และหลายระบบที่ใช้ dependency เดียวกัน (เช็คให้ครบก่อนประกาศว่าปิด)
- ❌ ทดสอบด้วย `curl` root URL ไม่ตาม redirect → ไม่เคย trigger JSP compile → ผ่านเพราะยังไม่เคยเกิด (BGJOB เคยพลาดแบบนี้) · ใช้ `curl -sSL` ให้ถึงหน้า login จริง
- ❌ เชื่อเอกสารเรื่องเวอร์ชัน Java/Tomcat ของ prod โดยไม่ถาม admin (`bin\version.bat`)
- ❌ ตัดสินว่า "แก้แล้ว" เพราะ log ขึ้นว่า sweep ลบ N entry — ต้องดู GC root ว่างเปล่าจริง
- ❌ ลบ `unirest-java` ออกจาก pom โดยคิดว่าไม่ใช้ — `BGJobApi.jar` อ้าง `kong/unirest/*` + `org.apache.http.*` → `NoClassDefFoundError` (InspectSystem/QCMS เคยพังรอบ 3)
- ❌ ส่ง admin โดยไม่บอกวิธีตรวจบน Tomcat จริงและข้อจำกัดของการทดสอบ

## ห้ามทำ

- ห้ามแก้ด้วยการสั่ง admin ใส่ `-XX:MaxMetaspaceSize` หรือ restart Tomcat ทุกครั้งโดยไม่หาตัวถือ — แค่ซ่อนอาการ
