package th.co.wacoal.atech.pcms2.entities;

import java.util.List;

public class ColumnHiddenDetail {
	private String userId;
	private String colVisibleDetail;
	private String colVisibleSummary;
	private List<String> colList;
	private String iconStatus;
	private String systemStatus;

	public ColumnHiddenDetail(String userId, String ColVisibleDetail,String ColVisibleSummary ) {
		super();
		this.userId = userId;
		this.colVisibleDetail = ColVisibleDetail;
		this.colVisibleSummary = ColVisibleSummary;
	}
	public ColumnHiddenDetail() {
		// TODO Auto-generated constructor stub
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getColVisibleDetail() {
		return colVisibleDetail;
	}
	public void setColVisibleDetail(String colVisibleDetail) {
		this.colVisibleDetail = colVisibleDetail;
	}
	public String getColVisibleSummary() {
		return colVisibleSummary;
	}
	public void setColVisibleSummary(String colVisibleSummary) {
		this.colVisibleSummary = colVisibleSummary;
	}
	public List<String> getColList() {
		return colList;
	}
	public void setColList(List<String> colList) {
		this.colList = colList;
	}
	public String getIconStatus() {
		return iconStatus;
	}
	public void setIconStatus(String iconStatus) {
		this.iconStatus = iconStatus;
	}
	public String getSystemStatus() {
		return systemStatus;
	}
	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
	}
}
