package dao.master.InspectSystem;

import java.util.ArrayList;

import entities.NCDetail;

public interface InspectNcDao {

	ArrayList<NCDetail> getInspectNcByProductionOrder(String prodOrder);

}
