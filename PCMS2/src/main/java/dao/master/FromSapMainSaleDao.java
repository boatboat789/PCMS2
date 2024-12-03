package dao.master;

import java.util.ArrayList;

import entities.ConfigCustomerUserDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import entities.erp.atech.FromErpMainSaleDetail;

public interface FromSapMainSaleDao {

	ArrayList<PCMSSecondTableDetail> getDivisionDetail() ;

	ArrayList<PCMSAllDetail> getCustomerNameDetail();

	ArrayList<PCMSAllDetail> getCustomerShortNameDetail();

	ArrayList<PCMSTableDetail> getSaleNumberDetail();

	ArrayList<PCMSAllDetail> getCustomerNameDetail(ArrayList<ConfigCustomerUserDetail> poList);

	ArrayList<PCMSAllDetail> getCustomerShortNameDetail(ArrayList<ConfigCustomerUserDetail> poList);

	String upsertFromSapMainSaleDetail(ArrayList<FromErpMainSaleDetail> paList);
}
