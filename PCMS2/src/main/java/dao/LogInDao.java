package dao;

import java.util.ArrayList;

import entities.ConfigCustomerUserDetail;
import entities.UserDetail;


public interface LogInDao {

	UserDetail getUserDetail(String userId);

	UserDetail getUserDetail(String userId,String passWord);

	String descryptedText(String ciphertext);

	ArrayList<ConfigCustomerUserDetail> getConfigCustomerUserDetail(String userId);



}
