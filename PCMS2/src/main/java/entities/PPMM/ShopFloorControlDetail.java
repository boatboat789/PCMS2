package entities.PPMM;

public class ShopFloorControlDetail {
	private String productionOrder;
	private String lotNo;
	private String operation;
	private String workDate;
	private String workCenter; 
	private String cartNo;
	private String cartType;
	private String colorCheckOperation;
	private String colorCheckWorkDate;
	private String da;
	private String db;
	private String l;
	private String st;
	private String deltaE;
	private String colorCheckName;
	private String colorCheckStatus;
	private String colorCheckRollNo;
	private String colorCheckRemark;
	private String dyeingStatus;
	private String dyeRemark;
	private String machineInspect;
	private String inspectRemark;
	 
	public ShopFloorControlDetail(String productionOrder, String lotNo, String operation, String workDate, String workCenter,
			String cartNo, String cartType, String colorCheckOperation, String colorCheckWorkDate, String da, String db, String l,
			String st, String deltaE, String colorCheckName, String colorCheckStatus, String colorCheckRollNo,
			String colorCheckRemark, String dyeingStatus, String dyeRemark,String inspectRemark,String machineInspect) {
		super();
		this.machineInspect = machineInspect;
		this.productionOrder = productionOrder;
		this.lotNo = lotNo;
		this.operation = operation;
		this.workDate = workDate;
		this.workCenter = workCenter;
		this.cartNo = cartNo;
		this.cartType = cartType;
		this.colorCheckOperation = colorCheckOperation;
		this.colorCheckWorkDate = colorCheckWorkDate;
		this.da = da;
		this.db = db;
		this.l = l;
		this.st = st;
		this.deltaE = deltaE;
		this.colorCheckName = colorCheckName;
		this.colorCheckStatus = colorCheckStatus;
		this.colorCheckRollNo = colorCheckRollNo;
		this.colorCheckRemark = colorCheckRemark;
		this.dyeingStatus = dyeingStatus;
		this.dyeRemark = dyeRemark;
		this.inspectRemark = inspectRemark;
	}
	public String getMachineInspect()
	{
		return machineInspect;
	}
	public void setMachineInspect(String machineInspect)
	{
		this.machineInspect = machineInspect;
	}
	public String getInspectRemark()
	{
		return inspectRemark;
	}
	public void setInspectRemark(String inspectRemark)
	{
		this.inspectRemark = inspectRemark;
	}
	public String getDyeingStatus()
	{
		return dyeingStatus;
	}
	public void setDyeingStatus(String dyeingStatus)
	{
		this.dyeingStatus = dyeingStatus;
	}
	public String getDyeRemark()
	{
		return dyeRemark;
	}
	public void setDyeRemark(String dyeRemark)
	{
		this.dyeRemark = dyeRemark;
	}
	public String getWorkCenter()
	{
		return workCenter;
	}
	public void setWorkCenter(String workCenter)
	{
		this.workCenter = workCenter;
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
	public String getWorkDate()
	{
		return workDate;
	}
	public void setWorkDate(String workDate)
	{
		this.workDate = workDate;
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
	public String getColorCheckOperation()
	{
		return colorCheckOperation;
	}
	public void setColorCheckOperation(String colorCheckOperation)
	{
		this.colorCheckOperation = colorCheckOperation;
	}
	public String getColorCheckWorkDate()
	{
		return colorCheckWorkDate;
	}
	public void setColorCheckWorkDate(String colorCheckWorkDate)
	{
		this.colorCheckWorkDate = colorCheckWorkDate;
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
	public String getL()
	{
		return l;
	}
	public void setL(String l)
	{
		this.l = l;
	}
	public String getSt()
	{
		return st;
	}
	public void setSt(String st)
	{
		this.st = st;
	}
	public String getDeltaE()
	{
		return deltaE;
	}
	public void setDeltaE(String deltaE)
	{
		this.deltaE = deltaE;
	}
	public String getColorCheckName()
	{
		return colorCheckName;
	}
	public void setColorCheckName(String colorCheckName)
	{
		this.colorCheckName = colorCheckName;
	}
	public String getColorCheckStatus()
	{
		return colorCheckStatus;
	}
	public void setColorCheckStatus(String colorCheckStatus)
	{
		this.colorCheckStatus = colorCheckStatus;
	}
	public String getColorCheckRollNo()
	{
		return colorCheckRollNo;
	}
	public void setColorCheckRollNo(String colorCheckRollNo)
	{
		this.colorCheckRollNo = colorCheckRollNo;
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
