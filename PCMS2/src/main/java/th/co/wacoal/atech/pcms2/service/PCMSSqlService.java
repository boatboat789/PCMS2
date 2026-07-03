package th.co.wacoal.atech.pcms2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

@Service
public class PCMSSqlService {

	/**
	 * DROP statements for all temp tables used in read queries (SELECT INTO #temp).
	 * Used by SqlStatementHandler.queryList(database, dropAllTemp, sql) — drop BEFORE and AFTER.
	 * NOTE: Excludes PCMSSearch temp tables (#tempLotNoList, #tempUserStatusList,
	 *       #tempCustomerList, #tempCustomerShortList) because those are created
	 *       by PCMSSearchDaoImpl before the main query runs and must remain visible.
	 */
	public static final String dropAllTemp =
			"IF OBJECT_ID('tempdb..#BillBatchFlag')          IS NOT NULL DROP TABLE #BillBatchFlag;\r\n"
		  + "IF OBJECT_ID('tempdb..#tmpSumVolOP')            IS NOT NULL DROP TABLE #tmpSumVolOP;\r\n"
		  + "IF OBJECT_ID('tempdb..#tmpSumVolRP')            IS NOT NULL DROP TABLE #tmpSumVolRP;\r\n"
		  + "IF OBJECT_ID('tempdb..#tmpCRP')                 IS NOT NULL DROP TABLE #tmpCRP;\r\n"
		  + "IF OBJECT_ID('tempdb..#tmpSaleAgg')             IS NOT NULL DROP TABLE #tmpSaleAgg;\r\n"
		  + "IF OBJECT_ID('tempdb..#tmpProdAgg')             IS NOT NULL DROP TABLE #tmpProdAgg;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempMainSale')           IS NOT NULL DROP TABLE #tempMainSale;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempPlandeliveryDate')   IS NOT NULL DROP TABLE #tempPlandeliveryDate;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempPrepWaitLot')        IS NOT NULL DROP TABLE #tempPrepWaitLot;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempProdData')           IS NOT NULL DROP TABLE #tempProdData;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempUCAL')               IS NOT NULL DROP TABLE #tempUCAL;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempUSMSpecial1')        IS NOT NULL DROP TABLE #tempUSMSpecial1;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempUCALBroad')          IS NOT NULL DROP TABLE #tempUCALBroad;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempUSMSPE')             IS NOT NULL DROP TABLE #tempUSMSPE;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempUCALBest')           IS NOT NULL DROP TABLE #tempUCALBest;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempSumGRMain')          IS NOT NULL DROP TABLE #tempSumGRMain;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempSumGR')              IS NOT NULL DROP TABLE #tempSumGR;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempSumBill')            IS NOT NULL DROP TABLE #tempSumBill;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempSCC')                IS NOT NULL DROP TABLE #tempSCC;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempFromSORCFM')         IS NOT NULL DROP TABLE #tempFromSORCFM;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempProdWorkDate')       IS NOT NULL DROP TABLE #tempProdWorkDate;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempProdWorkDateFiltered') IS NOT NULL DROP TABLE #tempProdWorkDateFiltered;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempSPO')                IS NOT NULL DROP TABLE #tempSPO;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempSPOSale')            IS NOT NULL DROP TABLE #tempSPOSale;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempPlanCFMLabDate')     IS NOT NULL DROP TABLE #tempPlanCFMLabDate;\r\n"
		  + "IF OBJECT_ID('tempdb..#FlagHasRP')              IS NOT NULL DROP TABLE #FlagHasRP;\r\n"
		  + "IF OBJECT_ID('tempdb..#FlagHasOP')              IS NOT NULL DROP TABLE #FlagHasOP;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempMain')               IS NOT NULL DROP TABLE #tempMain;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempWaitLot')            IS NOT NULL DROP TABLE #tempWaitLot;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempOP')                 IS NOT NULL DROP TABLE #tempOP;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempOPSW')               IS NOT NULL DROP TABLE #tempOPSW;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempSW')                 IS NOT NULL DROP TABLE #tempSW;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempRP')                 IS NOT NULL DROP TABLE #tempRP;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempPrdOPA')             IS NOT NULL DROP TABLE #tempPrdOPA;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempPrdOP')              IS NOT NULL DROP TABLE #tempPrdOP;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempPrdOPSW')            IS NOT NULL DROP TABLE #tempPrdOPSW;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempPrdSW')              IS NOT NULL DROP TABLE #tempPrdSW;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempProdMain')           IS NOT NULL DROP TABLE #tempProdMain;\r\n"
		  + "IF OBJECT_ID('tempdb..#tempFinalResult')        IS NOT NULL DROP TABLE #tempFinalResult;\r\n";

	public static class UserStatusGroups {
		public List<String> lotNoList = new ArrayList<>();
		public List<String> userStatusCalRPList = new ArrayList<>();
		public List<String> userStatusCalList = new ArrayList<>();
		public List<String> userStatusListA = new ArrayList<>();
	}

	private UserStatusGroups categorizeUserStatus(List<String> statuses)
	{
		UserStatusGroups groups = new UserStatusGroups();
		for (String text : statuses) {
			String safeText = "'" + text.replace("'", "''") + "'";
			if (text.equals("รอจัด Lot") || text.equals("ขาย stock") || text.equals("รับจ้างถัก") || text.equals("Lot ขายแล้ว")
					|| text.equals("พ่วงแล้วรอสวม") || text.equals("รอสวมเคยมี Lot")) {
				groups.lotNoList.add(safeText);
			} else {
				groups.userStatusCalRPList.add(safeText);
				groups.userStatusCalList.add(safeText);
				groups.userStatusListA.add(safeText);
			}
		}
		return groups;
	}

	public String createTempBillBatchFlag = ""
			+ this.buildIfTempTableDrop("#BillBatchFlag")
			+ "SELECT SaleOrder, SaleLine\r\n"
			+ "INTO #BillBatchFlag\r\n"
			+ "FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			+ "WHERE DataStatus = 'O'\r\n"
			+ "GROUP BY SaleOrder, SaleLine;\r\n"
			+ "CREATE CLUSTERED INDEX IX_BBF ON #BillBatchFlag(SaleOrder, SaleLine);";

	public String createTempSumVolOP = ""
			+ this.buildIfTempTableDrop("#tmpSumVolOP") 
			+ "SELECT \r\n"
			+ "   a.ProductionOrder,\r\n"
			+ "   SUM(a.Volumn) AS SumVolOP\r\n"
			+ "INTO #tmpSumVolOP\r\n"
			+ "FROM [PCMS].[dbo].FromSapMainProdSale a\r\n"
			+ "INNER JOIN [PCMS].[dbo].[FromSapMainProd] b \r\n"
			+ "    ON a.ProductionOrder = b.ProductionOrder  \r\n"
			+ "INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] v\r\n"
			+ "    ON b.[UserStatus] = v.UserStatus \r\n"
			+ "   AND v.Special = 1  \r\n"
			+ "WHERE a.DataStatus = 'O'\r\n"
			+ "GROUP BY a.ProductionOrder;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpSumVolOP ON #tmpSumVolOP(ProductionOrder);";
	public String createTempSumVolRP = ""
			+ this.buildIfTempTableDrop("#tmpSumVolRP") 
			+ "SELECT \r\n"
			+ "   a.ProductionOrderRP,\r\n"
			+ "   SUM(CASE WHEN a.Volume = 0 THEN b.Volumn ELSE a.Volume END) AS SumVolRP\r\n"
			+ "INTO #tmpSumVolRP\r\n"
			+ "FROM [PCMS].[dbo].[ReplacedProdOrder] a  \r\n"
			+ "INNER JOIN [PCMS].[dbo].[FromSapMainProd] b \r\n"
			+ "    ON a.ProductionOrderRP = b.ProductionOrder  \r\n"
			+ "INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] v\r\n"
			+ "    ON b.[UserStatus] = v.UserStatus \r\n"
			+ "   AND v.Special = 1  \r\n"
			+ "WHERE a.DataStatus = 'O'\r\n"
			+ "GROUP BY a.ProductionOrderRP;\r\n"
			+ "\r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpSumVolRP ON #tmpSumVolRP(ProductionOrderRP);";
	public String createTempCRP = ""
			+ this.buildIfTempTableDrop("#tmpCRP")  
			+ "SELECT a.SaleOrder, a.SaleLine , 1 as countProdRP\r\n"
			+ "INTO #tmpCRP\r\n"
			+ "FROM [PCMS].[dbo].[ReplacedProdOrder] a\r\n"
			+ "INNER JOIN [PCMS].[dbo].[FromSapMainProd] b \r\n"
			+ "    ON a.ProductionOrderRP = b.ProductionOrder \r\n"
			+ "INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] v\r\n"
			+ "    ON b.[UserStatus] = v.UserStatus \r\n"
			+ "   AND v.Special = 1  \r\n"
			+ "WHERE a.DataStatus = 'O'\r\n"
			+ "GROUP BY a.SaleOrder, a.SaleLine; \r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpCRP ON #tmpCRP(SaleOrder, SaleLine);";
	public String createTempSaleAgg = ""
			+ this.buildIfTempTableDrop("#tmpSaleAgg")  
			+ "SELECT \r\n"
			+ "    SaleOrder,\r\n"
			+ "    SaleLine,\r\n"
			+ "    SUM(COALESCE(t.SumVolOP,0)) AS SumVolOP,\r\n"
			+ "    SUM(COALESCE(s.SumVolRP,0)) AS SumVolRP,\r\n"
			+ "    SUM(CASE WHEN p.DataStatus='O' THEN p.Volumn ELSE 0 END) AS SumVolMain\r\n"
			+ "INTO #tmpSaleAgg\r\n"
			+ "FROM [PCMS].[dbo].[FromSapMainProd] p\r\n"
			+ "LEFT JOIN #tmpSumVolOP t ON p.ProductionOrder = t.ProductionOrder\r\n"
			+ "LEFT JOIN #tmpSumVolRP s ON p.ProductionOrder = s.ProductionOrderRP\r\n"
			+ "GROUP BY SaleOrder, SaleLine;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpSaleAgg \r\n"
			+ "ON #tmpSaleAgg(SaleOrder, SaleLine);\n";
	public String createTempProdAgg = ""
			+ this.buildIfTempTableDrop("#tmpProdAgg")  
			+ "SELECT \r\n"
			+ "    p.[ProductionOrder],\r\n"
			+ "    SUM(COALESCE(t.SumVolOP,0)) AS SumVolOP,\r\n"
			+ "    SUM(COALESCE(s.SumVolRP,0)) AS SumVolRP,\r\n"
			+ "    SUM(CASE WHEN p.DataStatus='O' THEN p.Volumn ELSE 0 END) AS SumVolMain\r\n"
			+ "INTO #tmpProdAgg\r\n"
			+ "FROM [PCMS].[dbo].[FromSapMainProd] p\r\n"
			+ "LEFT JOIN #tmpSumVolOP t ON p.ProductionOrder = t.ProductionOrder\r\n"
			+ "LEFT JOIN #tmpSumVolRP s ON p.ProductionOrder = s.ProductionOrderRP\r\n"
			+ "GROUP BY p.ProductionOrder;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpProdAgg \r\n"
			+ "ON #tmpProdAgg(ProductionOrder);\n";
	public String createTempForMainAndWaitLot = ""
			+ this.createTempBillBatchFlag
			+ this.createTempSumVolOP
			+ this.createTempSumVolRP
			+ this.createTempCRP
			+ this.createTempSaleAgg
			+ this.createTempProdAgg;

	// เวอร์ชัน filter ด้วย #tempMainSale — ใช้เฉพาะที่สร้าง #tempMainSale ไว้ก่อนแล้ว
	// (getPCMSSumaryDetail, searchByDetail) ลด I/O ลงอย่างมากเมื่อ user search ด้วย criteria
	public String createTempForMainAndWaitLotFiltered = ""
			+ this.buildIfTempTableDrop("#BillBatchFlag")
			+ "SELECT a.SaleOrder, a.SaleLine\r\n"
			+ "INTO #BillBatchFlag\r\n"
			+ "FROM [PCMS].[dbo].[FromSapMainBillBatch] a\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = a.SaleOrder AND ms.SaleLine = a.SaleLine\r\n"
			+ "WHERE a.DataStatus = 'O'\r\n"
			+ "GROUP BY a.SaleOrder, a.SaleLine;\r\n"
			+ "CREATE CLUSTERED INDEX IX_BBF ON #BillBatchFlag(SaleOrder, SaleLine);\r\n"

			+ this.buildIfTempTableDrop("#tmpSumVolOP")
			+ "SELECT a.ProductionOrder, SUM(a.Volumn) AS SumVolOP\r\n"
			+ "INTO #tmpSumVolOP\r\n"
			+ "FROM [PCMS].[dbo].[FromSapMainProdSale] a\r\n"
			+ "INNER JOIN [PCMS].[dbo].[FromSapMainProd] b ON a.ProductionOrder = b.ProductionOrder\r\n"
			+ "INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] v\r\n"
			+ "    ON b.[UserStatus] = v.UserStatus AND v.Special = 1\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = a.SaleOrder AND ms.SaleLine = a.SaleLine\r\n"
			+ "WHERE a.DataStatus = 'O'\r\n"
			+ "GROUP BY a.ProductionOrder;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpSumVolOP ON #tmpSumVolOP(ProductionOrder);\r\n"

			+ this.buildIfTempTableDrop("#tmpSumVolRP")
			+ "SELECT a.ProductionOrderRP,\r\n"
			+ "    SUM(CASE WHEN a.Volume = 0 THEN b.Volumn ELSE a.Volume END) AS SumVolRP\r\n"
			+ "INTO #tmpSumVolRP\r\n"
			+ "FROM [PCMS].[dbo].[ReplacedProdOrder] a\r\n"
			+ "INNER JOIN [PCMS].[dbo].[FromSapMainProd] b ON a.ProductionOrderRP = b.ProductionOrder\r\n"
			+ "INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] v\r\n"
			+ "    ON b.[UserStatus] = v.UserStatus AND v.Special = 1\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = a.SaleOrder AND ms.SaleLine = a.SaleLine\r\n"
			+ "WHERE a.DataStatus = 'O'\r\n"
			+ "GROUP BY a.ProductionOrderRP;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpSumVolRP ON #tmpSumVolRP(ProductionOrderRP);\r\n"

			+ this.buildIfTempTableDrop("#tmpCRP")
			+ "SELECT a.SaleOrder, a.SaleLine, 1 AS countProdRP\r\n"
			+ "INTO #tmpCRP\r\n"
			+ "FROM [PCMS].[dbo].[ReplacedProdOrder] a\r\n"
			+ "INNER JOIN [PCMS].[dbo].[FromSapMainProd] b ON a.ProductionOrderRP = b.ProductionOrder\r\n"
			+ "INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] v\r\n"
			+ "    ON b.[UserStatus] = v.UserStatus AND v.Special = 1\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = a.SaleOrder AND ms.SaleLine = a.SaleLine\r\n"
			+ "WHERE a.DataStatus = 'O'\r\n"
			+ "GROUP BY a.SaleOrder, a.SaleLine;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpCRP ON #tmpCRP(SaleOrder, SaleLine);\r\n"

			+ this.buildIfTempTableDrop("#tmpSaleAgg")
			+ "SELECT p.SaleOrder, p.SaleLine,\r\n"
			+ "    SUM(COALESCE(t.SumVolOP,0)) AS SumVolOP,\r\n"
			+ "    SUM(COALESCE(s.SumVolRP,0)) AS SumVolRP,\r\n"
			+ "    SUM(CASE WHEN p.DataStatus='O' THEN p.Volumn ELSE 0 END) AS SumVolMain\r\n"
			+ "INTO #tmpSaleAgg\r\n"
			+ "FROM [PCMS].[dbo].[FromSapMainProd] p\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = p.SaleOrder AND ms.SaleLine = p.SaleLine\r\n"
			+ "LEFT JOIN #tmpSumVolOP t ON p.ProductionOrder = t.ProductionOrder\r\n"
			+ "LEFT JOIN #tmpSumVolRP s ON p.ProductionOrder = s.ProductionOrderRP\r\n"
			+ "GROUP BY p.SaleOrder, p.SaleLine;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpSaleAgg ON #tmpSaleAgg(SaleOrder, SaleLine);\r\n"

			+ this.buildIfTempTableDrop("#tmpProdAgg")
			+ "SELECT p.[ProductionOrder],\r\n"
			+ "    SUM(COALESCE(t.SumVolOP,0)) AS SumVolOP,\r\n"
			+ "    SUM(COALESCE(s.SumVolRP,0)) AS SumVolRP,\r\n"
			+ "    SUM(CASE WHEN p.DataStatus='O' THEN p.Volumn ELSE 0 END) AS SumVolMain\r\n"
			+ "INTO #tmpProdAgg\r\n"
			+ "FROM [PCMS].[dbo].[FromSapMainProd] p\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = p.SaleOrder AND ms.SaleLine = p.SaleLine\r\n"
			+ "LEFT JOIN #tmpSumVolOP t ON p.ProductionOrder = t.ProductionOrder\r\n"
			+ "LEFT JOIN #tmpSumVolRP s ON p.ProductionOrder = s.ProductionOrderRP\r\n"
			+ "GROUP BY p.ProductionOrder;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tmpProdAgg ON #tmpProdAgg(ProductionOrder);\r\n";

	public String createDropTempForMainAndWaitLot = ""
			+ this.buildIfTempTableDrop("#BillBatchFlag")
			+ this.buildIfTempTableDrop("#tmpSumVolOP")
			+ this.buildIfTempTableDrop("#tmpSumVolRP")
			+ this.buildIfTempTableDrop("#tmpCRP")
			+ this.buildIfTempTableDrop("#tmpSaleAgg")
			+ this.buildIfTempTableDrop("#tmpProdAgg") ;

	public String leftJoinBPartOneT_A = ""
			+ " LEFT JOIN #tmpSumVolOP t ON b.ProductionOrder = t.ProductionOrder\r\n";
	public String leftJoinBPartOneS_A = ""
			+ " LEFT JOIN #tmpSumVolRP s ON b.ProductionOrder = s.ProductionOrderRP\r\n";
	public String withProdData = ""
//			+ this.createTempBillBatchFlag
//			+ this.createTempSumVolOP
//			+ this.createTempSumVolRP
//			+ this.createTempCRP
			+ "; WITH ProdData AS (\r\n"
			+ "    SELECT \r\n"
			+ "         \r\n"
			+ "        a.[SaleOrder]\r\n"
			+ "        ,a.[Saleline]\r\n"
			+ "        ,a.[DesignFG]\r\n"
			+ "        ,a.[ArticleFG]\r\n"
			+ "		,a.[Price]\r\n"
			+ "		,a.[Division]\r\n"
			+ "		,a.[DistChannel] \r\n"
			+ "		,a.CustomerName \r\n"
			+ "		,a.CustomerShortName \r\n"
			+ "		,a.SaleCreateDate \r\n"
			+ "		,a.PurchaseOrder \r\n"
			+ "		,a.[SaleStatus]\r\n"
			+ "		,a.OrderAmount\r\n"
			+ "		,a.SaleQuantity \r\n"
			+ "		,a.RemainQuantity\r\n"
			+ "		,a.RemainAmount\r\n"
			+ "		,a.MaterialNo\r\n"
			+ "		,a.CustomerMaterial\r\n"
			+ "		,a.CustomerMaterialBase\r\n"
			+ "		,a.SaleUnit\r\n"
			+ "		,a.CustomerDue\r\n"
			+ "		,a.DueDate\r\n"
			+ "		,a.ShipDate\r\n"
			+ "		,a.SaleNumber\r\n"
			+ "		,a.SaleFullName\r\n"
			+ "		,a.Color\r\n"
			+ "		,a.ColorCustomer\r\n"
			+ "		,a.DeliveryStatus\r\n"
			+ "			,calc.ProdOrderValue AS [ProductionOrder] \r\n"
			+ "			,calc.LotNoValue     AS [LotNo] "
			+ "		, b.[Unit]\r\n"
			+ "		, b.[RemAfterCloseOne]\r\n"
			+ "		, b.[RemAfterCloseTwo]\r\n"
			+ "		, b.[RemAfterCloseThree]\r\n"
			+ "		, b.[LabStatus] \r\n"
			+ "		, b.[BookNo]\r\n"
			+ "		, b.[Center] \r\n"
			+ "		, b.[Batch]\r\n"
			+ "		, b.[LabNo]\r\n"
			+ "		, b.[RemarkOne]\r\n"
			+ "		, b.[RemarkTwo]\r\n"
			+ "		, b.[RemarkThree]\r\n"
			+ "		, b.[BCAware]\r\n"
			+ "		, b.[OrderPuang]\r\n"
			+ "		, b.[RefPrd]\r\n"
			+ "		, b.[GreigeInDate]\r\n"
			+ "		, b.[BCDate]\r\n"
			+ "		, b.[Volumn]\r\n"
			+ "		, b.[CFdate]\r\n"
			+ "		, b.[CFType]\r\n"
			+ "		, b.[Shade]\r\n"
			+ "		, b.[PrdCreateDate]\r\n"
			+ "		, b.[GreigeArticle]\r\n"
			+ "		, b.[GreigeDesign]\r\n"
			+ "		, b.[GreigeMR]\r\n"
			+ "		, b.[GreigeKG]\r\n"
			+ "		, b.[BillSendQuantity]\r\n"
			+ "		, b.TotalQuantity\r\n"
			+ "		,CASE \r\n"
			+ "		  WHEN s.SumVolRP IS NOT NULL AND t.SumVolOP IS NOT NULL THEN b.Volumn - s.SumVolRP - t.SumVolOP\r\n"
			+ "		  WHEN s.SumVolRP IS NOT NULL THEN b.Volumn - s.SumVolRP\r\n"
			+ "		  WHEN t.SumVolOP IS NOT NULL THEN b.Volumn - t.SumVolOP\r\n"
			+ "		  WHEN b.Volumn IS NOT NULL THEN b.Volumn\r\n"
			+ "		  ELSE 0\r\n"
			+ "		END adjVol\r\n"
			+ "    FROM #tempMainSale a\r\n"
			+ "    LEFT JOIN [PCMS].[dbo].[FromSapMainProd] b ON a.SaleOrder = b.SaleOrder AND a.SaleLine = b.SaleLine\r\n"
			+ "	   LEFT JOIN #BillBatchFlag bb ON bb.SaleOrder = a.SaleOrder\r\n"
			+ "								 AND bb.SaleLine  = a.SaleLine\r\n"
			+ this.leftJoinBPartOneT_A
			+ this.leftJoinBPartOneS_A
			+ "    CROSS APPLY (\r\n"
			+ "   	 SELECT \r\n"
			+ "        CASE \r\n"
			+ "            WHEN b.[ProductionOrder] IS NOT NULL THEN b.[ProductionOrder]\r\n"
			+ "            WHEN a.MaterialNo LIKE 'V%' THEN N'รับจ้างถัก'\r\n"
			+ "            WHEN bb.SaleOrder IS NOT NULL THEN N'Lot ขายแล้ว'\r\n"
			+ "            WHEN a.[SaleStatus] = 'C' THEN N'ขาย stock'\r\n"
			+ "            ELSE N'รอจัด Lot'\r\n"
			+ "        END AS ProdOrderValue,\r\n"
			+ "        CASE \r\n"
			+ "            WHEN b.[ProductionOrder] IS NOT NULL THEN b.[LotNo]\r\n"
			+ "            WHEN a.MaterialNo LIKE 'V%' THEN N'รับจ้างถัก'\r\n"
			+ "            WHEN bb.SaleOrder IS NOT NULL THEN N'Lot ขายแล้ว'\r\n"
			+ "            WHEN a.[SaleStatus] = 'C' THEN N'ขาย stock'\r\n"
			+ "            ELSE N'รอจัด Lot'\r\n"
			+ "        END AS LotNoValue\r\n"
			+ ") AS calc \r\n"
			+ ") \r\n";

	// Materialized version of ProdData CTE — SELECT INTO #tempProdData with indexes.
	// ใช้แทน WITH ProdData AS (...) ใน searchByDetail เพื่อหลีกเลี่ยง inline join กับ FromSapMainProd
	// ต้องรัน AFTER createTempForMainAndWaitLotFiltered (ต้องการ #BillBatchFlag, #tmpSumVolOP, #tmpSumVolRP)
	public String createTempProdData = ""
			+ this.buildIfTempTableDrop("#tempProdData")
			+ "SELECT\r\n"
			+ "    a.[SaleOrder]\r\n"
			+ "   ,a.[Saleline]\r\n"
			+ "   ,a.[DesignFG]\r\n"
			+ "   ,a.[ArticleFG]\r\n"
			+ "   ,a.[Price]\r\n"
			+ "   ,a.[Division]\r\n"
			+ "   ,a.[DistChannel]\r\n"
			+ "   ,a.CustomerName\r\n"
			+ "   ,a.CustomerShortName\r\n"
			+ "   ,a.SaleCreateDate\r\n"
			+ "   ,a.PurchaseOrder\r\n"
			+ "   ,a.[SaleStatus]\r\n"
			+ "   ,a.OrderAmount\r\n"
			+ "   ,a.SaleQuantity\r\n"
			+ "   ,a.RemainQuantity\r\n"
			+ "   ,a.RemainAmount\r\n"
			+ "   ,a.MaterialNo\r\n"
			+ "   ,a.CustomerMaterial\r\n"
			+ "   ,a.CustomerMaterialBase\r\n"
			+ "   ,a.SaleUnit\r\n"
			+ "   ,a.CustomerDue\r\n"
			+ "   ,a.DueDate\r\n"
			+ "   ,a.ShipDate\r\n"
			+ "   ,a.SaleNumber\r\n"
			+ "   ,a.SaleFullName\r\n"
			+ "   ,a.Color\r\n"
			+ "   ,a.ColorCustomer\r\n"
			+ "   ,a.DeliveryStatus\r\n"
			+ "   ,calc.ProdOrderValue AS [ProductionOrder]\r\n"
			+ "   ,calc.LotNoValue     AS [LotNo]\r\n"
			+ "   ,b.[Unit]\r\n"
			+ "   ,b.[RemAfterCloseOne]\r\n"
			+ "   ,b.[RemAfterCloseTwo]\r\n"
			+ "   ,b.[RemAfterCloseThree]\r\n"
			+ "   ,b.[LabStatus]\r\n"
			+ "   ,b.[BookNo]\r\n"
			+ "   ,b.[Center]\r\n"
			+ "   ,b.[Batch]\r\n"
			+ "   ,b.[LabNo]\r\n"
			+ "   ,b.[RemarkOne]\r\n"
			+ "   ,b.[RemarkTwo]\r\n"
			+ "   ,b.[RemarkThree]\r\n"
			+ "   ,b.[BCAware]\r\n"
			+ "   ,b.[OrderPuang]\r\n"
			+ "   ,b.[RefPrd]\r\n"
			+ "   ,b.[GreigeInDate]\r\n"
			+ "   ,b.[BCDate]\r\n"
			+ "   ,b.[Volumn]\r\n"
			+ "   ,b.[CFdate]\r\n"
			+ "   ,b.[CFType]\r\n"
			+ "   ,b.[Shade]\r\n"
			+ "   ,b.[PrdCreateDate]\r\n"
			+ "   ,b.[GreigeArticle]\r\n"
			+ "   ,b.[GreigeDesign]\r\n"
			+ "   ,b.[GreigeMR]\r\n"
			+ "   ,b.[GreigeKG]\r\n"
			+ "   ,b.[BillSendQuantity]\r\n"
			+ "   ,b.TotalQuantity\r\n"
			+ "   ,CASE\r\n"
			+ "      WHEN s.SumVolRP IS NOT NULL AND t.SumVolOP IS NOT NULL THEN b.Volumn - s.SumVolRP - t.SumVolOP\r\n"
			+ "      WHEN s.SumVolRP IS NOT NULL THEN b.Volumn - s.SumVolRP\r\n"
			+ "      WHEN t.SumVolOP IS NOT NULL THEN b.Volumn - t.SumVolOP\r\n"
			+ "      WHEN b.Volumn IS NOT NULL THEN b.Volumn\r\n"
			+ "      ELSE 0\r\n"
			+ "    END AS adjVol\r\n"
			+ "INTO #tempProdData\r\n"
			+ "FROM #tempMainSale a\r\n"
			+ "LEFT JOIN [PCMS].[dbo].[FromSapMainProd] b ON a.SaleOrder = b.SaleOrder AND a.SaleLine = b.SaleLine\r\n"
			+ "LEFT JOIN #BillBatchFlag bb ON bb.SaleOrder = a.SaleOrder AND bb.SaleLine = a.SaleLine\r\n"
			+ this.leftJoinBPartOneT_A
			+ this.leftJoinBPartOneS_A
			+ "CROSS APPLY (\r\n"
			+ "    SELECT\r\n"
			+ "        CASE\r\n"
			+ "            WHEN b.[ProductionOrder] IS NOT NULL THEN b.[ProductionOrder]\r\n"
			+ "            WHEN a.MaterialNo LIKE 'V%' THEN N'รับจ้างถัก'\r\n"
			+ "            WHEN bb.SaleOrder IS NOT NULL THEN N'Lot ขายแล้ว'\r\n"
			+ "            WHEN a.[SaleStatus] = 'C' THEN N'ขาย stock'\r\n"
			+ "            ELSE N'รอจัด Lot'\r\n"
			+ "        END AS ProdOrderValue,\r\n"
			+ "        CASE\r\n"
			+ "            WHEN b.[ProductionOrder] IS NOT NULL THEN b.[LotNo]\r\n"
			+ "            WHEN a.MaterialNo LIKE 'V%' THEN N'รับจ้างถัก'\r\n"
			+ "            WHEN bb.SaleOrder IS NOT NULL THEN N'Lot ขายแล้ว'\r\n"
			+ "            WHEN a.[SaleStatus] = 'C' THEN N'ขาย stock'\r\n"
			+ "            ELSE N'รอจัด Lot'\r\n"
			+ "        END AS LotNoValue\r\n"
			+ ") AS calc;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempProdData_PO ON #tempProdData(ProductionOrder);\r\n"
			+ "CREATE NONCLUSTERED INDEX IX_tempProdData_SOSL ON #tempProdData(SaleOrder, SaleLine);\r\n";

//	public String crossApplyVolCalc = ""
//			+ "\r\n"
//			+ "	CROSS APPLY (\r\n"
//			+ "		SELECT \r\n"
//			+ "			CASE \r\n"
//			+ "				WHEN s.SumVolRP IS NOT NULL AND t.SumVolOP IS NOT NULL THEN a.Volumn - s.SumVolRP - t.SumVolOP\r\n"
//			+ "				WHEN s.SumVolRP IS NOT NULL AND t.SumVolOP IS NULL THEN a.Volumn - s.SumVolRP\r\n"
//			+ "				WHEN s.SumVolRP IS NULL AND t.SumVolOP IS NOT NULL THEN a.Volumn - t.SumVolOP\r\n"
//			+ "				WHEN a.Volumn IS NOT NULL THEN a.Volumn\r\n"
//			+ "				ELSE 0\r\n"
//			+ "			END AS adjVol\r\n"
//			+ "	) AS volCalc ";

	// Pre-filter TEMP_UserStatusAuto ให้เหลือแค่ ProductionOrder ที่ใช้จริงใน batch นี้
	// → #tempUCAL  (จาก millions row → หลักร้อย/พัน row)
	// ต้องรัน AFTER createTempProdData (#tempProdData ต้องมีแล้ว)
	public String createTempUCAL = ""
			+ this.buildIfTempTableDrop("#tempUCAL")
			+ "SELECT DISTINCT UCAL.ProductionOrder, UCAL.UserStatusCal, UCAL.Grade\r\n"
			+ "INTO #tempUCAL\r\n"
			+ "FROM [PCMS].[dbo].[TEMP_UserStatusAuto] AS UCAL\r\n"
			+ "INNER JOIN (\r\n"
			+ "    SELECT DISTINCT ProductionOrder FROM #tempProdData\r\n"
			+ ") AS pd ON pd.ProductionOrder = UCAL.ProductionOrder\r\n"
			+ "WHERE UCAL.DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempUCAL ON #tempUCAL(ProductionOrder, Grade);\r\n";

	// Pre-build viewUserStatusMappingPCMS (Special=1) → #tempUSMSpecial1
	// ใช้แทน INNER JOIN view ใน typed tables (OP, RP, OPSW, SW)
	// ต้องสร้างใน buildSqlCommonTables ก่อน buildSqlTypedTables รัน
	public String createTempUSMSpecial1 = ""
			+ this.buildIfTempTableDrop("#tempUSMSpecial1")
			+ "SELECT UserStatus\r\n"
			+ "INTO #tempUSMSpecial1\r\n"
			+ "FROM [PCMS].[dbo].[viewUserStatusMappingPCMS]\r\n"
			+ "WHERE Special = 1;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempUSMSpecial1 ON #tempUSMSpecial1(UserStatus);\r\n";

	// Pre-build TEMP_UserStatusAuto (DataStatus='O') → #tempUCALBroad
	// ใช้แทน LEFT JOIN TEMP_UserStatusAuto ใน typed tables (OP, RP, OPSW, SW)
	// รวม UserStatusCal (ใช้ใน OP/OPSW/SW) และ UserStatusCalRP (ใช้ใน RP)
	// ต้องสร้างใน buildSqlCommonTables ก่อน buildSqlTypedTables รัน
	public String createTempUCALBroad = ""
			+ this.buildIfTempTableDrop("#tempUCALBroad")
			+ "SELECT ProductionOrder, UserStatusCal, UserStatusCalRP, Grade\r\n"
			+ "INTO #tempUCALBroad\r\n"
			+ "FROM [PCMS].[dbo].[TEMP_UserStatusAuto]\r\n"
			+ "WHERE DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempUCALBroad ON #tempUCALBroad(ProductionOrder, Grade);\r\n";

	// Pre-build viewUserStatusMappingPCMS (Special=0) เป็น temp table
	// → #tempUSMSPE  (แทนการ expand view inline ทุกครั้ง)
	public String createTempUSMSPE = ""
			+ this.buildIfTempTableDrop("#tempUSMSPE")
			+ "SELECT UserStatus, Special\r\n"
			+ "INTO #tempUSMSPE\r\n"
			+ "FROM [PCMS].[dbo].[viewUserStatusMappingPCMS]\r\n"
			+ "WHERE Special = 0;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempUSMSPE ON #tempUSMSPE(UserStatus);\r\n";

	// Pre-resolve best UCAL per (ProductionOrder, Grade) — replaces OUTER APPLY correlated subquery
	// Requires: #tempProdData, #tempSumGR, #tempUCAL, #tempUSMSPE  (run in that order before this)
	//
	// v3 — explicit NULL-safe join condition แทน (_uc.Grade = sg.Grade OR _uc.Grade IS NULL)
	//       ที่มี ambiguity เมื่อ sg.Grade IS NULL  (_uc.Grade = NULL = UNKNOWN ทำให้ evaluate ผิด)
	//   InputGrade IS NOT NULL → จับทั้ง grade-specific (_uc.Grade = InputGrade) และ catch-all (_uc.Grade IS NULL)
	//   InputGrade IS NULL     → จับเฉพาะ catch-all (_uc.Grade IS NULL) เท่านั้น
	//
	// Why faster than OUTER APPLY:
	//   OUTER APPLY TOP 1 fires once per every row in (#tempProdData × #tempSumGR) ≈ N×G times.
	//   This CTE computes once per DISTINCT (PO, InputGrade) pair — far fewer executions.
	public String createTempUCALBest = ""
			+ this.buildIfTempTableDrop("#tempUCALBest")
			+ ";WITH _pairs AS (\r\n"
			+ "    SELECT DISTINCT pd.ProductionOrder, sg.Grade AS InputGrade\r\n"
			+ "    FROM (\r\n"
			+ "        SELECT DISTINCT ProductionOrder FROM #tempProdData WHERE ProductionOrder IS NOT NULL\r\n"
			+ "    ) AS pd\r\n"
			+ "    LEFT JOIN #tempSumGR AS sg ON sg.ProductionOrder = pd.ProductionOrder\r\n"
			+ "),\r\n"
			+ "_ranked AS (\r\n"
			+ "    SELECT\r\n"
			+ "        p.ProductionOrder,\r\n"
			+ "        p.InputGrade,\r\n"
			+ "        _uc.UserStatusCal,\r\n"
			+ "        ROW_NUMBER() OVER (\r\n"
			+ "            PARTITION BY p.ProductionOrder, p.InputGrade\r\n"
			+ "            ORDER BY\r\n"
			+ "                CASE WHEN _spe.UserStatus IS NOT NULL THEN 0 ELSE 1 END,\r\n"
			+ "                CASE WHEN _uc.Grade     IS NOT NULL THEN 0 ELSE 1 END\r\n"
			+ "        ) AS rn\r\n"
			+ "    FROM _pairs AS p\r\n"
			+ "    INNER JOIN #tempUCAL AS _uc ON _uc.ProductionOrder = p.ProductionOrder\r\n"
			+ "        AND (\r\n"
			+ "            (p.InputGrade IS NOT NULL AND (_uc.Grade = p.InputGrade OR _uc.Grade IS NULL))\r\n"
			+ "            OR\r\n"
			+ "            (p.InputGrade IS NULL     AND  _uc.Grade IS NULL)\r\n"
			+ "        )\r\n"
			+ "    LEFT JOIN #tempUSMSPE AS _spe ON _spe.UserStatus = _uc.UserStatusCal\r\n"
			+ ")\r\n"
			+ "SELECT ProductionOrder, InputGrade AS Grade, UserStatusCal\r\n"
			+ "INTO #tempUCALBest FROM _ranked WHERE rn = 1;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempUCALBest ON #tempUCALBest(ProductionOrder, Grade);\r\n";

	// JOIN to #tempUCALBest — handles both non-NULL and NULL grades
	// NULL=NULL is UNKNOWN in SQL so need explicit IS NULL check for the no-GR-data case
	public String getLeftJoinTempUCALBest(String aliasProd, String aliasGrade) {
		return String.format(
				" LEFT JOIN #tempUCALBest AS UCAL\r\n"
				+ "     ON UCAL.ProductionOrder = %s.ProductionOrder\r\n"
				+ "    AND (UCAL.Grade = %s.Grade OR (UCAL.Grade IS NULL AND %s.Grade IS NULL))\r\n",
				aliasProd, aliasGrade, aliasGrade);
	}

	// INNER JOIN #tempUSMSpecial1 — แทน buildInnerJoinViewUSM_SPE(..., 1, ...) ใน typed tables
	public String buildInnerJoinTempUSMSpecial1(String aliasTable, String aliasUserStatus) {
		return String.format(
				" INNER JOIN #tempUSMSpecial1 AS viewUSM_SPE\r\n"
				+ "     ON viewUSM_SPE.UserStatus = %s.%s\r\n",
				aliasTable, aliasUserStatus);
	}

	// LEFT JOIN #tempUCALBroad — แทน buildLeftJoinUserStatusAuto ใน typed tables
	// ใช้ TEMP_UserStatusAuto ที่ pre-materialized แล้ว (ไม่ต้อง scan ตาราง permanent ซ้ำทุก query)
	public String getLeftJoinTempUCALBroad(String aliasJoin, String aliasProd, String aliasGrade) {
		return String.format(
				" LEFT JOIN #tempUCALBroad AS %s\r\n"
				+ "     ON %s.ProductionOrder = %s.ProductionOrder\r\n"
				+ "    AND (%s.Grade = %s.Grade OR %s.Grade IS NULL)\r\n",
				aliasJoin, aliasJoin, aliasProd, aliasJoin, aliasGrade, aliasJoin);
	}

	// ใช้แทน buildLeftJoinUserStatusAuto ใน createTempProdMain
	// join #tempUCAL (small, pre-filtered) แทน TEMP_UserStatusAuto (large permanent table)
	// ⚠️ ระวัง: NULL-check อยู่ที่ UCAL.Grade — คนละ semantics กับ buildLeftJoinUserStatusAuto
	//    (ของเดิม check ที่ %s.Grade ฝั่ง SumGR) — ห้ามใช้แทนกันตรงๆ ดู getLeftJoinTempUCALSameAsAuto
	public String getLeftJoinTempUCAL(String aliasProd, String aliasGrade)
	{
		return String.format(
				" LEFT JOIN #tempUCAL AS UCAL ON UCAL.ProductionOrder = %s.ProductionOrder\r\n"
				+ "     AND (UCAL.Grade = %s.Grade OR UCAL.Grade IS NULL)\r\n",
				aliasProd, aliasGrade);
	}

	// drop-in ของ buildLeftJoinUserStatusAuto("UCAL", aliasProd, aliasGrade) แต่ join #tempUCAL
	// คง ON clause เดิมทุกตัวอักษร (DataStatus='O' ย้ายเข้า temp ตอนสร้างแล้ว):
	//   เดิม:  (m.Grade = UCAL.Grade OR m.Grade IS NULL)  ← NULL-check ฝั่ง SumGR grade
	// harness จับได้ว่า NULL-check ฝั่ง UCAL.Grade (getLeftJoinTempUCAL) ให้ผลต่าง 77 แถว (userStatus เกิน)
	public String getLeftJoinTempUCALSameAsAuto(String aliasProd, String aliasGrade)
	{
		return String.format(" "
				+ "     LEFT JOIN #tempUCAL AS UCAL \n"
				+ "     ON %s.ProductionOrder = UCAL.ProductionOrder \n"
				+ "     AND (%s.Grade = UCAL.Grade OR %s.Grade IS NULL) \n",
				aliasProd, aliasGrade, aliasGrade);
	}

	// ใช้แทน buildLeftJoinViewUserStatusMappingPCMS ใน createTempProdMain
	// join #tempUSMSPE (materialized, indexed) แทน view (expanded inline)
	public String getLeftJoinTempUSMSPE()
	{
		return " LEFT JOIN #tempUSMSPE AS viewUSM_SPE ON viewUSM_SPE.UserStatus = UCAL.UserStatusCal\r\n";
	}

	// Pre-filter SumGRCache ให้เหลือเฉพาะ PO ที่ใช้จริง + 1 row ต่อ PO (prefer Grade='A' ก่อน)
	// ไม่ filter ตาม Grade เพราะ PO บางตัวอาจมีแค่ Grade='B'/'C' — ถ้า filter ออกจะทำให้ UCAL lookup ผิด
	// → #tempSumGRMain (เหมือน pattern ของ #tempUCAL กับ TEMP_UserStatusAuto)
	// ต้องรัน AFTER createTempProdData (#tempProdData ต้องมีแล้ว)
	public String createTempSumGRMain = ""
			+ this.buildIfTempTableDrop("#tempSumGRMain")
			+ ";WITH _sgRanked AS (\r\n"
			+ "    SELECT sg.*,\r\n"
			+ "           ROW_NUMBER() OVER (\r\n"
			+ "               PARTITION BY sg.ProductionOrder\r\n"
			+ "               ORDER BY CASE WHEN sg.Grade = 'A' THEN 0 WHEN sg.Grade = '' THEN 1 WHEN sg.Grade IS NULL THEN 2 ELSE 3 END\r\n"
			+ "           ) AS _sgRn\r\n"
			+ "    FROM [PCMS].[dbo].SumGRCache AS sg\r\n"
			+ "    INNER JOIN (\r\n"
			+ "        SELECT DISTINCT ProductionOrder FROM #tempProdData WHERE ProductionOrder IS NOT NULL\r\n"
			+ "    ) AS pd ON pd.ProductionOrder = sg.ProductionOrder\r\n"
			+ ")\r\n"
			+ "SELECT * INTO #tempSumGRMain FROM _sgRanked WHERE _sgRn = 1;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempSumGRMain ON #tempSumGRMain(ProductionOrder);\r\n";

	// LEFT JOIN #tempSumGRMain ใน createTempProdMain — แทน getOuterApplyTempSumGRGradeA
	// ไม่ต้องใช้ OUTER APPLY เพราะ #tempSumGRMain มี 1 row ต่อ PO อยู่แล้ว → 1:1 join แน่นอน
	public String buildLeftJoinTempSumGRMain(String aliasProd)
	{
		return String.format(
				" LEFT JOIN #tempSumGRMain AS m ON m.ProductionOrder = %s.ProductionOrder\r\n",
				aliasProd);
	}

	// OUTER APPLY แทน LEFT JOIN #tempUCAL ใน createTempProdMain
	// TOP 1 + prefer Grade-specific over NULL → รับประกัน 1 row ต่อ PO → ไม่เกิด row multiplication → ไม่ต้องใช้ SELECT DISTINCT
	public String getOuterApplyTempUCAL(String aliasProd, String aliasGrade)
	{
		return String.format(
				" OUTER APPLY (\r\n"
				+ "     SELECT TOP 1 UserStatusCal\r\n"
				+ "     FROM #tempUCAL AS _uc\r\n"
				+ "     WHERE _uc.ProductionOrder = %s.ProductionOrder\r\n"
				+ "       AND (_uc.Grade = %s.Grade OR _uc.Grade IS NULL)\r\n"
				+ "     ORDER BY CASE WHEN _uc.Grade IS NOT NULL THEN 0 ELSE 1 END\r\n"
				+ " ) AS UCAL\r\n",
				aliasProd, aliasGrade);
	}

	/**
	 * OUTER APPLY แทน LEFT JOIN #tempUCAL — แก้ปัญหา SELECT DISTINCT 60+ คอลัมน์
	 *
	 * ปัญหาเดิม: LEFT JOIN #tempUCAL ด้วย (Grade = m.Grade OR Grade IS NULL) ทำให้ได้ 2 row ต่อ 1 (PO, Grade)
	 *   - row จาก grade-specific UCAL (Grade='X')
	 *   - row จาก catch-all UCAL (Grade IS NULL)
	 *   → SELECT DISTINCT จำเป็น เพื่อ collapse แต่ sort/hash บน 60+ คอลัมน์ช้ามาก
	 *
	 * วิธีนี้ใช้ OUTER APPLY TOP 1 โดย ORDER BY 2 เกณฑ์:
	 *   1. prefer UCAL row ที่ UserStatusCal มี entry ใน #tempUSMSPE (Special=0) → PassFilter=1 ได้
	 *   2. ถ้าเท่ากัน: prefer grade-specific row มากกว่า catch-all
	 *
	 * ผลลัพธ์:
	 *   - Grade Z: grade-specific row (Special=1 เท่านั้น, ไม่อยู่ใน #tempUSMSPE) → ลำดับ 1
	 *             catch-all row (Special=0, อยู่ใน #tempUSMSPE) → ลำดับ 0 → ถูกเลือก ✓
	 *   - Grade A: grade-specific (Special=0) → ลำดับ (0, 0) → ถูกเลือก ✓
	 *   - ไม่มี UCAL row ใดเลย: OUTER APPLY คืน NULL → PassFilter ผ่าน adjVol แทน ✓
	 *
	 * ต้องใช้คู่กับ getLeftJoinTempUSMSPE() ใน outer query (viewUSM_SPE ยังคงต้องใช้ใน PassFilter CASE)
	 */
	public String getOuterApplyTempUCALWithSpecialPref(String aliasProd, String aliasGrade)
	{
		return String.format(
				" OUTER APPLY (\r\n"
				+ "     SELECT TOP 1 _uc.UserStatusCal\r\n"
				+ "     FROM #tempUCAL AS _uc\r\n"
				+ "     LEFT JOIN #tempUSMSPE AS _spe ON _spe.UserStatus = _uc.UserStatusCal\r\n"
				+ "     WHERE _uc.ProductionOrder = %s.ProductionOrder\r\n"
				+ "       AND (_uc.Grade = %s.Grade OR _uc.Grade IS NULL)\r\n"
				+ "     ORDER BY\r\n"
				+ "         CASE WHEN _spe.UserStatus IS NOT NULL THEN 0 ELSE 1 END,\r\n"
				+ "         CASE WHEN _uc.Grade IS NOT NULL THEN 0 ELSE 1 END\r\n"
				+ " ) AS UCAL\r\n",
				aliasProd, aliasGrade);
	}

	public String fromProdA = "" + " from ProdData as a \r\n";

	/**
	 * PassFilter CASE — ใช้ใน leftJoinBSelect ของ PCMSMainDaoImpl และ PCMSDetailDaoImpl
	 *   1 = passFilter (แสดงผล), 0 = กรองออก
	 *   ลำดับเงื่อนไข:
	 *     adjVol IS NOT NULL  → มี production order จริง → pass
	 *     LotNo เป็นค่า virtual + adjVol = 0 + ไม่มี CRP → pass
	 *     Volumn = 0          → order ไม่มีปริมาณ → pass
	 *     viewUSM_SPE.Special = 0 → UserStatus map ไปหน่วยงานที่รับ (Special=0) → pass
	 *     else                → ไม่ผ่าน
	 */
	public String passFilterExpr = ""
			+ "                ,CASE\r\n"
			+ "                   WHEN adjVol IS NOT NULL THEN 1\r\n"
			+ "                   WHEN a.LotNo IN (N'รอจัด Lot',N'ขาย stock',N'รับจ้างถัก',N'Lot ขายแล้ว',N'พ่วงแล้วรอสวม',N'รอสวมเคยมี Lot')\r\n"
			+ "                        AND adjVol = 0 AND CRP.SaleOrder IS NULL THEN 1\r\n"
			+ "                   WHEN a.Volumn = 0 THEN 1\r\n"
			+ "                   WHEN viewUSM_SPE.Special = 0 THEN 1\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 END AS PassFilter\r\n";
	// pre-build PlanCFMLabDate (ROW_NUMBER ครั้งเดียว แทน 5 ครั้งต่อ query)
	public String createTempPlanCFMLabDate = ""
			+ this.buildIfTempTableDrop("#tempPlanCFMLabDate")
			+ "SELECT ProductionOrder, SaleOrder, SaleLine, PlanDate AS CFMPlanLabDate\r\n"
			+ "INTO #tempPlanCFMLabDate\r\n"
			+ "FROM (\r\n"
			+ "    SELECT *,\r\n"
			+ "           ROW_NUMBER() OVER (\r\n"
			+ "               PARTITION BY ProductionOrder, SaleOrder, SaleLine\r\n"
			+ "               ORDER BY CreateDate DESC\r\n"
			+ "           ) AS rn\r\n"
			+ "    FROM [PCMS].[dbo].[PlanCFMLabDate]\r\n"
			+ ") AS ranked\r\n"
			+ "WHERE rn = 1;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempPlanCFMLabDate\r\n"
			+ "ON #tempPlanCFMLabDate (ProductionOrder, SaleOrder, SaleLine);\r\n";

	// createTempSumGR: โหลดทั้ง table เพราะ ProductionOrderRP (case สวม) อยู่ใน
	// FromSapMainProdSale ด้วย SaleOrder ต้นทาง ซึ่งอาจไม่อยู่ใน #tempMainSale
	public String createTempSumGR = ""
			+ this.buildIfTempTableDrop("#tempSumGR")
			+ " SELECT *\r\n"
			+ " INTO #tempSumGR\r\n"
			+ " FROM [PCMS].[dbo].SumGRCache;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempSumGR ON #tempSumGR(ProductionOrder, Grade);\r\n";
	// filtered version — คง granularity (ProductionOrder, Grade) เดิมทุกอย่าง แค่กรอง SumGRCache
	// เหลือเฉพาะ PO ที่ search นี้อ้างถึง (ชุดเดียวกับ createTempProdWorkDateFiltered — regular
	// FromSapMainProd + replaced ProductionOrderRP scope ตาม #tempMainSale) → copy row น้อยลงมาก
	// บน PRD. POs ที่ถูกตัดออกไม่เคย match join `aliasMain.ProductionOrder = m.ProductionOrder`
	// อยู่แล้ว (aliasMain มีเฉพาะ PO ในขอบเขต) → output ต้องเท่าเดิม (harness gate)
	// requires #tempMainSale + clustered index ก่อน
	public String createTempSumGRFiltered = ""
			+ this.buildIfTempTableDrop("#tempSumGR")
			+ " SELECT sg.*\r\n"
			+ " INTO #tempSumGR\r\n"
			+ " FROM [PCMS].[dbo].SumGRCache AS sg\r\n"
			+ " INNER JOIN (\r\n"
			+ "     SELECT fsp.ProductionOrder\r\n"
			+ "     FROM [PCMS].[dbo].[FromSapMainProd] AS fsp\r\n"
			+ "     INNER JOIN #tempMainSale AS ms ON ms.SaleOrder = fsp.SaleOrder AND ms.SaleLine = fsp.SaleLine\r\n"
			+ "     WHERE fsp.DataStatus = 'O'\r\n"
			+ "     UNION\r\n"
			+ "     SELECT fps.ProductionOrder\r\n"   // OrderPuang subs: sale→prod mapping ทั้งหมด
			+ "     FROM [PCMS].[dbo].[FromSapMainProdSale] AS fps\r\n"
			+ "     INNER JOIN #tempMainSale AS ms ON ms.SaleOrder = fps.SaleOrder AND ms.SaleLine = fps.SaleLine\r\n"
			+ "     WHERE fps.DataStatus = 'O'\r\n"
			+ "     UNION\r\n"
			+ "     SELECT rpo.ProductionOrderRP\r\n"   // replaced
			+ "     FROM [PCMS].[dbo].[ReplacedProdOrder] AS rpo\r\n"
			+ "     INNER JOIN #tempMainSale AS ms ON ms.SaleOrder = rpo.SaleOrder AND ms.SaleLine = rpo.SaleLine\r\n"
			+ "     WHERE rpo.DataStatus = 'O'\r\n"
			+ "     UNION\r\n"
			+ "     SELECT spo.ProductionOrder\r\n"   // switch — ต้นทาง
			+ "     FROM [PCMS].[dbo].[SwitchProdOrder] AS spo\r\n"
			+ "     INNER JOIN #tempMainSale AS ms ON ms.SaleOrder = spo.SaleOrderSW AND ms.SaleLine = spo.SaleLineSW\r\n"
			+ "     WHERE spo.DataStatus = 'O'\r\n"
			+ "     UNION\r\n"
			+ "     SELECT spo.ProductionOrderSW\r\n"   // switch — ปลายทาง
			+ "     FROM [PCMS].[dbo].[SwitchProdOrder] AS spo\r\n"
			+ "     INNER JOIN #tempMainSale AS ms ON ms.SaleOrder = spo.SaleOrderSW AND ms.SaleLine = spo.SaleLineSW\r\n"
			+ "     WHERE spo.DataStatus = 'O'\r\n"
			+ " ) AS flt ON flt.ProductionOrder = sg.ProductionOrder;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempSumGR ON #tempSumGR(ProductionOrder, Grade);\r\n";
	// pre-build PlanSendCFMCusDate → #tempSCC (1 scan ครั้งเดียว แทนหลาย scan ต่อ query)
	public String createTempSCC = ""
			+ this.buildIfTempTableDrop("#tempSCC")
			+ "SELECT ProductionOrder, MAX(SendCFMCusDate) AS SendCFMCusDate\r\n"
			+ "INTO #tempSCC\r\n"
			+ "FROM [PCMS].[dbo].[PlanSendCFMCusDate]\r\n"
			+ "WHERE DataStatus = 'O'\r\n"
			+ "GROUP BY ProductionOrder;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempSCC ON #tempSCC(ProductionOrder);\r\n";

	// pre-build FromSORCFM → #tempFromSORCFM (1 scan ครั้งเดียว แทนหลาย scan ต่อ query)
	public String createTempFromSORCFM = ""
			+ this.buildIfTempTableDrop("#tempFromSORCFM")
			+ "SELECT SaleOrder, SaleLine, MAX(CFMDATE) AS CFMDate\r\n"
			+ "INTO #tempFromSORCFM\r\n"
			+ "FROM [PCMS].[dbo].[FromSORCFM]\r\n"
			+ "GROUP BY SaleOrder, SaleLine;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempFromSORCFM ON #tempFromSORCFM(SaleOrder, SaleLine);\r\n";

	// full scan version — ใช้เมื่อไม่มี #tempMainSale (เช่น background job)
	public String createTempProdWorkDate = ""
			+ this.buildIfTempTableDrop("#tempProdWorkDate")
			+ "SELECT *\r\n"
			+ "INTO #tempProdWorkDate\r\n"
			+ "FROM [PCMS].[dbo].[TEMP_ProdWorkDate];\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempProdWorkDate ON #tempProdWorkDate(ProductionOrder);\r\n";

	// filtered version — requires #tempMainSale with clustered index already built
	// filters to only POs relevant to the current search:
	//   regular POs  : FromSapMainProd   JOIN #tempMainSale (SaleOrder, SaleLine)
	//   replaced POs : ReplacedProdOrder JOIN #tempMainSale (SaleOrder, SaleLine) -> ProductionOrderRP
	// eliminates ~90% of TEMP_ProdWorkDate rows → cuts tempdb I/O dramatically
	public String createTempProdWorkDateFiltered = ""
			+ this.buildIfTempTableDrop("#tempProdWorkDate")
			+ "SELECT pwd.*\r\n"
			+ "INTO #tempProdWorkDate\r\n"
			+ "FROM [PCMS].[dbo].[TEMP_ProdWorkDate] AS pwd\r\n"
			+ "INNER JOIN (\r\n"
			+ "    SELECT fsp.ProductionOrder\r\n"
			+ "    FROM [PCMS].[dbo].[FromSapMainProd] AS fsp\r\n"
			+ "    INNER JOIN #tempMainSale AS ms\r\n"
			+ "        ON ms.SaleOrder = fsp.SaleOrder AND ms.SaleLine = fsp.SaleLine\r\n"
			+ "    WHERE fsp.DataStatus = 'O'\r\n"
			+ "    UNION\r\n"
			+ "    SELECT rpo.ProductionOrderRP\r\n"
			+ "    FROM [PCMS].[dbo].[ReplacedProdOrder] AS rpo\r\n"
			+ "    INNER JOIN #tempMainSale AS ms\r\n"
			+ "        ON ms.SaleOrder = rpo.SaleOrder AND ms.SaleLine = rpo.SaleLine\r\n"
			+ "    WHERE rpo.DataStatus = 'O'\r\n"
			+ ") AS flt ON flt.ProductionOrder = pwd.ProductionOrder;\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempProdWorkDate ON #tempProdWorkDate(ProductionOrder);\r\n";

	// pre-build SwitchProdOrder → #tempSPO (1 scan แทนหลาย scan ต่อ query)
	public String createTempSPO = ""
			+ this.buildIfTempTableDrop("#tempSPO")
			+ "SELECT ProductionOrder, ProductionOrderSW\r\n"
			+ "INTO #tempSPO\r\n"
			+ "FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
			+ "WHERE ProductionOrder <> ProductionOrderSW AND DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempSPO_SW ON #tempSPO(ProductionOrderSW);\r\n"
			+ "CREATE NONCLUSTERED INDEX IX_tempSPO_PO ON #tempSPO(ProductionOrder);\r\n";

	// pre-build FromSapMainProdSale filtered to SPO rows only → #tempSPOSale (reused by OPSW and SW)
	public String createTempSPOSale = ""
			+ this.buildIfTempTableDrop("#tempSPOSale")
			+ "SELECT\r\n"
			+ "    CASE WHEN B.ProductionOrderSW IS NOT NULL THEN B.ProductionOrderSW\r\n"
			+ "         ELSE C.ProductionOrder END AS ProductionOrder,\r\n"
			+ "    A.SaleOrder, A.SaleLine, A.Volumn\r\n"
			+ "INTO #tempSPOSale\r\n"
			+ "FROM [PCMS].[dbo].[FromSapMainProdSale] AS A\r\n"
			+ "LEFT JOIN #tempSPO AS B ON A.ProductionOrder = B.ProductionOrder\r\n"
			+ "LEFT JOIN #tempSPO AS C ON A.ProductionOrder = C.ProductionOrderSW\r\n"
			+ "WHERE (B.ProductionOrder IS NOT NULL OR C.ProductionOrder IS NOT NULL)\r\n"
			+ "  AND A.DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempSPOSale_SO_SL ON #tempSPOSale(SaleOrder, SaleLine);\r\n"
			+ "CREATE NONCLUSTERED INDEX IX_tempSPOSale_PO ON #tempSPOSale(ProductionOrder);\r\n";

	// clustered index on #tempMainSale — added after INSERT so every downstream INNER JOIN is a seek
	public String createTempMainSaleIndex =
			"CREATE CLUSTERED INDEX IX_tempMainSale ON #tempMainSale(SaleOrder, SaleLine);\r\n";

	// pre-build InputReplacedRemark → #tempInputRR
	// INNER JOIN #tempMainSale: กรองเฉพาะ SaleOrder/SaleLine ใน search criteria
	// → ลดจาก "ทุก record DataStatus=O" เหลือแค่แถวที่จะปรากฏใน final SELECT
	public String createTempInputRR = ""
			+ this.buildIfTempTableDrop("#tempInputRR")
			+ "SELECT ir.ProductionOrder, ir.SaleOrder, ir.SaleLine, ir.ReplacedRemark\r\n"
			+ "INTO #tempInputRR\r\n"
			+ "FROM [PCMS].[dbo].[InputReplacedRemark] ir\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = ir.SaleOrder AND ms.SaleLine = ir.SaleLine\r\n"
			+ "WHERE ir.DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempInputRR ON #tempInputRR(ProductionOrder, SaleOrder, SaleLine);\r\n";

	// pre-build InputPCRemark → #tempInputPCR
	// INNER JOIN #tempMainSale: กรองเฉพาะ SaleOrder/SaleLine ใน search criteria
	public String createTempInputPCR = ""
			+ this.buildIfTempTableDrop("#tempInputPCR")
			+ "SELECT ir.ProductionOrder, ir.SaleOrder, ir.SaleLine, ir.PCRemark\r\n"
			+ "INTO #tempInputPCR\r\n"
			+ "FROM [PCMS].[dbo].[InputPCRemark] ir\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = ir.SaleOrder AND ms.SaleLine = ir.SaleLine\r\n"
			+ "WHERE ir.DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempInputPCR ON #tempInputPCR(ProductionOrder, SaleOrder, SaleLine);\r\n";

	// pre-build InputCauseOfDelay → #tempInputCOD  (ไม่มี SaleOrder/SaleLine — กรองด้วย SPO แทน)
	public String createTempInputCOD = ""
			+ this.buildIfTempTableDrop("#tempInputCOD")
			+ "SELECT ProductionOrder, CauseOfDelay\r\n"
			+ "INTO #tempInputCOD\r\n"
			+ "FROM [PCMS].[dbo].[InputCauseOfDelay]\r\n"
			+ "WHERE DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempInputCOD ON #tempInputCOD(ProductionOrder);\r\n";

	// pre-build InputDelayedDep → #tempInputDD  (ไม่มี SaleOrder/SaleLine)
	public String createTempInputDD = ""
			+ this.buildIfTempTableDrop("#tempInputDD")
			+ "SELECT ProductionOrder, DelayedDep\r\n"
			+ "INTO #tempInputDD\r\n"
			+ "FROM [PCMS].[dbo].[InputDelayedDep]\r\n"
			+ "WHERE DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempInputDD ON #tempInputDD(ProductionOrder);\r\n";

	// pre-build InputSwitchRemark → #tempInputSR  (ไม่มี SaleOrder/SaleLine)
	public String createTempInputSR = ""
			+ this.buildIfTempTableDrop("#tempInputSR")
			+ "SELECT ProductionOrder, SwitchRemark\r\n"
			+ "INTO #tempInputSR\r\n"
			+ "FROM [PCMS].[dbo].[InputSwitchRemark]\r\n"
			+ "WHERE DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempInputSR ON #tempInputSR(ProductionOrder);\r\n";

	// pre-build InputStockLoad → #tempInputSL
	// INNER JOIN #tempMainSale: กรองเฉพาะ SaleOrder/SaleLine ใน search criteria
	public String createTempInputSL = ""
			+ this.buildIfTempTableDrop("#tempInputSL")
			+ "SELECT ir.ProductionOrder, ir.SaleOrder, ir.SaleLine, ir.StockLoad\r\n"
			+ "INTO #tempInputSL\r\n"
			+ "FROM [PCMS].[dbo].[InputStockLoad] ir\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = ir.SaleOrder AND ms.SaleLine = ir.SaleLine\r\n"
			+ "WHERE ir.DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempInputSL ON #tempInputSL(ProductionOrder, SaleOrder, SaleLine);\r\n";

	// pre-build InputStockRemark → #tempInputStockRemark
	// INNER JOIN #tempMainSale: กรองเฉพาะ SaleOrder/SaleLine ใน search criteria
	// (ก่อนหน้านี้ load ทุก record → join 4 column ใน final SELECT ช้ามาก)
	public String createTempInputStockRemark = ""
			+ this.buildIfTempTableDrop("#tempInputStockRemark")
			+ "SELECT ir.ProductionOrder, ir.SaleOrder, ir.SaleLine, ir.Grade, ir.StockRemark\r\n"
			+ "INTO #tempInputStockRemark\r\n"
			+ "FROM [PCMS].[dbo].[InputStockRemark] ir\r\n"
			+ "INNER JOIN #tempMainSale ms ON ms.SaleOrder = ir.SaleOrder AND ms.SaleLine = ir.SaleLine\r\n"
			+ "WHERE ir.DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempInputStockRemark ON #tempInputStockRemark(ProductionOrder, SaleOrder, SaleLine, Grade);\r\n";

	// pre-build TAPP (SOR_TempProd JOIN ApprovedPlanDate) → #tempTAPP
	public String createTempTAPP = ""
			+ this.buildIfTempTableDrop("#tempTAPP")
			+ "SELECT a.ProductionOrder, b.SORCFMDate, b.SORDueDate\r\n"
			+ "INTO #tempTAPP\r\n"
			+ "FROM [PPMM].[dbo].[SOR_TempProd] AS a\r\n"
			+ "INNER JOIN [PPMM].[dbo].[ApprovedPlanDate] AS b ON a.POId = b.POId\r\n"
			+ "WHERE a.DataStatus = 'O' AND b.DataStatus = 'O';\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempTAPP ON #tempTAPP(ProductionOrder);\r\n";

	public String createTempSumBill = ""
			+ "  If(OBJECT_ID('tempdb..#tempSumBill') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempSumBill\r\n"
			+ "	end ;\r\n"
			+ " SELECT b.*\r\n"
			+ " INTO #tempSumBill\r\n"
			+ " FROM [PCMS].dbo.SumBillCache b\r\n"
			+ " JOIN #tempMainSale ms ON ms.SaleOrder = b.SaleOrder AND ms.SaleLine = b.SaleLine;\r\n"
	+ "CREATE CLUSTERED INDEX IX_tempSumBill ON #tempSumBill(ProductionOrder, SaleOrder, SaleLine, Grade);\r\n";
	public String createTempMainSale = ""
			+ " If(OBJECT_ID('tempdb..#tempMainSale') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempMainSale\r\n"
			+ "	end ; "
			+ " SELECT   \r\n"
			+ "	   a.*\r\n" 
			+ " INTO #tempMainSale \r\n"
			+ " FROM [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			+ " left join [PCMS].[dbo].[ConfigCustomerEX] as b on a.[CustomerNo] = b.[CustomerNo] and b.[DataStatus] = 'O' ";
	public String createTempMainSaleWithJoinCustomer = ""
			+ " If(OBJECT_ID('tempdb..#tempMainSale') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempMainSale\r\n"
			+ "	end ; "
			+ " SELECT   \r\n"
			+ "	   a.*\r\n" 
			+ " INTO #tempMainSale \r\n"
			+ " FROM [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			+ " INNER JOIN #tempCustomerList AS c\r\n"
			+ "    ON a.CustomerName = c.CustomerName "
			+ " INNER JOIN #tempCustomerShortList AS d\r\n"
			+ "    ON a.CustomerShortName = d.CustomerShortName "
			+ " left join [PCMS].[dbo].[ConfigCustomerEX] as b on a.[CustomerNo] = b.[CustomerNo] and b.[DataStatus] = 'O' "
			+ "";

//	public String createClusteredIndexTempMainSale =
//			"" + "CREATE CLUSTERED INDEX IX_tempMainSale_SO_SL\r\n" + "ON #tempMainSale (SaleOrder, SaleLine);";
	public String createTempPlanDeliveryDate = "  "
			+ " If(OBJECT_ID('tempdb..#tempPlandeliveryDate') Is Not Null)\r\n"
			+ "		begin\r\n"
			+ "			Drop Table #tempPlandeliveryDate\r\n"
			+ "		end ; \r\n"
			+ " ;WITH LatestPlan AS (\r\n"
			+ "    SELECT\r\n"
			+ "        Id,\r\n"
			+ "        [ProductionOrder],\r\n"
			+ "        [SaleOrder],\r\n"
			+ "        [SaleLine],\r\n"
			+ "        [PlanDate] AS DeliveryDate,\r\n"
			+ "        ROW_NUMBER() OVER (\r\n"
			+ "            PARTITION BY [ProductionOrder], [SaleOrder], [SaleLine]\r\n"
			+ "            ORDER BY Id DESC\r\n"
			+ "        ) AS rn\r\n"
			+ "    FROM [PCMS].[dbo].[PlanDeliveryDate]\r\n"
			+ ")\r\n"
			+ "SELECT\r\n"
			+ "    Id,\r\n"
			+ "    [ProductionOrder],\r\n"
			+ "    [SaleOrder],\r\n"
			+ "    [SaleLine],\r\n"
			+ "    DeliveryDate\r\n"
			+ "INTO #tempPlandeliveryDate\r\n"
			+ "FROM LatestPlan\r\n"
			+ "WHERE rn = 1;\r\n"
			+ "-- เพิ่ม Index หลัง SELECT INTO ทันที\r\n"
			+ "CREATE CLUSTERED INDEX IX_tempPlanDelivery ON #tempPlandeliveryDate (ProductionOrder, SaleOrder, SaleLine); \n";

	public String createTempPrepWaitLot = ""
			+ this.buildIfTempTableDrop("#tempPrepWaitLot")
			+ " SELECT \r\n"
			+ "		a.saleorder 		, a.saleline		,CASE  \r\n"
			+ " 			WHEN COALESCE(c.SumVolMain, 0 ) >  ( c.SumVolOP+ SumVolRP) THEN 'A'\r\n"
			+ "			WHEN COALESCE(c.SumVolMain, 0 ) <=  ( c.SumVolOP+ SumVolRP) THEN 'B' \r\n"
			+ "			ELSE  'C'\r\n"
			+ "	 		END AS SumVol \r\n"
			+ "		,'รอจัด Lot' as ProductionOrder\r\n"
			+ "		,CASE  \r\n"
			+ " 	    WHEN COALESCE( SumVolOP, 0 ) >  0 THEN 'พ่วงแล้วรอสวม'\r\n"
			+ "			WHEN COALESCE( SumVolRP, 0 ) >  0 THEN 'รอสวมเคยมี Lot'\r\n"
			+ "			ELSE  'รอจัด Lot'\r\n"
			+ "	 		END AS LotNo  \r\n"
			+ "		,SumVolOP\r\n"
			+ "		,SumVolRP\r\n"
			+ "		,CountProdRP\r\n"
			+ "     ,cast(null as decimal) as TotalQuantity \r\n"
			+ "		,cast(null as NVARCHAR) as Grade \r\n"
			+ "		,cast(null as decimal) as BillSendWeightQuantity \r\n"
			+ "		,cast(null as decimal) as BillSendQuantity  \r\n"
			+ "		,cast(null as decimal) as BillSendMRQuantity \r\n"
			+ "		,cast(null as decimal) as BillSendYDQuantity  \r\n"
			+ "		,cast(null as NVARCHAR) as LabNo\r\n"
			+ "		,cast(null as NVARCHAR) as LabStatus\r\n"
			+ "		,cast(null as date) as CFMPlanLabDate\r\n"
			+ "		,cast(null as date) as CFMActualLabDate \r\n"
			+ "		,cast(null as date) as CFMCusAnsLabDate \r\n"
			+ "		,cast(null as NVARCHAR) as UserStatus \r\n"
			+ "		,cast(null as date) as TKCFM \r\n"
			+ "		,cast(null as date) as CFMPlanDate \r\n"
			+ "		,cast(null as date) as DeliveryDate  \r\n"
			+ "		,cast(null as date) as SendCFMCusDate \r\n"
			+ "		,cast(null as date) as CFMSendDate \r\n"
			+ "		,cast(null as date) as CFMAnswerDate \r\n"
			+ "		,cast(null as NVARCHAR) as CFMStatus \r\n"
			+ "		,cast(null as NVARCHAR) as CFMNumber  \r\n"
			+ "		,cast(null as NVARCHAR) as CFMRemark  \r\n"
			+ "		,cast(null as NVARCHAR) as RemarkOne \r\n"
			+ "		,cast(null as NVARCHAR) as RemarkTwo \r\n"
			+ "		,cast(null as NVARCHAR) as RemarkThree  \r\n"
			+ "		,cast(null as NVARCHAR) as StockRemark \r\n"
			+ "		,cast(null as decimal) as  GRSumKG \r\n"
			+ "		,cast(null as decimal) as  GRSumYD \r\n"
			+ "		,cast(null as decimal) as  GRSumMR \r\n"
			+ "		,cast(null as date) as  DyePlan \r\n"
			+ "		,cast(null as date) as DyeActual   \r\n"
			+ "		,cast(null as NVARCHAR) as [SwitchRemark] \r\n"
			+ "		,cast(null as date) as [PrdCreateDate]\r\n"
			+ "		,cast(null as decimal) AS Volumn   \r\n"
			+ "		,cast(null as decimal) AS VolumnFGAmount  	\r\n"
			+ "	    , cast(null as date) as GreigeInDate \r\n"
			+ "		, cast(null as date) as Dryer \r\n"
			+ "		, cast(null as date) as Finishing  \r\n"
			+ "		, cast(null as date) as Inspectation \r\n"
			+ "		, cast(null as date) as Prepare \r\n"
			+ "		, cast(null as date) as Preset \r\n"
			+ "		, cast(null as date) as Relax \r\n"
			+ "		,cast(null as date) as CFMDateActual \r\n"
			+ "		, cast(null as NVARCHAR) as DyeStatus \r\n"
			+ "		, cast(null as date) AS LotShipping \r\n"
			+ "		, cast(null as date) as PlanGreigeDate  \r\n"
			+ "		,cast(null as NVARCHAR) as CFMDetailAll \r\n"
			+ "		,cast(null as NVARCHAR) as RollNoRemarkAll \r\n"
			+ "		,cast(null as NVARCHAR) as CFMNumberAll \r\n"
			+ "		,cast(null as NVARCHAR) as CFMRemarkAll\r\n"
			+ " into #tempPrepWaitLot\r\n"
			// ใช้ #tempMainSale แทน FromSapMainSale ทั้ง table
			// #tempMainSale filtered DataStatus='O' ไว้แล้ว และมีเฉพาะ sales ที่ค้นหา
			+ "	from #tempMainSale as a\r\n"
			+ " LEFT JOIN #tmpSaleAgg c \r\n"
			+ "       ON a.SaleOrder = c.SaleOrder \r\n"
			+ "      AND a.SaleLine  = c.SaleLine\r\n"
			+ " left join (\r\n"
			+ "		SELECT a.SaleOrder, a.SaleLine , 1 as countProdRP \r\n"
			+ "		FROM [PCMS].[dbo].[ReplacedProdOrder] a\r\n"
			+ "		INNER JOIN [PCMS].[dbo].[FromSapMainProd] b \r\n"
			+ "			ON a.ProductionOrderRP = b.ProductionOrder \r\n"
			+ "		INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] v\r\n"
			+ "			ON b.[UserStatus] = v.UserStatus \r\n"
			+ "			AND v.Special = 1  \r\n"
			+ "		WHERE a.DataStatus = 'O' and a.ProductionOrder = 'รอจัด Lot' \r\n"
			+ "		GROUP BY a.SaleOrder, a.SaleLine \r\n"
			+ "	) AS D ON A.SaleOrder = D.SaleOrder  \r\n"
			+ "          AND A.SaleLine = D.SaleLine  \r\n"
			+ "	where  1 = 1 AND \r\n"
			+ "         ( c.SumVolMain > 0 OR ( c.SumVolMain is null AND D.SaleOrder IS NOT NULL ) )  ;\r\n";
	public String innerJoinWaitLotB = ""
			+ " INNER JOIN #tempPrepWaitLot AS b ON a.SaleOrder = b.SaleOrder and\r\n"
			+ "                                    a.SaleLine = b.SaleLine \n"; 
	public Map<String, String> buildWhereClauses(PCMSTableDetail bean)
	{
		Map<String, String> whereClauses = new HashMap<>();
		String whereBase = "where 1 = 1";
		String whereSale = " where A.[DataStatus] = 'O' AND 1 = 1";
		String whereWaitLot = " where 1 = 1 ";
//		String whereBMainUserStatus = " where 1 = 1";
		String whereProd = " ";
		String whereCaseTryRP = "";
		String whereCaseTry = "";
		String tmpWhereNoLotUCAL = ""; 
		String saleNumber = "",materialNo = "",saleOrder = "",saleCreateDate = "",labNo = "",articleFG = "",designFG = "",
				prdOrder = "",prdCreateDate = "",deliveryStatus = "",saleStatus = "",distChannel = "",dueDate = "",po = "";
		saleNumber = bean.getSaleNumber();
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
		distChannel = bean.getDistChannel();
		List<String> userStatusList = bean.getUserStatusList();
		List<String> divisionList = bean.getDivisionList();
		// Build where clauses
		whereBase += buildLikeClause("MaterialNo", materialNo, "a");
		whereSale += buildLikeClause("MaterialNo", materialNo, "a");
		whereBase += buildLikeClause("SaleOrder", saleOrder, "a");
		whereSale += buildLikeClause("SaleOrder", saleOrder, "a");
		whereBase += buildDateClause("SaleCreateDate", saleCreateDate, "a");
		whereSale += buildDateClause("SaleCreateDate", saleCreateDate, "a");
		whereBase += buildListClause("Division", divisionList, "a");
		whereSale += buildListClause("Division", divisionList, "a");
		whereBase += buildLikeClause("PurchaseOrder", po, "a");
		whereSale += buildLikeClause("PurchaseOrder", po, "a");
		whereBase += buildLikeClause("SaleNumber", saleNumber, "a");
		whereSale += buildLikeClause("SaleNumber", saleNumber, "a");
		whereBase += buildLikeClause("ArticleFG", articleFG, "a");
		whereSale += buildLikeClause("ArticleFG", articleFG, "a");
		whereBase += buildLikeClause("DesignFG", designFG, "a");
		whereSale += buildLikeClause("DesignFG", designFG, "a");
		whereBase += buildDateClause("DueDate", dueDate, "a");
		whereSale += buildDateClause("DueDate", dueDate, "a");
		whereBase += buildLikeClause("DeliveryStatus", deliveryStatus, "a");
		whereSale += buildLikeClause("DeliveryStatus", deliveryStatus, "a");
		whereBase += buildSaleStatusClause(saleStatus);
		whereSale += buildSaleStatusClause(saleStatus);
		whereBase += buildListClauseByArray("DistChannel", distChannel.split("\\|"));
		whereSale += buildListClauseByArray("DistChannel", distChannel.split("\\|"));

		// Production order conditions
		whereBase += buildLikeClause("LabNo", labNo, "b");
		whereProd += buildLikeClause("LabNo", labNo, "b");
		whereBase += buildLikeClause("ProductionOrder", prdOrder, "b");
		whereProd += buildLikeClause("ProductionOrder", prdOrder, "b");

		whereBase += buildDateClause("PrdCreateDate", prdCreateDate, "b");
		whereProd += buildDateClause("PrdCreateDate", prdCreateDate, "b");
		
		
		String whereProdFinal = "";
		if(!whereProd.trim().isEmpty()) {
			whereProdFinal = "\n and 1 = 1 "+whereProd.replace("b.", "fsmp.");
		}
		whereWaitLot = whereBase;
		whereCaseTry = whereProd;
		whereCaseTryRP = whereProd;
//		whereBMainUserStatus = whereProd;

		if (userStatusList.size() > 0) {
			UserStatusGroups groups = categorizeUserStatus(userStatusList);
			List<String> lotNoList = groups.lotNoList;
//			List<String> userStatusCalRPList = groups.userStatusCalRPList;
			List<String> userStatusCalList = groups.userStatusCalList;
//			List<String> userStatusListA = groups.userStatusListA;
			StringBuilder tmpWhere = new StringBuilder(" and ( b.ProductionOrder is not null and ( \r\n");
			StringBuilder stringTmpWhereNoLotUCAL = new StringBuilder(" and ( b.ProductionOrder is not null and ( \r\n");
			StringBuilder whereCaseTryRPBuilder =
					new StringBuilder(whereCaseTryRP + " and ( b.ProductionOrder is not null and ( \r\n");
			StringBuilder whereCaseTryBuilder =
					new StringBuilder(whereCaseTry + " and ( a.ProductionOrder is not null and ( \r\n");

			boolean hasLotNo = ! lotNoList.isEmpty();
			boolean hasUserStatus = ! userStatusCalList.isEmpty();

			if (hasLotNo) {
				String lotNoInClause = " "
						+ " EXISTS (\r\n"
						+ "   SELECT 1 \r\n"
						+ "   FROM #tempLotNoList l\r\n"
						+ "   WHERE l.LotNo = b.LotNo\r\n"
						+ ")\r\n"
						+ "";
				tmpWhere.append(lotNoInClause);
				whereCaseTryRPBuilder.append(lotNoInClause);
				whereCaseTryBuilder.append(lotNoInClause);
			}

			if (hasUserStatus) {
				String userStatusCalIn = 
						""
//						+ "UCAL.UserStatusCal IN (" + String.join(",", userStatusCalList) + ")"
						+ " EXISTS (\r\n"
						+ "   SELECT 1 \r\n"
						+ "   FROM #tempUserStatusList u\r\n"
						+ "   WHERE u.UserStatus = UCAL.UserStatusCal\r\n"
						+ ")";
				String userStatusCalRPIn = ""
//						+ "UCALRP.UserStatusCalRP IN (" + String.join(",", userStatusCalRPList) + ")"
						+ " EXISTS (\r\n"
						+ "   SELECT 1 \r\n"
						+ "   FROM #tempUserStatusList u\r\n"
						+ "   WHERE u.UserStatus = UCALRP.UserStatusCalRP\r\n"
						+ ")";
				String userStatusAIn = //"a.UserStatus IN (" + String.join(",", userStatusListA) + ")"
						  ""
						+ " EXISTS (\r\n"
						+ "   SELECT 1 \r\n"
						+ "   FROM #tempUserStatusList u\r\n"
						+ "   WHERE u.UserStatus = a.UserStatus\r\n"
						+ ")";

				if (hasLotNo) {
					tmpWhere.append(" OR ").append(userStatusCalIn);
					whereCaseTryRPBuilder.append(" OR ").append(userStatusCalRPIn);
					whereCaseTryBuilder.append(" OR ").append(userStatusAIn);
					stringTmpWhereNoLotUCAL.append(userStatusCalIn);
				} else {
					tmpWhere.append(userStatusCalIn);
					whereCaseTryRPBuilder.append(userStatusCalRPIn);
					whereCaseTryBuilder.append(userStatusAIn);
					stringTmpWhereNoLotUCAL.append(userStatusCalIn);
				}
			} else {
				// กรณีไม่มี userStatus ให้เติมเงื่อนไขเท่ากับค่าว่างเพื่อให้ where
				// มีความสมบูรณ์
				stringTmpWhereNoLotUCAL.append("UCAL.UserStatusCal = ''");
			}

			tmpWhere.append(" ) ) \r\n");
			whereCaseTryBuilder.append(" ) ) \r\n");
			whereCaseTryRPBuilder.append(" ) ) \r\n");
			stringTmpWhereNoLotUCAL.append(" ) ) \r\n");

			whereBase += tmpWhere.toString();
			whereCaseTry = whereCaseTryBuilder.toString();
			whereCaseTryRP = whereCaseTryRPBuilder.toString();
			tmpWhereNoLotUCAL = stringTmpWhereNoLotUCAL.toString();
			// กรณีต้องการเก็บ tmpWhereNoLotUCAL ด้วย
			// กรณีนี้ถ้า tmpWhereNoLotUCAL ถูกใช้ต่อที่อื่น ก็ใช้ string ตัวนี้ต่อไป
			// ถ้าเป็น field ระหว่าง method, ต้องเก็บเป็น field หรือ return ค่ากลับ

			if (hasLotNo) {
				whereWaitLot += " and " 
						+ " EXISTS (\r\n"
						+ "   SELECT 1 \r\n"
						+ "   FROM #tempLotNoList l\r\n"
						+ "   WHERE l.LotNo = b.LotNo\r\n"
						+ " )\r\n"
						+ "";
//						+ "( b.LotNo IN (\r\n" + String.join(",", lotNoList) + " ) ) \r\n";
			} else {
				whereWaitLot += " and ( b.UserStatus is not null ) \r\n";
			}

//			whereBMainUserStatus += " and a.SaleOrder <> '' " + tmpWhere.toString();
		}

		// แทนที่ชื่อ field และ alias
//		whereBMainUserStatus = whereBMainUserStatus.replace("UserStatusCalRP", "UserStatus")
//				.replace("UserStatusCal", "UserStatus").replace("UCALRP.", "a.").replace("UCAL.", "a.").replace("b.", "a.");

		whereCaseTry = whereCaseTry.replace("UserStatusCal", "UserStatus").replace("UCALRP.", "a.").replace("UCAL.", "a.")
				.replace("b.", "a.");

		// เก็บค่าใน Map ตามเดิม
		whereClauses.put("whereCaseTry", whereCaseTry);
		whereClauses.put("whereCaseTryRP", whereCaseTryRP);
		whereClauses.put("tmpWhereNoLotUCAL", tmpWhereNoLotUCAL.toString());
		whereClauses.put("whereBase", whereBase);
		whereClauses.put("whereProdFinal", whereProdFinal);
		whereClauses.put("whereSale", whereSale);
		whereClauses.put("whereWaitLot", whereWaitLot);

		return whereClauses;
	}

	public static String buildDateClause(String columnName, String dateRange, String para)
	{
		if (dateRange.isEmpty()) {
			return "";
		}
		String[] dateArray = dateRange.split("-");
		String sqlTemplate = "and (%s.%s >= CONVERT(DATE,'%s',103) and \n" + "           %s.%s <= CONVERT(DATE,'%s',103)) \n";
		return String.format(sqlTemplate, para, columnName, dateArray[0].trim(), para, columnName, dateArray[1].trim());
	}

	private String buildLikeClause(String columnName, String value, String para)
	{
		if (value.isEmpty()) {
			return "";
		}
		return String.format("and %s.%s like '%s%%' \n", para, columnName, value);
	}

	private String buildListClause(String columnName, List<String> list, String para)
	{
		if (list.isEmpty()) {
			return "";
		}
		List<String> escapedList = new ArrayList<>();
		for (String element : list) {
			escapedList.add("'" + element.replaceAll("'", "''") + "'");
		}
		return "and (" + para + "." + columnName + " IN (" + String.join(",", escapedList) + ")) \n";
	}

	private String buildListClauseByArray(String columnName, String[] strings)
	{
		if (strings == null || strings.length == 0) {
			return "";
		}
		List<String> escapedList = new ArrayList<>();
		for (String element : strings) {
			escapedList.add("'" + element.replaceAll("'", "''") + "'");
		}
		return "and (" + columnName + " IN (" + String.join(",", escapedList) + ")) \n";
	}

	private String buildSaleStatusClause(String saleStatus)
	{
		if (saleStatus.isEmpty()) {
			return "";
		}

		String sqlTemplate;
		switch (saleStatus) {
		case "O":
			sqlTemplate = "and (SaleStatus like '%s%%' or ( a.[RemainQuantity] > 0 and SaleStatus <> 'X' )) \n";
			return String.format(sqlTemplate, saleStatus);
		case "X":
			sqlTemplate = "and (SaleStatus like '%s%%') \n";
			return String.format(sqlTemplate, saleStatus);
		case "C":
			sqlTemplate = "and (SaleStatus like '%s%%') \n";
			return String.format(sqlTemplate, saleStatus);
		default:
			sqlTemplate = "and (SaleStatus like '%s%%' or a.[RemainQuantity] = 0) \n";
			return String.format(sqlTemplate, saleStatus);
		}
	}

	public String getLeftJoinPlanCFMLabDate(String aliasProd, String aliasSale)
	{
		// ใช้ #tempPlanCFMLabDate ที่ pre-build ไว้แล้ว แทน inline ROW_NUMBER() subquery
		String sqlTemplate = ""
				+ " LEFT JOIN #tempPlanCFMLabDate AS e\r\n"
				+ "     ON e.[ProductionOrder] = %s.[ProductionOrder]\r\n"
				+ "    AND e.[SaleOrder] = %s.[SaleOrder]\r\n"
				+ "    AND e.[SaleLine] = %s.[SaleLine]\r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale);
	}

	public String getLeftJoinFromSORCFM(String aliasSale)
	{
		String sqlTemplate = " LEFT JOIN #tempFromSORCFM AS J ON J.SaleOrder = %s.SaleOrder AND J.SaleLine = %s.SaleLine\r\n";
		return String.format(sqlTemplate, aliasSale, aliasSale);
	}

	public String getLeftJoinInputReplacedRemark(String aliasProd, String aliasSale)
	{
		String sqlTemplate = " LEFT JOIN #tempInputRR AS K ON K.ProductionOrder = %s.ProductionOrder"
				+ " AND K.SaleOrder = %s.SaleOrder AND K.SaleLine = %s.SaleLine\r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale);
	}

	public String getLeftJoinInputStockLoad(String aliasProd, String aliasSale)
	{
		String sqlTemplate = " LEFT JOIN #tempInputSL AS SL ON SL.ProductionOrder = %s.ProductionOrder"
				+ " AND SL.SaleOrder = %s.SaleOrder AND SL.SaleLine = %s.SaleLine\r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale);
	}

	public String getLeftJoinInputStockRemark(String aliasProd, String aliasSale, String aliasGrade)
	{
		String sqlTemplate = " LEFT JOIN #tempInputStockRemark AS l ON l.ProductionOrder = %s.ProductionOrder"
				+ " AND l.SaleOrder = %s.SaleOrder AND l.SaleLine = %s.SaleLine AND l.Grade = %s.Grade\r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale, aliasGrade);
	}

	public String getLeftJoinInputPCRemark(String aliasProd, String aliasSale)
	{
		String sqlTemplate = " LEFT JOIN #tempInputPCR AS P ON P.ProductionOrder = %s.ProductionOrder"
				+ " AND P.SaleOrder = %s.SaleOrder AND P.SaleLine = %s.SaleLine\r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale);
	}

	public String getLeftJoinInputSwitchRemark(String aliasProd)
	{
		String sqlTemplate = " left join ( \r\n"
				+ "    SELECT ProductionOrder, SwitchRemark \r\n"
				+ "    FROM [PCMS].[dbo].InputSwitchRemark \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS q on %s.ProductionOrder = q.ProductionOrder \r\n";
		return String.format(sqlTemplate, aliasProd);
	}

	public String getLeftJoinInputCauseOfDelay(String aliasProd)
	{
		String sqlTemplate = " left join ( \r\n"
				+ "    SELECT ProductionOrder, CauseOfDelay \r\n"
				+ "    FROM [PCMS].[dbo].[InputCauseOfDelay] \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS InputCOD on %s.ProductionOrder = InputCOD.ProductionOrder \r\n";
		return String.format(sqlTemplate, aliasProd);
	}

	public String getLeftJoinInputDelayedDep(String aliasProd)
	{
		String sqlTemplate = " left join ( \r\n"
				+ "    SELECT ProductionOrder, DelayedDep \r\n"
				+ "    FROM [PCMS].[dbo].[InputDelayedDep] \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS InputDD on %s.ProductionOrder = InputDD.ProductionOrder \r\n";
		return String.format(sqlTemplate, aliasProd);
	}

	public String getLeftJoinSimpleTable(String aliasProd, String tableName, String alias, String selectFields, String joinField,
			String joinAliasField)
	{
		// route pre-built Input* tables to their temp table versions
		if (tableName.equals("InputCauseOfDelay")) {
			return String.format(" LEFT JOIN #tempInputCOD AS InputCOD ON %s.ProductionOrder = InputCOD.ProductionOrder\r\n", aliasProd);
		}
		if (tableName.equals("InputDelayedDep")) {
			return String.format(" LEFT JOIN #tempInputDD AS InputDD ON %s.ProductionOrder = InputDD.ProductionOrder\r\n", aliasProd);
		}
		if (tableName.equals("InputSwitchRemark")) {
			return String.format(" LEFT JOIN #tempInputSR AS q ON %s.ProductionOrder = q.ProductionOrder\r\n", aliasProd);
		}
		String sqlTemplate = " left join ( \r\n"
				+ "    SELECT %s \r\n"
				+ "    FROM [PCMS].[dbo].%s \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS %s on %s.%s = %s.%s \r\n";
		return String.format(sqlTemplate, selectFields, tableName, alias, aliasProd, joinField, alias, joinAliasField);
	}

	public String getLeftJoinTAPP(String aliasProd)
	{
		return String.format(" LEFT JOIN #tempTAPP AS TAPP ON TAPP.ProductionOrder = %s.ProductionOrder\r\n", aliasProd);
	}

	public String getLeftJoinTempSumBill(String aliasProd, String aliasSale, String aliasGrade)
	{
		String sqlTemplate = " left join #tempSumBill AS FSMBB ON FSMBB.ProductionOrder = %s.ProductionOrder \r\n"
				+ "    AND FSMBB.SaleOrder = %s.SaleOrder \r\n"
				+ "    AND FSMBB.SaleLine = %s.SaleLine \r\n"
				+ "    AND FSMBB.Grade = %s.Grade \r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale, aliasGrade);
	}

	public String getLeftJoinCRP(String aliasSale)
	{
//		String sqlTemplate = " "
//				+ " LEFT JOIN (\r\n"
//				+ "     SELECT   a.SaleOrder, a.SaleLine \r\n"
//				+ "     FROM [PCMS].[dbo].[ReplacedProdOrder] as a \r\n"
//				+ "     LEFT JOIN [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrderRP = b.ProductionOrder \r\n"
//				+ this.buildInnerJoinViewUSM_SPE("b", 1)
//				+ "     WHERE a.DataStatus = 'O' \r\n"
//				+ "     GROUP BY a.SaleOrder, a.SaleLine \r\n"
//				+ ") AS CRP on CRP.SaleOrder = %s.SaleOrder \r\n"
//				+ "        AND CRP.SaleLine = %s.SaleLine \r\n";
		String sqlTemplate = ""
				+ " LEFT JOIN #tmpCRP CRP ON CRP.SaleOrder = %s.SaleOrder \r\n"
				+ "                      AND CRP.SaleLine  = %s.SaleLine ";
		return String.format(sqlTemplate, aliasSale, aliasSale);
	}

	public String getLeftJoinSwitchProdOrder(String aliasProd, String aliasJoinField)
	{
		String sqlTemplate = " "
				+ " left join ( \r\n"
				+ "    SELECT ProductionOrder, ProductionOrderSW \r\n"
				+ "    FROM [PCMS].[dbo].[SwitchProdOrder] \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS R on %s.ProductionOrder = R.%s \r\n";
		return String.format(sqlTemplate, aliasProd, aliasJoinField);
	}

	public String getLeftJoinSwitchProdOrder(String aliasProd)
	{
		return String.format(" LEFT JOIN #tempSPO AS SPO ON SPO.ProductionOrderSW = %s.ProductionOrder\r\n", aliasProd);
	}

	public String getLeftJoinTempPlandeliveryDate(String aliasProd, String aliasSale)
	{
		String sqlTemplate = ""
				+ " left join #tempPlandeliveryDate as h on h.ProductionOrder = %s.ProductionOrder and\r\n"
				+ "                                         h.SaleOrder = %s.SaleOrder and\r\n"
				+ "							                h.SaleLine = %s.SaleLine\r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale);
	}

	public String buildLeftJoinUserStatusAuto(String aliasTable, String aliasProd, String aliasGrade)
	{
		String sqlTemplate = " "
				+ "     LEFT JOIN [PCMS].[dbo].[TEMP_UserStatusAuto] AS %s \n"
				+ "     ON %s.[DataStatus] = 'O' \n"
				+ "     AND %s.ProductionOrder = %s.ProductionOrder \n"
				+ "     AND (%s.Grade = %s.Grade OR %s.Grade IS NULL) \n"
//				+ "     AND ISNULL(%s.Grade, 'N/A') = ISNULL(%s.Grade, 'N/A')"
				;

		return String.format(sqlTemplate, aliasTable, aliasTable, aliasProd, aliasTable, aliasGrade, aliasTable, aliasGrade);
	}
	public String buildLeftJoinViewUserStatusMappingPCMS(String aliasTableJoin ,String aliasUserStatus,int specialCon)
	{
		String sqlTemplate = "\r\n"
				+ " LEFT JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] AS viewUSM_SPE 	ON \r\n"
				+ "	viewUSM_SPE.UserStatus = %s.%s    \r\n"
				+ "	and viewUSM_SPE.[Special] = %s \n";

		return String.format(sqlTemplate, aliasTableJoin,aliasUserStatus,specialCon );
	}
	public String buildLeftJoinSCC(String aliasProd)
	{
		return String.format(" LEFT JOIN #tempSCC AS SCC ON SCC.ProductionOrder = %s.ProductionOrder\n", aliasProd);
	}

	public String buildLeftJoinTempSumGR(String aliasMain)
	{
		return String.format(" LEFT JOIN #tempSumGR AS m ON %s.ProductionOrder = m.ProductionOrder \n", aliasMain);
	}

	public String buildLeftJoinTempProdWorkDate(String aliasMain)
	{
		return String.format(" LEFT JOIN #tempProdWorkDate AS g ON g.ProductionOrder = %s.ProductionOrder\n", aliasMain);
	}

	public String buildInnerJoinFromSapMainProd(String aliasJoinTableAs, String fieldJoinTableAs, String aliasTableMainJoin,
			String fieldMainTableAs)
	{
		String sqlTemplate = " INNER JOIN [PCMS].[dbo].[FromSapMainProd] AS %s \n" + "     ON %s.%s = %s.%s \n";

		return String.format(sqlTemplate, aliasJoinTableAs, aliasJoinTableAs, fieldJoinTableAs, aliasTableMainJoin,
				fieldMainTableAs
//	    		, aliasJoinTableAs
		);
	}

	// buildInnerJoinViewUSM_SPE ถูกลบ 2026-07-03 — ทุก caller ย้ายไปใช้ buildInnerJoinTempUSMSpecial1
	// (join #tempUSMSpecial1 ที่ pre-materialize จาก viewUserStatusMappingPCMS WHERE Special=1)

//	public String buildLeftJoinViewUSM_SPE(String aliasMain, int specialCon)
//	{
//		return String.format(" "
//				+ "	LEFT JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] AS viewUSM_SPE "
//				+ "		ON viewUSM_SPE.UserStatus = %s.[UserStatus] "
//				+ "     and viewUSM_SPE.[Special] = %s \n", aliasMain, specialCon);
//	}

	public String buildIfTempTableDrop(String tempName)
	{
		String sqlTemplate =
				"" + " If(OBJECT_ID('tempdb..%s') Is Not Null)\r\n" + "	begin\r\n" + "		Drop Table %s\r\n" + "	end ;\r\n";
		return String.format(sqlTemplate, tempName, tempName);
	}

	/**
	 * Common lookup preamble ที่ getPCMSSumaryDetail (PCMSMainDaoImpl) และ searchByDetail
	 * (PCMSDetailDaoImpl) ใช้ร่วมกัน — นิยาม temp set ที่เดียว ลำดับ fragment ห้ามสลับ
	 * (#tempMainSale ต้องมาก่อน fragment ที่ filter ด้วยมัน)
	 *
	 * sumGRFragment: ส่ง createTempSumGR หรือ createTempSumGRFiltered ตาม caller
	 * (ตอนนี้ Detail ใช้ Filtered แล้ว ส่วน Main ยังใช้ตัวเต็ม — จูน perf แยกกัน)
	 * ส่วน fragment เฉพาะของแต่ละ method ให้ caller ต่อท้ายเอง
	 */
	public String buildCommonLookupTables(String createUserStatus, String createCusList,
			String whereSale, String sumGRFragment)
	{
		return createUserStatus + createCusList
				+ createTempMainSaleWithJoinCustomer + whereSale
				+ createTempMainSaleIndex
				+ createTempPlanDeliveryDate
				+ sumGRFragment
				+ createTempSumBill
				+ createTempSCC
				+ createTempProdWorkDateFiltered;   // filtered by #tempMainSale — was full scan
	}

}