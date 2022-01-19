package entities;

import java.util.List;

public class ColumnHiddenDetail {
	private String UserId;
	private String ColName;
	 private List<String> ColList;
	 private String IconStatus;
		private String SystemStatus;

	public ColumnHiddenDetail(String userId, String colName) {
		super();
		UserId = userId;
		ColName = colName;
	}
	public ColumnHiddenDetail() {
		// TODO Auto-generated constructor stub
	}
	public List<String> getColList() {
		return ColList;
	}
	public void setColList(List<String> colList) {
		ColList = colList;
	}
	public String getIconStatus() {
		return IconStatus;
	}
	public void setIconStatus(String iconStatus) {
		IconStatus = iconStatus;
	}
	public String getSystemStatus() {
		return SystemStatus;
	}
	public void setSystemStatus(String systemStatus) {
		SystemStatus = systemStatus;
	}
	public String getUserId() {
		return UserId;
	} 
	public void setUserId(String userId) {
		this.UserId = userId;
	}
	public String getColName() {
		return ColName;
	}
	public void setColName(String colName) {
		ColName = colName;
	}
}
