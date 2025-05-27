package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.WaitTestDetail;

public interface FromSapWaitTestDao {

	ArrayList<WaitTestDetail> getFromSapWaitTestDetailByProductionOrder(String prodOrder);

}
