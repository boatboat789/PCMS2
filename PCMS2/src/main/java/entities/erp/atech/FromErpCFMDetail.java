package entities.erp.atech;

public class FromErpCFMDetail {
	private String id ; 
	private String productionOrder ; 
	private String cfmNo ; 
	private String cfmNumber ; 
	private String cfmSendDate ; 
	private String cfmAnswerDate ;  
	private String cfmStatus ; 
	private String cfmRemark ; 
	

	private String saleOrder ; 
	private String saleLine ; 
	private String nextLot ; 
	private String soChange ; 
	private String soChangeQty ; 
	private String soChangeUnit ; 
	private String rollNo ;  
	private String rollNoRemark ;
	private String dataStatus ;

	private String syncDate ;
 
	public FromErpCFMDetail(String id, String productionOrder, String cfmNo, String cfmNumber, String cfmSendDate,
			String cfmAnswerDate, String cfmStatus, String cfmRemark, String saleOrder, String saleLine, String nextLot,
			String soChange, String soChangeQty, String soChangeUnit, String rollNo, String rollNoRemark, String dataStatus, String syncDate ) {
		super();
		this.id = id;
		this.productionOrder = productionOrder;
		this.cfmNo = cfmNo;
		this.cfmNumber = cfmNumber;
		this.cfmSendDate = cfmSendDate;
		this.cfmAnswerDate = cfmAnswerDate;
		this.cfmStatus = cfmStatus;
		this.cfmRemark = cfmRemark;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.nextLot = nextLot;
		this.soChange = soChange;
		this.soChangeQty = soChangeQty;
		this.soChangeUnit = soChangeUnit;
		this.rollNo = rollNo;
		this.rollNoRemark = rollNoRemark;
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
	public String getNextLot()
	{
		return nextLot;
	}
	public void setNextLot(String nextLot)
	{
		this.nextLot = nextLot;
	}
	public String getSoChange()
	{
		return soChange;
	}
	public void setSoChange(String soChange)
	{
		this.soChange = soChange;
	}
	public String getSoChangeQty()
	{
		return soChangeQty;
	}
	public void setSoChangeQty(String soChangeQty)
	{
		this.soChangeQty = soChangeQty;
	}
	public String getSoChangeUnit()
	{
		return soChangeUnit;
	}
	public void setSoChangeUnit(String soChangeUnit)
	{
		this.soChangeUnit = soChangeUnit;
	}
	public String getRollNo()
	{
		return rollNo;
	}
	public void setRollNo(String rollNo)
	{
		this.rollNo = rollNo;
	}
	public String getDataStatus()
	{
		return dataStatus;
	}
	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getProductionOrder()
	{
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder)
	{
		this.productionOrder = productionOrder;
	}
	public String getCfmNo()
	{
		return cfmNo;
	}
	public void setCfmNo(String cfmNo)
	{
		this.cfmNo = cfmNo;
	}
	public String getCfmNumber()
	{
		return cfmNumber;
	}
	public void setCfmNumber(String cfmNumber)
	{
		this.cfmNumber = cfmNumber;
	}
	public String getCfmSendDate()
	{
		return cfmSendDate;
	}
	public void setCfmSendDate(String cfmSendDate)
	{
		this.cfmSendDate = cfmSendDate;
	}
	public String getCfmAnswerDate()
	{
		return cfmAnswerDate;
	}
	public void setCfmAnswerDate(String cfmAnswerDate)
	{
		this.cfmAnswerDate = cfmAnswerDate;
	}
	public String getCfmStatus()
	{
		return cfmStatus;
	}
	public void setCfmStatus(String cfmStatus)
	{
		this.cfmStatus = cfmStatus;
	}
	public String getCfmRemark()
	{
		return cfmRemark;
	}
	public void setCfmRemark(String cfmRemark)
	{
		this.cfmRemark = cfmRemark;
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
	public String getRollNoRemark()
	{
		return rollNoRemark;
	}
	public void setRollNoRemark(String rollNoRemark)
	{
		this.rollNoRemark = rollNoRemark;
	} 

}
