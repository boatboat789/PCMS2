	package dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
//import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import dao.PCMSDetailDao;
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
			    "  "
			  + " If(OBJECT_ID('tempdb..#tempPlandeliveryDate') Is Not Null)\r\n"
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
//			+ "	  ,b.CustomerType \r\n"
			+ "	  ,a.[Division] AS CustomerDivision\r\n"
			+ " INTO #tempMainSale \r\n"
			+ " FROM [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			+ " left join [PCMS].[dbo].[ConfigCustomerEX] as b on a.[CustomerNo] = b.[CustomerNo] and b.[DataStatus] = 'O'\r\n"
			;

	private String selectFromTempMainPrdTemp = ""

		+ "				SELECT distinct \r\n"
		+ "				     a.SaleOrder\r\n"
		+ "                ,a.SaleLine\r\n"
		+ "                ,CASE\r\n"
		+ "                   WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]\r\n"
		+ "                   WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
		+ "                   WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
		+ "                   WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
		+ "                   ELSE 'รอจัด Lot'\r\n"
		+ "                 END AS [ProductionOrder]\r\n"
		+ "                ,CASE\r\n"
		+ "                   WHEN b.[ProductionOrder] is not null THEN b.[LotNo]\r\n"
		+ "                   WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
		+ "                   WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
		+ "                   WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
		+ "                   ELSE 'รอจัด Lot'\r\n"
		+ "                 END AS [LotNo]\r\n"
		+ "                ,b.[TotalQuantity]\r\n"
		+ "                ,b.[Unit]\r\n"
		+ "                ,b.[RemAfterCloseOne]\r\n"
		+ "                ,b.[RemAfterCloseTwo]\r\n"
		+ "                ,b.[RemAfterCloseThree]\r\n"
		+ "                ,b.[LabStatus]\r\n"
		+ "                ,a.[DesignFG]\r\n"
		+ "                ,a.[ArticleFG]\r\n"
		+ "                ,b.[BookNo]\r\n"
		+ "                ,b.[Center]\r\n"
		+ "                ,b.[Batch]\r\n"
		+ "                ,b.[LabNo]\r\n"
		+ "                ,b.[RemarkOne]\r\n"
		+ "                ,b.[RemarkTwo]\r\n"
		+ "                ,b.[RemarkThree]\r\n"
		+ "                ,b.[BCAware]\r\n"
		+ "                ,b.[OrderPuang]\r\n"
		+ "                ,b.[RefPrd]\r\n"
		+ "                ,b.[GreigeInDate]\r\n"
		+ "                ,b.[BCDate]\r\n"
		+ "                ,b.[Volumn]\r\n"
		+ "                ,b.[CFdate]\r\n"
		+ "                ,b.[CFType]\r\n"
		+ "                ,b.[Shade]\r\n"
		+ "                ,b.[PrdCreateDate]\r\n"
		+ "                ,b.[GreigeArticle]\r\n"
		+ "                ,b.[GreigeDesign]\r\n"
		+ "                ,b.[GreigeMR]\r\n"
		+ "                ,b.[GreigeKG]\r\n"
		+ "                ,b.[BillSendQuantity]\r\n"
		+ "				from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
		+ "				left join [PCMS].[dbo].[FromSapMainProd] as b on  a.SaleLine = b.SaleLine and \r\n"
		+ "                                                               a.SaleOrder = b.SaleOrder \r\n"
		+ "			   	left join ( \r\n"
		+ "                 SELECT distinct [SaleOrder],[SaleLine] ,1 as [CheckBill] \r\n"
		+ "					FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
		+ "					where DataStatus = 'O'\r\n"
		+ "					group by [SaleOrder],[SaleLine]\r\n"
		+ "            ) as z on A.[SaleOrder] = z.[SaleOrder] AND\r\n"
		+ "                      A.[SaleLine] = z.[SaleLine]  \r\n";
	private String leftJoinTempMainPrdTempB =
			""
			+ " left join (\r\n"
			+ this.selectFromTempMainPrdTemp
			+ " ) as b on a.SaleOrder = b.SaleOrder and \r\n"
			+ "           a.SaleLine = b.SaleLine \r\n";
	private String selectWaitLot = ""
  		    + "   a.SaleOrder \r\n"
  		    + "   , CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
  		    + "		WHEN 0 THEN '' \r\n"
  		    + "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) \r\n"
  		    + "		END AS [SaleLine] \r\n"
  		    + "   , a.Division\r\n"
  		    + "   , a.CustomerShortName\r\n"
  		    + "   , a.SaleCreateDate\r\n"
  		    + "   , a.PurchaseOrder\r\n"
  		    + "   , a.MaterialNo\r\n"
  		    + "   , a.CustomerMaterial\r\n"
  		    + "   , a.Price\r\n"
  		    + "   , a.SaleUnit\r\n"
  		    + "   , a.OrderAmount\r\n"
  		    + "   , a.SaleQuantity\r\n"
  		    + "   , a.RemainQuantity\r\n"
  		    + "   , a.RemainAmount \r\n"
  		    + "   , b.TotalQuantity \r\n"
  		    + "   , b.Grade \r\n"
  		    + "   , b.BillSendWeightQuantity \r\n"
  		    + "   , b.BillSendQuantity  \r\n"
  		    + "   , b.BillSendMRQuantity \r\n"
  		    + "   , b.BillSendYDQuantity \r\n"
  		    + "   , a.CustomerDue\r\n"
  		    + "   , a.DueDate \r\n"
  		    + "   , b.ProductionOrder\r\n"
  		    + "   , b.LotNo \r\n"
  		    + "   , b.LabNo\r\n"
  		    + "   , b.LabStatus\r\n"
  		    + "   , b.CFMPlanLabDate\r\n"
  		    + "   , b.CFMActualLabDate \r\n"
  		    + "   , b.CFMCusAnsLabDate \r\n"
  		    + "   , b.UserStatus \r\n"
  		    + "   , b.TKCFM \r\n"
  		    + "   , b.CFMPlanDate \r\n"
  		    + "   , b.SendCFMCusDate\r\n"
  		    + "   , b.DeliveryDate  \r\n"
  		    + "   , b.CFMDateActual\r\n"
  		    + "   , b.CFMDetailAll \r\n"
  		    + "   , b.CFMNumberAll  \r\n"
  		    + "   , b.CFMRemarkAll \r\n"
  		    + "   , b.RollNoRemarkAll \r\n"
  		    + "   , a.ShipDate \r\n"
  		    + "   , b.RemarkOne \r\n"
  		    + "   , b.RemarkTwo \r\n"
  		    + "   , b.RemarkThree \r\n"
  		    + "   , k.ReplacedRemark \r\n"
  		    + "   , b.StockRemark\r\n"
  		    + "   , b.GRSumKG \r\n"
  		    + "   , b.GRSumYD \r\n"
  		    + "   , b.GRSumMR \r\n"
  		    + "   , b.DyePlan \r\n"
  		    + "   , b.DyeActual \r\n"
  		    + "   , p.PCRemark\r\n"
  		    + "   , InputDD.[DelayedDep] \r\n"
  		    + "   , InputCOD.[CauseOfDelay] \r\n"
  		    + "   , b.[SwitchRemark]\r\n"
  		    + "   , SL.[StockLoad] \r\n"
  		    + "   , b.[PrdCreateDate]\r\n"
  		    + "   , b.LotShipping \r\n"
  		    + "   , b.Volumn \r\n"
  		    + "   , b.VolumnFGAmount  \r\n"
  		    + "   , 'WaitLot' as TypePrd \r\n"
  		    + "   , 'WaitLot' AS TypePrdRemark  \r\n" 
  		    + "   , CAST(null AS VARCHAR(10) ) as [DyeStatus]\r\n"
  		    + "   , a.[CustomerMaterialBase]\r\n";
	private String selectOPV2 = ""
	  		  + "   a.SaleOrder ,\r\n"
	  		  + "	CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
	  		  + "		WHEN 0 THEN '' \r\n"
	  		  + "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) 		END AS [SaleLine] ,\r\n"
	  		  + "   a.Division,\r\n"
	  		  + "   a.CustomerName,--ADD\r\n"
	  		  + "   a.SaleStatus,--ADD\r\n"
	  		  + "   a.DistChannel,--ADD\r\n"
	  		  + "   a.CustomerShortName,	\r\n"
	  		  + "   a.SaleCreateDate,\r\n"
	  		  + "   a.PurchaseOrder,\r\n"
	  		  + "   a.MaterialNo,\r\n"
	  		  + "   a.CustomerMaterial,\r\n"
	  		  + "   a.Price,\r\n"
	  		  + "   a.SaleUnit,\r\n"
	  		  + "   a.OrderAmount,\r\n"
	  		  + "   a.SaleQuantity,\r\n"
	  		  + "   a.RemainQuantity,\r\n"
	  		  + "   a.RemainAmount,\r\n"
	  		  + "   b.TotalQuantity,\r\n"
	  		  + "   a.Grade,\r\n"
	  		  + "   a.BillSendWeightQuantity,\r\n"
	  		  + "   a.BillSendQuantity,\r\n"
	  		  + "   a.BillSendMRQuantity,\r\n"
	  		  + "   a.BillSendYDQuantity,\r\n"
	  		  + "   a.CustomerDue,\r\n"
	  		  + "   a.DueDate,\r\n"
	  		  + "   b.ProductionOrder,\r\n"
	  		  + "   b.LotNo,\r\n"
	  		  + "   b.LabNo,\r\n"
	  		  + "   b.LabStatus,\r\n"
	  		  + "   e.CFMPlanLabDate,\r\n"
	  		  + "   a.CFMActualLabDate,\r\n"
	  		  + "   a.CFMCusAnsLabDate,\r\n"
	  		  + "   a.UserStatusCal as UserStatus,\r\n"
	  		  + "   CASE\r\n"
	  		  + "		WHEN TAPP.SORCFMDate IS NOT NULL THEN TAPP.SORCFMDate\r\n"
	  		  + "		ELSE j.CFMDate \r\n"
	  		  + "		END as TKCFM,\r\n"
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
	  		  + "   a.ShipDate,\r\n"
	  		  + "   b.RemarkOne,\r\n"
	  		  + "   b.RemarkTwo,\r\n"
	  		  + "   b.RemarkThree ,\r\n"
	  		  + "   k.ReplacedRemark ,\r\n"
	  		  + "   l.StockRemark,\r\n"
	  		  + "   a.GRSumKG,\r\n"
	  		  + "   a.GRSumYD,\r\n"
	  		  + "   a.GRSumMR,\r\n"
	  		  + "   a.DyePlan, \r\n"
	  		  + "   a.DyeActual, \r\n"
	  		  + "   P.PCRemark, \r\n"
	  		  + "   InputDD.[DelayedDep], \r\n"
	  		  + "   InputCOD.[CauseOfDelay], \r\n"
	  		  + "   q.[SwitchRemark],\r\n"
	  		  + "   SL.[StockLoad], \r\n"
	  		  + "   b.[PrdCreateDate],\r\n"
	  		  + "   a.LotShipping \r\n" ;
    private String selectSW = ""
  		  + "   a.SaleOrder, \r\n"
  		  + "	CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
  		  + "		WHEN 0 THEN '' \r\n"
  		  + "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) \r\n"
  		  + "		END AS [SaleLine] ,\r\n"
  		  + "   a.Division,\r\n"
  		  + "   a.CustomerShortName,	\r\n"
  		  + "   a.SaleCreateDate,\r\n"
  		  + "   a.PurchaseOrder,\r\n"
  		  + "   a.MaterialNo,\r\n"
  		  + "   a.CustomerMaterial,\r\n"
  		  + "   a.Price,\r\n"
  		  + "   a.SaleUnit,\r\n"
  		  + "   a.OrderAmount,\r\n"
  		  + "   a.SaleQuantity,\r\n"
  		  + "   a.RemainQuantity,\r\n"
  		  + "   a.RemainAmount,\r\n"
  		  + "   b.TotalQuantity,\r\n"
  		  + "   m.Grade,\r\n"
  		  + "   FSMBB.BillSendWeightQuantity,\r\n"
  		  + "  case\r\n"
  		  + "					WHEN a.SaleUnit  = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
  		  + "					WHEN a.SaleUnit  = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
  		  + "					ELSE FSMBB.BillSendMRQuantity\r\n"
  		  + "				end AS BillSendQuantity ,\r\n"
  		  + "   FSMBB.BillSendMRQuantity,\r\n"
  		  + "   FSMBB.BillSendYDQuantity,\r\n"
  		  + "   a.CustomerDue,\r\n"
  		  + "   a.DueDate,\r\n"
  		  + "   b.ProductionOrder,\r\n"
  		  + "   b.LotNo,\r\n"
  		  + "   b.LabNo,\r\n"
  		  + "   b.LabStatus,\r\n"
  		  + "   e.CFMPlanLabDate,\r\n"
  		  + "   g.CFMActualLabDate,\r\n"
  		  + "   g.CFMCusAnsLabDate,\r\n"
  		  + "   UCAL.UserStatusCal as UserStatus,\r\n"
  		  + "   CASE\r\n"
  		  + "		WHEN TAPP.SORCFMDate IS NOT NULL THEN TAPP.SORCFMDate\r\n"
  		  + "		ELSE j.CFMDate \r\n"
  		  + "		END as TKCFM,\r\n"
  		  + "   g.CFMPlanDate AS CFMPlanDate ,  \r\n"
  		  + "   CASE \r\n"
  		  + " 		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> '' THEN SCC.SendCFMCusDate \r\n"
  		  + "     ELSE  g.SendCFMCusDate \r\n"
  		  + "    	END AS SendCFMCusDate ,\r\n"
  		  + "   CASE \r\n"
  		  + "		WHEN h.[ProductionOrder] is not null \r\n"
  		  + "		THEN H.DeliveryDate ELSE b.CFTYPE \r\n"
  		  + "		END AS DeliveryDate , \r\n"
  		  + "   CFMDateActual,\r\n"
  		  + "   g.CFMDetailAll, \r\n"
  		  + "   g.CFMNumberAll,  \r\n"
  		  + "   g.CFMRemarkAll, \r\n"
  		  + "   g.RollNoRemarkAll , \r\n"
  		  + "   a.ShipDate,\r\n"
  		  + "   b.RemarkOne,\r\n"
  		  + "   b.RemarkTwo,\r\n"
  		  + "   b.RemarkThree ,\r\n"
  		  + "   k.ReplacedRemark ,\r\n"
  		  + "   l.StockRemark,\r\n"
  		  + "   m.GRSumKG,\r\n"
  		  + "   m.GRSumYD,\r\n"
  		  + "   m.GRSumMR,\r\n"
  		  + "   g.DyePlan , \r\n"
  		  + "   g.DyeActual, \r\n"
  		  + "   PCRemark, \r\n"
  		  + "   InputDD.[DelayedDep], \r\n"
  		  + "   InputCOD.[CauseOfDelay], \r\n"
  		  + "   q.[SwitchRemark],\r\n"
  		  + "   SL.[StockLoad], \r\n"
  		  + "   b.[PrdCreateDate],\r\n"
  		  + "   g.LotShipping,\r\n"
  		  + "   g.PlanGreigeDate\r\n ";
    private String selectMainV2 = ""
    		  + "   b.SaleOrder, \r\n"
    		  + "   CASE PATINDEX('%[^0 ]%', b.[SaleLine]  + ' ‘') \r\n"
    		  + "		WHEN 0 THEN '' ELSE SUBSTRING(b.[SaleLine] , PATINDEX('%[^0 ]%', b.[SaleLine]  + ' '), LEN(b.[SaleLine] ) ) \r\n"
    		  + "		END AS [SaleLine] ,\r\n"
    		  + "   b.Division,\r\n"
    		  + "   b.CustomerShortName,\r\n"
    		  + "   b.SaleCreateDate,\r\n"
    		  + "   b.PurchaseOrder,\r\n"
    		  + "   b.MaterialNo,\r\n"
    		  + "   b.CustomerMaterial,\r\n"
    		  + "   b.Price,\r\n"
    		  + "   b.SaleUnit,\r\n"
    		  + "   b.OrderAmount,\r\n"
    		  + "   b.SaleQuantity,\r\n"
    		  + "   b.RemainQuantity,\r\n"
    		  + "   b.RemainAmount,\r\n"
    		  + "   b.TotalQuantity,\r\n"
    		  + "   b.Grade,\r\n"
    		  + "   b.BillSendWeightQuantity,\r\n"
    		  + "   b.BillSendQuantity,\r\n"
    		  + "   b.BillSendMRQuantity,\r\n"
    		  + "   b.BillSendYDQuantity,\r\n"
    		  + "   b.CustomerDue,\r\n"
    		  + "   b.DueDate,\r\n"
    		  + "   b.ProductionOrder,\r\n"
    		  + "   b.LotNo,\r\n"
    		  + "   b.LabNo,\r\n"
    		  + "   b.LabStatus,\r\n"
    		  + "   e.CFMPlanLabDate,\r\n"
    		  + "   b.CFMActualLabDate,\r\n"
    		  + "   b.CFMCusAnsLabDate,\r\n"
    		  + "   b.UserStatus,\r\n"
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
    		  + "   b.CFMDateActual,\r\n"
    		  + "   b.CFMDetailAll,\r\n"
    		  + "   b.CFMNumberAll,\r\n"
    		  + "   b.CFMRemarkAll,\r\n"
    		  + "   b.RollNoRemarkAll , \r\n"
    		  + "   b.ShipDate,\r\n"
    		  + "   b.RemarkOne,\r\n"
    		  + "   b.RemarkTwo,\r\n"
    		  + "   b.RemarkThree ,\r\n"
    		  + "   k.ReplacedRemark ,\r\n"
    		  + "   l.StockRemark,\r\n"
    		  + "   b.GRSumKG,\r\n"
    		  + "   b.GRSumYD,\r\n"
    		  + "   b.GRSumMR,\r\n"
    		  + "   b.DyePlan , \r\n"
    		  + "   b.DyeActual, \r\n"
    		  + "   p.PCRemark,\r\n"
    		  + "   InputDD.[DelayedDep],\r\n"
    		  + "   InputCOD.[CauseOfDelay],\r\n"
    		  + "   q.[SwitchRemark],\r\n"
    		  + "   SL.[StockLoad],\r\n"
    		  + "   b.[PrdCreateDate], \r\n"
    		  + "   b.LotShipping\r\n";
    private String selectRPV2 = ""
    		+ "   a.SaleOrder,\r\n"
    		+ "   CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘') \r\n"
    		+ "		WHEN 0 THEN '' \r\n"
    		+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) ) \r\n"
    		+ "		END AS [SaleLine] ,\r\n"
    		+ "   a.Division,\r\n"
    		+ "   a.CustomerName,--ADD\r\n"
    		+ "   a.SaleStatus,--ADD\r\n"
    		+ "   a.DistChannel,--ADD\r\n"
    		+ "   a.CustomerShortName,\r\n"
    		+ "   a.SaleCreateDate,\r\n"
    		+ "   a.PurchaseOrder,\r\n"
    		+ "   a.MaterialNo,\r\n"
    		+ "   a.CustomerMaterial,\r\n"
    		+ "   a.Price,\r\n"
    		+ "   a.SaleUnit,\r\n"
    		+ "   a.OrderAmount,\r\n"
    		+ "   a.SaleQuantity,\r\n"
    		+ "   a.RemainQuantity,\r\n"
    		+ "   a.RemainAmount,\r\n"
    		+ "   b.TotalQuantity,\r\n"
    		+ "   m.Grade,\r\n"
    		+ "   FSMBB.BillSendWeightQuantity, \r\n"
    		+ "   case\r\n"
    		+ "		WHEN a.SaleUnit  = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
    		+ "		WHEN a.SaleUnit  = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
    		+ "		ELSE FSMBB.BillSendMRQuantity\r\n"
    		+ "	  end AS BillSendQuantity, \r\n"
    		+ "   FSMBB.BillSendMRQuantity,\r\n"
    		+ "   FSMBB.BillSendYDQuantity, \r\n"
    		+ "   a.CustomerDue,\r\n"
    		+ "   a.DueDate,\r\n"
    		+ "   b.ProductionOrder,\r\n"
    		+ "   b.LotNo,\r\n"
    		+ "   b.LabNo,\r\n"
    		+ "   b.LabStatus,\r\n"
    		+ "   e.CFMPlanLabDate,\r\n"
    		+ "   g.CFMActualLabDate,\r\n"
    		+ "   g.CFMCusAnsLabDate,\r\n"
    		+ "   UCALRP.UserStatusCalRP as UserStatus,\r\n"
    		+ "   CASE\r\n"
    		+ "		WHEN TAPP.SORCFMDate IS NOT NULL THEN TAPP.SORCFMDate\r\n"
    		+ "		ELSE j.CFMDate \r\n"
    		+ "		END as TKCFM,\r\n"
    		+ "   g.CFMPlanDate AS CFMPlanDate ,  \r\n"
    		+ "   CASE \r\n"
    		+ "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
    		+ "     ELSE  g.SendCFMCusDate \r\n"
    		+ "    	END AS SendCFMCusDate,\r\n"
    		+ "   CASE \r\n"
    		+ "		WHEN h.[ProductionOrder] is not null THEN H.DeliveryDate \r\n"
    		+ "		ELSE b.CFTYPE \r\n"
    		+ "		END AS DeliveryDate , \r\n"
    		+ "   g.CFMDateActual,\r\n"
    		+ "   g.CFMDetailAll, \r\n"
    		+ "   g.CFMNumberAll,  \r\n"
    		+ "   g.CFMRemarkAll, \r\n"
    		+ "   g.RollNoRemarkAll , \r\n"
    		+ "   a.ShipDate,\r\n"
    		+ "   b.RemarkOne,\r\n"
    		+ "   b.RemarkTwo,\r\n"
    		+ "   b.RemarkThree ,\r\n"
    		+ "   k.ReplacedRemark ,\r\n"
    		+ "   l.StockRemark,\r\n"
    		+ "   m.GRSumKG,\r\n"
    		+ "   m.GRSumYD,\r\n"
    		+ "   m.GRSumMR,\r\n"
    		+ "   g.DyePlan , \r\n"
    		+ "   g.DyeActual, \r\n"
    		+ "   PCRemark,\r\n"
    		+ "   InputDD.[DelayedDep],\r\n"
    		+ "   InputCOD.[CauseOfDelay],\r\n"
    		+ "   q.[SwitchRemark],\r\n"
    		+ "   SL.[StockLoad],\r\n"
    		+ "   b.[PrdCreateDate],\r\n"
    		+ "   g.LotShipping, \r\n"
    		+ "   g.PlanGreigeDate\r\n";
    private String selectAll = ""
		  + "   a.SaleOrder,\r\n"
		  + "   a.[SaleLine] ,\r\n"
		  + "   a.Division,\r\n"
		  + "   a.CustomerShortName,	 \r\n"
		  + "   a.SaleCreateDate,\r\n"
		  + "   a.PurchaseOrder,\r\n"
		  + "   a.MaterialNo,\r\n"
		  + "   a.CustomerMaterial,\r\n"
		  + "   a.Price,\r\n"
		  + "   a.SaleUnit,\r\n"
		  + "   a.OrderAmount,\r\n"
		  + "   a.SaleQuantity,\r\n"
		  + "   a.RemainQuantity,\r\n"
		  + "   a.RemainAmount,\r\n"
		  + "   a.TotalQuantity,\r\n"
		  + "   a.Grade,\r\n"
		  + "   a.BillSendWeightQuantity, \r\n"
		  + "   a.BillSendQuantity, \r\n"
		  + "   a.BillSendMRQuantity,\r\n"
		  + "   a.BillSendYDQuantity, \r\n"
		  + "   a.CustomerDue,\r\n"
		  + "   a.DueDate,\r\n"
		  + "   a.ProductionOrder,\r\n"
		  + "   a.LotNo,\r\n"
		  + "   a.LabNo,\r\n"
		  + "   a.LabStatus,\r\n"
		  + "   a.CFMPlanLabDate,\r\n"
		  + "   a.CFMActualLabDate,\r\n"
		  + "   a.CFMCusAnsLabDate,\r\n"
		  + "   a.UserStatus,\r\n"
		  + "   a.TKCFM,\r\n"
		  + "   a.CFMPlanDate ,  \r\n"
		  + "   a.SendCFMCusDate,\r\n"
		  + "   a.DeliveryDate , \r\n"
		  + "   a.CFMDateActual,\r\n"
		  + "   a.CFMDetailAll, \r\n"
		  + "   a.CFMNumberAll,  \r\n"
		  + "   a.CFMRemarkAll, \r\n"
		  + "   a.RollNoRemarkAll , \r\n"
		  + "   a.ShipDate,\r\n"
		  + "   a.RemarkOne,\r\n"
		  + "   a.RemarkTwo,\r\n"
		  + "   a.RemarkThree ,\r\n"
		  + "   a.ReplacedRemark ,\r\n"
		  + "   a.StockRemark,\r\n"
		  + "   a.GRSumKG,\r\n"
		  + "   a.GRSumYD,\r\n"
		  + "   a.GRSumMR,\r\n"
		  + "   a.DyePlan, \r\n"
		  + "   a.DyeActual, \r\n"
		  + "   a.PCRemark,\r\n"
		  + "   a.[DelayedDep],\r\n"
		  + "   a.[CauseOfDelay],\r\n"
		  + "   a.[SwitchRemark],\r\n"
		  + "   a.[StockLoad],\r\n"
		  + "   a.[PrdCreateDate]\r\n"
		  + "   , a.LotShipping\r\n"
		  + "   , a.Volumn  \r\n"
		  + "   , a.VolumnFGAmount \r\n"
		  + "   , a.TypePrd \r\n"
		  + "   , a.TypePrdRemark \r\n"
//		  + "   , a.CustomerType\r\n"
		  + "   , a.[DyeStatus]\r\n"
		  + "   , a.[CustomerMaterialBase]\r\n";
    private String innerJoinWaitLotB = ""
   		 + " INNER JOIN (\r\n"
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
   		 + "	left join (\r\n"
   		 + "        SELECT DISTINCT a.SaleOrder , A.SaleLine ,ISNULL(SumVolOP, 0 ) + ISNULL(SumVolRP, 0 ) as SumVolUsed --,ISNULL(SumVolRP, 0 )\r\n"
   		 + "			           ,SumVolOP,SumVolRP\r\n"
   		 + "		FROM [PCMS].[dbo].[FromSapMainProd]  AS A\r\n"
   		 + "		LEFT JOIN (  \r\n"
   		 + "			SELECT DISTINCT a.[ProductionOrder] \r\n"
   		 + "                           , sum (A.[Volumn]) as SumVolOP \r\n"
   		 + "			FROM [PCMS].[dbo].[FromSapMainProdSale] AS A \r\n"
   		 + "			left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder  \r\n"
   		 + "			WHERE a.[DataStatus] = 'O' and\r\n"
   		 + "                  ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
   		 + "			GROUP BY A.[ProductionOrder]\r\n"
   		 + "        ) AS D ON D.ProductionOrder = A.ProductionOrder\r\n"
   		 + "		LEFT JOIN (   \r\n"
   		 + "			SELECT a.ProductionOrderRP , sum(a.Volume) as SumVolRP  \r\n"
   		 + "			from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
   		 + "			left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrderRP = b.ProductionOrder \r\n"
   		 + "			WHERE a.[DataStatus] = 'O' and\r\n"
   		 + "                  ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ')  \r\n"
   		 + "            group by a.ProductionOrderRP\r\n"
   		 + "		) AS E ON A.ProductionOrder = E.ProductionOrderRP\r\n"
   		 + "	)  as b on a.SaleOrder = b.SaleOrder and\r\n"
   		 + "               a.SaleLine = b.SaleLine\r\n"
   		 + "	LEFT JOIN ( \r\n"
   		 + "		SELECT [SaleOrder] ,[SaleLine]  ,sum( [Volumn] ) as SumVolMain \r\n"
   		 + "		FROM [PCMS].[dbo].[FromSapMainProd]\r\n"
   		 + "		WHERE DataStatus = 'O'\r\n"
   		 + "		group by  [SaleOrder]  ,[SaleLine]\r\n"
   		 + "    ) AS C ON A.SaleOrder = C.SaleOrder AND A.SaleLine = C.SaleLine \r\n"
   		 + "	left join (\r\n"
   		 + "        select DISTINCT a.SaleOrder ,a.SaleLine ,1 AS CountProdRP\r\n"
   		 + "		from [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
   		 + "		inner join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrderRP = b.ProductionOrder\r\n"
   		 + "		where a.DataStatus = 'O' and\r\n"
   		 + "              a.ProductionOrder = 'รอจัด Lot' and\r\n"
   		 + "		      ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
   		 + "		GROUP BY a.SaleOrder ,a.SaleLine \r\n"
   		 + "	) AS D ON A.SaleOrder = D.SaleOrder AND \r\n"
   		 + "             A.SaleLine = D.SaleLine  \r\n"
   		 + "	where "
//   		 + "		  c.SumVolMain > 0 OR D.SaleOrder IS NOT NULL \r\n"
   		 + "          c.SumVolMain > 0 OR ( c.SumVolMain is null AND D.SaleOrder IS NOT NULL )"
   		 + " ) AS b ON a.SaleOrder = b.SaleOrder and\r\n"
   		 + "           a.SaleLine = b.SaleLine \r\n" ;
    private String leftJoinUCAL =  ""
    		+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCAL on UCAL.[DataStatus] = 'O' and \r\n"
    		+ "                                                         b.ProductionOrder = UCAL.ProductionOrder AND \r\n"
    		+ "                                                         (  m.Grade = UCAL.Grade OR  m.Grade IS NULL )  \r\n";
    private String leftJoinUCALRP = "    "
    		+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCALRP on UCALRP.[DataStatus] = 'O' and \r\n"
    		+ "                                                           b.ProductionOrder = UCALRP.ProductionOrder AND \r\n"
    		+ "                                                           ( m.Grade = UCALRP.Grade OR m.Grade IS NULL )    \r\n";
    private String leftJoinSCC = ""
    		+ " left join [PCMS].[dbo].[PlanSendCFMCusDate] as SCC on b.ProductionOrder = SCC.ProductionOrder and SCC.DataStatus = 'O'    \r\n";
	private String leftJoinM =
			" left join #tempSumGR as m on b.ProductionOrder = m.ProductionOrder \r\n";
	   private String leftJoinFSMBBTempSumBill = ""
	    		+ " left join #tempSumBill AS FSMBB ON b.[ProductionOrder] = FSMBB.[ProductionOrder] \r\n"
	    		+ "							    AND FSMBB.Grade = M.Grade \r\n"
	    		+ "							    AND FSMBB.SaleOrder = a.SaleOrder \r\n"
	    		+ "							    AND FSMBB.SaleLine = a.SaleLine\r\n";

	   private String leftJoinTempG =
				  " left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"  ;
	   private String leftJoinBPartOneH =  ""
				  + "           left join #tempPlandeliveryDate as h on h.ProductionOrder = b.ProductionOrder and\r\n"
				  + "                                                   h.SaleOrder = a.SaleOrder and\r\n"
				  + "                                                   h.SaleLine = a.SaleLine\r\n";
		private String leftJoinBSelect =
				""
			+ "            a.[SaleOrder]\r\n"
			+ "                ,a.[Saleline]\r\n"
			+ "                ,b.[TotalQuantity]\r\n"
			+ "                ,b.[Unit]\r\n"
			+ "                ,b.[RemAfterCloseOne]\r\n"
			+ "                ,b.[RemAfterCloseTwo]\r\n"
			+ "                ,b.[RemAfterCloseThree]\r\n"
			+ "                ,b.[LabStatus]\r\n"
			+ "                ,a.[DesignFG]\r\n"
			+ "                ,a.[ArticleFG]\r\n"
			+ "                ,b.[BookNo]\r\n"
			+ "                ,b.[Center]\r\n"
			+ "                ,b.[Batch]\r\n"
			+ "                ,b.[LabNo]\r\n"
			+ "                ,b.[RemarkOne]\r\n"
			+ "                ,b.[RemarkTwo]\r\n"
			+ "                ,b.[RemarkThree]\r\n"
			+ "                ,b.[BCAware]\r\n"
			+ "                ,b.[OrderPuang]\r\n"
			+ "                ,b.[RefPrd]\r\n"
			+ "                ,b.[GreigeInDate]\r\n"
			+ "                ,b.[BCDate]\r\n"
			+ "                ,b.[Volumn]\r\n"
			+ "                ,b.[CFdate]\r\n"
			+ "                ,b.[CFType]\r\n"
			+ "                ,b.[Shade]\r\n"
			+ "                ,g.[LotShipping]\r\n"
			+ "                ,m.[Grade]\r\n"
			+ "                ,b.[PrdCreateDate]\r\n"
			+ "                ,b.[GreigeArticle]\r\n"
			+ "                ,b.[GreigeDesign]\r\n"
			+ "                ,b.[GreigeMR]\r\n"
			+ "                ,b.[GreigeKG]\r\n"
			+ "                ,b.[ProductionOrder]\r\n"
			+ "                ,b.[LotNo]\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN ( s.SumVolRP is not null\r\n"
			+ "                          AND t.SumVolOP is not null ) THEN ( b.Volumn - s.SumVolRP - t.SumVolOP )\r\n"
			+ "                   WHEN ( s.SumVolRP is not null\r\n"
			+ "                          AND t.SumVolOP is null ) THEN ( b.Volumn - s.SumVolRP )\r\n"
			+ "                   WHEN ( s.SumVolRP is null\r\n"
			+ "                          AND t.SumVolOP is not null ) THEN ( b.Volumn - t.SumVolOP )\r\n"
			+ "                   WHEN b.Volumn is not null THEN b.Volumn\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 END AS SumVol\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN ( s.SumVolRP is not null\r\n"
			+ "                          AND t.SumVolOP is not null ) THEN a.Price * ( b.Volumn - s.SumVolRP - t.SumVolOP )\r\n"
			+ "                   WHEN ( s.SumVolRP is not null\r\n"
			+ "                          AND t.SumVolOP is null ) THEN a.Price * ( b.Volumn - s.SumVolRP )\r\n"
			+ "                   WHEN ( s.SumVolRP is null\r\n"
			+ "                          AND t.SumVolOP is not null ) THEN a.Price * ( b.Volumn - t.SumVolOP )\r\n"
			+ "                   WHEN b.Volumn is not null THEN a.Price * b.Volumn\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 END AS SumVolFGAmount\r\n"
			+ "                ,s.SumVolRP\r\n"
			+ "                ,t.SumVolOP\r\n"
			+ "                ,b.Volumn as RealVolumn\r\n"
			+ "                ,g.[DyePlan]\r\n"
			+ "                ,g.[DyeActual]\r\n"
			+ "                ,g.[Dryer]\r\n"
			+ "                ,g.[Finishing]\r\n"
			+ "                ,g.[Inspectation]\r\n"
			+ "                ,g.[Prepare]\r\n"
			+ "                ,g.[Preset]\r\n"
			+ "                ,g.[Relax]\r\n"
			+ "                ,g.[CFMDateActual]\r\n"
			+ "                ,g.[CFMPlanDate]\r\n"
			+ "                ,g.[DyeStatus]\r\n"
			+ "                ,h.DeliveryDate\r\n"
			+ "                ,UCAL.UserStatusCal as UserStatus\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN SCC.SendCFMCusDate IS NOT NULL\r\n"
			+ "                        and SCC.SendCFMCusDate <> '' THEN SCC.SendCFMCusDate\r\n"
			+ "                   ELSE g.SendCFMCusDate\r\n"
			+ "                 END AS SendCFMCusDate\r\n"
			+ "                ,GRSumKG\r\n"
			+ "                ,GRSumYD\r\n"
			+ "                ,GRSumMR\r\n"
			+ "                ,g.CFMDetailAll\r\n"
			+ "                ,g.CFMNumberAll\r\n"
			+ "                ,g.CFMRemarkAll\r\n"
			+ "                ,g.RollNoRemarkAll\r\n"
			+ "                ,g.CFMActualLabDate\r\n"
			+ "                ,g.CFMCusAnsLabDate\r\n"
			+ "                ,FSMBB.BillSendWeightQuantity\r\n"
			+ "                ,case\r\n"
			+ "                   WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity\r\n"
			+ "                   WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity\r\n"
			+ "                   ELSE FSMBB.BillSendMRQuantity\r\n"
			+ "                 end                AS BillSendQuantity\r\n"
			+ "                ,FSMBB.BillSendMRQuantity\r\n"
			+ "                ,FSMBB.BillSendYDQuantity\r\n"
			+ "                ,g.PlanGreigeDate\r\n"
			+ "                ,a.Division\r\n"
			+ "                ,a.CustomerShortName\r\n"
			+ "                ,a.SaleCreateDate\r\n"
			+ "                ,a.PurchaseOrder\r\n"
			+ "                ,a.MaterialNo\r\n"
			+ "                ,a.CustomerMaterial\r\n"
			+ "                ,a.CustomerMaterialBase\r\n"
			+ "                ,a.Price\r\n"
			+ "                ,a.SaleUnit\r\n"
			+ "                ,a.OrderAmount\r\n"
			+ "                ,a.SaleQuantity\r\n"
			+ "                ,a.RemainQuantity\r\n"
			+ "                ,a.RemainAmount\r\n"
			+ "                ,a.CustomerDue\r\n"
			+ "                ,a.DueDate\r\n"
			+ "                ,a.ShipDate\r\n"
//			+ "                ,a.CustomerType\r\n"
			+ "                ,a.[SaleNumber]\r\n"
			+ "                ,a.[SaleFullName]\r\n"
			+ "                ,a.DistChannel\r\n"
			+ "                ,a.Color\r\n"
			+ "                ,a.ColorCustomer\r\n"
			+ "                ,a.CustomerName\r\n"
			+ "                ,a.DeliveryStatus\r\n"
			+ "                ,a.SaleStatus\r\n"
			+ "";
	private String leftJoinBPartOneT =  ""
			  + "			left join ( \r\n"
			  + "                SELECT a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
			  + "                from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
			  + "			     left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "			     WHERE a.[DataStatus] = 'O' and \r\n"
			  + "                      ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') and\r\n"
			  + "                      a.Volumn is not null\r\n"
			  + "			     group by a.ProductionOrder\r\n"
			  + "           ) as t on b.ProductionOrder = t.ProductionOrder\r\n";
	  private String leftJoinBPartOneS =  ""
			  + "           left join ( \r\n"
			  + "                SELECT a.ProductionOrderRP , sum(a.Volume) as SumVolRP  \r\n"
			  + "    			 from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
			  + "				 left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
			  + "				 WHERE a.[DataStatus] = 'O' and \r\n"
			  + "                      ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') and\r\n"
			  + "                      a.Volume is not null\r\n"
			  + "				 group by a.ProductionOrderRP\r\n"
			  + "           ) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n" ;
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
	private String leftJoinE = ""
					+ "  left join ( \r\n"
				  	+ "			SELECT distinct\r\n"
				  	+ "				a.[ProductionOrder] \r\n"
					+ "           , a.[SaleOrder] \r\n"
					+ "           , a.[SaleLine] \r\n"
					+ "           , [PlanDate] AS CFMPlanLabDate \r\n"
					+ "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
					+ "			  inner join (\r\n"
					+ "                 select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
					+ "				    FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
					+ "				    group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]\r\n"
					+ "           ) as b on a.[ProductionOrder] = b.[ProductionOrder] and\r\n"
					+ "                     a.[SaleOrder] = b.[SaleOrder] and\r\n"
					+ "                     a.[SaleLine] = b.[SaleLine] and\r\n"
					+ "                     a.[CreateDate] = b.[MaxCreateDate] \r\n"
					+ " ) as e on e.[ProductionOrder] = b.[ProductionOrder] and \r\n"
					+ "           e.[SaleOrder] = a.[SaleOrder] and\r\n"
					+ "           e.[SaleLine] = a.[SaleLine]\r\n" ;
	private String leftJoinE_B = ""
			+ "  left join ( \r\n"
				  	+ "			SELECT distinct\r\n"
				  	+ "				a.[ProductionOrder] \r\n"
					+ "           , a.[SaleOrder] \r\n"
					+ "           , a.[SaleLine] \r\n"
					+ "           , [PlanDate] AS CFMPlanLabDate \r\n"
					+ "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
					+ "			  inner join (\r\n"
					+ "                 select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
					+ "				    FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
					+ "				    group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]\r\n"
					+ "           ) as b on a.[ProductionOrder] = b.[ProductionOrder] and\r\n"
					+ "                     a.[SaleOrder] = b.[SaleOrder] and\r\n"
					+ "                     a.[SaleLine] = b.[SaleLine] and\r\n"
					+ "                     a.[CreateDate] = b.[MaxCreateDate] \r\n"
					+ " ) as e on e.[ProductionOrder] = b.[ProductionOrder] and \r\n"
					+ "           e.[SaleOrder] = b.[SaleOrder] and\r\n"
					+ "           e.[SaleLine] = b.[SaleLine]\r\n" ;

	private String leftJoinH = ""
 		  	 + " left join #tempPlandeliveryDate as h on h.ProductionOrder = a.ProductionOrder and \r\n"
 		  	 + "                                         h.SaleOrder = a.SaleOrder and\r\n"
 		  	 + "                                         h.SaleLine = a.SaleLine \r\n"  ;
	private String leftJoinBH = ""
		  	 + " left join #tempPlandeliveryDate as h on h.ProductionOrder = b.ProductionOrder and\r\n"
		  	 + "                                         h.SaleOrder = a.SaleOrder and\r\n"
		  	 + "                                         h.SaleLine = a.SaleLine \r\n"  ;
	private String leftJoinJ =""
			+ " left join ( \r\n"
			+ "    SELECT distinct SALELINE,SALEORDER,CFMDATE \r\n"
			+ "	   FROM [PCMS].[dbo].[FromSORCFM]\r\n"
			+ " )AS J  on a.SaleLine = J.SaleLine and\r\n"
			+ "           a.SaleOrder = J.SaleOrder \r\n ";
	private String leftJoinJ_B =""
			+ " left join (\r\n"
			+ "    SELECT distinct SALELINE,SALEORDER,CFMDATE \r\n"
			+ "	   FROM [PCMS].[dbo].[FromSORCFM] \r\n"
			+ " )AS J on b.SaleLine = J.SaleLine and \r\n"
			+ "          b.SaleOrder = J.SaleOrder \r\n ";

	private String leftJoinTAPP =""
			  + " left join (\r\n"
			  + " 	select a.ProductionOrder,b.SORCFMDate,b.SORDueDate \r\n"
			  + "	from [PPMM].[dbo].[SOR_TempProd] as a  \r\n"
			  + "	inner join [PPMM].[dbo].[ApprovedPlanDate] as b on a.POId = b.POId \r\n"
			  + "	WHERE A.[DataStatus] = 'O' and b.[DataStatus] = 'O'\r\n"
			  + " ) AS TAPP on TAPP.ProductionOrder = b.ProductionOrder\r\n ";
	private String leftJoinK =
			"  left join (\r\n"
			+ "     SELECT SALELINE,SALEORDER,ProductionOrder,ReplacedRemark \r\n"
			+ "     FROM [PCMS].[dbo].[InputReplacedRemark] \r\n"
			+ "     WHERE DataStatus = 'O'\r\n"
			+ " ) AS K on K.ProductionOrder = b.ProductionOrder and \r\n"
			+ "           K.SaleOrder = a.SaleOrder and\r\n"
			+ "           K.SaleLine = a.SaleLine \r\n ";
	private String leftJoinK_B = ""
			+ "  left join (\r\n"
			+ "       SELECT SALELINE,SALEORDER,ProductionOrder,ReplacedRemark \r\n"
			+ "       FROM [PCMS].[dbo].[InputReplacedRemark] \r\n"
			+ "       WHERE DataStatus = 'O'\r\n"
			+ " ) AS K on K.ProductionOrder = b.ProductionOrder and \r\n"
			+ "           K.SaleOrder = b.SaleOrder and\r\n"
			+ "           K.SaleLine = b.SaleLine \r\n ";
	private String leftJoinSL = ""
			+ " left join (\r\n"
			+ "    SELECT SALELINE,SALEORDER,ProductionOrder,StockLoad \r\n"
			+ "	   FROM [PCMS].[dbo].InputStockLoad \r\n"
			+ "	   where Datastatus = 'O'\r\n"
			+ " )AS SL on SL.ProductionOrder = b.ProductionOrder and\r\n"
			+ "           SL.SaleOrder = a.SaleOrder and\r\n"
			+ "           SL.SaleLine = a.SaleLine  \r\n"
			+ "\r\n ";
	private String leftJoinSL_B = ""
			+ " left join (\r\n"
			+ "    SELECT SALELINE,SALEORDER,ProductionOrder,StockLoad \r\n"
			+ "	   FROM [PCMS].[dbo].InputStockLoad \r\n"
			+ "	   where Datastatus = 'O'\r\n"
			+ " )AS SL on SL.ProductionOrder = b.ProductionOrder and\r\n"
			+ "           SL.SaleOrder = b.SaleOrder and\r\n"
			+ "           SL.SaleLine = b.SaleLine  \r\n"
			+ "\r\n ";
	private String leftJoinl = ""
			+ " left join (\r\n"
		    + "	    SELECT SALELINE,SALEORDER,ProductionOrder,Grade ,StockRemark\r\n"
			+ "	    FROM [PCMS].[dbo].[InputStockRemark] \r\n"
			+ "	    WHERE DataStatus = 'O'\r\n"
			+ " ) AS l on l.ProductionOrder = b.ProductionOrder and \r\n"
			+ "           l.SaleOrder = a.SaleOrder and\r\n"
			+ "           l.SaleLine = a.SaleLine and\r\n"
			+ "           l.Grade = m.Grade\r\n ";
	private String leftJoinMainl = ""
			+ " left join (\r\n"
            + "      SELECT SALELINE,SALEORDER,ProductionOrder,Grade ,StockRemark\r\n"
			+ "		 FROM [PCMS].[dbo].[InputStockRemark] \r\n"
			+ "	     WHERE DataStatus = 'O'\r\n"
			+ " ) AS l on l.ProductionOrder = b.ProductionOrder and \r\n"
			+ "           l.SaleOrder = a.SaleOrder and\r\n"
			+ "           l.SaleLine = a.SaleLine and\r\n"
			+ "           l.Grade = b.Grade\r\n ";
	private String leftJoinL_B = ""
			+ " left join (\r\n"
		    + "	    SELECT SALELINE,SALEORDER,ProductionOrder,Grade ,StockRemark\r\n"
			+ "	    FROM [PCMS].[dbo].[InputStockRemark] \r\n"
			+ "	    WHERE DataStatus = 'O'\r\n"
			+ " ) AS l on l.ProductionOrder = b.ProductionOrder and \r\n"
			+ "           l.SaleOrder = b.SaleOrder and\r\n"
			+ "           l.SaleLine = b.SaleLine and\r\n"
			+ "           l.Grade = b.Grade\r\n ";
	private String leftJoinP =
			""
					+ " left join (\r\n"
					+ "      SELECT SALELINE,SALEORDER,ProductionOrder,PCRemark\r\n"
					+ "      FROM [PCMS].[dbo].InputPCRemark \r\n"
					+ "		 WHERE DataStatus = 'O'\r\n"
					+ " ) AS P on P.ProductionOrder = b.ProductionOrder and\r\n"
					+ "           P.SaleOrder = a.SaleOrder and\r\n"
					+ "           P.SaleLine = a.SaleLine\r\n";
	private String leftJoinP_B =
			""
					+ " left join (\r\n"
					+ "      SELECT SALELINE,SALEORDER,ProductionOrder,PCRemark\r\n"
					+ "      FROM [PCMS].[dbo].InputPCRemark \r\n"
					+ "		 WHERE DataStatus = 'O'\r\n"
					+ " ) AS P on P.ProductionOrder = b.ProductionOrder and\r\n"
					+ "           P.SaleOrder = b.SaleOrder and\r\n"
					+ "           P.SaleLine = b.SaleLine\r\n";
	private String leftJoinQ =
			""
					+ " left join (\r\n"
					+ "      SELECT ProductionOrder,SwitchRemark\r\n"
					+ "	     FROM [PCMS].[dbo].InputSwitchRemark \r\n"
					+ "	     WHERE DataStatus = 'O'\r\n"
					+ " ) AS q on b.ProductionOrder = q.ProductionOrder \r\n";
	private String leftJoinInputCOD =
			""
					+ " left join (\r\n"
					+ "      SELECT ProductionOrder,[CauseOfDelay]\r\n"
					+ "      FROM [PCMS].[dbo].[InputCauseOfDelay] \r\n"
					+ "      WHERE DataStatus = 'O'\r\n"
					+ " ) AS InputCOD on b.ProductionOrder = InputCOD.ProductionOrder \r\n";
	private String leftJoinInputDD =
			""
					+ " left join (\r\n"
					+ "      SELECT  ProductionOrder,[DelayedDep]\r\n"
					+ "      FROM [PCMS].[dbo].[InputDelayedDep] \r\n"
					+ "      WHERE DataStatus = 'O'\r\n"
					+ " ) AS InputDD on b.ProductionOrder = InputDD.ProductionOrder \r\n";

	private String createTempMainFirst = ""
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
//			+ "   , CustomerType\r\n"
			+ "   , b.[DyeStatus]\r\n"
  		  	+ "   , b.[CustomerMaterialBase]\r\n"
  		    + " INTO #tempMain  \r\n"  ;
	private String createTempMainSecond = ""
			  	+ this.leftJoinE_B
				+ this.leftJoinJ_B
				+ this.leftJoinTAPP
				+ this.leftJoinK_B
				+ this.leftJoinL_B
				+ this.leftJoinP_B
				+ this.leftJoinInputCOD
				+ this.leftJoinInputDD
				+ this.leftJoinQ
				+ this.leftJoinSL_B
				+ " LEFT JOIN ( \r\n"
				+ "   SELECT [SaleOrder] ,[SaleLine]  ,1 as countnRP  \r\n"
				+ "	  FROM [PCMS].[dbo].[ReplacedProdOrder] \r\n"
				+ "   WHERE DataStatus = 'O'\r\n"
				+ "	  GROUP BY  [SaleOrder] ,[SaleLine]\r\n"
				+ " )  as CRP on  CRP.SaleOrder = b.SaleOrder and CRP.SaleLine = b.SaleLine\r\n"
				+ " where \r\n"
				+ "		( \r\n"
				+ "        b.SumVol Is not null or\r\n" //20230911 FIX HERE
				+ "		   ( \r\n"
				+ "           ( \r\n"
				+ "             b.LotNo = 'รอจัด Lot'  or  b.LotNo = 'ขาย stock' or b.LotNo = 'รับจ้างถัก' or \r\n"
				+ "             b.LotNo = 'Lot ขายแล้ว' or b.LotNo = 'พ่วงแล้วรอสวม' or b.LotNo = 'รอสวมเคยมี Lot'  \r\n"
				+ "           )  and \r\n"
				+ "          b.SumVol = 0 and \r\n"
				+ "		     ( countnRP is null )  \r\n"
				+ "        ) or\r\n"
				+ "        RealVolumn = 0 or\r\n"
				+ "        ( b.UserStatus = 'ยกเลิก' or b.UserStatus = 'ตัดเกรดZ') \r\n"
				+ "     )\r\n"
				+ " and NOT EXISTS ( \r\n"
				+ "                 select distinct ProductionOrderSW\r\n"
				+ "                 FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA\r\n"
				+ "				    WHERE DataStatus = 'O' and \r\n"
				+ "                       BAA.ProductionOrderSW = B.ProductionOrder\r\n"
				+ "				   )   \r\n"	 ;
	private String createTempOPFromA = ""
			+ " If(OBJECT_ID('tempdb..#tempPrdOPA') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempPrdOPA\r\n"
			+ "	end ;\r\n"
			+ " SELECT DISTINCT \r\n"
			+ "				a.SaleOrder\r\n"
			+ "                ,a.[SaleLine]\r\n"
			+ "                ,a.DistChannel\r\n"
			+ "                ,a.Color\r\n"
			+ "                ,a.ColorCustomer\r\n"
			+ "                ,a.SaleQuantity\r\n"
			+ "                ,a.RemainQuantity\r\n"
			+ "                ,a.SaleUnit\r\n"
			+ "                ,a.DueDate\r\n"
			+ "                ,a.CustomerShortName\r\n"
			+ "                ,a.[SaleFullName]\r\n"
			+ "                ,a.[SaleNumber]\r\n"
			+ "                ,a.SaleCreateDate\r\n"
			+ "                ,a.MaterialNo\r\n"
			+ "                ,a.DeliveryStatus\r\n"
			+ "                ,a.SaleStatus\r\n"
			+ "                ,b.ProductionOrder\r\n"
			+ "                ,a.CustomerName\r\n"
			+ "                ,a.DesignFG\r\n"
			+ "                ,a.OrderAmount\r\n"
			+ "                ,'SUB'         as TypePrdRemark\r\n"
			+ "                ,a.ArticleFG\r\n"
			+ "                ,a.ShipDate\r\n"
			+ "                ,a.Division\r\n"
			+ "                ,a.PurchaseOrder\r\n"
			+ "                ,a.CustomerMaterial\r\n"
			+ "                ,a.Price\r\n"
			+ "                ,RemainAmount\r\n"
			+ "                ,CustomerDue\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN b.Volumn <> 0 THEN b.Volumn\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 END           AS Volumn\r\n"
			+ "                ,m.[Grade]\r\n"
			+ "                ,[PriceSTD]\r\n"
			+ "                ,GRSumMR\r\n"
			+ "                ,GRSumKG\r\n"
			+ "                ,GRSumYD\r\n"
			+ "                ,UCAL.UserStatusCal\r\n"
			+ "                ,g.CFMActualLabDate\r\n"
			+ "                ,g.CFMCusAnsLabDate\r\n"
			+ "                ,g.CFMPlanDate AS CFMPlanDate\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN SCC.SendCFMCusDate IS NOT NULL\r\n"
			+ "                        and SCC.SendCFMCusDate <> '' THEN SCC.SendCFMCusDate\r\n"
			+ "                   ELSE g.SendCFMCusDate\r\n"
			+ "                 END           AS SendCFMCusDate\r\n"
			+ "                ,g.CFMDateActual\r\n"
			+ "                ,g.CFMDetailAll\r\n"
			+ "                ,g.CFMNumberAll\r\n"
			+ "                ,g.CFMRemarkAll\r\n"
			+ "                ,g.RollNoRemarkAll\r\n"
			+ "                ,g.LotShipping\r\n"
			+ "                ,g.DyePlan\r\n"
			+ "                ,g.DyeActual\r\n"
			+ "                ,FSMBB.BillSendWeightQuantity\r\n"
			+ "                ,case\r\n"
			+ "                   WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity\r\n"
			+ "                   WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity\r\n"
			+ "                   ELSE FSMBB.BillSendMRQuantity\r\n"
			+ "                 end AS BillSendQuantity\r\n"
			+ "                ,FSMBB.BillSendMRQuantity\r\n"
			+ "                ,FSMBB.BillSendYDQuantity\r\n"
//			+ "                ,CustomerType\r\n"
			+ "                ,g.PlanGreigeDate\r\n"
			+ "                ,g.[DyeStatus]\r\n"
			+ "                ,a.[CustomerMaterialBase]\r\n"
			+ "         into #tempPrdOPA\r\n"
			+ "		 	from #tempMainSale as a  \r\n"
			+ "		 	inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and \r\n"
			+ "                                                               a.SaleOrder = b.SaleOrder \r\n"
			+ "         "+this.leftJoinTempG
			+ "         "+this.leftJoinM
			+ "         "+this.leftJoinUCAL
		    + "         "+this.leftJoinFSMBBTempSumBill
			+ "         "+ this.leftJoinSCC
			+ "		 	WHERE b.DataStatus = 'O' \r\n";
	private String createTempOP  = ""
	+ " If(OBJECT_ID('tempdb..#tempPrdOP') Is Not Null)\r\n"
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
	+ "   , 'OrderPuang' as TypePrd \r\n"
	+ "   , a.TypePrdRemark \r\n"
//	+ "   , a.CustomerType\r\n"
	+ "   , a.[DyeStatus]\r\n"
  	+ "   , a.[CustomerMaterialBase]\r\n"
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

	private String createTempOPSWFirst = ""
			+ " If(OBJECT_ID('tempdb..#tempPrdOPSW') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempPrdOPSW\r\n"
			+ "	end ; \r\n"
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
			+ "	  , a.TypePrdRemark \r\n"
//			+ "   , a.CustomerType\r\n"
			+ "   , g.[DyeStatus]\r\n"
			+ "   , a.[CustomerMaterialBase]\r\n"
			+ " INTO #tempPrdOPSW  \r\n"
			+ " FROM (  SELECT DISTINCT  \r\n"
			+ "			    a.SaleOrder\r\n"
			+ "                ,a.[SaleLine]\r\n"
			+ "                ,a.DistChannel\r\n"
			+ "                ,a.Color\r\n"
			+ "                ,a.ColorCustomer\r\n"
			+ "                ,a.SaleQuantity\r\n"
			+ "                ,a.RemainQuantity\r\n"
			+ "                ,a.SaleUnit\r\n"
			+ "                ,a.DueDate\r\n"
			+ "                ,a.CustomerShortName\r\n"
			+ "                ,a.[SaleFullName]\r\n"
			+ "                ,a.[SaleNumber]\r\n"
			+ "                ,a.SaleCreateDate\r\n"
			+ "                ,a.MaterialNo\r\n"
			+ "                ,a.DeliveryStatus\r\n"
			+ "                ,a.SaleStatus\r\n"
			+ "                ,b.ProductionOrder\r\n"
			+ "                ,a.CustomerName\r\n"
			+ "                ,a.DesignFG\r\n"
			+ "                ,a.OrderAmount\r\n"
			+ "                ,'SUB' as TypePrdRemark\r\n"
			+ "                ,a.ArticleFG\r\n"
			+ "                ,a.ShipDate\r\n"
			+ "                ,a.Division\r\n"
			+ "                ,a.PurchaseOrder\r\n"
			+ "                ,a.CustomerMaterial\r\n"
			+ "                ,a.Price\r\n"
			+ "                ,a.RemainAmount\r\n"
			+ "                ,a.CustomerDue\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN b.Volumn <> 0 THEN b.Volumn\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 END   AS Volumn\r\n"
//			+ "                ,CustomerType\r\n"
			+ "                ,a.[CustomerMaterialBase]\r\n"
			+ "		 	from #tempMainSale as a  \r\n"
			+ "		 	inner join ( \r\n"
			+ "            	SELECT \r\n"
			+ " 				CASE \r\n"
			+ "			          WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
			+ "			          ELSE C.ProductionOrder\r\n"
			+ "			          END AS [ProductionOrder],\r\n"
			+ "		           [SaleOrder] ,[SaleLine] ,[Volumn]  ,[DataStatus]\r\n"
			+ "		        FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
			+ "		        LEFT JOIN (\r\n"
			+ "					SELECT [ProductionOrder] ,[ProductionOrderSW] \r\n"
			+ "					       FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
			+ "					       WHERE ProductionOrder <> ProductionOrderSW AND \r\n"
			+ "                              DataStatus = 'O'	\r\n"
			+ "				) AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
			+ "		        LEFT JOIN (\r\n"
			+ "					SELECT  [ProductionOrder] \r\n"
			+ "						 	 ,[ProductionOrderSW] \r\n"
			+ "					       FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
			+ "					       WHERE ProductionOrder <> ProductionOrderSW AND\r\n"
			+ "                              DataStatus = 'O'	\r\n"
			+ "				) AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n"
			+ "				WHERE ( B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL and a.DataStatus = 'O') \r\n"
			+ "       	) as b on a.SaleLine = b.SaleLine and \r\n"
			+ "                   a.SaleOrder = b.SaleOrder \r\n"
			+ "			where b.DataStatus <> 'X' and b.SaleLine <> ''\r\n";
		private String createTempOPSWSecond = ""
				+ "	) as a  \r\n "
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

	private String createTempPrdSWFirst = ""
	+ " If(OBJECT_ID('tempdb..#tempPrdSW') Is Not Null)\r\n"
	+ "	begin\r\n"
	+ "		Drop Table #tempPrdSW\r\n"
	+ "	end ;\r\n "
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
//	+ "   , CustomerType\r\n"
	+ "   , g.[DyeStatus]\r\n"
  	+ "   , a.[CustomerMaterialBase]\r\n"
    + " INTO #tempPrdSW  \r\n"
	+ " FROM (\r\n"
	+ "       SELECT DISTINCT\r\n"
	+ "             a.SaleOrder\r\n"
	+ "                ,a.[SaleLine]\r\n"
	+ "                ,a.DistChannel\r\n"
	+ "                ,a.Color\r\n"
	+ "                ,a.ColorCustomer\r\n"
	+ "                ,a.SaleQuantity\r\n"
	+ "                ,a.RemainQuantity\r\n"
	+ "                ,a.SaleUnit\r\n"
	+ "                ,a.DueDate\r\n"
	+ "                ,a.CustomerShortName\r\n"
	+ "                ,a.[SaleFullName]\r\n"
	+ "                ,a.[SaleNumber]\r\n"
	+ "                ,a.SaleCreateDate\r\n"
	+ "                ,a.MaterialNo\r\n"
	+ "                ,a.DeliveryStatus\r\n"
	+ "                ,a.SaleStatus\r\n"
	+ "                ,b.ProductionOrderSW as ProductionOrder\r\n"
	+ "                ,a.CustomerName\r\n"
	+ "                ,a.DesignFG\r\n"
	+ "                ,a.OrderAmount\r\n"
	+ "                ,a.ArticleFG\r\n"
	+ "                ,a.ShipDate\r\n"
	+ "                ,a.Division\r\n"
	+ "                ,a.PurchaseOrder\r\n"
	+ "                ,a.CustomerMaterial\r\n"
	+ "                ,a.Price\r\n"
	+ "                ,a.RemainAmount\r\n"
	+ "                ,a.CustomerDue\r\n"
	+ "                ,CASE\r\n"
	+ "                   when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
	+ "                   ELSE 'SUB'\r\n"
	+ "                 END                 TypePrdRemark\r\n"
	+ "                ,C.SumVol\r\n"
//	+ "                ,a.CustomerType\r\n"
	+ "                ,a.[CustomerMaterialBase]\r\n"
	+ "		 	from #tempMainSale as a  \r\n"
	+ "		 	inner join [PCMS].[dbo].[SwitchProdOrder]  as b on a.SaleLine = b.SaleLineSW and a.SaleOrder = b.SaleOrderSW  \r\n \r\n"
	+ "		 	LEFT JOIN ( \r\n"
	+ "				SELECT PRDORDERSW ,sum([Volumn]) as SumVol\r\n"
	+ "				FROM (  \r\n"
	+ "                 SELECT \r\n"
	+ "                    a.[ProductionOrder] \r\n"
	+ "					 , CASE \r\n"
	+ "					       WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
	+ "						   ELSE C.ProductionOrder\r\n"
	+ "					   END AS PRDORDERSW\r\n"
	+ "					 , a.[SaleOrder] ,a.[SaleLine] ,a.[Volumn] ,a.[DataStatus]\r\n"
	+ "				    FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
	+ "				    LEFT JOIN (	\r\n"
	+ "                    SELECT  [ProductionOrder] \r\n"
	+ "							  ,[ProductionOrderSW] \r\n"
	+ "					   FROM [PCMS].[dbo].[SwitchProdOrder] AS A\r\n"
	+ "					   WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'\r\n"
	+ "                 ) AS B ON A.[ProductionOrder] = B.ProductionOrder \r\n"
	+ "				  	LEFT JOIN (\r\n"
	+ "                     SELECT  [ProductionOrder] \r\n"
	+ "							   ,[ProductionOrderSW] \r\n"
	+ "					    FROM [PCMS].[dbo].[SwitchProdOrder] AS A	\r\n"
	+ "						WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O'\r\n"
	+ "                 ) AS C ON A.[ProductionOrder] = C.[ProductionOrderSW] \r\n"
	+ "					WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL)\r\n"
	+ "				 ) AS A\r\n"
	+ "				 group by PRDORDERSW\r\n"
	+ "		    ) AS C ON B.ProductionOrderSW = C.PRDORDERSW \r\n"
	+ "		    where b.DataStatus <> 'X' \r\n";

	private String createTempPrdSWSecond = ""
	+ " ) as a  \r\n "
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
    + this.leftJoinFSMBBTempSumBill;



	private String createTempPrdReplacedFirst = ""
	+ " If(OBJECT_ID('tempdb..#tempPrdReplaced') Is Not Null)\r\n"
	+ "	begin\r\n"
	+ "		Drop Table #tempPrdReplaced\r\n"
	+ "	end ; \r\n"
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
//	+ "   , a.CustomerType\r\n"
	+ "   , g.[DyeStatus]\r\n"
  	+ "   , a.[CustomerMaterialBase]\r\n"
	+ " into #tempPrdReplaced\r\n"
	+ " from #tempMainSale as a  \r\n"
	+ " inner join ( \r\n"
	+ "		select SaleOrder , SaleLine,[Volume], [ProductionOrderRP] AS ProductionOrder,DataStatus  "
	+ "		from [PCMS].[dbo].[ReplacedProdOrder] \r\n";
	private String createTempPrdReplacedSecond = ""
	+ " )  as rpo on a.SaleLine = rpo.SaleLine and a.SaleOrder = rpo.SaleOrder and rpo.DataStatus <> 'X'  \r\n"
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
	+ " where ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n";
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
		ArrayList<String> listUserStatus = new ArrayList<>();
		ArrayList<String> listString = new ArrayList<>();
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

			listString.clear();
			for (String element : array) {
				listString.add("'"+element.replaceAll("'","''")+"' ");
			}
			tmpWhere += " and ( a.Division IN ( \r\n" ;
			tmpWhere += String.join(",",  listString  );
			tmpWhere += " ) ) \r\n" ;

			whereSale += tmpWhere;
		}
		if (!saleOrder.equals("")) {
			whereSale +=  " and a.SaleOrder like '" + saleOrder + "%' \r\n";
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
			whereSale +=  "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
		}
		if(!po.equals("")) {
			where += " and ([PurchaseOrder],  like '"+po+"%' \r\n" ;
			whereSale += " and ([PurchaseOrder],  like '"+po+"%' \r\n" ;
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
			for (String element : cusNameList) {
				listString.add("'"+element.replaceAll("'","''")+"' ");
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
			for (String element : cusShortNameList) {
				listString.add("'"+element.replaceAll("'","''")+"' ");
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
			for (String element : divisionList) {
				listString.add("'"+element.replaceAll("'","''")+"' ");
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
			if(saleStatus.equals("O")) {
				where += " and ( SaleStatus like '" + saleStatus + "%' and a.[RemainQuantity] > 0 )  \r\n";
				whereSale += " and ( SaleStatus like '" + saleStatus + "%' and a.[RemainQuantity] > 0 ) \r\n";
			}
			else {
				where += " and ( SaleStatus like '" + saleStatus + "%' or a.[RemainQuantity] = 0 )  \r\n";
				whereSale += " and ( SaleStatus like '" + saleStatus + "%' or a.[RemainQuantity] = 0 ) \r\n";

			}
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
			for (String element : array) {
				listString.add("'"+element.replaceAll("'","''")+"' ");
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
		whereBMainUserStatus = whereProd;
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
					}
				}
				if (i != userStatusList.size() - 1) {
					tmpWhere += " or ";
					whereCaseTryRP += " or ";
					whereCaseTry += " or ";
				}
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
			
			whereBMainUserStatus += " and a.SaleOrder <> '' "+  tmpWhere  ; 
//			whereBMainUserStatus += " b.SaleOrder <> '' "+  tmpWhere ; 
		} 
		whereBMainUserStatus = whereBMainUserStatus.replace("UserStatusCalRP", "UserStatus");
		whereBMainUserStatus = whereBMainUserStatus.replace("UserStatusCal", "UserStatus");
		whereBMainUserStatus = whereBMainUserStatus.replace("UCALRP.", "a.");
		whereBMainUserStatus = whereBMainUserStatus.replace("UCAL.", "a.");
		whereBMainUserStatus = whereBMainUserStatus.replace("b.", "a."); 
		
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
				  +	" from (\r\n"
				  + "      SELECT distinct \r\n"
				  + this.leftJoinBSelect
				  + "			from #tempMainSale as a\r\n"
				  + this.leftJoinTempMainPrdTempB
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
				    + this.createTempMainFirst
					+ fromMainB 
					+ this.createTempMainSecond
//					+ whereCaseTry
					;

		String createTempOPFromA = ""
				+ this.createTempOPFromA
				+ "         "+tmpWhereNoLotUCAL
				+ this.createTempOP;
		String sqlOP = ""
					+ " select \r\n"
					+ this.selectAll
		  		    + " INTO #tempOP  \r\n"
					+ " from #tempPrdOP as a \r\n"
					+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') and\r\n"
					+ "       NOT EXISTS ( select distinct ProductionOrderSW \r\n"
					+ "				       FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA \r\n"
					+ "				       WHERE DataStatus = 'O' and BAA.ProductionOrderSW = A.ProductionOrder\r\n"
					+ "				     ) \r\n "
					+ whereCaseTry ;
	String sqlOPSW = ""
			+ " select \r\n"
			+ this.selectAll
  		    + " INTO #tempOPSW  \r\n"
			+ " from #tempPrdOPSW as a \r\n"
			+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') \r\n"
			+ whereCaseTry ;
	String sqlSW =  ""
		    + " select \r\n"
			+ this.selectAll
  		    + " INTO #tempSW  \r\n"
			+ " from #tempPrdSW as a \r\n"
			+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') \r\n"
			+ whereCaseTry ;

//////				// สวม
	String createTempRP = ""
			+ this.createTempPrdReplacedFirst
			+ this.createTempPrdReplacedSecond
			+ whereCaseTryRP    ;
	String sqlRP = ""
				+ " select \r\n"
				+ this.selectAll
	  		    + " INTO #tempRP  \r\n"
				+ " from #tempPrdReplaced as a \r\n"
				+ " where ( a.UserStatus <> 'ยกเลิก' and a.UserStatus <> 'ตัดเกรดZ') \r\n"
				+ whereCaseTry    ;
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
				+ createTempMainSale
			 	+ this.createTempPlanDeliveryDate
			 	+ this.createTempSumGR
			 	+ this.createTempSumBill
			 	+ createTempRP
			 	+ createTempOPFromA
				+ " If(OBJECT_ID('tempdb..#tempPrdOPA') Is Not Null) begin Drop Table #tempPrdOPA end ;   \r\n"
			 	+ this.createTempOPSWFirst
			 	+ this.createTempOPSWSecond
			 	+ this.createTempPrdSWFirst
			 	+ this.createTempPrdSWSecond
				+ sqlWaitLot
				+ sqlMain
				+ " If(OBJECT_ID('tempdb..#tempSumBill') Is Not Null) begin Drop Table #tempSumBill end ;   \r\n"
				+ " If(OBJECT_ID('tempdb..#tempSumGR') Is Not Null) begin Drop Table #tempSumGR end ;   \r\n"
				+ " If(OBJECT_ID('tempdb..#tempPlandeliveryDate') Is Not Null) begin Drop Table #tempPlandeliveryDate end ;   \r\n"
				+ " If(OBJECT_ID('tempdb..#tempSumGR') Is Not Null) begin Drop Table #tempSumGR end ;   \r\n"
				+ " If(OBJECT_ID('tempdb..#tempMainSale') Is Not Null) begin Drop Table #tempMainSale end ;   "
				+ sqlOP
				+ "  If(OBJECT_ID('tempdb..#tempPrdOP') Is Not Null) begin Drop Table #tempPrdOP end ;  \r\n"
				+ sqlOPSW
				+ "  If(OBJECT_ID('tempdb..#tempPrdOPSW') Is Not Null) begin Drop Table #tempPrdOPSW end ;  \r\n"
				+ sqlSW
				+ "  If(OBJECT_ID('tempdb..#tempPrdSW') Is Not Null) begin Drop Table #tempPrdSW end ;  \r\n"
				+ sqlRP
				+ "  If(OBJECT_ID('tempdb..#tempPrdReplaced') Is Not Null) begin Drop Table #tempPrdReplaced end ;\r\n"

				+ " SELECT a.* FROM #tempWaitLot as a\r\n"
				+ " left join  #tempMain as b on a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
				+ " where b.SaleOrder is null \r\n"
				+ " union ALL  \r\n"
				+ " SELECT * FROM #tempMain as a\r\n"
				+ " where 1 = 1 "+whereBMainUserStatus
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
//	 System.out.println(this.selectFromTempMainPrdTemp);
//		System.out.println("Before SQL: "+new Date());
// 	 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
//		System.out.println("AFTER SQL: "+new Date());
		list = new ArrayList<>();
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
		ArrayList<InputDateDetail> list  = new ArrayList<>();
		ArrayList<InputDateDetail> listCount  = new ArrayList<>();
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
		ArrayList<InputDateDetail> listInput = new ArrayList<>();
//		java.util.Date date ;
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
					int index = 1;
					prepared.setString(index++, bean.getProductionOrder());
					prepared.setString(index++, bean.getSaleOrder());
					prepared.setString(index++, saleLine);
//					if (planDate.equals("undefined") || planDate.equals("")  || !isValidDate(planDate)) {
//						prepared.setNull(4, java.sql.Types.DATE);
//					}
//					else {
//						date = sdf2.parse(planDate);
//						prepared.setDate(4, convertJavaDateToSqlDate(date) );
//					}
					prepared = this.sshUtl.setSqlDate(prepared, planDate, index++);
					prepared.setString(index++, bean.getUserId());
					prepared.setTimestamp(index++, new Timestamp(time));
					prepared.setString(index++, bean.getLotNo());
					prepared.executeUpdate();
					prepared.close();
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
			}catch (SQLException e) {
				System.err.println(e.getMessage());
				beanInput.setIconStatus("E");
				beanInput.setSystemStatus("Something happen, Please contact IT.");
			} 
		}
		listInput.add(beanInput);
		return listInput;
	}


//	public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
//		return new java.sql.Date(date.getTime());
//	}
//	public static boolean isValidDate(String inDate) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//		dateFormat.setLenient(false);
//		try {
//			dateFormat.parse(inDate.trim());
//		} catch (ParseException pe) {
//			return false;
//		}
//		return true;
//	}
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
		ArrayList<PCMSSecondTableDetail> list = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOld = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOldNormal = new ArrayList<>();
//		ArrayList<PCMSSecondTableDetail> poListTmp = new ArrayList<PCMSSecondTableDetail>();
		ArrayList<PCMSSecondTableDetail> listRP  = new ArrayList<>();
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
		if(! errCheck) {  }
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
//			ArrayList<ReplacedProdOrderDetail> listRPOld = rpoModel.getReplacedProdOrderDetailByPrdMain(prdOrder);
			ArrayList<ReplacedProdOrderDetail> listRPOld = rpoModel.getReplacedProdOrderDetailByPrdMainAndSO(prdOrder,saleOrder,saleLine); 
			bean = rpoModel.updateReplacedProdOrder(bean, "X");
			ArrayList<PCMSSecondTableDetail> checkList = null;
			for (String element : newRPSplit) {
				String[] subSplit = element.split("=");
				ReplacedProdOrderDetail beanRP = new ReplacedProdOrderDetail();
				numeric = true;
				prdOrderRP = "";
				if(subSplit.length == 1) {
					prdOrderRP = element.trim();
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
		String where = " where  1 = 1 \r\n";
//		String whereTempUCAL = " where  ";
		if (listRP.size() > 0)  {
//			whereTempUCAL += " ( ";
			String saleOrder = "";
			String saleLine = "";
			int sizeList = listRP.size() ;
			where += " AND ( \r\n";
			for ( int i = 0;i<sizeList;i++) {
				PCMSSecondTableDetail bean = listRP.get(i);
				saleOrder = bean.getSaleOrder();
				saleLine = bean.getSaleLine();
				where = where + " ( a.SaleOrder = '"+saleOrder+"' and a.SaleLine = '"+saleLine+"' ) ";
				if (i != sizeList - 1) {
					where += " or ";
				}
			}
			where += " ) \r\n";
		}

		String sql = ""
//				+ this.declareTempApproved
				+ this.createTempMainSale
				+ where
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
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	private ArrayList<PCMSSecondTableDetail> handlerSwitchProdOrder(PCMSSecondTableDetail bean) {
		SwitchProdOrderModel spoModel = new SwitchProdOrderModel();
		ReplacedProdOrderModel rpoModel = new ReplacedProdOrderModel();

		ArrayList<PCMSSecondTableDetail> list = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListTMP = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOld = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOP = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOPSW = new ArrayList<>();
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
					+ " SELECT \r\n"
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
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
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
				}
			}
		}
		if (divisionList.size() > 0) {
			String text = "";
			for (int i = 0; i < divisionList.size(); i++) {
				text = divisionList.get(i);
				divisionName += text  ;
				if (i != divisionList.size() - 1) {
					divisionName += "|";
				}
			}
		}
		if (cusShortNameList.size() > 0) {
			String text = "";
			for (int i = 0; i < cusShortNameList.size(); i++) {
				text = cusShortNameList.get(i);
				customerShortName += text;
				if (i != cusShortNameList.size() - 1) {
					customerShortName += "|";
				}
			}
		}
		if (userStatusList.size() > 0) {
			String text = "";
			for (int i = 0; i < userStatusList.size(); i++) {
				text = userStatusList.get(i);
				userStatus +=  text ;
				if (i != userStatusList.size() - 1) {
					userStatus += "|";
				}
			}
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
		String where = "    ";
		String orderBy = " Order by  CustomerShortName,  DueDate, [SaleOrder], [SaleLine], [ProductionOrder]";
		if (poList.size() > 0)  {
			where += " and ( \r\n";
			String prodOrder = "";
			for ( int i = 0;i<poList.size();i++) {
				if(prdOrderType.equals(this.C_PRODORDER)) {
					prodOrder = poList.get(i).getProductionOrder();
				}else if(prdOrderType.equals(this.C_PRODORDERRP)) {
					prodOrder = poList.get(i).getProductionOrderRP();
				}
				where = where + " b.ProductionOrder = '"+prodOrder+"' ";
				if (i != poList.size() - 1) {
					where += " or ";
				}
			}
			where += " ) \r\n";
		}

		String fromMainB = ""
				  +	" from ( SELECT distinct \r\n"
				  + this.leftJoinBSelect
				  + "			from #tempMainSale as a\r\n"
				  + this.leftJoinTempMainPrdTempB
//				  + "           left join #tempMainPrdTemp as b on  a.SaleOrder = b.SaleOrder  and a.SaleLine = b.SaleLine  \r\n"
				  + this.leftJoinBPartOneT
				  + this.leftJoinBPartOneS
				  + "          "+this.leftJoinTempG
				  + "          "+this.leftJoinSCC
				  + this.leftJoinBPartOneH
				  + this.leftJoinM
				  + "           "+this.leftJoinUCAL
				  + this.leftJoinFSMBBTempSumBill
				  + " ) as b \r\n";
		String sqlMain = ""
				+ this.createTempMainSale
		 		+ this.createTempPlanDeliveryDate
			 	+ this.createTempSumBill
			 	+ this.createTempSumGR
			    + this.createTempMainFirst
				+ fromMainB
				+ this.createTempMainSecond
				+ where
				+ " SELECT * \r\n"
				+ "	FROM #tempMain\r\n"
				+ orderBy
					;
		List<Map<String, Object>> datas = this.database.queryList(sqlMain);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}
	private ArrayList<PCMSSecondTableDetail> getReplacedCaseByProdOrder(String prdOrderType,ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " WHERE ( \r\n";
		String prodOrder = "";
		for ( int i = 0;i<poList.size();i++) {
			if(prdOrderType.equals(this.C_PRODORDER)) {
				prodOrder = poList.get(i).getProductionOrder();
				 String saleLine = ("000000" + poList.get(i).getSaleLine()).substring(poList.get(i).getSaleLine().length()) ;
				where = where + " ( "+prdOrderType +" = '"+prodOrder+"' and\r\n"
						      + "    [SaleOrder] = '"+poList.get(i).getSaleOrder()+"' and\r\n"
						      + "    [SaleLine] = '"+saleLine+"' \r\n"
				      		  + " ) \r\n" ;
			}
			else if(prdOrderType.equals(this.C_PRODORDERRP)) {
				prodOrder = poList.get(i).getProductionOrderRP();
				where = where + " "+prdOrderType +" = '"+prodOrder+"' ";
			} 
			if (i != poList.size() - 1) {
				where += " or ";
			}
		}
		where += " ) \r\n";
		String createTempRP = ""
				+ this.createTempPrdReplacedFirst
				+ where
				+ this.createTempPrdReplacedSecond
				;
		String sqlRP = ""
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
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " and ( \r\n";
		for ( int i = 0;i<poList.size();i++) {
			String ProductionOrder = poList.get(i).getProductionOrder();
			where = where + " b.ProductionOrder = '"+ProductionOrder+"' ";
			if (i != poList.size() - 1) {
				where += " or ";
			}
		}
		where += " ) \r\n";
		String createTempSWFromA = ""

			+ this.createTempPrdSWFirst
			+ where
			+ this.createTempPrdSWSecond  ;
	String sqlSW = ""
			+ this.createTempMainSale
		 	+ this.createTempPlanDeliveryDate
			+ this.createTempSumBill
		 	+ this.createTempSumGR
		 	+ createTempSWFromA
		    + " select distinct\r\n"
			+ this.selectAll
			+ " from #tempPrdSW as a \r\n"
			+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') \r\n"  ;
		List<Map<String, Object>> datas = this.database.queryList(sqlSW);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}
	public ArrayList<PCMSSecondTableDetail> getOrderPuangListByPrd(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " and  ( \r\n";
		for ( int i = 0;i<poList.size();i++) {
			String ProductionOrder = poList.get(i).getProductionOrder();
			where = where + " b.ProductionOrder = '"+ProductionOrder+"' ";
			if (i != poList.size() - 1) {
				where += " or ";
			}
		}
		where += " ) \r\n";
		String createTempOPFromA = ""
					+ this.createTempOPFromA
					+ "          "+where
					+ this.createTempOP ;
		String sqlOP = ""
//				+ this.declareTempApproved
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
//		System.out.println(sqlOP);
		List<Map<String, Object>> datas = this.database.queryList(sqlOP);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}
	public ArrayList<PCMSSecondTableDetail> getOrderPuangSWListByPrd(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " and ( \r\n";
		for ( int i = 0;i<poList.size();i++) {
			String ProductionOrder = poList.get(i).getProductionOrder();
			where = where + " b.ProductionOrder = '"+ProductionOrder+"' ";
			if (i != poList.size() - 1) {
				where += " or ";
			}
		}
		where += " ) \r\n";

		String sql =  ""
				+ this.createTempMainSale
			 	+ this.createTempPlanDeliveryDate
			 	+ this.createTempSumBill
			 	+ this.createTempSumGR
			 	+ this.createTempOPSWFirst
			 	+ "      	     "+where+" \r\n"
			 	+ this.createTempOPSWSecond
				+ " where ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
				+ " SELECT * \r\n"
				+ "	FROM #tempPrdOPSW \r\n";
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
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
		ArrayList<PCMSSecondTableDetail> listPST = new ArrayList<>();
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
//				prepared.setString(index, planDate);index+=1;
				prepared = this.sshUtl.setSqlDate(prepared, planDate, index);index+=1;
				prepared.setString(index, bean.getUserId());index+=1;
				prepared.setTimestamp(index, new Timestamp(time));index+=1;
				prepared.setString(index, bean.getLotNo());index+=1;
				prepared.executeUpdate();
				prepared.close();
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
				prepared.close();
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("updateLogRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			////this.database.close();
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
				prepared.close();
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("updateLogRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			////this.database.close();
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
				prepared.close();
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("updateLogRemarkWithGrade"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			////this.database.close();
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
					prepared.close();
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("updateLogRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			////this.database.close();
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
				prepared.close();
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("upSertRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			////this.database.close();
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
					prepared.close();
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("updateLogRemarkCaseOne"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}finally {
		////this.database.close();
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
					prepared.close();
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("upSertRemarkCaseTwo"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			////this.database.close();
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
				prepared.close();
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("upSertRemarkCaseWithGrade"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			////this.database.close();
		}
		return bean;
	}

}
