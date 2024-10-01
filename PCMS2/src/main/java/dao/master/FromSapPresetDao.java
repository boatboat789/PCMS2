package dao.master;

import java.util.ArrayList;

import entities.PresetDetail;

public interface FromSapPresetDao {

	ArrayList<PresetDetail> getFromSapPresetDetailByProductionOrder(String prodOrder);

}
