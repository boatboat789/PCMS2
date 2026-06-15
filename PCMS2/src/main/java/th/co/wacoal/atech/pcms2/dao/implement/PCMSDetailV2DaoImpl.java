package th.co.wacoal.atech.pcms2.dao.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.PCMSDetailV2Dao;
import th.co.wacoal.atech.pcms2.entities.DataTableResponse;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.service.BackGroundJobService;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.service.PCMSSearchService;
import th.co.wacoal.atech.pcms2.service.PCMSSqlService;
import th.co.wacoal.atech.pcms2.service.PCMSSqlServiceV2;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainProdService;
import th.co.wacoal.atech.pcms2.service.master.PlanCFMDateService;
import th.co.wacoal.atech.pcms2.service.master.PlanCFMLabDateService;
import th.co.wacoal.atech.pcms2.service.master.PlanDeliveryDateService;
import th.co.wacoal.atech.pcms2.service.master.ReplacedProdOrderService;
import th.co.wacoal.atech.pcms2.service.master.SearchSettingService;
import th.co.wacoal.atech.pcms2.service.master.SwitchProdOrderService;
import th.co.wacoal.atech.pcms2.service.master.TEMP_UserStatusAutoService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class PCMSDetailV2DaoImpl implements PCMSDetailV2Dao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private PCMSSqlService pss = new PCMSSqlService();
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private String C_PRODORDER = "ProductionOrder";
	private String C_PRODORDERRP = "ProductionOrderRP";
	private BeanCreateService bcModel = new BeanCreateService();  
 
	
	private String selectTempAllProdBase = ""
			+ "  a.SaleOrder,\r\n"
			+ "   a.[SaleLine] ,\r\n"
			+ "   tms.Division,\r\n"
			+ "   tms.CustomerShortName,	 \r\n"
			+ "   tms.SaleCreateDate,\r\n"
			+ "   tms.PurchaseOrder,\r\n"
			+ "   tms.MaterialNo,\r\n"
			+ "   tms.CustomerMaterial,\r\n"
			+ "   tms.Price,\r\n"
			+ "   tms.SaleUnit,\r\n"
			+ "   tms.OrderAmount,\r\n"
			+ "   tms.SaleQuantity,\r\n"
			+ "   tms.RemainQuantity,\r\n"
			+ "   tms.RemainAmount,\r\n"
			+ "   fsmp.TotalQuantity,\r\n"
			+ "   a.Grade,\r\n"
			+ "   case\r\n"
			+ "    WHEN tms.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity\r\n"
			+ "    WHEN tms.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity\r\n"
			+ "    ELSE FSMBB.BillSendMRQuantity\r\n"
			+ "    end AS BillSendQuantity,\r\n"
			+ "   FSMBB.BillSendWeightQuantity,\r\n"
			+ "   FSMBB.BillSendMRQuantity,\r\n"
			+ "   FSMBB.BillSendYDQuantity,\r\n"
			+ "   tms.CustomerDue,\r\n"
			+ "   tms.DueDate,\r\n"
			+ "   a.ProductionOrder as ProductionOrder,\r\n"
			+ "   a.LotNoValue as LotNo,\r\n"
			+ "   fsmp.LabNo,\r\n"
			+ "   fsmp.LabStatus,\r\n"
			+ "   e.CFMPlanLabDate,\r\n"
			+ "   g.CFMActualLabDate,\r\n"
			+ "   g.CFMCusAnsLabDate,\r\n"
			+ "   a.UserStatus ,\r\n"
			+ "   coalesce ( TAPP.SORCFMDate ,j.CFMDate ) AS TKCFM, \r\n"
			+ "   g.CFMPlanDate ,  \r\n"
			+ "   g.SendCFMCusDate,\r\n"
			+ "\r\n"
			+ "   CASE \r\n"
			+ "		WHEN h.[ProductionOrder] is not null THEN H.DeliveryDate \r\n"
			+ "		ELSE fsmp.CFTYPE \r\n"
			+ "		END AS DeliveryDate , \r\n"
			+ "   g.CFMDateActual,\r\n"
			+ "   g.CFMDetailAll, \r\n"
			+ "   g.CFMNumberAll,  \r\n"
			+ "   g.CFMRemarkAll, \r\n"
			+ "   g.RollNoRemarkAll , \r\n"
			+ "   tms.ShipDate,\r\n"
			+ "   fsmp.RemarkOne,\r\n"
			+ "   fsmp.RemarkTwo,\r\n"
			+ "   fsmp.RemarkThree ,\r\n"
			+ "   k.ReplacedRemark ,\r\n"
			+ "   l.StockRemark,\r\n"
			+ "   a.GRSumKG,\r\n"
			+ "   a.GRSumYD,\r\n"
			+ "   a.GRSumMR,\r\n"
			+ "   g.DyePlan, \r\n"
			+ "   g.DyeActual,  \r\n"
			+ "   P.PCRemark, \r\n"
			+ "   InputDD.[DelayedDep], \r\n"
			+ "   InputCOD.[CauseOfDelay], \r\n"
			+ "   q.[SwitchRemark],\r\n"
			+ "   SL.[StockLoad], \r\n"
			+ "   fsmp.[PrdCreateDate],\r\n"
			+ "   fsmp.LotShipping, \r\n"
			+ "   CASE\r\n"
			+ "		WHEN a.Grade = 'A' OR a.Grade is null THEN  a.Volumn\r\n"
			+ "		ELSE  NULL\r\n"
			+ "		END AS Volumn, \r\n"
			+ "   CASE\r\n"
			+ "		WHEN a.Grade = 'A' OR a.Grade is null THEN tms.Price *  a.Volumn \r\n"
			+ "		ELSE  NULL\r\n"
			+ "		END AS VolumnFGAmount  , \r\n"
			+ "   a.TypePrd ,\r\n"
			+ "   a.TypePrdRemark ,\r\n"
			+ "   g.[DyeStatus],\r\n"
			+ "   tms.[CustomerMaterialBase] ,\r\n"
			+ "   fsmp.ArticleFG,\r\n"
			+ "   tms.Color\n";
 
	private final Database database;
    
    private final PCMSSearchService psService;
    
    private final PlanCFMDateService planCFMDateService;
    private final PlanDeliveryDateService planDeliveryDateService;
    private final PlanCFMLabDateService planCFMLabDateService;
    private final PCMSSqlServiceV2 pCMSSqlServiceV2;

    @Autowired
    public PCMSDetailV2DaoImpl(
            @Qualifier("pcmsDatabase") Database database,
            BackGroundJobService bgjService,
            PCMSSearchService psService, 
            // Services เพิ่มเติมที่เคย new ไว้
            FromSapMainProdService fromSapMainProdService,
            SearchSettingService searchSettingService,
            SwitchProdOrderService switchProdOrderService,
            ReplacedProdOrderService replacedProdOrderService,
            PlanCFMDateService planCFMDateService,
            PlanDeliveryDateService planDeliveryDateService,
            PlanCFMLabDateService planCFMLabDateService,
            TEMP_UserStatusAutoService tusaService, PCMSSqlServiceV2 pCMSSqlServiceV2) {

        this.database = database;
        this.psService = psService;
        this.planCFMDateService = planCFMDateService;
        this.planDeliveryDateService = planDeliveryDateService;
        this.planCFMLabDateService = planCFMLabDateService;
		this.pCMSSqlServiceV2 = pCMSSqlServiceV2;
    }
	@Override
	public ArrayList<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList)
	{
 
		ArrayList<PCMSSecondTableDetail> list = null;
		PCMSTableDetail bean = poList.get(0);
		List<String> userStatusList = bean.getUserStatusList();
		Map<String, String> results = pss.buildWhereClauses(bean);
		String whereSale = results.get("whereSale");	
		String whereProdFinal = results.get("whereProdFinal"); 
		String createTempTableUserStatus = ""
				+ psService.handlerTempTableUserStatusList(userStatusList);
		String createCusListSearch = ""
			+ psService.handlerTempTableCustomerSearchList(bean.getCustomerNameList(), bean.getCustomerShortNameList());
		String sql = ""
			+ createTempTableUserStatus
			+ createCusListSearch
			+ this.pCMSSqlServiceV2.finalIntoTempMainSale(whereSale) 
			+ this.pCMSSqlServiceV2.createTempPlanDeliveryDate
			+ this.pCMSSqlServiceV2.createTempSumGR
			+ this.pCMSSqlServiceV2.createTempSumBill
			+ this.pCMSSqlServiceV2.createTempBillBatchFlag
			+ this.pCMSSqlServiceV2.createTempSumVolOP
			+ this.pCMSSqlServiceV2.createTempSumVolRP
			+ this.pCMSSqlServiceV2.createTempSaleAgg
			+ this.pCMSSqlServiceV2.createTempProdAgg
			+ this.pCMSSqlServiceV2.createTempFlagHasRP
			+ this.pCMSSqlServiceV2.createTempFlagHasOP
			+ this.pCMSSqlServiceV2.finalIntoTempAllProdBase(whereProdFinal)
			+ " select \n "
			+ this.selectTempAllProdBase
			+ " FROM #tempAllProdBase A \n"
			+ " JOIN #tempMainSale tms on a.SaleOrder = tms.SaleOrder and a.SaleLine = tms.SaleLine \n"
			+ this.pCMSSqlServiceV2.getLeftJoinFromSapMainProd("a") 
			+ this.pCMSSqlServiceV2.getLeftJoinPlanCFMLabDate("a", "a") 
			+ this.pCMSSqlServiceV2.buildLeftJoinTempProdWorkDate("a") 
			+ this.pCMSSqlServiceV2.buildLeftJoinSCC("a") 
			+ this.pCMSSqlServiceV2.getLeftJoinTempPlandeliveryDate("a", "a") 
			+ this.pCMSSqlServiceV2.getLeftJoinFromSORCFM("a") 
			+ this.pCMSSqlServiceV2.getLeftJoinTAPP("a") 
			+ this.pCMSSqlServiceV2.getLeftJoinInputReplacedRemark("a","a") 
			+ this.pCMSSqlServiceV2.getLeftJoinInputStockRemark("a","a","a")  
			+ this.pCMSSqlServiceV2.getLeftJoinInputPCRemark("a","a")  
			+ this.pCMSSqlServiceV2.getLeftJoinInputCauseOfDelay("a")  
			+ this.pCMSSqlServiceV2.getLeftJoinInputDelayedDep("a")  
			+ this.pCMSSqlServiceV2.getLeftJoinInputSwitchRemark("a")
			+ this.pCMSSqlServiceV2.getLeftJoinInputStockLoad("a", "a")
			+ this.pCMSSqlServiceV2.getLeftJoinTempSumBill("a", "a", "a")  
//			+ whereProdFinal
			+ "order by  a.SaleOrder , a.SaleLine,TypePrd ,a.ProductionOrder  \r\n"
			+ ""
			;;    
//			System.out.println(sql);
		List<Map<String, Object>> datas = SqlStatementHandler.queryList(this.database, PCMSSqlService.dropAllTemp, sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		} 
		return list;
	}  
//	@Override
//	public ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList)
//	{
//
//
//
//		ArrayList<InputDateDetail> list = new ArrayList<>();
//		ArrayList<InputDateDetail> listCount = new ArrayList<>();
//		String fromTable = "";
//		int check = 0;
//		PCMSSecondTableDetail bean = poList.get(0);
//		String caseSave = bean.getCaseSave();
//		String planDate = "";
////		java.util.Date today = new java.util.Date();
////		String todayString=sdf3.format(today);
//		if (caseSave.equals("cfmPlanLabDate")) {
//			planDate = bean.getCfmPlanLabDate();
//			fromTable = " [PCMS].[dbo].[PlanCFMLabDate] ";
//			list = planCFMLabDateService.getMaxCFMPlanLabDateDetail(poList);
//			listCount = planCFMLabDateService.getCountCFMPlanLabDateDetail(poList);
//			check = list.size();
//		} else if (caseSave.equals("cfmPlanDate")) {
//			planDate = bean.getCfmPlanDate();
//			fromTable = "[PCMS].[dbo].[PlanCFMDate] ";
//			list = planCFMDateService.getMaxCFMPlanDateDetail(poList);
//			listCount = planCFMDateService.getCountCFMPlanDateDetail(poList);
//			check = list.size();
//		} else if (caseSave.equals("deliveryDate")) {
//			planDate = bean.getDeliveryDate();
//			fromTable = "[PCMS].[dbo].[PlanDeliveryDate] ";
//			list = planDeliveryDateService.getMaxDeliveryPlanDateDetail(poList);
//			listCount = planDeliveryDateService.getCountDeliveryPlanDateDetail(poList);
//			check = list.size();
//		}
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
////		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
//		String saleLine = bean.getSaleLine();
//		ArrayList<InputDateDetail> listInput = new ArrayList<>();
//		InputDateDetail beanInput = new InputDateDetail();
//		if (check > 0) {
//			beanInput.setIconStatus("I");
//			beanInput.setSystemStatus("Date : " + planDate + " already confirm.Try to refresh again.");
//		} else {
//			try {
//				String sql = "";
//				sql = " insert into "
//						+ fromTable
//						+ " ( "
//						+ "		[ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[PlanDate]  ,[CreateBy]  , " // 5
//						+ "		[CreateDate] ,[LotNo] "
//						+ "     ) "// 24
//						+ " 	values(? , ? , ? , ? , ?"// 1
//						+ "			  ,? , ? "
//						+ " ) ;";
//	
//				int index = 1;
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , saleLine);
//this.sshUtl.setSqlDate(prepared, planDate, index ++ );
//				prepared.setString(index ++ , bean.getUserId());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getLotNo());
//				prepared.executeUpdate();
//				prepared.close();
//				if (caseSave.equals("CFMPlanDate")) {
//					beanInput.setIconStatus("I0");
//				} else {
//					beanInput.setIconStatus("I1");
//				}
//				beanInput.setSystemStatus("Update Success.");
//				if (listCount.size() > 0) {
//					beanInput.setCountPlanDate(listCount.get(0).getCountPlanDate()+1);
//				} else {
//					beanInput.setCountPlanDate(1);
//				}
////				}
//			} catch (SQLException e) {
////				System.err.println(e.getMessage());
//				e.printStackTrace();
//				beanInput.setIconStatus("E");
//				beanInput.setSystemStatus("Something happen, Please contact IT.");
//			}
//		}
//		listInput.add(beanInput);
//		return listInput;
//	} 
//	@Override
//	public ArrayList<PCMSSecondTableDetail> getWaitLotCaseBySaleOrder(ArrayList<PCMSSecondTableDetail> listRP)
//	{
//		ArrayList<PCMSSecondTableDetail> list = null;
//		String where = " where  1 = 1 \r\n";
//		if (listRP.size() > 0) {
//			String saleOrder = "";
//			String saleLine = "";
//			int sizeList = listRP.size();
//			where += " AND ( \r\n";
//			for (int i = 0; i < sizeList; i ++ ) {
//				PCMSSecondTableDetail bean = listRP.get(i);
//				saleOrder = bean.getSaleOrder();
//				saleLine = bean.getSaleLine();
//				where = where + " ( a.SaleOrder = '" + saleOrder + "' and a.SaleLine = '" + saleLine + "' ) ";
//				if (i != sizeList-1) {
//					where += " or ";
//				}
//			}
//			where += " ) \r\n";
//		}
//
//		String sql = ""
////				+ this.declareTempApproved
//				+ this.pss.createTempPrepWaitLot
//				+ this.pss.createTempMainSale
//				+ this.pss.createTempForMainAndWaitLot
//				+ where
//				+ " SELECT DISTINCT  \r\n"
//				+ this.selectWaitLot
//				+ " FROM #tempMainSale as a \r\n "
//				+ this.pss.innerJoinWaitLotB 
//				+ this.pss.getLeftJoinFromSORCFM("a")
//				+ this.pss.getLeftJoinTAPP("b")  
//				+ this.pss.getLeftJoinInputReplacedRemark("b", "a")
//				+ this.pss.getLeftJoinInputPCRemark("b", "a") 
//				+ this.pss.getLeftJoinSimpleTable("b", "InputCauseOfDelay", "InputCOD", "ProductionOrder, CauseOfDelay", "ProductionOrder", "ProductionOrder")  
//				+ this.pss.getLeftJoinSimpleTable("b", "InputDelayedDep", "InputDD", "ProductionOrder, DelayedDep", "ProductionOrder", "ProductionOrder") 
//				+ this.pss.getLeftJoinInputStockLoad("b", "a")
//				+ where
//				+ " and ( SumVol = 'B' OR countProdRP > 0 ) ";
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genPCMSSecondTableDetail(map));
//		}
//		return list;
//	}
// 
//	@Override
//	public ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList)
//	{
//		ArrayList<InputDateDetail> list = null;
//		PCMSSecondTableDetail bean = poList.get(0);
////		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
//		String sql = ""
//				+ " SET NOCOUNT ON; ;\r\n"
//				+ " SELECT \r\n"
//				+ "		 [ProductionOrder]\r\n"
//				+ "     ,[SaleOrder]\r\n"
//				+ "     ,[SaleLine]\r\n"
//				+ "     ,[PlanDate]\r\n"
//				+ "     ,[CreateBy]\r\n"
//				+ "     ,[CreateDate]\r\n"
//				+ "	  	,'0:PCMS' as InputFrom \r\n"
//				+ "     ,LotNo \r\n"
//				+ " FROM [PCMS].[dbo].[PlanDeliveryDate] as a\r\n"
//				+ " where a.[ProductionOrder] = ? and \r\n"
//				+ "       a.[SaleOrder] = ? and \r\n"
//				+ "       a.[SaleLine] = ? \r\n"
//				+ " union ALL  \r\n "
//				+ " SELECT \r\n"
//				+ "      [ProductionOrder]\r\n"
//				+ "      ,[SaleOrder]\r\n"
//				+ "      ,[SaleLine]\r\n"
//				+ "      ,[CFType] as [PlanDate]\r\n"
//				+ "      ,'' AS [CreateBy]\r\n"
//				+ "      ,null AS [CreateDate]\r\n"
//				+ "	     , '1:SAP' as InputFrom \r\n"
//				+ "      ,'' AS LotNo \r\n"
//				+ " FROM [PCMS].[dbo].[FromSapMainProd] as a\r\n"
//				+ " where a.[ProductionOrder] = ? "
//				+ " and CFType is not null  \r\n"  
//				+ " ORDER BY InputFrom ,CreateDate desc ";
//
//		List<Map<String, Object>> datas = this.database.queryList(sql
//				,bean.getProductionOrder()
//				,bean.getSaleOrder()
//				,bean.getSaleLine()
//				,bean.getProductionOrder());
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genInputDateDetail(map));
//		}
//		return list;
//	} 
//
//	@Override
//	public ArrayList<PCMSSecondTableDetail> getNormalCaseByProdOrder(String prdOrderType,
//			ArrayList<PCMSSecondTableDetail> poList)
//	{
//		ArrayList<PCMSSecondTableDetail> list = null;
//		String where = " where 1 = 1 ";
//		String orderBy = " Order by  CustomerShortName,  DueDate, [SaleOrder], [SaleLine], [ProductionOrder]";
//		if (poList.size() > 0) {
//			where += " and a.ProductionOrder in ( \r\n";
//			String prodOrder = "";
//			for (int i = 0; i < poList.size(); i ++ ) {
//				if (prdOrderType.equals(this.C_PRODORDER)) {
//					prodOrder = poList.get(i).getProductionOrder();
//				} else if (prdOrderType.equals(this.C_PRODORDERRP)) {
//					prodOrder = poList.get(i).getProductionOrderRP();
//				}
//				where = where + "'" + prodOrder + "' ";
//				if (i != poList.size()-1) {
//					where += " , ";
//				}
//			}
//			where += " ) \r\n";
//		}
//
//		String fromMainB = ""
//				+ " from (  "
//				+ " SELECT distinct \r\n"
//				+ this.leftJoinBSelect 
//				+ this.pss.fromProdA 
//				+ this.pss.getLeftJoinTempPlandeliveryDate("A","a")    
//				+ this.pss.buildLeftJoinTempProdWorkDate("a")
//				+ this.pss.buildLeftJoinSCC("a")
//				+ this.pss.buildLeftJoinTempSumGR("a")
//				+ this.pss.buildLeftJoinUserStatusAuto("UCAL", "a", "m")
//				+ this.pss.buildLeftJoinViewUserStatusMappingPCMS("UCAL", "UserStatusCal", 0)
//				+ this.pss.getLeftJoinTempSumBill("a", "a", "M")  
//				+ this.pss.getLeftJoinCRP("a")  
//				+ where
//				+ " ) as b \r\n";
//		String sqlMain = ""
//				+ this.pss.createTempMainSale
//				+ this.pss.createTempPlanDeliveryDate
//				+ this.pss.createTempSumBill
//				+ this.pss.createTempSumGR
//				+ this.pss.createTempBillBatchFlag
//				+ this.pss.createTempSumVolOP
//				+ this.pss.createTempSumVolRP
//				+ this.pss.createTempCRP
//	  		    + this.pss.withProdData
//				+ this.createTempMainFirst
//				+ fromMainB 
//				+ this.createTempMainSecond 
//				+ " SELECT * \r\n"
//				+ "	FROM #tempMain\r\n"
//				+ orderBy; 
//		List<Map<String, Object>> datas = this.database.queryList(sqlMain);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genPCMSSecondTableDetail(map));
//		}
//		return list;
//	}
//
//	@Override
//	public ArrayList<PCMSSecondTableDetail> getReplacedCaseByProdOrder(String prdOrderType,
//			ArrayList<PCMSSecondTableDetail> poList)
//	{
//		ArrayList<PCMSSecondTableDetail> list = null;
//		String where = " and ( \r\n";
//		String prodOrder = "";
//		for (int i = 0; i < poList.size(); i ++ ) {
//			if (prdOrderType.equals(this.C_PRODORDER)) {
//				prodOrder = poList.get(i).getProductionOrder();
//				String saleLine = poList.get(i).getSaleLine();
//				where = where
//						+ " ( a."
//						+ prdOrderType
//						+ " = '"
//						+ prodOrder
//						+ "' and\r\n"
//						+ "    a.[SaleOrder] = '"
//						+ poList.get(i).getSaleOrder()
//						+ "' and\r\n"
//						+ "    a.[SaleLine] = '"
//						+ saleLine
//						+ "' \r\n"
//						+ " ) \r\n"; 
//			} else if (prdOrderType.equals(this.C_PRODORDERRP)) {
//				prodOrder = poList.get(i).getProductionOrderRP();
//				where = where + " " + prdOrderType + " = '" + prodOrder + "' ";
//			}
//			if (i != poList.size()-1) {
//				where += " or ";
//			}
//		}
//		where += " ) \r\n";  
//		
//		String sqlRP = ""
//				+ this.pss.createTempMainSale
//				+ this.pss.createTempPlanDeliveryDate
//				+ this.pss.createTempSumBill
//				+ this.pss.createTempSumGR
//				+ " ; WITH PRD_REPLACED as ( \n "
//				+ this.createTempPrdReplacedFirst 
//				+ where 
//				+ this.createTempPrdReplacedSecond 
//				+ " ) \r\n" 
//				+ " select \r\n"
//				+ this.selectAll
//				+ " from PRD_REPLACED as a \r\n"  
//		; 
//		List<Map<String, Object>> datas = this.database.queryList(sqlRP);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genPCMSSecondTableDetail(map));
//		}
//		return list;
//	}
//
//	@Override
//	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList)
//	{
//		ArrayList<PCMSSecondTableDetail> list = null;
//		String where = " and  ( b.ProductionOrder in ( \r\n";
//		for (int i = 0; i < poList.size(); i ++ ) {
//			String ProductionOrder = poList.get(i).getProductionOrder();
//			where = where + " '" + ProductionOrder + "' ";
//			if (i != poList.size()-1) {
//				where += " , ";
//			}
//		}
//		where += " ) " + " ) \r\n";
//		String createTempSWFromA = ""
//
//				+ this.createTempPrdSWFirst
//				+ where
//				+ this.createTempPrdSWSecond;
//		String sqlSW = ""
//				+ this.pss.createTempMainSale
//				+ this.pss.createTempPlanDeliveryDate
//				+ this.pss.createTempSumBill
//				+ this.pss.createTempSumGR
//				+ createTempSWFromA
//				+ " select distinct\r\n"
//				+ this.selectAll
//				+ " from #tempPrdSW as a \r\n"
//				+ this.pss.buildInnerJoinViewUSM_SPE("a",1,"UserStatus")
//				;
//		List<Map<String, Object>> datas = this.database.queryList(sqlSW);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genPCMSSecondTableDetail(map));
//		}
//		return list;
//	}
//
//	@Override
//	public ArrayList<PCMSSecondTableDetail> getOrderPuangListByPrd(ArrayList<PCMSSecondTableDetail> poList)
//	{
//		ArrayList<PCMSSecondTableDetail> list = null;
//		String where = " and  ( b.ProductionOrder in ( \r\n";
//		for (int i = 0; i < poList.size(); i ++ ) {
//			String ProductionOrder = poList.get(i).getProductionOrder();
//			where = where + " '" + ProductionOrder + "' ";
//			if (i != poList.size()-1) {
//				where += " , ";
//			}
//		}
//		where += " ) " + " ) \r\n";
//		String createTempOPFromA = "" 
//				+ this.createTempPrdOPA 
//				+ "         " + where 
//				+ this.createTempOP; 
//		String sqlOP = "" 
//				+ this.pss.createTempMainSale
//				+ this.pss.createTempPlanDeliveryDate
//				+ this.pss.createTempSumBill
//				+ this.pss.createTempSumGR
//				+ createTempOPFromA 
//				+ " select distinct \r\n"
//				+ this.selectAll
//				+ " from #tempPrdOP as a \r\n"
//				+ this.pss.getLeftJoinSwitchProdOrder("A") 
//				+ this.pss.buildInnerJoinViewUSM_SPE("a",1,"UserStatus")
//				+ " where 1 = 1 "
//				+ "    AND SPO.ProductionOrderSW IS NULL " 
//				;
//		List<Map<String, Object>> datas = this.database.queryList(sqlOP);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genPCMSSecondTableDetail(map));
//		}
//		return list;
//	}
//
//	@Override
//	public ArrayList<PCMSSecondTableDetail> getOrderPuangSWListByPrd(ArrayList<PCMSSecondTableDetail> poList)
//	{
//		ArrayList<PCMSSecondTableDetail> list = null;
//		String where = " ";
//		if ( ! poList.isEmpty()) {
//			where = " and b.ProductionOrder IN (";
//			List<String> productionOrders = new ArrayList<>();
//
//			for (PCMSSecondTableDetail detail : poList) {
//				productionOrders.add("'" + detail.getProductionOrder() + "'");
//			}
//
//			where += String.join(", ", productionOrders) + ") \r\n";
//		}
//		String sql = ""
//				+ this.pss.createTempMainSale
//				+ this.pss.createTempPlanDeliveryDate
//				+ this.pss.createTempSumBill
//				+ this.pss.createTempSumGR
//				+ this.createTempOPSWFirst 
//				+ where
//				+ " \r\n" 
//				+ this.createTempOPSWSecond
//				+ this.pss.buildInnerJoinViewUSM_SPE("b",1,"UserStatus")
//				+ " SELECT * \r\n"
//				+ "	FROM #tempPrdOPSW \r\n";
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genPCMSSecondTableDetail(map));
//		}
//		return list;
//	} 
//	@Override
//	public PCMSSecondTableDetail upSertRemarkCaseThree(String tableName, String planDate, PCMSSecondTableDetail bean)
//	{
//
//
//
//		String prdOrder = bean.getProductionOrder();
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//		String caseSave = bean.getCaseSave();
//		try {
//			String sql = " INSERT INTO [PCMS].[dbo]."
//					+ tableName
//					+ " \r\n"
//					+ " ([ProductionOrder] ,"
//					+ caseSave
//					+ ",[ChangeBy] ,[ChangeDate],[LotNo])"// 55
//					+ " values(? , ? , ? , ? , ?   )  "
//					+ ";";
//			int index = 1;
//
//			prepared.setString(index ++ , prdOrder);
//			prepared = this.sshUtl.setSqlDate(prepared, planDate, index ++ );
//			prepared.setString(index ++ , bean.getUserId());
//			prepared.setTimestamp(index ++ , new Timestamp(time));
//			prepared.setString(index ++ , bean.getLotNo());
//			prepared.executeUpdate();
//			prepared.close();
//			bean.setIconStatus("I");
//			bean.setSystemStatus("Update Success.");
//		} catch (SQLException e) {
//			e.printStackTrace();
////			System.err.println("upSertRemarkCaseThree" + e.getMessage());
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Something happen.Please contact IT.");
//		}
//		return bean;
//	}
//
//	@Override
//	public PCMSSecondTableDetail updateLogRemarkCaseOne(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
//	{
//
//
//
//		String prdOrder = bean.getProductionOrder();
//		String saleOrder = bean.getSaleOrder();
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
////		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
//		try {
//			String sql = " UPDATE [PCMS].[dbo]."
//					+ tableName
//					+ " 	SET DataStatus = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
//					+ " WHERE [ProductionOrder]  = ? "
//					+ "		and [SaleOrder] = ?  "
//					+ "		and [SaleLine] = ? "
//					+ "		and DataStatus = 'O'; ";
//
//			prepared.setString(1, close_STATUS);
//			prepared.setString(2, bean.getUserId());
//			prepared.setTimestamp(3, new Timestamp(time));
//			prepared.setString(4, prdOrder);
//			prepared.setString(5, saleOrder);
//			prepared.setString(6,  bean.getSaleLine() );
//			prepared.executeUpdate();
//			prepared.close();
//			bean.setIconStatus("I");
//			bean.setSystemStatus("Update Success.");
//		} catch (SQLException e) {
//			e.printStackTrace();
////			System.err.println("updateLogRemarkCaseOne" + e.getMessage());
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Something happen.Please contact IT.");
//		} finally {
//			//// this.database.close();
//		}
//		return bean;
//	}
//
//	@Override
//	public PCMSSecondTableDetail updateLogRemarkCaseFix(String tableName, String valueChange, PCMSSecondTableDetail bean)
//	{
//
//
//
//		String prdOrder = bean.getProductionOrder();
//		String saleOrder = bean.getSaleOrder();
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
////		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
//		String caseSave = bean.getCaseSave();
//		try {
//			String sql = "UPDATE [PCMS].[dbo]."
//					+ tableName
//					+ " SET "
//					+ caseSave
//					+ " = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
//					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? and DataStatus = 'O' ";
//
//			prepared.setString(1, valueChange);
//			prepared.setString(2, bean.getUserId());
//			prepared.setTimestamp(3, new Timestamp(time));
//			prepared.setString(4, prdOrder);
//			prepared.setString(5, saleOrder);
//			prepared.setString(6, bean.getSaleLine());
//			prepared.executeUpdate();
//			prepared.close();
//			bean.setIconStatus("I");
//			bean.setSystemStatus("Update Success.");
//		} catch (SQLException e) {
////			System.err.println("updateLogRemarkCaseOne" + e.getMessage());
//			e.printStackTrace();
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Something happen.Please contact IT.");
//		} finally {
//			//// this.database.close();
//		}
//		return bean;
//	}
//	@Override
//	public PCMSSecondTableDetail updateLogRemarkWithGrade(String tableName, PCMSSecondTableDetail bean, String Status)
//	{
//
//
//
//		String prdOrder = bean.getProductionOrder();
//		String saleOrder = bean.getSaleOrder();
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
////		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
////		String caseSave = bean.getCaseSave();
//		String grade = bean.getGrade();
//		try {
//			String sql = "UPDATE [PCMS].[dbo]."
//					+ tableName
//					+ " SET DataStatus = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
//					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? and [Grade] = ? and DataStatus = 'O' ";
//
//			prepared.setString(1, Status);
//			prepared.setString(2, bean.getUserId());
//			prepared.setTimestamp(3, new Timestamp(time));
//			prepared.setString(4, prdOrder);
//			prepared.setString(5, saleOrder);
//			prepared.setString(6, bean.getSaleLine());
//			prepared.setString(7, grade);
//			prepared.executeUpdate();
//			prepared.close();
//			bean.setIconStatus("I");
//			bean.setSystemStatus("Update Success.");
//		} catch (SQLException e) {
//			e.printStackTrace();
////			System.err.println("updateLogRemarkWithGrade" + e.getMessage());
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Something happen.Please contact IT.");
//		} finally {
//			//// this.database.close();
//		}
//		return bean;
//	}
//
//	@Override
//	public PCMSSecondTableDetail updateLogRemarkCaseThree(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
//	{
//
//
//
//		String prdOrder = bean.getProductionOrder();
////		String saleOrder = bean.getSaleOrder();
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		currentTime.getTime(); 
//		try {
//			String sql = "UPDATE [PCMS].[dbo]."
//					+ tableName
//					+ " SET DataStatus = ?  "
//					+ " WHERE [ProductionOrder]  = ?  and DataStatus = 'O' ; ";
//
//			prepared.setString(1, close_STATUS);
//			prepared.setString(2, prdOrder);
//			prepared.executeUpdate();
//			prepared.close();
//			bean.setIconStatus("I");
//			bean.setSystemStatus("Update Success.");
//		} catch (SQLException e) {
//			e.printStackTrace();
////			System.err.println("updateLogRemarkCaseOne" + e.getMessage());
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Something happen.Please contact IT.");
//		} finally {
//			//// this.database.close();
//		}
//		return bean;
//	}
//	@Override
//	public PCMSSecondTableDetail upSertRemarkCaseOne(String tableName, String valueChange, PCMSSecondTableDetail bean)
//	{
//
//
//
//		String prdOrder = bean.getProductionOrder();
//		String saleOrder = bean.getSaleOrder();
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
////		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
//		String caseSave = bean.getCaseSave();
//		try {
//			String sql = " INSERT INTO [PCMS].[dbo]."
//					+ tableName
//					+ " ([ProductionOrder],[SaleOrder] ,[SaleLine],"
//					+ caseSave
//					+ ",[ChangeBy] ,[ChangeDate])"// 55
//					+ " values \r\n"
//					+ "	(? , ? , ? , ? , ? "
//					+ ", ? )  "
//					+ ";";
//
//			prepared.setString(1, prdOrder);
//			prepared.setString(2, saleOrder);
//			prepared.setString(3, bean.getSaleLine());
//			prepared.setString(4, valueChange);
//			prepared.setString(5, bean.getUserId());
//			prepared.setTimestamp(6, new Timestamp(time));
//			prepared.executeUpdate();
//			prepared.close();
//			bean.setIconStatus("I");
//			bean.setSystemStatus("Update Success.");
//		} catch (SQLException e) {
//			e.printStackTrace();
////			System.err.println("upSertRemarkCaseOne" + e.getMessage());
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Something happen.Please contact IT.");
//		} finally {
//			//// this.database.close();
//		}
//		return bean;
//	}
//
//	@Override
//	public PCMSSecondTableDetail updateLogRemarkCaseTwo(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
//	{
//
//
//
//		String prdOrder = bean.getProductionOrder();
////		String saleOrder = bean.getSaleOrder();
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime(); 
//		try {
//			String sql = " UPDATE [PCMS].[dbo]."
//					+ tableName
//					+ " SET DataStatus = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
//					+ " WHERE [ProductionOrder]  = ?  and DataStatus = 'O' ; ";
//
//			prepared.setString(1, close_STATUS);
//			prepared.setString(2, bean.getUserId());
//			prepared.setTimestamp(3, new Timestamp(time));
//			prepared.setString(4, prdOrder);
//			prepared.executeUpdate();
//			prepared.close();
//			bean.setIconStatus("I");
//			bean.setSystemStatus("Update Success.");
//		} catch (SQLException e) {
////			System.err.println("updateLogRemarkCaseOne" + e.getMessage());
//			e.printStackTrace();
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Something happen.Please contact IT.");
//		} finally {
//			//// this.database.close();
//		}
//		return bean;
//	}
//	@Override
//	public PCMSSecondTableDetail upSertRemarkCaseTwo(String tableName, String valueChange, PCMSSecondTableDetail bean)
//	{
//
//
//
//		String prdOrder = bean.getProductionOrder(); 
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime(); 
//		String caseSave = bean.getCaseSave();
//		try {
//			String sql = " INSERT INTO [PCMS].[dbo]."
//					+ tableName
//					+ " \r\n"
//					+ " ([ProductionOrder] ,"
//					+ caseSave
//					+ ",[ChangeBy] ,[ChangeDate])"// 55
//					+ " values(? , ? , ? , ?   )  "
//					+ ";";
//
//			prepared.setString(1, prdOrder);
//			prepared.setString(2, valueChange);
//			prepared.setString(3, bean.getUserId());
//			prepared.setTimestamp(4, new Timestamp(time));
//			prepared.executeUpdate();
//			prepared.close();
//			bean.setIconStatus("I");
//			bean.setSystemStatus("Update Success.");
//		} catch (SQLException e) {
////			System.err.println("upSertRemarkCaseTwo" + e.getMessage());
//			e.printStackTrace();
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Something happen.Please contact IT.");
//		} finally {
//			//// this.database.close();
//		}
//		return bean;
//	}
//
//	@Override
//	public PCMSSecondTableDetail upSertRemarkCaseWithGrade(String tableName, String valueChange, PCMSSecondTableDetail bean)
//	{
//
//
//
//		String prdOrder = bean.getProductionOrder();
//		String saleOrder = bean.getSaleOrder();
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
////		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
//		String caseSave = bean.getCaseSave();
//		String grade = bean.getGrade();
//		try {
//			String sql = " INSERT INTO [PCMS].[dbo]."
//					+ tableName
//					+ " 	([ProductionOrder],[SaleOrder] ,[SaleLine],"
//					+ caseSave
//					+ ",[ChangeBy] "
//					+ " 	,[ChangeDate],[Grade])"// 55
//					+ " values \r\n"
//					+ "		(? , ? , ? , ? , ? "
//					+ "    , ? , ? )  ;";
//
//			prepared.setString(1, prdOrder);
//			prepared.setString(2, saleOrder);
//			prepared.setString(3, bean.getSaleLine());
//			prepared.setString(4, valueChange);
//			prepared.setString(5, bean.getUserId());
//			prepared.setTimestamp(6, new Timestamp(time));
//			prepared.setString(7, grade);
//			prepared.executeUpdate();
//			prepared.close();
//			bean.setIconStatus("I");
//			bean.setSystemStatus("Update Success.");
//		} catch (SQLException e) {
////			System.err.println("upSertRemarkCaseWithGrade" + e.getMessage());
//			e.printStackTrace();
//			bean.setIconStatus("E");
//			bean.setSystemStatus("Something happen.Please contact IT.");
//		} finally {
//			//// this.database.close();
//		}
//		return bean;
//	}
	@Override
	public ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getWaitLotCaseBySaleOrder(ArrayList<PCMSSecondTableDetail> listRP)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getNormalCaseByProdOrder(String prdOrderType, ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getReplacedCaseByProdOrder(String prdOrderType,
			ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getOrderPuangSWListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PCMSSecondTableDetail upSertRemarkCaseThree(String tableName, String planDate, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getOrderPuangListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PCMSSecondTableDetail updateLogRemarkCaseOne(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PCMSSecondTableDetail updateLogRemarkCaseFix(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PCMSSecondTableDetail updateLogRemarkCaseThree(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PCMSSecondTableDetail upSertRemarkCaseTwo(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PCMSSecondTableDetail updateLogRemarkCaseTwo(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PCMSSecondTableDetail upSertRemarkCaseWithGrade(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PCMSSecondTableDetail upSertRemarkCaseOne(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PCMSSecondTableDetail updateLogRemarkWithGrade(String tableName, PCMSSecondTableDetail bean, String Status)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DataTableResponse<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList, int start, int length)
	{
		   PCMSTableDetail bean = poList.get(0);
		    List<String> userStatusList = bean.getUserStatusList();
		    Map<String, String> results = pss.buildWhereClauses(bean);
		    String whereSale = results.get("whereSale");
			String whereProdFinal = results.get("whereProdFinal"); 

		    String baseSql =
		          psService.handlerTempTableUserStatusList(userStatusList)
		        + psService.handlerTempTableCustomerSearchList(
		              bean.getCustomerNameList(),
		              bean.getCustomerShortNameList())
		        + pCMSSqlServiceV2.finalIntoTempMainSale(whereSale)
		        + pCMSSqlServiceV2.createTempPlanDeliveryDate
		        + pCMSSqlServiceV2.createTempSumGR
		        + pCMSSqlServiceV2.createTempSumBill
		        + pCMSSqlServiceV2.createTempBillBatchFlag
		        + pCMSSqlServiceV2.createTempSumVolOP
		        + pCMSSqlServiceV2.createTempSumVolRP
		        + pCMSSqlServiceV2.createTempSaleAgg
		        + pCMSSqlServiceV2.createTempProdAgg
		        + pCMSSqlServiceV2.createTempFlagHasRP
		        + pCMSSqlServiceV2.createTempFlagHasOP
		        + pCMSSqlServiceV2.finalIntoTempAllProdBase(whereProdFinal);

		    // 1) query total ก่อน (ยังไม่ OFFSET)
		    String countSql =
		        baseSql +
		        " SELECT COUNT(1) AS CNT " +
		        " FROM #tempAllProdBase A " +
		        " JOIN #tempMainSale tms ON a.SaleOrder = tms.SaleOrder " +
		        "   AND a.SaleLine = tms.SaleLine ";

		 // querySingle คืนค่าเป็น Map (1 แถว)
		    Map<String, Object> result = this.database.querySingle(countSql);

		    long total = 0;
		    if (result != null && result.get("CNT") != null) {
		        // ดึงค่าจาก Key "CNT" ที่เรา AS ไว้ใน SQL
		        // แนะนำให้ใช้ .toString() แล้ว Parse ป้องกันเรื่อง Integer/Long mismatch จาก Driver
		        total = Long.parseLong(result.get("CNT").toString());
		    }
		    // 2) query page data
		    String dataSql =
		        baseSql +
		        " SELECT " + selectTempAllProdBase +
		        " FROM #tempAllProdBase A " +
		        " JOIN #tempMainSale tms ON a.SaleOrder = tms.SaleOrder " +
		        "   AND a.SaleLine = tms.SaleLine " +
		        pCMSSqlServiceV2.getLeftJoinFromSapMainProd("a") +
		        pCMSSqlServiceV2.getLeftJoinPlanCFMLabDate("a", "a") +
		        pCMSSqlServiceV2.buildLeftJoinTempProdWorkDate("a") +
		        pCMSSqlServiceV2.buildLeftJoinSCC("a") +
		        pCMSSqlServiceV2.getLeftJoinTempPlandeliveryDate("a", "a") +
		        pCMSSqlServiceV2.getLeftJoinFromSORCFM("a") +
		        pCMSSqlServiceV2.getLeftJoinTAPP("a") +
		        pCMSSqlServiceV2.getLeftJoinInputReplacedRemark("a","a") +
		        pCMSSqlServiceV2.getLeftJoinInputStockRemark("a","a","a") +
		        pCMSSqlServiceV2.getLeftJoinInputPCRemark("a","a") +
		        pCMSSqlServiceV2.getLeftJoinInputCauseOfDelay("a") +
		        pCMSSqlServiceV2.getLeftJoinInputDelayedDep("a") +
		        pCMSSqlServiceV2.getLeftJoinInputSwitchRemark("a") +
		        pCMSSqlServiceV2.getLeftJoinInputStockLoad("a", "a") +
		        pCMSSqlServiceV2.getLeftJoinTempSumBill("a", "a", "a") +
		        " ORDER BY a.SaleOrder, a.SaleLine, a.ProductionOrder, a.TypePrd " +
		        " OFFSET " + start + " ROWS FETCH NEXT " + length + " ROWS ONLY ";

		    List<Map<String, Object>> datas = SqlStatementHandler.queryList(this.database, PCMSSqlService.dropAllTemp, dataSql);

		    List<PCMSSecondTableDetail> list = new ArrayList<>();
		    for (Map<String, Object> map : datas) {
		        list.add(bcModel._genPCMSSecondTableDetail(map));
		    }

		    DataTableResponse<PCMSSecondTableDetail> res = new DataTableResponse<>();
		    res.setData(list);
		    res.setRecordsTotal(total);
		    res.setRecordsFiltered(total);

		    return res;
	}

}
