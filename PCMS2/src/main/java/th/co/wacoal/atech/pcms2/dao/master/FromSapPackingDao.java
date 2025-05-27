package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;

public interface FromSapPackingDao {

	ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder);

	String upsertFromSapPackingDetail(ArrayList<FromErpPackingDetail> paList);

}
