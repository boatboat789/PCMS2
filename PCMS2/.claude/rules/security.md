# Security Rules — PCMS2

PCMS2 ใช้ **FilterLogin** เท่านั้น — ไม่มี Spring Security

---

## Auth Pattern

`FilterLogin extends OncePerRequestFilter` — ตรวจ session ทุก request  
User object อยู่ใน `session.getAttribute("user")` — ไม่ใช่ SecurityContext  
ไม่มี `@PreAuthorize` — ตรวจสิทธิ์ใน Controller หรือ Service แทน

---

## Getting the Logged-in User

```java
// ✅ CORRECT — PCMS2 ใช้ FilterLogin
User user = (User) session.getAttribute("user");

// ❌ WRONG — ไม่มี SecurityContext ใน PCMS2
SecurityContextHolder.getContext().getAuthentication();
```

---

## URL Access Control

FilterLogin ตรวจ session ทุก request โดย default  
เพิ่ม path exception ใน `FilterLogin.doFilterInternal()` ถ้าต้องการ public path (เช่น `/login`, `/resources/**`)

---

## Input Validation

- Validate ที่ system boundary เท่านั้น (controller)
- Parameterized queries เสมอ — ห้าม string concatenation ใน SQL
- ห้าม log sensitive data (password, token, session ID)

---

## Auth Bypass (SSO pattern — internal network only)

```java
// invalidate session เดิมก่อน (ป้องกัน session fixation)
HttpSession old = req.getSession(false);
if (old != null) { old.invalidate(); }
HttpSession session = req.getSession(true);
// inject user object...
```

ต้อง validate empId ใน DB + activeFlag ก่อนเสมอ — ห้าม blind trust parameter
