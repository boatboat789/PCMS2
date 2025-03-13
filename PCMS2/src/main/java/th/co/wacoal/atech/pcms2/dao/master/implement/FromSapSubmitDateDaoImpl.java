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

import th.co.wacoal.atech.pcms2.dao.master.FromSapSubmitDateDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapSubmitDateDaoImpl implements  FromSapSubmitDateDao{
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
    public FromSapSubmitDateDaoImpl (Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	} 

	@Override
	public ArrayList<InputDateDetail> getSubmitDateDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSTableDetail bean = poList.get(0);
		String prdOrder = bean.getProductionOrder();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String sql =
			" SELECT \r\n"
		  + "    	 [ProductionOrder]\r\n"
		  + "      , [SaleOrder]\r\n"
		  + "      , [SaleLine]\r\n"
		  + "      , [PlanDate]\r\n"
		  + "      , [CreateBy]\r\n"
		  + "      , [CreateDate]\r\n"
		  + "	   , '1:PCMS' as InputFrom \r\n"
		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
		  + " where a.[ProductionOrder] = '" + prdOrder + "' and \r\n"
		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
		  + "       a.[SaleLine] = '" + saleLine+ "' \r\n"
		  + " union ALL  \r\n "
		  + " SELECT \r\n"
		  + "        [ProductionOrder]\r\n"
		  + "      , [SaleOrder]\r\n"
		  + "      , [SaleLine]\r\n"
		  + "      , SubmitDate as [PlanDate]\r\n"
		  + "      , '' AS [CreateBy]\r\n"
		  + "      , null AS [CreateDate]\r\n"
		  + "	   , '0:ERP365' as InputFrom \r\n"
		  + " FROM [PCMS].[dbo].[FromSapSubmitDate]  as a\r\n"
		  + " where a.[ProductionOrder] = '" + prdOrder + "' and SubmitDate is not null \r\n"
  		  + "   and a.[DataStatus] = 'O' \r\n"
		  + " ORDER BY InputFrom ,CreateDate ";


		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>(); 
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}    
	@Override
	public String upsertFromSapSubmitDateDetail(ArrayList<FromErpSubmitDateDetail> paList)
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
				  + "IF ? = 'X'\r\n"
				  + "BEGIN\r\n"
				  + "    UPDATE [dbo].[FromSapSubmitDate]\r\n"
				  + "    SET [DataStatus] = 'X'\r\n"
				  + "    WHERE [ProductionOrder] = ?;\r\n"
				  + "END\r\n"
				  + "ELSE \r\n"
				  + "BEGIN\r\n"
				  + "UPDATE [dbo].[FromSapSubmitDate]\r\n"
				  + "SET \r\n"   
				  + "    [SubmitDate] = ?,\r\n"
				  + "    [Remark] = ?,\r\n"  
				  + "    [DataStatus] = ?,\r\n"
				  + "    [ChangeDate] = ? \r\n"  
					+ "      ,[SyncDate] =  ?\r\n" 
				  + "WHERE \r\n"
				  + "    [ProductionOrder] = ? AND\r\n" 
				  + "    [SaleOrder] = ? AND\r\n"
				  + "    [SaleLine] = ? AND\r\n"
				  + "    [No] = ? ;\r\n"
				  + "END\r\n"
				  + "-- Check if rows were updated\r\n"
				  + "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				  + "IF @rc <> 0\r\n"
				  + "   SELECT 1;\r\n"
				  + "ELSE \r\n"
				  + "BEGIN\r\n"
				  + "    -- Insert if no rows were updated\r\n"
				  + "    INSERT INTO [dbo].[FromSapSubmitDate] (\r\n"
				  + "        [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[No] ,[SubmitDate]\r\n"
				  + "      ,[Remark] ,[DataStatus] ,[ChangeDate] ,[CreateDate]\r\n"
				  + "      ,[SyncDate] \r\n"
				  + "    ) VALUES (\r\n"
				  + "		?, ?, ?, ?, ?, "
				  + "		?, ?, ?, ? " 
					+ "		, ? " 
				  + "    ); "
				  + "END "    ;
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql); 
			for(FromErpSubmitDateDetail bean : paList) {
				index = 1;
				prepared.setString(index++, bean.getDataStatus()   );
				prepared.setString(index++, bean.getProductionOrder()    );

				prepared = this.sshUtl.setSqlDate(prepared, bean.getSubmitDate() , index++);  
				prepared.setString(index++, bean.getRemark()    );
				prepared.setString(index++, bean.getDataStatus()   ); 
				prepared.setTimestamp(index++, new Timestamp(time)); 
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index++);

				prepared.setString(index++, bean.getProductionOrder()    );
				prepared.setString(index++, bean.getSaleOrder()    );
				prepared.setString(index++, bean.getSaleLine()    );
				prepared.setString(index++, bean.getNo()    ); 
				
				
				prepared.setString(index++, bean.getProductionOrder()    );
				prepared.setString(index++, bean.getSaleOrder()    );
				prepared.setString(index++, bean.getSaleLine()    );
				prepared.setString(index++, bean.getNo()    ); 
				prepared = this.sshUtl.setSqlDate(prepared, bean.getSubmitDate() , index++);  
				prepared.setString(index++, bean.getRemark()    );
				prepared.setString(index++, bean.getDataStatus()   ); 
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
//			System.err.println(e); 
			 e.printStackTrace();
			iconStatus = "E";
		}finally {
			//this.database.close();
		}
		return iconStatus;
	} 

	 
}
