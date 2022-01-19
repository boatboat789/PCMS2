package entities;

public class NCDetail { 
	private String No;
	private String NCDate;
	private String CarNo;
	private String Quantity;
	private String Unit;
	private String NCFrom;
	private String Remark;
	public NCDetail(String no, String nCDate, String carNo, String quantity, String unit, String nCFrom,
			String remark) {
		super();
		No = no;
		NCDate = nCDate;
		CarNo = carNo;
		Quantity = quantity;
		Unit = unit;
		NCFrom = nCFrom;
		Remark = remark;
	}
	public String getNo() {
		return No;
	}
	public void setNo(String no) {
		No = no;
	}
	public String getNCDate() {
		return NCDate;
	}
	public void setNCDate(String nCDate) {
		NCDate = nCDate;
	}
	public String getCarNo() {
		return CarNo;
	}
	public void setCarNo(String carNo) {
		CarNo = carNo;
	}
	public String getQuantity() {
		return Quantity;
	}
	public void setQuantity(String quantity) {
		Quantity = quantity;
	}
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public String getNCFrom() {
		return NCFrom;
	}
	public void setNCFrom(String nCFrom) {
		NCFrom = nCFrom;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	} 
}
