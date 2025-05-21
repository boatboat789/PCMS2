	package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapDyeingDao;
import th.co.wacoal.atech.pcms2.entities.DyeingDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapDyeingDaoImpl implements  FromSapDyeingDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String selectDyeing = ""
			+ "      \r\n"
			+ "dfs.[ProductionOrder] \r\n"
			+ ",dfs.[LotNo]\r\n"
			+ ",dfs.[Operation]	\r\n"
			+ ",dfs.[OperationEndDate] as WorkDate\r\n"
			+ ",dfs.[WorkCenter]\r\n"
			+ ",sfc.[CartNo]\r\n"
			+ ",sfc.[CartType] \r\n"
			+ ",sfc.[DyeingStatus]\r\n"
			+ ",sfc.[Da]\r\n"
			+ ",sfc.[Db]\r\n"
			+ ",sfc.[ST]\r\n"
			+ ",sfc.[L]\r\n"
			+ ",sfc.[ValDeltaE]\r\n"
			+ ",sfc.[DyeRemark] \r\n"
			+ ",sfc.[ColorCheckStatus] \r\n"
			+ ",sfc.[ColorCheckRemark]\r\n ";
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
    public FromSapDyeingDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<DyeingDetail> getFromSapDyeingDetailByProductionOrder(String prodOrder){
		ArrayList<DyeingDetail> list = null;
		String where = " where  "; 
		where += " dfs.ProductionOrder = '" + prodOrder + "'  and "
				+ "		dfs.[Operation] >= 100 and dfs.Operation <= 104 and\r\n"
				+ "	    dfs.[AdminStatus] = '-'\r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				 + this.selectDyeing
				 + " \r\n"
				 + "  FROM [PPMM].[dbo].[DataFromSap] as dfs  \r\n"
				 + "  left join [PPMM].[dbo].[ShopFloorControlDetail] as sfc on sfc.[ProductionOrder] = dfs.[ProductionOrder] and\r\n"
				 + "													        sfc.[Operation] = dfs.[Operation]\r\n "
				 + where
				 + " Order by dfs.Operation"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genDyeingDetail(map));
		}
		return list;
	} 
}
