package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.DataImportSORDao;
import dao.implement.DataImportSORDaoImpl;
import dao.master.FromSORCFMDao;
import dao.master.PlanCFMDateDao;
import dao.master.PlanCFMLabDateDao;
import dao.master.PlanSendCFMCusDateDao;
import dao.master.implement.FromSORCFMDaoImpl;
import dao.master.implement.PlanCFMDateDaoImpl;
import dao.master.implement.PlanCFMLabDateDaoImpl;
import dao.master.implement.PlanSendCFMCusDateDaoImpl;
import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;
import entities.SORDetail;
import info.SORSqlInfo;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class PlanCFMLabDateModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database; 
	   private PlanCFMLabDateDao dao;
	   @SuppressWarnings("unused")	
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	   public PlanCFMLabDateModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance()); 
	         this.dao = new PlanCFMLabDateDaoImpl(this.database );
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
 
		public ArrayList<InputDateDetail> getCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList){
			// TODO Auto-generated method stub
			ArrayList<InputDateDetail> list = this.dao.getCFMPlanLabDateDetail(poList);
			return list;
		}
	public ArrayList<InputDateDetail> getMaxCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getMaxCFMPlanLabDateDetail(poList);
		return list;
	}
	public ArrayList<InputDateDetail> getCountCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList){
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getCountCFMPlanLabDateDetail(poList);
		return list;
	}
 
}
