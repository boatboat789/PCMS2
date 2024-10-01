	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapWorkInLabDao;
import entities.WorkInLabDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapWorkInLabDaoImpl  implements  FromSapWorkInLabDao{
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

	public FromSapWorkInLabDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<WorkInLabDetail> getFromSapWorkInLabDetailByProductionOrder(String prodOrder){
		ArrayList<WorkInLabDetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				+ "   [ProductionOrder],[SaleOrder]\r\n"
				+ "   ,[SaleLine],[No],[SendDate],[NOK]\r\n"
				+ "   ,[LotNo],[ReceiveDate],[Remark],[Da]\r\n"
				+ "   ,[Db],[L],[ST],a.[DataStatus]\r\n" + "   "
				+ " from [PCMS].[dbo].[FromSapWorkInLab] as a \r\n "
				+ where
				+ " Order by No";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genWorkInLabDetail(map));
		}
		return list;
	} 
}
