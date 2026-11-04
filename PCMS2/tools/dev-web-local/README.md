# dev-web-local — รัน PCMS2 บนเครื่อง ชี้ DEV แล้วกดทดสอบหน้าเว็บ

ดัดแปลงจาก `LBMS/tools/dev-web-local` · สร้างสำเนา webapp นอก repo (`C:/Users/94687/pcms2-dev-tomcat`) ไม่แตะ source/Tomcat เดิม

1. `mvn -o package -DskipTests` (JDK 8 · ไม่ต้อง clean) — WAR ยังมี `database.properties` ของ PRD แต่สำเนาจะถูกเขียนทับ
2. `python tools/dev-web-local/setup_dev_webapp_local.py`
   - ใช้ `src/test/resources/database.properties` (DEV `10.11.44.101\PCMSDEV,1435` ทั้ง 4 datasource, gitignored) เป็น config ของสำเนา
   - guard: หยุดถ้าไม่ใช่ DEV ครบ 4 ตัว หรือมี `ERP_PROD`/`SOR_PRODUCTION`
3. PowerShell: `JAVA_HOME`/`JRE_HOME` = `jdk-1.8` · `CATALINA_HOME=D:\apache-tomcat-9.0.100` · `CATALINA_BASE=C:\Users\94687\pcms2-dev-tomcat`
   แล้ว `catalina.bat run` (เปลี่ยนเส้นทาง stdout ไปไฟล์ เพื่อเห็นบรรทัด `[DevTest] scheduled jobs OFF`)
4. เปิด `http://localhost:8082/PCMS2/Main` (localhost เท่านั้น) — `DevTestLoginFilter` ล็อกอินเป็น `ZZTEST` สิทธิ์ `VIEWONLY`
   และปิด scheduled jobs (TaskService.scheduleEnabled=false)
5. จบ: `catalina.bat stop`

## กับดัก
- DEV ของ PCMS2 มีสิทธิ์แค่ `CUST01`/`CUST02`/`VIEWONLY` (ไม่มี ADMIN/ITSUPP) — แก้ใน init-param ถ้าต้องการอย่างอื่น
- bean ของ controller/service อยู่ใน context ของ DispatcherServlet (`FrameworkServlet.CONTEXT.PCMS2`) ไม่ใช่ root context
- cron ของ TaskService รัน 01:00 และ :14/:44 ทุกชั่วโมง — **อย่าสตาร์ตใกล้เวลานั้น** (filter ปิด job ได้หลัง context พร้อมเท่านั้น)
- logging ของแอปเป็น NOP (ไม่มี slf4j binding) → ดูว่า job รันหรือไม่จาก log ไม่ได้
- หน้า Summary: ค่าเริ่มต้นตัวกรองสถานะคือ "In-Process" — SO ที่ปิดแล้วต้องเลือก "All" ถึงจะเจอ
