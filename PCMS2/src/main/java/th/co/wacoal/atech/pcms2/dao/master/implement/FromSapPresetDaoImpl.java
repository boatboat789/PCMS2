package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapPresetDao;
import th.co.wacoal.atech.pcms2.entities.PresetDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

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
	private BeanCreateService bcModel = new BeanCreateService();
	private JdbcTemplate jdbc;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
    public FromSapPresetDaoImpl(@Qualifier("pcmsDatabase")JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<PresetDetail> getFromSapPresetDetailByProductionOrder(String prodOrder){
		ArrayList<PresetDetail> list = null;
		String where = " where  ";
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		where += ""
				+ " a.ProductionOrder = '" + prodOrderSafe + "'  and \r\n"
				+ "	a.[DataStatus] = 'O' \r\n";
		String sql =
				" SELECT DISTINCT \r\n "
		       + this.selectPreset
		       + " from [PCMS].[dbo].[FromSapPreset] as a \r\n "
		       + where;
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPresetDetail(map));
		}
		return list;
	}
}
