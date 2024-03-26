package model;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;

import dao.PCMSMainDao;
import dao.implement.PCMSMainDaoImpl;
import entities.ColumnHiddenDetail;
import entities.ConfigCustomerUserDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class PCMSMainModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private PCMSMainDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	   public PCMSMainModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance());
	         this.dao = new PCMSMainDaoImpl(this.database);
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

	public ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList, boolean isCustomer) {
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.searchByDetail(poList, isCustomer);
		return list;
	}  
	public  ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getPrdDetailByRow(poList);
		return list;
	}

	public ArrayList<PCMSAllDetail> getUserStatusList() {
		// TODO Auto-generated method stub   
		ArrayList<PCMSAllDetail> list = this.dao.getUserStatusList();
		return list;
	}  
	public ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.saveDefault(poList);
		return list;
	}
	public ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.loadDefault(poList);
		return list;
	}
 
 
}
