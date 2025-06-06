package th.co.wacoal.atech.pcms2.model;

import java.sql.SQLException;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.DataImportSORDao;
import th.co.wacoal.atech.pcms2.dao.implement.DataImportSORDaoImpl;
import th.co.wacoal.atech.pcms2.info.SqlSORInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class SORModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private DataImportSORDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	    @Autowired
	   public SORModel() {
	      try {
	         this.database = new Database(SqlSORInfo.getInstance());
	         this.dao = new DataImportSORDaoImpl( this.database);
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

	   public void upSertSORToPCMS( ) {
		   this.dao.upSertSORToPCMS();
	   }


}
