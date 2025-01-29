package dao.implement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Test.utilities.HandlerListLog;
import Test.utilities.SqlErrorHandler;
import dao.FacWorkDateDao;
import entities.ChangeSettingLogDetail;
import entities.InputFacHolidayDetail;
import entities.InputFacWorkDateDetail;
import entities.InputTempProdDetail;
import entities.MasterSettingChangeDetail;
import entities.WorkDateDetail;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.PlanningProdModel;
import model.master.ChangeSettingLogModel;
import model.master.FacHolidayModel;
import model.master.InputFacWorkDateModel;
import model.master.MasterSettingChangeModel;
import model.master.TEMPPlanningLotModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class FacWorkDateDaoImpl implements FacWorkDateDao {
	Config config = new Config();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String conType;
	private String message;
//	private String C_OPEN_STATUS = "O";
//	private String C_CLOSE_STATUS = "X";

	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdfFull = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	private String fromFacWorkDateMain = " "
			+ "  FROM (\r\n"
			+ "		SELECT a.[GroupNo]\r\n"
			+ "				,a.[SubGroup]\r\n"
			+ "				, FORMAT (c.WorkDate, 'MM/yyyy') as ColMonthYear \r\n"
			+ "				,DAY(EOMONTH(c.WorkDate)) AS DaysInMonth \r\n"
			+ "				,C.NormalWork\r\n"
			+ "				,CASE\r\n"
			+ "					when A.LotPerDay = 0 then CAST(1 AS BIT)\r\n"
			+ "					else CAST(0 AS BIT)\r\n"
			+ "					end as IsLotPerDayZero\r\n"
			+ "             ,a.LotPerDay as LotPerDaySubGroup\r\n"
			+ "             ,case \r\n"
			+ "						when C.NormalWork = 'X' then CAST( NULL AS VARCHAR ) \r\n"
			+ "						when  b.LotPerDay is not null then CAST(  b.LotPerDay AS VARCHAR )\r\n"
			+ "						else CAST(a.LotPerDayInWork AS VARCHAR )\r\n"
			+ "						end as LotPerDay"
			+ "				,C.WorkDate\r\n"
			+ "		FROM [PPMM].[dbo].[InputSubGroupDeail] as a \r\n"
			+ "		--CROSS JOIN [PPMM].[dbo].[InputDateRunning] as c \r\n"
			+ "		INNER JOIN [PPMM].[dbo].[GroupWorkDate] AS C ON A.GroupNo = C.GroupNo AND\r\n"
			+ "														A.SubGroup = C.SubGroup\r\n"
			+ "		left join [PPMM].[dbo].[InputFacWorkDate] as b on a.[GroupNo] = b.[GroupNo] and\r\n"
			+ "														  a.[SubGroup] = b.[SubGroup] and\r\n"
			+ "														  c.WorkDate = b.[StartDate] \r\n"
			+ "		left join [PPMM].[dbo].[InputFacHoliday] as d on c.WorkDate = d.[StartDate] and\r\n"
			+ "														 d.[DataStatus] = 'O'\r\n"
			+ "		where a.DataStatus = 'O' \r\n"
			+ "			 and ( c.WorkDate >= @BeginDate and \r\n"
			+ "                c.WorkDate <= @LastDate )\r\n"
			+ "		) as a \r\n";

	public FacWorkDateDaoImpl(Database database, String conType) {
		this.database = database;
		this.message = "";
		this.conType = conType;
	}

	@Override
	public ArrayList<InputFacHolidayDetail> cancelHolidayDate(ArrayList<InputFacHolidayDetail> poList) {
		String remarkAction = Config.C_ACTION_TEXT_DELETE_04;
		PlanningProdModel ppModal = new PlanningProdModel(this.conType);
		InputFacWorkDateModel ifwdModel = new InputFacWorkDateModel(this.conType);
		BackGroundJobModel model = new BackGroundJobModel(this.conType);
		FacHolidayModel fhModel = new FacHolidayModel(this.conType);
		String iconStatus = "";
		String systemStatus = "";

		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		String changeBy = "";
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeFacHolidayLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		HashMap<String, InputFacHolidayDetail> mapDateAndDataStatus = new HashMap<>();
		// GET NEW DATE
		ArrayList<InputFacHolidayDetail> listCompare = fhModel.getFacHolidayDetailByDateMaster(poList);
		for (InputFacHolidayDetail beanTmp : poList) {
			changeBy = beanTmp.getChangeBy();
			beanTmp.setDataStatus(Config.C_CLOSE_STATUS);
			if ( ! beanTmp.getStartDate().equals("")) {
				mapDateAndDataStatus.put(beanTmp.getStartDate(), beanTmp);
			}
		}
		String oldVal = "",newVal = "";
		for (InputFacHolidayDetail beanTmpOld : listCompare) {
			InputFacHolidayDetail beanTmpNew = mapDateAndDataStatus.get(beanTmpOld.getStartDate());
			newVal = beanTmpNew.getDataStatus();
			oldVal = beanTmpOld.getDataStatus(); 
			// Date
			if (beanTmpNew != null) {
				if ( ! oldVal.equals(newVal)) {
					listCSL = HandlerListLog.handlerListLog(listCSL, oldVal, newVal,
							mapMSC.get(Config.sqlFieldDataStatus), beanTmpOld.getNo(), changeBy,
							Config.sqlFieldDataStatus, remarkAction);
				}
				// Remark
				if ( ! beanTmpOld.getRemark().equals(beanTmpNew.getRemark())) {
					listCSL = HandlerListLog.handlerListLog(listCSL, beanTmpOld.getRemark(), beanTmpNew.getRemark(),
							mapMSC.get(Config.sqlFieldRemark), beanTmpOld.getNo(), changeBy, Config.sqlFieldRemark,
							remarkAction);
				}
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeFacHolidayLog,
					Config.sqlFieldFacHolidayId);
		}

		iconStatus = fhModel.updateFacHolidayDetail(poList);
		ifwdModel.updateInputFacWorkDateFromFacHoliday(poList, Config.C_OPEN_STATUS);
		// RECREATE DATE
		model.execUpsertGroupWorkDate();
		ppModal.rePlanningLot();
		// GET NEW DATE
		ArrayList<InputFacHolidayDetail> list = fhModel.getFacHolidayDetailByDate(poList);
		if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) {
			iconStatus = Config.C_SUC_ICON_STATUS;
			systemStatus = "Cancel Success.";
		} else {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = "Cancel Fail.";
		}
		if (list.size() == 0) {
			list = poList;
			list.get(0).setIconStatus(iconStatus);
			list.get(0).setSystemStatus(systemStatus);
		} else {
			list.get(0).setIconStatus(iconStatus);
			list.get(0).setSystemStatus(systemStatus);
		}
		return poList;
	}

	@Override
	public ArrayList<InputFacHolidayDetail> getCalendarDetail() {
		ArrayList<InputFacHolidayDetail> list = null;

		String declarePlan = ""
				+ " declare @CurDate date  = CONVERT(date, GETDATE(), 103) ;\r\n"
				+ "	declare @BeginDate date  = DATEADD(MONTH,-1,@CurDate)\r\n"
				+ " declare @LastDate date  = DATEADD(YEAR,1,@CurDate);\r\n";
		String sql = ""
				+ declarePlan
				+ " SELECT "
				+ "	b.[Id]\r\n"
				+ " ,[Title]\r\n"
				+ "	,[Date] AS [StartDate]\r\n"
				+ "	,B.Remark\r\n"
				+ "	,[DateName]	\r\n"
				+ " ,[ChangeDate]\r\n"
				+ " ,[ChangeBy]\r\n"
				+ "	,[DataStatus] \r\n"
				+ "	FROM [PPMM].[dbo].[InputDateRunning] as a\r\n"
				+ "	left join [PPMM].[dbo].[InputFacHoliday] as b on a.[Date] = b.[StartDate]\r\n"
				+ "	where ( a.[DateName] = 'Sunday' and a.[Date] >= @BeginDate ) or \r\n"
				+ "       ( b.[DataStatus] = 'O' and   [StartDate] >= CONVERT(DATE,GETDATE() ))  "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFacHolidayDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputFacHolidayDetail> getCalendarDetail(ArrayList<InputFacHolidayDetail> poList) {
		ArrayList<InputFacHolidayDetail> list = null;
		InputFacHolidayDetail bean = poList.get(0);
		String startDate = bean.getStartDate();
		String endDate = bean.getEndDate();
		String declarePlan = ""
//				+ " declare @CurDate date  = CONVERT(date, GETDATE(), 103) ;\r\n"
				+ "	declare @BeginDate date  = '"
				+ startDate
				+ "' ;\r\n"
				+ " declare @LastDate date  = '"
				+ endDate
				+ "';\r\n";
		String sql = ""
				+ declarePlan
				+ " SELECT "
				+ "	b.[Id]\r\n"
				+ " ,[Title]\r\n"
				+ "	,[Date] AS [StartDate]\r\n"
				+ "	,B.Remark\r\n"
				+ "	,[DateName]	\r\n"
				+ " ,[ChangeDate]\r\n"
				+ " ,[ChangeBy]\r\n"
				+ "	,[DataStatus] \r\n"
				+ "	FROM [PPMM].[dbo].[InputDateRunning] as a\r\n"
				+ "	left join [PPMM].[dbo].[InputFacHoliday] as b on a.[Date] = b.[StartDate]\r\n"
				+ "	where ( a.[DateName] = 'Sunday' and a.[Date] >= @BeginDate and  a.[Date] <= @LastDate ) or \r\n"
				+ "       ( b.[DataStatus] = 'O' and ( b.[StartDate] >= @BeginDate and b.[StartDate] <= @LastDate ) ) \r\n "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFacHolidayDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputFacWorkDateDetail> getFacWorkDateDetail() {
		ArrayList<InputFacWorkDateDetail> list = null;
		String sql = ""
				+ " \r\n"
				+ " SELECT *\r\n"
				+ " FROM\r\n"
				+ " (\r\n"
				+ "  SELECT  a.[GroupNo]\r\n"
				+ "       ,a.[SubGroup] \r\n"
				+ "	   ,a.NormalWork \r\n"
				+ "	   ,ColMonthYear\r\n"
				+ "	   ,DaysInMonth \r\n"
				+ "		--,DAY(a.[Date]) AS rownum\r\n"
				+ "	    ,ROW_NUMBER() OVER(PARTITION BY a.[GroupNo] ,a.[SubGroup]   ORDER BY a.[GroupNo] ,a.[SubGroup],a.[WorkDate]   ) as rownum \r\n"
				+ this.fromFacWorkDateMain
				+ " ) AS A PIVOT(max(NormalWork) FOR a.rownum \r\n"
				+ "		IN( [1], [2], [3], [4], [5] ,[6], [7], [8], [9], [10] ,\r\n"
				+ "			[11], [12], [13], [14], [15] ,[16], [17], [18], [19], [20] ,\r\n"
				+ "			[21], [22], [23], [24], [25] ,[26], [27], [28], [29], [30] ,\r\n"
				+ "			[31], [32]  \r\n"
				+ "		) ) AS PivotTable; \r\n ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFacWorkDateDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputFacWorkDateDetail> getFacWorkDateDetailByDate(ArrayList<InputFacWorkDateDetail> poList) {
		ArrayList<InputFacWorkDateDetail> list = null;
		InputFacWorkDateDetail bean = poList.get(0);
		String monthYear = bean.getColMonthYear();
		String firstDay = "1/" + monthYear; 
		String declarePlan = ""
				+ " declare @BeginDate date  = CONVERT(date, '" + firstDay + "', 103) ;\r\n"
				+ " declare @LastDate date  = EOMONTH(@BeginDate) ;\r\n"; 
		String sql = ""
				+ declarePlan
				+ " \r\n"
				+ " SELECT *\r\n"
				+ "FROM\r\n"
				+ "(\r\n"
				+ "  SELECT  a.[GroupNo]\r\n"
				+ "       ,a.[SubGroup] \r\n"
				+ "	   ,a.NormalWork \r\n"
				+ "	   ,ColMonthYear\r\n"
				+ "	   ,DaysInMonth \r\n" 
				+ "	    ,ROW_NUMBER() OVER(PARTITION BY a.[GroupNo] ,a.[SubGroup]   ORDER BY a.[GroupNo] ,a.[SubGroup],a.[WorkDate]   ) as rownum \r\n"
				+ this.fromFacWorkDateMain
				+ ") AS A PIVOT(max(NormalWork) FOR a.rownum \r\n"
				+ "		IN( [1], [2], [3], [4], [5] ,[6], [7], [8], [9], [10] ,\r\n"
				+ "			[11], [12], [13], [14], [15] ,[16], [17], [18], [19], [20] ,\r\n"
				+ "			[21], [22], [23], [24], [25] ,[26], [27], [28], [29], [30] ,\r\n"
				+ "			[31], [32]  \r\n"
				+ "		) ) AS PivotTable; \r\n ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFacWorkDateDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputFacWorkDateDetail> getLotPerDayFromFacWorkDateDetailByDate(
			ArrayList<InputFacWorkDateDetail> poList) {
		ArrayList<InputFacWorkDateDetail> list = null;
		InputFacWorkDateDetail bean = poList.get(0);
		String monthYear = bean.getColMonthYear();
		String firstDay = "1/" + monthYear; 
		String declarePlan = ""
				+ " declare @BeginDate date  = CONVERT(date, '" + firstDay + "', 103) ;\r\n"
				+ " declare @LastDate date  = EOMONTH(@BeginDate) ;\r\n"; 
		String sql = ""
				+ declarePlan
				+ " \r\n" 
				+ "	SELECT \r\n"
				+ "  a.[GroupNo]\r\n"
				+ "	,a.[SubGroup]  \r\n"
				+ "	,ColMonthYear\r\n"
				+ "	,DaysInMonth   \r\n"
				+ "	,A.IsLotPerDayZero\r\n"
				+ "	,A.LotPerDaySubGroup\r\n"
				+ "	,[1] AS L1 , [2] as L2 , [3] as L3 , [4] as L4 , [5] AS L5 , [6] as L6 , [7] as L7 , [8] as L8 , [9] as L9 , [10] AS L10 , \r\n"
				+ "	 [11] as L11 , [12] as L12 , [13] as L13 , [14] as L14 , [15] AS L15 , [16] as L16 , [17] as L17 , [18] as L18 , [19] as L19 , [20] AS L20 , \r\n"
				+ "	 [21] as L21 , [22] as L22 , [23] as L23 , [24] as L24 , [25] AS L25 , [26] as L26 , [27] as L27 , [28] as L28 , [29] as L29 , [30] AS L30 , \r\n"
				+ "	 [31] as L31 , [32] AS L32\r\n"
				+ " FROM ( \r\n"
				+ " 	SELECT *\r\n"
				+ " 	FROM (\r\n"
				+ "  		SELECT\r\n"
				+ "			a.[GroupNo]\r\n"
				+ "    		,a.[SubGroup] \r\n"
				+ "	   		,a.LotPerDay \r\n"
				+ "			,A.IsLotPerDayZero\r\n"
				+ "			,A.LotPerDaySubGroup\r\n"
				+ "	   		,ColMonthYear\r\n"
				+ "	   		,DaysInMonth \r\n"
				+ "	   		,ROW_NUMBER() OVER(PARTITION BY a.[GroupNo] ,a.[SubGroup]   ORDER BY a.[GroupNo] ,a.[SubGroup],a.[WorkDate]   ) as rownum \r\n"
				+ this.fromFacWorkDateMain
				+ "		) AS A \r\n"
				+ "		PIVOT(\r\n"
				+ "			max(LotPerDay) \r\n"
				+ "			FOR rownum \r\n"
				+ "				IN	(	[1], [2], [3], [4], [5] ,[6], [7], [8], [9], [10] ,\r\n"
				+ "					[11], [12], [13], [14], [15] ,[16], [17], [18], [19], [20] ,\r\n"
				+ "					[21], [22], [23], [24], [25] ,[26], [27], [28], [29], [30] ,\r\n"
				+ "					[31], [32]  \r\n"
				+ "			) \r\n"
				+ "		) AS PivotTable \r\n"
				+ " ) as a ; \r\n "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFacWorkDateDetail(map));
		}
		return list;
	}

	public String getMessage() {
		return this.message;
	}
 
	@Override
	public ArrayList<InputFacHolidayDetail> saveHolidayDate(ArrayList<InputFacHolidayDetail> poList) {
		String remarkAction = Config.C_ACTION_TEXT_UPDATE_12;
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);

		InputFacWorkDateModel ifwdModel = new InputFacWorkDateModel(this.conType);
		FacHolidayModel fhModel = new FacHolidayModel(this.conType);
		BackGroundJobModel model = new BackGroundJobModel(this.conType);
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		PlanningProdModel ppModal = new PlanningProdModel(this.conType);
		InputFacHolidayDetail bean = new InputFacHolidayDetail();
		String startDate = "";
		String iconStatus = "";
		String systemStatus = "";
		String changeBy = "";
		ArrayList<InputFacWorkDateDetail> listFWD = new ArrayList<>();
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		bean = poList.get(0);
		startDate = bean.getStartDate();

		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeFacHolidayLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		HashMap<String, InputFacHolidayDetail> mapDateAndDataStatus = new HashMap<>();
		// GET NEW DATE
		ArrayList<InputFacHolidayDetail> listCompare = fhModel.getFacHolidayDetailByDateMaster(poList);
		for (InputFacHolidayDetail beanTmp : poList) {
			changeBy = beanTmp.getChangeBy();
			beanTmp.setDataStatus(Config.C_OPEN_STATUS);
			if ( ! beanTmp.getStartDate().equals("")) {
				mapDateAndDataStatus.put(beanTmp.getStartDate(), beanTmp);
			}
		}
		String oldVal = "",newVal = "";
		if ( ! listCompare.isEmpty()) {
			for (InputFacHolidayDetail beanTmpOld : listCompare) {
				InputFacHolidayDetail beanTmpNew = mapDateAndDataStatus.get(beanTmpOld.getStartDate());
				newVal = beanTmpNew.getDataStatus();
				oldVal = beanTmpOld.getDataStatus();
				newVal = Config.C_OPEN_STATUS;
				// else {
				// }
				// Date
				if (beanTmpNew != null) {
//					if ( ! oldVal.equals(newVal)) {
//						ChangeSettingLogDetail beanObj = new ChangeSettingLogDetail();
						listCSL = HandlerListLog.handlerListLog(listCSL, oldVal, newVal,
								mapMSC.get(Config.sqlFieldDataStatus), beanTmpOld.getNo(), changeBy,
								Config.sqlFieldDataStatus, remarkAction);
//					}
					// Remark
//					if ( ! beanTmpOld.getRemark().equals(beanTmpNew.getRemark())) {
						listCSL = HandlerListLog.handlerListLog(listCSL, beanTmpOld.getRemark(), beanTmpNew.getRemark(),
								mapMSC.get(Config.sqlFieldRemark), beanTmpOld.getNo(), changeBy, Config.sqlFieldRemark,
								remarkAction);
//					}
				}
			}
		} else {

		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeFacHolidayLog,
					Config.sqlFieldFacHolidayId);
		}

		iconStatus = fhModel.upsertFacHolidayDetailByStartDate(poList);
		ifwdModel.updateInputFacWorkDateFromFacHoliday(poList, Config.C_CLOSE_STATUS);
		// RECREATE DATE
		model.execUpsertGroupWorkDate();
		// SET NULL PROD WITH DATE
		InputFacWorkDateDetail beanFWD = new InputFacWorkDateDetail();
		beanFWD.setDateClickedStr(startDate);
		listFWD.add(beanFWD);

		ArrayList<InputTempProdDetail> listTemp = ppModal.getPlanProdFromCalendar(listFWD);
		if (listTemp.size() > 0) {
			tplModel.updateCancelDateByIdForTEMPPlanningLot(listTemp);
		}
		ppModal.rePlanningLot();
		// GET NEW DATE
		ArrayList<InputFacHolidayDetail> list = fhModel.getFacHolidayDetailByDate(poList);
		if (list.size() == 0) {
			list = poList;
			list.get(0).setIconStatus(iconStatus);
			list.get(0).setSystemStatus(systemStatus);
		} else {
			list.get(0).setIconStatus(iconStatus);
			list.get(0).setSystemStatus(systemStatus);
		}
		return list;
	}

	@Override
	public ArrayList<InputFacWorkDateDetail> updateFacWorkDetail(ArrayList<InputFacWorkDateDetail> poList) {
		// TODO Auto-generated method stub
		String remarkAction = Config.C_ACTION_TEXT_UPDATE_12;
		PlanningProdModel ppModal = new PlanningProdModel(this.conType);
		InputFacWorkDateModel ifwdModel = new InputFacWorkDateModel(this.conType);
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		BackGroundJobModel model = new BackGroundJobModel(this.conType);

//		GroupWorkDateModel gwdModel = new GroupWorkDateModel(this.conType);
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();

		DecimalFormat df = new DecimalFormat("00");
		HashMap<String, InputFacWorkDateDetail> mapDateAndDataStatus = new HashMap<>();
		String groupNo = "";
		String subGroup = "";
		@SuppressWarnings("unused") String normalStatus = "";
		String dataStatus = "";
		String startDate = "";
		String colMonthYear = "";
		String iconStatus = "";
		String systemStatus = "";
		String changeBy = "";
		ArrayList<InputFacWorkDateDetail> listFWD = new ArrayList<>();
		ArrayList<InputFacWorkDateDetail> listAll = new ArrayList<>();
		ArrayList<InputFacWorkDateDetail> listDifData = new ArrayList<>();
		InputFacWorkDateDetail bean = null;
		int j = 0;
		int totalDay = 0; 
		for (int i = 0; i < poList.size(); i ++ ) {
			bean = poList.get(i);
			groupNo = bean.getGroupNo();
			subGroup = bean.getSubGroup();
			colMonthYear = bean.getColMonthYear();
			totalDay = Integer.parseInt(bean.getTotalDay());
			changeBy = bean.getChangeBy();
			for (j = 1; j <= totalDay; j ++ ) {
				String pad = df.format(j);
				startDate = pad + "/" + colMonthYear;
				dataStatus = ifwdModel.getDataStatusByDate(bean, j); 
				InputFacWorkDateDetail beanFWD = new InputFacWorkDateDetail();
				beanFWD.setDateClickedStr(startDate);
				beanFWD.setGroupNo(groupNo);
				beanFWD.setSubGroup(subGroup);
				beanFWD.setDataStatus(dataStatus);
				beanFWD.setTotalDay(bean.getTotalDay());
				beanFWD.setChangeBy(changeBy);
				beanFWD.setColMonthYear(colMonthYear);
				beanFWD.setStartDate(startDate);
				if (dataStatus.equals("X")) {
					listFWD.add(beanFWD);
				}
				listAll.add(beanFWD);
				if (true) {
					mapDateAndDataStatus.put(groupNo
							+Config.C_COLON
							+subGroup
							+Config.C_COLON
							+startDate, beanFWD);
				}
			}
		}
		// --------------------------------------------LOG----------------------------------------------
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeFacWorkDateLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		ArrayList<WorkDateDetail> listOld = ifwdModel.getInputFacWorkDateDetailForHandlerLog(listAll);
		String oldVal = "",newVal = "";
		for (int i = 0; i < listOld.size(); i ++ ) {
			WorkDateDetail beanTmpOld = listOld.get(i);
			InputFacWorkDateDetail beanTmpNew = mapDateAndDataStatus.get(beanTmpOld.getGroupNo()
					+Config.C_COLON
					+beanTmpOld.getSubGroup()
					+Config.C_COLON
					+beanTmpOld.getWorkDate());
			// Date
			if (beanTmpNew == null) {
//				listDifData.add(beanTmpNew);
			} else {
				newVal = beanTmpNew.getDataStatus();
				oldVal = beanTmpOld.getDataStatus();
				if ( ! oldVal.equals("") && ! oldVal.equals(newVal)) {
					listDifData.add(beanTmpNew);
					listCSL = HandlerListLog.handlerListLog(listCSL, oldVal, newVal,
							mapMSC.get(Config.sqlFieldDataStatus), Integer.toString(beanTmpOld.getId()), changeBy,
							Config.sqlFieldDataStatus, remarkAction);
				}
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeFacWorkDateLog,
					Config.sqlFieldFacWorkDateId);
		}
		// --------------------------------------------LOG----------------------------------------------
		iconStatus = ifwdModel.upsertInputFacWorkDetail(listDifData);
		model.execUpsertGroupWorkDate();
		// SET NULL PROD WITH DATE
		if (listFWD.size() > 0) {
			ArrayList<InputTempProdDetail> list = ppModal.getPlanProdFromDate(listFWD);
			if (list.size() > 0) {
				tplModel.updateCancelDateByIdForTEMPPlanningLot(list);
			}
		}
		ppModal.rePlanningLot();
		if (iconStatus.equals(Config.C_ERR_ICON_STATUS)) {
			systemStatus = Config.C_ERROR_TEXT;
		}
		bean = new InputFacWorkDateDetail();
		bean.setIconStatus(iconStatus);
		bean.setSystemStatus(systemStatus);
		poList.clear();
		poList.add(bean);
		return poList;
	}

	@Override
	public ArrayList<InputFacWorkDateDetail> updateLotPerDayForFacWorkDetail(ArrayList<InputFacWorkDateDetail> poList) {
		// TODO Auto-generated method stub
		String remarkAction = Config.C_ACTION_TEXT_UPDATE_13;
		PlanningProdModel ppModal = new PlanningProdModel(this.conType);
		InputFacWorkDateModel ifwdModel = new InputFacWorkDateModel(this.conType);
//		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		BackGroundJobModel model = new BackGroundJobModel(this.conType);
//		GroupWorkDateModel gwdModel = new GroupWorkDateModel(this.conType);
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);

		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("00");
		HashMap<String, InputFacWorkDateDetail> mapDateAndDataStatus = new HashMap<>();
		String groupNo = "";
		String subGroup = "";
		String lotPerDay = "";
		String startDate = "";
		String colMonthYear = "";
		String iconStatus = "";
//		String systemStatus = "";
		String dataStatus = "";
		String changeBy = "";
		ArrayList<InputFacWorkDateDetail> listFWD = new ArrayList<>();
		ArrayList<InputFacWorkDateDetail> listDifData = new ArrayList<>();
		ArrayList<InputFacWorkDateDetail> listAll = new ArrayList<>();
		InputFacWorkDateDetail bean = null;
		int j = 0;
		int totalDay = 0;
		for (int i = 0; i < poList.size(); i ++ ) {
			bean = poList.get(i);
			groupNo = bean.getGroupNo();
			subGroup = bean.getSubGroup();
			colMonthYear = bean.getColMonthYear();
			totalDay = Integer.parseInt(bean.getTotalDay());
			changeBy = bean.getChangeBy();
			for (j = 1; j <= totalDay; j ++ ) {
				String pad = df.format(j);
				startDate = pad + "/" + colMonthYear;
//				dataStatus = ifwdModel.getDataStatusByDate(bean,j);
				lotPerDay = ifwdModel.getLotPerDayByDate(bean, j);
				InputFacWorkDateDetail beanFWD = new InputFacWorkDateDetail();
				beanFWD.setDateClickedStr(startDate);
				beanFWD.setGroupNo(groupNo);
				beanFWD.setSubGroup(subGroup);
				beanFWD.setDataStatus(Config.C_OPEN_STATUS); // O เพราะว่า แก้ไขได้เฉพาะค่าที่เปิดช่องอยู่
				beanFWD.setLotPerDay(lotPerDay);
				beanFWD.setTotalDay(bean.getTotalDay());
				beanFWD.setChangeBy(changeBy);
				beanFWD.setColMonthYear(colMonthYear);
				beanFWD.setStartDate(startDate);
				if (dataStatus.equals(Config.C_CLOSE_STATUS)) {
					listFWD.add(beanFWD);
				}
				listAll.add(beanFWD);
				if (true) {
					mapDateAndDataStatus.put(groupNo
							+Config.C_COLON
							+subGroup
							+Config.C_COLON
							+startDate, beanFWD);
				}
			}
		}
//		ifwdModel.handlerChangeFacWorkDateLogForLotPerDay(poList, remarkAction);

		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeFacWorkDateLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		ArrayList<WorkDateDetail> listOld = ifwdModel.getInputFacWorkDateDetailForHandlerLog(listAll);
		for (int i = 0; i < listOld.size(); i ++ ) {
			WorkDateDetail beanTmpOld = listOld.get(i);
			InputFacWorkDateDetail beanTmpNew = mapDateAndDataStatus
					.get(beanTmpOld.getGroupNo()+Config.C_COLON+beanTmpOld.getSubGroup()+Config.C_COLON+beanTmpOld.getWorkDate());
			int oldId = beanTmpOld.getId();
			String oldLotPerDayInWork = beanTmpOld.getLotPerDayInWork();
			// Remark
			if (beanTmpNew == null) {
//				listDifData.add(beanTmpNew);
			} else {
				String newLotPerDay = beanTmpNew.getLotPerDay();
 				if ( ! oldLotPerDayInWork.equals("") && // NO WORK ( NO DATA ) = '' WHEN SELECT
						! newLotPerDay.equals("") && // NO WORK ( NO DATA ) = '' WHEN SELECT
						! oldLotPerDayInWork.equals(newLotPerDay)) {
					listDifData.add(beanTmpNew);
					listCSL = HandlerListLog.handlerListLog(listCSL, oldLotPerDayInWork, newLotPerDay,
							mapMSC.get(Config.sqlFieldLotPerDay), Integer.toString(oldId), changeBy, Config.sqlFieldLotPerDay,
							remarkAction);
				}
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeFacWorkDateLog,
					Config.sqlFieldFacWorkDateId);
		}
		iconStatus = ifwdModel.upsertInputFacWorkDetailForLotPerDay(listDifData);
		model.execUpsertGroupWorkDate();
		ppModal.rePlanningLot();
		if (iconStatus.equals(Config.C_ERR_ICON_STATUS)) {
//			systemStatus = Config.C_ERROR_TEXT;
		}
		bean = new InputFacWorkDateDetail();
		bean.setIconStatus(iconStatus);
		bean.setSystemStatus(SqlErrorHandler.handlerSqlErrorText(iconStatus));
		poList.clear();
		poList.add(bean);
		return poList;
	}
}
