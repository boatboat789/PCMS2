package dao.implement;

import java.math.BigDecimal; 
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import dao.BeanCreateModelDao;
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
import entities.ReplacedProdOrderDetail;
import entities.SORDetail;
import entities.SaleDetail;
import entities.SaleInputDetail;
import entities.SendTestQCDetail;
import entities.SubmitDateDetail;
import entities.SwitchProdOrderDetail;
import entities.WaitTestDetail;
import entities.WorkInLabDetail;

public class BeanCreateModelDaoImpl implements BeanCreateModelDao {
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm:ss"); 
	DecimalFormat df = new DecimalFormat("0.00");       
	DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
	@Override  
	public PCMSTableDetail _genPCMSTableDetail(Map<String, Object> map) {
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
		
		String RemainQuantity = "";
		Double doubleRemainQ = 0.00;
		if (map.get("RemainQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainQuantity");
			doubleRemainQ = value.doubleValue();
			RemainQuantity = formatter.format(doubleRemainQ);
		} 
		String SaleQuantity = "";  
		Double doubleSaleQ = 0.00;
		String BillQuantity = "";
		if (map.get("SaleQuantity") != null) {
			BigDecimal value = (BigDecimal) map.get("SaleQuantity");
			doubleSaleQ = value.doubleValue();
			SaleQuantity = formatter.format(doubleSaleQ);
			
			Double billQ = doubleSaleQ - doubleRemainQ;
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
//			java.util.Date timestamp1 = (Date) map.get("GreigeInDate");
//			GreigeInDate = sdf2.format(timestamp1);
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
			java.util.Date timestamp1 = (Date) map.get("DueDate");
			DueDate = sdf2.format(timestamp1);
		} 
		String Prepare = "", Preset = "", DyePlan = "", DyeActual = "", Dryer = "", Finishing = "", Inspectation = "",
				CFMPlanDate = "", CFMDateActual = "", DeliveryDate = "",Relax = "",GreigeInDate = "",LotShipping = "";;
//		if (map.get("GreigeInDate") != null) {
//			GreigeInDate = (String) map.get("GreigeInDate");
//		}
//		if (map.get("Prepare") != null) {
//			Prepare = (String) map.get("Prepare");
//		}
//		if (map.get("Relax") != null) {
//			Relax = (String) map.get("Relax");
//		}
//		if (map.get("Preset") != null) {
//			Preset = (String) map.get("Preset");
//		}
//		if (map.get("DyePlan") != null) {
//			DyePlan = (String) map.get("DyePlan");
//		}
//		if (map.get("DyeActual") != null) {
//			DyeActual = (String) map.get("DyeActual");
//		}
//		if (map.get("Dryer") != null) {
//			Dryer = (String) map.get("Dryer");
//		}
//		if (map.get("Finishing") != null) {
//			Finishing = (String) map.get("Finishing");
//		}
//		if (map.get("Inspectation") != null) {
//			Inspectation = (String) map.get("Inspectation");
//		}
//		if (map.get("CFMPlanDate") != null) {
//			CFMPlanDate = (String) map.get("CFMPlanDate");
//		}
//		if (map.get("CFMDateActual") != null) {
//			CFMDateActual = (String) map.get("CFMDateActual");
//		}
//		if (map.get("DeliveryDate") != null) {
//			DeliveryDate = (String) map.get("DeliveryDate");
//		}  
//		if (map.get("LotShipping") != null) {
//			LotShipping = (String) map.get("LotShipping");
//		}
		
		
		if (map.get("GreigeInDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("GreigeInDate");
			String dateStr =  sdf2.format(timestamp1);   
			GreigeInDate = createDDMM(dateStr);
		}
		if (map.get("Prepare") != null) {
			java.util.Date timestamp1 = (Date) map.get("Prepare");
			String dateStr =  sdf2.format(timestamp1);   
			Prepare = createDDMM(dateStr);
		}
		if (map.get("Relax") != null) {
			java.util.Date timestamp1 = (Date) map.get("Relax");
			String dateStr =  sdf2.format(timestamp1);   
			Relax = createDDMM(dateStr);
		}
		if (map.get("Preset") != null) {
			java.util.Date timestamp1 = (Date) map.get("Preset");
			String dateStr =  sdf2.format(timestamp1);   
			Preset = createDDMM(dateStr);
		}
		if (map.get("DyePlan") != null) {
			java.util.Date timestamp1 = (Date) map.get("DyePlan");
			String dateStr =  sdf2.format(timestamp1);   
			DyePlan = createDDMM(dateStr);
		}
		if (map.get("DyeActual") != null) {
			java.util.Date timestamp1 = (Date) map.get("DyeActual");
			String dateStr =  sdf2.format(timestamp1);   
			DyeActual = createDDMM(dateStr);
		}
		if (map.get("Dryer") != null) {
			java.util.Date timestamp1 = (Date) map.get("Dryer");
			String dateStr =  sdf2.format(timestamp1);   
			Dryer = createDDMM(dateStr);
		}
		if (map.get("Finishing") != null) {
			java.util.Date timestamp1 = (Date) map.get("Finishing");
			String dateStr =  sdf2.format(timestamp1);   
			Finishing = createDDMM(dateStr);
		}
		if (map.get("Inspectation") != null) {
			java.util.Date timestamp1 = (Date) map.get("Inspectation");
			String dateStr =  sdf2.format(timestamp1);   
			Inspectation = createDDMM(dateStr);
		}
		if (map.get("CFMPlanDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("CFMPlanDate");
			String dateStr =  sdf2.format(timestamp1);   
			CFMPlanDate = createDDMM(dateStr);
		}
		if (map.get("CFMDateActual") != null) {
			java.util.Date timestamp1 = (Date) map.get("CFMDateActual");
			String dateStr =  sdf2.format(timestamp1);   
			CFMDateActual = createDDMM(dateStr);
		}
		if (map.get("DeliveryDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("DeliveryDate");
			String dateStr = sdf2.format(timestamp1);   
			DeliveryDate = createDDMM(dateStr);
		}   
		if (map.get("LotShipping") != null) {    
			java.util.Date timestamp1 = (Date) map.get("LotShipping");
			String dateStr =  sdf2.format(timestamp1);   
			LotShipping = createDDMM(dateStr);
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
			SaleFullName =  (String) map.get("SaleFullName");
		}
//		SaleFullName = SaleNumber+ ":"+SaleFullName;
		String SaleCreateDate = "";
		if (map.get("SaleCreateDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("SaleCreateDate");
			SaleCreateDate = sdf2.format(timestamp1);
		}
		String PrdCreateDate = "";
		if (map.get("PrdCreateDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("PrdCreateDate");
			PrdCreateDate = sdf2.format(timestamp1);
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
		}String ShipDate = "";
		if (map.get("ShipDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("ShipDate");
			ShipDate = sdf2.format(timestamp1);
		}
		String Division = "";
		if (map.get("Division") != null) {
			java.util.Date timestamp1 = (Date) map.get("Division");
			ShipDate = sdf2.format(timestamp1);
		}  
		String DyeStatus = "";
		if (map.get("DyeStatus") != null) {
			DyeStatus = (String) map.get("DyeStatus");  
		}
		String TypePrd = "";
		if (map.get("TypePrd") != null) {
			TypePrd = (String) map.get("TypePrd");  
		} 
		String TypePrdRemark   = "";
		if (map.get("TypePrdRemark") != null) {
			TypePrdRemark = (String) map.get("TypePrdRemark");
		}
		return new PCMSTableDetail(SaleOrder, SaleLine, DesignFG, ArticleFG, DistChannel, Color, ColorCustomer,
				SaleQuantity, BillQuantity, SaleUnit, ProductionOrder, TotalQuantity, GreigeInDate, UserStatus,
				LabStatus, DueDate, Prepare, Preset, DyePlan, DyeActual, Dryer, Finishing, Inspectation, CFMPlanDate,
				CFMDateActual, DeliveryDate, LotShipping, LabNo, CustomerShortName, SaleNumber, SaleFullName,
				SaleCreateDate, PrdCreateDate, MaterialNo, DeliveryStatus, SaleStatus,LotNo,ShipDate,Relax,
				CustomerName,Division,DyeStatus,TypePrd,TypePrdRemark);
	}
	public String createDDMM(String dateStr) {
		String[] x = dateStr.split("/");
		String dd = x[0].replaceFirst("^0+(?!$)", ""); 
		String mm = x[1].replaceFirst("^0+(?!$)", "");
		dateStr = dd+"/"+mm;
		return dateStr;
	}
	public PCMSTableDetail _genSearchTableDetail(Map<String, Object> map) {
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
		} String Division = "";
		if (map.get("Division") != null) {
			Division = (String) map.get("Division");
		} 
		return new PCMSTableDetail(SaleOrder, DesignFG, ArticleFG, DistChannel , ProductionOrder, 
				UserStatus , DueDate, LabNo, CustomerShortName, SaleNumber ,
				SaleCreateDate, PrdCreateDate, MaterialNo, DeliveryStatus, SaleStatus ,CustomerName
				,No,UserId,Division);
	}

	@Override
	public PCMSAllDetail _genPCMSAllDetail(Map<String, Object> map) {
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
			java.util.Date timestamp1 = (Date) map.get("PrdCreateDate");
			PrdCreateDate = sdf2.format(timestamp1);
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("DueDate");
			DueDate = sdf2.format(timestamp1);
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
		}String CustomerShortName = "";
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
			java.util.Date timestamp1 = (Date) map.get("PlanGreigeDate");
			PlanGreigeDate = sdf2.format(timestamp1);
		}
		String RefPrd = "";
		if (map.get("RefPrd") != null) {
			RefPrd = (String) map.get("RefPrd");
		}
		String GreigeInDate = "";
		if (map.get("GreigeInDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("GreigeInDate");
			GreigeInDate = sdf2.format(timestamp1);
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
			java.util.Date timestamp1 = (Date) map.get("CFMPlanDate");
			CFMPlanDate = sdf2.format(timestamp1);
		}
		String DeliveryDate = "";
		if (map.get("DeliveryDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("DeliveryDate");
			DeliveryDate = sdf2.format(timestamp1);
		}   
//		String CFMPlanDate = "";
//		if (map.get("CFMPlanDate") != null) { 
//			java.util.Date timestamp1 = (Date) map.get("CFMPlanDate");
//			CFMPlanDate = sdf2.format(timestamp1);
//			}       
//		String DeliveryDate = "";
//		if (map.get("DeliveryDate") != null) { 
//			java.util.Date timestamp1 = (Date) map.get("DeliveryDate");
//			DeliveryDate = sdf2.format(timestamp1);
//			}   
		String BCDate = "";
		if (map.get("BCDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("BCDate");
			BCDate = sdf2.format(timestamp1);
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
		return new PCMSAllDetail(ProductionOrder, LotNo, Batch, LabNo, PrdCreateDate, DueDate, SaleOrder, SaleLine,
				PurchaseOrder, ArticleFG, DesignFG, CustomerName, CustomerShortName,Shade, BookNo, Center, MaterialNo, Volumn, SaleUnit,
				STDUnit, Color, PlanGreigeDate, RefPrd, GreigeInDate, BCAware, OrderPuang, UserStatus, LabStatus,
				CFMPlanDate, DeliveryDate, BCDate, RemarkOne, RemarkTwo, RemarkThree, RemAfterCloseOne,
				RemAfterCloseTwo, RemAfterCloseThree,GreigeArticle,GreigeDesign,ColorCustomer);
	}

	@Override
	public PODetail _genPODetail(Map<String, Object> map) {
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
		String CreateDate = "";
		if (map.get("CreateDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("CreateDate");
			CreateDate = sdf2.format(timestamp1);
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
			java.util.Date timestamp1 = (Date) map.get("RequiredDate");
			RequiredDate = sdf2.format(timestamp1);
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
			java.util.Date timestamp1 = (Date) map.get("POPostingDateDefault");
			POPostingDateDefault = sdf2.format(timestamp1);
		}
		return new PODetail(ProductionOrder, PurchaseOrder, PurchaseOrderLine, CreateDate, RequiredDate, RollNo,
				QuantityKG, QuantityMR, PODefault, POLineDefault, POPostingDateDefault);
	}

	@Override
	public PresetDetail _genPresetDetail(Map<String, Object> map) {
		String PostingDate = "";
		if (map.get("PostingDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("PostingDate");
			PostingDate = sdf2.format(timestamp1);
		}
		String WorkCenter = "";
		if (map.get("WorkCenter") != null) {
			WorkCenter = (String) map.get("WorkCenter");
		}
		return new PresetDetail(PostingDate, WorkCenter);
	}

	@Override
	public DyeingDetail _genDyeingDetail(Map<String, Object> map) {
		String PostingDate = "";
		if (map.get("PostingDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("PostingDate");
			PostingDate = sdf2.format(timestamp1);
		}
		String Operation = "";
		if (map.get("Operation") != null) {
			Operation = (String) map.get("Operation");
		}
		String WorkCenter = "";
		if (map.get("WorkCenter") != null) {
			WorkCenter = (String) map.get("WorkCenter");
		}
		String DyeStatus = "";
		if (map.get("DyeStatus") != null) {
			DyeStatus = (String) map.get("DyeStatus");
		}
		String DeltaE = "";
		if (map.get("DeltaE") != null) {
			DeltaE = (String) map.get("DeltaE");
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
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String Redye = "";
		if (map.get("Redye") != null) {
			Redye = (String) map.get("Redye");
		}
		String Batch = "";
		if (map.get("Batch") != null) {
			Batch = (String) map.get("Batch");
		}
		String ColorStatus = "";
		if (map.get("ColorStatus") != null) {
			ColorStatus = (String) map.get("ColorStatus");
		}
		String ColorRemark = "";
		if (map.get("ColorRemark") != null) {
			ColorRemark = (String) map.get("ColorRemark");
		}

		return new DyeingDetail(PostingDate, Operation, WorkCenter, DyeStatus, DeltaE, L, Da, Db, ST, Remark, Redye,
				Batch, ColorStatus, ColorRemark);
	}

	@Override
	public SendTestQCDetail _genSendTestQCDetail(Map<String, Object> map) {
		String SendDate = "";
		if (map.get("SendDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("SendDate");
			SendDate = sdf2.format(timestamp1);
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
			java.util.Date timestamp1 = (Date) map.get("CheckColorDate");
			CheckColorDate = sdf2.format(timestamp1);
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
	public InspectDetail _genInspectDetail(Map<String, Object> map) {
		String PostingDate = "";
		if (map.get("PostingDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("PostingDate");
			PostingDate = sdf2.format(timestamp1);
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
	public FinishingDetail _genFinishingDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String PostingDate = "";
		if (map.get("PostingDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("PostingDate");
			PostingDate = sdf2.format(timestamp1);
		}
		String WorkCenter = "";
		if (map.get("WorkCenter") != null) {
			WorkCenter = (String) map.get("WorkCenter");
		}
		String Status = "";
		if (map.get("Status") != null) {
			Status = (String) map.get("Status");
		}
		String NCDate = "";
		if (map.get("NCDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("NCDate");
			NCDate = sdf2.format(timestamp1);
		}
		String Cause = "";
		if (map.get("Cause") != null) {
			Cause = (String) map.get("Cause");
		}
		String CarNo = "";
		if (map.get("CarNo") != null) {
			CarNo = (String) map.get("CarNo");
		}
		String DeltaE = "";
		if (map.get("DeltaE") != null) {
			DeltaE = (String) map.get("DeltaE");
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}

		String Operation = "";
		if (map.get("Operation") != null) {
			Operation = (String) map.get("Operation");
		}
		String CCStatus = "";
		if (map.get("CCStatus") != null) {
			CCStatus = (String) map.get("CCStatus");
		}
		String CCRemark = "";
		if (map.get("CCRemark") != null) {
			CCRemark = (String) map.get("CCRemark");
		}
		String RollNo = "";
		if (map.get("RollNo") != null) {
			RollNo = (String) map.get("RollNo");
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
		String CCPostingDate = "";
		if (map.get("CCPostingDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("CCPostingDate");
			CCPostingDate = sdf2.format(timestamp1);
		}
		String CCOperation = "";
		if (map.get("CCOperation") != null) {
			CCOperation = (String) map.get("CCOperation");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		return new FinishingDetail(PostingDate, WorkCenter, Status, NCDate, Cause, CarNo, DeltaE, Color, Operation,
				CCStatus, CCRemark, RollNo, Da, Db, L, ST, CCPostingDate, CCOperation, LotNo);
	}

	@Override
	public PackingDetail _genPackingDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String PostingDate = "";
		if (map.get("PostingDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("PostingDate");
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
		return new PackingDetail(PostingDate, Quantity, RollNo, Status, QuantityKG, Grade,QuantityYD);
	}

	@Override
	public WorkInLabDetail _genWorkInLabDetail(Map<String, Object> map) {
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}
		String SendDate = "";
		if (map.get("SendDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("SendDate");
			SendDate = sdf2.format(timestamp1);
		}
		String NOK = "";
		if (map.get("NOK") != null) {
			NOK = (String) map.get("NOK");
		}
		String NCDate = "";
		if (map.get("NCDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("NCDate");
			NCDate = sdf2.format(timestamp1);
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String ReceiveDate = "";
		if (map.get("ReceiveDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("ReceiveDate");
			ReceiveDate = sdf2.format(timestamp1);
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
	public WaitTestDetail _genWaitTestDetail(Map<String, Object> map) { 
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		}
		String DateInTest = "";
		if (map.get("DateInTest") != null) {
			java.util.Date timestamp1 = (Date) map.get("DateInTest");
			DateInTest = sdf2.format(timestamp1);
		} 
		String DateOutTest = "";
		if (map.get("DateOutTest") != null) {
			java.util.Date timestamp1 = (Date) map.get("DateOutTest");
			DateOutTest = sdf2.format(timestamp1);
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
	public CFMDetail _genCFMDetail(Map<String, Object> map) {  
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
			java.util.Date timestamp1 = (Date) map.get("CFMSendDate");
			CFMSendDate = sdf2.format(timestamp1);
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
			java.util.Date timestamp1 = (Date) map.get("CFMAnswerDate");
			CFMAnswerDate = sdf2.format(timestamp1);
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
		// TODO Auto-generated method stub 
		return new CFMDetail(CFMNo, CFMNumber, CFMSendDate, RollNo, RollNoRemark,
				L, Da, Db, ST, SaleOrder, SaleLine, Color, CFMAnswerDate, CFMStatus,
				CFMRemark, NextLot, SOChange, SOChangeQty, SOChangeUnit);
	}

	@Override
	public SaleDetail _genSaleDetail(Map<String, Object> map) {
		String BillDate = "";
		if (map.get("BillDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("BillDate");
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
		if (map.get("Quantity") != null) {
			BigDecimal value = (BigDecimal) map.get("BillQtyPerStock");
			Double doubleVal = value.doubleValue();
			BillQtyPerStock = formatter.format(doubleVal);
		}
		return new SaleDetail(BillDate, BillQtyPerSale, SaleOrder, SaleLine, BillQtyPerStock);
	}

	@Override
	public SaleInputDetail _genSaleInputDetail(Map<String, Object> map) { 
		String BillDate = "";
		if (map.get("BillDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("BillDate");
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
		if (map.get("Quantity") != null) {
			BigDecimal value = (BigDecimal) map.get("BillQtyPerStock");
			Double doubleVal = value.doubleValue();
			BillQtyPerStock = formatter.format(doubleVal);
		}
		return new SaleInputDetail(BillDate, BillQtyPerSale, SaleOrder, SaleLine, BillQtyPerStock);
	}

	@Override
	public SubmitDateDetail _genSubmitDateDetail(Map<String, Object> map) { 
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		} 
		String SubmitDate = "";
		if (map.get("SubmitDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("SubmitDate");
			SubmitDate = sdf2.format(timestamp1);
		}   
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		} 
		return new SubmitDateDetail(No, SubmitDate, Remark);
	}

	@Override
	public NCDetail _genNCDetail(Map<String, Object> map) { 
		String No = "";
		if (map.get("No") != null) {
			No = (String) map.get("No");
		} 
		String NCDate = "";
		if (map.get("NCDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("NCDate");
			NCDate = sdf2.format(timestamp1);
		}   
		String CarNo = "";
		if (map.get("CarNo") != null) {
			CarNo = (String) map.get("CarNo");
		} 
		String Quantity = "";
		if (map.get("Quantity") != null) {
			BigDecimal value = (BigDecimal) map.get("Quantity");
			Double doubleVal = value.doubleValue();
			Quantity = formatter.format(doubleVal);
		}
		String Unit = "";
		if (map.get("Unit") != null) {
			Unit = (String) map.get("Unit");
		} 
		String NCFrom = "";
		if (map.get("NCFrom") != null) {
			NCFrom = (String) map.get("NCFrom");
		} 
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}  
		return new NCDetail(No, NCDate, CarNo, Quantity, Unit, NCFrom, Remark);
	}

	@Override
	public ReceipeDetail _genReceipeDetail(Map<String, Object> map) { 
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
			java.util.Date timestamp1 = (Date) map.get("PostingDate");
			PostingDate = sdf2.format(timestamp1);
		}   
		String Receipe = "";
		if (map.get("Receipe") != null) {
			Receipe = (String) map.get("Receipe");
		}
		return new ReceipeDetail(No, LotNo, PostingDate, Receipe);
	}

	@Override
	public PCMSSecondTableDetail _genPCMSSecondTableDetail(Map<String, Object> map) {
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
			java.util.Date timestamp1 = (Date) map.get("SaleCreateDate");
			SaleCreateDate = sdf2.format(timestamp1);
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
			java.util.Date timestamp1 = (Date) map.get("DueDate");
			DueDate = sdf2.format(timestamp1);
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
			java.util.Date timestamp1 = (Date) map.get("CFMPlanLabDate");
			CFMPlanLabDate = sdf2.format(timestamp1);
		}   
		String CFMActualLabDate = "";
		if (map.get("CFMActualLabDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("CFMActualLabDate");
			CFMActualLabDate = sdf2.format(timestamp1);
		}   
		String CFMCusAnsLabDate = ""; 
		if (map.get("CFMCusAnsLabDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("CFMCusAnsLabDate");
			CFMCusAnsLabDate = sdf2.format(timestamp1);
		}   
		String UserStatus  = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String TKCFM = "";
		if (map.get("TKCFM") != null) {
			java.util.Date timestamp1 = (Date) map.get("TKCFM");
			TKCFM = sdf2.format(timestamp1);
		}
		String CFMPlanDate = ""; 
		if (map.get("CFMPlanDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("CFMPlanDate");
			CFMPlanDate = sdf2.format(timestamp1);
		}   
		String CFMSendDate = "";
		if (map.get("CFMSendDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("CFMSendDate");
			CFMSendDate = sdf2.format(timestamp1);
		}   
		String CFMAnswerDate = "";
		if (map.get("CFMAnswerDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("CFMAnswerDate");
			CFMAnswerDate = sdf2.format(timestamp1);
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
			java.util.Date timestamp1 = (Date) map.get("DeliveryDate");
			DeliveryDate = sdf2.format(timestamp1);
		}   
		String ShipDate = ""; 
		if (map.get("ShipDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("ShipDate");
			ShipDate = sdf2.format(timestamp1);
		}   
		String RemarkOne = ""; 
		if (map.get("RemarkOne") != null) {
			RemarkOne = (String) map.get("RemarkOne");
		}
		String RemarkThree = "";
		if (map.get("RemarkThree") != null) {
			RemarkThree = (String) map.get("RemarkThree");
		}
		String RemarkTwo   = "";
		if (map.get("RemarkTwo") != null) {
			RemarkTwo = (String) map.get("RemarkTwo");
		}
		String ProductionOrder   = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String Remark   = "";
//		if (map.get("Remark") != null) {
//			Remark = (String) map.get("Remark");
//		}
		String CFMLastest   = "";
//		if (map.get("CFMLastest") != null) {
//			CFMLastest = (String) map.get("CFMLastest");
//		} 
		if(CFMLastest.trim().equals("") && CFMStatus.trim().equals("") && CFMRemark.trim().equals("")) {
			CFMLastest = "";
		}    
		else {
			CFMLastest =  CFMAnswerDate + " | " + CFMStatus + " | "+ CFMRemark ;
		}   
		if((BillSendWeightQuantity.trim().equals("") && BillSendMRQuantity.trim().equals("") && BillSendYDQuantity.trim().equals(""))||
	       (BillSendWeightQuantity.trim().equals("0.00") && BillSendMRQuantity.trim().equals("0.00") && BillSendYDQuantity.trim().equals("0.00"))
	       ) {
			BillSendWeightQuantity = "";  
		}    
		else {  
			BillSendWeightQuantity =  BillSendWeightQuantity + " | " + BillSendMRQuantity + " | "+ BillSendYDQuantity ;
		}  
		String Volumn = "";
		if (map.get("Volumn") != null) {
			BigDecimal value = (BigDecimal) map.get("Volumn");
			Double doubleVal = value.doubleValue();
			Volumn = formatter.format(doubleVal);
		}   
		String ReplacedRemark   = "";
		if (map.get("ReplacedRemark") != null) {
			ReplacedRemark = (String) map.get("ReplacedRemark");
		}
		String StockRemark   = "";
		if (map.get("StockRemark") != null) {
			StockRemark = (String) map.get("StockRemark");
		}
		String StockLoad   = "";
		if (map.get("StockLoad") != null) {
			StockLoad = (String) map.get("StockLoad");
		}
		Remark = RemarkOne + RemarkTwo   + RemarkThree;
		String QuantityKG   = "";
		if (map.get("GRSumKG") != null) { 
			BigDecimal value = (BigDecimal) map.get("GRSumKG");
			Double doubleVal = value.doubleValue();
			QuantityKG = formatter.format(doubleVal);
		}
		String QuantityYD   = "";
		if (map.get("GRSumYD") != null) { 
			BigDecimal value = (BigDecimal) map.get("GRSumYD");
			Double doubleVal = value.doubleValue();
			QuantityYD = formatter.format(doubleVal);
		}
		String QuantityMR   = "";
		if (map.get("GRSumMR") != null) { 
			BigDecimal value = (BigDecimal) map.get("GRSumMR");
			Double doubleVal = value.doubleValue();
			QuantityMR = formatter.format(doubleVal);
		}String GRQuantity = "";
		if((QuantityKG.trim().equals("") && QuantityYD.trim().equals("") && QuantityMR.trim().equals(""))||
	       (QuantityKG.trim().equals("0.00") && QuantityYD.trim().equals("0.00") && QuantityMR.trim().equals("0.00"))
	       ) {
			GRQuantity = "";  
		}    
		else {  
			GRQuantity =  QuantityKG + " | " + QuantityMR + " | "+ QuantityYD ;
		}   
		String VolumnFGAmount   = "";
		if (map.get("VolumnFGAmount") != null) { 
			BigDecimal value = (BigDecimal) map.get("VolumnFGAmount");
			Double doubleVal = value.doubleValue();
			VolumnFGAmount = formatter.format(doubleVal);
		}
		String DyePlan   = "";
		if (map.get("DyePlan") != null) {
			DyePlan = (String) map.get("DyePlan");
		}
		String DyeActual   = "";
		if (map.get("DyeActual") != null) {
			DyeActual = (String) map.get("DyeActual");
		}
		String PCRemark   = "";
		if (map.get("PCRemark") != null) {
			PCRemark = (String) map.get("PCRemark");
		}
		String SwitchRemark   = "";
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
		String TypePrdRemark   = "";
		if (map.get("TypePrdRemark") != null) {
			TypePrdRemark = (String) map.get("TypePrdRemark");
		}
		String SendCFMCusDate = ""; 
		if (map.get("SendCFMCusDate") != null) {
			java.util.Date timestamp1 = (Date) map.get("SendCFMCusDate");
			SendCFMCusDate = sdf2.format(timestamp1);
		}
		String CauseOfDelay   = "";
		if (map.get("CauseOfDelay") != null) {
			CauseOfDelay = (String) map.get("CauseOfDelay");
		}
		String DelayedDepartment   = "";
		if (map.get("DelayedDep") != null) {
			DelayedDepartment = (String) map.get("DelayedDep");
		}  
		String CFMDetailAll   = "";
		if (map.get("CFMDetailAll") != null) {
			CFMDetailAll = (String) map.get("CFMDetailAll");
		}  
		String CFMNumberAll   = "";
		if (map.get("CFMNumberAll") != null) {
			CFMNumberAll = (String) map.get("CFMNumberAll");
		}  
		String CFMRemarkAll   = "";
		if (map.get("CFMRemarkAll") != null) {
			CFMRemarkAll = (String) map.get("CFMRemarkAll");
		}  
		String RollNoRemarkAll   = "";
		if (map.get("RollNoRemarkAll") != null) {
			RollNoRemarkAll = (String) map.get("RollNoRemarkAll");
		}  
		PCMSSecondTableDetail bean = new PCMSSecondTableDetail(Division, SaleOrder, SaleLine, CustomerShortName, 
				SaleCreateDate, PurchaseOrder, MaterialNo, CustomerMaterial, Price, SaleUnit, SaleQuantity,OrderAmount, 
				RemainQuantity, RemainAmount, TotalQuantity, Grade, BillSendWeightQuantity, BillSendMRQuantity, BillSendYDQuantity, BillSendQuantity, CustomerDue, 
				DueDate, LotNo, LabNo, LabStatus, CFMPlanLabDate, CFMActualLabDate, CFMCusAnsLabDate, UserStatus,
				TKCFM, CFMPlanDate, CFMSendDate, CFMAnswerDate, 
				CFMNumber, CFMStatus, CFMRemark, DeliveryDate, ShipDate, RemarkOne, RemarkTwo, RemarkThree,
				Remark,CFMLastest,ProductionOrder,Volumn,ReplacedRemark,StockRemark,GRQuantity,VolumnFGAmount, DyePlan, DyeActual,
				PCRemark,SwitchRemark,TypePrd,StockLoad,SendCFMCusDate,CauseOfDelay,DelayedDepartment,
				CFMDetailAll,CFMNumberAll,CFMRemarkAll,RollNoRemarkAll) ;
		bean.setCountInSW(CountInSW);
		bean.setSaleOrderSW(SaleOrderSW);
		bean.setSaleLineSW(SaleLineSW);
		bean.setProductionOrderSW(ProductionOrderSW);
		bean.setTypePrdRemark(TypePrdRemark);
		return bean;
	}
	@Override
	public InputDateDetail _genInputDateDetail(Map<String, Object> map) {  
		String ProductionOrder   = "";
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
			java.util.Date timestamp1 = (Date) map.get("PlanDate"); 
			PlanDate = sdf2.format(timestamp1);
		}
		String CreateBy = "";
		if (map.get("CreateBy") != null) {
			CreateBy = (String) map.get("CreateBy");
		}
		String InputFrom = "";
		if (map.get("InputFrom") != null) {
			InputFrom = (String) map.get("InputFrom");
		}
		String CreateDate = "";
		if (map.get("CreateDate") != null) {   
		    Timestamp timestamp1 = (Timestamp)map.get("CreateDate");
			CreateDate = this.sdf3.format(timestamp1);
		} 
		int countAll = 0;
		if (map.get("countAll") != null) {
			countAll = (int) map.get("countAll");
		} 
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		return new InputDateDetail(ProductionOrder, SaleOrder, SaleLine, PlanDate, CreateBy,CreateDate ,InputFrom,countAll,LotNo);
	}

	@Override
	public ColumnHiddenDetail _genColumnHiddenDetail(Map<String, Object> map) {
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
		return new ColumnHiddenDetail(UserId, ColVisibleDetail,ColVisibleSummary );
	}
	@Override
	public SORDetail _genSORDetail(Map<String, Object> map) {
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
			java.util.Date timestamp1 = (Date) map.get("CFM");
			CFMDate = sdf2.format(timestamp1); 
		}   
		String LastUpdate = "";
		if (map.get("LastUpdateCFM") != null) {
		    Timestamp timestamp1 = (Timestamp)map.get("LastUpdateCFM");
		    LastUpdate = this.sdf4.format(timestamp1);
		}  
		return new SORDetail(SaleOrder, SaleLine, CFMDate, LastUpdate);
	}
	@Override
	public SORDetail _genSORFromPCMSDetail(Map<String, Object> map) {
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
			java.util.Date timestamp1 = (Date) map.get("CFMDate");
			CFMDate = sdf2.format(timestamp1);
		}   
		String LastUpdate = "";
		if (map.get("LastUpdate") != null) {
		    Timestamp timestamp1 = (Timestamp)map.get("LastUpdate");
		    LastUpdate = this.sdf3.format(timestamp1);
		}  
		return new SORDetail(SaleOrder, SaleLine, CFMDate, LastUpdate);
	}
	@Override
	public SwitchProdOrderDetail _genSwitchProdOrderDetail(Map<String, Object> map) {
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
		    Timestamp timestamp1 = (Timestamp)map.get("ChangeDate");
		    ChangeDate = this.sdf3.format(timestamp1);
		} 
		String TypePrd = "";
		if (map.get("TypePrd") != null) {
			TypePrd = (String) map.get("TypePrd");  
		}
		SwitchProdOrderDetail bean = new SwitchProdOrderDetail(SaleOrder, SaleLine, ProductionOrder, SaleOrderSW, SaleLineSW, ProductionOrderSW, ChangeBy, ChangeDate);
		bean.setTypePrd(TypePrd);
		return bean;
	}
	@Override
	public ReplacedProdOrderDetail _genReplacedProdOrderDetail(Map<String, Object> map) {
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
		    Timestamp timestamp1 = (Timestamp)map.get("ChangeDate");
		    ChangeDate = this.sdf3.format(timestamp1);
		} 
		return new ReplacedProdOrderDetail(SaleOrder, SaleLine, ProductionOrder, ProductionOrderRP, Volume, ChangeBy, ChangeDate);
	}
}
