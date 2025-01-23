package model;

import java.sql.SQLException;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.BackGroundJobDao;
import dao.implement.BackGroundJobDaoImpl;
import info.SqlInfo;
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
			this.database = new Database(SqlInfo.getInstance());
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

}
