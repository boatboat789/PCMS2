package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapCFMDao;
import dao.master.implement.FromSapCFMDaoImpl;
import entities.CFMDetail;
import entities.erp.atech.FromErpCFMDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapCFMModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapCFMDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapCFMModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapCFMDaoImpl(this.database);
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

	public ArrayList<CFMDetail> getFromSapCFMDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<CFMDetail> list = this.dao.getFromSapCFMDetailByProductionOrder(prodOrder);
		return list;
	}
	public  String upsertFromSapCFMDetail( ArrayList<FromErpCFMDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapCFMDetail(paList );
		return iconStatus;
	}
}
