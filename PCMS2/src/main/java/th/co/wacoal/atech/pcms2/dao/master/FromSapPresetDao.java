package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PresetDetail;

public interface FromSapPresetDao {

	ArrayList<PresetDetail> getFromSapPresetDetailByProductionOrder(String prodOrder);

}
