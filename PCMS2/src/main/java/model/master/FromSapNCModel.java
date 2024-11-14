//package model.master;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//import javax.servlet.http.HttpServlet;
//
//import dao.master.FromSapNCDao;
//import dao.master.implement.FromSapNCDaoImpl;
//import entities.NCDetail;
//import info.SqlInfo;
//import th.in.totemplate.core.sql.Database;
//
//public class FromSapNCModel extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	private Database database;
//	private FromSapNCDao dao;
//	@SuppressWarnings("unused")
//	private String[] uiColumns;
//	@SuppressWarnings("unused")
//	private static final String columns = "";
//
//	public FromSapNCModel() {
//		try {
//			this.database = new Database(SqlInfo.getInstance());
//			this.dao = new FromSapNCDaoImpl(this.database);
//			this.uiColumns = arrayColumn();
//		} catch (SQLException | ClassNotFoundException var2) {
//			var2.printStackTrace();
//		}
//
//	}
//
//	public static String stringColumn()
//	{
//		return "[]";
//	}
//
//	public static String[] arrayColumn()
//	{
//		return "".replaceAll("'", "").split(",");
//	}
//
//	@Override
//	public void destroy()
//	{
//		this.database.close();
//		super.destroy();
//	}
//
//	public ArrayList<NCDetail> getFromSapNCDetailByProductionOrder(String prodOrder)
//	{
//		// TODO Auto-generated method stub
//		ArrayList<NCDetail> list = this.dao.getFromSapNCDetailByProductionOrder(prodOrder);
//		return list;
//	}
//}
