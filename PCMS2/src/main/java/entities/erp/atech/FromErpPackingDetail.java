package entities.erp.atech;

public class FromErpPackingDetail {
	private String productionOrder ; 
	private String postingDate ; 
	private String quantity ; 
	private String rollNo ; 
	private String quantityKG ; 
	private String grade ; 
	private String no ; 
	private String quantityYD ;
	private String dataStatus;
	private String syncDate ;
	public FromErpPackingDetail(String productionOrder, String postingDate, String quantity, String rollNo, String quantityKG,
			String grade, String no, String quantityYD, String dataStatus, String syncDate) {
		super();
		this.productionOrder = productionOrder;
		this.postingDate = postingDate;
		this.quantity = quantity;
		this.rollNo = rollNo;
		this.quantityKG = quantityKG;
		this.grade = grade;
		this.no = no;
		this.quantityYD = quantityYD;
		this.dataStatus = dataStatus;
		this.syncDate = syncDate;
	}
	public String getSyncDate()
	{
		return syncDate;
	}
	public void setSyncDate(String syncDate)
	{
		this.syncDate = syncDate;
	}
	public String getDataStatus()
	{
		return dataStatus;
	}
	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}
	public String getProductionOrder()
	{
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder)
	{
		this.productionOrder = productionOrder;
	}
	public String getPostingDate()
	{
		return postingDate;
	}
	public void setPostingDate(String postingDate)
	{
		this.postingDate = postingDate;
	}
	public String getQuantity()
	{
		return quantity;
	}
	public void setQuantity(String quantity)
	{
		this.quantity = quantity;
	}
	public String getRollNo()
	{
		return rollNo;
	}
	public void setRollNo(String rollNo)
	{
		this.rollNo = rollNo;
	}
	public String getQuantityKG()
	{
		return quantityKG;
	}
	public void setQuantityKG(String quantityKG)
	{
		this.quantityKG = quantityKG;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getNo()
	{
		return no;
	}
	public void setNo(String no)
	{
		this.no = no;
	}
	public String getQuantityYD()
	{
		return quantityYD;
	}
	public void setQuantityYD(String quantityYD)
	{
		this.quantityYD = quantityYD;
	} 

}
