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
//    		+ "b.Volumn,"          
    		+ "CustomerMaterial,\r\n"
    		+ "Price,\r\n"
    		+ "SaleUnit,\r\n"
    		+ "OrderAmount,\r\n"
    		+ "SaleQuantity,\r\n"
    		+ "RemainQuantity,\r\n"
    		+ "RemainAmount,\r\n"
    		+ "TotalQuantity,\r\n"    
    		+ "m.Grade,\r\n"   
    		+ "c.BillSendWeightQuantity,\r\n"
    		+ "c.BillSendQuantity,\r\n"  
    		+ "c.BillSendMRQuantity,\r\n"  
    		+ "c.BillSendYDQuantity,\r\n"    
    		+ "CustomerDue,\r\n"
    		+ "DueDate,\r\n"  
//    		+ "LotNo,\r\n"
    		+  "  CASE  \r\n"
  			+ "		  WHEN b.[ProductionOrder] is not null THEN b.[LotNo]  \r\n"  
  			+ "		  ELSE 'รอจัด Lot'  \r\n"
  			+ "		  END AS [LotNo] ,\r\n"   
    		+ "b.LabNo,\r\n" 
    		+ "LabStatus,\r\n"
    		+ "e.CFMPlanLabDate,\r\n"
    		+ " lb.[CFMDate]  as CFMActualLabDate,\r\n"
    		+ " lb.[CFMAnswerDate] as CFMCusAnsLabDate,\r\n"
    		+ "UserStatus ,\r\n"
    		+ "j.CFMDate as TKCFM,\r\n" 
    		+ "CASE  \r\n"
    		+ "		WHEN g.[ProductionOrder] is not null THEN g.CFMPlanDate  \r\n"
    		+ "		ELSE f.CFMPlanDateOne \r\n"
    		+ "		END AS CFMPlanDate ,  \r\n"
    		+ " CASE  \r\n"
    		+ "		WHEN h.[ProductionOrder] is not null THEN H.DeliveryDate  \r\n"
    		+ "		ELSE b.CFTYPE \r\n"
    		+ "		END AS DeliveryDate , \r\n"
    		+ "d.CFMSendDate,\r\n" 
    		+ "d.CFMAnswerDate,\r\n"
    		
    		
//    		+ "CFMNumber,\r\n"
//    		+ "CFMStatus,\r\n"
//    		+ "CFMRemark,\r\n" 

    		+ "CASE  \r\n"
    		+ "		WHEN i.CFMStatus is not null THEN i.CFMStatus  \r\n"
    		+ "		ELSE d.CFMStatus\r\n"
    		+ "		END AS CFMStatus ,  \r\n"
    		+ "CASE  \r\n"
    		+ "		WHEN i.CFMStatus is not null THEN i.CFMNumber  \r\n"
    		+ "		ELSE d.CFMNumber\r\n"
    		+ "		END AS CFMNumber ,  \r\n" 
    		+ "CASE  \r\n"
    		+ "		WHEN i.CFMStatus is not null THEN i.CFMRemark  \r\n"
    		+ "		ELSE d.CFMRemark\r\n"
    		+ "		END AS CFMRemark ,  \r\n" 
    		+ "ShipDate,\r\n"
    		+ "RemarkOne,\r\n"
    		+ "RemarkTwo,\r\n"
    		+ "RemarkThree ,\r\n"   
    		+ "ReplacedRemark ,\r\n"
    		+ "StockRemark,"   
    		+ "b.ProductionOrder , GRSumKG\r\n"
    		+ ", GRSumYD\r\n"   
    		+ ", GRSumMR\r\n" 
//    		+ ",PriceSTD * GRSumMR as VolumnFGAmount\r\n"
    		+ ", CASE  \r\n"
    		+ "		WHEN m.Grade = 'A' THEN b.Volumn\r\n"
    		+ "		ELSE  NULL\r\n"
    		+ "		END AS Volumn   \r\n"  
    		+ ", CASE  \r\n"
    		+ "		WHEN m.Grade = 'A' THEN a.Price * b.Volumn \r\n"
    		+ "		ELSE  NULL\r\n"
    		+ "		END AS VolumnFGAmount  \r\n"
    		+ "		  , CASE WHEN (N.WorkDate is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(N.WorkDate) AS VARCHAR(2)) + '/' +   CAST(MONTH(N.WorkDate)AS VARCHAR(2))  END AS DyePlan  \r\n"
			+ "		 , CASE WHEN (O.DyeActual is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(O.DyeActual) AS VARCHAR(2)) + '/' +   CAST(MONTH(O.DyeActual)AS VARCHAR(2))  END AS DyeActual \r\n"; 
        
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
  		  + "  			  ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS CFMPlanLabDate \r\n"
  		  + "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
  		  + "			  inner join (select distinct  [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
  		  + "				          FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
  		  + "				          group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
  		  + "				          on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
  		  + "				         and a.[CreateDate] = b.[MaxCreateDate] \r\n"
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
    		+ "			/****** Script for SelectTopNRows command from SSMS  ******/\r\n"
    		+ "SELECT distinct max(b.[CFMDate]) as CFMDate\r\n"
    		+ "      ,a.[CFMAnswerDate] \r\n"
    		+ "      ,a.[LabNo] \r\n"
    		+ "  FROM ( SELECT distinct   a.[LabNo],b.[LabWorkProcessNo],  max([CFMAnswerDate])  as [CFMAnswerDate] \r\n"
    		+ "			  FROM [LBMS].[dbo].[SaleCFMDetail] as a\r\n"
    		+ "			  inner join (select distinct   [SaleSet] ,max([LabWorkProcessNo])  as [LabWorkProcessNo]   \r\n"
    		+ "	 							FROM [LBMS].[dbo].[SaleCFMDetail]     \r\n"
    		+ "	 							group by [SaleSet]   ) as b on a.[SaleSet] = b.[SaleSet]  and a.LabWorkProcessNo = b.[LabWorkProcessNo]\r\n"
    		+ "			  where a.saleset <> '' and SampleNo <> '' \r\n"
    		+ "			  group by  a.[LabNo],b.[LabWorkProcessNo]  )\r\n"
    		+ "			  as a\r\n"
    		+ "  inner join [LBMS].[dbo].[SaleCFMDetail] as b on a.LabNo = b.LabNo and a.[LabWorkProcessNo] = b.LabWorkProcessNo\r\n"
    		+ "  where a.labno = 'KA21-09-003'  \r\n"
    		+ "  group by a.LabNo ,a.CFMAnswerDate  ) AS lb on b.LabNo = lb.LabNo    \r\n";
	private String leftJoinJ = " left join [PCMS].[dbo].[FromSORCFM] AS J  on a.SaleLine = J.SaleLine and a.SaleOrder = J.SaleOrder \r\n ";
	private String leftJoinK = " left join [PCMS].[dbo].[InputReplacedRemark] AS K on K.ProductionOrder = b.ProductionOrder and K.SaleOrder = a.SaleOrder and K.SaleLine = a.SaleLine\r\n ";
	private String leftJoinl = " left join [PCMS].[dbo].[InputStockRemark] AS l on l.ProductionOrder = b.ProductionOrder and \r\n"
							 + "                                                   l.SaleOrder = a.SaleOrder and\r\n"
							 + "                                                   l.SaleLine = a.SaleLine and\r\n"
							 + "                                                   l.Grade = m.Grade  \r\n ";
	private String leftJoinM = " left join (SELECT distinct [ProductionOrder] \r\n"
			+ "				  ,[Grade] \r\n"
			+ "               ,[PriceSTD]\r\n"
			+ "				  ,sum([QuantityMR]) as GRSumMR\r\n"      
			+ "				  ,sum([QuantityKG]) as GRSumKG\r\n"
			+ "				  ,sum([QuantityYD]) as GRSumYD\r\n" 
			+ "			  FROM [PCMS].[dbo].[FromSapGoodReceive]\r\n"
			+ "			  where datastatus = 'O'\r\n"   
			+ "			  GROUP BY ProductionOrder,Grade ,[PriceSTD])\r\n"
//			+ "  as m on c.ProductionOrder = m.ProductionOrder and c.Grade = m.Grade	\r\n";
			+ "  as m on c.ProductionOrder = m.ProductionOrder \r\n";
	private String leftJoinNToO =  
			  " left join ( select distinct a.ProductionOrder , WorkDate \r\n"
			+ "              from  [PPMM].[dbo].[OperationWorkDate]  as a\r\n"
			+ "              inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                          from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                          where Operation >= 100 and Operation <= 103\r\n"
			+ "                          group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "              where a.Operation >= 100 and a.Operation <= 103 ) as N on b.ProductionOrder = N.ProductionOrder\r\n"
			+ " left join ( select a.ProductionOrder , OperationEndDate as DyeActual\r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap]  as a\r\n"
			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                        where Operation >= 100 and Operation <= 103\r\n"
			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "            where a.Operation >= 100 and a.Operation <= 103 \r\n"
			+ "            ) as O on b.ProductionOrder = O.ProductionOrder\r\n"
			 ;
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
		int check = 0;
		String customerShortName ="", saleNumber = "" , materialNo = "",saleOrder = "", saleLine = "",
		saleCreateDate = "",labNo = "" ,articleFG = "",designFG = "",userStatus = "", prdOrder= "",
		prdCreateDate = "",deliveryStatus = "",saleStatus ="",dist="",customerName = "",dueDate = ""; 
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
		dueDate = bean.getDueDate();
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel(); 
		List<String> userStatusList = bean.getUserStatusList();
		List<String> cusNameList = bean.getCustomerNameList();
		List<String> cusShortNameList = bean.getCustomerShortNameList();
		List<String> divisionList = bean.getDivisionList();
		where += 
//				" CustomerName like '%" + customerName + "%' and\r\n"
//				+ " CustomerShortName like '%" + customerShortName + "%' and \r\n" 
				   " MaterialNo like '"+materialNo+"%' and\r\n"
				  + " a.SaleOrder like '"+saleOrder+"%' \r\n"
//				  + " a.SaleLine like '"+saleLine+"%' and\r\n"
				  ;   	
		if(!saleCreateDate.equals("")) {     
			String[] dateArray = saleCreateDate.split("-");
			where += "and ( SaleCreateDate >= CONVERT(DATE,'"+ dateArray[0].trim() +"',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'"+ dateArray[1].trim() +"',103) ) \r\n" ;
		}
		if (cusNameList.size() > 0) { 
			where += " and  ( ";
			String text = "";
			for (int i = 0; i < cusNameList.size(); i++) {
				text = cusNameList.get(i);
				where += " CustomerName = '" +text + "' ";
				if (i != cusNameList.size() - 1) {
					where += " or ";
				} ;
			}
			where += " ) \r\n";
		}
		if (cusShortNameList.size() > 0) { 
			where += " and  ( ";
			String text = "";
			for (int i = 0; i < cusShortNameList.size(); i++) {
				text = cusShortNameList.get(i);
				where += " CustomerShortName = '" +text + "' ";
				if (i != cusShortNameList.size() - 1) {
					where += " or ";
				} ;
			}
			where += " ) \r\n"; 
		}
		if (userStatusList.size() > 0) {
//			where += " and b.UserStatus like '" + userStatus + "%'  \r\n";
			where += " and  ( ( b.ProductionOrder is null and b.UserStatus is null ) or ( b.ProductionOrder is not null and ";
//			where += " and  ( ";
			String text = "";
			for (int i = 0; i < userStatusList.size(); i++) {
				text = userStatusList.get(i);
				if(text.equals("รอจัดLot")) {
					check = 1;
				}
				where += " b.UserStatus = '" +text + "' ";
				if (i != userStatusList.size() - 1) {
					where += " or ";
				} ;
			}
			where += " 		) \r\n";
			where += " ) \r\n";
		}
		if (divisionList.size() > 0) {  
			where += " and  ( ";
			String text = "";
			for (int i = 0; i < divisionList.size(); i++) {
				text = divisionList.get(i);
				where += " Division = '" +text + "' ";
				if (i != divisionList.size() - 1) {
					where += " or ";
				} ;
			}
			where += " ) \r\n";
		}
		if(!saleNumber.equals("")) { where += " and a.SaleNumber like '"+saleNumber+"%' \r\n" ; }
		if(!labNo.equals("")) { where += " and b.LabNo like '"+labNo+"%'  \r\n" ; }
		if(!articleFG.equals("")) { where += " and a.ArticleFG like '"+articleFG+"%'  \r\n" ; } 	
		if(!designFG.equals("")) { where += " and a.DesignFG like '"+designFG+"%'  \r\n" ; }
//		if(!userStatus.equals("")) { where += " and b.UserStatus like '"+userStatus+"%'  \r\n" ; }
		if(!prdOrder.equals("")) { where += " and b.ProductionOrder like '"+prdOrder+"%'  \r\n " ; }
		if(!prdCreateDate.equals("")) {  
			String[] dateArray = prdCreateDate.split("-");
			where += " and ( PrdCreateDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
					+ " PrdCreateDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
		}
		if(!dueDate.equals("")) {  
			String[] dateArray = dueDate.split("-");
			where += " and ( DueDate >= CONVERT(DATE,'"+ dateArray[0].trim()+"',103)  and \r\n"
				+ " DueDate <= CONVERT(DATE,'"+ dateArray[1].trim()+"',103) )  \r\n" ;
		}
		if(!deliveryStatus.equals("")) { where += " and a.DeliveryStatus like '"+deliveryStatus+"%'  \r\n" ; } 
		if(!saleStatus.equals("")) { where += " and a.SaleStatus like '"+saleStatus+"%'  \r\n" ; } 
		if(!dist.equals("")) { 
			String[] array = dist.split("\\|");   
			where += " and  ( " ; 
			for(int i = 0 ;i< array.length ;i++) {     
				where += " a.DistChannel = '"+array[i]+"' " ;    	
				if(i != array.length - 1) { where += " or " ;};
			}
			where += " ) \r\n" ; 
		}    
		if(check == 0) {   
			where += "   and b.ProductionOrder is not null \r\n";
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
					+ this.leftJoinJ
					+ this.leftJoinK   
					+ this.leftJoinM
					+ this.leftJoinl
					+ this.leftJoinNToO
					+ where  
					+ " and ( c.DataStatus <> 'X'  OR C.DataStatus IS NULL ) "          
//					+ " and x.ProductionOrder is null "
					+ " union " 
					+  " SELECT DISTINCT  "       
					+ this.select
					+ " FROM (  SELECT DISTINCT  a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
					+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
					+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
					+ "	          , b.ProductionOrder,a.CustomerName ,a.DesignFG,a.OrderAmount  \r\n"
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
					+ this.leftJoinJ
					+ this.leftJoinK
					+ this.leftJoinM       
					+ this.leftJoinl
					+ this.leftJoinNToO
					+ where    
					+ " and ( c.DataStatus <> 'X'  OR C.DataStatus IS NULL ) "      
					+ " Order by a.CustomerShortName, a.DueDate,b.[ProductionOrder]";
//					+ " Order by a.SaleOrder, SaleLine,b.ProductionOrder,c.Grade ";      
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
		ArrayList<InputDateDetail> list  = new ArrayList<InputDateDetail>();
		ArrayList<InputDateDetail> listCount  = new ArrayList<InputDateDetail>();
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
			list = this.getMaxCFMPlanLabDateDetail(poList);
			listCount = this.getCountCFMPlanLabDateDetail(poList);
			check = list.size();
			} 
		else if(caseSave.equals("CFMPlanDate")) { 
			planDate = bean.getCFMPlanDate();
			fromTable = "[PCMS].[dbo].[PlanCFMDate] "  ; 
			list = this.getMaxCFMPlanDateDetail(poList) ;
			listCount = this.getCountCFMPlanDateDetail(poList);
			check = list.size();	
		} 
		else if(caseSave.equals("DeliveryDate")) { 
			planDate = bean.getDeliveryDate();
			fromTable = "[PCMS].[dbo].[PlanDeliveryDate] "  ; 
			list = this.getMaxDeliveryPlanDateDetail(poList) ;
			listCount = this.getCountDeliveryPlanDateDetail(poList);
			check = list.size();	
		}  
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine())); 
		ArrayList<InputDateDetail> listInput = new ArrayList<InputDateDetail>();
		java.util.Date date ;
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
							+ "		[CreateDate] ,[LotNo] " 
							+ "     ) "//24   
							+ " 	values(? , ? , ? , ? , ?"//  1
							+ "			  ,? , ? "
							+ " ) ;"; 
						 
						prepared = connection.prepareStatement(sql);   
						prepared.setString(1, bean.getProductionOrder());
						prepared.setString(2, bean.getSaleOrder());
						prepared.setString(3, saleLine); 
						if (planDate.equals("undefined") || planDate.equals("")  || !isValidDate(planDate)) {
							prepared.setNull(4, java.sql.Types.DATE);
						} 
						else {
							date = sdf2.parse(planDate);
							prepared.setDate(4, convertJavaDateToSqlDate(date) );
						}   
						prepared.setString(5, bean.getUserId());
						prepared.setTimestamp(6, new Timestamp(time));;   
						prepared.setString(7, bean.getLotNo());
						prepared.executeUpdate();  
						if(caseSave.equals("CFMPlanDate")) {
							beanInput.setIconStatus("I0");
						}
						else { 
							beanInput.setIconStatus("I1");
						} 
						beanInput.setSystemStatus("Update Success."); 
						if(listCount.size() > 0) {
							beanInput.setCountPlanDate(listCount.get(0).getCountPlanDate()+1);
						}
						else { 
							beanInput.setCountPlanDate(1);
						}    
						 
			}catch (SQLException | ParseException e) {
				System.err.println(e.getMessage());
				beanInput.setIconStatus("E");
				beanInput.setSystemStatus("Something happen, Please contact IT.");
			} 
		}    
		listInput.add(beanInput);
		return listInput;
	}
	public static boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
	@Override
	public ArrayList<PCMSSecondTableDetail> saveInputDetail(ArrayList<PCMSSecondTableDetail> poList) {
		 
		PCMSSecondTableDetail bean = poList.get(0);  
		String caseSave = bean.getCaseSave();
		String valueChange = ""; 
		String tableName = "";
		if(caseSave.equals("StockRemark")) {
			valueChange = bean.getStockRemark(); 
			tableName = "[InputStockRemark]";
			bean = this.upSertRemarkCaseWithGrade(tableName,valueChange,bean);
		}
		else if(caseSave.equals("ReplacedRemark")){
			valueChange = bean.getReplacedRemark();
			tableName = "[InputReplacedRemark]";
			bean = this.upSertRemarkCaseOne(tableName,valueChange,bean);
		} 
		poList.clear();
		poList.add(bean); 
		return poList;
	} 
	private PCMSSecondTableDetail upSertRemarkCaseOne(String tableName, String valueChange,
			PCMSSecondTableDetail bean) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		String caseSave = bean.getCaseSave();
		try {      
			String sql =  
					"UPDATE [PCMS].[dbo]."+tableName
					+ " SET "+caseSave+" = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ?  "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ "  if @rc <> 0 " 
					+ " print @rc " 
					+ " else "
					+ " INSERT INTO [PCMS].[dbo]."+tableName
					+ " ([ProductionOrder],[SaleOrder] ,[SaleLine],"+caseSave+",[ChangeBy] ,[ChangeDate])"//55 
					+ " values(? , ? , ? , ? , ? "
					+ "      , ? )  "
					+ ";"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, valueChange);
				prepared.setString(2, bean.getUserId());
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);  
				prepared.setString(5, saleOrder);  
				prepared.setString(6, saleLine);  
				prepared.setString(7, prdOrder);  
				prepared.setString(8, saleOrder);  
				prepared.setString(9, saleLine);  
				prepared.setString(10, valueChange);  
				prepared.setString(11, bean.getUserId());  
				prepared.setTimestamp(12, new Timestamp(time));  
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("insertLabNoDetail"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		return bean;
	}

	private PCMSSecondTableDetail upSertRemarkCaseWithGrade(String tableName, String valueChange,
			PCMSSecondTableDetail bean) {
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();   
		String prdOrder = bean.getProductionOrder();   
		String saleOrder = bean.getSaleOrder();   
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));  
		String caseSave = bean.getCaseSave();
		String grade = bean.getGrade();
		try {      
			String sql =  
					"UPDATE [PCMS].[dbo]."+tableName
					+ " SET "+caseSave+" = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
					+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? and [Grade] = ? "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ "  if @rc <> 0 " 
					+ " print @rc " 
					+ " else "
					+ " INSERT INTO [PCMS].[dbo]."+tableName
					+ " ([ProductionOrder],[SaleOrder] ,[SaleLine],"+caseSave+",[ChangeBy] "
					+ " ,[ChangeDate],[Grade])"//55 
					+ " values(? , ? , ? , ? , ? "
					+ "      , ? , ? )  ;"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, valueChange);
				prepared.setString(2, bean.getUserId());
				prepared.setTimestamp(3, new Timestamp(time));  
				prepared.setString(4, prdOrder);  
				prepared.setString(5, saleOrder);  
				prepared.setString(6, saleLine);  
				prepared.setString(7, grade);  
				prepared.setString(8, prdOrder);  
				prepared.setString(9, saleOrder);  
				prepared.setString(10, saleLine);  
				prepared.setString(11, valueChange);  
				prepared.setString(12, bean.getUserId());  
				prepared.setTimestamp(13, new Timestamp(time));  
				prepared.setString(14, grade);  
				prepared.executeUpdate();    
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.err.println("insertLabNoDetail"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}    
		return bean;
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
				    + "   , LotNo \r\n"  
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
		    + "   , LotNo \r\n"
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
	      + "   ,'' AS LotNo \r\n"
		  + " FROM [PCMS].[dbo].[FromSapSubmitDate]  as a\r\n" 
		  + " where a.[ProductionOrder] = '" + bean.getProductionOrder() + "' and SubmitDate is not null "
  		  + "   and [DataStatus] = 'O' "
		  + "    ORDER BY InputFrom ,CreateDate desc ";
				
		 
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
					    + "   , LotNo \r\n"
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
					    + "   ,'' AS LotNo \r\n"
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
	public ArrayList<InputDateDetail> getCountCFMPlanLabDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0); 
		String sql = 
				  " SELECT distinct count(a.[ProductionOrder]) as countAll  \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMLabDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMLabDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and "
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[PlanDate] = CONVERT(DATE,'"+bean.getCFMPlanLabDate()+ "',103)  ";
		 		  ; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
	public ArrayList<InputDateDetail> getCountDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0); 
		String sql = 
				  " SELECT distinct count(a.[ProductionOrder]) as countAll  \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and "
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where  a.[PlanDate] = CONVERT(DATE,'"+bean.getDeliveryDate()+ "',103) ";
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
	public ArrayList<InputDateDetail> getCountCFMPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0); 
		String sql = 
				    " SELECT distinct count(a.[ProductionOrder]) as countAll  \r\n"
		 		  + " FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
		 		  + " inner join (select distinct [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
		 		  + "			  FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
		 		  + "			  group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
		 		  + "				on a.ProductionOrder = b.ProductionOrder and "
		 		  + "                  a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine and\r\n"
		 		  + "				   a.[CreateDate] = b.[MaxCreateDate] \r\n "
		 		  + " where a.[PlanDate] = CONVERT(DATE,'"+bean.getCFMPlanDate()+ "',103) ";
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
	public ArrayList<ColumnHiddenDetail> getColVisibleDetail(String user) {
		ArrayList<ColumnHiddenDetail> list = null;  
		String sql = 
				    " SELECT distinct [EmployeeID] ,[ColVisibleDetail] ,[ColVisibleSummary]\r\n"
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
		String colName = pd.getColVisibleDetail(); 
		String user = pd.getUserId(); 
		ArrayList<ColumnHiddenDetail> list = new ArrayList<ColumnHiddenDetail>();
		ColumnHiddenDetail bean = new ColumnHiddenDetail();
//		ArrayList<ColumnHiddenDetail> beanCheck = this.getColHiddenDetail(leftJoinB);
//		if(beanCheck.size() > 0) { 
//		} 
		try {      
			String sql = 
					"UPDATE [PCMS].[dbo].[ColumnSetting] "
					+ " SET [ColVisibleDetail] = ?  "
					+ " WHERE [EmployeeID]  = ? "
					+ " declare  @rc int = @@ROWCOUNT " // 56
					+ "  if @rc <> 0 " 
					+ " print @rc " 
					+ " else "
					+ " INSERT INTO [PCMS].[dbo].[ColumnSetting]	 "
					+ " ([EmployeeID] ,[ColVisibleDetail])"//55 
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
			System.err.println("insertLabNoDetail"+e.getMessage());
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
	@Override
	public ArrayList<PCMSAllDetail> getCustomerNameList() {
		ArrayList<PCMSAllDetail> list = null; 
		String sql = 
				  "SELECT distinct [CustomerName] \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
				+ " order by [CustomerName] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getCustomerShortNameList() {
		ArrayList<PCMSAllDetail> list = null; 
		String sql =   
				  "SELECT distinct [CustomerShortName]  \r\n"  
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n " 
				+ " order by  [CustomerShortName] \r\n"; 
		  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		return list;
	}
	private String forPage = "Detail"; 
	@Override    
	public ArrayList<PCMSTableDetail> saveDefault(ArrayList<PCMSTableDetail> poList) { 
		ArrayList<PCMSTableDetail> list = null;
		String where = " where  ";
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = ""  ,divisionName = "";
		PCMSTableDetail bean = poList.get(0);    
//		System.out.println(bean.toString());
		userId = bean.getUserId();
		List<String> userStatusList = bean.getUserStatusList();
		List<String> cusNameList = bean.getCustomerNameList();
		List<String> cusShortNameList = bean.getCustomerShortNameList(); 
		List<String> divisionList = bean.getDivisionList(); 
		if (cusNameList.size() > 0) {  
			String text = "";
			for (int i = 0; i < cusNameList.size(); i++) {
				text = cusNameList.get(i);
				customerName += text  ;
				if (i != cusNameList.size() - 1) {
					customerName += "|";
				} ;
			} ;
		}
		if (divisionList.size() > 0) {  
			String text = "";
			for (int i = 0; i < divisionList.size(); i++) {
				text = divisionList.get(i);
				divisionName += text  ;
				if (i != divisionList.size() - 1) {
					divisionName += "|";
				} ;
			} ;
		}
		if (cusShortNameList.size() > 0) {  
			String text = "";
			for (int i = 0; i < cusShortNameList.size(); i++) {
				text = cusShortNameList.get(i);
				customerShortName += text;
				if (i != cusShortNameList.size() - 1) {
					customerShortName += "|";
				} ;
			}
			where += " ) \r\n";
		}
		if (userStatusList.size() > 0) {  
			String text = "";
			for (int i = 0; i < userStatusList.size(); i++) {
				text = userStatusList.get(i);
				userStatus +=  text ;
				if (i != userStatusList.size() - 1) {
					userStatus += "|";
				} ;
			} ;
		}         

		poList.get(0).setDivision(divisionName);
		poList.get(0).setUserStatus(userStatus);
		poList.get(0).setCustomerName(customerName);
		poList.get(0).setCustomerShortName(customerShortName); 
		ArrayList<PCMSTableDetail> beanCheck = this.getSearchSettingDetail(userId,this.forPage);
		if(beanCheck.size() == 0) {
			list = this.insertSearchSettingDetai(poList);
		}
		else {
			list = this.updateSearchSettingDetai(poList);
		} 
		return list; 
	}

	private ArrayList<PCMSTableDetail> updateSearchSettingDetai(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();    
		ArrayList<PCMSTableDetail> list = new ArrayList<PCMSTableDetail>(); 
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,division = ""; 
		PCMSTableDetail bean = poList.get(0); 
		userId = bean.getUserId();
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();
		saleCreateDate = bean.getSaleOrderCreateDate();
		saleNumber = bean.getSaleNumber();
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		dueDate = bean.getDueDate();
		designFG = bean.getDesignFG();  
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();   
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel();    
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
		userStatus = bean.getUserStatus();  
		division = bean.getDivision();
		int no = 1;   
		try {      
			String sql = 
					"UPDATE [dbo].[SearchSetting]\r\n"
					+ "   SET [No] = ?  ,[CustomerName] = ?,[CustomerShortName] = ?\r\n"
					+ "      ,[SaleOrder] = ? ,[ArticleFG] = ? ,[DesignFG] =  ? \r\n"
					+ "      ,[ProductionOrder] = ? ,[SaleNumber] = ? ,[MaterialNo] = ?\r\n"
					+ "      ,[LabNo] = ? ,[DeliveryStatus] = ? ,[DistChannel] = ?\r\n"
					+ "      ,[SaleStatus] = ? ,[DueDate] = ? ,[SaleCreateDate] = ? \r\n"
					+ "      ,[PrdCreateDate] = ? ,[UserStatus] = ? , [Division] = ? \r\n"
					+ "      where  [EmployeeID] = ? and [ForPage] = ?" ;     	
			prepared = connection.prepareStatement(sql);    
//			prepared.setString(1, userId);
			prepared.setInt(1, no);
			prepared.setString(2, customerName);
			prepared.setString(3, customerShortName);  
			prepared.setString(4, saleOrder);  
			prepared.setString(5, articleFG);  
			prepared.setString(6, designFG);  
			prepared.setString(7, prdOrder);  
			prepared.setString(8, saleNumber);  
			prepared.setString(9, materialNo);  
			prepared.setString(10, labNo);  
			prepared.setString(11, deliveryStatus);  
			prepared.setString(12, dist);  
			prepared.setString(13, saleStatus);  
			prepared.setString(14, dueDate);  
			prepared.setString(15, saleCreateDate);  
			prepared.setString(16, prdCreateDate);  
			prepared.setString(17, userStatus);
			prepared.setString(18, division);
			prepared.setString(19, userId);
			prepared.setString(20, this.forPage);
			prepared.executeUpdate();   
			bean.setIconStatus("I");
			bean.setSystemStatus("save Success.");
		} catch (SQLException e) {
			System.out.println("updateSearchSettingDetai"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}

	private ArrayList<PCMSTableDetail> insertSearchSettingDetai(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();    
		ArrayList<PCMSTableDetail> list = new ArrayList<PCMSTableDetail>(); 
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="",dueDate="",
				userId = "" ,division = ""; ;
		PCMSTableDetail bean = poList.get(0); 
		userId = bean.getUserId();
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();
		saleCreateDate = bean.getSaleOrderCreateDate();
		saleNumber = bean.getSaleNumber();
		labNo = bean.getLabNo();
		articleFG = bean.getArticleFG();
		dueDate = bean.getDueDate();
		designFG = bean.getDesignFG();  
		prdOrder = bean.getProductionOrder();
		prdCreateDate = bean.getProductionOrderCreateDate();   
		deliveryStatus = bean.getDeliveryStatus();
		saleStatus = bean.getSaleStatus();
		dist = bean.getDistChannel();    
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
		userStatus = bean.getUserStatus();  
		division = bean.getDivision();
		int no = 1;   
		try {      
			String sql = 
					"INSERT INTO [dbo].[SearchSetting]\r\n"
					+ "           ( [EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
					+ "           ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
					+ "           ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
					+ "           ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[ForPage],[Division] \r\n"
					+ "           )\r\n"
					+ "     VALUES\r\n"
					+ "           ( "
					+ "            ? , ? , ? , ? , ?, "
					+ "            ? , ? , ? , ? , ?,"
					+ "            ? , ? , ? , ? , ?,"
					+ "            ? , ? , ? , ? , ?"
					+ "           )"  ;     	
				prepared = connection.prepareStatement(sql);    
				prepared.setString(1, userId);
				prepared.setInt(2, no);
				prepared.setString(3, customerName);
				prepared.setString(4, customerShortName);  
				prepared.setString(5, saleOrder);  
				prepared.setString(6, articleFG);  
				prepared.setString(7, designFG);  
				prepared.setString(8, prdOrder);  
				prepared.setString(9, saleNumber);  
				prepared.setString(10, materialNo);  
				prepared.setString(11, labNo);  
				prepared.setString(12, deliveryStatus);  
				prepared.setString(13, dist);  
				prepared.setString(14, saleStatus);  
				prepared.setString(15, dueDate);  
				prepared.setString(16, saleCreateDate);  
				prepared.setString(17, prdCreateDate);  
				prepared.setString(18, userStatus);  
				prepared.setString(19, this.forPage);  
				prepared.setString(20, division);  
				prepared.executeUpdate();   
				bean.setIconStatus("I");
				bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			System.out.println("insertSearchSettingDetail"+e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  
		list.add(bean);
		return list;
	}

	@Override  
	public ArrayList<PCMSTableDetail> loadDefault(ArrayList<PCMSTableDetail> poList) {
		// TODO Auto-generated method stub
		String userId = poList.get(0).getUserId();
		ArrayList<PCMSTableDetail> bean = this.getSearchSettingDetail(userId,this.forPage);
		return bean;
	}
	public ArrayList<PCMSTableDetail> getSearchSettingDetail(String userId,String forPage) {
		ArrayList<PCMSTableDetail> list = null; 
		String sql =   
				  "SELECT "     
				  + " [EmployeeID] ,[No] ,[CustomerName] ,[CustomerShortName] ,[SaleOrder]\r\n"
				  + "      ,[ArticleFG] ,[DesignFG] ,[ProductionOrder] ,[SaleNumber] ,[MaterialNo]\r\n"
				  + "      ,[LabNo] ,[DeliveryStatus] ,[DistChannel] ,[SaleStatus] ,[DueDate]\r\n"
				  + "      ,[SaleCreateDate] ,[PrdCreateDate],[UserStatus],[Division]\r\n"
				  + "  FROM [PCMS].[dbo].[SearchSetting]\r\n"
				  + " where EmployeeID = '"+userId+"' and ForPage = '"+forPage+ "' "; 
//		  System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSearchTableDetail(map));
		} 
		return list;
	} 
	@Override
	public ArrayList<PCMSSecondTableDetail> getDivisionList() {
		ArrayList<PCMSSecondTableDetail> list = null; 
		String sql =    
				  "SELECT distinct [Division] \r\n"   
				+ " FROM [PCMS].[dbo].[FromSapMainSale] \r\n"
				+ " where Division <> '' " 
				+ " order by [Division] \r\n";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSSecondTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		} 
		return list;
	} 
	 
}
