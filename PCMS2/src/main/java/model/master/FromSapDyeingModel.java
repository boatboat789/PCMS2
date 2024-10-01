package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapDyeingDao;
import dao.master.implement.FromSapDyeingDaoImpl;
import entities.DyeingDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapDyeingModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapDyeingDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public FromSapDyeingModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapDyeingDaoImpl(this.database);
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

	public ArrayList<DyeingDetail> getFromSapDyeingDetailByProductionOrder(String prodOrder) 
	{
		// TODO Auto-generated method stub
		ArrayList<DyeingDetail> list = this.dao.getFromSapDyeingDetailByProductionOrder(prodOrder);
		return list;
	}
}
