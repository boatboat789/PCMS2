package dao.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dao.PCMSMainDao;
import entities.CFMDetail;
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
import entities.SaleDetail;
import entities.SaleInputDetail;
import entities.SendTestQCDetail;
import entities.SubmitDateDetail;
import entities.WaitTestDetail;
import entities.WorkInLabDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

public class PCMSMainDaoImpl implements PCMSMainDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	private String select = " a.SaleOrder "
			+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
			+ "		WHEN 0 THEN ''  \r\n"  
			+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
			+ "		END AS [SaleLine] "
			+ "    ,a.DesignFG\r\n" 
			+ "   ,a.ArticleFG\r\n" 
			+ "   ,a.DistChannel\r\n"
			+ "   ,a.Color\r\n" 
			+ "   ,a.ColorCustomer,a.SaleQuantity\r\n"
			+ "	  ,( a.SaleQuantity - a.RemainQuantity ) as BillQuantity\r\n" + "   ,a.SaleUnit\r\n"
			+ "	  ,b.ProductionOrder,b.TotalQuantity,b.GreigeInDate\r\n"
			+ "	  , CASE WHEN (b.GreigeInDate is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(b.GreigeInDate) AS VARCHAR(2)) + '/' +  CAST(MONTH(GreigeInDate)AS VARCHAR(2)) END AS GreigeInDate \r\n"
			+ "	  ,b.UserStatus,b.LabStatus,a.DueDate\r\n"
//    		+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as Prepare\r\n"
//    		+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as Preset\r\n"
			+ "		  , CASE WHEN (i.WorkDate is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(i.WorkDate) AS VARCHAR(2)) + '/' +  CAST(MONTH(i.WorkDate)AS VARCHAR(2)) END AS DyePlan  \r\n"
			+ "		 , CASE WHEN (j.DyeActual is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(j.DyeActual) AS VARCHAR(2)) + '/' +  CAST(MONTH(j.DyeActual)AS VARCHAR(2)) END AS DyeActual \r\n"
			+ "		 , CASE WHEN (k.Dryer is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(k.Dryer) AS VARCHAR(2)) + '/' +  CAST(MONTH(k.Dryer)AS VARCHAR(2)) END AS Dryer \r\n"
			+ "		 , CASE WHEN (l.Finishing is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(l.Finishing) AS VARCHAR(2)) + '/' +  CAST(MONTH(l.Finishing)AS VARCHAR(2)) END AS Finishing \r\n"
			+ "		 , CASE WHEN (m.Inspectation is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(m.Inspectation) AS VARCHAR(2)) + '/' +  CAST(MONTH(m.Inspectation)AS VARCHAR(2)) END AS Inspectation  \r\n"
			+ "		 , CASE WHEN (n.Prepare is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(n.Prepare) AS VARCHAR(2)) + '/' +  CAST(MONTH(n.Prepare)AS VARCHAR(2)) END AS Prepare \r\n"
			+ "	  , CASE WHEN (o.Preset is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(o.Preset) AS VARCHAR(2)) + '/' +  CAST(MONTH(o.Preset)AS VARCHAR(2)) END AS Preset   \r\n  "
//    		+ "	     , CAST(DAY(h.Prepare) AS VARCHAR(2)) + '/' +  CAST(MONTH(h.Prepare)AS VARCHAR(2)) AS Prepare \r\n"
//    		+ "	     , CAST(DAY(i.Preset) AS VARCHAR(2)) + '/' +  CAST(MONTH(i.Preset)AS VARCHAR(2)) AS Preset \r\n"
//    		+ "	     , CAST(DAY(c.WorkDate) AS VARCHAR(2)) + '/' +  CAST(MONTH(c.WorkDate)AS VARCHAR(2)) AS DyePlan \r\n"
//    		+ "		 , CAST(DAY(d.DyeActual) AS VARCHAR(2)) + '/' +  CAST(MONTH(d.DyeActual)AS VARCHAR(2)) AS DyeActual \r\n"
//    		+ "		 , CAST(DAY(e.Dryer) AS VARCHAR(2)) + '/' +  CAST(MONTH(e.Dryer)AS VARCHAR(2)) END AS Dryer \r\n"
//    		+ "		 , CAST(DAY(f.Finishing) AS VARCHAR(2)) + '/' +  CAST(MONTH(f.Finishing)AS VARCHAR(2)) AS Finishing \r\n"
//    		+ "		 , CAST(DAY(g.Inspectation) AS VARCHAR(2)) + '/' +  CAST(MONTH(g.Inspectation)AS VARCHAR(2)) AS Inspectation \r\n" 
//    		+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as DyeActual\r\n"
//    		+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as Dryer\r\n"
//    		+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as Finishing\r\n"
//    		+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as Inspectation\r\n"
			+ "	  ,CAST(DAY(d.CFMAnswerDate) AS VARCHAR(2)) + '/' +  CAST(MONTH(d.CFMAnswerDate)AS VARCHAR(2)) as CFMDateActual\r\n" 
			+ ",   CASE  \r\n"
    		+ "		  WHEN g.CFMPlanDate is not null \r\n"    
    		+ "       THEN CAST(DAY(g.CFMPlanDate) AS VARCHAR(2)) + '/' +  CAST(MONTH(g.CFMPlanDate )AS VARCHAR(2))   \r\n"
    		+ "		  ELSE CAST(DAY(f.CFMPlanDateOne) AS VARCHAR(2)) + '/' +  CAST(MONTH(f.CFMPlanDateOne )AS VARCHAR(2))   \r\n"
    		+ "		  END AS CFMPlanDate \r\n"
//			+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as CFMDateActual\r\n"
			+ "   ,CASE  \r\n"
    		+ "		  WHEN h.DeliveryDate is not null \r\n"
    		+ "       THEN CAST(DAY(h.DeliveryDate) AS VARCHAR(2)) + '/' +  CAST(MONTH(h.DeliveryDate)AS VARCHAR(2))   \r\n"
    		+ "		  ELSE CAST(DAY(b.CFTYPE) AS VARCHAR(2)) + '/' +  CAST(MONTH(b.CFTYPE)AS VARCHAR(2))   \r\n"
    		+ "		  END AS DeliveryDate \r\n"
//			+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as ShipDatePlan  \r\n"
			+ "	  , CASE WHEN (z.LotShipping is NULL) THEN null \r\n"
			+ "	    ELSE CAST(DAY(z.LotShipping) AS VARCHAR(2)) + '/' +  CAST(MONTH(z.LotShipping)AS VARCHAR(2)) END AS LotShipping \r\n"
			+ "	  ,b.LabNo,a.CustomerShortName,a.SaleNumber\r\n"
//    		+ "	  ,a.SaleFullName\r\n"
			+ "   ,CASE PATINDEX('%[^0 ]%', a.[SaleNumber]  + ' ‘') \r\n" + "    		 WHEN 0 THEN ''   \r\n"
			+ "    		 ELSE SUBSTRING(a.[SaleNumber] , 5, LEN(a.[SaleNumber])) +':'+a.[SaleFullName]\r\n"
			+ "    		 END AS [SaleFullName]   \r\n" 
			+ "	  ,a.SaleCreateDate,b.PrdCreateDate\r\n" 
			+ "	  ,a.MaterialNo,a.DeliveryStatus,a.SaleStatus\r\n" 
			+ "   ,[LotNo] ,a.ShipDate\r\n";
	private String selectTwo = " b.[ProductionOrder],[LotNo],[Batch],[LabNo]\r\n"
			+ "	  ,[PrdCreateDate],a.[DueDate],a.[SaleOrder]\r\n"
			+ "	  ,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n" 
			+ "		WHEN 0 THEN ''  \r\n"
			+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
			+ "		END AS [SaleLine] ,PurchaseOrder,a.ArticleFG,a.DesignFG\r\n"
			+ "	  ,CustomerName,CustomerShortName,Shade,BookNo,Center\r\n"
			+ "	  ,MaterialNo,Volumn,SaleUnit,Unit as STDUnit\r\n" 
			+ "	  ,Color,PlanGreigeDate,RefPrd,GreigeInDate\r\n"
			+ "	  ,BCAware,OrderPuang,UserStatus,LabStatus\r\n"
			//			+ "	  ,'' as CFMPlanDate,'' as DeliveryDate"
			+ ",   CASE  \r\n"
			+ "		  WHEN g.CFMPlanDate is not null THEN g.CFMPlanDate  \r\n"
			+ "		  ELSE f.CFMPlanDateOne   \r\n"
			+ "		  END AS CFMPlanDate \r\n"
			//+ "	  ,CAST(DAY(GETDATE()) AS VARCHAR(2)) + '/' +  CAST(MONTH(GETDATE()  )AS VARCHAR(2)) as CFMDateActual\r\n"
			+ "   ,CASE  \r\n"
			+ "		  WHEN h.DeliveryDate is not null THEN h.DeliveryDate  \r\n"  
			+ "		  ELSE b.CFTYPE  \r\n"
			+ "		  END AS DeliveryDate \r\n"
			+ "   ,BCDate,RemarkOne\r\n"   
			+ "	  ,RemarkTwo,RemarkThree,RemAfterCloseOne,RemAfterCloseTwo\r\n"
			+ "	  ,RemAfterCloseThree \r\n" + "   ,GreigeArticle \r\n " + "   ,GreigeDesign \r\n";
	private String selectPO = "  [ProductionOrder],[RollNo],[QuantityKG]\r\n"
			+ "      ,[QuantityMR],[CreateDate] ,[PurchaseOrder]\r\n"
			+ "		,CASE PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' ‘')\r\n"
			+ "		 WHEN 0 THEN ''  \r\n"    
			+ "		 ELSE SUBSTRING( [PurchaseOrderLine] , PATINDEX('%[^0 ]%', [PurchaseOrderLine]  + ' '), LEN( [PurchaseOrderLine] ) )\r\n"
			+ "       END AS [PurchaseOrderLine] \r\n"      
			+ "      ,[RequiredDate],[PurchaseOrderDate],[PODefault]\r\n"
			+ "      ,[POLineDefault],[POPostingDateDefault],[DataStatus] \r\n";

	private String leftJoinB = " left join  [PCMS].[dbo].[FromSapMainProd] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n";
	private String leftJoinD = 
  		  "   left join ( SELECT distinct a.[ProductionOrder]\r\n"    
  		+ "					  ,[CFMNo]\r\n"
  		+ "					  ,[CFMNumber]\r\n"
  		+ "					  ,[CFMSendDate]\r\n"
  		+ "					  ,[CFMAnswerDate]\r\n"
  		+ "					  ,[CFMStatus] \r\n"   
  		+ "					  ,[CFMRemark]\r\n"
  		+ "				FROM [PCMS].[dbo].[FromSapCFM] as a\r\n"
  		+ "				inner join (select ProductionOrder ,   max([CFMNo]) as maxCFMNo  \r\n"
  		+ "					        from [PCMS].[dbo].[FromSapCFM] \r\n"
  		+ "							where  DataStatus <> 'X'  and [CFMNumber] <> ''\r\n"
  		+ "					 		group by ProductionOrder )as b on \r\n"
  		+ "                         a.[ProductionOrder] = b.[ProductionOrder] and  a.[CFMNo] = b.[maxCFMNo]  \r\n"
  		+ "             where  DataStatus <> 'X'  and [CFMNumber] <> '' \r\n"
  		+ "			) as d on b.ProductionOrder = d.ProductionOrder \r\n";  
	private String leftJoinF = 
  		  	" left join (SELECT a.[ProductionOrder] \r\n"
  		  	+ "			,max(a.SubmitDate) as CFMPlanDateOne\r\n"
  		  	+ "			FROM [PCMS].[dbo].[FromSapSubmitDate]  as a \r\n"
  		  	+ "         WHERE [DataStatus] = 'O'"
  		  	+ "			group by a.[ProductionOrder] ) as f on b.ProductionOrder = f.ProductionOrder \r\n"  ;   
	private String leftJoinG = 
  		  	" left join ( SELECT distinct a.[ProductionOrder] \r\n"  
  		  	+ "  			 ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS CFMPlanDate \r\n"
  		  	+ "			FROM [PCMS].[dbo].[PlanCFMDate]  as a\r\n"
  		  	+ "			inner join (select [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
  		  	+ "				FROM [PCMS].[dbo].[PlanCFMDate]  \r\n"
  		  	+ "				group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
  		  	+ "				on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
  		  	+ "				and a.[CreateDate] = b.[MaxCreateDate] \r\n"
  		  	+ "			) as g on g.ProductionOrder = b.ProductionOrder and g.SaleOrder = a.SaleOrder and g.SaleLine = a.SaleLine\r\n"  ;   
	private String leftJoinH = 
  		  	" left join ( SELECT distinct a.[ProductionOrder] \r\n"
  		  	+ "  			 ,a.[SaleOrder] ,a.[SaleLine] ,[PlanDate] AS DeliveryDate \r\n"
  		  	+ "			FROM [PCMS].[dbo].[PlanDeliveryDate]  as a\r\n"
  		  	+ "			inner join (select [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ,max([CreateDate]) as [MaxCreateDate]\r\n"
  		  	+ "				FROM [PCMS].[dbo].[PlanDeliveryDate]  \r\n"
  		  	+ "				group by [ProductionOrder]  ,[SaleOrder] ,[SaleLine]  ) as b  \r\n"
  		  	+ "				on a.ProductionOrder = b.ProductionOrder and a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine\r\n"
  		  	+ "				and a.[CreateDate] = b.[MaxCreateDate] \r\n"
  		  	+ "			) as h on h.ProductionOrder = b.ProductionOrder and h.SaleOrder = a.SaleOrder and h.SaleLine = a.SaleLine\r\n"  ;   
	private String leftJoinX = " left join [PCMS].[dbo].[FromSapMainProdSale] as x on a.SaleLine = x.SaleLine and x.SaleOrder = a.SaleOrder  \r\n";
	private String leftJoinZ = 
		      " left join (SELECT distinct [ProductionOrder] ,[SaleOrder] ,[SaleLine] ,max([LotShipping]) as [LotShipping]\r\n  "
			+ "            FROM [PCMS].[dbo].[FromSapMainGrade]\r\n  "
			+ "            GROUP BY [ProductionOrder] ,[SaleOrder] ,[SaleLine] ) \r\n  "
			+ "  as z on z.ProductionOrder = b.ProductionOrder \r\n      and z.SaleLine = b.SaleLine \r\n "
			+ "      and z.SaleOrder = b.SaleOrder \r\n";
	private String leftJoinIToO = 
			  " left join ( select distinct a.ProductionOrder , WorkDate \r\n"
			+ "              from  [PPMM].[dbo].[OperationWorkDate]  as a\r\n"
			+ "              inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                          from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                          where Operation >= 100 and Operation <= 103\r\n"
			+ "                          group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "              where a.Operation >= 100 and a.Operation <= 103 ) as i on b.ProductionOrder = i.ProductionOrder\r\n"
			+ " left join ( select a.ProductionOrder , OperationEndDate as DyeActual\r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap]  as a\r\n"
			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                        where Operation >= 100 and Operation <= 103\r\n"
			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "            where a.Operation >= 100 and a.Operation <= 103 \r\n"
			+ "            ) as j on b.ProductionOrder = j.ProductionOrder\r\n"
			+ " left join ( select distinct a.ProductionOrder , OperationEndDate as Dryer \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                        where Operation >= 140 and Operation <= 143\r\n"
			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "            where a.Operation >= 140 and a.Operation <= 143 )  as k on b.ProductionOrder = k.ProductionOrder\r\n"
			+ " left join ( select distinct a.ProductionOrder , OperationEndDate as Finishing \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            inner join (select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                        from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                        where Operation >= 190 and Operation <= 193\r\n"
			+ "                        group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "            where a.Operation >= 190 and a.Operation <= 193  ) as l on b.ProductionOrder = l.ProductionOrder\r\n"
			+ " left join ( select distinct a.ProductionOrder , OperationEndDate as Inspectation \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            inner join ( select ProductionOrder ,   max(Operation) as Operation   \r\n"
			+ "                         from  [PPMM].[dbo].[DataFromSap] \r\n"
			+ "                         where Operation >= 199 and Operation <= 200\r\n"
			+ "                         group by ProductionOrder  ) as b on a.ProductionOrder = b.ProductionOrder and a.Operation = b.Operation\r\n"
			+ "            where a.Operation >= 199 and a.Operation <= 200 ) as m on b.ProductionOrder = m.ProductionOrder \r\n"
			+ " left join ( select ProductionOrder , OperationEndDate as Prepare  \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            where a.Operation  = 5 ) as n on b.ProductionOrder = n.ProductionOrder \r\n"
			+ " left join ( select distinct ProductionOrder , OperationEndDate as Preset  \r\n"
			+ "            from  [PPMM].[dbo].[DataFromSap] as a\r\n"
			+ "            where a.Operation = 50  ) as o on b.ProductionOrder = o.ProductionOrder \r\n " ;
	private String selectPreset = " [ProductionOrder],[PostingDate],[WorkCenter]\r\n"
			+ "      ,[Operation],[No],[DataStatus] \r\n";
	private String selectDyeing = "[ProductionOrder],[PostingDate],[Operation]\r\n"
			+ "      ,[WorkCenter],[DyeStatus],[Remark],[ReDye]\r\n"  
			+ "      ,[RollNo],[Da],[Db],[L],[ST]\r\n"
			+ "      ,[ColorStatus],[ColorRemark],[DeltaE],[No]\r\n"
			+ "      ,[DataStatus]\r\n ";
	private String selectPacking = "[ProductionOrder],[PostingDate],[Quantity]\r\n"
			+ "      ,[RollNo],[Status],[QuantityKG],[Grade]\r\n"
			+ "      ,[No],[DataStatus],[QuantityYD]\r\n ";
	private String selectInspect = "[ProductionOrder],[PostingDate],[QuantityGreige]\r\n"
			+ "      ,[Operation],[QuantityFG],[Remark],[No]\r\n"
			+ "      ,[DataStatus]\r\n ";
	private String selectFinishing = "[ProductionOrder],[No],[PostingDate]\r\n"
			+ "      ,[WorkCenter],[Status],[NCDate],[Cause]\r\n"
			+ "      ,[CarNo],[DeltaE],[Color],[Operation]\r\n"
			+ "      ,[CCStatus],[CCRemark],[RollNo],[Da]\r\n"
			+ "      ,[Db],[L],[ST],[CCPostingDate]\r\n"
			+ "      ,[CCOperation],[LotNo],[DataStatus]\r\n ";
	private String selectSendTestQC = "[ProductionOrder],[No],[SendDate]\r\n"
			+ "      ,[CheckColorDate],[RollNo],[Status],[DeltaE]\r\n"
			+ "      ,[Color],[Remark],[DataStatus]\r\n ";

	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	public PCMSMainDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<PCMSTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSTableDetail> list = null;
		String where = " where  ";
		String customerShortName = "", saleNumber = "", materialNo = "", saleOrder = "", saleLine = "",
				saleCreateDate = "", labNo = "", articleFG = "", designFG = "", userStatus = "", prdOrder = "",
				prdCreateDate = "", deliveryStatus = "", saleStatus = "", dist = "",customerName="";
		PCMSTableDetail bean = poList.get(0);
		customerName = bean.getCustomerName();
		customerShortName = bean.getCustomerShortName();
//		saleNumber = bean.getSaleNumber() ;  
		materialNo = bean.getMaterialNo();
		saleOrder = bean.getSaleOrder();
		saleCreateDate = bean.getSaleOrderCreateDate();
		saleNumber = bean.getSaleNumber();
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
				+ " MaterialNo like '" + materialNo  + "%' and\r\n" 
				+ " a.SaleOrder like '" + saleOrder + "%' \r\n"
//				  + " a.SaleLine like '"+saleLine+"%' and\r\n"
		;
		if (!saleCreateDate.equals("")) {  

			String[] dateArray = saleCreateDate.split("-");
			where += "and ( SaleCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " SaleCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) ) \r\n";
		}

		if(!saleNumber.equals("")) { where += " and A.SaleNumber like '"+saleNumber+"%' \r\n" ; }
		if (!labNo.equals("")) {
			where += " and b.LabNo like '" + labNo + "%'  \r\n";
		}
		if (!articleFG.equals("")) {
			where += " and a.ArticleFG like '" + articleFG + "%'  \r\n";   
		}
		if (!designFG.equals("")) {
			where += " and a.DesignFG like '" + designFG + "%'  \r\n";
		}
		if (!userStatus.equals("")) {
			where += " and b.UserStatus like '" + userStatus + "%'  \r\n";
		}
		if (!prdOrder.equals("")) {
			where += " and b.ProductionOrder like '" + prdOrder + "%'  \r\n ";
		}
		if (!prdCreateDate.equals("")) {
			String[] dateArray = prdCreateDate.split("-");
			where += " and ( PrdCreateDate >= CONVERT(DATE,'" + dateArray[0].trim() + "',103)  and \r\n"
					+ " PrdCreateDate <= CONVERT(DATE,'" + dateArray[1].trim() + "',103) )  \r\n";
		}
		if (!deliveryStatus.equals("")) {
			where += " and a.DeliveryStatus like '" + deliveryStatus + "%'  \r\n";
		}
		if (!saleStatus.equals("")) {
			where += " and a.SaleStatus like '" + saleStatus + "%'  \r\n";
		}
		if (!dist.equals("")) {
			String[] array = dist.split(",");
			where += " and  ( ";
			for (int i = 0; i < array.length; i++) {
				where += " a.DistChannel = '" + array[i] + "' ";
				if (i != array.length - 1) {
					where += " or ";
				} ;
			}
			where += " ) \r\n";
		}   
		String sql = 
				  "SELECT DISTINCT  " 
				+ this.select 
				+ " FROM [PCMS].[dbo].[FromSapMainSale] as a \r\n "
				+ this.leftJoinB
				+ this.leftJoinD
				+ this.leftJoinF
				+ this.leftJoinG
				+ this.leftJoinH
				+ this.leftJoinX
				+ this.leftJoinZ 
				+ this.leftJoinIToO
				+ where
				+ "  and x.ProductionOrder is null \r\n"
				+ " union "
				+ "SELECT DISTINCT  " 
				+ this.select 
				+ " FROM (  SELECT DISTINCT   "
				+ "           a.SaleOrder  , a.[SaleLine]  ,a.DistChannel ,a.Color ,a.ColorCustomer   \r\n"
				+ "	          , a.SaleQuantity ,a.RemainQuantity  ,a.SaleUnit  ,a.DueDate  ,a.CustomerShortName  \r\n"
				+ "           , a.[SaleFullName] ,  a.[SaleNumber]  ,a.SaleCreateDate ,a.MaterialNo ,a.DeliveryStatus ,a.SaleStatus  \r\n"
				+ "	          , b.ProductionOrder,a.CustomerName\r\n"
				+ "  ,a.DesignFG ,a.ArticleFG ,a.ShipDate,a.Division,a.PurchaseOrder,a.CustomerMaterial,a.Price,RemainAmount,CustomerDue\r\n"
				+ "		 from [PCMS].[dbo].[FromSapMainSale] as a  \r\n"
				+ "		 inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleLine = b.SaleLine and a.SaleOrder = b.SaleOrder \r\n"
				+ "		 where b.DataStatus <> 'X') as a \r\n "
				+ "  left join  [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
				+ this.leftJoinD
				+ this.leftJoinF
				+ this.leftJoinG
				+ this.leftJoinH
				+ this.leftJoinZ  
				+ this.leftJoinIToO  
				+ where   
				+ " Order by a.SaleOrder , SaleLine"; 
		  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		} 
		return list;
	}

	@Override
	public ArrayList<PCMSAllDetail> getPrdDetailByRow(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PCMSAllDetail> list = null;
		String where = " where  ";
		String prdOrder = "" ;
		PCMSTableDetail bean = poList.get(0); 
		prdOrder = bean.getProductionOrder();      
		where += " b.ProductionOrder = '" + prdOrder + "' \r\n";
		String sql = "SELECT distinct top 1   " 
				+ this.selectTwo 
				+ " from  [PCMS].[dbo].FromSapMainSale as a \r\n "
				+ " inner join [PCMS].[dbo].[FromSapMainProd] as b on a.SaleOrder = b.SaleOrder and a.SaleLine = b.SaleLine \r\n"
				+ this.leftJoinF
				+ this.leftJoinG   
				+ this.leftJoinH
				+ where 
				+ " Order by a.SaleOrder , 	SaleLine"; 	
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql); 
		list = new ArrayList<PCMSAllDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		} 
		if (list.size() > 0) {
			ArrayList<PODetail> poDetailList = getPODetail(poList);
			ArrayList<PresetDetail> presetDetailList = getPresetDetail(poList);
			ArrayList<DyeingDetail> dyeingDetailList = getDyeingDetail(poList);

			ArrayList<SendTestQCDetail> sendTestQCDetailList = getSendTestQCDetail(poList);
			ArrayList<FinishingDetail> finDetailList = getFinishingDetail(poList);
			ArrayList<InspectDetail> insDetailList = getInspectDetail(poList);
			ArrayList<PackingDetail> packDetailList = getPackingDetail(poList);

			ArrayList<WorkInLabDetail> workInLabDetailList = getWorkInLabDetail(poList);
			ArrayList<WaitTestDetail> waitTestDetailList = getWaitTestDetail(poList);
			ArrayList<CFMDetail> cfmDetailList = getCFMDetail(poList);
			ArrayList<SaleDetail> saleDetailList = getSaleDetail(poList);
			ArrayList<SaleInputDetail> saleInputDetailList = getSaleInputDetail(poList);
			ArrayList<InputDateDetail> submitdatDetailList = getSubmitDateDetail(poList);
			ArrayList<NCDetail> ncDetailList = getNCDetail(poList);
			ArrayList<ReceipeDetail> receipeDetailList = getReceipeDetail(poList);
			list.get(0).setPoDetailList(poDetailList);
			list.get(0).setPresetDetailList(presetDetailList);
			list.get(0).setSendTestQCDetailList(sendTestQCDetailList);
			list.get(0).setDyeingDetailList(dyeingDetailList);
			list.get(0).setFinishingDetailList(finDetailList);
			list.get(0).setInspectDetailList(insDetailList);
			list.get(0).setPackingDetailList(packDetailList);

			list.get(0).setWorkInLabDetailList(workInLabDetailList);
			list.get(0).setWaitTestDetailList(waitTestDetailList);
			list.get(0).setCfmDetailList(cfmDetailList);
			list.get(0).setSaleDetailList(saleDetailList);
			list.get(0).setSaleInputDetailList(saleInputDetailList);
			list.get(0).setSubmitdatDetailList(submitdatDetailList);
			list.get(0).setNcDetailList(ncDetailList);
			list.get(0).setReceipeDetailList(receipeDetailList);

		}
		return list;
	}

	private ArrayList<ReceipeDetail> getReceipeDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<ReceipeDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[No],[PostingDate]\r\n"
				+ "      ,[LotNo],[Receipe],[DataStatus] \r\n"
				+ " from [PCMS].[dbo].[FromSapReceipe] as a \r\n " + where + " Order by No";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<ReceipeDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genReceipeDetail(map));
		}
		return list;
	}

	private ArrayList<NCDetail> getNCDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<NCDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[SaleOrder],[SaleLine]\r\n"
				+ "      ,[No],[NCDate],[CarNo],[Remark]\r\n"
				+ "      ,[Quantity],[Unit],[NCFrom],[DataStatus]\r\n"
				+ "  " + " from [PCMS].[dbo].[FromSapNC] as a \r\n " + where + " Order by No";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<NCDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genNCDetail(map));
		}
		return list;
	}

	private ArrayList<InputDateDetail> getSubmitDateDetail(ArrayList<PCMSTableDetail> poList) {
//		ArrayList<SubmitDateDetail> list = null;
//		String where = " where  ";
//		String prdOrder = "";
//		PCMSTableDetail bean = poList.get(0);
//		prdOrder = bean.getProductionOrder();
//		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
//		String sql = "SELECT DISTINCT  " 
//				+ " [ProductionOrder],[SaleOrder],[SaleLine]\r\n"
//				+ "      ,[No],[SubmitDate],[Remark],[DataStatus]\r\n"
//				+ "  " 
//				+ " from [PCMS].[dbo].[FromSapSubmitDate] as a \r\n " 
//				+ where
//				+ " Order by No";
////		 System.out.println(sql);
//		List<Map<String, Object>> datas = this.database.queryList(sql);
//		list = new ArrayList<SubmitDateDetail>();
//		for (Map<String, Object> map : datas) {
//			list.add(this.bcModel._genSubmitDateDetail(map));
//		}
		
		ArrayList<InputDateDetail> list = null;
		PCMSTableDetail bean = poList.get(0);
		String prdOrder = bean.getProductionOrder();
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
		  + " where a.[ProductionOrder] = '" + prdOrder + "' and "
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
		  + " where a.[ProductionOrder] = '" + prdOrder + "'    and SubmitDate is not null "
  		  + "   and [DataStatus] = 'O'"
		  + "   ORDER BY InputFrom ,CreateDate ";
				
		 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InputDateDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}

	private ArrayList<SaleInputDetail> getSaleInputDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<SaleInputDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[BillDate]\r\n"
				+ "      ,[BillQtyPerSale],[SaleOrder]"
				+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "		WHEN 0 THEN ''  \r\n"
				+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "		END AS [SaleLine] "
				+ "      ,[BillQtyPerStock],[Remark],[CustomerNo]\r\n"
				+ "      ,[CustomerName1],[CustomerPO],[DueDate]\r\n"
				+ "      ,[Color],[No],[DataStatus]\r\n" + " "
				+ " from [PCMS].[dbo].[FromSapSaleInput] as a \r\n " + where + " Order by [No]";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SaleInputDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSaleInputDetail(map));
		}
		return list;
	}

	private ArrayList<SaleDetail> getSaleDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<SaleDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[BillDate]\r\n"
				+ "      ,[BillQtyPerSale],[SaleOrder]"
				+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "		WHEN 0 THEN ''  \r\n"
				+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "		END AS [SaleLine] "
				+ "      ,[BillQtyPerStock],[Remark],[CustomerNo]\r\n"
				+ "      ,[CustomerName1],[CustomerPO],[DueDate]\r\n"
				+ "      ,[Color],[No],[DataStatus]\r\n" + "  "
				+ " from [PCMS].[dbo].[FromSapSale] as a \r\n " + where + " Order by [No]";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SaleDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSaleDetail(map));
		}
		return list;
	}

	private ArrayList<CFMDetail> getCFMDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<CFMDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[CFMNo],[CFMNumber]\r\n"
				+ "      ,[CFMSendDate],[CFMAnswerDate],[CFMStatus]\r\n"
				+ "      ,[CFMRemark],[Da],[Db],[L]\r\n"
				+ "      ,[ST],[SaleOrder]"
				+ "		,CASE PATINDEX('%[^0 ]%', a.[SaleLine]  + ' ‘')\r\n"
				+ "		WHEN 0 THEN ''  \r\n"
				+ "		ELSE SUBSTRING(a.[SaleLine] , PATINDEX('%[^0 ]%', a.[SaleLine]  + ' '), LEN(a.[SaleLine] ) )\r\n"
				+ "		END AS [SaleLine] "
				+ "     ,[CFMCheckLab]\r\n"
				+ "      ,[CFMNextLab],[CFMCheckLot],[CFMNextLot]\r\n"
				+ "      ,[NextLot],[SOChange],[SOChangeQty]\r\n"
				+ "      ,[SOChangeUnit],[RollNo],[RollNoRemark]\r\n"
				+ "      ,[DataStatus]\r\n" + " " + " from [PCMS].[dbo].[FromSapCFM] as a \r\n " + where
				+ " Order by [CFMNo]";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<CFMDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCFMDetail(map));
		}
		return list;
	}

	private ArrayList<WaitTestDetail> getWaitTestDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<WaitTestDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + " [ProductionOrder],[No],[DateInTest]\r\n"
				+ "      ,[DateOutTest],[Status],[Remark],[DataStatus]\r\n"
				+ " " + " from [PCMS].[dbo].[FromSapWaitTest] as a \r\n " + where + " Order by [No]";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<WaitTestDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genWaitTestDetail(map));
		}
		return list;
	}

	private ArrayList<WorkInLabDetail> getWorkInLabDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<WorkInLabDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + "  [ProductionOrder],[SaleOrder]\r\n"
				+ "      ,[SaleLine],[No],[SendDate],[NOK]\r\n"
				+ "      ,[LotNo],[ReceiveDate],[Remark],[Da]\r\n"
				+ "      ,[Db],[L],[ST],[DataStatus]\r\n" + "   "
				+ " from [PCMS].[dbo].[FromSapWorkInLab] as a \r\n " + where + " Order by No";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<WorkInLabDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genWorkInLabDetail(map));
		}
		return list;
	}

	private ArrayList<PackingDetail> getPackingDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PackingDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectPacking + " from [PCMS].[dbo].[FromSapPacking] as a \r\n "
				+ where;
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PackingDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPackingDetail(map));
		}
		return list;
	}

	private ArrayList<FinishingDetail> getFinishingDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<FinishingDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectFinishing + " from [PCMS].[dbo].[FromSapFinishing] as a \r\n "
				+ where + " Order by Operation";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<FinishingDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genFinishingDetail(map));
		}
		return list;
	}

	private ArrayList<InspectDetail> getInspectDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<InspectDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectInspect + " from [PCMS].[dbo].[FromSapInspect] as a \r\n " + where
				+ " Order by Operation";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<InspectDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInspectDetail(map));
		}
		return list;
	}

	private ArrayList<SendTestQCDetail> getSendTestQCDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<SendTestQCDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectSendTestQC + " from [PCMS].[dbo].[FromSapSendTestQC] as a \r\n "
				+ where;
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<SendTestQCDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSendTestQCDetail(map));
		}
		return list;
	}

	private ArrayList<DyeingDetail> getDyeingDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<DyeingDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectDyeing + " from [PCMS].[dbo].[FromSapDyeing] as a \r\n " + where
				+ " Order by Operation";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<DyeingDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genDyeingDetail(map));
		}
		return list;
	}

	private ArrayList<PresetDetail> getPresetDetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PresetDetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectPreset + " from [PCMS].[dbo].[FromSapPreset] as a \r\n " + where;
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PresetDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPresetDetail(map));
		}
		return list;
	}

	public ArrayList<PODetail> getPODetail(ArrayList<PCMSTableDetail> poList) {
		ArrayList<PODetail> list = null;
		String where = " where  ";
		String prdOrder = "";
		PCMSTableDetail bean = poList.get(0);
		prdOrder = bean.getProductionOrder();
		where += " a.ProductionOrder = '" + prdOrder + "'  and [DataStatus] = 'O' \r\n";
		String sql = "SELECT DISTINCT  " + this.selectPO + " from [PCMS].[dbo].[FromSapPO] as a \r\n " + where
				+ " Order by [RollNo]";
//		      
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PODetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPODetail(map));
		}
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
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<PCMSTableDetail>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSTableDetail(map));
		}
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
