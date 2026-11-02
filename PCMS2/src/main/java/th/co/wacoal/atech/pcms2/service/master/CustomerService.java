package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.CustomerDao;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;

@Service
public class CustomerService {
	private CustomerDao dao;

	@Autowired
	public CustomerService(CustomerDao dao) {
		this.dao = dao;
	}

	public ArrayList<CustomerDetail> getCustomerNameForOption()
	{
		// TODO Auto-generated method stub
		return this.dao.getCustomerNameForOption();
	}
	public ArrayList<CustomerDetail> getCustomerShortNameForOption()
	{
		// TODO Auto-generated method stub
		return this.dao.getCustomerShortNameForOption();
	}
	public ArrayList<CustomerDetail> getCustomerNameForOption(ArrayList<ConfigCustomerUserDetail> poList)
	{
		// TODO Auto-generated method stub
		return this.dao.getCustomerNameForOption(poList);
	}
	public ArrayList<CustomerDetail> getCustomerShortNameForOption(ArrayList<ConfigCustomerUserDetail> poList)
	{
		// TODO Auto-generated method stub
		return this.dao.getCustomerShortNameForOption(poList);
	}
	public String upsertCustomerDetail(ArrayList<CustomerDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertCustomerDetail(paList);
		return iconStatus;
	}

}
