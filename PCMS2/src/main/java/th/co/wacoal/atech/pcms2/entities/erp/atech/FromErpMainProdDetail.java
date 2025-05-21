package th.co.wacoal.atech.pcms2.entities.erp.atech;

import javax.persistence.Entity;

@Entity
public class FromErpMainProdDetail {
	private String productionOrder ; 
	private String saleOrder ; 
	private String saleLine ; 
	private String totalQuantity ; 
	private String unit ; 
	private String remAfterCloseOne ; 
	private String remAfterCloseTwo ; 
	private String remAfterCloseThree ; 
	private String labStatus ; 
	private String userStatus ; 
	private String designFG ; 
	private String articleFG ; 
	private String bookNo ; 
	private String center ; 
	private String lotNo ; 
	private String batch ; 
	private String labNo ; 
	private String remarkOne ; 
	private String remarkTwo ; 
	private String remarkThree ; 
	private String bcAware ; 
	private String orderPuang ; 
	private String refPrd ; 
	private String greigeInDate ; 
	private String bcDate ; 
	private String volumn ; 
	private String cfDate ; 
	private String cfType ; 
	private String shade ; 
	private String lotShipping ; 
	private String billSendQuantity ; 
	private String grade ; 
	private String dataStatus ; 
	private String prdCreateDate ; 
	private String greigeArticle ; 
	private String greigeDesign ; 
	private String greigeMR ; 
	private String greigeKG ; 
	private String orderType ;
	private String syncDate ;

	public FromErpMainProdDetail(String productionOrder, String saleOrder, String saleLine, String totalQuantity, String unit,
			String remAfterCloseOne, String remAfterCloseTwo, String remAfterCloseThree, String labStatus, String userStatus,
			String designFG, String articleFG, String bookNo, String center, String lotNo, String batch, String labNo,
			String remarkOne, String remarkTwo, String remarkThree, String bcAware, String orderPuang, String refPrd,
			String greigeInDate, String bcDate, String volumn, String cfDate, String cfType, String shade, String lotShipping,
			String billSendQuantity, String grade, String dataStatus, String prdCreateDate, String greigeArticle,
			String greigeDesign, String greigeMR, String greigeKG, String orderType, String syncDate) {
		super();
		this.syncDate = syncDate;
		this.productionOrder = productionOrder;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.totalQuantity = totalQuantity;
		this.unit = unit;
		this.remAfterCloseOne = remAfterCloseOne;
		this.remAfterCloseTwo = remAfterCloseTwo;
		this.remAfterCloseThree = remAfterCloseThree;
		this.labStatus = labStatus;
		this.userStatus = userStatus;
		this.designFG = designFG;
		this.articleFG = articleFG;
		this.bookNo = bookNo;
		this.center = center;
		this.lotNo = lotNo;
		this.batch = batch;
		this.labNo = labNo;
		this.remarkOne = remarkOne;
		this.remarkTwo = remarkTwo;
		this.remarkThree = remarkThree;
		this.bcAware = bcAware;
		this.orderPuang = orderPuang;
		this.refPrd = refPrd;
		this.greigeInDate = greigeInDate;
		this.bcDate = bcDate;
		this.volumn = volumn;
		this.cfDate = cfDate;
		this.cfType = cfType;
		this.shade = shade;
		this.lotShipping = lotShipping;
		this.billSendQuantity = billSendQuantity;
		this.grade = grade;
		this.dataStatus = dataStatus;
		this.prdCreateDate = prdCreateDate;
		this.greigeArticle = greigeArticle;
		this.greigeDesign = greigeDesign;
		this.greigeMR = greigeMR;
		this.greigeKG = greigeKG;
		this.orderType = orderType;
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
	public String getTotalQuantity()
	{
		return totalQuantity;
	}
	public void setTotalQuantity(String totalQuantity)
	{
		this.totalQuantity = totalQuantity;
	}
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	public String getRemAfterCloseOne()
	{
		return remAfterCloseOne;
	}
	public void setRemAfterCloseOne(String remAfterCloseOne)
	{
		this.remAfterCloseOne = remAfterCloseOne;
	}
	public String getRemAfterCloseTwo()
	{
		return remAfterCloseTwo;
	}
	public void setRemAfterCloseTwo(String remAfterCloseTwo)
	{
		this.remAfterCloseTwo = remAfterCloseTwo;
	}
	public String getRemAfterCloseThree()
	{
		return remAfterCloseThree;
	}
	public void setRemAfterCloseThree(String remAfterCloseThree)
	{
		this.remAfterCloseThree = remAfterCloseThree;
	}
	public String getLabStatus()
	{
		return labStatus;
	}
	public void setLabStatus(String labStatus)
	{
		this.labStatus = labStatus;
	}
	public String getUserStatus()
	{
		return userStatus;
	}
	public void setUserStatus(String userStatus)
	{
		this.userStatus = userStatus;
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
	public String getBatch()
	{
		return batch;
	}
	public void setBatch(String batch)
	{
		this.batch = batch;
	}
	public String getLabNo()
	{
		return labNo;
	}
	public void setLabNo(String labNo)
	{
		this.labNo = labNo;
	}
	public String getRemarkOne()
	{
		return remarkOne;
	}
	public void setRemarkOne(String remarkOne)
	{
		this.remarkOne = remarkOne;
	}
	public String getRemarkTwo()
	{
		return remarkTwo;
	}
	public void setRemarkTwo(String remarkTwo)
	{
		this.remarkTwo = remarkTwo;
	}
	public String getRemarkThree()
	{
		return remarkThree;
	}
	public void setRemarkThree(String remarkThree)
	{
		this.remarkThree = remarkThree;
	}
	public String getBcAware()
	{
		return bcAware;
	}
	public void setBcAware(String bcAware)
	{
		this.bcAware = bcAware;
	}
	public String getOrderPuang()
	{
		return orderPuang;
	}
	public void setOrderPuang(String orderPuang)
	{
		this.orderPuang = orderPuang;
	}
	public String getRefPrd()
	{
		return refPrd;
	}
	public void setRefPrd(String refPrd)
	{
		this.refPrd = refPrd;
	}
	public String getGreigeInDate()
	{
		return greigeInDate;
	}
	public void setGreigeInDate(String greigeInDate)
	{
		this.greigeInDate = greigeInDate;
	}
	public String getBcDate()
	{
		return bcDate;
	}
	public void setBcDate(String bcDate)
	{
		this.bcDate = bcDate;
	}
	public String getVolumn()
	{
		return volumn;
	}
	public void setVolumn(String volumn)
	{
		this.volumn = volumn;
	}
	public String getCfDate()
	{
		return cfDate;
	}
	public void setCfDate(String cfDate)
	{
		this.cfDate = cfDate;
	}
	public String getCfType()
	{
		return cfType;
	}
	public void setCfType(String cfType)
	{
		this.cfType = cfType;
	}
	public String getShade()
	{
		return shade;
	}
	public void setShade(String shade)
	{
		this.shade = shade;
	}
	public String getLotShipping()
	{
		return lotShipping;
	}
	public void setLotShipping(String lotShipping)
	{
		this.lotShipping = lotShipping;
	}
	public String getBillSendQuantity()
	{
		return billSendQuantity;
	}
	public void setBillSendQuantity(String billSendQuantity)
	{
		this.billSendQuantity = billSendQuantity;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getDataStatus()
	{
		return dataStatus;
	}
	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}
	public String getPrdCreateDate()
	{
		return prdCreateDate;
	}
	public void setPrdCreateDate(String prdCreateDate)
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
	public String getGreigeMR()
	{
		return greigeMR;
	}
	public void setGreigeMR(String greigeMR)
	{
		this.greigeMR = greigeMR;
	}
	public String getGreigeKG()
	{
		return greigeKG;
	}
	public void setGreigeKG(String greigeKG)
	{
		this.greigeKG = greigeKG;
	}
	public String getOrderType()
	{
		return orderType;
	}
	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	} 
	

}
