package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;

public interface FromSapGoodReceiveDao {

	String upsertFromSapGoodReceiveDetail(ArrayList<FromErpGoodReceiveDetail> paList);

}
