package model.master.InspectSystem;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.InspectSystem.InspectOrdersDao;
import dao.master.implement.InspectSystem.InspectOrdersDaoImpl;
import entities.PPMM.InspectOrdersDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class InspectOrdersModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private InspectOrdersDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public InspectOrdersModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new InspectOrdersDaoImpl(this.database);
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

	public ArrayList<InspectOrdersDetail> getInspectOrdersByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<InspectOrdersDetail> list = this.dao.getInspectOrdersByProductionOrder(prodOrder);
		return list;
	}
}
