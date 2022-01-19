package entities;

public class CFMDetail {         
	  private String CFMNo;
	  private String CFMNumber;
	  private String CFMSendDate;
	  private String RollNo;
	  private String RollNoRemark;
	  private String L;
	  private String Da;
	  private String Db; 
	  private String ST;
	  private String SaleOrder;
	  private String SaleLine;
	  private String Color;
	  private String CFMAnswerDate;
	  private String CFMStatus;
	  private String CFMRemark; 
	  private String NextLot;
	  private String SOChange;
	  private String SOChangeQty;
	  private String SOChangeUnit;
	public CFMDetail(String cFMNo, String cFMNumber, String cFMSendDate, String rollNo, String rollNoRemark, String l,
			String da, String db, String sT, String saleOrder, String saleLine, String color, String cFMAnswerDate,
			String cFMStatus, String cFMRemark, String NextLot, String sOChange, String sOChangeQty,
			String sOChangeUnit) {
		super();
		CFMNo = cFMNo;
		CFMNumber = cFMNumber;
		CFMSendDate = cFMSendDate;
		RollNo = rollNo;
		RollNoRemark = rollNoRemark;
		L = l;
		Da = da;
		Db = db;
		ST = sT;
		SaleOrder = saleOrder;
		SaleLine = saleLine;
		Color = color;
		CFMAnswerDate = cFMAnswerDate;
		CFMStatus = cFMStatus;
		CFMRemark = cFMRemark;
		this.NextLot = NextLot;
		SOChange = sOChange;
		SOChangeQty = sOChangeQty;
		SOChangeUnit = sOChangeUnit;
	}
	public String getCFMNo() {
		return CFMNo;
	}
	public void setCFMNo(String cFMNo) {
		CFMNo = cFMNo;
	}
	public String getCFMNumber() {
		return CFMNumber;
	}
	public void setCFMNumber(String cFMNumber) {
		CFMNumber = cFMNumber;
	}
	public String getCFMSendDate() {
		return CFMSendDate;
	}
	public void setCFMSendDate(String cFMSendDate) {
		CFMSendDate = cFMSendDate;
	}
	public String getRollNo() {
		return RollNo;
	}
	public void setRollNo(String rollNo) {
		RollNo = rollNo;
	}
	public String getRollNoRemark() {
		return RollNoRemark;
	}
	public void setRollNoRemark(String rollNoRemark) {
		RollNoRemark = rollNoRemark;
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
	public String getSaleOrder() {
		return SaleOrder;
	}
	public void setSaleOrder(String saleOrder) {
		SaleOrder = saleOrder;
	}
	public String getSaleLine() {
		return SaleLine;
	}
	public void setSaleLine(String saleLine) {
		SaleLine = saleLine;
	}
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	public String getCFMAnswerDate() {
		return CFMAnswerDate;
	}
	public void setCFMAnswerDate(String cFMAnswerDate) {
		CFMAnswerDate = cFMAnswerDate;
	}
	public String getCFMStatus() {
		return CFMStatus;
	}
	public void setCFMStatus(String cFMStatus) {
		CFMStatus = cFMStatus;
	}
	public String getCFMRemark() {
		return CFMRemark;
	}
	public void setCFMRemark(String cFMRemark) {
		CFMRemark = cFMRemark;
	}
	public String getNextLot() {
		return NextLot;
	}
	public void setNextLot(String NextLot) {
		this.NextLot = NextLot;
	}
	public String getSOChange() {
		return SOChange;
	}
	public void setSOChange(String sOChange) {
		SOChange = sOChange;
	}
	public String getSOChangeQty() {
		return SOChangeQty;
	}
	public void setSOChangeQty(String sOChangeQty) {
		SOChangeQty = sOChangeQty;
	}
	public String getSOChangeUnit() {
		return SOChangeUnit;
	}
	public void setSOChangeUnit(String sOChangeUnit) {
		SOChangeUnit = sOChangeUnit;
	} 
}
