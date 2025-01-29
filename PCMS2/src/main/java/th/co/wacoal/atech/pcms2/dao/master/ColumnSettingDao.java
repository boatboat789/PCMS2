package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.ColumnHiddenDetail;

public interface ColumnSettingDao {


	ArrayList<ColumnHiddenDetail> upsertColumnSettingDetail(ColumnHiddenDetail pd);

	ArrayList<ColumnHiddenDetail> getColumnVisibleDetail(String user);

	ArrayList<ColumnHiddenDetail> upsertColumnVisibleSummary(ColumnHiddenDetail pd);


}
