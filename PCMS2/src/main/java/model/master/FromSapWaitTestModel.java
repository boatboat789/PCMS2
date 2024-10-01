package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapWaitTestDao;
import dao.master.implement.FromSapWaitTestDaoImpl;
import entities.WaitTestDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapWaitTestModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapWaitTestDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapWaitTestModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
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
