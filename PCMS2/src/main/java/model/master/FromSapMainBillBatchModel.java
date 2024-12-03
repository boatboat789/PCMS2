package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapMainBillBatchDao;
import dao.master.implement.FromSapMainBillBatchDaoImpl;
import entities.erp.atech.FromErpMainBillBatchDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapMainBillBatchModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapMainBillBatchDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapMainBillBatchModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapMainBillBatchDaoImpl(this.database);
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
	public  String upsertFromSapMainBillBatchDetail( ArrayList<FromErpMainBillBatchDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapMainBillBatchDetail(paList );
		return iconStatus;
	}
}
