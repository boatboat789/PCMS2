package th.co.wacoal.atech.pcms2.entities.erp.atech;

import java.math.BigDecimal;
import java.util.Date;

public class Z_ATT_CustomerConfirm2Detail {
	private int id ; 
    private Date sendDate;
    private Integer noPerDay;
    private Date replyDate;
    private String cfmNo;
    private String customerName;
    private String so;
    private String soLine;
    private Date dueDate;
    private String po;
    private String material;
    private String productName;
    private String labNo;
    private String color;
    private String prodID;
    private String lotNo;

    private BigDecimal  dyeL;
    private BigDecimal  dyeDa;
    private BigDecimal  dyeDb;
    private BigDecimal  dyeSt;
    private BigDecimal  dyeDeltaE;

    private BigDecimal  colorCheckL;
    private BigDecimal  colorCheckDa;
    private BigDecimal  colorCheckDb;
    private BigDecimal  colorCheckSt;
    private BigDecimal  colorCheckDeltaE;

    private BigDecimal  cfmL;
    private BigDecimal  cfmDa;
    private BigDecimal  cfmDb;
    private BigDecimal  cfmSt;
    private BigDecimal  cfmDeltaE;

    private Date colorCheckDate;
    private String colorCheckStatus;
    private String colorCheckRemark;

    private String result;
    private String qcComment;
    private String remarkFromSubmit;
    private String nextLot;
    private BigDecimal  qty;
    private String unitId;

	private String dataStatus ;
	private String changeDate ;
	private String createDate ;
	

	private String replyDateRange;
	private String sendDateRange;
	public Z_ATT_CustomerConfirm2Detail() {
		super();
	}
	public Z_ATT_CustomerConfirm2Detail(int id, Date sendDate, Integer noPerDay, Date replyDate, String cfmNo,
			String customerName, String so, String soLine, Date dueDate, String po, String material, String productName,
			String labNo, String color, String prodID, String lotNo, BigDecimal  dyeL, BigDecimal  dyeDa, BigDecimal  dyeDB, BigDecimal  dyeSt,
			BigDecimal  dyeDeltaE, BigDecimal  colorCheckL, BigDecimal  colorCheckDa, BigDecimal  colorCheckDb, BigDecimal  colorCheckSt,
			BigDecimal  colorCheckDeltaE, BigDecimal  cfmL, BigDecimal  cfmDa, BigDecimal  cfmDb, BigDecimal  cfmSt, BigDecimal  cfmDeltaE, Date colorCheckDate,
			String colorCheckStatus, String colorCheckRemark, String result, String qcComment, String remarkFromSubmit,
			String nextLot, BigDecimal  qty, String unitId, String dataStatus ) {
		super();
		this.id = id;
		this.sendDate = sendDate;
		this.noPerDay = noPerDay;
		this.replyDate = replyDate;
		this.cfmNo = cfmNo;
		this.customerName = customerName;
		this.so = so;
		this.soLine = soLine;
		this.dueDate = dueDate;
		this.po = po;
		this.material = material;
		this.productName = productName;
		this.labNo = labNo;
		this.color = color;
		this.prodID = prodID;
		this.lotNo = lotNo;
		this.dyeL = dyeL;
		this.dyeDa = dyeDa;
		this.dyeDb = dyeDB;
		this.dyeSt = dyeSt;
		this.dyeDeltaE = dyeDeltaE;
		this.colorCheckL = colorCheckL;
		this.colorCheckDa = colorCheckDa;
		this.colorCheckDb = colorCheckDb;
		this.colorCheckSt = colorCheckSt;
		this.colorCheckDeltaE = colorCheckDeltaE;
		this.cfmL = cfmL;
		this.cfmDa = cfmDa;	
		this.cfmDb = cfmDb;
		this.cfmSt = cfmSt;
		this.cfmDeltaE = cfmDeltaE;
		this.colorCheckDate = colorCheckDate;
		this.colorCheckStatus = colorCheckStatus;
		this.colorCheckRemark = colorCheckRemark;
		this.result = result;
		this.qcComment = qcComment;
		this.remarkFromSubmit = remarkFromSubmit;
		this.nextLot = nextLot;
		this.qty = qty;
		this.unitId = unitId;
		this.dataStatus = dataStatus; 
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public Date getSendDate()
	{
		return sendDate;
	}
	public void setSendDate(Date sendDate)
	{
		this.sendDate = sendDate;
	}
	public Integer getNoPerDay()
	{
		return noPerDay;
	}
	public void setNoPerDay(Integer noPerDay)
	{
		this.noPerDay = noPerDay;
	}
	public Date getReplyDate()
	{
		return replyDate;
	}
	public void setReplyDate(Date replyDate)
	{
		this.replyDate = replyDate;
	}
	public String getCfmNo()
	{
		return cfmNo;
	}
	public void setCfmNo(String cfmNo)
	{
		this.cfmNo = cfmNo;
	}
	public String getCustomerName()
	{
		return customerName;
	}
	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}
	public String getSo()
	{
		return so;
	}
	public void setSo(String so)
	{
		this.so = so;
	}
	public String getSoLine()
	{
		return soLine;
	}
	public void setSoLine(String soLine)
	{
		this.soLine = soLine;
	}
	public Date getDueDate()
	{
		return dueDate;
	}
	public void setDueDate(Date dueDate)
	{
		this.dueDate = dueDate;
	}
	public String getPo()
	{
		return po;
	}
	public void setPo(String po)
	{
		this.po = po;
	}
	public String getMaterial()
	{
		return material;
	}
	public void setMaterial(String material)
	{
		this.material = material;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	public String getLabNo()
	{
		return labNo;
	}
	public void setLabNo(String labNo)
	{
		this.labNo = labNo;
	}
	public String getColor()
	{
		return color;
	}
	public void setColor(String color)
	{
		this.color = color;
	}
	public String getProdID()
	{
		return prodID;
	}
	public void setProdID(String prodID)
	{
		this.prodID = prodID;
	}
	public String getLotNo()
	{
		return lotNo;
	}
	public void setLotNo(String lotNo)
	{
		this.lotNo = lotNo;
	} 
	public BigDecimal getDyeL()
	{
		return dyeL;
	}
	public void setDyeL(BigDecimal dyeL)
	{
		this.dyeL = dyeL;
	}
	public BigDecimal getDyeDa()
	{
		return dyeDa;
	}
	public void setDyeDa(BigDecimal dyeDa)
	{
		this.dyeDa = dyeDa;
	}
	public BigDecimal getDyeDb()
	{
		return dyeDb;
	}
	public void setDyeDb(BigDecimal dyeDB)
	{
		this.dyeDb = dyeDB;
	}
	public BigDecimal getDyeSt()
	{
		return dyeSt;
	}
	public void setDyeSt(BigDecimal dyeSt)
	{
		this.dyeSt = dyeSt;
	}
	public BigDecimal getDyeDeltaE()
	{
		return dyeDeltaE;
	}
	public void setDyeDeltaE(BigDecimal dyeDeltaE)
	{
		this.dyeDeltaE = dyeDeltaE;
	}
	public BigDecimal getColorCheckL()
	{
		return colorCheckL;
	}
	public void setColorCheckL(BigDecimal colorCheckL)
	{
		this.colorCheckL = colorCheckL;
	}
	public BigDecimal getColorCheckDa()
	{
		return colorCheckDa;
	}
	public void setColorCheckDa(BigDecimal colorCheckDa)
	{
		this.colorCheckDa = colorCheckDa;
	}
	public BigDecimal getColorCheckDb()
	{
		return colorCheckDb;
	}
	public void setColorCheckDb(BigDecimal colorCheckDb)
	{
		this.colorCheckDb = colorCheckDb;
	}
	public BigDecimal getColorCheckSt()
	{
		return colorCheckSt;
	}
	public void setColorCheckSt(BigDecimal colorCheckSt)
	{
		this.colorCheckSt = colorCheckSt;
	}
	public BigDecimal getColorCheckDeltaE()
	{
		return colorCheckDeltaE;
	}
	public void setColorCheckDeltaE(BigDecimal colorCheckDeltaE)
	{
		this.colorCheckDeltaE = colorCheckDeltaE;
	}
	public BigDecimal getCfmL()
	{
		return cfmL;
	}
	public void setCfmL(BigDecimal cfmL)
	{
		this.cfmL = cfmL;
	}
	public BigDecimal getCfmDa()
	{
		return cfmDa;
	}
	public void setCfmDa(BigDecimal cfmDa)
	{
		this.cfmDa = cfmDa;
	}
	public BigDecimal getCfmDb()
	{
		return cfmDb;
	}
	public void setCfmDb(BigDecimal cfmDb)
	{
		this.cfmDb = cfmDb;
	}
	public BigDecimal getCfmSt()
	{
		return cfmSt;
	}
	public void setCfmSt(BigDecimal cfmSt)
	{
		this.cfmSt = cfmSt;
	}
	public BigDecimal getCfmDeltaE()
	{
		return cfmDeltaE;
	}
	public void setCfmDeltaE(BigDecimal cfmDeltaE)
	{
		this.cfmDeltaE = cfmDeltaE;
	}
	public Date getColorCheckDate()
	{
		return colorCheckDate;
	}
	public void setColorCheckDate(Date colorCheckDate)
	{
		this.colorCheckDate = colorCheckDate;
	}
	public String getColorCheckStatus()
	{
		return colorCheckStatus;
	}
	public void setColorCheckStatus(String colorCheckStatus)
	{
		this.colorCheckStatus = colorCheckStatus;
	}
	public String getColorCheckRemark()
	{
		return colorCheckRemark;
	}
	public void setColorCheckRemark(String colorCheckRemark)
	{
		this.colorCheckRemark = colorCheckRemark;
	}
	public String getResult()
	{
		return result;
	}
	public void setResult(String result)
	{
		this.result = result;
	}
	public String getQcComment()
	{
		return qcComment;
	}
	public void setQcComment(String qcComment)
	{
		this.qcComment = qcComment;
	}
	public String getRemarkFromSubmit()
	{
		return remarkFromSubmit;
	}
	public void setRemarkFromSubmit(String remarkFromSubmit)
	{
		this.remarkFromSubmit = remarkFromSubmit;
	}
	public String getNextLot()
	{
		return nextLot;
	}
	public void setNextLot(String nextLot)
	{
		this.nextLot = nextLot;
	}
	public BigDecimal getQty()
	{
		return qty;
	}
	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}
	public String getUnitId()
	{
		return unitId;
	}
	public void setUnitId(String unitId)
	{
		this.unitId = unitId;
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
	public String getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}
	public String getReplyDateRange()
	{
		return replyDateRange;
	}
	public void setReplyDateRange(String replyDateRange)
	{
		this.replyDateRange = replyDateRange;
	}
	public String getSendDateRange()
	{
		return sendDateRange;
	}
	public void setSendDateRange(String sendDateRange)
	{
		this.sendDateRange = sendDateRange;
	} 
}
