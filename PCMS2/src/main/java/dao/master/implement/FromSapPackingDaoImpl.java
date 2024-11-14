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
			  "      fsp.[Id]\r\n"
			  + "      ,[ProductionOrder]\r\n"
			  + "      ,[PostingDate]\r\n"
			  + "      ,[Quantity]\r\n"
			  + "      ,[RollNo]\r\n"
			  + "      ,[Status]\r\n"
			  + "	  ,insorder.[RollupNote] as [Status]\r\n"
			  + "      ,[QuantityKG]\r\n"
			  + "      ,[Grade]\r\n"
			  + "      ,[No]\r\n"
			  + "      ,[DataStatus]\r\n"
			  + "      ,[QuantityYD]\r\n"
			  + "      ,fsp.[ChangeDate]\r\n"
			  + "      ,[CreateDate] \r\n ";  ; 
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
		where += " "
				+ " fsp.ProductionOrder = '" + prodOrder + "'  and \r\n"
				+ " fsp.[DataStatus] = 'O' \r\n";
		String sql =
				 ""
				 + " SELECT DISTINCT  \r\n"
				+ this.selectPacking 
				+ "  from [PCMS].[dbo].[FromSapPacking] as fsp\r\n"
				+ "  left join [InspectSystem].[dbo].[InspectOrders] as insorder on fsp.[ProductionOrder] = insorder.[PrdNumber] \r\n "
				+ where; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPackingDetail(map));
		}
		return list;
	} 
}
