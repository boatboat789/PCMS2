package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.FromSapPODao;
import th.co.wacoal.atech.pcms2.dao.master.implement.FromSapPODaoImpl;
import th.co.wacoal.atech.pcms2.entities.PODetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPODetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapPOModel extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	private Database database;
//	private FromSapPODao dao;
//	@SuppressWarnings("unused")
//	private String[] uiColumns;
//	@SuppressWarnings("unused")
//	private static final String columns = "";
//
//    @Autowired
//	public FromSapPOModel() {
//		try {
//			this.database = new Database(SqlPCMSInfo.getInstance());
//			this.dao = new FromSapPODaoImpl(this.database);
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
//	public ArrayList<PODetail> getFromSapPODetailByProductionOrder(String prodOrder)
//	{
//		// TODO Auto-generated method stub
//		ArrayList<PODetail> list = this.dao.getFromSapPODetailByProductionOrder(prodOrder);
//		return list;
//	}
// 
//
//	public String upsertFromSapPODetail(ArrayList<FromErpPODetail> paList)
//	{
//		// TODO Auto-generated method stub
//		String iconStatus = this.dao.upsertFromSapPODetail(paList);
//		return iconStatus;
//		// TODO Auto-generated method stub
//		
//	}
}
