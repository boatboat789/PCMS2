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
						+ " SELECT TOP (1000) [Id]\r\n"
						+ "      ,[PermitId]\r\n"
						+ "      ,[Description]\r\n"
						+ "      ,[IsTag]\r\n"
						+ "      ,[IsPC]\r\n"
						+ "      ,[IsWorkOp5]\r\n"
						+ "      ,[IsWorkOp10]\r\n"
						+ "      ,[IsWorkOp15]\r\n"
						+ "      ,[IsWorkOp20]\r\n"
						+ "      ,[IsWorkOp30]\r\n"
						+ "      ,[IsWorkOp35]\r\n"
						+ "      ,[IsWorkOp40]\r\n"
						+ "      ,[IsWorkOp50]\r\n"
						+ "      ,[IsWorkOp60]\r\n"
						+ "      ,[IsWorkOp70]\r\n"
						+ "      ,[IsWorkOp80]\r\n"
						+ "      ,[IsWorkOp90]\r\n"
						+ "      ,[IsWorkOp100]\r\n"
						+ "      ,[IsWorkOp110]\r\n"
						+ "      ,[IsWorkOp120]\r\n"
						+ "      ,[IsWorkOp130]\r\n"
						+ "      ,[IsWorkOp135]\r\n"
						+ "      ,[IsWorkOp140]\r\n"
						+ "      ,[IsWorkOp144]\r\n"
						+ "      ,[IsWorkOp145]\r\n"
						+ "      ,[IsWorkOp147]\r\n"
						+ "      ,[IsWorkOp150]\r\n"
						+ "      ,[IsWorkOp155]\r\n"
						+ "      ,[IsWorkOp160]\r\n"
						+ "      ,[IsWorkOp180]\r\n"
						+ "      ,[IsWorkOp181]\r\n"
						+ "      ,[IsWorkOp185]\r\n"
						+ "      ,[IsWorkOp190]\r\n"
						+ "      ,[IsWorkOp195]\r\n" 
						+ "      ,[IsWorkOp199]\r\n"
						+ "      ,[IsWorkOp205]\r\n"
						+ "      ,[IsWorkOp210]\r\n"
						+ "      ,[IsWorkOp215]\r\n"
						+ "      ,[IsWorkOp220]"
						+ "      ,[DataStatus]\r\n"
						+ "      ,[ChangeBy]\r\n"
						+ "      ,[ChangeDate]\r\n"
						+ "      ,[CreateBy]\r\n"
						+ "      ,[CreateDate]\r\n"
						+ "  FROM [PCMS2].[dbo].[Permits] as a\r\n"
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

}