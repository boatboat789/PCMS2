package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapReceipeDao;
import th.co.wacoal.atech.pcms2.entities.ReceipeDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpReceipeDetail;

@Service
public class FromSapReceipeService  { 
	private FromSapReceipeDao dao; 
	@Autowired
	public FromSapReceipeService(FromSapReceipeDao dao) {
		this.dao = dao; 
	}
 

	public ArrayList<ReceipeDetail> getFromSapReceipeDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<ReceipeDetail> list = this.dao.getFromSapReceipeDetailByProductionOrder(prodOrder);
		return list;
	}

	public void upsertFromSapReceipeDetail(ArrayList<FromErpReceipeDetail> ferdList)
	{
		// TODO Auto-generated method stub
		this.dao.upsertFromSapReceipeDetail(ferdList);

	}
}
