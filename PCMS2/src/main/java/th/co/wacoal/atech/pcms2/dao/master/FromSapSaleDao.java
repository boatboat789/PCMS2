package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.SaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;

public interface FromSapSaleDao {

	ArrayList<SaleDetail> getFromSapSaleDetailByProductionOrder(String prodOrder);

	String upsertFromSapSaleDetail(ArrayList<FromErpSaleDetail> paList);

}
