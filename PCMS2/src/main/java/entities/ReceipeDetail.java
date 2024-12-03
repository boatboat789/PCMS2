package entities;

public class ReceipeDetail {
	private int id;
	private String no;
	private String lotNo;
	private String postingDate;
	private String receipe; 
	private String dataStatus;
	private String changeDate;
	private String changeBy;
	private String createDate;
	private String createBy;
	private String iconStatus;
	private String systemStatus; 
	public ReceipeDetail(int id, String no, String lotNo, String postingDate, String receipe, String changeDate, String changeBy,
			String createDate, String createBy) {
		super();
		this.id = id;
		this.no = no;
		this.lotNo = lotNo;
		this.postingDate = postingDate;
		this.receipe = receipe;
		this.changeDate = changeDate;
		this.changeBy = changeBy;
		this.createDate = createDate;
		this.createBy = createBy;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getDataStatus()
	{
		return dataStatus;
	}
	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}
	public String getChangeDate()
	{
		return changeDate;
	}
	public void setChangeDate(String changeDate)
	{
		this.changeDate = changeDate;
	}
	public String getChangeBy()
	{
		return changeBy;
	}
	public void setChangeBy(String changeBy)
	{
		this.changeBy = changeBy;
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
	public void setCreateBy(String createBy)
	{
		this.createBy = createBy;
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
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}
	public String getReceipe() {
		return receipe;
	}
	public void setReceipe(String receipe) {
		this.receipe = receipe;
	}


}
