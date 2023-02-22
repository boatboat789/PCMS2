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
import entities.DelayedDepartmentDetail;
import entities.InputDateDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import entities.ReplacedProdOrderDetail;
import entities.SwitchProdOrderDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

public class PCMSDetailDaoImpl implements PCMSDetailDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String C_PRODORDER = "ProductionOrder";
	private String C_PRODORDERRP = "ProductionOrderRP";
	private String OPEN_STATUS = "O";
	private String CLOSE_STATUS = "X";
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
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
//  		  + ", CFMSendDate \r\n"
//  		  + ", CFMAnswerDate \r\n"
//  		  + ", CFMStatus \r\n"
//  		  + ", CFMNumber  \r\n"
//  		  + ", CFMRemark \r\n" 
	 	  
		   + "   ,CFMDateActual\r\n"                    
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
  		  + "   , b.ProductionOrder \r\n"
  		  + "   , GRSumKG \r\n"
  		  + "   , GRSumYD \r\n"
  		  + "   , GRSumMR \r\n"
  		  + "   , CASE \r\n"
  		  + "		WHEN ( DyePlan is NULL) THEN null \r\n"
  		  + "       ELSE CAST(DAY( DyePlan) AS VARCHAR(2)) + '/' +   CAST(MONTH( DyePlan)AS VARCHAR(2))  \r\n"
  		  + "  		END AS DyePlan \r\n "	
  		  + "   , CASE \r\n"
  		  + "		WHEN ( DyeActual is NULL) THEN null \r\n"
  		  + "       ELSE CAST(DAY( DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( DyeActual)AS VARCHAR(2))  \r\n"
  		  + "  		END AS DyeActual \r\n "				
//  		  + ", DyePlan \r\n"
//  		  + ", DyeActual  \r\n" 
  		  + "   , PCRemark\r\n" 
	      + "   , [DelayedDep] \r\n"
	      + "   , [CauseOfDelay] \r\n"
  		  + "   , [SwitchRemark]\r\n"
  		  + "   , SL.[StockLoad] \r\n"
  		  + "   , [PrdCreateDate]\r\n"
  		  + "   , Volumn \r\n"
  		  + "   , VolumnFGAmount  \r\n"  
  		  + "   , 'WaitLot' as TypePrd \r\n"
  		  + "   , 'WaitLot' AS TypePrdRemark  \r\n"; 
	private String selectOP = 
  		  "   a.SaleOrder ,\r\n"
  		+ "	  CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
  		+ "		WHEN 0 THEN '' \r\n"
  		+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) "
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
  		+ "   j.CFMDate as TKCFM,\r\n"  
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
  		+ "   b.ProductionOrder , \r\n"
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
		+ "   b.[PrdCreateDate]\r\n"; 
    private String select = 
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
    		+ "   g.CFMActualLabDate,\r\n"
    		+ "   g.CFMCusAnsLabDate,\r\n"
    		+ "   a.UserStatusCal as UserStatus,\r\n"
    		+ "   j.CFMDate as TKCFM,\r\n"  
    		+ "   g.CFMPlanDate AS CFMPlanDate ,  \r\n"
//    		+ "   g.SendCFMCusDate, \r\n"
    		+ "   CASE \r\n "
    		+ "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> '' THEN SCC.SendCFMCusDate \r\n"
   		  	+ "     ELSE  g.SendCFMCusDate \r\n"     
   		  	+ "    	END AS SendCFMCusDate ,\r\n"
    		+ "   CASE \r\n"
    		+ "		WHEN h.[ProductionOrder] is not null \r\n"
    		+ "		THEN H.DeliveryDate ELSE b.CFTYPE \r\n"
    		+ "		END AS DeliveryDate , \r\n"
//    		+ "   g.CFMSendDate,\r\n" 
//    		+ "   g.CFMAnswerDate,\r\n" 
//      		+ "   g.CFMStatus,\r\n" 
//      		+ "   g.CFMNumber,\r\n" 
//      		+ "   g.CFMRemark,\r\n"  
 	    	+ "   CFMDateActual,\r\n"   
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
    		+ "   b.ProductionOrder , \r\n"
    		+ "   GRSumKG,\r\n"
    		+ "   GRSumYD,\r\n"   
    		+ "   GRSumMR,\r\n"   
  		    + "   CASE \r\n"
  		    + "		WHEN ( g.DyePlan is NULL) THEN null \r\n"
  		    + "     ELSE CAST(DAY( g.DyePlan) AS VARCHAR(2)) + '/' + CAST(MONTH( g.DyePlan)AS VARCHAR(2))  \r\n"
  		    + "     END AS DyePlan , \r\n "	
  		    + "   CASE \r\n"
  		    + "		WHEN ( g.DyeActual is NULL) THEN null \r\n"
  		    + "     ELSE CAST(DAY( g.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( g.DyeActual)AS VARCHAR(2))  \r\n"
  		    + "     END AS DyeActual , \r\n "	
//    		+ "   g.DyePlan , \r\n"
//			+ "   g.DyeActual, \r\n"
			+ "   PCRemark, \r\n"
	  		+ "   [DelayedDep], \r\n"
	  		+ "   [CauseOfDelay], \r\n"
			+ "   q.[SwitchRemark],\r\n"
			+ "   SL.[StockLoad], \r\n"
			+ "   b.[PrdCreateDate]\r\n"; 
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
  		+ "   j.CFMDate as TKCFM,\r\n"  
  		+ "   g.CFMPlanDate AS CFMPlanDate ,  \r\n"
//  		+ "   g.SendCFMCusDate, \r\n"
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
  		+ "   StockRemark,\r\n"   
  		+ "   b.ProductionOrder , \r\n"
  		+ "   GRSumKG,\r\n"
  		+ "   GRSumYD,\r\n"   
  		+ "   GRSumMR,\r\n"   
		    + "   CASE \r\n"
		    + "		WHEN ( g.DyePlan is NULL) THEN null \r\n"
		    + "     ELSE CAST(DAY( g.DyePlan) AS VARCHAR(2)) + '/' + CAST(MONTH( g.DyePlan)AS VARCHAR(2))  \r\n"
		    + "     END AS DyePlan , \r\n "	
		    + "   CASE \r\n"
		    + "		WHEN ( g.DyeActual is NULL) THEN null \r\n"
		    + "     ELSE CAST(DAY( g.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( g.DyeActual)AS VARCHAR(2))  \r\n"
		    + "     END AS DyeActual , \r\n "	
//  		+ "   g.DyePlan , \r\n"
//			+ "   g.DyeActual, \r\n"
			+ "   PCRemark, \r\n"
	  		+ "   [DelayedDep], \r\n"
	  		+ "   [CauseOfDelay], \r\n"
			+ "   q.[SwitchRemark],\r\n"
			+ "   SL.[StockLoad], \r\n"
			+ "   b.[PrdCreateDate]\r\n"; 
    private String selectMain = 
  		  "   a.SaleOrder, \r\n"
  		+ "   CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
  		+ "		WHEN 0 THEN '' ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) \r\n"
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
//  		+ "   lb.[CFMDate]  as CFMActualLabDate,\r\n"
//  		+ "   lb.[CFMAnswerDate] as CFMCusAnsLabDate,\r\n"

		+ "   b.CFMActualLabDate,\r\n"
		+ "   b.CFMCusAnsLabDate,\r\n"
  		+ "   b.UserStatus,\r\n"
  		+ "   j.CFMDate as TKCFM,\r\n"  
  		+ "   b.CFMPlanDate AS CFMPlanDate ,  \r\n"
  		+ "   b.SendCFMCusDate,\r\n"
  		+ "   CASE \r\n"
  		+ "		WHEN b.[ProductionOrder] is not null THEN b.DeliveryDate \r\n"
  		+ "		ELSE b.CFTYPE \r\n"
  		+ "		END AS DeliveryDate , \r\n" 
//  		+ "   b.CFMSendDate,\r\n" 
//  		+ "   b.CFMAnswerDate,\r\n" 
//  		+ "   b.CFMStatus,\r\n" 
//  		+ "   b.CFMNumber,\r\n" 
//  		+ "   b.CFMRemark,\r\n"
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
  		+ "   b.ProductionOrder ,\r\n "
  		+ "   GRSumKG,\r\n"
  		+ "   GRSumYD,\r\n"   
  		+ "   GRSumMR,\r\n"  
  		+ "   CASE \r\n"
  		+ "		WHEN ( b.DyePlan is NULL) THEN null \r\n"
		+ "    	ELSE CAST(DAY( b.DyePlan) AS VARCHAR(2)) + '/' +   CAST(MONTH( b.DyePlan)AS VARCHAR(2))  \r\n"
		+ "     END AS DyePlan ,\r\n "	
		+ "   CASE \r\n"
		+ "		WHEN ( b.DyeActual is NULL) THEN null \r\n"
		+ "     ELSE CAST(DAY( b.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( b.DyeActual)AS VARCHAR(2))  \r\n"
		+ "     END AS DyeActual ,\r\n "	
//  		+ "   b.DyePlan , \r\n"
//		+ "   b.DyeActual, \r\n"
		+ "   PCRemark,\r\n"
		+ "   [DelayedDep],\r\n"
		+ "   [CauseOfDelay],\r\n"
		+ "   q.[SwitchRemark],\r\n"
		+ "   SL.[StockLoad],\r\n"
		+ "   b.[PrdCreateDate] \r\n"; 
    private String selectRP = 
  		  "   a.SaleOrder,\r\n"
  		+ "   CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
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
  		+ "   m.Grade,\r\n"   
//  		+ "   c.BillSendWeightQuantity,\r\n"
//  		+ "   c.BillSendQuantity,\r\n"  
//  		+ "   c.BillSendMRQuantity,\r\n"  
//  		+ "   c.BillSendYDQuantity,\r\n"  

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
//  		+ "   lb.[CFMDate]  as CFMActualLabDate,\r\n"
//  		+ "   lb.[CFMAnswerDate] as CFMCusAnsLabDate,\r\n" 
		+ "   g.CFMActualLabDate,\r\n"
		+ "   g.CFMCusAnsLabDate,\r\n"
  		+ "   UCALRP.UserStatusCalRP as UserStatus,\r\n"
  		+ "   j.CFMDate as TKCFM,\r\n"  
  		+ "   g.CFMPlanDate AS CFMPlanDate ,  \r\n"
//  		+ "   g.SendCFMCusDate , \r\n"
  		+ "   CASE \r\n"
  		+ "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
  		+ "     ELSE  g.SendCFMCusDate \r\n"     
  		+ "    	END AS SendCFMCusDate,\r\n"
  		+ "   CASE \r\n"
  		+ "		WHEN h.[ProductionOrder] is not null THEN H.DeliveryDate \r\n"
  		+ "		ELSE b.CFTYPE \r\n"
  		+ "		END AS DeliveryDate , \r\n" 
//  		+ "   g.CFMSendDate,\r\n" 
//  		+ "   g.CFMAnswerDate,\r\n" 
//  		+ "   g.CFMStatus,\r\n" 
//  		+ "   g.CFMNumber,\r\n"    
//  		+ "   g.CFMRemark,\r\n" 
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
  		+ "   b.ProductionOrder , \r\n"
  		+ "   GRSumKG,\r\n"
  		+ "   GRSumYD,\r\n"   
  		+ "   GRSumMR,\r\n"   
  		+ "   CASE \r\n"
  		+ "		WHEN ( g.DyePlan is NULL) THEN null \r\n"
		+ "     ELSE CAST(DAY( g.DyePlan) AS VARCHAR(2)) + '/' + CAST(MONTH( g.DyePlan)AS VARCHAR(2))  \r\n"
		+ "     END AS DyePlan, \r\n "	
		+ "   CASE \r\n"
		+ "		WHEN ( g.DyeActual is NULL) THEN null \r\n"
		+ "     ELSE CAST(DAY( g.DyeActual) AS VARCHAR(2)) + '/' + CAST(MONTH( g.DyeActual)AS VARCHAR(2))  \r\n"
		+ "     END AS DyeActual, \r\n "
//		+ "   g.DyePlan , \r\n"
//		+ "   g.DyeActual, \r\n"
		+ "   PCRemark,\r\n"
		+ "   [DelayedDep],\r\n"
		+ "   [CauseOfDelay],\r\n"
		+ "   q.[SwitchRemark],\r\n"
		+ "   SL.[StockLoad],\r\n"
		+ "   b.[PrdCreateDate]\r\n"; 
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
   		 + "			--, 'รอจัด Lot' AS ProductionOrder \r\n"
   		 + "        ,null as TotalQuantity \r\n"
   		 + "		,'' as Grade \r\n"
   		 + "		,null as BillSendWeightQuantity \r\n"
   		 + "		,null as BillSendQuantity  \r\n"
   		 + "		,null as BillSendMRQuantity \r\n"
   		 + "		,null as BillSendYDQuantity  \r\n"
   		 + "		,'' as LabNo\r\n"
   		 + "		,'' as LabStatus\r\n"
   		 + "		,null as CFMPlanLabDate\r\n"
   		 + "		,null as CFMActualLabDate \r\n"
   		 + "		,null as CFMCusAnsLabDate \r\n"
   		 + "		,'' as UserStatus \r\n"
   		 + "		,null as TKCFM \r\n"
   		 + "		,null as CFMPlanDate \r\n"
   		 + "		,null as SendCFMCusDate \r\n"
   		 + "		,null as DeliveryDate  \r\n"
   		 + "		,null as CFMSendDate \r\n"
   		 + "		,null as CFMAnswerDate \r\n"
   		 + "		,'' as CFMStatus \r\n"
   		 + "		,'' as CFMNumber  \r\n"
   		 + "		,'' as CFMRemark  \r\n"
   		 + "		,'' as RemarkOne \r\n"
   		 + "		,'' as RemarkTwo \r\n"
   		 + "		,'' as RemarkThree  \r\n"
   		 + "		,'' as StockRemark \r\n"
   		 + "		,null as  GRSumKG \r\n"
   		 + "		,null as  GRSumYD \r\n"
   		 + "		,null as  GRSumMR \r\n"
   		 + "		,null as  DyePlan \r\n"
   		 + "		,null as DyeActual   \r\n"
   		 + "		,'' as [SwitchRemark] \r\n"
   		 + "		,null as [PrdCreateDate]\r\n"
   		 + "		,NULL AS Volumn   \r\n"
   		 + "		,NULL AS VolumnFGAmount  	\r\n"
   		 + "		,'' as CFMDetailAll \r\n"
   		 + "		,'' as CFMNumberAll  \r\n"
   		 + "		,'' as CFMRemarkAll \r\n"
	     + "        ,'' as RollNoRemarkAll\r\n"
	     + "        ,null as CFMDateActual\r\n" 
   		 + "	from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
   		 + "	left join ( SELECT DISTINCT a.SaleOrder , A.SaleLine ,ISNULL(SumVolOP, 0 ) + ISNULL(SumVolRP, 0 ) as SumVolUsed --,ISNULL(SumVolRP, 0 )\r\n"
   		 + "							   ,SumVolOP,SumVolRP\r\n"
   		 + "				FROM[PCMS].[dbo].[FromSapMainProd]  AS A\r\n"
   		 + "				LEFT JOIN (  \r\n"
   		 + "					SELECT DISTINCT a.[ProductionOrder] \r\n"
   		 + "								   ,sum (A.[Volumn]) as SumVolOP \r\n"
   		 + "					FROM [PCMS].[dbo].[FromSapMainProdSale] AS A \r\n"
   		 + "					left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
   		 + "					WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"
   		 + "					GROUP BY A.[ProductionOrder] ) AS D ON D.ProductionOrder = A.ProductionOrder\r\n"
   		 + "				LEFT JOIN (   \r\n"
   		 + "					SELECT a.ProductionOrderRP , sum(c.Volume) as SumVolRP  \r\n"
   		 + "					from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
   		 + "					left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
   		 + "					left join ( SELECT ProductionOrderRP  \r\n"
   		 + "								     , CASE  \r\n"
   		 + "									   		WHEN ( Volume = 0 ) THEN b.Volumn  \r\n"
   		 + "											ELSE a.Volume\r\n"
   		 + "											END AS Volume  \r\n"
   		 + "								from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
   		 + "								left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
   		 + "								WHERE a.[DataStatus] = 'O' \r\n"
   		 + "							 ) as c on a.ProductionOrderRP = c.ProductionOrderRP\r\n"
   		 + "					WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z')  \r\n"
   		 + "					group by a.ProductionOrderRP\r\n"
   		 + "					) AS E ON A.ProductionOrder = E.ProductionOrderRP	 \r\n"
   		 + "	)  as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n"
   		 + "	LEFT JOIN ( \r\n"
   		 + "				SELECT [SaleOrder] ,[SaleLine]  ,sum( [Volumn] ) as SumVolMain \r\n"
   		 + "				FROM [PCMS].[dbo].[FromSapMainProd]\r\n"
   		 + "				WHERE DataStatus = 'O'\r\n"
   		 + "				group by  [SaleOrder]  ,[SaleLine] ) AS C ON A.SaleOrder = C.SaleOrder AND A.SaleLine = C.SaleLine \r\n"
   		 + "	left join ( select DISTINCT SaleOrder ,SaleLine ,COUNT(ProductionOrder) AS CountProdRP\r\n"
   		 + "				from [PCMS].[dbo].[ReplacedProdOrder] \r\n"
   		 + "				where DataStatus = 'O' and  ProductionOrder = 'รอจัด Lot' \r\n"
   		 + "				GROUP BY  SaleOrder ,SaleLine \r\n"
   		 + "			) AS D ON A.SaleOrder = D.SaleOrder AND A.SaleLine = D.SaleLine  \r\n"
   		 + "	where ( c.SumVolMain > 0   ) OR D.SaleOrder IS NOT NULL \r\n"
   		 + " ) AS b ON a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n" ;
//    private String leftJoinB = " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n";
    private String leftJoinUCAL =  ""
    		+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCAL on b.ProductionOrder = UCAL.ProductionOrder AND (  m.Grade = UCAL.Grade OR  m.Grade IS NULL )  \r\n"; 
    private String leftJoinUCALRP = "    "
    		+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCALRP on b.ProductionOrder = UCALRP.ProductionOrder AND ( m.Grade = UCALRP.Grade OR m.Grade IS NULL )    \r\n";
    private String leftJoinSCC = ""
    		+ " left join [PCMS].[dbo].[PlanSendCFMCusDate] as SCC on b.ProductionOrder = SCC.ProductionOrder and SCC.DataStatus = 'O'    \r\n";
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
//    private String leftJoinBPartOne =    
//			  " left join  ( SELECT distinct a.[SaleOrder] , a.[Saleline]  ,[TotalQuantity] ,[Unit]\r\n"
//			  + "			 ,[RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
//			  + "			 ,a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
//			  + "			 ,[Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
//			  + "			 ,[OrderPuang] ,[RefPrd],b.[GreigeInDate] ,[BCDate],b.[Volumn]\r\n"
//			  + "			 ,[CFdate],[CFType],[Shade],b.[LotShipping],[BillSendQuantity],m.[Grade] \r\n"
//			  + "			 ,[PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG]\r\n"
//			  + "            ,b.[ProductionOrder] ,b.[LotNo] \r\n " 
//			  + " 			 ,CASE  \r\n"
//			  + " 					WHEN ( s.SumVolRP is not null AND t.SumVolOP is not null ) THEN (  b.Volumn -  s.SumVolRP -  t.SumVolOP)\r\n"
//			  + "					WHEN ( s.SumVolRP is not null AND t.SumVolOP is null ) THEN (  b.Volumn -  s.SumVolRP )\r\n"
//			  + "					WHEN ( s.SumVolRP is null AND t.SumVolOP is not null ) THEN (  b.Volumn -  t.SumVolOP) \r\n"
//			  + "					WHEN  b.Volumn is not null THEN  b.Volumn\r\n"
//			  + "				    ELSE   0\r\n"
//			  + "	 				END AS SumVol   \r\n"
//			  + " 			 , CASE  \r\n"
//			  + "				 	WHEN ( s.SumVolRP is not null AND t.SumVolOP is not null ) THEN a.Price * (  b.Volumn -  s.SumVolRP -  t.SumVolOP)\r\n"
//			  + "				 	WHEN ( s.SumVolRP is not null AND t.SumVolOP is null ) THEN a.Price * (  b.Volumn -  s.SumVolRP )\r\n"
//			  + "				 	WHEN ( s.SumVolRP is null AND t.SumVolOP is not null ) THEN a.Price * (  b.Volumn -  t.SumVolOP)   \r\n"
//			  + "				 	WHEN  b.Volumn is not null THEN a.Price * b.Volumn\r\n"
//			  + "					ELSE   0\r\n"
//			  + "				 	END AS SumVolFGAmount   \r\n"
//			  + "              ,s.SumVolRP,t.SumVolOP,b.Volumn as RealVolumn\r\n"
//			  + "              , g.[DyePlan]  \r\n"
//			  + "	          , g.[DyeActual] , g.[Dryer] , g.[Finishing] , g.[Inspectation]  \r\n"
//			  + "             , g.[Prepare] , g.[Preset] , g.[Relax] , g.[CFMDateActual] , g.[CFMPlanDate]  \r\n"
//			  + "             , g.[DyeStatus] , h.DeliveryDate, 	UCAL.UserStatusCal as UserStatus "
//			  + "             , g.SendCFMCusDate, GRSumKG, GRSumYD, GRSumMR \r\n " 
//			  + "           , g.CFMDetailAll \r\n"
//			  + "           , g.CFMNumberAll  \r\n"
//			  + "           , g.CFMRemarkAll \r\n" 
//	 		  + "           , g.RollNoRemarkAll \r\n"
//			  + "			from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
////			  + "			left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n"
//			  + "           left join ( select distinct a.[SaleOrder],a.[SaleLine] ,[TotalQuantity] ,[Unit]\r\n"
//			  + "			 , [RemAfterCloseOne],[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
//			  + "			 ,a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
//			  + "			 ,[Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
//			  + "			 ,[OrderPuang] ,[RefPrd],a.[GreigeInDate] ,[BCDate],a.[Volumn]\r\n"
//			  + "			 ,[CFdate],[CFType],[Shade],a.[LotShipping],[BillSendQuantity] \r\n"
//			  + "			 ,[PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG] \r\n"
//			  + "			 ,CASE  \r\n"
//			  + "					WHEN a.[ProductionOrder] is not null THEN a.[ProductionOrder]  \r\n"
//			  + "					WHEN SUBSTRING (b.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//			  + "                    WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
//			  + "					WHEN b.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
//			  + "					ELSE 'รอจัด Lot'  \r\n"
//			  + "					END AS [ProductionOrder]  \r\n"
//			  + "				,CASE  \r\n"
//			  + "					WHEN a.[ProductionOrder] is not null THEN a.[LotNo] \r\n"
//			  + "					WHEN SUBSTRING (b.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//			  + "                    WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
//			  + "					WHEN b.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
//			  + "					ELSE 'รอจัด Lot'  \r\n"
//			  + "					END AS [LotNo]   \r\n"
//			  + "			from [PCMS].[dbo].[FromSapMainProd] as a\r\n"
//			  + "			left join [PCMS].[dbo].[FromSapMainSale]  as b on a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n"
//			  + "			left join ( SELECT distinct [SaleOrder],[SaleLine] ,count([DataStatus]) as [CheckBill] \r\n"
//			  + "				FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
//			  + "				where DataStatus = 'O'\r\n"
//			  + "				group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine]  \r\n"
//			  + "			where (  A.DataStatus = 'O' )\r\n"
//			  + "			) as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder 	"
//			  + "			left join ( SELECT a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
//			  + "                       from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
//			  + "					 	left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
//			  + "			            WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z')\r\n"
//			  + "			            group by a.ProductionOrder) as t on b.ProductionOrder = t.ProductionOrder   \r\n"
//			  + "            left join ( SELECT a.ProductionOrderRP , sum(c.Volume) as SumVolRP  \r\n"
//			  + "    					from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
//			  + "						left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
//			  + "						left join ( SELECT ProductionOrderRP  \r\n"
//			  + "										 , CASE  \r\n"
//			  + "										   WHEN ( Volume = 0 ) THEN b.Volumn  \r\n"
//			  + "										   ELSE a.Volume\r\n"
//			  + "										   END AS Volume  \r\n"
//			  + "									from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
//			  + "									left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
//			  + "									WHERE a.[DataStatus] = 'O' ) as c on a.ProductionOrderRP = c.ProductionOrderRP\r\n"
//			  + "						WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"
//			  + "						group by a.ProductionOrderRP) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n"
////			  + "           left join ( SELECT distinct [SaleOrder],[SaleLine] ,count([DataStatus]) as [CheckBill] \r\n"
////			  + "							FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
////			  + "							where DataStatus = 'O'\r\n"
////			  + "					   group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine] \r\n" 
//			  + "           left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"
//			  + "           left join ( SELECT distinct  a.id,a.[ProductionOrder] \r\n"
//			  + "  			                  ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
//			  + "			              FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
//			  + "			              inner join (select [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([id]) as [MaxId]\r\n"
//			  + "				                       FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
//			  + "				                       group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
//			  + "				          on a.[id] = b.[MaxId] \r\n"
//			  + "			) as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n"
//			  + "           "+this.leftJoinM
//			  + "           "+this.leftJoinUCAL  ; 
	   private String leftJoinFSMBB = ""
	    		+ " left join ( SELECT distinct [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[Grade]  ,SUM([QuantityKG]) AS [BillSendWeightQuantity] \r\n"
	    		+ "							,SUM([QuantityYD]) AS [BillSendYDQuantity] ,SUM([QuantityMR]) AS [BillSendMRQuantity] \r\n"
	    		+ "						  FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
	    		+ "						  where DataStatus = 'O'\r\n"
	    		+ "						  GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[Grade]  ) \r\n"
	    		+ "						AS FSMBB ON b.[ProductionOrder] = FSMBB.[ProductionOrder] \r\n"
	    		+ "							    AND FSMBB.Grade = M.Grade \r\n" 
	    		+ "							    AND FSMBB.SaleOrder = a.SaleOrder \r\n"
	    		+ "							    AND FSMBB.SaleLine = a.SaleLine\r\n";
	   private String leftJoinTempG = 
				  " left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"  ;   
		 
    private String leftJoinBPartOne =    
		    " left join  ( SELECT distinct \r\n"
		  + "				  a.[SaleOrder] , a.[Saleline]  ,[TotalQuantity] ,[Unit]\r\n"
		  + "			 	, [RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
		  + "			 	, a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
		  + "			 	, [Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
		  + "				, [OrderPuang] ,[RefPrd],b.[GreigeInDate] ,[BCDate],b.[Volumn]\r\n"
		  + "			 	, [CFdate],[CFType],[Shade],b.[LotShipping] ,m.[Grade] \r\n"
		  + "				, [PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG]\r\n "
		  + "			 	, b.[ProductionOrder]   ,b.[LotNo] \r\n"  
//		  + "			 ,CASE  \r\n"
//		  + "					WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
//		  + "					WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//		  + "                   WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
//		  + "					WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n" 
//		  + "					ELSE 'รอจัด Lot'  \r\n"
//		  + "					END AS [ProductionOrder]  \r\n"
//		  + "			 ,CASE  \r\n"
//		  + "					WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
//		  + "					WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//		  + "                   WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
//		  + "					WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
//		  + "					ELSE 'รอจัด Lot'  \r\n"
//		  + "					END AS [LotNo]  \r\n"
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
 		  + "				, FSMBB.BillSendYDQuantity \r\n" 
//		  + "			  	, FSMG.BillSendWeightQuantity\r\n"
//		  + "				, FSMG.BillSendQuantity\r\n"
//		  + "				, FSMG.BillSendMRQuantity\r\n"
//		  + "				, FSMG.BillSendYDQuantity \r\n  " 
		  + "			from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
//		  + "			left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n" 
		  + "           left join ( \r\n"
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
		  + "				 		,[CFdate],[CFType],[Shade],g.[LotShipping],[BillSendQuantity] \r\n"
		  + "				 		,[PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG] \r\n"
		  + "				from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
		  + "				left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
		  + "			   	left join ( SELECT distinct [SaleOrder],[SaleLine] ,count([DataStatus]) as [CheckBill] \r\n"
		  + "							FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
		  + "							where DataStatus = 'O'\r\n"
		  + "						   	group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine]  \r\n"
		  + "              "+this.leftJoinTempG   
		  + "				)as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
		  + "			left join ( SELECT a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
		  + "                       from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
		  + "					 	left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
		  + "			            WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z')\r\n"
		  + "			            group by a.ProductionOrder) as t on b.ProductionOrder = t.ProductionOrder   \r\n"
		  + "           left join ( SELECT a.ProductionOrderRP , sum(c.Volume) as SumVolRP  \r\n"
		  + "    					from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
		  + "						left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
		  + "						left join ( SELECT ProductionOrderRP  \r\n"
		  + "										 , CASE  \r\n"
		  + "										   	WHEN ( Volume = 0 ) THEN b.Volumn  \r\n"
		  + "										   	ELSE a.Volume\r\n"
		  + "										   	END AS Volume  \r\n"
		  + "									from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
		  + "									left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
		  + "									WHERE a.[DataStatus] = 'O' ) as c on a.ProductionOrderRP = c.ProductionOrderRP\r\n"
		  + "						WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"
		  + "						group by a.ProductionOrderRP) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n"
//		  + "           left join ( SELECT distinct [SaleOrder],[SaleLine] ,count([DataStatus]) as [CheckBill] \r\n"
//		  + "						FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
//		  + "						where DataStatus = 'O'\r\n"
//		  + "					   	group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine] \r\n" 
//		  + "           left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"
		  + "          "+this.leftJoinTempG
		  + "          "+this.leftJoinSCC
		  + "           left join ( SELECT distinct  a.id,a.[ProductionOrder] \r\n"
		  + "  			                  			,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
		  + "			            FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
		  + "			            inner join (select [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([id]) as [MaxId]\r\n"
		  + "				                    FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
		  + "				                    group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		  + "				        on a.[id] = b.[MaxId] \r\n"
		  + "			) as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n"
		  + "           "+this.leftJoinM
		  + "           "+this.leftJoinUCAL 
		  + "            \r\n"
		  + "           "+this.leftJoinFSMBB
//		  + "        	left join ( SELECT  [ProductionOrder]  ,[Grade]  ,SUM([QuantityKG]) AS [BillSendWeightQuantity] \r\n"
//		  + "							,SUM([QuantityYD]) AS [BillSendYDQuantity] ,SUM([QuantityMR]) AS [BillSendMRQuantity] \r\n"
//		  + "						  FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
//		  + "						  where DataStatus = 'O'\r\n"
//		  + "						  GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[Grade]  ) \r\n"
//		  + "						AS FSMBB ON b.[ProductionOrder] = FSMBB.[ProductionOrder] AND FSMBB.Grade = M.Grade"
//		  + "        	left join [PCMS].[dbo].[FromSapMainGrade] as FSMG on b.ProductionOrder = FSMG.ProductionOrder and FSMG.DataStatus = 'O'    \r\n"
			;
  //    private String leftJoinBPartOne =    
//			    " left join  ( SELECT distinct \r\n"
//			  + "				  a.[SaleOrder] , a.[Saleline]  ,[TotalQuantity] ,[Unit]\r\n"
//			  + "			 	, [RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
//			  + "			 	, a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
//			  + "			 	, [Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
//			  + "				, [OrderPuang] ,[RefPrd],b.[GreigeInDate] ,[BCDate],b.[Volumn]\r\n"
//			  + "			 	, [CFdate],[CFType],[Shade],b.[LotShipping] ,m.[Grade] \r\n"
//			  + "				, [PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG]\r\n "
//			  + "			 	, b.[ProductionOrder]   ,b.[LotNo] \r\n"  
////			  + "			 ,CASE  \r\n"
////			  + "					WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
////			  + "					WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
////			  + "                   WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
////			  + "					WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n" 
////			  + "					ELSE 'รอจัด Lot'  \r\n"
////			  + "					END AS [ProductionOrder]  \r\n"
////			  + "			 ,CASE  \r\n"
////			  + "					WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
////			  + "					WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
////			  + "                   WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
////			  + "					WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
////			  + "					ELSE 'รอจัด Lot'  \r\n"
////			  + "					END AS [LotNo]  \r\n"
//			  + " 			 	, CASE  \r\n"
//			  + " 					WHEN ( s.SumVolRP is not null AND t.SumVolOP is not null ) THEN (  b.Volumn -  s.SumVolRP -  t.SumVolOP)\r\n"
//			  + "					WHEN ( s.SumVolRP is not null AND t.SumVolOP is null ) THEN (  b.Volumn -  s.SumVolRP )\r\n"
//			  + "					WHEN ( s.SumVolRP is null AND t.SumVolOP is not null ) THEN (  b.Volumn -  t.SumVolOP) \r\n"
//			  + "					WHEN  b.Volumn is not null THEN  b.Volumn\r\n"
//			  + "				    ELSE   0\r\n"
//			  + "	 				END AS SumVol   \r\n"
//			  + " 			 	, CASE  \r\n"
//			  + "				 	WHEN ( s.SumVolRP is not null AND t.SumVolOP is not null ) THEN a.Price * (  b.Volumn -  s.SumVolRP -  t.SumVolOP)\r\n"
//			  + "				 	WHEN ( s.SumVolRP is not null AND t.SumVolOP is null ) THEN a.Price * (  b.Volumn -  s.SumVolRP )\r\n"
//			  + "				 	WHEN ( s.SumVolRP is null AND t.SumVolOP is not null ) THEN a.Price * (  b.Volumn -  t.SumVolOP)   \r\n"
//			  + "				 	WHEN  b.Volumn is not null THEN a.Price * b.Volumn\r\n"
//			  + "					ELSE   0\r\n"
//			  + "				 	END AS SumVolFGAmount   \r\n"
//			  + "              	, s.SumVolRP,t.SumVolOP,b.Volumn as RealVolumn\r\n"
//			  + "              	, g.[DyePlan]  \r\n"
//			  + "	          	, g.[DyeActual] , g.[Dryer] , g.[Finishing] , g.[Inspectation]  \r\n"
//			  + "             	, g.[Prepare] , g.[Preset] , g.[Relax] , g.[CFMDateActual] , g.[CFMPlanDate]  \r\n"
//			  + "            	, g.[DyeStatus] , h.DeliveryDate, 	UCAL.UserStatusCal as UserStatus "
////			  + "             , g.SendCFMCusDate" 
//			  + " 		  		, CASE \r\n"
//			  + "					WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
//			  + "    			  	ELSE  g.SendCFMCusDate \r\n"     
//			  + "    				END AS SendCFMCusDate\r\n"
//			  + "				, GRSumKG, GRSumYD, GRSumMR \r\n " 
//			  + "           	, g.CFMDetailAll \r\n"
//			  + "           	, g.CFMNumberAll  \r\n"
//			  + "           	, g.CFMRemarkAll \r\n" 
//	 		  + "           	, g.RollNoRemarkAll \r\n"
//			  + "           	, g.CFMActualLabDate \r\n" 
//	 		  + "           	, g.CFMCusAnsLabDate \r\n" 
//			  + "			  	, FSMG.BillSendWeightQuantity\r\n"
//			  + "				, FSMG.BillSendQuantity\r\n"
//			  + "				, FSMG.BillSendMRQuantity\r\n"
//			  + "				, FSMG.BillSendYDQuantity \r\n  " 
//			  + "			from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
////			  + "			left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder\r\n" 
//			  + "           left join ( \r\n"
//			  + "				SELECT distinct \r\n"
//			  + "						 a.SaleOrder ,a.SaleLine \r\n"
//			  + "						,CASE  \r\n"
//			  + "							WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
//			  + "							WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//			  + "					   		WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
//			  + "							WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
//			  + "							ELSE 'รอจัด Lot'  \r\n"
//			  + "							END AS [ProductionOrder]  \r\n"
//			  + "				 		,CASE  \r\n"
//			  + "							WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
//			  + "							WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//			  + "					   		WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
//			  + "							WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
//			  + "							ELSE 'รอจัด Lot'  \r\n"
//			  + "							END AS [LotNo]   \r\n"
//			  + "				 		,[TotalQuantity] ,[Unit]\r\n"
//			  + "				 		,[RemAfterCloseOne] ,[RemAfterCloseTwo] ,[RemAfterCloseThree] ,[LabStatus] \r\n"
//			  + "				 		,a.[DesignFG],a.[ArticleFG],[BookNo],[Center] \r\n"
//			  + "				 		,[Batch],[LabNo],[RemarkOne],[RemarkTwo],[RemarkThree],[BCAware]\r\n"
//			  + "				 		,[OrderPuang] ,[RefPrd],b.[GreigeInDate] ,[BCDate],b.[Volumn]\r\n"
//			  + "				 		,[CFdate],[CFType],[Shade],b.[LotShipping],[BillSendQuantity] \r\n"
//			  + "				 		,[PrdCreateDate],[GreigeArticle],[GreigeDesign],[GreigeMR],[GreigeKG] \r\n"
//			  + "				from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
//			  + "				left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
//			  + "			   	left join ( SELECT distinct [SaleOrder],[SaleLine] ,count([DataStatus]) as [CheckBill] \r\n"
//			  + "							FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
//			  + "							where DataStatus = 'O'\r\n"
//			  + "						   	group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine]  \r\n"
//			  + "				)as b on  a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
//			  + "			left join ( SELECT a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
//			  + "                       from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
//			  + "					 	left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
//			  + "			            WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z')\r\n"
//			  + "			            group by a.ProductionOrder) as t on b.ProductionOrder = t.ProductionOrder   \r\n"
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
//			  + "						WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"
//			  + "						group by a.ProductionOrderRP) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n"
////			  + "           left join ( SELECT distinct [SaleOrder],[SaleLine] ,count([DataStatus]) as [CheckBill] \r\n"
////			  + "						FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
////			  + "						where DataStatus = 'O'\r\n"
////			  + "					   	group by [SaleOrder],[SaleLine]) as z on A.[SaleOrder] = z.[SaleOrder] AND  A.[SaleLine] = z.[SaleLine] \r\n" 
//			  + "           left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"
//			  + "          "+this.leftJoinSCC
//			  + "           left join ( SELECT distinct  a.id,a.[ProductionOrder] \r\n"
//			  + "  			                  			,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
//			  + "			            FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
//			  + "			            inner join (select [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([id]) as [MaxId]\r\n"
//			  + "				                    FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
//			  + "				                    group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
//			  + "				        on a.[id] = b.[MaxId] \r\n"
//			  + "			) as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n"
//			  + "           "+this.leftJoinM
//			  + "           "+this.leftJoinUCAL 
//			  + "        	left join [PCMS].[dbo].[FromSapMainGrade] as FSMG on b.ProductionOrder = FSMG.ProductionOrder and FSMG.DataStatus = 'O'    \r\n"
//				;
			  private String leftJoinBPartTwo = ""
			  + " ) as b on  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n";
    public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");  
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm"); 
	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//	private String leftJoinC = ""
//			+ " left join [PCMS].[dbo].[FromSapMainGrade] as c on b.ProductionOrder = c.ProductionOrder and c.DataStatus = 'O'  \r\n"; 
//	private String leftJoinC = ""
//			+ " left join [PCMS].[dbo].[FromSapMainGrade] as c on b.ProductionOrder = c.ProductionOrder and c.DataStatus = 'O'  \r\n"; 
//	private String leftJoinX = " left join [PCMS].[dbo].[FromSapMainProdSale] as x on a.SaleLine = x.SaleLine and x.SaleOrder = a.SaleOrder  	 \r\n";
//	private String leftJoinCFMLastD = 
//    		  "   left join ( SELECT distinct a.[ProductionOrder]\r\n"    
//    		+ "					  ,[CFMNo]\r\n"
//    		+ "					  ,[CFMNumber]\r\n"
//    		+ "					  ,[CFMSendDate]\r\n"
//    		+ "					  ,[CFMAnswerDate]\r\n"
//    		+ "					  ,[CFMStatus] \r\n"
//    		+ "					  ,[CFMRemark]\r\n"
//    		+ "				FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
//    		+ "				inner join (select distinct  ProductionOrder ,   max([CFMNo]) as maxCFMNo  \r\n"
//    		+ "					        from [PCMS].[dbo].[FromSapCFM] \r\n"
//    		+ "							where  DataStatus <> 'X'  and [CFMNumber] <> '' \r\n"
//    		+ "					 		group by ProductionOrder )as b on \r\n"
//    		+ "                         a.[ProductionOrder] = b.[ProductionOrder] and  a.[CFMNo] = b.[maxCFMNo]  \r\n"
//    		+ "             where  DataStatus <> 'X'  and [CFMNumber] <> '' \r\n"
//    		+ "			) as d on b.ProductionOrder = d.ProductionOrder \r\n";  
//	private String leftJoinCFMLastNoStatI = 
//  		  "     left join ( SELECT distinct a.[ProductionOrder]\r\n"
//  		  + "					  ,[CFMNo]\r\n"
//  		  + "					  ,[CFMNumber]\r\n"
//  		  + "					  ,[CFMSendDate]\r\n"
//  		  + "					  ,[CFMAnswerDate]\r\n"
//  		  + "					  ,[CFMStatus] \r\n"
//  		  + "					  ,[CFMRemark]\r\n"
//  		  + "				FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
//  		  + "				inner join (select distinct  ProductionOrder ,   max([CFMNo]) as maxCFMNo  \r\n"
//  		  + "					        from [PCMS].[dbo].[FromSapCFM] \r\n"
//  		  + "							where  DataStatus <> 'X' and CFMStatus = ''"
//  		  + "								 and [CFMNumber] <> ''\r\n"
//  		  + "					 		group by ProductionOrder )as b on \r\n"
//  		  + "                         a.[ProductionOrder] = b.[ProductionOrder] and  a.[CFMNo] = b.[maxCFMNo]  \r\n"
//  		  + "             where  DataStatus <> 'X' and CFMStatus = '' and [CFMNumber] <> ''\r\n"
//  		  + "			) as i on b.ProductionOrder = i.ProductionOrder  \r\n";  
//	private String createTempCFM = 
//			  " If(OBJECT_ID('tempdb..#tempCFMD') Is Not Null)\r\n"
//			+ "	begin\r\n"
//			+ "		Drop Table #tempCFMD\r\n"
//			+ "	end ;\r\n"
//			+ "    SELECT distinct a.[ProductionOrder]\r\n"    
//	  		+ "					  ,[CFMNo]\r\n"
//	  		+ "					  ,[CFMNumber]\r\n"
//	  		+ "					  ,[CFMSendDate]\r\n"
//	  		+ "					  ,[CFMAnswerDate]\r\n"
//	  		+ "					  ,[CFMStatus] \r\n"
//	  		+ "					  ,[CFMRemark]\r\n\r\n"
//	  		+ "	   ,  STUFF((\r\n"
//	  		+ "				SELECT ' ,' +c.[CFMNo]+'. '+ --c.[CFMNumber]+' | '+\r\n"
//	  		+ "				convert(VARCHAR, c.[CFMSendDate],103) +' | '+\r\n"
//	  		+ "				convert(VARCHAR, c.[CFMAnswerDate],103)+' | '+c.[CFMStatus]--+' | '+c.[RollNoRemark]\r\n"
//	  		+ "				FROM [PCMS].[dbo].[FromSapCFM] as c\r\n"
//	  		+ "				WHERE a.ProductionOrder = c.ProductionOrder and c.DataStatus = 'O'\r\n"
//	  		+ "				FOR XML PATH('')\r\n"
//	  		+ "				), 1, 2, '') as CFMDetailAll\r\n"
//	  		+ "	  ,  STUFF((\r\n"
//	  		+ "				SELECT ' ,' +c.[CFMNo]+'. '+ c.[CFMNumber]\r\n"
//	  		+ "				FROM [PCMS].[dbo].[FromSapCFM] as c\r\n"
//	  		+ "				WHERE a.ProductionOrder = c.ProductionOrder and c.DataStatus = 'O'\r\n"
//	  		+ "				FOR XML PATH('')\r\n"
//	  		+ "				), 1, 2, '') as CFMNumberAll\r\n"
//	  		+ "	  ,  STUFF((\r\n"
//	  		+ "				SELECT ' ,' +c.[CFMNo]+'. '+ c.[CFMRemark]\r\n"
//	  		+ "				FROM [PCMS].[dbo].[FromSapCFM] as c\r\n"
//	  		+ "				WHERE a.ProductionOrder = c.ProductionOrder and c.DataStatus = 'O'\r\n"
//	  		+ "				FOR XML PATH('')\r\n"
//	  		+ "				), 1, 2, '') as CFMRemarkAll\r\n" 
//	  		+ "				into #tempCFMD \r\n" 
//	  		+ "				FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
//	  		+ "				inner join (select distinct  ProductionOrder ,   max([CFMNo]) as maxCFMNo  \r\n"
//	  		+ "					        from [PCMS].[dbo].[FromSapCFM] \r\n"
//	  		+ "							where  DataStatus <> 'X'  and [CFMNumber] <> '' \r\n"
//	  		+ "					 		group by ProductionOrder )as b on \r\n"
//	  		+ "                         a.[ProductionOrder] = b.[ProductionOrder] and  a.[CFMNo] = b.[maxCFMNo]  \r\n"
//	  		+ "             where  DataStatus <> 'X'  \r\n"
//	  		+ "	 ";  ;
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
//	private String leftJoinCFMLastD = 
//	  		  "   left join #tempCFMD as CFMD on b.ProductionOrder = CFMD.ProductionOrder \r\n";  
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
//	private String leftJoinF = 
//  		  	" left join (SELECT distinct  a.[ProductionOrder] \r\n"
//  		  	+ "			,max(a.SubmitDate) as CFMPlanDateOne\r\n"
//  		  	+ "			FROM [PCMS].[dbo].[FromSapSubmitDate]  as a \r\n"
//  		  	+ "         where [DataStatus] = 'O' "
//  		  	+ "			group by a.[ProductionOrder] ) as f on b.ProductionOrder = f.ProductionOrder \r\n"  ;   
//	private String leftJoinG = 
//  		  	" left join ( SELECT distinct    a.[ProductionOrder] \r\n"  
//  		  	+ "  			 ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS CFMPlanDate \r\n"
//  		  	+ "			FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
//  		  	+ "			inner join (select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
//  		  	+ "				FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
//  		  	+ "				group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
//  		  	+ "				on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
//  		  	+ "				and a.[CreateDate] = b.[MaxCreateDate] \r\n"
//  		  	+ "			) as g on g.ProductionOrder = b.ProductionOrder and g.SaleOrder = a.SaleOrder and g.SaleLine = a.SaleLine\r\n"  ;   
	private String leftJoinG = 
  		  	" left join "
//  		  	+ " ( SELECT distinct a.[ProductionOrder]   \r\n"
//  		  	+ "	  		, case \r\n"
//  		  	+ "				when EndDateName = 'Saturday' then DATEADD(DAY, 9, a.[WorkDate])\r\n"
//  		  	+ "				when EndDateName is not null then DATEADD(DAY, 8, a.[WorkDate])\r\n"
//  		  	+ "				else null \r\n"
//  		  	+ "				end as CFMPlanDate  \r\n"
//  		  	+ "  FROM ( select [ProductionOrder] ,[WorkDate]  ,[Operation],DATENAME(DW, [WorkDate]) as EndDateName\r\n"
//  		  	+ "		FROM [PPMM].[dbo].[OperationWorkDate] ) as a   \r\n"
//  		  	+ "  inner join ( select [ProductionOrder]   \r\n"
//  		  	+ "					  ,max([Operation]) as maxOperation \r\n"
//  		  	+ "			   FROM [PPMM].[dbo].[OperationWorkDate]  \r\n"
//  		  	+ "			   where Operation >= 100 and Operation <= 103\r\n"
//  		  	+ "			   group by ProductionOrder) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.maxOperation\r\n"
//  		  	+ " \r\n"
//  		  	+ "  ) "
			+ " #TempG as g on g.ProductionOrder = b.ProductionOrder \r\n"  ;   
	 private String leftJoinH = 
  		  	  " left join ( SELECT distinct  a.id,a.[ProductionOrder] ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
  		  	+ "			    FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
  		  	+ "			    inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max(Id) as maxId\r\n"
  		  	+ "						    FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
  		  	+ "						    group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b on a.Id = b.maxId  \r\n"
  		  	+ "			  ) as h on h.ProductionOrder = a.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine \r\n"
  		  	+ "\r\n"  ;   
//    private String leftJoinZ =   
//		      " left join (SELECT distinct  [ProductionOrder]\r\n      "
//			+ "                 ,[SaleOrder]\r\n      " 
//		    + "                 ,[SaleLine]\r\n      "
//			+ "                 ,max([LotShipping]) as [LotShipping]\r\n  "
//			+ "            FROM [PCMS].[dbo].[FromSapMainGrade]\r\n"
//			+ "			  where DataStatus = 'O'\r\n  "
//			+ "            GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) \r\n  "
//			+ "  as z on z.ProductionOrder = b.ProductionOrder \r\n      and z.SaleLine = b.SaleLine \r\n      and z.SaleOrder = b.SaleOrder \r\n";
//    private String leftJoinLB = 
//    		  "  left join ( \r\n" 
//    		+ "            SELECT distinct max(b.[CFMDate]) as CFMDate ,a.[CFMAnswerDate] ,a.[LabNo] \r\n"
//    		+ "            FROM ( SELECT distinct   a.[LabNo],b.[LabWorkProcessNo],  max([CFMAnswerDate])  as [CFMAnswerDate] \r\n"
//    		+ "			          FROM [LBMS].[dbo].[SaleCFMDetail] as a\r\n"
//    		+ "			          inner join (select distinct   [SaleSet] ,max([LabWorkProcessNo])  as [LabWorkProcessNo]   \r\n"
//    		+ "	 							  FROM [LBMS].[dbo].[SaleCFMDetail]     \r\n"
//    		+ "	 							  group by [SaleSet]   ) as b on a.[SaleSet] = b.[SaleSet]  and a.LabWorkProcessNo = b.[LabWorkProcessNo]\r\n"
//    		+ "			          where a.saleset <> '' and SampleNo <> '' \r\n"
//    		+ "			          group by  a.[LabNo],b.[LabWorkProcessNo]  ) as a\r\n"
//    		+ "            inner join [LBMS].[dbo].[SaleCFMDetail] as b on a.LabNo = b.LabNo and a.[LabWorkProcessNo] = b.LabWorkProcessNo\r\n" 
//    		+ "            group by a.LabNo ,a.CFMAnswerDate  ) AS lb on b.LabNo = lb.LabNo    \r\n";
	private String leftJoinJ = 
			  " left join ( SELECT distinct SALELINE,SALEORDER,CFMDATE \r\n"
			+ "			    FROM [PCMS].[dbo].[FromSORCFM] )AS J  on a.SaleLine = J.SaleLine and a.SaleOrder = J.SaleOrder \r\n ";
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
//	private String leftJoinNToO =  
//			  " left join ( select distinct a.ProductionOrder , \r\n" 
//			+ "                        CASE WHEN ( WorkDate is NULL) THEN null \r\n"
//			+ "								ELSE CAST(DAY( WorkDate) AS VARCHAR(2)) + '/' +   CAST(MONTH( WorkDate)AS VARCHAR(2))  \r\n"
//			+ "								END AS DyePlan \r\n "								
//			+ "              from  [PPMM].[dbo].[OperationWorkDate]  as a\r\n"
//			+ "              inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			+ "                          from  [PPMM].[dbo].[DataFromSap] \r\n"
//			+ "                          where Operation >= 100 and Operation <= 103\r\n"
//			+ "                          group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			+ "              where a.Operation >= 100 and a.Operation <= 103 ) as N on b.ProductionOrder = N.ProductionOrder\r\n"
//			+ " left join ( select a.ProductionOrder , \r\n" 
//			+ "                    CASE WHEN ( OperationEndDate is NULL) THEN null \r\n"
//			+ "	                        ELSE CAST(DAY( OperationEndDate) AS VARCHAR(2)) + '/' +   CAST(MONTH( OperationEndDate)AS VARCHAR(2)) \r\n "
//			+ "                         END AS DyeActual \r\n"
//			+ "            from  [PPMM].[dbo].[DataFromSap]  as a\r\n"
//			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
//			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
//			+ "                        where Operation >= 100 and Operation <= 103\r\n"
//			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
//			+ "            where a.Operation >= 100 and a.Operation <= 103 \r\n"
//			+ "            ) as O on b.ProductionOrder = O.ProductionOrder\r\n"
//			 ;    
	private String leftJoinP = 
			  " left join (SELECT SALELINE,SALEORDER,ProductionOrder,PCRemark\r\n"
			+ "			   FROM [PCMS].[dbo].InputPCRemark \r\n"
			+ "			   WHERE DataStatus = 'O')   AS P on P.ProductionOrder = b.ProductionOrder and P.SaleOrder = a.SaleOrder and P.SaleLine = a.SaleLine\r\n" ;
//	private String leftJoinQ = " left join [PCMS].[dbo].[InputSwitchRemark] as q on a.SaleLine = q.SaleLine and q.SaleOrder = a.SaleOrder and b.ProductionOrder = q.ProductionOrder  \r\n";
	private String leftJoinQ = 
			  " left join (SELECT  ProductionOrder,SwitchRemark\r\n"
			+ "			   FROM [PCMS].[dbo].InputSwitchRemark \r\n"
			+ "			   WHERE DataStatus = 'O') AS q on b.ProductionOrder = q.ProductionOrder \r\n";
//	private String leftJoinR = " left join [PCMS].[dbo].[SwitchProdOrder] as R on b.ProductionOrder = R.ProductionOrderSW  and r.DataStatus = 'O' \r\n"; 
	private String leftJoinR = 
	  		    " left join (select ProductionOrder , ProductionOrderSW\r\n"
	  		  + "		     FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
	  		  + "		     WHERE DataStatus = 'O' ) as R on b.ProductionOrder = R.ProductionOrderSW  \r\n";  
	private String leftJoinInputCOD = 
			  " left join (SELECT ProductionOrder,[CauseOfDelay]\r\n"
			+ "			   FROM [PCMS].[dbo].[InputCauseOfDelay] \r\n"
			+ "			   WHERE DataStatus = 'O') AS InputCOD on b.ProductionOrder = InputCOD.ProductionOrder \r\n";
	private String leftJoinInputDD =   
			  " left join (SELECT  ProductionOrder,[DelayedDep]\r\n"
			+ "			   FROM [PCMS].[dbo].[InputDelayedDep] \r\n"
			+ "			   WHERE DataStatus = 'O') AS InputDD on b.ProductionOrder = InputDD.ProductionOrder \r\n";
//	private String leftJoinR = 
//			  "  left join ( SELECT ProductionOrderSW, sum(Volume) as SumVol\r\n"    
//			+ "               from [PCMS].[dbo].[SwitchProdOrder] \r\n"
//			+ "            where Productionorder <> [ProductionOrderSW] and DataStatus = 'O' and \r\n" 
//			+ "			   group by ProductionOrderRP) as s on b.ProductionOrder = R.ProductionOrderSW  \r\n";  
//	private String leftJoinS = "  "
//			+ "  left join ( SELECT ProductionOrderRP , sum(Volume) as SumVolRP\r\n"    
//			+ "            	   from [PCMS].[dbo].[ReplacedProdOrder] \r\n"
//			+ "			   WHERE [DataStatus] = 'O'\r\n"
//			+ "			   group by ProductionOrderRP) as s on b.ProductionOrder = s.ProductionOrderRP   \r\n"; 
//	private String leftJoinT = "    "
//			+ "   left join ( SELECT ProductionOrder  , sum(Volumn) as SumVolOP\r\n"
//			+ "               from [PCMS].[dbo].FromSapMainProdSale\r\n"
//			+ "			      WHERE [DataStatus] = 'O'\r\n"
//			+ "			      group by ProductionOrder) as t on b.ProductionOrder = t.ProductionOrder      \r\n"; 
//	private String createTempUCAL = ""
////			+ " If(OBJECT_ID('tempdb..#TempUCAL') Is Not Null)\r\n"
////			+ "	begin\r\n"
////			+ "		Drop Table #TempUCAL\r\n"
////			+ "	end ;\r\n"
//			+ "  SELECT distinct -- a.[ProductionOrder]  ,[UserStatus]  ,d.[Grade]  ,countGRFinish ,IsAnita ,CoaApproveDate  ,[CFMStatus],[DistChannel],\r\n"
//			+ "	  a.[ProductionOrder], a.saleorder,SumBill,CoaApproveDate,IsAnita,StockLoad,countGRFinish,a.GRADE\r\n"
//			+ "	    , a.ProductionOrderRPM  \r\n"
//			+ "		, CoaApproveDateRPM , StockLoadRPM, CountGRFinishRPM, GradeRPM, a.Volumn\r\n"
//			+ "		, CFMStatusRPM,SumGR,\r\n"
//			+ "	  	  CASE \r\n"
//			+ "	    when --a.UserStatus = 'ขายแล้ว' or   \r\n"
//			+ "			 a.UserStatus = 'ยกเลิก' or a.UserStatus = 'ปิดเพื่อแก้ไข' or  a.UserStatus = 'รับเข้าST.(P/S)' or\r\n"
//			+ "			 a.UserStatus = 'ตัดเกรดZ' or a.UserStatus = 'Over' or a.UserStatus = 'ขายเหลือ' or a.UserStatus = 'Lab' or \r\n"
//			+ "          a.UserStatus = 'HOLD,รอโอน' 		THEN a.UserStatus\r\n"
//			+ "		when ( a.[grade] = 'A' OR a.[grade] = 'C' ) AND countGRFinish = 1 and [DistChannel] = 'DM' AND a.[ProductionOrderRP] IS NULL THEN \r\n"
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
//			+ "		when ( a.[grade] = 'A' OR a.[grade] = 'C' ) AND countGRFinish = 1 and [DistChannel] = 'EX' AND a.[ProductionOrderRP] IS NULL THEN \r\n"
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
//			+ "	  , CoaApproveDate as CoaApproveDateRPM ,StockLoad AS StockLoadRPM,countGRFinish AS CountGRFinishRPM, H.GRADE AS GradeRPM\r\n"
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
//			+ "							, CoaApproveDate, StockLoad,countGRFinish, D.GRADE,CFMStatus \r\n"
//			+ "							FROM [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
//			+ "							inner join [PCMS].[dbo].FromSapMainProd as b on \r\n"
//			+ "									b.ProductionOrder = a.ProductionOrderRP and b.DataStatus = 'O' and\r\n"
//			+ "									a.[SaleOrder] = b.[SaleOrder] and a.[SaleLine] = b.[SaleLine]\r\n"
//			+ "							left join ( SELECT [ProductionOrder] \r\n"
//			+ "											,count([OperationEndTime]) as countGRFinish\r\n"
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
//			+ "						,count([OperationEndTime]) as countGRFinish\r\n"
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
//	private String createTempG = "SELECT distinct a.[ProductionOrder]   \r\n"
//			+ "	  		, case \r\n"
//			+ "				when EndDateName = 'Saturday' then DATEADD(DAY, 9, a.[WorkDate])\r\n"
//			+ "				when EndDateName is not null then DATEADD(DAY, 8, a.[WorkDate])\r\n"
//			+ "				else null \r\n"
//			+ "				end as CFMPlanDate  \r\n"
//			+ "	  		, case \r\n"
//			+ "				when EndDateName = 'Saturday' then DATEADD(DAY, 18, a.[WorkDate])\r\n"
//			+ "				when EndDateName is not null then DATEADD(DAY, 16, a.[WorkDate])\r\n"
//			+ "				else null \r\n"    
//			+ "				end as SendCFMCusDate  \r\n"
//			+ "  into #TempG \r\n"
//			+ "  FROM ( select [ProductionOrder] ,[WorkDate]  ,[Operation],DATENAME(DW, [WorkDate]) as EndDateName\r\n"
//			+ "			FROM [PPMM].[dbo].[OperationWorkDate] ) as a   \r\n"
//			+ "  inner join ( select [ProductionOrder]   \r\n"
//			+ "					  ,max([Operation]) as maxOperation \r\n"
//			+ "			   FROM [PPMM].[dbo].[OperationWorkDate]  \r\n"
//			+ "			   where Operation >= 100 and Operation <= 103\r\n"
//			+ "			   group by ProductionOrder) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.maxOperation\r\n"
//			+ " " ;
//	private String createTempUCALRP = 
//		    " SELECT distinct  A.ProductionOrder ,a.[Grade] --, a.ProductionOrderRPM ,a.SumBill \r\n"
//		    + "--, a.CoaApproveDateRPM , a.StockLoadRPM, a.CountGRFinishRPM, a.GradeRPM , a.CFMStatusRPM  ,a.Volumn  \r\n"
//		    + ", CASE  \r\n"
//		    + "		when ( a.GradeRPM = 'A' ) AND a.CountGRFinishRPM = 1 AND a.SumBill  IS NULL  THEN \r\n"
//		    + "			CASE \r\n"
//		    + "				WHEN a.CFMStatusRPM = '' THEN 'รอตอบ CFM' \r\n"
//		    + "				WHEN a.CFMStatusRPM = 'N' AND a.CoaApproveDateRPM IS NULL THEN 'รอสรุปจาก QA'   \r\n"
//		    + "				WHEN a.CFMStatusRPM = 'Y' AND CoaApproveDate IS NULL THEN 'รอตอบ CFM ตัวแทน' \r\n"
//		    + "				WHEN a.CFMStatusRPM = 'Y' AND a.CoaApproveDateRPM IS NOT NULL THEN 'รอแจ้งส่ง'     \r\n"
//		    + "				ELSE a.UserStatusCal\r\n"
//		    + "			end \r\n"
//		    + "		ELSE a.UserStatusCal END AS UserStatusCal \r\n"
//		    + "into #tempUCALRP\r\n"
//		    + "FROM #tempUCAL AS A \r\n"
////		    + "WHERE a.ProductionOrderRPM IS NOT NULL "
//		+ "  ;\r\n" ;
//	private String leftJoinUCAL = "    "
//			+ "   left join #TempUCAL as UCAL on b.ProductionOrder = UCAL.ProductionOrder   AND (  m.Grade = UCAL.Grade    OR  m.Grade IS NULL )    \r\n"; 
//	private String leftJoinUCALRP = "    "
//			+ "   left join #TempUCALRP as UCALRP on b.ProductionOrder = UCALRP.ProductionOrder   AND (  m.Grade = UCALRP.Grade    OR  m.Grade IS NULL )    \r\n"; 
	 
	
	private String leftJoinCSW = 
			  " LEFT JOIN ( SELECT [SaleOrderSW] ,[SaleLineSW] ,count ([ProductionOrderSW]) as countSW \r\n"
			+ "			    FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
			+ "			    WHERE DataStatus = 'O'\r\n"
			+ "			    GROUP BY  [SaleOrderSW] ,[SaleLineSW]\r\n"
			+ "   ) as CSW on  CSW.[SaleOrderSW] = a.SaleOrder and CSW.[SaleLineSW] = a.SaleLine\r\n" ;
	private String leftJoinCRP = 
			 " LEFT JOIN ( SELECT [SaleOrder] ,[SaleLine]  ,COUNT ([ProductionOrderRP]) as countnRP  \r\n"
			+ "			   FROM [PCMS].[dbo].[ReplacedProdOrder] \r\n"
			+ "			   WHERE DataStatus = 'O'\r\n"
			+ "			   GROUP BY  [SaleOrder] ,[SaleLine]\r\n"
			+ "   )  as CRP on  CRP.SaleOrder = a.SaleOrder and CRP.SaleLine = a.SaleLine\r\n" ;
	private String leftJoinCOP = 
			  " LEFT JOIN ( SELECT [SaleOrder] ,[SaleLine] ,COUNT([ProductionOrder]) as countnOP\r\n"
			+ "			FROM [PCMS].[dbo].[FromSapMainProdSale]\r\n"
			+ "			WHERE DataStatus = 'O'\r\n"
			+ "			GROUP BY  [SaleOrder] ,[SaleLine]   \r\n"
			+ "   ) as COP on  COP.SaleOrder = a.SaleOrder and COP.SaleLine = a.SaleLine \r\n";
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
		String where = " where  "; 
		String whereUCALRP = ""; 
		String whereBMain = "";  
		String whereBMainUserStatus = " where"; 
		String whereCaseA = "";
		String whereWaitLot = " where ";
//		String whereTempUCAL = " where ";
		int check = 0;
		String customerShortName ="", saleNumber = "" , materialNo = "",saleOrder = "", saleLine = "",
		saleCreateDate = "",labNo = "" ,articleFG = "",designFG = "",userStatus = "", prdOrder= "",
		prdCreateDate = "",deliveryStatus = "",saleStatus ="",dist="",customerName = "",dueDate = ""; 
		PCMSTableDetail bean = poList.get(0);
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
		saleNumber = bean.getSaleNumber() ; 
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();  
		saleCreateDate = bean.getSaleOrderCreateDate();	  
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		designFG = bean.getDesignFG();
		userStatus = bean.getUserStatus();
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();
		dueDate = bean.getDueDate();
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel(); 
		List<String> userStatusList = bean.getUserStatusList();
		List<String> cusNameList = bean.getCustomerNameList();
		List<String> cusShortNameList = bean.getCustomerShortNameList();
		List<String> divisionList = bean.getDivisionList();
		where +=    " MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n";
//				  + " a.SaleLine like '"+saleLine+"%' and\r\n"
		whereWaitLot +=    " MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n"; 
		;
		if (!saleCreateDate.equals("")) {   
			String[] dateArray = saleCreateDate.split("-");
			where += "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
			whereWaitLot +=  "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
		} 
		if(!saleNumber.equals("")) { 
			where += " and SaleNumber like '"+saleNumber+"%' \r\n" ; 
			whereWaitLot += " and SaleNumber like '"+saleNumber+"%' \r\n" ; 
		}
		if (!labNo.equals("")) {
			where += " and b.LabNo like '" + labNo + "%'  \r\n";
//			whereWaitLot += " and LabNo like '" + labNo + "%'  \r\n";
		}
		if (!articleFG.equals("")) {
			where += " and a.ArticleFG like '" + articleFG + "%'  \r\n";   
			whereWaitLot += " and a.ArticleFG like '" + articleFG + "%'  \r\n";   
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
//			whereTempUCAL += " and b.ProductionOrder like '" + prdOrder + "%'  \r\n ";
		}
		if (!prdCreateDate.equals("")) {
			String[] dateArray = prdCreateDate.split("-");
			where += " and ( PrdCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " PrdCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) )  \r\n";
//			whereTempUCAL += " and ( PrdCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
//					+ " PrdCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) )  \r\n";
		}if(!dueDate.equals("")) {  
			String[] dateArray = dueDate.split("-");
			where += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
			whereWaitLot += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
		}
		if (!deliveryStatus.equals("")) {
			where += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n";
			whereWaitLot += " and DeliveryStatus like '" + deliveryStatus + "%'  \r\n";
		}
		if (!saleStatus.equals("")) {
			where += " and SaleStatus like '" + saleStatus + "%'  \r\n";
//			whereTempUCAL += " and SaleStatus like '" + saleStatus + "%'  \r\n";
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
		whereWaitLot = where ;
		whereBMain = where;
//		whereCaseA = where;
		String tmpWhereNoLotUCAL = "";
		if (userStatusList.size() > 0) {
			String tmpWhere = "";
			tmpWhereNoLotUCAL = " and ( b.ProductionOrder is not null and ( \r\n";  
			tmpWhere += " and ( b.ProductionOrder is not null and ( \r\n";  
			whereUCALRP += " and ( b.ProductionOrder is not null and ( \r\n";  
			String text = "";
			for (int i = 0; i < userStatusList.size(); i++) {
				text = userStatusList.get(i); 
				if(text.equals("รอจัด Lot") || text.equals("ขาย stock") || text.equals("รับจ้างถัก") || text.equals("Lot ขายแล้ว")
				|| text.equals("พ่วงแล้วรอสวม")|| text.equals("รอสวมเคยมี Lot") ) { 
					tmpWhere += " b.LotNo = '" +text + "' "; 
					whereUCALRP += " b.LotNo = '" +text + "' ";
					listUserStatus.add(" b.LotNo = '" +text + "' "); 
				} else {  
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
			whereCaseA = where.replace("UCAL.UserStatusCal", "a.UserStatusCal");
//			whereTempUCAL += tmpWhere;
		}  
		String sqlWaitLot = 
				  " SELECT DISTINCT  \r\n"       
				+ this.selectWaitLot 
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n " 
				+ this.innerJoinWaitLotB
//				+ " inner join (select * , 'รอจัด Lot' AS LotNo\r\n "
//				+ "             ,null as TotalQuantity \r\n"
//				+ "				,'' as Grade \r\n"
//				+ "				,null as BillSendWeightQuantity \r\n"
//				+ "				,null as BillSendQuantity  \r\n"
//				+ "				,null as BillSendMRQuantity \r\n"
//				+ "				,null as BillSendYDQuantity  \r\n"
//				+ "				,'' as LabNo\r\n"
//				+ "				,'' as LabStatus\r\n"
//				+ "				,null as CFMPlanLabDate\r\n"
//				+ "				,null as CFMActualLabDate \r\n"
//				+ "				,null as CFMCusAnsLabDate \r\n"
//				+ "				,'' as UserStatus \r\n"
//				+ "				,null as TKCFM \r\n"
//				+ "				,null as CFMPlanDate \r\n"
//				+ "				,null as DeliveryDate  \r\n"
//				+ "				,null as CFMSendDate \r\n"
//				+ "				,null as CFMAnswerDate \r\n"
//				+ "				,'' as CFMStatus \r\n"
//				+ "				,'' as CFMNumber  \r\n"
//				+ "				,'' as CFMRemark  \r\n"
//				+ "				,'' as RemarkOne \r\n"
//				+ "				,'' as RemarkTwo \r\n"
//				+ "				,'' as RemarkThree  \r\n"
//				+ "				,'' as StockRemark \r\n"
//				+ "				,null as  GRSumKG \r\n"
//				+ "				,null as  GRSumYD \r\n"
//				+ "				,null as  GRSumMR \r\n"
//				+ "				,null as  DyePlan \r\n"
//				+ "				,null as DyeActual   \r\n"
//				+ "				,'' as [SwitchRemark] \r\n"
//				+ "				,null as [PrdCreateDate]\r\n"
//				+ "				, NULL AS Volumn   \r\n"
//				+ "				, NULL AS VolumnFGAmount  " 
//				+ "			    from [PCMS].[dbo].[ReplacedProdOrder] as a \r\n"
//				+ "			    where ProductionOrder = 'รอจัด Lot' and a.DataStatus = 'O') \r\n"
//				+ "              as b on a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine   \r\n"
				+ this.leftJoinJ
				+ this.leftJoinK    
				+ this.leftJoinP 
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinSL 
				+ whereWaitLot   
				+ " and ( SumVol = 'B' OR countProdRP > 0 ) \r\n";
		String sqlMain = 
				      " SELECT DISTINCT \r\n"       
					+ this.selectMain
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
					+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n " 
					+ this.leftJoinBPartOne
					+ whereBMainUserStatus
					+ this.leftJoinBPartTwo
//					+ this.leftJoinC	  
					+ this.leftJoinE   
//					+ this.leftJoinLB  
					+ this.leftJoinJ
					+ this.leftJoinK    
					+ this.leftJoinMainl
//					+ this.leftJoinNToO 
					+ this.leftJoinP
					+ this.leftJoinInputCOD 
					+ this.leftJoinInputDD  
					+ this.leftJoinQ 
					+ this.leftJoinSL 
					+ this.leftJoinCRP        //10/08/2022 
					+ whereBMain    
					+ " and ( b.SumVol <> 0\r\n"
					+ "		or ( (  b.LotNo = 'รอจัด Lot'  or  b.LotNo = 'ขาย stock' or b.LotNo = 'รับจ้างถัก' or b.LotNo = 'Lot ขายแล้ว' \r\n"
					+ "				or b.LotNo = 'พ่วงแล้วรอสวม' or b.LotNo = 'รอสวมเคยมี Lot'  )  and b.SumVol = 0 \r\n"
//					+ "		     and ( countnRP is null and countSW is null and countnOP is null )  \r\n" 
					+ "		     and ( countnRP is null )  \r\n"
					+ "        ) \r\n" 
					+ "     or  RealVolumn = 0 \r\n"
					+ "     or ( b.UserStatus = 'ยกเลิก' or b.UserStatus = 'ตัดเกรด Z') \r\n"
					+ "     )\r\n"        
					+ " and NOT EXISTS ( select distinct ProductionOrderSW\r\n"
					+ "				   FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA\r\n"
					+ "				   WHERE DataStatus = 'O' and BAA.ProductionOrderSW = B.ProductionOrder\r\n"
					+ "				 )   \r\n" ;
//					+ " union \r\n" + 
//////					// Order Puang
		String sqlOP =  
					  " SELECT DISTINCT \r\n"       
					+ this.selectOP 
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
					+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
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
//					+ "			  ,g.SendCFMCusDate \r\n"
					+ " 		  , CASE \r\n"
					+ "					WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
					+ "    				ELSE  g.SendCFMCusDate \r\n"     
					+ "    				END AS SendCFMCusDate\r\n"
					+ "			  ,g.CFMDateActual\r\n"
					+ "           , g.CFMDetailAll \r\n"
					+ "			  , g.CFMNumberAll \r\n"
					+ "			  , g.CFMRemarkAll \r\n"
					+ "			  , g.RollNoRemarkAll \r\n"
					+ "			  , CASE \r\n"
					+ "					WHEN ( g.DyePlan is NULL) THEN null \r\n"
					+ "				   	ELSE CAST(DAY( g.DyePlan) AS VARCHAR(2)) + '/' +   CAST(MONTH( g.DyePlan)AS VARCHAR(2))  \r\n"
					+ "			  		END AS DyePlan \r\n"
					+ "			  , CASE \r\n"
					+ "					WHEN ( g.DyeActual is NULL) THEN null \r\n"
					+ "				  	ELSE CAST(DAY( g.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( g.DyeActual)AS VARCHAR(2))  \r\n"
					+ "			  		END AS DyeActual  \r\n"
					+ "			, FSMBB.BillSendWeightQuantity \r\n"
					+ "			, case\r\n"
					+ "					WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
					+ "					WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
					+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
					+ "				end AS BillSendQuantity \r\n" 
					+ "			, FSMBB.BillSendMRQuantity\r\n"
					+ "			, FSMBB.BillSendYDQuantity \r\n"
//					+ "			  , FSMG.BillSendWeightQuantity\r\n"
//					+ "			  , FSMG.BillSendQuantity\r\n"
//					+ "			  , FSMG.BillSendMRQuantity\r\n"
//					+ "			  , FSMG.BillSendYDQuantity \r\n  " 
					+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
					+ "		 	inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"  
					+ "         "+this.leftJoinTempG   
					+ "         "+this.leftJoinM   
					+ "         "+this.leftJoinUCAL   
				    + "         "+this.leftJoinFSMBB
//					+ "         left join [PCMS].[dbo].[FromSapMainGrade] as FSMG on b.ProductionOrder = FSMG.ProductionOrder and FSMG.DataStatus = 'O'    \r\n"
					+ "         "+ this.leftJoinSCC
					+ "		 	WHERE b.DataStatus <> 'X' \r\n" 
					+ "         "+tmpWhereNoLotUCAL
					+ "      ) as a  \r\n "  
					+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"  
//					+ this.leftJoinC	       
					+ this.leftJoinE    
//					+ this.leftJoinTempG   
					+ this.leftJoinH  
//					+ this.leftJoinLB  
					+ this.leftJoinJ
					+ this.leftJoinK      
					+ this.leftJoinMainl 
					+ this.leftJoinP
					+ this.leftJoinInputCOD 
					+ this.leftJoinInputDD  
					+ this.leftJoinQ 
					+ this.leftJoinSL
					+ whereBMain           
					+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"   
					+ " and NOT EXISTS ( select distinct ProductionOrderSW \r\n"
					+ "				   FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA \r\n"
					+ "				   WHERE DataStatus = 'O' and BAA.ProductionOrderSW = A.ProductionOrder\r\n"
					+ "				 ) \r\n " ; 
	String sqlOPSW = 
				  " SELECT DISTINCT  \r\n"       
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
//				+ "           , m.[Grade],[PriceSTD],GRSumMR,GRSumKG,GRSumYD,UCAL.UserStatusCal\r\n" 
//				+ "			, FSMBB.BillSendWeightQuantity \r\n"
//				+ "			, case\r\n"
//				+ "					WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
//				+ "					WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
//				+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
//				+ "				end AS BillSendQuantity \r\n"
//				+ "			, FSMBB.BillSendMRQuantity\r\n"
//				+ "			, FSMBB.BillSendYDQuantity \r\n"
				+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
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
//				+ "         "+ this.leftJoinM   
//				+ "         "+ this.leftJoinUCAL    
//			    + "         "+ this.leftJoinFSMBB
				+ "			where b.DataStatus <> 'X' and b.SaleLine <> '' ) as a  \r\n "  
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"  
//				+ this.leftJoinC	       
				+ this.leftJoinE    
				+ this.leftJoinTempG   
				+ this.leftJoinSCC  
				+ this.leftJoinH   
//				+ this.leftJoinLB  
				+ this.leftJoinJ
				+ this.leftJoinK 
				+ this.leftJoinMainl   
				+ this.leftJoinP
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinQ 
				+ this.leftJoinSL 
				+ this.leftJoinM   
				+ this.leftJoinUCAL    
			    + this.leftJoinFSMBB
				+ where       
				+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"  ; 
////					// Switch   
					
	String sqlSW =    " SELECT DISTINCT  \r\n"       
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
					+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
					+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
					+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
					+ "	          , b.ProductionOrderSW as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
					+ "  		  ,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
					+ " 		  , CASE \r\n"
					+ "					when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
					+ "					ELSE 'SUB' \r\n"
					+ "					END	TypePrdRemark  ,C.SumVol\r\n "
//					+ "           , m.[Grade],[PriceSTD],GRSumMR,GRSumKG,GRSumYD,UCAL.UserStatusCal \r\n"   
//					+ "			, FSMBB.BillSendWeightQuantity \r\n"
//					+ "			, case\r\n"
//					+ "					WHEN a.SaleUnit  = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
//					+ "					WHEN a.SaleUnit  = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
//					+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
//					+ "				end AS BillSendQuantity \r\n"
//					+ "			, FSMBB.BillSendMRQuantity\r\n"
//					+ "			, FSMBB.BillSendYDQuantity \r\n"
					+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
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
//					+ "         "+ this.leftJoinM   
//					+ "         "+ this.leftJoinUCAL  
//				    + "         "+ this.leftJoinFSMBB
					+ "		    where b.DataStatus <> 'X') as a  \r\n "  
					+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"  
//					+ this.leftJoinC	       
					+ this.leftJoinE    
					+ this.leftJoinTempG 
					+ this.leftJoinSCC
					+ this.leftJoinH  
//					+ this.leftJoinLB  
					+ this.leftJoinJ
					+ this.leftJoinK  
					+ this.leftJoinMainl  
//					+ this.leftJoinNToO 
					+ this.leftJoinP
					+ this.leftJoinInputCOD 
					+ this.leftJoinInputDD  
					+ this.leftJoinQ 
					+ this.leftJoinSL 
					+ this.leftJoinM   
					+ this.leftJoinUCAL  
				    + this.leftJoinFSMBB
					+ where    
					+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"   ;
	 
//////				// สวม  
	String sqlRP = 
				  " SELECT DISTINCT  \r\n"       
				+ this.selectRP
				+ "   , CASE \r\n"
	    		+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Volumn\r\n" 
	    		+ "			ELSE NULL\r\n"
	    		+ "			END AS Volumn  \r\n"  
	    		+ "   , CASE \r\n"
	    		+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Price * a.Volumn \r\n" 
	    		+ "			ELSE NULL\r\n"
	    		+ "			END AS VolumnFGAmount \r\n"
				+ "   ,'Replaced' as TypePrd \r\n"
    			+ "   ,TypePrdRemark \r\n"
				+ " FROM (  SELECT DISTINCT \r\n"
				+ "             b.[SaleOrder]  , b.[SaleLine] ,a.DistChannel ,a.Color ,a.ColorCustomer  \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity ,a.SaleUnit ,a.DueDate ,a.CustomerShortName \r\n"
				+ "           , a.[SaleFullName] , a.[SaleNumber] ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus \r\n"
				+ "	          , b.[ProductionOrderRP] as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount \r\n"
				+ "  		  , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ "   		  , CASE \r\n"
				+ "					WHEN b.Volume <> 0 THEN b.Volume\r\n"
				+ "					ELSE c.Volumn\r\n" 
				+ "					END AS Volumn  \r\n" 
				+ " 		  , 'SUB' as TypePrdRemark \r\n"  
				+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a \r\n"
				+ "		 	inner join [PCMS].[dbo].[ReplacedProdOrder] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder  \r\n"
				+ "		 	left join [PCMS].[dbo].[FromSapMainProd] as c on c.ProductionOrder = b.[ProductionOrderRP]\r\n"
				+ "      	where b.DataStatus <> 'X') as a \r\n " 
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n" 
//				+ this.leftJoinC	 
				+ this.leftJoinE    
				+ this.leftJoinTempG   
				+ this.leftJoinSCC
				+ this.leftJoinH 
//				+ this.leftJoinLB  
				+ this.leftJoinJ
				+ this.leftJoinK
				+ this.leftJoinM       
				+ this.leftJoinl  
				+ this.leftJoinP
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinUCALRP
				+ this.leftJoinQ 
				+ this.leftJoinSL
				+ this.leftJoinFSMBB
				+ whereUCALRP        
				+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"   ;   
	 String sql =
			 		""
//				  this.createTempUCAL
//				+ this.createTempUCALRP
//				+ this.createTempG
//			 	+ this.createTempCFM
			 	+ this.createTempSumGR
				+ sqlWaitLot
				+ " union \r\n"  
				+ sqlMain   
				+ " union \r\n"  
				+ sqlOP    
				+ " union \r\n"     
				+ sqlOPSW    
				+ " union \r\n" 
				+ sqlSW 
				+ " union \r\n" 
				+ sqlRP  
				+ " Order by a.CustomerShortName, a.DueDate,a.[SaleOrder], [SaleLine],b.[ProductionOrder] " 
				;       
//	 String x =   ""
//			 + this.createTempSumGR
//			 
//			 + sqlMain       
//			 ;
//			 + " union \r\n"  
////			 + sqlOPSW   ;  
//	    System.out.println(x);  
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
	public ArrayList<PCMSTableDetail> getSaleNumberList() {
		ArrayList<PCMSTableDetail> list = null;
		String sql = 
				  " SELECT DISTINCT \r\n " 
				+ "	   a.SaleNumber\r\n"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" 
				+ "    		 WHEN 0 THEN ''   \r\n"
				+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+[SaleFullName]\r\n"
				+ "    		 END AS [SaleFullName]   " 
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n "
				+ " where SaleNumber <> '00000000' \r\n" 
				+ " Order by [SaleNumber]"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList) {
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
		if(caseSave.equals("CFMPlanLabDate")) { 
			planDate = bean.getCFMPlanLabDate();
			fromTable = " [PCMS].[dbo].[PlanCFMLabDate] "  ;
			list = this.getMaxCFMPlanLabDateDetail(poList);
			listCount = this.getCountCFMPlanLabDateDetail(poList);
			check = list.size();
			} 
		else if(caseSave.equals("CFMPlanDate")) { 
			planDate = bean.getCFMPlanDate();
			fromTable = "[PCMS].[dbo].[PlanCFMDate] "  ; 
			list = this.getMaxCFMPlanDateDetail(poList) ;
			listCount = this.getCountCFMPlanDateDetail(poList);
			check = list.size();	
		} 
		else if(caseSave.equals("DeliveryDate")) { 
			planDate = bean.getDeliveryDate();
			fromTable = "[PCMS].[dbo].[PlanDeliveryDate] "  ; 
			list = this.getMaxDeliveryPlanDateDetail(poList) ;
			listCount = this.getCountDeliveryPlanDateDetail(poList);
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
//				if(caseSave.equals("CFMCusAnsLabDate")) { 
//					sql =  " insert into "
//							+ fromTable
//							+ " ( "
//							+ "		[ProductionOrder]  ,[CFMCusAnsLabDate]  ,[ChangeBy]  , [ChangeDate] ,[LotNo] " 
//							+ "     ) "//24   
//							+ " 	values(? , ? , ? , ? , ? "//  1 
//							+ " ) ;";
//					prepared = connection.prepareStatement(sql);   
//					prepared.setString(1, bean.getProductionOrder()); 
//					if (planDate.equals("undefined") || planDate.equals("")  || !isValidDate(planDate)) {
//						prepared.setNull(2, java.sql.Types.DATE);
//					} 
//					else {
//						date = sdf2.parse(planDate);
//						prepared.setDate(2, convertJavaDateToSqlDate(date) );
//					}   
//					prepared.setString(3, bean.getUserId());
//					prepared.setTimestamp(4, new Timestamp(time));;   
//					prepared.setString(5, bean.getLotNo());
//					prepared.executeUpdate();     
//					beanInput.setIconStatus("I1"); 
//					beanInput.setSystemStatus("Update Success.");  
//				}
//				else { 
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
					else { 	 beanInput.setCountPlanDate(1); } 
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
		 
		PCMSSecondTableDetail bean = poList.get(0);  
//		String prodOrder = bean.getProductionOrder();
		String caseSave = bean.getCaseSave();
		String valueChange = ""; 
		String tableName = "";  
		if(caseSave.equals("StockRemark")) {
			valueChange = bean.getStockRemark(); 
			tableName = "[InputStockRemark]";
			bean = this.updateLogRemarkWithGrade(tableName,bean,this.CLOSE_STATUS);
			bean = this.upSertRemarkCaseWithGrade(tableName,valueChange,bean);
			poList.clear();
			poList.add(bean); 
		}  
		else if(caseSave.equals("PCRemark")){
			valueChange = bean.getPCRemark(); 
			tableName = "[InputPCRemark]"; 
			bean = this.updateLogRemarkCaseOne(tableName,bean,this.CLOSE_STATUS);
			bean = this.upSertRemarkCaseOne(tableName,valueChange,bean); 
			poList.clear();
			poList.add(bean); 
		} 
		else if(caseSave.equals("StockLoad")){
			valueChange = bean.getStockLoad(); 
			tableName = "[InputStockLoad]"; 
			bean = this.updateLogRemarkCaseOne(tableName,bean,this.CLOSE_STATUS);
			bean = this.upSertRemarkCaseOne(tableName,valueChange,bean); 
			poList.clear();
			poList.add(bean); 
		} 
		else if(caseSave.equals("ReplacedRemark")){
			valueChange = bean.getReplacedRemark();
			tableName = "[InputReplacedRemark]";  
			bean = this.updateLogRemarkCaseOne(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseOne(tableName,valueChange,bean);
			poList = this.handlerReplacedProdOrder(bean);      
			
		}     
		else if(caseSave.equals("SwitchRemark")){
			valueChange = bean.getSwitchRemark();
			tableName = "[InputSwitchRemark]";  
			bean = this.updateLogRemarkCaseTwo(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseTwo(tableName,valueChange,bean);
			poList = this.handlerSwitchProdOrder(bean); 
		}    
		else if(caseSave.equals("DelayedDep")){
			valueChange = bean.getDelayedDepartment();
			tableName = "[InputDelayedDep]";  
			bean = this.updateLogRemarkCaseTwo(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseTwo(tableName,valueChange,bean);
			poList.clear();
			poList.add(bean); 
		} 
		else if(caseSave.equals("CauseOfDelay")){
			valueChange = bean.getCauseOfDelay();
			tableName = "[InputCauseOfDelay]";  
			bean = this.updateLogRemarkCaseTwo(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseTwo(tableName,valueChange,bean);
			poList.clear();
			poList.add(bean); 
		}   
		else if(caseSave.equals("SendCFMCusDate")) { 
			valueChange = bean.getSendCFMCusDate(); 
			tableName = "[PlanSendCFMCusDate] "  ;    
			bean = this.updateLogRemarkCaseThree(tableName,bean,this.CLOSE_STATUS); 
			bean = this.upSertRemarkCaseThree(tableName,valueChange,bean); 
			poList.clear();
			poList.add(bean); 
		}  
		return poList;   
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
		long time = currentTime.getTime();
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
				prepared = connection.prepareStatement(sql);     
				prepared.setString(1, prdOrder);   
				prepared.setString(2, planDate);  
				if (planDate.equals("undefined") || planDate.equals("")  || !isValidDate(planDate)) {
					prepared.setNull(2, java.sql.Types.DATE);
				} 
				else {
					Date date = sdf2.parse(planDate);
					prepared.setDate(2, convertJavaDateToSqlDate(date) );
				}   
				prepared.setString(3, bean.getUserId());  
				prepared.setTimestamp(4, new Timestamp(time));
				prepared.setString(5, bean.getLotNo());  
				prepared.executeUpdate();  
				bean.setIconStatus("I"); 
				bean.setSystemStatus("Update Success.");
		} catch (SQLException | ParseException e) { 
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

	private ArrayList<PCMSSecondTableDetail> handlerReplacedProdOrder(PCMSSecondTableDetail bean) {
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
		ArrayList<SwitchProdOrderDetail> listSWMainOne = this.getSwitchProdOrderDetailByPrd(prdOrder); 
		ArrayList<SwitchProdOrderDetail> listSWMainTwo = this.getSwitchProdOrderDetailByPrdSW(prdOrder);  
		String tableName = "[InputReplacedRemark]";     
		boolean numeric = true; 	
		boolean errCheck = true; 
		String[] newRPSplit = repRemark.split(",");  
//		for(int i = 0 ; i < mainSplit.length; i++) {
//			poListTmp.clear();
//			PCMSSecondTableDetail beanTmp = new PCMSSecondTableDetail();
//			String[] subSplit = mainSplit[i].split("="); 
//			if(subSplit.length == 1) {
//				prdOrderRP = mainSplit[i].trim(); 
//			}
//			else {
//				prdOrderRP = subSplit[0].trim(); 
//			}   
//			beanTmp.setProductionOrder(prdOrderRP);
//			poListTmp.add(beanTmp);
////			listOPMainTwo = this.getOrderPuangListByPrd( poListTmp);   
////			if(listOPMainTwo.size() > 0) {
////				String prodOrderCheck = "";
////				if(listOPMainTwo.size() > 0) {
////					prodOrderCheck = listOPMainTwo.get(0).getProductionOrder();
////				} 
////				poList.clear();  
////				bean.setReplacedRemark(""); 
////				bean = this.updateLogRemarkCaseFix(tableName,"",bean);   
////				bean.setIconStatus("E");
////				bean.setSystemStatus(prodOrderCheck+" is OrderPuang so it can't replaced .");
////				poList.add(bean);  
////				errCheck = false; 
////				break;
////			} 
//		}
		
		
//		poListTmp.clear();
//		poListTmp.add(bean);
//		listOPMainTwo = this.getOrderPuangListByPrd( poListTmp);     
		listRP.clear();
		if(errCheck == false) {  }
		else if(repRemark.equals("")) { 
//			-----------------------------------------------------
			list.add(bean);   
			listRP = this.getReplacedCaseByProdOrder(this.C_PRODORDER,list);    
			bean = this.updateReplacedPrd(bean, "X");     
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
//		else if(listSWMainOne.size() > 0) {
//			String prodOrderCheck = "";
//			if(listSWMainOne.size() > 0) { prodOrderCheck = listSWMainOne.get(0).getProductionOrder(); }
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Prod.Order already switch between "+prdOrder+" and "+ prodOrderCheck+".");
//			poList.add(bean);  
//		}
		else if(listSWMainTwo.size() > 0) {
			String prodOrderCheck = "";
			if(listSWMainTwo.size() > 0) {
				prodOrderCheck = listSWMainTwo.get(0).getProductionOrder();
			}
			bean.setIconStatus("E");
			bean.setSystemStatus("Prod.Order already switch between "+prdOrder+" and "+ prodOrderCheck+".");
			poList.add(bean);  
		}
//		else if(listOPMainTwo.size() > 0) {
//			String prodOrderCheck = "";
//			if(listOPMainTwo.size() > 0) {
//				prodOrderCheck = listOPMainTwo.get(0).getProductionOrder();
//			}
//			bean.setReplacedRemark(""); 
//			bean = this.updateLogRemarkCaseFix(tableName,"",bean);     
//			bean.setIconStatus("E");
//			bean.setSystemStatus(prodOrderCheck+" is OrderPuang so it can't replaced !");
//			poList.add(bean);  
//		}
		else if(newRPSplit.length > 0) {    
//			list.add(bean);         
			prdOrder = bean.getProductionOrder().trim();  
			ArrayList<ReplacedProdOrderDetail> listRPOld = this.getReplacedProdOrderDetailByPrdMain(prdOrder);
			bean = this.updateReplacedPrd(bean, "X");  
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
						 Double num = Double.parseDouble(volume);
			        } catch (NumberFormatException e) { numeric = false; }
					 if(numeric) { }
					 else { volume = "0";  } 
				} 
				if(!prdOrderRP.equals("")) {
					checkList = getFromSapMainProdDetail(prdOrderRP);
					if(checkList.size() == 0) { 
						errCheck = false;
						bean.setIconStatus("E");
						bean.setSystemStatus(prdOrderRP+" is not in the Database or wrong data entry. ");  
						poList.add(bean);
						break;
					}
					else {
						PCMSSecondTableDetail beanCheckRP = checkList.get(0);
//						String saleOrderRP = beanCheckRP.getSaleOrder().trim() ;
//						String saleLineRP = beanCheckRP.getSaleLine().trim() ;  
						listRP.add(beanCheckRP);
						
						beanRP.setProductionOrder(prdOrder);
						beanRP.setSaleOrder(saleOrder);
						beanRP.setSaleLine(saleLine);
						beanRP.setProductionOrderRP(prdOrderRP ); 
						beanRP.setChangeBy(userId);  
						beanRP.setVolume(volume);  
						ReplacedProdOrderDetail beanX = this.upsertReplacedPrd(beanRP,"O");
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
		
		String sql = 
				 " SELECT DISTINCT  \r\n"       
				+ this.selectWaitLot 
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n " 
				+ this.innerJoinWaitLotB 
				+ this.leftJoinJ
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
		ArrayList<PCMSSecondTableDetail> list = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poListTMP = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poListOld = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poListOP = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> poListOPSW = new ArrayList<PCMSSecondTableDetail>();
		String prdOrderSW = bean.getSwitchRemark().trim();
		String prdOrder = bean.getProductionOrder().trim();
		String sql = 
				  " SELECT \r\n"
				+ "			a.[ProductionOrder]\r\n"
				+ "      	,[SaleOrder]\r\n"
				+ "      	,[SaleLine] \r\n"
				+ "	     	,isnull( countInSW ,0) as CountInSW\r\n"
				+ "  FROM [PCMS].[dbo].[FromSapMainProd] AS A\r\n"
				+ "  LEFT JOIN (SELECT ProductionOrderSW, COUNT(id) as countInSW  \r\n"
				+ "			  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "			  where DataStatus = 'O' AND  ProductionOrderSW = '"+prdOrderSW+"' \r\n"
				+ "			  group by ProductionOrderSW ) AS B ON A.ProductionOrder = B.ProductionOrderSW\r\n"
				+ "  where SaleOrder <> '' AND a.ProductionOrder = '"+prdOrderSW+"' \r\n"
				+ " ";  
		if(prdOrderSW.equals("")) {       
			list.add(bean);   
			poList = this.getSwitchProdOrderListByPrd(list);      
			bean = this.updateSwitchPrd(bean, "X");    
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
			List<Map<String, Object>> datas = this.database.queryList(sql); 
			for (Map<String, Object> map : datas) { list.add(this.bcModel._genPCMSSecondTableDetail(map)); }
			if(list.size() > 0 ) {
				PCMSSecondTableDetail beanL = list.get(0);
				ArrayList<ReplacedProdOrderDetail> listRPSubOne = this.getReplacedProdOrderDetailByPrdRP(prdOrder);
				ArrayList<ReplacedProdOrderDetail> listRPSubTwo = this.getReplacedProdOrderDetailByPrdRP(prdOrderSW);
				ArrayList<ReplacedProdOrderDetail> listRPMainOne = this.getReplacedProdOrderDetailByPrd(prdOrder);
				ArrayList<ReplacedProdOrderDetail> listRPMainTwo = this.getReplacedProdOrderDetailByPrd(prdOrderSW);
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
					ArrayList<SwitchProdOrderDetail> listCheck = this.getSwitchProdOrderDetailByPrdSW(prdOrderSW);
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
					ArrayList<SwitchProdOrderDetail> listSW = this.getSwitchProdOrderDetailByPrd(prdOrder);  
					bean = this.updateSwitchPrd(bean, "X");  
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
					beanD = this.upsertSwitchPrd(beanD, "O");
					//  X2 
//					beanD.setSaleOrder(bean.getSaleOrder());              //1
//					beanD.setSaleLine(bean.getSaleLine());                //1
//					beanD.setProductionOrder(bean.getProductionOrder());  //A
					bean.setProductionOrderSW(prdOrder);                  //A 
					bean.setSaleOrderSW(beanL.getSaleOrder());            //2
					bean.setSaleLineSW(beanL.getSaleLine());              //1   
					bean = this.upsertSwitchPrd(bean, "O"); 
//					bean.setIconStatus("I");
//					bean.setSystemStatus("Update Success.");  
					poList.add(bean);       
//					if(poListOP.size() > 0) { poList.add(poListOP.get(0)); }    
//					if(poListOld.size() > 0) { poList.add(poListOld.get(0)); }    
//					poListOP.clear();
//					poListOld.clear();
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
				bean = this.updateSwitchPrd(bean, "X");
				bean.setIconStatus("E");
				bean.setSystemStatus("ProductionOrder is not in the Database or wrong data entry. ");  
				poList.add(bean);  
			} 
		}    
		return poList; 
	} 
	private ReplacedProdOrderDetail upsertReplacedPrd( ReplacedProdOrderDetail bean ,String dataStatus) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();    
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		String prdOrderRP = bean.getProductionOrderRP();   
		String volume = bean.getVolume();   
		String userID = bean.getChangeBy();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();	
		long time = currentTime.getTime(); 
		String sql =  
				  " UPDATE [PCMS].[dbo].[ReplacedProdOrder] " 
				+ " SET [Volume] = ? , [DataStatus] = ? ,[ChangeBy] = ?,[ChangeDate]  = ? "
				+ " WHERE [ProductionOrder] = ? and [SaleOrder] = ? and [SaleLine] = ? and "
				+ "       [ProductionOrderRP] = ? "
				+ " declare  @rc int = @@ROWCOUNT " // 56	
				+ "  if @rc <> 0 " 
				+ " print @rc " 
				+ " else "
				+ " INSERT INTO [PCMS].[dbo].[ReplacedProdOrder]" 
				+ "  ([ProductionOrder] ,[SaleOrder] ,[SaleLine], [ProductionOrderRP], [Volume], "
				+ "   [ChangeBy] ,[ChangeDate] )"//55 
				+ " values(? , ? , ? , ? , ? "
				+ "      , ? , ? "
				+ "      )  "
				+ ";"  ;     	
		try {      
			 
				prepared = connection.prepareStatement(sql);  
				prepared.setDouble(1, Double.parseDouble(volume));
				prepared.setString(2, dataStatus);
				prepared.setString(3, userID);
				prepared.setTimestamp(4, new Timestamp(time));  
				prepared.setString(5, prdOrder);  
				prepared.setString(6, saleOrder);  
				prepared.setString(7, saleLine);  
				prepared.setString(8, prdOrderRP);  
				 
				prepared.setString(9, prdOrder);  
				prepared.setString(10, saleOrder);  
				prepared.setString(11, saleLine);  
				prepared.setString(12, prdOrderRP);  
				prepared.setString(13, volume);  
				prepared.setString(14, userID);    
				prepared.setTimestamp(15, new Timestamp(time));   
				 prepared.executeUpdate(); 
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) { 
			System.out.println(e);
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean;
	}
	private PCMSSecondTableDetail updateReplacedPrd( PCMSSecondTableDetail bean ,String dataStatus) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();    
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String caseSave = bean.getCaseSave();
		try {      
			String sql =  
					  " UPDATE [PCMS].[dbo].[ReplacedProdOrder]" 
					+ " 	SET [DataStatus] = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " 	WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ?  "
					+ " declare  @rc int = @@ROWCOUNT "  
					+ ";"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, dataStatus);
				prepared.setString(2, bean.getUserId());
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);  
				prepared.setString(5, saleOrder);  
				prepared.setString(6, saleLine);  
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("ReplacedProdOrder"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean;
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

	private ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdRP(String prodOrder) {
		ArrayList<ReplacedProdOrderDetail> list = null; 
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n" 
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderRP] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrderRP] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";       
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<ReplacedProdOrderDetail>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genReplacedProdOrderDetail(map)); }
		return list;
	}
	private ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrd(String prodOrder) {
		ArrayList<ReplacedProdOrderDetail> list = null; 
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n" 
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderRP] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";      
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<ReplacedProdOrderDetail>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genReplacedProdOrderDetail(map)); }
		return list;
	}
	private ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMain(String prodOrder) {
		ArrayList<ReplacedProdOrderDetail> list = null; 
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n" 
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";       
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<ReplacedProdOrderDetail>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genReplacedProdOrderDetail(map)); }
		return list;
	}
	private ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrdSW(String prodOrder) {
		ArrayList<SwitchProdOrderDetail> list = null; 
		String sql = 
				  " SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrderSW]\r\n"   
				+ "      ,[SaleLineSW]\r\n"
				+ "      ,[ProductionOrderSW]\r\n"  
				+ "  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderSW] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrderSW] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";      
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SwitchProdOrderDetail>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genSwitchProdOrderDetail(map)); }
		return list;
	}
	private ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrd(String prodOrder) {
		ArrayList<SwitchProdOrderDetail> list = null; 
		String sql = 
				  " SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrderSW]\r\n"   
				+ "      ,[SaleLineSW]\r\n"
				+ "      ,[ProductionOrderSW]\r\n"  
				+ "  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderSW] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";      
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SwitchProdOrderDetail>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genSwitchProdOrderDetail(map)); }
		return list;
	}
	private ArrayList<SwitchProdOrderDetail> getSWProdOrderDetailByPrd(String prodOrder) {
		ArrayList<SwitchProdOrderDetail> list = null; 
		String sql = "SELECT \r\n"
				+ "       [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrderSW]\r\n"   
				+ "      ,[SaleLineSW]\r\n"
				+ "      ,[ProductionOrderSW]\r\n"  
				+ "  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "  where DataStatus = 'O' and \r\n"
				+ "       [ProductionOrderSW] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";      
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SwitchProdOrderDetail>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genSwitchProdOrderDetail(map)); }
		return list;
	}
	private PCMSSecondTableDetail upsertSwitchPrd( PCMSSecondTableDetail bean ,String dataStatus) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();    
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		String prdOrderSW = bean.getProductionOrderSW();   
		String saleOrderSW = bean.getSaleOrderSW();    
		String saleLineSW = String.format("%06d", Integer.parseInt(bean.getSaleLineSW()));  
		String userID = bean.getUserId();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime(); 
		try {      
			String sql =  
					  " UPDATE [PCMS].[dbo].[SwitchProdOrder]" 
					+ " 	SET [DataStatus] = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? AND"
					+ "       [ProductionOrderSW]  = ? and [SaleOrderSW] = ?  and [SaleLineSW] = ?  "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ " if @rc <> 0 " 
					+ " 	print @rc " 
					+ " else "
					+ " 	INSERT INTO [PCMS].[dbo].[SwitchProdOrder]" 
					+ " 	([ProductionOrder]  ,[SaleOrder]   ,[SaleLine],"
					+ " 	 [ProductionOrderSW],[SaleOrderSW] ,[SaleLineSW],"
					+ "  	[ChangeBy] ,[ChangeDate] )"//55 
					+ " 	values(? , ? , ? , ? , ? "
					+ "    	     , ? , ? , ?  "
					+ "     )  "
					+ ";"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, dataStatus);
				prepared.setString(2, bean.getUserId());
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);  
				prepared.setString(5, saleOrder);  
				prepared.setString(6, saleLine);  
				prepared.setString(7, prdOrderSW);  
				prepared.setString(8, saleOrderSW);  
				prepared.setString(9, saleLineSW);  
				
				prepared.setString(10, prdOrder);  
				prepared.setString(11, saleOrder);  
				prepared.setString(12, saleLine);  
				prepared.setString(13, prdOrderSW);  
				prepared.setString(14, saleOrderSW);  
				prepared.setString(15, saleLineSW);   
				prepared.setString(16, userID);  
				prepared.setTimestamp(17, new Timestamp(time));   
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
//			System.err.println("upsertSwitchPrd"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean;
	}
	private PCMSSecondTableDetail updateSwitchPrd( PCMSSecondTableDetail bean ,String dataStatus) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
//		String saleOrder = bean.getSaleOrder();    
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String caseSave = bean.getCaseSave();
		try {      
			String sql =  
					  " UPDATE [PCMS].[dbo].[SwitchProdOrder]" 
					+ " 	SET [DataStatus] = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? "
//					+ "and [SaleOrder] = ?  and [SaleLine] = ?  "
					+ " declare  @rc int = @@ROWCOUNT "  
					+ ";"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, dataStatus);
				prepared.setString(2, bean.getUserId());
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);  
//				prepared.setString(5, saleOrder);  
//				prepared.setString(6, saleLine);  
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("upsertSwitchPrd"+e.getMessage());
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
//					"UPDATE [PCMS].[dbo]."+tableName
//					+ " SET "+caseSave+" = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
//					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ?  "
//					+ " declare  @rc int = @@ROWCOUNT " // 56
//					+ "  if @rc <> 0 " 
//					+ " print @rc " 
//					+ " else "+
					  " INSERT INTO [PCMS].[dbo]."+tableName
					+ " ([ProductionOrder],[SaleOrder] ,[SaleLine],"+caseSave+",[ChangeBy] ,[ChangeDate])"//55 
					+ " values \r\n"
					+ "	(? , ? , ? , ? , ? "
					+ ", ? )  "
					+ ";"  ;     	 
				prepared = connection.prepareStatement(sql);    
//				prepared.setString(1, valueChange);
//				prepared.setString(2, bean.getUserId());
//				prepared.setTimestamp(3, new Timestamp(time));  
//				prepared.setString(4, prdOrder);  
//				prepared.setString(5, saleOrder);  
//				prepared.setString(6, saleLine);  
//				prepared.setString(7, prdOrder);  
//				prepared.setString(8, saleOrder);  
//				prepared.setString(9, saleLine);  
//				prepared.setString(10, valueChange);  
//				prepared.setString(11, bean.getUserId());  
//				prepared.setTimestamp(12, new Timestamp(time));  
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
//					"UPDATE [PCMS].[dbo]."+tableName
//					+ " SET "+caseSave+" = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
//					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? and [Grade] = ? "
//					+ " declare  @rc int = @@ROWCOUNT " // 56
//					+ "  if @rc <> 0 " 
//					+ " print @rc " 
//					+ " else "+
					  " INSERT INTO [PCMS].[dbo]."+tableName
					+ " 	([ProductionOrder],[SaleOrder] ,[SaleLine],"+caseSave+",[ChangeBy] "
					+ " 	,[ChangeDate],[Grade])"//55 
					+ " values \r\n"
					+ "		(? , ? , ? , ? , ? "
					+ "    , ? , ? )  ;"  ;     	
				prepared = connection.prepareStatement(sql);    
//				prepared.setString(1, valueChange);
//				prepared.setString(2, bean.getUserId());
//				prepared.setTimestamp(3, new Timestamp(time));  
//				prepared.setString(4, prdOrder);  
//				prepared.setString(5, saleOrder);  
//				prepared.setString(6, saleLine);  
//				prepared.setString(7, grade);  
//				prepared.setString(8, prdOrder);  
//				prepared.setString(9, saleOrder);  
//				prepared.setString(10, saleLine);  
//				prepared.setString(11, valueChange);  
//				prepared.setString(12, bean.getUserId());  
//				prepared.setTimestamp(13, new Timestamp(time));  
//				prepared.setString(14, grade);
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

	@Override
	public ArrayList<InputDateDetail> getCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				      " SELECT \r\n"
		    		+ "		  [ProductionOrder]\r\n"
				    + "      ,[SaleOrder]\r\n"
				    + "      ,[SaleLine]\r\n"
				    + "      ,[PlanDate]\r\n"
				    + "      ,[CreateBy]\r\n"
				    + "      ,[CreateDate]\r\n"
				    + "	  	 ,'0:PCMS' as InputFrom \r\n" 
				    + "      ,LotNo \r\n"  
				    + " FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n" 
				    + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and \r\n"
				    + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
				    + "       a.[SaleLine] = '" +  saleLine+ "' \r\n" 
		 		  	+ " ORDER BY InputFrom ,CreateDate desc "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	} 
	@Override
	public ArrayList<InputDateDetail> getCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql =  
			  " SELECT \r\n"
			+ "		  [ProductionOrder]\r\n"
		    + "      ,[SaleOrder]\r\n"
		    + "      ,[SaleLine]\r\n"
		    + "      ,[PlanDate]\r\n"
		    + "      ,[CreateBy]\r\n"
		    + "      ,[CreateDate]\r\n"
		    + "	 	 , '1:PCMS' as InputFrom \r\n"
		    + "   	 , LotNo \r\n"
		    + " FROM [PCMS].[dbo].[PlanCFMDate] as a\r\n" 
		    + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and \r\n"
		    + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
		    + "       a.[SaleLine] = '" + saleLine+ "' \r\n"
//		  + " union \r\n "
//		  + " SELECT [ProductionOrder]\r\n"
//		  + "      ,[SaleOrder]\r\n"
//		  + "      ,[SaleLine]\r\n"
//		  + "      ,SubmitDate as [PlanDate]\r\n"  
//		  + "      ,'' AS [CreateBy]\r\n"
//		  + "      ,null AS [CreateDate]\r\n"
//	      + "	    , '0:SAP' as InputFrom \r\n"
//	      + "   ,'' AS LotNo \r\n"
//		  + " FROM [PCMS].[dbo].[FromSapSubmitDate]  as a\r\n" 
//		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and SubmitDate is not null "
//  		  + "   and [DataStatus] = 'O' "
		  + " ORDER BY InputFrom ,CreateDate desc ";
				
		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	} 
	@Override
	public ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
					  " SELECT \r\n"
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
				 	+ " union \r\n "
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
	public ArrayList<InputDateDetail> getMaxCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				    " SELECT distinct \r\n"
				  + "		 a.[ProductionOrder] ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] ,[CreateBy]\r\n"
	    		  + "      	,[CreateDate] \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and \r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and \r\n"
		 		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
		 		  + "       a.[SaleLine] = '" + saleLine+ "' and \r\n"
		  		  + "       a.[PlanDate] = CONVERT(DATE,'"+bean.getCFMPlanLabDate()+ "',103)  ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	public ArrayList<InputDateDetail> getCountCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0); 
		String sql = 
				    " SELECT distinct \r\n"
				  + "		count(a.[ProductionOrder]) as countAll \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMLabDate] as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMLabDate] \r\n"
		 		  + "			  group by [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) as b \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[PlanDate] = CONVERT(DATE,'"+bean.getCFMPlanLabDate()+ "',103) ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	public ArrayList<InputDateDetail> getCountDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0); 
		String sql = 
				    " SELECT distinct count(a.[ProductionOrder]) as countAll \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanDeliveryDate] as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanDeliveryDate] \r\n"
		 		  + "			  group by [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) as b \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[PlanDate] = CONVERT(DATE,'"+bean.getDeliveryDate()+ "',103) ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	public ArrayList<InputDateDetail> getMaxDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				    " SELECT distinct \r\n"
				  + "       a.[ProductionOrder]  \r\n"
				  + "      ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] ,[CreateBy]\r\n"
	    		  + "      ,[CreateDate] \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder()  + "' and \r\n"
		 		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
		 		  + "       a.[SaleLine] = '" + saleLine+ "' and \r\n"
		  		  + "       a.[PlanDate] = CONVERT(DATE,'"+bean.getDeliveryDate()+ "',103) ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	public ArrayList<InputDateDetail> getCountCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0); 
		String sql = 
				    " SELECT distinct count(a.[ProductionOrder]) as countAll  \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[PlanDate] = CONVERT(DATE,'"+bean.getCFMPlanDate()+ "',103) ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);  
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) { 
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	public ArrayList<InputDateDetail> getMaxCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				    " SELECT distinct \r\n"
				  + " 		a.[ProductionOrder]  ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] ,[CreateBy]\r\n"
	    		  + "      ,[CreateDate] \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder()+ "' and \r\n"
		 		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
		 		  + "       a.[SaleLine] = '" + saleLine+ "' and \r\n"
 		  		  + "       a.[PlanDate] = CONVERT(DATE,'"+bean.getCFMPlanDate()+ "',103) \r\n";
		 		  ; 
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
	public ArrayList<ColumnHiddenDetail> getColVisibleDetail(String user) {
		ArrayList<ColumnHiddenDetail> list = null;  
		String sql = 
				    " SELECT distinct \r\n"
				  + "		[EmployeeID] ,[ColVisibleDetail] ,[ColVisibleSummary]\r\n"
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
		String colName = pd.getColVisibleDetail(); 
		String user = pd.getUserId(); 
		ArrayList<ColumnHiddenDetail> list = new ArrayList<ColumnHiddenDetail>();
		ColumnHiddenDetail bean = new ColumnHiddenDetail();
//		ArrayList<ColumnHiddenDetail> beanCheck = this.getColHiddenDetail(leftJoinB);
//		if(beanCheck.size() > 0) { 
//		} 
		try {      
			String sql = 
					  " UPDATE [PCMS].[dbo].[ColumnSetting] "
					+ " 	SET [ColVisibleDetail] = ?  "
					+ " 	WHERE [EmployeeID]  = ? "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ " if @rc <> 0 " 
					+ " 	print @rc " 
					+ " else "
					+ " 	INSERT INTO [PCMS].[dbo].[ColumnSetting]	 "
					+ " 		([EmployeeID] ,[ColVisibleDetail])"//55 
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
			System.err.println("saveColSettingToServer"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getUserStatusList() {
		ArrayList<PCMSAllDetail> list = null;
		String where = " where UserStatus <> '' \r\n"; 
		String sql = 
				  "SELECT distinct \r\n"
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
				  "SELECT distinct \r\n"
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
	private String forPage = "Detail"; 
	@Override    
	public ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList) { 
		ArrayList<PCMSTableDetail> list = null;
		String where = " where  ";
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = ""  ,divisionName = "";
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
			list = this.insertSearchSettingDetai(poList);
		}
		else {
			list = this.updateSearchSettingDetai(poList);
		} 
		return list; 
	}

	private ArrayList<PCMSTableDetail> updateSearchSettingDetai(ArrayList<PCMSTableDetail> poList) {
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
					+ "   where  [EmployeeID] = ? and [ForPage] = ?" ;     	
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
			System.out.println("updateSearchSettingDetai"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}

	private ArrayList<PCMSTableDetail> insertSearchSettingDetai(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();    
		ArrayList<PCMSTableDetail> list = new ArrayList<PCMSTableDetail>(); 
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,division = ""; ;
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
				    " SELECT \r\n"     
				  + "	[EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
				  + "  ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
				  + "  ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
				  + "  ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[Division]\r\n"
				  + " FROM [PCMS].[dbo].[SearchSetting]\r\n"
				  + " where [EmployeeID] = '"+userId+"' and [ForPage] = '"+forPage+ "' "; 
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
				  "SELECT distinct \r\n"
			    + "		[Division] \r\n"   
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
		
		String sqlMain = 
				"" 
			 	+ this.createTempSumGR
				+ " SELECT DISTINCT \r\n "       
				+ this.selectMain
				+ "   ,CASE  \r\n"    
				+ "     	WHEN ( b.SumVol is not null and ( b.Grade = 'A' or b.Grade is null or b.Grade  = '') ) THEN b.SumVol \r\n"  
				+ "			ELSE  NULL\r\n"   
				+ "			END AS Volumn   \r\n"
				+ "   , CASE  \r\n" 
				+ "     	WHEN ( b.SumVolFGAmount is not null and ( b.Grade = 'A' or b.Grade is null or b.Grade  = '' ) ) THEN b.SumVolFGAmount \r\n"  
				+ "			ELSE  NULL\r\n"     
				+ "			END AS VolumnFGAmount  \r\n"
				+ "   , 'Main' as TypePrd \r\n"
				+ "   , 'Main' AS TypePrdRemark \r\n"   
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n " 
				+ this.leftJoinBPartOne
//				+ where  
				+ this.leftJoinBPartTwo
//				+ this.leftJoinC	 
//				+ this.leftJoinCFMLastD    
				+ this.leftJoinE  
//				+ this.leftJoinH
//				+ this.leftJoinCFMLastNoStatI
//				+ this.leftJoinLB  
				+ this.leftJoinJ
				+ this.leftJoinK   
//				+ this.leftJoinM
				+ this.leftJoinMainl
//				+ this.leftJoinNToO 
				+ this.leftJoinP
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD   
				+ this.leftJoinQ 
				+ this.leftJoinSL
//				+ this.leftJoinTempG
//				+ this.leftJoinR 
//				+ this.leftJoinUCAL
//				+ this.leftJoinCSW  
				+ this.leftJoinCRP
//				+ this.leftJoinCOP    
				+ where  
//				+ " where "
//				+ " and R.ProductionOrderSW is null \r\n"
				+ " and ( b.SumVol <> 0\r\n"
				+ "		or ( (  b.LotNo = 'รอจัด Lot'  or  b.LotNo = 'ขาย stock' or b.LotNo = 'รับจ้างถัก' or b.LotNo = 'Lot ขายแล้ว' \r\n"
				+ "				or b.LotNo = 'พ่วงแล้วรอสวม' or b.LotNo = 'รอสวมเคยมี Lot'  )  and b.SumVol = 0 \r\n"
//				+ "		     and ( countnRP is null and countSW is null and countnOP is null )  \r\n" 
				+ "		     and ( countnRP is null )  \r\n"
				+ "        ) \r\n" 
				+ "     or  RealVolumn = 0 \r\n"
				+ "     or ( b.UserStatus = 'ยกเลิก' or b.UserStatus = 'ตัดเกรด Z') \r\n"
				+ "     )\r\n"
				+ " and NOT EXISTS ( select distinct ProductionOrderSW\r\n"
				+ "				   	 FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA\r\n"
				+ "				   	 WHERE DataStatus = 'O' and BAA.ProductionOrderSW = B.ProductionOrder\r\n"
				+ "				   )  \r\n "   
				+ " Order by a.CustomerShortName, a.DueDate,a.[SaleOrder], [SaleLine],b.[ProductionOrder]" 
					;         
//		System.out.println(sql);
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
			}else if(prdOrderType.equals(this.C_PRODORDERRP)) {
				prodOrder = poList.get(i).getProductionOrderRP();
			}  
			where = where + " b."+prdOrderType +" = '"+prodOrder+"' ";
//			whereTempUCAL = whereTempUCAL + " a."+prdOrderType +" = '"+prodOrder+"' ";
			if (i != poList.size() - 1) {
				where += " or ";   
//				whereTempUCAL += " or ";   
			} ;
		}
		where += " ) \r\n";
//		whereTempUCAL += " ) \r\n";
		String sqlRP =  ""
//				  this.createTempUCAL//+whereTempUCAL
//				+ this.createTempG
//				+ this.createTempUCALRP
//			 	+ this.createTempCFM
			 	+ this.createTempSumGR
			 	+ " SELECT DISTINCT  \r\n"       
				+ this.selectRP
				+ "   , CASE \r\n"
	    		+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Volumn\r\n" 
	    		+ "			ELSE NULL\r\n"
	    		+ "			END AS Volumn  \r\n"  
	    		+ "   , CASE \r\n"
	    		+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Price * a.Volumn \r\n" 
	    		+ "			ELSE NULL\r\n"
	    		+ "			END AS VolumnFGAmount \r\n"
				+ "   ,'Replaced' as TypePrd \r\n"
    			+ "   ,TypePrdRemark \r\n"
				+ " FROM (  SELECT DISTINCT \r\n"
				+ "             b.[SaleOrder]  , b.[SaleLine] ,a.DistChannel ,a.Color ,a.ColorCustomer  \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity ,a.SaleUnit ,a.DueDate ,a.CustomerShortName \r\n"
				+ "           , a.[SaleFullName] , a.[SaleNumber] ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus \r\n"
				+ "	          , b.[ProductionOrderRP] as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount \r\n"
				+ "  		  , a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ "   		  , CASE \r\n"
				+ "					WHEN b.Volume <> 0 THEN b.Volume\r\n"
				+ "					ELSE c.Volumn\r\n" 
				+ "					END AS Volumn  \r\n" 
				+ " 		  , 'SUB' as TypePrdRemark \r\n"  
				+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a \r\n"
				+ "		 	inner join [PCMS].[dbo].[ReplacedProdOrder] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder  \r\n"
				+ "		 	left join [PCMS].[dbo].[FromSapMainProd] as c on c.ProductionOrder = b.[ProductionOrderRP]\r\n"
				+ "      	where b.DataStatus <> 'X' and\r\n"
				+ "            "+where+"  ) as a  \r\n "  
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n" 
//				+ this.leftJoinC	      
//				+ this.leftJoinCFMLastD   
				+ this.leftJoinE    
				+ this.leftJoinTempG    
				+ this.leftJoinSCC
				+ this.leftJoinH
//				+ this.leftJoinCFMLastNoStatI    
//				+ this.leftJoinLB  
				+ this.leftJoinJ
				+ this.leftJoinK
				+ this.leftJoinM       
				+ this.leftJoinl 
//				+ this.leftJoinNToO 	
				+ this.leftJoinP
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinQ
//				+ this.leftJoinUCAL
				+ this.leftJoinUCALRP
				+ this.leftJoinSL
				+ this.leftJoinFSMBB
				+ " where \r\n"  
				+ " 	( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"  
//				+ " ( c.DataStatus <> 'X'  OR C.DataStatus IS NULL ) "   
				;  
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
		String sqlSW = ""
			 	+ this.createTempSumGR
			 	+ this.selectSW
				+ "   , CASE  \r\n"
    			+ "			WHEN a.Grade = 'A' OR a.Grade is null THEN  ( b.Volumn - a.SumVol )\r\n"
    			+ "			ELSE  NULL\r\n"
    			+ "			END AS Volumn   \r\n"  
    			+ "   , CASE  \r\n"
    			+ "			WHEN a.Grade = 'A' OR a.Grade is null THEN a.Price * ( b.Volumn - a.SumVol ) \r\n"
    			+ "			ELSE  NULL\r\n"
    			+ "			END AS VolumnFGAmount  \r\n"
				+ "   , 'Switch' as TypePrd \r\n"  
    			+ "   , TypePrdRemark \r\n"
				+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
				+ "	          , b.ProductionOrderSW as ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
				+ "  		  ,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ " 		  , CASE \r\n"
				+ "					when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
				+ "					ELSE 'SUB' \r\n"
				+ "					END	TypePrdRemark  ,C.SumVol\r\n "
//				+ "           , m.[Grade],[PriceSTD],GRSumMR,GRSumKG,GRSumYD,UCAL.UserStatusCal \r\n"   
//				+ "			, FSMBB.BillSendWeightQuantity \r\n"
//				+ "			, case\r\n"
//				+ "					WHEN a.SaleUnit  = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
//				+ "					WHEN a.SaleUnit  = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
//				+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
//				+ "				end AS BillSendQuantity \r\n"
//				+ "			, FSMBB.BillSendMRQuantity\r\n"
//				+ "			, FSMBB.BillSendYDQuantity \r\n"
				+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
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
//				+ "         "+ this.leftJoinM   
//				+ "         "+ this.leftJoinUCAL  
//			    + "         "+ this.leftJoinFSMBB
				+ "		    where b.DataStatus <> 'X') as a  \r\n "  
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"  
//				+ this.leftJoinC	       
				+ this.leftJoinE    
				+ this.leftJoinTempG 
				+ this.leftJoinSCC
				+ this.leftJoinH  
//				+ this.leftJoinLB  
				+ this.leftJoinJ
				+ this.leftJoinK  
				+ this.leftJoinMainl  
//				+ this.leftJoinNToO 
				+ this.leftJoinP
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinQ 
				+ this.leftJoinSL
				+ this.leftJoinM   
				+ this.leftJoinUCAL  
			    + this.leftJoinFSMBB
				+ " where \r\n"  
				+ " 	( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"  
//				+ " ( c.DataStatus <> 'X'  OR C.DataStatus IS NULL ) "   
				;    
//		System.out.println(sql);
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
		String sqlOP =  ""
//				this.createTempUCAL 
//				+this.createTempG
//			 	+ this.createTempCFM
		 	+ this.createTempSumGR
			+ " SELECT DISTINCT \r\n"       
			+ this.selectOP 
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
			+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
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
//					+ "			  ,g.SendCFMCusDate \r\n"
			+ " 		  , CASE \r\n"
			+ "					WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
			+ "    				ELSE  g.SendCFMCusDate \r\n"     
			+ "    				END AS SendCFMCusDate\r\n"

			+ "			  ,g.CFMDateActual\r\n"
			+ "			  , g.CFMDetailAll \r\n"
			+ "			  , g.CFMNumberAll \r\n"
			+ "			  , g.CFMRemarkAll \r\n"
			+ "			  , g.RollNoRemarkAll \r\n"
			+ "			  , CASE \r\n"
			+ "					WHEN ( g.DyePlan is NULL) THEN null \r\n"
			+ "				   	ELSE CAST(DAY( g.DyePlan) AS VARCHAR(2)) + '/' +   CAST(MONTH( g.DyePlan)AS VARCHAR(2))  \r\n"
			+ "			  		END AS DyePlan \r\n"
			+ "			  , CASE \r\n"
			+ "					WHEN ( g.DyeActual is NULL) THEN null \r\n"
			+ "				  	ELSE CAST(DAY( g.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH( g.DyeActual)AS VARCHAR(2))  \r\n"
			+ "			  		END AS DyeActual  \r\n"
			+ "			, FSMBB.BillSendWeightQuantity \r\n"
			+ "			, case\r\n"
			+ "					WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
			+ "					WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
			+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
			+ "				end AS BillSendQuantity \r\n" 
			+ "			, FSMBB.BillSendMRQuantity\r\n"
			+ "			, FSMBB.BillSendYDQuantity \r\n" 
			+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
			+ "		 	inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"  
			+ "         "+this.leftJoinTempG   
			+ "         "+this.leftJoinM   
			+ "         "+this.leftJoinUCAL   
		    + "         "+this.leftJoinFSMBB
			+ "         "+ this.leftJoinSCC
//			+ "         left join [PCMS].[dbo].[FromSapMainGrade] as FSMG on b.ProductionOrder = FSMG.ProductionOrder and FSMG.DataStatus = 'O'    \r\n"
//			+ "         left join [PCMS].[dbo].[PlanSendCFMCusDate] as SCC on b.ProductionOrder = SCC.ProductionOrder and SCC.DataStatus = 'O'    \r\n" 
			+ "		 	WHERE b.DataStatus <> 'X' AND\r\n" 
			+ "         	  "+where+" \r\n"
			+ "       ) as a  \r\n "  
			+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"  
//			+ this.leftJoinC	       
			+ this.leftJoinE    
//			+ this.leftJoinTempG   
			+ this.leftJoinH  
//			+ this.leftJoinLB  
			+ this.leftJoinJ
			+ this.leftJoinK      
			+ this.leftJoinMainl 
			+ this.leftJoinP
			+ this.leftJoinInputCOD 
			+ this.leftJoinInputDD  
			+ this.leftJoinQ 
			+ this.leftJoinSL
			+ " where \r\n"    
//			+ "  ( c.DataStatus <> 'X'  OR C.DataStatus IS NULL ) and "
//			+ " R.ProductionOrderSW is null \r\n" 
			+ " 	( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"
			+ " 	and NOT EXISTS ( select distinct ProductionOrderSW\r\n"
			+ "				   		 FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA\r\n"
			+ "				   		 WHERE DataStatus = 'O' and BAA.ProductionOrderSW = A.ProductionOrder\r\n"
			+ "				 		)   " 
//			+ "  AND a.Volumn <> '0' \r\n"   
;  
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
//				+ "           , m.[Grade],[PriceSTD],GRSumMR,GRSumKG,GRSumYD,UCAL.UserStatusCal\r\n" 
//				+ "			, FSMBB.BillSendWeightQuantity \r\n"
//				+ "			, case\r\n"
//				+ "					WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
//				+ "					WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
//				+ "					ELSE FSMBB.BillSendMRQuantity\r\n"
//				+ "				end AS BillSendQuantity \r\n"
//				+ "			, FSMBB.BillSendMRQuantity\r\n"
//				+ "			, FSMBB.BillSendYDQuantity \r\n"
				+ "		 	from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
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
			    + "         "+ this.leftJoinFSMBB 
				+ "		    where b.DataStatus <> 'X'  \r\n"
				+ "      	  AND "+where+" ) as a  \r\n "  
				+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"  
//				+ this.leftJoinC	       
				+ this.leftJoinE    
				+ this.leftJoinTempG   
				+ this.leftJoinSCC
				+ this.leftJoinH 
//				+ this.leftJoinLB  
				+ this.leftJoinJ
				+ this.leftJoinK 
				+ this.leftJoinMainl   
				+ this.leftJoinP
				+ this.leftJoinInputCOD 
				+ this.leftJoinInputDD  
				+ this.leftJoinQ 
				+ this.leftJoinSL 
				+ this.leftJoinM   
				+ this.leftJoinUCAL    
			    + this.leftJoinFSMBB
				+ " where \r\n"      
//				+ " ( c.DataStatus <> 'X'  OR C.DataStatus IS NULL )  AND "
//				+ " a.Volumn <> '0'\r\n" 
				+ "		( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรด Z') \r\n"     ; 
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
		String prodOrder = poList.get(0).getProductionOrder();
		ArrayList<SwitchProdOrderDetail> list = this.getSWProdOrderDetailByPrd(prodOrder); 
		if(list.size()> 0 ) { prodOrder = list.get(0).getProductionOrder(); }
		ArrayList<PCMSSecondTableDetail> listPST = new ArrayList<PCMSSecondTableDetail>(); 
		if(prodOrder.equals("")) {
			PCMSSecondTableDetail bean = new PCMSSecondTableDetail();
			bean.setIconStatus("E");
			bean.setSystemStatus("This Prod.Order already remove switch prod.order from other user.");
			listPST.add(bean);
		}  
		else {  
			String where = " ( \r\n"; 
			where += " a.ProductionOrder = '"+prodOrder+"' ";
			where += " ) \r\n";
			String sql = 
					  " SELECT   \r\n"
					+ "     case \r\n"
					+ "			when a.ProductionOrder = ProductionOrderSW then 'MAIN'\r\n"
					+ "			ELSE 'SUB' \r\n"
					+ "			END	TypePrdRemark \r\n" 
					+ "     ,[ProductionOrderSW] as ProductionOrder\r\n"
					+ "	    ,b.SwitchRemark\r\n"
					+ "     ,[DataStatus] \r\n"
					+ " FROM [PCMS].[dbo].[SwitchProdOrder]  as a\r\n"
					+ " left join [PCMS].[dbo].[InputSwitchRemark] as b on a.ProductionOrderSW = b.ProductionOrder\r\n"
					+ " WHERE DataStatus = 'O' AND \r\n"
					+where   ; 
  
			List<Map<String, Object>> datas = this.database.queryList(sql); 
			for (Map<String, Object> map : datas) {
				listPST.add(this.bcModel._genPCMSSecondTableDetail(map));   	
			}
		}    
		return listPST;
	}
	public ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrder) {
		ArrayList<PCMSSecondTableDetail> list = null;   
		String sql = 
				  " SELECT DISTINCT * \r\n"     
				+ " FROM [PCMS].[dbo].[FromSapMainProd] \r\n"
				+ " where \r\n "    
				+ " 	ProductionOrder = '"+prdOrder+"' and \r\n"
				+ " 	( DataStatus = 'O' ) "   ;     
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getDelayedDepartmentList() {
		ArrayList<PCMSSecondTableDetail> list = null; 
		String sql =      
				  " SELECT distinct [DepName] AS DelayedDep \r\n"     
				+ " FROM [PCMS].[dbo].[ConfigDepartment] \r\n"  
				+ " where DataStatus = 'O' \r\n" 
				+ " order by [DepName] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list; 
	}

	@Override
	public ArrayList<InputDateDetail> getSendCFMCusDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				  " SELECT [ProductionOrder]\r\n" 
			    + "       ,[SendCFMCusDate] as [PlanDate]\r\n"
			    + "       ,[ChangeBy] as [CreateBy]\r\n"
			    + "       ,[ChangeDate] as [CreateDate]\r\n"
			    + "	      , '0:PCMS' as InputFrom \r\n"
			    + "       , LotNo \r\n"
			    + " FROM [PCMS].[dbo].[PlanSendCFMCusDate] as a\r\n" 
			  	+ " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' \r\n" 
//					  + " union \r\n "
//					  + " SELECT [ProductionOrder]\r\n" 
//					    + "      ,[CFType] as [PlanDate]\r\n"
//					    + "      ,'' AS [CreateBy]\r\n"
//					    + "      ,null AS [CreateDate]\r\n"    
//					    + "	    , '1:SAP' as InputFrom \r\n"
//					    + "   ,'' AS LotNo \r\n"
//					  + " FROM [PCMS].[dbo].[TEMP_ProdWorkDate]  as a\r\n" 
//					  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "'   and CFType is not null  "
			  	+ " ORDER BY InputFrom ,[ChangeDate] desc ";
				  
		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;   
	}
}
