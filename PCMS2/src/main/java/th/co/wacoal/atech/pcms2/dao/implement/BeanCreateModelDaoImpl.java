package th.co.wacoal.atech.pcms2.dao.implement;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.BeanCreateModelDao;
import th.co.wacoal.atech.pcms2.entities.CFMDetail;
import th.co.wacoal.atech.pcms2.entities.ColumnHiddenDetail;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.DyeingDetail;
import th.co.wacoal.atech.pcms2.entities.EmployeeDetail;
import th.co.wacoal.atech.pcms2.entities.FinishingDetail;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.InspectDetail;
import th.co.wacoal.atech.pcms2.entities.NCDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.PODetail;
import th.co.wacoal.atech.pcms2.entities.PackingDetail;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.PresetDetail;
import th.co.wacoal.atech.pcms2.entities.ReceipeDetail;
import th.co.wacoal.atech.pcms2.entities.ReplacedProdOrderDetail;
import th.co.wacoal.atech.pcms2.entities.SORDetail;
import th.co.wacoal.atech.pcms2.entities.SaleDetail;
import th.co.wacoal.atech.pcms2.entities.SaleInputDetail;
import th.co.wacoal.atech.pcms2.entities.SendTestQCDetail;
import th.co.wacoal.atech.pcms2.entities.SubmitDateDetail;
import th.co.wacoal.atech.pcms2.entities.SwitchProdOrderDetail;
import th.co.wacoal.atech.pcms2.entities.TempUserStatusAutoDetail;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.co.wacoal.atech.pcms2.entities.WaitTestDetail;
import th.co.wacoal.atech.pcms2.entities.WorkInLabDetail;
import th.co.wacoal.atech.pcms2.entities.LBMS.ImportDetail;
import th.co.wacoal.atech.pcms2.entities.PPMM.InspectOrdersDetail;
import th.co.wacoal.atech.pcms2.entities.PPMM.ShopFloorControlDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainBillBatchDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPODetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpReceipeDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleInputDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;

@Repository // Spring annotation to mark this as a DAO component
public class BeanCreateModelDaoImpl implements BeanCreateModelDao {
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdfDateTime1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public SimpleDateFormat sdfFullDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm:ss");
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat formatter = new DecimalFormat("###,###,##0.00");

	@Override
	public PCMSTableDetail _genPCMSTableDetail(Map<String, Object> map)
	{
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String DesignFG = "";
		if (map.get("DesignFG") != null) {
			DesignFG = (String) map.get("DesignFG");
		}
		String ArticleFG = "";
		if (map.get("ArticleFG") != null) {
			ArticleFG = (String) map.get("ArticleFG");
		}
		String DistChannel = "";
		if (map.get("DistChannel") != null) {
			DistChannel = (String) map.get("DistChannel");
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}
		String ColorCustomer = "";
		if (map.get("ColorCustomer") != null) {
			ColorCustomer = (String) map.get("ColorCustomer");
		}

//		String RemainQuantity = "";
		Double doubleRemainQ = 0.00;
		if (map.get("RemainQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainQuantity");
			doubleRemainQ = value.doubleValue();
//			RemainQuantity = formatter.format(doubleRemainQ);
		}
		String SaleQuantity = "";
		Double doubleSaleQ = 0.00;
		String BillQuantity = "";
		if (map.get("SaleQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("SaleQuantity");
			doubleSaleQ = value.doubleValue();
			SaleQuantity = formatter.format(doubleSaleQ);

			Double billQ = doubleSaleQ-doubleRemainQ;
			BillQuantity = formatter.format(billQ);
		}
		String SaleUnit = "";
		if (map.get("SaleUnit") != null) {
			SaleUnit = (String) map.get("SaleUnit");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String TotalQuantity = "";
		if (map.get("TotalQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalQuantity");
			Double doubleVal = value.doubleValue();
			TotalQuantity = formatter.format(doubleVal);
		}
//		if (map.get("GreigeInDate") != null) {
//			java.util.Date dateValue = (Date) map.get("GreigeInDate");
//			GreigeInDate = sdf2.format(dateValue);
//		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateValue = (Date) map.get("DueDate");
			DueDate = sdf2.format(dateValue);
		}
		String Prepare = "",Preset = "",DyePlan = "",DyeActual = "",Dryer = "",Finishing = "",Inspectation = "",CFMPlanDate = "",
				CFMDateActual = "",DeliveryDate = "",Relax = "",GreigeInDate = "",LotShipping = "";
//		if (map.get("GreigeInDate") != null) {
//			java.util.Date dateValue = (Date) map.get("GreigeInDate");
//			String dateStr =  sdf2.format(dateValue);
//			GreigeInDate = createDDMM(dateStr);
//		}
//		if (map.get("Prepare") != null) {
//			java.util.Date dateValue = (Date) map.get("Prepare");
//			String dateStr =  sdf2.format(dateValue);
//			Prepare = createDDMM(dateStr);
//		}
//		if (map.get("Relax") != null) {
//			java.util.Date dateValue = (Date) map.get("Relax");
//			String dateStr =  sdf2.format(dateValue);
//			Relax = createDDMM(dateStr);
//		}
//		if (map.get("Preset") != null) {
//			java.util.Date dateValue = (Date) map.get("Preset");
//			String dateStr =  sdf2.format(dateValue);
//			Preset = createDDMM(dateStr);
//		}
//		if (map.get("DyePlan") != null) {
//			java.util.Date dateValue = (Date) map.get("DyePlan");
//			String dateStr =  sdf2.format(dateValue);
//			DyePlan = createDDMM(dateStr);
//		}
//		if (map.get("DyeActual") != null) {
//			java.util.Date dateValue = (Date) map.get("DyeActual");
//			String dateStr =  sdf2.format(dateValue);
//			DyeActual = createDDMM(dateStr);
//		}
//		if (map.get("Dryer") != null) {
//			java.util.Date dateValue = (Date) map.get("Dryer");
//			String dateStr =  sdf2.format(dateValue);
//			Dryer = createDDMM(dateStr);
//		}
//		if (map.get("Finishing") != null) {
//			java.util.Date dateValue = (Date) map.get("Finishing");
//			String dateStr =  sdf2.format(dateValue);
//			Finishing = createDDMM(dateStr);
//		}
//		if (map.get("Inspectation") != null) {
//			java.util.Date dateValue = (Date) map.get("Inspectation");
//			String dateStr =  sdf2.format(dateValue);
//			Inspectation = createDDMM(dateStr);
//		}
//		if (map.get("CFMPlanDate") != null) {
//			java.util.Date dateValue = (Date) map.get("CFMPlanDate");
//			String dateStr =  sdf2.format(dateValue);
//			CFMPlanDate = createDDMM(dateStr);
//		}
//		if (map.get("CFMDateActual") != null) {
//			java.util.Date dateValue = (Date) map.get("CFMDateActual");
//			String dateStr =  sdf2.format(dateValue);
//			CFMDateActual = createDDMM(dateStr);
//		}
//		if (map.get("DeliveryDate") != null) {
//			java.util.Date dateValue = (Date) map.get("DeliveryDate");
//			String dateStr = sdf2.format(dateValue);
//			DeliveryDate = createDDMM(dateStr);
//		}
//		if (map.get("LotShipping") != null) {
//			java.util.Date dateValue = (Date) map.get("LotShipping");
//			String dateStr =  sdf2.format(dateValue);
//			LotShipping = createDDMM(dateStr);
//		}
		if (map.get("GreigeInDate") != null) {
			java.util.Date dateValue = (Date) map.get("GreigeInDate");
			GreigeInDate = sdf2.format(dateValue);
		}
		if (map.get("Prepare") != null) {
			java.util.Date dateValue = (Date) map.get("Prepare");
			Prepare = sdf2.format(dateValue);
		}
		if (map.get("Relax") != null) {
			java.util.Date dateValue = (Date) map.get("Relax");
			Relax = sdf2.format(dateValue);
		}
		if (map.get("Preset") != null) {
			java.util.Date dateValue = (Date) map.get("Preset");
			Preset = sdf2.format(dateValue);
		}
		if (map.get("DyePlan") != null) {
			java.util.Date dateValue = (Date) map.get("DyePlan");
			DyePlan = sdf2.format(dateValue);
		}
		if (map.get("DyeActual") != null) {
			java.util.Date dateValue = (Date) map.get("DyeActual");
			DyeActual = sdf2.format(dateValue);
		}
		if (map.get("Dryer") != null) {
			java.util.Date dateValue = (Date) map.get("Dryer");
			Dryer = sdf2.format(dateValue);
		}
		if (map.get("Finishing") != null) {
			java.util.Date dateValue = (Date) map.get("Finishing");
			Finishing = sdf2.format(dateValue);
		}
		if (map.get("Inspectation") != null) {
			java.util.Date dateValue = (Date) map.get("Inspectation");
			Inspectation = sdf2.format(dateValue);
		}
		if (map.get("CFMPlanDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMPlanDate");
			CFMPlanDate = sdf2.format(dateValue);
		}
		if (map.get("CFMDateActual") != null) {
			java.util.Date dateValue = (Date) map.get("CFMDateActual");
			CFMDateActual = sdf2.format(dateValue);
		}
		if (map.get("DeliveryDate") != null) {
			java.util.Date dateValue = (Date) map.get("DeliveryDate");
			DeliveryDate = sdf2.format(dateValue);
		}
		if (map.get("LotShipping") != null) {
			java.util.Date dateValue = (Date) map.get("LotShipping");
			LotShipping = sdf2.format(dateValue);
		}
		String LabNo = "";
		if (map.get("LabNo") != null) {
			LabNo = (String) map.get("LabNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerShortName = "";
		if (map.get("CustomerShortName") != null) {
			CustomerShortName = (String) map.get("CustomerShortName");
		}
		String SaleNumber = "";
		if (map.get("SaleNumber") != null) {
			SaleNumber = (String) map.get("SaleNumber");
		}
		String SaleFullName = "";
		if (map.get("SaleFullName") != null) {
			SaleFullName = (String) map.get("SaleFullName");
		}
//		SaleFullName = SaleNumber+ ":"+SaleFullName;
		String SaleCreateDate = "";
		if (map.get("SaleCreateDate") != null) {
			java.util.Date dateValue = (Date) map.get("SaleCreateDate");
			SaleCreateDate = sdf2.format(dateValue);
		}
		String PrdCreateDate = "";
		if (map.get("PrdCreateDate") != null) {
			java.util.Date dateValue = (Date) map.get("PrdCreateDate");
			PrdCreateDate = sdf2.format(dateValue);
		}
		String SendCFMCusDate = "";
		if (map.get("SendCFMCusDate") != null) {
			java.util.Date dateValue = (Date) map.get("SendCFMCusDate");
			SendCFMCusDate = sdf2.format(dateValue);
		}
		String PlanGreigeDate = "";
		if (map.get("PlanGreigeDate") != null) {
			java.util.Date dateValue = (Date) map.get("PlanGreigeDate");
			PlanGreigeDate = sdf2.format(dateValue);
		}

		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String DeliveryStatus = "";
		if (map.get("DeliveryStatus") != null) {
			DeliveryStatus = (String) map.get("DeliveryStatus");
		}
		String SaleStatus = "";
		if (map.get("SaleStatus") != null) {
			SaleStatus = (String) map.get("SaleStatus");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String ShipDate = "";
		if (map.get("ShipDate") != null) {
			java.util.Date dateValue = (Date) map.get("ShipDate");
			ShipDate = sdf2.format(dateValue);
		}
		String Division = "";
		if (map.get("Division") != null) {
			java.util.Date dateValue = (Date) map.get("Division");
			ShipDate = sdf2.format(dateValue);
		}
		String TypePrd = "";
		if (map.get("TypePrd") != null) {
			TypePrd = (String) map.get("TypePrd");
		}
		String TypePrdRemark = "";
		if (map.get("TypePrdRemark") != null) {
			TypePrdRemark = (String) map.get("TypePrdRemark");
		}
//		String SendCFMCusDate   = "";
//		if (map.get("SendCFMCusDate") != null) {
//			java.util.Date dateValue = (Date) map.get("SendCFMCusDate");
//			String dateStr = sdf2.format(dateValue);
//			SendCFMCusDate = createDDMM(dateStr);
//		}
		String PurchaseOrder = "";
		if (map.get("PurchaseOrder") != null) {
			PurchaseOrder = (String) map.get("PurchaseOrder");
		}
		String CustomerDivision = "";
		if (map.get("CustomerDivision") != null) {
			CustomerDivision = (String) map.get("CustomerDivision");
		}
//		String SendCFMCusDate   = "";
//		if (map.get("SendCFMCusDate") != null) {
//			java.util.Date dateValue = (Date) map.get("SendCFMCusDate");
//			String dateStr = sdf2.format(dateValue);
//			SendCFMCusDate = createDDMM(dateStr);
//		}
//		String PlanGreigeDate = "";
//		if (map.get("PlanGreigeDate") != null) {
//			java.util.Date dateValue = (Date) map.get("PlanGreigeDate");
//			String dateStr =  sdf2.format(dateValue);
//			PlanGreigeDate = createDDMM(dateStr);
//		}
		String cfmDetailAll = "";
		if (map.get("CFMDetailAll") != null) {
			cfmDetailAll = (String) map.get("CFMDetailAll");
		}
		String RollNoRemarkAll = "";
		if (map.get("RollNoRemarkAll") != null) {
			RollNoRemarkAll = (String) map.get("RollNoRemarkAll");
		}
		String DyeStatus = "";
		if (map.get("DyeStatus") != null) {
			DyeStatus = (String) map.get("DyeStatus");
		}
		return new PCMSTableDetail(SaleOrder, SaleLine, DesignFG, ArticleFG, DistChannel, Color, ColorCustomer, SaleQuantity,
				BillQuantity, SaleUnit, ProductionOrder, TotalQuantity, GreigeInDate, UserStatus, LabStatus, DueDate, Prepare,
				Preset, DyePlan, DyeActual, Dryer, Finishing, Inspectation, CFMPlanDate, CFMDateActual, DeliveryDate, LotShipping,
				LabNo, CustomerShortName, SaleNumber, SaleFullName, SaleCreateDate, PrdCreateDate, MaterialNo, DeliveryStatus,
				SaleStatus, LotNo, ShipDate, Relax, CustomerName, Division, DyeStatus, TypePrd, TypePrdRemark, SendCFMCusDate,
				PurchaseOrder, CustomerDivision, PlanGreigeDate, cfmDetailAll, RollNoRemarkAll);
	}

	public String createDDMM(String dateStr)
	{
		String[] x = dateStr.split("/");
		String dd = x[0].replaceFirst("^0+(?!$)", "");
		String mm = x[1].replaceFirst("^0+(?!$)", "");
		dateStr = dd + "/" + mm;
		return dateStr;
	}

	@Override
	public PCMSTableDetail _genSearchTableDetail(Map<String, Object> map)
	{
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String DesignFG = "";
		if (map.get("DesignFG") != null) {
			DesignFG = (String) map.get("DesignFG");
		}
		String ArticleFG = "";
		if (map.get("ArticleFG") != null) {
			ArticleFG = (String) map.get("ArticleFG");
		}
		String DistChannel = "";
		if (map.get("DistChannel") != null) {
			DistChannel = (String) map.get("DistChannel");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			DueDate = (String) map.get("DueDate");
		}
		String LabNo = "";
		if (map.get("LabNo") != null) {
			LabNo = (String) map.get("LabNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerShortName = "";
		if (map.get("CustomerShortName") != null) {
			CustomerShortName = (String) map.get("CustomerShortName");
		}
		String SaleNumber = "";
		if (map.get("SaleNumber") != null) {
			SaleNumber = (String) map.get("SaleNumber");
		}
		String SaleCreateDate = "";
		if (map.get("SaleCreateDate") != null) {
			SaleCreateDate = (String) map.get("SaleCreateDate");
		}
		String PrdCreateDate = "";
		if (map.get("PrdCreateDate") != null) {
			PrdCreateDate = (String) map.get("PrdCreateDate");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String DeliveryStatus = "";
		if (map.get("DeliveryStatus") != null) {
			DeliveryStatus = (String) map.get("DeliveryStatus");
		}
		String SaleStatus = "";
		if (map.get("SaleStatus") != null) {
			SaleStatus = (String) map.get("SaleStatus");
		}
		int No = 0;
		if (map.get("No") != null) {
			No = (int) map.get("No");
		}
		String UserId = "";
		if (map.get("UserId") != null) {
			UserId = (String) map.get("UserId");
		}
		String Division = "";
		if (map.get("Division") != null) {
			Division = (String) map.get("Division");
		}
		String PurchaseOrder = "";
		if (map.get("PurchaseOrder") != null) {
			PurchaseOrder = (String) map.get("PurchaseOrder");
		}
		return new PCMSTableDetail(SaleOrder, DesignFG, ArticleFG, DistChannel, ProductionOrder, UserStatus, DueDate, LabNo,
				CustomerShortName, SaleNumber, SaleCreateDate, PrdCreateDate, MaterialNo, DeliveryStatus, SaleStatus,
				CustomerName, No, UserId, Division, PurchaseOrder);
	}

	@Override
	public PCMSAllDetail _genPCMSAllDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String Batch = "";
		if (map.get("Batch") != null) {
			Batch = (String) map.get("Batch");
		}
		String LabNo = "";
		if (map.get("LabNo") != null) {
			LabNo = (String) map.get("LabNo");
		}
		String PrdCreateDate = "";
		if (map.get("PrdCreateDate") != null) {
			java.util.Date dateValue = (Date) map.get("PrdCreateDate");
			PrdCreateDate = sdf2.format(dateValue);
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateValue = (Date) map.get("DueDate");
			DueDate = sdf2.format(dateValue);
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String PurchaseOrder = "";
		if (map.get("PurchaseOrder") != null) {
			PurchaseOrder = (String) map.get("PurchaseOrder");
		}
		String ArticleFG = "";
		if (map.get("ArticleFG") != null) {
			ArticleFG = (String) map.get("ArticleFG");
		}
		String DesignFG = "";
		if (map.get("DesignFG") != null) {
			DesignFG = (String) map.get("DesignFG");
		}

		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerShortName = "";
		if (map.get("CustomerShortName") != null) {
			CustomerShortName = (String) map.get("CustomerShortName");
		}
		String Shade = "";
		if (map.get("Shade") != null) {
			Shade = (String) map.get("Shade");
		}
		String BookNo = "";
		if (map.get("BookNo") != null) {
			BookNo = (String) map.get("BookNo");
		}
		String Center = "";
		if (map.get("Center") != null) {
			Center = (String) map.get("Center");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String Volumn = "";
		if (map.get("Volumn") != null) {
			BigDecimal value = (BigDecimal) map.get("Volumn");
			Double doubleVal = value.doubleValue();
			Volumn = formatter.format(doubleVal);
		}
		String SaleUnit = "";
		if (map.get("SaleUnit") != null) {
			SaleUnit = (String) map.get("SaleUnit");
		}
		String STDUnit = "";
		if (map.get("STDUnit") != null) {
			STDUnit = (String) map.get("STDUnit");
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}
		String PlanGreigeDate = "";
		if (map.get("PlanGreigeDate") != null) {
			java.util.Date dateValue = (Date) map.get("PlanGreigeDate");
			PlanGreigeDate = sdf2.format(dateValue);
		}
		String RefPrd = "";
		if (map.get("RefPrd") != null) {
			RefPrd = (String) map.get("RefPrd");
		}
		String GreigeInDate = "";
		if (map.get("GreigeInDate") != null) {
			java.util.Date dateValue = (Date) map.get("GreigeInDate");
			GreigeInDate = sdf2.format(dateValue);
		}
		String BCAware = "";
		if (map.get("BCAware") != null) {
			BCAware = (String) map.get("BCAware");
		}
		String OrderPuang = "";
		if (map.get("OrderPuang") != null) {
			OrderPuang = (String) map.get("OrderPuang");
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String CFMPlanDate = "";
		if (map.get("CFMPlanDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMPlanDate");
			CFMPlanDate = sdf2.format(dateValue);
		}
		String DeliveryDate = "";
		if (map.get("DeliveryDate") != null) {
			java.util.Date dateValue = (Date) map.get("DeliveryDate");
			DeliveryDate = sdf2.format(dateValue);
		}
//		String CFMPlanDate = "";
//		if (map.get("CFMPlanDate") != null) {
//			java.util.Date dateValue = (Date) map.get("CFMPlanDate");
//			CFMPlanDate = sdf2.format(dateValue);
//			}
//		String DeliveryDate = "";
//		if (map.get("DeliveryDate") != null) {
//			java.util.Date dateValue = (Date) map.get("DeliveryDate");
//			DeliveryDate = sdf2.format(dateValue);
//			}
		String BCDate = "";
		if (map.get("BCDate") != null) {
			java.util.Date dateValue = (Date) map.get("BCDate");
			BCDate = sdf2.format(dateValue);
		}
		String RemarkOne = "";
		if (map.get("RemarkOne") != null) {
			RemarkOne = (String) map.get("RemarkOne");
		}
		String RemarkTwo = "";
		if (map.get("RemarkTwo") != null) {
			RemarkTwo = (String) map.get("RemarkTwo");
		}
		String RemarkThree = "";
		if (map.get("RemarkThree") != null) {
			RemarkThree = (String) map.get("RemarkThree");
		}
		String RemAfterCloseOne = "";
		if (map.get("RemAfterCloseOne") != null) {
			RemAfterCloseOne = (String) map.get("RemAfterCloseOne");
		}
		String RemAfterCloseTwo = "";
		if (map.get("RemAfterCloseTwo") != null) {
			RemAfterCloseTwo = (String) map.get("RemAfterCloseTwo");
		}
		String RemAfterCloseThree = "";
		if (map.get("RemAfterCloseThree") != null) {
			RemAfterCloseThree = (String) map.get("RemAfterCloseThree");
		}
		String GreigeArticle = "";
		if (map.get("GreigeArticle") != null) {
			GreigeArticle = (String) map.get("GreigeArticle");
		}
		String GreigeDesign = "";
		if (map.get("GreigeDesign") != null) {
			GreigeDesign = (String) map.get("GreigeDesign");
		}
		String ColorCustomer = "";
		if (map.get("ColorCustomer") != null) {
			ColorCustomer = (String) map.get("ColorCustomer");
		}
		return new PCMSAllDetail(ProductionOrder, LotNo, Batch, LabNo, PrdCreateDate, DueDate, SaleOrder, SaleLine, PurchaseOrder,
				ArticleFG, DesignFG, CustomerName, CustomerShortName, Shade, BookNo, Center, MaterialNo, Volumn, SaleUnit,
				STDUnit, Color, PlanGreigeDate, RefPrd, GreigeInDate, BCAware, OrderPuang, UserStatus, LabStatus, CFMPlanDate,
				DeliveryDate, BCDate, RemarkOne, RemarkTwo, RemarkThree, RemAfterCloseOne, RemAfterCloseTwo, RemAfterCloseThree,
				GreigeArticle, GreigeDesign, ColorCustomer);
	}

	@Override
	public PODetail _genPODetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String RollNo = "";
		if (map.get("RollNo") != null) {
			RollNo = (String) map.get("RollNo");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			Double doubleVal = value.doubleValue();
			QuantityKG = formatter.format(doubleVal);
		}
		String QuantityMR = "";
		if (map.get("QuantityMR") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityMR");
			Double doubleVal = value.doubleValue();
			QuantityMR = formatter.format(doubleVal);
		}
		String POCreatedate = "";
		if (map.get("POCreatedate") != null) {
			java.util.Date dateValue = (Date) map.get("POCreatedate");
			POCreatedate = sdf2.format(dateValue);
		}

		String PurchaseOrder = "";
		if (map.get("PurchaseOrder") != null) {
			PurchaseOrder = (String) map.get("PurchaseOrder");
		}
		String PurchaseOrderLine = "";
		if (map.get("PurchaseOrderLine") != null) {
			PurchaseOrderLine = (String) map.get("PurchaseOrderLine");
		}
		String RequiredDate = "";
		if (map.get("RequiredDate") != null) {
			java.util.Date dateValue = (Date) map.get("RequiredDate");
			RequiredDate = sdf2.format(dateValue);
		}
		String PODefault = "";
		if (map.get("PODefault") != null) {
			PODefault = (String) map.get("PODefault");
		}
		String POLineDefault = "";
		if (map.get("POLineDefault") != null) {
			POLineDefault = (String) map.get("POLineDefault");
		}
		String POPostingDateDefault = "";
		if (map.get("POPostingDateDefault") != null) {
			java.util.Date dateValue = (Date) map.get("POPostingDateDefault");
			POPostingDateDefault = sdf2.format(dateValue);
		}
		return new PODetail(ProductionOrder, PurchaseOrder, PurchaseOrderLine, POCreatedate, RequiredDate, RollNo, QuantityKG,
				QuantityMR, PODefault, POLineDefault, POPostingDateDefault);
	}

	@Override
	public PresetDetail _genPresetDetail(Map<String, Object> map)
	{
		String PostingDate = "";
		if (map.get("PostingDate") != null) {
			java.util.Date dateValue = (Date) map.get("PostingDate");
			PostingDate = sdf2.format(dateValue);
		}
		String WorkCenter = "";
		if (map.get("WorkCenter") != null) {
			WorkCenter = (String) map.get("WorkCenter");
		}
		return new PresetDetail(PostingDate, WorkCenter);
	}

	@Override
	public DyeingDetail _genDyeingDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder ") != null) {
			ProductionOrder = (String) map.get("ProductionOrder ");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String Operation = "";
		if (map.get("Operation") != null) {
			int value = (int) map.get("Operation");
			Operation = Integer.toString(value);
		}
		String WorkDate = "";
		if (map.get("WorkDate") != null) {
			java.util.Date dateValue = (Date) map.get("WorkDate");
			WorkDate = sdf2.format(dateValue);
		}
		String WorkCenter = "";
		if (map.get("WorkCenter") != null) {
			WorkCenter = (String) map.get("WorkCenter");
		}
		String CartNo = "";
		if (map.get("CartNo") != null) {
			CartNo = (String) map.get("CartNo");
		}
		String CartType = "";
		if (map.get("CartType") != null) {
			CartType = (String) map.get("CartType");
		}
		String DyeingStatus = "";
		if (map.get("DyeingStatus") != null) {
			DyeingStatus = (String) map.get("DyeingStatus");
		}
		String Da = "";
		if (map.get("Da") != null) {
			Da = (String) map.get("Da");
		}
		String Db = "";
		if (map.get("Db") != null) {
			Db = (String) map.get("Db");
		}
		String ST = "";
		if (map.get("ST") != null) {
			ST = (String) map.get("ST");
		}
		String L = "";
		if (map.get("L") != null) {
			L = (String) map.get("L");
		}
		String ValDeltaE = "";
		if (map.get("ValDeltaE") != null) {
			ValDeltaE = (String) map.get("ValDeltaE");
		}
		String DyeRemark = "";
		if (map.get("DyeRemark") != null) {
			DyeRemark = (String) map.get("DyeRemark");
		}
		String ColorCheckStatus = "";
		if (map.get("ColorCheckStatus") != null) {
			ColorCheckStatus = (String) map.get("ColorCheckStatus");
		}
		String ColorCheckRemark = "";
		if (map.get("ColorCheckRemark") != null) {
			ColorCheckRemark = (String) map.get("ColorCheckRemark");
		}
		return new DyeingDetail(ProductionOrder, LotNo, Operation, CartNo, CartType, WorkDate, WorkCenter, DyeingStatus,
				ValDeltaE, L, Da, Db, ST, DyeRemark, ColorCheckStatus, ColorCheckRemark);
	}

	@Override
	public SendTestQCDetail _genSendTestQCDetail(Map<String, Object> map)
	{
		String SendDate = "";
		if (map.get("SendDate") != null) {
			java.util.Date dateValue = (Date) map.get("SendDate");
			SendDate = sdf2.format(dateValue);
		}
		String Status = "";
		if (map.get("Status") != null) {
			Status = (String) map.get("Status");
		}
		String RollNo = "";
		if (map.get("RollNo") != null) {
			RollNo = (String) map.get("RollNo");
		}
		String CheckColorDate = "";
		if (map.get("CheckColorDate") != null) {
			java.util.Date dateValue = (Date) map.get("CheckColorDate");
			CheckColorDate = sdf2.format(dateValue);
		}
		String DeltaE = "";
		if (map.get("DeltaE") != null) {
			DeltaE = (String) map.get("DeltaE");
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}
		return new SendTestQCDetail(SendDate, RollNo, Status, CheckColorDate, DeltaE, CheckColorDate, Color);
	}

	@Override
	public InspectDetail _genInspectDetail(Map<String, Object> map)
	{
		String PostingDate = "";
		if (map.get("PostingDate") != null) {
			java.util.Date dateValue = (Date) map.get("PostingDate");
			PostingDate = sdf2.format(dateValue);
		}
		String QuantityGreige = "";
		if (map.get("QuantityGreige") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityGreige");
			Double doubleVal = value.doubleValue();
			QuantityGreige = formatter.format(doubleVal);
		}
		String Operation = "";
		if (map.get("Operation") != null) {
			Operation = (String) map.get("Operation");
		}
		String QuantityFG = "";
		if (map.get("QuantityFG") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityFG");
			Double doubleVal = value.doubleValue();
			QuantityFG = formatter.format(doubleVal);
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		return new InspectDetail(PostingDate, QuantityGreige, Operation, QuantityFG, Remark);
	}

	@Override
	public FinishingDetail _genFinishingDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder ") != null) {
			ProductionOrder = (String) map.get("ProductionOrder ");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String Operation = "";
		if (map.get("Operation") != null) {
			int value = (int) map.get("Operation");
			Operation = Integer.toString(value);
		}
		String WorkDate = "";
		if (map.get("WorkDate") != null) {
			java.util.Date dateValue = (Date) map.get("WorkDate");
			WorkDate = sdf2.format(dateValue);
		}
		String WorkCenter = "";
		if (map.get("WorkCenter") != null) {
			WorkCenter = (String) map.get("WorkCenter");
		}
		String CartNo = "";
		if (map.get("CartNo") != null) {
			CartNo = (String) map.get("CartNo");
		}
		String CartType = "";
		if (map.get("CartType") != null) {
			CartType = (String) map.get("CartType");
		}
		String ColorCheckOperation = "";
		if (map.get("ColorCheckOperation") != null) {
			int value = (int) map.get("ColorCheckOperation");
			ColorCheckOperation = Integer.toString(value);
		}
		String ColorCheckWorkDate = "";
		if (map.get("ColorCheckWorkDate") != null) {
			java.util.Date dateValue = (Date) map.get("ColorCheckWorkDate");
			ColorCheckWorkDate = sdf2.format(dateValue);
		}
		String Da = "";
		if (map.get("Da") != null) {
			Da = (String) map.get("Da");
		}
		String Db = "";
		if (map.get("Db") != null) {
			Db = (String) map.get("Db");
		}
		String ST = "";
		if (map.get("ST") != null) {
			ST = (String) map.get("ST");
		}
		String L = "";
		if (map.get("L") != null) {
			L = (String) map.get("L");
		}
		String ValDeltaE = "";
		if (map.get("ValDeltaE") != null) {
			ValDeltaE = (String) map.get("ValDeltaE");
		}
		String ColorCheckName = "";
		if (map.get("ColorCheckName") != null) {
			ColorCheckName = (String) map.get("ColorCheckName");
		}
		String ColorCheckStatus = "";
		if (map.get("ColorCheckStatus") != null) {
			ColorCheckStatus = (String) map.get("ColorCheckStatus");
		}
		String ColorCheckRollNo = "";
		if (map.get("ColorCheckRollNo") != null) {
			ColorCheckRollNo = (String) map.get("ColorCheckRollNo");
		}
		String ColorCheckRemark = "";
		if (map.get("ColorCheckRemark") != null) {
			ColorCheckRemark = (String) map.get("ColorCheckRemark");
		}

		return new FinishingDetail(ProductionOrder, LotNo, Operation, WorkDate, WorkCenter, CartNo, CartType, ColorCheckOperation,
				ColorCheckWorkDate, Da, Db, L, ST, ValDeltaE, ColorCheckName, ColorCheckStatus, ColorCheckRollNo,
				ColorCheckRemark);
	}

	@Override
	public PackingDetail _genPackingDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
		String PostingDate = "";
		if (map.get("PostingDate") != null) {
			java.util.Date dateValue = (Date) map.get("PostingDate");
			PostingDate = sdf2.format(dateValue);
		}
		String Quantity = "";
		if (map.get("Quantity") != null) {
			BigDecimal value = (BigDecimal) map.get("Quantity");
			Double doubleVal = value.doubleValue();
			Quantity = formatter.format(doubleVal);
		}
		String RollNo = "";
		if (map.get("RollNo") != null) {
			RollNo = (String) map.get("RollNo");
		}
		String Status = "";
		if (map.get("Status") != null) {
			Status = (String) map.get("Status");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			Double doubleVal = value.doubleValue();
			QuantityKG = formatter.format(doubleVal);
		}
		String Grade = "";
		if (map.get("Grade") != null) {
			Grade = (String) map.get("Grade");
		}
		String QuantityYD = "";
		if (map.get("QuantityYD") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityYD");
			Double doubleVal = value.doubleValue();
			QuantityYD = formatter.format(doubleVal);
		}
		return new PackingDetail(PostingDate, Quantity, RollNo, Status, QuantityKG, Grade, QuantityYD);
	}

	@Override
	public WorkInLabDetail _genWorkInLabDetail(Map<String, Object> map)
	{
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}
		String SendDate = "";
		if (map.get("SendDate") != null) {
			java.util.Date dateValue = (Date) map.get("SendDate");
			SendDate = sdf2.format(dateValue);
		}
		String NOK = "";
		if (map.get("NOK") != null) {
			NOK = (String) map.get("NOK");
		}
		String NCDate = "";
		if (map.get("NCDate") != null) {
			java.util.Date dateValue = (Date) map.get("NCDate");
			NCDate = sdf2.format(dateValue);
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String ReceiveDate = "";
		if (map.get("ReceiveDate") != null) {
			java.util.Date dateValue = (Date) map.get("ReceiveDate");
			ReceiveDate = sdf2.format(dateValue);
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String Da = "";
		if (map.get("Da") != null) {
			Da = (String) map.get("Da");
		}
		String Db = "";
		if (map.get("Db") != null) {
			Db = (String) map.get("Db");
		}
		String L = "";
		if (map.get("L") != null) {
			L = (String) map.get("L");
		}
		String ST = "";
		if (map.get("ST") != null) {
			ST = (String) map.get("ST");
		}
		// TODO Auto-generated method stub
		return new WorkInLabDetail(No, SendDate, NOK, NCDate, LotNo, ReceiveDate, Remark, Da, Db, L, ST);
	}

	@Override
	public WaitTestDetail _genWaitTestDetail(Map<String, Object> map)
	{
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}
		String DateInTest = "";
		if (map.get("DateInTest") != null) {
			java.util.Date dateValue = (Date) map.get("DateInTest");
			DateInTest = sdf2.format(dateValue);
		}
		String DateOutTest = "";
		if (map.get("DateOutTest") != null) {
			java.util.Date dateValue = (Date) map.get("DateOutTest");
			DateOutTest = sdf2.format(dateValue);
		}
		String Status = "";
		if (map.get("Status") != null) {
			Status = (String) map.get("Status");
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		return new WaitTestDetail(No, DateInTest, DateOutTest, Status, Remark);
	}

	@Override
	public CFMDetail _genCFMDetail(Map<String, Object> map)
	{
		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String CFMNo = "";
		if (map.get("CFMNo") != null) {
			CFMNo = (String) map.get("CFMNo");
		}
		String CFMNumber = "";
		if (map.get("CFMNumber") != null) {
			CFMNumber = (String) map.get("CFMNumber");
		}
		String CFMSendDate = "";
		if (map.get("CFMSendDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMSendDate");
			CFMSendDate = sdf2.format(dateValue);
		}
		String RollNo = "";
		if (map.get("RollNo") != null) {
			RollNo = (String) map.get("RollNo");
		}
		String RollNoRemark = "";
		if (map.get("RollNoRemark") != null) {
			RollNoRemark = (String) map.get("RollNoRemark");
		}
		String L = "";
		if (map.get("L") != null) {
			L = (String) map.get("L");
		}
		String Da = "";
		if (map.get("Da") != null) {
			Da = (String) map.get("Da");
		}
		String Db = "";
		if (map.get("Db") != null) {
			Db = (String) map.get("Db");
		}

		String ST = "";
		if (map.get("ST") != null) {
			ST = (String) map.get("ST");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}
		String CFMAnswerDate = "";
		if (map.get("CFMAnswerDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMAnswerDate");
			CFMAnswerDate = sdf2.format(dateValue);
		}
		String CFMStatus = "";
		if (map.get("CFMStatus") != null) {
			CFMStatus = (String) map.get("CFMStatus");
		}
		String CFMRemark = "";
		if (map.get("CFMRemark") != null) {
			CFMRemark = (String) map.get("CFMRemark");
		}
		String NextLot = "";
		if (map.get("NextLot") != null) {
			NextLot = (String) map.get("NextLot");
		}
		String SOChange = "";
		if (map.get("SOChange") != null) {
			SOChange = (String) map.get("SOChange");
		}
		String SOChangeQty = "";
		if (map.get("SOChangeQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SOChangeQty");
			Double doubleVal = value.doubleValue();
			SOChangeQty = formatter.format(doubleVal);
		}
		String SOChangeUnit = "";
		if (map.get("SOChangeUnit") != null) {
			SOChangeUnit = (String) map.get("SOChangeUnit");
		}
		String DE = "";
		if (map.get("DE") != null) {
			DE = (String) map.get("DE");
		}

		// TODO Auto-generated method stub
		return new CFMDetail(Id, ProductionOrder, CFMNo, CFMNumber, CFMSendDate, RollNo, RollNoRemark, L, Da, Db, ST, SaleOrder,
				SaleLine, Color, CFMAnswerDate, CFMStatus, CFMRemark, NextLot, SOChange, SOChangeQty, SOChangeUnit, DE);
	}

	@Override
	public SaleDetail _genSaleDetail(Map<String, Object> map)
	{
		String BillDate = "";
		if (map.get("BillDate") != null) {
			java.util.Date dateValue = (Date) map.get("BillDate");
			BillDate = sdf2.format(dateValue);
		}
		String BillQtyPerSale = "";
		if (map.get("BillQtyPerSale") != null) {
			BigDecimal value = (BigDecimal) map.get("BillQtyPerSale");
			Double doubleVal = value.doubleValue();
			BillQtyPerSale = formatter.format(doubleVal);
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String BillQtyPerStock = "";
		if (map.get("Quantity") != null) {
			BigDecimal value = (BigDecimal) map.get("BillQtyPerStock");
			Double doubleVal = value.doubleValue();
			BillQtyPerStock = formatter.format(doubleVal);
		}
		return new SaleDetail(BillDate, BillQtyPerSale, SaleOrder, SaleLine, BillQtyPerStock);
	}

	@Override
	public SaleInputDetail _genSaleInputDetail(Map<String, Object> map)
	{
		String BillDate = "";
		if (map.get("BillDate") != null) {
			java.util.Date dateValue = (Date) map.get("BillDate");
			BillDate = sdf2.format(dateValue);
		}
		String BillQtyPerSale = "";
		if (map.get("BillQtyPerSale") != null) {
			BigDecimal value = (BigDecimal) map.get("BillQtyPerSale");
			Double doubleVal = value.doubleValue();
			BillQtyPerSale = formatter.format(doubleVal);
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String BillQtyPerStock = "";
		if (map.get("Quantity") != null) {
			BigDecimal value = (BigDecimal) map.get("BillQtyPerStock");
			Double doubleVal = value.doubleValue();
			BillQtyPerStock = formatter.format(doubleVal);
		}
		return new SaleInputDetail(BillDate, BillQtyPerSale, SaleOrder, SaleLine, BillQtyPerStock);
	}

	@Override
	public SubmitDateDetail _genSubmitDateDetail(Map<String, Object> map)
	{
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}
		String SubmitDate = "";
		if (map.get("SubmitDate") != null) {
			java.util.Date dateValue = (Date) map.get("SubmitDate");
			SubmitDate = sdf2.format(dateValue);
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		return new SubmitDateDetail(No, SubmitDate, Remark);
	}

	@Override
	public NCDetail _genNCDetail(Map<String, Object> map)
	{
		String productionOrder = "";
		if (map.get("PrdNumber") != null) {
			productionOrder = (String) map.get("PrdNumber");
		}
		String No = "";
		if (map.get("No") != null) {
			int value = (int) map.get("No");
			No = Integer.toString(value);
		}
		String NCDate = "";
		if (map.get("NcDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("NcDate");
			NCDate = this.sdfDateTime1.format(timestamp1);
		}
		String NcLength = "";
		if (map.get("NcLength") != null) {
			double value = (double) map.get("NcLength");
//			BigDecimal value = (BigDecimal) map.get("NcLength");
//			Double doubleVal = value.doubleValue();
			NcLength = formatter.format(value);
		}
		String NcReceiverBase = "";
		if (map.get("NcReceiverBase") != null) {
			NcReceiverBase = (String) map.get("NcReceiverBase");
		}
		String NcCarNumber = "";
		if (map.get("NcCarNumber") != null) {
			NcCarNumber = (String) map.get("NcCarNumber");
		}
		String NcProblem = "";
		if (map.get("NcProblem") != null) {
			NcProblem = (String) map.get("NcProblem");
		}
		String NcSolution = "";
		if (map.get("NcSolution") != null) {
			NcSolution = (String) map.get("NcSolution");
		}
		return new NCDetail(productionOrder, No, NCDate, NcLength, NcReceiverBase, NcCarNumber, NcProblem, NcSolution);
	}

	@Override
	public ReceipeDetail _genReceipeDetail(Map<String, Object> map)
	{
		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String PostingDate = "";
		if (map.get("PostingDate") != null) {
			java.util.Date dateValue = (Date) map.get("PostingDate");
			PostingDate = sdf2.format(dateValue);
		}
		String Receipe = "";
		if (map.get("Receipe") != null) {
			Receipe = (String) map.get("Receipe");
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdfDateTime1.format(timestamp1);
		}
		String CreateBy = "";
		if (map.get("CreateBy") != null) {
			CreateBy = (String) map.get("CreateBy");
		}
		String CreateDate = "";
		if (map.get("CreateDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("CreateDate");
			CreateDate = this.sdfDateTime1.format(timestamp1);
		}
		return new ReceipeDetail(Id, No, LotNo, PostingDate, Receipe, ChangeDate, ChangeBy, CreateDate, CreateBy);
	}

	@Override
	public PCMSSecondTableDetail _genPCMSSecondTableDetail(Map<String, Object> map)
	{
		String Division = "";
		if (map.get("Division") != null) {
			Division = (String) map.get("Division");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String CustomerShortName = "";
		if (map.get("CustomerShortName") != null) {
			CustomerShortName = (String) map.get("CustomerShortName");
		}
		String SaleCreateDate = "";
		if (map.get("SaleCreateDate") != null) {
			java.util.Date dateValue = (Date) map.get("SaleCreateDate");
			SaleCreateDate = sdf2.format(dateValue);
		}
		String PurchaseOrder = "";
		if (map.get("PurchaseOrder") != null) {
			PurchaseOrder = (String) map.get("PurchaseOrder");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String CustomerMaterial = "";
		if (map.get("CustomerMaterial") != null) {
			CustomerMaterial = (String) map.get("CustomerMaterial");
		}
		String CustomerMaterialBase = "";
		if (map.get("CustomerMaterialBase") != null) {
			CustomerMaterialBase = (String) map.get("CustomerMaterialBase");
		}
		String Price = "";
		if (map.get("Price") != null) {
			BigDecimal value = (BigDecimal) map.get("Price");
			Double doubleVal = value.doubleValue();
			Price = formatter.format(doubleVal);
		}
		String SaleUnit = "";
		if (map.get("SaleUnit") != null) {
			SaleUnit = (String) map.get("SaleUnit");
		}

		String OrderAmount = "";
		if (map.get("OrderAmount") != null) {
			BigDecimal value = (BigDecimal) map.get("OrderAmount");
			Double doubleVal = value.doubleValue();
			OrderAmount = formatter.format(doubleVal);
		}
		String SaleQuantity = "";
		if (map.get("SaleQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("SaleQuantity");
			Double doubleVal = value.doubleValue();
			SaleQuantity = formatter.format(doubleVal);
		}
		String RemainQuantity = "";
		if (map.get("RemainQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainQuantity");
			Double doubleVal = value.doubleValue();
			RemainQuantity = formatter.format(doubleVal);
		}
		String RemainAmount = "";
		if (map.get("RemainAmount") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainAmount");
			Double doubleVal = value.doubleValue();
			RemainAmount = formatter.format(doubleVal);
		}
		String TotalQuantity = "";
		if (map.get("TotalQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalQuantity");
			Double doubleVal = value.doubleValue();
			TotalQuantity = formatter.format(doubleVal);
		}
		String Grade = "";
		if (map.get("Grade") != null) {
			Grade = (String) map.get("Grade");
		}
		String BillSendWeightQuantity = "";
		if (map.get("BillSendWeightQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("BillSendWeightQuantity");
			Double doubleVal = value.doubleValue();
			BillSendWeightQuantity = formatter.format(doubleVal);
		}
		String BillSendMRQuantity = "";
		if (map.get("BillSendMRQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("BillSendMRQuantity");
			Double doubleVal = value.doubleValue();
			BillSendMRQuantity = formatter.format(doubleVal);
		}
		String BillSendYDQuantity = "";
		if (map.get("BillSendYDQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("BillSendYDQuantity");
			Double doubleVal = value.doubleValue();
			BillSendYDQuantity = formatter.format(doubleVal);
		}
		String BillSendQuantity = "";
		if (map.get("BillSendQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("BillSendQuantity");
			Double doubleVal = value.doubleValue();
			BillSendQuantity = formatter.format(doubleVal);
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			CustomerDue = (String) map.get("CustomerDue");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateValue = (Date) map.get("DueDate");
			DueDate = sdf2.format(dateValue);
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String LabNo = "";
		if (map.get("LabNo") != null) {
			LabNo = (String) map.get("LabNo");
		}

		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String CFMPlanLabDate = "";
		if (map.get("CFMPlanLabDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMPlanLabDate");
			CFMPlanLabDate = sdf2.format(dateValue);
		}
		String CFMActualLabDate = "";
		if (map.get("CFMActualLabDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMActualLabDate");
			CFMActualLabDate = sdf2.format(dateValue);
		}
		String CFMCusAnsLabDate = "";
		if (map.get("CFMCusAnsLabDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMCusAnsLabDate");
			CFMCusAnsLabDate = sdf2.format(dateValue);
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String TKCFM = "";
		if (map.get("TKCFM") != null) {
			java.util.Date dateValue = (Date) map.get("TKCFM");
			TKCFM = sdf2.format(dateValue);
		}
		String CFMPlanDate = "";
		if (map.get("CFMPlanDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMPlanDate");
			CFMPlanDate = sdf2.format(dateValue);
		}
		String CFMSendDate = "";
		if (map.get("CFMSendDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMSendDate");
			CFMSendDate = sdf2.format(dateValue);
		}
		String CFMAnswerDate = "";
		if (map.get("CFMAnswerDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMAnswerDate");
			CFMAnswerDate = sdf2.format(dateValue);
		}
		String CFMNumber = "";
		if (map.get("CFMNumber") != null) {
			CFMNumber = (String) map.get("CFMNumber");
		}
		String CFMStatus = "";
		if (map.get("CFMStatus") != null) {
			CFMStatus = (String) map.get("CFMStatus");
		}
		String CFMRemark = "";
		if (map.get("CFMRemark") != null) {
			CFMRemark = (String) map.get("CFMRemark");
		}
		String DeliveryDate = "";
		if (map.get("DeliveryDate") != null) {
			java.util.Date dateValue = (Date) map.get("DeliveryDate");
			DeliveryDate = sdf2.format(dateValue);
		}
		String ShipDate = "";
		if (map.get("ShipDate") != null) {
			java.util.Date dateValue = (Date) map.get("ShipDate");
			ShipDate = sdf2.format(dateValue);
		}
		String RemarkOne = "";
		if (map.get("RemarkOne") != null) {
			RemarkOne = (String) map.get("RemarkOne");
		}
		String RemarkThree = "";
		if (map.get("RemarkThree") != null) {
			RemarkThree = (String) map.get("RemarkThree");
		}
		String RemarkTwo = "";
		if (map.get("RemarkTwo") != null) {
			RemarkTwo = (String) map.get("RemarkTwo");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String Remark = "";
//		if (map.get("Remark") != null) {
//			Remark = (String) map.get("Remark");
//		}
		String CFMLastest = "";
//		if (map.get("CFMLastest") != null) {
//			CFMLastest = (String) map.get("CFMLastest");
//		}
		if (CFMLastest.trim().equals("") && CFMStatus.trim().equals("") && CFMRemark.trim().equals("")) {
			CFMLastest = "";
		} else {
			CFMLastest = CFMAnswerDate + " | " + CFMStatus + " | " + CFMRemark;
		}
		if ((BillSendWeightQuantity.trim().equals("") && BillSendMRQuantity.trim().equals("")
				&& BillSendYDQuantity.trim().equals(""))
				|| (BillSendWeightQuantity.trim().equals("0.00") && BillSendMRQuantity.trim().equals("0.00")
						&& BillSendYDQuantity.trim().equals("0.00"))) {
			BillSendWeightQuantity = "";
		} else {
			BillSendWeightQuantity = BillSendWeightQuantity + " | " + BillSendMRQuantity + " | " + BillSendYDQuantity;
		}
		String Volumn = "";
		if (map.get("Volumn") != null) {
			BigDecimal value = (BigDecimal) map.get("Volumn");
			Double doubleVal = value.doubleValue();
			Volumn = formatter.format(doubleVal);
		}
		String ReplacedRemark = "";
		if (map.get("ReplacedRemark") != null) {
			ReplacedRemark = (String) map.get("ReplacedRemark");
		}
		String StockRemark = "";
		if (map.get("StockRemark") != null) {
			StockRemark = (String) map.get("StockRemark");
		}
		String StockLoad = "";
		if (map.get("StockLoad") != null) {
			StockLoad = (String) map.get("StockLoad");
		}
		Remark = RemarkOne+RemarkTwo+RemarkThree;
		String QuantityKG = "";
		if (map.get("GRSumKG") != null) {
			BigDecimal value = (BigDecimal) map.get("GRSumKG");
			Double doubleVal = value.doubleValue();
			QuantityKG = formatter.format(doubleVal);
		}
		String QuantityYD = "";
		if (map.get("GRSumYD") != null) {
			BigDecimal value = (BigDecimal) map.get("GRSumYD");
			Double doubleVal = value.doubleValue();
			QuantityYD = formatter.format(doubleVal);
		}
		String QuantityMR = "";
		if (map.get("GRSumMR") != null) {
			BigDecimal value = (BigDecimal) map.get("GRSumMR");
			Double doubleVal = value.doubleValue();
			QuantityMR = formatter.format(doubleVal);
		}
		String GRQuantity = "";
		if ((QuantityKG.trim().equals("") && QuantityYD.trim().equals("") && QuantityMR.trim().equals(""))
				|| (QuantityKG.trim().equals("0.00") && QuantityYD.trim().equals("0.00") && QuantityMR.trim().equals("0.00"))) {
			GRQuantity = "";
		} else {
			GRQuantity = QuantityKG + " | " + QuantityMR + " | " + QuantityYD;
		}
		String VolumnFGAmount = "";
		if (map.get("VolumnFGAmount") != null) {
			BigDecimal value = (BigDecimal) map.get("VolumnFGAmount");
			Double doubleVal = value.doubleValue();
			VolumnFGAmount = formatter.format(doubleVal);
		}
		String DyePlan = "";
		if (map.get("DyePlan") != null) {
//			DyePlan = (String) map.get("DyePlan");
			java.util.Date dateValue = (Date) map.get("DyePlan");
			DyePlan = sdf2.format(dateValue);
		}
		String DyeActual = "";
		if (map.get("DyeActual") != null) {
//			DyeActual = (String) map.get("DyeActual");
			java.util.Date dateValue = (Date) map.get("DyeActual");
			DyeActual = sdf2.format(dateValue);
		}
		String PCRemark = "";
		if (map.get("PCRemark") != null) {
			PCRemark = (String) map.get("PCRemark");
		}
		String SwitchRemark = "";
		if (map.get("SwitchRemark") != null) {
			SwitchRemark = (String) map.get("SwitchRemark");
		}
		String TypePrd = "";
		if (map.get("TypePrd") != null) {
			TypePrd = (String) map.get("TypePrd");
		}
		int CountInSW = 0;
		if (map.get("CountInSW") != null) {
			CountInSW = (int) map.get("CountInSW");
		}
		String SaleOrderSW = "";
		if (map.get("SaleOrderSW") != null) {
			SaleOrderSW = (String) map.get("SaleOrderSW");
		}
		String SaleLineSW = "";
		if (map.get("SaleLineSW") != null) {
			SaleLineSW = (String) map.get("SaleLineSW");
		}
		String ProductionOrderSW = "";
		if (map.get("ProductionOrderSW") != null) {
			ProductionOrderSW = (String) map.get("ProductionOrderSW");
		}
		String TypePrdRemark = "";
		if (map.get("TypePrdRemark") != null) {
			TypePrdRemark = (String) map.get("TypePrdRemark");
		}
		String SendCFMCusDate = "";
		if (map.get("SendCFMCusDate") != null) {
			java.util.Date dateValue = (Date) map.get("SendCFMCusDate");
			SendCFMCusDate = sdf2.format(dateValue);
		}
		String CauseOfDelay = "";
		if (map.get("CauseOfDelay") != null) {
			CauseOfDelay = (String) map.get("CauseOfDelay");
		}
		String DelayedDepartment = "";
		if (map.get("DelayedDep") != null) {
			DelayedDepartment = (String) map.get("DelayedDep");
		}
		String CFMDetailAll = "";
		if (map.get("CFMDetailAll") != null) {
			CFMDetailAll = (String) map.get("CFMDetailAll");
		}
		String CFMNumberAll = "";
		if (map.get("CFMNumberAll") != null) {
			CFMNumberAll = (String) map.get("CFMNumberAll");
		}
		String CFMRemarkAll = "";
		if (map.get("CFMRemarkAll") != null) {
			CFMRemarkAll = (String) map.get("CFMRemarkAll");
		}
		String RollNoRemarkAll = "";
		if (map.get("RollNoRemarkAll") != null) {
			RollNoRemarkAll = (String) map.get("RollNoRemarkAll");
		}
		String CFMDateActual = "";
		if (map.get("CFMDateActual") != null) {
			java.util.Date dateValue = (Date) map.get("CFMDateActual");
			CFMDateActual = sdf2.format(dateValue);
		}
//		String CustomerType   = "";
//		if (map.get("CustomerType") != null) {
//			CustomerType = (String) map.get("CustomerType");
//		}
		String CustomerDivision = "";
		if (map.get("CustomerDivision") != null) {
			CustomerDivision = (String) map.get("CustomerDivision");
		}
		String LotShipping = "";
		if (map.get("LotShipping") != null) {
			java.util.Date dateValue = (Date) map.get("LotShipping");
			LotShipping = sdf2.format(dateValue);
		}
		String DyeStatus = "";
		if (map.get("DyeStatus") != null) {
			DyeStatus = (String) map.get("DyeStatus");
		}
		PCMSSecondTableDetail bean = new PCMSSecondTableDetail(Division, SaleOrder, SaleLine, CustomerShortName, SaleCreateDate,
				PurchaseOrder, MaterialNo, CustomerMaterial, Price, SaleUnit, SaleQuantity, OrderAmount, RemainQuantity,
				RemainAmount, TotalQuantity, Grade, BillSendWeightQuantity, BillSendMRQuantity, BillSendYDQuantity,
				BillSendQuantity, CustomerDue, DueDate, LotNo, LabNo, LabStatus, CFMPlanLabDate, CFMActualLabDate,
				CFMCusAnsLabDate, UserStatus, TKCFM, CFMPlanDate, CFMSendDate, CFMAnswerDate, CFMNumber, CFMStatus, CFMRemark,
				DeliveryDate, ShipDate, RemarkOne, RemarkTwo, RemarkThree, Remark, CFMLastest, ProductionOrder, Volumn,
				ReplacedRemark, StockRemark, GRQuantity, VolumnFGAmount, DyePlan, DyeActual, PCRemark, SwitchRemark, TypePrd,
				StockLoad, SendCFMCusDate, CauseOfDelay, DelayedDepartment, CFMDetailAll, CFMNumberAll, CFMRemarkAll,
				RollNoRemarkAll, CFMDateActual,
//				CustomerType, 
				CustomerDivision, LotShipping, DyeStatus, CustomerMaterialBase, QuantityKG, QuantityMR, QuantityYD);
		bean.setCountInSW(CountInSW);
		bean.setSaleOrderSW(SaleOrderSW);
		bean.setSaleLineSW(SaleLineSW);
		bean.setProductionOrderSW(ProductionOrderSW);
		bean.setTypePrdRemark(TypePrdRemark);
		return bean;
	}

	@Override
	public InputDateDetail _genInputDateDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String PlanDate = "";
		if (map.get("PlanDate") != null) {
			java.util.Date dateValue = (Date) map.get("PlanDate");
			PlanDate = sdf2.format(dateValue);
		}
		String InputFrom = "";
		if (map.get("InputFrom") != null) {
			InputFrom = (String) map.get("InputFrom");
		}
		String CreateBy = "";
		if (map.get("CreateBy") != null) {
			CreateBy = (String) map.get("CreateBy");
		}
		String CreateDate = "";
		if (map.get("CreateDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("CreateDate");
			CreateDate = this.sdfDateTime1.format(timestamp1);
		}
		int countAll = 0;
		if (map.get("countAll") != null) {
			countAll = (int) map.get("countAll");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		return new InputDateDetail(ProductionOrder, SaleOrder, SaleLine, PlanDate, CreateBy, CreateDate, InputFrom, countAll,
				LotNo);
	}

	@Override
	public ColumnHiddenDetail _genColumnHiddenDetail(Map<String, Object> map)
	{
		String UserId = "";
		if (map.get("UserId") != null) {
			UserId = (String) map.get("UserId");
		}
		String ColVisibleDetail = "";
		if (map.get("ColVisibleDetail") != null) {
			ColVisibleDetail = (String) map.get("ColVisibleDetail");
		}
		String ColVisibleSummary = "";
		if (map.get("ColVisibleSummary") != null) {
			ColVisibleSummary = (String) map.get("ColVisibleSummary");
		}
////		 String[] ColList = null  ;
//		 List<String> ColList = null;
//		 if(!ColVisibleSummary.equals("")) {
////			 ColList = ColVisibleSummary.split(",");
//			 ColList = new ArrayList<String>(Arrays.asList(ColVisibleSummary.split(",")));
//		 }
		return new ColumnHiddenDetail(UserId, ColVisibleDetail, ColVisibleSummary);
	}

	@Override
	public SORDetail _genSORDetail(Map<String, Object> map)
	{
		String SaleOrder = "";
		if (map.get("SO_NO") != null) {
			SaleOrder = (String) map.get("SO_NO");
		}
		String SaleLine = "";
		if (map.get("SO_Line") != null) {
			SaleLine = (String) map.get("SO_Line");
		}
		String CFMDate = "";
		if (map.get("CFM") != null) {
			java.util.Date dateValue = (Date) map.get("CFM");
			CFMDate = sdf2.format(dateValue);
		}
		String LastUpdate = "";
		if (map.get("LastUpdateCFM") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("LastUpdateCFM");
			LastUpdate = this.sdf4.format(timestamp1);
		}
		return new SORDetail(SaleOrder, SaleLine, CFMDate, LastUpdate);
	}

	@Override
	public SORDetail _genSORFromPCMSDetail(Map<String, Object> map)
	{
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String CFMDate = "";
		if (map.get("CFMDate") != null) {
			java.util.Date dateValue = (Date) map.get("CFMDate");
			CFMDate = sdf2.format(dateValue);
		}
		String LastUpdate = "";
		if (map.get("LastUpdate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("LastUpdate");
			LastUpdate = this.sdfDateTime1.format(timestamp1);
		}
		return new SORDetail(SaleOrder, SaleLine, CFMDate, LastUpdate);
	}

	@Override
	public SwitchProdOrderDetail _genSwitchProdOrderDetail(Map<String, Object> map)
	{
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String SaleOrderSW = "";
		if (map.get("SaleOrderSW") != null) {
			SaleOrderSW = (String) map.get("SaleOrderSW");
		}
		String SaleLineSW = "";
		if (map.get("SaleLineSW") != null) {
			SaleLineSW = (String) map.get("SaleLineSW");
		}
		String ProductionOrderSW = "";
		if (map.get("ProductionOrderSW") != null) {
			ProductionOrderSW = (String) map.get("ProductionOrderSW");
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdfDateTime1.format(timestamp1);
		}
		String TypePrd = "";
		if (map.get("TypePrd") != null) {
			TypePrd = (String) map.get("TypePrd");
		}
		SwitchProdOrderDetail bean = new SwitchProdOrderDetail(SaleOrder, SaleLine, ProductionOrder, SaleOrderSW, SaleLineSW,
				ProductionOrderSW, ChangeBy, ChangeDate);
		bean.setTypePrd(TypePrd);
		return bean;
	}

	@Override
	public ReplacedProdOrderDetail _genReplacedProdOrderDetail(Map<String, Object> map)
	{
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String ProductionOrderRP = "";
		if (map.get("ProductionOrderRP") != null) {
			ProductionOrderRP = (String) map.get("ProductionOrderRP");
		}
		String Volume = "";
		if (map.get("Volume") != null) {
			BigDecimal value = (BigDecimal) map.get("Volume");
			Double doubleVal = value.doubleValue();
			Volume = formatter.format(doubleVal);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdfDateTime1.format(timestamp1);
		}
		return new ReplacedProdOrderDetail(SaleOrder, SaleLine, ProductionOrder, ProductionOrderRP, Volume, ChangeBy, ChangeDate);
	}

	@Override
	public ConfigCustomerUserDetail _genConfigCustomerUserDetail(Map<String, Object> map)
	{
		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}
		String UserId = "";
		if (map.get("UserId") != null) {
			UserId = (String) map.get("UserId");
		}
		String CustomerDivision = "";
		if (map.get("CustomerDivision") != null) {
			CustomerDivision = (String) map.get("CustomerDivision");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		Boolean IsPCMSDetailPage = true;
		if (map.get("IsPCMSDetailPage") != null) {
			IsPCMSDetailPage = (Boolean) map.get("IsPCMSDetailPage");
		}
		Boolean IsPCMSSumPage = true;
		if (map.get("IsPCMSSumPage") != null) {
			IsPCMSSumPage = (Boolean) map.get("IsPCMSSumPage");
		}
		Boolean IsProdPathBtn = true;
		if (map.get("IsProdPathBtn") != null) {
			IsProdPathBtn = (Boolean) map.get("IsProdPathBtn");
		}
		Boolean IsLBMSPathBtn = true;
		if (map.get("IsLBMSPathBtn") != null) {
			IsLBMSPathBtn = (Boolean) map.get("IsLBMSPathBtn");
		}
		Boolean IsQCMSPathBtn = true;
		if (map.get("IsQCMSPathBtn") != null) {
			IsQCMSPathBtn = (Boolean) map.get("IsQCMSPathBtn");
		}
		Boolean IsInspectPathBtn = true;
		if (map.get("IsInspectPathBtn") != null) {
			IsInspectPathBtn = (Boolean) map.get("IsInspectPathBtn");
		}
		Boolean IsSFCPathBtn = true;
		if (map.get("IsSFCPathBtn") != null) {
			IsSFCPathBtn = (Boolean) map.get("IsSFCPathBtn");
		}
		return new ConfigCustomerUserDetail(Id, UserId, CustomerNo, IsPCMSDetailPage, IsPCMSSumPage, IsProdPathBtn, IsLBMSPathBtn,
				IsQCMSPathBtn, IsInspectPathBtn, IsSFCPathBtn, CustomerDivision);
	}

	@Override
	public TempUserStatusAutoDetail _genTempUserStatusAutoDetail(Map<String, Object> map)
	{
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String ProductionOrderRPM = "";
		if (map.get("ProductionOrderRPM") != null) {
			ProductionOrderRPM = (String) map.get("ProductionOrderRPM");
		}
		String Volume = "";
		if (map.get("Volume") != null) {
			BigDecimal value = (BigDecimal) map.get("Volume");
			Double doubleVal = value.doubleValue();
			Volume = formatter.format(doubleVal);
		}
		String Grade = "";
		if (map.get("Grade") != null) {
			Grade = (String) map.get("Grade");
		}
		String UserStatusCal = "";
		if (map.get("UserStatusCal") != null) {
			UserStatusCal = (String) map.get("UserStatusCal");
		}
		String UserStatusCalRP = "";
		if (map.get("UserStatusCalRP") != null) {
			UserStatusCalRP = (String) map.get("UserStatusCalRP");
		}
		return new TempUserStatusAutoDetail(ProductionOrder, SaleOrder, SaleLine, ProductionOrderRPM, Volume, Grade,
				UserStatusCal, UserStatusCalRP);
	}

	@Override
	public ShopFloorControlDetail _genShopFloorControlDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder ") != null) {
			ProductionOrder = (String) map.get("ProductionOrder ");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String Operation = "";
		if (map.get("Operation") != null) {
			int value = (int) map.get("Operation");
			Operation = Integer.toString(value);
		}
		String WorkDate = "";
		if (map.get("WorkDate") != null) {
			java.util.Date dateValue = (Date) map.get("WorkDate");
			WorkDate = sdf2.format(dateValue);
		}
		String WorkCenter = "";
		if (map.get("WorkCenter") != null) {
			WorkCenter = (String) map.get("WorkCenter");
		}
		String CartNo = "";
		if (map.get("CartNo") != null) {
			CartNo = (String) map.get("CartNo");
		}
		String CartType = "";
		if (map.get("CartType") != null) {
			CartType = (String) map.get("CartType");
		}
		String ColorCheckOperation = "";
		if (map.get("ColorCheckOperation") != null) {
			int value = (int) map.get("ColorCheckOperation");
			ColorCheckOperation = Integer.toString(value);
		}
		String ColorCheckWorkDate = "";
		if (map.get("ColorCheckWorkDate") != null) {
			java.util.Date dateValue = (Date) map.get("ColorCheckWorkDate");
			ColorCheckWorkDate = sdf2.format(dateValue);
		}
		String Da = "";
		if (map.get("Da") != null) {
			Da = (String) map.get("Da");
		}
		String Db = "";
		if (map.get("Db") != null) {
			Db = (String) map.get("Db");
		}
		String ST = "";
		if (map.get("ST") != null) {
			ST = (String) map.get("ST");
		}
		String L = "";
		if (map.get("L") != null) {
			L = (String) map.get("L");
		}
		String ValDeltaE = "";
		if (map.get("ValDeltaE") != null) {
			ValDeltaE = (String) map.get("ValDeltaE");
		}
		String ColorCheckName = "";
		if (map.get("ColorCheckName") != null) {
			ColorCheckName = (String) map.get("ColorCheckName");
		}
		String ColorCheckStatus = "";
		if (map.get("ColorCheckStatus") != null) {
			ColorCheckStatus = (String) map.get("ColorCheckStatus");
		}
		String ColorCheckRollNo = "";
		if (map.get("ColorCheckRollNo") != null) {
			ColorCheckRollNo = (String) map.get("ColorCheckRollNo");
		}
		String ColorCheckRemark = "";
		if (map.get("ColorCheckRemark") != null) {
			ColorCheckRemark = (String) map.get("ColorCheckRemark");
		}

		String DyeingStatus = "";
		if (map.get("DyeingStatus") != null) {
			DyeingStatus = (String) map.get("DyeingStatus");
		}
		String DyeRemark = "";
		if (map.get("DyeRemark") != null) {
			DyeRemark = (String) map.get("DyeRemark");
		}

		String InspectRemark = "";
		if (map.get("InspectRemark") != null) {
			InspectRemark = (String) map.get("InspectRemark");
		}
		String MachineInspect = "";
		if (map.get("MachineInspect") != null) {
			MachineInspect = (String) map.get("MachineInspect");
		}
		return new ShopFloorControlDetail(ProductionOrder, LotNo, Operation, WorkDate, WorkCenter, CartNo, CartType,
				ColorCheckOperation, ColorCheckWorkDate, Da, Db, L, ST, ValDeltaE, ColorCheckName, ColorCheckStatus,
				ColorCheckRollNo, ColorCheckRemark, DyeingStatus, DyeRemark, InspectRemark, MachineInspect);

	}

	@Override
	public InspectOrdersDetail _genInspectOrdersDetail(Map<String, Object> map)
	{
		String prdNumber = "";
		if (map.get("PrdNumber") != null) {
			prdNumber = (String) map.get("PrdNumber");
		}
		String inspectNote = "";
		if (map.get("InspectNote") != null) {
			inspectNote = (String) map.get("InspectNote");
		}

		String rollupNote = "";
		if (map.get("RollupNote") != null) {
			rollupNote = (String) map.get("RollupNote");
		}
		String machineInspect = "";
		if (map.get("MachineInspect") != null) {
			machineInspect = (String) map.get("MachineInspect");
		}

		String machineRollup = "";
		if (map.get("MachineRollup") != null) {
			machineRollup = (String) map.get("MachineRollup");
		}
		return new InspectOrdersDetail(prdNumber, inspectNote, rollupNote, machineInspect, machineRollup);
	}

	@Override
	public ImportDetail _genImportDetail(Map<String, Object> map)
	{
		String productionOrder = "";
		if (map.get("ProductionOrder") != null) {
			productionOrder = (String) map.get("ProductionOrder");
		}
		String labNo = "";
		if (map.get("LabNo") != null) {
			labNo = (String) map.get("LabNo");
		}
		String noOfSendInLab = "";
		if (map.get("NoOfSendInLab") != null) {
			int value = (int) map.get("NoOfSendInLab");
			noOfSendInLab = Integer.toString(value);
		}
		String noOfStartLab = "";
		if (map.get("NoOfStartLab") != null) {
			int value = (int) map.get("NoOfStartLab");
			noOfStartLab = Integer.toString(value);
		}
		String sendLabDate = "";
		if (map.get("SendLabDate") != null) {
			java.util.Date dateValue = (Date) map.get("SendLabDate");
			sendLabDate = sdf2.format(dateValue);
		}
		String remark = "";
		if (map.get("Remark") != null) {
			remark = (String) map.get("Remark");
		}
		String sendFrom = "";
		if (map.get("SendFrom") != null) {
			sendFrom = (String) map.get("SendFrom");
		}
		String dateRequiredLab = "";
		if (map.get("DateRequiredLab") != null) {
			java.util.Date dateValue = (Date) map.get("DateRequiredLab");
			dateRequiredLab = sdf2.format(dateValue);
		}
		String labStartDate = "";
		if (map.get("LabStartDate") != null) {
			java.util.Date dateValue = (Date) map.get("LabStartDate");
			labStartDate = sdf2.format(dateValue);
		}
		String labStopDate = "";
		if (map.get("LabStopDate") != null) {
			java.util.Date dateValue = (Date) map.get("LabStopDate");
			labStopDate = sdf2.format(dateValue);
		}
		return new ImportDetail(productionOrder, labNo, noOfSendInLab, noOfStartLab, sendLabDate, remark, sendFrom,
				dateRequiredLab, labStartDate, labStopDate);
	}

	@Override
	public CustomerDetail _genCustomerDetail(Map<String, Object> map)
	{
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerNoWOZero = "";
		if (map.get("CustomerNoWOZero") != null) {
			CustomerNoWOZero = (String) map.get("CustomerNoWOZero");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerShortName = "";
		if (map.get("CustomerShortName") != null) {
			CustomerShortName = (String) map.get("CustomerShortName");
		}
		String CustomerType = "";
		if (map.get("CustomerType") != null) {
			CustomerType = (String) map.get("CustomerType");
		}
		String DistChannel = "";
		if (map.get("DistChannel") != null) {
			DistChannel = (String) map.get("DistChannel");
		} 
		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		} 
		boolean IsSabina = false;
		if (map.get("IsSabina") != null) {
			IsSabina = (boolean) map.get("IsSabina");
		}
		CustomerDetail bean = new CustomerDetail(CustomerNo, CustomerName, CustomerShortName, CustomerType, DistChannel, SyncDate);
		bean.setCustomerNoWOZero(CustomerNoWOZero);
		bean.setSabina(IsSabina);
				return bean;
	}

	@Override
	public FromErpCFMDetail _genFromErpCFMDetail(Map<String, Object> map)
	{
		String ID = "";
		if (map.get("ID") != null) {
			ID = (String) map.get("ID");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String CFMNo = "";
		if (map.get("CFMNo") != null) {
			CFMNo = (String) map.get("CFMNo");
		}
		String CFMNumber = "";
		if (map.get("CFMNumber") != null) {
			CFMNumber = (String) map.get("CFMNumber");
		}
		String CFMSendDate = "";
		if (map.get("CFMSendDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("CFMSendDate");
			CFMSendDate = this.sdfFullDatetime.format(timestamp1); 
		}
		String CFMAnswerDate = "";
		if (map.get("CFMAnswerDate") != null) { 
			Timestamp timestamp1 = (Timestamp) map.get("CFMAnswerDate");
			CFMAnswerDate = this.sdfFullDatetime.format(timestamp1); 
		}
		String CFMStatus = "";
		if (map.get("CFMStatus") != null) {
			CFMStatus = (String) map.get("CFMStatus");
		}
		String CFMRemark = "";
		if (map.get("CFMRemark") != null) {
			CFMRemark = (String) map.get("CFMRemark");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}

		String NextLot = "";
		if (map.get("NextLot") != null) {
			NextLot = (String) map.get("NextLot");
		}

		String SOChange = "";
		if (map.get("SOChange") != null) {
			SOChange = (String) map.get("SOChange");
		}
		String SOChangeQty = "";
		if (map.get("SOChangeQty") != null) {
			SOChangeQty = (String) map.get("SOChangeQty");
		}
		String SOChangeUnit = "";
		if (map.get("SOChangeUnit") != null) {
			SOChangeUnit = (String) map.get("SOChangeUnit");
		}
		String RollNo = "";
		if (map.get("RollNo") != null) {
			RollNo = (String) map.get("RollNo");
		}
		String RollNoRemark = "";
		if (map.get("RollNoRemark") != null) {
			RollNoRemark = (String) map.get("RollNoRemark");
		}

		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpCFMDetail(ID, ProductionOrder, CFMNo, CFMNumber, CFMSendDate, CFMAnswerDate, CFMStatus, CFMRemark,
				SaleOrder, SaleLine, NextLot, SOChange, SOChangeQty, SOChangeUnit, RollNo, RollNoRemark, CFMStatus, SyncDate);
	}

	@Override
	public FromErpGoodReceiveDetail _genFromErpGoodReceiveDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String Grade = "";
		if (map.get("Grade") != null) {
			Grade = (String) map.get("Grade");
		}
		String RollNumber = "";
		if (map.get("RollNumber") != null) {
			RollNumber = (String) map.get("RollNumber");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {

			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			Double doubleVal = value.doubleValue();
			QuantityKG = formatter.format(doubleVal);
		}
		String QuantityYD = "";
		if (map.get("QuantityYD") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityYD");
			Double doubleVal = value.doubleValue();
			QuantityYD = formatter.format(doubleVal);
		}
		String QuantityMR = "";
		if (map.get("QuantityMR") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityMR");
			Double doubleVal = value.doubleValue();
			QuantityMR = formatter.format(doubleVal);
		}
		String PriceSTD = "";
		if (map.get("PriceSTD") != null) {
			BigDecimal value = (BigDecimal) map.get("PriceSTD");
			Double doubleVal = value.doubleValue();
			PriceSTD = formatter.format(doubleVal);
		}

		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpGoodReceiveDetail(ProductionOrder, SaleOrder, SaleLine, Grade, RollNumber, QuantityKG, QuantityYD,
				QuantityMR, PriceSTD, DataStatus, SyncDate);
	}

	@Override
	public FromErpMainBillBatchDetail _genFromErpMainBillBatchDetail(Map<String, Object> map)
	{
		String BillDoc = "";
		if (map.get("BillDoc") != null) {
			BillDoc = (String) map.get("BillDoc");
		}
		String BillItem = "";
		if (map.get("BillItem") != null) {
			BillItem = (String) map.get("BillItem");
		}
		String LotShipping = "";
		if (map.get("LotShipping") != null) {
//			java.util.Date dateValue = (Date) map.get("LotShipping");
//			LotShipping = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("LotShipping");
			LotShipping = sdf2.format(timestamp1);
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String Grade = "";
		if (map.get("Grade") != null) {
			Grade = (String) map.get("Grade");
		}
		String RollNumber = "";
		if (map.get("RollNumber") != null) {
			RollNumber = (String) map.get("RollNumber");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {

			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			Double doubleVal = value.doubleValue();
			QuantityKG = formatter.format(doubleVal);
		}
		String QuantityYD = "";
		if (map.get("QuantityYD") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityYD");
			Double doubleVal = value.doubleValue();
			QuantityYD = formatter.format(doubleVal);
		}
		String QuantityMR = "";
		if (map.get("QuantityMR") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityMR");
			Double doubleVal = value.doubleValue();
			QuantityMR = formatter.format(doubleVal);
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}

		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpMainBillBatchDetail(BillDoc, BillItem, LotShipping, ProductionOrder, SaleOrder, SaleLine, Grade,
				RollNumber, QuantityKG, QuantityYD, QuantityMR, LotNo, DataStatus, SyncDate);
	}

	@Override
	public FromErpMainProdDetail _genFromErpMainProdDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String TotalQuantity = "";
		if (map.get("TotalQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalQuantity");
			Double doubleVal = value.doubleValue();
			TotalQuantity = formatter.format(doubleVal);
		}
		String Unit = "";
		if (map.get("Unit") != null) {
			Unit = (String) map.get("Unit");
		}
		String RemAfterCloseOne = "";
		if (map.get("RemAfterCloseOne") != null) {
			RemAfterCloseOne = (String) map.get("RemAfterCloseOne");
		}
		String RemAfterCloseTwo = "";
		if (map.get("RemAfterCloseTwo") != null) {
			RemAfterCloseTwo = (String) map.get("RemAfterCloseTwo");
		}
		String RemAfterCloseThree = "";
		if (map.get("RemAfterCloseThree") != null) {
			RemAfterCloseThree = (String) map.get("RemAfterCloseThree");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String DesignFG = "";
		if (map.get("DesignFG") != null) {
			DesignFG = (String) map.get("DesignFG");
		}
		String ArticleFG = "";
		if (map.get("ArticleFG") != null) {
			ArticleFG = (String) map.get("ArticleFG");
		}
		String BookNo = "";
		if (map.get("BookNo") != null) {
			BookNo = (String) map.get("BookNo");
		}
		String Center = "";
		if (map.get("Center") != null) {
			Center = (String) map.get("Center");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String Batch = "";
		if (map.get("Batch") != null) {
			Batch = (String) map.get("Batch");
		}
		String LabNo = "";
		if (map.get("LabNo") != null) {
			LabNo = (String) map.get("LabNo");
		}
		String RemarkOne = "";
		if (map.get("RemarkOne") != null) {
			RemarkOne = (String) map.get("RemarkOne");
		}
		String RemarkTwo = "";
		if (map.get("RemarkTwo") != null) {
			RemarkTwo = (String) map.get("RemarkTwo");
		}
		String RemarkThree = "";
		if (map.get("RemarkThree") != null) {
			RemarkThree = (String) map.get("RemarkThree");
		}
		String BCAware = "";
		if (map.get("BCAware") != null) {
			BCAware = (String) map.get("BCAware");
		}
		String OrdErpuang = "";
		if (map.get("OrdErpuang") != null) {
			OrdErpuang = (String) map.get("OrdErpuang");
		}
		String RefPrd = "";
		if (map.get("RefPrd") != null) {
			RefPrd = (String) map.get("RefPrd");
		}
		String GreigeInDate = "";
		if (map.get("GreigeInDate") != null) {
//			java.util.Date dateValue = (Date) map.get("GreigeInDate");
//			GreigeInDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("GreigeInDate");
			GreigeInDate = sdf2.format(timestamp1);

		}
		String BCDate = "";
		if (map.get("BCDate") != null) {
//			java.util.Date dateValue = (Date) map.get("BCDate");
//			BCDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("BCDate");
			BCDate = sdf2.format(timestamp1);
		}
		String Volumn = "";
		if (map.get("Volumn") != null) {
			BigDecimal value = (BigDecimal) map.get("Volumn");
			Double doubleVal = value.doubleValue();
			Volumn = formatter.format(doubleVal);

		}
		String CFdate = "";
		if (map.get("CFdate") != null) {
//			java.util.Date dateValue = (Date) map.get("CFdate");
//			CFdate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("CFdate");
			CFdate = sdf2.format(timestamp1);
		}
		String CFType = "";
		if (map.get("CFType") != null) {
//			java.util.Date dateValue = (Date) map.get("CFType");
//			CFType = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("CFType");
			CFType = sdf2.format(timestamp1);
		}
		String Shade = "";
		if (map.get("Shade") != null) {
			Shade = (String) map.get("Shade");
		}
		String LotShipping = "";
		if (map.get("LotShipping") != null) {
//			java.util.Date dateValue = (Date) map.get("LotShipping");
//			LotShipping = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("LotShipping");
			LotShipping = sdf2.format(timestamp1);
		}
		String BillSendQuantity = "";
		if (map.get("BillSendQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("BillSendQuantity");
			Double doubleVal = value.doubleValue();
			BillSendQuantity = formatter.format(doubleVal);
		}
		String Grade = "";
		if (map.get("Grade") != null) {
			Grade = (String) map.get("Grade");
		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String PrdCreateDate = "";
		if (map.get("PrdCreateDate") != null) {
//			java.util.Date dateValue = (Date) map.get("PrdCreateDate");
//			PrdCreateDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("PrdCreateDate");
			PrdCreateDate = sdf2.format(timestamp1);
		}
		String GreigeArticle = "";
		if (map.get("GreigeArticle") != null) {
			GreigeArticle = (String) map.get("GreigeArticle");
		}
		String GreigeDesign = "";
		if (map.get("GreigeDesign") != null) {
			GreigeDesign = (String) map.get("GreigeDesign");
		}
		String GreigeMR = "";
		if (map.get("GreigeMR") != null) {
			BigDecimal value = (BigDecimal) map.get("GreigeMR");
			Double doubleVal = value.doubleValue();
			GreigeMR = formatter.format(doubleVal);
		}
		String GreigeKG = "";
		if (map.get("GreigeKG") != null) {
			BigDecimal value = (BigDecimal) map.get("GreigeKG");
			Double doubleVal = value.doubleValue();
			GreigeKG = formatter.format(doubleVal);
		}
		String OrderType = "";
		if (map.get("OrderType") != null) {
			OrderType = (String) map.get("OrderType");
		}

		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpMainProdDetail(ProductionOrder, SaleOrder, SaleLine, TotalQuantity, Unit, RemAfterCloseOne,
				RemAfterCloseTwo, RemAfterCloseThree, LabStatus, UserStatus, DesignFG, ArticleFG, BookNo, Center, LotNo, Batch,
				LabNo, RemarkOne, RemarkTwo, RemarkThree, BCAware, OrdErpuang, RefPrd, GreigeInDate, BCDate, Volumn, CFdate,
				CFType, Shade, LotShipping, BillSendQuantity, Grade, DataStatus, PrdCreateDate, GreigeArticle, GreigeDesign,
				GreigeMR, GreigeKG, OrderType, SyncDate);
	}

	@Override
	public FromErpMainProdSaleDetail _genFromErpMainProdSaleDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String Volumn = "";
		if (map.get("Volumn") != null) {
			BigDecimal value = (BigDecimal) map.get("Volumn");
			Double doubleVal = value.doubleValue();
			Volumn = formatter.format(doubleVal);
		}

		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpMainProdSaleDetail(ProductionOrder, SaleOrder, SaleLine, Volumn, DataStatus, SyncDate);
	}

	@Override
	public FromErpMainSaleDetail _genFromErpMainSaleDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
//			java.util.Date dateValue = (Date) map.get("DueDate");
//			DueDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("DueDate");
			DueDate = sdf2.format(timestamp1);
		}
		String PlanGreigeDate = "";
		if (map.get("PlanGreigeDate") != null) {
//			java.util.Date dateValue = (Date) map.get("PlanGreigeDate");
//			PlanGreigeDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("PlanGreigeDate");
			PlanGreigeDate = sdf2.format(timestamp1);
		}
		String SaleUnit = "";
		if (map.get("SaleUnit") != null) {
			SaleUnit = (String) map.get("SaleUnit");
		}
		String SaleQuantity = "";
		if (map.get("SaleQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("SaleQuantity");
			Double doubleVal = value.doubleValue();
			SaleQuantity = formatter.format(doubleVal);
		}
		String CustomerMaterial = "";
		if (map.get("CustomerMaterial") != null) {
			CustomerMaterial = (String) map.get("CustomerMaterial");
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String PurchaseOrder = "";
		if (map.get("PurchaseOrder") != null) {
			PurchaseOrder = (String) map.get("PurchaseOrder");
		}
		String SaleOrg = "";
		if (map.get("SaleOrg") != null) {
			SaleOrg = (String) map.get("SaleOrg");
		}
		String DistChannel = "";
		if (map.get("DistChannel") != null) {
			DistChannel = (String) map.get("DistChannel");
		}
		String Division = "";
		if (map.get("Division") != null) {
			Division = (String) map.get("Division");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerShortName = "";
		if (map.get("CustomerShortName") != null) {
			CustomerShortName = (String) map.get("CustomerShortName");
		}
		String ColorCustomer = "";
		if (map.get("ColorCustomer") != null) {
			ColorCustomer = (String) map.get("ColorCustomer");
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
//			java.util.Date dateValue = (Date) map.get("CustomerDue");
//			CustomerDue = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("CustomerDue");
			CustomerDue = sdf2.format(timestamp1);
		}
		String RemainQuantity = "";
		if (map.get("RemainQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainQuantity");
			Double doubleVal = value.doubleValue();
			RemainQuantity = formatter.format(doubleVal);
		}
		String ShipDate = "";
		if (map.get("ShipDate") != null) {
//			java.util.Date dateValue = (Date) map.get("ShipDate");
//			ShipDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("ShipDate");
			ShipDate = sdf2.format(timestamp1);
		}
		String SaleStatus = "";
		if (map.get("SaleStatus") != null) {
			SaleStatus = (String) map.get("SaleStatus");
		}
		String Currency = "";
		if (map.get("Currency") != null) {
			Currency = (String) map.get("Currency");
		}
		String Price = "";
		if (map.get("Price") != null) {
			BigDecimal value = (BigDecimal) map.get("Price");
			Double doubleVal = value.doubleValue();
			Price = formatter.format(doubleVal);
		}
		String OrderAmount = "";
		if (map.get("OrderAmount") != null) {
			BigDecimal value = (BigDecimal) map.get("OrderAmount");
			Double doubleVal = value.doubleValue();
			OrderAmount = formatter.format(doubleVal);
		}
		String RemainAmount = "";
		if (map.get("RemainAmount") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainAmount");
			Double doubleVal = value.doubleValue();
			RemainAmount = formatter.format(doubleVal);
		}
		String SaleCreateDate = "";
		if (map.get("SaleCreateDate") != null) {
//			java.util.Date dateValue = (Date) map.get("SaleCreateDate");
//			SaleCreateDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("SaleCreateDate");
			SaleCreateDate = sdf2.format(timestamp1);
		}
		String SaleNumber = "";
		if (map.get("SaleNumber") != null) {
			SaleNumber = (String) map.get("SaleNumber");
		}
		String SaleFullName = "";
		if (map.get("SaleFullName") != null) {
			SaleFullName = (String) map.get("SaleFullName");
		}
		String DeliveryStatus = "";
		if (map.get("DeliveryStatus") != null) {
			DeliveryStatus = (String) map.get("DeliveryStatus");
		}
		String DesignFG = "";
		if (map.get("DesignFG") != null) {
			DesignFG = (String) map.get("DesignFG");
		}
		String ArticleFG = "";
		if (map.get("ArticleFG") != null) {
			ArticleFG = (String) map.get("ArticleFG");
		}
		String OrderSheetPrintDate = "";
		if (map.get("OrderSheetPrintDate") != null) {
//			java.util.Date dateValue = (Date) map.get("OrderSheetPrintDate");
//			OrderSheetPrintDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("OrderSheetPrintDate");
			OrderSheetPrintDate = sdf2.format(timestamp1);
		}
		String CustomerMaterialBase = "";
		if (map.get("CustomerMaterialBase") != null) {
			CustomerMaterialBase = (String) map.get("CustomerMaterialBase");
		}

		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpMainSaleDetail(ProductionOrder,SaleOrder, SaleLine, MaterialNo, DueDate, PlanGreigeDate, SaleUnit, SaleQuantity,
				CustomerMaterial, Color, CustomerNo, PurchaseOrder, SaleOrg, DistChannel, Division, CustomerName,
				CustomerShortName, ColorCustomer, CustomerDue, RemainQuantity, ShipDate, SaleStatus, Currency, Price, OrderAmount,
				RemainAmount, SaleCreateDate, SaleNumber, SaleFullName, DeliveryStatus, DesignFG, ArticleFG, OrderSheetPrintDate,
				CustomerMaterialBase, DataStatus, SyncDate);
	}

	@Override
	public FromErpPackingDetail _genFromErpPackingDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String PostingDate = "";
		if (map.get("PostingDate") != null) { 
			Timestamp timestamp1 = (Timestamp) map.get("PostingDate");
			PostingDate = sdf2.format(timestamp1);
		}
		String Quantity = "";
		if (map.get("Quantity") != null) {
			BigDecimal value = (BigDecimal) map.get("Quantity");
			Double doubleVal = value.doubleValue();
			Quantity = formatter.format(doubleVal);
		}
		String RollNo = "";
		if (map.get("RollNo") != null) {
			RollNo = (String) map.get("RollNo");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			Double doubleVal = value.doubleValue();
			QuantityKG = formatter.format(doubleVal);
		}
		String Grade = "";
		if (map.get("Grade") != null) {
			Grade = (String) map.get("Grade");
		}
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}
		String QuantityYD = "";
		if (map.get("QuantityYD") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityYD");
			Double doubleVal = value.doubleValue();
			QuantityYD = formatter.format(doubleVal);
		}

		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpPackingDetail(ProductionOrder, PostingDate, Quantity, RollNo, QuantityKG, Grade, No, QuantityYD,
				DataStatus, SyncDate);
	}

	@Override
	public FromErpPODetail _genFromErpPODetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String RollNo = "";
		if (map.get("RollNo") != null) {
			RollNo = (String) map.get("RollNo");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			Double doubleVal = value.doubleValue();
			QuantityKG = formatter.format(doubleVal);
		}
		String QuantityMR = "";
		if (map.get("QuantityMR") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityMR");
			Double doubleVal = value.doubleValue();
			QuantityMR = formatter.format(doubleVal);
		}
		String POCreatedate = "";
		if (map.get("POCreatedate") != null) {
//			java.util.Date dateValue = (Date) map.get("POCreatedate");
//			POCreatedate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("POCreatedate");
			POCreatedate = sdf2.format(timestamp1);
		}
		String RequiredDate = "";
		if (map.get("RequiredDate") != null) {
//			java.util.Date dateValue = (Date) map.get("RequiredDate");
//			RequiredDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("RequiredDate");
			RequiredDate = sdf2.format(timestamp1);
		}
		String PurchaseOrder = "";
		if (map.get("PurchaseOrder") != null) {
			PurchaseOrder = (String) map.get("PurchaseOrder");
		}
		String PurchaseOrderLine = "";
		if (map.get("PurchaseOrderLine") != null) {
			PurchaseOrderLine = (String) map.get("PurchaseOrderLine");
		}
		String PODefault = "";
		if (map.get("PODefault") != null) {
			PODefault = (String) map.get("PODefault");
		}
		String POLineDefault = "";
		if (map.get("POLineDefault") != null) {
			POLineDefault = (String) map.get("POLineDefault");
		}
		String POPostingDateDefault = "";
		if (map.get("POPostingDateDefault") != null) {
//			java.util.Date dateValue = (Date) map.get("POPostingDateDefault");
//			POPostingDateDefault = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("POPostingDateDefault");
			POPostingDateDefault = sdf2.format(timestamp1);
		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}

		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpPODetail(ProductionOrder, RollNo, QuantityKG, QuantityMR, POCreatedate, RequiredDate, PurchaseOrder,
				PurchaseOrderLine, null, PODefault, POLineDefault, POPostingDateDefault, DataStatus, SyncDate);
	}

	@Override
	public FromErpReceipeDetail _genFromErpReceipeDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}

		// TODO Auto-generated method stub
		return new FromErpReceipeDetail(ProductionOrder, LotNo, SyncDate, DataStatus);
	}

	@Override
	public FromErpSaleDetail _genFromErpSaleDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String BillDate = "";
		if (map.get("BillDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("BillDate");
			BillDate = sdf2.format(timestamp1);
		}
		String BillQtyPerSale = "";
		if (map.get("BillQtyPerSale") != null) {

			BigDecimal value = (BigDecimal) map.get("BillQtyPerSale");
			Double doubleVal = value.doubleValue();
			BillQtyPerSale = formatter.format(doubleVal);
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String BillQtyPerStock = "";
		if (map.get("BillQtyPerStock") != null) {

			BigDecimal value = (BigDecimal) map.get("BillQtyPerStock");
			Double doubleVal = value.doubleValue();
			BillQtyPerStock = formatter.format(doubleVal);
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName1 = "";
		if (map.get("CustomerName1") != null) {
			CustomerName1 = (String) map.get("CustomerName1");
		}
		String CustomErpO = "";
		if (map.get("CustomErpO") != null) {
			CustomErpO = (String) map.get("CustomErpO");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("DueDate");
			DueDate = sdf2.format(timestamp1);
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}

		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpSaleDetail(ProductionOrder, BillDate, BillQtyPerSale, SaleOrder, SaleLine, BillQtyPerStock, Remark,
				CustomerNo, CustomerName1, CustomErpO, DueDate, Color, No, DataStatus, SyncDate);
	}

	@Override
	public FromErpSaleInputDetail _genFromErpSaleInputDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String BillDate = "";
		if (map.get("BillDate") != null) {

//			java.util.Date dateValue = (Date) map.get("BillDate");
//			BillDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("BillDate");
			BillDate = sdf2.format(timestamp1);
		}
		String BillQtyPerSale = "";
		if (map.get("BillQtyPerSale") != null) {

			BigDecimal value = (BigDecimal) map.get("BillQtyPerSale");
			Double doubleVal = value.doubleValue();
			BillQtyPerSale = formatter.format(doubleVal);
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String BillQtyPerStock = "";
		if (map.get("BillQtyPerStock") != null) {

			BigDecimal value = (BigDecimal) map.get("BillQtyPerStock");
			Double doubleVal = value.doubleValue();
			BillQtyPerStock = formatter.format(doubleVal);
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName1 = "";
		if (map.get("CustomerName1") != null) {
			CustomerName1 = (String) map.get("CustomerName1");
		}
		String CustomErpO = "";
		if (map.get("CustomErpO") != null) {
			CustomErpO = (String) map.get("CustomErpO");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
//			java.util.Date dateValue = (Date) map.get("DueDate");
//			DueDate = sdf2.format(dateValue);
			Timestamp timestamp1 = (Timestamp) map.get("DueDate");
			DueDate = sdf2.format(timestamp1);
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}

		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpSaleInputDetail(ProductionOrder, BillDate, BillQtyPerSale, SaleOrder, SaleLine, BillQtyPerStock, Remark,
				CustomerNo, CustomerName1, CustomErpO, DueDate, Color, No, DataStatus, SyncDate);
	}

	@Override
	public FromErpSubmitDateDetail _genFromErpSubmitDateDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}
		String SubmitDate = "";
		if (map.get("SubmitDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SubmitDate");
			SubmitDate = sdf2.format(timestamp1);
//			java.util.Date dateValue = (Date) map.get("SubmitDate");
//			SubmitDate = sdf2.format(dateValue);
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}

		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String SyncDate = "";
		if (map.get("SyncDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("SyncDate");
			SyncDate = this.sdfFullDatetime.format(timestamp1);
		}
		return new FromErpSubmitDateDetail(ProductionOrder, SaleOrder, SaleLine, No, SubmitDate, Remark, DataStatus, SyncDate);
	}

	@Override
	public EmployeeDetail _genEmployeeDetail(Map<String, Object> map)
	{
		String Id = "";
		if (map.get("Id") != null) {
			Id = Integer.toString((int) map.get("Id"));
		}
		String EmployeeID = "";
		if (map.get("EmployeeID") != null) {
			EmployeeID = (String) map.get("EmployeeID");
		}
		String FirstName = "";
		if (map.get("FirstName") != null) {
			FirstName = (String) map.get("FirstName");
		}
		String LastName = "";
		if (map.get("LastName") != null) {
			LastName = (String) map.get("LastName");
		}
		String Role = "";
		if (map.get("Role") != null) {
			Role = (String) map.get("Role");
		}
		String ArrangedBy = "";
		if (map.get("ArrangedBy") != null) {
			ArrangedBy = (String) map.get("ArrangedBy");
		}
		String AuthorizedBy = "";
		if (map.get("AuthorizedBy") != null) {
			AuthorizedBy = (String) map.get("AuthorizedBy");
		}
		String PermitId = ""; // add
		if (map.get("PermitId") != null) {
			PermitId = (String) map.get("PermitId");
		}
		String Responsible = "";
		if (map.get("Responsible") != null) {
			Responsible = (String) map.get("Responsible");
		}

		EmployeeDetail bean = new EmployeeDetail(EmployeeID, PermitId, Responsible, "", FirstName, LastName, Role, "", "", Id,
				ArrangedBy, AuthorizedBy);
		return bean;
	}

	@Override
	public UserDetail _genUsersDetail(Map<String, Object> map)
	{
//		String Id = "";
//		if (map.get("Id") != null) {
//			Id = Integer.toString((int) map.get("Id"));
//		}
		String UserId = "";
		if (map.get("UserId") != null) {
			UserId = (String) map.get("UserId");
		}
		String FirstName = "";
		if (map.get("FirstName") != null) {
			FirstName = (String) map.get("FirstName");
		}
		String LastName = "";
		if (map.get("LastName") != null) {
			LastName = (String) map.get("LastName");
		}
		String Role = "";
		if (map.get("Role") != null) {
			Role = (String) map.get("Role");
		}
		String ArrangedBy = "";
		if (map.get("ArrangedBy") != null) {
			ArrangedBy = (String) map.get("ArrangedBy");
		}
		String AuthorizedBy = "";
		if (map.get("AuthorizedBy") != null) {
			AuthorizedBy = (String) map.get("AuthorizedBy");
		}
		String PermitId = ""; // add
		if (map.get("PermitId") != null) {
			PermitId = (String) map.get("PermitId");
		}
		String Responsible = "";
		if (map.get("Responsible") != null) {
			Responsible = (String) map.get("Responsible");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdfDateTime1.format(timestamp1);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String LastSignDate = "";
		if (map.get("LastSignDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("LastSignDate");
			LastSignDate = this.sdfDateTime1.format(timestamp1);
		}
		String RegistDate = "";
		if (map.get("RegistDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("RegistDate");
			RegistDate = this.sdfDateTime1.format(timestamp1);
		}
		String registBy = "";
		if (map.get("registBy") != null) {
			registBy = (String) map.get("registBy");
		}
		boolean IsSystem = false;
		if (map.get("IsSystem") != null) {
			IsSystem = (boolean) map.get("IsSystem");
		}
		boolean IsAdmin = false;
		if (map.get("IsAdmin") != null) {
			IsAdmin = (boolean) map.get("IsAdmin");
		}
		boolean IsCustomer = false;
		if (map.get("IsCustomer") != null) {
			IsCustomer = (boolean) map.get("IsCustomer");
		}
		String department = "";
		String email = "";
		UserDetail bean =
				new UserDetail(0, UserId, "", FirstName, LastName, Role, department, email, ArrangedBy, AuthorizedBy, IsSystem,
						IsAdmin, PermitId, LastSignDate, Responsible, ChangeBy, ChangeDate, registBy, RegistDate, "", IsCustomer);
//		public UserDetail(int id, String userId, String password, String firstName, String lastName, String role,
//				String department, String email, 
//				String arrangedBy, String authorizedBy, boolean isSystem, boolean isAdmin,
//				String permitId, Date lastSignDate, String responsible
//				, String changeBy, Date changeDate, String registBy,
//				Date registDate, String UserType, boolean IsCustomer) {
		return bean;
	}

	@Override
	public PermitDetail _genPermitDetail(Map<String, Object> map)
	{
		int Id = 0;
		if (map.get("Id") != null) {
//			Id = Integer.toString((int) map.get("Id"));
			Id = (int) map.get("Id");
		}
		String PermitId = "";
		if (map.get("PermitId") != null) {
			PermitId = (String) map.get("PermitId");
		}
		String Description = "";
		if (map.get("Description") != null) {
			Description = (String) map.get("Description");
		}
		boolean IsPCMSDetailPage = false;
		if (map.get("IsPCMSDetailPage") != null) {
			IsPCMSDetailPage = (boolean) map.get("IsPCMSDetailPage");
		}
		boolean IsPCMSSumPage = false;
		if (map.get("IsPCMSSumPage") != null) {
			IsPCMSSumPage = (boolean) map.get("IsPCMSSumPage");
		}
		boolean IsProdPathBtn = false;
		if (map.get("IsProdPathBtn") != null) {
			IsProdPathBtn = (boolean) map.get("IsProdPathBtn");
		}
		boolean IsLBMSPathBtn = false;
		if (map.get("IsLBMSPathBtn") != null) {
			IsLBMSPathBtn = (boolean) map.get("IsLBMSPathBtn");
		}
		boolean IsQCMSPathBtn = false;
		if (map.get("IsQCMSPathBtn") != null) {
			IsQCMSPathBtn = (boolean) map.get("IsQCMSPathBtn");
		}
		boolean IsInspectPathBtn = false;
		if (map.get("IsInspectPathBtn") != null) {
			IsInspectPathBtn = (boolean) map.get("IsInspectPathBtn");
		}
		boolean IsSFCPathBtn = false;
		if (map.get("IsSFCPathBtn") != null) {
			IsSFCPathBtn = (boolean) map.get("IsSFCPathBtn");
		}

		return new PermitDetail(Id, PermitId, Description, IsPCMSDetailPage, IsPCMSSumPage, IsProdPathBtn, IsLBMSPathBtn,
				IsQCMSPathBtn, IsInspectPathBtn, IsSFCPathBtn);
	}

}
