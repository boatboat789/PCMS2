package th.co.wacoal.atech.pcms2.dao.master.PPMM;

import java.util.ArrayList; 
import th.co.wacoal.atech.pcms2.entities.PODetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPODetail;

public interface RollFromSapDao {

	ArrayList<PODetail> getRollFromSapDetailByProductionOrder(String prodOrder);

	String upsertRollFromSapFromERPPODetail(ArrayList<FromErpPODetail> paList);

}
