//package entities;
//
//import java.util.Date;
//
//public class UserDetailOld {
//	private int id;
//	private String userId;
//	private String password;
//	private String firstName;
//	private String astName;
//	private String Role;
//	private String Department;
//    private String Email;
//    private String ArrangedBy;
//    private String AuthorizedBy;
//    private boolean IsSystem;
//    private boolean IsAdmin;
//    private String PermitId;
//    private String Responsible;
//    private Date LastSignDate;
//    private String ChangeBy;
//    private Date ChangeDate;
//    private String RegistBy;
//    private Date RegistDate;
//    private String UserType;
//    private boolean IsCustomer;
//    private String aNoNe = "ddddddddddddddddddddddddddd";
//	public UserDetailOld(int id, String userId, String password, String firstName, String lastName, String role,
//			String department, String email, String arrangedBy, String authorizedBy, boolean isSystem, boolean isAdmin,
//			String permitId,Date lastSignDate, String responsible, String changeBy, Date changeDate, String registBy, Date registDate,
//			String UserType,boolean IsCustomer) {
//		super();
//		this.LastSignDate = lastSignDate;
//		this.UserType = UserType;
//		this.Id = id;
//		this.UserId = userId;
//		this.Password = password;
//		this.FirstName = firstName;
//		this.LastName = lastName;
//		this.Role = role;
//		this.Department = department;
//		this.Email = email;
//		this.ArrangedBy = arrangedBy;
//		this.AuthorizedBy = authorizedBy;
//		this.IsSystem = isSystem;
//		this.IsAdmin = isAdmin;
//		this.PermitId = permitId;
//		this.Responsible = responsible;
//		this.ChangeBy = changeBy;
//		this.ChangeDate = changeDate;
//		this.RegistBy = registBy;
//		this.RegistDate = registDate;
//		this.IsCustomer =IsCustomer;
//	}
//
//	public Date getLastSignDate() {
//		return LastSignDate;
//	}
//
//	public void setLastSignDate(Date lastSignDate) {
//		LastSignDate = lastSignDate;
//	}
//
//	public UserDetailOld() {
//		// TODO Auto-generated constructor stub
//	}
//
////	 public UserDetail(String userId, String password, String machine) {
//////		 this(0, userId, password, machine, null, null, null, null, null, false, false, "", "", null, "", null, "", null);
////	 }
//
//
//	public UserDetailOld(String UserType, String UserId) {
//		// TODO Auto-generated constructor stub
//		this.UserType = UserType;
//		this.UserId = UserId;
//		Id = 0;
//		Password = "";
//		FirstName = "";
//		LastName = "";
//		Role = "";
//		Department = "";
//		Email = "";
//		ArrangedBy = "";
//		AuthorizedBy = "";
//		IsSystem = false;
//		IsAdmin = false;
//		PermitId = "";
//		Responsible = "";
//		ChangeBy = "";
//		IsCustomer = false;
////		ChangeDate =;
//		RegistBy = "";
////		RegistDate = registDate;
//	}
//
//	public int getId() {
//		return Id;
//	}
//
//	public void setId(int id) {
//		Id = id;
//	}
//
//	public String getUserId() {
//		return UserId;
//	}
//
//	public void setUserId(String userId) {
//		UserId = userId;
//	}
//
//	public String getPassword() {
//		return Password;
//	}
//
//	public void setPassword(String password) {
//		Password = password;
//	}
//
//	public String getFirstName() {
//		return FirstName;
//	}
//
//	public void setFirstName(String firstName) {
//		FirstName = firstName;
//	}
//
//	public String getLastName() {
//		return LastName;
//	}
//
//	public void setLastName(String lastName) {
//		LastName = lastName;
//	}
//
//	public String getRole() {
//		return Role;
//	}
//
//	public void setRole(String role) {
//		Role = role;
//	}
//
//	public String getDepartment() {
//		return Department;
//	}
//
//	public void setDepartment(String department) {
//		Department = department;
//	}
//
//	public String getEmail() {
//		return Email;
//	}
//
//	public void setEmail(String email) {
//		Email = email;
//	}
//
//	public String getArrangedBy() {
//		return ArrangedBy;
//	}
//
//	public void setArrangedBy(String arrangedBy) {
//		ArrangedBy = arrangedBy;
//	}
//
//	public String getAuthorizedBy() {
//		return AuthorizedBy;
//	}
//
//	public void setAuthorizedBy(String authorizedBy) {
//		AuthorizedBy = authorizedBy;
//	}
//
//	public boolean getIsSystem() {
//		return IsSystem;
//	}
//
//	public void setIsSystem(boolean isSystem) {
//		IsSystem = isSystem;
//	}
//
//	public boolean getIsAdmin() {
//		return IsAdmin;
//	}
//
//	public void setIsAdmin(boolean isAdmin) {
//		IsAdmin = isAdmin;
//	}
//
//	public String getPermitId() {
//		return PermitId;
//	}
//
//	public void setPermitId(String permitId) {
//		PermitId = permitId;
//	}
//
//	public String getResponsible() {
//		return Responsible;
//	}
//
//	public void setResponsible(String responsible) {
//		Responsible = responsible;
//	}
//
//	public String getChangeBy() {
//		return ChangeBy;
//	}
//
//	public void setChangeBy(String changeBy) {
//		ChangeBy = changeBy;
//	}
//
//	public Date getChangeDate() {
//		return ChangeDate;
//	}
//
//	public void setChangeDate(Date changeDate) {
//		ChangeDate = changeDate;
//	}
//
//	public String getRegistBy() {
//		return RegistBy;
//	}
//
//	public void setRegistBy(String registBy) {
//		RegistBy = registBy;
//	}
//
//	public Date getRegistDate() {
//		return RegistDate;
//	}
//
//	public void setRegistDate(Date registDate) {
//		RegistDate = registDate;
//	}
//
//	public String getUserType() {
//		return UserType;
//	}
//
//	public void setUserType(String userType) {
//		UserType = userType;
//	}
//
//	public boolean getIsCustomer() {
//		return IsCustomer;
//	}
//
//	public void setIsCustomer(boolean isCustomer) {
//		IsCustomer = isCustomer;
//	}
//
//	public String getaNoNe() {
//		return aNoNe;
//	}
//
//	public void setaNoNe(String aNoNe) {
//		this.aNoNe = aNoNe;
//	}
//
////	@Override
////	public String toString() {
////		return this.UserId ;
////	}
//
//
//}
