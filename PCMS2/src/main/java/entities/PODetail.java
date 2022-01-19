package entities;

public class PODetail {
	private String ProductionOrder;
	private String PONo ;
	private String POLine ;
	private String CreateDate ;
	private String RequiredDate ;
	private String RollNo ;
	private String QuantityKG ;
	private String QuantityMR ;
	private String PODefault ;
	private String POLineDefault ;
	private String POPostingDateDefault ;
	public PODetail(String productionOrder, String pONo, String pOLine, String createDate, String RequiredDate, String rollNo,
			String quantityKG, String quantityMR, String pODefault, String pOLineDefault, String pOPostingDateDefault) {
		super();
		ProductionOrder = productionOrder;
		PONo = pONo;
		POLine = pOLine;
		CreateDate = createDate;
		this.RequiredDate = RequiredDate;
		RollNo = rollNo;
		QuantityKG = quantityKG;
		QuantityMR = quantityMR;
		PODefault = pODefault;
		POLineDefault = pOLineDefault;
		POPostingDateDefault = pOPostingDateDefault;
	}
	public String getProductionOrder() {
		return ProductionOrder;
	}
	public void setProductionOrder(String productionOrder) {
		ProductionOrder = productionOrder;
	}
	public String getPONo() {
		return PONo;
	}
	public void setPONo(String pONo) {
		PONo = pONo;
	}
	public String getPOLine() {
		return POLine;
	}
	public void setPOLine(String pOLine) {
		POLine = pOLine;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public String getRequiredDate() {
		return RequiredDate;
	}
	public void setRequiredDate(String RequiredDate) {
		this.RequiredDate = RequiredDate;
	}
	public String getRollNo() {
		return RollNo;
	}
	public void setRollNo(String rollNo) {
		RollNo = rollNo;
	}
	public String getQuantityKG() {
		return QuantityKG;
	}
	public void setQuantityKG(String quantityKG) {
		QuantityKG = quantityKG;
	}
	public String getQuantityMR() {
		return QuantityMR;
	}
	public void setQuantityMR(String quantityMR) {
		QuantityMR = quantityMR;
	}
	public String getPODefault() {
		return PODefault;
	}
	public void setPODefault(String pODefault) {
		PODefault = pODefault;
	}
	public String getPOLineDefault() {
		return POLineDefault;
	}
	public void setPOLineDefault(String pOLineDefault) {
		POLineDefault = pOLineDefault;
	}
	public String getPOPostingDateDefault() {
		return POPostingDateDefault;
	}
	public void setPOPostingDateDefault(String pOPostingDateDefault) {
		POPostingDateDefault = pOPostingDateDefault;
	} 
}
