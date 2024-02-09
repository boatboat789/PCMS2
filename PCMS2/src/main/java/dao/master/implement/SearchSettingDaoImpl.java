	package dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import dao.PCMSMainDao;
import dao.master.FromSORCFMDao;
import dao.master.SearchSettingDao;
import dao.master.SwitchProdOrderDao;
import entities.CFMDetail;
import entities.ColumnHiddenDetail;
import entities.ConfigCustomerUserDetail;
import entities.DyeingDetail;
import entities.FinishingDetail;
import entities.InputDateDetail;
import entities.InspectDetail;
import entities.NCDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import entities.PODetail;
import entities.PackingDetail;
import entities.PresetDetail;
import entities.ReceipeDetail;
import entities.SORDetail;
import entities.SaleDetail;
import entities.SaleInputDetail;
import entities.SendTestQCDetail;
import entities.WaitTestDetail;
import entities.WorkInLabDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class SearchSettingDaoImpl implements  SearchSettingDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

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
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSearchTableDetail(map));
		} 
		return list;
	} 

	@Override
	public  ArrayList<PCMSTableDetail> insertSearchSettingDetai(ArrayList<PCMSTableDetail> poList, String forPage) {
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();    
		ArrayList<PCMSTableDetail> list = new ArrayList<PCMSTableDetail>(); 
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,division = ""; ;
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
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.out.println("insertSearchSettingDetail"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}

	@Override
	public ArrayList<PCMSTableDetail> updateSearchSettingDetai(ArrayList<PCMSTableDetail> poList, String forPage) { 
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();    
		ArrayList<PCMSTableDetail> list = new ArrayList<PCMSTableDetail>(); 
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
			bean.setIconStatus("I");
			bean.setSystemStatus("save Success.");
		} catch (SQLException e) {
			System.out.println("updateSearchSettingDetai"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
	return list; 
	}

}
