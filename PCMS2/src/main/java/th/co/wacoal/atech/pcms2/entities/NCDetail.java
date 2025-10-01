package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class NCDetail {

	private String productionOrder;
	private String no;
	private String ncDate;
	private String ncLength;
	private String ncReceiverBase;
	private String ncCarNumber;
	private String ncProblem;
	private String ncSolution;

	public NCDetail(String productionOrder, String no, String nCDate, String ncLength, String ncReceiverBase, String ncCarNumber,
			String ncProblem, String ncSolution) {
		super();
		this.productionOrder = productionOrder;
		this.no = no;
		this.ncDate = nCDate;
		this.ncLength = ncLength;
		this.ncReceiverBase = ncReceiverBase;
		this.ncCarNumber = ncCarNumber;
		this.ncProblem = ncProblem;
		this.ncSolution = ncSolution;
	}
	public String getProductionOrder()
	{
		return productionOrder;
	}

	public void setProductionOrder(String productionOrder)
	{
		this.productionOrder = productionOrder;
	}

	public String getNo()
	{
		return no;
	}

	public void setNo(String no)
	{
		this.no = no;
	}

	public String getncDate()
	{
		return ncDate;
	}

	public void setncDate(String nCDate)
	{
		this.ncDate = nCDate;
	}

	public String getNcLength()
	{
		return ncLength;
	}

	public void setNcLength(String ncLength)
	{
		this.ncLength = ncLength;
	}

	public String getNcReceiverBase()
	{
		return ncReceiverBase;
	}

	public void setNcReceiverBase(String ncReceiverBase)
	{
		this.ncReceiverBase = ncReceiverBase;
	}

	public String getNcCarNumber()
	{
		return ncCarNumber;
	}

	public void setNcCarNumber(String ncCarNumber)
	{
		this.ncCarNumber = ncCarNumber;
	}

	public String getNcProblem()
	{
		return ncProblem;
	}

	public void setNcProblem(String ncProblem)
	{
		this.ncProblem = ncProblem;
	}

	public String getNcSolution()
	{
		return ncSolution;
	}

	public void setNcSolution(String ncSolution)
	{
		this.ncSolution = ncSolution;
	}
}
