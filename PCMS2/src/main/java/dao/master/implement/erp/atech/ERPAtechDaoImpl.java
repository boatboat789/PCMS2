package dao.master.implement.erp.atech;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import dao.master.erp.atech.ERPAtechDao;
import entities.erp.atech.CustomerDetail;
import entities.erp.atech.FromErpCFMDetail;
import entities.erp.atech.FromErpGoodReceiveDetail;
import entities.erp.atech.FromErpMainBillBatchDetail;
import entities.erp.atech.FromErpMainProdDetail;
import entities.erp.atech.FromErpMainProdSaleDetail;
import entities.erp.atech.FromErpMainSaleDetail;
import entities.erp.atech.FromErpPODetail;
import entities.erp.atech.FromErpPackingDetail;
import entities.erp.atech.FromErpReceipeDetail;
import entities.erp.atech.FromErpSaleDetail;
import entities.erp.atech.FromErpSaleInputDetail;
import entities.erp.atech.FromErpSubmitDateDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

@Component
public class ERPAtechDaoImpl implements ERPAtechDao  { 
	private BeanCreateModel bcModel = new BeanCreateModel();
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm"); 
	private Database database;
	public ERPAtechDaoImpl(Database database) {
		this.message = "";
		this.database = database;
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<CustomerDetail> getCustomerDetail()
	{
		ArrayList<CustomerDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " CustomerNo,\r\n"
				+ "CustomerName,\r\n"
				+ "CustomerShortName,\r\n"
				+ "CustomerType,\r\n"
				+ "DistChannel \r\n" 
				+ "      ,[SyncDate]"
				+ " from CustomerDetail"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCustomerDetail(map));
		}
		return list;
	}

	 
	@Override
	public ArrayList<FromErpCFMDetail> getFromErpCFMDetail()
	{
		ArrayList<FromErpCFMDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ "ID,\r\n"
				+ "ProductionOrder,\r\n"
				+ "CFMNo,\r\n"
				+ "CFMNumber,\r\n"
				+ "CFMSendDate,\r\n"
				+ "CFMAnswerDate,\r\n"
				+ "CFMStatus,\r\n"
				+ "CFMRemark,\r\n"
				+ "SaleOrder,\r\n"
				+ "SaleLine,\r\n"
				+ "RollNoRemark \r\n" 
				+ "      ,[SyncDate]"
				+ " from FromErpCFM"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpCFMDetail(map));
		}
		return list;
	}

	 
//	@Override
//	public ArrayList<FromErpDyeingDetail> getFromErpDyeingDetail()
//	{
//		ArrayList<FromErpDyeingDetail> list = new ArrayList<>(); 
//		String sql =
//				" "
//				+ " SELECT distinct   \r\n"
//				+ this.select 
//				+ " from FromErpDyeing"
//				
//				; 
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genFromErpDyeingDetail(map));
//		}
//		return list;
//	}

	 
//	@Override
//	public ArrayList<FromErpFinishingDetail> getFromErpFinishingDetail()
//	{
//		ArrayList<FromErpFinishingDetail> list = new ArrayList<>(); 
//		String sql =
//				" "
//				+ " SELECT distinct   \r\n"
//				+ this.select 
//				+ " from FromErpFinishing"
//				
//				; 
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genFromErpFinishingDetail(map));
//		}
//		return list;
//	}

	 
	@Override
	public ArrayList<FromErpGoodReceiveDetail> getFromErpGoodReceiveDetail()
	{
		ArrayList<FromErpGoodReceiveDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " ProductionOrder,\r\n"
				+ "SaleOrder,\r\n"
				+ "SaleLine,\r\n"
				+ "Grade,\r\n"
				+ "RollNumber,\r\n"
				+ "QuantityKG,\r\n"
				+ "QuantityYD,\r\n"
				+ "QuantityMR,\r\n"
				+ "PriceSTD \r\n"
				+ "      ,[SyncDate]"
				+ " "
				+ " from FromErpGoodReceive"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpGoodReceiveDetail(map));
		}
		return list;
	}

	 
//	@Override
//	public ArrayList<FromErpInspectDetail> getFromErpInspectDetail()
//	{
//		ArrayList<FromErpInspectDetail> list = new ArrayList<>(); 
//		String sql =
//				" "
//				+ " SELECT distinct   \r\n"
//				+ this.select 
//				+ " from FromErpInspect"
//				
//				; 
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genFromErpInspectDetail(map));
//		}
//		return list;
//	}

	 
	@Override
	public ArrayList<FromErpMainBillBatchDetail> getFromErpMainBillBatchDetail()
	{
		ArrayList<FromErpMainBillBatchDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " BillDoc,\r\n"
				+ "BillItem,\r\n"
				+ "LotShipping,\r\n"
				+ "ProductionOrder,\r\n"
				+ "SaleOrder,\r\n"
				+ "SaleLine,\r\n"
				+ "Grade,\r\n"
				+ "RollNumber,\r\n"
				+ "QuantityKG,\r\n"
				+ "QuantityYD,\r\n"
				+ "QuantityMR,\r\n"
				+ "LotNo\r\n"
				+ "      ,[SyncDate]"
				+ "  " 
				+ " from FromErpMainBillBatch"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpMainBillBatchDetail(map));
		}
		return list;
	}

	 
	@Override
	public ArrayList<FromErpMainProdDetail> getFromErpMainProdDetail()
	{
		ArrayList<FromErpMainProdDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " ProductionOrder,\r\n"
				+ "SaleOrder,\r\n"
				+ "SaleLine,\r\n"
				+ "TotalQuantity,\r\n"
				+ "Unit,\r\n"
				+ "RemAfterCloseOne,\r\n"
				+ "RemAfterCloseTwo,\r\n"
				+ "RemAfterCloseThree,\r\n"
				+ "LabStatus,\r\n"
				+ "UserStatus,\r\n"
				+ "DesignFG,\r\n"
				+ "ArticleFG,\r\n"
				+ "BookNo,\r\n"
				+ "Center,\r\n"
				+ "LotNo,\r\n"
				+ "Batch,\r\n"
				+ "LabNo,\r\n"
				+ "RemarkOne,\r\n"
				+ "RemarkTwo,\r\n"
				+ "RemarkThree,\r\n"
				+ "BCAware,\r\n"
				+ "OrdErpuang,\r\n"
				+ "RefPrd,\r\n"
				+ "GreigeInDate,\r\n"
				+ "BCDate,\r\n"
				+ "Volumn,\r\n"
				+ "CFdate,\r\n"
				+ "CFType,\r\n"
				+ "Shade,\r\n"
				+ "LotShipping,\r\n"
				+ "BillSendQuantity,\r\n"
				+ "Grade,\r\n"
				+ "DataStatus,\r\n"
				+ "PrdCreateDate,\r\n"
				+ "GreigeArticle,\r\n"
				+ "GreigeDesign,\r\n"
				+ "GreigeMR,\r\n"
				+ "GreigeKG,\r\n"
				+ "OrderType \r\n"
				+ "      ,[SyncDate]"
				+ " from FromErpMainProd"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpMainProdDetail(map));
		}
		return list;
	}

	 
	@Override
	public ArrayList<FromErpMainProdSaleDetail> getFromErpMainProdSaleDetail()
	{
		ArrayList<FromErpMainProdSaleDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " ProductionOrder,\r\n"
				+ "SaleOrder,\r\n"
				+ "SaleLine,\r\n"
				+ "Volumn\r\n"
				+ "      ,[SyncDate]"
				+ " " 
				+ " from FromErpMainProdSale"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpMainProdSaleDetail(map));
		}
		return list;
	}

	 
	@Override
	public ArrayList<FromErpMainSaleDetail> getFromErpMainSaleDetail()
	{
		ArrayList<FromErpMainSaleDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " SaleOrder,\r\n"
				+ "SaleLine,\r\n"
				+ "MaterialNo,\r\n"
				+ "DueDate,\r\n"
				+ "PlanGreigeDate,\r\n"
				+ "SaleUnit,\r\n"
				+ "SaleQuantity,\r\n"
				+ "CustomerMaterial,\r\n"
				+ "Color,\r\n"
				+ "CustomerNo,\r\n"
				+ "PurchaseOrder,\r\n"
				+ "SaleOrg,\r\n"
				+ "DistChannel,\r\n"
				+ "Division,\r\n"
				+ "CustomerName,\r\n"
				+ "CustomerShortName,\r\n"
				+ "ColorCustomer,\r\n"
				+ "CustomerDue,\r\n"
				+ "RemainQuantity,\r\n"
				+ "ShipDate,\r\n"
				+ "SaleStatus,\r\n"
				+ "Currency,\r\n"
				+ "Price,\r\n"
				+ "OrderAmount,\r\n"
				+ "RemainAmount,\r\n"
				+ "SaleCreateDate,\r\n"
				+ "SaleNumber,\r\n"
				+ "SaleFullName,\r\n"
				+ "DeliveryStatus,\r\n"
				+ "DesignFG,\r\n"
				+ "ArticleFG,\r\n"
				+ "OrderSheetPrintDate,\r\n"
				+ "CustomerMaterialBase \r\n"
				+ "      ,[SyncDate]"
				+ " " 
				+ " from FromErpMainSale"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpMainSaleDetail(map));
		}
		return list;
	}

	 
	@Override
	public ArrayList<FromErpPackingDetail> getFromErpPackingDetail()
	{
		ArrayList<FromErpPackingDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " ProductionOrder,\r\n"
				+ "PostingDate,\r\n"
				+ "Quantity,\r\n"
				+ "RollNo,\r\n"
				+ "QuantityKG,\r\n"
				+ "Grade,\r\n"
				+ "No,\r\n"
				+ "QuantityYD\r\n"
				+ "      ,[SyncDate]"
				+ " " 
				+ " from FromErpPacking"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpPackingDetail(map));
		}
		return list;
	}

	 
	@Override
	public ArrayList<FromErpPODetail> getFromErpPODetail()
	{
		ArrayList<FromErpPODetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " ProductionOrder,\r\n"
				+ "RollNo,\r\n"
				+ "QuantityKG,\r\n"
				+ "QuantityMR,\r\n"
				+ "POCreatedate,\r\n"
				+ "RequiredDate,\r\n"
				+ "PurchaseOrder,\r\n"
				+ "PurchaseOrderLine,\r\n"
				+ "PODefault,\r\n"
				+ "POLineDefault,\r\n"
				+ "POPostingDateDefault\r\n"
				+ "      ,[SyncDate]"
				+ " " 
				+ " from FromErpPO"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpPODetail(map));
		}
		return list;
	}

	 
//	@Override
//	public ArrayList<FromErpPresetDetail> getFromErpPresetDetail()
//	{
//		ArrayList<FromErpPresetDetail> list = new ArrayList<>(); 
//		String sql =
//				" "
//				+ " SELECT distinct   \r\n"
//				+ this.select 
//				+ " from FromErpPreset"
//				
//				; 
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genFromErpPresetDetail(map));
//		}
//		return list;
//	}

	 
	@Override
	public ArrayList<FromErpReceipeDetail> getFromErpReceipeDetail()
	{
		ArrayList<FromErpReceipeDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " [ProductionOrder]\r\n"
				+ "      ,[LotNo]"
				+ "      ,[SyncDate]"
				+ " from FromErpReceipe"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpReceipeDetail(map));
		}
		return list;
	}



	 
	@Override
	public ArrayList<FromErpSaleDetail> getFromErpSaleDetail()
	{
		ArrayList<FromErpSaleDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " ProductionOrder,\r\n"
				+ "BillDate,\r\n"
				+ "BillQtyPerSale,\r\n"
				+ "SaleOrder,\r\n"
				+ "SaleLine,\r\n"
				+ "BillQtyPerStock,\r\n"
				+ "Remark,\r\n"
				+ "CustomerNo,\r\n"
				+ "CustomerName1,\r\n"
				+ "CustomErpO,\r\n"
				+ "DueDate,\r\n"
				+ "Color,\r\n"
				+ "No \r\n"
				+ "      ,[SyncDate]"
				+ "" 
				+ " from FromErpSale"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpSaleDetail(map));
		}
		return list;
	}



	 
	@Override
	public ArrayList<FromErpSaleInputDetail> getFromErpSaleInputDetail()
	{
		ArrayList<FromErpSaleInputDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+  " ProductionOrder,\r\n"
				+ "BillDate,\r\n"
				+ "BillQtyPerSale,\r\n"
				+ "SaleOrder,\r\n"
				+ "SaleLine,\r\n"
				+ "BillQtyPerStock,\r\n"
				+ "Remark,\r\n"
				+ "CustomerNo,\r\n"
				+ "CustomerName1,\r\n"
				+ "CustomErpO,\r\n"
				+ "DueDate,\r\n"
				+ "Color,\r\n"
				+ "No\r\n"
				+ "      ,[SyncDate]"
				+ " " 
				+ " from FromErpSaleInput"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpSaleInputDetail(map));
		}
		return list;
	}



	 
//	@Override
//	public ArrayList<FromErpSendTestQCDetail> getFromErpSendTestQCDetail()
//	{
//		ArrayList<FromErpSendTestQCDetail> list = new ArrayList<>(); 
//		String sql =
//				" "
//				+ " SELECT distinct   \r\n"
//				+ this.select 
//				+ " from FromErpSendTestQC"
//				
//				; 
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genFromErpSendTestQCDetail(map));
//		}
//		return list;
//	}



	 
	@Override
	public ArrayList<FromErpSubmitDateDetail> getFromErpSubmitDateDetail()
	{
		ArrayList<FromErpSubmitDateDetail> list = new ArrayList<>(); 
		String sql =
				" "
				+ " SELECT distinct   \r\n"
				+ " ProductionOrder,\r\n"
				+ " SaleOrder,\r\n"
				+ " SaleLine,\r\n"
				+ " No,\r\n"
				+ " SubmitDate,\r\n"
				+ " Remark\r\n"
				+ "      ,[SyncDate]"
				+ " " 
				+ " from FromErpSubmitDate"
				
				; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpSubmitDateDetail(map));
		}
		return list;
	}



	 
	//	@Override
	//	public ArrayList<FromErpWaitTestDetail> getFromErpWaitTestDetail()
	//	{
	//		ArrayList<FromErpWaitTestDetail> list = new ArrayList<>(); 
	//		String sql =
	//				" "
	//				+ " SELECT distinct   \r\n"
	//				+ this.select 
	//				+ " from FromErpWaitTest"
	//				
	//				; 
	//		List<Map<String, Object>> datas = this.database.queryList(sql);
	//		list = new ArrayList<>();
	//		for (Map<String, Object> map : datas) {
	//			list.add(this.bcModel._genFromErpWaitTestDetail(map));
	//		}
	//		return list;
	//	}



	 
//	@Override
//	public ArrayList<FromErpWorkInLabDetail> getFromErpWorkInLabDetail()
//	{
//		ArrayList<FromErpWorkInLabDetail> list = new ArrayList<>(); 
//		String sql =
//				" "
//				+ " SELECT distinct   \r\n"
//				+ this.select 
//				+ " from FromErpWorkInLab"
//				
//				; 
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genFromErpWorkInLabDetail(map));
//		}
//		return list;
//	}

 
}
