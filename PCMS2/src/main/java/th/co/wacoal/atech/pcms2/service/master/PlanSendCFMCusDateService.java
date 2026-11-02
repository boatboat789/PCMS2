package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.PlanSendCFMCusDateDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;

@Service
public class PlanSendCFMCusDateService {
	private PlanSendCFMCusDateDao dao;

	@Autowired
	public PlanSendCFMCusDateService(PlanSendCFMCusDateDao dao) {
		this.dao = dao;
	}

	public ArrayList<InputDateDetail> getSendCFMCusDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getSendCFMCusDateDetail(poList);
		return list;
	}

}
