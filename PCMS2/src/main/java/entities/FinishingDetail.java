package entities;

public class FinishingDetail {
	private String postingDate;
	private String workCenter;
	private String status;
	private String ncDate;
	private String cause;
	private String carNo;
	private String deltaE;
	private String color;
	private String operation;
	private String ccStatus;
	private String ccRemark;
	private String rollNo;
	private String da;
	private String db;
	private String l;
	private String st;
	private String ccPostingDate;
	private String ccOperation;
	private String lotNo;

	public FinishingDetail(String postingDate, String workCenter, String status, String nCDate, String cause,
			String carNo, String deltaE, String color, String operation, String cCStatus, String cCRemark,
			String rollNo, String da, String db, String l, String sT, String cCPostingDate, String cCOperation,
			String lotNo) {
		super();
		this.postingDate = postingDate;
		this.workCenter = workCenter;
		this.status = status;
		this.ncDate = nCDate;
		this.cause = cause;
		this.carNo = carNo;
		this.deltaE = deltaE;
		this.color = color;
		this.operation = operation;
		this.ccStatus = cCStatus;
		this.ccRemark = cCRemark;
		this.rollNo = rollNo;
		this.da = da;
		this.db = db;
		this.l = l;
		this.st = sT;
		this.ccPostingDate = cCPostingDate;
		this.ccOperation = cCOperation;
		this.lotNo = lotNo;
	}

	public String getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}

	public String getWorkCenter() {
		return workCenter;
	}

	public void setWorkCenter(String workCenter) {
		this.workCenter = workCenter;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNcDate() {
		return ncDate;
	}

	public void setNcDate(String ncDate) {
		this.ncDate = ncDate;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getDeltaE() {
		return deltaE;
	}

	public void setDeltaE(String deltaE) {
		this.deltaE = deltaE;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCcStatus() {
		return ccStatus;
	}

	public void setCcStatus(String ccStatus) {
		this.ccStatus = ccStatus;
	}

	public String getCcRemark() {
		return ccRemark;
	}

	public void setCcRemark(String ccRemark) {
		this.ccRemark = ccRemark;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
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

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getCcPostingDate() {
		return ccPostingDate;
	}

	public void setCcPostingDate(String ccPostingDate) {
		this.ccPostingDate = ccPostingDate;
	}

	public String getCcOperation() {
		return ccOperation;
	}

	public void setCcOperation(String ccOperation) {
		this.ccOperation = ccOperation;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
}
