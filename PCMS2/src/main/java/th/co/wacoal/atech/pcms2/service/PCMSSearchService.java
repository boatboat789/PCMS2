package th.co.wacoal.atech.pcms2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

@Service
public class PCMSSearchService {
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
			+ " SELECT DISTINCT \r\n"
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
			+ " SELECT DISTINCT \r\n"
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
			+ " SELECT distinct  a.id,a.[ProductionOrder] ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
			+ " into #tempPlandeliveryDate\r\n"
			+ " FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
			+ " inner join (\r\n"
			+ "		select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max(Id) as maxId\r\n"
			+ "		FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
			+ "		group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]\r\n"
			+ " ) as b on a.Id = b.maxId  \r\n";
	public String fromMainSale_A =
			""
			+ " from (\r\n"
			+ "			SELECT distinct \r\n"
			+ "                   	 a.*\r\n"
			+ "					   , CASE  \r\n"
			+ "							WHEN b.[ProductionOrder] is not null THEN b.[ProductionOrder]  \r\n"
			+ "							WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
			+ "					   		WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
			+ "							WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
			+ "							ELSE 'รอจัด Lot'  \r\n"
			+ "							END AS [ProductionOrder]  \r\n"
			+ "				 	   , CASE  \r\n"
			+ "							WHEN b.[ProductionOrder] is not null THEN b.[LotNo] \r\n"
			+ "							WHEN SUBSTRING (a.[MaterialNo], 1, 1) = 'V' THEN 'รับจ้างถัก'\r\n"
			+ "					   		WHEN z.[CheckBill] > 0 THEN 'Lot ขายแล้ว'\r\n"
			+ "							WHEN a.[SaleStatus] = 'C' THEN 'ขาย stock'\r\n"
			+ "							ELSE 'รอจัด Lot'  \r\n"
			+ "							END AS [LotNo]   \r\n"
			+ "				 	    , b.[TotalQuantity]\r\n"
			+ "                     , b.[Unit]\r\n"
			+ "				 	    , b.[RemAfterCloseOne]\r\n"
			+ "                     , b.[RemAfterCloseTwo]\r\n"
			+ "                     , b.[RemAfterCloseThree]\r\n"
			+ "                     , b.[LabStatus] \r\n" 
			+ "                     , b.[BookNo]\r\n"
			+ "                     , b.[Center] \r\n"
			+ "				 	    , b.[Batch]\r\n"
			+ "                     , b.[LabNo]\r\n"
			+ "                     , b.[RemarkOne]\r\n"
			+ "                     , b.[RemarkTwo]\r\n"
			+ "                     , b.[RemarkThree]\r\n"
			+ "                     , b.[BCAware]\r\n"
			+ "				 	    , b.[OrderPuang]\r\n"
			+ "                     , b.[RefPrd]\r\n"
			+ "                     , b.[GreigeInDate]\r\n"
			+ "                     , b.[BCDate]\r\n"
			+ "                     , b.[Volumn]\r\n"
			+ "				 	    , b.[CFdate]\r\n"
			+ "                     , b.[CFType]\r\n"
			+ "                     , b.[Shade]\r\n"
			+ "				        , b.[PrdCreateDate]\r\n"
			+ "                     , b.[GreigeArticle]\r\n"
			+ "                     , b.[GreigeDesign]\r\n"
			+ "                     , b.[GreigeMR]\r\n"
			+ "                     , b.[GreigeKG]\r\n"
			+ "				        , b.[BillSendQuantity]\r\n"
			+ "             from #tempMainSale as a\r\n"
			+ "             left join [PCMS].[dbo].[FromSapMainProd] as b on a.SaleOrder = b.SaleOrder and\r\n"
			+ "                                                              a.SaleLine = b.SaleLine \r\n"
			+ "			   	left join ( \r\n"
			+ "                 SELECT distinct [SaleOrder],[SaleLine] ,1 as [CheckBill] \r\n"
			+ "					FROM [PCMS].[dbo].[FromSapMainBillBatch]\r\n"
			+ "					where DataStatus = 'O'\r\n"
			+ "				    group by [SaleOrder],[SaleLine]\r\n"
			+ "             ) as z on A.[SaleOrder] = z.[SaleOrder] AND\r\n"
			+ "                       A.[SaleLine] = z.[SaleLine]  \r\n"
			+ " ) as a \r\n";
	public String leftJoinB_H = ""
			+ " left join #tempPlandeliveryDate as h on h.ProductionOrder = b.ProductionOrder and\r\n"
			+ "                                         h.SaleOrder = a.SaleOrder and\r\n"
			+ "                                         h.SaleLine = a.SaleLine \r\n";
	public String leftJoinSCC = ""
			+ " left join [PCMS].[dbo].[PlanSendCFMCusDate] as SCC on SCC.ProductionOrder = b.ProductionOrder and\r\n"
			+ "                                                       SCC.DataStatus = 'O'\r\n";
	public String leftJoinM_A = " left join #tempSumGR as m on A.ProductionOrder = m.ProductionOrder \r\n";
	public String leftJoinSCC_A = ""
			+ " left join [PCMS].[dbo].[PlanSendCFMCusDate] as SCC on SCC.ProductionOrder = a.ProductionOrder and\r\n"
			+ "                                                       SCC.DataStatus = 'O'\r\n";

	public String leftJoinBPartOneH_A = ""
			+ "           left join #tempPlandeliveryDate as h on h.ProductionOrder = a.ProductionOrder and\r\n"
			+ "                                                   h.SaleOrder = a.SaleOrder and\r\n"
			+ "							                          h.SaleLine = a.SaleLine\r\n";
	public String leftJoinTempG_A =
			" left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g on g.ProductionOrder = a.ProductionOrder \r\n";
	public String leftJoinUCAL_A = "    "
			+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCAL on UCAL.[DataStatus] = 'O' AND\r\n"
			+ "                                                         a.ProductionOrder = UCAL.ProductionOrder AND\r\n"
			+ "                                                         ( m.Grade = UCAL.Grade OR m.Grade IS NULL )  \r\n";

	public String leftJoinBPartOneT_A = ""
			+ "			left join ( \r\n"
			+ "				SELECT "
			+ "					a.ProductionOrder  , sum(a.Volumn) as SumVolOP\r\n"
			+ "				from [PCMS].[dbo].FromSapMainProdSale as a\r\n"
			+ "			   	left join [PCMS].[dbo].[FromSapMainProd] as b "
			+ "					on a.ProductionOrder = b.ProductionOrder  \r\n"
			+ "			   	WHERE a.[DataStatus] = 'O' and \r\n"
			+ "                   ( b.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' ))\r\n"
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
			+ "				WHERE a.[DataStatus] = 'O'  \r\n"
			+ "    				AND (b.UserStatus NOT IN ('ยกเลิก', 'ตัดเกรดZ'))  \r\n"
			+ "				GROUP BY a.ProductionOrderRP \r\n"
			+ "			) as s "
			+ "				on a.ProductionOrder = s.ProductionOrderRP  \r\n";

	public String leftJoinUCAL = "    "
			+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCAL "
			+ "		on UCAL.[DataStatus] = 'O' \r\n"
			+ "		AND b.ProductionOrder = UCAL.ProductionOrder \r\n"
			+ "		AND ( m.Grade = UCAL.Grade OR m.Grade IS NULL )  \r\n";
	public String leftJoinUCALRP = "    "
			+ " left join [PCMS].[dbo].[TEMP_UserStatusAuto] as UCALRP "
			+ "		on UCALRP.[DataStatus] = 'O'   \r\n"
			+ "		AND b.ProductionOrder = UCALRP.ProductionOrder  \r\n"
			+ "		AND ( m.Grade = UCALRP.Grade OR m.Grade IS NULL )    \r\n";
	public String leftJoinM = " "
			+ " left join #tempSumGR as m "
			+ "		on b.ProductionOrder = m.ProductionOrder \r\n";
	public String leftJoinTempG =
			" "
			+ " left join [PCMS].[dbo].[TEMP_ProdWorkDate] as g "
			+ "		on g.ProductionOrder = b.ProductionOrder \r\n"; 
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
			+ "        ,cast(null as decimal) as TotalQuantity \r\n"
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
	   		 + "             A.SaleOrder\r\n"
	   		 + "           , A.SaleLine\r\n"
	   		 + "           , COALESCE(SumVolOP, 0 ) + COALESCE(SumVolRP, 0 ) as SumVolUsed --,COALESCE(SumVolRP, 0 )\r\n"
	   		 + "	       , SumVolOP\r\n"
	   		 + "           , SumVolRP\r\n"
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
			+ "        select DISTINCT a.SaleOrder ,a.SaleLine ,1 AS CountProdRP\r\n"
			+ "		from [PCMS].[dbo].[ReplacedProdOrder] as a\r\n"
			+ "		inner join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrderRP = b.ProductionOrder\r\n"
			+ "		where a.DataStatus = 'O' and\r\n"
			+ "              a.ProductionOrder = 'รอจัด Lot' and\r\n"
			+ "		      ( b.UserStatus not in ( 'ยกเลิก' , 'ตัดเกรดZ' )) \r\n"
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

		ArrayList<String> listUserStatus = new ArrayList<>();
		String where = "where 1 = 1";
		String whereSale = " where A.[DataStatus] = 'O' AND 1 = 1";
		String whereWaitLot = " where 1 = 1 ";
		String whereBMainUserStatus = " where 1 = 1";
		String whereProd = " ";
		String whereCaseTryRP = "";
		String whereCaseTry = "";
		String tmpWhereNoLotUCAL = "";
//		String whereCaseA = "";
//		@SuppressWarnings("unused")
		String saleNumber = "",materialNo = "",saleOrder = "",saleCreateDate = "",labNo = "",articleFG = "",designFG = "",
				prdOrder = "",prdCreateDate = "",deliveryStatus = "",saleStatus = "",distChannel = "",dueDate = "",po = "";
//		bean.getCustomerName();
//		bean.getCustomerShortName();
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
		where += buildLikeClause("MaterialNo", materialNo,"a");
		whereSale += buildLikeClause("MaterialNo", materialNo,"a");
		where += buildLikeClause("SaleOrder", saleOrder,"a");
		whereSale += buildLikeClause("SaleOrder", saleOrder,"a");
		where += buildDateClause("SaleCreateDate", saleCreateDate,"a");
		whereSale += buildDateClause("SaleCreateDate", saleCreateDate,"a");
		where += buildListClause("Division", divisionList,"a");
		whereSale += buildListClause("Division", divisionList,"a");
		where += buildLikeClause("PurchaseOrder", po,"a");
		whereSale += buildLikeClause("PurchaseOrder", po,"a");
		where += buildLikeClause("SaleNumber", saleNumber,"a");
		whereSale += buildLikeClause("SaleNumber", saleNumber,"a");
		where += buildLikeClause("ArticleFG", articleFG,"a");
		whereSale += buildLikeClause("ArticleFG", articleFG,"a");
		where += buildLikeClause("DesignFG", designFG,"a");
		whereSale += buildLikeClause("DesignFG", designFG,"a");
//		where += buildListClause("CustomerName", cusNameList,"a");
//		whereSale += buildListClause("CustomerName", cusNameList,"a");
//		where += buildListClause("CustomerShortName", cusShortNameList,"a");
//		whereSale += buildListClause("CustomerShortName", cusShortNameList,"a");
		
		where += "   "
				+ "  AND EXISTS (\r\n"
				+ "        SELECT 1\r\n"
				+ "        FROM #tempCustomerList AS c\r\n"
				+ "        WHERE c.CustomerName COLLATE Thai_100_CI_AS = a.CustomerName COLLATE Thai_100_CI_AS\r\n"
				+ "    ) ";
		where += "   "
				+ "  AND EXISTS (\r\n"
				+ "        SELECT 1\r\n"
				+ "        FROM #tempCustomerShortList AS c\r\n"
				+ "        WHERE c.CustomerShortName COLLATE Thai_100_CI_AS = a.CustomerShortName COLLATE Thai_100_CI_AS\r\n"
				+ "    ) " ;
		where += buildListClause("Division", divisionList,"a");
		whereSale += buildListClause("Division", divisionList,"a");
		where += buildDateClause("DueDate", dueDate,"a");
		whereSale += buildDateClause("DueDate", dueDate,"a");
		where += buildLikeClause("DeliveryStatus", deliveryStatus,"a");
		whereSale += buildLikeClause("DeliveryStatus", deliveryStatus,"a");
		where += buildSaleStatusClause(saleStatus);
		whereSale += buildSaleStatusClause(saleStatus);
		where += buildListClauseByArray("DistChannel", distChannel.split("\\|"));
		whereSale += buildListClauseByArray("DistChannel", distChannel.split("\\|"));

		// Production order conditions
		whereProd += buildLikeClause("LabNo", labNo,"b");
		where += buildLikeClause("LabNo", labNo,"b");
		where += buildLikeClause("ProductionOrder", prdOrder,"b");
		whereProd += buildLikeClause("ProductionOrder", prdOrder,"b");
		where += buildDateClause("PrdCreateDate", prdCreateDate,"b");
		whereProd += buildDateClause("PrdCreateDate", prdCreateDate,"b");
		whereWaitLot = where;

		whereCaseTry = whereProd;

		whereCaseTryRP = "" + whereProd;
		whereBMainUserStatus = whereProd;
		if (userStatusList.size() > 0) {
			String tmpWhere = "";
			tmpWhereNoLotUCAL = " and ( b.ProductionOrder is not null and ( \r\n";
			tmpWhere += " and ( b.ProductionOrder is not null and ( \r\n";
			whereCaseTryRP += " and ( b.ProductionOrder is not null and ( \r\n";
			whereCaseTry += " and ( a.ProductionOrder is not null and ( \r\n";
			String text = "";
			int int_emerCheck = 0;
			for (int i = 0; i < userStatusList.size(); i ++ ) {
				text = userStatusList.get(i);
				if (text.equals("รอจัด Lot") || text.equals("ขาย stock") || text.equals("รับจ้างถัก")
						|| text.equals("Lot ขายแล้ว") || text.equals("พ่วงแล้วรอสวม") || text.equals("รอสวมเคยมี Lot")) {
					tmpWhere += " b.LotNo = '" + text + "' ";
					whereCaseTryRP += " b.LotNo = '" + text + "' ";
					whereCaseTry += " a.LotNo = '" + text + "' ";
					listUserStatus.add("'" + text.replaceAll("'", "''") + "' ");
				} else {
					int_emerCheck = 1;
					whereCaseTryRP += "UCALRP.UserStatusCalRP = '" + text + "' ";
					tmpWhere += "UCAL.UserStatusCal = '" + text + "' ";
					tmpWhereNoLotUCAL += "UCAL.UserStatusCal = '" + text + "' ";
					whereCaseTry += "a.UserStatus = '" + text + "' ";
					if (i != userStatusList.size()-1) {
						tmpWhereNoLotUCAL += " or ";
					}
				}
				if (i != userStatusList.size()-1) {
					tmpWhere += " or ";
					whereCaseTryRP += " or ";
					whereCaseTry += " or ";
				}
			}
			if (int_emerCheck == 0) {
				tmpWhereNoLotUCAL += "UCAL.UserStatusCal = ''  ";
			}
			int sizeUS = listUserStatus.size();
			if (sizeUS > 0) {
				whereWaitLot += " and ( b.LotNo IN ( \r\n";
				whereWaitLot += String.join(",", listUserStatus);
				whereWaitLot += " ) ) \r\n";
			} else {
				whereWaitLot += " and ( b.UserStatus is not null ) \r\n";
			}
			tmpWhere += ") 		) \r\n";
			whereCaseTry += ") 		) \r\n";
			whereCaseTryRP += ") 		) \r\n";
			tmpWhereNoLotUCAL += ") 		) \r\n";
			where += tmpWhere;

			whereBMainUserStatus += " and a.SaleOrder <> '' " + tmpWhere;
		}
		whereBMainUserStatus = whereBMainUserStatus.replace("UserStatusCalRP", "UserStatus");
		whereBMainUserStatus = whereBMainUserStatus.replace("UserStatusCal", "UserStatus");
		whereBMainUserStatus = whereBMainUserStatus.replace("UCALRP.", "a.");
		whereBMainUserStatus = whereBMainUserStatus.replace("UCAL.", "a.");
		whereBMainUserStatus = whereBMainUserStatus.replace("b.", "a.");

		whereCaseTry = whereCaseTry.replace("UserStatusCal", "UserStatus");
		whereCaseTry = whereCaseTry.replace("UCALRP.", "a.");
		whereCaseTry = whereCaseTry.replace("UCAL.", "a.");
		whereCaseTry = whereCaseTry.replace("b.", "a.");
		// Put all where clauses in the map
		whereClauses.put("whereCaseTry", whereCaseTry);
		whereClauses.put("whereCaseTryRP", whereCaseTryRP);
		whereClauses.put("tmpWhereNoLotUCAL", tmpWhereNoLotUCAL);
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
		return "and ("+para+"."
				+ columnName
				+ " >= CONVERT(DATE,'"
				+ dateArray[0].trim()
				+ "',103) and \n"+para+"."
				+ columnName
				+ " <= CONVERT(DATE,'"
				+ dateArray[1].trim()
				+ "',103)) \n";
	}

	private String buildLikeClause(String columnName, String value, String para)
	{
		if (value.isEmpty()) {
			return "";
		}
		return "and "+para+"." + columnName + " like '" + value + "%' \n";
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
		return "and ("+para+"."   + columnName + " IN (" + String.join(",", escapedList) + ")) \n";
	}

	private String buildListClauseByArray(String columnName, String[] strings)
	{
		if (strings.length > 0) {
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
		if (saleStatus.equals("O")) {
			return ""
					+ " and (SaleStatus like '"
					+ saleStatus
					+ "%' or "
					+ "			( a.[RemainQuantity] > 0 and SaleStatus <> 'X' ) "
					+ "		) \n";
		} else if (saleStatus.equals("X")) {
			return "and (SaleStatus like '" + saleStatus + "%') \n";
		} else if (saleStatus.equals("C")) {
			return "and (SaleStatus like '" + saleStatus + "%') \n";
		} else {
			return "and (SaleStatus like '" + saleStatus + "%' or a.[RemainQuantity] = 0) \n";
		}
	}
}
