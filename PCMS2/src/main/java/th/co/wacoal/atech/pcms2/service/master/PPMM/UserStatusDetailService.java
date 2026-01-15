package th.co.wacoal.atech.pcms2.service.master.PPMM;

import java.util.ArrayList;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.UserStatusDetailDao;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;

@Service
public class UserStatusDetailService {  
	private UserStatusDetailDao dao;  

	@Autowired
	public UserStatusDetailService(UserStatusDetailDao dao) { 
			this.dao = dao; 
	} 

	public ArrayList<PCMSAllDetail> getUserStatusDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getUserStatusDetail();
		return list;
	}
}
