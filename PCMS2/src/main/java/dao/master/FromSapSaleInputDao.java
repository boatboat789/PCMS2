package dao.master;

import java.util.ArrayList;

import entities.SaleInputDetail;

public interface FromSapSaleInputDao {

	ArrayList<SaleInputDetail> getFromSapSaleInputDetailByProductionOrder(String prodOrder);

}
