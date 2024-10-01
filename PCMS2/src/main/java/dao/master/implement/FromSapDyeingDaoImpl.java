	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapDyeingDao;
import entities.DyeingDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapDyeingDaoImpl implements  FromSapDyeingDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String selectDyeing = ""
			+ "       [ProductionOrder]"
			+ "      ,[PostingDate]"
			+ "      ,[Operation]\r\n"
			+ "      ,[WorkCenter]"
			+ "      ,[DyeStatus]"
			+ "      ,[Remark]"
			+ "      ,[ReDye]\r\n"
			+ "      ,[RollNo]"
			+ "      ,[Da]"
			+ "      ,[Db]"
			+ "      ,[L]"
			+ "      ,[ST]\r\n"
			+ "      ,[ColorStatus]"
			+ "      ,[ColorRemark]"
			+ "      ,[DeltaE]"
			+ "      ,[No]\r\n"
			+ "      ,[DataStatus]\r\n ";
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public FromSapDyeingDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<DyeingDetail> getFromSapDyeingDetailByProductionOrder(String prodOrder){
		ArrayList<DyeingDetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				 + this.selectDyeing
				 + " from [PCMS].[dbo].[FromSapDyeing] as a \r\n "
				 + where
				 + " Order by Operation";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genDyeingDetail(map));
		}
		return list;
	} 
}
