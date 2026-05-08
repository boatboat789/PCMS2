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

import th.co.wacoal.atech.pcms2.dao.master.FromSapSaleDao;
import th.co.wacoal.atech.pcms2.entities.SaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapSaleDaoImpl implements FromSapSaleDao {
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
	public FromSapSaleDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<SaleDetail> getFromSapSaleDetailByProductionOrder(String prodOrder)
	{
		ArrayList<SaleDetail> list = null;
		String where = " where  ";
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = " SELECT DISTINCT  \r\n"
				+ "   [ProductionOrder],[BillDate]\r\n"
				+ "   ,[BillQtyPerSale],[SaleOrder] \r\n"
				+ "	  ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "			WHEN 0 THEN ''  \r\n"
				+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "			END AS [SaleLine] \r\n"
				+ "   ,[BillQtyPerStock],[Remark],[CustomerNo]\r\n"
				+ "   ,[CustomerName1],[CustomerPO],[DueDate]\r\n"
				+ "   ,[Color],[No],a.[DataStatus]\r\n"
				+ "  "
				+ " from [PCMS].[dbo].[FromSapSale] as a \r\n "
				+ where
				+ " Order by [No]";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSaleDetail(map));
		}
		return list;
	}
	@Override
	public String upsertFromSapSaleDetail(ArrayList<FromErpSaleDetail> paList) {
	    String iconStatus = "I";
		Connection conn = this.database.getConnection();
		PreparedStatement prepared = null;

		try {
	        conn.setAutoCommit(false);

	        try (Statement stmt = conn.createStatement()) {
	        	stmt.execute("IF OBJECT_ID('tempdb..#TempSale') IS NOT NULL DROP TABLE #TempSale");
	        	
	            // 1. สร้าง Temp Table ตาม Schema (Decimal 13,3 และขนาด Varchar ที่กำหนด)
	            stmt.execute("CREATE TABLE #TempSale (" +
	                         "ProductionOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "BillDate DATE, " +
	                         "BillQtyPerSale DECIMAL(13, 3), " +
	                         "SaleOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "SaleLine VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "BillQtyPerStock DECIMAL(13, 3), " +
	                         "Remark VARCHAR(100) COLLATE DATABASE_DEFAULT, " +
	                         "CustomerNo VARCHAR(20) COLLATE DATABASE_DEFAULT, " +
	                         "CustomerName1 VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "CustomerPO VARCHAR(30) COLLATE DATABASE_DEFAULT, " +
	                         "DueDate DATE, " +
	                         "Color VARCHAR(20) COLLATE DATABASE_DEFAULT, " +
	                         "No VARCHAR(3) COLLATE DATABASE_DEFAULT, " +
	                         "DataStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, " +
	                         "SyncDate DATETIME)");

	            // 2. Bulk Insert ลง Temp Table
	            String insertTemp = "INSERT INTO #TempSale VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	            try (PreparedStatement ps = conn.prepareStatement(insertTemp)) {
	                for (FromErpSaleDetail bean : paList) {
	                    int idx = 1;
	                    ps.setString(idx++, bean.getProductionOrder());
	                    this.sshUtl.setSqlDate(ps, bean.getBillDate(), idx++);
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getBillQtyPerSale(), idx++);
	                    ps.setString(idx++, bean.getSaleOrder());
	                    ps.setString(idx++, bean.getSaleLine());
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getBillQtyPerStock(), idx++);
	                    ps.setString(idx++, bean.getRemark());
	                    ps.setString(idx++, bean.getCustomerNo());
	                    ps.setString(idx++, bean.getCustomerName1());
	                    ps.setString(idx++, bean.getCustomErpO()); // จากโค้ดเดิม bean.getCustomErpO() คือ CustomerPO
	                    this.sshUtl.setSqlDate(ps, bean.getDueDate(), idx++);
	                    ps.setString(idx++, bean.getColor());
	                    ps.setString(idx++, bean.getNo());
	                    ps.setString(idx++, bean.getDataStatus());
	                    this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx++);
	                    ps.addBatch();
	                }
	                ps.executeBatch();
	            }

//	            // 3. จัดการ DataStatus = 'X' (ปิดรายการตาม SaleOrder เดิม)
//	            stmt.execute("UPDATE target SET target.DataStatus = 'X', target.ChangeDate = GETDATE() " +
//	                         "FROM [FromSapSale] AS target " +
//	                         "INNER JOIN #TempSale AS src ON target.SaleOrder = src.SaleOrder " +
//	                         "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'");
//
//	            // 4. Update ข้อมูลเดิม (Matching: ProductionOrder + No)
//	            stmt.execute("UPDATE target SET " +
//	                         "target.BillDate = src.BillDate, " +
//	                         "target.BillQtyPerSale = src.BillQtyPerSale, " +
//	                         "target.SaleOrder = src.SaleOrder, " +
//	                         "target.SaleLine = src.SaleLine, " +
//	                         "target.BillQtyPerStock = src.BillQtyPerStock, " +
//	                         "target.Remark = src.Remark, " +
//	                         "target.CustomerNo = src.CustomerNo, " +
//	                         "target.CustomerName1 = src.CustomerName1, " +
//	                         "target.CustomerPO = src.CustomerPO, " +
//	                         "target.DueDate = src.DueDate, " +
//	                         "target.Color = src.Color, " +
//	                         "target.DataStatus = src.DataStatus, " +
//	                         "target.ChangeDate = GETDATE(), " +
//	                         "target.SyncDate = src.SyncDate " +
//	                         "FROM [FromSapSale] AS target " +
//	                         "INNER JOIN #TempSale AS src ON target.ProductionOrder = src.ProductionOrder AND target.No = src.No " +
//	                         "WHERE src.DataStatus <> 'X'");
//
//	            // 5. Insert ข้อมูลใหม่
//	            stmt.execute("INSERT INTO [FromSapSale] (ProductionOrder, BillDate, BillQtyPerSale, SaleOrder, SaleLine, " +
//	                         "BillQtyPerStock, Remark, CustomerNo, CustomerName1, CustomerPO, DueDate, Color, No, " +
//	                         "DataStatus, ChangeDate, CreateDate, SyncDate) " +
//	                         "SELECT src.ProductionOrder, src.BillDate, src.BillQtyPerSale, src.SaleOrder, src.SaleLine, " +
//	                         "src.BillQtyPerStock, src.Remark, src.CustomerNo, src.CustomerName1, src.CustomerPO, src.DueDate, " +
//	                         "src.Color, src.No, src.DataStatus, GETDATE(), GETDATE(), src.SyncDate " +
//	                         "FROM #TempSale AS src " +
//	                         "LEFT JOIN [FromSapSale] AS target ON target.ProductionOrder = src.ProductionOrder AND target.No = src.No " +
//	                         "WHERE target.ProductionOrder IS NULL AND src.DataStatus <> 'X'");
//
	         // รวมข้อ 3, 4, 5 เป็น Batch เดียวเพื่อคุมเวลา GETDATE() ให้เท่ากัน และลดภาระของ Database
	            String upsertSql = 
	                  "DECLARE @Now DATETIME = GETDATE(); "
	                
	                + "/* 3. จัดการ DataStatus = 'X' (ปิดรายการตาม SaleOrder เดิม) */ "
	                + "UPDATE target SET "
	                + "    target.DataStatus = 'X', "
	                + "    target.ChangeDate = @Now "
	                + "FROM [FromSapSale] AS target "
	                + "INNER JOIN #TempSale AS src ON target.SaleOrder = src.SaleOrder "
	                + "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'; "

	                + "/* 4. Update ข้อมูลเดิม (Matching: ProductionOrder + No) */ "
	                + "UPDATE target SET "
	                + "    target.BillDate = src.BillDate, "
	                + "    target.BillQtyPerSale = src.BillQtyPerSale, "
	                + "    target.SaleOrder = src.SaleOrder, "
	                + "    target.SaleLine = src.SaleLine, "
	                + "    target.BillQtyPerStock = src.BillQtyPerStock, "
	                + "    target.Remark = src.Remark, "
	                + "    target.CustomerNo = src.CustomerNo, "
	                + "    target.CustomerName1 = src.CustomerName1, "
	                + "    target.CustomerPO = src.CustomerPO, "
	                + "    target.DueDate = src.DueDate, "
	                + "    target.Color = src.Color, "
	                + "    target.DataStatus = src.DataStatus, "
	                + "    target.ChangeDate = @Now, "
	                + "    target.SyncDate = src.SyncDate "
	                + "FROM [FromSapSale] AS target "
	                + "INNER JOIN #TempSale AS src ON target.ProductionOrder = src.ProductionOrder AND target.No = src.No "
	                + "WHERE src.DataStatus <> 'X'; "

	                + "/* 5. Insert ข้อมูลใหม่ */ "
	                + "INSERT INTO [FromSapSale] ( "
	                + "    ProductionOrder, BillDate, BillQtyPerSale, SaleOrder, SaleLine, "
	                + "    BillQtyPerStock, Remark, CustomerNo, CustomerName1, CustomerPO, DueDate, Color, No, "
	                + "    DataStatus, ChangeDate, CreateDate, SyncDate) "
	                + "SELECT "
	                + "    src.ProductionOrder, src.BillDate, src.BillQtyPerSale, src.SaleOrder, src.SaleLine, "
	                + "    src.BillQtyPerStock, src.Remark, src.CustomerNo, src.CustomerName1, src.CustomerPO, src.DueDate, "
	                + "    src.Color, src.No, src.DataStatus, @Now, @Now, src.SyncDate "
	                + "FROM #TempSale AS src "
	                + "LEFT JOIN [FromSapSale] AS target ON target.ProductionOrder = src.ProductionOrder AND target.No = src.No "
	                + "WHERE target.ProductionOrder IS NULL AND src.DataStatus <> 'X';";

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

finally {
    // ✅ ปิด transaction เสมอ ไม่ว่าจะ success หรือ error
    try {
        conn.setAutoCommit(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
	    return iconStatus;
	}
//	@Override
//	public String upsertFromSapSaleDetail(ArrayList<FromErpSaleDetail> paList)
//	{
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//
//		String iconStatus = "I";
//		String sql = "-- Update if the record exists\r\n"
//				+ "IF ? = 'X'\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapSale]\r\n"
//				+ "    SET\r\n"
//				+ "        [DataStatus] = 'X',\r\n"
//				+ "        [ChangeDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [SaleOrder] = ?\r\n"
//				+ "        AND [DataStatus] = 'O';\r\n"
//				+ "END\r\n"
//				+ "ELSE\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapSale]\r\n"
//				+ "    SET\r\n"
//				+ "        [BillDate] = ?,\r\n"
//				+ "        [BillQtyPerSale] = ?,\r\n"
//				+ "        [SaleOrder] = ?,\r\n"
//				+ "        [SaleLine] = ?,\r\n"
//				+ "        [BillQtyPerStock] = ?,\r\n"
//				+ "        [Remark] = ?,\r\n"
//				+ "        [CustomerNo] = ?,\r\n"
//				+ "        [CustomerName1] = ?,\r\n"
//				+ "        [CustomerPO] = ?,\r\n"
//				+ "        [DueDate] = ?,\r\n"
//				+ "        [Color] = ?,\r\n"
//				+ "        [DataStatus] = ?,\r\n"
//				+ "        [ChangeDate] = ?,\r\n"
//				+ "        [SyncDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ?\r\n"
//				+ "        AND [No] = ?;\r\n"
//				+ "    \r\n"
//				+ "    -- Check if rows were updated\r\n"
//				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "\r\n"
//				+ "    IF @rc = 0\r\n"
//				+ "    BEGIN\r\n"
//				+ "        -- Insert if no rows were updated\r\n"
//				+ "        INSERT INTO [dbo].[FromSapSale] (\r\n"
//				+ "            [ProductionOrder],\r\n"
//				+ "            [BillDate],\r\n"
//				+ "            [BillQtyPerSale],\r\n"
//				+ "            [SaleOrder],\r\n"
//				+ "            [SaleLine],\r\n"
//				+ "            [BillQtyPerStock],\r\n"
//				+ "            [Remark],\r\n"
//				+ "            [CustomerNo],\r\n"
//				+ "            [CustomerName1],\r\n"
//				+ "            [CustomerPO],\r\n"
//				+ "            [DueDate],\r\n"
//				+ "            [Color],\r\n"
//				+ "            [No],\r\n"
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
//			for (FromErpSaleDetail bean : paList) {
//				index = 1;
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getSaleOrder());
//
//				this.sshUtl.setSqlDate(prepared, bean.getBillDate(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getBillQtyPerSale(), index ++ );
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getBillQtyPerStock(), index ++ );
//				prepared.setString(index ++ , bean.getRemark());
//				prepared.setString(index ++ , bean.getCustomerNo());
//				prepared.setString(index ++ , bean.getCustomerName1());
//				prepared.setString(index ++ , bean.getCustomErpO());
//				this.sshUtl.setSqlDate(prepared, bean.getDueDate(), index ++ );
//				prepared.setString(index ++ , bean.getColor());
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getNo());
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				this.sshUtl.setSqlDate(prepared, bean.getBillDate(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getBillQtyPerSale(), index ++ );
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getBillQtyPerStock(), index ++ );
//				prepared.setString(index ++ , bean.getRemark());
//				prepared.setString(index ++ , bean.getCustomerNo());
//				prepared.setString(index ++ , bean.getCustomerName1());
//				prepared.setString(index ++ , bean.getCustomErpO());
//				this.sshUtl.setSqlDate(prepared, bean.getDueDate(), index ++ );
//				prepared.setString(index ++ , bean.getColor());
//				prepared.setString(index ++ , bean.getNo());
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
////				prepared.setString(index++, bean.get    );
////this.sshUtl.setSqlDate(prepared, bean.get , index++); 
////				prepared.setTimestamp(index++, new Timestamp(time));
////this.sshUtl.setSqlBigDecimal(prepared, bean.get , index++); 
//				prepared.addBatch();
//				batchSize ++ ;
//				if (batchSize % 500 == 0) { // Execute batch every 500 records
//					prepared.executeBatch();
//					prepared.clearBatch();
//					batchSize = 0; // Reset batch size
//				}
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
