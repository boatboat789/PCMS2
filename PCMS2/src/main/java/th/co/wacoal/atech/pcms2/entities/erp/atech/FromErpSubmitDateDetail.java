package th.co.wacoal.atech.pcms2.entities.erp.atech;

public class FromErpSubmitDateDetail {
	private String productionOrder ; 
	private String saleOrder ; 
	private String saleLine ; 
	private String no ; 
	private String submitDate ; 
	private String remark ;
	private String dataStatus ;	private String syncDate ;
	public FromErpSubmitDateDetail(String productionOrder, String saleOrder, String saleLine, String no, String submitDate,
			String remark,String dataStatus, String syncDate) {
		super();
		this.dataStatus = dataStatus;
		this.productionOrder = productionOrder;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.no = no;
		this.submitDate = submitDate;
		this.remark = remark;		this.syncDate = syncDate;
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
	public String getNo()
	{
		return no;
	}
	public void setNo(String no)
	{
		this.no = no;
	}
	public String getSubmitDate()
	{
		return submitDate;
	}
	public void setSubmitDate(String submitDate)
	{
		this.submitDate = submitDate;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	} 

}
