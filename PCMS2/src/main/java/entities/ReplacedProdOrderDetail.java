package entities;

public class ReplacedProdOrderDetail {

	private String SaleOrder;
	private String SaleLine;  
	private String ProductionOrder;  
	private String ProductionOrderRP;
	private String Volume;
	private String ChangeBy;
	private String ChangeDate;

	private String IconStatus;
	private String SystemStatus;
	public ReplacedProdOrderDetail() {
		super();
	}
	public ReplacedProdOrderDetail(String saleOrder, String saleLine, String productionOrder, String productionOrderRP,
			String volume, String changeBy, String changeDate) {
		super();
		SaleOrder = saleOrder;
		SaleLine = saleLine;
		ProductionOrder = productionOrder;
		ProductionOrderRP = productionOrderRP;
		Volume = volume;
		ChangeBy = changeBy;
		ChangeDate = changeDate;
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
	public String getProductionOrder() {
		return ProductionOrder;
	}
	public void setProductionOrder(String productionOrder) {
		ProductionOrder = productionOrder;
	}
	public String getProductionOrderRP() {
		return ProductionOrderRP;
	}
	public void setProductionOrderRP(String productionOrderRP) {
		ProductionOrderRP = productionOrderRP;
	}
	public String getVolume() {
		return Volume;
	}
	public void setVolume(String volume) {
		Volume = volume;
	}
	public String getChangeBy() {
		return ChangeBy;
	}
	public void setChangeBy(String changeBy) {
		ChangeBy = changeBy;
	}
	public String getChangeDate() {
		return ChangeDate;
	}
	public void setChangeDate(String changeDate) {
		ChangeDate = changeDate;
	}
	public String getIconStatus() {
		return IconStatus;
	}
	public void setIconStatus(String iconStatus) {
		IconStatus = iconStatus;
	}
	public String getSystemStatus() {
		return SystemStatus;
	}
	public void setSystemStatus(String systemStatus) {
		SystemStatus = systemStatus;
	}
	@Override
	public String toString() {
		return "ReplacedProdOrderDetail [SaleOrder=" + SaleOrder + ", SaleLine=" + SaleLine + ", ProductionOrder="
				+ ProductionOrder + ", ProductionOrderRP=" + ProductionOrderRP + ", Volume=" + Volume + ", ChangeBy="
				+ ChangeBy + ", ChangeDate=" + ChangeDate + "]";
	}
	
}
