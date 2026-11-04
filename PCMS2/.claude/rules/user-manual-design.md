# User-Manual / Documentation Design Rules

กฎนี้ใช้กับทุกโปรเจกต์ใน workspace — ใช้เมื่อเขียน/แก้ไขคู่มือผู้ใช้ในระบบ (in-app manual, help text, FAQ,
onboarding) หรือเอกสารอื่นที่มีกลุ่มเป้าหมายเป็น **ผู้ใช้งานปลายทาง** (ไม่ใช่ developer/PM)

บทเรียนจากเคสจริง: Dyeing feature "renumber po_d_item หลังลบ line" (2026-08-19) — user แจ้งว่าคู่มือ
"ข้อความมหาศาลมาก user ไม่อ่าน" — แก้โดยเปลี่ยนจากข้อความล้วนเป็น visual component แล้ว user บอกว่า
"ดีมาก" และสั่งให้ใช้แนวทางนี้ **ทุกโปรเจกต์** ต่อไป

## ต้องทำ

1. **อธิบายการเปลี่ยนแปลงสถานะ/ข้อมูล (before → after) ด้วย diagram ภาพ ไม่ใช่ข้อความล้วน**
   ตัวอย่าง: "มี Line 10,20,30,40 แล้วลบ 30 เหลือ 10,20,30" (ข้อความ) → เปลี่ยนเป็น chip diagram
   ก่อน/หลัง (แดง+ขีดทับ = ถูกลบ, เขียว+✓ = ค่าใหม่, ลูกศรคั่นกลาง)

2. **ขั้นตอนทำงานแบบ step-by-step (>3 ขั้น) → ใช้ step-card** (ไอคอนเลขขั้นตอนวงกลม + bold title สั้น +
   fact chip เล็กๆ แทน paragraph ยาวใน `<li><small>...</small></li>`) — เงื่อนไข/คำเตือนสำคัญที่ไม่ใช่ fact
   สั้นๆ ให้แยกเป็นกล่อง alert (warning/danger) ต่างหาก อย่ายัดรวมเป็นประโยคเดียวยาวๆ

3. **รายการฟีเจอร์/ส่วนประกอบหน้าจอ (>3 อย่าง) → ใช้ card grid** (ไอคอน + bold label สั้น + คำอธิบาย
   1 ประโยค) แทน bullet list ที่มีประโยคยาวต่อข้อ

4. **Component ต้องเป็น CSS-only** (ไม่พึ่ง JS/library ใหม่), **accessible**
   (diagram ใส่ `role="img"` + `aria-label` บรรยายผลลัพธ์เต็มให้ screen reader, icon ตกแต่งใส่
   `aria-hidden="true"`), **responsive** (จอเล็ก `max-width:480px` ให้ chip/fact เรียงแนวตั้งแทนแนวนอน)

5. **บีบอัดการนำเสนอ ไม่ใช่บีบอัดเนื้อหา** — ทุก fact/เงื่อนไข/ข้อยกเว้น/คำเตือนที่มีอยู่เดิมต้องยังอยู่ครบ
   หลังปรับ design แค่จัดรูปแบบให้สแกนง่ายขึ้น ห้ามตัดข้อมูลทิ้งเพื่อความสั้น

## ห้ามทำ

- ห้ามปล่อย `<ol><li><small>ประโยคยาวหลายอนุประโยคผูก → ต่อกัน</small></li></ol>` ไว้เป็น step-by-step
  instructions โดยไม่แปลงเป็น step-card เมื่อมีมากกว่า 3 ขั้นตอน
- ห้าม redesign แล้วลบ/ย่อทิ้งข้อมูลจริง (เงื่อนไข, ข้อยกเว้น, badge สถานะ, สิทธิ์ผู้ใช้) เพื่อให้ดูสั้นลง
- ห้ามเพิ่ม JS framework/library ใหม่แค่เพื่อทำ diagram ที่ทำด้วย CSS ธรรมดาได้อยู่แล้ว

## เมื่อไหร่ควรใช้ (สัญญาณ)

- อธิบาย "ทำไม X ถึงเปลี่ยนเป็น Y" (FAQ ที่มีสถานะ/ค่าก่อน-หลัง)
- ขั้นตอนทำงานแบบ step-by-step ที่มีมากกว่า 3 ขั้น
- รายการฟีเจอร์/ส่วนประกอบ UI ที่มีมากกว่า 3 อย่างในหัวข้อเดียว
- user บ่นว่าคู่มือ/เอกสารส่วนไหน "อ่านยาก" "เยอะเกินไป" "ไม่เข้าใจ"

## Reference implementation (CSS พร้อมใช้ — คัดลอกปรับได้ทุกโปรเจกต์ที่ใช้ Bootstrap 4)

```css
/* Before/after chip diagram */
.lrd-wrap { background:#fff; border:1px solid #e3e6ec; border-radius:8px; padding:10px 14px; margin:6px 0; }
.lrd-row { display:flex; align-items:center; flex-wrap:wrap; gap:6px; margin:4px 0; }
.lrd-caption { font-size:11px; font-weight:700; color:#6c757d; text-transform:uppercase; letter-spacing:.03em; min-width:92px; }
.lrd-chip { display:inline-flex; align-items:center; gap:4px; padding:3px 10px; border-radius:6px; font-weight:700;
            font-size:13px; background:#eef1f5; color:#333; border:1px solid #dde1e7; font-variant-numeric:tabular-nums; }
.lrd-chip-removed { background:#fdeeee; color:#c0392b; border-color:#f3c9c9; text-decoration:line-through; }
.lrd-chip-new { background:#e8f7ee; color:#1e7e34; border-color:#bfe6cc; }
.lrd-arrow { text-align:center; color:#adb5bd; font-size:13px; margin:2px 0; }
@media (max-width:480px) { .lrd-row { flex-direction:column; align-items:flex-start; } .lrd-caption { min-width:auto; } }

/* Step-card — แทน <ol><li><small>ข้อความยาว</small></li></ol> */
.mstep-list { list-style:none; padding:0; margin:0 0 1rem; }
.mstep { display:flex; align-items:flex-start; gap:.6rem; background:#fff; border:1px solid #e3e6ec;
         border-radius:8px; padding:.6rem .8rem; margin-bottom:.5rem; }
.mstep-num { flex:0 0 auto; width:24px; height:24px; border-radius:50%; background:#4e73df; color:#fff;
             display:flex; align-items:center; justify-content:center; font-weight:700; font-size:12px; margin-top:1px; }
.mstep-body { flex:1 1 auto; min-width:0; }
.mstep-title { font-weight:700; margin-bottom:.25rem; }
.mstep-facts { display:flex; flex-wrap:wrap; gap:.3rem .5rem; margin-bottom:.2rem; }
.mstep-fact { background:#f4f6f9; border:1px solid #e6e9ee; border-radius:5px; padding:1px 8px; font-size:12px; color:#495057; }
.mstep-result { font-size:12px; color:#6c757d; }
.mstep-substeps { margin:.3rem 0 .4rem 1.1rem; padding:0; font-size:12px; color:#495057; }

/* Feature card grid — แทน <p>+<ul> อธิบายส่วนประกอบหน้าจอ */
.mfeat-grid { display:grid; grid-template-columns:repeat(auto-fit, minmax(220px, 1fr)); gap:.6rem; margin-bottom:1rem; }
.mfeat-card { background:#fff; border:1px solid #e3e6ec; border-left:3px solid #4e73df; border-radius:6px; padding:.55rem .7rem; }
.mfeat-card-title { font-weight:700; font-size:13px; margin-bottom:.2rem; display:flex; align-items:center; gap:.3rem; }
.mfeat-card-desc { font-size:12px; color:#6c757d; line-height:1.5; }
```

โปรเจกต์ที่ใช้ JSP (PCMS2/SFC/PPMM2/LBMS/InspectSystem) ใช้ CSS ชุดเดียวกันนี้ได้เลย (เป็น CSS ธรรมดา
ไม่ผูกกับ Thymeleaf) — ดูตัวอย่างการใช้งานจริงที่ Dyeing repo:
`src/main/resources/templates/manual/manual.html` (`.lrd-*`, `.mstep-*`, `.mfeat-*`) และ sections
`s03-workflow.html` / `s08-notes.html` / `s09-screens.html` / `s10-role-guide.html` / `s11-faq.html`
