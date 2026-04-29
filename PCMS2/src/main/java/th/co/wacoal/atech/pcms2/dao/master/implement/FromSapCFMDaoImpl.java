package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapCFMDao;
import th.co.wacoal.atech.pcms2.entities.CFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapCFMDaoImpl implements FromSapCFMDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSapCFMDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<CFMDetail> getFromSapCFMDetailByProductionOrder(String prodOrder)
	{
		ArrayList<CFMDetail> list = null;
		String where = " where  ";
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = " SELECT DISTINCT  \r\n"
				+ "   [Id]"
				+ "   ,[ProductionOrder],[CFMNo],[CFMNumber]\r\n"
				+ "   ,[CFMSendDate],[CFMAnswerDate],[CFMStatus]\r\n"
				+ "   ,[CFMRemark],[Da],[Db],[L]\r\n"
				+ "   ,[ST],[SaleOrder]"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "			WHEN 0 THEN ''  \r\n"
				+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "			END AS [SaleLine] "
				+ "   ,[CFMCheckLab]\r\n"
				+ "   ,[CFMNextLab],[CFMCheckLot],[CFMNextLot]\r\n"
				+ "   ,[NextLot],[SOChange],[SOChangeQty]\r\n"
				+ "   ,[SOChangeUnit],[RollNo],[RollNoRemark]\r\n"
				+ "   ,a.[DataStatus]\r\n"
				+ "   ,[DE]\r\n"
				+ " from [PCMS].[dbo].[FromSapCFM] as a \r\n "
				+ where
				+ " Order by [CFMNo]";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCFMDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapCFMDetail(ArrayList<FromErpCFMDetail> paList)
	{
		String iconStatus = "I";
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String systemUser = "SYSTEM"; // หรือดึงจาก session ถ้ามี

		Connection conn = this.database.getConnection();
		PreparedStatement prepared = null;

		try {
			conn.setAutoCommit(false);

			try (Statement stmt = conn.createStatement()) {
				// 1. สร้าง Temp Table ให้ล้อตาม Schema จริง
				stmt.execute("IF OBJECT_ID('tempdb..#TempCFM') IS NOT NULL DROP TABLE #TempCFM");
				stmt.execute("CREATE TABLE #TempCFM ("
						+ "ProductionOrder NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "CFMNo NVARCHAR(10) COLLATE DATABASE_DEFAULT, "
						+ // ปรับให้ใกล้เคียง Schema
						"CFMNumber NVARCHAR(20) COLLATE DATABASE_DEFAULT, "
						+ "CFMSendDate DATETIME, CFMAnswerDate DATETIME, "
						+ "CFMStatus NVARCHAR(10) COLLATE DATABASE_DEFAULT, "
						+ "CFMRemark NVARCHAR(200) COLLATE DATABASE_DEFAULT, "
						+ // ปรับความยาวตาม Schema
						"SaleOrder NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "SaleLine NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "NextLot NVARCHAR(15) COLLATE DATABASE_DEFAULT, "
						+ "SOChange NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "SOChangeQty DECIMAL(18,4), "
						+ "SOChangeUnit NVARCHAR(10) COLLATE DATABASE_DEFAULT, "
						+ "RollNo NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "RollNoRemark NVARCHAR(200) COLLATE DATABASE_DEFAULT, "
						+ "DataStatus NVARCHAR(10) COLLATE DATABASE_DEFAULT, "
						+ "SyncDate DATETIME)");

				// 2. Bulk Insert (เหมือนเดิม)
				String insertTemp = "INSERT INTO #TempCFM VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				try (PreparedStatement ps = conn.prepareStatement(insertTemp)) {
					for (FromErpCFMDetail bean : paList) {
						int idx = 1;
						ps.setString(idx ++ , bean.getProductionOrder());
						ps.setString(idx ++ , bean.getCfmNo());
						ps.setString(idx ++ , bean.getCfmNumber());
						this.sshUtl.setSqlDate(ps, bean.getCfmSendDate(), idx ++ );
						this.sshUtl.setSqlDate(ps, bean.getCfmAnswerDate(), idx ++ );
						ps.setString(idx ++ , bean.getCfmStatus());
						ps.setString(idx ++ , bean.getCfmRemark());
						ps.setString(idx ++ , bean.getSaleOrder());
						ps.setString(idx ++ , bean.getSaleLine());
						ps.setString(idx ++ , bean.getNextLot());
						ps.setString(idx ++ , bean.getSoChange());
						this.sshUtl.setSqlBigDecimal(ps, bean.getSoChangeQty(), idx ++ );
						ps.setString(idx ++ , bean.getSoChangeUnit());
						ps.setString(idx ++ , bean.getRollNo());
						ps.setString(idx ++ , bean.getRollNoRemark());
						ps.setString(idx ++ , bean.getDataStatus());
						this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx ++ );
						ps.addBatch();
					}
					ps.executeBatch();
				}

				// 3. จัดการ DataStatus = 'X'
				stmt.execute("UPDATE target SET target.DataStatus = 'X', target.ChangeDate = GETDATE(), target.ChangeBy = '"
						+ systemUser
						+ "' "
						+ "FROM [FromSapCFM] AS target INNER JOIN #TempCFM AS src ON target.ProductionOrder = src.ProductionOrder "
						+ "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'");

				// 4. Update ข้อมูลเดิม
				String updateNormal = "UPDATE target SET "
						+ "  target.CFMNumber = src.CFMNumber, target.CFMSendDate = src.CFMSendDate, "
						+ "  target.CFMAnswerDate = src.CFMAnswerDate, target.CFMStatus = src.CFMStatus, "
						+ "  target.CFMRemark = src.CFMRemark, target.SaleOrder = src.SaleOrder, "
						+ "  target.SaleLine = src.SaleLine, target.NextLot = src.NextLot, "
						+ "  target.SOChange = src.SOChange, target.SOChangeQty = src.SOChangeQty, "
						+ "  target.SOChangeUnit = src.SOChangeUnit, target.RollNo = src.RollNo, "
						+ "  target.RollNoRemark = src.RollNoRemark, target.DataStatus = src.DataStatus, "
						+ "  target.ChangeDate = GETDATE(), target.ChangeBy = '"
						+ systemUser
						+ "', target.SyncDate = src.SyncDate "
						+ "FROM [FromSapCFM] AS target "
						+ "INNER JOIN #TempCFM AS src ON target.ProductionOrder = src.ProductionOrder AND target.CFMNo = src.CFMNo "
						+ "WHERE src.DataStatus <> 'X'";
				stmt.execute(updateNormal);

				// 5. Insert ข้อมูลใหม่ (เพิ่มการระบุ ChangeBy, CreateBy)
				String insertNew = "INSERT INTO [FromSapCFM] (ProductionOrder, CFMNo, CFMNumber, CFMSendDate, CFMAnswerDate, "
						+ "CFMStatus, CFMRemark, SaleOrder, SaleLine, NextLot, SOChange, SOChangeQty, SOChangeUnit, "
						+ "RollNo, RollNoRemark, DataStatus, ChangeDate, ChangeBy, CreateDate, CreateBy, SyncDate) "
						+ "SELECT src.ProductionOrder, src.CFMNo, src.CFMNumber, src.CFMSendDate, src.CFMAnswerDate, "
						+ "src.CFMStatus, src.CFMRemark, src.SaleOrder, src.SaleLine, src.NextLot, src.SOChange, src.SOChangeQty, "
						+ "src.SOChangeUnit, src.RollNo, src.RollNoRemark, src.DataStatus, GETDATE(), '"
						+ systemUser
						+ "', GETDATE(), '"
						+ systemUser
						+ "', src.SyncDate "
						+ "FROM #TempCFM AS src "
						+ "LEFT JOIN [FromSapCFM] AS target ON target.ProductionOrder = src.ProductionOrder AND target.CFMNo = src.CFMNo "
						+ "WHERE target.ProductionOrder IS NULL "
						+ "  AND src.DataStatus <> 'X' "
						+ "  AND src.CFMNo IS NOT NULL AND src.CFMNo <> ''";
				stmt.execute(insertNew);

				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			finally {
			    // ✅ ปิด transaction เสมอ ไม่ว่าจะ success หรือ error
			    try {
			        conn.setAutoCommit(true);
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
			iconStatus = "E";
		}
		return iconStatus;
	}
//	@Override
//	public String upsertFromSapCFMDetail(ArrayList<FromErpCFMDetail> paList)
//	{
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//
//		String iconStatus = "I";
//		String sql = "-- Update if the record exists\r\n"
//				+ " "
//				+ "-- Update if the record exists\r\n"
//				+ "IF ? = 'X'\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapCFM]\r\n"
//				+ "    SET\r\n"
//				+ "        [DataStatus] = 'X',\r\n"
//				+ "        [ChangeDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ? \r\n"
//				+ "      and [DataStatus] = 'O' ;\r\n"
//				+ "END\r\n"
//				+ "ELSE\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapCFM]\r\n"
//				+ "    SET\r\n"
//				+ "        [CFMNumber] = ?,\r\n"
//				+ "        [CFMSendDate] = ?,\r\n"
//				+ "        [CFMAnswerDate] = ?,\r\n"
//				+ "        [CFMStatus] = ?,\r\n"
//				+ "        [CFMRemark] = ?,\r\n"
//				+ "        [SaleOrder] = ?,\r\n"
//				+ "        [SaleLine] = ?,\r\n"
//				+ "        [NextLot] = ?,\r\n"
//				+ "        [SOChange] = ?,\r\n"
//				+ "        [SOChangeQty] = ?,\r\n"
//				+ "        [SOChangeUnit] = ?,\r\n"
//				+ "        [RollNo] = ?,\r\n"
//				+ "        [RollNoRemark] = ?,\r\n"
//				+ "        [DataStatus] = ?,\r\n"
//				+ "        [ChangeDate] = ?,\r\n"
//				+ "        [SyncDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ?\r\n"
//				+ "        AND [CFMNo] = ?;\r\n"
//				+ "    \r\n"
//				+ "    -- Check if rows were updated\r\n"
//				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "    \r\n"
//				+ "    IF @rc = 0 AND ? <> '' AND ? <> ''\r\n"
//				+ "    BEGIN\r\n"
//				+ "        -- Insert if no rows were updated\r\n"
//				+ "        INSERT INTO [dbo].[FromSapCFM] (\r\n"
//				+ "            [ProductionOrder],\r\n"
//				+ "            [CFMNo],\r\n"
//				+ "            [CFMNumber],\r\n"
//				+ "            [CFMSendDate],\r\n"
//				+ "            [CFMAnswerDate],\r\n"
//				+ "            [CFMStatus],\r\n"
//				+ "            [CFMRemark],\r\n"
//				+ "            [SaleOrder],\r\n"
//				+ "            [SaleLine],\r\n"
//				+ "            [NextLot],\r\n"
//				+ "            [SOChange],\r\n"
//				+ "            [SOChangeQty],\r\n"
//				+ "            [SOChangeUnit],\r\n"
//				+ "            [RollNo],\r\n"
//				+ "            [RollNoRemark],\r\n"
//				+ "            [DataStatus],\r\n"
//				+ "            [ChangeDate],\r\n"
//				+ "            [CreateDate],\r\n"
//				+ "            [SyncDate]\r\n"
//				+ "        )\r\n"
//				+ "        VALUES (\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?,\r\n"
//				+ "            ?, ?, ?\r\n"
//				+ "        );\r\n"
//				+ "    END\r\n"
//				+ "END\r\n";
//
//		int index = 1;
//		int batchSize = 0;
//
//		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
//Connection connection = this.database.getConnection();
//PreparedStatement prepared = null;
//
//try {
//    prepared = connection.prepareStatement(sql);
//			for (FromErpCFMDetail bean : paList) {
//				index = 1;
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getProductionOrder());
//
//				prepared.setString(index ++ , bean.getCfmNumber());
//				this.sshUtl.setSqlDate(prepared, bean.getCfmSendDate(), index ++ );
//				this.sshUtl.setSqlDate(prepared, bean.getCfmAnswerDate(), index ++ );
//				prepared.setString(index ++ , bean.getCfmStatus());
//				prepared.setString(index ++ , bean.getCfmRemark());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				prepared.setString(index ++ , bean.getNextLot());
//				prepared.setString(index ++ , bean.getSoChange());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getSoChangeQty(), index ++ );
//				prepared.setString(index ++ , bean.getSoChangeUnit());
//				prepared.setString(index ++ , bean.getRollNo());
//				prepared.setString(index ++ , bean.getRollNoRemark());
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getCfmNo());
//
//				prepared.setString(index ++ , bean.getCfmNo());// CHECK CFM NO <> ''
//				prepared.setString(index ++ , bean.getCfmNumber());// CHECK CFM NUMBER <> ''
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getCfmNo());
//				prepared.setString(index ++ , bean.getCfmNumber());
//				this.sshUtl.setSqlDate(prepared, bean.getCfmSendDate(), index ++ );
//				this.sshUtl.setSqlDate(prepared, bean.getCfmAnswerDate(), index ++ );
//
//				prepared.setString(index ++ , bean.getCfmStatus());
//				prepared.setString(index ++ , bean.getCfmRemark());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				prepared.setString(index ++ , bean.getNextLot());
//
//				prepared.setString(index ++ , bean.getSoChange());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getSoChangeQty(), index ++ );
//				prepared.setString(index ++ , bean.getSoChangeUnit());
//				prepared.setString(index ++ , bean.getRollNo());
//				prepared.setString(index ++ , bean.getRollNoRemark());
//
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.addBatch();
//				batchSize ++ ;
//				if (batchSize % 500 == 0) { // Execute batch every 500 records
//					prepared.executeBatch();
//					prepared.clearBatch();
//					batchSize = 0; // Reset batch size
//				}
////				prepa
//			}
//			prepared.executeBatch();
//			prepared.close();
//		} catch (SQLException e) {
////			e.printStackTrace();
//			e.printStackTrace();
//			iconStatus = "E";
//		} finally {
//			// this.database.close();
//		}
//		return iconStatus;
//	}

}
