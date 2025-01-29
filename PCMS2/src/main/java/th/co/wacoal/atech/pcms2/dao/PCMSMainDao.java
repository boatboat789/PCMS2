package th.co.wacoal.atech.pcms2.dao;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

public interface PCMSMainDao {

	ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList, boolean isCustomer);


	ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSAllDetail> getUserStatusList();

	ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList);




}
