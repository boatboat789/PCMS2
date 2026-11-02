# Roles — [PROJECT_NAME]

---

## Role Registry

| Role Code | ชื่อ | Level | คำอธิบาย |
|---|---|---|---|
| `ADMIN` | ผู้ดูแลระบบ | 1000 | เห็นและทำได้ทุกอย่าง |
| `[ROLE_CODE]` | [ชื่อ] | [level] | [description] |

> Level ใช้สำหรับ role-based filter — สูงกว่า = เห็นข้อมูลมากกว่า

---

## Visibility Matrix

| หน้า / Feature | ADMIN | [ROLE_A] | [ROLE_B] | หมายเหตุ |
|---|---|---|---|---|
| [page/feature] | ✅ | ✅ | ❌ | [condition] |

---

## Permission Matrix (action-level)

| Action | ADMIN | [ROLE_A] | [ROLE_B] | Condition |
|---|---|---|---|---|
| ดูรายการ | ✅ | ✅ | ✅ | — |
| สร้างใหม่ | ✅ | ✅ | ❌ | [condition] |
| แก้ไข | ✅ | ✅ | ❌ | เฉพาะที่ตัวเองสร้าง |
| ลบ / ยกเลิก | ✅ | ❌ | ❌ | [condition] |
| อนุมัติ | ✅ | ✅ | ❌ | [condition] |

---

## Role Groups (ถ้ามี)

```
Business Family:  BUSINESS, HEAD_BUSINESS
Purchase Family:  PURCHASE, HEAD_PURCHASE
Factory Family:   FACTORY_DYE, HEAD_FACTORY_DYE
```

---

## Spring Security Role Mapping

```
DB role_code   →   Spring role (ROLE_ prefix)
ADMIN          →   ROLE_ADMIN
[ROLE_CODE]    →   ROLE_[ROLE_CODE]
```

intercept-url ใช้ `hasRole('ROLE_ADMIN')` หรือ `hasAnyRole('ADMIN','MANAGER')` (Spring ลบ ROLE_ ให้อัตโนมัติ)
