	package th.co.wacoal.atech.pcms2.dao.master.implement.PPMM;

import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.ShopFloorControlDao;
import th.co.wacoal.atech.pcms2.entities.PPMM.ShopFloorControlDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Component
public class ShopFloorControlDaoImpl implements  ShopFloorControlDao{
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

	@Autowired
    public ShopFloorControlDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<ShopFloorControlDetail> getShopFloorControlDetailByProductionOrder(String prodOrder){
		ArrayList<ShopFloorControlDetail> list = null;
		String where = " where  "; 
		where += " dfs.ProductionOrder = '" + prodOrder + "' and \r\n"
			+ "		dfs.[AdminStatus] = '-'\r\n";
		String sql =
				  "  SELECT DISTINCT  \r\n"
				  + " dfs.[ProductionOrder] \r\n"
				  + " ,dfs.[LotNo]\r\n"
				  + " ,dfs.[Operation]	\r\n"
				  + " ,dfs.[WorkCenter]	\r\n"
				  + " ,dfs.[OperationEndDate] as WorkDate\r\n"
				  + ",sfc.[DyeingStatus]\r\n"
				  + ",sfc.[DyeRemark] \r\n"
				  + ",sfc.[CartNo]\r\n"
				  + ",sfc.[CartType]\r\n"
				  + ",sfc.ColorCheckOperation\r\n"
				  + ",sfc.ColorCheckWorkDate\r\n"
				  + ",sfc.[Da]\r\n"
				  + ",sfc.[Db]\r\n"
				  + ",sfc.[ST]\r\n"
				  + ",sfc.[L]\r\n"
				  + ",sfc.[ValDeltaE]\r\n"
				  + ",sfc.[DyeRemark]\r\n"
				  + ",sfc.[ColorCheckName]\r\n"
				  + ",sfc.[ColorCheckStatus]\r\n"
				  + ",sfc.[ColorCheckRollNo]\r\n"
				  + ",sfc.[ColorCheckRemark]\r\n"
				  + "  FROM [PPMM].[dbo].[DataFromSap] as dfs  \r\n"
				  + " left join ( \r\n"
				  + "		select sfc.[ProductionOrder] ,sfc.[CartNo]\r\n"
				  + "			  	   ,sfc.[CartType]\r\n"
				  + "			        ,sfc.[Operation] as ColorCheckOperation\r\n"
				  + "			        ,sfc.[WorkDate] as ColorCheckWorkDate\r\n"
				  + "			        ,sfc.[Da]\r\n"
				  + "			        ,sfc.[Db]\r\n"
				  + "			        ,sfc.[ST]\r\n"
				  + "			        ,sfc.[L]\r\n"
				  + "			        ,sfc.[ValDeltaE]\r\n"
				  + "			        ,sfc.[DyeRemark]\r\n"
				  + "			        ,sfc.[ColorCheckName]\r\n"
				  + "			        ,sfc.[ColorCheckStatus]\r\n"
				  + "			        ,sfc.[ColorCheckRollNo]\r\n"
				  + "			        ,sfc.[ColorCheckRemark]\r\n"
				  + "					,sfc.[DyeingStatus] \r\n"
				  + "\r\n"
				  + "		from [PPMM].[dbo].[DataFromSap] as dfs\r\n"
				  + "		inner join (\r\n"
				  + "			select mainSFC.*\r\n"
				  + "			from [PPMM].[dbo].[ShopFloorControlDetail] as mainSFC\r\n"
				  + "			INNER JOIN ( \r\n"
				  + "				SELECT ProductionOrder , max(Operation) as maxOperation\r\n"
				  + "				from [PPMM].[dbo].[ShopFloorControlDetail]\r\n"
				  + "				where Operation >= 195 and Operation <= 198\r\n"
				  + "				group by ProductionOrder\r\n"
				  + "			) as subSFC on mainSFC.[ProductionOrder] = subSFC.[ProductionOrder] and\r\n"
				  + "					   		mainSFC.[Operation] = subSFC.[maxOperation] 	\r\n"
				  + "		) as sfc on dfs.[ProductionOrder] = sfc.[ProductionOrder] and\r\n"
				  + "					dfs.[Operation] = sfc.[Operation] \r\n"
				  + "	 	where  dfs.[Operation] >= 195 and dfs.Operation <= 198 and\r\n"
				  + "			dfs.[AdminStatus] = '-'\r\n"
				  + "  ) as sfc on sfc.[ProductionOrder] = dfs.[ProductionOrder] \r\n"
				  + where
				  + " and dfs.[Operation] >= 190 and dfs.Operation <= 193 and\r\n"
				  + " 	  dfs.[AdminStatus] = '-' \r\n"
				  + " union\r\n"
				  + " SELECT \r\n"
				  + "dfs.[ProductionOrder] \r\n"
				  + ",dfs.[LotNo]\r\n"
				  + ",dfs.[Operation]	\r\n"
				  + ",dfs.[WorkCenter]\r\n"
				  + ",dfs.[OperationEndDate] as WorkDate\r\n"
				  + ",sfc.[DyeingStatus]\r\n"
				  + ",sfc.[DyeRemark] \r\n"
				  + ",sfc.[CartNo]\r\n"
				  + ",sfc.[CartType]\r\n"
				  + ",'' as ColorCheckOperation\r\n"
				  + ",'' as ColorCheckWorkDate\r\n"
				  + ",sfc.[Da]\r\n"
				  + ",sfc.[Db]\r\n"
				  + ",sfc.[ST]\r\n"
				  + ",sfc.[L]\r\n"
				  + ",sfc.[ValDeltaE]\r\n"
				  + ",sfc.[DyeRemark]\r\n"
				  + ",sfc.[ColorCheckName]\r\n"
				  + ",sfc.[ColorCheckStatus]\r\n"
				  + ",sfc.[ColorCheckRollNo]\r\n"
				  + ",sfc.[ColorCheckRemark]\r\n"
				  + "  FROM [PPMM].[dbo].[DataFromSap] as dfs  \r\n"
				  + "  left join [PPMM].[dbo].[ShopFloorControlDetail] as sfc on sfc.[ProductionOrder] = dfs.[ProductionOrder] and\r\n"
				  + "													        sfc.[Operation] = dfs.[Operation]\r\n"
				  + where
				  + "      and "
				  + "      (\r\n"
				  + "			( dfs.[Operation] >= 100 and dfs.Operation <= 104 ) or\r\n"
				  + "			( dfs.[Operation] in ( 50,60,145,180,200,201 )   )\r\n"
				  + "		)  \r\n" 
				  + " Order by dfs.[ProductionOrder],dfs.Operation\r\n"
				  + "";  
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genShopFloorControlDetail(map));
		}
		return list;
	} 
}
