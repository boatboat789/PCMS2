package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainSaleDao;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.SaleOrderLogDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;

@Service
public class FromSapMainSaleService  { 
	private FromSapMainSaleDao dao; 

    @Autowired
	public FromSapMainSaleService(FromSapMainSaleDao dao) {
		this.dao = dao; 
	} 

	public ArrayList<PCMSSecondTableDetail> getDivisionDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getDivisionDetail();
		return list;
	}

	public ArrayList<PCMSAllDetail> getCustomerNameDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerNameDetail();
		return list;
	}

	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerShortNameDetail();
		return list;
	}

	public ArrayList<PCMSTableDetail> getSaleNumberDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.getSaleNumberDetail();
		return list;
	}

	public ArrayList<PCMSAllDetail> getCustomerNameDetail(ArrayList<ConfigCustomerUserDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerNameDetail(poList);
		return list;
	}

	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail(ArrayList<ConfigCustomerUserDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerShortNameDetail(poList);
		return list;
	}

	public  String upsertFromSapMainSaleDetail( ArrayList<FromErpMainSaleDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapMainSaleDetail(paList );
		return iconStatus;
	}

	public ArrayList<SaleOrderLogDetail> getFromSapMainSaleDetailWithRangeOfChangeDate(String changeDateStart,
			String changeDateEnd, String saleOrder)
	{
		ArrayList<SaleOrderLogDetail> list =
				this.dao.getFromSapMainSaleDetailWithRangeOfChangeDate(changeDateStart, changeDateEnd, saleOrder);
		return list;
	}
}
