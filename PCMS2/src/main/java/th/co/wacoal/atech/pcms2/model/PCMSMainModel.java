package th.co.wacoal.atech.pcms2.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.PCMSMainDao;
import th.co.wacoal.atech.pcms2.dao.implement.PCMSMainDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class PCMSMainModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private PCMSMainDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	    @Autowired
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

	   @Override
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
