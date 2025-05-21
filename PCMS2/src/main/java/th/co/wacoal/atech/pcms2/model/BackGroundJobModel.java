package th.co.wacoal.atech.pcms2.model;

import java.sql.SQLException;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.BackGroundJobDao;
import th.co.wacoal.atech.pcms2.dao.implement.BackGroundJobDaoImpl;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class BackGroundJobModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private BackGroundJobDao  dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public BackGroundJobModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new BackGroundJobDaoImpl(this.database);
			this.uiColumns = arrayColumn();
		} catch (SQLException | ClassNotFoundException var2) {
			var2.printStackTrace();
		}
	}
	public static String stringColumn() {
	   return "[]";
	}
   	public static String[] arrayColumn() {
   		return "".replaceAll("'", "").split(",");
   	}
   	@Override
	public void destroy() {
   		this.database.close();
   		super.destroy();
   	}
   	public void handlerERPAtechToWebApp() {
   		this.dao.handlerERPAtechToWebApp();
   	}
	public void execUpsertToMainProd() {
		// TODO Auto-generated method stub
		 this.dao.execUpsertToMainProd();
	}
	public void execUpsertToTEMPProdWorkDate( ) {
		// TODO Auto-generated method stub
		 this.dao.execUpsertToTEMPProdWorkDate( );
	}
	public void execUpsertToTEMPUserStatusOnWeb( ) {
		// TODO Auto-generated method stub
		 this.dao.execUpsertToTEMPUserStatusOnWeb( );
	}
	public void execUpsertToTEMPUserStatusOnWebWithProdOrder(String productionOrder) {
		// TODO Auto-generated method stub
		 this.dao.execUpsertToTEMPUserStatusOnWebWithProdOrder( productionOrder);

	}
	public void handlerERPAtechToWebAppProductionOrder()
	{
   		this.dao.handlerERPAtechToWebAppProductionOrder();
		// TODO Auto-generated method stub
		
	}
	public void handlerERPAtechToWebAppSaleOrder()
	{
   		this.dao.handlerERPAtechToWebAppSaleOrder();
		// TODO Auto-generated method stub
		
	}
	public void sortBackGroundAfterGetERPDataProcedure()
	{
		// TODO Auto-generated method stub
   		this.dao.sortBackGroundAfterGetERPDataProcedure();
		
	}
	public void handlerERPAtechToWebAppCustomer()
	{
		// TODO Auto-generated method stub
   		this.dao.handlerERPAtechToWebAppCustomer();
		
	}
	public void handlerBackGroundZ_ATT_CustomerConfirm2()
	{
		// TODO Auto-generated method stub
   		this.dao.handlerBackGroundZ_ATT_CustomerConfirm2();
		
	}

}
