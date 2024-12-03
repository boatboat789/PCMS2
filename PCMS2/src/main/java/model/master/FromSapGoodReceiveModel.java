package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapGoodReceiveDao;
import dao.master.implement.FromSapGoodReceiveDaoImpl;
import entities.erp.atech.FromErpGoodReceiveDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapGoodReceiveModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapGoodReceiveDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapGoodReceiveModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapGoodReceiveDaoImpl(this.database);
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
	public  String upsertFromSapGoodReceiveDetail( ArrayList<FromErpGoodReceiveDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapGoodReceiveDetail(paList );
		return iconStatus;
	}
}
