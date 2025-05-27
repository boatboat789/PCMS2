	package th.co.wacoal.atech.pcms2.dao.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.PCMSMainDao;
import th.co.wacoal.atech.pcms2.entities.CFMDetail;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.NCDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.PODetail;
import th.co.wacoal.atech.pcms2.entities.PackingDetail;
import th.co.wacoal.atech.pcms2.entities.SaleDetail;
import th.co.wacoal.atech.pcms2.entities.LBMS.ImportDetail;
import th.co.wacoal.atech.pcms2.entities.PPMM.InspectOrdersDetail;
import th.co.wacoal.atech.pcms2.entities.PPMM.ShopFloorControlDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.model.PCMSSearchModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapCFMModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainProdModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapPackingModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapSaleModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapSubmitDateModel;
import th.co.wacoal.atech.pcms2.model.master.SearchSettingModel;
import th.co.wacoal.atech.pcms2.model.master.InspectSystem.InspectNcModel;
import th.co.wacoal.atech.pcms2.model.master.InspectSystem.InspectOrdersModel;
import th.co.wacoal.atech.pcms2.model.master.LBMS.ImportDetailModel;
import th.co.wacoal.atech.pcms2.model.master.PPMM.RollFromSapModel;
import th.co.wacoal.atech.pcms2.model.master.PPMM.ShopFloorControlModel;
import th.co.wacoal.atech.pcms2.service.PCMSSearchService;
import th.in.totemplate.core.sql.Database;
@Repository // Spring annotation to mark this as a DAO component
public class PCMSMainDaoImpl implements PCMSMainDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private PCMSSearchService pss = new PCMSSearchService();
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
			+ "	  , CASE \r\n"
			+ "			WHEN PATINDEX('%[^0 ]%', b.[SaleLine]  + ' ‘') = 0 THEN ''  \r\n"
			+ "			ELSE SUBSTRING(b.[SaleLine] , PATINDEX('%[^0 ]%', b.[SaleLine]  + ' '), LEN(b.[SaleLine] ) )\r\n"
			+ "			END AS [SaleLine] \r\n"
			+ "   , CASE  \r\n"
			+ "    		 WHEN PATINDEX('%[^0 ]%', b.[SaleNumber]  + ' ‘') = 0 THEN ''   \r\n"
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

 
	 private String leftJoinBSelect =  ""
	 		  + "			      a.[BillSendQuantity]\r\n"
	 		  + "                ,a.[SaleOrder]\r\n"
	 		  + "                ,a.[Saleline]\r\n"
	 		  + "                ,a.[TotalQuantity]\r\n"
	 		  + "                ,a.[Unit]\r\n"
	 		  + "                ,a.[RemAfterCloseOne]\r\n"
	 		  + "                ,a.[RemAfterCloseTwo]\r\n"
	 		  + "                ,a.[RemAfterCloseThree]\r\n"
	 		  + "                ,a.[LabStatus]\r\n"
	 		  + "                ,a.[DesignFG]\r\n"
	 		  + "                ,a.[ArticleFG]\r\n"
	 		  + "                ,a.[BookNo]\r\n"
	 		  + "                ,a.[Center]\r\n"
	 		  + "                ,a.[Batch]\r\n"
	 		  + "                ,a.[LabNo]\r\n"
	 		  + "                ,a.[RemarkOne]\r\n"
	 		  + "                ,a.[RemarkTwo]\r\n"
	 		  + "                ,a.[RemarkThree]\r\n"
	 		  + "                ,a.[BCAware]\r\n"
	 		  + "                ,a.[OrderPuang]\r\n"
	 		  + "                ,a.[RefPrd]\r\n"
	 		  + "                ,a.[GreigeInDate]\r\n"
	 		  + "                ,a.[BCDate]\r\n"
	 		  + "                ,a.[Volumn]\r\n"
	 		  + "                ,a.[CFdate]\r\n"
	 		  + "                ,a.[CFType]\r\n"
	 		  + "                ,a.[Shade]\r\n"
	 		  + "                ,g.[LotShipping]\r\n"
	 		  + "                ,m.[Grade]\r\n"
	 		  + "                ,a.[PrdCreateDate]\r\n"
	 		  + "                ,a.[GreigeArticle]\r\n"
	 		  + "                ,a.[GreigeDesign]\r\n"
	 		  + "                ,a.[GreigeMR]\r\n"
	 		  + "                ,a.[GreigeKG]\r\n"
	 		  + "                ,a.[ProductionOrder]\r\n"
	 		  + "                ,a.[LotNo]\r\n"
	 		  + "                ,CASE\r\n"
	 		  + "                   WHEN ( s.SumVolRP is not null\r\n"
	 		  + "                          AND t.SumVolOP is not null ) THEN ( a.Volumn - s.SumVolRP - t.SumVolOP )\r\n"
	 		  + "                   WHEN ( s.SumVolRP is not null\r\n"
	 		  + "                          AND t.SumVolOP is null ) THEN ( a.Volumn - s.SumVolRP )\r\n"
	 		  + "                   WHEN ( s.SumVolRP is null\r\n"
	 		  + "                          AND t.SumVolOP is not null ) THEN ( a.Volumn - t.SumVolOP )\r\n"
	 		  + "                   WHEN a.Volumn is not null THEN a.Volumn\r\n"
	 		  + "                   ELSE 0\r\n"
	 		  + "                 END                AS SumVol\r\n"
	 		  + "                ,CASE\r\n"
	 		  + "                   WHEN ( s.SumVolRP is not null\r\n"
	 		  + "                          AND t.SumVolOP is not null ) THEN a.Price * ( a.Volumn - s.SumVolRP - t.SumVolOP )\r\n"
	 		  + "                   WHEN ( s.SumVolRP is not null\r\n"
	 		  + "                          AND t.SumVolOP is null ) THEN a.Price * ( a.Volumn - s.SumVolRP )\r\n"
	 		  + "                   WHEN ( s.SumVolRP is null\r\n"
	 		  + "                          AND t.SumVolOP is not null ) THEN a.Price * ( a.Volumn - t.SumVolOP )\r\n"
	 		  + "                   WHEN a.Volumn is not null THEN a.Price * a.Volumn\r\n"
	 		  + "                   ELSE 0\r\n"
	 		  + "                 END                AS SumVolFGAmount\r\n"
	 		  + "                ,s.SumVolRP\r\n"
	 		  + "                ,t.SumVolOP\r\n"
	 		  + "                ,a.Volumn           as RealVolumn\r\n"
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
	 		  + "                ,a.[SaleNumber]\r\n"
	 		  + "                ,a.[SaleFullName]\r\n"
	 		  + "                ,a.DistChannel\r\n"
	 		  + "                ,a.Color\r\n"
	 		  + "                ,a.ColorCustomer\r\n"
	 		  + "                ,a.CustomerName\r\n"
	 		  + "                ,a.DeliveryStatus\r\n"
	 		  + "                ,a.SaleStatus\r\n"
			  + "\r\n"  ; 
 	private String leftJoinCSW = ""
			+ " LEFT JOIN (\r\n"
			+ "   SELECT [SaleOrderSW] ,[SaleLineSW] ,1 as countSW \r\n"
			+ "	  FROM [PCMS].[dbo].[SwitchProdOrder] as a\r\n"
			  + "	  left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n" 
			+ "	  WHERE a.[DataStatus] = 'O' and \r\n"
			+ "           ( b.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' ) )\r\n"
			+ "	  GROUP BY [SaleOrderSW] ,[SaleLineSW]\r\n"
			+ " ) as CSW on CSW.[SaleOrderSW] = b.SaleOrder \r\n"
			+ "			and CSW.[SaleLineSW] = b.SaleLine\r\n" ;
	private String leftJoinCRP = ""
			 + " LEFT JOIN (\r\n"
			 + "     SELECT distinct a.[SaleOrder] ,a.[SaleLine]  ,1 as countnRP  \r\n"
			 + "	 FROM [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
			 + "	 left join [PCMS].[dbo].[FromSapMainProd] as b on a.[ProductionOrderRP] = b.ProductionOrder  \r\n" 
			 + "     WHERE a.[DataStatus] = 'O' and\r\n"
			 + "           ( b.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' ) )\r\n"
			 + "	 GROUP BY a.[SaleOrder] ,a.[SaleLine]\r\n"
			 + " ) as CRP on CRP.SaleOrder = b.SaleOrder and\r\n"
			 + "              CRP.SaleLine = b.SaleLine\r\n" ;
	private String leftJoinCOP = ""
			  + " LEFT JOIN ( \r\n"
			  + "     SELECT a.[SaleOrder] ,a.[SaleLine] ,1 as countnOP\r\n"
			  + "	  FROM [PCMS].[dbo].[FromSapMainProdSale] as a\r\n" 
			  + "	  left join [PCMS].[dbo].[FromSapMainProd] as b on  a.ProductionOrder = b.ProductionOrder  \r\n"
			  + "	  WHERE a.[DataStatus] = 'O' and\r\n"
			  + "           ( b.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' ) )\r\n"
			  + "	  GROUP BY  a.[SaleOrder] ,a.[SaleLine]  \r\n"
			  + " ) as COP on COP.SaleOrder = b.SaleOrder and\r\n"
			  + "             COP.SaleLine = b.SaleLine  \r\n";
 	private String leftJoinR = ""
 			+ " left join (\r\n"
 			+ "     select ProductionOrder , ProductionOrderSW\r\n"
	  		+ "     FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
	  	    + "		WHERE DataStatus = 'O'\r\n"
	  		+ " ) as R on b.ProductionOrder = R.ProductionOrderSW  \r\n";   
 

    @Autowired
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
		
		PCMSSearchModel psModel = new PCMSSearchModel();
		ArrayList<PCMSTableDetail> list = null;
		PCMSTableDetail bean = poList.get(0); 
		Map<String, String> results = pss.buildWhereClauses(bean);
		String whereCaseTry = results.get("whereCaseTry");
		String whereCaseTryRP = results.get("whereCaseTryRP");
		String tmpWhereNoLotUCAL = results.get("tmpWhereNoLotUCAL");
		String where = results.get("where");
		String whereBMainUserStatus = results.get("whereBMainUserStatus");
		String whereSale = results.get("whereSale");
		String whereWaitLot = results.get("whereWaitLot");
		String createCusListSearch = ""
				+ psModel.handlerTempTableCustomerSearchList(bean.getCustomerNameList(), bean.getCustomerShortNameList());
			String createTempMainSale = ""
				+ createCusListSearch
				+ this.pss.createTempMainSaleWithJoinCustomer 
				+ whereSale; 
		String sqlWaitLot =
				  " SELECT DISTINCT  \r\n"
				+ this.selectWaitLot
	  		    + " INTO #tempWaitLot  \r\n"
				+ " FROM #tempMainSale as a \r\n "
				+ this.pss.innerJoinWaitLotB
				+ this.pss.leftJoinB_H
				+ whereWaitLot
				+ " and ( SumVol = 'B' OR countProdRP > 0 ) \r\n"; 
		String fromMainB = ""
				  +	" from ( \r\n"
				  + "	SELECT distinct \r\n"
				  + this.leftJoinBSelect  
				  + this.pss.fromMainSale_A   
				  + this.pss.leftJoinBPartOneT_A
				  + this.pss.leftJoinBPartOneS_A 
				  + this.pss.leftJoinBPartOneH_A
				  + this.pss.leftJoinTempG_A
					+ this.pss.leftJoinSCC_A
				  + this.pss.leftJoinM_A
				  + this.pss.leftJoinUCAL_A 
				  + " ) as b \r\n";
		String sqlMain = ""
				+ " SELECT DISTINCT \r\n "
				+ this.selectMainV2
	  		    + " INTO #tempMain  \r\n"
				+ fromMainB 
				+ this.leftJoinCSW
				+ this.leftJoinCRP
				+ this.leftJoinCOP 
				+ " where ( b.SumVol Is not null\r\n" //20230911 FIX HERE
//				+ "			 b.SumVol >= 0 or\r\n" //20230911 FIX HERE
//				+ "			 b.SumVol <> 0 or\r\n" //20230911 FIX HERE
				+ "			or ( \r\n"
				+ "				(\r\n"
				+ "					 b.LotNo in ( 'รอจัด Lot','ขาย stock','รับจ้างถัก','Lot ขายแล้ว','พ่วงแล้วรอสวม','รอสวมเคยมี Lot' ) \r\n"
				+ "				)  and b.SumVol = 0 \r\n"
				+ "		     	and ( countnRP is null )  \r\n"
				+ "          ) or\r\n"
				+ "     	 RealVolumn = 0 or\r\n"
				+ "     	 ( b.UserStatus in ( 'ยกเลิก' ,'ตัดเกรดZ' ) ) \r\n"
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
				+ "       inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleOrder = b.SaleOrder and "
				+ "                                                             a.SaleLine = b.SaleLine    \r\n"
				+ "       "+this.pss.leftJoinTempG
				+ "       "+this.pss.leftJoinSCC
				+ "       "+this.pss.leftJoinM
				+ "       "+this.pss.leftJoinUCAL
				+ "       where b.DataStatus = 'O' \r\n"
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
				+ this.pss.leftJoinB_H     ;
		String sqlOP = ""
					+ " select \r\n"
					+ this.selectAll
		  		    + " INTO #tempOP  \r\n"
					+ " from #tempPrdOP as a \r\n"
					+ " where ( a.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' ) ) \r\n"
					+ " and NOT EXISTS ( select distinct ProductionOrderSW \r\n"
					+ "				   FROM [PCMS].[dbo].[SwitchProdOrder] AS BAA \r\n"
					+ "				   WHERE DataStatus = 'O' and BAA.ProductionOrderSW = A.ProductionOrder\r\n"
					+ "				 ) \r\n "
					+ whereCaseTry ;
//				//// Order PuangSwitch 
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
				+ "				WHERE (B.ProductionOrder IS NOT NULL OR  C.ProductionOrder IS NOT NULL) "
				+ "					AND A.[DataStatus] = 'O' \r\n"
				+ "       	) as b on a.SaleOrder = b.SaleOrder and "
				+ "                   a.SaleLine = b.SaleLine   \r\n"
				+ "		 	where b.DataStatus = 'O' and b.SaleLine <> '' ) as a  \r\n "
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n" 
				+ this.pss.leftJoinTempG
				+ this.pss.leftJoinSCC 
				+ this.pss.leftJoinB_H
				+ this.leftJoinR
				+ this.pss.leftJoinM
				+ this.pss.leftJoinUCAL
				+ where
				+ " and ( b.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' ) ) \r\n"   ;
		String sqlOPSW = ""
				+ " select \r\n"
				+ this.selectAll
	  		    + " INTO #tempOPSW  \r\n"
				+ " from #tempPrdOPSW as a \r\n"
				+ " where ( a.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' )) \r\n"
				+ whereCaseTry ;
//////			// Switch 
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
				+ "		 	inner join [PCMS].[dbo].[SwitchProdOrder]  as b on  a.SaleOrder = b.SaleOrderSW and "
				+ "																a.SaleLine = b.SaleLineSW \r\n \r\n"
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
				+ "                AND a.[DataStatus] = 'O' "
				+ "				) AS A\r\n"
				+ "				group by PRDORDERSW\r\n"
				+ "		 	) AS C ON B.ProductionOrderSW = C.PRDORDERSW \r\n"
				+ "		 	where b.DataStatus = 'O') as a  \r\n "
				+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n" 
				+ this.pss.leftJoinTempG 
				+ this.pss.leftJoinSCC
				+ this.pss.leftJoinB_H
				+ this.leftJoinR
				+ this.pss.leftJoinM
				+ this.pss.leftJoinUCAL
				+ where
				+ " and ( b.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' )) \r\n"  ;
			String sqlSW =  ""
					  + " select \r\n"
					  + this.selectAll
		  		      + " INTO #tempSW  \r\n"
					  + " from #tempPrdSW as a \r\n" ;
//////			// สวม 
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
		  		    + "		select \r\n"
		  		    + "			a.SaleOrder , \r\n"
		  		    + "			a.SaleLine, \r\n"
		  		    + "			CASE WHEN a.Volume = 0 THEN b.Volumn ELSE a.Volume END as [Volume] ,\r\n"
		  		    + "			[ProductionOrderRP] AS ProductionOrder \r\n"
		  		    + "		from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
		  		    + "		LEFT JOIN [PCMS].[dbo].[FromSapMainProd] AS b ON a.ProductionOrderRP = b.ProductionOrder  \r\n" 
		  		    + "		WHERE a.[DataStatus] = 'O'  \r\n"
		  		    + "			AND (b.UserStatus NOT IN ('ยกเลิก', 'ตัดเกรดZ'))  \r\n"
		  		    + " )  as rpo on a.SaleOrder = rpo.SaleOrder \r\n"
		  		    + "		  and a.SaleLine = rpo.SaleLine \r\n"
					+ " left join  [PCMS].[dbo].[FromSapMainProd] as b on b.ProductionOrder = rpo.ProductionOrder \r\n" 
					+ this.pss.leftJoinTempG 
					+ this.pss.leftJoinSCC
					+ this.pss.leftJoinB_H
					+ this.leftJoinR
					+ this.pss.leftJoinM
					+ this.pss.leftJoinUCALRP
					+ " where ( b.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' )) \r\n"
					+ whereCaseTryRP    ;
  
			String sqlRP = ""
						+ " select \r\n"
						+ this.selectAll
			  		    + " INTO #tempRP  \r\n"
						+ " from #tempPrdReplaced as a \r\n"  ;

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
			 	+ this.pss.createTempPlanDeliveryDate 
			 	+ this.pss.createTempSumGR
			 	+ this.pss.createTempSumBill
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
				+ " left join  #tempMain as b on a.SaleOrder = b.SaleOrder and "
				+ "                              a.SaleLine = b.SaleLine\r\n"
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
				+ " Order by CustomerShortName, DueDate, [SaleOrder], [SaleLine],TypePrdRemark, [ProductionOrder] "; 
//			 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}
		return list;
	}
 
	@Override
	public ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSAllDetail> list = null;
		String where = " where  ";
		String prdOrder = "" ;
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "' \r\n";
		String fromMainB = ""
				+ " from ( \r\n"
				+ "			SELECT distinct \r\n"
				+ this.leftJoinBSelect
				+ this.pss.fromMainSale_A
				+ this.pss.leftJoinBPartOneT_A
				+ this.pss.leftJoinBPartOneS_A
				+ this.pss.leftJoinBPartOneH_A
				+ this.pss.leftJoinTempG_A
				+ this.pss.leftJoinSCC_A
				+ this.pss.leftJoinM_A
				+ this.pss.leftJoinUCAL_A
//				  + this.leftJoinFSMBBTempSumBill_A 
				+ where
				  + " ) as b \r\n";
		String sql =  ""
				+ this.pss.createTempMainSale
				+ this.pss.createTempPlanDeliveryDate
			 	+ this.pss.createTempSumBill
			 	+ this.pss.createTempSumGR
				+  " SELECT distinct top 1  \r\n "
				+ this.selectTwo
				+ fromMainB
				+ this.pss.leftJoinTempG
				+ " Order by SaleOrder , 	SaleLine"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		if (list.size() > 0) {
			boolean isCheck = false ;
//			isCheck = true; 
			ShopFloorControlModel sfcModel = new ShopFloorControlModel();
			ImportDetailModel idModel = new ImportDetailModel();
			InspectOrdersModel insOrderModel = new InspectOrdersModel();
			RollFromSapModel rfsModel = new RollFromSapModel();
//			FromSapPOModel fspoModel = new FromSapPOModel();
//			FromSapFinishingModel fsfModel = new FromSapFinishingModel();
//			FromSapPresetModel fspModel = new FromSapPresetModel();
//			FromSapDyeingModel fsdModel = new FromSapDyeingModel( );
//			FromSapSendTestQCModel fsstQCModel = new FromSapSendTestQCModel( );
//			FromSapReceipeModel fsrModel = new FromSapReceipeModel( );
			InspectNcModel insNCModel = new InspectNcModel( );
//			FromSapSaleInputModel fssiModel = new FromSapSaleInputModel( );
			FromSapSaleModel fssModel = new FromSapSaleModel( );
			FromSapCFMModel fsCFMModel = new FromSapCFMModel( );
//			FromSapWaitTestModel fswtModel = new FromSapWaitTestModel( );
//			FromSapInspectModel fsiModel = new FromSapInspectModel( );
//			FromSapWorkInLabModel fswilModel = new FromSapWorkInLabModel( );
			FromSapPackingModel fspackingModel = new FromSapPackingModel( );
			FromSapSubmitDateModel fssdModel = new FromSapSubmitDateModel();
			String productionOrder = bean.getProductionOrder();
			if(isCheck) { System.out.println("1: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
//			ArrayList<PODetail> poDetailList = fspoModel.getFromSapPODetailByProductionOrder(productionOrder); 
			ArrayList<PODetail> poDetailList = rfsModel.getRollFromSapDetailByProductionOrder(productionOrder) ; 
//			ArrayList<SendTestQCDetail> sendTestQCDetailList = fsstQCModel.getFromSapSendTestQCByProductionOrder(productionOrder);
//			ArrayList<FinishingDetail> finDetailList = fsfModel.getFromSapFinishingDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("2: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<PackingDetail> packDetailList = fspackingModel.getFromSapPackingDetailByProductionOrder(productionOrder);
//			ArrayList<WorkInLabDetail> workInLabDetailList = fswilModel.getFromSapWorkInLabDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("3: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<ImportDetail> workInLabDetailList = idModel.getImportDetailByProductionOrder(prdOrder);
//			ArrayList<WaitTestDetail> waitTestDetailList = fswtModel.getFromSapWaitTestDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("4: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<CFMDetail> cfmDetailList = fsCFMModel.getFromSapCFMDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("5: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<SaleDetail> saleDetailList = fssModel.getFromSapSaleDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("6: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
//			ArrayList<SaleInputDetail> saleInputDetailList = fssiModel.getFromSapSaleInputDetailByProductionOrder(productionOrder);
//			ArrayList<InputDateDetail> submitdatDetailList = getSubmitDateDetail(poList);  

			ArrayList<InputDateDetail> submitdatDetailList = fssdModel.getSubmitDateDetail(poList);
			if(isCheck) { System.out.println("7: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<NCDetail> ncDetailList = insNCModel.getInspectNcByProductionOrder(prdOrder);
			if(isCheck) { System.out.println("8: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
//			ArrayList<ReceipeDetail> receipeDetailList = fsrModel.getFromSapReceipeDetailByProductionOrder(productionOrder);
//			if(isCheck) { System.out.println("9: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			

			ArrayList<ShopFloorControlDetail> presetDetailList = new ArrayList<ShopFloorControlDetail>();
			ArrayList<ShopFloorControlDetail> dyeingDetailList = new ArrayList<ShopFloorControlDetail>();
			ArrayList<ShopFloorControlDetail> insDetailList = new ArrayList<ShopFloorControlDetail>();
			ArrayList<ShopFloorControlDetail> finDetailList = new ArrayList<ShopFloorControlDetail>();
			ArrayList<ShopFloorControlDetail> sfcList = sfcModel.getShopFloorControlDetailByProductionOrder(prdOrder);
			for(ShopFloorControlDetail sfcBean : sfcList) {
				if(sfcBean.getOperation().equals("60")||sfcBean.getOperation().equals("145")||sfcBean.getOperation().equals("180")||
					sfcBean.getOperation().equals("200")||sfcBean.getOperation().equals("201") ) {
					if(sfcBean.getOperation().equals("200")||sfcBean.getOperation().equals("201")) {
						ArrayList<InspectOrdersDetail> insOrderList = insOrderModel.getInspectOrdersByProductionOrder(prdOrder);
						for(InspectOrdersDetail insBean : insOrderList) {
							sfcBean.setMachineInspect(insBean.getMachineInspect());
							sfcBean.setInspectRemark(insBean.getInspectNote());
						}
					}
					insDetailList.add(sfcBean);
				}
				else if(sfcBean.getOperation().equals("100")||sfcBean.getOperation().equals("101")||sfcBean.getOperation().equals("102")||
						sfcBean.getOperation().equals("103")||sfcBean.getOperation().equals("104") ) {
					dyeingDetailList.add(sfcBean);
					}
				else if(sfcBean.getOperation().equals("50") ) {
					presetDetailList.add(sfcBean);
					}
				else if(sfcBean.getOperation().equals("190")||sfcBean.getOperation().equals("191")||sfcBean.getOperation().equals("192")||
						sfcBean.getOperation().equals("193") )  {
					finDetailList.add(sfcBean);
				}
			}
			if(isCheck) { System.out.println("10: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			PCMSAllDetail beanTmp = list.get(0);
			beanTmp.setPoDetailList(poDetailList);
			beanTmp.setPresetDetailList(presetDetailList);
//			beanTmp.setSendTestQCDetailList(sendTestQCDetailList);
			beanTmp.setDyeingDetailList(dyeingDetailList);
			beanTmp.setFinishingDetailList(finDetailList);
			beanTmp.setInspectDetailList(insDetailList);
			beanTmp.setPackingDetailList(packDetailList);

			beanTmp.setWorkInLabDetailList(workInLabDetailList);
//			beanTmp.setWaitTestDetailList(waitTestDetailList);
			beanTmp.setCfmDetailList(cfmDetailList);
			beanTmp.setSaleDetailList(saleDetailList);
//			beanTmp.setSaleInputDetailList(saleInputDetailList);
			beanTmp.setSubmitDateDetailList(submitdatDetailList);
			beanTmp.setNcDetailList(ncDetailList);
//			beanTmp.setReceipeDetailList(receipeDetailList);

		}
		return list;
	}   
	@Override
	public ArrayList<PCMSAllDetail> getUserStatusList() {
		FromSapMainProdModel fsmpModel = new FromSapMainProdModel();
		ArrayList<PCMSAllDetail> list = fsmpModel.getUserStatusDetail();
//		PCMSAllDetail bean = new PCMSAllDetail();
//		bean.setUserStatus("รอ COA ลูกค้า ok สี");
//		list.add(bean);
//		bean = new PCMSAllDetail();
//		bean.setUserStatus("ขายแล้วบางส่วน");
//		list.add(bean);
//		bean = new PCMSAllDetail();
//		bean.setUserStatus("รอตอบ CFM ตัวแทน");
//		list.add(bean);
//		bean = new PCMSAllDetail();
//		bean.setUserStatus("รอเปิดบิล");
//		list.add(bean);
		return list;
	}
	private String forPage = "Summary";
	@Override
	public ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList) {
		SearchSettingModel ssModel = new SearchSettingModel();
		ArrayList<PCMSTableDetail> list = null;
		String customerShortName = "", userStatus = "", customerName="",userId = "" ,divisionName = "";
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
