package th.co.wacoal.atech.pcms2.dao.master.InspectSystem;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PPMM.InspectOrdersDetail;

public interface InspectOrdersDao {
 
	ArrayList<InspectOrdersDetail> getInspectOrdersByProductionOrder(String prodOrder);

}
