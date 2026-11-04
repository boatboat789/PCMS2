# API Contract Rules — PCMS2

PCMS2 ใช้ **Gson pattern** เท่านั้น — ไม่ใช่ Jackson/ApiResponse

---

## Response Format

```java
// ทุก @ResponseBody ใน PCMS2 return Gson string
@ResponseBody
public String getList() {
    List<Foo> list = fooService.findAll();
    return new Gson().toJson(list);
}
```

```javascript
// JS ต้อง JSON.parse() เสมอ — Gson return String ไม่ใช่ JSON object
$.ajax({
    success: function(res) {
        var data = JSON.parse(res);
        // ใช้ data
    }
});
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

Context path: `/PCMS2`

---

## Anti-patterns

- ห้าม return `ResponseEntity<ApiResponse<T>>` — PCMS2 ไม่ใช้ ApiResponse
- ห้าม `import com.fasterxml.jackson.*` — ใช้ Gson เท่านั้น
- ห้าม Jackson `ObjectMapper` — ใช้ `new Gson().toJson()` / `new Gson().fromJson()`
- ห้าม throw exception ออกนอก controller โดยไม่ handle
