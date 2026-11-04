package th.co.wacoal.atech.pcms2.dao.master.implement.PPMM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.RollFromSapDao;
import th.co.wacoal.atech.pcms2.entities.PODetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;

@Repository // Spring annotation to mark this as a DAO component
public class RollFromSapDaoImpl implements RollFromSapDao {
	private String selectPO = ""
			+ "     [ProductionOrder]\r\n"
			+ "   , [RollNumber]\r\n"
			+ "   , [RollWeight]\r\n"
			+ "   , [RollLength]\r\n"
			+ "   , [POCreatedate]\r\n"
			+ "   , [PurchaseOrder]\r\n"
			+ "   , CASE PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' ')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"
			+ "		 	ELSE SUBSTRING( [PurchaseOrderLine] , PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' '), LEN( [PurchaseOrderLine] ) )\r\n"
			+ "       	END AS [PurchaseOrderLine] \r\n"
			+ "   , [RequiredDate]\r\n"
			+ "   , [PODefault]\r\n"
			+ "   , [POLineDefault]\r\n"
			+ "   , [POPostingDateDefault]\r\n"
			+ "   , a.[DataStatus] \r\n";
	private BeanCreateService bcModel = new BeanCreateService();
	private JdbcTemplate jdbc;

	public RollFromSapDaoImpl(@Qualifier("ppmmDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public ArrayList<PODetail> getRollFromSapDetailByProductionOrder(String prodOrder)
	{
		ArrayList<PODetail> list = null;
		String where = " where  ";
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		where += " a.ProductionOrder = '" + prodOrderSafe + "'  and a.[DataStatus] in ( 'O' ) \r\n";
		String sql = " SELECT DISTINCT  \r\n"
				+ this.selectPO
				+ " from [PPMM].[dbo].[RollFromSap] as a \r\n "
				+ where
				+ " Order by [RollNumber]";
//
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}
}
