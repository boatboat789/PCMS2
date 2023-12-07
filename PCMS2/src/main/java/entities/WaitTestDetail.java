package entities;

public class WaitTestDetail { 
	private String no;
	private String dateInTest;
	private String dateOutTest;
	private String status;
	private String remark;
	public WaitTestDetail(String no, String dateInTest, String dateOutTest, String status, String remark) {
		super();
		this.no = no;
		this.dateInTest = dateInTest;
		this.dateOutTest = dateOutTest;
		this.status = status;
		this.remark = remark;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getDateInTest() {
		return dateInTest;
	}
	public void setDateInTest(String dateInTest) {
		this.dateInTest = dateInTest;
	}
	public String getDateOutTest() {
		return dateOutTest;
	}
	public void setDateOutTest(String dateOutTest) {
		this.dateOutTest = dateOutTest;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	} 
}
