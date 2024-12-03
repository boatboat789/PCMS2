package dao.master.InspectSystem;

import java.util.ArrayList;

import entities.PPMM.InspectOrdersDetail;

public interface InspectOrdersDao {
 
	ArrayList<InspectOrdersDetail> getInspectOrdersByProductionOrder(String prodOrder);

}
