package model;

import java.util.Map;

import dao.BeanCreateModelDao;
import dao.implement.BeanCreateModelDaoImpl;
import entities.CFMDetail;
import entities.ColumnHiddenDetail;
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
import entities.WaitTestDetail;
import entities.WorkInLabDetail;

public class BeanCreateModel {
	private BeanCreateModelDao dao;

	public BeanCreateModel() {
		this.dao = new BeanCreateModelDaoImpl();
	}

	public SORDetail _genSORDetail(Map<String, Object> map) {
		SORDetail bean = this.dao._genSORDetail(map);
		return bean;
	}
	public PCMSTableDetail _genPCMSTableDetail(Map<String, Object> map) {
		PCMSTableDetail bean = this.dao._genPCMSTableDetail(map);
		return bean;
	}
	public PCMSAllDetail _genPCMSAllDetail(Map<String, Object> map) {
		PCMSAllDetail bean = this.dao._genPCMSAllDetail(map);
		return bean;
	}

	public PresetDetail _genPresetDetail(Map<String, Object> map) {
		PresetDetail bean = this.dao._genPresetDetail(map);
		return bean;
	}

	public DyeingDetail _genDyeingDetail(Map<String, Object> map) {
		DyeingDetail bean = this.dao._genDyeingDetail(map);
		return bean;
	}

	public PODetail _genPODetail(Map<String, Object> map) {
		PODetail bean = this.dao._genPODetail(map);
		return bean;
	}

	public SendTestQCDetail _genSendTestQCDetail(Map<String, Object> map) {
		SendTestQCDetail bean = this.dao._genSendTestQCDetail(map);
		return bean;
	}

	public InspectDetail _genInspectDetail(Map<String, Object> map) {
		InspectDetail bean = this.dao._genInspectDetail(map);
		return bean;
	}

	public FinishingDetail _genFinishingDetail(Map<String, Object> map) {
		FinishingDetail bean = this.dao._genFinishingDetail(map);
		return bean;
	}  

	public PackingDetail _genPackingDetail(Map<String, Object> map) {
		PackingDetail bean = this.dao._genPackingDetail(map);
		return bean;
	}

	public WorkInLabDetail _genWorkInLabDetail(Map<String, Object> map) {
		WorkInLabDetail bean = this.dao._genWorkInLabDetail(map);
		return bean;
	}

	public WaitTestDetail _genWaitTestDetail(Map<String, Object> map) {
		WaitTestDetail bean = this.dao._genWaitTestDetail(map);
		return bean;
	}

	public CFMDetail _genCFMDetail(Map<String, Object> map) {
		CFMDetail bean = this.dao._genCFMDetail(map);
		return bean;
	}

	public SaleDetail _genSaleDetail(Map<String, Object> map) {
		SaleDetail bean = this.dao._genSaleDetail(map);
		return bean;
	}

	public SaleInputDetail _genSaleInputDetail(Map<String, Object> map) {
		SaleInputDetail bean = this.dao._genSaleInputDetail(map);
		return bean;
	}

	public SubmitDateDetail _genSubmitDateDetail(Map<String, Object> map) {
		SubmitDateDetail bean = this.dao._genSubmitDateDetail(map);
		return bean;
	}

	public NCDetail _genNCDetail(Map<String, Object> map) {
		NCDetail bean = this.dao._genNCDetail(map);
		return bean;
	}

	public ReceipeDetail _genReceipeDetail(Map<String, Object> map) {
		ReceipeDetail bean = this.dao._genReceipeDetail(map);
		return bean;
	}

	public PCMSSecondTableDetail _genPCMSSecondTableDetail(Map<String, Object> map) {
		PCMSSecondTableDetail bean = this.dao._genPCMSSecondTableDetail(map);
		return bean;
	}

	public InputDateDetail _genInputDateDetail(Map<String, Object> map) {
		InputDateDetail bean = this.dao._genInputDateDetail(map);
		return bean;
	}

	public ColumnHiddenDetail _genColumnHiddenDetail(Map<String, Object> map) {
		ColumnHiddenDetail bean = this.dao._genColumnHiddenDetail(map);
		return bean;
	}

	public PCMSTableDetail _genSearchTableDetail(Map<String, Object> map) {
		PCMSTableDetail bean = this.dao._genSearchTableDetail(map);
		return bean;
	}

	public ReplacedProdOrderDetail _genReplacedProdOrderDetail(Map<String, Object> map) {
		ReplacedProdOrderDetail bean = this.dao._genReplacedProdOrderDetail(map);
		return bean;
	}

	public SwitchProdOrderDetail _genSwitchProdOrderDetail(Map<String, Object> map) {
		SwitchProdOrderDetail bean = this.dao._genSwitchProdOrderDetail(map);
		return bean;
	}
}
