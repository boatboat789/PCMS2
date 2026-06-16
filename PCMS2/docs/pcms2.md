# PCMS2 — Project Reference

**Path**: `C:\RedirectDoc\94687\My Documents\GitHub\PCMS2\PCMS2`  
**Purpose**: Production/sales order monitoring with SAP integration

## DB Qualifiers

ดู `applicationContext.xml` ในโปรเจกต์สำหรับ qualifier ที่ใช้จริง  
มีหลาย datasource (production DB + SAP-side DB) — **ตรวจ qualifier ทุกครั้งก่อนแตะ DAO**

## SAP Integration

- SAP data ดึงมาผ่าน stored procedures / linked server — ไม่ใช่ REST API โดยตรง
- SAP-side tables อยู่ใน DB คนละตัวกับ PCMS DB — อย่าเขียน cross-DB query โดยไม่ตรวจ qualifier

## Known Quirks

- **`PCMSSearch` temp tables** — มี lifecycle ต่างจาก temp table ทั่วไป: ห้ามรวมใน `dropAllTemp` batch  
  ดู Pattern A / Pattern B ใน `backend-patterns.md` และ drop เฉพาะ table ที่โค้ดนั้นสร้าง

## Adding a New Feature Page

1. Entity → DAO interface + Impl (`@Qualifier` ให้ถูก DB) → Service → Controller → JSP + JS → Sidebar
2. ดู shared pattern ใน `SKILL.md`
