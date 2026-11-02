package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.UsersDao;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class UsersDaoImpl implements UsersDao {
	private BeanCreateService bcModel = new BeanCreateService();
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtil = new SqlStatementHandler();
	private JdbcTemplate jdbc;
	private String message;

	public DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String select = ""
			+ "       A.[UserId]\r\n"
			+ "      ,B.[PermitId]\r\n"
			+ "      ,B.[Responsible]\r\n"
			+ "      ,A.[IsClosed]\r\n"
			+ "      ,A.[IsLocalUser]\r\n"
			+ "      ,A.[Firstname]\r\n"
			+ "      ,A.[Lastname]\r\n"
			+ "      ,A.[ChangeBy]\r\n"
			+ "      ,A.[ChangeDate]\r\n"
			+ "      ,A.[IsCustomer]\r\n"
			+ "      ,A.[RegistBy]\r\n"
			+ "      ,A.[RegistDate]\r\n"
			+ "      ,A.[LastSignDate]\r\n";
    @Autowired
	public UsersDaoImpl(@Qualifier("pcmsDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<UserDetail> getUsers ( )
	{
		ArrayList<UserDetail> list = null;
		String sql =
				""
				+ " SELECT \r\n"
				+ this.select
				+ "  FROM [PCMS].[dbo].[Users] as a \r\n"
				+ "  left join [PCMS].[dbo].[EmployeePermits] as b on a.[UserId] = b.[EmployeeId] \r\n"
				+ "  where  ( b.[WebApp] = 'PCMS2' OR B.[WebApp] IS NULL )\r\n";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genUsersDetail(map));
		}
		return list;
	}

	@Override
	public int updateUserPermit(String userId, String permitId, String changeBy)
	{
		String sql = "IF EXISTS (SELECT 1 FROM [PCMS].[dbo].[EmployeePermits]"
				+ "             WHERE [EmployeeId]=? AND [WebApp]='PCMS2')\r\n"
				+ "  UPDATE [PCMS].[dbo].[EmployeePermits]"
				+ "     SET [PermitId]=?, [ChangeBy]=?, [ChangeDate]=GETDATE()"
				+ "   WHERE [EmployeeId]=? AND [WebApp]='PCMS2'\r\n"
				+ "ELSE\r\n"
				+ "  INSERT INTO [PCMS].[dbo].[EmployeePermits]"
				+ "    ([EmployeeId],[WebApp],[PermitId],[DataStatus],[CreateBy],[CreateDate])"
				+ "  VALUES (?, 'PCMS2', ?, 'O', ?, GETDATE())";
		return this.jdbc.update(sql, userId, permitId, changeBy, userId, userId, permitId, changeBy);
	}

	@Override
	public ArrayList<UserDetail> getUsersByUserId(String userId)
	{
		ArrayList<UserDetail> list = null;
		String sql =
				""
				+ " SELECT \r\n"
				+ this.select
				+ "  FROM [PCMS].[dbo].[Users] as a \r\n"
				+ "  left join [PCMS].[dbo].[EmployeePermits] as b on a.[UserId] = b.[EmployeeId] \r\n"
				+ "  where  ( b.[WebApp] = 'PCMS2' OR B.[WebApp] IS NULL )\r\n"
				+ "     and  a.[UserId] = '"+userId+"' \r\n";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genUsersDetail(map));
		}
		return list;
	}
}
