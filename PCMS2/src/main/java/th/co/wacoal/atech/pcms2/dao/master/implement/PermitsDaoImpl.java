package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.PermitsDao;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class PermitsDaoImpl implements PermitsDao {
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

	private String select =""

						+ "       [Id]\r\n"
						+ "      ,[WebApp]\r\n"
						+ "      ,[PermitId]\r\n"
						+ "      ,[Description]\r\n"
						+ "      ,[IsPCMSMain]\r\n"
						+ "      ,[IsPCMSDetail]\r\n"
						+ "      ,[IsPCMSMainToProd]\r\n"
						+ "      ,[IsPCMSMainToLBMS]\r\n"
						+ "      ,[IsPCMSMainToQCMS]\r\n"
						+ "      ,[IsPCMSMainToInspect]\r\n"
						+ "      ,[IsPCMSMainToSFC]\r\n"
						+ "      ,[IsReport]\r\n"
						+ "      ,[IsUserManagement]\r\n"
						+ "      ,[DataStatus]\r\n"
						+ "      ,[ChangeBy]\r\n"
						+ "      ,[ChangeDate]\r\n"
						+ "      ,[CreateBy]\r\n"
						+ "      ,[CreateDate]\r\n";
	@Autowired
    public PermitsDaoImpl(Database database ) {
		this.database = database ;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}


	@Override
	public ArrayList<PermitDetail> getPermitsDetail( )
	{
		ArrayList<PermitDetail> list = null;
//		String sql =
//				""
//						+ " SELECT \r\n"
//						+ this.select
//						+ " FROM [PCMS2].[dbo].[Permits] as a\r\n"
//						+ " WHERE a.[WebApp] = 'PCMS2' and \r\n"
//						+ "       A.DataStatus = 'O'   \r\n" ;  
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genPermitDetail(map));
//		}
		return list;		
	}
	@Override
	public ArrayList<PermitDetail> getPermitsDetailByPermitId(String permitId)
	{
		ArrayList<PermitDetail> list = null;
		String sql =
				""
						+ " SELECT TOP (1000)" 
						+ this.select
						+ "  FROM [PCMS].[dbo].[Permits] as a\r\n"
						+ " WHERE a.[WebApp] = 'PCMS2' and \r\n"
						+ "       A.DataStatus = 'O' and \r\n"
						+ "		  a.[PermitId] = '"+permitId+"' \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPermitDetail(map));
		}
		return list;		
	}

	@Override
	public ArrayList<PermitDetail> getEmployeePermitsDetailByPermitId(String userId, String permitId)
	{
		ArrayList<PermitDetail> list = null;
		String sql =
				""
						+ "SELECT \r\n"
						+ "	pe.Id\r\n"
						+ "	,ed.UserId  as UserId  \r\n"
						+ "	,pe.[WebApp]\r\n"
						+ "	,pe.[PermitId]\r\n"
						+ "	,pe.[Description]\r\n"
						+ "	,pe.[IsPCMSMain]\r\n"
						+ "	,pe.[IsPCMSDetail]\r\n"
						+ "	,pe.[IsPCMSMainToProd]\r\n"
						+ "	,pe.[IsPCMSMainToLBMS]\r\n"
						+ "	,pe.[IsPCMSMainToQCMS]\r\n"
						+ "	,pe.[IsPCMSMainToInspect]\r\n"
						+ "	,pe.[IsPCMSMainToSFC]\r\n"
						+ "	,pe.[IsReport]\r\n"
						+ "	,pe.[IsUserManagement]\r\n"
						+ "	,pe.[DataStatus]\r\n"
						+ "	,pe.[ChangeBy]\r\n"
						+ "	,pe.[ChangeDate]\r\n"
						+ "	,pe.[CreateBy]\r\n"
						+ "	,pe.[CreateDate]\r\n"
						+ "FROM [PCMS].[dbo].[Users] as ed\r\n"
						+ "LEFT join [PCMS].[dbo].[EmployeePermits] as ep \r\n"
						+ "on ep.EmployeeId = ed.UserId \r\n"
						+ "	and ep.[DataStatus] = 'O' \r\n"
						+ "left join [PCMS].[dbo].[Permits] as pe \r\n"
						+ "on pe.PermitId = ep.PermitId  \r\n"
						+ " WHERE EP.WebApp = 'PCMS2' AND\r\n"
						+ "		  pe.WebApp = 'PCMS2' AND\r\n"
						+ "		  pe.[PermitId] = '"+permitId+"' AND\r\n"
						+ "		  ed.UserId = '"+userId+"' \r\n" 
						+ " ORDER BY ed.UserId\r\n"
						+ "\r\n"  ;   
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPermitDetail(map));
		}
		return list;		
	}

}