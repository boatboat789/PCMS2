package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.SaleInputDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleInputDetail;

public interface FromSapSaleInputDao {

	ArrayList<SaleInputDetail> getFromSapSaleInputDetailByProductionOrder(String prodOrder);

	String upsertFromSapSaleInputDetail(ArrayList<FromErpSaleInputDetail> paList);

}
