package th.co.wacoal.atech.pcms2.dao.master.implement;

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
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapCFMDao;
import th.co.wacoal.atech.pcms2.entities.CFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapCFMDaoImpl implements FromSapCFMDao {
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
	public FromSapCFMDaoImpl(@Qualifier("pcmsDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
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
		String prodOrderSafe = (prodOrder == null ? "" : prodOrder.replace("'", "''"));
		where += " a.ProductionOrder = '" + prodOrderSafe + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = " SELECT DISTINCT  \r\n"
				+ "   [Id]"
				+ "   ,[ProductionOrder],[CFMNo],[CFMNumber]\r\n"
				+ "   ,[CFMSendDate],[CFMAnswerDate],[CFMStatus]\r\n"
				+ "   ,[CFMRemark],[Da],[Db],[L]\r\n"
				+ "   ,[ST],[SaleOrder]"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ')\r\n"
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
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCFMDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapCFMDetail(ArrayList<FromErpCFMDetail> paList)
	{
		String[] iconStatus = {"I"};
		String systemUser = "SYSTEM"; // หรือดึงจาก session ถ้ามี

		this.jdbc.execute((org.springframework.jdbc.core.ConnectionCallback<Void>) conn -> {
			try {
				conn.setAutoCommit(false);

				try (Statement stmt = conn.createStatement()) {
					stmt.setQueryTimeout(300);
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
						ps.setQueryTimeout(300);
						for (FromErpCFMDetail bean : paList) {
							int idx = 1;
							ps.setString(idx ++ , bean.getProductionOrder());
							ps.setString(idx ++ , bean.getCfmNo());
							ps.setString(idx ++ , bean.getCfmNumber());
							sshUtl.setSqlDate(ps, bean.getCfmSendDate(), idx ++ );
							sshUtl.setSqlDate(ps, bean.getCfmAnswerDate(), idx ++ );
							ps.setString(idx ++ , bean.getCfmStatus());
							ps.setString(idx ++ , bean.getCfmRemark());
							ps.setString(idx ++ , bean.getSaleOrder());
							ps.setString(idx ++ , bean.getSaleLine());
							ps.setString(idx ++ , bean.getNextLot());
							ps.setString(idx ++ , bean.getSoChange());
							sshUtl.setSqlBigDecimal(ps, bean.getSoChangeQty(), idx ++ );
							ps.setString(idx ++ , bean.getSoChangeUnit());
							ps.setString(idx ++ , bean.getRollNo());
							ps.setString(idx ++ , bean.getRollNoRemark());
							ps.setString(idx ++ , bean.getDataStatus());
							sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx ++ );
							ps.addBatch();
						}
						ps.executeBatch();
					}
					// รวมข้อ 3, 4, 5 เป็น Batch เดียวกันเพื่อประกาศตัวแปรคุมเวลาให้เท่ากัน 100%
					String upsertSql =
					      "SET XACT_ABORT ON; SET DEADLOCK_PRIORITY LOW; "
					    + "DECLARE @Now DATETIME = GETDATE(); "

					    + "/* 3. จัดการ DataStatus = 'X' */ "
					    + "UPDATE target SET "
					    + "    target.DataStatus = 'X', "
					    + "    target.ChangeDate = @Now, "
					    + "    target.ChangeBy = '" + systemUser + "' "
					    + "FROM [FromSapCFM] AS target "
					    + "INNER JOIN #TempCFM AS src ON target.ProductionOrder = src.ProductionOrder "
					    + "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'; "

					    + "/* 4. Update ข้อมูลเดิม */ "
					    + "UPDATE target SET "
					    + "    target.CFMNumber = src.CFMNumber, target.CFMSendDate = src.CFMSendDate, "
					    + "    target.CFMAnswerDate = src.CFMAnswerDate, target.CFMStatus = src.CFMStatus, "
					    + "    target.CFMRemark = src.CFMRemark, target.SaleOrder = src.SaleOrder, "
					    + "    target.SaleLine = src.SaleLine, target.NextLot = src.NextLot, "
					    + "    target.SOChange = src.SOChange, target.SOChangeQty = src.SOChangeQty, "
					    + "    target.SOChangeUnit = src.SOChangeUnit, target.RollNo = src.RollNo, "
					    + "    target.RollNoRemark = src.RollNoRemark, target.DataStatus = src.DataStatus, "
					    + "    target.ChangeDate = @Now, "
					    + "    target.ChangeBy = '" + systemUser + "', "
					    + "    target.SyncDate = src.SyncDate "
					    + "FROM [FromSapCFM] AS target "
					    + "INNER JOIN #TempCFM AS src ON target.ProductionOrder = src.ProductionOrder AND target.CFMNo = src.CFMNo "
					    + "WHERE src.DataStatus <> 'X'; "

					    + "/* 5. Insert ข้อมูลใหม่ */ "
					    + "INSERT INTO [FromSapCFM] (ProductionOrder, CFMNo, CFMNumber, CFMSendDate, CFMAnswerDate, "
					    + "    CFMStatus, CFMRemark, SaleOrder, SaleLine, NextLot, SOChange, SOChangeQty, SOChangeUnit, "
					    + "    RollNo, RollNoRemark, DataStatus, ChangeDate, ChangeBy, CreateDate, CreateBy, SyncDate) "
					    + "SELECT "
					    + "    src.ProductionOrder, src.CFMNo, src.CFMNumber, src.CFMSendDate, src.CFMAnswerDate, "
					    + "    src.CFMStatus, src.CFMRemark, src.SaleOrder, src.SaleLine, src.NextLot, src.SOChange, src.SOChangeQty, "
					    + "    src.SOChangeUnit, src.RollNo, src.RollNoRemark, src.DataStatus, @Now, '" + systemUser + "', @Now, '" + systemUser + "', src.SyncDate "
					    + "FROM #TempCFM AS src "
					    + "LEFT JOIN [FromSapCFM] AS target ON target.ProductionOrder = src.ProductionOrder AND target.CFMNo = src.CFMNo "
					    + "WHERE target.ProductionOrder IS NULL "
					    + "  AND src.DataStatus <> 'X' "
					    + "  AND src.CFMNo IS NOT NULL AND src.CFMNo <> '';";

					stmt.execute(upsertSql);
//				stmt.execute(insertNew);

					conn.commit();
				} catch (Exception e) {
					conn.rollback();
					throw e;
				}
				finally {
				    // ✅ ปิด transaction เสมอ ไม่ว่าจะ success หรือ error
				    try (java.sql.Statement cleanup = conn.createStatement()) {
				        cleanup.execute("IF OBJECT_ID('tempdb..#TempCFM') IS NOT NULL DROP TABLE #TempCFM");
				    } catch (Exception ignored) {}
				    try {
				        conn.setAutoCommit(true);
				    } catch (Exception e) {
				        e.printStackTrace();
				    }
				}
			} catch (Exception e) {
				log.error("[ERP-sync] upsertFromSapCFMDetail failed", e);
				iconStatus[0] = "E";
			}
			return null;
		});

		return iconStatus[0];
	}
}
