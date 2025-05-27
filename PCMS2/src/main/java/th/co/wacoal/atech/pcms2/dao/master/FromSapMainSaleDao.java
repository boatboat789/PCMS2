package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;

public interface FromSapMainSaleDao {

	ArrayList<PCMSSecondTableDetail> getDivisionDetail() ;

	ArrayList<PCMSAllDetail> getCustomerNameDetail();

	ArrayList<PCMSAllDetail> getCustomerShortNameDetail();

	ArrayList<PCMSTableDetail> getSaleNumberDetail();

	ArrayList<PCMSAllDetail> getCustomerNameDetail(ArrayList<ConfigCustomerUserDetail> poList);

	ArrayList<PCMSAllDetail> getCustomerShortNameDetail(ArrayList<ConfigCustomerUserDetail> poList);

	String upsertFromSapMainSaleDetail(ArrayList<FromErpMainSaleDetail> paList);
}
