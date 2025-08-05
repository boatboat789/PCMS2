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
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainProdDao;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.ProductionOrderLogDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.MapperUtility;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapMainProdDaoImpl implements FromSapMainProdDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
	public FromSapMainProdDaoImpl(Database database) {
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
			+ ",SyncDate\r\n" ;
	@Override
	public ArrayList<ProductionOrderLogDetail> getFromSapMainProdDetailWithRangeOfChangeDate( String startLogDate , String endLogDate  , String productionOrder )
	{
		ArrayList<ProductionOrderLogDetail> list = null;
		String where = " WHERE 1 = 1 and ( DataStatus = 'O' )   ";
		if ( ! startLogDate.equals("")) {
//			String[] array = startLogDate.split(" - ");
			where += " "
					+ " and (  "
					+ "	CAST(a.[ChangeDate] AS DATE) >= convert(date,'" + startLogDate + "', 103) AND \r\n"
					+ "	CAST(a.[ChangeDate] AS DATE) <= convert(date,'" + endLogDate + "', 103) \r\n"
					+ "	) \r\n";    
//			where += " CAST(a.[CreateDate] AS DATE) = convert(date, '"+createDate+"', 103)  \r\n" ;
		}
		if ( ! productionOrder.equals("")) { 
			where += " "
					+ " and (  "
					+ " a.[ProductionOrder] = '" + productionOrder + "' \r\n" 
					+ "	) \r\n";     
		}
		String sql = " "
				+ " SELECT DISTINCT \r\n"
				+ this.selectForLog
				+ " FROM [PCMS].[dbo].[FromSapMainProd] a\r\n"
				+ where  ;
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(MapperUtility .mapToObject(map, ProductionOrderLogDetail.class));
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
				+ " 	ProductionOrder = '" + prdOrder + "'  \r\n"
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
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection(); 
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();

		String iconStatus = "I";
		String sql = "-- Update if the record exists\r\n"
				+ "IF ? = 'X'\r\n"
				+ "BEGIN\r\n"
				+ "    UPDATE [dbo].[FromSapMainProd]\r\n"
				+ "    SET [DataStatus] = 'X'\r\n"
				+ "    WHERE [ProductionOrder] = ?;\r\n"
				+ "END\r\n"
				+ "ELSE \r\n"
				+ "BEGIN\r\n"
				+ "UPDATE [dbo].[FromSapMainProd]\r\n"
				+ "SET \r\n"
				+ "    [SaleOrder] = ?,\r\n"
				+ "    [SaleLine] = ?,\r\n"
				+ "    [TotalQuantity] = ?,\r\n"
				+ "    [Unit] = ?,\r\n"
				+ "    [RemAfterCloseOne] = ?,\r\n"
				+ "    [RemAfterCloseTwo] = ?,\r\n"
				+ "    [RemAfterCloseThree] = ?,\r\n"
				+ "    [LabStatus] = ?,\r\n"
				+ "    [UserStatus] = ?,\r\n"
				+ "    [DesignFG] = ?,\r\n"
				+ "    [ArticleFG] = ?,\r\n"
				+ "    [BookNo] = ?,\r\n"
				+ "    [Center] = ?,\r\n"
				+ "    [LotNo] = ?,\r\n"
				+ "    [Batch] = ?,\r\n"
				+ "    [LabNo] = ?,\r\n"
				+ "    [RemarkOne] = ?,\r\n"
				+ "    [RemarkTwo] = ?,\r\n"
				+ "    [RemarkThree] = ?,\r\n"
				+ "    [BCAware] = ?,\r\n"
				+ "    [OrderPuang] = ?,\r\n"
				+ "    [RefPrd] = ?,\r\n"
				+ "    [GreigeInDate] = ?,\r\n"
				+ "    [BCDate] = ?,\r\n"
				+ "    [Volumn] = ?,\r\n"
				+ "    [CFdate] = ?,\r\n"
				+ "    [CFType] = ?,\r\n"
				+ "    [Shade] = ?,\r\n"
				+ "    [LotShipping] = ?,\r\n"
				+ "    [BillSendQuantity] = ?,\r\n"
				+ "    [Grade] = ?,\r\n"
				+ "    [DataStatus] = ?,\r\n"
				+ "    [PrdCreateDate] = ?,\r\n"
				+ "    [GreigeArticle] = ?,\r\n"
				+ "    [GreigeDesign] = ?,\r\n"
				+ "    [GreigeMR] = ?,\r\n"
				+ "    [GreigeKG] = ?,\r\n"
				+ "    [ChangeDate] = ?,\r\n"
				+ "    [OrderType] = ?,\r\n"  
				+ "    [SyncDate] =  ?\r\n"

				+ "WHERE \r\n"
				+ "    [ProductionOrder] = ?;\r\n"
				+ "\r\n"
				+ "END\r\n"
				+ "-- Check if rows were updated\r\n"
				+ "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				+ "IF @rc <> 0\r\n"
				+ "   SELECT 1;\r\n"
				+ "ELSE \r\n"
				+ "BEGIN\r\n"
				+ "    -- Insert if no rows were updated\r\n"
				+ "    INSERT INTO [dbo].[FromSapMainProd] (\r\n"
				+ "        [ProductionOrder],\r\n"
				+ "        [SaleOrder],\r\n"
				+ "        [SaleLine],\r\n"
				+ "        [TotalQuantity],\r\n"
				+ "        [Unit],\r\n" 
				+ "        [RemAfterCloseOne],\r\n"
				+ "        [RemAfterCloseTwo],\r\n"
				+ "        [RemAfterCloseThree],\r\n"
				+ "        [LabStatus],\r\n"
				+ "        [UserStatus],\r\n" // 10 
				+ "        [DesignFG],\r\n"
				+ "        [ArticleFG],\r\n"
				+ "        [BookNo],\r\n"
				+ "        [Center],\r\n"
				+ "        [LotNo],\r\n" 
				+ "        [Batch],\r\n"
				+ "        [LabNo],\r\n"
				+ "        [RemarkOne],\r\n"
				+ "        [RemarkTwo],\r\n"
				+ "        [RemarkThree],\r\n" // 20 
				+ "        [BCAware],\r\n"
				+ "        [OrderPuang],\r\n"
				+ "        [RefPrd],\r\n"
				+ "        [GreigeInDate],\r\n"
				+ "        [BCDate],\r\n" 
				+ "        [Volumn],\r\n"
				+ "        [CFdate],\r\n"
				+ "        [CFType],\r\n"
				+ "        [Shade],\r\n"
				+ "        [LotShipping],\r\n"// 30 
				+ "        [BillSendQuantity],\r\n"
				+ "        [Grade],\r\n"
				+ "        [DataStatus],\r\n"
				+ "        [PrdCreateDate],\r\n"
				+ "        [GreigeArticle],\r\n" 
				+ "        [GreigeDesign],\r\n"
				+ "        [GreigeMR],\r\n"
				+ "        [GreigeKG],\r\n"
				+ "        [ChangeDate],\r\n"
				+ "        [CreateDate],\r\n"// 40 
				+ "        [OrderType]\r\n"
				+ "      ,[SyncDate] \r\n"
				+ "    ) VALUES (\r\n"
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "// 10
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "// 20
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?,"
				+ "?, "// 30
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "// 40
				+ "?\r\n"
				+ ", ? "
				+ "    ); "
				+ "END ";
		try {

			int index = 1;
			int batchSize = 0;
			prepared = connection.prepareStatement(sql);
			for (FromErpMainProdDetail bean : paList) {
				index = 1;
				prepared.setString(index++, bean.getDataStatus()   );
				prepared.setString(index++, bean.getProductionOrder()    );
				
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getTotalQuantity(), index ++ );
				prepared.setString(index ++ , bean.getUnit());
				prepared.setString(index ++ , bean.getRemAfterCloseOne());
				prepared.setString(index ++ , bean.getRemAfterCloseTwo());
				prepared.setString(index ++ , bean.getRemAfterCloseThree());
				prepared.setString(index ++ , bean.getLabStatus());
				prepared.setString(index ++ , bean.getUserStatus());
				prepared.setString(index ++ , bean.getDesignFG());
				prepared.setString(index ++ , bean.getArticleFG());
				prepared.setString(index ++ , bean.getBookNo());
				prepared.setString(index ++ , bean.getCenter());
				prepared.setString(index ++ , bean.getLotNo());
				prepared.setString(index ++ , bean.getBatch());
				prepared.setString(index ++ , bean.getLabNo());
				prepared.setString(index ++ , bean.getRemarkOne());
				prepared.setString(index ++ , bean.getRemarkTwo());
				prepared.setString(index ++ , bean.getRemarkThree());
				prepared.setString(index ++ , bean.getBcAware());
				prepared.setString(index ++ , bean.getOrderPuang());
				prepared.setString(index ++ , bean.getRefPrd());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getGreigeInDate(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getBcDate(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getVolumn(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCfDate(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCfType(), index ++ );
				prepared.setString(index ++ , bean.getShade());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getBillSendQuantity(), index++); 
				prepared.setString(index ++ , bean.getGrade());
				prepared.setString(index ++ , bean.getDataStatus());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getPrdCreateDate(), index ++ );
				prepared.setString(index ++ , bean.getGreigeArticle());
				prepared.setString(index ++ , bean.getGreigeDesign());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getGreigeMR(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getGreigeKG(), index ++ );
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setString(index ++ , bean.getOrderType());
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ ); 
				prepared.setString(index ++ , bean.getProductionOrder());

				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , bean.getSaleLine());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getTotalQuantity(), index ++ );
				prepared.setString(index ++ , bean.getUnit());
				prepared.setString(index ++ , bean.getRemAfterCloseOne());
				prepared.setString(index ++ , bean.getRemAfterCloseTwo());
				prepared.setString(index ++ , bean.getRemAfterCloseThree());
				prepared.setString(index ++ , bean.getLabStatus());
				prepared.setString(index ++ , bean.getUserStatus());
				prepared.setString(index ++ , bean.getDesignFG());
				prepared.setString(index ++ , bean.getArticleFG());
				prepared.setString(index ++ , bean.getBookNo());
				prepared.setString(index ++ , bean.getCenter());
				prepared.setString(index ++ , bean.getLotNo());
				prepared.setString(index ++ , bean.getBatch());
				prepared.setString(index ++ , bean.getLabNo());
				prepared.setString(index ++ , bean.getRemarkOne());
				prepared.setString(index ++ , bean.getRemarkTwo());
				prepared.setString(index ++ , bean.getRemarkThree());
				prepared.setString(index ++ , bean.getBcAware());
				prepared.setString(index ++ , bean.getOrderPuang());
				prepared.setString(index ++ , bean.getRefPrd());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getGreigeInDate(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getBcDate(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getVolumn(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCfDate(), index ++ );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getCfType(), index ++ );
				prepared.setString(index ++ , bean.getShade());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getLotShipping(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getBillSendQuantity(), index++); 
				prepared.setString(index ++ , bean.getGrade());
				prepared.setString(index ++ , bean.getDataStatus());
				prepared = this.sshUtl.setSqlDate(prepared, bean.getPrdCreateDate(), index ++ );
				prepared.setString(index ++ , bean.getGreigeArticle());
				prepared.setString(index ++ , bean.getGreigeDesign());
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getGreigeMR(), index ++ );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getGreigeKG(), index ++ );
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setString(index ++ , bean.getOrderType());
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index ++ );
				prepared.addBatch();
				batchSize++;
	            if (batchSize % 500 == 0) { // Execute batch every 500 records 
	    			prepared.executeBatch();
	    			prepared.clearBatch();
	                batchSize = 0; // Reset batch size
	            }
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
