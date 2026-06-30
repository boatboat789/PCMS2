package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapWorkInLabDao;
import th.co.wacoal.atech.pcms2.entities.WorkInLabDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapWorkInLabDaoImpl  implements  FromSapWorkInLabDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private JdbcTemplate jdbc;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
    public FromSapWorkInLabDaoImpl(@Qualifier("pcmsDatabase")JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<WorkInLabDetail> getFromSapWorkInLabDetailByProductionOrder(String prodOrder){
		ArrayList<WorkInLabDetail> list = null;
		String where = " where  ";
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		where += " a.ProductionOrder = '" + prodOrderSafe + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				+ "   [ProductionOrder],[SaleOrder]\r\n"
				+ "   ,[SaleLine],[No],[SendDate],[NOK]\r\n"
				+ "   ,[LotNo],[ReceiveDate],[Remark],[Da]\r\n"
				+ "   ,[Db],[L],[ST],a.[DataStatus]\r\n" + "   "
				+ " from [PCMS].[dbo].[FromSapWorkInLab] as a \r\n "
				+ where
				+ " Order by No";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genWorkInLabDetail(map));
		}
		return list;
	}
}
