	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapCFMDao;
import entities.CFMDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapCFMDaoImpl implements  FromSapCFMDao{
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

	public FromSapCFMDaoImpl (Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<CFMDetail> getFromSapCFMDetailByProductionOrder(String prodOrder){
		ArrayList<CFMDetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				+ "   [ProductionOrder],[CFMNo],[CFMNumber]\r\n"
				+ "   ,[CFMSendDate],[CFMAnswerDate],[CFMStatus]\r\n"
				+ "   ,[CFMRemark],[Da],[Db],[L]\r\n"
				+ "   ,[ST],[SaleOrder]"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "			WHEN 0 THEN ''  \r\n"
				+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "			END AS [SaleLine] "
				+ "   ,[CFMCheckLab]\r\n"
				+ "   ,[CFMNextLab],[CFMCheckLot],[CFMNextLot]\r\n"
				+ "   ,[NextLot],[SOChange],[SOChangeQty]\r\n"
				+ "   ,[SOChangeUnit],[RollNo],[RollNoRemark]\r\n"
				+ "   ,a.[DataStatus]\r\n"
				+ "   ,[DE]\r\n"
				+ " from [PCMS].[dbo].[FromSapCFM] as a \r\n "
				+ where
				+ " Order by [CFMNo]"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCFMDetail(map));
		}
		return list;
	} 
}
