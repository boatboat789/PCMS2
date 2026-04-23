package th.co.wacoal.atech.pcms2.service.master.erp.atech;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.erp.atech.ERPAtechDao;
import th.co.wacoal.atech.pcms2.entities.ProductionOrderLogDetail;
import th.co.wacoal.atech.pcms2.entities.SaleOrderLogDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainBillBatchDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPODetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;

@Service
public class ERPAtechService {
	private ERPAtechDao dao;

	@Autowired
	public ERPAtechService(ERPAtechDao dao) {
		this.dao = dao;
	}

	public ArrayList<CustomerDetail> getCustomerDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<CustomerDetail> list = this.dao.getCustomerDetail();
		return list;
	}

	public ArrayList<FromErpCFMDetail> getFromErpCFMDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpCFMDetail> list = this.dao.getFromErpCFMDetail();
		return list;
	}

	public ArrayList<FromErpGoodReceiveDetail> getFromErpGoodReceiveDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpGoodReceiveDetail> list = this.dao.getFromErpGoodReceiveDetail();

		return list;
	}

	public ArrayList<FromErpMainBillBatchDetail> getFromErpMainBillBatchDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpMainBillBatchDetail> list = this.dao.getFromErpMainBillBatchDetail();
		return list;
	}

	public ArrayList<FromErpMainProdDetail> getFromErpMainProdDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpMainProdDetail> list = this.dao.getFromErpMainProdDetail();
		return list;
	}

	public ArrayList<FromErpMainProdSaleDetail> getFromErpMainProdSaleDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpMainProdSaleDetail> list = this.dao.getFromErpMainProdSaleDetail();
		return list;
	}

	public ArrayList<FromErpMainSaleDetail> getFromErpMainSaleDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpMainSaleDetail> list = this.dao.getFromErpMainSaleDetail();
		return list;
	}

	public ArrayList<FromErpPackingDetail> getFromErpPackingDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpPackingDetail> list = this.dao.getFromErpPackingDetail();
		return list;
	}

	public ArrayList<FromErpPODetail> getFromErpPODetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpPODetail> list = this.dao.getFromErpPODetail();
		return list;
	}

	public ArrayList<FromErpSaleDetail> getFromErpSaleDetail()

	{
		// TODO Auto-generated method stub
		ArrayList<FromErpSaleDetail> list = this.dao.getFromErpSaleDetail();
		return list;
	}

//	public ArrayList<FromErpSaleInputDetail> getFromErpSaleInputDetail() 
//	{
//		// TODO Auto-generated method stub
//		ArrayList<FromErpSaleInputDetail> list = this.dao.getFromErpSaleInputDetail();
//		return list;
//	}

	public ArrayList<FromErpSubmitDateDetail> getFromErpSubmitDateDetail()

	{
		// TODO Auto-generated method stub
		ArrayList<FromErpSubmitDateDetail> list = this.dao.getFromErpSubmitDateDetail();
		return list;
	}

	public ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2Detail()

	{
		// TODO Auto-generated method stub
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = this.dao.getZ_ATT_CustomerConfirm2Detail();
		return list;
	}

	public ArrayList<ProductionOrderLogDetail> getFromErpMainProdDetailWithRangeOfChangeDate(String changeDateStart,
			String changeDateEnd, String productionOrder)
	{
		ArrayList<ProductionOrderLogDetail> list =
				this.dao.getFromErpMainProdDetailWithRangeOfChangeDate(changeDateStart, changeDateEnd, productionOrder);
		return list;
	}

	public ArrayList<SaleOrderLogDetail> getFromErpMainSaleDetailWithRangeOfChangeDate(String changeDateStart,
			String changeDateEnd, String saleOrder)
	{
		ArrayList<SaleOrderLogDetail> list =
				this.dao.getFromErpMainSaleDetailWithRangeOfChangeDate(changeDateStart, changeDateEnd, saleOrder);
		return list;
	}
}
