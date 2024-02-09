package dao.master;

import java.util.ArrayList;

import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import entities.SORDetail;

public interface FromSapMainSaleDao {

	ArrayList<PCMSSecondTableDetail> getDivisionDetail() ;

	ArrayList<PCMSAllDetail> getCustomerNameDetail();

	ArrayList<PCMSAllDetail> getCustomerShortNameDetail();

	ArrayList<PCMSTableDetail> getSaleNumberDetail();
}
