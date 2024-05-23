package entities;

public class TempUserStatusAutoDetail {
	private String productionOrder;
	private String saleOrder;
	private String saleLine;
	private String productionOrderRPM;
	private String volume;
	private String grade;
	private String userStatusCal;
	private String userStatusCalRP;

	private String iconStatus;
	private String systemStatus;
	public TempUserStatusAutoDetail() {
		super();
	}
	public TempUserStatusAutoDetail(String productionOrder, String saleOrder, String saleLine,
			String productionOrderRPM, String volume, String grade, String userStatusCal, String userStatusCalRP ) {
		super();
		this.productionOrder = productionOrder;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.productionOrderRPM = productionOrderRPM;
		this.volume = volume;
		this.grade = grade;
		this.userStatusCal = userStatusCal;
		this.userStatusCalRP = userStatusCalRP;
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
	public String getProductionOrderRPM() {
		return productionOrderRPM;
	}
	public void setProductionOrderRPM(String productionOrderRPM) {
		this.productionOrderRPM = productionOrderRPM;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getUserStatusCal() {
		return userStatusCal;
	}
	public void setUserStatusCal(String userStatusCal) {
		this.userStatusCal = userStatusCal;
	}
	public String getUserStatusCalRP() {
		return userStatusCalRP;
	}
	public void setUserStatusCalRP(String userStatusCalRP) {
		this.userStatusCalRP = userStatusCalRP;
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
