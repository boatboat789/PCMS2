package dao;

import java.util.ArrayList;
import java.util.List;

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

	ArrayList<ColumnHiddenDetail> getColHiddenDetail(String user);

	ArrayList<PCMSAllDetail> getUserStatusList(); 

}
