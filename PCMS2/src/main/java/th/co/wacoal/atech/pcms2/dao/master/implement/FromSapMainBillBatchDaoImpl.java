package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
		String iconStatus = "I";
		Connection conn = this.database.getConnection();
//		PreparedStatement prepared = null;

		try {
			conn.setAutoCommit(false);

			try (Statement stmt = conn.createStatement()) {
				// 1. สร้าง Temp Table ให้ตรงตาม Schema (13, 3) และใช้ COLLATE DATABASE_DEFAULT
				stmt.execute("IF OBJECT_ID('tempdb..#TempMainBill') IS NOT NULL DROP TABLE #TempMainBill");
				stmt.execute("CREATE TABLE #TempMainBill ("
						+ "BillDoc VARCHAR(20) COLLATE DATABASE_DEFAULT, "
						+ "BillItem VARCHAR(20) COLLATE DATABASE_DEFAULT, "
						+ "LotShipping DATE, "
						+ "ProductionOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "SaleOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "SaleLine VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "Grade VARCHAR(20) COLLATE DATABASE_DEFAULT, "
						+ "RollNumber VARCHAR(20) COLLATE DATABASE_DEFAULT, "
						+ "QuantityKG DECIMAL(13, 3), "
						+ "QuantityYD DECIMAL(13, 3), "
						+ "QuantityMR DECIMAL(13, 3), "
						+ "LotNo VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "DataStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, "
						+ "SyncDate DATETIME)");

				// 2. Bulk Insert ลง Temp Table
				String insertTemp = "INSERT INTO #TempMainBill VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				try (PreparedStatement ps = conn.prepareStatement(insertTemp)) {
					for (FromErpMainBillBatchDetail bean : paList) {
						int idx = 1;
						ps.setString(idx ++ , bean.getBillDoc());
						ps.setString(idx ++ , bean.getBillItem());
						this.sshUtl.setSqlDate(ps, bean.getLotShipping(), idx ++ );
						ps.setString(idx ++ , bean.getProductionOrder());
						ps.setString(idx ++ , bean.getSaleOrder());
						ps.setString(idx ++ , bean.getSaleLine());
						ps.setString(idx ++ , bean.getGrade());
						ps.setString(idx ++ , bean.getRollNumber());
						this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityKG(), idx ++ );
						this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityYD(), idx ++ );
						this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityMR(), idx ++ );
						ps.setString(idx ++ , bean.getLotNo());
						ps.setString(idx ++ , bean.getDataStatus());
						this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx ++ );
						ps.addBatch();
					}
					ps.executeBatch();
				}

//				// 3. จัดการ DataStatus = 'X' (สั่งปิดรายการตาม ProductionOrder)
//				stmt.execute("UPDATE target SET target.DataStatus = 'X', target.ChangeDate = GETDATE() "
//						+ "FROM [FromSapMainBillBatch] AS target "
//						+ "INNER JOIN #TempMainBill AS src ON target.ProductionOrder = src.ProductionOrder "
//						+ "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'");
//
//				// 4. Update ข้อมูลเดิม (Matching 6 Keys)
//				stmt.execute("UPDATE target SET "
//						+ "  target.LotShipping = src.LotShipping, target.Grade = src.Grade, "
//						+ "  target.QuantityKG = src.QuantityKG, target.QuantityYD = src.QuantityYD, "
//						+ "  target.QuantityMR = src.QuantityMR, target.LotNo = src.LotNo, "
//						+ "  target.DataStatus = src.DataStatus, target.ChangeDate = GETDATE(), "
//						+ "  target.SyncDate = src.SyncDate "
//						+ "FROM [FromSapMainBillBatch] AS target "
//						+ "INNER JOIN #TempMainBill AS src ON "
//						+ "  target.BillDoc = src.BillDoc AND target.BillItem = src.BillItem AND "
//						+ "  target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine AND "
//						+ "  target.RollNumber = src.RollNumber AND target.ProductionOrder = src.ProductionOrder "
//						+ "WHERE src.DataStatus <> 'X'");
//
//				// 5. Insert ข้อมูลใหม่
//				stmt.execute("INSERT INTO [FromSapMainBillBatch] (BillDoc, BillItem, LotShipping, ProductionOrder, "
//						+ "SaleOrder, SaleLine, Grade, RollNumber, QuantityKG, QuantityYD, QuantityMR, LotNo, "
//						+ "DataStatus, ChangeDate, CreateDate, SyncDate) "
//						+ "SELECT src.BillDoc, src.BillItem, src.LotShipping, src.ProductionOrder, "
//						+ "src.SaleOrder, src.SaleLine, src.Grade, src.RollNumber, src.QuantityKG, src.QuantityYD, "
//						+ "src.QuantityMR, src.LotNo, src.DataStatus, GETDATE(), GETDATE(), src.SyncDate "
//						+ "FROM #TempMainBill AS src "
//						+ "LEFT JOIN [FromSapMainBillBatch] AS target ON "
//						+ "  target.BillDoc = src.BillDoc AND target.BillItem = src.BillItem AND "
//						+ "  target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine AND "
//						+ "  target.RollNumber = src.RollNumber AND target.ProductionOrder = src.ProductionOrder "
//						+ "WHERE target.ProductionOrder IS NULL "
//						+ "  AND src.DataStatus <> 'X' "
//						+ "  AND src.BillDoc IS NOT NULL AND src.BillDoc <> ''");
				// รวมข้อ 3, 4, 5 เป็น Batch เดียวเพื่อคุมเวลา GETDATE() ให้เท่ากันทั้ง 3 Step
				String upsertSql = 
				      "DECLARE @Now DATETIME = GETDATE(); "
				    
				    + "/* 3. จัดการ DataStatus = 'X' (สั่งปิดรายการตาม ProductionOrder) */ "
				    + "UPDATE target SET "
				    + "    target.DataStatus = 'X', "
				    + "    target.ChangeDate = @Now "
				    + "FROM [FromSapMainBillBatch] AS target "
				    + "INNER JOIN #TempMainBill AS src ON target.ProductionOrder = src.ProductionOrder "
				    + "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'; "

				    + "/* 4. Update ข้อมูลเดิม (Matching 6 Keys) */ "
				    + "UPDATE target SET "
				    + "    target.LotShipping = src.LotShipping, "
				    + "    target.Grade = src.Grade, "
				    + "    target.QuantityKG = src.QuantityKG, "
				    + "    target.QuantityYD = src.QuantityYD, "
				    + "    target.QuantityMR = src.QuantityMR, "
				    + "    target.LotNo = src.LotNo, "
				    + "    target.DataStatus = src.DataStatus, "
				    + "    target.ChangeDate = @Now, "
				    + "    target.SyncDate = src.SyncDate "
				    + "FROM [FromSapMainBillBatch] AS target "
				    + "INNER JOIN #TempMainBill AS src ON "
				    + "    target.BillDoc = src.BillDoc AND target.BillItem = src.BillItem AND "
				    + "    target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine AND "
				    + "    target.RollNumber = src.RollNumber AND target.ProductionOrder = src.ProductionOrder "
				    + "WHERE src.DataStatus <> 'X'; "

				    + "/* 5. Insert ข้อมูลใหม่ */ "
				    + "INSERT INTO [FromSapMainBillBatch] ( "
				    + "    BillDoc, BillItem, LotShipping, ProductionOrder, "
				    + "    SaleOrder, SaleLine, Grade, RollNumber, QuantityKG, QuantityYD, "
				    + "    QuantityMR, LotNo, DataStatus, ChangeDate, CreateDate, SyncDate) "
				    + "SELECT "
				    + "    src.BillDoc, src.BillItem, src.LotShipping, src.ProductionOrder, "
				    + "    src.SaleOrder, src.SaleLine, src.Grade, src.RollNumber, src.QuantityKG, src.QuantityYD, "
				    + "    src.QuantityMR, src.LotNo, src.DataStatus, @Now, @Now, src.SyncDate "
				    + "FROM #TempMainBill AS src "
				    + "LEFT JOIN [FromSapMainBillBatch] AS target ON "
				    + "    target.BillDoc = src.BillDoc AND target.BillItem = src.BillItem AND "
				    + "    target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine AND "
				    + "    target.RollNumber = src.RollNumber AND target.ProductionOrder = src.ProductionOrder "
				    + "WHERE target.ProductionOrder IS NULL "
				    + "  AND src.DataStatus <> 'X' "
				    + "  AND src.BillDoc IS NOT NULL AND src.BillDoc <> '';";

				stmt.execute(upsertSql);
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			finally {
			    // ✅ ปิด transaction เสมอ ไม่ว่าจะ success หรือ error
			    try (java.sql.Statement cleanup = conn.createStatement()) {
			        cleanup.execute("IF OBJECT_ID('tempdb..#TempMainBill') IS NOT NULL DROP TABLE #TempMainBill");
			    } catch (Exception ignored) {}
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
//	public String upsertFromSapMainBillBatchDetail(ArrayList<FromErpMainBillBatchDetail> paList)
//	{
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//		String iconStatus = "I";
//		String sql = " "
//				+ "-- Update if the record exists\r\n"
//				+ "IF ? = 'X'\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapMainBillBatch]\r\n"
//				+ "    SET\r\n"
//				+ "        [DataStatus] = 'X',\r\n"
//				+ "        [ChangeDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ? \r\n"
//				+ "      and [DataStatus] = 'O' ;\r\n"
//				+ "END\r\n"
//				+ "ELSE\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapMainBillBatch]\r\n"
//				+ "    SET\r\n"
//				+ "        [LotShipping] = ?,\r\n"
//				+ "        [Grade] = ?,\r\n"
//				+ "        [QuantityKG] = ?,\r\n"
//				+ "        [QuantityYD] = ?,\r\n"
//				+ "        [QuantityMR] = ?,\r\n"
//				+ "        [LotNo] = ?,\r\n"
//				+ "        [DataStatus] = ?,\r\n"
//				+ "        [ChangeDate] = ?,\r\n"
//				+ "        [SyncDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [BillDoc] = ?\r\n"
//				+ "        AND [BillItem] = ?\r\n"
//				+ "        AND [SaleOrder] = ?\r\n"
//				+ "        AND [SaleLine] = ?\r\n"
//				+ "        AND [RollNumber] = ?\r\n"
//				+ "        AND [ProductionOrder] = ?;\r\n"
//				+ "\r\n"
//				+ "    -- Check if rows were updated\r\n"
//				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "\r\n"
//				+ "    IF @rc = 0  AND ? <> ''\r\n"
//				+ "    BEGIN\r\n"
//				+ "        -- Insert if no rows were updated\r\n"
//				+ "        INSERT INTO [dbo].[FromSapMainBillBatch] (\r\n"
//				+ "            [BillDoc],\r\n"
//				+ "            [BillItem],\r\n"
//				+ "            [LotShipping],\r\n"
//				+ "            [ProductionOrder],\r\n"
//				+ "            [SaleOrder],\r\n"
//				+ "            [SaleLine],\r\n"
//				+ "            [Grade],\r\n"
//				+ "            [RollNumber],\r\n"
//				+ "            [QuantityKG],\r\n"
//				+ "            [QuantityYD],\r\n"
//				+ "            [QuantityMR],\r\n"
//				+ "            [LotNo],\r\n"
//				+ "            [DataStatus],\r\n"
//				+ "            [ChangeDate],\r\n"
//				+ "            [CreateDate],\r\n"
//				+ "            [SyncDate]\r\n"
//				+ "        )\r\n"
//				+ "        VALUES (\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?\r\n"
//				+ "        );\r\n"
//				+ "    END\r\n"
//				+ "END";
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
//			for (FromErpMainBillBatchDetail bean : paList) {
//				index = 1;
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getProductionOrder());
//				this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
//				prepared.setString(index ++ , bean.getGrade());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
//
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ );
//				prepared.setString(index ++ , bean.getLotNo());
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//
//				prepared.setString(index ++ , bean.getBillDoc());
//				prepared.setString(index ++ , bean.getBillItem());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				prepared.setString(index ++ , bean.getRollNumber());
//				prepared.setString(index ++ , bean.getProductionOrder());
//
//				prepared.setString(index ++ , bean.getBillDoc());// CHECK BILL NUMBER <> ''
//
//				prepared.setString(index ++ , bean.getBillDoc());
//				prepared.setString(index ++ , bean.getBillItem());
//				this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getSaleOrder());
//
//				prepared.setString(index ++ , bean.getSaleLine());
//				prepared.setString(index ++ , bean.getGrade());
//				prepared.setString(index ++ , bean.getRollNumber());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
//
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR(), index ++ );
//				prepared.setString(index ++ , bean.getLotNo());
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.addBatch();
//				batchSize ++ ;
//				if (batchSize % 500 == 0) { // Execute batch every 500 records
//					prepared.executeBatch();
//					prepared.clearBatch();
//					batchSize = 0; // Reset batch size
//				}
//			}
////			System.out.println("here1");
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
