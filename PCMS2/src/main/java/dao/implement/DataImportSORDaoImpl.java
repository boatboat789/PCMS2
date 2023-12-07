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
import model.master.FromSORCFMModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class DataImportSORDaoImpl implements DataImportSORDao {
	private Database database;
	private Database databaseSor;
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private String message;
	private BeanCreateModel bcModel = new BeanCreateModel();

	public DataImportSORDaoImpl(Database database) {
		this.database = database; 
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

   public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");  
   @Override
	public void upSertSORToPCMS() { 
	   FromSORCFMModel fscModel = new FromSORCFMModel();
		ArrayList<SORDetail> list = this.getSORdetail(); 
		String value = fscModel.upSertFromSORCFMDetail(list);
   } 
   private ArrayList<SORDetail> getSORdetail() {
      ArrayList<SORDetail> list = null;
      String sql = ""
//      		+  "SELECT DISTINCT  [SO_NO]\r\n"
//      		+ "      ,[SO_Line] \r\n"    
//      		+ "	  ,	CONVERT(date, [CFM])    AS [CFM]\r\n"
//      		+ "	  ,max(a.[LastUpdateCFM]) as [LastUpdateCFM]  \r\n"
//      		+ "  FROM [SOR_PRODUCTION].[dbo].[V_PCMS2]  as z   \r\n"
//      		+ "  inner join [SOR_PRODUCTION].[dbo].[PurchaseOrders] as b on b.[No] = z.PO_NO\r\n"
//      		+ "  inner join [SOR_PRODUCTION].[dbo].[POLineItems] as a on b.Id = a.[POId] and z.MaterialCode = a.MaterialCode\r\n"
//      		+ "  where SaleOrderId is not null and a.IsActive = 1 and \r\n" 
//      		+ "  	   (CONVERT(date, a.[LastUpdateCFM]) > CONVERT(date, GETDATE()-10)  )\r\n"
//      		+ "  group by  [SO_NO] ,[SO_Line]  ,[CFM]\r\n "
    		  + " SELECT DISTINCT  viewPCMS2.[SO_NO]\r\n"
    		  + "      		      ,viewPCMS2.[SO_Line]  \r\n"
    		  + "				  ,viewPCMS2.CFM_DATE\r\n"
    		  + "      			  --,	CONVERT(date, [CFM])    AS [CFM]\r\n"
    		  + "      			  , POLI.[LastUpdateCFM]  as [LastUpdateCFM]  \r\n"
    		  + "      		  FROM [SOR_PRODUCTION].[dbo].[V_PCMS2]  as viewPCMS2   \r\n"
    		  + "      		  inner join [SOR_PRODUCTION].[dbo].[PurchaseOrders] as PO on PO.[No] = viewPCMS2.PO_NO\r\n"
    		  + "      		  inner join [SOR_PRODUCTION].[dbo].[POLineItems] as POLI on PO.Id = POLI.[POId] and viewPCMS2.MaterialCode = POLI.MaterialCode\r\n"
    		  + "      		  where SaleOrderId is not null and POLI.IsActive = 1 and  \r\n"
    		  + "      		  	   (CONVERT(date, POLI.[LastUpdateCFM]) > CONVERT(date, GETDATE()-1)  )\r\n"
    		  + " "
      		+ "  "; 
      List<Map<String, Object>> datas = this.databaseSor.queryList(sql);
      list = new ArrayList<SORDetail>();
      for (Map<String, Object> map : datas) {
    	  list.add(this.bcModel._genSORDetail(map));
      }
      return list;
   }
 
}
