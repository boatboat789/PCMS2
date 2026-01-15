package th.co.wacoal.atech.pcms2.service.master.PPMM;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.RollFromSapDao;
import th.co.wacoal.atech.pcms2.entities.PODetail;

@Service
public class RollFromSapService   { 
	private RollFromSapDao dao; 

	@Autowired
	public RollFromSapService(RollFromSapDao dao) { 
			this.dao = dao; 
	}
 
	public ArrayList<PODetail> getRollFromSapDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<PODetail> list = this.dao.getRollFromSapDetailByProductionOrder(prodOrder);
		return list;
	}
}
