package th.co.wacoal.atech.pcms2.dao;

import th.co.wacoal.atech.pcms2.entities.UserDetail;


public interface LogInDao {

	UserDetail getUserDetail(String userId);

	UserDetail getUserDetail(String userId,String passWord);

	String descryptedText(String ciphertext);
 

}
