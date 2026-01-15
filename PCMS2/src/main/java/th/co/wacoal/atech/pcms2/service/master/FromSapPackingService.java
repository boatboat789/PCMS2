package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapPackingDao;
import th.co.wacoal.atech.pcms2.entities.PackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;

@Service
public class FromSapPackingService  { 
	private FromSapPackingDao dao; 
    @Autowired
	public FromSapPackingService(FromSapPackingDao dao) {
		this.dao = dao; 
	} 

	public ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<PackingDetail> list = this.dao.getFromSapPackingDetailByProductionOrder(prodOrder);
		return list;
	}
 
	public String upsertFromSapPackingDetail(ArrayList<FromErpPackingDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertFromSapPackingDetail(paList);
		return iconStatus;
		
	}
}
