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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.ReplacedProdOrderDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.ReplacedProdOrderDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class ReplacedProdOrderDaoImpl implements ReplacedProdOrderDao {
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
	public ReplacedProdOrderDaoImpl(@Qualifier("pcmsDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdRP(String prodOrder)
	{
		ArrayList<ReplacedProdOrderDetail> list = null;
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n"
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderRP] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrderRP] = '"
				+ prodOrderSafe
				+ "' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genReplacedProdOrderDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrd(String prodOrder)
	{
		ArrayList<ReplacedProdOrderDetail> list = null;
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n"
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderRP] and \r\n"
				+ "        DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"
				+ prodOrderSafe
				+ "' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genReplacedProdOrderDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMainAndSO(String prodOrder, String saleOrder,
			String saleLine)
	{
		ArrayList<ReplacedProdOrderDetail> list = null;
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		String saleOrderSafe = (saleOrder == null ? "" : saleOrder.replace("'", "''"));
		String saleLineSafe = (saleLine == null ? "" : saleLine.replace("'", "''"));
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n"
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"
				+ prodOrderSafe
				+ "' and\r\n"
				+ "       [SaleOrder] = '"
				+ saleOrderSafe
				+ "' and\r\n"
				+ "       [SaleLine] = '"
				+ saleLineSafe
				+ "' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genReplacedProdOrderDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMain(String prodOrder)
	{
		ArrayList<ReplacedProdOrderDetail> list = null;
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n"
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"
				+ prodOrderSafe
				+ "' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genReplacedProdOrderDetail(map));
		}
		return list;
	}

	@Override
	public ReplacedProdOrderDetail upsertReplacedProdOrder(ReplacedProdOrderDetail bean, String dataStatus)
	{

		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String saleLine = bean.getSaleLine();
		String prdOrderRP = bean.getProductionOrderRP();
		String volume = bean.getVolume();
		String userID = bean.getChangeBy();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String sql = " UPDATE [PCMS].[dbo].[ReplacedProdOrder] "
				+ " SET [Volume] = ? , [DataStatus] = ? ,[ChangeBy] = ?,[ChangeDate]  = ? "
				+ " WHERE [ProductionOrder] = ? and [SaleOrder] = ? and [SaleLine] = ? and "
				+ "       [ProductionOrderRP] = ? "
				+ " declare  @rc int = @@ROWCOUNT " // 56
				+ "  if @rc <> 0 "
				+ " print @rc "
				+ " else "
				+ " INSERT INTO [PCMS].[dbo].[ReplacedProdOrder]"
				+ "  ([ProductionOrder] ,[SaleOrder] ,[SaleLine], [ProductionOrderRP], [Volume], "
				+ "   [ChangeBy] ,[ChangeDate] )"// 55
				+ " values(? , ? , ? , ? , ? "
				+ "      , ? , ? "
				+ "      )  "
				+ ";";

		Connection connection = DataSourceUtils.getConnection(this.jdbc.getDataSource());
		PreparedStatement prepared = null;

		try {
			prepared = connection.prepareStatement(sql);

			prepared.setDouble(1, Double.parseDouble(volume));
			prepared.setString(2, dataStatus);
			prepared.setString(3, userID);
			prepared.setTimestamp(4, new Timestamp(time));
			prepared.setString(5, prdOrder);
			prepared.setString(6, saleOrder);
			prepared.setString(7, saleLine);
			prepared.setString(8, prdOrderRP);

			prepared.setString(9, prdOrder);
			prepared.setString(10, saleOrder);
			prepared.setString(11, saleLine);
			prepared.setString(12, prdOrderRP);
			prepared.setString(13, volume);
			prepared.setString(14, userID);
			prepared.setTimestamp(15, new Timestamp(time));
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("อัพเดตข้อมูลสำเร็จ");
		} catch (SQLException e) {
			e.printStackTrace();
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null)
				try {
					prepared.close();
				} catch (Exception e) {
				}
			DataSourceUtils.releaseConnection(connection, this.jdbc.getDataSource());
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail updateReplacedProdOrder(PCMSSecondTableDetail bean, String dataStatus)
	{

		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String saleLine = bean.getSaleLine();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String caseSave = bean.getCaseSave();
		String sql = " UPDATE [PCMS].[dbo].[ReplacedProdOrder]"
				+ " 	SET [DataStatus] = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
				+ " 	WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ?  "
				+ " declare  @rc int = @@ROWCOUNT "
				+ ";";

		Connection connection = DataSourceUtils.getConnection(this.jdbc.getDataSource());
		PreparedStatement prepared = null;

		try {
			prepared = connection.prepareStatement(sql);
			prepared.setString(1, dataStatus);
			prepared.setString(2, bean.getUserId());
			prepared.setTimestamp(3, new Timestamp(time));
			prepared.setString(4, prdOrder);
			prepared.setString(5, saleOrder);
			prepared.setString(6, saleLine);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("อัพเดตข้อมูลสำเร็จ");
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println("ReplacedProdOrder"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null)
				try {
					prepared.close();
				} catch (Exception e) {
				}
			DataSourceUtils.releaseConnection(connection, this.jdbc.getDataSource());
		}
		return bean;
	}

}
