package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.EmployeePermitsDao;
import th.co.wacoal.atech.pcms2.entities.EmployeeDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

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
	public EmployeePermitsDaoImpl(Database database ) {
		this.database = database ;
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