package dao.master;

import java.util.ArrayList;

import entities.SaleDetail;

public interface FromSapSaleDao {

	ArrayList<SaleDetail> getFromSapSaleDetailByProductionOrder(String prodOrder);

}
