package th.co.wacoal.atech.pcms2.dao.master.implement.PPMM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.RollFromSapDao;
import th.co.wacoal.atech.pcms2.entities.PODetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler; 
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class RollFromSapDaoImpl implements RollFromSapDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String selectPO = ""
			+ "     [ProductionOrder]\r\n"
		    + "   , [RollNumber]\r\n"
		    + "   , [RollWeight]\r\n"
			+ "   , [RollLength]\r\n"
			+ "   , [POCreatedate]\r\n"
			+ "   , [PurchaseOrder]\r\n"
			+ "   , CASE PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"
			+ "		 	ELSE SUBSTRING( [PurchaseOrderLine] , PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' '), LEN( [PurchaseOrderLine] ) )\r\n"
			+ "       	END AS [PurchaseOrderLine] \r\n"
			+ "   , [RequiredDate]\r\n" 
			+ "   , [PODefault]\r\n"
			+ "   , [POLineDefault]\r\n"
			+ "   , [POPostingDateDefault]\r\n"
			+ "   , a.[DataStatus] \r\n"; 
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public RollFromSapDaoImpl(Database database ) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}
	@Override
	public  ArrayList<PODetail> getRollFromSapDetailByProductionOrder(String prodOrder){
		ArrayList<PODetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] in ( 'O' ) \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				+ this.selectPO 
				+ " from [PPMM].[dbo].[RollFromSap] as a \r\n "
				+ where
				+ " Order by [RollNumber]";
//
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}
//	@Override
//	public String upsertRollFromSapFromERPPODetail(ArrayList<FromErpPODetail> paList)
//	{
//
//		PreparedStatement prepared = null;
//		Connection connection;
//		connection = this.database.getConnection();  
//		Calendar calendar = Calendar.getInstance();
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
//		
//		String iconStatus = "I";
//		String sql =
//				  "-- Update if the record exists\r\n"
//				  + " "
//				  + "IF ? = 'X'\r\n"
//				  + "BEGIN\r\n"
//				  + "    UPDATE [dbo].[RollFromSap]\r\n"
//				  + "    SET [DataStatus] = 'X'\r\n"
//				  + "    WHERE [ProductionOrder] = ?;\r\n"
//				  + "END\r\n"
//				  + "ELSE \r\n"
//				  + "BEGIN\r\n"
//				  + "    UPDATE [dbo].[RollFromSap]\r\n"
//				  + "    SET \r\n"
//				  + "        [RollWeight] = ?,\r\n"
//				  + "        [RollLength] = ?,\r\n"
//				  + "        [POCreatedate] = ?,\r\n"
//				  + "        [RequiredDate] = ?,\r\n"
//				  + "        [PurchaseOrder] = ?,\r\n"
//				  + "        [PurchaseOrderLine] = ?,\r\n"
//				  + "        [PODefault] = ?,\r\n"
//				  + "        [POLineDefault] = ?,\r\n"
//				  + "        [POPostingDateDefault] = ?,\r\n"
//				  + "        [ChangeDate] = ?,\r\n"
//				  + "        [SyncDate] = ?,\r\n"
//				  + "        [DataStatus] = ?\r\n"
//				  + "    WHERE \r\n"
//				  + "        [ProductionOrder] = ? AND\r\n"
//				  + "        [RollNumber] = ?;\r\n"
//				  + "END\r\n"
//				  + "\r\n"
//				  + "-- Check if rows were updated\r\n"
//				  + "DECLARE @rc INT = @@ROWCOUNT;\r\n"
//				  + "IF @rc <> 0\r\n"
//				  + "    SELECT 1;\r\n"
//				  + "ELSE \r\n"
//				  + "BEGIN\r\n"
//				  + "    -- Insert if no rows were updated\r\n"
//				  + "    INSERT INTO [dbo].[RollFromSap] (\r\n"
//				  + "        [ProductionOrder], [RollNumber], [RollWeight], [RollLength], [POCreatedate],\r\n"
//				  + "        [RequiredDate], [PurchaseOrder], [PurchaseOrderLine], [PODefault],\r\n"
//				  + "        [POLineDefault], [POPostingDateDefault], [ChangeDate], [CreateDate], [SyncDate],\r\n"
//				  + "        [DataStatus]\r\n"
//				  + "    ) VALUES (\r\n"
//				  + "        ?, ?, ?, ?, ?, \r\n"
//				  + "        ?, ?, ?, ?, \r\n"
//				  + "        ?, ?, ?, ?, ?, \r\n"
//				  + "        ?\r\n"
//				  + "    );\r\n"
//				  + "END "   ;
//		try {
//
//			int index = 1;
//			prepared = connection.prepareStatement(sql); 
////			System.out.println(paList.size());
//			for(FromErpPODetail bean : paList) {
//				index = 1; 
// 
//				prepared.setString(index++, bean.getDataStatus()   );
//				prepared.setString(index++, bean.getProductionOrder()    );
//				
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRollWeight() , index++); 
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRollLength() , index++);  
//				prepared = this.sshUtl.setSqlDate(prepared, bean.getpoCreatedate() , index++); 
//				prepared = this.sshUtl.setSqlDate(prepared, bean.getRequiredDate() , index++); 
//				prepared.setString(index++, bean.getPurchaseOrder()    );
//				prepared.setString(index++, bean.getPurchaseOrderLine()    );  
//				prepared.setString(index++, bean.getPoDefault()    );
//				prepared.setString(index++, bean.getPoLineDefault()    ); 
//				prepared = this.sshUtl.setSqlDate(prepared, bean.getPoPostingDateDefault() , index++);   
//				prepared.setTimestamp(index++, new Timestamp(time));  
//				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index++);
//				prepared.setString(index++, bean.getDataStatus()   );
//				prepared.setString(index++, bean.getProductionOrder()    );
//				prepared.setString(index++, bean.getRollNumber()    );
//				
//				prepared.setString(index++, bean.getProductionOrder()    );
//				prepared.setString(index++, bean.getRollNumber()    );
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRollWeight() , index++); 
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRollLength() , index++);  
//				prepared = this.sshUtl.setSqlDate(prepared, bean.getpoCreatedate() , index++); 
//				prepared = this.sshUtl.setSqlDate(prepared, bean.getRequiredDate() , index++); 
//				prepared.setString(index++, bean.getPurchaseOrder()    );
//				prepared.setString(index++, bean.getPurchaseOrderLine()    ); 
//				prepared.setString(index++, bean.getPoDefault()    );
//				prepared.setString(index++, bean.getPoLineDefault()    ); 
//				prepared = this.sshUtl.setSqlDate(prepared, bean.getPoPostingDateDefault() , index++);   
//				prepared.setTimestamp(index++, new Timestamp(time));
//				prepared.setTimestamp(index++, new Timestamp(time));  
//				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index++);
//				prepared.setString(index++, bean.getDataStatus()   );  
//				prepared.addBatch();
//			}
//			prepared.executeBatch();
//			prepared.close(); 
//			this.execHandlerRollNoImportDetail();
////			System.out.println(" "+paList.size());
//		} catch (Exception e) {
//			e.printStackTrace(); 
//			iconStatus = "E";
//		}finally {
//			//this.database.close();
//		}
//		return iconStatus;
//	} 
//	public void execHandlerRollNoImportDetail() {
//		// TODO Auto-generated method stub
//		Connection connection;
//		connection = this.database.getConnection();
//		String sql = "EXEC [dbo].[spd_RollNoImport] ";
//		try {
//			PreparedStatement prepared = connection.prepareStatement(sql);
//			prepared.execute();
//			prepared.close();
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//	}
}
