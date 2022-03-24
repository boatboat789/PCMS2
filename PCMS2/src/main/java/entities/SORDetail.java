package entities;

public class SORDetail {
	private String SaleOrder;
	private String SaleLine;
	private String CFMDate;
	private String LastUpdate;
	public SORDetail(String saleOrder, String saleLine, String cFMDate, String lastUpdate) {
		super();
		SaleOrder = saleOrder;
		SaleLine = saleLine;
		CFMDate = cFMDate;
		LastUpdate = lastUpdate;
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
	public String getCFMDate() {
		return CFMDate;
	}
	public void setCFMDate(String cFMDate) {
		CFMDate = cFMDate;
	}
	public String getLastUpdate() {
		return LastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		LastUpdate = lastUpdate;
	}
	
	
}
