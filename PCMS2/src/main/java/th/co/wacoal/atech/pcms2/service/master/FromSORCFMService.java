package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSORCFMDao;
import th.co.wacoal.atech.pcms2.entities.SORDetail;

@Service
public class FromSORCFMService  { 
	private FromSORCFMDao dao; 
	@Autowired
	public FromSORCFMService(FromSORCFMDao dao) {
		this.dao = dao; 
	}
 
	public String upSertFromSORCFMDetail(ArrayList<SORDetail> list2)
	{
		// TODO Auto-generated method stub
		return this.dao.upSertFromSORCFMDetail(list2);
	}

}
