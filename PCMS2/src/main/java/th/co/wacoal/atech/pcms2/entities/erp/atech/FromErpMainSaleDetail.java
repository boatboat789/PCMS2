package th.co.wacoal.atech.pcms2.entities.erp.atech;

import javax.persistence.Entity;

@Entity
public class FromErpMainSaleDetail {
	private String productionOrder ; 
	private String saleOrder ; 
	private String saleLine ; 
	private String materialNo ; 
	private String dueDate ; 
	private String planGreigeDate ; 
	private String saleUnit ; 
	private String saleQuantity ; 
	private String customerMaterial ; 
	private String color ; 
	private String customerNo ; 
	private String purchaseOrder ; 
	private String saleOrg ; 
	private String distChannel ; 
	private String division ; 
	private String customerName ; 
	private String customerShortName ; 
	private String colorCustomer ; 
	private String customerDue ; 
	private String remainQuantity ; 
	private String shipDate ; 
	private String saleStatus ; 
	private String currency ; 
	private String price ; 
	private String orderAmount ; 
	private String remainAmount ; 
	private String saleCreateDate ; 
	private String saleNumber ; 
	private String saleFullName ; 
	private String deliveryStatus ; 
	private String designFG ; 
	private String articleFG ; 
	private String orderSheetPrintDate ; 
	private String customerMaterialBase ;
	private String dataStatus ;
	private String syncDate ;
	public FromErpMainSaleDetail(String productionOrder,String saleOrder, String saleLine, String materialNo, String dueDate, String planGreigeDate,
			String saleUnit, String saleQuantity, String customerMaterial, String color, String customerNo, String purchaseOrder,
			String saleOrg, String distChannel, String division, String customerName, String customerShortName,
			String colorCustomer, String customerDue, String remainQuantity, String shipDate, String saleStatus, String currency,
			String price, String orderAmount, String remainAmount, String saleCreateDate, String saleNumber, String saleFullName,
			String deliveryStatus, String designFG, String articleFG, String orderSheetPrintDate, String customerMaterialBase,
			String dataStatus, String syncDate) {
		super();
		this.productionOrder = productionOrder;
		this.dataStatus = dataStatus;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.materialNo = materialNo;
		this.dueDate = dueDate;
		this.planGreigeDate = planGreigeDate;
		this.saleUnit = saleUnit;
		this.saleQuantity = saleQuantity;
		this.customerMaterial = customerMaterial;
		this.color = color;
		this.customerNo = customerNo;
		this.purchaseOrder = purchaseOrder;
		this.saleOrg = saleOrg;
		this.distChannel = distChannel;
		this.division = division;
		this.customerName = customerName;
		this.customerShortName = customerShortName;
		this.colorCustomer = colorCustomer;
		this.customerDue = customerDue;
		this.remainQuantity = remainQuantity;
		this.shipDate = shipDate;
		this.saleStatus = saleStatus;
		this.currency = currency;
		this.price = price;
		this.orderAmount = orderAmount;
		this.remainAmount = remainAmount;
		this.saleCreateDate = saleCreateDate;
		this.saleNumber = saleNumber;
		this.saleFullName = saleFullName;
		this.deliveryStatus = deliveryStatus;
		this.designFG = designFG;
		this.articleFG = articleFG;
		this.orderSheetPrintDate = orderSheetPrintDate;
		this.customerMaterialBase = customerMaterialBase;
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
	public String getMaterialNo()
	{
		return materialNo;
	}
	public void setMaterialNo(String materialNo)
	{
		this.materialNo = materialNo;
	}
	public String getDueDate()
	{
		return dueDate;
	}
	public void setDueDate(String dueDate)
	{
		this.dueDate = dueDate;
	}
	public String getPlanGreigeDate()
	{
		return planGreigeDate;
	}
	public void setPlanGreigeDate(String planGreigeDate)
	{
		this.planGreigeDate = planGreigeDate;
	}
	public String getSaleUnit()
	{
		return saleUnit;
	}
	public void setSaleUnit(String saleUnit)
	{
		this.saleUnit = saleUnit;
	}
	public String getSaleQuantity()
	{
		return saleQuantity;
	}
	public void setSaleQuantity(String saleQuantity)
	{
		this.saleQuantity = saleQuantity;
	}
	public String getCustomerMaterial()
	{
		return customerMaterial;
	}
	public void setCustomerMaterial(String customerMaterial)
	{
		this.customerMaterial = customerMaterial;
	}
	public String getColor()
	{
		return color;
	}
	public void setColor(String color)
	{
		this.color = color;
	}
	public String getCustomerNo()
	{
		return customerNo;
	}
	public void setCustomerNo(String customerNo)
	{
		this.customerNo = customerNo;
	}
	public String getPurchaseOrder()
	{
		return purchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder)
	{
		this.purchaseOrder = purchaseOrder;
	}
	public String getSaleOrg()
	{
		return saleOrg;
	}
	public void setSaleOrg(String saleOrg)
	{
		this.saleOrg = saleOrg;
	}
	public String getDistChannel()
	{
		return distChannel;
	}
	public void setDistChannel(String distChannel)
	{
		this.distChannel = distChannel;
	}
	public String getDivision()
	{
		return division;
	}
	public void setDivision(String division)
	{
		this.division = division;
	}
	public String getCustomerName()
	{
		return customerName;
	}
	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}
	public String getCustomerShortName()
	{
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName)
	{
		this.customerShortName = customerShortName;
	}
	public String getColorCustomer()
	{
		return colorCustomer;
	}
	public void setColorCustomer(String colorCustomer)
	{
		this.colorCustomer = colorCustomer;
	}
	public String getCustomerDue()
	{
		return customerDue;
	}
	public void setCustomerDue(String customerDue)
	{
		this.customerDue = customerDue;
	}
	public String getRemainQuantity()
	{
		return remainQuantity;
	}
	public void setRemainQuantity(String remainQuantity)
	{
		this.remainQuantity = remainQuantity;
	}
	public String getShipDate()
	{
		return shipDate;
	}
	public void setShipDate(String shipDate)
	{
		this.shipDate = shipDate;
	}
	public String getSaleStatus()
	{
		return saleStatus;
	}
	public void setSaleStatus(String saleStatus)
	{
		this.saleStatus = saleStatus;
	}
	public String getCurrency()
	{
		return currency;
	}
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}
	public String getPrice()
	{
		return price;
	}
	public void setPrice(String price)
	{
		this.price = price;
	}
	public String getOrderAmount()
	{
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount)
	{
		this.orderAmount = orderAmount;
	}
	public String getRemainAmount()
	{
		return remainAmount;
	}
	public void setRemainAmount(String remainAmount)
	{
		this.remainAmount = remainAmount;
	}
	public String getSaleCreateDate()
	{
		return saleCreateDate;
	}
	public void setSaleCreateDate(String saleCreateDate)
	{
		this.saleCreateDate = saleCreateDate;
	}
	public String getSaleNumber()
	{
		return saleNumber;
	}
	public void setSaleNumber(String saleNumber)
	{
		this.saleNumber = saleNumber;
	}
	public String getSaleFullName()
	{
		return saleFullName;
	}
	public void setSaleFullName(String saleFullName)
	{
		this.saleFullName = saleFullName;
	}
	public String getDeliveryStatus()
	{
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus)
	{
		this.deliveryStatus = deliveryStatus;
	}
	public String getDesignFG()
	{
		return designFG;
	}
	public void setDesignFG(String designFG)
	{
		this.designFG = designFG;
	}
	public String getArticleFG()
	{
		return articleFG;
	}
	public void setArticleFG(String articleFG)
	{
		this.articleFG = articleFG;
	}
	public String getOrderSheetPrintDate()
	{
		return orderSheetPrintDate;
	}
	public void setOrderSheetPrintDate(String orderSheetPrintDate)
	{
		this.orderSheetPrintDate = orderSheetPrintDate;
	}
	public String getCustomerMaterialBase()
	{
		return customerMaterialBase;
	}
	public void setCustomerMaterialBase(String customerMaterialBase)
	{
		this.customerMaterialBase = customerMaterialBase;
	} 

}
