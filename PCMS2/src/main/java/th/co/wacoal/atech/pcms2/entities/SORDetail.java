package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class SORDetail {
	private String saleOrder;
	private String saleLine;
	private String cfmDate;
	private String lastUpdate;
	public SORDetail(String saleOrder, String saleLine, String cFMDate, String lastUpdate) {
		super();
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.cfmDate = cFMDate;
		this.lastUpdate = lastUpdate;
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
	public String getCfmDate() {
		return cfmDate;
	}
	public void setCfmDate(String cfmDate) {
		this.cfmDate = cfmDate;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
