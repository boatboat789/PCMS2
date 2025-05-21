package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.SearchSettingDao;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class SearchSettingDaoImpl implements SearchSettingDao {
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
	public SearchSettingDaoImpl(Database database) {
		this.database = database;
		this.message = "";
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
				+ "	[EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
				+ "  ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
				+ "  ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
				+ "   ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[Division],[PurchaseOrder]\r\n"
				+ " FROM [PCMS].[dbo].[SearchSetting]\r\n"
				+ " where [EmployeeID] = '"
				+ userId
				+ "' and [ForPage] = '"
				+ forPage
				+ "' "; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSearchTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSTableDetail> insertSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage)
	{
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		ArrayList<PCMSTableDetail> list = new ArrayList<>();
		String customerShortName = "",saleNumber = "",materialNo = "",saleOrder = "",saleCreateDate = "",labNo = "",
				articleFG = "",designFG = "",userStatus = "",prdOrder = "",prdCreateDate = "",deliveryStatus = "",saleStatus = "",
				dist = "",customerName = "",dueDate = "",userId = "",division = "";
		PCMSTableDetail bean = poList.get(0);
		userId = bean.getUserId();
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
		try {
			String sql = " INSERT INTO [dbo].[SearchSetting]\r\n"
					+ "           ( [EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
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
			int index = 1;
			prepared = connection.prepareStatement(sql);
			prepared.setString(index ++ , userId);
			prepared.setInt(index ++ , no);
			prepared.setString(index ++ , customerName);
			prepared.setString(index ++ , customerShortName);
			prepared.setString(index ++ , saleOrder);
			prepared.setString(index ++ , articleFG);
			prepared.setString(index ++ , designFG);
			prepared.setString(index ++ , prdOrder);
			prepared.setString(index ++ , saleNumber);
			prepared.setString(index ++ , materialNo);
			prepared.setString(index ++ , labNo);
			prepared.setString(index ++ , deliveryStatus);
			prepared.setString(index ++ , dist);
			prepared.setString(index ++ , saleStatus);
			prepared.setString(index ++ , dueDate);
			prepared.setString(index ++ , saleCreateDate);
			prepared.setString(index ++ , prdCreateDate);
			prepared.setString(index ++ , userStatus);
			prepared.setString(index ++ , forPage);
			prepared.setString(index ++ , division);
			prepared.setString(index ++ , po);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) { 
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// this.database.close();
		}
		list.add(bean);
		return list;
	}

	@Override
	public ArrayList<PCMSTableDetail> updateSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage)
	{
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		ArrayList<PCMSTableDetail> list = new ArrayList<>();
		String customerShortName = "",saleNumber = "",materialNo = "",saleOrder = "",saleCreateDate = "",labNo = "",
				articleFG = "",designFG = "",userStatus = "",prdOrder = "",prdCreateDate = "",deliveryStatus = "",saleStatus = "",
				dist = "",customerName = "",dueDate = "",userId = "",division = "";
		PCMSTableDetail bean = poList.get(0);
		userId = bean.getUserId();
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
		try {
			String sql = " UPDATE [dbo].[SearchSetting]\r\n"
					+ "  SET [No] = ?  ,[CustomerName] = ?,[CustomerShortName] = ?\r\n"
					+ "      ,[SaleOrder] = ? ,[ArticleFG] = ? ,[DesignFG] =  ? \r\n"
					+ "      ,[ProductionOrder] = ? ,[SaleNumber] = ? ,[MaterialNo] = ?\r\n"
					+ "      ,[LabNo] = ? ,[DeliveryStatus] = ? ,[DistChannel] = ?\r\n"
					+ "      ,[SaleStatus] = ? ,[DueDate] = ? ,[SaleCreateDate] = ? \r\n"
					+ "      ,[PrdCreateDate] = ? ,[UserStatus] = ? , [Division] = ? , [PurchaseOrder] = ?\r\n"
					+ "  where  [EmployeeID] = ? and [ForPage] = ?";
			prepared = connection.prepareStatement(sql);
//				prepared.setString(1, userId);
			int index = 1;
			prepared.setInt(index ++ , no);
			prepared.setString(index ++ , customerName);
			prepared.setString(index ++ , customerShortName);
			prepared.setString(index ++ , saleOrder);
			prepared.setString(index ++ , articleFG);
			prepared.setString(index ++ , designFG);
			prepared.setString(index ++ , prdOrder);
			prepared.setString(index ++ , saleNumber);
			prepared.setString(index ++ , materialNo);
			prepared.setString(index ++ , labNo);
			prepared.setString(index ++ , deliveryStatus);
			prepared.setString(index ++ , dist);
			prepared.setString(index ++ , saleStatus);
			prepared.setString(index ++ , dueDate);
			prepared.setString(index ++ , saleCreateDate);
			prepared.setString(index ++ , prdCreateDate);
			prepared.setString(index ++ , userStatus);
			prepared.setString(index ++ , division);
			prepared.setString(index ++ , po);
			prepared.setString(index ++ , userId);
			prepared.setString(index ++ , forPage);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Save Success.");
		} catch (SQLException e) { 
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// this.database.close();
		}
		list.add(bean);
		return list;
	}

}
