# หน้า Detail/Summary มี "กี่แบบ อะไรบ้าง" — จัดประเภทครบ (ตีความจากโค้ด db9f119 + ข้อมูลจริง) · 2026-11-03

> เป้าหมาย: ทำความเข้าใจทั้งระบบก่อนแก้อะไร · ที่มา: อ่านโค้ด (`PCMSDetailDaoImpl`, `PCMSMainDaoImpl`, `PCMSSqlService`) + นับข้อมูล DEV/PRD (SELECT อย่างเดียว) + ผลลัพธ์ 13 เคสบน PRD
> 🟡 = **ความหมายทางธุรกิจที่ตีความจากโค้ด ต้องให้ผู้ใช้/ฝ่ายวางแผนยืนยัน** · ไม่ติดป้าย = อ่านได้ตรงจากโค้ด/ข้อมูล
> เอกสารคู่กัน: `logic-map-detail-2026-11-03.md` (ข้อค้นพบ Prod. Qty เปลี่ยนตามตัวกรอง)

## สรุปคำตอบ "กี่แบบ"
| มิติ | จำนวน | รายการ |
|---|---|---|
| **A. ชนิดแถวในตาราง** (UNION ของชุดข้อมูล) | **6 ชุด** แต่ป้ายที่เห็นมี **6 คู่ (TypePrd/Remark)** และ **OP กับ OPSW ใช้ป้ายเดียวกัน** (`OrderPuang/SUB`) แยกจากหน้าจอไม่ได้ | Main · OP(พ่วง) · OPSW(พ่วง+switch) · SW(switch) · RP(สวม) · WaitLot |
| **B. แถว Main เมื่อ "ไม่มี PO"** | **4 แบบ** (+ แบบปกติที่มี PO = 5 แบบของแถว Main) | รับจ้างถัก · Lot ขายแล้ว · ขาย stock · รอจัด Lot |
| **C. แบบของ Switch** | **3 แบบ** | self-switch (`MAIN`) · switch ข้าม SO (`SUB`) · OPSW |
| **D. WaitLot** | **3 ป้าย** ใน LotNo × สถานะปริมาณ **A/B/C** (แสดงเฉพาะ B หรือมีสวม) | พ่วงแล้วรอสวม · รอสวมเคยมี Lot · รอจัด Lot |
| **E. สูตร Prod. Qty** | **5 สูตร** ตามชนิดแถว | หัวข้อ 5 |
| **F. แหล่งตัวเลขที่เป็นแคช** | **2 แคช** | `SumGRCache`, `SumBillCache` |
| **G. แถวย่อย (drill-down)** | **7 เมธอด** | หัวข้อ 7 |
| **H. ช่องที่ผู้ใช้กรอก** | **10 ช่อง** (7+ ตาราง) | หัวข้อ 8 |
| **I. หน้าที่ใช้ชุดเดียวกัน** | **2 หน้า** | Summary (`PCMSMainDaoImpl`) ใช้ 6 ชุดเดียวกันย่อคอลัมน์ · Detail (`PCMSDetailDaoImpl`) เต็มคอลัมน์ |

## ขนาดข้อมูลต้นทาง (SELECT, 2026-11-03)
| | DEV | PRD |
|---|---|---|
| sale line (DataStatus O) | 17,827 | **37,686** |
| `FromSapMainProd` (O) | 33,436 | 67,788 |
| sale line ที่มี PO | 13,257 | 29,160 |
| — ไม่มี PO แบบ **รับจ้างถัก** (`V%`) | 75 | 145 |
| — ไม่มี PO แบบ **Lot ขายแล้ว** (มีใบส่ง) | 2,732 | 3,403 |
| — ไม่มี PO แบบ **ขาย stock** (SaleStatus C) | 937 | 4,583 |
| — ไม่มี PO แบบ **รอจัด Lot** | 826 | 395 |
| `FromSapMainProdSale` พ่วง (O) | 4,068 (ข้าม SO จริง 4,006) | 6,978 (ข้าม SO จริง 6,865) |
| `ReplacedProdOrder` สวม (O) | **0** | **7,846** |
| `SwitchProdOrder` (O) รวม / self / ข้าม | 34 / 17 / 17 | 60 / 30 / 30 |

⚠️ **DEV ไม่มีข้อมูล "สวม" เลย (0 แถว) ขณะที่ PRD มี 7,846** → ชุด RP และแถว WaitLot แบบ "รอสวมเคยมี Lot" ทดสอบบน DEV ไม่ได้ — เหตุผลที่บั๊ก WaitLot รอบก่อนไม่เห็นบน DEV และเป็นช่องโหว่ใหญ่ที่สุดของการทดสอบบน DEV
พ่วงส่วนใหญ่ (98%) เป็นการพ่วง **ข้าม SO** จริง ดังนั้นปัญหา "ปริมาณหักพ่วงขึ้นกับตัวกรอง" มีโอกาสกระทบมาก

## 1) ชนิดแถว (A) — ผลลัพธ์ = WaitLot(ที่ไม่มี Main) ∪ Main ∪ OP ∪ OPSW ∪ SW ∪ RP
จำนวนจริงใน 13 เคสบน PRD (ไม่ซ้ำ 12,586 แถว): **Main/Main 11,515 · OrderPuang/SUB 663 (OP+OPSW) · Replaced/SUB 340 · WaitLot 55 · Switch/SUB 7 · Switch/MAIN 6**

| ชุด | แถวมาจาก (ตัวขับ) | ตารางต้นทาง | ป้าย | ปริมาณที่แสดง |
|---|---|---|---|---|
| **Main** | **sale line ในขอบเขตค้นหา** (`#tempMainSale`) ⟕ PO หลัก | `FromSapMainSale`(O) ⟕ `FromSapMainProd` | `Main/Main` | `adjVol` = Volumn − ยอดสวม − ยอดพ่วง (Grade A/ว่าง) |
| **OP (พ่วง)** | **sale line ปลายทางที่ถูกพ่วง** (ในขอบเขต) ⨝ แถวพ่วง | `FromSapMainProdSale`(O) ตรง SO/line | `OrderPuang/SUB` | `FromSapMainProdSale.Volumn` · ต้องสถานะ Special=1 · ตัด PO ที่อยู่ใน SwitchProdOrder |
| **OPSW** | sale line ปลายทางของพ่วงที่ **PO อยู่ใน switch** | `#tempSPOSale` | `OrderPuang/SUB` | Volumn จาก ProdSale |
| **SW (switch)** | sale line **ปลายทาง** `SaleOrderSW/SaleLineSW` | `SwitchProdOrder`(O) | `Switch/MAIN` ถ้า PO=PO_SW (self) ไม่งั้น `Switch/SUB` | `Volumn_PO − SumVol(พ่วงของ PO)` |
| **RP (สวม)** | sale line ที่ **ผู้ใช้บันทึกสวม** ด้วย PO อื่น | `ReplacedProdOrder`(O) ⨝ `FromSapMainProd` (Special=1) | `Replaced/SUB` | `ReplacedProdOrder.Volume` (0 = ใช้ Volumn ของ PO) |
| **WaitLot** | sale line ที่ **ไม่มีแถว Main** แต่มีพ่วง/สวม (หัวข้อ 4) | `#tempPrepWaitLot` | `WaitLot/WaitLot` | ไม่มี |

ข้อสังเกต: ทุกชุดเริ่มจาก **sale line ในขอบเขตค้นหา** ดังนั้น "แถวที่เห็น" ขึ้นกับตัวกรองได้ (ถูกต้อง) แต่ **ค่าในแถวต้องไม่ขึ้นกับตัวกรอง** — ข้อค้นพบ Prod. Qty ละเมิดข้อนี้

## 2) แถว Main เมื่อไม่มี PO (B) — ลำดับตัดสินใน `CASE` (ข้อแรกที่ตรงชนะ)
| ลำดับ | เงื่อนไข | แสดงเป็น | DEV | PRD |
|---|---|---|---|---|
| 0 | มี PO ใน `FromSapMainProd` | PO จริง | 13,257 | 29,160 |
| 1 | `MaterialNo LIKE 'V%'` | **รับจ้างถัก** 🟡 | 75 | 145 |
| 2 | มีใบส่ง `FromSapMainBillBatch` 'O' ของ SO/line | **Lot ขายแล้ว** 🟡 | 2,732 | 3,403 |
| 3 | `SaleStatus = 'C'` | **ขาย stock** 🟡 | 937 | 4,583 |
| 4 | นอกนั้น | **รอจัด Lot** | 826 | 395 |

ข้อสังเกต: PRD มี "ขาย stock" 4,583 แถว ซึ่งมากกว่า DEV 5 เท่า (ส่วนใหญ่ SaleStatus C) — หน้า Detail บน PRD จะเห็นแถวชนิดนี้เยอะ ต่างจาก DEV

## 3) Switch (C) — 3 แบบ
- **self-switch** (`ProductionOrder = ProductionOrderSW`): DEV 17 / PRD 30 · แถว Main ของ PO นั้น **ถูกตัดออก** (anti-join `#tempSPOAnti`) และแสดงเป็น `Switch/MAIN` ที่ sale ปลายทาง 🟡
- **switch ข้าม** (PO ≠ PO_SW): DEV 17 / PRD 30 · แสดง `Switch/SUB` · ใช้แมป PO ใน `#tempSPO`/`#tempSPOSale`
- **OPSW**: PO ที่ switch แล้วมีแถวพ่วง → แสดงที่ SO ปลายทางของพ่วง · คู่ที่มี sale จริง: DEV 1 คู่ / PRD 2 คู่ (SO 1109009155, 1000003047)

## 4) WaitLot (D)
- ป้าย LotNo (3): `พ่วงแล้วรอสวม` (ยอดพ่วง>0) · `รอสวมเคยมี Lot` (ยอดสวม>0) · `รอจัด Lot` (ที่เหลือ)
- สถานะปริมาณ (3): `A` = ปริมาณ Main รวม > ใช้ไปแล้ว (พ่วง+สวม) · `B` = ≤ · `C` = ไม่ตรงทั้งสอง
- แสดงแถวเมื่อ sale line `SumVolMain > 0` (หรือไม่มี Main แต่มีสวม) **และ** `SumVol='B'` หรือมีสวม (`countProdRP>0`)
- ⚠️ ต่างจากโค้ด main เก่า: เก่ารวมปริมาณ "ต่อ PO" แล้วเทียบ · ปัจจุบันรวม "ต่อ sale line" (`#tmpSaleAgg`, `c6e6109` 2026-06-15) → DEV 44 → 148 แถว 🟡 ยังไม่เคยดูบน PRD ซึ่งมีสวม 7,846 แถว
- ถูกเรียกอีกจุด: หลังผู้ใช้บันทึก Replaced PO (`PCMSDetailProcess`) → `getWaitLotCaseBySaleOrder` รีเฟรชแถว

## 5) สูตร Prod. Qty (E) — 5 สูตร
| ชนิด | สูตร |
|---|---|
| Main | `Volumn_PO − SumVolRP − SumVolOP` (หักเฉพาะที่มีค่า; PO ต้อง Special=1) |
| OP / OPSW | `FromSapMainProdSale.Volumn` (0 ถ้าไม่มี) |
| SW | `FromSapMainProd.Volumn − SumVol` (SumVol = พ่วงของ PO จาก `#tempSPOSale`) |
| RP | `ReplacedProdOrder.Volume` (0 → Volumn ของ PO) |
| WaitLot | ไม่มีปริมาณ |

จำนวนเงิน = `Price × ปริมาณ` เฉพาะ Grade A/ว่าง (อื่นๆ = NULL) · "Special" = `viewUserStatusMappingPCMS.Special = 1` (DEV 70 จาก 73 สถานะ) มีผลทั้งการหักปริมาณและการแสดงแถว OP/RP

## 6) แหล่งตัวเลข / แคช (F)
| ตัวเลข | แหล่ง (ปัจจุบัน) | สด (main เก่า) | สถานะ |
|---|---|---|---|
| GR (KG/MR/YD) | `SumGRCache` ต่อ (PO, Grade) | `FromSapGoodReceive` 'O' | PRD ตรง 100% (64,303 กลุ่ม ต่าง 0) · DEV ไม่ตรง (แคชค้าง) |
| ยอดส่ง (BillSend\*) | `SumBillCache` ต่อ (PO,SO,line,Grade) | `FromSapMainBillBatch` 'O' | ⚠️ **PRD ค้าง 2,154 กลุ่มของใบ 'X' (ยกเลิก)** → 377 ชุดในหน้า Detail โชว์ยอดจากใบยกเลิก |
| วันที่ผลิต | `TEMP_ProdWorkDate` | — | กรองด้วย PO (ปลอดภัยต่อตัวกรอง) |
| ยอดพ่วง/สวมที่หัก | `#tmpSumVolOP/RP` | ตารางสด | ⚠️ กรองด้วย SO ปลายทาง → ผลขึ้นกับตัวกรอง |

แคชเติมโดย `spd_SumBillAndGoodReceive` (job `ERP_SYNC_JOB` ที่ :14/:44) · ที่มา `4cb4d9a` (2026-04-23)

## 7) แถวย่อย/drill-down (G) — เมธอดใน `PCMSDetailDao`
`getNormalCaseByProdOrder` (โหมดปกติ + โหมด RP) · `getReplacedCaseByProdOrder` · `getSwitchProdOrderListByPrd` · `getOrderPuangListByPrd` · `getOrderPuangSWListByPrd` · `getWaitLotCaseBySaleOrder` — แต่ละตัวสร้างตารางย่อยของตัวเอง (จุดที่ harness เดิมครอบไม่ครบ: WaitLot, RP)

## 8) ช่องที่ผู้ใช้กรอกในตาราง (H) — 10 ช่อง (คอลัมน์ 31–51 ของตารางกว้าง 7,634px)
| ช่อง | เก็บใน | กุญแจ |
|---|---|---|
| Plan CFM LAB (วันที่) | `PlanCFMLabDate` | 🟡 |
| Send CFM Cus Date (วันที่) | ผ่าน `saveInputDate` | 🟡 |
| Plan Delivery Date (วันที่) | `PlanDeliveryDate` | PO+SO+line |
| Cause of Delay | `InputCauseOfDelay` | **PO** |
| Delayed Department | `InputDelayedDep` | **PO** |
| PC Remark | `InputPCRemark` | PO+SO+line |
| Replaced Remark | `InputReplacedRemark` | PO+SO+line |
| Switch Remark | `InputSwitchRemark` | **PO** |
| Stock Remark | ตารางหมายเหตุ stock | 🟡 |
| Stock Load | `InputStockLoad` | PO+SO+line |

บันทึกทีละช่องตอน Enter/blur ผ่าน `handlerInputField` + ตัวกัน `check1` · ช่องกุญแจ **PO** ใช้ค่าร่วมกันทุกแถวของ PO นั้น (หลาย SO) 🟡

## 9) จุดที่ "ผลขึ้นกับตัวกรอง" ที่รู้
| จุด | สถานะ |
|---|---|
| `#tmpSumVolOP/RP` กรองด้วย SO ปลายทาง (`createTempForMainAndWaitLotFiltered`) | ✅ พิสูจน์แล้ว: Prod. Qty 300 ↔ 200 ตามตัวกรองลูกค้า (DEV) · PRD 151 แถว |
| `#tempSumGR`, `#tempProdWorkDate` กรองด้วย PO | ✅ ไม่พบค่าเปลี่ยน (ระดับ harness) |
| `#tempInput*` (RR/PCR/SL) กรองด้วย SO/line `INNER JOIN #tempMainSale` | ⬜ ยังไม่ตรวจ — ช่องของแถว SUB ที่ SO ปลายทางอยู่นอกขอบเขตอาจหาย |
| แถว OP (SUB) ที่หายบน DEV 27 แถว | ⬜ ยังไม่รู้สาเหตุ |
| แถวซ้ำ 1 แถวบน PRD (SO 1000003411 line 2 PO P6I000) | ⬜ ยังไม่ตรวจ |

## 10) คำถามที่ต้องให้เจ้าของงานยืนยัน (🟡)
1. Prod. Qty ของ Main ควรหักส่วนพ่วง/สวมให้ SO อื่นเสมอ และไม่ขึ้นกับตัวกรอง ใช่ไหม
2. ยอดส่งจากใบ **ยกเลิก** ควรหายออกใช่ไหม
3. WaitLot: เทียบปริมาณ "ต่อ sale line" หรือ "ต่อ PO" (PRD มีสวม 7,846 แถว ผลต่างอาจมากกว่า DEV)
4. OP กับ OPSW ใช้ป้ายเดียวกัน — ตั้งใจให้แยกไม่ออกหรือไม่
5. ลำดับสถานะ Main ไม่มี PO (รับจ้างถัก → Lot ขายแล้ว → ขาย stock → รอจัด Lot) ถูกต้องไหม และ "Lot ขายแล้ว" (มีใบส่งแต่ไม่มี PO) หมายถึงอะไรในทางธุรกิจ
6. ช่องกรอกที่กุญแจเป็น PO (Cause of Delay / Delayed Dep / Switch Remark) ใช้ร่วมกันทุกแถวของ PO ถูกต้องไหม
