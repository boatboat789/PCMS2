/*
  2026-11-04_01 (แก้ไขรอบ 2026-11-09) — กัน TEMP_UserStatusAuto คีย์ซ้ำ   ⚠️ ส่งให้ DBA ตรวจก่อนรัน

  คีย์ = ProductionOrder + SaleOrder + SaleLine + Grade (ตาราง PCMS.dbo.TEMP_UserStatusAuto, SQL Server 2014 compat 120)

  ▌สถานะบน PRD (ตรวจ 2026-11-09 08:40 แบบอ่านอย่างเดียว)
    - proc spd_UpsertToTEMP_UserStatusOnWeb ถูก ALTER แล้วเมื่อ 2026-11-04 16:58:41 (ส่วน (A) ฉบับแรกรันไปแล้ว — นิยามบน PRD ตรงกับไฟล์ฉบับเดิมทุกบรรทัด)
    - ล้างคีย์ซ้ำ (B) รันไปแล้ว ~16:58:17 (แถว X 9 คีย์ ChangeDate 2026-11-04 16:58:17)
    - แต่ซ้ำเกิดใหม่ 3 คีย์หลังจากนั้น: P6E076/2R (06 พ.ย. 13:44), P6H142 (06 พ.ย. 13:44), S6I012 (07 พ.ย. 16:44) → ปัญหายังเกิดซ้ำ ~วันละราย
    - ตาราง 106,302 แถว (O 68,924 / X 37,378) · 29 คีย์มีมากกว่า 1 แถว (นับทุกสถานะ): O+O 3 · O+X 9 · X+X 17
    - ยังไม่มี unique index ใดๆ (มีแค่ IX_TEMP_UserStatusAuto_PO_Grade_Sale ไม่ unique)

  ▌ข้อผิดพลาดของฉบับเดิมที่พบตอนตรวจ (ทดสอบใน tempdb ไม่แตะตารางจริง)
    1) (C) เดิม CREATE UNIQUE ... WHERE DataStatus='O' WITH (IGNORE_DUP_KEY = ON) **สร้างไม่ได้** — Msg 10618: filtered index ใช้ IGNORE_DUP_KEY ไม่ได้
    2) ถ้าเปลี่ยนเป็น unique ธรรมดา UPDATE ขั้น 2 ของ proc จะ **พังด้วย Msg 2601** เมื่อคีย์มีหลายแถว เพราะ UPDATE พลิก "ทุกแถว" ของคีย์กลับเป็น 'O'
       (ฉบับ (A) เดิมก็มีผลเดียวกัน: แถว X ที่ (B) ปิดไว้ถูกพลิกกลับเป็น O เมื่อ PO นั้นถูกประมวลผลอีก → ซ้ำคืนชีพ; 9 คีย์ O+X ตอนนี้เข้าข่าย)
       และ proc ไม่ได้อยู่ใน transaction: ขั้น 1 (ปิด O→X ทั้ง batch) commit แล้ว ขั้น 2 error → สถานะของทั้ง batch ว่างจนรอบถัดไป
    3) ต้นเหตุของซ้ำ: แถวซ้ำมี CreateDate เท่ากันถึง ms (เช่น 2026-06-18 14:53:14.737) และ Id สลับกันเป็นชุด 3+3 → proc สองรอบเริ่มพร้อมกัน
       ทั้งคู่เช็ค NOT EXISTS ผ่านก่อน INSERT (race) · ยังไม่ทราบว่าผู้เรียกที่สองคือใคร (ข้อสงสัย: job สองตัวที่ยิง proc เดียวกัน เช่น TaskService ของ PCMS2 กับ BGJOB, หรือเครื่อง DEV ที่ชี้ PRD) — ควรหาด้วย
       (เวลาที่เกิด 14:53:14 / 13:44:11 / 16:44:14 ไม่ตรงกับ :14/:44 เสมอไป)

  ▌สิ่งที่ฉบับนี้ทำ
    (A2) ALTER proc จากนิยามบน PRD เดิม แก้ 2 จุดเท่านั้น (ค้นคำว่า "FIX 2026-11-09" ในตัว proc):
         - UPDATE ขั้น 2 ฟื้นเฉพาะแถว Id สูงสุดของแต่ละคีย์ (ไม่พลิกทุกแถว)
         - ครอบ "ตรวจ NOT EXISTS แล้ว INSERT" ด้วย sp_getapplock (Exclusive, Transaction) + TRY/CATCH — ปิด race ที่ต้นเหตุ ไม่ต้องพึ่ง index
    (B2) ปิดแถว O ซ้ำที่เหลือ (ตอนนี้ 3 คีย์) — SELECT ตรวจก่อน, UPDATE ถูก comment ไว้
    (C2) [ไม่บังคับ — แนะนำรอ] unique index แบบ filtered ธรรมดา (ไม่มี IGNORE_DUP_KEY) เป็นตาข่ายกันพลาด:
         ทุก connection ที่เขียนตารางนี้ต้อง SET QUOTED_IDENTIFIER ON (sqlcmd/job เก่าบางตัวเป็น OFF → INSERT/UPDATE จะ error) และถ้า race หลุด proc จะ error แทนที่จะซ้ำเงียบๆ
         จึงควรรัน (A2) แล้วเฝ้าดู dup_key_groups = 0 สักสัปดาห์ก่อนค่อยสร้าง

  ▌ลำดับรัน: (A2) → (B2) → ตรวจผล (SELECT ท้ายไฟล์) · ไม่รันช่วง job (:14/:44, 01:00) · รันซ้ำได้ · ก่อน ALTER ให้ script นิยามเดิมเก็บไว้ (sp_helptext) เพื่อย้อนกลับ
  ▌ไม่ได้ทดสอบ: การรัน proc ทั้งตัว (ตรวจแค่ syntax ด้วย PARSEONLY + ทดสอบลอจิก UPDATE/INSERT/applock ใน #temp table) — ควรลองบน DEV ก่อน
*/
USE [PCMS]
GO
-- (A2) ALTER proc (นิยามเต็ม = PRD ณ 2026-11-09 + 2 จุดที่แก้)
-- =============================================
-- Author:		Thiti
-- Create date: 16/05/2563
-- Description:	
-- 2025-06-27 #CH001 BOAT : Add CFMSendDate For check with CFMStatus = ''
-- =============================================
ALTER PROCEDURE [dbo].[spd_UpsertToTEMP_UserStatusOnWeb]
--CREATE PROCEDURE [dbo].[spd_UpsertToTEMP_UserStatusOnWeb] 
AS
BEGIN
	SET NOCOUNT ON;     
 	declare @Date date  = convert(Date,GetDate()) ;
	declare @Time time = convert(Time,GetDate()) ;
	declare  @dateTime datetime = cast(@Date as datetime) + cast(@Time as datetime);
	declare  @dateTimeFortyMinAgo datetime = cast(dateadd(MINUTE, -60, getdate()) as datetime)  ; 
	--declare  @dateTimeFortyMinAgo datetime = cast(dateadd(MONTH, -4, getdate()) as datetime)  ;
	--declare  @dateTimeFortyMinAgo datetime = cast(dateadd(year, -3, getdate()) as datetime)  ; 


IF OBJECT_ID('tempdb..#tempUCAL') IS NOT NULL   
DROP TABLE #tempUCAL   ;
IF OBJECT_ID('tempdb..#tempUCALRP') IS NOT NULL  
DROP TABLE #tempUCALRP;  
IF OBJECT_ID('tempdb..#TempG') IS NOT NULL   
DROP TABLE #TempG ;
IF OBJECT_ID('tempdb..#TempProdOrder') IS NOT NULL
    DROP TABLE #TempProdOrder;

-- Use UNION ALL for better performance and apply DISTINCT once at the end.
SELECT DISTINCT ProductionOrder
INTO #TempProdOrder
FROM (
    SELECT [ProductionOrder]
    FROM [PCMS].[dbo].[ReplacedProdOrder]
    WHERE DataStatus = 'O' AND [ChangeDate] > @dateTimeFortyMinAgo

    UNION ALL

    SELECT [ProductionOrderRP]
    FROM [PCMS].[dbo].[ReplacedProdOrder]
    WHERE DataStatus = 'O' AND [ChangeDate] > @dateTimeFortyMinAgo

    UNION ALL

    SELECT [ProductionOrder]
    FROM [PCMS].[dbo].[SwitchProdOrder]
    WHERE DataStatus = 'O' AND [ChangeDate] > @dateTimeFortyMinAgo

    UNION ALL

    SELECT ProductionOrderSW
    FROM [PCMS].[dbo].[SwitchProdOrder]
    WHERE DataStatus = 'O' AND [ChangeDate] > @dateTimeFortyMinAgo

    UNION ALL

    SELECT a.[ProductionOrder]
    FROM [PCMS].[dbo].[FromSapMainProd] AS a
    LEFT JOIN [PCMS].[dbo].[FromSapMainSale] AS b
        ON a.[SaleOrder] = b.[SaleOrder]
        AND a.[SaleLine] = b.[SaleLine]
    WHERE (a.DataStatus = 'O' AND a.[ChangeDate] > @dateTimeFortyMinAgo)
        OR (b.DataStatus = 'O' AND b.[ChangeDate] > @dateTimeFortyMinAgo)

    UNION ALL

    SELECT [ProductionOrder]
    FROM [PCMS].[dbo].[InputStockLoad]
    WHERE DataStatus = 'O' AND [ChangeDate] > @dateTimeFortyMinAgo

    UNION ALL

    SELECT [ProductionOrder]
    FROM [PCMS].[dbo].[FromSapGoodReceive]
    WHERE DataStatus = 'O' AND [ChangeDate] > @dateTimeFortyMinAgo

    UNION ALL

    SELECT [ProductionOrder]
    FROM [PCMS].[dbo].[FromSapMainBillBatch]
    WHERE DataStatus = 'O' AND [ChangeDate] > @dateTimeFortyMinAgo
) AS A;

  --select * from #TempProdOrder WHERE PRODUCTIONORDER = 'P5B157/1R'
  --select * from #tempProdTwentyMinAgo WHERE PRODUCTIONORDER = 'P5B157/1R'
-- Consolidate CTE_FromSapGoodReceive and CTE_SumGR into a single temp table
IF OBJECT_ID('tempdb..#FromSapGoodReceive') IS NOT NULL DROP TABLE #FromSapGoodReceive;

SELECT DISTINCT 
    a.ProductionOrder, 
    a.Grade,
    SUM(a.QuantityKG) AS SumGRKG,
    SUM(a.QuantityYD) AS SumGRYD,
    SUM(a.QuantityMR) AS SumGRMR
INTO #FromSapGoodReceive
FROM 
    [PCMS].[dbo].[FromSapGoodReceive] AS a
WHERE 
    a.DataStatus = 'O' 
    AND a.ProductionOrder <> '' 
    AND EXISTS (SELECT 1 FROM #TempProdOrder AS tpo WHERE a.ProductionOrder = tpo.ProductionOrder)
GROUP BY 
    a.ProductionOrder, 
    a.Grade;
-- Consolidate CTE_FromSapGoodReceive and CTE_SumGR into a single temp table
IF OBJECT_ID('tempdb..#FromSapMainBillBatch_SumBill') IS NOT NULL DROP TABLE #FromSapMainBillBatch_SumBill;
SELECT [ProductionOrder]  
		,SUM([QuantityKG]) AS SumBillKG
		,SUM([QuantityYD]) AS SumBillYD
		,SUM([QuantityMR]) AS SumBillMR
	into #FromSapMainBillBatch_SumBill
	FROM [PCMS].[dbo].[FromSapMainBillBatch] as a
	WHERE [DataStatus]  = 'O' and [ProductionOrder] <> '' 
    AND EXISTS (SELECT 1 FROM #TempProdOrder AS tpo WHERE a.ProductionOrder = tpo.ProductionOrder)
	GROUP BY [ProductionOrder]; 
WITH CTE_LotNumber AS (
    SELECT 
        CASE 
            WHEN [LotNumber] LIKE '% %' 
            THEN LEFT([LotNumber], CHARINDEX(' ', [LotNumber]) - 1) 
            ELSE [LotNumber] 
        END AS [LotNumber], 
        MAX(CoaApproveDate) AS CoaApproveDate
    FROM [QCMS].[dbo].[RequestOrder]
    WHERE CoaApproveDate IS NOT NULL
    GROUP BY LotNumber
),
CTE_ProductionOrder AS (
    SELECT 
        PrdNumber AS LotNumber, 
        MAX(CoaApproveDate) AS CoaApproveDate
    FROM [QCMS].[dbo].[RequestOrder] AS a
    WHERE CoaApproveDate IS NOT NULL
    AND EXISTS (
        SELECT 1 FROM #TempProdOrder AS tpo 
        WHERE a.PrdNumber = tpo.ProductionOrder
    )
    GROUP BY PrdNumber
),
CTE_RequestOrderAllLotNumber AS (
    -- Combine both results
    SELECT * FROM CTE_LotNumber
    UNION ALL
    SELECT * FROM CTE_ProductionOrder
) , 
CTE_DataFromSapGRFinish AS (
    SELECT [ProductionOrder], COUNT(GoodReceive) AS countGRFinish
    FROM [PCMS].[dbo].[TEMP_ProdWorkDate] as a
    WHERE GoodReceive is not null 
    AND EXISTS (SELECT 1 FROM #TempProdOrder AS tpo WHERE a.ProductionOrder = tpo.ProductionOrder)
    GROUP BY ProductionOrder
),  
CTE_FromSapCFM_LastCFM AS (
	SELECT a.[ProductionOrder] ,[CFMStatus]  , CFMNo ,CFMSendDate
	FROM [PCMS].[dbo].[FromSapCFM] as a
	INNER join (
		SELECT [ProductionOrder] , max([CFMNo]) as maxCFMNo
		FROM [PCMS].[dbo].[FromSapCFM]
		WHERE CFMNumber <> '' and
			  DataStatus = 'O'
		  AND EXISTS (SELECT 1 FROM #TempProdOrder tpo WHERE [FromSapCFM].[ProductionOrder] = tpo.ProductionOrder)
		group by [ProductionOrder]
	) as b on a.ProductionOrder = b.ProductionOrder AND A.CFMNo = B.maxCFMNo
	WHERE EXISTS (SELECT 1 FROM #TempProdOrder tpo WHERE a.ProductionOrder = tpo.ProductionOrder)
)  ,
CTE_InputStockLoad AS (
	SELECT [ProductionOrder] ,[SaleOrder] ,[SaleLine]  ,[StockLoad] 
	FROM [PCMS].[dbo].[InputStockLoad]  as a
	WHERE DataStatus = 'O' 
    AND EXISTS (SELECT 1 FROM #TempProdOrder AS tpo WHERE a.ProductionOrder = tpo.ProductionOrder)
) ,
CTE_ProductionOrderRPM AS (
	SELECT distinct  
		a.ProductionOrder AS ProductionOrderRPM
		, [ProductionOrderRP]  
		, b.SaleOrder
		, b.SaleLine
		, ISNULL( e1.CoaApproveDate ,e.CoaApproveDate ) as CoaApproveDateRPM
		, StockLoad as StockLoadRPM
		, countGRFinish AS countGRFinishRPM
		, D.Grade
		, F.[CFMSendDate]
		, F.CFMStatus 
	FROM [PCMS].[dbo].[ReplacedProdOrder] as a
	inner join [PCMS].[dbo].[FromSapMainProd] as b on 
			b.ProductionOrder = a.ProductionOrderRP and 
			b.[SaleOrder] = a.[SaleOrder] and 
			b.[SaleLine] = a.[SaleLine] and
			b.DataStatus = 'O' 
	left join CTE_DataFromSapGRFinish as C on A.ProductionOrder = C.ProductionOrder   
	LEFT JOIN (
		SELECT distinct ProductionOrder ,Grade
		from #FromSapGoodReceive
	) AS d ON A.ProductionOrder = d.ProductionOrder 
	left join CTE_RequestOrderAllLotNumber as e 
		on B.[LotNo] = e.[LotNumber] 
	left join CTE_RequestOrderAllLotNumber as e1 
		on B.[ProductionOrder] = e1.[LotNumber] 
	left join CTE_FromSapCFM_LastCFM as f on A.[ProductionOrder] = f.[ProductionOrder]  
	LEFT JOIN CTE_InputStockLoad AS g 
		on A.ProductionOrder = g.ProductionOrder and 
			B.SaleOrder = g.SaleOrder and 
			B.SaleLine = g.SaleLine    
	WHERE a.DataStatus = 'O'  and 
			EXISTS ( 
				SELECT ProductionOrder 
				FROM #TempProdOrder AS tpo 
				WHERE A.ProductionOrder = tpo.ProductionOrder 
			) 
) ,
CTE_ProductionOrderMain AS ( 
	SELECT DISTINCT 
		a.ProductionOrder 
	, a.Volumn
	, a.UserStatus
	, a.SaleOrder
	, a.SaleLine
	, a.LotNo
	, ProductionOrderRPM
	, [ProductionOrderRP] 
	, CoaApproveDateRPM , StockLoadRPM,  countGRFinishRPM, H.GRADE AS GradeRPM
	, CFMStatus AS CFMStatusRPM
	, CFMSendDate AS CFMSendDateRPM
	, gs.[Grade], a.[ChangeDate] 
	, right(a.ProductionOrder, 1) as LastProductionOrderCharacter
	, SUBSTRING(  a.ProductionOrder , 7, 1)  AS PositionSeven 
	, CASE
		WHEN BS.ProductionOrder IS NOT NULL THEN 'Y'
		ELSE 'N'
		END AS IsHireWorkHaveBill 
	, case
		WHEN Unit = 'KG' THEN SumBillKG   
		WHEN Unit = 'YD' THEN SumBillYD  
		ELSE SumBillMR
		end AS SumBill
	, case
		WHEN Unit = 'KG' THEN SumGRKG   
		WHEN Unit = 'YD' THEN SumGRYD  
		ELSE SumGRMR
		end AS SumGR
	from [PCMS].[dbo].[FromSapMainProd] as a
	LEFT JOIN #FromSapMainBillBatch_SumBill AS BS ON A.ProductionOrder = BS.ProductionOrder 
	LEFT JOIN #FromSapGoodReceive AS GS ON A.ProductionOrder = GS.ProductionOrder 
	LEFT JOIN CTE_ProductionOrderRPM as h on a.ProductionOrder = h.ProductionOrderRP
	where a.SaleOrder <> '' and 
		EXISTS ( 
			SELECT ProductionOrder 
			FROM #TempProdOrder AS tpo 
			WHERE A.ProductionOrder = tpo.ProductionOrder 
		) 
) ,
CTE_SaleRorJangSong AS (
	SELECT a.[SaleOrder] ,a.[SaleLine] ,[SaleUnit] ,[DistChannel] ,ChangeDate, count (b.CustomerNo) as isRorJangSong
	FROM [PCMS].[dbo].[FromSapMainSale] AS A
	left join [PCMS].[dbo].[ConfigCustomerEX] AS B ON A.CustomerNo = B.CustomerNo
	where SaleOrder <> '' AND A.[DataStatus] = 'O'
	  AND EXISTS (
	      SELECT 1 FROM [PCMS].[dbo].[FromSapMainProd] p
	      JOIN #TempProdOrder tpo ON p.ProductionOrder = tpo.ProductionOrder
	      WHERE p.SaleOrder = a.SaleOrder AND p.SaleLine = a.SaleLine AND p.DataStatus = 'O'
	  )
	group by [SaleOrder] ,[SaleLine],[SaleUnit],[DistChannel]  ,ChangeDate
)
select  distinct a.[ProductionOrder]
		, a.Saleorder
		, SaleLine
		, SumBill
		, CoaApproveDate
		, isRorJangSong
		, StockLoad
		, countGRFinish
	    , a.GRADE 
		, a.ProductionOrderRPM  
		, a.Volumn 
		, a.UserStatusCal
		, UserStatus AS UserStatusCalFrom
		, CASE  
			when ( a.GradeRPM = 'A' ) AND a.countGRFinishRPM = 1 AND a.SumBill  IS NULL  THEN 
				CASE 
					WHEN a.CFMStatusRPM = '' 
						AND CFMSendDateRPM IS NOT NULL --#CH001
						THEN 'รอตอบ CFM' 
					WHEN a.CFMStatusRPM = 'N'  
						AND CFMSendDateRPM IS NOT NULL --#CH001
						AND a.CoaApproveDateRPM IS NULL THEN 'รอสรุปจาก QA'   
					WHEN a.CFMStatusRPM = 'Y'  
						AND CFMSendDateRPM IS NOT NULL --#CH001
						AND CoaApproveDate IS NULL THEN 'รอตอบ CFM ตัวแทน' 
					WHEN a.CFMStatusRPM = 'Y'  
						AND CFMSendDateRPM IS NOT NULL --#CH001
						AND a.CoaApproveDateRPM IS NOT NULL THEN 'รอแจ้งส่ง'     
					ELSE a.UserStatusCal
				end 
			ELSE a.UserStatusCal 
			END AS UserStatusCalRP 
INTO #tempUCAL
from (
	SELECT   
	  a.[ProductionOrder]
	, a.Saleorder
	, a.SaleLine 
	, SumBill 
	, ISNULL( e1.CoaApproveDate ,e.CoaApproveDate ) as CoaApproveDate
	, c.isRorJangSong
	, g.StockLoad
	, b.countGRFinish
	, a.GRADE
	, a.ProductionOrderRPM  
	, a.CoaApproveDateRPM 
	, a.StockLoadRPM
	, a.countGRFinishRPM
	, a.GradeRPM
	, a.Volumn
	, a.CFMStatusRPM
	, a.CFMSendDateRPM
	, a.SumGR
	, a.UserStatus
	, CASE
		when a.UserStatus in 
			( 'ยกเลิก' , 'ปิดเพื่อแก้ไข' , 'รับเข้าST.(P/S)' , 'ตัดเกรดZ' , 'Over' , 'ขายเหลือ' , 'Lab' , 'HOLD,รอโอน' , 'รอตอบ CFM' ) or 
			 f.CFMStatus IN ('C' )  
		THEN a.UserStatus 
		WHEN (a.[grade] IN ('A', 'C')) 
			 AND b.countGRFinish = 1 
			 AND [DistChannel] IN ('DM', 'EX') 
			 AND a.[ProductionOrderRP] IS NULL 
		THEN 
			CASE 
				WHEN f.CFMStatus = '' 
					AND a.SumBill IS NULL 
					AND f.CFMSendDate is not null--#CH001
					THEN 'รอตอบ CFM'
				WHEN f.CFMStatus IN ('N', 'N/Y', 'N,Y') 
					 AND A.[LastProductionOrderCharacter] <> 'I'
					 AND ISNULL(e1.CoaApproveDate ,e.CoaApproveDate) IS NULL 
					 AND a.SumBill IS NULL 
					 AND f.CFMSendDate is not null--#CH001
					THEN 'รอสรุปจาก QA' 
				WHEN CFMStatus IN ('Y', 'Y/N', 'Y,N') 
					 AND A.[LastProductionOrderCharacter] <> 'I'
					 AND ISNULL(e1.CoaApproveDate ,e.CoaApproveDate) IS NULL 
					 AND a.SumBill IS NULL 
					 AND c.isRorJangSong = 1 
					 AND f.CFMSendDate is not null --#CH001 
					 then 'รอตอบ CFM ตัวแทน' 
				WHEN f.CFMStatus IN ('Y', 'Y/N', 'Y,N') 
					 AND A.[LastProductionOrderCharacter] <> 'I'
					 AND ISNULL(e1.CoaApproveDate ,e.CoaApproveDate) IS NULL 
					 AND a.SumBill IS NULL 
					 AND f.CFMSendDate is not null 
					THEN 'รอCOAลูกค้าOKสี' 
				WHEN f.CFMStatus IN ('Y', 'Y/N', 'Y,N') 
					AND ISNULL(e1.CoaApproveDate ,e.CoaApproveDate)  IS NOT NULL 
					AND f.CFMSendDate is not null --#CH001
					THEN 
					CASE 
						WHEN a.SumBill IS NULL THEN 
							CASE 
								WHEN (g.StockLoad IS NOT NULL OR g.StockLoad <> '') 
									THEN 'รอเปิดบิล' 
								WHEN c.[DistChannel] = 'DM' THEN 
									CASE  
										WHEN c.isRorJangSong = 1 THEN 'รอแจ้งส่ง'  
										ELSE 'รอขาย' 
									end      
								WHEN c.[DistChannel] = 'EX' THEN 'รอแจ้งส่ง'   
							end      
						WHEN a.SumGR <= SumBill THEN 'ขายแล้ว'  
						WHEN a.SumBill > 0 THEN 'ขายแล้วบางส่วน'  
						ELSE a.UserStatus
					end 
				WHEN  a.SumBill IS NOT NULL THEN 
					CASE  
						WHEN a.SumGR <= SumBill THEN 'ขายแล้ว'  
						WHEN a.SumBill > 0 THEN 'ขายแล้วบางส่วน'
						ELSE a.UserStatus   
					end 
				ELSE a.UserStatus
			end 
		WHEN a.PositionSeven = 'H' and ( a.[UserStatus] = 'รอขาย' ) and a.IsHireWorkHaveBill = 'Y' THEN 'ขายแล้ว'   
		WHEN a.[grade] = 'Z' THEN 'ตัดเกรดZ'   
		ELSE a.UserStatus END AS UserStatusCal  
 	FROM CTE_ProductionOrderMain as a
	left join CTE_SaleRorJangSong as c on a.SaleOrder = c.SaleOrder and 
			                              a.SaleLine = c.SaleLine 
	left join CTE_DataFromSapGRFinish as b on a.ProductionOrder = b.ProductionOrder     
	left join CTE_RequestOrderAllLotNumber as e on a.[LotNo] = e.[LotNumber] 
	left join CTE_RequestOrderAllLotNumber as e1 on B.[ProductionOrder] = e1.[LotNumber] 
	left join CTE_FromSapCFM_LastCFM as f on a.[ProductionOrder] = f.[ProductionOrder]  
	LEFT JOIN CTE_InputStockLoad AS g on a.ProductionOrder = g.ProductionOrder and 
										 a.SaleOrder = g.SaleOrder and 
										 a.SaleLine = g.SaleLine  
)  AS A  
 
  --select * from #tempUCAL WHERE PRODUCTIONORDER = 'P5B157/1R'
  --select * from #tempProdTwentyMinAgo WHERE PRODUCTIONORDER = 'P5B157/1R'
	UPDATE b SET  
	   b.[DataStatus]           = 'X' 
	FROM  [PCMS].[dbo].[TEMP_UserStatusAuto]    as b
	inner join  #tempUCAL as a on b.[ProductionOrder] = a.[ProductionOrder] 
	where b.[DataStatus] = 'O'

	-- FIX 2026-11-04 (ป้องกันล่วงหน้า): จับคู่ด้วยคีย์เต็ม (PO + Grade + SaleOrder + SaleLine) และไม่เขียนทับคอลัมน์คีย์
	-- เดิมจับแค่ (PO + Grade) → ถ้า PO อยู่หลาย sale line และ Grade เดียวกัน แถวทั้งหมดจะถูกเขียนทับเป็นค่าของ line ใดก็ได้
	UPDATE b SET
	   b.[UserStatusCalFrom]  = CAST(a.[UserStatusCalFrom] AS nvarchar(50))
	  ,b.[UserStatusCal]      = CAST(a.[UserStatusCal] AS nvarchar(50))
	  ,b.[ProductionOrderRPM] = CAST(a.[ProductionOrderRPM] AS nvarchar(20))
	  ,b.[Volumn]             = CAST(a.[Volumn] AS decimal(13,3))
	  ,b.[UserStatusCalRP]    = CAST(a.[UserStatusCalRP] AS nvarchar(50))
	  ,b.[DataStatus]         = 'O'
	  ,b.[ChangeDate]         = @dateTime
	FROM [PCMS].[dbo].[TEMP_UserStatusAuto] as b
	INNER JOIN (
	    SELECT ProductionOrder, SaleOrder, SaleLine, Grade, UserStatusCalFrom, UserStatusCal,
	           ProductionOrderRPM, Volumn, UserStatusCalRP,
	           ROW_NUMBER() OVER (PARTITION BY ProductionOrder, SaleOrder, SaleLine, Grade ORDER BY ProductionOrder) AS rn
	    FROM #tempUCAL
	) as a
	    ON b.[ProductionOrder] = a.[ProductionOrder]
	    AND (b.Grade = a.Grade OR (b.Grade IS NULL AND a.Grade IS NULL))
	    AND (b.SaleOrder = a.SaleOrder OR (b.SaleOrder IS NULL AND a.SaleOrder IS NULL))
	    AND (b.SaleLine  = a.SaleLine  OR (b.SaleLine  IS NULL AND a.SaleLine  IS NULL))
	    AND a.rn = 1
	-- FIX 2026-11-09: คีย์เดียวอาจมีหลายแถว (เช่น X+O จากการล้างซ้ำ) — เดิม UPDATE พลิก "ทุกแถว" ของคีย์กลับเป็น 'O' ทำให้ซ้ำคืนชีพ
	-- และจะชน unique index ถ้ามี · เลือกฟื้นเฉพาะแถว Id สูงสุดของคีย์ (แถวอื่นของคีย์เดียวกันคงสถานะ X)
	WHERE b.Id = (
	    SELECT MAX(x.Id) FROM [PCMS].[dbo].[TEMP_UserStatusAuto] AS x
	    WHERE x.[ProductionOrder] = b.[ProductionOrder]
	      AND (x.Grade     = b.Grade     OR (x.Grade     IS NULL AND b.Grade     IS NULL))
	      AND (x.SaleOrder = b.SaleOrder OR (x.SaleOrder IS NULL AND b.SaleOrder IS NULL))
	      AND (x.SaleLine  = b.SaleLine  OR (x.SaleLine  IS NULL AND b.SaleLine  IS NULL))
	)

-- FIX 2026-11-09: ครอบ "ตรวจแล้ว INSERT" ด้วย applock + transaction สั้นๆ — สองรอบที่เริ่มพร้อมกัน (CreateDate ตรงกันถึง ms) เคยเช็ค NOT EXISTS ผ่านทั้งคู่แล้ว insert ซ้ำ
DECLARE @lockResult int;
BEGIN TRY
BEGIN TRAN;
EXEC @lockResult = sp_getapplock @Resource = 'spd_UpsertToTEMP_UserStatusOnWeb_insert', @LockMode = 'Exclusive', @LockOwner = 'Transaction', @LockTimeout = 120000;
IF @lockResult < 0 RAISERROR('spd_UpsertToTEMP_UserStatusOnWeb: ขอ applock ไม่ได้ (timeout)', 16, 1);
;WITH UCAL_Distinct AS (
    SELECT 
        ProductionOrder,
        SaleOrder,
        SaleLine,
        Grade,
        UserStatusCalFrom,
        UserStatusCal,
        ProductionOrderRPM,
        Volumn,
        UserStatusCalRP
    FROM (
        SELECT *,
               ROW_NUMBER() OVER (
                   PARTITION BY ProductionOrder, SaleOrder, SaleLine, Grade
                   ORDER BY ProductionOrder -- หรือ order ตาม CreateDate ถ้ามี
               ) AS rn
        FROM #tempUCAL
    ) t
    WHERE rn = 1
)
INSERT INTO [PCMS].[dbo].[TEMP_UserStatusAuto] (
    ProductionOrder, SaleOrder, SaleLine, Grade,
    UserStatusCalFrom, UserStatusCal, ProductionOrderRPM,
    Volumn, UserStatusCalRP, CreateDate, ChangeDate
)
SELECT
    CAST(a.[ProductionOrder] AS nvarchar(20)),
    CAST(a.[SaleOrder] AS nvarchar(20)),
    CAST(a.[SaleLine] AS nvarchar(20)),
    CAST(a.[Grade] AS nvarchar(30)),
    CAST(a.[UserStatusCalFrom] AS nvarchar(50)),
    CAST(a.[UserStatusCal] AS nvarchar(50)),
    CAST(a.[ProductionOrderRPM] AS nvarchar(20)),
    CAST(a.[Volumn] AS decimal(13,3)),
    CAST(a.[UserStatusCalRP] AS nvarchar(50)),
    @dateTime, @dateTime
FROM UCAL_Distinct AS a
WHERE a.ProductionOrder <> ''
  AND NOT EXISTS (
        SELECT 1
        FROM [PCMS].[dbo].[TEMP_UserStatusAuto] AS d
        WHERE d.[ProductionOrder] = a.[ProductionOrder]
          AND (d.Grade    = a.Grade    OR (d.Grade    IS NULL AND a.Grade    IS NULL))
          AND (d.SaleOrder = a.SaleOrder OR (d.SaleOrder IS NULL AND a.SaleOrder IS NULL))
          AND (d.SaleLine  = a.SaleLine  OR (d.SaleLine  IS NULL AND a.SaleLine  IS NULL))
  );
COMMIT TRAN;
END TRY
BEGIN CATCH
	IF @@TRANCOUNT > 0 ROLLBACK TRAN;
	THROW;
END CATCH

 END
GO
-- (B2) ตรวจแถว O ซ้ำที่เหลือ
SELECT ProductionOrder, SaleOrder, SaleLine, Grade, COUNT(*) AS rows_O, MIN(Id) AS minId, MAX(Id) AS maxId
FROM [PCMS].[dbo].[TEMP_UserStatusAuto] WHERE DataStatus = 'O'
GROUP BY ProductionOrder, SaleOrder, SaleLine, Grade HAVING COUNT(*) > 1;
-- ปิด (X) แถวที่ไม่ใช่ Id สูงสุดของแต่ละคีย์ — ไม่ลบจริง · ยกเลิก comment เมื่อพร้อม
/*
;WITH d AS (
  SELECT Id, ROW_NUMBER() OVER (PARTITION BY ProductionOrder, SaleOrder, SaleLine, Grade ORDER BY Id DESC) rn
  FROM [PCMS].[dbo].[TEMP_UserStatusAuto] WHERE DataStatus = 'O')
UPDATE t SET DataStatus = 'X', ChangeDate = GETDATE()
FROM [PCMS].[dbo].[TEMP_UserStatusAuto] t INNER JOIN d ON d.Id = t.Id WHERE d.rn > 1;
*/
GO
-- ตรวจผลหลัง (A2)+(B2): ต้องได้ 0 และเฝ้าดูซ้ำทุกวัน (ค่าที่ควรเป็น 0 ตลอด)
SELECT COUNT(*) AS dup_key_groups_O FROM (
  SELECT 1 x FROM [PCMS].[dbo].[TEMP_UserStatusAuto] WHERE DataStatus = 'O'
  GROUP BY ProductionOrder, SaleOrder, SaleLine, Grade HAVING COUNT(*) > 1) t;
GO
-- (C2) [ไม่บังคับ — แนะนำรอ] ทำเมื่อ dup_key_groups_O = 0 ต่อเนื่อง และตรวจแล้วว่าทุกผู้เขียนตารางนี้ใช้ QUOTED_IDENTIFIER ON
/*
SET QUOTED_IDENTIFIER ON;
CREATE UNIQUE NONCLUSTERED INDEX UX_TEMP_UserStatusAuto_O
ON [PCMS].[dbo].[TEMP_UserStatusAuto] (ProductionOrder, SaleOrder, SaleLine, Grade)
WHERE DataStatus = 'O';
*/
