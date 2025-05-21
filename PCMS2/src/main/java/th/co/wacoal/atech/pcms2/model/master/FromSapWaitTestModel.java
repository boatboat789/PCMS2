package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.FromSapWaitTestDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.FromSapWaitTestDaoImpl;
import th.co.wacoal.atech.pcms2.entities.WaitTestDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapWaitTestModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapWaitTestDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSapWaitTestModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new FromSapWaitTestDaoImpl(this.database);
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

	public ArrayList<WaitTestDetail> getFromSapWaitTestDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<WaitTestDetail> list = this.dao.getFromSapWaitTestDetailByProductionOrder(prodOrder);
		return list;
	}
}
