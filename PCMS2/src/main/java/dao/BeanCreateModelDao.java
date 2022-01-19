package dao;

import java.util.Map;

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
import entities.SaleDetail;
import entities.SaleInputDetail;
import entities.SendTestQCDetail;
import entities.SubmitDateDetail;
import entities.WaitTestDetail;
import entities.WorkInLabDetail;

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

}
