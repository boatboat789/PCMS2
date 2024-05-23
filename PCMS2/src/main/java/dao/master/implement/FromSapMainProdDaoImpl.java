	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapMainProdDao;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapMainProdDaoImpl implements  FromSapMainProdDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public FromSapMainProdDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrder) {
		ArrayList<PCMSSecondTableDetail> list = null;
		String sql =
				  " SELECT DISTINCT * \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainProd] \r\n"
				+ " where \r\n "
				+ " 	ProductionOrder = '"+prdOrder+"' and \r\n"
				+ " 	( DataStatus = 'O' ) "   ;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}
	@Override
	public ArrayList<PCMSAllDetail> getUserStatusDetail( ) {
		ArrayList<PCMSAllDetail> list = null;
		String where = " where UserStatus <> '' \r\n";
		String sql =
				  "SELECT distinct \r\n"
				+ "		[UserStatus] \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainProd] \r\n "
			    + where
				+ " order by UserStatus \r\n";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		return list;
	}

}
