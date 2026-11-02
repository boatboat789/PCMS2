package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.PermitsDao;
import th.co.wacoal.atech.pcms2.dao.master.UsersDao;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.UserDetail;

@Service
public class PermitsService  {
    private final PermitsDao dao;
    private final UsersDao usersDao;

	@Autowired
	public PermitsService(PermitsDao dao, UsersDao usersDao) {
		this.dao = dao;
		this.usersDao = usersDao;
	}

	public ArrayList<PermitDetail> getEmployeePermitsDetailByPermitId(String userId, String permitId)
	{
		return this.dao.getEmployeePermitsDetailByPermitId(userId, permitId);
	}

	public ArrayList<PermitDetail> getPermitsDetail()
	{
		return this.dao.getPermitsDetail();
	}

	public ArrayList<PermitDetail> getPermitsDetailByPermitId(String permitId)
	{
		return this.dao.getPermitsDetailByPermitId(permitId);
	}

	public int updatePermit(PermitDetail permit, String changeBy)
	{
		return this.dao.updatePermit(permit, changeBy);
	}

	public ArrayList<UserDetail> getUsers()
	{
		return this.usersDao.getUsers();
	}

	public int updateUserPermit(String userId, String permitId, String changeBy)
	{
		return this.usersDao.updateUserPermit(userId, permitId, changeBy);
	}
}
