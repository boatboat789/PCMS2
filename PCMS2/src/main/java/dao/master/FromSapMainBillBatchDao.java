package dao.master;

import java.util.ArrayList;

import entities.erp.atech.FromErpMainBillBatchDetail;

public interface FromSapMainBillBatchDao {

	String upsertFromSapMainBillBatchDetail(ArrayList<FromErpMainBillBatchDetail> paList);
 

}
