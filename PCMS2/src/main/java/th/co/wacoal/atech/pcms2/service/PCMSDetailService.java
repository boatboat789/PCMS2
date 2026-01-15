package th.co.wacoal.atech.pcms2.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.PCMSDetailDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

@Service
public class PCMSDetailService { 
	private PCMSDetailDao dao; 

	@Autowired
	public PCMSDetailService(PCMSDetailDao dao) { 
			this.dao = dao; 
	} 
	public ArrayList<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.searchByDetail(poList);
		return list;
	}

	public ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.saveInputDate(poList);
		return list;
	}

	public ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getDeliveryPlanDateDetail(poList);
		return list;
	}

	public ArrayList<PCMSAllDetail> getUserStatusList()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getUserStatusList();
		return list;
	}

	public ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.saveDefault(poList);
		return list;
	}

	public ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.loadDefault(poList);
		return list;
	}

	public ArrayList<PCMSSecondTableDetail> saveInputDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.saveInputDetail(poList);
		return list;
	}

	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getSwitchProdOrderListByPrd(poList);
		return list;
	}

	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByRowProd(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getSwitchProdOrderListByRowProd(poList);
		return list;
	}

}
