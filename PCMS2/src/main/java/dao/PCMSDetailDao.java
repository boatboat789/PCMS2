package dao;

import java.util.ArrayList;
import entities.ColumnHiddenDetail;
import entities.InputDateDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;

public interface PCMSDetailDao {

	ArrayList<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSTableDetail> getSaleNumberList();

	ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList);
 
	ArrayList<InputDateDetail> getCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList); 
	ArrayList<ColumnHiddenDetail> saveColSettingToServer(ColumnHiddenDetail pd);

	ArrayList<ColumnHiddenDetail> getColVisibleDetail(String user);

	ArrayList<PCMSAllDetail> getUserStatusList();

	ArrayList<PCMSAllDetail> getCustomerNameList();

	ArrayList<PCMSAllDetail> getCustomerShortNameList();

	ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> saveInputDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> getDivisionList();

	ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByRowProd(ArrayList<PCMSSecondTableDetail> poList); 

}
