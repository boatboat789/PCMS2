package dao.master;

import java.util.ArrayList;

import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;
import entities.SORDetail;

public interface PlanSendCFMCusDateDao {

	ArrayList<InputDateDetail> getSendCFMCusDateDetail(ArrayList<PCMSSecondTableDetail> poList);

 

}
