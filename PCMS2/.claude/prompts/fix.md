# Prompt: Bug Fix

---

## Template A — รู้ root cause แล้ว

```
Fix: [bug description]
Symptom: [อาการที่เห็น]
Root cause: [สาเหตุที่รู้แล้ว]
Files: [ไฟล์ที่เกี่ยวข้อง]
Constraints:
- อย่าแตะ [FILE] — [เหตุผล]
- อย่าเปลี่ยน behavior ของ [OTHER_FEATURE]
```

---

## Template B — ยังไม่รู้ root cause

```
Diagnose: [symptom description]
Symptom: [error message หรือ wrong behavior]
When: [reproduction steps]
Role: [user role ที่เจอ bug]
URL: [endpoint หรือ page]
Suspected area: [เช่น DaoImpl.search(), spring-security.xml, cache]

อ่านก่อน diagnose:
- docs/business/rules.md (business logic)
- docs/database.md (DB)
- backend/CLAUDE.md (filter/permission)
```

---

## Common Fix Patterns

| อาการ | สาเหตุที่พบบ่อย | แนวทางแก้ |
|---|---|---|
| 403 หน้าใหม่ | intercept-url ไม่ครอบ URL | เพิ่มใน spring-security.xml ก่อน catch-all |
| #temp table already exists | ไม่มี DROP ใน finally | Pattern A หรือ B |
| selectpicker blank | ไม่ได้ refresh() หลัง container visible | เรียก `.selectpicker('refresh')` |
| Cache ส่งข้อมูลผิด user | cache key ไม่มี userId | เพิ่ม userId เข้า cache key |
| Status transition ไม่เกิด | ไม่มี record ใน workflow config | เพิ่ม SQL + run ใน SSMS |
| javax.annotation compile error | JAVA_HOME ไม่ใช่ jdk-1.8 | `$env:JAVA_HOME = "C:\Program Files\Java\jdk-1.8"` |
| HikariPool timeout | DB offline / VPN ไม่ได้เชื่อม | ตรวจ database.properties, เชื่อม VPN |
| Response ไม่ใช่ JSON | throw exception ออกนอก controller | wrap ด้วย try/catch → return ApiResponse.fail() |
| workType ใหม่แสดงผิด | ลืม branch ใน list.js / form.js | ไล่ทุกจุดที่ใช้ currentWorkType |
| @PreAuthorize ไม่ทำงาน | method เป็น private หรือ same-bean call | ตรวจ proxy — Spring AOP ไม่ intercept private/self-call |

---

## Diagnostic Steps

1. **ตรวจ Network tab** — request/response status, response body
2. **ตรวจ Console log** — JS error, uncaught exception
3. **ตรวจ Tomcat log** — stack trace, SQL error
4. **ตรวจ SQL** — run ใน SSMS ด้วย parameters จริง
5. **ตรวจ docs/business/rules.md** — อาจเป็น expected behavior
