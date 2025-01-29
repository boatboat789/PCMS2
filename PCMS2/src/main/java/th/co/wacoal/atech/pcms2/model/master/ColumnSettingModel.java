package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.ColumnSettingDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.ColumnSettingDaoImpl;
import th.co.wacoal.atech.pcms2.entities.ColumnHiddenDetail;
import th.co.wacoal.atech.pcms2.info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class ColumnSettingModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private ColumnSettingDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	    @Autowired
	   public ColumnSettingModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance());
	         this.dao = new ColumnSettingDaoImpl(this.database );
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

		public ArrayList<ColumnHiddenDetail> getColumnVisibleDetail(String user) {
			ArrayList<ColumnHiddenDetail> list = this.dao.getColumnVisibleDetail(user);
			return list;
		}
		public ArrayList<ColumnHiddenDetail> upsertColumnSettingDetail(ColumnHiddenDetail pd) {
			// TODO Auto-generated method stub
			ArrayList<ColumnHiddenDetail> list = this.dao.upsertColumnSettingDetail(pd);
			return list;
		}

		public ArrayList<ColumnHiddenDetail> upsertColumnVisibleSummary(ColumnHiddenDetail pd) {
			// TODO Auto-generated method stub
			ArrayList<ColumnHiddenDetail> list = this.dao.upsertColumnVisibleSummary(pd);
			return list;
		}

}
