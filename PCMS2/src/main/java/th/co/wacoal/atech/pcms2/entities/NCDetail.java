package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class NCDetail {

	private String productionOrder; 
	private String no ; 
	private String ncDate; 
	private String ncLength; 
	private String ncReceiverBase; 
	private String ncCarNumber; 
	private String ncProblem ; 
	private String ncSolution; 
public NCDetail(String productionOrder, String no, String nCDate, String ncLength, String ncReceiverBase, String ncCarNumber,
			String ncProblem, String ncSolution) {
		super();
		this.productionOrder = productionOrder;
		this.no = no;
		this.ncDate = nCDate;
		this.ncLength = ncLength;
		this.ncReceiverBase = ncReceiverBase;
		this.ncCarNumber = ncCarNumber;
		this.ncProblem = ncProblem;
		this.ncSolution = ncSolution;
	}
	//	private String no;
//	private String ncDate;
//	private String carNo;
//	private String quantity;
//	private String unit;
//	private String ncFrom;
//	private String remark;
//	public NCDetail(String no, String nCDate, String carNo, String quantity, String unit, String nCFrom,
//			String remark) {
//		super();
//		this.no = no;
//		this.ncDate = nCDate;
//		this.carNo = carNo;
//		this.quantity = quantity;
//		this.unit = unit;
//		this.ncFrom = nCFrom;
//		this.remark = remark;
//	}
//	public String getNo() {
//		return no;
//	}
//	public void setNo(String no) {
//		this.no = no;
//	}
//	public String getNcDate() {
//		return ncDate;
//	}
//	public void setNcDate(String ncDate) {
//		this.ncDate = ncDate;
//	}
//	public String getCarNo() {
//		return carNo;
//	}
//	public void setCarNo(String carNo) {
//		this.carNo = carNo;
//	}
//	public String getQuantity() {
//		return quantity;
//	}
//	public void setQuantity(String quantity) {
//		this.quantity = quantity;
//	}
//	public String getUnit() {
//		return unit;
//	}
//	public void setUnit(String unit) {
//		this.unit = unit;
//	}
//	public String getNcFrom() {
//		return ncFrom;
//	}
//	public void setNcFrom(String ncFrom) {
//		this.ncFrom = ncFrom;
//	}
//	public String getRemark() {
//		return remark;
//	}
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
	public String getProductionOrder()
	{
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder)
	{
		this.productionOrder = productionOrder;
	}
	public String getNo()
	{
		return no;
	}
	public void setNo(String no)
	{
		this.no = no;
	}
	public String getncDate()
	{
		return ncDate;
	}
	public void setncDate(String nCDate)
	{
		this.ncDate = nCDate;
	}
	public String getNcLength()
	{
		return ncLength;
	}
	public void setNcLength(String ncLength)
	{
		this.ncLength = ncLength;
	}
	public String getNcReceiverBase()
	{
		return ncReceiverBase;
	}
	public void setNcReceiverBase(String ncReceiverBase)
	{
		this.ncReceiverBase = ncReceiverBase;
	}
	public String getNcCarNumber()
	{
		return ncCarNumber;
	}
	public void setNcCarNumber(String ncCarNumber)
	{
		this.ncCarNumber = ncCarNumber;
	}
	public String getNcProblem()
	{
		return ncProblem;
	}
	public void setNcProblem(String ncProblem)
	{
		this.ncProblem = ncProblem;
	}
	public String getNcSolution()
	{
		return ncSolution;
	}
	public void setNcSolution(String ncSolution)
	{
		this.ncSolution = ncSolution;
	}
}
