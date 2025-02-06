package dao.implement;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Test.utilities.CompareDate;
import Test.utilities.SqlStatementHandler;
import controller.setting.SORAPIController;
import dao.PlanningProdDao;
import entities.InputApprovedDetail;
import entities.InputApprovedFCDetail;
import entities.InputDateRunningDetail;
import entities.InputFacWorkDateDetail;
import entities.InputGroupDetail;
import entities.InputLotDragDropDetail;
import entities.InputPODetail;
import entities.InputPlanningLotDetail;
import entities.InputTempProdDetail;
import entities.ListApprovedDetail;
import entities.PlanningReportDetail;
import entities.ProdOrderRunningDetail;
import entities.SetLotWorkDateDetail;
import entities.WorkDateDetail;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.ProdCreatedDetailModel;
import model.ReportModel;
import model.master.ApprovedPlanDateModel;
import model.master.GroupWorkDateModel;
import model.master.InputPlanningRemarkModel;
import model.master.ProdOrderRunningModel;
import model.master.TEMPPlanningLotModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class PlanningProdDaoImpl implements PlanningProdDao {
	private BeanCreateModel bcModel = new BeanCreateModel();
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private Database database;
	private String message; 
	private String conType = " ";
	private String C_Y = "Y";
	private String C_P = "P";
	private String C_CANTPLAN = "Can't Plan";
	private String C_FIRSTLOTREMARK = "FIRSTLOTREMARK";
	private String C_CANCELED = "Cancel Date";
	private String C_APPROVED = "Approved";
	private String C_POTMP = "POTMP";
	private String C_REDYE = "REDYE";
	private String C_POADD = "POADD";
	private String C_US_RORYEPPRAKOB = "รอเย็บประกบ";
	private String C_SCOURING = "SCOURING";
	private String C_WAITRESULT = "WAITRESULT";
	private String C_POADDTMP = "POADDTMP";
	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	public SimpleDateFormat sdfFull = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

	private String leftJoinSPOPDA = "" + " left join [PPMM].[dbo].[SOR_POPuangDetail] as SPOPD on a.[POPuangId] = SPOPD.Id\r\n ";

	private String leftJoinSPOPDB = "" + " left join [PPMM].[dbo].[SOR_POPuangDetail] as SPOPD on b.[POPuangId] = SPOPD.Id\r\n ";
	private String leftJoinSPOPDSamePOId = ""
			+ " left join [PPMM].[dbo].[SOR_POPuangDetail] as SPOPD on b.[POPuangId] = SPOPD.Id AND\r\n"
			+ "														   SPOPD.POId  = A.POId\r\n ";
	private String innerJoinSPOPDNotSamePOId = ""
			+ " inner join [PPMM].[dbo].[SOR_POPuangDetail] as SPOPD on a.[POPuangId] = SPOPD.Id AND\r\n"
			+ "														    SPOPD.POId  <> A.[Id] AND\r\n"
			+ " 													    SPOPD.[DataStatus] = 'O' \r\n ";
	private String leftJoinIAD = "" + "  left join [PPMM].[dbo].[InputArticleDetail] as IAD on a.[Article] = IAD.[Article]\r\n";
	private String selectPODetailOne = ""
			+ "		 a.[Id]\r\n"
			+ "     ,a.[ReferenceId]\r\n"
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
			+ "			when a.[OrderQtyCalLast] is not null and SPDC.OrderQty <> 0 then CAST( SPDC.OrderQty AS DECIMAL(15, 2)) \r\n"
			+ "			else CAST( a.[OrderQtyCalLast] AS DECIMAL(15, 2)) \r\n"
			+ "			end as [OrderQtyCalLast] \r\n"
			+ " 	,case\r\n"
			+ "			when (OrderQtyCalLast%50) >= 1 and (OrderQtyCalLast%50) <= 9 then cast(1 as bit)\r\n"
			+ "			else cast(0 as bit)\r\n"
			+ "		end as isRecheck\r\n"
			+ "     ,a.isGroupRecheck\r\n"
			+ "	   , CD.CustomerType\r\n"
			+ "		, case \r\n"
			+ "			when stc.StockReceive is not null and stc.StockReceive <> '' then stc.StockReceive\r\n"
			+ "			else 1\r\n"
			+ "			end as StockReceive   \r\n"
			+ "      , case \r\n"
			+ "			when DayOfMonth is not null and DayOfMonth <> '' then DayOfMonth\r\n"
//			+ "			else '25'\r\n"
			+ "			else CAST( ( SELECT [DayOfMonth] FROM [PPMM].[dbo].[StockDefaultDate] where [DataStatus] ='O' ) AS NVARCHAR(2) )\r\n"
//			+ "			else ( SELECT [DayOfMonth] FROM [PPMM].[dbo].[StockDefaultDate] where [DataStatus] ='O' )\r\n"
			+ "			end as DayOfMonth \r\n"
			+ "      ,  isSabina\r\n"
			+ "      ,a.[CustomerDue] as ForSortDueAndCustDue\r\n";
	private String leftJoinE_LFL = ""
			+ "  LEFT JOIN (\r\n"
			+ "		SELECT DISTINCT \r\n"
			+ "			  STP.[POId]\r\n"
			+ "	  		, MAX([PlanSystemDate]) as [FirstLotDate]   \r\n"
			+ "	  	FROM [PPMM].[dbo].[SOR_TempProd] AS STP \r\n"
			+ "	  	INNER join [PPMM].[dbo].[TEMP_PlanningLot] as TPL on STP.[Id] = TPL.[TempProdId]\r\n"
			+ "	  	WHERE \r\n"
			+ "			STP.[DataStatus] = 'O' AND\r\n"
			+ "			TPL.[DataStatus] = 'O' and\r\n"
			+ "         TPL.[PlanSystemDate] is not null AND\r\n"
			+ "			STP.[FirstLot] = 'Y'   \r\n"
			+ "	  	GROUP BY  STP.[POId]  \r\n"
			+ "  ) AS LFL ON E.[POId] = LFL.[POId]\r\n";
	private String leftJoinLFL = ""
			+ "  LEFT JOIN (\r\n"
			+ "		SELECT DISTINCT \r\n"
			+ "		      STP.[POId]\r\n"
			+ "	  		, MAX([PlanSystemDate]) as [FirstLotDate]   \r\n"
			+ "	  	FROM [PPMM].[dbo].[SOR_TempProd] AS STP \r\n"
			+ "	  	INNER join [PPMM].[dbo].[TEMP_PlanningLot] as TPL on STP.[Id] = TPL.[TempProdId]\r\n"
			+ "	  	WHERE \r\n"
			+ "			STP.[DataStatus] = 'O' AND\r\n"
			+ "			TPL.[DataStatus] = 'O' and \r\n"
			+ "         TPL.[PlanSystemDate] is not null AND\r\n"
			+ "			STP.[FirstLot] = 'Y'   \r\n"
			+ "	  	GROUP BY  STP.[POId] \r\n"
			+ "  ) AS LFL ON A.[POId] = LFL.[POId]\r\n";
	private String innerJoinDYEE = "" 
			+ " inner join [PPMM].[dbo].[PlanLotSORDetail] as e on a.[Id] = e.[TempProdId]\r\n";
	private String leftJoinDYEE = "" + " left join [PPMM].[dbo].[PlanLotSORDetail] as e on a.[Id] = e.[TempProdId]\r\n";
	private String selectPO = ""
			+ "        a.[Id] as No\r\n"
			+ "       ,a.[POId]\r\n"
			+ "       ,CAST(NULL AS int) as [ForecastId]\r\n"
			+ "       ,CAST(NULL AS int) as [RedyeId]\r\n \r\n"
			+ "       ,CAST(NULL AS int) as [TempPOAddId]\r\n"
			+ "       ,a.[ColorType]\r\n"
			+ "       ,a.[ProductionOrder]\r\n"
			+ "       ,[FirstLot]\r\n"
			+ "       ,[ProdOrderQty]\r\n"
			+ "       ,[PPMMStatus]\r\n"
			+ "       ,a.[DataStatus]\r\n"
			+ "       ,a.[ChangeDate]\r\n"
			+ "       ,a.[ChangeBy]\r\n"
			+ "       ,a.[ColorCustomer] \r\n"
			+ "       ,a.[Design]\r\n"
			+ "       ,a.[CustomerDue]\r\n"
			+ "       ,a.[Article]\r\n"
			+ "    	  ,a.[CustomerName]\r\n"
			+ "       ,'PO' as LotType\r\n"
			+ "       ,a.DyeAfterCFM\r\n"
			+ "       ,a.PlanSystemDate \r\n"
			+ "       ,a.PlanUserDate  \r\n"
			+ "       ,a.OperationEndDate\r\n"
			+ "       ,a.GroupNo\r\n"
			+ "       ,a.SubGroup\r\n"
			+ "       ,a.[GroupOptions] \r\n"
			+ "	      ,[FirstLotDate]\r\n"
			+ "	      ,a.[PlanBy]\r\n"
			+ "       ,a.[GreigePlan] \r\n"
			+ "       ,a.[DyeAfterGreigeInBegin]\r\n"
			+ "       ,a.[DyeAfterGreigeInLast]\r\n"
			+ "       ,a.[LastCFMDate]\r\n"
			+ "       ,cast([FirstLotGroupNo] as NVARCHAR) as [FirstLotGroupNo]\r\n"
			+ "       ,cast([FirstLotSubGroup] as NVARCHAR) as [FirstLotSubGroup]\r\n"
			+ "       ,a.[TempPlanningId]\r\n"
			+ "	      ,case \r\n"
			+ "          when MaxLTDeliveryDate > a.[CustomerDue] then  CONVERT(Bit, 'True')\r\n"
			+ "          else CONVERT(Bit, 'False')\r\n"
			+ "          END AS isOverDue\r\n"
			+ "       ,[GroupBegin]\r\n"
			+ "       ,CAST(NULL AS int) as [Operation] \r\n"
			+ "       ,a.[MaterialNo]\r\n"
			+ "       ,a.[PO]\r\n"
			+ "       ,a.[POLine]\r\n"
			+ "       ,a.isInSap\r\n"
			+ "       ,a.IsApproved\r\n"
			+ "       ,a.[SORDueDate] \r\n"
			+ "       ,a.MaxLTDeliveryDate \r\n"
			+ "       ,a.MaxLTCFMDate \r\n"
			+ "       ,[LTPOInputDate]\r\n"
			+ "       ,[LTMakeLotDate]\r\n"
			+ "       ,[LTBCDate]\r\n"
			+ "       ,[LTPlanDate]\r\n"
			+ "       ,[LTCFMDate]\r\n"
			+ "       ,[LTCFMAnswerDate]\r\n"
			+ "       ,[LTDeliveryDate]\r\n"
			+ "       ,a.UserStatus\r\n"
			+ "       ,a.LabStatus\r\n"
			+ "       ,a.SaleOrder\r\n"
			+ "       ,a.SaleLine\r\n"
			+ "       ,[POQty]\r\n"
			+ "       ,[POUnit]\r\n"
			+ "       , a.IsExpired  \r\n"
			+ "       , PriorDistChannal\r\n"
			+ "       , PresetEndDate\r\n"
			+ "       , 0 as isRedye\r\n"
			+ "       , 0 AS isFC\r\n"
			+ "       , 1 as isPO\r\n"
			+ "       , a.isPSSpecial  \r\n"
			+ "       ,a.CustomerMat\r\n"
			+ "       ,a.Batch\r\n"
			+ "       ,HaveFirstLot"
			+ "       ,a.PlanInsteadId\r\n"
			+ "       ,a.POIdInstead\r\n"
			+ "       ,cast(null as date) as ForecastDateMonthBefore\r\n"
			+ "		  ,cast(null as date) as ForecastDateMonthLast\r\n"
			+ "	      ,a.[POPuangId]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as ForecastMY\r\n"
			+ "       ,cast(null as date) as DueDate\r\n"
			+ "       ,ForSortDueAndCustDue\r\n"
			+ "       ,GreigePlan as PlanGreigeDate\r\n"
			+ "       ,CAST(NULL AS DATE) as GreigeInDate\r\n"
			+ "       ,CAST(NULL as decimal(13,3))  as [QuantityKG]\r\n"
			+ "       ,'' as LotNo\r\n"
			+ "       ,a.PlanningRemark\r\n"
			+ "       ,a.ProductionOrderType\r\n"
			+ " 	  ,a.[StatusAfterDate]\r\n"
			+ " 	  ,a.Id as [TempProdId]\r\n"
			+ "       ,a.ProductionOrderFirstLot\r\n"
			+ "      , a.[DyeSAPAfterFLDate]\r\n"
			+ "      ,A.[DyeSAPAfterFLStatus]\r\n";
	private String selectPOV2 = ""
			+ "        a.[Id] as No\r\n"
			+ "       ,a.[POId]\r\n"
			+ "       ,CAST(NULL AS int) as [ForecastId]\r\n"
			+ "       ,CAST(NULL AS int) as [RedyeId]\r\n \r\n"
			+ "       ,CAST(NULL AS int) as [TempPOAddId]\r\n"
			+ "       ,a.[ColorType]\r\n"
			+ "       ,a.[ProductionOrder]\r\n"
			+ "       ,[FirstLot]\r\n"
			+ "       ,[ProdOrderQty]\r\n"
			+ "       ,[PPMMStatus]\r\n"
			+ "       ,a.[DataStatus]\r\n"
			+ "       ,a.[ChangeDate]\r\n"
			+ "       ,a.[ChangeBy]\r\n"
			+ "       ,a.[ColorCustomer] \r\n"
			+ "       ,a.[Design]\r\n"
			+ "       ,a.[CustomerDue]\r\n"
			+ "       ,a.[Article]\r\n"
			+ "    	  ,a.[CustomerName]\r\n"
			+ "       ,a.LotType\r\n"
			+ "       ,a.DyeAfterCFM\r\n"
			+ "       ,a.PlanSystemDate \r\n"
			+ "       ,a.PlanUserDate  \r\n"
			+ "       ,a.OperationEndDate\r\n"
			+ "       ,a.GroupNo\r\n"
			+ "       ,a.SubGroup\r\n"
			+ "       ,a.[GroupOptions] \r\n"
			+ "	      ,[FirstLotDate]\r\n"
			+ "	      ,a.[PlanBy]\r\n"
			+ "       ,a.[GreigePlan] \r\n"
			+ "       ,a.[DyeAfterGreigeInBegin]\r\n"
			+ "       ,a.[DyeAfterGreigeInLast]\r\n"
			+ "       ,a.[LastCFMDate]\r\n"
			+ "       ,cast([FirstLotGroupNo] as NVARCHAR) as [FirstLotGroupNo]\r\n"
			+ "       ,cast([FirstLotSubGroup] as NVARCHAR) as [FirstLotSubGroup]\r\n"
			+ "       ,a.[TempPlanningId]\r\n"
			+ "	      ,case \r\n"
			+ "          when MaxLTDeliveryDate > a.[CustomerDue] then  CONVERT(Bit, 'True')\r\n"
			+ "          else CONVERT(Bit, 'False')\r\n"
			+ "          END AS isOverDue\r\n"
			+ "       ,[GroupBegin]\r\n"
			+ "       ,CAST(NULL AS int) as [Operation] \r\n"
			+ "       ,a.[MaterialNo]\r\n"
			+ "       ,a.[PO]\r\n"
			+ "       ,a.[POLine]\r\n"
			+ "       ,a.isInSap\r\n"
			+ "       ,a.IsApproved\r\n"
			+ "       ,a.[SORDueDate] \r\n"
			+ "       ,MaxLTDeliveryDate \r\n"
			+ "       ,MaxLTCFMDate \r\n"
			+ "       ,[LTPOInputDate]\r\n"
			+ "       ,[LTMakeLotDate]\r\n"
			+ "       ,[LTBCDate]\r\n"
			+ "       ,[LTPlanDate]\r\n"
			+ "       ,[LTCFMDate]\r\n"
			+ "       ,[LTCFMAnswerDate]\r\n"
			+ "       ,[LTDeliveryDate]\r\n"
			+ "       ,a.UserStatus\r\n"
			+ "       ,a.LabStatus\r\n"
			+ "       ,a.SaleOrder\r\n"
			+ "       ,a.SaleLine\r\n"
			+ "       ,[POQty]\r\n"
			+ "       ,[POUnit]\r\n"
			+ "       , a.IsExpired  \r\n"
			+ "       , PriorDistChannal\r\n"
			+ "       , PresetEndDate\r\n"
			+ "       , 0 as isRedye\r\n"
			+ "       , 0 AS isFC\r\n"
			+ "       , 1 as isPO\r\n"
			+ "       , a.isPSSpecial  \r\n"
			+ "       ,a.CustomerMat\r\n"
			+ "       ,a.Batch\r\n"
			+ "       ,HaveFirstLot"
			+ "       ,a.PlanInsteadId\r\n"
			+ "       ,a.POIdInstead\r\n"
			+ "       ,cast(null as date) as ForecastDateMonthBefore\r\n"
			+ "		  ,cast(null as date) as ForecastDateMonthLast\r\n"
			+ "	      ,a.[POPuangId]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as ForecastMY\r\n"
			+ "       ,cast(null as date) as DueDate\r\n"
			+ "       ,ForSortDueAndCustDue\r\n"
			+ "       ,GreigePlan as PlanGreigeDate\r\n"
			+ "       ,CAST(NULL AS DATE) as GreigeInDate\r\n"
			+ "       ,CAST(NULL as decimal(13,3))  as [QuantityKG]\r\n"
			+ "       ,'' as LotNo\r\n"
			+ "       ,a.PlanningRemark\r\n"
			+ "       ,a.ProductionOrderType\r\n"
			+ " 	  ,a.[StatusAfterDate]\r\n"
			+ " 	  ,a.Id as [TempProdId]\r\n"
			+ "       ,a.ProductionOrderFirstLot\r\n"
			+ "      , a.[DyeSAPAfterFLDate]\r\n"
			+ "      ,A.[DyeSAPAfterFLStatus]\r\n";

	private String selectFC = ""
			+ "        a.[Id] as No\r\n"
			+ "       ,a.[POId]\r\n"
			+ "       ,a.[ForecastId]\r\n"
			+ "       ,CAST(NULL AS int) as [RedyeId]\r\n "
			+ "       ,CAST(NULL AS int) as [TempPOAddId]\r\n"
			+ "       ,a.[ColorType]\r\n"
			+ "       ,a.[ProductionOrder]\r\n"
			+ "       ,a.[FirstLot]\r\n"
			+ "       ,a.[ProdOrderQty]\r\n"
			+ "       ,a.[PPMMStatus]\r\n"
			+ "       ,a.[DataStatus]\r\n"
			+ "       ,a.[ChangeDate]\r\n"
			+ "       ,a.[ChangeBy]\r\n"
			+ "       ,'' as [ColorCustomer]\r\n"
			+ "       ,'' as [Design]\r\n"
			+ "       ,ForecastDate as [CustomerDue]\r\n"
			+ "       ,'' as [Article]\r\n"
			+ "       ,a.[CustomerName]\r\n"
			+ "       ,'FC' as LotType\r\n"
			+ "       ,0 as DyeAfterCFM\r\n"
			+ "       ,a.PlanSystemDate \r\n"
			+ "       ,a.PlanUserDate  \r\n"
			+ "       ,CAST(NULL AS date) as OperationEndDate\r\n"
			+ "       ,a.GroupNo\r\n"
			+ "       ,a.SubGroup\r\n"
			+ "       ,a.[GroupOptions] \r\n"
			+ "       ,CAST(NULL AS date) as [FirstLotDate]\r\n"
			+ "       ,a.[PlanBy]\r\n"
			+ "       ,CAST(NULL AS date) as [GreigePlan] \r\n"
			+ "       ,CAST(NULL AS date) as [DyeAfterGreigeInBegin]\r\n"
			+ "       ,CAST(NULL AS date) as [DyeAfterGreigeInLast]\r\n"
			+ "       ,a.[LastCFMDate]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [FirstLotGroupNo]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [FirstLotSubGroup]\r\n"
			+ "       ,A.[TempPlanningId] \r\n "
			+ "       ,case \r\n"
			+ "          when MaxLTDeliveryDate > a.[ForecastDateMonthLast] then  CONVERT(Bit, 'True')\r\n"
			+ "          else CONVERT(Bit, 'False')\r\n"
			+ "          END AS isOverDue\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as GroupBegin\r\n"
			+ "       ,CAST(NULL AS int) as [Operation] \r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [MaterialNo]\r\n"
			+ "       ,a.ForecastNo as [PO]\r\n"
			+ "       ,'' as [POLine]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as isInSap\r\n"
			+ "       ,CAST( NULL AS BIT)  as IsApproved\r\n"
			+ "       ,CAST( NULL AS DATE)  as [SORDueDate] \r\n"
			+ "       , MaxLTDeliveryDate \r\n"
			+ "       , MaxLTCFMDate \r\n"
			+ "       ,[LTPOInputDate]\r\n"
			+ "       ,[LTMakeLotDate]\r\n"
			+ "       ,[LTBCDate]\r\n"
			+ "       ,[LTPlanDate]\r\n"
			+ "       ,[LTCFMDate]\r\n"
			+ "       ,[LTCFMAnswerDate]\r\n"
			+ "       ,[LTDeliveryDate]\r\n"
			+ "       ,'' as UserStatus\r\n"
			+ "       ,'' as LabStatus\r\n"
			+ "       ,'' as SaleOrder\r\n"
			+ "       ,'' as SaleLine\r\n"
			+ "       ,case\r\n"
			+ "          when [ColorType] = 'Black' then [ForecastBLQty]\r\n"
			+ "          else [ForecastNonBLQty] \r\n"
			+ "          end as [POQty] \r\n"
			+ "       ,'m' as [POUnit]\r\n"
			+ "       , CAST(NULL AS bit) as IsExpired\r\n"
			+ "       , PriorDistChannal\r\n"
			+ "	      , CAST(NULL AS date) as PresetEndDate\r\n"
			+ "       , 0 as isRedye\r\n"
			+ "       , 1 AS isFC\r\n"
			+ "       , 0 as isPO\r\n"
			+ "       , 0 as isPSSpecial\r\n"
			+ "       ,'' as CustomerMat\r\n"
			+ "       ,a.Batch\r\n"
			+ "       ,CAST(NULL AS int) AS HaveFirstLot\r\n"
			+ "       ,CAST(NULL AS int) as PlanInsteadId\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as POIdInstead\r\n"
//			+ "       ,LFL.POIdInstead\r\n"
			+ "       ,a.ForecastDateMonthBefore\r\n"
			+ "       ,a.ForecastDateMonthLast\r\n"
			+ "       ,0 as [POPuangId]\r\n"
			+ "       ,ForecastMY\r\n"
			+ "       ,CAST(NULL AS date) as [DueDate]\r\n"
			+ "       ,ForSortDueAndCustDue\r\n"
			+ "       ,CAST(NULL AS DATE) as PlanGreigeDate\r\n"
			+ "       ,CAST(NULL AS DATE) as GreigeInDate\r\n"
			+ "       ,CAST(NULL as decimal(13,3))  as [QuantityKG]\r\n"
			+ "       ,'' as LotNo\r\n"
			+ "       ,'' as PlanningRemark\r\n"
			+ "       ,'' as ProductionOrderType\r\n"
			+ "       ,CAST(NULL AS DATE) AS [StatusAfterDate]\r\n"
			+ " 	  ,a.[Id] as [TempProdId]\r\n"
			+ "       ,'' as ProductionOrderFirstLot\r\n"
			+ "      , CAST(NULL AS DATE) AS  [DyeSAPAfterFLDate]\r\n"
			+ "      , 'O' AS [DyeSAPAfterFLStatus]\r\n";

	private String selectFCForShowOnly = ""
			+ "        a.[Id] as No\r\n"
			+ "       ,a.[POId]\r\n"
			+ "       ,a.[ForecastId]\r\n"
			+ "       ,CAST(NULL AS int) as [RedyeId]\r\n"
			+ "        ,CAST(NULL AS int) as [TempPOAddId]\r\n"
			+ "       ,a.[ColorType]\r\n"
			+ "       ,a.[ProductionOrder]\r\n"
			+ "       ,a.[FirstLot]\r\n"
			+ "       ,a.[ProdOrderQty]\r\n"
			+ "       ,a.[PPMMStatus]\r\n"
			+ "       ,a.[DataStatus]\r\n"
			+ "       ,a.[ChangeDate]\r\n"
			+ "       ,a.[ChangeBy]\r\n"
			+ "       ,'' as [ColorCustomer]\r\n"
			+ "       ,'' as [Design]\r\n"
			+ "       ,ForecastDate as [CustomerDue]\r\n"
			+ "       ,'' as [Article]\r\n"
			+ "       ,A.[CustomerName]\r\n"
			+ "       ,'FC' as LotType\r\n"
			+ "       ,0 as DyeAfterCFM\r\n"
			+ "       ,a.PlanSystemDate \r\n"
			+ "       ,a.PlanUserDate  \r\n"
			+ "       ,CAST(NULL AS date) as OperationEndDate\r\n"
			+ "       ,a.GroupNo\r\n"
			+ "       ,a.SubGroup\r\n"
			+ "       ,a.[GroupOptions] \r\n"
			+ "       ,CAST(NULL AS date) as [FirstLotDate]\r\n"
			+ "       ,a.[PlanBy]\r\n"
			+ "       ,CAST(NULL AS date) as [GreigePlan] \r\n"
			+ "       ,CAST(NULL AS date) as [DyeAfterGreigeInBegin]\r\n"
			+ "       ,CAST(NULL AS date) as [DyeAfterGreigeInLast]\r\n"
			+ "       ,A.[LastCFMDate]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [FirstLotGroupNo]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [FirstLotSubGroup]\r\n"
			+ "       ,A.[TempPlanningId] \r\n"
			+ "        ,case \r\n"
			+ "          when MaxLTDeliveryDate > A.[ForecastDateMonthLast] then  CONVERT(Bit, 'True')\r\n"
			+ "          else CONVERT(Bit, 'False')\r\n"
			+ "          END AS isOverDue\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as GroupBegin\r\n"
			+ "       ,CAST(NULL AS int) as [Operation] \r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [MaterialNo]\r\n"
			+ "       ,A.ForecastNo as [PO]\r\n"
			+ "       ,'' as [POLine]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as isInSap\r\n"
			+ "       ,CAST( NULL AS BIT)  as IsApproved\r\n"
			+ "       ,CAST( NULL AS DATE)  as [SORDueDate] \r\n"
			+ "       , MaxLTDeliveryDate \r\n"
			+ "       , MaxLTCFMDate \r\n"
			+ "       ,[LTPOInputDate]\r\n"
			+ "       ,[LTMakeLotDate]\r\n"
			+ "       ,[LTBCDate]\r\n"
			+ "       ,[LTPlanDate]\r\n"
			+ "       ,[LTCFMDate]\r\n"
			+ "       ,[LTCFMAnswerDate]\r\n"
			+ "       ,[LTDeliveryDate]\r\n"
			+ "       ,'' as UserStatus\r\n"
			+ "       ,'' as LabStatus\r\n"
			+ "       ,'' as SaleOrder\r\n"
			+ "       ,'' as SaleLine\r\n"
			+ "       ,case\r\n"
			+ "          when [ColorType] = 'Black' then [ForecastBLQty]\r\n"
			+ "          else [ForecastNonBLQty] \r\n"
			+ "          end as [POQty] \r\n"
			+ "       ,'m' as [POUnit]\r\n"
			+ "       , CAST(NULL AS bit) as IsExpired\r\n"
			+ "       , PriorDistChannal\r\n"
			+ "       , CAST(NULL AS date) as PresetEndDate\r\n"
			+ "       , 0 as isRedye\r\n"
			+ "       , 1 AS isFC\r\n"
			+ "       , 0 as isPO\r\n"
			+ "       , 0 as isPSSpecial\r\n"
			+ "       ,'' as CustomerMat\r\n"
			+ "       ,a.Batch\r\n"
			+ "       ,CAST(NULL AS int) AS HaveFirstLot\r\n"
			+ "       ,CAST(NULL AS int) as PlanInsteadId\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as POIdInstead\r\n"
//			+ "       ,LFL.POIdInstead\r\n"
			+ "       ,A.ForecastDateMonthBefore\r\n"
			+ "       ,A.ForecastDateMonthLast\r\n"
			+ "       ,0 as [POPuangId]\r\n"
			+ "       ,ForecastMY\r\n"
			+ "       ,CAST(NULL AS date) as [DueDate]\r\n"
			+ "       ,ForSortDueAndCustDue\r\n"
			+ "       ,CAST(NULL AS DATE) as PlanGreigeDate\r\n"
			+ "       ,CAST(NULL AS DATE) as GreigeInDate\r\n"
			+ "       ,CAST(NULL as decimal(13,3))  as [QuantityKG]\r\n"
			+ "       ,'' as LotNo\r\n"
			+ "       ,'' as PlanningRemark\r\n"
			+ "       ,'' as ProductionOrderType\r\n"
			+ "       ,CAST(NULL AS DATE) AS [StatusAfterDate]\r\n"
			+ "       ,a.[Id] as [TempProdId]\r\n"
			+ "       ,'' as ProductionOrderFirstLot\r\n"
			+ "      , CAST(NULL AS DATE) AS  [DyeSAPAfterFLDate]\r\n"
			+ "      , 'O' AS [DyeSAPAfterFLStatus]";

	private String fromFCForShowOnly = ""
			+ " FROM ( \r\n"
			+ " SELECT a.*\r\n"
			+ "      ,BatchFirst+'/'+cast(BatchLast as NVARCHAR(max)) as Batch  \r\n"
			+ "      , MaxLTDeliveryDate,MaxLTCFMDate \r\n"
			+ "      , 0 as PriorDistChannal\r\n"
			+ "      ,SFCD.[DocDate]\r\n"
			+ "		 ,SFCD.[CustomerNo]\r\n"
			+ "		 ,SFCD.[ForecastNo]\r\n"
			+ "		 ,SFCD.[ForecastMY]\r\n"
			+ "		 ,SFCD.[TotalForecastQty]\r\n"
			+ "		 ,SFCD.[ForecastBLQty]\r\n"
			+ "		 ,SFCD.[ForecastNonBLQty]\r\n"
			+ "		 ,SFCD.[Unit]\r\n"
			+ "		 ,SFCD.[RuleNo] \r\n"
			+ "		 ,SFCD.[LastCFMDate] \r\n"
			+ "		 , case\r\n"
			+ "				when  CD.[CustomerName] is not null and CD.[CustomerName] <> '' then  CD.[CustomerName]\r\n"
			+ "    			else SFCD.[CustomerName]\r\n"
			+ "				end as [CustomerName]  \r\n"
			+ "		 ,convert(date ,right(SFCD.ForecastMY,4) + left(SFCD.ForecastMY,2) + '01') AS ForecastDate \r\n"
			+ "		 ,convert(date ,right(SFCD.ForecastMY,4) + left(SFCD.ForecastMY,2) + '01') AS ForSortDueAndCustDue \r\n"
			+ "      ,[ForecastDateBefore] AS ForecastDateMonthBefore \r\n"
			+ "      ,[ForecastDateLast] AS ForecastDateMonthLast   \r\n"
			+ "		 ,[RemainNonBLQty]\r\n"
			+ "		 ,[RemainBLQty]\r\n"
			+ "  from #tempFCTempProd as a\r\n"
			+ "  inner join ( \r\n"
			+ "	  	select forecastId ,max(cast( BatchFirst as int)) as BatchLast, MAX(c.[LTCFMDate]) as MaxLTCFMDate, MAX(c.LTDeliveryDate) as MaxLTDeliveryDate  \r\n"
			+ "	  	from #tempFCTempProd AS C\r\n"
			+ "	  	group by forecastId\r\n"
			+ "  ) as b on a.ForecastId = b.ForecastId\r\n"
			+ "  INNER join [PPMM].[dbo].[SOR_ForecastDetail]  AS SFCD ON A.[ForecastId] = SFCD.[Id] AND \r\n"
			+ "                                                           SFCD.[DataStatus] = 'O'\r\n"
			+ "  LEFT JOIN (\r\n"
			+ "		SELECT SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
			+ "	 		,[CustomerShortName] as [CustomerName]\r\n"
			+ "			,[ChangeDate]\r\n"
			+ "			,[CreateDate]\r\n"
			+ "		, DistChannel\r\n"
			+ "	   , CustomerType\r\n"
//			+ "	   ,  isSabina\r\n"
			+ "      , case\r\n"
			+ "          when [CustomerName] not like '%SABINA%' and [CustomerShortName] not like '%SBF%'\r\n"
			+ "          then 0 \r\n"
			+ "			 else 1 \r\n"
			+ "        end as isSabina\r\n"
			+ "		FROM [PCMS].[dbo].[CustomerDetail]  AS A \r\n"
			+ "  ) AS CD ON SFCD.CustomerNo = CD.CustomerNo \r\n"
			+ "  where a.ForecastId is not null and \r\n"
			+ "			[ForecastDateBefore] > CONVERT(DATE,GETDATE() ) \r\n"
			+ " ) as a\r\n";
	private String selectRDE = " "
			+ "       a.[RedyeId] as No\r\n"
			+ "       ,CAST(NULL AS int) as [POId]\r\n"
			+ "       ,CAST(NULL AS int) as [ForecastId]\r\n"
			+ "       ,a.[RedyeId]\r\n"
			+ "       ,CAST(NULL AS int) as [TempPOAddId]\r\n"
			+ "       ,a.[ColorType]\r\n"
			+ "       ,a.[ProductionOrder]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [FirstLot]\r\n"
			+ "       ,CAST(a.[QuantityMR] as decimal(13,3))  as [ProdOrderQty]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [PPMMStatus]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [DataStatus]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [ChangeDate]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [ChangeBy] \r\n"
			+ "       ,a.[ColorCustomer] \r\n"
			+ "       ,[FGDesign] AS [Design]\r\n"
			+ "       ,a.[CustomerDue]\r\n"
			+ "       ,a.[Article]\r\n"
			+ "       ,a.[CustomerName]\r\n"
			+ "       ,POType as LotType\r\n"
			+ "       ,CAST(NULL AS int) as DyeAfterCFM\r\n"
			+ "       ,c.PlanSystemDate\r\n"
			+ "       ,c.PlanUserDate\r\n"
			+ "       ,a.OperationEndDate\r\n"
			+ "       ,c.GroupNo\r\n"
			+ "       ,c.SubGroup\r\n"
			+ "       ,a.[GroupOptions]\r\n"
			+ "       ,CAST(NULL AS date) as [FirstLotDate]\r\n"
			+ "       ,c.[PlanBy]\r\n"
			+ "       ,a.[GreigePlan] as [GreigePlan] \r\n"
			+ "       ,CAST(NULL AS date) as [DyeAfterGreigeInBegin]\r\n"
			+ "       ,CAST(NULL AS date) as [DyeAfterGreigeInLast]\r\n"
			+ "       ,CAST(NULL AS date) as [LastCFMDate]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [FirstLotGroupNo]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [FirstLotSubGroup]\r\n"
			+ "       ,c.[Id] as TempPlanningId\r\n"
			+ "	      ,case \r\n"
			+ "          when C.LTDeliveryDate > a.[DueDate] then  CONVERT(Bit, 'True')\r\n"
			+ "          else CONVERT(Bit, 'False')\r\n"
			+ "          END AS isOverDue\r\n"
			+ "       , GroupBegin\r\n"
			+ "       ,a.[Operation] \r\n"
			+ "       ,a.[MaterialNumber] as MaterialNo\r\n"
			+ "       ,a.PurchaseOrder as [PO]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [POLine]\r\n"
			+ "       ,'Y' as isInSap\r\n"
			+ "       ,CAST(NULL AS bit) as [IsApproved] \r\n"
			+ "       ,CAST(NULL AS date) as [SORDueDate] \r\n"
			+ "       , CAST(NULL AS date) as MaxLTDeliveryDate\r\n"
			+ "       , CAST(NULL AS date) as MaxLTCFMDate \r\n"
			+ "       ,[LTPOInputDate]\r\n"
			+ "       ,[LTMakeLotDate]\r\n"
			+ "       ,[LTBCDate]\r\n"
			+ "       ,[LTPlanDate]\r\n"
			+ "       ,[LTCFMDate]\r\n"
			+ "       ,[LTCFMAnswerDate]\r\n"
			+ "       ,[LTDeliveryDate]\r\n"
			+ "       ,a.UserStatus\r\n"
			+ "       ,a.LabStatus\r\n"
			+ "       ,a.SaleOrder\r\n"
			+ "       ,a.SaleLine\r\n"
			+ "       ,CAST(NULL AS decimal(13,3)) as [POQty] \r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [POUnit]\r\n"
			+ "       , CPP.IsExpired  \r\n"
			+ "       , PriorDistChannal\r\n"
			+ "       , PresetEndDate\r\n"
			+ "       , 1 as isRedye\r\n"
			+ "       , 0 AS isFC\r\n"
			+ "       , 0 as isPO\r\n"
			+ "       , CASE \r\n"
			+ "         WHEN CPP.ProductionOrder IS NOT NULL THEN 1 \r\n"
			+ "         ELSE 0 \r\n"
			+ "         END as isPSSpecial  \r\n"
			+ "       , a.CustomerMat\r\n"
			+ "       ,'' as Batch\r\n"
			+ "       ,CAST(NULL AS int) AS HaveFirstLot\r\n"
			+ "       ,CAST(NULL AS int) as PlanInsteadId\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as POIdInstead\r\n"
			+ "       ,cast(null as date) as ForecastDateMonthBefore\r\n"
			+ "       ,cast(null as date) as ForecastDateMonthLast\r\n"
			+ "	      ,0 as [POPuangId]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as ForecastMY\r\n"
			+ "       , DueDate\r\n"
			+ "       ,DueDate as ForSortDueAndCustDue\r\n"
			+ "       , PlanGreigeDate\r\n"
			+ "       , GreigeInDate\r\n"
			+ "       ,CAST(a.[QuantityKG] as decimal(13,3))  as [QuantityKG]\r\n"
			+ "       ,a.LotNo\r\n"
			+ "       ,a.PlanningRemark\r\n"
			+ "       ,'' as ProductionOrderType\r\n"
			+ "       ,c.[StatusAfterDate]\r\n"
			+ " 	  ,CAST(NULL AS int) as [TempProdId]\r\n"
			+ "       ,'' AS ProductionOrderFirstLot\r\n"
			+ "      , c.[DyeSAPAfterFLDate]\r\n"
			+ "      , c.[DyeSAPAfterFLStatus]\r\n";
	private String selectPOAdd = " "
			+ "        a.[TempPOAddId] as No\r\n"
			+ "       ,CAST(NULL AS int) as [POId]\r\n"
			+ "       ,CAST(NULL AS int) as [ForecastId]\r\n"
			+ "       ,CAST(NULL AS int) as [RedyeId]\r\n"
			+ "       ,a.TempPOAddId\r\n"
			+ "       ,case\r\n"
			+ "         when LEFT( RIGHT(a.[MaterialNumber] ,6),2 ) = 'BL' THEN 'Black' \r\n"
			+ "         else 'Color'\r\n"
			+ "         end as [ColorType]\r\n"
			+ "       ,a.[ProductionOrder]\r\n"
			+ "       , [FirstLot]\r\n"
			+ "       ,CAST(a.[QuantityMR] as decimal(13,3))  as [ProdOrderQty]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [PPMMStatus]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [DataStatus]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [ChangeDate]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [ChangeBy] \r\n"
			+ "       ,a.[ColorCustomer] \r\n"
			+ "       ,[FGDesign] AS [Design]\r\n"
			+ "       ,a.[CustomerDue]\r\n"
			+ "       ,a.[Article]\r\n"
			+ "       ,a.[CustomerName]\r\n"
			+ "       ,POType as LotType\r\n"
			+ "       , DyeAfterCFM\r\n"
			+ "       ,c.PlanSystemDate\r\n"
			+ "       ,c.PlanUserDate\r\n"
			+ "       ,a.OperationEndDate\r\n"
			+ "       ,c.GroupNo\r\n"
			+ "       ,c.SubGroup\r\n"
			+ "       ,a.[GroupOptions]\r\n"
			+ "       ,[FirstLotDate]\r\n"
			+ "       ,c.[PlanBy]\r\n"
			+ "       ,a.[GreigePlan] as [GreigePlan] \r\n"
			+ "       , [DyeAfterGreigeInBegin]\r\n"
			+ "       , [DyeAfterGreigeInLast]\r\n"
			+ "       , [LastCFMDate]\r\n"
			+ "       , [FirstLotGroupNo]\r\n"
			+ "       , [FirstLotSubGroup]\r\n"
			+ "       ,c.[Id] as TempPlanningId\r\n"
			+ "	      ,case \r\n"
			+ "          when C.LTDeliveryDate > a.[DueDate]  then  CONVERT(Bit, 'True')\r\n"
			+ "          else CONVERT(Bit, 'False')\r\n"
			+ "          END AS isOverDue\r\n"
			+ "       , GroupBegin\r\n"
			+ "       ,a.[Operation] \r\n"
			+ "       ,a.[MaterialNumber] as MaterialNo\r\n"
			+ "       ,a.PurchaseOrder as [PO]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [POLine]\r\n"
			+ "       ,'Y' as isInSap\r\n"
			+ "       ,CAST(NULL AS bit) as [IsApproved] \r\n"
			+ "       ,CAST(NULL AS date) as [SORDueDate] \r\n"
			+ "       , CAST(NULL AS date) as MaxLTDeliveryDate\r\n"
			+ "       , CAST(NULL AS date) as MaxLTCFMDate \r\n"
			+ "       ,[LTPOInputDate]\r\n"
			+ "       ,[LTMakeLotDate]\r\n"
			+ "       ,[LTBCDate]\r\n"
			+ "       ,[LTPlanDate]\r\n"
			+ "       ,[LTCFMDate]\r\n"
			+ "       ,[LTCFMAnswerDate]\r\n"
			+ "       ,[LTDeliveryDate]\r\n"
			+ "       ,a.UserStatus\r\n"
			+ "       ,a.LabStatus\r\n"
			+ "       ,a.SaleOrder\r\n"
			+ "       ,a.SaleLine\r\n"
			+ "       ,CAST(NULL AS decimal(13,3)) as [POQty] \r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as [POUnit]\r\n"
			+ "       , CPP.IsExpired  \r\n"
			+ "       , PriorDistChannal\r\n"
			+ "       , PresetEndDate\r\n"
			+ "       , 0 as isRedye\r\n"
			+ "       , 0 AS isFC\r\n"
			+ "       , 1 as isPO\r\n"
			+ "       , CASE \r\n"
			+ "          WHEN CPP.ProductionOrder IS NOT NULL THEN 1 \r\n"
			+ "          ELSE 0 \r\n"
			+ "          END as isPSSpecial  \r\n"
			+ "       , a.CustomerMat\r\n"
			+ "       ,'' as Batch\r\n"
			+ "       ,0 AS HaveFirstLot\r\n"
			+ "       ,CAST(NULL AS int) as PlanInsteadId\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as POIdInstead\r\n"
			+ "       ,cast(null as date) as ForecastDateMonthBefore\r\n"
			+ "       ,cast(null as date) as ForecastDateMonthLast\r\n"
			+ "       ,0 as [POPuangId]\r\n"
			+ "       ,CAST(NULL AS NVARCHAR) as ForecastMY\r\n"
			+ "       , DueDate\r\n"
			+ "       ,DueDate as ForSortDueAndCustDue\r\n"
			+ "       , PlanGreigeDate\r\n"
			+ "       , GreigeInDate\r\n"
			+ "       ,CAST(a.[QuantityKG] as decimal(13,3))  as [QuantityKG]\r\n"
			+ "       ,a.LotNo\r\n"
			+ "       ,a.PlanningRemark\r\n"
			+ "       ,'' as ProductionOrderType\r\n"
			+ "       ,c.[StatusAfterDate]\r\n"
			+ " 	  ,CAST(NULL AS int) as [TempProdId]\r\n"
			+ "       ,'' AS ProductionOrderFirstLot\r\n"
			+ "      , c.[DyeSAPAfterFLDate]\r\n"
			+ "      , c.[DyeSAPAfterFLStatus]\r\n";
	private String fromGroupDetailIMGD = "" + "  FROM [PPMM].[dbo].[InputMainGroupDetail] as IMGD\r\n";

	private String innerJoinGroupDetailIINSGD =
			"" + "  INNER JOIN [PPMM].[dbo].[InputSubGroupDeail] as INSGD on IMGD.[GroupNo] = INSGD.[GroupNo]\r\n";
	private String innerJoinGroupDetailIASGD = ""
			+ "  inner join [PPMM].[dbo].[InputArticleSubGroupDetail] as IASGD on IASGD.[GroupNo] = INSGD.[GroupNo] and \r\n"
			+ "                                                                   IASGD.[SubGroup] = INSGD.[SubGroup]\r\n";
	private String leftJoinPOStatusACD = ""
			+ "	left join (\r\n"
			+ "      SELECT [Id]\r\n"
			+ "      ,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
			+ "      ,[CustomerShortName] as [CustomerName]\r\n"
			+ "      ,[ChangeDate]\r\n"
			+ "	   , CustomerType\r\n"
//			+ "	   ,  isSabina\r\n"
+ "      , case\r\n"
+ "          when [CustomerName] not like '%SABINA%' and [CustomerShortName] not like '%SBF%'\r\n"
+ "          then 0 \r\n"
+ "			 else 1 \r\n"
+ "        end as isSabina\r\n"
			+ "      FROM [PCMS].[dbo].[CustomerDetail]\r\n"
			+ "	) as cd on a.[CustomerNo] = cd.[CustomerNo] \r\n";
	private String leftJoinPOStatusBCD = ""
			+ "	left join (\r\n"
			+ "		SELECT [Id]\r\n"
			+ "		      ,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
			+ "		      ,[CustomerShortName] as [CustomerName]\r\n"
			+ "		      ,[ChangeDate]\r\n"
			+ "		      ,[CreateDate]\r\n"
			+ "		  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
			+ "	) as cd on b.[CustomerNo] = cd.[CustomerNo] \r\n";
	private String leftJoinLApp = ""
			+ "	left join (\r\n"
			+ "		select distinct a.[POId] ,[SORCFMDate] as LastSORCFMDate, a.[SORDueDate] as LastSORDueDate ,a.[ApprovedBy]  as LastApprovedBy,a.[ApprovedDate],a.[ApprovedDate] as [LastApprovedDate]\r\n"
			+ "		from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "		inner join (\r\n"
			+ "			select a.[POId] , max(a.[ApprovedDate]) as maxApprovedDate\r\n"
			+ "			from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "			where [DataStatus] = 'O' and\r\n"
			+ "               a.[SorDueDate] is not null \r\n"
			+ "			Group by a.[POId]\r\n"
			+ "		)as LApp on a.[POId] = LApp.[POId]  and\r\n"
			+ "                 a.[ApprovedDate] = LApp.[maxApprovedDate]\r\n"
			+ "		where a.[DataStatus] = 'O' and \r\n"
			+ "           a.[SorDueDate] is not null \r\n"
			+ "		)as LApp on a.[POId] = LApp.[POId]\r\n";
	private String leftJoinPOStatusMaxCFMAndDeliveryMain = ""
			+ "	left join (\r\n"
			+ "		select a.POId, MAX(c.LTCFMDate) as MaxLTCFMDate  , MAX(c.LTDeliveryDate) as MaxLTDeliveryDate  \r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "		inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "		where a.DataStatus = 'O' AND\r\n"
			+ "           c.DataStatus = 'O' and\r\n"
			+ "           a.POId is not null\r\n"
			+ "		group by a.POId  \r\n"
			+ "	) as maxDel on a.POId = maxDel.POId \r\n";
	private String leftJoinPOStatusMaxCFMAndDeliveryPuang = ""
			+ " left join (\r\n"
			+ "	  select a.POId, MAX(c.LTCFMDate) as MaxLTCFMDate , MAX(c.LTDeliveryDate) as MaxLTDeliveryDate  \r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "		inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "		where a.DataStatus = 'O' AND \r\n"
			+ "           c.DataStatus = 'O' and\r\n"
			+ "           a.POId is not null\r\n"
			+ "		group by  a.POId  \r\n"
			+ "	) as f on b.POId = f.POId  \r\n";
	private String leftJoinLAppPuang = ""
			+ "	left join (\r\n"
			+ "		select distinct a.[POId] ,[SORCFMDate] as LastSORCFMDate, a.[SORDueDate] as LastSORDueDate ,a.[ApprovedBy]  as LastApprovedBy,a.[ApprovedDate]\r\n"
			+ "		from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "		inner join (\r\n"
			+ "			select a.[POId] , max(a.[ApprovedDate]) as maxApprovedDate\r\n"
			+ "			from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "			where [DataStatus] = 'O' and a.[SorDueDate] is not null   \r\n"
			+ "			Group by  a.[POId]\r\n"
			+ "		)as LApp on a.[POId] = LApp.[POId] and\r\n"
			+ "                 a.[ApprovedDate] = LApp.[maxApprovedDate]\r\n"
			+ "		where [DataStatus] = 'O' and\r\n"
			+ "           a.[SorDueDate] is not null   \r\n"
			+ "		)as LApp on b.[Id] = LApp.[POId]\r\n";
	private String leftJoinPOStatusSTC =
			"" + "	LEFT JOIN [PPMM].[dbo].[StockCustomerDate] AS stc on a.CustomerNo = stc.CustomerNo \r\n";

//	private String leftJoinPOStatusSCMR =
//			"" + " left join [PPMM].[dbo].[SpecialCaseMR] as SCMR on a.Article = scmr.Article\r\n";
	private String leftJoinPOStatusSPDC = ""
			+ "  left join [PPMM].[dbo].[SOR_PODetailChange] as SPDC on a.[Id] = SPDC.[POId] AND\r\n"
			+ "                                                         SPDC.[DataStatus] = 'O' \r\n";
	private String joinSelectPODetail = ""
			+ "  SELECT \r\n"
			+ "		a.[Id]\r\n"
			+ "     ,a.[ReferenceId]\r\n"
			+ "		,a.[DocDate]\r\n"
			+ "		,a.[CustomerNo]\r\n"
			+ "    ,a.[CustomerName]\r\n"
			+ "    ,a.[PO]\r\n"
			+ "    ,a.[POLine]\r\n"
			+ "    ,a.[MaterialNoTWC] \r\n"
			+ "    ,case\r\n"
			+ "			when IAD.[ArticleReplaced] is not null and IAD.ArticleReplaced <> '' then IAD.[ArticleReplaced]\r\n"
			+ "   	 	else a.Article\r\n"
			+ "    	end as Article\r\n"
			+ "    ,a.[Design]\r\n"
			+ "    ,a.[ColorCustomer]\r\n"
			+ "    ,a.[Color]\r\n"
			+ "    ,a.[LabRef]\r\n"
			+ "    ,a.[Unit]\r\n"
			+ "    ,a.[CustomerDue]\r\n"
			+ "    ,a.[GreigePlan]\r\n"
			+ "    ,a.[ItemNote]\r\n"
			+ "    ,a.[ProductionMemo]\r\n"
			+ "    ,a.[ModelCode]\r\n"
			+ "    ,a.[LabRefLotNo]\r\n"
			+ "    ,a.[MaterialNo]\r\n"
			+ "    ,a.[PODate]\r\n"
			+ "    ,a.[UpdateBy]\r\n"
			+ "    ,a.[UpdateDate]\r\n"
			+ "    ,a.[IsUpdate]\r\n"
			+ "    ,a.[DistChannal]\r\n"
			+ "    ,a.[Division]\r\n"
			+ "    ,a.[RuleNo]\r\n"
			+ "    ,a.[DyeAfterCFM]\r\n"
			+ "    ,a.[DataStatus]\r\n"
			+ "    ,a.[DyeAfterGreigeInBegin]\r\n"
			+ "    ,a.[DyeAfterGreigeInLast]\r\n"
			+ "    ,a.[LastCFMDate]\r\n"
			+ "    , a.[ChangeBy]\r\n"
			+ "    , a.[ChangeDate]\r\n"
			+ "    , a.[CreateBy]\r\n"
			+ "    , a.[CreatedDate]\r\n"
			+ "    , a.[POPuangId]\r\n"
			+ "    , case\r\n"
			+ "			when SUBSTRING(a.[MaterialNo], 1, 1) = 'H' then 1\r\n"
			+ "			else 0\r\n"
			+ "			end as isHW\r\n"
			+ "	    ,case\r\n"
			+ "			WHEN a.[Color] = '' or a.[Color] is null THEN ''\r\n"
			+ "			WHEN LEFT(a.[Color], 2) = 'BL' THEN 'Black'\r\n"
			+ "			ELSE 'Color'\r\n"
			+ "		end as [ColorType]\r\n"
			+ "    , a.OrderQty \r\n"
			+ "    , CAST( a.[OrderQtyCal] AS DECIMAL(15, 2)) as [OrderQtyCal] \r\n"
			+ "    , case\r\n"
			+ "			when a.[OrderQtyCalLast] is not null and SPDC.OrderQty <> 0 then CAST( SPDC.OrderQty AS DECIMAL(15, 2)) \r\n"
			+ "			else CAST( a.[OrderQtyCalLast] AS DECIMAL(15, 2)) \r\n"
			+ "			end as [OrderQtyCalLast] \r\n"
			+ "		, case\r\n"
			+ "			when (OrderQtyCalLast%50) >= 1 and (OrderQtyCalLast%50) <= 9 then cast(1 as bit)\r\n"
			+ "			else cast(0 as bit)\r\n"
			+ "			end as isRecheck\r\n"
			+ "    , a.isGroupRecheck\r\n"
			+ "    , cd.CustomerType\r\n"
			+ "	   , case \r\n"
			+ "			when stc.StockReceive is not null and stc.StockReceive <> '' then stc.StockReceive\r\n"
			+ "			else 1\r\n"
			+ "			end as StockReceive   \r\n"
			+ "    , case \r\n"
			+ "			when DayOfMonth is not null and DayOfMonth <> '' then DayOfMonth\r\n"
//			+ "			else '25'\r\n"
			+ "			else ( SELECT [DayOfMonth] FROM [PPMM].[dbo].[StockDefaultDate] where [DataStatus] ='O' )\r\n" 
			+ "			end as DayOfMonth \r\n"
//			+ "    , isSabina\r\n"

			+ "      , isSabina\r\n"
			+ "    , a.[CustomerDue] as ForSortDueAndCustDue\r\n"
			+ " FROM [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ this.leftJoinPOStatusSPDC 
			+ this.leftJoinPOStatusSTC
			+ this.leftJoinPOStatusACD
			+ this.leftJoinIAD
			+ " where a.DataStatus = 'O' and \r\n"
			+ "      [IsAllLotOnprocess] = 1  \r\n";

	private String innerJoinPODetailB = " " + " inner join ( \r\n" + this.joinSelectPODetail + " ) as b on e.[POId] = b.[Id]\r\n";
	private String declareTempFromLotMainAP = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempFromLotMain') IS NOT NULL   \r\n"
			+ "    DROP TABLE #tempFromLotMain;"
			+ "     SELECT \r\n"
			+ this.selectPODetailOne
			+ "  into #tempFromLotMain \r\n"
			+ "  FROM [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ this.leftJoinPOStatusSPDC
			+ this.leftJoinPOStatusSTC
			+ this.leftJoinPOStatusACD
			+ this.leftJoinIAD
			+ " where a.DataStatus = 'O'  \r\n";
	private String leftJoinTempLotMainA = ""
			+ "	left join (\r\n"
			+ "		select a.*\r\n"
			+ "		, case\r\n"
			+ "			when SPECAL.SpecialType is null or SPECAL.Variable is null  then a.ProdOrderQty\r\n"
			+ "			when SPECAL.SpecialType = 'Divide' then a.ProdOrderQty*SPECAL.Variable\r\n"
			+ "			when SPECAL.SpecialType = 'Multiply' then a.ProdOrderQty/SPECAL.Variable\r\n"
			+ "			end as ProdOrderQtyCal\r\n"
			+ "     ,SPECAL.IsCotton\r\n"
			+ "     ,SPECAL.ArticleComment\r\n"
			+ "     ,SPECAL.Variable\r\n"
			+ "		from #tempPOMainNPOInstead as a\r\n"
			+ "     left join (\r\n"
			+ "	  		SELECT a.Id as POId,a.PO ,a.POLine ,c.SpecialType ,c.Variable ,b.IsCotton  ,C.ArticleComment\r\n"
			+ "	 		from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ "	  		left join [PPMM].[dbo].[InputArticleDetail] as b on a.Article = b.Article and\r\n"
			+ "                                                             b.[DataStatus] = 'O'\r\n"
			+ "	  		left join [PPMM].[dbo].[SpecialCaseMR] as c on b.SpecialCaseId = c.Id and\r\n"
			+ "                                                        c.DataStatus = 'O'\r\n"
			+ "			WHERE A.[DataStatus] = 'O' \r\n"
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
			+ "     ,SPECAL.IsCotton\r\n"
			+ "     ,SPECAL.ArticleComment\r\n"
			+ "     ,SPECAL.Variable\r\n"
			+ "		from (\r\n"
			+ "			select  a.[Id]\r\n" 
			+ "			, A.POId \r\n"
			+ "      	,a.[ForecastId] ,a.[PlanInsteadId] ,a.[RuleNo] ,a.[ColorType]\r\n"
			+ "      	,a.[ProductionOrder] ,a.[FirstLot] ,a.[ProdOrderQty] ,a.[GroupOptions]\r\n"
			+ "      	,a.[GroupBegin] ,a.[PPMMStatus] ,a.[DataStatus] ,a.[ChangeDate]\r\n"
			+ "      	,a.[ChangeBy] ,a.[CreateDate] ,a.[CreateBy] ,a.[Batch]\r\n"
			+ "			from #tempPOMainNPOInstead as a\r\n" 
			+ "			where ( a.POId is not null ) and\r\n"
			+ "					a.DataStatus = 'O'\r\n"
			+ "  	) as a\r\n"
			+ "     left join (\r\n"
			+ "	  		SELECT a.Id as POId,a.PO ,a.POLine ,c.SpecialType ,c.Variable ,b.IsCotton ,c.ArticleComment\r\n"
			+ "	 		from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ "	  		left join [PPMM].[dbo].[InputArticleDetail] as b on a.Article = b.Article and\r\n"
			+ "                                                             B.[DataStatus] = 'O'\r\n"
			+ "	  		left join [PPMM].[dbo].[SpecialCaseMR] as c on b.SpecialCaseId = c.Id and\r\n"
			+ "                                                        c.DataStatus = 'O' \r\n"
			+ "			WHERE A.[DataStatus] = 'O'\r\n"
			+ "  	) as SPECAL on a.POId = SPECAL.POId\r\n"
			+ " ) as a on a.POId = b.POId\r\n";
	private String declarePPMMReDye = ""
			+ " IF OBJECT_ID('tempdb..#tempPPMMReDye') IS NOT NULL   \r\n"
			+ "	   DROP TABLE #tempPPMMReDye;\r\n"
			+ " select distinct\r\n"
			+ "		rdd.Id as [RedyeId]\r\n"
			+ "		,RDD.[GroupBegin]\r\n"
			+ "		,RDD.[GroupOptions]\r\n"
			+ "	    ,RDD.[ProductionOrder] \r\n"
			+ "	    ,RDD.[QuantityMR]  \r\n"
			+ "	    ,RDD.[QuantityKG]  \r\n"
			+ "		,RDD.[ColorCustomer] \r\n"
			+ "		,RDD.[FGDesign] \r\n"
			+ "		,RDD.[CustomerDue]\r\n"
			+ "		,RDD.[Article]\r\n"
			+ "		,RDD.OperationEndDate \r\n"
			+ "     ,RDD.OperationStatus\r\n"
			+ "		,RDD.[Operation] \r\n"
			+ "		,RDD.[MaterialNumber] \r\n"
			+ "	  	,RDD.UserStatus\r\n"
			+ "	  	,RDD.LabStatus\r\n"
			+ "	  	,RDD.SaleOrder\r\n"
			+ "	  	,RDD.SaleLine  \r\n"
			+ "		,RDD.CustomerMat\r\n"
			+ "		, RDD.[PriorDistChannal]\r\n"
			+ "		, RDD.[CustomerName]\r\n"
			+ "		  ,RDD.PurchaseOrder\r\n"
			+ "		  ,rdd.DataStatus \r\n"
			+ "    , RDD.LotNo\r\n"
			+ "    ,rdd.POType\r\n"
			+ "    ,rdd.DueDate\r\n"
			+ "    ,rdd.[DueDate] as ForSortDueAndCustDue\r\n"
			+ " ,rdd.GreigePlan\r\n"
			+ "		,RDD.[PlanGreigeDate] as PlanGreigeDate\r\n"
			+ "		,RDD.[GreigeInDate] as GreigeInDate\r\n"
			+ "	   ,RDD.[ColorType]\r\n"
			+ "	   ,IPR.[PlanningRemark]\r\n"
			+ " into #tempPPMMReDye\r\n"
			+ " FROM [PPMM].[dbo].[PlanLotRedyeDetail] as rdd   \r\n"
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON RDD.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ " WHERE RDD.AdminStatus <> 'ForceClosed' AND RDD.DataStatus = 'O'; \r\n";
	private String fromPPMMReDyeForShowOnly = ""
			+ " FROM ( \r\n"
			+ " select distinct\r\n"
			+ "		rdd.Id as [RedyeId]\r\n"
			+ "		,RDD.[GroupBegin]\r\n"
			+ "		,RDD.[GroupOptions]\r\n"
			+ "	    ,RDD.[ProductionOrder] \r\n"
			+ "	    ,RDD.[QuantityMR]  \r\n"
			+ "	    ,RDD.[QuantityKG]  \r\n"
			+ "		,RDD.[ColorCustomer] \r\n"
			+ "		,RDD.[FGDesign] \r\n"
			+ "		,RDD.[CustomerDue]\r\n"
			+ "		,RDD.[Article]\r\n"
			+ "		,RDD.OperationEndDate \r\n"
			+ "     ,RDD.OperationStatus\r\n"
			+ "		,RDD.[Operation] \r\n"
			+ "		,RDD.[MaterialNumber] \r\n"
			+ "	  	,RDD.UserStatus\r\n"
			+ "	  	,RDD.LabStatus\r\n"
			+ "	  	,RDD.SaleOrder\r\n"
			+ "	  	,RDD.SaleLine  \r\n"
			+ "		,RDD.CustomerMat\r\n"
			+ "		, RDD.[PriorDistChannal]\r\n"
			+ "		, RDD.[CustomerName]\r\n"
			+ "		  ,RDD.PurchaseOrder\r\n"
			+ "		  ,rdd.DataStatus \r\n"
			+ "    , RDD.LotNo\r\n"
			+ "    ,rdd.POType\r\n"
			+ "    ,rdd.DueDate\r\n"
			+ "    ,rdd.[DueDate] as ForSortDueAndCustDue\r\n"
			+ " 	,rdd.GreigePlan\r\n"
			+ "		,RDD.[PlanGreigeDate] as PlanGreigeDate\r\n"
			+ "		,RDD.[GreigeInDate] as GreigeInDate\r\n"
			+ "	   ,RDD.[ColorType]\r\n"
			+ "	   ,IPR.[PlanningRemark]\r\n"
			+ " FROM [PPMM].[dbo].[PlanLotRedyeDetail] as rdd   \r\n"
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON RDD.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ " WHERE RDD.AdminStatus <> 'ForceClosed' AND \r\n"
			+ "       RDD.DataStatus = 'O'  \r\n"
			+ " ) as a\r\n";
	private String declarePPMMPOAdd = ""
			+ " IF OBJECT_ID('tempdb..#tempPPMMPOAdd') IS NOT NULL   \r\n"
			+ "	   DROP TABLE #tempPPMMPOAdd;\r\n"
			+ " select distinct\r\n"
			+ "		rdd.Id as [TempPOAddId]\r\n"
			+ "		,RDD.[GroupBegin]\r\n"
			+ "		,RDD.[GroupOptions]\r\n"
			+ "	    ,RDD.[ProductionOrder] \r\n"
			+ "	    ,RDD.[QuantityMR]  \r\n"
			+ "	    ,RDD.[QuantityKG]  \r\n"
			+ "		,RDD.[ColorCustomer] \r\n"
			+ "		,RDD.[FGDesign] \r\n"
			+ "		,RDD.[CustomerDue]\r\n"
			+ "		,RDD.[Article]\r\n"
			+ "		,RDD.OperationEndDate \r\n"
			+ "     ,RDD.OperationStatus\r\n"
			+ "		,RDD.[GreigeDue] \r\n"
			+ "		,RDD.[Operation] \r\n"
			+ "		,RDD.[MaterialNumber] \r\n"
			+ "	  	,RDD.UserStatus\r\n"
			+ "	  	,RDD.LabStatus\r\n"
			+ "	  	,RDD.SaleOrder\r\n"
			+ "	  	,RDD.SaleLine  \r\n"
			+ "		,RDD.CustomerMat\r\n"
			+ "		, RDD.[PriorDistChannal]\r\n"
			+ "		, RDD.[CustomerName]\r\n"
			+ "		  ,rdd.PurchaseOrder\r\n"
			+ "		  ,rdd.DataStatus \r\n"
			+ " , RDD.LotNo\r\n"
			+ " ,rdd.POType\r\n"
			+ " ,RDD.[DyeAfterGreigeInBegin]\r\n"
			+ " ,RDD.[DyeAfterGreigeInLast]\r\n"
			+ " ,RDD.[LastCFMDate]\r\n"
			+ " ,cast(null as date)  as FirstLotDate\r\n"
			+ " ,cast(null as NVARCHAR(50) ) as FirstLotGroupNo\r\n"
			+ " ,cast(null as NVARCHAR(50) ) as FirstLotSubGroup\r\n"
			+ " ,rdd.FirstLot\r\n"
			+ " ,rdd.DyeAfterCFM\r\n"
			+ " ,rdd.DueDate\r\n"
			+ " ,rdd.[DueDate] as ForSortDueAndCustDue\r\n"
			+ " ,rdd.GreigePlan\r\n"
			+ " ,RDD.[PlanGreigeDate] \r\n"
			+ " ,RDD.[GreigeInDate]  \r\n"
			+ " ,RDD.[ColorType]\r\n"
			+ " ,IPR.[PlanningRemark]\r\n"
			+ " into #tempPPMMPOAdd\r\n"
			+ " FROM [PPMM].[dbo].[PlanLotPOAddDetail] as rdd   \r\n"
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON RDD.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ " WHERE RDD.DataStatus = 'O' ; \r\n";
	private String fromPPMMPOAddForShowOnly = ""
			+ " FROM ( \r\n"
			+ " select distinct\r\n"
			+ "		rdd.Id as [TempPOAddId]\r\n"
			+ "		,RDD.[GroupBegin]\r\n"
			+ "		,RDD.[GroupOptions]\r\n"
			+ "	    ,RDD.[ProductionOrder] \r\n"
			+ "	    ,RDD.[QuantityMR]  \r\n"
			+ "	    ,RDD.[QuantityKG]  \r\n"
			+ "		,RDD.[ColorCustomer] \r\n"
			+ "		,RDD.[FGDesign] \r\n"
			+ "		,RDD.[CustomerDue]\r\n"
			+ "		,RDD.[Article]\r\n"
			+ "		,RDD.OperationEndDate \r\n"
			+ "     ,RDD.OperationStatus\r\n"
			+ "		,RDD.[GreigeDue] \r\n"
			+ "		,RDD.[Operation] \r\n"
			+ "		,RDD.[MaterialNumber] \r\n"
			+ "	  	,RDD.UserStatus\r\n"
			+ "	  	,RDD.LabStatus\r\n"
			+ "	  	,RDD.SaleOrder\r\n"
			+ "	  	,RDD.SaleLine  \r\n"
			+ "		,RDD.CustomerMat\r\n"
			+ "		, RDD.[PriorDistChannal]\r\n"
			+ "		, RDD.[CustomerName]\r\n"
			+ "		  ,rdd.PurchaseOrder\r\n"
			+ "		  ,rdd.DataStatus \r\n"
			+ " , RDD.LotNo\r\n"
			+ " ,rdd.POType\r\n"
			+ " ,RDD.[DyeAfterGreigeInBegin]\r\n"
			+ " ,RDD.[DyeAfterGreigeInLast]\r\n"
			+ " ,RDD.[LastCFMDate]\r\n"
			+ " ,cast(null as date)  as FirstLotDate\r\n"
			+ " ,cast(null as NVARCHAR(50) ) as FirstLotGroupNo\r\n"
			+ " ,cast(null as NVARCHAR(50) ) as FirstLotSubGroup\r\n"
			+ " ,rdd.FirstLot\r\n"
			+ " ,rdd.DyeAfterCFM\r\n"
			+ " ,rdd.DueDate\r\n"
			+ " ,rdd.[DueDate] as ForSortDueAndCustDue\r\n"
			+ " ,rdd.GreigePlan\r\n"
			+ " ,RDD.[PlanGreigeDate] \r\n"
			+ " ,RDD.[GreigeInDate]  \r\n"
			+ " ,RDD.[ColorType]\r\n"
			+ " ,IPR.[PlanningRemark]\r\n"
			+ " FROM [PPMM].[dbo].[PlanLotPOAddDetail] as rdd   \r\n"
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON RDD.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ " WHERE RDD.DataStatus = 'O' \r\n"
			+ " ) as a\r\n";

	private String leftJoinMasterRelationSORPOAndSO_MRSPAS =
			"" + " LEFT JOIN [PPMM].[dbo].[MasterRelationSORPOAndSO] AS MRSPAS ON MRSPAS.POId = a.POId\r\n";
	private String leftJoinMasterRelationSORPOAndSO_E_MRSPAS =
			"" + " LEFT JOIN [PPMM].[dbo].[MasterRelationSORPOAndSO] AS MRSPAS ON MRSPAS.POId = E.POId\r\n";
	private String declareTempLotMainAP = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempLotMain') IS NOT NULL   \r\n"
			+ "		DROP TABLE #tempLotMain;\r\n"
			+ "	select distinct \r\n"
			+ "		 a.[Id]  \r\n"
			+ "		,b.[Id] as POId \r\n"
			+ "     ,b.[ReferenceId]\r\n"
			+ "		,a.[ForecastId] ,a.[RuleNo] ,a.[ColorType] ,a.[ProductionOrder]\r\n"
			+ "		,a.[FirstLot] ,a.[ProdOrderQty] ,a.[GroupOptions] ,a.[GroupBegin] ,a.[PPMMStatus],a.CreateDate   \r\n"
			+ "		,c.PlanSystemDate ,c.PlanUserDate ,c.GroupNo ,c.SubGroup \r\n"
			+ "     , LFL.[FirstLotDate] \r\n"
			+ "		, c.[PlanBy]\r\n"
			+ "		, null as [FirstLotGroupNo]\r\n"
			+ "		, null as [FirstLotSubGroup],c.[Id] as TempPlanningId \r\n"
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
			+ "			when SPOPD.POId is not null then 'POMain Puang'\r\n"
			+ "			else 'PO'\r\n"
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
			+ "																			sr.StockReceive = b.StockReceive and \r\n"
			+ "                                                                         sr.[DataStatus] = 'O' \r\n"
			+ "						where ipdr.NormalWork = 'O' and \r\n"
			+ "							( ipdr.Date >= tmsd.SPMaxLTDeliveryDate and ipdr.Date <= tmsd.[SPMaxCusDue] ) \r\n"
			+ "						ORDER BY Date desc\r\n"
			+ "					)\r\n"
			+ "					, MaxLTDeliveryDate\r\n"
			+ "				)\r\n"
			+ " 	  WHEN MaxLTDeliveryDate <= b.[CustomerDue]  then \r\n"
			+ "			ISNULL (\r\n"
			+ "					( SELECT top 1 ipdr.Date \r\n"
			+ "						FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "						inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																			sr.StockReceive = b.StockReceive and \r\n"
			+ "                                                                         sr.[DataStatus] = 'O' \r\n"
			+ "						where ipdr.NormalWork = 'O' and \r\n"
			+ "							( ipdr.Date >= MaxLTDeliveryDate and\r\n"
			+ "                           ipdr.Date <= b.[CustomerDue] ) \r\n"
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
			+ "     ,bRPAP.BatchNo as Batch\r\n"
			+ "     ,case\r\n"
			+ "			when a.ArticleComment like '%FN ผ่ากลาง%'\r\n"
			+ "				then a.[ProdOrderQty] * 2\r\n"
			+ "			when a.ArticleComment like '%แถบ%'\r\n"
			+ "				then a.[ProdOrderQty] * a.Variable\r\n"
			+ "         else a.[ProdOrderQty]\r\n"
			+ "			end as QtyGreigeMR\r\n"
			+ "   , case\r\n"
			+ "			when isRecheck = cast(1 as bit) or isHW = 1 then b.[OrderQtyCalLast]\r\n"
			+ "			else ceiling(b.[OrderQtyCalLast] )  \r\n"
			+ "			end as OrderQtyCalLast \r\n"
			+ "     , case\r\n"
			+ "			when SPOPD.POId is not null then b.PO\r\n"
			+ "			else '' "
			+ "			end as POPuangMain\r\n"
			+ "     , case\r\n"
			+ "			when SPOPD.POId is not null then b.POLine\r\n"
			+ "			else '' "
			+ "			end as POLinePuangMain\r\n"
			+ "     , case\r\n"
			+ "			when SPOPD.POId is not null then b.[CustomerDue]\r\n"
			+ "			else '' "
			+ "			end as CustomerDuePuangMain\r\n"
			+ "		 ,bRPAP.[Volume] AS [VolumeFG]  \r\n"
			+ "      ,B.[PODate]\r\n"
			+ "      ,a.IsCotton\r\n"
			+ "      ,b.[Division],[StockReceive],DayOfMonth\r\n"
			+ "      ,isSabina \r\n"
			+ " ,b.Color\r\n"
			+ " ,b.LabRef\r\n"
			+ " ,b.LabRefLotNo \r\n"
			+ " , MaxLTCFMDate  AS PlanCFMDate\r\n"
			+ " , MaxLTDeliveryDate AS PlanDueDate\r\n"
			+ " ,b.ForSortDueAndCustDue\r\n"
			+ " ,IPR.PlanningRemark\r\n"
			+ " ,c.[StatusAfterDate]\r\n"
			+ " ,C.ProductionOrderFirstLot\r\n"
			+ " , c.[DyeSAPAfterFLDate]\r\n"
			+ "      , c.[DyeSAPAfterFLStatus]\r\n"
			+ "       ,e.OperationEndDate\r\n"
			+ "       ,case\r\n"
			+ "          when MRSPAS.[IsSapLotCreate] = 1 then 'Y' \r\n"
			+ "          ELSE 'N' \r\n"
			+ "          END as isInSap\r\n"
			+ "       ,e.UserStatus\r\n"
			+ "       ,e.LabStatus\r\n"
			+ ",MRSPAS.SaleOrder\r\n"
			+ ",MRSPAS.SaleLine\r\n"
			+ "	 into  #tempLotMain\r\n"
			+ " from #tempFromLotMain as b \r\n"
			+ this.leftJoinTempLotMainA
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON A.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ "	left join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.[Id] = c.TempProdId    \r\n"
			+ " left join [PPMM].[dbo].[InputArticleDetail] as iad on iad.Article = b.Article\r\n"
			+ " left join #tempMainSpecialDesign as tmsd on tmsd.[Id] = a.[POId]\r\n"
			+ " left join [PPMM].[dbo].[RelationPOAndProdOrder] as bRPAP on b.[Id] = bRPAP.[POId] and\r\n"
			+ "                                                             a.[Id] = bRPAP.[TempProdId] AND\r\n"
			+ "                                                             bRPAP.[DataStatus] = 'O' \r\n"
			+ this.leftJoinPOStatusBCD
			+ this.leftJoinLApp
			+ this.leftJoinSPOPDSamePOId
			+ this.leftJoinPOStatusMaxCFMAndDeliveryMain
			+ this.leftJoinLFL
			+ this.leftJoinDYEE
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
			+ "		    ( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )\r\n";
	private String declareTempFromLotSubAP = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempFromLotSub') IS NOT NULL   \r\n"
			+ "    DROP TABLE #tempFromLotSub;\r\n"
			+ "		SELECT \r\n"
			+ this.selectPODetailOne
			+ "     ,SPOPD.POId\r\n"
			+ "     ,SPOPD.PO AS POPuangMain\r\n"
			+ "     ,SPOPD.POLine AS POLinePuangMain\r\n"
			+ "     ,SPOPD.CustomerDue as CustomerDuePuangMain\r\n"
			+ " into #tempFromLotSub\r\n"
			+ " from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ this.innerJoinSPOPDNotSamePOId
//			+ this.leftJoinPOStatusSCMR
			+ this.leftJoinPOStatusSPDC
			+ this.leftJoinPOStatusSTC
			+ this.leftJoinPOStatusACD
			+ this.leftJoinIAD
			+ "  where a.DataStatus = 'O' and \r\n"
			+ "        SPOPD.DataStatus = 'O'   \r\n"
//			+ "		 and ( SPDC.[DataStatus] = 'O' OR SPDC.[DataStatus] IS NULL )\r\n"
	;

	private String declareTempLotSubAP = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempLotSub') IS NOT NULL   \r\n"
			+ "    DROP TABLE #tempLotSub;\r\n"
			+ "	select distinct \r\n"
			+ "		  a.[Id]  \r\n"
			+ "		, b.[Id] as  [POId]\r\n"
			+ "     ,b.[ReferenceId]\r\n"
			+ "		, a.[ForecastId] ,a.[RuleNo] ,a.[ColorType] ,a.[ProductionOrder]\r\n"
			+ "		, a.[FirstLot] ,a.[ProdOrderQty] ,a.[GroupOptions] ,a.[GroupBegin] ,a.[PPMMStatus],a.CreateDate   \r\n"
			+ "		, c.PlanSystemDate  ,c.PlanUserDate   ,c.GroupNo ,c.SubGroup \r\n"
			+ "     , LFL.[FirstLotDate] \r\n"
			+ "		, c.[PlanBy]\r\n"
			+ "		, null as [FirstLotGroupNo]\r\n"
			+ "		, null as [FirstLotSubGroup],c.[Id] as TempPlanningId \r\n"
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
			+ "     , b.[MaterialNoTWC] as CustomerMat \r\n"
			+ "     ,case\r\n"
			+ "       when LastSORDueDate is not null then LastSORDueDate\r\n"
			+ " 	  WHEN MaxLTDeliveryDate <= b.[CustomerDue]  then \r\n"
			+ "			ISNULL (\r\n"
			+ "					( SELECT top 1 ipdr.Date \r\n"
			+ "						FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "						inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																			sr.StockReceive = b.StockReceive and \r\n"
			+ "                                                                         sr.[DataStatus] = 'O' \r\n"
			+ "						where ipdr.NormalWork = 'O' and \r\n"
			+ "							( ipdr.Date >= MaxLTDeliveryDate and ipdr.Date <= b.[CustomerDue] ) \r\n"
			+ "						ORDER BY Date desc\r\n"
			+ "					)  \r\n"
			+ "					, MaxLTDeliveryDate"
			+ "				)\r\n"
			+ "       else MaxLTDeliveryDate\r\n"
			+ "       end as SORDueDate\r\n"
			+ "     ,case\r\n"
			+ "       	when LastSORCFMDate is not null then LastSORCFMDate\r\n"
			+ "       	else tlm.SORCFMDate\r\n"
			+ "       	end as SORCFMDate\r\n"
			+ "  ,LApp.ApprovedDate\r\n"
			+ "	 ,b.CustomerNo\r\n"
			+ "     ,bRPAP.BatchNo as Batch\r\n"
			+ "     ,case\r\n"
			+ "			when a.ArticleComment like '%FN ผ่ากลาง%'\r\n"
			+ "				then a.[ProdOrderQty] * 2\r\n"
			+ "			when a.ArticleComment like '%แถบ%'\r\n"
			+ "				then a.[ProdOrderQty] * a.Variable\r\n"
			+ "         else a.[ProdOrderQty]\r\n"
			+ "			end as QtyGreigeMR\r\n"
			+ "		,b.OrderQtyCalLast\r\n "
			+ "	 ,  POPuangMain \r\n"
			+ "	 ,  POLinePuangMain \r\n"
			+ "  , CustomerDuePuangMain\r\n"
			+ "		 ,bRPAP.[Volume] AS [VolumeFG]  \r\n"
			+ "      ,B.[PODate]\r\n"
			+ "      ,a.IsCotton\r\n"
			+ "      ,b.[Division],[StockReceive],DayOfMonth\r\n"
			+ "      ,isSabina\r\n"
			+ "	  ,b.Color\r\n"
			+ "   ,b.LabRef\r\n"
			+ "	  ,b.LabRefLotNo \r\n"
			+ "   , MaxLTCFMDate  AS PlanCFMDate\r\n"
			+ "   , MaxLTDeliveryDate AS PlanDueDate\r\n"
			+ "      ,b.ForSortDueAndCustDue\r\n"
			+ " ,IPR.PlanningRemark\r\n"
			+ " ,c.[StatusAfterDate]\r\n"
			+ " ,C.ProductionOrderFirstLot\r\n"
			+ " , c.[DyeSAPAfterFLDate]\r\n"
			+ "      , c.[DyeSAPAfterFLStatus]\r\n"
			+ "       ,e.OperationEndDate\r\n"
			+ "       ,case\r\n"
			+ "          when MRSPAS.[IsSapLotCreate] = 1 then 'Y' \r\n"
			+ "          ELSE 'N' \r\n"
			+ "          END as isInSap\r\n"
			+ "       ,e.UserStatus\r\n"
			+ "       ,e.LabStatus\r\n"
			+ ",MRSPAS.SaleOrder\r\n"
			+ ",MRSPAS.SaleLine\r\n"
			+ "	 into  #tempLotSub\r\n"
			+ " from #tempFromLotSub as b \r\n"
			+ this.leftJoinTempLotSubA
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON A.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ "	left join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId    \r\n"
			+ " left join [PPMM].[dbo].[InputArticleDetail] as iad on iad.Article = b.Article\r\n"
			+ " LEFT JOIN ( \r\n"
			+ "     SELECT [Article] ,[FormulaYD] ,[FormulaPC] ,[FormulaKG] ,[FormulaMR] \r\n"
			+ "		FROM [PPMM].[dbo].[InputConversionDetail]\r\n"
			+ "		where DataStatus = 'O'\r\n"
			+ " ) AS ICD ON ICD.Article = b.Article\r\n"
			+ " left join [PPMM].[dbo].[RelationPOAndProdOrder] as bRPAP on b.[Id] = bRPAP.[POId] and\r\n"
			+ "                                                             a.[Id] = bRPAP.[TempProdId] AND\r\n"
			+ "                                                             bRPAP.[DataStatus] = 'O' \r\n"
			+ this.leftJoinPOStatusBCD
			+ this.leftJoinLAppPuang
			+ this.leftJoinPOStatusMaxCFMAndDeliveryPuang
			+ this.leftJoinSPOPDB
			+ "	inner join (\r\n"
			+ "   select distinct POId , SORCFMDate \r\n"
			+ "	  from #tempLotMain \r\n"
			+ " )  as tlm on tlm.POId = SPOPD.POId\r\n"
			+ this.leftJoinLFL
			+ this.leftJoinDYEE
			+ this.leftJoinMasterRelationSORPOAndSO_MRSPAS
			+ "	where	( e.[DataStatus] = 'O' OR E.[DataStatus] is null ) AND\r\n"
			+ "			(	(  a.POId is not null and \r\n"
			+ "				( a.[DataStatus] = 'O' or a.[DataStatus] = 'P' )\r\n"
			+ "				)\r\n"
			+ "				or a.[DataStatus] is null  ) and \r\n"
			+ "			( B.[DataStatus] = 'O' ) AND\r\n"
			+ "			( C.[DataStatus] = 'O' OR c.[DataStatus] = 'P' OR C.[DataStatus] IS NULL )and\r\n"
			+ "			( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL ) \r\n";
	private String declareTempBeginDataAP = "\r\n"
			+ "IF OBJECT_ID('tempdb..#tempLot') IS NOT NULL   \r\n"
			+ "  DROP TABLE #tempLot;\r\n"
			+ " select * \r\n"
			+ " into #tempLot \r\n"
			+ " FROM (\r\n"
			+ "	 select * from #tempLotMain \r\n"
			+ "	 union ALL\r\n"
			+ "	 select * from  #tempLotSub\r\n "
			+ " ) AS A \r\n"
			+ " IF OBJECT_ID('tempdb..#tempBeginData') IS NOT NULL   \r\n"
			+ "   DROP TABLE #tempBeginData; \r\n"
			+ " SELECT a.* \r\n"
			+ "     ,HaveFirstLot\r\n"
			+ " 	, CASE \r\n"
			+ "		WHEN DayOfMonth = 'LastDayOfMonth' THEN EOMONTH(SORDueDate)\r\n"
			+ "		WHEN DayOfMonth is null or DayOfMonth = '' THEN \r\n"
			+ "			cast(\r\n"
			+ "				CAST( year(SORDueDate) as NVARCHAR(4)) + \r\n"
			+ "					RIGHT('00' + cast(month(SORDueDate) as NVARCHAR(2)),2)    +\r\n"
			+ "				RIGHT('00' + LTRIM( cast('25' as NVARCHAR(2))),2) AS Date)\r\n"
			+ "			ELSE cast(\r\n"
			+ "				CAST( year(SORDueDate) as NVARCHAR(4)) + \r\n"
			+ "					RIGHT('00' + cast(month(SORDueDate) as NVARCHAR(2)),2)    +\r\n"
			+ "				RIGHT('00' + LTRIM( cast(DayOfMonth as NVARCHAR(2))),2) AS Date)\r\n"
			+ "			END AS StockDate\r\n"
			+ " 	,dateadd(month,datediff(month,0,dateadd(month,1,SORDueDate)),0) as NextMonthSORDueDateWithOne\r\n"
			+ " 	, dateadd( month, datediff(month,0, SORDueDate ), 0) as SORDueDateWithOne\r\n"
			+ " INTO #tempBeginData\r\n"
			+ " FROM #tempLot AS A\r\n"
			+ " LEFT join [PPMM].[dbo].[InputDateRunning] as c on a.CustomerDue = c.Date \r\n"
			+ " left join ( \r\n"
			+ "   SELECT distinct POId,1 as HaveFirstLot\r\n"
			+ "   FROM #tempPOMainNPOInstead AS SUBSTP\r\n"
			+ "   where SUBSTP.DataStatus = 'O' and SUBSTP.[FirstLot] = 'Y' \r\n"
			+ " ) AS hfl on A.POId = hfl.POId\r\n";
	private String declareTempFromPO = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempFromPO') IS NOT NULL   \r\n"
			+ "    DROP TABLE #tempFromPO;  \r\n"
			+ " select A.*  \r\n"
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
			+ "   select a.*    \r\n"
			+ "	 ,case\r\n"
			+ "			when b.SORCFMDate is not null then b.SORCFMDate\r\n"
			+ "			else a.SORCFMDate\r\n"
			+ "			end as SORCFMDateLast \r\n"
			+ "	 ,case \r\n"
			+ "		when b.SORDueDate is not null then b.SORDueDate\r\n"
			+ "		when a.SORDueDate > StockDate then \r\n"
			+ "			( SELECT ipdr.Date \r\n"
			+ "				FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n" 
			+ "				inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																	sr.StockReceive = a.StockReceive and \r\n"
			+ "                                                                 sr.[DataStatus] = 'O' \r\n"
			+ "				LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "				where ipdr.NormalWork = 'O' and\r\n"
			+ "				      ipdr.Date >= a.NextMonthSORDueDateWithOne and\r\n"
			+ "					  ( tad.CountPRD is null or tad.CountPRD < 50 )\r\n"
			+ "         	ORDER BY Date\r\n"
			+ "				OFFSET 1 ROWS\r\n"
			+ "				FETCH NEXT 1 ROWS ONLY )\r\n"
			+ "		else \r\n"
			+ "			case \r\n "
			+ "				when a.SORDueDate < ( SELECT ipdr.Date \r\n"
			+ "										FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "										inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																							sr.StockReceive = a.StockReceive and\r\n"
			+ "																							sr.[DataStatus] = 'O'\r\n"
			+ "										LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "										where ipdr.NormalWork = 'O' and \r\n"
			+ "											  ipdr.Date >= a.SORDueDateWithOne \r\n"
			+ "										ORDER BY Date \r\n"
			+ "										OFFSET 1 ROWS\r\n"
			+ "										FETCH NEXT 1 ROWS ONLY\r\n"
			+ "										)  \r\n"
			+ "				then \r\n"
			+ "					( SELECT top 1 ipdr.Date \r\n"
			+ "						FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "						inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																	  		sr.StockReceive = a.StockReceive and\r\n"
			+ "																	  		sr.[DataStatus] = 'O'\r\n"
			+ "						LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "						where ipdr.NormalWork = 'O' and \r\n"
			+ "							  ipdr.Date >= a.SORDueDateWithOne and\r\n"
			+ "							  ipdr.Date >= a.SORDueDate  and \r\n"
			+ "							  ( tad.CountPRD is null or tad.CountPRD < 50 ) and\r\n"
			+ "							  ipdr.Date >= ( \r\n"
			+ "											SELECT ipdr.Date \r\n"
			+ "											FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "											inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																								sr.StockReceive = a.StockReceive and\r\n"
			+ "																								sr.[DataStatus] = 'O'\r\n"
			+ "											LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "											where ipdr.NormalWork = 'O' and \r\n"
			+ "											 	  ipdr.Date >= a.SORDueDateWithOne \r\n"
			+ "											ORDER BY Date \r\n"
			+ "											OFFSET 1 ROWS\r\n"
			+ "											FETCH NEXT 1 ROWS ONLY\r\n"
			+ "											)   \r\n"
			+ "						ORDER BY Date  \r\n"
			+ "						)\r\n"
			+ "				else\r\n"
			+ "					(\r\n"
			+ "						SELECT top 1 ipdr.Date \r\n"
			+ "						FROM [PPMM].[dbo].[InputDateRunning] as ipdr \r\n"
			+ "						inner join [PPMM].[dbo].[StockReceiveDate] as sr on ipdr.DateName = sr.DateName and \r\n"
			+ "																			sr.StockReceive = a.StockReceive and\r\n"
			+ "																			sr.[DataStatus] = 'O'\r\n"
			+ "						LEFT JOIN #tempApprovedDate AS tad on tad.SORDueDate = ipdr.Date\r\n"
			+ "						where ipdr.NormalWork = 'O' and \r\n"
			+ "							  ipdr.Date >= a.SORDueDateWithOne and \r\n"
			+ "							  ipdr.Date >= a.SORDueDate  and \r\n"
			+ "							  ( tad.CountPRD is null or tad.CountPRD < 50 ) \r\n"
			+ "						ORDER BY Date  \r\n"
			+ "						)\r\n"
			+ "			end \r\n"
			+ "		end as SORDueDateLast \r\n"
			+ "	 	from #tempBeginData as a  \r\n"
			+ "		left join [PPMM].[dbo].[ApprovedPlanDate] as b on b.POId = a.POId and \r\n"
			+ "														  b.[SorDueDate] is not null and\r\n"
			+ "													      b.[DataStatus] = 'O'\r\n "
			+ ") as a\r\n";

	private String declareTempApprovedDateAP = "\r\n"
			+ "IF OBJECT_ID('tempdb..#tempApprovedDate') IS NOT NULL   \r\n"
			+ "	DROP TABLE #tempApprovedDate;  \r\n"
			+ "SELECT C.SORDueDate , COUNT(B.ProductionOrder ) AS CountPRD\r\n"
			+ "  INTO #tempApprovedDate\r\n"
			+ "  FROM [PPMM].[dbo].[SOR_PODetail] AS A\r\n"
			+ "  INNER JOIN #tempPOMainNPOInstead as b on a.id = b.POId\r\n"
			+ "  inner join [PPMM].[dbo].[ApprovedPlanDate] as c on c.POId = b.POId\r\n"
			+ "  where a.DataStatus = 'O' and\r\n"
			+ "			b.DataStatus = 'O' and\r\n"
			+ "			c.DataStatus = 'O' and\r\n"
			+ "			c.[SorDueDate] is not null \r\n"
			+ "  GROUP BY C.SORDueDate \r\n";
	private String declareTempBeginSpecialDesign = "\r\n"
			+ "  IF OBJECT_ID('tempdb..#tempBeginSpecialDesign') IS NOT NULL \r\n"
			+ "	DROP TABLE #tempBeginSpecialDesign; \r\n"
			+ "	 \r\n"
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
			+ "	WHERE ( SCDO.DesignCPOne IS NOT NULL OR SCTW.DesignCPTwo IS NOT NULL ) AND\r\n"
			+ "       a.DataStatus = 'O'   \r\n"
			+ "	order by a.PO ,a.POLine,a.CustomerDue\r\n";
	private String declareTempMaxPlan = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempMaxPlan') IS NOT NULL \r\n"
			+ "	DROP TABLE #tempMaxPlan ; \r\n"
			+ "	 SELECT a.Id,A.PO , A.POLine,A.CustomerDue,SpecailDesignId,MaxLTCFMDate,MaxLTDeliveryDate, LastSORCFMDate AS SPLastSORCFMDate, LastSORDueDate AS SPLastSORDueDate\r\n"
			+ "		into #tempMaxPlan\r\n"
			+ "		FROM #tempBeginSpecialDesign AS A\r\n"
			+ "		left join (\r\n"
			+ "				select a.POId, MAX(c.LTDeliveryDate) as MaxLTDeliveryDate , MAX(c.LTCFMDate) as MaxLTCFMDate  \r\n"
			+ "				from #tempPOMainNPOInstead as a\r\n"
			+ "				inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "				where a.DataStatus = 'O' AND c.DataStatus = 'O' and a.POId is not null\r\n"
			+ "				group by  a.POId  \r\n"
			+ "			) as b on a.Id = b.POId\r\n"
			+ "	left join (\r\n"
			+ "		select distinct a.[POId] ,[SORCFMDate] as LastSORCFMDate, a.[SORDueDate] as LastSORDueDate ,a.[ApprovedBy]  as LastApprovedBy,a.[ApprovedDate]\r\n"
			+ "		from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "		inner join (\r\n"
			+ "			select a.[POId] , max(a.[ApprovedDate]) as maxApprovedDate\r\n"
			+ "			from [PPMM].[dbo].[ApprovedPlanDate] as a\r\n"
			+ "			where \r\n"
			+ "			a.DataStatus = 'O' and\r\n"
			+ "			a.[SorDueDate] is not null \r\n"
			+ "			Group by  a.[POId]\r\n"
			+ "		)as LApp on a.[POId] = LApp.[POId]  and a.[ApprovedDate] = LApp.[maxApprovedDate]\r\n"
			+ "		where [DataStatus] = 'O' and \r\n"
			+ "			  a.[SorDueDate] is not null \r\n"
			+ "	) as LApp on a.Id = LApp.[POId]\r\n"
			+ " where MaxLTCFMDate is not null    \r\n"
			+ "	order by a.PO ,a.POLine,a.CustomerDue\r\n";
	private String declareTempMainSpecialDesign = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempMainSpecialDesign') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempMainSpecialDesign; \r\n"
			+ "	select a.Id  ,SPMaxCusDue,SPMaxLTCFMDate ,SPMaxLTDeliveryDate,b.SPLastSORCFMDate,b.SPLastSORDueDate\r\n"
			+ "	into #tempMainSpecialDesign\r\n"
			+ "	from (\r\n"
			+ " 	select mainA.Id\r\n"
			+ " 	,(select top 1 CustomerDue\r\n"
			+ "	 		from #tempMaxPlan as a\r\n "
			+ "	 		where a.PO = mainA.PO and a.SpecailDesignId = mainA.SpecailDesignId\r\n"
			+ "	 		order by CustomerDue desc\r\n"
			+ "	 	)  as SPMaxCusDue\r\n"
			+ " 	,(select top 1 MaxLTCFMDate\r\n"
			+ "	 		from #tempMaxPlan as a\r\n"
			+ "	 		where a.PO = mainA.PO and a.CustomerDue = mainA.CustomerDue and a.SpecailDesignId = mainA.SpecailDesignId\r\n"
			+ "	 		order by MaxLTCFMDate desc\r\n"
			+ "	 	)  as SPMaxLTCFMDate\r\n"
			+ " 	,(select top 1 MaxLTDeliveryDate\r\n"
			+ "	 		from #tempMaxPlan as a\r\n"
			+ "	 		where a.PO = mainA.PO and a.CustomerDue = mainA.CustomerDue and a.SpecailDesignId = mainA.SpecailDesignId\r\n"
			+ "	 		order by MaxLTDeliveryDate desc\r\n"
			+ "	 	)  as SPMaxLTDeliveryDate\r\n"
			+ "	 	from #tempBeginSpecialDesign as mainA\r\n"
			+ "	) as a \r\n"
			+ "	inner join #tempMaxPlan as b on a.Id = b.Id\r\n"
			+ "	where a.SPMaxLTCFMDate is not null\r\n";
	private String declarePOMainNPOInstead = "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempPOMainNPOInstead') IS NOT NULL \r\n"
			+ "	DROP TABLE #tempPOMainNPOInstead;  \r\n"
			+ "	select  stp.[Id]\r\n"
			+ "       ,e.POId\r\n"
			+ "       ,stp.id as TempProdId\r\n"
			+ "       ,stp.[ProductionOrderType]\r\n"
			+ "       ,stp.[ForecastId]\r\n"
			+ "       ,stp.[PlanInsteadId]\r\n"
			+ "       ,stp.[RuleNo]\r\n"
			+ "       ,stp.[ColorType]\r\n"
			+ "       ,stp.[ProductionOrder]\r\n"
			+ "       ,stp.[FirstLot]\r\n"
			+ "       ,stp.[ProdOrderQty]\r\n"
			+ "       ,stp.[GroupOptions]\r\n"
			+ "       ,stp.[GroupBegin]\r\n"
			+ "       ,stp.[PPMMStatus]\r\n"
			+ "       ,stp.[DataStatus]\r\n"
			+ "       ,stp.[ChangeDate]\r\n"
			+ "       ,stp.[ChangeBy]\r\n"
			+ "       ,stp.[CreateDate]\r\n"
			+ "       ,stp.[CreateBy]\r\n"
			+ "       ,stp.[Batch]\r\n"
			+ "	INTO #tempPOMainNPOInstead\r\n"
			+ "	from [PPMM].[dbo].[SOR_TempProd] as stp\r\n"
			+ "	INNER JOIN [PPMM].[dbo].[PlanLotSORDetail] as e on stp.Id = e.TempProdId\r\n"
			+ "	INNER JOIN [PPMM].[dbo].[SOR_PODetail] as spd on spd.Id = e.POId and\r\n"
			+ "                                                  SPD.[DataStatus] = 'O' \r\n"
			+ "	where ( stp.POId is not null ) and\r\n"
			+ "			stp.DataStatus = 'O' AND\r\n"
			+ "			SPD.IsAllLotOnprocess = 1 \r\n";

	private String leftJoinTempPOF = "\r\n"
			+ "		left join (\r\n"
			+ "			    select a.POId, MAX(c.[LTCFMDate]) as MaxLTCFMDate, MAX(c.[LTDeliveryDate]) as MaxLTDeliveryDate\r\n"
			+ "				from #tempPOMainNPOInstead as a\r\n"
			+ "				inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId\r\n"
			+ "				where a.DataStatus = 'O' AND c.DataStatus = 'O' and a.POId is not null\r\n"
			+ "				group by  a.POId\r\n"
			+ "		 ) as f on a.POId = f.POId\r\n"; 
	private String leftJoinTempPOAPD = "\r\n"
			+ "			left join [PPMM].[dbo].[ApprovedPlanDate] as apd on a.[POId] = apd.[POId] and\r\n"
			+ "															     apd.[IsFirst] = 'true' and\r\n"
			+ "															     apd.DataStatus = 'O' and\r\n"
			+ "					                                             apd.[SorDueDate] is not null\r\n";

	private String leftJoinTempPOCPP =
			"\r\n" + "			left join [PPMM].[dbo].[TEMP_ProdPreset] as CPP on a.ProductionOrder = CPP.[ProductionOrder]\r\n";

	private String leftJoinbRPAP = ""
			+ " left join [PPMM].[dbo].[RelationPOAndProdOrder] as bRPAP on a.[Id] = bRPAP.[TempProdId] AND bRPAP.[DataStatus] = 'O' \r\n";
	private String innerJoinbRPAP = "\r\n"
			+ " inner join [PPMM].[dbo].[RelationPOAndProdOrder] as bRPAP on a.[Id] = bRPAP.[TempProdId] AND bRPAP.[DataStatus] = 'O' \r\n";
	private String declareBegin = "" + this.declarePOMainNPOInstead;
	private String declareTempPO = ""
			+ " IF OBJECT_ID('tempdb..#tempFromPO') IS NOT NULL   \r\n"
			+ "		DROP TABLE #tempFromPO; \r\n"
			+ " select distinct \r\n"
			+ "		a.*,c.PlanSystemDate  ,c.PlanUserDate   ,c.GroupNo ,c.SubGroup\r\n"
			+ "     ,LFL.[FirstLotDate]\r\n"
			+ "     ,NULL AS [FirstLotGroupNo], NULL AS [FirstLotSubGroup]\r\n"
			+ "		, c.[PlanBy]\r\n"
			+ "      ,c.[Id] as TempPlanningId \r\n"
			+ "		, b.[ColorCustomer] ,b.[Design] ,b.[CustomerDue] ,b.[Article] ,b.[CustomerName] ,b.[DyeAfterGreigeInBegin]\r\n"
			+ "     , b.[LastCFMDate] ,b.[DyeAfterGreigeInLast] ,b.[MaterialNo] ,b.[PO] ,b.[POLine] ,b.DyeAfterCFM ,b.[GreigePlan] \r\n"
			+ "     , case \r\n"
			+ "			when SPOPD.OrderQty is null then b.[OrderQty]\r\n"
			+ "			else SPOPD.OrderQty\r\n"
			+ "			end as [POQty] \r\n"
			+ "     , b.[Unit] as [POUnit] \r\n"
			+ "     , c.[LTPOInputDate] ,c.[LTMakeLotDate] ,c.[LTBCDate] ,c.[LTPlanDate] ,c.[LTCFMDate]\r\n"
			+ "     , c.[LTCFMAnswerDate] ,c.[LTDeliveryDate]\r\n"
			+ "		, case \r\n"
			+ "				when b.[DistChannal] = 'EX' then 2\r\n"
			+ "				when b.[DistChannal] = 'HW' then\r\n"
			+ "					case \r\n"
			+ "						when b.CustomerType = 'EX' then 2\r\n"
			+ "						else 1\r\n"
			+ "						end\r\n"
			+ "				ELSE 0\r\n"
			+ "		  end as PriorDistChannal\r\n"
			+ "      ,b.MaterialNoTWC as CustomerMat \r\n"
			+ "      ,b.[Division]\r\n"
			+ "      ,CAST(NULL AS NVARCHAR) as POIdInstead\r\n"
			+ "	  ,b.[POPuangId]\r\n"
			+ "	  ,ForSortDueAndCustDue\r\n"
			+ "   ,IPR.PlanningRemark\r\n"
			+ "   ,c.[StatusAfterDate]\r\n"
			+ "   ,C.ProductionOrderFirstLot\r\n"
			+ "   , c.[DyeSAPAfterFLDate]\r\n"
			+ "      , c.[DyeSAPAfterFLStatus]\r\n"
			+ "   ,CASE\r\n"
			+ "		WHEN a.[ProductionOrderType] = '>6m' THEN 1\r\n"
			+ "     ELSE 0\r\n"
			+ "    END AS HaveFirstLot\r\n"
			+ "       ,e.OperationEndDate\r\n"
			+ "       ,case\r\n"
			+ "          when MRSPAS.[IsSapLotCreate] = 1 then 'Y' \r\n"
			+ "          ELSE 'N' \r\n"
			+ "          END as isInSap\r\n"
			+ "       ,e.UserStatus\r\n"
			+ "       ,e.LabStatus\r\n"
			+ " ,CASE \r\n"
			+ "		WHEN C.[DataStatus] = 'O' THEN 'PO'\r\n"
			+ "		ELSE 'POTMP'\r\n"
			+ "		END AS LotType\r\n"
			+ ",MRSPAS.SaleOrder\r\n"
			+ ",MRSPAS.SaleLine\r\n"
			+ ",MaxLTDeliveryDate \r\n"
			+ ",MaxLTCFMDate \r\n"
			+ ",apd.[IsFirst] as IsApproved\r\n"
			+ ",apd.[SORDueDate] \r\n"
			+ ", CPP.IsExpired  \r\n"
			+ ", CASE \r\n"
			+ "    WHEN CPP.ProductionOrder IS NOT NULL THEN 1 \r\n"
			+ "    ELSE 0 \r\n"
			+ "    END as isPSSpecial  \r\n"
			+ " , CPP.PresetEndDate\r\n"
			+ " into #tempFromPO \r\n"
			+ " from [PPMM].[dbo].[SOR_TempProd] as a\r\n"
			+ this.innerJoinDYEE
			+ this.innerJoinPODetailB
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON A.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ " left join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ this.leftJoinSPOPDB
			+ this.leftJoinE_LFL
			+ this.leftJoinMasterRelationSORPOAndSO_E_MRSPAS
			+ this.leftJoinTempPOF
			+ this.leftJoinTempPOAPD
			+ this.leftJoinTempPOCPP
			+ " where ( e.[DataStatus] = 'O' OR E.[DataStatus] is null ) AND\r\n"
			+ "		  a.POId is not null and \r\n"
			+ "       a.[DataStatus] = 'O' and \r\n"
			+ "		  ( C.[DataStatus] = 'O' OR C.[DataStatus] IS NULL )and\r\n"
			+ "	      ( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )  \r\n";
	private String declareTempPOV2 = ""
			+ " IF OBJECT_ID('tempdb..#tempFromPO') IS NOT NULL   \r\n"
			+ "		DROP TABLE #tempFromPO; \r\n"
			+ " select distinct a.*\r\n"
			+ "                ,c.PlanSystemDate\r\n"
			+ "                ,c.PlanUserDate\r\n"
			+ "                ,c.GroupNo\r\n"
			+ "                ,c.SubGroup\r\n"
			+ "                ,LFL.[FirstLotDate]\r\n"
			+ "                ,NULL                  AS [FirstLotGroupNo]\r\n"
			+ "                ,NULL                  AS [FirstLotSubGroup]\r\n"
			+ "                ,c.[PlanBy]\r\n"
			+ "                ,c.[Id]                as TempPlanningId\r\n"
			+ "                ,b.[ColorCustomer]\r\n"
			+ "                ,b.[Design]\r\n"
			+ "                ,b.[CustomerDue]\r\n"
			+ "                ,b.[Article]\r\n"
			+ "                ,b.[CustomerName]\r\n"
			+ "                ,b.[DyeAfterGreigeInBegin]\r\n"
			+ "                ,b.[LastCFMDate]\r\n"
			+ "                ,b.[DyeAfterGreigeInLast]\r\n"
			+ "                ,b.[MaterialNo]\r\n"
			+ "                ,b.[PO]\r\n"
			+ "                ,b.[POLine]\r\n"
			+ "                ,b.DyeAfterCFM\r\n"
			+ "                ,b.[GreigePlan]\r\n"
			+ "                ,case\r\n"
			+ "                   when SPOPD.OrderQty is null then b.[OrderQty]\r\n"
			+ "                   else SPOPD.OrderQty\r\n"
			+ "                 end                   as [POQty]\r\n"
			+ "                ,b.[Unit]              as [POUnit]\r\n"
			+ "                ,c.[LTPOInputDate]\r\n"
			+ "                ,c.[LTMakeLotDate]\r\n"
			+ "                ,c.[LTBCDate]\r\n"
			+ "                ,c.[LTPlanDate]\r\n"
			+ "                ,c.[LTCFMDate]\r\n"
			+ "                ,c.[LTCFMAnswerDate]\r\n"
			+ "                ,c.[LTDeliveryDate]\r\n"
			+ "                ,case\r\n"
			+ "                   when b.[DistChannal] = 'EX' then 2\r\n"
			+ "                   when b.[DistChannal] = 'HW' then\r\n"
			+ "                     case\r\n"
			+ "                       when b.CustomerType = 'EX' then 2\r\n"
			+ "                       else 1\r\n"
			+ "                     end\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 end                   as PriorDistChannal\r\n"
			+ "                ,b.MaterialNoTWC       as CustomerMat\r\n"
			+ "                ,b.[Division]\r\n"
			+ "                ,Cast(NULL AS NVARCHAR) as POIdInstead\r\n"
			+ "                ,b.[POPuangId]\r\n"
			+ "                ,ForSortDueAndCustDue\r\n"
			+ "                ,IPR.PlanningRemark\r\n"
			+ "                ,c.[StatusAfterDate]\r\n"
			+ "                ,C.ProductionOrderFirstLot\r\n"
			+ "                ,c.[DyeSAPAfterFLDate]\r\n"
			+ "                ,c.[DyeSAPAfterFLStatus]\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN a.[ProductionOrderType] = '>6m' THEN 1\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 END                   AS HaveFirstLot\r\n"
			+ "                ,e.OperationEndDate\r\n"
			+ "                ,case\r\n"
			+ "                   when MRSPAS.[IsSapLotCreate] = 1 then 'Y'\r\n"
			+ "                   ELSE 'N'\r\n"
			+ "                 END                   as isInSap\r\n"
			+ "                ,e.UserStatus\r\n"
			+ "                ,e.LabStatus\r\n"
			+ "                ,MRSPAS.SaleOrder\r\n"
			+ "                ,MRSPAS.SaleLine\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN C.[DataStatus] = 'O' THEN 'PO'\r\n"
			+ "                   ELSE 'POTMP'\r\n"
			+ "                 END                   AS LotType\r\n"
			+ "                ,MaxLTDeliveryDate\r\n"
			+ "                ,MaxLTCFMDate\r\n"
			+ "                ,apd.[IsFirst]         as IsApproved\r\n"
			+ "                ,apd.[SORDueDate]\r\n"
			+ "                ,CPP.IsExpired\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN CPP.ProductionOrder IS NOT NULL THEN 1\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 END                   as isPSSpecial\r\n"
			+ "                ,CPP.PresetEndDate\r\n"
			+ " into #tempFromPO \r\n"
			+ " from [PPMM].[dbo].[SOR_TempProd] as a\r\n"
			+ this.innerJoinDYEE
			+ this.innerJoinPODetailB
			+ " LEFT JOIN [PPMM].[dbo].[InputPlanningRemark] AS IPR ON A.[ProductionOrder] = IPR.[ProductionOrder]\r\n"
			+ " left join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ this.leftJoinSPOPDB
			+ this.leftJoinE_LFL
			+ this.leftJoinMasterRelationSORPOAndSO_E_MRSPAS
			+ this.leftJoinTempPOF
			+ this.leftJoinTempPOAPD
			+ this.leftJoinTempPOCPP
			+ " where ( e.[DataStatus] = 'O' OR E.[DataStatus] is null ) AND\r\n"
			+ "		  a.POId is not null and \r\n"
			+ "       a.[DataStatus] = 'O' and \r\n"
			+ "		  ( C.[DataStatus] = 'O' OR C.[DataStatus] = 'P' OR C.[DataStatus] IS NULL )and\r\n"
			+ "	      ( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )  \r\n";
	private String declarePO = "" + this.declareBegin + this.declareTempPO;
	private String declarePOV2 = "" + this.declareBegin + this.declareTempPOV2;
	private String declareFC = ""

			+ "  IF OBJECT_ID('tempdb..#tempFCDate') IS NOT NULL \r\n"
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
			+ "					(SELECT TOP 1 [DayOfMonth]+1  FROM [PPMM].[dbo].[StockDefaultDate] WHERE DataStatus = 'O') AS NVARCHAR(2))\r\n"
			+ "			)\r\n"
			+ "		)\r\n"
			+ "	ELSE \r\n"
			+ "		DATEADD(\r\n"
			+ "			MONTH,-2,convert(\r\n"
			+ "				date ,right(a.ForecastMY,4) + \r\n"
			+ "				left(a.ForecastMY,2) + \r\n"
			+ "				CAST(\r\n"
			+ "					(SELECT TOP 1 [DayOfMonth]+1  FROM [PPMM].[dbo].[StockDefaultDate] WHERE DataStatus = 'O') AS NVARCHAR(2))\r\n"
			+ "			)\r\n"
			+ "		)\r\n"
			+ "	END AS ForecastDateMonthBefore \r\n"
			+ ",CASE \r\n"
			+ "	WHEN DistChannel = 'EX' \r\n"
			+ "		THEN DATEADD(\r\n"
			+ "			MONTH,-2,convert(\r\n"
			+ "				date ,right(a.ForecastMY,4) + \r\n"
			+ "				left(a.ForecastMY,2) + \r\n"
			+ "				CAST(\r\n"
			+ "					(SELECT TOP 1 [DayOfMonth]  FROM [PPMM].[dbo].[StockDefaultDate] WHERE DataStatus = 'O') AS NVARCHAR(2))\r\n"
			+ "			)\r\n"
			+ "		)\r\n"
			+ "	ELSE \r\n"
			+ "		DATEADD(\r\n"
			+ "			MONTH,-1,convert(\r\n"
			+ "				date ,right(a.ForecastMY,4) + \r\n"
			+ "				left(a.ForecastMY,2) + \r\n"
			+ "				CAST(\r\n"
			+ "					(SELECT TOP 1 [DayOfMonth]  FROM [PPMM].[dbo].[StockDefaultDate] WHERE DataStatus = 'O') AS NVARCHAR(2))\r\n"
			+ "			)\r\n"
			+ "		)\r\n"
			+ "	END AS ForecastDateMonthLast \r\n"
			+ " ,[RemainNonBLQty]\r\n"
			+ " ,[RemainBLQty]\r\n"
			+ "\r\n"
			+ " ,[POId]\r\n"
			+ ",[ForecastId]  \r\n"
			+ ",[ColorType]\r\n"
			+ ",[ProductionOrder]\r\n"
			+ ",[FirstLot]\r\n"
			+ ",[ProdOrderQty]\r\n"
			+ ",[GroupOptions] \r\n"
			+ ",[PPMMStatus] \r\n"
			+ "	,CAST(  ROW_NUMBER() OVER ( PARTITION BY [ForecastId]  ORDER BY [ForecastId] , [ProductionOrder] )as NVARCHAR(max))  as [BatchFirst] \r\n"
			+ " into #tempFCDate \r\n"
			+ " from [PPMM].[dbo].[SOR_ForecastDetail]  AS A \r\n"
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
			+ "	 FROM [PCMS].[dbo].[CustomerDetail] \r\n"
			+ " ) AS B ON A.CustomerNo = b.CustomerNo\r\n"
			+ " INNER JOIN [PPMM].[dbo].[SOR_TempProd] AS STP ON STP.[ForecastId] = A.[Id]\r\n"
			+ " where A.[DataStatus] = 'O' and \r\n"
			+ "	   STP.[DataStatus] = 'O' and a.[ForecastMY] is not null  and [ForecastDateLast] >= GETDATE() AND\r\n"
			+ "	   A.[RemainNonBLQty] <> 0 AND A.[RemainBLQty] <> 0; \r\n"
			+ "\r\n"
			+ " IF OBJECT_ID('tempdb..#tempFromFC') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempFromFC ; \r\n"
			+ "  SELECT a.*\r\n"
			+ "		,BatchFirst+'/'+cast(BatchLast as NVARCHAR(max)) as Batch \r\n"
			+ "		,c.PlanSystemDate  ,c.PlanUserDate ,c.GroupNo ,c.SubGroup  ,c.[PlanBy]\r\n"
			+ "     , c.[Id] as TempPlanningId \r\n"
			+ "     , MaxLTDeliveryDate,MaxLTCFMDate ,[LTPOInputDate] ,[LTMakeLotDate] ,[LTBCDate]\r\n"
			+ "     , [LTPlanDate] ,[LTCFMDate] ,[LTCFMAnswerDate]  ,[LTDeliveryDate]\r\n"
			+ "		, 0 as PriorDistChannal\r\n"
			+ "	 into #tempFromFC\r\n"
			+ "  from #tempFCDate as a\r\n"
			+ "  inner join ( \r\n"
			+ "	  	select forecastId ,max(cast( BatchFirst as int)) as BatchLast\r\n"
			+ "	  	from #tempFCDate\r\n"
			+ "	  	group by forecastId\r\n"
			+ "  ) as b on a.ForecastId = b.ForecastId \r\n"
			+ "  left join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "  left join (\r\n"
			+ " 	select a.ForecastId, MAX(c.[LTCFMDate]) as MaxLTCFMDate, MAX(c.LTDeliveryDate) as MaxLTDeliveryDate  \r\n"
			+ "		from [PPMM].[dbo].[SOR_TempProd] as a\r\n"
			+ "		inner join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ "		where a.DataStatus = 'O' AND c.DataStatus = 'O' and a.ForecastId is not null and c.LTDeliveryDate is not null\r\n"
			+ "		group by  a.ForecastId  \r\n"
			+ "	 ) as f on a.ForecastId = f.ForecastId \r\n"
			+ "  where a.ForecastId is not null ;\r\n";
	private String declareFCForShowOnly = ""
			+ " IF OBJECT_ID('tempdb..#tempFCTempProd') IS NOT NULL \r\n"
			+ "		DROP TABLE #tempFCTempProd ;\r\n"
			+ " SELECT A.[Id]\r\n"
			+ "       ,[POId]\r\n"
			+ "       ,[ForecastId]\r\n"
			+ "       ,[ColorType]\r\n"
			+ "       ,A.[ProductionOrder]\r\n"
			+ "       ,[FirstLot]\r\n"
			+ "       ,[ProdOrderQty]\r\n"
			+ "       ,[GroupOptions]\r\n"
			+ "       ,[PPMMStatus]\r\n"
			+ "       ,a.[DataStatus]\r\n"
			+ "       ,a.[ChangeDate]\r\n"
			+ "       ,a.[ChangeBy]\r\n"
			+ "       ,[LTPOInputDate]\r\n"
			+ "       ,[LTMakeLotDate]\r\n"
			+ "       ,[LTBCDate]\r\n"
			+ "       ,[LTPlanDate]\r\n"
			+ "       ,[LTCFMDate]\r\n"
			+ "       ,[LTCFMAnswerDate]\r\n"
			+ "       ,[LTDeliveryDate]\r\n"
			+ "       ,c.PlanSystemDate\r\n"
			+ "       ,c.PlanUserDate\r\n"
			+ "       ,c.GroupNo\r\n"
			+ "       ,c.SubGroup\r\n"
			+ "       ,c.[PlanBy]\r\n"
			+ "       ,c.[Id] as TempPlanningId\r\n"
			+ "       ,Cast(ROW_NUMBER()\r\n"
			+ "               OVER (\r\n"
			+ "                 PARTITION BY [ForecastId]\r\n"
			+ "                 ORDER BY [ForecastId], A.[ProductionOrder] ) as NVARCHAR(max)) as [BatchFirst] \r\n"
			+ " into #tempFCTempProd\r\n"
			+ " FROM [PPMM].[dbo].[SOR_TempProd] as a\r\n"
			+ " INNER join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.id = c.TempProdId  \r\n"
			+ " where ForecastId is not null and\r\n"
			+ "		  A.DataStatus = 'O' AND\r\n"
			+ "		  C.[DataStatus] = 'O' and\r\n"
			+ "       C.[PlanSystemDate] >= CAST(GETDATE() AS DATE) \r\n";

//	private String leftJoinAPD = ""
//			+ " left join [PPMM].[dbo].[ApprovedPlanDate] as apd on a.[POId] = apd.[POId] and\r\n"
//			+ "													     apd.[IsFirst] = 'true' and\r\n"
//			+ "													     apd.DataStatus = 'O' and\r\n"
//			+ "			                                             apd.[SorDueDate] is not null \r\n";
	private String fromPOA = "" + " FROM #tempFromPO as a \r\n";
	private String fromFCA = "" + " FROM #tempFromFC as a \r\n";
	private String leftJoinRDC = "" + " left join [PPMM].[dbo].[TEMP_PlanningLot] as c on A.[RedyeId] = c.[ReDyeId] \r\n";
	private String leftJoinTPAC =
			"" + " left join [PPMM].[dbo].[TEMP_PlanningLot] as c on A.[TempPOAddId] = c.[TempPOAddId] \r\n";
	private String leftJoinCPP =
			"" + "	left join [PPMM].[dbo].[TEMP_ProdPreset] as CPP on a.ProductionOrder = CPP.[ProductionOrder]\r\n";

	public PlanningProdDaoImpl(Database database, String conType) {
		this.database = database;
		this.conType = conType;
		this.message = "";
	}

	@Override
	public ArrayList<InputApprovedFCDetail> approvedFCPlanDate(ArrayList<InputApprovedFCDetail> tmpList)
	{
		SORAPIController controller = new SORAPIController();
		ApprovedPlanDateModel apdModel = new ApprovedPlanDateModel(this.conType);
		try {
			String sorStatus = controller.updateDyeDate(tmpList);
			if (sorStatus.equals(Config.C_SUC_ICON_STATUS)) {
				apdModel.insertApprovedFCPlanDate(tmpList);
			} else {
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return tmpList;
	}

	@Override
	public ArrayList<ListApprovedDetail> approvedPlanDate(ArrayList<ListApprovedDetail> tmpList)
	{
		SORAPIController sorAPIController = new SORAPIController();
		ApprovedPlanDateModel apdModel = new ApprovedPlanDateModel(this.conType);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "";

		ListApprovedDetail beanList = tmpList.get(0);
		String planStartDate = beanList.getPlanStartDate();
		String approvedBy = beanList.getApprovedBy();
		String iconStatusCheck = beanList.getIconStatus(); 
		ArrayList<InputApprovedDetail> listAPP = beanList.getInputApprovedDetailList();

		InputTempProdDetail beanTmp = new InputTempProdDetail();
		List<String> listDate = beanTmp.getListDate();
		List<Integer> listPOId = beanTmp.getListPOId();
		HashMap<String, Integer> mapCountDue = new HashMap<>();

		for (InputApprovedDetail bean : listAPP) {
			int poId = bean.getPoId();
			listPOId.add(poId);
			bean.setApprovedBy(approvedBy);
			bean.setDataStatus(Config.C_OPEN_STATUS);
		}
		beanTmp.setPlanStartDate(planStartDate);
		beanTmp.setListPOId(listPOId); 
		ArrayList<InputApprovedDetail> listPO = this.getApprovedDetailByGroupNoNDate(beanTmp, "Normal"); 
		if ( ! listPO.isEmpty()) {
			for (int i = 0; i < listPO.size(); i ++ ) {
				InputApprovedDetail bean = listAPP.get(i);
				String sorDueDate = bean.getSorDueDate();
				if (mapCountDue.get(sorDueDate) == null) {
					mapCountDue.put(sorDueDate, 1);
				} else {
					int count = mapCountDue.get(sorDueDate)+1;
					mapCountDue.put(sorDueDate, count);
				}
			}
			for (Entry<String, Integer> entry : mapCountDue.entrySet()) {
				listDate.add(entry.getKey());
			}
			beanTmp.setListDate(listDate); 
			ArrayList<InputApprovedDetail> listCountDate = this.getApprovedDetailByGroupNoNDate(beanTmp, "CountSOR"); 
			for (InputApprovedDetail bean : listCountDate) {
				String sorDueDate = bean.getSorDueDate();
				int countSORDueDate = bean.getCountSORDueDateLast();
				if (mapCountDue.get(sorDueDate) != null) {
					int countApporvedDate = mapCountDue.get(sorDueDate); 
					if (Config.C_MAXPOPERDAY >= countSORDueDate+countApporvedDate) {
						// OK
					} else {
						iconStatus = "W";
						systemStatus += ""
								+ sorDueDate
								+ " : "
								+ countSORDueDate
								+ " po has already been accepted and "
								+ countApporvedDate
								+ " new po are being accepted. | Summary "
								+ (countSORDueDate+countApporvedDate)
								+ " po > 50\n";
						// NOT OK
					}
				}
			}
		} else {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus += "Some PO already approved.Please try again \n";
		} 
		if ((iconStatusCheck.equals("NoSor") && iconStatus.equals(Config.C_SUC_ICON_STATUS)) || iconStatusCheck.equals("NoSorW")) {
			systemStatus = ""; 
			iconStatus = apdModel.upsertApprovedPlanDate(listAPP);
			this.replaceTempProdWithReal(listAPP); 
			if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) { 
				
			} else if (iconStatus.equals(Config.C_ERR_ICON_STATUS)) {
				systemStatus = Config.C_ERROR_TEXT;
			}
		} else if (iconStatus.equals(Config.C_SUC_ICON_STATUS)
				|| (iconStatusCheck.equals("W") && (iconStatus.equals(Config.C_SUC_ICON_STATUS) || iconStatus.equals("W")))) {
			String sorIconStatus = "",sorSystemStatus = "";
			InputApprovedDetail beanSOR = sorAPIController.updateProductionDue(listAPP);
			sorIconStatus = beanSOR.getIconStatus();
			sorSystemStatus = beanSOR.getSystemStatus();  
			if (sorIconStatus.equals(Config.C_SUC_ICON_STATUS)) {  
				iconStatus = Config.C_SUC_ICON_STATUS;
				systemStatus = sorSystemStatus;
				apdModel.upsertApprovedPlanDate(listAPP);
				this.replaceTempProdWithReal(listAPP);
			} else {   
				iconStatus = Config.C_ERR_ICON_STATUS;
				systemStatus += "PPMM2 failed to send some data to SOR.\n ( " + sorSystemStatus + " ) ";
			} 
		} else if (iconStatusCheck.equals("W") && iconStatus.equals(Config.C_ERR_ICON_STATUS)) {
			iconStatus = Config.C_ERR_ICON_STATUS;
		}
		tmpList.get(0).setIconStatus(iconStatus);
		tmpList.get(0).setSystemStatus(systemStatus);
		return tmpList;
	}

	private boolean checkDifOldValueWithNewValue(String oldGroupNo, String oldSubGroup, String oldPlanSystemDate,
			InputTempProdDetail beanNew)
	{
		String newGroupNo = beanNew.getGroupNo();
		String newSubGroup = beanNew.getSubGroup();
		String newPlanSystemDate = beanNew.getPlanSystemDate();
		int tempPlanningId = beanNew.getTempPlanningId();
		if (newGroupNo == null) {
			newGroupNo = "";
		}
		if (newSubGroup == null) {
			newSubGroup = "";
		}
		if (newPlanSystemDate == null) {
			newPlanSystemDate = "";
		}
		if (tempPlanningId == 0) {
			return true;
		} else if ( ! oldGroupNo.equals(newGroupNo)) {
			return true;
		} else if ( ! oldSubGroup.equals(newSubGroup)) {
			return true;
		} else if ( ! oldPlanSystemDate.equals(newPlanSystemDate)) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<InputTempProdDetail> createLotTempByGroupNo(ArrayList<InputLotDragDropDetail> poList)
	{
		ProdCreatedDetailModel pcdModal = new ProdCreatedDetailModel(this.conType);
		ArrayList<InputTempProdDetail> ansList = new ArrayList<>();
		InputLotDragDropDetail bean = poList.get(0);
		InputTempProdDetail dragLotBean = bean.getDragLot();
		InputTempProdDetail dropLotBean = bean.getDropLot();
		bean.getPlanCase();

		String colorType = dragLotBean.getColorType();
		ArrayList<InputTempProdDetail> calList = bean.getCalTableList();

		int poIdDrag = dragLotBean.getPoId();

//		dragLotBean.getColor();
//		dragLotBean.getOperation();
//		dragLotBean.getNo();
//		dragLotBean.getLotType();
//		dragLotBean.getProductionOrder();

		String articleDrag = dragLotBean.getArticle();
		dragLotBean.getPo();
		dragLotBean.getPoLine();

		ArrayList<InputPODetail> listMain;
		ArrayList<InputGroupDetail> listAG;
		ArrayList<InputGroupDetail> listAGTmp;
		ArrayList<InputGroupDetail> listSingleAG;
		List<String> articleList;
		InputGroupDetail beanIGD;

		String prodQtyDrop = dropLotBean.getProductionOrderQty();
		double db_orderQty = Double.parseDouble(prodQtyDrop);

		double db_tmpOrderQty = 0;

		if (calList.isEmpty()) {

		} else {
			prodQtyDrop = dropLotBean.getProductionOrderQty();
			db_orderQty = Double.parseDouble(prodQtyDrop);

			boolean bl_haveFirstLot = false;
			for (InputTempProdDetail beanCal : calList) {
				if (beanCal.getFirstLot().equals(this.C_Y)) {
					bl_haveFirstLot = true;
				}
				db_tmpOrderQty += beanCal.getDbProdQty();
			}
			db_orderQty = db_tmpOrderQty;
			listMain = pcdModal.getPODetailByPO(poIdDrag);
			listAG = this.getGroupSubDetail(poList);
			listAGTmp = new ArrayList<>();
			beanIGD = new InputGroupDetail();
			articleList = new ArrayList<>();
			articleList.add(articleDrag);
			beanIGD.setDbLotQtyMax(db_orderQty);
			beanIGD.setArticleList(articleList);
			listAGTmp.add(beanIGD);

			listSingleAG = pcdModal.getBaseArticleSubGroupDetail(listAGTmp, colorType, "SINGLE");
			if (listSingleAG.isEmpty()) {
				listSingleAG = pcdModal.getBaseArticleSubGroupDetail(listAGTmp, colorType, "SINGLELOWEST");
			}

			InputPODetail beanMain = null;
			if ( ! listMain.isEmpty()) {
				beanMain = listMain.get(0);
				ansList = pcdModal.recreateTMPProdOrder(beanMain, db_orderQty, "", listAG, colorType, listSingleAG, false);
				for (int i = 0; i < ansList.size(); i ++ ) {
					if (i == 0 && bl_haveFirstLot) {
						ansList.get(i).setFirstLot(this.C_Y);
					}
//					else if(bl_IsWorkInSixMonth) {
//						ansList.get(i).setFirstLot(">6M");
//					}
					else {
						ansList.get(i).setFirstLot("N");
					}
				}
			}
		}
		return ansList;
	}

//	@Override
//	public ArrayList<InputTempProdDetail> generateLotTemp(ArrayList<InputLotDragDropDetail> poList)
//	{
//		ProdCreatedDetailModel pcdModal = new ProdCreatedDetailModel(this.conType);
//		TempPORunningModel tprModel = new TempPORunningModel(this.conType);
//		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
//		RelationPOAndProdOrderModel rpapModel = new RelationPOAndProdOrderModel(this.conType);
//		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
//		PlanInsteadProcessIdModel pipModel = new PlanInsteadProcessIdModel(this.conType);
//		PlanInsteadProdOrderModel pipoModel = new PlanInsteadProdOrderModel(this.conType);
//		ReportModel rpModel = new ReportModel(this.conType);
//		SORTempProdModel sorTModel = new SORTempProdModel(this.conType);
//
//		String iconStatus = Config.C_SUC_ICON_STATUS;
//		String systemStatus = "";
//
//		ArrayList<InputTempProdDetail> ansList = new ArrayList<>();
//		InputLotDragDropDetail bean = poList.get(0);
//		InputTempProdDetail dropLotBean = bean.getDropLot();
//		bean.getGroupNoDrop();
//
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//		new Timestamp(time);
//		ArrayList<InputTempProdDetail> calList = bean.getCalTableList();
////		bean.getResultTableList();
////		bean.getGroupNoDrop();
////		bean.getSubGroupDrop();
////		bean.getPlanDateDrop();
////		dropLotBean.getNo();
////		dropLotBean.getLotType();
////		dropLotBean.getPOId();
//		String changeBy = bean.getChangeBy();
//
//		ArrayList<InputPODetail> listMain;
//		ArrayList<InputGroupDetail> listAG;
//		ArrayList<InputGroupDetail> listAGTmp;
//		ArrayList<InputGroupDetail> listSingleAG;
//		List<String> articleList;
//		InputGroupDetail beanIGD;
//		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
//
//		String prodQtyDrop = dropLotBean.getProductionOrderQty();
//		double db_orderQty = Double.parseDouble(prodQtyDrop);
//
//		double db_tmpOrderQty = 0;
//
//		if (calList.isEmpty()) {
//		} else {
//			ArrayList<InputTempProdDetail> checkProdList = this.getProdDetailByTempProdId(calList);
//			if (checkProdList.size() != calList.size()) {
//				iconStatus = Config.C_ERR_ICON_STATUS;
//				systemStatus += "Some Prod.Order already recreate or can't recreate.\n";
//			} else {
//
//				InputTempProdDetail cusDueMinBean = checkProdList.get(0);
//				int poIdDrag = cusDueMinBean.getPoId();
//				int tempProdIdDrag = cusDueMinBean.getTempProdId();
//				String articleDrag = cusDueMinBean.getArticle();
//				String colorType = cusDueMinBean.getColorType();
//
//				prodQtyDrop = dropLotBean.getProductionOrderQty();
//				db_orderQty = Double.parseDouble(prodQtyDrop);
//
//				for (InputTempProdDetail beanCal : calList) {
//					db_tmpOrderQty += beanCal.getDbProdQty();
//					beanCal.setChangeBy(changeBy);
//				}
//				db_orderQty = db_tmpOrderQty;
//				listMain = pcdModal.getPODetailByPO(poIdDrag);
//				listAG = this.getGroupSubDetail(poList);
//
//				listAGTmp = new ArrayList<>();
//				beanIGD = new InputGroupDetail();
//				articleList = new ArrayList<>();
//				articleList.add(articleDrag);
//				beanIGD.setDbLotQtyMax(db_orderQty);
//				beanIGD.setArticleList(articleList);
//				listAGTmp.add(beanIGD);
//
//				listSingleAG = pcdModal.getBaseArticleSubGroupDetail(listAGTmp, colorType, "SINGLE");
//				if (listSingleAG.isEmpty()) {
//					listSingleAG = pcdModal.getBaseArticleSubGroupDetail(listAGTmp, colorType, "SINGLELOWEST");
//				}
//				InputPODetail beanMain = null;
//				if ( ! listMain.isEmpty()) {
//					beanMain = listMain.get(0);
//					ansList = pcdModal.recreateTMPProdOrder(beanMain, db_orderQty, "", listAG, colorType, listSingleAG, false);
//					if (ansList.isEmpty()) {
//						iconStatus = Config.C_ERR_ICON_STATUS;
//						systemStatus += "Failed to recreate Prod.Order.\n";
//					} else {
//						ArrayList<InputTempProdDetail> upsertNullList = new ArrayList<>();
//						boolean bl_haveFirstLot = false;
//						String prodOrderType = "";
//						for (int i = 0; i < calList.size(); i ++ ) {
//							InputTempProdDetail beanCal = calList.get(i);
//							prodOrderType = beanCal.getProductionOrderType();
//							if (prodOrderType.equals("")) {
//								prodOrderType = beanCal.getProductionOrderType();
//							} else {
//								prodOrderType = beanCal.getProductionOrderType();
//							}
//							if (beanCal.getFirstLot().equals(this.C_Y)) {
//								bl_haveFirstLot = true;
//							}
//							InputTempProdDetail beanNull = new InputTempProdDetail();
//							beanNull.setTempProdId(beanCal.getTempProdId());
//							upsertNullList.add(beanNull);
//						}
//						String lotBegin = "";
//						String lastRunningNo = "";
//						ArrayList<String> prodOrderConcatList = new ArrayList<>();
//						for (int i = 0; i < ansList.size(); i ++ ) {
//							if (i == 0 && bl_haveFirstLot) {
//								ansList.get(i).setFirstLot(this.C_Y);
//							}
////							else if(bl_IsWorkInSixMonth) {
////								ansList.get(i).setFirstLot(">6M");
////							}
//							else {
//								ansList.get(i).setFirstLot("N");
//							}
//							lotBegin = ansList.get(i).getProductionOrder().substring(0, 2);
//							lastRunningNo = ansList.get(i).getProductionOrder();
//							prodOrderConcatList.add(ansList.get(i).getProductionOrder());
//						}
//						InputPODetail beanTmp = new InputPODetail();
//						beanTmp.setChangeBy(changeBy);
//						iconStatus = tprModel.upsertTEMPPORunningDetail(lotBegin, lastRunningNo, iconStatus, changeBy); // boathere
//						// SET 'X' TO OLD
//						sorTModel.updateDataStatusWithIdForSORTempProd(calList, Config.C_CLOSE_STATUS);
//						tplModel.updateTempPlanningLotWithDataStatus(upsertNullList, Config.C_CLOSE_STATUS);
//						rpapModel.updateRelationPOAndProdOrderWithDataStatus(calList, Config.C_CLOSE_STATUS);
//						pipModel.insertPlanInsteadProcessId(cusDueMinBean);
//						ArrayList<InputPlanInsteadProdOrderDetail> listPSP =
//								pipModel.getPlanInsteadProcessIdDetail(cusDueMinBean);
//						if ( ! listPSP.isEmpty()) {
//							InputPlanInsteadProdOrderDetail beanPSP = listPSP.get(0);
//							int newPlanInseadId = beanPSP.getId();
//							for (InputTempProdDetail element : ansList) {
//								element.setNo("");
//								element.setPlanInsteadId(newPlanInseadId);
//								element.setProductionOrderType(prodOrderType);
//								element.setChangeBy(changeBy);
//							}
//							stpModel.insertSORTempProd(ansList, this.C_P);
//							pipoModel.insertPlanInsteadProdOrder(calList, poIdDrag, tempProdIdDrag, newPlanInseadId,
//									prodOrderConcatList);
//						}
//
//						bgjModel.execHandlerBatchNoForRelation();
//						bgjModel.execHandlerPlanLotSORDetail();
//					}
//				}
//			}
//		}
//
//		if (ansList.isEmpty()) {
//			InputTempProdDetail beanTemp = new InputTempProdDetail();
//			beanTemp.setIconStatus(iconStatus);
//			beanTemp.setSystemStatus(systemStatus);
//		} else {
//			ansList.get(0).setIconStatus(iconStatus);
//			ansList.get(0).setSystemStatus(systemStatus);
//		}
//		pcdModal.searchGroupOptionListForNull();
////		bgjModel.execUpsertLeadTime();
//		bgjModel.execUpsertRuleCalculated();
//		rpModel.processVolumeForReport();
//		this.rePlanningLot();
//		return ansList;
//	}

	@Override
	public ArrayList<InputApprovedDetail> getApprovedDetailByGroupNoNDate(InputTempProdDetail bean, String caseWork)
	{
		ArrayList<InputApprovedDetail> list = null;
		String[] array;
		String groupNo = "";
		String subGroup = "";
		String planStart = bean.getPlanStartDate();
		String beanCaseWork = bean.getCaseWork();
		List<String> groupList = bean.getMainNSubGroupList();
		List<String> dateList = bean.getListDate();
		List<Integer> poList = bean.getListPOId();
		String whereGroup = "";
		if (groupList.isEmpty()) {
			whereGroup = "";
		} else {
			whereGroup = "  (  \r\n";
			for (int i = 0; i < groupList.size(); i ++ ) {
				String groupSub = groupList.get(i).replace(" ", "");
				array = groupSub.split(Config.C_COLON);
				groupNo = array[0];
				subGroup = array[1];
				whereGroup += " ( a.[GroupNo] = '" + groupNo + "' and a.[SubGroup] = '" + subGroup + "' )";
				if (i < groupList.size()-1) {
					whereGroup += " or \r\n";
				}
			}
			whereGroup += " ) \r\n";
			whereGroup = " and " + whereGroup;
		}
		String sql = "";
		if ( ! planStart.equals("")) {
			sql += " "
					+ " declare @BeginDate date  = convert(date, '"
					+ planStart
					+ "', 103) ;\r\n"
					+ " declare @LastDate date  = DATEADD(DAY, 30, @BeginDate) ;\r\n";
		}
		sql += " "
				+ " SET NOCOUNT ON;\r\n"
				+ this.declareBegin
				+ this.declareTempBeginSpecialDesign
				+ this.declareTempMaxPlan
				+ this.declareTempMainSpecialDesign
				+ this.declareTempFromLotMainAP
				+ this.declareTempLotMainAP
				+ this.declareTempFromLotSubAP
				+ this.declareTempLotSubAP
				+ this.declareTempBeginDataAP
				+ this.declareTempApprovedDateAP
				+ this.declareTempFromPO;
		String whereLast = "";
		if (caseWork.equals("Normal")) {
			if ( ! poList.isEmpty()) {
				whereLast = " and (  \r\n";
				for (int i = 0; i < poList.size(); i ++ ) {
					int poId = poList.get(i);
					whereLast += " a.[POId] = '" + poId + "' ";
					if (i < poList.size()-1) {
						whereLast += " or \r\n";
					}
				}
				whereLast += " ) \r\n";
			}
			if (whereGroup.equals("")) {
//				 whereLast = addStringAndIfNotEmpty(whereLast);
				whereLast += whereGroup;
			}
			if (planStart.equals("") || beanCaseWork.equals("ApprovedAll")) {
				whereLast += " ";
			} else {
				whereLast += " AND ( [PlanSystemDate] >= @BeginDate AND [PlanSystemDate] <= @LastDate )\r\n";
			}
			sql += ""
					+ " ----------- FIND BEGIN / LAST DYE DATE FROM FAC HOLIDAY ----------------\r\n"
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
					+ " SELECT \r\n"
					+ "	ROW_NUMBER() OVER( ORDER BY [Date] ASC) as RowNum\r\n"
					+ "	,[Date]\r\n"
					+ "      ,[DateName]\r\n"
					+ "      ,[NormalWork]\r\n"
					+ "	into #tempWorkDateRunning\r\n"
					+ "  FROM [PPMM].[dbo].[InputBKKDateRunning]\r\n"
					+ "  where NormalWork = 'O'\r\n"
					+ " select DISTINCT\r\n"
					+ "		a.Id as POId ,\r\n"
					+ "     a.[ReferenceId],\r\n"
					+ "		a.PO , a.POLine,a.GreigePlan , a.CustomerDue ,a.Design,a.RuleNo,a.POPuangId,a.MaterialNo\r\n"
					+ "		, case\r\n"
					+ "			when IAD.[ArticleReplaced] is not null and IAD.ArticleReplaced <> '' then IAD.[ArticleReplaced]\r\n"
					+ "			else a.Article\r\n"
					+ "			end as Article   \r\n"
					+ "		,case \r\n"
					+ "			when SPOPD.POId is not null then SPOPD.POId\r\n"
					+ "			else a.Id\r\n"
					+ "			end as POIdCheck\r\n"
					+ "     , MaxSORCFMDateLast as SORCFMDateLast,MaxSORDueDateLast as SORDueDateLast\r\n"
					+ "		into #tempAPPODetail\r\n"
					+ "		from [PPMM].[dbo].[SOR_PODetail] as a\r\n"
					+ "		INNER JOIN #tempFromPO as b on a.Id = b.POId \r\n"
					+ "		LEFT JOIN ( \r\n" // 20240602 // แกเงื่อนไข
					+ "			select POId  ,max(SORCFMDateLast) AS MaxSORCFMDateLast, MAX(SORDueDateLast) AS MaxSORDueDateLast\r\n"
					+ "			from #tempFromPO as a\r\n"
					+ "			group by POID\r\n"
					+ "		) as BA on B.POId = BA.POId AND BA.MaxSORDueDateLast = B.SORDueDateLast\r\n"
					+ this.leftJoinSPOPDA  
//					+ "		left join (\r\n"
//					+ "			  SELECT [Id]\r\n"
//					+ "			  ,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo]\r\n"
//					+ "			  ,[CustomerShortName] as [CustomerName]\r\n"
//					+ "			  ,[ChangeDate]\r\n"
//					+ "			  ,[CreateDate]\r\n"
//					+ "			  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
//					+ "		) as cd on a.[CustomerNo] = cd.[CustomerNo] \r\n"
//
					+ this.leftJoinPOStatusACD
//					+ "		left join [PPMM].[dbo].[SpecialCaseMR] as SCMR on a.Article = scmr.Article \r\n"
					+ this.leftJoinIAD
					+ "		WHERE a.[DataStatus] = 'O' and\r\n"
					+ "           ( SPOPD.[DataStatus] = 'O' OR SPOPD.[DataStatus] IS NULL )\r\n"
					+ " select \r\n"
					+ "		a.POId \r\n"
					+ "   ,a.POPuangId\r\n"
					+ "	  , indexCFM\r\n"
					+ "	  , indexCFMAnswer \r\n"
					+ "	  , DayAdd \r\n"
					+ " into  #tempAPDyeDate  \r\n"
					+ "	from ( \r\n"
					+ "		SELECT DISTINCT\r\n"
					+ "			 a.POId  \r\n"
					+ "			,a.[PO]\r\n"
					+ "         ,a.POPuangId\r\n"
					+ "		  	,a.[POLine] \r\n"
					+ "		  	,a.[RuleNo]  \r\n"
					+ "		  	,CASE \r\n"
					+ "				WHEN LTDELDS.DayAdd is not null then LTDELDS.DayAdd \r\n"
					+ "				WHEN LTDELAR.DayAdd is not null then LTDELAR.DayAdd \r\n"
					+ "				ELSE 0\r\n"
					+ "				end as DayAdd  \r\n"
					+ "		  	,indexSORDueDate - d.LeadTimeDeliveryToCFM as indexCFM\r\n"
					+ "		  	,indexSORDueDate - d.LeadTimeDyeToCFMAnswer as indexCFMAnswer  \r\n"
					+ "	 	FROM #tempAPPODetail AS A\r\n"
					+ "		LEFT JOIN (\r\n"
					+ "			select\r\n"
					+ "				a.Id"
					+ "				, a.ProductionOrder\r\n"
					+ "				,a.DataStatus \r\n"
					+ "				,case\r\n"
					+ "					when b.FirstLot is not null then a.FirstLot \r\n"
					+ "					ELSE 'N'\r\n"
					+ "					END AS FirstLot\r\n"
					+ "				,A.POId  \r\n"
					+ "				,case\r\n"
					+ "					when b.FirstLot is not null then 'Y'\r\n"
					+ "					ELSE 'N'\r\n"
					+ "					END AS HaveFirstLot\r\n"
					+ "		from #tempPOMainNPOInstead as a \r\n"
					+ "		left join ( \r\n"
					+ "			SELECT distinct POId,FirstLot \r\n"
					+ "			FROM #tempPOMainNPOInstead AS SUBSTP \r\n"
					+ "			 where SUBSTP.DataStatus = 'O' and SUBSTP.[FirstLot] = 'Y'\r\n"
					+ "		 ) as b on a.POId = b.POId   \r\n"
					+ "	) AS B ON a.POIdCheck = b.POId and b.[DataStatus] = 'O'\r\n"
					+ "	left join ( \r\n"
					+ "		SELECT *\r\n"
					+ "		FROM [PPMM].[dbo].[SpecialCaseLeadTime] \r\n"
					+ "		where Design <> '' and Design is not null and LeadTimeTypeId = 1\r\n"
					+ " ) as LTDELDS on a.Article = LTDELDS.Article and  a.Design = LTDELDS.Design\r\n"
					+ "	left join (\r\n"
					+ "		SELECT *\r\n"
					+ "		FROM [PPMM].[dbo].[SpecialCaseLeadTime] \r\n"
					+ "		where Design = '' OR Design is null and LeadTimeTypeId = 1\r\n"
					+ "	) as LTDELAR on a.Article = LTDELAR.Article  \r\n"
					+ "	left join (\r\n"
					+ "			SELECT DISTINCT \r\n"
					+ "			a.[RuleNo]   \r\n"
					+ "			,a.[Delivery] as LeadTimeDyeToCFMAnswer\r\n"
					+ "			,a.[CFMAnswer]+ a.[Delivery] as LeadTimeDeliveryToCFM\r\n"
					+ "			, case\r\n"
					+ "				when a.LeadTimeType = 'FirstLot' then 'Y'\r\n"
					+ "				else 'N'\r\n"
					+ "				END FirstLot \r\n"
					+ "			FROM [PPMM].[dbo].[InputLeadTimeDetail] AS A  \r\n"
					+ "			WHERE A.[DataStatus] = 'O'\r\n"
					+ "	)as d on a.[RuleNo] = d.[RuleNo] and b.[FirstLot] = d.[FirstLot] \r\n"
					+ "	left join (\r\n"
					+ "		SELECT *\r\n"
					+ "				, (select top 1 RowNum \r\n"
					+ "				from #tempWorkDateRunning  as idr\r\n"
					+ "				where idr.Date >= a.SORDueDateLast \r\n"
					+ "				order by idr.Date asc) as indexSORDueDate\r\n"
					+ "		FROM #tempFromPO AS A \r\n"
					+ "		) AS TPL on A.POId = tpl.POId \r\n"
					+ " ) as a  \r\n"
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
					+ " left join ( select RowNum, Date as LTCFMDate\r\n"
					+ "				from #tempWorkDateRunning  as idr\r\n"
					+ "				where idr.NormalWork = 'O'  )  as i on a.indexCFM = i.RowNum\r\n"
					+ " left join ( select RowNum, Date as LTCFMAnswerDate\r\n"
					+ "				from #tempWorkDateRunning  as idr\r\n"
					+ "				where idr.NormalWork = 'O'  )  as j on a.indexCFMAnswer = j.RowNum\r\n"
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
					+ ""
					+ " SELECT \r\n"
					+ "a.[Id],\r\n"
					+ "a.[ReferenceId],\r\n"
					+ "a.[POId],a.[PO],a.[POLine],a.[POType],a.CustomerNo,a.CustomerName,a.[CustomerMat],a.[Article],\r\n"
					+ "a.[Design],a.[Color],a.[ColorCustomer],a.[LabRef],\r\n"
					+ "a.[OrderQty] as POQty ,a.[Unit],A.[CustomerDue],A.[PlanSystemDate],\r\n"
					+ "A.[PlanDueDate],\r\n"
					+ "CASE \r\n"
					+ "	  WHEN  TAF.LTCFMDate IS NOT NULL THEN TAF.LTCFMDate\r\n"
					+ "	  ELSE a.SORCFMDate\r\n"
					+ "	  END AS SORCFMDate,\r\n"
					+ "A.[SORDueDateLast],\r\n"
					+ "a.[CaseSORDueDate] ,\r\n"
					+ "a.[MaterialNo],a.PlanCFMDate ,a.PlanDueDate,a.Remark,a.ProductionOrder,a.POPuangId\r\n"
					+ " FROM  #tempFromPO as a\r\n"
//				+ " LEFT join (\r\n"
					+ " INNER join (\r\n"
					+ "  	select a.POId,a.SORDueDate ,max(a.ProductionOrder) as maxProductionOrder\r\n"
					+ " 	FROM  #tempFromPO as a\r\n"
					+ " 	inner join ( \r\n"
					+ "			SELECT a.POId  ,max(SORDueDate) as maxSorDueDate\r\n"
					+ "	 		FROM  #tempFromPO as a \r\n"
					+ "			where a.POId is not null\r\n"
					+ "                  "
					+ whereLast
					+ "			group by a.POId\r\n"
					+ "	 	) as b on a.POId = b.POId and a.SORDueDate = b.maxSorDueDate\r\n"
					+ " 	group by a.POId,a.SORDueDate \r\n "
					+ " ) as b on a.POId = b.POId and a.SorDueDate = b.SorDueDate and a.ProductionOrder = b.maxProductionOrder\r\n"
					+ " LEFT JOIN #tempAPFacDyeDate AS TAF ON A.POId = TAF.POId\r\n"
					+ " where a.ApprovedDate is null AND a.SORCFMDate IS NOT NULL\r\n"
					+ whereLast
					+ " ORDER BY a.POPuangId, a.PO,a.POLine\r\n"; 
		}
		// COUNT TOTAL SORDUE DATE FOR CHECK 50
		else {
			if ( ! dateList.isEmpty()) {
				whereLast = " and (  \r\n";
				for (int i = 0; i < dateList.size(); i ++ ) {
					String dateDue = dateList.get(i);
					whereLast += " a.[SORDueDateLast] = CONVERT(DATE,'" + dateDue + "' , 103 ) ";
					if (i < dateList.size()-1) {
						whereLast += " or \r\n";
					}
				}
				whereLast += " ) \r\n";
				whereLast = " " + whereLast;
			}
			if (whereGroup.equals("")) {
//				 whereLast = addStringAndIfNotEmpty(whereLast);
				whereLast += whereGroup;
			}
			if (planStart.equals("") || beanCaseWork.equals("ApprovedAll")) {
				whereLast += " ";
			} else {
				whereLast += " AND ( [PlanSystemDate] >= @BeginDate AND [PlanSystemDate] <= @LastDate )\r\n";
			}
			sql += ""
					+ " IF OBJECT_ID('tempdb..#tempPOId') IS NOT NULL\r\n"
					+ "    DROP TABLE #tempPOId; \r\n"
					+ " SELECT a.POId\r\n"
					+ " into #tempPOId\r\n"
					+ " FROM  #tempFromPO as a\r\n"
					+ " where a.POId is not null \r\n"
					+ whereLast
					+ " ORDER BY a.PO,a.POLine\r\n"
					+ " SELECT A.SORDueDateLast, count(SORDueDateLast) as countSORDueDateLast \r\n"
					+ " FROM  #tempFromPO as a\r\n"
					+ " inner join (\r\n"
					+ "  	select a.POId,a.SORDueDate ,max(a.ProductionOrder) as maxProductionOrder\r\n"
					+ " 	FROM  #tempFromPO as a \r\n"
					+ " 	inner join ( \r\n"
					+ "			SELECT a.POId  ,max(SORDueDate) as maxSorDueDate\r\n"
					+ "	 		FROM  #tempFromPO as a \r\n"
					+ "	 		inner join #tempPOId as tempPOId on a.POId = tempPOId.POId\r\n"
					+ "			group by a.POId\r\n"
					+ "	 	) as b on a.POId = b.POId and a.SORDueDate = b.maxSorDueDate\r\n"
					+ " 	group by a.POId,a.SORDueDate \r\n "
					+ " ) as b on a.POId = b.POId and a.SorDueDate = b.SorDueDate and a.ProductionOrder = b.maxProductionOrder\r\n"
					+ " group by SORDueDateLast\r\n";
		} 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genApprovedDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputGroupDetail> getArticleSubGroupDetail()
	{
		ArrayList<InputGroupDetail> list = null;
		String sql = ""
				+ " SET NOCOUNT ON;\r\n"
				+ " SELECT DISTINCT a.[GroupNo]\r\n"
				+ "		,a.[SubGroup] \r\n"
				+ "		,b.[LotPerDay]\r\n"
				+ "		,a.[Article]  \r\n"
				+ "	    ,STUFF((\r\n"
				+ "		   SELECT ' ,' +MachineName\r\n"
				+ "		   FROM [PPMM].[dbo].[InputMachineDetail] as c\r\n"
				+ "		   WHERE a.GroupNo = c.GroupNo and \r\n"
				+ "			     a.SubGroup = c.SubGroup and \r\n"
				+ "			     c.DataStatus = 'O' and \r\n"
				+ "		         MachineName <> '' \r\n"
				+ "		   FOR XML PATH('')\r\n"
				+ "		   ), 1, 2, '') as [MachineName]\r\n"
				+ "		,a.[LotMinMax] \r\n"
				+ "		,a.[ChangeBy]\r\n"
				+ "		,a.[ChangeDate] \r\n"
				+ "		,c.[Division]\r\n"
				+ "		,d.[ColorType]\r\n"
				+ "	FROM [PPMM].[dbo].[InputArticleSubGroupDetail] as a\r\n"
				+ "	inner join [PPMM].[dbo].[InputSubGroupDeail] as b on a.GroupNo = b.GroupNo and a.SubGroup = b.SubGroup\r\n"
				+ "	inner join [PPMM].[dbo].[InputArticleDetail] as c on a.[Article] = c.[Article]\r\n"
				+ "	LEFT join [PPMM].[dbo].[InputMainGroupDetail] as d on a.GroupNo = d.GroupNo\r\n"
				+ "	where a.DataStatus = 'O' and \r\n"
				+ "       b.DataStatus = 'O' and \r\n"
				+ "       ( a.[LotMinMax] is not null AND a.[LotMinMax] <> '' AND a.[LotMinMax] <> '0' )\r\n"
				+ "		and d.DataStatus = 'O'\r\n"
				+ "	order by a.GroupNo , SubGroup ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genGroupDetail(map));
		}
		return list;
	}

	public ArrayList<InputGroupDetail> getArticleSubGroupDetailByArticleListNColor(ArrayList<InputGroupDetail> poList,
			String colorType)
	{
		ArrayList<InputGroupDetail> list = null;
		InputGroupDetail bean = poList.get(0);
		List<String> articleList = bean.getArticleList();
		String whereArticle = " (  \r\n";
		for (int i = 0; i < articleList.size(); i ++ ) {
			whereArticle += " a.Article = '" + articleList.get(i) + "' ";
			if (i < articleList.size()-1) {
				whereArticle += " or \r\n";
			}
		}
		whereArticle += " ) \r\n";
		whereArticle += " and ( d.[ColorType] = '" + colorType + "' or d.[ColorType] = 'All' ) \r\n";
		String sql = ""
				+ " SET NoCount ON; \r\n"
				+ " SELECT a.[Id]\r\n"
				+ "      ,a.[GroupNo]\r\n"
				+ "      ,a.[SubGroup]\r\n"
				+ "      ,a.[Article]\r\n"
				+ "      ,a.[LotMinMax]\r\n"
				+ "      ,a.[LotDif]\r\n"
				+ "      ,a.[IsOverCap]\r\n"
				+ "      ,a.[OverCapQty] \r\n"
				+ "      ,a.[ChangeBy]\r\n"
				+ "      ,a.[ChangeDate]\r\n"
				+ "	  	 ,c.[Division]\r\n"
				+ "		 ,d.[ColorType]\r\n"
				+ "  FROM [PPMM].[dbo].[InputArticleSubGroupDetail] as a\r\n"
				+ "  inner join [PPMM].[dbo].[InputArticleDetail] as c on a.[Article] = c.[Article]\r\n"
				+ "  inner join [PPMM].[dbo].[InputMainGroupDetail] as d on a.[GroupNo] = d.[GroupNo] \r\n"
				+ "  where a.DataStatus = 'O' and \r\n"
				+ "        c.DataStatus = 'O' and \r\n"
				+ "        a.[LotMinMax] <> '' and\r\n"
				+ whereArticle
				+ " Order By a.[LotMinMax] \r\n";
//				+ "        a.[Article] = '"+bean.getArticle()+"' ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genGroupDetail(map));
		}
		return list;
	}

	public ArrayList<InputGroupDetail> getCountPlanUserDate(InputLotDragDropDetail bean)
	{
		ArrayList<InputGroupDetail> list = null;
		// PLAN MANAGE BY SYSTEM AND USER STILL NOT PLAN
		String groupNoDrop = bean.getGroupNoDrop();
		String subGroupDrop = bean.getSubGroupDrop();
		String planDateDrop = bean.getPlanDateDrop();
		String sql = ""
//					+ this.declareTempPlanningONPC
				+ " SET NoCount ON; \r\n"
				+ " declare @BeginDate date  = convert(date, '"
				+ planDateDrop
				+ "', 103) ;\r\n"
				+ " SELECT a.[GroupNo]\r\n"
				+ "	,a.[SubGroup] \r\n"
				+ "	,a.[LotPerDay]\r\n"
				+ "	, COUNT([PlanUserDate]) AS CountWorkDate \r\n"
				+ "  FROM [PPMM].[dbo].[InputSubGroupDeail]   as a\r\n"
				+ "  left join [PPMM].[dbo].[viewTEMPPlanningLotOnProcess] as b on a.GroupNo = b.GroupNo and \r\n"
				+ "                                                                a.SubGroup = b.SubGroup\r\n"
//					+ "  left join #tempPlanningONPC as b on a.GroupNo = b.GroupNo and a.SubGroup = b.SubGroup\r\n"
				+ "  where ( b.PlanUserDate is null or  PlanUserDate = @BeginDate ) and\r\n"
				+ "			a.[GroupNo] = '"
				+ groupNoDrop
				+ "' and\r\n"
				+ "			a.[SubGroup] = '"
				+ subGroupDrop
				+ "'   \r\n"
				+ "  GROUP BY a.GroupNo ,a.SubGroup,a.[LotPerDay] \r\n"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genGroupDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputDateRunningDetail> getDateRunningDetail(ArrayList<InputTempProdDetail> tmpList)
	{
		ArrayList<InputDateRunningDetail> list = null;
		InputTempProdDetail bean = tmpList.get(0);
		String planStart = bean.getPlanStartDate();
		bean.getPlanEndDate();
		String declarePlan = " ";
		if (planStart.equals("")) {
			declarePlan = ""
					+ " declare @BeginDate date  = convert(date, GETDATE(), 103) ;\r\n"
					+ " declare @LastDate date  = DATEADD(DAY, 30, @BeginDate) ;\r\n";
		} else {
			declarePlan = ""
					+ " declare @BeginDate date  = convert(date, '"
					+ planStart
					+ "', 103) ;\r\n"
					+ " declare @LastDate date  = DATEADD(DAY, 30, @BeginDate) ;\r\n";
		}
		String sql = "\r\n"
				+ " SET NOCOUNT ON;\r\n"
				+ declarePlan
				+ "	SELECT\r\n"
				+ "		ROW_NUMBER() OVER (\r\n"
				+ "       ORDER BY [Date]  \r\n"
				+ "    	) - 1 as Id\r\n"
				+ "      ,[Date]\r\n"
				+ "      ,[DateName]\r\n"
				+ "  FROM [PPMM].[dbo].[InputDateRunning]\r\n"
				+ " WHERE ( [Date] >= @BeginDate AND [Date] <= @LastDate ) \r\n"
				+ " order by Date \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateRunningDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputGroupDetail> getGroupSubDetail(ArrayList<InputLotDragDropDetail> poList)
	{
		ArrayList<InputGroupDetail> list = null;
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		new Timestamp(time);

		InputLotDragDropDetail bean = poList.get(0);
		InputTempProdDetail dragLotBean = bean.getDragLot();
//		InputTempProdDetail dropLotBean = bean.getDropLot();
		bean.getGroupNoDrop();
		String groupNoDrop = bean.getGroupNoDrop();
		String subGroupDrop = bean.getSubGroupDrop();
//		bean.getPlanDateDrop();
//		dropLotBean.getNo();
//		dropLotBean.getLotType();
//		dropLotBean.getPOId();
//		bean.getGroupNoDrag();
//		bean.getSubGroupDrag();
//		bean.getPlanDateDrag();
//		bean.getChangeBy();
//		dragLotBean.getPOId();
//		dragLotBean.getOperation();
//		dragLotBean.getNo();
//		dragLotBean.getLotType();
//		dragLotBean.getProductionOrder();

		String articleDrag = dragLotBean.getArticle();
		String orderBy = " order by IMGD.[GroupNo],INSGD.[SubGroup]";
		String declared = "";
		String sql = ""
				+ " SET NoCount ON; \r\n"
				+ declared
				+ "	SELECT \r\n"
				+ "		IMGD.[GroupNo]\r\n"
				+ "	  	,INSGD.[SubGroup]\r\n"
				+ "	  	,IASGD.[Article]\r\n"
				+ "     ,[Description]\r\n"
				+ "     ,INSGD.[LotPerDay]\r\n"
				+ "     ,IMGD.[ColorType]\r\n"
				+ "     ,IMGD.[GroupType] \r\n"
				+ "      ,IASGD.[LotMinMax]\r\n"
				+ "      ,IASGD.[LotQtyMin]\r\n"
				+ "      ,IASGD.[LotQtyMax]\r\n"
				+ "      ,IASGD.[LotDif]\r\n"
				+ "      ,IASGD.[LotDifMax]\r\n"
				+ "      ,IASGD.[LotDifMin]\r\n"
				+ "      ,IASGD.[IsOverCap]\r\n"
				+ "      ,IASGD.[OverCapQty]\r\n"
				+ "      ,IASGD.[OverCapQtyMin]\r\n"
				+ "      ,IASGD.[OverCapQtyMax]\r\n"
				+ this.fromGroupDetailIMGD
				+ this.innerJoinGroupDetailIINSGD
				+ this.innerJoinGroupDetailIASGD
				+ " where IASGD.[Article] = '"
				+ articleDrag
				+ "' and  \r\n"
				+ "		  IASGD.[GroupNo] = '"
				+ groupNoDrop
				+ "' and\r\n"
				+ "		  IASGD.[SubGroup] = '"
				+ subGroupDrop
				+ "' \r\n"
				+ orderBy;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genGroupDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputGroupDetail> getMainSubGroupQtyForSetDate(ArrayList<InputTempProdDetail> poList)
	{
		ArrayList<InputGroupDetail> list = new ArrayList<>();
		InputTempProdDetail bean = poList.get(0);
		String matNo = bean.getMaterialNo();
		String article = bean.getArticle();
		String colorType = bean.getColorType();
		String prodOrderQty = bean.getProductionOrderQty();

		if (matNo.substring(0, 1).equals("H")) {
			String articleHW = "H" + article;
			InputTempProdDetail beanTemp = new InputTempProdDetail();
			beanTemp.setColorType(colorType);
			beanTemp.setArticle(articleHW);
			beanTemp.setProductionOrderQty(prodOrderQty);
			list = this.getMainSubGroupQtyForSetDate(beanTemp);
			if (list.isEmpty()) {
				articleHW = matNo.substring(0, 3);
				beanTemp = new InputTempProdDetail();
				beanTemp.setColorType(colorType);
				beanTemp.setArticle(articleHW);
				beanTemp.setProductionOrderQty(prodOrderQty);
				list = this.getMainSubGroupQtyForSetDate(beanTemp);
			}
		} else {
			list = this.getMainSubGroupQtyForSetDate(bean);
		}
		return list;
	}

	public ArrayList<InputGroupDetail> getMainSubGroupQtyForSetDate(InputTempProdDetail bean)
	{
		ArrayList<InputGroupDetail> list = new ArrayList<>();
		String article = bean.getArticle();
		String colorType = bean.getColorType();
		String prodOrderQty = bean.getProductionOrderQty();
		String where = "";
		String declareAdd = "";
		String selectAdd = "";
		String whereAdd = "";
 
		if ( ! colorType.equals(Config.C_BLANK)) {
			where += " and  ( imgd.[ColorType] = 'All' or imgd.[ColorType] = '" + colorType + "' ) \r\n";
		}
		if ( ! prodOrderQty.equals(Config.C_BLANK)) {
			declareAdd += "" + " DECLARE @ProdQty DECIMAL(13,3)\r\n" + " SET @ProdQty = " + prodOrderQty + ";";
			selectAdd += ""
					+ ",CASE \r\n"
					+ "	WHEN A.LotQtyMin IS NULL THEN \r\n"
					+ "		CASE \r\n"
					+ "			WHEN A.LotQtyMax = @ProdQty or a.LotQtyMaxWithOverCap = @ProdQty then 'Y'\r\n"
					+ "			ELSE 'N'\r\n"
					+ "		end\r\n"
					+ "	ELSE \r\n"
					+ "		CASE WHEN A.LotQtyMin <= @ProdQty AND @ProdQty <= A.LotQtyMaxWithOverCap THEN 'Y'\r\n"
					+ "		ELSE 'N'\r\n"
					+ "		END\r\n"
					+ "	END AS IsWork\r\n";
			whereAdd += " where IsWork = 'Y'";
		}
		String sql = " "
				+ " SET NoCount ON; \r\n"
				+ declareAdd
				+ "select *\r\n"
				+ "from (\r\n"
				+ "   select\r\n"
				+ "            a.ColorType,a.Article,a.GroupNo,a.SubGroup,a.LotQtyMax,a.LotQtyMin,a.OverCapQtyMax\r\n"
				+ selectAdd
				+ "           ,case \r\n"
				+ "          	  when a.GroupNo = 'G8' and a.GroupWithQty is not null then a.GroupWithQty\r\n"
				+ "               when a.[GroupNo] = b.[GroupNo] and a.[SubGroup] = b.[SubGroup] then [LotMinMax] \r\n"
				+ "	              ELSE A.GroupWithQty\r\n"
				+ "	           END AS GroupWithQty\r\n"
				+ " from ( \r\n"
				+ "		SELECT DISTINCT IMGD.ColorType,  B.*\r\n"
				+ "		FROM [PPMM].[dbo].[InputMainGroupDetail] AS IMGD\r\n"
				+ "		INNER JOIN [PPMM].[dbo].[InputSubGroupDeail] AS ISGD ON IMGD.GroupNo = ISGD.GroupNo   \r\n"
				+ "		inner join (\r\n"
				+ "			SELECT A.ARTICLE,A.GroupNo,A.SubGroup\r\n"
				+ "				,A.LotQtyMax\r\n"
				+ "				,A.OverCapQtyMax\r\n"
				+ "				,A.LotQtyMax + A.OverCapQtyMax  AS LotQtyMaxWithOverCap\r\n"
				+ "				,CASE WHEN A.LotQtyMin = 0 OR A.LotDifMin = NULL THEN NULL\r\n"
				+ "					ELSE A.LotQtyMin\r\n"
				+ "					END AS LotQtyMin\r\n"
				+ "				, CASE \r\n"
				+ "					WHEN OverCapQty is null or OverCapQty = '' or OverCapQty = '0' then CAST(A.LotMinMax AS NVARCHAR)\r\n"
				+ "					else CAST(A.LotMinMax AS NVARCHAR)+' ( '+CAST(A.OverCapQty AS NVARCHAR)+' )'\r\n"
				+ "					end AS GroupWithQty\r\n"
				+ "			FROM [PPMM].[dbo].[InputArticleSubGroupDetail]  AS A \r\n"
				+ "				where ( A.[DataStatus] = 'O' )  \r\n"
				+ "		) as b on b.[GroupNo] = IMGD.[GroupNo] AND\r\n"
				+ "               b.[SubGroup] = ISGD.[SubGroup]\r\n"
				+ "	   WHERE B.[Article] = '"
				+ article
				+ "'AND\r\n"
				+ "			  IMGD.[DataStatus] = 'O' AND \r\n"
				+ "			  ISGD.[DataStatus] = 'O' \r\n"
				+ where
				+ "    ) as a\r\n"
				+ "    left join [PPMM].[dbo].[MasterDefaultSubGroupQty] as b on a.[GroupNo] = b.[GroupNo] and\r\n"
				+ "                                                            a.[SubGroup] = b.[SubGroup]\r\n"
				+ " ) as a\r\n"
				+ whereAdd
				+ " order by  A.GroupNo,A.SubGroup \r\n"
				+ "\r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genGroupDetail(map));
		}
		return list;
	}

	public String getMessage()
	{
		return this.message;
	} 
	@Override
	public ArrayList<InputPlanningLotDetail> getPlanningGroupDetail(InputTempProdDetail p_bean)
	{
		GroupWorkDateModel gwdModel = new GroupWorkDateModel(this.conType);
		InputTempProdDetail beanTmpPrd;
		ArrayList<InputPlanningLotDetail> tmpGroupList = new ArrayList<>();
		ArrayList<InputPlanningLotDetail> planningList = new ArrayList<>();
		ArrayList<InputTempProdDetail> listDummy = new ArrayList<>();
		InputTempProdDetail beanDummy = new InputTempProdDetail();
		beanDummy.setPlanStartDate(p_bean.getPlanStartDate());
		beanDummy.setPlanEndDate(p_bean.getPlanEndDate());
		beanDummy.setMainNSubGroupList(p_bean.getMainNSubGroupList());
		listDummy.add(beanDummy);
		int countIdx = 0;
		int lotPerDay = 0;
		String planSystemDate = "";
		String tmpLastPlanSystemDate = "";
		int indexGroupSubRunning = 0;
		int indexGroupSubStart = 0;
		int indexGroupSubStop = 0;
		int indexWork = 0;
		int indexRow = 0;
		int indexPlanGroup = 0;
		int removeIndex = 0;
		String Date = "";
		String groupNo = "";
		String subGroup = "";
		String colorType = "";
		String groupType = "";
		String machineName = "";
		String keyMainSup = "";
		String groupNoPrdList = "";
		String subGroupPrdList = "";
		HashMap<String, Integer> mapWorkDateIdx = new HashMap<>();
		HashMap<String, String> mapKeyMainSubIdxWork = new HashMap<>();
		ArrayList<InputDateRunningDetail> workList = this.getDateRunningDetail(listDummy);
		ArrayList<InputPlanningLotDetail> groupList = this.getSubGroupDetail(listDummy);
		ArrayList<InputTempProdDetail> listPOOnPlan = new ArrayList<>();
		ArrayList<InputTempProdDetail> listPOTmp = new ArrayList<>();
		ArrayList<InputTempProdDetail> listReDyeTmp = new ArrayList<>();
		boolean bl_checkTimeRunning = false;
//	   bl_checkTimeRunning = true;   
		if (bl_checkTimeRunning) {
			System.out.println(" 1 : " + new Timestamp(System.currentTimeMillis()));
		}
		ArrayList<WorkDateDetail> workDateAvaList = gwdModel.getGroupWorkDateDetail(listDummy, "ALL");// GET WORK DATE
		if (bl_checkTimeRunning) {
			System.out.println(" 2 : " + new Timestamp(System.currentTimeMillis()));
		}
		ArrayList<InputTempProdDetail> listTempSearch = this.getTempProdDetail(listDummy); // GET PLANNING LOT
		if (bl_checkTimeRunning) {
			System.out.println(" 3 : " + new Timestamp(System.currentTimeMillis()));
		} 
		for (InputTempProdDetail x : listTempSearch) {
			String lotType = x.getLotType();
			String planSD = x.getPlanSystemDate();
			if (lotType.equals("WAITRESULT") || lotType.equals("SCOURING") || lotType.equals("REDYE")) {
				if (planSD.equals("")) {
					listReDyeTmp.add(x);
				} else {
					listPOOnPlan.add(x);
				}
			} else if (lotType.equals("POADDTMP")) {
				listPOTmp.add(x);
			} else {
				if (planSD.equals(Config.C_BLANK)) {
					listPOTmp.add(x);
				} else {
					listPOOnPlan.add(x);
				}
			}
		}
		// GET WORK DAY EXCEPT HOLIDAY
		for (WorkDateDetail beanTmp : workDateAvaList) {
			mapKeyMainSubIdxWork.put(beanTmp.getKeyMainSup()+Config.C_COLON+beanTmp.getRowNum(), beanTmp.getNormalWork());
		}
		for (InputDateRunningDetail bean : workList) {
			Date = bean.getDate();
			indexWork = bean.getId() % Config.C_TOTALDAY;
			mapWorkDateIdx.put(Date, indexWork);
		}
		for (int i = 0; i < groupList.size(); i ++ ) {
			tmpGroupList.clear();
			indexWork = 0;
			removeIndex = 0;
			countIdx = 0;
			indexPlanGroup = 0;
			tmpLastPlanSystemDate = "";
			InputPlanningLotDetail bean = groupList.get(i);
			// CREATE ROW BY ( GROUP:SUPGROUP ) * LOT PERDAY FOR SHOW
			groupNo = bean.getGroupNo();
			subGroup = bean.getSubGroup();
			machineName = bean.getMachineName();
			lotPerDay = bean.getLotPerDay();
			colorType = bean.getColorType();
			groupType = bean.getGroupType();
			keyMainSup = bean.getKeyMainSup();
			// CREATE ROW BY ( GROUP:SUPGROUP ) * LOT PERDAY FOR SHOW
			indexGroupSubStart = indexGroupSubRunning;
			indexGroupSubStop = indexGroupSubRunning+lotPerDay-1;
			for (int j = 0; j < lotPerDay; j ++ ) {
				InputPlanningLotDetail beanTmp = new InputPlanningLotDetail();
				beanTmp.setGroupNo(groupNo);
				beanTmp.setSubGroup(subGroup);
				beanTmp.setKeyMainSup(keyMainSup);
				beanTmp.setMachineName(machineName);
				beanTmp.setLotPerDay(lotPerDay);
				beanTmp.setColorType(colorType);
				beanTmp.setGroupType(groupType);
				beanTmp.setIndexGroupSubRunning(indexGroupSubRunning+j);
				beanTmp.setIndexGroupSubStart(indexGroupSubStart);
				beanTmp.setIndexGroupSubStop(indexGroupSubStop);
				tmpGroupList.add(beanTmp);
			}
			indexGroupSubRunning = indexGroupSubRunning+lotPerDay;
			groupNo = bean.getGroupNo();
			subGroup = bean.getSubGroup();
			removeIndex = 0; 
			// SET PRD ORDER TO POSITION
			level1: for (int j = 0; j < listPOOnPlan.size(); j ++ ) {
				beanTmpPrd = listPOOnPlan.get(j);
				groupNoPrdList = beanTmpPrd.getGroupNo();
				subGroupPrdList = beanTmpPrd.getSubGroup(); 
				if (groupNoPrdList.equals(groupNo) && subGroupPrdList.equals(subGroup)) {
					planSystemDate = beanTmpPrd.getPlanSystemDate();
					if ( ! tmpLastPlanSystemDate.equals(planSystemDate)) {
						countIdx = 0;
						tmpLastPlanSystemDate = planSystemDate;
					}
					indexPlanGroup = mapWorkDateIdx.get(planSystemDate);
					if (countIdx < tmpGroupList.size()) {
						tmpGroupList.set(countIdx, this.setPlanSystemDateToDays(tmpGroupList, indexPlanGroup, beanTmpPrd, countIdx));
						if (tmpLastPlanSystemDate.equals(planSystemDate)) {
							countIdx = countIdx+1;
						}
					} else {

					}
				} else {
					removeIndex = j;
					break level1;
				}
			}

			tmpLastPlanSystemDate = "";
			for (int j = 0; j < removeIndex; j ++ ) {
				listPOOnPlan.remove(0);
			}
			InputTempProdDetail beanTmpHoliday = new InputTempProdDetail();

			beanTmpHoliday.setGroupNo(groupNo);
			beanTmpHoliday.setSubGroup(subGroup);
			beanTmpHoliday.setKeyMainSup(keyMainSup);
			beanTmpHoliday.setColorType(colorType);
			beanTmpHoliday.setNormalWork(this.C_Y);
			// SET HOLIDAY DATE TO POSITION
			for (int j = 0; j < Config.C_TOTALDAY; j ++ ) {
				indexWork = j+1;
				if (mapKeyMainSubIdxWork.get(keyMainSup+Config.C_COLON+indexWork).equals(Config.C_CLOSE_STATUS)) {
					for (int j1 = 0; j1 < lotPerDay; j1 ++ ) {
						tmpGroupList.set(j1, this.setPlanSystemDateToDays(tmpGroupList, j, beanTmpHoliday, j1));
					}
				}
			}
			for (int j = 0; j < tmpGroupList.size(); j ++ ) {
				planningList.add(tmpGroupList.get(j));
			}
		}
		// PO TMP
		tmpGroupList.clear();
		if ( ! listPOTmp.isEmpty()) {
			int mod = Config.C_TOTALDAY;
			int rowTMPLot = (int) Math.ceil((double) listPOTmp.size() / mod);
			indexWork = 0;
			indexRow = 0;
			indexGroupSubStart = indexGroupSubRunning;
			indexGroupSubStop = indexGroupSubRunning+rowTMPLot-1;
			for (int i = 0; i < rowTMPLot; i ++ ) {
				InputPlanningLotDetail beanTmp = new InputPlanningLotDetail();
				beanTmp.setGroupNo(this.C_POTMP);
				beanTmp.setKeyMainSup(this.C_POTMP);
				beanTmp.setSubGroup(Config.C_BLANK);
				beanTmp.setMachineName(Config.C_BLANK);
				beanTmp.setIndexGroupSubRunning(indexGroupSubRunning+i);
				beanTmp.setIndexGroupSubStart(indexGroupSubStart);
				beanTmp.setIndexGroupSubStop(indexGroupSubStop);
				tmpGroupList.add(beanTmp);
			}
			indexGroupSubRunning = indexGroupSubRunning+rowTMPLot;
			for (int i = 0; i < listPOTmp.size(); i ++ ) {
				InputTempProdDetail bean = listPOTmp.get(i);
				if (indexWork == Config.C_TOTALDAY) { 
					indexWork = 0;
					indexRow = indexRow+1;
				}
				tmpGroupList.set(indexRow, this.setPlanSystemDateToDays(tmpGroupList, indexWork, bean, indexRow));
				indexWork = indexWork+1;
			}
			for (int j = 0; j < tmpGroupList.size(); j ++ ) {
				planningList.add(tmpGroupList.get(j));
			}
		} else {
			indexWork = 0;
			indexRow = 0;
			indexGroupSubStart = indexGroupSubRunning;
			indexGroupSubStop = indexGroupSubRunning;
			InputPlanningLotDetail beanTmp = new InputPlanningLotDetail();
			beanTmp.setGroupNo(this.C_POTMP);
			beanTmp.setKeyMainSup(this.C_POTMP);
			beanTmp.setSubGroup(Config.C_BLANK);
			beanTmp.setMachineName(Config.C_BLANK);
			beanTmp.setIndexGroupSubRunning(indexGroupSubRunning);
			beanTmp.setIndexGroupSubStart(indexGroupSubStart);
			beanTmp.setIndexGroupSubStop(indexGroupSubStop);
			planningList.add(beanTmp);
			indexGroupSubRunning = indexGroupSubRunning+1;
		}
		// REDYE TMP
		tmpGroupList.clear();
		if (listReDyeTmp.size() > 0) {
			int mod = Config.C_TOTALDAY;
			int rowTMPLot = (int) Math.ceil((double) listReDyeTmp.size() / mod);
			indexWork = 0;
			indexRow = 0;
			indexGroupSubStart = indexGroupSubRunning;
			indexGroupSubStop = indexGroupSubRunning+rowTMPLot-1;
			for (int i = 0; i < rowTMPLot; i ++ ) {
				InputPlanningLotDetail beanTmp = new InputPlanningLotDetail();
				beanTmp.setGroupNo(this.C_REDYE);
				beanTmp.setKeyMainSup(this.C_REDYE);
				beanTmp.setSubGroup(Config.C_BLANK);
				beanTmp.setMachineName(Config.C_BLANK);
				beanTmp.setIndexGroupSubRunning(indexGroupSubRunning+i);
				beanTmp.setIndexGroupSubStart(indexGroupSubStart);
				beanTmp.setIndexGroupSubStop(indexGroupSubStop);
				tmpGroupList.add(beanTmp);
			}
			indexGroupSubRunning = indexGroupSubRunning+rowTMPLot;
			for (int i = 0; i < listReDyeTmp.size(); i ++ ) {
				InputTempProdDetail bean = listReDyeTmp.get(i);
				if (indexWork == Config.C_TOTALDAY) {
					indexWork = 0;
					indexRow = indexRow+1;
				}
				tmpGroupList.set(indexRow, this.setPlanSystemDateToDays(tmpGroupList, indexWork, bean, indexRow));
				indexWork = indexWork+1;
			}
			for (int j = 0; j < tmpGroupList.size(); j ++ ) {
				planningList.add(tmpGroupList.get(j));
			}
		} else {
			int mod = Config.C_TOTALDAY;
			int rowTMPLot = (int) Math.ceil((double) 10 / mod);
			indexWork = 0;
			indexRow = 0;
			indexGroupSubStart = indexGroupSubRunning;
			indexGroupSubStop = indexGroupSubRunning+rowTMPLot-1;
			for (int i = 0; i < rowTMPLot; i ++ ) {
				InputPlanningLotDetail beanTmp = new InputPlanningLotDetail();
				beanTmp.setGroupNo(this.C_REDYE);
				beanTmp.setKeyMainSup(this.C_REDYE);
				beanTmp.setSubGroup(Config.C_BLANK);
				beanTmp.setMachineName(Config.C_BLANK);
				beanTmp.setIndexGroupSubRunning(indexGroupSubRunning+i);
				beanTmp.setIndexGroupSubStart(indexGroupSubStart);
				beanTmp.setIndexGroupSubStop(indexGroupSubStop);
				tmpGroupList.add(beanTmp);
			}
			indexGroupSubRunning = indexGroupSubRunning+rowTMPLot;
			for (int j = 0; j < tmpGroupList.size(); j ++ ) {
				planningList.add(tmpGroupList.get(j));
			}
		}
		if (bl_checkTimeRunning) {
			System.out.println(" 5 : " + new Timestamp(System.currentTimeMillis()));
		}
		return planningList;
	}

	@Override
	public ArrayList<InputTempProdDetail> getPlanProdFromCalendar(ArrayList<InputFacWorkDateDetail> poList)
	{
		ArrayList<InputTempProdDetail> list = new ArrayList<>();
		String wherePO = " WHERE \r\n";
		String whereFC = " WHERE \r\n";
		String whereRD = " \r\n";
		String orderBy = " order by ";
		InputFacWorkDateDetail bean = poList.get(0);
		String planDate = bean.getDateClickedStr();
		String declarePlan = "" + " declare @BeginDate date  = convert(date,'" + planDate + "', 103) ;	\r\n";
		wherePO += " a.POId is not null and\r\n"
				+ " ( a.OperationEndDate is null or a.OperationEndDate > CONVERT(DATE,GETDATE())  ) AND \r\n"
				+ " ( a.[PlanSystemDate] = @BeginDate )  \r\n";
		whereFC += " a.ForecastId is not null and\r\n"
				+ " ( a.[PlanSystemDate] = @BeginDate )   \r\n"
				+ "  and A.[ForecastDateMonthLast] >  CONVERT(DATE,GETDATE() )   \r\n";
		whereRD += " ( c.[PlanSystemDate] = @BeginDate )   \r\n";

		orderBy += " PlanUserDate ";
		String sql = " " + " SET NoCount ON; \r\n" + declarePlan
//					+ this.declareTempFCDate
				+ this.declarePO
				+ this.declareFC
				+ this.declarePPMMReDye
				+ this.declarePPMMPOAdd
				+ "	SELECT distinct \r\n"
				+ this.selectPO
				+ this.fromPOA
//				+ this.leftJoinMaxF
//				+ this.leftJoinAPD
//				+ this.leftJoinCPP
				+ wherePO
				+ " UNION ALL\r\n"
				+ " SELECT distinct \r\n"
				+ this.selectFC
				+ this.fromFCA
//					+ this.leftJoinFCB
//					+ this.leftJoinFCAPD
				+ whereFC
				+ " union ALL\r\n"
				+ " select * \r\n"
				+ " from ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectRDE
				+ " FROM #tempPPMMReDye as a \r\n"
				+ this.leftJoinRDC
				+ this.leftJoinCPP
				+ " where A.[DataStatus] = 'O' and  \r\n"
				+ "       ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null )  and \r\n"
				+ whereRD
				+ " ) as a \r\n"
				+ " UNION ALL\r\n"
				+ " select * \r\n"
				+ " from ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectPOAdd
				+ " FROM #tempPPMMPOAdd as a \r\n"
				+ this.leftJoinTPAC
				+ this.leftJoinCPP
				+ " where A.[DataStatus] = 'O' and  \r\n"
				+ "       ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null )  and \r\n"
				+ whereRD
				+ " ) as a \r\n"
				+ orderBy;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputTempProdDetail> getPlanProdFromDate(ArrayList<InputFacWorkDateDetail> poList)
	{
		ArrayList<InputTempProdDetail> list = new ArrayList<>();
		String wherePO = " WHERE \r\n";
		String whereFC = " WHERE \r\n";
		String whereRD = " \r\n";
		String orderBy = " order by ";
		InputFacWorkDateDetail bean = poList.get(0);
		String groupNo = bean.getGroupNo();
		String subGroup = bean.getSubGroup();
		String planDate = bean.getDateClickedStr();
		String declarePlan = "" + " declare @BeginDate date  = convert(date,'" + planDate + "', 103) ;\r\n";
		String whereGroup = "" + " (  \r\n";
		String whereGroupRedye = " ";
		whereGroup += " ( a.[GroupNo] = '" + groupNo + "' and a.[SubGroup] = '" + subGroup + "' )";
		whereGroup += " ) \r\n";
		whereGroupRedye = whereGroup.replace("a.", "c.");
		wherePO += " a.POId is not null and\r\n"
				+ " ( a.OperationEndDate is null or a.OperationEndDate > CONVERT(DATE,GETDATE())  ) AND \r\n"
				+ " ( a.[PlanSystemDate] = @BeginDate ) and\r\n"
				+ whereGroup;
		whereFC += " a.ForecastId is not null and\r\n"
				+ " ( a.[PlanSystemDate] = @BeginDate ) and \r\n"
				+ "  A.[ForecastDateMonthLast] >  CONVERT(DATE,GETDATE() ) and \r\n"
				+ whereGroup;
		whereRD += " ( c.[PlanSystemDate] = @BeginDate ) and \r\n" + whereGroupRedye;

		orderBy += " a.GroupNo ,a.SubGroup , PlanSystemDate ";
		String sql = " " + " SET NOCOUNT ON;\r\n" + declarePlan
//					+ this.declareLead
//					+ this.declareTempFCDate
//					+ this.declareFCDate
				+ this.declarePO
				+ this.declareFC
//					+ this.declarePPMMDye
				+ this.declarePPMMReDye
				+ this.declarePPMMPOAdd
				+ "	SELECT distinct \r\n"
				+ this.selectPO
				+ this.fromPOA
//				+ this.leftJoinMaxF
//				+ this.leftJoinAPD
//				+ this.leftJoinCPP
				+ this.leftJoinbRPAP
//					+ this.leftJoinPODFS
				+ wherePO
				+ " UNION ALL\r\n"
				+ " SELECT distinct \r\n"
				+ this.selectFC
				+ this.fromFCA
//					+ this.leftJoinFCB
//					+ this.leftJoinFCAPD
				+ whereFC
				+ " union ALL\r\n"
				+ " select * \r\n"
				+ " from ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectRDE
				+ " FROM #tempPPMMReDye as a \r\n"
				+ this.leftJoinRDC
				+ this.leftJoinCPP
				+ " where A.[DataStatus] = 'O' and  \r\n"
				+ "       ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null ) and\r\n"
				+ "	      C.PlanUserDate IS not NULL AND \r\n"
				+ whereRD
				+ " ) as a \r\n"
				+ " UNION ALL\r\n"
				+ " select * \r\n"
				+ " from ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectPOAdd
				+ " FROM #tempPPMMPOAdd as a \r\n"
				+ this.leftJoinTPAC
				+ this.leftJoinCPP
				+ " where A.[DataStatus] = 'O' and  \r\n"
				+ "       ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null )  and \r\n"
				+ "	      C.PlanSystemDate IS not NULL AND \r\n"
				+ whereRD
				+ " ) as a \r\n"
				+ orderBy;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	public ArrayList<InputTempProdDetail> getProdDetailByMaterialNo(ArrayList<InputLotDragDropDetail> poList)
	{
		ArrayList<InputTempProdDetail> list = null;
		InputLotDragDropDetail bean = poList.get(0);
		InputTempProdDetail dragLotBean = bean.getDragLot();
		String matNo = dragLotBean.getMaterialNo();
		String greigeDue = dragLotBean.getGreigePlan();
		dragLotBean.getArticle();
		String wherePO = " WHERE \r\n";
		String sql = "";
		String orderBy = " order by ";
		// PLAN MANAGE BY SYSTEM AND USER STILL NOT PLAN
		// STILL LACKING IN DYE END BY LOT TMP IN SAP
		wherePO += " a.POId is not null and\r\n" 
				+ " a.[MaterialNo] = '" + matNo + "' and \r\n"
				+ " a.[GreigePlan] = convert(date,'" + greigeDue + "', 103) and \r\n" 
				+ " ( a.[OperationEndDate] is null ) and\r\n"
				+ " a.[ProductionOrder] is null \r\n and \r\n"
				+ " a.[PlanInsteadId] is null and\r\n"
				+ "  a.[POPuangId] is null\r\n";
		orderBy += " [CustomerDue], [POId], [FirstLot] desc , a.[ProductionOrder]  ";

		sql = "" + this.declarePO 
				+ "	SELECT * \r\n"
				+ " FROM ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectPO
//				+ "	   ,ISNUMERIC(LEFT(a.ProductionOrder, 1)) as isNumber\r\n"
				+ "    ,CASE \r\n"
				+ "        WHEN LEFT(a.ProductionOrder, 1) LIKE '[0-9]' THEN 1\r\n"
				+ "        ELSE 0\r\n"
				+ "    END AS isNumber\r\n"
				+ "	   ,ProductionOrderType\r\n"
				+ this.fromPOA 
				+ this.innerJoinbRPAP
				+ wherePO
				+ " ) AS A\r\n"
				+ " where isNumber = 1	\r\n"
				+ orderBy; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputTempProdDetail> getProdDetailByPlanCase(ArrayList<InputLotDragDropDetail> poList)
	{
		ArrayList<InputTempProdDetail> list = null;
		InputLotDragDropDetail bean = poList.get(0);
		String caseStr = bean.getPlanCase();
		// SINGLE TO DOUBLE
//		if(caseStr.equals("SD1") || caseStr.equals("SD2")) {
		if (caseStr.contains("SD") || caseStr.equals("SD")) {
			list = this.getProdDetailByMaterialNo(poList);
		}
		// DOUBLE TO SINGLE
		else {
			list = this.getProdDetailByProdOrder(poList);
		}
		return list;
	}

	public ArrayList<InputTempProdDetail> getProdDetailByProdOrder(ArrayList<InputLotDragDropDetail> poList)
	{
		ArrayList<InputTempProdDetail> list = null;
		InputLotDragDropDetail bean = poList.get(0);
		InputTempProdDetail dragLotBean = bean.getDragLot();
		String prodOrder = dragLotBean.getProductionOrder();
		int poId = dragLotBean.getPoId();
		String wherePO = " WHERE \r\n";
		String sql = "";
		String orderBy = " order by ";
		// PLAN MANAGE BY SYSTEM AND USER STILL NOT PLAN
		// STILL LACKING IN DYE END BY LOT TMP IN SAP
		wherePO += " a.POId is not null and\r\n"
				+ " a.[POId] = '"
				+ poId
				+ "' and \r\n"
				+ " a.[ProductionOrder] = '"
				+ prodOrder
				+ "' and \r\n"
				+ " ( a.[OperationEndDate] is null ) and\r\n"
				+ " a.[ProductionOrder] is null and\r\n"
				+ " a.[PlanInsteadId] is null and\r\n"
				+ "  a.[POPuangId] is null\r\n";
		orderBy += " [CustomerDue], [POId], [FirstLot] desc , a.[ProductionOrder]  ";

		sql = "" + this.declarePO
//				+ this.declarePPMMDye
				+ "	SELECT * \r\n"
				+ " FROM ( \r\n"
				+ "	SELECT distinct \r\n"
				+ this.selectPO
				+ "    ,CASE \r\n"
				+ "        WHEN LEFT(a.ProductionOrder, 1) LIKE '[0-9]' THEN 1\r\n"
				+ "        ELSE 0 \r\n"
				+ "    END AS isNumber\r\n"
//				+ "	   ,ISNUMERIC(LEFT(a.ProductionOrder, 1)) as isNumber\r\n"
				+ "	   ,ProductionOrderType\r\n"
				+ this.fromPOA 
				+ this.innerJoinbRPAP
				+ wherePO
				+ " ) AS A\r\n"
				+ " where isNumber = 1	\r\n"
				+ orderBy;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	public ArrayList<InputTempProdDetail> getProdDetailByTempProdId(ArrayList<InputTempProdDetail> calList)
	{
		ArrayList<InputTempProdDetail> list = null;
		String where = "";
		for (int i = 0; i < calList.size(); i ++ ) {
			InputTempProdDetail bean = calList.get(i);
//			where += "a.Id = '"+bean.getNo()+"' ";
			where += "a.Id = '" + bean.getTempProdId() + "' ";
			if (i < calList.size()-1) {
				where = where + " or ";
			}
		}
		if ( ! where.equals("")) {
			where = " and " + where;
		}
		String wherePO = " WHERE \r\n";
		String sql = "";
		String orderBy = " order by ";
		// PLAN MANAGE BY SYSTEM AND USER STILL NOT PLAN
		// STILL LACKING IN DYE END BY LOT TMP IN SAP
		wherePO += " a.POId is not null and\r\n"
				+ " ( a.[OperationEndDate] is null ) and\r\n"
				+ " a.[ProductionOrder] is null and\r\n"
				+ " a.[PlanInsteadId] is null and\r\n"
				+ "  a.POPuangId is null \r\n"
				+ where;
		orderBy +=
				" isFC  ,IsExpired DESC ,PresetEndDate, [CustomerDue]   ,isRedye DESC ,isPO DESC   , PriorDistChannal desc, [FirstLot] desc , a.[ProductionOrder] DESC   ";

		sql = "" + this.declarePO 
				+ "	SELECT * \r\n"
				+ " FROM ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectPO
				+ "    ,CASE \r\n"
				+ "        WHEN LEFT(a.ProductionOrder, 1) LIKE '[0-9]' THEN 1\r\n"
				+ "        ELSE 0 \r\n"
				+ "    END AS isNumber\r\n"
//				+ "	   ,ISNUMERIC(LEFT(a.ProductionOrder, 1)) as isNumber\r\n"
				+ "	   ,ProductionOrderType\r\n"
				+ this.fromPOA 
				+ this.innerJoinbRPAP
				+ wherePO
				+ " ) AS A\r\n"
				+ " where isNumber = 1	\r\n"
				+ orderBy; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	public ArrayList<InputTempProdDetail> getProductionOrderDetailByProductionOrderFL(String prodOrderFL)
	{
		ArrayList<InputTempProdDetail> list = new ArrayList<>();
		String sql = " "
				+ " SET NoCount ON; \r\n"
				+ "SELECT DFS.[Id] \r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[DueDate]\r\n"
				+ "      ,[Operation] \r\n"
				+ "      ,[OperationStatus]\r\n"
				+ "      ,[AdminStatus]\r\n"
				+ "      ,[OperationStartDate]\r\n"
				+ "      ,[OperationEndDate] \r\n"
				+ "      ,[OperationStartTime]\r\n"
				+ "      ,[OperationEndTime]  \r\n" 
				+ "	     ,( \r\n"
				+ "					SELECT distinct A.AddDays \r\n"
				+ "					FROM  dbo.InputLeadTimeStatus AS A \r\n"
				+ "					LEFT JOIN [PPMM].[dbo].[viewUserStatusCondition] AS B ON A.UserStatusId = B.Id  \r\n"
				+ "					where UserStatusSapId = 'U023'\r\n" // รอผลย้อม Lot แรก
				+ "			) as [AddDays]\r\n"
//				+ "	     ,( \r\n"
//				+ "					SELECT distinct A.AddDays \r\n"
//				+ "					FROM  dbo.InputLeadTimeStatus AS A \r\n"
//				+ "					LEFT JOIN dbo.UserStatusDetail AS B ON A.UserStatusId = B.Id \r\n"
//				+ "					LEFT JOIN dbo.LabStatusDetail AS C ON A.LabStatusId = C.Id\r\n"
//				+ "				    where UserStatusSapId = 'U023'\r\n" // รอผลย้อม Lot แรก
//				+ "			) as [AddDays]\r\n"
				+ "  FROM [PPMM].[dbo].[DataFromSap] as DFS \r\n"	
				+ "	 INNER JOIN [PPMM].[dbo].[viewUserStatusCondition] AS viewUSC ON viewUSC.UserStatus = DFS.UserStatus AND viewUSC.[DyeFocus] = 'O'\r\n"
//				+ "  INNER JOIN [PPMM].[dbo].[UserStatusDetail] AS USD ON USD.UserStatus = DFS.UserStatus \r\n"
//				+ "	 INNER JOIN [PPMM].[dbo].[UserStatusCondtion] AS USCUSD ON USD.UserStatusSapId = USCUSD.UserStatusSapId AND USCUSD.[DyeFocus] = 'O'\r\n"
				+ "  where Operation = 100 AND \r\n"
				+ "		( DFS.[OperationEndDate] IS NULL OR DFS.[OperationEndDate] >= DATEADD(MONTH,-1,GETDATE())) AND\r\n"
				+ "		DFS.[AdminStatus] = '-' AND\r\n"
				+ "     DFS.[ProductionOrder] = '"
				+ prodOrderFL
				+ "' \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	public ArrayList<InputTempProdDetail> getProductionOrderDetailByProductionOrderFLForDyeSAPAfterFLDate(String prodOrderFL)
	{
		ArrayList<InputTempProdDetail> list = new ArrayList<>();
		String sql = " \r\n"
				+ " SET NoCount ON; \r\n"
				+ " IF OBJECT_ID('tempdb..#tempBaseDate') IS NOT NULL   \r\n"
				+ "      DROP TABLE #tempBaseDate;\r\n"
				+ " SELECT DFS.[Id] \r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[DueDate]\r\n"
				+ "      ,[Operation] \r\n"
				+ "      ,[OperationStatus]\r\n"
				+ "      ,[AdminStatus]\r\n"
				+ "      ,[OperationStartDate]\r\n"
				+ "      ,[OperationEndDate] \r\n"
				+ "      ,[OperationStartTime]\r\n"
				+ "      ,[OperationEndTime]  \r\n"

				+ "	     ,( \r\n"
				+ "					SELECT distinct A.AddDays \r\n"
				+ "					FROM  dbo.InputLeadTimeStatus AS A \r\n"
				+ "					LEFT JOIN [PPMM].[dbo].[viewUserStatusCondition] AS B ON A.UserStatusId = B.Id  \r\n"
				+ "					where UserStatusSapId = 'U023'\r\n" // รอผลย้อม Lot แรก
				+ "			) as [AddDays]\r\n"
//				+ "	     ,( \r\n"
//				+ "					SELECT distinct A.AddDays \r\n"
//				+ "					FROM  dbo.InputLeadTimeStatus AS A \r\n"
//				+ "					LEFT JOIN dbo.UserStatusDetail AS B ON A.UserStatusId = B.Id \r\n"
//				+ "					LEFT JOIN dbo.LabStatusDetail AS C ON A.LabStatusId = C.Id\r\n"
//				+ "				    where UserStatusSapId = 'U023'\r\n"
//				+ "       ) as [AddDays]\r\n"
				+ "  into #tempBaseDate\r\n"
				+ "  FROM [PPMM].[dbo].[DataFromSap] as DFS \r\n" 
				+ "	 INNER JOIN [PPMM].[dbo].[viewUserStatusCondition] AS viewUSC ON viewUSC.UserStatus = DFS.UserStatus AND viewUSC.[DyeFocus] = 'O'\r\n"
//				+ "  INNER JOIN [PPMM].[dbo].[UserStatusDetail] AS USD ON USD.UserStatus = DFS.UserStatus \r\n"
//				+ "	 INNER JOIN [PPMM].[dbo].[UserStatusCondtion] AS USCUSD ON USD.UserStatusSapId = USCUSD.UserStatusSapId AND USCUSD.[DyeFocus] = 'O'\r\n"
				+ "  where Operation = 100 AND \r\n"
				+ "		( DFS.[OperationEndDate] >= DATEADD(MONTH,-1,GETDATE())) AND\r\n"
				+ "		DFS.[AdminStatus] = '-' AND\r\n"
				+ "     DFS.[ProductionOrder] = '"
				+ prodOrderFL
				+ "' \r\n"
				+ " IF OBJECT_ID('tempdb..#tempWorkDate') IS NOT NULL   \r\n"
				+ "		DROP TABLE #tempWorkDate;\r\n"
				+ "	select  ROW_NUMBER() OVER ( ORDER BY Date ) as RowIdx, a.Date\r\n"
				+ "	into #tempWorkDate\r\n"
				+ "	from [PPMM].[dbo].[InputDateRunning] as a\r\n"
				+ "	where [NormalWork] = 'O' and \r\n"
				+ "		  a.[Date] >= ( select min([OperationEndDate]) from #tempBaseDate )\r\n"
				+ ""
				+ "  select a.* ,b.[DyeSAPAfterFLDate]\r\n"
				+ "	 from #tempBaseDate as a\r\n"
				+ "	 INNER JOIN (\r\n"
				+ "		 SELECT Id\r\n"
				+ "		,( select  Date\r\n"
				+ "			from #tempWorkDate as sub_a\r\n"
				+ "			where sub_a.Date > a.[OperationEndDate]  \r\n"
				+ "			ORDER BY Date\r\n"
				+ "			OFFSET a.[AddDays] ROWS\r\n"
				+ "			FETCH NEXT 1 ROWS ONLY \r\n"
				+ "			) as [DyeSAPAfterFLDate]\r\n"
				+ "		FROM #tempBaseDate AS A \r\n"
				+ "		WHERE [OperationEndDate] IS NOT NULL \r\n"
				+ "	 ) AS B ON A.Id = B.Id \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputPlanningLotDetail> getSubGroupDetail()
	{
		ArrayList<InputPlanningLotDetail> list = null;
		String sql = ""
				+ " SET NoCount ON; \r\n"
				+ " SELECT distinct \r\n"
				+ "       a.[GroupNo]\r\n"
				+ "      ,a.[SubGroup]\r\n"
				+ "	     ,b.ColorType\r\n"
				+ "		 , STUFF((\r\n"
				+ "			SELECT '  ' +MachineName\r\n"
				+ "			FROM [PPMM].[dbo].[InputMachineDetail] as c\r\n"
				+ "			WHERE a.GroupNo = c.GroupNo and \r\n"
				+ "				  a.SubGroup = c.SubGroup and \r\n"
				+ "				  c.DataStatus = 'O' and \r\n"
				+ "				  MachineName <> '' \r\n"
				+ "			FOR XML PATH('')\r\n"
				+ "			), 1, 2, '') as [MachineName]\r\n"
				+ "	     ,a.GroupType\r\n"
				+ "      ,A.[LotPerDay]\r\n"
				+ " FROM [PPMM].[dbo].[InputSubGroupDeail] as a\r\n"
				+ " INNER JOIN  [PPMM].[dbo].[InputMainGroupDetail] AS b on b.GroupNo = a.GroupNo  \r\n"
				+ " LEFT JOIN  [PPMM].[dbo].[InputMachineDetail] AS c on c.GroupNo = a.GroupNo and c.SubGroup = a.SubGroup\r\n"
				+ " where a.[DataStatus] = 'O' and b.[DataStatus] = 'O'  and a.LotPerDay > 0\r\n"
				+ " order by a.[GroupNo] ,a.[SubGroup] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputPlanningLotDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputPlanningLotDetail> getSubGroupDetail(ArrayList<InputTempProdDetail> tmpList)
	{
		ArrayList<InputPlanningLotDetail> list = null;
		String groupNo = "";
		String subGroup = "";

		String[] array;
		InputTempProdDetail bean = tmpList.get(0);
		String whereGroup = "  ";
		bean.getPlanStartDate();
		bean.getPlanEndDate();
		List<String> groupList = bean.getMainNSubGroupList();
		if (groupList.isEmpty()) {
			whereGroup = "";
		} else {
			whereGroup = "  (  \r\n";
			for (int i = 0; i < groupList.size(); i ++ ) {
				String groupSub = groupList.get(i).replace(" ", Config.C_BLANK);
				array = groupSub.split(Config.C_COLON);
				groupNo = array[0];
				subGroup = array[1];
				whereGroup += " ( a.[GroupNo] = '" + groupNo + "' and a.[SubGroup] = '" + subGroup + "' )";
				if (i < groupList.size()-1) {
					whereGroup += " or \r\n";
				}
			}
			whereGroup = "and " + whereGroup;
			whereGroup += " ) \r\n";
		}
		String sql = ""
				+ " SET NoCount ON; \r\n"
				+ " SELECT distinct \r\n"
				+ "       a.[GroupNo]\r\n"
				+ "      ,a.[SubGroup]\r\n"
				+ "	     ,b.ColorType\r\n"
				+ "		 ,STUFF((\r\n"
				+ "			SELECT '  ' +MachineName\r\n"
				+ "			FROM [PPMM].[dbo].[InputMachineDetail] as c\r\n"
				+ "			WHERE a.GroupNo = c.GroupNo and \r\n"
				+ "				  a.SubGroup = c.SubGroup and \r\n"
				+ "				  c.DataStatus = 'O' and \r\n"
				+ "				  MachineName <> '' \r\n"
				+ "			FOR XML PATH('')\r\n"
				+ "			), 1, 2, '') as [MachineName]\r\n"
				+ "	     ,a.GroupType\r\n"
				+ "      ,sum(a.[LotPerDay]) as [LotPerDay]\r\n"
				+ " FROM [PPMM].[dbo].[InputSubGroupDeail] as a\r\n"
				+ " INNER JOIN  [PPMM].[dbo].[InputMainGroupDetail] AS b on b.GroupNo = a.GroupNo  \r\n"
				+ " left JOIN  [PPMM].[dbo].[InputMachineDetail] AS c on c.GroupNo = a.GroupNo and c.SubGroup = a.SubGroup\r\n"
				+ " where a.[DataStatus] = 'O' and b.[DataStatus] = 'O' and a.LotPerDay > 0\r\n"
				+ " \r\n"
				+ whereGroup
				+ " group by a.[GroupNo] ,a.[SubGroup]  ,b.ColorType,c.[MachineName],a.[GroupType]\r\n "
				+ " order by a.[GroupNo] ,a.[SubGroup] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputPlanningLotDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputTempProdDetail> getTempPONPlanLotRedyeDetailForPushTMR(String caseDate, InputPlanningLotDetail bean,
			String planDate)
	{
		ArrayList<InputTempProdDetail> list = null;
		String wherePO = " WHERE \r\n";
		String whereRedye = " WHERE \r\n";
		String wherePOAdd = " WHERE \r\n";
		String orderBy = " order by ";
		String declared = "";
		String groupNo = "";
		String subGroup = "";
		// PLAN MANAGE BY SYSTEM AND USER STILL NOT PLAN
		if (caseDate.equals("TDY")) {
			wherePO += " \r\n"
					+ " a.POId is not null and \r\n"
					+ " (	 a.[PlanSystemDate] = CONVERT(DATE,GETDATE() ) )  and\r\n"
					+ " ( a.OperationEndDate is null ) \r\n";
			whereRedye += " "
					+ " c.[ReDyeId] is not null and \r\n"
					+ " ( c.[PlanSystemDate] = CONVERT(DATE,GETDATE() ) ) and \r\n"
					+ "   A.[DataStatus] = 'O' \r\n"
//					+ " ( a.OperationEndDate is null )  \r\n"
			;
			wherePOAdd += " "
					+ " c.[TempPOAddId] is not null and \r\n"
					+ " ( c.[PlanSystemDate] = CONVERT(DATE,GETDATE() ) ) and \r\n"
					+ "   A.[DataStatus] = 'O' and   \r\n"
					+ " ( a.OperationEndDate is null )  \r\n";
			orderBy += " [GroupNo], [SubGroup]  ,IsExpired DESC ,isRedye DESC ,isPO DESC , [CustomerDue], PriorDistChannal desc ";
		} else if (caseDate.equals("TMR")) {
			groupNo = bean.getGroupNo();
			subGroup = bean.getSubGroup();
			declared += " declare @BeginDate date  = convert(date, '" + planDate + "', 103) ;\r\n";
//			declared +=  " declare @BeginDate date  = convert(date, GETDATE() , 103) ;\r\n";
//			declared += " declare @NextDate date  = DATEADD(DAY, 1, @BeginDate) ;\r\n" ;
			wherePO += " \r\n"
					+ " a.POId is not null and \r\n"
					+ " ( a.[PlanSystemDate] = CONVERT(DATE, @BeginDate ) )  and\r\n"
					+ " ( a.OperationEndDate is null ) and\r\n"
					+ " a.[GroupNo] = '"
					+ groupNo
					+ "' and\r\n"
					+ " a.[SubGroup] = '"
					+ subGroup
					+ "' \r\n";
			whereRedye += " "
					+ " c.[ReDyeId] is not null and \r\n"
					+ " ( c.[PlanSystemDate] = CONVERT(DATE,@BeginDate) )  and\r\n"
					+ " c.[GroupNo] = '"
					+ groupNo
					+ "' and\r\n"
					+ " c.[SubGroup] = '"
					+ subGroup
					+ "' and\r\n"
					+ " a.[DataStatus] = 'O' and  \r\n"
					+ " c.[DataStatus] = 'O' AND \r\n"
					+ " c.[ReDyeId] is not null   \r\n";
			wherePOAdd += " "
					+ " c.[TempPOAddId] is not null and \r\n"
					+ " ( c.[PlanSystemDate] = CONVERT(DATE,@BeginDate) ) and\r\n"
					+ " c.[GroupNo] = '"
					+ groupNo
					+ "' and\r\n"
					+ " c.[SubGroup] = '"
					+ subGroup
					+ "' and\r\n"
					+ " a.[DataStatus] = 'O' and  \r\n"
					+ " c.[DataStatus] = 'O' AND \r\n"
					+ " c.[ReDyeId] is not null AND \r\n"
					+ " ( a.OperationStatus <> 'PROCESS DONE' or  a.OperationEndDate is null )   \r\n";
			orderBy += " [GroupNo], [SubGroup],[PlanUserDate] desc ,[PlanSystemDate] ,IsExpired DESC ,isRedye DESC ,"
					+ " isPO DESC , [CustomerDue], PriorDistChannal desc ";
		}

		else if (caseDate.equals("TDYNTMR")) {
			groupNo = bean.getGroupNo();
			subGroup = bean.getSubGroup();
			declared += " declare @BeginDate date  = convert(date, GETDATE(), 103) ;\r\n";
			declared += " declare @LastDate date  = convert(date, '" + planDate + "', 103) ;\r\n";
//			declared += " declare @NextDate date  = DATEADD(DAY, 1, @BeginDate) ;\r\n" ;
			wherePO += " \r\n"
					+ " a.POId is not null and \r\n"
					+ " ( a.[PlanSystemDate] = CONVERT(DATE, @BeginDate ) OR a.[PlanSystemDate] = @LastDate )  and\r\n"
					+ " ( a.OperationEndDate is null ) and\r\n"
					+ " a.[GroupNo] = '"
					+ groupNo
					+ "' and\r\n"
					+ " a.[SubGroup] = '"
					+ subGroup
					+ "' \r\n";
			whereRedye += " "
					+ " c.[ReDyeId] is not null and \r\n"
					+ " ( c.[PlanSystemDate] = CONVERT(DATE,@BeginDate) OR c.[PlanSystemDate] = @LastDate)  and\r\n"
					+ " c.[GroupNo] = '"
					+ groupNo
					+ "' and\r\n"
					+ " c.[SubGroup] = '"
					+ subGroup
					+ "' and\r\n"
					+ " a.[DataStatus] = 'O' and  \r\n"
					+ " c.[DataStatus] = 'O'   \r\n";
			wherePOAdd += " "
					+ " c.[TempPOAddId] is not null and \r\n"
					+ " ( c.[PlanSystemDate] = CONVERT(DATE,@BeginDate) OR c.[PlanSystemDate] = @LastDate)  and\r\n"
					+ " c.[GroupNo] = '"
					+ groupNo
					+ "' and\r\n"
					+ " c.[SubGroup] = '"
					+ subGroup
					+ "' and\r\n"
					+ " a.[DataStatus] = 'O' and  \r\n"
					+ " c.[DataStatus] = 'O'   \r\n";
			orderBy += " [GroupNo], [SubGroup],[PlanUserDate] desc ,[PlanSystemDate] ,IsExpired DESC ,isRedye DESC ,"
					+ " isPO DESC , [CustomerDue], PriorDistChannal desc ";
		}
		String sql = " " + " SET NOCOUNT ON;\r\n" + declared 
				+ this.declarePO 
				+ this.declarePPMMReDye
				+ this.declarePPMMPOAdd
				+ "	SELECT distinct \r\n"
				+ this.selectPO
				+ this.fromPOA 
				+ this.leftJoinbRPAP 
				+ wherePO
				+ " UNION ALL\r\n"
				+ " SELECT distinct \r\n"
				+ this.selectRDE
				+ " FROM #tempPPMMReDye as a \r\n"
				+ this.leftJoinRDC
				+ this.leftJoinCPP
				+ whereRedye
				+ " UNION ALL\r\n"
				+ " select * \r\n"
				+ " from ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectPOAdd
				+ " FROM #tempPPMMPOAdd as a \r\n"
				+ this.leftJoinTPAC
				+ this.leftJoinCPP
				+ wherePOAdd
				+ " ) as a \r\n"
				+ orderBy;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputTempProdDetail> getTempProdDetail(ArrayList<InputTempProdDetail> tmpList)
	{
		ArrayList<InputTempProdDetail> list = null;
		String wherePO = " WHERE \r\n";
		String whereFC = " WHERE \r\n";
		String whereRD = " \r\n";
		String orderBy = " order by ";
		String[] array;
		String groupNo = "";
		String subGroup = "";
		InputTempProdDetail bean = tmpList.get(0);
		String whereGroup = "  (  \r\n";
		String whereGroupRedye = " ";
		String planStart = bean.getPlanStartDate();
		String caseWork = bean.getCaseWork();
		List<String> groupList = bean.getMainNSubGroupList();
		for (int i = 0; i < groupList.size(); i ++ ) {
			String groupSub = groupList.get(i).replace(" ", "");
			array = groupSub.split(Config.C_COLON);
			groupNo = array[0];
			subGroup = array[1];
			whereGroup += " ( a.[GroupNo] = '" + groupNo + "' and a.[SubGroup] = '" + subGroup + "' )";
			if (i < groupList.size()-1) {
				whereGroup += " or \r\n";
			}
		}
		whereGroup += " ) \r\n";
		String declarePlan = " " 
		if (caseWork.equals("PlanStart")) {
			declarePlan = ""
					+ " declare @BeginDate date  = convert(date, '"
					+ planStart
					+ "', 103) ;\r\n"
					+ " declare @LastDate date  = @BeginDate ;\r\n";

		} else {
			if (planStart.equals("")) {
				declarePlan = ""
						+ " declare @BeginDate date  = convert(date, GETDATE(), 103) ;\r\n"
						+ " declare @LastDate date  = DATEADD(DAY, 30, @BeginDate) ;\r\n";
			} else {
				declarePlan = ""
						+ " declare @BeginDate date  = convert(date, '"
						+ planStart
						+ "', 103) ;\r\n"
						+ " declare @LastDate date  = DATEADD(DAY, 30, @BeginDate) ;\r\n";
			}
		}
		if (groupList.size() == 0) {
			whereGroup = "";
		} else {
			if (caseWork.equals("PlanStart")) {
				whereGroup = " and ( \r\n" + "     " + whereGroup + " )\r\n";
			} else {
				whereGroup = " and ( \r\n"
						+ "     "
						+ whereGroup
						+ " 	OR ( a.[GroupNo] is null and a.[SubGroup] is null ) \r\n"
						+ " )\r\n";
			}
		}
		whereGroupRedye = whereGroup.replace("a.", "c.");
		// PLAN MANAGE BY SYSTEM AND USER STILL NOT PLAN
		// STILL LACKING IN DYE END BY LOT TMP IN SAP
		wherePO += " a.POId is not null and\r\n"
				+ " ( a.OperationEndDate is null or a.OperationEndDate > CONVERT(DATE,GETDATE())  ) AND \r\n"
				+ " ( ( a.[PlanSystemDate] >= @BeginDate AND a.[PlanSystemDate] <= @LastDate ) or a.[PlanSystemDate] is null ) \r\n"
				+ whereGroup;
		whereFC += " a.ForecastId is not null and\r\n"
				+ " ( a.[PlanSystemDate] >= @BeginDate AND a.[PlanSystemDate] <= @LastDate )   \r\n"
//				+ "  and b.[ForecastDateMonthLast] >  CONVERT(DATE,GETDATE() ) \r\n"
				+ whereGroup;
		whereRD +=
				"( ( c.[PlanSystemDate] >= @BeginDate AND c.[PlanSystemDate] <= @LastDate ) or c.[PlanSystemDate] is null )  \r\n"
						+ whereGroupRedye;
		orderBy += " a.GroupNo ,a.SubGroup , PlanSystemDate ,CustomerDue ";
		String sql = " "
				+ "  SET NoCount ON;  \r\n"
				+ declarePlan  
				+ this.declarePOV2
				+ this.declareFCForShowOnly
				+ "	SELECT distinct \r\n"
				+ this.selectPOV2  
				+ this.fromPOA   
				+ this.leftJoinbRPAP
				+ wherePO
				+ " UNION ALL\r\n"
				+ " SELECT distinct \r\n"
				+ this.selectFCForShowOnly
				+ this.fromFCForShowOnly
				+ whereFC
				+ " union ALL\r\n"
				+ " select * \r\n"
				+ " from ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectRDE   
				+ this.fromPPMMReDyeForShowOnly
				+ this.leftJoinRDC
				+ this.leftJoinCPP
				+ " where A.[DataStatus] = 'O' and  \r\n"
				+ "       ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null ) and  \r\n"
				+ whereRD
				+ " ) as a \r\n"
				+ " UNION ALL\r\n"
				+ " select * \r\n"
				+ " from ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectPOAdd
				+ this.fromPPMMPOAddForShowOnly
				+ this.leftJoinTPAC
				+ this.leftJoinCPP
				+ " where A.[DataStatus] = 'O' and  \r\n"
				+ "       ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null ) and   \r\n"
				+ whereRD
				+ " ) as a \r\n"
				+ orderBy;  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	public ArrayList<InputTempProdDetail> getTempProdDetail(String caseStr)
	{
		ArrayList<InputTempProdDetail> list = null;
		String wherePO = " WHERE \r\n";
		String whereFC = " WHERE \r\n";
		String whereRedye = " WHERE \r\n";
		String wherePOAdd = " WHERE \r\n";
		String sql = ""; 
		// PLAN MANAGE BY SYSTEM AND USER STILL NOT PLAN
		if (caseStr.equals("1LOW")) {
			// STILL LACKING IN DYE END BY LOT TMP IN SAP
		}
		// PLAN MANAGE BY USER
		else if (caseStr.equals("3HIGH")) {
//			// STILL LACKING IN DYE END BY LOT TMP IN SAP
		} else if (caseStr.equals("10ALL")) {
			wherePO += " a.POId is not null and\r\n"
					+ " ( a.PlanUserDate IS NULL ) and \r\n"
					+ " ( a.[PlanSystemDate] is null or a.[PlanSystemDate] > CONVERT(DATE,GETDATE() ) ) and\r\n"
					+ "  a.OperationEndDate is null \r\n";
			whereFC += " a.ForecastId is not null and\r\n"
					+ " ( a.PlanUserDate is null ) and \r\n"
					+ " A.[ForecastDateMonthLast] >  CONVERT(DATE,GETDATE() ) \r\n";
			whereRedye += ""
					+ " A.[DataStatus] = 'O' and \r\n"
					+ " (\r\n"
					+ " 	( C.PlanUserDate IS NULL ) AND\r\n"
					+ " 	( c.[PlanSystemDate] is null or c.[PlanSystemDate] > CONVERT(DATE,GETDATE() )   ) \r\n"
					+ "	) and\r\n"
					+ " GroupOptions <> '' and\r\n"
					+ " ( c.[DataStatus] = 'O' OR c.[DataStatus] is null )\r\n ";
			wherePOAdd += ""
					+ " A.[DataStatus] = 'O' and \r\n"
					+ " (\r\n"
					+ " 	( C.PlanUserDate IS NULL ) AND\r\n"
					+ " 	( c.[PlanSystemDate] is null or c.[PlanSystemDate] > CONVERT(DATE,GETDATE() )   ) \r\n"
					+ "	) and\r\n"
					+ " GroupOptions <> '' and\r\n"
					+ " ( c.[DataStatus] = 'O' OR c.[DataStatus] is null )\r\n ";
			// ------------------- SETTING SYSTEM PLAN ---------------------
			String declareFirst = " "
					+ " IF OBJECT_ID('tempdb..#tempSLFirst') IS NOT NULL   \r\n"
					+ "		DROP TABLE #tempSLFirst; \r\n"
					+ " select 1 as Filter ,row_number() over ( order by isFC  ,IsExpired DESC ,PresetEndDate, [ForSortDueAndCustDue]   ,isRedye DESC ,isPO DESC , PriorDistChannal desc, [FirstLot] desc , a.[ProductionOrder] DESC   ) as rn\r\n"
					+ " ,a.*\r\n"
					+ " into #tempSLFirst\r\n"
					+ " from ( \r\n"
					+ " SELECT distinct \r\n"
					+ this.selectPO
					+ this.fromPOA 
					+ wherePO
					+ " UNION all\r\n"
					+ " SELECT distinct \r\n"
					+ this.selectFC
					+ this.fromFCA
					+ whereFC
					+ " UNION all\r\n"
					+ " SELECT distinct \r\n"
					+ this.selectRDE
					+ " FROM #tempPPMMReDye as a \r\n"
					+ this.leftJoinRDC
					+ this.leftJoinCPP
					+ whereRedye
					+ " UNION all\r\n"
					+ " SELECT distinct \r\n"
					+ this.selectPOAdd
					+ " FROM #tempPPMMPOAdd as a \r\n"
					+ this.leftJoinTPAC
					+ this.leftJoinCPP
					+ wherePOAdd
					+ " ) as a\r\n";
//			------------------- SETTING USER PLAN ---------------------
			wherePO = " where a.POId is not null and\r\n"
					+ " ( a.PlanUserDate IS NOT NULL  ) and\r\n"
					+ " ( a.[PlanSystemDate] > CONVERT(DATE,GETDATE() ) )  and\r\n"
					+ " ( a.OperationEndDate is null or a.OperationEndDate > CONVERT(DATE,GETDATE()) )\r\n";
			whereRedye = "  where  "
					+ " ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null )  and \r\n"
					+ " A.[DataStatus] = 'O' and \r\n"
					+ " ( c.[PlanSystemDate] > CONVERT(DATE,GETDATE() ) )  and\r\n"
					+ " ( C.PlanUserDate IS not NULL )    \r\n";

			wherePOAdd = "  where "
					+ " ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null )  and \r\n"
					+ " A.[DataStatus] = 'O' and  \r\n"
					+ " ( c.[PlanSystemDate] > CONVERT(DATE,GETDATE() ) )  and\r\n"
					+ " ( C.PlanUserDate IS not NULL   ) \r\n";
			String declareSecond = ""
					+ " IF OBJECT_ID('tempdb..#tempSLSecond') IS NOT NULL \r\n"
					+ " 	DROP TABLE #tempSLSecond; \r\n"
					+ " select 0 as Filter,row_number() over ( order by a.[PlanUserDate] , IsExpired DESC,PresetEndDate ,[ForSortDueAndCustDue] ,isRedye DESC ,isPO DESC ,a.[GroupNo] ,a.[SubGroup], PriorDistChannal desc   ) as rn\r\n"
					+ " ,* \r\n"
					+ " into #tempSLSecond\r\n"
					+ " from ( \r\n"
					+ "	SELECT distinct \r\n"
					+ this.selectPO
					+ this.fromPOA 
					+ wherePO
					+ " UNION all\r\n"
					+ " SELECT distinct \r\n"
					+ this.selectRDE
					+ " FROM #tempPPMMReDye as a \r\n	"
					+ this.leftJoinRDC
					+ this.leftJoinCPP
					+ whereRedye
					+ " UNION all\r\n"
					+ " SELECT distinct \r\n"
					+ this.selectPOAdd 
					+ " FROM #tempPPMMPOAdd as a \r\n"
					+ this.leftJoinTPAC
					+ this.leftJoinCPP
					+ wherePOAdd
					+ " ) as a\r\n";
//			------------------- SETTING FINAL PLAN ---------------------
			sql = " " + " SET NoCount ON; \r\n" 
					+ this.declarePO 
					+ this.declareFC
					+ this.declarePPMMReDye
					+ this.declarePPMMPOAdd 
					+ declareFirst
					+ " IF OBJECT_ID('tempdb..#tempFromFC') IS NOT NULL DROP TABLE #tempFromFC;  \r\n"
					+ " IF OBJECT_ID('tempdb..#tempFCDate') IS NOT NULL DROP TABLE #tempFCDate; \r\n"
					+ declareSecond
					+ " \r\n"
					+ " IF OBJECT_ID('tempdb..#tempPPMMPOAdd') IS NOT NULL DROP TABLE #tempPPMMPOAdd; \r\n"
					+ " IF OBJECT_ID('tempdb..#tempPPMMReDye') IS NOT NULL DROP TABLE #tempPPMMReDye; \r\n"
					+ " IF OBJECT_ID('tempdb..#tempFromPO') IS NOT NULL DROP TABLE #tempFromPO; \r\n"
					+ " IF OBJECT_ID('tempdb..#tempPOMainNPOInstead') IS NOT NULL DROP TABLE #tempPOMainNPOInstead;\r\n"
					+ " select * from #tempSLFirst\r\n"
					+ " UNION all\r\n"
					+ " select * from #tempSLSecond\r\n"
					+ " order by Filter,rn"; 
		}  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputTempProdDetail> getTempProdDetailByGroupNo(ArrayList<InputTempProdDetail> tmpList)
	{
		ArrayList<InputTempProdDetail> list = null;
		String wherePO = " WHERE \r\n";
		String whereFC = " WHERE \r\n";
		String whereRD = " \r\n";
		String orderBy = " order by ";
		String groupNo = "";
		String subGroup = "";
		InputTempProdDetail bean = tmpList.get(0);
		String whereGroup = "  ";
		String whereGroupRedye = " ";
		groupNo = bean.getGroupNo();
		subGroup = bean.getSubGroup();
		whereGroup += " a.[GroupNo] = '" + groupNo + "' \r\n";
		if ( ! subGroup.equals("")) {
			whereGroup += " and a.[SubGroup] = '" + subGroup + "' \r\n";
		}
		whereGroupRedye = whereGroup.replace("a.", "c.");
		// PLAN MANAGE BY SYSTEM AND USER STILL NOT PLAN
		// STILL LACKING IN DYE END BY LOT TMP IN SAP
		wherePO += " a.POId is not null and\r\n"
				+ " ( a.OperationEndDate is null or a.OperationEndDate > CONVERT(DATE,GETDATE())  ) AND \r\n"
				+ " ( a.[PlanSystemDate] >= CONVERT(DATE,GETDATE()) ) and\r\n"
				+ whereGroup;
		whereFC += " a.ForecastId is not null and\r\n"
				+ " ( a.[PlanSystemDate] >= CONVERT(DATE,GETDATE())  ) and \r\n"
				+ "    A.[ForecastDateMonthLast] >  CONVERT(DATE,GETDATE() ) and\r\n"
				+ whereGroup;
		whereRD += " ( c.[PlanSystemDate] >= CONVERT(DATE,GETDATE())  ) and\r\n" + whereGroupRedye;
		orderBy += " a.GroupNo ,a.SubGroup ,PlanSystemDate , RedyeId , [POId] ,CustomerDue";
		String sql = " "
				+ " SET NOCOUNT ON;\r\n"
				+ this.declarePO
				+ this.declareFC
				+ this.declarePPMMReDye
				+ this.declarePPMMPOAdd
				+ "	SELECT distinct \r\n"
				+ this.selectPO
				+ this.fromPOA
				+ this.leftJoinbRPAP
				+ wherePO
				+ " UNION ALL\r\n"
				+ " SELECT distinct \r\n"
				+ this.selectFC
				+ this.fromFCA
				+ whereFC
				+ " union ALL\r\n"
				+ " select * \r\n"
				+ " from ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectRDE
				+ " FROM #tempPPMMReDye as a \r\n"
				+ this.leftJoinRDC
				+ this.leftJoinCPP
				+ " where A.[DataStatus] = 'O' and  \r\n"
				+ "	      ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null )  and \r\n"
				+ "       C.PlanUserDate IS not NULL AND \r\n"
				+ whereRD
				+ " ) as a \r\n"
				+ " UNION ALL\r\n"
				+ " select * \r\n"
				+ " from ( \r\n"
				+ " SELECT distinct \r\n"
				+ this.selectPOAdd
				+ " FROM #tempPPMMPOAdd as a \r\n"
				+ this.leftJoinTPAC
				+ this.leftJoinCPP
				+ " where A.[DataStatus] = 'O' and  \r\n"
				+ "       ( c.[DataStatus] <> 'X'  or c.[DataStatus] is null )  and \r\n"
				+ "	      C.PlanSystemDate IS not NULL AND \r\n"
				+ whereRD
				+ " ) as a \r\n"
				+ orderBy; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	@Override
	public String getThirdPosLot(String month)
	{
		String thirdPos = "";
		switch (month) {
		case "01":
			thirdPos = "A";
			break;
		case "02":
			thirdPos = "B";
			break;
		case "03":
			thirdPos = "C";
			break;
		case "04":
			thirdPos = "D";
			break;
		case "05":
			thirdPos = Config.C_ERR_ICON_STATUS;
			break;
		case "06":
			thirdPos = "F";
			break;
		case "07":
			thirdPos = "G";
			break;
		case "08":
			thirdPos = "H";
			break;
		case "09":
			thirdPos = Config.C_SUC_ICON_STATUS;
			break;
		case "10":
			thirdPos = "J";
			break;
		case "11":
			thirdPos = "K";
			break;
		case "12":
			thirdPos = "L";
			break;
		}
		return thirdPos;
	}

	@Override
	public ArrayList<InputTempProdDetail> handlerProdOrderFirstLotWithProdOrderFL(ArrayList<InputTempProdDetail> poList)
	{
		String systemStatus = "";
		String iconStatus = Config.C_SUC_ICON_STATUS;
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		ArrayList<InputTempProdDetail> listTemp;
		InputTempProdDetail bean = poList.get(0);
		String prodOrderFL = bean.getProductionOrderFirstLot();
		if ( ! prodOrderFL.equals("")) {
			listTemp = this.getProductionOrderDetailByProductionOrderFLForDyeSAPAfterFLDate(prodOrderFL);
			if (listTemp.size() == 0) {
			} else {
				listTemp.get(0).setTempPlanningId(bean.getTempPlanningId());
				iconStatus = tplModel.updateTempPlanningLotForDyeSAPAfterFLDateWithTempPlanningId(listTemp);
				this.rePlanningLot();
			}
		}
		if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) {
		} else {
			systemStatus = "Please contact IT.";
		}
		InputTempProdDetail bean1 = new InputTempProdDetail();
		bean1.setIconStatus(iconStatus);
		bean1.setSystemStatus(systemStatus);
		poList.clear();
		poList.add(bean1);
		return poList;
	}

	// boat here
	private void replaceTempProdWithReal(ArrayList<InputApprovedDetail> listAPP)
	{
		// TODO Auto-generated method stub
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		ReportModel rpModel = new ReportModel(this.conType);
		String sevenToEightPos = "";
//		String custNo = "";
		String prodOrder = "";
		String prodOrderCheck = "";
		String changeBy = "";
		String sorDueDateStr = "";
		String firstPos = "";
		String secondPos = "";
		String thirdPos = "";
		String keyWord = "";
		String poType = "";
		for (int i = 0; i < listAPP.size(); i ++ ) {
			InputApprovedDetail bean = listAPP.get(i); 
			sorDueDateStr = bean.getSorDueDate();
			prodOrder = bean.getProductionOrder();
			changeBy = bean.getApprovedBy();
			poType = bean.getPoType();
			// get data from table for recheck templot and get all lot.
			// for case order puang too
			// STILL TEMP LOT
			// boat here 
			ArrayList<PlanningReportDetail> listProd = rpModel.getProductionOrderByPOId(bean.getPoId()); 
			if ( ! listProd.isEmpty()) {
				PlanningReportDetail beanTmp = listProd.get(0);
				prodOrderCheck = beanTmp.getProductionOrder();
				if ( ! poType.equals("POSub Puang")) {
					if (this.sshUtl.isNumeric(prodOrderCheck.substring(0, 1))) {
						String[] array = sorDueDateStr.split("/");
						int int_month = Integer.parseInt(array[1]);
						String month = String.format("%02d", int_month);
						String year = array[2];
						year = year.substring(3, 4); // yy 'MM'
						// month = month.substring(1,2); //yy 'MM'
						firstPos = prodOrderCheck.substring(1, 2);
						secondPos = year; // y 'y' MM
						thirdPos = this.getThirdPosLot(month);
						keyWord = firstPos+secondPos+thirdPos;
						ArrayList<ProdOrderRunningDetail> listRunning = porModel.getProdOrderRunningDetail(keyWord);
						if (listRunning.isEmpty()) {
							bgjModel.execUpsertToProdOrderRunning(keyWord);
							listRunning = porModel.getProdOrderRunningDetail(keyWord);
						}
						String checkPosSeven = prodOrder.substring(6, prodOrder.length());
						if (checkPosSeven.contains("H")) {
							sevenToEightPos = "H";
						} else if (prodOrder.contains("/P")) {
							sevenToEightPos = "/P";
						} else if (prodOrder.contains("/S")) {
							sevenToEightPos = "/S";
						} else {
							sevenToEightPos = "";
						} 
						ArrayList<ProdOrderRunningDetail> listRunningUsed = new ArrayList<>();
						for (int i1 = 0; i1 < listProd.size(); i1 ++ ) {
							beanTmp = listProd.get(i1);//
							ProdOrderRunningDetail beanTmpRunning = listRunning.get(i1);// T0A001
							String prodOrderTemp = beanTmp.getProductionOrder();
							String prodOrderRunning = beanTmpRunning.getProductionOrder()+sevenToEightPos;
							bgjModel.execReplacedProdOrderOldWithNew(prodOrderTemp, prodOrderRunning);
							beanTmpRunning.setDataStatus(Config.C_CLOSE_STATUS);
							beanTmpRunning.setProductionOrderTemp(prodOrderTemp);
							beanTmpRunning.setRemark(this.C_APPROVED);
							beanTmpRunning.setChangeBy(changeBy);
							listRunningUsed.add(beanTmpRunning);
						}
						porModel.updateProdOrderRunningWithId(listRunningUsed);
					}
				} else {
				}
			}
		}
		bgjModel.execHandlerPlanLotSORDetail();
	}

	// NORMAL VERSION
	@Override
	public void rePlanningLot()
	{
		GroupWorkDateModel gwdModel = new GroupWorkDateModel(this.conType);
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		HashMap<String, Integer> mapWorkStrIdx = new HashMap<>(); 
		HashMap<String, String> mapWorkIdxStr = new HashMap<>();
		HashMap<String, Integer> mapKMSLotPerDay = new HashMap<>();
		HashMap<String, Integer> mapKMSWDLotPerDay = new HashMap<>();
		HashMap<Integer, String> mapFirstLotPlanningDate = new HashMap<>();
		HashMap<String, String> mapPOAddFirstLotPlanningDate = new HashMap<>();
		HashMap<String, Date> mapColorTypeMinDate = new HashMap<>();
		HashMap<String, Integer> mapCountLotPerDay = new HashMap<>();

//		เย็บประกบ
		HashMap<String, String> mapYepPraKob = new HashMap<>();

		ArrayList<InputTempProdDetail> planningTempPlanningIdList = new ArrayList<>();
		ArrayList<InputTempProdDetail> planningLotList = new ArrayList<>();
		ArrayList<InputTempProdDetail> planningRedyeList = new ArrayList<>();
		ArrayList<InputTempProdDetail> planningPOAddedList = new ArrayList<>();

		ArrayList<InputTempProdDetail> lotSystemList = new ArrayList<>();
		ArrayList<InputTempProdDetail> lotSystemAfterFirstLotList = new ArrayList<>();
		ArrayList<InputTempProdDetail> lotUserList = new ArrayList<>();
		InputTempProdDetail beanLot;
		boolean bl_checkAvailable = false;
		List<String> groupOptionsList;
		Calendar cal;
		String groupNo = "";
		String subGroup = "";
		String po = "";
		String isFirstLot = "";
		String firstLotDateStr = "";
		String keyMainSupWorkDate = "";
		String keyMainSupIdx;
		String planUserDateStr = "";
		String tmpFLDateStr = "";
		String dyeAfterGreigeInBeginStr = "";
		String tmpDateStr = "";
		String lotType = "";
		String keyMainSup = "";
		String groupBegin = "";
		String poIdInstead = "";
		String greigeInDate = "";
		String planGreigeDate = "";
		String userStatus = "";
		@SuppressWarnings("unused")
		String productionOrderType = "";
		String statusAfterDateStr = "";
		String dyeSAPAfterFLDateStr = "";
		@SuppressWarnings("unused") String prodOrder = "";
		String oldGroupNo = "";
		String oldSubGroup = "";
		String oldPlanSystemDate = "";
		@SuppressWarnings("unused") String oldDataStatus = "";
		String tmpKeyMainSup = "";
		String tmpKeyMainSupWorkDate = "";
		Date tmpDate = null;
		Date tmpDateLast = null;
		Date dyeAfterGreigeInBeginDate = null;
		Date statusAfterDate = null;
		Date dyeSAPAfterFLDate = null;
		int poId = 0;
		int ForecastId = 0;
		int lotPerDay = 0;
		int dyeAfterCFM = 0;
		int countLotPerDay = 0;
		int counterBreaker = 0;
		int idxDate = 0;
		int reDyeId = 0;
		int haveFirstLot = 0;
		int TempPOAddId = 0;
		int tempPlanningId = 0;
		Date today = new Date();
		String todayStr = this.sdf2.format(today);
		boolean bl_haveLotInSap = true;
		boolean bl_checkTimeRunning = false;
		boolean bl_isUpdate = true;
//	   bl_isUpdate = false;
		boolean bl_isPlanAble = true;
//	   bl_checkTimeRunning = true;
		bgjModel.execHandlerProdOrderType();
		if (bl_checkTimeRunning) {
			System.out.println(" 1 : " + new Timestamp(System.currentTimeMillis()));
		}
//	   	ArrayList<InputPlanningLotDetail> groupList = this.getSubGroupDetail();
		if (bl_checkTimeRunning) {
			System.out.println(" 2 : " + new Timestamp(System.currentTimeMillis()));
		}
		ArrayList<WorkDateDetail> workList = gwdModel.getGroupWorkDateDetail("TMR", Config.C_OPEN_STATUS);
		if (bl_checkTimeRunning) {
			System.out.println(" 5.5 start : " + new Timestamp(System.currentTimeMillis()));
		}
		ArrayList<InputTempProdDetail> allList = this.getTempProdDetail("10ALL");
		if (bl_checkTimeRunning) {
			System.out.println(" 5.5 end : " + new Timestamp(System.currentTimeMillis()));
		}
		int filter = 0;
		for (int i = 0; i < allList.size(); i ++ ) {
			InputTempProdDetail bean = allList.get(i);
			String dyeSAPAfterFLStatus = bean.getDyeSAPAfterFLStatus();
			filter = bean.getFilter();
			dyeSAPAfterFLDateStr = bean.getDyeSAPAfterFLDate();
			if (filter == 0 && dyeSAPAfterFLDateStr.equals("")) {
				lotUserList.add(bean);
			} else if (dyeSAPAfterFLStatus.equals(Config.C_CLOSE_STATUS) && ! dyeSAPAfterFLDateStr.equals(Config.C_BLANK)) {
				lotSystemAfterFirstLotList.add(bean);
			} else if (filter == 0) {
				lotUserList.add(bean);
			} else {
				lotSystemList.add(bean);
			}
		} 
		try {
			// SET FORECAST MINDATE BLACK COLOR
			mapColorTypeMinDate.put("Black", null);
			mapColorTypeMinDate.put("Color", null); 
			// GET WORK DAY EXCEPT HOLIDAY
			for (WorkDateDetail beanTmp : workList) {
				mapWorkStrIdx.put(beanTmp.getKeyMainSup()+Config.C_COLON+beanTmp.getWorkDate(), beanTmp.getRowNum());
				mapWorkIdxStr.put(beanTmp.getKeyMainSup()+Config.C_COLON+beanTmp.getRowNum(), beanTmp.getWorkDate()); 
				mapKMSLotPerDay.put(beanTmp.getKeyMainSup(), Integer.parseInt(beanTmp.getLotPerDay()));
				mapKMSWDLotPerDay.put(beanTmp.getKeyMainSup()+Config.C_COLON+beanTmp.getWorkDate(),
						Integer.parseInt(beanTmp.getLotPerDayInWork()));
			}
			// Planning User Date 3HIGH
			for (int i = 0; i < lotUserList.size(); i ++ ) {
				countLotPerDay = 0;
				bl_isPlanAble = true;
				beanLot = lotUserList.get(i);
				beanLot.setCreateBy(Config.C_SYSTEM);
				beanLot.setDataStatus(Config.C_OPEN_STATUS);
				poId = beanLot.getPoId();
				TempPOAddId = beanLot.getTempPOAddId();
				userStatus = beanLot.getUserStatus();
				isFirstLot = beanLot.getFirstLot();
				planUserDateStr = beanLot.getPlanUserDate();
				groupNo = beanLot.getGroupNo();
				subGroup = beanLot.getSubGroup();
				lotType = beanLot.getLotType();
				keyMainSup = beanLot.getKeyMainSup();
				keyMainSupWorkDate = keyMainSup+Config.C_COLON+planUserDateStr;
				poIdInstead = beanLot.getPoIdInstead();
				tempPlanningId = beanLot.getTempPlanningId();
				prodOrder = beanLot.getProductionOrder();
				oldGroupNo = beanLot.getGroupNo();
				oldSubGroup = beanLot.getSubGroup();
				oldPlanSystemDate = beanLot.getPlanSystemDate();
				oldDataStatus = beanLot.getDataStatus();
				bl_isPlanAble = true;
				if (mapWorkStrIdx.get(keyMainSupWorkDate) == null) {
					bl_isPlanAble = false;
					tmpDate = null;
					tmpDateStr = null;
					groupNo = null;
					subGroup = null;
					countLotPerDay = 0;
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanUserDate(tmpDateStr);
					beanLot.setPlanUserChangeDate(todayStr);
					beanLot.setPlanBy(this.C_CANCELED);
					beanLot.setDataStatus(this.C_P);
					beanLot.setPlanIndex(countLotPerDay);
				} else {
					if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
						countLotPerDay = 1;
						mapCountLotPerDay.put(keyMainSupWorkDate, 1);
						bl_isPlanAble = true;
					} else {
						if (mapKMSLotPerDay.get(keyMainSup) == null) { 
							bl_isPlanAble = false;
						} else {
							lotPerDay = mapKMSLotPerDay.get(keyMainSup);
							countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);
							if (countLotPerDay < lotPerDay) {
								countLotPerDay = countLotPerDay+1;
								mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
								bl_isPlanAble = true;
							} // > Lot per day something wrong push until next workday
							else {
								bl_isPlanAble = false;
							}
						}
					}
				} 
				if ( ! bl_isPlanAble) {
					tmpDate = null;
					tmpDateStr = null;
					groupNo = null;
					subGroup = null;
					countLotPerDay = 0;
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanUserDate(tmpDateStr);
					beanLot.setPlanUserChangeDate(todayStr);
					beanLot.setPlanBy(this.C_CANCELED);
					beanLot.setDataStatus(this.C_P);
					beanLot.setPlanIndex(countLotPerDay);
				} else {
					beanLot.setPlanSystemDate(planUserDateStr);
					beanLot.setPlanIndex(countLotPerDay);
					mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
					if (isFirstLot.equals(this.C_Y)) {
						if ( ! poIdInstead.equals(Config.C_BLANK)) {
							String[] array = poIdInstead.split(",");
							for (String element : array) {
								int poIdAR = Integer.parseInt(element);
								mapFirstLotPlanningDate.put(poIdAR, planUserDateStr + "|" + keyMainSup);
							}
						} else {
							if (poId != 0) {
								mapFirstLotPlanningDate.put(poId, planUserDateStr + "|" + keyMainSup);
							} else if (TempPOAddId != 0) {
								mapPOAddFirstLotPlanningDate.put(po, planUserDateStr + "|" + keyMainSup);
							}
						}
					}
					if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
						// e ACCEPT 1 ONLY
						if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
							mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
						}
					}
				} 
				if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) { 
					if (tempPlanningId != 0) {
						planningTempPlanningIdList.add(beanLot);
					} else if (lotType.equals(this.C_REDYE) || lotType.equals(this.C_SCOURING)
							|| lotType.equals(this.C_WAITRESULT)) {
						planningRedyeList.add(beanLot);
					} else if (lotType.equals(this.C_POADD)) {
						planningPOAddedList.add(beanLot);
					} else {
						planningLotList.add(beanLot);
					}
				}
			}
			// Planning User Date 2.5 HIGH
			for (int i = 0; i < lotSystemAfterFirstLotList.size(); i ++ ) {
				bl_isPlanAble = true;
				countLotPerDay = 0;
				beanLot = lotSystemAfterFirstLotList.get(i);
				beanLot.setCreateBy(Config.C_SYSTEM);
				beanLot.setDataStatus(Config.C_OPEN_STATUS);
				poId = beanLot.getPoId();
				TempPOAddId = beanLot.getTempPOAddId();
				ForecastId = beanLot.getForecastId();
				userStatus = beanLot.getUserStatus();
				dyeAfterCFM = beanLot.getDyeAfterCFM();
				beanLot.getPlanSystemDate();
				planUserDateStr = beanLot.getPlanUserDate();
				groupOptionsList = beanLot.getGroupOptionsList();
				groupBegin = beanLot.getGroupBegin();
				groupNo = beanLot.getGroupNo();
				subGroup = beanLot.getSubGroup();
				lotType = beanLot.getLotType();
				keyMainSup = beanLot.getKeyMainSup();
				keyMainSupWorkDate = keyMainSup+Config.C_COLON+planUserDateStr;
				haveFirstLot = beanLot.getHaveFirstLot();
				poIdInstead = beanLot.getPoIdInstead();
				po = beanLot.getPo();
				dyeSAPAfterFLDateStr = beanLot.getDyeSAPAfterFLDate();
				tempPlanningId = beanLot.getTempPlanningId();
				prodOrder = beanLot.getProductionOrder();
				oldGroupNo = beanLot.getGroupNo();
				oldSubGroup = beanLot.getSubGroup();
				oldPlanSystemDate = beanLot.getPlanSystemDate();
				oldDataStatus = beanLot.getDataStatus();
 				if ( ! dyeSAPAfterFLDateStr.equals(Config.C_BLANK)) {
					dyeSAPAfterFLDate = this.sdf2.parse(dyeSAPAfterFLDateStr);
				}
				if ((poId > 0 || TempPOAddId > 0) && ! groupBegin.equals(Config.C_BLANK) // &&
																							// !dyeSAPAfterFLDateStr.equals(Config.C_BLANK)
				) {
					cal = Calendar.getInstance();
					tmpDate = dyeSAPAfterFLDate;
					cal.setTime(tmpDate);
					bl_checkAvailable = false;
					counterBreaker = 0;
					while ( ! bl_checkAvailable) {
						cal.setTime(cal.getTime());
						tmpDate = cal.getTime();
						tmpDateStr = sdf2.format(tmpDate);
						tmpKeyMainSup = groupBegin; 
						tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr; 
						// check that date is work date ?
						if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null) { 
						} else {
							lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);  
							keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
							// check that date is work date ?
							if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
								if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
									if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
										// e ACCEPT 1 ONLY
										if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
											mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
											countLotPerDay = 1;
											bl_checkAvailable = true;
											break;
										} else {

										}
									} else {
										if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
											countLotPerDay = 1;
											bl_checkAvailable = true;
											break;
										} else {
											countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET
																										// countLotPerDay
																										// , IF NULL = 0
											if (countLotPerDay < lotPerDay) {
												countLotPerDay = countLotPerDay+1;
												bl_checkAvailable = true;
												break;
											}
										}
									}
								} else {
									countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET countLotPerDay ,
																								// IF NULL = 0
									if (countLotPerDay < lotPerDay) {
										if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
											// e ACCEPT 1 ONLY
											if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
												mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
												countLotPerDay = 1;
												bl_checkAvailable = true;
												break;
											}
										} else {
											if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
												countLotPerDay = 1;
												bl_checkAvailable = true;
												break;
											} else {
												countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET
																											// countLotPerDay
																											// , IF NULL
																											// = 0
												if (countLotPerDay < lotPerDay) {
													countLotPerDay = countLotPerDay+1;
													bl_checkAvailable = true;
													break;
												}
											}
										}
									}
								}
							}
						}
						cal.add(Calendar.DATE, 1);
						counterBreaker += 1;
						if (counterBreaker == 1500) {
							bl_checkAvailable = true;
							bl_isPlanAble = false;
						}
					} 
					if ( ! bl_isPlanAble) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0;
						beanLot.setPlanBy(this.C_CANTPLAN);
						beanLot.setDataStatus(this.C_P);
					} else {
						String[] tmpArray = tmpKeyMainSup.split(Config.C_COLON);
						groupNo = tmpArray[0];
						subGroup = tmpArray[1];
						mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
						beanLot.setPlanBy(this.C_FIRSTLOTREMARK);
						beanLot.setDataStatus(Config.C_OPEN_STATUS);

					}
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanUserDate(tmpDateStr);
					beanLot.setPlanUserChangeDate(todayStr);
					beanLot.setDyeSAPAfterFLDate(Config.C_BLANK);
					beanLot.setPlanIndex(countLotPerDay);
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else if (poId > 0) {
							planningLotList.add(beanLot);
						} else {
							planningPOAddedList.add(beanLot);
						}
					}
				}
			}
			Date greigeDate = null;
			for (int i = 0; i < lotSystemList.size(); i ++ ) {
				bl_checkAvailable = false;
				bl_isPlanAble = true;
				bl_haveLotInSap = true;
				countLotPerDay = 0;
				counterBreaker = 0;
				idxDate = 0;
				tmpKeyMainSup = "";
				keyMainSup = "";
				keyMainSupIdx = "";
				beanLot = lotSystemList.get(i);
				beanLot.setCreateBy(Config.C_SYSTEM);
				beanLot.setDataStatus(Config.C_OPEN_STATUS);
				userStatus = beanLot.getUserStatus();
				TempPOAddId = beanLot.getTempPOAddId();
				lotType = beanLot.getLotType();
				poId = beanLot.getPoId();
				ForecastId = beanLot.getForecastId();
				prodOrder = beanLot.getProductionOrder(); 
				isFirstLot = beanLot.getFirstLot();
				po = beanLot.getPo();
				beanLot.getDbProdQty();
				dyeAfterCFM = beanLot.getDyeAfterCFM();
				String greigeDateStr = beanLot.getGreigePlan();
				beanLot.getPlanSystemDate();
				planUserDateStr = beanLot.getPlanUserDate();
				planGreigeDate = beanLot.getPlanGreigeDate();
				reDyeId = beanLot.getRedyeId();
				firstLotDateStr = beanLot.getFirstLotDate();
				dyeAfterGreigeInBeginStr = beanLot.getDyeAfterGreigeInBegin();
				beanLot.getDyeAfterGreigeInLast();
				poIdInstead = beanLot.getPoIdInstead();
				statusAfterDateStr = beanLot.getStatusAfterDate();
				tempPlanningId = beanLot.getTempPlanningId();
				prodOrder = beanLot.getProductionOrder();
				oldGroupNo = beanLot.getGroupNo();
				oldSubGroup = beanLot.getSubGroup();
				oldPlanSystemDate = beanLot.getPlanSystemDate();
				oldDataStatus = beanLot.getDataStatus(); 
				if ( ! statusAfterDateStr.equals(Config.C_BLANK)) {
					statusAfterDate = this.sdf2.parse(statusAfterDateStr);
				} else {
					statusAfterDate = null;
				}
				if ( ! greigeDateStr.equals(Config.C_BLANK)) { 
					greigeDate = CompareDate.tryAfterDate(today, this.sdf2.parse(greigeDateStr)) ;
					
				} else {
					greigeDate = today;
				} 
				keyMainSup = beanLot.getKeyMainSup();
 
				if ( ! dyeAfterGreigeInBeginStr.equals(Config.C_BLANK)) { 
					dyeAfterGreigeInBeginDate = CompareDate.tryAfterDate(today, this.sdf2.parse(dyeAfterGreigeInBeginStr)) ;
				} else { dyeAfterGreigeInBeginDate = today; }
				groupOptionsList = beanLot.getGroupOptionsList();
				groupBegin = beanLot.getGroupBegin();
				haveFirstLot = beanLot.getHaveFirstLot();
				greigeInDate = beanLot.getGreigeInDate();
				productionOrderType = beanLot.getProductionOrderType();
				//08/05/2024 DELETE
//				if (productionOrderType.equals(">6M")) {
//					dyeAfterCFM = 0;
//				}
				boolean bl_isPastDate = true;

				// ------------------------------- FORECAST ---------------------
				if (ForecastId != 0) {
					if (beanLot.getForecastDateMonthBefore().equals(Config.C_BLANK)) {
						tmpDate = null;
					} else {
						tmpDate = this.sdf2.parse(beanLot.getForecastDateMonthBefore());
						tmpDateLast = this.sdf2.parse(beanLot.getForecastDateMonthLast());
						tmpDateStr = this.sdf2.format(tmpDate);
						cal = Calendar.getInstance();
						cal.setTime(tmpDate);
						bl_checkAvailable = false;
						counterBreaker = 0;
						while ( ! bl_checkAvailable) {
							idxDate = 0;
							cal.setTime(cal.getTime());
							tmpDate = cal.getTime();
							tmpDateStr = this.sdf2.format(tmpDate);
							for (String element : groupOptionsList) {
								tmpKeyMainSup = element;
								tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
								if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null) {

								} else {
									lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
									keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
									if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
										idxDate = mapWorkStrIdx.get(keyMainSupWorkDate); // GET ROWNUM
										if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
											countLotPerDay = 1;
											bl_checkAvailable = true; ;
											break;
										} else {
											countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET
																										// countLotPerDay
																										// , IF NULL = 0
											if (countLotPerDay < lotPerDay) {
												countLotPerDay = countLotPerDay+1;
												bl_checkAvailable = true;
												break;
											}
										}
									}
								}
							}
							cal.add(Calendar.DATE, 1);
							counterBreaker += 1;
							if (counterBreaker == 1500) {
								bl_checkAvailable = true;
								bl_isPlanAble = false;
							}
						} 
						if (tmpDate.after(tmpDateLast) || ! bl_isPlanAble) {
							tmpDate = null;
							tmpDateStr = null;
							groupNo = null;
							subGroup = null;
							countLotPerDay = 0;
						} else {
							tmpDate = this.sdf2.parse(tmpDateStr);
							String[] tmpArray = tmpKeyMainSup.split(Config.C_COLON);
							groupNo = tmpArray[0];
							subGroup = tmpArray[1];
							mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
						}
					} 
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr); 
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningLotList.add(beanLot);
						}
					}

				}
				// ------------------------------- REDYE ---------------------
				else if (reDyeId > 0) {
					cal = Calendar.getInstance();
					if (statusAfterDate == null) {
						tmpDate = greigeDate;
						bl_haveLotInSap = false;
					} else { 
						tmpDate  = CompareDate.tryAfterDate(statusAfterDate, greigeDate); 
					}
					cal.setTime(tmpDate);
					if (groupOptionsList.isEmpty()) {
						bl_isPlanAble = false;
					} else {
						counterBreaker = 0;
						bl_checkAvailable = false;
						while ( ! bl_checkAvailable) {
							idxDate = 0;
							cal.setTime(cal.getTime());
							tmpDate = cal.getTime();
							tmpDateStr = this.sdf2.format(tmpDate);
							for (String element : groupOptionsList) {
								tmpKeyMainSup = element;
								tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
								// check that date is work date ?
								if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null) { 
								} else {
									lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
									keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
									// check index of date from groupno:subgroup:date
									if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
										idxDate = mapWorkStrIdx.get(keyMainSupWorkDate); // GET ROWNUM
										// check lotperday from index of date
										if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
											// check userstatus 'รอเย็บประกบ' 
											if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
												// e ACCEPT 1 ONLY
												// check มี lot ที่ userstatus 'รอเย็บประกบ' แล้วหรือยัง from groupno:subgroup:date
												if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
													mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
													countLotPerDay = 1;
													bl_checkAvailable = true;
													break;
												} else {
												}
											} else {
												countLotPerDay = 1;
												bl_checkAvailable = true;
												break;
											}
										} 
										else {
											countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate); 
											if (countLotPerDay < lotPerDay) {
												if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
													// e ACCEPT 1 ONLY
													if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
														mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
														countLotPerDay = countLotPerDay+1;
														bl_checkAvailable = true;
														break;
													} else {
													}
												} else {
													countLotPerDay = countLotPerDay+1;
													bl_checkAvailable = true;
													break;
												}
											}
										}
									}
								}
							}
							cal.add(Calendar.DATE, 1);
							counterBreaker += 1;
							if (counterBreaker == 1500) {
								bl_checkAvailable = true;
								bl_isPlanAble = false;
							}
						}
					}

					String[] tmpArray = tmpKeyMainSup.split(Config.C_COLON);
					if (! bl_isPlanAble) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0;
					} else {
						groupNo = tmpArray[0];
						subGroup = tmpArray[1];
						mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
					}
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanIndex(countLotPerDay);
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningRedyeList.add(beanLot);
						}
					}
				} else if (poId > 0 && isFirstLot.equals(this.C_Y) && ! groupBegin.equals(Config.C_BLANK)) { 
 
					cal = Calendar.getInstance();
					if (statusAfterDate == null) {
						tmpDate = dyeAfterGreigeInBeginDate;
						bl_haveLotInSap = false;
					}  else {
						tmpDate  = CompareDate.tryAfterDate(statusAfterDate, dyeAfterGreigeInBeginDate); 
					}
					cal.setTime(tmpDate);
					counterBreaker = 0;
					bl_checkAvailable = false;
					while ( ! bl_checkAvailable) {
						cal.setTime(cal.getTime());
						tmpDate = cal.getTime();
						tmpDateStr = this.sdf2.format(tmpDate);
						tmpKeyMainSup = groupBegin;
						tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
						// check that date is work date ?
						if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null) { 

						} else {
							lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
							keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
							// check that date is work date ?
							if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
								if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
									if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
										// e ACCEPT 1 ONLY
										if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
											mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
											countLotPerDay = 1;
											bl_checkAvailable = true;
											break;
										} else {
										}
									} else {
										countLotPerDay = 1;
										bl_checkAvailable = true;
										break;
									}
								} else {
									countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET countLotPerDay ,
																								// IF NULL = 0
									if (countLotPerDay < lotPerDay) {
										if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
											// e ACCEPT 1 ONLY
											if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
												mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
												countLotPerDay = countLotPerDay+1;
												bl_checkAvailable = true;
												break;
											} else {
											}
										} else {
											countLotPerDay = countLotPerDay+1;
											bl_checkAvailable = true;
											break;
										}
									}
								}
							}
						}
						cal.add(Calendar.DATE, 1);
						counterBreaker += 1;
						if (counterBreaker == 1500) {
							bl_checkAvailable = true;
							bl_isPlanAble = false;
						}
					}
					if ( ! bl_isPlanAble) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0;
						beanLot.setDataStatus(this.C_P);
					} else {
						String[] tmpArray = keyMainSupWorkDate.split(Config.C_COLON);
						groupNo = tmpArray[0];
						subGroup = tmpArray[1];
						mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
						if ( ! poIdInstead.equals(Config.C_BLANK)) {
							String[] array = poIdInstead.split(","); 
							for (String element : array) {
								int poIdAR = Integer.parseInt(element);
								mapFirstLotPlanningDate.put(poIdAR, tmpDateStr + "|" + tmpKeyMainSup);
							}
						} else {
							mapFirstLotPlanningDate.put(poId, tmpDateStr + "|" + tmpKeyMainSup);
						}
					}
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanIndex(countLotPerDay);
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningLotList.add(beanLot);
						}
					}
				} 
				else if (poId > 0 && haveFirstLot == 0) { 
					cal = Calendar.getInstance();
					if (statusAfterDate == null) {
						tmpDate = dyeAfterGreigeInBeginDate;
						bl_haveLotInSap = false;
					} else { 
						tmpDate  = CompareDate.tryAfterDate(statusAfterDate, dyeAfterGreigeInBeginDate); 
					}
//					tmpDate = dyeAfterGreigeInBeginDate;
					cal.setTime(tmpDate);
					counterBreaker = 0;
					bl_checkAvailable = false;
					while ( ! bl_checkAvailable) {
						cal.setTime(cal.getTime());
						tmpDate = cal.getTime();
						tmpDateStr = sdf2.format(tmpDate);
						tmpKeyMainSup = groupBegin;
						tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
						if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null) {

						} else {
							lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
							keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
							// check that date is work date ?
							if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
								if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
									// countLotPerDay = 1;
									// bl_checkAvailable = true;
									if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
										// e ACCEPT 1 ONLY
										if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
											mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
											countLotPerDay = 1;
											bl_checkAvailable = true;
											break;
										} else {
										}
									} else {
										countLotPerDay = 1;
										bl_checkAvailable = true;
										break;
									}
								} else {
									countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET countLotPerDay ,
																								// IF NULL = 0
									if (countLotPerDay < lotPerDay) { 
										if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
											// e ACCEPT 1 ONLY
											if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
												mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
												countLotPerDay = countLotPerDay+1;
												bl_checkAvailable = true;
												break;
											} else {
											}
										} else {
											countLotPerDay = countLotPerDay+1;
											bl_checkAvailable = true;
											break;
										}
									}
								}
							}
						}
						cal.add(Calendar.DATE, 1);
						counterBreaker += 1;
						if (counterBreaker == 1500) {
							bl_checkAvailable = true;
							bl_isPlanAble = false;
						}
					}
					if ( ! bl_isPlanAble) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0;
						beanLot.setDataStatus(this.C_P);
					} else {
						String[] tmpArray = tmpKeyMainSup.split(Config.C_COLON);
						groupNo = tmpArray[0];
						subGroup = tmpArray[1];
						mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
					} 
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanIndex(countLotPerDay);
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningLotList.add(beanLot);
						}
					}
				}
				// PLANNING SAMETIME WITH FIRST LOT
				else if (poId > 0 && mapFirstLotPlanningDate.get(poId) != null) {  
					String valueWorkDateFLNMainSupGroup = mapFirstLotPlanningDate.get(poId);
					String[] array = valueWorkDateFLNMainSupGroup.split("\\|");
					tmpDateStr = array[0];   
					tmpFLDateStr = array[0];
					tmpKeyMainSup = groupBegin;  
					// STEP ONE NEW  08/05/2024  -- ADD START   
					// 1. NEW LOGIC GET FIRST LOT + LEAD TIME  ( 1'ST CFM + 2'ND DYE ) FIRST .
					// 2. COMPARE 3 VALUE 
					// 2.1 FIRST LOT 
					// 2.2 DYE AFTER GREIGE ( ALREADY + LEAD TIME [ 1'ST BC + 1'ST DYE ] ) 
					// 2.3 USER STATUS DATE 
					// 3. GET LASTEST DATE AND USE IT ...   
					// STEP 1 
					// --- FIRST LOT HANDLER ---
					int indexLotFLDate = 0;
					int indexLotFLAfterPlus = 0;
					String keyMainSupFirstLotDate = groupBegin+Config.C_COLON+tmpFLDateStr;
					String keyMainSupFirstLotIndex = "";
					String tempFirstLotDateWithLeadTime = "";
					Date firstLotWithLeadTimeDate = null;
					// SEARCH FIRST LOT
					if (mapWorkStrIdx.get(keyMainSupFirstLotDate) != null) {
						indexLotFLDate = mapWorkStrIdx.get(keyMainSupFirstLotDate); // GET ROWNUM
						indexLotFLAfterPlus = indexLotFLDate + 1; 
						indexLotFLAfterPlus = indexLotFLAfterPlus + dyeAfterCFM ;//ALWAY PLUS BECAUSE FIRST LOT;  
						keyMainSupFirstLotIndex = groupBegin+Config.C_COLON+Integer.toString(indexLotFLAfterPlus);
						tempFirstLotDateWithLeadTime = mapWorkIdxStr.get(keyMainSupFirstLotIndex);
					}  
					// ------------------------
					if (statusAfterDate == null) {  bl_haveLotInSap = false; }
						
					if ( ! tempFirstLotDateWithLeadTime.equals(Config.C_BLANK)) {
						firstLotWithLeadTimeDate = sdf2.parse(tmpDateStr);  
						tmpDate  = firstLotWithLeadTimeDate;
						tmpDate = CompareDate.tryAfterDate(tmpDate, dyeAfterGreigeInBeginDate) ;
						tmpDate = CompareDate.tryAfterDate(tmpDate, statusAfterDate);
					} 
					else { 
						tmpDate  = CompareDate.tryAfterDate(dyeAfterGreigeInBeginDate, statusAfterDate); 
					}   

					tmpDateStr = sdf2.format(tmpDate);   
					// 1 STEP ONE NEW  08/05/2024  -- ADD STOP     
					keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
					cal = Calendar.getInstance();
					cal.setTime(tmpDate);  
					counterBreaker = 0;
					bl_checkAvailable = false;
					while ( ! bl_checkAvailable) {
						cal.setTime(cal.getTime());
						tmpDate = cal.getTime();
						tmpDateStr = sdf2.format(tmpDate);
						keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
						// CHECK MC + WORK DATE IS AVAILABLE AFTER PLUS ? IF NOT WORK DATE PLUS ONE
						if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
							idxDate = mapWorkStrIdx.get(keyMainSupWorkDate); // GET ROWNUM
							bl_checkAvailable = true;
						}

						if (counterBreaker == 1500) {
							bl_checkAvailable = true; 
							bl_isPlanAble = false;
						}
						counterBreaker += 1;
						if ( ! bl_checkAvailable) {
							cal.add(Calendar.DATE, 1);
						}
					}  
					counterBreaker = 0; 
					tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
					if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null|| !bl_isPlanAble) {
						bl_isPlanAble = false;
					} else {
						keyMainSupIdx = tmpKeyMainSup+Config.C_COLON+Integer.toString(idxDate);
						tmpDateStr = mapWorkIdxStr.get(keyMainSupIdx);
						if(tmpDateStr == null) {
							bl_isPlanAble = false;
						}
						else {
							tmpDate = sdf2.parse(tmpDateStr);
							cal = Calendar.getInstance();
							cal.setTime(tmpDate);
							countLotPerDay = 0;
							bl_checkAvailable = false;
	
							cal = Calendar.getInstance();
							cal.setTime(tmpDate);
							counterBreaker = 0;
							bl_checkAvailable = false;
							while ( ! bl_checkAvailable) {
								cal.setTime(cal.getTime());
								tmpDate = cal.getTime();
								tmpDateStr = sdf2.format(tmpDate);
								tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
								// check that date is work date ?
								if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null) {
	
								} else {
									lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
									keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
									// check that date is work date ?
									if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
										if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
											if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
												// e ACCEPT 1 ONLY
												if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
													mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
													countLotPerDay = 1;
													bl_checkAvailable = true;
													break;
												} else {
												}
											} else {
												countLotPerDay = 1;
												bl_checkAvailable = true;
												break;
											}
										} else {
											countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET countLotPerDay
																										// , IF NULL = 0
											if (countLotPerDay < lotPerDay) {
												if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
													// e ACCEPT 1 ONLY
													if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
														mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
														countLotPerDay = countLotPerDay+1;
														bl_checkAvailable = true;
														break;
													} else {
													}
												} else {
													countLotPerDay = countLotPerDay+1;
													bl_checkAvailable = true;
													break;
												}
											}
										}
									}
								}
								cal.add(Calendar.DATE, 1);
								counterBreaker += 1;
								if (counterBreaker == 1500) {
									bl_checkAvailable = true;
									bl_isPlanAble = false;
								}
							}
						} 
					}
					if ( ! bl_isPlanAble) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0;
						beanLot.setDataStatus(this.C_P);
					} else {
						String[] tmpArray = keyMainSupWorkDate.split(Config.C_COLON);
						groupNo = tmpArray[0];
						subGroup = tmpArray[1];
						mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
					}
					beanLot.setDyeAfterDate(tmpFLDateStr);
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanIndex(countLotPerDay);
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningLotList.add(beanLot);
						}
					}
				} else if (TempPOAddId > 0 && haveFirstLot == 0) { 
					if ( ! planGreigeDate.equals(Config.C_BLANK)) {
						Date pgDate = sdf2.parse(planGreigeDate);
						if (today.after(pgDate)) {
							bl_isPastDate = true;	
						}
					}
					if (greigeInDate.equals(Config.C_BLANK) && bl_isPastDate) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0;
						bl_isPlanAble = false;
					} else { 
						cal = Calendar.getInstance(); 
						if (statusAfterDate == null) {
							tmpDate = greigeDate;
						}else {
							tmpDate  = CompareDate.tryAfterDate(statusAfterDate, greigeDate); 
						} 
						cal.setTime(tmpDate); 
						counterBreaker = 0;
						bl_checkAvailable = false;
						while ( ! bl_checkAvailable) {
							cal.setTime(cal.getTime());
							tmpDate = cal.getTime();
							tmpDateStr = sdf2.format(tmpDate);
							tmpKeyMainSup = groupBegin;
							tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
							// check that date is work date ?
							if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null) {
							} else {
								lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
								keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
								// check that date is work date ?
								if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
									if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
										if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
											// e ACCEPT 1 ONLY
											if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
												mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
												countLotPerDay = 1;
												bl_checkAvailable = true;
												break;
											} else {
											}
										} else {
											countLotPerDay = 1;
											bl_checkAvailable = true;
											break;
										}
									} else {
										countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET countLotPerDay
																									// , IF NULL = 0
										if (countLotPerDay < lotPerDay) {
											if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
												// e ACCEPT 1 ONLY
												if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
													mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
													countLotPerDay = countLotPerDay+1;
													bl_checkAvailable = true;
													break;
												} else {
												}
											} else {
												countLotPerDay = countLotPerDay+1;
												bl_checkAvailable = true;
												break;
											}
										}
									}
								}
							}
							cal.add(Calendar.DATE, 1);
							counterBreaker += 1;
							if (counterBreaker == 1500) {
								bl_checkAvailable = true;
								bl_isPlanAble = false;
							}
						}
						if ( ! bl_isPlanAble) {
							tmpDate = null;
							tmpDateStr = null;
							groupNo = null;
							subGroup = null;
							countLotPerDay = 0;
						} else {
							String[] tmpArray = tmpKeyMainSup.split(Config.C_COLON);
							groupNo = tmpArray[0];
							subGroup = tmpArray[1];
							mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
						}
					}
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanIndex(countLotPerDay);
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningPOAddedList.add(beanLot);
						}
					}
				} else if (TempPOAddId > 0 && isFirstLot.equals(this.C_Y) && ! groupBegin.equals(Config.C_BLANK)) {
					cal = Calendar.getInstance();
					if ( ! planGreigeDate.equals(Config.C_BLANK)) {
						Date pgDate = sdf2.parse(planGreigeDate);
						if (today.after(pgDate)) {
							bl_isPastDate = true;
						}
					}
					if (greigeInDate.equals(Config.C_BLANK) && bl_isPastDate) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0;
						bl_isPlanAble = false;
					} else {

						if (statusAfterDate == null) {
							tmpDate = greigeDate;
						} else { 
							tmpDate  = CompareDate.tryAfterDate(greigeDate, statusAfterDate);  
						} 
						cal.setTime(tmpDate);
						bl_checkAvailable = false;
						counterBreaker = 0;
						while ( ! bl_checkAvailable) {
							cal.setTime(cal.getTime());
							tmpDate = cal.getTime();
							tmpDateStr = sdf2.format(tmpDate);
							tmpKeyMainSup = groupBegin;
							tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
							if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null) {

							} else {
								lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
								keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
								// check that date is work date ?
								if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
									if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
										if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
											// e ACCEPT 1 ONLY
											if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
												mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
												countLotPerDay = 1;
												bl_checkAvailable = true;
												break;
											} else {
											}
										} else {
											countLotPerDay = 1;
											bl_checkAvailable = true;
											break;
										}
									} else {
										countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET countLotPerDay
																									// , IF NULL = 0
										if (countLotPerDay < lotPerDay) {
											// countLotPerDay = countLotPerDay + 1;
											// bl_checkAvailable = true;
											if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
												// e ACCEPT 1 ONLY
												if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
													mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
													countLotPerDay = countLotPerDay+1;
													bl_checkAvailable = true;
													break;
												} else {
												}
											} else {
												countLotPerDay = countLotPerDay+1;
												bl_checkAvailable = true;
												break;
											}
										}
									}
								}
							}
							cal.add(Calendar.DATE, 1);
							counterBreaker += 1;
							if (counterBreaker == 1500) {
								bl_checkAvailable = true;
								bl_isPlanAble = false;
							}
						}
						if ( ! bl_isPlanAble) {
							tmpDate = null;
							tmpDateStr = null;
							groupNo = null;
							subGroup = null;
							countLotPerDay = 0;
						} else {
							String[] tmpArray = keyMainSupWorkDate.split(Config.C_COLON);
							groupNo = tmpArray[0];
							subGroup = tmpArray[1];
							mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
							mapPOAddFirstLotPlanningDate.put(po, tmpDateStr + "|" + tmpKeyMainSup);
						}
					}
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanIndex(countLotPerDay);
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningPOAddedList.add(beanLot);
						}
					}
				}
				// PLANNING SAMETIME WITH FIRST LOT
				else if (TempPOAddId > 0 && mapPOAddFirstLotPlanningDate.get(po) != null) { 
					if ( ! planGreigeDate.equals(Config.C_BLANK)) {
						Date pgDate = sdf2.parse(planGreigeDate);
						if (today.after(pgDate)) {
							bl_isPastDate = true;
						}
					}
					if (greigeInDate.equals(Config.C_BLANK) && bl_isPastDate) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0; 
					} else {
						String valueWorkDateFLNMainSupGroup = mapPOAddFirstLotPlanningDate.get(po);
						String[] array = valueWorkDateFLNMainSupGroup.split("\\|");
						tmpDateStr = array[0]; 
						if (statusAfterDate == null) {
							tmpDate = this.sdf2.parse(tmpDateStr);
							bl_haveLotInSap = false;
						}else { 
							tmpDate  = CompareDate.tryAfterDate(greigeDate, statusAfterDate);   
						} 
						tmpFLDateStr = array[0];
						tmpKeyMainSup = groupBegin;
						keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
						cal = Calendar.getInstance();
						cal.setTime(tmpDate);
						if (mapWorkStrIdx.get(keyMainSupWorkDate) == null) {
							bl_checkAvailable = false;
							counterBreaker = 0;
							while ( ! bl_checkAvailable) {
								cal.setTime(cal.getTime());
								tmpDate = cal.getTime();
								tmpDateStr = sdf2.format(tmpDate);
								keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
								if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
									idxDate = mapWorkStrIdx.get(keyMainSupWorkDate); // GET ROWNUM
									bl_checkAvailable = true;
								}
								counterBreaker += 1;

								if (counterBreaker == 1500) {
									bl_checkAvailable = true;
								}
								if ( ! bl_checkAvailable) {
									cal.add(Calendar.DATE, 1);
								}
							}
						} else {
							idxDate = mapWorkStrIdx.get(keyMainSupWorkDate); // GET ROWNUM
						}

						int indexPlus = idxDate+1;
						if ( ! bl_haveLotInSap) {
							indexPlus += dyeAfterCFM;
						}

						keyMainSupIdx = tmpKeyMainSup+Config.C_COLON+Integer.toString(indexPlus);
						tmpDateStr = mapWorkIdxStr.get(keyMainSupIdx);
						if(tmpDateStr == null) {
							bl_isPlanAble = false;
						}else {
							tmpDate = sdf2.parse(tmpDateStr);
							tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
						}
						if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null|| !bl_isPlanAble) {
							bl_isPlanAble = false;
						} else {
							lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
							countLotPerDay = 0;
							cal = Calendar.getInstance();
							cal.setTime(tmpDate);
							bl_checkAvailable = false;
							counterBreaker = 0;
							while ( ! bl_checkAvailable) {
								cal.setTime(cal.getTime());
								tmpDate = cal.getTime();
								tmpDateStr = sdf2.format(tmpDate);
								tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
								// check that date is work date ?
								if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null) {

								} else {
									lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
									keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
									// check that date is work date ?
									if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
										if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
											if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
												// e ACCEPT 1 ONLY
												if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
													mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
													countLotPerDay = 1;
													bl_checkAvailable = true;
													break;
												} else {
												}
											} else {
												countLotPerDay = 1;
												bl_checkAvailable = true;
												break;
											}
										} else {
											countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET
																										// countLotPerDay
																										// , IF NULL = 0
											if (countLotPerDay < lotPerDay) {
												// countLotPerDay = countLotPerDay + 1;
												// bl_checkAvailable = true;
												if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
													// e ACCEPT 1 ONLY
													if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
														mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
														countLotPerDay = countLotPerDay+1;
														bl_checkAvailable = true;
														break;
													} else {
													}
												} else {
													countLotPerDay = countLotPerDay+1;
													bl_checkAvailable = true;
													break;
												}
											}
										}
									}
								}
							}
							cal.add(Calendar.DATE, 1);
							counterBreaker += 1;
							if (counterBreaker == 1500) {
								bl_checkAvailable = true;
								bl_isPlanAble = false;
							}
						}
						if ( ! bl_isPlanAble) {
							tmpDate = null;
							tmpDateStr = null;
							groupNo = null;
							subGroup = null;
							countLotPerDay = 0;
						} else {
							String[] tmpArray = keyMainSupWorkDate.split(Config.C_COLON);
							groupNo = tmpArray[0];
							subGroup = tmpArray[1];
							mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
						}
					}
					beanLot.setDyeAfterDate(tmpFLDateStr);
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanIndex(countLotPerDay);
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningPOAddedList.add(beanLot);
						}
					}
				} else if (TempPOAddId != 0) {
					idxDate = 0;
					if ( ! planGreigeDate.equals(Config.C_BLANK)) {
						Date pgDate = sdf2.parse(planGreigeDate);
						if (today.after(pgDate)) {
							bl_isPastDate = true;
						}
					} 
					if (greigeInDate.equals(Config.C_BLANK) && bl_isPastDate) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0;
					} else {
						if (firstLotDateStr.equals(Config.C_BLANK)) {
							firstLotDateStr = sdf2.format(today);
						}
						tmpFLDateStr = firstLotDateStr; 
						tmpDateStr = firstLotDateStr;  
						tmpKeyMainSup = groupBegin;
						// STEP ONE NEW  08/05/2024  -- ADD START   
						// 1. NEW LOGIC GET FIRST LOT + LEAD TIME  ( 1'ST CFM + 2'ND DYE ) FIRST .
						// 2. COMPARE 3 VALUE 
						// 2.1 FIRST LOT 
						// 2.2 DYE AFTER GREIGE ( ALREADY + LEAD TIME [ 1'ST BC + 1'ST DYE ] ) 
						// 2.3 USER STATUS DATE 
						// 3. GET LASTEST DATE AND USE IT ...   
//						// STEP ONE NEW  08/05/2024  -- ADD STOP   
						if (statusAfterDate == null) {
							tmpDate = sdf2.parse(tmpDateStr);
							bl_haveLotInSap = false;
						} else {
							tmpDate  = CompareDate.tryAfterDate(greigeDate, statusAfterDate);  
						}
						tmpDateStr = this.sdf2.format(tmpDate); 
						keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;  
						cal = Calendar.getInstance();
						cal.setTime(tmpDate);  
						bl_checkAvailable = false;
						counterBreaker = 0;
						while ( ! bl_checkAvailable) {
							cal.setTime(cal.getTime());
							tmpDate = cal.getTime();
							tmpDateStr = sdf2.format(tmpDate);
							keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
							if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
								idxDate = mapWorkStrIdx.get(keyMainSupWorkDate); // GET ROWNUM
								bl_checkAvailable = true;
							}
							if (counterBreaker == 1500) {
								bl_checkAvailable = true; 
							}
							counterBreaker += 1;
							if ( ! bl_checkAvailable) {
								cal.add(Calendar.DATE, 1);
							}
						}  
						keyMainSupIdx = tmpKeyMainSup+Config.C_COLON+Integer.toString(idxDate);
						tmpDateStr = mapWorkIdxStr.get(keyMainSupIdx);
						if(tmpDateStr == null) {
							bl_isPlanAble = false;
						}else {
							tmpDate = sdf2.parse(tmpDateStr);
							tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
							cal = Calendar.getInstance();
							cal.setTime(tmpDate);
						}
						if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null||!bl_isPlanAble) {
							bl_isPlanAble = false;
						} else {
							lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
							countLotPerDay = 0;
							bl_checkAvailable = false;
							counterBreaker = 0;
							while ( ! bl_checkAvailable) {
								cal.setTime(cal.getTime());
								tmpDate = cal.getTime();
								tmpDateStr = sdf2.format(tmpDate);
								keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
								if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
									if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) { 
										if (userStatus.equals(this.C_US_RORYEPPRAKOB)) { 
											if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
												mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
												countLotPerDay = 1;
												bl_checkAvailable = true;
												break;
											} else {
											}
										} else {
											countLotPerDay = 1;
											bl_checkAvailable = true;
											break;
										}
									} else {
										countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET countLotPerDay 
										if (countLotPerDay < lotPerDay) {
											if (userStatus.equals(this.C_US_RORYEPPRAKOB)) { 
												if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
													mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
													countLotPerDay = countLotPerDay+1;
													bl_checkAvailable = true;
													break;
												}
											} else {
												countLotPerDay = countLotPerDay+1;
												bl_checkAvailable = true;
												break;
											}
										}
									}
								}
							}
							if (counterBreaker == 1500) {
								bl_checkAvailable = true;
								bl_isPlanAble = false;
							}
							counterBreaker += 1;
							if ( ! bl_checkAvailable) {
								cal.add(Calendar.DATE, 1);
							}
						}
						if ( ! bl_isPlanAble) {
							tmpDate = null;
							tmpDateStr = null;
							groupNo = null;
							subGroup = null;
							countLotPerDay = 0; 
							beanLot.setPlanSystemDate(tmpDateStr);
							beanLot.setPlanUserDate(tmpDateStr);
							beanLot.setPlanUserChangeDate(todayStr);
							beanLot.setPlanBy(this.C_CANCELED);
							beanLot.setDataStatus(this.C_P);
							beanLot.setPlanIndex(countLotPerDay);
						} else {
							String[] tmpArray = tmpKeyMainSup.split(Config.C_COLON);
							groupNo = tmpArray[0];
							subGroup = tmpArray[1];
							mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
						}
					}
					beanLot.setDyeAfterDate(tmpFLDateStr);
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanIndex(countLotPerDay);
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningPOAddedList.add(beanLot);
						}
					}
				}
				// PLANNING AFTER FIRST LOT SUCCESS => 2ND LOT
				else { 
					idxDate = 0;
					if (firstLotDateStr.equals(Config.C_BLANK)) {
						firstLotDateStr = sdf2.format(today);
					}
					tmpFLDateStr = firstLotDateStr;
					tmpDateStr = firstLotDateStr;	
					tmpKeyMainSup = groupBegin;
					if (statusAfterDate == null) {
						tmpDate = sdf2.parse(tmpDateStr);
						bl_haveLotInSap = false; 
					} else {
						tmpDate  = CompareDate.tryAfterDate(greigeDate, statusAfterDate);  
					} 
					tmpDateStr = this.sdf2.format(tmpDate);
					tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
					// FIRST LOT
					cal = Calendar.getInstance();
					cal.setTime(tmpDate);
					if (mapWorkStrIdx.get(tmpKeyMainSupWorkDate) == null) {
						counterBreaker = 0;
						bl_checkAvailable = false;
						while ( ! bl_checkAvailable) {
							cal.setTime(cal.getTime());
							tmpDate = cal.getTime();
							tmpDateStr = this.sdf2.format(tmpDate);
							tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
							if (mapWorkStrIdx.get(tmpKeyMainSupWorkDate) != null) {
								idxDate = mapWorkStrIdx.get(tmpKeyMainSupWorkDate); // GET ROWNUM
								bl_checkAvailable = true;
							}
							if (counterBreaker == 1500) {
								bl_checkAvailable = true; 
								bl_isPlanAble = false;
							}
							counterBreaker += 1;
							if ( ! bl_checkAvailable) {
								cal.add(Calendar.DATE, 1);
							}
						}
					} else {
						idxDate = mapWorkStrIdx.get(tmpKeyMainSupWorkDate); // GET ROWNUM
					}
					int indexPlus = idxDate+1;
					if ( ! bl_haveLotInSap) {
						indexPlus += dyeAfterCFM;
					}  
					keyMainSupIdx = tmpKeyMainSup+Config.C_COLON+Integer.toString(indexPlus); 
					tmpDateStr = mapWorkIdxStr.get(keyMainSupIdx);
					if(tmpDateStr == null) {
						bl_isPlanAble = false;
					}else {
						tmpDate = this.sdf2.parse(tmpDateStr);
						cal = Calendar.getInstance();
						cal.setTime(tmpDate);
						tmpKeyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr; 
					}
					// check that date is work date ?
					if (mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate) == null||!bl_isPlanAble ) {
						bl_isPlanAble = false;
					} else {
						lotPerDay = mapKMSWDLotPerDay.get(tmpKeyMainSupWorkDate);
						countLotPerDay = 0;
						bl_checkAvailable = false;
						counterBreaker = 0;
						while ( ! bl_checkAvailable) {
							cal.setTime(cal.getTime());
							tmpDate = cal.getTime();
							tmpDateStr = sdf2.format(tmpDate);
							keyMainSupWorkDate = tmpKeyMainSup+Config.C_COLON+tmpDateStr;
							if (mapWorkStrIdx.get(keyMainSupWorkDate) != null) {
								if (mapCountLotPerDay.get(keyMainSupWorkDate) == null) {
									if (userStatus.equals(this.C_US_RORYEPPRAKOB)) {
										// e ACCEPT 1 ONLY
										if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
											mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
											countLotPerDay = 1;
											bl_checkAvailable = true;
											break;
										}
									} else {
										countLotPerDay = 1;
										bl_checkAvailable = true;
										break;
									}
								} else {
									countLotPerDay = mapCountLotPerDay.get(keyMainSupWorkDate);// GET countLotPerDay ,
																								// IF NULL = 0
									if (countLotPerDay < lotPerDay) { 
										if (userStatus.equals(this.C_US_RORYEPPRAKOB)) { 
											if (mapYepPraKob.get(keyMainSupWorkDate) == null) {
												mapYepPraKob.put(keyMainSupWorkDate, this.C_Y);
												countLotPerDay = countLotPerDay+1;
												bl_checkAvailable = true;
												break;
											}
										} else {
											countLotPerDay = countLotPerDay+1;
											bl_checkAvailable = true;
											break;
										}
									}
								}
							}
							if (counterBreaker == 1500) {
								bl_checkAvailable = true;
								bl_isPlanAble = false;
							}
							counterBreaker += 1;
							if ( ! bl_checkAvailable) {
								cal.add(Calendar.DATE, 1);
							}
						}
					}
					if ( ! bl_isPlanAble) {
						tmpDate = null;
						tmpDateStr = null;
						groupNo = null;
						subGroup = null;
						countLotPerDay = 0;
  
					} else {
						String[] tmpArray = tmpKeyMainSup.split(Config.C_COLON);
						groupNo = tmpArray[0];
						subGroup = tmpArray[1];
						mapCountLotPerDay.put(keyMainSupWorkDate, countLotPerDay);
					} 
					beanLot.setDyeAfterDate(tmpFLDateStr);
					beanLot.setGroupNo(groupNo);
					beanLot.setSubGroup(subGroup);
					beanLot.setPlanSystemDate(tmpDateStr);
					beanLot.setPlanIndex(countLotPerDay); 
					if (this.checkDifOldValueWithNewValue(oldGroupNo, oldSubGroup, oldPlanSystemDate, beanLot)) {
						if (tempPlanningId != 0) {
							planningTempPlanningIdList.add(beanLot);
						} else {
							planningLotList.add(beanLot);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (bl_isUpdate) { 
			tplModel.upsertTEMPPlanningLotWithTempPlanningId(planningTempPlanningIdList);
			// ไม่ควรเข้า if นี้อีกแล้ว เพราะทำดักให้เข้า temp planninglot ไปตั้งแต่แรกสร้าง
			// lot 
			if ( ! planningLotList.isEmpty()) {
				tplModel.upsertTEMPPlanningLotWithTempProdId(planningLotList, Config.C_OPEN_STATUS);
			} 
			if ( ! planningRedyeList.isEmpty()) {
				tplModel.upsertTEMPPlanningLotWithRedye(planningRedyeList);
			} 
			if ( ! planningPOAddedList.isEmpty()) {
				tplModel.upsertTEMPPlanningLotWithPOAdded(planningPOAddedList);
			} 
			bgjModel.execUpsertLeadTime(); 
		}
	}

	@Override
	public ArrayList<InputTempProdDetail> saveProdFirstLotForInputTempProdDetail(ArrayList<InputTempProdDetail> poList)
	{
		String systemStatus = "";
		String iconStatus = Config.C_SUC_ICON_STATUS;
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		ArrayList<InputTempProdDetail> list = tplModel.getTempProdDetailById(poList);
		InputTempProdDetail bean = poList.get(0);
		String prodOrderFL = bean.getProductionOrderFirstLot();
		int tempPlanningId = bean.getTempPlanningId();
		if (list.size() > 0) {
			InputTempProdDetail beanTemp = list.get(0);
			int baseTempPlanningId = beanTemp.getTempPlanningId();
			if (tempPlanningId == 0) {
				tempPlanningId = baseTempPlanningId;
			}
			bean.setTempPlanningId(tempPlanningId);
			iconStatus = tplModel.updateProdFirstLotForInputTempProdDetail(poList);
		} else {
			iconStatus = tplModel.insertProdFirstLotForInputTempProdDetail(poList, Config.C_OPEN_STATUS);
		}
		if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) {
		} else {
			systemStatus = Config.C_ERROR_TEXT;
		}

		if ( ! prodOrderFL.equals("")) {
			list = this.getProductionOrderDetailByProductionOrderFL(prodOrderFL);
			if (list.size() == 0) {

			} else {
				InputTempProdDetail beanTemp = list.get(0);
				String operationEndDate = beanTemp.getOperationEndDate();
				if (operationEndDate.equals("")) {
				} else {
					iconStatus = "W";
					systemStatus = "Prod.Order(FL) ได้มีการคอนเฟิร์มเวลาจบแล้ว ( "
							+ operationEndDate
							+ " ) \n ต้องการให้วางแผนบวก lead time รอผลย้อม lot แรกไหม";
				}
			}
		}
		InputTempProdDetail bean1 = new InputTempProdDetail();
		bean1.setIconStatus(iconStatus);
		bean1.setSystemStatus(systemStatus);
		poList.clear();
		poList.add(bean1);
		return poList;
	}

	@Override
	public ArrayList<InputTempProdDetail> saveRemarkForInputTempProdDetail(ArrayList<InputTempProdDetail> poList)
	{
		// TODO Auto-generated method stub
		String systemStatus = "";
		String iconStatus = Config.C_SUC_ICON_STATUS;
		InputPlanningRemarkModel iprModel = new InputPlanningRemarkModel(this.conType);
		iconStatus = iprModel.upsertInputPlanningRemarkWithPlanningRemark(poList);
		if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) {
		} else {
			systemStatus = Config.C_ERROR_TEXT;
		}
		InputTempProdDetail bean1 = new InputTempProdDetail();
		bean1.setIconStatus(iconStatus);
		bean1.setSystemStatus(systemStatus);
		poList.clear();
		poList.add(bean1);
		return poList;
	}

	private InputPlanningLotDetail setPlanSystemDateToDays(ArrayList<InputPlanningLotDetail> tmpGroupList, int index,
			InputTempProdDetail beanTmpPrd, int countIdx)
	{
		tmpGroupList.get(countIdx).setDayFromDaySettersByIndex(index, beanTmpPrd);
//		tmpGroupList.get(countIdx).setDayFromIndex(index, beanTmpPrd);
//		if (index == 0) {
//			tmpGroupList.get(countIdx).setD1(beanTmpPrd);
//		} else if (index == 1) {
//			tmpGroupList.get(countIdx).setD2(beanTmpPrd);
//		} else if (index == 2) {
//			tmpGroupList.get(countIdx).setD3(beanTmpPrd);
//		} else if (index == 3) {
//			tmpGroupList.get(countIdx).setD4(beanTmpPrd);
//		} else if (index == 4) {
//			tmpGroupList.get(countIdx).setD5(beanTmpPrd);
//		} else if (index == 5) {
//			tmpGroupList.get(countIdx).setD6(beanTmpPrd);
//		} else if (index == 6) {
//			tmpGroupList.get(countIdx).setD7(beanTmpPrd);
//		} else if (index == 7) {
//			tmpGroupList.get(countIdx).setD8(beanTmpPrd);
//		} else if (index == 8) {
//			tmpGroupList.get(countIdx).setD9(beanTmpPrd);
//		} else if (index == 9) {
//			tmpGroupList.get(countIdx).setD10(beanTmpPrd);
//		} else if (index == 10) {
//			tmpGroupList.get(countIdx).setD11(beanTmpPrd);
//		} else if (index == 11) {
//			tmpGroupList.get(countIdx).setD12(beanTmpPrd);
//		} else if (index == 12) {
//			tmpGroupList.get(countIdx).setD13(beanTmpPrd);
//		} else if (index == 13) {
//			tmpGroupList.get(countIdx).setD14(beanTmpPrd);
//		} else if (index == 14) {
//			tmpGroupList.get(countIdx).setD15(beanTmpPrd);
//		} else if (index == 15) {
//			tmpGroupList.get(countIdx).setD16(beanTmpPrd);
//		} else if (index == 16) {
//			tmpGroupList.get(countIdx).setD17(beanTmpPrd);
//		} else if (index == 17) {
//			tmpGroupList.get(countIdx).setD18(beanTmpPrd);
//		} else if (index == 18) {
//			tmpGroupList.get(countIdx).setD19(beanTmpPrd);
//		} else if (index == 19) {
//			tmpGroupList.get(countIdx).setD20(beanTmpPrd);
//		} else if (index == 20) {
//			tmpGroupList.get(countIdx).setD21(beanTmpPrd);
//		} else if (index == 21) {
//			tmpGroupList.get(countIdx).setD22(beanTmpPrd);
//		} else if (index == 22) {
//			tmpGroupList.get(countIdx).setD23(beanTmpPrd);
//		} else if (index == 23) {
//			tmpGroupList.get(countIdx).setD24(beanTmpPrd);
//		} else if (index == 24) {
//			tmpGroupList.get(countIdx).setD25(beanTmpPrd);
//		} else if (index == 25) {
//			tmpGroupList.get(countIdx).setD26(beanTmpPrd);
//		} else if (index == 26) {
//			tmpGroupList.get(countIdx).setD27(beanTmpPrd);
//		} else if (index == 27) {
//			tmpGroupList.get(countIdx).setD28(beanTmpPrd);
//		} else if (index == 28) {
//			tmpGroupList.get(countIdx).setD29(beanTmpPrd);
//		} else if (index == 29) {
//			tmpGroupList.get(countIdx).setD30(beanTmpPrd);
//		} else if (index == 30) {
//			tmpGroupList.get(countIdx).setD31(beanTmpPrd);
//		}
		return tmpGroupList.get(countIdx);
	}

	@Override
	public ArrayList<SetLotWorkDateDetail> setWorkDateForLot(ArrayList<SetLotWorkDateDetail> poList)
	{
		String systemStatus = "";
		String iconStatus = Config.C_SUC_ICON_STATUS;
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		SetLotWorkDateDetail beanTemp = poList.get(0);
		InputTempProdDetail beanProd = beanTemp.getBeanProd();
		String lotType = beanProd.getLotType();
		String curGroupNo = beanProd.getGroupNo();

		String groupType = beanTemp.getNewGroupNo();
		if (groupType.equals("TempLot")) {
			if (lotType.equals(this.C_REDYE) || lotType.equals(this.C_SCOURING) || lotType.equals(this.C_WAITRESULT)) {
				// REDYE SAME TEMPLOT
				if (curGroupNo.equals("")) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = "this lot already in Temp Lot.";
				}
				// REDYE MOVEOUT
				else {
					tplModel.upsertTempPlanningLotWithRedyeId(poList);
					this.rePlanningLot();
				}
			} else if (lotType.equals(this.C_POADD) || lotType.equals(this.C_POADDTMP)) {
				// REDYE SAME TEMPLOT
				if (curGroupNo.equals("")) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = "this lot already in Temp Lot.";
				}
				// REDYE MOVEOUT
				else {
					tplModel.upsertTEMPPlanningLotWithPOAddId(poList);
					this.rePlanningLot();
				}
			}
			// po
			else {
				// TEMP => TEMPLOT
				if (curGroupNo.equals("")) {
					iconStatus = Config.C_ERR_ICON_STATUS;
					systemStatus = "this lot already in Temp Lot.";
				}
				// GROUP TO TEMP
				else {
					tplModel.updateTempPlanningLotWithPOIdToTemp(poList, Config.C_OPEN_STATUS, this.C_P);
					this.rePlanningLot();
				}
			}
		} else {
			String groupNo = beanTemp.getNewGroupNo();
			String subGroup = beanTemp.getNewSubGroup();
			String workDate = beanTemp.getNewWorkDate();
			InputLotDragDropDetail beanLotDrop = new InputLotDragDropDetail();
			beanLotDrop.setGroupNoDrop(groupNo);
			beanLotDrop.setSubGroupDrop(subGroup);
			beanLotDrop.setPlanDateDrop(workDate);
			ArrayList<InputGroupDetail> list = this.getCountPlanUserDate(beanLotDrop); 
			if ( ! list.isEmpty()) {
				InputGroupDetail beanTempCount = list.get(0);
				int lotPerDay = Integer.parseInt(beanTempCount.getLotPerDay());
				int countWorkDate = beanTempCount.getCountWorkDate();
				if (countWorkDate >= lotPerDay) {
					iconStatus = "E0";
					systemStatus = "Enter the number of operations that have reached the predetermined point.";
				} // AVAILABLE
				else {
					// NORMAL REDYE
					if (lotType.equals(this.C_REDYE) || lotType.equals(this.C_SCOURING) || lotType.equals(this.C_WAITRESULT)) {
						tplModel.upsertTempPlanningLotWithRedyeId(poList);
					} else if (lotType.equals(this.C_POADD) || lotType.equals(this.C_POADDTMP)) {
						tplModel.upsertTEMPPlanningLotWithPOAddId(poList);
					} else {
						// PO drag from TEMP LOT => GROUP
						if (curGroupNo.equals(Config.C_BLANK)) {
							tplModel.updateTempPlanningLotWithTempProdId(poList, Config.C_OPEN_STATUS);
							tplModel.updateTempPlanningLotWithPOIdToTemp(poList, this.C_P, Config.C_OPEN_STATUS);
						} else {
							tplModel.updateTempPlanningLotWithTempProdId(poList, Config.C_OPEN_STATUS);
						}
					}
					this.rePlanningLot();
				}
			} else {
				if (lotType.equals(this.C_REDYE) || lotType.equals(this.C_SCOURING) || lotType.equals(this.C_WAITRESULT)) {
					tplModel.upsertTempPlanningLotWithRedyeId(poList);
				} else if (lotType.equals(this.C_POADD) || lotType.equals(this.C_POADDTMP)) {
					tplModel.upsertTEMPPlanningLotWithPOAddId(poList);
				} else { 
					// PO drag from TEMP LOT => GROUP
					if (curGroupNo.equals(Config.C_BLANK)) {
						tplModel.updateTempPlanningLotWithTempProdId(poList, Config.C_OPEN_STATUS);
						tplModel.updateTempPlanningLotWithPOIdToTemp(poList, this.C_P, Config.C_OPEN_STATUS);
					} else {
						tplModel.updateTempPlanningLotWithTempProdId(poList, Config.C_OPEN_STATUS);
					}
				}
				this.rePlanningLot();
			}

			// MORE THAN LOTPERDAY

		}
		SetLotWorkDateDetail beanResult = new SetLotWorkDateDetail();
		beanResult.setSystemStatus(systemStatus);
		beanResult.setIconStatus(iconStatus);
		poList.clear();
		poList.add(beanResult);
		return poList;
	}

	@Override
	public ArrayList<InputPlanningLotDetail> updateUserPlanDateSingleToSingle(ArrayList<InputLotDragDropDetail> poList)
	{
		// TODO Auto-generated method stub
		InputLotDragDropDetail bean = poList.get(0);
		ArrayList<InputPlanningLotDetail> listIP = new ArrayList<>();
		InputPlanningLotDetail beanIP = new InputPlanningLotDetail();
		String systemStatus = "";
		String iconStatus = "";
		ArrayList<InputGroupDetail> list = this.getCountPlanUserDate(bean);
		String countPlanUserDatStr = "0";
		int countPlanUserDat = 0;
		if ( ! list.isEmpty()) {
			countPlanUserDatStr = list.get(0).getLotPerDay();
			countPlanUserDat = Integer.parseInt(countPlanUserDatStr);
			// MORE THAN LOTPERDAY
			if (countPlanUserDat <= 0) {
				iconStatus = "E0";
				systemStatus = "Enter the number of operations that have reached the predetermined point.";
			} // AVAILABLE
			else {
				int counter = this.upsertTMPPlanningLotDetailByUserPlan(poList);
				if (counter > 0) {
					iconStatus = "I0";
					systemStatus = "Success.";
					this.rePlanningLot();
				} else {
					iconStatus = "E99";
					systemStatus = Config.C_ERROR_TEXT;
				}
			}
		} else {
			int counter = this.upsertTMPPlanningLotDetailByUserPlan(poList);
			if (counter > 0) {
				iconStatus = "I0";
				systemStatus = "Success.";
				this.rePlanningLot();
			} else {
				iconStatus = "E99";
				systemStatus = Config.C_ERROR_TEXT;
			}
		}
		beanIP.setSystemStatus(systemStatus);
		beanIP.setIconStatus(iconStatus);
		listIP.add(beanIP);
		return listIP;
	}

	public int upsertTMPPlanningLotDetailByUserPlan(ArrayList<InputLotDragDropDetail> poList)
	{
		int counterAll = 0;

		InputLotDragDropDetail bean = poList.get(0);
		InputTempProdDetail dragLotBean = bean.getDragLot();
		bean.getDropLot();
		bean.getGroupNoDrop();

		String groupNoDrop = bean.getGroupNoDrop();
		String subGroupDrop = bean.getSubGroupDrop();
		String planDateDrop = bean.getPlanDateDrop();

		String groupNoDrag = bean.getGroupNoDrag();
		String changeBy = bean.getChangeBy();

		int poIdDrag = dragLotBean.getPoId();
		String operationDrag = dragLotBean.getOperation();
		String noDrag = dragLotBean.getNo();
		int reDyeDrag = dragLotBean.getRedyeId();
		int poAddId = dragLotBean.getTempPOAddId();
		int tempProdId = dragLotBean.getTempProdId();
		String lotTypeDrag = dragLotBean.getLotType();
		String prodOrder = dragLotBean.getProductionOrder();
		SetLotWorkDateDetail beanTemp = new SetLotWorkDateDetail();
		InputTempProdDetail beanProd = beanTemp.getBeanProd();
		beanProd.setPoId(poIdDrag);
		beanProd.setRedyeId(reDyeDrag);
		beanProd.setTempProdId(tempProdId);
		beanProd.setNo(noDrag);
		beanProd.setProductionOrder(prodOrder);
		beanProd.setOperation(operationDrag);
		beanProd.setTempPOAddId(poAddId);
		beanTemp.setBeanProd(beanProd);
		beanTemp.setNewGroupNo(groupNoDrop);
		beanTemp.setNewSubGroup(subGroupDrop);
		beanTemp.setNewWorkDate(planDateDrop);
		beanTemp.setChangeBy(changeBy);
		ArrayList<SetLotWorkDateDetail> setLotList = new ArrayList<>();
		setLotList.add(beanTemp);
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		if (lotTypeDrag.equals(this.C_REDYE) || lotTypeDrag.equals(this.C_SCOURING) || lotTypeDrag.equals(this.C_WAITRESULT)) {
			counterAll += tplModel.upsertTempPlanningLotWithRedyeId(setLotList);
		} else if (lotTypeDrag.equals(this.C_POADD) || lotTypeDrag.equals(this.C_POADDTMP)) {
			counterAll += tplModel.upsertTEMPPlanningLotWithPOAddId(setLotList);
		}
		// TEMP TO GROUP [ ALREADY BLOCK TEMP TO TEMP
		else if (groupNoDrag.equals(this.C_POTMP)) {
			counterAll += tplModel.updateTempPlanningLotWithTempProdId(setLotList, Config.C_OPEN_STATUS);
			counterAll += tplModel.updateTempPlanningLotWithPOIdToTemp(setLotList, this.C_P, Config.C_OPEN_STATUS);
		} else if (groupNoDrop.equals(this.C_POTMP)) {
			counterAll += tplModel.updateTempPlanningLotWithPOIdToTemp(setLotList, Config.C_OPEN_STATUS, this.C_P);
		} else {
			counterAll += tplModel.updateTempPlanningLotWithTempProdId(setLotList, Config.C_OPEN_STATUS);
		}
		return counterAll;
	}
}
