	package dao.master.implement;

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

import dao.PCMSMainDao;
import dao.master.FromSORCFMDao;
import entities.CFMDetail;
import entities.ColumnHiddenDetail;
import entities.ConfigCustomerUserDetail;
import entities.DyeingDetail;
import entities.FinishingDetail;
import entities.InputDateDetail;
import entities.InspectDetail;
import entities.NCDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import entities.PODetail;
import entities.PackingDetail;
import entities.PresetDetail;
import entities.ReceipeDetail;
import entities.SORDetail;
import entities.SaleDetail;
import entities.SaleInputDetail;
import entities.SendTestQCDetail;
import entities.WaitTestDetail;
import entities.WorkInLabDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class FromSORCFMDaoImpl implements  FromSORCFMDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public FromSORCFMDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	} 

	@Override
	public String upSertFromSORCFMDetail(ArrayList<SORDetail> list) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		Timestamp dateTime = new Timestamp(time);
		String saleLine = "", cfmDate = "";
		String iconStatus = "I";
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
		try {
			prepared = connection.prepareStatement(sql);
			for (i = 0; i < list.size(); i++) {
				SORDetail bean = list.get(i);
				saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
				cfmDate = bean.getCfmDate();
				int index = 1;
				prepared = this.sshUtl.setSqlDate(prepared, cfmDate, index);
				index += 1;
				prepared.setTimestamp(index, dateTime);
				index += 1;
				prepared.setString(index, bean.getSaleOrder());
				index += 1;
				prepared.setString(index, saleLine);
				index += 1;
				prepared.setString(index, bean.getSaleOrder());
				index += 1;
				prepared.setString(index, saleLine);
				index += 1;
				prepared = this.sshUtl.setSqlDate(prepared, cfmDate, index);
				index += 1;
				prepared.setTimestamp(index, dateTime);
				index += 1;
				prepared.addBatch();
			}
			prepared.executeBatch();
		} catch (SQLException e) {
			System.err.println("insertLabNoDetail" + e.getMessage());
			iconStatus = "E";
		}
		return iconStatus;
	}
}
