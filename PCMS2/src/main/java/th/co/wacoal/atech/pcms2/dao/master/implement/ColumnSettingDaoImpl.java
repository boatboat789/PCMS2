package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.ColumnSettingDao;
import th.co.wacoal.atech.pcms2.entities.ColumnHiddenDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class ColumnSettingDaoImpl implements ColumnSettingDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public ColumnSettingDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<ColumnHiddenDetail> getColumnVisibleDetail(String user)
	{
		ArrayList<ColumnHiddenDetail> list = null;
		String sql = " SELECT distinct \r\n"
				+ "		[EmployeeId] ,[ColVisibleDetail] ,[ColVisibleSummary]\r\n"
				+ " FROM [PCMS].[dbo].[ColumnSetting] \r\n "
				+ " where [EmployeeId] = '"
				+ user
				+ "' ";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genColumnHiddenDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<ColumnHiddenDetail> upsertColumnSettingDetail(String user, ColumnHiddenDetail pd)
	{
		String colName = pd.getColVisibleDetail();
		ArrayList<ColumnHiddenDetail> list = new ArrayList<>();
		ColumnHiddenDetail bean = new ColumnHiddenDetail();
		String sql = " UPDATE [PCMS].[dbo].[ColumnSetting] "
				+ " 	SET [ColVisibleDetail] = ?  "
				+ " 	WHERE [EmployeeId]  = ? "
				+ " declare  @rc int = @@ROWCOUNT " // 56
				+ " if @rc <> 0 "
				+ " 	print @rc "
				+ " else "
				+ " 	INSERT INTO [PCMS].[dbo].[ColumnSetting]	 "
				+ " 		([EmployeeId] ,[ColVisibleDetail])"// 55
				+ " 	values(? , ? )  ;";
		try (Connection connection = database.getConnection(); PreparedStatement prepared = connection.prepareStatement(sql)) {
			prepared.setString(1, colName);
			prepared.setString(2, user);
			prepared.setString(3, user);
			prepared.setString(4, colName);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (Exception e) {
//			System.err.println("saveColSettingToServer"+e.getMessage());
			e.printStackTrace();
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// this.database.close();
		}
		list.add(bean);
		return list;
	}

	@Override
	public ArrayList<ColumnHiddenDetail> upsertColumnVisibleSummary(String user, ColumnHiddenDetail pd)
	{
		String colName = pd.getColVisibleSummary();
		ArrayList<ColumnHiddenDetail> list = new ArrayList<>();
		ColumnHiddenDetail bean = new ColumnHiddenDetail();
		String sql = " UPDATE [PCMS].[dbo].[ColumnSetting] "
				+ " 	SET [ColVisibleSummary] = ?  "
				+ " 	WHERE [EmployeeId]  = ? "
				+ " declare  @rc int = @@ROWCOUNT " // 56
				+ " if @rc <> 0 "
				+ " 	print @rc "
				+ " else "
				+ " 	INSERT INTO [PCMS].[dbo].[ColumnSetting]	 "
				+ " 		([EmployeeId] ,[ColVisibleSummary])"// 55
				+ " 	values(? , ? )  ;";
		try (Connection connection = database.getConnection(); PreparedStatement prepared = connection.prepareStatement(sql)) {
			connection.prepareStatement(sql);
			prepared.setString(1, colName);
			prepared.setString(2, user);
			prepared.setString(3, user);
			prepared.setString(4, colName);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println("saveColSettingToServer"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// this.database.close();
		}
		list.add(bean);
		return list;
	}

}
