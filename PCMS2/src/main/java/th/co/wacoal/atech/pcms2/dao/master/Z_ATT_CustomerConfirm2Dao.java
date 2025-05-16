package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;
 
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;

public interface Z_ATT_CustomerConfirm2Dao { 
	String upsertZ_ATT_CustomerConfirm2Detail(ArrayList<Z_ATT_CustomerConfirm2Detail> paList);

	ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2Detail(String prodOrder, String lotNubmer, String replyDate,
			String custName, String so, String sendDate);
 
	ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2DetailById(ArrayList<Z_ATT_CustomerConfirm2Detail> poList);

}
