package th.co.wacoal.atech.pcms2.model.master;

import javax.servlet.http.HttpServlet;

import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
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
