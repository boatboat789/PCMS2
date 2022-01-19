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

import dao.PCMSDetailDao;
import entities.ColumnHiddenDetail;
import entities.InputDateDetail;
import entities.PCMSAllDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

public class PCMSDetailDaoImpl implements PCMSDetailDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
    private String select = 
    		  " a.SaleOrder \r\n"
    		+ "	  ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
    		+ "		WHEN 0 THEN ''  \r\n"
    		+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
    		+ "		END AS [SaleLine] ,\r\n"
    		+ "	  Division,\r\n"  
    		+ "CustomerShortName,	\r\n"
    		+ "SaleCreateDate,\r\n"
    		+ "PurchaseOrder,\r\n"
    		+ "MaterialNo,\r\n"
    		+ "CustomerMaterial,\r\n"
    		+ "Price,\r\n"
    		+ "SaleUnit,\r\n"
    		+ "OrderAmount,\r\n"
    		+ "SaleQuantity,\r\n"
    		+ "RemainQuantity,\r\n"
    		+ "RemainAmount,\r\n"
    		+ "TotalQuantity,\r\n"
    		+ "c.Grade,\r\n"   
    		+ "c.BillSendWeightQuantity,\r\n"
    		+ "c.BillSendQuantity,\r\n"  
    		+ "CustomerDue,\r\n"
    		+ "DueDate,\r\n"  
    		+ "LotNo,\r\n"
    		+ "b.LabNo,\r\n" 
    		+ "LabStatus,\r\n"
    		+ "e.CFMPlanLabDate,\r\n"
    		+ " lb.[CFMDate]  as CFMActualLabDate,\r\n"
    		+ " lb.[CFMAnswerDate] as CFMCusAnsLabDate,\r\n"
    		+ "UserStatus ,\r\n"
    		+ "'' as TKCFM,\r\n" 
    		+ "CASE  \r\n"
    		+ "		WHEN g.CFMPlanDate is not null THEN g.CFMPlanDate  \r\n"
    		+ "		ELSE f.CFMPlanDateOne \r\n"
    		+ "		END AS CFMPlanDate ,  \r\n"
    		+ " CASE  \r\n"
    		+ "		WHEN h.DeliveryDate is not null THEN H.DeliveryDate  \r\n"
    		+ "		ELSE b.CFTYPE \r\n"
    		+ "		END AS DeliveryDate , \r\n"
    		+ "d.CFMSendDate,\r\n" 
    		+ "d.CFMAnswerDate,\r\n"
    		
    		
//    		+ "CFMNumber,\r\n"
//    		+ "CFMStatus,\r\n"
//    		+ "CFMRemark,\r\n" 

    		+ "CASE  \r\n"
    		+ "		WHEN i.CFMStatus is not null THEN d.CFMStatus  \r\n"
    		+ "		ELSE i.CFMStatus\r\n"
    		+ "		END AS CFMStatus ,  \r\n"
    		+ "CASE  \r\n"
    		+ "		WHEN i.CFMStatus is null THEN d.CFMNumber  \r\n"
    		+ "		ELSE i.CFMNumber\r\n"
    		+ "		END AS CFMNumber ,  \r\n"
    		+ "CASE  \r\n"
    		+ "		WHEN i.CFMStatus is null THEN d.CFMRemark  \r\n"
    		+ "		ELSE i.CFMRemark\r\n"
    		+ "		END AS CFMRemark ,  \r\n"
    		
    		+ "ShipDate,\r\n"
    		+ "RemarkOne,\r\n"
    		+ "RemarkTwo,\r\n"
    		+ "RemarkThree ,\r\n"
    		+ "b.ProductionOrder \r\n"; 
        
    private String leftJoinB = " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n";
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");  
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm"); 
	public SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String leftJoinC = " left join [PCMS].[dbo].[FromSapMainGrade] as c on b.ProductionOrder = c.ProductionOrder 	 \r\n"; 
	private String leftJoinX = " left join [PCMS].[dbo].[FromSapMainProdSale] as x on a.SaleLine = x.SaleLine and x.SaleOrder = a.SaleOrder  	 \r\n";
	private String leftJoinCFMLastD = 
    		  "   left join ( SELECT distinct a.[ProductionOrder]\r\n"    
    		+ "					  ,[CFMNo]\r\n"
    		+ "					  ,[CFMNumber]\r\n"
    		+ "					  ,[CFMSendDate]\r\n"
    		+ "					  ,[CFMAnswerDate]\r\n"
    		+ "					  ,[CFMStatus] \r\n"
    		+ "					  ,[CFMRemark]\r\n"
    		+ "				FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
    		+ "				inner join (select distinct  ProductionOrder ,   max([CFMNo]) as maxCFMNo  \r\n"
    		+ "					        from [PCMS].[dbo].[FromSapCFM] \r\n"
    		+ "							where  DataStatus <> 'X'  and [CFMNumber] <> '' \r\n"
    		+ "					 		group by ProductionOrder )as b on \r\n"
    		+ "                         a.[ProductionOrder] = b.[ProductionOrder] and  a.[CFMNo] = b.[maxCFMNo]  \r\n"
    		+ "             where  DataStatus <> 'X'  and [CFMNumber] <> '' \r\n"
    		+ "			) as d on b.ProductionOrder = d.ProductionOrder \r\n";  
	private String leftJoinCFMLastNoStatI = 
  		  "     left join ( SELECT distinct a.[ProductionOrder]\r\n"
  		  + "					  ,[CFMNo]\r\n"
  		  + "					  ,[CFMNumber]\r\n"
  		  + "					  ,[CFMSendDate]\r\n"
  		  + "					  ,[CFMAnswerDate]\r\n"
  		  + "					  ,[CFMStatus] \r\n"
  		  + "					  ,[CFMRemark]\r\n"
  		  + "				FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
  		  + "				inner join (select distinct  ProductionOrder ,   max([CFMNo]) as maxCFMNo  \r\n"
  		  + "					        from [PCMS].[dbo].[FromSapCFM] \r\n"
  		  + "							where  DataStatus <> 'X' and CFMStatus = ''"
  		  + "								 and [CFMNumber] <> ''\r\n"
  		  + "					 		group by ProductionOrder )as b on \r\n"
  		  + "                         a.[ProductionOrder] = b.[ProductionOrder] and  a.[CFMNo] = b.[maxCFMNo]  \r\n"
  		  + "             where  DataStatus <> 'X' and CFMStatus = '' and [CFMNumber] <> ''\r\n"
  		  + "			) as i on b.ProductionOrder = i.ProductionOrder  \r\n";  
	private String leftJoinE = 
  		  	" left join ( SELECT distinct  a.[ProductionOrder] \r\n"
  		  + "  			 ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS CFMPlanLabDate \r\n"
  		  + "			FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
  		  + "			inner join (select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
  		  + "				FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
  		  + "				group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
  		  + "				on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
  		  + "				and a.[CreateDate] = b.[MaxCreateDate] \r\n"
  		+ "			) as e on e.ProductionOrder = b.ProductionOrder and e.SaleOrder = a.SaleOrder and e.SaleLine = a.SaleLine\r\n"  ;   
	private String leftJoinF = 
  		  	" left join (SELECT distinct  a.[ProductionOrder] \r\n"
  		  	+ "			,max(a.SubmitDate) as CFMPlanDateOne\r\n"
  		  	+ "			FROM [PCMS].[dbo].[FromSapSubmitDate]  as a \r\n"
  		  	+ "         where [DataStatus] = 'O' "
  		  	+ "			group by a.[ProductionOrder] ) as f on b.ProductionOrder = f.ProductionOrder \r\n"  ;   
	private String leftJoinG = 
  		  	" left join ( SELECT distinct    a.[ProductionOrder] \r\n"  
  		  	+ "  			 ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS CFMPlanDate \r\n"
  		  	+ "			FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
  		  	+ "			inner join (select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
  		  	+ "				FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
  		  	+ "				group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
  		  	+ "				on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
  		  	+ "				and a.[CreateDate] = b.[MaxCreateDate] \r\n"
  		  	+ "			) as g on g.ProductionOrder = b.ProductionOrder and g.SaleOrder = a.SaleOrder and g.SaleLine = a.SaleLine\r\n"  ;   
	private String leftJoinH = 
  		  	" left join ( SELECT distinct    a.[ProductionOrder] \r\n"
  		  	+ "  			 ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
  		  	+ "			FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
  		  	+ "			inner join (select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
  		  	+ "				FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
  		  	+ "				group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
  		  	+ "				on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
  		  	+ "				and a.[CreateDate] = b.[MaxCreateDate] \r\n"
  		  	+ "			) as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n"  ;   
    private String leftJoinZ = 
		      " left join (SELECT distinct  [ProductionOrder]\r\n      "
			+ "                 ,[SaleOrder]\r\n      " 
		    + "                 ,[SaleLine]\r\n      "
			+ "                 ,max([LotShipping]) as [LotShipping]\r\n  "
			+ "            FROM [PCMS].[dbo].[FromSapMainGrade]\r\n  "
			+ "            GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) \r\n  "
			+ "  as z on z.ProductionOrder = b.ProductionOrder \r\n      and z.SaleLine = b.SaleLine \r\n      and z.SaleOrder = b.SaleOrder \r\n";
    private String leftJoinLB = 
    		  "  left join ( \r\n"
    		+ "			 SELECT distinct [LabNo]  ,[CFMDate] ,max([CFMAnswerDate])  as [CFMAnswerDate] \r\n"
    		+ "			  FROM [LBMS].[dbo].[SaleCFMDetail] as a\r\n"
    		+ "			  inner join (select distinct   [SaleSet] ,max([LabWorkProcessNo])  as [LabWorkProcessNo]   \r\n"
    		+ "	 							FROM [LBMS].[dbo].[SaleCFMDetail]     \r\n"
    		+ "	 							group by [SaleSet]   ) as b on a.[SaleSet] = b.[SaleSet]  and a.LabWorkProcessNo = b.[LabWorkProcessNo]\r\n"
    		+ "			  where a.saleset <> '' and SampleNo <> ''  \r\n"
    		+ "			  group by [LabNo] , [CFMDate]  ) AS lb on b.LabNo = lb.LabNo    \r\n";
	public PCMSDetailDaoImpl(Database database) {
		this.database = database;  
		this.message = "";     
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSSecondTableDetail> list = null;        
		String where = " where  "; 
		String customerShortName ="", saleNumber = "" , materialNo = "",saleOrder = "", saleLine = "",
		saleCreateDate = "",labNo = "" ,articleFG = "",designFG = "",userStatus = "", prdOrder= "",
		prdCreateDate = "",deliveryStatus = "",saleStatus ="",dist="",customerName = ""; 
		PCMSTableDetail bean = poList.get(0);
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
		saleNumber = bean.getSaleNumber() ; 
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();  
		saleCreateDate = bean.getSaleOrderCreateDate();	  
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		designFG = bean.getDesignFG();
		userStatus = bean.getUserStatus();
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel(); 
		where +=  " CustomerName like '%" + customerName + "%' and\r\n"
				+ " CustomerShortName like '%" + customerShortName + "%' and \r\n" 
				  + " MaterialNo like '"+materialNo+"%' and\r\n"
				  + " a.SaleOrder like '"+saleOrder+"%' \r\n"
//				  + " a.SaleLine like '"+saleLine+"%' and\r\n"
				  ;   	
		if(!saleCreateDate.equals("")) {    
			
			String[] dateArray = saleCreateDate.split("-");
			where += "and ( SaleCreateDate >= CONVERT(DATE,'"+ dateArray[0].trim() +"',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'"+ dateArray[1].trim() +"',103) ) \r\n" ;
		}
		
		if(!saleNumber.equals("")) { where += " and a.SaleNumber like '"+saleNumber+"%' \r\n" ; }
		if(!labNo.equals("")) { where += " and b.LabNo like '"+labNo+"%'  \r\n" ; }
		if(!articleFG.equals("")) { where += " and a.ArticleFG like '"+articleFG+"%'  \r\n" ; } 	
		if(!designFG.equals("")) { where += " and a.DesignFG like '"+designFG+"%'  \r\n" ; }
		if(!userStatus.equals("")) { where += " and b.UserStatus like '"+userStatus+"%'  \r\n" ; }
		if(!prdOrder.equals("")) { where += " and b.ProductionOrder like '"+prdOrder+"%'  \r\n " ; }
		if(!prdCreateDate.equals("")) {  
			String[] dateArray = prdCreateDate.split("-");
			where += " and ( PrdCreateDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " PrdCreateDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
		}
		if(!deliveryStatus.equals("")) { where += " and a.DeliveryStatus like '"+deliveryStatus+"%'  \r\n" ; } 
		if(!saleStatus.equals("")) { where += " and a.SaleStatus like '"+saleStatus+"%'  \r\n" ; } 
		if(!dist.equals("")) { 
			String[] array = dist.split(",");   
			where += " and  ( " ; 
			for(int i = 0 ;i< array.length ;i++) {     
				where += " a.DistChannel = '"+array[i]+"' " ;    	
				if(i != array.length - 1) { where += " or " ;};
			}
			where += " ) \r\n" ; 
		}    
		String sql = "SELECT DISTINCT  "       
					+ this.select
					+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n " 
					+ this.leftJoinB 
					+ this.leftJoinC	
					+ this.leftJoinX
					+ this.leftJoinCFMLastD    
					+ this.leftJoinE
					+ this.leftJoinF
					+ this.leftJoinG
					+ this.leftJoinH
					+ this.leftJoinCFMLastNoStatI
					+ this.leftJoinLB  
					+ where  
					+ " and c.DataStatus <> 'X' and x.ProductionOrder is null "
					+ " union " 
					+  " SELECT DISTINCT  "       
					+ this.select
					+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
					+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
					+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
					+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount \r\n"
					+ "  ,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
					+ "		 from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
					+ "		 inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
					+ "		 where b.DataStatus <> 'X') as a  \r\n " 
//					+ this.leftJoinB 
					+ "  left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"  
					+ this.leftJoinC	      
					+ this.leftJoinCFMLastD   
					+ this.leftJoinE   
					+ this.leftJoinF
					+ this.leftJoinG   
					+ this.leftJoinH
					+ this.leftJoinCFMLastNoStatI
					+ this.leftJoinLB
					+ where  
					+ " and c.DataStatus <> 'X' "      
					+ " Order by a.SaleOrder, SaleLine,b.LotNo,c.Grade ";      
//		System.out.println(sql 	);     
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		list = new ArrayList<PCMSSecondTableDetail>(); 
		for (Map<String, Object> map : datas) {  
			list.add(this.bcModel._genPCMSSecondTableDetail(map));   	
		}       
//		System.out.println("OH IT'K");
		return list;	
	} 
 
	 
	@Override
	public ArrayList<PCMSTableDetail> getSaleNumberList() {
		ArrayList<PCMSTableDetail> list = null;
		String sql = "SELECT DISTINCT  " + "	   a.SaleNumber\r\n"
				+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" + "    		 WHEN 0 THEN ''   \r\n"
				+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+[SaleFullName]\r\n"
				+ "    		 END AS [SaleFullName]   " + "   FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n "
				+ " where SaleNumber <> '00000000'" + " Order by [SaleNumber]";
//		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();  
		String fromTable = "";
		int check = 0;
		PCMSSecondTableDetail bean = poList.get(0);
		String caseSave = bean.getCaseSave();
		String planDate = "";
		java.util.Date today = new java.util.Date();
		String todayString=sdf3.format(today);
		if(caseSave.equals("CFMPlanLabDate")) { 
			planDate = bean.getCFMPlanLabDate();
			fromTable = " [PCMS].[dbo].[PlanCFMLabDate] "  ;
			check = getMaxCFMPlanLabDateDetail(poList).size();
			} 
		else if(caseSave.equals("CFMPlanDate")) { 
			planDate = bean.getCFMPlanDate();
			fromTable = "[PCMS].[dbo].[PlanCFMDate] "  ;
			check = getMaxCFMPlanDateDetail(poList).size();} 
		else if(caseSave.equals("DeliveryDate")) { 
			planDate = bean.getDeliveryDate();
			fromTable = "[PCMS].[dbo].[PlanDeliveryDate] "  ;
			check = getMaxDeliveryPlanDateDetail(poList).size();} 
//		System.out.println(todayString+ " "+insertTable);
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		ArrayList<InputDateDetail> listInput = new ArrayList<InputDateDetail>();
		InputDateDetail beanInput = new InputDateDetail();
		if(check > 0) {
			beanInput.setIconStatus("I");
			beanInput.setSystemStatus("Date : "+planDate+" already confirm.Try to refresh again.");
		}
		else {
			try {    
					String sql = 
							  " insert into "
							+ fromTable
							+ " ( "
							+ "		[ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[PlanDate]  ,[CreateBy]  , "  //5
							+ "		[CreateDate] " 
							+ "     ) "//24   
							+ " 	values(? , ? , ? , ? , ?"//  1
							+ "			  ,?  "
							+ " ) ;"; 
						java.util.Date date = sdf2.parse(planDate);
						prepared = connection.prepareStatement(sql);   
						prepared.setString(1, bean.getProductionOrder());
						prepared.setString(2, bean.getSaleOrder());
						prepared.setString(3, saleLine); 
						prepared.setDate(4, convertJavaDateToSqlDate(date) );
						prepared.setString(5, bean.getUserId());
						prepared.setTimestamp(6, new Timestamp(time));; 			
	//					prepared.setString(6, todayString);   			
						prepared.executeUpdate();  
						beanInput.setIconStatus("I");
						beanInput.setSystemStatus("Update Success.");
						
			}catch (SQLException | ParseException e) {
				System.out.println(e.getMessage());
				beanInput.setIconStatus("E");
				beanInput.setSystemStatus("Something happen, Please contact IT.");
			} 
		}    
		listInput.add(beanInput);
		return listInput;
	}
	@Override
	public ArrayList<InputDateDetail> getCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				    " SELECT [ProductionOrder]\r\n"
				    + "      ,[SaleOrder]\r\n"
				    + "      ,[SaleLine]\r\n"
				    + "      ,[PlanDate]\r\n"
				    + "      ,[CreateBy]\r\n"
				    + "      ,[CreateDate]\r\n"
				    + "	  , '0:PCMS' as InputFrom \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n" 
		 		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and "
		 		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and "
		 		  + "       a.[SaleLine] = '" +  saleLine+ "' " 
		 		  + "   ORDER BY InputFrom ,CreateDate desc ";
				 
		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	} 
	@Override
	public ArrayList<InputDateDetail> getCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql =  
		 " SELECT [ProductionOrder]\r\n"
		    + "      ,[SaleOrder]\r\n"
		    + "      ,[SaleLine]\r\n"
		    + "      ,[PlanDate]\r\n"
		    + "      ,[CreateBy]\r\n"
		    + "      ,[CreateDate]\r\n"
		    + "	  , '1:PCMS' as InputFrom \r\n"
		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n" 
		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and "
		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and "
		  + "       a.[SaleLine] = '" + saleLine+ "' "
		  + " union \r\n "
		  + " SELECT [ProductionOrder]\r\n"
		    + "      ,[SaleOrder]\r\n"
		    + "      ,[SaleLine]\r\n"
		    + "      ,SubmitDate as [PlanDate]\r\n"  
		    + "      ,'' AS [CreateBy]\r\n"
		    + "      ,null AS [CreateDate]\r\n"
		    + "	    , '0:SAP' as InputFrom \r\n"
		  + " FROM [PCMS].[dbo].[FromSapSubmitDate]  as a\r\n" 
		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "'    and SubmitDate is not null "
  		  + "   and [DataStatus] = 'O' "
		  + "   ORDER BY InputFrom ,CreateDate ";
				
		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	} 
	@Override
	public ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				" SELECT [ProductionOrder]\r\n"
					    + "      ,[SaleOrder]\r\n"
					    + "      ,[SaleLine]\r\n"
					    + "      ,[PlanDate]\r\n"
					    + "      ,[CreateBy]\r\n"
					    + "      ,[CreateDate]\r\n"
					    + "	  , '0:PCMS' as InputFrom \r\n"
					  + "  FROM [PCMS].[dbo].[PlanDeliveryDate] as a\r\n" 
					  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and "
					  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and "
					  + "       a.[SaleLine] = '" + saleLine+ "' "
					  + " union \r\n "
					  + " SELECT [ProductionOrder]\r\n"
					    + "      ,[SaleOrder]\r\n"
					    + "      ,[SaleLine]\r\n"
					    + "      ,[CFType] as [PlanDate]\r\n"
					    + "      ,'' AS [CreateBy]\r\n"
					    + "      ,null AS [CreateDate]\r\n"
					    + "	    , '1:SAP' as InputFrom \r\n"
					  + " FROM [PCMS].[dbo].[FromSapMainProd]  as a\r\n" 
					  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "'   and CFType is not null  "
					  + "   ORDER BY InputFrom ,CreateDate desc ";
				  
		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;   
	} 
	public ArrayList<InputDateDetail> getMaxCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				    " SELECT distinct a.[ProductionOrder]  ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] ,[CreateBy]\r\n"
	    		  + "      ,[CreateDate] \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and "
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and "
		 		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and "
		 		 + "       a.[SaleLine] = '" + saleLine+ "' and"
		  		  + "       a.[PlanDate] = CONVERT(DATE,'"+bean.getCFMPlanLabDate()+ "',103)  ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	public ArrayList<InputDateDetail> getMaxDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				    " SELECT distinct a.[ProductionOrder]  ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] ,[CreateBy]\r\n"
	    		  + "      ,[CreateDate] \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and "
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder()  + "' and "
		 		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and "
		 		 + "       a.[SaleLine] = '" + saleLine+ "' and"
		  		  + "       a.[PlanDate] = CONVERT(DATE,'"+bean.getDeliveryDate()+ "',103) ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	public ArrayList<InputDateDetail> getMaxCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		String sql = 
				    " SELECT distinct a.[ProductionOrder]  ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] ,[CreateBy]\r\n"
	    		  + "      ,[CreateDate] \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and "
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder()+ "' and "
		 		  + "       a.[SaleOrder] = '" + bean.getSaleOrder() + "' and "
		 		  + "       a.[SaleLine] = '" + saleLine+ "' and"
 		  		  + "       a.[PlanDate] = CONVERT(DATE,'"+bean.getCFMPlanDate()+ "',103) ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);  
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	@Override
	public ArrayList<ColumnHiddenDetail> getColHiddenDetail(String user) {
		ArrayList<ColumnHiddenDetail> list = null;  
		String sql = 
				    " SELECT distinct [EmployeeID] ,[ColName] \r\n"
		 		  + " FROM [PCMS].[dbo].[ColumnSetting] \r\n "
		 		  + " where [EmployeeID] = '" + user+ "' ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);  
		list = new ArrayList<ColumnHiddenDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genColumnHiddenDetail(map));
		}
		return list;
	}  

	@Override
	public ArrayList<ColumnHiddenDetail> saveColSettingToServer(ColumnHiddenDetail pd) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String colName = pd.getColName(); 
		String user = pd.getUserId(); 
		ArrayList<ColumnHiddenDetail> list = new ArrayList<ColumnHiddenDetail>();
		ColumnHiddenDetail bean = new ColumnHiddenDetail();
//		ArrayList<ColumnHiddenDetail> beanCheck = this.getColHiddenDetail(leftJoinB);
//		if(beanCheck.size() > 0) { 
//		} 
		try {      
			String sql = 
					"UPDATE [PCMS].[dbo].[ColumnSetting] "
					+ " SET [ColName] = ?  "
					+ " WHERE [EmployeeID]  = ? "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ "  if @rc <> 0 " 
					+ " print @rc " 
					+ " else "
					+ " INSERT INTO [PCMS].[dbo].[ColumnSetting]	 "
					+ " ([EmployeeID] ,[ColName])"//55 
					+ " values(? , ? )  ;"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, colName);
				prepared.setString(2, user);
				prepared.setString(3, user);
				prepared.setString(4, colName);  
				prepared.executeUpdate();   
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.out.println("insertLabNoDetail"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getUserStatusList() {
		ArrayList<PCMSAllDetail> list = null;
		String where = " where UserStatus <> '' \r\n"; 
		String sql = 
				  "SELECT distinct [UserStatus] \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainProd] \r\n "
			    + where
				+ " order by UserStatus \r\n"; 
		  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}

}
