package th.co.wacoal.atech.pcms2.entities;

public class PackingDetail { // ค่าผิดอยู่ช็คให่ม
	private String productionOrder;
	private String postingDate;
	private String quantity;
	private String rollNo;
	private String status;
	private String quantityKG;
	private String grade;
	private String quantityYD;

	public PackingDetail(String postingDate, String quantity, String rollNo, String status, String quantityKG,
			String grade, String quantityYD) {
		super();
		this.quantityYD = quantityYD;
		this.postingDate = postingDate;
		this.quantity = quantity;
		this.rollNo = rollNo;
		this.status = status;
		this.quantityKG = quantityKG;
		this.grade = grade;
	}

	public String getProductionOrder() {
		return productionOrder;
	}

	public void setProductionOrder(String productionOrder) {
		this.productionOrder = productionOrder;
	}

	public String getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQuantityKG() {
		return quantityKG;
	}

	public void setQuantityKG(String quantityKG) {
		this.quantityKG = quantityKG;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getQuantityYD() {
		return quantityYD;
	}

	public void setQuantityYD(String quantityYD) {
		this.quantityYD = quantityYD;
	}
}
