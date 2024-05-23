package dao.master;

import java.util.ArrayList;

import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;

public interface FromSapMainProdDao {

	ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrder);

	ArrayList<PCMSAllDetail> getUserStatusDetail();


}
