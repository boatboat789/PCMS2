package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;

public interface FromSapSubmitDateDao {

	String upsertFromSapSubmitDateDetail(ArrayList<FromErpSubmitDateDetail> paList);

	ArrayList<InputDateDetail> getSubmitDateDetail(ArrayList<PCMSTableDetail> poList);

}
