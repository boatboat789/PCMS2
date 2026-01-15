package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapSaleInputDao;
import th.co.wacoal.atech.pcms2.entities.SaleInputDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleInputDetail;

@Service
public class FromSapSaleInputService   { 
	private FromSapSaleInputDao dao; 

    @Autowired
	public FromSapSaleInputService (FromSapSaleInputDao dao) {
		this.dao = dao; 
	} 
	public ArrayList<SaleInputDetail> getFromSapSaleInputDetailByProductionOrder(String prodOrder)
	{ 
		ArrayList<SaleInputDetail> list = this.dao.getFromSapSaleInputDetailByProductionOrder(prodOrder);
		return list;
	}
	public String upsertFromSapSaleInputDetail(ArrayList<FromErpSaleInputDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertFromSapSaleInputDetail(paList);
		return iconStatus;
	}
}
