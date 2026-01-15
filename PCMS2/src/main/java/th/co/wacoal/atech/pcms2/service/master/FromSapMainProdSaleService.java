package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainProdSaleDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;

@Service
public class FromSapMainProdSaleService  { 
	private FromSapMainProdSaleDao dao; 
    @Autowired
	public FromSapMainProdSaleService(FromSapMainProdSaleDao dao) {
		this.dao = dao; 
	}
 
	public  String upsertFromSapMainProdSaleDetail( ArrayList<FromErpMainProdSaleDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapMainProdSaleDetail(paList );
		return iconStatus;
	}
}
