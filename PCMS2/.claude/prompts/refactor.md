# Prompt: Refactor

---

## Template

```
Refactor: [FILE or FEATURE]
Goal: [e.g. reduce duplication, improve readability, extract method]
Preserve behavior: YES — output ต้องเหมือนเดิมทุก case
Tests: [มี/ไม่มี — วิธีตรวจว่า behavior เหมือนเดิม]
Constraints:
- อย่าเปลี่ยน public method signatures
- อย่าแตะ [FILES_TO_AVOID]
- Follow patterns ใน backend/CLAUDE.md + .claude/rules/
- ถ้าแยก class ใหม่ → ไม่เพิ่ม Spring bean ถ้าไม่จำเป็น
```

---

## Refactor Rules

1. **Preserve behavior** — output ต้องเหมือนเดิมทุก case รวม edge cases
2. **Match existing style** — ตาม indentation, naming, comment style ของ project
3. **Surgical** — แตะเฉพาะที่ต้องการ อย่า clean up code ข้างเคียง
4. **No premature abstraction** — อย่า extract interface ถ้ามีแค่ 1 implementation
5. **SQL string** — ถ้า refactor SELECT ตรวจ trailing comma หลัง field สุดท้ายเสมอ
6. **ห้ามเพิ่ม behavior** — refactor คือ restructure ไม่ใช่ add feature

---

## Common Refactor Scenarios

| Scenario | แนวทาง |
|---|---|
| Duplicate SQL WHERE clause | Extract เป็น private final String constant ใน DaoImpl |
| Duplicate frontend logic | Extract เป็น JS function ใน general.js |
| Long controller method (>50 lines) | Extract business logic ลง Service |
| Duplicate status check | ใช้ utility method ที่มีอยู่ หรือ extract ใหม่ |
| N+1 query ใน loop | Extract inner logic เป็น private method, cache result ก่อน loop |
| DAO อ่าน SecurityContext | Pass `CustomUserDetails` จาก Service แทน |
| Singleton with mutable state | เปลี่ยนเป็น ThreadLocal |

---

## Verification After Refactor

```
□ Build passes: mvn clean package -DskipTests
□ ทดสอบ golden path ด้วยมือใน browser
□ ทดสอบ edge cases (empty list, null, unauthorized)
□ ตรวจ Tomcat log ไม่มี exception ใหม่
```
