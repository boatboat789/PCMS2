package th.co.wacoal.atech.pcms2.entities;

public class QueryResults {
	private String whereCaseTry;
	private String whereCaseTryRP;
	private String tmpWhereNoLotUCAL;
	private String where;
	private String whereBMainUserStatus;
	private String whereSale;
	private String whereWaitLot;

	public QueryResults(String whereCaseTry, String whereCaseTryRP, String tmpWhereNoLotUCAL, String where
			, String whereBMainUserStatus
			, String whereSale
			, String whereWaitLot) {
        this.whereCaseTry = whereCaseTry;
        this.whereCaseTryRP = whereCaseTryRP;
        this.tmpWhereNoLotUCAL = tmpWhereNoLotUCAL;
        this.where = where;
        this.whereBMainUserStatus = whereBMainUserStatus;
        this.whereSale = whereSale;
        this.whereWaitLot = whereWaitLot;
    }

	// Getters
	public String getWhereCaseTry()
	{
		return whereCaseTry;
	}

	public String getWhereCaseTryRP()
	{
		return whereCaseTryRP;
	}

	public String getTmpWhereNoLotUCAL()
	{
		return tmpWhereNoLotUCAL;
	}

	public String getWhere()
	{
		return where;
	}

	public String getWhereBMainUserStatus()
	{
		return whereBMainUserStatus;
	}

	public String getWhereSale()
	{
		return whereSale;
	}

	public void setWhereSale(String whereSale)
	{
		this.whereSale = whereSale;
	}

	public String getWhereWaitLot()
	{
		return whereWaitLot;
	}

	public void setWhereWaitLot(String whereWaitLot)
	{
		this.whereWaitLot = whereWaitLot;
	}

	public void setWhereCaseTry(String whereCaseTry)
	{
		this.whereCaseTry = whereCaseTry;
	}

	public void setWhereCaseTryRP(String whereCaseTryRP)
	{
		this.whereCaseTryRP = whereCaseTryRP;
	}

	public void setTmpWhereNoLotUCAL(String tmpWhereNoLotUCAL)
	{
		this.tmpWhereNoLotUCAL = tmpWhereNoLotUCAL;
	}

	public void setWhere(String where)
	{
		this.where = where;
	}

	public void setWhereBMainUserStatus(String whereBMainUserStatus)
	{
		this.whereBMainUserStatus = whereBMainUserStatus;
	}
}