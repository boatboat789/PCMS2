package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;

public interface CustomerDao {

	ArrayList<CustomerDetail> getCustomerDetail();
	
	String upsertCustomerDetail(ArrayList<CustomerDetail> paList);
  

	ArrayList<CustomerDetail> getCustomerNameForOption();

	ArrayList<CustomerDetail> getCustomerShortNameForOption();

	ArrayList<CustomerDetail> getCustomerShortNameForOption(ArrayList<ConfigCustomerUserDetail> poList);

	ArrayList<CustomerDetail> getCustomerNameForOption(ArrayList<ConfigCustomerUserDetail> poList);


}
