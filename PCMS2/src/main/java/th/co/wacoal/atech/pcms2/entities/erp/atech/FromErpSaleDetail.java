package th.co.wacoal.atech.pcms2.entities.erp.atech;

import javax.persistence.Entity;

@Entity
public class FromErpSaleDetail {
	private String productionOrder ; 
	private String billDate ; 
	private String billQtyPerSale ; 
	private String saleOrder ; 
	private String saleLine ; 
	private String billQtyPerStock ; 
	private String remark ; 
	private String customerNo ; 
	private String customerName1 ; 
	private String customErpO ; 
	private String dueDate ; 
	private String color ; 
	private String no ;

	private String dataStatus;	private String syncDate ;
	public FromErpSaleDetail(String productionOrder, String billDate, String billQtyPerSale, String saleOrder, String saleLine,
			String billQtyPerStock, String remark, String customerNo, String customerName1, String customErpO, String dueDate,
			String color, String no,String dataStatus, String syncDate ) {
		super();
		this.dataStatus = dataStatus;
		this.productionOrder = productionOrder;
		this.billDate = billDate;
		this.billQtyPerSale = billQtyPerSale;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.billQtyPerStock = billQtyPerStock;
		this.remark = remark;
		this.customerNo = customerNo;
		this.customerName1 = customerName1;
		this.customErpO = customErpO;
		this.dueDate = dueDate;
		this.color = color;
		this.no = no;
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

	public String getBillDate()
	{
		return billDate;
	}

	public void setBillDate(String billDate)
	{
		this.billDate = billDate;
	}

	public String getBillQtyPerSale()
	{
		return billQtyPerSale;
	}

	public void setBillQtyPerSale(String billQtyPerSale)
	{
		this.billQtyPerSale = billQtyPerSale;
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

	public String getBillQtyPerStock()
	{
		return billQtyPerStock;
	}

	public void setBillQtyPerStock(String billQtyPerStock)
	{
		this.billQtyPerStock = billQtyPerStock;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getCustomerNo()
	{
		return customerNo;
	}

	public void setCustomerNo(String customerNo)
	{
		this.customerNo = customerNo;
	}

	public String getCustomerName1()
	{
		return customerName1;
	}

	public void setCustomerName1(String customerName1)
	{
		this.customerName1 = customerName1;
	}

	public String getCustomErpO()
	{
		return customErpO;
	}

	public void setCustomErpO(String customErpO)
	{
		this.customErpO = customErpO;
	}

	public String getDueDate()
	{
		return dueDate;
	}

	public void setDueDate(String dueDate)
	{
		this.dueDate = dueDate;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	public String getNo()
	{
		return no;
	}

	public void setNo(String no)
	{
		this.no = no;
	} 

}
