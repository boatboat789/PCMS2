package dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Repository;

import dao.master.FromSapReceipeDao;
import entities.ReceipeDetail; 
import entities.erp.atech.FromErpReceipeDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapReceipeDaoImpl implements FromSapReceipeDao {
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
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
	@Override
	public String upsertFromSapReceipeDetail(ArrayList<FromErpReceipeDetail> paList)
	{
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();

		String iconStatus = "I";
		String sql = "-- Update if the record exists\r\n"
				+ "UPDATE [dbo].[FromSapReceipe]\r\n"
				+ "SET \r\n"
				+ "    [LotNo] = ?,\r\n"
				+ "    [DataStatus] = ?,\r\n"
				+ "    [ChangeDate] = ? \r\n"
				+ "      ,[SyncDate] =  ?\r\n"

				+ "WHERE \r\n"
				+ "    [ProductionOrder] = ?  " 
				+ "    ;\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "    PRINT @rc;\r\n"
				+ "ELSE \r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[FromSapReceipe] (\r\n"
				+ "        [ProductionOrder]  ,[LotNo] ,[DataStatus] ,[ChangeDate] ,[CreateDate]\r\n"
				+ "      ,[SyncDate] \r\n"
				+ "    ) "
				+ "    VALUES (\r\n"
				+ "		?, ?, ?, ?, ? " 
				+ "		, ? "
				+ "    ); "
				+ ";";
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql);
			for (FromErpReceipeDetail bean : paList) {
				index = 1;
				prepared.setString(index ++ , bean.getLotNo());
				prepared.setString(index ++ , bean.getDataStatus());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.setString(index ++ , bean.getProductionOrder()); 

				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getLotNo());
				prepared.setString(index ++ , bean.getDataStatus()); 
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );

				prepared.addBatch(); 
			}
			prepared.executeBatch();
			prepared.close();
		} catch (SQLException e) {
			System.err.println(e);
			iconStatus = "E";
		} finally {
			// this.database.close();
		}
		return iconStatus;
	}
}
