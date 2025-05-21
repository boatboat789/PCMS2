package th.co.wacoal.atech.pcms2.model;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.BeanCreateModelDao;
import th.co.wacoal.atech.pcms2.dao.implement.BeanCreateModelDaoImpl;
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
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;

@Component
public class BeanCreateModel {
	private BeanCreateModelDao dao;

	@Autowired
	public BeanCreateModel() {
		this.dao = new BeanCreateModelDaoImpl();
	}

	public SORDetail _genSORDetail(Map<String, Object> map)
	{
		SORDetail bean = this.dao._genSORDetail(map);
		return bean;
	}

	public PCMSTableDetail _genPCMSTableDetail(Map<String, Object> map)
	{
		PCMSTableDetail bean = this.dao._genPCMSTableDetail(map);
		return bean;
	}

	public PCMSAllDetail _genPCMSAllDetail(Map<String, Object> map)
	{
		PCMSAllDetail bean = this.dao._genPCMSAllDetail(map);
		return bean;
	}

	public PresetDetail _genPresetDetail(Map<String, Object> map)
	{
		PresetDetail bean = this.dao._genPresetDetail(map);
		return bean;
	}

	public DyeingDetail _genDyeingDetail(Map<String, Object> map)
	{
		DyeingDetail bean = this.dao._genDyeingDetail(map);
		return bean;
	}

	public PODetail _genPODetail(Map<String, Object> map)
	{
		PODetail bean = this.dao._genPODetail(map);
		return bean;
	}

	public SendTestQCDetail _genSendTestQCDetail(Map<String, Object> map)
	{
		SendTestQCDetail bean = this.dao._genSendTestQCDetail(map);
		return bean;
	}

	public InspectDetail _genInspectDetail(Map<String, Object> map)
	{
		InspectDetail bean = this.dao._genInspectDetail(map);
		return bean;
	}

	public FinishingDetail _genFinishingDetail(Map<String, Object> map)
	{
		FinishingDetail bean = this.dao._genFinishingDetail(map);
		return bean;
	}

	public PackingDetail _genPackingDetail(Map<String, Object> map)
	{
		PackingDetail bean = this.dao._genPackingDetail(map);
		return bean;
	}

	public WorkInLabDetail _genWorkInLabDetail(Map<String, Object> map)
	{
		WorkInLabDetail bean = this.dao._genWorkInLabDetail(map);
		return bean;
	}

	public WaitTestDetail _genWaitTestDetail(Map<String, Object> map)
	{
		WaitTestDetail bean = this.dao._genWaitTestDetail(map);
		return bean;
	}

	public CFMDetail _genCFMDetail(Map<String, Object> map)
	{
		CFMDetail bean = this.dao._genCFMDetail(map);
		return bean;
	}

	public SaleDetail _genSaleDetail(Map<String, Object> map)
	{
		SaleDetail bean = this.dao._genSaleDetail(map);
		return bean;
	}

	public SaleInputDetail _genSaleInputDetail(Map<String, Object> map)
	{
		SaleInputDetail bean = this.dao._genSaleInputDetail(map);
		return bean;
	}

	public SubmitDateDetail _genSubmitDateDetail(Map<String, Object> map)
	{
		SubmitDateDetail bean = this.dao._genSubmitDateDetail(map);
		return bean;
	}

	public NCDetail _genNCDetail(Map<String, Object> map)
	{
		NCDetail bean = this.dao._genNCDetail(map);
		return bean;
	}

	public ReceipeDetail _genReceipeDetail(Map<String, Object> map)
	{
		ReceipeDetail bean = this.dao._genReceipeDetail(map);
		return bean;
	}

	public PCMSSecondTableDetail _genPCMSSecondTableDetail(Map<String, Object> map)
	{
		PCMSSecondTableDetail bean = this.dao._genPCMSSecondTableDetail(map);
		return bean;
	}

	public InputDateDetail _genInputDateDetail(Map<String, Object> map)
	{
		InputDateDetail bean = this.dao._genInputDateDetail(map);
		return bean;
	}

	public ColumnHiddenDetail _genColumnHiddenDetail(Map<String, Object> map)
	{
		ColumnHiddenDetail bean = this.dao._genColumnHiddenDetail(map);
		return bean;
	}

	public PCMSTableDetail _genSearchTableDetail(Map<String, Object> map)
	{
		PCMSTableDetail bean = this.dao._genSearchTableDetail(map);
		return bean;
	}

	public ReplacedProdOrderDetail _genReplacedProdOrderDetail(Map<String, Object> map)
	{
		ReplacedProdOrderDetail bean = this.dao._genReplacedProdOrderDetail(map);
		return bean;
	}

	public SwitchProdOrderDetail _genSwitchProdOrderDetail(Map<String, Object> map)
	{
		SwitchProdOrderDetail bean = this.dao._genSwitchProdOrderDetail(map);
		return bean;
	}

	public ConfigCustomerUserDetail _genConfigCustomerUserDetail(Map<String, Object> map)
	{
		ConfigCustomerUserDetail bean = this.dao._genConfigCustomerUserDetail(map);
		return bean;
	}

	public TempUserStatusAutoDetail _genTempUserStatusAutoDetail(Map<String, Object> map)
	{
		TempUserStatusAutoDetail bean = this.dao._genTempUserStatusAutoDetail(map);
		return bean;
	}

	public ShopFloorControlDetail _genShopFloorControlDetail(Map<String, Object> map)
	{
		ShopFloorControlDetail bean = this.dao._genShopFloorControlDetail(map);
		return bean;
	}

	public InspectOrdersDetail _genInspectOrdersDetail(Map<String, Object> map)
	{
		InspectOrdersDetail bean = this.dao._genInspectOrdersDetail(map);
		return bean;
	}

	public ImportDetail _genImportDetail(Map<String, Object> map)
	{
		ImportDetail bean = this.dao._genImportDetail(map);
		return bean;
	}

	public CustomerDetail _genCustomerDetail(Map<String, Object> map)
	{
		CustomerDetail bean = this.dao._genCustomerDetail(map);
		return bean;
	}

	public FromErpCFMDetail _genFromErpCFMDetail(Map<String, Object> map)
	{
		FromErpCFMDetail bean = this.dao._genFromErpCFMDetail(map);
		return bean;
	}

	public FromErpGoodReceiveDetail _genFromErpGoodReceiveDetail(Map<String, Object> map)
	{
		FromErpGoodReceiveDetail bean = this.dao._genFromErpGoodReceiveDetail(map);
		return bean;
	}

	public FromErpMainBillBatchDetail _genFromErpMainBillBatchDetail(Map<String, Object> map)
	{
		FromErpMainBillBatchDetail bean = this.dao._genFromErpMainBillBatchDetail(map);
		return bean;
	}

	public FromErpMainProdDetail _genFromErpMainProdDetail(Map<String, Object> map)
	{
		FromErpMainProdDetail bean = this.dao._genFromErpMainProdDetail(map);
		return bean;
	}

	public FromErpMainProdSaleDetail _genFromErpMainProdSaleDetail(Map<String, Object> map)
	{
		FromErpMainProdSaleDetail bean = this.dao._genFromErpMainProdSaleDetail(map);
		return bean;
	}

	public FromErpMainSaleDetail _genFromErpMainSaleDetail(Map<String, Object> map)
	{
		FromErpMainSaleDetail bean = this.dao._genFromErpMainSaleDetail(map);
		return bean;
	}

	public FromErpPackingDetail _genFromErpPackingDetail(Map<String, Object> map)
	{
		FromErpPackingDetail bean = this.dao._genFromErpPackingDetail(map);
		return bean;
	}

	public FromErpPODetail _genFromErpPODetail(Map<String, Object> map)
	{
		FromErpPODetail bean = this.dao._genFromErpPODetail(map);
		return bean;
	}

	public FromErpReceipeDetail _genFromErpReceipeDetail(Map<String, Object> map)
	{
		FromErpReceipeDetail bean = this.dao._genFromErpReceipeDetail(map);
		return bean;
	}

	public FromErpSaleDetail _genFromErpSaleDetail(Map<String, Object> map)
	{
		FromErpSaleDetail bean = this.dao._genFromErpSaleDetail(map);
		return bean;
	}

	public FromErpSaleInputDetail _genFromErpSaleInputDetail(Map<String, Object> map)
	{
		FromErpSaleInputDetail bean = this.dao._genFromErpSaleInputDetail(map);
		return bean;
	}

	public FromErpSubmitDateDetail _genFromErpSubmitDateDetail(Map<String, Object> map)
	{
		FromErpSubmitDateDetail bean = this.dao._genFromErpSubmitDateDetail(map);
		return bean;
	}

	public EmployeeDetail _genEmployeeDetail(Map<String, Object> map)
	{
		EmployeeDetail bean = this.dao._genEmployeeDetail(map);
		return bean;
	}

	public PermitDetail _genPermitDetail(Map<String, Object> map)
	{
		PermitDetail bean = this.dao._genPermitDetail(map);
		return bean;
	}

	public UserDetail _genUsersDetail(Map<String, Object> map)
	{
		UserDetail bean = this.dao._genUsersDetail(map);
		return bean;
	}

	public Z_ATT_CustomerConfirm2Detail _genZ_ATT_CustomerConfirm2Detail(Map<String, Object> map)
	{
		Z_ATT_CustomerConfirm2Detail bean = this.dao._genZ_ATT_CustomerConfirm2Detail(map);
		return bean;
	} 
}
