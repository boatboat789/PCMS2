package th.co.wacoal.atech.pcms2.dao.master.implement.PPMM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.UserStatusDetailDao;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler; 
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class UserStatusDetailDaoImpl implements UserStatusDetailDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String select = ""
			+ "     [Id]\r\n"
			+ "      ,[UserStatusSapId]\r\n"
			+ "      ,[UserStatus]\r\n"
			+ "      ,[DataStatus]\r\n"; 
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public UserStatusDetailDaoImpl(Database database ) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}
	@Override
	public  ArrayList<PCMSAllDetail> getUserStatusDetail( ){
		ArrayList<PCMSAllDetail> list = null;
		String where = " where  "; 
		where += " a.[DataStatus] in ( 'O' ) \r\n";
		String sql =
				  " "
				+ " SELECT DISTINCT  \r\n"
				+ this.select 
				+ " FROM [PPMM].[dbo].[UserStatusDetail] as a \r\n "
				+ where
				+ " ";
//
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		return list;
	} 
}
