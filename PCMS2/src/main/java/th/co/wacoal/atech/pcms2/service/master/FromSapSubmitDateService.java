package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapSubmitDateDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;

@Service
public class FromSapSubmitDateService { 
	private FromSapSubmitDateDao dao; 
    @Autowired
	public FromSapSubmitDateService(FromSapSubmitDateDao dao) {
		this.dao = dao; 
	}
 
 
	public  String upsertFromSapSubmitDateDetail( ArrayList<FromErpSubmitDateDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapSubmitDateDetail(paList );
		return iconStatus;
	}

	public ArrayList<InputDateDetail> getSubmitDateDetail(ArrayList<PCMSTableDetail> poList)
	{
		 ArrayList<InputDateDetail> list = this.dao.getSubmitDateDetail(poList );
		return list;
	}
}
