package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.FinishingDetail;

public interface FromSapFinishingDao {

	ArrayList<FinishingDetail> getFromSapFinishingDetailByProductionOrder(String prodOrder);

}
