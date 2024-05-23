package entities;

public class DyeingDetail {
	private String postingDate;
	private String operation;
	private String workCenter;
	private String dyeStatus;
	private String deltaE;
	private String l;
	private String da;
	private String db;
	private String st	;
	private String remark	;
	private String redye;
	private String batch;
	private String colorStatus;
	private String colorRemark;
	public DyeingDetail(String postingDate, String operation, String workCenter, String dyeStatus, String deltaE,
			String l, String da, String db, String sT, String remark, String redye, String batch, String colorStatus,
			String colorRemark) {
		super();
		this.postingDate = postingDate;
		this.operation = operation;
		this.workCenter = workCenter;
		this.dyeStatus = dyeStatus;
		this.deltaE = deltaE;
		this.l = l;
		this.da = da;
		this.db = db;
		this.st = sT;
		this.remark = remark;
		this.redye = redye;
		this.batch = batch;
		this.colorStatus = colorStatus;
		this.colorRemark = colorRemark;
	}
	public String getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getWorkCenter() {
		return workCenter;
	}
	public void setWorkCenter(String workCenter) {
		this.workCenter = workCenter;
	}
	public String getDyeStatus() {
		return dyeStatus;
	}
	public void setDyeStatus(String dyeStatus) {
		this.dyeStatus = dyeStatus;
	}
	public String getDeltaE() {
		return deltaE;
	}
	public void setDeltaE(String deltaE) {
		this.deltaE = deltaE;
	}
	public String getL() {
		return l;
	}
	public void setL(String l) {
		this.l = l;
	}
	public String getDa() {
		return da;
	}
	public void setDa(String da) {
		this.da = da;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRedye() {
		return redye;
	}
	public void setRedye(String redye) {
		this.redye = redye;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getColorStatus() {
		return colorStatus;
	}
	public void setColorStatus(String colorStatus) {
		this.colorStatus = colorStatus;
	}
	public String getColorRemark() {
		return colorRemark;
	}
	public void setColorRemark(String colorRemark) {
		this.colorRemark = colorRemark;
	}
}
