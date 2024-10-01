package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapSaleDao;
import dao.master.implement.FromSapSaleDaoImpl;
import entities.SaleDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapSaleModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapSaleDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapSaleModel () {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapSaleDaoImpl(this.database);
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

	public ArrayList<SaleDetail> getFromSapSaleDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<SaleDetail> list = this.dao.getFromSapSaleDetailByProductionOrder(prodOrder);
		return list;
	}
}
