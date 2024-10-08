package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapWorkInLabDao;
import dao.master.implement.FromSapWorkInLabDaoImpl;
import entities.WorkInLabDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapWorkInLabModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapWorkInLabDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapWorkInLabModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapWorkInLabDaoImpl(this.database);
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

	public ArrayList<WorkInLabDetail> getFromSapWorkInLabDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<WorkInLabDetail> list = this.dao.getFromSapWorkInLabDetailByProductionOrder(prodOrder);
		return list;
	}
}
