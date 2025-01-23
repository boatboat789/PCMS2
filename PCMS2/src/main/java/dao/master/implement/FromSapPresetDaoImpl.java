	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import dao.master.FromSapPresetDao;
import entities.PresetDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapPresetDaoImpl implements  FromSapPresetDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String selectPreset = ""
			+ "       [ProductionOrder]"
			+ "      ,[PostingDate]"
			+ "      ,[WorkCenter]\r\n"
			+ "      ,[Operation]"
			+ "      ,[No]"
			+ "      ,[DataStatus] \r\n";
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
    public FromSapPresetDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<PresetDetail> getFromSapPresetDetailByProductionOrder(String prodOrder){
		ArrayList<PresetDetail> list = null;
		String where = " where  "; 
		where += ""
				+ " a.ProductionOrder = '" + prodOrder + "'  and \r\n"
				+ "	a.[DataStatus] = 'O' \r\n";
		String sql =
				" SELECT DISTINCT \r\n "
		       + this.selectPreset
		       + " from [PCMS].[dbo].[FromSapPreset] as a \r\n "
		       + where;
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPresetDetail(map));
		}
		return list;
	} 
}
