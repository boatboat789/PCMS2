package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class PresetDetail {
	private String postingDate;
	private String workCenter;
	public PresetDetail(String postingDate, String workCenter) {
		super();
		this.postingDate = postingDate;
		this.workCenter = workCenter;
	}
	public String getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}
	public String getWorkCenter() {
		return workCenter;
	}
	public void setWorkCenter(String workCenter) {
		this.workCenter = workCenter;
	}

}
