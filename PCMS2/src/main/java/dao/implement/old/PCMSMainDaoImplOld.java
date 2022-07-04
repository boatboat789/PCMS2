package dao.implement.old;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dao.PCMSMainDao;
import entities.CFMDetail;
import entities.ColumnHiddenDetail;
import entities.DyeingDetail;
import entities.FinishingDetail;
import entities.InputDateDetail;
import entities.InspectDetail;
import entities.NCDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import entities.PODetail;
import entities.PackingDetail;
import entities.PresetDetail;
import entities.ReceipeDetail;
import entities.SaleDetail;
import entities.SaleInputDetail;
import entities.SendTestQCDetail;
import entities.WaitTestDetail;
import entities.WorkInLabDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

public class PCMSMainDaoImplOld implements PCMSMainDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	private String select = 
			  " a.SaleOrder "
			+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
			+ "		WHEN 0 THEN ''  \r\n"  
			+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
			+ "		END AS [SaleLine] "

			+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" 
			+ "    		 WHEN 0 THEN ''   \r\n"
			+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+a.[SaleFullName]\r\n"
			+ "    		 END AS [SaleFullName]   \r\n" 
			+ "    ,a.DesignFG\r\n" 
			+ "   ,a.ArticleFG\r\n" 
			+ "   ,a.DistChannel\r\n"
			+ "   ,a.Color\r\n" 
			+ "   ,a.ColorCustomer,a.SaleQuantity\r\n"
			+ "	  ,( a.SaleQuantity - a.RemainQuantity ) as BillQuantity\r\n" 
			+ "   ,a.SaleUnit\r\n" 
//			+  "  ,CASE  \r\n"
//			+ "		  WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"  
//			+ "	      WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"  
//			+ "		  ELSE 'รอจัด Lot'  \r\n"
//			+ "		  END AS [ProductionOrder] \r\n"
//			+ "   ,CASE  \r\n"
//			+ "		  WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]   \r\n"
//			+ "		  ELSE b.[ProductionOrderFilter]\r\n"
//			+ "		  END AS [ProductionOrder]  \r\n"
			+ "   , b.ProductionOrder"
			+ "   ,b.TotalQuantity,b.GreigeInDate\r\n"    
			+ "	  , CASE WHEN (b.GreigeInDate is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(b.GreigeInDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(GreigeInDate)AS VARCHAR(2))  END AS GreigeInDate \r\n"
			+ "	  ,	UCAL.UserStatusCal as UserStatus\r\n"
			+ "   ,b.LabStatus,a.DueDate\r\n" 
			+ "		  , CASE WHEN (i.WorkDate is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(i.WorkDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(i.WorkDate)AS VARCHAR(2))  END AS DyePlan  \r\n"
			+ "		 , CASE WHEN (j.DyeActual is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(j.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH(j.DyeActual)AS VARCHAR(2))  END AS DyeActual \r\n"
			+ "		 , CASE WHEN (k.Dryer is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(k.Dryer) AS VARCHAR(2)) + '/' +   CAST(MONTH(k.Dryer)AS VARCHAR(2))  END AS Dryer \r\n"
			+ "		 , CASE WHEN (l.Finishing is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(l.Finishing) AS VARCHAR(2)) + '/' +   CAST(MONTH(l.Finishing)AS VARCHAR(2))  END AS Finishing \r\n"
			+ "		 , CASE WHEN (m.Inspectation is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(m.Inspectation) AS VARCHAR(2)) + '/' +   CAST(MONTH(m.Inspectation)AS VARCHAR(2))  END AS Inspectation  \r\n"
			+ "		 , CASE WHEN (n.Prepare is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(n.Prepare) AS VARCHAR(2)) + '/' +   CAST(MONTH(n.Prepare)AS VARCHAR(2))  END AS Prepare \r\n"
			+ "	  , CASE WHEN (o.Preset is NULL) THEN null \r\n" 
			+ "	    ELSE CAST(DAY(o.Preset) AS VARCHAR(2)) + '/' +   CAST(MONTH(o.Preset)AS VARCHAR(2))  END AS Preset   \r\n  "
			+ "	  , CASE WHEN (p.Relax is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(p.Relax) AS VARCHAR(2)) + '/' +   CAST(MONTH(p.Relax)AS VARCHAR(2))  END AS Relax   \r\n  " 
			+ "	  ,CAST(DAY(d.CFMAnswerDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(d.CFMAnswerDate)AS VARCHAR(2))   as CFMDateActual\r\n" 
//			+ ",   CASE  \r\n"
//    		+ "		  WHEN g.[ProductionOrder] is not null THEN CAST(DAY(g.CFMPlanDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(g.CFMPlanDate )AS VARCHAR(2))     \r\n"
//    		+ "		  ELSE CAST(DAY(f.CFMPlanDateOne) AS VARCHAR(2)) + '/' +   CAST(MONTH(f.CFMPlanDateOne )AS VARCHAR(2))     \r\n"
//    		+ "		  END AS CFMPlanDate \r\n"
    		+ "   ,CAST(DAY(g.CFMPlanDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(g.CFMPlanDate )AS VARCHAR(2))  AS CFMPlanDate \r\n "
			+ "   ,CASE  \r\n"
    		+ "		  WHEN h.[ProductionOrder] is not null \r\n"
    		+ "       THEN CAST(DAY(h.DeliveryDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(h.DeliveryDate)AS VARCHAR(2))     \r\n"
    		+ "		  ELSE CAST(DAY(b.CFTYPE) AS VARCHAR(2)) + '/' +   CAST(MONTH(b.CFTYPE)AS VARCHAR(2))     \r\n"
    		+ "		  END AS DeliveryDate \r\n" 
    		+ "	  , CASE WHEN (z.LotShipping is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(z.LotShipping) AS VARCHAR(2)) + '/' +   CAST(MONTH(z.LotShipping)AS VARCHAR(2))   END AS LotShipping \r\n"
			+ "	  ,b.LabNo,a.CustomerName,a.CustomerShortName,a.SaleNumber\r\n" 
			+ "	  ,a.SaleCreateDate,b.PrdCreateDate\r\n" 
			+ "	  ,a.MaterialNo,a.DeliveryStatus,a.SaleStatus\r\n" 
			+ "   ,[LotNo] ,a.ShipDate,j.DyeStatus \r\n";         
	private String selectTwo = 
//			  "  CASE  \r\n"
//			+ "		  WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"  
//			+ "		  ELSE 'รอจัด Lot'  \r\n"
//			+ "		  END AS [ProductionOrder] \r\n"     
		     " b.[ProductionOrder]"
			+ "   ,ColorCustomer"
		    + "   ,[LotNo],[Batch],[LabNo]\r\n"
			+ "	  ,[PrdCreateDate],a.[DueDate],a.[SaleOrder]\r\n"
			+ "	  ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n" 
			+ "		WHEN 0 THEN ''  \r\n"
			+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
			+ "		END AS [SaleLine] ,PurchaseOrder,a.ArticleFG,a.DesignFG\r\n"
			+ "	  ,CustomerName,CustomerShortName,Shade,BookNo,Center\r\n"
			+ "	  ,MaterialNo,Volumn,SaleUnit,Unit as STDUnit\r\n" 
			+ "	  ,Color,PlanGreigeDate,RefPrd,GreigeInDate\r\n"
			+ "	  ,BCAware,OrderPuang,UserStatus,LabStatus\r\n"
			//			+ "	  ,'' as CFMPlanDate,'' as DeliveryDate"
			+ "   , g.CFMPlanDate AS CFMPlanDate \r\n"
//			+ ",   CASE  \r\n"
//			+ "		  WHEN g.CFMPlanDate is not null THEN g.CFMPlanDate  \r\n"
//			+ "		  ELSE f.CFMPlanDateOne   \r\n"
//			+ "		  END AS CFMPlanDate \r\n"
			//+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as CFMDateActual\r\n"
			+ "   ,CASE  \r\n"
			+ "		  WHEN h.DeliveryDate is not null THEN h.DeliveryDate  \r\n"  
			+ "		  ELSE b.CFTYPE  \r\n"
			+ "		  END AS DeliveryDate \r\n"
			+ "   ,BCDate,RemarkOne\r\n"   
			+ "	  ,RemarkTwo,RemarkThree,RemAfterCloseOne,RemAfterCloseTwo\r\n"
			+ "	  ,RemAfterCloseThree \r\n" 
			+ "   ,GreigeArticle \r\n " 
			+ "   ,GreigeDesign \r\n";
	private String selectPO = "  [ProductionOrder],[RollNo],[QuantityKG]\r\n"
			+ "      ,[QuantityMR],[CreateDate] ,[PurchaseOrder]\r\n"
			+ "		,CASE PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' ‘')\r\n"
			+ "		 WHEN 0 THEN ''  \r\n"    
			+ "		 ELSE SUBSTRING( [PurchaseOrderLine] , PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' '), LEN( [PurchaseOrderLine] ) )\r\n"
			+ "       END AS [PurchaseOrderLine] \r\n"      
			+ "      ,[RequiredDate],[PurchaseOrderDate],[PODefault]\r\n"
			+ "      ,[POLineDefault],[POPostingDateDefault],[DataStatus] \r\n";
	private String createTempUCAL = ""
//			+ " If(OBJECT_ID('tempdb..#TempUCAL') Is Not Null)\r\n"
//			+ "	begin\r\n"
//			+ "		Drop Table #TempUCAL\r\n"
//			+ "	end ;\r\n"
			+ "   SELECT distinct -- a.[ProductionOrder]  ,[UserStatus]  ,d.[Grade]  ,countGRFinish ,isAnita ,CoaApproveDate  ,[CFMStatus],[DistChannel],\r\n"
			+ "	  a.[ProductionOrder],  \r\n"
			+ "	  CASE \r\n"
			+ "	    when --a.UserStatus = 'ขายแล้ว' or   \r\n"
			+ "			 a.UserStatus = 'ยกเลิก' or a.UserStatus = 'ปิดเพื่อแก้ไข' or  a.UserStatus = 'รับเข้าST.(P/S)' or\r\n"
			+ "			 a.UserStatus = 'ตัดเกรดZ' or a.UserStatus = 'Over' or a.UserStatus = 'ขายเหลือ' or a.UserStatus = 'Lab' \r\n"
			+ "		THEN a.UserStatus\r\n"
			+ "		when ( d.[grade] = 'A' OR d.[grade] = 'C' ) AND countGRFinish = 1 and [DistChannel] = 'DM' THEN \r\n"
			+ "			CASE \r\n"
			+ "				WHEN CFMStatus = '' THEN 'รอตอบ CFM' \r\n"
			+ "				WHEN CFMStatus = 'N' AND CoaApproveDate IS NULL THEN 'รอสรุปจาก QA'  \r\n"
			+ "				--WHEN CFMStatus = 'Y' AND CoaApproveDate IS NULL THEN 'รอ COA ลูกค้า ok สี'  \r\n"
			+ "				WHEN isAnita = 1 and CFMStatus = 'Y' AND CoaApproveDate IS NULL THEN 'รอตอบ CFM ตัวแทน'\r\n"
			+ "				WHEN isAnita is null and CFMStatus = 'Y' AND CoaApproveDate IS NULL THEN 'รอ COA ลูกค้า ok สี'   \r\n"
			+ "				WHEN CFMStatus = 'Y' AND CoaApproveDate IS NOT NULL THEN \r\n"
			+ "					CASE \r\n"
			+ "						WHEN SumBill IS NULL THEN \r\n"
			+ "							CASE \r\n"
			+ "								WHEN ( StockLoad is not null or StockLoad <> '' ) THEN 'รอเปิดบิล'    \r\n"
			+ "								WHEN isAnita = 1 THEN 'รอแจ้งส่ง'  \r\n"
			+ "								ELSE 'รอขาย' \r\n"
			+ "							end      \r\n"
			+ "						WHEN Volumn <= SumBill THEN 'ขายแล้ว'  \r\n"
			+ "						WHEN SumBill > 0 THEN 'ขายแล้วบางส่วน'  \r\n"
			+ "						ELSE a.UserStatus\r\n"
			+ "					end \r\n"
			+ "				ELSE a.UserStatus\r\n"
			+ "			end\r\n"
			+ "		when ( d.[grade] = 'A' OR d.[grade] = 'C' ) AND countGRFinish = 1 and [DistChannel] = 'EX' THEN \r\n"
			+ "			CASE \r\n"
			+ "				WHEN CFMStatus = '' THEN 'รอตอบ CFM' \r\n"
			+ "				WHEN CFMStatus = 'N' AND CoaApproveDate IS NULL THEN 'รอสรุปจก QA'  \r\n"
			+ "				WHEN isAnita = 1 and CFMStatus = 'Y' AND CoaApproveDate IS NULL THEN 'รอตอบ CFM ตัวแทน'\r\n"
			+ "				WHEN isAnita is null and CFMStatus = 'Y' AND CoaApproveDate IS NULL THEN 'รอ COA ลูกค้า ok สี'   \r\n"
			+ "				WHEN CFMStatus = 'Y' AND CoaApproveDate IS NOT NULL THEN \r\n"
			+ "					CASE \r\n"
			+ "						WHEN SumBill IS NULL THEN  \r\n"
			+ "							CASE \r\n"
			+ "								WHEN ( StockLoad is not null or StockLoad <> '' ) THEN 'รอเปิดบิล'    \r\n"
			+ "								ELSE 'รอแจ้งส่ง'  \r\n"
			+ "							end \r\n"
			+ "						WHEN Volumn <= SumBill THEN 'ขายแล้ว'  \r\n"
			+ "						WHEN SumBill > 0 THEN 'ขายแล้วบางส่วน'\r\n"
			+ "						ELSE a.UserStatus   \r\n"
			+ "					end \r\n"
			+ "				ELSE a.UserStatus\r\n"
			+ "				end\r\n"
			+ "			when d.[grade] = 'Z' THEN 'ตัดเกรด Z'  \r\n"
			+ "		ELSE a.UserStatus END AS UserStatusCal\r\n"
			+ "		into #TempUCAL\r\n"
			+ "	  FROM ( select a.ProductionOrder ,a.Volumn,a.UserStatus,a.SaleOrder,SaleLine,a.LotNo,\r\n"
			+ "			LabNo,ArticleFG,a.DesignFG ,PrdCreateDate, \r\n"
			+ "					case\r\n"
			+ "						WHEN Unit = 'KG' THEN SumBillKG   \r\n"
			+ "						WHEN Unit = 'YD' THEN SumBillYD  \r\n"
			+ "						ELSE SumBillMR\r\n"
			+ "					end AS SumBill\r\n"
			+ "			 from [PCMS].[dbo].[FromSapMainProd] as a\r\n"
			+ "			 LEFT JOIN ( SELECT [ProductionOrder]  \r\n"
			+ "						  ,SUM([QuantityKG]) AS SumBillKG\r\n"
			+ "						  ,SUM([QuantityYD]) AS SumBillYD\r\n"
			+ "						  ,SUM([QuantityMR]) AS SumBillMR\r\n"
			+ "					  FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			+ "					  WHERE [DataStatus]  = 'O'\r\n"
			+ "					  GROUP BY [ProductionOrder]  )AS BS ON A.ProductionOrder = BS.ProductionOrder\r\n"
			+ "         where SaleOrder <> '' \r\n"
			+ "			)as a\r\n"
			+ "   inner join (   SELECT a.[SaleOrder] ,a.[SaleLine] ,[SaleUnit] ,[DistChannel] , isAnita ,MaterialNo\r\n"
			+ "				,SaleCreateDate,SaleNumber, CustomerName,CustomerShortName,Division,DueDate ,DeliveryStatus\r\n"
			+ "				,SaleStatus \r\n"
			+ "					  FROM [PCMS].[dbo].[FromSapMainSale] AS A\r\n"
			+ "					  left join (  SELECT [SaleOrder] ,[SaleLine] ,count (b.CustomerNo) as isAnita \r\n"
			+ "									 FROM [PCMS].[dbo].[FromSapMainSale] AS A \r\n"
			+ "									 inner join [PCMS].[dbo].[ConfigCustomerEX] AS B ON A.CustomerNo = B.CustomerNo \r\n"
			+ "                                  where SaleOrder <> '' \r\n"
			+ "									 group by [SaleOrder] ,[SaleLine],[SaleUnit],[DistChannel]  ) as c on a.SaleOrder = c.SaleOrder and a.SaleLine = c.SaleLine\r\n"
			+ "                where a.SaleOrder <> '' \r\n"
			+ "			  ) as c on a.SaleOrder = c.SaleOrder and a.SaleLine = c.SaleLine \r\n"
			+ "	  left join ( SELECT [ProductionOrder] \r\n"
			+ "						,count([OperationEndTime]) as countGRFinish\r\n"
			+ "				  FROM [PPMM].[dbo].[DataFromSap]\r\n"
			+ "				  WHERE OPERATION = '230' AND OperationStatus = 'PROCESS DONE'\r\n"
			+ "				  group by ProductionOrder ) as b on a.ProductionOrder = b.ProductionOrder   \r\n"
			+ "   left join (SELECT ProductionOrder , Grade \r\n"
			+ "				 from [PCMS].[dbo].[FromSapMainGrade] \r\n"
			+ "				 where DataStatus = 'O' ) as d on a.ProductionOrder = d.ProductionOrder	 left join ( SELECT  [LotNumber]  ,max(CoaApproveDate)  as CoaApproveDate\r\n"
			+ "					  FROM [QCMS].[dbo].[RequestOrder]\r\n"
			+ "					  where CoaApproveDate is not null  \r\n"
			+ "					  group by LotNumber) as e on a.[LotNo] = e.[LotNumber]\r\n"
			+ "	 left join ( SELECT a.[ProductionOrder] ,[CFMStatus]  , CFMNo \r\n"
			+ "					  FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
			+ "					  INNER join (  SELECT [ProductionOrder] , max([CFMNo]) as maxCFMNo \r\n"
			+ "									  FROM [PCMS].[dbo].[FromSapCFM]\r\n"
			+ "                                  WHERE [CFMNumber] <> '' \r\n"
			+ "									  group by [ProductionOrder]   \r\n"
			+ "								) as b on a.ProductionOrder = b.ProductionOrder AND A.CFMNo = B.maxCFMNo\r\n"
			+ "					  ) as f on a.[ProductionOrder] = f.[ProductionOrder]  \r\n"
			+ "	   LEFT JOIN( SELECT [ProductionOrder] ,[SaleOrder] ,[SaleLine]  ,[StockLoad] \r\n"
			+ "				FROM [PCMS].[dbo].[InputStockLoad] \r\n"
			+ "				WHERE DataStatus = 'O' ) AS g on a.ProductionOrder = g.ProductionOrder and a.SaleOrder = c.SaleOrder and a.SaleLine = c.SaleLine  \r\n" 
			+ "  \r\n";
	private String createTempG = "SELECT distinct a.[ProductionOrder]   \r\n"
			+ "	  		, case \r\n"
			+ "				when EndDateName = 'Saturday' then DATEADD(DAY, 9, a.[WorkDate])\r\n"
			+ "				when EndDateName is not null then DATEADD(DAY, 8, a.[WorkDate])\r\n"
			+ "				else null \r\n"
			+ "				end as CFMPlanDate  \r\n"
			+ "  into #TempG \r\n"
			+ "  FROM ( select [ProductionOrder] ,[WorkDate]  ,[Operation],DATENAME(DW, [WorkDate]) as EndDateName\r\n"
			+ "			FROM [PPMM].[dbo].[OperationWorkDate] ) as a   \r\n"
			+ "  inner join ( select [ProductionOrder]   \r\n"
			+ "					  ,max([Operation]) as maxOperation \r\n"
			+ "			   FROM [PPMM].[dbo].[OperationWorkDate]  \r\n"
			+ "			   where Operation >= 100 and Operation <= 103\r\n"
			+ "			   group by ProductionOrder) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.maxOperation\r\n"
			+ " " ;
	private String leftJoinUCAL = "    "
			+ "   left join #TempUCAL "
//			+ " ( \r\n"
//			+ "   SELECT distinct -- a.[ProductionOrder]  ,[UserStatus]  ,d.[Grade]  ,countGRFinish ,isAnita ,CoaApproveDate  ,[CFMStatus],[DistChannel],\r\n"
//			+ "	  a.[ProductionOrder],  \r\n"
//			+ "	  CASE \r\n"
//			+ "	    when --a.UserStatus = 'ขายแล้ว' or \r\n"
//			+ "			 a.UserStatus = 'ยกเลิก' or a.UserStatus = 'ปิดเพื่อแก้ไข' or  \r\n"
//			+ "			 a.UserStatus = 'ตัดเกรดZ' or a.UserStatus = 'Over' or a.UserStatus = 'ขายเหลือ' or a.UserStatus = 'Lab' \r\n"
//			+ "		THEN a.UserStatus\r\n"
//			+ "		when ( d.[grade] = 'A' OR d.[grade] = 'C' ) AND countGRFinish = 1 and [DistChannel] = 'DM' THEN \r\n"
//			+ "			CASE \r\n"
//			+ "				WHEN CFMStatus = '' THEN 'รอตอบ CFM' \r\n"
//			+ "				WHEN CFMStatus = 'N' AND CoaApproveDate IS NULL THEN 'รอสรุปจาก QA'  \r\n"
//			+ "				WHEN CFMStatus = 'Y' AND CoaApproveDate IS NULL THEN 'รอ COA ลูกค้า ok สี'  \r\n"
//			+ "				WHEN CFMStatus = 'Y' AND CoaApproveDate IS NOT NULL THEN \r\n"
//			+ "					CASE \r\n"
//			+ "						WHEN SumBill IS NULL THEN 'รอขาย'     \r\n"
//			+ "						WHEN Volumn <= SumBill THEN 'ขายแล้ว'  \r\n"
//			+ "						WHEN SumBill > 0 THEN 'ขายแล้วบางส่วน'  \r\n"
//			+ "						ELSE a.UserStatus\r\n"
//			+ "					end \r\n"
//			+ "				ELSE a.UserStatus\r\n"
//			+ "			end\r\n"
//			+ "		when ( d.[grade] = 'A' OR d.[grade] = 'C' ) AND countGRFinish = 1 and [DistChannel] = 'EX' THEN \r\n"
//			+ "			CASE \r\n"
//			+ "				WHEN CFMStatus = '' THEN 'รอตอบ CFM' \r\n"
//			+ "				WHEN CFMStatus = 'N' AND CoaApproveDate IS NULL THEN 'รอสรุปจก QA'  \r\n"
//			+ "				WHEN isAnita = 1 and CFMStatus = 'Y' AND CoaApproveDate IS NULL THEN 'รอตอบ CFM ตัวแทน'\r\n"
//			+ "				WHEN isAnita is null and CFMStatus = 'Y' AND CoaApproveDate IS NULL THEN 'รอ COA ลูกค้า ok สี'   \r\n"
//			+ "				WHEN CFMStatus = 'Y' AND CoaApproveDate IS NOT NULL THEN \r\n"
//			+ "					CASE \r\n"
//			+ "						WHEN SumBill IS NULL THEN  \r\n"
//			+ "							CASE \r\n"
//			+ "								WHEN ( StockLoad is not null or StockLoad <> '' ) THEN 'รอเปิดบิล'    \r\n"
//			+ "								ELSE 'รอแจ้งส่ง'  \r\n"
//			+ "							end \r\n"
//			+ "						WHEN Volumn <= SumBill THEN 'ขายแล้ว'  \r\n"
//			+ "						WHEN SumBill > 0 THEN 'ขายแล้วบางส่วน'\r\n"
//			+ "						ELSE a.UserStatus   \r\n"
//			+ "					end \r\n"
//			+ "				ELSE a.UserStatus\r\n"
//			+ "				end\r\n"
//			+ "			when d.[grade] = 'Z' THEN 'ตัดเกรด Z'  \r\n"
//			+ "		ELSE a.UserStatus END AS UserStatusCal\r\n"
//			+ "	  FROM ( select a.ProductionOrder ,a.Volumn,a.UserStatus,a.SaleOrder,SaleLine,a.LotNo,\r\n"
//			+ "					case\r\n"
//			+ "						WHEN Unit = 'KG' THEN SumBillKG   \r\n"
//			+ "						WHEN Unit = 'YD' THEN SumBillYD  \r\n"
//			+ "						ELSE SumBillMR\r\n"
//			+ "					end AS SumBill\r\n"
//			+ "			 from [PCMS].[dbo].[FromSapMainProd] as a\r\n"
//			+ "			 LEFT JOIN ( SELECT [ProductionOrder]  \r\n"
//			+ "						  ,SUM([QuantityKG]) AS SumBillKG\r\n"
//			+ "						  ,SUM([QuantityYD]) AS SumBillYD\r\n"
//			+ "						  ,SUM([QuantityMR]) AS SumBillMR\r\n"
//			+ "					  FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
//			+ "					  WHERE [DataStatus]  = 'O'\r\n"
//			+ "					  GROUP BY [ProductionOrder]  )AS BS ON A.ProductionOrder = BS.ProductionOrder\r\n"
//			+ "         where SaleOrder <> '' \r\n"
//			+ "			)as a\r\n"
//			+ "   inner join (  SELECT a.[SaleOrder] ,a.[SaleLine] ,[SaleUnit] ,[DistChannel] , isAnita \r\n"
//			+ "					  FROM [PCMS].[dbo].[FromSapMainSale] AS A\r\n"
//			+ "					  left join (  SELECT [SaleOrder] ,[SaleLine] ,count (b.CustomerNo) as isAnita \r\n"
//			+ "									 FROM [PCMS].[dbo].[FromSapMainSale] AS A \r\n"
//			+ "									 inner join [PCMS].[dbo].[ConfigCustomerEX] AS B ON A.CustomerNo = B.CustomerNo \r\n"
//			+ "                                  where SaleOrder <> '' \r\n"
//			+ "									 group by [SaleOrder] ,[SaleLine],[SaleUnit],[DistChannel]  ) as c on a.SaleOrder = c.SaleOrder and a.SaleLine = c.SaleLine\r\n"
//			+ "                where a.SaleOrder <> '' \r\n"
//			+ "			  ) as c on a.SaleOrder = c.SaleOrder and a.SaleLine = c.SaleLine \r\n"
//			+ "	  left join ( SELECT [ProductionOrder] \r\n"
//			+ "						,count([OperationEndTime]) as countGRFinish\r\n"
//			+ "				  FROM [PPMM].[dbo].[DataFromSap]\r\n"
//			+ "				  WHERE OPERATION = '230' AND OperationStatus = 'PROCESS DONE'\r\n"
//			+ "				  group by ProductionOrder ) as b on a.ProductionOrder = b.ProductionOrder   \r\n"
////			+ "	  left join (  SELECT [SaleOrder]\r\n"
////			+ "						  ,[SaleLine]  \r\n"
////			+ "						  ,[SaleUnit]\r\n"
////			+ "                       ,[DistChannel]\r\n"
////			+ "						  ,count (b.CustomerNo) as isAnita \r\n"
////			+ "					  FROM [PCMS].[dbo].[FromSapMainSale] AS A\r\n"
////			+ "					  inner join [PCMS].[dbo].[ConfigCustomerEX] AS B ON A.CustomerNo = B.CustomerNo \r\n"
////			+ "					  group by [SaleOrder] ,[SaleLine],[SaleUnit],[DistChannel]  ) as c on a.SaleOrder = c.SaleOrder and a.SaleLine = c.SaleLine\r\n"
////			+ "	  left join ( SELECT [SaleOrder] ,[SaleLine] ,[DistChannel]\r\n"
////			+ "					  FROM [PCMS].[dbo].[FromSapMainSale]\r\n"
////			+ "				  ) as c1 on a.SaleOrder = c1.SaleOrder and a.SaleLine = c1.SaleLine\r\n"
//			 
//			+ "   left join (SELECT ProductionOrder , Grade \r\n"
//			+ "				 from [FromSapMainGrade] \r\n"
//			+ "				 where DataStatus = 'O' ) as d on a.ProductionOrder = d.ProductionOrder	 left join ( SELECT  [LotNumber]  ,max(CoaApproveDate)  as CoaApproveDate\r\n"
//			+ "					  FROM [QCMS].[dbo].[RequestOrder]\r\n"
//			+ "					  where CoaApproveDate is not null  \r\n"
//			+ "					  group by LotNumber) as e on a.[LotNo] = e.[LotNumber]\r\n"
//			+ "	 left join ( SELECT a.[ProductionOrder] ,[CFMStatus]  , CFMNo \r\n"
//			+ "					  FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
//			+ "					  INNER join (  SELECT [ProductionOrder] , max([CFMNo]) as maxCFMNo \r\n"
//			+ "									  FROM [PCMS].[dbo].[FromSapCFM]\r\n"
//			+ "									  group by [ProductionOrder]   \r\n"
//			+ "								) as b on a.ProductionOrder = b.ProductionOrder AND A.CFMNo = B.maxCFMNo\r\n"
//			+ "					  ) as f on a.[ProductionOrder] = f.[ProductionOrder]  \r\n"
//			+ "	   LEFT JOIN( SELECT [ProductionOrder] ,[SaleOrder] ,[SaleLine]  ,[StockLoad] \r\n"
//			+ "				FROM [PCMS].[dbo].[InputStockLoad] \r\n"
//			+ "				WHERE DataStatus = 'O' ) AS g on a.ProductionOrder =g.ProductionOrder and a.SaleOrder = c.SaleOrder and a.SaleLine = c.SaleLine  \r\n"
//			+ "    ) "
			+ " as UCAL on b.ProductionOrder = UCAL.ProductionOrder      \r\n"; 
//	private String leftJoinB = " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n";
//	private String leftJoinB =    
//			" left join  ( SELECT a.[SaleOrder] , a.[Saleline]  ,[TotalQuantity] ,[Unit]\r\n"
//			+ " ,[RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] ,[UserStatus]\r\n"
//			+ " ,a.[DesignFG],a.[ArticleFG],[BookNo],[Center],[LotNo]\r\n"
//			+ " ,[Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
//			+ " ,[OrderPuang] ,[RefPrd],[GreigeInDate] ,[BCDate],[Volumn]\r\n"
//			+ " ,[CFdate],[CFType],[Shade],[LotShipping],[BillSendQuantity],[Grade],[DataStatus]\r\n"
//			+ " ,[PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG]\r\n"
//			+ "				 ,CASE  \r\n"
//			+ "						  WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
//			+ "						  WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
//			+ "						  ELSE 'รอจัด Lot'  \r\n"
//			+ "						  END AS [ProductionOrder]  \r\n"
//			+ "			from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
//			+ "			left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n"
//			+ "			) as b on  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n";
	 private String leftJoinB =    
			  " left join  ( SELECT a.[SaleOrder] , a.[Saleline]  ,[TotalQuantity] ,[Unit]\r\n"
			  + "			 ,[RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] ,[UserStatus]\r\n"
			  + "			 ,a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
			  + "			 ,[Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
			  + "			 ,[OrderPuang] ,[RefPrd],[GreigeInDate] ,[BCDate],[Volumn]\r\n"
			  + "			 ,[CFdate],[CFType],[Shade],[LotShipping],[BillSendQuantity],[Grade],[DataStatus]\r\n"
			  + "			 ,[PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG]\r\n"
			  + "			 ,CASE  \r\n"
			  + "					WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
			  + "					WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
			  + "					ELSE 'รอจัด Lot'  \r\n"
			  + "					END AS [ProductionOrder]  \r\n"
			  + "			 ,CASE  \r\n"
			  + "					WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
			  + "					WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
			  + "					ELSE 'รอจัด Lot'  \r\n"
			  + "					END AS [LotNo]  \r\n"
			  + " 			 ,CASE  \r\n"
			  + " 					WHEN ( s.SumVolRP is not null AND t.SumVolOP is not null ) THEN (  b.Volumn -  s.SumVolRP -  t.SumVolOP)\r\n"
			  + "					WHEN ( s.SumVolRP is not null AND t.SumVolOP is null ) THEN (  b.Volumn -  s.SumVolRP )\r\n"
			  + "					WHEN ( s.SumVolRP is null AND t.SumVolOP is not null ) THEN (  b.Volumn -  t.SumVolOP) \r\n"
			  + "					WHEN  b.Volumn is not null THEN  b.Volumn\r\n"
			  + "				    ELSE   0\r\n"
			  + "	 				END AS SumVol   \r\n"
			  + " 			 , CASE  \r\n"
			  + "				 	WHEN ( s.SumVolRP is not null AND t.SumVolOP is not null ) THEN a.Price * (  b.Volumn -  s.SumVolRP -  t.SumVolOP)\r\n"
			  + "				 	WHEN ( s.SumVolRP is not null AND t.SumVolOP is null ) THEN a.Price * (  b.Volumn -  s.SumVolRP )\r\n"
			  + "				 	WHEN ( s.SumVolRP is null AND t.SumVolOP is not null ) THEN a.Price * (  b.Volumn -  t.SumVolOP)   \r\n"
			  + "				 	WHEN  b.Volumn is not null THEN a.Price * b.Volumn\r\n"
			  + "					ELSE   0\r\n"
			  + "				 	END AS SumVolFGAmount   \r\n"
			  + "              ,s.SumVolRP,t.SumVolOP\r\n"
			  + "			from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			  + "			left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n"
			  + "			left join ( SELECT ProductionOrder  , sum(Volumn) as SumVolOP\r\n"
			  + "                        from [PCMS].[dbo].FromSapMainProdSale\r\n"
			  + "			            WHERE [DataStatus] = 'O'\r\n"
			  + "			            group by ProductionOrder) as t on b.ProductionOrder = t.ProductionOrder   \r\n"
			  + "            left join ( SELECT a.ProductionOrderRP , sum(c.Volume) as SumVolRP  \r\n"
			  + "    					from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
			  + "						left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
			  + "						left join ( SELECT ProductionOrderRP  \r\n"
			  + "										 , CASE  \r\n"
			  + "										   WHEN ( Volume = 0 ) THEN b.Volumn  \r\n"
			  + "										   ELSE a.Volume\r\n"
			  + "										   END AS Volume  \r\n"
			  + "									from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
			  + "									left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
			  + "									WHERE a.[DataStatus] = 'O' ) as c on a.ProductionOrderRP = c.ProductionOrderRP\r\n"
			  + "				WHERE a.[DataStatus] = 'O'\r\n"
			  + "				group by a.ProductionOrderRP) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n" 
			  + "			) as b on  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n";
	private String leftJoinBCaseTwo =    
			" left join  ( SELECT a.[SaleOrder] , a.[Saleline]  ,[TotalQuantity] ,[Unit]\r\n"
			+ " ,[RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] ,[UserStatus]\r\n"
			+ " ,a.[DesignFG],a.[ArticleFG],[BookNo],[Center],[LotNo]\r\n"
			+ " ,[Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
			+ " ,[OrderPuang] ,[RefPrd],[GreigeInDate] ,[BCDate],[Volumn]\r\n"
			+ " ,[CFdate],[CFType],[Shade],[LotShipping],[BillSendQuantity],[Grade],[DataStatus]\r\n"
			+ " ,[PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG]\r\n"
			+ "				 ,CASE  \r\n"
			+ "						  WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
			+ "						  WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
			+ "						  ELSE 'รอจัด Lot'  \r\n"
			+ "						  END AS [ProductionOrder]  \r\n"
			+ "			from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			+ "			left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n"
			+ "			) as b on  a.ProductionOrder = b.ProductionOrder \r\n";
	private String leftJoinD = 
  		  "   left join ( SELECT distinct a.[ProductionOrder]\r\n"    
  		+ "					  ,[CFMNo]\r\n"
  		+ "					  ,[CFMNumber]\r\n"
  		+ "					  ,[CFMSendDate]\r\n"
  		+ "					  ,[CFMAnswerDate]\r\n"
  		+ "					  ,[CFMStatus] \r\n"   
  		+ "					  ,[CFMRemark]\r\n"
  		+ "				FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
  		+ "				inner join (select ProductionOrder ,   max([CFMNo]) as maxCFMNo  \r\n"
  		+ "					        from [PCMS].[dbo].[FromSapCFM] \r\n"
  		+ "							where  DataStatus <> 'X'  and [CFMNumber] <> ''\r\n"
  		+ "					 		group by ProductionOrder )as b on \r\n"
  		+ "                         a.[ProductionOrder] = b.[ProductionOrder] and  a.[CFMNo] = b.[maxCFMNo]  \r\n"
  		+ "             where  DataStatus <> 'X'  and [CFMNumber] <> '' \r\n"
  		+ "			) as d on b.ProductionOrder = d.ProductionOrder \r\n";  
	private String leftJoinC = " left join [PCMS].[dbo].[FromSapMainGrade] as c on b.ProductionOrder = c.ProductionOrder and c.DataStatus = 'O' \r\n"; 
	
//	private String leftJoinF = 
//  		  	" left join (SELECT a.[ProductionOrder] \r\n"
//  		  	+ "			,max(a.SubmitDate) as CFMPlanDateOne\r\n"
//  		  	+ "			FROM [PCMS].[dbo].[FromSapSubmitDate]  as a \r\n"
//  		  	+ "         WHERE [DataStatus] = 'O'"
//  		  	+ "			group by a.[ProductionOrder] ) as f on b.ProductionOrder = f.ProductionOrder \r\n"  ;   
//	private String leftJoinG = 
//  		  	" left join ( SELECT distinct a.[ProductionOrder] \r\n"  
//  		  	+ "  			 ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS CFMPlanDate \r\n"
//  		  	+ "			FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
//  		  	+ "			inner join (select [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
//  		  	+ "				FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
//  		  	+ "				group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
//  		  	+ "				on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
//  		  	+ "				and a.[CreateDate] = b.[MaxCreateDate] \r\n"
//  		  	+ "			) as g on g.ProductionOrder = b.ProductionOrder and g.SaleOrder = a.SaleOrder and g.SaleLine = a.SaleLine\r\n"  ;   
	private String leftJoinG = 
			" left join "
//		  	+ " ( SELECT distinct a.[ProductionOrder]   \r\n"
//		  	+ "	  		, case \r\n"
//		  	+ "				when EndDateName = 'Saturday' then DATEADD(DAY, 9, a.[WorkDate])\r\n"
//		  	+ "				when EndDateName is not null then DATEADD(DAY, 8, a.[WorkDate])\r\n"
//		  	+ "				else null \r\n"
//		  	+ "				end as CFMPlanDate  \r\n"
//		  	+ "  FROM ( select [ProductionOrder] ,[WorkDate]  ,[Operation],DATENAME(DW, [WorkDate]) as EndDateName\r\n"
//		  	+ "		FROM [PPMM].[dbo].[OperationWorkDate] ) as a   \r\n"
//		  	+ "  inner join ( select [ProductionOrder]   \r\n"
//		  	+ "					  ,max([Operation]) as maxOperation \r\n"
//		  	+ "			   FROM [PPMM].[dbo].[OperationWorkDate]  \r\n"
//		  	+ "			   where Operation >= 100 and Operation <= 103\r\n"
//		  	+ "			   group by ProductionOrder) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.maxOperation\r\n"
//		  	+ " \r\n"
//		  	+ "  ) "
		+ " #TempG "
		  	+ " as g on g.ProductionOrder = b.ProductionOrder \r\n"  ;   
	private String leftJoinH = 
  		  	" left join ( SELECT distinct a.[ProductionOrder] \r\n"
  		  	+ "  			 ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
  		  	+ "			FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
  		  	+ "			inner join (select [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
  		  	+ "				FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
  		  	+ "				group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
  		  	+ "				on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
  		  	+ "				and a.[CreateDate] = b.[MaxCreateDate] \r\n"
  		  	+ "			) as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n"  ;   
	private String leftJoinX = " left join [PCMS].[dbo].[FromSapMainProdSale] as x on a.SaleLine = x.SaleLine and x.SaleOrder = a.SaleOrder  \r\n";
	private String leftJoinZ = 
		      " left join (SELECT distinct [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,max([LotShipping]) as [LotShipping]\r\n  "
			+ "            FROM [PCMS].[dbo].[FromSapMainGrade]\r\n  "
			+ "            GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) \r\n  "
			+ "  as z on z.ProductionOrder = b.ProductionOrder \r\n      and z.SaleLine = b.SaleLine \r\n "
			+ "      and z.SaleOrder = b.SaleOrder \r\n";
	private String leftJoinIToP = 
			  " left join ( select distinct a.ProductionOrder , WorkDate \r\n"
			+ "              from  [PPMM].[dbo].[OperationWorkDate]  as a\r\n"
			+ "              inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                          from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                          where Operation >= 100 and Operation <= 103\r\n"
			+ "                          group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "              where a.Operation >= 100 and a.Operation <= 103 ) as i on b.ProductionOrder = i.ProductionOrder\r\n"
			+ " left join ( select a.ProductionOrder , OperationEndDate as DyeActual,c.DyeingStatus as DyeStatus\r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap]  as a\r\n"
			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                        where Operation >= 100 and Operation <= 103\r\n"
			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "				left join [PPMM].[dbo].[ShopFloorControlDetail] as c on a.ProductionOrder = c.ProductionOrder and c.Operation = b.Operation \r\n"
			+ "            where a.Operation >= 100 and a.Operation <= 103 \r\n"
			+ "            ) as j on b.ProductionOrder = j.ProductionOrder\r\n"
			+ " left join ( select distinct a.ProductionOrder , OperationEndDate as Dryer \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                        where Operation >= 140 and Operation <= 143\r\n"
			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "            where a.Operation >= 140 and a.Operation <= 143 )  as k on b.ProductionOrder = k.ProductionOrder\r\n"
			+ " left join ( select distinct a.ProductionOrder , OperationEndDate as Finishing \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                        where Operation >= 190 and Operation <= 193\r\n"
			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "            where a.Operation >= 190 and a.Operation <= 193  ) as l on b.ProductionOrder = l.ProductionOrder\r\n"
			+ " left join ( select distinct a.ProductionOrder , OperationEndDate as Inspectation \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            inner join ( select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                         from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                         where Operation >= 199 and Operation <= 200\r\n"
			+ "                         group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "            where a.Operation >= 199 and a.Operation <= 200 ) as m on b.ProductionOrder = m.ProductionOrder \r\n"
			+ " left join ( select ProductionOrder , OperationEndDate as Prepare  \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            where a.Operation  = 5 ) as n on b.ProductionOrder = n.ProductionOrder \r\n"
			+ " left join ( select distinct ProductionOrder , OperationEndDate as Preset  \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            where a.Operation = 50  ) as o on b.ProductionOrder = o.ProductionOrder \r\n "
			+ " left join ( select distinct ProductionOrder , OperationEndDate as Relax  \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            where a.Operation = 20  ) as p on b.ProductionOrder = p.ProductionOrder \r\n" ;
	private String leftJoinBS = 
		      " left join "
			+ "           ("
			+ "            SELECT [ProductionOrder] ,SUM([QuantityKG]) AS SumBillKG ,SUM([QuantityYD]) AS SumBillKG ,SUM([QuantityMR]) AS SumBillKG\r\n"
			+ "  			FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			+ "  			WHERE [DataStatus]  = 'O'\r\n"
			+ "  			GROUP BY [ProductionOrder] "
			+ ")\r\n" 
			+ "  as BS on b.ProductionOrder = BS.ProductionOrder \r\n";
//	private String leftJoinQ = " left join [PCMS].[dbo].[InputSwitchRemark] as q on a.SaleLine = q.SaleLine and q.SaleOrder = a.SaleOrder  \r\n";
//	private String leftJoinR = " left join [PCMS].[dbo].[InputSwitchRemark] as r on a.SaleLine = r.SaleLine and r.SaleOrder = a.SaleOrder  \r\n"; 
	private String leftJoinR = 
	  		  "   left join (select ProductionOrder , ProductionOrderSW\r\n"
	  		  + "		   FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
	  		  + "		   WHERE DataStatus = 'O'\r\n"
	  		  + "		   ) as R on b.ProductionOrder = R.ProductionOrderSW  \r\n";  
	private String selectPreset = " [ProductionOrder],[PostingDate],[WorkCenter]\r\n"
			+ "      ,[Operation],[No],[DataStatus] \r\n";
	private String selectDyeing = "[ProductionOrder],[PostingDate],[Operation]\r\n"
			+ "      ,[WorkCenter],[DyeStatus],[Remark],[ReDye]\r\n"  
			+ "      ,[RollNo],[Da],[Db],[L],[ST]\r\n"
			+ "      ,[ColorStatus],[ColorRemark],[DeltaE],[No]\r\n"
			+ "      ,[DataStatus]\r\n ";
	private String selectPacking = "[ProductionOrder],[PostingDate],[Quantity]\r\n"
			+ "      ,[RollNo],[Status],[QuantityKG],[Grade]\r\n"
			+ "      ,[No],[DataStatus],[QuantityYD]\r\n ";
	private String selectInspect = "[ProductionOrder],[PostingDate],[QuantityGreige]\r\n"
			+ "      ,[Operation],[QuantityFG],[Remark],[No]\r\n"
			+ "      ,[DataStatus]\r\n ";
	private String selectFinishing = "[ProductionOrder],[No],[PostingDate]\r\n"
			+ "      ,[WorkCenter],[Status],[NCDate],[Cause]\r\n"
			+ "      ,[CarNo],[DeltaE],[Color],[Operation]\r\n"
			+ "      ,[CCStatus],[CCRemark],[RollNo],[Da]\r\n"
			+ "      ,[Db],[L],[ST],[CCPostingDate]\r\n"
			+ "      ,[CCOperation],[LotNo],[DataStatus]\r\n ";
	private String selectSendTestQC = "[ProductionOrder],[No],[SendDate]\r\n"
			+ "      ,[CheckColorDate],[RollNo],[Status],[DeltaE]\r\n"
			+ "      ,[Color],[Remark],[DataStatus]\r\n ";

	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public PCMSMainDaoImplOld(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSTableDetail> list = null;
		String where = " where  ";
		String whereTempUCAL = " where ";
		int checkWaitLot = 0,checkSaleStock = 0;
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="";
		PCMSTableDetail bean = poList.get(0);
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
//		saleNumber = bean.getSaleNumber() ;  
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();
		saleCreateDate = bean.getSaleOrderCreateDate();
		saleNumber = bean.getSaleNumber();
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		dueDate = bean.getDueDate();
		designFG = bean.getDesignFG();
		userStatus = bean.getUserStatus();
		List<String> divisionList = bean.getDivisionList();
		List<String> userStatusList = bean.getUserStatusList();
		List<String> cusNameList = bean.getCustomerNameList();
		List<String> cusShortNameList = bean.getCustomerShortNameList();
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();   
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel();
		where +=    " MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n";
//				  + " a.SaleLine like '"+saleLine+"%' and\r\n"
		whereTempUCAL +=" MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n";
		;
		if (!saleCreateDate.equals("")) {   
			String[] dateArray = saleCreateDate.split("-");
			where += "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
			whereTempUCAL +=  "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
		} 
		if(!saleNumber.equals("")) { 
			where += " and SaleNumber like '"+saleNumber+"%' \r\n" ; 
			whereTempUCAL += " and SaleNumber like '"+saleNumber+"%' \r\n" ; 
		}
		if (!labNo.equals("")) {
			where += " and LabNo like '" + labNo + "%'  \r\n";
			whereTempUCAL += " and LabNo like '" + labNo + "%'  \r\n";
		}
		if (!articleFG.equals("")) {
			where += " and a.ArticleFG like '" + articleFG + "%'  \r\n";   
			whereTempUCAL += " and a.ArticleFG like '" + articleFG + "%'  \r\n";   
		}
		if (!designFG.equals("")) {
			where += " and a.DesignFG like '" + designFG + "%'  \r\n";
			whereTempUCAL += " and a.DesignFG like '" + designFG + "%'  \r\n";
		} 
		if (cusNameList.size() > 0) { 
			String tmpWhere = "";
			tmpWhere += " and  ( ";
			String text = "";
			for (int i = 0; i < cusNameList.size(); i++) {
				text = cusNameList.get(i);
				tmpWhere += " CustomerName = '" +text + "' ";
				if (i != cusNameList.size() - 1) {
					tmpWhere += " or ";
				} ;
			}
			tmpWhere += " ) \r\n";
			where += tmpWhere;
			whereTempUCAL += tmpWhere;
		}
		if (cusShortNameList.size() > 0) { 
			String tmpWhere = "";
			tmpWhere += " and  ( "; 
			String text = "";
			for (int i = 0; i < cusShortNameList.size(); i++) {
				text = cusShortNameList.get(i);
				tmpWhere += " CustomerShortName = '" +text + "' ";
				if (i != cusShortNameList.size() - 1) {
					tmpWhere += " or ";
				} ;
			}
			tmpWhere += " ) \r\n";
			where += tmpWhere;
			whereTempUCAL += tmpWhere;
		}
		if (divisionList.size() > 0) {  
			String tmpWhere = "";
			tmpWhere += " and  ( ";  
			String text = "";
			for (int i = 0; i < divisionList.size(); i++) {
				text = divisionList.get(i);
				tmpWhere += " Division = '" +text + "' ";
					if (i != divisionList.size() - 1) {
						tmpWhere += " or ";
					} ;
				}
			tmpWhere += " ) \r\n";
			where += tmpWhere;
			whereTempUCAL += tmpWhere;
			}
		 
		if (!prdOrder.equals("")) {
			where += " and b.ProductionOrder like '" + prdOrder + "%'  \r\n ";
			whereTempUCAL += " and b.ProductionOrder like '" + prdOrder + "%'  \r\n ";
		}
		if (!prdCreateDate.equals("")) {
			String[] dateArray = prdCreateDate.split("-");
			where += " and ( PrdCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " PrdCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) )  \r\n";
			whereTempUCAL += " and ( PrdCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " PrdCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) )  \r\n";
		}if(!dueDate.equals("")) {  
			String[] dateArray = dueDate.split("-");
			where += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
			whereTempUCAL += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
		}
		if (!deliveryStatus.equals("")) {
			where += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n";
			whereTempUCAL += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n";
		}
		if (!saleStatus.equals("")) {
			where += " and SaleStatus like '" + saleStatus + "%'  \r\n";
			whereTempUCAL += " and SaleStatus like '" + saleStatus + "%'  \r\n";
		}
		if (!dist.equals("")) {
			String tmpWhere = "";
			tmpWhere += " and  ( ";  
			String[] array = dist.split("\\|");      
			for (int i = 0; i < array.length; i++) { 
				tmpWhere += " DistChannel = '" + array[i] + "' ";
				if (i != array.length - 1) {
					tmpWhere += " or ";
				} ;
			}
			tmpWhere += " ) \r\n"; 
			where += tmpWhere;
			whereTempUCAL += tmpWhere;
		}   
		if (userStatusList.size() > 0) {
			String tmpWhere = "";
			tmpWhere += " and  \r\n" ; 
			tmpWhere += "  ( b.ProductionOrder is not null and ( \r\n";
			String text = "";
			for (int i = 0; i < userStatusList.size(); i++) {
				text = userStatusList.get(i); 
				if(text.equals("รอจัด Lot")  ) { 
					tmpWhere += " b.ProductionOrder = '" +text + "' ";
				}
				else if( text.equals("ขาย stock")) { 
					tmpWhere += " b.ProductionOrder = '" +text + "' ";
				}
				else { 
//					where += " b.UserStatus = '" +text + "' "; 
					tmpWhere += "UCAL.UserStatusCal = '" +text + "' ";
				} 
				if (i != userStatusList.size() - 1) {
					tmpWhere += " or ";   
				} ;
			}
			tmpWhere += ") 		) \r\n";  
			where += tmpWhere;
//			whereTempUCAL += tmpWhere;
		}  
		String sql = this.createTempUCAL//+whereTempUCAL
				+this.createTempG
				+ "SELECT DISTINCT  " 
				+ this.select 	
				+ " ,'Main' as TypePrd \r\n"
				+ " , 'Main' AS TypePrdRemark \r\n"   
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n "
				+ this.leftJoinB
				+ this.leftJoinC
				+ this.leftJoinD 
				+ this.leftJoinG
				+ this.leftJoinH 
				+ this.leftJoinZ 
				+ this.leftJoinIToP 
				+ this.leftJoinR 
				+ this.leftJoinUCAL 
				+ where     
				+ " and R.ProductionOrderSW is null \r\n"
				+ " and (b.SumVol <> 0 or ( (  b.ProductionOrder = 'รอจัด Lot'  or  b.ProductionOrder = 'ขาย stock' )  and b.SumVol = 0 ) )\r\n"        
				// Order Puang
//				+ " union "
//				+ "SELECT DISTINCT  " 
//				+ this.select 
//				+ " ,'OrderPuang' as TypePrd \r\n" 
//    			+ " ,TypePrdRemark \r\n"
//    			+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
//				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
//				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
//				+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n" 
//				+ " ,   'SUB' as TypePrdRemark \r\n" 
//				+ "  ,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
//				+ " , CASE WHEN b.Volumn <> 0 THEN b.Volumn \r\n"
//				+ "    ELSE  0 \r\n"     
//				+ "    END AS Volumn \r\n"
//				+ "		 from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
//				+ "		 inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
//				+ "		 where b.DataStatus <> 'X' and b.SaleLine <> '' ) as a  \r\n "  
//				+ "  left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
//				+ this.leftJoinC	 
//				+ this.leftJoinD 
//				+ this.leftJoinG
//				+ this.leftJoinH
//				+ this.leftJoinZ      
//				+ this.leftJoinIToP     
//				+ this.leftJoinR
//				+ this.leftJoinUCAL 
//				+ where     
//				+ " and R.ProductionOrderSW is null"        
//				//// Order PuangSwitch
//				+ " union "
//				+ "SELECT DISTINCT  " 
//				+ this.select 
//				+ " ,'OrderPuang' as TypePrd \r\n"  
//    			+ " ,TypePrdRemark \r\n"
//				+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
//				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
//				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
//				+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n" 
//				+ "           , 'SUB' as TypePrdRemark \r\n" 
//				+ "           ,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
//				+ "           , CASE WHEN b.Volumn <> 0 THEN b.Volumn \r\n"
//				+ "             ELSE  0 \r\n"     
//				+ "             END AS Volumn \r\n"
//				+ "		 from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
//				+ "		 inner join ( \r\n"
//				+ "            SELECT CASE \r\n"
//				+ "			          WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
//				+ "			          ELSE C.ProductionOrder\r\n"
//				+ "			          END AS [ProductionOrder]\r\n"
//				+ "		              ,[SaleOrder] ,[SaleLine] ,[Volumn]  ,[DataStatus]\r\n"
//				+ "		        FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
//				+ "		        LEFT JOIN (SELECT  [ProductionOrder] ,[ProductionOrderSW] \r\n"
//				+ "					       FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
//				+ "					       WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
//				+ "					       AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
//				+ "		        LEFT JOIN (SELECT  [ProductionOrder] \r\n"
//				+ "							,[ProductionOrderSW] \r\n"
//				+ "					       FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
//				+ "					       WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
//				+ "					       AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n"
//				+ "		WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL)\r\n" 
//				+ "       ) as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
//				+ "		 where b.DataStatus <> 'X' and b.SaleLine <> '' ) as a  \r\n "  
//				+ "  left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
//				+ this.leftJoinC	 
//				+ this.leftJoinD 
//				+ this.leftJoinG
//				+ this.leftJoinH
//				+ this.leftJoinZ      
//				+ this.leftJoinIToP     
//				+ this.leftJoinR
//				+ this.leftJoinUCAL
//				
//				+ where     
////			// Switch   
//				+ " union "
//				+ "SELECT DISTINCT  " 
//				+ this.select 
//				+ " ,'Switch' as TypePrd \r\n"  
//    			+ " ,TypePrdRemark \r\n"
//    			+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
//				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
//				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
//				+ "	          , b.ProductionOrderSW as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
//				+ "  		,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
//				+ " 		, CASE \r\n"
//				+ "				when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
//				+ "				ELSE 'SUB' \r\n"
//				+ "			END	TypePrdRemark  ,C.SumVol\r\n" 
//				+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
//				+ "		 	inner join [PCMS].[dbo].[SwitchProdOrder]  as b on a.SaleLine = b.SaleLineSW and a.SaleOrder = b.SaleOrderSW  \r\n \r\n"
//				+ "		 	LEFT JOIN ( \r\n"
//				+ "				 SELECT PRDORDERSW ,sum([Volumn]) as SumVol\r\n"
//				+ "				FROM ( SELECT A.[ProductionOrder] \r\n"
//				+ "					  ,CASE \r\n"
//				+ "						WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
//				+ "						ELSE C.ProductionOrder\r\n"
//				+ "						END AS PRDORDERSW\r\n"
//				+ "					  ,[SaleOrder]\r\n"
//				+ "					  ,[SaleLine]\r\n"
//				+ "					  ,[Volumn]\r\n"
//				+ "					  ,[DataStatus]\r\n"
//				+ "				  FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
//				+ "				  LEFT JOIN (SELECT  [ProductionOrder] \r\n"
//				+ "									,[ProductionOrderSW] \r\n"
//				+ "							  FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
//				+ "							  WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
//				+ "							   AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
//				+ "				  LEFT JOIN (SELECT  [ProductionOrder] \r\n"
//				+ "									,[ProductionOrderSW] \r\n"
//				+ "							  FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
//				+ "							  WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
//				+ "							   AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n"
//				+ "				WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL)\r\n"
//				+ "				) AS A\r\n"
//				+ "				group by PRDORDERSW\r\n"
//				+ "		 \r\n"
//				+ "		 ) AS C ON B.ProductionOrderSW = C.PRDORDERSW "
//				+ "		 where b.DataStatus <> 'X') as a  \r\n "  
//				+ "  left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
//				+ this.leftJoinC	 
//				+ this.leftJoinD 
//				+ this.leftJoinG
//				+ this.leftJoinH
//				+ this.leftJoinZ      
//				+ this.leftJoinIToP     
//				+ this.leftJoinR
//				+ this.leftJoinUCAL  
//				+ where     
////////			// สวม  
//				+ " union "
//				+ "SELECT DISTINCT  " 
//				+ this.select 
//				+ " ,'Replaced' as TypePrd \r\n"
//    			+ " ,TypePrdRemark \r\n"
//				+ " FROM (  SELECT DISTINCT  b.[SaleOrder]  , b.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
//				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
//				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
//				+ "	          , b.[ProductionOrderRP] as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
//				+ " 	 ,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
//				+ "   	, CASE  \r\n"
//				+ "			WHEN b.Volume <> 0 THEN b.Volume\r\n"
//				+ "			ELSE  c.Volumn\r\n"  
//				+ "			END AS Volumn   \r\n"
//				+ " 	,   'SUB' as TypePrdRemark \r\n" 
//				+ "		 from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
//				+ "		 inner join [PCMS].[dbo].[ReplacedProdOrder] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder   \r\n"
//				+ "		 left join  [PCMS].[dbo].[FromSapMainProd] as c on c.ProductionOrder = b.[ProductionOrderRP]\r\n"
//				+ "      where b.DataStatus <> 'X') as a  \r\n "  
//				+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
//				+ this.leftJoinC	 
//				+ this.leftJoinD 
//				+ this.leftJoinG
//				+ this.leftJoinH
//				+ this.leftJoinZ      
//				+ this.leftJoinIToP     
//				+ this.leftJoinR				
//				+ this.leftJoinUCAL  
//				+ where     
//				+ " Order by a.CustomerShortName, a.DueDate ,a.[SaleOrder], [SaleLine], [ProductionOrder]"
				;      
		System.out.println(sql);
		System.out.println("Before SQL: "+new Date());
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}  
		System.out.println("AFTER SQL LIST: "+new Date());
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSAllDetail> list = null;
		String where = " where  ";
		String prdOrder = "" ;
		PCMSTableDetail bean = poList.get(0); 
		prdOrder = bean.getProductionOrder();      
		where += " b.ProductionOrder = '" + prdOrder + "' \r\n";
		String sql = this.createTempUCAL 
				+this.createTempG+
				"SELECT distinct top 1   " 
				+ this.selectTwo 
				+ " from  [PCMS].[dbo].FromSapMainSale as a \r\n "
//				+ " inner join [PCMS].[dbo].[FromSapMainProd] as b on a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n"
////				+ this.leftJoinF
				+ this.leftJoinB
				+ this.leftJoinG   
				+ this.leftJoinH
				+ where 
				+ " Order by a.SaleOrder , 	SaleLine"; 	
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		if (list.size() > 0) {
			ArrayList<PODetail> poDetailList = getPODetail(poList);
			ArrayList<PresetDetail> presetDetailList = getPresetDetail(poList);
			ArrayList<DyeingDetail> dyeingDetailList = getDyeingDetail(poList);

			ArrayList<SendTestQCDetail> sendTestQCDetailList = getSendTestQCDetail(poList);
			ArrayList<FinishingDetail> finDetailList = getFinishingDetail(poList);
			ArrayList<InspectDetail> insDetailList = getInspectDetail(poList);
			ArrayList<PackingDetail> packDetailList = getPackingDetail(poList);

			ArrayList<WorkInLabDetail> workInLabDetailList = getWorkInLabDetail(poList);
			ArrayList<WaitTestDetail> waitTestDetailList = getWaitTestDetail(poList);
			ArrayList<CFMDetail> cfmDetailList = getCFMDetail(poList);
			ArrayList<SaleDetail> saleDetailList = getSaleDetail(poList);
			ArrayList<SaleInputDetail> saleInputDetailList = getSaleInputDetail(poList);
			ArrayList<InputDateDetail> submitdatDetailList = getSubmitDateDetail(poList);
			ArrayList<NCDetail> ncDetailList = getNCDetail(poList);
			ArrayList<ReceipeDetail> receipeDetailList = getReceipeDetail(poList);
			PCMSAllDetail beanTmp = list.get(0);
			beanTmp.setPoDetailList(poDetailList);
			beanTmp.setPresetDetailList(presetDetailList);
			beanTmp.setSendTestQCDetailList(sendTestQCDetailList);
			beanTmp.setDyeingDetailList(dyeingDetailList);
			beanTmp.setFinishingDetailList(finDetailList);
			beanTmp.setInspectDetailList(insDetailList);
			beanTmp.setPackingDetailList(packDetailList);

			beanTmp.setWorkInLabDetailList(workInLabDetailList);
			beanTmp.setWaitTestDetailList(waitTestDetailList);
			beanTmp.setCfmDetailList(cfmDetailList);
			beanTmp.setSaleDetailList(saleDetailList);
			beanTmp.setSaleInputDetailList(saleInputDetailList);
			beanTmp.setSubmitdatDetailList(submitdatDetailList);
			beanTmp.setNcDetailList(ncDetailList);
			beanTmp.setReceipeDetailList(receipeDetailList);

		}
		return list;
	}

	private ArrayList<ReceipeDetail> getReceipeDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<ReceipeDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[No],[PostingDate]\r\n"
				+ "      ,[LotNo],[Receipe],[DataStatus] \r\n"
				+ " from [PCMS].[dbo].[FromSapReceipe] as a \r\n " + where + " Order by No";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<ReceipeDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genReceipeDetail(map));
		}
		return list;
	}

	private ArrayList<NCDetail> getNCDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<NCDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[SaleOrder],[SaleLine]\r\n"
				+ "      ,[No],[NCDate],[CarNo],[Remark]\r\n"
				+ "      ,[Quantity],[Unit],[NCFrom],[DataStatus]\r\n"
				+ "  " + " from [PCMS].[dbo].[FromSapNC] as a \r\n " + where + " Order by No";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<NCDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genNCDetail(map));
		}
		return list;
	}

	private ArrayList<InputDateDetail> getSubmitDateDetail(ArrayList<PCMSTableDetail> poList) {
//		ArrayList<SubmitDateDetail> list = null;
//		String where = " where  ";
//		String prdOrder = "";
//		PCMSTableDetail bean = poList.get(0);
//		prdOrder = bean.getProductionOrder();
//		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
//		String sql = "SELECT DISTINCT  " 
//				+ " [ProductionOrder],[SaleOrder],[SaleLine]\r\n"
//				+ "      ,[No],[SubmitDate],[Remark],[DataStatus]\r\n"
//				+ "  " 
//				+ " from [PCMS].[dbo].[FromSapSubmitDate] as a \r\n " 
//				+ where
//				+ " Order by No";
////		 System.out.println(sql);
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<SubmitDateDetail>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genSubmitDateDetail(map));
//		}
		
		ArrayList<InputDateDetail> list = null;
		PCMSTableDetail bean = poList.get(0);
		String prdOrder = bean.getProductionOrder();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql =  
		 " SELECT [ProductionOrder]\r\n"
		    + "      ,[SaleOrder]\r\n"
		    + "      ,[SaleLine]\r\n"
		    + "      ,[PlanDate]\r\n"
		    + "      ,[CreateBy]\r\n"
		    + "      ,[CreateDate]\r\n"
		    + "	  , '1:PCMS' as InputFrom \r\n"
		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n" 
		  + " where a.[ProductionOrder] = '" + prdOrder + "' and "
		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and "
		  + "       a.[SaleLine] = '" + saleLine+ "' "
		  + " union \r\n "
		  + " SELECT [ProductionOrder]\r\n"
		    + "      ,[SaleOrder]\r\n"
		    + "      ,[SaleLine]\r\n"
		    + "      ,SubmitDate as [PlanDate]\r\n"  
		    + "      ,'' AS [CreateBy]\r\n"
		    + "      ,null AS [CreateDate]\r\n" 
		    + "	    , '0:SAP' as InputFrom \r\n"
		  + " FROM [PCMS].[dbo].[FromSapSubmitDate]  as a\r\n" 
		  + " where a.[ProductionOrder] = '" + prdOrder + "'    and SubmitDate is not null "
  		  + "   and [DataStatus] = 'O'"
		  + "   ORDER BY InputFrom ,CreateDate ";
				
		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}

	private ArrayList<SaleInputDetail> getSaleInputDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<SaleInputDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[BillDate]\r\n"
				+ "      ,[BillQtyPerSale],[SaleOrder]"
				+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "		WHEN 0 THEN ''  \r\n"
				+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "		END AS [SaleLine] "
				+ "      ,[BillQtyPerStock],[Remark],[CustomerNo]\r\n"
				+ "      ,[CustomerName1],[CustomerPO],[DueDate]\r\n"
				+ "      ,[Color],[No],[DataStatus]\r\n" + " "
				+ " from [PCMS].[dbo].[FromSapSaleInput] as a \r\n " + where + " Order by [No]";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SaleInputDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSaleInputDetail(map));
		}
		return list;
	}

	private ArrayList<SaleDetail> getSaleDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<SaleDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[BillDate]\r\n"
				+ "      ,[BillQtyPerSale],[SaleOrder]"
				+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "		WHEN 0 THEN ''  \r\n"
				+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "		END AS [SaleLine] "
				+ "      ,[BillQtyPerStock],[Remark],[CustomerNo]\r\n"
				+ "      ,[CustomerName1],[CustomerPO],[DueDate]\r\n"
				+ "      ,[Color],[No],[DataStatus]\r\n" + "  "
				+ " from [PCMS].[dbo].[FromSapSale] as a \r\n " + where + " Order by [No]";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SaleDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSaleDetail(map));
		}
		return list;
	}

	private ArrayList<CFMDetail> getCFMDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<CFMDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[CFMNo],[CFMNumber]\r\n"
				+ "      ,[CFMSendDate],[CFMAnswerDate],[CFMStatus]\r\n"
				+ "      ,[CFMRemark],[Da],[Db],[L]\r\n"
				+ "      ,[ST],[SaleOrder]"
				+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "		WHEN 0 THEN ''  \r\n"
				+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "		END AS [SaleLine] "
				+ "     ,[CFMCheckLab]\r\n"
				+ "      ,[CFMNextLab],[CFMCheckLot],[CFMNextLot]\r\n"
				+ "      ,[NextLot],[SOChange],[SOChangeQty]\r\n"
				+ "      ,[SOChangeUnit],[RollNo],[RollNoRemark]\r\n"
				+ "      ,[DataStatus]\r\n" + " " + " from [PCMS].[dbo].[FromSapCFM] as a \r\n " + where
				+ " Order by [CFMNo]";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<CFMDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCFMDetail(map));
		}
		return list;
	}

	private ArrayList<WaitTestDetail> getWaitTestDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<WaitTestDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[No],[DateInTest]\r\n"
				+ "      ,[DateOutTest],[Status],[Remark],[DataStatus]\r\n"
				+ " " + " from [PCMS].[dbo].[FromSapWaitTest] as a \r\n " + where + " Order by [No]";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<WaitTestDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genWaitTestDetail(map));
		}
		return list;
	}

	private ArrayList<WorkInLabDetail> getWorkInLabDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<WorkInLabDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + "  [ProductionOrder],[SaleOrder]\r\n"
				+ "      ,[SaleLine],[No],[SendDate],[NOK]\r\n"
				+ "      ,[LotNo],[ReceiveDate],[Remark],[Da]\r\n"
				+ "      ,[Db],[L],[ST],[DataStatus]\r\n" + "   "
				+ " from [PCMS].[dbo].[FromSapWorkInLab] as a \r\n " + where + " Order by No";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<WorkInLabDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genWorkInLabDetail(map));
		}
		return list;
	}

	private ArrayList<PackingDetail> getPackingDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PackingDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectPacking + " from [PCMS].[dbo].[FromSapPacking] as a \r\n "
				+ where;
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PackingDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPackingDetail(map));
		}
		return list;
	}

	private ArrayList<FinishingDetail> getFinishingDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<FinishingDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectFinishing + " from [PCMS].[dbo].[FromSapFinishing] as a \r\n "
				+ where + " Order by Operation";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<FinishingDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFinishingDetail(map));
		}
		return list;
	}

	private ArrayList<InspectDetail> getInspectDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<InspectDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectInspect + " from [PCMS].[dbo].[FromSapInspect] as a \r\n " + where
				+ " Order by Operation";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InspectDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInspectDetail(map));
		}
		return list;
	}

	private ArrayList<SendTestQCDetail> getSendTestQCDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<SendTestQCDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectSendTestQC + " from [PCMS].[dbo].[FromSapSendTestQC] as a \r\n "
				+ where;
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SendTestQCDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSendTestQCDetail(map));
		}
		return list;
	}

	private ArrayList<DyeingDetail> getDyeingDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<DyeingDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectDyeing + " from [PCMS].[dbo].[FromSapDyeing] as a \r\n " + where
				+ " Order by Operation";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<DyeingDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genDyeingDetail(map));
		}
		return list;
	}

	private ArrayList<PresetDetail> getPresetDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PresetDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectPreset + " from [PCMS].[dbo].[FromSapPreset] as a \r\n " + where;
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PresetDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPresetDetail(map));
		}
		return list;
	}

	public ArrayList<PODetail> getPODetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PODetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectPO + " from [PCMS].[dbo].[FromSapPO] as a \r\n " + where
				+ " Order by [RollNo]";
//		      
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PODetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSTableDetail> getSaleNumberList() {
		ArrayList<PCMSTableDetail> list = null;
		String sql = "SELECT DISTINCT  " + "	   a.SaleNumber\r\n"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" + "    		 WHEN 0 THEN ''   \r\n"
				+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+[SaleFullName]\r\n"
				+ "    		 END AS [SaleFullName]   " + "   FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n "
				+ " where SaleNumber <> '00000000'" + " Order by [SaleNumber]";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getUserStatusList() {
		ArrayList<PCMSAllDetail> list = null;
		String where = " where UserStatus <> '' \r\n"; 
		String sql = 
				  "SELECT distinct [UserStatus] \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainProd] \r\n "
			    + where
				+ " order by UserStatus \r\n"; 
		  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		PCMSAllDetail bean = new PCMSAllDetail();
		bean.setUserStatus("รอ COA ลูกค้า ok สี");
		list.add(bean);
		bean = new PCMSAllDetail();
		bean.setUserStatus("ขายแล้วบางส่วน");
		list.add(bean);  
		bean = new PCMSAllDetail();
		bean.setUserStatus("รอตอบ CFM ตัวแทน");
		list.add(bean);
		bean = new PCMSAllDetail();
		bean.setUserStatus("รอเปิดบิล");
		list.add(bean);
		return list;
	}

	@Override
	public ArrayList<ColumnHiddenDetail> getColVisibleDetail(String user) {
		ArrayList<ColumnHiddenDetail> list = null;  
		String sql = 
				    " SELECT distinct [EmployeeID] ,[ColVisibleDetail] ,[ColVisibleSummary]\r\n"
		 		  + " FROM [PCMS].[dbo].[ColumnSetting] \r\n "
		 		  + " where [EmployeeID] = '" + user+ "' ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);  
		list = new ArrayList<ColumnHiddenDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genColumnHiddenDetail(map));
		}
		return list;
	}  

	@Override
	public ArrayList<ColumnHiddenDetail> saveColSettingToServer(ColumnHiddenDetail pd) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String colName = pd.getColVisibleSummary();   
		String user = pd.getUserId(); 
		ArrayList<ColumnHiddenDetail> list = new ArrayList<ColumnHiddenDetail>();
		ColumnHiddenDetail bean = new ColumnHiddenDetail();
//		ArrayList<ColumnHiddenDetail> beanCheck = this.getColHiddenDetail(leftJoinB);
//		if(beanCheck.size() > 0) { 
//		} 
		try {      
			String sql = 
					"UPDATE [PCMS].[dbo].[ColumnSetting] "
					+ " SET [ColVisibleSummary] = ?  "
					+ " WHERE [EmployeeID]  = ? "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ "  if @rc <> 0 " 
					+ " print @rc " 
					+ " else "
					+ " INSERT INTO [PCMS].[dbo].[ColumnSetting]	 "
					+ " ([EmployeeID] ,[ColVisibleSummary])"//55 
					+ " values(? , ? )  ;"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, colName);
				prepared.setString(2, user);
				prepared.setString(3, user);
				prepared.setString(4, colName);  
				prepared.executeUpdate();   
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.out.println("saveColSettingToServer"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerNameList() {
		ArrayList<PCMSAllDetail> list = null; 
		String sql = 
				  "SELECT distinct [CustomerName] \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
				+ " order by [CustomerName] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerShortNameList() {
		ArrayList<PCMSAllDetail> list = null; 
		String sql =   
				  "SELECT distinct [CustomerShortName]  \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
				+ " order by  [CustomerShortName] \r\n"; 
		  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}
	private String forPage = "Summary"; 
	@Override 
	public ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList) { 
		ArrayList<PCMSTableDetail> list = null;
		String where = " where  ";
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,divisionName = "";
		PCMSTableDetail bean = poList.get(0);  
//		System.out.println(bean.toString());
		userId = bean.getUserId();
		List<String> userStatusList = bean.getUserStatusList();
		List<String> cusNameList = bean.getCustomerNameList();
		List<String> cusShortNameList = bean.getCustomerShortNameList(); 
		List<String> divisionList = bean.getDivisionList(); 
		if (cusNameList.size() > 0) {  
			String text = "";
			for (int i = 0; i < cusNameList.size(); i++) {
				text = cusNameList.get(i);
				customerName += text  ;
				if (i != cusNameList.size() - 1) {
					customerName += "|";
				} ;
			} ;
		}
		if (divisionList.size() > 0) {  
			String text = "";
			for (int i = 0; i < divisionList.size(); i++) {
				text = divisionList.get(i);
				divisionName += text  ;
				if (i != divisionList.size() - 1) {
					divisionName += "|";
				} ;
			} ;
		}
		if (cusShortNameList.size() > 0) {  
			String text = "";
			for (int i = 0; i < cusShortNameList.size(); i++) {
				text = cusShortNameList.get(i);
				customerShortName += text;
				if (i != cusShortNameList.size() - 1) {
					customerShortName += "|";
				} ;
			}
			where += " ) \r\n";
		}
		if (userStatusList.size() > 0) {  
			String text = "";
			for (int i = 0; i < userStatusList.size(); i++) {
				text = userStatusList.get(i);
				userStatus +=  text ;
				if (i != userStatusList.size() - 1) {
					userStatus += "|";
				} ; 
			} ;
		}     
		poList.get(0).setDivision(divisionName);
		poList.get(0).setUserStatus(userStatus);
		poList.get(0).setCustomerName(customerName);
		poList.get(0).setCustomerShortName(customerShortName); 
		ArrayList<PCMSTableDetail> beanCheck = this.getSearchSettingDetail(userId,this.forPage);
		if(beanCheck.size() == 0) {
			list = this.insertSearchSettingDetail(poList);
		}
		else {
			list = this.updateSearchSettingDetail(poList);
		} 
		return list; 
	}

	private ArrayList<PCMSTableDetail> updateSearchSettingDetail(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();    
		ArrayList<PCMSTableDetail> list = new ArrayList<PCMSTableDetail>(); 
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,division = "";
		PCMSTableDetail bean = poList.get(0); 
		userId = bean.getUserId();
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();
		saleCreateDate = bean.getSaleOrderCreateDate();
		saleNumber = bean.getSaleNumber();
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		dueDate = bean.getDueDate();
		designFG = bean.getDesignFG();  
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();   
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel();    
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
		userStatus = bean.getUserStatus();  
		division = bean.getDivision();
		int no = 1;   
		try {      
			String sql = 
					"UPDATE [dbo].[SearchSetting]\r\n"
					+ "   SET [No] = ?  ,[CustomerName] = ?,[CustomerShortName] = ?\r\n"
					+ "      ,[SaleOrder] = ? ,[ArticleFG] = ? ,[DesignFG] =  ? \r\n"
					+ "      ,[ProductionOrder] = ? ,[SaleNumber] = ? ,[MaterialNo] = ?\r\n"
					+ "      ,[LabNo] = ? ,[DeliveryStatus] = ? ,[DistChannel] = ?\r\n"
					+ "      ,[SaleStatus] = ? ,[DueDate] = ? ,[SaleCreateDate] = ? \r\n"
					+ "      ,[PrdCreateDate] = ? ,[UserStatus] = ? , [Division] = ? \r\n"
					+ "      where  [EmployeeID] = ? and [ForPage] = ?" ;     	
			prepared = connection.prepareStatement(sql);    
//			prepared.setString(1, userId);
			prepared.setInt(1, no);
			prepared.setString(2, customerName);
			prepared.setString(3, customerShortName);  
			prepared.setString(4, saleOrder);  
			prepared.setString(5, articleFG);  
			prepared.setString(6, designFG);  
			prepared.setString(7, prdOrder);  
			prepared.setString(8, saleNumber);  
			prepared.setString(9, materialNo);  
			prepared.setString(10, labNo);  
			prepared.setString(11, deliveryStatus);  
			prepared.setString(12, dist);  
			prepared.setString(13, saleStatus);  
			prepared.setString(14, dueDate);  
			prepared.setString(15, saleCreateDate);  
			prepared.setString(16, prdCreateDate);  
			prepared.setString(17, userStatus);
			prepared.setString(18, division);
			prepared.setString(19, userId);
			prepared.setString(20, this.forPage);
			prepared.executeUpdate();   
			bean.setIconStatus("I");
			bean.setSystemStatus("save Success.");
		} catch (SQLException e) {
			System.out.println("updateSearchSettingDetail"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}

	private ArrayList<PCMSTableDetail> insertSearchSettingDetail(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();    
		ArrayList<PCMSTableDetail> list = new ArrayList<PCMSTableDetail>(); 
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,division = "";
		PCMSTableDetail bean = poList.get(0); 
		userId = bean.getUserId();
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();
		saleCreateDate = bean.getSaleOrderCreateDate();
		saleNumber = bean.getSaleNumber();
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		dueDate = bean.getDueDate();
		designFG = bean.getDesignFG();  
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();   
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel();    
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
		userStatus = bean.getUserStatus();  
		division = bean.getDivision();
		int no = 1;   
		try {      
			String sql = 
					"INSERT INTO [dbo].[SearchSetting]\r\n"
					+ "           ( [EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
					+ "           ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
					+ "           ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
					+ "           ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[ForPage],[Division] \r\n"
					+ "           )\r\n"
					+ "     VALUES\r\n"
					+ "           ( "
					+ "            ? , ? , ? , ? , ?, "
					+ "            ? , ? , ? , ? , ?,"
					+ "            ? , ? , ? , ? , ?,"
					+ "            ? , ? , ? , ? , ?"
					+ "           )"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, userId);
				prepared.setInt(2, no);
				prepared.setString(3, customerName);
				prepared.setString(4, customerShortName);  
				prepared.setString(5, saleOrder);  
				prepared.setString(6, articleFG);  
				prepared.setString(7, designFG);  
				prepared.setString(8, prdOrder);  
				prepared.setString(9, saleNumber);  
				prepared.setString(10, materialNo);  
				prepared.setString(11, labNo);  
				prepared.setString(12, deliveryStatus);  
				prepared.setString(13, dist);  
				prepared.setString(14, saleStatus);  
				prepared.setString(15, dueDate);  
				prepared.setString(16, saleCreateDate);  
				prepared.setString(17, prdCreateDate);  
				prepared.setString(18, userStatus);  
				prepared.setString(19, this.forPage);  
				prepared.setString(20, division);  
				prepared.executeUpdate();   
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.out.println("insertSearchSettingDetail"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}

	@Override  
	public ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		String userId = poList.get(0).getUserId();
		ArrayList<PCMSTableDetail> bean = this.getSearchSettingDetail(userId,this.forPage);
		return bean;
	}
	public ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId,String forPage) {
		ArrayList<PCMSTableDetail> list = null; 
		String sql =   
				  "SELECT "    
				  + " [EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
				  + "      ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
				  + "      ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
				  + "      ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[Division]\r\n"
				  + "  FROM [PCMS].[dbo].[SearchSetting]\r\n"
				  + " where EmployeeID = '"+userId+"' and ForPage = '"+forPage+ "' "; 
//		  System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSearchTableDetail(map));
		} 
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getDivisionList() {
		ArrayList<PCMSSecondTableDetail> list = null; 
		String sql =    
				  "SELECT distinct [Division] \r\n"   
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n"
				+ " where Division <> '' " 
				+ " order by [Division] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		} 
		return list;
	}  
	public String handlerUserStatus(String userStatusPara,String dist ,String gradePrd , String qaApproveBy,
			String finishGR,String lastCFMStatus,String isAnita) {
		String userStatus = "";
//		DM
		if (gradePrd.equals("A") && !finishGR.equals("")) {
			// CFM = 'N' AND QAAPPROVED = ''
			if (lastCFMStatus.equals("N") && qaApproveBy.equals("")) { userStatus = "รอสรุป QA"; }
			// CFM = 'Y' AND QAAPPROVED = ''
			else if (lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอ COA ลูกค้า ok สี"; }
			// CFM = ''
			else if (lastCFMStatus.equals("")) { userStatus = "รอตอบ CFM"; }
			// CFM = 'Y' AND QAAPPROVED = 'XX/XX/XXXX'
			else if (lastCFMStatus.equals("Y") && !qaApproveBy.equals("")) { userStatus = "รอขาย"; }
		} else if (gradePrd.equals("C") && !finishGR.equals("")) {
			// CFM = 'N' AND QAAPPROVED = ''
			if (lastCFMStatus.equals("N") && qaApproveBy.equals("")) { userStatus = "รอสรุป QA"; }
			// CFM = 'Y' AND QAAPPROVED = ''
			else if (lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอ COA ลูกค้า ok สี"; } 
			// CFM = ''
			else if (lastCFMStatus.equals("")) { userStatus = "รอตอบ CFM"; } 
			// CFM = 'Y' AND QAAPPROVED = 'XX/XX/XXXX'
			else if (lastCFMStatus.equals("Y") && !qaApproveBy.equals("")) { userStatus = "รอขาย"; } 
			// CFM = 'Y' AND userStatusParaSAP = 'Hold รอโอน'
//			else if (userStatusPara.equals("Hold รอโอน")) { userStatus = "Hold รอโอน"; } 
		} else if (gradePrd.equals("Z")) { userStatus = "ตัดเกรด Z"; } 

//		EX
		if (gradePrd.equals("A") && !finishGR.equals("")) {
			// CFM = 'N' AND QAAPPROVED = ''
			if (lastCFMStatus.equals("N") && qaApproveBy.equals("")) { userStatus = "รอสรุป QA"; }
			else if (lastCFMStatus.equals("")) { userStatus = "รอตอบ CFM"; }
			// CUSTOMER = 'ANITA' CFM = 'Y' AND QAAPPROVED = ''
			else if (isAnita.equals("Y") && lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอตอบ CFM ตัวแทน"; }
			// CUSTOMER <> 'ANITA' CFM = 'Y' AND QAAPPROVED = ''
			else if (isAnita.equals("N") && lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอ COA ลูกค้า ok สี"; } 
			// CFM = 'Y' AND QAAPPROVED = 	'XX/XX/XXXX'
			else if (lastCFMStatus.equals("Y") && !qaApproveBy.equals("")) { userStatus = "รอแจ้งส่ง"; }
		} else if (gradePrd.equals("C") && !finishGR.equals("")) {
			// CFM = 'N' AND QAAPPROVED = ''
			if (lastCFMStatus.equals("N") && qaApproveBy.equals("")) { userStatus = "รอสรุป QA"; }
			// CUSTOMER = 'ANITA' CFM = 'Y' AND QAAPPROVED = ''
			else if (isAnita.equals("Y") && lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอตอบ CFM ตัวแทน"; }
			// CUSTOMER <> 'ANITA' CFM = 'Y' AND QAAPPROVED = ''
			else if (isAnita.equals("N") && lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอ COA ลูกค้า ok สี "; } 
			else if (lastCFMStatus.equals("") ) { userStatus = "รอตอบ CFM"; }
			// CFM = 'Y'AND QAAPPROVED = 'XX/XX/XXXX'
			else if (lastCFMStatus.equals("Y")  && !qaApproveBy.equals("")) { userStatus = "รอแจ้งส่ง";  }   
		} else if (gradePrd.equals("Z") && !finishGR.equals("")) { userStatus = "ตัดเกรด Z"; } 
		return userStatus;
	} 
}
