package dao.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dao.ChangeLogDetailDao;
import entities.ChangeLogDetail;
import model.BeanCreateModel;
import model.master.CSOSOASModel;
import model.master.ChangeApprovedPlanDateLogModel;
import model.master.ChangeArticleDetailLogModel;
import model.master.ChangeArticleSubGroupDetailLogModel;
import model.master.ChangeBKKHolidayLogModel;
import model.master.ChangeConversionDetailLogModel;
import model.master.ChangeFacHolidayLogModel;
import model.master.ChangeFacWorkDateLogModel;
import model.master.ChangeLeadTimeStatusLogModel;
import model.master.ChangeMachineDetailLogModel;
import model.master.ChangeMainGroupDetailLogModel;
import model.master.ChangeProdOrderRunningLogModel;
import model.master.ChangeSORPODetailLogModel;
import model.master.ChangeSORTempProdLogModel;
import model.master.ChangeStockCustomerDateLogModel;
import model.master.ChangeSubGroupDetailLogModel;
import model.master.ChangeTempPlanningLotLogModel;
import model.master.PPMMTransactionlogModel;
import model.master.TEMPPlanningLotDailyLogModel;
import th.in.totemplate.core.sql.Database;

public class ChangeLogDetailDaoImpl implements ChangeLogDetailDao {
	@SuppressWarnings("unused")
	private BeanCreateModel bcModel = new BeanCreateModel();
	@SuppressWarnings("unused")
	private Database database;
	private String message;
	@SuppressWarnings("unused")
	private String conType;

	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdfFull = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public ChangeLogDetailDaoImpl(Database database, String conType) {
		this.database = database;
		this.conType = conType;
		this.message = "";
	}

	@Override
	public ArrayList<ChangeLogDetail> getChangeLogDetailByCaseLogDetail(ArrayList<ChangeLogDetail> poList) {
		ArrayList<ChangeLogDetail> list = new ArrayList<>();

		if ( ! poList.isEmpty()) {
			ChangeLogDetail bean = poList.get(0);
			String caseLog = bean.getCaseLog(); 
			if (caseLog.equals("SORPODetail")) {
				ChangeSORPODetailLogModel cspModel = new ChangeSORPODetailLogModel();
				list = cspModel.getChangeSORPODetailLogDetail(poList);
			} else if (caseLog.equals("SORTempProd")) {
				ChangeSORTempProdLogModel cspModel = new ChangeSORTempProdLogModel();
				list = cspModel.getChangeSORTempProdLogDetail(poList);

			} else if (caseLog.equals("StockCustomerDate")) {
				ChangeStockCustomerDateLogModel cspModel = new ChangeStockCustomerDateLogModel();
				list = cspModel.getChangeStockCustomerDateLogDetail(poList);

			} else if (caseLog.equals("ChangeSubGroupDetailLog")) {
				ChangeSubGroupDetailLogModel cspModel = new ChangeSubGroupDetailLogModel();
				list = cspModel.getChangeSubGroupDetailLogDetail(poList);

			} else if (caseLog.equals("TempPlanningLotDailyLog")) {
				TEMPPlanningLotDailyLogModel cspModel = new TEMPPlanningLotDailyLogModel();
				list = cspModel.getTEMPPlanningLotDailyLog(poList);

			} else if (caseLog.equals("TempPlannnigLotLog")) {
				ChangeTempPlanningLotLogModel cspModel = new ChangeTempPlanningLotLogModel();
				list = cspModel.getChangeTempPlanningLotLogDetail(poList);

			} else if (caseLog.equals("ProdOrderRunning")) {
				ChangeProdOrderRunningLogModel cspModel = new ChangeProdOrderRunningLogModel();
				list = cspModel.getChangeProdOrderRunningLogDetail(poList);

			} else if (caseLog.equals("MainGroupDetail")) {
				ChangeMainGroupDetailLogModel cspModel = new ChangeMainGroupDetailLogModel();
				list = cspModel.getChangeMainGroupDetailLogDetail(poList);

			} else if (caseLog.equals("MachineDetail")) {
				ChangeMachineDetailLogModel cspModel = new ChangeMachineDetailLogModel();
				list = cspModel.getChangeMachineDetailLogDetail(poList);

			} else if (caseLog.equals("LeadTimeStatus")) {
				ChangeLeadTimeStatusLogModel cspModel = new ChangeLeadTimeStatusLogModel();
				list = cspModel.getChangeLeadTimeStatusLogDetail(poList);
			} else if (caseLog.equals("FacWorkDate")) {
				ChangeFacWorkDateLogModel cspModel = new ChangeFacWorkDateLogModel();
				list = cspModel.getChangeFacWorkDateLogDetail(poList);

			} else if (caseLog.equals("FacHoliday")) {
				ChangeFacHolidayLogModel cspModel = new ChangeFacHolidayLogModel();
				list = cspModel.getChangeFacHolidayLogDetail(poList);

			} else if (caseLog.equals("ConversionDetail")) {
				ChangeConversionDetailLogModel cspModel = new ChangeConversionDetailLogModel();
				list = cspModel.getChangeConversionDetailLogDetail(poList);

			} else if (caseLog.equals("BKKHoliday")) {
				ChangeBKKHolidayLogModel cspModel = new ChangeBKKHolidayLogModel();
				list = cspModel.getChangeBKKHolidayLogDetail(poList);

			} else if (caseLog.equals("ArticleSubGroupDetail")) {
				ChangeArticleSubGroupDetailLogModel cspModel = new ChangeArticleSubGroupDetailLogModel();
				list = cspModel.getChangeArticleSubGroupDetailLogDetail(poList);

			} else if (caseLog.equals("ArticleDetail")) {
				ChangeArticleDetailLogModel cspModel = new ChangeArticleDetailLogModel();
				list = cspModel.getChangeArticleDetailLogDetail(poList);

			} else if (caseLog.equals("ApprovedPlanDate")) {
				ChangeApprovedPlanDateLogModel cspModel = new ChangeApprovedPlanDateLogModel();
				list = cspModel.getChangeApprovedPlanDateLogDetail(poList);

			} else if (caseLog.equals("CSO_SOAS")) {
				CSOSOASModel cspModel = new CSOSOASModel();
				list = cspModel.getCSO_SOASDetailLogDetail(poList);
			} else if (caseLog.equals("PPMMPOTransactionLog")) {
				PPMMTransactionlogModel cspModel = new PPMMTransactionlogModel();
				list = cspModel.getPPMMPOTransactionlogDetailLogDetail(poList);
			}
		}

		return list;
	}

	public String getMessage() {
		return this.message;
	}

}
