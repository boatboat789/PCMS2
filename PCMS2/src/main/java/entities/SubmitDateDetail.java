package entities;

public class SubmitDateDetail {         
	private String No;
  	private String SubmitDate;
  	private String Remark;
	public SubmitDateDetail(String no, String submitDate, String remark) {
		super();
		No = no;
		SubmitDate = submitDate;
		Remark = remark;
	}
	public String getNo() {
		return No;
	}
	public void setNo(String no) {
		No = no;
	}
	public String getSubmitDate() {
		return SubmitDate;
	}
	public void setSubmitDate(String submitDate) {
		SubmitDate = submitDate;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	} 
}
