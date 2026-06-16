# Security Rules

---

## Auth Pattern

**ถ้าใช้ Spring Security:**
- `spring-security.xml` — intercept-url + form-login + session management
- `CustomAuthenticationProvider` — AD API + local BCrypt fallback
- `CustomUserDetails` — stores roles, permissionCodes, responsibleStepIds, pagePermissions
- `GlobalExceptionHandler` — catch uncaught → return FAIL response (ไม่ expose stack trace)

**ถ้าใช้ FilterLogin:**
- `FilterLogin extends OncePerRequestFilter` — ตรวจ session ทุก request
- User object อยู่ใน `session.getAttribute("user")` ไม่ใช่ SecurityContext
- ไม่มี `@PreAuthorize` — ตรวจสิทธิ์ใน Controller/Service แทน

---

## URL Access Control

**Spring Security:**
```xml
<!-- spring-security.xml — เพิ่มก่อนเสมอ มิฉะนั้น catch-all บัง -->
<intercept-url pattern="/new-feature/**" access="hasAnyRole('ROLE_ADMIN','ROLE_USER')"/>
<intercept-url pattern="/api/new-feature/**" access="isAuthenticated()"/>
<!-- catch-all อยู่บรรทัดสุดท้าย -->
<intercept-url pattern="/**" access="isAuthenticated()"/>
```

**FilterLogin:**
- เพิ่ม path exception ใน `FilterLogin.doFilterInternal()` ถ้าต้องการ public path

---

## Controller / API Guards

```java
// Spring Security — method level
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@PostMapping("/api/feature/save")
public ResponseEntity<ApiResponse<Boolean>> save(...) { ... }

// Spring Security — class level (ครอบทุก method ใน class)
@PreAuthorize("isAuthenticated()")
@RestController
public class FeatureApiController { ... }
```

---

## Getting the Logged-in User

**Spring Security:**
```java
// CORRECT
@AuthenticationPrincipal CustomUserDetails userDetails
// หรือ
CustomUserDetails user = SecurityUtils.getCurrentUser();

// WRONG — ไม่มี SecurityContext ใน FilterLogin projects
session.getAttribute("user")  // ← ผิดสำหรับ Spring Security project
```

**FilterLogin:**
```java
// CORRECT
User user = (User) session.getAttribute("user");

// WRONG — ไม่มี SecurityContext
SecurityContextHolder.getContext().getAuthentication()
```

---

## Input Validation

- Validate ที่ system boundary เท่านั้น (controller / API)
- Parameterized queries เสมอ — ห้าม string concatenation ใน SQL
- ห้าม expose stack trace — GlobalExceptionHandler จัดการ
- ห้าม log sensitive data (password, token, session ID)

---

## CSRF

Spring Security default: CSRF enabled สำหรับ state-changing requests
- AJAX POST ต้องส่ง `_csrf` token หรือ disable สำหรับ REST API:
```xml
<csrf disabled="true"/>  <!-- ถ้า REST API ใช้ stateless token แทน -->
```

---

## Auth Bypass (SSO pattern — internal network only)

ถ้ามี bypass endpoint:
```java
// invalidate session เดิมก่อน (ป้องกัน session fixation)
HttpSession old = req.getSession(false);
if (old != null) { old.invalidate(); }
HttpSession session = req.getSession(true);
// inject auth context...
```
ต้อง validate empId ใน DB + activeFlag ก่อนเสมอ — ห้าม blind trust parameter
