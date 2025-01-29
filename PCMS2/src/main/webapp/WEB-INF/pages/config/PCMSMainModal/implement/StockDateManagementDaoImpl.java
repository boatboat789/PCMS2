package dao.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Test.utilities.HandlerListLog;
import Test.utilities.SqlErrorHandler;
import dao.StockDateManagementDao;
import entities.ChangeSettingLogDetail;
import entities.InputStockCustomerDateDetail;
import entities.MasterSettingChangeDetail;
import model.BeanCreateModel;
import model.master.ChangeSettingLogModel;
import model.master.MasterSettingChangeModel;
import model.master.StockCustomerDateModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class StockDateManagementDaoImpl implements StockDateManagementDao {
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;

	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	private String conType;

	public StockDateManagementDaoImpl(Database database2, String conType) {
		this.database = database2;
		this.message = "";
		this.conType = conType;
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<InputStockCustomerDateDetail> getStockCustomerDateDetail() {
		ArrayList<InputStockCustomerDateDetail> list = null;
		String sql = ""
				+ " SELECT \r\n"
				+ "    scd.[Id] \r\n"
				+ "   ,cd.CustomerNo\r\n"
				+ "	  ,cd.CustomerName\r\n"
				+ "	  ,cd.CustomerShortName\r\n"
				+ "   ,scd.[DayOfMonth]\r\n	"
				+ "   ,scd.[StockReceive] \r\n"     
				+ "   ,scd.[StockRemark]\r\n"    
				+ "   ,scd.[ChangeDate]\r\n"
				+ "   ,scd.[ChangeBy]\r\n"
				+ "   ,scd.[CreateDate]	\r\n"
				+ "  FROM  [PCMS].[dbo].[CustomerDetail]  as cd   \r\n"
				+ "  left join (\r\n"
				+ "		select  RIGHT('00000'+ CONVERT(VARCHAR, [CustomerNo]),10)  as CustomerNo \r\n"
				+ "		, a.StockMonth\r\n"
				+ "		, a.StockReceive\r\n"
				+ "		, a.StockRemark\r\n"
				+ "		, a.StockYear\r\n"
				+ "		, a.DayOfMonth\r\n"
				+ "		, a.Id\r\n"
				+ "		, a.ChangeDate\r\n"
				+ "		, a.ChangeBy\r\n"
				+ "		, a.CreateDate\r\n"
				+ "		from [PPMM].[dbo].[StockCustomerDate] as a\r\n"
				+ "  )  as scd on scd.CustomerNo = cd.CustomerNo \r\n"
				+ " order by cd.CustomerNo\r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genStockCustomerDateDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputStockCustomerDateDetail> upsertStockCustomerDateDetail(
			ArrayList<InputStockCustomerDateDetail> poList) {
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		StockCustomerDateModel scdModel = new StockCustomerDateModel(this.conType);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String remarkAction = Config.C_ACTION_TEXT_UPDATE_11;
		HashMap<String, InputStockCustomerDateDetail> mapDateAndDataStatus = new HashMap<>();
		String changeBy = "";
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		ArrayList<InputStockCustomerDateDetail> listForSearch = new ArrayList<>();
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeStockCustomerDateLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		for (int i = 0; i < poList.size(); i ++ ) {
			InputStockCustomerDateDetail beanTmp = poList.get(i);
			changeBy = beanTmp.getChangeBy();
			String customerNoAfterPad = beanTmp.getCustomerNo().replaceFirst("^0+(?!$)", Config.C_BLANK);
			if ( ! beanTmp.getCustomerNo().equals(Config.C_BLANK)) {
				mapDateAndDataStatus.put(customerNoAfterPad, beanTmp);
			}
			InputStockCustomerDateDetail beanForSearch = new InputStockCustomerDateDetail();
			beanForSearch.setCustomerNo(customerNoAfterPad);
			listForSearch.add(beanForSearch);
		}
		ArrayList<InputStockCustomerDateDetail> listCompare = scdModel.getStockCustomerDateMasterDetail(listForSearch);
		for (int i = 0; i < listCompare.size(); i ++ ) {
			InputStockCustomerDateDetail beanTmpOld = listCompare.get(i);
			InputStockCustomerDateDetail beanTmpNew = mapDateAndDataStatus.get(beanTmpOld.getCustomerNo());
			String id = Integer.toString(beanTmpOld.getId());
			String oldStockRemark = beanTmpOld.getStockRemark();
			String oldDayOfMonth = beanTmpOld.getDayOfMonth();
			String oldStockReceive = beanTmpOld.getStockReceive();
 
			// Remark
			if (beanTmpNew != null) {
				String newdStockRemark = beanTmpNew.getStockRemark();
				String newdDayOfMonth = beanTmpNew.getDayOfMonth();
				String newdStockReceive = beanTmpNew.getStockReceive(); 
				listCSL = HandlerListLog.handlerListLog(listCSL, oldStockRemark, newdStockRemark,
						mapMSC.get(Config.sqlFieldStockRemark), id, changeBy,
						Config.sqlFieldStockRemark, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldDayOfMonth, newdDayOfMonth,
						mapMSC.get(Config.sqlFieldDayOfMonth), id, changeBy,
						Config.sqlFieldDayOfMonth, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldStockReceive, newdStockReceive,
						mapMSC.get(Config.sqlFieldStockReceive), id, changeBy,
						Config.sqlFieldStockReceive, remarkAction);  
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeStockCustomerDateLog,
					Config.sqlFieldStockCustomerDateId);
		}

		iconStatus = scdModel.upsertStockCustomerDateDetail(poList);
		InputStockCustomerDateDetail bean = new InputStockCustomerDateDetail();
		bean.setIconStatus(iconStatus);
		bean.setSystemStatus(SqlErrorHandler.handlerSqlErrorText(iconStatus));
		poList.clear();
		poList.add(bean);
		return poList;
	}
}
