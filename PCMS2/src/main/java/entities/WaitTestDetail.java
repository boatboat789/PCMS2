package entities;

public class WaitTestDetail { 
	private String No;
	private String DateInTest;
	private String DateOutTest;
	private String Status;
	private String Remark;
	public WaitTestDetail(String no, String dateInTest, String dateOutTest, String status, String remark) {
		super();
		No = no;
		DateInTest = dateInTest;
		DateOutTest = dateOutTest;
		Status = status;
		Remark = remark;
	}
	public String getNo() {
		return No;
	}
	public void setNo(String no) {
		No = no;
	}
	public String getDateInTest() {
		return DateInTest;
	}
	public void setDateInTest(String dateInTest) {
		DateInTest = dateInTest;
	}
	public String getDateOutTest() {
		return DateOutTest;
	}
	public void setDateOutTest(String dateOutTest) {
		DateOutTest = dateOutTest;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	} 
}
