package entities;

public class SendTestQCDetail {
	private String SendDate;
	private String RollNo;
	private String Status;
	private String CheckColorDate;
	private String DeltaE;
	private String Color;
	private String Remark;
	public SendTestQCDetail(String sendDate, String rollNo, String status, String checkColorDate, String deltaE,
			String color, String remark) {
		super();
		SendDate = sendDate;
		RollNo = rollNo;
		Status = status;
		CheckColorDate = checkColorDate;
		DeltaE = deltaE;
		Color = color;
		Remark = remark;
	}
	public String getSendDate() {
		return SendDate;
	}
	public void setSendDate(String sendDate) {
		SendDate = sendDate;
	}
	public String getRollNo() {
		return RollNo;
	}
	public void setRollNo(String rollNo) {
		RollNo = rollNo;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getCheckColorDate() {
		return CheckColorDate;
	}
	public void setCheckColorDate(String checkColorDate) {
		CheckColorDate = checkColorDate;
	}
	public String getDeltaE() {
		return DeltaE;
	}
	public void setDeltaE(String deltaE) {
		DeltaE = deltaE;
	}
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	} 
}
