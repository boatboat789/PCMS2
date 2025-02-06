package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainSaleDao;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapMainSaleDaoImpl implements FromSapMainSaleDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSapMainSaleDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getDivisionDetail()
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		String sql = "SELECT distinct \r\n"
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
	public ArrayList<PCMSAllDetail> getCustomerNameDetail()
	{
		ArrayList<PCMSAllDetail> list = null;
		String sql = " SELECT distinct \r\n"
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
	public ArrayList<PCMSAllDetail> getCustomerNameDetail(ArrayList<ConfigCustomerUserDetail> poList)
	{
		ArrayList<PCMSAllDetail> list = null;
		ConfigCustomerUserDetail bean = poList.get(0);
		String custNo = bean.getCustomerNo();
		String where = " where (";
		String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i ++ ) {
			where += " [CustomerNo] = '" + array[i] + "' ";
			if (i != array.length-1) {
				where += " or \r\n";
			}
		}
		where += " ) \r\n";
		String sql = " SELECT distinct \r\n"
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
	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail(ArrayList<ConfigCustomerUserDetail> poList)
	{
		ArrayList<PCMSAllDetail> list = null;
		ConfigCustomerUserDetail bean = poList.get(0);
		String custNo = bean.getCustomerNo();
		String where = " where (";
		String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i ++ ) {
			where += " [CustomerNo] = '" + array[i] + "' ";
			if (i != array.length-1) {
				where += " or \r\n";
			}
		}
		where += " ) \r\n";
		String sql = " SELECT distinct \r\n"
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
	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail()
	{
		ArrayList<PCMSAllDetail> list = null;
		String sql = "SELECT distinct \r\n"
				+ "		[CustomerShortName]  \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n "
				+ " order by  [CustomerShortName] \r\n";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSTableDetail> getSaleNumberDetail()
	{
		ArrayList<PCMSTableDetail> list = null;
		String sql = " SELECT DISTINCT \r\n "
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

	@Override
	public String upsertFromSapMainSaleDetail(ArrayList<FromErpMainSaleDetail> paList)
	{
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();

		String iconStatus = "I";
		String sql = "-- Update if the record exists\r\n"
				+ "UPDATE [dbo].[FromSapMainSale]\r\n"
				+ "SET \r\n"
				+ "    [MaterialNo] = ?,\r\n"
				+ "    [DueDate] = ?,\r\n"
				+ "    [PlanGreigeDate] = ?,\r\n"
				+ "    [SaleUnit] = ?,\r\n"
				+ "    [SaleQuantity] = ?,\r\n"
				+ "    [CustomerMaterial] = ?,\r\n"
				+ "    [Color] = ?,\r\n"
				+ "    [CustomerNo] = ?,\r\n"
				+ "    [PurchaseOrder] = ?,\r\n"
				+ "    [SaleOrg] = ?,\r\n"
				+ "    [DistChannel] = ?,\r\n"
				+ "    [Division] = ?,\r\n"
				+ "    [CustomerName] = ?,\r\n"
				+ "    [CustomerShortName] = ?,\r\n"
				+ "    [ColorCustomer] = ?,\r\n"
				+ "    [CustomerDue] = ?,\r\n"
				+ "    [RemainQuantity] = ?,\r\n"
				+ "    [ShipDate] = ?,\r\n"
				+ "    [SaleStatus] = ?,\r\n"
				+ "    [Currency] = ?,\r\n"
				+ "    [Price] = ?,\r\n"
				+ "    [OrderAmount] = ?,\r\n"
				+ "    [RemainAmount] = ?,\r\n"
				+ "    [SaleCreateDate] = ?,\r\n"
				+ "    [SaleNumber] = ?,\r\n"
				+ "    [SaleFullName] = ?,\r\n"
				+ "    [DeliveryStatus] = ?,\r\n"
				+ "    [DesignFG] = ?,\r\n"
				+ "    [ArticleFG] = ?,\r\n"
				+ "    [OrderSheetPrintDate] = ?,\r\n"
				+ "    [CustomerMaterialBase] = ?,\r\n"
				+ "    [ChangeDate] = ?,\r\n"
//				+ "    [DataStatus] = ? \r\n" 
				+ "    [SyncDate] =  ?\r\n"
				+ "WHERE \r\n"
				+ "    [SaleOrder] = ? and"
				+ "    [SaleLine] = ?  "
				+ "    ;\r\n"
				+ "\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "    PRINT @rc;\r\n"
				+ "ELSE \r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[FromSapMainSale] (\r\n"
				+ "        [SaleOrder] ,[SaleLine] ,[MaterialNo] ,[DueDate] ,[PlanGreigeDate]\r\n"
				+ "      ,[SaleUnit] ,[SaleQuantity] ,[CustomerMaterial] ,[Color] ,[CustomerNo]\r\n"
				+ "      ,[PurchaseOrder] ,[SaleOrg] ,[DistChannel] ,[Division] ,[CustomerName]\r\n"
				+ "      ,[CustomerShortName] ,[ColorCustomer] ,[CustomerDue] ,[RemainQuantity] ,[ShipDate]\r\n"
				+ "      ,[SaleStatus] ,[Currency] ,[Price] ,[OrderAmount] ,[RemainAmount]\r\n"
				+ "      ,[SaleCreateDate] ,[SaleNumber] ,[SaleFullName] ,[DeliveryStatus] ,[DesignFG]\r\n"
				+ "      ,[ArticleFG] ,[OrderSheetPrintDate] ,[CustomerMaterialBase] ,[ChangeDate] ,[CreateDate]\r\n"
//				+ "      ,[DataStatus]\r\n"
				+ "      ,[SyncDate] \r\n"
				+ "    ) VALUES (\r\n"
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "// 10
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "// 10
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "// 10
				+ "?, ?, ?, ?, ?, "
//				+ "? \r\n"
				+ "? "
				+ "    ); "
				+ ";";
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql);
			for (FromErpMainSaleDetail bean : paList) {
				index = 1;

				prepared.setString(index ++ , bean.getMaterialNo());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getDueDate(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getPlanGreigeDate(), index ++ );
				prepared.setString(index ++ , bean.getSaleUnit());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getSaleQuantity(), index ++ );
				prepared.setString(index ++ , bean.getCustomerMaterial()); 
				prepared.setString(index ++ , bean.getColor());
				prepared.setString(index ++ , bean.getCustomerNo());
				prepared.setString(index ++ , bean.getPurchaseOrder());
				prepared.setString(index ++ , bean.getSaleOrg());
				prepared.setString(index ++ , bean.getDistChannel());
				prepared.setString(index ++ , bean.getDivision());
				prepared.setString(index ++ , bean.getCustomerName());
				prepared.setString(index ++ , bean.getCustomerShortName()); 
				prepared.setString(index ++ , bean.getColorCustomer());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCustomerDue(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRemainQuantity(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getShipDate(), index ++ );
				prepared.setString(index ++ , bean.getSaleStatus());
				prepared.setString(index ++ , bean.getCurrency());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getPrice(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getOrderAmount(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRemainAmount(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getSaleCreateDate(), index ++ );
				prepared.setString(index ++ , bean.getSaleNumber());
				prepared.setString(index ++ , bean.getSaleFullName());
				prepared.setString(index ++ , bean.getDeliveryStatus());
				prepared.setString(index ++ , bean.getDesignFG());
				prepared.setString(index ++ , bean.getArticleFG());
				prepared.setString(index ++ , bean.getOrderSheetPrintDate());
				prepared.setString(index ++ , bean.getCustomerMaterialBase());
				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getDataStatus());
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );

				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());

				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared.setString(index ++ , bean.getMaterialNo());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getDueDate(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getPlanGreigeDate(), index ++ );
				prepared.setString(index ++ , bean.getSaleUnit());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getSaleQuantity(), index ++ );
				prepared.setString(index ++ , bean.getCustomerMaterial());
				prepared.setString(index ++ , bean.getColor());
				prepared.setString(index ++ , bean.getCustomerNo());
				prepared.setString(index ++ , bean.getPurchaseOrder());
				prepared.setString(index ++ , bean.getSaleOrg());
				prepared.setString(index ++ , bean.getDistChannel());
				prepared.setString(index ++ , bean.getDivision());
				prepared.setString(index ++ , bean.getCustomerName());
				prepared.setString(index ++ , bean.getCustomerShortName());
				prepared.setString(index ++ , bean.getColorCustomer());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCustomerDue(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRemainQuantity(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getShipDate(), index ++ );
				prepared.setString(index ++ , bean.getSaleStatus());
				prepared.setString(index ++ , bean.getCurrency());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getPrice(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getOrderAmount(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRemainAmount(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getSaleCreateDate(), index ++ );
				prepared.setString(index ++ , bean.getSaleNumber());
				prepared.setString(index ++ , bean.getSaleFullName());
				prepared.setString(index ++ , bean.getDeliveryStatus());
				prepared.setString(index ++ , bean.getDesignFG());
				prepared.setString(index ++ , bean.getArticleFG());
				prepared.setString(index ++ , bean.getOrderSheetPrintDate());
				prepared.setString(index ++ , bean.getCustomerMaterialBase());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getDataStatus());
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );

				prepared.addBatch();
//				prepared.setString(index++, bean.get    );
//				prepared = this.sshUtl.setSqlDate(prepared, bean.get , index++); 
//				prepared.setTimestamp(index++, new Timestamp(time));
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.get , index++); 
			}
			prepared.executeBatch();
			prepared.close();
		} catch (SQLException e) {
			System.err.println(e);
			iconStatus = "E";
		} finally {
			// this.database.close();
		}
		return iconStatus;
	}

}
