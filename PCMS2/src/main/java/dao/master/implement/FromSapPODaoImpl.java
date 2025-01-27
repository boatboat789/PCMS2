	package dao.master.implement;

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

import dao.master.FromSapPODao;
import entities.PODetail;
import entities.erp.atech.FromErpPODetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapPODaoImpl implements  FromSapPODao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private String selectPO = ""
			+ "     [ProductionOrder]\r\n"
		    + "   , [RollNo]\r\n"
		    + "   , [QuantityKG]\r\n"
			+ "   , [QuantityMR]\r\n"
			+ "   , [POCreatedate]\r\n"
			+ "   , [PurchaseOrder]\r\n"
			+ "   , CASE PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' ‘')\r\n"
			+ "			WHEN 0 THEN ''  \r\n"
			+ "		 	ELSE SUBSTRING( [PurchaseOrderLine] , PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' '), LEN( [PurchaseOrderLine] ) )\r\n"
			+ "       	END AS [PurchaseOrderLine] \r\n"
			+ "   , [RequiredDate]\r\n"
			+ "   , [PurchaseOrderDate]\r\n"
			+ "   , [PODefault]\r\n"
			+ "   , [POLineDefault]\r\n"
			+ "   , [POPostingDateDefault]\r\n"
			+ "   , a.[DataStatus] \r\n"; 
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
    public FromSapPODaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<PODetail> getFromSapPODetailByProductionOrder(String prodOrder){
		ArrayList<PODetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
				+ this.selectPO
				+ " from [PCMS].[dbo].[FromSapPO] as a \r\n "
				+ where
				+ " Order by [RollNo]";
//
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapPODetail(ArrayList<FromErpPODetail> paList)
	{

		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection(); 
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		
		String iconStatus = "I";
		String sql =
				  "-- Update if the record exists\r\n"
				  + " "
				  + "UPDATE [dbo].[FromSapCFM]\r\n"
				  + "SET \r\n"   
				  + "    [QuantityKG] = ?,\r\n"
				  + "    [QuantityMR] = ?,\r\n"  
				  + "    [POCreatedate] = ?,\r\n"
				  + "    [RequiredDate] = ? ,\r\n" 
				  + "    [PurchaseOrder] = ?, \r\n" 
				  + "    [PurchaseOrderLine] = ?, \r\n" 
				  + "    [PurchaseOrderDate] = ? ,\r\n" 
				  + "    [PODefault] = ?, \r\n" 
				  + "    [POLineDefault] = ?, \r\n" 
				  + "    [POPostingDateDefault] = ?, \r\n"  
				  + "    [DataStatus] = ?, \r\n" 
				  + "    [ChangeDate]= ? \r\n" 

				+ "      ,[SyncDate] =  ?\r\n" 
				  + "WHERE \r\n"
				  + "    [ProductionOrder] = ? AND\r\n" 
				  + "    [RollNo] = ? ;\r\n"
				  + "-- Check if rows were updated\r\n"
				  + "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				  + "IF @rc <> 0\r\n"
				  + "    PRINT @rc;\r\n"
				  + "ELSE \r\n"
				  + "    -- Insert if no rows were updated\r\n"
				  + "    INSERT INTO [dbo].[FromSapCFM] (\r\n"
				  + "        [ProductionOrder] ,[RollNo] ,[QuantityKG]  ,[QuantityMR] ,[POCreatedate]\r\n"
				  + "      ,[RequiredDate] ,[PurchaseOrder] ,[PurchaseOrderLine] ,[PurchaseOrderDate] ,[PODefault]\r\n"
				  + "      ,[POLineDefault] ,[POPostingDateDefault] ,[DataStatus] ,[ChangeDate] ,[CreateDate]\r\n"
				  + "      ,[SyncDate] \r\n"
				  + "    ) VALUES (\r\n"
				  + "		?, ?, ?, ?, ?, "
				  + "		?, ?, ?, ?, ?, "
				  + "		?, ?, ?, ?, ? " 
					+ ", ? " 
				  + "    ); "
				+ ";"  ;
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql); 
			for(FromErpPODetail bean : paList) {
				index = 1; 

				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG() , index++); 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR() , index++);  
				prepared = this.sshUtl.setSqlDate(prepared, bean.getpoCreatedate() , index++); 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRequiredDate() , index++); 
				prepared.setString(index++, bean.getPurchaseOrder()    );
				prepared.setString(index++, bean.getPurchaseOrderLine()    );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getPurchaseOrderDate() , index++); 
				prepared.setString(index++, bean.getPoDefault()    );
				prepared.setString(index++, bean.getPoLineDefault()    ); 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getPoPostingDateDefault() , index++);  
				prepared.setString(index++, bean.getDataStatus()    ); 
				prepared.setTimestamp(index++, new Timestamp(time));  
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index++);
				prepared.setString(index++, bean.getProductionOrder()    );
				prepared.setString(index++, bean.getRollNo()    );
				
				prepared.setString(index++, bean.getProductionOrder()    );
				prepared.setString(index++, bean.getRollNo()    );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityKG() , index++); 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getQuantityMR() , index++);  
				prepared = this.sshUtl.setSqlDate(prepared, bean.getpoCreatedate() , index++); 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getRequiredDate() , index++); 
				prepared.setString(index++, bean.getPurchaseOrder()    );
				prepared.setString(index++, bean.getPurchaseOrderLine()    );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getPurchaseOrderDate() , index++); 
				prepared.setString(index++, bean.getPoDefault()    );
				prepared.setString(index++, bean.getPoLineDefault()    ); 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getPoPostingDateDefault() , index++);  
				prepared.setString(index++, bean.getDataStatus()    ); 
				prepared.setTimestamp(index++, new Timestamp(time));
				prepared.setTimestamp(index++, new Timestamp(time));  
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index++);
//				prepared.setString(index++, bean.get    );
//				prepared = this.sshUtl.setSqlDate(prepared, bean.get , index++); 
//				prepared.setTimestamp(index++, new Timestamp(time));
//				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.get , index++); 
				prepared.addBatch();
			}
			prepared.executeBatch();
			prepared.close(); 
		} catch (SQLException e) {
			System.err.println(e); 
			iconStatus = "E";
		}finally {
			//this.database.close();
		}
		return iconStatus;
	} 
}
