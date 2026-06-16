package th.co.wacoal.atech.pcms2.dao.master.implement.erp.atech;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.erp.atech.ERPAtechDao;
import th.co.wacoal.atech.pcms2.entities.ProductionOrderLogDetail;
import th.co.wacoal.atech.pcms2.entities.SaleOrderLogDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainBillBatchDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPODetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.MapperUtility;
import th.in.totemplate.core.sql.Database;

@Component
public class ERPAtechDaoImpl implements ERPAtechDao {
	private BeanCreateService bcModel = new BeanCreateService();
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	private Database database;

	@Autowired
	public ERPAtechDaoImpl(@Qualifier("erpDatabase") Database database) {
		this.message = "";
		this.database = database;
	}

	public String getMessage()
	{
		return this.message;
	}
	private String declareTimeFocus = ""  
	+ " DECLARE @oneHourAgo DATETIME = DATEADD(HOUR, -1, GETDATE()); \r\n"
//	private String declareTimeFocus = "" 
//	+ " declare  @oneHourAgo datetime = DATEADD(MINUTE, -60, GETDATE());";

//+" declare  @oneHourAgo datetime = DATEADD(MONTH, -1, GETDATE());"; 
//+" declare  @oneHourAgo datetime = DATEADD(MONTH, -4, GETDATE());"; 
//	+" declare  @oneHourAgo datetime = DATEADD(DAY, -2, GETDATE());"; 
;
	@Override
	public ArrayList<CustomerDetail> getCustomerDetail()
	{
		ArrayList<CustomerDetail> list = new ArrayList<>();
		String sql = " "
				+ this.declareTimeFocus
				+ " SELECT distinct   \r\n"
				+ " CASE \r\n"
				+ "		WHEN LEN([CustomerNo]) > 10 THEN [CustomerNo]\r\n"
				+ "		WHEN TRY_CAST([CustomerNo] AS INT ) IS NULL  THEN [CustomerNo]\r\n"
				+ "		ELSE RIGHT('0000000000'+ISNULL([CustomerNo],''),10) \r\n"
				+ "	END as [CustomerNo] ,"
				+ " TRY_CAST( CustomerNo AS NVARCHAR(50)) as CustomerNoWOZero  ,\r\n"
				+ " TRY_CAST( CustomerName AS NVARCHAR(500)) as CustomerName  ,\r\n"
				+ " TRY_CAST( CustomerShortName AS NVARCHAR(500)) as CustomerShortName  ,\r\n"
				// สลับ CustomerType , DistChannel เพื่อให้ข้อมูลตรงกับ LOGIC ของ Field :
				// DistChannel จาก SOR
				+ " TRY_CAST( CustomerType AS NVARCHAR(50)) as DistChannel  ,\r\n"
				+ " TRY_CAST( DistChannel AS NVARCHAR(50)) as CustomerType, \r\n"
				+ "	 case\r\n"
				+ "		 when [CustomerName] not like '%SABINA%' and [CustomerShortName] not like '%SBF%'\r\n"
				+ "		 then CAST ( 0 AS BIT ) \r\n"
				+ "		 else CAST ( 1 AS BIT ) "
				+ "	end as IsSabina ,"
				+ " [SyncDate] "
				+ " from CustomerDetail "
				+ " where TRY_CAST( CustomerNo AS int) is not null and "
				+ "       SyncDate >= @oneHourAgo ";
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
		String sql = " "
				+ this.declareTimeFocus
				+ "WITH ProductionOrders AS (\r\n"
				+ "    SELECT DISTINCT a.[ProductionOrder]\r\n"
				+ "    FROM [FromErpMainProd] AS a\r\n"
				+ "    WHERE SyncDate >= @oneHourAgo\r\n"
				+ "\r\n"
				+ "    UNION\r\n"
				+ "\r\n"
				+ "    SELECT DISTINCT a.[ProductionOrder]\r\n"
				+ "    FROM [FromErpCFM] AS a\r\n"
				+ "    WHERE SyncDate >= @oneHourAgo\r\n"
				+ "),\r\n"
				+ "CFMData AS (\r\n"
				+ "    SELECT DISTINCT\r\n"
				+ "        TRY_CAST(ProductionOrder AS NVARCHAR(50)) AS ProductionOrderCheck,\r\n"
				+ "        TRY_CAST(CFMNo AS NVARCHAR(2)) AS CFMNo,\r\n"
				+ "        TRY_CAST(CFMNumber AS NVARCHAR(20)) AS CFMNumber,\r\n"
				+ "        CASE\r\n"
				+ "            WHEN CFMSendDate = '1900-01-01 00:00:00.000' THEN NULL\r\n"
				+ "            WHEN CFMSendDate IS NULL OR CFMSendDate = '' THEN NULL\r\n"
				+ "            ELSE TRY_CAST(CFMSendDate as DATETIME)\r\n"
				+ "        END AS CFMSendDate,\r\n"
				+ "        CASE\r\n"
				+ "            WHEN CFMAnswerDate = '1900-01-01 00:00:00.000' THEN NULL\r\n"
				+ "            WHEN CFMAnswerDate IS NULL OR CFMAnswerDate = '' THEN NULL\r\n"
				+ "            ELSE TRY_CAST(CFMAnswerDate as DATETIME)\r\n"
				+ "        END AS CFMAnswerDate,\r\n"
				+ " 		CASE "
				+ "				WHEN CFMAnswerDate = '1900-01-01 00:00:00.000' THEN TRY_CAST(NULL AS BIT) \r\n"
				+ "				WHEN CFMAnswerDate is null or CFMAnswerDate = '' THEN TRY_CAST(NULL AS BIT) \r\n"
				+ "				ELSE TRY_CAST(CFMStatus AS BIT) "
				+ "				END AS CFMStatus  ,\r\n"
//				+ "        TRY_CAST(CFMStatus AS NVARCHAR(30)) AS CFMStatus,\r\n"
				+ "        TRY_CAST(CFMRemark AS NVARCHAR(80)) AS CFMRemark,\r\n"
				+ "        TRY_CAST(SaleOrder AS NVARCHAR(50)) AS SaleOrder,\r\n"
				+ "        TRY_CAST(SaleLine AS NVARCHAR(50)) AS SaleLine,\r\n"
				+ "        TRY_CAST(RollNoRemark AS NVARCHAR(200)) AS RollNoRemark,\r\n"
				+ "        [SyncDate]\r\n"
				+ "    FROM FromErpCFM\r\n"
				+ ")\r\n"
				+ "SELECT A.[ProductionOrder]\r\n"
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,B.*\r\n"
				+ "FROM ProductionOrders AS A\r\n"
				+ "LEFT JOIN CFMData AS B ON A.[ProductionOrder] = B.[ProductionOrderCheck]  \r\n"
				+ " WHERE   a.ProductionOrder NOT LIKE '20%' \r\n"
				+ "       AND a.ProductionOrder NOT LIKE 'WO%' \r\n"
				+ "       AND a.ProductionOrder NOT LIKE 'Y2%';"
//				+ " SELECT distinct   \r\n" 
//				+ " TRY_CAST(ProductionOrder AS NVARCHAR(50)) as ProductionOrder ,\r\n"
//				+ " TRY_CAST(CFMNo AS NVARCHAR(2)) as CFMNo ,\r\n"
//				+ " TRY_CAST(CFMNumber AS NVARCHAR(20)) as CFMNumber ,\r\n"
//				+ "  CASE \r\n"
//				+ "        WHEN CFMSendDate = '1900-01-01 00:00:00.000' THEN null \r\n" 
//				+ "        WHEN CFMSendDate is null or CFMSendDate = '' THEN null \r\n"
//				+ "        ELSE CFMSendDate \r\n"
//				+ "    END AS CFMSendDate, \r\n"   
//				+ "  CASE \r\n"
//				+ "        WHEN CFMAnswerDate = '1900-01-01 00:00:00.000' THEN null\r\n"
//				+ "        WHEN CFMAnswerDate is null or CFMAnswerDate = '' THEN null \r\n"
//				+ "        ELSE CFMAnswerDate \r\n"
//				+ "    END AS CFMAnswerDate, \r\n"  
//				+ " TRY_CAST( CFMStatus AS NVARCHAR(30)) as CFMStatus ,\r\n" 
//				+ " TRY_CAST( CFMRemark AS NVARCHAR(80)) as CFMRemark ,\r\n" 
//				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder ,\r\n"
//				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine ,\r\n"
//				+ " TRY_CAST(RollNoRemark AS NVARCHAR(200)) as RollNoRemark,  \r\n" 
//				+ " [SyncDate]"
//				+ " from FromErpCFM"

		;
//		System.out.println(sql);
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
		String sql = " "
				+ this.declareTimeFocus
				+ "WITH ProductionOrders AS ( \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM [FromErpMainProd] AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "				  \r\n"
				+ "	UNION \r\n"
				+ "				  \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM FromErpGoodReceive AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "), \r\n"
				+ "GoodReceive AS ( \r\n"
				+ "	SELECT DISTINCT \r\n"
				+ "	TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrderCheck,  \r\n"
				+ "	TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder,  \r\n"
				+ "	TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine,  \r\n"
				+ "	TRY_CAST(Grade AS NVARCHAR(20)) as Grade,  \r\n"
				+ "	TRY_CAST(RollNumber AS NVARCHAR(20)) as RollNumber,  \r\n"
				+ "	TRY_CAST(QuantityKG AS decimal(13, 3)) as QuantityKG,  \r\n"
				+ "	TRY_CAST(QuantityYD AS decimal(13, 3)) as QuantityYD,  \r\n"
				+ "	TRY_CAST(QuantityMR AS decimal(13, 3)) as QuantityMR,  \r\n"
				+ "	TRY_CAST(PriceSTD AS decimal(13, 3)) as PriceSTD,   \r\n"
				+ "	[SyncDate] \r\n"
				+ "	FROM FromErpGoodReceive \r\n"
				+ ") \r\n"
				+ "				  \r\n"
				+ "SELECT A.[ProductionOrder]\r\n"
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,B.*\r\n"
				+ "FROM ProductionOrders AS A \r\n"
				+ "LEFT JOIN GoodReceive AS B ON A.[ProductionOrder] = B.[ProductionOrderCheck]  \r\n"
				+ " WHERE "
				+ "      1 = 1 "
				+ "     AND a.ProductionOrder NOT LIKE '20%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'WO%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'Y2%' ; "
//				+ " SELECT distinct   \r\n"
//				+ " TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrder,\r\n"
//				+ " TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder,\r\n"
//				+ " TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine,\r\n"
//				+ " TRY_CAST(Grade AS NVARCHAR(20)) as Grade,\r\n"
//				+ " TRY_CAST(RollNumber AS NVARCHAR(20)) as RollNumber,\r\n"
//				+ " TRY_CAST(QuantityKG AS decimal(13, 3)) as QuantityKG,\r\n"
//				+ " TRY_CAST(QuantityYD AS decimal(13, 3)) as QuantityYD,\r\n"
//				+ " TRY_CAST(QuantityMR AS decimal(13, 3)) as QuantityMR,\r\n"
//				+ " TRY_CAST(PriceSTD AS decimal(13, 3)) as PriceSTD, \r\n"
//				+ " [SyncDate]"
//				+ " "
//				+ " from FromErpGoodReceive"

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
		String sql = " "
				+ this.declareTimeFocus
				+ " WITH ProductionOrders AS (\r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM [FromErpMainProd] AS a\r\n"
				+ "	WHERE [SyncDate] >= @oneHourAgo    \r\n"
				+ "	UNION \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM FromErpMainBillBatch AS a\r\n"
				+ "	WHERE SyncDate >= @oneHourAgo\r\n"
				+ "),\r\n"
				+ "BillBatch  AS (\r\n"
				+ "    SELECT DISTINCT\r\n"
				+ "				  TRY_CAST(BillDoc AS NVARCHAR(20)) as BillDoc, \r\n"
				+ "				  TRY_CAST(BillItem AS NVARCHAR(20)) as BillItem,  \r\n"
				+ "				   CASE  \r\n"
				+ "				         WHEN LotShipping = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				         WHEN LotShipping is null or LotShipping = '' THEN null  \r\n"
				+ "				         ELSE TRY_CAST( LotShipping AS DATETIME)  \r\n"
				+ "				     END AS LotShipping,    \r\n"
				+ "				  TRY_CAST(ProductionOrder AS NVARCHAR(50)) as ProductionOrderCheck, \r\n"
				+ "				  TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder , \r\n"
				+ "				  TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine , \r\n"
				+ "				  TRY_CAST(Grade AS NVARCHAR(20)) as Grade, \r\n"
				+ "				  TRY_CAST(RollNumber AS NVARCHAR(20)) as RollNumber, \r\n"
				+ "				  TRY_CAST(QuantityKG AS decimal(13, 3)) as QuantityKG, \r\n"
				+ "				  TRY_CAST(QuantityYD AS decimal(13, 3)) as QuantityYD, \r\n"
				+ "				  TRY_CAST(QuantityMR AS decimal(13, 3)) as QuantityMR,  	\r\n"
				+ "				  [SyncDate]   \r\n"
				+ "				  from FromErpMainBillBatch \r\n"
				+ ") \r\n"
				+ "\r\n"
				+ " SELECT a.[ProductionOrder] "
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,       B.BillDoc, B.BillItem, B.LotShipping,\r\n"
				+ "       -- ไม่ใช้ B.* เพราะมี B.ProductionOrder ซ้อนทับ\r\n"
				+ "       B.SaleOrder, B.SaleLine, B.Grade, B.RollNumber,\r\n"
				+ "       B.QuantityKG, B.QuantityYD, B.QuantityMR, B.SyncDate\r\n"
				+ " FROM ProductionOrders AS A\r\n"
				+ " LEFT JOIN BillBatch AS B ON A.[ProductionOrder] = B.[ProductionOrderCheck]   \r\n"
				+ " WHERE "
				+ "      1 = 1 "
				+ "     AND a.ProductionOrder NOT LIKE '20%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'WO%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'Y2%' ; "; 
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
		String sql = ""
				+ this.declareTimeFocus
				+ "WITH ProductionOrders AS ( \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM [FromErpMainProd] AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "), \r\n"
				+ "MainProd AS (  \r\n"
				+ "	SELECT distinct    \r\n"
				+ "		TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrderCheck, \r\n"
				+ "		TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder, \r\n"
				+ "		TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine, \r\n"
				+ "		TRY_CAST(TotalQuantity AS decimal(13, 3)) as TotalQuantity, \r\n"
				+ "		CASE  \r\n"
				+ "			WHEN TRY_CAST( Unit AS NVARCHAR(20)) = 'M' THEN 'MR' \r\n"
				+ "			ELSE TRY_CAST( Unit AS NVARCHAR(20)) \r\n"
				+ "		END as Unit,   \r\n"
				+ "			CASE  \r\n"
				+ "				WHEN RemAfterCloseOne IS NULL OR LEN(RemAfterCloseOne) < 1 THEN NULL \r\n"
				+ "				ELSE SUBSTRING(RemAfterCloseOne, 1, 100)  \r\n"
				+ "			END AS RemAfterCloseOne,  \r\n"
				+ "			CASE   \r\n"
				+ "				WHEN RemAfterCloseOne IS NULL OR LEN(RemAfterCloseOne) < 101 THEN NULL \r\n"
				+ "				ELSE SUBSTRING(RemAfterCloseOne, 101, 100)  \r\n"
				+ "			END AS RemAfterCloseTwo,  \r\n"
				+ "			CASE  \r\n"
				+ "				WHEN RemAfterCloseOne IS NULL OR LEN(RemAfterCloseOne) < 201 THEN NULL \r\n"
				+ "				ELSE SUBSTRING(RemAfterCloseOne, 201, 100)  \r\n"
				+ "			END AS RemAfterCloseThree, \r\n"
				+ "		CASE WHEN LabStatus = '' THEN '-' "
				+ "			ELSE TRY_CAST(LabStatus AS NVARCHAR(50)) "
				+ "			END as LabStatus, \r\n"
				+ "		TRY_CAST(UserStatus AS NVARCHAR(50)) as UserStatus, \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(DesignFG AS NVARCHAR(50)))) as DesignFG, \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(ArticleFG AS NVARCHAR(50)))) as ArticleFG, \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(BookNo AS NVARCHAR(20)))) as BookNo, \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(Center AS NVARCHAR(20)))) as Center, \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(LotNo AS NVARCHAR(50)))) as LotNo, \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(Batch AS NVARCHAR(30)))) as Batch, \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(LabNo AS NVARCHAR(30)))) as LabNo,  \r\n"
				+ "			CASE  \r\n"
				+ "				WHEN RemarkOne IS NULL OR LEN(RemarkOne) < 1 THEN NULL \r\n"
				+ "				ELSE SUBSTRING(RemarkOne, 1, 100)  \r\n"
				+ "			END AS RemarkOne,  \r\n"
				+ "			CASE  \r\n"
				+ "				WHEN RemarkOne IS NULL OR LEN(RemarkOne) < 101 THEN NULL \r\n"
				+ "				ELSE SUBSTRING(RemarkOne, 101, 100)  \r\n"
				+ "			END AS RemarkTwo,  \r\n"
				+ "			CASE  \r\n"
				+ "				WHEN RemarkOne IS NULL OR LEN(RemarkOne) < 201 THEN NULL \r\n"
				+ "				ELSE SUBSTRING(RemarkOne, 201, 100)  \r\n"
				+ "			END AS RemarkThree, \r\n"
				+ "		TRY_CAST(BCAware AS NVARCHAR(200)) as BCAware, \r\n"
				+ "		TRY_CAST(OrdErpuang AS NVARCHAR(200)) as OrdErpuang, \r\n"
				+ "		TRY_CAST(RefPrd AS NVARCHAR(200)) as RefPrd,  \r\n"
				+ "		CASE  \r\n"
				+ "				WHEN GreigeInDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				WHEN GreigeInDate is null or  \r\n"
				+ "					GreigeInDate = '' THEN null  \r\n"
				+ "				ELSE GreigeInDate   \r\n"
				+ "			END AS GreigeInDate,    \r\n"
				+ "		CASE  \r\n"
				+ "				WHEN BCDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				WHEN BCDate is null or  \r\n"
				+ "					BCDate = '' THEN null  \r\n"
				+ "				ELSE BCDate   \r\n"
				+ "			END AS BCDate,    \r\n"
				+ "		TRY_CAST(Volumn AS decimal(13, 3)) as Volumn,   \r\n"
				+ "		CASE  \r\n"
				+ "				WHEN CFdate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				WHEN CFdate is null or  \r\n"
				+ "					CFdate = '' THEN null  \r\n"
				+ "				ELSE CFdate   \r\n"
				+ "			END AS CFdate,     \r\n"
				+ "		CASE  \r\n"
				+ "				WHEN CFType = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				WHEN CFType is null or  \r\n"
				+ "					CFType = '' THEN null  \r\n"
				+ "				ELSE TRY_CAST(CFType as Datetime)   \r\n"
				+ "			END AS CFType,    \r\n"
				+ "		TRY_CAST(Shade AS NVARCHAR(30)) as Shade,    \r\n"
				+ "		CASE  \r\n"
				+ "				WHEN LotShipping = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				WHEN LotShipping is null or  \r\n"
				+ "					LotShipping = '' THEN null  \r\n"
				+ "				ELSE TRY_CAST( LotShipping AS DATETIME )    \r\n"
				+ "			END AS LotShipping,     \r\n"
				+ "		TRY_CAST(BillSendQuantity AS decimal(13, 3)) as BillSendQuantity, \r\n"
				+ "		TRY_CAST(Grade AS NVARCHAR(30)) as Grade,  \r\n"
				+ "		CASE  \r\n"
				+ "				WHEN PrdCreateDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				WHEN PrdCreateDate is null or  \r\n"
				+ "					PrdCreateDate = '' THEN null  \r\n"
				+ "				ELSE PrdCreateDate   \r\n"
				+ "			END AS PrdCreateDate,    \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(GreigeArticle AS NVARCHAR(50)))) as GreigeArticle, \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(GreigeDesign AS NVARCHAR(50)))) as GreigeDesign,  \r\n"
				+ "		TRY_CAST(GreigeMR AS decimal(13, 3)) as GreigeMR, \r\n"
				+ "		TRY_CAST(GreigeKG AS decimal(13, 3)) as GreigeKG, \r\n"
				+ "		TRY_CAST(OrderType AS NVARCHAR(20)) as OrderType, \r\n"
				+ "		[SyncDate] \r\n"
				+ "	from [FromErpMainProd]  \r\n"
				+ ") \r\n"
				+ "SELECT A.[ProductionOrder]\r\n"
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,B.*\r\n"
				+ "FROM ProductionOrders AS A \r\n"
				+ "LEFT JOIN MainProd AS B ON A.[ProductionOrder] = B.[ProductionOrderCheck]  \r\n"
				+ " WHERE "
				+ "      1 = 1 "
				+ "     AND a.ProductionOrder NOT LIKE '20%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'WO%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'Y2%' ; ";
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
		String sql = " "
				+ this.declareTimeFocus
				+ "WITH ProductionOrders AS ( \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM [FromErpMainProd] AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "				  \r\n"
				+ "	UNION \r\n"
				+ "				  \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM FromErpMainProdSale AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "), \r\n"
				+ "MainProdSale AS (   \r\n"
				+ "	SELECT distinct    \r\n"
				+ "	TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrderCheck, \r\n"
				+ "	TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrder, \r\n"
				+ "	TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine, \r\n"
				+ "	TRY_CAST(Volumn AS decimal(13, 3)) as Volumn , \r\n"
				+ "	[SyncDate] \r\n"
				+ "	from FromErpMainProdSale  \r\n"
				+ ") \r\n"
				+ "				  \r\n"
				+ "SELECT A.[ProductionOrder]\r\n"
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,B.*\r\n"
				+ "FROM ProductionOrders AS A \r\n"
				+ "LEFT JOIN MainProdSale AS B ON A.[ProductionOrder] = B.[ProductionOrderCheck]  \r\n"
				+ " WHERE "
				+ "      1 = 1 "
				+ "     AND a.ProductionOrder NOT LIKE '20%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'WO%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'Y2%' ; ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpMainProdSaleDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<ProductionOrderLogDetail> getFromErpMainProdDetailWithRangeOfChangeDate(String changeDateStart,
			String changeDateEnd, String productionOrder)
	{
		ArrayList<ProductionOrderLogDetail> list = null;
		String where = " WHERE 1 = 1  ";
		if ( ! changeDateStart.equals("")) {
			where += " "
					+ " and (  "
					+ "	CAST(a.[SyncDate] AS DATE) >= convert(date,'"
					+ changeDateStart
					+ "', 103) AND \r\n"
					+ "	CAST(a.[SyncDate] AS DATE) <= convert(date,'"
					+ changeDateEnd
					+ "', 103) \r\n"
					+ "	) \r\n";
		}
		if ( ! productionOrder.equals("")) {
			where += " " + " and (  " + " a.[ProductionOrder] = '" + productionOrder + "' \r\n" + "	) \r\n";
		}
		String sql = " "
				+ " SELECT  \r\n"
				+ "	      [ProductionOrder]\r\n"
				+ "      ,[SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[TotalQuantity]\r\n"
				+ "      ,[Volumn]\r\n"
				+ "      ,[Unit]\r\n"
				+ "      ,[LabStatus]\r\n"
				+ "      ,[UserStatus]\r\n"
				+ "      ,[DesignFG]\r\n"
				+ "      ,[ArticleFG]\r\n"
				+ "      ,[BookNo]\r\n"
				+ "      ,[Center]\r\n"
				+ "      ,[LotNo]\r\n"
				+ "      ,[LabNo] \r\n"
				+ "      ,CASE \r\n"
				+ "        WHEN [GREIGEINDATE] = '1900-01-01 00:00:00.000' THEN CAST( null AS Date )   \r\n"
				+ "        WHEN [GREIGEINDATE] is null or \r\n"
				+ "             [GREIGEINDATE] = '' THEN CAST( null AS Date )  \r\n"
				+ "        ELSE CAST( [GREIGEINDATE] AS Date )   \r\n"
				+ "		END AS GreigeInDate  \r\n"
				+ "      ,[Shade]\r\n"
				+ "      ,CASE \r\n"
				+ "        WHEN [PrdCreateDate] = '1900-01-01 00:00:00.000' THEN CAST( null AS Date )   \r\n"
				+ "        WHEN [PrdCreateDate] is null or \r\n"
				+ "             [PrdCreateDate] = '' THEN CAST( null AS Date )  \r\n"
				+ "        ELSE CAST( [PrdCreateDate] AS Date )   \r\n"
				+ "		END AS [PrdCreateDate]    \r\n"
				+ "      ,[GreigeArticle]\r\n"
				+ "      ,[GreigeDesign]\r\n"
				+ "      ,[OrderType]\r\n"
				+ "      ,[SyncDate]\r\n"
				+ " FROM [FromErpMainProd] a\r\n"
				+ where
				+ " Order by SyncDate desc";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genProductionOrderLogDetail(map));
			list.add(MapperUtility.mapToObject(map, ProductionOrderLogDetail.class));
		}
		return list;
	}

	@Override
	public ArrayList<FromErpMainSaleDetail> getFromErpMainSaleDetail()
	{
		ArrayList<FromErpMainSaleDetail> list = new ArrayList<>();
		String sql = " "
				+ this.declareTimeFocus
				+ "WITH ProductionOrders AS (  		  \r\n"
				+ "	SELECT DISTINCT a.SaleOrder \r\n"
				+ "	FROM FromErpMainSale AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "	      OR [SyncDateHeader] >= @oneHourAgo\r\n"
				+ "	      OR [syncDateProdHeader] >= @oneHourAgo\r\n"
				+ "), \r\n"
				+ "MainSale AS (   \r\n"
				+ " SELECT distinct    \r\n"
				+ "				  TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrderCheck, \r\n"
				+ "				  TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine , \r\n"
//				+ "				  TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLineCheck, \r\n"
				+ "				  TRY_CAST(MaterialNo AS NVARCHAR(50)) as MaterialNo,  \r\n"
				+ "				   CASE  \r\n"
				+ "				         WHEN DueDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				         WHEN DueDate is null or  \r\n"
				+ "				              DueDate = '' THEN null  \r\n"
				+ "				         ELSE DueDate   \r\n"
				+ "				     END AS DueDate,     \r\n"
				+ "				   CASE  \r\n"
				+ "				         WHEN PlanGreigeDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				         WHEN PlanGreigeDate is null or  \r\n"
				+ "				             PlanGreigeDate = '' THEN null  \r\n"
				+ "				         ELSE PlanGreigeDate   \r\n"
				+ "				     END AS PlanGreigeDate,    \r\n"
				+ "				  TRY_CAST(SaleUnit AS NVARCHAR(20)) as SaleUnit, \r\n"
				+ "				  TRY_CAST(SaleQuantity AS decimal(13, 3)) as SaleQuantity, \r\n"
				+ "				  TRY_CAST(CustomerMaterial AS NVARCHAR(50)) as CustomerMaterial, \r\n"
				+ "				  TRY_CAST(Color AS NVARCHAR(20)) as Color, \r\n"
//				+ "				  TRY_CAST(CustomerNo AS NVARCHAR(20)) as CustomerNo, \r\n"
				+ " CASE \r\n"
				+ "		WHEN LEN([CustomerNo]) > 10 THEN [CustomerNo]\r\n"
				+ "		WHEN TRY_CAST([CustomerNo] AS INT ) IS NULL  THEN [CustomerNo]\r\n"
				// 20250506
//				+ "		ELSE [CustomerNo] \r\n"
				+ "		ELSE RIGHT('0000000000'+ISNULL([CustomerNo],''),10) \r\n"
				+ "	END as [CustomerNo] ,"
				+ "				  TRY_CAST(PurchaseOrder AS NVARCHAR(50)) as PurchaseOrder, \r\n"
				+ "				  TRY_CAST(SaleOrg AS NVARCHAR(50)) as SaleOrg, \r\n"
				+ "				  TRY_CAST(DistChannel AS NVARCHAR(50)) as DistChannel, \r\n"
				+ "				  TRY_CAST(Division AS NVARCHAR(50)) as Division, \r\n"
				+ "				  TRY_CAST(CustomerName AS NVARCHAR(100)) as CustomerName, \r\n"
				+ "				  TRY_CAST(CustomerShortName AS NVARCHAR(100)) as CustomerShortName, \r\n"
				+ "				  TRY_CAST(ColorCustomer AS NVARCHAR(50)) as ColorCustomer,  \r\n"
				+ "				   CASE  \r\n"
				+ "				         WHEN CustomerDue = '1900-01-01 00:00:00.000' THEN TRY_CAST( null as varchar) \r\n"
				+ "				         WHEN CustomerDue is null or  \r\n"
				+ "				              CustomerDue = '' THEN  TRY_CAST( null as varchar)   \r\n"
				+ "				         ELSE CONVERT(varchar,CustomerDue, 103)    \r\n"
				+ "				     END AS CustomerDue,    \r\n"
				+ "				  TRY_CAST(RemainQuantity AS decimal(13, 3)) as RemainQuantity,  \r\n"
				+ "				   CASE  \r\n"
				+ "				         WHEN ShipDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				         WHEN ShipDate is null or   \r\n"
				+ "				              ShipDate = '' THEN null  \r\n"
				+ "				         ELSE ShipDate   \r\n"
				+ "				       END AS ShipDate,    \r\n"
//				+ "				  TRY_CAST(SaleStatus AS NVARCHAR(1)) as SaleStatus, \r\n"
				+ "				  CASE"
				+ "					WHEN SaleStatus IN ( 'Open order','Delivered' ) THEN 'O' "
				+ "					WHEN SaleStatus in ( 'Invoiced') THEN 'C'"
				+ "					WHEN SaleStatus in ( 'Canceled' ) THEN 'X' "
				+ "					ELSE NULL "
				+ "					END AS SaleStatus ,"
				+ "				  TRY_CAST(Currency AS NVARCHAR(50)) as Currency, \r\n"
				+ "				  TRY_CAST(Price AS decimal(13, 3)) as Price, \r\n"
				+ "				  TRY_CAST(OrderAmount AS decimal(13, 3)) as OrderAmount, \r\n"
				+ "				  TRY_CAST(RemainAmount  AS decimal(13, 3)) as RemainAmount,  \r\n"
				+ "				   CASE  \r\n"
				+ "				         WHEN SaleCreateDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				         WHEN SaleCreateDate is null or  \r\n"
				+ "				              SaleCreateDate = '' THEN null  \r\n"
				+ "				         ELSE TRY_CAST( SaleCreateDate AS DATETIME )    \r\n"
				+ "				       END AS SaleCreateDate,    \r\n"
				+ "				  TRY_CAST(SaleNumber AS NVARCHAR(50)) as SaleNumber, \r\n"
				+ "				  TRY_CAST(SaleFullName AS NVARCHAR(100)) as SaleFullName, \r\n"
				+ "				  CASE"
				+ "					WHEN DeliveryStatus IN ( 'Open order' ) THEN 'O' "
				+ "					WHEN DeliveryStatus in ( 'Delivered' , 'Invoiced') THEN 'C'"
				+ "					WHEN DeliveryStatus in ( 'Canceled' ) THEN 'X' "
				+ "					ELSE NULL "
				+ "					END AS DeliveryStatus,"
//				+ "					TRY_CAST(DeliveryStatus AS NVARCHAR(1)) as DeliveryStatus, \r\n"
				+ "				  TRY_CAST(DesignFG AS NVARCHAR(50)) as DesignFG, \r\n"
				+ "				  TRY_CAST(ArticleFG AS NVARCHAR(50)) as ArticleFG,   \r\n"
				+ "				  CASE  \r\n"
				+ "				         WHEN OrderSheetPrintDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				         WHEN OrderSheetPrintDate is null or  \r\n"
				+ "				              OrderSheetPrintDate = '' THEN null  \r\n"
				+ "				         ELSE OrderSheetPrintDate   \r\n"
				+ "				     END AS OrderSheetPrintDate,    \r\n"
				+ "				  TRY_CAST(CustomerMaterialBase  AS NVARCHAR(50)) as CustomerMaterialBase,  \r\n"
				+ "				  [SyncDate]  \r\n"
				+ "				  from FromErpMainSale  \r\n"
				+ ") \r\n"
				+ "				  \r\n"
				+ "SELECT A.[SaleOrder]\r\n"
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,B.*\r\n"
				+ "FROM ProductionOrders AS A \r\n"
				+ "LEFT JOIN MainSale AS B ON A.[SaleOrder] = B.SaleOrderCheck "

		;
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpMainSaleDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<SaleOrderLogDetail> getFromErpMainSaleDetailWithRangeOfChangeDate(String changeDateStart,
			String changeDateEnd, String saleOrder)
	{
		ArrayList<SaleOrderLogDetail> list = null;
		String where = " WHERE 1 = 1  ";
		if ( ! changeDateStart.equals("")) {
			where += " "
					+ " and (  "
					+ "	CAST(a.[SyncDate] AS DATE) >= convert(date,'"
					+ changeDateStart
					+ "', 103) AND \r\n"
					+ "	CAST(a.[SyncDate] AS DATE) <= convert(date,'"
					+ changeDateEnd
					+ "', 103) \r\n"
					+ "	) \r\n";
		}
		if ( ! saleOrder.equals("")) {
			where += " " + " and (  " + " a.[SaleOrder] = '" + saleOrder + "' \r\n" + "	) \r\n";
		}
		String sql = " "
				+ " SELECT  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[Division]\r\n"
				+ "      ,[MaterialNo]\r\n"
				+ "      ,[ArticleFG]\r\n"
				+ "      ,[DesignFG] \r\n"
				+ "      ,[Color]\r\n"
				+ "      ,[DistChannel]\r\n"
				+ "      ,[CustomerName]\r\n"
				+ "      ,[CustomerShortName]\r\n"
				+ "      ,[ColorCustomer] \r\n"
				+ "	  , CASE\r\n"
				+ "	      WHEN [SaleCreateDate] = '1900-01-01 00:00:00.000' THEN NULL\r\n"
				+ "	      WHEN [SaleCreateDate] IS NULL OR [SaleCreateDate] = '' THEN NULL\r\n"
				+ "	      ELSE TRY_CAST([SaleCreateDate] as DATE )\r\n"
				+ "	     END AS [SaleCreateDate] \r\n"
				+ "	  ,CASE\r\n"
				+ "	      WHEN [PlanGreigeDate] = '1900-01-01 00:00:00.000' THEN NULL\r\n"
				+ "	      WHEN [PlanGreigeDate] IS NULL OR [PlanGreigeDate] = '' THEN NULL\r\n"
				+ "	      ELSE TRY_CAST([PlanGreigeDate] as DATE )\r\n"
				+ "	     END AS [PlanGreigeDate] \r\n"
				+ "	  ,CASE\r\n"
				+ "	      WHEN [DueDate] = '1900-01-01 00:00:00.000' THEN NULL\r\n"
				+ "	      WHEN [DueDate] IS NULL OR [DueDate] = '' THEN NULL\r\n"
				+ "	      ELSE TRY_CAST([DueDate] as DATE )\r\n"
				+ "	     END AS [DueDate]\r\n"
				+ "	  ,CASE\r\n"
				+ "	      WHEN [CustomerDue] = '1900-01-01 00:00:00.000' THEN NULL\r\n"
				+ "	      WHEN [CustomerDue] IS NULL OR [DueDate] = '' THEN NULL\r\n"
				+ "	      ELSE CONVERT(varchar,CustomerDue, 103) \r\n"
				+ "	     END AS [CustomerDue]     \r\n"
				+ "      ,[SaleQuantity]\r\n"
				+ "      ,[SaleUnit]\r\n"
				+ "      ,[OrderAmount]\r\n"
				+ "      ,[RemainQuantity]\r\n"
				+ "      ,[RemainAmount]\r\n"
				+ "      ,[PurchaseOrder]\r\n"
				+ "      ,[CustomerNo]\r\n"
				+ "      ,[CustomerMaterial]\r\n"
				+ "      ,[SaleOrg]\r\n"
				+ "      ,[SaleStatus] \r\n"
				+ "      ,[SaleFullName]\r\n"
				+ "      ,[DeliveryStatus]\r\n"
				+ "      ,[SyncDate]\r\n"
				+ "      ,[SyncDateHeader]\r\n"
				+ "  FROM  [FromErpMainSale] a\r\n"
				+ where
				+ " Order by SyncDate desc";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genProductionOrderLogDetail(map));
			list.add(MapperUtility.mapToObject(map, SaleOrderLogDetail.class));
		}
		return list;
	}

	@Override
	public ArrayList<FromErpPackingDetail> getFromErpPackingDetail()
	{
		ArrayList<FromErpPackingDetail> list = new ArrayList<>();
		String sql = " "
				+ this.declareTimeFocus
				+ "WITH ProductionOrders AS ( \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM [FromErpMainProd] AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "				  \r\n"
				+ "	UNION \r\n"
				+ "				  \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM FromErpPacking AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "), \r\n"
				+ "Packing  AS (   \r\n"
				+ "	SELECT distinct    \r\n"
				+ "		TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrderCheck, \r\n"
				+ "		CASE  \r\n"
				+ "				WHEN PostingDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				WHEN PostingDate is null or   \r\n"
				+ "				    PostingDate = '' THEN null  \r\n"
				+ "				ELSE TRY_CAST( PostingDate AS DATETIME )   \r\n"
				+ "			END AS PostingDate,     \r\n"
				+ "		TRY_CAST(Quantity AS decimal(13, 3)) as Quantity, \r\n"
				+ "		TRY_CAST(RollNo AS NVARCHAR(10)) as RollNo, \r\n"
				+ "		TRY_CAST(QuantityKG AS decimal(13, 3)) as QuantityKG, \r\n"
				+ "		TRY_CAST(Grade AS NVARCHAR(10)) as Grade, \r\n"
				+ "		TRY_CAST(No AS NVARCHAR(10)) as No, \r\n"
				+ "		TRY_CAST(QuantityYD AS decimal(13, 3)) as QuantityYD , \r\n"
				+ "		[SyncDate]  \r\n"
				+ "		from FromErpPacking \r\n"
				+ ") \r\n"
				+ "				  \r\n"
				+ "SELECT A.[ProductionOrder]\r\n"
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,B.*\r\n"
				+ "FROM ProductionOrders AS A \r\n"
				+ "LEFT JOIN Packing AS B ON A.[ProductionOrder] = B.ProductionOrderCheck  \r\n"
				+ " WHERE "
				+ "      1 = 1 "
				+ "     AND a.ProductionOrder NOT LIKE '20%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'WO%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'Y2%' ; "
				+ " "
//				+ " declare  @oneHourAgo datetime = DATEADD(MINUTE, -30, GETDATE());"
//				+ " SELECT distinct   \r\n"
//				+ " TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrder,\r\n"
//				+ "  CASE \r\n"
//				+ "        WHEN PostingDate = '1900-01-01 00:00:00.000' THEN null\r\n"
//				+ "        WHEN PostingDate is null or  "
//				+ "             PostingDate = '' THEN null \r\n"
//				+ "        ELSE TRY_CAST( PostingDate AS DATETIME )  \r\n"
//				+ "      END AS PostingDate, \r\n"   
//				+ " TRY_CAST(Quantity AS decimal(13, 3)) as Quantity,\r\n"
//				+ " TRY_CAST(RollNo AS NVARCHAR(10)) as RollNo,\r\n"
//				+ " TRY_CAST(QuantityKG AS decimal(13, 3)) as QuantityKG,\r\n"
//				+ " TRY_CAST(Grade AS NVARCHAR(10)) as Grade,\r\n"
//				+ " TRY_CAST(No AS NVARCHAR(10)) as No,\r\n"
//				+ " TRY_CAST(QuantityYD AS decimal(13, 3)) as QuantityYD ,\r\n"
//				+ " [SyncDate]" 
//				+ " from FromErpPacking"

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
		String sql = " "
				+ this.declareTimeFocus
				+ "WITH ProductionOrders AS ( \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM [FromErpMainProd] AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "				  \r\n"
				+ "	UNION \r\n"
				+ "				  \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM FromErpPO AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "), \r\n"
				+ "	PO   AS (   \r\n"
				+ " 	SELECT distinct    \r\n"
				+ "	TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrderCheck, \r\n"
				+ "	TRY_CAST(RollNo AS NVARCHAR(10)) as RollNumber, \r\n"
				+ "	TRY_CAST(QuantityKG AS decimal(13, 3)) as RollWeight, \r\n"
				+ "	TRY_CAST(QuantityMR AS decimal(13, 3)) as RollLength,  \r\n"
				+ "	CASE  \r\n"
				+ "			WHEN POCreatedate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "			WHEN POCreatedate is null or   \r\n"
				+ "				POCreatedate = '' THEN null   \r\n"
				+ "			ELSE TRY_CAST( POCreatedate AS DATETIME )   \r\n"
				+ "		END AS POCreatedate,      \r\n"
				+ "	CASE  \r\n"
				+ "			WHEN RequiredDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "			WHEN RequiredDate is null or   \r\n"
				+ "				RequiredDate = '' THEN null   \r\n"
				+ "			ELSE TRY_CAST( RequiredDate AS DATETIME )   \r\n"
				+ "		END AS RequiredDate,     \r\n"
				+ "	TRY_CAST(PurchaseOrder AS NVARCHAR(50)) as PurchaseOrder, \r\n"
				+ "	TRY_CAST(PurchaseOrderLine AS NVARCHAR(5)) as PurchaseOrderLine, \r\n"
				+ "	TRY_CAST(PODefault AS NVARCHAR(15)) as PODefault, \r\n"
				+ "	TRY_CAST(POLineDefault AS NVARCHAR(5)) as POLineDefault,  \r\n"
				+ "	CASE  \r\n"
				+ "			WHEN POPostingDateDefault = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "			WHEN POPostingDateDefault is null or   \r\n"
				+ "				POPostingDateDefault = '' THEN null   \r\n"
				+ "			ELSE TRY_CAST( POPostingDateDefault AS DATETIME )   \r\n"
				+ "		END AS POPostingDateDefault,     \r\n"
				+ "	[SyncDate]  \r\n"
				+ "	from FromErpPO "
				+ " where [RollNo] <> '' and [RollNo] is not null\r\n"
				+ ") \r\n"
				+ "SELECT A.[ProductionOrder]\r\n"
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,B.*\r\n"
				+ "FROM ProductionOrders AS A \r\n"
				+ "LEFT JOIN PO AS B ON A.[ProductionOrder] = B.ProductionOrderCheck  \r\n"
				+ " WHERE "
				+ "      1 = 1 "
				+ "     AND a.ProductionOrder NOT LIKE '20%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'WO%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'Y2%' ; "

		;
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpPODetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<FromErpSaleDetail> getFromErpSaleDetail()
	{
		ArrayList<FromErpSaleDetail> list = new ArrayList<>();
		String sql = " "
				+ this.declareTimeFocus
				+ "WITH SaleOrderLines AS (\r\n"
				+ "    SELECT DISTINCT a.[SaleOrder] \r\n"
				+ "    FROM [FromErpMainSale] AS a\r\n"
				+ "    WHERE SyncDate >= @oneHourAgo\r\n"
				+ "	      OR [SyncDateHeader] >= @oneHourAgo\r\n"
				+ "\r\n"
				+ "    UNION\r\n"
				+ "\r\n"
				+ "    SELECT DISTINCT a.[SaleOrder] \r\n"
				+ "    FROM FromErpSale AS a\r\n"
				+ "    WHERE SyncDate >= @oneHourAgo\r\n"
				+ "),\r\n"
				+ "Sale AS (   \r\n"
				+ "		SELECT distinct    \r\n"
				+ "		TRY_CAST( [PRODUCTIONID] AS NVARCHAR(50)) as ProductionOrder,  \r\n"
				+ "			CASE  \r\n"
				+ "				WHEN BillDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				WHEN BillDate is null or   \r\n"
				+ "					BillDate = '' THEN null  \r\n"
				+ "				ELSE BillDate   \r\n"
				+ "				END AS BillDate,     \r\n"
				+ "		TRY_CAST(BillQtyPerSale AS decimal(13, 3)) as BillQtyPerSale, \r\n"
				+ "		TRY_CAST(SaleOrder AS NVARCHAR(50)) as SaleOrderCheck, \r\n"
				+ "		TRY_CAST(SaleLine AS NVARCHAR(50)) as SaleLine, \r\n"
				+ "		TRY_CAST(BillQtyPerStock AS decimal(13, 3)) as BillQtyPerStock, \r\n"
				+ "		TRY_CAST(Remark AS NVARCHAR(100)) as Remark, \r\n"
				+ " CASE \r\n"
				+ "		WHEN LEN([CustomerNo]) > 10 THEN [CustomerNo]\r\n"
				+ "		WHEN TRY_CAST([CustomerNo] AS INT ) IS NULL  THEN [CustomerNo]\r\n"
				// 20250506
//				+ "		ELSE [CustomerNo] \r\n"
				+ "		ELSE RIGHT('0000000000'+ISNULL([CustomerNo],''),10) \r\n"
				+ "	END as [CustomerNo] ,"
				+ "		TRY_CAST(CustomerName1 AS NVARCHAR(50)) as CustomerName1, \r\n"
				+ "		TRY_CAST(CustomErpO AS NVARCHAR(30)) as CustomErpO,  \r\n"
				+ "			CASE  \r\n"
				+ "				WHEN DueDate = '1900-01-01 00:00:00.000' THEN null \r\n"
				+ "				WHEN DueDate is null or   \r\n"
				+ "					DueDate = '' THEN null  \r\n"
				+ "				ELSE DueDate   \r\n"
				+ "				END AS DueDate,     \r\n"
				+ "		LTRIM(RTRIM(TRY_CAST(Color AS NVARCHAR(20)))) as Color,  \r\n"
				+ "			[SyncDate]  \r\n"
				+ "		from FromErpSale \r\n"
				+ ") \r\n"
				+ "				  \r\n"
				+ "SELECT A.[SaleOrder]\r\n"
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,B.*\r\n"
				+ "FROM SaleOrderLines AS A \r\n"
				+ "LEFT JOIN Sale AS B ON A.[SaleOrder] = B.SaleOrderCheck; \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpSaleDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<FromErpSubmitDateDetail> getFromErpSubmitDateDetail()
	{
		ArrayList<FromErpSubmitDateDetail> list = new ArrayList<>();
		String sql = " "
				+ this.declareTimeFocus
				+ "WITH ProductionOrders AS ( \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder] \r\n"
				+ "	FROM [FromErpMainProd] AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "				  \r\n"
				+ "	UNION \r\n"
				+ "				  \r\n"
				+ "	SELECT DISTINCT a.[ProductionOrder]   \r\n"
				+ "	FROM [FromErpSubmitDate] AS a \r\n"
				+ "	WHERE SyncDate >= @oneHourAgo \r\n"
				+ "), \r\n"
				+ "SubmitDate AS (    \r\n"
				+ "	SELECT distinct    \r\n"
				+ "	TRY_CAST( ProductionOrder AS NVARCHAR(50)) as ProductionOrderCheck  , \r\n"
				+ "	TRY_CAST( SaleOrder AS NVARCHAR(50)) as SaleOrder  , \r\n"
				+ "	TRY_CAST( SaleLine AS NVARCHAR(50)) as SaleLine  , \r\n"
				+ "	TRY_CAST( No AS NVARCHAR(2)) as No  ,  \r\n"
				+ "	CASE  \r\n"
				+ "		WHEN SubmitDate = '1900-01-01 00:00:00.000' THEN NULL  \r\n"
				+ "		WHEN SubmitDate is null or   \r\n"
				+ "			SubmitDate = '' THEN null  \r\n"
				+ "		ELSE SubmitDate  \r\n"
				+ "	END AS SubmitDate,   \r\n"
				+ "	TRY_CAST( Remark AS NVARCHAR(50)) as Remark,   \r\n"
				+ "	[SyncDate]  \r\n"
				+ "	from [FromErpSubmitDate]  \r\n"
				+ ") \r\n"
				+ "				  \r\n"
				+ "SELECT A.[ProductionOrder]\r\n"
				+ "		,CASE\r\n"
				+ "			WHEN B.[SyncDate] IS NULL THEN 'X'\r\n"
				+ "			ELSE 'O'\r\n"
				+ "			END AS DataStatus \r\n"
				+ "		,B.*\r\n"
				+ "FROM ProductionOrders AS A \r\n"
				+ "LEFT JOIN SubmitDate AS B ON A.[ProductionOrder] = B.ProductionOrderCheck"
				+ " WHERE "
				+ "      1 = 1 "
				+ "     AND a.ProductionOrder NOT LIKE '20%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'WO%' \r\n"
				+ "     AND a.ProductionOrder NOT LIKE 'Y2%' ; ";
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFromErpSubmitDateDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2Detail()
	{
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = new ArrayList<>();
		// หา distinct ReplyDate (ไม่รวม NULL) จาก records ที่ sync ใน 1 ชั่วโมงล่าสุด
		// แล้วดึงทุก record ที่: (1) ReplyDate อยู่ใน batch นั้น หรือ (2) sync ใน 1 ชั่วโมงและ ReplyDate = NULL
		String sql = " "
				+ this.declareTimeFocus
//				+ " DECLARE @oneHourAgo DATETIME = DATEADD(HOUR, -1, GETDATE());\r\n"
				+ "\r\n"
				+ "-- CTE หาเฉพาะ ProdId ที่มีการเปลี่ยนแปลง (Update/Insert) ใน 1 ชั่วโมงที่ผ่านมา\r\n"
//				+ " DECLARE @oneHourAgo DATETIME = DATEADD(HOUR, -1, GETDATE());\r\n"
				+ "\r\n"
				+ "-- CTE สำหรับหาเฉพาะ ProdId ที่มีการเคลื่อนไหวในช่วง 1 ชั่วโมงที่ผ่านมา\r\n"
				+ "WITH TargetProdId AS (\r\n"
				+ "    SELECT DISTINCT ProdId\r\n"
				+ "    FROM [ERP_PROD].[dbo].[Z_ATT_CustomerConfirm2]\r\n"
				+ "    WHERE SYNCSTARTDATETIME >= @oneHourAgo\r\n"
				+ "      -- เปลี่ยนเป็น NOT LIKE เพื่อให้รองรับการทำงานของ Index (Sargable)\r\n"
				+ "      AND ProdId NOT LIKE '20%'\r\n"
				+ "      AND ProdId NOT LIKE 'WO%'\r\n"
				+ "      AND ProdId NOT LIKE 'Y2%'\r\n"
				+ ")\r\n"
				+ "\r\n"
				+ "SELECT \r\n"
				+ "    CASE  \r\n"
				+ "        WHEN a.[SendDate] = '1900-01-01 00:00:00.000' THEN NULL  \r\n"
				+ "        WHEN a.[SendDate] IS NULL OR a.[SendDate] = '' THEN NULL  \r\n"
				+ "        ELSE TRY_CAST(a.[SendDate] AS DATE)  \r\n"
				+ "    END AS SendDate,   \r\n"
				+ "    TRY_CAST(a.[NoPerDay] AS INT) AS NoPerDay, \r\n"
				+ "    CASE  \r\n"
				+ "        WHEN a.[ReplyDate] = '1900-01-01 00:00:00.000' THEN NULL  \r\n"
				+ "        WHEN a.[ReplyDate] IS NULL OR a.[ReplyDate] = '' THEN NULL  \r\n"
				+ "        ELSE TRY_CAST(a.[ReplyDate] AS DATE)  \r\n"
				+ "    END AS ReplyDate,   \r\n"
				+ "    a.[CFMNo], \r\n"
				+ "    a.[Customer Name] AS CustomerName, \r\n"
				+ "    a.[SO], \r\n"
				+ "    TRY_CAST(a.[SOLineNo] AS NVARCHAR(50)) AS SOLine, \r\n"
				+ "    TRY_CAST(a.[DueDate] AS DATE) AS DueDate, \r\n"
				+ "    a.[PO], \r\n"
				+ "    a.[Material], \r\n"
				+ "    a.[ProductName], \r\n"
				+ "    a.[LabNo], \r\n"
				+ "    a.[Color], \r\n"
				+ "    a.[ProdId], \r\n"
				+ "    a.[LotNo], \r\n"
				+ "    TRY_CAST(a.[L] AS DECIMAL(13,3)) AS CFM_L, \r\n"
				+ "    TRY_CAST(a.[Da] AS DECIMAL(13,3)) AS CFM_Da, \r\n"
				+ "    TRY_CAST(a.[Db] AS DECIMAL(13,3)) AS CFM_Db, \r\n"
				+ "    TRY_CAST(a.[St] AS DECIMAL(13,3)) AS CFM_St, \r\n"
				+ "    TRY_CAST(a.[DE] AS DECIMAL(13,3)) AS CFM_DeltaE, \r\n"
				+ "    a.[QCComment], \r\n"
				+ "    CASE  \r\n"
				+ "        WHEN a.[ReplyDate] = '1900-01-01 00:00:00.000' THEN NULL  \r\n"
				+ "        WHEN a.[ReplyDate] IS NULL OR a.[ReplyDate] = '' THEN NULL  \r\n"
				+ "        ELSE a.Result  \r\n"
				+ "    END AS Result,   \r\n"
				+ "    a.[Remark from submit] AS RemarkFromSubmit, \r\n"
				+ "    a.[NextLot], \r\n"
				+ "    TRY_CAST(a.[Qty] AS DECIMAL(13,3)) AS Qty, \r\n"
				+ "    a.[UnitId], \r\n"
				+ "    'O' AS DataStatus,\r\n"
				+ "    a.[SYNCSTARTDATETIME]\r\n"
				+ "FROM [ERP_PROD].[dbo].[Z_ATT_CustomerConfirm2] a  \r\n"
				+ "INNER JOIN TargetProdId b \r\n"
				+ "    ON a.ProdId = b.ProdId\r\n"
				+ "WHERE a.ProdId NOT LIKE '20%'\r\n"
				+ "  AND a.ProdId NOT LIKE 'WO%'\r\n"
				+ "  AND a.ProdId NOT LIKE 'Y2%'\r\n"
//				+ "  -- กรองเอาเฉพาะ Row ของ ProdId นั้นๆ ที่เพิ่ง Sync หรือเป็นกลุ่มที่มีการ Reply แล้ว\r\n"
//				+ "  AND (a.SYNCSTARTDATETIME >= @oneHourAgo OR a.[ReplyDate] IS NOT NULL);\r\n"
				+ "  ;";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genZ_ATT_CustomerConfirm2Detail(map));
		}
		return list;
	}

}
