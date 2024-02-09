package dao.master;

import java.util.ArrayList;

import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;
import entities.SORDetail;

public interface PlanCFMLabDateDao {

	ArrayList<InputDateDetail> getCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getMaxCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<InputDateDetail> getCountCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList); 
}
