package entities;

public class ReceipeDetail {  
	private String No;
	private String LotNo;
	private String PostingDate; 
	private String Receipe;
	public ReceipeDetail(String no, String lotNo, String postingDate, String receipe) {
		super();
		No = no;
		LotNo = lotNo;
		PostingDate = postingDate;
		Receipe = receipe;
	} 
	public String getNo() {
		return No;
	}
	public void setNo(String no) {
		No = no;
	}
	public String getLotNo() {
		return LotNo;
	}
	public void setLotNo(String lotNo) {
		LotNo = lotNo;
	}
	public String getPostingDate() {
		return PostingDate;
	}
	public void setPostingDate(String postingDate) {
		PostingDate = postingDate;
	}
	public String getReceipe() {
		return Receipe;
	}
	public void setReceipe(String receipe) {
		Receipe = receipe;
	}
 
	
}
