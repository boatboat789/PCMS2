package th.co.wacoal.atech.pcms2.dao.master.implement.erp.atech;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.erp.atech.ERPAtechDao;
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
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
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
				+ " CASE \r\n"
				+ "		WHEN LEN([CustomerNo]) > 10 THEN [CustomerNo]\r\n"
				+ "		WHEN TRY_CAST([CustomerNo] AS INT ) IS NULL  THEN [CustomerNo]\r\n"
				+ "		ELSE RIGHT('0000000000'+ISNULL([CustomerNo],''),10) \r\n"
				+ "	END as [CustomerNo] ,"
				+ " TRY_CAST( CustomerNo AS NVARCHAR(50)) as CustomerNoWOZero  ,\r\n"
				+ " TRY_CAST( CustomerName AS NVARCHAR(500)) as CustomerName  ,\r\n"
				+ " TRY_CAST( CustomerShortName AS NVARCHAR(500)) as CustomerShortName  ,\r\n"
				+ " TRY_CAST( CustomerType AS NVARCHAR(50)) as CustomerType  ,\r\n"
				+ " TRY_CAST( DistChannel AS NVARCHAR(50)) as DistChannel, \r\n"
				+ "	 case\r\n"
				+ "		 when [CustomerName] not like '%SABINA%' and [CustomerShortName] not like '%SBF%'\r\n"
				+ "		 then CAST ( 0 AS BIT ) \r\n"
				+ "		 else CAST ( 1 AS BIT ) "
				+ "	end as IsSabina ," 
				+ " [SyncDate] "
				+ " from CustomerDetail "
				+ " where TRY_CAST( CustomerNo AS int) is not null"
				
				; 
//		System.out.println(sql);
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
				+ " TRY_CAST(ProductionOrder AS NVARCHAR(50)) as ProductionOrder ,\r\n"
				+ " TRY_CAST(CFMNo AS NVARCHAR(2)) as CFMNo ,\r\n"
				+ " TRY_CAST(CFMNumber AS NVARCHAR(20)) as CFMNumber ,\r\n"
				+ "  CASE \r\n"
				+ "        WHEN CFMSendDate = '1900-01-01 00:00:00.000' THEN null \r\n" 
				+ "        WHEN CFMSendDate is null or CFMSendDate = '' THEN null \r\n"
				+ "        ELSE CFMSendDate \r\n"
				+ "    END AS CFMSendDate, \r\n"   
				+ "  CASE \r\n"
				+ "        WHEN CFMAnswerDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN CFMAnswerDate is null or CFMAnswerDate = '' THEN null \r\n"
				+ "        ELSE CFMAnswerDate \r\n"
				+ "    END AS CFMAnswerDate, \r\n"  
				+ " TRY_CAST( CFMStatus AS NVARCHAR(30)) as CFMStatus ,\r\n" 
				+ " TRY_CAST( CFMRemark AS NVARCHAR(80)) as CFMRemark ,\r\n" 
				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder ,\r\n"
				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine ,\r\n"
				+ " TRY_CAST(RollNoRemark AS NVARCHAR(200)) as RollNoRemark,  \r\n" 
				+ " [SyncDate]"
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
				+ " TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrder,\r\n"
				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder,\r\n"
				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine,\r\n"
				+ " TRY_CAST(Grade AS NVARCHAR(20)) as Grade,\r\n"
				+ " TRY_CAST(RollNumber AS NVARCHAR(20)) as RollNumber,\r\n"
				+ " TRY_CAST(QuantityKG AS decimal(13, 3)) as QuantityKG,\r\n"
				+ " TRY_CAST(QuantityYD AS decimal(13, 3)) as QuantityYD,\r\n"
				+ " TRY_CAST(QuantityMR AS decimal(13, 3)) as QuantityMR,\r\n"
				+ " TRY_CAST(PriceSTD AS decimal(13, 3)) as PriceSTD, \r\n"
				+ " [SyncDate]"
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
				+ " TRY_CAST(BillDoc AS NVARCHAR(20)) as BillDoc,\r\n"
				+ " TRY_CAST(BillItem AS NVARCHAR(20)) as BillItem,\r\n"
//				+ " TRY_CAST(LotShipping AS NVARCHAR(50)) as LotShipping,\r\n"
				+ "  CASE \r\n"
				+ "        WHEN LotShipping = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN LotShipping is null or LotShipping = '' THEN null \r\n"
				+ "        ELSE LotShipping  \r\n"
				+ "    END AS LotShipping, \r\n"  
				+ " TRY_CAST(ProductionOrder AS NVARCHAR(50)) as ProductionOrder,\r\n"
				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder,\r\n"
				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine,\r\n"
				+ " TRY_CAST(Grade AS NVARCHAR(20)) as Grade,\r\n"
				+ " TRY_CAST(RollNumber AS NVARCHAR(20)) as RollNumber,\r\n"
				+ " TRY_CAST(QuantityKG AS decimal(13, 3)) as QuantityKG,\r\n"
				+ " TRY_CAST(QuantityYD AS decimal(13, 3)) as QuantityYD,\r\n"
				+ " TRY_CAST(QuantityMR AS decimal(13, 3)) as QuantityMR,\r\n" 	
				+ " [SyncDate] " 
				+ " from FromErpMainBillBatch"
				+ " where BillDoc is not null and BillDoc <> ''"
				
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
				+ " TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrder,\r\n"
				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder,\r\n"
				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine,\r\n"
				+ " TRY_CAST(TotalQuantity AS decimal(13, 3)) as TotalQuantity,\r\n"
				+ " CASE \r\n"
				+ "	  WHEN TRY_CAST( Unit AS NVARCHAR(20)) = 'M' THEN 'MR'\r\n"
				+ "	  ELSE TRY_CAST( Unit AS NVARCHAR(20))\r\n"
				+ " END as Unit," 
//				+ " TRY_CAST(Unit AS NVARCHAR(20)) as Unit,\r\n"
//				+ " TRY_CAST([RemAfterCloseOne] AS NVARCHAR(200)) as [RemAfterCloseOne],\r\n"
//				+ " TRY_CAST([RemAfterCloseTwo] AS NVARCHAR(200)) as [RemAfterCloseTwo],\r\n"
//				+ " TRY_CAST([RemAfterCloseThree] AS NVARCHAR(200)) as [RemAfterCloseThree],\r\n"
				+ "     CASE \r\n"
				+ "        WHEN RemAfterCloseOne IS NULL OR LEN(RemAfterCloseOne) < 1 THEN NULL\r\n"
				+ "        ELSE SUBSTRING(RemAfterCloseOne, 1, 100) \r\n"
				+ "    END AS RemAfterCloseOne,\r\n" 
				+ "    CASE \r\n" 
				+ "        WHEN RemAfterCloseOne IS NULL OR LEN(RemAfterCloseOne) < 101 THEN NULL\r\n"
				+ "        ELSE SUBSTRING(RemAfterCloseOne, 101, 100) \r\n"
				+ "    END AS RemAfterCloseTwo,\r\n" 
				+ "    CASE \r\n"
				+ "        WHEN RemAfterCloseOne IS NULL OR LEN(RemAfterCloseOne) < 201 THEN NULL\r\n"
				+ "        ELSE SUBSTRING(RemAfterCloseOne, 201, 100) \r\n"
				+ "    END AS RemAfterCloseThree,"
				+ " TRY_CAST(LabStatus AS NVARCHAR(50)) as LabStatus,\r\n"
				+ " TRY_CAST(UserStatus AS NVARCHAR(50)) as UserStatus,\r\n"
				+ " TRY_CAST(DesignFG AS NVARCHAR(50)) as DesignFG,\r\n"
				+ " TRY_CAST(ArticleFG AS NVARCHAR(50)) as ArticleFG,\r\n"
				+ " TRY_CAST(BookNo AS NVARCHAR(20)) as BookNo,\r\n"
				+ " TRY_CAST(Center AS NVARCHAR(20)) as Center,\r\n"
				+ " TRY_CAST(LotNo AS NVARCHAR(50)) as LotNo,\r\n"
				+ " TRY_CAST(Batch AS NVARCHAR(30)) as Batch,\r\n"
				+ " TRY_CAST(LabNo AS NVARCHAR(30)) as LabNo,\r\n"
//				+ " TRY_CAST(RemarkOne AS NVARCHAR(200)) as RemarkOne,\r\n"
//				+ " TRY_CAST(RemarkTwo AS NVARCHAR(200)) as RemarkTwo,\r\n"
//				+ " TRY_CAST(RemarkThree AS NVARCHAR(200)) as RemarkThree,\r\n"
				+ "     CASE \r\n"
				+ "        WHEN RemarkOne IS NULL OR LEN(RemarkOne) < 1 THEN NULL\r\n"
				+ "        ELSE SUBSTRING(RemarkOne, 1, 100) \r\n"
				+ "    END AS RemarkOne,\r\n" 
				+ "    CASE \r\n"
				+ "        WHEN RemarkOne IS NULL OR LEN(RemarkOne) < 101 THEN NULL\r\n"
				+ "        ELSE SUBSTRING(RemarkOne, 101, 100) \r\n"
				+ "    END AS RemarkTwo,\r\n" 
				+ "    CASE \r\n"
				+ "        WHEN RemarkOne IS NULL OR LEN(RemarkOne) < 201 THEN NULL\r\n"
				+ "        ELSE SUBSTRING(RemarkOne, 201, 100) \r\n"
				+ "    END AS RemarkThree,"
				+ " TRY_CAST(BCAware AS NVARCHAR(200)) as BCAware,\r\n"
				+ " TRY_CAST(OrdErpuang AS NVARCHAR(200)) as OrdErpuang,\r\n"
				+ " TRY_CAST(RefPrd AS NVARCHAR(200)) as RefPrd,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN GreigeInDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN GreigeInDate is null or "
				+ "             GreigeInDate = '' THEN null \r\n"
				+ "        ELSE GreigeInDate  \r\n"
				+ "    END AS GreigeInDate, \r\n"  
				+ "  CASE \r\n"
				+ "        WHEN BCDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN BCDate is null or "
				+ "             BCDate = '' THEN null \r\n"
				+ "        ELSE BCDate  \r\n"
				+ "    END AS BCDate, \r\n"  
				+ " TRY_CAST(Volumn AS decimal(13, 3)) as Volumn,\r\n" 
//				+ " null as Volumn,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN CFdate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN CFdate is null or "
				+ "             CFdate = '' THEN null \r\n"
				+ "        ELSE CFdate  \r\n"
				+ "    END AS CFdate, \r\n"   
				+ "  CASE \r\n"
				+ "        WHEN CFType = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN CFType is null or "
				+ "             CFType = '' THEN null \r\n"
				+ "        ELSE CFType  \r\n"
				+ "    END AS CFType, \r\n"  
				+ " TRY_CAST(Shade AS NVARCHAR(30)) as Shade,\r\n"   
				+ "  CASE \r\n"
				+ "        WHEN LotShipping = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN LotShipping is null or "
				+ "             LotShipping = '' THEN null \r\n"
				+ "        ELSE TRY_CAST( LotShipping AS DATETIME )   \r\n"
				+ "    END AS LotShipping, \r\n"   
				+ " TRY_CAST(BillSendQuantity AS decimal(13, 3)) as BillSendQuantity,\r\n"
				+ " TRY_CAST(Grade AS NVARCHAR(30)) as Grade,\r\n"
//				+ " TRY_CAST(DataStatus AS NVARCHAR(20)) as DataStatus,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN PrdCreateDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN PrdCreateDate is null or "
				+ "             PrdCreateDate = '' THEN null \r\n"
				+ "        ELSE PrdCreateDate  \r\n"
				+ "    END AS PrdCreateDate, \r\n"  
				+ " TRY_CAST(GreigeArticle AS NVARCHAR(50)) as GreigeArticle,\r\n"
				+ " TRY_CAST(GreigeDesign AS NVARCHAR(50)) as GreigeDesign,\r\n" 
				+ " TRY_CAST(GreigeMR AS decimal(13, 3)) as GreigeMR,\r\n"
				+ " TRY_CAST(GreigeKG AS decimal(13, 3)) as GreigeKG,\r\n"
				+ " TRY_CAST(OrderType AS NVARCHAR(20)) as OrderType,\r\n"
				+ " [SyncDate]"
				+ " from [FromErpMainProd] "
				
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
				+ " TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrder,\r\n"
				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder,\r\n"
				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine,\r\n"
				+ " TRY_CAST(Volumn AS decimal(13, 3)) as Volumn ,\r\n\r\n"
//				+ " CASE"
//				+ "		WHEN SaleOrder = '' OR SaleOrder IS NULL THEN 'X' "
//				+ "		ELSE 'O' "
//				+ "	END AS DataStatus,"
				+ " [SyncDate]"
				+ " " 
				+ " from FromErpMainProdSale "
				
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
				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder,\r\n"
				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine,\r\n"
				+ " TRY_CAST(MaterialNo AS NVARCHAR(50)) as MaterialNo,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN DueDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN DueDate is null or "
				+ "             DueDate = '' THEN null \r\n"
				+ "        ELSE DueDate  \r\n"
				+ "    END AS DueDate, \r\n"   
				+ "  CASE \r\n"
				+ "        WHEN PlanGreigeDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN PlanGreigeDate is null or "
				+ "            PlanGreigeDate = '' THEN null \r\n"
				+ "        ELSE PlanGreigeDate  \r\n"
				+ "    END AS PlanGreigeDate, \r\n"  
				+ " TRY_CAST(SaleUnit AS NVARCHAR(20)) as SaleUnit,\r\n"
				+ " TRY_CAST(SaleQuantity AS decimal(13, 3)) as SaleQuantity,\r\n"
				+ " TRY_CAST(CustomerMaterial AS NVARCHAR(50)) as CustomerMaterial,\r\n"
				+ " TRY_CAST(Color AS NVARCHAR(20)) as Color,\r\n"
				+ " TRY_CAST(CustomerNo AS NVARCHAR(20)) as CustomerNo,\r\n"
				+ " TRY_CAST(PurchaseOrder AS NVARCHAR(20)) as PurchaseOrder,\r\n"
				+ " TRY_CAST(SaleOrg AS NVARCHAR(20)) as SaleOrg,\r\n"
				+ " TRY_CAST(DistChannel AS NVARCHAR(20)) as DistChannel,\r\n"
				+ " TRY_CAST(Division AS NVARCHAR(20)) as Division,\r\n"
				+ " TRY_CAST(CustomerName AS NVARCHAR(100)) as CustomerName,\r\n"
				+ " TRY_CAST(CustomerShortName AS NVARCHAR(100)) as CustomerShortName,\r\n"
				+ " TRY_CAST(ColorCustomer AS NVARCHAR(50)) as ColorCustomer,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN CustomerDue = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN CustomerDue is null or "
				+ "             CustomerDue = '' THEN null \r\n"
				+ "        ELSE CustomerDue  \r\n"
				+ "    END AS CustomerDue, \r\n"  
				+ " TRY_CAST(RemainQuantity AS decimal(13, 3)) as RemainQuantity,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN ShipDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN ShipDate is null or  "
				+ "             ShipDate = '' THEN null \r\n"
				+ "        ELSE ShipDate  \r\n"
				+ "      END AS ShipDate, \r\n"  
				+ " TRY_CAST(SaleStatus AS NVARCHAR(1)) as SaleStatus,\r\n"
				+ " TRY_CAST(Currency AS NVARCHAR(20)) as Currency,\r\n"
				+ " TRY_CAST(Price AS decimal(13, 3)) as Price,\r\n"
				+ " TRY_CAST(OrderAmount AS decimal(13, 3)) as OrderAmount,\r\n"
				+ " TRY_CAST(RemainAmount  AS decimal(13, 3)) as RemainAmount,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN SaleCreateDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN SaleCreateDate is null or "
				+ "             SaleCreateDate = '' THEN null \r\n"
				+ "        ELSE TRY_CAST( SaleCreateDate AS DATETIME )   \r\n"
				+ "      END AS SaleCreateDate, \r\n"  
				+ " TRY_CAST(SaleNumber AS NVARCHAR(20)) as SaleNumber,\r\n"
				+ " TRY_CAST(SaleFullName AS NVARCHAR(100)) as SaleFullName,\r\n"
				+ " TRY_CAST(DeliveryStatus AS NVARCHAR(1)) as DeliveryStatus,\r\n"
				+ " TRY_CAST(DesignFG AS NVARCHAR(50)) as DesignFG,\r\n"
				+ " TRY_CAST(ArticleFG AS NVARCHAR(50)) as ArticleFG,\r\n" 
//				+ "  OrderSheetPrintDate,"
				+ " CASE \r\n"
				+ "        WHEN OrderSheetPrintDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN OrderSheetPrintDate is null or "
				+ "             OrderSheetPrintDate = '' THEN null \r\n"
				+ "        ELSE OrderSheetPrintDate  \r\n"
				+ "    END AS OrderSheetPrintDate, \r\n"  
				+ " TRY_CAST(CustomerMaterialBase  AS NVARCHAR(50)) as CustomerMaterialBase, \r\n"
				+ " [SyncDate]" 
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
				+ " TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrder,\r\n"
				+ "  CASE \r\n"
				+ "        WHEN PostingDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN PostingDate is null or  "
				+ "             PostingDate = '' THEN null \r\n"
				+ "        ELSE TRY_CAST( PostingDate AS DATETIME )  \r\n"
				+ "      END AS PostingDate, \r\n"   
				+ " TRY_CAST(Quantity AS decimal(13, 3)) as Quantity,\r\n"
				+ " TRY_CAST(RollNo AS NVARCHAR(10)) as RollNo,\r\n"
				+ " TRY_CAST(QuantityKG AS decimal(13, 3)) as QuantityKG,\r\n"
				+ " TRY_CAST(Grade AS NVARCHAR(10)) as Grade,\r\n"
				+ " TRY_CAST(No AS NVARCHAR(10)) as No,\r\n"
				+ " TRY_CAST(QuantityYD AS decimal(13, 3)) as QuantityYD ,\r\n"
				+ " [SyncDate]"
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
				+ " TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrder,\r\n"
				+ " TRY_CAST(RollNo AS NVARCHAR(10)) as RollNo,\r\n"
				+ " TRY_CAST(QuantityKG AS decimal(13, 3)) as QuantityKG,\r\n"
				+ " TRY_CAST(QuantityMR AS decimal(13, 3)) as QuantityMR,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN POCreatedate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN POCreatedate is null or  "
				+ "             POCreatedate = '' THEN null \r\n" 
				+ "        ELSE TRY_CAST( POCreatedate AS DATETIME )  \r\n"
				+ "      END AS POCreatedate, \r\n"    
				+ "  CASE \r\n"
				+ "        WHEN RequiredDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN RequiredDate is null or  "
				+ "             RequiredDate = '' THEN null \r\n" 
				+ "        ELSE TRY_CAST( RequiredDate AS DATETIME )  \r\n"
				+ "      END AS RequiredDate, \r\n"   
				+ " TRY_CAST(PurchaseOrder AS NVARCHAR(15)) as PurchaseOrder,\r\n"
				+ " TRY_CAST(PurchaseOrderLine AS NVARCHAR(5)) as PurchaseOrderLine,\r\n"
				+ " TRY_CAST(PODefault AS NVARCHAR(15)) as PODefault,\r\n"
				+ " TRY_CAST(POLineDefault AS NVARCHAR(5)) as POLineDefault,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN POPostingDateDefault = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN POPostingDateDefault is null or  "
				+ "             POPostingDateDefault = '' THEN null \r\n"
//				+ "        ELSE POPostingDateDefault  \r\n"
				+ "        ELSE TRY_CAST( POPostingDateDefault AS DATETIME )  \r\n"
				+ "      END AS POPostingDateDefault, \r\n"   
				+ "  [SyncDate]"
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
				+ " TRY_CAST( [ProductionOrder] AS NVARCHAR(50)) as ProductionOrder  ,\r\n"
				+ " TRY_CAST( [LotNo] AS NVARCHAR(50)) as LotNo  ,"
				+ "  [SyncDate]"
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
				+ " TRY_CAST( [PRODUCTIONID] AS NVARCHAR(50)) as ProductionOrder,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN BillDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN BillDate is null or  "
				+ "             BillDate = '' THEN null \r\n"
				+ "        ELSE BillDate  \r\n"
				+ "      END AS BillDate, \r\n"   
				+ " TRY_CAST(BillQtyPerSale AS decimal(13, 3)) as BillQtyPerSale,\r\n"
				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder,\r\n"
				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine,\r\n"
				+ " TRY_CAST(BillQtyPerStock AS decimal(13, 3)) as BillQtyPerStock,\r\n"
				+ " TRY_CAST(Remark AS NVARCHAR(100)) as Remark,\r\n"
				+ " TRY_CAST(CustomerNo AS NVARCHAR(20)) as CustomerNo,\r\n"
				+ " TRY_CAST(CustomerName1 AS NVARCHAR(50)) as CustomerName1,\r\n"
				+ " TRY_CAST(CustomErpO AS NVARCHAR(30)) as CustomErpO,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN DueDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN DueDate is null or  "
				+ "             DueDate = '' THEN null \r\n"
				+ "        ELSE DueDate  \r\n"
				+ "      END AS DueDate, \r\n"   
				+ " TRY_CAST(Color AS NVARCHAR(20)) as Color,\r\n"
//				+ " TRY_CAST(No AS NVARCHAR(3)) as No, \r\n"
				+ "  [SyncDate]"
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
				+ " TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrder,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN BillDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN BillDate is null or  "
				+ "             BillDate = '' THEN null \r\n"
				+ "        ELSE BillDate  \r\n"
				+ "      END AS BillDate, \r\n"   
				+ " TRY_CAST(BillQtyPerSale AS decimal(13, 3)) as BillQtyPerSale,\r\n"
				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder,\r\n"
				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine,\r\n"
				+ " TRY_CAST(BillQtyPerStock AS decimal(13, 3)) as BillQtyPerStock,\r\n"
				+ " TRY_CAST(Remark AS NVARCHAR(100)) as Remark,\r\n"
				+ " TRY_CAST(CustomerNo AS NVARCHAR(20)) as CustomerNo,\r\n"
				+ " TRY_CAST(CustomerName1 AS NVARCHAR(50)) as CustomerName1,\r\n"
				+ " TRY_CAST(CustomErpO AS NVARCHAR(30)) as CustomErpO,\r\n" 
				+ "  CASE \r\n"
				+ "        WHEN DueDate = '1900-01-01 00:00:00.000' THEN null\r\n"
				+ "        WHEN DueDate is null or  "
				+ "             DueDate = '' THEN null \r\n"
				+ "        ELSE DueDate  \r\n"
				+ "      END AS DueDate, \r\n"   
				+ " TRY_CAST(Color AS NVARCHAR(20)) as Color,\r\n"
//				+ " TRY_CAST(No AS NVARCHAR(3)) as No, \r\n"
				+ "  [SyncDate]"
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
				+ " TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrder  ,\r\n"
				+ " TRY_CAST( SaleOrder AS NVARCHAR(50)) as SaleOrder  ,\r\n"
				+ " TRY_CAST( SaleLine AS NVARCHAR(50)) as SaleLine  ,\r\n"
				+ " TRY_CAST( No AS NVARCHAR(2)) as No  ,\r\n" 
				+ " CASE \r\n"
				+ "        WHEN SubmitDate = '1900-01-01 00:00:00.000' THEN NULL \r\n"
				+ "        WHEN SubmitDate is null or  "
				+ "             SubmitDate = '' THEN null \r\n"
				+ "        ELSE SubmitDate \r\n"
				+ "      END AS SubmitDate, \r\n" 
				+ " TRY_CAST( Remark AS NVARCHAR(50)) as Remark,  \r\n"
				+ " [SyncDate]" 
				+ " from [FromErpSubmitDate] "
				
				; 
//		System.out.println(sql);
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
