package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
 
import th.co.wacoal.atech.pcms2.dao.master.Z_ATT_CustomerConfirm2Dao; 
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.service.PCMSSearchService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler; 
import th.in.totemplate.core.sql.Database; 

public class Z_ATT_CustomerConfirm2DaoImpl implements  Z_ATT_CustomerConfirm2Dao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New 
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private String cte = ""
			+ " WITH OperationMax AS (\r\n"
			+ "    SELECT \r\n"
			+ "        ProductionOrder, \r\n"
			+ "        MAX(CASE WHEN Operation BETWEEN 100 AND 104 AND AdminStatus = '-' THEN Operation END) AS maxOperationDye,\r\n"
//			+ "        MAX(CASE WHEN Operation BETWEEN 190 AND 193 AND AdminStatus = '-' THEN Operation END) AS maxOperationFinishing,\r\n"
			+ "        MAX(CASE WHEN Operation BETWEEN 195 AND 198 AND AdminStatus = '-' THEN Operation END) AS maxOperationColorCheck\r\n"
			+ "    FROM [PPMM].[dbo].[DataFromSap]\r\n"
			+ "    GROUP BY ProductionOrder\r\n"
			+ "),\r\n"
			+ "CTE_Data AS (\r\n"
			+ "    SELECT DISTINCT\r\n"
			+ "        dfs.ProductionOrder,\r\n"
			+ "        om.maxOperationDye,\r\n"
//			+ "        om.maxOperationFinishing,\r\n"
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
	
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message; 
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public Z_ATT_CustomerConfirm2DaoImpl (Database database) {
		this.database = database;
		this.message = "";  
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2Detail(
			String prodOrder
			,String lotNubmer 
			,String replyDate
			,String custName 
			,String so 
			,String sendDate  ){
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = null;
		String where = " where DataStatus = 'O' "; 
//		String where = " where 1 = 1 "; 
		if(!custName.equals("")) { where += " and a.[CustomerName] like '"+custName+"%' \r\n";}
		if(!so.equals("")) { where += " and a.[SO] like '"+so+"%' \r\n";}
		if(!prodOrder.equals("")) { where += " and a.[ProdId] like '"+prodOrder+"%' \r\n";} 
		if(!lotNubmer.equals("")) { where += " and a.LotNo like '"+lotNubmer+"%' \r\n";} 
		where += PCMSSearchService.buildDateClause("SendDate", sendDate,"a");
		where += PCMSSearchService.buildDateClause("ReplyDate", replyDate,"a");
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
	public  ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2DetailById(
			ArrayList<Z_ATT_CustomerConfirm2Detail> poList  ){
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = null;
		String where = " where DataStatus = 'O' "; 
//		String where = " where 1 = 1 ";  
		if(!poList.isEmpty()) {
			where += " AND a.Id in ( " ;
			for (int i = 0; i < poList.size(); i ++ ) {
				Z_ATT_CustomerConfirm2Detail beanTmp = poList.get(i);
				int poId = beanTmp.getId();
				where += " " + poId + " ";
				if (i < poList.size() -1) {
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
	public String upsertZ_ATT_CustomerConfirm2Detail(ArrayList<Z_ATT_CustomerConfirm2Detail> paList)
	{
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection(); 
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();

		String iconStatus = "I";
		String sql = ""
//				+ "-- Update if the record exists\r\n"
//				+ "IF ? = 'X'\r\n"
//				+ "BEGIN\r\n"
//				+ "    UPDATE [dbo].[Z_ATT_CustomerConfirm2]\r\n"
//				+ "    SET [DataStatus] = 'X'\r\n"
//				+ "    WHERE [ProdId] = ?;\r\n"
//				+ "END\r\n"
//				+ "ELSE \r\n"
				+ "BEGIN\r\n"
				+ "UPDATE [dbo].[Z_ATT_CustomerConfirm2]\r\n"
				+ "SET \r\n"
				+ "    [SendDate] = ?,\r\n"
				+ "    [NoPerDay] = ?,\r\n"
				+ "    [ReplyDate] = ?,\r\n" 
				+ "    [CustomerName] = ?,\r\n"
				+ "    [SO] = ?,\r\n"
				+ "    [SOLine] = ?,\r\n"
				+ "    [DueDate] = ?,\r\n"
				+ "    [PO] = ?,\r\n"
				+ "    [Material] = ?,\r\n"
				+ "    [ProductName] = ?,\r\n"
				+ "    [LabNo] = ?,\r\n"
				+ "    [Color] = ?,\r\n"
				+ "    [LotNo] = ?,\r\n"
				+ "    [CFM_L] = ?,\r\n"
				+ "    [CFM_Da] = ?,\r\n"
				+ "    [CFM_Db] = ?,\r\n"
				+ "    [CFM_St] = ?,\r\n"
				+ "    [CFM_DeltaE] = ?,\r\n"
				+ "    [Result] = ?,\r\n"
				+ "    [QCComment] = ?,\r\n"
				+ "    [RemarkFromSubmit] = ?,\r\n"
				+ "    [NextLot] = ?,\r\n"
				+ "    [Qty] = ?,\r\n"
				+ "    [UnitId] = ?,\r\n" 
				+ "    [DataStatus] = ?,\r\n"
				+ "    [ChangeDate] = ? \r\n" 
				+ "WHERE \r\n"
				+ "    [ProdId] = ? and"
				+ "    [CFMNo] = ? \r\n"
				+ "    ;\r\n"
				+ "\r\n"
				+ "END\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "   SELECT 1;\r\n"
				+ "ELSE \r\n"
				+ "BEGIN\r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[Z_ATT_CustomerConfirm2]\r\n"
				+ "           ([SendDate]\r\n"
				+ "           ,[NoPerDay]\r\n"
				+ "           ,[ReplyDate]\r\n"
				+ "           ,[CFMNo]\r\n"
				+ "           ,[CustomerName]\r\n"
				+ "           ,[SO]\r\n"
				+ "           ,[SOLine]\r\n"
				+ "           ,[DueDate]\r\n"
				+ "           ,[PO]\r\n"
				+ "           ,[Material]\r\n"
				+ "           ,[ProductName]\r\n"
				+ "           ,[LabNo]\r\n"
				+ "           ,[Color]\r\n"
				+ "           ,[ProdId]\r\n"
				+ "           ,[LotNo]\r\n"
				+ "           ,[CFM_L]\r\n"
				+ "           ,[CFM_Da]\r\n"
				+ "           ,[CFM_Db]\r\n"
				+ "           ,[CFM_St]\r\n"
				+ "           ,[CFM_DeltaE]\r\n"
				+ "           ,[Result]\r\n"
				+ "           ,[QCComment]\r\n"
				+ "           ,[RemarkFromSubmit]\r\n"
				+ "           ,[NextLot]\r\n"
				+ "           ,[Qty]\r\n"
				+ "           ,[UnitId]\r\n"
				+ "           ,[DataStatus]\r\n"
				+ "           ,[ChangeDate]\r\n"
				+ "           ,[CreateDate])"
				+ "	 VALUES  \r\n"
				+ "("
				+ "	?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ ",?\r\n"
				+ " ); "
				+ "END\r\n";
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql);
			for (Z_ATT_CustomerConfirm2Detail bean : paList) {
				index = 1; 
//				prepared.setString(index++, bean.getProdID()) ;

				prepared = this.sshUtl.setSqlDate(prepared, bean.getSendDate(), index ++ );
				prepared = this.sshUtl.setSqlInt(prepared, bean.getNoPerDay(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getReplyDate(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getCustomerName(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getSo(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getSoLine(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getDueDate(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getPo(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getMaterial(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getProductName(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getLabNo(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getColor(), index ++ ); 
				prepared = this.sshUtl.setSqlString(prepared, bean.getLotNo() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmL() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmDa() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmDb() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmSt() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmDeltaE() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getResult() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getQcComment() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getRemarkFromSubmit() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getNextLot() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQty(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getUnitId() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getDataStatus() , index ++ );
				prepared.setTimestamp(index ++ , new Timestamp(time)); 
				
				prepared.setString(index++, bean.getProdID()    );
				prepared = this.sshUtl.setSqlString(prepared, bean.getCfmNo(), index ++ );
				 
				prepared = this.sshUtl.setSqlDate(prepared, bean.getSendDate(), index ++ );
				prepared = this.sshUtl.setSqlInt(prepared, bean.getNoPerDay(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getReplyDate(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getCfmNo(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getCustomerName(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getSo(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getSoLine(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getDueDate(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getPo(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getMaterial(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getProductName(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getLabNo(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getColor(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getProdID() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getLotNo() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmL() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmDa() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmDb() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmSt() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getCfmDeltaE() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getResult() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getQcComment() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getRemarkFromSubmit() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getNextLot() , index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQty(), index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getUnitId() , index ++ );
				prepared = this.sshUtl.setSqlString(prepared, bean.getDataStatus() , index ++ );
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setTimestamp(index ++ , new Timestamp(time));
 
				prepared.addBatch();
//				prepared.setString(index++, bean.get    );
//				prepared = this.sshUtl.setSqlDate(prepared, bean.get , index++); 
//				prepared.setTimestamp(index++, new Timestamp(time));
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.get , index++); 
			}
			prepared.executeBatch();
			prepared.close();
		} catch (SQLException e) {
//			e.printStackTrace();
			 e.printStackTrace();
			iconStatus = "E";
		} finally {
			// this.database.close();
		} 
		return iconStatus;
	}
} 


