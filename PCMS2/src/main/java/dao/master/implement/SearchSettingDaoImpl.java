	package dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Repository;

import dao.master.SearchSettingDao;
import entities.PCMSTableDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class SearchSettingDaoImpl implements  SearchSettingDao{
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

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId,String forPage) {
		ArrayList<PCMSTableDetail> list = null;
		String sql =
				    " SELECT \r\n"
				  + "	[EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
				  + "  ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
				  + "  ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
				  + "   ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[Division],[PurchaseOrder]\r\n"
				  + " FROM [PCMS].[dbo].[SearchSetting]\r\n"
				  + " where [EmployeeID] = '"+userId+"' and [ForPage] = '"+forPage+ "' ";
//		  System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSearchTableDetail(map));
		}
		return list;
	}

	@Override
	public  ArrayList<PCMSTableDetail> insertSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage) {
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		ArrayList<PCMSTableDetail> list = new ArrayList<>();
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,division = "";
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
			String sql =
					  " INSERT INTO [dbo].[SearchSetting]\r\n"
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
								+ "           )"  ;
				prepared = connection.prepareStatement(sql);
				prepared.setString(1, userId);
				prepared.setInt(2, no);
				prepared.setString(3, customerName);
				prepared.setString(4, customerShortName);
				prepared.setString(5, saleOrder);
				prepared.setString(6, articleFG);
				prepared.setString(7, designFG);
				prepared.setString(8, prdOrder);
				prepared.setString(9, saleNumber);
				prepared.setString(10, materialNo);
				prepared.setString(11, labNo);
				prepared.setString(12, deliveryStatus);
				prepared.setString(13, dist);
				prepared.setString(14, saleStatus);
				prepared.setString(15, dueDate);
				prepared.setString(16, saleCreateDate);
				prepared.setString(17, prdCreateDate);
				prepared.setString(18, userStatus);
				prepared.setString(19, forPage);
				prepared.setString(20, division);
				prepared.setString(21, po);
				prepared.executeUpdate();
				prepared.close();
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
//			System.out.println("insertSearchSettingDetail"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			//this.database.close();
		}
		list.add(bean);
		return list;
	}

	@Override
	public ArrayList<PCMSTableDetail> updateSearchSettingDetail(ArrayList<PCMSTableDetail> poList, String forPage) {
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		ArrayList<PCMSTableDetail> list = new ArrayList<>();
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,division = "";
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
			String sql =
					" UPDATE [dbo].[SearchSetting]\r\n"
							+ "  SET [No] = ?  ,[CustomerName] = ?,[CustomerShortName] = ?\r\n"
							+ "      ,[SaleOrder] = ? ,[ArticleFG] = ? ,[DesignFG] =  ? \r\n"
							+ "      ,[ProductionOrder] = ? ,[SaleNumber] = ? ,[MaterialNo] = ?\r\n"
							+ "      ,[LabNo] = ? ,[DeliveryStatus] = ? ,[DistChannel] = ?\r\n"
							+ "      ,[SaleStatus] = ? ,[DueDate] = ? ,[SaleCreateDate] = ? \r\n"
							+ "      ,[PrdCreateDate] = ? ,[UserStatus] = ? , [Division] = ? , [PurchaseOrder] = ?\r\n"
							+ "  where  [EmployeeID] = ? and [ForPage] = ?" ;
			prepared = connection.prepareStatement(sql);
//				prepared.setString(1, userId);
			prepared.setInt(1, no);
			prepared.setString(2, customerName);
			prepared.setString(3, customerShortName);
			prepared.setString(4, saleOrder);
			prepared.setString(5, articleFG);
			prepared.setString(6, designFG);
			prepared.setString(7, prdOrder);
			prepared.setString(8, saleNumber);
			prepared.setString(9, materialNo);
			prepared.setString(10, labNo);
			prepared.setString(11, deliveryStatus);
			prepared.setString(12, dist);
			prepared.setString(13, saleStatus);
			prepared.setString(14, dueDate);
			prepared.setString(15, saleCreateDate);
			prepared.setString(16, prdCreateDate);
			prepared.setString(17, userStatus);
			prepared.setString(18, division);
			prepared.setString(19, po);
			prepared.setString(20, userId);
			prepared.setString(21, forPage);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("save Success.");
		} catch (SQLException e) {
//			System.out.println("updateSearchSettingDetai"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			//this.database.close();
		}
		list.add(bean);
	return list;
	}

}
