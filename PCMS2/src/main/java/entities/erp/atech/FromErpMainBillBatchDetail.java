package entities.erp.atech;

public class FromErpMainBillBatchDetail {
	private String billDoc ; 
	private String billItem ; 
	private String lotShipping ; 
	private String productionOrder ; 
	private String saleOrder ; 
	private String saleLine ; 
	private String grade ; 
	private String rollNumber ; 
	private String quantityKG ; 
	private String quantityYD ; 
	private String quantityMR ; 
	private String lotNo ;
	private String dataStatus ;
	public FromErpMainBillBatchDetail(String billDoc, String billItem, String lotShipping, String productionOrder,
			String saleOrder, String saleLine, String grade, String rollNumber, String quantityKG, String quantityYD,
			String quantityMR, String lotNo,String dataStatus) {
		super();
		this.dataStatus = dataStatus;
		this.billDoc = billDoc;
		this.billItem = billItem;
		this.lotShipping = lotShipping;
		this.productionOrder = productionOrder;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.grade = grade;
		this.rollNumber = rollNumber;
		this.quantityKG = quantityKG;
		this.quantityYD = quantityYD;
		this.quantityMR = quantityMR;
		this.lotNo = lotNo;
	}
	public String getDataStatus()
	{
		return dataStatus;
	}
	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}
	public String getBillDoc()
	{
		return billDoc;
	}
	public void setBillDoc(String billDoc)
	{
		this.billDoc = billDoc;
	}
	public String getBillItem()
	{
		return billItem;
	}
	public void setBillItem(String billItem)
	{
		this.billItem = billItem;
	}
	public String getLotShipping()
	{
		return lotShipping;
	}
	public void setLotShipping(String lotShipping)
	{
		this.lotShipping = lotShipping;
	}
	public String getProductionOrder()
	{
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder)
	{
		this.productionOrder = productionOrder;
	}
	public String getSaleOrder()
	{
		return saleOrder;
	}
	public void setSaleOrder(String saleOrder)
	{
		this.saleOrder = saleOrder;
	}
	public String getSaleLine()
	{
		return saleLine;
	}
	public void setSaleLine(String saleLine)
	{
		this.saleLine = saleLine;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getRollNumber()
	{
		return rollNumber;
	}
	public void setRollNumber(String rollNumber)
	{
		this.rollNumber = rollNumber;
	}
	public String getQuantityKG()
	{
		return quantityKG;
	}
	public void setQuantityKG(String quantityKG)
	{
		this.quantityKG = quantityKG;
	}
	public String getQuantityYD()
	{
		return quantityYD;
	}
	public void setQuantityYD(String quantityYD)
	{
		this.quantityYD = quantityYD;
	}
	public String getQuantityMR()
	{
		return quantityMR;
	}
	public void setQuantityMR(String quantityMR)
	{
		this.quantityMR = quantityMR;
	}
	public String getLotNo()
	{
		return lotNo;
	}
	public void setLotNo(String lotNo)
	{
		this.lotNo = lotNo;
	} 

}
