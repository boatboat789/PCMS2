# ทดสอบ PERF_SEARCH บน PRD (ผู้ใช้รันเอง) — คู่มือขั้นตอน

ใช้ก่อนเอา Draft ออกจาก MR !4 (`PERF_SEARCH` → `main`) · ทั้งหมด **อ่านอย่างเดียว** (harness สร้างแค่ `#temp` table ใน tempdb, scheduler ถูกตัดออกจาก context)
ทดสอบบน DEV ครบแล้ว (ดู `NEXT_SESSION.md` หัวข้อ "สรุปสถานะ PERF_SEARCH") — เอกสารนี้ปิดช่องที่ DEV ตอบไม่ได้: **ข้อมูลจริง** และ **ความเร็วจริง**

## สารบัญ
0. ก่อนเริ่ม · 1. เลือก SO จริงบน PRD · 2. รันเทียบเก่า/ใหม่ · 3. อ่านผล + เกณฑ์ผ่าน/ไม่ผ่าน · 4. ตรวจแถวที่หาย · 5. หลังจบ · 6. สิ่งที่เอกสารนี้ไม่ครอบคลุม

## 0. ก่อนเริ่ม (เช็กทีละข้อ)
- [ ] ปิด Eclipse/Tomcat ที่รัน PCMS2 จาก `target/classes` (สคริปต์รัน `mvn test` ซึ่งเขียนทับ `target/`)
- [ ] เครื่องต่อ VPN/เครือข่ายบริษัท เข้า `10.11.44.101` ได้
- [ ] อยู่บน branch `PERF_SEARCH` ที่ commit ล่าสุด (`git status` ไม่มีไฟล์ `src/` ที่ยังไม่ commit)
- [ ] **เวลา:** sync job ของ PCMS2 รัน **01:00 และ :14 / :44 ทุกชั่วโมง** และเปลี่ยนข้อมูลกลางคัน → เริ่มรันตอน **:20 หรือ :50** (หนึ่งรอบ old+new ≈ 3–4 นาที, 3 รอบ ≈ 10–12 นาที) สคริปต์จะปฏิเสธถ้าเริ่มใกล้เวลาพวกนี้
- [ ] ช่วงที่ผู้ใช้จริงใช้น้อย (ตัวเลขความเร็วบน PRD ที่ยุ่งจะเพี้ยน)
- [ ] `python` อยู่ใน PATH (`python --version`)

## 1. เลือก SO จริงบน PRD (ทำก่อนรัน)
เคสเดิมใน `src/test/resources/pcms2/regression/cases/` ใช้ค่าจาก DEV (เช่น SO 1107011323) — บน PRD **อาจไม่มีหรือไม่ตรง** ถ้าเคสให้ `rows=0` แปลว่าไม่ได้ทดสอบอะไร
รัน query นี้ใน SSMS บน PRD (`PCMS`, อ่านอย่างเดียว) เพื่อหาคู่ switch จริงที่มี sale:

```sql
SELECT s.ProductionOrder AS po, s.ProductionOrderSW AS po_sw,
       p.ProductionOrder AS po_with_sale, p.SaleOrder, p.SaleLine,
       ms.CustomerShortName, ms.CustomerName, ms.SaleStatus
FROM PCMS.dbo.SwitchProdOrder s
JOIN PCMS.dbo.FromSapMainProdSale p
  ON p.ProductionOrder IN (s.ProductionOrder, s.ProductionOrderSW) AND p.DataStatus = 'O'
LEFT JOIN PCMS.dbo.FromSapMainSale ms ON ms.SaleOrder = p.SaleOrder AND ms.SaleLine = p.SaleLine
WHERE s.DataStatus = 'O' AND s.ProductionOrder <> s.ProductionOrderSW;
```
(เมื่อนับ 2026-11-01: PRD มี switch ที่ไม่ใช่ self 30 คู่ มี sale แค่ 2 คู่ — ถ้าได้ 0–2 แถวก็ปกติ)

สำหรับแต่ละแถวที่ได้ ให้ **คัดลอก** `cases/16_single_saleorder_switch_target.json` เป็น `17_…json`, `18_…json` แล้วแก้ 4 ช่อง: `name`, `customerNameList` (= `CustomerName`), `customerShortNameList` (= `CustomerShortName`), `saleOrder` — ทุกเคสต้องมีชื่อลูกค้าทั้งสองรายการเสมอ ไม่งั้นได้ 0 แถวเงียบๆ
แล้วเช็กเคสเดิม (01–15) ว่ามีข้อมูลบน PRD ไหม — ถ้า SO/ลูกค้าไม่มี ให้ตั้ง `"enabled": false` หรือแทนค่า

ไฟล์ใหม่ใน `cases/` ไม่ต้อง commit ก่อน (สคริปต์คัดลอกไปทั้ง worktree เก่าและใหม่ให้เอง)

## 2. รัน (หนึ่งคำสั่ง)
เปิด PowerShell ที่โฟลเดอร์โปรเจกต์ (`...\GitHub\PCMS2\PCMS2`):

```powershell
powershell -ExecutionPolicy Bypass -File tools\run-harness-ab.ps1 -Target PRD -Rounds 3 -Out C:\temp\pcms2_ab_prd
```
- พิมพ์ `YES` เมื่อถูกถาม (จะโชว์ host ที่จะต่อ — ต้องเป็น `10.11.44.101` ไม่มี `\PCMSDEV`)
- สคริปต์: สร้าง worktree โค้ดเก่า (`8371211`) ที่ `C:\temp\pcms2_old` · สลับ `database.properties` เป็นของ PRD ชั่วคราว (เอาจาก `src/main/resources/`) · รัน **เก่า → ใหม่ สลับกัน 3 รอบ** · เก็บ baseline/log ที่ `-Out` · **คืน `database.properties` เป็น DEV และคืน baseline เดิมเสมอ** แม้ล้มกลางทาง (บล็อก `finally`)
- ทดลองก่อนได้บน DEV: เปลี่ยน `-Target DEV -Rounds 1 -Yes` (ผมทดสอบแล้ว 2026-11-02: ผ่าน)
- ถ้าเบรกกลางทางหรือสคริปต์ error: ตรวจ `src\test\resources\database.properties` ว่ายังชี้ `PCMSDEV` (`findstr PCMSDEV`) — ถ้าไม่ใช่ให้คัดลอกจาก `database.properties.dev.bak` ทับ

## 3. อ่านผล — ส่ง output 3 ส่วนที่สคริปต์พิมพ์ให้ผม
| ส่วน | ผ่านเมื่อ | ถ้าไม่ผ่าน |
|---|---|---|
| **CONTROL** (เก่า r1 vs เก่า r2 = โค้ดเดิมสองรอบ) | ทุกตัวเลข = 0 | ข้อมูลเปลี่ยนระหว่างรอบ (sync job/ผู้ใช้แก้) → ผลเทียบเก่า/ใหม่เชื่อไม่ได้ ให้รันใหม่ตอนเงียบกว่านี้ |
| **RESULT** (เก่า r1 vs ใหม่ r1) | `cells_changed_nonempty = 0` และ `rows_added = 0` และ `dup_count_changed = 0` | **หยุด ห้าม merge** ส่งผลให้ผม — แปลว่าค่าเดิมเปลี่ยนจริง |
| **TIMING** (median 3 รอบ) | ไม่มีเคสธง `<-- ช้าลง` และ "ใหม่/เก่า" รวม ≤ 1.20 | ส่งให้ผมดูว่าเคสไหน/เพราะอะไร |

ที่คาดว่าจะเห็น (ตาม DEV): `cells_filled` > 0 (วันที่ย้อม/CFM/GR ที่เคยว่างมีค่า) และ `rows_removed` > 0 เฉพาะแถว **Main ที่ PO อยู่ใน `SwitchProdOrder`**
⚠️ ตัวเลขความเร็วรอบเดียวแกว่งมาก (บน DEV เคสเดียวกันแกว่ง 0.47×–1.97× ระหว่างรอบ และผลรวมได้ทั้ง 1.20× กับ 0.90×) — **อย่าตัดสินจากรอบเดียว** ใช้ median ของ 3 รอบ ถ้ายังคลุมเครือให้รัน `-Rounds 5`

## 4. ตรวจแถวที่หาย (ต้องทำถ้า `rows_removed` > 0)
แถวที่หาย = Main ที่ไม่ควรโผล่ซ้ำกับแถว switch แต่ต้อง **ยืนยันกับข้อมูลจริง** ว่าทุกแถวที่สคริปต์ลิสต์ มี PO อยู่ใน `SwitchProdOrder` จริง (แทน `<PO>` ด้วยค่าที่ได้):
```sql
SELECT ProductionOrder, ProductionOrderSW, DataStatus
FROM PCMS.dbo.SwitchProdOrder
WHERE DataStatus = 'O' AND '<PO>' IN (ProductionOrder, ProductionOrderSW);
```
- ได้ ≥ 1 แถว → ถูกต้องตามกติกา `main` เดิม (anti-join ตัด PO ที่อยู่ใน SwitchProdOrder รวม self-switch)
- ได้ 0 แถว → **หยุด** แถวที่หายไม่มีเหตุผลรองรับ ส่งให้ผม
- ขอให้คนที่รู้เรื่อง switch (ฝ่ายวางแผน) ดูรายชื่อแถวที่หายอีกชั้นว่า "ไม่ควรโผล่ในรายการปกติ" ตามที่ใช้งานจริง

## 5. หลังจบ
- [ ] `findstr PCMSDEV src\test\resources\database.properties` ต้องเจอ (คืน DEV แล้ว)
- [ ] ลบ worktree เก่า: `git worktree remove --force C:\temp\pcms2_old`
- [ ] **ส่งผมหรือจดลง `NEXT_SESSION.md`**: วันเวลาที่รัน, จำนวนรอบ, ผล CONTROL/RESULT/TIMING, รายชื่อ SO ที่เพิ่มเป็นเคส 17+ (ถ้าผ่านและอยากเก็บ ให้ commit เฉพาะไฟล์ `cases/*.json` — ตรวจก่อนว่าไม่มีข้อมูลที่ไม่ควรเก็บ)
- [ ] ผ่านครบ → ตัดสินใจเอา Draft ออกจาก MR !4 · ไม่ผ่าน → อย่า merge ส่งผมวิเคราะห์

## 6. สิ่งที่เอกสารนี้ไม่ครอบคลุม (ยังค้าง)
- **หน้าเว็บบน PRD** (ตัวกรอง, Download Excel ของ Detail/Report CFM, หน้า Log) — harness ทดสอบที่ชั้น DAO ไม่ใช่ JSP/Excel · ต้อง deploy ขึ้น DEV Tomcat จริงก่อนแล้วกดดู
- Report CFM บน PRD: ตารางมีข้อมูลจริง 17,567 แถว (ตรวจ metadata แล้ว) แต่ยังไม่ได้ทดสอบหน้านี้กับข้อมูลจริง
- MR นี้ใหญ่ (41 commit, 2,379 ไฟล์ รวมลบ SAP JCo) — harness ครอบเฉพาะ query หน้า Summary/Detail ไม่ครอบส่วนอื่นของ MR
