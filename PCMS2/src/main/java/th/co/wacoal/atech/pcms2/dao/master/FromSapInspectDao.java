package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.InspectDetail;

public interface FromSapInspectDao {

	ArrayList<InspectDetail> getFromSapInspectDetailByProductionOrder(String prodOrder);

}
