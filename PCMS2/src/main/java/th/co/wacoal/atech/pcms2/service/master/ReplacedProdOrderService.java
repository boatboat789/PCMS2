package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.ReplacedProdOrderDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.ReplacedProdOrderDetail;

@Service
public class ReplacedProdOrderService { 
	private ReplacedProdOrderDao dao; 

	@Autowired
	public ReplacedProdOrderService(ReplacedProdOrderDao dao) {
		this.dao = dao; 
	} 
	public ReplacedProdOrderDetail upsertReplacedProdOrder(ReplacedProdOrderDetail bean, String dataStatus)
	{
		// TODO Auto-generated method stub
		ReplacedProdOrderDetail list = this.dao.upsertReplacedProdOrder(bean, dataStatus);
		return list;
	}

	public PCMSSecondTableDetail updateReplacedProdOrder(PCMSSecondTableDetail bean, String dataStatus)
	{
		// TODO Auto-generated method stub
		PCMSSecondTableDetail list = this.dao.updateReplacedProdOrder(bean, dataStatus);
		return list;
	}

	public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdRP(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<ReplacedProdOrderDetail> list = this.dao.getReplacedProdOrderDetailByPrdRP(prodOrder);
		return list;
	}

	public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrd(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<ReplacedProdOrderDetail> list = this.dao.getReplacedProdOrderDetailByPrd(prodOrder);
		return list;
	}
	public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMainAndSO(String prdOrder, String saleOrder,
			String saleLine)
	{
		// TODO Auto-generated method stub
		ArrayList<ReplacedProdOrderDetail> list =
				this.dao.getReplacedProdOrderDetailByPrdMainAndSO(prdOrder, saleOrder, saleLine);
		return list;
	}
}
