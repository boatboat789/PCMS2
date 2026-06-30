package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.ConfigCustomerUserDao;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class ConfigCustomerUserDaoImpl implements  ConfigCustomerUserDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private JdbcTemplate jdbc;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

    @Autowired
	public ConfigCustomerUserDaoImpl(@Qualifier("pcmsDatabase")JdbcTemplate jdbc) {
		this.jdbc = jdbc;
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
				  + "      ,[EmployeeId]\r\n"
				  + "      ,[CustomerNo]\r\n"
				  + "      ,[CustomerDivision]\r\n"
				  + "      ,[DataStatus]\r\n"
				  + "  FROM [PCMS].[dbo].[ConfigCustomerUser] as a\r\n"
			  	+ " where a.[EmployeeId] = '" + userId+ "' \r\n"
			  	+ " ORDER BY EmployeeId desc ";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genConfigCustomerUserDetail(map));
		}
		return list;
	}
}
