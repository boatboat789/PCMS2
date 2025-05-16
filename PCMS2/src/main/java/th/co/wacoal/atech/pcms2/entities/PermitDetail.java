package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class PermitDetail {
	private int id;
	private String permitId;
	private String description; 
	private boolean isPCMSMain ; 
	private boolean isPCMSDetail ; 
	private boolean isPCMSMainToProd ; 
	private boolean isPCMSMainToLBMS ; 
	private boolean isPCMSMainToQCMS ; 
	private boolean isPCMSMainToInspect ; 
	private boolean isPCMSMainToSFC ; 
	private boolean isReport ; 
	private boolean isUserManagement ;
	public PermitDetail(int id, String permitId, String description, boolean isPCMSMain, boolean isPCMSDetail,
			boolean isPCMSMainToProd, boolean isPCMSMainToLBMS, boolean isPCMSMainToQCMS, boolean isPCMSMainToInspect,
			boolean isPCMSMainToSFC, boolean isReport, boolean isUserManagement) {
		super();
		this.id = id;
		this.permitId = permitId;
		this.description = description;
		this.isPCMSMain = isPCMSMain;
		this.isPCMSDetail = isPCMSDetail;
		this.isPCMSMainToProd = isPCMSMainToProd;
		this.isPCMSMainToLBMS = isPCMSMainToLBMS;
		this.isPCMSMainToQCMS = isPCMSMainToQCMS;
		this.isPCMSMainToInspect = isPCMSMainToInspect;
		this.isPCMSMainToSFC = isPCMSMainToSFC;
		this.isReport = isReport;
		this.isUserManagement = isUserManagement;
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
	public boolean isPCMSMain()
	{
		return isPCMSMain;
	}
	public void setPCMSMain(boolean isPCMSMain)
	{
		this.isPCMSMain = isPCMSMain;
	}
	public boolean isPCMSDetail()
	{
		return isPCMSDetail;
	}
	public void setPCMSDetail(boolean isPCMSDetail)
	{
		this.isPCMSDetail = isPCMSDetail;
	}
	public boolean isPCMSMainToProd()
	{
		return isPCMSMainToProd;
	}
	public void setPCMSMainToProd(boolean isPCMSMainToProd)
	{
		this.isPCMSMainToProd = isPCMSMainToProd;
	}
	public boolean isPCMSMainToLBMS()
	{
		return isPCMSMainToLBMS;
	}
	public void setPCMSMainToLBMS(boolean isPCMSMainToLBMS)
	{
		this.isPCMSMainToLBMS = isPCMSMainToLBMS;
	}
	public boolean isPCMSMainToQCMS()
	{
		return isPCMSMainToQCMS;
	}
	public void setPCMSMainToQCMS(boolean isPCMSMainToQCMS)
	{
		this.isPCMSMainToQCMS = isPCMSMainToQCMS;
	}
	public boolean isPCMSMainToInspect()
	{
		return isPCMSMainToInspect;
	}
	public void setPCMSMainToInspect(boolean isPCMSMainToInspect)
	{
		this.isPCMSMainToInspect = isPCMSMainToInspect;
	}
	public boolean isPCMSMainToSFC()
	{
		return isPCMSMainToSFC;
	}
	public void setPCMSMainToSFC(boolean isPCMSMainToSFC)
	{
		this.isPCMSMainToSFC = isPCMSMainToSFC;
	}
	public boolean isReport()
	{
		return isReport;
	}
	public void setReport(boolean isReport)
	{
		this.isReport = isReport;
	}
	public boolean isUserManagement()
	{
		return isUserManagement;
	}
	public void setUserManagement(boolean isUserManagement)
	{
		this.isUserManagement = isUserManagement;
	} 


 
}
