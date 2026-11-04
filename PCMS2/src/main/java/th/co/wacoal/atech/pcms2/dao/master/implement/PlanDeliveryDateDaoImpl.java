package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.PlanDeliveryDateDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;

@Repository // Spring annotation to mark this as a DAO component
public class PlanDeliveryDateDaoImpl implements  PlanDeliveryDateDao{
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
    public PlanDeliveryDateDaoImpl(@Qualifier("pcmsDatabase")JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public ArrayList<InputDateDetail> getCountDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String deliveryDateSafe = (bean.getDeliveryDate() == null ? "" : bean.getDeliveryDate().replace("'", "''"));
		String sql =
				    " SELECT distinct count(a.[ProductionOrder]) as countAll \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanDeliveryDate] as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanDeliveryDate] \r\n"
		 		  + "			  group by [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) as b \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[PlanDate] = CONVERT(DATE,'"+deliveryDateSafe+ "',103) ";

		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	@Override
	public ArrayList<InputDateDetail> getMaxDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String saleLine = bean.getSaleLine() ;
		String productionOrderSafe = (bean.getProductionOrder() == null ? "" : bean.getProductionOrder().replace("'", "''"));
		String saleOrderSafe = (bean.getSaleOrder() == null ? "" : bean.getSaleOrder().replace("'", "''"));
		String saleLineSafe = (saleLine == null ? "" : saleLine.replace("'", "''"));
		String deliveryDateSafe = (bean.getDeliveryDate() == null ? "" : bean.getDeliveryDate().replace("'", "''"));
		String sql =
				    " SELECT distinct \r\n"
				  + "       a.[ProductionOrder]  \r\n"
				  + "      ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] ,[CreateBy]\r\n"
    		  + "      ,[CreateDate] \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and \r\n"
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[ProductionOrder] = '" + productionOrderSafe  + "' and \r\n"
		 		  + "       a.[SaleOrder] = '" + saleOrderSafe + "' and \r\n"
		 		  + "       a.[SaleLine] = '" + saleLineSafe+ "' and \r\n"
		  		  + "       a.[PlanDate] = CONVERT(DATE,'"+deliveryDateSafe+ "',103) ";

		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
}
