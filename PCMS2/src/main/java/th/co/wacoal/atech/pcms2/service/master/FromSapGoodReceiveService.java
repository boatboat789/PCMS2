package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapGoodReceiveDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;

@Service
public class FromSapGoodReceiveService { 
	private FromSapGoodReceiveDao dao; 
    @Autowired
	public FromSapGoodReceiveService(FromSapGoodReceiveDao dao) {
		this.dao = dao; 
	} 
	public  String upsertFromSapGoodReceiveDetail( ArrayList<FromErpGoodReceiveDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapGoodReceiveDetail(paList );
		return iconStatus;
	}
}
