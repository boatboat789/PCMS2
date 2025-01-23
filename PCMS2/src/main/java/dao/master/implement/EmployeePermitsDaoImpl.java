package dao.master.implement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import dao.master.EmployeePermitsDao;
import entities.EmployeeDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class EmployeePermitsDaoImpl implements EmployeePermitsDao {
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
	public EmployeePermitsDaoImpl(Database database2) {
		this.database = database2;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<EmployeeDetail> getEmployeePermitsDetailByUserId(String userId)
	{
		ArrayList<EmployeeDetail> list = null;
		String sql =
				""
						+ " SELECT"
						+ "		   [Id]"
						+ "      , [EmployeeId]"
						+ "      , [WebApp]"
						+ "      , [PermitId]" 
						+ "      ,[DataStatus]\r\n"
						+ "      ,[ChangeBy]\r\n"
						+ "      ,[ChangeDate]\r\n"
						+ "      ,[CreateBy]\r\n"
						+ "      ,[CreateDate]\r\n"
						+ " FROM [PPMM].[dbo].[EmployeePermits] as a\r\n"
						+ " WHERE a.[WebApp] = 'PPMM2' and \r\n"
						+ "       A.DataStatus = 'O' and \r\n"
						+ "		  a.[EmployeeId] = '"+userId+"' \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genEmployeeDetail(map));
		}
		return list;		
	}

}