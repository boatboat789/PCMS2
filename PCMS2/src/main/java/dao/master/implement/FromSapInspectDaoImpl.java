	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapInspectDao;
import entities.InspectDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapInspectDaoImpl  implements  FromSapInspectDao{
	// PC - Lab-ReLab 
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	private String selectInspect =
			  "      [ProductionOrder],[PostingDate],[QuantityGreige]\r\n"
			+ "      ,[Operation],[QuantityFG],[Remark],[No]\r\n"
			+ "      ,[DataStatus]\r\n ";
	public FromSapInspectDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<InspectDetail> getFromSapInspectDetailByProductionOrder(String prodOrder){
		ArrayList<InspectDetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				 " SELECT DISTINCT  \r\n"
		        + this.selectInspect
		        + " from [PCMS].[dbo].[FromSapInspect] as a \r\n "
		        + where
				+ " Order by Operation";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInspectDetail(map));
		}
		return list;
	} 
}
