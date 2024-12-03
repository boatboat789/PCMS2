package dao.master;

import java.util.ArrayList;

import entities.erp.atech.FromErpMainProdSaleDetail;

public interface FromSapMainProdSaleDao {

	String upsertFromSapMainProdSaleDetail(ArrayList<FromErpMainProdSaleDetail> paList);

}
