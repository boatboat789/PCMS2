package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.SwitchProdOrderDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.SwitchProdOrderDetail;

@Component
public class SwitchProdOrderService  { 
	private SwitchProdOrderDao dao; 

	@Autowired
	public SwitchProdOrderService(SwitchProdOrderDao dao) {
		this.dao = dao; 
	}
 

	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrder(String prdOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getSwitchProdOrderDetailByProdOrder(prdOrder);
		return list;
	}

	public PCMSSecondTableDetail updateSwitchProdOrderDetail(PCMSSecondTableDetail bean, String dataStatus)
	{
		// TODO Auto-generated method stub
		PCMSSecondTableDetail list = this.dao.updateSwitchProdOrderDetail(bean, dataStatus);
		return list;
	}

	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrderForHandlerSwitchProd(String prdOrderSW)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getSwitchProdOrderDetailByProdOrderForHandlerSwitchProd(prdOrderSW);
		return list;
	}

	public ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrdSW(String prodOrderSW)
	{
		// TODO Auto-generated method stub
		ArrayList<SwitchProdOrderDetail> list = this.dao.getSwitchProdOrderDetailByPrdSW(prodOrderSW);
		return list;
	}

	public ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrd(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<SwitchProdOrderDetail> list = this.dao.getSwitchProdOrderDetailByPrd(prodOrder);
		return list;
	}

	public ArrayList<SwitchProdOrderDetail> getSWProdOrderDetailByPrd(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<SwitchProdOrderDetail> list = this.dao.getSWProdOrderDetailByPrd(prodOrder);
		return list;
	}

	public PCMSSecondTableDetail upsertSwitchProdOrder(PCMSSecondTableDetail bean, String dataStatus)
	{
		// TODO Auto-generated method stub
		PCMSSecondTableDetail list = this.dao.upsertSwitchProdOrder(bean, dataStatus);
		return list;
	}
}
