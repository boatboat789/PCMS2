package entities.erp.atech; 
public class CustomerDetail {  
	private String customerNo ; 
	private String customerName ; 
	private String customerShortName ; 
	private String customerType ; 
	private String distChannel ;
	public CustomerDetail(String customerNo, String customerName, String customerShortName, String customerType,
			String distChannel) {
		super();
		this.customerNo = customerNo;
		this.customerName = customerName;
		this.customerShortName = customerShortName;
		this.customerType = customerType;
		this.distChannel = distChannel;
	}
	public String getCustomerNo()
	{
		return customerNo;
	}
	public void setCustomerNo(String customerNo)
	{
		this.customerNo = customerNo;
	}
	public String getCustomerName()
	{
		return customerName;
	}
	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}
	public String getCustomerShortName()
	{
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName)
	{
		this.customerShortName = customerShortName;
	}
	public String getCustomerType()
	{
		return customerType;
	}
	public void setCustomerType(String customerType)
	{
		this.customerType = customerType;
	}
	public String getDistChannel()
	{
		return distChannel;
	}
	public void setDistChannel(String distChannel)
	{
		this.distChannel = distChannel;
	} 

	  
}
