package th.co.wacoal.atech.pcms2.model;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.PCMSSearchDao;
import th.co.wacoal.atech.pcms2.dao.implement.PCMSSearchDaoImpl;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class PCMSSearchModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private PCMSSearchDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public PCMSSearchModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new PCMSSearchDaoImpl(this.database);
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

	public String handlerTempTableCustomerSearchList(List<String> customerNameList, List<String> customerShortNameList) {
		// TODO Auto-generated method stub
		String list = this.dao.handlerTempTableCustomerSearchList(customerNameList,customerShortNameList);
		return list;
	}

	public void handlerCloseTempTableCustomerSearchList(){
		// TODO Auto-generated method stub
		 this.dao.handlerCloseTempTableCustomerSearchList( );
		return;
	} 

}
