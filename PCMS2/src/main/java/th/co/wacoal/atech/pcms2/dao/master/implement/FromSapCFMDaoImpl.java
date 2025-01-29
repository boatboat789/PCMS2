package th.co.wacoal.atech.pcms2.dao.master.implement;

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

import th.co.wacoal.atech.pcms2.dao.master.FromSapCFMDao;
import th.co.wacoal.atech.pcms2.entities.CFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapCFMDaoImpl implements FromSapCFMDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSapCFMDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<CFMDetail> getFromSapCFMDetailByProductionOrder(String prodOrder)
	{
		ArrayList<CFMDetail> list = null;
		String where = " where  ";
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = " SELECT DISTINCT  \r\n"
				+ "   [Id]"
				+ "   ,[ProductionOrder],[CFMNo],[CFMNumber]\r\n"
				+ "   ,[CFMSendDate],[CFMAnswerDate],[CFMStatus]\r\n"
				+ "   ,[CFMRemark],[Da],[Db],[L]\r\n"
				+ "   ,[ST],[SaleOrder]"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "			WHEN 0 THEN ''  \r\n"
				+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "			END AS [SaleLine] "
				+ "   ,[CFMCheckLab]\r\n"
				+ "   ,[CFMNextLab],[CFMCheckLot],[CFMNextLot]\r\n"
				+ "   ,[NextLot],[SOChange],[SOChangeQty]\r\n"
				+ "   ,[SOChangeUnit],[RollNo],[RollNoRemark]\r\n"
				+ "   ,a.[DataStatus]\r\n"
				+ "   ,[DE]\r\n"
				+ " from [PCMS].[dbo].[FromSapCFM] as a \r\n "
				+ where
				+ " Order by [CFMNo]";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCFMDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapCFMDetail(ArrayList<FromErpCFMDetail> paList)
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
				+ " "
				+ "UPDATE [dbo].[FromSapCFM]\r\n"
				+ "SET \r\n"
				+ "    [CFMNumber] = ?,\r\n"
				+ "    [CFMSendDate] = ?,\r\n"
				+ "    [CFMAnswerDate] = ?,\r\n"
				+ "    [CFMStatus] = ? ,\r\n"
				+ "    [CFMRemark] = ? ,\r\n"
				+ "    [SaleOrder] = ? ,\r\n"
				+ "    [SaleLine] = ? ,\r\n"
				+ "    [SOChange] = ? ,\r\n"
				+ "    [NextLot] = ? ,\r\n"
				+ "    [SOChange] = ? ,\r\n"
				+ "    [SOChangeQty] = ? ,\r\n"
				+ "    [SOChangeUnit] = ? ,\r\n"
				+ "    [RollNo] = ? ,\r\n"
				+ "    [RollNoRemark] = ? ,\r\n"
				+ "    [DataStatus] = ? ,\r\n"
				+ "    [ChangeDate]= ? \r\n"
				+ "      ,[SyncDate] =  ?\r\n"

				+ "WHERE \r\n"
				+ "    [ProductionOrder] = ? AND\r\n"
				+ "    [CFMNo] = ? ;\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "    PRINT @rc;\r\n"
				+ "ELSE \r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[FromSapCFM] (\r\n"
				+ "        [ProductionOrder] ,[CFMNo] ,[CFMNumber] ,[CFMSendDate] ,[CFMAnswerDate]\r\n"
				+ "      ,[CFMStatus] ,[CFMRemark] ,[SaleOrder] ,[SaleLine] ,[NextLot]\r\n"
				+ "      ,[SOChange] ,[SOChangeQty] ,[SOChangeUnit] ,[RollNo] ,[RollNoRemark]"
				+ "      ,[DataStatus] ,[ChangeDate] ,[CreateDate],[SyncDate]\r\n"
				+ "    ) VALUES (\r\n"
				+ "		?, ?, ?, ?, ?, "
				+ "		?, ?, ?, ?, ?, "
				+ "		?, ?, ?, ?, ?, "
				+ "		?, ?, ?, ? "// 10
				+ "    ); "
				+ ";";
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql);
			for (FromErpCFMDetail bean : paList) {
				index = 1;

				prepared.setString(index ++ , bean.getCfmNumber());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCfmSendDate(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCfmAnswerDate(), index ++ );
				prepared.setString(index ++ , bean.getCfmStatus());
				prepared.setString(index ++ , bean.getCfmRemark());
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared.setString(index ++ , bean.getNextLot());
				prepared.setString(index ++ , bean.getSoChange());
				prepared.setString(index ++ , bean.getSoChangeQty());
				prepared.setString(index ++ , bean.getSoChangeUnit());
				prepared.setString(index ++ , bean.getRollNo());
				prepared.setString(index ++ , bean.getRollNoRemark());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getCfmNo());

				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getCfmNo());
				prepared.setString(index ++ , bean.getCfmNumber());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCfmSendDate(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCfmAnswerDate(), index ++ );
				prepared.setString(index ++ , bean.getCfmStatus());
				prepared.setString(index ++ , bean.getCfmRemark());
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared.setString(index ++ , bean.getNextLot());
				prepared.setString(index ++ , bean.getSoChange());
				prepared.setString(index ++ , bean.getSoChangeQty());
				prepared.setString(index ++ , bean.getSoChangeUnit());
				prepared.setString(index ++ , bean.getRollNo());
				prepared.setString(index ++ , bean.getRollNoRemark());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.setString(index++, bean.get    );
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
		} finally {
			// this.database.close();
		}
		return iconStatus;
	}

}
