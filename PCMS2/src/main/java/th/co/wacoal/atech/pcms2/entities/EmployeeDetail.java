package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class EmployeeDetail {
	private String id;
	private String permitId;
	private String responsible;
	private String employeeID;
	private String password;
	private String firstName;
	private String lastName;
	private String role;
	private String department;
	private String email;
	private String arrangedBy;
	private String authorizedBy;
	private String dataStatus;
	private String changeBy;
	private String changeDate;
	private String createBy;
	private String createDate;

	private String iconStatus;
	private String systemStatus;

	public EmployeeDetail() {

	}

	public EmployeeDetail(String employeeID, String permitId, String responsible, String password, String firstName,
			String lastName, String role, String department, String email, String Id, String ArrangedBy, String AuthorizedBy,
			String changeBy, String changeDate, String createBy, String createDate) {
		super();
		this.createBy = createBy;
		this.createDate = createDate;
		this.changeBy = changeBy;
		this.changeDate = changeDate;
		this.responsible = responsible;
		this.permitId = permitId;
		this.employeeID = employeeID;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.department = department;
		this.email = email;
		this.id = Id;
		this.arrangedBy = ArrangedBy;
		this.authorizedBy = AuthorizedBy;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public String getCreateBy()
	{
		return createBy;
	}

	public String getIconStatus()
	{
		return iconStatus;
	}

	public void setIconStatus(String iconStatus)
	{
		this.iconStatus = iconStatus;
	}

	public String getSystemStatus()
	{
		return systemStatus;
	}

	public void setSystemStatus(String systemStatus)
	{
		this.systemStatus = systemStatus;
	}

	public String getDataStatus()
	{
		return dataStatus;
	}

	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}

	public String getChangeBy()
	{
		return changeBy;
	}

	public void setChangeBy(String changeBy)
	{
		this.changeBy = changeBy;
	}

	public String getChangeDate()
	{
		return changeDate;
	}

	public void setChangeDate(String changeDate)
	{
		this.changeDate = changeDate;
	}

	public String getResponsible()
	{
		return responsible;
	}

	public void setResponsible(String responsible)
	{
		this.responsible = responsible;
	}

	public String getPermitId()
	{
		return permitId;
	}

	public void setPermitId(String permitId)
	{
		this.permitId = permitId;
	}

	public String getArrangedBy()
	{
		return arrangedBy;
	}

	public String getAuthorizedBy()
	{
		return authorizedBy;
	}

	public String getDepartment()
	{
		return department;
	}

	public String getEmail()
	{
		return email;
	}

	public String getEmployeeID()
	{
		return employeeID;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getId()
	{
		return id;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getPassword()
	{
		return password;
	}

	public String getRole()
	{
		return role;
	}

	public void setArrangedBy(String arrangedBy)
	{
		this.arrangedBy = arrangedBy;
	}

	public void setAuthorizedBy(String authorizedBy)
	{
		this.authorizedBy = authorizedBy;
	}

	public void setDepartment(String department)
	{
		this.department = department;
	}

	public void setEmail(String rmail)
	{
		this.email = rmail;
	}

	public void setEmployeeID(String employeeID)
	{
		this.employeeID = employeeID;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	@Override
	public String toString()
	{
		return "EmployeeDetail [id="
				+ id
				+ ", employeeID="
				+ employeeID
				+ ", firstName="
				+ firstName
				+ ", lastName="
				+ lastName
				+ ", role="
				+ role
				+ ", department="
				+ department
				+ ", email="
				+ email
				+ ", arrangedBy="
				+ arrangedBy
				+ ", authorizedBy="
				+ authorizedBy
				+ "]";
	}

	public void setCreateBy(String createBy)
	{
		this.createBy = createBy;
	}

}
