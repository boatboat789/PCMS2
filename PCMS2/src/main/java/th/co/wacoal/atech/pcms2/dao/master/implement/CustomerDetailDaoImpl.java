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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.CustomerDao;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class CustomerDetailDaoImpl implements CustomerDao {
	private BeanCreateService bcModel = new BeanCreateService();
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private Database database;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public CustomerDetailDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
	}

	@Override
	public ArrayList<CustomerDetail> getCustomerDetail()
	{
		ArrayList<CustomerDetail> list = null;
		String sql = ""
				+ " SELECT TOP (1000) [Id]\r\n"
				+ "      ,[CustomerNo]\r\n"
				+ "      ,[CustomerNoWOZero]\r\n"
				+ "      ,[CustomerName]\r\n"
				+ "      ,[CustomerShortName]\r\n"
				+ "      ,[CustomerType]\r\n"
				+ "      ,[CustomerGroup]\r\n"
				+ "      ,[DistChannel]\r\n"
				+ "      ,[IsSabina]\r\n"
				+ "      ,[ChangeDate]\r\n"
				+ "      ,[CreateDate]\r\n"
				+ "      ,[SyncDate]\r\n"
				+ "  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
				+ "";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCustomerDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<CustomerDetail> getCustomerNameForOption()
	{
		ArrayList<CustomerDetail> list = null;
		String sql = ""
				+ " SELECT distinct \r\n"
				+ "       [CustomerName] "
				+ "  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
				+ "  ORDER BY [CustomerName] "
				+ "";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCustomerDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<CustomerDetail> getCustomerShortNameForOption()
	{
		ArrayList<CustomerDetail> list = null;
		String sql = ""
				+ " SELECT distinct \r\n"
				+ "       [CustomerShortName] "
				+ "  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
				+ "  ORDER BY [CustomerShortName] "
				+ "";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCustomerDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<CustomerDetail> getCustomerNameForOption(ArrayList<ConfigCustomerUserDetail> poList)
	{
		ArrayList<CustomerDetail> list = null;
		ConfigCustomerUserDetail bean = poList.get(0);
		String custNo = bean.getCustomerNo();
		String where = " where 1 = 1 AND ( [CustomerNo] IN ( ";
		String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i ++ ) {
			where += " '" + array[i] + "' ";
			if (i != array.length-1) {
				where += " , \r\n";
			}
		}
		where += " ) \r\n";
		where += " ) \r\n";
		String sql = ""
				+ " SELECT distinct \r\n"
				+ "       [CustomerName] "
				+ "  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
				+ where
				+ "  ORDER BY [CustomerName] "
				+ "";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCustomerDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<CustomerDetail> getCustomerShortNameForOption(ArrayList<ConfigCustomerUserDetail> poList)
	{
		ArrayList<CustomerDetail> list = null;
		ConfigCustomerUserDetail bean = poList.get(0);
		String custNo = bean.getCustomerNo();
		String where = " where 1 = 1 AND ( [CustomerNo] IN ( ";
		String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i ++ ) {
			where += " '" + array[i] + "' ";
			if (i != array.length-1) {
				where += " , \r\n";
			}
		}
		where += " ) \r\n";
		where += " ) \r\n";
		String sql = ""
				+ " SELECT distinct [CustomerShortName] "
				+ "  FROM [PCMS].[dbo].[CustomerDetail]\r\n"
				+ where
				+ "  ORDER BY [CustomerShortName] "
				+ "";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCustomerDetail(map));
		}
		return list;
	}

	@Override
	public String upsertCustomerDetail(ArrayList<CustomerDetail> paList)
	{
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
				+ " if @rc = 0 "
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

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);

			int index = 1;
			for (CustomerDetail bean : paList) {
				index = 1;
				;
				prepared.setString(index ++ , bean.getCustomerNoWOZero());
				prepared.setString(index ++ , bean.getCustomerName());
				prepared.setString(index ++ , bean.getCustomerShortName());
				prepared.setString(index ++ , bean.getCustomerType());
				prepared.setString(index ++ , bean.getDistChannel());
				prepared.setBoolean(index ++ , bean.isSabina());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
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
//this.sshUtl.setSqlDate(prepared, bean.get , index++); 
//				prepared.setTimestamp(index++, new Timestamp(time));
//this.sshUtl.setSqlBigDecimal(prepared, bean.get , index++); 
				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.addBatch();
			}
			prepared.executeBatch();
			prepared.close();
		} catch (SQLException e) {
			e.printStackTrace();
			iconStatus = "E";
		}  finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return iconStatus;
	}
}
