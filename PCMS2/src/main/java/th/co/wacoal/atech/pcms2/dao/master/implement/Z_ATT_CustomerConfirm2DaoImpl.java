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

import th.co.wacoal.atech.pcms2.dao.master.Z_ATT_CustomerConfirm2Dao;
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.service.PCMSSqlService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository
public class Z_ATT_CustomerConfirm2DaoImpl implements Z_ATT_CustomerConfirm2Dao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private String cte = ""
			+ " WITH OperationMax AS (\r\n"
			+ "    SELECT \r\n"
			+ "        ProductionOrder, \r\n"
			+ "        MAX(CASE WHEN Operation BETWEEN 100 AND 104 AND AdminStatus = '-' THEN Operation END) AS maxOperationDye,\r\n"
			+ "        MAX(CASE WHEN Operation BETWEEN 195 AND 198 AND AdminStatus = '-' THEN Operation END) AS maxOperationColorCheck\r\n"
			+ "    FROM [PPMM].[dbo].[DataFromSap]\r\n"
			+ "    GROUP BY ProductionOrder\r\n"
			+ "),\r\n"
			+ "CTE_Data AS (\r\n"
			+ "    SELECT DISTINCT\r\n"
			+ "        dfs.ProductionOrder,\r\n"
			+ "        om.maxOperationDye,\r\n"
			+ "        om.maxOperationColorCheck,\r\n"
			+ "        SFCD_Dye.ST AS Dye_St,\r\n"
			+ "        SFCD_Dye.Da AS Dye_Da,\r\n"
			+ "        SFCD_Dye.Db AS Dye_Db,\r\n"
			+ "        SFCD_Dye.L AS Dye_L,\r\n"
			+ "        SFCD_Dye.ValDeltaE AS Dye_DeltaE,\r\n"
			+ "        SFCD_ColorCheck.ST AS ColorCheck_ST,\r\n"
			+ "        SFCD_ColorCheck.Da AS ColorCheck_Da,\r\n"
			+ "        SFCD_ColorCheck.Db AS ColorCheck_Db,\r\n"
			+ "        SFCD_ColorCheck.L AS ColorCheck_L,\r\n"
			+ "        SFCD_ColorCheck.ValDeltaE AS ColorCheck_DeltaE,\r\n"
			+ "        SFCD_ColorCheck.WorkDate AS ColorCheckDate,\r\n"
			+ "        SFCD_ColorCheck.ColorCheckName,\r\n"
			+ "        SFCD_ColorCheck.ColorCheckStatus,\r\n"
			+ "        SFCD_ColorCheck.ColorCheckRollNo,\r\n"
			+ "        SFCD_ColorCheck.ColorCheckRemark\r\n"
			+ "    FROM [PCMS].[dbo].[FromSapMainProd] dfs\r\n"
			+ "    LEFT JOIN OperationMax om ON om.ProductionOrder = dfs.ProductionOrder\r\n"
			+ "    LEFT JOIN [PPMM].[dbo].[ShopFloorControlDetail] AS SFCD_Dye\r\n"
			+ "        ON SFCD_Dye.ProductionOrder = dfs.ProductionOrder\r\n"
			+ "        AND SFCD_Dye.Operation = om.maxOperationDye\r\n"
//			+ "    LEFT JOIN [PPMM].[dbo].[ShopFloorControlDetail] AS SFCD_Fin\r\n"
//			+ "        ON SFCD_Fin.ProductionOrder = dfs.ProductionOrder\r\n"
//			+ "        AND SFCD_Fin.Operation = om.maxOperationFinishing\r\n"
			+ "    LEFT JOIN [PPMM].[dbo].[ShopFloorControlDetail] AS SFCD_ColorCheck\r\n"
			+ "        ON SFCD_ColorCheck.ProductionOrder = dfs.ProductionOrder\r\n"
			+ "        AND SFCD_ColorCheck.Operation = om.maxOperationColorCheck\r\n"
			+ "    WHERE dfs.DataStatus = 'O'\r\n"
			+ ")\r\n";
	private String select = ""

			+ "    a.[Id],"
			+ "    a.[SendDate],\r\n"
			+ "    TRY_CAST( a.[NoPerDay] AS INT ) AS NoPerDay,\r\n"
			+ "    a.[ReplyDate],\r\n"
			+ "    a.[CFMNo],\r\n"
			+ "    a.[CustomerName],\r\n"
			+ "    a.[SO],\r\n"
			+ "    a.[SOLine] AS SOLine,\r\n"
			+ "    a.[DueDate],\r\n"
			+ "    a.[PO],\r\n"
			+ "    a.[Material],\r\n"
			+ "    a.[ProductName],\r\n"
			+ "    a.[LabNo],\r\n"
			+ "    a.[Color],\r\n"
			+ "    a.[ProdId],\r\n"
			+ "    a.[LotNo],\r\n"
			+ "    TRY_CAST( b.[Dye_L] AS DECIMAL(13,3) ) AS Dye_L,\r\n"
			+ "    TRY_CAST( b.[Dye_Da] AS DECIMAL(13,3) ) AS Dye_Da ,\r\n"
			+ "    TRY_CAST( b.[Dye_Db] AS DECIMAL(13,3) ) AS Dye_Db,\r\n"
			+ "    TRY_CAST( b.[Dye_St] AS DECIMAL(13,3) ) AS Dye_St,\r\n"
			+ "    TRY_CAST( b.[Dye_DeltaE] AS DECIMAL(13,3) ) AS Dye_DeltaE,\r\n"
			+ "    TRY_CAST( b.[ColorCheck_L] AS DECIMAL(13,3) ) AS ColorCheck_L,\r\n"
			+ "    TRY_CAST( b.[ColorCheck_Da] AS DECIMAL(13,3) ) AS ColorCheck_Da,\r\n"
			+ "    TRY_CAST( b.[ColorCheck_Db] AS DECIMAL(13,3) ) AS ColorCheck_Db,\r\n"
			+ "    TRY_CAST( b.[ColorCheck_St] AS DECIMAL(13,3) ) AS ColorCheck_St,\r\n"
			+ "    TRY_CAST( b.[ColorCheck_DeltaE] AS DECIMAL(13,3) ) AS ColorCheck_DeltaE,\r\n"
			+ "    a.[CFM_L],\r\n"
			+ "    a.[CFM_Da],\r\n"
			+ "    a.[CFM_Db],\r\n"
			+ "    a.[CFM_St],\r\n"
			+ "    a.[CFM_DeltaE],\r\n"
			+ "    a.[Result],\r\n"
			+ "    a.[QCComment],\r\n"
			+ "    a.[RemarkFromSubmit],\r\n"
			+ "    a.[NextLot],\r\n"
			+ "    b.ColorCheckDate,\r\n"
			+ "    b.ColorCheckStatus,\r\n"
			+ "    b.ColorCheckRemark,\r\n"
			+ "    TRY_CAST(a.[Qty] AS Decimal(13,3) ) as Qty,\r\n"
			+ "    a.[UnitId],\r\n"
			+ "    a.[DataStatus],\r\n"
			+ "    a.[ChangeDate],\r\n"
			+ "    a.[CreateDate]\r\n";

	private BeanCreateService bcModel = new BeanCreateService();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public Z_ATT_CustomerConfirm2DaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2Detail(String prodOrder, String lotNubmer,
			String replyDate, String custName, String so, String sendDate)
	{
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = null;
		String where = " where DataStatus = 'O' ";
//		String where = " where 1 = 1 "; 
		if ( ! custName.equals("")) {
			where += " and a.[CustomerName] like '" + custName + "%' \r\n";
		}
		if ( ! so.equals("")) {
			where += " and a.[SO] like '" + so + "%' \r\n";
		}
		if ( ! prodOrder.equals("")) {
			where += " and a.[ProdId] like '" + prodOrder + "%' \r\n";
		}
		if ( ! lotNubmer.equals("")) {
			where += " and a.LotNo like '" + lotNubmer + "%' \r\n";
		}
		where += PCMSSqlService.buildDateClause("SendDate", sendDate, "a");
		where += PCMSSqlService.buildDateClause("ReplyDate", replyDate, "a");
//		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = ""
				+ this.cte
				+ "SELECT \r\n"
				+ this.select
				+ "FROM [PCMS].[dbo].[Z_ATT_CustomerConfirm2] AS a\r\n"
				+ "LEFT JOIN CTE_Data AS b ON a.[ProdId] = b.[ProductionOrder]\r\n"
				+ where;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genZ_ATT_CustomerConfirm2Detail(map));
		}
		return list;
	}

	@Override
	public ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2DetailById(
			ArrayList<Z_ATT_CustomerConfirm2Detail> poList)
	{
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = null;
		String where = " where DataStatus = 'O' ";
//		String where = " where 1 = 1 ";  
		if ( ! poList.isEmpty()) {
			where += " AND a.Id in ( ";
			for (int i = 0; i < poList.size(); i ++ ) {
				Z_ATT_CustomerConfirm2Detail beanTmp = poList.get(i);
				int poId = beanTmp.getId();
				where += " " + poId + " ";
				if (i < poList.size()-1) {
					where += " , ";
				}
			}
			where += " ) \r\n";
		}
//		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql = ""
				+ this.cte
				+ "SELECT \r\n"
				+ this.select
				+ "FROM [PCMS].[dbo].[Z_ATT_CustomerConfirm2] AS a\r\n"
				+ "LEFT JOIN CTE_Data AS b ON a.[ProdId] = b.[ProductionOrder]\r\n"
				+ where;
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genZ_ATT_CustomerConfirm2Detail(map));
		}
		return list;
	}
	@Override
	public String upsertZ_ATT_CustomerConfirm2Detail(ArrayList<Z_ATT_CustomerConfirm2Detail> paList) {
	    if (paList == null || paList.isEmpty()) return "I";

	    String iconStatus = "I";
	    Connection connection = this.database.getConnection();

	    try {
	        connection.setAutoCommit(false);
	        try (Statement stmt = connection.createStatement()) {
	            // 1. สร้าง Temp Table (เช็กชื่อคอลัมน์ให้ตรงกับตัว ps.set ด้านล่าง)
	        	// 1. สร้าง Temp Table โดยบังคับ Collation ให้ตรงกับ Database หลัก
	        	stmt.execute("IF OBJECT_ID('tempdb..#TempCustConfirm') IS NOT NULL DROP TABLE #TempCustConfirm");
				 

	        	stmt.execute("CREATE TABLE #TempCustConfirm (" +
	        	    "SendDate DATE, NoPerDay INT, ReplyDate DATE, " +
	        	    "CFMNo VARCHAR(50) COLLATE DATABASE_DEFAULT, " + // เพิ่ม COLLATE DATABASE_DEFAULT
	        	    "CustomerName VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "SO VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "SOLine VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "DueDate DATE, " +
	        	    "PO VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "Material VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "ProductName VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "LabNo VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "Color VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "ProdId VARCHAR(50) COLLATE DATABASE_DEFAULT, " + // สำคัญมาก เพราะใช้ JOIN
	        	    "LotNo VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "CFM_L DECIMAL(13,3), CFM_Da DECIMAL(13,3), CFM_Db DECIMAL(13,3), " +
	        	    "CFM_St DECIMAL(13,3), CFM_DeltaE DECIMAL(13,3), " +
	        	    "Result VARCHAR(10) COLLATE DATABASE_DEFAULT, " +
	        	    "QCComment VARCHAR(200) COLLATE DATABASE_DEFAULT, " +
	        	    "RemarkFromSubmit VARCHAR(200) COLLATE DATABASE_DEFAULT, " +
	        	    "NextLot VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "Qty DECIMAL(13,3), " +
	        	    "UnitId VARCHAR(50) COLLATE DATABASE_DEFAULT, " +
	        	    "DataStatus VARCHAR(1) COLLATE DATABASE_DEFAULT)");
	            // 2. Batch Insert ลง Temp (27 พารามิเตอร์)
	            String insertTemp = "INSERT INTO #TempCustConfirm VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	            try (PreparedStatement ps = connection.prepareStatement(insertTemp)) {
	                for (Z_ATT_CustomerConfirm2Detail bean : paList) {
	                    int idx = 1;
	                    this.sshUtl.setSqlDate(ps, bean.getSendDate(), idx++);        // 1
	                    this.sshUtl.setSqlInt(ps, bean.getNoPerDay(), idx++);         // 2
	                    this.sshUtl.setSqlDate(ps, bean.getReplyDate(), idx++);       // 3
	                    ps.setString(idx++, bean.getCfmNo());                         // 4
	                    ps.setString(idx++, bean.getCustomerName());                  // 5
	                    ps.setString(idx++, bean.getSo());                            // 6
	                    ps.setString(idx++, bean.getSoLine());                        // 7
	                    this.sshUtl.setSqlDate(ps, bean.getDueDate(), idx++);         // 8
	                    ps.setString(idx++, bean.getPo());                            // 9
	                    ps.setString(idx++, bean.getMaterial());                      // 10
	                    ps.setString(idx++, bean.getProductName());                   // 11
	                    ps.setString(idx++, bean.getLabNo());                         // 12
	                    ps.setString(idx++, bean.getColor());                         // 13
	                    ps.setString(idx++, bean.getProdID());                        // 14
	                    ps.setString(idx++, bean.getLotNo());                         // 15
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getCfmL(), idx++);      // 16
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getCfmDa(), idx++);     // 17
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getCfmDb(), idx++);     // 18
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getCfmSt(), idx++);     // 19
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getCfmDeltaE(), idx++); // 20
	                    ps.setString(idx++, bean.getResult());                        // 21
	                    ps.setString(idx++, bean.getQcComment());                     // 22
	                    ps.setString(idx++, bean.getRemarkFromSubmit());              // 23
	                    ps.setString(idx++, bean.getNextLot());                        // 24
	                    this.sshUtl.setSqlBigDecimal(ps, bean.getQty(), idx++);       // 25
	                    ps.setString(idx++, bean.getUnitId());                        // 26
	                    ps.setString(idx++, bean.getDataStatus());                    // 27
	                    ps.addBatch();
	                }
	                ps.executeBatch();
	            }
	         // รวมข้อ 3, 4, 5 เป็น Batch เดียวเพื่อประสิทธิภาพและเวลาที่สอดคล้องกัน
	            String upsertSql =
	                  "DECLARE @Now DATETIME = GETDATE(); "

	                // 3. ปิด stale records: ProdId อยู่ใน batch ใหม่ แต่ ProdId+CFMNo ไม่มาด้วย
	                // = ERP ยกเลิก/ลบ CFMNo นั้นออกแล้ว ต้องปิดด้วย DataStatus='X'
	                + "/* 3. CLOSE stale — ProdId in new batch but CFMNo not included */ "
	                + " UPDATE target\r\n"
	                + "SET\r\n"
	                + "    target.DataStatus = 'X',\r\n"
	                + "    target.ChangeDate = @Now\r\n"
	                + "FROM dbo.Z_ATT_CustomerConfirm2 target\r\n"
	                + "INNER JOIN\r\n"
	                + "(\r\n"
	                + "    SELECT DISTINCT ProdId\r\n"
	                + "    FROM #TempCustConfirm\r\n"
	                + ") p\r\n"
	                + "    ON p.ProdId = target.ProdId\r\n"
	                + "LEFT JOIN #TempCustConfirm src\r\n"
	                + "    ON src.ProdId = target.ProdId\r\n"
	                + "   AND src.CFMNo = target.CFMNo\r\n"
	                + "WHERE src.ProdId IS NULL\r\n"
	                + "  AND target.DataStatus <> 'X'; "

	                + "/* 4. UPDATE (ครบ 26 คอลัมน์ที่ต้องเปลี่ยน) */ "
	                + "UPDATE target SET "
	                + "    target.SendDate = src.SendDate, target.NoPerDay = src.NoPerDay, target.ReplyDate = src.ReplyDate, "
	                + "    target.CustomerName = src.CustomerName, target.SO = src.SO, target.SOLine = src.SOLine, "
	                + "    target.DueDate = src.DueDate, target.PO = src.PO, target.Material = src.Material, "
	                + "    target.ProductName = src.ProductName, target.LabNo = src.LabNo, target.Color = src.Color, "
	                + "    target.LotNo = src.LotNo, target.CFM_L = src.CFM_L, target.CFM_Da = src.CFM_Da, "
	                + "    target.CFM_Db = src.CFM_Db, target.CFM_St = src.CFM_St, target.CFM_DeltaE = src.CFM_DeltaE, "
	                + "    target.Result = src.Result, target.QCComment = src.QCComment, target.RemarkFromSubmit = src.RemarkFromSubmit, "
	                + "    target.NextLot = src.NextLot, target.Qty = src.Qty, target.UnitId = src.UnitId, "
	                + "    target.DataStatus = src.DataStatus, "
	                + "    target.ChangeDate = @Now "
	                + "FROM [dbo].[Z_ATT_CustomerConfirm2] AS target "
	                + "INNER JOIN #TempCustConfirm AS src ON target.ProdId = src.ProdId AND target.CFMNo = src.CFMNo; "

	                + "/* 5. INSERT (ครบ 29 คอลัมน์ตาม Schema) */ "
	                + "INSERT INTO [dbo].[Z_ATT_CustomerConfirm2] ("
	                + "    SendDate, NoPerDay, ReplyDate, CFMNo, CustomerName, SO, SOLine, DueDate, PO, Material, "
	                + "    ProductName, LabNo, Color, ProdId, LotNo, CFM_L, CFM_Da, CFM_Db, CFM_St, CFM_DeltaE, "
	                + "    Result, QCComment, RemarkFromSubmit, NextLot, Qty, UnitId, DataStatus, ChangeDate, CreateDate) "
	                + "SELECT "
	                + "    src.SendDate, src.NoPerDay, src.ReplyDate, src.CFMNo, src.CustomerName, src.SO, src.SOLine, "
	                + "    src.DueDate, src.PO, src.Material, src.ProductName, src.LabNo, src.Color, src.ProdId, src.LotNo, "
	                + "    src.CFM_L, src.CFM_Da, src.CFM_Db, src.CFM_St, src.CFM_DeltaE, src.Result, src.QCComment, "
	                + "    src.RemarkFromSubmit, src.NextLot, src.Qty, src.UnitId, src.DataStatus, @Now, @Now "
	                + "FROM #TempCustConfirm AS src "
	                + "LEFT JOIN [dbo].[Z_ATT_CustomerConfirm2] AS target ON target.ProdId = src.ProdId AND target.CFMNo = src.CFMNo "
	                + "WHERE target.ProdId IS NULL;";

	            stmt.execute(upsertSql);
	            connection.commit();
	        } catch (Exception e) {
	            if (connection != null) connection.rollback();
	            throw e;
	        } finally {
	            // ✅ DROP temp table ก่อนคืน connection กลับ pool
	            if (connection != null) {
	                try (java.sql.Statement cleanup = connection.createStatement()) {
	                    cleanup.execute("IF OBJECT_ID('tempdb..#TempCustConfirm') IS NOT NULL DROP TABLE #TempCustConfirm");
	                } catch (Exception ignored) {}
	                connection.setAutoCommit(true);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        iconStatus = "E";
	    }
	    return iconStatus;
	}
}
