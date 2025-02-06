package th.co.wacoal.atech.pcms2.entities.erp.atech;

import javax.persistence.Entity;

@Entity
public class FromErpGoodReceiveDetail {
	private String productionOrder ; 
	private String saleOrder ; 
	private String saleLine ; 
	private String grade ; 
	private String rollNumber ; 
	private String quantityKG ; 
	private String quantityYD ; 
	private String quantityMR ; 
	private String priceSTD ;
	private String dataStatus; ;
	private String syncDate ;
	
	public FromErpGoodReceiveDetail(String productionOrder, String saleOrder, String saleLine, String grade, String rollNumber,
			String quantityKG, String quantityYD, String quantityMR, String priceSTD,String dataStatus, String syncDate) {
		super();
		this.dataStatus = dataStatus;
		this.productionOrder = productionOrder;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.grade = grade;
		this.rollNumber = rollNumber;
		this.quantityKG = quantityKG;
		this.quantityYD = quantityYD;
		this.quantityMR = quantityMR;
		this.priceSTD = priceSTD;
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
	public String getPriceSTD()
	{
		return priceSTD;
	}
	public void setPriceSTD(String priceSTD)
	{
		this.priceSTD = priceSTD;
	} 

}
