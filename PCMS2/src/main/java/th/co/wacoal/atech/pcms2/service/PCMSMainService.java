package th.co.wacoal.atech.pcms2.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.PCMSMainDao;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

@Service
public class PCMSMainService {  
	private PCMSMainDao dao; 

	@Autowired
	public PCMSMainService(PCMSMainDao dao) { 
			this.dao = dao; 
	} 

	public ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList, boolean isCustomer)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.searchByDetail(poList, isCustomer);
		return list;
	}

	public ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getPrdDetailByRow(poList);
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

}
