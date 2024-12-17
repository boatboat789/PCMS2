package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.PermitsDao;
import dao.master.implement.PermitsDaoImpl;
import entities.PermitDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class PermitsModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public static String[] arrayColumn()
	{
		return "".replaceAll("'", "").split(",");
	}

	public static String stringColumn()
	{
		return "[]";
	}

	private Database database;

	private PermitsDao dao;

	@SuppressWarnings("unused")
	private String[] uiColumns;

	public PermitsModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new PermitsDaoImpl(this.database);
			this.uiColumns = arrayColumn();
		} catch (SQLException | ClassNotFoundException var2) {
			var2.printStackTrace();
		}

	}

	@Override
	public void destroy()
	{
		this.database.close();
		super.destroy();
	}

	public ArrayList<PermitDetail> getPermitsDetailByPermitId(String permitId)
	{
		ArrayList<PermitDetail> list = this.dao.getPermitsDetailByPermitId(permitId) ;
		return list;
	}
}
