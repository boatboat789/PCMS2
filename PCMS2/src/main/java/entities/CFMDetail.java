package entities;

public class CFMDetail {         
	  private String cfmNo;
	  private String cfmNumber;
	  private String cfmSendDate;
	  private String rollNo;
	  private String rollNoRemark;
	  private String l;
	  private String da;
	  private String db; 
	  private String st;
	  private String de;
	  private String saleOrder;
	  private String saleLine;
	  private String color;
	  private String cfmAnswerDate;
	  private String cfmStatus;
	  private String cfmRemark; 
	  private String nextLot;
	  private String soChange;
	  private String soChangeQty;
	  private String soChangeUnit;
	public CFMDetail(String cFMNo, String cFMNumber, String cFMSendDate, String rollNo, String rollNoRemark, String l,
			String da, String db, String sT, String saleOrder, String saleLine, String color, String cFMAnswerDate,
			String cFMStatus, String cFMRemark, String NextLot, String sOChange, String sOChangeQty,
			String sOChangeUnit,String de) {
		super();
		this.de = de;
		this.cfmNo = cFMNo;
		this.cfmNumber = cFMNumber;
		this.cfmSendDate = cFMSendDate;
		this.rollNo = rollNo;
		this.rollNoRemark = rollNoRemark;
		this.l = l;
		this.da = da;
		this.db = db;
		this.st = sT;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.color = color;
		this.cfmAnswerDate = cFMAnswerDate;
		this.cfmStatus = cFMStatus;
		this.cfmRemark = cFMRemark;
		this.nextLot = NextLot;
		this.soChange = sOChange;
		this.soChangeQty = sOChangeQty;
		this.soChangeUnit = sOChangeUnit;
	}
	public String getCfmNo() {
		return cfmNo;
	}
	public void setCfmNo(String cfmNo) {
		this.cfmNo = cfmNo;
	}
	public String getCfmNumber() {
		return cfmNumber;
	}
	public void setCfmNumber(String cfmNumber) {
		this.cfmNumber = cfmNumber;
	}
	public String getCfmSendDate() {
		return cfmSendDate;
	}
	public void setCfmSendDate(String cfmSendDate) {
		this.cfmSendDate = cfmSendDate;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getRollNoRemark() {
		return rollNoRemark;
	}
	public void setRollNoRemark(String rollNoRemark) {
		this.rollNoRemark = rollNoRemark;
	}
	public String getL() {
		return l;
	}
	public void setL(String l) {
		this.l = l;
	}
	public String getDa() {
		return da;
	}
	public void setDa(String da) {
		this.da = da;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getDe() {
		return de;
	}
	public void setDe(String de) {
		this.de = de;
	}
	public String getSaleOrder() {
		return saleOrder;
	}
	public void setSaleOrder(String saleOrder) {
		this.saleOrder = saleOrder;
	}
	public String getSaleLine() {
		return saleLine;
	}
	public void setSaleLine(String saleLine) {
		this.saleLine = saleLine;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getCfmAnswerDate() {
		return cfmAnswerDate;
	}
	public void setCfmAnswerDate(String cfmAnswerDate) {
		this.cfmAnswerDate = cfmAnswerDate;
	}
	public String getCfmStatus() {
		return cfmStatus;
	}
	public void setCfmStatus(String cfmStatus) {
		this.cfmStatus = cfmStatus;
	}
	public String getCfmRemark() {
		return cfmRemark;
	}
	public void setCfmRemark(String cfmRemark) {
		this.cfmRemark = cfmRemark;
	}
	public String getNextLot() {
		return nextLot;
	}
	public void setNextLot(String nextLot) {
		this.nextLot = nextLot;
	}
	public String getSoChange() {
		return soChange;
	}
	public void setSoChange(String soChange) {
		this.soChange = soChange;
	}
	public String getSoChangeQty() {
		return soChangeQty;
	}
	public void setSoChangeQty(String soChangeQty) {
		this.soChangeQty = soChangeQty;
	}
	public String getSoChangeUnit() {
		return soChangeUnit;
	}
	public void setSoChangeUnit(String soChangeUnit) {
		this.soChangeUnit = soChangeUnit;
	} 
}
