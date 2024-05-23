package entities;

public class InputDateDetail {
	private String productionOrder;
	private String saleOrder;
	private String saleLine;
	private String planDate;
	private String createBy;
	private String createDate;
	private String inputFrom;
	private String lotNo;
	private String iconStatus;
	private String systemStatus;
	private int countPlanDate;
	public InputDateDetail(String productionOrder, String saleOrder, String saleLine, String planDate
			 , String createBy ,String CreateDate,String InputFrom,int CountPlanDate,String LotNo) {
		super();
		this.productionOrder = productionOrder;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.planDate = planDate;
		this.createBy = createBy;
		this.createDate = CreateDate;
		this.inputFrom = InputFrom;
		this.countPlanDate = CountPlanDate;
		this.lotNo = LotNo;
	}
	public InputDateDetail() {
		// TODO Auto-generated constructor stub
	}
	public String getProductionOrder() {
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder) {
		this.productionOrder = productionOrder;
	}
	public String getSaleOrder() {
		return saleOrder;
	}
	public void setSaleOrder(String saleOrder) {
		this.saleOrder = saleOrder;
	}
	public String getSaleLine() {
		return saleLine;
	}
	public void setSaleLine(String saleLine) {
		this.saleLine = saleLine;
	}
	public String getPlanDate() {
		return planDate;
	}
	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getInputFrom() {
		return inputFrom;
	}
	public void setInputFrom(String inputFrom) {
		this.inputFrom = inputFrom;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getIconStatus() {
		return iconStatus;
	}
	public void setIconStatus(String iconStatus) {
		this.iconStatus = iconStatus;
	}
	public String getSystemStatus() {
		return systemStatus;
	}
	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
	}
	public int getCountPlanDate() {
		return countPlanDate;
	}
	public void setCountPlanDate(int countPlanDate) {
		this.countPlanDate = countPlanDate;
	}


}
