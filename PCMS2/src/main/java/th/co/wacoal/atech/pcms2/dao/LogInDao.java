package th.co.wacoal.atech.pcms2.dao;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.UserDetail;


public interface LogInDao {

	UserDetail getUserDetail(String userId);

	UserDetail getUserDetail(String userId,String passWord);

	String descryptedText(String ciphertext);

	ArrayList<ConfigCustomerUserDetail> getConfigCustomerUserDetail(String userId);



}
