package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.FromSapMainSaleDao;
import dao.master.implement.FromSapMainSaleDaoImpl;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class FromSapMainSaleModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database; 
	   private FromSapMainSaleDao dao;
	   @SuppressWarnings("unused")	
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	   public FromSapMainSaleModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance()); 
	         this.dao = new FromSapMainSaleDaoImpl(this.database );
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
	   
	public ArrayList<PCMSSecondTableDetail> getDivisionDetail( ) {
		// TODO Auto-generated method stub
				ArrayList<PCMSSecondTableDetail> list = this.dao.getDivisionDetail( );
				return list;
	} 

	public ArrayList<PCMSAllDetail> getCustomerNameDetail( ) {
		// TODO Auto-generated method stub
				ArrayList<PCMSAllDetail> list = this.dao.getCustomerNameDetail( );
				return list;
	} 
	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail( ) {
		// TODO Auto-generated method stub
				ArrayList<PCMSAllDetail> list = this.dao.getCustomerShortNameDetail( );
				return list;
	}  
	public ArrayList<PCMSTableDetail> getSaleNumberDetail() {
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.getSaleNumberDetail();
		return list;
	}

}
