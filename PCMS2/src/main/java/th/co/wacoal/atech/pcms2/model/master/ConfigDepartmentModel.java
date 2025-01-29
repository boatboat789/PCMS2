package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.ConfigDepartmentDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.ConfigDepartmentDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class ConfigDepartmentModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private ConfigDepartmentDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	    @Autowired
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

	   @Override
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
