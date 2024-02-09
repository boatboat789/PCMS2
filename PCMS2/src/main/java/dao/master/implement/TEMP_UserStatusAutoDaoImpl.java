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
import dao.master.SwitchProdOrderDao;
import dao.master.TEMP_UserStatusAutoDao;
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
import entities.TempUserStatusAutoDetail;
import entities.WaitTestDetail;
import entities.WorkInLabDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class TEMP_UserStatusAutoDaoImpl implements  TEMP_UserStatusAutoDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public TEMP_UserStatusAutoDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	} 

	@Override
	public ArrayList<TempUserStatusAutoDetail> getTempUserStatusAutoDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<TempUserStatusAutoDetail> list = null;
		//		String prdOrder = "";
		PCMSSecondTableDetail bean = poList.get(0);
		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		String saleLine = bean.getSaleLine();
		saleLine = String.format("%06d", Integer.parseInt(saleLine));  
		String sql = "SELECT distinct  [Id]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrderRPM]\r\n"
				+ "      ,[Volumn]\r\n"
				+ "      ,[Grade]\r\n"
				+ "      ,[UserStatusCal]\r\n"
				+ "      ,[UserStatusCalRP]\r\n"
				+ "      ,[DataStatus]\r\n"
				+ "      ,[ChangeDate]\r\n"
				+ "      ,[CreateDate]\r\n" 
				+ "  FROM [PCMS].[dbo].[TEMP_UserStatusAuto] \r\n"
				+ "  WHERE ( GRADE IS NULL OR GRADE = '' ) and\r\n"
				+ "		ProductionOrder = '"+prdOrder+"' and \r\n"
				+ "		SaleOrder = '"+saleOrder+"' and\r\n"
				+ "		SaleLine = '"+saleLine+"' and\r\n"
				+ "		DataStatus = 'O'";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<TempUserStatusAutoDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempUserStatusAutoDetail(map));
		}
		return list;
	}  
}
