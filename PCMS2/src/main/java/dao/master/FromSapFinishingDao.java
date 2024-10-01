package dao.master;

import java.util.ArrayList;

import entities.FinishingDetail;

public interface FromSapFinishingDao {

	ArrayList<FinishingDetail> getFromSapFinishingDetailByProductionOrder(String prodOrder);

}
