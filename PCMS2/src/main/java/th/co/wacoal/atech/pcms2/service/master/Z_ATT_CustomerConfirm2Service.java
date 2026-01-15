package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.Z_ATT_CustomerConfirm2Dao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;

@Service
public class Z_ATT_CustomerConfirm2Service  { 
	private Z_ATT_CustomerConfirm2Dao dao; 

	@Autowired
	public Z_ATT_CustomerConfirm2Service(Z_ATT_CustomerConfirm2Dao dao) {
		this.dao = dao; 
	} 

	public ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2DetailByProductionOrder(String prodOrder,
			String lotNubmer, String replyDate, String custName, String so, String sendDate)
	{
		// TODO Auto-generated method stub
		ArrayList<Z_ATT_CustomerConfirm2Detail> list =
				this.dao.getZ_ATT_CustomerConfirm2Detail(prodOrder, lotNubmer, replyDate, custName, so, sendDate);
		return list;
	}

	public ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2DetailById(
			ArrayList<Z_ATT_CustomerConfirm2Detail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = this.dao.getZ_ATT_CustomerConfirm2DetailById(poList);
		return list;
	}

	public String upsertZ_ATT_CustomerConfirm2Detail(ArrayList<Z_ATT_CustomerConfirm2Detail> zCustList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertZ_ATT_CustomerConfirm2Detail(zCustList);
		return iconStatus;
	}
}
