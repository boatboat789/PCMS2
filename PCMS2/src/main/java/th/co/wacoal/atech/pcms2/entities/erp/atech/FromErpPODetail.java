package th.co.wacoal.atech.pcms2.entities.erp.atech;

import javax.persistence.Entity;

@Entity
public class FromErpPODetail {
	private String productionOrder;
	private String rollNumber; 
	private String rollWeight;
	private String rollLength;
	private String poCreatedate;
	private String requiredDate;
	private String purchaseOrder;
	private String purchaseOrderLine;
	private String purchaseOrderDate;
	private String poDefault;
	private String poLineDefault;
	private String poPostingDateDefault;
	private String dataStatus;
	private String syncDate;

	public FromErpPODetail(String productionOrder, String rollNumber, String rollWeight, String rollLength, String poCreatedate,
			String requiredDate, String purchaseOrder, String purchaseOrderLine, String purchaseOrderDate, String poDefault,
			String poLineDefault, String poPostingDateDefault, String dataStatus, String syncDate) {
		super();
		this.productionOrder = productionOrder;
		this.rollNumber = rollNumber;
		this.rollWeight = rollWeight;
		this.rollLength = rollLength;
		this.poCreatedate = poCreatedate;
		this.requiredDate = requiredDate;
		this.purchaseOrder = purchaseOrder;
		this.purchaseOrderLine = purchaseOrderLine;
		this.purchaseOrderDate = purchaseOrderDate;
		this.poDefault = poDefault;
		this.poLineDefault = poLineDefault;
		this.poPostingDateDefault = poPostingDateDefault;
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
