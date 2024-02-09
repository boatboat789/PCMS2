package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.DataImportSORDao;
import dao.implement.DataImportSORDaoImpl;
import dao.master.ConfigDepartmentDao;
import dao.master.FromSORCFMDao;
import dao.master.implement.ConfigDepartmentDaoImpl;
import dao.master.implement.FromSORCFMDaoImpl;
import entities.PCMSSecondTableDetail;
import entities.SORDetail;
import info.SORSqlInfo;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class ConfigDepartmentModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database; 
	   private ConfigDepartmentDao dao;
	   @SuppressWarnings("unused")	
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	   public ConfigDepartmentModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance()); 
	         this.dao = new ConfigDepartmentDaoImpl(this.database );
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

		public ArrayList<PCMSSecondTableDetail> getDelayedDepartmentList() {
			// TODO Auto-generated method stub
			ArrayList<PCMSSecondTableDetail> list = this.dao.getDelayedDepartmentList();
			return list;
		} 
 
}
