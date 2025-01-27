package dao.master ;

import java.util.ArrayList;

import entities.ReceipeDetail;
import entities.erp.atech.FromErpReceipeDetail;

public interface FromSapReceipeDao {

	ArrayList<ReceipeDetail> getFromSapReceipeDetailByProductionOrder(String prodOrder);

	String upsertFromSapReceipeDetail(ArrayList<FromErpReceipeDetail> paList);

}
