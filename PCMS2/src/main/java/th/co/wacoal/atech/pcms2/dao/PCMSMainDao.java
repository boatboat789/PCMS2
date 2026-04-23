package th.co.wacoal.atech.pcms2.dao;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

public interface PCMSMainDao {

	ArrayList<PCMSTableDetail> getPCMSSumaryDetail(ArrayList<PCMSTableDetail> poList); 
	ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList);





}
