package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.PlanCFMLabDateDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;

@Service
public class PlanCFMLabDateService { 
	private PlanCFMLabDateDao dao; 

	@Autowired
	public PlanCFMLabDateService(PlanCFMLabDateDao dao) {
		this.dao = dao; 
	}
 

	public ArrayList<InputDateDetail> getCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getCFMPlanLabDateDetail(poList);
		return list;
	}

	public ArrayList<InputDateDetail> getMaxCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getMaxCFMPlanLabDateDetail(poList);
		return list;
	}

	public ArrayList<InputDateDetail> getCountCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getCountCFMPlanLabDateDetail(poList);
		return list;
	}

}
