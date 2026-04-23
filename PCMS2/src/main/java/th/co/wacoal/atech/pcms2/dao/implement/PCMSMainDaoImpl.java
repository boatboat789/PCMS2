	package th.co.wacoal.atech.pcms2.dao.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.service.PCMSSearchService;
import th.co.wacoal.atech.pcms2.service.PCMSSqlService;
import th.co.wacoal.atech.pcms2.service.master.FromSapCFMService;
import th.co.wacoal.atech.pcms2.service.master.FromSapPackingService;
import th.co.wacoal.atech.pcms2.service.master.FromSapSaleService;
import th.co.wacoal.atech.pcms2.service.master.FromSapSubmitDateService;
import th.co.wacoal.atech.pcms2.service.master.InspectSystem.InspectNcService;
import th.co.wacoal.atech.pcms2.service.master.InspectSystem.InspectOrdersService;
import th.co.wacoal.atech.pcms2.service.master.LBMS.ImportDetailService;
import th.co.wacoal.atech.pcms2.service.master.PPMM.RollFromSapService;
import th.co.wacoal.atech.pcms2.service.master.PPMM.ShopFloorControlService;
import th.in.totemplate.core.sql.Database;
@Repository // Spring annotation to mark this as a DAO component
public class PCMSMainDaoImpl implements PCMSMainDao { 
	private PCMSSqlService pss = new PCMSSqlService();
	private BeanCreateService bcModel = new BeanCreateService();
	private Database database; 
	private String selectOPSWA =
			    "   "
		      + "     a.SaleOrder \r\n"
			  + "	, a.[SaleLine] \r\n"
			  + "   , a.[SaleFullName] \r\n" 
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
			  + "	, a.[SaleLine] \r\n"
			  + "   , a.[SaleFullName] \r\n" 
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
			  + "	, a.[SaleLine] \r\n"
			  + "   , a.[SaleFullName] \r\n" 
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
			+ "	  , b.[SaleLine] \r\n"
			+ "   , b.[SaleFullName] \r\n" 
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
			  + "	, a.[SaleLine] \r\n"
			  + "   , a.[SaleFullName] \r\n" 
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
				  + "	, a.[SaleLine] \r\n"
				  + "   , a.[SaleFullName] \r\n" 
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
		    + "	, b.[SaleLine] \r\n"  
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
			  + "			     , adjVol AS SumVol\r\n"
			  + "				 , a.Price * adjVol AS SumVolFGAmount"  
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
	 		  + "                ,coalesce ( SCC.SendCFMCusDate ,g.SendCFMCusDate ) AS SendCFMCusDate \r\n"
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
	 		  + "				,CASE \r\n"
	 		  + "				  WHEN adjVol IS NOT NULL THEN 1\r\n"
	 		  + "				  WHEN a.LotNo IN ('รอจัด Lot','ขาย stock','รับจ้างถัก','Lot ขายแล้ว','พ่วงแล้วรอสวม','รอสวมเคยมี Lot')\r\n"
	 		  + "					   AND adjVol = 0 AND CRP.SaleOrder IS NULL THEN 1\r\n"
	 		  + "				  WHEN a.Volumn = 0 THEN 1\r\n"
	 		  + "				  WHEN viewUSM_SPE.Special = 0 THEN 1\r\n"
	 		  + "				  ELSE 0\r\n"
	 		  + "				 END AS PassFilter\r\n"  ;
	    private final PCMSSearchService psService;
	    private final ShopFloorControlService sfcService;
	    private final RollFromSapService rfsService;
	    private final ImportDetailService idService;
	    private final InspectOrdersService insOrderService;
	    private final InspectNcService insNCService;

	    // Services เพิ่มเติมจากที่คุณส่งมา (เปลี่ยนชื่อให้สอดคล้องและชัดเจน)
	    private final FromSapSaleService fromSapSaleService;
	    private final FromSapCFMService fromSapCFMService;
	    private final FromSapPackingService fromSapPackingService;
	    private final FromSapSubmitDateService fromSapSubmitDateService; 
 
	    @Autowired
	    public PCMSMainDaoImpl(
	            @Qualifier("pcmsDatabase") Database database,
	            PCMSSearchService psService,
	            ShopFloorControlService sfcService,
	            RollFromSapService rfsService,
	            ImportDetailService idService,
	            InspectOrdersService insOrderService,
	            InspectNcService insNCService, 
	            FromSapSaleService fromSapSaleService,
	            FromSapCFMService fromSapCFMService,
	            FromSapPackingService fromSapPackingService,
	            FromSapSubmitDateService fromSapSubmitDateService ) {

	        this.database = database;
	        this.psService = psService;
	        this.sfcService = sfcService;
	        this.rfsService = rfsService;
	        this.idService = idService;
	        this.insOrderService = insOrderService;
	        this.insNCService = insNCService;

	        this.fromSapSaleService = fromSapSaleService;
	        this.fromSapCFMService = fromSapCFMService;
	        this.fromSapPackingService = fromSapPackingService;
	        this.fromSapSubmitDateService = fromSapSubmitDateService; 
 
	    } 
    @Override
	public ArrayList<PCMSTableDetail> getPCMSSumaryDetail(ArrayList<PCMSTableDetail> poList ) {
		 
		ArrayList<PCMSTableDetail> list = null;
		PCMSTableDetail bean = poList.get(0); 
		List<String> userStatusList = bean.getUserStatusList();
		Map<String, String> results = pss.buildWhereClauses(bean);
		String whereCaseTry = results.get("whereCaseTry");
		String whereCaseTryRP = results.get("whereCaseTryRP");
		String tmpWhereNoLotUCAL = results.get("tmpWhereNoLotUCAL");
		String whereBase = results.get("whereBase"); 
//		String whereBMainUserStatus = results.get("whereBMainUserStatus");
		String whereSale = results.get("whereSale");
		String whereWaitLot = results.get("whereWaitLot");
		String createTempTableUserStatus = ""
				+ psService.handlerTempTableUserStatusList(userStatusList);
		String createCusListSearch = ""
				+ psService.handlerTempTableCustomerSearchList(bean.getCustomerNameList(), bean.getCustomerShortNameList());
			String createTempMainSale = ""
				+ createTempTableUserStatus
				+ createCusListSearch
				+ this.pss.createTempMainSaleWithJoinCustomer 
				+ whereSale 
				; 
		String sqlWaitLot =
				  ""
				+ this.pss.createTempPrepWaitLot
				+ " SELECT    \r\n"
				+ this.selectWaitLot
	  		    + " INTO #tempWaitLot  \r\n"
				+ " FROM #tempMainSale as a \r\n "
				+ this.pss.innerJoinWaitLotB 
				+ this.pss.getLeftJoinTempPlandeliveryDate("b","a") 
				+ whereWaitLot
				+ " and ( SumVol = 'B' OR countProdRP > 0 ) \r\n"; 
		String fromMainB = ""
				+ " from ( \r\n"
				+ "	SELECT   \r\n"
				+ this.leftJoinBSelect
				+ this.pss.fromProdA
				+ this.pss.getLeftJoinTempPlandeliveryDate("a", "a")
				+ this.pss.buildLeftJoinTempProdWorkDate("a")
				+ this.pss.buildLeftJoinSCC("a") 
				+ this.pss.buildLeftJoinTempSumGR("a")
				+ this.pss.buildLeftJoinUserStatusAuto("UCAL", "A", "m")
				+ this.pss.buildLeftJoinViewUserStatusMappingPCMS("UCAL", "UserStatusCal", 0)
				+ this.pss.getLeftJoinCRP("a")
				+ whereBase.replace("b.", "a.")
//				+ this.pss.crossApplyVolCalc
				+ " ) as b \r\n";
		String sqlMain = ""
	  		    + this.pss.withProdData
				+ " SELECT DISTINCT \r\n "
				+ this.selectMainV2
	  		    + " INTO #tempMain  \r\n"
				+ fromMainB   
				+ this.pss.getLeftJoinCRP("b") 
				+ this.pss.getLeftJoinSwitchProdOrder("b") 
				+ " where b.PassFilter = 1\r\n" 
				+ "    AND SPO.ProductionOrderSW IS NULL " 
				;
				// Order Puang
//				+ " union ALL  "
		String createTempOPFromA =  ""
				+ " If(OBJECT_ID('tempdb..#tempPrdOPA') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempPrdOPA\r\n"
				+ "	end ; \r\n"
    			+ "       SELECT  \r\n"
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
				+ "       inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleOrder = b.SaleOrder and \n"
				+ "                                                             a.SaleLine = b.SaleLine and  \r\n"
				+ "                                                             b.[DataStatus] = 'O' \n" 
				+ "       "+this.pss.buildLeftJoinTempProdWorkDate("b")
				+ "       "+this.pss.buildLeftJoinSCC("b")
				+ "       "+this.pss.buildLeftJoinTempSumGR("b")
				+ this.pss.buildLeftJoinUserStatusAuto("UCAL","b","m") 
				+ "       where 1 = 1  \r\n"
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
 
				+ this.pss.getLeftJoinTempPlandeliveryDate("b","a")     ;
		String sqlOP = ""
					+ " select \r\n"
					+ this.selectAll
		  		    + " INTO #tempOP  \r\n"
					+ " from #tempPrdOP as a \r\n" 
					+ this.pss.getLeftJoinSwitchProdOrder("A") 
					+ this.pss.buildInnerJoinViewUSM_SPE("a",1,"UserStatus")
					+ " where 1 = 1 "
					+ "    AND SPO.ProductionOrderSW IS NULL " 
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
				+ this.pss.buildInnerJoinFromSapMainProd("b", "ProductionOrder","a","ProductionOrder")
				+ this.pss.buildInnerJoinViewUSM_SPE("b",1,"UserStatus")
				+ this.pss.buildLeftJoinTempProdWorkDate("b")
				+ this.pss.buildLeftJoinSCC("b")  
				+ this.pss.getLeftJoinTempPlandeliveryDate("b","a") 
				+ this.pss.getLeftJoinSwitchProdOrder("b", "ProductionOrderSW")
				+ this.pss.buildLeftJoinTempSumGR("b")
				+ this.pss.buildLeftJoinUserStatusAuto("UCAL","b","m") 
				+ whereBase
				+ " and 1 = 1 \r\n"   ;
		String sqlOPSW = ""
				+ " select \r\n"
				+ this.selectAll
	  		    + " INTO #tempOPSW  \r\n"
				+ " from #tempPrdOPSW as a \r\n"
				+ this.pss.buildInnerJoinViewUSM_SPE("a",1,"UserStatus")
				+ " where 1 = 1 " 
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
				+ this.pss.buildInnerJoinFromSapMainProd("b", "ProductionOrder","a","ProductionOrder")
				+ this.pss.buildInnerJoinViewUSM_SPE("b",1,"UserStatus")
				+ this.pss.buildLeftJoinTempProdWorkDate("b") 
				+ this.pss.buildLeftJoinSCC("b") 
				+ this.pss.getLeftJoinTempPlandeliveryDate("b","a") 
				+ this.pss.getLeftJoinSwitchProdOrder("b", "ProductionOrderSW") 
				+ this.pss.buildLeftJoinTempSumGR("b") 
				+ this.pss.buildLeftJoinUserStatusAuto("UCAL","b","m") 
				+ whereBase
				+ " and 1 = 1 \r\n"  ;
			String sqlSW =  ""
					  + " select \r\n"
					  + this.selectAll
		  		      + " INTO #tempSW  \r\n"
					  + " from #tempPrdSW as a \r\n" ;
//////			// สวม 
			String createTempRP = ""
					+ "  If(OBJECT_ID('tempdb..#tempRP') Is Not Null)\r\n"
					+ "	begin\r\n"
					+ "		Drop Table #tempRP\r\n"
					+ "	end ;  \r\n"
					+ " ;WITH PRD_REPLACED AS ( \r\n"
					+" SELECT    \r\n"
					+ this.selectRP 
					+ " from #tempMainSale as a  \r\n"
		  		    + " inner join ( \r\n"
		  		    + "		select \r\n"
		  		    + "			a.SaleOrder , \r\n"
		  		    + "			a.SaleLine, \r\n"
		  		    + "			CASE WHEN a.Volume = 0 THEN b.Volumn ELSE a.Volume END as [Volume] ,\r\n"
					+ "			a.[ProductionOrderRP] AS ProductionOrder , \r\n"
					+ "			b.TotalQuantity,\r\n" 
					+ "			b.LotNo,\r\n"
					+ "			b.LabNo,\r\n"
					+ "			b.LabStatus,\r\n"
					+ "			b.CFTYPE ,\r\n"
					+ "			b.RemarkOne,\r\n"
					+ "			b.RemarkTwo,\r\n"
					+ "			b.RemarkThree ,\r\n"
					+ "			b.[PrdCreateDate]\r\n"
		  		    + "		from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"  
					+ this.pss.buildInnerJoinFromSapMainProd("b", "ProductionOrder","a","ProductionOrderRP")
					+ this.pss.buildInnerJoinViewUSM_SPE("b",1,"UserStatus")
		  		    + "		WHERE a.[DataStatus] = 'O'  \r\n" 
		  		    + " )  as b on a.SaleOrder = b.SaleOrder \r\n"
		  		    + "		  and a.SaleLine = b.SaleLine \r\n" 
					+ this.pss.buildLeftJoinTempProdWorkDate("b") 
					+ this.pss.buildLeftJoinSCC("b") 
					+ this.pss.getLeftJoinTempPlandeliveryDate("b","a") 
					+ this.pss.getLeftJoinSwitchProdOrder("b", "ProductionOrderSW") 
					+ this.pss.buildLeftJoinTempSumGR("b") 
					+ this.pss.buildLeftJoinUserStatusAuto("UCALRP","b","m") 
					+ " where 1 = 1 \r\n"
					+ whereCaseTryRP  
					+ " ) " 
					+ " select \r\n"
					+ this.selectAll
		  		    + " INTO #tempRP  \r\n"
					+ " from PRD_REPLACED as a \r\n"  ;

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
			 	+ createTempOPFromA
				+ sqlOP  
				+ this.pss.buildIfTempTableDrop("#tempPrdOPA") 
			 	+ createTempOPSWFromA
				+ sqlOPSW 
				+ this.pss.buildIfTempTableDrop("#tempPrdOPSW")   
			 	+ createTempSWFromA 
				+ sqlSW 
				+ this.pss.buildIfTempTableDrop("#tempPrdSW")  
				+ createTempRP
				+ this.pss.createTempForMainAndWaitLot
				+ sqlWaitLot
				+ sqlMain
				+ this.pss.createDropTempForMainAndWaitLot
				+ " SELECT a.* FROM #tempWaitLot as a\r\n"
				+ " left join  #tempMain as b on a.SaleOrder = b.SaleOrder and "
				+ "                              a.SaleLine = b.SaleLine\r\n"
				+ " where b.SaleOrder is null \r\n"
				+ " union ALL  \r\n"
				+ " SELECT * FROM #tempMain as a\r\n"
				+ " where 1 = 1 "
//				+whereBMainUserStatus
				+ " union ALL  \r\n"
				+ " SELECT * FROM #tempOP\r\n"
				+ " union ALL  \r\n"
				+ " SELECT * FROM #tempOPSW\r\n"
				+ " union ALL  \r\n"
				+ " SELECT * FROM #tempSW\r\n"
				+ " union ALL  \r\n"
				+ " SELECT * FROM #tempRP\r\n"
				+ " Order by CustomerShortName, DueDate, [SaleOrder], [SaleLine],TypePrdRemark, [ProductionOrder] "; 
//		System.out.println(sql);
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
	    	    + this.pss.fromProdA  
				+ this.pss.getLeftJoinTempPlandeliveryDate("a","a") 
				+ this.pss.buildLeftJoinTempProdWorkDate("a")
				+ this.pss.buildLeftJoinSCC("a")
				+ this.pss.buildLeftJoinTempSumGR("a") 
				+ this.pss.buildLeftJoinUserStatusAuto("UCAL","a","m") 
				+ this.pss.buildLeftJoinViewUserStatusMappingPCMS("UCAL", "UserStatusCal",0)
				+ this.pss.getLeftJoinCRP("a")   
				+ where
				  + " ) as b \r\n";
		String sql =  ""
				+ this.pss.createTempMainSale
				+ this.pss.createTempPlanDeliveryDate
			 	+ this.pss.createTempSumBill
			 	+ this.pss.createTempSumGR
				+ this.pss.createTempForMainAndWaitLot
	  		    + this.pss.withProdData
				+  " SELECT distinct top 1  \r\n "
				+ this.selectTwo
				+ fromMainB
				+ this.pss.buildLeftJoinTempProdWorkDate("b")
				+ " Order by SaleOrder , 	SaleLine"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		if (list.size() > 0) {
			boolean isCheck = false ;
//			isCheck = true; 
			String productionOrder = bean.getProductionOrder();
			if(isCheck) { System.out.println("1: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
//			ArrayList<PODetail> poDetailList = fspoModel.getFromSapPODetailByProductionOrder(productionOrder); 
			ArrayList<PODetail> poDetailList = rfsService.getRollFromSapDetailByProductionOrder(productionOrder) ; 
//			ArrayList<SendTestQCDetail> sendTestQCDetailList = fsstQCModel.getFromSapSendTestQCByProductionOrder(productionOrder);
//			ArrayList<FinishingDetail> finDetailList = fsfModel.getFromSapFinishingDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("2: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<PackingDetail> packDetailList = fromSapPackingService.getFromSapPackingDetailByProductionOrder(productionOrder);
//			ArrayList<WorkInLabDetail> workInLabDetailList = fswilModel.getFromSapWorkInLabDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("3: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<ImportDetail> workInLabDetailList = idService.getImportDetailByProductionOrder(prdOrder);
//			ArrayList<WaitTestDetail> waitTestDetailList = fswtModel.getFromSapWaitTestDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("4: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<CFMDetail> cfmDetailList = fromSapCFMService.getFromSapCFMDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("5: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<SaleDetail> saleDetailList = fromSapSaleService.getFromSapSaleDetailByProductionOrder(productionOrder);
			if(isCheck) { System.out.println("6: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
//			ArrayList<SaleInputDetail> saleInputDetailList = fssiModel.getFromSapSaleInputDetailByProductionOrder(productionOrder);
//			ArrayList<InputDateDetail> submitdatDetailList = getSubmitDateDetail(poList);  

			ArrayList<InputDateDetail> submitdatDetailList = fromSapSubmitDateService.getSubmitDateDetail(poList);
			if(isCheck) { System.out.println("7: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			ArrayList<NCDetail> ncDetailList = insNCService.getInspectNcByProductionOrder(prdOrder);
			if(isCheck) { System.out.println("8: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
//			ArrayList<ReceipeDetail> receipeDetailList = fsrModel.getFromSapReceipeDetailByProductionOrder(productionOrder);
//			if(isCheck) { System.out.println("9: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));}
			
 
			ArrayList<ShopFloorControlDetail> presetDetailList = new ArrayList<ShopFloorControlDetail>();
			ArrayList<ShopFloorControlDetail> dyeingDetailList = new ArrayList<ShopFloorControlDetail>();
			ArrayList<ShopFloorControlDetail> insDetailList = new ArrayList<ShopFloorControlDetail>();
			ArrayList<ShopFloorControlDetail> finDetailList = new ArrayList<ShopFloorControlDetail>();
			ArrayList<ShopFloorControlDetail> sfcList = sfcService.getShopFloorControlDetailByProductionOrder(prdOrder);
			for(ShopFloorControlDetail sfcBean : sfcList) {
				if(sfcBean.getOperation().equals("60")||sfcBean.getOperation().equals("145")||sfcBean.getOperation().equals("180")||
					sfcBean.getOperation().equals("200")||sfcBean.getOperation().equals("201") ) {
					if(sfcBean.getOperation().equals("200")||sfcBean.getOperation().equals("201")) {
						ArrayList<InspectOrdersDetail> insOrderList = insOrderService.getInspectOrdersByProductionOrder(prdOrder);
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
}
