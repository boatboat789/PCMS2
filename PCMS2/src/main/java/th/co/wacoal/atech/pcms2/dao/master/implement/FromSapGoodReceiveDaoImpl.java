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

import th.co.wacoal.atech.pcms2.dao.master.FromSapGoodReceiveDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
//import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapGoodReceiveDaoImpl implements FromSapGoodReceiveDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
//	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSapGoodReceiveDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String upsertFromSapGoodReceiveDetail(ArrayList<FromErpGoodReceiveDetail> paList)
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
				+ "UPDATE [dbo].[FromSapGoodReceive]\r\n"
				+ "SET \r\n"
				+ "    [SaleOrder] = ?,\r\n"
				+ "    [SaleLine] = ?,\r\n"
				+ "    [Grade] = ?,\r\n" 
				+ "    [QuantityKG] = ?,\r\n"
				+ "    [QuantityYD] = ?,\r\n"
				+ "    [QuantityMR] = ?,\r\n"
				+ "    [PriceSTD] = ?,\r\n"
//				+ "    [DataStatus] = ?,\r\n"
				+ "    [ChangeDate] = ? \r\n"
				+ "      ,[SyncDate] =  ?\r\n"
				+ "WHERE \r\n"
				+ "    [ProductionOrder] = ? and"
				+ "    [RollNumber] = ? \r\n"
				+ "    ;\r\n"
				+ "\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "   SELECT 1;\r\n"
				+ "ELSE \r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[FromSapGoodReceive] (\r\n"
				+ "        [ProductionOrder]\r\n"
				+ "      ,[SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[Grade]\r\n"
				+ "      ,[RollNumber]\r\n" 
				+ "      ,[QuantityKG]\r\n"
				+ "      ,[QuantityYD]\r\n"
				+ "      ,[QuantityMR]\r\n"
				+ "      ,[PriceSTD]\r\n"
//				+ "      ,[DataStatus]\r\n"
				
				+ "      ,[ChangeDate]\r\n"
				+ "      ,[CreateDate]\r\n"
				+ "      ,[SyncDate] \r\n"
				+ "    ) VALUES (\r\n"
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, "
//				+ "?, "// 10
				+ "?, "
				+ "?,"
				+ "? "
				+ "    ); "
				+ ";";
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql);
			for (FromErpGoodReceiveDetail bean : paList) {
				index = 1;

				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared.setString(index ++ , bean.getGrade());

				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getPriceSTD(), index ++ );
//				prepared.setString(index ++ , bean.getDataStatus());

				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getRollNumber());

				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared.setString(index ++ , bean.getGrade());
				prepared.setString(index ++ , bean.getRollNumber());

				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getPriceSTD(), index ++ );
//				prepared.setString(index ++ , bean.getDataStatus());

				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
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
		} finally {
			// this.database.close();
		}
		return iconStatus;
	}
}
