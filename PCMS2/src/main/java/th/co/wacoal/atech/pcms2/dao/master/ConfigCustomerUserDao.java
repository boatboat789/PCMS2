package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;

public interface ConfigCustomerUserDao {

	ArrayList<ConfigCustomerUserDetail> getConfigCustomerUserDetail(String userId);

}
