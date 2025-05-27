package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class WorkInLabDetail {
	private String no;
	private String sendDate;
	private String nok;
	private String ncDate;
	private String lotNo;
	private String receiveDate;
	private String remark;
	private String da;
	private String db;
	private String l;
	private String st;

	public WorkInLabDetail(String no, String sendDate, String nOK, String nCDate, String lotNo, String receiveDate,
			String remark, String da, String db, String l, String sT) {
		super();
		this.no = no;
		this.sendDate = sendDate;
		this.nok = nOK;
		this.ncDate = nCDate;
		this.lotNo = lotNo;
		this.receiveDate = receiveDate;
		this.remark = remark;
		this.da = da;
		this.db = db;
		this.l = l;
		this.st = sT;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getNok() {
		return nok;
	}

	public void setNok(String nok) {
		this.nok = nok;
	}

	public String getNcDate() {
		return ncDate;
	}

	public void setNcDate(String ncDate) {
		this.ncDate = ncDate;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDa() {
		return da;
	}

	public void setDa(String da) {
		this.da = da;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}
}
