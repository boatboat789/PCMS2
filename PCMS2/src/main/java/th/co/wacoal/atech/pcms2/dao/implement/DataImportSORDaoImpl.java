package th.co.wacoal.atech.pcms2.dao.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.DataImportSORDao;
import th.co.wacoal.atech.pcms2.entities.SORDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.model.master.FromSORCFMModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;
@Repository // Spring annotation to mark this as a DAO component
public class DataImportSORDaoImpl implements DataImportSORDao {
	private Database database;
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private String message;
	private BeanCreateModel bcModel = new BeanCreateModel();

    @Autowired
	public DataImportSORDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public void upSertSORToPCMS()
	{
		FromSORCFMModel fscModel = new FromSORCFMModel();
		ArrayList<SORDetail> list = this.getSORdetail();
		@SuppressWarnings("unused") String value = fscModel.upSertFromSORCFMDetail(list);
	}

	private ArrayList<SORDetail> getSORdetail()
	{
		ArrayList<SORDetail> list = null;
		String sql = ""
				+ " SELECT DISTINCT  	"
				+ "	viewPCMS2.[SO_NO]\r\n"
				+ " ,viewPCMS2.[SO_Line]  \r\n"
				+ "	,viewPCMS2.CFM_DATE\r\n" 
				+ " ,POLI.[LastUpdateCFM]  as [LastUpdateCFM]  \r\n"
				+ " FROM [SOR_PRODUCTION].[dbo].[V_PCMS2]  as viewPCMS2   \r\n"
				+ " inner join [SOR_PRODUCTION].[dbo].[PurchaseOrders] as PO on PO.[No] = viewPCMS2.PO_NO\r\n"
				+ " inner join [SOR_PRODUCTION].[dbo].[POLineItems] as POLI on PO.Id = POLI.[POId] and viewPCMS2.MaterialCode = POLI.MaterialCode\r\n"
				+ " where [SaleOrderId] is not null and "
				+ "		  POLI.[IsActive] = 1 and  \r\n"
				+ "       (CONVERT(date, POLI.[LastUpdateCFM]) > CONVERT(date, GETDATE()-1)  )\r\n"
				+ " "
				+ "  ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSORDetail(map));
		}
		return list;
	}

}
