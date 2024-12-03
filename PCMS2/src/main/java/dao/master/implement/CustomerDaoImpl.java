	package dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import dao.master.CustomerDao;
import entities.erp.atech.CustomerDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class CustomerDaoImpl implements  CustomerDao{
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

	public CustomerDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public String upsertCustomerDetail( ArrayList<CustomerDetail> paList ) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection(); 
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		
		String iconStatus = "I";
		String sql =
				  "-- Update if the record exists\r\n"
				  + " "
				  + "UPDATE [dbo].[CustomerDetail]\r\n"
				  + "SET \r\n"  
				  + "    [CustomerName] = ?,\r\n"
				  + "    [CustomerShortName] = ?,\r\n"
				  + "    [CustomerType] = ?,\r\n"  
				  + "    [DistChannel] = ?,\r\n"
				  + "    [ChangeDate]= ? \r\n" 
				  + "WHERE \r\n"
				  + "    [CustomerNo] = ?;\r\n" 
				  + "-- Check if rows were updated\r\n"
				  + "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				  + "IF @rc <> 0\r\n"
				  + "    PRINT @rc;\r\n"
				  + "ELSE \r\n"
				  + "    -- Insert if no rows were updated\r\n"
				  + "    INSERT INTO [dbo].[CustomerDetail] (\r\n"
				  + "        [CustomerNo] ,[CustomerName] ,[CustomerShortName] ,[CustomerType],[DistChannel] \r\n"
				  + "       ,[ChangeDate] ,[CreateDate]\r\n"
				  + "    ) VALUES (\r\n"
				  + "		?, ?, ?, ?, ?, "
				  + "		?, ? "//10  
				  + "    ); "
				+ ";"  ;
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql); 
			for(CustomerDetail bean : paList) {
				index = 1;
				prepared.setString(index++, bean.getCustomerName()   );
				prepared.setString(index++, bean.getCustomerShortName()  );
				prepared.setString(index++, bean.getCustomerType()  ); 
				prepared.setString(index++, bean.getDistChannel() );
				prepared.setTimestamp(index++, new Timestamp(time));
				prepared.setString(index++, bean.getCustomerNo()  );
				

				prepared.setString(index++, bean.getCustomerNo()  );
				prepared.setString(index++, bean.getCustomerName()   );
				prepared.setString(index++, bean.getCustomerShortName()  );
				prepared.setString(index++, bean.getCustomerType()  ); 
				prepared.setString(index++, bean.getDistChannel() );
				prepared.setTimestamp(index++, new Timestamp(time));
				prepared.setTimestamp(index++, new Timestamp(time));
//				prepared = this.sshUtl.setSqlDate(prepared, bean.get , index++); 
//				prepared.setTimestamp(index++, new Timestamp(time));
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.get , index++); 
				prepared.addBatch();
			}
			prepared.executeBatch();
			prepared.close(); 
		} catch (SQLException e) {
			System.err.println(e); 
			iconStatus = "E";
		}finally {
			//this.database.close();
		}
		return iconStatus;
	}
  
}
