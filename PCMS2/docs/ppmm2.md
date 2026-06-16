# PPMM2 — Project Reference

**Path**: `C:\RedirectDoc\94687\My Documents\GitHub\PPMM2`  
**Purpose**: Production Planning & Material Management — planning lot scheduling, SOR/ERP integration

## DB Qualifiers

ดู `applicationContext.xml` — มี datasource หลายตัว (planning DB, SOR API DB)

## TEMP_PlanningLot Structure

| Column | หมายเหตุ |
|---|---|
| `PlanUserDate` | วันที่ user drag/กำหนดเอง |
| `PlanSystemDate` | วันที่ระบบคำนวณ |
| `GroupWorkDate` | วันทำงาน group — ใช้ determine target date ใน rePlanningLot |
| `filter` | computed field — **ไม่ใช่ stored column** อย่าใส่ใน INSERT/UPDATE |
| `isValidPlan` | flag ที่ rePlanningLot() ใช้กรอง lotUserList |

## rePlanningLot() Behavior

- รับ lot list → วิ่ง planning algorithm → update `TEMP_PlanningLot`
- **`isValidPlan=false`** → lot นั้นถูก skip จาก lotUserList → วันไม่เปลี่ยน (root cause ของ replanning reset bug)
- `GroupWorkDate` record ต้องมีอยู่สำหรับ target date — ถ้าไม่มีจะ fallback หรือ error

## SOR API Integration

- `SOR_API_TOKEN` — ดึงจาก trigger/config ใน DB (ไม่ใช่ hardcode)
- แก้ trigger fix แล้ว 2026-06-04: token refresh ทำงานถูกต้องแล้ว

## Known Open Issue

- **Replanning Reset Bug** (OPEN): lot ใน templot ถูก reset กลับหลัง user drag — วันใหม่ไม่ถูก save  
  Root cause: `isValidPlan=false` ใน `rePlanningLot()` lotUserList  
  ดู `NEXT_SESSION.md` สำหรับ investigation notes

## Adding a New Feature Page

1. Entity → DAO interface + Impl → Service → Controller → JSP + JS → Sidebar
2. ดู shared pattern ใน `SKILL.md`
