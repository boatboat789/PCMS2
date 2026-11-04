# แผนที่ logic หน้า Detail (PCMS2) — ตีความจากโค้ด + ข้อมูลจริง · 2026-11-03

> **สถานะ:** ความเข้าใจที่ได้จากการอ่านโค้ด `db9f119` และทดลองกับข้อมูล DEV/PRD (อ่านอย่างเดียว) — ส่วนที่ "ตีความความหมายทางธุรกิจ" ติดป้าย 🟡 ต้องให้เจ้าของงานยืนยัน
> หลักการ: PERF_SEARCH คือ source of truth (user 2026-07-03) จึงไม่ตัดสินว่า main เดิมถูก/ผิด — ใช้ **ความสม่ำเสมอของตัวเอง** (ผลต้องไม่ขึ้นกับตัวกรอง) เป็นเกณฑ์

## 1) ผลหลักของรอบนี้ — ตัวเลข Prod. Qty "เปลี่ยนตามตัวกรองลูกค้า" (พิสูจน์แล้ว)
แถวเดียวกัน SO 1107010287 / line 200 / PO S1K190 (Main, ลูกค้า G TECH):

| ตัวกรองลูกค้า | Prod. Qty (`volumn`) | จำนวนเงิน |
|---|---|---|
| G TECH อย่างเดียว | **300.00** | 28,500.00 |
| G TECH + WACOAL LAMPHUN (TW/PLC) | **200.00** | 19,000.00 |

เหตุผล: PO S1K190 ผลิตให้ SO ของ G TECH 300 แต่ **พ่วง (OrderPuang) 100 ให้ SO 1107010283/100 ซึ่งเป็นของ TW/PLC** → ปริมาณที่ "ใช้ไปแล้วกับ SO อื่น" ต้องหักออกจาก Main (300−100=200)
โค้ดสร้างตารางยอดพ่วง `#tmpSumVolOP` (และสวม `#tmpSumVolRP`) ใน `PCMSSqlService.createTempForMainAndWaitLotFiltered` โดย **INNER JOIN `#tempMainSale`** (ขอบเขตการค้นหา) ด้วย SO/line **ของฝั่งที่ถูกพ่วง** → ถ้า SO ปลายทางอยู่นอกขอบเขต ยอดพ่วงจะไม่ถูกหัก
→ **ผลลัพธ์ขึ้นกับตัวกรอง** (ผิดหลักความสม่ำเสมอ ไม่ว่าจะถือแบบเดิมหรือแบบใหม่) · ที่มา: ใส่ไว้ใน `c6e6109` (2026-06-15 "backup") เป็น perf (ลดแถวที่ต้องอ่าน) ไม่พบบันทึกว่าตั้งใจให้ค่าเปลี่ยน
ขนาดบน PRD (13 เคสลูกค้า/ตัวกรอง): 151 แถว Main ที่ค่าสูงกว่าที่ควร (ไม่เคยต่ำกว่า) ·
**ทางแก้ที่น่าจะถูก (ยังไม่ได้ทำ):** ตัวกรองขอบเขตควรอยู่ที่ "PO ที่ฝั่ง Main อยู่ในขอบเขต" (`ProductionOrder` ∈ PO ของ #tempMainSale) ไม่ใช่ที่ SO ปลายทางของการพ่วง/สวม — ลดข้อมูลได้เท่าเดิมแต่ผลไม่เปลี่ยน (เหมือน `createTempSumGRFiltered` ที่กรองด้วย PO)
ผลพลอยได้ที่อธิบายได้: แถว `OrderPuang` (SUB) ที่หายบน DEV และบางแถวที่ซ้ำ ฯลฯ อาจมีต้นเหตุตระกูลเดียวกัน (กรองด้วยขอบเขต SO) — ยังไม่ได้พิสูจน์ทีละตัว

## 2) แนวคิดแถวในหน้า Detail (ผลลัพธ์ = UNION ของ 6 ชนิด)
`searchByDetail` → `#tempFinalResult` = WaitLot (เฉพาะ sale line ที่ไม่มีแถว Main) ∪ Main ∪ OP ∪ OPSW ∪ SW ∪ RP แล้ว LEFT JOIN ช่องที่ผู้ใช้กรอกเอง (Input*)

| ชนิด (`TypePrd`/`Remark`) | มาจากตาราง | ความหมายที่อ่านได้ | |
|---|---|---|---|
| **Main** | `FromSapMainSale` (DataStatus 'O') ⟕ `FromSapMainProd` | 1 แถวต่อ sale line + PO หลักของมัน · ถ้าไม่มี PO: `V…`=รับจ้างถัก · มีใบส่ง (`FromSapMainBillBatch` 'O')=Lot ขายแล้ว · SaleStatus C=ขาย stock · นอกนั้น=รอจัด Lot | 🟡 ชื่อสถานะ |
| **OrderPuang / SUB** (พ่วง) | `FromSapMainProdSale` | PO ที่ผลิตให้ SO หนึ่งแบ่งปริมาณ (Volumn) ไปให้ SO อื่น → แสดงเป็นแถว SUB ใต้ SO ปลายทาง | 🟡 |
| **Replaced / RP** (สวม) | `ReplacedProdOrder` (ผู้ใช้บันทึก) | sale line ที่ยังรอจัด Lot ถูก "สวม" ด้วย PO อื่น (`ProductionOrderRP`) ปริมาณ `Volume` (0 = ใช้ปริมาณของ PO นั้น) | 🟡 |
| **Switch / SW, OPSW** | `SwitchProdOrder` | PO ถูกสลับ (`ProductionOrder`↔`ProductionOrderSW`, ปลายทาง `SaleOrderSW/SaleLineSW`) · แถว self-switch (PO=SW) ทำให้แถว Main ของ PO นั้นถูกตัดออก (anti-join `#tempSPOAnti`) | 🟡 |
| **WaitLot** | `#tempPrepWaitLot` (สรุปต่อ sale line) | sale line ที่ยังไม่มีแถว Main แต่ปริมาณถูกพ่วง/สวมแล้ว (`พ่วงแล้วรอสวม` / `รอสวมเคยมี Lot` / `รอจัด Lot`) | 🟡 เกณฑ์ |

เงื่อนไข "ปริมาณที่นับ": ทั้งพ่วงและสวมนับเฉพาะ PO ที่ `UserStatus` อยู่ใน `viewUserStatusMappingPCMS.Special = 1` (DEV: 70 จาก 73 สถานะ)

## 3) ตัวเลข/ช่อง — คำนวณอย่างไร
| ช่อง | สูตร (โค้ดปัจจุบัน) | แหล่ง | หมายเหตุ |
|---|---|---|---|
| **Prod. Qty (Main)** `volumn` | `adjVol = PO.Volumn − SumVolRP − SumVolOP` (หักเมื่อมีค่า) เฉพาะ Grade A/ว่าง · `volumnFGAmount = Price × adjVol` | `FromSapMainProd`, `#tmpSumVolOP/RP` | ⚠️ ข้อ 1 · แถว SUB ใช้ปริมาณของ `FromSapMainProdSale` ตรงๆ |
| **GR (KG/MR/YD)** | `SUM(QuantityKG/MR/YD)` ต่อ (PO, Grade) | **`SumGRCache`** (เดิม `FromSapGoodReceive` DataStatus 'O' สด) | แคชเติมโดย `spd_SumBillAndGoodReceive` · PRD ตรงกับของสด 100% · **DEV แคชค้าง/คนละชุด** (cache 60,993 แถว vs สด 30,440 กลุ่ม) |
| **ยอดส่ง (billSend\*)** | `SUM` ต่อ (PO, SO, line, Grade) | **`SumBillCache`** (เดิม `FromSapMainBillBatch` DataStatus 'O' สด) | ⚠️ **แคชไม่ลบกลุ่มที่ใบส่งถูกยกเลิก**: PRD ค้าง 2,154 กลุ่ม (2,153 ค่าไม่ศูนย์, ใบต้นทาง `DataStatus='X'`) → 377 ชุด (PO,SO,line) ในหน้า Detail โชว์ยอดจากใบที่ยกเลิก (ตัวอย่าง: SO 1000004687 line 1 PO P6J148 110 kg) · ที่มา: `4cb4d9a` 2026-04-23 |
| **WaitLot (เกณฑ์)** | A/B/C: `SumVolMain` เทียบ `SumVolOP+SumVolRP` · แสดงเมื่อ `SumVol='B' OR countProdRP>0` | `#tmpSaleAgg` (รวมต่อ sale line) | โค้ดเดิมรวม "ต่อ PO" แล้วเทียบ · ใหม่รวม "ต่อ sale line" (`c6e6109`, 2026-06-15) → DEV: 44 → 148 แถว · 🟡 ไม่รู้ว่าเจตนาแบบไหน |

## 4) ที่ยังไม่รู้ / ต้องถาม
1. 🟡 Prod. Qty ของ Main: ยืนยันว่า **หักส่วนที่พ่วง/สวมให้ SO อื่นแล้ว** (คือค่าเดิม 200) ใช่ไหม — ถ้าใช่ ข้อ 1 คือบั๊กชัดเจน (ผลเปลี่ยนตามตัวกรอง)
2. 🟡 ยอดส่งจากใบที่ **ยกเลิก** ควรแสดงไหม (โดยเจตนา: ปกติไม่ควร) → แก้ที่ proc/แคช หรือกรองตอนอ่าน
3. 🟡 WaitLot: เกณฑ์ "ครบปริมาณแล้ว" ควรเทียบ **ต่อ sale line (รวม PO ทั้งหมด)** หรือ **ต่อ PO** (ใหม่เทียบรวม → แถวเพิ่ม 104 บน DEV; ยังไม่เคยดูบน PRD)
4. 🟡 ความหมายของ Switch/OPSW และแถวที่ควรหาย (4 แถวไม่ซ้ำบน PRD: ทุก PO มี self-switch) ยืนยันกับฝ่ายวางแผน
5. สืบต่อ: แถว SUB ที่หายบน DEV (27) และแถวซ้ำ (1 บน PRD: SO 1000003411 line 2 PO P6I000) — น่าจะต้นเหตุเดียวกับข้อ 1 (กรองด้วยขอบเขต SO) แต่ยังไม่ได้พิสูจน์
