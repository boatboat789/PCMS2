package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapCFMDao;
import th.co.wacoal.atech.pcms2.entities.CFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;

@Service
public class FromSapCFMService  { 
	private FromSapCFMDao dao; 
    @Autowired
	public FromSapCFMService(FromSapCFMDao dao) {
		this.dao = dao; 

	} 

	public ArrayList<CFMDetail> getFromSapCFMDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<CFMDetail> list = this.dao.getFromSapCFMDetailByProductionOrder(prodOrder);
		return list;
	}
	public  String upsertFromSapCFMDetail( ArrayList<FromErpCFMDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapCFMDetail(paList );
		return iconStatus;
	}
}
