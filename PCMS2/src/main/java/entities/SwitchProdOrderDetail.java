package entities;

public class SwitchProdOrderDetail {
	private String saleOrder;
	private String saleLine;
	private String productionOrder;
	private String saleOrderSW;
	private String saleLineSW;
	private String productionOrderSW;
	private String changeBy;
	private String changeDate;
	private String typePrd;
	private String switchRemark;
	private String iconStatus;
	private String systemStatus;
	public SwitchProdOrderDetail() {
		super();
	}
	public SwitchProdOrderDetail(String saleOrder, String saleLine, String productionOrder, String saleOrderSW,
			String saleLineSW, String productionOrderSW, String changeBy, String changeDate) {
		super();
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.productionOrder = productionOrder;
		this.saleOrderSW = saleOrderSW;
		this.saleLineSW = saleLineSW;
		this.productionOrderSW = productionOrderSW;
		this.changeBy = changeBy;
		this.changeDate = changeDate;
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
	public String getProductionOrder() {
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder) {
		this.productionOrder = productionOrder;
	}
	public String getSaleOrderSW() {
		return saleOrderSW;
	}
	public void setSaleOrderSW(String saleOrderSW) {
		this.saleOrderSW = saleOrderSW;
	}
	public String getSaleLineSW() {
		return saleLineSW;
	}
	public void setSaleLineSW(String saleLineSW) {
		this.saleLineSW = saleLineSW;
	}
	public String getProductionOrderSW() {
		return productionOrderSW;
	}
	public void setProductionOrderSW(String productionOrderSW) {
		this.productionOrderSW = productionOrderSW;
	}
	public String getChangeBy() {
		return changeBy;
	}
	public void setChangeBy(String changeBy) {
		this.changeBy = changeBy;
	}
	public String getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}
	public String getTypePrd() {
		return typePrd;
	}
	public void setTypePrd(String typePrd) {
		this.typePrd = typePrd;
	}
	public String getSwitchRemark() {
		return switchRemark;
	}
	public void setSwitchRemark(String switchRemark) {
		this.switchRemark = switchRemark;
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

}
