package entities;

public class PresetDetail {
	private String PostingDate;
	private String WorkCenter;
	public PresetDetail(String postingDate, String workCenter) {
		super();
		PostingDate = postingDate;
		WorkCenter = workCenter;
	}
	public String getPostingDate() {
		return PostingDate;
	}
	public void setPostingDate(String postingDate) {
		PostingDate = postingDate;
	}
	public String getWorkCenter() {
		return WorkCenter;
	}
	public void setWorkCenter(String workCenter) {
		WorkCenter = workCenter;
	}
	
}
