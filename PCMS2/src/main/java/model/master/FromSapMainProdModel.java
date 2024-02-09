package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.DataImportSORDao;
import dao.implement.DataImportSORDaoImpl;
import dao.master.FromSORCFMDao;
import dao.master.FromSapMainProdDao;
import dao.master.implement.FromSORCFMDaoImpl;
import dao.master.implement.FromSapMainProdDaoImpl;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.SORDetail;
import info.SORSqlInfo;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapMainProdModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database; 
	   private FromSapMainProdDao dao;
	   @SuppressWarnings("unused")	
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	   public FromSapMainProdModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance()); 
	         this.dao = new FromSapMainProdDaoImpl(this.database );
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

	   public void destroy() {
	      this.database.close();
	      super.destroy();
	   }
	   
	public ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrderRP) {
		// TODO Auto-generated method stub
				ArrayList<PCMSSecondTableDetail> list = this.dao.getFromSapMainProdDetail(prdOrderRP);
				return list;
	} 

	   
	public ArrayList<PCMSAllDetail> getUserStatusDetail( ){
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getUserStatusDetail( );
		return list;
	} 

 
}
