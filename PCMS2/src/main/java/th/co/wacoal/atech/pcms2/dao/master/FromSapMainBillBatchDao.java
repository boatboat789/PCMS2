package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainBillBatchDetail;

public interface FromSapMainBillBatchDao {

	String upsertFromSapMainBillBatchDetail(ArrayList<FromErpMainBillBatchDetail> paList);
 

}
