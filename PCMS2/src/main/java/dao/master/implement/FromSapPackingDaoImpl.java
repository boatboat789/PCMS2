	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapPackingDao;
import entities.PackingDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapPackingDaoImpl implements  FromSapPackingDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New 
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	private String selectPacking =
			  "      [ProductionOrder],[PostingDate],[Quantity]\r\n"
			+ "      ,[RollNo],[Status],[QuantityKG],[Grade]\r\n"
			+ "      ,[No],[DataStatus],[QuantityYD]\r\n ";  ; 
	public FromSapPackingDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder){
		ArrayList<PackingDetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				 "SELECT DISTINCT  \r\n"
				+ this.selectPacking
				+ " from [PCMS].[dbo].[FromSapPacking] as a \r\n "
				+ where; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPackingDetail(map));
		}
		return list;
	} 
}
