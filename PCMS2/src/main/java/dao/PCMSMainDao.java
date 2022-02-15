package dao;

import java.util.ArrayList;
import java.util.List;

import entities.ColumnHiddenDetail;
import entities.PCMSAllDetail; 
import entities.PCMSTableDetail;

public interface PCMSMainDao {

	ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSTableDetail> getSaleNumberList();

	ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSAllDetail> getUserStatusList();

	ArrayList<ColumnHiddenDetail> getColVisibleDetail(String user);

	ArrayList<ColumnHiddenDetail> saveColSettingToServer(ColumnHiddenDetail pd);

	ArrayList<PCMSAllDetail> getCustomerNameList();

	ArrayList<PCMSAllDetail> getCustomerShortNameList();

	ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList);
 

}
