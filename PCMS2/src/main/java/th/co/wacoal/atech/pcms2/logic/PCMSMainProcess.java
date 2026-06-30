	package th.co.wacoal.atech.pcms2.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.service.PCMSMainService;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainProdService;
import th.co.wacoal.atech.pcms2.service.master.SearchSettingService;
@Repository
public class PCMSMainProcess {
	private final FromSapMainProdService fromSapMainProdService;
    private final SearchSettingService searchSettingService;
    private final PCMSMainService pCMSMainService;
	private String forPage = "Summary";
	@Autowired
	    public PCMSMainProcess(
	            PCMSMainService pCMSMainService,
	            FromSapMainProdService fromSapMainProdService,
	            SearchSettingService searchSettingService  ) {

	        this.fromSapMainProdService = fromSapMainProdService;
	        this.searchSettingService = searchSettingService;
			this.pCMSMainService = pCMSMainService;
	    }
	public ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList, boolean isCustomer) {
		ArrayList<PCMSTableDetail> list = this.pCMSMainService.getPCMSSumaryDetail(poList );

		if(isCustomer) {
			for (PCMSTableDetail element : list) {
				element.setCfmDetailAll("");
				element.setRollNoRemarkAll("");
			}
		}
		return list;
	} 
	public ArrayList<PCMSAllDetail> getUserStatusList() {
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
	public ArrayList<PCMSTableDetail> saveDefault(String userId, ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSTableDetail> list = null;
		String customerShortName = "", userStatus = "", customerName="" ,divisionName = "";
		PCMSTableDetail bean = poList.get(0);  
		List<String> userStatusList = bean.getUserStatusList();
		List<String> cusNameList = bean.getCustomerNameList();
		List<String> cusShortNameList = bean.getCustomerShortNameList();
		List<String> divisionList = bean.getDivisionList();
		if (cusNameList.size() > 0) {
			String text = "";
			for (int i = 0; i < cusNameList.size(); i++) {
				text = cusNameList.get(i);
				customerName += text  ;
				if (i != cusNameList.size() - 1) {
					customerName += "|";
				}
			}
		}
		if (divisionList.size() > 0) {
			String text = "";
			for (int i = 0; i < divisionList.size(); i++) {
				text = divisionList.get(i);
				divisionName += text  ;
				if (i != divisionList.size() - 1) {
					divisionName += "|";
				}
			}
		}
		if (cusShortNameList.size() > 0) {
			String text = "";
			for (int i = 0; i < cusShortNameList.size(); i++) {
				text = cusShortNameList.get(i);
				customerShortName += text;
				if (i != cusShortNameList.size() - 1) {
					customerShortName += "|";
				}
			}
		}
		if (userStatusList.size() > 0) {
			String text = "";
			for (int i = 0; i < userStatusList.size(); i++) {
				text = userStatusList.get(i);
				userStatus +=  text ;
				if (i != userStatusList.size() - 1) {
					userStatus += "|";
				}
			}
		}
		poList.get(0).setDivision(divisionName);
		poList.get(0).setUserStatus(userStatus);
		poList.get(0).setCustomerName(customerName);
		poList.get(0).setCustomerShortName(customerShortName);
		ArrayList<PCMSTableDetail> beanCheck = searchSettingService.getSearchSettingDetail(userId,this.forPage);
		if(beanCheck.size() == 0) {
			list = searchSettingService.insertSearchSettingDetail(userId, poList, this.forPage);
		}
		else {
			list = searchSettingService.updateSearchSettingDetail(userId,poList, this.forPage);
		}
		return list;
	} 
	public ArrayList<PCMSTableDetail> loadDefault(String userId) { 
		ArrayList<PCMSTableDetail> bean = searchSettingService.getSearchSettingDetail(userId,this.forPage);
		return bean;
	}
}
