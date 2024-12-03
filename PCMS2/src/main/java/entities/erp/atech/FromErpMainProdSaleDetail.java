package entities.erp.atech;

public class FromErpMainProdSaleDetail {
	private String productionOrder ; 
	private String saleOrder ; 
	private String saleLine ; 
	private String volumn ;
	private String dataStatus ;
	
	public FromErpMainProdSaleDetail(String productionOrder, String saleOrder, String saleLine, String volumn,
			String dataStatus) {
		super();
		this.dataStatus = dataStatus;
		this.productionOrder = productionOrder;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.volumn = volumn;
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
	public String getVolumn()
	{
		return volumn;
	}
	public void setVolumn(String volumn)
	{
		this.volumn = volumn;
	} 

}
