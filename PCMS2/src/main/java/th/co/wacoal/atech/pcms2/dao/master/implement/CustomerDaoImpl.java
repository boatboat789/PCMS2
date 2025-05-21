package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.CustomerDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class CustomerDaoImpl implements CustomerDao {
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

	@Autowired
	public CustomerDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String upsertCustomerDetail(ArrayList<CustomerDetail> paList)
	{
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection(); 
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();

		String iconStatus = "I";
		String sql = "-- Update if the record exists\r\n"
				+ " "
				+ "UPDATE [dbo].[CustomerDetail]\r\n"
				+ "SET \r\n"
				+ "    [CustomerNoWOZero] =  ? ,"
				+ "    [CustomerName] = ?,\r\n"
				+ "    [CustomerShortName] = ?,\r\n"
				+ "    [CustomerType] = ?,\r\n"
				+ "    [DistChannel] = ?,\r\n"
				+ "    [IsSabina] = ? , "
				+ "    [ChangeDate]= ? ,\r\n"
				+ "    [SyncDate]= ? \r\n"
				+ "WHERE \r\n"
				+ "    [CustomerNo] = ?;\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "   SELECT 1;\r\n"
				+ "ELSE \r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[CustomerDetail] (\r\n"
				+ "        [CustomerNo]"
				+ "       ,[CustomerNoWOZero] ,[CustomerName] ,[CustomerShortName] ,[CustomerType],[DistChannel]"
				+ "       ,[IsSabina]  \r\n"
				+ "       ,[ChangeDate] ,[CreateDate],[SyncDate]\r\n"
				+ "    ) VALUES (\r\n"
				+ "		?,"
				+ "     ?, ?, ?, ?, ?, "
				+ "     ?, "
				+ "		?, ?, ? "// 10
				+ "    ); "
				+ ";";
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql);
			for (CustomerDetail bean : paList) {
				index = 1; ;
				prepared.setString(index ++ , bean.getCustomerNoWOZero());
				prepared.setString(index ++ , bean.getCustomerName());
				prepared.setString(index ++ , bean.getCustomerShortName());
				prepared.setString(index ++ , bean.getCustomerType());
				prepared.setString(index ++ , bean.getDistChannel());
				prepared.setBoolean(index ++ , bean.isSabina());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.setString(index ++ , bean.getCustomerNo());
				
				prepared.setString(index ++ , bean.getCustomerNo());
				prepared.setString(index ++ , bean.getCustomerNoWOZero());
				prepared.setString(index ++ , bean.getCustomerName());
				prepared.setString(index ++ , bean.getCustomerShortName());
				prepared.setString(index ++ , bean.getCustomerType());
				prepared.setString(index ++ , bean.getDistChannel());
				prepared.setBoolean(index ++ , bean.isSabina());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared = this.sshUtl.setSqlDate(prepared, bean.get , index++); 
//				prepared.setTimestamp(index++, new Timestamp(time));
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.get , index++); 
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.addBatch();
			}
			prepared.executeBatch();
			prepared.close();
		} catch (SQLException e) {
			e.printStackTrace();
			iconStatus = "E";
		} finally {
			// this.database.close();
		}
		return iconStatus;
	} 
//	   public static String addLeadingZeros(String str, int desiredLength) {
//	        return String.format("%0" + desiredLength + "s", str);
//	    }
}
