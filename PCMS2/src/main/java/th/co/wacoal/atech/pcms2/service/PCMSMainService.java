package th.co.wacoal.atech.pcms2.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.PCMSMainDao;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

@Service
public class PCMSMainService {  
	private PCMSMainDao dao; 

	@Autowired
	public PCMSMainService(PCMSMainDao dao) { 
			this.dao = dao; 
	} 
 

	public ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList)
	{ 
		ArrayList<PCMSAllDetail> list = this.dao.getPrdDetailByRow(poList);
		return list;
	} 
	public ArrayList<PCMSTableDetail> getPCMSSumaryDetail(ArrayList<PCMSTableDetail> poList)
	{ 
		return this.dao.getPCMSSumaryDetail(poList);
	}

}
