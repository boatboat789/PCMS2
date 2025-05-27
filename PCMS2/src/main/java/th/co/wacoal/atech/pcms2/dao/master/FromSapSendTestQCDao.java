package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.SendTestQCDetail;

public interface FromSapSendTestQCDao {

	ArrayList<SendTestQCDetail> getFromSapSendTestQCByProductionOrder(String prodOrder);

}
