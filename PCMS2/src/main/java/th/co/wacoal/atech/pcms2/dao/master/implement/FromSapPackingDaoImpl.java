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

import th.co.wacoal.atech.pcms2.dao.master.FromSapPackingDao;
import th.co.wacoal.atech.pcms2.entities.PackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapPackingDaoImpl implements FromSapPackingDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	private String selectPacking = "      fsp.[Id]\r\n"
			+ "      ,[ProductionOrder]\r\n"
			+ "      ,[PostingDate]\r\n"
			+ "      ,[Quantity]\r\n"
			+ "      ,[RollNo]\r\n"
			+ "      ,[Status]\r\n"
			+ "	  ,insorder.[RollupNote] as [Status]\r\n"
			+ "      ,[QuantityKG]\r\n"
			+ "      ,[Grade]\r\n"
			+ "      ,[No]\r\n"
			+ "      ,[DataStatus]\r\n"
			+ "      ,[QuantityYD]\r\n"
			+ "      ,fsp.[ChangeDate]\r\n"
			+ "      ,[CreateDate] \r\n ";;

	@Autowired
	public FromSapPackingDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder)
	{
		ArrayList<PackingDetail> list = null;
		String where = " where  ";
		where += " " + " fsp.ProductionOrder = '" + prodOrder + "'  and \r\n" + " fsp.[DataStatus] = 'O' \r\n";
		String sql = ""
				+ " SELECT DISTINCT  \r\n"
				+ this.selectPacking
				+ "  from [PCMS].[dbo].[FromSapPacking] as fsp\r\n"
				+ "  left join [InspectSystem].[dbo].[InspectOrders] as insorder on fsp.[ProductionOrder] = insorder.[PrdNumber] \r\n "
				+ where;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPackingDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapPackingDetail(ArrayList<FromErpPackingDetail> paList)
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
				+ "    [PostingDate] = ?,\r\n"
				+ "    [Quantity] = ?,\r\n"
				+ "    [QuantityKG] = ?,\r\n"
				+ "    [Grade] = ?, \r\n"
				+ "    [No] = ? ,\r\n"
				+ "    [QuantityYD] = ?, \r\n"
				+ "    [DataStatus] = ?, \r\n"
				+ "    [ChangeDate]= ? \r\n"
				+ "      ,[SyncDate] =  ?\r\n"
				+ "WHERE \r\n"
				+ "    [ProductionOrder] = ? AND\r\n"
				+ "    [RollNo] = ? ;\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "    PRINT @rc;\r\n"
				+ "ELSE \r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[FromSapCFM] (\r\n"
				+ "       [ProductionOrder] ,[PostingDate] ,[Quantity] ,[RollNo] "
//				  + ",[Status]\r\n"
				+ "      ,[QuantityKG] ,[Grade] ,[No] ,[QuantityYD],[DataStatus]\r\n"
				+ "       ,[ChangeDate] ,[CreateDate]\r\n"
				+ "      ,[SyncDate] \r\n"
				+ "    ) VALUES (\r\n"
				+ "		?, ?, ?, ?, "
//				  + " ?, " เอกออก หยิบจาก Inspect แทน
				+ "		?, ?, ?, ?, ?, "
				+ "		?, ?  "// 10
				+ ", ? "
				+ "    ); "
				+ ";";
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql);
			for (FromErpPackingDetail bean : paList) {
				index = 1;
				prepared = this.sshUtl.setSqlDate(prepared, bean.getPostingDate(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantity(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				prepared.setString(index ++ , bean.getGrade());
				prepared.setString(index ++ , bean.getNo());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
				prepared.setString(index ++ , bean.getDataStatus());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );

				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getRollNo());

				prepared.setString(index ++ , bean.getProductionOrder());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getPostingDate(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantity(), index ++ );
				prepared.setString(index ++ , bean.getRollNo());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				prepared.setString(index ++ , bean.getGrade());
				prepared.setString(index ++ , bean.getNo());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
				prepared.setString(index ++ , bean.getDataStatus());
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
