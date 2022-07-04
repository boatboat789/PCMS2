package entities;
 

public class InspectDetail {
	private String PostingDate = ""; 
	private String QuantityGreige = ""; 
	private String Operation = ""; 
	private String QuantityFG = ""; 
	private String Remark = "";
	public InspectDetail(String postingDate, String quantityGreige, String operation, String quantityFG,
			String remark) {
		super();
		PostingDate = postingDate;
		QuantityGreige = quantityGreige;
		Operation = operation;
		QuantityFG = quantityFG;
		Remark = remark;
	}
	public String getPostingDate() {
		return PostingDate;
	}
	public void setPostingDate(String postingDate) {
		PostingDate = postingDate;
	}
	public String getQuantityGreige() {
		return QuantityGreige;
	}
	public void setQuantityGreige(String quantityGreige) {
		QuantityGreige = quantityGreige;
	}
	public String getOperation() {
		return Operation;
	}
	public void setOperation(String operation) {
		Operation = operation;
	}
	public String getQuantityFG() {
		return QuantityFG;
	}
	public void setQuantityFG(String quantityFG) {
		QuantityFG = quantityFG;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
}
