package entities.erp.atech;

public class FromErpReceipeDetail {
	private String productionOrder;
	private String lotNo;
	public FromErpReceipeDetail(String productionOrder, String lotNo) {
		super();
		this.productionOrder = productionOrder;
		this.lotNo = lotNo;
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
