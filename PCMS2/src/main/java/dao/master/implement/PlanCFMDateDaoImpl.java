	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.PlanCFMDateDao;
import entities.InputDateDetail;
import entities.PCMSSecondTableDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class PlanCFMDateDaoImpl implements  PlanCFMDateDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public PlanCFMDateDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public ArrayList<InputDateDetail> getCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String sql =
			  " SELECT \r\n"
			+ "		  [ProductionOrder]\r\n"
		    + "      ,[SaleOrder]\r\n"
		    + "      ,[SaleLine]\r\n"
		    + "      ,[PlanDate]\r\n"
		    + "      ,[CreateBy]\r\n"
		    + "      ,[CreateDate]\r\n"
		    + "	 	 , '1:PCMS' as InputFrom \r\n"
		    + "   	 , LotNo \r\n"
		    + " FROM [PCMS].[dbo].[PlanCFMDate] as a\r\n"
		    + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and \r\n"
		    + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
		    + "       a.[SaleLine] = '" + saleLine+ "' \r\n"
//  		  + "   and [DataStatus] = 'O' "
		  + " ORDER BY InputFrom ,CreateDate desc ";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	@Override
	public ArrayList<InputDateDetail> getCountCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String sql =
				    " SELECT distinct count(a.[ProductionOrder]) as countAll  \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[PlanDate] = CONVERT(DATE,'"+bean.getCfmPlanDate()+ "',103) ";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	@Override
	public ArrayList<InputDateDetail> getMaxCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String sql =
				    " SELECT distinct \r\n"
				  + " 		a.[ProductionOrder]  ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] ,[CreateBy]\r\n"
	    		  + "      ,[CreateDate] \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder()+ "' and \r\n"
		 		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and \r\n"
		 		  + "       a.[SaleLine] = '" + saleLine+ "' and \r\n"
 		  		  + "       a.[PlanDate] = CONVERT(DATE,'"+bean.getCfmPlanDate()+ "',103) \r\n";

		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
}
