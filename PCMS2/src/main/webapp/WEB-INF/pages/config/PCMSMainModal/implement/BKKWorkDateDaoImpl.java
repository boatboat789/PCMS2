package dao.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Test.utilities.HandlerListLog;
import dao.BKKWorkDateDao;
import entities.ChangeSettingLogDetail;
import entities.InputBKKHolidayDetail;
import entities.MasterSettingChangeDetail;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.master.BKKHolidayModel;
import model.master.ChangeSettingLogModel;
import model.master.MasterSettingChangeModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class BKKWorkDateDaoImpl implements BKKWorkDateDao {
	@SuppressWarnings("unused")
	private Config config = new Config();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	private String conType;
//	private String C_OPEN_STATUS = "O";
//	private String C_CLOSE_STATUS = "X";

	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdfFull = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public BKKWorkDateDaoImpl(Database database, String conType) {
		this.database = database;
		this.conType = conType;
		this.message = "";
	}

	@Override
	public ArrayList<InputBKKHolidayDetail> cancelHolidayDate(ArrayList<InputBKKHolidayDetail> poList) {
		String remarkAction = Config.C_ACTION_TEXT_DELETE_03;
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		BKKHolidayModel bkkHModel = new BKKHolidayModel(this.conType);
		HashMap<String, InputBKKHolidayDetail> mapDateAndDataStatus = new HashMap<>();
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		HashMap<String, String> mapMSC = new HashMap<>();
		String iconStatus = "";
		String systemStatus = "";

		String changeBy = "";
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeBKKHolidayLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		ArrayList<InputBKKHolidayDetail> listCompare = bkkHModel.getBKKHolidayDetailByDateMaster(poList);
		for (InputBKKHolidayDetail beanTmp : poList) {
			changeBy = beanTmp.getChangeBy();
			beanTmp.setDataStatus(Config.C_CLOSE_STATUS);
			if ( ! beanTmp.getStartDate().equals("")) {
				mapDateAndDataStatus.put(beanTmp.getStartDate(), beanTmp);
			}
		}
		String oldVal = "",newVal = "";
		for (InputBKKHolidayDetail beanTmpOld : listCompare) {
			InputBKKHolidayDetail beanTmpNew = mapDateAndDataStatus.get(beanTmpOld.getStartDate());
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
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeBKKHolidayLog,
					Config.sqlFieldBKKHolidayId);
		}

		iconStatus = bkkHModel.upsertBKKHolidayDetailByStartDate(poList, Config.C_CLOSE_STATUS);
		bgjModel.execUpsertBKKWorkDate();
		// RECREATE DATE
		ArrayList<InputBKKHolidayDetail> list = bkkHModel.getBKKHolidayDetailByDate(poList);
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
	public ArrayList<InputBKKHolidayDetail> getCalendarDetail() {
		ArrayList<InputBKKHolidayDetail> list = null;

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
				+ "	left join [PPMM].[dbo].[InputBKKHoliday]  as b on a.[Date] = b.[StartDate] AND b.[DataStatus] = 'O'\r\n"
				+ "	where ( ( a.[DateName] = 'Saturday' or  a.[DateName] = 'Sunday') and a.[Date] >= @BeginDate ) or \r\n"
				+ "       ( [StartDate] >= CONVERT(DATE,GETDATE() ))  ";
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genBKKHolidayDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputBKKHolidayDetail> getCalendarDetail(ArrayList<InputBKKHolidayDetail> poList) {
		ArrayList<InputBKKHolidayDetail> list = null;
		InputBKKHolidayDetail bean = poList.get(0);
		String startDate = bean.getStartDate();
		String endDate = bean.getEndDate();
		String declarePlan = ""
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
				+ "	left join [PPMM].[dbo].[InputBKKHoliday]  as b on a.[Date] = b.[StartDate] AND b.[DataStatus] = 'O'\r\n"
				+ "	where ( ( a.[DateName] = 'Saturday' or  a.[DateName] = 'Sunday') and a.[Date] >= @BeginDate and  a.[Date] <= @LastDate ) or \r\n"
				+ "       ( ( b.[StartDate] >= @BeginDate and b.[StartDate] <= @LastDate ) ) \r\n ";
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genBKKHolidayDetail(map));
		}
		return list;
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<InputBKKHolidayDetail> saveHolidayDate(ArrayList<InputBKKHolidayDetail> poList) {
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		String remarkAction = Config.C_ACTION_TEXT_UPDATE_11;
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		BKKHolidayModel bkkHModel = new BKKHolidayModel(this.conType);
		HashMap<String, InputBKKHolidayDetail> mapDateAndDataStatus = new HashMap<>();
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		HashMap<String, String> mapMSC = new HashMap<>();
		String iconStatus = "";
		String systemStatus = "";
		String changeBy = "";
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeBKKHolidayLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		ArrayList<InputBKKHolidayDetail> listCompare = bkkHModel.getBKKHolidayDetailByDateMaster(poList);
		for (InputBKKHolidayDetail beanTmp : poList) {
			changeBy = beanTmp.getChangeBy();
			beanTmp.setDataStatus(Config.C_OPEN_STATUS);
			if ( ! beanTmp.getStartDate().equals("")) {
				mapDateAndDataStatus.put(beanTmp.getStartDate(), beanTmp);
//				mapDateAndDataStatus.put(beanTmp.getStartDate(), beanTmp.getRemark());
			}
		}
		String oldVal = "",newVal = "";
		for (InputBKKHolidayDetail beanTmpOld : listCompare) {
			InputBKKHolidayDetail beanTmpNew = mapDateAndDataStatus.get(beanTmpOld.getStartDate());
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
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeBKKHolidayLog,
					Config.sqlFieldBKKHolidayId);
		}

		iconStatus = bkkHModel.upsertBKKHolidayDetailByStartDate(poList, Config.C_OPEN_STATUS);
		bgjModel.execUpsertBKKWorkDate();
		if (iconStatus.equals(Config.C_ERR_ICON_STATUS)) {
			systemStatus = Config.C_ERROR_TEXT;
		}

		// GET NEW DATE
		ArrayList<InputBKKHolidayDetail> list = bkkHModel.getBKKHolidayDetailByDate(poList);
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

}
