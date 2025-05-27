package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;

public interface FromSapMainProdDao {

	ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrder);

	ArrayList<PCMSAllDetail> getUserStatusDetail();

	String upsertFromSapMainProdDetail(ArrayList<FromErpMainProdDetail> paList);


}
