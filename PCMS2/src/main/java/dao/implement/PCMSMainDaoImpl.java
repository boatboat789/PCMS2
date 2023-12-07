	package dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.PCMSMainDao;
import entities.CFMDetail;
import entities.ColumnHiddenDetail;
import entities.ConfigCustomerUserDetail;
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

public class PCMSMainDaoImpl implements PCMSMainDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	private String select = 
			  "     a.SaleOrder \r\n"
			+ "	  , CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"  
			+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
			+ "			END AS [SaleLine] \r\n"
			+ "   , CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" 
			+ "			WHEN 0 THEN ''   \r\n"
			+ "			ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+a.[SaleFullName]\r\n"
			+ "    		END AS [SaleFullName]   \r\n" 
			+ "   , a.DesignFG\r\n" 
			+ "   , a.ArticleFG\r\n" 
			+ "   , a.DistChannel\r\n"
			+ "   , a.Color\r\n" 
			+ "   , a.ColorCustomer\r\n"
			+ "   , a.SaleQuantity\r\n"
			+ "   , a.RemainQuantity\r\n"
//			+ "	  , ( a.SaleQuantity - a.RemainQuantity ) as BillQuantity\r\n" 
			+ "   , a.SaleUnit\r\n"  
			+ "   , b.ProductionOrder\r\n"
			+ "   , b.TotalQuantity \r\n"     
			+ "	  , UCAL.UserStatusCal as UserStatus\r\n"
			+ "   , b.LabStatus\r\n"
			+ "   , a.DueDate\r\n"  
			+ "   , g.GreigeInDate \r\n"
			+ "   , g.[DyePlan]  \r\n	"
			+ "   , g.[DyeActual] \r\n"
			+ "   , g.[Dryer]	 \r\n"
			+ "   , g.[Finishing] \r\n"
			+ "   , g.[Inspectation]  \r\n"
			+ "   , g.[Prepare] \r\n"
			+ "   , g.[Preset] \r\n  "
			+ "   , g.[Relax] \r\n  " 
			+ "   , g.[CFMDateActual]\r\n"  
    		+ "   , g.[CFMPlanDate] \r\n "
    		+ "   , g.[LotShipping] \r\n"
    		+ "   , g.[DyeStatus]  \r\n"
			+ "   , h.DeliveryDate \r\n"  
			+ "   , b.LabNo\r\n"
			+ "   , a.CustomerName\r\n"
			+ "   , a.CustomerShortName\r\n"
			+ "   , a.SaleNumber\r\n" 
			+ "   , a.SaleCreateDate\r\n"
			+ "   , b.PrdCreateDate\r\n" 
			+ "   , a.MaterialNo\r\n"
			+ "   , a.DeliveryStatus\r\n"
			+ "   , a.SaleStatus\r\n" 
			+ "   , b.[LotNo] \r\n"
			+ "   , a.ShipDate \r\n"
			+ "  , CASE \r\n"
			+ "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
			+ "    	ELSE  g.SendCFMCusDate \r\n"
			+ "   END AS SendCFMCusDate\r\n"
			+ "   , g.PlanGreigeDate \r\n";  
	private String selectOP = 
			  "     a.SaleOrder \r\n"
			+ "	  , CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"  
			+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
			+ "			END AS [SaleLine] \r\n"
			+ "   , CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" 
			+ "    		 WHEN 0 THEN ''   \r\n"
			+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+a.[SaleFullName]\r\n"
			+ "    		 END AS [SaleFullName]   \r\n" 
			+ "   , a.DesignFG\r\n" 
			+ "   , a.ArticleFG\r\n" 
			+ "   , a.DistChannel\r\n"
			+ "   , a.Color\r\n" 
			+ "   , a.ColorCustomer\r\n"
			+ "   , a.SaleQuantity\r\n"
			+ "   , a.RemainQuantity\r\n" 
			+ "   , a.SaleUnit\r\n"  
			+ "   , b.ProductionOrder\r\n"
			+ "   , b.TotalQuantity \r\n"     
			+ "	  , a.UserStatus\r\n"
			+ "   , b.LabStatus\r\n"
			+ "   , a.DueDate\r\n"  
			+ "   , a.GreigeInDate \r\n"
			+ "   , a.[DyePlan]  \r\n	"
			+ "   , a.[DyeActual] \r\n"
			+ "   , a.[Dryer]	 \r\n"
			+ "   , a.[Finishing] \r\n"
			+ "   , a.[Inspectation]  \r\n"
			+ "   , a.[Prepare] \r\n"
			+ "   , a.[Preset] \r\n  "
			+ "   , a.[Relax] \r\n  " 
			+ "   , a.[CFMDateActual]\r\n"  
			+ "   , a.[CFMPlanDate] \r\n "
			+ "   , a.[LotShipping] \r\n"
			+ "   , a.[DyeStatus]  \r\n"
			+ "   , h.DeliveryDate \r\n"  
			+ "   , b.LabNo\r\n"
			+ "   , a.CustomerName\r\n"
			+ "   , a.CustomerShortName\r\n"
			+ "   , a.SaleNumber\r\n" 
			+ "   , a.SaleCreateDate\r\n"
			+ "   , b.PrdCreateDate\r\n" 
			+ "   , a.MaterialNo\r\n"
			+ "   , a.DeliveryStatus\r\n"
			+ "   , a.SaleStatus\r\n" 
			+ "   , [LotNo] \r\n"
			+ "   , a.ShipDate \r\n"
			+ "   , SendCFMCusDate\r\n" ;  
	private String selectMainV2 = 
			  "    b.SaleOrder \r\n"
			+ "	  , CASE PATINDEX('%[^0 ]%', b.[SaleLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"  
			+ "			ELSE SUBSTRING(b.[SaleLine] , PATINDEX('%[^0 ]%', b.[SaleLine]  + ' '), LEN(b.[SaleLine] ) )\r\n"
			+ "			END AS [SaleLine] \r\n"
			+ "   , CASE PATINDEX('%[^0 ]%', b.[SaleNumber]  + ' ‘') \r\n" 
			+ "    		 WHEN 0 THEN ''   \r\n"
			+ "    		 ELSE SUBSTRING(b.[SaleNumber] , 5, LEN(b.[SaleNumber])) +':'+b.[SaleFullName]\r\n"
			+ "    		 END AS [SaleFullName]   \r\n" 
			+ "   , b.DesignFG\r\n" 
			+ "   , b.ArticleFG\r\n" 
			+ "   , b.DistChannel\r\n"
			+ "   , b.Color\r\n" 
			+ "   , b.ColorCustomer\r\n"
			+ "   , b.SaleQuantity\r\n"
			+ "   , b.RemainQuantity\r\n"
//			+ "	  ,( a.SaleQuantity - a.RemainQuantity ) as BillQuantity\r\n" 
			+ "   , b.SaleUnit\r\n"  
			+ "   , b.ProductionOrder\r\n"
			+ "   , b.TotalQuantity \r\n"     
			+ "	  ,	b.UserStatus\r\n"
			+ "   , b.LabStatus\r\n"
			+ "   , b.DueDate\r\n"  
			+ "   , b.GreigeInDate \r\n"
			+ "   , b.[DyePlan]  \r\n"
			+ "   , b.[DyeActual] \r\n"
			+ "   , b.[Dryer]	 \r\n"
			+ "   , b.[Finishing] \r\n"
			+ "   , b.[Inspectation]  \r\n"
			+ "   , b.[Prepare] \r\n"
			+ "   , b.[Preset] \r\n"
			+ "   , b.[Relax] \r\n"
			+ "   , b.[CFMDateActual]\r\n"
			+ "   , b.[CFMPlanDate] \r\n"
			+ "   , b.[LotShipping] \r\n"
			+ "   , b.[DyeStatus]  \r\n"
			+ "   , b.DeliveryDate \r\n"  
			+ "   , b.LabNo\r\n"
			+ "   , b.CustomerName\r\n"
			+ "   , b.CustomerShortName\r\n"
			+ "   , b.SaleNumber\r\n" 
			+ "   , b.SaleCreateDate\r\n"
			+ "   , b.PrdCreateDate\r\n" 
			+ "   , b.MaterialNo\r\n"
			+ "   , b.DeliveryStatus"
			+ "   , b.SaleStatus\r\n" 
			+ "   , b.[LotNo] \r\n"
			+ "   , b.ShipDate \r\n"
			+ "   ,SendCFMCusDate\r\n" ;  
	private String selectRP = 
			  "     a.SaleOrder \r\n"
			+ "	  , CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"  
			+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
			+ "			END AS [SaleLine] \r\n" 
			+ "   , CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" 
			+ "    		 WHEN 0 THEN ''   \r\n"
			+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+a.[SaleFullName]\r\n"
			+ "    		 END AS [SaleFullName]   \r\n" 
			+ "   , a.DesignFG\r\n" 
			+ "   , a.ArticleFG\r\n" 
			+ "   , a.DistChannel\r\n"
			+ "   , a.Color\r\n" 
			+ "   , a.ColorCustomer\r\n"
			+ "   , a.SaleQuantity\r\n"
			+ "   , a.RemainQuantity\r\n" 
			+ "   , a.SaleUnit\r\n"  
			+ "   , b.ProductionOrder\r\n"
			+ "   , b.TotalQuantity \r\n"     
			+ "	  ,	UCALRP.UserStatusCalRP as UserStatus\r\n"
			+ "   , b.LabStatus\r\n"
			+ "   , a.DueDate\r\n"  
			+ "   , g.GreigeInDate \r\n"
			+ "   , g.DyePlan  \r\n	"
			+ "   , g.DyeActual \r\n"
			+ "   , g.Dryer \r\n"
			+ "   , g.Finishing \r\n"
			+ "   , g.Inspectation  \r\n"
			+ "   , g.Prepare \r\n"
			+ "   , g.Preset \r\n  "
			+ "   , g.Relax \r\n  " 
			+ "   , g.CFMDateActual\r\n"  
			+ "   , g.CFMPlanDate \r\n "
			+ "   , g.LotShipping \r\n"
  			+ "   , g.DyeStatus  \r\n" 
			+ "   , h.DeliveryDate \r\n" 
			+ "   , b.LabNo\r\n"
			+ "   , a.CustomerName\r\n"
			+ "   , a.CustomerShortName\r\n"
			+ "   , a.SaleNumber\r\n" 
			+ "   , a.SaleCreateDate"
			+ "   , b.PrdCreateDate\r\n" 
			+ "   , a.MaterialNo \r\n"
			+ "   , a.DeliveryStatus"
			+ "   , a.SaleStatus\r\n" 
			+ "   , b.[LotNo] \r\n"
			+ "   , a.ShipDate \r\n"
  		    + "   , CASE \r\n"
  		    + "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
  		    + "    	ELSE  g.SendCFMCusDate \r\n"
  		    + "    	END AS SendCFMCusDate\r\n"
			+ "   , g.PlanGreigeDate \r\n" ;    
	private String selectAll = 
			    "  SaleOrder \r\n"
			  + ", [SaleLine] \r\n"
			  + ", [SaleFullName]   \r\n"
			  + ", DesignFG\r\n"
			  + ", ArticleFG\r\n"
			  + ", DistChannel\r\n"
			  + ", Color\r\n"
			  + ", ColorCustomer\r\n"
			  + ", SaleQuantity\r\n"
			  + ", RemainQuantity\r\n"
			  + ", SaleUnit\r\n"
			  + ", ProductionOrder\r\n"
			  + ", TotalQuantity \r\n"
			  + ", UserStatus\r\n"
			  + ", LabStatus\r\n"
			  + ", DueDate\r\n"
			  + ", GreigeInDate \r\n"
			  + ", [DyePlan]  \r\n"
			  + ", [DyeActual] \r\n"
			  + ", [Dryer]	 \r\n"
			  + ", [Finishing] \r\n"
			  + ", [Inspectation]  \r\n"
			  + ", [Prepare] \r\n"
			  + ", [Preset] \r\n"
			  + ", [Relax] \r\n"
			  + ", [CFMDateActual]\r\n"
			  + ", [CFMPlanDate] \r\n"
			  + ", [LotShipping] \r\n"
			  + ", [DyeStatus]  \r\n"
			  + ", DeliveryDate \r\n"
			  + ", LabNo\r\n"
			  + ", CustomerName\r\n"
			  + ", CustomerShortName\r\n"
			  + ", SaleNumber\r\n"
			  + ", SaleCreateDate\r\n"
			  + ", PrdCreateDate\r\n"
			  + ", MaterialNo\r\n"
			  + ", DeliveryStatus   \r\n"
			  + ", SaleStatus\r\n"
			  + ", [LotNo] \r\n"
			  + ", ShipDate \r\n"
			  + ", SendCFMCusDate\r\n"
			  + ", TypePrd \r\n"
			  + ", TypePrdRemark \r\n"
			  + ", [PurchaseOrder]     \r\n"  
  		      + " , a.[PlanGreigeDate]\r\n"; 
	private String selectWaitLot = 
	  		      "     a.SaleOrder 		\r\n"
	    		+ "   , CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
	  		    + "			WHEN 0 THEN ''  \r\n"
	  		    + "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
	  		    + "			END AS [SaleLine]\r\n"
	  		    + "   , CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n"
	  		    + "    		WHEN 0 THEN ''   \r\n"
	  		    + "    		ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+a.[SaleFullName]\r\n"
	  		    + "    		END AS [SaleFullName]   \r\n"
	  		    + "   , a.DesignFG\r\n"
	  		    + "   , a.ArticleFG\r\n"
	  		    + "   , a.DistChannel\r\n"
	  		    + "   , a.Color\r\n"
	  		    + "   , a.ColorCustomer\r\n"
	  		    + "   , a.SaleQuantity\r\n"
	  		    + "   , a.RemainQuantity\r\n"
	  		    + "   , a.SaleUnit\r\n"
	  		    + "   , b.ProductionOrder   \r\n"
	  		    + "   , TotalQuantity \r\n"
	  		    + "   , UserStatus\r\n"
	  		    + "   , LabStatus\r\n"
	  		    + "   , a.DueDate\r\n"
	  		    + "   , GreigeInDate \r\n"
	  		    + "   , DyePlan  \r\n"
	  		    + "   , DyeActual \r\n"
	  		    + "   , Dryer \r\n"
	  		    + "   , Finishing \r\n"
	  		    + "   , Inspectation  \r\n"
	  		    + "   , Prepare \r\n"
	  		    + "   , Preset \r\n"
	  		    + "   , Relax \r\n"
	  		    + "   , CFMDateActual\r\n"
	  		    + "   , CFMPlanDate \r\n"
	  		    + "   , LotShipping \r\n" 
	  		    + "   , DyeStatus  \r\n"
	  		    + "   , h.DeliveryDate \r\n"
	  		    + "   , LabNo\r\n"
	  		    + "	  , a.CustomerName\r\n"
	  		    + "   , a.CustomerShortName\r\n"
	  		    + "   , a.SaleNumber\r\n"
	  		    + "   , a.SaleCreateDate\r\n"
	  		    + "   , PrdCreateDate\r\n"
	  		    + "   , a.MaterialNo\r\n"
	  		    + "   , a.DeliveryStatus\r\n"
	  		    + "   , a.SaleStatus\r\n"
	  		    + "   , [LotNo] \r\n"
	  		    + "   , a.ShipDate  \r\n"
	  		    + "   , SendCFMCusDate\r\n"
	  		    + "   , 'WaitLot' as TypePrd \r\n"
	  		    + "   , 'WaitLot' AS TypePrdRemark    \r\n"
	  		    + "   ,a.[PurchaseOrder] \r\n"
	  		    + "   , a.[PlanGreigeDate]\r\n"; 
	private String selectTwo =    
		      "    b.[ProductionOrder]\r\n"
			+ "   ,ColorCustomer \r\n"
		    + "   ,[LotNo],[Batch],[LabNo]\r\n"
			+ "	  ,[PrdCreateDate],b.[DueDate],b.[SaleOrder]\r\n"
			+ "	  ,CASE PATINDEX('%[^0 ]%', b.[SaleLine]  + ' ‘')\r\n" 
			+ "			WHEN 0 THEN ''  \r\n"
			+ "			ELSE SUBSTRING(b.[SaleLine] , PATINDEX('%[^0 ]%', b.[SaleLine]  + ' '), LEN(b.[SaleLine] ) )\r\n"
			+ "			END AS [SaleLine] \r\n"
			+ "	  ,PurchaseOrder,b.ArticleFG,b.DesignFG\r\n"
			+ "	  ,CustomerName,CustomerShortName,Shade,BookNo,Center\r\n"
			+ "	  ,MaterialNo,Volumn,SaleUnit,Unit as STDUnit\r\n" 
			+ "	  ,Color,g.PlanGreigeDate,RefPrd,b.GreigeInDate\r\n"
			+ "	  ,BCAware,OrderPuang,UserStatus,LabStatus\r\n" 
			+ "   , b.CFMPlanDate AS CFMPlanDate \r\n" 
			+ "   ,CASE  \r\n"
			+ "		  WHEN b.DeliveryDate is not null THEN b.DeliveryDate  \r\n"  
			+ "		  ELSE b.CFTYPE  \r\n"
			+ "		  END AS DeliveryDate \r\n"
			+ "   ,BCDate,RemarkOne\r\n"   
			+ "	  ,RemarkTwo,RemarkThree,RemAfterCloseOne,RemAfterCloseTwo\r\n"
			+ "	  ,RemAfterCloseThree \r\n" 
			+ "   ,GreigeArticle \r\n " 
			+ "   ,GreigeDesign \r\n"
  		    + "   ,b.[PurchaseOrder] \r\n"; 
	private String selectPO = 
			  "    [ProductionOrder],[RollNo],[QuantityKG]\r\n"
			+ "   ,[QuantityMR],[POCreatedate] ,[PurchaseOrder]\r\n"
			+ "   ,CASE PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"    
			+ "		 	ELSE SUBSTRING( [PurchaseOrderLine] , PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' '), LEN( [PurchaseOrderLine] ) )\r\n"
			+ "       	END AS [PurchaseOrderLine] \r\n"      
			+ "   ,[RequiredDate],[PurchaseOrderDate],[PODefault]\r\n"
			+ "   ,[POLineDefault],[POPostingDateDefault],a.[DataStatus] \r\n";

	   private String leftJoinFSMBBTempSumBill = "" 
	    		+ " left join #tempSumBill  \r\n"
	    		+ "						AS FSMBB ON b.[ProductionOrder] = FSMBB.[ProductionOrder] \r\n"
	    		+ "							    AND FSMBB.Grade = M.Grade \r\n" 
	    		+ "							    AND FSMBB.SaleOrder = a.SaleOrder \r\n"
	    		+ "							    AND FSMBB.SaleLine = a.SaleLine\r\n";
	 private String leftJoinSCC = ""
	    		+ " left join [PCMS].[dbo].[PlanSendCFMCusDate] as SCC on b.ProductionOrder = SCC.ProductionOrder and SCC.DataStatus = 'O'    \r\n";
	private String createTempSumGR = 
			  " If(OBJECT_ID('tempdb..#tempSumGR') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempSumGR\r\n"
			+ "	end ;\r\n"
			+ " SELECT distinct [ProductionOrder] ,[Grade] ,[PriceSTD]\r\n"
			+ "				     ,sum([QuantityMR]) as GRSumMR\r\n"      
			+ "				     ,sum([QuantityKG]) as GRSumKG\r\n"
			+ "				     ,sum([QuantityYD]) as GRSumYD\r\n "
			+ " into #tempSumGR \r\n" 
			+ " FROM [PCMS].[dbo].[FromSapGoodReceive]\r\n"
			+ "	where datastatus = 'O'\r\n"   
			+ "	GROUP BY ProductionOrder,Grade ,[PriceSTD] \r\n"  ;
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
			  + "				 		,[PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG] \r\n"
				+ "                     , g.PlanGreigeDate \r\n"
			  + "               into #tempMainPrdTemp\r\n"
			  + "				from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			  + "				left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
			  + "			   	left join ( SELECT distinct [SaleOrder],[SaleLine] ,1 as [CheckBill] \r\n"
			  + "							FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			  + "							where DataStatus = 'O'\r\n"
			  + "						   	group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine]  \r\n"
			  + " 				left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n" ;

	private String leftJoinTempG = 
			" left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"  ;   
	private String leftJoinM =   
			" left join #tempSumGR as m on b.ProductionOrder = m.ProductionOrder \r\n";
	private String leftJoinUCAL = "    "
			+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] " 
			+ " as UCAL on UCAL.[DataStatus] = 'O' AND b.ProductionOrder = UCAL.ProductionOrder AND ( m.Grade = UCAL.Grade OR m.Grade IS NULL )  \r\n"; 
	private String leftJoinUCALRP = "    "
			+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCALRP on \r\n" 
			+ " 						UCALRP.[DataStatus] = 'O' AND  \r\n"
			+ "							b.ProductionOrder = UCALRP.ProductionOrder AND \r\n" 
			+ "							(  m.Grade = UCALRP.Grade OR m.Grade IS NULL )    \r\n"; 
	private String innerJoinWaitLotB =    
   		   " INNER JOIN (\r\n"
   		 + "	SELECT DISTINCT a.saleorder , a.saleline, c.SumVolMain ,b.SumVolUsed\r\n"
   		 + "		, CASE  \r\n"
   		 + " 			WHEN ISNULL(c.SumVolMain, 0 ) >  b.SumVolUsed THEN 'A'\r\n"
   		 + "			WHEN ISNULL(c.SumVolMain, 0 ) <=  b.SumVolUsed THEN 'B' \r\n"
   		 + "			ELSE  'C'\r\n"
   		 + "	 		END AS SumVol \r\n"
   		 + "		, 'รอจัด Lot' as ProductionOrder\r\n"
   		 + "		, CASE  \r\n"
   		 + "			WHEN ISNULL( SumVolOP, 0 ) >  0 THEN 'พ่วงแล้วรอสวม'\r\n"
   		 + "			WHEN ISNULL( SumVolRP, 0 ) >  0 THEN 'รอสวมเคยมี Lot'\r\n"
   		 + "			ELSE  'รอจัด Lot'\r\n"
   		 + "	 		END AS LotNo  \r\n"
   		 + "		, SumVolOP\r\n"
   		 + "		, SumVolRP\r\n"
   		 + "		, CountProdRP\r\n" 
   		 + "        ,  cast(null as decimal) as TotalQuantity \r\n"
   		 + "		, cast(null as varchar) as Grade \r\n"
   		 + "		,  cast(null as decimal) as BillSendWeightQuantity \r\n"
   		 + "		,  cast(null as decimal) as BillSendQuantity  \r\n"
   		 + "		,  cast(null as decimal) as BillSendMRQuantity \r\n"
   		 + "		,  cast(null as decimal) as BillSendYDQuantity  \r\n"
   		 + "		, cast(null as varchar) as LabNo\r\n"
   		 + "		, cast(null as varchar) as LabStatus\r\n"
   		 + "		, cast(null as date) as CFMPlanLabDate\r\n"
   		 + "		, cast(null as date) as CFMActualLabDate \r\n"
   		 + "		, cast(null as date) as CFMCusAnsLabDate \r\n"
   		 + "		, cast(null as varchar) as UserStatus \r\n"
   		 + "		, cast(null as date) as TKCFM \r\n"
   		 + "		, cast(null as date) as CFMPlanDate \r\n"
   		 + "		, cast(null as date) as DeliveryDate  \r\n"
   		 + "		, cast(null as date) as CFMSendDate \r\n"
   		 + "		, cast(null as date) as CFMAnswerDate \r\n"
   		 + "		, cast(null as varchar) as CFMStatus \r\n"
   		 + "		, cast(null as varchar) as CFMNumber  \r\n"
   		 + "		, cast(null as varchar) as CFMRemark  \r\n"
   		 + "		, cast(null as varchar) as RemarkOne \r\n"
   		 + "		, cast(null as varchar) as RemarkTwo \r\n"
   		 + "		, cast(null as varchar) as RemarkThree  \r\n"
   		 + "		, cast(null as varchar) as StockRemark \r\n"
   		 + "		,  cast(null as decimal) as GRSumKG \r\n"
   		 + "		,  cast(null as decimal) as GRSumYD \r\n"
   		 + "		,  cast(null as decimal) as GRSumMR \r\n"
   		 + "		, cast(null as date) as DyePlan \r\n"
   		 + "		, cast(null as date) as DyeActual   \r\n"
   		 + "		, '' as [SwitchRemark] \r\n"
   		 + "		, cast(null as date) as [PrdCreateDate]\r\n"
   		 + "		,  cast(null as decimal) AS Volumn   \r\n"
   		 + "		,  cast(null as decimal) AS VolumnFGAmount  	\r\n"   
		 + "		, cast(null as date) as GreigeInDate \r\n" 
		 + "		, cast(null as date) as Dryer \r\n"
		 + "		, cast(null as date) as Finishing \r\n"
		 + "		, cast(null as date) as Inspectation  \r\n"
		 + "		, cast(null as date) as Prepare \r\n"
		 + "		, cast(null as date) as Preset \r\n"
		 + "		, cast(null as date) as Relax \r\n"
		 + "		, cast(null as date) as CFMDateActual\r\n" 
		 + "		, cast(null as varchar) as DyeStatus  \r\n"
		 + "		, cast(null as date) as SendCFMCusDate\r\n"
	     + "        , cast(null as date) AS LotShipping \r\n"   
		 + "		, cast(null as date) as PlanGreigeDate \r\n" 
   		 + "	from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
   		 + "	left join ( SELECT DISTINCT A.SaleOrder , A.SaleLine ,ISNULL(SumVolOP, 0 ) + ISNULL(SumVolRP, 0 ) as SumVolUsed --,ISNULL(SumVolRP, 0 )\r\n"
   		 + "				               , SumVolOP , SumVolRP\r\n"
   		 + "				FROM[PCMS].[dbo].[FromSapMainProd]  AS A\r\n"
   		 + "				LEFT JOIN (  \r\n"
   		 + "						SELECT DISTINCT a.[ProductionOrder] \r\n"
   		 + "									   ,sum(A.[Volumn]) as SumVolOP \r\n"
   		 + "						FROM [PCMS].[dbo].[FromSapMainProdSale] AS A \r\n"
   		 + "						left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
   		 + "						WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"
   		 + "						GROUP BY A.[ProductionOrder] ) AS D ON D.ProductionOrder = A.ProductionOrder\r\n"
   		 + "				LEFT JOIN (   \r\n"
   		 + "						  SELECT a.ProductionOrderRP , sum(c.Volume) as SumVolRP  \r\n"
   		 + "						  from [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
   		 + "						  left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
   		 + "						  left join ( SELECT ProductionOrderRP  \r\n"
   		 + "											, CASE  \r\n"
   		 + "												WHEN ( Volume = 0 ) THEN b.Volumn  \r\n"
   		 + "												ELSE a.Volume\r\n"
   		 + "												END AS Volume  \r\n"
   		 + "									  from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
   		 + "									  left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
   		 + "									  WHERE a.[DataStatus] = 'O' ) as c on a.ProductionOrderRP = c.ProductionOrderRP\r\n"
   		 + "						  WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z')  \r\n"
   		 + "						  group by a.ProductionOrderRP\r\n"
   		 + "						  ) AS E ON A.ProductionOrder = E.ProductionOrderRP	 \r\n"
   		 + "				)  as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n"
   		 + "	LEFT JOIN ( \r\n"
   		 + "				SELECT [SaleOrder] ,[SaleLine] ,sum( [Volumn] ) as SumVolMain \r\n"
   		 + "				FROM [PCMS].[dbo].[FromSapMainProd] as a\r\n"
   		 + "				WHERE a.DataStatus = 'O'\r\n"
   		 + "				group by  [SaleOrder]  ,[SaleLine] ) AS C ON A.SaleOrder = C.SaleOrder AND A.SaleLine = C.SaleLine \r\n"
   		 + "	left join ( select DISTINCT SaleOrder ,SaleLine ,1 AS CountProdRP\r\n"
   		 + "				from [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
   		 + "				where a.DataStatus = 'O' and  ProductionOrder = 'รอจัด Lot' \r\n"
   		 + "				GROUP BY  SaleOrder ,SaleLine \r\n"
   		 + "				) AS D ON A.SaleOrder = D.SaleOrder AND A.SaleLine = D.SaleLine  \r\n"
   		 + "	WHERE ( c.SumVolMain > 0 ) OR D.SaleOrder IS NOT NULL\r\n"
   		 + " ) AS B ON A.SaleOrder = B.SaleOrder AND A.SaleLine = B.SaleLine \r\n" ; 
	 private String leftJoinBPartOneT =  ""
	  + "			left join ( SELECT a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
	  + "                       from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
	  + "						left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
	  + "			            WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z')\r\n"
	  + "			            group by a.ProductionOrder) as t on b.ProductionOrder = t.ProductionOrder   \r\n";
	  private String leftJoinBPartOneS =  ""
	  + "           left join ( SELECT a.ProductionOrderRP , sum(c.Volume) as SumVolRP  \r\n"
	  + "    					from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
	  + "						left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
	  + "						left join ( SELECT ProductionOrderRP  \r\n"
	  + "										 , CASE  \r\n"
	  + "										   		WHEN ( Volume = 0 ) THEN b.Volumn  \r\n"
	  + "										   		ELSE a.Volume\r\n"
	  + "										   		END AS Volume  \r\n"
	  + "									from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
	  + "									left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
	  + "									WHERE a.[DataStatus] = 'O' ) as c on a.ProductionOrderRP = c.ProductionOrderRP\r\n"
	  + "						WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"
	  + "						group by a.ProductionOrderRP) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n" ;
	 private String leftJoinBPartOneH =  ""
	  + "           left join #tempPlandeliveryDate as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n";

		private String createTempPlanDeliveryDate = 
				  "  If(OBJECT_ID('tempdb..#tempPlandeliveryDate') Is Not Null)\r\n"
				  + "	begin\r\n"
				  + "		Drop Table #tempPlandeliveryDate\r\n"
				  + "	end ; \r\n"
				  + "SELECT distinct  a.id,a.[ProductionOrder] ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
				  + "into #tempPlandeliveryDate\r\n"
				  + "FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
				  + "inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max(Id) as maxId\r\n"
				  + "			FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
				  + "			group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b on a.Id = b.maxId  \r\n"  ;   
	 private String leftJoinBSelect =  ""
			  + "             a.[SaleOrder] , a.[Saleline]  ,[TotalQuantity] ,[Unit]\r\n"
			  + "			 , [RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
			  + "			 , a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
			  + "			 , [Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
			  + "			 , [OrderPuang] ,[RefPrd],b.[GreigeInDate] ,[BCDate],b.[Volumn]\r\n"
			  + "			 , [CFdate],[CFType],[Shade],b.[LotShipping],[BillSendQuantity],m.[Grade] \r\n"
			  + "			 , [PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG]\r\n"
			  + "			 , b.[ProductionOrder] ,b.[LotNo] \r\n"   
			  + " 			 , CASE  \r\n"
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
			  + "             , s.SumVolRP,t.SumVolOP,b.Volumn as RealVolumn\r\n"
			  + "             , g.[DyePlan]  \r\n"
			  + "	          , g.[DyeActual] , g.[Dryer] , g.[Finishing] , g.[Inspectation]  \r\n"
			  + "             , g.[Prepare] , g.[Preset] , g.[Relax] , g.[CFMDateActual] , g.[CFMPlanDate]  \r\n"
			  + "             , g.[DyeStatus] , h.DeliveryDate, 	UCAL.UserStatusCal as UserStatus \r\n"
			  + " 		  		, CASE \r\n"
			  + "					WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
			  + "    			  	ELSE  g.SendCFMCusDate \r\n"     
			  + "    				END AS SendCFMCusDate\r\n"
			  + "             , GRSumKG, GRSumYD, GRSumMR \r\n" 
				+ "          , g.PlanGreigeDate \r\n" ; 
	private String leftJoinH = 
			"           left join #tempPlandeliveryDate as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n"
		  ;   
 	private String leftJoinCSW = 
			    " LEFT JOIN ( SELECT [SaleOrderSW] ,[SaleLineSW] ,1 as countSW \r\n"
			  + "			  FROM [PCMS].[dbo].[SwitchProdOrder] as a\r\n"
			  + "		      left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "			  WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' or b.UserStatus <> 'ตัดเกรด Z')\r\n"
			  + "			  GROUP BY  [SaleOrderSW] ,[SaleLineSW]\r\n"
			  + " ) as CSW on  CSW.[SaleOrderSW] = b.SaleOrder and CSW.[SaleLineSW] = b.SaleLine\r\n" ;
	private String leftJoinCRP = 
			   " LEFT JOIN ( SELECT a.[SaleOrder] ,a.[SaleLine]  ,1 as countnRP  \r\n"
			 + "			 FROM [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
			 + "			 left join [PCMS].[dbo].[FromSapMainProd] as b on  a.[ProductionOrderRP] = b.ProductionOrder  \r\n"
			 + "			 WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' or b.UserStatus <> 'ตัดเกรด Z')\r\n"
			 + "			 GROUP BY  a.[SaleOrder] ,a.[SaleLine]\r\n"
			 + " )  as CRP on  CRP.SaleOrder = b.SaleOrder and CRP.SaleLine = b.SaleLine\r\n" ;
	private String leftJoinCOP = 
			    " LEFT JOIN ( SELECT a.[SaleOrder] ,a.[SaleLine] ,1 as countnOP\r\n"
			  + "			  FROM [PCMS].[dbo].[FromSapMainProdSale] as a\r\n"
			  + "			  left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "			  WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' or b.UserStatus <> 'ตัดเกรด Z')\r\n"
			  + "			  GROUP BY  a.[SaleOrder] ,a.[SaleLine]  \r\n"
			  + " ) as COP on  COP.SaleOrder = b.SaleOrder and COP.SaleLine = b.SaleLine  \r\n";
 	private String leftJoinR = 
	  		    " left join (select ProductionOrder , ProductionOrderSW\r\n"
	  		  + "		     FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
	  		  + "		     WHERE DataStatus = 'O'\r\n"
	  		  + " ) as R on b.ProductionOrder = R.ProductionOrderSW  \r\n";  
	private String selectPreset = 
			  "      [ProductionOrder],[PostingDate],[WorkCenter]\r\n"
			+ "      ,[Operation],[No],[DataStatus] \r\n";
	private String selectDyeing = "[ProductionOrder],[PostingDate],[Operation]\r\n"
			+ "      ,[WorkCenter],[DyeStatus],[Remark],[ReDye]\r\n"  
			+ "      ,[RollNo],[Da],[Db],[L],[ST]\r\n"
			+ "      ,[ColorStatus],[ColorRemark],[DeltaE],[No]\r\n"
			+ "      ,[DataStatus]\r\n ";
	private String selectPacking = 
			  "      [ProductionOrder],[PostingDate],[Quantity]\r\n"
			+ "      ,[RollNo],[Status],[QuantityKG],[Grade]\r\n"
			+ "      ,[No],[DataStatus],[QuantityYD]\r\n ";
	private String selectInspect = 
			  "      [ProductionOrder],[PostingDate],[QuantityGreige]\r\n"
			+ "      ,[Operation],[QuantityFG],[Remark],[No]\r\n"
			+ "      ,[DataStatus]\r\n ";
	private String selectFinishing = 
			  "      [ProductionOrder],[No],[PostingDate]\r\n"
			+ "      ,[WorkCenter],[Status],[NCDate],[Cause]\r\n"
			+ "      ,[CarNo],[DeltaE],[Color],[Operation]\r\n"
			+ "      ,[CCStatus],[CCRemark],[RollNo],[Da]\r\n"
			+ "      ,[Db],[L],[ST],[CCPostingDate]\r\n"
			+ "      ,[CCOperation],[LotNo],[DataStatus]\r\n ";
	private String selectSendTestQC = 
			  "      [ProductionOrder],[No],[SendDate]\r\n"
			+ "      ,[CheckColorDate],[RollNo],[Status],[DeltaE]\r\n"
			+ "      ,[Color],[Remark],[DataStatus]\r\n ";

	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public PCMSMainDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSTableDetail> list = null;    
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
		po = bean.getPurchaseOrder();
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();
		dueDate = bean.getDueDate();
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel(); 
		cusDiv = bean.getCustomerDivision();
		List<String> userStatusList = bean.getUserStatusList();
		List<String> cusNameList = bean.getCustomerNameList();
		List<String> cusShortNameList = bean.getCustomerShortNameList();
		List<String> divisionList = bean.getDivisionList();

		String whereSale = " where ( a.DataStatus = 'O' or a.DataStatus is null ) \r\n";  
		where +=    " MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n"; 
		whereWaitLot +=    " MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n";   
		;
		if (!saleOrder.equals("")) {    
			whereSale +=  " and a.SaleOrder like '" + saleOrder + "%' \r\n"; ;
		} 
		if (!saleCreateDate.equals("")) {   
			String[] dateArray = saleCreateDate.split("-");
			where += "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
			whereWaitLot +=  "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <	= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
			whereSale +=  "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
		} 
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
		if(!po.equals("")) { 
			where += " and [PurchaseOrder] like '"+po+"%' \r\n" ; 
			whereSale += " and [PurchaseOrder] like '"+po+"%' \r\n" ;  
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
			for (int i = 0; i < cusShortNameList.size(); i++) {
				listString.add("'"+cusShortNameList.get(i).replaceAll("'","''")+"' "); 
			} 
			tmpWhere += " and ( CustomerShortName IN ( \r\n" ;  
			tmpWhere += String.join(",",  listString  );
			tmpWhere += " ) ) \r\n" ;   
			
			where += tmpWhere;
			whereSale += tmpWhere; 
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
			whereSale += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
		}
		if (!deliveryStatus.equals("")) {
			where += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n"; 
			whereSale += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n";
		}
		if (!saleStatus.equals("")) {
			where += " and SaleStatus like '" + saleStatus + "%'  \r\n";
			whereSale += " and SaleStatus like '" + saleStatus + "%'  \r\n";
//			whereTempUCAL += " and SaleStatus like '" + saleStatus + "%'  \r\n";
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
		 
		whereWaitLot = where ;
//		whereBMain = where;
		whereCaseTry = whereProd;

		String whereCaseTryRP = ""+ whereProd;
		int sizeWaitForTest = 0;
		if (userStatusList.size() > 0) { 
			String tmpWhere = "";
			tmpWhereNoLotUCAL = " and ( b.ProductionOrder is not null and ( \r\n";  
			tmpWhere += " and ( b.ProductionOrder is not null and ( \r\n";  
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
//					listUserStatus.add(" b.LotNo = '" +text + "' "); 
					listUserStatus.add("'"+text.replaceAll("'","''")+"' "); 
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
			sizeWaitForTest = listUserStatus.size();
			if(sizeWaitForTest > 0) { 
//				whereWaitLot += " and ( \r\n" ; 
//				for ( int c = 0 ; c < sizeWaitForTest;c++) {
//					String str = listUserStatus.get(c);
//					whereWaitLot += str;
//					if (c != listUserStatus.size() - 1) {
//						whereWaitLot += " or ";    
//					} ;
//				}
//				whereWaitLot += " ) \r\n" ;  
				whereWaitLot += " and ( b.LotNo IN ( \r\n" ;  
				whereWaitLot += String.join(",",  listUserStatus  );
				whereWaitLot += " ) ) \r\n" ;       
			}   
			else {
				whereWaitLot += " and ( b.UserStatus is not null) \r\n" ;   
			}
//			System.out.println(sizeWaitForTest);
//			System.out.println(whereWaitLot);
			tmpWhere += ") 		) \r\n";  
			whereCaseTryRP += ") 		) \r\n";   
			whereCaseTry += ") 		) \r\n";   
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
				+ this.createTempMainSale
				+ whereSale;
		String sqlWaitLot =    
				  " SELECT DISTINCT  \r\n" 
				+ this.selectWaitLot 	    
	  		    + " INTO #tempWaitLot  \r\n"
				+ " FROM #tempMainSale as a \r\n " 
				+ this.innerJoinWaitLotB 
				+ this.leftJoinH   
				+ whereWaitLot  
				+ " and ( SumVol = 'B' OR countProdRP > 0 ) \r\n";   
		String fromMainB = ""
				  +	" from ( SELECT distinct \r\n"
				  + this.leftJoinBSelect
				  + "				,Division,CustomerShortName,SaleCreateDate,PurchaseOrder,MaterialNo,CustomerMaterial,Price,SaleUnit\r\n"
				  + "				,OrderAmount,SaleQuantity,RemainQuantity,RemainAmount,CustomerDue,DueDate,ShipDate,CustomerType\r\n"
				  + "				,[SaleNumber],[SaleFullName],DistChannel,Color,ColorCustomer,CustomerName,DeliveryStatus,SaleStatus \r\n"
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
		String sqlMain = "SELECT DISTINCT \r\n "  
				+ this.selectMainV2	 
				+ "   , 'Main' as TypePrd \r\n"
				+ "   , 'Main' AS TypePrdRemark \r\n"   
	  		    + "   , [PurchaseOrder] \r\n"
	  		    + "   , b.PlanGreigeDate\r\n"
	  		    + " INTO #tempMain  \r\n" 
				+ fromMainB 
				+ this.leftJoinCSW  
				+ this.leftJoinCRP
				+ this.leftJoinCOP
				+ " where (\r\n"
				+ "			 b.SumVol >= 0 or\r\n" //20230911 FIX HERE
//				+ "			 b.SumVol <> 0 or\r\n" //20230911 FIX HERE
				+ "			 ( \r\n"
				+ "				(\r\n"
				+ "					 b.LotNo = 'รอจัด Lot'  or  b.LotNo = 'ขาย stock' or b.LotNo = 'รับจ้างถัก' or b.LotNo = 'Lot ขายแล้ว' \r\n"
				+ "					or b.LotNo = 'พ่วงแล้วรอสวม' or b.LotNo = 'รอสวมเคยมี Lot'  \r\n"
				+ "				)  and b.SumVol = 0 \r\n" 
				+ "		     	and ( countnRP is null )  \r\n"
				+ "          ) or\r\n" 
				+ "     	 RealVolumn = 0 or\r\n"
				+ "     	 ( b.UserStatus = 'ยกเลิก' or b.UserStatus = 'ตัดเกรด Z') \r\n"
				+ "      )\r\n"        
				+ " and NOT EXISTS ( select distinct ProductionOrderSW\r\n"
				+ "				   FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA\r\n"
				+ "				   WHERE DataStatus = 'O' and BAA.ProductionOrderSW = B.ProductionOrder\r\n"
				+ "				 )   \r\n" ;
				// Order Puang
//				+ " union ALL  "
		String createTempOPFromA =  "" 
				+ " If(OBJECT_ID('tempdb..#tempPrdOPA') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOPA\r\n"
				+ "	end ; " 
    			+ "       SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
				+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n" 
				+ "           , 'SUB' as TypePrdRemark \r\n" 
				+ "           , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ "           , CASE WHEN b.Volumn <> 0 THEN b.Volumn \r\n"
				+ "                  ELSE  0 \r\n"     
				+ "                  END AS Volumn  \r\n" 
				+ "	          , g.[DyePlan]  \r\n"
				+ "	          , g.[DyeActual] , g.[Dryer] , g.[Finishing] , g.[Inspectation]  \r\n"
				+ "           , g.[Prepare] , g.[Preset] , g.[Relax] , g.[CFMDateActual] , g.[CFMPlanDate]  \r\n"
				+ "           , g.[DyeStatus]  , UCAL.UserStatusCal as UserStatus \r\n" 
				+ " 		   , CASE \r\n"
				+ "					WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
				+ "    			  	ELSE  g.SendCFMCusDate \r\n"     
				+ "    				END AS SendCFMCusDate\r\n"
				+ "           , GRSumKG, GRSumYD, GRSumMR \r\n"
				+ "           , g.CFMDetailAll \r\n"
				+ "           , g.CFMNumberAll  \r\n"
				+ "           , g.CFMRemarkAll \r\n"
				+ "           , g.RollNoRemarkAll \r\n"
				+ "           , g.CFMActualLabDate \r\n"
				+ "           , g.CFMCusAnsLabDate \r\n"
				+ "           , g.GreigeInDate\r\n"
				+ "           , g.LotShipping \r\n"
				+ "           , g.PlanGreigeDate \r\n"
				+ "       into #tempPrdOPA\r\n"  
				+ "		  from #tempMainSale as a  \r\n"
				+ "		  inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
				+ "       "+this.leftJoinTempG   
				+ "       "+this.leftJoinSCC
				+ "       "+this.leftJoinM   
				+ "       "+this.leftJoinUCAL   
				+ "		  where b.DataStatus <> 'X'  "
				+ "         "+tmpWhereNoLotUCAL+" \r\n"
				+ "   If(OBJECT_ID('tempdb..#tempPrdOP') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOP\r\n"
				+ "	end ; \r\n "
				+  " SELECT DISTINCT \r\n  " 
				+ this.selectOP
				+ "   ,'OrderPuang' as TypePrd \r\n" 
    			+ "   ,TypePrdRemark \r\n"
	  		    + "   , [PurchaseOrder] \r\n"
	  		    + "   , a.PlanGreigeDate\r\n"
				+ " into #tempPrdOP\r\n"
    			+ " FROM #tempPrdOPA as a  \r\n "  
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n" 
				+ this.leftJoinH     ; 
		String sqlOP = ""
					+ " select \r\n"     
					+ this.selectAll 
		  		    + " INTO #tempOP  \r\n"
					+ " from #tempPrdOP as a \r\n"  
					+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรด Z') \r\n"   
					+ " and NOT EXISTS ( select distinct ProductionOrderSW \r\n"
					+ "				   FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA \r\n"
					+ "				   WHERE DataStatus = 'O' and BAA.ProductionOrderSW = A.ProductionOrder\r\n"
					+ "				 ) \r\n "
					+ whereCaseTry ; 
//				//// Order PuangSwitch
//				+ " union ALL  "

		String createTempOPSWFromA = ""
				+ " If(OBJECT_ID('tempdb..#tempPrdOPSW') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOPSW\r\n"
				+ "	end ; " 
				 	+  " SELECT DISTINCT  \r\n" 
					+ this.select  
					+ "   , 'OrderPuang' as TypePrd \r\n"  
	    			+ "   , TypePrdRemark \r\n"
		  		    + "   ,a.[PurchaseOrder] \r\n" 
		  		    + " INTO #tempPrdOPSW  \r\n"
					+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
					+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
					+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
					+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n" 
					+ "           , 'SUB' as TypePrdRemark \r\n" 
					+ "           , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
					+ "           , CASE WHEN b.Volumn <> 0 THEN b.Volumn \r\n"
					+ "                  ELSE  0 \r\n"     
					+ "                  END AS Volumn , a.[PlanGreigeDate] \r\n"
					+ "		   from #tempMainSale as a  \r\n"
					+ "		   inner join ( \r\n"
					+ "             SELECT \r\n"
					+ "					CASE \r\n"
					+ "			          	WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
					+ "			          	ELSE C.ProductionOrder\r\n"
					+ "			          	END AS [ProductionOrder]\r\n"
					+ "		           , [SaleOrder] ,[SaleLine] ,[Volumn]  ,[DataStatus]\r\n"
					+ "		        FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
					+ "		        LEFT JOIN (SELECT  [ProductionOrder] ,[ProductionOrderSW] \r\n"
					+ "					       FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
					+ "					       WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
					+ "					       AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
					+ "		        LEFT JOIN (SELECT  [ProductionOrder] \r\n"
					+ "								  ,[ProductionOrderSW] \r\n"
					+ "					       FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
					+ "					       WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
					+ "					       AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n"
					+ "				WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL)\r\n" 
					+ "       	) as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
					+ "		 	where b.DataStatus <> 'X' and b.SaleLine <> '' ) as a  \r\n "  
					+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
//					+ this.leftJoinC	  
					+ this.leftJoinTempG
					+ this.leftJoinSCC
					+ this.leftJoinH   
					+ this.leftJoinR
					+ this.leftJoinM
					+ this.leftJoinUCAL  
					+ where     
					+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"   ;
		String sqlOPSW = "" 
				+ " select \r\n"
				+ this.selectAll 
	  		    + " INTO #tempOPSW  \r\n"
				+ " from #tempPrdOPSW as a \r\n"   
				+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรด Z') \r\n"  
				+ whereCaseTry ;  
//////			// Switch   
//				+ " union ALL  "
		String createTempSWFromA = ""
				+ " If(OBJECT_ID('tempdb..#tempPrdSW') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdSW\r\n"
				+ "	end ; " 
				+ " SELECT DISTINCT \r\n " 
				+ this.select 
				+ "   ,'Switch' as TypePrd \r\n"  
    			+ "   ,TypePrdRemark \r\n"
	  		    + "   ,a.[PurchaseOrder] \r\n" 
	  		    + " INTO #tempPrdSW  \r\n"
    			+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
				+ "	          , b.ProductionOrderSW as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
				+ "  		  , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ " 		  , CASE \r\n"
				+ "					when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
				+ "					ELSE 'SUB' \r\n"
				+ "			    	END	TypePrdRemark  ,C.SumVol, a.[PlanGreigeDate] \r\n" 
				+ "		 	from #tempMainSale as a  \r\n"
				+ "		 	inner join [PCMS].[dbo].[SwitchProdOrder]  as b on a.SaleLine = b.SaleLineSW and a.SaleOrder = b.SaleOrderSW  \r\n \r\n"
				+ "		 	LEFT JOIN ( \r\n"
				+ "				SELECT PRDORDERSW ,sum([Volumn]) as SumVol\r\n"
				+ "				FROM ( SELECT A.[ProductionOrder] \r\n"
				+ "					  ,CASE \r\n"
				+ "							WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
				+ "							ELSE C.ProductionOrder\r\n"
				+ "							END AS PRDORDERSW\r\n"
				+ "					  ,[SaleOrder]\r\n"
				+ "					  ,[SaleLine]\r\n"
				+ "					  ,[Volumn]\r\n"
				+ "					  ,[DataStatus]\r\n"
				+ "				  FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
				+ "				  LEFT JOIN (SELECT  [ProductionOrder] \r\n"
				+ "									,[ProductionOrderSW] \r\n"
				+ "							  FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
				+ "							  WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
				+ "							   AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
				+ "				  LEFT JOIN (SELECT  [ProductionOrder] \r\n"
				+ "									,[ProductionOrderSW] \r\n"
				+ "							  FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
				+ "							  WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'	)\r\n"
				+ "							   AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n"
				+ "				WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL)\r\n"
				+ "				) AS A\r\n"
				+ "				group by PRDORDERSW\r\n" 
				+ "		 	) AS C ON B.ProductionOrderSW = C.PRDORDERSW \r\n"
				+ "		 	where b.DataStatus <> 'X') as a  \r\n "  
				+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n" 
				+ this.leftJoinTempG
				+ this.leftJoinSCC
				+ this.leftJoinH 
				+ this.leftJoinR
				+ this.leftJoinM
				+ this.leftJoinUCAL  
				+ where     
				+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"  ;
			String sqlSW =  "" 
					  + " select \r\n"
					  + this.selectAll 
		  		      + " INTO #tempSW  \r\n"
					  + " from #tempPrdSW as a \r\n" ;  
//////			// สวม  
//				+ " union ALL  "
			String createTempRP = ""
					+ " If(OBJECT_ID('tempdb..#tempPrdReplaced') Is Not Null)\r\n"
					+ "	begin\r\n"
					+ "		Drop Table #tempPrdReplaced\r\n"
					+ "	end ;\r\n"
					+" SELECT DISTINCT  \r\n" 
					+ this.selectRP    
					+ "   , 'Replaced' as TypePrd \r\n"
	    			+ "   , 'SUB' as TypePrdRemark \r\n"
		  		    + "   ,a.[PurchaseOrder] \r\n" 
		  		    + " INTO #tempPrdReplaced  \r\n"  
					+ " from #tempMainSale as a  \r\n"
		  		    + " inner join ( select SaleOrder , SaleLine, [ProductionOrderRP] AS ProductionOrder,DataStatus from [PCMS].[dbo].[ReplacedProdOrder] )  as rpo on a.SaleLine = rpo.SaleLine and a.SaleOrder = rpo.SaleOrder and  rpo.DataStatus <> 'X'  \r\n"  
					
//					+ " inner join [PCMS].[dbo].[ReplacedProdOrder] as rpo on a.SaleLine = rpo.SaleLine and a.SaleOrder = rpo.SaleOrder and  rpo.DataStatus <> 'X'  \r\n"  
					+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on b.ProductionOrder = rpo.ProductionOrder \r\n" 
					+ this.leftJoinTempG 
					+ this.leftJoinSCC
					+ this.leftJoinH    
					+ this.leftJoinR	
					+ this.leftJoinM	  
					+ this.leftJoinUCALRP 
					+ " where ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n" 
					+ whereCaseTryRP    ;   ;

			String sqlRP = ""  
						+ " select \r\n"  
						+ this.selectAll 
			  		    + " INTO #tempRP  \r\n"
						+ " from #tempPrdReplaced as a \r\n"    
//						+ " where ( a.UserStatus <> 'ยกเลิก' and a.UserStatus <> 'ตัดเกรด Z') \r\n" 
//						+ whereCaseTry    ;
				;    
				
			 String sql =
					 " " 
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

				+ createTempMainSale
			 	+ this.createTempPlanDeliveryDate
				+ this.createTempMainPrdFromTempA 
//			    + this.createTempMainPrdFromTempA
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
					+ " SELECT * FROM #tempWaitLot\r\n"
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
					+ " Order by CustomerShortName, DueDate, [SaleOrder], [SaleLine], [ProductionOrder] ";    
//		 System.out.println(sql);	
		List<Map<String, Object>> datas = this.database.queryList(sql);  
		list = new ArrayList<PCMSTableDetail>();  
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}   
		return list;
	}
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

	@Override
	public ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSAllDetail> list = null;
		String where = " where  ";
		String prdOrder = "" ;
		PCMSTableDetail bean = poList.get(0); 
		prdOrder = bean.getProductionOrder();      
		where += " ProductionOrder = '" + prdOrder + "' \r\n"; 
		String fromMainB = ""
				  +	" from ( SELECT distinct \r\n"
				  + this.leftJoinBSelect
				  + "				,Division,CustomerShortName,SaleCreateDate,PurchaseOrder,MaterialNo,CustomerMaterial,Price,SaleUnit\r\n"
				  + "				,OrderAmount,SaleQuantity,RemainQuantity,RemainAmount,CustomerDue,DueDate,ShipDate,CustomerType\r\n"
				  + "				,[SaleNumber],[SaleFullName],DistChannel,Color,ColorCustomer,CustomerName,DeliveryStatus,SaleStatus \r\n"
				  + "			from #tempMainSale as a\r\n"  
				  + "           inner join (\r\n"
				  + "					select * \r\n"
				  + "					from #tempMainPrdTemp\r\n"
				  + "					"+where+" \r\n"
				  + "			) as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
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
		String sql =  ""
				+ this.createTempMainSale
				+ this.createTempPlanDeliveryDate
			 	+ this.createTempSumBill
			 	+ this.createTempSumGR 
				+ this.createTempMainPrdFromTempA
				+  " SELECT distinct top 1  \r\n " 
				+ this.selectTwo 
				+ fromMainB	 
				+ this.leftJoinTempG       
				+ " Order by SaleOrder , 	SaleLine"; 	 
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
			beanTmp.setSubmitDateDetailList(submitdatDetailList);
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				  "SELECT DISTINCT  \r\n" 
				+ "   [ProductionOrder],[No],[PostingDate] ,[LotNo],[Receipe],a.[DataStatus] \r\n"
				+ " from [PCMS].[dbo].[FromSapReceipe] as a \r\n " 
				+ where 
				+ " Order by No";
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  \r\n" 
				+ "   [ProductionOrder],[SaleOrder],[SaleLine]\r\n"
				+ "   ,[No],[NCDate],[CarNo],[Remark]\r\n"
				+ "   ,[Quantity],[Unit],[NCFrom],a.[DataStatus]\r\n" 
				+ " from [PCMS].[dbo].[FromSapNC] as a \r\n " 
				+ where 
				+ " Order by No";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<NCDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genNCDetail(map));
		}
		return list;
	}

	private ArrayList<InputDateDetail> getSubmitDateDetail(ArrayList<PCMSTableDetail> poList) { 
		ArrayList<InputDateDetail> list = null;
		PCMSTableDetail bean = poList.get(0);
		String prdOrder = bean.getProductionOrder();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql =  
			" SELECT \r\n"
		  + "    	 [ProductionOrder]\r\n"
		  + "      , [SaleOrder]\r\n"
		  + "      , [SaleLine]\r\n"
		  + "      , [PlanDate]\r\n"
		  + "      , [CreateBy]\r\n"
		  + "      , [CreateDate]\r\n"
		  + "	   , '1:PCMS' as InputFrom \r\n"
		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n" 
		  + " where a.[ProductionOrder] = '" + prdOrder + "' and \r\n"
		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
		  + "       a.[SaleLine] = '" + saleLine+ "' \r\n"
		  + " union ALL  \r\n "
		  + " SELECT \r\n"
		  + "        [ProductionOrder]\r\n"
		  + "      , [SaleOrder]\r\n"
		  + "      , [SaleLine]\r\n"
		  + "      , SubmitDate as [PlanDate]\r\n"  
		  + "      , '' AS [CreateBy]\r\n"
		  + "      , null AS [CreateDate]\r\n" 
		  + "	   , '0:SAP' as InputFrom \r\n"
		  + " FROM [PCMS].[dbo].[FromSapSubmitDate]  as a\r\n" 
		  + " where a.[ProductionOrder] = '" + prdOrder + "' and SubmitDate is not null \r\n"
  		  + "   and a.[DataStatus] = 'O' \r\n"
		  + " ORDER BY InputFrom ,CreateDate ";
				
		 
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				  " SELECT DISTINCT  \r\n" 
		        + " 	[ProductionOrder],[BillDate]\r\n"
				+ "     ,[BillQtyPerSale],[SaleOrder]\r\n"
				+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "			WHEN 0 THEN ''  \r\n"
				+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "			END AS [SaleLine] \r\n"
				+ "     ,[BillQtyPerStock],[Remark],[CustomerNo]\r\n"
				+ "     ,[CustomerName1],[CustomerPO],[DueDate]\r\n"
				+ "     ,[Color],[No],a.[DataStatus]\r\n" + " "
				+ " from [PCMS].[dbo].[FromSapSaleInput] as a \r\n " 
				+ where 
				+ " Order by [No]";
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				  " SELECT DISTINCT  \r\n" 
				+ "   [ProductionOrder],[BillDate]\r\n"
				+ "   ,[BillQtyPerSale],[SaleOrder] \r\n"
				+ "	  ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "			WHEN 0 THEN ''  \r\n"
				+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "			END AS [SaleLine] \r\n"
				+ "   ,[BillQtyPerStock],[Remark],[CustomerNo]\r\n"
				+ "   ,[CustomerName1],[CustomerPO],[DueDate]\r\n"
				+ "   ,[Color],[No],a.[DataStatus]\r\n" + "  "
				+ " from [PCMS].[dbo].[FromSapSale] as a \r\n "
				+ where 
				+ " Order by [No]";
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				  " SELECT DISTINCT  \r\n" 
				+ "   [ProductionOrder],[CFMNo],[CFMNumber]\r\n"
				+ "   ,[CFMSendDate],[CFMAnswerDate],[CFMStatus]\r\n"
				+ "   ,[CFMRemark],[Da],[Db],[L]\r\n"
				+ "   ,[ST],[SaleOrder]"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "			WHEN 0 THEN ''  \r\n"
				+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "			END AS [SaleLine] "
				+ "   ,[CFMCheckLab]\r\n"
				+ "   ,[CFMNextLab],[CFMCheckLot],[CFMNextLot]\r\n"
				+ "   ,[NextLot],[SOChange],[SOChangeQty]\r\n"
				+ "   ,[SOChangeUnit],[RollNo],[RollNoRemark]\r\n"
				+ "   ,a.[DataStatus]\r\n"
				+ "   ,[DE]\r\n"
				+ " from [PCMS].[dbo].[FromSapCFM] as a \r\n " 
				+ where
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				  " SELECT DISTINCT  \r\n" 
				+ "    [ProductionOrder],[No],[DateInTest]\r\n"
				+ "   ,[DateOutTest],[Status],[Remark],a.[DataStatus]\r\n" 
				+ " from [PCMS].[dbo].[FromSapWaitTest] as a \r\n " 
				+ where 
				+ " Order by [No]";
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				  " SELECT DISTINCT  \r\n" 
				+ "   [ProductionOrder],[SaleOrder]\r\n"
				+ "   ,[SaleLine],[No],[SendDate],[NOK]\r\n"
				+ "   ,[LotNo],[ReceiveDate],[Remark],[Da]\r\n"
				+ "   ,[Db],[L],[ST],a.[DataStatus]\r\n" + "   "
				+ " from [PCMS].[dbo].[FromSapWorkInLab] as a \r\n " 
				+ where 
				+ " Order by No";
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				 "SELECT DISTINCT  \r\n" 
				+ this.selectPacking 
				+ " from [PCMS].[dbo].[FromSapPacking] as a \r\n "
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				 " SELECT DISTINCT  \r\n" 
				+ this.selectFinishing 
				+ " from [PCMS].[dbo].[FromSapFinishing] as a \r\n "
				+ where 
				+ " Order by Operation";
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				 " SELECT DISTINCT  \r\n" 
		        + this.selectInspect 
		        + " from [PCMS].[dbo].[FromSapInspect] as a \r\n " 
		        + where
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				 " SELECT DISTINCT  \r\n" 
			    + this.selectSendTestQC 
			    + " from [PCMS].[dbo].[FromSapSendTestQC] as a \r\n "
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				  " SELECT DISTINCT  \r\n" 
				 + this.selectDyeing 
				 + " from [PCMS].[dbo].[FromSapDyeing] as a \r\n " 
				 + where
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				" SELECT DISTINCT \r\n " 
		       + this.selectPreset 
		       + " from [PCMS].[dbo].[FromSapPreset] as a \r\n " 
		       + where;
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
		where += " a.ProductionOrder = '" + prdOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = 
				  " SELECT DISTINCT  \r\n" 
				+ this.selectPO 
				+ " from [PCMS].[dbo].[FromSapPO] as a \r\n " 
				+ where
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
		String sql = 
				  " SELECT DISTINCT \r\n " 
				+ "	   a.SaleNumber\r\n"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" 
				+ "    		 WHEN 0 THEN ''   \r\n"
				+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+[SaleFullName]\r\n"
				+ "    		 END AS [SaleFullName]   \r\n" 
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n "
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
				  " SELECT distinct \r\n"
				+ "		[UserStatus] \r\n"  
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
				    " SELECT distinct \r\n"
				  + "	[EmployeeID] ,[ColVisibleDetail] ,[ColVisibleSummary]\r\n"
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
		try {      
			String sql = 
					  " UPDATE [PCMS].[dbo].[ColumnSetting] "
					+ " 	SET [ColVisibleSummary] = ?  "
					+ " 	WHERE [EmployeeID]  = ? "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ " if @rc <> 0 " 
					+ " 	print @rc " 
					+ " else "
					+ " 	INSERT INTO [PCMS].[dbo].[ColumnSetting]	 "
					+ " 	([EmployeeID] ,[ColVisibleSummary])"//55 
					+ " 	values(? , ? )  ;"  ;     	
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
				  " SELECT distinct \r\n"
				+ "		[CustomerName] \r\n"  
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
				  " SELECT distinct \r\n"
			    + "		[CustomerShortName]  \r\n"  
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
		String customerShortName = "", userStatus = "", customerName="",userId = "" ,divisionName = "";
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
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
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
		String po = bean.getPurchaseOrder();
		int no = 1;   
		try {      
			String sql = 
					  " UPDATE [dbo].[SearchSetting]\r\n"
					+ "  SET [No] = ?  ,[CustomerName] = ?,[CustomerShortName] = ?\r\n"
					+ "      ,[SaleOrder] = ? ,[ArticleFG] = ? ,[DesignFG] =  ? \r\n"
					+ "      ,[ProductionOrder] = ? ,[SaleNumber] = ? ,[MaterialNo] = ?\r\n"
					+ "      ,[LabNo] = ? ,[DeliveryStatus] = ? ,[DistChannel] = ?\r\n"
					+ "      ,[SaleStatus] = ? ,[DueDate] = ? ,[SaleCreateDate] = ? \r\n"
					+ "      ,[PrdCreateDate] = ? ,[UserStatus] = ? , [Division] = ? , [PurchaseOrder] = ?\r\n"
					+ "  where  [EmployeeID] = ? and [ForPage] = ?" ;     	
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
			prepared.setString(19, po);
			prepared.setString(20, userId);
			prepared.setString(21, this.forPage); 
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
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,division = "",po="'";
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
		po = bean.getPurchaseOrder();
		int no = 1;   
		try {      
			String sql = 
					  " INSERT INTO [dbo].[SearchSetting]\r\n"
					+ "           ( [EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
					+ "           ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
					+ "           ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
					+ "           ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[ForPage],[Division] \r\n"
					+ "           ,[PurchaseOrder] \r\n"
					+ "           )\r\n"
					+ " VALUES\r\n"
					+ "           ( "
					+ "            ? , ? , ? , ? , ?, "
					+ "            ? , ? , ? , ? , ?,"
					+ "            ? , ? , ? , ? , ?,"
					+ "            ? , ? , ? , ? , ?,"
					+ "            ?"
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
				prepared.setString(21, po);  
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
				    " SELECT \r\n"    
				  + "   [EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
				  + "   ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
				  + "   ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
				  + "   ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[Division],[PurchaseOrder]\r\n"
				  + " FROM [PCMS].[dbo].[SearchSetting]\r\n"
				  + " where EmployeeID = '"+userId+"' and \r\n"
		  		  + "       ForPage = '"+forPage+ "' "; 
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
				  " SELECT distinct \r\n"
			    + "		[Division] \r\n"   
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n"
				+ " where Division <> '' \r\n" 
				+ " order by [Division] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		} 
		return list;
	}   
	@Override
	public ArrayList<PCMSAllDetail> getCustomerNameList(ArrayList<ConfigCustomerUserDetail> poList) {
		ArrayList<PCMSAllDetail> list = null; 
		ConfigCustomerUserDetail bean = poList.get(0);
		 String custNo = bean.getCustomerNo();
		 String where = " where (";
		 String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i++) {
			where += " [CustomerNo] = '"+array[i]+"' " ;
			if (i != array.length - 1) {
				where += " or \r\n";
			} ;
		}
		where += " ) \r\n";
		String sql =      
				  " SELECT distinct \r\n"
				+ "		[CustomerName] \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
			    + where
				+ " order by [CustomerName] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerShortNameList(ArrayList<ConfigCustomerUserDetail> poList) {
		ArrayList<PCMSAllDetail> list = null; 
		ConfigCustomerUserDetail bean = poList.get(0);
		 String custNo = bean.getCustomerNo();
		 String where = " where (";
		 String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i++) {
			where += " [CustomerNo] = '"+array[i]+"' " ;
			if (i != array.length - 1) {
				where += " or \r\n";
			} ;
		}
		where += " ) \r\n";
		String sql =   
				  " SELECT distinct \r\n"
			    + "		[CustomerShortName]  \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
			    + where
				+ " order by  [CustomerShortName] \r\n"; 
		  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}
}
