# Frontend — JSP / jQuery (PCMS2)

> Code snippets อยู่ใน `.claude/rules/frontend-patterns.md` (auto-load)

---

## Stack

- JSP + JSTL + EL
- Bootstrap 4 · jQuery · DataTables · Bootstrap Select · SweetAlert2 · FlatPickr
- Static assets: `src/main/webapp/resources/`
- JSPs: `src/main/webapp/WEB-INF/pages/[feature]/`

---

## Rules

- Bootstrap 4 only (`badge-warning`, `mr-1`, `ml-1` — ไม่ใช่ Bootstrap 5)
- **selectpicker: เรียก `refresh()` ทุกครั้งหลัง container visible หรือ data เปลี่ยน**
- Datepicker: ใช้ `.datepicker('setDate', date)` ไม่ใช่ `.val()`
- Swal2 `preConfirm`: ใช้ `document.getElementById().value` ไม่ใช่ `$(selector).val()`
- AJAX response: ต้อง `JSON.parse(res)` ก่อนใช้ — Gson return `String` ไม่ใช่ JSON object
- Prefer reload in-place เหนือ `window.location.href` (ผู้ใช้ไม่หลุด scroll)

---

## JS Globals

```javascript
var ctx = '${pageContext.request.contextPath}';  // context path prefix
function handleAjaxError(xhr, status, error) {   // global error handler
    Swal.fire('Error', 'ไม่สามารถเชื่อมต่อได้ (HTTP ' + xhr.status + ')', 'error');
}
```

---

## Layout

ทุก JSP ใช้:
```jsp
<jsp:include page="../config/layout/Header.jsp"/>
<jsp:include page="../config/layout/Sidebar.jsp"/>
<!-- content -->
<jsp:include page="../config/layout/Footer.jsp"/>
```

เพิ่มเมนูใหม่ใน `Sidebar.jsp` เท่านั้น

---

## Role/Permission Flags

PCMS2 ใช้ FilterLogin — ตรวจสิทธิ์ผ่าน session:

```jsp
<c:if test="${sessionScope.user.role eq 'ADMIN'}">...</c:if>
```

```javascript
// Hidden inputs สำหรับ JS
var canEdit = $('#flag_can_edit').val() === 'true';
```

```java
// Controller ส่ง flag ไป JSP
mav.addObject("canEdit", user.isAdmin());
```

---

## div-edit-mode / div-view-mode Pattern

```javascript
// เปิด edit mode
$('.div-view-mode').hide();
$('.div-edit-mode').show();
// กลับ view mode
syncEditToView();   // copyค่า edit → view label ก่อน
$('.div-edit-mode').hide();
$('.div-view-mode').show();
```

**ห้ามลืม `syncEditToView()` ก่อน toggle visibility**

---

## Common Pitfalls

| อาการ | สาเหตุ | วิธีแก้ |
|---|---|---|
| selectpicker blank หลัง load | ไม่ได้ refresh() | เรียก `$(...).selectpicker('refresh')` |
| Datepicker ค่าไม่เปลี่ยน | ใช้ `.val()` แทน setDate | ใช้ `.datepicker('setDate', d)` |
| Swal preConfirm ค่าว่าง | ใช้ `$(selector).val()` ใน preConfirm | ใช้ `document.getElementById(id).value` |
| AJAX response ไม่ parse JSON | Gson return String — ต้อง parse | เพิ่ม `JSON.parse(res)` หลัง success |
| AES-encrypted param ใน URL | PCMSMainController เข้ารหัส orderId | ใช้ decrypt endpoint ก่อน query |
