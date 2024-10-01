package dao.master;

import java.util.ArrayList;

import entities.CFMDetail;

public interface FromSapCFMDao {

	ArrayList<CFMDetail> getFromSapCFMDetailByProductionOrder(String prodOrder);

}
