# Documentation Mapping Rule

กฎนี้คู่กับ `.claude/rules/session-logging.md` — session-logging บันทึก "เหตุการณ์" (เกิดอะไรขึ้น วันไหน)
ส่วนกฎนี้ดูแล "จุดอ้างอิงถาวร" (ไฟล์ไหนอยู่ที่ไหน หาเจอยังไง) ให้ session ใหม่เจอได้จาก `CLAUDE.md` โดยตรง
ไม่ต้องพึ่ง memory หรือเดินหลายต่อ

## ต้องทำ — ทุกครั้งที่สร้าง "จุดอ้างอิงถาวร" ใหม่ หรือย้ายของเดิม

"จุดอ้างอิงถาวร" คือไฟล์ที่ session ในอนาคตน่าจะต้องหา:

- ไฟล์ doc ใหม่ใน `docs/` (ADR, diagram, data dictionary, reference guide)
- ไฟล์ rule ใหม่ใน `.claude/rules/`
- ย้าย/เปลี่ยนชื่อไฟล์ที่เคย mapping ไว้แล้ว (ต้องอัปเดต path ให้ตรง ไม่ใช่ปล่อยลิงก์ตาย)
- ของที่เคยอยู่ชั่วคราว (scratchpad, Artifact link, session memory) แล้วย้ายเข้า repo ถาวร

→ เพิ่มแถวใน **Document Index** ของ `CLAUDE.md` ทันที ไม่ต้องรอ user สั่ง — เลือกหมวดที่ตรงที่สุด
(Business / Technical Reference / Design Decisions / Layer Rules / ฯลฯ) ถ้าไม่มีหมวดที่เข้ากัน ให้เพิ่มหมวด
ใหม่แทนที่จะยัดผิดหมวด

## ห้ามทำ

- ห้ามปล่อยให้ไฟล์สำคัญหาได้แค่ผ่าน memory ของ Claude หรือ session ก่อนหน้า — ถ้า mapping ไม่อยู่ใน
  `CLAUDE.md`/`docs/` ถือว่ายังไม่เสร็จ
- ห้ามฝากไฟล์ deliverable (Excel, diagram, report) ไว้ที่ scratchpad ของ Claude session เป็นที่เก็บถาวร —
  ต้อง copy เข้า repo (`docs/...`) เสมอเมื่องานนั้นถือว่าเสร็จแล้ว

## เมื่อ Document Index ใน CLAUDE.md ยาวเกิน ~200 บรรทัด

แยกเป็นไฟล์ index ย่อยตามหมวด แทนที่จะยัดทุกอย่างไว้ที่เดียว:

1. สร้าง `docs/index-<หมวด>.md` (เช่น `docs/index-business.md`, `docs/index-technical.md`,
   `docs/index-permissions.md`) — ย้ายตารางของหมวดนั้นไปที่ไฟล์ใหม่ทั้งตาราง
2. `CLAUDE.md` เหลือแค่ 1 บรรทัดต่อหมวด ชี้ไปที่ไฟล์ index ย่อย พร้อมสรุปสั้นๆ ว่าหมวดนั้นมีอะไรบ้าง
3. อย่าย้ายทุกหมวดพร้อมกันถ้ายังไม่จำเป็น — ย้ายเฉพาะหมวดที่เริ่มยาว (ดูจากจำนวนแถวในตารางนั้น ไม่ใช่ความยาว
   ทั้งไฟล์)
4. ตรวจว่าไม่มีลิงก์ตายหลังย้าย — grep หา reference เก่าที่ชี้ไป path ใน CLAUDE.md ตรงๆ (ถ้ามี) แล้วอัปเดต
