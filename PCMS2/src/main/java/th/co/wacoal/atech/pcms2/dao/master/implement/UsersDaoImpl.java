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
						+ " SELECT [Id]\r\n"
						+ "      ,[UserId] as EmployeeID\r\n"
						+ "      ,[PermitId]\r\n"
						+ "      ,[Password]\r\n"
						+ "      ,[IsClosed]\r\n"
						+ "      ,[IsLocalUser]\r\n"
						+ "      ,[Firstname]\r\n"
						+ "      ,[Lastname]\r\n"
						+ "      ,[IsAdminSystem]\r\n"
						+ "      ,[IsAdminUser]\r\n"
						+ "      ,[PermissionId]\r\n"
						+ "      ,[Responsible]\r\n"
						+ "      ,[LastSignDate]\r\n"
						+ "      ,[ChangeBy]\r\n"
						+ "      ,[ChangeDate]\r\n"
						+ "      ,[RegistBy]\r\n"
						+ "      ,[RegistDate]\r\n"
						+ "      ,[IsCustomer]\r\n"
						+ "  FROM [PCMS].[dbo].[Users] as a \r\n"; 
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
						+ " SELECT [Id]\r\n"
						+ "      ,[UserId] as EmployeeID\r\n"
						+ "      ,[PermitId]\r\n"
						+ "      ,[Password]\r\n"
						+ "      ,[IsClosed]\r\n"
						+ "      ,[IsLocalUser]\r\n"
						+ "      ,[Firstname]\r\n"
						+ "      ,[Lastname]\r\n"
						+ "      ,[IsAdminSystem]\r\n"
						+ "      ,[IsAdminUser]\r\n"
						+ "      ,[PermissionId]\r\n"
						+ "      ,[Responsible]\r\n"
						+ "      ,[LastSignDate]\r\n"
						+ "      ,[ChangeBy]\r\n"
						+ "      ,[ChangeDate]\r\n"
						+ "      ,[RegistBy]\r\n"
						+ "      ,[RegistDate]\r\n"
						+ "      ,[IsCustomer]\r\n"
						+ "  FROM [PCMS].[dbo].[Users] as a\r\n"
						+ " WHERE a.[UserId] = '"+userId+"' \r\n"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genUsersDetail(map));
		}
		return list;
	}
}