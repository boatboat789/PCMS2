	package th.co.wacoal.atech.pcms2.dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.PlanSendCFMCusDateDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.model.BeanCreateModel;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class PlanSendCFMCusDateDaoImpl implements  PlanSendCFMCusDateDao{
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
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}
}
