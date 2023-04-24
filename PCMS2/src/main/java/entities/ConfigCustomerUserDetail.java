package entities;

public class ConfigCustomerUserDetail {
	private int Id;
	private String UserId;
	private String CustomerNo;
	private String CustomerDivision;
	private Boolean IsPCMSDetailPage;;
	private Boolean IsPCMSSumPage;
	private Boolean IsProdPathBtn;
	private Boolean IsLBMSPathBtn;
	private Boolean IsQCMSPathBtn;
	private Boolean IsInspectPathBtn;
	private Boolean IsSFCPathBtn;
	public ConfigCustomerUserDetail() {
		super();
	}
	public ConfigCustomerUserDetail(int id, String userId, String CustomerNo,Boolean isPCMSDetailPage, Boolean isPCMSSumPage,
			Boolean isProdPathBtn, Boolean isLBMSPathBtn, Boolean isQCMSPathBtn, Boolean isInspectPathBtn,
			Boolean isSFCPathBtn,String CustomerDivision) {
		super();
		Id = id;
		UserId = userId;
		this.CustomerDivision = CustomerDivision;
		this.CustomerNo = CustomerNo;
		IsPCMSDetailPage = isPCMSDetailPage;
		IsPCMSSumPage = isPCMSSumPage;
		IsProdPathBtn = isProdPathBtn;
		IsLBMSPathBtn = isLBMSPathBtn;
		IsQCMSPathBtn = isQCMSPathBtn;
		IsInspectPathBtn = isInspectPathBtn;
		IsSFCPathBtn = isSFCPathBtn;
	}
	public String getCustomerDivision() {
		return CustomerDivision;
	}
	public void setCustomerDivision(String customerDivision) {
		CustomerDivision = customerDivision;
	}
	public String getCustomerNo() {
		return CustomerNo;
	}
	public void setCustomerNo(String customerNo) {
		CustomerNo = customerNo;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public Boolean getIsPCMSDetailPage() {
		return IsPCMSDetailPage;
	}
	public void setIsPCMSDetailPage(Boolean isPCMSDetailPage) {
		IsPCMSDetailPage = isPCMSDetailPage;
	}
	public Boolean getIsPCMSSumPage() {
		return IsPCMSSumPage;
	}
	public void setIsPCMSSumPage(Boolean isPCMSSumPage) {
		IsPCMSSumPage = isPCMSSumPage;
	}
	public Boolean getIsProdPathBtn() {
		return IsProdPathBtn;
	}
	public void setIsProdPathBtn(Boolean isProdPathBtn) {
		IsProdPathBtn = isProdPathBtn;
	}
	public Boolean getIsLBMSPathBtn() {
		return IsLBMSPathBtn;
	}
	public void setIsLBMSPathBtn(Boolean isLBMSPathBtn) {
		IsLBMSPathBtn = isLBMSPathBtn;
	}
	public Boolean getIsQCMSPathBtn() {
		return IsQCMSPathBtn;
	}
	public void setIsQCMSPathBtn(Boolean isQCMSPathBtn) {
		IsQCMSPathBtn = isQCMSPathBtn;
	}
	public Boolean getIsInspectPathBtn() {
		return IsInspectPathBtn;
	}
	public void setIsInspectPathBtn(Boolean isInspectPathBtn) {
		IsInspectPathBtn = isInspectPathBtn;
	}
	public Boolean getIsSFCPathBtn() {
		return IsSFCPathBtn;
	}
	public void setIsSFCPathBtn(Boolean isSFCPathBtn) {
		IsSFCPathBtn = isSFCPathBtn;
	} 
	
	
	
}
