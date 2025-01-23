package model.master.LBMS;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.master.LBMS.ImportDetailDao;
import dao.master.implement.LBMS.ImportDetailDaoImpl;
import entities.LBMS.ImportDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class ImportDetailModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private ImportDetailDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public ImportDetailModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new ImportDetailDaoImpl(this.database);
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

	public ArrayList<ImportDetail> getImportDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<ImportDetail> list = this.dao.getImportDetailByProductionOrder(prodOrder);
		return list;
	}
}
