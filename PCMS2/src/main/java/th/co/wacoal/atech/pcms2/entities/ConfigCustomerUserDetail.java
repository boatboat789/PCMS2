package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class ConfigCustomerUserDetail {
	private int id;
	private String userId;
	private String customerNo;
	private String customerDivision;

	public ConfigCustomerUserDetail() {
		super();
	}

	public ConfigCustomerUserDetail(int id, String userId, String CustomerNo, String CustomerDivision) {
		super();
		this.id = id;
		this.userId = userId;
		this.customerDivision = CustomerDivision;
		this.customerNo = CustomerNo;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getCustomerNo()
	{
		return customerNo;
	}

	public void setCustomerNo(String customerNo)
	{
		this.customerNo = customerNo;
	}

	public String getCustomerDivision()
	{
		return customerDivision;
	}

	public void setCustomerDivision(String customerDivision)
	{
		this.customerDivision = customerDivision;
	}
}
