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
import entities.SwitchProdOrderDetail;
import entities.WaitTestDetail;
import entities.WorkInLabDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class SwitchProdOrderDaoImpl implements  SwitchProdOrderDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public SwitchProdOrderDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	} 
	@Override
	public  ArrayList<SwitchProdOrderDetail> getSWProdOrderDetailByPrd(String prodOrder) {
		ArrayList<SwitchProdOrderDetail> list = null; 
		String sql = "SELECT \r\n"
				+ "       [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrderSW]\r\n"   
				+ "      ,[SaleLineSW]\r\n"
				+ "      ,[ProductionOrderSW]\r\n"  
				+ "  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "  where DataStatus = 'O' and \r\n"
				+ "       [ProductionOrderSW] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";      
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SwitchProdOrderDetail>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genSwitchProdOrderDetail(map)); }
		return list;
	}
	@Override
	public  ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrdSW(String prodOrder) {
		ArrayList<SwitchProdOrderDetail> list = null; 
		String sql = 
				  " SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrderSW]\r\n"   
				+ "      ,[SaleLineSW]\r\n"
				+ "      ,[ProductionOrderSW]\r\n"  
				+ "  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderSW] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrderSW] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";      
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SwitchProdOrderDetail>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genSwitchProdOrderDetail(map)); }
		return list;
	}
	@Override
	public  ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrd(String prodOrder) {
		ArrayList<SwitchProdOrderDetail> list = null; 
		String sql = 
				  " SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrderSW]\r\n"   
				+ "      ,[SaleLineSW]\r\n"
				+ "      ,[ProductionOrderSW]\r\n"  
				+ "  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderSW] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";      
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SwitchProdOrderDetail>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genSwitchProdOrderDetail(map)); }
		return list;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrderForHandlerSwitchProd(String prdOrderSW) {
		ArrayList<PCMSSecondTableDetail> list = new ArrayList<PCMSSecondTableDetail>(); 
		String sql = 
				  " SELECT \r\n"
				+ "			a.[ProductionOrder]\r\n"
				+ "      	,[SaleOrder]\r\n"
				+ "      	,[SaleLine] \r\n"
				+ "	     	,isnull( countInSW ,0) as CountInSW\r\n"
				+ "  FROM [PCMS].[dbo].[FromSapMainProd] AS A\r\n"
				+ "  LEFT JOIN (SELECT ProductionOrderSW, 1 as countInSW  \r\n"
				+ "			  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "			  where DataStatus = 'O' AND  ProductionOrderSW = '"+prdOrderSW+"' \r\n"
				+ "			  group by ProductionOrderSW ) AS B ON A.ProductionOrder = B.ProductionOrderSW\r\n"
				+ "  where SaleOrder <> '' AND a.ProductionOrder = '"+prdOrderSW+"' \r\n"
				+ " ";  
		list = new ArrayList<PCMSSecondTableDetail>();
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}
		return list;   
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrder(String prodOrder) {
		ArrayList<PCMSSecondTableDetail> list = null; 
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String where = " ( \r\n"; 
		where += " a.ProductionOrder = '"+prodOrder+"' ";
		where += " ) \r\n";
		String sql = 
				  " SELECT   \r\n"
				+ "     case \r\n"
				+ "			when a.ProductionOrder = ProductionOrderSW then 'MAIN'\r\n"
				+ "			ELSE 'SUB' \r\n"
				+ "			END	TypePrdRemark \r\n" 
				+ "     ,[ProductionOrderSW] as ProductionOrder\r\n"
				+ "	    ,b.SwitchRemark\r\n"
				+ "     ,[DataStatus] \r\n"
				+ " FROM [PCMS].[dbo].[SwitchProdOrder]  as a\r\n"
				+ " left join [PCMS].[dbo].[InputSwitchRemark] as b on a.ProductionOrderSW = b.ProductionOrder\r\n"
				+ " WHERE DataStatus = 'O' AND \r\n"
				+where   ; 

		list = new ArrayList<PCMSSecondTableDetail>();
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}
		return list;   
	}
	@Override
	public  PCMSSecondTableDetail updateSwitchProdOrderDetail( PCMSSecondTableDetail bean ,String dataStatus) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();    
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime(); 
		try {      
			String sql =  
					  " UPDATE [PCMS].[dbo].[SwitchProdOrder]" 
					+ " 	SET [DataStatus] = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? " 
					+ " declare  @rc int = @@ROWCOUNT "  
					+ ";"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, dataStatus);
				prepared.setString(2, bean.getUserId());
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);   
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("upsertSwitchPrd"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean;
	}
}
