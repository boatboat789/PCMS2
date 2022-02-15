package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;

import com.google.gson.JsonElement;

import dao.PCMSMainDao;
import dao.implement.PCMSMainDaoImpl;
import entities.ColumnHiddenDetail;
import entities.PCMSAllDetail; 
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

	public ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.searchByDetail(poList);
		return list;
	} 
	public ArrayList<PCMSTableDetail> getSaleNumberList() {
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.getSaleNumberList();
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

	public ArrayList<ColumnHiddenDetail> getColVisibleDetail(String user) {
		ArrayList<ColumnHiddenDetail> list = this.dao.getColVisibleDetail(user);
		return list;
	}

	public  ArrayList<ColumnHiddenDetail>  saveColSettingToServer(ColumnHiddenDetail pd) {
		ArrayList<ColumnHiddenDetail> list = this.dao.saveColSettingToServer(pd);
		return list;
	} 
	public ArrayList<PCMSAllDetail> getCustomerNameList() {
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerNameList();
		return list;
	}
	public ArrayList<PCMSAllDetail> getCustomerShortNameList() {
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerShortNameList();
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
