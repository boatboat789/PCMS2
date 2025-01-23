package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.master.FromSapFinishingDao;
import dao.master.implement.FromSapFinishingDaoImpl;
import entities.FinishingDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapFinishingModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapFinishingDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSapFinishingModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapFinishingDaoImpl(this.database);
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

	public ArrayList<FinishingDetail> getFromSapFinishingDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<FinishingDetail> list = this.dao.getFromSapFinishingDetailByProductionOrder(prodOrder);
		return list;
	} 
}
