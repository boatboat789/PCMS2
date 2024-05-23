	package dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dao.master.FromSORCFMDao;
import entities.SORDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSORCFMDaoImpl implements  FromSORCFMDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	@SuppressWarnings("unused")
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public FromSORCFMDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public String upSertFromSORCFMDetail(ArrayList<SORDetail> list) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		Timestamp dateTime = new Timestamp(time);
		String saleLine = "", cfmDate = "";
		String iconStatus = "I";
		String sql =
					"UPDATE [PCMS].[dbo].[FromSORCFM] "
					+ " SET [CFMDate] = ?\n"
					+ "     ,[ChangeDate] = ?\n"
					+ " WHERE [SaleOrder] = ? and [SaleLine]  = ? "
					+ " declare  @rc int = @@ROWCOUNT "
					+ "  if @rc <> 0 "
					+ " print @rc "
					+ " else "
					+ " INSERT INTO [PCMS].[dbo].[FromSORCFM]	 "
					+ " ([SaleOrder] ,[SaleLine] ,[CFMDate] ,[ChangeDate] )"
					+ " values(? , ? , ? , ?  )  ;"  ;
		int i = 0;
		try {
			prepared = connection.prepareStatement(sql);
			for (i = 0; i < list.size(); i++) {
				SORDetail bean = list.get(i);
				saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
				cfmDate = bean.getCfmDate();
				int index = 1;
				prepared = this.sshUtl.setSqlDate(prepared, cfmDate, index); 
				prepared.setTimestamp(index, dateTime); 
				prepared.setString(index, bean.getSaleOrder()); 
				prepared.setString(index, saleLine); 
				prepared.setString(index, bean.getSaleOrder()); 
				prepared.setString(index, saleLine); 
				prepared = this.sshUtl.setSqlDate(prepared, cfmDate, index); 
				prepared.setTimestamp(index, dateTime); 
				prepared.addBatch();
			}
			prepared.executeBatch();
			prepared.close();
		} catch (SQLException e) {
			System.err.println("insertLabNoDetail" + e.getMessage());
			iconStatus = "E";
		}finally {
			//this.database.close();
		}
		return iconStatus;
	}
}
