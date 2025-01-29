package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.PlanDeliveryDateDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.PlanDeliveryDateDaoImpl;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class PlanDeliveryDateModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private PlanDeliveryDateDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	    @Autowired
	   public PlanDeliveryDateModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance());
	         this.dao = new PlanDeliveryDateDaoImpl(this.database );
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

		public ArrayList<InputDateDetail> getCountDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList){
			// TODO Auto-generated method stub
			ArrayList<InputDateDetail> list = this.dao.getCountDeliveryPlanDateDetail(poList);
			return list;
		}
	public ArrayList<InputDateDetail> getMaxDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		// TODO Auto-generated method stub
		ArrayList<InputDateDetail> list = this.dao.getMaxDeliveryPlanDateDetail(poList);
		return list;
	}
}
