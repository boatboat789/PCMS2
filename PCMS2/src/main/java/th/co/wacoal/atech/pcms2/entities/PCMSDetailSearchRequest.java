package th.co.wacoal.atech.pcms2.entities;

import java.util.ArrayList;

public class PCMSDetailSearchRequest {
    private ArrayList<PCMSTableDetail> criteria;
    private int start;
    private int length;
	public PCMSDetailSearchRequest() {
		super();
	}
	public PCMSDetailSearchRequest(ArrayList<PCMSTableDetail> criteria, int start, int length) {
		super();
		this.criteria = criteria;
		this.start = start;
		this.length = length;
	}
	public ArrayList<PCMSTableDetail> getCriteria()
	{
		return criteria;
	}
	public void setCriteria(ArrayList<PCMSTableDetail> criteria)
	{
		this.criteria = criteria;
	}
	public int getStart()
	{
		return start;
	}
	public void setStart(int start)
	{
		this.start = start;
	}
	public int getLength()
	{
		return length;
	}
	public void setLengthl(int lengthl)
	{
		this.length = lengthl;
	}
}
