package dao.master;

import java.util.ArrayList;

import entities.SaleDetail;
import entities.erp.atech.FromErpSaleDetail;

public interface FromSapSaleDao {

	ArrayList<SaleDetail> getFromSapSaleDetailByProductionOrder(String prodOrder);

	String upsertFromSapSaleDetail(ArrayList<FromErpSaleDetail> paList);

}
