package dao;

import java.util.ArrayList;
import java.util.List;

import entities.PCMSAllDetail; 
import entities.PCMSTableDetail;

public interface PCMSMainDao {

	ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSTableDetail> getSaleNumberList();

	ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList);

	ArrayList<PCMSAllDetail> getUserStatusList();
 

}
