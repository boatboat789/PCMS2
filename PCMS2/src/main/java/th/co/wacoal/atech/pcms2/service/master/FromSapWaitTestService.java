package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapWaitTestDao;
import th.co.wacoal.atech.pcms2.entities.WaitTestDetail;

@Service
public class FromSapWaitTestService { 
	private FromSapWaitTestDao dao; 
    @Autowired
	public FromSapWaitTestService(FromSapWaitTestDao dao) {
		this.dao = dao; 
	} 

	public ArrayList<WaitTestDetail> getFromSapWaitTestDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<WaitTestDetail> list = this.dao.getFromSapWaitTestDetailByProductionOrder(prodOrder);
		return list;
	}
}
