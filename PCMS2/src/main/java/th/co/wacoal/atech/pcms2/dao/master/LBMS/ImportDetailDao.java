package th.co.wacoal.atech.pcms2.dao.master.LBMS;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.LBMS.ImportDetail;

public interface ImportDetailDao {

	ArrayList<ImportDetail> getImportDetailByProductionOrder(String prodOrder);
 

}
