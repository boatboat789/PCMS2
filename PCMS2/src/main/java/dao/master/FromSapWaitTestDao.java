package dao.master;

import java.util.ArrayList;

import entities.WaitTestDetail;

public interface FromSapWaitTestDao {

	ArrayList<WaitTestDetail> getFromSapWaitTestDetailByProductionOrder(String prodOrder);

}
