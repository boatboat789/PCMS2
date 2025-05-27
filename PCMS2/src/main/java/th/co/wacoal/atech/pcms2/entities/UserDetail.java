package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class UserDetail {
	private int id;
	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	private String role;
	private String department;
	private String email;
	private String arrangedBy;
	private String authorizedBy;
	private boolean isSystem;
	private boolean isAdmin;
	private String permitId;
	private String responsible;
	private String lastSignDate;
	private String changeBy;
	private String changeDate;
	private String registBy;
	private String registDate;
	private String userType;
	private boolean isCustomer;
	// ต้องตัวเล็กไม่งั้นเรียก parameter ไม่ได้
//    private boolean isDog = false;

//	public boolean getIsDog() {
//		return isDog;
//	}
//
//	public void setIsDog(boolean isDog) {
//		this.isDog = isDog;
//	}

	public UserDetail(int id, String userId, String password, String firstName, String lastName, String role,
			String department, String email, 
			String arrangedBy, String authorizedBy, boolean isSystem, boolean isAdmin,
			String permitId, String lastSignDate, String responsible
			, String changeBy, String changeDate, String registBy,
			String registDate, String UserType, boolean IsCustomer) {
		super();
		this.lastSignDate = lastSignDate;
		this.userType = UserType;
		this.id = id;
		this.userId = userId;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.department = department;
		this.email = email;
		this.arrangedBy = arrangedBy;
		this.authorizedBy = authorizedBy;
		this.isSystem = isSystem;
		this.isAdmin = isAdmin;
		this.permitId = permitId;
		this.responsible = responsible;
		this.changeBy = changeBy;
		this.changeDate = changeDate;
		this.registBy = registBy;
		this.registDate = registDate;
		this.isCustomer = IsCustomer;
	}

	public UserDetail(String UserType, String UserId) {
		// TODO Auto-generated constructor stub
		this.userType = UserType;
		this.userId = UserId;
		this.id = 0;
		this.password = "";
		this.firstName = "";
		this.lastName = "";
		this.role = "";
		this.department = "";
		this.email = "";
		this.arrangedBy = "";
		this.authorizedBy = "";
		this.isSystem = false;
		this.isAdmin = false;
		this.permitId = "";
		this.responsible = "";
		this.changeBy = "";
		this.isCustomer = false;
//		ChangeDate =;
		this.registBy = "";
//		RegistDate = registDate;
	}
//	@Override
//	public String toString() {
//		return this.UserId ;
//	}

	public UserDetail() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getArrangedBy() {
		return arrangedBy;
	}

	public void setArrangedBy(String arrangedBy) {
		this.arrangedBy = arrangedBy;
	}

	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPermitId() {
		return permitId;
	}

	public void setPermitId(String permitId) {
		this.permitId = permitId;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
 

	public String getLastSignDate()
	{
		return lastSignDate;
	}

	public void setLastSignDate(String lastSignDate)
	{
		this.lastSignDate = lastSignDate;
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

	public String getRegistBy()
	{
		return registBy;
	}

	public void setRegistBy(String registBy)
	{
		this.registBy = registBy;
	}

	public String getRegistDate()
	{
		return registDate;
	}

	public void setRegistDate(String registDate)
	{
		this.registDate = registDate;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public boolean isCustomer()
	{
		return isCustomer;
	}

	public void setCustomer(boolean isCustomer)
	{
		this.isCustomer = isCustomer;
	}

	public void setSystem(boolean isSystem)
	{
		this.isSystem = isSystem;
	}

	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "UserDetail [id=" + id + ", userId=" + userId + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", role=" + role + ", department=" + department + ", email=" + email
				+ ", arrangedBy=" + arrangedBy + ", authorizedBy=" + authorizedBy + ", isSystem=" + isSystem
				+ ", isAdmin=" + isAdmin + ", permitId=" + permitId + ", responsible=" + responsible + ", lastSignDate="
				+ lastSignDate + ", changeBy=" + changeBy + ", changeDate=" + changeDate + ", registBy=" + registBy
				+ ", registDate=" + registDate + ", userType=" + userType + ", isCustomer=" + isCustomer + "]";
	}

}
