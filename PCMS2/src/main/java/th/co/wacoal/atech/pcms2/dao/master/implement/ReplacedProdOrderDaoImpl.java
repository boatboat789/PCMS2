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

import th.co.wacoal.atech.pcms2.dao.master.ReplacedProdOrderDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.ReplacedProdOrderDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class ReplacedProdOrderDaoImpl implements  ReplacedProdOrderDao{
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
	public ReplacedProdOrderDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public  ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdRP(String prodOrder) {
		ArrayList<ReplacedProdOrderDetail> list = null;
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n"
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderRP] and DataStatus = 'O' and \r\n"
				+ "       [ProductionOrderRP] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genReplacedProdOrderDetail(map)); }
		return list;
	}
	@Override
	public  ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrd(String prodOrder) {
		ArrayList<ReplacedProdOrderDetail> list = null;
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n"
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where Productionorder <> [ProductionOrderRP] and \r\n"
				+ "        DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genReplacedProdOrderDetail(map)); }
		return list;
	} 
	@Override
	public  ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMainAndSO(String prodOrder, String saleOrder, String saleLine) {
		ArrayList<ReplacedProdOrderDetail> list = null;
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n"
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"+prodOrder+"' and\r\n"
				+ "       [SaleOrder] = '"+saleOrder+"' and\r\n" 
				+ "       [SaleLine] = '"+saleLine+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genReplacedProdOrderDetail(map)); }
		return list;
	}
	@Override
	public  ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMain(String prodOrder) {
		ArrayList<ReplacedProdOrderDetail> list = null;
		String sql = "SELECT \r\n"
				+ "		  [SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[ProductionOrderRP]\r\n"
				+ "      ,[Volume]\r\n"
				+ "  FROM [PCMS].[dbo].[ReplacedProdOrder]\r\n"
				+ "  where DataStatus = 'O' and \r\n"
				+ "       [ProductionOrder] = '"+prodOrder+"' \r\n"
				+ "  ORDER BY productionorder \r\n"
				+ " ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) { list.add(this.bcModel._genReplacedProdOrderDetail(map)); }
		return list;
	}

	@Override
	public ReplacedProdOrderDetail upsertReplacedProdOrder( ReplacedProdOrderDetail bean ,String dataStatus) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String prdOrderRP = bean.getProductionOrderRP();
		String volume = bean.getVolume();
		String userID = bean.getChangeBy();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String sql =
				  " UPDATE [PCMS].[dbo].[ReplacedProdOrder] "
				+ " SET [Volume] = ? , [DataStatus] = ? ,[ChangeBy] = ?,[ChangeDate]  = ? "
				+ " WHERE [ProductionOrder] = ? and [SaleOrder] = ? and [SaleLine] = ? and "
				+ "       [ProductionOrderRP] = ? "
				+ " declare  @rc int = @@ROWCOUNT " // 56
				+ "  if @rc <> 0 "
				+ " print @rc "
				+ " else "
				+ " INSERT INTO [PCMS].[dbo].[ReplacedProdOrder]"
				+ "  ([ProductionOrder] ,[SaleOrder] ,[SaleLine], [ProductionOrderRP], [Volume], "
				+ "   [ChangeBy] ,[ChangeDate] )"//55
				+ " values(? , ? , ? , ? , ? "
				+ "      , ? , ? "
				+ "      )  "
				+ ";"  ;
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
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println(e);
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}finally {
			//this.database.close();
		}
		return bean;
	}
	@Override
	public  PCMSSecondTableDetail updateReplacedProdOrder( PCMSSecondTableDetail bean ,String dataStatus) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String caseSave = bean.getCaseSave();
		try {
			String sql =
					  " UPDATE [PCMS].[dbo].[ReplacedProdOrder]"
					+ " 	SET [DataStatus] = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " 	WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ?  "
					+ " declare  @rc int = @@ROWCOUNT "
					+ ";"  ;
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
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("ReplacedProdOrder"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}finally {
			//this.database.close();
		}
		return bean;
	}

}
