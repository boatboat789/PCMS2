package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
public interface SearchSettingDao {

	ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId, String forPage);


	ArrayList<PCMSTableDetail> insertSearchSettingDetail(String user, ArrayList<PCMSTableDetail> poList, String forPage);
	ArrayList<PCMSTableDetail> updateSearchSettingDetail(String user, ArrayList<PCMSTableDetail> poList, String forPage);




}
