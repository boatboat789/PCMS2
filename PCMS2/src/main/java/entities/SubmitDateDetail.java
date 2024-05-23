package entities;

public class SubmitDateDetail {
	private String no;
  	private String submitDate;
  	private String remark;
	public SubmitDateDetail(String no, String submitDate, String remark) {
		super();
		this.no = no;
		this.submitDate = submitDate;
		this.remark = remark;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
