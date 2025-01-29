package th.co.wacoal.atech.pcms2.entities;

public class SaleDetail {
	  private String billDate;
	  private String billQtyPerSale;
	  private String saleOrder;
	  private String saleLine;
	  private String billQtyPerStock;
	public SaleDetail(String billDate, String billQtyPerSale, String saleOrder, String saleLine,
			String billQtyPerStock) {
		super();
		this.billDate = billDate;
		this.billQtyPerSale = billQtyPerSale;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.billQtyPerStock = billQtyPerStock;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getBillQtyPerSale() {
		return billQtyPerSale;
	}
	public void setBillQtyPerSale(String billQtyPerSale) {
		this.billQtyPerSale = billQtyPerSale;
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
	public String getBillQtyPerStock() {
		return billQtyPerStock;
	}
	public void setBillQtyPerStock(String billQtyPerStock) {
		this.billQtyPerStock = billQtyPerStock;
	}
}
