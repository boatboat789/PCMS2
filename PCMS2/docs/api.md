# API / Endpoint — PCMS2

> อัปเดต 2026-11-10 · ไม่ใช่ REST แบบ `ApiResponse` — เป็น Spring MVC `@Controller` + **Gson** (`response.getWriter().println(g.toJson(...))` หรือ `@ResponseBody String`)
> ฝั่ง JS ต้อง `JSON.parse` เองเมื่อได้ String · ห้ามใช้ Jackson/ObjectMapper (`.claude/rules/core-behavior.md`) · session เก็บ user ที่ `session["user"]`/`["userObject"]`/`["permit"]`

## Response conventions
| แบบ | รูป | ใช้ที่ |
|---|---|---|
| รายการ/แถว | JSON array ของ bean (`PCMSTableDetail` / `PCMSSecondTableDetail` ...) | Main/Detail search |
| บันทึกช่อง Detail | array ของแถวที่ต้องรีเฟรช — แต่ละแถวมี `iconStatus` (`I`=สำเร็จ, `E`=ผิดพลาด) + `systemStatus` (ข้อความ) | `saveInputDetail` |
| Permit/Job API | `{"status":"SUCCESS"\|"FAIL","message":"..."}` | `Setting/*` |
| Report/Log | ผิดพลาด → HTTP 400/403/404 + ข้อความไทย หรือ JSON `status:"error"` (Log) | `Report/*`, `Log/*` |
- ⚠️ HTTP 200 ไม่ได้แปลว่า business สำเร็จ — JS ต้องอ่าน `iconStatus`/`status` (กฎ Swal: `.claude/rules/frontend-patterns.md`)
- ไม่ login → redirect `/login` (`FilterLogin`) · ไม่มี permit → หน้า `error/AccessDenied`

## Endpoint ทั้งหมด (context path `/PCMS2`)
### Login
| Method | Path | หมายเหตุ |
|---|---|---|
| GET | `/login` · `/logout` | ล้าง session |
| POST | `/login/loginAuth` | AD → fallback `Users` → โหลด Permit |

### Main (`/Main`, `/`)
| Method | Path | หมายเหตุ |
|---|---|---|
| GET | `/Main` | หน้า Summary · ต้อง `IsPCMSMain` |
| GET | `/Main/getInitData` | os/userId/isCustomer |
| POST | `/Main/searchByDetail` | body = `[PCMSTableDetail]` (ตัวกรอง) → ผล Summary · `IsCustomer` ซ่อน CFM/RollNo |
| POST | `/Main/getPrdDetailByRow` | drill-down ต่อแถว |
| POST | `/Main/saveColSettingToServer` · `saveDefault` · `loadDefault` | คอลัมน์ที่เห็น / ชุดตัวกรองรายคน |
| POST | `/Main/getEncrypted/{userId}` | คืนรหัส AES ของ userId ให้ปุ่มเปิดระบบอื่น |

### Detail (`/Detail`)
| Method | Path | หมายเหตุ |
|---|---|---|
| GET | `/Detail` | หน้า Detail · ต้อง `IsPCMSDetail` |
| POST | `/Detail/searchByDetail` | ผล Detail (UNION 6 ชนิด) |
| POST | `/Detail/saveInputDetail` | `caseSave` = stockRemark · pcRemark · stockLoad · replacedRemark · switchRemark · delayedDep · causeOfDelay · sendCFMCusDate |
| POST | `/Detail/saveInputDate` | วันที่ Plan CFM Lab / Plan Delivery ฯลฯ |
| POST | `/Detail/getCFMPlanDateDetail` · `getCFMPlanLabDateDetail` · `getDeliveryPlanDateDetail` · `getSendCFMCusDateDetail` | ประวัติ/รายละเอียดวันที่ |
| POST | `/Detail/getSwitchProdOrderListByPrd` · `getSwitchProdOrderListByRowProd` | drill-down switch |
| POST | `/Detail/saveColSettingToServer` · `saveDefault` · `loadDefault` | เหมือน Main |
ทุก endpoint ของ `/Detail/*` ต้องมี **`IsPCMSDetail`** (หน้า GET → `AccessDenied`, POST → HTTP 403) — แก้ 2026-11-10 (`business/roles.md` §5)

### Report / Log
| Method | Path | หมายเหตุ |
|---|---|---|
| GET | `/Report/{ReportType}/Detail` | หน้ารายงาน (ตอนนี้ `CFM`) · `IsReport` |
| GET | `/Report/{ReportType}/getReportDetail` | ข้อมูล · เช็ค `IsReport` · ตรวจวันที่/ประเภท |
| POST | `/Report/{ReportType}/createExcelReportDetail` | body = `[{id}]` · server ดึงข้อมูลจริงเอง · เพดาน 20,000 แถว · JXLS template |
| GET | `/Log/ProductionOrderLog/{dataType}` · `/Log/SaleOrderLog/{dataType}` | หน้า log · `dataType` = `ATT` หรือ `ERP365` |
| POST | `/Log/ProductionOrderLog/{dataType}/getProductionOrderLogBySearch` | ค้นตามช่วงวัน/PO · ตัด 50,000 แถว |
| GET | `/Log/SaleOrderLog/{dataType}/getSaleOrderLogBySearch` | ชุดเดียวกันฝั่ง Sale |

### Setting (ADMIN/ITSUPP)
| Method | Path | หมายเหตุ |
|---|---|---|
| GET | `/Setting/PermitManagement` · `/api/permits` · `/api/users` | ดู |
| POST | `/Setting/PermitManagement/api/permit/update` | **ADMIN เท่านั้น** · แก้ ADMIN ไม่ได้ |
| POST | `/Setting/PermitManagement/api/user/permit` | ADMIN/ITSUPP · ITSUPP ตั้งเป็น ADMIN ไม่ได้ |
| GET | `/Setting/JobManagement` · `/api/status` | สถานะ `isRunning`/`scheduleEnabled` |
| POST | `/Setting/JobManagement/api/run` | ช่วงวันที่ ≤7 วัน (`TaskService.runManual`) |
| POST | `/Setting/JobManagement/api/schedule/toggle` | เปิด/ปิดทั้งสองงาน |
`/api/status` · `/api/run` · `/api/schedule/toggle` รับ header **`X-Internal-Token`** (= `${internal.job.token}`) แทน session — ใช้โดย BGJOB (`docs/webapi-integration.md`)

### อื่นๆ
`/handleError` · `/SapToWeb` (ซากหน้าทดสอบ) · `TestController` (ทดสอบ — อย่าพึ่ง)

## Error handling
`MyExceptionController` + `ErrorController` → หน้า `error/error.jsp` · controller ควร catch แล้วคืน JSON/ข้อความไทยเอง (Report/Log ทำแล้วหลัง `72d41c9`) · ห้ามปล่อย 500 ใส่ผู้ใช้
