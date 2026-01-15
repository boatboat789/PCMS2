package th.co.wacoal.atech.pcms2.service.master.InspectSystem;

import java.util.ArrayList;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.InspectSystem.InspectNcDao;
import th.co.wacoal.atech.pcms2.entities.NCDetail; 

@Service
public class InspectNcService   {  
	private InspectNcDao dao; 
	@Autowired
	public InspectNcService(InspectNcDao dao) { 
			this.dao =  dao; 
	} 
	public ArrayList<NCDetail> getInspectNcByProductionOrder(String prodOrder)
	{
		ArrayList<NCDetail> list = this.dao.getInspectNcByProductionOrder(prodOrder);
		return list;
	}
}
