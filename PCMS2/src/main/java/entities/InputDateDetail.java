package entities;

public class InputDateDetail {
	private String ProductionOrder;
	private String SaleOrder;
	private String SaleLine;
	private String PlanDate; 
	private String CreateBy;
	private String CreateDate;
	private String InputFrom;
	private String IconStatus;
	private String SystemStatus;

	public InputDateDetail(String productionOrder, String saleOrder, String saleLine, String planDate 
			 , String createBy ,String CreateDate,String InputFrom) {
		super();
		ProductionOrder = productionOrder;
		SaleOrder = saleOrder;
		SaleLine = saleLine;
		PlanDate = planDate; 
		CreateBy = createBy; 
		this.CreateDate = CreateDate;
		this.InputFrom = InputFrom;
	}

	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public InputDateDetail() {
		// TODO Auto-generated constructor stub
	}

	public String getInputFrom() {
		return InputFrom;
	}

	public void setInputFrom(String inputFrom) {
		InputFrom = inputFrom;
	}

	public String getProductionOrder() {
		return ProductionOrder;
	}

	public void setProductionOrder(String productionOrder) {
		ProductionOrder = productionOrder;
	}

	public String getSaleOrder() {
		return SaleOrder;
	}

	public void setSaleOrder(String saleOrder) {
		SaleOrder = saleOrder;
	}

	public String getSaleLine() {
		return SaleLine;
	}

	public void setSaleLine(String saleLine) {
		SaleLine = saleLine;
	}

	public String getPlanDate() {
		return PlanDate;
	}

	public void setPlanDate(String planDate) {
		PlanDate = planDate;
	}
   
	public String getCreateBy() {
		return CreateBy;
	}

	public void setCreateBy(String createBy) {
		CreateBy = createBy;
	}

	public String getIconStatus() {
		return IconStatus;
	}

	public void setIconStatus(String IconStatus) {
		this.IconStatus = IconStatus;
	}

	public String getSystemStatus() {
		return SystemStatus;
	}

	public void setSystemStatus(String SystemStatus) {
		this.SystemStatus = SystemStatus;
	}

}
