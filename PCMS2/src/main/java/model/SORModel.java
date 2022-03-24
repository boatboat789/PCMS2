package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;

import dao.DataImportSORDao;
import dao.implement.DataImportSORDaoImpl;
import info.SORSqlInfo;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class SORModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private Database databaseSor;
	   private DataImportSORDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	   public SORModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance());
	         this.databaseSor = new Database(SORSqlInfo.getInstance());
	         this.dao = new DataImportSORDaoImpl(this.database,this.databaseSor);
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
	  
	   public void upSertSORToPCMS( ) { 
		   this.dao.upSertSORToPCMS(); 
	   } 

 
}
