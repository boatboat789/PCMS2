package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapWorkInLabDao;
import th.co.wacoal.atech.pcms2.entities.WorkInLabDetail;

@Service
public class FromSapWorkInLabService  { 
	private FromSapWorkInLabDao dao; 
    @Autowired
	public FromSapWorkInLabService(FromSapWorkInLabDao dao) {
		this.dao = dao; 
	} 

	public ArrayList<WorkInLabDetail> getFromSapWorkInLabDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<WorkInLabDetail> list = this.dao.getFromSapWorkInLabDetailByProductionOrder(prodOrder);
		return list;
	}
}
