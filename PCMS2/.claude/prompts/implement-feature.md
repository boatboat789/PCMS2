# Prompt: Implement Feature

---

## Template: Setting Page ใหม่

```
Implement: Setting page สำหรับ [FEATURE_NAME]
Business: [description — ใครทำอะไรได้บ้าง]
Table: [DB table name]
Columns: [key columns + types]
Roles allowed: [ADMIN, CENTER, etc.]
Validation: [required fields, uniqueness, etc.]

อ่านก่อน: docs/api.md, backend/CLAUDE.md, frontend/CLAUDE.md

Checklist:
1. Entity + DAO interface + DaoImpl
2. Service
3. API Controller (ApiResponse<T>, @PreAuthorize)
4. MVC Controller (buildView)
5. JSP (list + modal form)
6. JS (DataTables + AJAX save/delete)
7. Sidebar entry
8. intercept-url ใน spring-security.xml  [ถ้าใช้ Spring Security]
9. SQL (IF NOT EXISTS) → sql/YYYY-MM-DD_[feature].sql
```

---

## Template: Workflow Step ใหม่

```
Add workflow step: [FROM_STATUS] → [TO_STATUS]
Process code: [MS_XXX / PC_XXX]
Who can trigger: [Role]
Condition: [e.g. only when approved_flag = 1]

SQL required:
1. INSERT master_po_status (status_code, name, order_no, ...)
2. INSERT master_process_code (process_code, name)
3. INSERT config_po_workflow (from_status_id, to_status_id, ...)
4. INSERT config_role_workflow_permission (role_id, step_id)
```

---

## Template: API Endpoint ใหม่

```
Add endpoint: [METHOD] /api/[feature]/[action]
Returns: ApiResponse<[T]>
Auth: @PreAuthorize("hasAnyRole('[ROLE]')")
Business: [description]
Input: [request body / path variables]
Output: [response data]

Steps:
1. Service method
2. API Controller method + @PreAuthorize
3. intercept-url  [Spring Security]
4. JS: $.ajax() call
```

---

## Template: แก้ Role Permission

```
Change: [Role] ให้ [สามารถ/ไม่สามารถ] [action]

ตรวจ 3 จุด:
1. spring-security.xml — intercept-url pattern ใน URL นั้น
2. @PreAuthorize — บน Controller / API method
3. config_role_* tables ใน DB:
   - config_role_permission_mapping (permission codes)
   - config_role_status_mapping (role → status visibility)
   - config_role_workflow_permission (role → step)
   - config_role_page_permission (role → page)
```

---

## Standard Order of Implementation

```
DB (SQL patch) → Entity → DAO → Service → Controller → JSP/JS → Test
```

อย่าข้ามขั้น — Entity ต้องมีก่อน DAO จะ compile ได้
