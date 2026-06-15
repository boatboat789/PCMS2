package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainProdSaleDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
//import model.BeanCreateModฉel;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapMainProdSaleDaoImpl implements FromSapMainProdSaleDao {
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
	public FromSapMainProdSaleDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}
	@Override
	public String upsertFromSapMainProdSaleDetail(ArrayList<FromErpMainProdSaleDetail> paList) {
	    String iconStatus = "I";
		Connection conn = this.database.getConnection();
		PreparedStatement prepared = null;

		try {
	        conn.setAutoCommit(false);

	        try (Statement stmt = conn.createStatement()) {
	            // 1. สร้าง Temp Table (ใช้ Decimal 13,3 ตาม Schema)
				stmt.execute("IF OBJECT_ID('tempdb..#TempMainProdSale') IS NOT NULL DROP TABLE #TempMainProdSale");
	            stmt.execute("CREATE TABLE #TempMainProdSale (" +
	                         "ProductionOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "SaleOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "SaleLine VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "Volumn DECIMAL(13, 3), " +
	                         "DataStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, " +
	                         "SyncDate DATETIME)");

	            // 2. Bulk Insert ลง Temp Table
	            String insertTemp = "INSERT INTO #TempMainProdSale VALUES (?,?,?,?,?,?)";
	            try (PreparedStatement ps = conn.prepareStatement(insertTemp)) {
	                for (FromErpMainProdSaleDetail bean : paList) {
	                    int idx = 1;
	                    ps.setString(idx++, bean.getProductionOrder());
	                    ps.setString(idx++, bean.getSaleOrder());
	                    ps.setString(idx++, bean.getSaleLine());
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getVolumn(), idx++);
	                    ps.setString(idx++, bean.getDataStatus());
	                    this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx++);
	                    ps.addBatch();
	                }
	                ps.executeBatch();
	            }

//	            // 3. จัดการ DataStatus = 'X'
//	            stmt.execute("UPDATE target SET target.DataStatus = 'X', target.ChangeDate = GETDATE() " +
//	                         "FROM [FromSapMainProdSale] AS target " +
//	                         "INNER JOIN #TempMainProdSale AS src ON target.ProductionOrder = src.ProductionOrder " +
//	                         "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'");
//
//	            // 4. Update ข้อมูลเดิม (Matching: PO + SO + Line)
//	            stmt.execute("UPDATE target SET " +
//	                         "target.Volumn = src.Volumn, " +
//	                         "target.DataStatus = src.DataStatus, " +
//	                         "target.ChangeDate = GETDATE(), " +
//	                         "target.SyncDate = src.SyncDate " +
//	                         "FROM [FromSapMainProdSale] AS target " +
//	                         "INNER JOIN #TempMainProdSale AS src ON " +
//	                         "target.ProductionOrder = src.ProductionOrder AND " +
//	                         "target.SaleOrder = src.SaleOrder AND " +
//	                         "target.SaleLine = src.SaleLine " +
//	                         "WHERE src.DataStatus <> 'X'");
//
//	            // 5. Insert ข้อมูลใหม่ (เช็คเงื่อนไข SaleOrder/Line ไม่เป็นค่าว่าง)
//	            stmt.execute("INSERT INTO [FromSapMainProdSale] (ProductionOrder, SaleOrder, SaleLine, Volumn, " +
//	                         "DataStatus, ChangeDate, CreateDate, SyncDate) " +
//	                         "SELECT src.ProductionOrder, src.SaleOrder, src.SaleLine, src.Volumn, " +
//	                         "src.DataStatus, GETDATE(), GETDATE(), src.SyncDate " +
//	                         "FROM #TempMainProdSale AS src " +
//	                         "LEFT JOIN [FromSapMainProdSale] AS target ON " +
//	                         "target.ProductionOrder = src.ProductionOrder AND " +
//	                         "target.SaleOrder = src.SaleOrder AND " +
//	                         "target.SaleLine = src.SaleLine " +
//	                         "WHERE target.ProductionOrder IS NULL " +
//	                         "AND src.DataStatus <> 'X' " +
//	                         "AND src.SaleOrder <> '' AND src.SaleLine <> ''");
	         // รวมข้อ 3, 4, 5 เป็น Batch เดียวเพื่อคุมเวลา GETDATE() ให้ตรงกัน และลดการ Round-trip ของ Network
	            String upsertSql = 
	                  "DECLARE @Now DATETIME = GETDATE(); "
	                
	                + "/* 3. จัดการ DataStatus = 'X' (สั่งปิดรายการตาม ProductionOrder) */ "
	                + "UPDATE target SET "
	                + "    target.DataStatus = 'X', "
	                + "    target.ChangeDate = @Now "
	                + "FROM [FromSapMainProdSale] AS target "
	                + "INNER JOIN #TempMainProdSale AS src ON target.ProductionOrder = src.ProductionOrder "
	                + "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'; "

	                + "/* 4. Update ข้อมูลเดิม (Matching: PO + SO + Line) */ "
	                + "UPDATE target SET "
	                + "    target.Volumn = src.Volumn, "
	                + "    target.DataStatus = src.DataStatus, "
	                + "    target.ChangeDate = @Now, "
	                + "    target.SyncDate = src.SyncDate "
	                + "FROM [FromSapMainProdSale] AS target "
	                + "INNER JOIN #TempMainProdSale AS src ON "
	                + "    target.ProductionOrder = src.ProductionOrder AND "
	                + "    target.SaleOrder = src.SaleOrder AND "
	                + "    target.SaleLine = src.SaleLine "
	                + "WHERE src.DataStatus <> 'X'; "

	                + "/* 5. Insert ข้อมูลใหม่ (เช็คเงื่อนไข SaleOrder/Line ไม่เป็นค่าว่าง) */ "
	                + "INSERT INTO [FromSapMainProdSale] ( "
	                + "    ProductionOrder, SaleOrder, SaleLine, Volumn, "
	                + "    DataStatus, ChangeDate, CreateDate, SyncDate) "
	                + "SELECT "
	                + "    src.ProductionOrder, src.SaleOrder, src.SaleLine, src.Volumn, "
	                + "    src.DataStatus, @Now, @Now, src.SyncDate "
	                + "FROM #TempMainProdSale AS src "
	                + "LEFT JOIN [FromSapMainProdSale] AS target ON "
	                + "    target.ProductionOrder = src.ProductionOrder AND "
	                + "    target.SaleOrder = src.SaleOrder AND "
	                + "    target.SaleLine = src.SaleLine "
	                + "WHERE target.ProductionOrder IS NULL "
	                + "  AND src.DataStatus <> 'X' "
	                + "  AND src.SaleOrder <> '' AND src.SaleLine <> '';";

	            stmt.execute(upsertSql);
	            conn.commit();
	        } catch (Exception e) {
	            conn.rollback();
	            throw e;
	        }
	        finally {
	            // ✅ ปิด transaction เสมอ ไม่ว่าจะ success หรือ error
	            try (java.sql.Statement cleanup = conn.createStatement()) {
	                cleanup.execute("IF OBJECT_ID('tempdb..#TempMainProdSale') IS NOT NULL DROP TABLE #TempMainProdSale");
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
//	public String upsertFromSapMainProdSaleDetail(ArrayList<FromErpMainProdSaleDetail> paList)
//	{
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//
//		String iconStatus = "I";
//		String sql = "-- Update if the record exists\r\n"
//				+ "IF ? = 'X'\r\n"
//				+ "	BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapMainProdSale]\r\n"
//				+ "    SET [DataStatus] = 'X'\r\n"
//				+ "        ,[ChangeDate] = ? \r\n"
//				+ "    WHERE [ProductionOrder] = ? \r\n"
//				+ "      and [DataStatus] = 'O' ;\r\n"
//				+ "	END\r\n"
//				+ "ELSE \r\n"
//				+ "	BEGIN\r\n"
//				+ "		UPDATE [dbo].[FromSapMainProdSale]\r\n"
//				+ "		SET \r\n"
//				+ "    		[Volumn] = ?\r\n"
//				+ "    		,[DataStatus] = ?\r\n"
//				+ "    		,[ChangeDate] = ? \r\n"
//				+ "    		,[SyncDate] =  ?\r\n"
//				+ "		WHERE \r\n"
//				+ "    		[ProductionOrder] = ? and"
//				+ "    		[SaleOrder] = ? and\r\n"
//				+ "    		[SaleLine] = ?  ;\r\n"
//				+ "		-- Check if rows were updated\r\n"
//				+ "		DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "		IF @rc = 0\r\n"
//				+ "			BEGIN\r\n"
//				+ "    		-- Insert if no rows were updated\r\n"
//				+ "    		-- Add a condition to prevent insert if SaleOrder or SaleLine are blank\r\n"
//				+ "    		IF ? <> '' AND ? <> ''\r\n"
//				+ "    			BEGIN\r\n"
//				+ "        			INSERT INTO [dbo].[FromSapMainProdSale] (\r\n"
//				+ "            			[ProductionOrder]  ,[SaleOrder] ,[SaleLine] ,[Volumn] ,[DataStatus]\r\n"
//				+ "          			,[ChangeDate] ,[CreateDate]\r\n"
//				+ "          			,[SyncDate] \r\n"
//				+ "        			) "
//				+ "        			VALUES (\r\n"
//				+ "            			?, ?, ?, ?, ?, "
//				+ "            			?, ?\r\n"
//				+ "            			, ? "
//				+ "        			); "
//				+ "    			END\r\n"
//				+ "			END "
//				+ "	END\r\n";
//
//		int index = 1;
//
//		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
//Connection connection = this.database.getConnection();
//PreparedStatement prepared = null;
//
//try {
//    prepared = connection.prepareStatement(sql);
//			for (FromErpMainProdSaleDetail bean : paList) {
//				index = 1;
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getProductionOrder());
//
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getVolumn(), index ++ );
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getVolumn(), index ++ );
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//
//				prepared.addBatch();
////				prepared.setString(index++, bean.get    );
////this.sshUtl.setSqlDate(prepared, bean.get , index++); 
////				prepared.setTimestamp(index++, new Timestamp(time));
////this.sshUtl.setSqlBigDecimal(prepared, bean.get , index++); 
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
