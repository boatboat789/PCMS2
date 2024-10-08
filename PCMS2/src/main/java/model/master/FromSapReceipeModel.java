package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapReceipeDao;
import dao.master.implement.FromSapReceipeDaoImpl;
import entities.ReceipeDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapReceipeModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapReceipeDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapReceipeModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapReceipeDaoImpl(this.database);
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

	public ArrayList<ReceipeDetail> getFromSapReceipeDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<ReceipeDetail> list = this.dao.getFromSapReceipeDetailByProductionOrder(prodOrder);
		return list;
	}
}
