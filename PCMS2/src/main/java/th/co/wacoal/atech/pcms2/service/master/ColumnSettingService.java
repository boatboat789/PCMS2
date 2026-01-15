package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.ColumnSettingDao;
import th.co.wacoal.atech.pcms2.entities.ColumnHiddenDetail;

@Service
public class ColumnSettingService  { 
	private ColumnSettingDao dao; 
	@Autowired
	public ColumnSettingService(ColumnSettingDao dao) {
		this.dao = dao; 
	} 
	public ArrayList<ColumnHiddenDetail> getColumnVisibleDetail(String user)
	{
		ArrayList<ColumnHiddenDetail> list = this.dao.getColumnVisibleDetail(user);
		return list;
	}

	public ArrayList<ColumnHiddenDetail> upsertColumnSettingDetail(ColumnHiddenDetail pd)
	{
		// TODO Auto-generated method stub
		ArrayList<ColumnHiddenDetail> list = this.dao.upsertColumnSettingDetail(pd);
		return list;
	}

	public ArrayList<ColumnHiddenDetail> upsertColumnVisibleSummary(ColumnHiddenDetail pd)
	{
		// TODO Auto-generated method stub
		ArrayList<ColumnHiddenDetail> list = this.dao.upsertColumnVisibleSummary(pd);
		return list;
	}

}
