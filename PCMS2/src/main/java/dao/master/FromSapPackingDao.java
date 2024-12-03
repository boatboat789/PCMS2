package dao.master;

import java.util.ArrayList;

import entities.PackingDetail;
import entities.erp.atech.FromErpPackingDetail;

public interface FromSapPackingDao {

	ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder);

	String upsertFromSapPackingDetail(ArrayList<FromErpPackingDetail> paList);

}
