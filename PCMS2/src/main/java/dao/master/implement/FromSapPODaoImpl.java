	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapPODao;
import entities.PODetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapPODaoImpl implements  FromSapPODao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String selectPO = ""
			+ "     [ProductionOrder]\r\n"
		    + "   , [RollNo]\r\n"
		    + "   , [QuantityKG]\r\n"
			+ "   , [QuantityMR]\r\n"
			+ "   , [POCreatedate]\r\n"
			+ "   , [PurchaseOrder]\r\n"
			+ "   , CASE PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"
			+ "		 	ELSE SUBSTRING( [PurchaseOrderLine] , PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' '), LEN( [PurchaseOrderLine] ) )\r\n"
			+ "       	END AS [PurchaseOrderLine] \r\n"
			+ "   , [RequiredDate]\r\n"
			+ "   , [PurchaseOrderDate]\r\n"
			+ "   , [PODefault]\r\n"
			+ "   , [POLineDefault]\r\n"
			+ "   , [POPostingDateDefault]\r\n"
			+ "   , a.[DataStatus] \r\n";
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public FromSapPODaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<PODetail> getFromSapPODetailByProductionOrder(String prodOrder){
		ArrayList<PODetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				+ this.selectPO
				+ " from [PCMS].[dbo].[FromSapPO] as a \r\n "
				+ where
				+ " Order by [RollNo]";
//
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	} 
}
