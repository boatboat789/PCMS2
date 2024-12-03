package entities.LBMS;

public class ImportDetail {  
	private String productionOrder; 
	private String labNo; 
	private String noOfSendInLab; 
	private String noOfStartLab; ; 
	private String sendLabDate; 
	private String remark; 
	private String sendFrom; 
	private String dateRequiredLab; 
	private String labStartDate; 
	private String labStopDate;
	public ImportDetail(String productionOrder, String labNo, String noOfSendInLab, String noOfStartLab, String sendLabDate,
			String remark,  String sendFrom, String dateRequiredLab, String labStartDate,
			String labStopDate) {
		super();
		this.productionOrder = productionOrder;
		this.labNo = labNo;
		this.noOfSendInLab = noOfSendInLab;
		this.noOfStartLab = noOfStartLab;
		this.sendLabDate = sendLabDate;
		this.remark = remark; 
		this.sendFrom = sendFrom;
		this.dateRequiredLab = dateRequiredLab;
		this.labStartDate = labStartDate;
		this.labStopDate = labStopDate;
	}
	public String getProductionOrder()
	{
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder)
	{
		this.productionOrder = productionOrder;
	}
	public String getLabNo()
	{
		return labNo;
	}
	public void setLabNo(String labNo)
	{
		this.labNo = labNo;
	}
	public String getNoOfSendInLab()
	{
		return noOfSendInLab;
	}
	public void setNoOfSendInLab(String noOfSendInLab)
	{
		this.noOfSendInLab = noOfSendInLab;
	}
	public String getNoOfStartLab()
	{
		return noOfStartLab;
	}
	public void setNoOfStartLab(String noOfStartLab)
	{
		this.noOfStartLab = noOfStartLab;
	}
	public String getSendLabDate()
	{
		return sendLabDate;
	}
	public void setSendLabDate(String sendLabDate)
	{
		this.sendLabDate = sendLabDate;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	} 
	public String getSendFrom()
	{
		return sendFrom;
	}
	public void setSendFrom(String sendFrom)
	{
		this.sendFrom = sendFrom;
	}
	public String getDateRequiredLab()
	{
		return dateRequiredLab;
	}
	public void setDateRequiredLab(String dateRequiredLab)
	{
		this.dateRequiredLab = dateRequiredLab;
	}
	public String getLabStartDate()
	{
		return labStartDate;
	}
	public void setLabStartDate(String labStartDate)
	{
		this.labStartDate = labStartDate;
	}
	public String getLabStopDate()
	{
		return labStopDate;
	}
	public void setLabStopDate(String labStopDate)
	{
		this.labStopDate = labStopDate;
	}   
	  
}
