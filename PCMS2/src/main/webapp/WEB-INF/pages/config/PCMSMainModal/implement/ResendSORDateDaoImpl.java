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
import controller.setting.SORAPIController;
import dao.ResendSORDateDao;
import entities.ChangeSettingLogDetail;
import entities.InputApprovedDetail;
import entities.MasterSettingChangeDetail;
import entities.ProdOrderRunningDetail;
import entities.ResendSORDateDetail;
import model.BackGroundJobModel;
import model.BeanCreateModel;
import model.master.ApprovedPlanDateModel;
import model.master.ChangeSettingLogModel;
import model.master.MasterSettingChangeModel;
import model.master.ProdOrderRunningModel;
import resources.Config;
import th.in.totemplate.core.sql.Database;

public class ResendSORDateDaoImpl implements ResendSORDateDao {
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;

	private String conType;
	private String message;
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	public DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String select =
			""
					+ "  SPD.[ReferenceId] \r\n"
					+ " ,SPD.[Id] as [POId]\r\n"
					+ " ,SPD.[PO]\r\n"
					+ " ,SPD.[POLine] \r\n"
					+ " ,cd.[CustomerNo]\r\n"
					+ "			 ,CASE	\r\n"
					+ "				WHEN PNPI.POType IS NOT NULL THEN 'MAIN'\r\n"
					+ "				ELSE 'POSub Puang'\r\n"
					+ "				END AS POType\r\n"
					+ "			 ,PNPI.[CurrentTempLot]\r\n"
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
					+ " ,APD.[DataStatus] AS ApprovedDataStatus\r\n";

	private String selectLot =
			""
					+ "  SPD.[ReferenceId] \r\n"
					+ " ,SPD.[Id] as [POId]\r\n"
					+ " ,SPD.[PO]\r\n"
					+ " ,SPD.[POLine] \r\n"
					+ " ,cd.[CustomerNo]\r\n"
					+ " ,PNPI.[ProductionOrder]\r\n"
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
					+ " ,APD.[DataStatus] AS ApprovedDataStatus\r\n"
					+ "		,CAST (\r\n"
					+ "			CASE \r\n"
					+ "				WHEN DFS.[ProductionOrderForCheck] IS NULL THEN 0\r\n"
					+ "				ELSE 1\r\n"
					+ "			END \r\n"
					+ "			AS BIT\r\n"
					+ "		 ) AS IsInSap\r\n";

	private String innerJoinAPD =
			""
					+ "	INNER JOIN [PPMM].[dbo].[ApprovedPlanDate] AS APD ON APD.[POId] = SPD.[Id] and\r\n"
					+ "													     APD.[DataStatus] = 'O' and\r\n"
					+ "                                                      APD.[SorDueDate] is not null\r\n";
	private String leftJoinSPDMainCD =
			""
					+ "	left join ( \r\n"
					+ "     	SELECT \r\n"
					+ "			[Id] \r\n"
					+ "    		,SUBSTRING([CustomerNo], PATINDEX('%[^0]%', [CustomerNo]+'.'), LEN([CustomerNo]))   as [CustomerNo] \r\n"
					+ "    		,[CustomerShortName] \r\n"
					+ "    		,[ChangeDate] \r\n"
					+ "    		,[CreateDate] \r\n"
					+ "    	FROM [PCMS].[dbo].[CustomerDetail] \r\n"
					+ "	) as cd on cd.[CustomerNo] = SPD.[CustomerNo] 	\r\n";
	private String innerJoinSPDMainRPAP =
			""
					+ " inner JOIN (\r\n"
					+ "	 SELECT DISTINCT \r\n"
					+ "			A.[POId]\r\n"
					+ "		   ,1 as IsInProcess\r\n"
					+ "	  FROM [PPMM].[dbo].[RelationPOAndProdOrder] AS A\r\n"
					+ "	  INNER JOIN [PPMM].[dbo].[viewTEMPPlanningLotOnProcess] AS B ON A.[ProductionOrder] = B.[ProductionOrder]\r\n"
					+ "	  where A.[DataStatus] = 'O'  \r\n"
					+ " ) AS RPAP ON RPAP.[POId] = SPD.[Id]\r\n";

	private String leftJoinCheckPNPI = ""
					+ " LEFT JOIN (\r\n"
					+ "		select DISTINCT \r\n"
					+ "		  a.[Id] as TempProdId\r\n"
					+ "		, a.[POId]  as POId\r\n"
					+ "		, 'POMain' as POType \r\n"
					+ "		, SUBSTRING(a.[ProductionOrder], 0, 7) as ProductionOrderForCheck\r\n"
					+ "		, a.[ProductionOrder] \r\n"
					+ "		from [PPMM].[dbo].[SOR_TempProd] as a  \r\n"
					+ "		where ( a.POId is not null ) and \r\n"
					+ "		  a.DataStatus = 'O' and\r\n"
					+ "		  ( a.[ProductionOrder] NOT LIKE 'F%' )  \r\n"
					+ " )  AS PNPI ON SPD.[Id] = PNPI.[POId]\r\n" ;
	private String leftJoinPNPI = ""
			+ " LEFT JOIN (\r\n"
			+ "		select DISTINCT "
			+ "       a.[POId] as POId\r\n"
			+ "		, 'POMain' as POType \r\n"
			+ "		, SUBSTRING(a.[ProductionOrder], 1,3)+'000'+SUBSTRING(a.[ProductionOrder], 7, 2)  AS CurrentTempLot\r\n"
			+ "		from [PPMM].[dbo].[SOR_TempProd] as a  \r\n"
			+ "		where ( a.POId is not null ) and \r\n"
			+ "				a.DataStatus = 'O' and\r\n"
			+ "		  		( a.[ProductionOrder] NOT LIKE 'F%' )\r\n"
			+ " )  AS PNPI ON SPD.[Id] = PNPI.[POId]\r\n" ;
	private String leftJoinDFS =
			""
					+ "	left join ( select distinct SUBSTRING([ProductionOrder] ,0,7) AS ProductionOrderForCheck FROM [PPMM].[dbo].[DataFromSap] ) AS DFS ON  PNPI.ProductionOrder =  DFS.[ProductionOrderForCheck]\r\n";

	public ResendSORDateDaoImpl(Database database , String conType) {
		this.database = database ;
		this.message = "";
		this.conType = conType;
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<ResendSORDateDetail> getResendSORDateDetailByPO(ArrayList<ResendSORDateDetail> poList)
	{
		ArrayList<ResendSORDateDetail> list = null;
		String where = "";
		ResendSORDateDetail bean = poList.get(0);
		String po = bean.getPo();
		if ( ! po.equals("")) {
			where = " where SPD.[PO] = '" + po + "' and \r\n"
				  + "       SPD.[DataStatus] = 'O' \r\n";
		}
		String sql =
				"" 
			+ " select \r\n"
			+ this.select
			+ " FROM [PPMM].[dbo].[SOR_PODetail] AS SPD \r\n "
			+ this.innerJoinAPD
			+ this.leftJoinPNPI
			+ this.leftJoinSPDMainCD
			+ this.innerJoinSPDMainRPAP
			+ where
			+ " Order By SPD.[PO] ,SPD.[POLine] ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genResendSORDateDetail(map));
		}
		return list;
	}

	public ArrayList<ResendSORDateDetail> getResendSORDateDetailByPOId(ArrayList<ResendSORDateDetail> poList)
	{
		ArrayList<ResendSORDateDetail> list = null;
		String where = "";
		ResendSORDateDetail bean = poList.get(0);
		int poId = bean.getPoId();
		where = " where SPD.[Id] = '" + poId + "' and \r\n"
		      + "       SPD.[DataStatus] = 'O' ";
		String sql =
				"" 
				+ " select \r\n"
				+ this.select
				+ " FROM [PPMM].[dbo].[SOR_PODetail] AS SPD \r\n "
				+ this.innerJoinAPD
				+ this.leftJoinPNPI
				+ this.leftJoinSPDMainCD
				+ this.innerJoinSPDMainRPAP
				+ where
				+ " Order By SPD.[PO] ,SPD.[POLine] "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genResendSORDateDetail(map));
		}
		return list;
	}

	public ArrayList<ResendSORDateDetail> getResendSORDateLotDetailByPOId(ArrayList<ResendSORDateDetail> poList)
	{
		ArrayList<ResendSORDateDetail> list = null;
		String where = "";
		ResendSORDateDetail bean = poList.get(0);
		int poId = bean.getPoId();
		where = ""
			+ " where SPD.[Id] = '" + poId + "' and \r\n"
			+ "       SPD.[DataStatus] = 'O' \r\n";
		String sql =
				""
//						+ this.declareLotPNPI
						+ " select \r\n"
						+ this.selectLot
						+ " FROM [PPMM].[dbo].[SOR_PODetail] AS SPD \r\n "
						+ this.innerJoinAPD  
						+ this.leftJoinCheckPNPI
						+ this.leftJoinSPDMainCD
						+ this.innerJoinSPDMainRPAP
						+ this.leftJoinDFS
						+ where
						+ " Order By SPD.[PO] ,SPD.[POLine] "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genResendSORDateDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<ResendSORDateDetail> getViewTEMPPlanningLotOnProcess()
	{
		ArrayList<ResendSORDateDetail> list = null;
		String sql =
				""  
				+ " SELECT DISTINCT a.[PO] \r\n"
				+ " FROM ( \r\n" 
				+ "		SELECT DISTINCT COALESCE( SPDPUANG.[Id] ,SPD.[Id])  as POId , \r\n"
				+ "						COALESCE( SPDPUANG.[PO] ,SPD.[PO])  as PO ,\r\n"
				+ "                     COALESCE( SPDPUANG.[POLine] ,SPD.[POLine])  as POLine \r\n"
				+ "		FROM [PPMM].[dbo].[viewTEMPPlanningLotOnProcess] as vtplop  \r\n"
				+ "		LEFT JOIN [PPMM].[dbo].[SOR_PODetail] AS spd on spd.[Id] = vtplop.[POId]\r\n"
				+ "		LEFT JOIN [PPMM].[dbo].[SOR_PODetail] AS SPDPUANG ON SPD.[POPuangId] = SPDPUANG.[POPuangId]  \r\n"
				+ "		WHERE POID IS NOT NULL and \r\n"
				+ "			(\r\n"
				+ "				( SPD.[DataStatus] = 'O' OR SPD.[DataStatus] is null) or \r\n"
				+ "				( SPDPUANG.[DataStatus] = 'O' )\r\n"
				+ "			)  \r\n"
				+ "	) AS A\r\n"
				+ " INNER JOIN [PPMM].[dbo].[ApprovedPlanDate] AS APD ON APD.[POId] = A.[POId] and \r\n"
				+ "													     APD.[DataStatus] = 'O' and\r\n"
				+ "                                                      APD.[SorDueDate] is not null\r\n"
				+ " Order By A.[PO]  "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genResendSORDateDetail(map));
		}
		return list;
	}

	private String handlerChangeApprovedPODetail(ArrayList<ResendSORDateDetail> listPOMain, String remarkAction)
	{
		MasterSettingChangeModel mscModel = new MasterSettingChangeModel(this.conType);
		ChangeSettingLogModel cslModel = new ChangeSettingLogModel(this.conType);
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		String iconStatus = Config.C_SUC_ICON_STATUS;
		HashMap<String, String> mapMSC = new HashMap<>();
		ArrayList<MasterSettingChangeDetail> listMSC =
				mscModel.getMasterSettingChangeDetail(Config.sqlTableChangeApprovedPlanDateLog);
		for (MasterSettingChangeDetail beanTemp : listMSC) {
			mapMSC.put(beanTemp.getFieldName(), beanTemp.getId());
		}
		for (ResendSORDateDetail beanTmpOld : listPOMain) {
			int appId = beanTmpOld.getApprovedPlanDateId();
			String oldSORCFMDate = beanTmpOld.getCurrentSorCFMDate();
			String oldSORDueDate = beanTmpOld.getCurrentSorDueDate();
			String oldChangeBy = beanTmpOld.getApprovedBy();
			// Remark
			String newSORCFMDate = beanTmpOld.getNewSorCFMDate();
			String newSORDueDate = beanTmpOld.getNewSorDueDate();
			String newChangeBy = beanTmpOld.getChangeBy();
			listCSL =
					HandlerListLog.handlerListLog(listCSL, oldSORCFMDate, newSORCFMDate,
							mapMSC.get(Config.sqlFieldSORCFMDate), Integer.toString(appId), newChangeBy,
							Config.sqlFieldSORCFMDate, remarkAction);
			listCSL =
					HandlerListLog.handlerListLog(listCSL, oldSORDueDate, newSORDueDate,
							mapMSC.get(Config.sqlFieldSORDueDate), Integer.toString(appId), newChangeBy,
							Config.sqlFieldSORDueDate, remarkAction);
			listCSL =
					HandlerListLog.handlerListLog(listCSL, oldChangeBy, newChangeBy,
							mapMSC.get(Config.sqlFieldApprovedBy), Integer.toString(appId), newChangeBy,
							Config.sqlFieldApprovedBy, remarkAction);
		}
		if ( ! listCSL.isEmpty()) {
			cslModel.insertChangeSettingLogDetail(listCSL, Config.sqlTableChangeApprovedPlanDateLog,
					Config.sqlFieldApprovedPlanDateId);
		}
		return iconStatus;
	}

	public String handlerReplaceTempProdWithReal(ArrayList<ResendSORDateDetail> listPOMain,
			ArrayList<ResendSORDateDetail> listOldPOLot, String remarkAction)
	{
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
		String iconStatus = Config.C_SUC_ICON_STATUS;
//		String systemStauts = "Update Success.";
		ArrayList<ChangeSettingLogDetail> listCSL = new ArrayList<>();
		HashMap<String, String> mapMSC = new HashMap<>();
		// CheckProdOrder 
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
				newProdOrderFull =
						runningNewProdOrder
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
				beanTmpCurrent.setProductionOrderTemp("");
				beanTmpCurrent.setProductionOrder(curProdOrder.substring(0, 6));
				beanTmpCurrent.setTopping(false);
				beanTmpCurrent.setRemark("");
				beanTmpCurrent.setChangeBy(changeBy);
				listRunningUsed.add(beanTmpCurrent); 
				cslModel.handlerChangeProdDetailForNewDetailList(listRunning, remarkAction);
				porModel.updateProdOrderRunningWithProductionOrder(listRunningUsed);
				bgjModel.execReplacedProdOrderOldWithNew(curProdOrder, newProdOrderFull);

				// LOG CHANGE
				listCSL =
						HandlerListLog.handlerListLog(listCSL, curProdOrder, newProdOrderFull,
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
	public ArrayList<ResendSORDateDetail> handlerResendDataToSOR(ArrayList<ResendSORDateDetail> listPOMain)
	{

		listPOMain = this.handlerLogicSendToSOR(listPOMain, true );
		return listPOMain;
	}

	@Override
	public ArrayList<ResendSORDateDetail> handlerDueDataOnly(ArrayList<ResendSORDateDetail> listPOMain)
	{
		listPOMain = this.handlerLogicSendToSOR(listPOMain, false) ;
		return listPOMain;
	}
	private ArrayList<ResendSORDateDetail> handlerLogicSendToSOR (ArrayList<ResendSORDateDetail> listPOMain 
			, boolean isSendToSor ){
		// TODO Auto-generated method stub
		SORAPIController sorApiController = new SORAPIController();
		ApprovedPlanDateModel apdModel = new ApprovedPlanDateModel(this.conType);
		String iconStatus = Config.C_SUC_ICON_STATUS;
		String systemStatus = "Update Success.";
		String sorSystemStatus = "Update Success.";
		ArrayList<InputApprovedDetail> listApproved = new ArrayList<>();

		ResendSORDateDetail beanMain = listPOMain.get(0);
		String changeBy = beanMain.getChangeBy();
		String newSorDueDate = beanMain.getNewSorDueDate().trim();
//		String curSorDueDate = beanMain.getCurrentSorDueDate().trim();
		String newSorCFMDate = beanMain.getNewSorCFMDate().trim();
//		String curSorCFMDate = beanMain.getCurrentSorCFMDate() .trim();
		String curLotTemp = beanMain.getCurrentLotTemp().trim();
		String newLotTemp = beanMain.getNewLotTemp().trim();
		ArrayList<ResendSORDateDetail> listBaseLot = this.getResendSORDateLotDetailByPOId(listPOMain);
		if (listBaseLot.isEmpty()) {
			// ERROR
			iconStatus = Config.C_ERR_ICON_STATUS;
			systemStatus = Config.C_ERROR_TEXT;
		} else {
			boolean isInSap = false;
			for (ResendSORDateDetail beanBase : listBaseLot) {
				beanBase.setChangeBy(changeBy);
				if (beanBase.isInSap()) {
					isInSap = true;
					break;
				}
			}
			// 1. CHECK LOT DIDN'T CREATE IN SAP
			if (isInSap) {
				// ERROR
				iconStatus = Config.C_ERR_ICON_STATUS;
				systemStatus = "Some Prod.Order already create in SAP.";
			} else {
				// TRY SEND TO SOR
				InputApprovedDetail beanTmp = new InputApprovedDetail();
				beanTmp.setPoId(beanMain.getPoId());
				beanTmp.setPo(beanMain.getPo());
				beanTmp.setPoLine(beanMain.getPoLine());
				beanTmp.setMaterialNo(beanMain.getMaterialNo());
				beanTmp.setSorDueDate(newSorDueDate);
				beanTmp.setSorCFMDate(newSorCFMDate);
				beanTmp.setApprovedBy(changeBy);
				beanTmp.setDataStatus(Config.C_OPEN_STATUS);
				beanTmp.setReferenceId(beanMain.getReferenceId());
				listApproved.add(beanTmp);
				  
				if(isSendToSor) {
					InputApprovedDetail beanSOR = sorApiController.updateProductionDue(listApproved);
					iconStatus = beanSOR.getIconStatus();
					sorSystemStatus = beanSOR.getSystemStatus(); 
					if (iconStatus.equals(Config.C_SUC_ICON_STATUS)) { 
						systemStatus = sorSystemStatus;
						// APPROVED
						this.handlerChangeApprovedPODetail(listPOMain, Config.C_ACTION_TEXT_SOR_UPDATE_01);
						apdModel.upsertApprovedPlanDate(listApproved); 
						if (newLotTemp.equals(curLotTemp)) {

						} else {
							this.handlerReplaceTempProdWithReal(listPOMain, listBaseLot,
									Config.C_ACTION_TEXT_SOR_UPDATE_01);
						}
					} else {
						iconStatus = Config.C_ERR_ICON_STATUS;
						systemStatus += "PPMM2 failed to send some data to SOR.\n ( " + sorSystemStatus + " ) ";
					}
				}
				else {   
					this.handlerChangeApprovedPODetail(listPOMain, Config.C_ACTION_TEXT_UPDATE_18);
					iconStatus = apdModel.upsertApprovedPlanDate(listApproved); 
					if(iconStatus.equals(Config.C_SUC_ICON_STATUS)) {
						if (newLotTemp.equals(curLotTemp)) {
	
						} else {
							this.handlerReplaceTempProdWithReal(listPOMain, listBaseLot,
									Config.C_ACTION_TEXT_UPDATE_18);
						}  
					} 
					else {
						systemStatus = SqlErrorHandler.handlerSqlErrorText(iconStatus);
					}
				} 

			}
		}
		listPOMain = this.getResendSORDateDetailByPOId(listPOMain);
		if (listPOMain.isEmpty()) {
			ResendSORDateDetail beanTemp = new ResendSORDateDetail();
			listPOMain.add(beanTemp);
		}
		listPOMain.get(0).setIconStatus(iconStatus);
		listPOMain.get(0).setSystemStatus(systemStatus); 
		return listPOMain;
	}

}