	package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.ConfigCustomerUserDao;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class ConfigCustomerUserDaoImpl implements  ConfigCustomerUserDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

    @Autowired
	public ConfigCustomerUserDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<ConfigCustomerUserDetail> getConfigCustomerUserDetail(String userId) {
		ArrayList<ConfigCustomerUserDetail> list = null;
		String sql =
				  " SELECT [Id]\r\n"
				  + "      ,[EmployeeID]\r\n"
				  + "      ,[CustomerNo]\r\n"
				  + "      ,[CustomerDivision]\r\n"
				  + "      ,[IsPCMSDetailPage]\r\n"
				  + "      ,[IsPCMSSumPage]\r\n"
				  + "      ,[IsProdPathBtn]\r\n"
				  + "      ,[IsLBMSPathBtn]\r\n"
				  + "      ,[IsQCMSPathBtn]\r\n"
				  + "      ,[IsInspectPathBtn]\r\n"
				  + "      ,[IsSFCPathBtn]\r\n"
				  + "      ,[DataStatus]\r\n"
				  + "  FROM [PCMS].[dbo].[ConfigCustomerUser] as a\r\n"
			  	+ " where a.[EmployeeID] = '" + userId+ "' \r\n"
			  	+ " ORDER BY EmployeeID desc "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genConfigCustomerUserDetail(map));
		}
		return list;
	}
}
