# Git Workflow Rules

---

## Branch Strategy

| Branch | ใช้เมื่อ |
|---|---|
| `main` / `master` | production |
| `develop` | integration |
| `feature/[name]` | feature ใหม่ |
| `fix/[name]` | bug fix |
| `hotfix/[name]` | urgent production fix |

---

## Commit Convention

```
<type>: <short description>  (ภาษาไทยหรืออังกฤษก็ได้)

Types:
  feat     — feature ใหม่
  fix      — bug fix
  refactor — ปรับโครงสร้าง (behavior ไม่เปลี่ยน)
  docs     — แก้ docs เท่านั้น
  sql      — SQL patch / migration
  style    — format, naming (logic ไม่เปลี่ยน)
  test     — เพิ่ม/แก้ tests
  chore    — build config, dependencies
```

**ตัวอย่าง:**
```
feat: add purchase approve endpoint (Phase 3)
fix: fix selectpicker blank after dynamic option load
sql: add role permissions for new workflow step
refactor: extract doExecuteDetailCancel to avoid N+1 re-fetch
```

---

## Pre-commit Checklist

```
□ Build passes: mvn clean package -DskipTests (JDK 1.8)
□ Temp table มี DROP ใน Pattern A (queryList) หรือ Pattern B (finally)
□ ไม่มี PCMSSearch tables (#tempLotNoList ฯลฯ) ใน dropAllTemp
□ Batch ที่มี CREATE INDEX ใช้ SqlStatementHandler.queryList() ไม่ใช่ queryForList()
□ @Qualifier ถูก DB — pcmsDatabase / ppmmDatabase / sorDatabase / erpDatabase
□ ไม่มีไฟล์ credential / database.properties ใน staging area
□ SQL scripts อยู่ใน sql/ พร้อม date prefix + IF NOT EXISTS guard
□ ไม่มี Jackson import / ApiResponse ใน code ใหม่ (PCMS2 ใช้ Gson เท่านั้น)
```

---

## SQL Scripts

```
sql/
  YYYY-MM-DD_feature_description.sql
  YYYY-MM-DD_add_role_permissions.sql
```

- ชื่อไฟล์ขึ้นต้นด้วย date — รันตามลำดับได้
- IF NOT EXISTS guard ทุก statement
- บันทึกใน NEXT_SESSION.md ว่ารันหรือยัง

---

## ไฟล์ที่ห้าม commit

```
src/main/resources/database.properties   # credentials
*.log
target/
.classpath
.project
.settings/
```

ตรวจ `.gitignore` ก่อน commit ครั้งแรก
