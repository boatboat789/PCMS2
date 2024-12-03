package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapSaleInputDao;
import dao.master.implement.FromSapSaleInputDaoImpl;
import entities.SaleInputDetail;
import entities.erp.atech.FromErpSaleInputDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;
 
public class FromSapSaleInputModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database; 
	private FromSapSaleInputDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapSaleInputModel () {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapSaleInputDaoImpl(this.database);
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

	public ArrayList<SaleInputDetail> getFromSapSaleInputDetailByProductionOrder(String prodOrder)
	{ 
		ArrayList<SaleInputDetail> list = this.dao.getFromSapSaleInputDetailByProductionOrder(prodOrder);
		return list;
	}
	public String upsertFromSapSaleInputDetail(ArrayList<FromErpSaleInputDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertFromSapSaleInputDetail(paList);
		return iconStatus;
	}
}
