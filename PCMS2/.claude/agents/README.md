# agents/ — Sub-agents (ยังไม่ได้ใช้งาน)

โฟลเดอร์นี้ยังว่างอยู่ในโปรเจกต์นี้ — scaffold ไว้ล่วงหน้า (template กลาง — copy ไปทุกโปรเจกต์)

⚠️ **DyeingRecord มี agents/ ใช้งานจริงอยู่แล้ว 9 ตัว** (dye-scout, dye-planner, dye-planner-s,
dye-backend, dye-backend-s, dye-frontend, dye-frontend-s, dye-micro, dye-verify) — ถ้าจะทำ agents/
ให้โปรเจกต์อื่น **อย่า copy ไฟล์ของ DyeingRecord ไปตรงๆ** เพราะชื่อ/prompt ผูกกับโครงสร้างโค้ด
เฉพาะของ DyeingRecord (dao/service/controller pattern, Spring Boot) — ให้ดูเป็น "ตัวอย่างรูปแบบ"
(แยก agent ตามขนาดงาน S/M/L + scout ก่อน plan ก่อน implement) แล้วออกแบบใหม่ให้ตรงกับ stack จริง
ของแต่ละโปรเจกต์แทน

## Agent คืออะไร

Sub-agent = รันแยก context ของตัวเอง (ไม่ปนกับบทสนทนาหลัก) กำหนด tool ที่ใช้ได้และ model ได้เฉพาะตัว
ต่อ 1 agent เหมาะกับงานที่ต้องการแยก context ชัดเจน (เช่น scout อ่านโค้ดเยอะๆ แล้วสรุปสั้นๆ กลับมา
ไม่ให้ context หลักบวมไปด้วยไฟล์ที่อ่านผ่าน) หรือกำหนด role เฉพาะทาง (code-reviewer, security-auditor)

## โครงสร้างไฟล์ต่อ 1 agent

```
agents/
  ชื่อ-agent.md   ← frontmatter (name/description/tools/model) + system prompt
```

**ยังไม่มี agent จริงในนี้** — เป็น template กลาง รอแต่ละโปรเจกต์ประเมินว่าต้องการ agent
เฉพาะทางแบบไหนตาม stack และขนาดงานจริงของโปรเจกต์นั้น
