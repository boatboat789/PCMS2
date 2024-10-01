	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapFinishingDao;
import entities.FinishingDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapFinishingDaoImpl implements  FromSapFinishingDao{
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

	private String selectFinishing =
			  "      [ProductionOrder],[No],[PostingDate]\r\n"
			+ "      ,[WorkCenter],[Status],[NCDate],[Cause]\r\n"
			+ "      ,[CarNo],[DeltaE],[Color],[Operation]\r\n"
			+ "      ,[CCStatus],[CCRemark],[RollNo],[Da]\r\n"
			+ "      ,[Db],[L],[ST],[CCPostingDate]\r\n"
			+ "      ,[CCOperation],[LotNo],[DataStatus]\r\n ";
	public FromSapFinishingDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<FinishingDetail> getFromSapFinishingDetailByProductionOrder(String prodOrder){
		ArrayList<FinishingDetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				 " SELECT DISTINCT  \r\n"
				+ this.selectFinishing
				+ " from [PCMS].[dbo].[FromSapFinishing] as a \r\n "
				+ where
				+ " Order by Operation";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFinishingDetail(map));
		}
		return list;
	} 
}
