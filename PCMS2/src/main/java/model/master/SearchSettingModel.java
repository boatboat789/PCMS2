package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.DataImportSORDao;
import dao.implement.DataImportSORDaoImpl;
import dao.master.FromSORCFMDao;
import dao.master.SearchSettingDao;
import dao.master.SwitchProdOrderDao;
import dao.master.implement.FromSORCFMDaoImpl;
import dao.master.implement.SearchSettingDaoImpl;
import dao.master.implement.SwitchProdOrderDaoImpl;
import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import entities.SORDetail;
import info.SORSqlInfo;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class SearchSettingModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database; 
	   private SearchSettingDao dao;
	   @SuppressWarnings("unused")	
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	   public SearchSettingModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance()); 
	         this.dao = new SearchSettingDaoImpl(this.database );
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
	  
	   public ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId,String forPage)  {
			// TODO Auto-generated method stub
			ArrayList<PCMSTableDetail> list = this.dao.getSearchSettingDetail(userId,forPage);
			return list;
		}

	public ArrayList<PCMSTableDetail> insertSearchSettingDetai(ArrayList<PCMSTableDetail> poList, String forPage) {
		// TODO Auto-generated method stub
					ArrayList<PCMSTableDetail> list = this.dao.insertSearchSettingDetai(poList,forPage);
					return list;
	}

	public ArrayList<PCMSTableDetail> updateSearchSettingDetai(ArrayList<PCMSTableDetail> poList, String forPage) {
		// TODO Auto-generated method stub
					ArrayList<PCMSTableDetail> list = this.dao.updateSearchSettingDetai(poList,forPage);
					return list;
	}
 
}
