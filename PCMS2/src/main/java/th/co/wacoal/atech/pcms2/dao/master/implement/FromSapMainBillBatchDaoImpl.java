package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainBillBatchDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainBillBatchDetail;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
//import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapMainBillBatchDaoImpl implements FromSapMainBillBatchDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
//	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSapMainBillBatchDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String upsertFromSapMainBillBatchDetail(ArrayList<FromErpMainBillBatchDetail> paList)
	{
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String iconStatus = "I";
		String sql = " "
				+ "-- Update if the record exists\r\n"
				+ "IF ? = 'X'\r\n"
				+ "BEGIN\r\n"
				+ "    UPDATE [dbo].[FromSapMainBillBatch]\r\n"
				+ "    SET\r\n"
				+ "        [DataStatus] = 'X',\r\n"
				+ "        [ChangeDate] = ?\r\n"
				+ "    WHERE\r\n"
				+ "        [ProductionOrder] = ? \r\n"
				+ "      and [DataStatus] = 'O' ;\r\n"
				+ "END\r\n"
				+ "ELSE\r\n"
				+ "BEGIN\r\n"
				+ "    UPDATE [dbo].[FromSapMainBillBatch]\r\n"
				+ "    SET\r\n"
				+ "        [LotShipping] = ?,\r\n"
				+ "        [Grade] = ?,\r\n"
				+ "        [QuantityKG] = ?,\r\n"
				+ "        [QuantityYD] = ?,\r\n"
				+ "        [QuantityMR] = ?,\r\n"
				+ "        [LotNo] = ?,\r\n"
				+ "        [DataStatus] = ?,\r\n"
				+ "        [ChangeDate] = ?,\r\n"
				+ "        [SyncDate] = ?\r\n"
				+ "    WHERE\r\n"
				+ "        [BillDoc] = ?\r\n"
				+ "        AND [BillItem] = ?\r\n"
				+ "        AND [SaleOrder] = ?\r\n"
				+ "        AND [SaleLine] = ?\r\n"
				+ "        AND [RollNumber] = ?\r\n"
				+ "        AND [ProductionOrder] = ?;\r\n"
				+ "\r\n"
				+ "    -- Check if rows were updated\r\n"
				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "\r\n"
				+ "    IF @rc = 0  AND ? <> ''\r\n"
				+ "    BEGIN\r\n"
				+ "        -- Insert if no rows were updated\r\n"
				+ "        INSERT INTO [dbo].[FromSapMainBillBatch] (\r\n"
				+ "            [BillDoc],\r\n"
				+ "            [BillItem],\r\n"
				+ "            [LotShipping],\r\n"
				+ "            [ProductionOrder],\r\n"
				+ "            [SaleOrder],\r\n"
				+ "            [SaleLine],\r\n"
				+ "            [Grade],\r\n"
				+ "            [RollNumber],\r\n"
				+ "            [QuantityKG],\r\n"
				+ "            [QuantityYD],\r\n"
				+ "            [QuantityMR],\r\n"
				+ "            [LotNo],\r\n"
				+ "            [DataStatus],\r\n"
				+ "            [ChangeDate],\r\n"
				+ "            [CreateDate],\r\n"
				+ "            [SyncDate]\r\n"
				+ "        )\r\n"
				+ "        VALUES (\r\n"
				+ "            ?, ?, ?, ?, ?,\r\n"
				+ "            ?, ?, ?, ?, ?,\r\n"
				+ "            ?, ?, ?, ?, ?,\r\n"
				+ "            ?\r\n"
				+ "        );\r\n"
				+ "    END\r\n"
				+ "END";

		int index = 1;
		int batchSize = 0;

		try (Connection connection = database.getConnection(); PreparedStatement prepared = connection.prepareStatement(sql)) {
			for (FromErpMainBillBatchDetail bean : paList) {
				index = 1;
				prepared.setString(index ++ , bean.getDataStatus());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setString(index ++ , bean.getProductionOrder());
				this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
				prepared.setString(index ++ , bean.getGrade());
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );

				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ );
				prepared.setString(index ++ , bean.getLotNo());
				prepared.setString(index ++ , bean.getDataStatus());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );

				prepared.setString(index ++ , bean.getBillDoc());
				prepared.setString(index ++ , bean.getBillItem());
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared.setString(index ++ , bean.getRollNumber());
				prepared.setString(index ++ , bean.getProductionOrder());

				prepared.setString(index ++ , bean.getBillDoc());// CHECK BILL NUMBER <> ''

				prepared.setString(index ++ , bean.getBillDoc());
				prepared.setString(index ++ , bean.getBillItem());
				this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getSaleOrder());

				prepared.setString(index ++ , bean.getSaleLine());
				prepared.setString(index ++ , bean.getGrade());
				prepared.setString(index ++ , bean.getRollNumber());
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );

				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ );
				prepared.setString(index ++ , bean.getLotNo());
				prepared.setString(index ++ , bean.getDataStatus());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setTimestamp(index ++ , new Timestamp(time));

				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.addBatch();
				batchSize ++ ;
				if (batchSize % 500 == 0) { // Execute batch every 500 records
					prepared.executeBatch();
					prepared.clearBatch();
					batchSize = 0; // Reset batch size
				}
			}
//			System.out.println("here1");
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
