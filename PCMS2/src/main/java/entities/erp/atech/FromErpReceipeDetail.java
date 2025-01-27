package entities.erp.atech;

public class FromErpReceipeDetail {
	private String productionOrder;
	private String lotNo;
	private String syncDate ;
	private String dataStatus ;
	public FromErpReceipeDetail(String productionOrder, String lotNo, String syncDate
			, String dataStatus) {
		super();
		this.productionOrder = productionOrder;
		this.lotNo = lotNo;
		this.syncDate = syncDate;
		this.dataStatus = dataStatus;
	}
	public String getDataStatus()
	{
		return dataStatus;
	}
	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}
	public String getSyncDate()
	{
		return syncDate;
	}
	public void setSyncDate(String syncDate)
	{
		this.syncDate = syncDate;
	}
	public String getProductionOrder()
	{
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder)
	{
		this.productionOrder = productionOrder;
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
