# Input Validation Rules — ทุกโปรเจกต์

กฎนี้ใช้กับทุกโปรเจกต์ใน workspace — ป้องกันปัญหาที่เจอจริงใน Dyeing (2026-07-16): user พิมพ์ข้อความยาวเกินที่ DB
รองรับ (เช่น `nvarchar(10)`) แล้วกด save → เจอ SQL error ดิบๆ ("String or binary data would be truncated")
โผล่ใส่หน้าจอผู้ใช้ตรงๆ แทนที่จะเตือนเป็นภาษาคนตั้งแต่ตอนพิมพ์

## ต้องทำ — ทุกครั้งที่เพิ่ม field/column ใหม่ที่ผู้ใช้พิมพ์เอง (free text)

1. **เช็คความยาวคอลัมน์จริงใน DB ก่อนเสมอ** — อย่าเดาหรือลอกจาก field อื่นที่ "น่าจะคล้ายกัน"
   ```sql
   SELECT c.name, t.name AS data_type,
     CASE WHEN t.name IN ('nvarchar','nchar') AND c.max_length <> -1 THEN c.max_length/2
          ELSE c.max_length END AS max_chars
   FROM sys.columns c
   JOIN sys.types t ON c.user_type_id = t.user_type_id
   WHERE c.object_id = OBJECT_ID('ชื่อตาราง');
   ```
   ⚠️ **`nvarchar`/`nchar` เก็บ `max_length` เป็นไบต์ (2 ไบต์/ตัวอักษร) ต้องหาร 2 เพื่อได้จำนวนตัวอักษรจริง** —
   `varchar`/`char` ไม่ต้องหาร (1 ไบต์/ตัวอักษร) — คอลัมน์ `nvarchar(MAX)`/`text` (`max_length = -1`) ไม่มี
   ขีดจำกัดจริง ไม่ต้องใส่ maxlength

2. **ใส่ `maxlength="N"` ให้ตรงกับคอลัมน์จริงทุกช่อง** — ทั้ง `<input>`/`<textarea>` แบบ static ใน
   HTML/JSP/Thymeleaf และแบบ render จาก JS (template string ใน DataTables column, modal ที่ inject ผ่าน
   `html:` string) ให้ครบทุกช่องในหน้าเดียวกัน — เจอบั๊กจริงว่า field พี่น้องกัน (เช่น สีเดิม/สีใหม่ ในตาราง
   เดียวกัน) มีแค่บาง column ที่ใส่ไว้ อีก column ไม่มี ทั้งที่ยาวเท่ากัน

3. **field ที่ render จาก JS แบบ dynamic** (DataTables column, table row builder) — เก็บความยาวสูงสุดเป็น
   property ของ column definition (เช่น `maxLen: 10`) แล้วให้ฟังก์ชัน render อ่านค่ามาใส่ dynamic แทนการ
   hardcode ตาม field-type เดียว (กันเผลอใช้ maxlength เดียวกันกับทุก field ที่ type เหมือนกันแต่ยาวไม่เท่ากันจริง)

4. **SweetAlert2 / popup ที่มี input/textarea กรอกเหตุผล-หมายเหตุ** — ใส่ `inputAttributes: { maxlength: N }`
   (เมื่อใช้ `input: 'textarea'`) หรือ `maxlength="N"` ตรงๆ ใน `<textarea>` ที่ inject ผ่าน `html:` string

5. **ทดสอบจริง ไม่ใช่แค่ดูโค้ด** — พิมพ์ค่ายาวเกิน limit จริงในเบราว์เซอร์แล้วเช็คว่า (a) พิมพ์ต่อไม่ได้ตั้งแต่
   ตัวอักษรที่เกิน (ผลจาก `maxlength` attribute) (b) กด save แล้วไม่มี SQL error ดิบโผล่ ถ้าจำเป็นต้องมี
   validation message เพิ่มเติม ต้องเป็นภาษาคนอ่านเข้าใจ

## ห้ามทำ

- ห้ามปล่อย `<input type="text">`/`<textarea>` ที่ผูกกับ DB column แบบไม่มี `maxlength` โดยไม่ query ความยาว
  จริงก่อน
- ห้ามใช้ maxlength ของ field อื่นที่ "น่าจะคล้ายกัน" โดยไม่ยืนยันด้วย query — field ชื่อคล้ายกันความยาวคอลัมน์
  ไม่จำเป็นต้องเท่ากัน
- ห้ามพึ่ง client-side `maxlength` เป็นเกราะป้องกันเดียวสำหรับ field ที่มีความเสี่ยงด้าน security/ธุรกิจสูง —
  ยังต้องมี server-side guard เป็น defense-in-depth ตามปกติ (`maxlength` เป็นแค่ UX กัน error ไม่ใช่ security
  control)

## บทเรียนจริง (Dyeing, 2026-07-16)

ตรวจ column ทั้ง schema (`sys.columns`) เทียบกับทุก input/textarea ในระบบทั้งแอป พบว่าหน้า setting
(master-data CRUD) ส่วนใหญ่มี `maxlength` ถูกต้องอยู่แล้วตั้งแต่แรก แต่ยังพบอีก ~15 จุดกระจายอยู่ในหน้า
transaction หลัก (detail-form, process-form, header-inline table, bulk-cancel remark) ที่ไม่มี `maxlength`
เลยสักช่อง — เดาว่าเป็น field ที่เพิ่มเข้ามาทีหลังโดยไม่ได้ผ่าน checklist นี้ ตรวจให้ครบทุกจุดเมื่อมีการเพิ่ม
field ใหม่ อย่ารอให้ user เจอ error ก่อนแล้วค่อยไล่แก้ทีละจุด
