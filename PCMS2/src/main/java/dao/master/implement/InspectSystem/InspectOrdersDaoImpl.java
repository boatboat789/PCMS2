	package dao.master.implement.InspectSystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.master.InspectSystem.InspectOrdersDao;
import entities.PPMM.InspectOrdersDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

@Component
public class InspectOrdersDaoImpl implements  InspectOrdersDao{
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

    @Autowired
	public InspectOrdersDaoImpl (Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<InspectOrdersDetail> getInspectOrdersByProductionOrder(String prodOrder){
		ArrayList<InspectOrdersDetail> list = null;
		String where = " where  "; 
		where += " "
				+ " PrdNumber = '" + prodOrder + "'  \r\n" ;
		String sql = ""
				+ " SELECT [Id]\r\n"
				+ "      ,[PrdNumber]  \r\n"
				+ "      ,[InspectNote] \r\n"
				+ "      ,[RollupNote] \r\n"
				+ "      ,[PackingNote] \r\n"
				+ "      ,[MachineInspect]\r\n"
				+ "      ,[MachineRollup]\r\n"
				+ "      ,[MachinePacking] \r\n"
				+ "  FROM [InspectSystem].[dbo].[InspectOrders] \r\n" 
				+ where
				+ "  \r\n "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInspectOrdersDetail(map));
		}
		return list;
	} 
}
