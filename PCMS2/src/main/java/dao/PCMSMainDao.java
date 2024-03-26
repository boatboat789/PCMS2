package dao;

import java.util.ArrayList;
import entities.ColumnHiddenDetail;
import entities.ConfigCustomerUserDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;

public interface PCMSMainDao {

	ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList, boolean isCustomer);
 

	ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSAllDetail> getUserStatusList(); 

	ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList);
 
  
 

}
