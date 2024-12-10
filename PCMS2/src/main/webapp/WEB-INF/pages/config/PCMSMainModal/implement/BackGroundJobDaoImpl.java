package dao.implement;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Test.utilities.HandlerListLog;
import dao.BackGroundJobDao;
import entities.CSO_SOASDetail;
import entities.ChangeSettingLogDetail;
import entities.InputForecastDetail;
import entities.InputGroupDetail;
import entities.InputPODetail;
import entities.InputPlanningLotDetail;
import entities.InputTempProdDetail;
import entities.MasterSettingChangeDetail;
import entities.POManagementDetail;
import entities.ProdOrderRunningDetail;
import entities.ResendSORDateDetail;
import entities.WorkDateDetail;
import entities.master.PPMM2.DataFromSapDetail;
import entities.master.PPMM2.RollFromSapDetail;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.POManagementModel;
import model.PlanningProdModel;
import model.ProdCreatedDetailModel;
import model.ReportModel;
import model.SOAS.SOASDataImportChangeDetailModel;
import model.master.CSOSOASModel;
import model.master.ChangeSettingLogModel;
import model.master.DataFromSapModel;
import model.master.GroupWorkDateModel;
import model.master.MasterSettingChangeModel;
import model.master.ProdOrderRunningModel;
import model.master.RollFromSapModel;
import model.master.SORPODetailChangeModel;
import model.master.SORPODetailModel;
import model.master.SORTempProdModel;
import model.master.SorForecastDetailModel;
import model.master.TEMPPlanningLotModel;
import model.master.erp.atech.ERPAtechModel;
import model.master.multi.MTempPlanningLotSORTempProdModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class BackGroundJobDaoImpl implements BackGroundJobDao {
	private String C_SYSTEM = "SYSTEM";
	private String conType = "";
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	private String leftJoinIAD =
			"" + "  left join [PPMM].[dbo].[InputArticleDetail] as IAD on a.[Article] = IAD.[Article]\r\n";
	private String innerJoinPODetailB = " "
			+ " inner join ( \r\n"
			+ "     SELECT \r\n"
			+ "       a.[Id]\r\n"
			+ "      ,a.[DocDate]\r\n"
			+ "      ,a.[CustomerNo]\r\n"
			+ "      ,a.[CustomerName]\r\n"
			+ "      ,a.[PO]\r\n"
			+ "      ,a.[POLine]\r\n"
			+ "      ,a.[MaterialNoTWC] \r\n"
			+ "      ,case\r\n"
			+ "			when IAD.[ArticleReplaced] is not null and IAD.ArticleReplaced <> '' then IAD.[ArticleReplaced]\r\n"
			+ "			else a.Article\r\n"
			+ "			end as Article\r\n"
			+ "      ,a.[Design]\r\n"
			+ "      ,a.[ColorCustomer]\r\n"
			+ "      ,a.[Color]\r\n"
			+ "      ,a.[LabRef]\r\n"
			+ "      ,a.[OrderQty]\r\n"
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
			+ "  FROM [PPMM].[dbo].[SOR_PODetail] as a\r\n"
			+ this.leftJoinIAD
			+ "  where a.DataStatus = 'O' \r\n"
			+ ") as b on a.POId = b.Id \r\n ";
//	private String leftJoinRDB =
//			"" + " left join [PCMS].[dbo].[FromSapMainProd] as b on a.[ProductionOrder] = b.[ProductionOrder]   \r\n";
//	private String leftJoinRDC = ""
//			+ " left join [PPMM].[dbo].[TEMP_PlanningLot] as c on a.[ProductionOrder] = c.[ProductionOrder] and\r\n"
//			+ "                                                   a.[Operation] = c.[Operation]\r\n";
////	private String innerJoinRDD = ""
////	+ " inner join [PPMM].[dbo].[InputArticleSubGroupDetail] as d on a.[Article] = d.[Article]\r\n";
//	private String leftJoinRDF = ""
//			+ "  left join [PPMM].[dbo].[PlanLotRedyeDetail] as f on a.[ProductionOrder] = f.[ProductionOrder] and\r\n"
//			+ "                                                      a.[Operation] = f.[Operation]\r\n";
	// ------------------------------------------------------------------------------------------------
//	public SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
//	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public BackGroundJobDaoImpl(Database database, String conType) {
		this.database = database;
		this.conType = conType;
		this.message = "";
	}

	@Override
	public void backGroudJopPushTMR() {

		String keyMainSup = "";
		String tmpWorkDate = "";
		int lotPerDay = 0,counterCheck = 0;
		PlanningProdModel ppModel = new PlanningProdModel(this.conType);
		GroupWorkDateModel gwdModel = new GroupWorkDateModel(this.conType);
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		HashMap<String, Integer> mapWorkStrIdx = new HashMap<>();
		HashMap<String, String> mapWorkIdxStr = new HashMap<>();
		HashMap<String, Integer> mapLotPerDay = new HashMap<>();
  
		this.execHandlerProdOrderType();
		ArrayList<InputTempProdDetail> poTDYNTMRList;
		ArrayList<WorkDateDetail> workList = gwdModel.getGroupWorkDateDetail("TMR", "O");
		ArrayList<InputPlanningLotDetail> groupList = ppModel.getSubGroupDetail();

		// GET WORK DAY EXCEPT HOLIDAY
		for (WorkDateDetail beanTmp : workList) {
			mapWorkStrIdx.put(beanTmp.getKeyMainSup() + Config.C_COLON + beanTmp.getWorkDate(), beanTmp.getRowNum());
			mapWorkIdxStr.put(beanTmp.getKeyMainSup() + Config.C_COLON + beanTmp.getRowNum(), beanTmp.getWorkDate());
		}
//		String caseWork = "";
		// CONFIG LOTPERDAY
		for (int i = 0; i < groupList.size(); i ++ ) {
			ArrayList<InputTempProdDetail> tmpWorkTMRList = new ArrayList<>();
			ArrayList<InputTempProdDetail> tmpCancelTMRList = new ArrayList<>();
			InputPlanningLotDetail bean = groupList.get(i);
			counterCheck = 0;
			lotPerDay = bean.getLotPerDay();
			keyMainSup = bean.getKeyMainSup();
			mapLotPerDay.put(keyMainSup, lotPerDay);
			tmpWorkDate = mapWorkIdxStr.get(keyMainSup + Config.C_COLON + "1");
			poTDYNTMRList = ppModel.getTempPONPlanLotRedyeDetailForPushTMR("TDYNTMR", bean, tmpWorkDate);
//			System.out.println(" GROUP NO: "+bean.getGroupNo()+" | "+bean.getSubGroup()+" tmpWorkDate : "+tmpWorkDate );
			for (int j = 0; j < poTDYNTMRList.size(); j ++ ) {
				InputTempProdDetail beanTmpLot = poTDYNTMRList.get(j);
				String planUserDateTmpStr = beanTmpLot.getPlanUserDate();
//				System.out.println(" PRD ORDER : "+beanTmpLot.getProductionOrder()+" | counterCheck "+counterCheck+" lotPerDay : "+lotPerDay );
				if (counterCheck < lotPerDay) {
					beanTmpLot.setPlanSystemDate(tmpWorkDate);
					beanTmpLot.setPlanUserDate(tmpWorkDate);
//					caseWork = "1      ";
					if (planUserDateTmpStr.equals(tmpWorkDate)) {
					} else { beanTmpLot.setPlanBy(this.C_SYSTEM); }
					tmpWorkTMRList.add(beanTmpLot);
					counterCheck += 1;
					
				} else {
//					caseWork = "2      ";
					beanTmpLot.setPlanSystemDate(Config.C_BLANK);
//					beanTmpLot.setPlanUserDate(Config.C_BLANK);  // dont use it it gonna broken
					beanTmpLot.setPlanUserChangeDate(Config.C_BLANK);
					beanTmpLot.setPlanBy(Config.C_BLANK); 
					tmpCancelTMRList.add(beanTmpLot);
				}
//				System.out.println( " PRD ORDER : "+beanTmpLot.getProductionOrder()
//				+" | "+beanTmpLot.getTempPlanningId()
//				+" | "+beanTmpLot.getPlanSystemDate()
//				+" | "+beanTmpLot.getGroupNo()
//				+" | "+beanTmpLot.getSubGroup()
//				+" | counterCheck "+counterCheck
//				+" lotPerDay : "+lotPerDay );
			}
			if (tmpWorkTMRList.size() > 0) {
				tplModel.updateTEMPPlanningLotWithUserDateById(tmpWorkTMRList);
			}
			if (tmpCancelTMRList.size() > 0) {
				tplModel.updateCancelDateByIdForTEMPPlanningLot(tmpCancelTMRList); 
			}
		}
		// 1. GET PO LOT + REDYE PLAN BY USER TODAY
		// 2. GET WORK NEXT WORK DATE EACH GROUP
		// 3. GET PO LOT + REDYE PLAN BY USER NEXT WORK DATE SORT BY CUS DUE, FISRT
		// LOT[N],POID
		// USE (3)
	}


	@Override
	public void execCalOperationWorkDate() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_CalOperationWorkDate] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execHandlerArticleHWToAvailable() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_HandlerArticleHWToAvailable]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execHandlerBatchNoForRelation() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_HandlerBatchNoForRelation]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execHandlerForecastDetail() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_HandlerForecastDetail]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execHandlerPlanLotSORDetail() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_HandlerPlanLotSORDetail] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execHandlerProdOrderType() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_HandlerProdOrderType] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execHandlerSOR_PODetailWithHireWorkArticle() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_HandlerSOR_PODetailWithHireWorkArticle] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execHandlerStatusAfterDate() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_HandlerStatusAfterDate] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execHandlerTempPlanningLotDailyLog() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_HandlerTempPlanningLotDailyLog]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execHandlerTempPlanningLotNotWorkLog() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_HandlerTempPlanningLotNotWorkLog]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execOneTimeUpsertToProdOrderRunning() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_OneTime_UpsertToProdOrderRunning] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execReplacedProdOrderOldWithNew(String prodOrderOld, String prodOrderNew) {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_ReplacedProdOrderOldWithNew]  @PrdNumberOld = ? ,@PrdNumberNew = ? ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.setString(1, prodOrderOld);
			prepared.setString(2, prodOrderNew);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpdateOrderQtyCal() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_updateOrderQtyCal] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertBKKWorkDate() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_UpsertBKKWorkDate]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertForecastGroupOptions() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_UpsertForecastGroupOptions] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
 
	@Override
	public void execUpsertGroupWorkDate() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_UpsertGroupWorkDate] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertLeadTime() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_UpsertLeadTime] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertProdPreset() {
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_UpsertProdPreset]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertRuleCalculated() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_UpsertRuleCalculated] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertRunningDate() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_UpsertRunningDate] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertTempPlanningLot() {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_updateOrderQtyCal] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertToProdOrderRunning(String keyword) {
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [PPMM].[dbo].[spd_UpsertToProdOrderRunning] ? ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.setString(1, keyword);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public ArrayList<InputGroupDetail> getArticleSubGroupDetailByArticleListNColor(ArrayList<InputGroupDetail> poList,
			String colorType) {
		ArrayList<InputGroupDetail> list = null;
		InputGroupDetail bean = poList.get(0);
		List<String> articleList = bean.getArticleList();
		String whereArticle = " (  \r\n";
		for (int i = 0; i < articleList.size(); i ++ ) {
			whereArticle += " a.Article = '" + articleList.get(i) + "' ";
			if (i < articleList.size()
					-1) {
				whereArticle += " or \r\n";
			}
		}
		whereArticle += " ) \r\n";
		whereArticle += " and ( d.[ColorType] = '" + colorType + "' or d.[ColorType] = 'All' ) \r\n";
		String sql = ""
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
				+ "        a.[LotMinMax] <> '' and "
				+ whereArticle
				+ " Order By a.[LotMinMax] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genGroupDetail(map));
		}
		return list;
	}

	public ArrayList<InputTempProdDetail> getForecastSumProdMoreThanRemain() {
		ArrayList<InputTempProdDetail> list = null;
		String sql = ""
				+ " \r\n"
				+ " SELECT *\r\n"
				+ " FROM (\r\n"
				+ "		SELECT a.[Id] as ForecastId\r\n"
				+ "		, b.Id as TempProdId\r\n"
				+ "		, [DocDate]\r\n"
				+ "		, [CustomerNo]\r\n"
				+ "		, [CustomerName]\r\n"
				+ "		, B.ColorType\r\n"
				+ "		, [ForecastNo] as PO\r\n"
				+ "		, [ForecastMY] \r\n"
				+ "				,case \r\n"
				+ "					when b.ProdOrderQty > 300 then 300\r\n"
				+ "					else b.ProdOrderQty\r\n"
				+ "				end  as 	ProdOrderQty\r\n"
				+ "		, [RemainNonBLQty] \r\n"
				+ "		, [RemainBLQty]   \r\n"
				+ "		, d.Id as TempPlanningId\r\n"
				+ " 	, b.ProductionOrder\r\n"
				+ "		, (select  sum(ProdOrderQty) as SumProdOrderQty"
				+ "    		from [PPMM].[dbo].[SOR_TempProd] as SUB_A\r\n"
				+ "			where ForecastId is not null and \r\n"
				+ "               ColorType = 'Black' AND \r\n"
				+ "               SUB_A.ForecastId = A.Id\r\n"
				+ "			group by ForecastId,ColorType) AS SumProdOrderQtyBL\r\n"
				+ "		, (select  sum(ProdOrderQty) as SumProdOrderQty\r\n"
				+ "    		from [PPMM].[dbo].[SOR_TempProd] as SUB_B\r\n"
				+ "			where SUB_B.[ForecastId] is not null and\r\n"
				+ "               SUB_B.[ColorType] = 'Color'  AND\r\n"
				+ "               SUB_B.[ForecastId] = A.Id\r\n"
				+ "			group by ForecastId,ColorType) AS SumProdOrderQtyNonBL\r\n"
				+ "		,b.DataStatus\r\n"
				+ " 	FROM [PPMM].[dbo].[SOR_ForecastDetail] as a\r\n"
				+ "		inner join [PPMM].[dbo].[SOR_TempProd] as b on a.id = b.ForecastId  \r\n"
				+ "		inner join [PPMM].[dbo].[TEMP_PlanningLot] as d on b.ID = d.TempProdId \r\n"
				+ " 	where b.DataStatus = 'O'"
				+ " )  AS A\r\n"
				+ " where ( SumProdOrderQtyBL > [RemainBLQty] ) OR\r\n"
				+ "	      ( SumProdOrderQtyNonBL > [RemainNonBLQty])\r\n"
				+ " order by a.ForecastId,ColorType,TempProdId\r\n"
				+ "";
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	public String getMessage() {
		return this.message;
	}

//	public ArrayList<InputTempProdDetail> getPlanLotRedyeDetail() {
//		ArrayList<InputTempProdDetail> list = null;
//		String sql = ""
//				+ " SELECT distinct  \r\n"
//				+ "	 	f.[Id] as No\r\n"
//				+ "		,NULL as [POId]\r\n"
//				+ "		,NULL as [ForecastId] \r\n"
//				+ "		,case \r\n"
//				+ "			when  LEFT ( RIGHT(a.[MaterialNumber] ,6),2 ) = 'BL' THEN 'Black' \r\n"
//				+ "			else 'Color' \r\n"
//				+ "			end as [ColorType]\r\n"
//				+ "		,a.[ProductionOrder]\r\n"
//				+ "		,NULL as [FirstLot]\r\n"
//				+ "		, CAST(a.[QuantityMR] as decimal(13,3))  as [ProdOrderQty]\r\n"
//				+ "		,NULL as [PPMMStatus]\r\n"
//				+ "		,NULL as [DataStatus]\r\n"
//				+ "		,NULL as [ChangeDate]\r\n"
//				+ "		,NULL as [ChangeBy] \r\n"
//				+ "		,a.[ColorCustomer] \r\n"
//				+ "		,a.[FGDesign] AS [Design]\r\n"
//				+ "		,NULL as [CustomerDue]\r\n"
//				+ "		,a.[Article]\r\n"
//				+ "		,NULL as [CustomerName]\r\n"
//				+ "		,'REDYE' as LotType\r\n"
//				+ "		,NULL as DyeAfterCFM\r\n"
//				+ "		,c.PlanSystemDate \r\n"
//				+ "		,c.PlanUserDate  \r\n"
//				+ "		,a.OperationEndDate\r\n"
//				+ "		,c.GroupNo\r\n"
//				+ "		,c.SubGroup\r\n"
//				+ "		,NULL as [GroupOptions] \r\n"
//				+ "		,NULL as [FirstLotDate]\r\n"
//				+ "		,NULL as [PlanBy]\r\n"
//				+ "		,NULL as [GreigePlan] \r\n"
//				+ "		,NULL as [DyeAfterGreigeInBegin]\r\n"
//				+ "		,NULL as [DyeAfterGreigeInLast]\r\n"
//				+ "		,NULL as [FirstLotGroupNo]\r\n"
//				+ "		,NULL as [FirstLotSubGroup]\r\n"
//				+ "     ,a.[Operation]\r\n"
//				+ "     ,A.Id as ProductionOrderId"
//				+ "	FROM [PPMM].[dbo].[DataFromSap] as a \r\n"
//				+ this.leftJoinRDB
//				+ this.leftJoinRDC
//				+ this.leftJoinRDF
//				+ "	where ( ( a.Operation >= 101 and a.Operation <=103 ) or\r\n"
//				+ "			( b.[OrderType]  = 'ARW0' and a.Operation = 100 ) or \r\n"
//				+ "			( [RemarkTwo] like '%ซ่อมแทน%'  and  ( a.Operation >= 101 and a.Operation <=103 ) )    \r\n"
//				+ "		  ) and a.OperationStatus = 'NOT DONE' AND \r\n"
//				+ "		    ( a.UserStatus <> 'ขายแล้ว' and a.UserStatus <> 'รอขาย' and a.UserStatus <> 'ยกเลิก' and a.UserStatus <> 'ตัดเกรดZ'  \r\n"
//				+ " 		and a.UserStatus <> 'จบการผลิต' and a.UserStatus <> 'ปิดเพื่อแก้ไข' and a.UserStatus <> 'HOLD,รอโอน'  \r\n"
//				+ "			and a.UserStatus <> '' ) AND \r\n"
//				+ "     C.PlanUserDate IS NULL and \r\n"
//				+ "		( A.OperationEndDate is null ) and \r\n"
//				+ "		a.[Article] <> '' and \r\n"
//				+ "		a.[MaterialNumber] <> ''\r\n";
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genTempProdDetail(map));
//		}
//		return list;
//	}

	public ArrayList<InputTempProdDetail> getSORTempProdDetailGroupOptionsNull() {
		ArrayList<InputTempProdDetail> list = null;
		String sql = ""
				+ " SELECT  a.[Id] as No\r\n"
				+ "		,[POId]\r\n"
				+ "		,[ForecastId]\r\n"
				+ "		,[ColorType]\r\n"
				+ "		,a.[ProductionOrder]\r\n"
				+ "		,[FirstLot]\r\n"
				+ "		,[ProdOrderQty]\r\n"
				+ "		,[PPMMStatus]\r\n"
				+ "		,a.[DataStatus]\r\n"
				+ "		,a.[ChangeDate]\r\n"
				+ "		,a.[ChangeBy]\r\n"
				+ "		,b.[ColorCustomer]   \r\n"
				+ "		,b.[Design]\r\n"
				+ "		,b.[CustomerDue] \r\n"
				+ "		,b.[Article]\r\n"
				+ "		,b.[CustomerName]\r\n"
				+ "		,'PO' as LotType\r\n"
				+ "		,b.DyeAfterCFM\r\n"
				+ "		,a.[GroupOptions]\r\n"
				+ "    ,[DyeAfterGreigeInBegin]\r\n"
				+ "    ,[DyeAfterGreigeInLast]\r\n"
				+ " FROM [PPMM].[dbo].[SOR_TempProd] as a\r\n"
				+ this.innerJoinPODetailB
				+ " where ( a.[GroupOptions] is null or a.[GroupOptions] = '' ) and \r\n"
				+ "         a.[DataStatus] = 'O' \r\n"
				+ " ORDER BY [POId],[FirstLot]";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempProdDetail(map));
		}
		return list;
	}

	public ArrayList<InputPODetail> handlerCheckDifPONewWithOld(ArrayList<InputPODetail> oldPOList,
			ArrayList<InputPODetail> newPOList, String remarkAction) {
		// TODO Auto-generated method stub

		String changeTable = Config.sqlTableChangeSORPODetailLog;
		String changeId = Config.sqlFieldSORPODetailId;
		SORPODetailChangeModel sdpcModel = new SORPODetailChangeModel(this.conType);
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);

		HashMap<Integer, InputPODetail> mapIdAndPODetail = new HashMap<>();
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<InputPODetail> cancelPOChangeList = new ArrayList<>();
		ArrayList<InputPODetail> recreatePOChangeList = new ArrayList<>();
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		ArrayList<MasterSettingChangeDetail> listMSC = mscModel.getMasterSettingChangeDetail(changeTable);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}

		for (InputPODetail beanTmp : newPOList) {
			int newId = beanTmp.getId();
			if (newId != 0) {
				mapIdAndPODetail.put(newId, beanTmp);
			}
		}
//				upsertSORPODetailChangeWithPOId
		for (int i = 0; i < oldPOList.size(); i ++ ) {
			InputPODetail beanTmpOld = oldPOList.get(i);
			int oldId = beanTmpOld.getId();
			// CANCEL PO / POLINE IF DIF
			String oldPO = beanTmpOld.getPo();
			String oldPOLine = beanTmpOld.getPoLine();
			String oldArticle = beanTmpOld.getArticle();
			String oldArticleReal = beanTmpOld.getArticleReal();
			String oldArticleCheck = oldArticle;
			if ( ! oldArticleReal.equals(Config.C_BLANK)) {
				oldArticleCheck = oldArticleReal;
			}
			String oldMaterial = beanTmpOld.getChangeBy();
			String oldColor = beanTmpOld.getColor();
			String oldCustomerNo = beanTmpOld.getCustomerNo();
			String oldCustomerDue = beanTmpOld.getCustomerDue();
			String oldDivision = beanTmpOld.getDivision();
			String oldGreigePlan = beanTmpOld.getGreigePlan();
			String oldDistChannal = beanTmpOld.getDistChannal();

			// CANCEL PO / POLINE IF DIF AND CANCEL CHANGE PO QTY IF HAVE
			String oldOrderQty = beanTmpOld.getOrderQty();
			String oldChangeOrderQty = beanTmpOld.getChangedOrderQty();
			String oldUnit = beanTmpOld.getUnit();

			// JUST CHECK CHANGE
			String oldColorCustomer = beanTmpOld.getColorCustomer();
			String oldDesign = beanTmpOld.getDesign();
			String oldLabRef = beanTmpOld.getLabRef();
			String oldSaleOrg = beanTmpOld.getSaleOrg();
			String oldModelCode = beanTmpOld.getModelCode();
			boolean oldIsDelete = beanTmpOld.isDelete();
			boolean oldIsUpdate = beanTmpOld.isUpdate();
//					String.valueOf(oldIsTopping)
			String newChangeBy = "SOR Dif Change";
//			String oldDataStatus = "O" ;
			InputPODetail beanTmpNew = mapIdAndPODetail.get(oldId);
			if (beanTmpNew != null) {
//				int newId = beanTmpNew.getId();
//				String newDataStatus = beanTmpNew.getDataStatus();
				// CANCEL PO / POLINE IF DIF
				String newPO = beanTmpNew.getPo();
				String newPOLine = beanTmpNew.getPoLine();
				String newArticle = beanTmpNew.getArticle();
				String newArticleReal = beanTmpNew.getArticleReal();
				String newArticleCheck = newArticle;
				if ( ! newArticleReal.equals(Config.C_BLANK)) {
					newArticleCheck = newArticleReal;
				}
				String newMaterial = beanTmpNew.getChangeBy();
				String newColor = beanTmpNew.getColor();
				String newCustomerNo = beanTmpNew.getCustomerNo();
				String newCustomerDue = beanTmpNew.getCustomerDue();
				String newDivision = beanTmpNew.getDivision();
				String newGreigePlan = beanTmpNew.getGreigePlan();
				String newDistChannal = beanTmpNew.getDistChannal();

				// CANCEL PO / POLINE IF DIF AND CANCEL CHANGE PO QTY IF HAVE
				String newOrderQty = beanTmpNew.getOrderQty();
				String newChangeOrderQty = beanTmpNew.getChangedOrderQty();
				String newUnit = beanTmpNew.getUnit();
				boolean isCancelPOChange = false;
				if (newOrderQty.equals(oldOrderQty) && newUnit.equals(oldUnit)) {
					isCancelPOChange = false;
				} else {
					isCancelPOChange = true;
					beanTmpNew.setChangeBy(newChangeBy);
					cancelPOChangeList.add(beanTmpNew);
				}
				// JUST CHECK CHANGE
				String newColorCustomer = beanTmpNew.getColorCustomer();
				String newDesign = beanTmpNew.getDesign();
				String newLabRef = beanTmpNew.getLabRef();
				String newSaleOrg = beanTmpNew.getSaleOrg();
				String newModelCode = beanTmpNew.getModelCode();
				boolean newIsDelete = beanTmpNew.isDelete();
				boolean newIsUpdate = beanTmpNew.isUpdate();
//				System.out.println(oldPO+" "+newPO);
				if (newIsDelete) {
				} else if (isCancelPOChange) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldPO.equals(newPO)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldPOLine.equals(newPOLine)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldArticleCheck.equals(newArticleCheck)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldMaterial.equals(newMaterial)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldColor.equals(newColor)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldCustomerNo.equals(newCustomerNo)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldCustomerDue.equals(newCustomerDue)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldDivision.equals(newDivision)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldGreigePlan.equals(newGreigePlan)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldDistChannal.equals(newDistChannal)) {
					recreatePOChangeList.add(beanTmpNew);
				} else if ( ! oldChangeOrderQty.equals(newChangeOrderQty)) {
					recreatePOChangeList.add(beanTmpNew);
				}else if ( ! oldUnit.equals(newUnit)) {
					recreatePOChangeList.add(beanTmpNew);
				}else if ( ! oldOrderQty.equals(newOrderQty)) {
					recreatePOChangeList.add(beanTmpNew);
				}

				listCSL = HandlerListLog.handlerListLog(listCSL, oldPO, newPO, mapMSC.get(Config.sqlFieldPO),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldPO, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldPOLine, newPOLine, mapMSC.get(Config.sqlFieldPOLine),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldPOLine, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldArticleCheck, newArticleCheck,
						mapMSC.get(Config.sqlFieldArticle), Integer.toString(oldId), newChangeBy, Config.sqlFieldArticle,
						remarkAction);

				listCSL = HandlerListLog.handlerListLog(listCSL, oldMaterial, newMaterial, mapMSC.get(Config.sqlFieldMaterialNo),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldMaterialNo, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldColor, newColor, mapMSC.get(Config.sqlFieldColor),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldColor, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldCustomerNo, newCustomerNo,
						mapMSC.get(Config.sqlFieldCustomerNo), Integer.toString(oldId), newChangeBy, Config.sqlFieldCustomerNo,
						remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldCustomerDue, newCustomerDue,
						mapMSC.get(Config.sqlFieldCustomerDue), Integer.toString(oldId), newChangeBy, Config.sqlFieldCustomerDue,
						remarkAction);

				listCSL = HandlerListLog.handlerListLog(listCSL, oldDivision, newDivision, mapMSC.get(Config.sqlFieldDivision),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldDivision, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldGreigePlan, newGreigePlan,
						mapMSC.get(Config.sqlFieldGreigePlan), Integer.toString(oldId), newChangeBy, Config.sqlFieldGreigePlan,
						remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldDistChannal, newDistChannal,
						mapMSC.get(Config.sqlFieldDistChannal), Integer.toString(oldId), newChangeBy, Config.sqlFieldDistChannal,
						remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldOrderQty, newOrderQty, mapMSC.get(Config.sqlFieldOrderQty),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldOrderQty, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldUnit, newUnit, mapMSC.get(Config.sqlFieldUnit),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldUnit, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldColorCustomer, newColorCustomer,
						mapMSC.get(Config.sqlFieldColorCustomer), Integer.toString(oldId), newChangeBy,
						Config.sqlFieldColorCustomer, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldDesign, newDesign, mapMSC.get(Config.sqlFieldDesign),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldDesign, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldLabRef, newLabRef, mapMSC.get(Config.sqlFieldLabRef),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldLabRef, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldSaleOrg, newSaleOrg, mapMSC.get(Config.sqlFieldSaleOrg),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldSaleOrg, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldModelCode, newModelCode, mapMSC.get(Config.sqlFieldModelCode),
						Integer.toString(oldId), newChangeBy, Config.sqlFieldModelCode, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, String.valueOf(oldIsDelete), String.valueOf(newIsDelete),
						mapMSC.get(Config.sqlFieldIsDelete), Integer.toString(oldId), newChangeBy, Config.sqlFieldIsDelete,
						remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, String.valueOf(oldIsUpdate), String.valueOf(newIsUpdate), mapMSC
						.get(Config.sqlFieldIsUpdate), Integer.toString(oldId),
						newChangeBy, Config.sqlFieldIsUpdate, remarkAction);
			}
		}  
		sdpcModel.upsertSORPODetailChangeWithPOId(cancelPOChangeList);
//		String iconStausUpsert = sdpcModel.upsertSORPODetailChangeWithPOId(cancelPOChangeList);
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, changeTable, changeId);
		}
		return recreatePOChangeList;
	}

	public String handlerCheckPOHeaderOrPOChange(ArrayList<InputPODetail> listForPOId, InputPODetail beanSPD,
			boolean bl_needUpdate, boolean bl_poQtyChange) {

		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		POManagementModel pomModel = new POManagementModel(this.conType);
		if ( ! listForPOId.isEmpty()) {
			InputPODetail beanForPOId = listForPOId.get(0);
			boolean isLotInSap = beanForPOId.isLotInSap();
			beanSPD.setId(beanForPOId.getId());
			beanSPD.setChangeBy(Config.C_SOMS); 
			if ( ! isLotInSap) {
				if (bl_needUpdate) {
					// HANDLER LOG
					POManagementDetail pd = new POManagementDetail();
					ArrayList<POManagementDetail> listForPOMM = new ArrayList<>();

					pd.setPoId(beanForPOId.getId());
					pd.setPo(beanForPOId.getPo());
					pd.setRemark("PO Qty change need cancel PO For Create again ");
					pd.setPoPuangId(beanForPOId.getPoPuangId());
					pd.setChangeBy(Config.C_SOMS);
					listForPOMM.add(pd);
					// System.out.println(beanCancel.getId()+" "+ beanCancel.getPo());
					// HANDLER UPDATE
					String iconStatus = spdModel.updateSORPODetailWithSOASDetail(beanSPD);
					if (iconStatus.equals("I") && bl_poQtyChange) {
						ArrayList<InputPODetail> poCreateList = new ArrayList<>();
						poCreateList.add(beanSPD);
						pomModel.cancelPOLine(listForPOMM, Config.C_OPEN_STATUS,true);
						this.tryCreateWithNewPOQty(poCreateList);
					}
					// if bl_poQtyChange = true recreate
				}
			}
		}
		return C_SYSTEM;
	}

	@Override
	public void handlerCheckPONewWithOld(ArrayList<InputPODetail> oldPOList, ArrayList<InputPODetail> newPOList,
			String remarkAction) {
		POManagementModel pomModel = new POManagementModel(this.conType);
		ProdCreatedDetailModel pcdModel = new ProdCreatedDetailModel(this.conType);
		PlanningProdModel ppModel = new PlanningProdModel(this.conType);
//		ReportModel rpModel = new ReportModel(this.conType);
		// CHECK DIF AND CREATE LOG AND GET DIF PO
		// SOR_PODetailChange ALREADY CANCEL HERE
		ArrayList<InputPODetail> listPO = this.handlerCheckDifPONewWithOld(oldPOList, newPOList, remarkAction);
		ArrayList<POManagementDetail> poList = new ArrayList<>();
		ArrayList<InputPODetail> listTmp = new ArrayList<>();
		ArrayList<InputTempProdDetail> tmpList = new ArrayList<>();
 
		// TODO Auto-generated method stub
		if ( ! listPO.isEmpty()) {
			for (int i = 0; i < listPO.size(); i ++ ) {
				poList.clear();
				POManagementDetail pd = new POManagementDetail();
				InputPODetail bean = listPO.get(i);
				pd.setPoId(bean.getId());
				pd.setPo(bean.getPo());
				pd.setRemark("PO Data Change");
				pd.setPoPuangId(bean.getPoPuangId());
				pd.setChangeBy("SOR");
				poList.add(pd); 
				// already cancle old po detail change if qty not same 
				pomModel.cancelPOLine(poList, Config.C_OPEN_STATUS,false);  
			}
			// CANCEL PO  
//			pomModel.cancelPOLine(poList, Config.C_OPEN_STATUS);
			// CREATE PO FROM DIF PO

			this.execUpsertRuleCalculated();
			this.execUpdateOrderQtyCal();
			this.searchPOTempGroupOptionListForNull();
			
			ArrayList<InputPODetail> list = pcdModel.getPODetailWithPOId(listPO);
			for (int i = 0; i < list.size(); i ++ ) {
				listTmp.clear();
				tmpList.clear();
				InputPODetail bean = list.get(i);
				listTmp.add(bean);
				tmpList = pcdModel.createTempLotPOFromSOR(listTmp);
				if ( ! tmpList.isEmpty()) {
					if (tmpList.get(0).getIconStatus().equals("I")) {
						ppModel.rePlanningLot();
					}
				}
			}
		}
		// FOR TEST
//		this.execHandlerPlanLotSORDetail();
////		spdModel.updateIsTryCreateLotForSORPODetail(list, true);
//		pcdModel.searchGroupOptionListForNull( );
//		rpModel.processVolumeForReport();
//		this.execHandlerBatchNoForRelation();
		// FOR TEST
	}

	@Override
	public void handlerFCDataFromSOR() {
		// TODO Auto-generated method stub
		SorForecastDetailModel sfdModel = new SorForecastDetailModel(this.conType);
		ProdCreatedDetailModel pcdModel = new ProdCreatedDetailModel(this.conType);
		this.execUpsertForecastGroupOptions();
		ArrayList<InputForecastDetail> list = pcdModel.getForecastDetail("15");
		pcdModel.createTempLotFC(list);
		sfdModel.updateIsTryCreateLotForForecastDetail(list, true);
		this.handlerForecastSumProdMoreThanRemain();
	}

	@Override
	public void handlerForecastSumProdMoreThanRemain() {
//		System.out.println(" 0.1 : "+new Timestamp(System.currentTimeMillis()));
		ArrayList<InputTempProdDetail> list = this.getForecastSumProdMoreThanRemain();
//		System.out.println(" 0.11 : "+new Timestamp(System.currentTimeMillis()));
		ArrayList<InputTempProdDetail> beanAllList = new ArrayList<>();
//		ArrayList<InputTempProdDetail> beanList = new ArrayList<>();
//		ArrayList<InputTempProdDetail> beanChangeQtyList = new ArrayList<>();
		int tmpForecastId = 0;
		String tmpColorType = "";
		double tmpRemainBL = 0;
		double tmpRemainNonBL = 0;
		double tmpSumProdQtyBL = 0;
		double tmpSumProdQtyNonBL = 0;
		double prodQty = 0;
		String curColorType = "";
		double curRemainBL = 0;
		double curRemainNonBL = 0;
		@SuppressWarnings("unused") double curSumProdQtyBL = 0;
		@SuppressWarnings("unused") double curSumProdQtyNonBL = 0;

		double db_prodQty = 0;

		boolean isHandlerYet = false;
		int curForecastId = 0;

		for (int i = 0; i < list.size(); i ++ ) {
			InputTempProdDetail bean = list.get(i);
			db_prodQty = 0;
			curForecastId = bean.getForecastId();
			prodQty = bean.getDbProdQty();
			curRemainBL = bean.getRemainBLQty();
			curRemainNonBL = bean.getRemainNonBLQty();
			curSumProdQtyBL = bean.getSumProdOrderQtyBL();
			curSumProdQtyNonBL = bean.getSumProdOrderQtyNonBL();
			curColorType = bean.getColorType();
			bean.setChangeBy("FCBGJOB");
			if (tmpForecastId == 0 || tmpForecastId != curForecastId) {
				tmpForecastId = curForecastId;
				tmpRemainBL = curRemainBL;
				tmpRemainNonBL = curRemainNonBL;
				tmpSumProdQtyBL = 0;
				tmpSumProdQtyNonBL = 0;
				tmpColorType = curColorType;
				isHandlerYet = false;
			}
			if ( ! tmpColorType.equals(curColorType)) {
				tmpForecastId = curForecastId;
				tmpRemainBL = curRemainBL;
				tmpRemainNonBL = curRemainNonBL;
				tmpSumProdQtyBL = 0;
				tmpSumProdQtyNonBL = 0;
				tmpColorType = curColorType;
				isHandlerYet = false;
			}
			if (prodQty == 0) {
				bean.setDataStatus(Config.C_CLOSE_STATUS);
				beanAllList.add(bean);
			} else if (tmpColorType.equals("Black")) {
//				System.out.println(tmpForecastId+" | TEMPProdID "+bean.getTempProdId()+" "+tmpColorType+" "+tmpSumProdQtyBL+" > "+tmpRemainBL);
				if (tmpSumProdQtyBL < tmpRemainBL) {
					isHandlerYet = true;
					if (tmpSumProdQtyBL+300 <= tmpRemainBL) {
						bean.setDataStatus(Config.C_OPEN_STATUS);
						tmpSumProdQtyBL = tmpSumProdQtyBL+300;
						db_prodQty = 300;
					} else {
						tmpSumProdQtyBL = tmpSumProdQtyBL+(tmpRemainBL-tmpSumProdQtyBL);
						db_prodQty = tmpSumProdQtyBL;
					}
					bean.setDbProdQty(db_prodQty);
					bean.setDataStatus(Config.C_OPEN_STATUS);
					beanAllList.add(bean);
//					System.out.println(tmpForecastId+" | AFTER "+bean.getTempProdId()+" "+tmpColorType+" "+tmpSumProdQtyBL+" > "+tmpRemainBL);

				} else if (isHandlerYet) { 
					bean.setDbProdQty(0);
					bean.setDataStatus(Config.C_CLOSE_STATUS);
					beanAllList.add(bean);
				} else { 
				}
			} else {
//				System.out.println(tmpForecastId+" | TEMPProdID "+bean.getTempProdId()+" "+tmpColorType+" "+tmpSumProdQtyBL+" "+tmpRemainBL);
				if (tmpSumProdQtyNonBL < tmpRemainNonBL) {
					isHandlerYet = true;
					if (tmpSumProdQtyNonBL+300 <= tmpRemainNonBL) {
						bean.setDataStatus(Config.C_OPEN_STATUS);
						tmpSumProdQtyNonBL = tmpSumProdQtyNonBL+300;
						db_prodQty = 300;
					} else {
						tmpSumProdQtyNonBL = tmpSumProdQtyNonBL+(tmpRemainNonBL-tmpSumProdQtyNonBL);
						db_prodQty = tmpSumProdQtyNonBL;
					}
//					if(tmpSumProdQtyNonBL - 300 >  tmpRemainNonBL) {
//						bean.setDataStatus(Config.C_OPEN_STATUS);
//						tmpSumProdQtyNonBL = tmpSumProdQtyNonBL - 300;
//						db_prodQty = 300;
//					}
//					else{
//						bean.setDataStatus(Config.C_OPEN_STATUS);
//						tmpSumProdQtyNonBL = tmpSumProdQtyNonBL - tmpRemainNonBL;
//						db_prodQty = tmpSumProdQtyNonBL;
//					}
					bean.setDbProdQty(db_prodQty);
//					bean.setDataStatus(Config.C_OPEN_STATUS);
					beanAllList.add(bean);
				} else if (isHandlerYet) {
					bean.setDbProdQty(0);
					bean.setDataStatus(Config.C_CLOSE_STATUS);
					beanAllList.add(bean);
				} else {

				}
			}
		}
		// FOR FASTNESS
////		this.updateDataStatusWithTempProdIdForTempPlanningLotAndSORTempProd(beanChangeQtyList, "O");
////		this.updateDataStatusWithTempProdIdForTempPlanningLotAndSORTempProd(beanList, "X");
//		this.updateDataStatusWithTempProdIdForTempPlanningLotAndSORTempProd(beanAllList );
		MTempPlanningLotSORTempProdModel mtplstpModel = new MTempPlanningLotSORTempProdModel(this.conType);
		mtplstpModel.updateDataStatusWithTempProdIdForTempPlanningLotAndSORTempProd(list);
//		System.out.println(" 3 : "+new Timestamp(System.currentTimeMillis()));
	}

	@Override
	public void handlerPODataFromSOR() {
		ProdCreatedDetailModel pcdModel = new ProdCreatedDetailModel(this.conType);
		PlanningProdModel ppModel = new PlanningProdModel(this.conType);
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		ReportModel rpModel = new ReportModel(this.conType);

		ArrayList<InputPODetail> listTmp = new ArrayList<>();
		ArrayList<InputTempProdDetail> tmpList = new ArrayList<>();

		this.execUpsertRuleCalculated();
		this.execUpdateOrderQtyCal();
		ArrayList<InputPODetail> list = pcdModel.getPODetail("TRYCREATE0");
		spdModel.updateIsTryCreateLotForSORPODetail(list, true); 
		for (int i = 0; i < list.size(); i ++ ) { 
			listTmp.clear();
			tmpList.clear();
			InputPODetail bean = list.get(i);
			listTmp.add(bean);
			tmpList = pcdModel.createTempLotPOFromSOR(listTmp);
			if ( ! tmpList.isEmpty()) {
				if (tmpList.get(0).getIconStatus().equals("I")) {
					ppModel.rePlanningLot();
				}
			}
		}
		this.execHandlerPlanLotSORDetail();
		pcdModel.searchGroupOptionListForNull();
		rpModel.processVolumeForReport();
		this.execHandlerBatchNoForRelation();
	}

	@Override
	public void handlerPODeleted() {
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		POManagementModel pomModel = new POManagementModel(this.conType);
		ArrayList<InputPODetail> listPO = spdModel.getPODetailWithIsDeleteTrue();
		ArrayList<POManagementDetail> poList = new ArrayList<>();

		// TODO Auto-generated method stub
		if ( ! listPO.isEmpty()) {
			for (int i = 0; i < listPO.size(); i ++ ) {
				poList.clear();
				POManagementDetail pd = new POManagementDetail();
				InputPODetail bean = listPO.get(i);
				pd.setPoId(bean.getId());
				pd.setPo(bean.getPo());
				pd.setRemark("IsDelete = 1");
				pd.setPoPuangId(bean.getPoPuangId());
				pd.setChangeBy("SOR");
				poList.add(pd);
				pomModel.cancelPOLine(poList, Config.C_CLOSE_STATUS,true);
			} 
		}
	}

	public String handlerReplaceTempProdWithReal(ArrayList<ResendSORDateDetail> listPOMain,
			ArrayList<ResendSORDateDetail> listOldPOLot, String remarkAction) {
		String changeTable = Config.sqlTableChangeSORTempProdLog;
		String changeId = Config.sqlFieldSORTempProdId;
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		ResendSORDateDetail beanMain = listPOMain.get(0);
		String newLotTemp = beanMain.getNewLotTemp();
		String changeBy = beanMain.getChangeBy();
		String keyWord = newLotTemp.substring(0, 3);
		String sevenToEightPos = newLotTemp.substring(6, newLotTemp.length());
		String iconStatus = "I";
//		String systemStauts = "Update Success.";
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		HashMap<String, String> mapMSC = new HashMap<>();
		// CheckProdOrder
//		System.out.println(listOld.size());
		if (listOldPOLot.size() > 0) {
			// 1. CHECK LOT ALREADY CREATE IN SAP
			ArrayList<MasterSettingChangeDetail> listMSC = mscModel.getMasterSettingChangeDetail(changeTable);
			for (MasterSettingChangeDetail beanTemp : listMSC) {
				mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
			}
			// 2. running
			ArrayList<ProdOrderRunningDetail> listRunningUsed = new ArrayList<>();
			for (int i = 0; i < listOldPOLot.size(); i ++ ) {
				listRunningUsed.clear();
				ResendSORDateDetail beanCurrent = listOldPOLot.get(i);
				int tempProdId = beanCurrent.getTempProdId();
				String curProdOrder = beanCurrent.getProductionOrder();
//				String newProdOrder = "";
				String newProdOrderFull = "";

				// FIND LASTESTRUNNING
				ArrayList<ProdOrderRunningDetail> listRunning = porModel.getProdOrderRunningDetail(keyWord);
				if (listRunning.isEmpty()) {
					bgjModel.execUpsertToProdOrderRunning(keyWord);
					listRunning = porModel.getProdOrderRunningDetail(keyWord);
				}
				ProdOrderRunningDetail beanTmpRunning = listRunning.get(0);// T0A001
				String runningNewProdOrder = beanTmpRunning.getProductionOrder();
				boolean isTopping = false;
				if (sevenToEightPos.contains("T")) {
					isTopping = true;
				}
				newProdOrderFull = runningNewProdOrder
						+sevenToEightPos;
				beanTmpRunning.setDataStatus(Config.C_CLOSE_STATUS);
				beanTmpRunning.setProductionOrderTemp(curProdOrder);
				beanTmpRunning.setProductionOrder(runningNewProdOrder);
				beanTmpRunning.setTopping(isTopping);
				beanTmpRunning.setRemark("RESEND TO SOR : " + curProdOrder + " => " + runningNewProdOrder);
				beanTmpRunning.setChangeBy(changeBy);
				listRunningUsed.add(beanTmpRunning);

				ProdOrderRunningDetail beanTmpCurrent = new ProdOrderRunningDetail();// T0A001
				beanTmpCurrent.setDataStatus(Config.C_OPEN_STATUS);
				beanTmpCurrent.setProductionOrderTemp(Config.C_BLANK);
				beanTmpCurrent.setProductionOrder(curProdOrder.substring(0, 6));
				beanTmpCurrent.setTopping(false);
				beanTmpCurrent.setRemark(Config.C_BLANK);
				beanTmpCurrent.setChangeBy(changeBy);
				listRunningUsed.add(beanTmpCurrent);
				cslModel.handlerChangeProdDetailForNewDetailList(listRunning, remarkAction);
				porModel.updateProdOrderRunningWithProductionOrder(listRunningUsed);

//				System.out.println( " runningNewProdOrder "+runningNewProdOrder);
//				System.out.println(curProdOrder+" "+newProdOrderFull);
				bgjModel.execReplacedProdOrderOldWithNew(curProdOrder, newProdOrderFull);

				// LOG CHANGE
				listCSL = HandlerListLog.handlerListLog(listCSL, curProdOrder, newProdOrderFull,
						mapMSC.get(Config.sqlFieldProductionOrder), Integer.toString(tempProdId), changeBy,
						Config.sqlFieldProductionOrder, remarkAction);
			}
			// INSERT LOG
			if ( ! listCSL.isEmpty()) {
				cslModel.insertChangeSettingLogDetail(listCSL, changeTable, changeId);
			}
		} else {
			iconStatus = Config.C_ERR_ICON_STATUS;
//			systemStauts = SqlErrorHandler.handlerSqlErrorText(iconStatus);
		}
		return iconStatus;
	}

	@Override
	public void handlerSOASDetailChangeDetail() {
		DecimalFormat formatter = new DecimalFormat("###,###,##0.0000");
		CSOSOASModel csoModel = new CSOSOASModel(this.conType);
		SOASDataImportChangeDetailModel sdcdModel = new SOASDataImportChangeDetailModel();

//		ProdCreatedDetailModel  pcdModel = new ProdCreatedDetailModel(this.conType);
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		POManagementModel pomModel = new POManagementModel(this.conType);
//		ArrayList<InputPODetail> listPO = spdModel.getPODetailWithIsDeleteTrue();

		ArrayList<CSO_SOASDetail> listSOASToday = sdcdModel.getSOASDataChangeDetail();
		if ( ! listSOASToday.isEmpty()) {
			csoModel.insertCSO_SOASDetail(listSOASToday);

			ArrayList<CSO_SOASDetail> listChange = csoModel.getCSO_SOASDetail();
			// PURCH_NO SOLD_TO PO_DATE
			// PURCH_NO + DIVISON
			// NEW_ITEM
			// PURCH_NO + MATERIAL + SOLD_TO + ORD_QTY
			// DELITEM
			// PURCH_NO + MATERIAL + SOLD_TO + ORD_QTY

			// ELSE
			// IF ORDER QTY NEED RECREATE <> ''
			// CANCEL PO ORDER QTY CHANGE
			// FLAG RECREATE
			// ELSE IF DUE_SUBMIT_DATE ( SOR DUE DATE )
			// ELSE
			// PO_DATE SHIPDATE LAB_NO CUST_MAT DUEGREIGE TEXT_LINE
			// CHANGE

			boolean bl_needUpdate = false;
			boolean bl_poQtyChange = false;
			String poTemp = "";
			String matNoTemp = "";
			String soldToTemp = "";
			BigDecimal poQtyTemp = null;
			String saleOrderTemp = "",poQtyTempStr = "";
			String saleLineTemp = "";
			Timestamp lastChangeOnTemp = null;
			String caseUpdateTemp = "";
			// loop
			InputPODetail beanSPD = new InputPODetail();
			for (int i = 0; i < listChange.size(); i ++ ) {
				// update in loop
				CSO_SOASDetail beanChange = listChange.get(i);
				String matNo = beanChange.getMATERIAL();
				String fieldCode = beanChange.getFIELD_CODE();
				String po = beanChange.getPURCH_NO();
//				String division = beanChange.getDIVISION() ;
				String soldTo = beanChange.getSOLD_TO();
				BigDecimal poQty = beanChange.getORD_QTY();
//				String labNo = beanChange.getLAB_NO() ;
				String saleOrder = beanChange.getSALES_ORDER_NO();
				String saleLine = beanChange.getDOC_ITEM();
//				String poDate = beanChange.get ;
//				Timestamp shipDate = beanChange.getSHIPDATE() ;
				Timestamp lastChangeOn = beanChange.getLASTCHANGED_ON();
//				String custMat = beanChange.getCUST_MAT() ;
				String caseUpdate = "";
//				Timestamp greigePlan = beanChange.getDUE_GREIGE() ;
				String poQtyStr = "";
//				caseUpdateTemp = "";
				if (poQty != null) {
					try {
						Double doublePOQty = poQty.doubleValue();
						poQtyStr = formatter.format(doublePOQty);
						poQtyStr = poQtyStr.replace(Config.C_COMMA, Config.C_BLANK);
					} catch (Exception ex) {
						poQtyStr = "";
					}
				}
//				String oldVal = beanChange.getOLD_VALUE();
				String newVal = beanChange.getNEW_VALUE();
				// PURCH_NO SOLD_TO PO_DATE
				if (caseUpdateTemp.equals(Config.C_BLANK)) {
					poTemp = po;
					matNoTemp = matNo;
					soldToTemp = soldTo;
					poQtyTemp = poQty;
					saleOrderTemp = saleOrder;
					saleLineTemp = saleLine;
					lastChangeOnTemp = lastChangeOn;
				} else if ((( ! poTemp.equals(po)) || ( ! lastChangeOnTemp.equals(lastChangeOn))
						|| ( ! matNoTemp.equals(matNo)) || (poQtyTemp != poQty) || ( ! soldToTemp.equals(soldTo))
						|| ( ! saleOrderTemp.equals(saleOrder)) || ( ! saleLineTemp.equals(saleLine)))) {
					// HANDLER OLD CASE
					if (caseUpdateTemp.equals(Config.C_CASE_POHEAD)) {
						ArrayList<InputPODetail> listForPOId = spdModel.getPODetailWithSOASPOHeaderDetail(poTemp,
								matNoTemp, poQtyTempStr, soldToTemp, Config.C_OPEN_STATUS);
						this.handlerCheckPOHeaderOrPOChange(listForPOId, beanSPD, bl_needUpdate, bl_poQtyChange);
					} else if (caseUpdateTemp.equals(Config.C_CASE_POLINE)) {
						ArrayList<InputPODetail> listForPOId = spdModel.getPODetailWithSOASPOItemDetail(poTemp,
								matNoTemp, poQtyTempStr, soldToTemp, Config.C_OPEN_STATUS);
						this.handlerCheckPOHeaderOrPOChange(listForPOId, beanSPD, bl_needUpdate, bl_poQtyChange);
					} else {

					}
					bl_poQtyChange = false;
					bl_needUpdate = false;
					// DO SOMETHING AND DONE CLEAR OLD VALUE
					beanSPD = new InputPODetail();
					// AFTER DONE
					caseUpdate = Config.C_BLANK;
				}

				if (fieldCode.equals(Config.C_PURCH_NO) || fieldCode.equals(Config.C_PO_DATE)
						|| fieldCode.equals(Config.C_SOLD_TO)) {
					bl_needUpdate = true;
					bl_poQtyChange = false;

					caseUpdate = Config.C_CASE_POHEAD;// HEADER ONLY PURCH_NO + SOLD_TO + SALE_ORDER_NO + DIVISON
					if (fieldCode.equals(Config.C_PURCH_NO)) {
						beanSPD.setPo(newVal);
						beanSPD.getUpdateFieldList().add(Config.sqlFieldPO);
					} else if (fieldCode.equals(Config.C_PO_DATE)) {
						beanSPD.setPoDate(newVal);
						beanSPD.getUpdateFieldList().add(Config.sqlFieldPODate);
					} else if (fieldCode.equals(Config.C_SOLD_TO)) {
						beanSPD.setCustomerNo(newVal);
						beanSPD.getUpdateFieldList().add(Config.sqlFieldCustomerNo);
					}
				} else if (fieldCode.equals(Config.C_SHIPDATE) || fieldCode.equals(Config.C_LAB_NO)
						|| fieldCode.equals(Config.C_CUST_MAT) || fieldCode.equals(Config.C_DUE_GREIGE)
						|| fieldCode.equals(Config.C_TEXT_LINE) || fieldCode.equals(Config.C_ORD_QTY)) {
					caseUpdate = Config.C_CASE_POLINE;
					bl_needUpdate = true;
					if (fieldCode.equals(Config.C_SHIPDATE)) { // SORDUEDATE ?
						beanSPD.setCustomerDue(newVal);
						beanSPD.getUpdateFieldList().add(Config.sqlFieldCustomerDue);
					} else if (fieldCode.equals(Config.C_ORD_QTY)) {
						beanSPD.setOrderQty(newVal.replace(Config.C_COMMA, Config.C_BLANK));
						beanSPD.getUpdateFieldList().add(Config.sqlFieldOrderQty);
						bl_poQtyChange = true;
					} else if (fieldCode.equals(Config.sqlFieldLabRef)) {
						beanSPD.setLabRef(newVal);
						beanSPD.getUpdateFieldList().add(Config.sqlFieldLabRef);
					} else if (fieldCode.equals(Config.C_CUST_MAT)) {
						beanSPD.setMaterialNoTWC(newVal);
						beanSPD.getUpdateFieldList().add(Config.sqlFieldMaterialNoTWC);
					} else if (fieldCode.equals(Config.C_DUE_GREIGE)) {
						beanSPD.setGreigePlan(newVal);
						beanSPD.getUpdateFieldList().add(Config.sqlFieldGreigePlan);
					} else if (fieldCode.equals(Config.C_TEXT_LINE)) {
						beanSPD.setProductionMemo(newVal);
						beanSPD.getUpdateFieldList().add(Config.sqlFieldProductionMemo);
					}
				} else if (fieldCode.equals(Config.C_NEW_ITEM)) {
					bl_needUpdate = false;
					caseUpdate = Config.C_CASE_REOPEN_N_CREATE;// HEADER ONLY PURCH_NO + SOLD_TO + SALE_ORDER_NO +
																// DIVISON
					// CHECK CLOSED DATASTATUS = 'X' AND REOPEN AGAIN
					bl_poQtyChange = false;
					InputPODetail beanAddCheck = new InputPODetail();
					beanAddCheck.setPo(po);
					beanAddCheck.setMaterialNo(matNo);
					beanAddCheck.setCustomerNo(soldTo);
					beanAddCheck.setOrderQty(poQtyStr);
					ArrayList<InputPODetail> listForRecreate = new ArrayList<>();
					listForRecreate = spdModel.getPODetailWithSOASPOItemDetail(po, matNo, poQtyStr, soldTo,
							Config.C_CLOSE_STATUS);

					if ( ! listForRecreate.isEmpty()) {
						InputPODetail beanCancel = listForRecreate.get(0);
						ArrayList<POManagementDetail> listLotIsInSap = new ArrayList<>();
						POManagementDetail beanPOM = new POManagementDetail();
						beanPOM.setPoId(beanCancel.getId());
						listLotIsInSap.add(beanPOM);
//						listLotIsInSap = pomModel.getPOManagementLotDetailByPOId(listLotIsInSap);
						boolean bl_isLotInSap = beanCancel.isLotInSap();
//						System.out.println("bl_isInSap "+bl_isLotInSap);
						if ( ! bl_isLotInSap) {
							ArrayList<POManagementDetail> poListTemp = new ArrayList<>();
							ArrayList<InputPODetail> poCreateList = new ArrayList<>();
							// CANCEL LOG
							POManagementDetail beanTempOne = new POManagementDetail();
							beanTempOne.setPoId(beanCancel.getId());
							beanTempOne.setPo(beanCancel.getPo());
							beanTempOne.setPoPuangId(0);
							beanTempOne.setDataStatus(Config.C_OPEN_STATUS);
							beanTempOne.setChangeBy(Config.C_SOMS);
							poListTemp.add(beanTempOne);

							InputPODetail beanTempTwo = new InputPODetail();
							beanTempTwo.setId(beanCancel.getId());
							poCreateList.add(beanTempTwo);
							// CANCEL PO POLINE MAIN

							if ( ! poListTemp.isEmpty()) {
								pomModel.handlerChangeSORPODetailWithDataStatus(poListTemp,
										Config.C_ACTION_TEXT_UPDATE_08);
							}
							if ( ! poListTemp.isEmpty()) {
								spdModel.updateDataStatusWithIdForSORPODetail(poListTemp);
							}
							this.tryCreateWithNewPOQty(poCreateList);
							// CREATE AGAIN
						}
					}
					beanSPD = new InputPODetail();
				}

				else if (fieldCode.equals(Config.C_DELITEM)) {
					caseUpdate = Config.C_CASE_DELITEM;
					bl_poQtyChange = false;
					bl_needUpdate = false;
					// check
					// SAP ?
					InputPODetail beanAddCheck = new InputPODetail();
					beanAddCheck.setPo(po);
					beanAddCheck.setMaterialNo(matNo);
					beanAddCheck.setCustomerNo(soldTo);
					beanAddCheck.setOrderQty(poQtyStr);

					ArrayList<InputPODetail> listForCancel = new ArrayList<>();

					listForCancel =
							spdModel.getPODetailWithSOASPOItemDetail(po, matNo, poQtyStr, soldTo, Config.C_OPEN_STATUS);
					// TODO Auto-generated method stub
					if ( ! listForCancel.isEmpty()) {
						InputPODetail beanCancel = listForCancel.get(0);

						ArrayList<POManagementDetail> listLotIsInSap = new ArrayList<>();
						POManagementDetail beanPOM = new POManagementDetail();
						beanPOM.setPoId(beanCancel.getId());
						listLotIsInSap.add(beanPOM);
						listLotIsInSap = pomModel.getPOManagementLotDetailByPOId(listLotIsInSap);
						boolean bl_isInSap = false;
						for (POManagementDetail beanCurrent : listLotIsInSap) {
							// 1. CHECK LOT ALREADY CREATE IN SAP
							if (beanCurrent.isInSap()) {
								bl_isInSap = true;
								break;
							}
						}
//						System.out.println("bl_isInSap "+bl_isInSap);
						if ( ! bl_isInSap) {
							POManagementDetail pd = new POManagementDetail();
							ArrayList<POManagementDetail> listForPOMM = new ArrayList<>();

							pd.setPoId(beanCancel.getId());
							pd.setPo(beanCancel.getPo());
							pd.setRemark("SOMS cancel PO");
							pd.setPoPuangId(beanCancel.getPoPuangId());
							pd.setChangeBy(Config.C_SOMS);
							listForPOMM.add(pd);
//							System.out.println(beanCancel.getId()+" "+ beanCancel.getPo());
							pomModel.cancelPOLine(listForPOMM, Config.C_CLOSE_STATUS,true);
						}
					}
					beanSPD = new InputPODetail();
				}
//				}
				else {
					caseUpdate = Config.C_CASE_POLINE;
					// DO NOTTHING
				}
//				if(po.equals("330195")) {
//					System.out.println(caseUpdateTemp+" "+beanSPD.getPo()+" "+beanSPD.getLabRef()+" "+beanSPD.getCustomerNo() );
//					System.out.println(poTemp+" "+matNoTemp+" "+poQtyStr+" "+soldToTemp);
//				}
				// DOLAST TIME
				if (i == listChange.size()-1) {
					if (caseUpdate.equals(Config.C_CASE_POHEAD)) {
						ArrayList<InputPODetail> listForPOId =
								spdModel.getPODetailWithSOASPOHeaderDetail(po, matNo, poQtyStr, soldTo, Config.C_OPEN_STATUS);
						this.handlerCheckPOHeaderOrPOChange(listForPOId, beanSPD, bl_needUpdate, bl_poQtyChange);
					} else if (caseUpdate.equals(Config.C_CASE_POLINE)) {
						ArrayList<InputPODetail> listForPOId = 
								spdModel.getPODetailWithSOASPOItemDetail(po, matNo, poQtyStr, soldTo, Config.C_OPEN_STATUS);
						this.handlerCheckPOHeaderOrPOChange(listForPOId, beanSPD, bl_needUpdate, bl_poQtyChange);
					} else {

					}
					bl_poQtyChange = false;
					bl_needUpdate = false;
					// DO SOMETHING AND DONE CLEAR OLD VALUE
					beanSPD = new InputPODetail();
					// AFTER DONE
					caseUpdate = Config.C_BLANK;
				}
//					 System.out.println(caseUpdate);
				poTemp = po;
				matNoTemp = matNo;
				soldToTemp = soldTo;
				poQtyTemp = poQty;
				saleOrderTemp = saleOrder;
				saleLineTemp = saleLine;
				lastChangeOnTemp = lastChangeOn;
				caseUpdateTemp = caseUpdate;
				poQtyTempStr = Config.C_BLANK;
				if (poQty != null) {
					try {
						Double doublePOQty = poQtyTemp.doubleValue();
						poQtyTempStr = formatter.format(doublePOQty);
						poQtyTempStr = poQtyTempStr.replace(Config.C_COMMA, Config.C_BLANK);
					} catch (Exception ex) {
						poQtyTempStr = "";
					}
				}
				// PURCH_NO + DIVISON
				// NEW_ITEM
				// PURCH_NO + MATERIAL + SOLD_TO + ORD_QTY
				// DELITEM
				// PURCH_NO + MATERIAL + SOLD_TO + ORD_QTY

				// ELSE
				// IF ORDER QTY NEED RECREATE <> ''
				// CANCEL PO ORDER QTY CHANGE
				// FLAG RECREATE
				// ELSE IF DUE_SUBMIT_DATE ( SOR DUE DATE )
				// ELSE
				// PO_DATE SHIPDATE LAB_NO CUST_MAT DUEGREIGE TEXT_LINE
				// CHANGE

				// 1. order qty need to destory and create lot again SET FLAG
			}
			// AFTER SET FLAG SELECT ALL FLAG PO

			// IF PO PUANG DESTROY

			// 1. order qty need to destory and create lot again

		}
		csoModel.updateCSO_SOASDetailByDataStatus();
	}

	private String removeLastChar(String s) {
		// returns the string after removing the last character
		return s.substring(0, s.length()
				-1);
	}

	// PO FORECAST
	@Override
	public void searchPOTempGroupOptionListForNull() {
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		ArrayList<InputGroupDetail> listAG = new ArrayList<>();
		List<String> articleList = new ArrayList<>();
		InputTempProdDetail bean;
		double orderQty_DB;
		double DBLotQtyMax = 0.00;
		double DBLotQtyMin = 0.00;
		String article = "";
		String colorType = "";

		String lotMinMax = "";
		String[] array;
		String no = "";
		String groupNo = "";
		String subGroup = "";

		String groupOptionsStr = "";
		int poId = 0;
		int poIdTmp = 0;
		ArrayList<InputTempProdDetail> list = this.getSORTempProdDetailGroupOptionsNull();
		for (int z = 0; z < list.size(); z ++ ) {
			listAG.clear();
			articleList.clear();
			bean = list.get(z);
			no = bean.getNo();
			poId = bean.getPoId();
			if (poIdTmp == 0 || poIdTmp != poId) {
				poIdTmp = poId;
				groupOptionsStr = "";
				orderQty_DB = bean.getDbProdQty();
				colorType = bean.getColorType();
				article = bean.getArticle();
				articleList.add(article);
				InputGroupDetail beanGroup = new InputGroupDetail();
				beanGroup.setArticleList(articleList);
				listAG.add(beanGroup);
				listAG = this.getArticleSubGroupDetailByArticleListNColor(listAG, colorType);
				for (InputGroupDetail beanTmp : listAG) {
					lotMinMax = beanTmp.getLotMinMax();
					DBLotQtyMax = beanTmp.getDbLotQtyMax();
					DBLotQtyMin = beanTmp.getDbLotQtyMin();
					array = lotMinMax.split("-");
					groupNo = beanTmp.getGroupNo();
					subGroup = beanTmp.getSubGroup();
					if (array.length == 1) {
						if (orderQty_DB == DBLotQtyMax) {
							groupOptionsStr = groupOptionsStr + groupNo + Config.C_COLON + subGroup + Config.C_COMMA;
						}
					} else {
						if (orderQty_DB >= DBLotQtyMin && orderQty_DB <= DBLotQtyMax) {
							groupOptionsStr = groupOptionsStr + groupNo + Config.C_COLON + subGroup + Config.C_COMMA;
						}
					}
				}
				if ( ! groupOptionsStr.equals(Config.C_BLANK)) {
					groupOptionsStr = this.removeLastChar(groupOptionsStr);
				}
			} else {
			}
			stpModel.updateGroupOptionForSORTempProd(no, groupOptionsStr);
		}
	}

	public String tryCreateWithNewPOQty(ArrayList<InputPODetail> poCreateList) {
		ProdCreatedDetailModel pcdModel = new ProdCreatedDetailModel(this.conType);
		PlanningProdModel ppModel = new PlanningProdModel(this.conType);
		ArrayList<InputPODetail> listTmp = new ArrayList<>();
		ArrayList<InputTempProdDetail> tmpList = new ArrayList<>();
//
		this.execUpsertRuleCalculated();
		this.execUpdateOrderQtyCal();
		ArrayList<InputPODetail> list = pcdModel.getPODetailWithPOId(poCreateList);
		for (int i1 = 0; i1 < list.size(); i1 ++ ) {
			listTmp.clear();
			tmpList.clear();
			InputPODetail bean = list.get(i1);
			listTmp.add(bean);
			tmpList = pcdModel.createTempLotPOFromSOR(listTmp);
			if ( ! tmpList.isEmpty()) {
				if (tmpList.get(0).getIconStatus().equals("I")) {
					ppModel.rePlanningLot();
				}
			}
		}
		return C_SYSTEM;
	}

	@Override
	public void handlerERPDataToWebApp()
	{
		ERPAtechModel erpaModel = new ERPAtechModel();
		DataFromSapModel dfsModel = new DataFromSapModel(conType);
		RollFromSapModel rfsModel = new RollFromSapModel(conType);
		
		ArrayList<DataFromSapDetail> prodList = erpaModel.getDataFromERPDetail();
		ArrayList<RollFromSapDetail> rollList = erpaModel.getRollFromERPDetail();
		// TODO Auto-generated method stub
		dfsModel.upsertDataFromSap(prodList);
		rfsModel.upsertRollFromSap(rollList); 
	}
}
