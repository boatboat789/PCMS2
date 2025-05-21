package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;

public interface PlanDeliveryDateDao {

	ArrayList<InputDateDetail> getCountDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getMaxDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList);

}
