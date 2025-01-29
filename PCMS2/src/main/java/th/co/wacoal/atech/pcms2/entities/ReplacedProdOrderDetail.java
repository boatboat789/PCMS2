package th.co.wacoal.atech.pcms2.entities;

public class ReplacedProdOrderDetail {

	private String saleOrder;
	private String saleLine;
	private String productionOrder;
	private String productionOrderRP;
	private String volume;
	private String changeBy;
	private String changeDate;

	private String iconStatus;
	private String systemStatus;
	public ReplacedProdOrderDetail() {
		super();
	}
	public ReplacedProdOrderDetail(String saleOrder, String saleLine, String productionOrder, String productionOrderRP,
			String volume, String changeBy, String changeDate) {
		super();
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.productionOrder = productionOrder;
		this.productionOrderRP = productionOrderRP;
		this.volume = volume;
		this.changeBy = changeBy;
		this.changeDate = changeDate;
	}
	public String getSaleOrder() {
		return saleOrder;
	}
	public void setSaleOrder(String aleOrder) {
		this.saleOrder = aleOrder;
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
	public String getProductionOrderRP() {
		return productionOrderRP;
	}
	public void setProductionOrderRP(String productionOrderRP) {
		this.productionOrderRP = productionOrderRP;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
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
