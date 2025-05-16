package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.ConfigCustomerUserDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.ConfigCustomerUserDaoImpl;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class ConfigCustomerUserModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private ConfigCustomerUserDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	@Autowired
	public ConfigCustomerUserModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new ConfigCustomerUserDaoImpl(this.database);
			this.uiColumns = arrayColumn();
		} catch (SQLException | ClassNotFoundException var2) {
			var2.printStackTrace();
		}

	}

	public static String stringColumn()
	{
		return "[]";
	}

	public static String[] arrayColumn()
	{
		return "".replaceAll("'", "").split(",");
	}

	@Override
	public void destroy()
	{
		this.database.close();
		super.destroy();
	}

	public ArrayList<ConfigCustomerUserDetail> getConfigCustomerUserDetail(String userId)
	{
		// TODO Auto-generated method stub
		ArrayList<ConfigCustomerUserDetail> list = this.dao.getConfigCustomerUserDetail(userId);
		return list;
	}

}
