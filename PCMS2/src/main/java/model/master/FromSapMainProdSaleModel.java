package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapMainProdSaleDao;
import dao.master.implement.FromSapMainProdSaleDaoImpl;
import entities.erp.atech.FromErpMainProdSaleDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapMainProdSaleModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapMainProdSaleDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapMainProdSaleModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapMainProdSaleDaoImpl(this.database);
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
	public  String upsertFromSapMainProdSaleDetail( ArrayList<FromErpMainProdSaleDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapMainProdSaleDetail(paList );
		return iconStatus;
	}
}
