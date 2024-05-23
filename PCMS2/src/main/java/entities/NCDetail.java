package entities;

public class NCDetail {
	private String no;
	private String ncDate;
	private String carNo;
	private String quantity;
	private String unit;
	private String ncFrom;
	private String remark;
	public NCDetail(String no, String nCDate, String carNo, String quantity, String unit, String nCFrom,
			String remark) {
		super();
		this.no = no;
		this.ncDate = nCDate;
		this.carNo = carNo;
		this.quantity = quantity;
		this.unit = unit;
		this.ncFrom = nCFrom;
		this.remark = remark;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getNcDate() {
		return ncDate;
	}
	public void setNcDate(String ncDate) {
		this.ncDate = ncDate;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNcFrom() {
		return ncFrom;
	}
	public void setNcFrom(String ncFrom) {
		this.ncFrom = ncFrom;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
