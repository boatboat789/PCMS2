package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.EmployeePermitsDao;
import th.co.wacoal.atech.pcms2.entities.EmployeeDetail;

@Service
public class EmployeePermitsService   { 
	private EmployeePermitsDao dao; 
    @Autowired
	public EmployeePermitsService(EmployeePermitsDao dao) {
		this.dao = dao; 
	} 
	public ArrayList<EmployeeDetail> getEmployeePermitsDetailByUserId(String permitId)
	{
		ArrayList<EmployeeDetail> list = this.dao.getEmployeePermitsDetailByUserId(permitId);
		return list;
	}

	public String upsertEmployeePermits(ArrayList<EmployeeDetail> poList, String webApp)
	{
		String list = this.dao.upsertEmployeePermits(poList, webApp);
		return list;
	}
}
