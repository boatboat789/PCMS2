package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.PlanCFMDateDao;
import dao.master.implement.PlanCFMDateDaoImpl;
import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class PlanCFMDateModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database; 
	   private PlanCFMDateDao dao;
	   @SuppressWarnings("unused")	
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	   public PlanCFMDateModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance()); 
	         this.dao = new PlanCFMDateDaoImpl(this.database );
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

		public ArrayList<InputDateDetail> getCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList){
			// TODO Auto-generated method stub
			ArrayList<InputDateDetail> list = this.dao.getCFMPlanDateDetail(poList);
			return list;
		}
	public ArrayList<InputDateDetail> getCountCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getCountCFMPlanDateDetail(poList);
		return list;
	}
	public ArrayList<InputDateDetail> getMaxCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList){
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getMaxCFMPlanDateDetail(poList);
		return list;
	}
 
}
