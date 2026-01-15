package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.CustomerDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;

@Service
public class CustomerService { 
	private CustomerDao dao; 
	@Autowired
	public CustomerService(CustomerDao dao) {
		this.dao = dao; 
	}
 

	public String upsertCustomerDetail(ArrayList<CustomerDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertCustomerDetail(paList);
		return iconStatus;
	}
}
