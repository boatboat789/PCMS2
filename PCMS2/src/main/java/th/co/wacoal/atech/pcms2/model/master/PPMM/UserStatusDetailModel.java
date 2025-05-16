package th.co.wacoal.atech.pcms2.model.master.PPMM;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.UserStatusDetailDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.PPMM.UserStatusDetailDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail; 
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class UserStatusDetailModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private UserStatusDetailDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public UserStatusDetailModel () {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new UserStatusDetailDaoImpl(this.database);
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

	public ArrayList<PCMSAllDetail> getUserStatusDetail( )
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getUserStatusDetail( );
		return list;
	}
}
