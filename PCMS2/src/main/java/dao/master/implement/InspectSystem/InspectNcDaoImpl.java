	package dao.master.implement.InspectSystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.master.InspectSystem.InspectNcDao;
import entities.NCDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

@Component
public class InspectNcDaoImpl implements  InspectNcDao{
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
	public InspectNcDaoImpl (Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<NCDetail> getInspectNcByProductionOrder(String prodOrder){
		ArrayList<NCDetail> list = null;
		String where = " where  "; 
		where += " "
				+ " PrdNumber = '" + prodOrder + "' and \r\n"
				+ " NcSolution <> 'ADMIN หลังบ้านออก'    \r\n";
		String sql = ""
				+ " SELECT [Id]\r\n" 
				+ "      ,[PrdNumber]   \r\n" 
				+ "	  	 ,CAST( ROW_NUMBER() OVER (PARTITION BY [PrdNumber] ORDER BY [PrdNumber],[NcDate]) AS INT)as No\r\n"
				+ "      ,[NcDate]  \r\n"
				+ "	  	 ,[NcLength]\r\n"
				+ "      ,[NcReceiverBase]\r\n" 
				+ "      ,[NcCarNumber] \r\n" 
				+ "      ,[NcProblem] \r\n" 
				+ "      ,[NcSolution] \r\n" 
				+ "  FROM [InspectSystem].[dbo].[InspectNc]\r\n" 
				+ where
				+ "  ORDER BY [PrdNumber],NCDATE  \r\n "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genNCDetail(map));
		}
		return list;
	} 
}
