package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.SearchSettingDao;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

@Component
public class SearchSettingService   { 
	private SearchSettingDao dao; 

	@Autowired
	public SearchSettingService(SearchSettingDao dao) {
		this.dao = dao; 
	}
 
	public ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId, String forPage)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.getSearchSettingDetail(userId, forPage);
		return list;
	}

	public ArrayList<PCMSTableDetail> insertSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.insertSearchSettingDetail(poList, forPage);
		return list;
	}

	public ArrayList<PCMSTableDetail> updateSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.updateSearchSettingDetail(poList, forPage);
		return list;
	}

}
