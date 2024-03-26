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
import dao.master.FromSapMainProdDao;
import dao.master.FromSapMainSaleDao;
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

public class FromSapMainSaleDaoImpl implements  FromSapMainSaleDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public FromSapMainSaleDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	} 
	@Override
	public ArrayList<PCMSSecondTableDetail> getDivisionDetail() {
		ArrayList<PCMSSecondTableDetail> list = null; 
		String sql =    
				  "SELECT distinct \r\n"
			    + "		[Division] \r\n"   
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n"
				+ " where Division <> '' " 
				+ " order by [Division] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		} 
		return list;
	} 
	@Override
	public ArrayList<PCMSAllDetail> getCustomerNameDetail() {
		ArrayList<PCMSAllDetail> list = null; 
		String sql = 
				  " SELECT distinct \r\n"
				+ "		[CustomerName] \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
				+ " order by [CustomerName] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}
	@Override
	public ArrayList<PCMSAllDetail> getCustomerNameDetail(ArrayList<ConfigCustomerUserDetail> poList) {
		ArrayList<PCMSAllDetail> list = null; 
		ConfigCustomerUserDetail bean = poList.get(0);
		 String custNo = bean.getCustomerNo();
		 String where = " where (";
		 String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i++) {
			where += " [CustomerNo] = '"+array[i]+"' " ;
			if (i != array.length - 1) {
				where += " or \r\n";
			} ;
		}
		where += " ) \r\n";
		String sql =      
				  " SELECT distinct \r\n"
				+ "		[CustomerName] \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
			    + where
				+ " order by [CustomerName] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail(ArrayList<ConfigCustomerUserDetail> poList) {
		ArrayList<PCMSAllDetail> list = null; 
		ConfigCustomerUserDetail bean = poList.get(0);
		 String custNo = bean.getCustomerNo();
		 String where = " where (";
		 String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i++) {
			where += " [CustomerNo] = '"+array[i]+"' " ;
			if (i != array.length - 1) {
				where += " or \r\n";
			} ;
		}
		where += " ) \r\n";
		String sql =   
				  " SELECT distinct \r\n"
			    + "		[CustomerShortName]  \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
			    + where
				+ " order by  [CustomerShortName] \r\n"; 
		  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail() {
		ArrayList<PCMSAllDetail> list = null; 
		String sql =   
				  "SELECT distinct \r\n"
			    + "		[CustomerShortName]  \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
				+ " order by  [CustomerShortName] \r\n"; 
		  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}	@Override
	public ArrayList<PCMSTableDetail> getSaleNumberDetail() {
		ArrayList<PCMSTableDetail> list = null;
		String sql = 
				  " SELECT DISTINCT \r\n " 
				+ "	   a.SaleNumber\r\n"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" 
				+ "    		 WHEN 0 THEN ''   \r\n"
				+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+[SaleFullName]\r\n"
				+ "    		 END AS [SaleFullName]   " 
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n "
				+ " where SaleNumber <> '00000000' \r\n" 
				+ " Order by [SaleNumber]"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}
		return list;
	}

}
