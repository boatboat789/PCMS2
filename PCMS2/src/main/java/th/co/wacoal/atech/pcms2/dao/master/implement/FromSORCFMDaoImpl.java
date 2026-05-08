package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSORCFMDao;
import th.co.wacoal.atech.pcms2.entities.SORDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSORCFMDaoImpl implements FromSORCFMDao {

	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	@SuppressWarnings("unused")
	private BeanCreateService bcModel = new BeanCreateService();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSORCFMDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String upSertFromSORCFMDetail(ArrayList<SORDetail> list)
	{
		String iconStatus = "I";
		Connection conn = this.database.getConnection();
//		PreparedStatement prepared = null;

		try {
			conn.setAutoCommit(false); // เริ่ม Transaction

			try (Statement stmt = conn.createStatement()) {
				// 1. สร้าง Temp Table (Mapping ตาม Schema: SaleOrder, SaleLine, CFMDate)
				stmt.execute("IF OBJECT_ID('tempdb..#TempSORCFM') IS NOT NULL DROP TABLE #TempSORCFM");
				 

				stmt.execute("CREATE TABLE #TempSORCFM ("
						+ "SaleOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "SaleLine VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "CFMDate DATE)");

				// 2. Bulk Insert ข้อมูลจาก List ลงใน Temp Table
				String insertTempSql = "INSERT INTO #TempSORCFM VALUES (?, ?, ?)";
				try (PreparedStatement ps = conn.prepareStatement(insertTempSql)) {
					for (SORDetail bean : list) {
						int idx = 1;
						ps.setString(idx ++ , bean.getSaleOrder());
						ps.setString(idx ++ , bean.getSaleLine());
						this.sshUtl.setSqlDate(ps, bean.getCfmDate(), idx ++ );
						ps.addBatch();
					}
					ps.executeBatch();
				}
				// รวมข้อ 3 และ 4 เป็น Batch เดียวเพื่อให้เวลา GETDATE() ตรงกันและทำงานได้เร็วขึ้น
				String upsertSql = 
				      "DECLARE @Now DATETIME = GETDATE(); "
				    
				    + "/* 3. Update ข้อมูลเดิมที่มี SaleOrder และ SaleLine ตรงกัน */ "
				    + "UPDATE target SET "
				    + "    target.CFMDate = src.CFMDate, "
				    + "    target.ChangeDate = @Now "
				    + "FROM [PCMS].[dbo].[FromSORCFM] AS target "
				    + "INNER JOIN #TempSORCFM AS src ON "
				    + "    target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine; "

				    + "/* 4. Insert ข้อมูลใหม่ที่ยังไม่มีในตารางหลัก */ "
				    + "INSERT INTO [PCMS].[dbo].[FromSORCFM] (SaleOrder, SaleLine, CFMDate, ChangeDate, CreateDate) "
				    + "SELECT "
				    + "    src.SaleOrder, src.SaleLine, src.CFMDate, @Now, @Now "
				    + "FROM #TempSORCFM AS src "
				    + "LEFT JOIN [PCMS].[dbo].[FromSORCFM] AS target ON "
				    + "    target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine "
				    + "WHERE target.SaleOrder IS NULL "
				    + "  AND src.SaleOrder IS NOT NULL AND src.SaleOrder <> '';";

				stmt.execute(upsertSql);
//				// 3. Update ข้อมูลเดิมที่มี SaleOrder และ SaleLine ตรงกัน
//				stmt.execute("UPDATE target SET "
//						+ "target.CFMDate = src.CFMDate, "
//						+ "target.ChangeDate = GETDATE() "
//						+ "FROM [PCMS].[dbo].[FromSORCFM] AS target "
//						+ "INNER JOIN #TempSORCFM AS src ON "
//						+ "target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine");
//
//				// 4. Insert ข้อมูลใหม่ที่ยังไม่มีในตารางหลัก
//				stmt.execute("INSERT INTO [PCMS].[dbo].[FromSORCFM] (SaleOrder, SaleLine, CFMDate, ChangeDate, CreateDate) "
//						+ "SELECT src.SaleOrder, src.SaleLine, src.CFMDate, GETDATE(), GETDATE() "
//						+ "FROM #TempSORCFM AS src "
//						+ "LEFT JOIN [PCMS].[dbo].[FromSORCFM] AS target ON "
//						+ "target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine "
//						+ "WHERE target.SaleOrder IS NULL "
//						+ "AND src.SaleOrder IS NOT NULL AND src.SaleOrder <> ''");

				conn.commit(); // ยืนยัน Transaction
			} catch (Exception e) {
				conn.rollback(); // ย้อนกลับหากเกิด Error
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
//	public String upSertFromSORCFMDetail(ArrayList<SORDetail> list)
//	{
//
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//		Timestamp dateTime = new Timestamp(time);
//		String saleLine = "",cfmDate = "";
//		String iconStatus = "I";
//		String sql = "UPDATE [PCMS].[dbo].[FromSORCFM] "
//				+ " SET [CFMDate] = ?\n"
//				+ "     ,[ChangeDate] = ?\n"
//				+ " WHERE [SaleOrder] = ? and [SaleLine]  = ? "
//				+ " declare  @rc int = @@ROWCOUNT "
//				+ "  if @rc = 0 "
//				+ " INSERT INTO [PCMS].[dbo].[FromSORCFM]	 "
//				+ " ([SaleOrder] ,[SaleLine] ,[CFMDate] ,[ChangeDate] )"
//				+ " values(? , ? , ? , ?  )  ;";
//		int i = 0;
//
//		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
//Connection connection = this.database.getConnection();
//PreparedStatement prepared = null;
//
//try {
//    prepared = connection.prepareStatement(sql);
//			for (i = 0; i < list.size(); i ++ ) {
//				SORDetail bean = list.get(i);
//				saleLine = bean.getSaleLine();
//				cfmDate = bean.getCfmDate();
//				int index = 1;
//				this.sshUtl.setSqlDate(prepared, cfmDate, index ++ );
//				prepared.setTimestamp(index ++ , dateTime);
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , saleLine);
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , saleLine);
//				this.sshUtl.setSqlDate(prepared, cfmDate, index ++ );
//				prepared.setTimestamp(index ++ , dateTime);
//				prepared.addBatch();
//			}
//			prepared.executeBatch();
//			prepared.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
////			System.err.println("insertLabNoDetail" + e.getMessage());
//			iconStatus = "E";
//		} finally {
//			// this.database.close();
//		}
//		return iconStatus;
//	}
}
