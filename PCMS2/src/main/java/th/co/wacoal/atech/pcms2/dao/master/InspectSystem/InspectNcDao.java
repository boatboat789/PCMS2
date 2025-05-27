package th.co.wacoal.atech.pcms2.dao.master.InspectSystem;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.NCDetail;

public interface InspectNcDao {

	ArrayList<NCDetail> getInspectNcByProductionOrder(String prodOrder);

}
