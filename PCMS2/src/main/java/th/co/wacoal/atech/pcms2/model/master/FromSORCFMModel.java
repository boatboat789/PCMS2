package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.FromSORCFMDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.FromSORCFMDaoImpl;
import th.co.wacoal.atech.pcms2.entities.SORDetail;
import th.co.wacoal.atech.pcms2.info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSORCFMModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSORCFMDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSORCFMModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSORCFMDaoImpl(this.database);
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

	public String upSertFromSORCFMDetail(ArrayList<SORDetail> list)
	{
		String value = this.dao.upSertFromSORCFMDetail(list);
		return value;
	}

}
