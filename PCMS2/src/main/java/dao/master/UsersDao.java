package dao.master;

import java.util.ArrayList;

import entities.EmployeeDetail;
import entities.UserDetail;

public interface UsersDao {

	ArrayList<UserDetail> getUsersByUserId(String userId);

	ArrayList<UserDetail> getUsers(); 

}
