package th.co.wacoal.atech.pcms2.dao.master.implement.InspectSystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.InspectSystem.InspectOrdersDao;
import th.co.wacoal.atech.pcms2.entities.PPMM.InspectOrdersDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Component
public class InspectOrdersDaoImpl implements  InspectOrdersDao{
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
	public InspectOrdersDaoImpl ( @Qualifier("pcmsDatabase")JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<InspectOrdersDetail> getInspectOrdersByProductionOrder(String prodOrder){
		ArrayList<InspectOrdersDetail> list = null;
		String where = " where  ";
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		where += " "
				+ " PrdNumber = '" + prodOrderSafe + "'  \r\n" ;
		String sql = ""
				+ " SELECT [Id]\r\n"
				+ "      ,[PrdNumber]  \r\n"
				+ "      ,[InspectNote] \r\n"
				+ "      ,[RollupNote] \r\n"
				+ "      ,[PackingNote] \r\n"
				+ "      ,[MachineInspect]\r\n"
				+ "      ,[MachineRollup]\r\n"
				+ "      ,[MachinePacking] \r\n"
				+ "  FROM [InspectSystem].[dbo].[InspectOrders] \r\n"
				+ where
				+ "  \r\n ";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInspectOrdersDetail(map));
		}
		return list;
	}
}
