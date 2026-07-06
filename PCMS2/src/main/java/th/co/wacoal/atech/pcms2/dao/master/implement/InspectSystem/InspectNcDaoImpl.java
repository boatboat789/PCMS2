package th.co.wacoal.atech.pcms2.dao.master.implement.InspectSystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.InspectSystem.InspectNcDao;
import th.co.wacoal.atech.pcms2.entities.NCDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Component
public class InspectNcDaoImpl implements  InspectNcDao{
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
	public InspectNcDaoImpl (@Qualifier("pcmsDatabase")JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<NCDetail> getInspectNcByProductionOrder(String prodOrder){
		ArrayList<NCDetail> list = null;
		String where = " where  ";
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		where += " "
				+ " PrdNumber = '" + prodOrderSafe + "' and \r\n"
				+ " NcSolution <> 'ADMIN หลังบ้านออก'    \r\n";
		String sql = ""
				+ " SELECT [Id]\r\n"
				+ "      ,[PrdNumber]   \r\n"
				+ "	  	 ,CAST( ROW_NUMBER() OVER (PARTITION BY [PrdNumber] ORDER BY [PrdNumber],[NcDate]) AS INT)as No\r\n"
				+ "      ,[NcDate]  \r\n"
				+ "	  	 ,[NcLength]\r\n"
				+ "      ,[NcReceiverBase]\r\n"
				+ "      ,[NcCarNumber] \r\n"
				+ "      ,[NcProblem] \r\n"
				+ "      ,[NcSolution] \r\n"
				+ "  FROM [InspectSystem].[dbo].[InspectNc]\r\n"
				+ where
				+ "  ORDER BY [PrdNumber],NCDATE  \r\n ";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genNCDetail(map));
		}
		return list;
	}
}
