package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapPODao;
import dao.master.implement.FromSapPODaoImpl;
import entities.PODetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapPOModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapPODao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapPOModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapPODaoImpl(this.database);
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

	public ArrayList<PODetail> getFromSapPODetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<PODetail> list = this.dao.getFromSapPODetailByProductionOrder(prodOrder);
		return list;
	}
}
