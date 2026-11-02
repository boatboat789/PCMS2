package th.co.wacoal.atech.pcms2.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.PCMSDetailDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

@Service
public class PCMSDetailService { 
	private PCMSDetailDao dao;  

	@Autowired
	public PCMSDetailService(PCMSDetailDao dao) { 
			this.dao = dao;  
	}  
	public ArrayList<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.searchByDetail(poList);
		return list;
	}

	public ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.saveInputDate(poList);
		return list;
	}

	public ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getDeliveryPlanDateDetail(poList);
		return list;
	} 

	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getSwitchProdOrderListByPrd(poList);
		return list;
	}
 
	public ArrayList<PCMSSecondTableDetail> getNormalCaseByProdOrder(String c_PRODORDER,
			ArrayList<PCMSSecondTableDetail> poListOldNormal)
	{
		// TODO Auto-generated method stub
		return this.dao.getNormalCaseByProdOrder(c_PRODORDER, poListOldNormal);
	}
	public ArrayList<PCMSSecondTableDetail> getWaitLotCaseBySaleOrder(ArrayList<PCMSSecondTableDetail> listRP)
	{
		// TODO Auto-generated method stub
		return this.dao.getWaitLotCaseBySaleOrder(listRP);
	}
	public ArrayList<PCMSSecondTableDetail> getReplacedCaseByProdOrder(String c_PRODORDERRP,
			ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return this.dao.getReplacedCaseByProdOrder(c_PRODORDERRP, poList);
	}
	public PCMSSecondTableDetail updateLogRemarkCaseFix(String tableName, String string, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return this.dao.updateLogRemarkCaseFix(tableName, string, bean);
	}
	public ArrayList<PCMSSecondTableDetail> getOrderPuangListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return this.dao.getOrderPuangListByPrd(poList);
	}
	public ArrayList<PCMSSecondTableDetail> getOrderPuangSWListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		// TODO Auto-generated method stub
		return this.dao.getOrderPuangSWListByPrd(poList);
	}
	public PCMSSecondTableDetail upSertRemarkCaseThree(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return this.dao.upSertRemarkCaseThree(tableName, valueChange, bean);
	}
	public PCMSSecondTableDetail updateLogRemarkCaseThree(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{
		// TODO Auto-generated method stub
		return this.dao.updateLogRemarkCaseThree(tableName, bean, close_STATUS);
	}
	public PCMSSecondTableDetail upSertRemarkCaseTwo(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return this.dao.upSertRemarkCaseTwo(tableName, valueChange, bean);
	}
	public PCMSSecondTableDetail updateLogRemarkCaseTwo(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{
		// TODO Auto-generated method stub
		return this.dao.updateLogRemarkCaseTwo(tableName, bean, close_STATUS);
	}
	public PCMSSecondTableDetail upSertRemarkCaseOne(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return this.dao.upSertRemarkCaseOne(tableName, valueChange, bean);
	}
	public PCMSSecondTableDetail updateLogRemarkCaseOne(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{
		// TODO Auto-generated method stub
		return this.dao.updateLogRemarkCaseOne(tableName, bean, close_STATUS);
	}
	public PCMSSecondTableDetail upSertRemarkCaseWithGrade(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{
		// TODO Auto-generated method stub
		return this.dao.upSertRemarkCaseWithGrade(tableName, valueChange, bean);
	}
	public PCMSSecondTableDetail updateLogRemarkWithGrade(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{
		// TODO Auto-generated method stub
		return this.dao.updateLogRemarkWithGrade(tableName, bean, close_STATUS);
	}

}
