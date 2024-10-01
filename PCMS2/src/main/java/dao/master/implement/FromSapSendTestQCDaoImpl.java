	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapSendTestQCDao;
import entities.SendTestQCDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapSendTestQCDaoImpl implements FromSapSendTestQCDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String selectSendTestQC =
			  "      [ProductionOrder],[No],[SendDate]\r\n"
			+ "      ,[CheckColorDate],[RollNo],[Status],[DeltaE]\r\n"
			+ "      ,[Color],[Remark],[DataStatus]\r\n ";
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public FromSapSendTestQCDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<SendTestQCDetail> getFromSapSendTestQCByProductionOrder(String prodOrder){
		ArrayList<SendTestQCDetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				 " SELECT DISTINCT  \r\n"
			    + this.selectSendTestQC
			    + " from [PCMS].[dbo].[FromSapSendTestQC] as a \r\n "
				+ where; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSendTestQCDetail(map));
		}
		return list;
	} 
}
