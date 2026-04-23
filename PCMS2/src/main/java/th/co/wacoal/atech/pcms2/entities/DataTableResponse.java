package th.co.wacoal.atech.pcms2.entities;

import java.util.List;

public class DataTableResponse<T> {
    private List<T> data;
    private long recordsTotal;
    private long recordsFiltered;
	public DataTableResponse() {
		super();
	}
	public DataTableResponse(List<T> data, long recordsTotal, long recordsFiltered) {
		super();
		this.data = data;
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
	}
	public List<T> getData()
	{
		return data;
	}
	public void setData(List<T> data)
	{
		this.data = data;
	}
	public long getRecordsTotal()
	{
		return recordsTotal;
	}
	public void setRecordsTotal(long recordsTotal)
	{
		this.recordsTotal = recordsTotal;
	}
	public long getRecordsFiltered()
	{
		return recordsFiltered;
	}
	public void setRecordsFiltered(long recordsFiltered)
	{
		this.recordsFiltered = recordsFiltered;
	}
}
