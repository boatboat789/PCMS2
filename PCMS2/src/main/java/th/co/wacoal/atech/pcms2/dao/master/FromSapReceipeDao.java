package th.co.wacoal.atech.pcms2.dao.master ;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.ReceipeDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpReceipeDetail;

public interface FromSapReceipeDao {

	ArrayList<ReceipeDetail> getFromSapReceipeDetailByProductionOrder(String prodOrder);

	String upsertFromSapReceipeDetail(ArrayList<FromErpReceipeDetail> paList);

}
