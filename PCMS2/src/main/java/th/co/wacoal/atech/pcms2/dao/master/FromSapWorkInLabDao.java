package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.WorkInLabDetail;

public interface FromSapWorkInLabDao {

	ArrayList<WorkInLabDetail> getFromSapWorkInLabDetailByProductionOrder(String prodOrder);

}
