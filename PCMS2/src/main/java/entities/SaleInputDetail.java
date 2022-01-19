package entities;

public class SaleInputDetail {    
	  private String BillDate;
	  private String BillQtyPerSale;
	  private String SaleOrder;
	  private String SaleLine;
	  private String BillQtyPerStock;
	public SaleInputDetail(String billDate, String billQtyPerSale, String saleOrder, String saleLine,
			String billQtyPerStock) {
		super();
		BillDate = billDate;
		BillQtyPerSale = billQtyPerSale;
		SaleOrder = saleOrder;
		SaleLine = saleLine;
		BillQtyPerStock = billQtyPerStock;
	}
	public String getBillDate() {
		return BillDate;
	}
	public void setBillDate(String billDate) {
		BillDate = billDate;
	}
	public String getBillQtyPerSale() {
		return BillQtyPerSale;
	}
	public void setBillQtyPerSale(String billQtyPerSale) {
		BillQtyPerSale = billQtyPerSale;
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
	public String getBillQtyPerStock() {
		return BillQtyPerStock;
	}
	public void setBillQtyPerStock(String billQtyPerStock) {
		BillQtyPerStock = billQtyPerStock;
	} 
}
