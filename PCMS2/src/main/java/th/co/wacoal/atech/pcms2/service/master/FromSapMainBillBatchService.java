package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainBillBatchDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainBillBatchDetail;

@Service
public class FromSapMainBillBatchService { 
	private FromSapMainBillBatchDao dao; 
    @Autowired
	public FromSapMainBillBatchService(FromSapMainBillBatchDao dao) {
		this.dao = dao; 
	} 
	public  String upsertFromSapMainBillBatchDetail( ArrayList<FromErpMainBillBatchDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapMainBillBatchDetail(paList );
		return iconStatus;
	}
}
