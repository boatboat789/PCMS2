package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.SearchSettingDao;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class SearchSettingDaoImpl implements SearchSettingDao {
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
	public SearchSettingDaoImpl(@Qualifier("pcmsDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId, String forPage)
	{
		ArrayList<PCMSTableDetail> list = null;
		String sql = " SELECT \r\n"
				+ "	[EmployeeId] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
				+ "  ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
				+ "  ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
				+ "   ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[Division],[PurchaseOrder]\r\n"
				+ " FROM [PCMS].[dbo].[SearchSetting]\r\n"
				+ " where [EmployeeId] = '"
				+ userId
				+ "' and [ForPage] = '"
				+ forPage
				+ "' ";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSearchTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSTableDetail> insertSearchSettingDetail(String user, ArrayList<PCMSTableDetail> poList, String forPage)
	{
		// TODO Auto-generated method stub

		ArrayList<PCMSTableDetail> list = new ArrayList<>();
		String customerShortName = "",saleNumber = "",materialNo = "",saleOrder = "",saleCreateDate = "",labNo = "",
				articleFG = "",designFG = "",userStatus = "",prdOrder = "",prdCreateDate = "",deliveryStatus = "",saleStatus = "",
				dist = "",customerName = "",dueDate = "",division = "";
		PCMSTableDetail bean = poList.get(0);
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();
		saleCreateDate = bean.getSaleOrderCreateDate();
		saleNumber = bean.getSaleNumber();
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		dueDate = bean.getDueDate();
		designFG = bean.getDesignFG();
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel();
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
		userStatus = bean.getUserStatus();
		division = bean.getDivision();
		String po = bean.getPurchaseOrder();
		int no = 1;
		String sql = " INSERT INTO [dbo].[SearchSetting]\r\n"
				+ "           ( [EmployeeId] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
				+ "           ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
				+ "           ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
				+ "           ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[ForPage],[Division] \r\n"
				+ "           ,[PurchaseOrder] \r\n"
				+ "           )\r\n"
				+ " VALUES\r\n"
				+ "           ( "
				+ "            ? , ? , ? , ? , ?, "
				+ "            ? , ? , ? , ? , ?,"
				+ "            ? , ? , ? , ? , ?,"
				+ "            ? , ? , ? , ? , ?,"
				+ "            ?"
				+ "           )";

		try {
			this.jdbc.update(sql,
					user,
					no,
					customerName,
					customerShortName,
					saleOrder,
					articleFG,
					designFG,
					prdOrder,
					saleNumber,
					materialNo,
					labNo,
					deliveryStatus,
					dist,
					saleStatus,
					dueDate,
					saleCreateDate,
					prdCreateDate,
					userStatus,
					forPage,
					division,
					po);
			bean.setIconStatus("I");
			bean.setSystemStatus("อัพเดตข้อมูลสำเร็จ");
		} catch (Exception e) {
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}
		list.add(bean);
		return list;
	}

	@Override
	public ArrayList<PCMSTableDetail> updateSearchSettingDetail(String user, ArrayList<PCMSTableDetail> poList, String forPage)
	{
		// TODO Auto-generated method stub

		ArrayList<PCMSTableDetail> list = new ArrayList<>();
		String customerShortName = "",saleNumber = "",materialNo = "",saleOrder = "",saleCreateDate = "",labNo = "",
				articleFG = "",designFG = "",userStatus = "",prdOrder = "",prdCreateDate = "",deliveryStatus = "",saleStatus = "",
				dist = "",customerName = "",dueDate = "",division = "";
		PCMSTableDetail bean = poList.get(0);
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();
		saleCreateDate = bean.getSaleOrderCreateDate();
		saleNumber = bean.getSaleNumber();
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		dueDate = bean.getDueDate();
		designFG = bean.getDesignFG();
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel();
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
		userStatus = bean.getUserStatus();
		division = bean.getDivision();
		String po = bean.getPurchaseOrder();
		int no = 1;
		String sql = " UPDATE [dbo].[SearchSetting]\r\n"
				+ "  SET [No] = ?  ,[CustomerName] = ?,[CustomerShortName] = ?\r\n"
				+ "      ,[SaleOrder] = ? ,[ArticleFG] = ? ,[DesignFG] =  ? \r\n"
				+ "      ,[ProductionOrder] = ? ,[SaleNumber] = ? ,[MaterialNo] = ?\r\n"
				+ "      ,[LabNo] = ? ,[DeliveryStatus] = ? ,[DistChannel] = ?\r\n"
				+ "      ,[SaleStatus] = ? ,[DueDate] = ? ,[SaleCreateDate] = ? \r\n"
				+ "      ,[PrdCreateDate] = ? ,[UserStatus] = ? , [Division] = ? , [PurchaseOrder] = ?\r\n"
				+ "  where  [EmployeeId] = ? and [ForPage] = ?";

		try {
			this.jdbc.update(sql,
					no,
					customerName,
					customerShortName,
					saleOrder,
					articleFG,
					designFG,
					prdOrder,
					saleNumber,
					materialNo,
					labNo,
					deliveryStatus,
					dist,
					saleStatus,
					dueDate,
					saleCreateDate,
					prdCreateDate,
					userStatus,
					division,
					po,
					user,
					forPage);
			bean.setIconStatus("I");
			bean.setSystemStatus("Save Success.");
		} catch (Exception e) {
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}
		list.add(bean);
		return list;
	}

}
