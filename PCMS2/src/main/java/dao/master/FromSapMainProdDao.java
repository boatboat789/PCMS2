package dao.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.SORDetail;

public interface FromSapMainProdDao {

	ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrder);

	ArrayList<PCMSAllDetail> getUserStatusDetail(); 
 

}
 