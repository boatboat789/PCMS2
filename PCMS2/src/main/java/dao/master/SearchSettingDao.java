package dao.master;

import java.util.ArrayList;

import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail; 
public interface SearchSettingDao {

	ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId, String forPage);

	ArrayList<PCMSTableDetail> insertSearchSettingDetai(ArrayList<PCMSTableDetail> poList, String forPage);

	ArrayList<PCMSTableDetail> updateSearchSettingDetai(ArrayList<PCMSTableDetail> poList, String forPage);
   
 
 

}
