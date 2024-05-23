	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.FromSapMainSaleDao;
import entities.ConfigCustomerUserDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSapMainSaleDaoImpl implements  FromSapMainSaleDao{
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
		list = new ArrayList<>();
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
		list = new ArrayList<>();
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
			}
		}
		where += " ) \r\n";
		String sql =
				  " SELECT distinct \r\n"
				+ "		[CustomerName] \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n "
			    + where
				+ " order by [CustomerName] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
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
			}
		}
		where += " ) \r\n";
		String sql =
				  " SELECT distinct \r\n"
			    + "		[CustomerShortName]  \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n "
			    + where
				+ " order by  [CustomerShortName] \r\n";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
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
		list = new ArrayList<>();
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
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}
		return list;
	}

}
