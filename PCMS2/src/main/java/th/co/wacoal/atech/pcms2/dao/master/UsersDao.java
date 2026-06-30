package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.UserDetail;

public interface UsersDao {

	ArrayList<UserDetail> getUsersByUserId(String userId);

	ArrayList<UserDetail> getUsers();

	int updateUserPermit(String userId, String permitId, String changeBy);

}
