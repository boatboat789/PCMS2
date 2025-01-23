	package dao.master.implement.LBMS;

import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.master.LBMS.ImportDetailDao;
import entities.LBMS.ImportDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

@Component
public class ImportDetailDaoImpl implements  ImportDetailDao{
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
	public ImportDetailDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}
	@Override
	public  ArrayList<ImportDetail> getImportDetailByProductionOrder(String prodOrder){
		ArrayList<ImportDetail> list = null;
		String where = " where  "; 
		where += " imd.ProductionOrder = '" + prodOrder + "'  AND "
				+ " SendFrom in (\r\n"
				+ "  'QA',\r\n"
				+ "  'Dye',\r\n"
				+ "  'PC',\r\n"
				+ "  'ReFormula' )  \r\n" ;
		String sql =
				  "  "
				  + " SELECT DISTINCT  \r\n"
				  + "	   imd.[ProductionOrder] \r\n"
				  + "      ,imd.[LabNo]\r\n" 
				  + "      ,imd.[Remark]\r\n"
				  + "      ,imd.[ImportStatus]\r\n"
				  + "      ,imd.[SendFrom]\r\n"
				  + "      ,imd.[SendLabDate]  \r\n"
				  + "      ,imd.[DateRequiredLab] \r\n"
				  + "	  ,ROW_NUMBER() OVER (\r\n"
				  + "     PARTITION BY ProductionOrder\r\n"
				  + "    ORDER BY ProductionOrder , SendLabDate\r\n"
				  + ") as NoOfSendInLab\r\n"
				  + "	  ,lwpd.No as NoOfStartLab\r\n"
				  + "	  ,StartDate as LabStartDate\r\n"
				  + "	  ,StopDate as LabStopDate\r\n"
				  + "  FROM [LBMS].[dbo].[ImportDetail] as imd\r\n"
				  + "  inner join (\r\n"
				  + "	select main.ImportId ,No,StartDate ,StopDate\r\n"
				  + "	from [LBMS].[dbo].[LabWorkProcessDetail] as main\r\n"
				  + "	INNER join (\r\n"
				  + "		select ImportId ,max(No) as maxNo \r\n"
				  + "		from [LBMS].[dbo].[LabWorkProcessDetail] \r\n" 
				  + "		group by ImportId\r\n"
				  + "	) as sub on main.ImportId = sub.ImportId and\r\n"
				  + "				main.[No] = sub.[maxNo]\r\n"
				  + "  ) as lwpd on imd.[Id] = lwpd.[ImportId]"
				  + where  
				  + " Order by imd.[ProductionOrder],imd.SendLabDate\r\n"
				  + ""; 
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genImportDetail(map));
		}
		return list;
	} 
}
