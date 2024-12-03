package entities.erp.atech;

public class FromErpPODetail {
	private String productionOrder ; 
	private String rollNo ; 
	private String quantityKG ; 
	private String quantityMR ; 
	private String poCreatedate ; 
	private String requiredDate ; 
	private String purchaseOrder ; 
	private String purchaseOrderLine ; 
	private String purchaseOrderDate ; 
	private String poDefault ; 
	private String poLineDefault ; 
	private String poPostingDateDefault ;
	private String dataStatus ; 
	public FromErpPODetail(String productionOrder, String rollNo, String quantityKG, String quantityMR, String poCreatedate,
			String requiredDate, String purchaseOrder, String purchaseOrderLine, String purchaseOrderDate, String poDefault,
			String poLineDefault, String poPostingDateDefault, String dataStatus) {
		super();
		this.productionOrder = productionOrder;
		this.rollNo = rollNo;
		this.quantityKG = quantityKG;
		this.quantityMR = quantityMR;
		this.poCreatedate = poCreatedate;
		this.requiredDate = requiredDate;
		this.purchaseOrder = purchaseOrder;
		this.purchaseOrderLine = purchaseOrderLine;
		this.purchaseOrderDate = purchaseOrderDate;
		this.poDefault = poDefault;
		this.poLineDefault = poLineDefault;
		this.poPostingDateDefault = poPostingDateDefault;
		this.dataStatus = dataStatus;
	}
	public String getPoCreatedate()
	{
		return poCreatedate;
	}
	public void setPoCreatedate(String poCreatedate)
	{
		this.poCreatedate = poCreatedate;
	}
	public String getPurchaseOrderDate()
	{
		return purchaseOrderDate;
	}
	public void setPurchaseOrderDate(String purchaseOrderDate)
	{
		this.purchaseOrderDate = purchaseOrderDate;
	}
	public String getProductionOrder()
	{
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder)
	{
		this.productionOrder = productionOrder;
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
	public String getQuantityMR()
	{
		return quantityMR;
	}
	public void setQuantityMR(String quantityMR)
	{
		this.quantityMR = quantityMR;
	}
	public String getpoCreatedate()
	{
		return poCreatedate;
	}
	public void setpOCreatedate(String pOCreatedate)
	{
		this.poCreatedate = pOCreatedate;
	}
	public String getRequiredDate()
	{
		return requiredDate;
	}
	public void setRequiredDate(String requiredDate)
	{
		this.requiredDate = requiredDate;
	}
	public String getPurchaseOrder()
	{
		return purchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder)
	{
		this.purchaseOrder = purchaseOrder;
	}
	public String getPurchaseOrderLine()
	{
		return purchaseOrderLine;
	}
	public void setPurchaseOrderLine(String purchaseOrderLine)
	{
		this.purchaseOrderLine = purchaseOrderLine;
	}
	public String getPoDefault()
	{
		return poDefault;
	}
	public void setPoDefault(String poDefault)
	{
		this.poDefault = poDefault;
	}
	public String getPoLineDefault()
	{
		return poLineDefault;
	}
	public void setPoLineDefault(String poLineDefault)
	{
		this.poLineDefault = poLineDefault;
	}
	public String getPoPostingDateDefault()
	{
		return poPostingDateDefault;
	}
	public void setPoPostingDateDefault(String poPostingDateDefault)
	{
		this.poPostingDateDefault = poPostingDateDefault;
	}
	public String getDataStatus()
	{
		return dataStatus;
	}
	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}
	
	 

}
