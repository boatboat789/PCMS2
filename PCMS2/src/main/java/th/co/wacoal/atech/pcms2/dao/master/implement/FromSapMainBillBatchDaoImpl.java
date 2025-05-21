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

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainBillBatchDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainBillBatchDetail;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
//import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapMainBillBatchDaoImpl implements FromSapMainBillBatchDao {
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
	public FromSapMainBillBatchDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String upsertFromSapMainBillBatchDetail(ArrayList<FromErpMainBillBatchDetail> paList)
	{
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String iconStatus = "I";
		String sql = "-- Update if the record exists\r\n"
				+ "IF ? = 'X'\r\n"
				+ "BEGIN\r\n"
				+ "    UPDATE [dbo].[FromSapMainBillBatch]\r\n"
				+ "    SET [DataStatus] = 'X'\r\n"
				+ "    WHERE [SaleOrder] = ?;\r\n"
				+ "END\r\n"
				+ "ELSE \r\n"
				+ "BEGIN\r\n"
				+ "UPDATE [dbo].[FromSapMainBillBatch]\r\n"
				+ "SET \r\n"
				+ "    [LotShipping] = ?,\r\n"
				+ "    [ProductionOrder] = ?,\r\n"
				+ "    [Grade] = ?,\r\n"
				+ "    [QuantityKG] = ?,\r\n"
				+ "    [QuantityYD] = ?,\r\n"
				+ "    [QuantityMR] = ?,\r\n" 
				+ "    [LotNo] = ?,\r\n"
				+ "    [DataStatus] = ?,\r\n"
				+ "    [ChangeDate] = ? \r\n"
				+ "   , [SyncDate] =  ?\r\n"
				+ "WHERE \r\n"
				+ "    [BillDoc] = ? and\r\n"
				+ "    [BillItem] = ? and\r\n"
				+ "    [SaleOrder] = ? and\r\n"
				+ "    [SaleLine] = ? and\r\n"
				+ "    [RollNumber] = ? \r\n"
				+ "    ;\r\n"
				+ "\r\n"
				+ "END\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "   SELECT 1;\r\n"
				+ "ELSE \r\n"
				+ "BEGIN\r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[FromSapMainBillBatch] (\r\n"
				+ "     [BillDoc]  ,[BillItem] ,[LotShipping] ,[ProductionOrder] ,[SaleOrder]\r\n"
				+ "    ,[SaleLine] ,[Grade] ,[RollNumber] ,[QuantityKG] ,[QuantityYD]\r\n"
				+ "    ,[QuantityMR] ,[LotNo] ,[DataStatus] ,[ChangeDate],[CreateDate]\r\n"
				+ "      ,[SyncDate] \r\n"
				+ "    \r\n"
				+ "    ) VALUES (\r\n"
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, " 
				+ "?, ?, ?, ?, ?, "
				+ "? " 
				+ "    ); "
				+ "END ";
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql);
			for (FromErpMainBillBatchDetail bean : paList) {
				index = 1;
				prepared.setString(index++, bean.getDataStatus()   );
				prepared.setString(index++, bean.getSaleOrder()    );
				
//				prepared.setString(index ++ , bean.getLotShipping());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getGrade());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );

				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ ); 
				prepared.setString(index ++ , bean.getLotNo());
				prepared.setString(index ++ , bean.getDataStatus());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );

				prepared.setString(index ++ , bean.getBillDoc());
				prepared.setString(index ++ , bean.getBillItem());
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared.setString(index ++ , bean.getRollNumber());

				prepared.setString(index ++ , bean.getBillDoc());
				prepared.setString(index ++ , bean.getBillItem());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getSaleOrder());

				prepared.setString(index ++ , bean.getSaleLine());
				prepared.setString(index ++ , bean.getGrade());
				prepared.setString(index ++ , bean.getRollNumber());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );

				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ );
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
//			e.printStackTrace();
			 e.printStackTrace();
			iconStatus = "E";
		} finally {
			// this.database.close();
		}
		return iconStatus;
	}
}
