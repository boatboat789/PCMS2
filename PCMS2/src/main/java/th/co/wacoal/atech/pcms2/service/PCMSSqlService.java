package th.co.wacoal.atech.pcms2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

@Service
public class PCMSSqlService {
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
	public String createDropTempForMainAndWaitLot = ""
			+ this.buildIfTempTableDrop("#BillBatchFlag") 
			+ this.buildIfTempTableDrop("#BillBatchFlag") 
			+ this.buildIfTempTableDrop("#tmpSumVolOP") 
			+ this.buildIfTempTableDrop("#tmpSumVolRP") 
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
	public String fromProdA = "" + " from ProdData as a \r\n";
	public String createTempSumGR = ""
			+ this.buildIfTempTableDrop("#tempSumGR")
			+ " SELECT *\r\n"
			+ " into #tempSumGR\r\n"
			+ " FROM [PCMS].[dbo].SumGRCache  ;\r\n";
	public String createTempSumBill = ""
			+ "  If(OBJECT_ID('tempdb..#tempSumBill') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempSumBill\r\n"
			+ "	end ;\r\n"
			+ " SELECT *\r\n"
			+ " into #tempSumBill\r\n"
			+ " FROM [PCMS].dbo.SumBillCache ; \r\n"; 
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
			+ "	from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
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
			+ "	where  A.[DataStatus] = 'O' AND \r\n"
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
			List<String> userStatusCalRPList = groups.userStatusCalRPList;
			List<String> userStatusCalList = groups.userStatusCalList;
			List<String> userStatusListA = groups.userStatusListA;
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
		String sqlTemplate = ""
				+ " LEFT JOIN ( \r\n"
				+ "		SELECT \r\n"
				+ "    	[ProductionOrder],\r\n"
				+ "    	[SaleOrder],\r\n"
				+ "    	[SaleLine],\r\n"
				+ "    	[PlanDate] AS CFMPlanLabDate\r\n"
				+ "		FROM (\r\n"
				+ "    		SELECT \r\n"
				+ "        		*,\r\n"
				+ "        		ROW_NUMBER() OVER (\r\n"
				+ "            PARTITION BY [ProductionOrder], [SaleOrder], [SaleLine] \r\n"
				+ "            ORDER BY [CreateDate] DESC\r\n"
				+ "        		) AS rn\r\n"
				+ "    		FROM [PCMS].[dbo].[PlanCFMLabDate]\r\n"
				+ "		) AS ranked\r\n"
				+ "		WHERE rn = 1"
				+ " ) as e on e.[ProductionOrder] = %s.[ProductionOrder] and \r\n"
				+ "           e.[SaleOrder] = %s.[SaleOrder] and\r\n"
				+ "           e.[SaleLine] = %s.[SaleLine]\r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale);
	}

	public String getLeftJoinFromSORCFM(String aliasSale)
	{
		String sqlTemplate = ""
				+ " left join ( \r\n"
				+ "    SELECT SALEORDER,SALELINE,CFMDATE \r\n"
				+ "	   FROM [PCMS].[dbo].[FromSORCFM]\r\n"
				+ " )AS J  on %s.SaleOrder = J.SaleOrder and \r\n"
				+ "           %s.SaleLine = J.SaleLine \r\n ";
		return String.format(sqlTemplate, aliasSale, aliasSale);
	}

	public String getLeftJoinInputReplacedRemark(String aliasProd, String aliasSale)
	{
		String sqlTemplate = ""
				+ " left join (\r\n"
				+ "     SELECT SALELINE,SALEORDER,ProductionOrder,ReplacedRemark \r\n"
				+ "     FROM [PCMS].[dbo].[InputReplacedRemark] \r\n"
				+ "     WHERE DataStatus = 'O'\r\n"
				+ " ) AS K on K.ProductionOrder = %s.ProductionOrder and \r\n"
				+ "           K.SaleOrder = %s.SaleOrder and\r\n"
				+ "           K.SaleLine = %s.SaleLine \r\n ";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale);
	}

	public String getLeftJoinInputStockLoad(String aliasProd, String aliasSale)
	{
		String sqlTemplate = " left join ( \r\n"
				+ "    SELECT SALELINE, SALEORDER, ProductionOrder, StockLoad \r\n"
				+ "    FROM [PCMS].[dbo].InputStockLoad \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS SL on SL.ProductionOrder = %s.ProductionOrder and \r\n"
				+ "           SL.SaleOrder = %s.SaleOrder and \r\n"
				+ "           SL.SaleLine = %s.SaleLine \r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale);
	}

	public String getLeftJoinInputStockRemark(String aliasProd, String aliasSale, String aliasGrade)
	{
		String sqlTemplate = " left join ( \r\n"
				+ "    SELECT SALELINE, SALEORDER, ProductionOrder, Grade, StockRemark \r\n"
				+ "    FROM [PCMS].[dbo].[InputStockRemark] \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS l on l.ProductionOrder = %s.ProductionOrder and \r\n"
				+ "         l.SaleOrder = %s.SaleOrder and \r\n"
				+ "         l.SaleLine = %s.SaleLine  and \r\n"
				+ "         l.Grade = %s.Grade \r\n";
		return String.format(sqlTemplate, aliasProd, aliasSale, aliasSale, aliasGrade);
	}

	public String getLeftJoinInputPCRemark(String aliasProd, String aliasSale)
	{
		String sqlTemplate = " left join ( \r\n"
				+ "    SELECT SALELINE, SALEORDER, ProductionOrder, PCRemark \r\n"
				+ "    FROM [PCMS].[dbo].InputPCRemark \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS P on P.ProductionOrder = %s.ProductionOrder and \r\n"
				+ "         P.SaleOrder = %s.SaleOrder and \r\n"
				+ "         P.SaleLine = %s.SaleLine   \r\n";
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
		String sqlTemplate = " left join ( \r\n"
				+ "    SELECT %s \r\n"
				+ "    FROM [PCMS].[dbo].%s \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS %s on %s.%s = %s.%s \r\n";
		return String.format(sqlTemplate, selectFields, tableName, alias, aliasProd, joinField, alias, joinAliasField);
	}

	public String getLeftJoinTAPP(String aliasProd)
	{
		String sqlTemplate = " left join ( \r\n"
				+ "    select a.ProductionOrder, b.SORCFMDate, b.SORDueDate \r\n"
				+ "    from [PPMM].[dbo].[SOR_TempProd] as a \r\n"
				+ "    inner join [PPMM].[dbo].[ApprovedPlanDate] as b on a.POId = b.POId \r\n"
				+ "    WHERE a.DataStatus = 'O' and b.DataStatus = 'O' \r\n"
				+ ") AS TAPP on TAPP.ProductionOrder = %s.ProductionOrder \r\n";
		return String.format(sqlTemplate, aliasProd);
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
		String sqlTemplate = " "
				+ " LEFT JOIN [PCMS].[dbo].[SwitchProdOrder] \r\n"
				+ "AS SPO on SPO.ProductionOrderSW = %s.ProductionOrder"
				+ "      AND SPO.DataStatus = 'O' \r\n";
		return String.format(sqlTemplate, aliasProd);
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
		String sqlTemplate = " LEFT JOIN [PCMS].[dbo].[PlanSendCFMCusDate] AS SCC\n"
				+ "     ON SCC.ProductionOrder = %s.ProductionOrder\n"
				+ "     AND SCC.DataStatus = 'O'\n";
		return String.format(sqlTemplate, aliasProd);
	}

	public String buildLeftJoinTempSumGR(String aliasMain)
	{
		return String.format(" LEFT JOIN #tempSumGR AS m ON %s.ProductionOrder = m.ProductionOrder \n", aliasMain);
	}

	public String buildLeftJoinTempProdWorkDate(String aliasMain)
	{
		return String.format(" LEFT JOIN [PCMS].[dbo].[TEMP_ProdWorkDate] AS g ON g.ProductionOrder = %s.ProductionOrder \n",
				aliasMain);
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

	public String buildInnerJoinViewUSM_SPE(String aliasMain, int specialCon,String aliasUserStatus )
	{
		return String.format(" "
				+ "	INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] AS viewUSM_SPE \r\n"
				+ "		ON %s.%s = viewUSM_SPE.UserStatus \n"
				+ "     and viewUSM_SPE.[Special] = %s  \n", aliasMain, aliasUserStatus, specialCon);
	}

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

}