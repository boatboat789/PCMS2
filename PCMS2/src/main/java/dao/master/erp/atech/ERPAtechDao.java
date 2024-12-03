package dao.master.erp.atech;

import java.util.ArrayList;

import entities.erp.atech.CustomerDetail;
import entities.erp.atech.FromErpCFMDetail;
import entities.erp.atech.FromErpGoodReceiveDetail;
import entities.erp.atech.FromErpMainBillBatchDetail;
import entities.erp.atech.FromErpMainProdDetail;
import entities.erp.atech.FromErpMainProdSaleDetail;
import entities.erp.atech.FromErpMainSaleDetail;
import entities.erp.atech.FromErpPODetail;
import entities.erp.atech.FromErpPackingDetail;
import entities.erp.atech.FromErpSaleDetail;
import entities.erp.atech.FromErpSaleInputDetail;
import entities.erp.atech.FromErpSubmitDateDetail;

public interface ERPAtechDao {

	ArrayList<CustomerDetail> getCustomerDetail();

	ArrayList<FromErpCFMDetail> getFromErpCFMDetail();

	ArrayList<FromErpGoodReceiveDetail> getFromErpGoodReceiveDetail();

	ArrayList<FromErpMainBillBatchDetail> getFromErpMainBillBatchDetail();

	ArrayList<FromErpMainProdDetail> getFromErpMainProdDetail();

	ArrayList<FromErpMainProdSaleDetail> getFromErpMainProdSaleDetail();

	ArrayList<FromErpMainSaleDetail> getFromErpMainSaleDetail();

	ArrayList<FromErpPackingDetail> getFromErpPackingDetail();

	ArrayList<FromErpPODetail> getFromErpPODetail(); 

	ArrayList<FromErpSaleDetail> getFromErpSaleDetail();

	ArrayList<FromErpSaleInputDetail> getFromErpSaleInputDetail();

	ArrayList<FromErpSubmitDateDetail> getFromErpSubmitDateDetail();
 

}
