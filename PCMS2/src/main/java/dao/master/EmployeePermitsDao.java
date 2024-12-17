package dao.master;

import java.util.ArrayList;

import entities.EmployeeDetail;

public interface EmployeePermitsDao {
 

	ArrayList<EmployeeDetail> getEmployeePermitsDetailByUserId(String permitId);

}
