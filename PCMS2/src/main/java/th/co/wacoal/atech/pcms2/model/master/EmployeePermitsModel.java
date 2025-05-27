package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.EmployeePermitsDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.EmployeePermitsDaoImpl;
import th.co.wacoal.atech.pcms2.entities.EmployeeDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database; 
@Component
public class EmployeePermitsModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final String columns = "";

	public static String[] arrayColumn()
	{
		return "".replaceAll("'", "").split(",");
	}

	public static String stringColumn()
	{
		return "[]";
	} 
	@SuppressWarnings("unused")
	private String[] uiColumns;
  
	private EmployeePermitsDao dao;  
	private Database database;  
	
	public EmployeePermitsModel(  ) { 
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new EmployeePermitsDaoImpl(this.database);
			this.uiColumns = arrayColumn();
		} catch (SQLException | ClassNotFoundException var2) {
			var2.printStackTrace();
		}
	}
	@Override
	public void destroy()
	{ 
		super.destroy();
	}

	public ArrayList<EmployeeDetail> getEmployeePermitsDetailByUserId(String permitId)
	{
		ArrayList<EmployeeDetail> list = this.dao.getEmployeePermitsDetailByUserId(permitId) ;
		return list;
	}

	public String upsertEmployeePermits(ArrayList<EmployeeDetail> poList,String webApp)
	{
		String list = this.dao.upsertEmployeePermits(poList,webApp) ;
		return list;
	}
}
