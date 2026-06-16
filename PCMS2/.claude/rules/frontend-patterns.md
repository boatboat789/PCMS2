# Frontend Patterns (JSP / jQuery)

## Stack

- Bootstrap 4 · jQuery · DataTables · FlatPickr · SweetAlert2
- Static assets: `src/main/webapp/resources/`
- JSPs: `src/main/webapp/WEB-INF/pages/<feature>/`

## Standard AJAX Pattern (POST JSON)

```javascript
$.ajax({
    url: contextPath + '/api/foo/save',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(payload),
    success: function(res) {
        var data = JSON.parse(res);   // Gson returns String, not auto-parsed
        if (data.status === 'success') {
            Swal.fire('สำเร็จ', data.message, 'success');
        } else {
            Swal.fire('ผิดพลาด', data.message, 'error');
        }
    },
    error: function() {
        Swal.fire('Error', 'ไม่สามารถเชื่อมต่อได้', 'error');
    }
});
```

> `contextPath` มาจาก JSP: `var contextPath = '${pageContext.request.contextPath}';`

## DataTables Init (Standard)

```javascript
$('#myTable').DataTable({
    language: { url: contextPath + '/resources/plugins/datatables/th.json' },
    order: [[0, 'desc']],
    responsive: true
});
```

## FlatPickr Date Input

```javascript
flatpickr("#myDate", {
    dateFormat: "Y-m-d",
    locale: "th"
});
```

## SweetAlert2 Confirm Before Action

```javascript
Swal.fire({
    title: 'ยืนยัน?', text: 'ต้องการดำเนินการนี้ใช่ไหม',
    icon: 'warning', showCancelButton: true,
    confirmButtonText: 'ยืนยัน', cancelButtonText: 'ยกเลิก'
}).then(function(result) {
    if (result.isConfirmed) { /* proceed */ }
});
```

## JSP Layout

ทุก JSP ใช้ `<jsp:include page="../config/layout/Header.jsp"/>` และ `Sidebar.jsp` / `Footer.jsp`  
เพิ่มเมนูใหม่ใน `Sidebar.jsp` เท่านั้น — ไม่ hardcode ใน JSP อื่น

## Role/Permission Flag ใน JSP

User object อยู่ใน session: `${sessionScope.user.role}` หรือ `${sessionScope.user.xxx}`  
ตรวจสิทธิ์ใน JSP ผ่าน JSTL: `<c:if test="${sessionScope.user.isAdmin}">...</c:if>`
