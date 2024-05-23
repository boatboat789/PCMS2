package dao;

import java.util.ArrayList;

import entities.InputDateDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;

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


}
