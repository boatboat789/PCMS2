package dao.master;

import java.util.ArrayList;

import entities.DyeingDetail;

public interface FromSapDyeingDao {

	ArrayList<DyeingDetail> getFromSapDyeingDetailByProductionOrder(String prodOrder);

}
