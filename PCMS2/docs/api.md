# API Design — [PROJECT_NAME]

---

## Response Wrapper

**[Jackson + ApiResponse pattern — แนะนำสำหรับโปรเจคใหม่]**

```java
// ทุก @RestController return ResponseEntity<ApiResponse<T>>
@GetMapping("/api/feature")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public ResponseEntity<ApiResponse<List<FooDto>>> getList() {
    return ResponseEntity.ok(ApiResponse.success(service.findAll()));
}

@PostMapping("/api/feature/save")
public ResponseEntity<ApiResponse<Boolean>> save(@RequestBody @Valid FooDto dto,
        @AuthenticationPrincipal CustomUserDetails user) {
    service.save(user.getUser(), dto);
    return ResponseEntity.ok(ApiResponse.success(true, "บันทึกสำเร็จ"));
}
```

```javascript
// JS: ไม่ต้อง JSON.parse — Jackson serialize ให้แล้ว
$.ajax({ contentType: 'application/json', ... })
success: function(res) {
    if (res.status === 'SUCCESS') { /* use res.data */ }
    else { Swal.fire('Error', res.message, 'error'); }
}
```

**[Gson pattern — legacy PCMS2/SFC/PPMM2]**

```java
@ResponseBody
public String getList() {
    return new Gson().toJson(service.findAll());
}
```

```javascript
// JS: ต้อง parse เอง
var data = JSON.parse(res);
if (data.status === 'success') { ... }
```

---

## WorkflowResult (Status Change)

```java
WorkflowResult result = service.updateStatus(user, id, "TARGET_STATUS");
if ("ERROR".equals(result.getStatus())) {
    return ResponseEntity.ok(ApiResponse.fail(result.getMessage()));
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

## MVC Controller (Page Render)

```java
@Controller
@RequestMapping("/feature")
public class FeatureController extends BaseController {

    @GetMapping("/list")
    public ModelAndView list(@AuthenticationPrincipal CustomUserDetails user) {
        return buildView("Feature List", "featureList",
                "pages/feature/list",
                "js/feature/list.js");
    }
}
```

---

## Exception Handling

`GlobalExceptionHandler` catches all uncaught → `ApiResponse.fail(message)` (ไม่ expose stack trace)

```java
// Controller — catch business exception เฉพาะที่ต้อง message เฉพาะ
try {
    service.doSomething();
    return ResponseEntity.ok(ApiResponse.success("สำเร็จ"));
} catch (BusinessException e) {
    return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
}
// RuntimeException → GlobalExceptionHandler จัดการ → ไม่ได้ HTTP 500
```

---

## Adding a New Setting Page (8 steps)

```
1.  SQL patch     → sql/YYYY-MM-DD_[feature].sql
2.  Entity        → entities/[Feature].java
3.  DAO interface → dao/[Feature]Dao.java
4.  DaoImpl       → dao/implement/[Feature]DaoImpl.java
5.  Service       → service/[Feature]Service.java
6.  API Controller → controller/api/[Feature]ApiController.java
7.  MVC Controller → controller/[Feature]Controller.java
8.  JSP + JS      → pages/[feature]/ + resources/js/[feature]/
9.  Sidebar       → layout/Sidebar.jsp
10. intercept-url → spring-security.xml  [Spring Security]
```

---

## Anti-patterns

- ห้าม return `String`, `Map`, `boolean` โดยตรงจาก `@RestController`
- ห้าม throw exception ออกนอก controller โดยไม่ handle
- ห้าม mix Gson + Jackson ใน project เดียวกัน
