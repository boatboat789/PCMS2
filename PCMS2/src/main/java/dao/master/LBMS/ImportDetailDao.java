package dao.master.LBMS;

import java.util.ArrayList;

import entities.LBMS.ImportDetail;

public interface ImportDetailDao {

	ArrayList<ImportDetail> getImportDetailByProductionOrder(String prodOrder);
 

}
