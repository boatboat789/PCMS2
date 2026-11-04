# hooks/ — Hooks (ยังไม่ได้ใช้งาน)

โฟลเดอร์นี้ยังว่างอยู่ — scaffold ไว้ล่วงหน้า (template กลาง — copy ไปทุกโปรเจกต์ในเวิร์กสเปซ)

## Hook คืออะไร

สคริปต์ (เช่น `.sh`) ที่ Claude Code รันอัตโนมัติ**ก่อน/หลัง**ใช้เครื่องมือบางตัว (เช่น ก่อน `Bash`,
หลัง `Edit`) ใช้กันเหตุการณ์ที่ไม่ต้องการ เช่น บล็อกคำสั่ง `rm -rf` อันตราย, รัน lint อัตโนมัติหลังแก้ไฟล์
`.java`, เตือนก่อน commit ถ้ามีไฟล์ credential ติดไปด้วย

**ตัวสคริปต์อย่างเดียวไม่พอ** — ต้องลงทะเบียนใน `.claude/settings.json` (หรือ `settings.local.json`)
ใต้ key `"hooks"` ด้วย ถึงจะทำงานจริง (ตอนนี้ทุกโปรเจกต์ในเวิร์กสเปซยังไม่มี `hooks` key ใน settings เลย)

## ไอเดียที่เข้ากับกฎกลาง (`.claude/rules/`) ที่ทุกโปรเจกต์มีอยู่แล้วเหมือนกัน

- ตรวจ `git status`/`git diff` ก่อน commit ว่ามีไฟล์ credential (`application-*.properties`,
  `database.properties`) ติดไปไหม (ตรงกับ `.claude/rules/git-workflow.md` § "ไฟล์ที่ห้าม commit"
  ที่มีอยู่ทุกโปรเจกต์)
- เตือนถ้าแก้ SQL แล้วไม่มี `IF NOT EXISTS` guard (ตรงกับ `.claude/rules/git-workflow.md` § SQL Scripts)
- รัน `mvn clean compile` อัตโนมัติหลังแก้ไฟล์ `.java` เพื่อจับ error เร็วขึ้น (ระวัง JDK version —
  บางโปรเจกต์ใช้ JDK 8 บางโปรเจกต์ (Dyeing) ใช้ JDK 17 ต้องตั้ง `$env:JAVA_HOME` ให้ถูกก่อนรันในสคริปต์)

**ยังไม่มี hook จริงในนี้** — เป็น template กลาง รอตัดสินใจว่าจะเริ่มจากอันไหนก่อน แล้วค่อยกระจาย
ไปโปรเจกต์อื่น (แต่ต้องปรับ path/JDK version ให้ตรงกับแต่ละโปรเจกต์เอง ไม่ใช่ copy เฉยๆ)
