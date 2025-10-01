package th.co.wacoal.atech.pcms2.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FromSapMainProd") // กำหนดชื่อตาราง
public class SaleOrderLogDetail {
	@Id
	private Integer id;
	@Column(name = "SaleOrder")
	private String saleOrder;
	@Column(name = "SaleLine")
	private String saleLine;
	@Column(name = "Division")
	private String division;
	@Column(name = "MaterialNo")
	private String materialNo;
	@Column(name = "ArticleFG")
	private String articleFG;
	@Column(name = "DesignFG")
	private String designFG;
	@Column(name = "Color")
	private String color;
	@Column(name = "DistChannel")
	private String distChannel;
	@Column(name = "CustomerName")
	private String customerName;
	@Column(name = "CustomerShortName")
	private String customerShortName;
	@Column(name = "ColorCustomer")
	private String colorCustomer;
	@Column(name = "SaleCreateDate")
	private Date saleCreateDate;
	@Column(name = "PlanGreigeDate")
	private Date planGreigeDate;
	@Column(name = "DueDate")
	private Date dueDate;
	@Column(name = "CustomerDue")
	private String customerDue;
	@Column(name = "SaleQuantity")
	private BigDecimal saleQuantity;
	@Column(name = "SaleUnit")
	private String saleUnit;
	@Column(name = "OrderAmount")
	private BigDecimal orderAmount;
	@Column(name = "RemainQuantity")
	private BigDecimal remainQuantity;
	@Column(name = "RemainAmount")
	private BigDecimal remainAmount;
	@Column(name = "PurchaseOrder")
	private String purchaseOrder;
	@Column(name = "CustomerNo")
	private String customerNo;
	@Column(name = "CustomerMaterial")
	private String customerMaterial;
	@Column(name = "SaleOrg")
	private String saleOrg;
	@Column(name = "SaleStatus ")
	private String saleStatus;
	@Column(name = "SaleFullName")
	private String saleFullName;
	@Column(name = "DeliveryStatus")
	private String deliveryStatus;
	@Column(name = "SyncDate")
	private Timestamp syncDate;
	@Column(name = "SyncDateHeader")
	private Timestamp syncDateHeader;

	public SaleOrderLogDetail() {
		super();
	}

	public SaleOrderLogDetail(Integer id, String saleOrder, String saleLine, String division, String materialNo, String articleFG,
			String designFG, String color, String distChannel, String customerName, String customerShortName,
			String colorCustomer, Date saleCreateDate, Date planGreigeDate, Date dueDate, String customerDue,
			BigDecimal saleQuantity, String saleUnit, BigDecimal orderAmount, BigDecimal remainQuantity, BigDecimal remainAmount,
			String purchaseOrder, String customerNo, String customerMaterial, String saleOrg, String saleStatus,
			String saleFullName, String deliveryStatus, Timestamp syncDate, Timestamp syncDateHeader) {
		super();
		this.id = id;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.division = division;
		this.materialNo = materialNo;
		this.articleFG = articleFG;
		this.designFG = designFG;
		this.color = color;
		this.distChannel = distChannel;
		this.customerName = customerName;
		this.customerShortName = customerShortName;
		this.colorCustomer = colorCustomer;
		this.saleCreateDate = saleCreateDate;
		this.planGreigeDate = planGreigeDate;
		this.dueDate = dueDate;
		this.customerDue = customerDue;
		this.saleQuantity = saleQuantity;
		this.saleUnit = saleUnit;
		this.orderAmount = orderAmount;
		this.remainQuantity = remainQuantity;
		this.remainAmount = remainAmount;
		this.purchaseOrder = purchaseOrder;
		this.customerNo = customerNo;
		this.customerMaterial = customerMaterial;
		this.saleOrg = saleOrg;
		this.saleStatus = saleStatus;
		this.saleFullName = saleFullName;
		this.deliveryStatus = deliveryStatus;
		this.syncDate = syncDate;
		this.syncDateHeader = syncDateHeader;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
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

	public String getDivision()
	{
		return division;
	}

	public void setDivision(String division)
	{
		this.division = division;
	}

	public String getMaterialNo()
	{
		return materialNo;
	}

	public void setMaterialNo(String materialNo)
	{
		this.materialNo = materialNo;
	}

	public String getArticleFG()
	{
		return articleFG;
	}

	public void setArticleFG(String articleFG)
	{
		this.articleFG = articleFG;
	}

	public String getDesignFG()
	{
		return designFG;
	}

	public void setDesignFG(String designFG)
	{
		this.designFG = designFG;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	public String getDistChannel()
	{
		return distChannel;
	}

	public void setDistChannel(String distChannel)
	{
		this.distChannel = distChannel;
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

	public Date getSaleCreateDate()
	{
		return saleCreateDate;
	}

	public void setSaleCreateDate(Date saleCreateDate)
	{
		this.saleCreateDate = saleCreateDate;
	}

	public Date getPlanGreigeDate()
	{
		return planGreigeDate;
	}

	public void setPlanGreigeDate(Date planGreigeDate)
	{
		this.planGreigeDate = planGreigeDate;
	}

	public Date getDueDate()
	{
		return dueDate;
	}

	public void setDueDate(Date dueDate)
	{
		this.dueDate = dueDate;
	}

	public String getCustomerDue()
	{
		return customerDue;
	}

	public void setCustomerDue(String customerDue)
	{
		this.customerDue = customerDue;
	}

	public BigDecimal getSaleQuantity()
	{
		return saleQuantity;
	}

	public void setSaleQuantity(BigDecimal saleQuantity)
	{
		this.saleQuantity = saleQuantity;
	}

	public String getSaleUnit()
	{
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit)
	{
		this.saleUnit = saleUnit;
	}

	public BigDecimal getOrderAmount()
	{
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount)
	{
		this.orderAmount = orderAmount;
	}

	public BigDecimal getRemainQuantity()
	{
		return remainQuantity;
	}

	public void setRemainQuantity(BigDecimal remainQuantity)
	{
		this.remainQuantity = remainQuantity;
	}

	public BigDecimal getRemainAmount()
	{
		return remainAmount;
	}

	public void setRemainAmount(BigDecimal remainAmount)
	{
		this.remainAmount = remainAmount;
	}

	public String getPurchaseOrder()
	{
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder)
	{
		this.purchaseOrder = purchaseOrder;
	}

	public String getCustomerNo()
	{
		return customerNo;
	}

	public void setCustomerNo(String customerNo)
	{
		this.customerNo = customerNo;
	}

	public String getCustomerMaterial()
	{
		return customerMaterial;
	}

	public void setCustomerMaterial(String customerMaterial)
	{
		this.customerMaterial = customerMaterial;
	}

	public String getSaleOrg()
	{
		return saleOrg;
	}

	public void setSaleOrg(String saleOrg)
	{
		this.saleOrg = saleOrg;
	}

	public String getSaleStatus()
	{
		return saleStatus;
	}

	public void setSaleStatus(String saleStatus)
	{
		this.saleStatus = saleStatus;
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

	public Timestamp getSyncDate()
	{
		return syncDate;
	}

	public void setSyncDate(Timestamp syncDate)
	{
		this.syncDate = syncDate;
	}

	public Timestamp getSyncDateHeader()
	{
		return syncDateHeader;
	}

	public void setSyncDateHeader(Timestamp syncDateHeader)
	{
		this.syncDateHeader = syncDateHeader;
	}
}
