package entities;

public class ConfigCustomerUserDetail {
	private int id;
	private String userId;
	private String customerNo;
	private String customerDivision;
	private Boolean isPCMSDetailPage = true;
	private Boolean isPCMSSumPage = true ;
	private Boolean isProdPathBtn = true ;
	private Boolean isLBMSPathBtn = true;
	private Boolean isQCMSPathBtn = true ;
	private Boolean isInspectPathBtn = true;
	private Boolean isSFCPathBtn = true ;
	public ConfigCustomerUserDetail() {
		super();
	}
	public ConfigCustomerUserDetail(int id, String userId, String CustomerNo,Boolean isPCMSDetailPage, Boolean isPCMSSumPage,
			Boolean isProdPathBtn, Boolean isLBMSPathBtn, Boolean isQCMSPathBtn, Boolean isInspectPathBtn,
			Boolean isSFCPathBtn,String CustomerDivision) {
		super();
		this.id = id;
		this.userId = userId;
		this.customerDivision = CustomerDivision;
		this.customerNo = CustomerNo;
		this.isPCMSDetailPage = isPCMSDetailPage;
		this.isPCMSSumPage = isPCMSSumPage;
		this.isProdPathBtn = isProdPathBtn;
		this.isLBMSPathBtn = isLBMSPathBtn;
		this.isQCMSPathBtn = isQCMSPathBtn;
		this.isInspectPathBtn = isInspectPathBtn;
		this.isSFCPathBtn = isSFCPathBtn;
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
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getCustomerDivision() {
		return customerDivision;
	}
	public void setCustomerDivision(String customerDivision) {
		this.customerDivision = customerDivision;
	}
	public Boolean getIsPCMSDetailPage() {
		return isPCMSDetailPage;
	}
	public void setIsPCMSDetailPage(Boolean isPCMSDetailPage) {
		this.isPCMSDetailPage = isPCMSDetailPage;
	}
	public Boolean getIsPCMSSumPage() {
		return isPCMSSumPage;
	}
	public void setIsPCMSSumPage(Boolean isPCMSSumPage) {
		this.isPCMSSumPage = isPCMSSumPage;
	}
	public Boolean getIsProdPathBtn() {
		return isProdPathBtn;
	}
	public void setIsProdPathBtn(Boolean isProdPathBtn) {
		this.isProdPathBtn = isProdPathBtn;
	}
	public Boolean getIsLBMSPathBtn() {
		return isLBMSPathBtn;
	}
	public void setIsLBMSPathBtn(Boolean isLBMSPathBtn) {
		this.isLBMSPathBtn = isLBMSPathBtn;
	}
	public Boolean getIsQCMSPathBtn() {
		return isQCMSPathBtn;
	}
	public void setIsQCMSPathBtn(Boolean isQCMSPathBtn) {
		this.isQCMSPathBtn = isQCMSPathBtn;
	}
	public Boolean getIsInspectPathBtn() {
		return isInspectPathBtn;
	}
	public void setIsInspectPathBtn(Boolean isInspectPathBtn) {
		this.isInspectPathBtn = isInspectPathBtn;
	}
	public Boolean getIsSFCPathBtn() {
		return isSFCPathBtn;
	}
	public void setIsSFCPathBtn(Boolean isSFCPathBtn) {
		this.isSFCPathBtn = isSFCPathBtn;
	}



}
