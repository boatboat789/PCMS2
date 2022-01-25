package entities;

import java.util.List;

public class ColumnHiddenDetail {
	private String UserId;
	private String ColVisibleDetail;
	private String ColVisibleSummary;
	 private List<String> ColList;
	 private String IconStatus;
		private String SystemStatus;

	public ColumnHiddenDetail(String userId, String ColVisibleDetail,String ColVisibleSummary) {
		super();
		UserId = userId;
		this.ColVisibleDetail = ColVisibleDetail;
		this.ColVisibleSummary = ColVisibleSummary;
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
	public String getColVisibleDetail() {
		return ColVisibleDetail;
	}
	public void setColVisibleDetail(String colVisibleDetail) {
		ColVisibleDetail = colVisibleDetail;
	}
	public String getColVisibleSummary() {
		return ColVisibleSummary;
	}
	public void setColVisibleSummary(String colVisibleSummary) {
		ColVisibleSummary = colVisibleSummary;
	}
	 
}
