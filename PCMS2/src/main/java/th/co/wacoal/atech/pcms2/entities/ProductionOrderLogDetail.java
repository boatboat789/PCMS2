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
public class ProductionOrderLogDetail {  
	@Id
	private Integer id ; 
	@Column(name = "ProductionOrder")private String productionOrder ; 
	@Column(name = "SaleOrder")private String saleOrder ; 
	@Column(name = "SaleLine")private String saleLine ; 
	@Column(name = "OrderType")private String orderType ; 
	@Column(name = "GreigeInDate")private Date greigeInDate ; 
	@Column(name = "PrdCreateDate")private Date prdCreateDate ; 
	@Column(name = "GreigeArticle")private String greigeArticle ; 
	@Column(name = "GreigeDesign")private String greigeDesign ; 
	@Column(name = "ArticleFG")private String articleFG ; 
	@Column(name = "DesignFG")private String designFG ; 
	@Column(name = "TotalQuantity")private BigDecimal totalQuantity ; 
	@Column(name = "Volumn")private BigDecimal volumn ; 
	@Column(name = "Unit")private String unit ; 
	@Column(name = "UserStatus")private String userStatus ; 
	@Column(name = "LabStatus")private String labStatus ; 
	@Column(name = "BookNo")private String bookNo ; 
	@Column(name = "Center")private String center ; 
	@Column(name = "LotNo")private String lotNo ; 
	@Column(name = "LabNo")private String labNo ; 
	@Column(name = "Shade")private String shade ; 
	@Column(name = "ChangeDate")private Timestamp changeDate ; 
	@Column(name = "SyncDate")private Timestamp syncDate ; 

	public ProductionOrderLogDetail() {
		super();
	}
	public ProductionOrderLogDetail(Integer id ,String productionOrder, String saleOrder, String saleLine, String orderType,
			Date greigeInDate, Date prdCreateDate, String greigeArticle, String greigeDesign, String articleFG, String designFG,
			BigDecimal totalQuantity, BigDecimal volumn, String unit, String userStatus, String labStatus, String bookNo,
			String center, String lotNo, String labNo, String shade, Timestamp changeDate, Timestamp syncDate) {
		super();
		this.id = id;
		this.productionOrder = productionOrder;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.orderType = orderType;
		this.greigeInDate = greigeInDate;
		this.prdCreateDate = prdCreateDate;
		this.greigeArticle = greigeArticle;
		this.greigeDesign = greigeDesign;
		this.articleFG = articleFG;
		this.designFG = designFG;
		this.totalQuantity = totalQuantity;
		this.volumn = volumn;
		this.unit = unit;
		this.userStatus = userStatus;
		this.labStatus = labStatus;
		this.bookNo = bookNo;
		this.center = center;
		this.lotNo = lotNo;
		this.labNo = labNo;
		this.shade = shade;
		this.changeDate = changeDate;
		this.syncDate = syncDate;
	}
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
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
	public String getOrderType()
	{
		return orderType;
	}
	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}
	public Date getGreigeInDate()
	{
		return greigeInDate;
	}
	public void setGreigeInDate(Date greigeInDate)
	{
		this.greigeInDate = greigeInDate;
	}
	public Date getPrdCreateDate()
	{
		return prdCreateDate;
	}
	public void setPrdCreateDate(Date prdCreateDate)
	{
		this.prdCreateDate = prdCreateDate;
	}
	public String getGreigeArticle()
	{
		return greigeArticle;
	}
	public void setGreigeArticle(String greigeArticle)
	{
		this.greigeArticle = greigeArticle;
	}
	public String getGreigeDesign()
	{
		return greigeDesign;
	}
	public void setGreigeDesign(String greigeDesign)
	{
		this.greigeDesign = greigeDesign;
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
	public BigDecimal getTotalQuantity()
	{
		return totalQuantity;
	}
	public void setTotalQuantity(BigDecimal totalQuantity)
	{
		this.totalQuantity = totalQuantity;
	}
	public BigDecimal getVolumn()
	{
		return volumn;
	}
	public void setVolumn(BigDecimal volumn)
	{
		this.volumn = volumn;
	}
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	public String getUserStatus()
	{
		return userStatus;
	}
	public void setUserStatus(String userStatus)
	{
		this.userStatus = userStatus;
	}
	public String getLabStatus()
	{
		return labStatus;
	}
	public void setLabStatus(String labStatus)
	{
		this.labStatus = labStatus;
	}
	public String getBookNo()
	{
		return bookNo;
	}
	public void setBookNo(String bookNo)
	{
		this.bookNo = bookNo;
	}
	public String getCenter()
	{
		return center;
	}
	public void setCenter(String center)
	{
		this.center = center;
	}
	public String getLotNo()
	{
		return lotNo;
	}
	public void setLotNo(String lotNo)
	{
		this.lotNo = lotNo;
	}
	public String getLabNo()
	{
		return labNo;
	}
	public void setLabNo(String labNo)
	{
		this.labNo = labNo;
	}
	public String getShade()
	{
		return shade;
	}
	public void setShade(String shade)
	{
		this.shade = shade;
	}
	public Timestamp getChangeDate()
	{
		return changeDate;
	}
	public void setChangeDate(Timestamp changeDate)
	{
		this.changeDate = changeDate;
	}
	public Timestamp getSyncDate()
	{
		return syncDate;
	}
	public void setSyncDate(Timestamp syncDate)
	{
		this.syncDate = syncDate;
	}
	  

}
