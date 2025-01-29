	package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapWaitTestDao;
import th.co.wacoal.atech.pcms2.entities.WaitTestDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapWaitTestDaoImpl implements  FromSapWaitTestDao{
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

	@Autowired
    public FromSapWaitTestDaoImpl (Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<WaitTestDetail> getFromSapWaitTestDetailByProductionOrder(String prodOrder){
		ArrayList<WaitTestDetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				+ "    [ProductionOrder],[No],[DateInTest]\r\n"
				+ "   ,[DateOutTest],[Status],[Remark],a.[DataStatus]\r\n"
				+ " from [PCMS].[dbo].[FromSapWaitTest] as a \r\n "
				+ where
				+ " Order by [No]"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genWaitTestDetail(map));
		}
		return list;
	} 
}
