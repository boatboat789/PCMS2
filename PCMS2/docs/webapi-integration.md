# Web API Integration — PCMS2

> Audit date: 2026-07-29. See `BGJOB/docs/webapi-integration.md` and `BGJobApi/docs/webapi-integration.md`
> for the server/library side of this integration.

## A) `BGJobApiService` — NOT used

The Spring `@Bean` factory is fully **commented out** in `config/AppConfig.java`:
```java
//	@Bean
//	public BGJobApiService bGJobApiService() { return new BGJobApiService(); }
```
No import, no call site, no autowiring anywhere else, and no dependency declaration in `pom.xml`. Unlike InspectSystem/QCMS/SFC, PCMS2 never pushes anything to D365 via this library.

## B) Endpoints PCMS2 exposes — for BGJOB to call IN

`JobManagementController` (`Setting/JobManagement`) exposes:

| Method | Path | Purpose |
|---|---|---|
| GET | `/api/status` | Returns `isRunning`/`scheduleEnabled` for PCMS2's two scheduled jobs |
| POST | `/api/run` | Kicks off `TaskService.runManual(fromDate, toDate)` on a daemon thread (≤7-day range) |
| POST | `/api/schedule/toggle` | Flips `scheduleEnabled` for both jobs |

All three accept an `X-Internal-Token` header (`isInternalCall()`, backed by `${internal.job.token}` property) as an alternative to session/permit auth. Comment in source: *"BGJOB (or other trusted internal caller) can skip the session/permit check with this header."* This is the mechanism BGJOB's `JobRepairService` uses to remotely trigger/inspect PCMS2's ERP sync jobs (`bgJobHandlerDataFromOrgatex`, `sortBackGroundAfterGetERPDataProcedure`) without duplicating the logic or touching PCMS2's DB directly.

## C) Other outbound web API calls

None. `Unirest` (dependency declared in `pom.xml`) is only referenced once, in `AppShutdownListener.java` (`Unirest.shutDown()`), and is never actually used to make a request anywhere in the codebase — dead dependency as far as outbound calls go. All ERP data retrieval is direct JDBC (`erpDatabase` `JdbcTemplate`), not a REST call — see `docs/background-jobs.md`.

## Why this shape

PCMS2 is a **callee**, not a caller, in the BGJOB integration. It doesn't need to push anything to D365 itself (its ERP sync is pure DB pull → upsert → stored proc), but BGJOB's central `JobRepair` admin page needs a way to trigger/inspect PCMS2's own jobs remotely — hence the token-gated `/api/*` endpoints instead of a `BGJobApiService` dependency.

## Cross-repo context

See sibling `docs/webapi-integration.md` in: `BGJOB`, `BGJobApi`, `InspectSystem`, `QCMS`, `SFC`, `LBMS`, `PPMM2`.
