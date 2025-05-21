package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.TEMP_UserStatusAutoDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.TEMP_UserStatusAutoDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.TempUserStatusAutoDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class TEMP_UserStatusAutoModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private TEMP_UserStatusAutoDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	    @Autowired
	   public TEMP_UserStatusAutoModel() {
	      try {
	         this.database = new Database(SqlPCMSInfo.getInstance());
	         this.dao = new TEMP_UserStatusAutoDaoImpl(this.database );
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

	   public ArrayList<TempUserStatusAutoDetail> getTempUserStatusAutoDetail(ArrayList<PCMSSecondTableDetail> poList) {
			// TODO Auto-generated method stub
			ArrayList<TempUserStatusAutoDetail> list = this.dao.getTempUserStatusAutoDetail(poList);
			return list;
		}

}
