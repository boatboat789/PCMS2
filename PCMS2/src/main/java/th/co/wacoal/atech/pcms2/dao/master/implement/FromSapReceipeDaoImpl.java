package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapReceipeDao;
import th.co.wacoal.atech.pcms2.entities.ReceipeDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpReceipeDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapReceipeDaoImpl implements FromSapReceipeDao {
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSapReceipeDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<ReceipeDetail> getFromSapReceipeDetailByProductionOrder(String prodOrder)
	{
		ArrayList<ReceipeDetail> list = null;
		String where = " where  ";
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  \r\n"
				+ "   [ProductionOrder],[No],[PostingDate] ,[LotNo],[Receipe],a.[DataStatus] \r\n"
				+ " from [PCMS].[dbo].[FromSapReceipe] as a \r\n "
				+ where
				+ " Order by No";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genReceipeDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapReceipeDetail(ArrayList<FromErpReceipeDetail> paList)
	{
		String iconStatus = "I";

		Connection conn = this.database.getConnection();
		PreparedStatement prepared = null;

		try {
			conn.setAutoCommit(false);

			try (Statement stmt = conn.createStatement()) {
				// 1. สร้าง Temp Table (กำหนดขนาดตาม Schema ที่ส่งมา)
				stmt.execute("IF OBJECT_ID('tempdb..#TempReceipe') IS NOT NULL DROP TABLE #TempReceipe");
				
				stmt.execute("CREATE TABLE #TempReceipe ("
						+ "ProductionOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "LotNo VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "DataStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, "
						+ "SyncDate DATETIME)");

				// 2. Bulk Insert ลง Temp Table
				String insertTemp = "INSERT INTO #TempReceipe VALUES (?,?,?,?)";
				try (PreparedStatement ps = conn.prepareStatement(insertTemp)) {
					for (FromErpReceipeDetail bean : paList) {
						int idx = 1;
						ps.setString(idx ++ , bean.getProductionOrder());
						ps.setString(idx ++ , bean.getLotNo());
						ps.setString(idx ++ , bean.getDataStatus());
						this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx ++ );
						ps.addBatch();
					}
					ps.executeBatch();
				}
				// 3 & 4. รวมเป็น Batch เดียวเพื่อประสิทธิภาพและเวลาที่แม่นยำ
				String upsertSql = 
				      "DECLARE @Now DATETIME = GETDATE(); "
				    
				    + "/* 3. Update ข้อมูลเดิมที่มี ProductionOrder ตรงกัน */ "
				    + "UPDATE target SET "
				    + "    target.LotNo = src.LotNo, "
				    + "    target.DataStatus = src.DataStatus, "
				    + "    target.ChangeDate = @Now, "
				    + "    target.SyncDate = src.SyncDate "
				    + "FROM [FromSapReceipe] AS target "
				    + "INNER JOIN #TempReceipe AS src ON target.ProductionOrder = src.ProductionOrder; "

				    + "/* 4. Insert ข้อมูลใหม่ที่ยังไม่มี ProductionOrder */ "
				    + "INSERT INTO [FromSapReceipe] (ProductionOrder, LotNo, DataStatus, ChangeDate, CreateDate, SyncDate) "
				    + "SELECT "
				    + "    src.ProductionOrder, src.LotNo, src.DataStatus, @Now, @Now, src.SyncDate "
				    + "FROM #TempReceipe AS src "
				    + "LEFT JOIN [FromSapReceipe] AS target ON target.ProductionOrder = src.ProductionOrder "
				    + "WHERE target.ProductionOrder IS NULL "
				    + "  AND src.ProductionOrder IS NOT NULL AND src.ProductionOrder <> '';";

				stmt.execute(upsertSql);
//				// 3. Update ข้อมูลเดิมที่มี ProductionOrder ตรงกัน
//				stmt.execute("UPDATE target SET "
//						+ "target.LotNo = src.LotNo, "
//						+ "target.DataStatus = src.DataStatus, "
//						+ "target.ChangeDate = GETDATE(), "
//						+ "target.SyncDate = src.SyncDate "
//						+ "FROM [FromSapReceipe] AS target "
//						+ "INNER JOIN #TempReceipe AS src ON target.ProductionOrder = src.ProductionOrder");
//
//				// 4. Insert ข้อมูลใหม่ที่ยังไม่มี ProductionOrder
//				stmt.execute(
//						"INSERT INTO [FromSapReceipe] (ProductionOrder, LotNo, DataStatus, ChangeDate, CreateDate, SyncDate) "
//								+ "SELECT src.ProductionOrder, src.LotNo, src.DataStatus, GETDATE(), GETDATE(), src.SyncDate "
//								+ "FROM #TempReceipe AS src "
//								+ "LEFT JOIN [FromSapReceipe] AS target ON target.ProductionOrder = src.ProductionOrder "
//								+ "WHERE target.ProductionOrder IS NULL "
//								+ "AND src.ProductionOrder IS NOT NULL AND src.ProductionOrder <> ''");

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
//	public String upsertFromSapReceipeDetail(ArrayList<FromErpReceipeDetail> paList)
//	{
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//
//		String iconStatus = "I";
//		String sql = ""
//				+ "-- Update if the record exists\r\n"
//				+ "UPDATE [dbo].[FromSapReceipe]\r\n"
//				+ "SET\r\n"
//				+ "    [LotNo] = ?,\r\n"
//				+ "    [DataStatus] = ?,\r\n"
//				+ "    [ChangeDate] = ?,\r\n"
//				+ "    [SyncDate] = ?\r\n"
//				+ "WHERE\r\n"
//				+ "    [ProductionOrder] = ?;\r\n"
//				+ "\r\n"
//				+ "-- Check if rows were updated\r\n"
//				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "\r\n"
//				+ "IF @rc = 0\r\n"
//				+ "BEGIN\r\n"
//				+ "    -- Insert if no rows were updated\r\n"
//				+ "    INSERT INTO [dbo].[FromSapReceipe] (\r\n"
//				+ "        [ProductionOrder],\r\n"
//				+ "        [LotNo],\r\n"
//				+ "        [DataStatus],\r\n"
//				+ "        [ChangeDate],\r\n"
//				+ "        [CreateDate],\r\n"
//				+ "        [SyncDate]\r\n"
//				+ "    )\r\n"
//				+ "    VALUES (\r\n"
//				+ "        ?, ?, ?, ?, ?,\r\n"
//				+ "        ?\r\n"
//				+ "    );\r\n"
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
//			for (FromErpReceipeDetail bean : paList) {
//				index = 1;
//				prepared.setString(index ++ , bean.getLotNo());
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.setString(index ++ , bean.getProductionOrder());
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getLotNo());
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//
//				prepared.addBatch();
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
}
