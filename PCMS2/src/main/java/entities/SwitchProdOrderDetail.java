package entities;

public class SwitchProdOrderDetail { 
	private String SaleOrder;
	private String SaleLine;  
	private String ProductionOrder; 
	private String SaleOrderSW;
	private String SaleLineSW;
	private String ProductionOrderSW;
	private String ChangeBy;
	private String ChangeDate;
	private String TypePrd;
	private String SwitchRemark;
	private String IconStatus;
	private String SystemStatus;
	public SwitchProdOrderDetail() {
		super();
	}
	public SwitchProdOrderDetail(String saleOrder, String saleLine, String productionOrder, String saleOrderSW,
			String saleLineSW, String productionOrderSW, String changeBy, String changeDate) {
		super();
		SaleOrder = saleOrder;
		SaleLine = saleLine;
		ProductionOrder = productionOrder;
		SaleOrderSW = saleOrderSW;
		SaleLineSW = saleLineSW;
		ProductionOrderSW = productionOrderSW;
		ChangeBy = changeBy;
		ChangeDate = changeDate;
	}
	public String getTypePrd() {
		return TypePrd;
	}
	public void setTypePrd(String typePrd) {
		TypePrd = typePrd;
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
	public String getSaleOrderSW() {
		return SaleOrderSW;
	}
	public void setSaleOrderSW(String saleOrderSW) {
		SaleOrderSW = saleOrderSW;
	}
	public String getSaleLineSW() {
		return SaleLineSW;
	}
	public void setSaleLineSW(String saleLineSW) {
		SaleLineSW = saleLineSW;
	}
	public String getProductionOrderSW() {
		return ProductionOrderSW;
	}
	public void setProductionOrderSW(String productionOrderSW) {
		ProductionOrderSW = productionOrderSW;
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
	public String getSwitchRemark() {
		return SwitchRemark;
	}
	public void setSwitchRemark(String switchRemark) {
		SwitchRemark = switchRemark;
	}
	
}
