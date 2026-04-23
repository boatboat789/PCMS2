package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.FromSapPresetDao;
import th.co.wacoal.atech.pcms2.entities.PresetDetail;

@Service
public class FromSapPresetService {
	private FromSapPresetDao dao;

	@Autowired
	public FromSapPresetService(FromSapPresetDao dao) {
		this.dao = dao;
	}

	public ArrayList<PresetDetail> getFromSapPresetDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<PresetDetail> list = this.dao.getFromSapPresetDetailByProductionOrder(prodOrder);
		return list;
	}
}
