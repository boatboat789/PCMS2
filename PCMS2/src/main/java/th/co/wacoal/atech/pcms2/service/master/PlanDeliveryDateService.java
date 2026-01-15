package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.PlanDeliveryDateDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;

@Service
public class PlanDeliveryDateService   { 
	private PlanDeliveryDateDao dao; 

	@Autowired
	public PlanDeliveryDateService(PlanDeliveryDateDao dao) {
		this.dao = dao; 

	} 

	public ArrayList<InputDateDetail> getCountDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getCountDeliveryPlanDateDetail(poList);
		return list;
	}

	public ArrayList<InputDateDetail> getMaxDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getMaxDeliveryPlanDateDetail(poList);
		return list;
	}
}
