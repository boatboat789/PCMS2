package dao.master;

import java.util.ArrayList;

import entities.erp.atech.FromErpSubmitDateDetail;

public interface FromSapSubmitDateDao {

	String upsertFromSapSubmitDateDetail(ArrayList<FromErpSubmitDateDetail> paList);

}
