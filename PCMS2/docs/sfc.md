# SFC — Project Reference

**Path**: `C:\RedirectDoc\94687\My Documents\GitHub\SFC\SFC`  
**Purpose**: Shop Floor Control — manufacturing production monitoring

## DB Qualifiers

Migration จาก single-connection `Database` → HikariCP + JdbcTemplate เสร็จสมบูรณ์แล้ว (2026-06-04)  
ดู `applicationContext.xml` สำหรับ qualifier ที่ active

## HikariCP Migration Notes (completed 2026-06-04)

- เปลี่ยนจาก singleton `Database` class มาเป็น `JdbcTemplate` + HikariCP pool ทั้งหมด
- **Pattern A** (2-arg queryList) ใช้กับ temp tables ส่วนใหญ่
- **Pattern B** (try/finally DROP) สำหรับกรณีพิเศษ
- SP calls ใช้ `DataSourceUtils.getConnection` + `releaseConnection`

## Known Bug Fixes (อย่า revert)

- **`ConfirmOp`**: เดิม `prepareStatement` ถูก close ก่อนใช้ — แก้แล้ว ให้ใช้ `ConnectionCallback`
- **`RollFromSapModel`**: NPE เมื่อ SAP return null row — เพิ่ม null check แล้ว

## Adding a New Feature Page

1. Entity → DAO interface + Impl → Service → Controller → JSP + JS → Sidebar
2. ดู shared pattern ใน `SKILL.md`
