package dao.implement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import Test.utilities.StringHandler;
import dao.ReportDao;
import entities.CustomerMonthlyReportDetail;
import entities.ForecastStatusReportDetail;
import entities.InputDateRunningDetail;
import entities.InputFacWorkDateDetail;
import entities.InputGroupDetail;
import entities.InputPlanningReportDetail;
import entities.MonthlyCapReportDetail;
import entities.POManagementDetail;
import entities.POStatusReportDetail;
import entities.PlanningReportDetail;
import entities.ProdOrderRunningDetail;
import entities.RecreateRedyeReportDetail;
import entities.RedyePlanningReportDetail;
import entities.SummaryMonthlyCapReportDetail;
import entities.TotalGroupDyeDetail;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.PlanningProdModel;
import model.master.ProdOrderRunningModel;
import model.master.RelationPOAndProdOrderModel;
import model.master.SubGroupDetailModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class ReportDaoImpl implements ReportDao {
	private static final String DATE_PATTERN = "MM/yyyy";
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;

	private String conType;
	public DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	private String leftJoinSPOPDA =
			"" + " left join [PPMM].[dbo].[SOR_POPuangDetail] as SPOPD on a.[POPuangId] = SPOPD.Id  \r\n ";

	private String leftJoinSPOPDB =
			"" + " left join [PPMM].[dbo].[SOR_POPuangDetail] as SPOPD on b.[POPuangId] = SPOPD.Id\r\n ";
	private String leftJoinSPOPDSamePOId = ""
			+ " left join [PPMM].[dbo].[SOR_POPuangDetail] as SPOPD on b.[POPuangId] = SPOPD.Id AND\r\n"
			+ "														   SPOPD.POId  = A.POId\r\n ";
	private String innerJoinSPOPDNotSamePOId = ""
			+ " inner join [PPMM].[dbo].[SOR_POPuangDetail] as SPOPD on SPOPD.Id = a.[POPuangId] AND\r\n"
			+ "														    SPOPD.POId  <> A.[Id] AND\r\n"
			+ " 													    SPOPD.[DataStatus] = 'O' \r\n ";
	private String leftJoinPOStatusSPDC =
			"" + "  left join [PPMM].[dbo].[SOR_PODetailChange] as SPDC on a.[Id] = SPDC.[POId] AND SPDC.[DataStatus] = 'O' \r\n";
	private String leftJoinIAD =
			"" + "  left join [PPMM].[dbo].[InputArticleDetail] as IAD on a.[Article] = IAD.[Article]\r\n";

	private String leftJoinMasterRelationSORPOAndSO_MRSPAS =
			"" + " LEFT JOIN [PPMM].[dbo].[MasterRelationSORPOAndSO] AS MRSPAS ON MRSPAS.POId = a.POId\r\n"; 
	private String declarePOMainNPOInstead = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempPOMainNPOInstead') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempPOMainNPOInstead; \r\n"
			+ " select\r\n"
			+ " a.[Id]\r\n"
			+ "	, A.POId \r\n" 
			+ "	,a.id as TempProdId\r\n"
			+ "	,a.[ProductionOrderType]\r\n"
			+ " ,a.[ForecastId] ,a.[PlanInsteadId] ,a.[RuleNo] ,a.[ColorType]\r\n"
			+ " ,a.[ProductionOrder] ,a.[FirstLot] ,a.[ProdOrderQty] ,a.[GroupOptions]\r\n"
			+ " ,a.[GroupBegin] ,a.[PPMMStatus] ,a.[DataStatus] ,a.[ChangeDate]\r\n"
			+ " ,a.[ChangeBy] ,a.[CreateDate] ,a.[CreateBy] ,a.[Batch]\r\n"
			+ " INTO #tempPOMainNPOInstead\r\n"
			+ " from [PPMM].[dbo].[SOR_TempProd] as a\r\n" 
			+ " where ( a.POId is not null ) and \r\n"
			+ "			a.DataStatus = 'O'  \r\n" 
			; 
	private String declarePOMainNPOInsteadMonthly = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempPOMainNPOInstead') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempPOMainNPOInstead; \r\n"
			+ " select\r\n"
			+ " a.[Id]\r\n"
			+ "	, A.POId \r\n" 
			+ " ,a.[ForecastId] ,a.[PlanInsteadId] ,a.[RuleNo] ,a.[ColorType]\r\n"
			+ " ,a.[ProductionOrder] ,a.[FirstLot] ,a.[ProdOrderQty] ,a.[GroupOptions]\r\n"
			+ " ,a.[GroupBegin] ,a.[PPMMStatus] ,a.[DataStatus] ,a.[ChangeDate]\r\n"
			+ " ,a.[ChangeBy] ,a.[CreateDate] ,a.[CreateBy] ,a.[Batch]\r\n"
			+ " INTO #tempPOMainNPOInstead\r\n"
			+ " from [PPMM].[dbo].[SOR_TempProd] as a\r\n" 
			+ " LEFT JOIN (\r\n"
			+ "		SELECT DISTINCT ProductionOrder , OperationEndDate ,UserStatus\r\n"
			+ "   	FROM [PPMM].[dbo].[DataFromSap] AS a \r\n"
			+ "     WHERE (AdminStatus <> 'ForceClosed' AND Operation = '100') \r\n"
			+ "  ) AS dfs100 ON a.ProductionOrder = dfs100.ProductionOrder \r\n"
//			+ " LEFT JOIN (\r\n"
//			+ "		SELECT USD.[Id] ,USD.[UserStatusSapId] ,USD.[UserStatus] \r\n"
//			+ "  	FROM [PPMM].[dbo].[UserStatusDetail] AS USD \r\n"
//			+ "	  	WHERE USD.[UserStatusSapId] = 'U043'or USD.[UserStatusSapId] = 'U046' or USD.[UserStatusSapId] = 'U048' or USD.[UserStatusSapId] = 'U049'or  \r\n"
//			+ "			  USD.[UserStatusSapId] = 'U050'or USD.[UserStatusSapId]= 'U051' or USD.[UserStatusSapId] = 'U056'  or  \r\n"
//			+ "			  USD.[UserStatusSapId] = 'U068' \r\n"
//			
//			+ "  ) AS USD ON dfs100.UserStatus = USD.UserStatus \r\n"  
//U043	รอขาย
//U046	ขายแล้ว
//U048	ตัดเกรดZ
//U049	ยกเลิก
//U050	OVER
//U051	HOLD,รอโอน
//U056	ปิดเพื่อแก้ไข
//U068	ขายเหลือ 
			+ " 	LEFT JOIN (\r\n"
			+ "			SELECT  [Id]\r\n"
			+ "      	,[UserStatusSapId]\r\n"
			+ "      	,[UserStatus] \r\n"
			+ "      	,[DyeFocus]\r\n"
			+ "  		FROM [PPMM].[dbo].[viewUserStatusCondition]\r\n"
			+ "  		WHERE [DyeFocus] = 'X'\r\n" 
			+ "  	) AS viewUSD ON dfs100.UserStatus = viewUSD.UserStatus\r\n"
			+ "  where ( a.POId is not null ) and \r\n"
			+ "		   a.DataStatus = 'O' AND \r\n" 
			+ "	       viewUSD.UserStatus is null and\r\n"
			+ "	       ( ( OperationEndDate is null and dfs100.ProductionOrder is not null ) or dfs100.ProductionOrder is null )\r\n";

	private String leftJoinLApp = ""
			+ "	left join (\r\n"
			+ " 	select distinct a.[POId] ,[SORCFMDate] as LastSORCFMDate, a.[SORDueDate] as LastSORDueDate ,a.[ApprovedBy]  as LastApprovedBy,a.[ApprovedDate]\r\n"
			+ "		from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "		inner join (\r\n"
			+ "	       select a.[POId] , max(a.[ApprovedDate]) as maxApprovedDate\r\n"
			+ "	       from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "	       where  \r\n"
			+ "	       a.DataStatus = 'O' and\r\n"
			+ "	       a.[SorDueDate] is not null \r\n"
			+ "	       Group by  a.[POId]\r\n"
			+ "		) as LApp on a.[POId] = LApp.[POId] and\r\n"
			+ "                  a.[ApprovedDate] = LApp.[maxApprovedDate]\r\n"
			+ "		where  \r\n"
			+ "			a.DataStatus = 'O' and\r\n"
			+ "			a.[SorDueDate] is not null \r\n"
			+ " )as LApp on a.[POId] = LApp.[POId]\r\n";
	private String leftJoinLAppPuang = ""
			+ "	left join (\r\n"
			+ "		select distinct a.[POId] ,[SORCFMDate] as LastSORCFMDate, a.[SORDueDate] as LastSORDueDate ,a.[ApprovedBy]  as LastApprovedBy,a.[ApprovedDate]\r\n"
			+ "		from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "		inner join (\r\n"
			+ "			select a.[POId] , max(a.[ApprovedDate]) as maxApprovedDate\r\n"
			+ "			from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "			where  \r\n"
			+ "			a.DataStatus = 'O' and\r\n"
			+ "			a.[SorDueDate] is not null \r\n"
			+ "			Group by  a.[POId]\r\n"
			+ "		)as LApp on a.[POId] = LApp.[POId] and\r\n"
			+ "                 a.[ApprovedDate] = LApp.[maxApprovedDate]\r\n"
			+ "		where  \r\n"
			+ "			a.DataStatus = 'O' and\r\n"
			+ "			a.[SorDueDate] is not null \r\n"
			+ " )as LApp on b.[Id] = LApp.[POId]\r\n";
	private String leftJoinFCLApp = ""
			+ "	left join (\r\n"
			+ "		select distinct a.[ForecastId] ,[SORCFMDate] as LastSORCFMDate, a.[SORDueDate] as LastSORDueDate ,a.[ApprovedBy]  as LastApprovedBy,a.[ApprovedDate]\r\n"
			+ "		from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "		inner join (\r\n"
			+ "			select a.[ForecastId] , max(a.[ApprovedDate]) as maxApprovedDate\r\n"
			+ "			from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "			where [DataStatus] = 'O'  \r\n"
			+ "			Group by  a.[ForecastId]\r\n"
			+ "		)as LApp on a.[ForecastId] = LApp.[ForecastId] and\r\n"
			+ "                 a.[ApprovedDate] = LApp.[maxApprovedDate]\r\n"
			+ "		where [DataStatus] = 'O'  \r\n"
			+ " ) as LApp on a.[Id] = LApp.[ForecastId]   \r\n";
	private String leftJoinPOStatusFMain = ""
			+ " left join (\r\n"
			+ "	  	select a.POId, MAX(c.LTDeliveryDate) as MaxLTDeliveryDate  \r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "		inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "		where a.DataStatus = 'O' AND\r\n "
			+ "           c.DataStatus = 'O' and\r\n "
			+ "           a.POId is not null\r\n"
			+ "		group by  a.POId  \r\n"
			+ "	) as f on a.POId = f.POId  \r\n";
	private String leftJoinPOStatusBCD = ""
			+ "	left join (\r\n"
			+ "		SELECT [Id]\r\n"
			+ "		      ,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
			+ "		      ,[CustomerShortName] as [CustomerName]\r\n"
			+ "		      ,[ChangeDate]\r\n"
			+ "		      ,[CreateDate]\r\n"
			+ "		  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
			+ "	) as cd on b.[CustomerNo] = cd.[CustomerNo] \r\n";

	private String leftJoinPOStatusMaxCFMMain = ""
			+ "	left join (\r\n"
			+ "		select a.POId, MAX(c.LTCFMDate) as MaxLTCFMDate  \r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "		inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "		where a.DataStatus = 'O' AND\r\n"
			+ "           c.DataStatus = 'O' and\r\n"
			+ "           a.POId is not null\r\n"
			+ "		group by  a.POId  \r\n"
			+ "	) as maxCFM on a.POId = maxCFM.POId \r\n";
	private String leftJoinPOStatusFPuang = ""
			+ " left join (\r\n"
			+ "	  select a.POId, MAX(c.LTDeliveryDate) as MaxLTDeliveryDate  \r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "		inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "		where a.DataStatus = 'O' AND\r\n"
			+ "           c.DataStatus = 'O' and\r\n"
			+ "           a.POId is not null\r\n"
			+ "		group by  a.POId  \r\n"
			+ "	) as f on b.POId = f.POId  \r\n";
	private String leftJoinPOStatusMaxCFMPuang = ""
			+ "	left join (\r\n"
			+ "		  select a.POId, MAX(c.LTCFMDate) as MaxLTCFMDate  \r\n"
			+ "			from #tempPOMainNPOInstead as a\r\n"
			+ "			inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "			where a.DataStatus = 'O' AND\r\n"
			+ "               c.DataStatus = 'O' and\r\n"
			+ "               a.POId is not null\r\n"
			+ "			group by  a.POId  \r\n"
			+ "	) as maxCFM on b.POId = maxCFM.POId \r\n";
	private String leftJoinPOStatusACD = ""
			+ "	left join (\r\n"
			+ "      SELECT [Id]\r\n"
			+ "      ,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
			+ "      ,[CustomerShortName] as [CustomerName]\r\n"
			+ "      ,[ChangeDate]\r\n"
			+ "      ,[CreateDate]\r\n"
			+ "	     , CustomerType\r\n"
//			+ "	     ,  isSabina\r\n"
			+ "      , case\r\n"
			+ "          when [CustomerName] not like '%SABINA%' and [CustomerShortName] not like '%SBF%'\r\n"
			+ "          then 0 \r\n"
			+ "			 else 1 \r\n"
			+ "        end as isSabina\r\n"
			+ "      FROM [PCMS].[dbo].[CustomerDetail]\r\n"
			+ "	) as cd on a.[CustomerNo] = cd.[CustomerNo] \r\n";
	private String leftJoinPOStatusSTC =
			"" + "	LEFT JOIN [PPMM].[dbo].[StockCustomerDate] AS stc on a.CustomerNo = stc.CustomerNo \r\n";

	private String declareTempBeginSpecialDesign = "\r\n"
			+ "  IF OBJECT_ID('tempdb..#tempBeginSpecialDesign') IS NOT NULL \r\n"
			+ "	DROP TABLE #tempBeginSpecialDesign; \r\n"
			+ "	SELECT DISTINCT\r\n"
			+ "		A.Id\r\n"
			+ "		,a.[PO]\r\n"
			+ "		,a.[POLine] \r\n"
			+ "		,a.[CustomerDue]\r\n"
			+ "		,a.[Design]  \r\n"
			+ "		, case \r\n"
			+ "			when SCDO.Id is not null then SCDO.Id\r\n"
			+ "			else SCTW.Id \r\n"
			+ "			end as SpecailDesignId\r\n"
			+ "		into #tempBeginSpecialDesign\r\n"
			+ "	FROM [PPMM].[dbo].[SOR_PODetail] AS A\r\n"
			+ "	left JOIN [PPMM].[dbo].[SpecialCaseDesignMatch] as SCDO on a.Design = SCDO.DesignCPOne\r\n"
			+ "	left JOIN [PPMM].[dbo].[SpecialCaseDesignMatch] as SCTW on a.Design = SCTW.DesignCPTwo \r\n"
			+ "	WHERE (SCDO.DesignCPOne IS NOT NULL OR SCTW.DesignCPTwo IS NOT NULL ) AND \r\n"
			+ "       a.DataStatus = 'O' \r\n"
			+ "	order by a.PO ,a.POLine,a.CustomerDue\r\n";
	private String declareTempMaxPlan = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempMaxPlan') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempMaxPlan ; \r\n"
			+ " SELECT a.Id,A.PO , A.POLine,A.CustomerDue,SpecailDesignId,MaxLTCFMDate,MaxLTDeliveryDate, LastSORCFMDate AS SPLastSORCFMDate, LastSORDueDate AS SPLastSORDueDate\r\n"
			+ "	into #tempMaxPlan\r\n"
			+ "	FROM #tempBeginSpecialDesign AS A\r\n"
			+ "	left join (\r\n"
			+ "		select a.POId, MAX(c.LTCFMDate) as MaxLTCFMDate  \r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "		inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "		where a.DataStatus = 'O' AND\r\n"
			+ "           c.DataStatus = 'O' and\r\n"
			+ "           a.POId is not null\r\n"
			+ "		group by  a.POId  \r\n"
			+ "	) as b on a.Id = b.POId\r\n"
			+ "	left join (\r\n"
			+ "		select a.POId, MAX(c.LTDeliveryDate) as MaxLTDeliveryDate \r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "		inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "		where a.DataStatus = 'O' AND\r\n"
			+ "           c.DataStatus = 'O' and\r\n"
			+ "           a.POId is not null\r\n"
			+ "		group by  a.POId  \r\n"
			+ "			) as c on a.Id =c.POId\r\n"
			+ "	left join (\r\n"
			+ "		select distinct a.[POId] ,[SORCFMDate] as LastSORCFMDate, a.[SORDueDate] as LastSORDueDate ,a.[ApprovedBy]  as LastApprovedBy,a.[ApprovedDate]\r\n"
			+ "		from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "		inner join (\r\n"
			+ "			select a.[POId] , max(a.[ApprovedDate]) as maxApprovedDate\r\n"
			+ "			from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "			where  \r\n"
			+ "			a.DataStatus = 'O' and\r\n"
			+ "			a.[SorDueDate] is not null \r\n"
			+ "			Group by  a.[POId]\r\n"
			+ "		)as LApp on a.[POId] = LApp.[POId] and\r\n"
			+ "                 a.[ApprovedDate] = LApp.[maxApprovedDate]\r\n"
			+ "		where   \r\n"
			+ "			a.DataStatus = 'O' and\r\n"
			+ "			a.[SorDueDate] is not null \r\n"
			+ "	)as LApp on a.Id = LApp.[POId]\r\n"
			+ " where MaxLTCFMDate is not null    \r\n"
			+ "	order by a.PO ,a.POLine,a.CustomerDue\r\n";
	private String declareTempMainSpecialDesign = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempMainSpecialDesign') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempMainSpecialDesign; \r\n"
			+ "	select a.Id  ,SPMaxCusDue,SPMaxLTCFMDate ,SPMaxLTDeliveryDate,b.SPLastSORCFMDate,b.SPLastSORDueDate\r\n"
			+ "	into #tempMainSpecialDesign\r\n"
			+ "	from (\r\n"
			+ " 	select mainA.Id \r\n"
			+ " 	,(select top 1 CustomerDue\r\n"
			+ "	 		from #tempMaxPlan as a\r\n "
			+ "	 		where a.PO = mainA.PO and a.SpecailDesignId = mainA.SpecailDesignId\r\n"
			+ "	 		order by CustomerDue desc\r\n"
			+ "	 	)  as SPMaxCusDue\r\n"
			+ " 	,(select top 1 MaxLTCFMDate\r\n"
			+ "	 		from #tempMaxPlan as a\r\n"
			+ "	 		where a.PO = mainA.PO and\r\n"
			+ "               a.CustomerDue = mainA.CustomerDue and \r\n"
			+ "               a.SpecailDesignId = mainA.SpecailDesignId\r\n"
			+ "	 		order by MaxLTCFMDate desc\r\n"
			+ "	 	)  as SPMaxLTCFMDate\r\n"
			+ " 	,(select top 1 MaxLTDeliveryDate\r\n"
			+ "	 		from #tempMaxPlan as a\r\n"
			+ "	 		where a.PO = mainA.PO and\r\n"
			+ "               a.CustomerDue = mainA.CustomerDue and\r\n"
			+ "               a.SpecailDesignId = mainA.SpecailDesignId\r\n"
			+ "	 		order by MaxLTDeliveryDate desc\r\n"
			+ "	 	)  as SPMaxLTDeliveryDate\r\n"
			+ "	 	from #tempBeginSpecialDesign as mainA\r\n"
			+ "	) as a \r\n"
			+ "	inner join #tempMaxPlan as b on a.Id = b.Id\r\n"
			+ "	where a.SPMaxLTCFMDate is not null\r\n";
	private String declareTempSumPOQtyPuang = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempSumPOQtyPuang') IS NOT NULL \r\n"
			+ "   DROP TABLE #tempSumPOQtyPuang; \r\n"
			+ "  SELECT a.POPuangId ,SUM(OrderQty) as sumPOQtyPuang\r\n"
			+ "  into #tempSumPOQtyPuang\r\n"
			+ "  FROM [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ "  where DataStatus = 'O' and\r\n"
			+ "        POPuangId IS NOT NULL	\r\n"
			+ "  group by POPuangId  \r\n"
			+ " order by POPuangId \r\n";
	private String declareTempMaxPOProd = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempMaxPOProd') IS NOT NULL \r\n"
			+ "   DROP TABLE #tempMaxPOProd;\r\n"
			+ " SELECT [POId] ,max([ProductionOrder]) as maxProducitonOrder\r\n"
			+ " 	into #tempMaxPOProd\r\n"
			+ "		FROM #tempPOMainNPOInstead\r\n"
			+ "		where [POId] is not null\r\n"
			+ "		group by POId  \r\n"
			+ "  	order by POId \r\n";
	private String declareTempSumPOWOLast = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempSumPOWOLast') IS NOT NULL \r\n"
			+ "   DROP TABLE #tempSumPOWOLast;\r\n"
			+ " select a.POId ,b.maxProducitonOrder , \r\n"
			+ " 	case\r\n"
			+ "			when ( [SpecialType] = 'EMBOSS' AND c.[Design] LIKE '%'+[DesignCheckTwo]+'%' ) or   \r\n"
			+ "				 ( [SpecialType] = 'EMBOSS' AND c.[Design] LIKE '%'+[DesignCheckOne]+'%' )\r\n"
			+ "			 	THEN OrderQty*  (  (sumProdQtyWOLast/sumProdQtyAll)*100 ) /100\r\n"
			+ "			when c.SpecialType is null or c.Variable is null then a.sumProdQtyWOLast\r\n"
			+ "			when c.SpecialType = 'Divide' then a.sumProdQtyWOLast * c.Variable\r\n"
			+ "			when c.SpecialType = 'Multiply' then a.sumProdQtyWOLast / c.Variable\r\n"
			+ "			end as sumProdQtyWOLast\r\n"
			+ " into #tempSumPOWOLast\r\n"
			+ " from (\r\n"
			+ " 	select a.POId ,sum(a.ProdOrderQty) as sumProdQtyWOLast\r\n"
			+ "  		FROM #tempPOMainNPOInstead as a\r\n"
			+ "  		left join #tempMaxPOProd as b on a.POId = b.POId and\r\n"
			+ "                                     a.ProductionOrder = b.maxProducitonOrder \r\n"
			+ "  		where a.[POId] is not null and\r\n"
			+ "               b.maxProducitonOrder is null AND\r\n"
			+ "               A.DataStatus <> 'X'\r\n"
			+ "  	 	group by a.POId\r\n"
			+ "  )  as a\r\n"
			+ "  left join (\r\n"
			+ "       select a.POId ,sum(a.ProdOrderQty) as sumProdQtyAll\r\n"
			+ "  		FROM #tempPOMainNPOInstead as a\r\n"
			+ "  	 	where a.[POId] is not null AND DataStatus <> 'X'\r\n"
			+ "		group by a.POId \r\n"
			+ "		) as SUMPRODALL ON SUMPRODALL.POId = A.POId\r\n"
			+ "  left join #tempMaxPOProd as b on a.POId = b.POId \r\n"
			+ "  left join (\r\n"
			+ "		SELECT  a.Id ,a.PO ,a.POLine ,a.OrderQty ,c.SpecialType ,a.Design,c.Variable,[DesignCheckTwo],[DesignCheckOne]\r\n"
			+ "	  	from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ "	  	left join [PPMM].[dbo].[InputArticleDetail] as b on a.Article = b.Article\r\n"
			+ "	  	left join (\r\n"
			+ "			select *\r\n"
			+ "			from [PPMM].[dbo].[SpecialCaseMR] \r\n"
			+ "			where DataStatus = 'O' and Variable is not null\r\n"
			+ "   	) as c on b.SpecialCaseId = c.Id\r\n"
			+ "  ) as c on a.POId = c.Id\r\n"
			+ "  order by a.POId  \r\n";
	private String declareTempBatchRPAP = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempBatchRPAP') IS NOT NULL \r\n"
			+ "   DROP TABLE #tempBatchRPAP;\r\n"
			+ " SELECT  a.[POId]\r\n"
			+ "      ,[PO]\r\n"
			+ "      ,[POLine]\r\n"
			+ "      , CAST(  ROW_NUMBER() OVER ( PARTITION BY a.POId ORDER BY [PO] ,[POLine],[ProductionOrder] )as varchar(max)) +'/'+ CAST([Batch]as varchar(max) ) as [Batch]\r\n"
			+ "      ,[ProductionOrder]\r\n"
//			+ "      ,[ApproveStatus] \r\n"
			+ "      ,[Volume]\r\n"
			+ "  into #tempBatchRPAP\r\n"
			+ "  FROM [PPMM].[dbo].[RelationPOAndProdOrder] as a\r\n"
			+ "  inner join (\r\n"
			+ "		SELECT [POId],COUNT(*) as Batch \r\n"
			+ "		FROM [PPMM].[dbo].[RelationPOAndProdOrder] \r\n"
			+ "		where DataStatus = 'O'\r\n"
			+ "		group by [POId]\r\n"
			+ "  ) as b on a.[POId] = b.[POId]  \r\n"
			+ "  where DataStatus = 'O'\r\n"
			+ " ORDER BY [PO] ,[POLine],[ProductionOrder]\r\n";

	private String leftJoinTempLotMainA = ""
			+ "	left join (\r\n"
			+ "		select a.*\r\n"
			+ "		, case\r\n"
			+ "			when SPECAL.SpecialType is null or SPECAL.Variable is null  then a.ProdOrderQty\r\n"
			+ "			when SPECAL.SpecialType = 'Divide' then a.ProdOrderQty*SPECAL.Variable\r\n"
			+ "			when SPECAL.SpecialType = 'Multiply' then a.ProdOrderQty/SPECAL.Variable\r\n"
			+ "			end as ProdOrderQtyCal\r\n"
			+ "     ,case\r\n"
			+ "			when SPECAL.SpecialType = 'Divide' then a.ProdOrderQty*SPECAL.Variable\r\n"
			+ "			when SPECAL.SpecialType = 'Multiply' then a.ProdOrderQty/SPECAL.Variable\r\n"
			+ "			else a.[ProdOrderQty]\r\n"
			+ "			end as QtyGreigeMR\r\n"
			+ "     ,SPECAL.IsCotton\r\n"
			+ "     ,SPECAL.ArticleComment\r\n"
			+ "     ,SPECAL.Variable\r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "     left join (\r\n"
			+ "	  		SELECT a.Id as POId,a.PO ,a.POLine ,c.SpecialType ,c.Variable ,b.IsCotton\r\n"
			+ "     ,C.ArticleComment\r\n"
			+ "	 		from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ "	  		left join [PPMM].[dbo].[InputArticleDetail] as b on a.Article = b.Article\r\n"
			+ "	  		left join (select * from [PPMM].[dbo].[SpecialCaseMR] where DataStatus = 'O' )as c on b.SpecialCaseId = c.Id\r\n"
			+ "  	) as SPECAL on a.POId = SPECAL.POId\r\n"
			+ " ) as a on a.POId = b.Id\r\n";

	private String leftJoinTempLotSubA = ""
			+ "	left join (\r\n"
			+ "		select a.*\r\n"
			+ "		, case\r\n"
			+ "			when SPECAL.SpecialType is null or SPECAL.Variable is null  then a.ProdOrderQty\r\n"
			+ "			when SPECAL.SpecialType = 'Divide' then a.ProdOrderQty*SPECAL.Variable\r\n"
			+ "			when SPECAL.SpecialType = 'Multiply' then a.ProdOrderQty/SPECAL.Variable\r\n"
			+ "			end as ProdOrderQtyCal\r\n"
			+ "     ,case\r\n"
			+ "			when SPECAL.SpecialType = 'Divide' then a.ProdOrderQty*SPECAL.Variable\r\n"
			+ "			when SPECAL.SpecialType = 'Multiply' then a.ProdOrderQty/SPECAL.Variable\r\n"
			+ "			else a.[ProdOrderQty]\r\n"
			+ "			end as QtyGreigeMR\r\n"
			+ "     ,SPECAL.IsCotton\r\n"
			+ "     ,SPECAL.ArticleComment\r\n"
			+ "     ,SPECAL.Variable\r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "     left join (\r\n"
			+ "	  		SELECT a.Id as POId,a.PO ,a.POLine ,c.SpecialType ,c.Variable ,b.IsCotton\r\n"
			+ "               ,c.ArticleComment\r\n"
			+ "	 		from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ "	  		left join [PPMM].[dbo].[InputArticleDetail] as b on a.Article = b.Article\r\n"
			+ "	  		left join (select * from [PPMM].[dbo].[SpecialCaseMR] where DataStatus = 'O' )as c on b.SpecialCaseId = c.Id\r\n"
			+ "  	) as SPECAL on a.POId = SPECAL.POId\r\n"
			+ " ) as a on a.POId = b.POId\r\n";
	private String leftJoinPPMMDyeE =
			"" + " left join [PPMM].[dbo].[PlanLotSORDetail] as e on a.ProductionOrder = e.ProductionOrder\r\n";

//	private String leftJoinPOStatusSCMR =
//			"" + " left join [PPMM].[dbo].[SpecialCaseMR] as SCMR on a.Article = scmr.Article\r\n";
	private String declareTempFromLotMain = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempFromLotMain') IS NOT NULL   \r\n"
			+ "    DROP TABLE #tempFromLotMain;"
			+ "     SELECT \r\n"
			+ "		 a.[Id]\r\n"
			+ "		,a.[DocDate]\r\n"
			+ "		,a.[CustomerNo]\r\n"
			+ "     ,a.[CustomerName]\r\n"
			+ "     ,a.[PO]\r\n"
			+ "     ,a.[POLine]\r\n"
			+ "     ,a.[MaterialNoTWC] \r\n"
			+ "     ,case\r\n"
			+ "			when IAD.[ArticleReplaced] is not null and IAD.ArticleReplaced <> '' then IAD.[ArticleReplaced]\r\n"
			+ "       	else a.Article\r\n"
			+ "       	end as Article\r\n"
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
			+ "     , CAST( a.[OrderQtyCal] AS DECIMAL(15, 2)) as [OrderQtyCal] \r\n"
			+ "     , case\r\n"
			+ "			when SPDC.OrderQty is not null then CAST( SPDC.OrderQty AS DECIMAL(15, 2)) \r\n"
			+ "			else CAST( a.[OrderQtyCalLast] AS DECIMAL(15, 2)) \r\n"
			+ "			end as [OrderQtyCalLast] \r\n"
			+ " 	,case\r\n"
			+ "			when (OrderQtyCalLast%50) >= 1 and (OrderQtyCalLast%50) <= 9 then cast(1 as bit)\r\n"
			+ "			else cast(0 as bit)\r\n"
			+ "		end as isRecheck\r\n"
			+ "     ,a.isGroupRecheck\r\n"
			+ "	   , CustomerType\r\n" 
			+ "		, case \r\n"
			+ "			when stc.StockReceive is not null and stc.StockReceive <> ''  then stc.StockReceive\r\n"
			+ "			else 1\r\n"
			+ "			end as StockReceive   \r\n"
			+ "      , case \r\n"
			+ "			when DayOfMonth is not null and DayOfMonth <> '' then DayOfMonth\r\n"
//			+ "			else '25'\r\n"
			+ "			else CAST( ( SELECT [DayOfMonth] FROM [PPMM].[dbo].[StockDefaultDate] where [DataStatus] ='O' ) AS VARCHAR(2) )\r\n" 
			+ "			end as DayOfMonth \r\n"
			+ "      ,  isSabina\r\n"
			+ "  into #tempFromLotMain \r\n"
			+ "  FROM [PPMM].[dbo].[SOR_PODetail] as a\r\n" 
			+ this.leftJoinPOStatusSPDC 
			+ this.leftJoinPOStatusSTC
			+ this.leftJoinPOStatusACD
			+ this.leftJoinIAD
			+ " where a.DataStatus = 'O' and\r\n"
			+ "		  ( SPDC.[DataStatus] = 'O' OR SPDC.[DataStatus] IS NULL );\r\n";
	private String declareTempLotMain = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempLotMain') IS NOT NULL   \r\n"
			+ "		DROP TABLE #tempLotMain;\r\n"
			+ "	select distinct \r\n"
			+ "		 a.[Id]  \r\n"
			+ "		,b.[Id] as POId \r\n"
			+ "		,a.[ForecastId] ,a.[RuleNo] ,a.[ColorType] ,a.[ProductionOrder]\r\n"
			+ "		,a.[FirstLot] ,a.[ProdOrderQty] ,a.[GroupOptions] ,a.[GroupBegin] ,a.[PPMMStatus],a.CreateDate   \r\n"
			+ "		,c.PlanSystemDate ,c.PlanUserDate ,c.GroupNo ,c.SubGroup \r\n"
			+ "		, ( select distinct ic.[PlanSystemDate] \r\n"
			+ "			from #tempPOMainNPOInstead as ia\r\n"
			+ "			left join [PPMM].[dbo].[TEMP_PlanningLot] as ic on ia.id = ic.TempProdId    \r\n"
			+ "			where 	ia.POId is not null and \r\n"
			+ " 				ia.FirstLot = 'Y' and \r\n"
			+ "					ia.[DataStatus] = 'O' and\r\n"
			+ "					ic.[DataStatus] = 'O' and\r\n"
			+ "					ia.POId = a.POId) as FirstLotDate\r\n"
			+ "		, c.[PlanBy]\r\n"
			+ "		, c.[GroupNo] as [FirstLotGroupNo]\r\n"
			+ "		, c.[SubGroup] as [FirstLotSubGroup],c.[Id] as TempPlanningId \r\n"
			+ "		, b.[ColorCustomer] ,b.[Design] ,b.[CustomerDue] ,b.[Article] \r\n"
			+ "		, case\r\n"
			+ "			when  cd.[CustomerName] is not null and cd.[CustomerName] <> '' then  cd.[CustomerName]\r\n"
			+ "         else b.[CustomerName]\r\n"
			+ "		  end as [CustomerName]\r\n"
			+ "		, b.[DyeAfterGreigeInBegin]\r\n"
			+ "		, b.[LastCFMDate] ,b.[DyeAfterGreigeInLast] ,b.[MaterialNo] ,b.[PO] ,b.[POLine] ,b.DyeAfterCFM ,b.[GreigePlan]  \r\n"
			+ "		, b.[OrderQty]\r\n"
			+ "		, b.[Unit] \r\n"
			+ "		, [LTPOInputDate] ,[LTMakeLotDate] ,[LTBCDate] ,[LTPlanDate] ,[LTCFMDate]\r\n"
			+ "		, [LTCFMAnswerDate] ,[LTDeliveryDate] \r\n"
			+ "		, b.[DocDate]  ,b.POPuangId,null as PPId\r\n"
			+ "		, case \r\n"
			+ "				when b.[DistChannal] = 'EX' then 2\r\n"
			+ "				when b.[DistChannal] = 'HW' then  \r\n"
			+ "					case \r\n"
			+ "						when b.CustomerType = 'EX' then 2\r\n"
			+ "						else 1\r\n"
			+ "						end\r\n"
			+ "				ELSE 0\r\n"
			+ "			end as PriorDistChannal  \r\n"
			+ "		, MaxLTDeliveryDate ,MaxLTCFMDate\r\n"
			+ "     , case\r\n"
			+ "			when SPOPD.POId is not null then 'POMain Puang'"
			+ "			else 'PO' "
			+ "			end as POType\r\n"
			+ "     , b.[MaterialNoTWC] as CustomerMat \r\n"
			+ "     , case\r\n"
			+ "       when LastSORDueDate is not null then LastSORDueDate\r\n"
			+ "	      when tmsd.SPLastSORDueDate is not null then tmsd.SPLastSORDueDate\r\n"
			+ "	   	  when tmsd.SPMaxLTDeliveryDate <= tmsd.[SPMaxCusDue] then \r\n" 
			+ "			ISNULL (\r\n"
			+ "					( SELECT top 1 ipdr.Date \r\n"
			+ "						FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "						inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																	  		sr.StockReceive = b.StockReceive  and\r\n"
			+ "																			sr.[DataStatus] = 'O'\r\n"
			+ "						where ipdr.NormalWork = 'O' and \r\n"
			+ "							( ipdr.Date >= tmsd.SPMaxLTDeliveryDate and ipdr.Date <= tmsd.[SPMaxCusDue] ) \r\n"
			+ "						ORDER BY Date desc\r\n"
			+ "					)  \r\n"
			+ "					, MaxLTDeliveryDate\r\n"
			+ "				)\r\n"
			+ " 	  WHEN MaxLTDeliveryDate <= b.[CustomerDue]  then \r\n"
			+ "			ISNULL (\r\n"
			+ "					( SELECT top 1 ipdr.Date \r\n"
			+ "						FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "						inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																			sr.StockReceive = b.StockReceive and \r\n"
			+ "																			sr.[DataStatus] = 'O'\r\n"
			+ "						where ipdr.NormalWork = 'O' and \r\n"
			+ "							( ipdr.Date >= MaxLTDeliveryDate and ipdr.Date <= b.[CustomerDue] ) \r\n"
			+ "						ORDER BY Date desc\r\n"
			+ "					)  \r\n"
			+ "					, MaxLTDeliveryDate\r\n"
			+ "				)\r\n"
			+ "       else MaxLTDeliveryDate\r\n"
			+ "       end as SORDueDate\r\n"
			+ "     ,case\r\n"
			+ "       	when LastSORCFMDate is not null then LastSORCFMDate\r\n"
			+ "	    	when tmsd.SPLastSORCFMDate is not null then tmsd.SPLastSORCFMDate\r\n"
			+ "	    	when tmsd.SPMaxLTCFMDate is not null then tmsd.SPMaxLTCFMDate\r\n"
			+ " 	  	WHEN MaxLTDeliveryDate <= b.[CustomerDue] THEN b.[LastCFMDate]\r\n"
			+ "       	else MaxLTCFMDate\r\n"
			+ "       	end as SORCFMDate\r\n"
			+ "     ,LApp.ApprovedDate\r\n"
			+ "	 	,b.CustomerNo\r\n"
			+ "     ,bRPAP.Batch\r\n"
			+ "		,a.QtyGreigeMR\r\n" 
			+ "   , case\r\n"
			+ "			when isRecheck = cast(1 as bit) or isHW = 1 then b.[OrderQtyCalLast]\r\n"
			+ "			else ceiling(b.[OrderQtyCalLast] )  \r\n"
			+ "			end as OrderQtyCalLast \r\n"
			+ "     , case\r\n"
			+ "			when SPOPD.POId is not null then b.Id\r\n"
			+ "			else ''\r\n "
			+ "			end as POIdPuangMain\r\n"
			+ "     , case\r\n"
			+ "			when SPOPD.POId is not null then b.PO\r\n"
			+ "			else ''\r\n "
			+ "			end as POPuangMain\r\n"
			+ "     , case\r\n"
			+ "			when SPOPD.POId is not null then b.POLine\r\n"
			+ "			else '' \r\n"
			+ "			end as POLinePuangMain\r\n"
			+ "     , case\r\n"
			+ "			when SPOPD.POId is not null then b.[CustomerDue]\r\n"
			+ "			else '' \r\n"
			+ "			end as CustomerDuePuangMain\r\n"
			+ "		 ,bRPAP.[Volume] AS [VolumeFG]  \r\n"
			+ "      ,B.[PODate]\r\n"
			+ "      ,a.IsCotton\r\n"
			+ "      ,b.[Division]"
			+ "    ,[StockReceive],DayOfMonth\r\n"
			+ "    ,isSabina\r\n"
			+ "	  ,b.Color\r\n"
			+ "   ,b.LabRef\r\n"
			+ "	  ,b.LabRefLotNo \r\n"
			+ "	  ,IPR.[PlanningRemark]\r\n"
			+ ",MRSPAS.SaleOrder\r\n"
			+ ",MRSPAS.SaleLine\r\n" 
			+ " , e.UserStatus\r\n"
			+ " , e.LabStatus\r\n"
			+ " ,e.LabNo\r\n"
			+ " ,e.Shade\r\n"
			+ " ,e.BookNo\r\n"
			+ " ,CAST(e.QuantityKG AS DECIMAL(13,3)) as QuantityKG\r\n"
			+ "  , CASE \r\n"
			+ "			WHEN e.DueDate IS NOT NULL THEN e.DueDate  \r\n"
			+ "			ELSE NULL\r\n" 
			+ "		END AS DueDate\r\n"
			+ " ,e.LotNo\r\n"
			+ " ,e.[PlanGreigeDate]\r\n"
			+ " ,e.[GreigeInDate]\r\n"
			+ " ,e.[OperationEndDate]\r\n"
			+ "	 into  #tempLotMain\r\n"
			+ " from #tempFromLotMain as b \r\n"
			+ this.leftJoinTempLotMainA
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON A.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ "	left join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId    \r\n"
			+ " left join [PPMM].[dbo].[InputArticleDetail] as iad on iad.Article = b.Article \r\n"
			+ " left join #tempSumPOWOLast as TSWOL on B.Id = TSWOL.POId and \r\n"
			+ "                                        a.ProductionOrder = TSWOL.maxProducitonOrder\r\n\r\n"
			+ " left join #tempSumPOQtyPuang as TSPOQP on B.POPuangId = TSPOQP.POPuangId \r\n"
			+ " left join #tempMainSpecialDesign as tmsd on tmsd.Id = a.POId\r\n"
			+ " left join #tempBatchRPAP as bRPAP on b.Id = bRPAP.POId and\r\n"
			+ "                                      a.ProductionOrder = bRPAP.ProductionOrder\r\n"
			+ this.leftJoinPOStatusBCD
			+ this.leftJoinLApp  
			+ this.leftJoinSPOPDSamePOId 
			+ this.leftJoinPOStatusFMain  
			+ this.leftJoinPOStatusMaxCFMMain
			+ this.leftJoinPPMMDyeE
			+ this.leftJoinMasterRelationSORPOAndSO_MRSPAS
			+ "	where   ( e.[DataStatus] = 'O' OR E.[DataStatus] is null ) AND \r\n"
			+ "			( \r\n"
			+ "			  (  a.POId is not null and \r\n"
			+ "			  	( a.[DataStatus] = 'O' or a.[DataStatus] = 'P' )\r\n"
			+ "			  )\r\n"
			+ "				or a.[DataStatus] is null  ) and \r\n"
			+ "			( B.[DataStatus] = 'O'  ) AND\r\n"
			+ "			( C.[DataStatus] = 'O'  OR c.[DataStatus] = 'P' OR C.[DataStatus] IS NULL ) AND\r\n"
			+ "			( \r\n"
			+ "				( B.[POPuangId] IS NOT NULL and a.Id is not null ) OR \r\n"
			+ "				( B.POPuangId IS NULL AND SPOPD.POID IS NULL )\r\n"
			+ " 		) and\r\n"
			+ "         ( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )\r\n"
			+ "";
	private String declareTempFromLotSub = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempFromLotSub') IS NOT NULL   \r\n"
			+ "    DROP TABLE #tempFromLotSub;"
			+ "		SELECT \r\n"
			+ "		 a.[Id]\r\n"
			+ "		,a.[DocDate]\r\n"
			+ "		,a.[CustomerNo]\r\n"
			+ "     ,a.[CustomerName]\r\n"
			+ "     ,a.[PO]\r\n"
			+ "     ,a.[POLine]\r\n"
			+ "     ,a.[MaterialNoTWC] \r\n"
			+ "     ,case\r\n"
			+ "       	when IAD.[ArticleReplaced] is not null and IAD.ArticleReplaced <> '' then IAD.[ArticleReplaced]\r\n"
			+ "       	else a.Article\r\n"
			+ "       	end as Article\r\n"
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
			+ "			ELSE 'Color' \r\n"
			+ "			end as [ColorType]\r\n"
			+ "     , a.OrderQty \r\n"
			+ "     , CAST( a.[OrderQtyCal] AS DECIMAL(15, 2)) as [OrderQtyCal] \r\n"
			+ "     , case\r\n"
			+ "			when SPDC.OrderQty is not null then CAST( SPDC.OrderQty AS DECIMAL(15, 2)) \r\n"
			+ "			else CAST( a.[OrderQtyCalLast] AS DECIMAL(15, 2)) \r\n"
			+ "			end as [OrderQtyCalLast] \r\n"
			+ " 	,case\r\n"
			+ "			when (OrderQtyCalLast%50) >= 1 and (OrderQtyCalLast%50) <= 9 then cast(1 as bit)\r\n"
			+ "			else cast(0 as bit)\r\n"
			+ "		end as isRecheck\r\n"
			+ "     ,a.isGroupRecheck\r\n"
			+ "	   , CustomerType\r\n"
//			+ "      , case \r\n"
//			+ "          when cd.CustomerName is not null or cd.CustomerName <> '' then RIGHT(LTRIM(RTRIM(cd.CustomerName)),2)  \r\n"
//			+ "          else null\r\n"
//			+ "          end as CustomerType\r\n"
			+ "		, case \r\n"
			+ "			when stc.StockReceive is not null and stc.StockReceive <> ''  then stc.StockReceive\r\n"
			+ "			else 1\r\n"
			+ "			end as StockReceive   \r\n"
			+ "      , case \r\n"
			+ "			when DayOfMonth is not null and DayOfMonth <> '' then DayOfMonth\r\n"
//			+ "			else '25'\r\n"
			+ "			else CAST( ( SELECT [DayOfMonth] FROM [PPMM].[dbo].[StockDefaultDate] where [DataStatus] ='O' ) AS VARCHAR(2) )\r\n"
			+ "			end as DayOfMonth \r\n"
			+ "      ,  isSabina\r\n"
			+ "     ,SPOPD.POId\r\n"
			+ "     ,SPOPD.POId as POIdPuangMain\r\n"
			+ "     ,SPOPD.PO AS POPuangMain\r\n"
			+ "     ,SPOPD.POLine AS POLinePuangMain\r\n"
			+ "     ,SPOPD.CustomerDue as CustomerDuePuangMain\r\n"
			+ " into #tempFromLotSub\r\n"
			+ " from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ this.innerJoinSPOPDNotSamePOId
//			+ " left join [PPMM].[dbo].[SpecialCaseMR] as SCMR on a.Article = scmr.Article\r\n"
			+ this.leftJoinPOStatusSPDC
			+ this.leftJoinPOStatusSTC
			+ this.leftJoinPOStatusACD
			+ this.leftJoinIAD
			+ "  where a.DataStatus = 'O' and \r\n"
			+ "			SPOPD.DataStatus = 'O' and\r\n"
			+ "			( SPDC.[DataStatus] = 'O' OR SPDC.[DataStatus] IS NULL ) \r\n";

	private String declareTempLotSub = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempLotSub') IS NOT NULL   \r\n"
			+ "    DROP TABLE #tempLotSub;\r\n"
			+ "	select distinct \r\n"
			+ "		 a.[Id]  \r\n"
			+ "		,b.[Id] as  [POId]\r\n"
			+ "		,a.[ForecastId] ,a.[RuleNo] ,a.[ColorType] ,a.[ProductionOrder]\r\n"
			+ "		,a.[FirstLot] ,a.[ProdOrderQty] ,a.[GroupOptions] ,a.[GroupBegin] ,a.[PPMMStatus],a.CreateDate   \r\n"
			+ "		,c.PlanSystemDate  ,c.PlanUserDate   ,c.GroupNo ,c.SubGroup \r\n"
			+ "		, ( select distinct  ic.[PlanSystemDate] \r\n"
			+ "			from #tempPOMainNPOInstead as ia\r\n"
			+ "			left join [PPMM].[dbo].[TEMP_PlanningLot] as ic on ia.id = ic.TempProdId    \r\n"
			+ "			where ia.POId is not null and \r\n"
			+ "					ia.FirstLot = 'Y' and \r\n"
			+ "					ia.[DataStatus] = 'O' and\r\n"
			+ "					ic.[DataStatus] = 'O' and\r\n"
			+ "					ia.POId = a.POId) as FirstLotDate\r\n"
			+ "		, c.[PlanBy]\r\n"
			+ "		, c.[GroupNo] as [FirstLotGroupNo]\r\n"
			+ "		, c.[SubGroup] as [FirstLotSubGroup],c.[Id] as TempPlanningId \r\n"
			+ "		, b.[ColorCustomer] ,b.[Design] ,b.[CustomerDue] ,b.[Article] \r\n"
			+ "		, case\r\n"
			+ "			when  cd.[CustomerName] is not null and cd.[CustomerName] <> '' then  cd.[CustomerName]\r\n"
			+ "         else b.[CustomerName]\r\n"
			+ "		  end as [CustomerName]\r\n"
			+ "		, b.[DyeAfterGreigeInBegin]\r\n"
			+ "		, b.[LastCFMDate] ,b.[DyeAfterGreigeInLast] ,b.[MaterialNo] ,b.[PO] ,b.[POLine] ,b.DyeAfterCFM ,b.[GreigePlan]  \r\n"
			+ "		, b.[OrderQty]\r\n"
			+ "		, b.[Unit] \r\n"
			+ "		, [LTPOInputDate] ,[LTMakeLotDate] ,[LTBCDate] ,[LTPlanDate] ,[LTCFMDate]\r\n"
			+ "		, [LTCFMAnswerDate] ,[LTDeliveryDate] \r\n"
			+ "		 ,b.[DocDate]  ,b.POPuangId,B.POId as PPId\r\n"
			+ "		, case \r\n"
			+ "				when b.[DistChannal] = 'EX' then 2\r\n"
			+ "				when b.[DistChannal] = 'HW' then \r\n"
			+ "					case \r\n"
			+ "						when b.CustomerType = 'EX' then 2\r\n"
			+ "						else 1\r\n"
			+ "						end\r\n"
			+ "				ELSE 0\r\n"
			+ "			end as PriorDistChannal  \r\n"
			+ "		, MaxLTDeliveryDate ,MaxLTCFMDate\r\n"
			+ "		, case\r\n"
			+ "			when b.Id = b.POId then 'POMain Puang'\r\n"
			+ "			else 'POSub Puang'\r\n"
			+ "			end as POType\r\n"
			+ "    , b.[MaterialNoTWC] as CustomerMat \r\n"
			+ "     ,case\r\n"
			+ "       when LastSORDueDate is not null then LastSORDueDate\r\n"
			+ " 	  WHEN MaxLTDeliveryDate <= b.[CustomerDue]  then \r\n"
			+ "			ISNULL (\r\n"
			+ "					( SELECT top 1 ipdr.Date \r\n"
			+ "						FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "						inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and\r\n"
			+ "                                                                         sr.StockReceive = b.StockReceive and \r\n"
			+ "																			sr.[DataStatus] = 'O'\r\n"
			+ "						where ipdr.NormalWork = 'O' and \r\n"
			+ "							( ipdr.Date >= MaxLTDeliveryDate and ipdr.Date <= b.[CustomerDue] ) \r\n"
			+ "						ORDER BY Date desc\r\n"
			+ "					)  \r\n"
			+ "					, MaxLTDeliveryDate\r\n"
			+ "				)\r\n"
			+ "       else MaxLTDeliveryDate\r\n"
			+ "       end as SORDueDate\r\n"
			+ "     ,case\r\n"
			+ "       	when LastSORCFMDate is not null then LastSORCFMDate\r\n"
			+ "       	else tlm.SORCFMDate\r\n"
			+ "       	end as SORCFMDate\r\n"
			+ "     ,LApp.ApprovedDate\r\n"
			+ "	    ,b.CustomerNo\r\n"
			+ "     ,bRPAP.Batch\r\n"
			+ "		,a.QtyGreigeMR\r\n" 
			+ "		,b.OrderQtyCalLast\r\n "
			+ "	    ,  POIdPuangMain \r\n"
			+ "	    ,  POPuangMain \r\n"
			+ "	     ,  POLinePuangMain \r\n"
			+ "      , CustomerDuePuangMain\r\n"
			+ "		 ,bRPAP.[Volume] AS [VolumeFG]  \r\n"
			+ "      ,B.[PODate]\r\n"
			+ "      ,a.IsCotton\r\n"
			+ "      ,b.[Division],[StockReceive],DayOfMonth\r\n"
			+ "      ,isSabina\r\n"
			+ "	  ,b.Color\r\n"
			+ "   ,b.LabRef\r\n"
			+ "	  ,b.LabRefLotNo \r\n"
			+ "	  ,IPR.[PlanningRemark]\r\n"
			+ "  , tlm.SaleOrder\r\n"
			+ " ,e.SaleLine\r\n"
			+ " ,e.UserStatus\r\n"
			+ " ,e.LabStatus\r\n"
			+ " ,e.LabNo\r\n"
			+ " ,e.Shade\r\n"
			+ " ,e.BookNo\r\n"
			+ " ,CAST(e.QuantityKG AS DECIMAL(13,3)) as QuantityKG\r\n"
			+ " ,e.DueDate\r\n"
			+ " ,e.LotNo\r\n"
			+ " ,e.[PlanGreigeDate]\r\n"
			+ " ,e.[GreigeInDate]\r\n"
			+ " ,e.[OperationEndDate]\r\n"
			+ "	 into  #tempLotSub\r\n"
			+ " from #tempFromLotSub as b \r\n"
			+ this.leftJoinTempLotSubA
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON A.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ "	left join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ " left join [PPMM].[dbo].[InputArticleDetail] as iad on iad.Article = b.Article \r\n"
			+ " LEFT JOIN (\r\n"
			+ "   SELECT [Article] ,[FormulaYD] ,[FormulaPC] ,[FormulaKG] ,[FormulaMR] \r\n"
			+ "	  FROM [PPMM].[dbo].[InputConversionDetail]\r\n"
			+ "	  where DataStatus = 'O'\r\n"
			+ " ) AS ICD ON ICD.Article = b.Article \r\n"
			+ " left join #tempSumPOWOLast as TSWOL on a.ProductionOrder = TSWOL.maxProducitonOrder\r\n"
			+ " left join #tempSumPOQtyPuang as TSPOQP on B.POPuangId = TSPOQP.POPuangId \r\n"
			+ " left join #tempBatchRPAP as bRPAP on b.[Id] = bRPAP.POId and\r\n"
			+ "                                      a.ProductionOrder = bRPAP.ProductionOrder\r\n"
			+ this.leftJoinPOStatusBCD
			+ this.leftJoinLAppPuang
			+ this.leftJoinPOStatusFPuang
			+ this.leftJoinPOStatusMaxCFMPuang
			+ this.leftJoinSPOPDB
			+ this.leftJoinPPMMDyeE
			+ "	inner join (select distinct POId , SORCFMDate ,SaleOrder,SaleLine\r\n"
			+ "				from #tempLotMain \r\n"
			+ " )  as tlm on tlm.POId = SPOPD.POId\r\n"
			+ "	where	( e.[DataStatus] = 'O' OR E.[DataStatus] is null ) AND  (	(  a.POId is not null and \r\n"
			+ "				( a.[DataStatus] = 'O' or a.[DataStatus] = 'P' )\r\n"
			+ "				)\r\n"
			+ "				or a.[DataStatus] is null  ) and \r\n"
			+ "			( B.[DataStatus] = 'O' ) AND\r\n"
			+ "			( C.[DataStatus] = 'O' OR c.[DataStatus] = 'P' OR C.[DataStatus] IS NULL ) and\r\n"
			+ "         ( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )\r\n";
	private String declareTempBeginData = "\r\n"

			+ "IF OBJECT_ID('tempdb..#tempLot') IS NOT NULL   \r\n"
			+ "		DROP TABLE #tempLot;\r\n"
			+ " select * \r\n"
			+ " into #tempLot \r\n"
			+ " FROM (\r\n"
			+ "	 select * from #tempLotMain \r\n"
			+ "	 union\r\n"
			+ "	 select * from  #tempLotSub\r\n "
			+ " ) AS A    \r\n"
			+ " IF OBJECT_ID('tempdb..#tempBeginData') IS NOT NULL   \r\n"
			+ "   DROP TABLE #tempBeginData; \r\n"
			+ " SELECT a.*\r\n"
			+ " 	, CASE \r\n"
			+ "		WHEN DayOfMonth = 'LastDayOfMonth' THEN EOMONTH(SORDueDate)\r\n"
			+ "		WHEN DayOfMonth is null or DayOfMonth = '' THEN \r\n"
			+ "			cast(\r\n"
			+ "				CAST( year(SORDueDate) as varchar(4)) + \r\n"
			+ "					RIGHT('00' + cast(month(SORDueDate) as varchar(2)),2)    +\r\n"
//			+ "				RIGHT('00' + LTRIM( cast('25' as varchar(2))),2) AS Date)\r\n"
			+ "				RIGHT('00' + LTRIM( cast( "
			+ "  				( SELECT [DayOfMonth] FROM [PPMM].[dbo].[StockDefaultDate] where [DataStatus] ='O' )"  
			+ " as varchar(2))),2) AS Date)\r\n" 
			+ "		ELSE cast(\r\n"
			+ "				CAST( year(SORDueDate) as varchar(4)) + \r\n"
			+ "					RIGHT('00' + cast(month(SORDueDate) as varchar(2)),2)    +\r\n"
			+ "				RIGHT('00' + LTRIM( cast(DayOfMonth as varchar(2))),2) AS Date)\r\n"
			+ "		END AS StockDate\r\n"
			+ " 	,dateadd(month,datediff(month,0,dateadd(month,1,SORDueDate)),0) as NextMonthSORDueDateWithOne\r\n"
			+ " 	, dateadd( month, datediff(month,0, SORDueDate ), 0) as SORDueDateWithOne\r\n"
			+ " INTO #tempBeginData\r\n"
			+ " FROM #tempLot AS A\r\n"
			+ " LEFT join [PPMM].[dbo].[InputDateRunning] as c on a.CustomerDue = c.Date;\r\n";
	private String declareTempApprovedDate = "\r\n"
			+ "IF OBJECT_ID('tempdb..#tempApprovedDate') IS NOT NULL   \r\n"
			+ "	DROP TABLE #tempApprovedDate;  \r\n"
			+ " SELECT    C.SORDueDate , COUNT(B.ProductionOrder ) AS CountPRD\r\n"
			+ "  INTO #tempApprovedDate\r\n"
			+ "  FROM [PPMM].[dbo].[SOR_PODetail] AS A\r\n"
			+ "  INNER JOIN #tempPOMainNPOInstead as b on a.id = b.POId\r\n"
			+ "  inner join [PPMM].[dbo].[ApprovedPlanDate] as c on c.POId = b.POId\r\n"
			+ "  where  a.DataStatus = 'O' and\r\n"
			+ "         b.DataStatus = 'O' and\r\n"
			+ "			c.DataStatus = 'O' and\r\n"
			+ "			c.[SorDueDate] is not null \r\n"
			+ "  GROUP BY C.SORDueDate \r\n";
	private String declareTempFromPO = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempFromPO') IS NOT NULL   \r\n"
			+ "    DROP TABLE #tempFromPO;  \r\n"
			+ " select A.* \r\n"
			+ " , CASE	\r\n"
			+ " 	WHEN SORDueDateLast <= a.[CustomerDue] THEN ''\r\n"
			+ " 	ELSE a.ProductionOrder+' '+ convert(varchar, a.[SORDueDate], 103)+' delivery after Customer Due.'\r\n"
			+ "  	END AS [Remark]\r\n"
			+ " , CASE	\r\n"
			+ " 	WHEN SORDueDateLast > a.[CustomerDue] THEN '1'\r\n"
			+ " 	WHEN SORDueDateLast = a.[CustomerDue] THEN '0'\r\n"
			+ " 	WHEN SORDueDateLast <= a.[CustomerDue] THEN '2'\r\n"
			+ " 	ELSE '0'\r\n"
			+ "  	END AS [CaseSORDueDate]\r\n"
			+ "		into #tempFromPO\r\n"
			+ "	 from (\r\n"
			+ " select a.*   \r\n"
			+ "	,case\r\n"
			+ "			when b.SORCFMDate is not null then b.SORCFMDate\r\n"
			+ "			else a.SORCFMDate\r\n"
			+ "			end as SORCFMDateLast \r\n"
			+ "	 ,case \r\n"
			+ "		when b.SORDueDate is not null then b.SORDueDate\r\n"
			+ "		when a.SORDueDate > StockDate then \r\n"
			+ "			( SELECT ipdr.Date \r\n"
			+ "				FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "				inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and\r\n"
			+ "															  		sr.StockReceive = a.StockReceive and\r\n"
			+ "																	sr.[DataStatus] = 'O'\r\n"
			+ "				LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "				where ipdr.NormalWork = 'O' and\r\n"
			+ "					  ipdr.Date >= a.NextMonthSORDueDateWithOne and \r\n"
			+ "					  ( tad.CountPRD is null or tad.CountPRD < 50 )\r\n"
			+ "         	ORDER BY Date\r\n"
			+ "				OFFSET 1 ROWS\r\n"
			+ "				FETCH NEXT 1 ROWS ONLY )\r\n"
			+ "		else \r\n"
			+ "			case \r\n"
			+ "				when a.SORDueDate < ( SELECT ipdr.Date \r\n"
			+ "										FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "										inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "                                                                                         sr.StockReceive = a.StockReceive and\r\n"
			+ "																							sr.[DataStatus] = 'O'\r\n"
			+ "										LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "										where ipdr.NormalWork = 'O' and ipdr.Date >= a.SORDueDateWithOne \r\n"
			+ "										ORDER BY Date \r\n"
			+ "										OFFSET 1 ROWS\r\n"
			+ "										FETCH NEXT 1 ROWS ONLY\r\n"
			+ "										)  \r\n"
			+ "				then \r\n"
			+ "					( SELECT top 1 ipdr.Date \r\n"
			+ "						FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "						inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "                                                                         sr.StockReceive = a.StockReceive and\r\n"
			+ "																			sr.[DataStatus] = 'O'\r\n"
			+ "						LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "						where ipdr.NormalWork = 'O' and ipdr.Date >= a.SORDueDateWithOne and\r\n"
			+ "                           ipdr.Date >= a.SORDueDate  and\r\n"
			+ "                           ( tad.CountPRD is null or tad.CountPRD < 50 ) and\r\n"
			+ "						       ipdr.Date >= ( SELECT ipdr.Date \r\n"
			+ "												FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "												inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																									sr.StockReceive = a.StockReceive and \r\n"
			+ "																									sr.[DataStatus] = 'O'\r\n"
			+ "												LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "												where ipdr.NormalWork = 'O' and \r\n"
			+ "											  			ipdr.Date >= a.SORDueDateWithOne \r\n"
			+ "												ORDER BY Date \r\n"
			+ "												OFFSET 1 ROWS\r\n"
			+ "												FETCH NEXT 1 ROWS ONLY\r\n"
			+ "										)   \r\n"
			+ "						ORDER BY Date  \r\n"
			+ "						)\r\n"
			+ "				else\r\n"
			+ "					( SELECT top 1 ipdr.Date \r\n"
			+ "						FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "						inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																			sr.StockReceive = a.StockReceive and \r\n"
			+ "																			sr.[DataStatus] = 'O'\r\n"
			+ "						LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "						where ipdr.NormalWork = 'O' and \r\n"
			+ "							  ipdr.Date >= a.SORDueDateWithOne and \r\n"
			+ "							  ipdr.Date >= a.SORDueDate and\r\n"
			+ "							  ( tad.CountPRD is null or tad.CountPRD < 50 ) \r\n"
			+ "						ORDER BY Date  \r\n"
			+ "						)\r\n"
			+ "			end \r\n"
			+ "		end as SORDueDateLast \r\n"
			+ "	 	from #tempBeginData as a  \r\n"
			+ "		left join [PPMM].[dbo].[ApprovedPlanDate] as b on b.POId = a.POId and\r\n"
			+ "			                                              b.DataStatus = 'O' and\r\n"
			+ "			                                              b.[SorDueDate] is not null \r\n"
			+ ") as a\r\n";
	private String declareTempAPFacDyeDate = "\r\n"
			+ "----------- FIND BEGIN / LAST DYE DATE FROM FAC HOLIDAY ----------------\r\n"
			+ "IF OBJECT_ID('tempdb..#tempBKKWorkDateRunning') IS NOT NULL   \r\n"
			+ "	DROP TABLE #tempBKKWorkDateRunning  \r\n"
			+ "IF OBJECT_ID('tempdb..#tempAPDyeDate') IS NOT NULL   \r\n"
			+ "	DROP TABLE #tempAPDyeDate  \r\n"
			+ "IF OBJECT_ID('tempdb..#tempAPDyeDate1') IS NOT NULL   \r\n"
			+ "	DROP TABLE #tempAPDyeDate1  \r\n"
			+ "IF OBJECT_ID('tempdb..#tempAPFacDyeDate') IS NOT NULL   \r\n"
			+ "	DROP TABLE #tempAPFacDyeDate   \r\n"
			+ "IF OBJECT_ID('tempdb..#tempAPPODetail') IS NOT NULL   \r\n"
			+ "	DROP TABLE #tempAPPODetail  ;\r\n"
			+ "IF OBJECT_ID('tempdb..#tempWorkDateRunning') IS NOT NULL   \r\n"
			+ "	DROP TABLE #tempWorkDateRunning  ;\r\n"
			+ "	\r\n"
			+ "SELECT \r\n"
			+ "	ROW_NUMBER() OVER( ORDER BY [Date] ASC) as RowNum\r\n"
			+ "	,[Date]\r\n"
			+ "      ,[DateName]\r\n"
			+ "      ,[NormalWork]\r\n"
			+ "	into #tempWorkDateRunning\r\n"
			+ "  FROM [PPMM].[dbo].[InputBKKDateRunning]\r\n"
			+ "  where NormalWork = 'O'\r\n"
			+ "select DISTINCT  a.Id as  POId ,a.PO , a.POLine,a.GreigePlan , a.CustomerDue ,a.Design,a.RuleNo,a.POPuangId,a.MaterialNo\r\n"
			+ "     ,case\r\n"
			+ "       	when IAD.[ArticleReplaced] is not null and IAD.ArticleReplaced <> '' then IAD.[ArticleReplaced]\r\n"
			+ "       	else a.Article\r\n"
			+ "       	end as Article\r\n"
			+ "			,case \r\n"
			+ "				when SPOPD.POId is not null then SPOPD.POId\r\n"
			+ "				else a.Id\r\n"
			+ "				end as POIdCheck\r\n"
			+ "        , MaxSORCFMDateLast as SORCFMDateLast,MaxSORDueDateLast as SORDueDateLast\r\n"
			+ "		into #tempAPPODetail\r\n"
			+ "		from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ "		INNER JOIN #tempFromPO as b on a.Id = b.POId \r\n"
			+ "		INNER JOIN  ( \r\n"
			+ "			select POId  ,max(SORCFMDateLast) AS MaxSORCFMDateLast, MAX(SORDueDateLast) AS MaxSORDueDateLast\r\n"
			+ "			from #tempFromPO as a\r\n"
			+ "			group by POID\r\n"
			+ "		) as BA on B.POId = BA.POId AND BA.MaxSORDueDateLast = B.SORDueDateLast\r\n"
			+ this.leftJoinSPOPDA
			+ this.leftJoinIAD
			+ this.leftJoinPOStatusACD 
			+ "		WHERE B.ApprovedDate IS NULL AND\r\n"
			+ "			  ( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )\r\n"
			+ "	 \r\n"
			+ " select \r\n"
			+ "	a.POId \r\n"
			+ "   ,a.POPuangId\r\n"
			+ "	  , indexCFM\r\n"
			+ "	  , indexCFMAnswer \r\n"
			+ "	  ,DayAdd \r\n"
			+ "into  #tempAPDyeDate  \r\n"
			+ "	from ( SELECT DISTINCT\r\n"
			+ "			 a.POId  \r\n"
			+ "			,a.[PO]\r\n"
			+ "   ,a.POPuangId\r\n"
			+ "		  ,a.[POLine] \r\n"
			+ "		  ,a.[RuleNo]  \r\n"
			+ "		  ,CASE \r\n"
			+ "			WHEN LTDELDS.DayAdd is not null then LTDELDS.DayAdd \r\n"
			+ "			WHEN LTDELAR.DayAdd is not null then LTDELAR.DayAdd \r\n"
			+ "			ELSE 0\r\n"
			+ "			end as DayAdd  \r\n"
			+ "		  ,indexSORDueDate - d.LeadTimeDeliveryToCFM as indexCFM\r\n"
			+ "		  ,indexSORDueDate - d.LeadTimeDyeToCFMAnswer as indexCFMAnswer  \r\n"
			+ "	 FROM #tempAPPODetail AS A\r\n"
			+ "	LEFT JOIN (\r\n"
			+ "		select a.Id, a.ProductionOrder ,a.DataStatus \r\n"
			+ "			,case\r\n"
			+ "				when b.FirstLot is not null then a.FirstLot \r\n"
			+ "				ELSE 'N'\r\n"
			+ "				END AS FirstLot\r\n"
			+ "			,A.POId  \r\n"
			+ "				,case\r\n"
			+ "					when b.FirstLot is not null then 'Y'\r\n"
			+ "					ELSE 'N'\r\n"
			+ "					END AS HaveFirstLot\r\n"
			+ "		from #tempPOMainNPOInstead as a \r\n"
			+ "		left join ( \r\n"
			+ "			SELECT distinct \r\n"
			+ "				POId,FirstLot \r\n"
			+ "			FROM #tempPOMainNPOInstead AS SUBSTP \r\n"
			+ "			where SUBSTP.DataStatus = 'O' and \r\n"
			+ "				  SUBSTP.[FirstLot] = 'Y'\r\n"
			+ "		 ) as b on a.POId = b.POId   \r\n"
			+ "	) AS B ON a.POIdCheck = b.POId and b.[DataStatus] = 'O'\r\n"
			+ "	left join ( \r\n"
			+ "    SELECT *\r\n"
			+ "	   FROM [PPMM].[dbo].[SpecialCaseLeadTime] \r\n"
			+ "	   where Design <> '' and\r\n"
			+ "			 Design is not null and \r\n"
			+ "			 LeadTimeTypeId = 1\r\n"
			+ "	) as LTDELDS on a.Article = LTDELDS.Article and\r\n"
			+ "                 a.Design = LTDELDS.Design\r\n"
			+ "	left join ( \r\n"
			+ "   SELECT *\r\n"
			+ "	  FROM [PPMM].[dbo].[SpecialCaseLeadTime] \r\n"
			+ "	  where ( Design = '' OR Design is null ) and\r\n"
			+ "         LeadTimeTypeId = 1\r\n"
			+ "	) as LTDELAR on a.Article = LTDELAR.Article  \r\n"
			+ "	left join (\r\n"
			+ "			SELECT DISTINCT \r\n"
			+ "			 a.[RuleNo]   \r\n"
			+ "			,a.[Delivery] as LeadTimeDyeToCFMAnswer\r\n"
			+ "			,a.[CFMAnswer]+ a.[Delivery] as LeadTimeDeliveryToCFM\r\n"
			+ "			, case\r\n"
			+ "				when a.LeadTimeType = 'FirstLot' then 'Y'\r\n"
			+ "				else 'N'\r\n"
			+ "				END FirstLot \r\n"
			+ "			FROM [PPMM].[dbo].[InputLeadTimeDetail] AS A  \r\n"
			+ "			WHERE A.[DataStatus] = 'O'\r\n"
			+ "	)as d on a.[RuleNo] = d.[RuleNo] and \r\n"
			+ "          b.[FirstLot] = d.[FirstLot] \r\n"
			+ "	left join (\r\n"
			+ "		SELECT *\r\n"
			+ "				, (select top 1 RowNum \r\n"
			+ "					from #tempWorkDateRunning  as idr\r\n"
			+ "					where idr.Date >= a.SORDueDateLast \r\n"
			+ "					order by idr.Date asc) as indexSORDueDate\r\n"
			+ "		FROM #tempFromPO AS A \r\n"
			+ "	) AS TPL on A.POId = tpl.POId \r\n"
			+ ") as a  \r\n"
			+ "\r\n"
			+ " select 	a.POId \r\n"
			+ "   , a.POPuangId\r\n"
			+ "	  , indexCFM\r\n"
			+ "	  , indexCFMAnswer \r\n"
			+ "	,case \r\n"
			+ "		when LTCFMDate  is null then convert ( date ,GETDATE())\r\n"
			+ "		when LTCFMDate  <= convert ( date ,GETDATE()) then convert ( date ,GETDATE())\r\n"
			+ "		else LTCFMDate\r\n"
			+ "		end as LTCFMDate   \r\n"
			+ "	,case \r\n"
			+ "		when LTCFMAnswerDate is null then convert ( date ,GETDATE())\r\n"
			+ "		when LTCFMAnswerDate <= convert ( date ,GETDATE()) then convert ( date ,GETDATE())\r\n"
			+ "		else LTCFMAnswerDate\r\n"
			+ "		end as LTCFMAnswerDate     \r\n"
			+ " into #tempAPDyeDate1 \r\n"
			+ " from #tempAPDyeDate as a \r\n"
			+ " left join ( \r\n"
			+ "   select RowNum, Date as LTCFMDate\r\n"
			+ "	  from #tempWorkDateRunning  as idr\r\n"
			+ "	  where idr.NormalWork = 'O'  \r\n"
			+ " )  as i on a.indexCFM = i.RowNum\r\n"
			+ " left join (\r\n"
			+ "  select RowNum, Date as LTCFMAnswerDate\r\n"
			+ "	 from #tempWorkDateRunning  as idr\r\n"
			+ "  where idr.NormalWork = 'O'  \r\n"
			+ " ) as j on a.indexCFMAnswer = j.RowNum\r\n"
			+ " select a.POId \r\n"
			+ "   ,case\r\n"
			+ "		when b.minLTCFMDate is null then LTCFMDate\r\n"
			+ "     else minLTCFMDate\r\n"
			+ "     end as LTCFMDate\r\n"
			+ "   ,LTCFMAnswerDate\r\n"
			+ " into #tempAPFacDyeDate\r\n"
			+ " from #tempAPDyeDate1 as a \r\n"
			+ " left join (\r\n"
			+ "    select a.POPuangId , min(LTCFMDate) as minLTCFMDate\r\n"
			+ " 	from #tempAPDyeDate1 as a  \r\n"
			+ " 	where POPuangId is not null\r\n"
			+ "     group by a.POPuangId \r\n"
			+ " ) as b on a.POPuangId = b.POPuangId\r\n"
			+ "";

	private String declarePO = "\r\n"
//			+ this.declarePPMMDye
			+ this.declarePOMainNPOInstead
			+ this.declareTempBeginSpecialDesign
			+ this.declareTempMaxPlan
			+ this.declareTempMainSpecialDesign
			+ this.declareTempSumPOQtyPuang
			+ this.declareTempMaxPOProd
			+ this.declareTempSumPOWOLast
			+ this.declareTempBatchRPAP
			+ this.declareTempFromLotMain
			+ this.declareTempLotMain
			+ this.declareTempFromLotSub
			+ this.declareTempLotSub
			+ this.declareTempBeginData
			+ this.declareTempApprovedDate
			+ this.declareTempFromPO
			+ this.declareTempAPFacDyeDate;
	private String selectPOStatus = ""
			+ "  a.[ApprovedDate]\r\n"
			+ " ,a.[DocDate]\r\n"
			+ " ,A.[POId]\r\n"
			+ " ,[PO]\r\n"
			+ " ,[POLine]\r\n"
			+ " ,[CustomerName]\r\n"
			+ " ,a.[MaterialNo] \r\n"
			+ " ,[Design]\r\n"
			+ " ,[ColorCustomer]\r\n"
			+ " ,[OrderQty]\r\n"
			+ " ,[Unit]\r\n"
			+ " ,[CustomerDue]\r\n"
			+ " ,[GreigePlan]\r\n"
			+ " ,A.ProductionOrder\r\n"
			+ " ,A.ProdOrderQty\r\n"
			+ "		,CASE \r\n"
			+ "       WHEN a.[FirstLot] ='Y' THEN '1 : '+a.[FirstLot]\r\n"
			+ "       WHEN a.[FirstLot] ='N' THEN '2 : '+a.[FirstLot]\r\n"
			+ "       ELSE '3 : '+a.[FirstLot]"
			+ "		  END AS [FirstLot]\r\n "
			+ "	  , case\r\n"
			+ "		when A.UserStatus is not null then 'Prod. order Created'\r\n"
			+ "		when a.SaleOrder is not null OR a.SaleLine IS NOT NULL then 'Sale Created'\r\n"
			+ "		when a.[ApprovedDate] is not null then 'Approved'\r\n"
			+ "		else 'Pending'\r\n"
			+ "		end  as PPMMStatus \r\n"
			+ "   , a.Article\r\n"
			+ "   , a.POType\r\n "
			+ "   , a.CustomerMat \r\n"
			+ "     ,a.Batch\r\n"
			+ "     ,a.QtyGreigeMR\r\n"
			+ "		,A.OrderQtyCalLast\r\n"
			+ "	 ,  CustomerDuePuangMain \r\n "
			+ "  , VolumeFG\r\n"
			+ "  , PODate\r\n"
			+ "  , a.SaleOrder \r\n"
			+ "  , a.SaleLine \r\n"
			+ "  , SORDueDate\r\n"
			+ " ,CASE \r\n"
			+ "		WHEN  a.ApprovedDate IS NULL AND TAF.LTCFMDate IS NOT NULL THEN TAF.LTCFMDate\r\n"
			+ "		ELSE a.SORCFMDate\r\n"
			+ "		END AS SORCFMDate\r\n"
			+ "  , a.LabNo\r\n"
			+ "  , a.Shade\r\n"
			+ "  , a.BookNo\r\n"
			+ "  , a.POIdPuangMain\r\n"
			+ "	 , A.POPuangMain \r\n"
			+ "	 , A.POLinePuangMain \r\n";
	private String selectFCStatus = ""
			+ "   a.[DocDate] \r\n"
			+ " ,[ForecastNo] \r\n"
			+ " ,[ForecastMY]\r\n"
			+ " ,[CustomerName]\r\n"
			+ " ,[TotalForecastQty] \r\n"
			+ " ,[ForecastBLQty]\r\n"
			+ " ,[ForecastNonBLQty] \r\n"
			+ " ,[RemainNonBLQty]\r\n"
			+ " ,[RemainBLQty]\r\n";
	private String fromFCCus = ""
			+ "	from (\r\n"
			+ " 	select\r\n"
			+ " 	 a.[Id]\r\n"
			+ " 	,a.[DocDate]\r\n"
			+ " 	,a.[CustomerNo]\r\n"
			+ " 	,a.[ForecastNo]\r\n"
			+ " 	,a.[ForecastMY]\r\n"
			+ " 	,a.[TotalForecastQty]\r\n"
			+ " 	,a.[ForecastBLQty]\r\n"
			+ " 	,a.[ForecastNonBLQty]\r\n"
			+ " 	,a.[Unit]\r\n"
			+ " 	,a.[RuleNo]\r\n"
			+ " 	,a.[DataStatus]\r\n"
			+ " 	,a.[LastCFMDate]\r\n"
			+ " 	,a.[ChangeDate]\r\n"
			+ " 	,a.[ChangeBy]\r\n"
			+ " 	,a.[CreateBy]\r\n"
			+ " 	,a.[CreatedDate]"
			+ " 	, case\r\n"
			+ "			when  cd.[CustomerName] is not null and cd.[CustomerName] <> '' then  cd.[CustomerName]\r\n"
			+ "    		else a.[CustomerName]\r\n"
			+ "			end as [CustomerName]\r\n"
			+ "      ,[RemainNonBLQty]\r\n"
			+ "      ,[RemainBLQty]\r\n"
			+ " 	from [PPMM].[dbo].[SOR_ForecastDetail] as a\r\n"
			+ " 	left join (\r\n"
			+ "			SELECT [Id]\r\n"
			+ "		      ,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo])) as [CustomerNo]\r\n"
			+ "		      ,[CustomerShortName] as [CustomerName]\r\n"
			+ "		      ,[ChangeDate]\r\n"
			+ "		      ,[CreateDate]\r\n"
			+ "		  	FROM [PCMS].[dbo].[CustomerDetail]\r\n"
			+ "		) as cd on a.[CustomerNo] = cd.[CustomerNo] \r\n"
			+ " ) as a\r\n";

	private String innerJoinFCCusB = ""
			+ "	inner join ( \r\n"
			+ " 	select\r\n"
			+ " 	a.[Id]\r\n"
			+ " 	,a.[DocDate]\r\n"
			+ " 	,a.[CustomerNo]\r\n"
			+ " 	,a.[ForecastNo]\r\n"
			+ " 	,a.[ForecastMY]\r\n"
			+ " 	,a.[TotalForecastQty]\r\n"
			+ " 	,a.[ForecastBLQty]\r\n"
			+ " 	,a.[ForecastNonBLQty]\r\n"
			+ " 	,a.[Unit]\r\n"
			+ " 	,a.[RuleNo]\r\n"
			+ " 	,a.[DataStatus]\r\n"
			+ " 	,a.[LastCFMDate]\r\n"
			+ " 	,a.[ChangeDate]\r\n"
			+ " 	,a.[ChangeBy]\r\n"
			+ " 	,a.[CreateBy]\r\n"
			+ " 	,a.[CreatedDate]"
			+ " 	, case\r\n"
			+ "			when  cd.[CustomerName] is not null and cd.[CustomerName] <> '' then  cd.[CustomerName]\r\n"
			+ "    		else a.[CustomerName]\r\n"
			+ "			end as [CustomerName]\r\n"
			+ " 	from [PPMM].[dbo].[SOR_ForecastDetail] as a"
			+ " 	left join (\r\n"
			+ "			SELECT [Id]\r\n"
			+ "		      ,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
			+ "		      ,[CustomerShortName] as [CustomerName]\r\n"
			+ "		      ,[ChangeDate]\r\n"
			+ "		      ,[CreateDate]\r\n"
			+ "		  	FROM [PCMS].[dbo].[CustomerDetail]\r\n"
			+ "		) as cd on a.[CustomerNo] = cd.[CustomerNo] \r\n"
			+ " ) as b on a.ForecastId = b.[Id]\r\n";

	private String selectMCRFirst = ""
			+ "   A.[GroupNo]\r\n"
			+ "  ,B.[SubGroup]\r\n"
			+ "  ,[Description]\r\n"
			+ "  ,B.[LotPerDay]\r\n"
			+ "  ,[ColorType]\r\n"
			+ "  ,b.[GroupType] \r\n"
			+ "	 ,TotalWorkDate \r\n"
			+ "	 ,CASE		\r\n"
			+ "			WHEN @ISFirstPast = 1 THEN 0\r\n"
			+ "			ELSE TotalWorkDate * b.LotPerDay \r\n"
			+ "			END as TotalLotWorkDate\n"
			+ "	 ,isnull(TotalPlanSystemDatePO,0) as TotalPlanSystemDatePO\r\n"
			+ "	 ,isnull(TotalPlanSystemDateRedye,0) as TotalPlanSystemDateRedye\r\n"
			+ "	 ,isnull(TotalPlanSystemDateForecast,0) as TotalPlanSystemDateForecast\r\n"
//			+ "	 ,TotalPlanSystemDate\r\n"
			+ "  ,b.MeterPerLot\r\n";
	private String selectMCRThird = ""
			+ "   A.[GroupNo]\r\n"
			+ "  ,B.[SubGroup]\r\n"
			+ "  ,[Description]\r\n"
			+ "  ,B.[LotPerDay]\r\n"
			+ "  ,[ColorType]\r\n"
			+ "  ,b.[GroupType] \r\n"
			+ "	 ,TotalWorkDate \r\n"
			+ "	 ,CASE		\r\n"
			+ "			WHEN @ISThirdPast = 1 THEN 0\r\n"
			+ "			ELSE TotalWorkDate * b.LotPerDay \r\n"
			+ "			END as TotalLotWorkDate\n"
			+ "	 ,isnull(TotalPlanSystemDatePO,0) as TotalPlanSystemDatePO\r\n"
			+ "	 ,isnull(TotalPlanSystemDateRedye,0) as TotalPlanSystemDateRedye\r\n"
			+ "	 ,isnull(TotalPlanSystemDateForecast,0) as TotalPlanSystemDateForecast\r\n" 
			+ "  ,b.MeterPerLot\r\n";

	private String selectMCRSecond = ""
			+ "   A.[GroupNo]\r\n"
			+ "  ,B.[SubGroup]\r\n"
			+ "  ,[Description]\r\n"
			+ "  ,B.[LotPerDay]\r\n"
			+ "  ,[ColorType]\r\n"
			+ "  ,b.[GroupType] \r\n"
			+ "	 ,TotalWorkDate \r\n"
			+ "	 ,CASE		\r\n"
			+ "			WHEN @ISSecondPast = 1 THEN 0\r\n"
			+ "			ELSE TotalWorkDate * b.LotPerDay \r\n"
			+ "			END as TotalLotWorkDate\n"
			+ "	 ,isnull(TotalPlanSystemDatePO,0) as TotalPlanSystemDatePO\r\n"
			+ "	 ,isnull(TotalPlanSystemDateRedye,0) as TotalPlanSystemDateRedye\r\n"
			+ "	 ,isnull(TotalPlanSystemDateForecast,0) as TotalPlanSystemDateForecast\r\n" 
			+ "  ,b.MeterPerLot\r\n";

	private String leftJoinTotalPODFirst = ""
			+ " left join ( SELECT a.GroupNo ,a.SubGroup , count(A.PlanSystemDate) as TotalPlanSystemDatePO\r\n"
			+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
			+ "				left join #tempPOMainNPOInstead as b on a.TempProdId = b.id\r\n"
			+ "				LEFT JOIN [PPMM].[dbo].[PlanLotPOAddDetail] AS C ON A.TempPOAddId = C.ID\r\n"
			+ "			  	WHERE A.DataStatus = 'O' and \r\n"
			+ "					  a.[GroupNo] is not null and\r\n"
			+ "				      a.[PlanSystemDate] >= @today and\r\n"
			+ "					  a.[PlanSystemDate] >= @BeginFirstMonth  and \r\n"
			+ "					  a.[PlanSystemDate] <= @LastFirstMonth AND \r\n"
			+ "					   ( b.POId IS NOT NULL or C.DataStatus = 'O')\r\n"
			+ "			  group by a.GroupNo ,a.SubGroup \r\n"
			+ "	) as d on a.[GroupNo] = d.[GroupNo]  AND\r\n"
			+ "           B.[SubGroup] = D.[SubGroup]\r\n";

	private String leftJoinTotalRedyeFirst = ""
			+ " left join ( SELECT a.GroupNo ,a.SubGroup , count(A.PlanSystemDate) as TotalPlanSystemDateRedye\r\n"
			+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
			+ "             INNER JOIN [PPMM].[dbo].[PlanLotRedyeDetail] AS B ON A.RedyeId = b.Id\r\n" 
			+ "			  	WHERE A.DataStatus = 'O' and \r\n"
			+ "					  a.[GroupNo] is not null and\r\n"
			+ "				      a.[PlanSystemDate] >= @today and\r\n"
			+ "					  a.[PlanSystemDate] >= @BeginFirstMonth  and \r\n"
			+ "					  a.[PlanSystemDate] <= @LastFirstMonth  AND \r\n"
			+ "					  A.ReDyeId IS NOT NULL and \r\n"
			+ "					  b.DataStatus = 'O'\r\n"
			+ "			  group by a.GroupNo ,a.SubGroup \r\n"
			+ "	) as E on a.[GroupNo] = E.[GroupNo]  AND\r\n"
			+ "           B.[SubGroup] = E.[SubGroup]\r\n";

	private String leftJoinTotalForecastFFirst = ""
			+ " \r\n"
			+ " left join ( SELECT a.GroupNo ,a.SubGroup , count(A.PlanSystemDate) as TotalPlanSystemDateForecast\r\n"
			+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
			+ "				inner join [PPMM].[dbo].[SOR_TempProd] as b on a.TempProdId = b.id\r\n"
			+ "				inner join #tempFCDate as c on b.ForecastId = c.Id\r\n"
			+ "			  	WHERE A.DataStatus = 'O' and \r\n"
			+ "					  a.[GroupNo] is not null and\r\n"
			+ "				      a.[PlanSystemDate] >= @today and\r\n"
			+ "					  a.[PlanSystemDate] >= @BeginFirstMonth  and \r\n"
			+ "					  a.[PlanSystemDate] <= @LastFirstMonth AND \r\n"
			+ "					  b.ForecastId IS NOT NULL\r\n"
			+ "			  group by a.GroupNo ,a.SubGroup \r\n"
			+ "	) as f on a.[GroupNo] = f.[GroupNo]  AND B.[SubGroup] = f.[SubGroup]\r\n";
	private String leftJoinTotalPODSecond = ""
			+ " left join ( SELECT a.GroupNo ,a.SubGroup , count(A.PlanSystemDate) as TotalPlanSystemDatePO\r\n"
			+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
			+ "				left join #tempPOMainNPOInstead as b on a.TempProdId = b.id\r\n"
			+ "				LEFT JOIN [PPMM].[dbo].[PlanLotPOAddDetail] AS C ON A.TempPOAddId = C.ID\r\n"
			+ "			  	WHERE A.DataStatus = 'O' and \r\n"
			+ "					  a.[GroupNo] is not null and\r\n"
			+ "				      a.[PlanSystemDate] >= @today and\r\n"
			+ "					  a.[PlanSystemDate] >= @BeginSecondMonth  and \r\n"
			+ "					  a.[PlanSystemDate] <= @LastSecondMonth AND \r\n"
			+ "					   ( b.POId IS NOT NULL or C.DataStatus = 'O')\r\n"
			+ "			  group by a.GroupNo ,a.SubGroup \r\n"
			+ "	) as d on a.[GroupNo] = d.[GroupNo]  AND B.[SubGroup] = D.[SubGroup]\r\n";
	private String leftJoinTotalRedyeSecond = ""
			+ " left join ( SELECT a.GroupNo ,a.SubGroup , count(A.PlanSystemDate) as TotalPlanSystemDateRedye\r\n"
			+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
			+ "             INNER JOIN [PPMM].[dbo].[PlanLotRedyeDetail] AS B ON A.RedyeId = b.Id\r\n"
//			+ "				inner join [PPMM].[dbo].[DataFromSap]  as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "			  	WHERE A.DataStatus = 'O' and \r\n"
			+ "					  a.[GroupNo] is not null and\r\n"
			+ "				      a.[PlanSystemDate] >= @today and\r\n"
			+ "					  a.[PlanSystemDate] >= @BeginSecondMonth  and \r\n"
			+ "					  a.[PlanSystemDate] <= @LastSecondMonth  AND \r\n"
			+ "					  A.ReDyeId IS NOT NULL and \r\n"
			+ "					  b.DataStatus = 'O'\r\n"
			+ "			  group by a.GroupNo ,a.SubGroup \r\n"
			+ "	) as E on a.[GroupNo] = E.[GroupNo]  AND B.[SubGroup] = E.[SubGroup]\r\n";
	private String leftJoinTotalForecastFSecond = ""
			+ " \r\n"
			+ " left join ( SELECT a.GroupNo ,a.SubGroup , count(A.PlanSystemDate) as TotalPlanSystemDateForecast\r\n"
			+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
			+ "				inner join [PPMM].[dbo].[SOR_TempProd] as b on a.TempProdId = b.id\r\n"
			+ "				inner join #tempFCDate as c on b.ForecastId = c.Id\r\n"
			+ "			  	WHERE A.DataStatus = 'O' and \r\n"
			+ "					  a.[GroupNo] is not null and\r\n"
			+ "				      a.[PlanSystemDate] >= @today and\r\n"
			+ "					  a.[PlanSystemDate] >= @BeginSecondMonth  and \r\n"
			+ "					  a.[PlanSystemDate] <= @LastSecondMonth AND \r\n"
			+ "					  b.ForecastId IS NOT NULL\r\n"
			+ "			  group by a.GroupNo ,a.SubGroup \r\n"
			+ "	) as f on a.[GroupNo] = f.[GroupNo]  AND B.[SubGroup] = f.[SubGroup]\r\n";
	private String leftJoinTotalPODThird = ""
			+ " left join ( SELECT a.GroupNo ,a.SubGroup , count(A.PlanSystemDate) as TotalPlanSystemDatePO\r\n"
			+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
			+ "				left join #tempPOMainNPOInstead as b on a.TempProdId = b.id\r\n"
			+ "				LEFT JOIN [PPMM].[dbo].[PlanLotPOAddDetail] AS C ON A.TempPOAddId = C.ID\r\n"
			+ "			  	WHERE A.DataStatus = 'O' and \r\n"
			+ "					  a.[GroupNo] is not null and\r\n"
			+ "				      a.[PlanSystemDate] >= @today and\r\n"
			+ "					  a.[PlanSystemDate] >= @BeginThirdMonth  and \r\n"
			+ "					  a.[PlanSystemDate] <= @LastThirdMonth AND \r\n"
			+ "					   ( b.POId IS NOT NULL or C.DataStatus = 'O')\r\n"
			+ "			  group by a.GroupNo ,a.SubGroup \r\n"
			+ "	) as d on a.[GroupNo] = d.[GroupNo]  AND B.[SubGroup] = D.[SubGroup]\r\n";
	private String leftJoinTotalRedyeThird = ""
			+ " left join ( SELECT a.GroupNo ,a.SubGroup , count(A.PlanSystemDate) as TotalPlanSystemDateRedye\r\n"
			+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
			+ "             INNER JOIN [PPMM].[dbo].[PlanLotRedyeDetail] AS B ON A.RedyeId = b.Id\r\n"
//			+ "				inner join [PPMM].[dbo].[DataFromSap]  as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "			  	WHERE A.DataStatus = 'O' and \r\n"
			+ "					  a.[GroupNo] is not null and\r\n"
			+ "				      a.[PlanSystemDate] >= @today and\r\n"
			+ "					  a.[PlanSystemDate] >= @BeginThirdMonth  and \r\n"
			+ "					  a.[PlanSystemDate] <= @LastThirdMonth  AND \r\n"
			+ "					  A.ReDyeId IS NOT NULL and \r\n"
			+ "					  b.DataStatus = 'O'\r\n"
			+ "			  group by a.GroupNo ,a.SubGroup \r\n"
			+ "	) as E on a.[GroupNo] = E.[GroupNo]  AND B.[SubGroup] = E.[SubGroup]\r\n";

	private String leftJoinTotalForecastFThird = ""
			+ " \r\n"
			+ " left join ( SELECT a.GroupNo ,a.SubGroup , count(A.PlanSystemDate) as TotalPlanSystemDateForecast\r\n"
			+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
			+ "				inner join [PPMM].[dbo].[SOR_TempProd] as b on a.TempProdId = b.id\r\n"
			+ "				inner join #tempFCDate as c on b.ForecastId = c.Id\r\n"
			+ "			  	WHERE A.DataStatus = 'O' and \r\n"
			+ "					  a.[GroupNo] is not null and\r\n"
			+ "				      a.[PlanSystemDate] >= @today and\r\n"
			+ "					  a.[PlanSystemDate] >= @BeginThirdMonth  and \r\n"
			+ "					  a.[PlanSystemDate] <= @LastThirdMonth AND \r\n"
			+ "					  b.ForecastId IS NOT NULL\r\n"
			+ "			  group by a.GroupNo ,a.SubGroup \r\n"
			+ "	) as f on a.[GroupNo] = f.[GroupNo]  AND B.[SubGroup] = f.[SubGroup]\r\n";
	private String declareMonthly = "\r\n"
//			+ " declare @BeginDateFirst date  = convert(date, '01/07/2023', 103) ;\r\n"
			+ " declare @BeginDateZero date  = dateadd(month,-1,@BeginDateFirst) ;\r\n"
			+ " declare @BeginDateSecond date  = dateadd(month,1,@BeginDateFirst) ;\r\n"
			+ " declare @BeginDateThird date = dateadd(month,1,@BeginDateSecond) ; \r\n"
//			+ " declare @StockDateFirst varchar(2) = '26'; \r\n"
//			+ " declare @StockDateLast varchar(2) = '25' ; \r\n" 
			+ " declare @StockDateFirst varchar(2) = CAST ( "
			+ "                                      ( SELECT [DayOfMonth] + 1 FROM [PPMM].[dbo].[StockDefaultDate] where [DataStatus] ='O' )"
			+ "                                       AS VARCHAR(2)"
			+ "                                      ); \r\n"
			+ " declare @StockDateLast varchar(2) = CAST ( "
			+ "										( SELECT [DayOfMonth] FROM [PPMM].[dbo].[StockDefaultDate] where [DataStatus] ='O' )"
			+ "										 AS VARCHAR(2)"
			+ "										) ; \r\n" 
			 
			+ " declare @FirstYear varchar(4)  = year( dateadd(YEAR,-1,@BeginDateFirst))   ;\r\n"
			+ " declare @LastYear varchar(4)  = year( dateadd(YEAR,1,@BeginDateFirst))  ; \r\n"
			+ " declare @BeginFirstMonth date  = datefromparts(\r\n"
			+ "			CAST( year(@BeginDateZero) as varchar(4))\r\n"
			+ "			,RIGHT('00' + cast(month(@BeginDateZero) as varchar(2)),2)\r\n"
			+ "		 ,@StockDateFirst\r\n"
			+ "		 ) ; \r\n"
			+ " declare @LastFirstMonth date  = datefromparts(\r\n"
			+ "			CAST( year(@BeginDateFirst) as varchar(4))\r\n"
			+ "			,RIGHT('00' + cast(month(@BeginDateFirst) as varchar(2)),2)\r\n"
			+ "		 ,@StockDateLast\r\n"
			+ "		 )  ; \r\n"
			+ " declare @BeginSecondMonth date  = datefromparts(\r\n"
			+ "			CAST( year(@BeginDateFirst) as varchar(4))\r\n"
			+ "			,RIGHT('00' + cast(month(@BeginDateFirst) as varchar(2)),2)\r\n"
			+ "		 ,@StockDateFirst\r\n"
			+ "		 )   ;\r\n"
			+ " declare @LastSecondMonth date  = datefromparts(\r\n"
			+ "			CAST( year(@BeginDateSecond) as varchar(4))\r\n"
			+ "			,RIGHT('00' + cast(month(@BeginDateSecond) as varchar(2)),2)\r\n"
			+ "		 ,@StockDateLast\r\n"
			+ "		 )  ;\r\n"
			+ " declare @BeginThirdMonth date  = datefromparts(\r\n"
			+ "			CAST( year(@BeginDateSecond) as varchar(4))\r\n"
			+ "			,RIGHT('00' + cast(month(@BeginDateSecond) as varchar(2)),2)\r\n"
			+ "		 ,@StockDateFirst\r\n"
			+ "		 ) ;\r\n"
			+ " declare @LastThirdMonth date  = datefromparts(\r\n"
			+ "			CAST( year(@BeginDateThird) as varchar(4))\r\n"
			+ "			,RIGHT('00' + cast(month(@BeginDateThird) as varchar(2)),2)\r\n"
			+ "		 ,@StockDateLast\r\n"
			+ "		 )  ;\r\n"
			+ " declare @CheckSameDate date  = cast ( DATEADD(month, DATEDIFF(month, 0, GETDATE()), 0  ) as DATE) ;  \r\n"
			+ "  declare @ISFirstPast int = 0 ;  \r\n"
			+ " declare @ISSecondPast int = 0 ;  \r\n"
			+ " declare @ISThirdPast int = 0 ;  \r\n"
			+ "  \r\n"
			+ "  IF(@BeginFirstMonth <= @CheckSameDate  and @CheckSameDate  <= @LastFirstMonth)\r\n"
			+ "   \r\n"
			+ "	BEGIN\r\n"
			+ "		SET @BeginFirstMonth = CAST(GETDATE() AS DATE);\r\n"
			+ "	END \r\n"
			+ "  ELSE IF (@CheckSameDate > @BeginFirstMonth  ) \r\n"
			+ "	BEGIN\r\n"
			+ "		SET @ISFirstPast = 1;\r\n"
			+ "	END\r\n"
			+ "   IF (@BeginSecondMonth <= @CheckSameDate  and @CheckSameDate  <= @LastSecondMonth) \r\n"
			+ "	BEGIN\r\n"
			+ "		SET @BeginSecondMonth =CAST(GETDATE() AS DATE);\r\n"
			+ "	END\r\n"
			+ "  ELSE IF (@CheckSameDate > @BeginSecondMonth  ) \r\n"
			+ "	BEGIN\r\n"
			+ "		SET @ISSecondPast = 1;\r\n"
			+ "	END\r\n"
			+ "  IF (@BeginThirdMonth <= @CheckSameDate  and @CheckSameDate  <= @LastThirdMonth) \r\n"
			+ "	BEGIN\r\n"
			+ "		SET @BeginThirdMonth = CAST(GETDATE() AS DATE);\r\n"
			+ "	END\r\n"
			+ "  ELSE IF (@CheckSameDate > @BeginThirdMonth  ) \r\n"
			+ "	BEGIN\r\n"
			+ "		SET @ISThirdPast = 1;\r\n"
			+ "	END\r\n";
	private String declareMonthlyTempWordDate = "\r\n"
			+ "  IF OBJECT_ID('tempdb..#tempWorkDate') IS NOT NULL \r\n"
			+ "	DROP TABLE #tempWorkDate; \r\n"
			+ "	SELECT *\r\n"
			+ "		,ROW_NUMBER() OVER(PARTITION BY a.[GroupNo] ,a.[SubGroup] ORDER BY a.[GroupNo] ,a.[SubGroup]  ) as RowNum \r\n"
			+ "	into #tempWorkDate\r\n"
			+ "	FROM ( \r\n"
			+ " 	SELECT  \r\n"
			+ "			  a.[GroupNo] ,a.[SubGroup]  ,[WorkDate]  \r\n"
			+ "			, FORMAT (A.[WorkDate], 'MM') as ColMonth\r\n"
			+ "			, FORMAT ([WorkDate],	'yyyy') as ColYear , FORMAT ([WorkDate], 'MM/yyyy') as ColMonthYear \r\n"
			+ "			, DAY(EOMONTH([WorkDate])) AS DaysInMonth	\r\n"
			+ "			,NormalWork\r\n"
			+ "		FROM [PPMM].[dbo].[GroupWorkDate] as a  \r\n"
			+ "		where a.NormalWork = 'O' \r\n"
			+ "	) AS A  \r\n"
			+ "	WHERE ( ColYear >= @FirstYear AND ColYear <= @LastYear ) AND NormalWork = 'O' "
			+ " ORDER BY a.[GroupNo] , a.[SubGroup] \r\n"
			+ " ; ";

	private String selectPO = ""
			+ "  a.ProductionOrder\r\n"
			+ " ,A.CustomerName\r\n"
			+ " ,A.MaterialNo\r\n"
			+ " ,a.ProdOrderQty\r\n"
			+ " ,a.GroupNo\r\n"
			+ " ,a.SubGroup\r\n"
//			+ " ,a.FirstLot\r\n"
			+ "		,CASE \r\n"
			+ "       WHEN a.[FirstLot] ='Y' THEN '1 : '+a.[FirstLot]\r\n"
			+ "       WHEN a.[FirstLot] ='N' THEN '2 : '+a.[FirstLot]\r\n"
			+ "       ELSE '2 : '+a.[FirstLot]"
			+ "		  END AS [FirstLot]\r\n "
			+ " ,a.PlanSystemDate\r\n"
			+ " ,MaxLTDeliveryDate\r\n"
			+ " ,MaxLTCFMDate  \r\n"
			+ " ,A.CustomerDue\r\n"
			+ " ,a.ApprovedDate \r\n"
			+ "	  , case\r\n"
			+ "		when a.UserStatus is not null then 'Prod. order Created'\r\n"
			+ "		when a.SaleOrder is not null OR a.SaleLine IS NOT NULL then 'Sale Created'\r\n"
			+ "		when a.[ApprovedDate] is not null then 'Approved'\r\n"
			+ "		else 'Pending'\r\n"
			+ "		end  as PPMMStatus \r\n"
			+ " ,a.[PO]\r\n"
			+ " ,a.[POLine]\r\n"
			+ " ,case \r\n"
			+ "		when CreateDate = '1900-01-01 00:00:00.000' then null\r\n"
			+ "		else CreateDate\r\n"
			+ "		end as TempLotCreateDate\r\n"
			+ " ,a.Article\r\n"
			+ " ,a.DocDate\r\n"
			+ " , a.POType\r\n "
			+ " , a.UserStatus\r\n"
			+ " , a.LabStatus\r\n"
			+ " , a.CustomerMat \r\n"
			+ " , SORDueDateLast as SORDueDate\r\n"
//			+ " , SORCFMDate\r\n"
			+ " ,CASE \r\n"
			+ "		WHEN  a.ApprovedDate IS NULL AND TAF.LTCFMDate IS NOT NULL THEN TAF.LTCFMDate\r\n"
			+ "		ELSE a.SORCFMDateLast\r\n"
			+ "		END AS SORCFMDate\r\n"
			+ " , Batch\r\n"
			+ "  , POIdPuangMain\r\n"
			+ "	 ,  POPuangMain \r\n"
			+ "	 ,  POLinePuangMain \r\n"
			+ " ,  CustomerDuePuangMain\r\n"
			+ " , VolumeFG\r\n"
			+ " , a.OrderQty\r\n"
			+ " , a.GreigePlan\r\n"
			+ " , a.Unit\r\n"
			+ " ,a.LabNo\r\n"
			+ " ,a.Shade\r\n"
			+ " ,a.BookNo\r\n"
			+ " ,a.QuantityKG\r\n"
			+ " ,a.DueDate\r\n"
			+ " ,CustomerDue AS CustomerDueShow\r\n"
			+ " ,a.LotNo\r\n"
			+ " ,a.PlanningRemark\r\n"
			+ " ,a.Design\r\n"
			+ " ,a.[GreigePlan] as  PlanGreigeDate\r\n"
			+ " ,a.[GreigeInDate]\r\n"
			+ " ,CAST(A.QtyGreigeMR AS DECIMAL(13,3)) AS QtyGreigeMR\r\n";
	private String selectPOAdd = ""
			+ "  a.ProductionOrder\r\n"
			+ " , CustomerName \r\n"
			+ " , MaterialNumber as MaterialNo\r\n"
			+ " , CAST( [QuantityMR] AS DECIMAL(15, 3)) as ProdOrderQty\r\n"
//			+ " , QuantityMR as ProdOrderQty\r\n"
			+ " , a.GroupNo\r\n"
			+ " , a.SubGroup\r\n"
			+ " , a.FirstLot\r\n"
			+ " , a.PlanSystemDate\r\n"
			+ " , MaxLTDeliveryDate\r\n"
			+ " , MaxLTCFMDate \r\n"
			+ " , CustomerDue\r\n"
			+ " , ApprovedDate \r\n"
			+ " , PPMMStatus   \r\n"
			+ "	, a.PurchaseOrder  as [PO]\r\n"
			+ "	, [POLine]\r\n"
			+ "	, TempLotCreateDate  \r\n"
			+ " , a.Article\r\n"
			+ " , DocDate\r\n"
//			+ " , 'START' AS  POType\r\n"
			+ " , POType\r\n"
			+ "  , a.UserStatus\r\n"
			+ " , a.LabStatus\r\n"
			+ " , a.CustomerMat \r\n"
			+ " , SORDueDate\r\n"
			+ " , SORCFMDate\r\n"
			+ " ,  Batch\r\n"
			+ "  , '' as POIdPuangMain\r\n"
			+ "	 ,'' as POPuangMain \r\n"
			+ "	 ,'' as POLinePuangMain \r\n"
			+ "  , '' as CustomerDuePuangMain\r\n"
			+ "  , null as VolumeFG\r\n"
			+ " , null as OrderQty\r\n"
			+ " , GreigePlan\r\n"
			+ " , '' as Unit\r\n"
			+ " , LabNo\r\n"
			+ " , Shade\r\n"
			+ " , BookNo\r\n"
			+ " , QuantityKG\r\n"
			+ " , DueDate\r\n"
			+ " , CustomerDueShow\r\n"
			+ " , LotNo \r\n"
			+ " , PlanningRemark\r\n"
			+ " , Design\r\n"
			+ " ,PlanGreigeDate\r\n"
			+ " ,GreigeInDate\r\n"
			+ " ,CAST(QtyGreigeMR AS DECIMAL(13,3)) AS QtyGreigeMR\r\n";
	private String selectPRDRedye = ""
			+ "  a.ProductionOrder\r\n"
			+ " , CustomerName \r\n"
			+ " , MaterialNumber as MaterialNo\r\n"
			+ " , CAST( [QuantityMR] AS DECIMAL(15, 3)) as ProdOrderQty\r\n"
//			+ " , QuantityMR as ProdOrderQty\r\n"
			+ " , a.GroupNo\r\n"
			+ " , a.SubGroup\r\n"
			+ " , a.FirstLot\r\n"
			+ " , a.PlanSystemDate\r\n"
			+ " , MaxLTDeliveryDate\r\n"
			+ " , MaxLTCFMDate \r\n"
			+ " , CustomerDue\r\n"
			+ " , ApprovedDate \r\n"
			+ " , PPMMStatus   \r\n"
			+ "	, a.PurchaseOrder  as [PO]\r\n"
			+ "	, [POLine]\r\n"
			+ "	, TempLotCreateDate  \r\n"
			+ " , a.Article\r\n"
			+ " , DocDate\r\n"
			+ " ,  POType\r\n"
			+ "  , a.UserStatus\r\n"
			+ " , a.LabStatus\r\n"
			+ " , a.CustomerMat \r\n"
			+ " , SORDueDate\r\n"
			+ " , SORCFMDate\r\n"
			+ " , Batch\r\n"
			+ "  , '' as POIdPuangMain\r\n"
			+ "	 ,'' as POPuangMain \r\n"
			+ "	 ,'' as POLinePuangMain \r\n"
			+ "  , '' as CustomerDuePuangMain\r\n"
			+ "  , null as VolumeFG\r\n"
			+ " , null as OrderQty\r\n"
			+ " , GreigePlan\r\n"
			+ " , '' as Unit\r\n"
			+ " , LabNo\r\n"
			+ " , Shade\r\n"
			+ " , BookNo\r\n"
			+ " , QuantityKG\r\n"
			+ " , DueDate\r\n"
			+ " , CustomerDueShow\r\n"
			+ " , LotNo\r\n"
			+ " , PlanningRemark\r\n"
			+ " , Design\r\n"
			+ " , PlanGreigeDate\r\n"
			+ " , GreigeInDate\r\n"
			+ " ,CAST(QtyGreigeMR AS DECIMAL(13,3)) AS QtyGreigeMR\r\n";
	private String selectPRDFC = ""
			+ "   a.ProductionOrder\r\n"
			+ " , CustomerName\r\n"
			+ " , MaterialNo\r\n"
			+ " , a.ProdOrderQty\r\n"
			+ " , a.GroupNo\r\n"
			+ " , a.SubGroup\r\n"
			+ " , a.FirstLot\r\n"
			+ " , a.PlanSystemDate\r\n"
			+ " , MaxLTDeliveryDate \r\n"
			+ " , MaxLTCFMDate \r\n"
			+ " , CustomerDue\r\n"
			+ " , a.ApprovedDate \r\n"
			+ "	, PPMMStatus   \r\n"
			+ "	, [PO]\r\n"
			+ "	, [POLine]\r\n"
			+ " , case \r\n"
			+ " 	when TempLotCreateDate = '1900-01-01 00:00:00.000' then null\r\n"
			+ "		else TempLotCreateDate\r\n"
			+ " 	end as TempLotCreateDate\r\n"
			+ " , Article\r\n"
			+ " , a.DocDate\r\n"
			+ " , 'FC' AS POType\r\n "
			+ " , UserStatus\r\n"
			+ " , LabStatus\r\n"
			+ " , CustomerMat \r\n"
			+ " , SORDueDate\r\n"
			+ " , SORCFMDate\r\n"
			+ " , Batch\r\n"
			+ "  , POIdPuangMain\r\n"
			+ " , POPuangMain\r\n"
			+ "	, POLinePuangMain \r\n"
			+ " , CustomerDuePuangMain\r\n"
			+ " , VolumeFG\r\n"
			+ " , OrderQty\r\n"
			+ " , GreigePlan\r\n"
			+ " , Unit\r\n"
			+ " ,'' as LabNo\r\n"
			+ " ,'' as Shade\r\n"
			+ " ,'' as BookNo\r\n"
			+ " , QuantityKG\r\n"
			+ " , DueDate\r\n"
			+ " , CustomerDueShow\r\n"
			+ " , LotNo\r\n"
			+ " , PlanningRemark\r\n"
			+ " , Design\r\n"
			+ " , PlanGreigeDate\r\n"
			+ " , GreigeInDate\r\n"
			+ " ,CAST(NULL AS DECIMAL(13,3)) AS QtyGreigeMR\r\n";
	private String leftJoinFCMDEL = ""
			+ "		left join (\r\n"
			+ "	 		select a.ForecastId, MAX(c.LTDeliveryDate) as MaxLTDeliveryDate  \r\n"
			+ "			from [PPMM].[dbo].[SOR_TempProd] as a\r\n"
			+ "			inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "			where a.DataStatus = 'O' AND c.DataStatus = 'O' and a.ForecastId is not null\r\n"
			+ "			group by  a.ForecastId  \r\n"
			+ "		) as MDEL on a.ForecastId = MDEL.ForecastId \r\n";

	private String leftJoinFCMCFM = ""
			+ "		left join (\r\n"
			+ "	 		select a.ForecastId, MAX(c.LTCFMDate) as MaxLTCFMDate  \r\n"
			+ "			from [PPMM].[dbo].[SOR_TempProd] as a\r\n"
			+ "			inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "			where a.DataStatus = 'O' AND c.DataStatus = 'O' and a.ForecastId is not null\r\n"
			+ "			group by  a.ForecastId  \r\n"
			+ "		) as MCFM on a.ForecastId = MCFM.ForecastId \r\n";

	private String leftJoinPRDFCLDD = ""
			+ " left join ( \r\n"
			+ " 	SELECT distinct \r\n"
			+ "			a.[Id] ,a.ForecastMY\r\n"
			+ "			,convert(date ,right(a.ForecastMY,4) + left(a.ForecastMY,2) + '01') as BeginDayDate \r\n"
			+ "			,EOMONTH(convert(date ,right(a.ForecastMY,4) + left(a.ForecastMY,2) + '01'),0) AS LastDayDate \r\n"
			+ "		FROM ( SELECT distinct a.[Id] ,CONVERT(varchar(7) ,a.ForecastMY)  as ForecastMY \r\n"
			+ "			FROM [PPMM].[dbo].[SOR_ForecastDetail] as a   \r\n"
			+ "			where a.DataStatus = 'O' and \r\n"
			+ "				  a.[ForecastMY] is not null  \r\n"
			+ "			) as a \r\n"
			+ " ) as LDD ON a.ForecastId = ldd.Id\r\n";

	private String declarePPMMReDye = ""
			+ " IF OBJECT_ID('tempdb..#tempPPMMReDye') IS NOT NULL   \r\n"
			+ "		DROP TABLE #tempPPMMReDye;\r\n"
			+ "	 select distinct\r\n"
			+ "   RDD.ProductionOrder\r\n"
			+ " , a.GroupNo\r\n"
			+ " , a.SubGroup\r\n"
			+ " , 'N' as FirstLot\r\n"
			+ " , a.PlanSystemDate \r\n"
			+ " , RDD.[CustomerName]\r\n"
//			+ " , case\r\n"
//			+ "		when  cd.[CustomerName] is not null and cd.[CustomerName] <> '' then  cd.[CustomerName]\r\n"
//			+ "     else RDD.[CustomerName]\r\n"
//			+ "		end as [CustomerName]\r\n"
			+ " , RDD.MaterialNumber\r\n"
			+ "  , RDD.QuantityMR \r\n"
			+ " , RDD.PurchaseOrder\r\n"
			+ " , RDD.Article\r\n"
			+ "	, 'Prod. order Created' as PPMMStatus\r\n"
			+ " , RDD.[DueDate]  \r\n"
			+ " , RDD.UserStatus\r\n"
			+ " , RDD.LabStatus\r\n"
			+ " , RDD.CustomerMaterial as CustomerMat \r\n"
			+ " , cast (null as Date) as MaxLTDeliveryDate\r\n"
			+ " , cast (null as Date)  as MaxLTCFMDate \r\n"
			+ " , cast (null as Date)  as ApprovedDate \r\n"
			+ " , cast (null as varchar)  as [POLine]\r\n"
			+ " , cast (null as Date)  as TempLotCreateDate  \r\n"
			+ " , cast (null as Date)  as DocDate\r\n"
			+ " , cast (null as Date)  as SORDueDate\r\n"
			+ " , cast (null as Date)  as SORCFMDate\r\n"
			+ " , RDD.DataStatus as ReDyeStatus \r\n"
			+ " , RDD.LotNo\r\n"
			+ " , RDD.QuantityKG\r\n"
			+ " ,RDD.LabNo\r\n"
			+ " ,RDD.Shade\r\n"
			+ " ,RDD.BookNo \r\n"
			+ " ,rdd.POType\r\n"
			+ " ,rdd.PlanGreigeDate\r\n"
			+ " ,rdd.GreigeInDate\r\n"
			+ " ,rdd.GreigePlan\r\n"
//			+ "		,CASE"
//			+ "			WHEN RDD.[GreigeInDate] IS NOT NULL AND RDD.[GreigeInDate]  <> '' THEN RDD.[GreigeInDate] "
//			+ "         ELSE RDD.[PlanGreigeDate] "
//			+ "			END as GreigePlan\r\n"
			+ " ,RDD.CustomerDue\r\n"
			+ " ,RDD.DueDate AS CustomerDueShow\r\n"
			+ " ,IPR.PlanningRemark\r\n"
			+ " ,RDD.Design\r\n"
			+ " ,RDD.Batch\r\n"
			+ " ,RDD.[QtyGreigeMR]\r\n"
			+ " into #tempPPMMReDye \r\n"
			+ " from [PPMM].[dbo].[PlanLotRedyeDetail] AS RDD  \r\n"
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON RDD.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ " INNER JOIN [PPMM].[dbo].[TEMP_PlanningLot] as a ON a.RedyeId = RDD.Id\r\n"
//			+ this.leftJoinPOStatusRDDCD
			+ " where a.ReDyeId is not null  and RDD.DataStatus = 'O' and \r\n"
			+ "		  ( ( a.[DataStatus] = 'O' OR a.[DataStatus] = 'P' or a.[DataStatus] IS NULL )  \r\n"
			+ "        ); \r\n"
			+ " ";

	private String declarePPMMPOAdd = ""
			+ " IF OBJECT_ID('tempdb..#tempPPMMPOAdd') IS NOT NULL   \r\n"
			+ "	   DROP TABLE #tempPPMMPOAdd;\r\n"
			+ "	 select distinct\r\n"
			+ "   RDD.ProductionOrder\r\n"
			+ " , a.GroupNo\r\n"
			+ " , a.SubGroup\r\n"
			+ " , 'N' as FirstLot\r\n"
			+ " , a.PlanSystemDate \r\n "
			+ " , RDD.[CustomerName]\r\n"
//			+ " , case\r\n"
//			+ "		when  cd.[CustomerName] is not null and cd.[CustomerName] <> '' then  cd.[CustomerName]\r\n"
//			+ "     else RDD.[CustomerName]\r\n"
//			+ "		end as [CustomerName]\r\n"
			+ " , RDD.MaterialNumber\r\n"
			+ " , RDD.QuantityMR \r\n"
			+ " , RDD.PurchaseOrder\r\n"
			+ " , RDD.Article\r\n"
			+ "	, 'Prod. order Created' as PPMMStatus\r\n"
			+ " , RDD.[DueDate] \r\n"
			+ " , RDD.UserStatus\r\n"
			+ " , RDD.LabStatus\r\n"
			+ " , RDD.CustomerMaterial as CustomerMat \r\n"
			+ " , cast (null as Date) as MaxLTDeliveryDate\r\n"
			+ " , cast (null as Date)  as MaxLTCFMDate \r\n"
			+ " , cast (null as Date)  as ApprovedDate \r\n"
			+ " , cast (null as varchar)  as [POLine]\r\n"
			+ " , cast (null as Date)  as TempLotCreateDate  \r\n"
			+ " , cast (null as Date)  as DocDate\r\n"
			+ " , cast (null as Date)  as SORDueDate\r\n"
			+ " , cast (null as Date)  as SORCFMDate\r\n"
			+ " , RDD.DataStatus as ReDyeStatus \r\n"
			+ " , RDD.LotNo\r\n"
			+ " , RDD.QuantityKG\r\n"
			+ " ,RDD.LabNo\r\n"
			+ " ,RDD.Shade\r\n"
			+ " ,RDD.BookNo \r\n"
			+ " ,rdd.POType\r\n"
			+ " ,rdd.PlanGreigeDate\r\n"
			+ " ,rdd.GreigeInDate\r\n"
			+ " ,rdd.GreigePlan\r\n"
//			+ "		,CASE"
//			+ "			WHEN RDD.[GreigeInDate] IS NOT NULL AND RDD.[GreigeInDate]  <> '' THEN RDD.[GreigeInDate] "
//			+ "         ELSE RDD.[PlanGreigeDate] "
//			+ "			END as GreigePlan\r\n"
			+ " ,RDD.CustomerDue\r\n"
			+ " ,RDD.DueDate AS CustomerDueShow\r\n"
			+ " ,RDD.Design\r\n"
			+ " ,RDD.Batch\r\n"
			+ " ,IPR.PlanningRemark\r\n"
			+ " ,RDD.[QtyGreigeMR]\r\n"
			+ " into #tempPPMMPOAdd\r\n"
			+ " FROM [PPMM].[dbo].[PlanLotPOAddDetail] as rdd   \r\n"
//			+ this.leftJoinPOStatusRDDCD
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON RDD.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ " left join [PPMM].[dbo].[TEMP_PlanningLot] as a on rdd.id = a.TempPOAddId\r\n"
			+ " where a.[TempPOAddId] is not null  and RDD.DataStatus = 'O' and \r\n"
			+ "		 ( a.[DataStatus] = 'O' OR a.[DataStatus] = 'P' or a.[DataStatus] IS NULL ) ; \r\n";
	private String declareFCTempProd = ""
			+ "IF OBJECT_ID('tempdb..#tempFCTempProd') IS NOT NULL \r\n"
			+ "	DROP TABLE #tempFCTempProd ;\r\n"
			+ "SELECT A.[Id]\r\n"
			+ "      , A.[POId]\r\n"
			+ "      , A.[ForecastId]\r\n"
			+ "      , A.[PlanInsteadId]\r\n"
			+ "      , A.[RuleNo]\r\n"
			+ "      , A.[ColorType]\r\n"
			+ "      , A.[ProductionOrder]\r\n"
			+ "      , A.[FirstLot]\r\n"
			+ "      , A.[ProdOrderQty]\r\n"
			+ "      , A.[GroupOptions]\r\n"
			+ "      , A.[GroupBegin]\r\n"
			+ "      , A.[PPMMStatus]\r\n"
			+ "      , A.[DataStatus]\r\n"
			+ "      , A.[ChangeDate]\r\n"
			+ "      , A.[ChangeBy]\r\n"
			+ "      , A.[CreateDate]\r\n"
			+ "      , A.[CreateBy] \r\n"
			+ "	  ,c.GroupNo,c.SubGroup,c.PlanSystemDate\r\n"
			+ "	   , CAST(  ROW_NUMBER() OVER ( PARTITION BY a.[ForecastId]  ORDER BY a.[ForecastId] , a.[ProductionOrder] )as varchar(max))  as [BatchFirst] \r\n"
			+ "  into #tempFCTempProd\r\n"
			+ "  FROM [PPMM].[dbo].[SOR_TempProd] AS A\r\n"
			+ "  INNER join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId and C.[DataStatus] = 'O' \r\n"
			+ "  WHERE PlanSystemDate IS NOT NULL AND\r\n"
			+ "        ForecastId is not null and\r\n"
			+ "        A.DataStatus = 'O'  and\r\n"
			+ "        c.[PlanSystemDate] >= GETDATE()\r\n"
			+ " ORDER BY a.[ForecastId] ,a.[ProductionOrder] \r\n"
			+ " IF OBJECT_ID('tempdb..#tempFCTempProdReal') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempFCTempProdReal ;\r\n"
			+ "  SELECT a.*\r\n"
			+ "		,BatchFirst+'/'+cast(BatchLast as varchar(max)) as Batch \r\n"
			+ "   into #tempFCTempProdReal\r\n"
			+ "  from #tempFCTempProd as a\r\n"
			+ "  inner join ( \r\n"
			+ "	  select forecastId ,max(cast( BatchFirst as int)) as BatchLast\r\n"
			+ "	  from #tempFCTempProd\r\n"
			+ "	  group by forecastId\r\n"
			+ "  ) as b on a.ForecastId = b.ForecastId;\r\n";
	private String selectVolume = ""
			+ this.declarePOMainNPOInstead
			+ this.declareTempSumPOQtyPuang
			+ this.declareTempMaxPOProd
			+ this.declareTempSumPOWOLast
			+ this.declareTempFromLotMain
			+ " IF OBJECT_ID('tempdb..#tempLotMain') IS NOT NULL   \r\n"
			+ "		DROP TABLE #tempLotMain;\r\n"
			+ " SELECT b.[Id] as POId\r\n"
			+ "	  ,a.[Id] as TempProdId \r\n"
			+ "   ,b.[PO]\r\n"
			+ "   ,b.[POLine] \r\n"
			+ "	  ,b.CustomerDue\r\n"
			+ "	  ,A.FirstLot\r\n"
			+ "	  ,a.ProductionOrder\r\n"
			+ "   ,b.[OrderQty]\r\n"
			+ "   ,b.[Unit] \r\n"
			+ "   ,b.[POPuangId]      \r\n"
			+ "	  , case\r\n"
			+ "			when SPOPD.POId is not null then 'POMain Puang'			\r\n"
			+ "			else 'PO' 			\r\n"
			+ "		end as POType \r\n"
			+ "   , case\r\n"
			+ "			when SPOPD.POId is not null then b.Id\r\n"
			+ "			else '' 			\r\n"
			+ "			end as POIdPuangMain\r\n"
			+ "   , case\r\n"
			+ "			when SPOPD.POId is not null then b.PO\r\n"
			+ "			else '' 			\r\n"
			+ "			end as POPuangMain\r\n"
			+ "   , case\r\n"
			+ "			when SPOPD.POId is not null then b.POLine\r\n"
			+ "			else '' 			\r\n"
			+ "		end as POLinePuangMain \r\n"
			+ "	  ,	case \r\n"
			+ "			when b.[Unit] = 'PC' THEN\r\n"
			+ "				case \r\n"
			+ "					when icd.[FormulaPC] > 0 then \r\n"
			+ "						case \r\n"
			+ "							when TSWOL.maxProducitonOrder is not null then \r\n"
			+ "								CASE WHEN TSPOQP.[sumPOQtyPuang] is not null THEN TSPOQP.[sumPOQtyPuang]  - (TSWOL.[sumProdQtyWOLast] / icd.[FormulaPC])\r\n"
			+ "								ELSE b.[OrderQty] - (TSWOL.[sumProdQtyWOLast] / icd.[FormulaPC]) \r\n"
			+ "								END\r\n"
			+ "							else a.[ProdOrderQtyCal] / icd.[FormulaPC] \r\n"
			+ "							end \r\n"
			+ "					else null\r\n"
			+ "				end \r\n"
			+ "			when b.[Unit] = 'KG' THEN\r\n"
			+ "				case \r\n"
			+ "					when icd.[FormulaKG] > 0 then \r\n"
			+ "						case \r\n"
			+ "							when TSWOL.maxProducitonOrder is not null then \r\n"
			+ "								CASE WHEN TSPOQP.[sumPOQtyPuang] is not null THEN TSPOQP.[sumPOQtyPuang]  - (TSWOL.[sumProdQtyWOLast] / icd.[FormulaKG ])\r\n"
			+ "                             ELSE b.[OrderQty] - (TSWOL.[sumProdQtyWOLast] / icd.[FormulaKG]) \r\n"
			+ "								END\r\n"
			+ "							else a.[ProdOrderQtyCal] / icd.[FormulaKG] \r\n"
			+ "							end \r\n"
			+ "					else null\r\n"
			+ "				end \r\n"
			+ "			when b.[Unit] = 'YD' THEN \r\n"
			+ "				case \r\n"
			+ "					when icd.[FormulaYD] > 0 then \r\n"
			+ "						case \r\n"
			+ "							when TSWOL.maxProducitonOrder is not null then \r\n"
			+ "								CASE WHEN TSPOQP.[sumPOQtyPuang] is not null THEN TSPOQP.[sumPOQtyPuang]  - (TSWOL.[sumProdQtyWOLast] / icd.[FormulaYD])\r\n"
			+ "                             ELSE b.[OrderQty] - (TSWOL.[sumProdQtyWOLast] / icd.[FormulaYD]) \r\n"
			+ "								END\r\n"
			+ "							else a.[ProdOrderQtyCal] / icd.[FormulaYD] \r\n"
			+ "							end \r\n"
			+ "					else null\r\n"
			+ "				end  \r\n"
			+ "			when b.[Unit] = 'MR' OR b.[Unit] = 'M' THEN \r\n"
			+ "				 case \r\n"
			+ "				 	when TSWOL.maxProducitonOrder is not null then \r\n"
			+ "						CASE WHEN TSPOQP.[sumPOQtyPuang] is not null THEN TSPOQP.[sumPOQtyPuang]  -  TSWOL.[sumProdQtyWOLast] \r\n"
			+ "						ELSE b.[OrderQty] -  TSWOL.[sumProdQtyWOLast]  \r\n"
			+ "						END\r\n"
			+ "				  	else a.[ProdOrderQtyCal] \r\n"
			+ "				 	end \r\n"
			+ "			else null\r\n"
			+ " END AS [VolumeFG]   \r\n"
			+ " into #tempLotMain\r\n"
			+ " FROM #tempFromLotMain as b\r\n"
			+ "	left join (\r\n"
			+ "		select a.*\r\n"
			+ "		, case\r\n"
			+ "			when SPECAL.SpecialType is null or SPECAL.Variable is null  then a.ProdOrderQty\r\n"
			+ "			when SPECAL.SpecialType = 'Divide' then a.ProdOrderQty*SPECAL.Variable\r\n"
			+ "			when SPECAL.SpecialType = 'Multiply' then a.ProdOrderQty/SPECAL.Variable\r\n"
			+ "			end as ProdOrderQtyCal\r\n"
			+ "     ,case\r\n"
			+ "			when SPECAL.SpecialType = 'Divide' then a.ProdOrderQty*SPECAL.Variable\r\n"
			+ "			when SPECAL.SpecialType = 'Multiply' then a.ProdOrderQty/SPECAL.Variable\r\n"
			+ "			else a.[ProdOrderQty]\r\n"
			+ "			end as QtyGreigeMR \r\n"
			+ "     ,SPECAL.ArticleComment\r\n"
			+ "     ,SPECAL.Variable\r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "     left join (\r\n"
			+ "	  		SELECT a.Id as POId,a.PO ,a.POLine ,c.SpecialType ,c.Variable ,b.IsCotton,c.ArticleComment\r\n"
			+ "	 		from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ "	  		left join [PPMM].[dbo].[InputArticleDetail] as b on a.Article = b.Article\r\n"
			+ "	  		left join [PPMM].[dbo].[SpecialCaseMR] as c on b.SpecialCaseId = c.Id and c.DataStatus = 'O'\r\n"
			+ "  	) as SPECAL on a.POId = SPECAL.POId\r\n"
			+ " ) as a on a.POId = b.Id \r\n"
			+ this.leftJoinSPOPDSamePOId
			+ " left join #tempSumPOWOLast as TSWOL on B.Id = TSWOL.POId and a.ProductionOrder = TSWOL.maxProducitonOrder \r\n"
			+ " left join #tempSumPOQtyPuang as TSPOQP on B.POPuangId = TSPOQP.POPuangId  \r\n"
			+ " LEFT JOIN ( SELECT [Article] ,[FormulaYD] ,[FormulaPC] ,[FormulaKG] ,[FormulaMR] \r\n"
			+ "			  FROM [PPMM].[dbo].[InputConversionDetail]\r\n"
			+ "			  where DataStatus = 'O') AS ICD ON ICD.Article = b.Article \r\n"
			+ "	where   ( (  a.POId is not null and \r\n"
			+ "				( a.[DataStatus] = 'O' or a.[DataStatus] = 'P' )\r\n"
			+ "				)\r\n"
			+ "				or a.[DataStatus] is null  ) and  \r\n"
			+ "			 ( B.[POPuangId] IS NOT NULL and a.Id is not null   ) OR (  B.POPuangId IS NULL AND SPOPD.POID IS NULL ) and\r\n"
			+ "			 a.ProductionOrder is not null and\r\n"
			+ "			( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )\r\n"
			+ this.declareTempFromLotSub
			+ "IF OBJECT_ID('tempdb..#tempLotSub') IS NOT NULL   \r\n"
			+ "    DROP TABLE #tempLotSub;\r\n"
			+ "	select distinct \r\n"
			+ "		b.[Id]  as POId\r\n"
			+ "		,a.[Id] as TempProdId\r\n"
			+ "      ,b.[PO]\r\n"
			+ "      ,b.[POLine] \r\n"
			+ "	  ,b.CustomerDue\r\n"
			+ "	  ,A.FirstLot\r\n"
			+ "	  ,a.ProductionOrder\r\n"
			+ "      ,b.[OrderQty]\r\n"
			+ "      ,b.[Unit] \r\n"
			+ "      ,b.[POPuangId]      \r\n"
			+ "		, case\r\n"
			+ "			when b.Id = b.POId then 'POMain Puang'\r\n"
			+ "			else 'POSub Puang'\r\n"
			+ "			end as POType\r\n"
			+ "     , POIdPuangMain\r\n"
			+ " 	 ,  POPuangMain \r\n"
			+ "	 ,  POLinePuangMain  \r\n"
			+ "	  ,	case \r\n"
			+ "			when b.[Unit] = 'PC' THEN\r\n"
			+ "				case \r\n"
			+ "					when icd.[FormulaPC] > 0 then \r\n"
			+ "						case \r\n"
			+ "							when TSWOL.maxProducitonOrder is not null then TSPOQP.[sumPOQtyPuang] - (TSWOL.[sumProdQtyWOLast] / icd.[FormulaPC]) \r\n"
			+ "							else a.[ProdOrderQtyCal] / icd.[FormulaPC]  \r\n"
			+ "							end \r\n"
			+ "					else null\r\n"
			+ "				end \r\n"
			+ "			when b.[Unit] = 'KG' THEN\r\n"
			+ "				case \r\n"
			+ "					when icd.[FormulaKG] > 0 then  \r\n"
			+ "						case \r\n"
			+ "							when TSWOL.maxProducitonOrder is not null then TSPOQP.[sumPOQtyPuang] - (TSWOL.[sumProdQtyWOLast] / icd.[FormulaKG])\r\n"
			+ "							else  a.[ProdOrderQtyCal] * icd.[FormulaKG]  \r\n"
			+ "							end \r\n"
			+ "					else null\r\n"
			+ "				end \r\n"
			+ "			when b.[Unit] = 'YD' THEN \r\n"
			+ "				case \r\n"
			+ "					when icd.[FormulaYD] > 0 then  \r\n"
			+ "						case \r\n"
			+ "							when TSWOL.maxProducitonOrder is not null then TSPOQP.[sumPOQtyPuang] - (TSWOL.[sumProdQtyWOLast] / icd.[FormulaYD])\r\n"
			+ " 							else a.[ProdOrderQtyCal] / icd.[FormulaYD] \r\n"
			+ "							end \r\n"
			+ "					else null\r\n"
			+ "				end  \r\n"
			+ "			when b.[Unit] = 'MR' OR b.[Unit] = 'M' THEN \r\n"
			+ "				 case \r\n"
			+ "				 	when TSWOL.maxProducitonOrder is not null then TSPOQP.[sumPOQtyPuang] - TSWOL.[sumProdQtyWOLast]  \r\n"
			+ "				  	else a.[ProdOrderQtyCal] \r\n"
			+ "				 	end \r\n"
			+ "			else null\r\n"
			+ "		END AS [VolumeFG]   \r\n"
			+ "	 into  #tempLotSub\r\n"
			+ " from #tempFromLotSub as b \r\n"
			+ "	left join (\r\n"
			+ "		select a.*\r\n"
			+ "		, case\r\n"
			+ "			when SPECAL.SpecialType is null or SPECAL.Variable is null  then a.ProdOrderQty\r\n"
			+ "			when SPECAL.SpecialType = 'Divide' then a.ProdOrderQty*SPECAL.Variable\r\n"
			+ "			when SPECAL.SpecialType = 'Multiply' then a.ProdOrderQty/SPECAL.Variable\r\n"
			+ "			end as ProdOrderQtyCal\r\n"
			+ "     ,case\r\n"
			+ "			when SPECAL.SpecialType = 'Divide' then a.ProdOrderQty*SPECAL.Variable\r\n"
			+ "			when SPECAL.SpecialType = 'Multiply' then a.ProdOrderQty/SPECAL.Variable\r\n"
			+ "			else a.[ProdOrderQty]\r\n"
			+ "			end as QtyGreigeMR\r\n"
			+ "     ,SPECAL.IsCotton\r\n"
			+ "     ,SPECAL.ArticleComment\r\n"
			+ "		from #tempPOMainNPOInstead  as a\r\n"
			+ "     left join (\r\n"
			+ "	  		SELECT a.Id as POId,a.PO ,a.POLine ,c.SpecialType ,c.Variable ,b.IsCotton\r\n"
			+ "     		,C.ArticleComment\r\n"
			+ "	 		from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ "	  		left join [PPMM].[dbo].[InputArticleDetail] as b on a.Article = b.Article\r\n"
			+ "	  		left join [PPMM].[dbo].[SpecialCaseMR] as c on b.SpecialCaseId = c.Id and c.DataStatus = 'O'\r\n"
			+ "  	) as SPECAL on a.POId = SPECAL.POId\r\n"
			+ " ) as a on a.POId = b.POId\r\n"
			+ "	left join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId\r\n"
			+ " left join [PPMM].[dbo].[InputArticleDetail] as iad on iad.Article = b.Article \r\n"
			+ " LEFT JOIN ( \r\n"
			+ "   SELECT [Article] ,[FormulaYD] ,[FormulaPC] ,[FormulaKG] ,[FormulaMR] \r\n"
			+ "	  FROM [PPMM].[dbo].[InputConversionDetail]\r\n"
			+ "	  where DataStatus = 'O'\r\n"
			+ " ) AS ICD ON ICD.Article = b.Article \r\n"
			+ " left join #tempSumPOWOLast as TSWOL on a.ProductionOrder = TSWOL.maxProducitonOrder\r\n"
			+ " left join #tempSumPOQtyPuang as TSPOQP on B.POPuangId = TSPOQP.POPuangId   \r\n"
			+ this.leftJoinSPOPDB
			+ "	where	(	(  a.POId is not null and \r\n"
			+ "				( a.[DataStatus] = 'O' or a.[DataStatus] = 'P' )\r\n"
			+ "				)\r\n"
			+ "				or a.[DataStatus] is null  ) and \r\n"
			+ "			( B.[DataStatus] = 'O' ) AND\r\n"
			+ "			( C.[DataStatus] = 'O' or C.[DataStatus] = 'P' OR C.[DataStatus] IS NULL ) and\r\n"
			+ "         ( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )\r\n"
			+ " IF OBJECT_ID('tempdb..#tempLot') IS NOT NULL   \r\n"
			+ "		DROP TABLE #tempLot;\r\n"
			+ " select *  \r\n"
			+ " FROM (\r\n"
			+ "	 select * from #tempLotMain \r\n"
			+ "	 union\r\n"
			+ "	 select * from  #tempLotSub\r\n"
			+ " ) AS A    \r\n";
	private String declareTempFCDate = ""
			+ " IF OBJECT_ID('tempdb..#tempFCDate') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempFCDate; \r\n"
			+ "	select \r\n"
			+ "  a.[Id]\r\n"
			+ " ,a.[DocDate]\r\n"
			+ " ,a.[CustomerNo]\r\n"
			+ " ,a.[ForecastNo]\r\n"
			+ " ,a.[ForecastMY]\r\n"
			+ " ,a.[TotalForecastQty]\r\n"
			+ " ,a.[ForecastBLQty]\r\n"
			+ " ,a.[ForecastNonBLQty]\r\n"
			+ " ,a.[Unit]\r\n"
			+ " ,a.[RuleNo]\r\n"
			+ " ,a.[DataStatus]\r\n"
			+ " ,a.[LastCFMDate]\r\n"
			+ " ,a.[ChangeDate]\r\n"
			+ " ,a.[ChangeBy]\r\n"
			+ " ,a.[CreateBy]\r\n"
			+ " ,a.[CreatedDate] , case\r\n"
			+ "		when  b.[CustomerName] is not null and b.[CustomerName] <> '' then  b.[CustomerName]\r\n"
			+ "    	else a.[CustomerName]\r\n"
			+ "		end as [CustomerName]  \r\n"
			+ " ,convert(date ,right(a.ForecastMY,4) + left(a.ForecastMY,2) + '01') AS ForecastDate \r\n"
			+ " ,convert(date ,right(a.ForecastMY,4) + left(a.ForecastMY,2) + '01') AS ForSortDueAndCustDue \r\n"
			+ " ,CASE \r\n"
			+ "	    WHEN DistChannel = 'EX' \r\n"
			+ "		THEN DATEADD(\r\n"
			+ "			MONTH,-3,convert(\r\n"
			+ "				date ,right(a.ForecastMY,4) + \r\n"
			+ "				left(a.ForecastMY,2) + \r\n"
			+ "				CAST(\r\n"
			+ "					(SELECT TOP 1 [DayOfMonth]+1  FROM [PPMM].[dbo].[StockDefaultDate] WHERE DataStatus = 'O') AS VARCHAR(2))\r\n"
			+ "			)\r\n"
			+ "		)\r\n"
			+ "	ELSE \r\n"
			+ "		DATEADD(\r\n"
			+ "			MONTH,-2,convert(\r\n"
			+ "				date ,right(a.ForecastMY,4) + \r\n"
			+ "				left(a.ForecastMY,2) + \r\n"
			+ "				CAST(\r\n"
			+ "					(SELECT TOP 1 [DayOfMonth]+1  FROM [PPMM].[dbo].[StockDefaultDate] WHERE DataStatus = 'O') AS VARCHAR(2))\r\n"
			+ "			)\r\n"
			+ "		)\r\n"
			+ "	END AS ForecastDateMonthBefore \r\n"
			+ ",CASE \r\n"
			+ "		WHEN DistChannel = 'EX' \r\n"
			+ "		THEN DATEADD(\r\n"
			+ "			MONTH,-2,convert(\r\n"
			+ "				date ,right(a.ForecastMY,4) + \r\n"
			+ "				left(a.ForecastMY,2) + \r\n"
			+ "				CAST(\r\n"
			+ "					(SELECT TOP 1 [DayOfMonth]  FROM [PPMM].[dbo].[StockDefaultDate] WHERE DataStatus = 'O') AS VARCHAR(2))\r\n"
			+ "			)\r\n"
			+ "		)\r\n"
			+ "		ELSE \r\n"
			+ "			DATEADD(\r\n"
			+ "			MONTH,-1,convert(\r\n"
			+ "				date ,right(a.ForecastMY,4) + \r\n"
			+ "				left(a.ForecastMY,2) + \r\n"
			+ "				CAST(\r\n"
			+ "					(SELECT TOP 1 [DayOfMonth]  FROM [PPMM].[dbo].[StockDefaultDate] WHERE DataStatus = 'O') AS VARCHAR(2))\r\n"
			+ "			)\r\n"
			+ "		)\r\n"
			+ "	END AS ForecastDateMonthLast \r\n"
			+ " ,[RemainNonBLQty]\r\n"
			+ " ,[RemainBLQty]\r\n"
			+ " into #tempFCDate \r\n"
			+ " from [PPMM].[dbo].[SOR_ForecastDetail] AS A \r\n"
			+ " INNER JOIN (\r\n"
			+ "	 	SELECT SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
			+ "	 	 ,[CustomerShortName] as [CustomerName]\r\n"
			+ "		 ,[ChangeDate]\r\n"
			+ "		 ,[CreateDate]\r\n"
			+ "      , DistChannel\r\n"
			+ "	   , CustomerType\r\n"
//			+ "	   ,  isSabina\r\n"
			+ "      , case\r\n"
			+ "          when [CustomerName] not like '%SABINA%' and [CustomerShortName] not like '%SBF%'\r\n"
			+ "          then 0 \r\n"
			+ "			 else 1 \r\n"
			+ "        end as isSabina\r\n"
//			+ "      , case\r\n"
//			+ "          when [CustomerName] not like '%SABINA%' and [CustomerShortName] not like '%SBF%'\r\n"
//			+ "          then 0 \r\n"
//			+ "			 else 1 \r\n"
//			+ "        end as isSabina\r\n"
			+ "	 FROM [PCMS].[dbo].[CustomerDetail] \r\n"
			+ " ) AS B ON A.CustomerNo = b.CustomerNo\r\n"
			+ " where DataStatus = 'O'\r\n";

	public ReportDaoImpl(Database database2, String conType) {
		this.database = database2;
		this.conType = conType;
		this.message = "";
	}

	@Override
	public ArrayList<RecreateRedyeReportDetail> bookingProdOrder(ArrayList<RecreateRedyeReportDetail> poList) {
		ArrayList<RecreateRedyeReportDetail> tmpList = new ArrayList<>();
		PlanningProdModel ppModel = new PlanningProdModel(this.conType);
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		RecreateRedyeReportDetail bean = poList.get(0);
		String prodOrder = bean.getProductionOrder();
		RecreateRedyeReportDetail rBean = new RecreateRedyeReportDetail();
		rBean.setProductionOrder(bean.getProductionOrder());
		rBean.setDueDate("");
		rBean.setMaterialNumber("");

		rBean.setCustomerMat("");
		tmpList.add(rBean);

		ArrayList<RecreateRedyeReportDetail> checkList = this.getRecreateRedyeReportDetail(tmpList); 
		if ( ! checkList.isEmpty()) {
			String prodBook = checkList.get(0).getProdOrderBook(); 
			if ( ! prodBook.equals("")) {

			} else {
				String keyWord = prodOrder.substring(0, 1);

				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, 0);
//				cal.add(Calendar.MONTH, 1);
				cal.setTime(cal.getTime());
				Date nextMonth = cal.getTime();
				String nextMonthStr = sdf2.format(nextMonth);
				String[] array = nextMonthStr.split(Config.C_SLASH);
				String year = array[2]; 
				String secondPos = year.substring(3);
				String month = array[1];
//				month =   String.format("%02d",month );
				String threePos = ppModel.getThirdPosLot(month);
				keyWord += secondPos+threePos;
				ArrayList<ProdOrderRunningDetail> prodRunningList = porModel.getProdOrderRunningDetail(keyWord);
				if (prodRunningList.isEmpty()) {
					bgjModel.execUpsertToProdOrderRunning(keyWord);
					prodRunningList = porModel.getProdOrderRunningDetail(keyWord);
				} 
				if ( ! prodRunningList.isEmpty()) {
					prodBook = prodRunningList.get(0).getProductionOrder(); 
					ArrayList<ProdOrderRunningDetail> porList = new ArrayList<>();
					for (int i = 0; i < poList.size(); i ++ ) {
						RecreateRedyeReportDetail beanTemp = poList.get(i);
						ProdOrderRunningDetail beanPOR = new ProdOrderRunningDetail();
						beanPOR.setDataStatus(Config.C_CLOSE_STATUS);
						beanPOR.setRemark("BookedfromPPMM2");
						beanPOR.setChangeBy(beanTemp.getChangeBy());
						beanPOR.setProductionOrder(prodBook);
						beanPOR.setProductionOrderTemp(beanTemp.getProductionOrder());
						porList.add(beanPOR);
					}
					porModel.updateProdOrderRunningWithProductionOrder(porList);
				}
				checkList.get(0).setProdOrderBook(prodBook);
			}
		} else {
			checkList = poList;
		}
		return checkList;
	}

	@Override
	public ArrayList<CustomerMonthlyReportDetail> getCustomerMonthlyReportDetail(
			ArrayList<InputFacWorkDateDetail> poList) {
		ArrayList<CustomerMonthlyReportDetail> list = null;
		InputFacWorkDateDetail bean = poList.get(0);
		String monthYear = bean.getColMonthYear();
		String firstDay = "1/" + monthYear;
		bean.getTotalDay();
		String declarePlan = ""
				+ " declare @BeginDate date  = CONVERT(date, '"
				+ firstDay
				+ "', 103) ;\r\n"
				+ " declare @LastDate date  = EOMONTH(@BeginDate) ; \r\n"
				+ " declare @NextMonthBeginDate date  = dateadd(month,datediff(month,0,dateadd(month,1,@BeginDate)),0)   ; \r\n"
				+ " declare @NextMonthLastDate date  = EOMONTH(@NextMonthBeginDate) ;  \r\n";
		String sql = ""
				+ this.declareTempFCDate
				+ declarePlan
				+ " IF OBJECT_ID('tempdb..#tempMainCustomer') IS NOT NULL \r\n"
				+ "	 DROP TABLE #tempMainCustomer;   \r\n"
				+ "	 select A.Id,A.PO,A.POLine,a.CustomerNo,a.CustomerDue,a.OrderQty,A.Unit \r\n"
				+ "			,b.[CustomerShortName] as [CustomerNameEng]\r\n"
				+ "     ,case\r\n"
				+ "			when IAD.[ArticleReplaced] is not null and IAD.ArticleReplaced <> '' then IAD.[ArticleReplaced]\r\n"
				+ "       	else a.Article\r\n"
				+ "       	end as Article\r\n"
				+ "		into #tempMainCustomer\r\n"
				+ "		from [PPMM].[dbo].[SOR_PODetail]  as a\r\n"
				+ "		  inner join (\r\n"
				+ "			  SELECT SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
				+ "				  ,[CustomerName]\r\n"
				+ "				  ,[CustomerShortName]\r\n" 
				+ "			  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
				+ "			  where CustomerShortName <> ''  \r\n"
				+ "		  )  as b on a.CustomerNo = b.CustomerNo\r\n"
				+ this.leftJoinIAD
//				+ "		 left join [PPMM].[dbo].[SpecialCaseMR] as SCMR on a.Article = scmr.Article\r\n"
				+ "		 WHERE a.PO NOT LIKE '%SALEMAN%'  \r\n"
				+ "		and   ( @BeginDate <= a.CustomerDue and a.CustomerDue  <= @LastDate ) or\r\n"
				+ "			  ( @NextMonthBeginDate <= a.CustomerDue and  a.CustomerDue <= @NextMonthLastDate  )\r\n"
				+ " IF OBJECT_ID('tempdb..#tempMainCustomerPO') IS NOT NULL \r\n"
				+ "	 DROP TABLE #tempMainCustomerPO;; \r\n"
				+ "	 select a.* \r\n"
				+ "		INTO #tempMainCustomerPO\r\n"
				+ "	 from (\r\n"
				+ "	 SELECT \r\n"
				+ "		  case \r\n"
				+ "		  when CustomerNameEng like '%wacoal%' then '5TWC' \r\n"
				+ "		  else '9OTH'\r\n"
				+ "		  end  AS  POType\r\n"
				+ "		, a.PO \r\n"
				+ "		 ,a.POLine\r\n"
				+ "		 ,a.[CustomerNo] \r\n"
				+ "		 ,a.[CustomerNameEng]\r\n"
				+ "		 ,a.CustomerDue  \r\n"
				+ "		 ,case	\r\n"
				+ "			when A.[Unit] = 'PC' THEN\r\n"
				+ "				case  \r\n"
				+ "					when b.[FormulaPC] > 0 then  a.[OrderQty] * [FormulaPC] \r\n"
				+ "					else null\r\n"
				+ "				end \r\n"
				+ "			when a.[Unit] = 'KG' THEN\r\n"
				+ "				case \r\n"
				+ "					when b.[FormulaKG] > 0 then  a.[OrderQty] * [FormulaKG] \r\n"
				+ "					else null\r\n"
				+ "				end \r\n"
				+ "			when a.[Unit] = 'YD' THEN \r\n"
				+ "				case \r\n"
				+ "					when b.[FormulaYD] > 0 then  a.[OrderQty] * [FormulaYD] \r\n"
				+ "					else null\r\n"
				+ "				end  \r\n"
				+ "			when a.[Unit] = 'MR' OR a.[Unit] = 'M' THEN a.[OrderQty]\r\n"
				+ "			else null\r\n"
				+ "		END AS OrderQtyM   \r\n"
				+ "		, c.PurchaseOrder  as POCreated\r\n"
				+ "		, cast( dateadd(month,datediff(month,0,a.CustomerDue),0) as date)  as InDueDate\r\n"
				+ "	  FROM #tempMainCustomer as a \r\n"
				+ "	  LEFT JOIN ( SELECT [Article] ,[FormulaYD] ,[FormulaPC] ,[FormulaKG] ,[FormulaMR] \r\n"
				+ " 				  FROM [PPMM].[dbo].[InputConversionDetail] \r\n"
				+ "   				  where DataStatus = 'O') AS B ON B.Article = A.Article \r\n"
				+ "	  LEFT JOIN [PCMS].[dbo].[FromSapMainSale] AS c ON A.PO = c.PurchaseOrder  \r\n"
				+ "	   WHERE a.PO NOT LIKE '%SALEMAN%'   and  c.PurchaseOrder  is null\r\n"
				+ "	) as a\r\n"
				+ "	where  ( @BeginDate <= a.InDueDate and @LastDate >= a.InDueDate ) or\r\n"
				+ "		   ( @NextMonthBeginDate <= a.InDueDate and @NextMonthLastDate >= a.InDueDate )\r\n"
				+ " IF OBJECT_ID('tempdb..#tempMainCustomerSaleSap') IS NOT NULL \r\n"
				+ "	 DROP TABLE #tempMainCustomerSaleSap; \r\n"
				+ "\r\n"
				+ "	 select a.* \r\n"
				+ "	INTO #tempMainCustomerSaleSap\r\n"
				+ "	 from (\r\n"
				+ "		 SELECT \r\n"
				+ "			  case \r\n"
				+ "			  when a.[CustomerName] like '%wacoal%' then '5TWC' \r\n"
				+ "			  else '9OTH'\r\n"
				+ "			  end  AS  POType \r\n"
				+ "			 , a.PurchaseOrder AS PO \r\n"
				+ "			 ,'' as POLine\r\n"
				+ "			 ,SUBSTRING(a.[CustomerNo] , PATINDEX('%[^0]%', a.[CustomerNo] +'.'), LEN(a.[CustomerNo] )) AS [CustomerNo] \r\n"
				+ "			 ,a.CustomerShortName  as [CustomerNameEng] \r\n"
				+ "			 ,convert(date,a.[DueDate], 103) as CustomerDue    \r\n"
				+ "			 ,case	\r\n"
				+ "				when A.[SaleUnit] = 'PC' THEN\r\n"
				+ "					case  \r\n"
				+ "						when b.[FormulaPC] > 0 then  a.[SaleQuantity] * [FormulaPC] \r\n"
				+ "						else null\r\n"
				+ "					end \r\n"
				+ "				when a.[SaleUnit] = 'KG' THEN\r\n"
				+ "					case \r\n"
				+ "						when b.[FormulaKG] > 0 then  a.[SaleQuantity] * [FormulaKG] \r\n"
				+ "						else null\r\n"
				+ "					end \r\n"
				+ "				when a.[SaleUnit] = 'YD' THEN \r\n"
				+ "					case \r\n"
				+ "						when b.[FormulaYD] > 0 then  a.[SaleQuantity] * [FormulaYD] \r\n"
				+ "						else null\r\n"
				+ "					end  \r\n"
				+ "				when a.[SaleUnit] = 'MR' OR a.[SaleUnit] = 'M' THEN a.[SaleQuantity]\r\n"
				+ "				else null\r\n"
				+ "			END AS OrderQtyM   \r\n"
				+ "			, a.PurchaseOrder  as POCreated\r\n"
				+ "			, cast( dateadd(month,datediff(month,0,convert(date,a.[DueDate], 103) ),0) as date)  as InDueDate \r\n"
				+ "		  FROM ( select b.[CustomerName], b.[CustomerShortName],[SaleQuantity],[SaleUnit], DueDate,PurchaseOrder,a.CustomerNo \r\n"
				+ "     	,case\r\n"
				+ "       		when IAD.[ArticleReplaced] is not null and IAD.ArticleReplaced <> '' then IAD.[ArticleReplaced]\r\n"
				+ "       		else a.ArticleFG\r\n"
				+ "       	 end as Article\r\n"
				+ "				from [PCMS].[dbo].[FromSapMainSale] as a\r\n"
				+ "             left join [PPMM].[dbo].[InputArticleDetail] as IAD on a.[ArticleFG] = IAD.[Article]\r\n"
//				+ "				left join [PPMM].[dbo].[SpecialCaseMR] as scmr on a.ArticleFG = scmr.Article\r\n"
				+ "				left join (\r\n"
				+ "					  SELECT  [CustomerNo]  as [CustomerNo]\r\n"
				+ "						  ,[CustomerName]\r\n"
				+ "						  ,[CustomerShortName]\r\n" 
				+ "					  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
				+ "					  where CustomerShortName <> ''  \r\n"
				+ "				  )  as b on a.CustomerNo = b.CustomerNo\r\n"
				+ "				where a.PurchaseOrder not like '%SALEMAN%'\r\n"
				+ "			) as a \r\n"
				+ "		  LEFT JOIN ( \r\n"
				+ "				SELECT [Article] ,[FormulaYD] ,[FormulaPC] ,[FormulaKG] ,[FormulaMR] \r\n"
				+ " 			FROM [PPMM].[dbo].[InputConversionDetail] \r\n"
				+ "   			where DataStatus = 'O'\r\n"
				+ "			) AS B ON B.Article = A.Article \r\n"
				+ "	) as a\r\n"
				+ "	where  ( @BeginDate <= a.InDueDate and @LastDate >= a.InDueDate ) or\r\n"
				+ "		   ( @NextMonthBeginDate <= a.InDueDate and @NextMonthLastDate >= a.InDueDate ) \r\n"
//				+ "   and OrderQtyM is not null\r\n"
				+ "	--WHERE a.PurchaseOrder NOT LIKE '%SALEMAN%'   \r\n"
				+ "IF OBJECT_ID('tempdb..#tempMainCustomerFC') IS NOT NULL \r\n"
				+ "	DROP TABLE #tempMainCustomerFC; \r\n"
				+ "select * \r\n"
				+ "  into #tempMainCustomerFC\r\n"
				+ "from ( \r\n"
				+ "		SELECT \r\n"
				+ "		  case \r\n"
				+ "		  when b.[CustomerName] like '%wacoal%' then '5TWC' \r\n"
				+ "		  else '9OTH'\r\n"
				+ "		  end  AS  POType\r\n"
				+ "	  ,CAST( dateadd(month,datediff(month,0,convert(date,  '01.'+[ForecastMY], 104)),0)   \r\n"
				+ "	  AS DATE) AS  InDueDate \r\n"
				+ "		  ,a.[CustomerNo]\r\n"
				+ "		  ,b.[CustomerShortName] as [CustomerNameEng] \r\n"
				+ "      ,isnull([RemainNonBLQty],0)+isnull([RemainBLQty],0) as SumTotal\r\n"
				+ "	  ,0 as SumOrder\r\n"
				+ "	  ,0 as SumPlan\r\n"
				+ "      ,isnull([RemainNonBLQty],0)+isnull([RemainBLQty],0) as SumForecast \r\n"
				+ "  FROM [PPMM].[dbo].[SOR_ForecastDetail] as a\r\n"
				+ "	inner join (\r\n"
				+ "		SELECT SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
				+ "			,[CustomerName]\r\n"
				+ "			,[CustomerShortName]\r\n" 
				+ "		FROM [PCMS].[dbo].[CustomerDetail]\r\n"
				+ "		where CustomerShortName <> ''  \r\n"
				+ "	)  as b on a.CustomerNo = b.CustomerNo\r\n"
				+ ") as a\r\n"
				+ "where  ( @BeginDate <= a.InDueDate and @LastDate >= a.InDueDate ) or\r\n"
				+ "	   ( @NextMonthBeginDate <= a.InDueDate and @NextMonthLastDate >= a.InDueDate )\r\n"
				+ "	   \r\n"
				+ "	    \r\n"
				+ "\r\n"
				+ "IF OBJECT_ID('tempdb..#tempBaseCustomer') IS NOT NULL \r\n"
				+ "	DROP TABLE #tempBaseCustomer; \r\n"
				+ "\r\n"
				+ "SELECT *\r\n"
				+ "into #tempBaseCustomer\r\n"
				+ "FROM (\r\n"
				+ "	SELECT distinct POType,CustomerNo,CustomerNameEng as CustomerName,InDueDate\r\n"
				+ "	FROM #tempMainCustomerSaleSap\r\n"
				+ "	UNION\r\n"
				+ "	SELECT distinct POType,CustomerNo,CustomerNameEng as CustomerName  ,InDueDate\r\n"
				+ "	FROM #tempMainCustomerPO\r\n"
				+ "	UNION\r\n"
				+ "	SELECT distinct POType,CustomerNo,CustomerNameEng as CustomerName,InDueDate\r\n"
				+ "	FROM #tempMainCustomerFC\r\n"
				+ ") AS A \r\n"
				+ "\r\n"
				+ "IF OBJECT_ID('tempdb..#tempBaseCustomerAll') IS NOT NULL \r\n"
				+ "	DROP TABLE #tempBaseCustomerAll; \r\n"
				+ "SELECT A.*  ,SumOrder+SumPlan+SumForecast AS SumTotal\r\n"
				+ "into #tempBaseCustomerAll\r\n"
				+ "FROM (\r\n"
				+ "	select A.*  ,ISNULL(SumOrder,0) AS SumOrder,ISNULL(SumPlan,0) AS SumPlan,ISNULL(SumForecast ,0) AS SumForecast\r\n"
				+ "	from #tempBaseCustomer AS A\r\n"
				+ "	left join (\r\n"
				+ "		SELECT CUSTOMERNO,InDueDate ,SUM(OrderQtyM) AS SumOrder \r\n"
				+ "		FROM #tempMainCustomerSaleSap\r\n"
				+ "		group by CUSTOMERNO,InDueDate\r\n"
				+ "	) as B ON A.CustomerNo = B.CustomerNo AND A.InDueDate = B.InDueDate\r\n"
				+ "	left join (\r\n"
				+ "		SELECT  CUSTOMERNO,InDueDate ,SUM(OrderQtyM) AS SumPlan\r\n"
				+ "		FROM #tempMainCustomerPO\r\n"
				+ "		group by CUSTOMERNO,InDueDate\r\n"
				+ "	) as C ON A.CustomerNo = C.CustomerNo AND A.InDueDate = C.InDueDate\r\n"
				+ "	left join (\r\n"
				+ "		SELECT  CustomerNo,InDueDate ,SumForecast\r\n"
				+ "		FROM #tempMainCustomerFC\r\n"
				+ "	) as D ON A.CustomerNo = D.CustomerNo AND A.InDueDate = D.InDueDate\r\n"
				+ ") AS A \r\n"
				+ "ORDER BY A.InDueDate ,POType\r\n"
				+ "\r\n"
				+ "IF OBJECT_ID('tempdb..#tempBaseCustomerFirstMonth') IS NOT NULL \r\n"
				+ "   DROP TABLE #tempBaseCustomerFirstMonth;\r\n"
				+ "select POType, CustomerNo , InDueDate , SumTotal,SumOrder,SumPlan,SumForecast\r\n"
				+ "into #tempBaseCustomerFirstMonth\r\n"
				+ "from #tempBaseCustomerAll as a\r\n"
				+ "where ( @BeginDate <= a.InDueDate and @LastDate >= a.InDueDate )   \r\n"
				+ "	\r\n"
				+ "IF OBJECT_ID('tempdb..#tempBaseCustomerSecondMonth') IS NOT NULL \r\n"
				+ "   DROP TABLE #tempBaseCustomerSecondMonth;\r\n"
				+ "select POType,CustomerNo , InDueDate      ,isnull(SumTotal,0) as NextSumTotal,isnull(SumOrder,0) as NextSumOrder      ,isnull(SumPlan,0)  as NextSumPlan,isnull(SumForecast,0) as NextSumForecast 	\r\n"
				+ "into #tempBaseCustomerSecondMonth\r\n"
				+ "from #tempBaseCustomerAll as a\r\n"
				+ "where ( @NextMonthBeginDate <= a.InDueDate and @NextMonthLastDate >= a.InDueDate ) \r\n"
				+ "--select * from #tempBaseCustomerFirstMonth\r\n"
				+ "--select * from #tempBaseCustomerSecondMonth\r\n"
				+ "SELECT *\r\n"
				+ "FROM (\r\n"
				+ " SELECT '0GRA' AS POType,'' as CustomerNo,'Grand Total' as CustomerName\r\n"
				+ ",( SELECT Sum(SumOrder)     from #tempBaseCustomerFirstMonth )AS SumOrder\r\n"
				+ ",( SELECT Sum(SumPlan)      from #tempBaseCustomerFirstMonth )AS SumPlan\r\n"
				+ ",( SELECT Sum(SumForecast)  from #tempBaseCustomerFirstMonth )AS SumForecast\r\n"
				+ ",( SELECT Sum(SumTotal)     from #tempBaseCustomerFirstMonth )AS SumTotal \r\n"
				+ ",( SELECT Sum(NextSumOrder) from #tempBaseCustomerSecondMonth )AS NextSumOrder\r\n"
				+ ",( SELECT Sum(NextSumPlan)  from #tempBaseCustomerSecondMonth )AS NextSumPlan\r\n"
				+ ",( SELECT Sum(NextSumForecast) from #tempBaseCustomerSecondMonth )AS NextSumForecast\r\n"
				+ ",( SELECT Sum(NextSumTotal) from #tempBaseCustomerSecondMonth )AS NextSumTotal \r\n"
				+ "UNION\r\n"
				+ "SELECT '4TWC' AS POType,'' as CustomerNo,'TWC Total' as CustomerName\r\n"
				+ ",( SELECT Sum(SumOrder)     from #tempBaseCustomerFirstMonth where POType = '5TWC')AS SumOrder\r\n"
				+ ",( SELECT Sum(SumPlan)      from #tempBaseCustomerFirstMonth where POType = '5TWC')AS SumPlan\r\n"
				+ ",( SELECT Sum(SumForecast)  from #tempBaseCustomerFirstMonth where POType = '5TWC')AS SumForecast\r\n"
				+ ",( SELECT Sum(SumTotal)     from #tempBaseCustomerFirstMonth where POType = '5TWC')AS SumTotal \r\n"
				+ ",( SELECT Sum(NextSumOrder) from #tempBaseCustomerSecondMonth where POType = '5TWC')AS NextSumOrder\r\n"
				+ ",( SELECT Sum(NextSumPlan)  from #tempBaseCustomerSecondMonth where POType = '5TWC')AS NextSumPlan\r\n"
				+ ",( SELECT Sum(NextSumForecast) from #tempBaseCustomerSecondMonth where POType = '5TWC')AS NextSumForecast\r\n"
				+ ",( SELECT Sum(NextSumTotal) from #tempBaseCustomerSecondMonth where POType = '5TWC')AS NextSumTotal \r\n"
				+ "UNION\r\n"
				+ "SELECT '8OTH' AS POType,'' as CustomerNo,'Other Total' as CustomerName\r\n"
				+ ",( SELECT Sum(SumOrder)     from #tempBaseCustomerFirstMonth where POType = '9OTH')AS SumOrder\r\n"
				+ ",( SELECT Sum(SumPlan)      from #tempBaseCustomerFirstMonth where POType = '9OTH')AS SumPlan\r\n"
				+ ",( SELECT Sum(SumForecast)  from #tempBaseCustomerFirstMonth where POType = '9OTH')AS SumForecast\r\n"
				+ ",( SELECT Sum(SumTotal)     from #tempBaseCustomerFirstMonth where POType = '9OTH')AS SumTotal \r\n"
				+ ",( SELECT Sum(NextSumOrder) from #tempBaseCustomerSecondMonth where POType = '9OTH')AS NextSumOrder\r\n"
				+ ",( SELECT Sum(NextSumPlan)  from #tempBaseCustomerSecondMonth where POType = '9OTH')AS NextSumPlan\r\n"
				+ ",( SELECT Sum(NextSumForecast) from #tempBaseCustomerSecondMonth where POType = '9OTH')AS NextSumForecast\r\n"
				+ ",( SELECT Sum(NextSumTotal) from #tempBaseCustomerSecondMonth where POType = '9OTH')AS NextSumTotal  \r\n"
				+ "UNION \r\n"
				+ " SELECT distinct \r\n"
				+ " A.POType,A.CustomerNo,CustomerName\r\n"
				+ " ,SUB_A.SumOrder,SUB_A.SumPlan,SUB_A.SumForecast,SUB_A.SumTotal\r\n"
				+ " ,SUB_B.NextSumOrder,SUB_B.NextSumPlan ,SUB_B.NextSumForecast,SUB_B.NextSumTotal \r\n"
				+ " from #tempBaseCustomerAll AS A\r\n"
				+ " LEFT JOIN  #tempBaseCustomerFirstMonth AS SUB_A ON SUB_A.POType = A.POType AND SUB_A.CustomerNo = A.CustomerNo\r\n"
				+ " LEFT JOIN  #tempBaseCustomerSecondMonth AS SUB_B ON SUB_B.POType = A.POType AND SUB_B.CustomerNo = A.CustomerNo  \r\n"
				+ " ) as a \r\n"
				+ " ORDER BY POType,CustomerName\r\n"
				+ " \r\n"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCustomerMonthlyReportDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PlanningReportDetail> getDetailForVolumeDetail() {
		ArrayList<PlanningReportDetail> list = null;
		String sql = ""
				+ this.selectVolume
				+ "  where ( POType LIKE '%PO%' AND POType <> 'POADD' ) AND \r\n"
				+ "         VolumeFG is not null AND\r\n "
				+ "         LEFT(a.ProductionOrder, 1) LIKE '[0-9]' \r\n" 
//				+ "         ISNUMERIC(LEFT(ProductionOrder, 1)) = 1 \r\n"
				+ "  Order By  a.POIdPuangMain ,POType,CustomerDue, a.POId,a.ProductionOrder   "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPlanningReportDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PlanningReportDetail> getDetailForVolumeDetailByPOId(ArrayList<POManagementDetail> poList) {
		ArrayList<PlanningReportDetail> list = null;
		String whereTmp = " ( ";
		for (int i = 0; i < poList.size(); i ++ ) {
			int poId = poList.get(i).getPoId();
			if (poId != 0) {
				whereTmp += " a.[POId] = " + poId + " ";
				if (i < poList.size()
						-1) {
					whereTmp += " or ";
				}
			}
		}
		whereTmp += " ) \r\n";
		String sql = ""
				+ this.selectVolume
				+ "  where POType LIKE '%PO%' AND POType <> 'POADD'  and\r\n"
				+ whereTmp
				+ "  Order By a.POIdPuangMain ,POType,CustomerDue, a.POId,a.ProductionOrder  ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPlanningReportDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<ForecastStatusReportDetail> getFCStatusReportDetail(ArrayList<ForecastStatusReportDetail> poList) {
		ArrayList<ForecastStatusReportDetail> list = null;
		ForecastStatusReportDetail bean = poList.get(0);
		String fc = bean.getForecastNo();
//		String poDate = bean.getApprovedDate();
		String fcDate = bean.getForecastDate();
		bean.getProductionOrder();

		String[] array;

//		+ " declare @BeginDate date  = convert(date, '"+planStart+"', 103) ;\r\n"
		String where = "";
		if ( ! fc.equals("")) {
			where += " [ForecastNo] like '" + fc + "%' ";
		}
		if ( ! fcDate.equals("")) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			array = fcDate.split(" - ");
			where += " ( [DocDate] >= convert(date,'"
					+ array[0]
					+ "', 103) AND [DocDate] <= convert(date,'"
					+ array[1]
					+ "', 103) ) \r\n";
		}
		if ( ! where.equals("")) {
			where = " where " + where;
		}
		String sql = ""
				+ " IF OBJECT_ID('tempdb..#tempFromFCData') IS NOT NULL   \r\n"
				+ "    DROP TABLE #tempFromFCData;\r\n"
				+ " select *\r\n"
				+ " into #tempFromFCData\r\n"
				+ " from ( SELECT \r\n"
				+ this.selectFCStatus
				+ this.fromFCCus
				+ " ) as a\r\n "
				+ where
				+ "  select * \r\n"
				+ "	,  (SELECT COUNT(ForecastNo)\r\n"
				+ "		FROM #tempFromFCData as SUB_A\r\n"
				+ "		) AS TotalFC \r\n"
				+ " from #tempFromFCData as a\r\n"
//				+ "  Order By a.CustomerDue , a.PO , a.POLine ,A.FirstLot,a.ProductionOrder  " ;
				+ " Order By a.ForecastMY, a.ForecastNo ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genForecastStatusReportDetail(map));
		}
		return list;
	}

	public int getLastDayOfMonth(String dateString) {
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_PATTERN);
		YearMonth yearMonth = YearMonth.parse(dateString, pattern);
		LocalDate date = yearMonth.atEndOfMonth();
		return date.lengthOfMonth();
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<MonthlyCapReportDetail> getMonthlyCapReportDetail(ArrayList<InputDateRunningDetail> poList) {
		ArrayList<MonthlyCapReportDetail> list = null;
		InputDateRunningDetail bean = poList.get(0);
		String MMyyyy = bean.getDate();
		String where = "";
		if ( ! where.equals("")) {
			where = " where " + where;
		}
		String begin = "01/" + MMyyyy;
		String declarePlan = ""
				+ " declare @BeginDateFirst date  = convert(date, '"
				+ begin
				+ "', 103) ;\r\n"
				+ " declare @today date  = cast(GETDATE() as date) ;\r\n";
		String sql = "" + declarePlan 
				+ this.declareTempFCDate 
				+ this.declareMonthly 
				+ this.declareMonthlyTempWordDate
//				+ this.declarePOMainNPOInstead
				+ this.declarePOMainNPOInsteadMonthly
				+ "SELECT DISTINCT \r\n"
				+ this.selectMCRFirst  
				+ "  FROM [PPMM].[dbo].[InputMainGroupDetail] as a\r\n"
				+ "  inner join [PPMM].[dbo].[InputSubGroupDeail] as b on a.[GroupNo] = b.[GroupNo] \r\n"
				+ "  left join (\r\n"
				+ "     SELECT a.[GroupNo] ,a.[SubGroup] \r\n"
				+ "			  , count(RowNum) as totalWorkDate\r\n"
				+ "		FROM #tempWorkDate   as a\r\n"
				+ "		where @BeginFirstMonth <= WorkDate and WorkDate <= @LastFirstMonth and WorkDate >= @today  \r\n"
				+ "		group by a.[GroupNo]  ,a.[SubGroup]\r\n"
				+ " ) as c on a.GroupNo = c.GroupNo and b.SubGroup = c.SubGroup \r\n"
				+ this.leftJoinTotalPODFirst
				+ this.leftJoinTotalRedyeFirst
				+ this.leftJoinTotalForecastFFirst
				+ "  where a.DataStatus = 'O' AND B.[DataStatus] = 'O'\r\n"
				+ "  ORDER BY A.GroupNo , B.SubGroup "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genMonthlyCapReportDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PlanningReportDetail> getPlanningReportDetail(ArrayList<InputPlanningReportDetail> poList) {
		ArrayList<PlanningReportDetail> list = null;
		String wherePO = "";
		String whereFC = "";
		String whereReDye = "";
		String dyeDate = "";
		String docDate = "";
		String prodOrder = "";
		String po = "";
		String tmpLotCreateDate = "";
		String article = "";
		String[] array;
		List<String> ppmmStatusList = new ArrayList<>();
		if (poList.size() == 1) {
			InputPlanningReportDetail bean = poList.get(0);

			dyeDate = bean.getDyeDate();
			docDate = bean.getDocDate();
			prodOrder = bean.getProductionOrder();
			po = bean.getPo();
			tmpLotCreateDate = bean.getTempLotCreateDate();
			article = bean.getArticle();
			ppmmStatusList = bean.getPpmmStatusList();
			if ( ! ppmmStatusList.isEmpty()) {
				wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
				whereFC = StringHandler.addStringAndIfNotEmpty(whereFC);
				whereReDye = StringHandler.addStringAndIfNotEmpty(whereReDye);
				String whereTmp = " ( ";
				int checkData = 0;
				for (int i = 0; i < ppmmStatusList.size(); i ++ ) {
					String beanTmp = ppmmStatusList.get(i);
					if ( ! beanTmp.equals("")) {
						checkData = 1;
						whereTmp += " [PPMMStatus] = '" + beanTmp + "' ";
						if (i < ppmmStatusList.size()
								-1) {
							whereTmp += " or ";
						}
					}
				}
				whereTmp += " ) \r\n";
				if (checkData == 0) {
					whereTmp = "";
				}
				wherePO += whereTmp;
				whereFC += whereTmp;
				whereReDye += whereTmp;
			}
			if ( ! docDate.equals("")) {
				wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
				whereFC = StringHandler.addStringAndIfNotEmpty(whereFC);
				whereReDye = StringHandler.addStringAndIfNotEmpty(whereReDye);
				array = docDate.split(" - ");
				wherePO += " ( [DocDate] >= convert(date,'" + array[0] + "', 103) AND \r\n"
						+ "    [DocDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
				whereFC += " ( [DocDate] >= convert(date,'" + array[0] + "', 103) AND \r\n"
						+ "    [DocDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
				whereReDye += ""
						+ " ( [DocDate] >= convert(date,'" + array[0] + "', 103) AND \r\n"
						+ "   [DocDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
			}
			if ( ! tmpLotCreateDate.equals("")) {
				wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
				whereFC = StringHandler.addStringAndIfNotEmpty(whereFC);
				whereReDye = StringHandler.addStringAndIfNotEmpty(whereReDye);
				array = tmpLotCreateDate.split(" - ");
				wherePO += ""
						+ " ( [TempLotCreateDate] >= convert(date,'" + array[0] + "', 103) AND \r\n"
						+ "   [TempLotCreateDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
				whereFC += ""
						+ " ( [TempLotCreateDate] >= convert(date,'" + array[0] + "', 103) AND \r\n"
						+ "   [TempLotCreateDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
				whereReDye += ""
						+ " ( [TempLotCreateDate] >= convert(date,'" + array[0] + "', 103) AND \r\n"
						+ "   [TempLotCreateDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
			}
			if ( ! po.equals("")) {
				wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
				whereFC = StringHandler.addStringAndIfNotEmpty(whereFC);
				whereReDye = StringHandler.addStringAndIfNotEmpty(whereReDye);
				wherePO += " ( a.[PO] like '" + po + "%' OR a.[POPuangMain] like '" + po + "%' )";
				whereFC += " a.[PO] like '" + po + "%' ";
				whereReDye += " a.[PO] like '" + po + "%' ";
			}
			if ( ! article.equals("")) {
				wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
				whereFC = StringHandler.addStringAndIfNotEmpty(whereFC);
				whereReDye = StringHandler.addStringAndIfNotEmpty(whereReDye);
				wherePO += " a.[Article] like '" + article + "%' ";
				whereFC += " a.[Article] like '" + article + "%' ";
				whereReDye += " a.[Article] like '" + article + "%' ";
			}
			if ( ! dyeDate.equals("")) {
				wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
				whereFC = StringHandler.addStringAndIfNotEmpty(whereFC);
				whereReDye = StringHandler.addStringAndIfNotEmpty(whereReDye);
				array = dyeDate.split(" - ");
				wherePO += ""
				        + " ( [PlanSystemDate] >= convert(date,'" + array[0] + "', 103) AND \r\n"
						+ "  [PlanSystemDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
				whereFC += ""
						+ " ( [PlanSystemDate] >= convert(date,'"+ array[0] + "', 103) AND \r\n"
						+ "   [PlanSystemDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
				whereReDye += ""
						+ " ( [PlanSystemDate] >= convert(date,'" + array[0] + "', 103) AND \r\n"
						+ "   [PlanSystemDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
			}    
			if ( ! prodOrder.equals("")) {
				wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
				whereFC = StringHandler.addStringAndIfNotEmpty(whereFC);
				whereReDye = StringHandler.addStringAndIfNotEmpty(whereReDye);
				wherePO += " [ProductionOrder] like '" + prodOrder + "%' ";
				whereFC += " [ProductionOrder] like '" + prodOrder + "%' ";
				whereReDye += " [ProductionOrder] like '" + prodOrder + "%' ";
			}
		} else if (poList.size() > 1) {
			String whereTemp = " ( \r\n";
			whereTemp += " [ProductionOrder] in ( \r\n";
			for (int i = 0; i < poList.size(); i ++ ) {
				prodOrder = poList.get(i).getProductionOrder();
				whereTemp += " '" + prodOrder + "' ";
				if (i < poList.size()
						-1) {
					whereTemp += " , ";
				}
			}
			whereTemp += " 	) \r\n";
			whereTemp += " ) \r\n";
			wherePO += whereTemp;
			whereFC += whereTemp;
			whereReDye += whereTemp;
		}
		if ( ! wherePO.equals("")) {
			wherePO = " where " + wherePO;
		}
		if ( ! whereFC.equals("")) {
			whereFC = " where " + whereFC;
		}
		if ( ! whereReDye.equals("")) {
			whereReDye = " where " + whereReDye;
		}

		String sql = "" + this.declareFCTempProd
//				+ this.declarePPMMDye
				+ this.declarePO
				+ this.declarePPMMReDye
				+ this.declarePPMMPOAdd
				+ " IF OBJECT_ID('tempdb..#tempMainDataPO') IS NOT NULL   \r\n"
				+ "    DROP TABLE #tempMainDataPO;\r\n"
				+ " SELECT distinct \r\n"
				+ this.selectPO    
				+ " INTO #tempMainDataPO\r\n"
				+ " FROM #tempFromPO as a\r\n"
				+ " LEFT JOIN #tempAPFacDyeDate AS TAF ON A.POId = TAF.POId\r\n"
				+ "  where a.Id is not null and a.OperationEndDate is null\r\n"
				+ " IF OBJECT_ID('tempdb..#tempMainData') IS NOT NULL   \r\n"
				+ "    DROP TABLE #tempMainData;\r\n"
				+ " select *\r\n" 
				+ " into #tempMainData\r\n"
				+ " from ( \r\n"
				+ " 	select *\r\n"
				+ " 	from #tempMainDataPO as a \r\n"
				+ wherePO
				+ " UNION \r\n"
				+ "  SELECT distinct \r\n"
				+ this.selectPRDFC    
				+ " FROM  (  select distinct \r\n"
				+ "      a.ForecastId, a.[ProductionOrder] , a.ProdOrderQty,a.FirstLot\r\n"
				+ "		,b.CustomerName,b.ForecastNo,a.CreateDate as TempLotCreateDate\r\n"
				+ "	    ,A.GroupNo,A.SubGroup,A.PlanSystemDate,MaxLTDeliveryDate,MaxLTCFMDate ,null as Article\r\n"
				+ "     ,cast( null as date) as ApprovedDate\r\n"
				+ "		,'' as MaterialNo\r\n"
				+ " 	,LastDayDate  AS CustomerDue\r\n"
				+ "		,b.ForecastNo as [PO]\r\n"
				+ "		,'' as [POLine]\r\n"
				+ "     ,'Planning' as PPMMStatus\r\n"
				+ "	    ,b.DocDate \r\n"
				+ "     ,case\r\n"
				+ "       when LastSORDueDate is not null then LastSORDueDate\r\n"
				+ " 	  WHEN MaxLTDeliveryDate <= LastDayDate THEN LastDayDate\r\n"
				+ "       else MaxLTDeliveryDate\r\n"
				+ "       end as SORDueDate\r\n"
				+ " ,case\r\n"
				+ "       when LastSORCFMDate is not null then LastSORCFMDate\r\n"
				+ " 	  WHEN MaxLTDeliveryDate <= LastDayDate THEN [LastCFMDate]\r\n"
				+ "       else MaxLTCFMDate\r\n"
				+ "       end as SORCFMDate\r\n"
				+ "	 ,LastDayDate\r\n"
				+ " , a.Batch\r\n"
				+ " , '' as UserStatus\r\n"
				+ " , '' as LabStatus\r\n"
				+ " , '' as CustomerMat \r\n"
				+ " , '' as POIdPuangMain\r\n"
				+ " , '' as POPuangMain\r\n"
				+ "	, '' as  POLinePuangMain \r\n"
				+ " , '' as LabNo\r\n"
				+ " , '' as Shade\r\n"
				+ " , '' as BookNo\r\n"
				+ " ,  cast ( null as date)  as CustomerDuePuangMain\r\n"
				+ " , null as VolumeFG\r\n"
				+ " , TotalForecastQty as OrderQty\r\n"
				+ " ,  cast ( null as date)  as GreigePlan\r\n"
				+ " , b.Unit\r\n"
				+ " , '' as LotNo\r\n"
				+ " , '' as Design\r\n"
				+ " ,CAST(NULL AS DECIMAL(13,3)) AS QuantityKG\r\n"
				+ " ,cast(null as date) as DueDate\r\n"
				+ " ,cast(null as date) AS CustomerDueShow\r\n"
				+ " ,cast(null as date) as PlanGreigeDate\r\n"
				+ " ,cast(null as date) as GreigeInDate\r\n"
				+ " ,'' as PlanningRemark\r\n"
				+ "		from #tempFCTempProdReal as a\r\n"
				+ "     " + this.innerJoinFCCusB
				+ "     " + this.leftJoinFCMDEL
				+ "     " + this.leftJoinFCMCFM 
				+ "     " + this.leftJoinPRDFCLDD
				+ "     " + this.leftJoinFCLApp
				+ "		where a.ForecastId is not null and \r\n"
				+ "				a.[DataStatus] = 'O'   \r\n"
				+ "		) as a  \r\n"
				+ whereFC
				+ " UNION \r\n"
				+ " select  *\r\n"
				+ "	from ( \r\n"
				+ "		SELECT distinct \r\n"
				+ this.selectPRDRedye
				+ " 	FROM #tempPPMMReDye as a   \r\n"
				+ " ) as a\r\n"
				+ whereReDye
				+ " UNION \r\n"
				+ " select  *\r\n"
				+ "	from ( \r\n"
				+ "		SELECT distinct \r\n"
				+ this.selectPOAdd  
				+ " 	FROM #tempPPMMPOAdd as a \r\n"
				+ " ) as a\r\n"
				+ whereReDye
				+ " ) as a\r\n"

				+ "  select * \r\n"
				+ "	,  (SELECT COUNT(PO)\r\n"
				+ "		FROM(select distinct  po ,POLine \r\n"
				+ "			from #tempMainData as SUB_A\r\n"
				+ "			where SUB_A.POType = 'PO'\r\n"
				+ "			) AS SUB_A\r\n"
				+ "		) AS TotalPO \r\n"
				+ "	,  (SELECT COUNT(PO)\r\n"
				+ "		FROM(select distinct  po ,POLine \r\n"
				+ "			from #tempMainData as SUB_A\r\n"
				+ "			where SUB_A.POType = 'POMain Puang'\r\n"
				+ "			) AS SUB_A\r\n"
				+ "		) AS TotalPOMainPuang \r\n"
				+ "	,  (SELECT COUNT(PO)\r\n"
				+ "		FROM(select distinct  po ,POLine \r\n"
				+ "			from #tempMainData as SUB_A\r\n"
				+ "			where SUB_A.POType = 'POSub Puang'\r\n"
				+ "			) AS SUB_A\r\n"
				+ "		) AS TotalPOSubPuang \r\n"
				+ "	,  (SELECT COUNT(PO)\r\n"
				+ "		FROM(select distinct  po  \r\n"
				+ "			from #tempMainData as SUB_A\r\n"
				+ "			where SUB_A.POType = 'FC'\r\n"
				+ "			) AS SUB_A\r\n"
				+ "		) AS TotalFC \r\n"
				+ "	,  (SELECT COUNT(ProductionOrder)\r\n"
				+ "		FROM(select distinct  ProductionOrder  \r\n"
				+ "			from #tempMainData as SUB_A\r\n"
				+ "			where SUB_A.POType = 'REDYE'\r\n"
				+ "			) AS SUB_A\r\n"
				+ "		) AS TotalRedye \r\n"
				+ "	,  (SELECT COUNT(ProductionOrder)\r\n"
				+ "		FROM(select distinct  ProductionOrder  \r\n"
				+ "			from #tempMainData as SUB_A\r\n"
				+ "			where SUB_A.POType = 'WAITRESULT'\r\n"
				+ "			) AS SUB_A\r\n"
				+ "		) AS TotalWaitResult \r\n"
				+ "	,  (SELECT COUNT(ProductionOrder)\r\n"
				+ "		FROM(select distinct  ProductionOrder  \r\n"
				+ "			from #tempMainData as SUB_A\r\n"
				+ "			where SUB_A.POType = 'SCOURING'\r\n"
				+ "			) AS SUB_A\r\n"
				+ "		) AS TotalScouring \r\n"
				+ "	,  (SELECT COUNT(ProductionOrder)\r\n"
				+ "		FROM(select distinct  ProductionOrder  \r\n"
				+ "			from #tempMainData as SUB_A\r\n"
				+ "			where SUB_A.POType = 'POADD'\r\n"
				+ "			) AS SUB_A\r\n"
				+ "		) AS TotalStart \r\n"
				+ " from #tempMainData as a\r\n"
				+ " Order By  a.POIdPuangMain,a.POPuangMain,a.POLinePuangMain,POType,CustomerDue, a.PO , a.POLine ,a.ProductionOrder  "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPlanningReportDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<POStatusReportDetail> getPOStatusReportDetail(ArrayList<POStatusReportDetail> poList) {
		ArrayList<POStatusReportDetail> list = null;
		POStatusReportDetail bean = poList.get(0);
		String po = bean.getPo();
		String poDate = bean.getPoDate();
		String docDate = bean.getDocDate();
		String matNo = bean.getMaterialNo();
		String cusDue = bean.getCustomerDue();
		String greigePlan = bean.getGreigePlan();
		String prodOrder = bean.getProductionOrder();
		List<String> ppmmStatusList = bean.getPpmmStatusList();
		String article = bean.getArticle();
		String[] array;
//		+ " declare @BeginDate date  = convert(date, '"+planStart+"', 103) ;\r\n"
		String where = "";
		if ( ! po.equals("")) {
			where += " ( [PO] like '" + po + "%' or [POPuangMain] like '" + po + "%' )";
		}
		if ( ! poDate.equals("")) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			array = poDate.split(" - ");
			where += " "
					+ " ( [PODate] >= convert(date,'"
					+ array[0]
					+ "', 103) AND [PODate] <= convert(date,'"
					+ array[1]
					+ "', 103) ) \r\n";
		}
		if ( ! docDate.equals("")) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			array = docDate.split(" - ");
			where += " "
					+ " ( [DocDate] >= convert(date,'" + array[0] + "', 103) AND \r\n"
					+ "   [DocDate] <= convert(date,'" + array[1] + "', 103) ) \r\n";
		}
		if ( ! article.equals("")) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			where += " [Article] like '" + article + "%' ";
		}
		if ( ! matNo.equals("")) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			where += " [MaterialNo] like '" + matNo + "%' ";
		}
		if ( ! cusDue.equals("")) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			array = cusDue.split(" - ");
			where += " "
					+ " ( [CustomerDue] >= convert(date,'" + array[0] + "', 103) and \r\n"
					+ "   [CustomerDue] <= convert(date,'" + array[0] + "', 103) )";
		}
		if ( ! greigePlan.equals("")) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			array = greigePlan.split(" - ");
			where += " ( [GreigePlan] >= convert(date,'"
					+ array[0]
					+ "', 103) AND [GreigePlan] <= convert(date,'"
					+ array[1]
					+ "', 103) ) \r\n";
//			where += " [GreigePlan] >= convert(date,'"+greigePlan+"', 103) ";
		}
		if ( ! prodOrder.equals("")) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			where += " [ProductionOrder] like '" + prodOrder + "%' ";
		}
		if (ppmmStatusList.size() > 0) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			String whereTmp = " ( ";
			int checkData = 0;
			for (int i = 0; i < ppmmStatusList.size(); i ++ ) {
				String beanTmp = ppmmStatusList.get(i);
				if ( ! beanTmp.equals("")) {
					checkData = 1;
					whereTmp += " [PPMMStatus] = '" + beanTmp + "' ";
					if (i < ppmmStatusList.size()
							-1) {
						whereTmp += " or ";
					}
				}
			}
			whereTmp += " ) \r\n";
			if (checkData == 0) {
				whereTmp = "";
			}
			where += whereTmp;
		}
		if ( ! where.equals("")) {
			where = " where " + where;
		}
		String sql = ""
				+ " SET NOCOUNT ON;"
				+ this.declarePO
				+ " \r\n"
				+ " IF OBJECT_ID('tempdb..#tempFromPOData') IS NOT NULL   \r\n"
				+ "    DROP TABLE #tempFromPOData;\r\n"
				+ " select *\r\n"
				+ " into #tempFromPOData\r\n"
				+ " from ( \r\n"
				+ "     SELECT \r\n"
				+ this.selectPOStatus
				+ "		FROM #tempFromPO as a \r\n"
				+ " LEFT JOIN #tempAPFacDyeDate AS TAF ON A.POId = TAF.POId\r\n"
				+ " where a.PO not like '%saleman%' \r\n"
				+ " ) as a\r\n "
				+ where
				+ "  select * \r\n"
				+ "	  , (SELECT COUNT(PO)\r\n"
				+ "		FROM(select distinct  po ,POLine \r\n"
				+ "			from #tempFromPOData as SUB_A\r\n"
				+ "			where SUB_A.POType = 'PO'\r\n"
				+ "			) AS A\r\n"
				+ "		) AS TotalPO \r\n"
				+ " from #tempFromPOData as a\r\n"
				+ "  Order By a.CustomerDue , a.PO , a.POLine ,A.FirstLot,a.ProductionOrder "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPOStatusReportDetail(map));
		} 
		return list;
	}

	@Override
	public ArrayList<PlanningReportDetail> getProductionOrderByPOId(int poId) {
		ArrayList<PlanningReportDetail> list = null;
//		int poId = bean.getPOId();
		String where = "";
		where += " ( [POId] = " + poId + " )";
		if ( ! where.equals("")) {
			where = " where " + where;
		}
		String sql = "" + this.declarePOMainNPOInstead
//				+ this.declarePPMMDye
				+ this.declareTempBeginSpecialDesign
				+ this.declareTempMaxPlan
				+ this.declareTempMainSpecialDesign
//				+ this.declareTempReCusDue
				+ this.declareTempSumPOQtyPuang
				+ this.declareTempMaxPOProd
				+ this.declareTempSumPOWOLast
				+ this.declareTempBatchRPAP
				+ " IF OBJECT_ID('tempdb..#tempLotMain') IS NOT NULL   \r\n"
				+ "		DROP TABLE #tempLotMain;\r\n"
				+ "	select distinct  \r\n"
				+ "		 b.[Id] as  [POId],B.PO,B.POLine,a.ProductionOrder,[SORDueDate]\r\n"
				+ "	  , case\r\n"
				+ "			when SPOPD.POId is not null then 'POMain Puang'			\r\n"
				+ "			else 'PO' 			\r\n"
				+ "			end as POType \r\n"
				+ "	 into  #tempLotMain\r\n"
				+ "  FROM [PPMM].[dbo].[SOR_PODetail] as B  \r\n"
				+ " left join  #tempPOMainNPOInstead  as a on a.POId = b.Id  \r\n"
				+ this.leftJoinSPOPDSamePOId
				+ "	left join (\r\n"
				+ "		select distinct a.[POId] ,[SORCFMDate] as LastSORCFMDate, a.[SORDueDate]  ,a.[ApprovedBy]  as LastApprovedBy,a.[ApprovedDate]\r\n"
				+ "		from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
				+ "		inner join (\r\n"
				+ "			select a.[POId] , max(a.[ApprovedDate]) as maxApprovedDate\r\n"
				+ "			from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
				+ "			where \r\n"
				+ "			a.DataStatus = 'O' and\r\n"
				+ "			a.[SorDueDate] is not null \r\n"
				+ "			Group by  a.[POId]\r\n"
				+ "		)as LApp on a.[POId] = LApp.[POId]  and a.[ApprovedDate] = LApp.[maxApprovedDate]\r\n"
				+ "		where \r\n"
				+ "			a.DataStatus = 'O' and\r\n"
				+ "			a.[SorDueDate] is not null \r\n"
				+ "	)as LApp on a.Id = LApp.[POId]\r\n"
				+ "	where  ( a.DataStatus = 'O' or a.[DataStatus] = 'P' ) AND\r\n"
				+ "			( B.[DataStatus] = 'O'  ) AND \r\n"
				+ "			 ( B.[POPuangId] IS NOT NULL and a.Id is not null   ) OR (  B.POPuangId IS NULL AND SPOPD.POID IS NULL ) AND\r\n"
				+ "			 A.ProductionOrder IS NOT NULL and \r\n"
				+ "         ( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )\r\n"
				+ " IF OBJECT_ID('tempdb..#tempLotSub') IS NOT NULL   \r\n"
				+ "    DROP TABLE #tempLotSub;\r\n"
				+ " select distinct \r\n"
				+ "		 b.[Id] as  [POId],B.PO,B.POLine ,a.[ProductionOrder] ,[SORDueDate]\r\n"
				+ "		, case\r\n"
				+ "			when b.Id = b.POId then 'POMain Puang'\r\n"
				+ "			else 'POSub Puang'\r\n"
				+ "			end as POType\r\n"
				+ " into  #tempLotSub\r\n"
				+ " FROM (  SELECT \r\n"
				+ "		 a.[Id] ,A.PO,A.POLine\r\n" 
				+ "     ,SPOPD.POId\r\n" 
				+ "     ,SPOPD.POId as POIdPuangMain\r\n"
				+ "     ,SPOPD.PO AS POPuangMain\r\n"
				+ "     ,SPOPD.POLine AS POLinePuangMain \r\n"
				+ "		from  [PPMM].[dbo].[SOR_PODetail] as a\r\n"
				+ "     "
				+ this.innerJoinSPOPDNotSamePOId
				+ "		WHERE A.DataStatus = 'O'\r\n"
				+ "	) as b\r\n"
				+ " 	left join #tempPOMainNPOInstead  as a on a.POId = b.POId\r\n"
				+ "	left join (\r\n"
				+ "		select distinct a.[POId] ,[SORCFMDate] as LastSORCFMDate, a.[SORDueDate]  ,a.[ApprovedBy]  as LastApprovedBy,a.[ApprovedDate]\r\n"
				+ "		from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
				+ "		inner join (\r\n"
				+ "			select a.[POId] , max(a.[ApprovedDate]) as maxApprovedDate\r\n"
				+ "			from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
				+ "			where  \r\n"
				+ "			a.DataStatus = 'O' and\r\n"
				+ "			a.[SorDueDate] is not null \r\n"
				+ "			Group by  a.[POId]\r\n"
				+ "		)as LApp on a.[POId] = LApp.[POId]  and a.[ApprovedDate] = LApp.[maxApprovedDate]\r\n"
				+ "		where   \r\n"
				+ "			a.DataStatus = 'O' and\r\n"
				+ "			a.[SorDueDate] is not null \r\n"
				+ "	)as LApp on a.Id = LApp.[POId]\r\n"
				+ "	where   ( a.DataStatus = 'O' or a.[DataStatus] = 'P' );\r\n"
				+ " select * \r\n"
				+ " FROM (\r\n"
				+ "	 select * from #tempLotMain \r\n"
				+ "	 union\r\n"
				+ "	 select * from  #tempLotSub\r\n"
				+ "  ) AS A    \r\n"
				+ where
				+ " Order By a.ProductionOrder ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPlanningReportDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<RecreateRedyeReportDetail> getRecreateRedyeReportDetail(
			ArrayList<RecreateRedyeReportDetail> poList) {
		ArrayList<RecreateRedyeReportDetail> list = null;
		RecreateRedyeReportDetail bean = poList.get(0);

		String prodOrder = bean.getProductionOrder();
		bean.getLotNo();
		String dueDate = bean.getDueDate();
		String matNo = bean.getMaterialNumber();
		String custMat = bean.getCustomerMat();
		bean.getCustomerNameList();
		bean.getQuantityKG();
		bean.getQuantityMR();
		bean.getUserStatusList();
		bean.getLabStatusList();

		String[] array;
		String wherePO = "";
		if ( ! prodOrder.equals("")) {
			wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
			wherePO += " a.[ProductionOrder] like '" + prodOrder + "%' ";
		}
		if ( ! dueDate.equals("")) {
			wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
			array = dueDate.split(" - ");
			wherePO += " (a.[DueDate] >= convert(date,'"
					+ array[0]
					+ "', 103) AND a.[DueDate] <= convert(date,'"
					+ array[1]
					+ "', 103) ) \r\n";
		}
		if ( ! matNo.equals("")) {
			wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
			wherePO += " a.MaterialNumber like '" + matNo + "%' ";
		}
		if ( ! custMat.equals("")) {
			wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
			wherePO += " a.CustomerMat like '" + custMat + "%' ";
		}
		if ( ! wherePO.equals("")) {
			wherePO = " and " + wherePO;
		}

		String sql = ""
				+ "		IF OBJECT_ID('tempdb..#tempUserCon') IS NOT NULL \r\n"
				+ "		DROP TABLE #tempUserCon;\r\n"
				+ "	 SELECT \r\n"
				+ "     a.[UserStatusId] ,a.LabStatusId ,b.UserStatus ,c.LabStatus ,[RedyeReport]\r\n"
				+ "	  into #tempUserCon\r\n"
				+ "  FROM [PPMM].[dbo].[UserStatusCondtion] as a\r\n"
				+ "  left join [PPMM].[dbo].UserStatusDetail as b on a.UserStatusId = b.Id\r\n"
				+ "  left join [PPMM].[dbo].LabStatusDetail as c on a.LabStatusId = c.[Id]\r\n"
				+ " WHERE [RedyeReport] = 'O'\r\n"
				+ " IF OBJECT_ID('tempdb..#tempMaxPrd') IS NOT NULL 	\r\n"
				+ "	DROP TABLE #tempMaxPrd;\r\n"
				+ " select * 	\r\n"
				+ "	,  CHARINDEX('F',\r\n"
				+ "		RIGHT([ProductionOrder], LEN([ProductionOrder]) - 6)  										\r\n"
				+ "	) AS IndexProdF\r\n"
				+ " into #tempMaxPrd	\r\n"
				+ " from (	\r\n"
				+ "	SELECT distinct [ProductionOrder] \r\n"
				+ "	,LEN([ProductionOrder]) as lenProdOrder \r\n"
				+ "	,max(Operation) as maxOperation\r\n"
				+ "	FROM [PPMM].[dbo].[DataFromSap] AS A  		\r\n"
				+ "  inner join [PPMM].[dbo].[viewUserStatusCondition] as viewUSC ON a.[UserStatus] = viewUSC.[UserStatus] and \r\n"
				+ "																	 viewUSC.[DyeFocus] = 'O' \r\n"
				+ "	left join #tempUserCon as b on a.UserStatus = b.UserStatus\r\n"
				+ "	left join #tempUserCon as c on a.LabStatus = c.LabStatus									\r\n"
				+ "	where	AdminStatus <> 'ForceClosed' and 										\r\n"
//		+ "			a.ChangeDate >= DATEADD(year, -1, GETDATE())  and									\r\n"
//		+ "			a.DueDate >= DATEADD(year, -2, GETDATE()) and 	 \r\n"
				+ "		 ( b.UserStatus is not null or c.LabStatus is not null )							\r\n"
				+ "	group by ProductionOrder\r\n"
				+ " ) as a 	\r\n"
				+ "  where lenProdOrder >= 6 	 \r\n"
				+ "\r\n"
				+ "SELECT DISTINCT \r\n"
				+ "		a.[ProductionOrder] \r\n"
				+ "		,FSMP.LotNo\r\n"
				+ "		, PlanGreigeDate\r\n"
				+ "		, GreigeInDate\r\n"
				+ "		,a.MaterialNumber\r\n"
				+ "		,a.CustomerMat\r\n"
				+ "		,a.DueDate \r\n"
				+ "		,a.[CustomerDue] \r\n"
				+ "		,a.CustomerName\r\n"
				+ "		,a.QuantityKG\r\n"
				+ "		,a.QuantityMR\r\n"
				+ "		,a.UserStatus\r\n"
				+ "		,a.LabStatus\r\n"
				+ "      ,d.ProdOrderBook \r\n"
				+ "  FROM [PPMM].[dbo].[DataFromSap] as a \r\n"
				+ "  inner join #tempMaxPrd as b on a.ProductionOrder = b.ProductionOrder \r\n"
//				+ "  inner join [PPMM].[dbo].[viewUserStatusCondition] as viewUSC ON a.[UserStatus] = viewUSC.[UserStatus] and \r\n"
//				+ "																	 viewUSC.[DyeFocus] = 'O' \r\n"
				+ "  left join ( \r\n"
				+ "	  SELECT a.ProductionOrder , a.Operation ,a.OperationEndDate,a.UserStatus\r\n"
				+ "	  FROM [PPMM].[dbo].[DataFromSap] as a  \r\n"
				+ "	  inner JOIN (\r\n"
				+ "		  select DISTINCT a.ProductionOrder , max(A.Operation) AS maxDye\r\n"
				+ "		  from [PPMM].[dbo].[DataFromSap] as a\r\n"
				+ "		  inner join #tempMaxPrd as b on a.ProductionOrder = b.ProductionOrder \r\n"
				+ "		  where AdminStatus <> 'ForceClosed' and ( a.Operation >= 100 AND A.Operation <= 104 )  \r\n"
				+ "		  group by a.ProductionOrder\r\n"
				+ "	  ) AS B on a.ProductionOrder = b.ProductionOrder and a.Operation = b.maxDye \r\n"
				+ "	  inner join [PPMM].[dbo].[viewUserStatusCondition] as c on a.UserStatus = c.UserStatus\r\n"
				//U002 รอคีย์ B/C , U051	HOLD,รอโอน , U058	คีย์ B/C แล้วรอจ่าย
				+ "	  	where  ( c.UserStatusSapId = 'U002' or c.UserStatusSapId = 'U051' or c.UserStatusSapId = 'U058'  ) \r\n"
				+ " ) as c on a.ProductionOrder = c.ProductionOrder and a.UserStatus = c.UserStatus\r\n"
				+ " left join (\r\n"
				+ "     SELECT [Id]\r\n"
				+ "      ,[KeyWord]\r\n"
				+ "      ,[ProductionOrder] as ProdOrderBook\r\n"
				+ "      ,[ProductionOrderTemp]  \r\n"
				+ "      ,[Remark]\r\n"
				+ "      ,[DataStatus]\r\n"
				+ "      ,[ChangeBy]\r\n"
				+ "      ,[ChangeDate]\r\n"
				+ "      ,[CreateDate]\r\n"
				+ "  FROM [PPMM].[dbo].[ProdOrderRunning]  \r\n"
				+ " ) as d on a.ProductionOrder = d.[ProductionOrderTemp]\r\n"
				+ "	left JOIN [PCMS].[dbo].[FromSapMainSale]  AS FSMS ON a.SaleOrder = FSMS.SaleOrder and a.SaleLine = FSMS.SaleLine\r\n"
				+ "	left JOIN [PCMS].[dbo].[FromSapMainProd]  AS FSMP ON a.ProductionOrder = FSMP.ProductionOrder\r\n"
				+ "  where ( \r\n"
				+ "			(\r\n"
				+ "				c.UserStatus is not null and \r\n"
				+ "		     	c.OperationEndDate is not null \r\n"
				+ "			) or \r\n"
				+ "			c.ProductionOrder is null \r\n" 
				+ "			) and \r\n"
				+ "			maxOperation <> 10 and \r\n"
				+ "			b.IndexProdF = 0 \r\n"
//				+ "	and\r\n"
//				+ "	(\r\n"
//				+ "		a.UserStatus <> 'ขายแล้ว'and a.UserStatus <> 'รอขาย' and a.UserStatus <> 'ปิดเพื่อแก้ไข' and a.UserStatus <> 'ยกเลิก'and  \r\n"
//				+ "		a.UserStatus <> 'ตัดเกรดZ'and a.UserStatus<> 'OVER' and a.UserStatus <> 'ขายเหลือ'  and  \r\n"
//				+ "		a.UserStatus <> 'จบการผลิต' and a.UserStatus <> ''  and a.UserStatus <> 'ขายแล้วแต่SALEยังไม่จบ' \r\n"
//				+ "	)\r\n"
				+ "       "
				+ wherePO
				+ "  order by a.ProductionOrder , a.DueDate ";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genRecreateRedyeReportDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<RedyePlanningReportDetail> getRedyePlanningReportDetail(
			ArrayList<RedyePlanningReportDetail> poList) {
		ArrayList<RedyePlanningReportDetail> list = null;
		RedyePlanningReportDetail bean = poList.get(0);

		String prodOrder = bean.getProductionOrder();
		bean.getLotNo();
		String dueDate = bean.getDueDate();
		String matNo = bean.getMaterialNumber();
		String custMat = bean.getCustomerMat();
		String[] array;
		String wherePO = "";
		if ( ! prodOrder.equals("")) {
			wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
			wherePO += " a.[ProductionOrder] like '" + prodOrder + "%' \r\n";
		}
		if ( ! dueDate.equals("")) {
			wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
			array = dueDate.split(" - ");
			wherePO += " (a.[DueDate] >= convert(date,'"
					+ array[0]
					+ "', 103) AND a.DueDate <= convert(date,'"
					+ array[1]
					+ "', 103) ) \r\n";
		}
		if ( ! matNo.equals("")) {
			wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
			wherePO += " a.MaterialNumber like '" + matNo + "%' \r\n";
		}
		if ( ! custMat.equals("")) {
			wherePO = StringHandler.addStringAndIfNotEmpty(wherePO);
			wherePO += " a.CustomerMat like '" + custMat + "%' \r\n";
		}

		if ( ! wherePO.equals("")) {
			wherePO = " and " + wherePO;
		}
		String sql = ""
				+ this.declarePPMMReDye
				+ " select * \r\n"
				+ "	from ( \r\n"
				+ "		SELECT distinct \r\n"
				+ "      a.ProductionOrder\r\n"
				+ " 	, LotNo\r\n"
				+ "		,a.PlanGreigeDate\r\n"
				+ "		,a.GreigeInDate\r\n"
				+ " 	, CustomerDue\r\n"
				+ " 	, DueDate\r\n"
				+ " 	, CustomerName \r\n"
				+ " 	, MaterialNumber \r\n"
				+ " 	, [QuantityMR]\r\n"
				+ " 	, [QuantityKG]\r\n"
				+ " 	, a.GroupNo\r\n"
				+ " 	, a.SubGroup \r\n"
				+ " 	, a.PlanSystemDate\r\n"
				+ "  	, a.UserStatus\r\n"
				+ " 	, a.LabStatus\r\n"
				+ " 	, a.CustomerMat\r\n"
				+ "     , a.ReDyeStatus\r\n"
				+ " 	FROM #tempPPMMReDye as a\r\n"
				+ "     where a.POType = 'REDYE' \r\n"
				+ wherePO
				+ " \r\n"
				+ " ) as a\r\n"
//				+ " where ReDyeStatus = 'O' "+wherePO +" \r\n"
				+ " Order By a.CustomerDue   , a.ProductionOrder ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genRedyePlanningReportDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<SummaryMonthlyCapReportDetail> getSummaryMonthlyCapReportDetail(
			ArrayList<InputDateRunningDetail> poList) {
		ArrayList<SummaryMonthlyCapReportDetail> list = null;
		InputDateRunningDetail bean = poList.get(0);
		String MMyyyy = bean.getDate();
		MMyyyy.split("/");
		String where = "";
		if ( ! where.equals("")) {
			where = " where " + where;
		}
		this.getLastDayOfMonth(MMyyyy);
		String begin = "01/" + MMyyyy;
		String declarePlan = ""
				+ " declare @BeginDateFirst date  = convert(date, '"
				+ begin
				+ "', 103) ;\r\n"
				+ " declare @today date  = cast(GETDATE() as date) ;\r\n";

		String sql = ""
				+ declarePlan
				+ this.declareMonthly
				+ this.declareMonthlyTempWordDate
				+ this.declarePOMainNPOInsteadMonthly
				+ this.declareTempFCDate
				+ " IF OBJECT_ID('tempdb..#tempRemainFirstMonth') IS NOT NULL \r\n"
				+ "		DROP TABLE #tempRemainFirstMonth; 	\r\n"
				+ " select *  \r\n"
				+ "	, ( TotalLotWorkDate  - ( TotalPlanSystemDatePO+TotalPlanSystemDateRedye+TotalPlanSystemDateForecast))   * MeterPerLot as RemainCapacityQty\r\n"
				+ " into #tempRemainFirstMonth\r\n"
				+ "	from (\r\n"
				+ "SELECT DISTINCT \r\n"
				+ this.selectMCRFirst
				+ "  FROM [PPMM].[dbo].[InputMainGroupDetail] as a\r\n"
				+ "  inner join [PPMM].[dbo].[InputSubGroupDeail] as b on a.[GroupNo] = b.[GroupNo] \r\n"
				+ "  left join (\r\n"
				+ "     SELECT a.[GroupNo] ,a.[SubGroup] \r\n"
				+ "			  , count(RowNum) as totalWorkDate\r\n"
				+ "		FROM #tempWorkDate   as a\r\n"
				+ "		where @BeginFirstMonth <= WorkDate and WorkDate <= @LastFirstMonth and WorkDate >= @today  and a.WorkDate >= @today  \r\n"
				+ "		group by a.[GroupNo]  ,a.[SubGroup]	 "
				+ " ) as c on a.GroupNo = c.GroupNo and b.SubGroup = c.SubGroup \r\n"
				+ this.leftJoinTotalPODFirst
				+ this.leftJoinTotalRedyeFirst
				+ this.leftJoinTotalForecastFFirst
				+ "  where a.DataStatus = 'O' AND B.[DataStatus] = 'O'  \r\n"
				+ "  ) as a\r\n"
				+ " IF OBJECT_ID('tempdb..#tempRemainSecondMonth') IS NOT NULL \r\n"
				+ "		DROP TABLE #tempRemainSecondMonth; 	\r\n"
				+ " select *  \r\n"
				+ "	, ( TotalLotWorkDate  -(TotalPlanSystemDatePO+TotalPlanSystemDateRedye+TotalPlanSystemDateForecast))   * MeterPerLot as RemainCapacityQty\r\n"
				+ " into #tempRemainSecondMonth\r\n"
				+ "	from (\r\n"
				+ "SELECT DISTINCT \r\n"
				+ this.selectMCRSecond
				+ "  FROM [PPMM].[dbo].[InputMainGroupDetail] as a\r\n"
				+ "  inner join [PPMM].[dbo].[InputSubGroupDeail] as b on a.[GroupNo] = b.[GroupNo] \r\n"
				+ "  left join (\r\n"
				+ "     SELECT a.[GroupNo] ,a.[SubGroup] \r\n"
				+ "			  , count(RowNum) as totalWorkDate\r\n"
				+ "		FROM #tempWorkDate   as a\r\n"
				+ "		where @BeginSecondMonth <= WorkDate and WorkDate <= @LastSecondMonth and a.WorkDate >= @today   \r\n"
				+ "		group by a.[GroupNo]  ,a.[SubGroup]	 "
				+ " ) as c on a.GroupNo = c.GroupNo and b.SubGroup = c.SubGroup \r\n"
				+ this.leftJoinTotalPODSecond
				+ this.leftJoinTotalRedyeSecond
				+ this.leftJoinTotalForecastFSecond
				+ "  where a.DataStatus = 'O' AND B.[DataStatus] = 'O'  \r\n"
				+ "  ) as a\r\n"
				+ " IF OBJECT_ID('tempdb..#tempRemainThirdMonth') IS NOT NULL \r\n"
				+ "		DROP TABLE #tempRemainThirdMonth; 	\r\n"
				+ " select *  \r\n"
				+ "	, ( TotalLotWorkDate  -(TotalPlanSystemDatePO+TotalPlanSystemDateRedye+TotalPlanSystemDateForecast))   * MeterPerLot as RemainCapacityQty\r\n"
				+ " into #tempRemainThirdMonth\r\n"
				+ "	from (\r\n"
				+ "SELECT DISTINCT \r\n"
				+ this.selectMCRThird
				+ "  FROM [PPMM].[dbo].[InputMainGroupDetail] as a\r\n"
				+ "  inner join [PPMM].[dbo].[InputSubGroupDeail] as b on a.[GroupNo] = b.[GroupNo] \r\n"
				+ "  left join (\r\n"
				+ "     SELECT a.[GroupNo] ,a.[SubGroup] \r\n"
				+ "			  , count(RowNum) as totalWorkDate\r\n"
				+ "		FROM #tempWorkDate   as a\r\n"
				+ "		where @BeginThirdMonth <= WorkDate and WorkDate <= @LastThirdMonth  and a.WorkDate >= @today   \r\n"
				+ "		group by a.[GroupNo]  ,a.[SubGroup]	 "
				+ " ) as c on a.GroupNo = c.GroupNo and b.SubGroup = c.SubGroup \r\n"
				+ this.leftJoinTotalPODThird
				+ this.leftJoinTotalRedyeThird
				+ this.leftJoinTotalForecastFThird
				+ "  where a.DataStatus = 'O' AND B.[DataStatus] = 'O'  \r\n"
				+ "  ) as a\r\n"
				+ ""
				+ " select \r\n "
				+ "     1 as No\r\n"
				+ "     ,(\r\n"
				+ "			 RIGHT('00' + cast(month(@LastFirstMonth) as varchar(2)),2)+'/'+\r\n"
				+ "			CAST( year(@LastFirstMonth) as varchar(4))\r\n"
				+ "		) as DateBeginLast\r\n"
				+ "		, ( \r\n"
				+ "			select   COUNT(WorkDate)   \r\n"
				+ "			from #tempWorkDate as a\r\n"
				+ "			where  a.WorkDate >= @BeginFirstMonth  and  a.WorkDate <= @LastFirstMonth and a.WorkDate >= @today and a.GroupNo = 'G1' AND a.[SubGroup] = '1' ) AS DaysRemainning\r\n"
				+ "		,(select sum( ProdOrderQty )\r\n"
				+ "			from (select case\r\n"
				+ "					when b.ProdOrderQty is not null then b.ProdOrderQty\r\n"
				+ "					else c.QuantityMR \r\n"
				+ "					end as ProdOrderQty\r\n"
				+ "					FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
				+ "					left join #tempPOMainNPOInstead as b on a.TempProdId = b.id\r\n"
				+ "					left join [PPMM].[dbo].[PlanLotPOAddDetail] as c on a.TempPOAddId = c.Id\r\n"
				+ "					WHERE A.DataStatus = 'O' and \r\n"
				+ "							a.[GroupNo] is not null and\r\n"
				+ "							a.[PlanSystemDate] >= @today and\r\n"
				+ "							a.[PlanSystemDate] >= @BeginFirstMonth  and \r\n"
				+ "							a.[PlanSystemDate] <= @LastFirstMonth AND \r\n"
				+ "							( b.POId IS NOT NULL or C.DataStatus = 'O' )\r\n"
				+ "			) as a) as TotalPlanSystemDatePO\r\n"
				+ "		,(select sum(b.[QuantityMR]) \r\n"
				+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
				+ "             INNER JOIN [PPMM].[dbo].[PlanLotRedyeDetail] AS B ON A.RedyeId = b.Id\r\n"
				+ "			  	WHERE A.DataStatus = 'O' and \r\n"
				+ "					a.[GroupNo] is not null and\r\n"
				+ "				      a.[PlanSystemDate] >= @today and\r\n"
				+ "					a.[PlanSystemDate] >= @BeginFirstMonth  and \r\n"
				+ "					a.[PlanSystemDate] <= @LastFirstMonth AND \r\n"
				+ "					A.ReDyeId IS NOT NULL  and \r\n"
				+ "					  b.DataStatus = 'O') as TotalPlanSystemDateRedye \r\n"
				+ "		,cast(\r\n"
				+ "			(select sum(a.RemainBLQty) \r\n"
				+ "			FROM #tempFCDate AS A  \r\n"
				+ "			WHERE A.DataStatus = 'O' and   \r\n"
				+ "				a.[ForecastDate]  >= @BeginFirstMonth  and \r\n"
				+ "				a.[ForecastDate] <= @LastFirstMonth  )   		\r\n"
				+ "				as decimal(13,1)) as TotalFCBL\r\n"
				+ "		,cast(\r\n"
				+ "			(select sum(a.RemainNonBLQty) \r\n"
				+ "			  	FROM #tempFCDate AS A  \r\n"
				+ "			  	WHERE A.DataStatus = 'O' and   \r\n"
				+ "					a.[ForecastDate]  >= @BeginFirstMonth  and \r\n"
				+ "					a.[ForecastDate] <= @LastFirstMonth  )   \r\n"
				+ "		as decimal(13,3)) as TotalFCNonBL \r\n"
				+ "		,cast(\r\n"
				+ "           ( select sum(RemainCapacityQty) from #tempRemainFirstMonth ) "
				+ "		as decimal(13,3)) as TotalRemain\r\n"
				+ " UNION \r\n"
				+ " select  \r\n"
				+ "     2 as No\r\n"
				+ "     ,(\r\n"
				+ "			 RIGHT('00' + cast(month(@LastSecondMonth) as varchar(2)),2)+'/'+\r\n"
				+ "			CAST( year(@LastSecondMonth) as varchar(4))\r\n"
				+ "		) as DateBeginLast\r\n"
				+ "		, ( \r\n"
				+ "			select   COUNT(WorkDate)   \r\n"
				+ "			from #tempWorkDate as a\r\n"
				+ "			where  a.WorkDate >= @BeginSecondMonth  and  a.WorkDate <= @LastSecondMonth  and a.WorkDate >= @today  and a.GroupNo = 'G1'  AND a.[SubGroup] = '1' AND a.[SubGroup] = '1'   ) AS DaysRemainning\r\n"
				+ "		,(	    select sum( ProdOrderQty )\r\n"
				+ "				from (select case\r\n"
				+ "						when b.ProdOrderQty is not null then b.ProdOrderQty\r\n"
				+ "						else c.QuantityMR \r\n"
				+ "						end as ProdOrderQty\r\n"
				+ "						FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
				+ "						left join #tempPOMainNPOInstead as b on a.TempProdId = b.id\r\n"
				+ "						left join [PPMM].[dbo].[PlanLotPOAddDetail] as c on a.TempPOAddId = c.Id\r\n"
				+ "						WHERE A.DataStatus = 'O' and \r\n"
				+ "								a.[GroupNo] is not null and\r\n"
				+ "								a.[PlanSystemDate] >= @today and\r\n"
				+ "								a.[PlanSystemDate] >= @BeginSecondMonth  and \r\n"
				+ "								a.[PlanSystemDate] <= @LastSecondMonth AND \r\n"
				+ "								( b.POId IS NOT NULL or C.DataStatus = 'O' )\r\n"
				+ "				) as a\r\n"
				+ "      ) as TotalPlanSystemDatePO\r\n"
				+ "		,(select sum(b.[QuantityMR]) \r\n"
				+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
				+ "             INNER JOIN [PPMM].[dbo].[PlanLotRedyeDetail] AS B ON A.RedyeId = b.Id\r\n"
				+ "			  	WHERE A.DataStatus = 'O' and \r\n"
				+ "					a.[GroupNo] is not null and\r\n"
				+ "				    a.[PlanSystemDate] >= @today and\r\n"
				+ "					a.[PlanSystemDate] >= @BeginSecondMonth  and \r\n"
				+ "					a.[PlanSystemDate] <= @LastSecondMonth AND \r\n"
				+ "					A.ReDyeId IS NOT NULL and \r\n"
				+ "					  b.DataStatus = 'O') as TotalPlanSystemDateRedye \r\n"
				+ "		,cast(\r\n"
				+ "			(select sum(a.RemainBLQty) \r\n"
				+ "			FROM #tempFCDate AS A  \r\n"
				+ "			WHERE A.DataStatus = 'O' and   \r\n"
				+ "				a.[ForecastDate]  >= @BeginSecondMonth  and \r\n"
				+ "				a.[ForecastDate] <= @LastSecondMonth  )   		\r\n"
				+ "				as decimal(13,1)) as TotalFCBL\r\n"
				+ "		,cast(\r\n"
				+ "			(select sum(a.RemainNonBLQty) \r\n"
				+ "			  	FROM #tempFCDate AS A  \r\n"
				+ "			  	WHERE A.DataStatus = 'O' and   \r\n"
				+ "					a.[ForecastDate]  >= @BeginSecondMonth  and \r\n"
				+ "					a.[ForecastDate] <= @LastSecondMonth  )   \r\n"
				+ "		as decimal(13,3)) as TotalFCNonBL \r\n"
				+ "		,cast(\r\n"
				+ "           ( select sum(RemainCapacityQty) from #tempRemainSecondMonth ) \r\n"
				+ "		as decimal(13,3)) as TotalRemain\r\n"
				+ " UNION \r\n"
				+ " select  \r\n"
				+ "     3 as No\r\n"
				+ "     ,(\r\n"
				+ "			 RIGHT('00' + cast(month(@LastThirdMonth) as varchar(2)),2)+'/'+\r\n"
				+ "			CAST( year(@LastThirdMonth) as varchar(4))\r\n"
				+ "		) as DateBeginLast\r\n"
				+ "		, ( \r\n"
				+ "			select   COUNT(WorkDate)   \r\n"
				+ "			from #tempWorkDate as a\r\n"
				+ "			where  a.WorkDate >= @BeginThirdMonth  and  a.WorkDate <= @LastThirdMonth  and a.WorkDate >= @today  and a.GroupNo = 'G1'  ) AS DaysRemainning\r\n"
				+ "		,(	select sum( ProdOrderQty )\r\n"
				+ "			from (select \r\n"
				+ "                case\r\n"
				+ "					when b.ProdOrderQty is not null then b.ProdOrderQty\r\n"
				+ "					else c.QuantityMR \r\n"
				+ "					end as ProdOrderQty\r\n"
				+ "				   FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
				+ "				   left join #tempPOMainNPOInstead as b on a.TempProdId = b.id\r\n"
				+ "				   left join [PPMM].[dbo].[PlanLotPOAddDetail] as c on a.TempPOAddId = c.Id\r\n"
				+ "				   WHERE A.DataStatus = 'O' and \r\n"
				+ "						 a.[GroupNo] is not null and\r\n"
				+ "						 a.[PlanSystemDate] >= @today and\r\n"
				+ "						 a.[PlanSystemDate] >= @BeginThirdMonth  and \r\n"
				+ "						 a.[PlanSystemDate] <= @LastThirdMonth AND \r\n"
				+ "						 ( b.POId IS NOT NULL or C.DataStatus = 'O')\r\n"
				+ "				) as a\r\n"
				+ "      ) as TotalPlanSystemDatePO\r\n"
				+ "		,(select sum(b.[QuantityMR]) \r\n"
				+ "			  	FROM [PPMM].[dbo].[TEMP_PlanningLot] AS A\r\n"
				+ "             INNER JOIN [PPMM].[dbo].[PlanLotRedyeDetail] AS B ON A.RedyeId = b.Id\r\n"
				+ "			  	WHERE A.DataStatus = 'O' and \r\n"
				+ "					a.[GroupNo] is not null and\r\n"
				+ "				    a.[PlanSystemDate] >= @today and\r\n"
				+ "					a.[PlanSystemDate] >= @BeginThirdMonth  and \r\n"
				+ "					a.[PlanSystemDate] <= @LastThirdMonth AND \r\n"
				+ "					A.ReDyeId IS NOT NULL and \r\n"
				+ "					  b.DataStatus = 'O') as TotalPlanSystemDateRedye \r\n"
				+ "		,cast(\r\n"
				+ "			(select sum(a.RemainBLQty) \r\n"
				+ "			FROM #tempFCDate AS A  \r\n"
				+ "			WHERE A.DataStatus = 'O' and   \r\n"
				+ "				a.[ForecastDate]  >= @BeginThirdMonth  and \r\n"
				+ "				a.[ForecastDate] <= @LastThirdMonth  )   		\r\n"
				+ "				as decimal(13,1)) as TotalFCBL\r\n"
				+ "		,cast(\r\n"
				+ "			(select sum(a.RemainNonBLQty) \r\n"
				+ "			  	FROM #tempFCDate AS A  \r\n"
				+ "			  	WHERE A.DataStatus = 'O' and   \r\n"
				+ "					a.[ForecastDate]  >= @BeginThirdMonth  and \r\n"
				+ "					a.[ForecastDate] <= @LastThirdMonth  )   \r\n"
				+ "		as decimal(13,3)) as TotalFCNonBL \r\n"
				+ "		,cast(\r\n"
				+ "           ( select sum(RemainCapacityQty) from #tempRemainThirdMonth ) \r\n"
				+ "		as decimal(13,1)) as TotalRemain\r\n"
				+ "";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSummaryMonthlyCapReportDetail(map));
		}
		return list;
	}
//	@Override
//	public ArrayList<ProdOrderRunningDetail> bookingProdOrderForProdOrderRunningReport(ArrayList<ProdOrderRunningDetail> poList) {
//		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
//		ProdOrderRunningDetail bean = poList.get(0);
//		String prodOrderBook = bean.getProductionOrder();
//		String changeByBook = bean.getChangeBy();
//		bean.setKeyWord(prodOrderBook);
//		String iconStatus = Config.C_SUC_ICON_STATUS;
//		String systemStauts = "Update Success.";
//		ArrayList<ProdOrderRunningDetail> list = porModel.getProductionOrderRunningDetailById(poList);
//		if(list.size() > 0 ) {
//			ProdOrderRunningDetail beanTmp = poList.get(0);
//			String dataStatus = beanTmp.getDataStatus();
//			String changeBy = beanTmp.getChangeBy();
//			String remark = beanTmp.getRemark();
//			String prodOrderDB = beanTmp.getProductionOrder() ;
//			boolean isAlrTopping = false;
//			String lastChar = prodOrderDB.substring(prodOrderDB.length() - 1);
//			if(lastChar.equals("T")) {
//				isAlrTopping = true;
//			}
//			list.get(0).setChangeBy(changeByBook);
//			list.get(0).setRemark(remark);
//			if(dataStatus.equals("O")) {
//				this.handlerChangeProdOrderRunningLog(poList);
//
//				ArrayList<ProdOrderRunningDetail> listTmp = new ArrayList<>();
//				listTmp = porModel.updateProdOrderRunningWithIdForRemark( list,changeByBook );
//				beanTmp = listTmp.get(0);
//
//				list = porModel.getProductionOrderRunningDetailById(poList);
//				list.get(0).setChangeDate(beanTmp.getChangeDate());
//				list.get(0).setDataStatus("X");
//			}
//			else {
//				iconStatus = Config.C_ERR_ICON_STATUS;
//				systemStauts = "This ProductionOrder already booked by "+changeBy;
//			}
//			list.get(0).setIconStatus(iconStatus);
//			list.get(0).setSystemStatus(systemStauts);
//		}
//		else {
//			list.clear();
//			ProdOrderRunningDetail beanTemp = new ProdOrderRunningDetail();
//			beanTemp.setIconStatus(Config.C_ERR_ICON_STATUS);
//			beanTemp.setSystemStatus("Something happen ! Please contact it.");
//			list.add(beanTemp);
//		}
//		return list;
//	}
//
//	private String handlerChangeProdOrderRunningLog(ArrayList<ProdOrderRunningDetail> poList) {
//		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
//		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
//		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
//		HashMap<Integer, ProdOrderRunningDetail> mapDateAndDataStatus = new HashMap<>();
//		String changeBy = "";
//		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
//		String iconStatus=Config.C_SUC_ICON_STATUS;
//		HashMap<String, String> mapMSC = new HashMap<>();
//		ArrayList<MasterSettingChangeDetail> listMSC = mscModel.getMasterSettingChangeDetail( Config.sqlTableChangeProdOrderRunningLog) ;
//		for(int i = 0 ;i < listMSC.size();i++) {
//			MasterSettingChangeDetail beanTemp = listMSC.get(i);
//			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
//		}
//		// GET NEW DATE
//		for(int i = 0 ; i < poList.size() ;i++) {
//			ProdOrderRunningDetail beanTmp = poList.get(i);
//			int id = beanTmp.getId();
//			mapDateAndDataStatus.put(id,beanTmp);
//		}
//		ArrayList<ProdOrderRunningDetail> listOld = porModel.getProductionOrderRunningDetailById(poList);
//		for(int i = 0 ; i < listOld.size() ;i++) {
//			ProdOrderRunningDetail beanTmpOld = listOld.get(i);
//			ProdOrderRunningDetail beanTmpNew = mapDateAndDataStatus.get(beanTmpOld.getId());
//			int id =  beanTmpOld.getId() ;
//			String oldDataStatus = beanTmpOld.getDataStatus();
//			String oldRemark = beanTmpOld.getRemark();
//			boolean oldIsTopping = beanTmpOld.isTopping();
//			// Remark
//			if(beanTmpNew != null) {
//				String newDataStatus = beanTmpNew.getDataStatus();
//				String newRemark = beanTmpNew.getRemark();
//				boolean newIsTopping = beanTmpNew.isTopping();
//				listCSL = HandlerListLog.handlerListLog(
//						listCSL ,
//						oldRemark,newRemark,mapMSC.get(Config.sqlFieldRemark),Integer.toString(id),changeBy,
//						Config.sqlFieldRemark
//						);
//				listCSL = HandlerListLog.handlerListLog(
//						listCSL ,
//						oldDataStatus,newDataStatus,mapMSC.get(Config.sqlFieldDatatStatus),Integer.toString(id),changeBy,
//						Config.sqlFieldDatatStatus
//						);
//				listCSL = HandlerListLog.handlerListLog(
//						listCSL ,
//						String.valueOf(oldIsTopping),String.valueOf(newIsTopping),mapMSC.get(Config.sqlFieldIsTopping),Integer.toString(id),changeBy,
//						Config.sqlFieldIsTopping
//						);
//			}
//		}
//		if(!listCSL.isEmpty()) {
//			cslModel.insertChangeSettingLogDetail(
//					listCSL
//					,Config.sqlTableChangeProdOrderRunningLog,Config.sqlFieldProdOrderRunningId
//					);
//		}
//		return iconStatus;
//	}
//

	public ArrayList<TotalGroupDyeDetail> getTotalGroupDyeDetail(ArrayList<InputDateRunningDetail> poList) {
		ArrayList<TotalGroupDyeDetail> list = null;
		InputDateRunningDetail bean = poList.get(0);
		String MMyyyy = bean.getDate();
		String where = "";
		if ( ! where.equals("")) {
			where = " where " + where;
		}
		String begin = "01/" + MMyyyy;
		String declarePlan = ""
				+ " declare @BeginDateFirst date  = convert(date, '"
				+ begin
				+ "', 103) ;\r\n"
				+ " declare @today date  = cast(GETDATE() as date) ;\r\n";
		String sql = ""
				+ declarePlan
				+ this.declareMonthly
				+ "  SELECT c.[WorkDate] as PlanDate, b.[GroupNo],b.[SubGroup]  ,C.[NormalWork] AS IsHolilyDay,CAST(d.[TotalLot] AS varchar) as  TotalLot\r\n"
				+ " FROM [PPMM].[dbo].[InputMainGroupDetail] AS A\r\n"
				+ " INNER JOIN [PPMM].[dbo].[InputSubGroupDeail] AS B ON A.[GroupNo] = B.[GroupNo]    \r\n"
				+ " INNER join [PPMM].[dbo].[GroupWorkDate] as c ON B.[GroupNo] = C.[GroupNo] AND B.[SubGroup] = C.[SubGroup] \r\n"
				+ " left join (\r\n"
				+ "		SELECT\r\n"
				+ "			[PlanSystemDate],a.GroupNo ,a.SubGroup\r\n"
				+ "			,count ( CASE WHEN A.GroupNo is not null and a.[SubGroup] is not null and [PlanSystemDate] is not null THEN 1 END ) as TotalLot\r\n"
				+ "			FROM [PPMM].[dbo].[viewTEMPPlanningLotOnProcess] AS A \r\n"
				+ "			where A.GroupNo is not null and A.SubGroup is not null AND \r\n"
				+ "				  A.[PlanSystemDate] >= @BeginFirstMonth and a.[PlanSystemDate] <= @LastThirdMonth and\r\n"
				+ "				  left(a.ProductionOrder,1) <> 'F'\r\n"
				+ "			Group by a.GroupNo ,a.SubGroup ,[PlanSystemDate]   \r\n"
				+ " ) as d on b.GroupNo = d.[GroupNo] and b.[SubGroup] = d.[SubGroup] and d.[PlanSystemDate] = c.[WorkDate]\r\n"
				+ " where a.[DataStatus] = 'O' and b.[DataStatus] = 'O' and b.[LotPerDay] > 0  \r\n"
				+ "		AND( C.[WorkDate] >= @BeginFirstMonth and C.[WorkDate] <= @LastThirdMonth)\r\n"
				+ "order by  c.[WorkDate], b.[GroupNo],b.[SubGroup] \r\n"
				+ "\r\n";
 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTotalGroupDyeDetail(map));
		}
		return list;
	}

	@Override
	public JSONObject handlerTotalGroupDyeDetail(ArrayList<InputDateRunningDetail> poList) {
		SubGroupDetailModel sgdModel = new SubGroupDetailModel(this.conType);
		HashMap<String, String> mapWorkGroup = new HashMap<>();
		ArrayList<InputGroupDetail> listGroup = sgdModel.getSubGroupDetailWithLotPerDayMoreThanZero();

		ArrayList<TotalGroupDyeDetail> listTotalGroup = this.getTotalGroupDyeDetail(poList);
		JSONArray jsonArrayData = new JSONArray();
		JSONArray jsonArrayColumn = new JSONArray();
//		JSONArray jsonArrayOrder = new JSONArray();

		JSONObject jsonObjectColumn = new JSONObject();
		jsonObjectColumn.put("data", "PlanDate");
		jsonObjectColumn.put("title", "PlanDate");
		jsonObjectColumn.put("name", "PlanDate");
		jsonObjectColumn.put("type", "date-euro");
		jsonArrayColumn.put(jsonObjectColumn);
		for (InputGroupDetail beanTmp : listGroup) {
			mapWorkGroup.put(beanTmp.getMainNSubGroup(), "Y");

			jsonObjectColumn = new JSONObject();
			jsonObjectColumn.put("data", beanTmp.getMainNSubGroup());
			jsonObjectColumn.put("name", beanTmp.getMainNSubGroup());
			jsonObjectColumn.put("title", beanTmp.getMainNSubGroup());
			jsonObjectColumn.put("orderable", false);
			jsonArrayColumn.put(jsonObjectColumn);
		}

		String isHoliday = "";
		String tempDate = "";
		String curDate = "";
		String mainNSubGroup = "";
		String totalLot = "";
		JSONObject jsonObject = new JSONObject();
		for (int i = 0; i < listTotalGroup.size(); i ++ ) {
			TotalGroupDyeDetail bean = listTotalGroup.get(i);
			curDate = bean.getPlanDate();
			totalLot = bean.getTotalLot();
			isHoliday = bean.getIsHolilyDay();
			mainNSubGroup = bean.getMainNSubGroup();
			if (tempDate.equals(curDate)) {
//				jsonObject.put(mainNSubGroup, totalLot);
//				jsonObject.put(mainNSubGroup+"isHolyday", isHoliday);
			} else {
				if ( ! tempDate.equals("")) {
					jsonArrayData.put(jsonObject);
				}
				tempDate = curDate;
				jsonObject = new JSONObject();
				jsonObject.put("PlanDate", curDate);
//				jsonObject.put(mainNSubGroup, totalLot);
//				jsonObject.put(mainNSubGroup+"isHolyday", isHoliday);
			}
			jsonObject.put(mainNSubGroup, totalLot);
			jsonObject.put(mainNSubGroup + "_" + "isHolyday", isHoliday);
			if (i == listTotalGroup.size()
					-1) {
				jsonArrayData.put(jsonObject);
			}
		}

		JSONObject jsonObjectMain = new JSONObject();
		jsonObjectMain.put("data", jsonArrayData);
		jsonObjectMain.put("columns", jsonArrayColumn);
		return jsonObjectMain;
	}

	@Override
	public ArrayList<PlanningReportDetail> handlerVolumeReport(ArrayList<PlanningReportDetail> poList) {
		ArrayList<PlanningReportDetail> list = new ArrayList<>();
		ArrayList<PlanningReportDetail> listTempPOPuang = new ArrayList<>(); 
		int poIdNext = 0; 
		double db_sumVolume = 0; 
		String poIdPuangNext = ""; 
		String prodOrderPuang = "";
		double db_volumeProdPuang = 0; 
		boolean bl_isPuangDone = false;
		PlanningReportDetail beanTmp = null;
		HashMap<String, Double> mapProdWithVolume = new HashMap<>();
		for (int i = 0; i < poList.size(); i ++ ) {
			PlanningReportDetail bean = poList.get(i);
			int poId = bean.getPoId(); 
			String poType = bean.getPoType(); 
			String prodOrder = bean.getProductionOrder(); 
			String POIdPuangMain = bean.getPoIdPuangMain(); 
			String orderQty = bean.getOrderQty();
			String volume = bean.getVolumeFG(); 
			// RESET VALUE
			if (i != poList.size()-1) {
				beanTmp = poList.get(i+1);
				poIdNext = beanTmp.getPoId(); 
				poIdPuangNext = beanTmp.getPoIdPuangMain(); 
			} else { 
				poIdNext = 0; 
				poIdPuangNext = ""; 
			}
			if (poType.equals("PO")) {
				double db_orderQty = Double.parseDouble(orderQty.replaceAll(",", ""));
				double db_volume = Double.parseDouble(volume.replaceAll(",", "")); 
				if (poIdNext == poId) {
					db_volume = db_volume-(db_volume % 10);
					if (db_volume == db_orderQty) {
					} else if (db_volume > db_orderQty) {
					} else {
					}
					volume = df3.format(db_volume);
					db_sumVolume += db_volume;
				} else {
					if (db_sumVolume == 0) {
						db_volume = db_orderQty;
					} else {
						db_volume = db_orderQty-db_sumVolume;
					}
					volume = df3.format(db_volume);
					db_sumVolume = 0;
				}
				bean.setVolumeFG(volume);
				list.add(bean);
			} else if (poType.equals("POMain Puang") || poType.equals("POSub Puang")) {
 				double db_orderQty = Double.parseDouble(orderQty.replaceAll(",", ""));
				double db_volume = Double.parseDouble(volume.replaceAll(",", ""));  
				if (poType.equals("POMain Puang")) {
					listTempPOPuang.add(bean);
					mapProdWithVolume.put(prodOrder, db_volume);
				} else { 
					if (POIdPuangMain.equals(poIdPuangNext) ) {
						listTempPOPuang.add(bean);
					} else {
						// GET LAST LINE
						listTempPOPuang.add(bean);
						db_sumVolume = 0;
						double db_sumSedProdQty = 0;
						// handler prod
						for (int j = 0; j < listTempPOPuang.size(); j ++ ) {
							PlanningReportDetail beanPOPuang = listTempPOPuang.get(j);
							orderQty = beanPOPuang.getOrderQty();
							prodOrderPuang = beanPOPuang.getProductionOrder();
							db_orderQty = Double.parseDouble(orderQty.replaceAll(",", ""));
							poId = beanPOPuang.getPoId(); 
//							// RESET VALUE
							if (j != listTempPOPuang.size()-1) {
								beanTmp = listTempPOPuang.get(j+1);
								poIdNext = beanTmp.getPoId();
								poIdPuangNext = beanTmp.getPoIdPuangMain();
							} else {
								poIdNext = 0; 
								poIdPuangNext = "";
							}
							if (mapProdWithVolume.get(prodOrderPuang) != null) {
								db_volumeProdPuang = mapProdWithVolume.get(prodOrderPuang);
							} else {
								db_volumeProdPuang = 0;
							}
							if (db_volumeProdPuang == 0 || db_orderQty < db_sumVolume) {
								volume = "";
								db_sumSedProdQty = 0;
							} else {
								db_sumVolume += db_volumeProdPuang;
								// 500 < 20 a=20
								// 500 < 20 + 490 | b=480
								if (bl_isPuangDone) {
									db_volumeProdPuang = db_sumVolume-(db_orderQty-db_sumSedProdQty);
									volume = "0";
								} else if (db_orderQty < db_sumVolume) {
									db_volumeProdPuang = db_sumVolume-(db_orderQty-db_sumSedProdQty);
									volume = df3.format(db_orderQty-db_sumSedProdQty);
								} else if (db_orderQty == db_sumVolume) {
									volume = df3.format(db_volumeProdPuang);
									db_volumeProdPuang = 0;
									db_sumSedProdQty = 0;
									bl_isPuangDone = true;

								} else if (db_orderQty > db_sumVolume) {
									volume = df3.format(db_volumeProdPuang);
									db_sumSedProdQty += db_volumeProdPuang;
									db_volumeProdPuang = 0;
								}
							} 
							mapProdWithVolume.put(prodOrderPuang, db_volumeProdPuang);
							if (poIdNext == poId) {

							} else {
								db_sumVolume = 0;
								db_sumSedProdQty = 0;
								bl_isPuangDone = false;
							}
							beanPOPuang.setVolumeFG(volume);
							list.add(beanPOPuang);
						}
						mapProdWithVolume = new HashMap<>(); 
						mapProdWithVolume.clear();
						listTempPOPuang.clear();
					}

				} 
			} else {
				list.add(bean);
			}
		}
		return list;
	}

	@Override
	public void processVolumeForReport() {
		RelationPOAndProdOrderModel rpapModel = new RelationPOAndProdOrderModel(this.conType);
		ArrayList<PlanningReportDetail> list = null;
		list = this.getDetailForVolumeDetail(); 
		if ( ! list.isEmpty()) {  
			list = this.handlerVolumeReport(list);
			rpapModel.upsertVolumeForRelationPOAndProdOrder(list);
		}
	}
}