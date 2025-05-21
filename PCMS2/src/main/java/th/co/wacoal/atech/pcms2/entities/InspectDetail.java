package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class InspectDetail {
	private String postingDate = "";
	private String quantityGreige = "";
	private String operation = "";
	private String quantityFG = "";
	private String remark = "";
	public InspectDetail(String postingDate, String quantityGreige, String operation, String quantityFG,
			String remark) {
		super();
		this.postingDate = postingDate;
		this.quantityGreige = quantityGreige;
		this.operation = operation;
		this.quantityFG = quantityFG;
		this.remark = remark;
	}
	public String getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}
	public String getQuantityGreige() {
		return quantityGreige;
	}
	public void setQuantityGreige(String quantityGreige) {
		this.quantityGreige = quantityGreige;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getQuantityFG() {
		return quantityFG;
	}
	public void setQuantityFG(String quantityFG) {
		this.quantityFG = quantityFG;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
