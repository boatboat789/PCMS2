package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.master.FromSapPresetDao;
import dao.master.implement.FromSapPresetDaoImpl;
import entities.PresetDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapPresetModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapPresetDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSapPresetModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapPresetDaoImpl(this.database);
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

	public ArrayList<PresetDetail> getFromSapPresetDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<PresetDetail> list = this.dao.getFromSapPresetDetailByProductionOrder(prodOrder);
		return list;
	}
}
