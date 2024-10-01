package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapReceipeDao;
import entities.ReceipeDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapReceipeDaoImpl implements FromSapReceipeDao {
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public FromSapReceipeDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<ReceipeDetail> getFromSapReceipeDetailByProductionOrder(String prodOrder)
	{
		ArrayList<ReceipeDetail> list = null;
		String where = " where  ";
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  \r\n"
				+ "   [ProductionOrder],[No],[PostingDate] ,[LotNo],[Receipe],a.[DataStatus] \r\n"
				+ " from [PCMS].[dbo].[FromSapReceipe] as a \r\n "
				+ where
				+ " Order by No";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genReceipeDetail(map));
		}
		return list;
	}
}
