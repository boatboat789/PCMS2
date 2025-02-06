package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.FromSapSubmitDateDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.FromSapSubmitDateDaoImpl;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapSubmitDateModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapSubmitDateDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSapSubmitDateModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new FromSapSubmitDateDaoImpl(this.database);
			this.uiColumns = arrayColumn();
		} catch (SQLException | ClassNotFoundException var2) {
			var2.printStackTrace();
		}

	}

	public static String stringColumn()
	{
		return "[]";
	}

	public static String[] arrayColumn()
	{
		return "".replaceAll("'", "").split(",");
	}

	@Override
	public void destroy()
	{
		this.database.close();
		super.destroy();
	}
 
	public  String upsertFromSapSubmitDateDetail( ArrayList<FromErpSubmitDateDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapSubmitDateDetail(paList );
		return iconStatus;
	}

	public ArrayList<InputDateDetail> getSubmitDateDetail(ArrayList<PCMSTableDetail> poList)
	{
		 ArrayList<InputDateDetail> list = this.dao.getSubmitDateDetail(poList );
		return list;
	}
}
