package dao.master;

import java.util.ArrayList;

import entities.PackingDetail;

public interface FromSapPackingDao {

	ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder);

}
