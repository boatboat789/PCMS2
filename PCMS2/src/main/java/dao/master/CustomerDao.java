package dao.master;

import java.util.ArrayList;

import entities.erp.atech.CustomerDetail;

public interface CustomerDao {

	String upsertCustomerDetail(ArrayList<CustomerDetail> paList);

}
