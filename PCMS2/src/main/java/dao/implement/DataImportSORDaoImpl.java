package dao.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	@SuppressWarnings("unused")
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
	      @SuppressWarnings("unused")
		String value = fscModel.upSertFromSORCFMDetail(list);
   }
   private ArrayList<SORDetail> getSORdetail() {
      ArrayList<SORDetail> list = null;
      String sql = ""
    		  + " SELECT DISTINCT  viewPCMS2.[SO_NO]\r\n"
    		  + "      		      ,viewPCMS2.[SO_Line]  \r\n"
    		  + "				  ,viewPCMS2.CFM_DATE\r\n"
    		  + "      			  --,	CONVERT(date, [CFM])    AS [CFM]\r\n"
    		  + "      			  , POLI.[LastUpdateCFM]  as [LastUpdateCFM]  \r\n"
    		  + "      		  FROM [SOR_PRODUCTION].[dbo].[V_PCMS2]  as viewPCMS2   \r\n"
    		  + "      		  inner join [SOR_PRODUCTION].[dbo].[PurchaseOrders] as PO on PO.[No] = viewPCMS2.PO_NO\r\n"
    		  + "      		  inner join [SOR_PRODUCTION].[dbo].[POLineItems] as POLI on PO.Id = POLI.[POId] and viewPCMS2.MaterialCode = POLI.MaterialCode\r\n"
    		  + "      		  where SaleOrderId is not null and POLI.IsActive = 1   \r\n"
    		  + "      		  	   and (CONVERT(date, POLI.[LastUpdateCFM]) > CONVERT(date, GETDATE()-1)  )\r\n"
    		  + " "
      		+ "  ";
      List<Map<String, Object>> datas = this.database.queryList(sql);
      list = new ArrayList<>();
      for (Map<String, Object> map : datas) {
    	  list.add(this.bcModel._genSORDetail(map));
      }
      return list;
   }

}
