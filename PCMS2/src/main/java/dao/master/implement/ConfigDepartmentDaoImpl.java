	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.ConfigDepartmentDao;
import entities.PCMSSecondTableDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class ConfigDepartmentDaoImpl implements  ConfigDepartmentDao{
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

	public ConfigDepartmentDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getDelayedDepartmentList() {
		ArrayList<PCMSSecondTableDetail> list = null;
		String sql =
				  " SELECT distinct [DepName] AS DelayedDep \r\n"
				+ " FROM [PCMS].[dbo].[ConfigDepartment] \r\n"
				+ " where DataStatus = 'O' \r\n"
				+ " order by [DepName] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

}
