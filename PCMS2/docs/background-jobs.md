# Background Jobs — PCMS2

> Audit date: 2026-07-29. Re-verify by checking `@Scheduled` annotations below before relying on this
> for anything load-bearing.

Mechanism: Spring `@Scheduled` via `config/SchedulerConfig.java` (`@EnableScheduling`, `TaskScheduler` bean, pool size 5, daemon threads, `destroyMethod="shutdown"`). No Quartz, no XML `<task:scheduled-tasks>`. All jobs live in one class: `service/TaskService.java`.

## Active jobs

### 1. `bgJobHandlerDataFromOrgatex` — Orgatex → SOR/CFM import
- **Trigger**: `@Scheduled(cron = "0 0 1 * * *")` — daily at 01:00.
- **เวลาเริ่ม**: cron-based — รอบแรกคือ 01:00 ที่ใกล้ที่สุดหลัง deploy
- **Guards**: `jobLocks["ORGATEX_IMPORT"]` (overlap guard, added late per `NEXT_SESSION.md` 2026-07-17) + `scheduleEnabled` flag.
- **What it does**: `DataImportSORService.getList()` → `DataImportSORDaoImpl` (plain SQL `SELECT DISTINCT` against SOR DB) → `FromSORCFMService.upSertFromSORCFMDetail(list)` → `FromSORCFMDaoImpl` (hand-built upsert SQL, no stored proc).

### 2. `sortBackGroundAfterGetERPDataProcedure` — full ERP sync
- **Trigger**: `@Scheduled(cron = "0 14,44 * * * *")` — twice hourly at :14/:44.
- **เวลาเริ่ม**: cron-based — รอบแรกคือนาที :14 หรือ :44 ที่ใกล้ที่สุดหลัง deploy
- **Guards**: `jobLocks["ERP_SYNC_JOB"]` + `scheduleEnabled`.
- **What it does** (in order):
  1. `handlerBackGroundZATTCustomerConfirm2()` → pulls `Z_ATT_CustomerConfirm2Detail` from ERP, upserts, then `EXEC spd_UpsertToZ_ATT_CustomerConfirm2`.
  2. `runAllSyncJobs()` — customer sync (no SP), sale order sync (`EXEC spd_UpsertToMainSale`, `EXEC spd_UpsertToSale`), production order sync (7 ERP detail sets → 7 stored procs: `spd_UpsertToMainProd`, `spd_UpsertToCFM`, `spd_UpsertToMainProdSale`, `spd_UpsertToPacking`, `spd_UpsertToSubmitDate`, `spd_UpsertToGoodReceive`, `spd_UpsertToMainBillBatch`).
  3. `execUpsertToTEMPProdWorkDate()`, `execUpsertToTEMPUserStatusOnWeb()`, `execSumBillAndGoodReceive()` (SPs: `spd_UpsertToTEMP_ProdWorkDate`, `spd_UpsertToTEMP_UserStatusOnWeb`, `spd_SumBillAndGoodReceive`).
- **Purpose**: full ERP → PCMS2 sync for customers, sale orders, production orders, CFM/packing/bill/good-receive data.

### Manual equivalent
- `TaskService.runManual(fromDate, toDate)` via `JobManagementController` `/api/run` — same 5-step sequence with a date-range override on `ERPAtechDaoImpl.timeFocusOverride` (`ThreadLocal`). Shares the `"ERP_SYNC_JOB"` lock. User-triggered, not periodic.

### Admin control
- `JobManagementController` + `Setting/JobManagement.jsp` — `/api/status`, `/api/run` (≤7-day range), `/api/schedule/toggle` (flips `scheduleEnabled` for both jobs).

## Not a scheduled job (context lifecycle only)

- `listener/AppShutdownListener.java` — `contextDestroyed` only (ThreadLocal sweep, Unirest shutdown, JDBC driver deregistration).

## Cross-repo context

See sibling `docs/background-jobs.md` in: `InspectSystem`, `QCMS`, `BGJOB`, `BGJobApi`, `core`, `LBMS`, `PPMM2`, `SFC`.
