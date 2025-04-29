package th.co.wacoal.atech.pcms2.dao;

import java.util.ArrayList;
import java.util.List;

import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

public interface PCMSDetailDao {

	ArrayList<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList);


	ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList);
	ArrayList<PCMSAllDetail> getUserStatusList();


	ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> saveInputDetail(ArrayList<PCMSSecondTableDetail> poList);


	ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByRowProd(ArrayList<PCMSSecondTableDetail> poList);


//	String handlerTempTableCustomerSearchList(List<String> customerNameList, List<String> customerShortNameList);


}
