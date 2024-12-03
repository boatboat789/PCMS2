package dao;

import java.util.Map;

import entities.CFMDetail;
import entities.ColumnHiddenDetail;
import entities.ConfigCustomerUserDetail;
import entities.DyeingDetail;
import entities.FinishingDetail;
import entities.InputDateDetail;
import entities.InspectDetail;
import entities.NCDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import entities.PODetail;
import entities.PackingDetail;
import entities.PresetDetail;
import entities.ReceipeDetail;
import entities.ReplacedProdOrderDetail;
import entities.SORDetail;
import entities.SaleDetail;
import entities.SaleInputDetail;
import entities.SendTestQCDetail;
import entities.SubmitDateDetail;
import entities.SwitchProdOrderDetail;
import entities.TempUserStatusAutoDetail;
import entities.WaitTestDetail;
import entities.WorkInLabDetail;
import entities.LBMS.ImportDetail;
import entities.PPMM.InspectOrdersDetail;
import entities.PPMM.ShopFloorControlDetail;
import entities.erp.atech.CustomerDetail;
import entities.erp.atech.FromErpCFMDetail;
import entities.erp.atech.FromErpGoodReceiveDetail;
import entities.erp.atech.FromErpMainBillBatchDetail;
import entities.erp.atech.FromErpMainProdDetail;
import entities.erp.atech.FromErpMainProdSaleDetail;
import entities.erp.atech.FromErpMainSaleDetail;
import entities.erp.atech.FromErpPODetail;
import entities.erp.atech.FromErpPackingDetail;
import entities.erp.atech.FromErpReceipeDetail;
import entities.erp.atech.FromErpSaleDetail;
import entities.erp.atech.FromErpSaleInputDetail;
import entities.erp.atech.FromErpSubmitDateDetail;

public interface BeanCreateModelDao {

	PCMSTableDetail _genPCMSTableDetail(Map<String, Object> map);

	PCMSAllDetail _genPCMSAllDetail(Map<String, Object> map);

	PODetail _genPODetail(Map<String, Object> map);

	PresetDetail _genPresetDetail(Map<String, Object> map);

	DyeingDetail _genDyeingDetail(Map<String, Object> map);

	SendTestQCDetail _genSendTestQCDetail(Map<String, Object> map);

	InspectDetail _genInspectDetail(Map<String, Object> map);

	FinishingDetail _genFinishingDetail(Map<String, Object> map);

	PackingDetail _genPackingDetail(Map<String, Object> map);

	WorkInLabDetail _genWorkInLabDetail(Map<String, Object> map);

	WaitTestDetail _genWaitTestDetail(Map<String, Object> map);

	CFMDetail _genCFMDetail(Map<String, Object> map);

	SaleDetail _genSaleDetail(Map<String, Object> map);

	SaleInputDetail _genSaleInputDetail(Map<String, Object> map);

	SubmitDateDetail _genSubmitDateDetail(Map<String, Object> map);

	NCDetail _genNCDetail(Map<String, Object> map);

	ReceipeDetail _genReceipeDetail(Map<String, Object> map);

	PCMSSecondTableDetail _genPCMSSecondTableDetail(Map<String, Object> map);

	InputDateDetail _genInputDateDetail(Map<String, Object> map);

	ColumnHiddenDetail _genColumnHiddenDetail(Map<String, Object> map);

	PCMSTableDetail _genSearchTableDetail(Map<String, Object> map);

	SORDetail _genSORDetail(Map<String, Object> map);

	SORDetail _genSORFromPCMSDetail(Map<String, Object> map);

	SwitchProdOrderDetail _genSwitchProdOrderDetail(Map<String, Object> map);

	ReplacedProdOrderDetail _genReplacedProdOrderDetail(Map<String, Object> map);

	ConfigCustomerUserDetail _genConfigCustomerUserDetail(Map<String, Object> map);

	TempUserStatusAutoDetail _genTempUserStatusAutoDetail(Map<String, Object> map);

	ShopFloorControlDetail _genShopFloorControlDetail(Map<String, Object> map);

	InspectOrdersDetail _genInspectOrdersDetail(Map<String, Object> map);

	ImportDetail _genImportDetail(Map<String, Object> map);

	CustomerDetail _genCustomerDetail(Map<String, Object> map);

	FromErpCFMDetail _genFromErpCFMDetail(Map<String, Object> map);

	FromErpGoodReceiveDetail _genFromErpGoodReceiveDetail(Map<String, Object> map);

	FromErpMainBillBatchDetail _genFromErpMainBillBatchDetail(Map<String, Object> map);

	FromErpMainProdDetail _genFromErpMainProdDetail(Map<String, Object> map);

	FromErpMainProdSaleDetail _genFromErpMainProdSaleDetail(Map<String, Object> map);

	FromErpMainSaleDetail _genFromErpMainSaleDetail(Map<String, Object> map);

	FromErpPackingDetail _genFromErpPackingDetail(Map<String, Object> map);

	FromErpPODetail _genFromErpPODetail(Map<String, Object> map);

	FromErpReceipeDetail _genFromErpReceipeDetail(Map<String, Object> map);

	FromErpSaleDetail _genFromErpSaleDetail(Map<String, Object> map);

	FromErpSaleInputDetail _genFromErpSaleInputDetail(Map<String, Object> map);

	FromErpSubmitDateDetail _genFromErpSubmitDateDetail(Map<String, Object> map);

}
