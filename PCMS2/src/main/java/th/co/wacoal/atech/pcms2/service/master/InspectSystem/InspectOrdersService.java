package th.co.wacoal.atech.pcms2.service.master.InspectSystem;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.InspectSystem.InspectOrdersDao;
import th.co.wacoal.atech.pcms2.entities.PPMM.InspectOrdersDetail;

@Component
public class InspectOrdersService  { 
	private InspectOrdersDao dao; 

	@Autowired
	public InspectOrdersService(InspectOrdersDao dao) { 
			this.dao = dao; 
	} 
	public ArrayList<InspectOrdersDetail> getInspectOrdersByProductionOrder(String prodOrder)
	{
		ArrayList<InspectOrdersDetail> list = this.dao.getInspectOrdersByProductionOrder(prodOrder);
		return list;
	}
}
