# SKILL.md — Recipes & Patterns

> ไฟล์นี้เก็บ recipes ที่ใช้ซ้ำบ่อย เพิ่มเมื่อพบ pattern ใหม่

---

## Recipe: เพิ่ม Feature Page ใหม่ (Standard)

```
1. Entity class          → src/main/java/.../entities/
2. DAO interface         → src/main/java/.../dao/
3. DaoImpl               → src/main/java/.../dao/implement/
4. Service               → src/main/java/.../service/
5. API Controller        → src/main/java/.../controller/api/
6. MVC Controller        → src/main/java/.../controller/
7. JSP (list + form)     → src/main/webapp/WEB-INF/pages/[feature]/
8. JS                    → src/main/webapp/resources/js/[feature]/
9. Sidebar entry         → layout/Sidebar.jsp
10. intercept-url        → spring-security.xml  [ถ้าใช้ Spring Security]
11. @PreAuthorize        → API methods
12. SQL (IF NOT EXISTS)  → sql/YYYY-MM-DD_[feature].sql
```

---

## Recipe: เพิ่ม Workflow Step ใหม่

```sql
-- 1. สถานะใหม่
INSERT INTO master_po_status (type, focus_by, order_no, status_code, name, color_class, ...)
-- 2. Process code
INSERT INTO master_process_code (process_code, name, ...)
-- 3. Transition
INSERT INTO po_status_workflow (from_status_id, to_status_id, ...)
-- 4. Role permission
INSERT INTO config_role_workflow_permission (role_id, step_id, ...)
```

---

## Recipe: เพิ่ม @RestController Endpoint

```java
@GetMapping("/api/[feature]")
@PreAuthorize("hasAnyRole('ADMIN','[ROLE]')")
public ResponseEntity<ApiResponse<List<Foo>>> getList() {
    List<Foo> data = service.findAll();
    return ResponseEntity.ok(ApiResponse.success(data));
}
```

---

## Recipe: AJAX POST (standard)

```javascript
$.ajax({
    url: ctx + '/api/[feature]/save',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(payload),
    success: function(res) {
        if (res.status === 'SUCCESS') {
            Swal.fire('สำเร็จ', res.message, 'success').then(() => location.reload());
        } else {
            Swal.fire('ผิดพลาด', res.message, 'error');
        }
    },
    error: handleAjaxError
});
```

---

## Recipe: Confirm Before Action (Swal2)

```javascript
Swal.fire({
    title: 'ยืนยัน?', text: 'ต้องการดำเนินการนี้ใช่ไหม',
    icon: 'warning', showCancelButton: true,
    confirmButtonText: 'ยืนยัน', cancelButtonText: 'ยกเลิก'
}).then(result => {
    if (result.isConfirmed) { /* proceed */ }
});
```

---

## Recipe: DataTables Init

```javascript
$('#myTable').DataTable({
    language: { url: ctx + '/resources/plugins/datatables/th.json' },
    order: [[0, 'desc']],
    responsive: true
});
```

---

## Recipe: selectpicker Refresh หลัง Dynamic Load

```javascript
// ต้อง refresh() ทุกครั้งหลัง container visible หรือ data เปลี่ยน
$('#mySelect').selectpicker('refresh');
// เพิ่ม option แล้ว set value
$('#mySelect').append(new Option(text, value, false, true)).selectpicker('refresh');
```

---

## Recipe: เพิ่ม DAO Method Checklist

```
□ เพิ่ม method ใน interface
□ implement ใน DaoImpl — ตรวจ @Qualifier
□ เพิ่ม method ใน Service (หรือ passthrough)
□ ตรวจ alias ใน SELECT ตรงกับ camelCase field ใน Entity
□ ตรวจ trailing comma ใน SELECT string (Java string concat)
□ ถ้ามี #temp — ใช้ Pattern A หรือ B
```

---

## Recipe: Common Fix Patterns

| อาการ | สาเหตุที่พบบ่อย |
|---|---|
| 403 หน้าใหม่ | intercept-url ใน spring-security.xml ไม่ครอบ URL |
| #temp table already exists | ไม่มี DROP ใน finally — ใช้ Pattern A/B |
| #temp ค้างแม้มี DROP (concurrent load) | DROP กับ main SQL วิ่งบน connection คนละตัว — ต้องย้าย DROP เข้าใน `StatementCallback` (`stmt.execute(dropSql)`) |
| `jdbc.update()` คืน 0 ทั้งที่ row update จริง | `SET NOCOUNT ON` ติดค้างที่ connection session — ต้อง `SET NOCOUNT OFF` ใน finally ก่อน release connection |
| SimpleDateFormat: corrupt date ตอน concurrent | `SimpleDateFormat` instance field ใน singleton — ต้อง `ThreadLocal<SimpleDateFormat>` ทั้ง static และ instance field |
| selectpicker blank | ไม่ได้ refresh() หลัง container visible |
| Cache ส่งข้อมูลผิด user | cache key ไม่มี userId |
| Status transition ไม่เกิด | ไม่มี record ใน workflow transition table |
| javax.annotation compile error | JAVA_HOME ไม่ใช่ jdk-1.8 |
| HikariPool timeout | DB offline หรือ firewall / VPN |
