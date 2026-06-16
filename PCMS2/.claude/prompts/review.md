# Prompt: Code Review

---

## Template

```
Review [FILE_OR_FEATURE] โดยตรวจตามหัวข้อนี้:

Context:
- Feature: [description]
- Files changed: [list]
- Related docs: [e.g. docs/business/rules.md]

Critical (ต้องแก้ก่อน commit):
1. Security — @PreAuthorize ครอบทุก API? intercept-url เพิ่มแล้ว?
2. Data integrity — @Qualifier ถูก? Temp table DROP ใน finally?
3. Business rules — ตรงกับ docs/business/rules.md?
4. Status transition — hardcode status ID? ใช้ statusProvider.getIdByCode()?

Major (ควรแก้):
5. ApiResponse pattern — @RestController คืน ApiResponse<T>?
6. Cache key — มี userId ใน key ถ้า cache per-user?
7. Temp table — DROP ทุก path?

Minor:
8. Naming — column alias ตรงกับ camelCase field ใน Entity?
9. No trailing comma ใน SQL SELECT string สุดท้าย
10. selectpicker refresh() ครบ?
```

---

## Quick Review Checklist

```
□ ห้าม JPA/Hibernate — JdbcTemplate เท่านั้น
□ ห้าม hardcode status ID
□ ตรวจ @Qualifier ทุก DAO
□ ตรวจ intercept-url  [Spring Security]
□ ApiResponse<T> สำหรับ @RestController  [Jackson project]
□ Temp table DROP ใน finally
□ ห้าม DriverManager.getConnection()
□ ห้าม log sensitive data
□ Parameterized queries — ห้าม string concat ใน SQL
□ ThreadLocal สำหรับ SimpleDateFormat ใน singleton
```

---

## Angle-based Review (ลึกขึ้น)

เมื่อต้องการ thorough review:

1. **Correctness** — logic ถูกต้องทุก case? edge cases?
2. **Removed behavior** — สิ่งที่ลบออกมี invariant อะไร ยังครอบอยู่ไหม?
3. **Cross-file** — callers ของ method ที่เปลี่ยน มี breaking change?
4. **Reuse** — code ซ้ำกับ utility อื่นที่มีอยู่แล้ว?
5. **Simplification** — มี complexity ที่ไม่จำเป็น?
6. **Performance** — N+1 query? correlated subquery? repeated computation?
7. **Architecture** — แก้ถูกชั้นหรือเปล่า? (DAO ไม่ควรรู้เรื่อง Security)
