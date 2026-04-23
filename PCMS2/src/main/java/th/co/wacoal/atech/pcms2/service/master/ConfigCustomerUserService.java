package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.ConfigCustomerUserDao;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;

@Service
public class ConfigCustomerUserService {
	private ConfigCustomerUserDao dao;

	@Autowired
	public ConfigCustomerUserService(ConfigCustomerUserDao dao) {
		this.dao = dao;
	}

	public ArrayList<ConfigCustomerUserDetail> getConfigCustomerUserDetail(String userId)
	{
		// TODO Auto-generated method stub
		ArrayList<ConfigCustomerUserDetail> list = this.dao.getConfigCustomerUserDetail(userId);
		return list;
	}

}
