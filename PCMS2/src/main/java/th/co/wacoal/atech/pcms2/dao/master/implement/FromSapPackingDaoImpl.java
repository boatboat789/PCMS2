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
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapPackingDao;
import th.co.wacoal.atech.pcms2.entities.PackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapPackingDaoImpl implements FromSapPackingDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private Database database;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");
	private String selectPacking = "      fsp.[Id]\r\n"
			+ "      ,[ProductionOrder]\r\n"
			+ "      ,[PostingDate]\r\n"
			+ "      ,[Quantity]\r\n"
			+ "      ,[RollNo]\r\n"
			+ "      ,[Status]\r\n"
			+ "	  ,insorder.[RollupNote] as [Status]\r\n"
			+ "      ,[QuantityKG]\r\n"
			+ "      ,[Grade]\r\n"
			+ "      ,[No]\r\n"
			+ "      ,[DataStatus]\r\n"
			+ "      ,[QuantityYD]\r\n"
			+ "      ,fsp.[ChangeDate]\r\n"
			+ "      ,[CreateDate] \r\n ";;

	@Autowired
	public FromSapPackingDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database; 
	}

	@Override
	public ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder)
	{
		ArrayList<PackingDetail> list = null;
		String where = " where  ";
		where += " " + " fsp.ProductionOrder = '" + prodOrder + "'  and \r\n" + " fsp.[DataStatus] = 'O' \r\n";
		String sql = ""
				+ " SELECT DISTINCT  \r\n"
				+ this.selectPacking
				+ "  from [PCMS].[dbo].[FromSapPacking] as fsp\r\n"
				+ "  left join [InspectSystem].[dbo].[InspectOrders] as insorder on fsp.[ProductionOrder] = insorder.[PrdNumber] \r\n "
				+ where;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPackingDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapPackingDetail(ArrayList<FromErpPackingDetail> paList)
	{
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();

		String iconStatus = "I";
		String sql = " "
				+ " "
				+ "-- Update if the record exists\r\n"
				+ "IF ? = 'X'\r\n"
				+ "BEGIN\r\n"
				+ "    UPDATE [dbo].[FromSapPacking]\r\n"
				+ "    SET\r\n"
				+ "        [DataStatus] = 'X',\r\n"
				+ "        [ChangeDate] = ?\r\n"
				+ "    WHERE\r\n"
				+ "        [ProductionOrder] = ?\r\n"
				+ "        AND [DataStatus] = 'O';\r\n"
				+ "END\r\n"
				+ "ELSE\r\n"
				+ "BEGIN\r\n"
				+ "    UPDATE [dbo].[FromSapPacking]\r\n"
				+ "    SET\r\n"
				+ "        [PostingDate] = ?,\r\n"
				+ "        [Quantity] = ?,\r\n"
				+ "        [QuantityKG] = ?,\r\n"
				+ "        [Grade] = ?,\r\n"
				+ "        [No] = ?,\r\n"
				+ "        [QuantityYD] = ?,\r\n"
				+ "        [ChangeDate] = ?,\r\n"
				+ "        [SyncDate] = ?,\r\n"
				+ "        [DataStatus] = ?\r\n"
				+ "    WHERE\r\n"
				+ "        [ProductionOrder] = ?\r\n"
				+ "        AND [RollNo] = ?;\r\n"
				+ "\r\n"
				+ "    -- Check if rows were updated\r\n"
				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "\r\n"
				+ "    IF @rc = 0\r\n"
				+ "    BEGIN\r\n"
				+ "        -- Insert if no rows were updated\r\n"
				+ "        INSERT INTO [dbo].[FromSapPacking] (\r\n"
				+ "            [ProductionOrder],\r\n"
				+ "            [PostingDate],\r\n"
				+ "            [Quantity],\r\n"
				+ "            [RollNo],\r\n"
				+ "            [QuantityKG],\r\n"
				+ "            [Grade],\r\n"
				+ "            [No],\r\n"
				+ "            [QuantityYD],\r\n"
				+ "            [ChangeDate],\r\n"
				+ "            [CreateDate],\r\n"
				+ "            [SyncDate],\r\n"
				+ "            [DataStatus]\r\n"
				+ "        )\r\n"
				+ "        VALUES (\r\n"
				+ "            ?, ?, ?, ?,\r\n"
				+ "            ?, ?, ?, ?, ?,\r\n"
				+ "            ?, ?, ?\r\n"
				+ "        );\r\n"
				+ "    END\r\n"
				+ "END";

		int index = 1;

		try (Connection connection = database.getConnection(); PreparedStatement prepared = connection.prepareStatement(sql)) {
			for (FromErpPackingDetail bean : paList) {
				index = 1;

				prepared.setString(index ++ , bean.getDataStatus());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setString(index ++ , bean.getProductionOrder());

				this.sshUtl.setSqlDate(prepared, bean.getPostingDate(), index ++ );
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantity(), index ++ );
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				prepared.setString(index ++ , bean.getGrade());
				prepared.setString(index ++ , bean.getNo());
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
				prepared.setTimestamp(index ++ , new Timestamp(time));
				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.setString(index ++ , bean.getDataStatus());

				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getRollNo());

				prepared.setString(index ++ , bean.getProductionOrder());
				this.sshUtl.setSqlDate(prepared, bean.getPostingDate(), index ++ );
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantity(), index ++ );
				prepared.setString(index ++ , bean.getRollNo());
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				prepared.setString(index ++ , bean.getGrade());
				prepared.setString(index ++ , bean.getNo());
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setTimestamp(index ++ , new Timestamp(time));
				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.setString(index ++ , bean.getDataStatus());
				prepared.addBatch();
			}
			prepared.executeBatch();
			prepared.close();
		} catch (SQLException e) {
//			e.printStackTrace();
			e.printStackTrace();
			iconStatus = "E";
		} finally {
			// this.database.close();
		}
		return iconStatus;
	}
}
