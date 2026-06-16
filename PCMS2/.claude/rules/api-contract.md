# API Contract Rules

---

## Response Format

**ถ้าใช้ Jackson + ApiResponse pattern (แนะนำ — Dyeing/ระบบใหม่):**

```java
// ทุก @RestController ต้อง return ResponseEntity<ApiResponse<T>>
@GetMapping("/api/feature")
public ResponseEntity<ApiResponse<List<Foo>>> getList() {
    return ResponseEntity.ok(ApiResponse.success(fooService.findAll()));
}

// Error case
return ResponseEntity.ok(ApiResponse.fail("ไม่พบข้อมูล"));
```

```javascript
// JS check
if (res.status === 'SUCCESS') { /* use res.data */ }
else { Swal.fire('Error', res.message, 'error'); }
```

Status values: `SUCCESS` / `FAIL` / `DUPLICATE`

**ถ้าใช้ Gson pattern (PCMS2/SFC/PPMM2 — legacy):**

```java
@ResponseBody
public String getList() {
    List<Foo> list = fooService.findAll();
    return new Gson().toJson(list);
}
```

```javascript
// JS ต้อง parse เอง
var data = JSON.parse(res);
```

---

## WorkflowResult (Status Change)

ใช้สำหรับ workflow transition ที่อาจ partial-success:

```java
WorkflowResult result = poService.updatePoStatus(user, poDId, "TARGET_STATUS");
if ("ERROR".equals(result.getStatus())) {
    return ResponseEntity.ok(ApiResponse.fail(result.getMessage()));
} else if ("WARNING".equals(result.getStatus())) {
    return ResponseEntity.ok(ApiResponse.success(result.getMessage()));
}
return ResponseEntity.ok(ApiResponse.success("บันทึกสำเร็จ"));
```

---

## URL Conventions

| Pattern | ตัวอย่าง | ใช้เมื่อ |
|---|---|---|
| `GET /api/[feature]` | `/api/foo` | list |
| `GET /api/[feature]/{id}` | `/api/foo/1` | single record |
| `POST /api/[feature]` | `/api/foo` | create |
| `POST /api/[feature]/{id}` | `/api/foo/1` | update |
| `POST /api/[feature]/{id}/[action]` | `/api/foo/1/approve` | action |
| `GET /[feature]/list` | `/foo/list` | MVC page render |

---

## Exception Handling

`GlobalExceptionHandler` catches all uncaught → returns `ApiResponse.fail(message)`

Controllers ควร catch เฉพาะ business exception ที่ต้องการ message เฉพาะ:
```java
try {
    service.doSomething();
    return ResponseEntity.ok(ApiResponse.success("สำเร็จ"));
} catch (BusinessException e) {
    return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
}
// RuntimeException → GlobalExceptionHandler จัดการเอง
```

---

## Anti-patterns

- ห้าม return `String`, `Map`, `boolean`, `List` โดยตรงจาก `@RestController`
- ห้าม throw exception ออกนอก controller โดยไม่ handle (จะได้ HTTP 500 แทน JSON)
- ห้าม `new Gson().toJson()` ใน project ที่ใช้ Jackson/ApiResponse
