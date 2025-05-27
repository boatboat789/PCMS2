package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
public interface SearchSettingDao {

	ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId, String forPage);


	ArrayList<PCMSTableDetail> insertSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage);
	ArrayList<PCMSTableDetail> updateSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage);




}
