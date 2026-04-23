package th.co.wacoal.atech.pcms2.dao.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.DataImportSORDao;
import th.co.wacoal.atech.pcms2.entities.SORDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class DataImportSORDaoImpl implements DataImportSORDao {
	private Database database;  
	private BeanCreateService bcModel = new BeanCreateService();

	@Autowired
	public DataImportSORDaoImpl(@Qualifier("sorDatabase")Database database) {
		this.database = database; 
	}  
	@Override
	public ArrayList<SORDetail> getList()
	{
		ArrayList<SORDetail> list = null;
		String sql = ""
				+ " SELECT DISTINCT  	"
				+ "	viewPCMS2.[SO_NO]\r\n"
				+ " ,viewPCMS2.[SO_Line]  \r\n"
				+ "	,CAST(viewPCMS2.[CFM_DATE] AS DATE) AS [CFM_DATE]\r\n"
				+ " ,POLI.[LastUpdateCFM]  as [LAST_UPDATE_CFM]  \r\n"
				+ " FROM [SOR_PRODUCTION].[dbo].[V_PCMS2]  as viewPCMS2   \r\n"
				+ " inner join [SOR_PRODUCTION].[dbo].[PurchaseOrders] as PO on PO.[No] = viewPCMS2.PO_NO\r\n"
				+ " inner join [SOR_PRODUCTION].[dbo].[POLineItems] as POLI on PO.Id = POLI.[POId] and viewPCMS2.MaterialCode = POLI.MaterialCode\r\n"
				+ " where [SaleOrderId] is not null and "
				+ "		  POLI.[IsActive] = 1 and  \r\n"
				+ "       (CONVERT(date, POLI.[LastUpdateCFM]) > CONVERT(date, GETDATE()-1)  )\r\n" ; 
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSORDetail(map));
		} 
		return list;
	}

}
