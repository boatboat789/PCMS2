package dao.master;

import java.util.ArrayList;

import entities.CFMDetail;
import entities.erp.atech.FromErpCFMDetail;

public interface FromSapCFMDao {

	ArrayList<CFMDetail> getFromSapCFMDetailByProductionOrder(String prodOrder);

	String upsertFromSapCFMDetail(ArrayList<FromErpCFMDetail> paList);

}
