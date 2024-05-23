package dao.master;

import java.util.ArrayList;

import entities.ColumnHiddenDetail;

public interface ColumnSettingDao {


	ArrayList<ColumnHiddenDetail> upsertColumnSettingDetail(ColumnHiddenDetail pd);

	ArrayList<ColumnHiddenDetail> getColumnVisibleDetail(String user);

	ArrayList<ColumnHiddenDetail> upsertColumnVisibleSummary(ColumnHiddenDetail pd);


}
