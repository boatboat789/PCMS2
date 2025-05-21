package th.co.wacoal.atech.pcms2.dao.master.erp.atech;

import java.util.ArrayList;

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

public interface ERPAtechDao {

	ArrayList<CustomerDetail> getCustomerDetail();

	ArrayList<FromErpCFMDetail> getFromErpCFMDetail();

	ArrayList<FromErpGoodReceiveDetail> getFromErpGoodReceiveDetail();

	ArrayList<FromErpMainBillBatchDetail> getFromErpMainBillBatchDetail();

	ArrayList<FromErpMainProdDetail> getFromErpMainProdDetail();

	ArrayList<FromErpMainProdSaleDetail> getFromErpMainProdSaleDetail();

	ArrayList<FromErpMainSaleDetail> getFromErpMainSaleDetail();

	ArrayList<FromErpPackingDetail> getFromErpPackingDetail();
 

	ArrayList<FromErpSaleDetail> getFromErpSaleDetail();

//	ArrayList<FromErpSaleInputDetail> getFromErpSaleInputDetail();

	ArrayList<FromErpSubmitDateDetail> getFromErpSubmitDateDetail();

//	ArrayList<FromErpReceipeDetail> getFromErpReceipeDetail();

	ArrayList<FromErpPODetail> getFromErpPODetail();

	ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2Detail();
 

}
