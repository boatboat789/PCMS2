package th.co.wacoal.atech.pcms2.dao;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

public interface PCMSDetailDao {

	ArrayList<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList);

	ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> getWaitLotCaseBySaleOrder(ArrayList<PCMSSecondTableDetail> listRP);

	ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> getNormalCaseByProdOrder(String prdOrderType, ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> getReplacedCaseByProdOrder(String prdOrderType, ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> getOrderPuangSWListByPrd(ArrayList<PCMSSecondTableDetail> poList);

	PCMSSecondTableDetail upSertRemarkCaseThree(String tableName, String planDate, PCMSSecondTableDetail bean);

	ArrayList<PCMSSecondTableDetail> getOrderPuangListByPrd(ArrayList<PCMSSecondTableDetail> poList);

	ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList);

	PCMSSecondTableDetail updateLogRemarkCaseOne(String tableName, PCMSSecondTableDetail bean, String close_STATUS);

	PCMSSecondTableDetail updateLogRemarkCaseFix(String tableName, String valueChange, PCMSSecondTableDetail bean);

	PCMSSecondTableDetail updateLogRemarkCaseThree(String tableName, PCMSSecondTableDetail bean, String close_STATUS);

	PCMSSecondTableDetail upSertRemarkCaseTwo(String tableName, String valueChange, PCMSSecondTableDetail bean);

	PCMSSecondTableDetail updateLogRemarkCaseTwo(String tableName, PCMSSecondTableDetail bean, String close_STATUS);

	PCMSSecondTableDetail upSertRemarkCaseWithGrade(String tableName, String valueChange, PCMSSecondTableDetail bean);

	PCMSSecondTableDetail upSertRemarkCaseOne(String tableName, String valueChange, PCMSSecondTableDetail bean);

	PCMSSecondTableDetail updateLogRemarkWithGrade(String tableName, PCMSSecondTableDetail bean, String Status);
 
}
