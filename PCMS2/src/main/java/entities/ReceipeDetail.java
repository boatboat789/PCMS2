package entities;

public class ReceipeDetail {
	private String no;
	private String lotNo;
	private String postingDate;
	private String receipe;
	public ReceipeDetail(String no, String lotNo, String postingDate, String receipe) {
		super();
		this.no = no;
		this.lotNo = lotNo;
		this.postingDate = postingDate;
		this.receipe = receipe;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}
	public String getReceipe() {
		return receipe;
	}
	public void setReceipe(String receipe) {
		this.receipe = receipe;
	}


}
