package dao.master.PPMM;

import java.util.ArrayList;

import entities.PPMM.ShopFloorControlDetail;

public interface ShopFloorControlDao {
 
	ArrayList<ShopFloorControlDetail> getShopFloorControlDetailByProductionOrder(String prodOrder);

}
