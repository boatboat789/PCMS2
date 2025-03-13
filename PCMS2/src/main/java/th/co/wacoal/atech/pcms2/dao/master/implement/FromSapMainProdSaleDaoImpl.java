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

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainProdSaleDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
//import model.BeanCreateModฉel;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapMainProdSaleDaoImpl implements FromSapMainProdSaleDao {
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
	public FromSapMainProdSaleDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String upsertFromSapMainProdSaleDetail(ArrayList<FromErpMainProdSaleDetail> paList)
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
				+ "IF ? = 'X'\r\n"
				+ "BEGIN\r\n"
				+ "    UPDATE [dbo].[FromSapMainProdSale]\r\n"
				+ "    SET [DataStatus] = 'X'\r\n"
				+ "    WHERE [ProductionOrder] = ?;\r\n"
				+ "END\r\n"
				+ "ELSE \r\n"
				+ "BEGIN\r\n"
				+ "UPDATE [dbo].[FromSapMainProdSale]\r\n"
				+ "SET \r\n"
				+ "    [Volumn] = ?,\r\n"
				+ "    [DataStatus] = ?,\r\n"
				+ "    [ChangeDate] = ? \r\n"
				+ "      ,[SyncDate] =  ?\r\n"

				+ "WHERE \r\n"
				+ "    [ProductionOrder] = ? and"
				+ "    [SaleOrder] = ? and\r\n"
				+ "    [SaleLine] = ? \r\n"
				+ "    ;\r\n"
				+ "END\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "   SELECT 1;\r\n"
				+ "ELSE \r\n"
				+ "BEGIN\r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[FromSapMainProdSale] (\r\n"
				+ "        [ProductionOrder]  ,[SaleOrder] ,[SaleLine] ,[Volumn] ,[DataStatus]\r\n"
				+ "      ,[ChangeDate] ,[CreateDate]\r\n"
				+ "      ,[SyncDate] \r\n"
				+ "    ) "
				+ "    VALUES (\r\n"
				+ "?, ?, ?, ?, ?, "
				+ "?, ?\r\n"
				+ ", ? "
				+ "    ); "
				+ "END ";
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql);
			for (FromErpMainProdSaleDetail bean : paList) {
				index = 1;
				prepared.setString(index++, bean.getDataStatus()   );
				prepared.setString(index++, bean.getProductionOrder()    );
				
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getVolumn(), index ++ );
				prepared.setString(index ++ , bean.getDataStatus());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());

				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getVolumn(), index ++ );
				prepared.setString(index ++ , bean.getDataStatus());
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
//			System.err.println(e);
			 e.printStackTrace();
			iconStatus = "E";
		} finally {
			// this.database.close();
		}
		return iconStatus;
	}
}
