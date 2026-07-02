package th.co.wacoal.atech.pcms2.dao.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
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
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
@Repository // Spring annotation to mark this as a DAO component
public class PCMSMainDaoImpl implements PCMSMainDao {
	private PCMSSqlService pss = new PCMSSqlService();
	private BeanCreateService bcModel = new BeanCreateService();
	private JdbcTemplate jdbc;
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
	 		  + pss.passFilterExpr;
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
            @Qualifier("pcmsDatabase") JdbcTemplate jdbc,
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

        this.jdbc = jdbc;
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
	public ArrayList<PCMSTableDetail> getPCMSSumaryDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSTableDetail> list = null;
		PCMSTableDetail bean = poList.get(0);

		Map<String, String> results   = pss.buildWhereClauses(bean);
		String whereCaseTry           = results.get("whereCaseTry");
		String whereCaseTryRP         = results.get("whereCaseTryRP");
		String tmpWhereNoLotUCAL      = results.get("tmpWhereNoLotUCAL");
		String whereBase              = results.get("whereBase");
		String whereSale              = results.get("whereSale");
		String whereWaitLot           = results.get("whereWaitLot");

		String sql = "SET NOCOUNT ON;\r\n"
				+ buildSummaryGuardDrops()
				+ buildSummaryCommonTables(bean, whereSale)
				+ buildSummaryOPTables(tmpWhereNoLotUCAL, whereCaseTry)
				+ buildSummaryOPSWTable(whereBase, whereCaseTry)
				+ buildSummarySWTable(whereBase)
				+ buildSummaryRPTable(whereCaseTryRP)
				+ buildSummaryMainAndWaitLot(whereBase, whereWaitLot)
				+ buildSummaryFinalSelect();

		List<Map<String, Object>> datas = SqlStatementHandler.queryList(this.jdbc, PCMSSqlService.dropAllTemp, sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}
		return list;
	}

	// =========================================================================
	// SQL batch builders — getPCMSSumaryDetail
	// =========================================================================

	/** Drop output temp tables ก่อนเริ่ม batch เพื่อป้องกัน error จาก session เก่า */
	private String buildSummaryGuardDrops() {
		return pss.buildIfTempTableDrop("#tempWaitLot")
				+ pss.buildIfTempTableDrop("#tempMain")
				+ pss.buildIfTempTableDrop("#tempOP")
				+ pss.buildIfTempTableDrop("#tempOPSW")
				+ pss.buildIfTempTableDrop("#tempSW")
				+ pss.buildIfTempTableDrop("#tempRP");
	}

	/** Lookup tables ที่ใช้ร่วมกันทุก type (#tempMainSale, #tempSumGR, #tempSPO ฯลฯ) */
	private String buildSummaryCommonTables(PCMSTableDetail bean, String whereSale) {
		String createUserStatus = psService.handlerTempTableUserStatusList(bean.getUserStatusList());
		String createCusList    = psService.handlerTempTableCustomerSearchList(
				bean.getCustomerNameList(), bean.getCustomerShortNameList());
		return createUserStatus + createCusList
				+ pss.createTempMainSaleWithJoinCustomer + whereSale
				+ pss.createTempMainSaleIndex
				+ pss.createTempPlanDeliveryDate
				+ pss.createTempSumGR
				+ pss.createTempSumBill
				+ pss.createTempSCC
				+ pss.createTempProdWorkDateFiltered   // filtered by #tempMainSale — was full scan
				+ pss.createTempSPO;
	}

	/**
	 * OrderPuang: #tempPrdOPA (ProdSale + UCAL) → #tempPrdOP (+ FromSapMainProd) → #tempOP
	 * filter ตาม tmpWhereNoLotUCAL ในขั้น OPA, ตาม whereCaseTry ในขั้น OP
	 */
	private String buildSummaryOPTables(String tmpWhereNoLotUCAL, String whereCaseTry) {
		String createOPA = pss.buildIfTempTableDrop("#tempPrdOPA")
				+ "SELECT a.SaleOrder, a.[SaleLine], a.DistChannel, a.Color, a.ColorCustomer\r\n"
				+ "      ,a.SaleQuantity, a.RemainQuantity, a.SaleUnit, a.DueDate\r\n"
				+ "      ,a.CustomerShortName, a.[SaleFullName], a.[SaleNumber], a.SaleCreateDate\r\n"
				+ "      ,a.MaterialNo, a.DeliveryStatus, a.SaleStatus\r\n"
				+ "      ,b.ProductionOrder, a.CustomerName, a.DesignFG, a.OrderAmount\r\n"
				+ "      ,'SUB' AS TypePrdRemark, a.ArticleFG, a.ShipDate, a.Division\r\n"
				+ "      ,a.PurchaseOrder, a.CustomerMaterial, a.Price, a.RemainAmount, a.CustomerDue\r\n"
				+ "      ,CASE WHEN b.Volumn <> 0 THEN b.Volumn ELSE 0 END AS Volumn\r\n"
				+ "      ,g.[DyePlan], g.[DyeActual], g.[Dryer], g.[Finishing], g.[Inspectation]\r\n"
				+ "      ,g.[Prepare], g.[Preset], g.[Relax], g.[CFMDateActual], g.[CFMPlanDate]\r\n"
				+ "      ,g.[DyeStatus], UCAL.UserStatusCal AS UserStatus\r\n"
				+ "      ,CASE WHEN SCC.SendCFMCusDate IS NOT NULL AND SCC.SendCFMCusDate <> ''\r\n"
				+ "            THEN SCC.SendCFMCusDate ELSE g.SendCFMCusDate END AS SendCFMCusDate\r\n"
				+ "      ,m.GRSumKG, m.GRSumYD, m.GRSumMR\r\n"
				+ "      ,g.CFMDetailAll, g.CFMNumberAll, g.CFMRemarkAll, g.RollNoRemarkAll\r\n"
				+ "      ,g.CFMActualLabDate, g.CFMCusAnsLabDate, g.GreigeInDate, g.LotShipping, g.PlanGreigeDate\r\n"
				+ "INTO #tempPrdOPA\r\n"
				+ "FROM #tempMainSale AS a\r\n"
				+ "INNER JOIN [PCMS].[dbo].[FromSapMainProdSale] AS b\r\n"
				+ "    ON a.SaleOrder = b.SaleOrder AND a.SaleLine = b.SaleLine AND b.[DataStatus] = 'O'\r\n"
				+ pss.buildLeftJoinTempProdWorkDate("b")
				+ pss.buildLeftJoinSCC("b")
				+ pss.buildLeftJoinTempSumGR("b")
				+ pss.buildLeftJoinUserStatusAuto("UCAL", "b", "m")
				+ "WHERE 1 = 1\r\n"
				+ "      " + tmpWhereNoLotUCAL + "\r\n";

		String createOP = pss.buildIfTempTableDrop("#tempPrdOP")
				+ "SELECT DISTINCT\r\n" + this.selectOP
				+ "INTO #tempPrdOP\r\n"
				+ "FROM #tempPrdOPA AS a\r\n"
				+ "LEFT JOIN [PCMS].[dbo].[FromSapMainProd] AS b ON a.ProductionOrder = b.ProductionOrder\r\n"
				+ pss.getLeftJoinTempPlandeliveryDate("b", "a");

		String insertOP = "SELECT\r\n" + this.selectAll
				+ "INTO #tempOP\r\n"
				+ "FROM #tempPrdOP AS a\r\n"
				+ "LEFT JOIN #tempSPO AS SPO ON SPO.ProductionOrderSW = a.ProductionOrder\r\n"
				+ pss.buildInnerJoinViewUSM_SPE("a", 1, "UserStatus")
				+ "WHERE 1 = 1 AND SPO.ProductionOrderSW IS NULL\r\n"
				+ whereCaseTry;

		return createOPA + createOP + insertOP
				+ pss.buildIfTempTableDrop("#tempPrdOPA")
				+ pss.buildIfTempTableDrop("#tempPrdOP");
	}

	/**
	 * OrderPuang+Switch: สร้าง #tempPrdOPSW จาก FromSapMainProdSale ที่ตรง SPO
	 * แล้ว INSERT INTO #tempOPSW โดย filter ตาม whereCaseTry
	 */
	private String buildSummaryOPSWTable(String whereBase, String whereCaseTry) {
		String createOPSW = pss.buildIfTempTableDrop("#tempPrdOPSW")
				+ "SELECT DISTINCT\r\n" + this.selectOPSWA
				+ "INTO #tempPrdOPSW\r\n"
				+ "FROM (\r\n"
				+ "    SELECT DISTINCT\r\n"
				+ "           a.SaleOrder, a.[SaleLine], a.DistChannel, a.Color, a.ColorCustomer\r\n"
				+ "          ,a.SaleQuantity, a.RemainQuantity, a.SaleUnit, a.DueDate\r\n"
				+ "          ,a.CustomerShortName, a.[SaleFullName], a.[SaleNumber], a.SaleCreateDate\r\n"
				+ "          ,a.MaterialNo, a.DeliveryStatus, a.SaleStatus\r\n"
				+ "          ,b.ProductionOrder, a.CustomerName, a.DesignFG, a.OrderAmount\r\n"
				+ "          ,'SUB' AS TypePrdRemark, a.ArticleFG, a.ShipDate, a.Division\r\n"
				+ "          ,a.PurchaseOrder, a.CustomerMaterial, a.Price, a.RemainAmount, a.CustomerDue\r\n"
				+ "          ,CASE WHEN b.Volumn <> 0 THEN b.Volumn ELSE 0 END AS Volumn\r\n"
				+ "          ,a.[PlanGreigeDate]\r\n"
				+ "    FROM #tempMainSale AS a\r\n"
				+ "    INNER JOIN (\r\n"
				+ "        SELECT CASE WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
				+ "                    ELSE C.ProductionOrder END AS [ProductionOrder]\r\n"
				+ "              ,[SaleOrder],[SaleLine],[Volumn],[DataStatus]\r\n"
				+ "        FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
				+ "        LEFT JOIN #tempSPO AS B ON A.[ProductionOrder] = B.ProductionOrder\r\n"
				+ "        LEFT JOIN #tempSPO AS C ON A.[ProductionOrder] = C.[ProductionOrderSW]\r\n"
				+ "        WHERE (B.ProductionOrder IS NOT NULL OR C.ProductionOrder IS NOT NULL)\r\n"
				+ "          AND A.[DataStatus] = 'O'\r\n"
				+ "    ) AS b ON a.SaleOrder = b.SaleOrder AND a.SaleLine = b.SaleLine\r\n"
				+ "    WHERE b.DataStatus = 'O' AND b.SaleLine <> ''\r\n"
				+ ") AS a\r\n"
				+ pss.buildInnerJoinFromSapMainProd("b", "ProductionOrder", "a", "ProductionOrder")
				+ pss.buildInnerJoinViewUSM_SPE("b", 1, "UserStatus")
				+ pss.buildLeftJoinTempProdWorkDate("b")
				+ pss.buildLeftJoinSCC("b")
				+ pss.getLeftJoinTempPlandeliveryDate("b", "a")
				+ " LEFT JOIN #tempSPO AS R ON b.ProductionOrder = R.ProductionOrderSW\r\n"
				+ pss.buildLeftJoinTempSumGR("b")
				+ pss.buildLeftJoinUserStatusAuto("UCAL", "b", "m")
				+ whereBase + " AND 1 = 1\r\n";

		String insertOPSW = "SELECT\r\n" + this.selectAll
				+ "INTO #tempOPSW\r\n"
				+ "FROM #tempPrdOPSW AS a\r\n"
				+ pss.buildInnerJoinViewUSM_SPE("a", 1, "UserStatus")
				+ "WHERE 1 = 1 " + whereCaseTry;

		return createOPSW + insertOPSW + pss.buildIfTempTableDrop("#tempPrdOPSW");
	}

	/**
	 * Switch: สร้าง #tempPrdSW จาก SwitchProdOrder
	 * แล้ว INSERT INTO #tempSW (ไม่มี whereCaseTry เพราะ filter ไว้ใน #tempPrdSW แล้ว)
	 */
	private String buildSummarySWTable(String whereBase) {
		String createSW = pss.buildIfTempTableDrop("#tempPrdSW")
				+ "SELECT DISTINCT\r\n" + this.selectSW
				+ "INTO #tempPrdSW\r\n"
				+ "FROM (\r\n"
				+ "    SELECT DISTINCT\r\n"
				+ "           a.SaleOrder, a.[SaleLine], a.DistChannel, a.Color, a.ColorCustomer\r\n"
				+ "          ,a.SaleQuantity, a.RemainQuantity, a.SaleUnit, a.DueDate\r\n"
				+ "          ,a.CustomerShortName, a.[SaleFullName], a.[SaleNumber], a.SaleCreateDate\r\n"
				+ "          ,a.MaterialNo, a.DeliveryStatus, a.SaleStatus\r\n"
				+ "          ,b.ProductionOrderSW AS ProductionOrder, a.CustomerName, a.DesignFG, a.OrderAmount\r\n"
				+ "          ,a.ArticleFG, a.ShipDate, a.Division, a.PurchaseOrder, a.CustomerMaterial\r\n"
				+ "          ,a.Price, a.RemainAmount, a.CustomerDue\r\n"
				+ "          ,CASE WHEN b.ProductionOrder = b.ProductionOrderSW THEN 'MAIN' ELSE 'SUB' END AS TypePrdRemark\r\n"
				+ "          ,C.SumVol, a.[PlanGreigeDate]\r\n"
				+ "    FROM #tempMainSale AS a\r\n"
				+ "    INNER JOIN [PCMS].[dbo].[SwitchProdOrder] AS b\r\n"
				+ "        ON a.SaleOrder = b.SaleOrderSW AND a.SaleLine = b.SaleLineSW\r\n"
				+ "    LEFT JOIN (\r\n"
				+ "        SELECT PRDORDERSW, SUM([Volumn]) AS SumVol\r\n"
				+ "        FROM (\r\n"
				+ "            SELECT A.[ProductionOrder]\r\n"
				+ "                  ,CASE WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
				+ "                        ELSE C.ProductionOrder END AS PRDORDERSW\r\n"
				+ "                  ,[SaleOrder],[SaleLine],[Volumn],[DataStatus]\r\n"
				+ "            FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
				+ "            LEFT JOIN #tempSPO AS B ON A.[ProductionOrder] = B.ProductionOrder\r\n"
				+ "            LEFT JOIN #tempSPO AS C ON A.[ProductionOrder] = C.[ProductionOrderSW]\r\n"
				+ "            WHERE (B.ProductionOrder IS NOT NULL OR C.ProductionOrder IS NOT NULL)\r\n"
				+ "              AND A.[DataStatus] = 'O'\r\n"
				+ "        ) AS A\r\n"
				+ "        GROUP BY PRDORDERSW\r\n"
				+ "    ) AS C ON b.ProductionOrderSW = C.PRDORDERSW\r\n"
				+ "    WHERE b.DataStatus = 'O'\r\n"
				+ ") AS a\r\n"
				+ pss.buildInnerJoinFromSapMainProd("b", "ProductionOrder", "a", "ProductionOrder")
				+ pss.buildInnerJoinViewUSM_SPE("b", 1, "UserStatus")
				+ pss.buildLeftJoinTempProdWorkDate("b")
				+ pss.buildLeftJoinSCC("b")
				+ pss.getLeftJoinTempPlandeliveryDate("b", "a")
				+ " LEFT JOIN #tempSPO AS R ON b.ProductionOrder = R.ProductionOrderSW\r\n"
				+ pss.buildLeftJoinTempSumGR("b")
				+ pss.buildLeftJoinUserStatusAuto("UCAL", "b", "m")
				+ whereBase + " AND 1 = 1\r\n";

		String insertSW = "SELECT\r\n" + this.selectAll
				+ "INTO #tempSW\r\n"
				+ "FROM #tempPrdSW AS a\r\n";

		return createSW + insertSW + pss.buildIfTempTableDrop("#tempPrdSW");
	}

	/** Replaced: PRD_REPLACED CTE → #tempRP  filter ตาม whereCaseTryRP */
	private String buildSummaryRPTable(String whereCaseTryRP) {
		return pss.buildIfTempTableDrop("#tempRP")
				+ ";WITH PRD_REPLACED AS (\r\n"
				+ "SELECT\r\n" + this.selectRP
				+ "FROM #tempMainSale AS a\r\n"
				+ "INNER JOIN (\r\n"
				+ "    SELECT a.SaleOrder, a.SaleLine\r\n"
				+ "          ,CASE WHEN a.Volume = 0 THEN b.Volumn ELSE a.Volume END AS [Volume]\r\n"
				+ "          ,a.[ProductionOrderRP] AS ProductionOrder\r\n"
				+ "          ,b.TotalQuantity, b.LotNo, b.LabNo, b.LabStatus\r\n"
				+ "          ,b.CFTYPE, b.RemarkOne, b.RemarkTwo, b.RemarkThree, b.[PrdCreateDate]\r\n"
				+ "    FROM [PCMS].[dbo].[ReplacedProdOrder] AS a\r\n"
				+ pss.buildInnerJoinFromSapMainProd("b", "ProductionOrder", "a", "ProductionOrderRP")
				+ pss.buildInnerJoinViewUSM_SPE("b", 1, "UserStatus")
				+ "    WHERE a.[DataStatus] = 'O'\r\n"
				+ ") AS b ON a.SaleOrder = b.SaleOrder AND a.SaleLine = b.SaleLine\r\n"
				+ pss.buildLeftJoinTempProdWorkDate("b")
				+ pss.buildLeftJoinSCC("b")
				+ pss.getLeftJoinTempPlandeliveryDate("b", "a")
				+ " LEFT JOIN #tempSPO AS R ON b.ProductionOrder = R.ProductionOrderSW\r\n"
				+ pss.buildLeftJoinTempSumGR("b")
				+ pss.buildLeftJoinUserStatusAuto("UCALRP", "b", "m")
				+ "WHERE 1 = 1\r\n" + whereCaseTryRP
				+ ")\r\n"
				+ "SELECT\r\n" + this.selectAll
				+ "INTO #tempRP\r\n"
				+ "FROM PRD_REPLACED AS a\r\n";
	}

	/**
	 * Main + WaitLot:
	 *   aggregate filter → #tempWaitLot
	 *   ProdData CTE + PassFilter → #tempMain
	 */
	private String buildSummaryMainAndWaitLot(String whereBase, String whereWaitLot) {
		String insertWaitLot = pss.createTempPrepWaitLot
				+ "SELECT\r\n" + this.selectWaitLot
				+ "INTO #tempWaitLot\r\n"
				+ "FROM #tempMainSale AS a\r\n"
				+ pss.innerJoinWaitLotB
				+ pss.getLeftJoinTempPlandeliveryDate("b", "a")
				+ whereWaitLot
				+ " AND (SumVol = 'B' OR countProdRP > 0)\r\n";

		String fromProdDataB = "FROM (\r\n"
				+ "    SELECT\r\n" + this.leftJoinBSelect
				+ pss.fromProdA
				+ pss.getLeftJoinTempPlandeliveryDate("a", "a")
				+ pss.buildLeftJoinTempProdWorkDate("a")
				+ pss.buildLeftJoinSCC("a")
				+ pss.buildLeftJoinTempSumGR("a")
				+ pss.buildLeftJoinUserStatusAuto("UCAL", "A", "m")
				+ pss.buildLeftJoinViewUserStatusMappingPCMS("UCAL", "UserStatusCal", 0)
				+ pss.getLeftJoinCRP("a")
				+ whereBase.replace("b.", "a.")
				+ ") AS b\r\n";

		String insertMain = pss.withProdData
				+ "SELECT DISTINCT\r\n" + this.selectMainV2
				+ "INTO #tempMain\r\n"
				+ fromProdDataB
				+ pss.getLeftJoinCRP("b")
				+ " LEFT JOIN #tempSPO AS SPO ON SPO.ProductionOrderSW = b.ProductionOrder\r\n"
				+ "WHERE b.PassFilter = 1 AND SPO.ProductionOrderSW IS NULL\r\n";

		return pss.createTempForMainAndWaitLotFiltered
				+ insertWaitLot
				+ insertMain
				+ pss.createDropTempForMainAndWaitLot;
	}

	/** UNION ALL 6 tables + ORDER BY */
	private String buildSummaryFinalSelect() {
		return "SELECT a.* FROM #tempWaitLot AS a\r\n"
				+ "LEFT JOIN #tempMain AS b ON a.SaleOrder = b.SaleOrder AND a.SaleLine = b.SaleLine\r\n"
				+ "WHERE b.SaleOrder IS NULL\r\n"
				+ "UNION ALL SELECT * FROM #tempMain\r\n"
				+ "UNION ALL SELECT * FROM #tempOP\r\n"
				+ "UNION ALL SELECT * FROM #tempOPSW\r\n"
				+ "UNION ALL SELECT * FROM #tempSW\r\n"
				+ "UNION ALL SELECT * FROM #tempRP\r\n"
				+ "ORDER BY CustomerShortName, DueDate, [SaleOrder], [SaleLine], TypePrdRemark, [ProductionOrder]\r\n";
	}

	@Override
	public ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSAllDetail> list = null;
		String where = " where  ";
		String prdOrder = "" ;
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		String prdOrderSafe = (prdOrder == null) ? "" : prdOrder.replace("'", "''");
		where += " a.ProductionOrder = '" + prdOrderSafe + "' \r\n";
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
				+ "SET NOCOUNT ON;\r\n"
				+ this.pss.createTempMainSale
				+ this.pss.createTempPlanDeliveryDate
			 	+ this.pss.createTempSumBill
			 	+ this.pss.createTempSumGR
			 	+ this.pss.createTempSCC
			 	+ this.pss.createTempProdWorkDate
				+ this.pss.createTempForMainAndWaitLot
  		        + this.pss.withProdData
				+  " SELECT distinct top 1  \r\n "
				+ this.selectTwo
				+ fromMainB
				+ this.pss.buildLeftJoinTempProdWorkDate("b")
				+ " Order by SaleOrder , 	SaleLine";
		List<Map<String, Object>> datas = SqlStatementHandler.queryList(this.jdbc, PCMSSqlService.dropAllTemp, sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		if (list.size() > 0) {
			String productionOrder = bean.getProductionOrder();
			ArrayList<PODetail> poDetailList = rfsService.getRollFromSapDetailByProductionOrder(productionOrder) ;
			ArrayList<PackingDetail> packDetailList = fromSapPackingService.getFromSapPackingDetailByProductionOrder(productionOrder);
			ArrayList<ImportDetail> workInLabDetailList = idService.getImportDetailByProductionOrder(prdOrder);
			ArrayList<CFMDetail> cfmDetailList = fromSapCFMService.getFromSapCFMDetailByProductionOrder(productionOrder);
			ArrayList<SaleDetail> saleDetailList = fromSapSaleService.getFromSapSaleDetailByProductionOrder(productionOrder);

			ArrayList<InputDateDetail> submitdatDetailList = fromSapSubmitDateService.getSubmitDateDetail(poList);
			ArrayList<NCDetail> ncDetailList = insNCService.getInspectNcByProductionOrder(prdOrder);


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
			PCMSAllDetail beanTmp = list.get(0);
			beanTmp.setPoDetailList(poDetailList);
			beanTmp.setPresetDetailList(presetDetailList);
			beanTmp.setDyeingDetailList(dyeingDetailList);
			beanTmp.setFinishingDetailList(finDetailList);
			beanTmp.setInspectDetailList(insDetailList);
			beanTmp.setPackingDetailList(packDetailList);

			beanTmp.setWorkInLabDetailList(workInLabDetailList);
			beanTmp.setCfmDetailList(cfmDetailList);
			beanTmp.setSaleDetailList(saleDetailList);
			beanTmp.setSubmitDateDetailList(submitdatDetailList);
			beanTmp.setNcDetailList(ncDetailList);

		}
		return list;
	}
}
