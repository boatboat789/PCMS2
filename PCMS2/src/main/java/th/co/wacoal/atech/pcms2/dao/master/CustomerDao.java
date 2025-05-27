package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;

public interface CustomerDao {

	String upsertCustomerDetail(ArrayList<CustomerDetail> paList);

}
