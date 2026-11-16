# SQL ที่แก้ข้อมูลบน PRD/DEV: transaction ต้องจบในบล็อกเดียว — ห้ามทิ้งเปิดค้างรอคนกด COMMIT

กฎนี้ใช้กับ**ทุกโปรเจกต์ที่รัน SQL ด้วยมือผ่าน SSMS** (ไม่มี migration framework) — เพิ่ม 2026-11-14 หลังเหตุจริง PPMM2/SFC

## เหตุการณ์จริง (2026-11-14 — ล้างแถวเบิ้ล PPMM.dbo.RollFromSap บน PRD)

Claude ให้ user รัน `BEGIN TRAN; DELETE ...; SELECT <ตรวจ>` แล้วบอกให้ `COMMIT TRAN` ทีหลังเมื่อตัวเลขถูก
user กด COMMIT **ผิดหน้าต่าง SSMS** (แต่ละหน้าต่าง = session แยก) → หน้าต่างเดิม (SPID 122) ถือ transaction ที่ลบ 18 แถวค้างไว้ ~20 นาที
→ ล็อกแถวใน `RollFromSap` → หน้า SFC ที่ดับเบิลคลิกดูข้อมูล (`getRollDetail` อ่านตารางเดียวกัน) **ค้างทั้งหน้า** บนระบบจริง
หาเจอเพราะ `DBCC OPENTRAN('PPMM')` + `SELECT @@SPID` ไล่ทีละหน้าต่าง (user ไม่มีสิทธิ์ `VIEW SERVER STATE`)
ก่อนหน้านั้น Claude ยังเพิ่ม `IF @@TRANCOUNT > 0 ROLLBACK TRAN` ต้นสคริปต์ → **rollback งานค้างของ user เงียบๆ** (แถวที่ลบไว้กลับมา ทำให้งง) — ทั้งสองข้อเป็นความผิดของวิธีเขียนสคริปต์ ไม่ใช่ของ user

## ต้องทำ

1. **SQL ที่แก้ข้อมูล (INSERT/UPDATE/DELETE/MERGE) บน PRD ต้องเป็น "บล็อกเดียวจบ"**: BEGIN TRAN → แก้ → **ตรวจเงื่อนไขในตัว** → `COMMIT` ถ้าผ่าน / `ROLLBACK` + `THROW` ถ้าไม่ผ่าน อัตโนมัติ
   **ห้ามให้ user "ดูตัวเลขแล้วกด COMMIT เอง"** และห้ามออกแบบขั้นตอนที่ transaction ข้ามหลายครั้งที่กด Execute
2. **แบบฟอร์มมาตรฐาน** (ใช้ TRY/CATCH — SQL Server 2012+; DEV/PRD ที่ใช้ตอนนี้ 2014 รองรับ):
   ```sql
   SET XACT_ABORT ON;
   DECLARE @ok bit = 0;
   BEGIN TRY
       BEGIN TRAN;
       -- 1) สำรองข้อมูลที่จะแก้ (SELECT INTO ตารางสำรองก่อน — นอก transaction หรือในนี้ก็ได้)
       -- 2) แก้ข้อมูล
       -- 3) ตรวจ แล้ว SET @ok = 1 เฉพาะเมื่อผ่านทุกข้อ: จำนวนแถวที่แก้ = ที่คาด · ไม่มีค่าผิด · ไม่มี orphan
       IF (@ok = 1) COMMIT TRAN;
       ELSE THROW 50099, 'verify failed - rolled back', 1;
   END TRY
   BEGIN CATCH
       IF @@TRANCOUNT > 0 ROLLBACK TRAN;
       THROW;
   END CATCH;
   SELECT @@TRANCOUNT AS open_tran;   -- ต้อง 0
   ```
   ท้ายบล็อกต้องมี `SELECT @@TRANCOUNT` ให้เห็นว่าไม่เหลือ transaction ค้าง
3. **ห้ามใส่ `IF @@TRANCOUNT > 0 ROLLBACK TRAN` ต้นสคริปต์** — จะ rollback งานที่ user ทำค้างไว้โดยไม่บอก ถ้าอยากเช็คให้ **แสดงค่า** (`SELECT @@TRANCOUNT`) แล้วหยุดถ้า > 0 ให้ user ตัดสินใจเอง
4. **สคริปต์หลายขั้น (วินิจฉัย → สำรอง → แก้) แยกเป็นไฟล์/บล็อกได้ แต่ "ขั้นที่แก้" ต้องเป็นบล็อกเดียว** · ขั้นอ่านอย่างเดียวแยกได้ตามสบาย
5. **ถ้าจำเป็นต้องแยก BEGIN TRAN กับ COMMIT จริงๆ** (เช่นต้องรอคนอื่นยืนยัน) — ต้องบอก user ก่อนให้รัน `SELECT @@SPID, @@TRANCOUNT` จดเลข SPID ไว้ และต้อง COMMIT ใน**หน้าต่างเดิมเท่านั้น** + กำหนดเวลาไม่เกินกี่นาที — ค่าเริ่มต้นคือ **ห้ามแยก**
6. **หลังแก้ PRD ทุกครั้ง ให้ user ตรวจว่าไม่มี transaction ค้าง**: `DBCC OPENTRAN('<ชื่อ DB>');` (ต้องไม่เหลือ transaction) — ทำก่อนบอกว่างานเสร็จ
7. **งานที่ต้องรันนาน/เสี่ยง connection หลุด** (เห็น `Msg 121 transport-level error`): connection หลุดระหว่าง transaction → SQL Server rollback ให้เอง (ปลอดภัย) แต่ session ค้างฝั่งเซิร์ฟเวอร์อาจอยู่นานก่อนถูกเก็บ → ต้องเช็ค `DBCC OPENTRAN` หลังหลุดทุกครั้ง · แบ่งงานเป็นก้อนเล็ก (หลายบล็อกเล็กที่จบในตัว) ดีกว่าบล็อกใหญ่ครั้งเดียว

## เมื่อ "ระบบค้างหลังแก้ DB" — ตรวจ transaction ค้างก่อนอย่างอื่น (user มักไม่มี VIEW SERVER STATE)

| ลำดับ | ทำอะไร | หมายเหตุ |
|---|---|---|
| 1 | หน้าต่าง SSMS ทุกอันที่เปิดวันนี้: `SELECT @@SPID AS spid, @@TRANCOUNT AS open_tran;` | `open_tran > 0` = หน้าต่างนั้นค้าง |
| 2 | `DBCC OPENTRAN('<DB>');` | บอก SPID + เวลาเริ่มของ transaction เก่าสุด (ต้อง db_owner) |
| 3 | จับคู่ SPID จากข้อ 2 กับข้อ 1 → ในหน้าต่างนั้นดูว่าค้างอะไร (เช่นเทียบกับตารางสำรอง) → ตัดสินใจ COMMIT หรือ ROLLBACK | **ห้าม `KILL` ก่อนรู้ว่าเป็นของใคร** (อาจเป็น session ของแอปจริง) |
| 4 | ทดสอบว่าตารางไหนถูกล็อก: `SET LOCK_TIMEOUT 5000; SELECT COUNT(*) FROM <ตาราง> WHERE <PO จริง>;` | error 1222 = ถูกบล็อก · **ต้องใช้ค่าจริง ไม่ใช่ placeholder** (เคยใส่ค่าที่ไม่มีในตาราง → ได้ 0 ไม่พิสูจน์อะไร) |

## ห้ามทำ

- ❌ ให้ user รัน `BEGIN TRAN` บน PRD โดยไม่มี COMMIT/ROLLBACK อัตโนมัติในบล็อกเดียวกัน
- ❌ ใส่ rollback อัตโนมัติต้นสคริปต์ที่ไปทับงานค้างของ user
- ❌ สั่ง `KILL` session โดยไม่รู้ว่าเป็นของใคร
- ❌ บอกว่า "COMMIT แล้ว" โดยไม่ให้ยืนยันด้วย `@@TRANCOUNT = 0` / `DBCC OPENTRAN`

## ตรวจ (ห้ามเชื่อแค่ที่เขียน)

- สคริปต์ที่จะส่งให้ user รัน: ค้น `BEGIN TRAN` ทุกจุด — ต้องมี `COMMIT`/`ROLLBACK` อยู่ในบล็อกเดียวกันและครบทุกทางออก (ผ่าน/ไม่ผ่าน/error)
- ทดสอบบน DEV ก่อนด้วยวิธีเดียวกัน (PPMM2: ตัวรันสคริปต์ผ่าน JDBC + ตัวตรวจ `@ok` ใช้ได้ผลดีในเหตุนี้) แล้วค่อยส่งฉบับ PRD

⚠️ **หมายเหตุ**: ไฟล์นี้อยู่ใน template repo — โปรเจกต์อื่นต้อง copy ไปใส่ `.claude/rules/` เอง ไม่ auto-sync (เหมือน `adr-conflict-notification.md`) · 2026-11-14 copy ไปแล้ว 12 โปรเจกต์: SFC, PPMM2, PCMS2, LBMS, QCMS, InspectSystem, BGJOB, core, BGJobApi, DyeingRecord, PPT, PrepareTax
