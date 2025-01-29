package th.co.wacoal.atech.pcms2.dao;

import java.util.Map;

import th.co.wacoal.atech.pcms2.entities.CFMDetail;
import th.co.wacoal.atech.pcms2.entities.ColumnHiddenDetail;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.DyeingDetail;
import th.co.wacoal.atech.pcms2.entities.EmployeeDetail;
import th.co.wacoal.atech.pcms2.entities.FinishingDetail;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.InspectDetail;
import th.co.wacoal.atech.pcms2.entities.NCDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.PODetail;
import th.co.wacoal.atech.pcms2.entities.PackingDetail;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.PresetDetail;
import th.co.wacoal.atech.pcms2.entities.ReceipeDetail;
import th.co.wacoal.atech.pcms2.entities.ReplacedProdOrderDetail;
import th.co.wacoal.atech.pcms2.entities.SORDetail;
import th.co.wacoal.atech.pcms2.entities.SaleDetail;
import th.co.wacoal.atech.pcms2.entities.SaleInputDetail;
import th.co.wacoal.atech.pcms2.entities.SendTestQCDetail;
import th.co.wacoal.atech.pcms2.entities.SubmitDateDetail;
import th.co.wacoal.atech.pcms2.entities.SwitchProdOrderDetail;
import th.co.wacoal.atech.pcms2.entities.TempUserStatusAutoDetail;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.co.wacoal.atech.pcms2.entities.WaitTestDetail;
import th.co.wacoal.atech.pcms2.entities.WorkInLabDetail;
import th.co.wacoal.atech.pcms2.entities.LBMS.ImportDetail;
import th.co.wacoal.atech.pcms2.entities.PPMM.InspectOrdersDetail;
import th.co.wacoal.atech.pcms2.entities.PPMM.ShopFloorControlDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainBillBatchDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPODetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpReceipeDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleInputDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;

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

	EmployeeDetail _genEmployeeDetail(Map<String, Object> map);

	PermitDetail _genPermitDetail(Map<String, Object> map);

	UserDetail _genUsersDetail(Map<String, Object> map);

}
