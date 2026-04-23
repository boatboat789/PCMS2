package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainProdDao;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.ProductionOrderLogDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;

@Service
public class FromSapMainProdService {
	private FromSapMainProdDao dao;

	@Autowired
	public FromSapMainProdService(FromSapMainProdDao dao) {
		this.dao = dao;
	}

	public ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrderRP)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getFromSapMainProdDetail(prdOrderRP);
		return list;
	}

	public ArrayList<PCMSAllDetail> getUserStatusDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getUserStatusDetail();
		return list;
	}

	public String upsertFromSapMainProdDetail(ArrayList<FromErpMainProdDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertFromSapMainProdDetail(paList);
		return iconStatus;
	}

	public ArrayList<ProductionOrderLogDetail> getFromSapMainProdDetailWithRangeOfChangeDate(String startLogDate,
			String endLogDate, String productionOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<ProductionOrderLogDetail> list =
				this.dao.getFromSapMainProdDetailWithRangeOfChangeDate(startLogDate, endLogDate, productionOrder);
		return list;
	}

}
