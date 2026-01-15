package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapSaleDao;
import th.co.wacoal.atech.pcms2.entities.SaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;

@Service
public class FromSapSaleService   { 
	private FromSapSaleDao dao; 
    @Autowired
	public FromSapSaleService (FromSapSaleDao dao) {
		this.dao = dao; 
	}
 

	public ArrayList<SaleDetail> getFromSapSaleDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<SaleDetail> list = this.dao.getFromSapSaleDetailByProductionOrder(prodOrder);
		return list;
	}
	public String   upsertFromSapSaleDetail(ArrayList<FromErpSaleDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertFromSapSaleDetail(paList);
		return iconStatus;
	}
}
