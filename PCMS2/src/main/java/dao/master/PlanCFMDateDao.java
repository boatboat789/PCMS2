package dao.master;

import java.util.ArrayList;

import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;

public interface PlanCFMDateDao {

	ArrayList<InputDateDetail> getCountCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getMaxCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList);
}
