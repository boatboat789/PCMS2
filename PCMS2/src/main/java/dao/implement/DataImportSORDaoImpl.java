package dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import dao.DataImportSORDao;
import entities.SORDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

public class DataImportSORDaoImpl implements DataImportSORDao {
   private Database database;
   private Database databaseSor;
 private String message; 
 private BeanCreateModel bcModel = new BeanCreateModel();
   public DataImportSORDaoImpl(Database database,Database databaseSor ) { 
	   this.database = database;  
	   this.databaseSor = databaseSor;  
	   this.message = "";     	
   }

   public String getMessage() {
      return this.message;
   }  
   public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");  
   @Override
public void upSertSORToPCMS() {
	   PreparedStatement prepared = null;
	   Connection connection;
	   connection = this.database.getConnection();   
		ArrayList<SORDetail> list = getSORdetail();
		Calendar calendar = Calendar.getInstance(); 
//		java.util.Date currentTime = calendar.getTime();
//		long time = currentTime.getTime();
		String saleLine = "",cfmDate = "";
		
		String sql = 
				"UPDATE [PCMS].[dbo].[FromSORCFM] "
				+ " SET [CFMDate] = ?\n"
				+ "     ,[ChangeDate] = ?\n" 
				+ " WHERE [SaleOrder] = ? and [SaleLine]  = ? "
				+ " declare  @rc int = @@ROWCOUNT "  
				+ "  if @rc <> 0 " 
				+ " print @rc " 
				+ " else " 
				+ " INSERT INTO [PCMS].[dbo].[FromSORCFM]	 "
				+ " ([SaleOrder] ,[SaleLine] ,[CFMDate] ,[ChangeDate] )" 
				+ " values(? , ? , ? , ?  )  ;"  ;  
		int i = 0;
		java.util.Date date ;
		Timestamp timestamp ;
		try {      
	    	for( i = 0 ; i < list.size();i++ ) {
	    		SORDetail bean = list.get(i); 
	    		saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
				prepared = connection.prepareStatement(sql);     
				timestamp = Timestamp.valueOf(bean.getLastUpdate()); 
//			   System.out.println(timestamp);
				cfmDate = bean.getCFMDate();
				if (cfmDate.equals("")) {
					prepared.setNull(1, java.sql.Types.DATE);   
					prepared.setNull(7, java.sql.Types.DATE);   
				}
				else {
					date = sdf2.parse(cfmDate ); 
					prepared.setDate(1, convertJavaDateToSqlDate(date) );
					prepared.setDate(7, convertJavaDateToSqlDate(date) );
				} 
				prepared.setTimestamp(2, timestamp);
				prepared.setString(3, bean.getSaleOrder());
				prepared.setString(4, saleLine);   
				prepared.setString(5, bean.getSaleOrder());
				prepared.setString(6, saleLine);  
				 
				prepared.setTimestamp(8, timestamp);  		
				prepared.executeUpdate();    
//				System.out.println(prepared.executeUpdate()  );
	    	}
		} catch (SQLException e) {
			System.err.println("insertLabNoDetail"+e.getMessage()); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
   }
   public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}
   private ArrayList<SORDetail> getSORdetail() {
      ArrayList<SORDetail> list = null;
      String sql = 
      		  "SELECT DISTINCT  [SO_NO]\r\n"
      		+ "      ,[SO_Line] \r\n"    
      		+ "	  ,	CONVERT(date, [CFM])    AS [CFM]\r\n"
      		+ "	  ,max(a.[LastUpdateCFM]) as [LastUpdateCFM]  \r\n"
      		+ "  FROM [SOR_PRODUCTION].[dbo].[V_PCMS2]  as z   \r\n"
      		+ "  inner join [SOR_PRODUCTION].[dbo].[PurchaseOrders] as b on b.[No] = z.PO_NO\r\n"
      		+ "  inner join [SOR_PRODUCTION].[dbo].[POLineItems] as a on b.Id = a.[POId] and z.MaterialCode = a.MaterialCode\r\n"
      		+ "  where   SaleOrderId is not null and a.IsActive = 1 \r\n"
//      		+ "     and (CONVERT(date, a.[LastUpdateCFM]) < CONVERT(date, GETDATE())  ) \r\n"
      		+ " and (CONVERT(date, a.[LastUpdateCFM]) > CONVERT(date, GETDATE()-10)  )"
      		+ "  group by  [SO_NO] ,[SO_Line]  ,[CFM]\r\n "
      		+ "  ";
//      System.out.println(sql);
      List<Map<String, Object>> datas = this.databaseSor.queryList(sql);
      list = new ArrayList<SORDetail>();
      for (Map<String, Object> map : datas) {
    	  list.add(this.bcModel._genSORDetail(map));
      }
      return list;
   }
 
}
