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

import th.co.wacoal.atech.pcms2.dao.master.FromSapSubmitDateDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapSubmitDateDaoImpl implements FromSapSubmitDateDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private final Logger log = LoggerFactory.getLogger(getClass());
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private JdbcTemplate jdbc;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSapSubmitDateDaoImpl(@Qualifier("pcmsDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<InputDateDetail> getSubmitDateDetail(ArrayList<PCMSTableDetail> poList)
	{
		ArrayList<InputDateDetail> list = null;
		PCMSTableDetail bean = poList.get(0);
		String prdOrder = bean.getProductionOrder();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String saleLine = bean.getSaleLine();
		String prdOrderSafe = (prdOrder == null ? "" : prdOrder.replace("'", "''"));
		String saleOrderSafe = (bean.getSaleOrder() == null ? "" : bean.getSaleOrder().replace("'", "''"));
		String saleLineSafe = (saleLine == null ? "" : saleLine.replace("'", "''"));
		String sql = " SELECT \r\n"
				+ "    	 [ProductionOrder]\r\n"
				+ "      , [SaleOrder]\r\n"
				+ "      , [SaleLine]\r\n"
				+ "      , [PlanDate]\r\n"
				+ "      , [CreateBy]\r\n"
				+ "      , [CreateDate]\r\n"
				+ "	   , '1:PCMS' as InputFrom \r\n"
				+ " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
				+ " where a.[ProductionOrder] = '"
				+ prdOrderSafe
				+ "' and \r\n"
				+ "       a.[SaleOrder] = '"
				+ saleOrderSafe
				+ "' and \r\n"
				+ "       a.[SaleLine] = '"
				+ saleLineSafe
				+ "' \r\n"
				+ " union ALL  \r\n "
				+ " SELECT \r\n"
				+ "        [ProductionOrder]\r\n"
				+ "      , [SaleOrder]\r\n"
				+ "      , [SaleLine]\r\n"
				+ "      , SubmitDate as [PlanDate]\r\n"
				+ "      , '' AS [CreateBy]\r\n"
				+ "      , null AS [CreateDate]\r\n"
				+ "	   , '0:ERP365' as InputFrom \r\n"
				+ " FROM [PCMS].[dbo].[FromSapSubmitDate]  as a\r\n"
				+ " where a.[ProductionOrder] = '"
				+ prdOrderSafe
				+ "' and SubmitDate is not null \r\n"
				+ "   and a.[DataStatus] = 'O' \r\n"
				+ " ORDER BY InputFrom ,CreateDate ";

		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}

//	@Override
//	public String upsertFromSapSubmitDateDetail(ArrayList<FromErpSubmitDateDetail> paList)
//	{
////		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//
//		String iconStatus = "I";
//		String sql = "-- Update if the record exists\r\n"
//				+ "IF ? = 'X'\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapSubmitDate]\r\n"
//				+ "    SET\r\n"
//				+ "        [DataStatus] = 'X',\r\n"
//				+ "        [ChangeDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ?\r\n"
//				+ "        AND [DataStatus] = 'O';\r\n"
//				+ "END\r\n"
//				+ "ELSE\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapSubmitDate]\r\n"
//				+ "    SET\r\n"
//				+ "        [SubmitDate] = ?,\r\n"
//				+ "        [Remark] = ?,\r\n"
//				+ "        [DataStatus] = ?,\r\n"
//				+ "        [ChangeDate] = ?,\r\n"
//				+ "        [SyncDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ?\r\n"
//				+ "        AND [SaleOrder] = ?\r\n"
//				+ "        AND [SaleLine] = ?\r\n"
//				+ "        AND [No] = ?;\r\n"
//				+ "    \r\n"
//				+ "    -- Check if rows were updated\r\n"
//				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "    \r\n"
//				+ "    IF @rc = 0\r\n"
//				+ "    BEGIN\r\n"
//				+ "        -- Insert if no rows were updated\r\n"
//				+ "        INSERT INTO [dbo].[FromSapSubmitDate] (\r\n"
//				+ "            [ProductionOrder],\r\n"
//				+ "            [SaleOrder],\r\n"
//				+ "            [SaleLine],\r\n"
//				+ "            [No],\r\n"
//				+ "            [SubmitDate],\r\n"
//				+ "            [Remark],\r\n"
//				+ "            [DataStatus],\r\n"
//				+ "            [ChangeDate],\r\n"
//				+ "            [CreateDate],\r\n"
//				+ "            [SyncDate]\r\n"
//				+ "        )\r\n"
//				+ "        VALUES (\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?,\r\n"
//				+ "            ?\r\n"
//				+ "        );\r\n"
//				+ "    END\r\n"
//				+ "END";
//
//		int index = 1;
//		int count = 0;
//		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
//Connection connection = this.database.getConnection();
//PreparedStatement prepared = null;
//
//try {
//    prepared = connection.prepareStatement(sql);
//			for (FromErpSubmitDateDetail bean : paList) {
//				index = 1;
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getProductionOrder());
//
//				this.sshUtl.setSqlDate(prepared, bean.getSubmitDate(), index ++ );
//				prepared.setString(index ++ , bean.getRemark());
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				prepared.setString(index ++ , bean.getNo());
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				prepared.setString(index ++ , bean.getNo());
//				this.sshUtl.setSqlDate(prepared, bean.getSubmitDate(), index ++ );
//				prepared.setString(index ++ , bean.getRemark());
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				// ... set parameters ...
//			    prepared.addBatch();
//
//			    if (++count % 1000 == 0) { // ส่งทุกๆ 1000 records
//			        prepared.executeBatch();
//			        count =0 ;System.out.println(new Date());
//			    }
//			}
//			prepared.executeBatch();
//			prepared.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			iconStatus = "E";
//		} finally {
//			// this.database.close();
//		}
//		return iconStatus;
//	}

	@Override
	public String upsertFromSapSubmitDateDetail(ArrayList<FromErpSubmitDateDetail> paList)
	{
		String iconStatus = "I";

		Connection conn = DataSourceUtils.getConnection(this.jdbc.getDataSource());
		PreparedStatement prepared = null;

		try {
			conn.setAutoCommit(false);

			try (Statement stmt = conn.createStatement()) {
				stmt.setQueryTimeout(300);
				// 1. สร้าง Temp Table ที่มีโครงสร้างเหมือนตารางจริง
				// แก้ไขจุดสร้าง Temp Table
				stmt.execute("IF OBJECT_ID('tempdb..#TempSubmitDate') IS NOT NULL DROP TABLE #TempSubmitDate");


				stmt.execute("CREATE TABLE #TempSubmitDate (" + "ProductionOrder NVARCHAR(50) COLLATE DATABASE_DEFAULT, " + // เพิ่ม
																															// COLLATE
						"SaleOrder NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "SaleLine NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "[No] NVARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "SubmitDate DATETIME, "
						+ "Remark nvarchar(200) COLLATE DATABASE_DEFAULT, "
						+ "DataStatus NVARCHAR(1) COLLATE DATABASE_DEFAULT, "
						+ "SyncDate DATETIME)");
				// 2. Bulk Insert ข้อมูลทั้งหมดลง Temp Table
				String insertTemp = "INSERT INTO #TempSubmitDate VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement ps = conn.prepareStatement(insertTemp)) {
					ps.setQueryTimeout(300);
					for (FromErpSubmitDateDetail bean : paList) {
						ps.setString(1, bean.getProductionOrder());
						ps.setString(2, bean.getSaleOrder());
						ps.setString(3, bean.getSaleLine());
						ps.setString(4, bean.getNo());
						this.sshUtl.setSqlDate(ps, bean.getSubmitDate(), 5);
						ps.setString(6, bean.getRemark());
						ps.setString(7, bean.getDataStatus());
						this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), 8);
						ps.addBatch();
					}
					ps.executeBatch();
				}

//				// 3. สั่ง Update ข้อมูลเดิมจาก Temp ไปยังตารางจริง (ทีเดียวจบ)
//				String updateSql = "UPDATE target SET "
//						+ "  target.SubmitDate = CASE WHEN src.DataStatus = 'X' THEN target.SubmitDate ELSE src.SubmitDate END, "
//						+ "  target.Remark = CASE WHEN src.DataStatus = 'X' THEN target.Remark ELSE src.Remark END, "
//						+ "  target.DataStatus = src.DataStatus, "
//						+ "  target.ChangeDate = GETDATE(), "
//						+ "  target.SyncDate = CASE WHEN src.DataStatus = 'X' THEN target.SyncDate ELSE src.SyncDate END "
//						+ "FROM [FromSapSubmitDate] AS target "
//						+ "INNER JOIN #TempSubmitDate AS src ON target.ProductionOrder = src.ProductionOrder "
//						+ "AND target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine AND target.No = src.No";
//				stmt.execute(updateSql);
//
//				// 4. สั่ง Insert ข้อมูลใหม่ที่ไม่มีในตารางจริง (ทีเดียวจบ)
//				String insertSql =
//						"INSERT INTO [FromSapSubmitDate] (ProductionOrder, SaleOrder, SaleLine, [No], SubmitDate, Remark, DataStatus, ChangeDate, CreateDate, SyncDate) "
//								+ "SELECT src.ProductionOrder, src.SaleOrder, src.SaleLine, src.No, src.SubmitDate, src.Remark, src.DataStatus, GETDATE(), GETDATE(), src.SyncDate "
//								+ "FROM #TempSubmitDate AS src "
//								+ "LEFT JOIN [FromSapSubmitDate] AS target ON target.ProductionOrder = src.ProductionOrder "
//								+ "AND target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine AND target.No = src.No "
//								+ "WHERE target.ProductionOrder IS NULL AND src.DataStatus != 'X'";
//				stmt.execute(insertSql);
				// รวมข้อ 3 และ 4 เป็น Batch เดียวเพื่อคุมเวลาและเพิ่มประสิทธิภาพ
				String upsertSql =
				      "SET XACT_ABORT ON; SET DEADLOCK_PRIORITY LOW; "
				    + "DECLARE @Now DATETIME = GETDATE(); "

				    + "/* 3. Update ข้อมูลเดิม โดยมีการเช็ก DataStatus = 'X' เพื่อรักษาค่าเดิม */ "
				    + "UPDATE target SET "
				    + "    target.SubmitDate = CASE WHEN src.DataStatus = 'X' THEN target.SubmitDate ELSE src.SubmitDate END, "
				    + "    target.Remark = CASE WHEN src.DataStatus = 'X' THEN target.Remark ELSE src.Remark END, "
				    + "    target.DataStatus = src.DataStatus, "
				    + "    target.ChangeDate = @Now, "
				    + "    target.SyncDate = CASE WHEN src.DataStatus = 'X' THEN target.SyncDate ELSE src.SyncDate END "
				    + "FROM [FromSapSubmitDate] AS target "
				    + "INNER JOIN #TempSubmitDate AS src ON target.ProductionOrder = src.ProductionOrder "
				    + "    AND target.SaleOrder = src.SaleOrder "
				    + "    AND target.SaleLine = src.SaleLine "
				    + "    AND target.No = src.No; "

				    + "/* 4. Insert ข้อมูลใหม่ที่ไม่มีในตารางจริง (ไม่รับตัวที่เป็น 'X') */ "
				    + "INSERT INTO [FromSapSubmitDate] ( "
				    + "    ProductionOrder, SaleOrder, SaleLine, [No], SubmitDate, Remark, "
				    + "    DataStatus, ChangeDate, CreateDate, SyncDate) "
				    + "SELECT "
				    + "    src.ProductionOrder, src.SaleOrder, src.SaleLine, src.No, src.SubmitDate, src.Remark, "
				    + "    src.DataStatus, @Now, @Now, src.SyncDate "
				    + "FROM #TempSubmitDate AS src "
				    + "LEFT JOIN [FromSapSubmitDate] AS target ON target.ProductionOrder = src.ProductionOrder "
				    + "    AND target.SaleOrder = src.SaleOrder "
				    + "    AND target.SaleLine = src.SaleLine "
				    + "    AND target.No = src.No "
				    + "WHERE target.ProductionOrder IS NULL AND src.DataStatus != 'X';";

				stmt.execute(upsertSql);
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}finally {
			    // ✅ ปิด transaction เสมอ ไม่ว่าจะ success หรือ error
			    try (java.sql.Statement cleanup = conn.createStatement()) {
			        cleanup.execute("IF OBJECT_ID('tempdb..#TempSubmitDate') IS NOT NULL DROP TABLE #TempSubmitDate");
			    } catch (Exception ignored) {}
			    try {
			        conn.setAutoCommit(true);
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			}
		} catch (Exception e) {
			log.error("[ERP-sync] upsertFromSapSubmitDateDetail failed", e);
			iconStatus = "E";
		} finally {
			DataSourceUtils.releaseConnection(conn, this.jdbc.getDataSource());
		}
		return iconStatus;
	}
}
