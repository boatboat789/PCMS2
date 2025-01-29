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

import th.co.wacoal.atech.pcms2.dao.master.SwitchProdOrderDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.SwitchProdOrderDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class SwitchProdOrderDaoImpl implements SwitchProdOrderDao {
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
	public SwitchProdOrderDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<SwitchProdOrderDetail> getSWProdOrderDetailByPrd(String prodOrder)
	{
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
				+ "       [ProductionOrderSW] = '"
				+ prodOrder
				+ "' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSwitchProdOrderDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrdSW(String prodOrder)
	{
		ArrayList<SwitchProdOrderDetail> list = null;
		String sql = " SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrderSW]\r\n"
				+ "      ,[SaleLineSW]\r\n"
				+ "      ,[ProductionOrderSW]\r\n"
				+ "  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderSW] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrderSW] = '"
				+ prodOrder
				+ "' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSwitchProdOrderDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrd(String prodOrder)
	{
		ArrayList<SwitchProdOrderDetail> list = null;
		String sql = " SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrderSW]\r\n"
				+ "      ,[SaleLineSW]\r\n"
				+ "      ,[ProductionOrderSW]\r\n"
				+ "  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderSW] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"
				+ prodOrder
				+ "' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSwitchProdOrderDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrderForHandlerSwitchProd(String prdOrderSW)
	{
		ArrayList<PCMSSecondTableDetail> list = new ArrayList<>();
		String sql = " SELECT \r\n"
				+ "			a.[ProductionOrder]\r\n"
				+ "      	,[SaleOrder]\r\n"
				+ "      	,[SaleLine] \r\n"
				+ "	     	,isnull( countInSW ,0) as CountInSW\r\n"
				+ "  FROM [PCMS].[dbo].[FromSapMainProd] AS A\r\n"
				+ "  LEFT JOIN (SELECT ProductionOrderSW, 1 as countInSW  \r\n"
				+ "			  FROM [PCMS].[dbo].[SwitchProdOrder]\r\n"
				+ "			  where DataStatus = 'O' AND  ProductionOrderSW = '"
				+ prdOrderSW
				+ "' \r\n"
				+ "			  group by ProductionOrderSW ) AS B ON A.ProductionOrder = B.ProductionOrderSW\r\n"
				+ "  where SaleOrder <> '' AND a.ProductionOrder = '"
				+ prdOrderSW
				+ "' \r\n"
				+ " ";
		list = new ArrayList<>();
		List<Map<String, Object>> datas = this.database.queryList(sql);
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrder(String prodOrder)
	{
		ArrayList<PCMSSecondTableDetail> list = null;
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String where = " ( \r\n";
		where += " a.ProductionOrder = '" + prodOrder + "' ";
		where += " ) \r\n";
		String sql = " SELECT   \r\n"
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
				+ where;

		list = new ArrayList<>();
		List<Map<String, Object>> datas = this.database.queryList(sql);
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public PCMSSecondTableDetail updateSwitchProdOrderDetail(PCMSSecondTableDetail bean, String dataStatus)
	{
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		String prdOrder = bean.getProductionOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		try {
			String sql = " UPDATE [PCMS].[dbo].[SwitchProdOrder]"
					+ " 	SET [DataStatus] = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? "
					+ " declare  @rc int = @@ROWCOUNT "
					+ ";";
			prepared = connection.prepareStatement(sql);
			int index = 1;
			prepared.setString(index ++ , dataStatus);
			prepared.setString(index ++ , bean.getUserId());
			prepared.setTimestamp(index ++ , new Timestamp(time));
			prepared.setString(index ++ , prdOrder);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("upsertSwitchPrd" + e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// this.database.close();
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail upsertSwitchProdOrder(PCMSSecondTableDetail bean, String dataStatus)
	{
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String prdOrderSW = bean.getProductionOrderSW();
		String saleOrderSW = bean.getSaleOrderSW();
		String saleLineSW = String.format("%06d", Integer.parseInt(bean.getSaleLineSW()));
		String userID = bean.getUserId();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		try {
			String sql = " UPDATE [PCMS].[dbo].[SwitchProdOrder]"
					+ " 	SET [DataStatus] = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? AND"
					+ "       [ProductionOrderSW]  = ? and [SaleOrderSW] = ?  and [SaleLineSW] = ?  "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ " if @rc <> 0 "
					+ " 	print @rc "
					+ " else "
					+ " 	INSERT INTO [PCMS].[dbo].[SwitchProdOrder]"
					+ " 	([ProductionOrder]  ,[SaleOrder]   ,[SaleLine],"
					+ " 	 [ProductionOrderSW],[SaleOrderSW] ,[SaleLineSW],"
					+ "  	[ChangeBy] ,[ChangeDate] )"// 55
					+ " 	values(? , ? , ? , ? , ? "
					+ "    	     , ? , ? , ?  "
					+ "     )  "
					+ ";";
			prepared = connection.prepareStatement(sql);
			int index = 1;
			prepared.setString(index ++ , dataStatus);
			prepared.setString(index ++ , bean.getUserId());
			prepared.setTimestamp(index ++ , new Timestamp(time));
			prepared.setString(index ++ , prdOrder);
			prepared.setString(index ++ , saleOrder);
			prepared.setString(index ++ , saleLine);
			prepared.setString(index ++ , prdOrderSW);
			prepared.setString(index ++ , saleOrderSW);
			prepared.setString(index ++ , saleLineSW);

			prepared.setString(index ++ , prdOrder);
			prepared.setString(index ++ , saleOrder);
			prepared.setString(index ++ , saleLine);
			prepared.setString(index ++ , prdOrderSW);
			prepared.setString(index ++ , saleOrderSW);
			prepared.setString(index ++ , saleLineSW);
			prepared.setString(index ++ , userID);
			prepared.setTimestamp(index ++ , new Timestamp(time));
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
//			System.err.println("upsertSwitchPrd"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// this.database.close();
		}
		return bean;
	}
}
