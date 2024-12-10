	package dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import dao.master.FromSapMainBillBatchDao;
import entities.erp.atech.FromErpMainBillBatchDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapMainBillBatchDaoImpl implements  FromSapMainBillBatchDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New 
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public FromSapMainBillBatchDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	} 
	@Override
	public String upsertFromSapMainBillBatchDetail( ArrayList<FromErpMainBillBatchDetail> paList ) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();  
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime(); 
		String iconStatus = "I";
		String sql =
				  "-- Update if the record exists\r\n"
				  + "UPDATE [dbo].[FromSapGoodReceive]\r\n"
				  + "SET \r\n" 
				  + "    [LotShipping] = ?,\r\n"
				  + "    [ProductionOrder] = ?,\r\n"
				  + "    [Grade] = ?,\r\n"
				  + "    [QuantityKG] = ?,\r\n"
				  + "    [QuantityYD] = ?,\r\n"
				  + "    [QuantityMR] = ?,\r\n" 
				  + "    [ProductionOrder] = ?,\r\n" 
				  + "    [LotNo] = ?,\r\n"  
				  + "    [DataStatus] = ?,\r\n" 
				  + "    [ChangeDate] = ? \r\n"  
				  + "WHERE \r\n"
				  + "    [BillDoc] = ? and\r\n"
				  + "    [BillItem] = ? and\r\n"
				  + "    [SaleOrder] = ? and\r\n"
				  + "    [SaleLine] = ? and\r\n"
				  + "    [RollNumber] = ? \r\n"
				  + "    ;\r\n"
				  + "\r\n"
				  + "-- Check if rows were updated\r\n"
				  + "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				  + "IF @rc <> 0\r\n"
				  + "    PRINT @rc;\r\n"
				  + "ELSE \r\n"
				  + "    -- Insert if no rows were updated\r\n"
				  + "    INSERT INTO [dbo].[FromSapGoodReceive] (\r\n"
				  + "     [BillDoc]  ,[BillItem] ,[LotShipping] ,[ProductionOrder] ,[SaleOrder]\r\n"
				  + "    ,[SaleLine] ,[Grade] ,[RollNumber] ,[QuantityKG] ,[QuantityYD]\r\n"
				  + "    ,[QuantityMR] ,[LotNo] ,[DataStatus] ,[ChangeDate],[CreateDate]\r\n"
				  + "    \r\n"
				  + "    ) VALUES (\r\n"
				  + "?, ?, ?, ?, ?, "
				  + "?, ?, ?, ?, ?, "//10 
				  + "?, ?, ?, ?, ?, "//10  
				  + "    ); "
				+ ";"  ;
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql); 
			for(FromErpMainBillBatchDetail bean : paList) {
				index = 1; 
				prepared.setString(index++, bean.getLotShipping()   );
				prepared.setString(index++, bean.getProductionOrder()   );
				prepared.setString(index++, bean.getGrade()   ); 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG() , index++);  
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD() , index++); 
				 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR() , index++);  
				prepared.setString(index++, bean.getLotNo()    );
				prepared.setString(index++, bean.getDataStatus()   );  
				prepared.setTimestamp(index++, new Timestamp(time)); 
				 
				prepared.setString(index++, bean.getBillDoc()   );
				prepared.setString(index++, bean.getBillItem()  );
				prepared.setString(index++, bean.getSaleOrder()   ); 
				prepared.setString(index++, bean.getSaleLine()   );  
				prepared.setString(index++, bean.getRollNumber()  );  
				
				
  
				prepared.setString(index++, bean.getBillDoc()   );
				prepared.setString(index++, bean.getBillItem()  );
				prepared.setString(index++, bean.getLotShipping()   );
				prepared.setString(index++, bean.getProductionOrder()   );
				prepared.setString(index++, bean.getSaleOrder()   );
				
				prepared.setString(index++, bean.getSaleLine()   );  
				prepared.setString(index++, bean.getGrade()   ); 
				prepared.setString(index++, bean.getRollNumber()  );  
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG() , index++);  
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD() , index++); 
				 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR() , index++);  
				prepared.setString(index++, bean.getLotNo()    );
				prepared.setString(index++, bean.getDataStatus()   );  
				prepared.setTimestamp(index++, new Timestamp(time));
				prepared.setTimestamp(index++, new Timestamp(time));
				prepared.addBatch();
//				prepared.setString(index++, bean.get    );
//				prepared = this.sshUtl.setSqlDate(prepared, bean.get , index++); 
//				prepared.setTimestamp(index++, new Timestamp(time));
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.get , index++); 
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
