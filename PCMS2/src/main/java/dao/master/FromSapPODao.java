package dao.master;

import java.util.ArrayList;

import entities.PODetail;

public interface FromSapPODao {

	ArrayList<PODetail> getFromSapPODetailByProductionOrder(String prodOrder);

}
