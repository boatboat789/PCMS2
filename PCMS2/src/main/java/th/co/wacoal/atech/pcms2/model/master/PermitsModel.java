package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.PermitsDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.PermitsDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class PermitsModel extends HttpServlet {
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

	private Database database;

	private PermitsDao dao;

	@SuppressWarnings("unused")
	private String[] uiColumns;

    @Autowired
	public PermitsModel() {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new PermitsDaoImpl(this.database);
			this.uiColumns = arrayColumn();
		} catch (SQLException | ClassNotFoundException var2) {
			var2.printStackTrace();
		}

	}

	@Override
	public void destroy()
	{
		this.database.close();
		super.destroy();
	}

	public ArrayList<PermitDetail> getPermitsDetailByPermitId(String permitId)
	{
		ArrayList<PermitDetail> list = this.dao.getPermitsDetailByPermitId(permitId) ;
		return list;
	}
	public ArrayList<PermitDetail> getPermitsDetail( )
	{
		ArrayList<PermitDetail> list = this.dao.getPermitsDetail( ) ;
		return list;
	} 
}
