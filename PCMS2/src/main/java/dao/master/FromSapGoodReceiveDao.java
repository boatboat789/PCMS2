package dao.master;

import java.util.ArrayList;

import entities.erp.atech.FromErpGoodReceiveDetail;

public interface FromSapGoodReceiveDao {

	String upsertFromSapGoodReceiveDetail(ArrayList<FromErpGoodReceiveDetail> paList);

}
