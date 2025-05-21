package th.co.wacoal.atech.pcms2.entities.erp.atech;

import javax.persistence.Entity;

@Entity
public class CustomerDetail {  
	private String customerNo ; 
	private String customerNoWOZero ; 
	private String customerName ; 
	private String customerShortName ; 
	private String customerType ; 
	private String distChannel ;
	private String syncDate ;
	private boolean isSabina;
	public CustomerDetail(String customerNo, String customerName, String customerShortName, String customerType,
			String distChannel, String syncDate) {
		super();
		this.customerNo = customerNo;
		this.customerName = customerName;
		this.customerShortName = customerShortName;
		this.customerType = customerType;
		this.distChannel = distChannel;
		this.syncDate = syncDate;
	}
	public boolean isSabina()
	{
		return isSabina;
	}
	public void setSabina(boolean isSabina)
	{
		this.isSabina = isSabina;
	}
	public String getCustomerNoWOZero()
	{
		return customerNoWOZero;
	}
	public void setCustomerNoWOZero(String customerNoWOZero)
	{
		this.customerNoWOZero = customerNoWOZero;
	}
	public String getSyncDate()
	{
		return syncDate;
	}
	public void setSyncDate(String syncDate)
	{
		this.syncDate = syncDate;
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
