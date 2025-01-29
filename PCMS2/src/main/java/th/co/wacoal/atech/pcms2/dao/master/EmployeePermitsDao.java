package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.EmployeeDetail;

public interface EmployeePermitsDao {
 

	ArrayList<EmployeeDetail> getEmployeePermitsDetailByUserId(String permitId);

}
