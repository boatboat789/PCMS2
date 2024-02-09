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
import dao.master.ColumnSettingDao;
import dao.master.ConfigDepartmentDao;
import dao.master.FromSORCFMDao;
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

public class ColumnSettingDaoImpl implements  ColumnSettingDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public ColumnSettingDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	} 
	@Override
	public ArrayList<ColumnHiddenDetail> getColumnVisibleDetail(String user) {
		ArrayList<ColumnHiddenDetail> list = null;  
		String sql = 
				    " SELECT distinct \r\n"
				  + "		[EmployeeID] ,[ColVisibleDetail] ,[ColVisibleSummary]\r\n"
		 		  + " FROM [PCMS].[dbo].[ColumnSetting] \r\n "
		 		  + " where [EmployeeID] = '" + user+ "' ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);  
		list = new ArrayList<ColumnHiddenDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genColumnHiddenDetail(map));
		} 
		return list;
	}  
	@Override
	public ArrayList<ColumnHiddenDetail> upsertColumnSettingDetail(ColumnHiddenDetail pd) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String colName = pd.getColVisibleDetail(); 
		String user = pd.getUserId(); 
		ArrayList<ColumnHiddenDetail> list = new ArrayList<ColumnHiddenDetail>();
		ColumnHiddenDetail bean = new ColumnHiddenDetail(); 
		try {      
			String sql = 
					  " UPDATE [PCMS].[dbo].[ColumnSetting] "
					+ " 	SET [ColVisibleDetail] = ?  "
					+ " 	WHERE [EmployeeID]  = ? "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ " if @rc <> 0 " 
					+ " 	print @rc " 
					+ " else "
					+ " 	INSERT INTO [PCMS].[dbo].[ColumnSetting]	 "
					+ " 		([EmployeeID] ,[ColVisibleDetail])"//55 
					+ " 	values(? , ? )  ;"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, colName);
				prepared.setString(2, user);
				prepared.setString(3, user);
				prepared.setString(4, colName);  
				prepared.executeUpdate();   
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("saveColSettingToServer"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}
 
}
