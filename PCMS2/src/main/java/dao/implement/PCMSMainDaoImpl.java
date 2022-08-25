	package dao.implement;

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
//			+ "	  ,  CAST(DAY(b.GreigeInDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(GreigeInDate)AS VARCHAR(2))   AS GreigeInDate \r\n"
//			+ "		  ,  CAST(DAY(i.WorkDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(i.WorkDate)AS VARCHAR(2))   AS DyePlan  \r\n"
//			+ "		 ,  CAST(DAY(j.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH(j.DyeActual)AS VARCHAR(2))   AS DyeActual \r\n"
//			+ "		 ,  CAST(DAY(k.Dryer) AS VARCHAR(2)) + '/' +   CAST(MONTH(k.Dryer)AS VARCHAR(2))   AS Dryer \r\n"
//			+ "		 ,  CAST(DAY(l.Finishing) AS VARCHAR(2)) + '/' +   CAST(MONTH(l.Finishing)AS VARCHAR(2))   AS Finishing \r\n"
//			+ "		 ,  CAST(DAY(m.Inspectation) AS VARCHAR(2)) + '/' +   CAST(MONTH(m.Inspectation)AS VARCHAR(2))   AS Inspectation  \r\n"
//			+ "		 ,  CAST(DAY(n.Prepare) AS VARCHAR(2)) + '/' +   CAST(MONTH(n.Prepare)AS VARCHAR(2))   AS Prepare \r\n"
//			+ "	  ,  CAST(DAY(o.Preset) AS VARCHAR(2)) + '/' +   CAST(MONTH(o.Preset)AS VARCHAR(2))   AS Preset   \r\n  "
//			+ "	  ,  CAST(DAY(p.Relax) AS VARCHAR(2)) + '/' +   CAST(MONTH(p.Relax)AS VARCHAR(2))   AS Relax   \r\n  " 
//			+ "	  , CAST(DAY(d.CFMAnswerDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(d.CFMAnswerDate)AS VARCHAR(2))   as CFMDateActual\r\n"  
//    		+ "   ,CAST(DAY(g.CFMPlanDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(g.CFMPlanDate )AS VARCHAR(2))  AS CFMPlanDate \r\n "
//			+ "   ,CASE  \r\n"
//    		+ "		  WHEN h.[ProductionOrder] is not null \r\n"
//    		+ "       THEN CAST(DAY(h.DeliveryDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(h.DeliveryDate)AS VARCHAR(2))     \r\n"
//    		+ "		  ELSE CAST(DAY(b.CFTYPE) AS VARCHAR(2)) + '/' +   CAST(MONTH(b.CFTYPE)AS VARCHAR(2))     \r\n"
//    		+ "		  END AS DeliveryDate \r\n" 
//    		+ "	  , CAST(DAY(z.LotShipping) AS VARCHAR(2)) + '/' +   CAST(MONTH(z.LotShipping)AS VARCHAR(2)) AS LotShipping \r\n"
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
			+ "   , a.ShipDate \r\n" ;  
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
//			+ "	  ,( a.SaleQuantity - a.RemainQuantity ) as BillQuantity\r\n" 
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
			+ "   , b.[LotShipping] \r\n"
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
			+ "   , a.ShipDate \r\n" ;  
	private String selectMain = 
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
//			+ "	  ,( a.SaleQuantity - a.RemainQuantity ) as BillQuantity\r\n" 
			+ "   , a.SaleUnit\r\n"  
			+ "   , b.ProductionOrder\r\n"
			+ "   , b.TotalQuantity \r\n"     
			+ "	  ,	b.UserStatus\r\n"
			+ "   , b.LabStatus\r\n"
			+ "   , a.DueDate\r\n"  
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
			+ "   , a.CustomerName\r\n"
			+ "   , a.CustomerShortName\r\n"
			+ "   , a.SaleNumber\r\n" 
			+ "   , a.SaleCreateDate\r\n"
			+ "   , b.PrdCreateDate\r\n" 
			+ "   , a.MaterialNo\r\n"
			+ "   , a.DeliveryStatus"
			+ "   , a.SaleStatus\r\n" 
			+ "   , [LotNo] \r\n"
			+ "   , a.ShipDate \r\n" ;    
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
			+ "   , [LotNo] \r\n"
			+ "   , a.ShipDate \r\n" ;         
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
	  		    + "   , c.LotShipping \r\n" 
	  		    + "   , DyeStatus  \r\n"
	  		    + "   , h.DeliveryDate \r\n"
	  		    + "   , LabNo,a.CustomerName\r\n"
	  		    + "   , a.CustomerShortName\r\n"
	  		    + "   , a.SaleNumber\r\n"
	  		    + "   , a.SaleCreateDate\r\n"
	  		    + "   , PrdCreateDate\r\n"
	  		    + "   , a.MaterialNo\r\n"
	  		    + "   , a.DeliveryStatus\r\n"
	  		    + "   , a.SaleStatus\r\n"
	  		    + "   , [LotNo] \r\n"
	  		    + "   , a.ShipDate  \r\n"
	  		    + "   , 'WaitLot' as TypePrd \r\n"
	  		    + "   , 'WaitLot' AS TypePrdRemark    \r\n"; 
	private String selectTwo = 
//			  "  CASE  \r\n"
//			+ "		  WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"  
//			+ "		  ELSE 'รอจัด Lot'  \r\n"
//			+ "		  END AS [ProductionOrder] \r\n"     
		      "    b.[ProductionOrder]\r\n"
			+ "   ,ColorCustomer \r\n"
		    + "   ,[LotNo],[Batch],[LabNo]\r\n"
			+ "	  ,[PrdCreateDate],a.[DueDate],a.[SaleOrder]\r\n"
			+ "	  ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n" 
			+ "			WHEN 0 THEN ''  \r\n"
			+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
			+ "			END AS [SaleLine] \r\n"
			+ "	  ,PurchaseOrder,a.ArticleFG,a.DesignFG\r\n"
			+ "	  ,CustomerName,CustomerShortName,Shade,BookNo,Center\r\n"
			+ "	  ,MaterialNo,Volumn,SaleUnit,Unit as STDUnit\r\n" 
			+ "	  ,Color,PlanGreigeDate,RefPrd,b.GreigeInDate\r\n"
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
			+ "   ,GreigeDesign \r\n";
 
	private String selectPO = 
			  "    [ProductionOrder],[RollNo],[QuantityKG]\r\n"
			+ "   ,[QuantityMR],[CreateDate] ,[PurchaseOrder]\r\n"
			+ "   ,CASE PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"    
			+ "		 	ELSE SUBSTRING( [PurchaseOrderLine] , PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' '), LEN( [PurchaseOrderLine] ) )\r\n"
			+ "       	END AS [PurchaseOrderLine] \r\n"      
			+ "   ,[RequiredDate],[PurchaseOrderDate],[PODefault]\r\n"
			+ "   ,[POLineDefault],[POPostingDateDefault],a.[DataStatus] \r\n";
//	private String createTempUCAL = ""
////			+ " If(OBJECT_ID('tempdb..#TempUCAL') Is Not Null)\r\n"
////			+ "	begin\r\n"
////			+ "		Drop Table #TempUCAL\r\n"
////			+ "	end ;\r\n"
//			+ "SELECT distinct -- a.[ProductionOrder]  ,[UserStatus]  ,d.[Grade]  ,CountGRFinish ,IsAnita ,CoaApproveDate  ,[CFMStatus],[DistChannel],\r\n"
//			+ "	  a.[ProductionOrder], a.saleorder,SumBill,CoaApproveDate,IsAnita,StockLoad,CountGRFinish,a.GRADE\r\n"
//			+ "	    , a.ProductionOrderRPM  \r\n"
//			+ "		, CoaApproveDateRPM , StockLoadRPM, CountGRFinishRPM, GradeRPM, a.Volumn\r\n"
//			+ "		, CFMStatusRPM,SumGR,\r\n"
//			+ "	  	  CASE \r\n"
//			+ "	    when --a.UserStatus = 'ขายแล้ว' or   \r\n"
//			+ "			 a.UserStatus = 'ยกเลิก' or a.UserStatus = 'ปิดเพื่อแก้ไข' or  a.UserStatus = 'รับเข้าST.(P/S)' or\r\n"
//			+ "			 a.UserStatus = 'ตัดเกรดZ' or a.UserStatus = 'Over' or a.UserStatus = 'ขายเหลือ' or a.UserStatus = 'Lab' or \r\n"
//			+ "          a.UserStatus = 'HOLD,รอโอน' 		THEN a.UserStatus\r\n"
//			+ "		when ( a.[grade] = 'A' OR a.[grade] = 'C' ) AND CountGRFinish = 1 and [DistChannel] = 'DM' AND a.[ProductionOrderRP] IS NULL THEN \r\n"
//			+ "			CASE \r\n"
//			+ "				WHEN CFMStatus = '' AND SumBill IS NULL THEN 'รอตอบ CFM' \r\n"
//			+ "				WHEN ( CFMStatus = 'N' or CFMStatus = 'N/Y' or CFMStatus = 'N,Y' ) AND CoaApproveDate IS NULL AND SumBill IS NULL THEN 'รอสรุปจาก QA'   \r\n"
//			+ "				WHEN IsAnita = 1 and ( CFMStatus = 'Y' or CFMStatus = 'Y/N' or CFMStatus = 'Y,N' ) AND CoaApproveDate IS NULL AND SumBill IS NULL THEN 'รอตอบ CFM ตัวแทน'\r\n"
//			+ "				WHEN IsAnita = 0 and ( CFMStatus = 'Y' or CFMStatus = 'Y/N' or CFMStatus = 'Y,N' ) AND CoaApproveDate IS NULL AND SumBill IS NULL THEN 'รอ COA ลูกค้า ok สี'   \r\n"
//			+ "				WHEN ( CFMStatus = 'Y' or CFMStatus = 'Y/N' or CFMStatus = 'Y,N' ) AND CoaApproveDate IS NOT NULL THEN \r\n"
//			+ "					CASE \r\n"
//			+ "						WHEN SumBill IS NULL THEN \r\n"
//			+ "							CASE \r\n"
//			+ "								WHEN ( StockLoad is not null or StockLoad <> '' ) THEN 'รอเปิดบิล'    \r\n"
//			+ "								WHEN IsAnita = 1 THEN 'รอแจ้งส่ง'  \r\n"
//			+ "								ELSE 'รอขาย' \r\n"
//			+ "							end      \r\n"
//			+ "						WHEN SumGR <= SumBill THEN 'ขายแล้ว'  \r\n"
//			+ "						WHEN SumBill > 0 THEN 'ขายแล้วบางส่วน'  \r\n"
//			+ "						ELSE a.UserStatus\r\n"
//			+ "					end \r\n"
//			+ "				WHEN  SumBill IS NOT NULL THEN \r\n"
//			+ "					CASE  \r\n"
//			+ "						WHEN SumGR <= SumBill THEN 'ขายแล้ว'  \r\n"
//			+ "						WHEN SumBill > 0 THEN 'ขายแล้วบางส่วน'\r\n"
//			+ "						ELSE a.UserStatus   \r\n"
//			+ "					end \r\n"
//			+ "				ELSE a.UserStatus\r\n"
//			+ "			end\r\n"
//			+ "		when ( a.[grade] = 'A' OR a.[grade] = 'C' ) AND CountGRFinish = 1 and [DistChannel] = 'EX' AND a.[ProductionOrderRP] IS NULL THEN \r\n"
//			+ "			CASE \r\n"
//			+ "				WHEN CFMStatus = '' AND SumBill IS NULL THEN 'รอตอบ CFM' \r\n"
//			+ "				WHEN ( CFMStatus = 'N' or CFMStatus = 'N/Y' or CFMStatus = 'N,Y' ) AND CoaApproveDate IS NULL AND  SumBill IS NULL  THEN 'รอสรุปจาก QA'  \r\n"
//			+ "				WHEN IsAnita = 1 and ( CFMStatus = 'Y' or CFMStatus = 'Y/N' or CFMStatus = 'Y,N' ) AND CoaApproveDate IS NULL AND SumBill IS NULL  THEN 'รอตอบ CFM ตัวแทน'\r\n"
//			+ "				WHEN IsAnita = 0 and ( CFMStatus = 'Y' or CFMStatus = 'Y/N' or CFMStatus = 'Y,N' ) AND CoaApproveDate IS NULL AND SumBill IS NULL  THEN 'รอ COA ลูกค้า ok สี'   \r\n"
//			+ "				WHEN ( CFMStatus = 'Y' or CFMStatus = 'Y/N' or CFMStatus = 'Y,N' ) AND CoaApproveDate IS NOT NULL THEN \r\n"
//			+ "					CASE \r\n"
//			+ "						WHEN SumBill IS NULL THEN  \r\n"
//			+ "							CASE \r\n"
//			+ "								WHEN ( StockLoad is not null or StockLoad <> '' ) THEN 'รอเปิดบิล'    \r\n"
//			+ "								ELSE 'รอแจ้งส่ง'  \r\n"
//			+ "							end \r\n"
//			+ "						WHEN SumGR <= SumBill THEN 'ขายแล้ว'  \r\n"
//			+ "						WHEN SumBill > 0 THEN 'ขายแล้วบางส่วน'\r\n"
//			+ "						ELSE a.UserStatus   \r\n"
//			+ "					end \r\n"
//			+ "				WHEN  SumBill IS NOT NULL THEN \r\n"
//			+ "					CASE  \r\n"
//			+ "						WHEN SumGR <= SumBill THEN 'ขายแล้ว'  \r\n"
//			+ "						WHEN SumBill > 0 THEN 'ขายแล้วบางส่วน'\r\n"
//			+ "						ELSE a.UserStatus   \r\n"
//			+ "					end \r\n"
//			+ "				ELSE a.UserStatus\r\n"
//			+ "				end\r\n"
//			+ "		when a.[grade] = 'Z' THEN 'ตัดเกรด Z'   \r\n"
//			+ "		ELSE a.UserStatus END AS UserStatusCal  \r\n"
//			+ "   into #tempUCAL \r\n "
//			+ "	  FROM (select distinct a.ProductionOrder ,a.Volumn,a.UserStatus,a.SaleOrder,a.SaleLine,a.LotNo,ProductionOrderRPM,[ProductionOrderRP] \r\n"
//			+ "	  , CoaApproveDate as CoaApproveDateRPM ,StockLoad AS StockLoadRPM,CountGRFinish AS CountGRFinishRPM, H.GRADE AS GradeRPM\r\n"
//			+ "	  , CFMStatus AS CFMStatusRPM,gs.[Grade],\r\n"
//			+ "					case\r\n"
//			+ "						WHEN Unit = 'KG' THEN SumBillKG   \r\n"
//			+ "						WHEN Unit = 'YD' THEN SumBillYD  \r\n"
//			+ "						ELSE SumBillMR\r\n"
//			+ "					end AS SumBill,\r\n"
//			+ "					case\r\n"
//			+ "						WHEN Unit = 'KG' THEN SumGRKG   \r\n"
//			+ "						WHEN Unit = 'YD' THEN SumGRYD  \r\n"
//			+ "						ELSE SumGRMR\r\n"
//			+ "					end AS SumGR\r\n"
//			+ "			 from [PCMS].[dbo].[FromSapMainProd] as a\r\n"
//			+ "			 LEFT JOIN ( SELECT [ProductionOrder]  \r\n"
//			+ "						  ,SUM([QuantityKG]) AS SumBillKG\r\n"
//			+ "						  ,SUM([QuantityYD]) AS SumBillYD\r\n"
//			+ "						  ,SUM([QuantityMR]) AS SumBillMR\r\n"
//			+ "					  FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
//			+ "					  WHERE [DataStatus]  = 'O' and [ProductionOrder] <> '' \r\n"
//			+ "					  GROUP BY [ProductionOrder]  )AS BS ON A.ProductionOrder = BS.ProductionOrder \r\n"
//			+ "			  LEFT JOIN (SELECT DISTINCT [ProductionOrder] \r\n"
//			+ "			                 ,[Grade]\r\n"
//			+ "							,SUM([QuantityKG]) AS SumGRKG\r\n"
//			+ "							,SUM([QuantityYD]) AS SumGRYD\r\n"
//			+ "							,SUM([QuantityMR]) AS SumGRMR\r\n"
//			+ "					  FROM [PCMS].[dbo].[FromSapGoodReceive]\r\n"
//			+ "					  where DataStatus = 'O' and [ProductionOrder] <> '' \r\n"
//			+ "					  GROUP BY [ProductionOrder],[Grade]  )AS GS ON A.ProductionOrder = GS.ProductionOrder \r\n"
//			+ "			 left join (  SELECT distinct  a.ProductionOrder AS ProductionOrderRPM, [ProductionOrderRP]  ,b.SaleOrder,b.SaleLine\r\n"
//			+ "							, CoaApproveDate, StockLoad,CountGRFinish, D.GRADE,CFMStatus \r\n"
//			+ "							FROM [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
//			+ "							inner join [PCMS].[dbo].FromSapMainProd as b on \r\n"
//			+ "									b.ProductionOrder = a.ProductionOrderRP and b.DataStatus = 'O' and\r\n"
//			+ "									a.[SaleOrder] = b.[SaleOrder] and a.[SaleLine] = b.[SaleLine]\r\n"
//			+ "							left join ( SELECT [ProductionOrder] \r\n"
//			+ "											,count([OperationEndTime]) as CountGRFinish\r\n"
//			+ "									  FROM [PPMM].[dbo].[DataFromSap]\r\n"
//			+ "									  WHERE OPERATION = '230' AND OperationStatus = 'PROCESS DONE'\r\n"
//			+ "									  group by ProductionOrder ) as C on A.ProductionOrder = C.ProductionOrder   \r\n"
//			+ "							LEFT JOIN (SELECT DISTINCT [ProductionOrder] \r\n"
//			+ "											 ,[Grade] \r\n"
//			+ "									  FROM [PCMS].[dbo].[FromSapGoodReceive]\r\n"
//			+ "									  where DataStatus = 'O' and [ProductionOrder] <> '' \r\n"
//			+ "									  )AS d ON A.ProductionOrder = d.ProductionOrder \r\n"
//			+ "							left join ( SELECT  [LotNumber]  ,max(CoaApproveDate)  as CoaApproveDate\r\n"
//			+ "											FROM [QCMS].[dbo].[RequestOrder]\r\n"
//			+ "											where CoaApproveDate is not null  \r\n"
//			+ "											group by LotNumber) as e on B.[LotNo] = e.[LotNumber]\r\n"
//			+ "							left join ( SELECT a.[ProductionOrder] ,[CFMStatus]  , CFMNo \r\n"
//			+ "											FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
//			+ "											INNER join (  SELECT [ProductionOrder] , max([CFMNo]) as maxCFMNo \r\n"
//			+ "															FROM [PCMS].[dbo].[FromSapCFM]\r\n"
//			+ "															WHERE CFMNumber <> ''\r\n"
//			+ "															group by [ProductionOrder]   \r\n"
//			+ "														) as b on a.ProductionOrder = b.ProductionOrder AND A.CFMNo = B.maxCFMNo\r\n"
//			+ "										) as f on A.[ProductionOrder] = f.[ProductionOrder]  \r\n"
//			+ "							LEFT JOIN( SELECT [ProductionOrder] ,[SaleOrder] ,[SaleLine]  ,[StockLoad] \r\n"
//			+ "									FROM [PCMS].[dbo].[InputStockLoad] \r\n"
//			+ "									WHERE DataStatus = 'O' ) AS g on A.ProductionOrder = g.ProductionOrder and B.SaleOrder = g.SaleOrder and B.SaleLine = g.SaleLine    \r\n"
//			+ "							WHERE a.DataStatus = 'O'   ) as h on a.ProductionOrder = h.ProductionOrderRP\r\n"
//			+ "         where a.SaleOrder <> ''	\r\n"
//			+ "			)as a\r\n"
//			+ "inner join (  SELECT a.[SaleOrder] ,a.[SaleLine] ,[SaleUnit] ,[DistChannel] , count (b.CustomerNo) as IsAnita \r\n"
//			+ "					  FROM [PCMS].[dbo].[FromSapMainSale] AS A\r\n"
//			+ "					   left  join [PCMS].[dbo].[ConfigCustomerEX] AS B ON A.CustomerNo = B.CustomerNo \r\n"
//			+ "                      where SaleOrder <> '' \r\n"
//			+ "					  group by [SaleOrder] ,[SaleLine],[SaleUnit],[DistChannel]  \r\n"
//			+ "			  ) as c on a.SaleOrder = c.SaleOrder and a.SaleLine = c.SaleLine \r\n"
//			+ "	  left join ( SELECT [ProductionOrder] \r\n"
//			+ "						,count([OperationEndTime]) as CountGRFinish\r\n"
//			+ "				  FROM [PPMM].[dbo].[DataFromSap]\r\n"
//			+ "				  WHERE OPERATION = '230' AND OperationStatus = 'PROCESS DONE'\r\n"
//			+ "				  group by ProductionOrder ) as b on a.ProductionOrder = b.ProductionOrder   \r\n"
//			+ "--left join (SELECT ProductionOrder , Grade \r\n"
//			+ "--				from [PCMS].[dbo].[FromSapMainGrade] \r\n"
//			+ "--				where DataStatus = 'O' ) as d on a.ProductionOrder = d.ProductionOrder	 \r\n"
//			+ "left join ( SELECT  [LotNumber]  ,max(CoaApproveDate)  as CoaApproveDate\r\n"
//			+ "				FROM [QCMS].[dbo].[RequestOrder]\r\n"
//			+ "				where CoaApproveDate is not null  \r\n"
//			+ "				group by LotNumber) as e on a.[LotNo] = e.[LotNumber]\r\n"
//			+ "left join ( SELECT a.[ProductionOrder] ,[CFMStatus]  , CFMNo \r\n"
//			+ "				FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
//			+ "				INNER join (  SELECT [ProductionOrder] , max([CFMNo]) as maxCFMNo \r\n"
//			+ "								FROM [PCMS].[dbo].[FromSapCFM]\r\n"
//			+ "								WHERE CFMNumber <> ''\r\n"
//			+ "								group by [ProductionOrder]   \r\n"
//			+ "							) as b on a.ProductionOrder = b.ProductionOrder AND A.CFMNo = B.maxCFMNo\r\n"
//			+ "			) as f on a.[ProductionOrder] = f.[ProductionOrder]  \r\n"
//			+ "LEFT JOIN( SELECT [ProductionOrder] ,[SaleOrder] ,[SaleLine]  ,[StockLoad] \r\n"
//			+ "		FROM [PCMS].[dbo].[InputStockLoad] \r\n"
//			+ "		WHERE DataStatus = 'O' ) AS g on a.ProductionOrder = g.ProductionOrder and a.SaleOrder = g.SaleOrder and a.SaleLine = g.SaleLine   \r\n"
//			+ "  \r\n";
//	private String createTempG = 
//			    " select distinct\r\n"
//			  + "   b.ProductionOrder , GreigeInDate , i.WorkDate AS DyePlan  , j.DyeActual , k.Dryer , l.Finishing , m.Inspectation  , n.Prepare \r\n"
//			  + " , o.Preset , p.Relax , d.CFMAnswerDate as CFMDateActual, g.CFMPlanDate  , z.LotShipping,j.DyeStatus \r\n"
//			  + " into #TempG  \r\n"
//			  + " FROM [PCMS].[dbo].[FromSapMainProd] as b \r\n"
//			  + " left join ( SELECT distinct a.[ProductionOrder]\r\n"
//			  + "					  ,[CFMNo]\r\n"
//			  + "					  ,[CFMNumber]\r\n"
//			  + "					  ,[CFMSendDate]\r\n"
//			  + "					  ,[CFMAnswerDate]\r\n"
//			  + "					  ,[CFMStatus] \r\n"
//			  + "					  ,[CFMRemark]\r\n"
//			  + "				FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
//			  + "				inner join (select ProductionOrder ,   max([CFMNo]) as maxCFMNo  \r\n"
//			  + "					        from [PCMS].[dbo].[FromSapCFM] as a\r\n"
//			  + "							where  DataStatus <> 'X'  and [CFMNumber] <> ''\r\n"
//			  + "					 		group by ProductionOrder )as b on \r\n"
//			  + "                         a.[ProductionOrder] = b.[ProductionOrder] and  a.[CFMNo] = b.[maxCFMNo]  \r\n"
//			  + "             where  DataStatus <> 'X'  and [CFMNumber] <> '' \r\n"
//			  + "			) as d on b.ProductionOrder = d.ProductionOrder  \r\n"
//			  + " left join ( SELECT distinct a.[ProductionOrder]   \r\n"
//			  + "	  		, case \r\n"
//			  + "				when EndDateName = 'Saturday' then DATEADD(DAY, 9, a.[WorkDate])\r\n"
//			  + "				when EndDateName is not null then DATEADD(DAY, 8, a.[WorkDate])\r\n"
//			  + "				else null \r\n"
//			  + "				end as CFMPlanDate   \r\n"
//			  + "  FROM ( select [ProductionOrder] ,[WorkDate]  ,[Operation],DATENAME(DW, [WorkDate]) as EndDateName\r\n"
//			  + "			FROM [PPMM].[dbo].[OperationWorkDate] ) as a   \r\n"
//			  + "  inner join ( select [ProductionOrder]   \r\n"
//			  + "					  ,max([Operation]) as maxOperation \r\n"
//			  + "			   FROM [PPMM].[dbo].[OperationWorkDate]  \r\n"
//			  + "			   where Operation >= 100 and Operation <= 103\r\n"
//			  + "			   group by ProductionOrder) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.maxOperation\r\n"
//			  + " ) as g on b.ProductionOrder = g.ProductionOrder  \r\n"
//			  + " left join ( select distinct a.ProductionOrder , WorkDate \r\n"
//			  + "              from  [PPMM].[dbo].[OperationWorkDate]  as a\r\n"
//			  + "              inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			  + "                          from  [PPMM].[dbo].[DataFromSap] \r\n"
//			  + "                          where Operation >= 100 and Operation <= 103\r\n"
//			  + "                          group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			  + "              where a.Operation >= 100 and a.Operation <= 103 ) as i on b.ProductionOrder = i.ProductionOrder\r\n"
//			  + " left join ( select a.ProductionOrder , OperationEndDate as DyeActual,c.DyeingStatus as DyeStatus\r\n"
//			  + "            from  [PPMM].[dbo].[DataFromSap]  as a\r\n"
//			  + "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			  + "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
//			  + "                        where Operation >= 100 and Operation <= 103\r\n"
//			  + "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			  + "				left join [PPMM].[dbo].[ShopFloorControlDetail] as c on a.ProductionOrder = c.ProductionOrder and c.Operation = b.Operation \r\n"
//			  + "            where a.Operation >= 100 and a.Operation <= 103 \r\n"
//			  + "            ) as j on b.ProductionOrder = j.ProductionOrder\r\n"
//			  + " left join ( select distinct a.ProductionOrder , OperationEndDate as Dryer \r\n"
//			  + "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			  + "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			  + "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
//			  + "                        where Operation >= 140 and Operation <= 143\r\n"
//			  + "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			  + "            where a.Operation >= 140 and a.Operation <= 143 )  as k on b.ProductionOrder = k.ProductionOrder\r\n"
//			  + " left join ( select distinct a.ProductionOrder , OperationEndDate as Finishing \r\n"
//			  + "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			  + "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			  + "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
//			  + "                        where Operation >= 190 and Operation <= 193\r\n"
//			  + "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			  + "            where a.Operation >= 190 and a.Operation <= 193  ) as l on b.ProductionOrder = l.ProductionOrder\r\n"
//			  + " left join ( select distinct a.ProductionOrder , OperationEndDate as Inspectation \r\n"
//			  + "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			  + "            inner join ( select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			  + "                         from  [PPMM].[dbo].[DataFromSap] \r\n"
//			  + "                         where Operation >= 199 and Operation <= 200\r\n"
//			  + "                         group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			  + "            where a.Operation >= 199 and a.Operation <= 200 ) as m on b.ProductionOrder = m.ProductionOrder \r\n"
//			  + " left join ( select ProductionOrder , OperationEndDate as Prepare  \r\n"
//			  + "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			  + "            where a.Operation  = 5 ) as n on b.ProductionOrder = n.ProductionOrder \r\n"
//			  + " left join ( select distinct ProductionOrder , OperationEndDate as Preset  \r\n"
//			  + "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			  + "            where a.Operation = 50  ) as o on b.ProductionOrder = o.ProductionOrder \r\n"
//			  + " left join ( select distinct ProductionOrder , OperationEndDate as Relax  \r\n"
//			  + "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			  + "            where a.Operation = 20  ) as p on b.ProductionOrder = p.ProductionOrder \r\n" 
//			  + " left join (SELECT distinct [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,max([LotShipping]) as [LotShipping]\r\n"
//			  + "              FROM [PCMS].[dbo].[FromSapMainGrade]\r\n"
//			  + "			where DataStatus = 'O' \r\n"
//			  + "            GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) \r\n"
//			  + "    as z on z.ProductionOrder = b.ProductionOrder  and z.SaleLine = b.SaleLine and z.SaleOrder = b.SaleOrder "
//			+ "  ;\r\n" ;
//	private String createTempUCALRP = 
//		    " SELECT distinct  A.ProductionOrder,A.Grade --, a.ProductionOrderRPM ,a.SumBill \r\n"
//		    + "--, a.CoaApproveDateRPM , a.StockLoadRPM, a.CountGRFinishRPM, a.GradeRPM , a.CFMStatusRPM  ,a.Volumn  \r\n"
//		    + ", CASE  \r\n"
//		    + "		when ( a.GradeRPM = 'A' ) AND a.CountGRFinishRPM = 1 AND a.SumBill  IS NULL  THEN \r\n"
//		    + "			CASE \r\n"
//		    + "				WHEN a.CFMStatusRPM = '' THEN 'รอตอบ CFM' \r\n"
//		    + "				WHEN ( CFMStatusRPM = 'N' or CFMStatusRPM = 'N/Y' or CFMStatusRPM = 'N,Y' ) AND a.CoaApproveDateRPM IS NULL THEN 'รอสรุปจาก QA'   \r\n"
//		    + "				WHEN ( CFMStatusRPM= 'Y' or CFMStatusRPM= 'Y/N' or CFMStatusRPM= 'Y,N' ) AND CoaApproveDate IS NULL THEN 'รอตอบ CFM ตัวแทน' \r\n"
//		    + "				WHEN ( CFMStatusRPM= 'Y' or CFMStatusRPM= 'Y/N' or CFMStatusRPM= 'Y,N' ) AND a.CoaApproveDateRPM IS NOT NULL THEN 'รอแจ้งส่ง'     \r\n"
//		    + "				ELSE a.UserStatusCal\r\n"
//		    + "			end \r\n"
//		    + "		ELSE a.UserStatusCal END AS UserStatusCal \r\n"
//		    + "into #tempUCALRP\r\n"
//		    + "FROM #tempUCAL AS A \r\n"
////		    + "WHERE a.ProductionOrderRPM IS NOT NULL "
//		+ "  ;\r\n" ;
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
	private String leftJoinM =  
//			  " left join (SELECT distinct [ProductionOrder] ,[Grade] ,[PriceSTD]\r\n"
//			+ "				     ,sum([QuantityMR]) as GRSumMR\r\n"      
//			+ "				     ,sum([QuantityKG]) as GRSumKG\r\n"
//			+ "				     ,sum([QuantityYD]) as GRSumYD\r\n" 
//			+ "			  FROM [PCMS].[dbo].[FromSapGoodReceive]\r\n"
//			+ "			  where datastatus = 'O'\r\n"   
//			+ "			  GROUP BY ProductionOrder,Grade ,[PriceSTD])\r\n" 
//			+ "  as m on b.ProductionOrder = m.ProductionOrder \r\n"
			" left join #tempSumGR as m on b.ProductionOrder = m.ProductionOrder \r\n";
//	private String leftJoinM = " left join (SELECT distinct [ProductionOrder] \r\n"
//			+ "				  ,[Grade] \r\n"
//			+ "               ,[PriceSTD]\r\n"
//			+ "				  ,sum([QuantityMR]) as GRSumMR\r\n"      
//			+ "				  ,sum([QuantityKG]) as GRSumKG\r\n"
//			+ "				  ,sum([QuantityYD]) as GRSumYD\r\n" 
//			+ "			  FROM [PCMS].[dbo].[FromSapGoodReceive] AS a\r\n"	
//			+ "			  where a.DataStatus = 'O'\r\n"   
//			+ "			  GROUP BY ProductionOrder,Grade ,[PriceSTD])\r\n"
////			+ "  as m on c.ProductionOrder = m.ProductionOrder and c.Grade = m.Grade	\r\n";
//			+ "  as m on b.ProductionOrder = m.ProductionOrder \r\n";
//	private String leftJoinUCAL = "    "
//			+ "   left join #TempUCAL " 
//			+ "   as UCAL on b.ProductionOrder = UCAL.ProductionOrder   AND (  m.Grade = UCAL.Grade    OR  m.Grade IS NULL )  \r\n"; 
//	private String leftJoinUCALRP = "    "
//			+ "   left join #TempUCALRP as UCALRP on b.ProductionOrder = UCALRP.ProductionOrder   AND (  m.Grade = UCALRP.Grade    OR  m.Grade IS NULL )    \r\n"; 
	private String leftJoinUCAL = "    "
			+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] " 
			+ " as UCAL on b.ProductionOrder = UCAL.ProductionOrder AND ( m.Grade = UCAL.Grade OR m.Grade IS NULL )  \r\n"; 
	private String leftJoinUCALRP = "    "
			+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCALRP on b.ProductionOrder = UCALRP.ProductionOrder   AND (  m.Grade = UCALRP.Grade    OR  m.Grade IS NULL )    \r\n"; 
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
   		 + "        , null as TotalQuantity \r\n"
   		 + "		, '' as Grade \r\n"
   		 + "		, null as BillSendWeightQuantity \r\n"
   		 + "		, null as BillSendQuantity  \r\n"
   		 + "		, null as BillSendMRQuantity \r\n"
   		 + "		, null as BillSendYDQuantity  \r\n"
   		 + "		, '' as LabNo\r\n"
   		 + "		, '' as LabStatus\r\n"
   		 + "		, null as CFMPlanLabDate\r\n"
   		 + "		, null as CFMActualLabDate \r\n"
   		 + "		, null as CFMCusAnsLabDate \r\n"
   		 + "		, '' as UserStatus \r\n"
   		 + "		, null as TKCFM \r\n"
   		 + "		, null as CFMPlanDate \r\n"
   		 + "		, null as DeliveryDate  \r\n"
   		 + "		, null as CFMSendDate \r\n"
   		 + "		, null as CFMAnswerDate \r\n"
   		 + "		, '' as CFMStatus \r\n"
   		 + "		, '' as CFMNumber  \r\n"
   		 + "		, '' as CFMRemark  \r\n"
   		 + "		, '' as RemarkOne \r\n"
   		 + "		, '' as RemarkTwo \r\n"
   		 + "		, '' as RemarkThree  \r\n"
   		 + "		, '' as StockRemark \r\n"
   		 + "		, null as GRSumKG \r\n"
   		 + "		, null as GRSumYD \r\n"
   		 + "		, null as GRSumMR \r\n"
   		 + "		, null as DyePlan \r\n"
   		 + "		, null as DyeActual   \r\n"
   		 + "		, '' as [SwitchRemark] \r\n"
   		 + "		, null as [PrdCreateDate]\r\n"
   		 + "		, NULL AS Volumn   \r\n"
   		 + "		, NULL AS VolumnFGAmount  	\r\n"   
		 + "		, null as GreigeInDate \r\n" 
		 + "		, null as Dryer \r\n"
		 + "		, null as Finishing \r\n"
		 + "		, null as Inspectation  \r\n"
		 + "		, null as Prepare \r\n"
		 + "		, null as Preset \r\n"
		 + "		, null as Relax \r\n"
		 + "		, null as CFMDateActual\r\n"
//			+ "				, null as CFMPlanDate  \r\n"
//			+ "				--, null as LotShipping \r\n"
		 + "		, null as DyeStatus  \r\n"
//			+ "				, null as LabNo\r\n"
//			+ "             , null as PrdCreateDate\r\n" 
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
   		 + "	left join ( select DISTINCT SaleOrder ,SaleLine ,COUNT(ProductionOrder) AS CountProdRP\r\n"
   		 + "				from [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
   		 + "				where a.DataStatus = 'O' and  ProductionOrder = 'รอจัด Lot' \r\n"
   		 + "				GROUP BY  SaleOrder ,SaleLine \r\n"
   		 + "				) AS D ON A.SaleOrder = D.SaleOrder AND A.SaleLine = D.SaleLine  \r\n"
   		 + "	WHERE ( c.SumVolMain > 0 ) OR D.SaleOrder IS NOT NULL\r\n"
   		 + " ) AS B ON A.SaleOrder = B.SaleOrder AND A.SaleLine = B.SaleLine \r\n" ;
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
	 private String leftJoinBPartOne =    
			  	" left join ( SELECT distinct a.[SaleOrder] , a.[Saleline]  ,[TotalQuantity] ,[Unit]\r\n"
			  + "			 , [RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
			  + "			 , a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
			  + "			 , [Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
			  + "			 , [OrderPuang] ,[RefPrd],b.[GreigeInDate] ,[BCDate],b.[Volumn]\r\n"
			  + "			 , [CFdate],[CFType],[Shade],b.[LotShipping],[BillSendQuantity],m.[Grade] \r\n"
			  + "			 , [PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG]\r\n"
			  + "			 , b.[ProductionOrder] ,b.[LotNo] \r\n"  
//			  + "			 ,CASE  \r\n"
//			  + "					WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
//			  + "					WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//			  + "                   WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
//			  + "					WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n" 
//			  + "					ELSE 'รอจัด Lot'  \r\n"
//			  + "					END AS [ProductionOrder]  \r\n"
//			  + "			 ,CASE  \r\n"
//			  + "					WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
//			  + "					WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//			  + "                   WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
//			  + "					WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
//			  + "					ELSE 'รอจัด Lot'  \r\n"
//			  + "					END AS [LotNo]  \r\n"
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
			  + "             , G.SendCFMCusDate, GRSumKG, GRSumYD, GRSumMR \r\n"  
			  + "			from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
//			  + "			left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n"
			  + "           left join ( \r\n"
			  + "				SELECT distinct a.SaleOrder ,a.SaleLine \r\n"
			  + "					, CASE  \r\n"
			  + "						WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
			  + "						WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
			  + "					   	WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
			  + "						WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
			  + "						ELSE 'รอจัด Lot'  \r\n"
			  + "						END AS [ProductionOrder]  \r\n"
			  + "				 	, CASE  \r\n"
			  + "						WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
			  + "						WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
			  + "					   	WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
			  + "						WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
			  + "						ELSE 'รอจัด Lot'  \r\n"
			  + "						END AS [LotNo]   \r\n"
			  + "				 	, [TotalQuantity] ,[Unit]\r\n"
			  + "				 	, [RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
			  + "				 	, a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
			  + "				 	, [Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
			  + "				 	, [OrderPuang] ,[RefPrd],b.[GreigeInDate] ,[BCDate],b.[Volumn]\r\n"
			  + "				 	, [CFdate],[CFType],[Shade],b.[LotShipping],[BillSendQuantity] \r\n"
			  + "				 	, [PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG] \r\n"
			  + "				from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			  + "				left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
			  + "			   	left join ( SELECT distinct [SaleOrder],[SaleLine] ,count([DataStatus]) as [CheckBill] \r\n"
			  + "							FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			  + "							where DataStatus = 'O'\r\n"
			  + "						    group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine]  \r\n" 
			  + "			) as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
			  + "			left join ( SELECT a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
			  + "                       from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
			  + "						left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "			            WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z')\r\n"
			  + "			            group by a.ProductionOrder) as t on b.ProductionOrder = t.ProductionOrder   \r\n"
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
			  + "						group by a.ProductionOrderRP) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n"
			  + "           left join ( SELECT distinct [SaleOrder],[SaleLine] ,count([DataStatus]) as [CheckBill] \r\n"
			  + "						FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			  + "						where DataStatus = 'O'\r\n"
			  + "					    group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine] \r\n"
			  + "           left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"
			  + "           left join ( SELECT distinct  a.id,a.[ProductionOrder] \r\n"
			  + "  			                  ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
			  + "			            FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
			  + "			            inner join (select [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([id]) as [MaxId]\r\n"
			  + "				                    FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
			  + "				                    group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b on a.[id] = b.[MaxId] \r\n"
			  + "			) as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n"
			  + "           left join ( SELECT distinct \r\n"
			  + "						   [ProductionOrder] \r\n"
			  + "						  ,[Grade] \r\n"
			  + "						  ,[PriceSTD]\r\n"
			  + "						  ,sum([QuantityMR]) as GRSumMR\r\n"
			  + "						  ,sum([QuantityKG]) as GRSumKG\r\n"
			  + "						  ,sum([QuantityYD]) as GRSumYD\r\n"
			  + "					    FROM [PCMS].[dbo].[FromSapGoodReceive]\r\n"
			  + "					    where datastatus = 'O'\r\n"
			  + "					    GROUP BY ProductionOrder,Grade ,[PriceSTD]) as m on b.ProductionOrder = m.ProductionOrder \r\n"
			  + "           "+this.leftJoinUCAL;
//			  + "			) as b on  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n";
	 private String leftJoinBPartTwo = 
			    " ) as b on  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n";
//	private String leftJoinBCaseTwo =    
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
//			+ "			) as b on  a.ProductionOrder = b.ProductionOrder \r\n";
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
  		+ "					        from [PCMS].[dbo].[FromSapCFM] as a\r\n"
  		+ "							where  a.DataStatus <> 'X'  and [CFMNumber] <> ''\r\n"
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
	private String leftJoinTempG = 
			" left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"  ;   
	private String leftJoinH = 
			"           left join ( SELECT distinct a.id,a.[ProductionOrder] \r\n"
		  + "  			                  			, a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
		  + "			            FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
		  + "			            inner join (select [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([id]) as [MaxId]\r\n"
		  + "				                    FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
		  + "				                    group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b on a.[id] = b.[MaxId] \r\n"
		  + "			) as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n"
		  ;   
	private String leftJoinX = " left join [PCMS].[dbo].[FromSapMainProdSale] as x on a.SaleLine = x.SaleLine and x.SaleOrder = a.SaleOrder  \r\n";
	private String leftJoinZ = 
		      " left join (SELECT distinct [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,max([LotShipping]) as [LotShipping]\r\n  "
			+ "            FROM [PCMS].[dbo].[FromSapMainGrade] \r\n"
			+ "			   where DataStatus = 'O' \r\n"
			+ "            GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) \r\n  "
			+ "  as z on z.ProductionOrder = b.ProductionOrder \r\n and "
			+ "			 z.SaleLine = b.SaleLine and\r\n "
			+ "       	 z.SaleOrder = b.SaleOrder \r\n";
//	private String leftJoinIToP = 
//			  " left join ( select distinct a.ProductionOrder , WorkDate \r\n"
//			+ "              from  [PPMM].[dbo].[OperationWorkDate]  as a\r\n"
//			+ "              inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			+ "                          from  [PPMM].[dbo].[DataFromSap] \r\n"
//			+ "                          where Operation >= 100 and Operation <= 103\r\n"
//			+ "                          group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			+ "              where a.Operation >= 100 and a.Operation <= 103 ) as i on b.ProductionOrder = i.ProductionOrder\r\n"
//			+ " left join ( select a.ProductionOrder , OperationEndDate as DyeActual,c.DyeingStatus as DyeStatus\r\n"
//			+ "            from  [PPMM].[dbo].[DataFromSap]  as a\r\n"
//			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
//			+ "                        where Operation >= 100 and Operation <= 103\r\n"
//			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			+ "				left join [PPMM].[dbo].[ShopFloorControlDetail] as c on a.ProductionOrder = c.ProductionOrder and c.Operation = b.Operation \r\n"
//			+ "            where a.Operation >= 100 and a.Operation <= 103 \r\n"
//			+ "            ) as j on b.ProductionOrder = j.ProductionOrder\r\n"
//			+ " left join ( select distinct a.ProductionOrder , OperationEndDate as Dryer \r\n"
//			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
//			+ "                        where Operation >= 140 and Operation <= 143\r\n"
//			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			+ "            where a.Operation >= 140 and a.Operation <= 143 )  as k on b.ProductionOrder = k.ProductionOrder\r\n"
//			+ " left join ( select distinct a.ProductionOrder , OperationEndDate as Finishing \r\n"
//			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
//			+ "                        where Operation >= 190 and Operation <= 193\r\n"
//			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			+ "            where a.Operation >= 190 and a.Operation <= 193  ) as l on b.ProductionOrder = l.ProductionOrder\r\n"
//			+ " left join ( select distinct a.ProductionOrder , OperationEndDate as Inspectation \r\n"
//			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			+ "            inner join ( select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			+ "                         from  [PPMM].[dbo].[DataFromSap] \r\n"
//			+ "                         where Operation >= 199 and Operation <= 200\r\n"
//			+ "                         group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			+ "            where a.Operation >= 199 and a.Operation <= 200 ) as m on b.ProductionOrder = m.ProductionOrder \r\n"
//			+ " left join ( select ProductionOrder , OperationEndDate as Prepare  \r\n"
//			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			+ "            where a.Operation  = 5 ) as n on b.ProductionOrder = n.ProductionOrder \r\n"
//			+ " left join ( select distinct ProductionOrder , OperationEndDate as Preset  \r\n"
//			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			+ "            where a.Operation = 50  ) as o on b.ProductionOrder = o.ProductionOrder \r\n "
//			+ " left join ( select distinct ProductionOrder , OperationEndDate as Relax  \r\n"
//			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
//			+ "            where a.Operation = 20  ) as p on b.ProductionOrder = p.ProductionOrder \r\n" ;
//	private String leftJoinBS = 
//		      " left join "
//			+ "           ("
//			+ "            SELECT [ProductionOrder] ,SUM([QuantityKG]) AS SumBillKG ,SUM([QuantityYD]) AS SumBillKG ,SUM([QuantityMR]) AS SumBillKG\r\n"
//			+ "  			FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
//			+ "  			WHERE [DataStatus]  = 'O' and [ProductionOrder] <> '' \r\n"
//			+ "  			GROUP BY [ProductionOrder] "
//			+ ")\r\n" 
//			+ "  as BS on b.ProductionOrder = BS.ProductionOrder \r\n";

	private String leftJoinCSW = 
			    " LEFT JOIN ( SELECT [SaleOrderSW] ,[SaleLineSW] ,count ([ProductionOrderSW]) as countSW \r\n"
			  + "			  FROM [PCMS].[dbo].[SwitchProdOrder] as a\r\n"
			  + "		      left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "			  WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' or b.UserStatus <> 'ตัดเกรด Z')\r\n"
			  + "			  GROUP BY  [SaleOrderSW] ,[SaleLineSW]\r\n"
			  + " ) as CSW on  CSW.[SaleOrderSW] = a.SaleOrder and CSW.[SaleLineSW] = a.SaleLine\r\n" ;
	private String leftJoinCRP = 
			   " LEFT JOIN ( SELECT a.[SaleOrder] ,a.[SaleLine]  ,COUNT ([ProductionOrderRP]) as countnRP  \r\n"
			 + "			 FROM [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
			 + "			 left join [PCMS].[dbo].[FromSapMainProd] as b on  a.[ProductionOrderRP] = b.ProductionOrder  \r\n"
			 + "			 WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' or b.UserStatus <> 'ตัดเกรด Z')\r\n"
			 + "			 GROUP BY  a.[SaleOrder] ,a.[SaleLine]\r\n"
			 + " )  as CRP on  CRP.SaleOrder = a.SaleOrder and CRP.SaleLine = a.SaleLine\r\n" ;
	private String leftJoinCOP = 
			    " LEFT JOIN ( SELECT a.[SaleOrder] ,a.[SaleLine] ,COUNT(a.[ProductionOrder]) as countnOP\r\n"
			  + "			  FROM [PCMS].[dbo].[FromSapMainProdSale] as a\r\n"
			  + "			  left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "			  WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' or b.UserStatus <> 'ตัดเกรด Z')\r\n"
			  + "			  GROUP BY  a.[SaleOrder] ,a.[SaleLine]  \r\n"
			  + " ) as COP on  COP.SaleOrder = a.SaleOrder and COP.SaleLine = a.SaleLine  \r\n";
//	private String leftJoinQ = " left join [PCMS].[dbo].[InputSwitchRemark] as q on a.SaleLine = q.SaleLine and q.SaleOrder = a.SaleOrder  \r\n";
//	private String leftJoinR = " left join [PCMS].[dbo].[InputSwitchRemark] as r on a.SaleLine = r.SaleLine and r.SaleOrder = a.SaleOrder  \r\n"; 
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
		String where = " where  ";
		String whereUCALRP = ""; 
		String whereBMain = ""; 
		String whereBMainUserStatus = " where"; 
		String whereWaitLot = " where ";
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
		where += " MaterialNo like '" + materialNo  + "%' and\r\n" 
			   + " a.SaleOrder like '" + saleOrder + "%' \r\n"; 
		whereWaitLot +=    
				  " MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n"; 
		;
		if (!saleCreateDate.equals("")) {   
			String[] dateArray = saleCreateDate.split("-");
			where += " and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
				   + " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n"; 
			whereWaitLot += 
					  "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n"; 
		} 
		if(!saleNumber.equals("")) { 
			where += " and SaleNumber like '"+saleNumber+"%' \r\n" ;    
			whereWaitLot += " and SaleNumber like '"+saleNumber+"%' \r\n" ;    
		}
		if (!labNo.equals("")) {
			where += " and b.LabNo like '" + labNo + "%'  \r\n"; 
		}
		if (!articleFG.equals("")) {
			where += " and a.ArticleFG like '" + articleFG + "%'  \r\n";     
			whereWaitLot  += " and a.ArticleFG like '" + articleFG + "%'  \r\n";     
		}
		if (!designFG.equals("")) {
			where += " and a.DesignFG like '" + designFG + "%'  \r\n";  
			whereWaitLot += " and a.DesignFG like '" + designFG + "%'  \r\n";  
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
			whereWaitLot += tmpWhere; 
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

			whereWaitLot += tmpWhere; 
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
			whereWaitLot += tmpWhere; 
			}
		 
		if (!prdOrder.equals("")) {
			where += " and b.ProductionOrder like '" + prdOrder + "%'  \r\n "; 
		}
		if (!prdCreateDate.equals("")) {
			String[] dateArray = prdCreateDate.split("-");
			where += " and ( PrdCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " PrdCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) )  \r\n"; 
		}if(!dueDate.equals("")) {  
			String[] dateArray = dueDate.split("-");
			where += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ; 
			whereWaitLot += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ; 
		}
		if (!deliveryStatus.equals("")) {
			where += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n"; 
			whereWaitLot+= " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n"; 
		}
		if (!saleStatus.equals("")) {
			where += " and SaleStatus like '" + saleStatus + "%'  \r\n";  
			whereWaitLot+= " and SaleStatus like '" + saleStatus + "%'  \r\n";  
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
			whereWaitLot += tmpWhere; 
		}     
		whereUCALRP = where;
		whereWaitLot = where;

		whereBMain = where;
		String tmpWhereNoLotUCAL = "";
		if (userStatusList.size() > 0) {
			String tmpWhere = "";
			String tmpWhereWaitLot = "";
//			boolean cWaitLot = false;
//			boolean cSaleStock = false;
			tmpWhereNoLotUCAL = " and ( b.ProductionOrder is not null and ( \r\n";  
			tmpWhere += " and ( b.ProductionOrder is not null and ( \r\n";
			whereUCALRP += " and ( b.ProductionOrder is not null and ( \r\n";
			String text = "";
			for (int i = 0; i < userStatusList.size(); i++) {
				text = userStatusList.get(i); 
				if(text.equals("รอจัด Lot") || text.equals("ขาย stock") || text.equals("รับจ้างถัก") || text.equals("Lot ขายแล้ว")
				|| text.equals("พ่วงแล้วรอสวม")|| text.equals("รอสวมเคยมี Lot") )  { 
					tmpWhere += " b.LotNo = '" +text + "' ";
					whereUCALRP += " b.LotNo = '" +text + "' ";
					listUserStatus.add(" b.LotNo = '" +text + "' "); 
				} 
				else { 
//					where += " b.UserStatus = '" +text + "' "; 
					tmpWhere += "UCAL.UserStatusCal = '" +text + "' ";
					tmpWhereNoLotUCAL += "UCAL.UserStatusCal = '" +text + "' ";
					whereUCALRP += "UCALRP.UserStatusCal = '" +text + "' ";
					if (i != userStatusList.size() - 1) { 
						tmpWhereNoLotUCAL += " or ";     
					} ;
				} 
				if (i != userStatusList.size() - 1) {
					tmpWhere += " or ";   
					whereUCALRP += " or ";   
				} ;
			}
			int sizeUS = listUserStatus.size();
			if(sizeUS > 0) {
				whereWaitLot += " and ( \r\n" ; 
				for ( int c = 0 ; c < sizeUS;c++) {
					String str = listUserStatus.get(c);
					whereWaitLot += str;
					if (c != listUserStatus.size() - 1) {
						whereWaitLot += " or ";    
					} ;
				}
				whereWaitLot += " ) \r\n" ;  
			}  
			tmpWhere += ") 		) \r\n";  
			whereUCALRP += ") 		) \r\n";  
			tmpWhereNoLotUCAL += ") 		) \r\n";   
			where += tmpWhere;  
			whereBMainUserStatus += " A.SaleOrder <> '' "+  tmpWhere ;
			
//			whereBMainUserStatus += " b.DataStatus = 'O' "+ tmpWhere.replace("UCAL.UserStatusCal", "b.UserStatus");  
//			whereBMain = where.replace("UCAL.UserStatusCal", "b.UserStatus");
		}  
		String sqlWaitLot = 
				  " SELECT DISTINCT  \r\n" 
				+ this.selectWaitLot 	 
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n " 
				+ this.innerJoinWaitLotB
//				+ " inner join (select * , 'รอจัด Lot' AS LotNo\r\n"
//				+ "				, null as TotalQuantity  , null as UserStatus\r\n"
//				+ "				, null as LabStatus  \r\n"
//				+ "				, null as GreigeInDate \r\n"
//				+ "				, null as DyePlan  \r\n"
//				+ "				, null as DyeActual \r\n"
//				+ "				, null as Dryer \r\n"
//				+ "				, null as Finishing \r\n"
//				+ "				, null as Inspectation  \r\n"
//				+ "				, null as Prepare \r\n"
//				+ "				, null as Preset \r\n"
//				+ "				, null as Relax \r\n"
//				+ "				, null as CFMDateActual\r\n"
//				+ "				, null as CFMPlanDate  \r\n"
//				+ "				--, null as LotShipping \r\n"
//				+ "				, null as DyeStatus  \r\n"
//				+ "				, null as LabNo\r\n"
//				+ "             , null as PrdCreateDate\r\n"
//				+ "			    from [PCMS].[dbo].[ReplacedProdOrder] as a \r\n"
//				+ "			    where ProductionOrder = 'รอจัด Lot' and a.DataStatus = 'O') \r\n"
//				+ "              as b on a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine   \r\n" 
				+ this.leftJoinC  
				+ this.leftJoinH  
//				+ this.leftJoinR 
//				+ this.leftJoinM 
				+ whereWaitLot  
				+ " and ( SumVol = 'B' OR countProdRP > 0 ) \r\n";
//				+ where
				;
		String sqlMain = "SELECT DISTINCT \r\n " 
				+ this.selectMain 	
				+ "   , 'Main' as TypePrd \r\n"
				+ "   , 'Main' AS TypePrdRemark \r\n"   
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n "
				+ this.leftJoinBPartOne
				+ whereBMainUserStatus
				+ this.leftJoinBPartTwo  
//				+ this.leftJoinB
//				+ this.leftJoinC 
//				+ this.leftJoinTempG
//				+ this.leftJoinH  	
//				+ this.leftJoinR 
//				+ this.leftJoinM
//				+ this.leftJoinUCAL 
				+ this.leftJoinCSW  
				+ this.leftJoinCRP
				+ this.leftJoinCOP
				+ whereBMain     
//				+ " and R.ProductionOrderSW is null \r\n"
				+ " and ( b.SumVol <> 0 \r\n"
				+ "		or ( (  b.LotNo = 'รอจัด Lot'  or  b.LotNo = 'ขาย stock' or b.LotNo = 'รับจ้างถัก' or b.LotNo = 'Lot ขายแล้ว' \r\n"
				+ "				or b.LotNo = 'พ่วงแล้วรอสวม' or b.LotNo = 'รอสวมเคยมี Lot'  )  and b.SumVol = 0 \r\n"
//				+ "		     ( countnRP is null and countSW is null and countnOP is null ) \r\n" 
				+ "		     and ( countnRP is null )  \r\n"
				+ "        ) \r\n"
				+ "     or  RealVolumn = 0 \r\n"
				+ "     or ( b.UserStatus = 'ยกเลิก' or b.UserStatus = 'ตัดเกรด Z') \r\n"  
				+ "     )\r\n"        
				+ " and NOT EXISTS ( select distinct ProductionOrderSW\r\n"
				+ "				     FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA\r\n"
				+ "				     WHERE DataStatus = 'O' and BAA.ProductionOrderSW = B.ProductionOrder\r\n"
				+ "				   )\r\n   " ;
				// Order Puang
//				+ " union "
		String sqlOP =  
				  " SELECT DISTINCT \r\n  " 
				+ this.selectOP
				+ "   ,'OrderPuang' as TypePrd \r\n" 
    			+ "   ,TypePrdRemark \r\n"
    			+ " FROM ( SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
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
				+ "           , g.[DyeStatus]  , UCAL.UserStatusCal as UserStatus , g.SendCFMCusDate, GRSumKG, GRSumYD, GRSumMR \r\n"
				+ "           , g.CFMDetailAll \r\n"
				+ "           , g.CFMNumberAll  \r\n"
				+ "           , g.CFMRemarkAll \r\n"
				+ "           , g.RollNoRemarkAll \r\n"
				+ "           , g.CFMActualLabDate \r\n"
				+ "           , g.CFMCusAnsLabDate \r\n"
				+ "           , g.GreigeInDate"
				+ "		  from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
				+ "		  inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
				+ "       "+this.leftJoinTempG   
				+ "       "+this.leftJoinM   
				+ "       "+this.leftJoinUCAL  
				+ "       left join [PCMS].[dbo].[FromSapMainGrade] as FSMG on b.ProductionOrder = FSMG.ProductionOrder and FSMG.DataStatus = 'O'    \r\n"
				+ "		  where b.DataStatus <> 'X'  "
				+ "         "+tmpWhereNoLotUCAL
				+ "       ) as a  \r\n "  
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
//				+ this.leftJoinC	  
//				+ this.leftJoinTempG
//				+ this.leftJoinM
//				+ this.leftJoinUCAL 
				+ this.leftJoinH   
//				+ this.leftJoinR
//				+ where     
				+ whereBMain        
//				+ " and R.ProductionOrderSW is null \r\n"        
				+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"
				+ " and NOT EXISTS ( select distinct ProductionOrderSW\r\n"
				+ "				     FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA\r\n"
				+ "				     WHERE DataStatus = 'O' and BAA.ProductionOrderSW = A.ProductionOrder\r\n"
				+ "				   )\r\n"   ;
//				//// Order PuangSwitch
//				+ " union "
		String sqlOPSW = 
				  " SELECT DISTINCT  \r\n" 
				+ this.select 
				+ "   , 'OrderPuang' as TypePrd \r\n"  
    			+ "   , TypePrdRemark \r\n"
				+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
				+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n" 
				+ "           , 'SUB' as TypePrdRemark \r\n" 
				+ "           , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ "           , CASE WHEN b.Volumn <> 0 THEN b.Volumn \r\n"
				+ "                  ELSE  0 \r\n"     
				+ "                  END AS Volumn \r\n"
				+ "		   from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
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
				+ this.leftJoinC	  
				+ this.leftJoinTempG
				+ this.leftJoinH   
				+ this.leftJoinR
				+ this.leftJoinM
				+ this.leftJoinUCAL  
				+ where     
				+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"  ;
//////			// Switch   
//				+ " union "
			String sqlSW =  
				  " SELECT DISTINCT \r\n " 
				+ this.select 
				+ "   ,'Switch' as TypePrd \r\n"  
    			+ "   ,TypePrdRemark \r\n"
    			+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
				+ "	          , b.ProductionOrderSW as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
				+ "  		  , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ " 		  , CASE \r\n"
				+ "					when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
				+ "					ELSE 'SUB' \r\n"
				+ "			    	END	TypePrdRemark  ,C.SumVol\r\n" 
				+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
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
				+ this.leftJoinC	  
				+ this.leftJoinTempG
				+ this.leftJoinH 
				+ this.leftJoinR
				+ this.leftJoinM
				+ this.leftJoinUCAL  
				+ where     
				+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"  ;
//////			// สวม  
//				+ " union "
			String sqlRP = 
				  " SELECT DISTINCT  \r\n" 
				+ this.selectRP 
				+ "   , 'Replaced' as TypePrd \r\n"
    			+ "   , TypePrdRemark \r\n"
				+ " FROM (  SELECT DISTINCT  b.[SaleOrder]  , b.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
				+ "	          , b.[ProductionOrderRP] as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
				+ " 	 	  , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ "   		  , CASE  \r\n"
				+ "					WHEN b.Volume <> 0 THEN b.Volume\r\n"
				+ "					ELSE  c.Volumn\r\n"  
				+ "					END AS Volumn   \r\n"
				+ " 		 , 'SUB' as TypePrdRemark \r\n" 
				+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
				+ "		 	inner join [PCMS].[dbo].[ReplacedProdOrder] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder   \r\n"
				+ "		 	left join  [PCMS].[dbo].[FromSapMainProd] as c on c.ProductionOrder = b.[ProductionOrderRP]\r\n"
				+ "      	where b.DataStatus <> 'X') as a  \r\n "  
				+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
				+ this.leftJoinC	 
				+ this.leftJoinTempG
				+ this.leftJoinH    
				+ this.leftJoinR	
				+ this.leftJoinM			
//				+ this.leftJoinUCAL
				+ this.leftJoinUCALRP  
				+ whereUCALRP    
				+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"  
//				+ " Order by a.CustomerShortName, a.DueDate ,a.[SaleOrder], [SaleLine], [ProductionOrder]"
				;     
			 String sql =
					 ""
//					  this.createTempUCAL 				
//					+ this.createTempG
//					+ this.createTempUCALRP 
					+ this.createTempSumGR
					+ sqlWaitLot
					+ " union \r\n" 
					+  sqlMain       
					+ " union \r\n"			        
					+  sqlOP       
					+ " union \r\n" 
					+ sqlOPSW     
					+ " union \r\n"
					+ sqlSW 
					+ " union \r\n"
					+ sqlRP  
					+ " Order by a.CustomerShortName, a.DueDate,a.[SaleOrder], [SaleLine],b.[ProductionOrder] ";   
//		System.out.println(sql);  
//		 System.out.println(createTempUCAL);
//		System.out.println("Before SQL: "+new Date());
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		list = new ArrayList<PCMSTableDetail>(); 
//		System.out.println("AFTER SQL:  AFTER SQL LIST: "+new Date());
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}  
//		System.out.println("AFTER SQL LIST: "+new Date());
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
		String sql = 
//				this.createTempUCAL 
//					+this.createTempG+
				  " SELECT distinct top 1  \r\n " 
				+ this.selectTwo 
				+ " from  [PCMS].[dbo].FromSapMainSale as a \r\n "
//				+ " inner join [PCMS].[dbo].[FromSapMainProd] as b on a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n"
////				+ this.leftJoinF
//				+ this.leftJoinB

				+ this.leftJoinBPartOne
//				+ where
				+ this.leftJoinBPartTwo
				+ this.leftJoinTempG   
//				+ this.leftJoinH
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
		  + " union \r\n "
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
//		bean = new PCMSAllDetail();
//		bean.setUserStatus("รับจ้างถัก");
//		list.add(bean);
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
//		ArrayList<ColumnHiddenDetail> beanCheck = this.getColHiddenDetail(leftJoinB);
//		if(beanCheck.size() > 0) { 
//		} 
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
					  " UPDATE [dbo].[SearchSetting]\r\n"
					+ "  SET [No] = ?  ,[CustomerName] = ?,[CustomerShortName] = ?\r\n"
					+ "      ,[SaleOrder] = ? ,[ArticleFG] = ? ,[DesignFG] =  ? \r\n"
					+ "      ,[ProductionOrder] = ? ,[SaleNumber] = ? ,[MaterialNo] = ?\r\n"
					+ "      ,[LabNo] = ? ,[DeliveryStatus] = ? ,[DistChannel] = ?\r\n"
					+ "      ,[SaleStatus] = ? ,[DueDate] = ? ,[SaleCreateDate] = ? \r\n"
					+ "      ,[PrdCreateDate] = ? ,[UserStatus] = ? , [Division] = ? \r\n"
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
					  " INSERT INTO [dbo].[SearchSetting]\r\n"
					+ "           ( [EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
					+ "           ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
					+ "           ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
					+ "           ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[ForPage],[Division] \r\n"
					+ "           )\r\n"
					+ " VALUES\r\n"
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
				    " SELECT \r\n"    
				  + "   [EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
				  + "   ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
				  + "   ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
				  + "   ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[Division]\r\n"
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
//	public String handlerUserStatus(String userStatusPara,String dist ,String gradePrd , String qaApproveBy,
//			String finishGR,String lastCFMStatus,String isAnita) {
//		String userStatus = "";
////		DM
//		if (gradePrd.equals("A") && !finishGR.equals("")) {
//			// CFM = 'N' AND QAAPPROVED = ''
//			if (lastCFMStatus.equals("N") && qaApproveBy.equals("")) { userStatus = "รอสรุป QA"; }
//			// CFM = 'Y' AND QAAPPROVED = ''
//			else if (lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอ COA ลูกค้า ok สี"; }
//			// CFM = ''
//			else if (lastCFMStatus.equals("")) { userStatus = "รอตอบ CFM"; }
//			// CFM = 'Y' AND QAAPPROVED = 'XX/XX/XXXX'
//			else if (lastCFMStatus.equals("Y") && !qaApproveBy.equals("")) { userStatus = "รอขาย"; }
//		} else if (gradePrd.equals("C") && !finishGR.equals("")) {
//			// CFM = 'N' AND QAAPPROVED = ''
//			if (lastCFMStatus.equals("N") && qaApproveBy.equals("")) { userStatus = "รอสรุป QA"; }
//			// CFM = 'Y' AND QAAPPROVED = ''
//			else if (lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอ COA ลูกค้า ok สี"; } 
//			// CFM = ''
//			else if (lastCFMStatus.equals("")) { userStatus = "รอตอบ CFM"; } 
//			// CFM = 'Y' AND QAAPPROVED = 'XX/XX/XXXX'
//			else if (lastCFMStatus.equals("Y") && !qaApproveBy.equals("")) { userStatus = "รอขาย"; } 
//			// CFM = 'Y' AND userStatusParaSAP = 'Hold รอโอน'
////			else if (userStatusPara.equals("Hold รอโอน")) { userStatus = "Hold รอโอน"; } 
//		} else if (gradePrd.equals("Z")) { userStatus = "ตัดเกรด Z"; } 
//
////		EX
//		if (gradePrd.equals("A") && !finishGR.equals("")) {
//			// CFM = 'N' AND QAAPPROVED = ''
//			if (lastCFMStatus.equals("N") && qaApproveBy.equals("")) { userStatus = "รอสรุป QA"; }
//			else if (lastCFMStatus.equals("")) { userStatus = "รอตอบ CFM"; }
//			// CUSTOMER = 'ANITA' CFM = 'Y' AND QAAPPROVED = ''
//			else if (isAnita.equals("Y") && lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอตอบ CFM ตัวแทน"; }
//			// CUSTOMER <> 'ANITA' CFM = 'Y' AND QAAPPROVED = ''
//			else if (isAnita.equals("N") && lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอ COA ลูกค้า ok สี"; } 
//			// CFM = 'Y' AND QAAPPROVED = 	'XX/XX/XXXX'
//			else if (lastCFMStatus.equals("Y") && !qaApproveBy.equals("")) { userStatus = "รอแจ้งส่ง"; }
//		} else if (gradePrd.equals("C") && !finishGR.equals("")) {
//			// CFM = 'N' AND QAAPPROVED = ''
//			if (lastCFMStatus.equals("N") && qaApproveBy.equals("")) { userStatus = "รอสรุป QA"; }
//			// CUSTOMER = 'ANITA' CFM = 'Y' AND QAAPPROVED = ''
//			else if (isAnita.equals("Y") && lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอตอบ CFM ตัวแทน"; }
//			// CUSTOMER <> 'ANITA' CFM = 'Y' AND QAAPPROVED = ''
//			else if (isAnita.equals("N") && lastCFMStatus.equals("Y") && qaApproveBy.equals("")) { userStatus = "รอ COA ลูกค้า ok สี "; } 
//			else if (lastCFMStatus.equals("") ) { userStatus = "รอตอบ CFM"; }
//			// CFM = 'Y'AND QAAPPROVED = 'XX/XX/XXXX'
//			else if (lastCFMStatus.equals("Y")  && !qaApproveBy.equals("")) { userStatus = "รอแจ้งส่ง";  }   
//		} else if (gradePrd.equals("Z") && !finishGR.equals("")) { userStatus = "ตัดเกรด Z"; } 
//		return userStatus;
//	} 
}
