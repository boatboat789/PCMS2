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

## SweetAlert2 — icon/ข้อความต้องตรงกับผลจริงเสมอ

ทุกครั้งที่ยิง action (save/delete/approve/cancel ฯลฯ) แล้วเด้ง Swal แจ้งผล **ห้าม hardcode icon/ข้อความไว้ตายตัว
โดยไม่เช็คผลจริงจาก response** — ต้อง map ตาม `data.status` ทุกครั้ง (ดู Standard AJAX Pattern ด้านบน):

### ต้องทำ
- **icon ต้องสอดคล้องกับ `data.status` เสมอ** — `'success'` → `'success'`, อื่นๆ (`'error'`/`'fail'`) → `'error'`
- AJAX `error:` callback (HTTP 4xx/5xx, network fail) ต้องขึ้น icon `'error'` เสมอ — ห้ามปล่อย `success:` callback จับ error case
- ถ้า action มีหลาย step ใน callback เดียว (เช่น save แล้ว reload list) และ step หลังพัง (throw/reject) **ห้ามให้ Swal success ที่โชว์ไปแล้วค้างอยู่โดยไม่แจ้งว่า step หลังพัง** — ต้อง catch แล้วแจ้งแยก

### ห้ามทำ
- ห้าม `Swal.fire(..., 'success')` แบบ hardcode ก่อนเช็ค `data.status`
- ห้ามเขียน logic ที่ยิง Swal success ใน `success:` callback ของ jQuery AJAX โดยไม่ดู response body — `success:` callback หมายถึง "HTTP call สำเร็จ" ไม่ใช่ "business logic สำเร็จ" (Gson คืน HTTP 200 เสมอแม้ business logic fail)
- ห้ามใช้ข้อความ generic เดียวกันทั้ง success/error (เช่น "ดำเนินการเสร็จสิ้น" ทั้งสองกรณี) — ต้องใช้ `data.message` จาก backend หรือข้อความที่บอกผลจริง

## JSP Layout

ทุก JSP ใช้ `<jsp:include page="../config/layout/Header.jsp"/>` และ `Sidebar.jsp` / `Footer.jsp`  
เพิ่มเมนูใหม่ใน `Sidebar.jsp` เท่านั้น — ไม่ hardcode ใน JSP อื่น

## Role/Permission Flag ใน JSP

User object อยู่ใน session: `${sessionScope.user.role}` หรือ `${sessionScope.user.xxx}`  
ตรวจสิทธิ์ใน JSP ผ่าน JSTL: `<c:if test="${sessionScope.user.isAdmin}">...</c:if>`
