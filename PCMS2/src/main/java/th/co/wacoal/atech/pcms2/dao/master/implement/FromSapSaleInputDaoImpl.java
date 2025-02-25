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

import th.co.wacoal.atech.pcms2.dao.master.FromSapSaleInputDao;
import th.co.wacoal.atech.pcms2.entities.SaleInputDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleInputDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class FromSapSaleInputDaoImpl implements  FromSapSaleInputDao{
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
    public FromSapSaleInputDaoImpl (Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<SaleInputDetail> getFromSapSaleInputDetailByProductionOrder(String prodOrder){
		ArrayList<SaleInputDetail> list = null;
		String where = " where  "; 
		where += " a.ProductionOrder = '" + prodOrder + "'  and a.[DataStatus] = 'O' \r\n";
		String sql =
				  " SELECT DISTINCT  \r\n"
		        + " 	[ProductionOrder],[BillDate]\r\n"
				+ "     ,[BillQtyPerSale],[SaleOrder]\r\n"
				+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "			WHEN 0 THEN ''  \r\n"
				+ "			ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "			END AS [SaleLine] \r\n"
				+ "     ,[BillQtyPerStock],[Remark],[CustomerNo]\r\n"
				+ "     ,[CustomerName1],[CustomerPO],[DueDate]\r\n"
				+ "     ,[Color],[No],a.[DataStatus]\r\n" + " "
				+ " from [PCMS].[dbo].[FromSapSaleInput] as a \r\n "
				+ where
				+ " Order by [No]"; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSaleInputDetail(map));
		}
		return list;
	}

	@Override
	public String upsertFromSapSaleInputDetail(ArrayList<FromErpSaleInputDetail> paList)
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
				  + "UPDATE [dbo].[FromSapSaleInput]\r\n"
				  + "SET \r\n"   
				  + "    [BillDate] = ?,\r\n"
				  + "    [BillQtyPerSale] = ?,\r\n"  
				  + "    [SaleOrder] = ?,\r\n"  
				  + "    [SaleLine] = ?,\r\n"  
				  + "    [BillQtyPerStock] = ?,\r\n"  
				  + "    [Remark] = ?,\r\n"  
				  + "    [CustomerNo] = ?,\r\n"  
				  + "    [CustomerName1] = ?,\r\n"  
				  + "    [CustomerPO] = ?,\r\n"  
				  + "    [DueDate] = ?,\r\n"  
				  + "    [Color] = ?,\r\n"   
//				  + "    [DataStatus] = ?,\r\n"
				  + "    [ChangeDate] = ? \r\n"  
				  + "    ,[SyncDate] =  ?\r\n" 
				  + "WHERE \r\n"
				  + "    [ProductionOrder] = ? AND\r\n"  
				  + "    [No] = ? ;\r\n"
				  + "-- Check if rows were updated\r\n"
				  + "DECLARE @rc INT = @@ROWCOUNT;\r\n"
				  + "IF @rc <> 0\r\n"
				  + "   SELECT 1;\r\n"
				  + "ELSE \r\n"
				  + "    -- Insert if no rows were updated\r\n"
				  + "    INSERT INTO [dbo].[FromSapSaleInput] (\r\n"
				  + "        [ProductionOrder] ,[BillDate] ,[BillQtyPerSale] ,[SaleOrder] ,[SaleLine]\r\n"
				  + "      ,[BillQtyPerStock] ,[Remark] ,[CustomerNo] ,[CustomerName1] ,[CustomerPO]\r\n"
				  + "      ,[DueDate] ,[Color] ,[No]  ,[ChangeDate]\r\n"
				  + "      ,[CreateDate]\r\n"
				  + "      ,[SyncDate] \r\n"
				  + "    ) VALUES (\r\n"
				  + "		?, ?, ?, ?, ?, "
				  + "		?, ?, ?, ?, ?, "
				  + "		?, ?, ?, ?, "
				  + "		? , " 
					+ "		 ? "
				  + "    ); "
				+ ";"  ;
		try {

			int index = 1;
			prepared = connection.prepareStatement(sql); 
			for(FromErpSaleInputDetail bean : paList) {
				index = 1; 
				prepared = this.sshUtl.setSqlDate(prepared, bean.getBillDate() , index++); 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getBillQtyPerSale() , index++); 
				prepared.setString(index++, bean.getSaleOrder()    );
				prepared.setString(index++, bean.getSaleLine()    );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getBillQtyPerStock() , index++); 
				prepared.setString(index++, bean.getRemark()    );
				prepared.setString(index++, bean.getCustomerNo()    );
				prepared.setString(index++, bean.getCustomerName1()    );
				prepared.setString(index++, bean.getCustomErpO()    );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getDueDate() , index++); 
				prepared.setString(index++, bean.getColor()    ); 
				prepared.setTimestamp(index++, new Timestamp(time));
				prepared = this.sshUtl.setSqlTimeStamp(prepared, bean.getSyncDate(), index++);
				
				prepared.setString(index++, bean.getProductionOrder()    );
				prepared.setString(index++, bean.getNo()    ); 

				prepared.setString(index++, bean.getProductionOrder()    );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getBillDate() , index++); 
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getBillQtyPerSale() , index++); 
				prepared.setString(index++, bean.getSaleOrder()    );
				prepared.setString(index++, bean.getSaleLine()    );
				prepared = this.sshUtl.setSqlBigDecimal(prepared, bean.getBillQtyPerStock() , index++); 
				prepared.setString(index++, bean.getRemark()    );
				prepared.setString(index++, bean.getCustomerNo()    );
				prepared.setString(index++, bean.getCustomerName1()    );
				prepared.setString(index++, bean.getCustomErpO()    );
				prepared = this.sshUtl.setSqlDate(prepared, bean.getDueDate() , index++); 
				prepared.setString(index++, bean.getColor()    );
				prepared.setString(index++, bean.getNo()    );  
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
