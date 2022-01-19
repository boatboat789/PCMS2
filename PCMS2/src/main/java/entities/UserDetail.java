package entities;

public class UserDetail {
	private String Id;
	private String EmployeeID;
	private String Password;
	private String FirstName;
	private String LastName;
	private String Role;
	private String Department; 
    private String Email;
    private String ArrangedBy;
    private String AuthorizedBy;
	public UserDetail(String string, String string2) {
		
	} 
	 
	public UserDetail(String employeeID, String password, String firstName, String lastName, String role,
			String department, String email,String Id,String ArrangedBy,String AuthorizedBy ) {
		super();
		EmployeeID = employeeID;
		Password = password;
		FirstName = firstName;
		LastName = lastName;
		Role = role;
		Department = department;
		Email = email;
		this.Id = Id;
		this.ArrangedBy = ArrangedBy;
		this.AuthorizedBy = AuthorizedBy;
	}

	public String getArrangedBy() {
		return ArrangedBy;
	}

	public void setArrangedBy(String arrangedBy) {
		ArrangedBy = arrangedBy;
	}

	public String getAuthorizedBy() {
		return AuthorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		AuthorizedBy = authorizedBy;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getEmployeeID() {
		return EmployeeID;
	}

	public void setEmployeeID(String employeeID) {
		EmployeeID = employeeID;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	@Override
	public String toString() {
		return "EmployeeDetail [Id=" + Id + ", EmployeeID=" + EmployeeID + ", Password=" + Password + ", FirstName="
				+ FirstName + ", LastName=" + LastName + ", Role=" + Role + ", Department=" + Department + ", Email="
				+ Email + ", ArrangedBy=" + ArrangedBy + ", AuthorizedBy=" + AuthorizedBy + "]";
	} 
}
