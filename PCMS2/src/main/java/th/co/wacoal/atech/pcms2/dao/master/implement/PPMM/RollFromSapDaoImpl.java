package th.co.wacoal.atech.pcms2.dao.master.implement.PPMM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.RollFromSapDao;
import th.co.wacoal.atech.pcms2.entities.PODetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class RollFromSapDaoImpl implements RollFromSapDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String selectPO = ""
			+ "     [ProductionOrder]\r\n"
		    + "   , [RollNumber]\r\n"
		    + "   , [RollWeight]\r\n"
			+ "   , [RollLength]\r\n"
			+ "   , [POCreatedate]\r\n"
			+ "   , [PurchaseOrder]\r\n"
			+ "   , CASE PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"
			+ "		 	ELSE SUBSTRING( [PurchaseOrderLine] , PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' '), LEN( [PurchaseOrderLine] ) )\r\n"
			+ "       	END AS [PurchaseOrderLine] \r\n"
			+ "   , [RequiredDate]\r\n"
			+ "   , [PurchaseOrderDate]\r\n"
			+ "   , [PODefault]\r\n"
			+ "   , [POLineDefault]\r\n"
			+ "   , [POPostingDateDefault]\r\n"
			+ "   , a.[DataStatus] \r\n"; 
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public RollFromSapDaoImpl(Database database ) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}
	@Override
	public  ArrayList<PODetail> getRollFromSapDetailByProductionOrder(String prodOrder){
		ArrayList<PODetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] in ( 'O' ) \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				+ this.selectPO
				+ " from [PCMS].[dbo].[RollFromSap] as a \r\n "
				+ where
				+ " Order by [RollNumber]";
//
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

}
