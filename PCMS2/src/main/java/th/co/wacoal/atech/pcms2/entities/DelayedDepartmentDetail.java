package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class DelayedDepartmentDetail {
	public String delayedDepartment;

	public DelayedDepartmentDetail(String delayedDepartment) {
		super();
		this.delayedDepartment = delayedDepartment;
	}

	public String getDelayedDepartment() {
		return delayedDepartment;
	}

	public void setDelayedDepartment(String delayedDepartment) {
		this.delayedDepartment = delayedDepartment;
	}
}
