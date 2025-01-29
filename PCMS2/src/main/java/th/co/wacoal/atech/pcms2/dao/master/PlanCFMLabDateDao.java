package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;

public interface PlanCFMLabDateDao {

	ArrayList<InputDateDetail> getCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getMaxCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getCountCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList);
}
