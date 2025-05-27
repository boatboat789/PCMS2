package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;

public interface FromSapMainProdSaleDao {

	String upsertFromSapMainProdSaleDetail(ArrayList<FromErpMainProdSaleDetail> paList);

}
