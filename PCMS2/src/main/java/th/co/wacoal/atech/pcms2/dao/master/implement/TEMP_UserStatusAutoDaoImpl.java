package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.TEMP_UserStatusAutoDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.TempUserStatusAutoDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class TEMP_UserStatusAutoDaoImpl implements TEMP_UserStatusAutoDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public TEMP_UserStatusAutoDaoImpl(@Qualifier("pcmsDatabase")Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<TempUserStatusAutoDetail> getTempUserStatusAutoDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		ArrayList<TempUserStatusAutoDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		String saleLine = bean.getSaleLine();
		String sql = String.format(""
				+ "SELECT DISTINCT "
				+ "		[Id], [ProductionOrder], [SaleOrder], [SaleLine], [ProductionOrderRPM], "
				+ "		[Volumn], [Grade], [UserStatusCal], [UserStatusCalRP], [DataStatus], [ChangeDate], [CreateDate] "
				+ "FROM [PCMS].[dbo].[TEMP_UserStatusAuto] "
				+ "WHERE (GRADE IS NULL OR GRADE = '') "
				+ "	AND ProductionOrder = '%s' "
				+ "	AND SaleOrder = '%s' "
				+ "	AND SaleLine = '%s' "
				+ "	AND DataStatus = 'O'", prdOrder, saleOrder, saleLine);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempUserStatusAutoDetail(map));
		}
		return list;
	}
}
