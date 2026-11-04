package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapPackingDao;
import th.co.wacoal.atech.pcms2.entities.PackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapPackingDaoImpl implements FromSapPackingDao {
	private final Logger log = LoggerFactory.getLogger(getClass());
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private JdbcTemplate jdbc;
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
	public FromSapPackingDaoImpl(@Qualifier("pcmsDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder)
	{
		ArrayList<PackingDetail> list = null;
		String where = " where  ";
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		where += " " + " fsp.ProductionOrder = '" + prodOrderSafe + "'  and \r\n" + " fsp.[DataStatus] = 'O' \r\n";
		String sql = ""
				+ " SELECT DISTINCT  \r\n"
				+ this.selectPacking
				+ "  from [PCMS].[dbo].[FromSapPacking] as fsp\r\n"
				+ "  left join [InspectSystem].[dbo].[InspectOrders] as insorder on fsp.[ProductionOrder] = insorder.[PrdNumber] \r\n "
				+ where;
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPackingDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapPackingDetail(ArrayList<FromErpPackingDetail> paList)
	{
		String iconStatus = "I";

		// ใช้ try-with-resources ตามสไตล์ Java 8 เพื่อจัดการ Connection
		Connection conn = DataSourceUtils.getConnection(this.jdbc.getDataSource());
		PreparedStatement prepared = null;

		try {
			conn.setAutoCommit(false);

			try (Statement stmt = conn.createStatement()) {
				stmt.setQueryTimeout(300);
				// 1. สร้าง Temp Table ให้ตรงตาม Schema (Decimal 13,3 และ Varchar
				// ตามความยาวที่กำหนด)
				stmt.execute("IF OBJECT_ID('tempdb..#TempPacking') IS NOT NULL DROP TABLE #TempPacking");
				stmt.execute("CREATE TABLE #TempPacking ("
						+ "ProductionOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "PostingDate DATE, "
						+ "Quantity DECIMAL(13, 3), "
						+ "RollNo VARCHAR(10) COLLATE DATABASE_DEFAULT, "
						+ "QuantityKG DECIMAL(13, 3), "
						+ "Grade VARCHAR(10) COLLATE DATABASE_DEFAULT, "
						+ "No VARCHAR(10) COLLATE DATABASE_DEFAULT, "
						+ "QuantityYD DECIMAL(13, 3), "
						+ "DataStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, "
						+ "SyncDate DATETIME)");

				// 2. Bulk Insert ข้อมูลจาก ArrayList ลงใน Temp Table
				String insertTempSql = "INSERT INTO #TempPacking VALUES (?,?,?,?,?,?,?,?,?,?)";
				try (PreparedStatement ps = conn.prepareStatement(insertTempSql)) {
					ps.setQueryTimeout(300);
					for (FromErpPackingDetail bean : paList) {
						int idx = 1;
						ps.setString(idx ++ , bean.getProductionOrder());
						this.sshUtl.setSqlDate(ps, bean.getPostingDate(), idx ++ );
						this.sshUtl.setSqlBigDecimal(ps, bean.getQuantity(), idx ++ );
						ps.setString(idx ++ , bean.getRollNo());
						this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityKG(), idx ++ );
						ps.setString(idx ++ , bean.getGrade());
						ps.setString(idx ++ , bean.getNo());
						this.sshUtl.setSqlBigDecimal(ps, bean.getQuantityYD(), idx ++ );
						ps.setString(idx ++ , bean.getDataStatus());
						this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx ++ );
						ps.addBatch();
					}
					ps.executeBatch();
				}

				// 3. จัดการ DataStatus = 'X' (สั่งยกเลิกตาม ProductionOrder)
//				stmt.execute("UPDATE target SET target.DataStatus = 'X', target.ChangeDate = GETDATE() "
//						+ "FROM [FromSapPacking] AS target "
//						+ "INNER JOIN #TempPacking AS src ON target.ProductionOrder = src.ProductionOrder "
//						+ "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'");
//
//				// 4. Update ข้อมูลเดิม (Matching ด้วย ProductionOrder + RollNo)
//				stmt.execute("UPDATE target SET "
//						+ "target.PostingDate = src.PostingDate, "
//						+ "target.Quantity = src.Quantity, "
//						+ "target.QuantityKG = src.QuantityKG, "
//						+ "target.Grade = src.Grade, "
//						+ "target.No = src.No, "
//						+ "target.QuantityYD = src.QuantityYD, "
//						+ "target.ChangeDate = GETDATE(), "
//						+ "target.SyncDate = src.SyncDate, "
//						+ "target.DataStatus = src.DataStatus "
//						+ "FROM [FromSapPacking] AS target "
//						+ "INNER JOIN #TempPacking AS src ON target.ProductionOrder = src.ProductionOrder AND target.RollNo = src.RollNo "
//						+ "WHERE src.DataStatus <> 'X'");
//
//				// 5. Insert ข้อมูลม้วนใหม่
//				stmt.execute("INSERT INTO [FromSapPacking] (ProductionOrder, PostingDate, Quantity, RollNo, QuantityKG, "
//						+ "Grade, No, QuantityYD, ChangeDate, CreateDate, SyncDate, DataStatus) "
//						+ "SELECT src.ProductionOrder, src.PostingDate, src.Quantity, src.RollNo, src.QuantityKG, "
//						+ "src.Grade, src.No, src.QuantityYD, GETDATE(), GETDATE(), src.SyncDate, src.DataStatus "
//						+ "FROM #TempPacking AS src "
//						+ "LEFT JOIN [FromSapPacking] AS target ON target.ProductionOrder = src.ProductionOrder AND target.RollNo = src.RollNo "
//						+ "WHERE target.ProductionOrder IS NULL "
//						+ "AND src.DataStatus <> 'X' "
//						+ "AND src.RollNo IS NOT NULL AND src.RollNo <> ''");
				// 3, 4, 5. รวมเป็น Batch เดียวเพื่อประสิทธิภาพและเวลาที่แม่นยำ
				String upsertSql = "SET XACT_ABORT ON; SET DEADLOCK_PRIORITY LOW; "
						+ "DECLARE @Now DATETIME = GETDATE(); "

						+ "/* 3. จัดการ DataStatus = 'X' */ "
						+ "UPDATE target SET "
						+ "    target.DataStatus = 'X', "
						+ "    target.ChangeDate = @Now "
						+ "FROM [FromSapPacking] AS target "
						+ "INNER JOIN #TempPacking AS src ON target.ProductionOrder = src.ProductionOrder "
						+ "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'; "

						+ "/* 4. Update ข้อมูลเดิม (Matching ด้วย ProductionOrder + RollNo) */ "
						+ "UPDATE target SET "
						+ "    target.PostingDate = src.PostingDate, "
						+ "    target.Quantity = src.Quantity, "
						+ "    target.QuantityKG = src.QuantityKG, "
						+ "    target.Grade = src.Grade, "
						+ "    target.No = src.No, "
						+ "    target.QuantityYD = src.QuantityYD, "
						+ "    target.ChangeDate = @Now, "
						+ "    target.SyncDate = src.SyncDate, "
						+ "    target.DataStatus = src.DataStatus "
						+ "FROM [FromSapPacking] AS target "
						+ "INNER JOIN #TempPacking AS src ON target.ProductionOrder = src.ProductionOrder AND target.RollNo = src.RollNo "
						+ "WHERE src.DataStatus <> 'X'; "

						+ "/* 5. Insert ข้อมูลม้วนใหม่ */ "
						+ "INSERT INTO [FromSapPacking] ( "
						+ "    ProductionOrder, PostingDate, Quantity, RollNo, QuantityKG, "
						+ "    Grade, No, QuantityYD, ChangeDate, CreateDate, SyncDate, DataStatus) "
						+ "SELECT "
						+ "    src.ProductionOrder, src.PostingDate, src.Quantity, src.RollNo, src.QuantityKG, "
						+ "    src.Grade, src.No, src.QuantityYD, @Now, @Now, src.SyncDate, src.DataStatus "
						+ "FROM #TempPacking AS src "
						+ "LEFT JOIN [FromSapPacking] AS target ON target.ProductionOrder = src.ProductionOrder AND target.RollNo = src.RollNo "
						+ "WHERE target.ProductionOrder IS NULL "
						+ "  AND src.DataStatus <> 'X' "
						+ "  AND src.RollNo IS NOT NULL AND src.RollNo <> '';";

				stmt.execute(upsertSql);
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			} finally {
				// ✅ ปิด transaction เสมอ ไม่ว่าจะ success หรือ error
				try (java.sql.Statement cleanup = conn.createStatement()) {
					cleanup.execute("IF OBJECT_ID('tempdb..#TempPacking') IS NOT NULL DROP TABLE #TempPacking");
				} catch (Exception ignored) {}
				try {
					conn.setAutoCommit(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error("[ERP-sync] upsertFromSapPackingDetail failed", e);
			iconStatus = "E";
		} finally {
			DataSourceUtils.releaseConnection(conn, this.jdbc.getDataSource());
		}
		return iconStatus;
	}
//	@Override
//	public String upsertFromSapPackingDetail(ArrayList<FromErpPackingDetail> paList)
//	{
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//
//		String iconStatus = "I";
//		String sql = " "
//				+ " "
//				+ "-- Update if the record exists\r\n"
//				+ "IF ? = 'X'\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapPacking]\r\n"
//				+ "    SET\r\n"
//				+ "        [DataStatus] = 'X',\r\n"
//				+ "        [ChangeDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ?\r\n"
//				+ "        AND [DataStatus] = 'O';\r\n"
//				+ "END\r\n"
//				+ "ELSE\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapPacking]\r\n"
//				+ "    SET\r\n"
//				+ "        [PostingDate] = ?,\r\n"
//				+ "        [Quantity] = ?,\r\n"
//				+ "        [QuantityKG] = ?,\r\n"
//				+ "        [Grade] = ?,\r\n"
//				+ "        [No] = ?,\r\n"
//				+ "        [QuantityYD] = ?,\r\n"
//				+ "        [ChangeDate] = ?,\r\n"
//				+ "        [SyncDate] = ?,\r\n"
//				+ "        [DataStatus] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ?\r\n"
//				+ "        AND [RollNo] = ?;\r\n"
//				+ "\r\n"
//				+ "    -- Check if rows were updated\r\n"
//				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "\r\n"
//				+ "    IF @rc = 0\r\n"
//				+ "    BEGIN\r\n"
//				+ "        -- Insert if no rows were updated\r\n"
//				+ "        INSERT INTO [dbo].[FromSapPacking] (\r\n"
//				+ "            [ProductionOrder],\r\n"
//				+ "            [PostingDate],\r\n"
//				+ "            [Quantity],\r\n"
//				+ "            [RollNo],\r\n"
//				+ "            [QuantityKG],\r\n"
//				+ "            [Grade],\r\n"
//				+ "            [No],\r\n"
//				+ "            [QuantityYD],\r\n"
//				+ "            [ChangeDate],\r\n"
//				+ "            [CreateDate],\r\n"
//				+ "            [SyncDate],\r\n"
//				+ "            [DataStatus]\r\n"
//				+ "        )\r\n"
//				+ "        VALUES (\r\n"
//				+ "            ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?\r\n"
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
//			for (FromErpPackingDetail bean : paList) {
//				index = 1;
//
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getProductionOrder());
//
//				this.sshUtl.setSqlDate(prepared, bean.getPostingDate(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantity(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
//				prepared.setString(index ++ , bean.getGrade());
//				prepared.setString(index ++ , bean.getNo());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.setString(index ++ , bean.getDataStatus());
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getRollNo());
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				this.sshUtl.setSqlDate(prepared, bean.getPostingDate(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantity(), index ++ );
//				prepared.setString(index ++ , bean.getRollNo());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG(), index ++ );
//				prepared.setString(index ++ , bean.getGrade());
//				prepared.setString(index ++ , bean.getNo());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityYD(), index ++ );
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.setString(index ++ , bean.getDataStatus());
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
