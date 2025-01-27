package dao.implement;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.BeanCreateModelDao;
import entities.CFMDetail;
import entities.CSO_SOASDetail;
import entities.ChangeLogDetail;
import entities.ChangeSettingLogDetail;
import entities.CustomerDetail;
import entities.CustomerMonthlyReportDetail;
import entities.EmployeeDetail;
import entities.ForecastRunningDetail;
import entities.ForecastStatusReportDetail;
import entities.InputApprovedDetail;
import entities.InputApprovedFCDetail;
import entities.InputArticleConversionDetail;
import entities.InputArticleDetail;
import entities.InputBKKHolidayDetail;
import entities.InputDateRunningDetail;
import entities.InputFacHolidayDetail;
import entities.InputFacWorkDateDetail;
import entities.InputForecastDetail;
import entities.InputGroupDetail;
import entities.InputLeadTimeDetail;
import entities.InputLeadTimeStatusDetail;
import entities.InputMachineDetail;
import entities.InputPODetail;
import entities.InputPlanInsteadProdOrderDetail;
import entities.InputPlanLotRedyeDetail;
import entities.InputPlanningLotDetail;
import entities.InputSpecialCaseMRDetail;
import entities.InputStockCustomerDateDetail;
import entities.InputTempProdDetail;
import entities.KPIReportDetail;
import entities.MasterSettingChangeDetail;
import entities.MonthlyCapReportDetail;
import entities.MonthlyCapWithDueDateReportDetail;
import entities.POManagementDetail;
import entities.POStatusReportDetail;
import entities.PermitDetail;
import entities.PlanningReportDetail;
import entities.ProdOrderRunningDetail;
import entities.RecreateRedyeReportDetail;
import entities.RedyePlanningReportDetail;
import entities.ResendSORDateDetail;
import entities.SOR_DUE_REPORTDetail;
import entities.SO_DOC_STEPDetail;
import entities.StockReceiveDateDetail;
import entities.StockReceiveDetail;
import entities.SummaryMonthlyCapReportDetail;
import entities.SummaryOSReportDetail;
import entities.TotalGroupDyeDetail;
import entities.TotalOperationDetail;
import entities.WorkDateDetail;
import entities.master.PPMM2.DataFromSapDetail;
import entities.master.PPMM2.RollFromSapDetail;
import resources.Config;

public class BeanCreateDaoImpl implements BeanCreateModelDao {
	public SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdfDDMM = new SimpleDateFormat("dd/MM");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm:ss");
	public DecimalFormat df = new DecimalFormat("0.0000");
	public DecimalFormat df1 = new DecimalFormat("###,###,##0");
	public DecimalFormat df2 = new DecimalFormat("###,###,##0.00");
	public DecimalFormat formatter = new DecimalFormat("###,###,##0.0000");

	@Override
	public InputApprovedDetail _genApprovedDetail(Map<String, Object> map)
	{
		String ReferenceId = "";
		if (map.get("ReferenceId") != null) {
			ReferenceId = (String) map.get("ReferenceId");
		}
		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}
		int POId = 0;
		if (map.get("POId") != null) {
			POId = (int) map.get("POId");
		}
		String PO = "";
		if (map.get("PO") != null) {
			PO = (String) map.get("PO");
		}
		String POLine = "";
		if (map.get("POLine") != null) {
			POLine = (String) map.get("POLine");
		}

		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = this.sdf2.format(dateStr);
		}
		String FirstSORDueDate = "";
		if (map.get("FirstSORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("FirstSORDueDate");
			FirstSORDueDate = this.sdf2.format(dateStr);
		}

		String FirstApprovedDate = "";
		if (map.get("FirstApprovedDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("FirstApprovedDate");
			FirstApprovedDate = this.sdf3.format(timestamp1);
		}
		String FirstApprovedBy = "";
		if (map.get("FirstApprovedBy") != null) {
			FirstApprovedBy = (String) map.get("FirstApprovedBy");
		}

		String LastSORDueDate = "";
		if (map.get("LastSORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("LastSORDueDate");
			LastSORDueDate = this.sdf2.format(dateStr);
		}
		String LastApprovedDate = "";
		if (map.get("LastApprovedDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("LastApprovedDate");
			LastApprovedDate = this.sdf3.format(timestamp1);
		}
		String LastApprovedBy = "";
		if (map.get("LastApprovedBy") != null) {
			LastApprovedBy = (String) map.get("LastApprovedBy");
		}
		String CurrentSORDueDate = "";
		if (map.get("CurrentSORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("CurrentSORDueDate");
			CurrentSORDueDate = this.sdf2.format(dateStr);
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerMat = "";
		if (map.get("CustomerMat") != null) {
			CustomerMat = (String) map.get("CustomerMat");
		}
		String POQty = "";
		if (map.get("POQty") != null) {
			BigDecimal value = (BigDecimal) map.get("POQty");
			double doubleVal = value.doubleValue();
			POQty = this.df2.format(doubleVal);
		}
		String Unit = "";
		if (map.get("Unit") != null) {
			Unit = (String) map.get("Unit");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String Design = "";
		if (map.get("Design") != null) {
			Design = (String) map.get("Design");
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}
		String ColorCustomer = "";
		if (map.get("ColorCustomer") != null) {
			ColorCustomer = (String) map.get("ColorCustomer");
		}
		String LabRef = "";
		if (map.get("LabRef") != null) {
			LabRef = (String) map.get("LabRef");
		}
		String PlanCFMDate = "";
		if (map.get("PlanCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanCFMDate");
			PlanCFMDate = this.sdf2.format(dateStr);
		}
		String PlanDueDate = "";
		if (map.get("PlanDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanDueDate");
			PlanDueDate = this.sdf2.format(dateStr);
		}
		String SORCFMDate = "";
		if (map.get("SORCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORCFMDate");
			SORCFMDate = this.sdf2.format(dateStr);
		}
		String SORDueDate = "";
		if (map.get("SORDueDateLast") != null) {
			java.util.Date dateStr = (Date) map.get("SORDueDateLast");
			SORDueDate = this.sdf2.format(dateStr);
		}
		int countSORDueDateLast = 0;
		if (map.get("countSORDueDateLast") != null) {
			countSORDueDateLast = (int) map.get("countSORDueDateLast");
		}
		String CaseSORDueDate = "";
		if (map.get("CaseSORDueDate") != null) {
			CaseSORDueDate = (String) map.get("CaseSORDueDate");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String POType = "";
		if (map.get("POType") != null) {
			POType = (String) map.get("POType");
		}
		return new InputApprovedDetail(POId, PO, POLine, CustomerDue, FirstSORDueDate, FirstApprovedDate, FirstApprovedBy,
				LastSORDueDate, LastApprovedDate, LastApprovedBy, CurrentSORDueDate, Remark, ProductionOrder, MaterialNo,
				PlanCFMDate, CustomerName, CustomerMat, POQty, Unit, Article, Design, Color, ColorCustomer, LabRef, PlanDueDate,
				SORCFMDate, SORDueDate, countSORDueDateLast, CaseSORDueDate, CustomerNo, POType, Id, ReferenceId);
	}

	@Override
	public InputApprovedFCDetail _genApprovedFCDetail(Map<String, Object> map)
	{
		int ForecastId = 0;
		if (map.get("ForecastId") != null) {
			ForecastId = (int) map.get("ForecastId");
		}
		String ForecastNo = "";
		if (map.get("ForecastNo") != null) {
			ForecastNo = (String) map.get("ForecastNo");
		}
		String ForecastMY = "";
		if (map.get("ForecastMY") != null) {
			ForecastMY = (String) map.get("ForecastMY");
		}
//		String PlanSystemDate = "";
//		if (map.get("PlanSystemDate") != null) {
//			java.util.Date dateStr = (Date)map.get("PlanSystemDate");
//			PlanSystemDate = this.sdf2.format(dateStr);
//		}
		String FirstSORDueDate = "";
		if (map.get("FirstSORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("FirstSORDueDate");
			FirstSORDueDate = this.sdf2.format(dateStr);
		}

		String FirstApprovedDate = "";
		if (map.get("FirstApprovedDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("FirstApprovedDate");
			FirstApprovedDate = this.sdf3.format(timestamp1);
		}
		String FirstApprovedBy = "";
		if (map.get("FirstApprovedBy") != null) {
			FirstApprovedBy = (String) map.get("FirstApprovedBy");
		}

		String LastSORDueDate = "";
		if (map.get("LastSORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("LastSORDueDate");
			LastSORDueDate = this.sdf2.format(dateStr);
		}
		String LastApprovedDate = "";
		if (map.get("LastApprovedDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("LastApprovedDate");
			LastApprovedDate = this.sdf3.format(timestamp1);
		}
		String LastApprovedBy = "";
		if (map.get("LastApprovedBy") != null) {
			LastApprovedBy = (String) map.get("LastApprovedBy");
		}
		String CurrentSORDueDate = "";
		if (map.get("CurrentSORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("CurrentSORDueDate");
			CurrentSORDueDate = this.sdf2.format(dateStr);
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String PlanCFMDate = "";
		if (map.get("PlanCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanCFMDate");
			PlanCFMDate = this.sdf2.format(dateStr);
		}
		String PlanDueDate = "";
		if (map.get("PlanDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanDueDate");
			PlanDueDate = this.sdf2.format(dateStr);
		}
		String SORCFMDate = "";
		if (map.get("SORCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORCFMDate");
			SORCFMDate = this.sdf2.format(dateStr);
		}
		String SORDueDate = "";
		if (map.get("SORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORDueDate");
			SORDueDate = this.sdf2.format(dateStr);
		}
		return new InputApprovedFCDetail(ForecastId, ForecastNo, ForecastMY, FirstSORDueDate, FirstApprovedDate, FirstApprovedBy,
				LastSORDueDate, LastApprovedDate, LastApprovedBy, CurrentSORDueDate, Remark, ProductionOrder, PlanCFMDate,
				PlanDueDate, SORCFMDate, SORDueDate, CustomerName);
	}

	@Override
	public InputArticleConversionDetail _genArticleConversionDetail(Map<String, Object> map)
	{
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String FormulaPC = "";
		double DBFormulaPC = 1;
		if (map.get("FormulaPC") != null) {
			BigDecimal value = (BigDecimal) map.get("FormulaPC");
			DBFormulaPC = value.doubleValue();
			FormulaPC = formatter.format(DBFormulaPC);
		}
		String FormulaKG = "";
		double DBFormulaKG = 1;
		if (map.get("FormulaKG") != null) {
			BigDecimal value = (BigDecimal) map.get("FormulaKG");
			DBFormulaKG = value.doubleValue();
			FormulaKG = formatter.format(DBFormulaKG);
		}
		String FormulaYD = "";
		double DBFormulaYD = 1;
		if (map.get("FormulaYD") != null) {
			BigDecimal value = (BigDecimal) map.get("FormulaYD");
			DBFormulaYD = value.doubleValue();
			FormulaYD = formatter.format(DBFormulaYD);
		}
		String FormulaMR = "";
		double DBFormulaMR = 1;
		if (map.get("FormulaMR") != null) {
			BigDecimal value = (BigDecimal) map.get("FormulaMR");
			DBFormulaMR = value.doubleValue();
			FormulaMR = formatter.format(DBFormulaMR);
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String QtyAdded = "";
		double DBQtyAdded = 0;
		if (map.get("QtyAdded") != null) {
			BigDecimal value = (BigDecimal) map.get("QtyAdded");
			DBQtyAdded = value.doubleValue();
			QtyAdded = formatter.format(DBQtyAdded);
		}
		return new InputArticleConversionDetail(No, Article, FormulaPC, FormulaYD, FormulaKG, FormulaMR, ChangeDate, ChangeBy,
				DataStatus, DBFormulaPC, DBFormulaYD, DBFormulaKG, DBFormulaMR, QtyAdded, DBQtyAdded);
	}

	// @Override
//	public PlanningDyeingDetail _genOrderDetailNew(Map<String, Object> map) {
//		String dueDate = "";
//		if (map.get("DueDate") != null) {
//			java.util.Date dateStr = (Date) map.get("DueDate");
//			dueDate = sdf2.format(dateStr);
//		}
//		String prdOrder = "";
//		if (map.get("ProductionOrder") != null) {
//			prdOrder = (String) map.get("ProductionOrder");
//		}
//		String lotNo = "";
//		if (map.get("LotNo") != null) {
//			lotNo = (String) map.get("LotNo");
//		}
//		String remarkStr = "";
//		if (map.get("Reason") != null) {
//			remarkStr = (String) map.get("Reason");
//		}
//		String GroupIdStr = "";
//		if (map.get("GroupPlanningDetailId") != null) {
//			GroupIdStr = (String) map.get("GroupPlanningDetailId");
//		}
//		String CustomerMatStr = "";
//		if (map.get("CustomerMat") != null) {
//			CustomerMatStr = (String) map.get("CustomerMat");
//		}
//		String SORDueDate = "";
//		if (map.get("SORDueDate") != null) {
//			java.util.Date dateStr = (Date) map.get("SORDueDate");
//			SORDueDate = sdf2.format(dateStr);
//		}
//		String matNumber = "";
//		if (map.get("MaterialNumber") != null) {
//			matNumber = (String) map.get("MaterialNumber");
//		}
//		String workCenter = "";
//		if (map.get("WorkCenter") != null) {
//			workCenter = (String) map.get("WorkCenter");
//		}
//		String CustomerName = "";
//		if (map.get("CustomerName") != null) {
//			CustomerName = (String) map.get("CustomerName");
//		}
//		double QuantityMR = 0;
//		if (map.get("QuantityMR") != null) {
//			QuantityMR = (double) map.get("QuantityMR");
//		}
//		double QuantityKG = 0;
//		if (map.get("QuantityKG") != null) {
//			QuantityKG = (double) map.get("QuantityKG");
//		}
//		int id = 0;
//		if (map.get("id") != null) {
//			id = (int) map.get("id");
//		}
//		int operation = 0;
//		if (map.get("Operation") != null) {
//			operation = (int) map.get("Operation");
//		}
//		String labStatus = "";
//		if (map.get("LabStatus") != null) {
//			labStatus = (String) map.get("LabStatus");
//		}
//		String userStatus = "";
//		if (map.get("UserStatus") != null) {
//			userStatus = (String) map.get("UserStatus");
//		}
//		String workTime = "";
//		if (map.get("WorkDate") != null) {
//			java.util.Date dateStr = (Date) map.get("WorkDate");
//			workTime = sdf2.format(dateStr);
//		}
//		String Priority = "";
//		if (map.get("Priority") != null) {
//			Priority = (String) map.get("Priority");
//		}
//		String customerDue = "";
//		if (map.get("CustomerDue") != null) {
//			java.util.Date dateStr = (Date) map.get("CustomerDue");
//			customerDue = sdf2.format(dateStr);
//		}
//		String greigeDue = "";
//		if (map.get("GreigeDue") != null) {
//			java.util.Date dateStr = (Date) map.get("GreigeDue");
//			greigeDue = sdf2.format(dateStr);
//		}
//		String priorUser = Priority + ":" + userStatus;
//
//		String ProductionOrderStatus = "";
//		if (map.get("ProductionOrderStatus") != null) {
//			ProductionOrderStatus = (String) map.get("ProductionOrderStatus");
//		}
//		PlanningDyeingDetail bean = new PlanningDyeingDetail("", id, prdOrder, matNumber, CustomerMatStr, workCenter, dueDate, CustomerName,
//				QuantityKG, QuantityMR, priorUser, labStatus, SORDueDate, GroupIdStr, remarkStr, "", operation, workTime,
//				"", "",lotNo,customerDue,greigeDue);
//		bean.setProductionOrderStatus(ProductionOrderStatus);
//		return bean;
//	}
	@Override
	public InputArticleDetail _genArticleDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
//		Double doubleRemainQ = 0.00;
//		if (map.get("RemainQuantity") != null) {
//			BigDecimal value = (BigDecimal) map.get("RemainQuantity");
//			doubleRemainQ = value.doubleValue();
//			RemainQuantity = formatter.format(doubleRemainQ);
//		}
//		String ChangeBy = "";
//		if (map.get("ChangeBy") != null) {
//			ChangeBy = (String) map.get("ChangeBy");
//		}
//		String ChangeDate = "";
//		if (map.get("ChangeDate") != null) {
//		    Timestamp timestamp1 = (Timestamp)map.get("ChangeDate");
//		    ChangeDate = this.sdf3.format(timestamp1);
//		}
//		if (map.get("GreigeInDate") != null) {
//			java.util.Date dateStr = (Date) map.get("GreigeInDate");
//			GreigeInDate = sdf2.format(dateStr);
//		}
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String Division = "";
		if (map.get("Division") != null) {
			Division = (String) map.get("Division");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String Code = "";
		if (map.get("Code") != null) {
			Code = (String) map.get("Code");
		}
		String Fiber = "";
		if (map.get("Fiber") != null) {
			Fiber = (String) map.get("Fiber");
		}
		String IsCotton = "";
		if (map.get("IsCotton") != null) {
			IsCotton = (String) map.get("IsCotton");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String FormulaPC = "";
		double DBFormulaPC = 1;
		if (map.get("FormulaPC") != null) {
			BigDecimal value = (BigDecimal) map.get("FormulaPC");
			DBFormulaPC = value.doubleValue();
			FormulaPC = formatter.format(DBFormulaPC);
		}
		String FormulaKG = "";
		double DBFormulaKG = 1;
		if (map.get("FormulaKG") != null) {
			BigDecimal value = (BigDecimal) map.get("FormulaKG");
			DBFormulaKG = value.doubleValue();
			FormulaKG = formatter.format(DBFormulaKG);
		}
		String FormulaYD = "";
		double DBFormulaYD = 1;
		if (map.get("FormulaYD") != null) {
			BigDecimal value = (BigDecimal) map.get("FormulaYD");
			DBFormulaYD = value.doubleValue();
			FormulaYD = formatter.format(DBFormulaYD);
		}
		String FormulaMR = "";
		double DBFormulaMR = 1;
		if (map.get("FormulaMR") != null) {
			BigDecimal value = (BigDecimal) map.get("FormulaMR");
			DBFormulaMR = value.doubleValue();
			FormulaMR = formatter.format(DBFormulaMR);
		}
		String QtyAdded = "";
		double DBQtyAdded = 0;
		if (map.get("QtyAdded") != null) {
			BigDecimal value = (BigDecimal) map.get("QtyAdded");
			DBQtyAdded = value.doubleValue();
			QtyAdded = this.df2.format(DBQtyAdded);
		}

		String ArticleGreige = "";
		if (map.get("ArticleGreige") != null) {
			ArticleGreige = (String) map.get("ArticleGreige");
		}
		String DesignGreige = "";
		if (map.get("DesignGreige") != null) {
			DesignGreige = (String) map.get("DesignGreige");
		}
		String QtyGreigeMR = "";
		int IntQtyGreigeMR = 0;
		if (map.get("QtyGreigeMR") != null) {
			int value = (int) map.get("QtyGreigeMR");
			IntQtyGreigeMR = value;
			QtyGreigeMR = Integer.toString(IntQtyGreigeMR);
		}
		String BeamType = "";
		if (map.get("BeamType") != null) {
			BeamType = (String) map.get("BeamType");
		}
		int SpecialCaseId = 0;
		if (map.get("SpecialCaseId") != null) {
			SpecialCaseId = (int) map.get("SpecialCaseId");
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String ArticleComment = "";
		if (map.get("ArticleComment") != null) {
			ArticleComment = (String) map.get("ArticleComment");
		}
		boolean isQtyGreigeForPO = false;
		if (map.get("IsQtyGreigeForPO") != null) {
			isQtyGreigeForPO = (boolean) map.get("IsQtyGreigeForPO");
		}
		String ArticleReplaced = "";
		if (map.get("ArticleReplaced") != null) {
			ArticleReplaced = (String) map.get("ArticleReplaced");
		}
		InputArticleDetail bean = new InputArticleDetail(No, Division, Article, Code, Fiber, ChangeDate, ChangeBy, DataStatus,
				IsCotton, FormulaPC, FormulaYD, FormulaKG, FormulaMR, DBFormulaPC, DBFormulaYD, DBFormulaKG, DBFormulaMR,
				QtyAdded, DBQtyAdded, ArticleGreige, DesignGreige, QtyGreigeMR, BeamType, SpecialCaseId, Remark, ArticleComment,
				isQtyGreigeForPO, ArticleReplaced);

		return bean;
	}

	@Override
	public InputBKKHolidayDetail _genBKKHolidayDetail(Map<String, Object> map)
	{
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String Title = "";
		if (map.get("Title") != null) {
			Title = (String) map.get("Title");
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String StartDate = "";
		if (map.get("StartDate") != null) {
			java.util.Date dateStr = (Date) map.get("StartDate");
			StartDate = sdf2.format(dateStr);
		}
		String EndDate = "";
		if (map.get("EndDate") != null) {
			java.util.Date dateStr = (Date) map.get("EndDate");
			EndDate = sdf2.format(dateStr);
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String DataStatus = "O";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		return new InputBKKHolidayDetail(No, Title, Remark, StartDate, EndDate, ChangeDate, ChangeBy, DataStatus);
	}

	@Override
	public CFMDetail _genCFMDetail(Map<String, Object> map)
	{
		int Id = 0;
		if (map.get("Id") != null) {
//			Id = Integer.toString((int) map.get("Id"));
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
		String DE = "";
		if (map.get("DE") != null) {
			DE = (String) map.get("DE");
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
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String CreateDate = "";
		if (map.get("CreateDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("CreateDate");
			CreateDate = this.sdf3.format(timestamp1);
		}
		String CreateBy = "";
		if (map.get("CreateBy") != null) {
			CreateBy = (String) map.get("CreateBy");
		}
		// TODO Auto-generated method stub
		return new CFMDetail(Id, ProductionOrder, CFMNo, CFMNumber, CFMSendDate, RollNo, RollNoRemark, L, Da, Db, ST, DE,
				SaleOrder, SaleLine, Color, CFMAnswerDate, CFMStatus, CFMRemark, NextLot, SOChange, SOChangeQty, SOChangeUnit,
				ChangeDate, ChangeBy, CreateDate, CreateBy);
	}

	@Override
	public ChangeLogDetail _genChangeLogDetail(Map<String, Object> map)
	{

		String Description = "";
		if (map.get("Description") != null) {
			Description = (String) map.get("Description");
		}
		String DescriptionTH = "";
		if (map.get("DescriptionTH") != null) {
			DescriptionTH = (String) map.get("DescriptionTH");
		}
		String FieldName = "";
		if (map.get("FieldName") != null) {
			FieldName = (String) map.get("FieldName");
		}

		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String Operation = "";
		if (map.get("Operation") != null) {
			Operation = Integer.toString((int) map.get("Operation"));
		}
		String PO = "";
		if (map.get("PO") != null) {
			PO = (String) map.get("PO");
		}
		String POLine = "";
		if (map.get("POLine") != null) {
			POLine = (String) map.get("POLine");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}

		String MachineName = "";
		if (map.get("MachineName") != null) {
			MachineName = (String) map.get("MachineName");
		}

		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String CreateBy = "";
		if (map.get("CreateBy") != null) {
			CreateBy = (String) map.get("CreateBy");
		}
		String CreateDate = "";
		if (map.get("CreateDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("CreateDate");
			CreateDate = this.sdf3.format(timestamp1);
		}
		String StartDate = "";
		if (map.get("StartDate") != null) {
			java.util.Date dateStr = (Date) map.get("StartDate");
			StartDate = sdf2.format(dateStr);
		}
		String OldValue = "";
		if (map.get("OldValue") != null) {
			OldValue = (String) map.get("OldValue");
		}
		String NewValue = "";
		if (map.get("NewValue") != null) {
			NewValue = (String) map.get("NewValue");
		}

		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String Division = "";
		if (map.get("Division") != null) {
			Division = (String) map.get("Division");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}

		String POQty = "";
		if (map.get("POQty") != null) {
			BigDecimal value = (BigDecimal) map.get("POQty");
			double doubleVal = value.doubleValue();
			POQty = this.df2.format(doubleVal);
		}
		String POUnit = "";
		if (map.get("POUnit") != null) {
			POUnit = (String) map.get("POUnit");
		}
		String LabNo = "";
		if (map.get("LabNo") != null) {
			LabNo = (String) map.get("LabNo");
		}
		String CustomerMaterial = "";
		if (map.get("CustomerMaterial") != null) {
			CustomerMaterial = (String) map.get("CustomerMaterial");
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date timestamp1 = (Date) map.get("CustomerDue");
			CustomerDue = this.sdf2.format(timestamp1);
		}
		String GreigePlan = "";
		if (map.get("GreigePlan") != null) {
			java.util.Date timestamp1 = (Date) map.get("GreigePlan");
			GreigePlan = this.sdf2.format(timestamp1);
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}

		String logType = "";
		if (map.get("LogType") != null) {
			logType = (String) map.get("LogType");
		}
		String poType = "";
		if (map.get("POType") != null) {
			poType = (String) map.get("POType");
		}
		String planSystemDate = "";
		if (map.get("PlanSystemDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanSystemDate");
			planSystemDate = this.sdf2.format(dateStr);
		}
		String planUserDate = "";
		if (map.get("PlanUserDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanUserDate");
			planUserDate = this.sdf2.format(dateStr);
		}
		String planUserChangeDate = "";
		if (map.get("PlanUserChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("PlanUserChangeDate");
			planUserChangeDate = this.sdf3.format(timestamp1);
		}
		String planBy = "";
		if (map.get("PlanBy") != null) {
			planBy = (String) map.get("PlanBy");
		}
		String changeRemark = "";
		if (map.get("ChangeRemark") != null) {
			changeRemark = (String) map.get("ChangeRemark");
		}

		String labRef = "";
		String labRefLotNo = "";
		String itemNote = "";
		String productionMemo = "";
		String modelCode = "";
		String poDate = "";
		String updateBy = "";
		String updateDate = "";
		String distChannal = "";
		if (map.get("DistChannal") != null) {
			distChannal = (String) map.get("DistChannal");
		}
		String design = "";
		if (map.get("Design") != null) {
			design = (String) map.get("Design");
		}
		String colorCustomer = "";
		if (map.get("ColorCustomer") != null) {
			colorCustomer = (String) map.get("ColorCustomer");
		}
		String color = "";
		if (map.get("Color") != null) {
			color = (String) map.get("Color");
		}
		if (map.get("LabRef") != null) {
			labRef = (String) map.get("LabRef");
		}
		if (map.get("ItemNote") != null) {
			itemNote = (String) map.get("ItemNote");
		}
		if (map.get("ProductionMemo") != null) {
			productionMemo = (String) map.get("ProductionMemo");
		}
		if (map.get("ModelCode") != null) {
			modelCode = (String) map.get("ModelCode");
		}
		if (map.get("LabRefLotNo") != null) {
			labRefLotNo = (String) map.get("LabRefLotNo");
		}

		if (map.get("PODate") != null) {
			java.util.Date dateStr = (Date) map.get("PODate");
			poDate = sdf2.format(dateStr);
		}

		if (map.get("UpdateDate") != null) {
			java.util.Date dateStr = (Date) map.get("UpdateDate");
			updateDate = sdf2.format(dateStr);
		}
		if (map.get("UpdateBy") != null) {
			updateBy = (String) map.get("UpdateBy");
		}
		boolean IsUpdate = false;
		if (map.get("IsUpdate") != null) {
			IsUpdate = (boolean) map.get("IsUpdate");
		}
		boolean IsDelete = false;
		if (map.get("IsDelete") != null) {
			IsDelete = (boolean) map.get("IsDelete");
		}
		String actionTime = "";
		if (map.get("ActionTime") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ActionTime");
			actionTime = this.sdf3.format(timestamp1);
		}
		String materialNoTWC = "";
		if (map.get("MaterialNoTWC") != null) {
			materialNoTWC = (String) map.get("MaterialNoTWC");
		}
		return new ChangeLogDetail(FieldName, OldValue, NewValue, CreateDate, CreateBy, Description, DescriptionTH,
				ProductionOrder, PO, POLine, Article, GroupNo, SubGroup, StartDate, UserStatus, LabStatus, MachineName,
				CustomerNo, CustomerName, Operation, Remark, SaleOrder, SaleLine, Division, MaterialNo, POQty, POUnit, LabNo,
				CustomerMaterial, CustomerDue, GreigePlan, ChangeDate, logType, poType, planSystemDate, planUserDate,
				planUserChangeDate, planBy, changeRemark,

				distChannal, design, colorCustomer, color, labRef, labRefLotNo, itemNote, productionMemo, modelCode, poDate,
				updateBy, updateDate, IsUpdate, IsDelete, actionTime, materialNoTWC);
	}

	@Override
	public ChangeSettingLogDetail _genChangeSettingLogDetail(Map<String, Object> map)
	{
		String Id = "";
		if (map.get("Id") != null) {
			int value = (int) map.get("Id");
			Id = Integer.toString(value);
		}
		String MasterSettingChangeId = "";
		if (map.get("MasterSettingChangeId") != null) {
			MasterSettingChangeId = (String) map.get("MasterSettingChangeId");
		}
		String TableName = "";
		if (map.get("TableName") != null) {
			TableName = (String) map.get("TableName");
		}
		String FieldName = "";
		if (map.get("FieldName") != null) {
			FieldName = (String) map.get("FieldName");
		}
		return null;
	}

	@Override
	public CSO_SOASDetail _genCSO_SOASDetail(Map<String, Object> map)
	{
		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}
		int ROW_NUM = 0;
		if (map.get("ROW_NUM") != null) {
			ROW_NUM = (int) map.get("ROW_NUM");
		}
		String SALES_ORDER_NO = "";
		if (map.get("SALES_ORDER_NO") != null) {
			SALES_ORDER_NO = (String) map.get("SALES_ORDER_NO");
		}
		String PURCH_NO = "";
		if (map.get("PURCH_NO") != null) {
			PURCH_NO = (String) map.get("PURCH_NO");
		}
		String DIVISION = "";
		if (map.get("DIVISION") != null) {
			DIVISION = (String) map.get("DIVISION");
		}
		String DOC_ITEM = "";
		if (map.get("DOC_ITEM") != null) {
			DOC_ITEM = (String) map.get("DOC_ITEM");
		}
		String MATERIAL = "";
		if (map.get("MATERIAL") != null) {
			MATERIAL = (String) map.get("MATERIAL");
		}
		BigDecimal ORD_QTY = null;
//		if (map.get("ORD_QTY") != null) {
//			ORD_QTY = (double) map.get("ORD_QTY");
//		}
		if (map.get("ORD_QTY") != null) {
			ORD_QTY = (BigDecimal) map.get("ORD_QTY");
		}
		String BASE_UNIT = "";
		if (map.get("BASE_UNIT") != null) {
			BASE_UNIT = (String) map.get("BASE_UNIT");
		}
		String FIELD_CODE = "";
		if (map.get("FIELD_CODE") != null) {
			FIELD_CODE = (String) map.get("FIELD_CODE");
		}
		String OLD_VALUE = "";
		if (map.get("OLD_VALUE") != null) {
			OLD_VALUE = (String) map.get("OLD_VALUE");
		}
		String NEW_VALUE = "";
		if (map.get("NEW_VALUE") != null) {
			NEW_VALUE = (String) map.get("NEW_VALUE");
		}
		Timestamp LASTCHANGED_ON = null;
		if (map.get("LASTCHANGED_ON") != null) {
			LASTCHANGED_ON = (Timestamp) map.get("LASTCHANGED_ON");
		}

		String SOLD_TO = "";
		if (map.get("SOLD_TO") != null) {
			SOLD_TO = (String) map.get("SOLD_TO");
		}
		int CHANGE_ORDER_ID = 0;
		if (map.get("CHANGE_ORDER_ID") != null) {
			CHANGE_ORDER_ID = (int) map.get("CHANGE_ORDER_ID");
		}
		String LAB_NO = "";
		if (map.get("LAB_NO") != null) {
			LAB_NO = (String) map.get("LAB_NO");
		}
		String CUST_MAT = "";
		if (map.get("CUST_MAT") != null) {
			CUST_MAT = (String) map.get("CUST_MAT");
		}
		Timestamp SHIPDATE = null;
		if (map.get("SHIPDATE") != null) {
			SHIPDATE = (Timestamp) map.get("SHIPDATE");
		}
		String DUE_SUBMIT_DATE = "";
		if (map.get("DUE_SUBMIT_DATE") != null) {
			DUE_SUBMIT_DATE = (String) map.get("DUE_SUBMIT_DATE");
		}
		String DUE_GREIGE = "";
		if (map.get("DUE_GREIGE") != null) {
			DUE_GREIGE = (String) map.get("DUE_GREIGE");
		}
		return new CSO_SOASDetail(Id, ROW_NUM, SALES_ORDER_NO, PURCH_NO, DIVISION, DOC_ITEM, MATERIAL, ORD_QTY, BASE_UNIT,
				FIELD_CODE, OLD_VALUE, NEW_VALUE, LASTCHANGED_ON, SOLD_TO, CHANGE_ORDER_ID, LAB_NO, CUST_MAT, SHIPDATE,
				DUE_SUBMIT_DATE, DUE_GREIGE);
	}

	@Override
	public CustomerDetail _genCustomerDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub

		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerShortName = "";
		if (map.get("CustomerShortName") != null) {
			CustomerShortName = (String) map.get("CustomerShortName");
		}
		String CustomerGroup = "";
		if (map.get("CustomerGroup") != null) {
			CustomerGroup = (String) map.get("CustomerGroup");
		}
		String CustomerType = "";
		if (map.get("CustomerType") != null) {
			CustomerType = (String) map.get("CustomerType");
		}
		String DistChannal = "";
		if (map.get("DistChannal") != null) {
			DistChannal = (String) map.get("DistChannal");
		}
		return new CustomerDetail(CustomerNo, CustomerName, CustomerShortName, CustomerGroup, CustomerType, DistChannal);
	}

	@Override
	public CustomerMonthlyReportDetail _genCustomerMonthlyReportDetail(Map<String, Object> map)
	{
//		Double doubleRemainQ = 0.00;
//		if (map.get("RemainQuantity") != null) {
//			BigDecimal value = (BigDecimal) map.get("RemainQuantity");
//			doubleRemainQ = value.doubleValue();
//			RemainQuantity = formatter.format(doubleRemainQ);
//		}
//		String ChangeBy = "";
//		if (map.get("ChangeBy") != null) {
//			ChangeBy = (String) map.get("ChangeBy");
//		}
//		String ChangeDate = "";
//		if (map.get("ChangeDate") != null) {
//		    Timestamp timestamp1 = (Timestamp)map.get("ChangeDate");
//		    ChangeDate = this.sdf3.format(timestamp1);
//		}
//		if (map.get("GreigeInDate") != null) {
//			java.util.Date dateStr = (Date) map.get("GreigeInDate");
//			GreigeInDate = sdf2.format(dateStr);
//		}
		String POType = "";
		if (map.get("POType") != null) {
			POType = (String) map.get("POType");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String SumTotal = "";
		if (map.get("SumTotal") != null) {
			BigDecimal value = (BigDecimal) map.get("SumTotal");
			double db_value = value.doubleValue();
			SumTotal = this.df2.format(db_value);
		}
		String SumOrder = "";
		if (map.get("SumOrder") != null) {
			BigDecimal value = (BigDecimal) map.get("SumOrder");
			double db_value = value.doubleValue();
			SumOrder = this.df2.format(db_value);
		}
		String SumPlan = "";
		if (map.get("SumPlan") != null) {
			BigDecimal value = (BigDecimal) map.get("SumPlan");
			double db_value = value.doubleValue();
			SumPlan = this.df2.format(db_value);
		}
		String SumForecast = "";
		if (map.get("SumForecast") != null) {
			BigDecimal value = (BigDecimal) map.get("SumForecast");
			double db_value = value.doubleValue();
			SumForecast = this.df2.format(db_value);
		}
		String NextSumTotal = "";
		if (map.get("NextSumTotal") != null) {
			BigDecimal value = (BigDecimal) map.get("NextSumTotal");
			double db_value = value.doubleValue();
			NextSumTotal = this.df2.format(db_value);
		}
		String NextSumOrder = "";
		if (map.get("NextSumOrder") != null) {
			BigDecimal value = (BigDecimal) map.get("NextSumOrder");
			double db_value = value.doubleValue();
			NextSumOrder = this.df2.format(db_value);
		}
		String NextSumPlan = "";
		if (map.get("NextSumPlan") != null) {
			BigDecimal value = (BigDecimal) map.get("NextSumPlan");
			double db_value = value.doubleValue();
			NextSumPlan = this.df2.format(db_value);
		}
		String NextSumForecast = "";
		if (map.get("NextSumForecast") != null) {
			BigDecimal value = (BigDecimal) map.get("NextSumForecast");
			double db_value = value.doubleValue();
			NextSumForecast = this.df2.format(db_value);
		}
		CustomerMonthlyReportDetail bean = new CustomerMonthlyReportDetail(POType, CustomerNo, CustomerName, SumTotal, SumOrder,
				SumPlan, SumForecast, NextSumTotal, NextSumOrder, NextSumPlan, NextSumForecast);

		return bean;
	}

	@Override
	public InputFacHolidayDetail _genFacHolidayDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
//		Double doubleRemainQ = 0.00;
//		if (map.get("RemainQuantity") != null) {
//			BigDecimal value = (BigDecimal) map.get("RemainQuantity");
//			doubleRemainQ = value.doubleValue();
//			RemainQuantity = formatter.format(doubleRemainQ);
//		}
//		String ChangeBy = "";
//		if (map.get("ChangeBy") != null) {
//			ChangeBy = (String) map.get("ChangeBy");
//		}
//		String ChangeDate = "";
//		if (map.get("ChangeDate") != null) {
//		    Timestamp timestamp1 = (Timestamp)map.get("ChangeDate");
//		    ChangeDate = this.sdf3.format(timestamp1);
//		}
//		if (map.get("GreigeInDate") != null) {
//			java.util.Date dateStr = (Date) map.get("GreigeInDate");
//			GreigeInDate = sdf2.format(dateStr);
//		}
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String Title = "";
		if (map.get("Title") != null) {
			Title = (String) map.get("Title");
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String StartDate = "";
		if (map.get("StartDate") != null) {
			java.util.Date dateStr = (Date) map.get("StartDate");
			StartDate = sdf2.format(dateStr);
		}
		String EndDate = "";
		if (map.get("EndDate") != null) {
			java.util.Date dateStr = (Date) map.get("EndDate");
			EndDate = sdf2.format(dateStr);
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		return new InputFacHolidayDetail(No, Title, Remark, StartDate, EndDate, ChangeDate, ChangeBy, DataStatus);
	}

	@Override
	public InputFacWorkDateDetail _genFacWorkDateDetail(Map<String, Object> map)
	{
//		String No = "";
//		if (map.get("1") != null) {
//			No = Integer.toString((int) map.get("No"));
//		}
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		String D1 = "";
		if (map.get("1") != null) {
			D1 = (String) map.get("1");
		}
		String D2 = "";
		if (map.get("2") != null) {
			D2 = (String) map.get("2");
		}
		String D3 = "";
		if (map.get("3") != null) {
			D3 = (String) map.get("3");
		}
		String D4 = "";
		if (map.get("4") != null) {
			D4 = (String) map.get("4");
		}
		String D5 = "";
		if (map.get("5") != null) {
			D5 = (String) map.get("5");
		}
		String D6 = "";
		if (map.get("6") != null) {
			D6 = (String) map.get("6");
		}
		String D7 = "";
		if (map.get("7") != null) {
			D7 = (String) map.get("7");
		}
		String D8 = "";
		if (map.get("8") != null) {
			D8 = (String) map.get("8");
		}
		String D9 = "";
		if (map.get("9") != null) {
			D9 = (String) map.get("9");
		}
		String D10 = "";
		if (map.get("10") != null) {
			D10 = (String) map.get("10");
		}
		String D11 = "";
		if (map.get("11") != null) {
			D11 = (String) map.get("11");
		}
		String D12 = "";
		if (map.get("12") != null) {
			D12 = (String) map.get("12");
		}
		String D13 = "";
		if (map.get("13") != null) {
			D13 = (String) map.get("13");
		}
		String D14 = "";
		if (map.get("14") != null) {
			D14 = (String) map.get("14");
		}
		String D15 = "";
		if (map.get("15") != null) {
			D15 = (String) map.get("15");
		}
		String D16 = "";
		if (map.get("16") != null) {
			D16 = (String) map.get("16");
		}
		String D17 = "";
		if (map.get("17") != null) {
			D17 = (String) map.get("17");
		}
		String D18 = "";
		if (map.get("18") != null) {
			D18 = (String) map.get("18");
		}
		String D19 = "";
		if (map.get("19") != null) {
			D19 = (String) map.get("19");
		}
		String D20 = "";
		if (map.get("20") != null) {
			D20 = (String) map.get("20");
		}
		String D21 = "";
		if (map.get("21") != null) {
			D21 = (String) map.get("21");
		}
		String D22 = "";
		if (map.get("22") != null) {
			D22 = (String) map.get("22");
		}
		String D23 = "";
		if (map.get("23") != null) {
			D23 = (String) map.get("23");
		}
		String D24 = "";
		if (map.get("24") != null) {
			D24 = (String) map.get("24");
		}
		String D25 = "";
		if (map.get("25") != null) {
			D25 = (String) map.get("25");
		}
		String D26 = "";
		if (map.get("26") != null) {
			D26 = (String) map.get("26");
		}
		String D27 = "";
		if (map.get("27") != null) {
			D27 = (String) map.get("27");
		}
		String D28 = "";
		if (map.get("28") != null) {
			D28 = (String) map.get("28");
		}
		String D29 = "";
		if (map.get("29") != null) {
			D29 = (String) map.get("29");
		}
		String D30 = "";
		if (map.get("30") != null) {
			D30 = (String) map.get("30");
		}
		String D31 = "";
		if (map.get("31") != null) {
			D31 = (String) map.get("31");
		}

		String L1 = "";
		if (map.get("L1") != null) {
			L1 = (String) map.get("L1");
		}
		String L2 = "";
		if (map.get("L2") != null) {
			L2 = (String) map.get("L2");
		}
		String L3 = "";
		if (map.get("L3") != null) {
			L3 = (String) map.get("L3");
		}
		String L4 = "";
		if (map.get("L4") != null) {
			L4 = (String) map.get("L4");
		}
		String L5 = "";
		if (map.get("L5") != null) {
			L5 = (String) map.get("L5");
		}
		String L6 = "";
		if (map.get("L6") != null) {
			L6 = (String) map.get("L6");
		}
		String L7 = "";
		if (map.get("L7") != null) {
			L7 = (String) map.get("L7");
		}
		String L8 = "";
		if (map.get("L8") != null) {
			L8 = (String) map.get("L8");
		}
		String L9 = "";
		if (map.get("L9") != null) {
			L9 = (String) map.get("L9");
		}
		String L10 = "";
		if (map.get("L10") != null) {
			L10 = (String) map.get("L10");
		}
		String L11 = "";
		if (map.get("L11") != null) {
			L11 = (String) map.get("L11");
		}
		String L12 = "";
		if (map.get("L12") != null) {
			L12 = (String) map.get("L12");
		}
		String L13 = "";
		if (map.get("L13") != null) {
			L13 = (String) map.get("L13");
		}
		String L14 = "";
		if (map.get("L14") != null) {
			L14 = (String) map.get("L14");
		}
		String L15 = "";
		if (map.get("L15") != null) {
			L15 = (String) map.get("L15");
		}
		String L16 = "";
		if (map.get("L16") != null) {
			L16 = (String) map.get("L16");
		}
		String L17 = "";
		if (map.get("L17") != null) {
			L17 = (String) map.get("L17");
		}
		String L18 = "";
		if (map.get("L18") != null) {
			L18 = (String) map.get("L18");
		}
		String L19 = "";
		if (map.get("L19") != null) {
			L19 = (String) map.get("L19");
		}
		String L20 = "";
		if (map.get("L20") != null) {
			L20 = (String) map.get("L20");
		}
		String L21 = "";
		if (map.get("L21") != null) {
			L21 = (String) map.get("L21");
		}
		String L22 = "";
		if (map.get("L22") != null) {
			L22 = (String) map.get("L22");
		}
		String L23 = "";
		if (map.get("L23") != null) {
			L23 = (String) map.get("L23");
		}
		String L24 = "";
		if (map.get("L24") != null) {
			L24 = (String) map.get("L24");
		}
		String L25 = "";
		if (map.get("L25") != null) {
			L25 = (String) map.get("L25");
		}
		String L26 = "";
		if (map.get("L26") != null) {
			L26 = (String) map.get("L26");
		}
		String L27 = "";
		if (map.get("L27") != null) {
			L27 = (String) map.get("L27");
		}
		String L28 = "";
		if (map.get("L28") != null) {
			L28 = (String) map.get("L28");
		}
		String L29 = "";
		if (map.get("L29") != null) {
			L29 = (String) map.get("L29");
		}
		String L30 = "";
		if (map.get("L30") != null) {
			L30 = (String) map.get("L30");
		}
		String L31 = "";
		if (map.get("L31") != null) {
			L31 = (String) map.get("L31");
		}
		String ColMonthYear = "";
		if (map.get("ColMonthYear") != null) {
			ColMonthYear = (String) map.get("ColMonthYear");
		}
		String TotalDay = "";
		if (map.get("DaysInMonth") != null) {
//			TotalDay = (String) map.get("DaysInMonth");
			TotalDay = Integer.toString((int) map.get("DaysInMonth"));
		}
		String LotPerDaySubGroup = "";
		if (map.get("LotPerDaySubGroup") != null) {
			LotPerDaySubGroup = Integer.toString((int) map.get("LotPerDaySubGroup"));
		}
		boolean isLotPerDayZero = false;
		if (map.get("IsLotPerDayZero") != null) {
			isLotPerDayZero = (boolean) map.get("IsLotPerDayZero");
		}
		return new InputFacWorkDateDetail(GroupNo, SubGroup, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15,
				D16, D17, D18, D19, D20, D21, D22, D23, D24, D25, D26, D27, D28, D29, D30, D31, ColMonthYear, TotalDay, L1, L2,
				L3, L4, L5, L6, L7, L8, L9, L10, L11, L12, L13, L14, L15, L16, L17, L18, L19, L20, L21, L22, L23, L24, L25, L26,
				L27, L28, L29, L30, L31, isLotPerDayZero, LotPerDaySubGroup);
	}

	@Override
	public InputForecastDetail _genForecastDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String ForecastNo = "";
		if (map.get("ForecastNo") != null) {
			ForecastNo = (String) map.get("ForecastNo");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String ForecastMY = "";
		if (map.get("ForecastMY") != null) {
			ForecastMY = (String) map.get("ForecastMY");
		}

		String Unit = "";
		if (map.get("Unit") != null) {
			Unit = (String) map.get("Unit");
		}
		String DocDate = "";
		if (map.get("DocDate") != null) {
			java.util.Date dateStr = (Date) map.get("DocDate");
			DocDate = sdf2.format(dateStr);
		}

		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		int CountProd = 0;
		if (map.get("CountProd") != null) {
			CountProd = (int) map.get("CountProd");
		}
		int CheckDivision = 0;
		if (map.get("CheckDivision") != null) {
			CheckDivision = (int) map.get("CheckDivision");
		}
		int CheckArticle = 0;
		if (map.get("CheckArticle") != null) {
			CheckArticle = (int) map.get("CheckArticle");
		}
		int CheckArticleWeight = 0;
		if (map.get("CheckArticleWeight") != null) {
			CheckArticleWeight = (int) map.get("CheckArticleWeight");
		}
		int totalAll = 0;
		if (map.get("totalAll") != null) {
			totalAll = (int) map.get("totalAll");
		}

		int totalError = 0;
		if (map.get("totalError") != null) {
			totalError = (int) map.get("totalError");
		}

		String Remain = "";
		double DBRemain = 0;
		if (map.get("Remain") != null) {
			BigDecimal value = (BigDecimal) map.get("Remain");
			DBRemain = value.doubleValue();
			Remain = this.df2.format(DBRemain);
		}

		String RemainNonBLQty = "";
		double DBRemainNonBLQty = 0;
		if (map.get("RemainNonBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainNonBLQty");
			DBRemainNonBLQty = value.doubleValue();
			RemainNonBLQty = this.df2.format(DBRemainNonBLQty);
		}

		String RemainBLQty = "";
		double DBRemainBLQty = 0;
		if (map.get("RemainBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainBLQty");
			DBRemainBLQty = value.doubleValue();
			RemainBLQty = this.df2.format(DBRemainBLQty);
		}
		String TotalForecastQty = "";
		if (map.get("TotalForecastQty") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalForecastQty");
			double doubleVal = value.doubleValue();
			TotalForecastQty = this.df2.format(doubleVal);
		}
		String ForecastBLQty = "";
		if (map.get("ForecastBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ForecastBLQty");
			double doubleVal = value.doubleValue();
			ForecastBLQty = this.df2.format(doubleVal);
		}
		String ForecastNonBLQty = "";
		if (map.get("ForecastNonBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ForecastNonBLQty");
			double doubleVal = value.doubleValue();
			ForecastNonBLQty = this.df2.format(doubleVal);
		}
		int totalPending = totalAll-totalError;
		InputForecastDetail bean = new InputForecastDetail(No, ForecastNo, CustomerNo, CustomerName, ForecastMY, Unit, DocDate,
				ChangeBy, ChangeDate, CountProd, CheckDivision, CheckArticle, CheckArticleWeight, Remain, DBRemain, RemainBLQty,
				DBRemainBLQty, RemainNonBLQty, DBRemainNonBLQty, TotalForecastQty, ForecastBLQty, ForecastNonBLQty);

		bean.setTotalAll(totalAll);
		bean.setTotalPending(totalPending);
		bean.setTotalError(totalError);// here
		return bean;
	}

	@Override
	public ForecastRunningDetail _genForecastRunningDetail(Map<String, Object> map)
	{
		String Year = "";
		if (map.get("Year") != null) {
			Year = (String) map.get("Year");
		}

		int RunningNo = 0;
		if (map.get("RunningNo") != null) {
			RunningNo = (int) map.get("RunningNo");
		}
		return new ForecastRunningDetail(Year, RunningNo);
	}

	@Override
	public ForecastStatusReportDetail _genForecastStatusReportDetail(Map<String, Object> map)
	{

		String ApprovedDate = "";
		if (map.get("ApprovedDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ApprovedDate");
			ApprovedDate = this.sdf3.format(timestamp1);
		}
		String ForecastNo = "";
		if (map.get("ForecastNo") != null) {
			ForecastNo = (String) map.get("ForecastNo");
		}
		String ForecastMY = "";
		if (map.get("ForecastMY") != null) {
			ForecastMY = (String) map.get("ForecastMY");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String FirstLot = "";
		if (map.get("FirstLot") != null) {
			FirstLot = (String) map.get("FirstLot");
		}
		String PPMMStatus = "";
		if (map.get("PPMMStatus") != null) {
			PPMMStatus = (String) map.get("PPMMStatus");
		}
		String ForecastDate = "";
		if (map.get("DocDate") != null) {
			java.util.Date dateStr = (Date) map.get("DocDate");
			ForecastDate = sdf2.format(dateStr);
		}

		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String ProdOrderQty = "";
		double DBProdQty = 0;
		if (map.get("ProdOrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ProdOrderQty");
			DBProdQty = value.doubleValue();
			ProdOrderQty = this.df2.format(DBProdQty);

		}
		String RemainNonBLQty = "";
		double DBRemainNonBLQty = 0;
		if (map.get("RemainNonBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainNonBLQty");
			DBRemainNonBLQty = value.doubleValue();
			RemainNonBLQty = this.df2.format(DBRemainNonBLQty);
		}

		String RemainBLQty = "";
		double DBRemainBLQty = 0;
		if (map.get("RemainBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainBLQty");
			DBRemainBLQty = value.doubleValue();
			RemainBLQty = this.df2.format(DBRemainBLQty);
		}
		String TotalForecastQty = "";
		if (map.get("TotalForecastQty") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalForecastQty");
			double doubleVal = value.doubleValue();
			TotalForecastQty = this.df2.format(doubleVal);
		}
		String ForecastBLQty = "";
		if (map.get("ForecastBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ForecastBLQty");
			double doubleVal = value.doubleValue();
			ForecastBLQty = this.df2.format(doubleVal);
		}
		String ForecastNonBLQty = "";
		if (map.get("ForecastNonBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ForecastNonBLQty");
			double doubleVal = value.doubleValue();
			ForecastNonBLQty = this.df2.format(doubleVal);
		}
		int TotalFC = 0;
		if (map.get("TotalFC") != null) {
			TotalFC = (int) map.get("TotalFC");
		}
		return new ForecastStatusReportDetail(ApprovedDate, ForecastNo, ForecastMY, CustomerName, TotalForecastQty, ForecastBLQty,
				ForecastNonBLQty, ProductionOrder, ProdOrderQty, FirstLot, PPMMStatus, ForecastDate, RemainBLQty, RemainNonBLQty,
				TotalFC);

	}

	@Override
	public InputGroupDetail _genGroupDetail(Map<String, Object> map)
	{
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		String MachineName = "";
		if (map.get("MachineName") != null) {
			MachineName = (String) map.get("MachineName");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String LotMinMax = "";
		if (map.get("LotMinMax") != null) {
			LotMinMax = (String) map.get("LotMinMax");
		}
		String LotDif = "";
		if (map.get("LotDif") != null) {
			LotDif = (String) map.get("LotDif");
		}
		String IsOverCap = "";
		if (map.get("IsOverCap") != null) {
			IsOverCap = (String) map.get("IsOverCap");
		}
		String OverCapQty = "";
		if (map.get("OverCapQty") != null) {
			OverCapQty = (String) map.get("OverCapQty");
		}
//		String ChangeDate = "";
//		if (map.get("ChangeDate") != null) {
//		    Timestamp timestamp1 = (Timestamp)map.get("ChangeDate");
//		    ChangeDate = this.sdf3.format(timestamp1);
//		}
//		String ChangeBy = "";
//		if (map.get("ChangeBy") != null) {
//			ChangeBy = (String) map.get("ChangeBy");
//		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String Description = "";
		if (map.get("Description") != null) {
			Description = (String) map.get("Description");
		}
		String LotPerDay = "";
		if (map.get("LotPerDay") != null) {
			LotPerDay = Integer.toString((Integer) map.get("LotPerDay"));
		}
		String Division = "";
		if (map.get("Division") != null) {
			Division = (String) map.get("Division");
		}
		String ColorType = "";
		if (map.get("ColorType") != null) {
			ColorType = (String) map.get("ColorType");
		}
		String GroupType = "";
		if (map.get("GroupType") != null) {
			GroupType = (String) map.get("GroupType");
		}
		double DBLotQtyMax = 0.00;
		double DBLotQtyMin = 0.00;
		String[] splitArray;
		String IsMaxOnly = "N";
		if ( ! LotMinMax.equals("")) {
			splitArray = LotMinMax.split("-");
			if (splitArray.length == 1) {
				IsMaxOnly = "Y";
				DBLotQtyMax = Double.parseDouble(splitArray[0]);
			} else {
				IsMaxOnly = "N";
				DBLotQtyMax = Double.parseDouble(splitArray[1]);
				DBLotQtyMin = Double.parseDouble(splitArray[0]);
			}
		}
		double DBLotDifMax = 0.00;
		double DBLotDifMin = 0.00;
		splitArray = null;
		if ( ! LotDif.equals("")) {
			splitArray = LotDif.split("-");
			if (splitArray.length == 1) {
				DBLotDifMax = Double.parseDouble(splitArray[0]);
			} else {
				DBLotDifMax = Double.parseDouble(splitArray[1]);
				DBLotDifMin = Double.parseDouble(splitArray[0]);
			}
		}
		double DBOverCapQtyMax = 0.00;
		double DBOverCapQtyMin = 0.00;
		splitArray = null;
		if ( ! OverCapQty.equals("")) {
			splitArray = OverCapQty.split("-");
			if (splitArray.length == 1) {
				DBOverCapQtyMax = Double.parseDouble(splitArray[0]);
			} else {
				DBOverCapQtyMax = Double.parseDouble(splitArray[1]);
				DBOverCapQtyMin = Double.parseDouble(splitArray[0]);
			}
		}

//		String QuantityMR = "";
//		if (map.get("QuantityMR") != null) {
//			double doubleVal = (double) map.get("QuantityMR");
//			QuantityMR = this.df2.format(doubleVal);
////			float val = (float) map.get("QuantityMR")  ;
////			QuantityMR = String.valueOf(val);
//		}
//

		String GroupOptions = "";
		if (map.get("GroupOptions") != null) {
			GroupOptions = (String) map.get("GroupOptions");
		}
		List<String> GroupOptionsList = new ArrayList<>();
		splitArray = null;
		if ( ! GroupOptions.equals("")) {
			splitArray = GroupOptions.split(",");
			for (String element : splitArray) {
				GroupOptionsList.add(element);
			}
		}
		int CountWorkDate = 0;
		if (map.get("CountWorkDate") != null) {
			CountWorkDate = (int) map.get("CountWorkDate");
		}
		Date DyeAfterGreigeInBegin = null;
		if (map.get("DyeAfterGreigeInBegin") != null) {
			DyeAfterGreigeInBegin = (Date) map.get("DyeAfterGreigeInBegin");
		}
		Date DyeAfterGreigeInLast = null;
		if (map.get("DyeAfterGreigeInLast") != null) {
			DyeAfterGreigeInLast = (Date) map.get("DyeAfterGreigeInLast");
		}
		String MeterPerLot = "";
		if (map.get("MeterPerLot") != null) {
			MeterPerLot = Integer.toString((Integer) map.get("MeterPerLot"));
		}

		int CheckQtyGreigeMR = 0;
		if (map.get("CheckQtyGreigeMR") != null) {
			CheckQtyGreigeMR = (int) map.get("CheckQtyGreigeMR");
		}
		String QtyGreigeMR = "";
		int IntQtyGreigeMR = 0;
		if (map.get("QtyGreigeMR") != null) {
			int value = (int) map.get("QtyGreigeMR");
			IntQtyGreigeMR = value;
			QtyGreigeMR = Integer.toString(IntQtyGreigeMR);
		}
		int CheckHW = 0;
		if (map.get("CheckHW") != null) {
			CheckHW = (int) map.get("CheckHW");
		}

		String LotQtyMaxWithOC = "";
		double DBLotQtyMaxWithOC = 1;
		if (map.get("LotQtyMaxWithOC") != null) {
			BigDecimal value = (BigDecimal) map.get("LotQtyMaxWithOC");
			DBLotQtyMaxWithOC = value.doubleValue();
			LotQtyMaxWithOC = formatter.format(DBLotQtyMaxWithOC);
		}
		String MainNSubGroup = GroupNo + ":" + SubGroup;
		int PriorityGroup = 5;
		if (map.get("PriorityGroup") != null) {
			PriorityGroup = ((int) map.get("PriorityGroup"));
		}
		boolean isMinGroup = false;
		if (map.get("isMinGroup") != null) {
			isMinGroup = (boolean) map.get("isMinGroup");
		}
		String GroupWithQty = "";
		if (map.get("GroupWithQty") != null) {
			GroupWithQty = (String) map.get("GroupWithQty");
		}
		String LotPerDayInWork = "";
		if (map.get("LotPerDayInWork") != null) {
			int value = ((int) map.get("LotPerDayInWork"));
			LotPerDayInWork = Integer.toString(value);
		}

		InputGroupDetail bean = new InputGroupDetail(No, GroupNo, SubGroup, MachineName, Article, LotMinMax, LotDif, IsOverCap,
				OverCapQty, Description, LotPerDay, MainNSubGroup, Division, ColorType, DBLotQtyMax, DBLotQtyMin, DBLotDifMax,
				DBLotDifMin, DBOverCapQtyMax, DBOverCapQtyMin, IsMaxOnly, GroupType, GroupOptions, CountWorkDate,
				DyeAfterGreigeInBegin, DyeAfterGreigeInLast, MeterPerLot, QtyGreigeMR, IntQtyGreigeMR, CheckQtyGreigeMR, CheckHW,
				LotQtyMaxWithOC, DBLotQtyMaxWithOC, PriorityGroup, isMinGroup, GroupWithQty, LotPerDayInWork);
		bean.setGroupOptionsList(GroupOptionsList);
		bean.setDataStatus(DataStatus);
		return bean;
	}

	@Override
	public InputDateRunningDetail _genInputDateRunningDetail(Map<String, Object> map)
	{
		int Id = 0;
		if (map.get("Id") != null) {
			long val = (long) map.get("Id");
			Id = (int) val;
//			Id =  (int) map.get("Id") ;
		}
		String Date = "";
		if (map.get("Date") != null) {
			java.util.Date dateStr = (Date) map.get("Date");
			Date = sdf2.format(dateStr);
		}
		String DateName = "";
		if (map.get("DateName") != null) {
			DateName = (String) map.get("DateName");
		}
		return new InputDateRunningDetail(Id, Date, DateName);
	}

	@Override
	public InputLeadTimeStatusDetail _genInputLeadTimeStatusDetail(Map<String, Object> map)
	{

		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}

		String StatusType = "";
		if (map.get("StatusType") != null) {
			StatusType = (String) map.get("StatusType");
		}
		int UserStatusId = 0;
		if (map.get("UserStatusId") != null) {
			UserStatusId = (int) map.get("UserStatusId");
		}
		int LabStatusId = 0;
		if (map.get("LabStatusId") != null) {
			LabStatusId = (int) map.get("LabStatusId");
		}

		String UserStatusSapId = "";
		if (map.get("UserStatusSapId") != null) {
			UserStatusSapId = (String) map.get("UserStatusSapId");
		}
		String LabStatusSapId = "";
		if (map.get("LabStatusSapId") != null) {
			LabStatusSapId = (String) map.get("LabStatusSapId");
		}
		String StatusName = "";
		if (map.get("StatusName") != null) {
			StatusName = (String) map.get("StatusName");
		}
		int AddDays = 0;
		if (map.get("AddDays") != null) {
			AddDays = (int) map.get("AddDays");
		}

		return new InputLeadTimeStatusDetail(Id, StatusType, UserStatusId, LabStatusId, UserStatusSapId, LabStatusSapId,
				StatusName, AddDays);
	}

	@Override
	public InputPlanningLotDetail _genInputPlanningLotDetail(Map<String, Object> map)
	{
		String MachineName = "";
		if (map.get("MachineName") != null) {
			MachineName = (String) map.get("MachineName");
		}
		String ColorType = "";
		if (map.get("ColorType") != null) {
			ColorType = (String) map.get("ColorType");
		}
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		int LotPerDay = 0;
		if (map.get("LotPerDay") != null) {
			LotPerDay = (int) map.get("LotPerDay");
		}
		String GroupType = "";
		if (map.get("GroupType") != null) {
			GroupType = (String) map.get("GroupType");
		}
		return new InputPlanningLotDetail(GroupNo, SubGroup, MachineName, ColorType, LotPerDay, GroupType);
	}

	@Override
	public KPIReportDetail _genKPIReportDetail(Map<String, Object> map)
	{
		String division = "";
		if (map.get("Division") != null) {
			division = (String) map.get("Division");
		}
		String saleOrder = "";
		if (map.get("SaleOrder") != null) {
			saleOrder = (String) map.get("SaleOrder");
		}
		String saleLine = "";
		if (map.get("SaleLine") != null) {
			saleLine = (String) map.get("SaleLine");
		}
		String customerShortName = "";
		if (map.get("CustomerShortName") != null) {
			customerShortName = (String) map.get("CustomerShortName");
		}
		String purchaseOrder = "";
		if (map.get("PurchaseOrder") != null) {
			purchaseOrder = (String) map.get("PurchaseOrder");
		}
		String materialNo = "";
		if (map.get("MaterialNo") != null) {
			materialNo = (String) map.get("MaterialNo");
		}
		String customerMaterial = "";
		if (map.get("CustomerMaterial") != null) {
			customerMaterial = (String) map.get("CustomerMaterial");
		}
		String dueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateStr = (Date) map.get("DueDate");
			dueDate = sdf2.format(dateStr);
		}
		String poDate = "";
		if (map.get("PODate") != null) {
			java.util.Date dateStr = (Date) map.get("PODate");
			poDate = sdf2.format(dateStr);
		}
		String greigeUpdate = "";
		if (map.get("GreigeUpdate") != null) {
			java.util.Date dateStr = (Date) map.get("GreigeUpdate");
			greigeUpdate = sdf2.format(dateStr);
		}
		String cfmUpdate = "";
		if (map.get("CFMUpdate") != null) {
			java.util.Date dateStr = (Date) map.get("CFMUpdate");
			cfmUpdate = sdf2.format(dateStr);
		}
		String saleCreateDate = "";
		if (map.get("SaleCreateDate") != null) {
			java.util.Date dateStr = (Date) map.get("SaleCreateDate");
			saleCreateDate = sdf2.format(dateStr);
		}
		String soasFacApproved = "";
		if (map.get("SOASFacApproved") != null) {
			java.util.Date dateStr = (Date) map.get("SOASFacApproved");
			soasFacApproved = sdf2.format(dateStr);
		}
		String orderSheetPrintDate = "";
		if (map.get("OrderSheetPrintDate") != null) {
			java.util.Date dateStr = (Date) map.get("OrderSheetPrintDate");
			orderSheetPrintDate = sdf2.format(dateStr);
		}
		String productionOrder = "";
		if (map.get("ProductionOrder") != null) {
			productionOrder = (String) map.get("ProductionOrder");
		}
		String lotNo = "";
		if (map.get("LotNo") != null) {
			lotNo = (String) map.get("LotNo");
		}
		String prdCreateDate = "";
		if (map.get("PrdCreateDate") != null) {
			java.util.Date dateStr = (Date) map.get("PrdCreateDate");
			prdCreateDate = sdf2.format(dateStr);
		}
		String poPostingDateDefault = "";
		if (map.get("POPostingDateDefault") != null) {
			java.util.Date dateStr = (Date) map.get("POPostingDateDefault");
			poPostingDateDefault = sdf2.format(dateStr);
		}
		String greigeInDate = "";
		if (map.get("GreigeInDate") != null) {
			java.util.Date dateStr = (Date) map.get("GreigeInDate");
			greigeInDate = sdf2.format(dateStr);
		}
		String bcDate = "";
		if (map.get("BCDate") != null) {
			java.util.Date dateStr = (Date) map.get("BCDate");
			bcDate = sdf2.format(dateStr);
		}
		String prepare = "";
		if (map.get("Prepare") != null) {
			java.util.Date dateStr = (Date) map.get("Prepare");
			prepare = sdf2.format(dateStr);
		}
		String unwind = "";
		if (map.get("Unwind") != null) {
			java.util.Date dateStr = (Date) map.get("Unwind");
			unwind = sdf2.format(dateStr);
		}
		String relax = "";
		if (map.get("Relax") != null) {
			java.util.Date dateStr = (Date) map.get("Relax");
			relax = sdf2.format(dateStr);
		}
		String preset = "";
		if (map.get("Preset") != null) {
			java.util.Date dateStr = (Date) map.get("Preset");
			preset = sdf2.format(dateStr);
		}
		String inspectAfterPreset = "";
		if (map.get("InspectAfterPreset") != null) {
			java.util.Date dateStr = (Date) map.get("InspectAfterPreset");
			inspectAfterPreset = sdf2.format(dateStr);
		}
		String dyeActual = "";
		if (map.get("DyeActual") != null) {
			java.util.Date dateStr = (Date) map.get("DyeActual");
			dyeActual = sdf2.format(dateStr);
		}
		String centifuge = "";
		if (map.get("Centifuge") != null) {
			java.util.Date dateStr = (Date) map.get("Centifuge");
			centifuge = sdf2.format(dateStr);
		}
		String scutcher = "";
		if (map.get("Scutcher") != null) {
			java.util.Date dateStr = (Date) map.get("Scutcher");
			scutcher = sdf2.format(dateStr);
		}
		String dryer = "";
		if (map.get("Dryer") != null) {
			java.util.Date dateStr = (Date) map.get("Dryer");
			dryer = sdf2.format(dateStr);
		}
		String labFinish = "";
		if (map.get("LabFinish") != null) {
			java.util.Date dateStr = (Date) map.get("LabFinish");
			labFinish = sdf2.format(dateStr);
		}
		String slitting = "";
		if (map.get("Slitting") != null) {
			java.util.Date dateStr = (Date) map.get("Slitting");
			slitting = sdf2.format(dateStr);
		}
		String cfmNoMini = "";
		if (map.get("CFMNoMini") != null) {
			cfmNoMini = (String) map.get("CFMNoMini");
		}
		String cfmNumberMini = "";
		if (map.get("CFMNumberMini") != null) {
			cfmNumberMini = (String) map.get("CFMNumberMini");
		}
		String cfmSendDateMini = "";
		if (map.get("CFMSendDateMini") != null) {
			java.util.Date dateStr = (Date) map.get("CFMSendDateMini");
			cfmSendDateMini = sdf2.format(dateStr);
		}
		String cfmAnswerDateMini = "";
		if (map.get("CFMAnswerDateMini") != null) {
			java.util.Date dateStr = (Date) map.get("CFMAnswerDateMini");
			cfmAnswerDateMini = sdf2.format(dateStr);
		}
		String cfmStatusMini = "";
		if (map.get("CFMStatusMini") != null) {
			cfmStatusMini = (String) map.get("CFMStatusMini");
		}
		String cfmRemarkMini = "";
		if (map.get("CFMRemarkMini") != null) {
			cfmRemarkMini = (String) map.get("CFMRemarkMini");
		}
		String rollNoRemarkMini = "";
		if (map.get("RollNoRemarkMini") != null) {
			rollNoRemarkMini = (String) map.get("RollNoRemarkMini");
		}
		String finishing = "";
		if (map.get("Finishing") != null) {
			java.util.Date dateStr = (Date) map.get("Finishing");
			finishing = sdf2.format(dateStr);
		}
		String inspectation = "";
		if (map.get("Inspectation") != null) {
			java.util.Date dateStr = (Date) map.get("Inspectation");
			inspectation = sdf2.format(dateStr);
		}
		String packing = "";
		if (map.get("Packing") != null) {
			java.util.Date dateStr = (Date) map.get("Packing");
			packing = sdf2.format(dateStr);
		}
		String userStatus = "";
		if (map.get("UserStatus") != null) {
			userStatus = (String) map.get("UserStatus");
		}
		int daysGreigeUpDate = 0;
		if (map.get("DaysGreigeUpDate") != null) {
			daysGreigeUpDate = (int) map.get("DaysGreigeUpDate");
		}
		int daysCFMUpdate = 0;
		if (map.get("DaysCFMUpdate") != null) {
			daysCFMUpdate = (int) map.get("DaysCFMUpdate");
		}
		int daysSaleCreateDate = 0;
		if (map.get("DaysSaleCreateDate") != null) {
			daysSaleCreateDate = (int) map.get("DaysSaleCreateDate");
		}
		int daysSOASFacApproved = 0;
		if (map.get("DaysSOASFacApproved") != null) {
			daysSOASFacApproved = (int) map.get("DaysSOASFacApproved");
		}
		int daysOrderSheetDate = 0;
		if (map.get("DaysOrderSheetDate") != null) {
			daysOrderSheetDate = (int) map.get("DaysOrderSheetDate");
		}
		int daysPOPostingDateDefault = 0;
		if (map.get("DaysPOPostingDateDefault") != null) {
			daysPOPostingDateDefault = (int) map.get("DaysPOPostingDateDefault");
		}
		int daysGreigeInDate = 0;
		if (map.get("DaysGreigeInDate") != null) {
			daysGreigeInDate = (int) map.get("DaysGreigeInDate");
		}
		int daysBCDate = 0;
		if (map.get("DaysBCDate") != null) {
			daysBCDate = (int) map.get("DaysBCDate");
		}
		int daysPrepare = 0;
		if (map.get("DaysPrepare") != null) {
			daysPrepare = (int) map.get("DaysPrepare");
		}
		int daysUnwind = 0;
		if (map.get("DaysUnwind") != null) {
			daysUnwind = (int) map.get("DaysUnwind");
		}
		int daysRelax = 0;
		if (map.get("DaysRelax") != null) {
			daysRelax = (int) map.get("DaysRelax");
		}
		int daysPreset = 0;
		if (map.get("DaysPreset") != null) {
			daysPreset = (int) map.get("DaysPreset");
		}
		int daysInspectAfterPreset = 0;
		if (map.get("DaysInspectAfterPreset") != null) {
			daysInspectAfterPreset = (int) map.get("DaysInspectAfterPreset");
		}
		int daysDyeActual = 0;
		if (map.get("DaysDyeActual") != null) {
			daysDyeActual = (int) map.get("DaysDyeActual");
		}
		int daysCentifuge = 0;
		if (map.get("DaysCentifuge") != null) {
			daysCentifuge = (int) map.get("DaysCentifuge");
		}
		int daysScutcher = 0;
		if (map.get("DaysScutcher") != null) {
			daysScutcher = (int) map.get("DaysScutcher");
		}
		int daysDryer = 0;
		if (map.get("DaysDryer") != null) {
			daysDryer = (int) map.get("DaysDryer");
		}
		int daysLabFinish = 0;
		if (map.get("DaysLabFinish") != null) {
			daysLabFinish = (int) map.get("DaysLabFinish");
		}
		int daysSlitting = 0;
		if (map.get("DaysSlitting") != null) {
			daysSlitting = (int) map.get("DaysSlitting");
		}
		int daysFinishing = 0;
		if (map.get("DaysFinishing") != null) {
			daysFinishing = (int) map.get("DaysFinishing");
		}
		int daysInspectation = 0;
		if (map.get("DaysInspectation") != null) {
			daysInspectation = (int) map.get("DaysInspectation");
		}
		int daysPacking = 0;
		if (map.get("DaysPacking") != null) {
			daysPacking = (int) map.get("DaysPacking");
		}
		String typePrd = "";
		if (map.get("TypePrd") != null) {
			typePrd = (String) map.get("TypePrd");
		}
		int daysPrdCreateDate = 0;
		if (map.get("DaysPrdCreateDate") != null) {
			daysPrdCreateDate = (int) map.get("DaysPrdCreateDate");
		}
		int daysCFMAnswerDate = 0;
		if (map.get("DaysCFMAnswerDate") != null) {
			daysCFMAnswerDate = (int) map.get("DaysCFMAnswerDate");
		}

		return new KPIReportDetail(division, saleOrder, saleLine, customerShortName, purchaseOrder, materialNo, customerMaterial,
				dueDate, poDate, greigeUpdate, cfmUpdate, saleCreateDate, soasFacApproved, orderSheetPrintDate, productionOrder,
				lotNo, prdCreateDate, poPostingDateDefault, greigeInDate, bcDate, prepare, unwind, relax, preset,
				inspectAfterPreset, dyeActual, centifuge, scutcher, dryer, labFinish, slitting, cfmNoMini, cfmNumberMini,
				cfmSendDateMini, cfmAnswerDateMini, cfmStatusMini, cfmRemarkMini, rollNoRemarkMini, finishing, inspectation,
				packing, userStatus, daysGreigeUpDate, daysCFMUpdate, daysSaleCreateDate, daysSOASFacApproved, daysOrderSheetDate,
				daysPOPostingDateDefault, daysGreigeInDate, daysBCDate, daysPrepare, daysUnwind, daysRelax, daysPreset,
				daysInspectAfterPreset, daysDyeActual, daysCentifuge, daysScutcher, daysDryer, daysLabFinish, daysSlitting,
				daysFinishing, daysInspectation, daysPacking, typePrd, daysPrdCreateDate, daysCFMAnswerDate);
	}

	@Override
	public InputLeadTimeDetail _genLeadTimeDetail(Map<String, Object> map)
	{
		int id = 0;
		if (map.get("Id") != null) {
			id = (int) map.get("Id");
//			int val = (int) map.get("Id") ;
//			id = Integer.toString(val);
		}
		String Description = "";
		if (map.get("Description") != null) {
			Description = (String) map.get("Description");
		}
		String LeadTimeType = "";
		if (map.get("LeadTimeType") != null) {
			LeadTimeType = (String) map.get("LeadTimeType");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String DistChannal = "";
		if (map.get("DistChannal") != null) {
			DistChannal = (String) map.get("DistChannal");
		}
		String CustomerShortName = "";
		if (map.get("CustomerShortName") != null) {
			CustomerShortName = (String) map.get("CustomerShortName");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		int POInput = 0;
		if (map.get("POInput") != null) {
			POInput = (int) map.get("POInput");
		}
		int MakeLot = 0;
		if (map.get("MakeLot") != null) {
			MakeLot = (int) map.get("MakeLot");
		}
		int BC = 0;
		if (map.get("BC") != null) {
			BC = (int) map.get("BC");
		}
		int Dye = 0;
		if (map.get("Dye") != null) {
			Dye = (int) map.get("Dye");
		}
		int CFM = 0;
		if (map.get("CFM") != null) {
			CFM = (int) map.get("CFM");
		}
		int CFMAnswer = 0;
		if (map.get("CFMAnswer") != null) {
			CFMAnswer = (int) map.get("CFMAnswer");
		}
		int Delivery = 0;
		if (map.get("Delivery") != null) {
			Delivery = (int) map.get("Delivery");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
//		String DataStatus = "";
//		if (map.get("DataStatus") != null) {
//			DataStatus = (String) map.get("DataStatus");
//		}
		int ruleNo = 0;
		if (map.get("RuleNo") != null) {
			ruleNo = (int) map.get("RuleNo");
		}
		return new InputLeadTimeDetail(id, Description, LeadTimeType, DistChannal, CustomerShortName, Article, POInput, MakeLot,
				BC, Dye, CFM, CFMAnswer, Delivery, ChangeDate, ChangeBy, ruleNo, CustomerNo);
	}

	@Override
	public InputMachineDetail _genMachineDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
//		Double doubleRemainQ = 0.00;
//		if (map.get("RemainQuantity") != null) {
//			BigDecimal value = (BigDecimal) map.get("RemainQuantity");
//			doubleRemainQ = value.doubleValue();
//			RemainQuantity = formatter.format(doubleRemainQ);
//		}
//		String ChangeBy = "";
//		if (map.get("ChangeBy") != null) {
//			ChangeBy = (String) map.get("ChangeBy");
//		}
//		String ChangeDate = "";
//		if (map.get("ChangeDate") != null) {
//		    Timestamp timestamp1 = (Timestamp)map.get("ChangeDate");
//		    ChangeDate = this.sdf3.format(timestamp1);
//		}
//		if (map.get("GreigeInDate") != null) {
//			java.util.Date dateStr = (Date) map.get("GreigeInDate");
//			GreigeInDate = sdf2.format(dateStr);
//		}
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String MachineName = "";
		if (map.get("MachineName") != null) {
			MachineName = (String) map.get("MachineName");
		}
		String Description = "";
		if (map.get("Description") != null) {
			Description = (String) map.get("Description");
		}
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		int LotPerDay = 0;
		if (map.get("LotPerDay") != null) {
			LotPerDay = (int) map.get("LotPerDay");
		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String ColorType = "";
		if (map.get("ColorType") != null) {
			ColorType = (String) map.get("ColorType");
		}
		return new InputMachineDetail(No, MachineName, Description, GroupNo, ColorType, LotPerDay, DataStatus, ChangeDate,
				ChangeBy);
	}

	@Override
	public MasterSettingChangeDetail _genMasterSettingChangeDetail(Map<String, Object> map)
	{
		String Id = "";
		if (map.get("Id") != null) {
			int value = (int) map.get("Id");
			Id = Integer.toString(value);
		}
		String Description = "";
		if (map.get("Description") != null) {
			Description = (String) map.get("Description");
		}
		String DescriptionTH = "";
		if (map.get("DescriptionTH") != null) {
			DescriptionTH = (String) map.get("DescriptionTH");
		}
		String TableName = "";
		if (map.get("TableName") != null) {
			TableName = (String) map.get("TableName");
		}
		String FieldName = "";
		if (map.get("FieldName") != null) {
			FieldName = (String) map.get("FieldName");
		}
		return new MasterSettingChangeDetail(Id, Description, DescriptionTH, TableName, FieldName);
	}

	@Override
	public MonthlyCapReportDetail _genMonthlyCapReportDetail(Map<String, Object> map)
	{

		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		String Description = "";
		if (map.get("Description") != null) {
			Description = (String) map.get("Description");
		}
		int LotPerDay = 0;
		if (map.get("LotPerDay") != null) {
			LotPerDay = (int) map.get("LotPerDay");
		}
		int MeterPerLot = 0;
		if (map.get("MeterPerLot") != null) {
			MeterPerLot = (int) map.get("MeterPerLot");
		}
		String ColorType = "";
		if (map.get("ColorType") != null) {
			ColorType = (String) map.get("ColorType");
		}
		int TotalWorkDate = 0;
		if (map.get("TotalWorkDate") != null) {
			int val = (int) map.get("TotalWorkDate");
			TotalWorkDate = val;
//			TotalWorkDate = (int) map.get("TotalWorkDate");
		}
		int TotalLotWorkDate = 0;
		if (map.get("TotalLotWorkDate") != null) {
			int val = (int) map.get("TotalLotWorkDate");
			TotalLotWorkDate = val;
//			TotalLotWorkDate = (int) map.get("TotalLotWorkDate") ;
		}
		int TotalPlanSystemDatePO = 0;
		if (map.get("TotalPlanSystemDatePO") != null) {
			TotalPlanSystemDatePO = (int) map.get("TotalPlanSystemDatePO");
		}
		int TotalPlanSystemDateRedye = 0;
		if (map.get("TotalPlanSystemDateRedye") != null) {
			TotalPlanSystemDateRedye = (int) map.get("TotalPlanSystemDateRedye");
		}
		int TotalPlanSystemDateForecast = 0;
		if (map.get("TotalPlanSystemDateForecast") != null) {
			TotalPlanSystemDateForecast = (int) map.get("TotalPlanSystemDateForecast");
		}
		String date = "";
		if (map.get("Date") != null) {
			java.util.Date dateStr = (Date) map.get("Date");
			date = this.sdf2.format(dateStr);
		}
		int RemainCapacityLot = TotalLotWorkDate-(TotalPlanSystemDatePO+TotalPlanSystemDateRedye+TotalPlanSystemDateForecast);
		int RemainCapacityQty = RemainCapacityLot * MeterPerLot;

		String remainCapacityLotStr = this.df2.format(RemainCapacityLot);
		String remainCapacityQtyStr = this.df2.format(RemainCapacityQty);
		MonthlyCapReportDetail bean = new MonthlyCapReportDetail(GroupNo, SubGroup, Description, LotPerDay, ColorType,
				TotalWorkDate, TotalLotWorkDate, RemainCapacityLot, RemainCapacityQty, MeterPerLot, TotalPlanSystemDatePO,
				TotalPlanSystemDateRedye, TotalPlanSystemDateForecast, date, remainCapacityLotStr, remainCapacityQtyStr);

		return bean;
	}

	@Override
	public MonthlyCapWithDueDateReportDetail _genMonthlyCapWithDueDateReportDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
		String groupNo = "";
		if (map.get("GroupNo") != null) {
			groupNo = (String) map.get("GroupNo");
		}
		String subGroup = "";
		if (map.get("SubGroup") != null) {
			subGroup = (String) map.get("SubGroup");
		}
		String mainNSubGroup = groupNo + ":" + subGroup;
		String description = "";
		if (map.get("Description") != null) {
			description = (String) map.get("Description");
		}
		int lotPerDay = 0;
		if (map.get("LotPerDay") != null) {
			lotPerDay = (int) map.get("LotPerDay");
		}
		int meterPerLot = 0;
		if (map.get("MeterPerLot") != null) {
			meterPerLot = (int) map.get("MeterPerLot");
		}
		String colorType = "";
		if (map.get("ColorType") != null) {
			colorType = (String) map.get("ColorType");
		}
		int firstTotalWorkDate = 0;
		if (map.get("FirstTotalWorkDate") != null) {
			firstTotalWorkDate = (int) map.get("FirstTotalWorkDate");
		}
		int firstTotalLotWorkDate = 0;
		if (map.get("FirstTotalLotWorkDate") != null) {
			firstTotalLotWorkDate = (int) map.get("FirstTotalLotWorkDate");
		}
		int firstMonthTotalPlanSystemDatePO = 0;
		if (map.get("FirstTotalPlanSystemDatePO") != null) {
			firstMonthTotalPlanSystemDatePO = (int) map.get("FirstTotalPlanSystemDatePO");
		}
		int firstMonthTotalPlanSystemDateRedye = 0;
		if (map.get("FirstTotalPlanSystemDateRedye") != null) {
			firstMonthTotalPlanSystemDateRedye = (int) map.get("FirstTotalPlanSystemDateRedye");
		}
		int firstMonthTotalPlanSystemDateForecast = 0;
		if (map.get("FirstTotalPlanSystemDateForecast") != null) {
			firstMonthTotalPlanSystemDateForecast = (int) map.get("FirstTotalPlanSystemDateForecast");
		}
		int firstMonthRemainCapacityLot = 0;
		if (map.get("FirstRemainCapacityLot") != null) {
			firstMonthRemainCapacityLot = (int) map.get("FirstRemainCapacityLot");
		}
		int firstMonthRemainCapacityQty = 0;
		if (map.get("FirstRemainCapacityMeter") != null) {
			firstMonthRemainCapacityQty = (int) map.get("FirstRemainCapacityMeter");
		}
		int firstRequiredDate = 0;
		if (map.get("FirstRequiredDate") != null) {
			firstRequiredDate = (int) map.get("FirstRequiredDate");
		}
		String firstDyeDate = "";
		if (map.get("FirstDyeDate") != null) {
			java.util.Date dateStr = (Date) map.get("FirstDyeDate");
			firstDyeDate = sdf2.format(dateStr);
		}
		String firstDMDate = "";
		if (map.get("FirstDMDate") != null) {
			java.util.Date dateStr = (Date) map.get("FirstDMDate");
			firstDMDate = sdf2.format(dateStr);
		}
		String firstEXDate = "";
		if (map.get("FirstEXDate") != null) {
			java.util.Date dateStr = (Date) map.get("FirstEXDate");
			firstEXDate = sdf2.format(dateStr);
		}

		int secondTotalWorkDate = 0;
		if (map.get("SecondTotalWorkDate") != null) {
			secondTotalWorkDate = (int) map.get("SecondTotalWorkDate");
		}
		int secondTotalLotWorkDate = 0;
		if (map.get("SecondTotalLotWorkDate") != null) {
			secondTotalLotWorkDate = (int) map.get("SecondTotalLotWorkDate");
		}
		int secondMonthTotalPlanSystemDatePO = 0;
		if (map.get("SecondTotalPlanSystemDatePO") != null) {
			secondMonthTotalPlanSystemDatePO = (int) map.get("SecondTotalPlanSystemDatePO");
		}
		int secondMonthTotalPlanSystemDateRedye = 0;
		if (map.get("SecondTotalPlanSystemDateRedye") != null) {
			secondMonthTotalPlanSystemDateRedye = (int) map.get("SecondTotalPlanSystemDateRedye");
		}
		int secondMonthTotalPlanSystemDateForecast = 0;
		if (map.get("SecondTotalPlanSystemDateForecast") != null) {
			secondMonthTotalPlanSystemDateForecast = (int) map.get("SecondTotalPlanSystemDateForecast");
		}
		int secondMonthRemainCapacityLot = 0;
		if (map.get("SecondRemainCapacityLot") != null) {
			secondMonthRemainCapacityLot = (int) map.get("SecondRemainCapacityLot");
		}
		int secondMonthRemainCapacityQty = 0;
		if (map.get("SecondRemainCapacityMeter") != null) {
			secondMonthRemainCapacityQty = (int) map.get("SecondRemainCapacityMeter");
		}
		int secondRequiredDate = 0;
		if (map.get("SecondRequiredDate") != null) {
			secondRequiredDate = (int) map.get("SecondRequiredDate");
		}
		String secondDyeDate = "";
		if (map.get("SecondDyeDate") != null) {
			java.util.Date dateStr = (Date) map.get("SecondDyeDate");
			secondDyeDate = sdf2.format(dateStr);
		}
		String secondDMDate = "";
		if (map.get("SecondDMDate") != null) {
			java.util.Date dateStr = (Date) map.get("SecondDMDate");
			secondDMDate = sdf2.format(dateStr);
		}
		String secondEXDate = "";
		if (map.get("SecondEXDate") != null) {
			java.util.Date dateStr = (Date) map.get("SecondEXDate");
			secondEXDate = sdf2.format(dateStr);
		}

		int thirdTotalWorkDate = 0;
		if (map.get("ThirdTotalWorkDate") != null) {
			thirdTotalWorkDate = (int) map.get("ThirdTotalWorkDate");
		}
		int thirdTotalLotWorkDate = 0;
		if (map.get("ThirdTotalLotWorkDate") != null) {
			thirdTotalLotWorkDate = (int) map.get("ThirdTotalLotWorkDate");
		}
		int thirdMonthTotalPlanSystemDatePO = 0;
		if (map.get("ThirdTotalPlanSystemDatePO") != null) {
			thirdMonthTotalPlanSystemDatePO = (int) map.get("ThirdTotalPlanSystemDatePO");
		}
		int thirdMonthTotalPlanSystemDateRedye = 0;
		if (map.get("ThirdTotalPlanSystemDateRedye") != null) {
			thirdMonthTotalPlanSystemDateRedye = (int) map.get("ThirdTotalPlanSystemDateRedye");
		}
		int thirdMonthTotalPlanSystemDateForecast = 0;
		if (map.get("ThirdTotalPlanSystemDateForecast") != null) {
			thirdMonthTotalPlanSystemDateForecast = (int) map.get("ThirdTotalPlanSystemDateForecast");
		}
		int thirdMonthRemainCapacityLot = 0;
		if (map.get("ThirdRemainCapacityLot") != null) {
			thirdMonthRemainCapacityLot = (int) map.get("ThirdRemainCapacityLot");
		}
		int thirdMonthRemainCapacityQty = 0;
		if (map.get("ThirdRemainCapacityMeter") != null) {
			thirdMonthRemainCapacityQty = (int) map.get("ThirdRemainCapacityMeter");
		}
		int thirdRequiredDate = 0;
		if (map.get("ThirdRequiredDate") != null) {
			thirdRequiredDate = (int) map.get("ThirdRequiredDate");
		}
		String thirdDyeDate = "";
		if (map.get("ThirdDyeDate") != null) {
			java.util.Date dateStr = (Date) map.get("ThirdDyeDate");
			thirdDyeDate = sdf2.format(dateStr);
		}
		String thirdDMDate = "";
		if (map.get("ThirdDMDate") != null) {
			java.util.Date dateStr = (Date) map.get("ThirdDMDate");
			thirdDMDate = sdf2.format(dateStr);
		}
//			System.out.println(thirdDMDate);
		String thirdEXDate = "";
		if (map.get("ThirdEXDate") != null) {
			java.util.Date dateStr = (Date) map.get("ThirdEXDate");
			thirdEXDate = sdf2.format(dateStr);
		}

		int sumFourthTotalWorkDateCap = 0;
		if (map.get("SumFourthTotalWorkDate") != null) {
			sumFourthTotalWorkDateCap = (int) map.get("SumFourthTotalWorkDate");
		}
		int sumFourthTotalPlanCapOrder = 0;
		if (map.get("SumFourthTotalPlanSystemOrder") != null) {
			sumFourthTotalPlanCapOrder = (int) map.get("SumFourthTotalPlanSystemOrder");
		}
		int sumFourthTotalPlanCapForeCast = 0;
		if (map.get("SumFourthTotalPlanSystemForeCast") != null) {
			sumFourthTotalPlanCapForeCast = (int) map.get("SumFourthTotalPlanSystemForeCast");
		}
		int sumFourthRemainCapacityMeter = 0;
		if (map.get("SumFourthRemainCapacityMeter") != null) {
			sumFourthRemainCapacityMeter = (int) map.get("SumFourthRemainCapacityMeter");
		}

		int sumFifthTotalTotalWorkDateCap = 0;
		if (map.get("SumFifthTotalWorkDate") != null) {
			sumFifthTotalTotalWorkDateCap = (int) map.get("SumFifthTotalWorkDate");
		}
		int sumFifthTotalPlanSystemOrder = 0;
		if (map.get("SumFifthTotalPlanSystemOrder") != null) {
			sumFifthTotalPlanSystemOrder = (int) map.get("SumFifthTotalPlanSystemOrder");
		}
		int sumFifthTotalTotalPlanCapForeCast = 0;
		if (map.get("SumFifthTotalPlanSystemForeCast") != null) {
			sumFifthTotalTotalPlanCapForeCast = (int) map.get("SumFifthTotalPlanSystemForeCast");
		}
		int sumFifthRemainCapacityMeter = 0;
		if (map.get("SumFifthRemainCapacityMeter") != null) {
			sumFifthRemainCapacityMeter = (int) map.get("SumFifthRemainCapacityMeter");
		}

		int sumSixthTotalTotalWorkDateCap = 0;
		if (map.get("SumSixthTotalWorkDate") != null) {
			sumSixthTotalTotalWorkDateCap = (int) map.get("SumSixthTotalWorkDate");
		}
		int sumSixthTotalPlanSystemOrder = 0;
		if (map.get("SumSixthTotalPlanSystemOrder") != null) {
			sumSixthTotalPlanSystemOrder = (int) map.get("SumSixthTotalPlanSystemOrder");
		}
		int sumSixthTotalTotalPlanCapForeCast = 0;
		if (map.get("SumSixthTotalPlanSystemForeCast") != null) {
			sumSixthTotalTotalPlanCapForeCast = (int) map.get("SumSixthTotalPlanSystemForeCast");
		}
		int sumSixthRemainCapacityMeter = 0;
		if (map.get("SumSixthRemainCapacityMeter") != null) {
			sumSixthRemainCapacityMeter = (int) map.get("SumSixthRemainCapacityMeter");
		}
		return new MonthlyCapWithDueDateReportDetail(groupNo, subGroup, mainNSubGroup, description, lotPerDay, meterPerLot,
				colorType, firstTotalWorkDate, firstTotalLotWorkDate, firstMonthTotalPlanSystemDatePO,
				firstMonthTotalPlanSystemDateRedye, firstMonthTotalPlanSystemDateForecast, firstMonthRemainCapacityLot,
				firstMonthRemainCapacityQty, firstRequiredDate, firstDyeDate, firstDMDate, firstEXDate, secondTotalWorkDate,
				secondTotalLotWorkDate, secondMonthTotalPlanSystemDatePO, secondMonthTotalPlanSystemDateRedye,
				secondMonthTotalPlanSystemDateForecast, secondMonthRemainCapacityLot, secondMonthRemainCapacityQty,
				secondRequiredDate, secondDyeDate, secondDMDate, secondEXDate, thirdTotalWorkDate, thirdTotalLotWorkDate,
				thirdMonthTotalPlanSystemDatePO, thirdMonthTotalPlanSystemDateRedye, thirdMonthTotalPlanSystemDateForecast,
				thirdMonthRemainCapacityLot, thirdMonthRemainCapacityQty, thirdRequiredDate, thirdDyeDate, thirdDMDate,
				thirdEXDate, sumFourthTotalWorkDateCap, sumFourthTotalPlanCapOrder, sumFourthTotalPlanCapForeCast,
				sumFourthRemainCapacityMeter, sumFifthTotalTotalWorkDateCap, sumFifthTotalPlanSystemOrder,
				sumFifthTotalTotalPlanCapForeCast, sumFifthRemainCapacityMeter, sumSixthTotalTotalWorkDateCap,
				sumSixthTotalPlanSystemOrder, sumSixthTotalTotalPlanCapForeCast, sumSixthRemainCapacityMeter);
	}

	@Override
	public InputPlanInsteadProdOrderDetail _genPlanInsteadProdOrderDetail(Map<String, Object> map)
	{
		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}

		int TempPlanningLotId = 0;
		if (map.get("TempPlanningLotId") != null) {
			TempPlanningLotId = (int) map.get("TempPlanningLotId");
		}
		int POId = 0;
		if (map.get("POId") != null) {
			POId = (int) map.get("POId");
		}
		InputPlanInsteadProdOrderDetail bean = new InputPlanInsteadProdOrderDetail(Id, POId, TempPlanningLotId, ProductionOrder);
		return bean;
	}

	@Override
	public InputPlanLotRedyeDetail _genPlanLotRedyeDetail(Map<String, Object> map)
	{
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateStr = (Date) map.get("DueDate");
			DueDate = sdf2.format(dateStr);
		}
		String Operation = "";
		if (map.get("Operation") != null) {
//			Operation = (String) map.get("Operation");
			Operation = Integer.toString((int) map.get("Operation"));
		}
		String Shade = "";
		if (map.get("Shade") != null) {
			Shade = (String) map.get("Shade");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String FGDesign = "";
		if (map.get("FGDesign") != null) {
			FGDesign = (String) map.get("FGDesign");
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = sdf2.format(dateStr);
		}
		String MaterialNumber = "";
		if (map.get("MaterialNumber") != null) {
			MaterialNumber = (String) map.get("MaterialNumber");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerMat = "";
		if (map.get("CustomerMat") != null) {
			CustomerMat = (String) map.get("CustomerMat");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {
			double doubleVal = (double) map.get("QuantityKG");
			QuantityKG = this.df2.format(doubleVal);
//			float val = (float) map.get("QuantityKG")  ;
//			QuantityKG = String.valueOf(val);
		}
		String QuantityMR = "";
		if (map.get("QuantityMR") != null) {
			double doubleVal = (double) map.get("QuantityMR");
			QuantityMR = this.df2.format(doubleVal);
//			float val = (float) map.get("QuantityMR")  ;
//			QuantityMR = String.valueOf(val);
		}
		String RemarkTwo = "";
		if (map.get("RemarkTwo") != null) {
			RemarkTwo = (String) map.get("RemarkTwo");
		}

		String OrderType = "";
		if (map.get("OrderType") != null) {
			OrderType = (String) map.get("OrderType");
		}
		return new InputPlanLotRedyeDetail(No, ProductionOrder, DueDate, Operation, Shade, Article, FGDesign, CustomerDue,
				MaterialNumber, LabStatus, UserStatus, CustomerName, CustomerMat, QuantityKG, QuantityMR, OrderType, RemarkTwo);
	}

	@Override
	public PlanningReportDetail _genPlanningReportDetail(Map<String, Object> map)
	{
		String PO = "";
		if (map.get("PO") != null) {
			PO = (String) map.get("PO");
		}
		String POLine = "";
		if (map.get("POLine") != null) {
			POLine = (String) map.get("POLine");
		}
		String Design = "";
		if (map.get("Design") != null) {
			Design = (String) map.get("Design");
		}
//		String ColorCustomer = "";
//		if (map.get("ColorCustomer") != null) {
//			ColorCustomer = (String) map.get("ColorCustomer");
//		}
//		String customerDue = "";
//		if (map.get("CustomerDue") != null) {
//			java.util.Date dateStr = (Date) map.get("CustomerDue");
//			customerDue = sdf2.format(dateStr);
//		}
		String GreigePlan = "";
		if (map.get("GreigePlan") != null) {
			java.util.Date dateStr = (Date) map.get("GreigePlan");
			GreigePlan = sdf2.format(dateStr);
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String ProdOrderQty = "";
		double DBProdQty = 0;
		if (map.get("ProdOrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ProdOrderQty");
			DBProdQty = value.doubleValue();
			ProdOrderQty = this.df2.format(DBProdQty);

//			DBProdQty = (double) map.get("ProdOrderQty");
//			ProdOrderQty = this.df2.format(DBProdQty);
		}
		String PlanDyeDate = "";
		if (map.get("PlanSystemDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanSystemDate");
			PlanDyeDate = sdf2.format(dateStr);
		}
		String PlanDueDate = "";
//		String PlanningDeliveryDateDDMM = "";
		if (map.get("MaxLTDeliveryDate") != null) {
			java.util.Date dateStr = (Date) map.get("MaxLTDeliveryDate");
			PlanDueDate = sdf2.format(dateStr);
//			PlanningDeliveryDateDDMM = sdfDDMM.format(dateStr);
		}
		String FirstLot = "";
		if (map.get("FirstLot") != null) {
			FirstLot = (String) map.get("FirstLot");
		}
		String ApprovedDate = "";
		if (map.get("ApprovedDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ApprovedDate");
			ApprovedDate = this.sdf3.format(timestamp1);
		}
		String PPMMStatus = "";
		if (map.get("PPMMStatus") != null) {
			PPMMStatus = (String) map.get("PPMMStatus");
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = sdf2.format(dateStr);
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}

		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String PlanCFMDate = "";
		if (map.get("MaxLTCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("MaxLTCFMDate");
			PlanCFMDate = sdf2.format(dateStr);
		}

		String POType = "";
		if (map.get("POType") != null) {
			POType = (String) map.get("POType");
		}
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		String GroupNoFull = "";
		GroupNoFull = GroupNo + "-" + SubGroup;
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String CustomerMat = "";
		if (map.get("CustomerMat") != null) {
			CustomerMat = (String) map.get("CustomerMat");
		}
		String SORCFMDate = "";
		if (map.get("SORCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORCFMDate");
			SORCFMDate = this.sdf2.format(dateStr);
		}
		String SORDueDate = "";
		if (map.get("SORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORDueDate");
			SORDueDate = this.sdf2.format(dateStr);
		}
		String Batch = "";
		if (map.get("Batch") != null) {
			Batch = (String) map.get("Batch");
		}
		String POPuangMain = "";
		if (map.get("POPuangMain") != null) {
			POPuangMain = (String) map.get("POPuangMain");
		}
		String POLinePuangMain = "";
		if (map.get("POLinePuangMain") != null) {
			POLinePuangMain = (String) map.get("POLinePuangMain");
		}
		String CustomerDuePuangMain = "";
		if (map.get("CustomerDuePuangMain") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDuePuangMain");
			CustomerDuePuangMain = sdf2.format(dateStr);
		}
		String VolumeFG = "";
		if (map.get("VolumeFG") != null) {
			BigDecimal value = (BigDecimal) map.get("VolumeFG");
			double db_volume = value.doubleValue();
			VolumeFG = this.df2.format(db_volume);
		}
		String OrderQty = "";
		if (map.get("OrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("OrderQty");
			double db_volume = value.doubleValue();
			OrderQty = this.df2.format(db_volume);
		}
		String Unit = "";
		if (map.get("Unit") != null) {
			Unit = (String) map.get("Unit");
		}
		String Division = "";
		if (map.get("Division") != null) {
			Division = (String) map.get("Division");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String IsCotton = "";
		if (map.get("IsCotton") != null) {
			IsCotton = (String) map.get("IsCotton");
		}
		int POId = 0;
		if (map.get("POId") != null) {
			POId = (int) map.get("POId");
		}
		String POIdPuangMain = "";
		if (map.get("POIdPuangMain") != null) {
			int value = (int) map.get("POIdPuangMain");
			POIdPuangMain = Integer.toString(value);
		}
		int TempProdId = 0;
		if (map.get("TempProdId") != null) {
			TempProdId = (int) map.get("TempProdId");
		}
		String LabNo = "";
		if (map.get("LabNo") != null) {
			LabNo = (String) map.get("LabNo");
		}
		String Shade = "";
		if (map.get("Shade") != null) {
			Shade = (String) map.get("Shade");
		}
		String BookNo = "";
		if (map.get("BookNo") != null) {
			BookNo = (String) map.get("BookNo");
		}
		int TotalPO = 0;
		if (map.get("TotalPO") != null) {
			TotalPO = (int) map.get("TotalPO");
		}
		int TotalPOMainPuang = 0;
		if (map.get("TotalPOMainPuang") != null) {
			TotalPOMainPuang = (int) map.get("TotalPOMainPuang");
		}
		int TotalPOSubPuang = 0;
		if (map.get("TotalPOSubPuang") != null) {
			TotalPOSubPuang = (int) map.get("TotalPOSubPuang");
		}
		int TotalFC = 0;
		if (map.get("TotalFC") != null) {
			TotalFC = (int) map.get("TotalFC");
		}
		int TotalRedye = 0;
		if (map.get("TotalRedye") != null) {
			TotalRedye = (int) map.get("TotalRedye");
		}
		int TotalWaitResult = 0;
		if (map.get("TotalWaitResult") != null) {
			TotalWaitResult = (int) map.get("TotalWaitResult");
		}
		int TotalScouring = 0;
		if (map.get("TotalScouring") != null) {
			TotalScouring = (int) map.get("TotalScouring");
		}
		int TotalStart = 0;
		if (map.get("TotalStart") != null) {
			TotalStart = (int) map.get("TotalStart");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			double db_volume = value.doubleValue();
			QuantityKG = this.df2.format(db_volume);
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateStr = (Date) map.get("DueDate");
			DueDate = sdf2.format(dateStr);
		}
		String CustomerDueShow = "";
		if (map.get("CustomerDueShow") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDueShow");
			CustomerDueShow = sdf2.format(dateStr);
		}
		String PlanningRemark = "";
		if (map.get("PlanningRemark") != null) {
			PlanningRemark = (String) map.get("PlanningRemark");
		}
		String GreigeInDate = "";
		if (map.get("GreigeInDate") != null) {
			java.util.Date dateStr = (Date) map.get("GreigeInDate");
			GreigeInDate = sdf2.format(dateStr);
		}
		String PlanGreigeDate = "";
		if (map.get("PlanGreigeDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanGreigeDate");
			PlanGreigeDate = sdf2.format(dateStr);
		}
		String QtyGreigeMR = "";
		if (map.get("QtyGreigeMR") != null) {
			BigDecimal value = (BigDecimal) map.get("QtyGreigeMR");
			double valueDb = value.doubleValue();
			QtyGreigeMR = this.df2.format(valueDb);
		}

		return new PlanningReportDetail(ProductionOrder, ProdOrderQty, FirstLot, PlanDyeDate, PlanDueDate, ApprovedDate,
				PPMMStatus, CustomerDue, MaterialNo, CustomerName, PO, POLine, PlanCFMDate, POType, GroupNoFull, UserStatus,
				LabStatus, CustomerMat, SORCFMDate, SORDueDate, Batch, POPuangMain, POLinePuangMain, CustomerDuePuangMain,
				VolumeFG, OrderQty, GreigePlan, Unit, Division, IsCotton, POId, LabNo, Shade, BookNo, TotalPO, TotalPOMainPuang,
				TotalPOSubPuang, TotalFC, TotalRedye, TotalWaitResult, TotalScouring, TotalStart, QuantityKG, DueDate,
				CustomerDueShow, LotNo, PlanningRemark, Design, PlanGreigeDate, GreigeInDate, TempProdId, POIdPuangMain,
				QtyGreigeMR);
	}

	@Override
	public InputPODetail _genPODetail(Map<String, Object> map)
	{
//		Double doubleRemainQ = 0.00;
//		if (map.get("RemainQuantity") != null) {
//			BigDecimal value = (BigDecimal) map.get("RemainQuantity");
//			doubleRemainQ = value.doubleValue();
//			RemainQuantity = formatter.format(doubleRemainQ);
//		}
//		String ChangeBy = "";
//		if (map.get("ChangeBy") != null) {
//			ChangeBy = (String) map.get("ChangeBy");
//		}
//		String ChangeDate = "";
//		if (map.get("ChangeDate") != null) {
//		    Timestamp timestamp1 = (Timestamp)map.get("ChangeDate");
//		    ChangeDate = this.sdf3.format(timestamp1);
//		}
//		if (map.get("GreigeInDate") != null) {
//			java.util.Date dateStr = (Date) map.get("GreigeInDate");
//			GreigeInDate = sdf2.format(dateStr);
//		}
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String PO = "";
		if (map.get("PO") != null) {
			PO = (String) map.get("PO");
		}
		String POLine = "";
		if (map.get("POLine") != null) {
			POLine = (String) map.get("POLine");
		}
		String Division = "";
		if (map.get("Division") != null) {
			Division = (String) map.get("Division");
		}
		String SaleOrg = "";
		if (map.get("SaleOrg") != null) {
			SaleOrg = (String) map.get("SaleOrg");
		}
		String Design = "";
		if (map.get("Design") != null) {
			Design = (String) map.get("Design");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String ColorCustomer = "";
		if (map.get("ColorCustomer") != null) {
			ColorCustomer = (String) map.get("ColorCustomer");
		}
		String Color = "";
		if (map.get("Color") != null) {
			Color = (String) map.get("Color");
		}
		String ColorType = "";
		if (map.get("ColorType") != null) {
			ColorType = (String) map.get("ColorType");
		}
		String articleReal = "";
		if (map.get("ArticleReal") != null) {
			articleReal = (String) map.get("ArticleReal");
		}
		String changedOrderQty = "";
		if (map.get("ChangedOrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ChangedOrderQty");
			double doubleVal = value.doubleValue();
			changedOrderQty = this.df2.format(doubleVal);
		}
		String OrderQty = "";
		if (map.get("OrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("OrderQty");
			double doubleVal = value.doubleValue();
			OrderQty = this.df2.format(doubleVal);
		}
		String OrderQtyCal = "";
		double db_orderQtyCal = 0;
		if (map.get("OrderQtyCal") != null) {
			BigDecimal value = (BigDecimal) map.get("OrderQtyCal");
			db_orderQtyCal = value.doubleValue();
			OrderQtyCal = this.df2.format(db_orderQtyCal);
		}
//		boolean isOrderQtyMod = false;
//		if (map.get("isOrderQtyMod") != null) {
//			isOrderQtyMod = (boolean) map.get("isOrderQtyMod");
//		}
		String Unit = "";
		if (map.get("Unit") != null) {
			Unit = (String) map.get("Unit");
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = sdf2.format(dateStr);
		}
		String GreigePlan = "";
		if (map.get("GreigePlan") != null) {
			java.util.Date dateStr = (Date) map.get("GreigePlan");
			GreigePlan = sdf2.format(dateStr);
		}
		String OrderPrice = "";
		if (map.get("OrderPrice") != null) {
			OrderPrice = (String) map.get("OrderPrice");
		}
		String OverCap = "";
		if (map.get("OverCap") != null) {
			OverCap = (String) map.get("OverCap");
		}
		String DocDate = "";
		if (map.get("DocDate") != null) {
			java.util.Date dateStr = (Date) map.get("DocDate");
			DocDate = sdf2.format(dateStr);
		}
		Date DyeAfterGreigeInBegin = null;
		if (map.get("DyeAfterGreigeInBegin") != null) {
			DyeAfterGreigeInBegin = (Date) map.get("DyeAfterGreigeInBegin");
//			DocDate = sdf2.format(dateStr);
		}
		Date DyeAfterGreigeInLast = null;
		if (map.get("DyeAfterGreigeInLast") != null) {
			DyeAfterGreigeInLast = (Date) map.get("DyeAfterGreigeInLast");
//			DocDate = sdf2.format(dateStr);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
//		String DataStatus = "";
//		if (map.get("DataStatus") != null) {
//			DataStatus = (String) map.get("DataStatus");
//		}
		int CountProd = 0;
		if (map.get("CountProd") != null) {
			CountProd = (int) map.get("CountProd");
		}
		int CheckDivision = 0;
		if (map.get("CheckDivision") != null) {
			CheckDivision = (int) map.get("CheckDivision");
		}
		int CheckArticle = 0;
		if (map.get("CheckArticle") != null) {
			CheckArticle = (int) map.get("CheckArticle");
		}
		int CheckArticleWeight = 0;
		if (map.get("CheckArticleWeight") != null) {
			CheckArticleWeight = (int) map.get("CheckArticleWeight");
		}
		String ErrorStatus = "I";
		if (CheckDivision > 0 || CheckArticle > 0 || CheckArticleWeight > 0) {
			ErrorStatus = Config.C_ERR_ICON_STATUS;
		}
		String IsCotton = "";
		if (map.get("IsCotton") != null) {
			IsCotton = (String) map.get("IsCotton");
		}
		String DistChannal = "";
		if (map.get("DistChannal") != null) {
			DistChannal = (String) map.get("DistChannal");
		}
		int CountWorkDate = 0;
		if (map.get("CountWorkDate") != null) {
			CountWorkDate = (int) map.get("CountWorkDate");
		}
		int CheckGreigePlan = 0;
		if (map.get("CheckGreigePlan") != null) {
			CheckGreigePlan = (int) map.get("CheckGreigePlan");
		}

		int CheckFormula = 0;
		if (map.get("CheckFormula") != null) {
			CheckFormula = (int) map.get("CheckFormula");
		}

		String MaterialNoTWC = "";
		if (map.get("MaterialNoTWC") != null) {
			MaterialNoTWC = (String) map.get("MaterialNoTWC");
		}
		String LabRef = "";
		if (map.get("LabRef") != null) {
			LabRef = (String) map.get("LabRef");
		}
		String ItemNote = "";
		if (map.get("ItemNote") != null) {
			ItemNote = (String) map.get("ItemNote");
		}
		String ProductionMemo = "";
		if (map.get("ProductionMemo") != null) {
			ProductionMemo = (String) map.get("ProductionMemo");
		}
		String ModelCode = "";
		if (map.get("ModelCode") != null) {
			ModelCode = (String) map.get("ModelCode");
		}
		String LabRefLotNo = "";
		if (map.get("LabRefLotNo") != null) {
			LabRefLotNo = (String) map.get("LabRefLotNo");
		}

		String PODate = "";
		if (map.get("PODate") != null) {
			java.util.Date dateStr = (Date) map.get("PODate");
			PODate = sdf2.format(dateStr);
		}

		String UpdateDate = "";
		if (map.get("UpdateDate") != null) {
			java.util.Date dateStr = (Date) map.get("UpdateDate");
			UpdateDate = sdf2.format(dateStr);
		}
		String UpdateBy = "";
		if (map.get("UpdateBy") != null) {
			UpdateBy = (String) map.get("UpdateBy");
		}
		boolean IsUpdate = false;
		if (map.get("IsUpdate") != null) {
			IsUpdate = (boolean) map.get("IsUpdate");
		}

		int totalAll = 0;
		if (map.get("totalAll") != null) {
			totalAll = (int) map.get("totalAll");
		}

		int totalError = 0;
		if (map.get("totalError") != null) {
			totalError = (int) map.get("totalError");
		}
		int totalOrderQtyMod = 0;
		if (map.get("totalOrderQtyMod") != null) {
			totalOrderQtyMod = (int) map.get("totalOrderQtyMod");
		}
		int totalGroupRecheck = 0;
		if (map.get("totalGroupRecheck") != null) {
			totalGroupRecheck = (int) map.get("totalGroupRecheck");
		}
		int CheckCustomerNo = 0;
		if (map.get("CheckCustomerNo") != null) {
			CheckCustomerNo = (int) map.get("CheckCustomerNo");
		}
		int checkQtyGreigeForPO = 0;
		if (map.get("CheckQtyGreigeForPO") != null) {
			checkQtyGreigeForPO = (int) map.get("CheckQtyGreigeForPO");
		}
		int CheckColor = 0;
		if (map.get("CheckColor") != null) {
			CheckColor = (int) map.get("CheckColor");
		}
		int CheckCustomerDue = 0;
		if (map.get("CheckCustomerDue") != null) {
			CheckCustomerDue = (int) map.get("CheckCustomerDue");
		}
		int CheckOrderQty = 0;
		if (map.get("CheckOrderQty") != null) {
			CheckOrderQty = (int) map.get("CheckOrderQty");
		}
		int POPuangId = 0;
		if (map.get("POPuangId") != null) {
			POPuangId = (int) map.get("POPuangId");
		}
		int CheckOP = 0;
		if (map.get("checkOP") != null) {
			CheckOP = (int) map.get("checkOP");
		}
		int CountDocDate = 0;
		if (map.get("CountDocDate") != null) {
			CountDocDate = (int) map.get("CountDocDate");
		}
		int CheckSixOutCast = 0;
		if (map.get("checkSixOutCast") != null) {
			CheckSixOutCast = (int) map.get("checkSixOutCast");
		}
		int CheckQtyGreigeMR = 0;
		if (map.get("CheckQtyGreigeMR") != null) {
			CheckQtyGreigeMR = (int) map.get("CheckQtyGreigeMR");
		}
		String QtyGreigeMR = "";
		int IntQtyGreigeMR = 0;
		if (map.get("QtyGreigeMR") != null) {
			int value = (int) map.get("QtyGreigeMR");
			IntQtyGreigeMR = value;
//			System.out.println(IntQtyGreigeMR);
			QtyGreigeMR = Integer.toString(IntQtyGreigeMR);
		}

		int CheckHW = 0;
		if (map.get("CheckHW") != null) {
			CheckHW = (int) map.get("CheckHW");
		}

		String SpecialType = "";
		if (map.get("SpecialType") != null) {
			SpecialType = (String) map.get("SpecialType");
		}
		String ArticleComment = "";
		if (map.get("ArticleComment") != null) {
			ArticleComment = (String) map.get("ArticleComment");
		}
		String DesignCheckOne = "";
		if (map.get("DesignCheckOne") != null) {
			DesignCheckOne = (String) map.get("DesignCheckOne");
		}
		String DesignCheckTwo = "";
		if (map.get("DesignCheckTwo") != null) {
			DesignCheckTwo = (String) map.get("DesignCheckTwo");
		}
		String GroupOptionOne = "";
		if (map.get("GroupOptionsOne") != null) {
			GroupOptionOne = (String) map.get("GroupOptionsOne");
		}
		String GroupOptionTwo = "";
		if (map.get("GroupOptionsTwo") != null) {
			GroupOptionTwo = (String) map.get("GroupOptionsTwo");
		}
		int Variable = 0;
		if (map.get("Variable") != null) {
			Variable = (int) map.get("Variable");
		}
		int SpecialCaseMRId = 0;
		if (map.get("SpecialCaseMRId") != null) {
			SpecialCaseMRId = (int) map.get("SpecialCaseMRId");
		}
		String ColorTypeCheck = "";
		if (map.get("ColorTypeCheck") != null) {
			ColorTypeCheck = (String) map.get("ColorTypeCheck");
		}
		String OrderQtyCalLastString = "";
		if (map.get("OrderQtyCalLastString") != null) {
			OrderQtyCalLastString = (String) map.get("OrderQtyCalLastString");
		}
		String OrderQtyCalLast = "";
		double db_orderQtyCalLast = 0;
		if (map.get("OrderQtyCalLast") != null) {
			BigDecimal value = (BigDecimal) map.get("OrderQtyCalLast");
			db_orderQtyCalLast = value.doubleValue();
			OrderQtyCalLast = this.df2.format(db_orderQtyCalLast);
		}
		boolean isRecheck = false;
		if (map.get("isRecheck") != null) {
			isRecheck = (boolean) map.get("isRecheck");
		}
		boolean isGroupRecheck = false;
		if (map.get("isGroupRecheck") != null) {
			isGroupRecheck = (boolean) map.get("isGroupRecheck");
		}
		String POType = "";
		double POMaxQty = 0;
		double POMinQty = 0;
		double POSumLowerQty = 0;
		int POLowerQty = 0;
		int POUpperQty = 0;
		if (map.get("POType") != null) {
			POType = (String) map.get("POType");
		}
		if (map.get("POMaxQty") != null) {
			BigDecimal value = (BigDecimal) map.get("POMaxQty");
			POMaxQty = value.doubleValue();
		}
		if (map.get("POMinQty") != null) {
			BigDecimal value = (BigDecimal) map.get("POMinQty");
			POMinQty = value.doubleValue();
		}
		if (map.get("POLowerQty") != null) {
			POLowerQty = (int) map.get("POLowerQty");
		}
		if (map.get("POUpperQty") != null) {
			POUpperQty = (int) map.get("POUpperQty");
		}
		if (map.get("POSumLowerQty") != null) {
			BigDecimal value = (BigDecimal) map.get("POSumLowerQty");
			POSumLowerQty = value.doubleValue();
		}

		String OrderQtyCalLastPlan = "";
		double db_orderQtyCalLastPlan = 0;
		if (map.get("OrderQtyCalLastPlan") != null) {
			BigDecimal value = (BigDecimal) map.get("OrderQtyCalLastPlan");
			db_orderQtyCalLastPlan = value.doubleValue();
			OrderQtyCalLastPlan = this.df2.format(db_orderQtyCalLastPlan);
		}

		boolean isLastWorkNotInSixMonth = false;
		if (map.get("isLastWorkNotInSixMonth") != null) {
			isLastWorkNotInSixMonth = (boolean) map.get("isLastWorkNotInSixMonth");
		}

		String referenceId = "";
		if (map.get("ReferenceId") != null) {
			referenceId = (String) map.get("ReferenceId");
		}
		String dataStatus = "";
		if (map.get("DataStatus") != null) {
			dataStatus = (String) map.get("DataStatus");
		}
		boolean IsDelete = false;
		if (map.get("IsDelete") != null) {
			IsDelete = (boolean) map.get("IsDelete");
		}
		int id = 0;
		if (map.get("Id") != null) {
			id = (int) map.get("Id");
		}
		boolean isLotInSap = false;
		if (map.get("IsLotInSap") != null) {
			isLotInSap = (boolean) map.get("IsLotInSap");
		}
		boolean isDCOrderPuang = false;
		if (map.get("IsDCOrderPuang") != null) {
			isDCOrderPuang = (boolean) map.get("IsDCOrderPuang");
		}
		int totalPending = totalAll-totalError-totalOrderQtyMod-totalGroupRecheck;
		int CheckError = CheckDivision
				+CheckArticle
				+CheckArticleWeight
				+CheckGreigePlan
				+CountProd
				+CheckFormula
				+CheckCustomerNo
				+CheckColor
				+CheckCustomerDue
				+CheckOrderQty;
		InputPODetail bean = new InputPODetail(No, PO, POLine, Division, SaleOrg, Design, Article, MaterialNo, CustomerNo,
				CustomerName, ColorCustomer, Color, OrderQty, Unit, CustomerDue, GreigePlan, OrderPrice, OverCap, DocDate,
				ChangeBy, ChangeDate, CountProd, CheckDivision, CheckArticle, CheckArticleWeight, ErrorStatus, IsCotton,
				DistChannal, DyeAfterGreigeInBegin, DyeAfterGreigeInLast, CountWorkDate, MaterialNoTWC, LabRef, ItemNote,
				ProductionMemo, ModelCode, LabRefLotNo, PODate, UpdateBy, UpdateDate, IsUpdate, CheckGreigePlan, CheckFormula,
				CheckCustomerNo, CheckColor, CheckCustomerDue, CheckOrderQty, CheckError, POPuangId, CheckSixOutCast, CheckOP,
				CountDocDate, OrderQtyCal, QtyGreigeMR, IntQtyGreigeMR, CheckQtyGreigeMR, CheckHW, ColorType, SpecialType,
				ArticleComment, Variable, DesignCheckOne, DesignCheckTwo, GroupOptionOne, GroupOptionTwo, SpecialCaseMRId,
				ColorTypeCheck, isRecheck, OrderQtyCalLastString, OrderQtyCalLast, isGroupRecheck, POType, POMaxQty, POMinQty,
				POSumLowerQty, POLowerQty, POUpperQty, OrderQtyCalLastPlan, isLastWorkNotInSixMonth, IsDelete, referenceId, id,
				articleReal, changedOrderQty, dataStatus, isLotInSap, isDCOrderPuang, checkQtyGreigeForPO);
		bean.setDb_orderQtyCalLastPlan(db_orderQtyCalLastPlan);
		bean.setDb_orderQtyCalLast(db_orderQtyCalLast);
		bean.setTotalAll(totalAll);
		bean.setTotalPending(totalPending);
		bean.setTotalError(totalError);// here
		bean.setTotalOrderQtyMod(totalOrderQtyMod);// here
		bean.setTotalGroupRecheck(totalGroupRecheck);
//		bean.setOrderQtyMod(isOrderQtyMod);
		return bean;
	}

	@Override
	public POManagementDetail _genPOManagementDetail(Map<String, Object> map)
	{
		String ReferenceId = "";
		if (map.get("ReferenceId") != null) {
			ReferenceId = (String) map.get("ReferenceId");
		}
		int relationId = 0;
		if (map.get("RelationId") != null) {
			relationId = (int) map.get("RelationId");
		}
		int planLotSorId = 0;
		if (map.get("PlanLotSorId") != null) {
			planLotSorId = (int) map.get("PlanLotSorId");
		}
		int POId = 0;
		if (map.get("POId") != null) {
			POId = (int) map.get("POId");
		}
		String PO = "";
		if (map.get("PO") != null) {
			PO = (String) map.get("PO");
		}
		String POLine = "";
		if (map.get("POLine") != null) {
			POLine = (String) map.get("POLine");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String GreigePlan = "";
		if (map.get("GreigePlan") != null) {
			java.util.Date dateStr = (Date) map.get("GreigePlan");
			GreigePlan = sdf2.format(dateStr);
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = this.sdf2.format(dateStr);
		}
		String SORCFMDate = "";
		if (map.get("SORCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORCFMDate");
			SORCFMDate = this.sdf2.format(dateStr);
		}
		String SORDueDate = "";
		if (map.get("SORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORDueDate");
			SORDueDate = this.sdf2.format(dateStr);
		}
		int POPuangId = 0;
		if (map.get("POPuangId") != null) {
			POPuangId = (int) map.get("POPuangId");
		}
		int TempProdId = 0;
		if (map.get("TempProdId") != null) {
			TempProdId = (int) map.get("TempProdId");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String GroupBegin = "";
		if (map.get("GroupBegin") != null) {
			GroupBegin = (String) map.get("GroupBegin");
		}
		String ProdOrderQty = "";
		double DBProdQty = 0;
		if (map.get("ProdOrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ProdOrderQty");
			DBProdQty = value.doubleValue();
			ProdOrderQty = this.df2.format(DBProdQty);
		}
		int TempPlanningId = 0;
		if (map.get("TempPlanningId") != null) {
			TempPlanningId = (int) map.get("TempPlanningId");
		}
		int ApprovedPlanDateId = 0;
		if (map.get("ApprovedPlanDateId") != null) {
			ApprovedPlanDateId = (int) map.get("ApprovedPlanDateId");
		}
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		String PlanSystemDate = "";
		if (map.get("PlanSystemDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanSystemDate");
			PlanSystemDate = this.sdf2.format(dateStr);
		}
//		String DocDate = "";
//		if (map.get("DocDate") != null) {
//			java.util.Date dateStr = (Date) map.get("DocDate");
//			DocDate = sdf2.format(dateStr);
//		}
		String approvedBy = "";
		if (map.get("ApprovedBy") != null) {
			approvedBy = (String) map.get("ApprovedBy");
		}
		boolean isApproved = false;
		if (map.get("IsApproved") != null) {
			isApproved = (boolean) map.get("IsApproved");
		}
		String article = "";
		if (map.get("Article") != null) {
			article = (String) map.get("Article");
		}
		String changeBy = "";
		if (map.get("ChangeBy") != null) {
			changeBy = (String) map.get("ChangeBy");
		}
		boolean isReverseAble = false;
		if (map.get("IsReverseAble") != null) {
			isReverseAble = (boolean) map.get("IsReverseAble");
		}
		boolean isInSap = false;
		if (map.get("IsInSap") != null) {
			isInSap = (boolean) map.get("IsInSap");
		}
		String approvedDataStatus = "";
		if (map.get("ApprovedDataStatus") != null) {
			approvedDataStatus = (String) map.get("ApprovedDataStatus");
		}
		String productionOrderType = "";
		if (map.get("ProductionOrderType") != null) {
			productionOrderType = (String) map.get("ProductionOrderType");
		}
		String colorType = "";
		if (map.get("ColorType") != null) {
			colorType = (String) map.get("ColorType");
		}
		return new POManagementDetail(ReferenceId, POId, PO, POLine, MaterialNo, CustomerNo, CustomerName, CustomerDue,
				GreigePlan, SORCFMDate, SORDueDate, POPuangId, ProductionOrder, GroupBegin, ProdOrderQty, GroupNo, SubGroup,
				PlanSystemDate, TempPlanningId, TempProdId, isApproved, ApprovedPlanDateId, approvedBy, article, changeBy,
				isReverseAble, isInSap, relationId, planLotSorId, approvedDataStatus, productionOrderType, colorType)

		;
	}

	@Override
	public POStatusReportDetail _genPOStatusReportDetail(Map<String, Object> map)
	{

		String ApprovedDate = "";
		if (map.get("ApprovedDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ApprovedDate");
			ApprovedDate = this.sdf3.format(timestamp1);
		}
		String PODate = "";
		if (map.get("DocDate") != null) {
			java.util.Date dateStr = (Date) map.get("DocDate");
			PODate = sdf2.format(dateStr);
		}
		String PO = "";
		if (map.get("PO") != null) {
			PO = (String) map.get("PO");
		}
		String POLine = "";
		if (map.get("POLine") != null) {
			POLine = (String) map.get("POLine");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String Design = "";
		if (map.get("Design") != null) {
			Design = (String) map.get("Design");
		}
		String ColorCustomer = "";
		if (map.get("ColorCustomer") != null) {
			ColorCustomer = (String) map.get("ColorCustomer");
		}
		String OrderQty = "";
		if (map.get("OrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("OrderQty");
			double doubleVal = value.doubleValue();
			OrderQty = this.df2.format(doubleVal);
		}
		String Unit = "";
		if (map.get("Unit") != null) {
			Unit = (String) map.get("Unit");
		}
		String customerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			customerDue = sdf2.format(dateStr);
		}
		String GreigePlan = "";
		if (map.get("GreigePlan") != null) {
			java.util.Date dateStr = (Date) map.get("GreigePlan");
			GreigePlan = sdf2.format(dateStr);
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String ProdOrderQty = "";
		double DBProdQty = 0;
		if (map.get("ProdOrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ProdOrderQty");
			DBProdQty = value.doubleValue();
			ProdOrderQty = this.df2.format(DBProdQty);

		}
		String FirstLot = "";
		if (map.get("FirstLot") != null) {
			FirstLot = (String) map.get("FirstLot");
		}
		String PPMMStatus = "";
		if (map.get("PPMMStatus") != null) {
			PPMMStatus = (String) map.get("PPMMStatus");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String DocDate = "";
		if (map.get("DocDate") != null) {
			java.util.Date dateStr = (Date) map.get("DocDate");
			DocDate = sdf2.format(dateStr);
		}
		String POType = "";
		if (map.get("POType") != null) {
			POType = (String) map.get("POType");
		}
		String CustomerMat = "";
		if (map.get("CustomerMat") != null) {
			CustomerMat = (String) map.get("CustomerMat");
		}
		String Batch = "";
		if (map.get("Batch") != null) {
			Batch = (String) map.get("Batch");
		}

		String QtyGreigeMR = "";
		double DBQtyGreigeMR = 0;
		if (map.get("QtyGreigeMR") != null) {
			BigDecimal value = (BigDecimal) map.get("QtyGreigeMR");
			DBQtyGreigeMR = value.doubleValue();
			QtyGreigeMR = this.df2.format(DBQtyGreigeMR);
		}
		String OrderQtyCalLast = "";
		double db_orderQtyCalLast = 0;
		if (map.get("OrderQtyCalLast") != null) {
			BigDecimal value = (BigDecimal) map.get("OrderQtyCalLast");
			db_orderQtyCalLast = value.doubleValue();
			OrderQtyCalLast = this.df2.format(db_orderQtyCalLast);
		}
		String POPuangMain = "";
		if (map.get("POPuangMain") != null) {
			POPuangMain = (String) map.get("POPuangMain");
		}
		String POLinePuangMain = "";
		if (map.get("POLinePuangMain") != null) {
			POLinePuangMain = (String) map.get("POLinePuangMain");
		}
		String CustomerDuePuangMain = "";
		if (map.get("CustomerDuePuangMain") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDuePuangMain");
			CustomerDuePuangMain = sdf2.format(dateStr);
		}
		String VolumeFG = "";
		if (map.get("VolumeFG") != null) {
			BigDecimal value = (BigDecimal) map.get("VolumeFG");
			double db_volume = value.doubleValue();
			VolumeFG = this.df2.format(db_volume);
		}

		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		int TotalPO = 0;
		if (map.get("TotalPO") != null) {
			TotalPO = (int) map.get("TotalPO");
		}

		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String LabNo = "";
		if (map.get("LabNo") != null) {
			LabNo = (String) map.get("LabNo");
		}
		String Shade = "";
		if (map.get("Shade") != null) {
			Shade = (String) map.get("Shade");
		}
		String BookNo = "";
		if (map.get("BookNo") != null) {
			BookNo = (String) map.get("BookNo");
		}

		String SORDueDate = "";
		if (map.get("SORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORDueDate");
			SORDueDate = sdf2.format(dateStr);
		}
		String SORCFMDate = "";
		if (map.get("SORCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORCFMDate");
			SORCFMDate = sdf2.format(dateStr);
		}
		String POIdPuangMain = "";
		if (map.get("POIdPuangMain") != null) {
			int value = (int) map.get("POIdPuangMain");
			POIdPuangMain = Integer.toString(value);
		}
		return new POStatusReportDetail(ApprovedDate, PO, POLine, CustomerName, MaterialNo, Design, ColorCustomer, OrderQty, Unit,
				customerDue, GreigePlan, ProductionOrder, ProdOrderQty, FirstLot, PPMMStatus, PODate, CustomerMat, DocDate,
				POType, Article, Batch, OrderQtyCalLast, QtyGreigeMR, POPuangMain, POLinePuangMain, CustomerDuePuangMain,
				VolumeFG, SaleOrder, TotalPO, SaleLine, SORDueDate, SORCFMDate, LabNo, Shade, BookNo, POIdPuangMain);
	}

	@Override
	public ProdOrderRunningDetail _genProdOrderRunningDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		int id = 0;
		if (map.get("Id") != null) {
			id = (int) map.get("Id");
		}
		String keyWord = "";
		if (map.get("KeyWord") != null) {
			keyWord = (String) map.get("KeyWord");
		}
		String remark = "";
		if (map.get("Remark") != null) {
			remark = (String) map.get("Remark");
		}
		String changeBy = "";
		if (map.get("ChangeBy") != null) {
			changeBy = (String) map.get("ChangeBy");
		}
		String dataStatus = "O";
		if (map.get("DataStatus") != null) {
			dataStatus = (String) map.get("DataStatus");
		}

		String changeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			changeDate = this.sdf3.format(timestamp1);
		}
		String ProductionOrderTemp = "";
		if (map.get("ProductionOrderTemp") != null) {
			ProductionOrderTemp = (String) map.get("ProductionOrderTemp");
		}
		boolean isTopping = false;
		if (map.get("IsTopping") != null) {
			isTopping = (boolean) map.get("IsTopping");
		}
		boolean isSapCreated = false;
		if (map.get("IsSapCreated") != null) {
			isSapCreated = (boolean) map.get("IsSapCreated");
		}
		boolean isSorLotOnProcess = false;
		if (map.get("IsSorLotOnProcess") != null) {
			isSorLotOnProcess = (boolean) map.get("IsSorLotOnProcess");
		}
		return new ProdOrderRunningDetail(id, keyWord, ProductionOrder, ProductionOrderTemp, remark, changeBy, changeDate,
				dataStatus, isTopping, isSapCreated, isSorLotOnProcess);
	}

	@Override
	public RecreateRedyeReportDetail _genRecreateRedyeReportDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = sdf2.format(dateStr);
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateStr = (Date) map.get("DueDate");
			DueDate = sdf2.format(dateStr);
		}
		String MaterialNumber = "";
		if (map.get("MaterialNumber") != null) {
			MaterialNumber = (String) map.get("MaterialNumber");
		}
		String CustomerMat = "";
		if (map.get("CustomerMat") != null) {
			CustomerMat = (String) map.get("CustomerMat");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {
			double doubleVal = (double) map.get("QuantityKG");
			QuantityKG = this.df2.format(doubleVal);
		}
		String QuantityMR = "";
		if (map.get("QuantityMR") != null) {
			double doubleVal = (double) map.get("QuantityMR");
			QuantityMR = this.df2.format(doubleVal);
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String remarkTwo = "";
		if (map.get("RemarkTwo") != null) {
			remarkTwo = (String) map.get("RemarkTwo");
		}
		String PlanGreigeDate = "";
		if (map.get("PlanGreigeDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanGreigeDate");
			PlanGreigeDate = sdf2.format(dateStr);
		}
		String greigeInDate = "";
		if (map.get("GreigeInDate") != null) {
			java.util.Date dateStr = (Date) map.get("GreigeInDate");
			greigeInDate = sdf2.format(dateStr);
		}
		String prodOrderBook = "";
		if (map.get("ProdOrderBook") != null) {
			prodOrderBook = (String) map.get("ProdOrderBook");
		}
		return new RecreateRedyeReportDetail(ProductionOrder, LotNo, DueDate, MaterialNumber, CustomerMat, CustomerName,
				QuantityKG, QuantityMR, UserStatus, LabStatus, PlanGreigeDate, remarkTwo, prodOrderBook, CustomerDue,
				greigeInDate);
	}

	@Override
	public RedyePlanningReportDetail _genRedyePlanningReportDetail(Map<String, Object> map)
	{
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateStr = (Date) map.get("DueDate");
			DueDate = sdf2.format(dateStr);
		}
		String MaterialNumber = "";
		if (map.get("MaterialNumber") != null) {
			MaterialNumber = (String) map.get("MaterialNumber");
		}
		String CustomerMat = "";
		if (map.get("CustomerMat") != null) {
			CustomerMat = (String) map.get("CustomerMat");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {
//			double doubleVal = (double) map.get("QuantityKG");
//			QuantityKG = this.df2.format(doubleVal);
			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			double db_value = value.doubleValue();
			QuantityKG = this.df2.format(db_value);
		}
		String QuantityMR = "";
		if (map.get("QuantityMR") != null) {
//			double doubleVal = (double) map.get("QuantityMR");
//			QuantityMR = this.df2.format(doubleVal);

			BigDecimal value = (BigDecimal) map.get("QuantityMR");
			double db_value = value.doubleValue();
			QuantityMR = this.df2.format(db_value);
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		String PlanDate = "";
		if (map.get("PlanSystemDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanSystemDate");
			PlanDate = sdf2.format(dateStr);
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = sdf2.format(dateStr);
		}
		String PlanGreigeDate = "";
		if (map.get("PlanGreigeDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanGreigeDate");
			PlanGreigeDate = sdf2.format(dateStr);
		}
		String greigeInDate = "";
		if (map.get("GreigeInDate") != null) {
			java.util.Date dateStr = (Date) map.get("GreigeInDate");
			greigeInDate = sdf2.format(dateStr);
		}
		return new RedyePlanningReportDetail(ProductionOrder, LotNo, DueDate, MaterialNumber, CustomerMat, CustomerName,
				QuantityKG, QuantityMR, UserStatus, LabStatus, GroupNo, SubGroup, PlanDate, CustomerDue, PlanGreigeDate,
				greigeInDate);
	}

	@Override
	public ResendSORDateDetail _genResendSORDateDetail(Map<String, Object> map)
	{
		String ReferenceId = "";
		if (map.get("ReferenceId") != null) {
			ReferenceId = (String) map.get("ReferenceId");
		}
		int relationId = 0;
		if (map.get("RelationId") != null) {
			relationId = (int) map.get("RelationId");
		}
		int planLotSorId = 0;
		if (map.get("PlanLotSorId") != null) {
			planLotSorId = (int) map.get("PlanLotSorId");
		}
		int POId = 0;
		if (map.get("POId") != null) {
			POId = (int) map.get("POId");
		}
		String PO = "";
		if (map.get("PO") != null) {
			PO = (String) map.get("PO");
		}
		String POLine = "";
		if (map.get("POLine") != null) {
			POLine = (String) map.get("POLine");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String GreigePlan = "";
		if (map.get("GreigePlan") != null) {
			java.util.Date dateStr = (Date) map.get("GreigePlan");
			GreigePlan = sdf2.format(dateStr);
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = this.sdf2.format(dateStr);
		}
		String SORCFMDate = "";
		if (map.get("SORCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORCFMDate");
			SORCFMDate = this.sdf2.format(dateStr);
		}
		String SORDueDate = "";
		if (map.get("SORDueDate") != null) {
			java.util.Date dateStr = (Date) map.get("SORDueDate");
			SORDueDate = this.sdf2.format(dateStr);
		}
		int POPuangId = 0;
		if (map.get("POPuangId") != null) {
			POPuangId = (int) map.get("POPuangId");
		}
		int TempProdId = 0;
		if (map.get("TempProdId") != null) {
			TempProdId = (int) map.get("TempProdId");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
//		String GroupBegin = "";
//		if (map.get("GroupBegin") != null) {
//			GroupBegin = (String) map.get("GroupBegin");
//		}
//		String ProdOrderQty = "";
//		double DBProdQty = 0 ;
//		if (map.get("ProdOrderQty") != null) {
//			BigDecimal value = (BigDecimal) map.get("ProdOrderQty");
//			DBProdQty = value.doubleValue();
//			ProdOrderQty = this.df2.format(DBProdQty);
//		}
		int TempPlanningId = 0;
		if (map.get("TempPlanningId") != null) {
			TempPlanningId = (int) map.get("TempPlanningId");
		}
		int ApprovedPlanDateId = 0;
		if (map.get("ApprovedPlanDateId") != null) {
			ApprovedPlanDateId = (int) map.get("ApprovedPlanDateId");
		}
//		String GroupNo = "";
//		if (map.get("GroupNo") != null) {
//			GroupNo = (String) map.get("GroupNo");
//		}
//		String SubGroup = "";
//		if (map.get("SubGroup") != null) {
//			SubGroup = (String) map.get("SubGroup");
//		}
//		String PlanSystemDate = "";
//		if (map.get("PlanSystemDate") != null) {
//			java.util.Date dateStr = (Date)map.get("PlanSystemDate");
//		    PlanSystemDate = this.sdf2.format(dateStr);
//		}
//		String DocDate = "";
//		if (map.get("DocDate") != null) {
//			java.util.Date dateStr = (Date) map.get("DocDate");
//			DocDate = sdf2.format(dateStr);
//		}
		String approvedBy = "";
		if (map.get("ApprovedBy") != null) {
			approvedBy = (String) map.get("ApprovedBy");
		}
		boolean isApproved = false;
		if (map.get("IsApproved") != null) {
			isApproved = (boolean) map.get("IsApproved");
		}
		String article = "";
		if (map.get("Article") != null) {
			article = (String) map.get("Article");
		}
		String changeBy = "";
		if (map.get("ChangeBy") != null) {
			changeBy = (String) map.get("ChangeBy");
		}
		boolean isInSap = false;
		if (map.get("IsInSap") != null) {
			isInSap = (boolean) map.get("IsInSap");
		}
		String approvedDataStatus = "";
		if (map.get("ApprovedDataStatus") != null) {
			approvedDataStatus = (String) map.get("DataStatus");
		}
		String CurrentTempLot = "";
		if (map.get("CurrentTempLot") != null) {
			CurrentTempLot = (String) map.get("CurrentTempLot");
		}
		String DataStatus = "";
		if (map.get("DataStatus") != null) {
			DataStatus = (String) map.get("DataStatus");
		}
		String POType = "";
		if (map.get("POType") != null) {
			POType = (String) map.get("POType");
		}
		return new ResendSORDateDetail(ReferenceId, POId, TempProdId, TempPlanningId, ApprovedPlanDateId, relationId,
				planLotSorId, PO, POLine, MaterialNo, article, CustomerNo, CustomerName, CustomerDue, GreigePlan, SORCFMDate,
				SORDueDate, CurrentTempLot, SORCFMDate, SORDueDate, CurrentTempLot, changeBy, approvedBy, isApproved, isInSap, "",
				"", POPuangId, DataStatus, approvedDataStatus, POType, ProductionOrder);
	}

	@Override
	public SO_DOC_STEPDetail _genSO_DOC_STEPDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}
		BigDecimal SALES_ID = null;
		if (map.get("SALES_ID") != null) {
//			SALES_ID = (int) map.get("SALES_ID");
			SALES_ID = (BigDecimal) map.get("SALES_ID");
		}
		int STEP_ID = 0;
		if (map.get("STEP_ID") != null) {
			STEP_ID = (int) map.get("STEP_ID");
		}
		String STEP_REQ_ON = "";
		if (map.get("STEP_REQ_ON") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("STEP_REQ_ON");
			STEP_REQ_ON = this.sdf3.format(timestamp1);
		}
		String STEP_REQ_BY = "";
		if (map.get("STEP_REQ_BY") != null) {
			STEP_REQ_BY = (String) map.get("STEP_REQ_BY");
		}
		String STEP_APPV_ON = "";
		if (map.get("STEP_APPV_ON") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("STEP_APPV_ON");
			STEP_APPV_ON = this.sdf3.format(timestamp1);
		}
		String STEP_APPV_BY = "";
		if (map.get("STEP_APPV_BY") != null) {
			STEP_APPV_BY = (String) map.get("STEP_APPV_BY");
		}
		String WAIT_APPV = "";
		if (map.get("WAIT_APPV") != null) {
			WAIT_APPV = (String) map.get("WAIT_APPV");
		}
		String REJECT = "";
		if (map.get("REJECT") != null) {
			REJECT = (String) map.get("REJECT");
		}
		String APPROVE = "";
		if (map.get("APPROVE") != null) {
			APPROVE = (String) map.get("APPROVE");
		}
		String REVIEW = "";
		if (map.get("REVIEW") != null) {
			REVIEW = (String) map.get("REVIEW");
		}
		int STEP_STATUS_ID = 0;
		if (map.get("STEP_STATUS_ID") != null) {
			STEP_STATUS_ID = (int) map.get("STEP_STATUS_ID");
		}
		int RE_REVIEW = 0;
		if (map.get("RE_REVIEW") != null) {
			RE_REVIEW = (int) map.get("RE_REVIEW");
		}
		String SALES_ORDER_NO = "";
		if (map.get("SALES_ORDER_NO") != null) {
			SALES_ORDER_NO = (String) map.get("SALES_ORDER_NO");
		}
		return new SO_DOC_STEPDetail(Id, SALES_ID, STEP_ID, STEP_REQ_ON, STEP_REQ_BY, STEP_APPV_ON, STEP_APPV_BY, WAIT_APPV,
				REJECT, APPROVE, REVIEW, STEP_STATUS_ID, RE_REVIEW, SALES_ORDER_NO);
	}

	@Override
	public SOR_DUE_REPORTDetail _genSOR_DUE_REPORTDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub

		String PO_DATE = "";
		if (map.get("PO_DATE") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("PO_DATE");
			PO_DATE = this.sdf3.format(timestamp1);
		}
		String PO_NO = "";
		if (map.get("PO_NO") != null) {
			PO_NO = (String) map.get("PO_NO");
		}
		String SO_NO = "";
		if (map.get("SO_NO") != null) {
			SO_NO = (String) map.get("SO_NO");
		}
		String SO_LINE = "";
		if (map.get("SO_LINE") != null) {
			SO_LINE = (String) map.get("SO_LINE");
		}
		String MATERIAL_CODE = "";
		if (map.get("MATERIAL_CODE") != null) {
			MATERIAL_CODE = (String) map.get("MATERIAL_CODE");
		}
		String REFERENCE_ID = "";
		if (map.get("REFERENCE_ID") != null) {
			REFERENCE_ID = (String) map.get("REFERENCE_ID");
		}
		String GREIGE_UPDATED = "";
		if (map.get("GREIGE_UPDATED") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("GREIGE_UPDATED");
			GREIGE_UPDATED = this.sdf3.format(timestamp1);
		}
		String CFM_UPDATED = "";
		if (map.get("CFM_UPDATED") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("CFM_UPDATED");
			CFM_UPDATED = this.sdf3.format(timestamp1);
		}
		return new SOR_DUE_REPORTDetail(PO_DATE, PO_NO, SO_NO, SO_LINE, MATERIAL_CODE, REFERENCE_ID, GREIGE_UPDATED, CFM_UPDATED);

	}

	@Override
	public InputSpecialCaseMRDetail _genSpecialCaseMRDetail(Map<String, Object> map)
	{
		String No = "";
		if (map.get("No") != null) {
			int val = (int) map.get("No");
			No = Integer.toString(val);
		}
		String SpecialType = "";
		if (map.get("SpecialType") != null) {
			SpecialType = (String) map.get("SpecialType");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String DesignCheckOne = "";
		if (map.get("DesignCheckOne") != null) {
			DesignCheckOne = (String) map.get("DesignCheckOne");
		}
		String DesignCheckTwo = "";
		if (map.get("DesignCheckTwo") != null) {
			DesignCheckTwo = (String) map.get("DesignCheckTwo");
		}
		String GroupOptionsOne = "";
		if (map.get("GroupOptionsOne") != null) {
			GroupOptionsOne = (String) map.get("GroupOptionsOne");
		}
		String GroupOptionsTwo = "";
		if (map.get("GroupOptionsTwo") != null) {
			GroupOptionsTwo = (String) map.get("GroupOptionsTwo");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String ArticleComment = "";
		if (map.get("ArticleComment") != null) {
			ArticleComment = (String) map.get("ArticleComment");
		}
		String ArticleReplaced = "";
		if (map.get("ArticleReplaced") != null) {
			ArticleReplaced = (String) map.get("ArticleReplaced");
		}
		int Variable = 0;
		if (map.get("Variable") != null) {
			Variable = (int) map.get("Variable");
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		return new InputSpecialCaseMRDetail(No, SpecialType, Article, DesignCheckOne, ChangeBy, ChangeDate, ArticleComment,
				Variable, ArticleReplaced, Remark, GroupOptionsOne, DesignCheckTwo, GroupOptionsTwo);
	}

	@Override
	public InputStockCustomerDateDetail _genStockCustomerDateDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
		int id = 0;
		if (map.get("Id") != null) {
			id = (int) map.get("Id");
		}
		String CustomerNo = "";
		if (map.get("CustomerNo") != null) {
			CustomerNo = (String) map.get("CustomerNo");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerShortName = "";
		if (map.get("CustomerShortName") != null) {
			CustomerShortName = (String) map.get("CustomerShortName");
		}
		String DayOfMonth = "";
		if (map.get("DayOfMonth") != null) {
			DayOfMonth = (String) map.get("DayOfMonth");
		}
		String StockReceive = "";
		if (map.get("StockReceive") != null) {
			StockReceive = (String) map.get("StockReceive");
		}
		String StockRemark = "";
		if (map.get("StockRemark") != null) {
			StockRemark = (String) map.get("StockRemark");
		}
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		return new InputStockCustomerDateDetail(id, CustomerNo, CustomerShortName, StockReceive, StockRemark, DayOfMonth,
				ChangeBy, CustomerName);
	}

	@Override
	public StockReceiveDetail _genStockReceiveDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub // TODO Auto-generated method stub
		int id = 0;
		if (map.get("Id") != null) {
			id = (int) map.get("Id");
		}
		String detail = "";
		if (map.get("Detail") != null) {
			detail = (String) map.get("Detail");
		}
		String detailThai = "";
		if (map.get("DetailThai") != null) {
			detailThai = (String) map.get("DetailThai");
		}
		String dateName = "";
		if (map.get("DateName") != null) {
			dateName = (String) map.get("DateName");
		}
		String dataStatus = "";
		if (map.get("DataStatus") != null) {
			dataStatus = (String) map.get("DataStatus");
		}
		String changeBy = "";
		if (map.get("ChangeBy") != null) {
			changeBy = (String) map.get("ChangeBy");
		}
		String changeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			changeDate = this.sdf3.format(timestamp1);
		}
		String createBy = "";
		if (map.get("CreateBy") != null) {
			createBy = (String) map.get("CreateBy");
		}
		String createDate = "";
		if (map.get("CreateDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("CreateDate");
			createDate = this.sdf3.format(timestamp1);
		}
		return new StockReceiveDetail(id, detail, detailThai, dateName, dataStatus, changeBy, changeDate, createBy, createDate);
	}

	@Override
	public StockReceiveDateDetail _genStockReceiveDateDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub // TODO Auto-generated method stub
		int id = 0;
		if (map.get("Id") != null) {
			id = (int) map.get("Id");
		}
		int stockReceive = 0;
		if (map.get("StockReceive") != null) {
			stockReceive = (int) map.get("StockReceive");
		}
		String dateName = "";
		if (map.get("DateName") != null) {
			dateName = (String) map.get("DateName");
		}
		String dataStatus = "";
		if (map.get("DataStatus") != null) {
			dataStatus = (String) map.get("DataStatus");
		}
		return new StockReceiveDateDetail(id, stockReceive, dateName, dataStatus);
	}

	@Override
	public SummaryMonthlyCapReportDetail _genSummaryMonthlyCapReportDetail(Map<String, Object> map)
	{
		String DateBeginLast = "";
		if (map.get("DateBeginLast") != null) {
			DateBeginLast = (String) map.get("DateBeginLast");
		}

//		int int_DaysRemainning = 0;
		String DaysRemainning = "";
		if (map.get("DaysRemainning") != null) {
			int value = (int) map.get("DaysRemainning");
			DaysRemainning = Integer.toString(value);
		}

		double DBTotalPlanSystemDatePO = 0;
		String TotalPlanSystemDatePO = "";
		if (map.get("TotalPlanSystemDatePO") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalPlanSystemDatePO");
			DBTotalPlanSystemDatePO = value.doubleValue();
			TotalPlanSystemDatePO = this.df2.format(DBTotalPlanSystemDatePO);
		}
		double DBTotalPlanSystemDateRedye = 0;
		String TotalPlanSystemDateRedye = "";
		if (map.get("TotalPlanSystemDateRedye") != null) {

			BigDecimal value = (BigDecimal) map.get("TotalPlanSystemDateRedye");
			DBTotalPlanSystemDateRedye = value.doubleValue();
			TotalPlanSystemDateRedye = this.df2.format(DBTotalPlanSystemDateRedye);
//			DBTotalPlanSystemDateRedye = (double) map.get("TotalPlanSystemDateRedye");
//			TotalPlanSystemDateRedye = String.valueOf(DBTotalPlanSystemDateRedye) ;
		}
		double DBTotalFCBL = 0;
		String TotalFCBL = "";
		if (map.get("TotalFCBL") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalFCBL");
			DBTotalFCBL = value.doubleValue();
			TotalFCBL = this.df2.format(DBTotalFCBL);
		}
		double DBTotalFCNonBL = 0;
		String TotalFCNonBL = "";
		if (map.get("TotalFCNonBL") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalFCNonBL");
			DBTotalFCNonBL = value.doubleValue();
			TotalFCNonBL = this.df2.format(DBTotalFCNonBL);
		}
		double DBTotalRemain = 0;
		String TotalRemain = "";
		if (map.get("TotalRemain") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalRemain");
			DBTotalRemain = value.doubleValue();
			TotalRemain = this.df2.format(DBTotalRemain);
		}
		int no = 0;
		if (map.get("No") != null) {
			no = (int) map.get("No");
		}

		String all = "";
		if (map.get("TotalAllCapWork") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalAllCapWork");
			double db_value = value.doubleValue();
			all = this.df2.format(db_value);
		}

		double DBall = 0;
		DBall = DBTotalPlanSystemDatePO+DBTotalPlanSystemDateRedye+DBTotalFCBL+DBTotalFCNonBL+DBTotalRemain;
		if (all.equals("")) {
			all = this.df2.format(DBall);
		}
		return new SummaryMonthlyCapReportDetail(DateBeginLast, DaysRemainning, all, TotalPlanSystemDatePO,
				TotalPlanSystemDateRedye, TotalFCBL, TotalFCNonBL, TotalRemain, no);
	}

	@Override
	public SummaryOSReportDetail _genSummaryOSReportDetail(Map<String, Object> map)
	{
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		String LotPerDay = "0";
		if (map.get("LotPerDay") != null) {
			int value = (int) map.get("LotPerDay");
			LotPerDay = Integer.toString(value);
		}
		String MachineName = "";
		if (map.get("MachineName") != null) {
			MachineName = (String) map.get("MachineName");
		}

		String CountMachine = "0";
		if (map.get("CountMachine") != null) {
			int value = (int) map.get("CountMachine");
			CountMachine = Integer.toString(value);
		}
		String CountFirstRedye = "0";
		if (map.get("CountFirstRedye") != null) {
			int value = (int) map.get("CountFirstRedye");
			CountFirstRedye = Integer.toString(value);
		}
		String SumFirstRedyeQty = "0";
		if (map.get("SumFirstRedyeQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumFirstRedyeQty");
			double db_value = value.doubleValue();
			SumFirstRedyeQty = this.df2.format(db_value);
		}
		String CountFirstPO = "0";
		if (map.get("CountFirstPO") != null) {
			int value = (int) map.get("CountFirstPO");
			CountFirstPO = Integer.toString(value);
		}
		String SumFirstPOQty = "0";
		if (map.get("SumFirstPOQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumFirstPOQty");
			double db_value = value.doubleValue();
			SumFirstPOQty = this.df2.format(db_value);
		}
		String CountFirstPOAdd = "0";
		if (map.get("CountFirstPOAdd") != null) {
			int value = (int) map.get("CountFirstPOAdd");
			CountFirstPOAdd = Integer.toString(value);
		}
		String SumFirstPOAddQty = "0";
		if (map.get("SumFirstPOAddQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumFirstPOAddQty");
			double db_value = value.doubleValue();
			SumFirstPOAddQty = this.df2.format(db_value);
		}
		String TotalCountFirst = "0";
		if (map.get("TotalCountFirst") != null) {
			int value = (int) map.get("TotalCountFirst");
			TotalCountFirst = Integer.toString(value);
		}
		String TotalSumFirst = "0";
		if (map.get("TotalSumFirst") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalSumFirst");
			double db_value = value.doubleValue();
			TotalSumFirst = this.df2.format(db_value);
		}
		String CountSecondRedye = "0";
		if (map.get("CountSecondRedye") != null) {
			int value = (int) map.get("CountSecondRedye");
			CountSecondRedye = Integer.toString(value);
		}
		String SumSecondRedyeQty = "0";
		if (map.get("SumSecondRedyeQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumSecondRedyeQty");
			double db_value = value.doubleValue();
			SumSecondRedyeQty = this.df2.format(db_value);
		}
		String CountSecondPO = "0";
		if (map.get("CountSecondPO") != null) {
			int value = (int) map.get("CountSecondPO");
			CountSecondPO = Integer.toString(value);
		}
		String SumSecondPOQty = "0";
		if (map.get("SumSecondPOQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumSecondPOQty");
			double db_value = value.doubleValue();
			SumSecondPOQty = this.df2.format(db_value);
		}
		String CountSecondPOAdd = "0";
		if (map.get("CountSecondPOAdd") != null) {
			int value = (int) map.get("CountSecondPOAdd");
			CountSecondPOAdd = Integer.toString(value);
		}
		String SumSecondPOAddQty = "0";
		if (map.get("SumSecondPOAddQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumSecondPOAddQty");
			double db_value = value.doubleValue();
			SumSecondPOAddQty = this.df2.format(db_value);
		}
		String TotalCountSecond = "0";
		if (map.get("TotalCountSecond") != null) {
			int value = (int) map.get("TotalCountSecond");
			TotalCountSecond = Integer.toString(value);
		}
		String TotalSumSecond = "0";
		if (map.get("TotalSumSecond") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalSumSecond");
			double db_value = value.doubleValue();
			TotalSumSecond = this.df2.format(db_value);
		}
		String CountThirdRedye = "0";
		if (map.get("CountThirdRedye") != null) {
			int value = (int) map.get("CountThirdRedye");
			CountThirdRedye = Integer.toString(value);
		}
		String SumThirdRedyeQty = "0";
		if (map.get("SumThirdRedyeQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumThirdRedyeQty");
			double db_value = value.doubleValue();
			SumThirdRedyeQty = this.df2.format(db_value);
		}
		String CountThirdPO = "0";
		if (map.get("CountThirdPO") != null) {
			int value = (int) map.get("CountThirdPO");
			CountThirdPO = Integer.toString(value);
		}
		String SumThirdPOQty = "0";
		if (map.get("SumThirdPOQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumThirdPOQty");
			double db_value = value.doubleValue();
			SumThirdPOQty = this.df2.format(db_value);
		}
		String CountThirdPOAdd = "0";
		if (map.get("CountThirdPOAdd") != null) {
			int value = (int) map.get("CountThirdPOAdd");
			CountThirdPOAdd = Integer.toString(value);
		}
		String SumThirdPOAddQty = "0";
		if (map.get("SumThirdPOAddQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumThirdPOAddQty");
			double db_value = value.doubleValue();
			SumThirdPOAddQty = this.df2.format(db_value);
		}
		String TotalCountThird = "0";
		if (map.get("TotalCountThird") != null) {
			int value = (int) map.get("TotalCountThird");
			TotalCountThird = Integer.toString(value);
		}
		String TotalSumThird = "0";
		if (map.get("TotalSumThird") != null) {
			BigDecimal value = (BigDecimal) map.get("TotalSumThird");
			double db_value = value.doubleValue();
			TotalSumThird = this.df2.format(db_value);
		}

		String FirstMonth = "";
		if (map.get("FirstMonth") != null) {
			FirstMonth = (String) map.get("FirstMonth");
		}
		String SecondMonth = "";
		if (map.get("SecondMonth") != null) {
			SecondMonth = (String) map.get("SecondMonth");
		}
		String ThirdMonth = "";
		if (map.get("ThirdMonth") != null) {
			ThirdMonth = (String) map.get("ThirdMonth");
		}
		return new SummaryOSReportDetail(GroupNo, SubGroup, LotPerDay, MachineName, CountMachine, CountFirstRedye,
				SumFirstRedyeQty, CountFirstPO, SumFirstPOQty, CountFirstPOAdd, SumFirstPOAddQty, TotalCountFirst, TotalSumFirst,
				CountSecondRedye, SumSecondRedyeQty, CountSecondPO, SumSecondPOQty, CountSecondPOAdd, SumSecondPOAddQty,
				TotalCountSecond, TotalSumSecond, CountThirdRedye, SumThirdRedyeQty, CountThirdPO, SumThirdPOQty, CountThirdPOAdd,
				SumThirdPOAddQty, TotalCountThird, TotalSumThird, FirstMonth, SecondMonth, ThirdMonth);
	}

	@Override
	public InputTempProdDetail _genTempProdDetail(Map<String, Object> map)
	{
		String No = "";
		if (map.get("No") != null) {
			No = Integer.toString((int) map.get("No"));
		}
		String PO = "";
		if (map.get("PO") != null) {
			PO = (String) map.get("PO");
		}
		String POLine = "";
		if (map.get("POLine") != null) {
			POLine = (String) map.get("POLine");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}

		int POId = 0;
		if (map.get("POId") != null) {
			POId = (int) map.get("POId");
		}
		int ProductionOrderId = 0;
		if (map.get("ProductionOrderId") != null) {
			ProductionOrderId = (int) map.get("ProductionOrderId");
		}
		int RedyeId = 0;
		if (map.get("RedyeId") != null) {
			RedyeId = (int) map.get("RedyeId");
		}
		int ForecastId = 0;
		if (map.get("ForecastId") != null) {
			ForecastId = (int) map.get("ForecastId");
		}

		String ProdOrderQty = "";
		double DBProdQty = 0;
		if (map.get("ProdOrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("ProdOrderQty");
			DBProdQty = value.doubleValue();
			ProdOrderQty = this.df2.format(DBProdQty);
		}
		String FirstLot = "";
		if (map.get("FirstLot") != null) {
			FirstLot = (String) map.get("FirstLot");
		}
		String PPMMStatus = "";
		if (map.get("PPMMStatus") != null) {
			PPMMStatus = (String) map.get("PPMMStatus");
		}
		boolean isApproved = false;
		if (map.get("IsApproved") != null) {
			isApproved = (boolean) map.get("IsApproved");
		}
//		System.out.println(isApproved+" "+ map.get("isfirst"));
		String ChangeBy = "";
		if (map.get("ChangeBy") != null) {
			ChangeBy = (String) map.get("ChangeBy");
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		String ColorType = "";
		if (map.get("ColorType") != null) {
			ColorType = (String) map.get("ColorType");
		}
		String PlanningDye = "";
		if (map.get("PlanningDye") != null) {
			PlanningDye = (String) map.get("PlanningDye");
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = sdf2.format(dateStr);
		}

		String Design = "";
		if (map.get("Design") != null) {
			Design = (String) map.get("Design");
		}
		String LotType = "";
		if (map.get("LotType") != null) {
			LotType = (String) map.get("LotType");
		}
		String ColorCustomer = "";
		if (map.get("ColorCustomer") != null) {
			ColorCustomer = (String) map.get("ColorCustomer");
		}
		String Machine = "";
		if (map.get("Machine") != null) {
			Machine = (String) map.get("Machine");
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}

		int DyeAfterCFM = 0;
		if (map.get("DyeAfterCFM") != null) {
			DyeAfterCFM = (int) map.get("DyeAfterCFM");
		}

		int PlanIndex = 0;
		if (map.get("PlanIndex") != null) {
			PlanIndex = (int) map.get("PlanIndex");
		}

		String CreateBy = "";
		if (map.get("CreateBy") != null) {
			CreateBy = (String) map.get("CreateBy");
		}
		String CreateDate = "";
		if (map.get("CreateDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("CreateDate");
			CreateDate = this.sdf3.format(timestamp1);
		}
		String PlanSystemDate = "";
		if (map.get("PlanSystemDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanSystemDate");
			PlanSystemDate = this.sdf2.format(dateStr);
		}
		String FirstLotDate = "";
		if (map.get("FirstLotDate") != null) {
			java.util.Date dateStr = (Date) map.get("FirstLotDate");
			FirstLotDate = this.sdf2.format(dateStr);
		}
		String PlanUserDate = "";
		if (map.get("PlanUserDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanUserDate");
			PlanUserDate = this.sdf2.format(dateStr);
		}
		String OperationEndDate = "";
		if (map.get("OperationEndDate") != null) {
			java.util.Date dateStr = (Date) map.get("OperationEndDate");
			OperationEndDate = sdf2.format(dateStr);
		}
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}

		String GroupOptions = "";
		if (map.get("GroupOptions") != null) {
			GroupOptions = (String) map.get("GroupOptions");
		}
		List<String> GroupOptionsList = new ArrayList<>();
		String[] splitArray = null;
		if ( ! GroupOptions.equals("")) {
			splitArray = GroupOptions.split(",");
			for (String element : splitArray) {
				GroupOptionsList.add(element);
			}
		}
		String PlanBy = "";
		if (map.get("PlanBy") != null) {
			PlanBy = (String) map.get("PlanBy");
		}

		String GreigePlan = "";
		if (map.get("GreigePlan") != null) {
			java.util.Date dateStr = (Date) map.get("GreigePlan");
			GreigePlan = sdf2.format(dateStr);
		}
		String DyeAfterGreigeInBegin = "";
		if (map.get("DyeAfterGreigeInBegin") != null) {
			java.util.Date dateStr = (Date) map.get("DyeAfterGreigeInBegin");
			DyeAfterGreigeInBegin = this.sdf2.format(dateStr);
		}
		String DyeAfterGreigeInLast = "";
		if (map.get("DyeAfterGreigeInLast") != null) {
			java.util.Date dateStr = (Date) map.get("DyeAfterGreigeInLast");
			DyeAfterGreigeInLast = this.sdf2.format(dateStr);
		}
		String PlanUserChangeDate = "";
		if (map.get("PlanUserChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("PlanUserChangeDate");
			PlanUserChangeDate = this.sdf3.format(timestamp1);
		}
		String FirstLotGroupNo = "";
		if (map.get("FirstLotGroupNo") != null) {
			FirstLotGroupNo = (String) map.get("FirstLotGroupNo");
		}
		String FirstLotSubGroup = "";
		if (map.get("FirstLotSubGroup") != null) {
			FirstLotSubGroup = (String) map.get("FirstLotSubGroup");
		}
		boolean isOverDue = true;
		if (map.get("isOverDue") != null) {
			isOverDue = (Boolean) map.get("isOverDue");
		}
		int TempPlanningId = 0;
		if (map.get("TempPlanningId") != null) {
			TempPlanningId = (int) map.get("TempPlanningId");
		}
		String GroupBegin = "";
		if (map.get("GroupBegin") != null) {
			GroupBegin = (String) map.get("GroupBegin");
		}
		String MaterialNo = "";
		if (map.get("MaterialNo") != null) {
			MaterialNo = (String) map.get("MaterialNo");
		}
		String Operation = "";
		if (map.get("Operation") != null) {
//			Operation = (int) map.get("Operation");
			Operation = Integer.toString((int) map.get("Operation"));
		}
		boolean isInSap = false;
		if (map.get("isInSap") != null) {
//			Operation = (int) map.get("Operation");
			String val = (String) map.get("isInSap");
			if (val.equals("Y")) {
				isInSap = true;
			}
		}
		String GroupType = "";
		if (map.get("GroupType") != null) {
			GroupType = (String) map.get("GroupType");
		}
		String MaxLTDeliveryDate = "";
		String MaxLTDeliveryDateDDMM = "";
		if (map.get("MaxLTDeliveryDate") != null) {
			java.util.Date dateStr = (Date) map.get("MaxLTDeliveryDate");
			MaxLTDeliveryDate = sdf2.format(dateStr);
			MaxLTDeliveryDateDDMM = sdfDDMM.format(dateStr);
		}
//		int LeadTimeToDelivery = 0;
//		if (map.get("LeadTimeToDelivery") != null) {
//			LeadTimeToDelivery = (int) map.get("LeadTimeToDelivery");
//		}
		String MaxLTCFMDate = "";
		if (map.get("MaxLTCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("MaxLTCFMDate");
			MaxLTCFMDate = sdf2.format(dateStr);
		}
//		int LeadTimeToCFM = 0;
//		if (map.get("LeadTimeToDelivery") != null) {
//			LeadTimeToCFM = (int) map.get("LeadTimeToCFM");
//		}

		String LTPOInputDate = "";
		if (map.get("LTPOInputDate") != null) {
			java.util.Date dateStr = (Date) map.get("LTPOInputDate");
			LTPOInputDate = sdf2.format(dateStr);
		}
		String LTMakeLotDate = "";
		if (map.get("LTMakeLotDate") != null) {
			java.util.Date dateStr = (Date) map.get("LTMakeLotDate");
			LTMakeLotDate = sdf2.format(dateStr);
		}
		String LTBCDate = "";
		if (map.get("LTBCDate") != null) {
			java.util.Date dateStr = (Date) map.get("LTBCDate");
			LTBCDate = sdf2.format(dateStr);
		}
		String LTPlanDate = "";
		if (map.get("LTPlanDate") != null) {
			java.util.Date dateStr = (Date) map.get("LTPlanDate");
			LTPlanDate = sdf2.format(dateStr);
		}
		String LTCFMDate = "";
		if (map.get("LTCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("LTCFMDate");
			LTCFMDate = sdf2.format(dateStr);
		}
		String LTCFMAnswerDate = "";
		if (map.get("LTCFMAnswerDate") != null) {
			java.util.Date dateStr = (Date) map.get("LTCFMAnswerDate");
			LTCFMAnswerDate = sdf2.format(dateStr);
		}
		String LTDeliveryDate = "";
		if (map.get("LTDeliveryDate") != null) {
			java.util.Date dateStr = (Date) map.get("LTDeliveryDate");
			LTDeliveryDate = sdf2.format(dateStr);
		}
		String LastCFMDate = "";
		if (map.get("LastCFMDate") != null) {
			java.util.Date dateStr = (Date) map.get("LastCFMDate");
			LastCFMDate = sdf2.format(dateStr);
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}

		String POUnit = "";
		if (map.get("POUnit") != null) {
			POUnit = (String) map.get("POUnit");
		}
		String POQty = "";
		if (map.get("POQty") != null) {
			BigDecimal value = (BigDecimal) map.get("POQty");
			double doubleVal = value.doubleValue();
			POQty = this.df2.format(doubleVal);
		}
		boolean isExpired = false;
		if (map.get("isExpired") != null) {
			isExpired = (boolean) map.get("isExpired");
		}
		boolean isPSSpecial = false;
		int isPSSpecialInt = 0;
		if (map.get("isPSSpecialInt") != null) {
			isPSSpecialInt = (int) map.get("isPSSpecialInt");
			if (isPSSpecialInt == 1) {
				isPSSpecial = true;
			}
		}
		String CustomerMat = "";
		if (map.get("CustomerMat") != null) {
			CustomerMat = (String) map.get("CustomerMat");
		}
		String Batch = "";
		if (map.get("Batch") != null) {
			Batch = (String) map.get("Batch");
		}
		int HaveFirstLot = 0;
		if (map.get("HaveFirstLot") != null) {
			HaveFirstLot = (int) map.get("HaveFirstLot");
		}
		int PlanInsteadId = 0;
		if (map.get("PlanInsteadId") != null) {
			PlanInsteadId = (int) map.get("PlanInsteadId");
		}
		int TempProdId = 0;
		if (map.get("TempProdId") != null) {
			TempProdId = (int) map.get("TempProdId");
		}
		String POIdInstead = "";
		if (map.get("POIdInstead") != null) {
			POIdInstead = (String) map.get("POIdInstead");
		}
		String ForecastDateMonthBefore = "";
		if (map.get("ForecastDateMonthBefore") != null) {
			java.util.Date dateStr = (Date) map.get("ForecastDateMonthBefore");
			ForecastDateMonthBefore = sdf2.format(dateStr);
		}

		double SumRemain = 0;
		if (map.get("SumRemain") != null) {
			BigDecimal value = (BigDecimal) map.get("SumRemain");
			SumRemain = value.doubleValue();
//			SumRemain = this.df2.format(dbVal);
		}
		double SumProdOrderQty = 0;
		if (map.get("SumProdOrderQty") != null) {
			BigDecimal value = (BigDecimal) map.get("SumProdOrderQty");
			SumProdOrderQty = value.doubleValue();
//			SumProdOrderQty = this.df2.format(dbVal);
		}
		double RemainBLQty = 0;
		if (map.get("RemainBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainBLQty");
			RemainBLQty = value.doubleValue();
//			SumProdOrderQty = this.df2.format(dbVal);
		}
		double RemainNonBLQty = 0;
		if (map.get("RemainNonBLQty") != null) {
			BigDecimal value = (BigDecimal) map.get("RemainNonBLQty");
			RemainNonBLQty = value.doubleValue();
//			SumProdOrderQty = this.df2.format(dbVal);
		}
		double SumProdOrderQtyBL = 0;
		if (map.get("SumProdOrderQtyBL") != null) {
			BigDecimal value = (BigDecimal) map.get("SumProdOrderQtyBL");
			SumProdOrderQtyBL = value.doubleValue();
//			SumProdOrderQty = this.df2.format(dbVal);
		}
		double SumProdOrderQtyNonBL = 0;
		if (map.get("SumProdOrderQtyNonBL") != null) {
			BigDecimal value = (BigDecimal) map.get("SumProdOrderQtyNonBL");
			SumProdOrderQtyNonBL = value.doubleValue();
//			SumProdOrderQty = this.df2.format(dbVal);
		}
		int Filter = 0;
		if (map.get("Filter") != null) {
			Filter = (int) map.get("Filter");
		}
		int POPuangId = 0;
		if (map.get("POPuangId") != null) {
			POPuangId = (int) map.get("POPuangId");
		}
		int TempPOAddId = 0;
		if (map.get("TempPOAddId") != null) {
			TempPOAddId = (int) map.get("TempPOAddId");
		}

		String ForecastDateMonthLast = "";
		if (map.get("ForecastDateMonthLast") != null) {
			java.util.Date dateStr = (Date) map.get("ForecastDateMonthLast");
			ForecastDateMonthLast = sdf2.format(dateStr);
		}
		String ForecastMY = "";
		if (map.get("ForecastMY") != null) {
			ForecastMY = (String) map.get("ForecastMY");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateStr = (Date) map.get("DueDate");
			DueDate = sdf2.format(dateStr);
		}
		String PlanGreigeDate = "";
		if (map.get("PlanGreigeDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanGreigeDate");
			PlanGreigeDate = sdf2.format(dateStr);
		}
		String GreigeInDate = "";
		if (map.get("GreigeInDate") != null) {
			java.util.Date dateStr = (Date) map.get("GreigeInDate");
			GreigeInDate = sdf2.format(dateStr);
		}

		String ProductionOrderType = "";
		if (map.get("ProductionOrderType") != null) {
			ProductionOrderType = (String) map.get("ProductionOrderType");
		}
		String QuantityKG = "";
		double db_quantityKG = 0;
		if (map.get("QuantityKG") != null) {
			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			db_quantityKG = value.doubleValue();
			QuantityKG = this.df2.format(db_quantityKG);
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String PlanningRemark = "";
		if (map.get("PlanningRemark") != null) {
			PlanningRemark = (String) map.get("PlanningRemark");
		}
		String StatusAfterDate = "";
		if (map.get("StatusAfterDate") != null) {
			java.util.Date dateStr = (Date) map.get("StatusAfterDate");
			StatusAfterDate = sdf2.format(dateStr);
		}
		String productionOrderFirstLot = "";
		if (map.get("ProductionOrderFirstLot") != null) {
			productionOrderFirstLot = (String) map.get("ProductionOrderFirstLot");
		}
		String dataStatus = "";
		if (map.get("DataStatus") != null) {
			dataStatus = (String) map.get("DataStatus");
		}
		String dyeSAPAfterFLDate = "";
		if (map.get("DyeSAPAfterFLDate") != null) {
			java.util.Date dateStr = (Date) map.get("DyeSAPAfterFLDate");
			dyeSAPAfterFLDate = sdf2.format(dateStr);
		}
		String DyeSAPAfterFLStatus = "";
		if (map.get("DyeSAPAfterFLStatus") != null) {
			DyeSAPAfterFLStatus = (String) map.get("DyeSAPAfterFLStatus");
		}
		String LogType = "";
		if (map.get("LogType") != null) {
			LogType = (String) map.get("LogType");
		}
		String Remark = "";
		if (map.get("Remark") != null) {
			Remark = (String) map.get("Remark");
		}
		String POType = "";
		if (map.get("POType") != null) {
			POType = (String) map.get("POType");
		}
		InputTempProdDetail bean = new InputTempProdDetail(No, POId, ForecastId, ProductionOrder, FirstLot, ProdOrderQty,
				PPMMStatus, ChangeBy, ChangeDate, ColorType, CustomerDue, PlanningDye, Design, LotType, ColorCustomer, Machine,
				Article, CustomerName, DBProdQty, DyeAfterCFM, PlanIndex, CreateBy, CreateDate, PlanSystemDate, PlanUserDate,
				OperationEndDate, GroupNo, SubGroup, GroupOptions, FirstLotDate, PlanBy, GreigePlan, DyeAfterGreigeInBegin,
				DyeAfterGreigeInLast, PlanUserChangeDate, FirstLotGroupNo, FirstLotSubGroup, isOverDue, TempPlanningId,
				GroupBegin, Operation, MaterialNo, PO, POLine, isInSap, RedyeId, isApproved, GroupType, MaxLTDeliveryDate,
				MaxLTCFMDate, LTPOInputDate, LTMakeLotDate, LTBCDate, LTPlanDate, LTCFMDate, LTCFMAnswerDate, LTDeliveryDate,
				LastCFMDate, UserStatus, LabStatus, SaleOrder, SaleLine, POQty, POUnit, isExpired, isPSSpecial, CustomerMat,
				Batch, ProductionOrderId, HaveFirstLot, PlanInsteadId, TempProdId, POIdInstead, ForecastDateMonthBefore,
				SumRemain, SumProdOrderQty, RemainBLQty, RemainNonBLQty, SumProdOrderQtyBL, SumProdOrderQtyNonBL, POPuangId,
				TempPOAddId, ForecastDateMonthLast, ForecastMY, DueDate, PlanGreigeDate, GreigeInDate, ProductionOrderType,
				QuantityKG, LotNo, PlanningRemark, Filter, StatusAfterDate, productionOrderFirstLot, dataStatus,
				dyeSAPAfterFLDate, DyeSAPAfterFLStatus, LogType);
		bean.setGroupOptionsList(GroupOptionsList);
		bean.setMaxLTDeliveryDateDDMM(MaxLTDeliveryDateDDMM);
		bean.setRemark(Remark);
		bean.setPoType(POType);
		return bean;
	}

	@Override
	public TotalGroupDyeDetail _genTotalGroupDyeDetail(Map<String, Object> map)
	{
		String PlanDate = "";
		if (map.get("PlanDate") != null) {
			java.util.Date dateStr = (Date) map.get("PlanDate");
			PlanDate = sdf2.format(dateStr);
		}
		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		String TotalLot = "";
		if (map.get("TotalLot") != null) {
			TotalLot = (String) map.get("TotalLot");
		}
		String IsHolilyDay = "";
		if (map.get("IsHolilyDay") != null) {
			IsHolilyDay = (String) map.get("IsHolilyDay");
		}
		return new TotalGroupDyeDetail(PlanDate, GroupNo, SubGroup, TotalLot, IsHolilyDay);
	}

	@Override
	public TotalOperationDetail _genTotalOperationDetailDetail(Map<String, Object> map)
	{
		return new TotalOperationDetail((Integer) map.get("Operation"), (Integer) map.get("countOperation"));
	}

	@Override
	public WorkDateDetail _genWorkDateDetail(Map<String, Object> map)
	{

		String GroupNo = "";
		if (map.get("GroupNo") != null) {
			GroupNo = (String) map.get("GroupNo");
		}
		String SubGroup = "";
		if (map.get("SubGroup") != null) {
			SubGroup = (String) map.get("SubGroup");
		}
		int DaysInMonth = 0;
		if (map.get("DaysInMonth") != null) {
			DaysInMonth = (int) map.get("DaysInMonth");
		}
		String ColMonthYear = "";
		if (map.get("ColMonthYear") != null) {
			ColMonthYear = (String) map.get("ColMonthYear");
		}
		String ColMonth = "";
		if (map.get("ColMonth") != null) {
			ColMonth = (String) map.get("ColMonth");
		}
		String ColYear = "";
		if (map.get("ColYear") != null) {
			ColYear = (String) map.get("ColYear");
		}
		String WorkDate = "";
		if (map.get("WorkDate") != null) {
			java.util.Date dateStr = (Date) map.get("WorkDate");
			WorkDate = sdf2.format(dateStr);
		}
		String NormalWork = "";
		if (map.get("NormalWork") != null) {
			NormalWork = (String) map.get("NormalWork");
		}
		int RowNum = 0;
		if (map.get("RowNum") != null) {
			long val = (long) map.get("RowNum");
			RowNum = (int) val;
		}

		String dataStatus = "";
		if (map.get("DataStatus") != null) {
			dataStatus = (String) map.get("DataStatus");
		}
		int id = 0;
		if (map.get("Id") != null) {
			id = (int) map.get("Id");
		}
		String LotPerDayInWork = "";
		if (map.get("LotPerDayInWork") != null) {
			int value = ((int) map.get("LotPerDayInWork"));
			LotPerDayInWork = Integer.toString(value);
		}

		String LotPerDay = "";
		if (map.get("LotPerDay") != null) {
			int value = ((int) map.get("LotPerDay"));
			LotPerDay = Integer.toString(value);
		}
		return new WorkDateDetail(GroupNo, SubGroup, DaysInMonth, ColMonthYear, ColMonth, ColYear, WorkDate, NormalWork, RowNum,
				id, dataStatus, LotPerDayInWork, LotPerDay);
	}

	@Override
	public DataFromSapDetail _genDataFromSapDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String DueDate = "";
		if (map.get("DueDate") != null) {
			java.util.Date dateStr = (Date) map.get("DueDate");
			DueDate = sdf2.format(dateStr);
		}
		String Operation = "";
		if (map.get("Operation") != null) {
			Operation = (String) map.get("Operation");
		}
		String WorkCenter = "";
		if (map.get("WorkCenter") != null) {
			WorkCenter = (String) map.get("WorkCenter");
		}
		String Durations = "";
		if (map.get("Durations") != null) {
			Durations = (String) map.get("Durations");
		}
		String Shade = "";
		if (map.get("Shade") != null) {
			Shade = (String) map.get("Shade");
		}
		String MaterialNumber = "";
		if (map.get("MaterialNumber") != null) {
			MaterialNumber = (String) map.get("MaterialNumber");
		}
		String LabStatus = "";
		if (map.get("LabStatus") != null) {
			LabStatus = (String) map.get("LabStatus");
		}
		String UserStatus = "";
		if (map.get("UserStatus") != null) {
			UserStatus = (String) map.get("UserStatus");
		}
		String QuantityKG = "";
		if (map.get("QuantityKG") != null) {
//			QuantityKG = (String) map.get("QuantityKG");
			BigDecimal value = (BigDecimal) map.get("QuantityKG");
			double dbValue = value.doubleValue();
			QuantityKG = this.df2.format(dbValue);
		}
		String QuantityMR = "";
		if (map.get("QuantityMR") != null) {
//			QuantityMR = (String) map.get("QuantityMR");
			BigDecimal value = (BigDecimal) map.get("QuantityMR");
			double dbValue = value.doubleValue();
			QuantityMR = this.df2.format(dbValue);
		}
		String CustomerName = "";
		if (map.get("CustomerName") != null) {
			CustomerName = (String) map.get("CustomerName");
		}
		String CustomerMat = "";
		if (map.get("CustomerMat") != null) {
			CustomerMat = (String) map.get("CustomerMat");
		}
		String MachineNo = "";
		if (map.get("MachineNo") != null) {
			MachineNo = (String) map.get("MachineNo");
		}
		String OperationStatus = "";
		if (map.get("OperationStatus") != null) {
			OperationStatus = (String) map.get("OperationStatus");
		}
		String AdminStatus = "";
		if (map.get("AdminStatus") != null) {
			AdminStatus = (String) map.get("AdminStatus");
		}
		String OperationStartDate = "";
		if (map.get("OperationStartDate") != null) {
			java.util.Date dateStr = (Date) map.get("OperationStartDate");
			OperationStartDate = sdf2.format(dateStr);
		}
		String OperationEndDate = "";
		if (map.get("OperationEndDate") != null) {
			java.util.Date dateStr = (Date) map.get("OperationEndDate");
			OperationEndDate = sdf2.format(dateStr);
		}
		String LotNo = "";
		if (map.get("LotNo") != null) {
			LotNo = (String) map.get("LotNo");
		}
		String CustomerDue = "";
		if (map.get("CustomerDue") != null) {
			java.util.Date dateStr = (Date) map.get("CustomerDue");
			CustomerDue = sdf2.format(dateStr);
		}
		String GreigeDue = "";
		if (map.get("GreigeDue") != null) {
			java.util.Date dateStr = (Date) map.get("GreigeDue");
			GreigeDue = sdf2.format(dateStr);
		}
		String Article = "";
		if (map.get("Article") != null) {
			Article = (String) map.get("Article");
		}
		String Batch = "";
		if (map.get("Batch") != null) {
			Batch = (String) map.get("Batch");
		}
		String FGDesign = "";
		if (map.get("FGDesign") != null) {
			FGDesign = (String) map.get("FGDesign");
		}
		String ColorCustomer = "";
		if (map.get("ColorCustomer") != null) {
			ColorCustomer = (String) map.get("ColorCustomer");
		}
		String Composition = "";
		if (map.get("Composition") != null) {
			Composition = (String) map.get("Composition");
		}
		String GreigeDesign = "";
		if (map.get("GreigeDesign") != null) {
			GreigeDesign = (String) map.get("GreigeDesign");
		}
		String LabNo = "";
		if (map.get("LabNo") != null) {
			LabNo = (String) map.get("LabNo");
		}
		String BookNo = "";
		if (map.get("BookNo") != null) {
			BookNo = (String) map.get("BookNo");
		}
		String Receipe = "";
		if (map.get("Receipe") != null) {
			Receipe = (String) map.get("Receipe");
		}
		String Awareness = "";
		if (map.get("Awareness") != null) {
			Awareness = (String) map.get("Awareness");
		}
		String Chemical = "";
		if (map.get("Chemical") != null) {
			Chemical = (String) map.get("Chemical");
		}
		String ChOne = "";
		if (map.get("ChOne") != null) {
			ChOne = (String) map.get("ChOne");
		}
		String ChTwo = "";
		if (map.get("ChTwo") != null) {
			ChTwo = (String) map.get("ChTwo");
		}
		String ChThree = "";
		if (map.get("ChThree") != null) {
			ChThree = (String) map.get("ChThree");
		}
		String ChFour = "";
		if (map.get("ChFour") != null) {
			ChFour = (String) map.get("ChFour");
		}
		String ChFive = "";
		if (map.get("ChFive") != null) {
			ChFive = (String) map.get("ChFive");
		}
		String ChSix = "";
		if (map.get("ChSix") != null) {
			ChSix = (String) map.get("ChSix");
		}
		String FanSpeedTopSTD = "";
		if (map.get("FanSpeedTopSTD") != null) {
			FanSpeedTopSTD = (String) map.get("FanSpeedTopSTD");
		}
		String FanSpeedBottomSTD = "";
		if (map.get("FanSpeedBottomSTD") != null) {
			FanSpeedBottomSTD = (String) map.get("FanSpeedBottomSTD");
		}
		String PressurePadderSTD = "";
		if (map.get("PressurePadderSTD") != null) {
			PressurePadderSTD = (String) map.get("PressurePadderSTD");
		}
		String PressureDancingSTD = "";
		if (map.get("PressureDancingSTD") != null) {
			PressureDancingSTD = (String) map.get("PressureDancingSTD");
		}
		String OverFeedTopSTD = "";
		if (map.get("OverFeedTopSTD") != null) {
			OverFeedTopSTD = (String) map.get("OverFeedTopSTD");
		}
		String OverFeedBottomSTD = "";
		if (map.get("OverFeedBottomSTD") != null) {
			OverFeedBottomSTD = (String) map.get("OverFeedBottomSTD");
		}
		String PinSpeedSTD = "";
		if (map.get("PinSpeedSTD") != null) {
			PinSpeedSTD = (String) map.get("PinSpeedSTD");
		}
		String CartType = "";
		if (map.get("CartType") != null) {
			CartType = (String) map.get("CartType");
		}
		String CourseSTD = "";
		if (map.get("CourseSTD") != null) {
			CourseSTD = (String) map.get("CourseSTD");
		}
		String WaleSTD = "";
		if (map.get("WaleSTD") != null) {
			WaleSTD = (String) map.get("WaleSTD");
		}
		String WidthSTD = "";
		if (map.get("WidthSTD") != null) {
			WidthSTD = (String) map.get("WidthSTD");
		}
		String WeightSTD = "";
		if (map.get("WeightSTD") != null) {
			WeightSTD = (String) map.get("WeightSTD");
		}
		String WidthMCSTD = "";
		if (map.get("WidthMCSTD") != null) {
			WidthMCSTD = (String) map.get("WidthMCSTD");
		}
		String SpeedMCSTD = "";
		if (map.get("SpeedMCSTD") != null) {
			SpeedMCSTD = (String) map.get("SpeedMCSTD");
		}
		String Glue = "";
		if (map.get("Glue") != null) {
			Glue = (String) map.get("Glue");
		}
		String EdgeCutting = "";
		if (map.get("EdgeCutting") != null) {
			EdgeCutting = (String) map.get("EdgeCutting");
		}
		String CenterCut = "";
		if (map.get("CenterCut") != null) {
			CenterCut = (String) map.get("CenterCut");
		}
		String SaleOrder = "";
		if (map.get("SaleOrder") != null) {
			SaleOrder = (String) map.get("SaleOrder");
		}
		String SaleLine = "";
		if (map.get("SaleLine") != null) {
			SaleLine = (String) map.get("SaleLine");
		}
		String RollNo = "";
		if (map.get("RollNo") != null) {
			RollNo = (String) map.get("RollNo");
		}
		String ColorStatus = "";
		if (map.get("ColorStatus") != null) {
			ColorStatus = (String) map.get("ColorStatus");
		}
		String ColorName = "";
		if (map.get("ColorName") != null) {
			ColorName = (String) map.get("ColorName");
		}
		String ColorRemark = "";
		if (map.get("ColorRemark") != null) {
			ColorRemark = (String) map.get("ColorRemark");
		}
		String DeltaE = "";
		if (map.get("DeltaE") != null) {
			DeltaE = (String) map.get("DeltaE");
		}
		String CFMNo = "";
		if (map.get("CFMNo") != null) {
			CFMNo = (String) map.get("CFMNo");
		}
		String CFMSendDate = "";
		if (map.get("CFMSendDate") != null) {
			java.util.Date dateStr = (Date) map.get("CFMSendDate");
			CFMSendDate = sdf2.format(dateStr);
		}
		String CFMNumber = "";
		if (map.get("CFMNumber") != null) {
			CFMNumber = (String) map.get("CFMNumber");
		}
		String CFMAnswerDate = "";
		if (map.get("CFMAnswerDate") != null) {
			java.util.Date dateStr = (Date) map.get("CFMAnswerDate");
			CFMAnswerDate = sdf2.format(dateStr);
		}
		String CFMStatus = "";
		if (map.get("CFMStatus") != null) {
			CFMStatus = (String) map.get("CFMStatus");
		}
		String CFMRemark = "";
		if (map.get("CFMRemark") != null) {
			CFMRemark = (String) map.get("CFMRemark");
		}
		String OperationStartTime = "";
		if (map.get("OperationStartTime") != null) {
			OperationStartTime = (String) map.get("OperationStartTime");
		}
		String OperationEndTime = "";
		if (map.get("OperationEndTime") != null) {
			OperationEndTime = (String) map.get("OperationEndTime");
		}
		String UserStatusDate = "";
		if (map.get("UserStatusDate") != null) {
			java.util.Date dateStr = (Date) map.get("UserStatusDate");
			UserStatusDate = sdf2.format(dateStr);
		}
		String LabStatusDate = "";
		if (map.get("LabStatusDate") != null) {
			java.util.Date dateStr = (Date) map.get("LabStatusDate");
			LabStatusDate = sdf2.format(dateStr);
		}

		return new DataFromSapDetail(Id, ProductionOrder, DueDate, Operation, WorkCenter, Durations, Shade, MaterialNumber,
				LabStatus, UserStatus, QuantityKG, QuantityMR, CustomerName, CustomerMat, MachineNo, OperationStatus, AdminStatus,
				OperationStartDate, OperationEndDate, LotNo, CustomerDue, GreigeDue, Article, Batch, FGDesign, ColorCustomer,
				Composition, GreigeDesign, LabNo, BookNo, Receipe, Awareness, Chemical, ChOne, ChTwo, ChThree, ChFour, ChFive,
				ChSix, FanSpeedTopSTD, FanSpeedBottomSTD, PressurePadderSTD, PressureDancingSTD, OverFeedTopSTD,
				OverFeedBottomSTD, PinSpeedSTD, CartType, CourseSTD, WaleSTD, WidthSTD, WeightSTD, WidthMCSTD, SpeedMCSTD, Glue,
				EdgeCutting, CenterCut, SaleOrder, SaleLine, RollNo, ColorStatus, ColorName, ColorRemark, DeltaE, CFMNo,
				CFMSendDate, CFMNumber, CFMAnswerDate, CFMStatus, CFMRemark, OperationStartTime, OperationEndTime, UserStatusDate,
				LabStatusDate, DueDate, DueDate);
	}

	@Override
	public RollFromSapDetail _genRollFromSapDetail(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
		int Id = 0;
		if (map.get("Id") != null) {
			Id = (int) map.get("Id");
		}
		String ProductionOrder = "";
		if (map.get("ProductionOrder") != null) {
			ProductionOrder = (String) map.get("ProductionOrder");
		}
		String RollLine = "";
		if (map.get("RollLine") != null) {
			RollLine = (String) map.get("RollLine");
		}
		String RollNumber = "";
		if (map.get("RollNumber") != null) {
			BigDecimal value = (BigDecimal) map.get("RollNumber");
			double doubleVal = value.doubleValue();
			RollNumber = this.df2.format(doubleVal);
		}
		String RollWeight = "";
		if (map.get("RollWeight") != null) {
			BigDecimal value = (BigDecimal) map.get("RollWeight");
			double doubleVal = value.doubleValue();
			RollWeight = this.df2.format(doubleVal);
		}
		String RollLength = "";
		if (map.get("RollLength") != null) {
			BigDecimal value = (BigDecimal) map.get("RollLength");
			double doubleVal = value.doubleValue();
			RollLength = this.df2.format(doubleVal);
		}

		String CreateDate = "";
		if (map.get("CreateDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("CreateDate");
			CreateDate = this.sdf3.format(timestamp1);
		}
		String ChangeDate = "";
		if (map.get("ChangeDate") != null) {
			Timestamp timestamp1 = (Timestamp) map.get("ChangeDate");
			ChangeDate = this.sdf3.format(timestamp1);
		}
		return new RollFromSapDetail(Id, ProductionOrder, RollLine, RollNumber, RollWeight, RollLength, ChangeDate, CreateDate);
	}

	@Override
	public EmployeeDetail _genEmployeeDetail(Map<String, Object> map)
	{
		String EmployeeID = ""; // add
		if (map.get("EmployeeID") != null) {
			EmployeeID = (String) map.get("EmployeeID");
		}
		String PermitId = ""; // add
		if (map.get("PermitId") != null) {
			PermitId = (String) map.get("PermitId");
		}
		String FirstName = ""; // add
		if (map.get("FirstName") != null) {
			FirstName = (String) map.get("FirstName");
		}
		String LastName = ""; // add
		if (map.get("LastName") != null) {
			LastName = (String) map.get("LastName");
		}
		String Role = ""; // add
		if (map.get("Role") != null) {
			Role = (String) map.get("Role");
		}
		EmployeeDetail bean = new EmployeeDetail();
		bean.setEmployeeID(EmployeeID);
		bean.setFirstName(FirstName);
		bean.setLastName(LastName);
		bean.setRole(Role);
		bean.setPermitId(PermitId);
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
		boolean IsCreateTempLot = false;
		if (map.get("IsCreateTempLot") != null) {
			IsCreateTempLot = (boolean) map.get("IsCreateTempLot");
		}
		boolean IsPlanning = false;
		if (map.get("IsPlanning") != null) {
			IsPlanning = (boolean) map.get("IsPlanning");
		}
		boolean IsApprove = false;
		if (map.get("IsApprove") != null) {
			IsApprove = (boolean) map.get("IsApprove");
		}
		boolean IsArticle = false;
		if (map.get("IsArticle") != null) {
			IsArticle = (boolean) map.get("IsArticle");
		}
		boolean IsArticleSubGroup = false;
		if (map.get("IsArticleSubGroup") != null) {
			IsArticleSubGroup = (boolean) map.get("IsArticleSubGroup");
		}
		boolean IsBangkokDate = false;
		if (map.get("IsBangkokDate") != null) {
			IsBangkokDate = (boolean) map.get("IsBangkokDate");
		}
		boolean IsFactoryDate = false;
		if (map.get("IsFactoryDate") != null) {
			IsFactoryDate = (boolean) map.get("IsFactoryDate");
		}
		boolean IsLeadTime = false;
		if (map.get("IsLeadTime") != null) {
			IsLeadTime = (boolean) map.get("IsLeadTime");
		}
		boolean IsLeadTimeStatus = false;
		if (map.get("IsLeadTimeStatus") != null) {
			IsLeadTimeStatus = (boolean) map.get("IsLeadTimeStatus");
		}
		boolean IsMachine = false;
		if (map.get("IsMachine") != null) {
			IsMachine = (boolean) map.get("IsMachine");
		}
		boolean IsMachineGroup = false;
		if (map.get("IsMachineGroup") != null) {
			IsMachineGroup = (boolean) map.get("IsMachineGroup");
		}
		boolean IsStockReceive = false;
		if (map.get("IsStockReceive") != null) {
			IsStockReceive = (boolean) map.get("IsStockReceive");
		}
		boolean IsStockDate = false;
		if (map.get("IsStockDate") != null) {
			IsStockDate = (boolean) map.get("IsStockDate");
		}
		boolean IsProdRunning = false;
		if (map.get("IsProdRunning") != null) {
			IsProdRunning = (boolean) map.get("IsProdRunning");
		}
		boolean IsPOManagement = false;
		if (map.get("IsPOManagement") != null) {
			IsPOManagement = (boolean) map.get("IsPOManagement");
		}
		boolean IsSendReCFM = false;
		if (map.get("IsSendReCFM") != null) {
			IsSendReCFM = (boolean) map.get("IsSendReCFM");
		}

		return new PermitDetail(Id, PermitId, Description, IsCreateTempLot, IsPlanning, IsApprove, IsArticle, IsArticleSubGroup,
				IsBangkokDate, IsFactoryDate, IsLeadTime, IsLeadTimeStatus, IsMachine, IsMachineGroup, IsStockReceive,
				IsStockDate, IsProdRunning, IsPOManagement, IsSendReCFM);
	}
}
