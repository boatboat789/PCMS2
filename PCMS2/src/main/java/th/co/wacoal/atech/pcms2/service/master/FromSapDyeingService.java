package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapDyeingDao;
import th.co.wacoal.atech.pcms2.entities.DyeingDetail;

@Service
public class FromSapDyeingService { 
	private FromSapDyeingDao dao; 
    @Autowired
	public FromSapDyeingService(FromSapDyeingDao dao) {
		this.dao = dao; 
	} 
	public ArrayList<DyeingDetail> getFromSapDyeingDetailByProductionOrder(String prodOrder) 
	{
		// TODO Auto-generated method stub
		ArrayList<DyeingDetail> list = this.dao.getFromSapDyeingDetailByProductionOrder(prodOrder);
		return list;
	} 
}
