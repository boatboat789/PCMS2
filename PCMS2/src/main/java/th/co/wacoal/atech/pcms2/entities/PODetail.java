package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class PODetail {
	private String productionOrder;
	private String poNo ;
	private String poLine ;
	private String poCreatedate;
//	private String createDate ;
	private String requiredDate ;
	private String rollNumber ;
	private String rollWeight ;
	private String rollLength ;
	private String poDefault ;
	private String poLineDefault ;
	private String poPostingDateDefault ;
	public PODetail(String productionOrder, String pONo, String pOLine, String poCreatedate, String RequiredDate, String rollNumber,
			String rollWeight, String rollLength, String pODefault, String pOLineDefault, String pOPostingDateDefault ) {
		super();
		this.poCreatedate = poCreatedate;
		this.productionOrder = productionOrder;
		this.poNo = pONo;
		this.poLine = pOLine;
//		this.createDate = createDate;
		this.requiredDate = RequiredDate;
		this.rollNumber =rollNumber;
		this.rollWeight = rollWeight;
		this.rollLength = rollLength;
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
 
	public String getRollNumber()
	{
		return rollNumber;
	}
	public void setRollNumber(String rollNumber)
	{
		this.rollNumber = rollNumber;
	}
	public String getRollWeight()
	{
		return rollWeight;
	}
	public void setRollWeight(String rollWeight)
	{
		this.rollWeight = rollWeight;
	}
	public String getRollLength()
	{
		return rollLength;
	}
	public void setRollLength(String rollLength)
	{
		this.rollLength = rollLength;
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
