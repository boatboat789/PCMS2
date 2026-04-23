package th.co.wacoal.atech.pcms2.service.master.PPMM;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.ShopFloorControlDao;
import th.co.wacoal.atech.pcms2.entities.PPMM.ShopFloorControlDetail;

@Service
public class ShopFloorControlService  { 
	private ShopFloorControlDao dao;  

	@Autowired
	public ShopFloorControlService(ShopFloorControlDao dao) { 
			this.dao = dao; 
	}
 
	public ArrayList<ShopFloorControlDetail> getShopFloorControlDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<ShopFloorControlDetail> list = this.dao.getShopFloorControlDetailByProductionOrder(prodOrder);
		return list;
	}
}
