package th.co.wacoal.atech.pcms2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.LogInDao;
import th.co.wacoal.atech.pcms2.entities.UserDetail;

@Service
public class LogInService { 
	private LogInDao dao; 

	@Autowired
	public LogInService(LogInDao dao) {
		this.dao = dao; 
	} 
	public UserDetail getUserDetail(String userId)
	{
		UserDetail bean = this.dao.getUserDetail(userId);
		return bean;
	}
	public UserDetail getUserDetail(String userId, String userPassword)
	{
		UserDetail bean = this.dao.getUserDetail(userId, userPassword);
		return bean;
	}
}
