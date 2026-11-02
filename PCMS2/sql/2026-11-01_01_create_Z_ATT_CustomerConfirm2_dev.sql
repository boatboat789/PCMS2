-- สร้างโครงตาราง PCMS.dbo.Z_ATT_CustomerConfirm2 (เปล่า ไม่มีข้อมูล) สำหรับ DEV
-- ที่มา: อ่าน metadata จาก PRD 2026-11-01 (sys.columns / sys.indexes) — ไม่ได้ดึงข้อมูล
-- เหตุผล: DEV ไม่มีตารางนี้ → หน้า Report CFM (/Report/CFM/Detail) ตอบ HTTP 500
--        "Invalid object name 'PCMS.dbo.Z_ATT_CustomerConfirm2'"
-- รันบน DEV เท่านั้น (PCMSDEV,1435) — PRD มีตารางนี้อยู่แล้ว · รันซ้ำได้ (guard ด้วย OBJECT_ID)
-- ตารางว่างหลังรัน: Report CFM จะโหลดได้แต่ไม่มีแถว จนกว่าจะรัน job sync
--   (TaskService.runManual → handlerBackGroundZATTCustomerConfirm2 → spd_UpsertToZ_ATT_CustomerConfirm2)

USE [PCMS];
GO

IF OBJECT_ID(N'dbo.Z_ATT_CustomerConfirm2', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Z_ATT_CustomerConfirm2 (
        Id               INT IDENTITY(1,1) NOT NULL,
        SendDate         DATE          NULL,
        NoPerDay         INT           NULL,
        ReplyDate        DATE          NULL,
        CFMNo            VARCHAR(50)   NULL,
        CustomerName     VARCHAR(50)   NULL,
        SO               VARCHAR(50)   NULL,
        SOLine           VARCHAR(50)   NULL,
        DueDate          DATE          NULL,
        PO               VARCHAR(50)   NULL,
        Material         VARCHAR(50)   NULL,
        ProductName      VARCHAR(50)   NULL,
        LabNo            VARCHAR(50)   NULL,
        Color            VARCHAR(50)   NULL,
        ProdId           VARCHAR(50)   NULL,
        LotNo            VARCHAR(50)   NULL,
        CFM_L            DECIMAL(13,3) NULL,
        CFM_Da           DECIMAL(13,3) NULL,
        CFM_Db           DECIMAL(13,3) NULL,
        CFM_St           DECIMAL(13,3) NULL,
        CFM_DeltaE       DECIMAL(13,3) NULL,
        Result           VARCHAR(10)   NULL,
        QCComment        VARCHAR(200)  NULL,
        RemarkFromSubmit VARCHAR(200)  NULL,
        NextLot          VARCHAR(50)   NULL,
        Qty              DECIMAL(13,3) NULL,
        UnitId           VARCHAR(50)   NULL,
        DataStatus       VARCHAR(1)    NULL,
        ChangeDate       DATETIME      NULL,
        CreateDate       DATETIME      NULL
    );  -- PRD เป็น heap (ไม่มี PK/clustered index), ไม่มี default/trigger/FK
END
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes
               WHERE name = N'IX_ZATT_Confirm_ProdId_ChangeDate'
                 AND object_id = OBJECT_ID(N'dbo.Z_ATT_CustomerConfirm2'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_ZATT_Confirm_ProdId_ChangeDate
        ON dbo.Z_ATT_CustomerConfirm2 (ProdId ASC, ChangeDate DESC)
        INCLUDE (DataStatus);
END
GO

-- ตรวจผล: ต้องได้ 30 คอลัมน์ + 1 index
SELECT (SELECT COUNT(*) FROM sys.columns WHERE object_id = OBJECT_ID(N'dbo.Z_ATT_CustomerConfirm2')) AS columns_,
       (SELECT COUNT(*) FROM sys.indexes WHERE name = N'IX_ZATT_Confirm_ProdId_ChangeDate'
                                           AND object_id = OBJECT_ID(N'dbo.Z_ATT_CustomerConfirm2')) AS ix_;
GO
