	package dao.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.PCMSMainDao;
import entities.CFMDetail;
import entities.DyeingDetail;
import entities.FinishingDetail;
import entities.InputDateDetail;
import entities.InspectDetail;
import entities.NCDetail;
import entities.PCMSAllDetail;
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
import model.master.FromSapCFMModel;
import model.master.FromSapDyeingModel;
import model.master.FromSapFinishingModel;
import model.master.FromSapInspectModel;
import model.master.FromSapMainProdModel;
import model.master.FromSapNCModel;
import model.master.FromSapPOModel;
import model.master.FromSapPackingModel;
import model.master.FromSapPresetModel;
import model.master.FromSapReceipeModel;
import model.master.FromSapSaleInputModel;
import model.master.FromSapSaleModel;
import model.master.FromSapSendTestQCModel;
import model.master.FromSapWaitTestModel;
import model.master.FromSapWorkInLabModel;
import model.master.SearchSettingModel;
import th.in.totemplate.core.sql.Database;

public class PCMSMainDaoImpl implements PCMSMainDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	private String selectOPSWA =
			    "   a.SaleOrder \r\n"
			  + "	, CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
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
			  + "   , a.SaleUnit\r\n"
			  + "   , b.ProductionOrder\r\n"
			  + "   , b.TotalQuantity \r\n"
			  + "   , UCAL.UserStatusCal as UserStatus\r\n"
			  + "   , b.LabStatus\r\n"
			  + "   , a.DueDate\r\n"
			  + "   , g.GreigeInDate \r\n"
			  + "   , g.[DyePlan]  \r\n"
			  + "   , g.[DyeActual] \r\n"
			  + "   , g.[Dryer]	 \r\n"
			  + "   , g.[Finishing] \r\n"
			  + "   , g.[Inspectation]  \r\n"
			  + "   , g.[Prepare] \r\n"
			  + "   , g.[Preset] \r\n"
			  + "   , g.[Relax] \r\n"
			  + "   , g.[CFMDateActual]\r\n"
			  + "   , g.[CFMPlanDate] \r\n"
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
			  + "   , CASE \r\n"
			  + "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
			  + "    	ELSE  g.SendCFMCusDate \r\n"
			  + "     END AS SendCFMCusDate\r\n"
			  + "   , g.PlanGreigeDate \r\n"
			  + "   , g.CFMDetailAll \r\n"
			  + "   , g.RollNoRemarkAll \r\n"
			  + "   , 'OrderPuang' as TypePrd \r\n"
			  + "   , a.TypePrdRemark \r\n"
			  + "   , a.[PurchaseOrder] \r\n" ;
	private String selectSW = ""
			  + "   a.SaleOrder \r\n"
			  + "	, CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
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
			  + "   , a.SaleUnit\r\n"
			  + "   , b.ProductionOrder\r\n"
			  + "   , b.TotalQuantity \r\n"
			  + "   , UCAL.UserStatusCal as UserStatus\r\n"
			  + "   , b.LabStatus\r\n"
			  + "   , a.DueDate\r\n"
			  + "   , g.GreigeInDate \r\n"
			  + "   , g.[DyePlan]  \r\n"
			  + "   , g.[DyeActual] \r\n"
			  + "   , g.[Dryer]	 \r\n"
			  + "   , g.[Finishing] \r\n"
			  + "   , g.[Inspectation]  \r\n"
			  + "   , g.[Prepare] \r\n"
			  + "   , g.[Preset] \r\n"
			  + "   , g.[Relax] \r\n"
			  + "   , g.[CFMDateActual]\r\n"
			  + "   , g.[CFMPlanDate] \r\n"
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
			  + "   , CASE \r\n"
			  + "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
			  + "    	ELSE  g.SendCFMCusDate \r\n"
			  + "     END AS SendCFMCusDate\r\n"
			  + "   , g.PlanGreigeDate \r\n"
			  + "   , g.CFMDetailAll \r\n"
			  + "   , g.RollNoRemarkAll \r\n"
			  + "   , 'Switch' as TypePrd \r\n"
    		  + "   , a.TypePrdRemark \r\n"
	  		  + "   , a.[PurchaseOrder] \r\n" ;
	private String selectOP = ""
			  + "     a.SaleOrder \r\n"
			  + "   , CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
			  + "		WHEN 0 THEN ''  \r\n"
			  + "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
			  + "	 END AS [SaleLine] \r\n"
			  + "   , CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n"
			  + "    	WHEN 0 THEN ''   \r\n"
			  + "    	ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+a.[SaleFullName]\r\n"
			  + "     END AS [SaleFullName]   \r\n"
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
			  + "   , a.UserStatus\r\n"
			  + "   , b.LabStatus\r\n"
			  + "   , a.DueDate\r\n"
			  + "   , a.GreigeInDate \r\n"
			  + "   , a.[DyePlan]  \r\n"
			  + "   , a.[DyeActual] \r\n"
			  + "   , a.[Dryer]	 \r\n"
			  + "   , a.[Finishing] \r\n"
			  + "   , a.[Inspectation]  \r\n"
			  + "   , a.[Prepare] \r\n"
			  + "   , a.[Preset] \r\n"
			  + "   , a.[Relax] \r\n"
			  + "   , a.[CFMDateActual]\r\n"
			  + "   , a.[CFMPlanDate] \r\n"
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
			  + "   , b.[LotNo] \r\n"
			  + "   , a.ShipDate \r\n"
			  + "   , a.SendCFMCusDate\r\n"
			  + "   , a.CFMDetailAll \r\n"
			  + "   , a.RollNoRemarkAll \r\n"
			  + "   , 'OrderPuang' as TypePrd \r\n"
			  + "   , a.TypePrdRemark \r\n"
			  + "   , a.[PurchaseOrder] \r\n"
			  + "   , a.PlanGreigeDate\r\n"  ;
	private String selectMainV2 = ""
			+ "    b.SaleOrder \r\n"
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
			+ "   , b.SaleUnit\r\n"
			+ "   , b.ProductionOrder\r\n"
			+ "   , b.TotalQuantity \r\n"
			+ "   ,	b.UserStatus\r\n"
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
			+ "   , b.SendCFMCusDate\r\n"
			+ "   , 'Main' as TypePrd \r\n"
			+ "   , 'Main' AS TypePrdRemark \r\n"
  		    + "   , b.[PurchaseOrder] \r\n"
  		    + "   , b.PlanGreigeDate\r\n"
  		    + "   , b.CFMDetailAll \r\n"
  		    + "   , b.RollNoRemarkAll \r\n"  ;
	private String selectRP =""
			  + "     a.SaleOrder \r\n"
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
			  + "   , UCALRP.UserStatusCalRP as UserStatus\r\n"
			  + "   , b.LabStatus\r\n"
			  + "   , a.DueDate\r\n"
			  + "   , g.GreigeInDate \r\n"
			  + "   , g.DyePlan  \r\n"
			  + "   , g.DyeActual \r\n"
			  + "   , g.Dryer \r\n"
			  + "   , g.Finishing \r\n"
			  + "   , g.Inspectation  \r\n"
			  + "   , g.Prepare \r\n"
			  + "   , g.Preset \r\n"
			  + "   , g.Relax \r\n"
			  + "   , g.CFMDateActual\r\n"
			  + "   , g.CFMPlanDate \r\n"
			  + "   , g.LotShipping \r\n"
			  + "   , g.DyeStatus  \r\n"
			  + "   , h.DeliveryDate \r\n"
			  + "   , b.LabNo\r\n"
			  + "   , a.CustomerName\r\n"
			  + "   , a.CustomerShortName\r\n"
			  + "   , a.SaleNumber\r\n"
			  + "   , a.SaleCreateDate   \r\n"
			  + "   , b.PrdCreateDate\r\n"
			  + "   , a.MaterialNo \r\n"
			  + "   , a.DeliveryStatus   \r\n"
			  + "   , a.SaleStatus\r\n"
			  + "   , b.[LotNo] \r\n"
			  + "   , a.ShipDate \r\n"
			  + "   , CASE \r\n"
			  + "		WHEN SCC.SendCFMCusDate IS NOT NULL and SCC.SendCFMCusDate <> ''  THEN SCC.SendCFMCusDate \r\n"
			  + "    	ELSE  g.SendCFMCusDate \r\n"
			  + "    	END AS SendCFMCusDate\r\n"
			  + "   , g.PlanGreigeDate \r\n"
			  + "   , g.CFMDetailAll \r\n"
			  + "   , g.RollNoRemarkAll \r\n"
			  + "   , 'Replaced' as TypePrd \r\n"
			  + "   , 'SUB' as TypePrdRemark \r\n"
			  + "   , a.[PurchaseOrder] \r\n"    ;
	private String selectAll = ""
			  + "  a.SaleOrder \r\n"
			  + ", a.[SaleLine] \r\n"
			  + ", a.[SaleFullName]   \r\n"
			  + ", a.DesignFG\r\n"
			  + ", a.ArticleFG\r\n"
			  + ", a.DistChannel\r\n"
			  + ", a.Color\r\n"
			  + ", a.ColorCustomer\r\n"
			  + ", a.SaleQuantity\r\n"
			  + ", a.RemainQuantity\r\n"
			  + ", a.SaleUnit\r\n"
			  + ", a.ProductionOrder\r\n"
			  + ", a.TotalQuantity \r\n"
			  + ", a.UserStatus\r\n"
			  + ", a.LabStatus\r\n"
			  + ", a.DueDate\r\n"
			  + ", a.GreigeInDate \r\n"
			  + ", a.[DyePlan]  \r\n"
			  + ", a.[DyeActual] \r\n"
			  + ", a.[Dryer]	 \r\n"
			  + ", a.[Finishing] \r\n"
			  + ", a.[Inspectation]  \r\n"
			  + ", a.[Prepare] \r\n"
			  + ", a.[Preset] \r\n"
			  + ", a.[Relax] \r\n"
			  + ", a.[CFMDateActual]\r\n"
			  + ", a.[CFMPlanDate] \r\n"
			  + ", a.[LotShipping] \r\n"
			  + ", a.[DyeStatus]  \r\n"
			  + ", a.DeliveryDate \r\n"
			  + ", a.LabNo\r\n"
			  + ", a.CustomerName\r\n"
			  + ", a.CustomerShortName\r\n"
			  + ", a.SaleNumber\r\n"
			  + ", a.SaleCreateDate\r\n"
			  + ", a.PrdCreateDate\r\n"
			  + ", a.MaterialNo\r\n"
			  + ", a.DeliveryStatus   \r\n"
			  + ", a.SaleStatus\r\n"
			  + ", a.[LotNo] \r\n"
			  + ", a.ShipDate \r\n"
			  + ", a.SendCFMCusDate\r\n"
			  + ", a.TypePrd \r\n"
			  + ", a.TypePrdRemark \r\n"
			  + ", a.[PurchaseOrder]     \r\n"
  		      + ", a.[PlanGreigeDate]\r\n"
  		      + ", a.CFMDetailAll \r\n"
  		      + ", a.RollNoRemarkAll \r\n"  ;
	private String selectWaitLot =
	  		      "       \r\n"
	  		      + "     a.SaleOrder 		\r\n"
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
	  		      + "   , b.TotalQuantity \r\n"
	  		      + "   , b.UserStatus\r\n"
	  		      + "   , b.LabStatus\r\n"
	  		      + "   , a.DueDate\r\n"
	  		      + "   , b.GreigeInDate \r\n"
	  		      + "   , b.DyePlan  \r\n"
	  		      + "   , b.DyeActual \r\n"
	  		      + "   , b.Dryer \r\n"
	  		      + "   , b.Finishing \r\n"
	  		      + "   , b.Inspectation  \r\n"
	  		      + "   , b.Prepare \r\n"
	  		      + "   , b.Preset \r\n"
	  		      + "   , b.Relax \r\n"
	  		      + "   , b.CFMDateActual\r\n"
	  		      + "   , b.CFMPlanDate \r\n"
	  		      + "   , b.LotShipping \r\n"
	  		      + "   , b.DyeStatus  \r\n"
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
	  		      + "   , a.ShipDate  \r\n"
	  		      + "   , b.SendCFMCusDate\r\n"
	  		      + "   , 'WaitLot' as TypePrd \r\n"
	  		      + "   , 'WaitLot' AS TypePrdRemark\r\n"
	  		      + "   , a.[PurchaseOrder] \r\n"
	  		      + "   , a.[PlanGreigeDate]\r\n"
	  		      + "   , b.CFMDetailAll \r\n"
	  		      + "   , b.RollNoRemarkAll \r\n"   ;
	private String selectTwo = ""
		    + "    b.[ProductionOrder]\r\n"
		    + ",b.ColorCustomer\r\n"
		    + ",b.[LotNo]\r\n"
		    + ",b.[Batch]\r\n"
		    + ",b.[LabNo]\r\n"
		    + ",b.[PrdCreateDate]\r\n"
		    + ",b.[DueDate]\r\n"
		    + ",b.[SaleOrder]\r\n"
		    + ",CASE Patindex('%[^0 ]%', b.[SaleLine] + ' ‘')\r\n"
		    + "    WHEN 0 THEN ''\r\n"
		    + "    ELSE Substring(b.[SaleLine], Patindex('%[^0 ]%',\r\n"
		    + "                                b.[SaleLine] + ' '),\r\n"
		    + "                Len(b.[SaleLine]))\r\n"
		    + "    END           AS [SaleLine]\r\n"
		    + ",b.PurchaseOrder\r\n"
		    + ",b.ArticleFG\r\n"
		    + ",b.DesignFG\r\n"
		    + ",b.CustomerName\r\n"
		    + ",b.CustomerShortName\r\n"
		    + ",b.Shade\r\n"
		    + ",b.BookNo\r\n"
		    + ",b.Center\r\n"
		    + ",b.MaterialNo\r\n"
		    + ",b.Volumn\r\n"
		    + ",b.SaleUnit\r\n"
		    + ",b.Unit          as STDUnit\r\n"
		    + ",b.Color\r\n"
		    + ",g.PlanGreigeDate\r\n"
		    + ",b.RefPrd\r\n"
		    + ",b.GreigeInDate\r\n"
		    + ",BCAware\r\n"
		    + ",OrderPuang\r\n"
		    + ",UserStatus\r\n"
		    + ",LabStatus\r\n"
		    + ",b.CFMPlanDate AS CFMPlanDate\r\n"
		    + ",CASE\r\n"
		    + "    WHEN b.DeliveryDate is not null THEN b.DeliveryDate\r\n"
		    + "    ELSE b.CFTYPE\r\n"
		    + "    END           AS DeliveryDate\r\n"
		    + ",b.BCDate\r\n"
		    + ",b.RemarkOne\r\n"
		    + ",b.RemarkTwo\r\n"
		    + ",b.RemarkThree\r\n"
		    + ",b.RemAfterCloseOne\r\n"
		    + ",b.RemAfterCloseTwo\r\n"
		    + ",b.RemAfterCloseThree\r\n"
		    + ",b.GreigeArticle\r\n"
		    + ",b.GreigeDesign\r\n"
		    + ",b.[PurchaseOrder] \r\n";

	   private String leftJoinFSMBBTempSumBill = ""
	    		+ " left join #tempSumBill AS FSMBB ON FSMBB.[ProductionOrder] = b.[ProductionOrder] AND\r\n"
	    		+ "							           FSMBB.Grade = M.Grade AND\r\n"
	    		+ "							           FSMBB.SaleOrder = a.SaleOrder AND\r\n"
	    		+ "							           FSMBB.SaleLine = a.SaleLine\r\n";
	 private String leftJoinSCC = ""
	    		+ " left join [PCMS].[dbo].[PlanSendCFMCusDate] as SCC on SCC.ProductionOrder = b.ProductionOrder and\r\n"
	    		+ "                                                       SCC.DataStatus = 'O'\r\n";
	private String createTempSumGR = ""
			+ " If(OBJECT_ID('tempdb..#tempSumGR') Is Not Null)\r\n"
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
			+ " 	 [ProductionOrder] \r\n"
			+ "    , [SaleOrder] \r\n"
			+ "    , [SaleLine]\r\n"
			+ "    , [Grade]\r\n"
			+ "    , SUM([QuantityKG]) AS [BillSendWeightQuantity] \r\n"
			+ "    , SUM([QuantityYD]) AS [BillSendYDQuantity] \r\n"
			+ "    , SUM([QuantityMR]) AS [BillSendMRQuantity] \r\n"
			+ " into #tempSumBill \r\n"
			+ " FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			+ " where DataStatus = 'O'\r\n"
			+ " GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[Grade] \r\n"  ;

	private String selectFromTempMainPrdTemp = ""
		+ "				SELECT distinct \r\n"
		+ "                   	 a.SaleOrder\r\n"
		+ "                    , a.SaleLine \r\n"
		+ "					   , CASE  \r\n"
		+ "							WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
		+ "							WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
		+ "					   		WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
		+ "							WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
		+ "							ELSE 'รอจัด Lot'  \r\n"
		+ "							END AS [ProductionOrder]  \r\n"
		+ "				 	   , CASE  \r\n"
		+ "							WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
		+ "							WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
		+ "					   		WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
		+ "							WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
		+ "							ELSE 'รอจัด Lot'  \r\n"
		+ "							END AS [LotNo]   \r\n"
		+ "				 	    , b.[TotalQuantity]\r\n"
		+ "                     , b.[Unit]\r\n"
		+ "				 	    , b.[RemAfterCloseOne]\r\n"
		+ "                     , b.[RemAfterCloseTwo]\r\n"
		+ "                     , b.[RemAfterCloseThree]\r\n"
		+ "                     , b.[LabStatus] \r\n"
		+ "				 	    , a.[DesignFG]\r\n"
		+ "                     , a.[ArticleFG]\r\n"
		+ "                     , b.[BookNo]\r\n"
		+ "                     , b.[Center] \r\n"
		+ "				 	    , b.[Batch]\r\n"
		+ "                     , b.[LabNo]\r\n"
		+ "                     , b.[RemarkOne]\r\n"
		+ "                     , b.[RemarkTwo]\r\n"
		+ "                     , b.[RemarkThree]\r\n"
		+ "                     , b.[BCAware]\r\n"
		+ "				 	    , b.[OrderPuang]\r\n"
		+ "                     , b.[RefPrd]\r\n"
		+ "                     , b.[GreigeInDate]\r\n"
		+ "                     , b.[BCDate]\r\n"
		+ "                     , b.[Volumn]\r\n"
		+ "				 	    , b.[CFdate]\r\n"
		+ "                     , b.[CFType]\r\n"
		+ "                     , b.[Shade]\r\n"
		+ "				        , b.[PrdCreateDate]\r\n"
		+ "                     , b.[GreigeArticle]\r\n"
		+ "                     , b.[GreigeDesign]\r\n"
		+ "                     , b.[GreigeMR]\r\n"
		+ "                     , b.[GreigeKG]\r\n"
		+ "				        , b.[BillSendQuantity]\r\n"
		+ "             from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
		+ "             left join [PCMS].[dbo].[FromSapMainProd] as b on a.SaleLine = b.SaleLine and\r\n"
		+ "                                                              a.SaleOrder = b.SaleOrder \r\n"
		+ "			   	left join ( \r\n"
		+ "                 SELECT distinct [SaleOrder],[SaleLine] ,1 as [CheckBill] \r\n"
		+ "					FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
		+ "					where DataStatus = 'O'\r\n"
		+ "				    group by [SaleOrder],[SaleLine]\r\n"
		+ "             ) as z on A.[SaleOrder] = z.[SaleOrder] AND\r\n"
		+ "                       A.[SaleLine] = z.[SaleLine]  \r\n"
		;
	private String leftJoinTempMainPrdTempB =
			""
			+ " left join (\r\n"
			+ this.selectFromTempMainPrdTemp
			+ " ) as b on a.SaleOrder = b.SaleOrder and \r\n"
			+ "           a.SaleLine = b.SaleLine \r\n";
	private String leftJoinTempG =
			" left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = b.ProductionOrder \r\n"  ;
	private String leftJoinM =
			" left join #tempSumGR as m on b.ProductionOrder = m.ProductionOrder \r\n";
	private String leftJoinUCAL = "    "
			+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCAL on UCAL.[DataStatus] = 'O' AND\r\n"
			+ "                                                         b.ProductionOrder = UCAL.ProductionOrder AND\r\n"
			+ "                                                         ( m.Grade = UCAL.Grade OR m.Grade IS NULL )  \r\n";
	private String leftJoinUCALRP = "    "
			+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCALRP on UCALRP.[DataStatus] = 'O' AND  \r\n"
			+ "							                                  b.ProductionOrder = UCALRP.ProductionOrder AND \r\n"
			+ "							                                  ( m.Grade = UCALRP.Grade OR m.Grade IS NULL )    \r\n";
	private String innerJoinWaitLotB = ""
   		 + " INNER JOIN (\r\n"
   		 + "	SELECT DISTINCT \r\n"
   		 + "          a.saleorder\r\n"
   		 + "        , a.saleline\r\n"
   		 + "        , c.SumVolMain\r\n"
   		 + "        , b.SumVolUsed\r\n"
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
   		 + "        , cast(null as decimal) as TotalQuantity \r\n"
   		 + "		, cast(null as varchar) as Grade \r\n"
   		 + "		, cast(null as decimal) as BillSendWeightQuantity \r\n"
   		 + "		, cast(null as decimal) as BillSendQuantity  \r\n"
   		 + "		, cast(null as decimal) as BillSendMRQuantity \r\n"
   		 + "		, cast(null as decimal) as BillSendYDQuantity  \r\n"
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
   		 + "		, cast(null as decimal) as GRSumKG \r\n"
   		 + "		, cast(null as decimal) as GRSumYD \r\n"
   		 + "		, cast(null as decimal) as GRSumMR \r\n"
   		 + "		, cast(null as date) as DyePlan \r\n"
   		 + "		, cast(null as date) as DyeActual   \r\n"
   		 + "		, '' as [SwitchRemark] \r\n"
   		 + "		, cast(null as date) as [PrdCreateDate]\r\n"
   		 + "		, cast(null as decimal) AS Volumn   \r\n"
   		 + "		, cast(null as decimal) AS VolumnFGAmount  	\r\n"
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
   		 + "		, cast(null as varchar) as CFMDetailAll \r\n"
   		 + "		, cast(null as varchar) as RollNoRemarkAll \r\n"
   		 + "	from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
   		 + "	left join ( \r\n"
   		 + "        SELECT DISTINCT \r\n"
   		 + "             A.SaleOrder\r\n"
   		 + "           , A.SaleLine\r\n"
   		 + "           , ISNULL(SumVolOP, 0 ) + ISNULL(SumVolRP, 0 ) as SumVolUsed --,ISNULL(SumVolRP, 0 )\r\n"
   		 + "	       , SumVolOP\r\n"
   		 + "           , SumVolRP\r\n"
   		 + "		FROM[PCMS].[dbo].[FromSapMainProd]  AS A\r\n"
   		 + "		LEFT JOIN (  \r\n"
   		 + "			SELECT DISTINCT \r\n"
   		 + "                   a.[ProductionOrder] \r\n"
   		 + "		         , sum(A.[Volumn]) as SumVolOP \r\n"
   		 + "			FROM [PCMS].[dbo].[FromSapMainProdSale] AS A \r\n"
   		 + "		    left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
   		 + "			WHERE a.[DataStatus] = 'O' and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
   		 + "			GROUP BY A.[ProductionOrder] \r\n"
   		 + "        ) AS D ON D.ProductionOrder = A.ProductionOrder\r\n"
   		 + "		LEFT JOIN (   \r\n"
   		 + "			SELECT a.ProductionOrderRP , sum(a.Volume) as SumVolRP  \r\n"
   		 + "			from [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
   		 + "			left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrderRP = b.ProductionOrder \r\n"
   		 + "			WHERE a.[DataStatus] = 'O' and \r\n"
   		 + "                  ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ')  \r\n"
   		 + "			group by a.ProductionOrderRP\r\n"
   		 + "		) AS E ON A.ProductionOrder = E.ProductionOrderRP	 \r\n"
   		 + "	)  as b on  a.SaleLine = b.SaleLine and\r\n"
   		 + "                a.SaleOrder = b.SaleOrder\r\n"
   		 + "	LEFT JOIN ( \r\n"
   		 + "		SELECT [SaleOrder] ,[SaleLine] ,sum( [Volumn] ) as SumVolMain \r\n"
   		 + "		FROM [PCMS].[dbo].[FromSapMainProd] as a\r\n"
   		 + "		WHERE a.DataStatus = 'O'\r\n"
   		 + "		group by  [SaleOrder]  ,[SaleLine] \r\n"
   		 + "    ) AS C ON A.SaleOrder = C.SaleOrder AND\r\n"
   		 + "              A.SaleLine = C.SaleLine \r\n"
   		 + "	left join (\r\n"
   		 + "        select DISTINCT a.SaleOrder ,a.SaleLine ,1 AS CountProdRP\r\n"
   		 + "		from [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
   		 + "		inner join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrderRP = b.ProductionOrder\r\n"
   		 + "		where a.DataStatus = 'O' and\r\n"
   		 + "              a.ProductionOrder = 'รอจัด Lot' and \r\n"
   		 + "		      ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ')  \r\n"
   		 + "		GROUP BY  a.SaleOrder ,a.SaleLine \r\n"
   		 + "	) AS D ON A.SaleOrder = D.SaleOrder AND\r\n"
   		 + "              A.SaleLine = D.SaleLine  \r\n"
   		 + "	WHERE ( c.SumVolMain > 0 ) OR D.SaleOrder IS NOT NULL\r\n"
   		 + " ) AS B ON A.SaleOrder = B.SaleOrder AND\r\n"
   		 + "           A.SaleLine = B.SaleLine \r\n" ;
	 private String leftJoinBPartOneT =  ""
	  + "			left join ( \r\n"
	  + "              SELECT a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
	  + "              from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
	  + "			   left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
	  + "			   WHERE a.[DataStatus] = 'O' and \r\n"
	  + "                    ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ')\r\n"
	  + "			   group by a.ProductionOrder\r\n"
	  + "           ) as t on b.ProductionOrder = t.ProductionOrder\r\n";
	  private String leftJoinBPartOneS =  ""
	  + "           left join (\r\n"
	  + "              SELECT a.ProductionOrderRP , sum(a.Volume) as SumVolRP  \r\n"
	  + "    		   from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
	  + "			   left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrderRP = b.ProductionOrder \r\n"
	  + "			   WHERE a.[DataStatus] = 'O' and \r\n"
	  + "                   ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
	  + "			   group by a.ProductionOrderRP\r\n"
	  + "           ) as s on b.ProductionOrder = s.ProductionOrderRP  \r\n" ;
	 private String leftJoinBPartOneH =  ""
	  + "           left join #tempPlandeliveryDate as h on h.ProductionOrder = b.ProductionOrder and\r\n"
	  + "                                                   h.SaleOrder = a.SaleOrder and\r\n"
	  + "											        h.SaleLine = a.SaleLine\r\n";

		private String createTempPlanDeliveryDate =
				  "  If(OBJECT_ID('tempdb..#tempPlandeliveryDate') Is Not Null)\r\n"
				  + "	begin\r\n"
				  + "		Drop Table #tempPlandeliveryDate\r\n"
				  + "	end ; \r\n"
				  + " SELECT distinct  a.id,a.[ProductionOrder] ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
				  + " into #tempPlandeliveryDate\r\n"
				  + " FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
				  + " inner join (\r\n"
				  + "     select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max(Id) as maxId\r\n"
				  + "	  FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
				  + "	  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]\r\n"
				  + " ) as b on a.Id = b.maxId  \r\n"  ;
	 private String leftJoinBSelect =  ""
	 		  + "			   b.[BillSendQuantity]\r\n"
	 		  + "                ,a.[SaleOrder]\r\n"
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
	 		  + "                 END                AS SumVol\r\n"
	 		  + "                ,CASE\r\n"
	 		  + "                   WHEN ( s.SumVolRP is not null\r\n"
	 		  + "                          AND t.SumVolOP is not null ) THEN a.Price * ( b.Volumn - s.SumVolRP - t.SumVolOP )\r\n"
	 		  + "                   WHEN ( s.SumVolRP is not null\r\n"
	 		  + "                          AND t.SumVolOP is null ) THEN a.Price * ( b.Volumn - s.SumVolRP )\r\n"
	 		  + "                   WHEN ( s.SumVolRP is null\r\n"
	 		  + "                          AND t.SumVolOP is not null ) THEN a.Price * ( b.Volumn - t.SumVolOP )\r\n"
	 		  + "                   WHEN b.Volumn is not null THEN a.Price * b.Volumn\r\n"
	 		  + "                   ELSE 0\r\n"
	 		  + "                 END                AS SumVolFGAmount\r\n"
	 		  + "                ,s.SumVolRP\r\n"
	 		  + "                ,t.SumVolOP\r\n"
	 		  + "                ,b.Volumn           as RealVolumn\r\n"
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
	 		  + "                 END                AS SendCFMCusDate\r\n"
	 		  + "                ,m.GRSumKG\r\n"
	 		  + "                ,m.GRSumYD\r\n"
	 		  + "                ,m.GRSumMR\r\n"
	 		  + "                ,g.PlanGreigeDate\r\n"
	 		  + "                ,g.CFMDetailAll\r\n"
	 		  + "                ,g.RollNoRemarkAll\r\n"
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
//	 		  + "                ,a.CustomerType\r\n"
	 		  + "                ,a.[SaleNumber]\r\n"
	 		  + "                ,a.[SaleFullName]\r\n"
	 		  + "                ,a.DistChannel\r\n"
	 		  + "                ,a.Color\r\n"
	 		  + "                ,a.ColorCustomer\r\n"
	 		  + "                ,a.CustomerName\r\n"
	 		  + "                ,a.DeliveryStatus\r\n"
	 		  + "                ,a.SaleStatus\r\n"
			  + "\r\n"  ;
	private String leftJoinH = ""
			  + "           left join #tempPlandeliveryDate as h on h.ProductionOrder = b.ProductionOrder and\r\n"
			  + "                                                   h.SaleOrder = a.SaleOrder and \r\n"
			  + "                                                   h.SaleLine = a.SaleLine\r\n"
		  ;
 	private String leftJoinCSW = ""
 			  + " LEFT JOIN (\r\n"
		      + "     SELECT [SaleOrderSW] ,[SaleLineSW] ,1 as countSW \r\n"
			  + "	  FROM [PCMS].[dbo].[SwitchProdOrder] as a\r\n"
			  + "	  left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "	  WHERE a.[DataStatus] = 'O' and \r\n"
			  + "           ( b.UserStatus <> 'ยกเลิก' or b.UserStatus <> 'ตัดเกรดZ')\r\n"
			  + "	  GROUP BY [SaleOrderSW] ,[SaleLineSW]\r\n"
			  + " ) as CSW on  CSW.[SaleOrderSW] = b.SaleOrder and\r\n"
			  + "              CSW.[SaleLineSW] = b.SaleLine\r\n" ;
	private String leftJoinCRP = ""
			 + " LEFT JOIN (\r\n"
			 + "     SELECT a.[SaleOrder] ,a.[SaleLine]  ,1 as countnRP  \r\n"
			 + "	 FROM [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
			 + "	 left join [PCMS].[dbo].[FromSapMainProd] as b on a.[ProductionOrderRP] = b.ProductionOrder  \r\n"
			 + "     WHERE a.[DataStatus] = 'O' and\r\n"
			 + "           ( b.UserStatus <> 'ยกเลิก' or b.UserStatus <> 'ตัดเกรดZ')\r\n"
			 + "	 GROUP BY a.[SaleOrder] ,a.[SaleLine]\r\n"
			 + " )  as CRP on CRP.SaleOrder = b.SaleOrder and\r\n"
			 + "              CRP.SaleLine = b.SaleLine\r\n" ;
	private String leftJoinCOP = ""
			  + " LEFT JOIN ( \r\n"
			  + "     SELECT a.[SaleOrder] ,a.[SaleLine] ,1 as countnOP\r\n"
			  + "	  FROM [PCMS].[dbo].[FromSapMainProdSale] as a\r\n"
			  + "	  left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "	  WHERE a.[DataStatus] = 'O' and\r\n"
			  + "           ( b.UserStatus <> 'ยกเลิก' or b.UserStatus <> 'ตัดเกรดZ')\r\n"
			  + "	  GROUP BY  a.[SaleOrder] ,a.[SaleLine]  \r\n"
			  + " ) as COP on COP.SaleOrder = b.SaleOrder and\r\n"
			  + "             COP.SaleLine = b.SaleLine  \r\n";
 	private String leftJoinR = ""
 			+ " left join (\r\n"
 			+ "     select ProductionOrder , ProductionOrderSW\r\n"
	  		+ "     FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
	  	    + "		WHERE DataStatus = 'O'\r\n"
	  		+ " ) as R on b.ProductionOrder = R.ProductionOrderSW  \r\n";   

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
	public ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList, boolean isCustomer) {
		ArrayList<PCMSTableDetail> list = this.getPCMSSumaryDetail(poList );

		if(isCustomer) {
			for (PCMSTableDetail element : list) {
				element.setCfmDetailAll("");
				element.setRollNoRemarkAll("");
			}
		}
		return list;
	}
	public ArrayList<PCMSTableDetail> getPCMSSumaryDetail(ArrayList<PCMSTableDetail> poList ) {
		ArrayList<PCMSTableDetail> list = null;
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

		if (!saleOrder.equals("")) {
			whereSale +=  " and a.SaleOrder like '" + saleOrder + "%' \r\n";
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
			listString.clear();
			for (String element : array) {
				listString.add("'"+element.replaceAll("'","''")+"' ");
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
			for (String element : cusShortNameList) {
				listString.add("'"+element.replaceAll("'","''")+"' ");
			}
			tmpWhere += " and ( CustomerShortName IN ( \r\n" ;
			tmpWhere += String.join(",",  listString  );
			tmpWhere += " ) ) \r\n" ;

			where += tmpWhere;
			whereSale += tmpWhere;
		}
		if (divisionList.size() > 0) {
			String tmpWhere = "";
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
				where += " and ( SaleStatus like '" + saleStatus + "%' or a.[RemainQuantity] > 0 )  \r\n";
				whereSale += " and ( SaleStatus like '" + saleStatus + "%' or a.[RemainQuantity] > 0 ) \r\n";
			}
			else {
				where += " and ( SaleStatus like '" + saleStatus + "%' or a.[RemainQuantity] = 0 )  \r\n";
				whereSale += " and ( SaleStatus like '" + saleStatus + "%' or a.[RemainQuantity] = 0 ) \r\n";

			}
		}
		if (!dist.equals("")) {
			String tmpWhere = "";
			String[] array = dist.split("\\|");
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

		whereWaitLot = where ;
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
					listUserStatus.add("'"+text.replaceAll("'","''")+"' ");
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
			sizeWaitForTest = listUserStatus.size();
			if(sizeWaitForTest > 0) {
				whereWaitLot += " and ( b.LotNo IN ( \r\n" ;
				whereWaitLot += String.join(",",  listUserStatus  );
				whereWaitLot += " ) ) \r\n" ;
			}
			else {
				whereWaitLot += " and ( b.UserStatus is not null) \r\n" ;
			}
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
				  +	" from ( \r\n"
				  + "	SELECT distinct \r\n"
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
				  + whereBMainUserStatus
				  + " ) as b \r\n";
		String sqlMain = ""
				+ " SELECT DISTINCT \r\n "
				+ this.selectMainV2
	  		    + " INTO #tempMain  \r\n"
				+ fromMainB
				+ this.leftJoinCSW
				+ this.leftJoinCRP
				+ this.leftJoinCOP
//				+ " where (\r\n"
				+ " where ( b.SumVol Is not null\r\n" //20230911 FIX HERE
//				+ "			 b.SumVol >= 0 or\r\n" //20230911 FIX HERE
//				+ "			 b.SumVol <> 0 or\r\n" //20230911 FIX HERE
				+ "			or ( \r\n"
				+ "				(\r\n"
				+ "					 b.LotNo = 'รอจัด Lot'  or  b.LotNo = 'ขาย stock' or b.LotNo = 'รับจ้างถัก' or b.LotNo = 'Lot ขายแล้ว' \r\n"
				+ "					or b.LotNo = 'พ่วงแล้วรอสวม' or b.LotNo = 'รอสวมเคยมี Lot'  \r\n"
				+ "				)  and b.SumVol = 0 \r\n"
				+ "		     	and ( countnRP is null )  \r\n"
				+ "          ) or\r\n"
				+ "     	 RealVolumn = 0 or\r\n"
				+ "     	 ( b.UserStatus = 'ยกเลิก' or b.UserStatus = 'ตัดเกรดZ') \r\n"
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
				+ "	end ; \r\n"
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
    			+ "                ,b.ProductionOrder\r\n"
    			+ "                ,a.CustomerName\r\n"
    			+ "                ,a.DesignFG\r\n"
    			+ "                ,a.OrderAmount\r\n"
    			+ "                ,'SUB'              as TypePrdRemark\r\n"
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
    			+ "                 END                AS Volumn\r\n"
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
    			+ "                ,UCAL.UserStatusCal as UserStatus\r\n"
    			+ "                ,CASE\r\n"
    			+ "                   WHEN SCC.SendCFMCusDate IS NOT NULL\r\n"
    			+ "                        and SCC.SendCFMCusDate <> '' THEN SCC.SendCFMCusDate\r\n"
    			+ "                   ELSE g.SendCFMCusDate\r\n"
    			+ "                 END                AS SendCFMCusDate\r\n"
    			+ "                ,m.GRSumKG\r\n"
    			+ "                ,m.GRSumYD\r\n"
    			+ "                ,m.GRSumMR\r\n"
    			+ "                ,g.CFMDetailAll\r\n"
    			+ "                ,g.CFMNumberAll\r\n"
    			+ "                ,g.CFMRemarkAll\r\n"
    			+ "                ,g.RollNoRemarkAll\r\n"
    			+ "                ,g.CFMActualLabDate\r\n"
    			+ "                ,g.CFMCusAnsLabDate\r\n"
    			+ "                ,g.GreigeInDate\r\n"
    			+ "                ,g.LotShipping\r\n"
    			+ "                ,g.PlanGreigeDate\r\n"
				+ "       into #tempPrdOPA\r\n"
				+ "       from #tempMainSale as a  \r\n"
				+ "       inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
				+ "       "+this.leftJoinTempG
				+ "       "+this.leftJoinSCC
				+ "       "+this.leftJoinM
				+ "       "+this.leftJoinUCAL
				+ "       where b.DataStatus <> 'X' \r\n"
				+ "             "+tmpWhereNoLotUCAL+" \r\n"
				+ " If(OBJECT_ID('tempdb..#tempPrdOP') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "	     Drop Table #tempPrdOP\r\n"
				+ "	end ; \r\n "
				+ " SELECT DISTINCT \r\n"
				+ this.selectOP
				+ " into #tempPrdOP\r\n"
    			+ " FROM #tempPrdOPA as a  \r\n "
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
				+ this.leftJoinH     ;
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
//				//// Order PuangSwitch
//				+ " union ALL  "

		String createTempOPSWFromA = ""
				+ " If(OBJECT_ID('tempdb..#tempPrdOPSW') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOPSW\r\n"
				+ "	end ;\r\n"
				+ " SELECT DISTINCT  \r\n"
				+ this.selectOPSWA
				+ " INTO #tempPrdOPSW  \r\n"
				+ " FROM ( \r\n"
				+ "    SELECT DISTINCT  a.SaleOrder\r\n"
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
				+ "                ,RemainAmount\r\n"
				+ "                ,CustomerDue\r\n"
				+ "                ,CASE\r\n"
				+ "                   WHEN b.Volumn <> 0 THEN b.Volumn\r\n"
				+ "                   ELSE 0\r\n"
				+ "                 END   AS Volumn\r\n"
				+ "                ,a.[PlanGreigeDate]\r\n"
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
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
				+ this.leftJoinTempG
				+ this.leftJoinSCC
				+ this.leftJoinH
				+ this.leftJoinR
				+ this.leftJoinM
				+ this.leftJoinUCAL
				+ where
				+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"   ;
		String sqlOPSW = ""
				+ " select \r\n"
				+ this.selectAll
	  		    + " INTO #tempOPSW  \r\n"
				+ " from #tempPrdOPSW as a \r\n"
				+ " where ( A.UserStatus <> 'ยกเลิก' and A.UserStatus <> 'ตัดเกรดZ') \r\n"
				+ whereCaseTry ;
//////			// Switch
//				+ " union ALL  "
		String createTempSWFromA = ""
				+ " If(OBJECT_ID('tempdb..#tempPrdSW') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdSW\r\n"
				+ "	end ; \r\n"
				+ " SELECT DISTINCT \r\n "
				+ this.selectSW
	  		    + " INTO #tempPrdSW  \r\n"
    			+ " FROM (  \r\n"
    			+ "			SELECT DISTINCT  \r\n"
    			+ "                a.SaleOrder\r\n"
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
    			+ "                ,RemainAmount\r\n"
    			+ "                ,CustomerDue\r\n"
    			+ "                ,CASE\r\n"
    			+ "                   when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
    			+ "                   ELSE 'SUB'\r\n"
    			+ "                 END TypePrdRemark\r\n"
    			+ "                ,C.SumVol\r\n"
    			+ "                ,a.[PlanGreigeDate] \r\n"
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
				+ " and ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"  ;
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
		  		    + " INTO #tempPrdReplaced  \r\n"
					+ " from #tempMainSale as a  \r\n"
		  		    + " inner join ( \r\n"
		  		    + "		select SaleOrder , SaleLine, [ProductionOrderRP] AS ProductionOrder,DataStatus \r\n"
		  		    + "		from [PCMS].[dbo].[ReplacedProdOrder] \r\n"
		  		    + " )  as rpo on a.SaleLine = rpo.SaleLine and \r\n"
		  		    + "              a.SaleOrder = rpo.SaleOrder and  \r\n"
		  		    + "              rpo.DataStatus <> 'X'  \r\n"
					+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on b.ProductionOrder = rpo.ProductionOrder \r\n"
					+ this.leftJoinTempG
					+ this.leftJoinSCC
					+ this.leftJoinH
					+ this.leftJoinR
					+ this.leftJoinM
					+ this.leftJoinUCALRP
					+ " where ( b.UserStatus <> 'ยกเลิก' and b.UserStatus <> 'ตัดเกรดZ') \r\n"
					+ whereCaseTryRP    ;

			String sqlRP = ""
						+ " select \r\n"
						+ this.selectAll
			  		    + " INTO #tempRP  \r\n"
						+ " from #tempPrdReplaced as a \r\n"
//						+ " where ( a.UserStatus <> 'ยกเลิก' and a.UserStatus <> 'ตัดเกรดZ') \r\n"
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
//				+ this.createTempMainPrdFromTempA
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
				+ " SELECT a.* FROM #tempWaitLot as a\r\n"
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
				+ " Order by CustomerShortName, DueDate, [SaleOrder], [SaleLine],TypePrdRemark, [ProductionOrder] ";
//			 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
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
//			+ "	  ,b.CustomerType \r\n"
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
		where += " b.ProductionOrder = '" + prdOrder + "' \r\n";
		String fromMainB = ""
				  +	" from ( \r\n"
				  + "			SELECT distinct \r\n"
				  + this.leftJoinBSelect
				  + "			from #tempMainSale as a\r\n"
				  + "           inner join (\r\n"
				  + "                  "+this.selectFromTempMainPrdTemp
				  + "					"+where+" \r\n"
				  + "			) as b on a.SaleOrder = b.SaleOrder and  a.SaleLine = b.SaleLine \r\n"
				  + this.leftJoinBPartOneT
				  + this.leftJoinBPartOneS
				  + "          "+this.leftJoinTempG
				  + "          "+this.leftJoinSCC
				  + this.leftJoinBPartOneH
				  + this.leftJoinM
				  + "           "+this.leftJoinUCAL
				  + this.leftJoinFSMBBTempSumBill
				  + " ) as b \r\n";
		String sql =  ""
				+ this.createTempMainSale
				+ this.createTempPlanDeliveryDate
			 	+ this.createTempSumBill
			 	+ this.createTempSumGR
				+  " SELECT distinct top 1  \r\n "
				+ this.selectTwo
				+ fromMainB
				+ this.leftJoinTempG
				+ " Order by SaleOrder , 	SaleLine";
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		if (list.size() > 0) {
			FromSapPOModel fspoModel = new FromSapPOModel();
			FromSapFinishingModel fsfModel = new FromSapFinishingModel();
			FromSapPresetModel fspModel = new FromSapPresetModel();
			FromSapDyeingModel fsdModel = new FromSapDyeingModel( );
			FromSapSendTestQCModel fsstQCModel = new FromSapSendTestQCModel( );
			FromSapReceipeModel fsrModel = new FromSapReceipeModel( );
			FromSapNCModel fsNCModel = new FromSapNCModel( );
			FromSapSaleInputModel fssiModel = new FromSapSaleInputModel( );
			FromSapSaleModel fssModel = new FromSapSaleModel( );
			FromSapCFMModel fsCFMModel = new FromSapCFMModel( );
			FromSapWaitTestModel fswtModel = new FromSapWaitTestModel( );
			FromSapInspectModel fsiModel = new FromSapInspectModel( );
			FromSapWorkInLabModel fswilModel = new FromSapWorkInLabModel( );
			FromSapPackingModel fspackingModel = new FromSapPackingModel( );
			String productionOrder = bean.getProductionOrder();
			ArrayList<PODetail> poDetailList = fspoModel.getFromSapPODetailByProductionOrder(productionOrder);
			ArrayList<PresetDetail> presetDetailList = fspModel.getFromSapPresetDetailByProductionOrder(productionOrder);
			ArrayList<DyeingDetail> dyeingDetailList = fsdModel.getFromSapDyeingDetailByProductionOrder(productionOrder);
			ArrayList<SendTestQCDetail> sendTestQCDetailList = fsstQCModel.getFromSapSendTestQCByProductionOrder(productionOrder);
			ArrayList<FinishingDetail> finDetailList = fsfModel.getFromSapFinishingDetailByProductionOrder(productionOrder);
			ArrayList<InspectDetail> insDetailList = fsiModel.getFromSapInspectDetailByProductionOrder(productionOrder);
			ArrayList<PackingDetail> packDetailList = fspackingModel.getFromSapPackingDetailByProductionOrder(productionOrder);
			ArrayList<WorkInLabDetail> workInLabDetailList = fswilModel.getFromSapWorkInLabDetailByProductionOrder(productionOrder);
			ArrayList<WaitTestDetail> waitTestDetailList = fswtModel.getFromSapWaitTestDetailByProductionOrder(productionOrder);
			ArrayList<CFMDetail> cfmDetailList = fsCFMModel.getFromSapCFMDetailByProductionOrder(productionOrder);
			ArrayList<SaleDetail> saleDetailList = fssModel.getFromSapSaleDetailByProductionOrder(productionOrder);
			ArrayList<SaleInputDetail> saleInputDetailList = fssiModel.getFromSapSaleInputDetailByProductionOrder(productionOrder);
			ArrayList<InputDateDetail> submitdatDetailList = getSubmitDateDetail(poList);
			ArrayList<NCDetail> ncDetailList = fsNCModel.getFromSapNCDetailByProductionOrder(productionOrder);
			ArrayList<ReceipeDetail> receipeDetailList = fsrModel.getFromSapReceipeDetailByProductionOrder(productionOrder);
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
	private String forPage = "Summary";
	@Override
	public ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList) {
		SearchSettingModel ssModel = new SearchSettingModel();
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
			list = ssModel.insertSearchSettingDetail(poList, this.forPage);
		}
		else {
			list = ssModel.updateSearchSettingDetail(poList, this.forPage);
		}
		return list;
	}



	@Override
	public ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList) {
		SearchSettingModel ssModel = new SearchSettingModel();
		// TODO Auto-generated method stub
		String userId = poList.get(0).getUserId();
		ArrayList<PCMSTableDetail> bean = ssModel.getSearchSettingDetail(userId,this.forPage);
		return bean;
	}
}
