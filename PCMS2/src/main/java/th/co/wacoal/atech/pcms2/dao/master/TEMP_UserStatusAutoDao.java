package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.TempUserStatusAutoDetail;
public interface TEMP_UserStatusAutoDao {

	ArrayList<TempUserStatusAutoDetail> getTempUserStatusAutoDetail(ArrayList<PCMSSecondTableDetail> poList);



}
