package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.UsersDao;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class UsersDaoImpl implements UsersDao {
	private BeanCreateModel bcModel = new BeanCreateModel();
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtil = new SqlStatementHandler();
	private Database database;
	private String message;

	public DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
	// ------------------------------------------------------------------------------------------------
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String select = ""
			+ "       A.[Id]\r\n"
			+ "      ,A.[EmployeeID]\r\n"
			+ "      ,B.[PermitId]\r\n"
			+ "      ,B.[Responsible]\r\n"
			+ "      ,A.[IsClosed]\r\n"
			+ "      ,A.[IsLocalUser]\r\n"
			+ "      ,A.[FirstName]\r\n"
			+ "      ,A.[LastName]\r\n"
			+ "      ,A.[Role]\r\n"
			+ "      ,A.[Password]\r\n"
			+ "      ,A.[ArrangedBy]\r\n"
			+ "      ,A.[AuthorizedBy]\r\n"
			+ "      ,A.[ChangeBy]\r\n"
			+ "      ,A.[ChangeDate]\r\n"
			+ "      ,a.[IsCustomer]\r\n"
			+ "      ,A.[CreateBy]\r\n"
			+ "      ,A.[CreateDate]\r\n";
    @Autowired
	public UsersDaoImpl(Database database ) {
		this.database = database ;
		this.message = "";
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
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genUsersDetail(map));
		}
		return list;
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
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genUsersDetail(map));
		}
		return list;
	}
}