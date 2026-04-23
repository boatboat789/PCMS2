package th.co.wacoal.atech.pcms2.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.ReplacedProdOrderDetail;
import th.co.wacoal.atech.pcms2.entities.SwitchProdOrderDetail;
import th.co.wacoal.atech.pcms2.entities.TempUserStatusAutoDetail;
import th.co.wacoal.atech.pcms2.service.BackGroundJobService;
import th.co.wacoal.atech.pcms2.service.PCMSDetailService;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainProdService;
import th.co.wacoal.atech.pcms2.service.master.ReplacedProdOrderService;
import th.co.wacoal.atech.pcms2.service.master.SearchSettingService;
import th.co.wacoal.atech.pcms2.service.master.SwitchProdOrderService;
import th.co.wacoal.atech.pcms2.service.master.TEMP_UserStatusAutoService;

@Repository // Spring annotation to mark this as a DAO component
public class PCMSDetailProcess {
	private String C_PRODORDER = "ProductionOrder";
	private String C_PRODORDERRP = "ProductionOrderRP";
	private String CLOSE_STATUS = "X";
	private final PCMSDetailService pCMSDetailService;
	private final BackGroundJobService bgjService;
	private final FromSapMainProdService fromSapMainProdService;
	private final SearchSettingService searchSettingService;
	private final SwitchProdOrderService switchProdOrderService;
	private final ReplacedProdOrderService replacedProdOrderService;
	private final TEMP_UserStatusAutoService tusaService;

	@Autowired
	public PCMSDetailProcess(TEMP_UserStatusAutoService tusaService,
			SwitchProdOrderService switchProdOrderService, SearchSettingService searchSettingService,
			BackGroundJobService bgjService, ReplacedProdOrderService replacedProdOrderService,
			PCMSDetailService pCMSDetailService, FromSapMainProdService fromSapMainProdService) {
		this.pCMSDetailService = pCMSDetailService;
		this.bgjService = bgjService;
		this.fromSapMainProdService = fromSapMainProdService;
		this.searchSettingService = searchSettingService;
		this.switchProdOrderService = switchProdOrderService;
		this.replacedProdOrderService = replacedProdOrderService;
		this.tusaService = tusaService;

	}

	public ArrayList<PCMSSecondTableDetail> saveInputDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		PCMSSecondTableDetail bean = poList.get(0);
		String caseSave = bean.getCaseSave();
		String valueChange = "";
		String tableName = "";
		if (caseSave.equals("stockRemark")) {
			valueChange = bean.getStockRemark();
			tableName = "[InputStockRemark]";
			bean = pCMSDetailService.updateLogRemarkWithGrade(tableName, bean, this.CLOSE_STATUS);
			bean = pCMSDetailService.upSertRemarkCaseWithGrade(tableName, valueChange, bean);
			poList.clear();
			poList.add(bean); 
		} else if (caseSave.equals("pcRemark")) {
			valueChange = bean.getPcRemark();
			tableName = "[InputPCRemark]";
			bean = pCMSDetailService.updateLogRemarkCaseOne(tableName, bean, this.CLOSE_STATUS);
			bean = pCMSDetailService.upSertRemarkCaseOne(tableName, valueChange, bean);
			poList.clear();
			poList.add(bean);
		} else if (caseSave.equals("stockLoad")) {
			valueChange = bean.getStockLoad();
			tableName = "[InputStockLoad]";
			bean = pCMSDetailService.updateLogRemarkCaseOne(tableName, bean, this.CLOSE_STATUS);
			bean = pCMSDetailService.upSertRemarkCaseOne(tableName, valueChange, bean);
			bgjService.execUpsertToTEMPUserStatusOnWebWithProdOrder(bean.getProductionOrder());
			ArrayList<TempUserStatusAutoDetail> list = tusaService.getTempUserStatusAutoDetail(poList);
			if (list.size() > 0) {
				TempUserStatusAutoDetail beanTmp = list.get(0);
				if (beanTmp.getProductionOrderRPM().equals("")) {
					bean.setUserStatus(beanTmp.getUserStatusCal());
				} else if ( ! beanTmp.getProductionOrderRPM().equals("")) {
					bean.setUserStatus(beanTmp.getUserStatusCalRP());
				} else {
					bean.setUserStatus("");
				}
			} else {
				bean.setUserStatus("");
			}
			poList.clear();
			poList.add(bean);
		} else if (caseSave.equals("replacedRemark")) {
			valueChange = bean.getReplacedRemark();
			tableName = "[InputReplacedRemark]";
			bean = pCMSDetailService.updateLogRemarkCaseOne(tableName, bean, this.CLOSE_STATUS);
			bean = pCMSDetailService.upSertRemarkCaseOne(tableName, valueChange, bean);
			poList = this.handlerReplacedProdOrder(bean);

		} else if (caseSave.equals("switchRemark")) {
			valueChange = bean.getSwitchRemark();
			tableName = "[InputSwitchRemark]";
			bean = pCMSDetailService.updateLogRemarkCaseTwo(tableName, bean, this.CLOSE_STATUS);
			bean = pCMSDetailService.upSertRemarkCaseTwo(tableName, valueChange, bean);
			poList = this.handlerSwitchProdOrder(bean);
		} else if (caseSave.equals("delayedDep")) {
			valueChange = bean.getDelayedDepartment();
			tableName = "[InputDelayedDep]";
			bean = pCMSDetailService.updateLogRemarkCaseTwo(tableName, bean, this.CLOSE_STATUS);
			bean = pCMSDetailService.upSertRemarkCaseTwo(tableName, valueChange, bean);
			poList.clear();
			poList.add(bean);
		} else if (caseSave.equals("causeOfDelay")) {
			valueChange = bean.getCauseOfDelay();
			tableName = "[InputCauseOfDelay]";
			bean = pCMSDetailService.updateLogRemarkCaseTwo(tableName, bean, this.CLOSE_STATUS);
			bean = pCMSDetailService.upSertRemarkCaseTwo(tableName, valueChange, bean);
			poList.clear();
			poList.add(bean);
		} else if (caseSave.equals("sendCFMCusDate")) {
			valueChange = bean.getSendCFMCusDate();
			tableName = "[PlanSendCFMCusDate] ";
			bean = pCMSDetailService.updateLogRemarkCaseThree(tableName, bean, this.CLOSE_STATUS);
			bean = pCMSDetailService.upSertRemarkCaseThree(tableName, valueChange, bean);
			poList.clear();
			poList.add(bean);
		}
		return poList;
	}
  
	private ArrayList<PCMSSecondTableDetail> handlerReplacedProdOrder(PCMSSecondTableDetail bean)
	{
		ArrayList<PCMSSecondTableDetail> list = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOld = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOldNormal = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> listRP = new ArrayList<>();
		String repRemark = bean.getReplacedRemark().trim();
		String volume = "";
		String prdOrder = bean.getProductionOrder().trim();
		String prdOrderRP = "";
		String saleOrder = bean.getSaleOrder().trim();
		String saleLine = bean.getSaleLine().trim();
		String userId = bean.getUserId().trim();
		ArrayList<SwitchProdOrderDetail> listSWMainOne = switchProdOrderService.getSwitchProdOrderDetailByPrd(prdOrder);
		ArrayList<SwitchProdOrderDetail> listSWMainTwo = switchProdOrderService.getSwitchProdOrderDetailByPrdSW(prdOrder);
		String tableName = "[InputReplacedRemark]";
		boolean numeric = true;
		boolean errCheck = true;
		String[] newRPSplit = repRemark.split(",");
		listRP.clear();
		if ( ! errCheck) {
		} else if (repRemark.equals("")) {
//			-----------------------------------------------------
			list.add(bean);
			listRP = pCMSDetailService.getReplacedCaseByProdOrder(this.C_PRODORDER, list);
			bean = replacedProdOrderService.updateReplacedProdOrder(bean, "X");
			if (listRP.size() > 0) {
				for (int i = 0; i < listRP.size(); i ++ ) {
					PCMSSecondTableDetail beanTmp = new PCMSSecondTableDetail();
					beanTmp.setProductionOrder(listRP.get(i).getProductionOrder());
					poListOld.add(beanTmp);
				}
				// get normal case from main prod order RP Remark
				poList = pCMSDetailService.getNormalCaseByProdOrder(this.C_PRODORDER, poListOld);
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
			} else {
//				bean = this.updateReplacedPrd(bean, "X");
				poList.clear();
				bean.setReplacedRemark("");
				bean = pCMSDetailService.updateLogRemarkCaseFix(tableName, "", bean);
				bean.setIconStatus("E");
				bean.setSystemStatus("Something happen.Please contact IT.");
				poList.add(bean);
			}
			poList = this.setBeanIconStatus(poList, bean);
		} else if (listSWMainOne.size() > 0) {
			String prodOrderCheck = "";
			if (listSWMainOne.size() > 0) {
				prodOrderCheck = listSWMainOne.get(0).getProductionOrder();
			}
			bean.setIconStatus("E");
			bean.setSystemStatus("Prod.Order already switch between " + prdOrder + " and " + prodOrderCheck + ".");
			poList.add(bean);
		} else if (listSWMainTwo.size() > 0) {
			String prodOrderCheck = "";
			if (listSWMainTwo.size() > 0) {
				prodOrderCheck = listSWMainTwo.get(0).getProductionOrder();
			}
			bean.setIconStatus("E");
			bean.setSystemStatus("Prod.Order already switch between " + prdOrder + " and " + prodOrderCheck + ".");
			poList.add(bean);
		} else if (newRPSplit.length > 0) {
			prdOrder = bean.getProductionOrder().trim();
			ArrayList<ReplacedProdOrderDetail> listRPOld =
					replacedProdOrderService.getReplacedProdOrderDetailByPrdMainAndSO(prdOrder, saleOrder, saleLine);
			bean = replacedProdOrderService.updateReplacedProdOrder(bean, "X");
			ArrayList<PCMSSecondTableDetail> checkList = null;
			for (String element : newRPSplit) {
				String[] subSplit = element.split("=");
				ReplacedProdOrderDetail beanRP = new ReplacedProdOrderDetail();
				numeric = true;
				prdOrderRP = "";
				if (subSplit.length == 1) {
					prdOrderRP = element.trim();
					volume = "0";
				} else {
					prdOrderRP = subSplit[0].trim();
					try {
						volume = subSplit[1].trim();
						Double.parseDouble(volume);
					} catch (NumberFormatException e) {
						numeric = false;
					}
					if (numeric) {
					} else {
						volume = "0";
					}
				}
				if ( ! prdOrderRP.equals("")) {
					checkList = fromSapMainProdService.getFromSapMainProdDetail(prdOrderRP);
					if (checkList.size() == 0) {
						errCheck = false;
						bean.setIconStatus("E");
						bean.setSystemStatus(prdOrderRP + " is not in the Database or wrong data entry. ");
						poList.add(bean);
						break;
					} else {
						PCMSSecondTableDetail beanCheckRP = checkList.get(0);
						listRP.add(beanCheckRP);

						beanRP.setProductionOrder(prdOrder);
						beanRP.setSaleOrder(saleOrder);
						beanRP.setSaleLine(saleLine);
						beanRP.setProductionOrderRP(prdOrderRP);
						beanRP.setChangeBy(userId);
						beanRP.setVolume(volume);
						ReplacedProdOrderDetail beanX = replacedProdOrderService.upsertReplacedProdOrder(beanRP, "O");
						if (beanX.getIconStatus().equals("E")) {
							errCheck = false;
							bean.setIconStatus(beanX.getIconStatus());
							bean.setSystemStatus(beanX.getSystemStatus());
						}
						PCMSSecondTableDetail beanTMP = new PCMSSecondTableDetail();
						beanTMP.setProductionOrder(prdOrder);
						beanTMP.setProductionOrderRP(prdOrderRP);
						poList.add(beanTMP);
					}
				}
			}
			if (errCheck) {
				listRP = pCMSDetailService.getWaitLotCaseBySaleOrder(listRP);
				poListOld = pCMSDetailService.getNormalCaseByProdOrder(this.C_PRODORDERRP, poList);
				poList = pCMSDetailService.getReplacedCaseByProdOrder(this.C_PRODORDERRP, poList);
				int i = 0;
				ArrayList<String> listCompareOne = new ArrayList<>();
				ArrayList<String> listCompareTwo = new ArrayList<>();
				for (i = 0; i < poList.size(); i ++ ) {
					listCompareOne.add(poList.get(i).getProductionOrder());
				}
				for (i = 0; i < listRPOld.size(); i ++ ) {
					listCompareTwo.add(listRPOld.get(i).getProductionOrderRP());
				}
				listCompareTwo.removeAll(listCompareOne);
				for (i = 0; i < listCompareTwo.size(); i ++ ) {
					PCMSSecondTableDetail beanTMP = new PCMSSecondTableDetail();
					beanTMP.setProductionOrder(listCompareTwo.get(i));
					poListOldNormal.add(beanTMP);
				}
				if (poListOldNormal.size() > 0) {
					poListOldNormal = pCMSDetailService.getNormalCaseByProdOrder(this.C_PRODORDER, poListOldNormal);
				}
				for (i = 0; i < poListOld.size(); i ++ ) {
					poList.add(poListOld.get(i));
				}
				for (i = 0; i < poListOldNormal.size(); i ++ ) {
					poList.add(poListOldNormal.get(i));
				}
				for (i = 0; i < listRP.size(); i ++ ) {
					poList.add(listRP.get(i));
				}
				poList = this.setBeanIconStatus(poList, bean);
			} else {
				poList = this.setBeanIconStatus(poList, bean);
			}
		}
		return poList;
	}

	private ArrayList<PCMSSecondTableDetail> handlerSwitchProdOrder(PCMSSecondTableDetail bean)
	{
		ArrayList<PCMSSecondTableDetail> list = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListTMP = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOld = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOP = new ArrayList<>();
		ArrayList<PCMSSecondTableDetail> poListOPSW = new ArrayList<>();
		String prdOrderSW = bean.getSwitchRemark().trim();
		String prdOrder = bean.getProductionOrder().trim();
		if (prdOrderSW.equals("")) {
			list.add(bean);
			poList = pCMSDetailService.getSwitchProdOrderListByPrd(list);
			bean = switchProdOrderService.updateSwitchProdOrderDetail(bean, "X");
			if (poList.size() > 0) {
				poListOP = pCMSDetailService.getOrderPuangListByPrd(poList);
				poList = pCMSDetailService.getNormalCaseByProdOrder(this.C_PRODORDER, poList);
				int i = 0;
				for (i = 0; i < poListOP.size(); i ++ ) {
					poList.add(poListOP.get(i));
				}
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
			} else {
				poList.clear();
				bean.setIconStatus("E");
				bean.setSystemStatus("Something happen.Please contact IT.");
				poList.add(bean);
			}
			poList = this.setBeanIconStatus(poList, bean);
		} else {

			list = switchProdOrderService.getSwitchProdOrderDetailByProdOrderForHandlerSwitchProd(prdOrderSW);
			if (list.size() > 0) {
				PCMSSecondTableDetail beanL = list.get(0);
				ArrayList<ReplacedProdOrderDetail> listRPSubOne = replacedProdOrderService.getReplacedProdOrderDetailByPrdRP(prdOrder);
				ArrayList<ReplacedProdOrderDetail> listRPSubTwo = replacedProdOrderService.getReplacedProdOrderDetailByPrdRP(prdOrderSW);
				ArrayList<ReplacedProdOrderDetail> listRPMainOne = replacedProdOrderService.getReplacedProdOrderDetailByPrd(prdOrder);
				ArrayList<ReplacedProdOrderDetail> listRPMainTwo = replacedProdOrderService.getReplacedProdOrderDetailByPrd(prdOrderSW);
				int countPrdSW = beanL.getCountInSW();
				int countCaseMOne = listRPMainOne.size();
				int countCaseMTwo = listRPMainTwo.size();
				int countCaseSOne = listRPSubOne.size();
				int countCaseSTwo = listRPSubTwo.size();
				if (countCaseMOne > 0) {
					ReplacedProdOrderDetail beanRP = listRPMainOne.get(0);
					bean.setIconStatus("E");
					bean.setSystemStatus("Prod.Order already replaced sale from "
							+ beanRP.getProductionOrder()
							+ " by "
							+ beanRP.getProductionOrderRP()
							+ ".");
					poList.add(bean);
				} else if (countCaseMTwo > 0) {
					ReplacedProdOrderDetail beanRP = listRPMainTwo.get(0);
					bean.setIconStatus("E");
					bean.setSystemStatus("Prod.Order already replaced sale from "
							+ beanRP.getProductionOrder()
							+ " by "
							+ beanRP.getProductionOrderRP()
							+ ".");
					poList.add(bean);
				} else if (countCaseSOne > 0) {
					ReplacedProdOrderDetail beanRP = listRPSubOne.get(0);
					bean.setIconStatus("E");
					bean.setSystemStatus("Prod.Order already replaced sale by " + beanRP.getProductionOrder() + ".");
					poList.add(bean);
				} else if (countCaseSTwo > 0) {
					ReplacedProdOrderDetail beanRP = listRPSubTwo.get(0);
					bean.setIconStatus("E");
					bean.setSystemStatus("Prod.Order already replaced sale by " + beanRP.getProductionOrder() + ".");
					poList.add(bean);
				} else if (countPrdSW > 0) {
					ArrayList<SwitchProdOrderDetail> listCheck = switchProdOrderService.getSwitchProdOrderDetailByPrdSW(prdOrderSW);
					String prodOrderCheck = "";
					if (listCheck.size() > 0) {
						prodOrderCheck = listCheck.get(0).getProductionOrder();
					}
					if (prdOrder.equals(prodOrderCheck)) {
						bean.setIconStatus("W");
						bean.setSystemStatus("Data already upadted.");
					} else {
						bean.setIconStatus("E");
						bean.setSystemStatus("Prod.Order already switch between " + prdOrder + " and " + prodOrderCheck + ".");
						poList.add(bean);
					}
				} else {
					ArrayList<SwitchProdOrderDetail> listSW = switchProdOrderService.getSwitchProdOrderDetailByPrd(prdOrder);
					bean = switchProdOrderService.updateSwitchProdOrderDetail(bean, "X");
					if (listSW.size() > 0) {
						String oldProdOrderSW = listSW.get(0).getProductionOrderSW();
						PCMSSecondTableDetail beanTmp = new PCMSSecondTableDetail();
						beanTmp.setProductionOrder(oldProdOrderSW);
						poListOld.add(beanTmp);
						poListOP = pCMSDetailService.getOrderPuangListByPrd(poListOld);
						poListOld = pCMSDetailService.getNormalCaseByProdOrder(this.C_PRODORDER, poListOld);
						int i = 0;
						for (i = 0; i < poListOP.size(); i ++ ) {
							poListTMP.add(poListOP.get(i));
						}
						for (i = 0; i < poListOPSW.size(); i ++ ) {
							poListTMP.add(poListOPSW.get(i));
						}
					}
					PCMSSecondTableDetail beanD = new PCMSSecondTableDetail();
					// CORE SALE LINE PRD PRDSW SALESW LINEDW
					// 1 1 A B
					// 2 1 B
					// ------------ TABLE --------------
					// X1 1 1 A B 1 1
					// X2 1 1 A A 2 1
					// ---------------------------------
					// X1
					beanD.setUserId(bean.getUserId());
					beanD.setSaleOrder(bean.getSaleOrder()); // 1
					beanD.setSaleLine(bean.getSaleLine()); // 1
					beanD.setProductionOrder(prdOrder); // A
					beanD.setProductionOrderSW(prdOrderSW); // B
					beanD.setSaleOrderSW(bean.getSaleOrder()); // 1
					beanD.setSaleLineSW(bean.getSaleLine()); // 1
					beanD = switchProdOrderService.upsertSwitchProdOrder(beanD, "O");
					// X2
//					beanD.setSaleOrder(bean.getSaleOrder());              //1
//					beanD.setSaleLine(bean.getSaleLine());                //1
//					beanD.setProductionOrder(bean.getProductionOrder());  //A
					bean.setProductionOrderSW(prdOrder); // A
					bean.setSaleOrderSW(beanL.getSaleOrder()); // 2
					bean.setSaleLineSW(beanL.getSaleLine()); // 1
					bean = switchProdOrderService.upsertSwitchProdOrder(bean, "O");
//					bean.setIconStatus("I");
//					bean.setSystemStatus("Update Success.");
					poList.add(bean);
					poList = pCMSDetailService.getSwitchProdOrderListByPrd(poList);
					poListOP = pCMSDetailService.getOrderPuangListByPrd(poList);
					poListOPSW = pCMSDetailService.getOrderPuangSWListByPrd(poList);
					if (poListOld.size() > 0) {
						poList.add(poListOld.get(0));
					}
					int i = 0;
					for (i = 0; i < poListOP.size(); i ++ ) {
						poList.add(poListOP.get(i));
					}
					for (i = 0; i < poListOPSW.size(); i ++ ) {
						poList.add(poListOPSW.get(i));
					}
					for (i = 0; i < poListTMP.size(); i ++ ) {
						poList.add(poListTMP.get(i));
					}
					poList = this.setBeanIconStatus(poList, bean);
				}
			} else {
				bean = switchProdOrderService.updateSwitchProdOrderDetail(bean, "X");
				bean.setIconStatus("E");
				bean.setSystemStatus("ProductionOrder is not in the Database or wrong data entry. ");
				poList.add(bean);
			}
		}
		return poList;
	}
	private ArrayList<PCMSSecondTableDetail> setBeanIconStatus(ArrayList<PCMSSecondTableDetail> poList,
			PCMSSecondTableDetail bean)
	{
		int i = 0;
		for (i = 0; i < poList.size(); i ++ ) {
			PCMSSecondTableDetail beanL = poList.get(i);
			beanL.setIconStatus(bean.getIconStatus());
			beanL.setSystemStatus(bean.getSystemStatus());
		}
		return poList;
	}

	public ArrayList<PCMSAllDetail> getUserStatusList()
	{
		ArrayList<PCMSAllDetail> list = fromSapMainProdService.getUserStatusDetail();
//		PCMSAllDetail bean = new PCMSAllDetail();
//		bean.setUserStatus("รอ COA ลูกค้า ok สี");
//		list.add(bean);
//		bean = new PCMSAllDetail();
//		bean.setUserStatus("ขายแล้วบางส่วน");
//		list.add(bean);
//		bean = new PCMSAllDetail();
//		bean.setUserStatus("รอตอบ CFM ตัวแทน");
//		list.add(bean);
//		bean = new PCMSAllDetail();
//		bean.setUserStatus("รอเปิดบิล");
//		list.add(bean);
		return list;
	}

	private String forPage = "Detail";

	public ArrayList<PCMSTableDetail> saveDefault(String user, ArrayList<PCMSTableDetail> poList)
	{
		ArrayList<PCMSTableDetail> list = null;
		String customerShortName = "",userStatus = "",customerName = "" ,divisionName = "";
		PCMSTableDetail bean = poList.get(0); 
		List<String> userStatusList = bean.getUserStatusList();
		List<String> cusNameList = bean.getCustomerNameList();
		List<String> cusShortNameList = bean.getCustomerShortNameList();
		List<String> divisionList = bean.getDivisionList();
		if (cusNameList.size() > 0) {
			String text = "";
			for (int i = 0; i < cusNameList.size(); i ++ ) {
				text = cusNameList.get(i);
				customerName += text;
				if (i != cusNameList.size()-1) {
					customerName += "|";
				}
			}
		}
		if (divisionList.size() > 0) {
			String text = "";
			for (int i = 0; i < divisionList.size(); i ++ ) {
				text = divisionList.get(i);
				divisionName += text;
				if (i != divisionList.size()-1) {
					divisionName += "|";
				}
			}
		}
		if (cusShortNameList.size() > 0) {
			String text = "";
			for (int i = 0; i < cusShortNameList.size(); i ++ ) {
				text = cusShortNameList.get(i);
				customerShortName += text;
				if (i != cusShortNameList.size()-1) {
					customerShortName += "|";
				}
			}
		}
		if (userStatusList.size() > 0) {
			String text = "";
			for (int i = 0; i < userStatusList.size(); i ++ ) {
				text = userStatusList.get(i);
				userStatus += text;
				if (i != userStatusList.size()-1) {
					userStatus += "|";
				}
			}
		}

		poList.get(0).setDivision(divisionName);
		poList.get(0).setUserStatus(userStatus);
		poList.get(0).setCustomerName(customerName);
		poList.get(0).setCustomerShortName(customerShortName);
		ArrayList<PCMSTableDetail> beanCheck = searchSettingService.getSearchSettingDetail(user, this.forPage);
		if (beanCheck.size() == 0) {
			list = searchSettingService.insertSearchSettingDetail(user,poList, this.forPage);
		} else {
			list = searchSettingService.updateSearchSettingDetail(user,poList, this.forPage);
		}
		return list;
	}

	public ArrayList<PCMSTableDetail> loadDefault(String user)
	{ 
		ArrayList<PCMSTableDetail> bean = searchSettingService.getSearchSettingDetail(user, this.forPage);
		return bean;
	}

	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByRowProd(ArrayList<PCMSSecondTableDetail> poList)
	{
		String prodOrder = poList.get(0).getProductionOrder();
		ArrayList<SwitchProdOrderDetail> list = switchProdOrderService.getSWProdOrderDetailByPrd(prodOrder);
		if (list.size() > 0) {
			prodOrder = list.get(0).getProductionOrder();
		}
		ArrayList<PCMSSecondTableDetail> listPST = new ArrayList<>();
		if (prodOrder.equals("")) {
			PCMSSecondTableDetail bean = new PCMSSecondTableDetail();
			bean.setIconStatus("E");
			bean.setSystemStatus("This Prod.Order already remove switch prod.order from other user.");
			listPST.add(bean);
		} else {
			listPST = switchProdOrderService.getSwitchProdOrderDetailByProdOrder(prodOrder);
		}
		return listPST;
	}
}
