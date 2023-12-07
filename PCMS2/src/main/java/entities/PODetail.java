package entities;

public class PODetail {
	private String productionOrder;
	private String poNo ;
	private String poLine ;
	private String poCreatedate;
//	private String createDate ;
	private String requiredDate ;
	private String rollNo ;
	private String quantityKG ;
	private String quantityMR ;
	private String poDefault ;
	private String poLineDefault ;
	private String poPostingDateDefault ;
	public PODetail(String productionOrder, String pONo, String pOLine, String poCreatedate, String RequiredDate, String rollNo,
			String quantityKG, String quantityMR, String pODefault, String pOLineDefault, String pOPostingDateDefault ) {
		super();
		this.poCreatedate = poCreatedate;
		this.productionOrder = productionOrder;
		this.poNo = pONo;
		this.poLine = pOLine;
//		this.createDate = createDate;
		this.requiredDate = RequiredDate;
		this.rollNo = rollNo;
		this.quantityKG = quantityKG;
		this.quantityMR = quantityMR;
		this.poDefault = pODefault;
		this.poLineDefault = pOLineDefault;
		this.poPostingDateDefault = pOPostingDateDefault;
	}
	public String getPoCreatedate() {
		return poCreatedate;
	}
	public void setPoCreatedate(String poCreatedate) {
		this.poCreatedate = poCreatedate;
	}
	public String getProductionOrder() {
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder) {
		this.productionOrder = productionOrder;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getPoLine() {
		return poLine;
	}
	public void setPoLine(String poLine) {
		this.poLine = poLine;
	}
//	public String getCreateDate() {
//		return createDate;
//	}
//	public void setCreateDate(String createDate) {
//		this.createDate = createDate;
//	}
	public String getRequiredDate() {
		return requiredDate;
	}
	public void setRequiredDate(String requiredDate) {
		this.requiredDate = requiredDate;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getQuantityKG() {
		return quantityKG;
	}
	public void setQuantityKG(String quantityKG) {
		this.quantityKG = quantityKG;
	}
	public String getQuantityMR() {
		return quantityMR;
	}
	public void setQuantityMR(String quantityMR) {
		this.quantityMR = quantityMR;
	}
	public String getPoDefault() {
		return poDefault;
	}
	public void setPoDefault(String poDefault) {
		this.poDefault = poDefault;
	}
	public String getPoLineDefault() {
		return poLineDefault;
	}
	public void setPoLineDefault(String poLineDefault) {
		this.poLineDefault = poLineDefault;
	}
	public String getPoPostingDateDefault() {
		return poPostingDateDefault;
	}
	public void setPoPostingDateDefault(String poPostingDateDefault) {
		this.poPostingDateDefault = poPostingDateDefault;
	} 
}
