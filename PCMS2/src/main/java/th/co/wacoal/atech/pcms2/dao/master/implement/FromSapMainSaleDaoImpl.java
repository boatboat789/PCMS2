package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainSaleDao;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.SaleOrderLogDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.MapperUtility;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapMainSaleDaoImpl implements FromSapMainSaleDao {
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
	public FromSapMainSaleDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	private String selectForLog = ""
			+ " [SaleOrder]\r\n"
			+ "      ,[SaleLine]\r\n"
			+ "      ,[Division]\r\n"
			+ "      ,[MaterialNo]\r\n"
			+ "      ,[ArticleFG]\r\n"
			+ "      ,[DesignFG] \r\n"
			+ "      ,[Color]\r\n"
			+ "      ,[DistChannel]\r\n"
			+ "      ,[CustomerName]\r\n"
			+ "      ,[CustomerShortName]\r\n"
			+ "      ,[ColorCustomer] \r\n"
			+ "	  , [SaleCreateDate] \r\n"
			+ "	  , [PlanGreigeDate] \r\n"
			+ "	  , [DueDate]\r\n"
			+ "	  , [CustomerDue]      \r\n"
			+ "      ,[SaleQuantity]\r\n"
			+ "      ,[SaleUnit]\r\n"
			+ "      ,[OrderAmount]\r\n"
			+ "      ,[RemainQuantity]\r\n"
			+ "      ,[RemainAmount]\r\n"
			+ "      ,[PurchaseOrder]\r\n"
			+ "      ,[CustomerNo]\r\n"
			+ "      ,[CustomerMaterial]\r\n"
			+ "      ,[SaleOrg]\r\n"
			+ "      ,[SaleStatus] \r\n"
			+ "      ,[SaleFullName]\r\n"
			+ "      ,[DeliveryStatus]\r\n"
			+ "      ,[SyncDate]\r\n"
			+ "      ,CAST ( NULL AS DateTime ) [SyncDateHeader]\r\n";

	@Override
	public ArrayList<SaleOrderLogDetail> getFromSapMainSaleDetailWithRangeOfChangeDate(String startLogDate, String endLogDate,
			String saleOrder)
	{
		ArrayList<SaleOrderLogDetail> list = null;
		String where = " WHERE 1 = 1 and ( DataStatus = 'O' )   ";
		if ( ! startLogDate.equals("")) {
//			String[] array = startLogDate.split(" - ");
			where += " "
					+ " and (  "
					+ "	CAST(a.[ChangeDate] AS DATE) >= convert(date,'"
					+ startLogDate
					+ "', 103) AND \r\n"
					+ "	CAST(a.[ChangeDate] AS DATE) <= convert(date,'"
					+ endLogDate
					+ "', 103) \r\n"
					+ "	) \r\n";
//			where += " CAST(a.[CreateDate] AS DATE) = convert(date, '"+createDate+"', 103)  \r\n" ;
		}
		if ( ! saleOrder.equals("")) {
			where += " " + " and (  " + " a.[SaleOrder] = '" + saleOrder + "' \r\n" + "	) \r\n";
		}
		String sql = " " + " SELECT DISTINCT \r\n" + this.selectForLog + " FROM [FromSapMainSale] a\r\n" + where;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(MapperUtility.mapToObject(map, SaleOrderLogDetail.class));
//			list.add(this.bcModel._genProductionOrderLogDetail(map));

		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getDivisionDetail()
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		String sql = "SELECT distinct \r\n"
				+ "		[Division] \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n"
				+ " where Division <> '' and "
				+ "		  [DataStatus] = 'O'  "
				+ " order by [Division] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerNameDetail()
	{
		ArrayList<PCMSAllDetail> list = null;
		String sql = " SELECT distinct \r\n"
				+ "		[CustomerName] \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n "
				+ "	WHERE [DataStatus] = 'O'  "
				+ " order by [CustomerName] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerNameDetail(ArrayList<ConfigCustomerUserDetail> poList)
	{
		ArrayList<PCMSAllDetail> list = null;
		ConfigCustomerUserDetail bean = poList.get(0);
		String custNo = bean.getCustomerNo();
		String where = " where 1 = 1 AND DataStatus = 'O' AND (";
		String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i ++ ) {
			where += " [CustomerNo] = '" + array[i] + "' ";
			if (i != array.length-1) {
				where += " or \r\n";
			}
		}
		where += " ) \r\n";
		String sql = " SELECT distinct \r\n"
				+ "		[CustomerName] \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n "
				+ where
				+ " order by [CustomerName] \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail(ArrayList<ConfigCustomerUserDetail> poList)
	{
		ArrayList<PCMSAllDetail> list = null;
		ConfigCustomerUserDetail bean = poList.get(0);
		String custNo = bean.getCustomerNo();
		String where = " where 1 = 1 AND DataStatus = 'O' AND ( [CustomerNo] IN ( ";
		String[] array = custNo.split(",");
		for (int i = 0; i < array.length; i ++ ) {
			where += " '" + array[i] + "' ";
			if (i != array.length-1) {
				where += " , \r\n";
			}
		}
		where += " ) \r\n";
		where += " ) \r\n";
		String sql = " SELECT distinct \r\n"
				+ "		[CustomerShortName]  \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n "
				+ where
				+ " order by  [CustomerShortName] \r\n";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail()
	{
		ArrayList<PCMSAllDetail> list = null;
		String sql = "SELECT distinct \r\n"
				+ "		[CustomerShortName]  \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n"
				+ " where DataStatus = 'O'  "
				+ " order by  [CustomerShortName] \r\n";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSTableDetail> getSaleNumberDetail()
	{
		ArrayList<PCMSTableDetail> list = null;
		String sql = " SELECT DISTINCT \r\n "
				+ "	   a.SaleNumber\r\n"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n"
				+ "    		 WHEN 0 THEN ''   \r\n"
				+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+[SaleFullName]\r\n"
				+ "    		 END AS [SaleFullName]   "
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n "
				+ " where SaleNumber <> '00000000' \r\n"
				+ " AND DataStatus = 'O'  "
				+ " Order by [SaleNumber]";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapMainSaleDetail(ArrayList<FromErpMainSaleDetail> paList) {
	    String iconStatus = "I";
	    // Java 8: ใช้ try-with-resources เพื่อปิด connection อัตโนมัติ
		Connection conn = this.database.getConnection();
		PreparedStatement prepared = null;

		try {
	        conn.setAutoCommit(false); // เริ่ม Transaction

	        try (Statement stmt = conn.createStatement()) {
	        	stmt.execute("IF OBJECT_ID('tempdb..#TempMainSale') IS NOT NULL DROP TABLE #TempMainSale");
	            // 1. สร้าง Temp Table (กำหนด Data Type และ Collation ให้เป๊ะตาม Schema)
	            stmt.execute("CREATE TABLE #TempMainSale (" +
	                         "SaleOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "SaleLine VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "MaterialNo VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "DueDate DATE, PlanGreigeDate DATE, " +
	                         "SaleUnit VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "SaleQuantity DECIMAL(13,3), " +
	                         "CustomerMaterial VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "Color VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "CustomerNo VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "PurchaseOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "SaleOrg VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "DistChannel VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "Division VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "CustomerName VARCHAR(200) COLLATE DATABASE_DEFAULT, " +
	                         "CustomerShortName VARCHAR(200) COLLATE DATABASE_DEFAULT, " +
	                         "ColorCustomer VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "CustomerDue VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "RemainQuantity DECIMAL(13,3), ShipDate DATE, " +
	                         "SaleStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, " +
	                         "Currency VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "Price DECIMAL(13,3), OrderAmount DECIMAL(13,3), " +
	                         "RemainAmount DECIMAL(13,3), SaleCreateDate DATE, " +
	                         "SaleNumber VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "SaleFullName VARCHAR(100) COLLATE DATABASE_DEFAULT, " +
	                         "DeliveryStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, " +
	                         "DesignFG VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "ArticleFG VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "OrderSheetPrintDate DATE, " +
	                         "CustomerMaterialBase VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	                         "DataStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, " +
	                         "SyncDate DATETIME)");

	            // 2. Prepare Statement สำหรับ Insert ลง Temp Table
	            String insertTempSql = "INSERT INTO #TempMainSale VALUES (" + String.join(",", java.util.Collections.nCopies(35, "?")) + ")";
	            try (PreparedStatement ps = conn.prepareStatement(insertTempSql)) {
	                for (FromErpMainSaleDetail bean : paList) {
	                    int idx = 1;
	                    ps.setString(idx++, bean.getSaleOrder());
	                    ps.setString(idx++, bean.getSaleLine());
	                    ps.setString(idx++, bean.getMaterialNo());
	                    this.sshUtl.setSqlDate(ps, bean.getDueDate(), idx++);
	                    this.sshUtl.setSqlDate(ps, bean.getPlanGreigeDate(), idx++);
	                    ps.setString(idx++, bean.getSaleUnit());
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getSaleQuantity(), idx++);
	                    ps.setString(idx++, bean.getCustomerMaterial());
	                    ps.setString(idx++, bean.getColor());
	                    ps.setString(idx++, bean.getCustomerNo());
	                    ps.setString(idx++, bean.getPurchaseOrder());
	                    ps.setString(idx++, bean.getSaleOrg());
	                    ps.setString(idx++, bean.getDistChannel());
	                    ps.setString(idx++, bean.getDivision());
	                    ps.setString(idx++, bean.getCustomerName());
	                    ps.setString(idx++, bean.getCustomerShortName());
	                    ps.setString(idx++, bean.getColorCustomer());
	                    ps.setString(idx++, bean.getCustomerDue());
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getRemainQuantity(), idx++);
	                    this.sshUtl.setSqlDate(ps, bean.getShipDate(), idx++);
	                    ps.setString(idx++, bean.getSaleStatus());
	                    ps.setString(idx++, bean.getCurrency());
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getPrice(), idx++);
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getOrderAmount(), idx++);
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getRemainAmount(), idx++);
	                    this.sshUtl.setSqlDate(ps, bean.getSaleCreateDate(), idx++);
	                    ps.setString(idx++, bean.getSaleNumber());
	                    ps.setString(idx++, bean.getSaleFullName());
	                    ps.setString(idx++, bean.getDeliveryStatus());
	                    ps.setString(idx++, bean.getDesignFG());
	                    ps.setString(idx++, bean.getArticleFG());
	                    this.sshUtl.setSqlDate(ps, bean.getOrderSheetPrintDate(), idx++);
	                    ps.setString(idx++, bean.getCustomerMaterialBase());
	                    ps.setString(idx++, bean.getDataStatus());
	                    this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx++);
	                    ps.addBatch();
	                }
	                ps.executeBatch();
	            }

	            // 3. จัดการเคส DataStatus = 'X' (ปิดงานทั้ง SaleOrder)
	            stmt.execute("UPDATE target SET target.DataStatus = 'X', target.ChangeDate = GETDATE() " +
	                         "FROM [FromSapMainSale] AS target " +
	                         "INNER JOIN #TempMainSale AS src ON target.SaleOrder = src.SaleOrder " +
	                         "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'");

	            // 4. Update ข้อมูลเดิม (Matching SaleOrder + SaleLine)
	            stmt.execute("UPDATE target SET " +
	                         "target.MaterialNo = src.MaterialNo, target.DueDate = src.DueDate, target.PlanGreigeDate = src.PlanGreigeDate, " +
	                         "target.SaleUnit = src.SaleUnit, target.SaleQuantity = src.SaleQuantity, target.CustomerMaterial = src.CustomerMaterial, " +
	                         "target.Color = src.Color, target.CustomerNo = src.CustomerNo, target.PurchaseOrder = src.PurchaseOrder, " +
	                         "target.SaleOrg = src.SaleOrg, target.DistChannel = src.DistChannel, target.Division = src.Division, " +
	                         "target.CustomerName = src.CustomerName, target.CustomerShortName = src.CustomerShortName, " +
	                         "target.ColorCustomer = src.ColorCustomer, target.CustomerDue = src.CustomerDue, target.RemainQuantity = src.RemainQuantity, " +
	                         "target.ShipDate = src.ShipDate, target.SaleStatus = src.SaleStatus, target.Currency = src.Currency, " +
	                         "target.Price = src.Price, target.OrderAmount = src.OrderAmount, target.RemainAmount = src.RemainAmount, " +
	                         "target.SaleCreateDate = src.SaleCreateDate, target.SaleNumber = src.SaleNumber, target.SaleFullName = src.SaleFullName, " +
	                         "target.DeliveryStatus = src.DeliveryStatus, target.DesignFG = src.DesignFG, target.ArticleFG = src.ArticleFG, " +
	                         "target.OrderSheetPrintDate = src.OrderSheetPrintDate, target.CustomerMaterialBase = src.CustomerMaterialBase, " +
	                         "target.ChangeDate = GETDATE(), target.DataStatus = src.DataStatus, target.SyncDate = src.SyncDate " +
	                         "FROM [FromSapMainSale] AS target " +
	                         "INNER JOIN #TempMainSale AS src ON target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine " +
	                         "WHERE src.DataStatus <> 'X'");

	            // 5. Insert ข้อมูลใหม่ (เฉพาะตัวที่ไม่มีในตารางหลัก)
	            stmt.execute("INSERT INTO [FromSapMainSale] (SaleOrder, SaleLine, MaterialNo, DueDate, PlanGreigeDate, SaleUnit, SaleQuantity, " +
	                         "CustomerMaterial, Color, CustomerNo, PurchaseOrder, SaleOrg, DistChannel, Division, CustomerName, CustomerShortName, " +
	                         "ColorCustomer, CustomerDue, RemainQuantity, ShipDate, SaleStatus, Currency, Price, OrderAmount, RemainAmount, " +
	                         "SaleCreateDate, SaleNumber, SaleFullName, DeliveryStatus, DesignFG, ArticleFG, OrderSheetPrintDate, " +
	                         "CustomerMaterialBase, ChangeDate, CreateDate, DataStatus, SyncDate) " +
	                         "SELECT src.SaleOrder, src.SaleLine, src.MaterialNo, src.DueDate, src.PlanGreigeDate, src.SaleUnit, src.SaleQuantity, " +
	                         "src.CustomerMaterial, src.Color, src.CustomerNo, src.PurchaseOrder, src.SaleOrg, src.DistChannel, src.Division, " +
	                         "src.CustomerName, src.CustomerShortName, src.ColorCustomer, src.CustomerDue, src.RemainQuantity, src.ShipDate, " +
	                         "src.SaleStatus, src.Currency, src.Price, src.OrderAmount, src.RemainAmount, src.SaleCreateDate, src.SaleNumber, " +
	                         "src.SaleFullName, src.DeliveryStatus, src.DesignFG, src.ArticleFG, src.OrderSheetPrintDate, src.CustomerMaterialBase, " +
	                         "GETDATE(), GETDATE(), src.DataStatus, src.SyncDate " +
	                         "FROM #TempMainSale AS src " +
	                         "LEFT JOIN [FromSapMainSale] AS target ON target.SaleOrder = src.SaleOrder AND target.SaleLine = src.SaleLine " +
	                         "WHERE target.SaleOrder IS NULL " +
	                         "AND src.DataStatus <> 'X' " +
	                         "AND src.SaleOrder IS NOT NULL AND src.SaleOrder <> ''");

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
//	public String upsertFromSapMainSaleDetail(ArrayList<FromErpMainSaleDetail> paList)
//	{
//
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//
//		String iconStatus = "I";
//		String sql = "-- Update if the record exists\r\n"
//				+ "-- Update if the record exists\r\n"
//				+ "IF ? = 'X'\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapMainSale]\r\n"
//				+ "    SET\r\n"
//				+ "        [DataStatus] = 'X',\r\n"
//				+ "        [ChangeDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [SaleOrder] = ?\r\n"
//				+ "        AND [DataStatus] = 'O';\r\n"
//				+ "END\r\n"
//				+ "ELSE\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapMainSale]\r\n"
//				+ "    SET\r\n"
//				+ "        [MaterialNo] = ?,\r\n"
//				+ "        [DueDate] = ?,\r\n"
//				+ "        [PlanGreigeDate] = ?,\r\n"
//				+ "        [SaleUnit] = ?,\r\n"
//				+ "        [SaleQuantity] = ?,\r\n"
//				+ "        [CustomerMaterial] = ?,\r\n"
//				+ "        [Color] = ?,\r\n"
//				+ "        [CustomerNo] = ?,\r\n"
//				+ "        [PurchaseOrder] = ?,\r\n"
//				+ "        [SaleOrg] = ?,\r\n"
//				+ "        [DistChannel] = ?,\r\n"
//				+ "        [Division] = ?,\r\n"
//				+ "        [CustomerName] = ?,\r\n"
//				+ "        [CustomerShortName] = ?,\r\n"
//				+ "        [ColorCustomer] = ?,\r\n"
//				+ "        [CustomerDue] = ?,\r\n"
//				+ "        [RemainQuantity] = ?,\r\n"
//				+ "        [ShipDate] = ?,\r\n"
//				+ "        [SaleStatus] = ?,\r\n"
//				+ "        [Currency] = ?,\r\n"
//				+ "        [Price] = ?,\r\n"
//				+ "        [OrderAmount] = ?,\r\n"
//				+ "        [RemainAmount] = ?,\r\n"
//				+ "        [SaleCreateDate] = ?,\r\n"
//				+ "        [SaleNumber] = ?,\r\n"
//				+ "        [SaleFullName] = ?,\r\n"
//				+ "        [DeliveryStatus] = ?,\r\n"
//				+ "        [DesignFG] = ?,\r\n"
//				+ "        [ArticleFG] = ?,\r\n"
//				+ "        [OrderSheetPrintDate] = ?,\r\n"
//				+ "        [CustomerMaterialBase] = ?,\r\n"
//				+ "        [ChangeDate] = ?,\r\n"
//				+ "        [DataStatus] = ?,\r\n"
//				+ "        [SyncDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [SaleOrder] = ?\r\n"
//				+ "        AND [SaleLine] = ?;\r\n"
//				+ "\r\n"
//				+ "    -- Check if rows were updated\r\n"
//				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "    \r\n"
//				+ "    IF @rc = 0\r\n"
//				+ "    BEGIN\r\n"
//				+ "        -- Insert if no rows were updated\r\n"
//				+ "        INSERT INTO [dbo].[FromSapMainSale] (\r\n"
//				+ "            [SaleOrder],\r\n"
//				+ "            [SaleLine],\r\n"
//				+ "            [MaterialNo],\r\n"
//				+ "            [DueDate],\r\n"
//				+ "            [PlanGreigeDate],\r\n"
//				+ "            [SaleUnit],\r\n"
//				+ "            [SaleQuantity],\r\n"
//				+ "            [CustomerMaterial],\r\n"
//				+ "            [Color],\r\n"
//				+ "            [CustomerNo],\r\n"
//				+ "            [PurchaseOrder],\r\n"
//				+ "            [SaleOrg],\r\n"
//				+ "            [DistChannel],\r\n"
//				+ "            [Division],\r\n"
//				+ "            [CustomerName],\r\n"
//				+ "            [CustomerShortName],\r\n"
//				+ "            [ColorCustomer],\r\n"
//				+ "            [CustomerDue],\r\n"
//				+ "            [RemainQuantity],\r\n"
//				+ "            [ShipDate],\r\n"
//				+ "            [SaleStatus],\r\n"
//				+ "            [Currency],\r\n"
//				+ "            [Price],\r\n"
//				+ "            [OrderAmount],\r\n"
//				+ "            [RemainAmount],\r\n"
//				+ "            [SaleCreateDate],\r\n"
//				+ "            [SaleNumber],\r\n"
//				+ "            [SaleFullName],\r\n"
//				+ "            [DeliveryStatus],\r\n"
//				+ "            [DesignFG],\r\n"
//				+ "            [ArticleFG],\r\n"
//				+ "            [OrderSheetPrintDate],\r\n"
//				+ "            [CustomerMaterialBase],\r\n"
//				+ "            [ChangeDate],\r\n"
//				+ "            [CreateDate],\r\n"
//				+ "            [DataStatus],\r\n"
//				+ "            [SyncDate]\r\n"
//				+ "        )\r\n"
//				+ "        VALUES (\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?, ?, ?, ?, ?,\r\n"
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
//			for (FromErpMainSaleDetail bean : paList) {
//				index = 1;
//
//				prepared.setString(index ++ , bean.getDataStatus());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getSaleOrder());
//
//				prepared.setString(index ++ , bean.getMaterialNo());
//				this.sshUtl.setSqlDate(prepared, bean.getDueDate(), index ++ );
//				this.sshUtl.setSqlDate(prepared, bean.getPlanGreigeDate(), index ++ );
//				prepared.setString(index ++ , bean.getSaleUnit());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getSaleQuantity(), index ++ );
//				prepared.setString(index ++ , bean.getCustomerMaterial());
//				prepared.setString(index ++ , bean.getColor());
//				prepared.setString(index ++ , bean.getCustomerNo());
//				prepared.setString(index ++ , bean.getPurchaseOrder());
//				prepared.setString(index ++ , bean.getSaleOrg());
//				prepared.setString(index ++ , bean.getDistChannel());
//				prepared.setString(index ++ , bean.getDivision());
//				prepared.setString(index ++ , bean.getCustomerName());
//				prepared.setString(index ++ , bean.getCustomerShortName());
//				prepared.setString(index ++ , bean.getColorCustomer());
//				prepared.setString(index ++ , bean.getCustomerDue());
////this.sshUtl.setSqlDate(prepared, bean.getCustomerDue(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getRemainQuantity(), index ++ );
//				this.sshUtl.setSqlDate(prepared, bean.getShipDate(), index ++ );
//				prepared.setString(index ++ , bean.getSaleStatus());
//				prepared.setString(index ++ , bean.getCurrency());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getPrice(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getOrderAmount(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getRemainAmount(), index ++ );
//				this.sshUtl.setSqlDate(prepared, bean.getSaleCreateDate(), index ++ );
//				prepared.setString(index ++ , bean.getSaleNumber());
//				prepared.setString(index ++ , bean.getSaleFullName());
//				prepared.setString(index ++ , bean.getDeliveryStatus());
//				prepared.setString(index ++ , bean.getDesignFG());
//				prepared.setString(index ++ , bean.getArticleFG());
//				prepared.setString(index ++ , bean.getOrderSheetPrintDate());
//				prepared.setString(index ++ , bean.getCustomerMaterialBase());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getDataStatus());
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//				prepared.setString(index ++ , bean.getMaterialNo());
//				this.sshUtl.setSqlDate(prepared, bean.getDueDate(), index ++ );
//				this.sshUtl.setSqlDate(prepared, bean.getPlanGreigeDate(), index ++ );
//				prepared.setString(index ++ , bean.getSaleUnit());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getSaleQuantity(), index ++ );
//				prepared.setString(index ++ , bean.getCustomerMaterial());
//				prepared.setString(index ++ , bean.getColor());
//				prepared.setString(index ++ , bean.getCustomerNo());
//				prepared.setString(index ++ , bean.getPurchaseOrder());
//				prepared.setString(index ++ , bean.getSaleOrg());
//				prepared.setString(index ++ , bean.getDistChannel());
//				prepared.setString(index ++ , bean.getDivision());
//				prepared.setString(index ++ , bean.getCustomerName());
//				prepared.setString(index ++ , bean.getCustomerShortName());
//				prepared.setString(index ++ , bean.getColorCustomer());
//				prepared.setString(index ++ , bean.getCustomerDue());
////this.sshUtl.setSqlDate(prepared, bean.getCustomerDue(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getRemainQuantity(), index ++ );
//				this.sshUtl.setSqlDate(prepared, bean.getShipDate(), index ++ );
//				prepared.setString(index ++ , bean.getSaleStatus());
//				prepared.setString(index ++ , bean.getCurrency());
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getPrice(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getOrderAmount(), index ++ );
//				this.sshUtl.setSqlBigDecimal(prepared, bean.getRemainAmount(), index ++ );
//				this.sshUtl.setSqlDate(prepared, bean.getSaleCreateDate(), index ++ );
//				prepared.setString(index ++ , bean.getSaleNumber());
//				prepared.setString(index ++ , bean.getSaleFullName());
//				prepared.setString(index ++ , bean.getDeliveryStatus());
//				prepared.setString(index ++ , bean.getDesignFG());
//				prepared.setString(index ++ , bean.getArticleFG());
//				prepared.setString(index ++ , bean.getOrderSheetPrintDate());
//				prepared.setString(index ++ , bean.getCustomerMaterialBase());
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getDataStatus());
//				this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//
//				prepared.addBatch();
//				batchSize ++ ;
//				if (batchSize % 500 == 0) { // Execute batch every 500 records
//					prepared.executeBatch();
//					prepared.clearBatch();
//					batchSize = 0; // Reset batch size
//				}
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
