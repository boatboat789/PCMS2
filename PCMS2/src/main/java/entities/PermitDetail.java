package entities;

public class PermitDetail {
	private int id;
	private String permitId;
	private String description; 
	private boolean isPCMSDetailPage ; 
	private boolean isPCMSSumPage ; 
	private boolean isProdPathBtn ; 
	private boolean isLBMSPathBtn ; 
	private boolean isQCMSPathBtn ; 
	private boolean isInspectPathBtn ; 
	private boolean isSFCPathBtn ; 

	public PermitDetail(int id, String permitId, String description, boolean isPCMSDetailPage, boolean isPCMSSumPage,
			boolean isProdPathBtn, boolean isLBMSPathBtn, boolean isQCMSPathBtn, boolean isInspectPathBtn, boolean isSFCPathBtn) {
		super();
		this.id = id;
		this.permitId = permitId;
		this.description = description;
		this.isPCMSDetailPage = isPCMSDetailPage;
		this.isPCMSSumPage = isPCMSSumPage;
		this.isProdPathBtn = isProdPathBtn;
		this.isLBMSPathBtn = isLBMSPathBtn;
		this.isQCMSPathBtn = isQCMSPathBtn;
		this.isInspectPathBtn = isInspectPathBtn;
		this.isSFCPathBtn = isSFCPathBtn;
	}

	public PermitDetail() {
		super();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getPermitId()
	{
		return permitId;
	}

	public void setPermitId(String permitId)
	{
		this.permitId = permitId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean isPCMSDetailPage()
	{
		return isPCMSDetailPage;
	}

	public void setPCMSDetailPage(boolean isPCMSDetailPage)
	{
		this.isPCMSDetailPage = isPCMSDetailPage;
	}

	public boolean isPCMSSumPage()
	{
		return isPCMSSumPage;
	}

	public void setPCMSSumPage(boolean isPCMSSumPage)
	{
		this.isPCMSSumPage = isPCMSSumPage;
	}

	public boolean isProdPathBtn()
	{
		return isProdPathBtn;
	}

	public void setProdPathBtn(boolean isProdPathBtn)
	{
		this.isProdPathBtn = isProdPathBtn;
	}

	public boolean isLBMSPathBtn()
	{
		return isLBMSPathBtn;
	}

	public void setLBMSPathBtn(boolean isLBMSPathBtn)
	{
		this.isLBMSPathBtn = isLBMSPathBtn;
	}

	public boolean isQCMSPathBtn()
	{
		return isQCMSPathBtn;
	}

	public void setQCMSPathBtn(boolean isQCMSPathBtn)
	{
		this.isQCMSPathBtn = isQCMSPathBtn;
	}

	public boolean isInspectPathBtn()
	{
		return isInspectPathBtn;
	}

	public void setInspectPathBtn(boolean isInspectPathBtn)
	{
		this.isInspectPathBtn = isInspectPathBtn;
	}

	public boolean isSFCPathBtn()
	{
		return isSFCPathBtn;
	}

	public void setSFCPathBtn(boolean isSFCPathBtn)
	{
		this.isSFCPathBtn = isSFCPathBtn;
	}
  
//	public boolean checkPermitsWorkOperation(String operationNumber)
//	{
//		boolean isPermit = false;
//		// TODO Auto-generated method stub
//		switch (operationNumber) {
//		case "5":
//			isPermit = this.isWorkOp5();
//			break;
//		case "10":
//			isPermit = this.isWorkOp10();
//			break;
//		case "15":
//			isPermit = this.isWorkOp15();
//			break;
//		case "20":
//			isPermit = this.isWorkOp20();
//			break;
//		case "30":
//			isPermit = this.isWorkOp30();
//			break;
//		case "35":
//			isPermit = this.isWorkOp35();
//			break;
//		case "40":
//			isPermit = this.isWorkOp40();
//			break;
//		case "50":
//			isPermit = this.isWorkOp50();
//			break;
//		case "60":
//			isPermit = this.isWorkOp60();
//			break;
//		case "70":
//			isPermit = this.isWorkOp70();
//			break;
//		case "80":
//			isPermit = this.isWorkOp80();
//			break;
//		case "90":
//			isPermit = this.isWorkOp90();
//			break;
//		case "100":
//			isPermit = this.isWorkOp100();
//			break;
//		case "104":
//			isPermit = this.isWorkOp100();
//			break;
//		case "110":
//			isPermit = this.isWorkOp110();
//			break;
//		case "120":
//			isPermit = this.isWorkOp120();
//			break;
//		case "130":
//			isPermit = this.isWorkOp130();
//			break;
//		case "135":
//			isPermit = this.isWorkOp135();
//			break;
//		case "140":
//			isPermit = this.isWorkOp140();
//			break;
//		case "144":
//			isPermit = this.isWorkOp144();
//			break;
//		case "145":
//			isPermit = this.isWorkOp145();
//			break;
//		case "147":
//			isPermit = this.isWorkOp147();
//			break;
//		case "150":
//			isPermit = this.isWorkOp150();
//			break;
//		case "155":
//			isPermit = this.isWorkOp155();
//			break;
//		case "160":
//			isPermit = this.isWorkOp160();
//			break;
//		case "180":
//			isPermit = this.isWorkOp180();
//			break;
//		case "181":
//			isPermit = this.isWorkOp181();
//			break;
//		case "185":
//			isPermit = this.isWorkOp185();
//			break;
//		case "190":
//			isPermit = this.isWorkOp190();
//			break;
//		case "195":
//			isPermit = this.isWorkOp195();
//			break;
//		case "198":
//			isPermit = this.isWorkOp198();
//			break;
//		case "199":
//			isPermit = this.isWorkOp199();
//			break;
//		case "205":
//			isPermit = this.isWorkOp205();
//			break;
//		case "210":
//			isPermit = this.isWorkOp210();
//			break;
//		case "215":
//			isPermit = this.isWorkOp215();
//			break;
//		case "220":
//			isPermit = this.isWorkOp220();
//			break;
//		default:
//			isPermit = false;
//			break;
//		}
//		return isPermit;
//	}
}
