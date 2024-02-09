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
import dao.master.PlanSendCFMCusDateDao;
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

public class PlanSendCFMCusDateDaoImpl implements  PlanSendCFMCusDateDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public PlanSendCFMCusDateDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	} 
	@Override
	public ArrayList<InputDateDetail> getSendCFMCusDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				  " SELECT [ProductionOrder]\r\n" 
			    + "       ,[SendCFMCusDate] as [PlanDate]\r\n"
			    + "       ,[ChangeBy] as [CreateBy]\r\n"
			    + "       ,[ChangeDate] as [CreateDate]\r\n"
			    + "	  	 ,CASE \r\n"
			    + "			WHEN [OperationDyeDate] is not null then '0:PCMS' "
			    + "			ELSE '1:PPMM' "
			    + "			end as InputFrom \r\n" 
			    + "       , LotNo \r\n"
			    + " FROM [PCMS].[dbo].[PlanSendCFMCusDate] as a\r\n" 
			  	+ " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' \r\n"  
			  	+ " ORDER BY  [ChangeDate] desc ";
				  
		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;   
	}
}
