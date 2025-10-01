package th.co.wacoal.atech.pcms2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

@Service
public class PCMSSearchService {
	public static class UserStatusGroups {
	    public List<String> lotNoList = new ArrayList<>();
	    public List<String> userStatusCalRPList = new ArrayList<>();
	    public List<String> userStatusCalList = new ArrayList<>();
	    public List<String> userStatusListA = new ArrayList<>();
	}
	private UserStatusGroups categorizeUserStatus(List<String> statuses) {
	    UserStatusGroups groups = new UserStatusGroups();
	    for (String text : statuses) {
	        String safeText = "'" + text.replace("'", "''") + "'";
	        if (text.equals("รอจัด Lot") || text.equals("ขาย stock") || text.equals("รับจ้างถัก")
	            || text.equals("Lot ขายแล้ว") || text.equals("พ่วงแล้วรอสวม") || text.equals("รอสวมเคยมี Lot")) {
	            groups.lotNoList.add(safeText);
	        } else {
	            groups.userStatusCalRPList.add(safeText);
	            groups.userStatusCalList.add(safeText);
	            groups.userStatusListA.add(safeText);
	        }
	    }
	    return groups;
	}
	public  String withProdData = ""
			+ "; WITH ProdData AS (\r\n"
			+ "    SELECT \r\n"
			+ "        a.*\r\n"
			+ "			,calc.ProdOrderValue AS [ProductionOrder] \r\n"
			+ "			,calc.LotNoValue     AS [LotNo] "
//			+ "        ,CASE WHEN b.[ProductionOrder] IS NOT NULL THEN b.[ProductionOrder]  \r\n"
//			+ "             WHEN SUBSTRING(a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//			+ "             WHEN EXISTS (SELECT 1 FROM [PCMS].[dbo].[FromSapMainBillBatch] \r\n"
//			+ "                          WHERE DataStatus = 'O' AND [SaleOrder] = a.[SaleOrder] AND [SaleLine] = a.[SaleLine]) THEN 'Lot ขายแล้ว'\r\n"
//			+ "             WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
//			+ "             ELSE 'รอจัด Lot'  \r\n"
//			+ "        END AS [ProductionOrder]  \r\n"
//			+ "		, CASE  \r\n"
//			+ "			WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
//			+ "			WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
//			+ "			WHEN EXISTS (SELECT 1 FROM [PCMS].[dbo].[FromSapMainBillBatch] \r\n"
//			+ "            WHERE DataStatus = 'O' AND [SaleOrder] = a.[SaleOrder] AND [SaleLine] = a.[SaleLine]) THEN 'Lot ขายแล้ว'\r\n"
//			+ "			WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
//			+ "			ELSE 'รอจัด Lot'  \r\n"
//			+ "			END AS [LotNo]    \r\n"
//			+ "					, b.[TotalQuantity]\r\n"
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
			+ "    FROM #tempMainSale a\r\n"
			+ "    LEFT JOIN [PCMS].[dbo].[FromSapMainProd] b ON a.SaleOrder = b.SaleOrder AND a.SaleLine = b.SaleLine\r\n"
			+ "    CROSS APPLY (\r\n"
			+ "    SELECT \r\n"
			+ "        CASE \r\n"
			+ "            WHEN b.[ProductionOrder] IS NOT NULL THEN b.[ProductionOrder]\r\n"
			+ "            WHEN SUBSTRING(a.[MaterialNo], 1, 1) = 'V' THEN N'รับจ้างถัก'\r\n"
			+ "            WHEN EXISTS (\r\n"
			+ "                SELECT 1 \r\n"
			+ "                FROM [PCMS].[dbo].[FromSapMainBillBatch] bb\r\n"
			+ "                WHERE bb.DataStatus = 'O' \r\n"
			+ "                  AND bb.[SaleOrder] = a.[SaleOrder] \r\n"
			+ "                  AND bb.[SaleLine] = a.[SaleLine]\r\n"
			+ "            ) THEN N'Lot ขายแล้ว'\r\n"
			+ "            WHEN a.[SaleStatus] = 'C' THEN N'ขาย stock'\r\n"
			+ "            ELSE N'รอจัด Lot'\r\n"
			+ "        END AS ProdOrderValue,\r\n"
			+ "        CASE \r\n"
			+ "            WHEN b.[ProductionOrder] IS NOT NULL THEN b.[LotNo]\r\n"
			+ "            WHEN SUBSTRING(a.[MaterialNo], 1, 1) = 'V' THEN N'รับจ้างถัก'\r\n"
			+ "            WHEN EXISTS (\r\n"
			+ "                SELECT 1 \r\n"
			+ "                FROM [PCMS].[dbo].[FromSapMainBillBatch] bb\r\n"
			+ "                WHERE bb.DataStatus = 'O' \r\n"
			+ "                  AND bb.[SaleOrder] = a.[SaleOrder] \r\n"
			+ "                  AND bb.[SaleLine] = a.[SaleLine]\r\n"
			+ "            ) THEN N'Lot ขายแล้ว'\r\n"
			+ "            WHEN a.[SaleStatus] = 'C' THEN N'ขาย stock'\r\n"
			+ "            ELSE N'รอจัด Lot'\r\n"
			+ "        END AS LotNoValue\r\n"
			+ ") AS calc "
			+ ") ";
	public  String crossApplyVolCalc = ""
			+ "\r\n"
			+ "	CROSS APPLY (\r\n"
			+ "		SELECT \r\n"
			+ "			CASE \r\n"
			+ "				WHEN s.SumVolRP IS NOT NULL AND t.SumVolOP IS NOT NULL THEN a.Volumn - s.SumVolRP - t.SumVolOP\r\n"
			+ "				WHEN s.SumVolRP IS NOT NULL AND t.SumVolOP IS NULL THEN a.Volumn - s.SumVolRP\r\n"
			+ "				WHEN s.SumVolRP IS NULL AND t.SumVolOP IS NOT NULL THEN a.Volumn - t.SumVolOP\r\n"
			+ "				WHEN a.Volumn IS NOT NULL THEN a.Volumn\r\n"
			+ "				ELSE 0\r\n"
			+ "			END AS adjVol\r\n"
			+ "	) AS volCalc ";   
	public String fromProdA = "" + " from ProdData as a ";
	public String createTempSumGR = ""
			+ " If(OBJECT_ID('tempdb..#tempSumGR') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempSumGR\r\n"
			+ "	end ;\r\n"
			+ " SELECT "
			+ "		[ProductionOrder] ,[Grade] \r\n"
			+ "		,sum([QuantityMR]) as GRSumMR\r\n"
			+ "	 	,sum([QuantityKG]) as GRSumKG\r\n"
			+ "	  	,sum([QuantityYD]) as GRSumYD\r\n "
			+ " into #tempSumGR \r\n"
			+ " FROM [PCMS].[dbo].[FromSapGoodReceive]  WITH (NOLOCK)\r\n"
			+ "	where datastatus = 'O'\r\n"
			+ "	GROUP BY ProductionOrder,Grade \r\n";
	public String createTempSumBill = ""
			+ " If(OBJECT_ID('tempdb..#tempSumBill') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempSumBill\r\n"
			+ "	end ;\r\n"
			+ " SELECT \r\n"
			+ " 	 [ProductionOrder] \r\n"
			+ "    , [SaleOrder] \r\n"
			+ "    , [SaleLine]\r\n"
			+ "    , [Grade]\r\n"
			+ "    , SUM([QuantityKG]) AS [BillSendWeightQuantity] \r\n"
			+ "    , SUM([QuantityYD]) AS [BillSendYDQuantity] \r\n"
			+ "    , SUM([QuantityMR]) AS [BillSendMRQuantity] \r\n"
			+ " into #tempSumBill \r\n"
			+ " FROM [PCMS].[dbo].[FromSapMainBillBatch]  WITH (NOLOCK)\r\n"
			+ " where DataStatus = 'O'\r\n"
			+ " GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[Grade] \r\n";
	public String createTempMainSale = ""
			+ " If(OBJECT_ID('tempdb..#tempMainSale') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempMainSale\r\n"
			+ "	end ; "
			+ " SELECT   \r\n"
			+ "	   a.*\r\n"
			+ "	  ,a.[Division] AS CustomerDivision\r\n"
			+ " INTO #tempMainSale \r\n"
			+ " FROM [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			+ " left join [PCMS].[dbo].[ConfigCustomerEX] as b on a.[CustomerNo] = b.[CustomerNo] and b.[DataStatus] = 'O'\r\n";
	public String createTempMainSaleWithJoinCustomer = ""
			+ " If(OBJECT_ID('tempdb..#tempMainSale') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempMainSale\r\n"
			+ "	end ; "
			+ " SELECT   \r\n"
			+ "	   a.*\r\n"
			+ "	  ,a.[Division] AS CustomerDivision\r\n"
			+ " INTO #tempMainSale \r\n"
			+ " FROM [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			+ " INNER JOIN #tempCustomerList AS c\r\n"
			+ "    ON a.CustomerName = c.CustomerName  COLLATE Thai_100_CI_AS"
			+ " INNER JOIN #tempCustomerShortList AS d\r\n"
			+ "    ON a.CustomerShortName = d.CustomerShortName  COLLATE Thai_100_CI_AS"
			+ " left join [PCMS].[dbo].[ConfigCustomerEX] as b on a.[CustomerNo] = b.[CustomerNo] and b.[DataStatus] = 'O'\r\n";

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
			+ "WHERE rn = 1;\r\n";
	public String leftJoinBPartOneT_A = ""
			+ "			left join ( \r\n"
			+ "				SELECT "
			+ "					a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
			+ "				from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
			+ "			   	left join [PCMS].[dbo].[FromSapMainProd] as b "
			+ "					on a.ProductionOrder = b.ProductionOrder  \r\n"
			+ "                "+this.buildInnerJoinViewUSM_SPE("b",1)
			+ "			   	WHERE a.[DataStatus] = 'O'  \r\n"
			+ "			   	group by a.ProductionOrder\r\n"
			+ "			) as t "
			+ "				on a.ProductionOrder = t.ProductionOrder\r\n";
	public String leftJoinBPartOneS_A = ""
			+ "			left join (\r\n"
			+ "             SELECT \r\n"
			+ "    				a.ProductionOrderRP,  \r\n"
			+ "    				SUM(CASE WHEN a.Volume = 0 THEN b.Volumn ELSE a.Volume END) AS SumVolRP  \r\n"
			+ "				FROM [PCMS].[dbo].[ReplacedProdOrder] AS a  \r\n"
			+ "				LEFT JOIN [PCMS].[dbo].[FromSapMainProd] AS b"
			+ "					ON a.ProductionOrderRP = b.ProductionOrder  \r\n"
			+ "                "+this.buildInnerJoinViewUSM_SPE("b",1)
			+ "				WHERE a.[DataStatus] = 'O'  \r\n"
			+ "				GROUP BY a.ProductionOrderRP \r\n"
			+ "			) as s "
			+ "				on a.ProductionOrder = s.ProductionOrderRP  \r\n";
	public String innerJoinWaitLotB = ""
			+ " INNER JOIN (\r\n"
			+ "	SELECT DISTINCT "
			+ "		a.saleorder "
			+ "		, a.saleline"
			+ "		, c.SumVolMain "
			+ "		,b.SumVolUsed\r\n"
			+ "		,CASE  \r\n"
			+ " 			WHEN COALESCE(c.SumVolMain, 0 ) >  b.SumVolUsed THEN 'A'\r\n"
			+ "			WHEN COALESCE(c.SumVolMain, 0 ) <=  b.SumVolUsed THEN 'B' \r\n"
			+ "			ELSE  'C'\r\n"
			+ "	 		END AS SumVol \r\n"
			+ "		,'รอจัด Lot' as ProductionOrder\r\n"
			+ "		,CASE  \r\n"
			+ " 			WHEN COALESCE( SumVolOP, 0 ) >  0 THEN 'พ่วงแล้วรอสวม'\r\n"
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
			+ "		,cast(null as NVARCHAR) as CFMRemarkAll  \r\n"
			+ "	from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
			+ "	left join (\r\n"
			+ "        SELECT DISTINCT \r\n"
			+ "         A.SaleOrder\r\n"
			+ "        , A.SaleLine\r\n"
			+ "        , COALESCE(SumVolOP, 0 ) + COALESCE(SumVolRP, 0 ) as SumVolUsed --,COALESCE(SumVolRP, 0 )\r\n"
			+ "	       , SumVolOP\r\n"
			+ "        , SumVolRP\r\n"
			+ "		FROM[PCMS].[dbo].[FromSapMainProd]  AS A\r\n"
			+ this.leftJoinBPartOneT_A
			+ this.leftJoinBPartOneS_A
			+ "	)  as b on a.SaleOrder = b.SaleOrder and\r\n"
			+ "               a.SaleLine = b.SaleLine\r\n"
			+ "	LEFT JOIN ( \r\n"
			+ "		SELECT [SaleOrder] ,[SaleLine]  ,sum( [Volumn] ) as SumVolMain \r\n"
			+ "		FROM [PCMS].[dbo].[FromSapMainProd]\r\n"
			+ "		WHERE DataStatus = 'O'\r\n"
			+ "		group by  [SaleOrder]  ,[SaleLine]\r\n"
			+ "    ) AS C ON A.SaleOrder = C.SaleOrder AND A.SaleLine = C.SaleLine \r\n"
			+ "	left join (\r\n"
			+ "     select DISTINCT a.SaleOrder ,a.SaleLine ,1 AS CountProdRP\r\n"
			+ "		from [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
			+ "		inner join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrderRP = b.ProductionOrder\r\n"
			+ "                "+this.buildInnerJoinViewUSM_SPE("b",1)
			+ "		where a.DataStatus = 'O' and\r\n"
			+ "              a.ProductionOrder = 'รอจัด Lot' \r\n"
			+ "		GROUP BY a.SaleOrder ,a.SaleLine \r\n"
			+ "	) AS D ON A.SaleOrder = D.SaleOrder AND \r\n"
			+ "             A.SaleLine = D.SaleLine  \r\n"
			+ "	where  A.[DataStatus] = 'O' AND \r\n"
			+ "          c.SumVolMain > 0 OR ( c.SumVolMain is null AND D.SaleOrder IS NOT NULL )\r\n"
			+ " ) AS b ON a.SaleOrder = b.SaleOrder and\r\n"
			+ "           a.SaleLine = b.SaleLine \r\n";

	public Map<String, String> buildWhereClauses(PCMSTableDetail bean)
	{
		Map<String, String> whereClauses = new HashMap<>();
		String where = "where 1 = 1";
		String whereSale = " where A.[DataStatus] = 'O' AND 1 = 1";
		String whereWaitLot = " where 1 = 1 ";
		String whereBMainUserStatus = " where 1 = 1";
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
//		List<String> cusNameList = bean.getCustomerNameList();
//		List<String> cusShortNameList = bean.getCustomerShortNameList();
		List<String> divisionList = bean.getDivisionList();
		// Build where clauses
		where += buildLikeClause("MaterialNo", materialNo, "a");
		whereSale += buildLikeClause("MaterialNo", materialNo, "a");
		where += buildLikeClause("SaleOrder", saleOrder, "a");
		whereSale += buildLikeClause("SaleOrder", saleOrder, "a");
		where += buildDateClause("SaleCreateDate", saleCreateDate, "a");
		whereSale += buildDateClause("SaleCreateDate", saleCreateDate, "a");
		where += buildListClause("Division", divisionList, "a");
		whereSale += buildListClause("Division", divisionList, "a");
		where += buildLikeClause("PurchaseOrder", po, "a");
		whereSale += buildLikeClause("PurchaseOrder", po, "a");
		where += buildLikeClause("SaleNumber", saleNumber, "a");
		whereSale += buildLikeClause("SaleNumber", saleNumber, "a");
		where += buildLikeClause("ArticleFG", articleFG, "a");
		whereSale += buildLikeClause("ArticleFG", articleFG, "a");
		where += buildLikeClause("DesignFG", designFG, "a");
		whereSale += buildLikeClause("DesignFG", designFG, "a");
		where += buildDateClause("DueDate", dueDate, "a");
		whereSale += buildDateClause("DueDate", dueDate, "a");
		where += buildLikeClause("DeliveryStatus", deliveryStatus, "a");
		whereSale += buildLikeClause("DeliveryStatus", deliveryStatus, "a");
		where += buildSaleStatusClause(saleStatus);
		whereSale += buildSaleStatusClause(saleStatus);
		where += buildListClauseByArray("DistChannel", distChannel.split("\\|"));
		whereSale += buildListClauseByArray("DistChannel", distChannel.split("\\|"));

		// Production order conditions
		where += buildLikeClause("LabNo", labNo, "b");
		whereProd += buildLikeClause("LabNo", labNo, "b");
		where += buildLikeClause("ProductionOrder", prdOrder, "b");
		whereProd += buildLikeClause("ProductionOrder", prdOrder, "b");

		where += buildDateClause("PrdCreateDate", prdCreateDate, "b");
		whereProd += buildDateClause("PrdCreateDate", prdCreateDate, "b");
		whereWaitLot = where;
		whereCaseTry = whereProd;
		whereCaseTryRP = whereProd;
		whereBMainUserStatus = whereProd;

		if (userStatusList.size() > 0) {
//			List<String> lotNoList = new ArrayList<>();
//			List<String> userStatusCalRPList = new ArrayList<>();
//			List<String> userStatusCalList = new ArrayList<>();
//			List<String> userStatusListA = new ArrayList<>();
		    UserStatusGroups groups = categorizeUserStatus(userStatusList);
		    List<String> lotNoList = groups.lotNoList;
		    List<String> userStatusCalRPList = groups.userStatusCalRPList;
		    List<String> userStatusCalList = groups.userStatusCalList;
		    List<String> userStatusListA = groups.userStatusListA;
//			for (String text : userStatusList) {
//				String safeText = "'" + text.replace("'", "''") + "'";
//				if (text.equals("รอจัด Lot") || text.equals("ขาย stock") || text.equals("รับจ้างถัก")
//						|| text.equals("Lot ขายแล้ว") || text.equals("พ่วงแล้วรอสวม") || text.equals("รอสวมเคยมี Lot")) {
//					lotNoList.add(safeText);
//				} else {
//					userStatusCalRPList.add(safeText);
//					userStatusCalList.add(safeText);
//					userStatusListA.add(safeText);
//				}
//			}

			StringBuilder tmpWhere = new StringBuilder(" and ( b.ProductionOrder is not null and ( \r\n");
			StringBuilder stringTmpWhereNoLotUCAL = new StringBuilder(" and ( b.ProductionOrder is not null and ( \r\n");
			StringBuilder whereCaseTryRPBuilder = new StringBuilder(whereCaseTryRP + " and ( b.ProductionOrder is not null and ( \r\n");
			StringBuilder whereCaseTryBuilder = new StringBuilder(whereCaseTry + " and ( a.ProductionOrder is not null and ( \r\n");

			boolean hasLotNo = ! lotNoList.isEmpty();
			boolean hasUserStatus = ! userStatusCalList.isEmpty();

			if (hasLotNo) {
				String lotNoInClause = "b.LotNo IN (" + String.join(",", lotNoList) + ")";
				tmpWhere.append(lotNoInClause);
				whereCaseTryRPBuilder.append(lotNoInClause);
				whereCaseTryBuilder.append(lotNoInClause);
			}

			if (hasUserStatus) {
				String userStatusCalIn = "UCAL.UserStatusCal IN (" + String.join(",", userStatusCalList) + ")";
				String userStatusCalRPIn = "UCALRP.UserStatusCalRP IN (" + String.join(",", userStatusCalRPList) + ")";
				String userStatusAIn = "a.UserStatus IN (" + String.join(",", userStatusListA) + ")";

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

			where += tmpWhere.toString();
			whereCaseTry = whereCaseTryBuilder.toString();
			whereCaseTryRP = whereCaseTryRPBuilder.toString();
			tmpWhereNoLotUCAL = stringTmpWhereNoLotUCAL.toString();
			// กรณีต้องการเก็บ tmpWhereNoLotUCAL ด้วย
			// กรณีนี้ถ้า tmpWhereNoLotUCAL ถูกใช้ต่อที่อื่น ก็ใช้ string ตัวนี้ต่อไป
			// ถ้าเป็น field ระหว่าง method, ต้องเก็บเป็น field หรือ return ค่ากลับ

			if (hasLotNo) {
				whereWaitLot += " and ( b.LotNo IN (\r\n" + String.join(",", lotNoList) + " ) ) \r\n";
			} else {
				whereWaitLot += " and ( b.UserStatus is not null ) \r\n";
			}

			whereBMainUserStatus += " and a.SaleOrder <> '' " + tmpWhere.toString();
		}

		// แทนที่ชื่อ field และ alias
		whereBMainUserStatus = whereBMainUserStatus.replace("UserStatusCalRP", "UserStatus")
				.replace("UserStatusCal", "UserStatus").replace("UCALRP.", "a.").replace("UCAL.", "a.").replace("b.", "a.");

		whereCaseTry = whereCaseTry.replace("UserStatusCal", "UserStatus").replace("UCALRP.", "a.").replace("UCAL.", "a.")
				.replace("b.", "a.");

		// เก็บค่าใน Map ตามเดิม
		whereClauses.put("whereCaseTry", whereCaseTry);
		whereClauses.put("whereCaseTryRP", whereCaseTryRP);
		whereClauses.put("tmpWhereNoLotUCAL", tmpWhereNoLotUCAL.toString());
		whereClauses.put("where", where);
		whereClauses.put("whereBMainUserStatus", whereBMainUserStatus);
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
		String sqlTemplate = "and (%s.%s >= CONVERT(DATE,'%s',103) and \n"
				+ "           %s.%s <= CONVERT(DATE,'%s',103)) \n";
		return String.format(sqlTemplate,
				para, columnName, dateArray[0].trim(),
				para, columnName, dateArray[1].trim());
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
	public String getLeftJoinCRP(String aliasSale) {
		String sqlTemplate =" "
				+ " LEFT JOIN (\r\n"
				+ "     SELECT DISTINCT a.SaleOrder, a.SaleLine \r\n"
				+ "     FROM [PCMS].[dbo].[ReplacedProdOrder] as a \r\n"
				+ "     LEFT JOIN [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrderRP = b.ProductionOrder \r\n"
				+ this.buildInnerJoinViewUSM_SPE("b",1)
				+ "     WHERE a.DataStatus = 'O' \r\n"
				+ "     GROUP BY a.SaleOrder, a.SaleLine \r\n"
				+ ") AS CRP on CRP.SaleOrder = %s.SaleOrder \r\n"
				+ "        AND CRP.SaleLine = %s.SaleLine \r\n";
	    return String.format(sqlTemplate, 
	    		aliasSale , 
	    		aliasSale );
	}

	public String getLeftJoinSwitchProdOrder(String aliasProd, String aliasJoinField)
	{ 
		String sqlTemplate =" "
				+ " left join ( \r\n"
				+ "    SELECT ProductionOrder, ProductionOrderSW \r\n"
				+ "    FROM [PCMS].[dbo].[SwitchProdOrder] \r\n"
				+ "    WHERE DataStatus = 'O' \r\n"
				+ ") AS R on %s.ProductionOrder = R.%s \r\n";
	    return String.format(sqlTemplate, 
	    		aliasProd ,aliasJoinField);
	}

	public String getLeftJoinSwitchProdOrder(String aliasProd)
	{
		String sqlTemplate =" "
				+ " LEFT JOIN [PCMS].[dbo].[SwitchProdOrder] \r\n"
				+ "AS SPO on SPO.ProductionOrderSW = %s.ProductionOrder"
				+ "      AND SPO.DataStatus = 'O' \r\n";
	    return String.format(sqlTemplate, 
	    		aliasProd );
	}

	public String getLeftJoinTempPlandeliveryDate(String aliasProd, String aliasSale)
	{
		String sqlTemplate = ""
				+ " left join #tempPlandeliveryDate as h on h.ProductionOrder = %s.ProductionOrder and\r\n"
				+ "                                         h.SaleOrder = %s.SaleOrder and\r\n"
				+ "							                h.SaleLine = %s.SaleLine\r\n";
	    return String.format(sqlTemplate, 
	    		aliasProd
	    		, aliasSale,aliasSale);
	}
	public String buildLeftJoinUserStatusAuto(String aliasTable, String aliasProd, String aliasGrade)
	{
		String sqlTemplate = " LEFT JOIN [PCMS].[dbo].[TEMP_UserStatusAuto] AS %s \n"
				+ "     ON %s.[DataStatus] = 'O' \n"
				+ "     AND %s.ProductionOrder = %s.ProductionOrder \n"
				+ "     AND (%s.Grade = %s.Grade OR %s.Grade IS NULL) \n";

		return String.format(sqlTemplate, aliasTable, aliasTable, aliasProd, aliasTable, aliasGrade, aliasTable, aliasGrade);
	}

	public String buildLeftJoinSCC(String aliasProd)
	{
		String sqlTemplate = " LEFT JOIN [PCMS].[dbo].[PlanSendCFMCusDate] AS SCC\n"
				+ "     ON SCC.ProductionOrder = %s.ProductionOrder\n"
				+ "     AND SCC.DataStatus = 'O'\n";
		return String.format(sqlTemplate, aliasProd);
	} 
	public String buildLeftJoinTempSumGR(String aliasMain) {
	    return String.format(
	        " LEFT JOIN #tempSumGR AS m ON %s.ProductionOrder = m.ProductionOrder \n",
	        aliasMain
	    );
	}

	public String buildLeftJoinTempProdWorkDate(String aliasMain) {
	    return String.format(
	        " LEFT JOIN [PCMS].[dbo].[TEMP_ProdWorkDate] AS g ON g.ProductionOrder = %s.ProductionOrder \n",
	        aliasMain
	    );
	} 
	public String buildInnerJoinFromSapMainProd(String aliasJoinTableAs, String fieldJoinTableAs, String aliasTableMainJoin, String fieldMainTableAs) {
	    String sqlTemplate = 
	        " INNER JOIN [PCMS].[dbo].[FromSapMainProd] AS %s \n" +
	        "     ON %s.%s = %s.%s \n" 
	        ;

	    return String.format(sqlTemplate, 
	    		aliasJoinTableAs
	    		, aliasJoinTableAs,fieldJoinTableAs,aliasTableMainJoin, fieldMainTableAs
//	    		, aliasJoinTableAs
	    		);
	}
	public String buildInnerJoinViewUSM_SPE(String aliasMain,int specialCon) {
	    return String.format(
	        " "
	        + "	INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] AS viewUSM_SPE "
	        + "		ON %s.[UserStatus] = viewUSM_SPE.UserStatus "
	        + "     and viewUSM_SPE.[Special] = %s  \n",
	        aliasMain,specialCon
	    );
	}
	public String buildLeftJoinViewUSM_SPE(String aliasMain,int specialCon) {
	    return String.format(
	        " "
	        + "	LEFT JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] AS viewUSM_SPE "
	        + "		ON viewUSM_SPE.UserStatus = %s.[UserStatus] "
	        + "     and viewUSM_SPE.[Special] = %s \n",
	        aliasMain,specialCon
	    );
	}
}
