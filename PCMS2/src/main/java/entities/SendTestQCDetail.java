package entities;

public class SendTestQCDetail {
	private String sendDate;
	private String rollNo;
	private String status;
	private String checkColorDate;
	private String deltaE;
	private String color;
	private String remark;
	public SendTestQCDetail(String sendDate, String rollNo, String status, String checkColorDate, String deltaE,
			String color, String remark) {
		super();
		this.sendDate = sendDate;
		this.rollNo = rollNo;
		this.status = status;
		this.checkColorDate = checkColorDate;
		this.deltaE = deltaE;
		this.color = color;
		this.remark = remark;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCheckColorDate() {
		return checkColorDate;
	}
	public void setCheckColorDate(String checkColorDate) {
		this.checkColorDate = checkColorDate;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	} 
}
