//package model.master;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//import javax.servlet.http.HttpServlet;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import dao.master.FromSapInspectDao;
//import dao.master.implement.FromSapInspectDaoImpl;
//import entities.InspectDetail;
//import info.SqlPCMSInfo;
//import th.in.totemplate.core.sql.Database;
//
//@Component
//public class FromSapInspectModel  extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	private Database database;
//	private FromSapInspectDao dao;
//	@SuppressWarnings("unused")
//	private String[] uiColumns;
//	@SuppressWarnings("unused")
//	private static final String columns = "";
//
//    @Autowired
//	public FromSapInspectModel() {
//		try {
//			this.database = new Database(SqlPCMSInfo.getInstance());
//			this.dao = new FromSapInspectDaoImpl(this.database);
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
//	public ArrayList<InspectDetail> getFromSapInspectDetailByProductionOrder(String prodOrder)
//	{
//		// TODO Auto-generated method stub
//		ArrayList<InspectDetail> list = this.dao.getFromSapInspectDetailByProductionOrder(prodOrder);
//		return list;
//	} 
//} 


