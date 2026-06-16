# Business Rules — [PROJECT_NAME]

> ไฟล์นี้คือ "กฎเหล็ก" — อ่านก่อนแตะ business logic ทุกครั้ง

---

## กฎที่ต้องห้ามผิด (Critical)

1. **[RULE_NAME]** — [description + เหตุผล]
   - ตัวอย่าง: เฉพาะผู้สร้างใบงานเท่านั้นที่ยกเลิกได้ก่อน approve
   - Implementation: `validateCancelPermission(user, poHId)`

2. **[RULE_NAME]** — [description]

---

## Permission Rules

| Action | Who can | Condition | Exception |
|---|---|---|---|
| [action] | [role] | [condition] | [exception] |

---

## Data Rules

| Rule | Description |
|---|---|
| [field] ห้าม null | [เหตุผล] |
| [field] ต้อง unique ต่อ [scope] | [เหตุผล] |
| [status transition] ห้ามข้าม | ต้องผ่าน [intermediate step] |

---

## Edge Cases ที่ต้องระวัง

1. **[Case]** — [description + how to handle]
   - ตัวอย่าง: Header ที่มีทุก detail CANCELLED แล้ว — ไม่ให้ cancel header อีก (return error)

2. **[Case]** — [description]

---

## กฎที่เคยสับสน (Historical)

| เรื่อง | เข้าใจผิดว่า | จริงๆ คือ |
|---|---|---|
| [topic] | [misconception] | [correct behavior] |

---

## Reference

- `docs/business/workflow.md` — status transitions
- `docs/business/roles.md` — who can do what
