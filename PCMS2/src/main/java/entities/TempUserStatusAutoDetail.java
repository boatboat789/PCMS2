package entities;

public class TempUserStatusAutoDetail { 
	private String ProductionOrder;  
	private String SaleOrder;
	private String SaleLine;  
	private String ProductionOrderRPM;
	private String Volume;
	private String Grade;
	private String UserStatusCal;
	private String UserStatusCalRP;

	private String IconStatus;
	private String SystemStatus;
	public TempUserStatusAutoDetail() {
		super();
	}
	public TempUserStatusAutoDetail(String productionOrder, String saleOrder, String saleLine,
			String productionOrderRPM, String volume, String grade, String userStatusCal, String userStatusCalRP ) {
		super();
		ProductionOrder = productionOrder;
		SaleOrder = saleOrder;
		SaleLine = saleLine;
		ProductionOrderRPM = productionOrderRPM;
		Volume = volume;
		Grade = grade;
		UserStatusCal = userStatusCal;
		UserStatusCalRP = userStatusCalRP; 
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
	public String getProductionOrderRPM() {
		return ProductionOrderRPM;
	}
	public void setProductionOrderRPM(String productionOrderRPM) {
		ProductionOrderRPM = productionOrderRPM;
	}
	public String getVolume() {
		return Volume;
	}
	public void setVolume(String volume) {
		Volume = volume;
	}
	public String getGrade() {
		return Grade;
	}
	public void setGrade(String grade) {
		Grade = grade;
	}
	public String getUserStatusCal() {
		return UserStatusCal;
	}
	public void setUserStatusCal(String userStatusCal) {
		UserStatusCal = userStatusCal;
	}
	public String getUserStatusCalRP() {
		return UserStatusCalRP;
	}
	public void setUserStatusCalRP(String userStatusCalRP) {
		UserStatusCalRP = userStatusCalRP;
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
	
}
