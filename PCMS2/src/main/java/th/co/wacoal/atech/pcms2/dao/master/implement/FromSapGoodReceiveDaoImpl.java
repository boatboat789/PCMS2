package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapGoodReceiveDao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
//import model.BeanCreateModel;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapGoodReceiveDaoImpl implements FromSapGoodReceiveDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
//	private BeanCreateModel bcModel = new BeanCreateModel();
	private final Logger log = LoggerFactory.getLogger(getClass());
	private JdbcTemplate jdbc;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSapGoodReceiveDaoImpl(@Qualifier("pcmsDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
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

		Connection conn = DataSourceUtils.getConnection(this.jdbc.getDataSource());
		try {
	        conn.setAutoCommit(false);

	        try (Statement stmt = conn.createStatement()) {
	        	stmt.setQueryTimeout(300);
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
	                ps.setQueryTimeout(300);
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
	         // รวมข้อ 3, 4, 5 เป็น Batch เดียวเพื่อประสิทธิภาพและเวลาที่แม่นยำ
	            String upsertSql =
	                  "SET XACT_ABORT ON; SET DEADLOCK_PRIORITY LOW; "
	                + "DECLARE @Now DATETIME = GETDATE(); "

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
	            try (java.sql.Statement cleanup = conn.createStatement()) {
	                cleanup.execute("IF OBJECT_ID('tempdb..#TempGR') IS NOT NULL DROP TABLE #TempGR");
	            } catch (Exception ignored) {}
	            try {
	                conn.setAutoCommit(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    } catch (Exception e) {
	        log.error("[ERP-sync] upsertFromSapGoodReceiveDetail failed", e);
	        iconStatus = "E";
	    } finally {
	        DataSourceUtils.releaseConnection(conn, this.jdbc.getDataSource());
	    }
	    return iconStatus;
	}
}
