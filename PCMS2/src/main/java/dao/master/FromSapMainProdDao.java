package dao.master;

import java.util.ArrayList;

import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.erp.atech.FromErpMainProdDetail;

public interface FromSapMainProdDao {

	ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrder);

	ArrayList<PCMSAllDetail> getUserStatusDetail();

	String upsertFromSapMainProdDetail(ArrayList<FromErpMainProdDetail> paList);


}
