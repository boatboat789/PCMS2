package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.PlanCFMLabDateDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class PlanCFMLabDateDaoImpl implements  PlanCFMLabDateDao{
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	@SuppressWarnings("unused")
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private BeanCreateService bcModel = new BeanCreateService();
	private JdbcTemplate jdbc;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	@Autowired
    public PlanCFMLabDateDaoImpl(@Qualifier("pcmsDatabase")JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public ArrayList<InputDateDetail> getCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String saleLine = bean.getSaleLine() ;
		String productionOrderSafe = (bean.getProductionOrder() == null ? "" : bean.getProductionOrder().replace("'", "''"));
		String saleOrderSafe = (bean.getSaleOrder() == null ? "" : bean.getSaleOrder().replace("'", "''"));
		String saleLineSafe = (saleLine == null ? "" : saleLine.replace("'", "''"));
		String sql =
				      " SELECT \r\n"
		    		+ "		  [ProductionOrder]\r\n"
				    + "      ,[SaleOrder]\r\n"
				    + "      ,[SaleLine]\r\n"
				    + "      ,[PlanDate]\r\n"
				    + "      ,[CreateBy]\r\n"
				    + "      ,[CreateDate]\r\n"
				    + "	  	 ,'0:PCMS' as InputFrom \r\n"
				    + "      ,LotNo \r\n"
				    + " FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
				    + " where a.[ProductionOrder] = '" + productionOrderSafe + "' and \r\n"
				    + "       a.[SaleOrder] = '" + saleOrderSafe + "' and \r\n"
				    + "       a.[SaleLine] = '" +  saleLineSafe+ "' \r\n"
		 		  	+ " ORDER BY InputFrom ,CreateDate desc ";
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputDateDetail> getMaxCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String saleLine = bean.getSaleLine() ;
		String productionOrderSafe = (bean.getProductionOrder() == null ? "" : bean.getProductionOrder().replace("'", "''"));
		String saleOrderSafe = (bean.getSaleOrder() == null ? "" : bean.getSaleOrder().replace("'", "''"));
		String saleLineSafe = (saleLine == null ? "" : saleLine.replace("'", "''"));
		String cfmPlanLabDateSafe = (bean.getCfmPlanLabDate() == null ? "" : bean.getCfmPlanLabDate().replace("'", "''"));
		String sql =
				    " SELECT distinct \r\n"
				  + "		 a.[ProductionOrder] ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] ,[CreateBy]\r\n"
    		  + "      	,[CreateDate] \r\n"
	 		  + " FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
	 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
	 		  + "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
	 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
	 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
	 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and \r\n"
	 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
	 		  + " where a.[ProductionOrder] = '" + productionOrderSafe + "' and \r\n"
	 		  + "       a.[SaleOrder] = '" + saleOrderSafe + "' and \r\n"
	 		  + "       a.[SaleLine] = '" + saleLineSafe+ "' and \r\n"
	  		  + "       a.[PlanDate] = CONVERT(DATE,'"+cfmPlanLabDateSafe+ "',103)  ";

		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	@Override
	public ArrayList<InputDateDetail> getCountCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String cfmPlanLabDateSafe = (bean.getCfmPlanLabDate() == null ? "" : bean.getCfmPlanLabDate().replace("'", "''"));
		String sql =
				    " SELECT distinct \r\n"
				  + "		count(a.[ProductionOrder]) as countAll \r\n"
	 		  + " FROM [PCMS].[dbo].[PlanCFMLabDate] as a\r\n"
	 		  + " inner join (select distinct [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,max([CreateDate]) as [MaxCreateDate]\r\n"
	 		  + "			  FROM [PCMS].[dbo].[PlanCFMLabDate] \r\n"
	 		  + "			  group by [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) as b \r\n"
	 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
	 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
	 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
	 		  + " where a.[PlanDate] = CONVERT(DATE,'"+cfmPlanLabDateSafe+ "',103) ";

		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
}
