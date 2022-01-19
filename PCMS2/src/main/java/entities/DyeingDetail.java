package entities;

public class DyeingDetail {
	private String PostingDate;
	private String Operation;
	private String WorkCenter;
	private String DyeStatus;
	private String DeltaE;
	private String L;
	private String Da;
	private String Db;
	private String ST	;
	private String Remark	;
	private String Redye;
	private String Batch;
	private String ColorStatus;
	private String ColorRemark;
	public DyeingDetail(String postingDate, String operation, String workCenter, String dyeStatus, String deltaE,
			String l, String da, String db, String sT, String remark, String redye, String batch, String colorStatus,
			String colorRemark) {
		super();
		PostingDate = postingDate;
		Operation = operation;
		WorkCenter = workCenter;
		DyeStatus = dyeStatus;
		DeltaE = deltaE;
		L = l;
		Da = da;
		Db = db;
		ST = sT;
		Remark = remark;
		Redye = redye;
		Batch = batch;
		ColorStatus = colorStatus;
		ColorRemark = colorRemark;
	}
	public String getPostingDate() {
		return PostingDate;
	}
	public void setPostingDate(String postingDate) {
		PostingDate = postingDate;
	}
	public String getOperation() {
		return Operation;
	}
	public void setOperation(String operation) {
		Operation = operation;
	}
	public String getWorkCenter() {
		return WorkCenter;
	}
	public void setWorkCenter(String workCenter) {
		WorkCenter = workCenter;
	}
	public String getDyeStatus() {
		return DyeStatus;
	}
	public void setDyeStatus(String dyeStatus) {
		DyeStatus = dyeStatus;
	}
	public String getDeltaE() {
		return DeltaE;
	}
	public void setDeltaE(String deltaE) {
		DeltaE = deltaE;
	}
	public String getL() {
		return L;
	}
	public void setL(String l) {
		L = l;
	}
	public String getDa() {
		return Da;
	}
	public void setDa(String da) {
		Da = da;
	}
	public String getDb() {
		return Db;
	}
	public void setDb(String db) {
		Db = db;
	}
	public String getST() {
		return ST;
	}
	public void setST(String sT) {
		ST = sT;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getRedye() {
		return Redye;
	}
	public void setRedye(String redye) {
		Redye = redye;
	}
	public String getBatch() {
		return Batch;
	}
	public void setBatch(String batch) {
		Batch = batch;
	}
	public String getColorStatus() {
		return ColorStatus;
	}
	public void setColorStatus(String colorStatus) {
		ColorStatus = colorStatus;
	}
	public String getColorRemark() {
		return ColorRemark;
	}
	public void setColorRemark(String colorRemark) {
		ColorRemark = colorRemark;
	}  
}
