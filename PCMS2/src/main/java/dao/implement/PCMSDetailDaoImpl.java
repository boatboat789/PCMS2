package dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dao.PCMSDetailDao;
import entities.ColumnHiddenDetail;
import entities.InputDateDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import entities.ReplacedProdOrderDetail;
import entities.SwitchProdOrderDetail;
import entities.TempUserStatusAutoDetail;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.master.FromSapMainProdModel;
import model.master.PlanCFMDateModel;
import model.master.PlanCFMLabDateModel;
import model.master.PlanDeliveryDateModel;
import model.master.ReplacedProdOrderModel;
import model.master.SearchSettingModel;
import model.master.SwitchProdOrderModel;
import model.master.TEMP_UserStatusAutoModel;
import service.BackGroundJob;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class PCMSDetailDaoImpl implements PCMSDetailDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private String C_PRODORDER = "ProductionOrder";
	private String C_PRODORDERRP = "ProductionOrderRP";
	private String CLOSE_STATUS = "X";
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	private String createTempPlanDeliveryDate = 
			  "  If(OBJECT_ID('tempdb..#tempPlandeliveryDate') Is Not Null)\r\n"
			  + "	begin\r\n"
			  + "		Drop Table #tempPlandeliveryDate\r\n"
			  + "	end ; \r\n"
			  + " SELECT distinct  a.id,a.[ProductionOrder] ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
			  + " into #tempPlandeliveryDate\r\n"
			  + " FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
			  + " inner join (\r\n"
			  + "	select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max(Id) as maxId\r\n"
			  + "	FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
			  + "	group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]\r\n"
			  + " ) as b on a.Id = b.maxId  \r\n"  ;  

	private String declareTempApproved = "\r\n"
			+ " 	IF OBJECT_ID('tempdb..#tempPOMainNPOInstead') IS NOT NULL \r\n"
			+ "					DROP TABLE #tempPOMainNPOInstead;  \r\n"
			+ "			 select\r\n"
			+ "			 a.[Id] \r\n"
			+ "				,case\r\n"
			+ "					when C.POIdFrom is not null then C.POIdFrom \r\n"
			+ "					ELSE A.POId\r\n"
			+ "				END AS POId\r\n"
			+ "			 ,a.[ForecastId] ,a.[PlanInsteadId] ,a.[RuleNo] ,a.[ColorType]\r\n"
			+ "			 ,a.[ProductionOrder] ,a.[FirstLot] ,a.[ProdOrderQty] ,a.[GroupOptions]\r\n"
			+ "			 ,a.[GroupBegin] ,a.[PPMMStatus] ,a.[DataStatus] ,a.[ChangeDate]\r\n"
			+ "			 ,a.[ChangeBy] ,a.[CreateDate] ,a.[CreateBy] ,a.[Batch]\r\n"
			+ "			 INTO #tempPOMainNPOInstead\r\n"
			+ "			 from [PPMM].[dbo].[SOR_TempProd] as a\r\n"
			+ "			 left join [PPMM].[dbo].[PlanInsteadProcessId] as b on a.PlanInsteadId = b.Id\r\n"
			+ "			 left join [PPMM].[dbo].[PlanInsteadProdOrder] as c on c.PlanInsteadProcessId = b.Id\r\n"
			+ "			 where ( a.POId is not null or a.PlanInsteadId is not null) and a.DataStatus = 'O'\r\n"
			+ "				  AND ( B.DataStatus = 'O' or B.DataStatus  is null )\r\n"
			+ "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempApproved') IS NOT NULL   \r\n"
			+ "	  DROP TABLE #tempApproved\r\n"
			+ " select a.ProductionOrder,b.SORCFMDate,b.SORDueDate\r\n"
			+ " into #tempApproved\r\n"
			+ "	from #tempPOMainNPOInstead as a  \r\n"
			+ "	inner join [PPMM].[dbo].[ApprovedPlanDate] as b on a.POId = b.POId \r\n"  ;
	private String createTempSumBill = 
			  " If(OBJECT_ID('tempdb..#tempSumBill') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempSumBill\r\n"
			+ "	end ;\r\n"
			+ " SELECT distinct \r\n"
			+ " 	[ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[Grade]  ,"
			+ " 	SUM([QuantityKG]) AS [BillSendWeightQuantity], \r\n"
			+ " 	SUM([QuantityYD]) AS [BillSendYDQuantity] ,\r\n"
			+ " 	SUM([QuantityMR]) AS [BillSendMRQuantity] \r\n"
			+ " into #tempSumBill \r\n" 
			+ " FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			+ " where DataStatus = 'O'\r\n"
			+ " GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[Grade] \r\n"  ;   
	private String createTempMainSale = ""
			+ " If(OBJECT_ID('tempdb..#tempMainSale') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempMainSale\r\n"
			+ "	end ; "
			+ " SELECT DISTINCT \r\n"
			+ "	   a.*\r\n"
			+ "	  ,b.CustomerType \r\n"
			+ "	  ,a.[Division] AS CustomerDivision\r\n" 
			+ " INTO #tempMainSale \r\n"
			+ " FROM [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			+ " left join [PCMS].[dbo].[ConfigCustomerEX] as b on a.[CustomerNo] = b.[CustomerNo] and b.[DataStatus] = 'O'\r\n" 
			;

private String createTempMainPrdFromTempA = 
			 " If(OBJECT_ID('tempdb..#tempMainPrdTemp') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempMainPrdTemp\r\n"
			+ "	end ;\r\n"
			+ " \r\n"
			 + "				SELECT distinct \r\n"
			  + "						 a.SaleOrder ,a.SaleLine \r\n"
			  + "						,CASE  \r\n"
			  + "							WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
			  + "							WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
			  + "					   		WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
			  + "							WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
			  + "							ELSE 'รอจัด Lot'  \r\n"
			  + "							END AS [ProductionOrder]  \r\n"
			  + "				 		,CASE  \r\n"
			  + "							WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
			  + "							WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
			  + "					   		WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
			  + "							WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
			  + "							ELSE 'รอจัด Lot'  \r\n"
			  + "							END AS [LotNo]   \r\n"
			  + "				 		,[TotalQuantity] ,[Unit]\r\n"
			  + "				 		,[RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
			  + "				 		,a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
			  + "				 		,[Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
			  + "				 		,[OrderPuang] ,[RefPrd],b.[GreigeInDate] ,[BCDate],b.[Volumn]\r\n"
			  + "				 		,[CFdate],[CFType],[Shade] ,g.[LotShipping],[BillSendQuantity] \r\n"
			  + "				 		,[PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG], g.[PlanGreigeDate]  \r\n"
			  + "               into #tempMainPrdTemp\r\n"
			  + "				from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			  + "				left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
			  + "			   	left join ( SELECT distinct [SaleOrder],[SaleLine] ,1 as [CheckBill] \r\n"
			  + "							FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			  + "							where DataStatus = 'O'\r\n"
			  + "						   	group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine]  \r\n"
			  + " 				left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n" ;

	private String selectWaitLot = 
  		    "   a.SaleOrder \r\n"
  		  + "   , CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
  		  + "		WHEN 0 THEN '' \r\n"
  		  + "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) \r\n"
  		  + "		END AS [SaleLine] \r\n"
  		  + "   , Division\r\n"
  		  + "   , CustomerShortName\r\n"
  		  + "   , SaleCreateDate\r\n"
  		  + "   , PurchaseOrder\r\n"
  		  + "   , MaterialNo\r\n"
  		  + "   , CustomerMaterial\r\n"
  		  + "   , Price\r\n"
  		  + "   , SaleUnit\r\n"
  		  + "   , OrderAmount\r\n"
  		  + "   , SaleQuantity\r\n"
  		  + "   , RemainQuantity\r\n"
  		  + "   , RemainAmount \r\n"
  		  + "   , TotalQuantity \r\n"
  		  + "   , Grade \r\n"
  		  + "   , BillSendWeightQuantity \r\n"
  		  + "   , BillSendQuantity  \r\n"
  		  + "   , BillSendMRQuantity \r\n"
  		  + "   , BillSendYDQuantity \r\n"
  		  + "   , CustomerDue\r\n"
  		  + "   , DueDate \r\n"
  		  + "   , b.ProductionOrder\r\n"
  		  + "   , b.LotNo \r\n"
  		  + "   , LabNo\r\n"
  		  + "   , LabStatus\r\n"
  		  + "   , CFMPlanLabDate\r\n"
  		  + "   , CFMActualLabDate \r\n"
  		  + "   , CFMCusAnsLabDate \r\n"
  		  + "   , UserStatus \r\n"
  		  + "   , TKCFM \r\n"
  		  + "   , CFMPlanDate \r\n" 
  		  + "   , SendCFMCusDate\r\n" 
  		  + "   , DeliveryDate  \r\n" 
  		  + "   , CFMDateActual\r\n"                    
		  + "   , CFMDetailAll \r\n"
   		  + "   , CFMNumberAll  \r\n"
   		  + "   , CFMRemarkAll \r\n"
   		  + "   , RollNoRemarkAll \r\n"
  		  + "   , a.ShipDate \r\n"
  		  + "   , RemarkOne \r\n"
  		  + "   , RemarkTwo \r\n"
  		  + "   , RemarkThree \r\n"
  		  + "   , ReplacedRemark \r\n"
  		  + "   , StockRemark\r\n" 
  		  + "   , GRSumKG \r\n"
  		  + "   , GRSumYD \r\n"
  		  + "   , GRSumMR \r\n"
  		  + "   , DyePlan \r\n "	
  		  + "   , DyeActual \r\n "	
//  		  + "   , CASE \r\n"
//  		  + "		WHEN ( DyePlan is NULL) THEN null \r\n"
//  		  + "       ELSE CAST(DAY( DyePlan) AS VARCHAR(2)) + '/' +   CAST(MONTH( DyePlan)AS VARCHAR(2))  \r\n"
//  		  + "  		END AS DyePlan \r\n "	
//  		  + "   , CASE \r\n"
//  		  + "		WHEN ( DyeActual is NULL) THEN null \r\n"
//  		  + "       ELSE CAST(DAY( DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( DyeActual)AS VARCHAR(2))  \r\n"
//  		  + "  		END AS DyeActual \r\n "				
//  		  + ", DyePlan \r\n"
//  		  + ", DyeActual  \r\n" 
  		  + "   , PCRemark\r\n" 
	      + "   , [DelayedDep] \r\n"
	      + "   , [CauseOfDelay] \r\n"
  		  + "   , [SwitchRemark]\r\n"
  		  + "   , SL.[StockLoad] \r\n"
  		  + "   , [PrdCreateDate]\r\n"
  		  + "   , LotShipping \r\n" 
  		  + "   , Volumn \r\n"
  		  + "   , VolumnFGAmount  \r\n"  
  		  + "   , 'WaitLot' as TypePrd \r\n"
  		  + "   , 'WaitLot' AS TypePrdRemark  \r\n" 
  		  + "   , CustomerType\r\n";  
	private String selectOPV2 = 
	  		  "   a.SaleOrder ,\r\n"
	  		+ "	  CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
	  		+ "		WHEN 0 THEN '' \r\n"
	  		+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) "
	  		+ "		END AS [SaleLine] ,\r\n"
	  		+ "   Division,\r\n" 
			+ "   CustomerName,--ADD\r\n"
			+ "   SaleStatus,--ADD\r\n"
			+ "   DistChannel,--ADD\r\n"
	  		+ "   CustomerShortName,	\r\n"
	  		+ "   SaleCreateDate,\r\n"
	  		+ "   PurchaseOrder,\r\n"
	  		+ "   MaterialNo,\r\n"     
	  		+ "   CustomerMaterial,\r\n"
	  		+ "   Price,\r\n"
	  		+ "   SaleUnit,\r\n"
	  		+ "   OrderAmount,\r\n"
	  		+ "   SaleQuantity,\r\n"   
	  		+ "   RemainQuantity,\r\n"
	  		+ "   RemainAmount,\r\n"
	  		+ "   TotalQuantity,\r\n"    
	  		+ "   a.Grade,\r\n"   
	  		+ "   a.BillSendWeightQuantity,\r\n"
	  		+ "   a.BillSendQuantity,\r\n"  
	  		+ "   a.BillSendMRQuantity,\r\n"  
	  		+ "   a.BillSendYDQuantity,\r\n"    
	  		+ "   CustomerDue,\r\n"
	  		+ "   DueDate,\r\n"  
	  		+ "   b.ProductionOrder,\r\n"   
	  		+ "   b.LotNo,\r\n"  
	  		+ "   b.LabNo,\r\n" 
	  		+ "   LabStatus,\r\n"
	  		+ "   e.CFMPlanLabDate,\r\n"
	  		+ "   a.CFMActualLabDate,\r\n"
	  		+ "   a.CFMCusAnsLabDate,\r\n"
	  		+ "   a.UserStatusCal as UserStatus,\r\n"
	  		+ "   CASE\r\n"
	  		+ "		WHEN TAPP.SORCFMDate IS NOT NULL THEN TAPP.SORCFMDate\r\n"
	  		+ "		ELSE j.CFMDate \r\n"
	  		+ "		END as TKCFM,\r\n"  
//	  		+ "   j.CFMDate as TKCFM,\r\n"  
	  		+ "   a.CFMPlanDate AS CFMPlanDate ,  \r\n"
	  		+ "   a.SendCFMCusDate, \r\n"
	  		+ "   CASE \r\n"
	  		+ "		WHEN h.[ProductionOrder] is not null THEN H.DeliveryDate \r\n"
	  		+ "		ELSE b.CFTYPE \r\n"
	  		+ "		END AS DeliveryDate , \r\n" 
	  		+ "   a.CFMDateActual, \r\n"
	 	    + "   a.CFMDetailAll, \r\n"
		    + "   a.CFMNumberAll, \r\n"
		    + "   a.CFMRemarkAll, \r\n"
		    + "   a.RollNoRemarkAll , \r\n"
	  		+ "   ShipDate,\r\n"
	  		+ "   RemarkOne,\r\n"
	  		+ "   RemarkTwo,\r\n"
	  		+ "   RemarkThree ,\r\n"   
	  		+ "   ReplacedRemark ,\r\n"
	  		+ "   StockRemark,\r\n"    
	  		+ "   GRSumKG,\r\n"
	  		+ "   GRSumYD,\r\n"   
	  		+ "   GRSumMR,\r\n"   
		    + "   a.DyePlan, \r\n "	
		    + "   a.DyeActual, \r\n "	 
			+ "   PCRemark, \r\n"
	  		+ "   [DelayedDep], \r\n"
	  		+ "   [CauseOfDelay], \r\n"
			+ "   q.[SwitchRemark],\r\n"
			+ "   SL.[StockLoad], \r\n"
			+ "   b.[PrdCreateDate],\r\n"
	  	    + "   a.LotShipping \r\n" ;    
    private String selectSW = 
  		  "   a.SaleOrder, \r\n"
  		+ "	  CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
  		+ "		WHEN 0 THEN '' \r\n"
  		+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) \r\n"
  		+ "		END AS [SaleLine] ,\r\n"
  		+ "   Division,\r\n"  
  		+ "   CustomerShortName,	\r\n"
  		+ "   SaleCreateDate,\r\n"
  		+ "   PurchaseOrder,\r\n"
  		+ "   MaterialNo,\r\n"     
  		+ "   CustomerMaterial,\r\n"
  		+ "   Price,\r\n"
  		+ "   SaleUnit,\r\n"
  		+ "   OrderAmount,\r\n"
  		+ "   SaleQuantity,\r\n"   
  		+ "   RemainQuantity,\r\n"
  		+ "   RemainAmount,\r\n"
  		+ "   TotalQuantity,\r\n"    
  		+ "    m.Grade,\r\n"
  		+ "   FSMBB.BillSendWeightQuantity,\r\n"
  		+ "  case\r\n"
  		+ "					WHEN a.SaleUnit  = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
  		+ "					WHEN a.SaleUnit  = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
  		+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
  		+ "				end AS BillSendQuantity ,\r\n"
  		+ "   FSMBB.BillSendMRQuantity,\r\n"
  		+ "   FSMBB.BillSendYDQuantity,\r\n"    
  		+ "   CustomerDue,\r\n"
  		+ "   DueDate,\r\n"  
  		+ "   b.ProductionOrder,\r\n"   
  		+ "   b.LotNo,\r\n"  
  		+ "   b.LabNo,\r\n" 
  		+ "   LabStatus,\r\n"
  		+ "   e.CFMPlanLabDate,\r\n"
  		+ "   g.CFMActualLabDate,\r\n"
  		+ "   g.CFMCusAnsLabDate,\r\n"
  		+ "   UCAL.UserStatusCal as UserStatus,\r\n" 
	  		+ "   CASE\r\n"
	  		+ "		WHEN TAPP.SORCFMDate IS NOT NULL THEN TAPP.SORCFMDate\r\n"
	  		+ "		ELSE j.CFMDate \r\n"
	  		+ "		END as TKCFM,\r\n"  
  		+ "   g.CFMPlanDate AS CFMPlanDate ,  \r\n" 
  		+ "   CASE \r\n "
  		+ "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> '' THEN SCC.SendCFMCusDate \r\n"
 		  	+ "     ELSE  g.SendCFMCusDate \r\n"     
 		  	+ "    	END AS SendCFMCusDate ,\r\n"
  		+ "   CASE \r\n"
  		+ "		WHEN h.[ProductionOrder] is not null \r\n"
  		+ "		THEN H.DeliveryDate ELSE b.CFTYPE \r\n"
  		+ "		END AS DeliveryDate , \r\n"
  			   + "   CFMDateActual,\r\n"   
//  		+ "   g.CFMSendDate,\r\n" 
//  		+ "   g.CFMAnswerDate,\r\n" 
//    		+ "   g.CFMStatus,\r\n" 
//    		+ "   g.CFMNumber,\r\n" 
//    		+ "   g.CFMRemark,\r\n"  
	 	    + "   g.CFMDetailAll, \r\n"
 		    + "   g.CFMNumberAll,  \r\n"
 		    + "   g.CFMRemarkAll, \r\n"
		    + "   g.RollNoRemarkAll , \r\n"
  		+ "   ShipDate,\r\n"
  		+ "   RemarkOne,\r\n"
  		+ "   RemarkTwo,\r\n"
  		+ "   RemarkThree ,\r\n"      
  		+ "   ReplacedRemark ,\r\n"
  		+ "   StockRemark,\r\n "
  		+ "   GRSumKG,\r\n"
  		+ "   GRSumYD,\r\n"   
  		+ "   GRSumMR,\r\n"   
//		    + "   CASE \r\n"
//		    + "		WHEN ( g.DyePlan is NULL) THEN null \r\n"
//		    + "     ELSE CAST(DAY( g.DyePlan) AS VARCHAR(2)) + '/' + CAST(MONTH( g.DyePlan)AS VARCHAR(2))  \r\n"
//		    + "     END AS DyePlan , \r\n "	
//		    + "   CASE \r\n"
//		    + "		WHEN ( g.DyeActual is NULL) THEN null \r\n"
//		    + "     ELSE CAST(DAY( g.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( g.DyeActual)AS VARCHAR(2))  \r\n"
//		    + "     END AS DyeActual , \r\n "	
  		+ "   g.DyePlan , \r\n"
			+ "   g.DyeActual, \r\n"
			+ "   PCRemark, \r\n"
	  		+ "   [DelayedDep], \r\n"
	  		+ "   [CauseOfDelay], \r\n"
			+ "   q.[SwitchRemark],\r\n"
			+ "   SL.[StockLoad], \r\n"
			+ "   b.[PrdCreateDate],\r\n"
	  		+ "   g.LotShipping\r\n"
	  		+ "   , g.PlanGreigeDate ";  
    private String selectMainV2 = 
    		  "   b.SaleOrder, \r\n"
    		+ "   CASE PATINDEX('%[^0 ]%', b.[SaleLine]  + ' ‘') \r\n"
    		+ "		WHEN 0 THEN '' ELSE SUBSTRING(b.[SaleLine] , PATINDEX('%[^0 ]%', b.[SaleLine]  + ' '), LEN(b.[SaleLine] ) ) \r\n"
    		+ "		END AS [SaleLine] ,\r\n"
    		+ "   Division,\r\n"  
    		+ "   CustomerShortName,\r\n"
    		+ "   SaleCreateDate,\r\n"
    		+ "   PurchaseOrder,\r\n"
    		+ "   MaterialNo,\r\n"     
    		+ "   CustomerMaterial,\r\n"
    		+ "   Price,\r\n"
    		+ "   SaleUnit,\r\n"
    		+ "   OrderAmount,\r\n"
    		+ "   SaleQuantity,\r\n"   
    		+ "   RemainQuantity,\r\n"
    		+ "   RemainAmount,\r\n"
    		+ "   TotalQuantity,\r\n"    
    		+ "   b.Grade,\r\n"   
    		+ "   b.BillSendWeightQuantity,\r\n"
    		+ "   b.BillSendQuantity,\r\n"  
    		+ "   b.BillSendMRQuantity,\r\n"  
    		+ "   b.BillSendYDQuantity,\r\n"    
    		+ "   CustomerDue,\r\n"
    		+ "   DueDate,\r\n"  
    		+ "   b.ProductionOrder,\r\n"   
    		+ "   b.LotNo,\r\n" 
    		+ "   b.LabNo,\r\n" 
    		+ "   LabStatus,\r\n"
    		+ "   e.CFMPlanLabDate,\r\n" 
  		+ "   b.CFMActualLabDate,\r\n"
  		+ "   b.CFMCusAnsLabDate,\r\n"
    		+ "   b.UserStatus,\r\n"
//    		+ "   j.CFMDate as TKCFM,\r\n"  
	  		+ "   CASE\r\n"
	  		+ "		WHEN TAPP.SORCFMDate IS NOT NULL THEN TAPP.SORCFMDate\r\n"
	  		+ "		ELSE j.CFMDate \r\n"
	  		+ "		END as TKCFM,\r\n"  
    		+ "   b.CFMPlanDate AS CFMPlanDate ,  \r\n"
    		+ "   b.SendCFMCusDate,\r\n"
    		+ "   CASE \r\n"
    		+ "		WHEN b.[ProductionOrder] is not null THEN b.DeliveryDate \r\n"
    		+ "		ELSE b.CFTYPE \r\n"
    		+ "		END AS DeliveryDate , \r\n"  
	  	    + "   CFMDateActual,\r\n"  
	   	    + "   b.CFMDetailAll,\r\n"
	  	    + "   b.CFMNumberAll,\r\n"
	  	    + "   b.CFMRemarkAll,\r\n" 
	  	    + "   b.RollNoRemarkAll , \r\n"
    		+ "   ShipDate,\r\n"
    		+ "   RemarkOne,\r\n"
    		+ "   RemarkTwo,\r\n"
    		+ "   RemarkThree ,\r\n"   
    		+ "   ReplacedRemark ,\r\n"
    		+ "   StockRemark,\r\n"    
    		+ "   GRSumKG,\r\n"
    		+ "   GRSumYD,\r\n"   
    		+ "   GRSumMR,\r\n"  
//    		+ "   CASE \r\n"
//    		+ "		WHEN ( b.DyePlan is NULL) THEN null \r\n"
//  		+ "    	ELSE CAST(DAY( b.DyePlan) AS VARCHAR(2)) + '/' +   CAST(MONTH( b.DyePlan)AS VARCHAR(2))  \r\n"
//  		+ "     END AS DyePlan ,\r\n "	
//  		+ "   CASE \r\n"
//  		+ "		WHEN ( b.DyeActual is NULL) THEN null \r\n"
//  		+ "     ELSE CAST(DAY( b.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( b.DyeActual)AS VARCHAR(2))  \r\n"
//  		+ "     END AS DyeActual ,\r\n "	
    		+ "   b.DyePlan , \r\n"
  		+ "   b.DyeActual, \r\n"
  		+ "   PCRemark,\r\n"
  		+ "   [DelayedDep],\r\n"
  		+ "   [CauseOfDelay],\r\n"
  		+ "   q.[SwitchRemark],\r\n"
  		+ "   SL.[StockLoad],\r\n"
  		+ "   b.[PrdCreateDate], \r\n"
  		+ "   LotShipping\r\n";  
    private String selectRPV2 = 
    		  "   a.SaleOrder,\r\n"
    		+ "   CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
    		+ "		WHEN 0 THEN '' \r\n"
    		+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) \r\n"
    		+ "		END AS [SaleLine] ,\r\n"
    		+ "   Division,\r\n"   
			+ "   CustomerName,--ADD\r\n"
			+ "   SaleStatus,--ADD\r\n"
			+ "   DistChannel,--ADD\r\n"
    		+ "   CustomerShortName,\r\n"
    		+ "   SaleCreateDate,\r\n"
    		+ "   PurchaseOrder,\r\n"
    		+ "   MaterialNo,\r\n"      
    		+ "   CustomerMaterial,\r\n"
    		+ "   Price,\r\n"
    		+ "   SaleUnit,\r\n"
    		+ "   OrderAmount,\r\n"
    		+ "   SaleQuantity,\r\n"   
    		+ "   RemainQuantity,\r\n"
    		+ "   RemainAmount,\r\n"
    		+ "   TotalQuantity,\r\n"    
    		+ "   m.Grade,\r\n"    
	  		+ "			  FSMBB.BillSendWeightQuantity, \r\n"
	  		+ "			 case\r\n"
	  		+ "					WHEN a.SaleUnit  = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
	  		+ "					WHEN a.SaleUnit  = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
	  		+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
	  		+ "				end AS BillSendQuantity, \r\n"
	  		+ "			 FSMBB.BillSendMRQuantity,\r\n"
	  		+ "			 FSMBB.BillSendYDQuantity, \r\n"
    		+ "   CustomerDue,\r\n"
    		+ "   DueDate,\r\n"  
    		+ "   b.ProductionOrder,\r\n"   
    		+ "   b.LotNo,\r\n" 
    		+ "   b.LabNo,\r\n" 
    		+ "   LabStatus,\r\n"
    		+ "   e.CFMPlanLabDate,\r\n" 
	  		+ "   g.CFMActualLabDate,\r\n"
	  		+ "   g.CFMCusAnsLabDate,\r\n"
    		+ "   UCALRP.UserStatusCalRP as UserStatus,\r\n"
//    		+ "   j.CFMDate as TKCFM,\r\n"  
	+ "   CASE\r\n"
	+ "		WHEN TAPP.SORCFMDate IS NOT NULL THEN TAPP.SORCFMDate\r\n"
	+ "		ELSE j.CFMDate \r\n"
	+ "		END as TKCFM,\r\n"  
    		+ "   g.CFMPlanDate AS CFMPlanDate ,  \r\n"
//    		+ "   g.SendCFMCusDate , \r\n"
    		+ "   CASE \r\n"
    		+ "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
    		+ "     ELSE  g.SendCFMCusDate \r\n"     
    		+ "    	END AS SendCFMCusDate,\r\n"
    		+ "   CASE \r\n"
    		+ "		WHEN h.[ProductionOrder] is not null THEN H.DeliveryDate \r\n"
    		+ "		ELSE b.CFTYPE \r\n"
    		+ "		END AS DeliveryDate , \r\n" 
//    		+ "   g.CFMSendDate,\r\n" 
//    		+ "   g.CFMAnswerDate,\r\n" 
//    		+ "   g.CFMStatus,\r\n" 
//    		+ "   g.CFMNumber,\r\n"    
//    		+ "   g.CFMRemark,\r\n" 
	  		+ "   g.CFMDateActual,\r\n"  
	   	    + "   g.CFMDetailAll, \r\n"
	  	    + "   g.CFMNumberAll,  \r\n"
	  	    + "   g.CFMRemarkAll, \r\n"
	  	    + "   g.RollNoRemarkAll , \r\n"
    		+ "   ShipDate,\r\n"
    		+ "   RemarkOne,\r\n"
    		+ "   RemarkTwo,\r\n"
    		+ "   RemarkThree ,\r\n"   
    		+ "   ReplacedRemark ,\r\n"
    		+ "   StockRemark,\r\n"    
    		+ "   GRSumKG,\r\n"
    		+ "   GRSumYD,\r\n"   
    		+ "   GRSumMR,\r\n"   
//    		+ "   CASE \r\n"
//    		+ "		WHEN ( g.DyePlan is NULL) THEN null \r\n"
//	  		+ "     ELSE CAST(DAY( g.DyePlan) AS VARCHAR(2)) + '/' + CAST(MONTH( g.DyePlan)AS VARCHAR(2))  \r\n"
//	  		+ "     END AS DyePlan, \r\n "	
//	  		+ "   CASE \r\n"
//	  		+ "		WHEN ( g.DyeActual is NULL) THEN null \r\n"
//	  		+ "     ELSE CAST(DAY( g.DyeActual) AS VARCHAR(2)) + '/' + CAST(MONTH( g.DyeActual)AS VARCHAR(2))  \r\n"
//	  		+ "     END AS DyeActual, \r\n "
  		+ "   g.DyePlan , \r\n"
  		+ "   g.DyeActual, \r\n"
	  		+ "   PCRemark,\r\n"
	  		+ "   [DelayedDep],\r\n"
	  		+ "   [CauseOfDelay],\r\n"
	  		+ "   q.[SwitchRemark],\r\n"
	  		+ "   SL.[StockLoad],\r\n"
	  		+ "   b.[PrdCreateDate],\r\n"
	  		+ "   g.LotShipping \r\n"
	  		+ "   , g.PlanGreigeDate "; 
    private String selectAll = 
		  "   a.SaleOrder,\r\n"
		+ "   a.[SaleLine] ,\r\n"
		+ "   Division,\r\n"  
		+ "   CustomerShortName,	\r\n\r\n" 
		+ "   SaleCreateDate,\r\n"
		+ "   PurchaseOrder,\r\n"
		+ "   MaterialNo,\r\n"      
		+ "   CustomerMaterial,\r\n"
		+ "   Price,\r\n"
		+ "   SaleUnit,\r\n"
		+ "   OrderAmount,\r\n"
		+ "   SaleQuantity,\r\n"   
		+ "   RemainQuantity,\r\n"
		+ "   RemainAmount,\r\n"
		+ "   TotalQuantity,\r\n"    
		+ "   a.Grade,\r\n"    
  		+ "	  a.BillSendWeightQuantity, \r\n"
  		+ "	  a.BillSendQuantity, \r\n"
  		+ "	  a.BillSendMRQuantity,\r\n"
  		+ "	  a.BillSendYDQuantity, \r\n"
		+ "   CustomerDue,\r\n"
		+ "   DueDate,\r\n"  
		+ "   a.ProductionOrder,\r\n"   
		+ "   a.LotNo,\r\n" 
		+ "   a.LabNo,\r\n" 
		+ "   LabStatus,\r\n"
		+ "   a.CFMPlanLabDate,\r\n" 
  		+ "   a.CFMActualLabDate,\r\n"
  		+ "   a.CFMCusAnsLabDate,\r\n"
		+ "   UserStatus,\r\n"
		+ "   TKCFM,\r\n"  
		+ "   CFMPlanDate ,  \r\n" 
		+ "   SendCFMCusDate,\r\n"
		+ "   DeliveryDate , \r\n"  
  		+ "   a.CFMDateActual,\r\n"  
   	    + "   a.CFMDetailAll, \r\n"
  	    + "   a.CFMNumberAll,  \r\n"
  	    + "   a.CFMRemarkAll, \r\n"
  	    + "   a.RollNoRemarkAll , \r\n"
		+ "   ShipDate,\r\n"
		+ "   RemarkOne,\r\n"
		+ "   RemarkTwo,\r\n"
		+ "   RemarkThree ,\r\n"   
		+ "   ReplacedRemark ,\r\n"
		+ "   StockRemark,\r\n"    
		+ "   GRSumKG,\r\n"
		+ "   GRSumYD,\r\n"   
		+ "   GRSumMR,\r\n"   
		+ "   DyePlan, \r\n "	
  		+ "   DyeActual, \r\n " 
  		+ "   PCRemark,\r\n"
  		+ "   [DelayedDep],\r\n"
  		+ "   [CauseOfDelay],\r\n"
  		+ "   a.[SwitchRemark],\r\n"
  		+ "   a.[StockLoad],\r\n"
  		+ "   a.[PrdCreateDate]\r\n"
  		+ "   ,LotShipping\r\n"
		+ "   , Volumn  \r\n"  
		+ "   , VolumnFGAmount \r\n"
		+ "   , TypePrd \r\n"
		+ "   , TypePrdRemark \r\n"
		+ "   , CustomerType\r\n"
		+ "   , [DyeStatus]\r\n"; 
    private String innerJoinWaitLotB =    
   		   " INNER JOIN (\r\n"
   		 + "	SELECT DISTINCT a.saleorder , a.saleline, c.SumVolMain ,b.SumVolUsed\r\n"
   		 + "		,CASE  \r\n"
   		 + " 			WHEN ISNULL(c.SumVolMain, 0 ) >  b.SumVolUsed THEN 'A'\r\n"
   		 + "			WHEN ISNULL(c.SumVolMain, 0 ) <=  b.SumVolUsed THEN 'B' \r\n"
   		 + "			ELSE  'C'\r\n"
   		 + "	 		END AS SumVol \r\n"
   		 + "		,'รอจัด Lot' as ProductionOrder\r\n"
   		 + "		,CASE  \r\n"
   		 + " 			WHEN ISNULL( SumVolOP, 0 ) >  0 THEN 'พ่วงแล้วรอสวม'\r\n"
   		 + "			WHEN ISNULL( SumVolRP, 0 ) >  0 THEN 'รอสวมเคยมี Lot'\r\n"
   		 + "			ELSE  'รอจัด Lot'\r\n"
   		 + "	 		END AS LotNo  \r\n"
   		 + "		,SumVolOP\r\n"
   		 + "		,SumVolRP\r\n"
   		 + "		,CountProdRP\r\n" 
   		 + "        ,cast(null as decimal) as TotalQuantity \r\n"
   		 + "		,cast(null as varchar) as Grade \r\n"
   		 + "		,cast(null as decimal) as BillSendWeightQuantity \r\n"
   		 + "		,cast(null as decimal) as BillSendQuantity  \r\n"
   		 + "		,cast(null as decimal) as BillSendMRQuantity \r\n"
   		 + "		,cast(null as decimal) as BillSendYDQuantity  \r\n"
   		 + "		,cast(null as varchar) as LabNo\r\n"
   		 + "		,cast(null as varchar) as LabStatus\r\n"
   		 + "		,cast(null as date) as CFMPlanLabDate\r\n"
   		 + "		,cast(null as date) as CFMActualLabDate \r\n"
   		 + "		,cast(null as date) as CFMCusAnsLabDate \r\n"
   		 + "		,cast(null as varchar) as UserStatus \r\n"
   		 + "		,cast(null as date) as TKCFM \r\n"
   		 + "		,cast(null as date) as CFMPlanDate \r\n"
   		 + "		,cast(null as date) as SendCFMCusDate \r\n"
   		 + "		,cast(null as date) as DeliveryDate  \r\n"
   		 + "		,cast(null as date) as CFMSendDate \r\n"
   		 + "		,cast(null as date) as CFMAnswerDate \r\n"
   		 + "		,cast(null as varchar) as CFMStatus \r\n"
   		 + "		,cast(null as varchar) as CFMNumber  \r\n"
   		 + "		,cast(null as varchar) as CFMRemark  \r\n"
   		 + "		,cast(null as varchar) as RemarkOne \r\n"
   		 + "		,cast(null as varchar) as RemarkTwo \r\n"
   		 + "		,cast(null as varchar) as RemarkThree  \r\n"
   		 + "		,cast(null as varchar) as StockRemark \r\n"
   		 + "		,cast(null as decimal) as  GRSumKG \r\n"
   		 + "		,cast(null as decimal) as  GRSumYD \r\n"
   		 + "		,cast(null as decimal) as  GRSumMR \r\n"
   		 + "		,cast(null as date) as  DyePlan \r\n"
   		 + "		,cast(null as date) as DyeActual   \r\n"
   		 + "		,cast(null as varchar) as [SwitchRemark] \r\n"
   		 + "		,cast(null as date) as [PrdCreateDate]\r\n"
   		 + "		,cast(null as decimal) AS Volumn   \r\n"
   		 + "		,cast(null as decimal) AS VolumnFGAmount  	\r\n"
   		 + "		,cast(null as varchar) as CFMDetailAll \r\n"
   		 + "		,cast(null as varchar) as CFMNumberAll  \r\n"
   		 + "		,cast(null as varchar) as CFMRemarkAll \r\n"
	     + "        ,cast(null as varchar) as RollNoRemarkAll\r\n"
	     + "        ,cast(null as date) as CFMDateActual\r\n" 
	     + "        , cast(null as date) AS LotShipping \r\n"   
		 + "		, cast(null as date) as PlanGreigeDate \r\n" 
   		 + "	from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
   		 + "	left join ( SELECT DISTINCT a.SaleOrder , A.SaleLine ,ISNULL(SumVolOP, 0 ) + ISNULL(SumVolRP, 0 ) as SumVolUsed --,ISNULL(SumVolRP, 0 )\r\n"
   		 + "							   ,SumVolOP,SumVolRP\r\n"
   		 + "				FROM[PCMS].[dbo].[FromSapMainProd]  AS A\r\n"
   		 + "				LEFT JOIN (  \r\n"
   		 + "					SELECT DISTINCT a.[ProductionOrder] \r\n"
   		 + "								   ,sum (A.[Volumn]) as SumVolOP \r\n"
   		 + "					FROM [PCMS].[dbo].[FromSapMainProdSale] AS A \r\n"
   		 + "					left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
   		 + "					WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
   		 + "					GROUP BY A.[ProductionOrder] ) AS D ON D.ProductionOrder = A.ProductionOrder\r\n"
   		 + "				LEFT JOIN (   \r\n"
   		 + "					SELECT a.ProductionOrderRP , sum(a.Volume) as SumVolRP  \r\n"
   		 + "					from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
   		 + "					left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n" 
   		 + "					WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ')  \r\n"
   		 + "					group by a.ProductionOrderRP\r\n"
   		 + "					) AS E ON A.ProductionOrder = E.ProductionOrderRP	 \r\n"
//   		 + "				LEFT JOIN (   \r\n"
//   		 + "					SELECT a.ProductionOrderRP , sum(c.Volume) as SumVolRP  \r\n"
//   		 + "					from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
//   		 + "					left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
//   		 + "					left join ( SELECT ProductionOrderRP  \r\n"
//   		 + "								     , CASE  \r\n"
//   		 + "									   		WHEN ( Volume = 0 ) THEN b.Volumn  \r\n"
//   		 + "											ELSE a.Volume\r\n"
//   		 + "											END AS Volume  \r\n"
//   		 + "								from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
//   		 + "								left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
//   		 + "								WHERE a.[DataStatus] = 'O' \r\n"
//   		 + "							 ) as c on a.ProductionOrderRP = c.ProductionOrderRP\r\n"
//   		 + "					WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ')  \r\n"
//   		 + "					group by a.ProductionOrderRP\r\n"
//   		 + "					) AS E ON A.ProductionOrder = E.ProductionOrderRP	 \r\n"
   		 + "	)  as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n"
   		 + "	LEFT JOIN ( \r\n"
   		 + "				SELECT [SaleOrder] ,[SaleLine]  ,sum( [Volumn] ) as SumVolMain \r\n"
   		 + "				FROM [PCMS].[dbo].[FromSapMainProd]\r\n"
   		 + "				WHERE DataStatus = 'O'\r\n"
   		 + "				group by  [SaleOrder]  ,[SaleLine] ) AS C ON A.SaleOrder = C.SaleOrder AND A.SaleLine = C.SaleLine \r\n"
   		 + "	left join ( select DISTINCT a.SaleOrder ,a.SaleLine ,1 AS CountProdRP\r\n"
   		 + "				from [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
   		 + "				inner join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrderRP = b.ProductionOrder\r\n"
   		 + "				where a.DataStatus = 'O' and  a.ProductionOrder = 'รอจัด Lot' \r\n"
   		 + "				 and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
   		 + "				GROUP BY  a.SaleOrder ,a.SaleLine \r\n"
   		 + "			) AS D ON A.SaleOrder = D.SaleOrder AND A.SaleLine = D.SaleLine  \r\n"
   		 + "	where ( c.SumVolMain > 0   ) OR D.SaleOrder IS NOT NULL \r\n"
   		 + " ) AS b ON a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n" ; 
    private String leftJoinUCAL =  ""
    		+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCAL on UCAL.[DataStatus] = 'O' and b.ProductionOrder = UCAL.ProductionOrder AND (  m.Grade = UCAL.Grade OR  m.Grade IS NULL )  \r\n"; 
    private String leftJoinUCALRP = "    "
    		+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCALRP on UCALRP.[DataStatus] = 'O' and b.ProductionOrder = UCALRP.ProductionOrder AND ( m.Grade = UCALRP.Grade OR m.Grade IS NULL )    \r\n";
    private String leftJoinSCC = ""
    		+ " left join [PCMS].[dbo].[PlanSendCFMCusDate] as SCC on b.ProductionOrder = SCC.ProductionOrder and SCC.DataStatus = 'O'    \r\n";
	private String leftJoinM =    
			" left join #tempSumGR as m on b.ProductionOrder = m.ProductionOrder \r\n"; 
	   private String leftJoinFSMBBTempSumBill = "" 
	    		+ " left join #tempSumBill  \r\n"
	    		+ "						AS FSMBB ON b.[ProductionOrder] = FSMBB.[ProductionOrder] \r\n"
	    		+ "							    AND FSMBB.Grade = M.Grade \r\n" 
	    		+ "							    AND FSMBB.SaleOrder = a.SaleOrder \r\n"
	    		+ "							    AND FSMBB.SaleLine = a.SaleLine\r\n";

	   private String leftJoinTempG = 
				  " left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"  ;   
	   private String leftJoinBPartOneH =  ""
				  + "           left join #tempPlandeliveryDate as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n";
	private String leftJoinBSelect = ""
			+ "				  a.[SaleOrder] , a.[Saleline]  ,[TotalQuantity] ,[Unit]\r\n"
			  + "			 	, [RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
			  + "			 	, a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
			  + "			 	, [Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
			  + "				, [OrderPuang] ,[RefPrd],b.[GreigeInDate] ,[BCDate],b.[Volumn]\r\n"
			  + "			 	, [CFdate],[CFType],[Shade],g.[LotShipping] ,m.[Grade] \r\n"
			  + "				, [PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG]\r\n "
			  + "			 	, b.[ProductionOrder]   ,b.[LotNo] \r\n"   
			  + " 			 	, CASE  \r\n"
			  + " 					WHEN ( s.SumVolRP is not null AND t.SumVolOP is not null ) THEN (  b.Volumn -  s.SumVolRP -  t.SumVolOP)\r\n"
			  + "					WHEN ( s.SumVolRP is not null AND t.SumVolOP is null ) THEN (  b.Volumn -  s.SumVolRP )\r\n"
			  + "					WHEN ( s.SumVolRP is null AND t.SumVolOP is not null ) THEN (  b.Volumn -  t.SumVolOP) \r\n"
			  + "					WHEN  b.Volumn is not null THEN  b.Volumn\r\n"
			  + "				    ELSE   0\r\n"
			  + "	 				END AS SumVol   \r\n"
			  + " 			 	, CASE  \r\n"
			  + "				 	WHEN ( s.SumVolRP is not null AND t.SumVolOP is not null ) THEN a.Price * (  b.Volumn -  s.SumVolRP -  t.SumVolOP)\r\n"
			  + "				 	WHEN ( s.SumVolRP is not null AND t.SumVolOP is null ) THEN a.Price * (  b.Volumn -  s.SumVolRP )\r\n"
			  + "				 	WHEN ( s.SumVolRP is null AND t.SumVolOP is not null ) THEN a.Price * (  b.Volumn -  t.SumVolOP)   \r\n"
			  + "				 	WHEN  b.Volumn is not null THEN a.Price * b.Volumn\r\n"
			  + "					ELSE   0\r\n"
			  + "				 	END AS SumVolFGAmount   \r\n"
			  + "              	, s.SumVolRP,t.SumVolOP,b.Volumn as RealVolumn\r\n"
			  + "              	, g.[DyePlan]  \r\n"
			  + "	          	, g.[DyeActual] , g.[Dryer] , g.[Finishing] , g.[Inspectation]  \r\n"
			  + "             	, g.[Prepare] , g.[Preset] , g.[Relax] , g.[CFMDateActual] , g.[CFMPlanDate]  \r\n"
			  + "            	, g.[DyeStatus] , h.DeliveryDate, 	UCAL.UserStatusCal as UserStatus \r\n" 
			  + " 		  		, CASE \r\n"
			  + "					WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
			  + "    			  	ELSE  g.SendCFMCusDate \r\n"     
			  + "    				END AS SendCFMCusDate\r\n"
			  + "				, GRSumKG, GRSumYD, GRSumMR \r\n "  
			  + "           	, g.CFMDetailAll \r\n"
			  + "           	, g.CFMNumberAll  \r\n"
			  + "           	, g.CFMRemarkAll \r\n" 
	 		  + "           	, g.RollNoRemarkAll \r\n"
			  + "           	, g.CFMActualLabDate \r\n" 
	 		  + "           	, g.CFMCusAnsLabDate \r\n"
	 		  + "				, FSMBB.BillSendWeightQuantity \r\n"
	 		  + "				, case\r\n"
	 		  + "					WHEN a.SaleUnit  = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
	 		  + "					WHEN a.SaleUnit  = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
	 		  + "					ELSE FSMBB.BillSendMRQuantity\r\n"
	 		  + "				end AS BillSendQuantity \r\n"
	 		  + "				, FSMBB.BillSendMRQuantity\r\n"
	 		  + "				, FSMBB.BillSendYDQuantity , g.PlanGreigeDate \r\n" ;  
	private String leftJoinBPartOneT =  ""
			  + "			left join ( SELECT a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
			  + "                       from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
			  + "					 	left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "			            WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ')\r\n"
			  + "                         and a.Volumn is not null\r\n"
			  + "			            group by a.ProductionOrder) as t on b.ProductionOrder = t.ProductionOrder   \r\n";
	  private String leftJoinBPartOneS =  ""
			  + "           left join ( SELECT a.ProductionOrderRP , sum(a.Volume) as SumVolRP  \r\n"
			  + "    					from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
			  + "						left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n" 
			  + "						WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
			  + "                         and a.Volume is not null\r\n"
			  + "						group by a.ProductionOrderRP) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n" ;
//	  private String leftJoinBPartOneS =  ""
//			  + "           left join ( SELECT a.ProductionOrderRP , sum(c.Volume) as SumVolRP  \r\n"
//			  + "    					from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
//			  + "						left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
//			  + "						left join ( SELECT ProductionOrderRP  \r\n"
//			  + "										 , CASE  \r\n"
//			  + "										   	WHEN ( Volume = 0 ) THEN b.Volumn  \r\n"
//			  + "										   	ELSE a.Volume\r\n"
//			  + "										   	END AS Volume  \r\n"
//			  + "									from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
//			  + "									left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
//			  + "									WHERE a.[DataStatus] = 'O' ) as c on a.ProductionOrderRP = c.ProductionOrderRP\r\n"
//			  + "						WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
//			  + "                         and c.Volume is not null\r\n"
//			  + "						group by a.ProductionOrderRP) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n" ;
	   
    public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");  
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm"); 
	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	private String createTempSumGR = 
			  " If(OBJECT_ID('tempdb..#tempSumGR') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempSumGR\r\n"
			+ "	end ;\r\n"
			+ " SELECT distinct [ProductionOrder] ,[Grade] ,[PriceSTD]\r\n"
			+ "				    ,sum([QuantityMR]) as GRSumMR\r\n"      
			+ "				    ,sum([QuantityKG]) as GRSumKG\r\n"
			+ "				    ,sum([QuantityYD]) as GRSumYD\r\n "
			+ "           into #tempSumGR \r\n" 
			+ "			  FROM [PCMS].[dbo].[FromSapGoodReceive]\r\n"
			+ "			  where datastatus = 'O'\r\n"   
			+ "			  GROUP BY ProductionOrder,Grade ,[PriceSTD] \r\n"  ;  
	private String leftJoinE = 
  		  	" left join ( SELECT distinct   a.[ProductionOrder] \r\n"
  		  + "  			  				  , a.[SaleOrder] \r\n"
  		  + "                             , a.[SaleLine] \r\n"
  		  + "                             , [PlanDate] AS CFMPlanLabDate \r\n"
  		  + "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
  		  + "			  inner join (select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
  		  + "				          FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
  		  + "				          group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
  		  + "				          on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
  		  + "				         and a.[CreateDate] = b.[MaxCreateDate] \r\n"
  		  + "			) as e on e.ProductionOrder = b.ProductionOrder and e.SaleOrder = a.SaleOrder and e.SaleLine = a.SaleLine\r\n"  ;    
	private String leftJoinH = 
 		  	  " left join #tempPlandeliveryDate as h on h.ProductionOrder = a.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine \r\n"  ;    
	private String leftJoinBH = 
		  	  " left join #tempPlandeliveryDate as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine \r\n"  ;    
	private String leftJoinJ = 
			  " left join ( SELECT distinct SALELINE,SALEORDER,CFMDATE \r\n"
			+ "			    FROM [PCMS].[dbo].[FromSORCFM] )AS J  on a.SaleLine = J.SaleLine and a.SaleOrder = J.SaleOrder \r\n "; 
	private String leftJoinJ_B = 
			  " left join ( SELECT distinct SALELINE,SALEORDER,CFMDATE \r\n"
			+ "			    FROM [PCMS].[dbo].[FromSORCFM] )AS J  on b.SaleLine = J.SaleLine and b.SaleOrder = J.SaleOrder \r\n "; 
	private String leftJoinTAPP = 
			  " left join #tempApproved AS TAPP on TAPP.ProductionOrder = b.ProductionOrder\r\n ";
	private String leftJoinK = 
			"  left join ( SELECT SALELINE,SALEORDER,ProductionOrder,ReplacedRemark \r\n"
			+ "			   FROM [PCMS].[dbo].[InputReplacedRemark] \r\n"
			+ "			   WHERE DataStatus = 'O')   AS K on K.ProductionOrder = b.ProductionOrder and K.SaleOrder = a.SaleOrder and K.SaleLine = a.SaleLine \r\n ";
//	private String leftJoinSL = 
//			  " left join ( SELECT SALELINE,SALEORDER,ProductionOrder,StockLoad \r\n"
//			+ "			  FROM [PCMS].[dbo].InputStockLoad \r\n"
//			+ "			  where Datastatus = 'O')AS SL on SL.ProductionOrder = b.ProductionOrder and SL.SaleOrder = a.SaleOrder and SL.SaleLine = a.SaleLine  \r\n"
//			+ "\r\n ";
	private String leftJoinSL = 
			  " left join ( SELECT SALELINE,SALEORDER,ProductionOrder,StockLoad \r\n"
			+ "			  FROM [PCMS].[dbo].InputStockLoad \r\n"
			+ "			  where Datastatus = 'O')AS SL on SL.ProductionOrder = b.ProductionOrder and SL.SaleOrder = a.SaleOrder and SL.SaleLine = a.SaleLine  \r\n"
			+ "\r\n ";
	private String leftJoinl = 
			  " left join (SELECT SALELINE,SALEORDER,ProductionOrder,Grade ,StockRemark\r\n"
			+ "			   FROM [PCMS].[dbo].[InputStockRemark] \r\n"
			+ "			   WHERE DataStatus = 'O')   AS l on l.ProductionOrder = b.ProductionOrder and \r\n"
			+ "                                              l.SaleOrder = a.SaleOrder and\r\n"
			+ "                                              l.SaleLine = a.SaleLine and\r\n"
			+ "                                              l.Grade = m.Grade\r\n ";
	private String leftJoinMainl = 
			  " left join (SELECT SALELINE,SALEORDER,ProductionOrder,Grade ,StockRemark\r\n"
			+ "			   FROM [PCMS].[dbo].[InputStockRemark] \r\n"
			+ "			   WHERE DataStatus = 'O')   AS l on l.ProductionOrder = b.ProductionOrder and \r\n"
			+ "                                              l.SaleOrder = a.SaleOrder and\r\n"
			+ "                                              l.SaleLine = a.SaleLine and\r\n"
			+ "                                              l.Grade = b.Grade\r\n ";  
	private String leftJoinP = 
			  " left join (SELECT SALELINE,SALEORDER,ProductionOrder,PCRemark\r\n"
			+ "			   FROM [PCMS].[dbo].InputPCRemark \r\n"
			+ "			   WHERE DataStatus = 'O')   AS P on P.ProductionOrder = b.ProductionOrder and P.SaleOrder = a.SaleOrder and P.SaleLine = a.SaleLine\r\n" ;
 	private String leftJoinQ = 
			  " left join (SELECT  ProductionOrder,SwitchRemark\r\n"
			+ "			   FROM [PCMS].[dbo].InputSwitchRemark \r\n"
			+ "			   WHERE DataStatus = 'O') AS q on b.ProductionOrder = q.ProductionOrder \r\n";
 	private String leftJoinInputCOD = 
			  " left join (SELECT ProductionOrder,[CauseOfDelay]\r\n"
			+ "			   FROM [PCMS].[dbo].[InputCauseOfDelay] \r\n"
			+ "			   WHERE DataStatus = 'O') AS InputCOD on b.ProductionOrder = InputCOD.ProductionOrder \r\n";
	private String leftJoinInputDD =   
			  " left join (SELECT  ProductionOrder,[DelayedDep]\r\n"
			+ "			   FROM [PCMS].[dbo].[InputDelayedDep] \r\n"
			+ "			   WHERE DataStatus = 'O') AS InputDD on b.ProductionOrder = InputDD.ProductionOrder \r\n";
  	public PCMSDetailDaoImpl(Database database) {
		this.database = database;  
		this.message = "";      
	}
   
	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null;    
		ArrayList<String> listUserStatus = new ArrayList<String>();    
		ArrayList<String> listString = new ArrayList<String>();       
		String where = "where";      
		String whereProd = " ";   

		String whereCaseTry = "";  
		String whereBMainUserStatus = " where"; 
//		String whereCaseA = "";
		String whereWaitLot = " where ";
String saleNumber = "" , materialNo = "",saleOrder = "", saleCreateDate = "",labNo = "" ,articleFG = "",designFG = "",prdOrder= "",
		prdCreateDate = "",deliveryStatus = "",saleStatus ="",dist="",dueDate = "",po="",
				cusDiv=""; 
		PCMSTableDetail bean = poList.get(0);
		bean.getCustomerName();
		bean.getCustomerShortName();
		saleNumber = bean.getSaleNumber() ; 
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();  
		saleCreateDate = bean.getSaleOrderCreateDate();	  
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		designFG = bean.getDesignFG();
		bean.getUserStatus();
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();
		dueDate = bean.getDueDate();
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		cusDiv = bean.getCustomerDivision(); 
		dist = bean.getDistChannel(); 
		List<String> userStatusList = bean.getUserStatusList();
		List<String> cusNameList = bean.getCustomerNameList();
		List<String> cusShortNameList = bean.getCustomerShortNameList();
		List<String> divisionList = bean.getDivisionList();

		String whereSale = " where ( a.DataStatus = 'O' or a.DataStatus is null ) \r\n";  
		where +=    " MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n"; 
		whereWaitLot +=    " MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n";   
		
//		System.out.println(cusDiv+" | "+divisionList.toString());
		if (!cusDiv.equals("")) {
			
			String[] array = cusDiv.split(",");
			String tmpWhere = "";
//			tmpWhere += " and  ( "; 
//			for (int i = 0; i < cusDivList.length; i++) { 
//				tmpWhere += " a.Division = '" +cusDivList[i] + "' ";
//				if (i != cusDivList.length - 1) {
//					tmpWhere += " or ";
//				} ;
//			}
//			tmpWhere += " ) \r\n"; 
			


			listString.clear();
			for (int i = 0; i < array.length; i++) {
				listString.add("'"+array[i].replaceAll("'","''")+"' "); 
			} 
			tmpWhere += " and ( a.Division IN ( \r\n" ;  
			tmpWhere += String.join(",",  listString  );
			tmpWhere += " ) ) \r\n" ;  
			
			whereSale += tmpWhere; 
		} 
		if (!saleOrder.equals("")) {    
			whereSale +=  " and a.SaleOrder like '" + saleOrder + "%' \r\n"; ;
		} 
		if(!po.equals("")) { 
			where += " and [PurchaseOrder] like '"+po+"%' \r\n" ; 
			whereSale += " and [PurchaseOrder] like '"+po+"%' \r\n" ; 
//			whereWaitLot += " and ([PurchaseOrder],  like '"+po+"%' \r\n" ; 
		}
		if (!saleCreateDate.equals("")) {   
			String[] dateArray = saleCreateDate.split("-");
			where += "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
//			whereWaitLot +=  "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
//					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
			whereSale +=  "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
		} 
		if(!po.equals("")) { 
			where += " and ([PurchaseOrder],  like '"+po+"%' \r\n" ; 
			whereSale += " and ([PurchaseOrder],  like '"+po+"%' \r\n" ; 
//			whereWaitLot += " and ([PurchaseOrder],  like '"+po+"%' \r\n" ; 
		}
		if(!saleNumber.equals("")) { 
			where += " and SaleNumber like '"+saleNumber+"%' \r\n" ; 
			whereSale += " and SaleNumber like '"+saleNumber+"%' \r\n" ;  
		}
		if (!articleFG.equals("")) {
			where += " and a.ArticleFG like '" + articleFG + "%'  \r\n";   
			whereSale += " and a.ArticleFG like '" + articleFG + "%'  \r\n";      
		}
		if (!designFG.equals("")) {
			where += " and a.DesignFG like '" + designFG + "%'  \r\n";
			whereSale += " and a.DesignFG like '" + designFG + "%'  \r\n"; 
		} 
		if (cusNameList.size() > 0) { 
			String tmpWhere = "";
//			tmpWhere += " and  ( ";
//			String text = "";
//			for (int i = 0; i < cusNameList.size(); i++) {
//				text = cusNameList.get(i);
//				tmpWhere += " CustomerName = '" +text + "' ";
//				if (i != cusNameList.size() - 1) {
//					tmpWhere += " or ";
//				} ;
//			}
//			tmpWhere += " ) \r\n";
			

			listString.clear();
			for (int i = 0; i < cusNameList.size(); i++) {
				listString.add("'"+cusNameList.get(i).replaceAll("'","''")+"' "); 
			} 
			tmpWhere += " and ( CustomerName IN ( \r\n" ;  
			tmpWhere += String.join(",",  listString  );
			tmpWhere += " ) ) \r\n" ;    
			
			
			where += tmpWhere; 
			whereSale += tmpWhere;
		}
		if (cusShortNameList.size() > 0) { 
			String tmpWhere = "";
//			tmpWhere += " and  ( "; 
//			String text = "";
//			for (int i = 0; i < cusShortNameList.size(); i++) {
//				text = cusShortNameList.get(i);
//				tmpWhere += " CustomerShortName = '" +text + "' ";
//				if (i != cusShortNameList.size() - 1) {
//					tmpWhere += " or ";
//				} ;
//			}
//			tmpWhere += " ) \r\n";
			listString.clear();
			for (int i = 0; i < cusShortNameList.size(); i++) {
				listString.add("'"+cusShortNameList.get(i).replaceAll("'","''")+"' "); 
			} 
			tmpWhere += " and ( CustomerShortName IN ( \r\n" ;  
			tmpWhere += String.join(",",  listString  );
			tmpWhere += " ) ) \r\n" ;    
			
			where += tmpWhere;
			whereSale += tmpWhere;
//			whereWaitLot += tmpWhere;
		}
		if (divisionList.size() > 0) {  
			String tmpWhere = "";
//			tmpWhere += " and  ( ";  
//			String text = "";
//			for (int i = 0; i < divisionList.size(); i++) {
//				text = divisionList.get(i);
//				tmpWhere += " Division = '" +text + "' ";
//					if (i != divisionList.size() - 1) {
//						tmpWhere += " or ";
//					} ;
//				}
//			tmpWhere += " ) \r\n";
			

			listString.clear();
			for (int i = 0; i < divisionList.size(); i++) {
				listString.add("'"+divisionList.get(i).replaceAll("'","''")+"' "); 
			} 
			tmpWhere += " and ( Division IN ( \r\n" ;  
			tmpWhere += String.join(",",  listString  );
			tmpWhere += " ) ) \r\n" ;   
			
			where += tmpWhere; 
			whereSale += tmpWhere;
			}
		 
		if(!dueDate.equals("")) {  
			String[] dateArray = dueDate.split("-");
			where += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
//			whereWaitLot += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
//					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
			whereSale += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
		}
		if (!deliveryStatus.equals("")) {
			where += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n";
//			whereWaitLot += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n";
			whereSale += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n";
		}
		if (!saleStatus.equals("")) {
//			where += " and SaleStatus like '" + saleStatus + "%'  \r\n";
//			whereSale += " and SaleStatus like '" + saleStatus + "%'  \r\n";
			if(saleStatus.equals("O")) {  
				where += " and ( SaleStatus like '" + saleStatus + "%' and a.[RemainQuantity] > 0 )  \r\n";
				whereSale += " and ( SaleStatus like '" + saleStatus + "%' and a.[RemainQuantity] > 0 ) \r\n";
			}
			else {
				where += " and ( SaleStatus like '" + saleStatus + "%' or a.[RemainQuantity] = 0 )  \r\n";
				whereSale += " and ( SaleStatus like '" + saleStatus + "%' or a.[RemainQuantity] = 0 ) \r\n";
				
			}
////			whereTempUCAL += " and SaleStatus like '" + saleStatus + "%'  \r\n";
		} 
		if (!dist.equals("")) {
			String tmpWhere = "";
//			tmpWhere += " and  ( ";  
			String[] array = dist.split("\\|");      
//			for (int i = 0; i < array.length; i++) { 
//				tmpWhere += " DistChannel = '" + array[i] + "' ";
//				if (i != array.length - 1) {
//					tmpWhere += " or ";
//				} ;
//			}
//			tmpWhere += " ) \r\n"; 
			


			listString.clear();
			for (int i = 0; i < array.length; i++) {
				listString.add("'"+array[i].replaceAll("'","''")+"' "); 
			} 
			tmpWhere += " and ( DistChannel IN ( \r\n" ;  
			tmpWhere += String.join(",",  listString  );
			tmpWhere += " ) ) \r\n" ;   
			where += tmpWhere; 
			whereSale += tmpWhere;
		}    
		String tmpWhereNoLotUCAL = "";

		// prod order
		if (!labNo.equals("")) {
			whereProd += " and b.LabNo like '" + labNo + "%'  \r\n";  
			where += " and b.LabNo like '" + labNo + "%'  \r\n";  
		}
		if (!prdOrder.equals("")) {
			where += " and b.ProductionOrder like '" + prdOrder + "%'  \r\n "; 
			whereProd += " and b.ProductionOrder like '" + prdOrder + "%'  \r\n "; 
		}  
		if (!prdCreateDate.equals("")) {
			String[] dateArray = prdCreateDate.split("-");
			where += " and ( PrdCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " PrdCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) )  \r\n"; 
			whereProd += " and ( PrdCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " PrdCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) )  \r\n"; 
		}
		if (!materialNo.equals("")) {
			whereProd += " and MaterialNo like '" + materialNo  + "%' \r\n"; 
		}

		String whereCaseTryRP = ""+ whereProd;
		whereWaitLot = where ;
//		whereBMain = where;
		whereCaseTry = whereProd; 
		if (userStatusList.size() > 0) { 
			String tmpWhere = "";
			tmpWhereNoLotUCAL = " and ( b.ProductionOrder is not null and ( \r\n";  
			tmpWhere += " and ( ( \r\n";  
			whereCaseTryRP  += " and ( b.ProductionOrder is not null and ( \r\n";  
			whereCaseTry += " and ( a.ProductionOrder is not null and ( \r\n";  
			String text = "";
			int int_emerCheck = 0;
			for (int i = 0; i < userStatusList.size(); i++) {
				text = userStatusList.get(i); 
				if(text.equals("รอจัด Lot") || text.equals("ขาย stock") || text.equals("รับจ้างถัก") || text.equals("Lot ขายแล้ว")
				|| text.equals("พ่วงแล้วรอสวม")|| text.equals("รอสวมเคยมี Lot") ) { 
					tmpWhere += " b.LotNo = '" +text + "' "; 
					whereCaseTryRP += " b.LotNo = '" +text + "' "; 
					whereCaseTry += " a.LotNo = '" +text + "' "; 
					listUserStatus.add("'"+text.replaceAll("'","''")+"' "); 
//					listUserStatus.add(" b.LotNo = '" +text + "' "); 
				} else {  
					int_emerCheck = 1;
					whereCaseTryRP += "UCALRP.UserStatusCalRP = '" +text + "' ";
					tmpWhere += "UCAL.UserStatusCal = '" +text + "' ";
					tmpWhereNoLotUCAL += "UCAL.UserStatusCal = '" +text + "' ";
					whereCaseTry += "a.UserStatus = '" +text + "' ";
					if (i != userStatusList.size() - 1) { 
						tmpWhereNoLotUCAL += " or ";     
					} ;
				} 
				if (i != userStatusList.size() - 1) {
					tmpWhere += " or ";    
					whereCaseTryRP += " or ";   
					whereCaseTry += " or ";   
				} ;
			}   
			if(int_emerCheck == 0) {       	
				tmpWhereNoLotUCAL += "UCAL.UserStatusCal = ''  ";
			}
			int sizeUS = listUserStatus.size();
			if(sizeUS > 0) {  
				whereWaitLot += " and ( b.LotNo IN ( \r\n" ;  
				whereWaitLot += String.join(",",  listUserStatus  );
				whereWaitLot += " ) ) \r\n" ;      
			}  
			else {
				whereWaitLot += " and ( b.UserStatus is not null ) \r\n" ;   
			}
			tmpWhere += ") 		) \r\n";  
			whereCaseTry += ") 		) \r\n";   
			whereCaseTryRP += ") 		) \r\n";   
			tmpWhereNoLotUCAL += ") 		) \r\n";   
			where += tmpWhere;   
			whereBMainUserStatus += " A.SaleOrder <> '' "+  tmpWhere ;  
		}     
		whereBMainUserStatus += whereProd; 
		whereCaseTry = whereCaseTry.replace("UserStatusCal", "UserStatus");
		whereCaseTry = whereCaseTry.replace("UCALRP.", "a.");
		whereCaseTry = whereCaseTry.replace("UCAL.", "a.");
		whereCaseTry = whereCaseTry.replace("b.", "a.");  
		String createTempMainSale = ""
				+ " If(OBJECT_ID('tempdb..#tempMainSale') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempMainSale\r\n"
				+ "	end ; \r\n"
				+ " SELECT a.* , b.CustomerType,a.[Division] AS CustomerDivision \r\n" 
				+ " INTO #tempMainSale \r\n" 
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a\r\n"
				+ " left join [PCMS].[dbo].[ConfigCustomerEX] as b on a.[CustomerNo] = b.[CustomerNo] and b.[DataStatus] = 'O'\r\n  " 
				+ whereSale; 
		String sqlWaitLot = 
				  " SELECT DISTINCT  \r\n"       
				+ this.selectWaitLot  
				+ " ,CAST(null AS VARCHAR(10) ) as [DyeStatus]\r\n"
//				+ " , null as [DyeStatus]\r\n"
	  		    + " INTO #tempWaitLot  \r\n" 
				+ " FROM #tempMainSale as a \r\n " 
				+ this.innerJoinWaitLotB   
				+ this.leftJoinJ
				+ this.leftJoinTAPP
				+ this.leftJoinK    
				+ this.leftJoinP 
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinSL 
				+ whereWaitLot   
				+ " and ( SumVol = 'B' OR countProdRP > 0 ) \r\n";  
		String fromMainB = ""
				  +	" from ( SELECT distinct \r\n"
				  + this.leftJoinBSelect
				  + "				,Division,CustomerShortName,SaleCreateDate,PurchaseOrder,MaterialNo,CustomerMaterial,Price,SaleUnit\r\n"
				  + "				,OrderAmount,SaleQuantity,RemainQuantity,RemainAmount,CustomerDue,DueDate,ShipDate,CustomerType\r\n"
				  + "				,[SaleNumber],[SaleFullName],DistChannel,Color,ColorCustomer,CustomerName,DeliveryStatus,SaleStatus\r\n"
				  + "			from #tempMainSale as a\r\n"  
				  + "           left join #tempMainPrdTemp as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
				  + this.leftJoinBPartOneT
				  + this.leftJoinBPartOneS
				  + "          "+this.leftJoinTempG   
				  + "          "+this.leftJoinSCC
				  + this.leftJoinBPartOneH
				  + this.leftJoinM 
				  + "           "+this.leftJoinUCAL 
				  + this.leftJoinFSMBBTempSumBill
				  + whereBMainUserStatus
				  + " ) as b \r\n";    
		String sqlMain = ""    
				    +  " SELECT DISTINCT \r\n"       
					+ this.selectMainV2
					+ "   ,CASE  \r\n"       
					+ "     	WHEN ( b.SumVol is not null and ( b.Grade = 'A' or b.Grade is null  or b.Grade  = '') ) THEN b.SumVol \r\n"  
					+ "			ELSE  NULL\r\n"   
					+ "			END AS Volumn \r\n"
					+ "   , CASE  \r\n" 
					+ "     	WHEN ( b.SumVolFGAmount is not null and ( b.Grade = 'A' or b.Grade is null  or b.Grade  = '') ) THEN b.SumVolFGAmount \r\n"  
					+ "			ELSE  NULL\r\n"     
					+ "			END AS VolumnFGAmount  \r\n"
					+ "   , 'Main' as TypePrd \r\n"
					+ "   , 'Main' AS TypePrdRemark \r\n"
					+ "   , CustomerType\r\n"
					+ "   , b.[DyeStatus]\r\n"
		  		    + " INTO #tempMain  \r\n"   
					+ fromMainB 
					+ "  left join ( SELECT distinct   a.[ProductionOrder] \r\n"
					+ "  			  				  , a.[SaleOrder] \r\n"
					+ "                             , a.[SaleLine] \r\n"
					+ "                             , [PlanDate] AS CFMPlanLabDate \r\n"
					+ "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
					+ "			  inner join (select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
					+ "				          FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
					+ "				          group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
					+ "				          on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
					+ "				         and a.[CreateDate] = b.[MaxCreateDate] \r\n"
					+ "			) as e on e.ProductionOrder = b.ProductionOrder and e.SaleOrder = b.SaleOrder and e.SaleLine = b.SaleLine\r\n"
					+ this.leftJoinJ_B 
					+ this.leftJoinTAPP 
					+ " left join ( SELECT SALELINE,SALEORDER,ProductionOrder,ReplacedRemark \r\n"
					+ "			   FROM [PCMS].[dbo].[InputReplacedRemark] \r\n"
					+ "			   WHERE DataStatus = 'O')   AS K on K.ProductionOrder = b.ProductionOrder and K.SaleOrder = b.SaleOrder and K.SaleLine = b.SaleLine \r\n"
					+ " left join (SELECT SALELINE,SALEORDER,ProductionOrder,Grade ,StockRemark\r\n"
					+ "			   FROM [PCMS].[dbo].[InputStockRemark] \r\n"
					+ "			   WHERE DataStatus = 'O')   AS l on l.ProductionOrder = b.ProductionOrder and \r\n"
					+ "                                              l.SaleOrder = b.SaleOrder and\r\n"
					+ "                                              l.SaleLine = b.SaleLine and\r\n"
					+ "                                              l.Grade = b.Grade\r\n"
					+ " left join (SELECT SALELINE,SALEORDER,ProductionOrder,PCRemark\r\n"
					+ "			   FROM [PCMS].[dbo].InputPCRemark \r\n"
					+ "			   WHERE DataStatus = 'O')   AS P on P.ProductionOrder = b.ProductionOrder and P.SaleOrder = b.SaleOrder and P.SaleLine = b.SaleLine\r\n"
					+ " left join (SELECT ProductionOrder,[CauseOfDelay]\r\n"
					+ "			   FROM [PCMS].[dbo].[InputCauseOfDelay] \r\n"
					+ "			   WHERE DataStatus = 'O') AS InputCOD on b.ProductionOrder = InputCOD.ProductionOrder \r\n"
					+ " left join (SELECT  ProductionOrder,[DelayedDep]\r\n"
					+ "			   FROM [PCMS].[dbo].[InputDelayedDep] \r\n"
					+ "			   WHERE DataStatus = 'O') AS InputDD on b.ProductionOrder = InputDD.ProductionOrder \r\n"
					+ " left join (SELECT  ProductionOrder,SwitchRemark\r\n"
					+ "			   FROM [PCMS].[dbo].InputSwitchRemark \r\n"
					+ "			   WHERE DataStatus = 'O') AS q on b.ProductionOrder = q.ProductionOrder \r\n"
					+ " left join ( SELECT SALELINE,SALEORDER,ProductionOrder,StockLoad \r\n"
					+ "			  FROM [PCMS].[dbo].InputStockLoad \r\n"
					+ "			  where Datastatus = 'O')AS SL on SL.ProductionOrder = b.ProductionOrder and SL.SaleOrder = b.SaleOrder and SL.SaleLine = b.SaleLine  \r\n"
					+ "  LEFT JOIN ( SELECT [SaleOrder] ,[SaleLine]  ,1 as countnRP  \r\n"
					+ "			   FROM [PCMS].[dbo].[ReplacedProdOrder] \r\n"
					+ "			   WHERE DataStatus = 'O'\r\n"
					+ "			   GROUP BY  [SaleOrder] ,[SaleLine]\r\n"
					+ "   )  as CRP on  CRP.SaleOrder = b.SaleOrder and CRP.SaleLine = b.SaleLine\r\n"  
					+ " where ( b.SumVol Is not null\r\n" //20230911 FIX HERE
//					+ " where (\r\n"
//					+ "		( b.SumVol >= 0 )\r\n"
////				+ " where ( b.SumVol <> 0\r\n" //20230911 FIX HERE
					+ "		or ( (  b.LotNo = 'รอจัด Lot'  or  b.LotNo = 'ขาย stock' or b.LotNo = 'รับจ้างถัก' or b.LotNo = 'Lot ขายแล้ว' \r\n"
					+ "				or b.LotNo = 'พ่วงแล้วรอสวม' or b.LotNo = 'รอสวมเคยมี Lot'  )  and b.SumVol = 0 \r\n" 
					+ "		     and ( countnRP is null )  \r\n"
					+ "        ) \r\n" 
					+ "     or  RealVolumn = 0 \r\n"
					+ "     or ( b.UserStatus = 'ยกเลิก' or b.UserStatus = 'ตัดเกรดZ') \r\n"
					+ "     )\r\n"        
					+ " and NOT EXISTS ( select distinct ProductionOrderSW\r\n"
					+ "				   FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA\r\n"
					+ "				   WHERE DataStatus = 'O' and BAA.ProductionOrderSW = B.ProductionOrder\r\n"
					+ "				 )   \r\n" 
//					+ whereCaseTry
					; 
		 
		String createTempOPFromA = ""
				+ " If(OBJECT_ID('tempdb..#tempPrdOPA') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOPA\r\n"
				+ "	end ; " 
				+ " SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
				+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n" 
				+ " 		  , 'SUB' as TypePrdRemark \r\n" 
				+ "  		  , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ " 		  , CASE \r\n"
				+ "					WHEN b.Volumn <> 0 THEN b.Volumn \r\n"
				+ "    				ELSE  0 \r\n"     
				+ "    				END AS Volumn \r\n"
				+ "           , m.[Grade],[PriceSTD],GRSumMR,GRSumKG,GRSumYD,UCAL.UserStatusCal \r\n"
				+ "			  , g.CFMActualLabDate\r\n"
				+ "			  , g.CFMCusAnsLabDate \r\n"
				+ "			  , g.CFMPlanDate AS CFMPlanDate  \r\n" 
				+ " 		  , CASE \r\n"
				+ "					WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
				+ "    				ELSE  g.SendCFMCusDate \r\n"     
				+ "    				END AS SendCFMCusDate\r\n"
				+ "			  ,g.CFMDateActual\r\n"
				+ "           , g.CFMDetailAll \r\n"
				+ "			  , g.CFMNumberAll \r\n"
				+ "			  , g.CFMRemarkAll \r\n"
				+ "			  , g.RollNoRemarkAll \r\n"
				+ "    		  , g.LotShipping \r\n"
				+ "           ,g.DyePlan\r\n"
				+ "           ,g.DyeActual\r\n" 
				+ "			, FSMBB.BillSendWeightQuantity \r\n"
				+ "			, case\r\n"
				+ "					WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
				+ "					WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
				+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
				+ "				end AS BillSendQuantity \r\n" 
				+ "			, FSMBB.BillSendMRQuantity\r\n"
				+ "			, FSMBB.BillSendYDQuantity \r\n"
				+ "         , CustomerType\r\n"
				+ "         , g.PlanGreigeDate \r\n"
				+ "         , g.[DyeStatus]\r\n"
				+ "         into #tempPrdOPA\r\n"  
				+ "		 	from #tempMainSale as a  \r\n"
				+ "		 	inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"  
				+ "         "+this.leftJoinTempG   
				+ "         "+this.leftJoinM   
				+ "         "+this.leftJoinUCAL    
			    + "         "+this.leftJoinFSMBBTempSumBill 
				+ "         "+ this.leftJoinSCC
				+ "		 	WHERE b.DataStatus = 'O' \r\n" 
				+ "         "+tmpWhereNoLotUCAL  	
				+ "   If(OBJECT_ID('tempdb..#tempPrdOP') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOP\r\n"
				+ "	end ; \r\n "
				+ " SELECT DISTINCT \r\n"       
				+ this.selectOPV2 
				+ "   , CASE\r\n" 
    			+ "			WHEN a.Grade = 'A' OR a.Grade is null THEN  a.Volumn\r\n"
    			+ "			ELSE  NULL\r\n"
    			+ "			END AS Volumn\r\n"  
    			+ "   , CASE\r\n"
    			+ "			WHEN a.Grade = 'A' OR a.Grade is null THEN a.Price *  a.Volumn \r\n"
    			+ "			ELSE  NULL\r\n"
    			+ "			END AS VolumnFGAmount  \r\n"
				+ "   ,'OrderPuang' as TypePrd \r\n" 
    			+ "   ,TypePrdRemark \r\n"
    			+ "   , CustomerType\r\n"
				+ "   , a.[DyeStatus]\r\n"
				+ " into #tempPrdOP\r\n"
				+ " FROM #tempPrdOPA as a  \r\n "  
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"          
				+ this.leftJoinE     
				+ this.leftJoinH   
				+ this.leftJoinJ
				+ this.leftJoinTAPP
				+ this.leftJoinK      
				+ this.leftJoinMainl 
				+ this.leftJoinP
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinQ 
				+ this.leftJoinSL;
		String sqlOP = ""
					+ " select \r\n"
					+ this.selectAll 
		  		    + " INTO #tempOP  \r\n"
					+ " from #tempPrdOP as a \r\n"  
					+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') \r\n"   
					+ " and NOT EXISTS ( select distinct ProductionOrderSW \r\n"
					+ "				   FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA \r\n"
					+ "				   WHERE DataStatus = 'O' and BAA.ProductionOrderSW = A.ProductionOrder\r\n"
					+ "				 ) \r\n "
					+ whereCaseTry ; 
		String createTempOPSWFromA = ""
				+ " If(OBJECT_ID('tempdb..#tempPrdOPSW') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOPSW\r\n"
				+ "	end ; " 
				 	+ " SELECT DISTINCT  \r\n"       
					+ this.selectSW 
					+ "	  , CASE  \r\n"
	    			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN  a.Volumn\r\n"
	    			+ "			ELSE  NULL\r\n"
	    			+ "			END AS Volumn   \r\n"  	
	    			+ "	  , CASE  \r\n"
	    			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Price *  a.Volumn \r\n"
	    			+ "			ELSE  NULL\r\n"
	    			+ "			END AS VolumnFGAmount  \r\n"   
					+ "	  , 'OrderPuang' as TypePrd \r\n" 
	    			+ "	  , TypePrdRemark \r\n"
	    			+ "   , CustomerType\r\n" 
					+ "   , g.[DyeStatus]\r\n"
		  		    + " INTO #tempPrdOPSW  \r\n"
					+ " FROM (  SELECT DISTINCT  \r\n"
					+ "			    a.SaleOrder , a.[SaleLine] ,a.DistChannel ,a.Color ,a.ColorCustomer  \r\n"
					+ "	          , a.SaleQuantity ,a.RemainQuantity ,a.SaleUnit ,a.DueDate ,a.CustomerShortName \r\n"
					+ "           , a.[SaleFullName] , a.[SaleNumber] ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus \r\n"
					+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount \r\n" 
					+ "           , 'SUB' as TypePrdRemark \r\n" 
					+ "           , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
					+ "           , CASE WHEN b.Volumn <> 0 THEN b.Volumn \r\n"
					+ "             	ELSE  0 \r\n"     
					+ "             	END AS Volumn  \r\n"
					+ "           ,CustomerType \r\n" 
					+ "		 	from #tempMainSale as a  \r\n" 
					+ "		 	inner join ( \r\n"
					+ "            	SELECT \r\n"
					+ " 				CASE \r\n"
					+ "			          WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
					+ "			          ELSE C.ProductionOrder\r\n"
					+ "			          END AS [ProductionOrder],\r\n"
					+ "		           [SaleOrder] ,[SaleLine] ,[Volumn]  ,[DataStatus]\r\n"
					+ "		        FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
					+ "		        LEFT JOIN (SELECT [ProductionOrder] ,[ProductionOrderSW] \r\n"
					+ "					       FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
					+ "					       WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
					+ "					       AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
					+ "		        LEFT JOIN (SELECT  [ProductionOrder] \r\n"
					+ "								  ,[ProductionOrderSW] \r\n"
					+ "					       FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
					+ "					       WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
					+ "					       AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n" 
					+ "				WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL and a.DataStatus = 'O') \r\n" 
					+ "       	) as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n" 
					+ "			where b.DataStatus <> 'X' and b.SaleLine <> '' ) as a  \r\n "  
					+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"       
					+ this.leftJoinE    
					+ this.leftJoinTempG    
					+ this.leftJoinSCC  
					+ this.leftJoinH    
					+ this.leftJoinJ
					+ this.leftJoinTAPP
					+ this.leftJoinK 
					+ this.leftJoinMainl   
					+ this.leftJoinP
					+ this.leftJoinInputCOD 
					+ this.leftJoinInputDD  
					+ this.leftJoinQ 
					+ this.leftJoinSL 
					+ this.leftJoinM   
					+ this.leftJoinUCAL      
				    + this.leftJoinFSMBBTempSumBill  ;
	String sqlOPSW = ""
			+ " select \r\n"
			+ this.selectAll 
  		    + " INTO #tempOPSW  \r\n"
			+ " from #tempPrdOPSW as a \r\n"   
			+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') \r\n"  
			+ whereCaseTry ;  
//////					// Switch   
	String createTempSWFromA = ""

			+ " If(OBJECT_ID('tempdb..#tempPrdSW') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempPrdSW\r\n"
			+ "	end ; " 
			+ " SELECT DISTINCT  \r\n"       
			+ this.selectSW 
			+ "   , CASE  \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN  ( b.Volumn - a.SumVol )\r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS Volumn   \r\n"  
			+ "   , CASE  \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Price * ( b.Volumn - a.SumVol ) \r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS VolumnFGAmount  \r\n"
			+ "   , 'Switch' as TypePrd \r\n"  
			+ "   , TypePrdRemark \r\n"
			+ "   ,CustomerType\r\n"
			+ "   , g.[DyeStatus]\r\n"
  		    + " INTO #tempPrdSW  \r\n"
			+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
			+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
			+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
			+ "	          , b.ProductionOrderSW as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
			+ "  		  ,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
			+ " 		  , CASE \r\n"
			+ "					when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
			+ "					ELSE 'SUB' \r\n"
			+ "					END	TypePrdRemark  ,C.SumVol \r\n"
			+ "           ,CustomerType\r\n"  
			+ "		 	from #tempMainSale as a  \r\n"
			+ "		 	inner join [PCMS].[dbo].[SwitchProdOrder]  as b on a.SaleLine = b.SaleLineSW and a.SaleOrder = b.SaleOrderSW  \r\n \r\n"
			+ "		 	LEFT JOIN ( \r\n"
			+ "				SELECT PRDORDERSW ,sum([Volumn]) as SumVol\r\n"
			+ "				FROM (  SELECT \r\n"
			+ "                           a.[ProductionOrder] \r\n"
			+ "					  		, CASE \r\n"
			+ "								WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
			+ "								ELSE C.ProductionOrder\r\n"
			+ "								END AS PRDORDERSW\r\n"
			+ "					  		, [SaleOrder] ,[SaleLine] ,[Volumn] ,[DataStatus]\r\n"
			+ "				      	FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
			+ "				  		LEFT JOIN (	SELECT  [ProductionOrder] \r\n"
			+ "										   ,[ProductionOrderSW] \r\n"
			+ "							  		FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
			+ "							  		WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
			+ "							   		AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
			+ "				  		LEFT JOIN (SELECT  [ProductionOrder] \r\n"
			+ "										  ,[ProductionOrderSW] \r\n"
			+ "							  	   FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
			+ "							  	   WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
			+ "							  	   AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n"
			+ "						WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL)\r\n"
			+ "					) AS A\r\n"
			+ "					group by PRDORDERSW\r\n" 
			+ "		 		) AS C ON B.ProductionOrderSW = C.PRDORDERSW \r\n" 
			+ "		    where b.DataStatus <> 'X') as a  \r\n "  
			+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"         
			+ this.leftJoinE    
			+ this.leftJoinTempG 
			+ this.leftJoinSCC
			+ this.leftJoinH  
			+ this.leftJoinJ
			+ this.leftJoinTAPP
			+ this.leftJoinK  
			+ this.leftJoinMainl  
			+ this.leftJoinP
			+ this.leftJoinInputCOD 
			+ this.leftJoinInputDD  
			+ this.leftJoinQ 
			+ this.leftJoinSL 
			+ this.leftJoinM   
			+ this.leftJoinUCAL  
		    + this.leftJoinFSMBBTempSumBill ;
	String sqlSW =  ""
		    + " select \r\n"
			+ this.selectAll 
  		    + " INTO #tempSW  \r\n"
			+ " from #tempPrdSW as a \r\n"   
			+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') \r\n"  
			+ whereCaseTry ;   
	 
//////				// สวม  
	String createTempRP = ""
			+ " If(OBJECT_ID('tempdb..#tempPrdReplaced') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempPrdReplaced\r\n"
			+ "	end ; "
			+ " SELECT DISTINCT  \r\n"       
			+ this.selectRPV2
			+ "   , CASE \r\n"
    		+ "			WHEN m.Grade = 'A' OR m.Grade is null and rpo.Volume <> 0 THEN rpo.Volume\r\n" 
    		+ "			WHEN m.Grade = 'A' OR m.Grade is null and b.Volumn <> 0 THEN b.Volumn\r\n" 
    		+ "			ELSE NULL\r\n"        
    		+ "			END AS Volumn  \r\n"  
    		+ "   , CASE \r\n"
    		+ "			WHEN m.Grade = 'A' OR m.Grade is null and rpo.Volume <> 0  THEN a.Price * rpo.Volume \r\n" 
    		+ "			WHEN m.Grade = 'A' OR m.Grade is null and b.Volumn <> 0  THEN a.Price * b.Volumn \r\n" 
    		+ "			ELSE NULL\r\n"
    		+ "			END AS VolumnFGAmount \r\n"
			+ "   ,'Replaced' as TypePrd \r\n"   
			+ "   ,'SUB' as TypePrdRemark  \r\n"
			+ "   ,CustomerType\r\n"
			+ "   , g.[DyeStatus]\r\n"
			+ " into #tempPrdReplaced\r\n"
			+ " from #tempMainSale as a  \r\n" 
			+ " inner join ( select SaleOrder , SaleLine,[Volume], [ProductionOrderRP] AS ProductionOrder,DataStatus  from [PCMS].[dbo].[ReplacedProdOrder] )  as rpo on a.SaleLine = rpo.SaleLine and a.SaleOrder = rpo.SaleOrder and  rpo.DataStatus <> 'X'  \r\n"  
			+ " inner join  [PCMS].[dbo].[FromSapMainProd] as b on b.ProductionOrder = rpo.ProductionOrder \r\n" 
			+ this.leftJoinE    
			+ this.leftJoinTempG    
			+ this.leftJoinSCC
			+ this.leftJoinBH  
			+ this.leftJoinJ
			+ this.leftJoinTAPP
			+ this.leftJoinK
			+ this.leftJoinM       
			+ this.leftJoinl  
			+ this.leftJoinP 
			+ this.leftJoinInputCOD 
			+ this.leftJoinInputDD  
			+ this.leftJoinUCALRP   
			+ this.leftJoinQ 
			+ this.leftJoinSL 
		    + this.leftJoinFSMBBTempSumBill 
			+ " where ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n" 
			+ whereCaseTryRP    ;;  
	String sqlRP = ""
				+ " select \r\n"
				+ this.selectAll 
	  		    + " INTO #tempRP  \r\n"
				+ " from #tempPrdReplaced as a \r\n"    
				+ " where ( a.UserStatus <> 'ยกเลิก' and a.UserStatus <> 'ตัดเกรดZ') \r\n" 
				+ whereCaseTry    ;
//				+ " and ( a.UserStatus <> 'ยกเลิก' and a.UserStatus <> 'ตัดเกรดZ') \r\n" ; 
	 String sql =
			 		"" 
				+ " SET NOCOUNT ON; ;\r\n"   
				+ " If(OBJECT_ID('tempdb..#tempWaitLot') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempWaitLot\r\n"
				+ "	end ; \r\n" 
				+ " If(OBJECT_ID('tempdb..#tempMain') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempMain\r\n"
				+ "	end ; \r\n" 
				+ " If(OBJECT_ID('tempdb..#tempOP') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempOP\r\n"
				+ "	end ; \r\n" 
				+ " If(OBJECT_ID('tempdb..#tempOPSW') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempOPSW\r\n"
				+ "	end ; \r\n" 
				+ " If(OBJECT_ID('tempdb..#tempSW') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempSW \r\n"
				+ "	end ; \r\n" 
				+ " If(OBJECT_ID('tempdb..#tempRP') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempRP \r\n"
				+ "	end ; \r\n"  
				+ this.declareTempApproved
				+ createTempMainSale
			 	+ this.createTempPlanDeliveryDate
			    + this.createTempMainPrdFromTempA
			 	+ this.createTempSumGR    
			 	+ this.createTempSumBill 
			 	+ createTempRP
			 	+ createTempOPFromA 
			 	+ createTempOPSWFromA
			 	+ createTempSWFromA
				+ sqlWaitLot 
				+ sqlMain        	        
				+ sqlOP        
				+ sqlOPSW      
				+ sqlSW  
				+ sqlRP  

				+ "  SELECT a.* FROM #tempWaitLot as a\r\n"
				+ " left join  #tempMain as b on a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
				+ " where b.SaleOrder is null \r\n"
				+ " union ALL  \r\n"  
				+ " SELECT * FROM #tempMain\r\n"
				+ " union ALL  \r\n"  
				+ " SELECT * FROM #tempOP\r\n"       
				+ " union ALL  \r\n"		
				+ " SELECT * FROM #tempOPSW\r\n"
				+ " union ALL  \r\n" 
				+ " SELECT * FROM #tempSW\r\n"
				+ " union ALL  \r\n"
				+ " SELECT * FROM #tempRP\r\n"  
				+ " Order by CustomerShortName, DueDate, [SaleOrder], [SaleLine],TypePrdRemark, [ProductionOrder] " 
				;          
//		System.out.println("Before SQL: "+new Date());
		List<Map<String, Object>> datas = this.database.queryList(sql);    
//		System.out.println("AFTER SQL: "+new Date());
		list = new ArrayList<PCMSSecondTableDetail>(); 
		for (Map<String, Object> map : datas) {  
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}           
		return list;	
	} 
 
	 
 
	@Override
	public ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList) {
		PlanCFMDateModel pcfmdModel = new PlanCFMDateModel();
		PlanDeliveryDateModel pddModel = new PlanDeliveryDateModel( );
		PlanCFMLabDateModel pcfmldModel = new PlanCFMLabDateModel( );
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();  
		ArrayList<InputDateDetail> list  = new ArrayList<InputDateDetail>();
		ArrayList<InputDateDetail> listCount  = new ArrayList<InputDateDetail>();
		String fromTable = "";
		int check = 0;
		PCMSSecondTableDetail bean = poList.get(0);
		String caseSave = bean.getCaseSave();
		String planDate = "";
//		java.util.Date today = new java.util.Date();
//		String todayString=sdf3.format(today);
		if(caseSave.equals("cfmPlanLabDate")) { 
			planDate = bean.getCfmPlanLabDate();
			fromTable = " [PCMS].[dbo].[PlanCFMLabDate] "  ;
			list = pcfmldModel.getMaxCFMPlanLabDateDetail(poList);
			listCount = pcfmldModel.getCountCFMPlanLabDateDetail(poList);
			check = list.size();
			} 
		else if(caseSave.equals("cfmPlanDate")) { 
			planDate = bean.getCfmPlanDate();
			fromTable = "[PCMS].[dbo].[PlanCFMDate] "  ; 
			list = pcfmdModel.getMaxCFMPlanDateDetail(poList) ;
			listCount = pcfmdModel.getCountCFMPlanDateDetail(poList);
			check = list.size();	
		} 
		else if(caseSave.equals("deliveryDate")) { 
			planDate = bean.getDeliveryDate();
			fromTable = "[PCMS].[dbo].[PlanDeliveryDate] "  ; 
			list = pddModel.getMaxDeliveryPlanDateDetail(poList) ;
			listCount = pddModel.getCountDeliveryPlanDateDetail(poList);
			check = list.size();	
		}   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		ArrayList<InputDateDetail> listInput = new ArrayList<InputDateDetail>();
		java.util.Date date ;
		InputDateDetail beanInput = new InputDateDetail();
		if(check > 0) {
			beanInput.setIconStatus("I");
			beanInput.setSystemStatus("Date : "+planDate+" already confirm.Try to refresh again."); 
		}  
		else {
			try {  
				String sql = ""; 
					sql =  " insert into "
							+ fromTable
							+ " ( "
							+ "		[ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[PlanDate]  ,[CreateBy]  , "  //5
							+ "		[CreateDate] ,[LotNo] " 
							+ "     ) "//24   
							+ " 	values(? , ? , ? , ? , ?"//  1
							+ "			  ,? , ? "
							+ " ) ;"; 
					prepared = connection.prepareStatement(sql);   
					prepared.setString(1, bean.getProductionOrder());
					prepared.setString(2, bean.getSaleOrder());
					prepared.setString(3, saleLine); 
					if (planDate.equals("undefined") || planDate.equals("")  || !isValidDate(planDate)) {
						prepared.setNull(4, java.sql.Types.DATE);
					} 
					else {
						date = sdf2.parse(planDate);
						prepared.setDate(4, convertJavaDateToSqlDate(date) );
					}   
					prepared.setString(5, bean.getUserId());
					prepared.setTimestamp(6, new Timestamp(time));;   
					prepared.setString(7, bean.getLotNo());
					prepared.executeUpdate();  
					if(caseSave.equals("CFMPlanDate")) {
						beanInput.setIconStatus("I0");
					}
					else {   
						beanInput.setIconStatus("I1");
					} 
					beanInput.setSystemStatus("Update Success."); 
					if(listCount.size() > 0) {
						beanInput.setCountPlanDate(listCount.get(0).getCountPlanDate()+1);
					}
					else { 	 
						beanInput.setCountPlanDate(1); 
					} 
//				}    	 
			}catch (SQLException | ParseException e) {
				System.err.println(e.getMessage());
				beanInput.setIconStatus("E");
				beanInput.setSystemStatus("Something happen, Please contact IT.");
			} 
		}    
		listInput.add(beanInput);
		return listInput;
	} 

	public static boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> saveInputDetail(ArrayList<PCMSSecondTableDetail> poList) {
		TEMP_UserStatusAutoModel tusaModel = new TEMP_UserStatusAutoModel();
		PCMSSecondTableDetail bean = poList.get(0);  
//		String prodOrder = bean.getProductionOrder();
		String caseSave = bean.getCaseSave();
		String valueChange = ""; 
		String tableName = "";  
		if(caseSave.equals("stockRemark")) {
			valueChange = bean.getStockRemark(); 
			tableName = "[InputStockRemark]";
			bean = this.updateLogRemarkWithGrade(tableName,bean,this.CLOSE_STATUS);
			bean = this.upSertRemarkCaseWithGrade(tableName,valueChange,bean);
			poList.clear();
			poList.add(bean); 
		}  
		else if(caseSave.equals("pcRemark")){
			valueChange = bean.getPcRemark(); 
			tableName = "[InputPCRemark]"; 
			bean = this.updateLogRemarkCaseOne(tableName,bean,this.CLOSE_STATUS);
			bean = this.upSertRemarkCaseOne(tableName,valueChange,bean); 
			poList.clear();
			poList.add(bean); 
		} 
		else if(caseSave.equals("stockLoad")){
			 BackGroundJobModel bgjModel = new BackGroundJobModel();
			valueChange = bean.getStockLoad(); 
			tableName = "[InputStockLoad]"; 
			bean = this.updateLogRemarkCaseOne(tableName,bean,this.CLOSE_STATUS);
			bean = this.upSertRemarkCaseOne(tableName,valueChange,bean);  
			bgjModel.execUpsertToTEMPUserStatusOnWebWithProdOrder(bean.getProductionOrder());
            ArrayList<TempUserStatusAutoDetail> list = tusaModel.getTempUserStatusAutoDetail(poList); 
            if(list.size() > 0) {
            	TempUserStatusAutoDetail beanTmp = list.get(0);
            	if(beanTmp.getProductionOrderRPM().equals("")) { 
            		bean.setUserStatus(beanTmp.getUserStatusCal());
            	}
            	else if(!beanTmp.getProductionOrderRPM().equals("")) { 
            		bean.setUserStatus(beanTmp.getUserStatusCalRP());
            	}
            	else {
            		bean.setUserStatus("");
            	}
            }
        	else {
        		bean.setUserStatus("");
        	}
			poList.clear();
			poList.add(bean); 
		} 
		else if(caseSave.equals("replacedRemark")){
			valueChange = bean.getReplacedRemark();
			tableName = "[InputReplacedRemark]";  
			bean = this.updateLogRemarkCaseOne(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseOne(tableName,valueChange,bean);
			poList = this.handlerReplacedProdOrder(bean);      
			
		}     
		else if(caseSave.equals("switchRemark")){
			valueChange = bean.getSwitchRemark();
			tableName = "[InputSwitchRemark]";  
			bean = this.updateLogRemarkCaseTwo(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseTwo(tableName,valueChange,bean);
			poList = this.handlerSwitchProdOrder(bean); 
		}    
		else if(caseSave.equals("delayedDep")){
			valueChange = bean.getDelayedDepartment();
			tableName = "[InputDelayedDep]";  
			bean = this.updateLogRemarkCaseTwo(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseTwo(tableName,valueChange,bean);
			poList.clear();
			poList.add(bean); 
		} 
		else if(caseSave.equals("causeOfDelay")){
			valueChange = bean.getCauseOfDelay();
			tableName = "[InputCauseOfDelay]";  
			bean = this.updateLogRemarkCaseTwo(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseTwo(tableName,valueChange,bean);
			poList.clear();
			poList.add(bean); 
		}   
		else if(caseSave.equals("sendCFMCusDate")) { 
			valueChange = bean.getSendCFMCusDate(); 
			tableName = "[PlanSendCFMCusDate] "  ;    
			bean = this.updateLogRemarkCaseThree(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseThree(tableName,valueChange,bean); 
			poList.clear();
			poList.add(bean); 
		}  
		return poList;   
	} 

  
 
 
	private ArrayList<PCMSSecondTableDetail> handlerReplacedProdOrder(PCMSSecondTableDetail bean) {
		FromSapMainProdModel fsmpModel = new FromSapMainProdModel();
		ReplacedProdOrderModel rpoModel = new ReplacedProdOrderModel();
		SwitchProdOrderModel spoModel = new SwitchProdOrderModel();
		ArrayList<PCMSSecondTableDetail> list = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<PCMSSecondTableDetail>();     
		ArrayList<PCMSSecondTableDetail> poListOld = new ArrayList<PCMSSecondTableDetail>();     
		ArrayList<PCMSSecondTableDetail> poListOldNormal = new ArrayList<PCMSSecondTableDetail>();     
//		ArrayList<PCMSSecondTableDetail> poListTmp = new ArrayList<PCMSSecondTableDetail>(); 
		ArrayList<PCMSSecondTableDetail> listRP  = new ArrayList<PCMSSecondTableDetail>();     
//		ArrayList<PCMSSecondTableDetail> listOPMainTwo   = new ArrayList<PCMSSecondTableDetail>();
		String repRemark = bean.getReplacedRemark().trim();  
		String volume = "";  
		String prdOrder = bean.getProductionOrder().trim();
		String prdOrderRP = "";
		String saleOrder = bean.getSaleOrder().trim() ;
		String saleLine = bean.getSaleLine().trim() ; 
		String userId = bean.getUserId().trim();   
		ArrayList<SwitchProdOrderDetail> listSWMainOne = spoModel.getSwitchProdOrderDetailByPrd(prdOrder); 
		ArrayList<SwitchProdOrderDetail> listSWMainTwo = spoModel.getSwitchProdOrderDetailByPrdSW(prdOrder);  
		String tableName = "[InputReplacedRemark]";     
		boolean numeric = true; 	
		boolean errCheck = true; 
		String[] newRPSplit = repRemark.split(",");      
		listRP.clear();
		if(errCheck == false) {  }
		else if(repRemark.equals("")) { 
//			-----------------------------------------------------
			list.add(bean);   
			listRP = this.getReplacedCaseByProdOrder(this.C_PRODORDER,list);  
				bean = rpoModel.updateReplacedProdOrder(bean, "X");         
			if(listRP.size()>0) {    
				for(int i = 0 ; i < listRP.size();i++) {
					PCMSSecondTableDetail beanTmp = new PCMSSecondTableDetail();
					beanTmp.setProductionOrder(listRP.get(i).getProductionOrder());
					poListOld.add(beanTmp);	
				}  
				// get normal case from main prod order RP Remark 
				poList = this.getNormalCaseByProdOrder(this.C_PRODORDER,poListOld);  
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success."); 
			}
			else {     
//				bean = this.updateReplacedPrd(bean, "X");    
				poList.clear();  
				bean.setReplacedRemark(""); 
				bean = this.updateLogRemarkCaseFix(tableName,"",bean); 
				bean.setIconStatus("E");   
				bean.setSystemStatus("Something happen.Please contact IT.");   
				poList.add(bean);  
			} 
			poList = this.setBeanIconStatus(poList,bean);  
		}
		else if(listSWMainOne.size() > 0) {
			String prodOrderCheck = "";
			if(listSWMainOne.size() > 0) { prodOrderCheck = listSWMainOne.get(0).getProductionOrder(); }
			bean.setIconStatus("E");
			bean.setSystemStatus("Prod.Order already switch between "+prdOrder+" and "+ prodOrderCheck+".");
			poList.add(bean);  
		} 
		else if(listSWMainTwo.size() > 0) {
			String prodOrderCheck = "";
			if(listSWMainTwo.size() > 0) {
				prodOrderCheck = listSWMainTwo.get(0).getProductionOrder();
			}
			bean.setIconStatus("E");
			bean.setSystemStatus("Prod.Order already switch between "+prdOrder+" and "+ prodOrderCheck+".");
			poList.add(bean);  
		} 
		else if(newRPSplit.length > 0) {    
//			list.add(bean);         
			prdOrder = bean.getProductionOrder().trim();  
			ArrayList<ReplacedProdOrderDetail> listRPOld = rpoModel.getReplacedProdOrderDetailByPrdMain(prdOrder);
			bean = rpoModel.updateReplacedProdOrder(bean, "X");  
			ArrayList<PCMSSecondTableDetail> checkList = null; 
			for(int i = 0 ; i < newRPSplit.length; i++) {
				String[] subSplit = newRPSplit[i].split("=");
				ReplacedProdOrderDetail beanRP = new ReplacedProdOrderDetail();
				numeric = true;
				prdOrderRP = "";
				if(subSplit.length == 1) {
					prdOrderRP = newRPSplit[i].trim();
					volume =  "0";
				}
				else {
					prdOrderRP = subSplit[0].trim();
					 try {     
						 volume =  subSplit[1].trim(); 
						 Double.parseDouble(volume);
			        } catch (NumberFormatException e) { numeric = false; }
					 if(numeric) { }
					 else { volume = "0";  } 
				} 
				if(!prdOrderRP.equals("")) {
					checkList = fsmpModel.getFromSapMainProdDetail(prdOrderRP);
					if(checkList.size() == 0) { 
						errCheck = false;
						bean.setIconStatus("E");
						bean.setSystemStatus(prdOrderRP+" is not in the Database or wrong data entry. ");  
						poList.add(bean);
						break;
					}
					else {
						PCMSSecondTableDetail beanCheckRP = checkList.get(0); 
						listRP.add(beanCheckRP);
						
						beanRP.setProductionOrder(prdOrder);
						beanRP.setSaleOrder(saleOrder);
						beanRP.setSaleLine(saleLine);
						beanRP.setProductionOrderRP(prdOrderRP ); 
						beanRP.setChangeBy(userId);  
						beanRP.setVolume(volume);  
						ReplacedProdOrderDetail beanX = rpoModel.upsertReplacedProdOrder(beanRP,"O");
						if(beanX.getIconStatus().equals("E")) {
							errCheck = false;
							bean.setIconStatus(beanX.getIconStatus());
							bean.setSystemStatus(beanX.getSystemStatus());
						}  
						PCMSSecondTableDetail beanTMP = new PCMSSecondTableDetail();
						beanTMP.setProductionOrder(prdOrder); 
						beanTMP.setProductionOrderRP(prdOrderRP);
						poList.add(beanTMP)  ;
					} 
				}
 			}       
			if(errCheck) {
				listRP = this.getWaitLotCaseBySaleOrder(listRP);
				poListOld = this.getNormalCaseByProdOrder(this.C_PRODORDERRP,poList);   
				poList = this.getReplacedCaseByProdOrder(this.C_PRODORDERRP,poList);     
				int i = 0 ;  
				ArrayList<String> listCompareOne = new ArrayList<>(); 
		        ArrayList<String> listCompareTwo = new ArrayList<>(); 
				for(i = 0;i<poList.size();i++) { listCompareOne.add(poList.get(i).getProductionOrder()); }    
				for(i = 0;i<listRPOld.size();i++) { listCompareTwo.add(listRPOld.get(i).getProductionOrderRP());  }  
				listCompareTwo.removeAll(listCompareOne); 
				for(i = 0;i<listCompareTwo.size();i++) {   
					PCMSSecondTableDetail beanTMP = new PCMSSecondTableDetail();
					beanTMP.setProductionOrder(listCompareTwo.get(i));
					poListOldNormal.add(beanTMP); 
				}     
				if(poListOldNormal.size()>0) { poListOldNormal = this.getNormalCaseByProdOrder(this.C_PRODORDER,poListOldNormal);  }   
				for(i = 0;i<poListOld.size();i++) { poList.add(poListOld.get(i));  }   
				for(i = 0;i<poListOldNormal.size();i++) { poList.add(poListOldNormal.get(i));  }    
				for(i = 0;i<listRP.size();i++) { poList.add(listRP.get(i));  }    
				poList = this.setBeanIconStatus(poList,bean); 
			}  
			else {  
				poList = this.setBeanIconStatus(poList,bean); 
			}  
		}  
		return poList; 
	}    
	 
 

	private ArrayList<PCMSSecondTableDetail> getWaitLotCaseBySaleOrder(ArrayList<PCMSSecondTableDetail> listRP) {
		ArrayList<PCMSSecondTableDetail> list = null;        
		String where = " where  ";  
//		String whereTempUCAL = " where  ";  
		if (listRP.size() > 0)  { 
//			whereTempUCAL += " ( ";  
			String saleOrder = ""; 
			String saleLine = ""; 
			int sizeList = listRP.size() ;
			where += " ( ";
			for ( int i = 0;i<sizeList;i++) {
				PCMSSecondTableDetail bean = listRP.get(i);
				saleOrder = bean.getSaleOrder(); 
				saleLine = bean.getSaleLine();
				where = where + " ( a.SaleOrder = '"+saleOrder+"' and a.SaleLine = '"+saleLine+"' ) "; 
				if (i != sizeList - 1) {
					where += " or ";     
				} ;
			} 
			where += " ) ";
		}   
		
		String sql = ""
				+ this.declareTempApproved
				+ this.createTempMainSale 
				+ " SELECT DISTINCT  \r\n"       
				+ this.selectWaitLot 
				+ " FROM #tempMainSale as a \r\n " 
				+ this.innerJoinWaitLotB 
				+ this.leftJoinJ
				+ this.leftJoinTAPP
				+ this.leftJoinK    
				+ this.leftJoinP 
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinSL 
				+ where   
				+ " and ( SumVol = 'B' OR countProdRP > 0 ) ";
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		list = new ArrayList<PCMSSecondTableDetail>(); 
		for (Map<String, Object> map : datas) {  
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}        
		return list;
	}

	private ArrayList<PCMSSecondTableDetail> handlerSwitchProdOrder(PCMSSecondTableDetail bean) {
		SwitchProdOrderModel spoModel = new SwitchProdOrderModel();
		ReplacedProdOrderModel rpoModel = new ReplacedProdOrderModel();
		
		ArrayList<PCMSSecondTableDetail> list = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poListTMP = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poListOld = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poListOP = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poListOPSW = new ArrayList<PCMSSecondTableDetail>();
		String prdOrderSW = bean.getSwitchRemark().trim();
		String prdOrder = bean.getProductionOrder().trim(); 
		if(prdOrderSW.equals("")) {       
			list.add(bean);   
			poList = this.getSwitchProdOrderListByPrd(list);      
			bean = spoModel.updateSwitchProdOrderDetail(bean, "X");    
			if(poList.size() >0) { 
				poListOP = this.getOrderPuangListByPrd( poList);  
				poList = this.getNormalCaseByProdOrder(this.C_PRODORDER,poList); 
				int i = 0 ;
				for(i = 0;i<poListOP.size();i++) {
					poList.add(poListOP.get(i));
				} 
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success."); 
			}
			else {
				poList.clear(); 
				bean.setIconStatus("E");
				bean.setSystemStatus("Something happen.Please contact IT.");  
				poList.add(bean);  
			} 
			poList = this.setBeanIconStatus(poList,bean);   
		}
		else {

			list = spoModel.getSwitchProdOrderDetailByProdOrderForHandlerSwitchProd(prdOrderSW);
//			List<Map<String, Object>> datas = this.database.queryList(sql); 
//			for (Map<String, Object> map : datas) { list.add(this.bcModel._genPCMSSecondTableDetail(map)); }
			if(list.size() > 0 ) {
				PCMSSecondTableDetail beanL = list.get(0);
				ArrayList<ReplacedProdOrderDetail> listRPSubOne = rpoModel.getReplacedProdOrderDetailByPrdRP(prdOrder);
				ArrayList<ReplacedProdOrderDetail> listRPSubTwo = rpoModel.getReplacedProdOrderDetailByPrdRP(prdOrderSW);
				ArrayList<ReplacedProdOrderDetail> listRPMainOne = rpoModel.getReplacedProdOrderDetailByPrd(prdOrder);
				ArrayList<ReplacedProdOrderDetail> listRPMainTwo = rpoModel.getReplacedProdOrderDetailByPrd(prdOrderSW);
				int countPrdSW = beanL.getCountInSW(); 
				int countCaseMOne = listRPMainOne.size(); 
				int countCaseMTwo = listRPMainTwo.size(); 
				int countCaseSOne = listRPSubOne.size(); 
				int countCaseSTwo  = listRPSubTwo.size(); 
				if(countCaseMOne > 0) {  
					ReplacedProdOrderDetail beanRP = listRPMainOne.get(0);
					bean.setIconStatus("E");
					bean.setSystemStatus("Prod.Order already replaced sale from "+beanRP.getProductionOrder()+" by "+ beanRP.getProductionOrderRP()+".");
					poList.add(bean);     
				}
				else if(countCaseMTwo > 0) {  
					ReplacedProdOrderDetail beanRP = listRPMainTwo.get(0);
					bean.setIconStatus("E");
					bean.setSystemStatus("Prod.Order already replaced sale from "+beanRP.getProductionOrder()+" by "+ beanRP.getProductionOrderRP()+".");
					poList.add(bean);     
				}
				else if(countCaseSOne > 0) {  
					ReplacedProdOrderDetail beanRP = listRPSubOne.get(0);
					bean.setIconStatus("E");
					bean.setSystemStatus("Prod.Order already replaced sale by "+beanRP.getProductionOrder() +".");
					poList.add(bean);     
				}
				else if(countCaseSTwo > 0) {  
					ReplacedProdOrderDetail beanRP = listRPSubTwo.get(0);
					bean.setIconStatus("E");
					bean.setSystemStatus("Prod.Order already replaced sale by "+beanRP.getProductionOrder() +".");
					poList.add(bean);     
				}
				else if(countPrdSW > 0) { 
					ArrayList<SwitchProdOrderDetail> listCheck = spoModel.getSwitchProdOrderDetailByPrdSW(prdOrderSW);
					String prodOrderCheck = "";
					if(listCheck.size() > 0) {
						prodOrderCheck = listCheck.get(0).getProductionOrder();
					}
					if(prdOrder.equals(prodOrderCheck)) {    
						bean.setIconStatus("W"); 
						bean.setSystemStatus("Data already upadted.");   
					}
					else {   
						bean.setIconStatus("E");
						bean.setSystemStatus("Prod.Order already switch between "+prdOrder+" and "+ prodOrderCheck+".");
						poList.add(bean);     
					}  
				} 
				else { 
					ArrayList<SwitchProdOrderDetail> listSW = spoModel.getSwitchProdOrderDetailByPrd(prdOrder);  
					bean = spoModel.updateSwitchProdOrderDetail(bean, "X");  
					if(listSW.size()>0) {
						String oldProdOrderSW = listSW.get(0).getProductionOrderSW(); 
						PCMSSecondTableDetail beanTmp = new PCMSSecondTableDetail();
						beanTmp.setProductionOrder(oldProdOrderSW);
						poListOld.add(beanTmp); 
						poListOP = this.getOrderPuangListByPrd( poListOld);  
						poListOld = this.getNormalCaseByProdOrder(this.C_PRODORDER,poListOld);  
						int i = 0;
						for(i= 0 ;i<poListOP.size();i++) { poListTMP.add(poListOP.get(i)); }		
						for(i= 0 ;i<poListOPSW.size();i++) { poListTMP.add(poListOPSW.get(i)); }    
					}
					PCMSSecondTableDetail beanD = new PCMSSecondTableDetail();
					// CORE  SALE  LINE PRD PRDSW SALESW LINEDW
					//       1     1    A   B
					//       2     1    B
					// ------------ TABLE --------------
					//  X1   1     1    A   B      1      1
					//  X2   1     1    A   A      2      1
					// ---------------------------------
					//  X1 
					beanD.setUserId(bean.getUserId()); 
					beanD.setSaleOrder(bean.getSaleOrder());              //1
					beanD.setSaleLine(bean.getSaleLine());                //1
					beanD.setProductionOrder(prdOrder);  //A 
					beanD.setProductionOrderSW(prdOrderSW);               //B
					beanD.setSaleOrderSW(bean.getSaleOrder());            //1
					beanD.setSaleLineSW(bean.getSaleLine());              //1  
					beanD = spoModel.upsertSwitchProdOrder(beanD, "O");
					//  X2 
//					beanD.setSaleOrder(bean.getSaleOrder());              //1
//					beanD.setSaleLine(bean.getSaleLine());                //1
//					beanD.setProductionOrder(bean.getProductionOrder());  //A
					bean.setProductionOrderSW(prdOrder);                  //A 
					bean.setSaleOrderSW(beanL.getSaleOrder());            //2
					bean.setSaleLineSW(beanL.getSaleLine());              //1   
					bean = spoModel.upsertSwitchProdOrder(bean, "O"); 
//					bean.setIconStatus("I");
//					bean.setSystemStatus("Update Success.");  
					poList.add(bean);        
					poList = this.getSwitchProdOrderListByPrd(poList); 
					poListOP = this.getOrderPuangListByPrd( poList);  
					poListOPSW = this.getOrderPuangSWListByPrd( poList);   
					if(poListOld.size() > 0) { poList.add(poListOld.get(0)); }    
					int i = 0;
					for(i= 0 ;i<poListOP.size();i++) { poList.add(poListOP.get(i)); }		
					for(i= 0 ;i<poListOPSW.size();i++) { poList.add(poListOPSW.get(i)); }  
					for(i= 0 ;i<poListTMP.size();i++) { poList.add(poListTMP.get(i)); }  
					poList = this.setBeanIconStatus(poList,bean); 
				} 
			}    
			else { 
				bean = spoModel.updateSwitchProdOrderDetail(bean, "X");
				bean.setIconStatus("E");
				bean.setSystemStatus("ProductionOrder is not in the Database or wrong data entry. ");  
				poList.add(bean);  
			} 
		}    
		return poList; 
	} 
 

	private ArrayList<PCMSSecondTableDetail> setBeanIconStatus(ArrayList<PCMSSecondTableDetail> poList,
			PCMSSecondTableDetail bean) {
		int i = 0;
		for( i = 0 ; i < poList.size(); i++) {
			PCMSSecondTableDetail beanL = poList.get(i);
			beanL.setIconStatus(bean.getIconStatus());
			beanL.setSystemStatus(bean.getSystemStatus()); 
		}
		return poList;
	}
  
 
	@Override
	public ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = ""
					+ " SET NOCOUNT ON; ;\r\n"   
					+  " SELECT \r\n"
					+ "		 [ProductionOrder]\r\n"
				    + "     ,[SaleOrder]\r\n"
				    + "     ,[SaleLine]\r\n"
				    + "     ,[PlanDate]\r\n"
				    + "     ,[CreateBy]\r\n"
				    + "     ,[CreateDate]\r\n"
				    + "	  	,'0:PCMS' as InputFrom \r\n"
				    + "     ,LotNo \r\n"
				    + " FROM [PCMS].[dbo].[PlanDeliveryDate] as a\r\n" 
				    + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and \r\n"
				    + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
				    + "       a.[SaleLine] = '" + saleLine+ "' \r\n"
				 	+ " union ALL  \r\n "
				 	+ " SELECT \r\n"
				 	+ "      [ProductionOrder]\r\n"
				    + "      ,[SaleOrder]\r\n"
				    + "      ,[SaleLine]\r\n"
				    + "      ,[CFType] as [PlanDate]\r\n"
				    + "      ,'' AS [CreateBy]\r\n"
				    + "      ,null AS [CreateDate]\r\n"  
				    + "	     , '1:SAP' as InputFrom \r\n"
				    + "      ,'' AS LotNo \r\n"
					+ " FROM [PCMS].[dbo].[FromSapMainProd] as a\r\n" 
					+ " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and CFType is not null  \r\n"
					+ " ORDER BY InputFrom ,CreateDate desc ";
				  
		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;   
	} 	 
 
 
	public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	} 
	@Override
	public ArrayList<PCMSAllDetail> getUserStatusList() {
		FromSapMainProdModel fsmpModel = new FromSapMainProdModel(); 
		ArrayList<PCMSAllDetail> list = fsmpModel.getUserStatusDetail();
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
 
	private String forPage = "Detail"; 
	@Override    
	public ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList) { 
		SearchSettingModel ssModel = new SearchSettingModel();
		ArrayList<PCMSTableDetail> list = null;
		String customerShortName = "", userStatus = "", customerName="",userId = ""  ,divisionName = "";
		PCMSTableDetail bean = poList.get(0);     
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
		ArrayList<PCMSTableDetail> beanCheck = ssModel.getSearchSettingDetail(userId,this.forPage);
		if(beanCheck.size() == 0) {
			list = ssModel.insertSearchSettingDetail(poList,this.forPage);
		}
		else {
			list = ssModel.updateSearchSettingDetail(poList,this.forPage);
		} 
		return list; 
	} 
	@Override  
	public ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		SearchSettingModel ssModel =new SearchSettingModel();
		String userId = poList.get(0).getUserId();
		ArrayList<PCMSTableDetail> bean = ssModel.getSearchSettingDetail(userId,this.forPage);
		return bean;
	}
 
	private ArrayList<PCMSSecondTableDetail> getNormalCaseByProdOrder(String prdOrderType, ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null;        
		String where = " where  ";  
//		String whereTempUCAL = " where  ";  
		if (poList.size() > 0)  {
			where += " ( ";  
//			whereTempUCAL += " ( ";  
			String prodOrder = ""; 
			for ( int i = 0;i<poList.size();i++) {
				if(prdOrderType.equals(this.C_PRODORDER)) { 
					prodOrder = poList.get(i).getProductionOrder();
				}else if(prdOrderType.equals(this.C_PRODORDERRP)) {
					prodOrder = poList.get(i).getProductionOrderRP();
				}  
				where = where + " b.ProductionOrder = '"+prodOrder+"' ";
//				whereTempUCAL = whereTempUCAL + " a.ProductionOrder = '"+prodOrder+"' ";
				if (i != poList.size() - 1) {
					where += " or ";    
//					whereTempUCAL += " or ";    
				} ;
			}
			where += " ) \r\n"; 
//			whereTempUCAL += " ) \r\n"; 
		}   

		String fromMainB = ""
				  +	" from ( SELECT distinct \r\n"
				  + this.leftJoinBSelect
				  + "				,Division,CustomerShortName,SaleCreateDate,PurchaseOrder,MaterialNo,CustomerMaterial,Price,SaleUnit\r\n"
				  + "				,OrderAmount,SaleQuantity,RemainQuantity,RemainAmount,CustomerDue,DueDate,ShipDate,CustomerType\r\n"
				  + "				,[SaleNumber],[SaleFullName],DistChannel,Color,ColorCustomer,CustomerName,DeliveryStatus,SaleStatus\r\n"
				  + "			from #tempMainSale as a\r\n"  
				  + "           left join #tempMainPrdTemp as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
				  + this.leftJoinBPartOneT
				  + this.leftJoinBPartOneS
				  + "          "+this.leftJoinTempG   
				  + "          "+this.leftJoinSCC
				  + this.leftJoinBPartOneH
				  + this.leftJoinM 
				  + "           "+this.leftJoinUCAL 
				  + this.leftJoinFSMBBTempSumBill
//				  + whereBMainUserStatus
				  + " ) as b \r\n"; 
		String sqlMain = ""    
				+ this.declareTempApproved
				+ this.createTempMainSale 
		 		+ this.createTempPlanDeliveryDate
			 	+ this.createTempSumBill
			 	+ this.createTempSumGR 
		        + this.createTempMainPrdFromTempA
			    +  " SELECT DISTINCT \r\n"       
				+ this.selectMainV2
				+ "   ,CASE  \r\n"       
				+ "     	WHEN ( b.SumVol is not null and ( b.Grade = 'A' or b.Grade is null  or b.Grade  = '') ) THEN b.SumVol \r\n"  
				+ "			ELSE  NULL\r\n"   
				+ "			END AS Volumn \r\n"
				+ "   , CASE  \r\n" 
				+ "     	WHEN ( b.SumVolFGAmount is not null and ( b.Grade = 'A' or b.Grade is null  or b.Grade  = '') ) THEN b.SumVolFGAmount \r\n"  
				+ "			ELSE  NULL\r\n"     
				+ "			END AS VolumnFGAmount  \r\n"
				+ "   , 'Main' as TypePrd \r\n"
				+ "   , 'Main' AS TypePrdRemark \r\n"
				+ "   , CustomerType\r\n"
				+ "   , b.[DyeStatus]\r\n"
//	  		    + " INTO #tempMain  \r\n"   
				+ fromMainB 
				+ "  left join ( SELECT distinct   a.[ProductionOrder] \r\n"
				+ "  			  				  , a.[SaleOrder] \r\n"
				+ "                             , a.[SaleLine] \r\n"
				+ "                             , [PlanDate] AS CFMPlanLabDate \r\n"
				+ "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
				+ "			  inner join (select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
				+ "				          FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
				+ "				          group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
				+ "				          on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
				+ "				         and a.[CreateDate] = b.[MaxCreateDate] \r\n"
				+ "			) as e on e.ProductionOrder = b.ProductionOrder and e.SaleOrder = b.SaleOrder and e.SaleLine = b.SaleLine\r\n"
				+ this.leftJoinJ_B 
				+ this.leftJoinTAPP 
				+ " left join ( SELECT SALELINE,SALEORDER,ProductionOrder,ReplacedRemark \r\n"
				+ "			   FROM [PCMS].[dbo].[InputReplacedRemark] \r\n"
				+ "			   WHERE DataStatus = 'O')   AS K on K.ProductionOrder = b.ProductionOrder and K.SaleOrder = b.SaleOrder and K.SaleLine = b.SaleLine \r\n"
				+ " left join (SELECT SALELINE,SALEORDER,ProductionOrder,Grade ,StockRemark\r\n"
				+ "			   FROM [PCMS].[dbo].[InputStockRemark] \r\n"
				+ "			   WHERE DataStatus = 'O')   AS l on l.ProductionOrder = b.ProductionOrder and \r\n"
				+ "                                              l.SaleOrder = b.SaleOrder and\r\n"
				+ "                                              l.SaleLine = b.SaleLine and\r\n"
				+ "                                              l.Grade = b.Grade\r\n"
				+ " left join (SELECT SALELINE,SALEORDER,ProductionOrder,PCRemark\r\n"
				+ "			   FROM [PCMS].[dbo].InputPCRemark \r\n"
				+ "			   WHERE DataStatus = 'O')   AS P on P.ProductionOrder = b.ProductionOrder and P.SaleOrder = b.SaleOrder and P.SaleLine = b.SaleLine\r\n"
				+ " left join (SELECT ProductionOrder,[CauseOfDelay]\r\n"
				+ "			   FROM [PCMS].[dbo].[InputCauseOfDelay] \r\n"
				+ "			   WHERE DataStatus = 'O') AS InputCOD on b.ProductionOrder = InputCOD.ProductionOrder \r\n"
				+ " left join (SELECT  ProductionOrder,[DelayedDep]\r\n"
				+ "			   FROM [PCMS].[dbo].[InputDelayedDep] \r\n"
				+ "			   WHERE DataStatus = 'O') AS InputDD on b.ProductionOrder = InputDD.ProductionOrder \r\n"
				+ " left join (SELECT  ProductionOrder,SwitchRemark\r\n"
				+ "			   FROM [PCMS].[dbo].InputSwitchRemark \r\n"
				+ "			   WHERE DataStatus = 'O') AS q on b.ProductionOrder = q.ProductionOrder \r\n"
				+ " left join ( SELECT SALELINE,SALEORDER,ProductionOrder,StockLoad \r\n"
				+ "			  FROM [PCMS].[dbo].InputStockLoad \r\n"
				+ "			  where Datastatus = 'O')AS SL on SL.ProductionOrder = b.ProductionOrder and SL.SaleOrder = b.SaleOrder and SL.SaleLine = b.SaleLine  \r\n"
				+ "  LEFT JOIN ( SELECT [SaleOrder] ,[SaleLine]  ,1 as countnRP  \r\n"
				+ "			   FROM [PCMS].[dbo].[ReplacedProdOrder] \r\n"
				+ "			   WHERE DataStatus = 'O'\r\n"
				+ "			   GROUP BY  [SaleOrder] ,[SaleLine]\r\n"
				+ "   )  as CRP on  CRP.SaleOrder = b.SaleOrder and CRP.SaleLine = b.SaleLine\r\n"
				+ where   
				+ " and ( b.SumVol <> 0\r\n"
				+ "		or ( (  b.LotNo = 'รอจัด Lot'  or  b.LotNo = 'ขาย stock' or b.LotNo = 'รับจ้างถัก' or b.LotNo = 'Lot ขายแล้ว' \r\n"
				+ "				or b.LotNo = 'พ่วงแล้วรอสวม' or b.LotNo = 'รอสวมเคยมี Lot'  )  and b.SumVol = 0 \r\n" 
				+ "		     and ( countnRP is null )  \r\n"
				+ "        ) \r\n" 
				+ "     or  RealVolumn = 0 \r\n"
				+ "     or ( b.UserStatus = 'ยกเลิก' or b.UserStatus = 'ตัดเกรดZ') \r\n"
				+ "     )\r\n"
				+ " and NOT EXISTS ( select distinct ProductionOrderSW\r\n"
				+ "				   	 FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA\r\n"
				+ "				   	 WHERE DataStatus = 'O' and BAA.ProductionOrderSW = B.ProductionOrder\r\n"
				+ "				   )  \r\n "   
				+ " Order by  CustomerShortName,  DueDate, [SaleOrder], [SaleLine],b.[ProductionOrder]" 
					;          
		List<Map<String, Object>> datas = this.database.queryList(sqlMain); 
		list = new ArrayList<PCMSSecondTableDetail>(); 
		for (Map<String, Object> map : datas) {  
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}        
		return list;	 
	}
	private ArrayList<PCMSSecondTableDetail> getReplacedCaseByProdOrder(String prdOrderType,ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null; 
		String where = " ( \r\n"; 
//		String whereTempUCAL = " where ( \r\n";
//		String ProductionOrder = poList.get(0).getProductionOrder();
		String prodOrder = "";
		for ( int i = 0;i<poList.size();i++) {
			if(prdOrderType.equals(this.C_PRODORDER)) { 
				prodOrder = poList.get(i).getProductionOrder();
//				where = where + " b."+prdOrderType +" = '"+prodOrder+"' ";
			}
			else if(prdOrderType.equals(this.C_PRODORDERRP)) {
				prodOrder = poList.get(i).getProductionOrderRP();
//				where = where + " b.[ProductionOrder]"  +" = '"+prodOrder+"' ";
			}  
			where = where + " "+prdOrderType +" = '"+prodOrder+"' ";
//			where = where + " b.[ProductionOrder] = '"+prodOrder+"' ";
//			where = where + " b."+prdOrderType +" = '"+prodOrder+"' "; // 15/11/2023 	FIX HERE
			if (i != poList.size() - 1) {
				where += " or ";    
			} ;
		}
		where += " ) \r\n";
//		whereTempUCAL += " ) \r\n";
		String createTempRP = ""
				+ " If(OBJECT_ID('tempdb..#tempPrdReplaced') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdReplaced\r\n"
				+ "	end ; "
				+ " SELECT DISTINCT  \r\n"       
				+ this.selectRPV2
				+ "   , CASE \r\n"
	    		+ "			WHEN m.Grade = 'A' OR m.Grade is null and rpo.Volume <> 0 THEN rpo.Volume\r\n" 
	    		+ "			WHEN m.Grade = 'A' OR m.Grade is null and b.Volumn <> 0 THEN b.Volumn\r\n" 
	    		+ "			ELSE NULL\r\n"        
	    		+ "			END AS Volumn  \r\n"  
	    		+ "   , CASE \r\n"
	    		+ "			WHEN m.Grade = 'A' OR m.Grade is null and rpo.Volume <> 0  THEN a.Price * rpo.Volume \r\n" 
	    		+ "			WHEN m.Grade = 'A' OR m.Grade is null and b.Volumn <> 0  THEN a.Price * b.Volumn \r\n" 
	    		+ "			ELSE NULL\r\n"
	    		+ "			END AS VolumnFGAmount \r\n"
				+ "   ,'Replaced' as TypePrd \r\n"   
				+ "   ,'SUB' as TypePrdRemark  \r\n"
				+ "   ,CustomerType\r\n"    
				+ "   , g.[DyeStatus]\r\n"
				+ " into #tempPrdReplaced\r\n"
				+ " from #tempMainSale as a  \r\n"
				+ " inner join ( \r\n"
				+ "		select SaleOrder , SaleLine,[Volume], [ProductionOrderRP] AS ProductionOrder,DataStatus  \r\n"
				+ "		from [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "		where "+where+"\r\n"
				+ "	)  as rpo on a.SaleLine = rpo.SaleLine and a.SaleOrder = rpo.SaleOrder and  rpo.DataStatus <> 'X'  \r\n"  
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on b.ProductionOrder = rpo.ProductionOrder \r\n" 
				+ this.leftJoinE    
				+ this.leftJoinTempG    
				+ this.leftJoinSCC
				+ this.leftJoinBH  
				+ this.leftJoinJ
				+ this.leftJoinTAPP
				+ this.leftJoinK
				+ this.leftJoinM       
				+ this.leftJoinl  
				+ this.leftJoinP 
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinUCALRP   
				+ this.leftJoinQ 
				+ this.leftJoinSL 
			    + this.leftJoinFSMBBTempSumBill 
				+ " where ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n" 
//				+ where
				;
		String sqlRP = ""
					+ this.declareTempApproved
					+ this.createTempMainSale
			 		+ this.createTempPlanDeliveryDate
				 	+ this.createTempSumBill
				 	+ this.createTempSumGR
				 	+ createTempRP
					+ " select \r\n"
					+ this.selectAll 
					+ " from #tempPrdReplaced as a \r\n" 
//					+ " where ( a.UserStatus <> 'ยกเลิก' and a.UserStatus <> 'ตัดเกรดZ') \r\n"
					;  
//		System.out.println(sqlRP);
		List<Map<String, Object>> datas = this.database.queryList(sqlRP);
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}
		return list;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null; 
		String where = " ( \r\n";  
		for ( int i = 0;i<poList.size();i++) {
			String ProductionOrder = poList.get(i).getProductionOrder();
			where = where + " b.ProductionOrder = '"+ProductionOrder+"' "; 
			if (i != poList.size() - 1) {
				where += " or ";    
			} ;
		}
		where += " ) \r\n"; 
		String createTempSWFromA = ""

			+ " If(OBJECT_ID('tempdb..#tempPrdSW') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempPrdSW\r\n"
			+ "	end ; " 
			+ " SELECT DISTINCT  \r\n"       
			+ this.selectSW 
			+ "   , CASE  \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN  ( b.Volumn - a.SumVol )\r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS Volumn   \r\n"  
			+ "   , CASE  \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Price * ( b.Volumn - a.SumVol ) \r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS VolumnFGAmount  \r\n"
			+ "   , 'Switch' as TypePrd \r\n"  
			+ "   , TypePrdRemark \r\n"
			+ "   ,CustomerType\r\n"
			+ "   , g.[DyeStatus]\r\n"
  		    + " INTO #tempPrdSW  \r\n"
			+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
			+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
			+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
			+ "	          , b.ProductionOrderSW as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
			+ "  		  ,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
			+ " 		  , CASE \r\n"
			+ "					when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
			+ "					ELSE 'SUB' \r\n"
			+ "					END	TypePrdRemark  ,C.SumVol\r\n "
			+ "           ,CustomerType\r\n" 
//			+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
			+ "		 	from #tempMainSale as a  \r\n"
			+ "		 	inner join [PCMS].[dbo].[SwitchProdOrder]  as b on a.SaleLine = b.SaleLineSW and a.SaleOrder = b.SaleOrderSW  \r\n \r\n"
			+ "		 	LEFT JOIN ( \r\n"
			+ "				SELECT PRDORDERSW ,sum([Volumn]) as SumVol\r\n"
			+ "				FROM (  SELECT \r\n"
			+ "                           a.[ProductionOrder] \r\n"
			+ "					  		, CASE \r\n"
			+ "								WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
			+ "								ELSE C.ProductionOrder\r\n"
			+ "								END AS PRDORDERSW\r\n"
			+ "					  		, [SaleOrder] ,[SaleLine] ,[Volumn] ,[DataStatus]\r\n"
			+ "				      	FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
			+ "				  		LEFT JOIN (	SELECT  [ProductionOrder] \r\n"
			+ "										   ,[ProductionOrderSW] \r\n"
			+ "							  		FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
			+ "							  		WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
			+ "							   		AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
			+ "				  		LEFT JOIN (SELECT  [ProductionOrder] \r\n"
			+ "										  ,[ProductionOrderSW] \r\n"
			+ "							  	   FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
			+ "							  	   WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
			+ "							  	   AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n"
			+ "						WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL)\r\n"
			+ "					) AS A\r\n"
			+ "					group by PRDORDERSW\r\n" 
			+ "		 		) AS C ON B.ProductionOrderSW = C.PRDORDERSW \r\n" 
			+ "		    where b.DataStatus <> 'X' \r\n"
			+ "            AND "+where+") as a  \r\n "  
			+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"         
			+ this.leftJoinE    
			+ this.leftJoinTempG 
			+ this.leftJoinSCC
			+ this.leftJoinH  
			+ this.leftJoinJ
			+ this.leftJoinTAPP
			+ this.leftJoinK  
			+ this.leftJoinMainl  
			+ this.leftJoinP
			+ this.leftJoinInputCOD 
			+ this.leftJoinInputDD  
			+ this.leftJoinQ 
			+ this.leftJoinSL 
			+ this.leftJoinM   
			+ this.leftJoinUCAL  
		    + this.leftJoinFSMBBTempSumBill ;
	String sqlSW = ""
			+ this.declareTempApproved
			+ this.createTempMainSale
		 	+ this.createTempPlanDeliveryDate
			+ this.createTempSumBill
		 	+ this.createTempSumGR
		 	+ createTempSWFromA
//		 	+ " SELECT DISTINCT  "     
//		 	+ this.selectSW
		    + " select distinct\r\n"
			+ this.selectAll  
			+ " from #tempPrdSW as a \r\n"   
			+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') \r\n"  ;    
		List<Map<String, Object>> datas = this.database.queryList(sqlSW);
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}
		return list;
	} 
	public ArrayList<PCMSSecondTableDetail> getOrderPuangListByPrd(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null; 
		String where = " ( \r\n";  
		for ( int i = 0;i<poList.size();i++) {
			String ProductionOrder = poList.get(i).getProductionOrder();
			where = where + " b.ProductionOrder = '"+ProductionOrder+"' "; 
			if (i != poList.size() - 1) {
				where += " or ";    
			} ;
		}
		where += " ) \r\n"; 
		String createTempOPFromA = ""
				+ " If(OBJECT_ID('tempdb..#tempPrdOPA') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOPA\r\n"
				+ "	end ; "

					+ " SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
					+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
					+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
					+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n" 
					+ " 		  , 'SUB' as TypePrdRemark \r\n" 
					+ "  		  , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
					+ " 		  , CASE \r\n"
					+ "					WHEN b.Volumn <> 0 THEN b.Volumn \r\n"
					+ "    				ELSE  0 \r\n"     
					+ "    				END AS Volumn \r\n"
					+ "           , m.[Grade],[PriceSTD],GRSumMR,GRSumKG,GRSumYD,UCAL.UserStatusCal \r\n"
					+ "			  , g.CFMActualLabDate\r\n"
					+ "			  , g.CFMCusAnsLabDate \r\n"
					+ "			  , g.CFMPlanDate AS CFMPlanDate  \r\n" 
					+ " 		  , CASE \r\n"
					+ "					WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
					+ "    				ELSE  g.SendCFMCusDate \r\n"     
					+ "    				END AS SendCFMCusDate\r\n"
					+ "			  ,g.CFMDateActual\r\n"
					+ "           , g.CFMDetailAll \r\n"
					+ "			  , g.CFMNumberAll \r\n"
					+ "			  , g.CFMRemarkAll \r\n"
					+ "			  , g.RollNoRemarkAll \r\n"
					+ "			  , g.DyePlan \r\n"
					+ "			  , g.DyeActual \r\n"
//					+ "			  , CASE \r\n"
//					+ "					WHEN ( g.DyePlan is NULL) THEN null \r\n"
//					+ "				   	ELSE CAST(DAY( g.DyePlan) AS VARCHAR(2)) + '/' +   CAST(MONTH( g.DyePlan)AS VARCHAR(2))  \r\n"
//					+ "			  		END AS DyePlan \r\n"
//					+ "			  , CASE \r\n"
//					+ "					WHEN ( g.DyeActual is NULL) THEN null \r\n"
//					+ "				  	ELSE CAST(DAY( g.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( g.DyeActual)AS VARCHAR(2))  \r\n"
//					+ "			  		END AS DyeActual  \r\n"
					+ "			, FSMBB.BillSendWeightQuantity \r\n"
					+ "			, case\r\n"
					+ "					WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
					+ "					WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
					+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
					+ "				end AS BillSendQuantity \r\n" 
					+ "			, FSMBB.BillSendMRQuantity\r\n"
					+ "			, FSMBB.BillSendYDQuantity \r\n"
					+ "         , CustomerType\r\n"
					+ "         , g.PlanGreigeDate\r\n"
					+ "         , g.[DyeStatus]\r\n"
					+ "         into #tempPrdOPA\r\n" 
					+ "		 	from #tempMainSale as a  \r\n"
					+ "		 	inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"  
					+ "         "+this.leftJoinTempG   
					+ "         "+this.leftJoinM   
					+ "         "+this.leftJoinUCAL    
				    + "         "+this.leftJoinFSMBBTempSumBill 
					+ "         "+ this.leftJoinSCC
					+ "		 	WHERE b.DataStatus = 'O' \r\n" 
					+ "         and "+where 
					+ "\r\n" 
				+ "   If(OBJECT_ID('tempdb..#tempPrdOP') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOP\r\n"
				+ "	end ; \r\n "
				+ " SELECT DISTINCT \r\n"       
				+ this.selectOPV2 
				+ "   , CASE  \r\n" 
    			+ "			WHEN a.Grade = 'A' OR a.Grade is null THEN  a.Volumn\r\n"
    			+ "			ELSE  NULL\r\n"
    			+ "			END AS Volumn   \r\n"  
    			+ "   , CASE  \r\n"
    			+ "			WHEN a.Grade = 'A' OR a.Grade is null THEN a.Price *  a.Volumn \r\n"
    			+ "			ELSE  NULL\r\n"
    			+ "			END AS VolumnFGAmount  \r\n"
				+ "   ,'OrderPuang' as TypePrd \r\n" 
    			+ "   ,TypePrdRemark \r\n"
				+ "   , CustomerType\r\n"
				+ "   , a.[DyeStatus]\r\n"
				+ " into #tempPrdOP\r\n"
				+ " FROM #tempPrdOPA as a  \r\n "  
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"          
				+ this.leftJoinE     
				+ this.leftJoinH   
				+ this.leftJoinJ
				+ this.leftJoinTAPP
				+ this.leftJoinK      
				+ this.leftJoinMainl 
				+ this.leftJoinP
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinQ 
				+ this.leftJoinSL;
		String sqlOP = "" 
				+ this.declareTempApproved
					+ this.createTempMainSale
			 		+ this.createTempPlanDeliveryDate
			 		+ this.createTempSumBill
			 		+ this.createTempSumGR
			 		+ createTempOPFromA
					+ " select distinct \r\n"
					+ this.selectAll  
					+ " from #tempPrdOP as a \r\n"           
					+ " WHERE ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') \r\n"   
					+ " and NOT EXISTS ( select distinct ProductionOrderSW \r\n"
					+ "				   FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA \r\n"
					+ "				   WHERE DataStatus = 'O' and BAA.ProductionOrderSW = A.ProductionOrder\r\n"
					+ "				 ) \r\n " ; 
		List<Map<String, Object>> datas = this.database.queryList(sqlOP);
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}
		return list;
	}
	public ArrayList<PCMSSecondTableDetail> getOrderPuangSWListByPrd(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null; 
		String where = " ( \r\n";  
		for ( int i = 0;i<poList.size();i++) {
			String ProductionOrder = poList.get(i).getProductionOrder();
			where = where + " b.ProductionOrder = '"+ProductionOrder+"' "; 
			if (i != poList.size() - 1) {
				where += " or ";    
			} ;
		}
		where += " ) \r\n";  
 
		String sql =  ""
//				this.createTempUCAL 
//				+this.createTempG
//			 	+ this.createTempCFM
				+ this.declareTempApproved
				+ this.createTempMainSale
			 	+ this.createTempPlanDeliveryDate
				 + this.createTempSumBill
			 	+ this.createTempSumGR
			 	+ " SELECT DISTINCT  "       
				+ this.selectSW 
				+ "	  , CASE  \r\n"
	  			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN  a.Volumn\r\n"
	  			+ "			ELSE  NULL\r\n"
	  			+ "			END AS Volumn   \r\n"  	
	  			+ "	  , CASE  \r\n"
	  			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Price *  a.Volumn \r\n"
	  			+ "			ELSE  NULL\r\n"
	  			+ "			END AS VolumnFGAmount  \r\n"   
				+ "   ,'OrderPuang' as TypePrd \r\n" 
    			+ "   ,TypePrdRemark \r\n"
    			+ "   , CustomerType\r\n"
				+ "   , g.[DyeStatus]\r\n"
				+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
				+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n" 
				+ "           , 'SUB' as TypePrdRemark \r\n" 
				+ "           , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ "           , CASE \r\n"
				+ "					WHEN b.Volumn <> 0 THEN b.Volumn \r\n"
				+ "             	ELSE  0 \r\n"     
				+ "             	END AS Volumn \r\n" 
				+ "          , CustomerType\r\n"
				+ "		   	from #tempMainSale as a  \r\n"
				+ "		 	inner join ( \r\n"
				+ "            SELECT CASE \r\n"
				+ "			          	WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
				+ "			          	ELSE C.ProductionOrder\r\n"
				+ "			          	END AS [ProductionOrder]\r\n"
				+ "		              ,[SaleOrder] ,[SaleLine] ,[Volumn]  ,[DataStatus]\r\n"
				+ "		       FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
				+ "		       LEFT JOIN (SELECT \r\n "
				+ "								[ProductionOrder] ,[ProductionOrderSW] \r\n"
				+ "					      FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
				+ "					      WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
				+ "					      AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
				+ "		       LEFT JOIN (SELECT  [ProductionOrder] \r\n"
				+ "								 ,[ProductionOrderSW] \r\n"
				+ "					       FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
				+ "					       WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
				+ "					       AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n" 
				+ "			   WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL and a.DataStatus = 'O') \r\n" 
				+ "       	   ) as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
				+ "        "+ this.leftJoinM   
				+ "        "+ this.leftJoinUCAL    
			    + "         "+this.leftJoinFSMBBTempSumBill
				+ "		    where b.DataStatus <> 'X' AND \r\n"
				+ "      	     "+where+" ) as a  \r\n "  
				+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"  
//				+ this.leftJoinC	       
				+ this.leftJoinE    
				+ this.leftJoinTempG   
				+ this.leftJoinSCC
				+ this.leftJoinH 
//				+ this.leftJoinLB  
				+ this.leftJoinJ
				+ this.leftJoinTAPP
				+ this.leftJoinK 
				+ this.leftJoinMainl   
				+ this.leftJoinP
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinQ 
				+ this.leftJoinSL 
				+ this.leftJoinM   
				+ this.leftJoinUCAL    
//			    + this.leftJoinFSMBB
			    + this.leftJoinFSMBBTempSumBill 
				+ " where \r\n"      
//				+ " ( c.DataStatus <> 'X'  OR C.DataStatus IS NULL )  AND "
//				+ " a.Volumn <> '0'\r\n" 
				+ "		( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"     ; 
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}
		return list;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByRowProd(ArrayList<PCMSSecondTableDetail> poList) { 
		SwitchProdOrderModel spoModel = new SwitchProdOrderModel();
		String prodOrder = poList.get(0).getProductionOrder();
		ArrayList<SwitchProdOrderDetail> list =  spoModel.getSWProdOrderDetailByPrd(prodOrder); 
		if(list.size()> 0 ) { prodOrder = list.get(0).getProductionOrder(); }
		ArrayList<PCMSSecondTableDetail> listPST = new ArrayList<PCMSSecondTableDetail>(); 
		if(prodOrder.equals("")) {
			PCMSSecondTableDetail bean = new PCMSSecondTableDetail();
			bean.setIconStatus("E");
			bean.setSystemStatus("This Prod.Order already remove switch prod.order from other user.");
			listPST.add(bean);
		}  
		else {  
			listPST = spoModel.getSwitchProdOrderDetailByProdOrder(prodOrder) ;
		}    
		return listPST;
	} 
	
	private PCMSSecondTableDetail upSertRemarkCaseThree(String tableName, String planDate,
			PCMSSecondTableDetail bean) { 
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();     
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime(); 
		String caseSave = bean.getCaseSave();
		try {      
			String sql =   
					  " INSERT INTO [PCMS].[dbo]."+tableName +" \r\n"
					+ " ([ProductionOrder] ,"+caseSave+",[ChangeBy] ,[ChangeDate],[LotNo])"//55 
					+ " values(? , ? , ? , ? , ?   )  "
					+ ";"  ;     	   
				int index = 1;
				prepared = connection.prepareStatement(sql);     
				prepared.setString(index, prdOrder);index+=1;
				prepared.setString(index, planDate);index+=1;  
				prepared = this.sshUtl.setSqlDate(prepared, planDate, index);index+=1; 
				prepared.setString(index, bean.getUserId());index+=1;  
				prepared.setTimestamp(index, new Timestamp(time));index+=1;
				prepared.setString(index, bean.getLotNo());index+=1;  
				prepared.executeUpdate();  
				bean.setIconStatus("I"); 
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) { 
			System.err.println("upSertRemarkCaseThree"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}   
		return bean;
	}

	private PCMSSecondTableDetail updateLogRemarkCaseOne(String tableName, PCMSSecondTableDetail bean,
			String close_STATUS) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));   
		try {      
			String sql =  
					  " UPDATE [PCMS].[dbo]."+tableName
					+ " 	SET DataStatus = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? and DataStatus = 'O'; " ;
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, close_STATUS);
				prepared.setString(2, bean.getUserId());
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);  
				prepared.setString(5, saleOrder);  
				prepared.setString(6, saleLine);   
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("updateLogRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean; 
	}
	private PCMSSecondTableDetail updateLogRemarkCaseFix(String tableName,String valueChange, PCMSSecondTableDetail bean ) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		String caseSave = bean.getCaseSave();
		try {      
			String sql =  
					"UPDATE [PCMS].[dbo]."+tableName
					+ " SET "+caseSave+" = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? and DataStatus = 'O' " ;
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, valueChange);
				prepared.setString(2, bean.getUserId());  
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);  
				prepared.setString(5, saleOrder);        
				prepared.setString(6, saleLine);    
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("updateLogRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean; 
	}
	private PCMSSecondTableDetail updateLogRemarkWithGrade(String tableName, PCMSSecondTableDetail bean,
			String Status) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
//		String caseSave = bean.getCaseSave();
		String grade = bean.getGrade();
		try {      
			String sql =  
					"UPDATE [PCMS].[dbo]."+tableName
					+ " SET DataStatus = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? and [Grade] = ? and DataStatus = 'O' " ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, Status);
				prepared.setString(2, bean.getUserId());
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);  
				prepared.setString(5, saleOrder);  
				prepared.setString(6, saleLine);  
				prepared.setString(7, grade);  
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("updateLogRemarkWithGrade"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}
		return bean;    
	}

	private PCMSSecondTableDetail updateLogRemarkCaseThree(String tableName, PCMSSecondTableDetail bean,
			String close_STATUS) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
//		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		currentTime.getTime();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));   
		try {      
			String sql =  
					"UPDATE [PCMS].[dbo]."+tableName
					+ " SET DataStatus = ?  "
					+ " WHERE [ProductionOrder]  = ?  and DataStatus = 'O' ; " ; 
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, close_STATUS); 
				prepared.setString(2, prdOrder);   
				 prepared.executeUpdate();  
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {  
			System.err.println("updateLogRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean;
	}

	private PCMSSecondTableDetail upSertRemarkCaseOne(String tableName, String valueChange,
			PCMSSecondTableDetail bean) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		String caseSave = bean.getCaseSave();
		try {      
			String sql =   
					  " INSERT INTO [PCMS].[dbo]."+tableName
					+ " ([ProductionOrder],[SaleOrder] ,[SaleLine],"+caseSave+",[ChangeBy] ,[ChangeDate])"//55 
					+ " values \r\n"
					+ "	(? , ? , ? , ? , ? "
					+ ", ? )  "
					+ ";"  ;     	 
				prepared = connection.prepareStatement(sql);     
				prepared.setString(1, prdOrder);  
				prepared.setString(2, saleOrder);  
				prepared.setString(3, saleLine);  
				prepared.setString(4, valueChange);  
				prepared.setString(5, bean.getUserId());  
				prepared.setTimestamp(6, new Timestamp(time));
				prepared.executeUpdate();    
				bean.setIconStatus("I");  
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("upSertRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean;
	}
	private PCMSSecondTableDetail updateLogRemarkCaseTwo(String tableName, PCMSSecondTableDetail bean,
			String close_STATUS) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
//		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));   
		try {      
			String sql =  
					  " UPDATE [PCMS].[dbo]."+tableName
					+ " SET DataStatus = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ?  and DataStatus = 'O' ; " ; 
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, close_STATUS);
				prepared.setString(2, bean.getUserId());
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);   
				 prepared.executeUpdate();  
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {  
			System.err.println("updateLogRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean; 
	}
	private PCMSSecondTableDetail upSertRemarkCaseTwo(String tableName, String valueChange,
			PCMSSecondTableDetail bean) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
//		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		String caseSave = bean.getCaseSave();
		try {      
			String sql =   
					 " INSERT INTO [PCMS].[dbo]."+tableName +" \r\n"
					+ " ([ProductionOrder] ,"+caseSave+",[ChangeBy] ,[ChangeDate])"//55 
					+ " values(? , ? , ? , ?   )  "
					+ ";"  ;     	   
				prepared = connection.prepareStatement(sql);     
				prepared.setString(1, prdOrder);   
				prepared.setString(2, valueChange);  
				prepared.setString(3, bean.getUserId());  
				prepared.setTimestamp(4, new Timestamp(time));
				 prepared.executeUpdate();  
				bean.setIconStatus("I"); 
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) { 
			System.err.println("upSertRemarkCaseTwo"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean;
	}
	private PCMSSecondTableDetail upSertRemarkCaseWithGrade(String tableName, String valueChange,
			PCMSSecondTableDetail bean) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		String caseSave = bean.getCaseSave();
		String grade = bean.getGrade();
		try {      
			String sql =   
					  " INSERT INTO [PCMS].[dbo]."+tableName
					+ " 	([ProductionOrder],[SaleOrder] ,[SaleLine],"+caseSave+",[ChangeBy] "
					+ " 	,[ChangeDate],[Grade])"//55 
					+ " values \r\n"
					+ "		(? , ? , ? , ? , ? "
					+ "    , ? , ? )  ;"  ;     	
				prepared = connection.prepareStatement(sql);     
				prepared.setString(1, prdOrder);  
				prepared.setString(2, saleOrder);  
				prepared.setString(3, saleLine);  
				prepared.setString(4, valueChange);  
				prepared.setString(5, bean.getUserId());  
				prepared.setTimestamp(6, new Timestamp(time));  
				prepared.setString(7, grade);  
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("upSertRemarkCaseWithGrade"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}    
		return bean;
	}

}
