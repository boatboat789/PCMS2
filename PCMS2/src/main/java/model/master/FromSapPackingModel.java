package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapPackingDao;
import dao.master.implement.FromSapPackingDaoImpl;
import entities.PackingDetail;
import entities.erp.atech.FromErpPackingDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapPackingModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapPackingDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapPackingModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapPackingDaoImpl(this.database);
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

	public ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<PackingDetail> list = this.dao.getFromSapPackingDetailByProductionOrder(prodOrder);
		return list;
	}
 
	public String upsertFromSapPackingDetail(ArrayList<FromErpPackingDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertFromSapPackingDetail(paList);
		return iconStatus;
		
	}
}
