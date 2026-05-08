package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapGoodReceiveDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
//import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapGoodReceiveDaoImpl implements FromSapGoodReceiveDao {
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
	public FromSapGoodReceiveDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}
	@Override
	public String upsertFromSapGoodReceiveDetail(ArrayList<FromErpGoodReceiveDetail> paList) {
	    String iconStatus = "I";
	    // ใช้เวลาปัจจุบันจากระบบ
//	    Timestamp now = new Timestamp(System.currentTimeMillis());

		Connection conn = this.database.getConnection();
		PreparedStatement prepared = null;

		try {
	        conn.setAutoCommit(false); 

	        try (Statement stmt = conn.createStatement()) {
	        	stmt.execute("IF OBJECT_ID('tempdb..#TempGR') IS NOT NULL DROP TABLE #TempGR");
	            // 1. ปรับ Temp Table ให้ Data Type ตรงกับ Schema จริง (13, 3) และความยาวตัวอักษร
	            stmt.execute("CREATE TABLE #TempGR ("
	                    + "ProductionOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, "
	                    + "SaleOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, "
	                    + "SaleLine VARCHAR(50) COLLATE DATABASE_DEFAULT, "
	                    + "Grade VARCHAR(20) COLLATE DATABASE_DEFAULT, "
	                    + "RollNumber VARCHAR(20) COLLATE DATABASE_DEFAULT, "
	                    + "QuantityKG DECIMAL(13, 3), "
	                    + "QuantityYD DECIMAL(13, 3), "
	                    + "QuantityMR DECIMAL(13, 3), "
	                    + "PriceSTD DECIMAL(13, 3), "
	                    + "DataStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, " // Schema เป็น varchar(1)
	                    + "SyncDate DATETIME)");

	            // 2. Bulk Insert ลง Temp Table (เหมือนเดิม)
	            String insertTemp = "INSERT INTO #TempGR VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	            try (PreparedStatement ps = conn.prepareStatement(insertTemp)) {
	                for (FromErpGoodReceiveDetail bean : paList) {
	                    int idx = 1;
	                    ps.setString(idx++, bean.getProductionOrder());
	                    ps.setString(idx++, bean.getSaleOrder());
	                    ps.setString(idx++, bean.getSaleLine());
	                    ps.setString(idx++, bean.getGrade());
	                    ps.setString(idx++, bean.getRollNumber());
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityKG(), idx++);
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityYD(), idx++);
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityMR(), idx++);
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getPriceSTD(), idx++);
	                    ps.setString(idx++, bean.getDataStatus());
	                    this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx++);
	                    ps.addBatch();
	                }
	                ps.executeBatch();
	            }

	            // 3. Update สถานะ X (ปิดรายการเก่า)
	            // ใช้ GETDATE() เพื่อความแม่นยำของ DB Server
//	            stmt.execute("UPDATE target SET target.DataStatus = 'X', target.ChangeDate = GETDATE() "
//	                    + "FROM [FromSapGoodReceive] AS target "
//	                    + "INNER JOIN #TempGR AS src ON target.ProductionOrder = src.ProductionOrder "
//	                    + "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'");
//
//	            // 4. Update ข้อมูลม้วนเดิม (Logic: PO + RollNumber)
//	            stmt.execute("UPDATE target SET "
//	                    + "  target.SaleOrder = src.SaleOrder, target.SaleLine = src.SaleLine, "
//	                    + "  target.Grade = src.Grade, target.QuantityKG = src.QuantityKG, "
//	                    + "  target.QuantityYD = src.QuantityYD, target.QuantityMR = src.QuantityMR, "
//	                    + "  target.PriceSTD = src.PriceSTD, target.DataStatus = src.DataStatus, "
//	                    + "  target.ChangeDate = GETDATE(), target.SyncDate = src.SyncDate "
//	                    + "FROM [FromSapGoodReceive] AS target "
//	                    + "INNER JOIN #TempGR AS src ON target.ProductionOrder = src.ProductionOrder AND target.RollNumber = src.RollNumber "
//	                    + "WHERE src.DataStatus <> 'X'");
//
//	            // 5. Insert ข้อมูลม้วนใหม่
//	            stmt.execute("INSERT INTO [FromSapGoodReceive] (ProductionOrder, SaleOrder, SaleLine, Grade, RollNumber, "
//	                    + "QuantityKG, QuantityYD, QuantityMR, PriceSTD, DataStatus, ChangeDate, CreateDate, SyncDate) "
//	                    + "SELECT src.ProductionOrder, src.SaleOrder, src.SaleLine, src.Grade, src.RollNumber, "
//	                    + "src.QuantityKG, src.QuantityYD, src.QuantityMR, src.PriceSTD, src.DataStatus, GETDATE(), GETDATE(), src.SyncDate "
//	                    + "FROM #TempGR AS src "
//	                    + "LEFT JOIN [FromSapGoodReceive] AS target ON target.ProductionOrder = src.ProductionOrder AND target.RollNumber = src.RollNumber "
//	                    + "WHERE target.ProductionOrder IS NULL "
//	                    + "  AND src.DataStatus <> 'X' "
//	                    + "  AND src.RollNumber IS NOT NULL AND src.RollNumber <> ''");
	         // รวมข้อ 3, 4, 5 เป็น Batch เดียวเพื่อประสิทธิภาพและเวลาที่แม่นยำ
	            String upsertSql = 
	                  "DECLARE @Now DATETIME = GETDATE(); "
	                
	                + "/* 3. จัดการ DataStatus = 'X' */ "
	                + "UPDATE target SET "
	                + "    target.DataStatus = 'X', "
	                + "    target.ChangeDate = @Now "
	                + "FROM [FromSapGoodReceive] AS target "
	                + "INNER JOIN #TempGR AS src ON target.ProductionOrder = src.ProductionOrder "
	                + "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'; "

	                + "/* 4. Update ข้อมูลม้วนเดิม (Logic: PO + RollNumber) */ "
	                + "UPDATE target SET "
	                + "    target.SaleOrder = src.SaleOrder, "
	                + "    target.SaleLine = src.SaleLine, "
	                + "    target.Grade = src.Grade, "
	                + "    target.QuantityKG = src.QuantityKG, "
	                + "    target.QuantityYD = src.QuantityYD, "
	                + "    target.QuantityMR = src.QuantityMR, "
	                + "    target.PriceSTD = src.PriceSTD, "
	                + "    target.DataStatus = src.DataStatus, "
	                + "    target.ChangeDate = @Now, "
	                + "    target.SyncDate = src.SyncDate "
	                + "FROM [FromSapGoodReceive] AS target "
	                + "INNER JOIN #TempGR AS src ON target.ProductionOrder = src.ProductionOrder AND target.RollNumber = src.RollNumber "
	                + "WHERE src.DataStatus <> 'X'; "

	                + "/* 5. Insert ข้อมูลม้วนใหม่ */ "
	                + "INSERT INTO [FromSapGoodReceive] ( "
	                + "    ProductionOrder, SaleOrder, SaleLine, Grade, RollNumber, "
	                + "    QuantityKG, QuantityYD, QuantityMR, PriceSTD, DataStatus, "
	                + "    ChangeDate, CreateDate, SyncDate) "
	                + "SELECT "
	                + "    src.ProductionOrder, src.SaleOrder, src.SaleLine, src.Grade, src.RollNumber, "
	                + "    src.QuantityKG, src.QuantityYD, src.QuantityMR, src.PriceSTD, src.DataStatus, "
	                + "    @Now, @Now, src.SyncDate "
	                + "FROM #TempGR AS src "
	                + "LEFT JOIN [FromSapGoodReceive] AS target ON target.ProductionOrder = src.ProductionOrder AND target.RollNumber = src.RollNumber "
	                + "WHERE target.ProductionOrder IS NULL "
	                + "  AND src.DataStatus <> 'X' "
	                + "  AND src.RollNumber IS NOT NULL AND src.RollNumber <> '';";

	            stmt.execute(upsertSql);
	            conn.commit(); 
	        } catch (Exception e) {
	            conn.rollback(); 
	            throw e;
	        }finally {
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
//	public String upsertFromSapGoodReceiveDetail(ArrayList<FromErpGoodReceiveDetail> paList)
//	{
//		String iconStatus = "I";
//		Timestamp now = new Timestamp(System.currentTimeMillis());
//
//		try (Connection conn = database.getConnection()) {
//			conn.setAutoCommit(false); // เริ่ม Transaction เพื่อความเร็วและความปลอดภัย
//
//			try (Statement stmt = conn.createStatement()) {
//				// 1. สร้าง Temp Table (ระบุ COLLATE DATABASE_DEFAULT เพื่อป้องกันปัญหาเดิม)
//				stmt.execute("CREATE TABLE #TempGR ("
//						+ "ProductionOrder NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
//						+ "SaleOrder NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
//						+ "SaleLine NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
//						+ "Grade NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
//						+ "RollNumber NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
//						+ "QuantityKG DECIMAL(18,4), QuantityYD DECIMAL(18,4), "
//						+ "QuantityMR DECIMAL(18,4), PriceSTD DECIMAL(18,4), "
//						+ "DataStatus NVARCHAR(10) COLLATE DATABASE_DEFAULT, "
//						+ "SyncDate DATETIME)");
//
//				// 2. Bulk Insert ลง Temp Table
//				String insertTemp = "INSERT INTO #TempGR VALUES (?,?,?,?,?,?,?,?,?,?,?)";
//				try (PreparedStatement ps = conn.prepareStatement(insertTemp)) {
//					for (FromErpGoodReceiveDetail bean : paList) {
//						int idx = 1;
//						ps.setString(idx ++ , bean.getProductionOrder());
//						ps.setString(idx ++ , bean.getSaleOrder());
//						ps.setString(idx ++ , bean.getSaleLine());
//						ps.setString(idx ++ , bean.getGrade());
//						ps.setString(idx ++ , bean.getRollNumber());
//						this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityKG(), idx ++ );
//						this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityYD(), idx ++ );
//						this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityMR(), idx ++ );
//						this.sshUtl.setSqlBigDecimal(ps, bean.getPriceSTD(), idx ++ );
//						ps.setString(idx ++ , bean.getDataStatus());
//						this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx ++ );
//						ps.addBatch();
//					}
//					ps.executeBatch();
//				}
//
//				// 3. ปรับปรุงสถานะ DataStatus = 'X' (สั่งปิดรายการเดิม)
//				String updateStatusX = "UPDATE target SET target.DataStatus = 'X', target.ChangeDate = GETDATE() "
//						+ "FROM [FromSapGoodReceive] AS target "
//						+ "INNER JOIN #TempGR AS src ON target.ProductionOrder = src.ProductionOrder "
//						+ "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'";
//				stmt.execute(updateStatusX);
//
//				// 4. Update ข้อมูลม้วนเดิม (Matching ProductionOrder + RollNumber)
//				String updateNormal = "UPDATE target SET "
//						+ "  target.SaleOrder = src.SaleOrder, target.SaleLine = src.SaleLine, "
//						+ "  target.Grade = src.Grade, target.QuantityKG = src.QuantityKG, "
//						+ "  target.QuantityYD = src.QuantityYD, target.QuantityMR = src.QuantityMR, "
//						+ "  target.PriceSTD = src.PriceSTD, target.DataStatus = src.DataStatus, "
//						+ "  target.ChangeDate = GETDATE(), target.SyncDate = src.SyncDate "
//						+ "FROM [FromSapGoodReceive] AS target "
//						+ "INNER JOIN #TempGR AS src ON target.ProductionOrder = src.ProductionOrder AND target.RollNumber = src.RollNumber "
//						+ "WHERE src.DataStatus <> 'X'";
//				stmt.execute(updateNormal);
//
//				// 5. Insert ข้อมูลม้วนใหม่
//				String insertNew = "INSERT INTO [FromSapGoodReceive] (ProductionOrder, SaleOrder, SaleLine, Grade, RollNumber, "
//						+ "QuantityKG, QuantityYD, QuantityMR, PriceSTD, DataStatus, ChangeDate, CreateDate, SyncDate) "
//						+ "SELECT src.ProductionOrder, src.SaleOrder, src.SaleLine, src.Grade, src.RollNumber, "
//						+ "src.QuantityKG, src.QuantityYD, src.QuantityMR, src.PriceSTD, src.DataStatus, GETDATE(), GETDATE(), src.SyncDate "
//						+ "FROM #TempGR AS src "
//						+ "LEFT JOIN [FromSapGoodReceive] AS target ON target.ProductionOrder = src.ProductionOrder AND target.RollNumber = src.RollNumber "
//						+ "WHERE target.ProductionOrder IS NULL "
//						+ "  AND src.DataStatus <> 'X' "
//						+ "  AND src.RollNumber IS NOT NULL AND src.RollNumber <> ''";
//				stmt.execute(insertNew);
//
//				conn.commit(); // ยืนยันข้อมูลทั้งหมด
//			} catch (Exception e) {
//				conn.rollback(); // ถ้าพังให้คืนค่าเดิม
//				throw e;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			iconStatus = "E";
//		}
//		return iconStatus;
//	}
//	@Override
//	public String upsertFromSapGoodReceiveDetail(ArrayList<FromErpGoodReceiveDetail> paList)
//	{
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//
//		String iconStatus = "I";
//		String sql = "-- Update if the record exists\r\n"
//				+ "-- Update if the record exists\r\n"
//				+ "IF ? = 'X'\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapGoodReceive]\r\n"
//				+ "    SET\r\n"
//				+ "        [DataStatus] = 'X',\r\n"
//				+ "        [ChangeDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ? \r\n"
//				+ "      and [DataStatus] = 'O' ;\r\n"
//				+ "END\r\n"
//				+ "ELSE\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapGoodReceive]\r\n"
//				+ "    SET\r\n"
//				+ "        [SaleOrder] = ?,\r\n"
//				+ "        [SaleLine] = ?,\r\n"
//				+ "        [Grade] = ?,\r\n"
//				+ "        [QuantityKG] = ?,\r\n"
//				+ "        [QuantityYD] = ?,\r\n"
//				+ "        [QuantityMR] = ?,\r\n"
//				+ "        [PriceSTD] = ?,\r\n"
//				+ "        [DataStatus] = ?,\r\n"
//				+ "        [ChangeDate] = ?,\r\n"
//				+ "        [SyncDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ?\r\n"
//				+ "        AND [RollNumber] = ?;\r\n"
//				+ "    -- Check if rows were updated\r\n"
//				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "\r\n"
//				+ "    IF @rc = 0  AND ? <> ''\r\n"
//				+ "    BEGIN\r\n"
//				+ "        -- Insert if no rows were updated\r\n"
//				+ "        INSERT INTO [dbo].[FromSapGoodReceive] (\r\n"
//				+ "            [ProductionOrder],\r\n"
//				+ "            [SaleOrder],\r\n"
//				+ "            [SaleLine],\r\n"
//				+ "            [Grade],\r\n"
//				+ "            [RollNumber],\r\n"
//				+ "            [QuantityKG],\r\n"
//				+ "            [QuantityYD],\r\n"
//				+ "            [QuantityMR],\r\n"
//				+ "            [PriceSTD],\r\n"
//				+ "            [DataStatus],\r\n"
//				+ "            [ChangeDate],\r\n"
//				+ "            [CreateDate],\r\n"
//				+ "            [SyncDate]\r\n"
//				+ "        )\r\n"
//				+ "        VALUES (\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?,\r\n"
//				+ "            ?,\r\n"
//				+ "            ?,\r\n"
//				+ "            ?,\r\n"
//				+ "            ?\r\n"
//				+ "        );\r\n"
//				+ "    END\r\n"
//				+ "END";
//
//		int index = 1;
//
//		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
//Connection connection = this.database.getConnection();
//PreparedStatement prepared = null;
//
//try {
//    prepared = connection.prepareStatement(sql);
//			for (FromErpGoodReceiveDetail bean : paList) {
//				index = 1;
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getProductionOrder());
//
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				prepared.setString(index ++ , bean.getGrade());
//
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getPriceSTD(), index ++ );
//				prepared.setString(index ++ , bean.getDataStatus());
//
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getRollNumber());
//
//				prepared.setString(index ++ , bean.getRollNumber());// CHECK ROLL NUMBER <> ''
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				prepared.setString(index ++ , bean.getGrade());
//				prepared.setString(index ++ , bean.getRollNumber());
//
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getPriceSTD(), index ++ );
//				prepared.setString(index ++ , bean.getDataStatus());
//
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.addBatch(); 
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
