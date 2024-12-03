package dao.master;

import java.util.ArrayList;

import entities.SaleInputDetail;
import entities.erp.atech.FromErpSaleInputDetail;

public interface FromSapSaleInputDao {

	ArrayList<SaleInputDetail> getFromSapSaleInputDetailByProductionOrder(String prodOrder);

	String upsertFromSapSaleInputDetail(ArrayList<FromErpSaleInputDetail> paList);

}
