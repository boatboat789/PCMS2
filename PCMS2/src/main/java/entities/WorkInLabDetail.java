package entities;

public class WorkInLabDetail {   
  private String No;
  private String SendDate;
  private String NOK;
  private String NCDate;
  private String LotNo;
  private String ReceiveDate;
  private String Remark; 
  private String Da;
  private String Db;
  private String L;
  private String ST;
public WorkInLabDetail(String no, String sendDate, String nOK, String nCDate, String lotNo, String receiveDate, String remark,
		String da, String db, String l, String sT) {
	super();
	No = no;
	SendDate = sendDate;
	NOK = nOK;
	NCDate = nCDate;
	LotNo = lotNo;
	ReceiveDate = receiveDate;
	Remark = remark;
	Da = da;
	Db = db;
	L = l;
	ST = sT;
}
public String getNo() {
	return No;
}
public void setNo(String no) {
	No = no;
}
public String getSendDate() {
	return SendDate;
}
public void setSendDate(String sendDate) {
	SendDate = sendDate;
}
public String getNOK() {
	return NOK;
}
public void setNOK(String nOK) {
	NOK = nOK;
}
public String getNCDate() {
	return NCDate;
}
public void setNCDate(String nCDate) {
	NCDate = nCDate;
}
public String getLotNo() {
	return LotNo;
}
public void setLotNo(String lotNo) {
	LotNo = lotNo;
}
public String getReceiveDate() {
	return ReceiveDate;
}
public void setReceiveDate(String receiveDate) {
	ReceiveDate = receiveDate;
}
public String getRemark() {
	return Remark;
}
public void setRemark(String remark) {
	Remark = remark;
}
public String getDa() {
	return Da;
}
public void setDa(String da) {
	Da = da;
}
public String getDb() {
	return Db;
}
public void setDb(String db) {
	Db = db;
}
public String getL() {
	return L;
}
public void setL(String l) {
	L = l;
}
public String getST() {
	return ST;
}
public void setST(String sT) {
	ST = sT;
}     
}
