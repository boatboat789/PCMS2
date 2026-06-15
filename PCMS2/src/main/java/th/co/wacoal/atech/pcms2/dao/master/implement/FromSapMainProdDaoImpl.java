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

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainProdDao;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.ProductionOrderLogDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.MapperUtility;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapMainProdDaoImpl implements FromSapMainProdDao {
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
	public FromSapMainProdDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	private String selectForLog = ""
			+ " [Id] "
			+ ",[ProductionOrder]\r\n"
			+ ",SaleOrder\r\n"
			+ ",SaleLine\r\n"
			+ ",OrderType\r\n"
			+ ",GreigeInDate\r\n"
			+ ",PrdCreateDate\r\n"
			+ ",GreigeArticle\r\n"
			+ ",GreigeDesign\r\n"
			+ ",ArticleFG\r\n"
			+ ",DesignFG\r\n"
			+ ",TotalQuantity\r\n"
			+ ",Volumn\r\n"
			+ ",Unit\r\n"
			+ ",UserStatus\r\n"
			+ ",LabStatus\r\n"
			+ ",BookNo\r\n"
			+ ",Center\r\n"
			+ ",LotNo\r\n"
			+ ",LabNo\r\n"
			+ ",Shade\r\n"
			+ ",ChangeDate\r\n"
			+ ",SyncDate\r\n";

	@Override
	public ArrayList<ProductionOrderLogDetail> getFromSapMainProdDetailWithRangeOfChangeDate(String startLogDate,
			String endLogDate, String productionOrder)
	{
		ArrayList<ProductionOrderLogDetail> list = null;
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
		if ( ! productionOrder.equals("")) {
			where += " " + " and (  " + " a.[ProductionOrder] = '" + productionOrder + "' \r\n" + "	) \r\n";
		}
		String sql = " " + " SELECT DISTINCT \r\n" + this.selectForLog + " FROM [PCMS].[dbo].[FromSapMainProd] a\r\n" + where;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(MapperUtility.mapToObject(map, ProductionOrderLogDetail.class));
//			list.add(this.bcModel._genProductionOrderLogDetail(map));

		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrder)
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		String sql = ""
				+ "  SELECT DISTINCT"
				+ " * \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainProd] \r\n"
				+ " where \r\n "
				+ " 	ProductionOrder = '"
				+ prdOrder
				+ "'  \r\n"
				+ "		and ( DataStatus = 'O' ) ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getUserStatusDetail()
	{
		ArrayList<PCMSAllDetail> list = null;
		String where = " where UserStatus <> '' \r\n";
		String sql = "SELECT distinct \r\n"
				+ "		[UserStatus] \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainProd] \r\n "
				+ where
				+ " order by UserStatus \r\n";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapMainProdDetail(ArrayList<FromErpMainProdDetail> paList)
	{
		String iconStatus = "I";

		Connection conn = this.database.getConnection();
//		PreparedStatement prepared = null;

		try {
			conn.setAutoCommit(false);

			try (Statement stmt = conn.createStatement()) {
				// 1. สร้าง Temp Table ที่มีพารามิเตอร์ครบตาม Schema (Decimal 13,3)
				stmt.execute("IF OBJECT_ID('tempdb..#TempMainProd') IS NOT NULL DROP TABLE #TempMainProd");
				stmt.execute("CREATE TABLE #TempMainProd ("
						+ "ProductionOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, SaleOrder VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "SaleLine VARCHAR(50) COLLATE DATABASE_DEFAULT, TotalQuantity DECIMAL(13,3), Unit VARCHAR(20) COLLATE DATABASE_DEFAULT, "
						+ "RemAfterCloseOne VARCHAR(200) COLLATE DATABASE_DEFAULT, RemAfterCloseTwo VARCHAR(200) COLLATE DATABASE_DEFAULT, "
						+ "RemAfterCloseThree VARCHAR(200) COLLATE DATABASE_DEFAULT, LabStatus VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "UserStatus VARCHAR(50) COLLATE DATABASE_DEFAULT, DesignFG VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "ArticleFG VARCHAR(50) COLLATE DATABASE_DEFAULT, BookNo VARCHAR(20) COLLATE DATABASE_DEFAULT, "
						+ "Center VARCHAR(20) COLLATE DATABASE_DEFAULT, LotNo VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "Batch VARCHAR(30) COLLATE DATABASE_DEFAULT, LabNo VARCHAR(30) COLLATE DATABASE_DEFAULT, "
						+ "RemarkOne VARCHAR(200) COLLATE DATABASE_DEFAULT, RemarkTwo VARCHAR(200) COLLATE DATABASE_DEFAULT, "
						+ "RemarkThree VARCHAR(200) COLLATE DATABASE_DEFAULT, BCAware VARCHAR(200) COLLATE DATABASE_DEFAULT, "
						+ "OrderPuang VARCHAR(200) COLLATE DATABASE_DEFAULT, RefPrd VARCHAR(200) COLLATE DATABASE_DEFAULT, "
						+ "GreigeInDate DATE, BCDate DATE, Volumn DECIMAL(13,3), CFdate DATE, CFType DATE, "
						+ "Shade VARCHAR(30) COLLATE DATABASE_DEFAULT, LotShipping DATE, BillSendQuantity DECIMAL(13,3), "
						+ "Grade VARCHAR(30) COLLATE DATABASE_DEFAULT, DataStatus VARCHAR(1) COLLATE DATABASE_DEFAULT, "
						+ "PrdCreateDate DATE, GreigeArticle VARCHAR(50) COLLATE DATABASE_DEFAULT, "
						+ "GreigeDesign VARCHAR(50) COLLATE DATABASE_DEFAULT, GreigeMR DECIMAL(13,3), "
						+ "GreigeKG DECIMAL(13,3), OrderType VARCHAR(20) COLLATE DATABASE_DEFAULT, SyncDate DATETIME)");

				// 2. Bulk Insert ลง Temp Table
				String insertTemp =
						"INSERT INTO #TempMainProd VALUES (" + String.join(",", java.util.Collections.nCopies(40, "?")) + ")";
				try (PreparedStatement ps = conn.prepareStatement(insertTemp)) {
					for (FromErpMainProdDetail bean : paList) {
						int idx = 1;
						ps.setString(idx ++ , bean.getProductionOrder());
						ps.setString(idx ++ , bean.getSaleOrder());
						ps.setString(idx ++ , bean.getSaleLine());
						this.sshUtl.setSqlBigDecimal(ps, bean.getTotalQuantity(), idx ++ );
						ps.setString(idx ++ , bean.getUnit());
						ps.setString(idx ++ , bean.getRemAfterCloseOne());
						ps.setString(idx ++ , bean.getRemAfterCloseTwo());
						ps.setString(idx ++ , bean.getRemAfterCloseThree());
						ps.setString(idx ++ , bean.getLabStatus());
						ps.setString(idx ++ , bean.getUserStatus());
						ps.setString(idx ++ , bean.getDesignFG());
						ps.setString(idx ++ , bean.getArticleFG());
						ps.setString(idx ++ , bean.getBookNo());
						ps.setString(idx ++ , bean.getCenter());
						ps.setString(idx ++ , bean.getLotNo());
						ps.setString(idx ++ , bean.getBatch());
						ps.setString(idx ++ , bean.getLabNo());
						ps.setString(idx ++ , bean.getRemarkOne());
						ps.setString(idx ++ , bean.getRemarkTwo());
						ps.setString(idx ++ , bean.getRemarkThree());
						ps.setString(idx ++ , bean.getBcAware());
						ps.setString(idx ++ , bean.getOrderPuang());
						ps.setString(idx ++ , bean.getRefPrd());
						this.sshUtl.setSqlDate(ps, bean.getGreigeInDate(), idx ++ );
						this.sshUtl.setSqlDate(ps, bean.getBcDate(), idx ++ );
						this.sshUtl.setSqlBigDecimal(ps, bean.getVolumn(), idx ++ );
						this.sshUtl.setSqlDate(ps, bean.getCfDate(), idx ++ );
						this.sshUtl.setSqlDate(ps, bean.getCfType(), idx ++ );
						ps.setString(idx ++ , bean.getShade());
						this.sshUtl.setSqlDate(ps, bean.getLotShipping(), idx ++ );
						this.sshUtl.setSqlBigDecimal(ps, bean.getBillSendQuantity(), idx ++ );
						ps.setString(idx ++ , bean.getGrade());
						ps.setString(idx ++ , bean.getDataStatus());
						this.sshUtl.setSqlDate(ps, bean.getPrdCreateDate(), idx ++ );
						ps.setString(idx ++ , bean.getGreigeArticle());
						ps.setString(idx ++ , bean.getGreigeDesign());
						this.sshUtl.setSqlBigDecimal(ps, bean.getGreigeMR(), idx ++ );
						this.sshUtl.setSqlBigDecimal(ps, bean.getGreigeKG(), idx ++ );
						ps.setString(idx ++ , bean.getOrderType());
						this.sshUtl.setSqlTimeStamp(ps, bean.getSyncDate(), idx ++ );
						ps.addBatch();
					}
					ps.executeBatch();
				}

//				// 3. จัดการ DataStatus = 'X'
//				stmt.execute("UPDATE target SET target.DataStatus = 'X', target.ChangeDate = GETDATE() "
//						+ "FROM [FromSapMainProd] AS target INNER JOIN #TempMainProd AS src ON target.ProductionOrder = src.ProductionOrder "
//						+ "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'");
//
//				// 4. Update ข้อมูลเดิม (Matching ProductionOrder)
//				stmt.execute("UPDATE target SET "
//						+ "target.SaleOrder = src.SaleOrder, target.SaleLine = src.SaleLine, target.TotalQuantity = src.TotalQuantity, "
//						+ "target.Unit = src.Unit, target.RemAfterCloseOne = src.RemAfterCloseOne, target.RemAfterCloseTwo = src.RemAfterCloseTwo, "
//						+ "target.RemAfterCloseThree = src.RemAfterCloseThree, target.LabStatus = src.LabStatus, target.UserStatus = src.UserStatus, "
//						+ "target.DesignFG = src.DesignFG, target.ArticleFG = src.ArticleFG, target.BookNo = src.BookNo, target.Center = src.Center, "
//						+ "target.LotNo = src.LotNo, target.Batch = src.Batch, target.LabNo = src.LabNo, target.RemarkOne = src.RemarkOne, "
//						+ "target.RemarkTwo = src.RemarkTwo, target.RemarkThree = src.RemarkThree, target.BCAware = src.BCAware, "
//						+ "target.OrderPuang = src.OrderPuang, target.RefPrd = src.RefPrd, target.GreigeInDate = src.GreigeInDate, "
//						+ "target.BCDate = src.BCDate, target.Volumn = src.Volumn, target.CFdate = src.CFdate, target.CFType = src.CFType, "
//						+ "target.Shade = src.Shade, target.LotShipping = src.LotShipping, target.BillSendQuantity = src.BillSendQuantity, "
//						+ "target.Grade = src.Grade, target.DataStatus = src.DataStatus, target.PrdCreateDate = src.PrdCreateDate, "
//						+ "target.GreigeArticle = src.GreigeArticle, target.GreigeDesign = src.GreigeDesign, target.GreigeMR = src.GreigeMR, "
//						+ "target.GreigeKG = src.GreigeKG, target.OrderType = src.OrderType, target.SyncDate = src.SyncDate, target.ChangeDate = GETDATE() "
//						+ "FROM [FromSapMainProd] AS target INNER JOIN #TempMainProd AS src ON target.ProductionOrder = src.ProductionOrder "
//						+ "WHERE src.DataStatus <> 'X'");
//
//				// 5. Insert ข้อมูลใหม่
//				stmt.execute("INSERT INTO [FromSapMainProd] (ProductionOrder, SaleOrder, SaleLine, TotalQuantity, Unit, "
//						+ "RemAfterCloseOne, RemAfterCloseTwo, RemAfterCloseThree, LabStatus, UserStatus, DesignFG, ArticleFG, "
//						+ "BookNo, Center, LotNo, Batch, LabNo, RemarkOne, RemarkTwo, RemarkThree, BCAware, OrderPuang, RefPrd, "
//						+ "GreigeInDate, BCDate, Volumn, CFdate, CFType, Shade, LotShipping, BillSendQuantity, Grade, DataStatus, "
//						+ "PrdCreateDate, GreigeArticle, GreigeDesign, GreigeMR, GreigeKG, OrderType, SyncDate, ChangeDate, CreateDate) "
//						+ "SELECT src.ProductionOrder, src.SaleOrder, src.SaleLine, src.TotalQuantity, src.Unit, "
//						+ "src.RemAfterCloseOne, src.RemAfterCloseTwo, src.RemAfterCloseThree, src.LabStatus, src.UserStatus, src.DesignFG, src.ArticleFG, "
//						+ "src.BookNo, src.Center, src.LotNo, src.Batch, src.LabNo, src.RemarkOne, src.RemarkTwo, src.RemarkThree, src.BCAware, src.OrderPuang, src.RefPrd, "
//						+ "src.GreigeInDate, src.BCDate, src.Volumn, src.CFdate, src.CFType, src.Shade, src.LotShipping, src.BillSendQuantity, src.Grade, src.DataStatus, "
//						+ "src.PrdCreateDate, src.GreigeArticle, src.GreigeDesign, src.GreigeMR, src.GreigeKG, src.OrderType, src.SyncDate, GETDATE(), GETDATE() "
//						+ "FROM #TempMainProd AS src LEFT JOIN [FromSapMainProd] AS target ON target.ProductionOrder = src.ProductionOrder "
//						+ "WHERE target.ProductionOrder IS NULL AND src.DataStatus <> 'X'");
				// รวมข้อ 3, 4, 5 เป็น Batch เดียวกันเพื่อประสิทธิภาพและเวลาที่แม่นยำ
				String upsertSql = 
				      "DECLARE @Now DATETIME = GETDATE(); "
				    
				    + "/* 3. จัดการ DataStatus = 'X' */ "
				    + "UPDATE target SET "
				    + "    target.DataStatus = 'X', "
				    + "    target.ChangeDate = @Now "
				    + "FROM [FromSapMainProd] AS target "
				    + "INNER JOIN #TempMainProd AS src ON target.ProductionOrder = src.ProductionOrder "
				    + "WHERE src.DataStatus = 'X' AND target.DataStatus = 'O'; "

				    + "/* 4. Update ข้อมูลเดิม (Matching ProductionOrder) */ "
				    + "UPDATE target SET "
				    + "    target.SaleOrder = src.SaleOrder, target.SaleLine = src.SaleLine, target.TotalQuantity = src.TotalQuantity, "
				    + "    target.Unit = src.Unit, target.RemAfterCloseOne = src.RemAfterCloseOne, target.RemAfterCloseTwo = src.RemAfterCloseTwo, "
				    + "    target.RemAfterCloseThree = src.RemAfterCloseThree, target.LabStatus = src.LabStatus, target.UserStatus = src.UserStatus, "
				    + "    target.DesignFG = src.DesignFG, target.ArticleFG = src.ArticleFG, target.BookNo = src.BookNo, target.Center = src.Center, "
				    + "    target.LotNo = src.LotNo, target.Batch = src.Batch, target.LabNo = src.LabNo, target.RemarkOne = src.RemarkOne, "
				    + "    target.RemarkTwo = src.RemarkTwo, target.RemarkThree = src.RemarkThree, target.BCAware = src.BCAware, "
				    + "    target.OrderPuang = src.OrderPuang, target.RefPrd = src.RefPrd, target.GreigeInDate = src.GreigeInDate, "
				    + "    target.BCDate = src.BCDate, target.Volumn = src.Volumn, target.CFdate = src.CFdate, target.CFType = src.CFType, "
				    + "    target.Shade = src.Shade, target.LotShipping = src.LotShipping, target.BillSendQuantity = src.BillSendQuantity, "
				    + "    target.Grade = src.Grade, target.DataStatus = src.DataStatus, target.PrdCreateDate = src.PrdCreateDate, "
				    + "    target.GreigeArticle = src.GreigeArticle, target.GreigeDesign = src.GreigeDesign, target.GreigeMR = src.GreigeMR, "
				    + "    target.GreigeKG = src.GreigeKG, target.OrderType = src.OrderType, target.SyncDate = src.SyncDate, "
				    + "    target.ChangeDate = @Now "
				    + "FROM [FromSapMainProd] AS target "
				    + "INNER JOIN #TempMainProd AS src ON target.ProductionOrder = src.ProductionOrder "
				    + "WHERE src.DataStatus <> 'X'; "

				    + "/* 5. Insert ข้อมูลใหม่ */ "
				    + "INSERT INTO [FromSapMainProd] ( "
				    + "    ProductionOrder, SaleOrder, SaleLine, TotalQuantity, Unit, "
				    + "    RemAfterCloseOne, RemAfterCloseTwo, RemAfterCloseThree, LabStatus, UserStatus, DesignFG, ArticleFG, "
				    + "    BookNo, Center, LotNo, Batch, LabNo, RemarkOne, RemarkTwo, RemarkThree, BCAware, OrderPuang, RefPrd, "
				    + "    GreigeInDate, BCDate, Volumn, CFdate, CFType, Shade, LotShipping, BillSendQuantity, Grade, DataStatus, "
				    + "    PrdCreateDate, GreigeArticle, GreigeDesign, GreigeMR, GreigeKG, OrderType, SyncDate, ChangeDate, CreateDate) "
				    + "SELECT "
				    + "    src.ProductionOrder, src.SaleOrder, src.SaleLine, src.TotalQuantity, src.Unit, "
				    + "    src.RemAfterCloseOne, src.RemAfterCloseTwo, src.RemAfterCloseThree, src.LabStatus, src.UserStatus, src.DesignFG, src.ArticleFG, "
				    + "    src.BookNo, src.Center, src.LotNo, src.Batch, src.LabNo, src.RemarkOne, src.RemarkTwo, src.RemarkThree, src.BCAware, src.OrderPuang, src.RefPrd, "
				    + "    src.GreigeInDate, src.BCDate, src.Volumn, src.CFdate, src.CFType, src.Shade, src.LotShipping, src.BillSendQuantity, src.Grade, src.DataStatus, "
				    + "    src.PrdCreateDate, src.GreigeArticle, src.GreigeDesign, src.GreigeMR, src.GreigeKG, src.OrderType, src.SyncDate, @Now, @Now "
				    + "FROM #TempMainProd AS src "
				    + "LEFT JOIN [FromSapMainProd] AS target ON target.ProductionOrder = src.ProductionOrder "
				    + "WHERE target.ProductionOrder IS NULL AND src.DataStatus <> 'X';";

				stmt.execute(upsertSql);
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}finally {
			    // ✅ ปิด transaction เสมอ ไม่ว่าจะ success หรือ error
			    try (java.sql.Statement cleanup = conn.createStatement()) {
			        cleanup.execute("IF OBJECT_ID('tempdb..#TempMainProd') IS NOT NULL DROP TABLE #TempMainProd");
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
//	public String upsertFromSapMainProdDetail(ArrayList<FromErpMainProdDetail> paList)
//	{
//
//
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
//				+ "    UPDATE [dbo].[FromSapMainProd]\r\n"
//				+ "    SET\r\n"
//				+ "        [DataStatus] = 'X',\r\n"
//				+ "        [ChangeDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ?\r\n"
//				+ "        AND [DataStatus] = 'O';\r\n"
//				+ "END\r\n"
//				+ "ELSE\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[FromSapMainProd]\r\n"
//				+ "    SET\r\n"
//				+ "        [SaleOrder] = ?,\r\n"
//				+ "        [SaleLine] = ?,\r\n"
//				+ "        [TotalQuantity] = ?,\r\n"
//				+ "        [Unit] = ?,\r\n"
//				+ "        [RemAfterCloseOne] = ?,\r\n"
//				+ "        [RemAfterCloseTwo] = ?,\r\n"
//				+ "        [RemAfterCloseThree] = ?,\r\n"
//				+ "        [LabStatus] = ?,\r\n"
//				+ "        [UserStatus] = ?,\r\n"
//				+ "        [DesignFG] = ?,\r\n"
//				+ "        [ArticleFG] = ?,\r\n"
//				+ "        [BookNo] = ?,\r\n"
//				+ "        [Center] = ?,\r\n"
//				+ "        [LotNo] = ?,\r\n"
//				+ "        [Batch] = ?,\r\n"
//				+ "        [LabNo] = ?,\r\n"
//				+ "        [RemarkOne] = ?,\r\n"
//				+ "        [RemarkTwo] = ?,\r\n"
//				+ "        [RemarkThree] = ?,\r\n"
//				+ "        [BCAware] = ?,\r\n"
//				+ "        [OrderPuang] = ?,\r\n"
//				+ "        [RefPrd] = ?,\r\n"
//				+ "        [GreigeInDate] = ?,\r\n"
//				+ "        [BCDate] = ?,\r\n"
//				+ "        [Volumn] = ?,\r\n"
//				+ "        [CFdate] = ?,\r\n"
//				+ "        [CFType] = ?,\r\n"
//				+ "        [Shade] = ?,\r\n"
//				+ "        [LotShipping] = ?,\r\n"
//				+ "        [BillSendQuantity] = ?,\r\n"
//				+ "        [Grade] = ?,\r\n"
//				+ "        [DataStatus] = ?,\r\n"
//				+ "        [PrdCreateDate] = ?,\r\n"
//				+ "        [GreigeArticle] = ?,\r\n"
//				+ "        [GreigeDesign] = ?,\r\n"
//				+ "        [GreigeMR] = ?,\r\n"
//				+ "        [GreigeKG] = ?,\r\n"
//				+ "        [ChangeDate] = ?,\r\n"
//				+ "        [OrderType] = ?,\r\n"
//				+ "        [SyncDate] = ?\r\n"
//				+ "    WHERE\r\n"
//				+ "        [ProductionOrder] = ?;\r\n"
//				+ "\r\n"
//				+ "    -- Check if rows were updated\r\n"
//				+ "    DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				+ "    \r\n"
//				+ "    IF @rc = 0\r\n"
//				+ "    BEGIN\r\n"
//				+ "        -- Insert if no rows were updated\r\n"
//				+ "        INSERT INTO [dbo].[FromSapMainProd] (\r\n"
//				+ "            [ProductionOrder],\r\n"
//				+ "            [SaleOrder],\r\n"
//				+ "            [SaleLine],\r\n"
//				+ "            [TotalQuantity],\r\n"
//				+ "            [Unit],\r\n"
//				+ "            [RemAfterCloseOne],\r\n"
//				+ "            [RemAfterCloseTwo],\r\n"
//				+ "            [RemAfterCloseThree],\r\n"
//				+ "            [LabStatus],\r\n"
//				+ "            [UserStatus],\r\n"
//				+ "            [DesignFG],\r\n"
//				+ "            [ArticleFG],\r\n"
//				+ "            [BookNo],\r\n"
//				+ "            [Center],\r\n"
//				+ "            [LotNo],\r\n"
//				+ "            [Batch],\r\n"
//				+ "            [LabNo],\r\n"
//				+ "            [RemarkOne],\r\n"
//				+ "            [RemarkTwo],\r\n"
//				+ "            [RemarkThree],\r\n"
//				+ "            [BCAware],\r\n"
//				+ "            [OrderPuang],\r\n"
//				+ "            [RefPrd],\r\n"
//				+ "            [GreigeInDate],\r\n"
//				+ "            [BCDate],\r\n"
//				+ "            [Volumn],\r\n"
//				+ "            [CFdate],\r\n"
//				+ "            [CFType],\r\n"
//				+ "            [Shade],\r\n"
//				+ "            [LotShipping],\r\n"
//				+ "            [BillSendQuantity],\r\n"
//				+ "            [Grade],\r\n"
//				+ "            [DataStatus],\r\n"
//				+ "            [PrdCreateDate],\r\n"
//				+ "            [GreigeArticle],\r\n"
//				+ "            [GreigeDesign],\r\n"
//				+ "            [GreigeMR],\r\n"
//				+ "            [GreigeKG],\r\n"
//				+ "            [ChangeDate],\r\n"
//				+ "            [CreateDate],\r\n"
//				+ "            [OrderType],\r\n"
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
//				+ "            ?, ?, ?, ?, ?,\r\n"
//				+ "            ?,\r\n"
//				+ "            ?\r\n"
//				+ "        );\r\n"
//				+ "    END\r\n"
//				+ "END";
//
//		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
//Connection connection = this.database.getConnection();
//PreparedStatement prepared = null;
//
//try {
//    prepared = connection.prepareStatement(sql);
//			int index = 1;
//			int batchSize = 0;
//
//			for (FromErpMainProdDetail bean : paList) {
//				index = 1;
//				prepared.setString(index++, bean.getDataStatus()   );
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index++, bean.getProductionOrder()    );
//				
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//this.sshUtl.setSqlBigDecimal(prepared, bean.getTotalQuantity(), index ++ );
//				prepared.setString(index ++ , bean.getUnit());
//				prepared.setString(index ++ , bean.getRemAfterCloseOne());
//				prepared.setString(index ++ , bean.getRemAfterCloseTwo());
//				prepared.setString(index ++ , bean.getRemAfterCloseThree());
//				prepared.setString(index ++ , bean.getLabStatus());
//				prepared.setString(index ++ , bean.getUserStatus());
//				prepared.setString(index ++ , bean.getDesignFG());
//				prepared.setString(index ++ , bean.getArticleFG());
//				prepared.setString(index ++ , bean.getBookNo());
//				prepared.setString(index ++ , bean.getCenter());
//				prepared.setString(index ++ , bean.getLotNo());
//				prepared.setString(index ++ , bean.getBatch());
//				prepared.setString(index ++ , bean.getLabNo());
//				prepared.setString(index ++ , bean.getRemarkOne());
//				prepared.setString(index ++ , bean.getRemarkTwo());
//				prepared.setString(index ++ , bean.getRemarkThree());
//				prepared.setString(index ++ , bean.getBcAware());
//				prepared.setString(index ++ , bean.getOrderPuang());
//				prepared.setString(index ++ , bean.getRefPrd());
//this.sshUtl.setSqlDate(prepared, bean.getGreigeInDate(), index ++ );
//this.sshUtl.setSqlDate(prepared, bean.getBcDate(), index ++ );
//this.sshUtl.setSqlBigDecimal(prepared, bean.getVolumn(), index ++ );
//this.sshUtl.setSqlDate(prepared, bean.getCfDate(), index ++ );
//this.sshUtl.setSqlDate(prepared, bean.getCfType(), index ++ );
//				prepared.setString(index ++ , bean.getShade());
//this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
//this.sshUtl.setSqlBigDecimal(prepared, bean.getBillSendQuantity(), index++); 
//				prepared.setString(index ++ , bean.getGrade());
//				prepared.setString(index ++ , bean.getDataStatus());
//this.sshUtl.setSqlDate(prepared, bean.getPrdCreateDate(), index ++ );
//				prepared.setString(index ++ , bean.getGreigeArticle());
//				prepared.setString(index ++ , bean.getGreigeDesign());
//this.sshUtl.setSqlBigDecimal(prepared, bean.getGreigeMR(), index ++ );
//this.sshUtl.setSqlBigDecimal(prepared, bean.getGreigeKG(), index ++ );
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getOrderType());
//this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ ); 
//				prepared.setString(index ++ , bean.getProductionOrder());
//
//				prepared.setString(index ++ , bean.getProductionOrder());
//				prepared.setString(index ++ , bean.getSaleOrder());
//				prepared.setString(index ++ , bean.getSaleLine());
//this.sshUtl.setSqlBigDecimal(prepared, bean.getTotalQuantity(), index ++ );
//				prepared.setString(index ++ , bean.getUnit());
//				prepared.setString(index ++ , bean.getRemAfterCloseOne());
//				prepared.setString(index ++ , bean.getRemAfterCloseTwo());
//				prepared.setString(index ++ , bean.getRemAfterCloseThree());
//				prepared.setString(index ++ , bean.getLabStatus());
//				prepared.setString(index ++ , bean.getUserStatus());
//				prepared.setString(index ++ , bean.getDesignFG());
//				prepared.setString(index ++ , bean.getArticleFG());
//				prepared.setString(index ++ , bean.getBookNo());
//				prepared.setString(index ++ , bean.getCenter());
//				prepared.setString(index ++ , bean.getLotNo());
//				prepared.setString(index ++ , bean.getBatch());
//				prepared.setString(index ++ , bean.getLabNo());
//				prepared.setString(index ++ , bean.getRemarkOne());
//				prepared.setString(index ++ , bean.getRemarkTwo());
//				prepared.setString(index ++ , bean.getRemarkThree());
//				prepared.setString(index ++ , bean.getBcAware());
//				prepared.setString(index ++ , bean.getOrderPuang());
//				prepared.setString(index ++ , bean.getRefPrd());
//this.sshUtl.setSqlDate(prepared, bean.getGreigeInDate(), index ++ );
//this.sshUtl.setSqlDate(prepared, bean.getBcDate(), index ++ );
//this.sshUtl.setSqlBigDecimal(prepared, bean.getVolumn(), index ++ );
//this.sshUtl.setSqlDate(prepared, bean.getCfDate(), index ++ );
//this.sshUtl.setSqlDate(prepared, bean.getCfType(), index ++ );
//				prepared.setString(index ++ , bean.getShade());
//this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
//this.sshUtl.setSqlBigDecimal(prepared, bean.getBillSendQuantity(), index++); 
//				prepared.setString(index ++ , bean.getGrade());
//				prepared.setString(index ++ , bean.getDataStatus());
//this.sshUtl.setSqlDate(prepared, bean.getPrdCreateDate(), index ++ );
//				prepared.setString(index ++ , bean.getGreigeArticle());
//				prepared.setString(index ++ , bean.getGreigeDesign());
//this.sshUtl.setSqlBigDecimal(prepared, bean.getGreigeMR(), index ++ );
//this.sshUtl.setSqlBigDecimal(prepared, bean.getGreigeKG(), index ++ );
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setTimestamp(index ++ , new Timestamp(time));
//				prepared.setString(index ++ , bean.getOrderType());
//this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
//				prepared.addBatch();
//				batchSize++;
//	            if (batchSize % 500 == 0) { // Execute batch every 500 records 
//	    			prepared.executeBatch();
//	    			prepared.clearBatch();
//	                batchSize = 0; // Reset batch size
//	            }
//			}
//			prepared.executeBatch();
//			prepared.close();
//		} catch (SQLException e) {
////			e.printStackTrace();
//			 e.printStackTrace();
//			iconStatus = "E";
//		} finally {
//			// this.database.close();
//		}
//		return iconStatus;
//	}
}
