package dao.master;

import java.util.ArrayList;

import entities.PODetail;
import entities.erp.atech.FromErpPODetail;

public interface FromSapPODao {

	ArrayList<PODetail> getFromSapPODetailByProductionOrder(String prodOrder);

	String upsertFromSapPODetail(ArrayList<FromErpPODetail> paList);

}
