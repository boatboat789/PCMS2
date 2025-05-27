package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
						+ " FROM [PCMS].[dbo].[EmployeePermits] as a\r\n"
						+ " WHERE a.[WebApp] = 'PCMS2' and \r\n"
						+ "       A.DataStatus = 'O' and \r\n"
						+ "		  a.[EmployeeId] = '"+userId+"' \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genEmployeeDetail(map));
		}
		return list;		
	}
	@Override
	public String upsertEmployeePermits(ArrayList<EmployeeDetail> poList,String webApp) {

		EmployeeDetail bean = new EmployeeDetail();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String changeBy = "";
		String iconStatus = "I";
		String sql = ""
				+ " UPDATE [PCMS].[dbo].[EmployeePermits] "
				+ " 	SET [PermitId] = ? ,[Responsible] = ? ,[DataStatus] = ?, [ChangeBy] = ? ,[ChangeDate] = ?  " 
				+ " 	WHERE [EmployeeId] = ? and "// 12
				+ "			  [WebApp] = ?  "// 14
				+ " declare  @rc int = @@ROWCOUNT " // 56
				
				+ " if @rc = 0 " 
				+ "  BEGIN " 
				+ " 	INSERT INTO [PCMS].[dbo].[EmployeePermits] \r\n"
				+ " 		("
				+ " 		[EmployeeId] ,[WebApp] ,[PermitId],[Responsible] ,[DataStatus] , "// 15,16,17,18,19
				+ "			[ChangeBy],[ChangeDate] ,[CreateBy] ,[CreateDate] "// 25,26,27,28
				+ " 		) "// 55
				+ " 	values \r\n"
				+ "			("
				+ "			? , ? , ? , ? , ? , "
				+ "			? , ? , ? , ?  " 
				+ "			)  ; END "; 
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		try {
			prepared = connection.prepareStatement(sql);
			for (EmployeeDetail  element : poList) { 

				bean = element;
				String permitId = bean.getPermitId();
				String dataStatus = bean.getDataStatus();
				String responsible = bean.getResponsible() ;
				changeBy = bean.getChangeBy(); 
				String userId = bean.getEmployeeID(); 
				changeBy = bean.getChangeBy(); 
				int index = 1;
				prepared.setString(index ++ , permitId);
				prepared.setString(index ++ , responsible); 
				prepared.setString(index ++ , dataStatus);
				prepared.setString(index ++ , changeBy);
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setString(index ++ , userId); 
				prepared.setString(index ++ , webApp);


				prepared.setString(index ++ , userId); 
				prepared.setString(index ++ , webApp);
				prepared.setString(index ++ , permitId);
				prepared.setString(index ++ , responsible); 
				prepared.setString(index ++ , dataStatus);
				prepared.setString(index ++ , changeBy);
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setString(index ++ , changeBy);
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.addBatch();
			}
			prepared.executeBatch();
			prepared.close();
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println("upsertEmployeePermits " + e.getMessage());
			iconStatus = "E";
		} finally {
			//this.database.close();
		}
		return iconStatus;
	} 
}