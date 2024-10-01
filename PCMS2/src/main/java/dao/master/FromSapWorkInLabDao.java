package dao.master;

import java.util.ArrayList;

import entities.WorkInLabDetail;

public interface FromSapWorkInLabDao {

	ArrayList<WorkInLabDetail> getFromSapWorkInLabDetailByProductionOrder(String prodOrder);

}
