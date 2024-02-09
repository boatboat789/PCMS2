package dao.master;

import java.util.ArrayList;

import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;
import entities.TempUserStatusAutoDetail; 
public interface TEMP_UserStatusAutoDao {
   
	ArrayList<TempUserStatusAutoDetail> getTempUserStatusAutoDetail(ArrayList<PCMSSecondTableDetail> poList);
 
 

}
