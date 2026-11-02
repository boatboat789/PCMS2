package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.TEMP_UserStatusAutoDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.TempUserStatusAutoDetail;

@Service
public class TEMP_UserStatusAutoService {
	private TEMP_UserStatusAutoDao dao;

	@Autowired
	public TEMP_UserStatusAutoService(TEMP_UserStatusAutoDao dao) {
		this.dao = dao;
	}

	public ArrayList<TempUserStatusAutoDetail> getTempUserStatusAutoDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<TempUserStatusAutoDetail> list = this.dao.getTempUserStatusAutoDetail(poList);
		return list;
	}

}
