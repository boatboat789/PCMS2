package dao.master;

import java.util.ArrayList;

import entities.InspectDetail;

public interface FromSapInspectDao {

	ArrayList<InspectDetail> getFromSapInspectDetailByProductionOrder(String prodOrder);

}
