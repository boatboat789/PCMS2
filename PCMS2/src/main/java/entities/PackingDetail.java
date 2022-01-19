package entities;

public class PackingDetail { // ค่าผิดอยู่ช็คให่ม
	private String ProductionOrder;
		      private String PostingDate;
		      private String Quantity;
		      private String RollNo;
		      private String Status;
		      private String QuantityKG;
		      private String Grade;
		      private String QuantityYD;
			public PackingDetail( String postingDate, String quantity, String rollNo,
					String status, String quantityKG, String grade,String quantityYD) {
				super(); 
				QuantityYD = quantityYD;
				PostingDate = postingDate;
				Quantity = quantity;
				RollNo = rollNo;
				Status = status;
				QuantityKG = quantityKG;
				Grade = grade;
			}
			public String getQuantityYD() {
				return QuantityYD;
			}
			public void setQuantityYD(String quantityYD) {
				QuantityYD = quantityYD;
			}
			public String getProductionOrder() {
				return ProductionOrder;
			}
			public void setProductionOrder(String productionOrder) {
				ProductionOrder = productionOrder;
			}
			public String getPostingDate() {
				return PostingDate;
			}
			public void setPostingDate(String postingDate) {
				PostingDate = postingDate;
			}
			public String getQuantity() {
				return Quantity;
			}
			public void setQuantity(String quantity) {
				Quantity = quantity;
			}
			public String getRollNo() {
				return RollNo;
			}
			public void setRollNo(String rollNo) {
				RollNo = rollNo;
			}
			public String getStatus() {
				return Status;
			}
			public void setStatus(String status) {
				Status = status;
			}
			public String getQuantityKG() {
				return QuantityKG;
			}
			public void setQuantityKG(String quantityKG) {
				QuantityKG = quantityKG;
			}
			public String getGrade() {
				return Grade;
			}
			public void setGrade(String grade) {
				Grade = grade;
			} 
}
