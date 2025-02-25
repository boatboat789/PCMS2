package dao.implement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Test.utilities.HandlerListLog;
import Test.utilities.SqlErrorHandler;
import Test.utilities.SqlStatementHandler;
import Test.utilities.StringHandler;
import dao.POManagementDao;
import entities.ChangeSettingLogDetail;
import entities.InputApprovedDetail;
import entities.InputPODetail;
import entities.InputTempProdDetail;
import entities.MasterSettingChangeDetail;
import entities.POManagementDetail;
import entities.PlanningReportDetail;
import entities.ProdOrderRunningDetail;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.PlanningProdModel;
import model.ReportModel;
import model.master.ApprovedPlanDateModel;
import model.master.ChangeSettingLogModel;
import model.master.MasterSettingChangeModel;
import model.master.PlanLotSORDetailModel;
import model.master.ProdOrderRunningModel;
import model.master.RelationPOAndProdOrderModel;
import model.master.SORPODetailChangeModel;
import model.master.SORPODetailModel;
import model.master.SORTempProdModel;
import model.master.TEMPPlanningLotModel;
import model.master.TempPORunningModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class POManagementDaoImpl implements POManagementDao {
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;

	private String conType;
	private String message;
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	public DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String select = ""
			+ "  SPD.[ReferenceId] \r\n"
			+ " ,SPD.[Id] as [POId]\r\n"
			+ " ,SPD.[PO]\r\n"
			+ " ,SPD.[POLine] \r\n"
			+ " ,cd.[CustomerNo]\r\n"
			+ "	,case \r\n"
			+ "			when [CustomerShortName] is not null then [CustomerShortName] \r\n"
			+ "			else SPD.[CustomerName] \r\n"
			+ "			end as [CustomerName]  \r\n"
			+ " ,SPD.[MaterialNo]\r\n"
			+ "	,SPD.[GreigePlan]\r\n"
			+ "	,SPD.[CustomerDue] \r\n"
			+ " ,APD.[Id] as ApprovedPlanDateId\r\n"
			+ "	,APD.[SORCFMDate]\r\n"
			+ "	,APD.[SORDueDate]\r\n"
			+ " ,APD.[ApprovedBy]\r\n"
			+ "	,cast (\r\n"
			+ "		CASE\r\n"
			+ "			WHEN APD.[SORDueDate] IS NULL THEN 0\r\n"
			+ "			ELSE 1\r\n"
			+ "		END \r\n"
			+ "	 as bit) as IsApproved\r\n"
			+ " ,SPD.[POPuangId] \r\n"
			+ " ,SPD.[DocDate] \r\n"
			+ " ,IAD.[IsReverseAble]\r\n"
			+ " ,APD.[DataStatus] AS ApprovedDataStatus\r\n";

	private String selectLot = ""
			+ "      a.POId\r\n"
			+ "		,A.TempProdId\r\n"
			+ "		,A.ProductionOrder\r\n"
			+ "     ,a.[Article]"
			+ "		,a.GroupBegin\r\n"
			+ "		,a.[ProdOrderQty]\r\n"
			+ "		,IAD.[IsReverseAble]\r\n"
			+ "		,TPL.Id AS TempPlanningId \r\n"
			+ "	    ,TPL.[GroupNo] \r\n"
			+ "		,TPL.[SubGroup] \r\n"
			+ "		,TPL.[PlanSystemDate]\r\n"
			+ "		,a.[ChangeBy]\r\n"
			+ "		,CAST (\r\n"
			+ "			CASE \r\n"
			+ "				WHEN DFS.[ProductionOrderForCheck] IS NULL THEN 0\r\n"
			+ "				ELSE 1\r\n"
			+ "			END \r\n"
			+ "			AS BIT\r\n"
			+ "		 ) AS IsInSap\r\n"
			+ "		 , a.[ProductionOrderType]\r\n"
			+ "		 , a.[ColorType]\r\n";

	private String leftJoinAPD = ""
			+ "	LEFT JOIN [PPMM].[dbo].[ApprovedPlanDate] AS APD ON APD.[POId] = SPD.[Id] and\r\n"
			+ "													    APD.[DataStatus] = 'O' and\r\n"
			+ "                                                     APD.[SorDueDate] is not null\r\n";
	private String leftJoinSPDMainCD = ""
			+ "	left join ( \r\n"
			+ "   SELECT \r\n"
			+ "	    [Id] \r\n"
			+ "     ,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo] \r\n"
			+ "     ,[CustomerShortName] \r\n"
			+ "     ,[ChangeDate] \r\n"
			+ "     ,[CreateDate] \r\n"
			+ "  FROM [PCMS].[dbo].[CustomerDetail] \r\n"
			+ "	) as cd on cd.[CustomerNo] = SPD.[CustomerNo] 	\r\n";
	private String selectFromA = ""
			+ "  	STP.[Id] AS TempProdId\r\n" 
			+ "		, STP.POId\r\n"
			+ "		, SPD.[PO] \r\n"
			+ "		, SPD.[POLine] as POLine   \r\n"
			+ "		, SPD.[MaterialNo] as [MaterialNo]\r\n"
			+ "		, SPD.[CustomerNo] as [CustomerNo]    \r\n"
			+ "		, SPD.[CustomerName] as [CustomerName]\r\n"
			+ "		, SPD.[CustomerDue] as [CustomerDue]\r\n"
			+ "		, SPD.[GreigePlan] as [GreigePlan]    \r\n"
			+ "		, SPD.[Article] as [Article]    \r\n"
//			+ "		,COALESCE( SPDPIPD.[PO] ,SPD.[PO]) as PO\r\n"
//			+ "		,COALESCE( SPDPIPD.[POLine] ,SPD.[POLine]) as POLine   \r\n"
//			+ "		,COALESCE( SPDPIPD.[MaterialNo] ,SPD.[MaterialNo]) as [MaterialNo]\r\n"
//			+ "		,COALESCE( SPDPIPD.[CustomerNo] ,SPD.[CustomerNo]) as [CustomerNo]    \r\n"
//			+ "		,COALESCE( SPDPIPD.[CustomerName] ,SPD.[CustomerName]) as [CustomerName]\r\n"
//			+ "		,COALESCE( SPDPIPD.[CustomerDue] ,SPD.[CustomerDue]) as [CustomerDue]\r\n"
//			+ "		,COALESCE( SPDPIPD.[GreigePlan] ,SPD.[GreigePlan]) as [GreigePlan]    \r\n"
//			+ "		,COALESCE( SPDPIPD.[Article] ,SPD.[Article]) as [Article]    \r\n"
			+ "		,STP.[ForecastId] ,STP.[PlanInsteadId]   ,STP.[ColorType]\r\n"
			+ "		,STP.[ProductionOrder] ,STP.[FirstLot] ,STP.[ProdOrderQty] ,STP.[GroupOptions]\r\n"
			+ "		,STP.[GroupBegin] ,STP.[PPMMStatus] ,STP.[DataStatus] ,STP.[ChangeDate]\r\n"
			+ "		,STP.[ChangeBy] ,STP.[CreateDate] ,STP.[CreateBy] ,STP.[Batch]\r\n"
			+ "		,STP.[ProductionOrderType]\r\n"
			+ "		, SPD.[POPuangId] as [POPuangId]   \r\n" 
			+ "		,SUBSTRING(STP.[ProductionOrder] ,0,7) AS ProductionOrderForCheck \r\n";
	private String fromSTP = "" + "	from [PPMM].[dbo].[SOR_TempProd] as STP\r\n"; 
	private String leftJoinSPD = ""
			+ "	left join [PPMM].[dbo].[SOR_PODetail] as SPD on SPD.[Id] = STP.[POId] and\r\n"
			+ "													SPD.[DataStatus] = 'O'\r\n";
	private String fromA = ""
//			+ " FROM (\r\n"
			+ " 	SELECT \r\n"
			+ "     " + this.selectFromA
			+ "     " + this.fromSTP 
			+ "     " + this.leftJoinSPD
			+ "     WHERE STP.[ForecastId] is null and \r\n"
			+ "		      STP.[DataStatus] = 'O'\r\n"
//			+ " ) as a\r\n"
	;
	private String leftJoinIADWithSPD =
			"" + "	LEFT JOIN [PPMM].[dbo].[InputArticleDetail] AS iad ON iad.[Article] = spd.[Article]\r\n";
	private String leftJoinIAD =
			"" + "	LEFT JOIN [PPMM].[dbo].[InputArticleDetail] AS iad ON iad.[Article] = a.[Article]\r\n";

	private String leftJoinTPL =
			"" + "	LEFT JOIN [PPMM].[dbo].[TEMP_PlanningLot] AS TPL ON TPL.[TempProdId] = a.[TempProdId]\r\n";
	private String leftJoinDFS = ""
			+ "	left join ( select distinct SUBSTRING([ProductionOrder] ,0,7) AS ProductionOrderForCheck FROM [PPMM].[dbo].[DataFromSap] ) AS DFS ON  A.ProductionOrderForCheck =  DFS.[ProductionOrderForCheck]\r\n";

	public POManagementDaoImpl(Database database , String conType) {
		this.database = database ;
		this.message = "";
		this.conType = conType;
	}

	public String getMessage() {
		return this.message;
	}


	@Override
	public ArrayList<POManagementDetail> addNewLotToPO(ArrayList<POManagementDetail> poList)
	{
		// TODO Auto-generated method stub 
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		TempPORunningModel tprModel = new TempPORunningModel(this.conType);
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);
		RelationPOAndProdOrderModel rpapModel = new RelationPOAndProdOrderModel(this.conType); 
		PlanningProdModel ppModel = new PlanningProdModel(this.conType);
		BackGroundJobModel bgjdModel = new BackGroundJobModel(this.conType);
		ReportModel rpModel = new ReportModel(this.conType); 
		POManagementDetail beanResult = new POManagementDetail();
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "";
		POManagementDetail beanParam = poList.get(0);
		int poId = beanParam.getPoId();
		String firstLot = "N";
//		String prodQty = "0";
//		String dataStatus = "O";
		String colorType = "";
		String groupBegin = "";
		String changeBy = beanParam.getChangeBy();
//		String piId = "";
		String prodOrderType = "";
		String caseWork = "";
		String prodOrderRunning =  "";
		// 1 CHECK HAVE LOT ATLEAST 1 FOR USE IT AS MAIN LOT TO SECOND LOT
		// PROD
		ArrayList<POManagementDetail> listCurLotPO = this.getPOManagementLotDetailByPOId(poList);
		// 1 YES  PROCESS
		if (listCurLotPO.size() > 0) {

			ArrayList<ProdOrderRunningDetail> listRunningUsed = new ArrayList<>();
			POManagementDetail beanLastProd = listCurLotPO.get(listCurLotPO.size()-1);
			String prodOrder = beanLastProd.getProductionOrder();
			String keyWord = "";

			colorType = beanLastProd.getColorType();
			groupBegin = beanLastProd.getGroupBegin();
			prodOrderType = beanLastProd.getProductionOrderType();
//			char firstChar = prodOrder.charAt(0);
 
			String sevenToEightPos = "";
			String checkPosSeven = prodOrder.substring(6, prodOrder.length());
			if (checkPosSeven.contains("H")) {
				sevenToEightPos = "H";
			} else if (prodOrder.contains("/P")) {
				sevenToEightPos = "/P";
			} else if (prodOrder.contains("/S")) {
				sevenToEightPos = "/S";
			} 
//			else if (prodOrder.contains("T")) {
//				sevenPos = "T";
//			} 
			else {
				sevenToEightPos = "";
			}
			// IF IS CURRENT LOT IS NOT APPROVED = DIGIT 4M0000 THEN NEED NEW RUNNING TEMP LOT
//			if(Character.isDigit(firstChar)) {
			if(this.sshUtl.isNumeric(prodOrder.substring(0, 1))){
				keyWord = prodOrder.substring(0,2);
				caseWork = "1";
				ArrayList<InputTempProdDetail> listLastRunning = tprModel.getLastRunningProdFromTEMPPORunning(keyWord);
				String lastProd = "";
				int lastRunningNo = 1;
				if (listLastRunning.isEmpty()) {
					lastRunningNo = 1;
				} else {
					lastProd = listLastRunning.get(0).getProductionOrder();
					String lastRunning = lastProd.substring(2, 6); // XYZ001
					lastRunningNo = Integer.parseInt(lastRunning) +1;
				}
				prodOrderRunning = keyWord+String.format("%04d", lastRunningNo)+sevenToEightPos;
			}
			// ELSE IS CURRENT LOT IS ALREADY APPROVED = S0A001 THEN NEED RUNNING NEW LOT BY FIRST 3 LOT
			else {
				caseWork = "2";
				keyWord = prodOrder.substring(0,3);
				ArrayList<ProdOrderRunningDetail> listRunning = porModel.getProdOrderRunningDetail(keyWord);
				if (listRunning.isEmpty()) {
					bgjModel.execUpsertToProdOrderRunning(keyWord);
					listRunning = porModel.getProdOrderRunningDetail(keyWord);
				}
				ProdOrderRunningDetail beanTmpRunning = listRunning.get(0);// T0A001
				String prodOrderTemp = "";
				prodOrderRunning = beanTmpRunning.getProductionOrder() +sevenToEightPos;
				bgjModel.execReplacedProdOrderOldWithNew(prodOrderTemp, prodOrderRunning);
				beanTmpRunning.setDataStatus(Config.C_CLOSE_STATUS);
				beanTmpRunning.setProductionOrderTemp(prodOrderTemp);
				beanTmpRunning.setRemark(Config.C_ACTION_TEXT_INSERT_02);
				beanTmpRunning.setChangeBy(changeBy);
				listRunningUsed.add(beanTmpRunning);
			}

			InputTempProdDetail beanTemp = new InputTempProdDetail();
			beanTemp.setNo(Integer.toString(poId));
			beanTemp.setProductionOrder(prodOrderRunning);
			beanTemp.setFirstLot(firstLot);
			beanTemp.setDbProdQty(0);
			beanTemp.setChangeBy(changeBy);
			beanTemp.setColorType(colorType);
			beanTemp.setGroupBegin(groupBegin);
			beanTemp.setProductionOrderType(prodOrderType);
			ArrayList<InputTempProdDetail> list = new ArrayList<>();
			//INSERT
			list.add(beanTemp);
			stpModel.insertSORTempProd(list, Config.C_OPEN_STATUS);
			if(caseWork.equals("1")) {   
				iconStatus = tprModel.upsertTEMPPORunningDetail(keyWord, prodOrderRunning, iconStatus, changeBy); // boathere 
			}else {
				porModel.updateProdOrderRunningWithId(listRunningUsed);

			}
			ArrayList<InputTempProdDetail> listTempProd = spdModel.getSorPODetailWithSORTempProdByPOId(Integer.toString(poId));
			for (InputTempProdDetail element : listTempProd) {
				element.setChangeBy(changeBy);
			}
			rpapModel.upsertRelationPOAndProdOrder(listTempProd);
			bgjModel.execHandlerPlanLotSORDetail();
			bgjModel.execUpsertRuleCalculated();
			bgjdModel.execHandlerBatchNoForRelation();
			rpModel.processVolumeForReport();
			ppModel.rePlanningLot();
		}
		//1  NO   SEND ERROR
		else {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = "Need atleast 1 Lot for add new Lot in PO.";
		}
		beanResult.setIconStatus(iconStatus);
		beanResult.setSystemStatus(systemStatus);
		listCurLotPO = this.getPOManagementLotDetailByPOId(poList);
		if(listCurLotPO.isEmpty()) {
			listCurLotPO.add(beanResult);
		}
		return listCurLotPO;
	}

	// USE IT : POLIST = 1 DONT USE FOR POLIST 
	@Override
	public ArrayList<POManagementDetail> cancelPOLine(ArrayList<POManagementDetail> poList,
			String SORPODetailDataStatus,boolean isCancelPOChange) {
		// TODO Auto-generated method stub
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		ApprovedPlanDateModel appModel = new ApprovedPlanDateModel(this.conType);
		SORPODetailModel spdModel = new SORPODetailModel(this.conType);
		SORPODetailChangeModel sdpcModel = new SORPODetailChangeModel(this.conType);
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		RelationPOAndProdOrderModel rpapModel = new RelationPOAndProdOrderModel(this.conType);
		PlanLotSORDetailModel plsdModel = new PlanLotSORDetailModel(this.conType);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "Cancel Success.";
		ArrayList<InputPODetail> ipdList = new ArrayList<>();
		ArrayList<InputTempProdDetail> itpdList = new ArrayList<>();
		ArrayList<POManagementDetail> poListTemp = new ArrayList<>();
		ArrayList<POManagementDetail> poListTempPOPuangOpen = new ArrayList<>();
		ArrayList<InputApprovedDetail> poListPuangTemp = new ArrayList<>();
		ArrayList<POManagementDetail> poListAPPuangTempLog = new ArrayList<>();
		ArrayList<POManagementDetail> tempListForLog = new ArrayList<>();
		ArrayList<ProdOrderRunningDetail> listRunningUsed = new ArrayList<>();
		// PROD 
		ArrayList<POManagementDetail> listOldPO = this.getPOManagementDetailByPOId(poList);
		if (listOldPO.size() > 0) {
			POManagementDetail beanNew = poList.get(0);
			String changeBy = beanNew.getChangeBy();
			String remark = beanNew.getRemark();
			POManagementDetail beanTmp = listOldPO.get(0);
			int poIdMain = beanTmp.getPoId();
			int poPuangId = beanTmp.getPoPuangId();
			
			InputPODetail ipdBean = new InputPODetail();
			ipdBean.setId(poIdMain);
			ipdBean.setChangeBy(changeBy);
			ipdList.add(ipdBean);
			if (poPuangId != 0) {
				ArrayList<POManagementDetail> listPOWithPuang = this.getPOManagementDetailByPOPuangId(listOldPO);
				for (int i = 0; i < listPOWithPuang.size(); i ++ ) {
					POManagementDetail beanCurrent = listPOWithPuang.get(i);
					POManagementDetail beanTempOne = new POManagementDetail();
					int poIdTemp = beanCurrent.getPoId();
					beanTempOne.setPoId(poIdTemp);
					beanTempOne.setPoPuangId(beanCurrent.getPoPuangId());
					beanTempOne.setApprovedPlanDateId(beanCurrent.getApprovedPlanDateId());
					beanTempOne.setDataStatus(Config.C_CLOSE_STATUS);
					beanTempOne.setSorCFMDate(Config.C_BLANK);
					beanTempOne.setSorDueDate(Config.C_BLANK);
					beanTempOne.setChangeBy(changeBy);
					beanTempOne.setRemark(remark);
					beanTempOne.setApprovedDataStatus(Config.C_CLOSE_STATUS);
					poListAPPuangTempLog.add(beanTempOne);

					InputApprovedDetail beanTempTwo = new InputApprovedDetail();
					beanTempTwo.setPoId(poIdTemp);
					beanTempTwo.setDataStatus(Config.C_CLOSE_STATUS);
					beanTempTwo.setSorCFMDate(Config.C_BLANK);
					beanTempTwo.setSorDueDate(Config.C_BLANK);
					beanTempTwo.setApprovedBy(changeBy);
					beanTempTwo.setRemark(remark);
					poListPuangTemp.add(beanTempTwo);
					if (poIdMain != poIdTemp) {
						POManagementDetail beanTempThree = new POManagementDetail();
						beanTempThree.setPoId(beanCurrent.getPoId());
						beanTempThree.setPoPuangId(0);
						beanTempThree.setApprovedPlanDateId(beanCurrent.getApprovedPlanDateId());
						beanTempThree.setDataStatus(Config.C_OPEN_STATUS);
						beanTempThree.setRemark(remark);
						beanTempThree.setChangeBy(changeBy);
						poListTempPOPuangOpen.add(beanTempThree);
					}
				}
			} 
			for (int i = 0; i < poList.size(); i ++ ) {
				POManagementDetail beanCurrent = poList.get(i);
				POManagementDetail beanTempOne = new POManagementDetail();
				int poIdTemp = beanCurrent.getPoId();
				beanTempOne.setPoId(poIdTemp);
				beanTempOne.setPoPuangId(0);
				beanTempOne.setRemark(remark);
				beanTempOne.setDataStatus(SORPODetailDataStatus);
				beanTempOne.setChangeBy(changeBy);
				poListTemp.add(beanTempOne);

				InputApprovedDetail beanTempTwo = new InputApprovedDetail();
				beanTempTwo.setPoId(poIdTemp);
				beanTempTwo.setDataStatus(Config.C_CLOSE_STATUS);
				beanTempTwo.setSorCFMDate(Config.C_BLANK);
				beanTempTwo.setSorDueDate(Config.C_BLANK);
				beanTempTwo.setApprovedBy(changeBy);
				beanTempTwo.setRemark(remark);
				poListPuangTemp.add(beanTempTwo);

				POManagementDetail beanTempThree = new POManagementDetail();
				beanTempThree.setPoId(poIdTemp);
				beanTempThree.setPoPuangId(beanCurrent.getPoPuangId());
				beanTempThree.setApprovedPlanDateId(beanCurrent.getApprovedPlanDateId());
				beanTempThree.setDataStatus(Config.C_CLOSE_STATUS);
				beanTempThree.setSorCFMDate(Config.C_BLANK);
				beanTempThree.setSorDueDate(Config.C_BLANK);
				beanTempThree.setChangeBy(changeBy);
				beanTempThree.setRemark(remark);
				beanTempThree.setApprovedDataStatus(Config.C_CLOSE_STATUS);
				poListAPPuangTempLog.add(beanTempOne);
			} 
			// PROD  
			ArrayList<POManagementDetail> listOldProdOrder = this.getPOManagementLotDetailByPOId(poList);
//			if (listOldProdOrder.size() > 0) {   
			for (int i = 0; i < listOldProdOrder.size(); i ++ ) {
				POManagementDetail beanCurrent = listOldProdOrder.get(i);
				boolean isInSap = beanCurrent.isInSap();
				String prodOrder = beanCurrent.getProductionOrder();
				int tempProdId = beanCurrent.getTempProdId();
				String curProdOrder = beanCurrent.getProductionOrder();

				InputTempProdDetail beanITPD = new InputTempProdDetail();
				beanITPD.setTempProdId(tempProdId);
				beanITPD.setChangeBy(changeBy);
				beanITPD.setDataStatus(Config.C_CLOSE_STATUS);
				itpdList.add(beanITPD);

				POManagementDetail beanTempOne = new POManagementDetail();
				beanTempOne.setPoId(beanCurrent.getPoId());
				beanTempOne.setTempPlanningId(beanCurrent.getTempPlanningId());
				beanTempOne.setTempProdId(beanCurrent.getTempProdId());
				beanTempOne.setChangeBy(beanCurrent.getChangeBy());
				beanTempOne.setDataStatus(Config.C_CLOSE_STATUS);
				tempListForLog.add(beanTempOne);
				// CHECK IN SAP IF IN SAP NOT CANCEL BOOK
				if ( ! isInSap) {
					if ( ! this.sshUtl.isNumeric(curProdOrder.substring(0, 1))) {
						ProdOrderRunningDetail beanTmpCurrent = new ProdOrderRunningDetail();// T0A001
						beanTmpCurrent.setDataStatus(Config.C_OPEN_STATUS);
						beanTmpCurrent.setProductionOrderTemp(Config.C_BLANK);
						beanTmpCurrent.setProductionOrder(prodOrder.substring(0, 6));
						beanTmpCurrent.setTopping(false);
						beanTmpCurrent.setRemark(Config.C_BLANK);
						beanTmpCurrent.setChangeBy(changeBy);
						listRunningUsed.add(beanTmpCurrent);
					}
				}
			}
//			}
			// LOG
			// CANCEL PO POLINE MAIN	
			if ( ! poListTemp.isEmpty()) {
				this.handlerChangeSORPODetailWithDataStatus(poListTemp, Config.C_ACTION_TEXT_DELETE_02);
			}
			// CANCEL PO POLINE : POPUANG ID SUB
			if ( ! poListTempPOPuangOpen.isEmpty()) {
				this.handlerChangeSORPODetailWithDataStatus(poListTempPOPuangOpen, Config.C_ACTION_TEXT_DELETE_02);
			}
			// CANCEL PROD  
			if ( ! tempListForLog.isEmpty()) {
				this.handlerChangSORTempProdWithDataStatus(tempListForLog, Config.C_ACTION_TEXT_DELETE_02);
				this.handlerChangeTempPlanningLotWithDataStatus(tempListForLog, Config.C_ACTION_TEXT_DELETE_02);
			}
			if ( ! listRunningUsed.isEmpty()) {
				cslModel.handlerChangeProdDetailForNewDetailList(listRunningUsed, Config.C_ACTION_TEXT_DELETE_02);
			}
			if ( ! poListAPPuangTempLog.isEmpty()) {
				this.handlerChangeApprovedPODetail(poListAPPuangTempLog, Config.C_ACTION_TEXT_DELETE_02);
			}

			// CANCEL
			// PO MAIN = 'X' AND PUANG = NULL
			if ( ! poListTemp.isEmpty()) {
				spdModel.updateDataStatusWithIdForSORPODetail(poListTemp);
			}
			// PO SUB = 'O' AND PUANG = NULL
			if ( ! poListTempPOPuangOpen.isEmpty()) {
				spdModel.updateDataStatusWithIdForSORPODetail(poListTempPOPuangOpen);
			}
			// TRY CANCEL PROD WITH POID
//			if ( ! itpdList.isEmpty()) {   
//				stpModel.updateDataStatusWithIdForSORTempProd(itpdList, Config.C_CLOSE_STATUS);
//			}
			// PROD
			if ( ! itpdList.isEmpty()) {   
				stpModel.updateDataStatusWithIdForSORTempProd(itpdList, Config.C_CLOSE_STATUS);
				tplModel.updateDataStatusWithTempProdIdForTempPlanningLot(itpdList);
			}
			if ( ! listRunningUsed.isEmpty()) { 
				porModel.updateProdOrderRunningWithProductionOrder(listRunningUsed);
			}
			if ( ! tempListForLog.isEmpty()) {
				plsdModel.updatePlanLotSORDetailWithTempProdId(tempListForLog);
				rpapModel.updateRelationPOAndProdOrderWithTempProdId(tempListForLog);
			}
			if ( ! poListPuangTemp.isEmpty()) {  
				iconStatus = appModel.upsertApprovedPlanDate(poListPuangTemp);
			} 

			if(isCancelPOChange) { 
				sdpcModel.upsertSORPODetailChangeWithPOId(ipdList);
			}
			// DELETE
			poList = this.getPOManagementDetailByPO(poList);
			if (poList.isEmpty()) {
				POManagementDetail beanTemp = new POManagementDetail();
				iconStatus = "I0";
				poList.add(beanTemp);
			}
		} else {
			poList.clear();
			POManagementDetail beanTemp = new POManagementDetail();
			iconStatus = Config.C_ERR_ICON_STATUS;
			poList.add(beanTemp);
		}
		systemStatus = SqlErrorHandler.handlerSqlErrorText(iconStatus);
		poList.get(0).setIconStatus(iconStatus);
		poList.get(0).setSystemStatus(systemStatus);
		return poList;
	}

	@Override
	public ArrayList<POManagementDetail> cancelProductionOrder(ArrayList<POManagementDetail> poList) {
		// TODO Auto-generated method stub
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		RelationPOAndProdOrderModel rpapModel = new RelationPOAndProdOrderModel(this.conType);
		PlanLotSORDetailModel plsdModel = new PlanLotSORDetailModel(this.conType);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "Cancel Success.";
		ArrayList<POManagementDetail> listOld = this.getPOManagementLotDetailByTempProdId(poList);
		if (listOld.size() == 0 ) {
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = "Someone already handler this Prod.Order.";
//			systemStatus = "If there is only one Production Order left, you need to cancel the PO.";
		}
		else if (listOld.size() > 0) {
//			ArrayList<POManagementDetail> listCheckProdOld = this.getPOManagementLotDetailByPOId(poList);
//			if(listCheckProdOld.size() == 1 ) {
//				iconStatus = Config.C_ERR_ICON_STATUS;
//				systemStatus = "Already have last prod.order need cancel PO instead.";
//			}
//			else {
				POManagementDetail beanCurrent = listOld.get(0);
				boolean isInSap = beanCurrent.isInSap();
				String prodOrder = beanCurrent.getProductionOrder();

				POManagementDetail beanTmp = poList.get(0);
				int tempProdId = beanTmp.getTempProdId();
				String curProdOrder = beanTmp.getProductionOrder();
				String changeBy = beanTmp.getChangeBy();
				ArrayList<InputTempProdDetail> itpdList = new ArrayList<>();
				InputTempProdDetail beanITPD = new InputTempProdDetail();
				beanITPD.setTempProdId(tempProdId);
				beanITPD.setChangeBy(changeBy);
				beanITPD.setDataStatus(Config.C_CLOSE_STATUS);
				itpdList.add(beanITPD);
				ArrayList<POManagementDetail> tempListForLog = new ArrayList<>();
				for (int i = 0; i < poList.size(); i ++ ) {
					POManagementDetail beanTemp = poList.get(i);
					POManagementDetail beanTempOne = new POManagementDetail();
					beanTempOne.setPoId(beanTemp.getPoId());
					beanTempOne.setTempPlanningId(beanTemp.getTempPlanningId());
					beanTempOne.setTempProdId(beanTemp.getTempProdId());
					beanTempOne.setChangeBy(beanTemp.getChangeBy());
					beanTempOne.setDataStatus(Config.C_CLOSE_STATUS);
					beanTempOne.setRemark(beanTemp.getRemark());
					tempListForLog.add(beanTempOne);
				}

				ArrayList<ProdOrderRunningDetail> listRunningUsed = new ArrayList<>();
				if ( ! isInSap) {
					if ( ! this.sshUtl.isNumeric(curProdOrder.substring(0, 1))) {
						ProdOrderRunningDetail beanTmpCurrent = new ProdOrderRunningDetail();// T0A001
						beanTmpCurrent.setDataStatus(Config.C_OPEN_STATUS);
						beanTmpCurrent.setProductionOrderTemp("");
						beanTmpCurrent.setProductionOrder(prodOrder.substring(0, 6));
						beanTmpCurrent.setTopping(false);
						beanTmpCurrent.setRemark("");
						beanTmpCurrent.setChangeBy(changeBy);
						listRunningUsed.add(beanTmpCurrent);
					}
				}

				// CANCEL PROD FROM WORK TABLE
				if ( ! tempListForLog.isEmpty()) {
					this.handlerChangSORTempProdWithDataStatus(tempListForLog, Config.C_ACTION_TEXT_DELETE_01);
					this.handlerChangeTempPlanningLotWithDataStatus(tempListForLog, Config.C_ACTION_TEXT_DELETE_01);
					plsdModel.updatePlanLotSORDetailWithTempProdId(tempListForLog);
					rpapModel.updateRelationPOAndProdOrderWithTempProdId(tempListForLog);
				}
				if ( ! itpdList.isEmpty()) {
					stpModel.updateDataStatusWithIdForSORTempProd(itpdList, Config.C_CLOSE_STATUS);
					tplModel.updateDataStatusWithTempProdIdForTempPlanningLot(itpdList);
				}
	//					ArrayList<POManagementDetail> relaList = rpapModel.getRelationPOAndProdOrderDetailForPOIdWithTempProdId(poList);
	//					ArrayList<POManagementDetail> planLotList = plsdModel.getPlanLotSORDetailWithTempProdId(poList);

				// CHECK IN SAP IF IN SAP NOT CANCEL BOOK
				if ( ! listRunningUsed.isEmpty()) {
					// LOG
					cslModel.handlerChangeProdDetailForNewDetailList(listRunningUsed, Config.C_ACTION_TEXT_DELETE_01);
					// REAL
					porModel.updateProdOrderRunningWithProductionOrder(listRunningUsed);
				}
				poList = this.getPOManagementLotDetailByPOId(poList);
				if (poList.isEmpty()) {
					POManagementDetail beanTemp = new POManagementDetail();
					iconStatus = "I0";
					poList.add(beanTemp);
				}
				systemStatus = SqlErrorHandler.handlerSqlErrorText(iconStatus); 
//			}
		} else {
			poList.clear();
			POManagementDetail beanTemp = new POManagementDetail();
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = SqlErrorHandler.handlerSqlErrorText(iconStatus);
			poList.add(beanTemp);
		}
		poList.get(0).setIconStatus(iconStatus);
		poList.get(0).setSystemStatus(systemStatus);
		return poList;
	}

	@Override
	public ArrayList<POManagementDetail> changeProductionOrderDetail(ArrayList<POManagementDetail> poList) {
		SORTempProdModel stpModel = new SORTempProdModel(this.conType);
		RelationPOAndProdOrderModel rpapModel = new RelationPOAndProdOrderModel(this.conType);
		PlanLotSORDetailModel plsdModel = new PlanLotSORDetailModel(this.conType); 
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "Update Success.";
		ArrayList<POManagementDetail> listOld = this.getPOManagementLotDetailByTempProdId(poList);
		if (listOld.size() > 0) {
			this.handlerChangeProdDetail(poList, Config.C_ACTION_TEXT_UPDATE_07);
			POManagementDetail beanTmp = poList.get(0);
			int tempProdId = beanTmp.getTempProdId();
			String changeBy = beanTmp.getChangeBy();
			String newProdOrderQty = beanTmp.getProductionOrderQty();
//			String oldGroupBegin = beanTmp.getGroupBegin();
			String newGroupBegin = beanTmp.getNewGroupBegin();
			@SuppressWarnings("unused")
			String planGroup = beanTmp.getMainNSubGroup();
			
			String oldProdOrderQty = listOld.get(0).getProductionOrderQty();
//			String oldGroupBegin = listOld.get(0).getGroupBegin();
			ArrayList<InputTempProdDetail> listApp = new ArrayList<>();
			InputTempProdDetail beanApp = new InputTempProdDetail();
			beanApp.setTempProdId(beanTmp.getTempProdId());
			beanApp.setGroupBegin(newGroupBegin);
			beanApp.setProductionOrderQty(newProdOrderQty);
			beanApp.setChangeBy(changeBy);
			listApp.add(beanApp);
			iconStatus = stpModel.updateSORTempProdWithMasterChangeProdOrderQty(listApp);
			if ( ! newProdOrderQty.equals(oldProdOrderQty)) {    
				ArrayList<POManagementDetail> list =
						rpapModel.getRelationPOAndProdOrderDetailForPOIdWithTempProdId(tempProdId);
				this.processVolumeForReport(list);
				plsdModel.updatePlanLotSORDetailForQuantityMRWithTempProdId(poList);
			}
//			C_ACTION_TEXT_UPDATE_15
			if ( ! newGroupBegin.equals(Config.C_BLANK)) { 
				//UPDATE BLANK USER DATE
				TEMPPlanningLotModel tplModel = new TEMPPlanningLotModel(this.conType);
				PlanningProdModel ppModel = new PlanningProdModel(this.conType);

				InputTempProdDetail bean = new InputTempProdDetail();
				bean.setTempProdId(tempProdId);
				bean.setPlanUserDate(null);
				bean.setPlanUserChangeDate(null); 
				bean.setPlanBy(changeBy);

				ArrayList<InputTempProdDetail> resList = new ArrayList<>();
				resList.add(bean);

				iconStatus = stpModel.updateSORTempProdWithMasterChangeGroupBegin(listApp);
				tplModel.updateTempPlanningLotWithPlanUserDateByTempProdId(resList); 
				ppModel.rePlanningLot();
			}

			poList = this.getPOManagementLotDetailByTempProdId(poList);
			if (poList.isEmpty()) {
				POManagementDetail beanTemp = new POManagementDetail();
				poList.add(beanTemp);
			}
		} else {
			poList.clear();
			POManagementDetail beanTemp = new POManagementDetail();
			iconStatus = Config.C_ERR_ICON_STATUS;
			poList.add(beanTemp);
		}
		systemStatus = SqlErrorHandler.handlerSqlErrorText(iconStatus);
		poList.get(0).setIconStatus(iconStatus);
		poList.get(0).setSystemStatus(systemStatus);
		return poList;
	}

	@Override
	public ArrayList<POManagementDetail> changeSorDueAndSorCFM(ArrayList<POManagementDetail> poList) {
		ApprovedPlanDateModel appModel = new ApprovedPlanDateModel(this.conType);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "Update Success.";
		ArrayList<POManagementDetail> listOld = this.getPOManagementDetailByPOId(poList);
		if (listOld.size() > 0) {
			POManagementDetail beanTmp = poList.get(0);
			String changeBy = beanTmp.getChangeBy();
			String remark = beanTmp.getRemark();
			beanTmp.setApprovedDataStatus(Config.C_OPEN_STATUS);
			ArrayList<InputApprovedDetail> listApp = new ArrayList<>();
			InputApprovedDetail beanApp = new InputApprovedDetail();
			beanApp.setPoId(beanTmp.getPoId());
			beanApp.setSorCFMDate(beanTmp.getSorCFMDate());
			beanApp.setSorDueDate(beanTmp.getSorDueDate());
			beanApp.setApprovedBy(changeBy);
			beanApp.setRemark(remark);
			beanApp.setDataStatus(Config.C_OPEN_STATUS);
			listApp.add(beanApp);
			this.handlerChangeApprovedPODetail(poList, Config.C_ACTION_TEXT_UPDATE_06);
			iconStatus = appModel.upsertApprovedPlanDate(listApp);
			poList = this.getPOManagementDetailByPOId(poList);
			if (poList.isEmpty()) {
				POManagementDetail beanTemp = new POManagementDetail();
				poList.add(beanTemp);
			}
		} else {
			poList.clear();
			POManagementDetail beanTemp = new POManagementDetail();
			iconStatus = Config.C_ERR_ICON_STATUS;
			poList.add(beanTemp);
		}
		systemStatus = SqlErrorHandler.handlerSqlErrorText(iconStatus);
		poList.get(0).setIconStatus(iconStatus);
		poList.get(0).setSystemStatus(systemStatus);
		return poList;
	} 
	@Override
	public ArrayList<POManagementDetail> getPOManagementDetailByPO(ArrayList<POManagementDetail> poList) {
		ArrayList<POManagementDetail> list = null;
		String where = "";
		POManagementDetail bean = poList.get(0);
		String po = bean.getPo();
		if ( ! po.equals("")) {
			where = " where SPD.[PO] = '" + po + "' and SPD.[DataStatus] = 'O' ";
		}
		String sql = ""
				+ " select \r\n"
				+ this.select
				+ " FROM [PPMM].[dbo].[SOR_PODetail] AS SPD \r\n "
				+ this.leftJoinAPD
				+ this.leftJoinIADWithSPD
				+ this.leftJoinSPDMainCD
				+ where
				+ " Order By SPD.[PO] ,SPD.[POLine] ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPOManagementDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<POManagementDetail> getPOManagementDetailByPOId(ArrayList<POManagementDetail> poList) {
		ArrayList<POManagementDetail> list = null;
		String where = "";

		String whereTmp = "";
		if ( ! poList.isEmpty()) {
			whereTmp = " ( ";
			for (int i = 0; i < poList.size(); i ++ ) {
				POManagementDetail beanTmp = poList.get(i);
				int poId = beanTmp.getPoId();
				whereTmp += " SPD.[Id] = '" + poId + "' ";
				if (i < poList.size() -1) {
					whereTmp += " or ";
				}
			}
			whereTmp += " ) \r\n";
		}
		where = " where SPD.[DataStatus] = 'O'\r\n";
		if ( ! whereTmp.equals("")) {
			where = StringHandler.addStringAndIfNotEmpty(where);
			where += whereTmp;
		}
		String sql = ""
				+ " select \r\n"
				+ this.select
				+ " FROM [PPMM].[dbo].[SOR_PODetail] AS SPD \r\n "
				+ this.leftJoinAPD
				+ this.leftJoinIADWithSPD
				+ this.leftJoinSPDMainCD
				+ where
				+ " Order By SPD.[PO] ,SPD.[POLine] "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPOManagementDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<POManagementDetail> getPOManagementDetailByPOPuangId(ArrayList<POManagementDetail> poList) {
		ArrayList<POManagementDetail> list = null;
		String where = "";
		POManagementDetail bean = poList.get(0);
		int poPuangId = bean.getPoPuangId();
		where = " where SPD.[POPuangId] = " + poPuangId + "  and SPD.[DataStatus] = 'O'";
		String sql = ""
				+ " select \r\n"
				+ this.select
				+ " FROM [PPMM].[dbo].[SOR_PODetail] AS SPD \r\n "
				+ this.leftJoinAPD
				+ this.leftJoinIADWithSPD
				+ this.leftJoinSPDMainCD 
				+ where
				+ " Order By SPD.[PO] ,SPD.[POLine] ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPOManagementDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<POManagementDetail> getPOManagementLotDetailByPOId(ArrayList<POManagementDetail> poList) {
		ArrayList<POManagementDetail> list = null;
		POManagementDetail bean = poList.get(0);
		int poId = bean.getPoId();
		String sql = ""
				+ " select \r\n"
				+ this.selectLot
				+ " from ( \r\n"
				+ this.fromA
				+ " and SPD.[Id] = "+ poId + " \r\n"
				+ " ) as a\r\n"
				+ this.leftJoinIAD
				+ this.leftJoinTPL
				+ this.leftJoinDFS
				+ " Order By a.[PO] ,a.[POLine],a.ProductionOrder "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPOManagementDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<POManagementDetail> getPOManagementLotDetailByTempProdId(ArrayList<POManagementDetail> poList) {
		ArrayList<POManagementDetail> list = null;
		POManagementDetail bean = poList.get(0);
		int tempProdId = bean.getTempProdId();
		String sql = ""
				+ " select \r\n"
				+ this.selectLot
				+ " from ( \r\n"
				+ this.fromA
				+ " 			and\r\n"
				+ "             STP.[Id] = "
				+ tempProdId
				+ " "
				+ " ) as a\r\n"
				+ this.leftJoinIAD
				+ this.leftJoinTPL
				+ this.leftJoinDFS
				+ " Order By a.[PO] ,a.[POLine],a.ProductionOrder "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPOManagementDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<POManagementDetail> getViewTEMPPlanningLotOnProcess() {
		ArrayList<POManagementDetail> list = null;
		String sql = "" // REPLACE(po, char(9), '') LTRIM(RTRIM(po))
				+ "  SELECT DISTINCT   COALESCE( SPDPUANG.[PO] ,SPD.[PO])  as PO \r\n"
				+ "  FROM [PPMM].[dbo].[viewTEMPPlanningLotOnProcess] as vtplop  \r\n"
				+ "  LEFT JOIN [PPMM].[dbo].[SOR_PODetail] AS spd  on spd.[Id] = vtplop.[POId]\r\n"
				+ "  LEFT JOIN [PPMM].[dbo].[SOR_PODetail] AS SPDPUANG ON SPD.[POPuangId] = SPDPUANG.[POPuangId] \r\n"
				+ "  WHERE POID IS NOT NULL and \r\n"
				+ "		(\r\n"
				+ "			( SPD.[DataStatus] = 'O' OR SPD.[DataStatus] is null) or \r\n"
				+ "			( SPDPUANG.[DataStatus] = 'O' )\r\n"
				+ "		) \r\n"
				+ "   Order By [PO]  "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPOManagementDetail(map));
		}
		return list;
	}

	private String handlerChangeApprovedPODetail(ArrayList<POManagementDetail> poList, String remarkAction) {
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		HashMap<Integer, POManagementDetail> mapDateAndDataStatus = new HashMap<>();
//		String changeBy = "";
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		String iconStatus = Config.C_SUC_ICON_STATUS;
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeApprovedPlanDateLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		for (POManagementDetail beanTmp : poList) {
			int id = beanTmp.getApprovedPlanDateId();
			mapDateAndDataStatus.put(id, beanTmp);
		}
		ArrayList<POManagementDetail> listOld = this.getPOManagementDetailByPOId(poList);
		for (POManagementDetail beanTmpOld : listOld) {
			int appId = beanTmpOld.getApprovedPlanDateId();
//			int poId =  beanTmpOld.getPoId() ;
			POManagementDetail beanTmpNew = mapDateAndDataStatus.get(appId);
			String oldSORCFMDate = beanTmpOld.getSorCFMDate();
			String oldSORDueDate = beanTmpOld.getSorDueDate();
			String oldChangeBy = beanTmpOld.getApprovedBy();
			String oldDataStatus = beanTmpOld.getApprovedDataStatus();
			String oldRemark = beanTmpOld.getRemark();
			// Remark
			if (beanTmpNew != null) {
				if(appId != 0) {
					String newSORCFMDate = beanTmpNew.getSorCFMDate();
					String newSORDueDate = beanTmpNew.getSorDueDate();
					String newRemark = beanTmpNew.getRemark();
					String newChangeBy = beanTmpNew.getChangeBy();
					String newDataStatus = beanTmpNew.getApprovedDataStatus();
					listCSL = HandlerListLog.handlerListLog(listCSL, oldRemark, newRemark, mapMSC.get(Config.sqlFieldRemark),
							Integer.toString(appId), newChangeBy, Config.sqlFieldRemark,
							remarkAction + " ( " + newRemark + " ) ");
					listCSL = HandlerListLog.handlerListLog(listCSL, oldSORCFMDate, newSORCFMDate,
							mapMSC.get(Config.sqlFieldSORCFMDate), Integer.toString(appId), newChangeBy,
							Config.sqlFieldSORCFMDate, remarkAction + " ( " + newRemark + " ) ");
					listCSL = HandlerListLog.handlerListLog(listCSL, oldSORDueDate, newSORDueDate,
							mapMSC.get(Config.sqlFieldSORDueDate), Integer.toString(appId), newChangeBy,
							Config.sqlFieldSORDueDate, remarkAction + " ( " + newRemark + " ) ");
					listCSL = HandlerListLog.handlerListLog(listCSL, oldChangeBy, newChangeBy,
							mapMSC.get(Config.sqlFieldApprovedBy), Integer.toString(appId), newChangeBy,
							Config.sqlFieldApprovedBy, remarkAction + " ( " + newRemark + " ) ");
					listCSL = HandlerListLog.handlerListLog(listCSL, oldDataStatus, newDataStatus,
							mapMSC.get(Config.sqlFieldDataStatus), Integer.toString(appId), newChangeBy,
							Config.sqlFieldDataStatus, remarkAction + " ( " + newRemark + " ) ");
				}
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeApprovedPlanDateLog,
					Config.sqlFieldApprovedPlanDateId);
		}
		return iconStatus;
	}

	private String handlerChangeProdDetail(ArrayList<POManagementDetail> poList, String remarkAction) {
		String changeTable = Config.sqlTableChangeSORTempProdLog;
		String changeId = Config.sqlFieldSORTempProdId;

		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		HashMap<Integer, POManagementDetail> mapDateAndDataStatus = new HashMap<>();
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		String iconStatus = Config.C_SUC_ICON_STATUS;
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC = mscModel.getMasterSettingChangeDetail(changeTable);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		for (POManagementDetail beanTmp : poList) {
			int id = beanTmp.getTempProdId();
			mapDateAndDataStatus.put(id, beanTmp);
		}
		ArrayList<POManagementDetail> listOld = this.getPOManagementLotDetailByTempProdId(poList);
		for (POManagementDetail beanTmpOld : listOld) {
			int tempProdId = beanTmpOld.getTempProdId();
			POManagementDetail beanTmpNew = mapDateAndDataStatus.get(tempProdId);
			String oldGroupBegin = beanTmpOld.getGroupBegin();
			String oldProductionOrderQty = beanTmpOld.getProductionOrderQty();
			String oldChangeBy = beanTmpOld.getChangeBy();
			String oldRemark = beanTmpOld.getRemark();
			// Remark
			if (beanTmpNew != null) {
				if(tempProdId != 0) {
					String newGroupBegin = beanTmpNew.getNewGroupBegin();
					String newProductionOrderQty = beanTmpNew.getProductionOrderQty();
					String newChangeBy = beanTmpNew.getChangeBy();
					String newRemark = beanTmpNew.getRemark(); 
					listCSL = HandlerListLog.handlerListLog(listCSL, oldRemark, newRemark, mapMSC.get(Config.sqlFieldRemark),
							Integer.toString(tempProdId), newChangeBy, Config.sqlFieldRemark,
							remarkAction + " ( " + newRemark + " ) ");
					if(!newGroupBegin.equals(Config.C_BLANK)) {
						listCSL = HandlerListLog.handlerListLog(listCSL, oldGroupBegin, newGroupBegin,
								mapMSC.get(Config.sqlFieldGroupBegin), Integer.toString(tempProdId), newChangeBy,
								Config.sqlFieldGroupBegin, remarkAction + " ( " + newRemark + " ) ");
					}
					listCSL = HandlerListLog.handlerListLog(listCSL, oldProductionOrderQty, newProductionOrderQty,
							mapMSC.get(Config.sqlFieldProdOrderQty), Integer.toString(tempProdId), newChangeBy,
							Config.sqlFieldProdOrderQty, remarkAction + " ( " + newRemark + " ) ");
					listCSL = HandlerListLog.handlerListLog(listCSL, oldChangeBy, newChangeBy,
							mapMSC.get(Config.sqlFieldChangeBy), Integer.toString(tempProdId), newChangeBy,
							Config.sqlFieldChangeBy, remarkAction + " ( " + newRemark + " ) ");
				}
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, changeTable, changeId);
		}
		return iconStatus;
	}

	@Override
	public String handlerChangeSORPODetailWithDataStatus(ArrayList<POManagementDetail> poList, String remarkAction) {
		String changeTable = Config.sqlTableChangeSORPODetailLog;
		String changeId = Config.sqlFieldSORPODetailId;

		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		HashMap<Integer, POManagementDetail> mapDateAndDataStatus = new HashMap<>();
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		String iconStatus = Config.C_SUC_ICON_STATUS;
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC = mscModel.getMasterSettingChangeDetail(changeTable);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		for (POManagementDetail beanTmp : poList) {
			int id = beanTmp.getPoId();
			mapDateAndDataStatus.put(id, beanTmp);
		}
		ArrayList<POManagementDetail> listOld = this.getPOManagementDetailByPOId(poList);
		for (POManagementDetail beanTmpOld : listOld) {
			int id = beanTmpOld.getPoId();
			POManagementDetail beanTmpNew = mapDateAndDataStatus.get(id);
			String oldChangeBy = beanTmpOld.getChangeBy();
			int oldPOPuangId = beanTmpOld.getPoPuangId();
			String strOldPOPuangId = Integer.toString(oldPOPuangId);
			if (oldPOPuangId == 0) {
				strOldPOPuangId = "";
			}
			// Remark
			if (beanTmpNew != null) {
				String newDataStatus = beanTmpNew.getDataStatus();
				String newChangeBy = beanTmpNew.getChangeBy();
				int newPOPuangId = beanTmpNew.getPoPuangId();
				String strNewPOPuangId = Integer.toString(newPOPuangId);
				String newRemark = beanTmpNew.getRemark();
				if (newPOPuangId == 0) {
					strNewPOPuangId = "";
				}
				listCSL = HandlerListLog.handlerListLog(listCSL, Config.C_OPEN_STATUS, newDataStatus,
						mapMSC.get(Config.sqlFieldDataStatus), Integer.toString(id), newChangeBy, Config.sqlFieldDataStatus,
						remarkAction + " ( " + newRemark + " ) ");
				listCSL = HandlerListLog.handlerListLog(listCSL, oldChangeBy, newChangeBy, mapMSC.get(Config.sqlFieldChangeBy),
						Integer.toString(id), newChangeBy, Config.sqlFieldChangeBy, remarkAction + " ( " + newRemark + " ) ");
				listCSL = HandlerListLog.handlerListLog(listCSL, strOldPOPuangId, strNewPOPuangId,
						mapMSC.get(Config.sqlFieldPOPuangId), Integer.toString(id), newChangeBy, Config.sqlFieldPOPuangId,
						remarkAction + " ( " + newRemark + " ) ");
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, changeTable, changeId);
		}
		return iconStatus;
	}

	private String handlerChangeTempPlanningLotWithDataStatus(ArrayList<POManagementDetail> poList,
			String remarkAction) {
		String changeTable = Config.sqlTableChangeTempPlanningLotLog;
		String changeId = Config.sqlFieldTempPlannnigLotId;

		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		HashMap<Integer, POManagementDetail> mapDateAndDataStatus = new HashMap<>();
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		String iconStatus = Config.C_SUC_ICON_STATUS;
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC = mscModel.getMasterSettingChangeDetail(changeTable);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		for (POManagementDetail beanTmp : poList) {
			int id = beanTmp.getTempPlanningId();
			mapDateAndDataStatus.put(id, beanTmp);
		}
		ArrayList<POManagementDetail> listOld = this.getPOManagementLotDetailByTempProdId(poList);
		for (POManagementDetail beanTmpOld : listOld) {
			int id = beanTmpOld.getTempPlanningId();
			POManagementDetail beanTmpNew = mapDateAndDataStatus.get(id);
			@SuppressWarnings("unused") String oldDataStatus = beanTmpOld.getDataStatus();
			String oldChangeBy = beanTmpOld.getChangeBy();
			// Remark
			if (beanTmpNew != null) {
				String newDataStatus = beanTmpNew.getDataStatus();
				String newChangeBy = beanTmpNew.getChangeBy();
				String newRemark = beanTmpNew.getRemark(); 
				listCSL = HandlerListLog.handlerListLog(listCSL, Config.C_OPEN_STATUS, newDataStatus,
						mapMSC.get(Config.sqlFieldDataStatus), Integer.toString(id), newChangeBy, Config.sqlFieldDataStatus,
						remarkAction + " ( " + newRemark + " ) ");
				listCSL = HandlerListLog.handlerListLog(listCSL, oldChangeBy, newChangeBy, mapMSC.get(Config.sqlFieldChangeBy),
						Integer.toString(id), newChangeBy, Config.sqlFieldChangeBy, remarkAction + " ( " + newRemark + " ) ");
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, changeTable, changeId);
		}
		return iconStatus;
	}

	private String handlerChangSORTempProdWithDataStatus(ArrayList<POManagementDetail> poList, String remarkAction) {
		String changeTable = Config.sqlTableChangeSORTempProdLog;
		String changeId = Config.sqlFieldSORTempProdId;

		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		HashMap<Integer, POManagementDetail> mapDateAndDataStatus = new HashMap<>();
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		String iconStatus = Config.C_SUC_ICON_STATUS;
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC = mscModel.getMasterSettingChangeDetail(changeTable);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		// GET NEW DATE
		for (POManagementDetail beanTmp : poList) {
			int id = beanTmp.getTempProdId();
			mapDateAndDataStatus.put(id, beanTmp);
		}
		ArrayList<POManagementDetail> listOld = this.getPOManagementLotDetailByTempProdId(poList);
		for (POManagementDetail beanTmpOld : listOld) {
			int tempProdId = beanTmpOld.getTempProdId();
			POManagementDetail beanTmpNew = mapDateAndDataStatus.get(tempProdId);
//			String oldDataStatus = beanTmpOld.getDataStatus();
			String oldChangeBy = beanTmpOld.getChangeBy();
			// Remark
			if (beanTmpNew != null) {
				String newDataStatus = beanTmpNew.getDataStatus();
				String newChangeBy = beanTmpNew.getChangeBy();
				String newRemark = beanTmpNew.getRemark();
				listCSL = HandlerListLog.handlerListLog(listCSL, Config.C_OPEN_STATUS, newDataStatus,
						mapMSC.get(Config.sqlFieldDataStatus), Integer.toString(tempProdId), newChangeBy,
						Config.sqlFieldDataStatus, remarkAction + " ( " + newRemark + " ) ");
				listCSL = HandlerListLog.handlerListLog(listCSL, oldChangeBy, newChangeBy, mapMSC.get(Config.sqlFieldChangeBy),
						Integer.toString(tempProdId), newChangeBy, Config.sqlFieldChangeBy,
						remarkAction + " ( " + newRemark + " ) ");
			}
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, changeTable, changeId);
		}
		return iconStatus;
	}

	public void processVolumeForReport(ArrayList<POManagementDetail> poList) {
		RelationPOAndProdOrderModel rpapModel = new RelationPOAndProdOrderModel(this.conType);
		ReportModel rModel = new ReportModel(this.conType);
		ArrayList<PlanningReportDetail> list = null;
		if(!poList.isEmpty()) {
			list = rModel.getDetailForVolumeDetailByPOId(poList);
			if ( ! list.isEmpty()) {
				list = rModel.handlerVolumeReport(list);
				rpapModel.upsertVolumeForRelationPOAndProdOrder(list);
			}
		}
	}

	@Override
	public ArrayList<POManagementDetail> reverseProdOrder(ArrayList<POManagementDetail> poList, String remarkAction) {
		String changeTable = Config.sqlTableChangeSORTempProdLog;
		String changeId = Config.sqlFieldSORTempProdId;
		ProdOrderRunningModel porModel = new ProdOrderRunningModel(this.conType);
		BackGroundJobModel bgjModel = new BackGroundJobModel(this.conType);

		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		String changeBy = poList.get(0).getChangeBy();
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "Update Success.";
		ArrayList<POManagementDetail> listOld = this.getPOManagementLotDetailByPOId(poList);
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		HashMap<String, String> mapMSC = new HashMap<>();
		// CheckProdOrder 
		if (listOld.size() > 0) {
			// PROCESS REVERSE

			// 1. CHECK LOT ALREADY CREATE IN SAP
			for (POManagementDetail beanCurrent : listOld) {
				// 1. CHECK LOT ALREADY CREATE IN SAP
				if (beanCurrent.isInSap()) {
					iconStatus = "E1";
					systemStatus = "Already create in SAP";
					break;
				}
			}
			if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) {

				ArrayList<MasterSettingChangeDetail> listMSC = mscModel.getMasterSettingChangeDetail(changeTable);
				for (MasterSettingChangeDetail beanTemp : listMSC) {
					mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
				}

				String charCheck = "";
				ArrayList<ProdOrderRunningDetail> listRunningUsed = new ArrayList<>();
				for (int i = 0; i < listOld.size(); i ++ ) {
					listRunningUsed.clear();
					POManagementDetail beanCurrent = listOld.get(i);
					int tempProdId = beanCurrent.getTempProdId();
					String curProdOrder = beanCurrent.getProductionOrder();
					String newProdOrder = "";
					String newProdOrderFull = "";
//					isLotTemp = false;
					// CHECK LOT TEMP [ CHECK INSIDE BCOZ GROUP DOUBLE TO SINGLE CASE ]
					if (this.sshUtl.isNumeric(curProdOrder.substring(0, 1))) {
//						isLotTemp = true;
						charCheck = curProdOrder.substring(1, 2);
						charCheck = this.tryReverseProdOrder(charCheck);
						// REPLACED TEMP LOT
						newProdOrder = curProdOrder.replace(curProdOrder.substring(1, 2), charCheck);
					} else {
						charCheck = curProdOrder.substring(0, 1);
						charCheck = this.tryReverseProdOrder(charCheck);
						// FIND LASTESTRUNNING
						newProdOrder = curProdOrder.replace(curProdOrder.substring(0, 1), charCheck);
						String sevenToEightPos = newProdOrder.substring(6, newProdOrder.length());
						String keyWord = newProdOrder.substring(0, 3);
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
//						newProdOrder = runningNewProdOrder+ newProdOrder.substring(6, newProdOrder.length());
						newProdOrderFull = runningNewProdOrder
								+sevenToEightPos;
						beanTmpRunning.setDataStatus(Config.C_CLOSE_STATUS);
						beanTmpRunning.setProductionOrderTemp(curProdOrder);
						beanTmpRunning.setProductionOrder(runningNewProdOrder);
						beanTmpRunning.setTopping(isTopping);
						beanTmpRunning.setRemark("REVERSE : " + curProdOrder + " => " + newProdOrderFull);
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
						cslModel.handlerChangeProdDetailForNewDetailList(listRunning, Config.C_ACTION_TEXT_UPDATE_07);
						porModel.updateProdOrderRunningWithProductionOrder(listRunningUsed);
					}
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

			}
			poList = this.getPOManagementLotDetailByPOId(poList);
			if (poList.isEmpty()) {
				POManagementDetail beanTemp = new POManagementDetail();
				poList.add(beanTemp);
			}
		} else {
			poList.clear();
			POManagementDetail beanTemp = new POManagementDetail();
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = SqlErrorHandler.handlerSqlErrorText(iconStatus);
			poList.add(beanTemp);
		}
		poList.get(0).setIconStatus(iconStatus);
		poList.get(0).setSystemStatus(systemStatus);
		return poList;
	}

	private String tryReverseProdOrder(String currentChar) {
		String newChar = "C";
		if (currentChar.equals("C")) {
			newChar = "S";
		}
		return newChar;
	}

}