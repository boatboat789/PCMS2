# Coding Standards

## Code Generation Rules

When generating code:
* Produce complete working examples.
* Prefer clarity over cleverness.
* Use meaningful names.
* Avoid unnecessary abstraction.
* Avoid over-engineering.
* Include error handling where appropriate.
* Keep functions focused on a single responsibility.

---

## Naming Conventions

| Context | Convention | Example |
|---|---|---|
| Java class | PascalCase | `TransformOrderService` |
| Java method / field | camelCase | `findByEmpId()` |
| DB column | snake_case | `emp_id`, `created_on` |
| DB → Java mapping | BeanPropertyRowMapper auto-maps | `emp_id` → `empId` |
| JSP file | kebab-case | `transform-order-list.jsp` |
| JS variable | camelCase | `contextPath`, `orderId` |
| URL path | kebab-case | `/transform-order/list` |

---

## Code Review Criteria

### Critical — fix before merge

* Bugs that cause incorrect behavior
* Security risks (injection, auth bypass, sensitive data exposure)
* Data corruption risks (missing transaction, wrong qualifier)

### Major — should fix

* Performance problems (N+1 query, missing index hint)
* Maintainability concerns (duplicate logic, unclear responsibility)
* Architectural violations (JPA used, Jackson used, wrong DB qualifier)

### Minor — nice to fix

* Style inconsistencies
* Naming issues
* Small improvements

---

## Refactoring Rules

When refactoring:
* Preserve behavior exactly.
* Reduce duplication.
* Improve readability.
* Improve maintainability.
* Avoid large rewrites unless requested.

Explain the reason behind significant changes.

---

## Debugging Approach

When diagnosing problems:
1. Identify likely root causes.
2. Explain reasoning.
3. List assumptions.
4. Suggest verification steps.
5. Suggest fixes from safest to most aggressive.

Do not jump to conclusions.

---

## Security Guidelines

Always consider:
* Input validation at system boundaries (user input, external APIs)
* Authentication — session check via FilterLogin
* Authorization — role check in controller or @PreAuthorize
* SQL injection — use parameterized queries, never string concat
* Sensitive data — never log passwords or tokens

Never recommend insecure practices.

---

## Performance Guidelines

Consider:
* Algorithm complexity — avoid O(n²) in large datasets
* Memory usage — avoid loading entire table into memory
* Database access patterns — use TOP/pagination for large result sets
* Temp tables — always DROP in finally block (see `database/CLAUDE.md`)
* ThreadLocal for per-request state in singleton services

Focus on bottlenecks with measurable impact.

---

## Comment Policy

Write comments only when the WHY is non-obvious:
* A hidden constraint
* A workaround for a specific external system bug
* A subtle invariant that would surprise a reader

Do not comment on WHAT the code does — well-named identifiers already do that.

---

## Output Format (for Claude responses)

For code changes:
1. Summary of change
2. Issues found (if any)
3. Recommended solution
4. Updated code

For code reviews:
1. Critical
2. Major
3. Minor
4. Suggested improvements

For debugging:
1. Symptoms
2. Likely causes
3. Verification steps
4. Recommended fix
