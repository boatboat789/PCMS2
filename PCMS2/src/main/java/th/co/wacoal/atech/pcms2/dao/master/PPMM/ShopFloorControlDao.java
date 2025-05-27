package th.co.wacoal.atech.pcms2.dao.master.PPMM;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PPMM.ShopFloorControlDetail;

public interface ShopFloorControlDao {
 
	ArrayList<ShopFloorControlDetail> getShopFloorControlDetailByProductionOrder(String prodOrder);

}
