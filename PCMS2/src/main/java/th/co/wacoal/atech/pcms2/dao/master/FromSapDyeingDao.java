package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.DyeingDetail;

public interface FromSapDyeingDao {

	ArrayList<DyeingDetail> getFromSapDyeingDetailByProductionOrder(String prodOrder);

}
