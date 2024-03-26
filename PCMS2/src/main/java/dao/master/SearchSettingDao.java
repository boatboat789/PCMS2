package dao.master;

import java.util.ArrayList;

import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail; 
public interface SearchSettingDao {

	ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId, String forPage);
 

	ArrayList<PCMSTableDetail> insertSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage);
	ArrayList<PCMSTableDetail> updateSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage);
   
 
 

}
