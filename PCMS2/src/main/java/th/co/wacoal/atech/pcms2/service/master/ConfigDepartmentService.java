package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.ConfigDepartmentDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;

@Service
public class ConfigDepartmentService  { 
	private ConfigDepartmentDao dao; 
	@Autowired
	public ConfigDepartmentService(ConfigDepartmentDao dao) {
		this.dao = dao; 
	} 
	public ArrayList<PCMSSecondTableDetail> getDelayedDepartmentList()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getDelayedDepartmentList();
		return list;
	}

}
