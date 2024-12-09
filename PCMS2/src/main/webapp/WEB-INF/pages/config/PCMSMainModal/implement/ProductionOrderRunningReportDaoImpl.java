package dao.implement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import Test.utilities.HandlerListLog;
import Test.utilities.SqlErrorHandler;
import Test.utilities.StringHandler;
import dao.ProductionOrderRunningReportDao;
import entities.ChangeSettingLogDetail;
import entities.InputTempProdDetail;
import entities.MasterSettingChangeDetail;
import entities.ProdOrderRunningDetail;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.master.ChangeSettingLogModel;
import model.master.MasterSettingChangeModel;
import model.master.ProdOrderRunningModel;
import model.master.SORTempProdModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class ProductionOrderRunningReportDaoImpl implements ProductionOrderRunningReportDao {
	@SuppressWarnings("unused")
	private BeanCreateModel bcModel = new BeanCreateModel();
	private String message;
	@SuppressWarnings("unused")
	private Database database;
	public DecimalFormat df3 = new DecimalFormat("###,###,##0.00");
	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	private String conType;

	public ProductionOrderRunningReportDaoImpl(Database database2, String conType) {
		this.database = database2;
		this.message = "";
		this.conType = conType;
	}

	@Override
	public ArrayList<ProdOrderRunningDetail> bookingProdOrderForProdOrderRunningReport(
			ArrayList<ProdOrderRunningDetail> poList) {
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		ProdOrderRunningDetail bean = poList.get(0);
		String prodOrderBook = bean.getProductionOrder();
		String changeByBook = bean.getChangeBy();
		for (ProdOrderRunningDetail beanTemp : poList) {
			boolean bl_topping = true;
			if ( ! beanTemp.getIsToppingChangeString().equals("true")) {
				bl_topping = false;
			}
			beanTemp.setTopping(bl_topping);
		}
		bean.setKeyWord(prodOrderBook);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStauts = "Update Success.";
		ArrayList<ProdOrderRunningDetail> list = porModel.getProductionOrderRunningDetailById(poList);
		if (list.size() > 0) {
			ProdOrderRunningDetail beanTmp = poList.get(0);
			String dataStatus = beanTmp.getDataStatus();
			String changeBy = beanTmp.getChangeBy();
			String remark = beanTmp.getRemark();
			list.get(0).setChangeBy(changeByBook);
			list.get(0).setRemark(remark);
			if (dataStatus.equals(Config.C_OPEN_STATUS)) {
				this.handlerChangeProdOrderRunningLog(poList, Config.C_ACTION_TEXT_UPDATE_05);
//				this.handlerTopping(poList);
				iconStatus = porModel.updateProdOrderRunningWithIdForRemark(list, changeByBook);
//				beanTmp = listTmp.get(0);
//				list = porModel.getProductionOrderRunningDetailById(poList);
//				list.get(0).setChangeDate(beanTmp.getChangeDate());
//				list.get(0).setDataStatus("X");
				// HANDLER TOPPING CHANGE
			} else {
				iconStatus = Config.C_ERR_ICON_STATUS;
				systemStauts = "This ProductionOrder already booked by " + changeBy;
			}
			// HANDLER TOPPING CHANGE
			list = porModel.getProductionOrderRunningDetailById(poList);
			list.get(0).setIconStatus(iconStatus);
			list.get(0).setSystemStatus(systemStauts);
		} else {
			list.clear();
			ProdOrderRunningDetail beanTemp = new ProdOrderRunningDetail();
			beanTemp.setIconStatus(Config.C_ERR_ICON_STATUS);
			beanTemp.setSystemStatus(Config.C_ERROR_TEXT);
			list.add(beanTemp);
		}
		return list;
	}

	@Override
	public ArrayList<ProdOrderRunningDetail> cancelProdOrderRunningWithId(ArrayList<ProdOrderRunningDetail> poList) {
		// TODO Auto-generated method stub
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
//		SORTempProdModel stpModel = new SORTempProdModel(this.conType);

		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "";

		ProdOrderRunningDetail beanMain = poList.get(0); 
		ArrayList<ProdOrderRunningDetail> listCheck = porModel.getProdOrderRunningDetailByProductionOrder(poList);
		// LOG
		if ( ! listCheck.isEmpty()) {
			ProdOrderRunningDetail beanListCheck = listCheck.get(0);
			boolean isSapCreated = beanListCheck.isSapCreated();
			boolean isSorLotOnProcess = beanListCheck.isSorLotOnProcess();
			if (isSorLotOnProcess) {
				iconStatus = Config.C_ERR_ICON_STATUS;
				systemStatus = "Need to Cancel lot from PO that on process.";
			} else if (isSapCreated) {
				iconStatus = Config.C_ERR_ICON_STATUS;
				systemStatus = "Production Order already in SAP or SOR Lot on process.";
			} else {
				beanMain.setTopping(false);
				beanMain.setRemark("");
				beanMain.setDataStatus(Config.C_OPEN_STATUS);
				beanMain.setProductionOrderTemp("");
				this.handlerChangeProdOrderRunningLog(poList, Config.C_ACTION_TEXT_DELETE_05);
				iconStatus = porModel.updateProdOrderRunningWithId(poList);
				systemStatus = SqlErrorHandler.handlerSqlErrorText(iconStatus);
			}
		} 
		// RESULT
		ArrayList<ProdOrderRunningDetail> list = porModel.getProductionOrderRunningDetailById(poList);
		list.get(0).setIconStatus(iconStatus);
		list.get(0).setSystemStatus(systemStatus);
		return list;
	}

	public String getMessage() {
		return this.message;
	}

	private String handlerChangeProdOrderRunningLog(ArrayList<ProdOrderRunningDetail> poList, String remarkAction) {
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		HashMap<Integer, ProdOrderRunningDetail> mapDateAndDataStatus = new HashMap<>();
		String changeBy = "";
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		String iconStatus = Config.C_SUC_ICON_STATUS;
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeProdOrderRunningLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		for (ProdOrderRunningDetail beanTmp : poList) {
			int id = beanTmp.getId();
			mapDateAndDataStatus.put(id, beanTmp);
		}
		ArrayList<ProdOrderRunningDetail> listOld = porModel.getProductionOrderRunningDetailById(poList);
		for (ProdOrderRunningDetail beanTmpOld : listOld) {
			ProdOrderRunningDetail beanTmpNew = mapDateAndDataStatus.get(beanTmpOld.getId());
			int id = beanTmpOld.getId();
			String oldDataStatus = beanTmpOld.getDataStatus();
			String oldRemark = beanTmpOld.getRemark();
			String oldProdTemp = beanTmpOld.getProductionOrderTemp();
			boolean oldIsTopping = beanTmpOld.isTopping();
			// Remark
			if (beanTmpNew != null) {
				String newDataStatus = beanTmpNew.getDataStatus();
				String newRemark = beanTmpNew.getRemark();
				boolean newIsTopping = beanTmpNew.isTopping();
				String newProdTemp = beanTmpNew.getProductionOrderTemp();
				listCSL =
						HandlerListLog.handlerListLog(listCSL, oldRemark, newRemark, mapMSC.get(Config.sqlFieldRemark),
								Integer.toString(id), changeBy, Config.sqlFieldRemark, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldDataStatus, newDataStatus,
						mapMSC.get(Config.sqlFieldDataStatus), Integer.toString(id), changeBy,
						Config.sqlFieldDataStatus, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, String.valueOf(oldIsTopping),
						String.valueOf(newIsTopping), mapMSC.get(Config.sqlFieldIsTopping), Integer.toString(id),
						changeBy, Config.sqlFieldIsTopping, remarkAction);
				listCSL = HandlerListLog.handlerListLog(listCSL, oldProdTemp, newProdTemp,
						mapMSC.get(Config.sqlFieldProductionOrderTemp), Integer.toString(id), changeBy,
						Config.sqlFieldProductionOrderTemp, remarkAction);
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeProdOrderRunningLog,
					Config.sqlFieldProdOrderRunningId);
		}
		return iconStatus;
	}

	private String handlerTopping(ArrayList<ProdOrderRunningDetail> poList) {
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		ArrayList<ProdOrderRunningDetail> list = porModel.getProductionOrderRunningDetailById(poList);
		ProdOrderRunningDetail beanTmpOld = list.get(0);
		boolean oldIsTopping = beanTmpOld.isTopping();
		String oldProdOrder = beanTmpOld.getProductionOrder();
		if (oldIsTopping) {
			oldProdOrder = oldProdOrder + "T";
		}
		ProdOrderRunningDetail beanTmpNew = poList.get(0);
		boolean newIsTopping = beanTmpNew.isTopping();
		String newProdOrder = "";
		String iconStatus = "";
		if (oldIsTopping == newIsTopping) {

		} else {
			if (newIsTopping) {
				if (oldProdOrder.substring(oldProdOrder.length()-1).equals("T")) {
					newProdOrder = oldProdOrder;
				} else {
					newProdOrder = oldProdOrder + "T";
				}
			} else if ( ! newIsTopping) {
				if (oldProdOrder.substring(oldProdOrder.length()-1).equals("T")) {
					newProdOrder = StringHandler.removeLastChar(oldProdOrder);
				} else {
					newProdOrder = oldProdOrder;
				}
			}
			bgjModel.execReplacedProdOrderOldWithNew(oldProdOrder, newProdOrder);
			iconStatus = Config.C_SUC_ICON_STATUS;
		}
		return iconStatus;
	}

	@Override
	public ArrayList<ProdOrderRunningDetail> saveProdOrderRunningWithIdForRemark(
			ArrayList<ProdOrderRunningDetail> poList)
	{
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		String prodOrder = poList.get(0).getProductionOrder();
		@SuppressWarnings("unused") String toppingStatus = "";
		ArrayList<InputTempProdDetail> listCheck = stpModel.getSorTempProdDetailByProductionOrder(prodOrder);
		// LOG
		this.handlerChangeProdOrderRunningLog(poList, Config.C_ACTION_TEXT_UPDATE_04);
		if ( ! listCheck.isEmpty()) {
			toppingStatus = this.handlerTopping(poList);
		}
		String iconStatus = porModel.updateProdOrderRunningWithIdForRemark(poList);

		// RESULT
		ArrayList<ProdOrderRunningDetail> list = porModel.getProductionOrderRunningDetailById(poList);
		list.get(0).setIconStatus(iconStatus);
		list.get(0).setSystemStatus(SqlErrorHandler.handlerSqlErrorText(iconStatus));
		return list;
	}

}
