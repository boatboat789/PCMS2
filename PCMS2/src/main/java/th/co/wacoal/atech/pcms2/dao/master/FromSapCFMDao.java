package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.CFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;

public interface FromSapCFMDao {

	ArrayList<CFMDetail> getFromSapCFMDetailByProductionOrder(String prodOrder);

	String upsertFromSapCFMDetail(ArrayList<FromErpCFMDetail> paList);

}
