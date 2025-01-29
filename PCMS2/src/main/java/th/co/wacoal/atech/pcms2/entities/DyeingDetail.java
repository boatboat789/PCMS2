package th.co.wacoal.atech.pcms2.entities;

public class DyeingDetail {
	private String productionOrder;
	private String lotNo;
	private String operation;
	private String cartNo;
	private String cartType;
	private String workDate;
	private String workCenter; 
	private String dyeingStatus;
	private String dyeRemark;
	private String deltaE;
	private String l;
	private String da;
	private String db;
	private String st	;
	private String colorCheckStatus;
	private String colorCheckRemark;
	public DyeingDetail(String productionOrder, String lotNo, String operation, String cartNo, String cartType, String workDate,
			String workCenter, String dyeingStatus, String deltaE, String l, String da, String db, String st, String dyeRemark,
			String colorCheckStatus, String colorCheckRemark) {
		super();
		this.productionOrder = productionOrder;
		this.lotNo = lotNo;
		this.operation = operation;
		this.cartNo = cartNo;
		this.cartType = cartType;
		this.workDate = workDate;
		this.workCenter = workCenter;
		this.dyeingStatus = dyeingStatus;
		this.deltaE = deltaE;
		this.l = l;
		this.da = da;
		this.db = db;
		this.st = st;
		this.dyeRemark = dyeRemark;
		this.colorCheckStatus = colorCheckStatus;
		this.colorCheckRemark = colorCheckRemark;
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
	public String getOperation()
	{
		return operation;
	}
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	public String getCartNo()
	{
		return cartNo;
	}
	public void setCartNo(String cartNo)
	{
		this.cartNo = cartNo;
	}
	public String getCartType()
	{
		return cartType;
	}
	public void setCartType(String cartType)
	{
		this.cartType = cartType;
	}
	public String getWorkDate()
	{
		return workDate;
	}
	public void setWorkDate(String workDate)
	{
		this.workDate = workDate;
	}
	public String getWorkCenter()
	{
		return workCenter;
	}
	public void setWorkCenter(String workCenter)
	{
		this.workCenter = workCenter;
	}
	public String getDyeingStatus()
	{
		return dyeingStatus;
	}
	public void setDyeingStatus(String dyeingStatus)
	{
		this.dyeingStatus = dyeingStatus;
	}
	public String getDeltaE()
	{
		return deltaE;
	}
	public void setDeltaE(String deltaE)
	{
		this.deltaE = deltaE;
	}
	public String getL()
	{
		return l;
	}
	public void setL(String l)
	{
		this.l = l;
	}
	public String getDa()
	{
		return da;
	}
	public void setDa(String da)
	{
		this.da = da;
	}
	public String getDb()
	{
		return db;
	}
	public void setDb(String db)
	{
		this.db = db;
	}
	public String getSt()
	{
		return st;
	}
	public void setSt(String st)
	{
		this.st = st;
	}
	public String getDyeRemark()
	{
		return dyeRemark;
	}
	public void setDyeRemark(String dyeRemark)
	{
		this.dyeRemark = dyeRemark;
	}
	public String getColorCheckStatus()
	{
		return colorCheckStatus;
	}
	public void setColorCheckStatus(String colorCheckStatus)
	{
		this.colorCheckStatus = colorCheckStatus;
	}
	public String getColorCheckRemark()
	{
		return colorCheckRemark;
	}
	public void setColorCheckRemark(String colorCheckRemark)
	{
		this.colorCheckRemark = colorCheckRemark;
	}  
}
