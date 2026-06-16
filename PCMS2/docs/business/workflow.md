# Workflow — [PROJECT_NAME]

---

## Status Machine

```
[INIT] ──create──► [STATUS_A] ──approve──► [STATUS_B] ──complete──► [STATUS_C]
                       │                                                  │
                    cancel                                            [CANCELLED]
                       ▼
                  [CANCELLED]
```

---

## Status Registry

| status_code | name | order_no | color_class | คำอธิบาย |
|---|---|---|---|---|
| `INIT` | รอดำเนินการ | 10 | bg-gray | สถานะเริ่มต้น |
| `[STATUS_CODE]` | [ชื่อ] | [N] | [bg-*] | [description] |
| `CANCELLED` | ยกเลิก | 999 | bg-slate-07 | ยกเลิกแล้ว |

> `order_no` ใช้สำหรับ sort และ filter สถานะ — ต่ำกว่า = ก่อนกว่าใน flow

---

## Transitions (config_po_workflow)

| from_status | to_status | action | responsible_role | condition |
|---|---|---|---|---|
| `STATUS_A` | `STATUS_B` | approve | [ROLE] | [condition] |
| `STATUS_A` | `CANCELLED` | cancel | [ROLE] | [condition] |

---

## Process Codes (po_process_tracking)

| process_code | ชื่อ | trigger_by | คำอธิบาย |
|---|---|---|---|
| `MS_[NAME]` | [milestone name] | [action] | บันทึกเมื่อ [event] |
| `PC_[NAME]` | [process name] | [action] | บันทึกเมื่อ [event] |

> `MS_` = milestone (once per item)  
> `PC_` = process (อาจ upsert หลายครั้ง)

---

## Workflow Rules

- Transition ต้องมี record ใน `config_po_workflow` — ไม่อย่างนั้น reject ทุก case
- ห้าม hardcode status_id ใน Java — ใช้ `statusProvider.getIdByCode("STATUS_CODE")`
- Cancel ต้อง filter out รายการที่ CANCELLED ไปแล้วก่อน — อย่า re-cancel

---

## workType Routing (ถ้ามี list หลายหน้า)

| workType | หน้า | ข้อมูลที่เห็น |
|---|---|---|
| `[workType]` | `/[feature]/list` | [filter description] |
