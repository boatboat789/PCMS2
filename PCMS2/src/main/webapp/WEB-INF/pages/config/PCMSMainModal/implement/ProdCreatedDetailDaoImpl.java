package dao.implement;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import Test.utilities.ParseDouble;
import Test.utilities.StringHandler;
import dao.ProdCreatedDetailDao;
import entities.ForecastRunningDetail;
import entities.InputArticleDetail;
import entities.InputForecastDetail;
import entities.InputGroupDetail;
import entities.InputPODetail;
import entities.InputPlanLotRedyeDetail;
import entities.InputTempProdDetail;
import entities.POManagementDetail;
import entities.SorPODetailList;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.POManagementModel;
import model.PlanningProdModel;
import model.ReportModel;
import model.master.ArticleDetailModel;
import model.master.RelationPOAndProdOrderModel;
import model.master.SORPODetailChangeModel;
import model.master.SORPODetailModel;
import model.master.SORTempProdModel;
import model.master.SorPOPuangDetailModel;
import model.master.TempForecastRunningModel;
import model.master.TempPORunningModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class ProdCreatedDetailDaoImpl implements ProdCreatedDetailDao {
	public static List<Entry<Double, Integer>> getMapEntryList(HashMap<Double, Integer> map,
			Comparator<Entry<Double, Integer>> valDescThenKeyDesc)
	{
		return map.entrySet().stream().sorted(valDescThenKeyDesc).collect(Collectors.toList());
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map)
	{
		return map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed()).collect(
				Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//    	return map.entrySet().stream()
//    		       .sorted(Map.Entry.comparingByValue())
//    		       .collect(Collectors.toMap(
//    		    		   Map.Entry::getKey,
//    		    		   Map.Entry::getValue,
//    		    		   (oldValue, newValue) ->  oldValue,
//    						LinkedHashMap::new));
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueV2(Map<K, V> map)
	{
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
		Map<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String conType;
	private String message;
	private String C_CK = "CK";

	private String C_PENDING = "PENDING";

	private String C_MAX = "MAX";
	private String C_MIN = "MIN";
	private int INT_POQTYMOD = 100;
	private DateFormat C_yyyy = new SimpleDateFormat("yyyy"); // Just the year, with 2 digits
	private DateFormat C_yyMM = new SimpleDateFormat("yyMM"); // Just the year, with 2 digits
	private DecimalFormat df2 = new DecimalFormat("#.##");
	public DecimalFormat df3 = new DecimalFormat("###,###,##0.00");
	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	private String declarePOMainNPOInstead = ""
			+ " IF OBJECT_ID('tempdb..#tempPOMainNPOInstead') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempPOMainNPOInstead;  \r\n"
			+ "	select  a.[Id]\r\n"
			+ "	, a.POId \r\n"
			+ "	,a.[ForecastId] ,a.[PlanInsteadId] ,a.[RuleNo] ,a.[ColorType]\r\n"
			+ "	,a.[ProductionOrder] ,a.[FirstLot] ,a.[ProdOrderQty] ,a.[GroupOptions]\r\n"
			+ "	,a.[GroupBegin] ,a.[PPMMStatus] ,a.[DataStatus] ,a.[ChangeDate]\r\n"
			+ "	,a.[ChangeBy] ,a.[CreateDate] ,a.[CreateBy] ,a.[Batch]\r\n"
			+ "	INTO #tempPOMainNPOInstead\r\n"
			+ "	from [PPMM].[dbo].[SOR_TempProd] as a\r\n"
			+ "	where a.POId is not null and \r\n"
			+ "       a.DataStatus = 'O' \r\n";

	private String declareSixMonthAgo = ""
			+ " declare @SixMonthAgo date  = CAST( dateadd(MONTH,-6, GETDATE()) AS DATE) ;   \r\n"
			+ " IF OBJECT_ID('tempdb..#tempIsLastWorkNotInSixMonth') IS NOT NULL \r\n"
			+ "	 DROP TABLE #tempIsLastWorkNotInSixMonth ; \r\n"
			+ " select DISTINCT a.ID,a.Article,a.Color   \r\n"
			+ " ,CASE \r\n"
			+ "	WHEN MaxCreateDate IS NULL AND LastEndDate IS NULL THEN cast(1 as bit)\r\n"
			+ "	WHEN MaxCreateDate IS NOT NULL AND LastEndDate IS NOT NULL THEN \r\n"
			+ "		CASE\r\n"
			+ "			WHEN CAST(MaxCreateDate as Date )  >= CAST(LastEndDate as Date ) THEN\r\n"
			+ "				CASE	\r\n"
			+ "					WHEN CAST(MaxCreateDate as Date ) >= @SixMonthAgo THEN cast(0 as bit)\r\n"
			+ "					ELSE  cast(1 as bit)\r\n"
			+ "				END\r\n"
			+ "			WHEN CAST(MaxCreateDate as Date )  <= CAST(LastEndDate as Date ) THEN\r\n"
			+ "				CASE	\r\n"
			+ "					WHEN CAST(LastEndDate as Date ) >= @SixMonthAgo THEN cast(0 as bit)\r\n"
			+ "					ELSE cast(1 as bit)\r\n"
			+ "				END \r\n"
			+ "			END\r\n"
			+ "	WHEN MaxCreateDate IS NOT NULL THEN \r\n"
			+ "		CASE	\r\n"
			+ "			WHEN CAST(MaxCreateDate as Date ) >= @SixMonthAgo THEN cast(0 as bit)\r\n"
			+ "			ELSE cast(1 as bit)\r\n"
			+ "		END\r\n"
			+ "	WHEN LastEndDate IS NOT NULL THEN \r\n"
			+ "		CASE	\r\n"
			+ "			WHEN CAST(LastEndDate as Date ) >= @SixMonthAgo THEN cast(0 as bit)\r\n"
			+ "			ELSE cast(1 as bit)\r\n"
			+ "		END  \r\n"
			+ "	ELSE cast(0 as bit)\r\n"
			+ "	END AS isLastWorkNotInSixMonth  \r\n"
			+ " INTO #tempIsLastWorkNotInSixMonth\r\n"
			+ " from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ " LEFT JOIN (\r\n"
			+ "	  SELECT distinct A.Article\r\n"
			+ "		  ,A.Color\r\n"
			+ "		  ,max(B.[CreateDate]) as MaxCreateDate\r\n"
			+ "	  FROM [SOR_PODetail]AS A\r\n"
			+ "	  INNER JOIN [PPMM].[dbo].[SOR_TempProd] AS B ON A.Id = B.POId and B.[DataStatus] = 'O'\r\n"
			+ "	  where a.[DataStatus] = 'O' AND\r\n"
			+ "         b.[POId] is not null\r\n"
			+ "	  group by A.Article  ,A.Color  \r\n"
			+ ") AS npo on a.Article = npo.Article  and a.Color = npo.Color\r\n"
			+ "left join (\r\n"
			+ "	 select  MaterialNumber,Article ,max(OperationEndDate) as LastEndDate \r\n"
			+ "	 from [PPMM].[dbo].[DataFromSap] \r\n"
			+ "	 where AdminStatus = '-' and OperationStatus = 'PROCESS DONE' and OperationEndDate is not null and Article is not null\r\n"
			+ "	 group by  MaterialNumber,Article\r\n"
			+ ") as b on a.Color = RIGHT(b.MaterialNumber,6) and a.Article = b.Article\r\n"
			+ " where a.[DataStatus] = 'O'\r\n"
			+ " ORDER BY ID\r\n";
	private String leftJoinIAD = "" + "  left join [PPMM].[dbo].[InputArticleDetail] as IAD on a.[Article] = IAD.[Article]\r\n";
	private String select = ""
			+ "       a.[Id] as No\r\n"
			+ "      ,a.[PO]\r\n"
			+ "      ,a.[POLine]\r\n"
			+ "      ,a.[Division]\r\n"
			+ "      ,a.[DistChannal]\r\n"
			+ "      ,a.[Design]\r\n"
			+ "      ,a.[Article]"
			+ "      ,a.[MaterialNo]\r\n"
			+ "      ,a.[CustomerNo]\r\n"
			+ "      ,case \r\n"
			+ "			when [CustomerShortName] is not null then [CustomerShortName]\r\n"
			+ "			else a.[CustomerName]\r\n"
			+ "			end as [CustomerName]\r\n"
			+ "      ,a.[ColorCustomer]\r\n"
			+ "      ,a.[Color]\r\n"
			+ "      ,a.[OrderQty]\r\n"
			+ "	  	 ,a.[OrderQtyCal]  \r\n"
			+ "	  	 ,a.[OrderQtyCalLast]\r\n"
			+ "      ,a.[Unit]\r\n"
			+ "      ,a.[CustomerDue]\r\n"
			+ "      ,a.[GreigePlan]\r\n"
			+ "      ,a.[DocDate]\r\n"
			+ "      ,a.[ChangeBy]\r\n"
			+ "      ,a.[ChangeDate]  \r\n"
			+ "	     ,[CountProd] \r\n"
			+ "    , case \r\n"
			+ "			when a.[Division]  = 'TR' OR a.[Division]  = 'TW' OR  a.[Division]  = 'CK' or \r\n"
			+ "              a.[Division]  = 'PN' OR a.[Division]  = 'OT'  THEN 0\r\n"
			+ "			ELSE 1\r\n"
			+ "		END AS [CheckDivision]\r\n"
			+ "	   , case \r\n"
			+ "			when a.[Article] = '' or  c.[Article] is null THEN 1\r\n"
			+ "			ELSE 0\r\n"
			+ "		END AS [CheckArticle]\r\n"
			+ "	   , case \r\n"
			+ "			when d.[CountLotMinMax] > 0 THEN 0\r\n"
			+ "			else 1 \r\n"
			+ "		END AS [CheckArticleWeight]  \r\n"
			+ "	   , case \r\n"
			+ "			when a.[GreigePlan] = '' OR a.[GreigePlan] IS null THEN 1\r\n"
			+ "			else 0 \r\n"
			+ "		END AS [CheckGreigePlan]  \r\n "
			+ "	   , [IsCotton] \r\n"
			+ "    ,a.[DyeAfterGreigeInBegin]\r\n"
			+ "    ,a.[DyeAfterGreigeInLast]\r\n"
			+ "	  ,	case \r\n"
			+ "			when a.[Unit] = 'PC' THEN\r\n"
			+ "				case \r\n"
			+ "					when f.[FormulaPC] > 0 then 0\r\n"
			+ "					else 1\r\n"
			+ "				end \r\n"
			+ "			when a.[Unit] = 'KG' THEN\r\n"
			+ "				case \r\n"
			+ "					when f.[FormulaKG] > 0 then 0\r\n"
			+ "					else 1\r\n"
			+ "				end \r\n"
			+ "			when a.[Unit] = 'YD' THEN \r\n"
			+ "				case \r\n"
			+ "					when f.[FormulaYD] > 0 then 0\r\n"
			+ "					else 1\r\n"
			+ "				end  \r\n"
			+ "			when a.[Unit] = 'MR' OR a.[Unit] = 'M' THEN 0\r\n"
			+ "			else 2\r\n"
			+ "		END AS [CheckFormula]  \r\n"
			+ "	  ,	case \r\n"
			+ "			when a.Color = '' OR a.Color IS null THEN 1\r\n"
			+ "			else 0 \r\n"
			+ "		END AS [CheckColor]  \r\n"
			+ "	  ,	case \r\n"
			+ "			when a.CustomerNo = '' OR a.CustomerNo IS null THEN 1\r\n"
			+ "			else 0 \r\n"
			+ "		END AS [CheckCustomerNo]  \r\n"
			+ "	  ,	case \r\n"
			+ "			when a.[CustomerDue] = '' OR a.[CustomerDue] IS null THEN 1\r\n"
			+ "			else 0 \r\n"
			+ "		END AS [CheckCustomerDue]  \r\n"
			+ "	  ,	case \r\n"
			+ "			when a.OrderQty = 0 OR a.OrderQty IS null THEN 1\r\n"
			+ "			else 0 \r\n"
			+ "		END AS [CheckOrderQty]   \r\n"
			+ "	, case \r\n"
			+ "		when SPOPD.[Id] = a.[POPuangId] then 1\r\n"
			+ "		else 0 \r\n"
			+ "	 end as isPOPuang\r\n"
			+ "	, case \r\n"
			+ "		when SPOPD.[POId] = a.[Id] then 1\r\n"
			+ "		else 0 \r\n"
			+ "	 end as isPOPuangMain\r\n"
			+ " , a.POPuangId\r\n"
			+ " , case \r\n"
			+ "		when checkOP is not null then 1\r\n"
			+ "		else 0\r\n"
			+ "		end as checkOP\r\n"
			+ " ,a.[PODate]\r\n"
			+ " ,[CountDocDate]\r\n"
			+ " ,case \r\n"
			+ "		when [isHW] = 1 then 0\r\n"
			+ "		when c.[QtyGreigeMR] is null or c.[QtyGreigeMR] = 0 then 1\r\n"
			+ "		else 0 \r\n"
			+ "		end as CheckQtyGreigeMR \r\n"
			+ " ,c.[QtyGreigeMR]\r\n"
			+ " , isHW\r\n"
			+ "	  ,a.[ColorType]\r\n"
			+ "   ,scmr.[Id] as [SpecialCaseMRId]\r\n"
			+ "   ,scmr.[SpecialType]\r\n"
			+ "   ,scmr.[ArticleComment]\r\n"
			+ "   ,scmr.[Variable]\r\n"
			+ "   ,scmr.[GroupOptionsOne]\r\n"
			+ "   ,scmr.[GroupOptionsTwo]\r\n"
			+ "   ,scmr.[ColorTypeCheck]\r\n"
			+ "   , case\r\n"
			+ "		when isRecheck = cast(1 as bit) or isHW = 1 then  a.[OrderQtyCalLast]\r\n"
			+ "	    when c.IsQtyGreigeForPO = cast(1 as bit) and c.QtyGreigeMR > 0 then a.[OrderQtyCalLast]  --ceiling(a.[OrderQtyCalLast] / c.QtyGreigeMR) * c.QtyGreigeMR \r\n"
			+ "		else ceiling(a.[OrderQtyCalLast] / 50) * 50  \r\n"
			+ "		end as OrderQtyCalLastPlan \r\n"
			+ " 	,a.isRecheck\r\n"
			+ "     ,a.isGroupRecheck\r\n"
			+ "	  , isLastWorkNotInSixMonth\r\n"
			+ "   , a.[IsDCOrderPuang] \r\n"
			+ "   , CASE \r\n"
			+ "		WHEN C.[IsQtyGreigeForPO] = 1 THEN \r\n"
			+ "			CASE \r\n"
			+ "				WHEN C.[QtyGreigeMR] = 0 OR C.[QtyGreigeMR] IS NULL THEN 1 \r\n"
			+ "				WHEN A.[OrderQtyCalLast]%C.[QtyGreigeMR] = 0 THEN 0 \r\n"
			+ "				ELSE 1\r\n"
			+ "			END\r\n"
			+ "		ELSE 0\r\n"
			+ "		END AS CheckQtyGreigeForPO\r\n";

	private String leftJoinPOStatusSPDC =
			"" + "  left join [PPMM].[dbo].[SOR_PODetailChange] as SPDC on a.[Id] = SPDC.[POId] AND SPDC.[DataStatus] = 'O' \r\n";
	private String from = " "
			+ " FROM ( \r\n"
			+ "		SELECT \r\n"
			+ "		 a.[Id]\r\n"
			+ "		,a.[DocDate]\r\n"
			+ "		,a.[CustomerNo]\r\n"
			+ "     ,a.[CustomerName]\r\n"
			+ "     ,a.[PO]\r\n"
			+ "     ,a.[POLine]\r\n"
			+ "     ,a.[MaterialNoTWC] \r\n"
			+ "     ,case\r\n"
			+ "			when IAD.[ArticleReplaced] is not null and IAD.ArticleReplaced <> '' then IAD.[ArticleReplaced]\r\n"
			+ "       else a.Article\r\n"
			+ "       end as Article\r\n"
			+ "      ,a.[Design]\r\n"
			+ "      ,a.[ColorCustomer]\r\n"
			+ "      ,a.[Color]\r\n"
			+ "      ,a.[LabRef]\r\n"
			+ "      ,a.[Unit]\r\n"
			+ "      ,a.[CustomerDue]\r\n"
			+ "      ,a.[GreigePlan]\r\n"
			+ "      ,a.[ItemNote]\r\n"
			+ "      ,a.[ProductionMemo]\r\n"
			+ "      ,a.[ModelCode]\r\n"
			+ "      ,a.[LabRefLotNo]\r\n"
			+ "      ,a.[MaterialNo]\r\n"
			+ "      ,a.[PODate]\r\n"
			+ "      ,a.[UpdateBy]\r\n"
			+ "      ,a.[UpdateDate]\r\n"
			+ "      ,a.[IsUpdate]\r\n"
			+ "      ,a.[DistChannal]\r\n"
			+ "      ,a.[Division]\r\n"
			+ "      ,a.[RuleNo]\r\n"
			+ "      ,a.[DyeAfterCFM]\r\n"
			+ "      ,a.[DataStatus]\r\n"
			+ "      ,a.[DyeAfterGreigeInBegin]\r\n"
			+ "      ,a.[DyeAfterGreigeInLast]\r\n"
			+ "      ,a.[LastCFMDate]\r\n"
			+ "      ,a.[ChangeBy]\r\n"
			+ "      ,a.[ChangeDate]\r\n"
			+ "      ,a.[CreateBy]\r\n"
			+ "      ,a.[CreatedDate]\r\n"
			+ "      ,a.[POPuangId]\r\n"
			+ "      ,case\r\n"
			+ "			when SUBSTRING(a.[MaterialNo], 1, 1) = 'H' then 1\r\n"
			+ "			else 0\r\n"
			+ "			end as isHW\r\n"
			+ "	     ,case\r\n"
			+ "			WHEN a.[Color] = '' or a.[Color] is null THEN ''\r\n"
			+ "			WHEN LEFT(a.[Color], 2) = 'BL' THEN 'Black'\r\n"
			+ "			ELSE 'Color'	\r\n"
			+ "			end as [ColorType]\r\n"
			+ "     , a.OrderQty \r\n"
			+ "     , CAST( a.[OrderQtyCal] AS DECIMAL(15, 2)) as [OrderQtyCal]\r\n"
			+ "		,CAST( a.[OrderQtyCalLast] AS DECIMAL(15, 2))  as [OrderQtyCalLast] \r\n"
			+ " 	,case\r\n"
			+ "			when spdc.OrderQty is not null and (spdc.OrderQty%50 ) >= 1  and (spdc.OrderQty%50) <= 9  then cast(1 as bit)\r\n"
			+ "			when spdc.OrderQty is not null then cast(0 as bit)\r\n"
			+ "			when (OrderQtyCalLast%50) >= 1 and (OrderQtyCalLast%50) <= 9 then cast(1 as bit)\r\n"
			+ "			else cast(0 as bit)\r\n"
			+ "		end as isRecheck\r\n"
			+ "     ,a.isGroupRecheck\r\n"
			+ "     ,a.isTryCreateLot\r\n"
			+ "      ,spdc.OrderQty as OrderQtyReplaced\r\n"
			+ "     ,a.[IsDCOrderPuang]\r\n"
			+ "  FROM [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ this.leftJoinPOStatusSPDC
			+ this.leftJoinIAD
			+ "  where a.DataStatus = 'O' \r\n"
			+ ")  as a\r\n ";
	private String leftJoinC = ""
			+ " LEFT JOIN [PPMM].[dbo].[InputArticleDetail] AS C ON a.Article = c.Article and\r\n"
			+ "                                                     c.DataStatus = 'O'\r\n";
	private String leftJoinSCMR = ""
			+ " left join [PPMM].[dbo].[SpecialCaseMR] as SCMR on SCMR.Id = c.SpecialCaseId and\r\n"
			+ "                                                   SCMR.DataStatus = 'O'\r\n";

	private String leftJoinCD = ""
			+ " left join ( \r\n"
			+ " 	SELECT [Id] \r\n"
			+ "		  ,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo] \r\n"
			+ "		  ,[CustomerShortName] \r\n"
			+ "		  ,[ChangeDate] \r\n"
			+ "		  ,[CreateDate] \r\n"
			+ "		  FROM [PCMS].[dbo].[CustomerDetail] \r\n"
			+ " ) as cd on a.[CustomerNo] = cd.[CustomerNo]\r\n";
	private String leftJoinTISMA = ""
			+ " left join #tempIsLastWorkNotInSixMonth as tisma on a.[Article] = tisma.[Article] and \r\n"
			+ "                                                    a.[Color] = tisma.[Color]\r\n";
	private String leftJoinCountDocDate = ""
			+ " left join ( \r\n"
			+ "		SELECT distinct a.[MaterialNo]  , a.[DocDate],a.[GreigePlan],count(a.[DocDate] ) as CountDocDate\r\n"
			+ " 		FROM [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ " 		left join ( \r\n"
			+ "			select distinct [POId] , count(ProductionOrder) as CountProd \r\n"
			+ "			from #tempPOMainNPOInstead \r\n"
			+ "			where POId IS NOT NULL  and [DataStatus] = 'O'\r\n"
			+ "			group by [POId] \r\n"
			+ "		) as b on a.Id = b.[POId]\r\n "
			+ "		where a.POPuangId is null and  \r\n"
			+ "			  ( [CountProd] = 0 or [CountProd] is null ) and\r\n"
			+ "			     a.[MaterialNo] is not null and\r\n"
			+ "			     a.[MaterialNo] <> '' and\r\n"
			+ " 			 a.[DataStatus] = 'O' and \r\n"
			+ "				 a.IsDCOrderPuang = 0\r\n"
			+ "		group by a.[MaterialNo],a.[DocDate],a.[GreigePlan]\r\n"
			+ " ) as CDD on a.MaterialNo = cdd.MaterialNo and\r\n"
			+ "             a.DocDate = cdd.DocDate and\r\n"
			+ "             a.GreigePlan = cdd.GreigePlan\r\n";
	private String leftJoinSPOPD = ""
			+ " left join [PPMM].[dbo].[SOR_POPuangDetail] as SPOPD on a.[POPuangId] = SPOPD.Id AND\r\n"
			+ "                                                        SPOPD.[DataStatus] = 'O' \r\n ";

	private String leftJoinCPNG = ""
			+ "  LEFT JOIN (\r\n"
			+ "		SELECT A.Id,a.PO as checkOP   \r\n"
			+ "		FROM (\r\n"
			+ "			SELECT DISTINCT a.Id ,a.PO , a.POLine,a.OrderQty, a.Article\r\n"
			+ "			from [PPMM].[dbo].[SOR_PODetail]  as a\r\n"
			+ "			left join [PPMM].[dbo].[SOR_TempProd] as b on a.Id = b.POId\r\n"
			+ "			where a.OrderQty < 50 AND\r\n"
			+ "				  A.DataStatus = 'O'and\r\n"
			+ "				  POPuangId is null and\r\n"
			+ "               b.ProductionOrder is null and\r\n"
			+ "				  ( b.[DataStatus] = 'O' or b.[DataStatus] is null )\r\n"
			+ "		) as a\r\n"
			+ "		left join (\r\n"
			+ "			SELECT DISTINCT [Article]  ,min([LotQtyMin]) as minLotQtyMin \r\n"
			+ "			FROM [PPMM].[dbo].[InputArticleSubGroupDetail]\r\n"
			+ "			where DataStatus = 'O' AND LotQtyMin IS NOT NULL \r\n"
			+ "			group by [Article]\r\n"
			+ "		 ) AS b on a.Article = b.Article\r\n"
			+ "		where a.OrderQty < b.minLotQtyMin \r\n"
			+ "	) AS CPNG on a.Id = CPNG.Id \r\n ";
	private String leftJoinD = ""
			+ " LEFT JOIN (\r\n"
			+ "		select distinct Article , count([LotMinMax]) as CountLotMinMax\r\n"
			+ "		from [PPMM].[dbo].[InputArticleSubGroupDetail]\r\n"
			+ "		where LotMinMax is not null and \r\n"
			+ "			  [LotMinMax] <> '' and \r\n"
			+ "			  [LotMinMax] <> '0' and\r\n"
			+ "			  DataStatus = 'O'\r\n"
			+ "		group by Article\r\n"
			+ " ) AS d ON a.Article = d.Article\r\n";
	private String leftJoinF = ""
			+ " LEFT JOIN (\r\n"
			+ "		SELECT [Article] ,[FormulaYD] ,[FormulaPC] ,[FormulaKG] ,[FormulaMR] \r\n"
			+ "		FROM [PPMM].[dbo].[InputConversionDetail]\r\n"
			+ "		where DataStatus = 'O'\r\n"
			+ "	) AS F ON a.Article = F.Article\r\n";

	private String selectFC = ""
			+ "       a.[Id] as No	\r\n"
			+ "      ,[ForecastNo]\r\n"
			+ "      ,[CustomerNo]\r\n"
			+ "      ,[CustomerName]\r\n"
			+ "      ,[ForecastMY]\r\n"
			+ "      ,[TotalForecastQty]\r\n"
			+ "      ,[ForecastBLQty]\r\n"
			+ "      ,[ForecastNonBLQty]\r\n"
			+ "      ,[Unit]\r\n"
			+ "      ,[DocDate]\r\n"
			+ "      ,[DataStatus]\r\n"
			+ "      ,[ChangeDate]\r\n"
			+ "      ,[ChangeBy] \r\n"
			+ "	     ,[CountProd] \r\n"
			+ "		 ,0 as [CheckDivision]\r\n"
			+ "		 ,0 as [CheckArticle]\r\n"
			+ "		 ,0 as [CheckArticleWeight]\r\n"
			+ "      ,0 as [CheckGreigePlan]\r\n";
	private String fromFC = "" + " FROM [PPMM].[dbo].[SOR_ForecastDetail] as a\r\n";
	private String leftJoinFCB = ""
			+ " left join (\r\n"
			+ "		select [ForecastId] , count(ProductionOrder) as CountProd \r\n"
			+ "		from [PPMM].[dbo].[SOR_TempProd] \r\n"
			+ "		group by [ForecastId]\r\n"
			+ "	) as b on a.Id = b.[ForecastId]\r\n ";
	private String selectArticleSubGroupDetail = ""
			+ "	iasg.[Id]\r\n"
			+ ",iasg.[GroupNo]\r\n"
			+ ",iasg.[SubGroup]\r\n"
			+ ",iasg.[Article]\r\n"
			+ ",iasg.[LotMinMax]\r\n"
			+ ",iasg.[LotDif]\r\n"
			+ ",iasg.[IsOverCap]\r\n"
			+ ",iasg.[OverCapQty] \r\n"
			+ ",iasg.[ChangeBy]\r\n"
			+ ",iasg.[ChangeDate]\r\n"
			+ ",iad.[Division]\r\n"
			+ ",imgd.[ColorType]\r\n"
			+ ",[LotQtyMax]\r\n"
			+ ",[LotQtyMin]\r\n"
			+ ",iad.[QtyGreigeMR]\r\n"
			+ ",isgd.[GroupType]\r\n"
			+ ",isgd.[PriorityGroup]\r\n"
			+ ",isgd.[isMinGroup]\r\n"
			+ ",[LotQtyMax] + iasg.[OverCapQtyMax] AS LotQtyMaxWithOC\r\n"
			+ ",[GroupPriority]\r\n";
	private String fromIASG = "" + " FROM [PPMM].[dbo].[InputArticleSubGroupDetail] as iasg\r\n";

	private String innerJoinIAD =
			"" + " inner join [PPMM].[dbo].[InputArticleDetail] as iad on iasg.[Article] = iad.[Article]\r\n";

	private String innerJoinIMGD =
			"" + " inner join [PPMM].[dbo].[InputMainGroupDetail] as imgd on iasg.[GroupNo] = imgd.[GroupNo] \r\n";

	private String innerJoinISGD = ""
			+ " inner join [PPMM].[dbo].[InputSubGroupDeail] as isgd on iasg.[GroupNo] = isgd.[GroupNo] AND\r\n"
			+ "                                                         iasg.SubGroup = isgd.SubGroup\r\n";

	private String leftJoinCountC = ""
			+ " left join (\r\n"
			+ "		select distinct  [POId] , count(ProductionOrder) as CountProd \r\n"
			+ "		from #tempPOMainNPOInstead \r\n"
			+ "     where POId is not null and DataStatus = 'O' "
			+ "		group by [POId]\r\n"
			+ " ) as b on a.Id = b.[POId]\r\n ";

	public ProdCreatedDetailDaoImpl(Database database, String conType) {
		this.database = database;
		this.conType = conType;
		this.message = "";
	}

	private ArrayList<InputPODetail> addBeanPOToList(ArrayList<InputPODetail> listPODataResult, InputPODetail bean)
	{

		String no = bean.getNo();
		String po = bean.getPo();
		String poLine = bean.getPoLine();
		String cusDue = bean.getCustomerDue();
		String colorType = bean.getColorType();
		String cusName = bean.getCustomerName();
		String cusNo = bean.getCustomerNo();
		String isCotton = bean.getIsCotton();
		String color = bean.getColor();
		String division = bean.getDivision();
		String matNo = bean.getMaterialNo();
		String orderQtyCalLast = bean.getOrderQtyCalLast();
		String errorStatus = bean.getErrorStatus();
		String greigePlan = bean.getGreigePlan();
		String specialType = bean.getSpecialType();
		String articleComment = bean.getArticleComment();
		String article = bean.getArticle();
		String unit = bean.getUnit();
		String docDate = bean.getDocDate();
		String colorTypeCheck = bean.getColorTypeCheck();
		java.sql.Date dyeAfterGreigeInBegin = bean.getDyeAfterGreigeInBegin();
		java.sql.Date dyeAfterGreigeInLast = bean.getDyeAfterGreigeInLast();
		String design = bean.getDesign();
		int specialCaseId = bean.getSpecialCaseMRId();
//		int int_opKey = bean.getIntOPKey();
		String groupOptionsTwo = bean.getGroupOptionTwo();
		String groupOptionsOne = bean.getGroupOptionOne();
		int countError = bean.getCheckError();
		int int_qtyGreigeMR = bean.getIntQtyGreigeMR();
		String poType = bean.getPoType();
		String changeBy = bean.getChangeBy();
		InputPODetail beanTmp1 = new InputPODetail();
		beanTmp1.setGreigePlan(greigePlan);
		beanTmp1.setNo(no);
		beanTmp1.setPo(po);
		beanTmp1.setPoLine(poLine);
		beanTmp1.setCustomerDue(cusDue);
		beanTmp1.setColorType(colorType);
		beanTmp1.setCustomerName(cusName);
		beanTmp1.setIsCotton(isCotton);
		beanTmp1.setColor(color);
		beanTmp1.setDivision(division);
		beanTmp1.setMaterialNo(matNo);
		beanTmp1.setOrderQtyCalLast(orderQtyCalLast);
		beanTmp1.setErrorStatus(errorStatus);
		beanTmp1.setSpecialType(specialType);
		beanTmp1.setArticleComment(articleComment);
		beanTmp1.setArticle(article);
		beanTmp1.setUnit(unit);
		beanTmp1.setColorTypeCheck(colorTypeCheck);
		beanTmp1.setDyeAfterGreigeInBegin(dyeAfterGreigeInBegin);
		beanTmp1.setDyeAfterGreigeInLast(dyeAfterGreigeInLast);
		beanTmp1.setDesign(design);
		beanTmp1.setSpecialCaseMRId(specialCaseId);
		beanTmp1.setGroupOptionTwo(groupOptionsTwo);
		beanTmp1.setGroupOptionOne(groupOptionsOne);
		beanTmp1.setCheckError(countError);
		beanTmp1.setIntQtyGreigeMR(int_qtyGreigeMR);
		beanTmp1.setChangeBy(changeBy);
		beanTmp1.setOrderQtyCalLast(bean.getOrderQtyCalLast());
		beanTmp1.setDb_orderQtyCalLast(bean.getDb_orderQtyCalLast());
		beanTmp1.setDocDate(docDate);
		beanTmp1.setCustomerNo(cusNo);
		beanTmp1.setPoType(poType);

		listPODataResult.add(beanTmp1);
		return listPODataResult;
	}

	private ArrayList<InputPODetail> addPOToListPOResult(ArrayList<InputPODetail> listPODataResult,
			ArrayList<InputPODetail> listPOData, int i, double db_poSumLowerQty)
	{
		DecimalFormat dfx = new DecimalFormat("###,###,##0.00");
		InputPODetail bean = listPOData.get(0);
		String no = bean.getNo();
		String po = bean.getPo();
		String poLine = bean.getPoLine();
		String cusDue = bean.getCustomerDue();
		String colorType = bean.getColorType();
		String cusName = bean.getCustomerName();
		String cusNo = bean.getCustomerNo();
		String isCotton = bean.getIsCotton();
		String color = bean.getColor();
		String division = bean.getDivision();
		String matNo = bean.getMaterialNo();
		String orderQtyCalLast = bean.getOrderQtyCalLast();
		String errorStatus = bean.getErrorStatus();
		String specialType = bean.getSpecialType();
		String articleComment = bean.getArticleComment();
		String article = bean.getArticle();
		String unit = bean.getUnit();
		String docDate = bean.getDocDate();
		String colorTypeCheck = bean.getColorTypeCheck();
		java.sql.Date dyeAfterGreigeInBegin = bean.getDyeAfterGreigeInBegin();
		java.sql.Date dyeAfterGreigeInLast = bean.getDyeAfterGreigeInLast();
		String design = bean.getDesign();
		int specialCaseId = bean.getSpecialCaseMRId();
//		int int_opKey = bean.getIntOPKey();
		String groupOptionsTwo = bean.getGroupOptionTwo();
		String groupOptionsOne = bean.getGroupOptionOne();
		int countError = bean.getCheckError();
		int int_qtyGreigeMR = bean.getIntQtyGreigeMR();
		String greigePlan = bean.getGreigePlan();
		String poType = bean.getPoType();
		String changeBy = bean.getChangeBy();
		InputPODetail beanTmp1 = new InputPODetail();
		beanTmp1.setGreigePlan(greigePlan);
		beanTmp1.setNo(no);
		beanTmp1.setPo(po);
		beanTmp1.setPoLine(poLine);
		beanTmp1.setCustomerDue(cusDue);
		beanTmp1.setColorType(colorType);
		beanTmp1.setCustomerName(cusName);
		beanTmp1.setIsCotton(isCotton);
		beanTmp1.setColor(color);
		beanTmp1.setDivision(division);
		beanTmp1.setMaterialNo(matNo);
		beanTmp1.setOrderQtyCalLast(orderQtyCalLast);
		beanTmp1.setErrorStatus(errorStatus);
		beanTmp1.setSpecialType(specialType);
		beanTmp1.setArticleComment(articleComment);
		beanTmp1.setArticle(article);
		beanTmp1.setUnit(unit);
		beanTmp1.setColorTypeCheck(colorTypeCheck);
		beanTmp1.setDyeAfterGreigeInBegin(dyeAfterGreigeInBegin);
		beanTmp1.setDyeAfterGreigeInLast(dyeAfterGreigeInLast);
		beanTmp1.setDesign(design);
		beanTmp1.setSpecialCaseMRId(specialCaseId);
		beanTmp1.setGroupOptionTwo(groupOptionsTwo);
		beanTmp1.setGroupOptionOne(groupOptionsOne);
		beanTmp1.setCheckError(countError);
		beanTmp1.setIntQtyGreigeMR(int_qtyGreigeMR);
		beanTmp1.setChangeBy(changeBy);
		beanTmp1.setIntOPKey(i);
		beanTmp1.setOrderQtyCalLast(dfx.format(db_poSumLowerQty));
		beanTmp1.setDb_orderQtyCalLast(db_poSumLowerQty);
		beanTmp1.setDocDate(docDate);
		beanTmp1.setCustomerNo(cusNo);
		beanTmp1.setPoType(poType);
		listPODataResult.add(beanTmp1);
		return listPODataResult;
	}

	@Override
	public ArrayList<InputPODetail> cancelPOLine(ArrayList<InputPODetail> poList)
	{
		ArrayList<POManagementDetail> poListTemp = new ArrayList<>();
		ArrayList<InputPODetail> listCheck = new ArrayList<>(); 
		SORPODetailChangeModel sdpcModel = new SORPODetailChangeModel(this.conType);
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		POManagementModel pmModel = new POManagementModel(this.conType);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "";
		InputPODetail beanCur = poList.get(0);
		String poId = beanCur.getNo();
		beanCur.setId(Integer.parseInt(poId));
		String remark = beanCur.getRemark();
		String changeBy = beanCur.getChangeBy();
		POManagementDetail beanPO = new POManagementDetail();

		beanPO.setPoId(Integer.parseInt(poId));
		beanPO.setPoPuangId(0);
		beanPO.setDataStatus(Config.C_CLOSE_STATUS);
		beanPO.setRemark(remark);
		beanPO.setChangeBy(changeBy);
		poListTemp.add(beanPO);
		// LOG
		// CANCEL PO POLINE MAIN
		if ( ! poListTemp.isEmpty()) { 
			pmModel.handlerChangeSORPODetailWithDataStatus(poListTemp, Config.C_ACTION_TEXT_DELETE_02);
			spdModel.updateDataStatusWithIdForSORPODetail(poListTemp);
			sdpcModel.upsertSORPODetailChangeWithPOId(poList);
		}
		if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) {
//			String no = poList.get(0).getNo();
//			listCheck = this.getPODetailByPO(Integer.parseInt(no) );
		} else {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = "Something happen ! Please Contact IT.";
		}
		if (listCheck.isEmpty()) {
			InputPODetail bean = new InputPODetail();
			listCheck.add(bean);
		}
		listCheck.get(0).setIconStatus(iconStatus);
		listCheck.get(0).setSystemStatus(systemStatus);
		return listCheck;
	}

	@Override
	public ArrayList<InputPODetail> changePODocDate(ArrayList<InputPODetail> poList)
	{
		ArrayList<InputPODetail> listCheck = new ArrayList<>();
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "";
		iconStatus = spdModel.updateDocDateWithIdForSORPODetail(poList);
		if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) {
			String no = poList.get(0).getNo();
			listCheck = this.getPODetailByPO(Integer.parseInt(no));
		} else {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = "Something happen ! Please Contact IT.";
		}
		if (listCheck.isEmpty()) {
			InputPODetail bean = new InputPODetail();
			listCheck.add(bean);
		}
		listCheck.get(0).setIconStatus(iconStatus);
		listCheck.get(0).setSystemStatus(systemStatus);
		return listCheck;
	}

	@Override
	public ArrayList<InputPODetail> changePOIsDCOrderPuang(ArrayList<InputPODetail> poList)
	{
		ArrayList<InputPODetail> listCheck = new ArrayList<>();
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "";
		iconStatus = spdModel.updateIsDCOrderPuangWithIdForSORPODetail(poList); 
		if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) {
			String no = poList.get(0).getNo();
			listCheck = this.getPODetailByPO(Integer.parseInt(no));
		} else {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = "Something happen ! Please Contact IT.";
		}
		if (listCheck.isEmpty()) {
			InputPODetail bean = new InputPODetail();
			listCheck.add(bean);
		}
		listCheck.get(0).setIconStatus(iconStatus);
		listCheck.get(0).setSystemStatus(systemStatus);
		return listCheck;
	}

	@Override
	public String checkColorType(String colorATT)
	{
		String colorType = "All";
		if (colorATT.contains("BL")) {
			colorType = "Black";
		} else {
			colorType = "Color";
		}
		return colorType;
	}

	private InputGroupDetail checkIsSpecialCase(InputPODetail bean)
	{
		boolean bl_isSpecial = false;
		String color = bean.getColor();
		String article = bean.getArticle();
		String design = bean.getDesign();
		String specialType = bean.getSpecialType();
		String colorType = this.checkColorType(color);
		String colorTypeCheck = bean.getColorTypeCheck();
		String orderQtyCalLast = bean.getOrderQtyCalLast();

		String customerDue = bean.getCustomerDue(); 
		Date dyeAfterGreigeInBegin = bean.getDyeAfterGreigeInBegin();
		Date dyeAfterGreigeInLast = bean.getDyeAfterGreigeInLast();
		double db_baseOrderQty = 0;
		db_baseOrderQty = ParseDouble.tryParseDouble(orderQtyCalLast); 
//		NON_DB = ParseDouble.tryParseDouble(NON_BL_QTY); 
//		try {
//			db_baseOrderQty = (NumberFormat.getNumberInstance().parse(orderQtyCalLast).doubleValue());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String groupOptionsTwo = bean.getGroupOptionTwo();
		String groupOptionsOne = bean.getGroupOptionOne();
		List<String> listAR = new ArrayList<>();
		listAR.add(article);
		InputGroupDetail beanGroup = new InputGroupDetail();
		beanGroup.setDbLotQtyMax(db_baseOrderQty);
		beanGroup.setArticleList(listAR);
		beanGroup.setCustomerDue(customerDue);
		beanGroup.setDyeAfterGreigeInBegin(dyeAfterGreigeInBegin);
		beanGroup.setDyeAfterGreigeInLast(dyeAfterGreigeInLast);
		beanGroup.setSpecialType(specialType);
		// GROUP BL BEAM : BLACK WORK ONLY
		if ((specialType.equals("BLBeam") && colorType.equals(colorTypeCheck) && ! groupOptionsOne.equals(""))) {
			beanGroup.setGroupOptions(groupOptionsOne);
			bl_isSpecial = true;
		}
		// GROUP BEAM : COLOR / BLACK WORK ALL
		else if ((specialType.equals("Beam") &&
//					colorType.equals(colorTypeCheck)&&
				! groupOptionsOne.equals(""))) {
			beanGroup.setGroupOptions(groupOptionsOne);
			bl_isSpecial = true;
		} else if ((specialType.equals("SpeBeam") && colorType.equals(colorTypeCheck))) {
			String designTrim = design.replace(" ", "");
			if (designTrim.contains("NYLONBLACK") && ! groupOptionsTwo.equals("")) {
				beanGroup.setGroupOptions(groupOptionsTwo);
				bl_isSpecial = true;
			} else {
				if ( ! groupOptionsOne.equals("")) {
					bl_isSpecial = true;
					beanGroup.setGroupOptions(groupOptionsOne);
				}
			}
		}
		beanGroup.setSpecial(bl_isSpecial);
		return beanGroup;
	}

	@Override
	public ArrayList<InputForecastDetail> createTempLotFC(ArrayList<InputForecastDetail> poList)
	{
		PlanningProdModel model = new PlanningProdModel(this.conType);
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		InputForecastDetail bean;
		double BL_DB;
		double NON_DB;
		String BL_QTY = "";
		String NON_BL_QTY = "";
		String iconStatus = Config.C_SUC_ICON_STATUS;
		ArrayList<InputTempProdDetail> listTMP;
		String C_BL = "Black";
		String C_NONBL = "Color";
		for (InputForecastDetail element : poList) {
			bean = element;
			listTMP = stpModel.getTempProdDetailByForecastId(bean);
			if ( ! listTMP.isEmpty()) {
			} else {
				BL_QTY = bean.getForecastBLQty();
				NON_BL_QTY = bean.getForecastNonBLQty();
				BL_DB = ParseDouble.tryParseDouble(BL_QTY); 
				NON_DB = ParseDouble.tryParseDouble(NON_BL_QTY); 
//					BL_DB = NumberFormat.getNumberInstance().parse(BL_QTY).doubleValue();
//					NON_DB = NumberFormat.getNumberInstance().parse(NON_BL_QTY).doubleValue();
				iconStatus = this.createTMPFCRunning(bean, BL_DB, iconStatus, C_BL);
				iconStatus = this.createTMPFCRunning(bean, NON_DB, iconStatus, C_NONBL);
			}
		}
		this.searchGroupOptionListForNull();
		model.rePlanningLot();
		ArrayList<InputForecastDetail> list = this.getForecastDetail();
		if (list.isEmpty()) {
			list.add(new InputForecastDetail());
			list.get(0).setIconStatus("NONE");
		} else {
			list.get(0).setIconStatus(iconStatus);
		}
		return list;
	}

	@Override
	public ArrayList<InputTempProdDetail> createTempLotPO(ArrayList<InputPODetail> poList)
	{ 
		ArrayList<InputTempProdDetail> listResult = new ArrayList<>(); 
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		PlanningProdModel ppModel = new PlanningProdModel(this.conType);
		BackGroundJobModel bgjdModel = new BackGroundJobModel(this.conType);
		ReportModel rpModel = new ReportModel(this.conType);
		bgjModel.execUpdateOrderQtyCal(); 
		
		listResult = this.createTempLotPOFromSOR(poList);
		
		this.searchGroupOptionListForNull();
		bgjModel.execHandlerPlanLotSORDetail();
		bgjdModel.execHandlerBatchNoForRelation();
		ppModel.rePlanningLot();
		rpModel.processVolumeForReport(); 
		return listResult;
	} 
	@Override
	public ArrayList<InputTempProdDetail> createTempLotPOFromSOR(ArrayList<InputPODetail> poList)
	{
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		ArticleDetailModel adModel = new ArticleDetailModel(this.conType);
		ArrayList<InputGroupDetail> listAG = new ArrayList<>();
		ArrayList<InputGroupDetail> listSingleAG = new ArrayList<>();
		ArrayList<InputGroupDetail> listAGTmp = new ArrayList<>();
		ArrayList<InputTempProdDetail> listResult = new ArrayList<>();
		ArrayList<InputArticleDetail> listCA = new ArrayList<>();
		InputPODetail bean;
		double db_baseOrderQty;
		String article = "";
		String orderQtyCalLast = "";
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "";
		String matNo = "";
		String color = "";
		String isCotton = "";
		String division = "";
		String specialType = "";
		String poLine = "";
		String no = "";
		String po = "";
		String custNo = "";
		String strPattern = "^0+(?!$)";
		String colorType = "";
		int int_qtyGreigeMR = 0,int_opKey = 0;
		ArrayList<InputPODetail> listCheck = new ArrayList<>();

		int countPrd = 0;
		int countError = 0;
		boolean bl_isHireWork = false;
		boolean bl_isSpecial = false;

		int db_poLowerQty,db_poUpperQty;
		for (int z = 0; z < poList.size(); z ++ ) {
			try {
				listAG.clear();
				listAGTmp.clear();
				listCA.clear();
				bl_isSpecial = false;
				bl_isHireWork = false;

				bean = poList.get(z);
				no = bean.getNo();
				listCheck = this.getPODetailByPO(Integer.parseInt(no));
				orderQtyCalLast = bean.getOrderQtyCalLastPlan();

				isCotton = bean.getIsCotton();
				color = bean.getColor();
				division = bean.getDivision();
				po = bean.getPo();
				poLine = bean.getPoLine();
				matNo = bean.getMaterialNo();
				specialType = bean.getSpecialType();
				article = bean.getArticle();
				int_opKey = bean.getIntOPKey();

				countPrd = 0;
				countError = 0;
				countError = bean.getCheckError();
				int_qtyGreigeMR = bean.getIntQtyGreigeMR();
				colorType = this.checkColorType(color);
				custNo = bean.getCustomerNo();
				custNo = custNo.replaceAll(strPattern, "");
				Date dyeAfterGreigeInBegin = bean.getDyeAfterGreigeInBegin();
				Date dyeAfterGreigeInLast = bean.getDyeAfterGreigeInLast();
				
				InputGroupDetail beanGroup = this.checkIsSpecialCase(bean);
				ArrayList<InputPODetail> listPOSumQty = this.getPODetailForOP(bean, beanGroup, colorType, "POUL");
				if ( ! listPOSumQty.isEmpty()) {
					InputPODetail beanTmp = listPOSumQty.get(0);
					db_poLowerQty = beanTmp.getPoLowerQty();
					db_poUpperQty = beanTmp.getPoUpperQty();
				} else {
					db_poLowerQty = 0;
					db_poUpperQty = 0;
				}
				if (matNo.length() > 0) {
					if (matNo.charAt(0) == 'H') {
						bl_isHireWork = true;
					}
				}
				if ( ! listCheck.isEmpty()) {
					countPrd = listCheck.get(0).getCountProd();
				}

				if (dyeAfterGreigeInBegin == null || dyeAfterGreigeInLast == null) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = " Can't find Rule for Leadtime. Please contact IT. ";
				} else if (orderQtyCalLast == null) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = " Something happen [ OrderQty(M.) ] ! Please contact IT. ";
				} else if (orderQtyCalLast.equals("")) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = " Something happen [ OrderQty(M.) ] ! Please contact IT. ";
				} else if (custNo.equals("61100007") && matNo.equals("KPR500540PR0000")) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = " Unica Lace and MatNo(KPR500540PR0000) can't create lot. ";
				} else if (countPrd > 0) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = po + " / " + poLine + " already create lot by PO. ";
				}
				// คือ Orderpuang boat here
				else if (int_opKey == 0 && ((db_poLowerQty > 1) || (db_poLowerQty == 1 && db_poUpperQty >= 1))) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus =  
							po + " / " + poLine + " have POPuang with same docdate and material no. Need to create POPuang first. ";;
				} else if (countError > 0) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = " PO still has issues that need to be resolved first. ";
				} else if (int_qtyGreigeMR == 0 && ! bl_isHireWork) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = " This article need quantity greige. ";
				} else {
					listCA = adModel.getArticleDetailByArticle(article);
					if (division.equals("CK")) {
						if (isCotton.equals("")) {
							iconStatus = Config.C_ERR_ICON_STATUS;
							systemStatus = " Division CK need to set isCotton in Article Management. ";
							break;
						}
					}
					if (listCA.isEmpty()) {
						iconStatus = Config.C_ERR_ICON_STATUS;
						systemStatus = " Please check config convertion for Article. ";
					} else {
						db_baseOrderQty = ParseDouble.tryParseDouble(orderQtyCalLast);  
						if (db_baseOrderQty % 50 >= 1 && db_baseOrderQty % 50 <= 9 && ! bl_isHireWork) {
							iconStatus = Config.C_ERR_ICON_STATUS;
							systemStatus = " Please check config convertion for Article. ";
						} else { 
							// ปัดให้เป็นจำนวนเต็มหาร 50 ลงตัว
							boolean isGreigeQtyWork = true;
							boolean isQtyGreigeForPO = listCA.get(0).isQtyGreigeForPO();
							String qtyGreigeMR = listCA.get(0).getQtyGreigeMR();
							if ( ! bl_isHireWork) {
								if (isQtyGreigeForPO) {
									int intQtyGreigeMR = 0;
									try {
										intQtyGreigeMR = Integer.parseInt(qtyGreigeMR);
									} catch (Exception e) {
										intQtyGreigeMR = 0;
									}
									if (qtyGreigeMR.equals("") || qtyGreigeMR.equals("0") || intQtyGreigeMR == 0) {
										isGreigeQtyWork = false;
									} else {
										double checkZero = db_baseOrderQty % intQtyGreigeMR;
										if (checkZero == 0) {
											isGreigeQtyWork = true;
											// do notthing best value
										} else {
											isGreigeQtyWork = false;
										}
									}
								} else {
									db_baseOrderQty = this.roundUpDoubleByMod(db_baseOrderQty, this.INT_POQTYMOD);
								}
							} 
							bl_isSpecial = beanGroup.isSpecial();
							beanGroup.setDbLotQtyMax(db_baseOrderQty);
							listAGTmp.clear();
							listAGTmp.add(beanGroup);

							if ( ! isGreigeQtyWork) {	
								iconStatus = Config.C_ERR_ICON_STATUS;
								systemStatus = "Greige Qty must divide PO Qty equals zero. ";
							} else {
								listAG = this.getArticleSubGroupDetailByArticleNColor(listAGTmp, colorType, "");
								if (bl_isSpecial) { 
									listSingleAG = this.getArticleSubGroupDetailByArticleNColor(listAGTmp, colorType, "SINGLE");
									if (listSingleAG.isEmpty()) {
										listSingleAG = this.getArticleSubGroupDetailByArticleNColor(listAGTmp, colorType,
												"SINGLELOWEST");
									}
								}
								// NORMAL
								else { 
									if (listAG.isEmpty()) { 
									} else {
										listSingleAG =
												this.getArticleSubGroupDetailByArticleNColor(listAGTmp, colorType, "SINGLE");
										if (listSingleAG.isEmpty()) {
											listSingleAG = this.getArticleSubGroupDetailByArticleNColor(listAGTmp, colorType,
													"SINGLELOWEST");
										}
									}
								}
								if (listAG.isEmpty() && bl_isSpecial) {
									if (int_opKey != 0) {
									} else {
										spdModel.updateIsGroupRecheckForSORPODetail(true, no);
									}
									iconStatus = Config.C_ERR_ICON_STATUS;
									systemStatus = " Case '" + specialType + "' | Can't create anygroup. Need PC adjust PO Qty.";
								} else if (listAG.isEmpty()) {
									if (int_opKey != 0) {
									} else {
										spdModel.updateIsGroupRecheckForSORPODetail(true, no);
									}
									iconStatus = Config.C_ERR_ICON_STATUS;
									systemStatus = " Can't create anygroup. Need PC adjust PO Qty. ";
								} else {
									listResult = this.recreateTMPProdOrder(bean, db_baseOrderQty, iconStatus, listAG, colorType,
											listSingleAG, true);
									if (listResult == null) {
										iconStatus = Config.C_ERR_ICON_STATUS;
										systemStatus =
												"There are no groups that can create production order. Please contact IT. ";
									} else if (listResult.isEmpty()) {
										iconStatus = Config.C_ERR_ICON_STATUS;
										systemStatus = "There are no groups that can create production order. Please contact IT.";
									} else {
										iconStatus = Config.C_SUC_ICON_STATUS;
										systemStatus = "  ";
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				iconStatus = Config.C_ERR_ICON_STATUS;
				systemStatus = Config.C_ERROR_TEXT+e.getMessage();
			}
		} 
		if (listResult.isEmpty()) {
			listResult.add(new InputTempProdDetail());
			listResult.get(0).setIconStatus(iconStatus);
			listResult.get(0).setSystemStatus(systemStatus);
		} else {
			listResult.get(0).setIconStatus(iconStatus);
			listResult.get(0).setSystemStatus(systemStatus);
		}
		return listResult;
	}

	@Override
	public ArrayList<InputTempProdDetail> createTempLotPOPuang(ArrayList<SorPODetailList> poList)
	{
		SorPODetailList bean = poList.get(0);
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		ReportModel rpModel = new ReportModel(this.conType);
		SorPOPuangDetailModel spopdModel = new SorPOPuangDetailModel(this.conType);
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		SorPOPuangDetailModel sppdModel = new SorPOPuangDetailModel();
		RelationPOAndProdOrderModel rpapModel = new RelationPOAndProdOrderModel(this.conType);
		ArrayList<InputPODetail> listPODataResult = bean.getListPOResult();
		ArrayList<InputPODetail> listPODataShow = bean.getListPOShow();
		ArrayList<InputPODetail> listPOResult = new ArrayList<>();
		ArrayList<InputTempProdDetail> listAfterCreate = new ArrayList<>();
		String iconStatus = Config.C_ERR_ICON_STATUS;
		String systemStatus = " PO already puang and created lot. ";
		ArrayList<InputPODetail> listSum = this.getPOSumDetailForOP(listPODataResult);
		if (listSum.size() > 0) {
			InputPODetail beanTmp = listPODataResult.get(0);
			String color = beanTmp.getColor();
			String colorType = this.checkColorType(color);
			InputGroupDetail beanGroup = this.checkIsSpecialCase(beanTmp);
			ArrayList<InputPODetail> listPOSumQty = this.getPODetailForOP(beanTmp, beanGroup, colorType, "POUL");
			int db_poLowerQty = 0;
			int db_poUpperQty = 0;
			if (listPOSumQty.size() > 0) {
				InputPODetail beanTmp1 = listPOSumQty.get(0);
				db_poLowerQty = beanTmp1.getPoLowerQty();
				db_poUpperQty = beanTmp1.getPoUpperQty();
			}
			if ((db_poLowerQty > 1) || (db_poLowerQty == 1 && db_poUpperQty >= 1)) {
				int int_IdPOMainPuang = listSum.get(0).getPoPuangId();
				if (int_IdPOMainPuang == 0) {
					// here
					for (InputPODetail beanPDR : listPODataResult) {
						beanPDR.setCheckError(0);
						beanPDR.setOrderQtyCalLastPlan(beanPDR.getOrderQtyCalLast());
					}
					listAfterCreate = this.createTempLotPO(listPODataResult);
					if (listAfterCreate.get(0).getIconStatus().equals(Config.C_SUC_ICON_STATUS)) {
						sppdModel.insertPOPuangDetail(listPODataResult);
						listPOResult = spopdModel.getPOPuangDetailByPOId(listSum);
						if ( ! listPOResult.isEmpty()) {
							String poPuangId = listPOResult.get(0).getNo();
							spdModel.updatePOPuangForSORPODetail(listPODataShow, poPuangId);
						}
						String poIdForSearch = listSum.get(0).getNo();
						ArrayList<InputTempProdDetail> listTempProd = spdModel.getSORPODetailWithPuangDetailByPOId(poIdForSearch);
						if ( ! listTempProd.isEmpty()) {
							for (InputTempProdDetail element : listTempProd) {
								element.setChangeBy(bean.getChangeBy()); 
							}
							rpapModel.upsertRelationPOAndProdOrder(listTempProd);
							rpModel.processVolumeForReport();
						}
						// listPO = this.getPODetail();
						iconStatus = listAfterCreate.get(0).getIconStatus();
						systemStatus = listAfterCreate.get(0).getSystemStatus();
					} else {
						iconStatus = listAfterCreate.get(0).getIconStatus();
						systemStatus = listAfterCreate.get(0).getSystemStatus();
					}
				}
			}
		}
		bgjModel.execHandlerBatchNoForRelation();
		bgjModel.execHandlerPlanLotSORDetail();
		if (listAfterCreate.isEmpty()) {
			listAfterCreate.add(new InputTempProdDetail());
		}
		listAfterCreate.get(0).setIconStatus(iconStatus);
		listAfterCreate.get(0).setSystemStatus(systemStatus);
		return listAfterCreate;
	}

	private String createTMPFCRunning(InputForecastDetail bean, double orderQty, String iconStatus, String ColorType) {
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		TempForecastRunningModel tfrModel = new TempForecastRunningModel(this.conType);
		String lv_yyyy = C_yyyy.format(Calendar.getInstance().getTime());
		String runningStr = "";
		String runningLotFC = "";
		double c_300 = 300.00;
		int running = 1;
		int checkFLot = 0;
		int counterBatch = 1; // will be 2;

		ArrayList<InputTempProdDetail> listTemp = new ArrayList<>();
		String lv_yyMM = C_yyMM.format(Calendar.getInstance().getTime());
		ArrayList<ForecastRunningDetail> list = tfrModel.getForecastRunningDetail(lv_yyyy);
		if ( ! list.isEmpty()) {
			running = list.get(0).getRunningNo();
		}

		InputTempProdDetail beanTemp = new InputTempProdDetail();
		double db_totalBatch = Math.ceil(orderQty / 300); // will be 2;
		int totalBatch = (int) db_totalBatch; // will be 2;
		String batchNo = "";
		while (orderQty >= 0) {
			beanTemp = new InputTempProdDetail();
			runningStr = String.format("%04d", running);
			runningLotFC = "F" + lv_yyMM + runningStr;
			batchNo = Integer.toString(counterBatch) + "/" + totalBatch;
			// ------------- set data ------------
			beanTemp.setForecastId(Integer.parseInt(bean.getNo()));
			beanTemp.setFirstLot("N");
			beanTemp.setProductionOrder(runningLotFC);
			beanTemp.setChangeBy(bean.getChangeBy());
			beanTemp.setColorType(ColorType);
			beanTemp.setBatch(batchNo);
			beanTemp.setDbProductionOrderQty(c_300);
			listTemp.add(beanTemp);
			// ------------- minus ------------
			orderQty = orderQty-300;
			running = running+1;
			checkFLot = checkFLot+1;
			counterBatch += 1;
		}
		orderQty = (orderQty+300);
		orderQty = this.roundUpDouble(orderQty);
		if (orderQty != 0) {
			runningStr = String.format("%04d", running);
			runningLotFC = "F" + lv_yyMM + runningStr;
			// ------------- set data ------------
			batchNo = Integer.toString(counterBatch) + "/" + totalBatch;
			beanTemp.setForecastId(Integer.parseInt(bean.getNo()));
			beanTemp.setFirstLot("N");
			beanTemp.setProductionOrder(runningLotFC);
			beanTemp.setChangeBy(bean.getChangeBy());
			beanTemp.setColorType(ColorType);
			beanTemp.setBatch(batchNo);
			beanTemp.setDbProductionOrderQty(orderQty);
			listTemp.add(beanTemp);
			// ------------- minus ------------
			running = running+1;
			counterBatch += 1;
		} 
		iconStatus = stpModel.insertSORTEMPProdWithForecastTempLot(listTemp, this.C_PENDING);
		iconStatus = tfrModel.upsertForecastRunningDetail(lv_yyyy, running);
		bgjModel.execUpsertForecastGroupOptions();
		return iconStatus;
	}

//	public double roundToNearest(double x) {
//	    if (x%50 < 25) {
//	        return x - (x%50);
//	    }
//	    else if (x%50 > 25) {
//	        return x + (50 - (x%50));
//	    }
//	    else if (x%50 == 25) {
//	        return x + 25; //when it is halfawy between the nearest 50 it will automatically round up, change this line to 'return x - 25' if you want it to automatically round down
//	    }
//		return x;
//	}

	private int findOrderPuangCase(double db_poSumLowerQtyAfterMod, double db_lowerPOMinQty, double db_lowerPOMaxQty,
			double db_upperPOMinQty, double db_upperPOMaxQty, int db_poUpperQty, boolean bl_isSpecial)
	{
		int casePOData = 0;
		// CASE SPECIAL 1
		if (db_lowerPOMinQty > db_poSumLowerQtyAfterMod) {
			// รวมกับตัวในกลุ่ม Group UPPER ที่ค่าน้อยสุด
			if (db_poUpperQty >= 1) {
				casePOData = 1;
			}
			// push to mingroup
			else {
				casePOData = 10;
			}
		}
		// CASE SPECIAL 2
		else if (db_lowerPOMinQty <= db_poSumLowerQtyAfterMod && db_poSumLowerQtyAfterMod <= db_lowerPOMaxQty) {
			// รวมกับตัวในกลุ่ม Group UPPER ที่ค่าน้อยสุด
			if (db_poUpperQty >= 1) {
				casePOData = 1;
			}
			// work in min group
			else {
				casePOData = 4;
			}
		}
		// case ไม่อยู่ในช่วง qty ที่่วางแผนได้
		else if (db_upperPOMinQty > db_poSumLowerQtyAfterMod) {
			// รวมกับตัวในกลุ่ม Group UPPER ที่ค่าน้อยสุด
			if (db_poUpperQty >= 1) {
				casePOData = 1;
			}
			// case ไม่อยู่ในช่วง qty ที่่วางแผนได้ ต้องแก้ไขจำนวน
			else {
				if (bl_isSpecial) {
					casePOData = 99;
				} else {
					casePOData = 20;
				}
			}
		}
		// case อยู่ในช้่วงที่สร้างได้ 1 lot พอดีจากตัวพ่วง
		else if (db_upperPOMinQty <= db_poSumLowerQtyAfterMod && db_poSumLowerQtyAfterMod <= db_upperPOMaxQty) {
			casePOData = 5;
		}
		// case มากกว่า group ที่มากที่สุดของ Group ต้องทำการเฉลียน po มารวมกัน
		else if (db_upperPOMaxQty < db_poSumLowerQtyAfterMod) {
			casePOData = 40;
		}
		return casePOData;
	}

	public ArrayList<InputGroupDetail> getArticleSubGroupDetailByArticleListNColor(ArrayList<InputGroupDetail> poList,
			String colorType)
	{
		ArrayList<InputGroupDetail> list = null;
		InputGroupDetail bean = poList.get(0);
		List<String> articleList = bean.getArticleList();
		String whereArticle = " (  \r\n";
		for (int i = 0; i < articleList.size(); i ++ ) {
			whereArticle += " iasg.Article = '" + articleList.get(i) + "' ";
			if (i < articleList.size()-1) {
				whereArticle += " or \r\n";
			}
		}
		whereArticle += " ) \r\n";
		whereArticle += " and ( imgd.[ColorType] = '" + colorType + "' or imgd.[ColorType] = 'All' ) \r\n";
		String sql = ""
				+ "SELECT distinct \r\n"
				+ this.selectArticleSubGroupDetail
				+ this.fromIASG
				+ this.innerJoinIAD
				+ this.innerJoinIMGD
				+ this.innerJoinISGD
				+ " where iasg.DataStatus = 'O' and\r\n"
				+ "		  iad.DataStatus = 'O' and\r\n"
				+ "	      iasg.[LotMinMax] <> '' and\r\n"
				+ "	      isgd.[DataStatus] = 'O' and\r\n"
				+ whereArticle
				+ " Order By iasg.[LotMinMax] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genGroupDetail(map));
		}
		return list;
	}

	public ArrayList<InputGroupDetail> getArticleSubGroupDetailByArticleNColor(ArrayList<InputGroupDetail> poList,
			String colorType, String caseStr)
	{
		ArrayList<InputGroupDetail> list = new ArrayList<>();
		InputGroupDetail bean = poList.get(0);
		Date dyeAfterGreigeBegin = bean.getDyeAfterGreigeInBegin();
		Date dyeAfterGreigeLast = bean.getDyeAfterGreigeInLast();
		double db_baseOrderQty = bean.getDbLotQtyMax();
		String orderQty = df2.format(db_baseOrderQty);
		String specialType = bean.getSpecialType();
		orderQty = orderQty.replace(Config.C_COMMA, "");
		if (dyeAfterGreigeBegin != null || dyeAfterGreigeLast != null) {
			int result = dyeAfterGreigeBegin.compareTo(dyeAfterGreigeLast);
			String groupOption = bean.getGroupOptions();
			String whereSpeGroupMain = "";
			String whereSpeGroupMinPrior = "";
			boolean bl_isSpecial = bean.isSpecial();
			String sql = " ";
			String selectTemp = " ";
			List<String> articleList = bean.getArticleList();
			String whereArticle = " 			(  \r\n";
			for (int i = 0; i < articleList.size(); i ++ ) {
				whereArticle += " 		iasg.Article = '" + articleList.get(i) + "' ";
				if (i < articleList.size()-1) {
					whereArticle += " or \r\n";
				}
			}
			whereArticle += " 		) \r\n";
			whereArticle += "" + " 		and (  imgd.[ColorType] = '" + colorType + "' or  imgd.[ColorType] = 'All' ) \r\n";
			String wherePOQty = "";
			if (caseStr.equals("NOPOQTY")) {
				wherePOQty += "";
			} else if (caseStr.equals("SINGLELOWEST")) {
				wherePOQty += "" + " and a.GroupType = 'Single'\r\n";
			} else if (caseStr.equals("SINGLE")) {
				wherePOQty += ""
						+ " and a.GroupType = 'Single'\r\n"
						+ " and (\r\n"
						+ "			a.LotQtyMax <= '" + orderQty + "' or \r\n"
						+ "	      ( \r\n"
						+ "			a.LotQtyMin <= '" + orderQty + "' and \r\n"
						+ "			'" + orderQty + "' <= a.LotQtyMaxWithOC\r\n"
						+ "		  ) \r\n"
						+ "     ) \r\n";
			} else {
				wherePOQty += ""
						+ " and (\r\n"
						+ "		 a.LotQtyMax <= '" + orderQty + "' or \r\n"
						+ "	      (\r\n"
						+ "			a.LotQtyMin <= '" + orderQty + "' and \r\n"
						+ "			'" + orderQty + "' <= a.LotQtyMaxWithOC \r\n"
						+ "		  ) \r\n"
						+ "     ) \r\n";
			}
			if (specialType.equals("BLBeam") && ! bl_isSpecial) {
				wherePOQty += " AND  a.GroupNo <> 'G7' \r\n";
			}
			if ( ! specialType.equals("") && bl_isSpecial) {
				String[] arrayGO = groupOption.split(Config.C_COMMA);
				if ( ! groupOption.equals("") && arrayGO.length >= 1) {
					whereSpeGroupMain = " and ( \r\n";
					for (int i = 0; i < arrayGO.length; i ++ ) {
						whereSpeGroupMain += " a.GroupNo = '" + arrayGO[i] + "' ";
						if (i < arrayGO.length-1) {
							whereSpeGroupMain += " or ";
						}
					}
					whereSpeGroupMain += ""
							+ " and ( [LotQtyMax] <= '" + orderQty + "' or  \r\n"
							+ "			(\r\n"
							+ "				LotQtyMin <= '" + orderQty + "' and \r\n"
							+ "			    '" + orderQty + "' <= LotQtyMaxWithOC\r\n"
							+ "			)\r\n"
							+ "		)\r\n";
					whereSpeGroupMain += "" + " ) \r\n";
					whereSpeGroupMinPrior += ""
							+ " and (\r\n"
							+ "			LotQtyMin <= '" + orderQty + "' and \r\n"
							+ "			'" + orderQty + "' <= LotQtyMaxWithOC\r\n"
							+ "		) \r\n";
				}
			}
			sql = " ";
			selectTemp = " "
					+ "	declare @DyeStartDate date  = convert(date , '" + dyeAfterGreigeBegin + "', 126) ;\r\n" // YYYY-MM-DD
					+ "	declare @DyeEndDate  date  = convert(date , '" + dyeAfterGreigeLast + "', 126) ;\r\n"
					+ " 	declare @IsBeforeDyeStartDate  bit  = 0;\r\n"
					+ "	IF @DyeStartDate <= @DyeEndDate \r\n"
					+ "		SET @IsBeforeDyeStartDate = 0 ;\r\n"
					+ "	ELSE \r\n"
					+ "		SET @IsBeforeDyeStartDate = 1 ; \r\n"
					+ "		\r\n"
					+ "	IF OBJECT_ID('tempdb..#tempPriorWorkNumber') IS NOT NULL \r\n"
					+ "		DROP TABLE #tempPriorWorkNumber;  \r\n"
					+ "	select \r\n"
					+ "	--  RANK() OVER(  ORDER BY   MinWorkDate    ) as PriorWorkNubmer\r\n"
					+ "	--, ROW_NUMBER() OVER( ORDER BY   MinWorkDate   ) as PriorWorkNubmer\r\n"
					+ "	 DENSE_RANK() OVER( ORDER BY   MinWorkDate     ) as PriorWorkNubmer\r\n"
					+ "	, a.[GroupNo] ,a.[SubGroup] ,a.[LotPerDay],MinWorkDate\r\n"
					+ "	into #tempPriorWorkNumber\r\n"
					+ "	FROM (\r\n"
					+ "		select  a.[GroupNo] ,a.[SubGroup] ,a.[LotPerDay] , min([WorkDate]) as MinWorkDate\r\n"
					+ "		from (\r\n"
					+ "			SELECT distinct gwd.[GroupNo] ,gwd.[SubGroup] , gwd.[WorkDate] ,b.[LotPerDay]\r\n"
					+ "				, COUNT( [PlanSystemDate] )  as totalLotDate\r\n"
					+ "  		FROM [PPMM].[dbo].[GroupWorkDate] as gwd\r\n"
					+ "			left join [PPMM].[dbo].[viewTEMPPlanningLotOnProcess] as a on \r\n"
					+ "					gwd.[GroupNo] = a.[GroupNo] and \r\n"
					+ "					gwd.[SubGroup] = a.[SubGroup] and \r\n"
					+ "					gwd.[WorkDate] = a.[PlanSystemDate]\r\n"
					+ "  		left join [PPMM].[dbo].[InputSubGroupDeail] as b on gwd.[GroupNo] = b.[GroupNo] and\r\n"
					+ "                                                             gwd.[SubGroup] = b.[SubGroup] \r\n"
					+ "  		where ( b.[LotPerDay] is not null and b.[LotPerDay] > 0 ) and\r\n"
					+ "					gwd.[WorkDate] > GETDATE() and \r\n"
					+ "					gwd.[NormalWork] = 'O'\r\n"
					+ "  				group by gwd.[GroupNo] ,gwd.[SubGroup] , gwd.[WorkDate] , b.[LotPerDay] \r\n"
					+ "			) as a\r\n"
					+ "		where a.[LotPerDay] > totalLotDate\r\n"
					+ "  	group by a.[GroupNo] ,a.[SubGroup] ,a.[LotPerDay]\r\n"
					+ "	 ) AS A\r\n"
					+ "	 ORDER BY MinWorkDate,  a.[GroupNo] ,a.[SubGroup]\r\n" // YYYY-MM-DD
					+ this.declarePOMainNPOInstead
					+ this.declareSixMonthAgo
					+ " declare @CountOtherGroup int, @CountG5NG6 int;\r\n"
					+ " IF OBJECT_ID('tempdb..#tempTable') IS NOT NULL   \r\n"
					+ "	   DROP TABLE #tempTable ;\r\n"
					+ " select *\r\n"
					+ " into #tempTable\r\n"
					+ " from (\r\n"
					+ "  	SELECT distinct \r\n"
					+ "     " + this.selectArticleSubGroupDetail
					+ "		 , f0.CountGroupSubWorkDate - ISNULL(e0.[CountWorkDate],0)  as CountWorkDate\r\n"
					+ "     " + this.fromIASG
					+ "     " + this.innerJoinIAD
					+ "     " + this.innerJoinIMGD
					+ "     " + this.innerJoinISGD
					+ "  	left join ( \r\n "
					+ "			SELECT distinct a.[GroupNo] ,a.[SubGroup] , b.[LotPerDay] , ISNULL(COUNT( [PlanSystemDate] ) /b.[LotPerDay] ,0) AS CountWorkDate\r\n"
					+ "  		FROM [PPMM].[dbo].[viewTEMPPlanningLotOnProcess] as a\r\n"
					+ "			left join [PPMM].[dbo].[SOR_TempProd] as stp on a.TempProdId = stp.id \r\n"
					+ "  		left join [PPMM].[dbo].[InputSubGroupDeail] as b on a.[GroupNo] = b.[GroupNo] and\r\n"
					+ "                                                             a.[SubGroup] = b.[SubGroup]\r\n"
					+ "  		where a.[DataStatus] = 'O' and \r\n"
					+ "             ( stp.[DataStatus] is not null or ( stp.[DataStatus] = 'O' and stp.[ForecastId] is null ) ) and\r\n"
					+ "		     	( [PlanSystemDate] >= @DyeStartDate  and  [PlanSystemDate] <=  @DyeEndDate  ) and \r\n"
					+ " 		 	( b.[LotPerDay] is not null and b.[LotPerDay] > 0 ) \r\n"
					+ "  		group by a.[GroupNo] ,a.[SubGroup],b.[LotPerDay] \r\n"
					+ "   	) as e0 on iasg.[GroupNo] = e0.[GroupNo] and\r\n"
					+ "                iasg.[SubGroup] = e0.[SubGroup]  \r\n "
					+ "		left join  (\r\n"
					+ "			SELECT [GroupNo] ,[SubGroup] , COUNT(A.NormalWork) as CountGroupSubWorkDate\r\n"
					+ "  		FROM [PPMM].[dbo].[GroupWorkDate] as a\r\n"
					+ "  		where  a.DataStatus = 'O' AND \r\n"
					+ "                a.NormalWork = 'O' and \r\n"
					+ "			 	   ( a.[WorkDate] >=  @DyeStartDate and\r\n"
					+ "                a.[WorkDate] <=  @DyeEndDate  )\r\n"
					+ "			GROUP BY a.GroupNo,a.SubGroup\r\n"
					+ "	  	) as f0 on iasg.[GroupNo] = f0.[GroupNo] AND\r\n"
					+ "                iasg.[SubGroup] = f0.[SubGroup] \r\n"
					+ "  	where iasg.DataStatus = 'O' and\r\n"
					+ "			  iad.DataStatus = 'O' and\r\n"
					+ "			  iasg.[LotMinMax] <> '' and\r\n"
					+ "			  isgd.[DataStatus] = 'O' and\r\n"
					+ "			  ( isgd.[LotPerDay] is not null and isgd.[LotPerDay] > 0 ) and\r\n"
					+ whereArticle
					+ "	) as a\r\n"
					+ "	where a.Id is not null \r\n"
					+ wherePOQty
					+ " select @CountOtherGroup = ISNULL(SUM(1),0)\r\n"
					+ " from #tempTable as a\r\n"
					+ " where [isMinGroup] = 0 \r\n"
					+ whereSpeGroupMain
					+ " select @CountG5NG6 = ISNULL(SUM(1),0)\r\n"
					+ " from #tempTable as a\r\n"
					+ " where [isMinGroup] = 1 \r\n "
					+ whereSpeGroupMinPrior;

			String orderBy = "";
			String orderByGroupFiveSix = "";
			orderByGroupFiveSix = " Order By [LotQtyMaxWithOC] desc,[LotQtyMin] desc, [GroupNo], [SubGroup] ; \r\n";
			String leftJoinB =
					"" + " LEFT JOIN #tempPriorWorkNumber as b on a.[GroupNo] = b.[GroupNo] and\r\n"
					   + "                                        a.[SubGroup] = b.[SubGroup]\r\n";
			if (result < 0) {
				orderBy = ""
						+ " Order By a.CountWorkDate desc ,a.[LotQtyMaxWithOC] desc,a.[LotQtyMin] desc, a.[GroupNo], a.[SubGroup] ;\r\n";
			} else {
				orderBy = " Order By PriorWorkNubmer , [LotQtyMaxWithOC] desc,[LotQtyMin] desc, [GroupNo], [SubGroup] ;\r\n";
			}
			if (caseStr.equals("NOPOQTY")) {
				sql += selectTemp
						+ "  select * \r\n"
						+ "  ,case \r\n"
						+ "		when LotQtyMin = 0 or LotQtyMin is null then LotQtyMax\r\n"
						+ "		else LotQtyMin\r\n"
						+ "		end as LotQtyMinimun\r\n"
						+ " from #tempTable\r\n"
						+ " Order By PriorityGroup asc, [GroupNo], [SubGroup]  \r\n";
			}
			// BEAM ONLY WITH SINGLE BCOZ BEAM GROUP 7 NO MAIN SINGLE AND NO DOUBLE
			else if (caseStr.equals("SINGLE") && ( ! specialType.equals("") && bl_isSpecial)
					&& whereSpeGroupMain.contains("G7")) {
				sql += selectTemp
						+ " if @CountOtherGroup > 0  \r\n"
						+ "		select a.* \r\n"
						+ "		from #tempTable as a\r\n"
						+ leftJoinB
						+ "		where [isMinGroup] = 0\r\n"
						+ whereSpeGroupMain
						+ orderBy
						+ " else\r\n"
						+ "		select a.* \r\n"
						+ "		from #tempTable as a\r\n"
						// + leftJoinB
						+ "		where [isMinGroup] = 1\r\n"
						+ whereSpeGroupMinPrior
						+ orderByGroupFiveSix
				// + orderBy
				;

			}
			// NORMAL CASE
			else if (caseStr.equals("SINGLE")) {
				sql += selectTemp
						+ "		select a.* \r\n"
						+ "		from #tempTable as a\r\n"
						+ " Order By [PriorityGroup] , [GroupNo], [SubGroup] \r\n";
			}
			// UNDER MIN CASE
			else if (caseStr.equals("SINGLELOWEST")) {
				sql += selectTemp
						+ " select * \r\n"
						+ " from #tempTable\r\n"
						+ " Order By  [LotQtyMin], [GroupNo], [SubGroup] \r\n";
			} else {
				sql += selectTemp
						+ " if @CountOtherGroup > 0  \r\n"
						+ "		select a.* \r\n"
						+ "		from #tempTable as a\r\n"
						+ leftJoinB
						+ "		where [isMinGroup] = 0\r\n"
						+ whereSpeGroupMain
						+ orderBy
						+ " else\r\n"
						+ "		select a.* \r\n"
						+ "		from #tempTable as a\r\n"
						+ "		where [isMinGroup] = 1\r\n"
						+ whereSpeGroupMinPrior
						+ orderByGroupFiveSix;
			} 
			List<Map<String, Object>> datas = this.database.queryList(sql);
			for (Map<String, Object> map : datas) {
				list.add(this.bcModel._genGroupDetail(map));
			} 
		} else {

		}
		return list;
	}

	@Override
	public ArrayList<InputGroupDetail> getBaseArticleSubGroupDetail(ArrayList<InputGroupDetail> poList, String colorType,
			String caseStr)
	{
		ArrayList<InputGroupDetail> list = null;
		InputGroupDetail bean = poList.get(0);
		double db_baseOrderQty = bean.getDbLotQtyMax();
		List<String> articleList = bean.getArticleList();
		String orderQty = df2.format(db_baseOrderQty);
		orderQty = orderQty.replace(Config.C_COMMA, "");
//		String orderBy = "";

		String whereSpeGroupMain = "";
		String whereSpeGroupMinPrior = "";
		String sql = " ";
		String selectTemp = " ";
		String whereArticle = " 			(  \r\n";
		for (int i = 0; i < articleList.size(); i ++ ) {
			whereArticle += " 		iasg.Article = '" + articleList.get(i) + "' ";
			if (i < articleList.size()-1) {
				whereArticle += " or \r\n";
			}
		}
		whereArticle += " 		) \r\n";
		whereArticle += "" + " 		and (  imgd.[ColorType] = '" + colorType + "' or  imgd.[ColorType] = 'All' ) \r\n";
		String wherePOQty = "";
		if (caseStr.equals("SINGLELOWEST")) {
			wherePOQty += "" + " and a.GroupType = 'Single'\r\n";
		} else if (caseStr.equals("SINGLE")) {
			wherePOQty += ""
					+ " and a.GroupType = 'Single'\r\n"
					+ " and ( a.LotQtyMax <= '"
					+ orderQty
					+ "' or \r\n"
					+ "	      ( a.LotQtyMin <= '"
					+ orderQty
					+ "' and '"
					+ orderQty
					+ "' <= a.LotQtyMaxWithOC  ) \r\n"
					+ "     ) \r\n";
		}
		sql = " ";
		selectTemp = " \r\n"
				+ " declare @CountOtherGroup int, @CountG5NG6 int;\r\n"
				+ " IF OBJECT_ID('tempdb..#tempTable') IS NOT NULL   \r\n"
				+ "	   DROP TABLE #tempTable ;\r\n"
				+ " select *\r\n"
				+ " into #tempTable\r\n"
				+ " from (\r\n"
				+ "  	SELECT distinct \r\n"
				+ "     "
				+ this.selectArticleSubGroupDetail
				+ "     "
				+ this.fromIASG
				+ "     "
				+ this.innerJoinIAD
				+ "     "
				+ this.innerJoinIMGD
				+ "     "
				+ this.innerJoinISGD
				+ "  	where iasg.DataStatus = 'O' and\r\n"
				+ "			  iad.DataStatus = 'O' and\r\n"
				+ "			  iasg.[LotMinMax] <> '' and\r\n"
				+ "			  isgd.[DataStatus] = 'O' and\r\n"
				+ whereArticle
				+ "	) as a\r\n"
				+ "	where a.Id is not null \r\n"
				+ wherePOQty
//				+ whereGroupType
				+ " select @CountOtherGroup = ISNULL(SUM(1),0)\r\n"
				+ " from #tempTable\r\n"
				+ " where [isMinGroup] = 0 \r\n"
				+ whereSpeGroupMain
				+ " select @CountG5NG6 = ISNULL(SUM(1),0)\r\n"
				+ " from #tempTable\r\n"
				+ " where [isMinGroup] = 1 \r\n "
				+ whereSpeGroupMinPrior;
		// NORMAL CASE
		if (caseStr.equals("SINGLE")) {
			sql += selectTemp
					+ " select * \r\n"
					+ " from #tempTable\r\n"
					+ " Order By [PriorityGroup] , [GroupNo], [SubGroup] \r\n";
		}
		// UNDER MIN CASE
		else if (caseStr.equals("SINGLELOWEST")) {
			sql += selectTemp + " select * \r\n" + " from #tempTable\r\n" + " Order By  [LotQtyMin], [GroupNo], [SubGroup] \r\n";
		}
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genGroupDetail(map));
		}
		return list;
	}

	@Override
	public String getFirstPosLot(String division, String isCotton)
	{
		String firstPos = "";
		switch (division) {
		case "TR":
			firstPos = "T";
			break;
		case "TW":
			firstPos = "W";
			break;
		case "PN":
			firstPos = "P";
			break;
		case "OT":
			firstPos = "O";
			break;
		case "CK":
			if (isCotton.equals("Y")) {
				firstPos = "C";
			} else {
				firstPos = "S";
			}
			break;
		}
		return firstPos;
	}

	@Override
	public ArrayList<InputForecastDetail> getForecastDetail()
	{
		ArrayList<InputForecastDetail> list = null;
		String sql = ""
				+ " SELECT distinct \r\n"
				+ this.selectFC
				+ this.fromFC
				+ this.leftJoinFCB
				+ " where [CountProd] = 0 or [CountProd] is null \r\n "
				+ " order by [CountProd] ,[DocDate] desc";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genForecastDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputForecastDetail> getForecastDetail(String caseTime)
	{
		String where = "";
		ArrayList<InputForecastDetail> list = null;
		if (caseTime.equals("15")) {
			where = " and a.[ChangeDate] >= DATEADD(MINUTE,-15,GETDATE()) and [isTryCreateLot] = 0\r\n ";
		}
		String sql = ""
				+ " SELECT distinct \r\n"
				+ this.selectFC
				+ this.fromFC
				+ this.leftJoinFCB
				+ " where ( [CountProd] = 0 or [CountProd] is null ) \r\n "
				+ where
				+ " order by [CountProd] ,[DocDate] desc";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genForecastDetail(map));
		}
		return list;
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<InputPlanLotRedyeDetail> getPlanLotRedyeDetail()
	{
		ArrayList<InputPlanLotRedyeDetail> list = null;
		String sql = ""
				+ " SELECT distinct \r\n"
				+ "       e.[Id] as No	\r\n"
				+ "      ,a.[ProductionOrder] \r\n"
				+ "      ,[CustomerDue] \r\n"
				+ "      ,[Article] \r\n"
				+ "      ,[FGDesign] \r\n"
				+ "      ,[DueDate]\r\n"
				+ "      ,a.[Operation] \r\n"
				+ "      ,a.[Shade]\r\n"
				+ "      ,[MaterialNumber]\r\n"
				+ "      ,a.[LabStatus]\r\n"
				+ "      ,a.[UserStatus]\r\n"
				+ "      ,[CustomerName]\r\n"
				+ "      ,[CustomerMat]	\r\n"
				+ "      ,[QuantityKG]\r\n"
				+ "      ,[QuantityMR]  \r\n"
				+ "		 ,[RemarkTwo] \r\n"
				+ "      ,[OrderType] \r\n"
				+ " FROM [PPMM].[dbo].[DataFromSap] as a\r\n "
				+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.[ProductionOrder] = b.[ProductionOrder] \r\n"
				+ " INNER join [PPMM].[dbo].[PlanLotRedyeDetail] as e on\r\n"
				+ "		a.[ProductionOrder] = e.[ProductionOrder] AND \r\n"
				+ "		A.[Operation] = E.[Operation] \r\n"
				+ "	where e.[DataStatus] = 'O' and\r\n"
				+ "	      ( a.[OperationEndDate] is null or a.[OperationEndDate] > CONVERT(DATE,GETDATE()) ) \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPlanLotRedyeDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputPODetail> getPODetail()
	{
		ArrayList<InputPODetail> list = null;
		String sql = ""
				+ this.declarePOMainNPOInstead
				+ this.declareSixMonthAgo
				+ " SELECT distinct \r\n"
				+ this.select
				+ this.from
				+ " left join ( \r\n"
				+ "		select  distinct [POId] , count(ProductionOrder) as CountProd \r\n"
				+ "		from [PPMM].[dbo].[SOR_TempProd] \r\n"
				+ "		where POId IS NOT NULL  and [DataStatus] = 'O'\r\n"
				+ "		group by [POId] \r\n"
				+ " ) as b on a.Id = b.[POId]\r\n "
				+ this.leftJoinC
				+ this.leftJoinD
				+ this.leftJoinF
				+ this.leftJoinSPOPD
				+ this.leftJoinCPNG
				+ this.leftJoinCountDocDate
				+ this.leftJoinSCMR
				+ this.leftJoinCD
				+ this.leftJoinTISMA
				+ " where ( [CountProd] = 0 or [CountProd] is null ) and \r\n"
				+ "		   a.POPuangId is null and  \r\n"
				+ "        a.PO not like '%saleman%'\r\n"
				+ " order by [CountProd] ,[CustomerDue] "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputPODetail> getPODetail(String caseString)
	{
		String where = "";
		if (caseString.equals("15")) {
			where = " and a.[ChangeDate] >= DATEADD(MINUTE,-15,GETDATE()) and [isTryCreateLot] = 0\r\n ";
		} else if (caseString.equals("TRYCREATE0")) {
			where = " and [isTryCreateLot] = 0\r\n ";
		}
		ArrayList<InputPODetail> list = null;
		String sql = ""
				+ this.declarePOMainNPOInstead
				+ this.declareSixMonthAgo
				+ " SELECT distinct \r\n"
				+ this.select
				+ this.from
				+ " left join ( \r\n"
				+ "		select distinct  [POId] , count(ProductionOrder) as CountProd \r\n"
				+ "		from [PPMM].[dbo].[SOR_TempProd] \r\n"
				+ "		where POId IS NOT NULL and [DataStatus] = 'O'\r\n"
				+ "		group by [POId]\r\n"
				+ "	) as b on a.Id = b.[POId]\r\n "
				+ this.leftJoinC
				+ this.leftJoinD
				+ this.leftJoinF
				+ this.leftJoinSPOPD
				+ this.leftJoinCPNG
				+ this.leftJoinCountDocDate
				+ this.leftJoinSCMR
				+ this.leftJoinCD
				+ this.leftJoinTISMA
				+ " where ( [CountProd] = 0 or [CountProd] is null ) and \r\n"
				+ "		   a.POPuangId is null and  \r\n"
				+ "        a.PO not like '%saleman%'\r\n"
				+ where
				+ " order by [CountProd] ,[CustomerDue] ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputPODetail> getPODetailByPO(int poId)
	{
		ArrayList<InputPODetail> list = null;
//		String where = "";
//		if (tempProdDataStatus.equals(Config.C_OPEN_STATUS)) {
//			where = " where DataStatus = '" + tempProdDataStatus + "' \r\n";
//		} else if (tempProdDataStatus.equals(Config.C_CLOSE_STATUS)) {
//			where = " where DataStatus = '" + tempProdDataStatus + "' \r\n";
//		} else {
//		}
		String sql = ""
				+ this.declarePOMainNPOInstead
				+ this.declareSixMonthAgo
				+ " SELECT distinct \r\n"
				+ this.select
				+ this.from
				+ this.leftJoinCountC
				+ this.leftJoinC
				+ this.leftJoinD
				+ this.leftJoinF
				+ this.leftJoinSPOPD
				+ this.leftJoinCPNG
				+ this.leftJoinCountDocDate
				+ this.leftJoinSCMR
				+ this.leftJoinCD
				+ this.leftJoinTISMA
				+ " where a.[Id] = "
				+ poId
				+ " \r\n "
				+ " order by [CountProd] ,[CustomerDue] "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputPODetail> getPODetailByPOId(ArrayList<InputPODetail> poList)
	{
		ArrayList<InputPODetail> list = null;
		String where = " and ( ";
		for (int i = 0; i < poList.size(); i ++ ) {
			where += " a.[Id] = " + poList.get(i).getNo();
			if (i != poList.size()-1) {
				where += " or ";
			}
		}
		where += " ) ";
		String sql = ""
				+ this.declarePOMainNPOInstead
				+ this.declareSixMonthAgo
				+ " SELECT distinct \r\n"
				+ this.select
				+ this.from
				+ this.leftJoinCountC
				+ this.leftJoinC
				+ this.leftJoinD
				+ this.leftJoinF
				+ this.leftJoinSPOPD
				+ this.leftJoinCPNG
				+ this.leftJoinCountDocDate
				+ this.leftJoinSCMR
				+ this.leftJoinCD
				+ this.leftJoinTISMA
				+ " where ( [CountProd] = 0 or [CountProd] is null ) and \r\n"
				+ "		   a.POPuangId is null \r\n"
				+ where
				+ " order by [CountProd] ,[CustomerDue] ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	public ArrayList<InputPODetail> getPODetailForOP(InputPODetail beanPO, InputGroupDetail beanGroup, String colorType,
			String caseStr)
	{
		ArrayList<InputPODetail> list = null;
		String article = beanPO.getArticle();
		String matNo = beanPO.getMaterialNo();
		String docDate = beanPO.getDocDate();
		String greigePlan = beanPO.getGreigePlan();
		String groupOption = beanGroup.getGroupOptions();
		boolean bl_isSpecial = beanGroup.isSpecial();

		String whereSpe = "";
		if (bl_isSpecial) {
			String[] arrayGO = groupOption.split(Config.C_COMMA);
			if ( ! groupOption.equals("") && arrayGO.length >= 1) {
				whereSpe = " and ( \r\n";
				// GROUPNO
				for (int i = 0; i < arrayGO.length; i ++ ) {
					whereSpe += "  GroupNo = '" + arrayGO[i] + "' ";
					if (i < arrayGO.length-1) {
						whereSpe += " or ";
					}
				}
				whereSpe += " ) \r\n";
			}
		}
		String sql = ""
				+ this.declarePOMainNPOInstead
				+ this.declareSixMonthAgo
				+ " IF OBJECT_ID('tempdb..#tempPOQty') IS NOT NULL \r\n"
				+ "     DROP TABLE #tempPOQty \r\n"
				+ " IF OBJECT_ID('tempdb..#tempPOData') IS NOT NULL \r\n"
				+ "     DROP TABLE #tempPOData \r\n"
				+ " IF OBJECT_ID('tempdb..#tempPOPriority') IS NOT NULL \r\n"
				+ "     DROP TABLE #tempPOPriority  \r\n"
				+ " IF OBJECT_ID('tempdb..#tempPOSingleLower') IS NOT NULL \r\n"
				+ "     DROP TABLE #tempPOSingleLower\r\n"
				+ " IF OBJECT_ID('tempdb..#tempPOPriorityPOMax') IS NOT NULL \r\n"
				+ "     DROP TABLE #tempPOPriorityPOMax;\r\n"
				+ " IF OBJECT_ID('tempdb..#tempPOPriorityPOLower') IS NOT NULL \r\n"
				+ "     DROP TABLE #tempPOPriorityPOLower;\r\n"
				+ "  SELECT distinct \r\n"
				+ "	iasg.[Id]\r\n"
				+ ",iasg.[GroupNo]\r\n"
				+ ",iasg.[SubGroup]\r\n"
				+ ",iasg.[Article]\r\n"
				+ ",iasg.[LotMinMax] \r\n"
				+ ",iasg.[OverCapQty]   \r\n"
				+ ",imgd.[ColorType] \r\n"
				+ ",[LotQtyMin] \r\n"
				+ ",isgd.[GroupType]\r\n"
				+ ",isgd.[PriorityGroup]\r\n"
				+ ",isgd.[isMinGroup]\r\n"
				+ ",[LotQtyMax] + iasg.[OverCapQtyMax] AS LotQtyMaxWithOC\r\n"
				+ ",[GroupPriority] \r\n"
				+ " into #tempPOPriority\r\n"
				+ this.fromIASG
				+ this.innerJoinIAD
				+ this.innerJoinIMGD
				+ this.innerJoinISGD
				+ "  where iasg.DataStatus = 'O' and\r\n"
				+ "		  iad.DataStatus = 'O' and\r\n"
				+ "	      iasg.[LotMinMax] <> '' and\r\n"
				+ "	      isgd.[DataStatus] = 'O'   and\r\n"
				+ "		  iasg.Article = '"
				+ article
				+ "' and\r\n"
				+ "		  (  imgd.[ColorType] = 'Color' or imgd.[ColorType] = 'All' )  \r\n"
				+ " select top 1 * \r\n"
				+ " into #tempPOSingleLower \r\n"
				+ " from #tempPOPriority \r\n"
				+ " where isMinGroup = 0 and GroupType = 'Single' \r\n"
				+ whereSpe
				+ " order by [PriorityGroup]\r\n"
				+ "  \r\n"
				+ " select a.* \r\n"
				+ " into  #tempPOPriorityPOMax \r\n"
				+ " from (\r\n"
				+ "    	select * \r\n"
				+ "		from #tempPOPriority as a\r\n"
				+ "		where a.[isMinGroup] = 0 \r\n"
				+ whereSpe
				+ " )  as a\r\n"
				+ " left join #tempPOSingleLower as b on a.Id = b.Id\r\n"
				+ " where a.[isMinGroup] = 0 and \r\n"
				+ "       b.id is null\r\n"
				+ " SELECT * \r\n"
				+ " INTO #tempPOPriorityPOLower\r\n"
				+ " FROM (\r\n"
				+ "		select * from #tempPOPriority  where [isMinGroup] = 1 \r\n"
				+ whereSpe
				+ "		union\r\n"
				+ "		select * from #tempPOSingleLower \r\n"
				+ " ) AS a\r\n"
				+ "\r\n"
				+ "  select * \r\n"
				+ " into #tempPOQty\r\n"
				+ " from (\r\n"
				+ "	 select \r\n"
				+ "		'POMax' as POType ,\r\n"
				+ "		( select max(LotQtyMaxWithOC) from #tempPOPriorityPOMax) as POMaxQty,\r\n"
				+ "		case \r\n"
				+ "			when (select min([LotQtyMin]) from #tempPOPriorityPOMax where [LotQtyMin] > 0 ) is not null\r\n"
				+ "				then (select min([LotQtyMin]) from #tempPOPriorityPOMax where [LotQtyMin] > 0 )\r\n"
				+ "			else (select min(LotQtyMaxWithOC) from #tempPOPriorityPOMax)\r\n"
				+ "			end as POMinQty\r\n"
				+ "	union \r\n"
				+ "	 select \r\n"
				+ "		'POMin' as POType\r\n"
				+ "		,\r\n"
				+ "		( select max(LotQtyMaxWithOC)  from #tempPOPriorityPOLower as a )  as POMaxQty , \r\n"
				+ "		case \r\n"
				+ "			when (select min([LotQtyMin]) from #tempPOPriorityPOLower where [LotQtyMin] > 0) is not null\r\n"
				+ "               then (select min([LotQtyMin]) from #tempPOPriorityPOLower where [LotQtyMin] > 0)\r\n"
				+ "			else (select min(LotQtyMaxWithOC) from #tempPOPriorityPOLower )\r\n"
				+ "			end as POMinQty\r\n"
				+ " ) as a \r\n"
				+ " declare @POBaseQty int = 0;  \r\n"
				+ " SET @POBaseQty = (SELECT  POMaxQty \r\n"
				+ "                    FROM #tempPOQty  \r\n"
				+ "                    where POType = 'POMin'  );\r\n"
				+ " "
				+ " SELECT distinct \r\n"
				+ this.select
				+ "	,case \r\n"
				+ "		when a.OrderQtyCalLast < @POBaseQty then 'POLower'\r\n"
				+ "		else 'POUpper'\r\n"
				+ "	end as POType\r\n"
				+ " into #tempPOData\r\n"
				+ this.from
				+ this.leftJoinCountC
				+ this.leftJoinC
				+ this.leftJoinD
				+ this.leftJoinF
				+ this.leftJoinSPOPD
				+ this.leftJoinCPNG
				+ this.leftJoinCountDocDate
				+ this.leftJoinSCMR
				+ this.leftJoinCD
				+ this.leftJoinTISMA
				+ " where ( [CountProd] = 0 or [CountProd] is null ) and \r\n"
				+ "		   a.POPuangId is null  and\r\n"
				+ "		   a.[MaterialNo] = '" + matNo + "' and\r\n"
				+ "		   a.[DocDate] =  CONVERT(DATE,'" + docDate + "',103) and \r\n"
				+ "		   a.[GreigePlan] =  CONVERT(DATE,'" + greigePlan + "',103)  and\r\n"
				+ "		   a.[IsDCOrderPuang] = 0\r\n"
				+ " order by [OrderQtyCalLast] , [CustomerDue] \r\n";
		if (caseStr.equals("PODATA")) {
			sql += " " + " select * from #tempPOData \r\n" + " order by  [CustomerDue] ,[OrderQtyCalLast] ,[PO],[POLine]  ";

		} else if (caseStr.equals("POMM")) {
			sql += " select * from #tempPOQty  \r\n";
		} else if (caseStr.equals("POUL")) {
			sql += " "
					+ " select \r\n"
					+ "    ( select sum(OrderQtyCalLast)  \r\n"
					+ "			from #tempPOData \r\n"
					+ "			where OrderQtyCalLast < @POBaseQty) as POSumLowerQty , \r\n"
					+ "	   ( select count(No)  \r\n"
					+ "			from #tempPOData \r\n"
					+ "			where OrderQtyCalLast < @POBaseQty) as POLowerQty , \r\n"
					+ "	   ( select count(No) \r\n"
					+ "			from #tempPOData \r\n"
					+ "			where OrderQtyCalLast >= @POBaseQty) as POUpperQty  \r\n"; 
		}
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputPODetail> getPODetailWithPOId(ArrayList<InputPODetail> poList)
	{
		String where = "";
		where += " and ( \r\n";
		for (int i = 0; i < poList.size(); i ++ ) {
			where += " a.[Id] = " + poList.get(i).getId() + " ";
			if (i < poList.size()-1) {
				where += " or \r\n";
			}
		}
		where += " ) \r\n";
		ArrayList<InputPODetail> list = null;
		String sql = ""
				+ this.declarePOMainNPOInstead
				+ this.declareSixMonthAgo
				+ " SELECT distinct \r\n"
				+ this.select
				+ this.from
				+ this.leftJoinCountC
				+ this.leftJoinC
				+ this.leftJoinD
				+ this.leftJoinF
				+ this.leftJoinSPOPD
				+ this.leftJoinCPNG
				+ this.leftJoinCountDocDate
				+ this.leftJoinSCMR
				+ this.leftJoinCD
				+ this.leftJoinTISMA
				+ " where ( [CountProd] = 0 or [CountProd] is null ) and \r\n"
				+ "		   a.POPuangId is null and  \r\n"
				+ "        a.PO not like '%saleman%'\r\n"
				+ where
				+ " order by [CountProd] ,[CustomerDue] "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<SorPODetailList> getPOListDetailForOP(ArrayList<InputPODetail> poList)
	{
		ArrayList<SorPODetailList> list = this.getPOPuangSumLot(poList);
		return list;
	}

	@Override
	public ArrayList<SorPODetailList> getPOPuangSumLot(ArrayList<InputPODetail> poList)
	{
		String colorType;
		String color; // orderQtyCalLast,
		String specialType;
		int db_poLowerQty,db_poUpperQty,db_poAllQty;
		double db_poSumLowerQty,db_poSumLowerQtyAfterMod = 0,db_upperPOMinQty,db_upperPOMaxQty,db_lowerPOMinQty,db_lowerPOMaxQty;
		boolean bl_isSpecial = false;
		ArrayList<InputPODetail> listPOData,listPODataSortQty,listPODataLower,listPODataUpper,listPOMinMax,listPOSumQty,
				listPODataShow,listPODataResult;

		ArticleDetailModel adModel = new ArticleDetailModel(this.conType);
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		bgjModel.execUpdateOrderQtyCal();

		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "";
		ArrayList<SorPODetailList> listSorPOResult = new ArrayList<>();
		SorPODetailList beanRes = new SorPODetailList();
		listPODataResult = new ArrayList<>();
		listPODataShow = new ArrayList<>();
		listPODataSortQty = new ArrayList<>();
		listPODataLower = new ArrayList<>();
		listPODataUpper = new ArrayList<>();
		db_upperPOMinQty = db_lowerPOMinQty = db_lowerPOMaxQty = db_upperPOMaxQty = 0;

		InputPODetail bean = poList.get(0);
		bean.getNo();
		String article = bean.getArticle();
		color = bean.getColor();
		colorType = this.checkColorType(color);
		specialType = bean.getSpecialType();
		InputGroupDetail beanGroup = this.checkIsSpecialCase(bean);
		bl_isSpecial = beanGroup.isSpecial();
		// SET DETAIL FOR BEGIN
		listPOData = this.getPODetailForOP(bean, beanGroup, colorType, "PODATA");
		for (InputPODetail beanTmp : listPOData) {
			listPODataSortQty = this.addBeanPOToList(listPODataSortQty, beanTmp);
		}
		Collections.sort(listPODataSortQty);
		for (int i1 = 0; i1 < listPODataSortQty.size(); i1 ++ ) {
			InputPODetail beanTmp = listPODataSortQty.get(i1);
			if (beanTmp.getPoType().equals("POUpper")) {
				listPODataUpper.add(beanTmp);
			} else {
				listPODataLower.add(beanTmp);
			}
		}
		listPOMinMax = this.getPODetailForOP(bean, beanGroup, colorType, "POMM");
		for (InputPODetail beanTmp : listPOMinMax) {
			if (beanTmp.getPoType().equals("POMax")) {
				db_upperPOMinQty = beanTmp.getPoMinQty();
				db_upperPOMaxQty = beanTmp.getPoMaxQty();
			} else {
				db_lowerPOMinQty = beanTmp.getPoMinQty();
				db_lowerPOMaxQty = beanTmp.getPoMaxQty();
			}
		}
		listPOSumQty = this.getPODetailForOP(bean, beanGroup, colorType, "POUL");
		if ( ! listPOSumQty.isEmpty()) {
			InputPODetail beanTmp = listPOSumQty.get(0);
			db_poSumLowerQty = beanTmp.getPoSumLowerQty();
			db_poLowerQty = beanTmp.getPoLowerQty();
			db_poUpperQty = beanTmp.getPoUpperQty();
		} else {
			db_poSumLowerQty = 0;
			db_poLowerQty = 0;
			db_poUpperQty = 0;
		}
		ArrayList<InputArticleDetail> listCA = adModel.getArticleDetailByArticle(article);
  		// ปัดให้เป็นจำนวนเต็มหาร 50 ลงตัว
		// ไม่มีทางเข้าเคสนี้เพราะดักตั้งแต่ด้านนอก
		boolean isGreigeQtyWork = true;
		if (listCA.size() == 0) {
			db_poSumLowerQtyAfterMod = this.roundUpDoubleByMod(db_poSumLowerQty, this.INT_POQTYMOD);
		} else {
			boolean isQtyGreigeForPO = listCA.get(0).isQtyGreigeForPO();
			String qtyGreigeMR = listCA.get(0).getQtyGreigeMR();
			if (isQtyGreigeForPO) {
				int intQtyGreigeMR = 0;
				try {
					intQtyGreigeMR = Integer.parseInt(qtyGreigeMR);
				} catch (Exception e) {
					intQtyGreigeMR = 0;
				}
				if (qtyGreigeMR.equals("") || qtyGreigeMR.equals("0") || intQtyGreigeMR == 0) {
					isGreigeQtyWork = false;
				} else {
					double checkZero = db_poSumLowerQty % intQtyGreigeMR;
					if (checkZero == 0) {
						// do notthing best value
						isGreigeQtyWork = true;
						db_poSumLowerQtyAfterMod = db_poSumLowerQty;
					} else {
						isGreigeQtyWork = false;
					}
				}
			} else {
				db_poSumLowerQtyAfterMod = this.roundUpDoubleByMod(db_poSumLowerQty, this.INT_POQTYMOD);
			}
		} 
		db_poAllQty = db_poLowerQty+db_poUpperQty; 
		// ไม่ใช่ Orderpuang 
		if ( ! isGreigeQtyWork) {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus =
					"Article นี้จำเป็นต้องให้ POQty หาร QtyGreige ลงตัวเท่านั้น โปรดตรวจสอบในหน้า Setting - ArticleManagement.";

		} else if (db_poLowerQty == 0 || db_poAllQty == 1) {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = "This PO is not OrderPuang."; 
		} else {
			int casePOData = 0;
			if ((db_poLowerQty > 1) || (db_poLowerQty == 1 || db_poUpperQty >= 1)) {
				casePOData = this.findOrderPuangCase(db_poSumLowerQtyAfterMod, db_lowerPOMinQty, db_lowerPOMaxQty,
						db_upperPOMinQty, db_upperPOMaxQty, db_poUpperQty, bl_isSpecial); 
				if (casePOData < 40) {
					// รวมกับตัวในกลุ่ม Group UPPER ที่ค่าน้อยสุด
					listPODataShow = listPODataLower;
					if (casePOData == 1) {
						InputPODetail beanTmp = listPODataUpper.get(0);
						listPODataShow.add(beanTmp);
						db_poSumLowerQty = db_poSumLowerQty+listPODataUpper.get(0).getDb_orderQtyCalLast();
					}
					// WORK TO LOWER GROUP
					else if (casePOData == 4) {
					}
					// WORK TO UPPER GROUP
					else if (casePOData == 5) {
					}
					// PUSH TO MIN LOWER GROUP
					else if (casePOData == 10) {
						db_poSumLowerQty = db_lowerPOMinQty;
					}
					// PUSH TO MIN UPPER GROUP
					else if (casePOData == 20) {
						db_poSumLowerQty = db_upperPOMinQty;
					}
					listPODataResult = this.addPOToListPOResult(listPODataResult, listPOData, 1, db_poSumLowerQty);

				}
				// SUM LOWER PO MORE THAN MAX GROUP
				else if (casePOData == 40) {
					listPODataShow = listPODataLower;
					listPODataResult = this.addPOToListPOResult(listPODataResult, listPODataLower, 1, db_poSumLowerQty);
				}
				// CHANGE VALUE
				else if (casePOData == 99) {
					listPODataShow = listPODataLower;
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = " Case '" + specialType + "' | Can't create anygroup. Need PC adjust PO Qty.";
				}
				beanRes.setListPOShow(listPODataShow);
				beanRes.setListPOResult(listPODataResult);
			} else {
				iconStatus = Config.C_ERR_ICON_STATUS;
				systemStatus = "This PO is not OrderPuang."; 
			}
		}
		beanRes.setIconStatus(iconStatus);
		beanRes.setSystemStatus(systemStatus);
		listSorPOResult.add(beanRes);
		return listSorPOResult;
	}

	@Override
	public ArrayList<InputPODetail> getPOSumDetailForOP(ArrayList<InputPODetail> poList)
	{
		ArrayList<InputPODetail> list = null;
		InputPODetail bean = poList.get(0);
		String no = bean.getNo();
		String sql = ""
				+ this.declarePOMainNPOInstead
				+ this.declareSixMonthAgo
				+ " IF OBJECT_ID('tempdb..#tempPOData') IS NOT NULL \r\n"
				+ "     DROP TABLE #tempPOData \r\n"
				+ " SELECT distinct \r\n"
				+ this.select
				+ this.from
				+ this.leftJoinCountC
				+ this.leftJoinC
				+ this.leftJoinD
				+ this.leftJoinF
				+ this.leftJoinSPOPD
				+ this.leftJoinCPNG
				+ this.leftJoinCountDocDate
				+ this.leftJoinSCMR
				+ this.leftJoinCD
				+ this.leftJoinTISMA
				+ " where ( [CountProd] = 0 or [CountProd] is null ) and \r\n"
				+ "		   a.POPuangId is null  and\r\n"
				+ "		   a.[Id] = " + no + "  \r\n"
				+ " order by [OrderQtyCalLast] , [CustomerDue] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputForecastDetail> getTotalFCDetail()
	{
		ArrayList<InputForecastDetail> list = null;
		String sql = ""
				+ " IF OBJECT_ID('tempdb..#tempTotal') IS NOT NULL   \r\n"
				+ "	DROP TABLE #tempTotal \r\n "
				+ " SELECT distinct \r\n"
				+ this.selectFC
				+ "into #tempTotal \r\n"
				+ this.fromFC
				+ this.leftJoinFCB
				+ " where [CountProd] = 0 or [CountProd] is null \r\n "
				+ " order by [CountProd] ,[DocDate] desc\r\n"
				+ " select ( select count(a.[No]) \r\n"
				+ "		from #tempTotal as a ) as totalAll\r\n"
				+ "		, \r\n"
				+ "		 (  select count(a.[No]) \r\n"
				+ "		 	from ( \r\n"
				+ "				select No,[CheckDivision]+[CheckArticle] +[CheckArticleWeight]+[CheckGreigePlan] as errorCounter\r\n"
				+ "				from #tempTotal as a\r\n"
				+ "				) as a\r\n"
				+ "		 where errorCounter > 0 ) as totalError \r\n ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genForecastDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputPODetail> getTotalPODetail()
	{
		ArrayList<InputPODetail> list = null;
		String sql = ""
				+ this.declarePOMainNPOInstead
				+ this.declareSixMonthAgo
				+ " IF OBJECT_ID('tempdb..#tempTotal') IS NOT NULL   \r\n"
				+ "		DROP TABLE #tempTotal; \r\n "
				+ " select *  \r\n"
				+ "  into #tempTotal \r\n"
				+ "  from (\r\n"
				+ " 	SELECT distinct \r\n"
				+ this.select
				+ this.from
				+ this.leftJoinCountC
				+ "     " + this.leftJoinC
				+ "     " + this.leftJoinD
				+ "     " + this.leftJoinF
				+ "     " + this.leftJoinSPOPD
				+ "     "
				+ this.leftJoinCPNG
				+ "     "
				+ this.leftJoinCountDocDate
				+ "     "
				+ this.leftJoinSCMR
				+ this.leftJoinCD
				+ this.leftJoinTISMA
				+ " where ( [CountProd] = 0 or [CountProd] is null ) and \r\n"
				+ "		   a.POPuangId is null \r\n"
				+ " 	) as a \r\n"
				+ " 	order by [CountProd] ,[CustomerDue];\r\n"
				+ " select \r\n"
				+ "  ( select count(a.[No]) \r\n"
				+ "	   from #tempTotal as a ) as totalAll\r\n"
				+ ", ( select count(a.[No]) \r\n"
				+ "	   from ( select No,[CheckDivision]+[CheckArticle] +[CheckArticleWeight]+[CheckGreigePlan]+[CheckFormula]\r\n"
				+ "	               +CheckColor+CheckCustomerNo+CheckCustomerDue+CheckOrderQty+CheckQtyGreigeMR+CheckQtyGreigeForPO as errorCounter\r\n"
				+ "			  from #tempTotal as a\r\n"
				+ "		    ) as a\r\n"
				+ "	   where errorCounter > 0 ) as totalError \r\n "
				+ ", ( select count(a.[No]) \r\n"
				+ "	   from ( select No,isRecheck,[CheckDivision]+[CheckArticle] +[CheckArticleWeight]+[CheckGreigePlan]+[CheckFormula]+CheckColor+CheckCustomerNo+CheckCustomerDue+CheckOrderQty+CheckQtyGreigeMR+CheckQtyGreigeForPO as errorCounter\r\n"
				+ "			  from #tempTotal as a\r\n"
				+ "		    ) as a\r\n"
				+ "	    where errorCounter = 0 and isRecheck = 1 ) as totalOrderQtyMod\r\n  "
				+ ", ( select count(a.[No]) \r\n"
				+ "	   from #tempTotal as a\r\n"
				+ "	   where a.isGroupRecheck = 1 ) as totalGroupRecheck \r\n "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	private ArrayList<InputTempProdDetail> handlerDataForCreateTempLot(ArrayList<InputTempProdDetail> listTemp,
			InputPODetail bean, boolean isUpdate)
	{
		ArrayList<InputTempProdDetail> listTempLast = new ArrayList<>();
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		RelationPOAndProdOrderModel rpapModel = new RelationPOAndProdOrderModel(this.conType);
		TempPORunningModel tprModel = new TempPORunningModel(this.conType);
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		String runningStr = "";
		String lastProdOrder = "";
		String sevenToEightPos = "";
		String iconStatus = "";
		String division = bean.getDivision();
		String isCotton = bean.getIsCotton();
		String lv_yyMM = this.C_yyMM.format(Calendar.getInstance().getTime());
		String firstPos = lv_yyMM.substring(1, 2); // yy 'MM'
		String secondPos = this.getFirstPosLot(division, isCotton); // y 'y' MM
		String lotBegin = firstPos+secondPos;
		String matNo = bean.getMaterialNo();
		String custNo = bean.getCustomerNo();
		boolean bl_checkMat = matNo.contains("PR0000");
		if (matNo.length() > 0) {
			if (matNo.charAt(0) == 'H') {
				sevenToEightPos = "H";
			} else if (matNo.equals("KPR500540PR0000") && custNo.equals("61100007")) {
				sevenToEightPos = "/P";
			} else if (bl_checkMat) {
				sevenToEightPos = "/S";
			}
		}
		ArrayList<InputTempProdDetail> listLastRunning = tprModel.getLastRunningProdFromTEMPPORunning(lotBegin);
		String lastProd = "";
		int lastRunningNo = 1;
		if (listLastRunning.isEmpty()) {
			lastRunningNo = 1;
		} else {
			lastProd = listLastRunning.get(0).getProductionOrder();
			String lastRunning = lastProd.substring(2, 6); // XYZ001
			lastRunningNo = Integer.parseInt(lastRunning)+1;
		}
		double db_tmpMaxQty = 0;
		int int_indexMax = 0;
		Collections.sort(listTemp);
		// FIX HERE
		boolean isLastWorkNotInSixMonth = bean.isLastWorkNotInSixMonth();
		for (int i = 0; i < listTemp.size(); i ++ ) {
			InputTempProdDetail beanTemp = listTemp.get(i);
			runningStr = String.format("%04d", lastRunningNo);
			lastProdOrder = lotBegin+runningStr+sevenToEightPos;
			beanTemp.setFirstLot("N");
			if (isLastWorkNotInSixMonth) {
				beanTemp.setProductionOrderType(">6M");
			} else {
				beanTemp.setProductionOrderType("Normal");
			}
			beanTemp.setProductionOrder(lastProdOrder);
			if (db_tmpMaxQty < listTemp.get(i).getDbProdQty()) {
				db_tmpMaxQty = listTemp.get(i).getDbProdQty();
				int_indexMax = i;
			}
			lastRunningNo += 1;
		}
		if (isLastWorkNotInSixMonth) {
			listTemp.get(int_indexMax).setFirstLot("Y");
		}
		for (int i = 0; i < listTemp.size(); i ++ ) {
			InputTempProdDetail beanTemp = listTemp.get(i);
			double dbProdQty = listTemp.get(i).getDbProdQty();
			if (dbProdQty > 0) {
				listTempLast.add(beanTemp);
			}
 		} // FOR TEST
		if (isUpdate) {
			int[] countExe = stpModel.insertSORTempProd(listTempLast, this.C_PENDING); // boathere
			if (countExe != null) {
				lastRunningNo = lastRunningNo-1;
				runningStr = String.format("%04d", lastRunningNo);
				lastProdOrder = lotBegin+runningStr;
				iconStatus = tprModel.upsertTEMPPORunningDetail(lotBegin, lastProdOrder, iconStatus, bean.getChangeBy()); // boathere
				
				InputTempProdDetail beanTempLast = listTempLast.get(0);
				String poId = beanTempLast.getNo();
				String changeBy = beanTempLast.getChangeBy();
				ArrayList<InputTempProdDetail> listTempProd = spdModel.getSorPODetailWithSORTempProdByPOId(poId);
				for (InputTempProdDetail element : listTempProd) {
					element.setChangeBy(changeBy);
				}
				rpapModel.upsertRelationPOAndProdOrder(listTempProd);
			}
		}
		return listTemp;
	}

	@Override
	public ArrayList<InputTempProdDetail> recreateTMPProdOrder(InputPODetail bean, double db_baseOrderQty, String iconStatus,
			ArrayList<InputGroupDetail> listAG, String colorType, ArrayList<InputGroupDetail> listSingleAG, boolean isUpdate)
	{
		ArrayList<Double> listLotMax = new ArrayList<>();
		HashMap<Double, Integer> hm_countLotDup = new HashMap<>();
		ArrayList<InputTempProdDetail> listTemp = new ArrayList<>();
		String groupNo = "",subGroup = "",groupType = "";
		String no = bean.getNo();
		String po = bean.getPo();
		String poLine = bean.getPoLine();
		String changeBy = bean.getChangeBy();
		String division = bean.getDivision();
		String matNo = bean.getMaterialNo();
		int int_qtyGreigeMR = bean.getIntQtyGreigeMR();
		boolean bl_checkLot = false;
		double db_lotMax,db_lotMin = 0,db_difMax,db_difMin,db_OCMax,db_OCMin;
		double db_lotMaxRound,db_lotMinRound = 0;
		int int_countLotQty = 0;
		double db_poQtyScrap = 0;
		double db_curLotMax = 0;
		double db_curLotPos = 0;
		double db_curLotDif = 0;
		double db_minLotDif = 0;
		String groupBegin = "";
		double db_orderQty = 0;
		double db_curOrderQty = 0;
		boolean bl_isHireWork = false;
		boolean bl_checkSplitLot = false;
		int int_counterLotPos = 0;
		int int_counterPossible = 0;
		String custNo = bean.getCustomerNo();
		String strPattern = "^0+(?!$)";
		custNo = custNo.replaceAll(strPattern, "");
		if (matNo.length() > 0) {
			if (matNo.charAt(0) == 'H') {
				bl_isHireWork = true;
			}
		}

		// CK
		if (db_baseOrderQty % int_qtyGreigeMR != 0 && division.equals(this.C_CK) && ! bl_isHireWork) {
			db_baseOrderQty = db_baseOrderQty+(int_qtyGreigeMR-(db_baseOrderQty % int_qtyGreigeMR));
		}
		if ( ! listSingleAG.isEmpty()) { 
			InputGroupDetail beanAG = listSingleAG.get(0);
			groupNo = beanAG.getGroupNo();
			subGroup = beanAG.getSubGroup();
			db_lotMax = beanAG.getDbLotQtyMax();
			db_lotMin = beanAG.getDbLotQtyMin();
			db_difMax = beanAG.getDbLotDifMax();
			db_difMin = beanAG.getDbLotDifMin();
			db_OCMax = beanAG.getDbOverCapQtyMax();
			db_OCMin = beanAG.getDbOverCapQtyMin();

			if (division.equals(this.C_CK) && ! bl_isHireWork) { db_minLotDif = int_qtyGreigeMR; } 
			else if (db_difMax % 50 == 0) { db_minLotDif = 50; } 
			else if (db_difMax < 50) {      db_minLotDif = db_difMax; } 
			else if (db_difMax % 30 == 0) { db_minLotDif = 30; } 
			else if (db_difMax % 20 == 0) { db_minLotDif = 20; } 
			else { db_minLotDif = db_difMax; }

			db_lotMaxRound = this.roundUpDoubleByMod(this.C_MAX, db_lotMax, (int) (db_minLotDif));
			db_orderQty = db_baseOrderQty;
			db_curLotMax = db_lotMaxRound;

			db_poQtyScrap = db_orderQty / db_curLotMax;
			int_countLotQty = (int) (db_orderQty / db_curLotMax);
			if (db_poQtyScrap % 1 != 0) {
				int_countLotQty = int_countLotQty+1;
			}
			for (int i = 0; i < int_countLotQty; i ++ ) {
				listLotMax.add(db_curLotMax);
			}
			db_curOrderQty = db_curLotMax * int_countLotQty;
			int_counterLotPos = 0;
			// 50 , 100 ( 100 แบ่งได้ 50 OK )
			if (db_orderQty % db_lotMaxRound <= db_OCMax && db_OCMax != 0 && db_difMax != 0
					&& db_orderQty % db_lotMaxRound <= db_difMax) {
				Double db = listLotMax.get(listLotMax.size()-1)+(db_orderQty % db_lotMaxRound);
				listLotMax.set(listLotMax.size()-1, db);
				double sum = 0;
				for (Double element : listLotMax) { sum = sum+element; }
				if (sum > db_baseOrderQty && (listLotMax.size()-1 > 0)) {
					listLotMax.remove(0);
				} 
			} else {
				while ( ! bl_checkSplitLot && int_counterPossible != 500) {
					db_curLotDif = db_minLotDif;
					// BASE CK 50 ,100
					db_curLotPos = listLotMax.get(int_counterLotPos);
					// 350
					if (db_curOrderQty-db_curLotDif < db_orderQty) {
						db_curLotDif = db_curOrderQty-db_orderQty;
						bl_checkSplitLot = true;
					}
					// PERFECT CASE
					else if (db_curOrderQty-db_curLotDif == db_orderQty) {
						bl_checkSplitLot = true;
					}
					db_curOrderQty = db_curOrderQty-db_curLotDif;
					db_curLotPos = db_curLotPos-db_curLotDif;
					listLotMax.set(int_counterLotPos, db_curLotPos);
					int_counterLotPos = int_counterLotPos+1;
					int_counterPossible = int_counterPossible+1;
					if (int_counterLotPos >= int_countLotQty) {
						int_counterLotPos = 0;
					}
				}
			}
			Collections.sort(listLotMax);
			for (Double element : listLotMax) {
				if (hm_countLotDup.get(element) == null) {
					hm_countLotDup.put(element, 1);
				} else {
					int count = hm_countLotDup.get(element);
					hm_countLotDup.put(element, count+1);
				}
			}
			listLotMax = this.sortHashmapByValueToList(hm_countLotDup);
			boolean bl_listSize = true;
			boolean bl_break = false;
			int_counterPossible = 0;
			double db_lotQtyTemp = 0;
			while (bl_listSize) {
				hm_countLotDup.clear();
				for (Double element : listLotMax) {
					if (hm_countLotDup.get(element) == null) {
						hm_countLotDup.put(element, 1);
					} else {
						int count = hm_countLotDup.get(element);
						hm_countLotDup.put(element, count+1);
					}
				}
				listLotMax = this.sortHashmapByValueToList(hm_countLotDup);
				double db_lotQty = listLotMax.get(0);
				int int_counterTemp = 0;
				bl_break = false;
				for (int i = 0; i < listAG.size(); i ++ ) {
					InputGroupDetail beanFG = listAG.get(i);
					db_lotMax = beanFG.getDbLotQtyMax();
					db_lotMin = beanFG.getDbLotQtyMin();
					db_OCMax = beanFG.getDbOverCapQtyMax();
					db_difMax = beanAG.getDbLotDifMax();
					if (division.equals(this.C_CK)) { db_minLotDif = int_qtyGreigeMR; } 
					else if (db_difMax % 50 == 0) {   db_minLotDif = 50; } 
					else if (db_difMax < 50) {        db_minLotDif = db_difMax; } 
					else {                            db_minLotDif = db_difMax; }
					db_lotMaxRound = this.roundUpDoubleByMod(this.C_MAX, db_lotMax, (int) (db_minLotDif));
					db_curLotMax = db_lotMaxRound;
					// // try plus oc max
					if (listLotMax.size() == 1 && db_OCMax >= 0) {
						db_curLotMax = db_curLotMax+db_OCMax;
					}
					groupType = beanFG.getGroupType();
					groupNo = beanFG.getGroupNo();
					subGroup = beanFG.getSubGroup();
					groupBegin = groupNo + ":" + subGroup; 
					if (bl_break) { }
					// last lot need go to single bcoz it born from single machine
					else if (listLotMax.size() == 1 && groupType.equals("Double")) { } 
					else if (db_lotQty > db_curLotMax) { }
					// GOT MAX ONLY
					else if (db_lotMin == 0) {
						// PERFECT
						if (db_lotQty == db_curLotMax) {
							InputTempProdDetail beanTempLot = new InputTempProdDetail();
							beanTempLot.setNo(no);
							beanTempLot.setPo(po);
							beanTempLot.setPoLine(poLine);
							beanTempLot.setPpmmStatus(this.C_PENDING);
							beanTempLot.setChangeBy(changeBy);
							beanTempLot.setColorType(colorType);
							beanTempLot.setGroupBegin(groupBegin);
							beanTempLot.setDbProdQty(db_lotQty);
							listTemp.add(beanTempLot);
							bl_break = true;
							listLotMax.remove(0);
						} 
						else if (db_lotQty > db_curLotMax) {
						} else { /// (db_lotQty < db_lotMin )

							db_lotQtyTemp = db_lotQty;
							int_counterTemp = 0;
							while (db_lotQtyTemp < db_curLotMax) {
								// 450 450 450
								// 1. 450
								// 2. 450 + 450 => 900
								// 3. 450 => ELSE DONE
								if (int_counterTemp+1 < listLotMax.size()) {
									if (db_lotQtyTemp+listLotMax.get(int_counterTemp+1) <= db_curLotMax) {
										int_counterTemp = int_counterTemp+1;
										db_lotQtyTemp = db_lotQtyTemp+listLotMax.get(int_counterTemp);
									}
									// DONE
									else {
										break;
									}
								} else {
									break;
								}
							}
							db_lotQty = db_lotQtyTemp;
							// AFTER PLUS USE THIS
							if (db_lotMin <= db_lotQty && db_lotQty <= db_curLotMax) {
								InputTempProdDetail beanTempLot = new InputTempProdDetail();
								beanTempLot.setNo(no);
								beanTempLot.setPo(po);
								beanTempLot.setPoLine(poLine);
								beanTempLot.setPpmmStatus(this.C_PENDING);
								beanTempLot.setChangeBy(changeBy);
								beanTempLot.setColorType(colorType);
								beanTempLot.setGroupBegin(groupBegin);
								beanTempLot.setDbProdQty(db_lotQty);
								listTemp.add(beanTempLot);
								bl_break = true;
								for (int j = 0; j < int_counterTemp+1; j ++ ) {
									if ( ! listLotMax.isEmpty()) {
										listLotMax.remove(0);
									}
								}
							}
							// CAN'T USE GO NEXT GROUP
							else {
							}
						}
					}
					// MIN AND MAX
					else { 
						if (db_lotQty < db_curLotMax) {
							db_lotQtyTemp = db_lotQty;
							int_counterTemp = 0;
							while (db_lotQtyTemp < db_curLotMax) {
								if (int_counterTemp+1 < listLotMax.size()) { 
									if (db_lotQtyTemp+listLotMax.get(int_counterTemp+1) <= db_curLotMax) {
										int_counterTemp = int_counterTemp+1;
										db_lotQtyTemp = db_lotQtyTemp+listLotMax.get(int_counterTemp);
									} // DONE
									else {
										break;
									}
								} else {
									break;
								}
							} 
							db_lotQty = db_lotQtyTemp;
							// AFTER PLUS USE THIS
							if (db_lotMin <= db_lotQty && db_lotQty <= db_curLotMax) {
								InputTempProdDetail beanTempLot = new InputTempProdDetail();
								beanTempLot.setNo(no);
								beanTempLot.setPo(po);
								beanTempLot.setPoLine(poLine);
								beanTempLot.setPpmmStatus(this.C_PENDING);
								beanTempLot.setChangeBy(changeBy);
								beanTempLot.setColorType(colorType);
								beanTempLot.setGroupBegin(groupBegin);
								beanTempLot.setDbProdQty(db_lotQty);
								listTemp.add(beanTempLot);
								bl_break = true;
								for (int j = 0; j < int_counterTemp+1; j ++ ) {
									if ( ! listLotMax.isEmpty()) {
										listLotMax.remove(0);
									}
								}
							}
							// CAN'T USE GO NEXT GROUP
							else {
							}
						}
						// SMALL CAN'T USE
						else if (db_lotQty > db_curLotMax) {
						}
						// BEST
						else {
							InputTempProdDetail beanTempLot = new InputTempProdDetail();
							beanTempLot.setNo(no);
							beanTempLot.setPo(po);
							beanTempLot.setPoLine(poLine);
							beanTempLot.setPpmmStatus(this.C_PENDING);
							beanTempLot.setChangeBy(changeBy);
							beanTempLot.setColorType(colorType);
							beanTempLot.setGroupBegin(groupBegin);
							beanTempLot.setDbProdQty(db_lotQty);
							listTemp.add(beanTempLot);
							bl_break = true;
							listLotMax.remove(0);
						}
					}
				}
				if ( ! bl_break || listLotMax.isEmpty()) {
					bl_listSize = false;
					break;
				}
				int_counterPossible = int_counterPossible+1;
			}
//			groupBegin = groupNo+Config.C_COMMA+subGroup;
		} else {
			for (int i = 0; i < listAG.size(); i ++ ) {
				InputGroupDetail beanAG = listAG.get(i);
				groupNo = beanAG.getGroupNo();
				subGroup = beanAG.getSubGroup();
				db_lotMax = beanAG.getDbLotQtyMax();
				db_lotMin = beanAG.getDbLotQtyMin();
				db_difMax = beanAG.getDbLotDifMax();
				db_difMin = beanAG.getDbLotDifMin();
				db_OCMax = beanAG.getDbOverCapQtyMax();
				db_OCMin = beanAG.getDbOverCapQtyMin();

				if (division.equals(this.C_CK)) {
					db_minLotDif = int_qtyGreigeMR;
				} 
				else if (int_qtyGreigeMR / 2 == db_difMax) { db_minLotDif = db_difMax; } 
				else if (db_difMax % 50 == 0) {              db_minLotDif = 50; } 
				else if (db_difMax < 50) {                   db_minLotDif = db_difMax; } 
				else {                                       db_minLotDif = db_difMax; } 
				db_lotMaxRound = this.roundUpDoubleByMod(this.C_MAX, db_lotMax, (int) (db_minLotDif));
				db_lotMinRound = this.roundUpDoubleByMod(this.C_MIN, db_lotMin, (int) (db_minLotDif));
				this.roundUpDoubleByMod(this.C_MAX, db_difMax, (int) (db_minLotDif));
				this.roundUpDoubleByMod(this.C_MIN, db_difMin, (int) (db_minLotDif));
				this.roundUpDoubleByMod(this.C_MAX, db_OCMax, (int) (db_minLotDif));
				this.roundUpDoubleByMod(this.C_MIN, db_OCMin, (int) (db_minLotDif));
				groupType = beanAG.getGroupType();
				db_orderQty = db_baseOrderQty;
				groupBegin = groupNo+Config.C_COMMA+subGroup;
				if (bl_checkLot) {
				} else if (db_orderQty < db_lotMinRound) {
				} else if (bl_isHireWork) {
					if (db_lotMin <= db_orderQty && db_orderQty <= db_lotMax) {
						InputTempProdDetail beanTempLot = new InputTempProdDetail();
						beanTempLot.setNo(no);
						beanTempLot.setPo(po);
						beanTempLot.setPoLine(poLine);
						beanTempLot.setPpmmStatus(this.C_PENDING);
						beanTempLot.setChangeBy(changeBy);
						beanTempLot.setColorType(colorType);
						beanTempLot.setGroupBegin(groupBegin);
						beanTempLot.setDbProdQty(db_orderQty);
						listTemp.add(beanTempLot);
						bl_checkLot = true;
						break;
					}
				} else {
				}
			}
		}
		if ( ! listTemp.isEmpty()) {
			bl_checkLot = true;
		} else {
			listTemp.clear();
		}
		if (bl_checkLot) {
			listTemp = this.handlerDataForCreateTempLot(listTemp, bean, isUpdate);
		}
		return listTemp;
	}

	public double roundUpDouble(double x)
	{
		if (x % 100 == 50 || x % 100 == 0) {
			return x; // when it is halfawy between the nearest 50 it will automatically round up,
						// change this line to 'return x - 25' if you want it to automatically round
						// down
		} else if (x % 100 > 50) {
			return x+(100-(x % 100));
		} else if (x % 100 < 50) {
			return x+(50-(x % 100));
		}
		return x;
	}

	@Override
	public double roundUpDouble(String caseVal, double x)
	{
		if (x % 100 == 50 || x % 100 == 0) {
			return x; // when it is halfawy between the nearest 50 it will automatically round up,
						// change this line to 'return x - 25' if you want it to automatically round
						// down
		} else if (caseVal.equals(this.C_MAX)) { // 199 -> 150 , 149 -> 100
			if (x % 100 > 50) { // 190 -> 90 - ( 90 - 50) - > 150
				return x-((x % 100)-50);
			} else if (x % 100 < 50) { // 190 -> 40 - ( 90 - 50) - > 100
				return x-(x % 100);
			}
		} else { // 199 -> 200 , 149 -> 150
			if (x % 100 > 50) { // 190 -> 90 + (100 - 90) - > 200
				return x+(100-(x % 100));
			} else if (x % 100 < 50) { // 140 -> 40 + (50 - 40) - > 150
				return x+(50-(x % 100));
			}
		}
		return x;
	}

	private double roundUpDoubleByMod(double x, int mod)
	{
		double xPercentMod = x % mod;
		double halveMod = mod / 2;
		// 0 < x < 1
		double checkXLess = x % 50; 
		// 0 < 0.7 < 1 | 100.7 -0.7 = 100
		if (0 < checkXLess && checkXLess < 1) {
			return x-checkXLess;
		} else if (xPercentMod == halveMod || xPercentMod == 0) {
			return x;
		}
		// 149 | 49>25 | 149 + ( 50 -49 ) | 150
		else if (xPercentMod > halveMod) {
			return x+(mod-(xPercentMod));
		}
		// 123 | 23<25 | 123 + ( 25 - 23 ) | 150
		else if (xPercentMod < halveMod) {
			return x+(halveMod-(xPercentMod));
		}
		return x;
	}

	private double roundUpDoubleByMod(String caseVal, double x, int mod)
	{
		double xPercentMod = x % mod;
//		int xPercentModTwoTime = (int) (x%(mod*2));
		double halveX = mod / 2;
		if (caseVal.equals("PUSHMAX")) { // 199 -> 150 , 149 -> 100
			return x-xPercentMod+mod;
		} else if (caseVal.equals("PUSHMIN")) {
			return x-xPercentMod;
		} else {
			if (xPercentMod == halveX || xPercentMod == 0) {
				return x; // when it is halfawy between the nearest 50 it will automatically round up,
							// change this line to 'return x - 25' if you want it to automatically round
							// down
			} else if (caseVal.equals(this.C_MAX)) { // 199 -> 150 , 149 -> 100
				if (xPercentMod >= halveX) { // 190 -> 190 - ( 40 - 25) - > 40>25 - > 190-40 = 150
					// return x - xPercentMod; //175 -> 175 - ( 40 - 25) - > 40>25 - > 190-40 = 150
					return x-xPercentMod+halveX;
				} else if (xPercentMod < halveX) { // 160 -> 160 - ( 50 - 10) - > 100
					return x-xPercentMod;
				}
			} else if (caseVal.equals(this.C_MIN)) { // 199 -> 200 , 149 -> 150
				if (xPercentMod >= halveX) { // 426 -> 426 + (50 - 26) - > 200
					// return x + ( mod - xPercentMod );
					return x+(mod-xPercentMod);
				} else if (xPercentMod < halveX) { // 401 -> 401 + (25 - 1) - > 150
					return x+(halveX-xPercentMod);
				}
			}
		}
		return x;
	}

	@Override
	public ArrayList<InputPODetail> savePOQtyCal(ArrayList<InputPODetail> poList)
	{
		ArrayList<InputPODetail> listCheck;
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		SORPODetailChangeModel spdcModel = new SORPODetailChangeModel(this.conType);
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		String systemStatus = "";
		int db_poLowerQty,db_poUpperQty;
		InputPODetail bean = poList.get(0);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String poIdStr = bean.getNo();
		int poId = Integer.parseInt(poIdStr);
		listCheck = this.getPODetailByPO(poId);

		int int_opKey = bean.getIntOPKey();
		String po = bean.getPo();
		String poLine = bean.getPoLine();
		String color = bean.getColor();
		String colorType = this.checkColorType(color);
		InputGroupDetail beanGroup = this.checkIsSpecialCase(bean);
		ArrayList<InputPODetail> listPOSumQty = this.getPODetailForOP(bean, beanGroup, colorType, "POUL");
		if ( ! listPOSumQty.isEmpty()) {
			InputPODetail beanTmp = listPOSumQty.get(0);
			db_poLowerQty = beanTmp.getPoLowerQty();
			db_poUpperQty = beanTmp.getPoUpperQty();
		} else {
			db_poLowerQty = 0;
			db_poUpperQty = 0;
		}
		if (int_opKey == 0 && ((db_poLowerQty > 1) || (db_poLowerQty == 1 && db_poUpperQty >= 1))) {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus =
					po + " / " + poLine + " have POPuang with same docdate and material no. Need to create POPuang first. ";
		} else if ( ! listCheck.isEmpty()) {
			iconStatus = spdcModel.upsertSORPODetailChange(poList);
			spdModel.updateIsGroupRecheckForSORPODetail(false, poIdStr);
			bgjModel.execUpdateOrderQtyCal();
			listCheck = this.getPODetailByPO(Integer.parseInt(bean.getNo()));
		} else {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = " This PO already create lot.";
		}
		if (listCheck.isEmpty()) {
			listCheck.add(bean);
			listCheck.get(0).setIconStatus(iconStatus);
			listCheck.get(0).setSystemStatus(systemStatus);
		} else {
			listCheck.get(0).setIconStatus(iconStatus);
			listCheck.get(0).setSystemStatus(systemStatus);
		}
		return listCheck;
	}

	@Override
	public ArrayList<InputTempProdDetail> searchGroupOptionListForNull()
	{
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		ArrayList<InputGroupDetail> listAG = new ArrayList<>();
		List<String> articleList = new ArrayList<>();
		InputTempProdDetail bean;
		double db_baseOrderQty;
		double DBLotQtyMaxWithOC = 0.00;
		double DBLotQtyMin = 0.00;
		String article = "";
		String colorType = "";

		String no = "";
		String groupNo = "";
		String subGroup = "";
		String groupOptionsStr = "";
		ArrayList<InputTempProdDetail> list = stpModel.getSORTempProdDetailWithNullGroupOption();
		for (int z = 0; z < list.size(); z ++ ) {
			listAG.clear();
			articleList.clear();
			bean = list.get(z);
			no = bean.getNo();
			groupOptionsStr = "";
			db_baseOrderQty = bean.getDbProdQty();
			colorType = bean.getColorType();
			article = bean.getArticle();
			articleList.add(article);
			InputGroupDetail beanGroup = new InputGroupDetail();
			beanGroup.setArticleList(articleList);
			listAG.add(beanGroup);
			listAG = this.getArticleSubGroupDetailByArticleListNColor(listAG, colorType);
			for (InputGroupDetail beanTmp : listAG) {
				beanTmp.getDbLotQtyMax();
				DBLotQtyMin = beanTmp.getDbLotQtyMin();
				groupNo = beanTmp.getGroupNo();
				subGroup = beanTmp.getSubGroup();
				DBLotQtyMaxWithOC = beanTmp.getDbLotQtyMaxWithOC();
				if (DBLotQtyMin <= db_baseOrderQty && db_baseOrderQty <= DBLotQtyMaxWithOC) {
					groupOptionsStr = groupOptionsStr+groupNo+Config.C_COLON+subGroup+Config.C_COMMA;
				}
			}
			if ( ! groupOptionsStr.equals("")) {
				groupOptionsStr = StringHandler.removeLastChar(groupOptionsStr);
			}
			if ( ! groupOptionsStr.equals("")) {
				stpModel.updateGroupOptionForSORTempProd(no, groupOptionsStr);
			}
		}
		return list;
	}

	private ArrayList<Double> sortHashmapByValueToList(HashMap<Double, Integer> map)
	{
		ArrayList<Double> listResult = new ArrayList<>();
		map = (HashMap<Double, Integer>) ProdCreatedDetailDaoImpl.sortByValue(map);
		for (Entry<Double, Integer> entry : map.entrySet()) {
			int count = 0;
			while (count < entry.getValue()) {
				count = count+1;
				listResult.add(entry.getKey());
			}
		}
		return listResult;
	}
}
