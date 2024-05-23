	package dao.master.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.master.TEMP_UserStatusAutoDao;
import entities.PCMSSecondTableDetail;
import entities.TempUserStatusAutoDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;
import utilities.SqlStatementHandler;

public class TEMP_UserStatusAutoDaoImpl implements  TEMP_UserStatusAutoDao{
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

	public TEMP_UserStatusAutoDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<TempUserStatusAutoDetail> getTempUserStatusAutoDetail(ArrayList<PCMSSecondTableDetail> poList) {
		ArrayList<TempUserStatusAutoDetail> list = null;
		//		String prdOrder = "";
		PCMSSecondTableDetail bean = poList.get(0);
		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		String saleLine = bean.getSaleLine();
		saleLine = String.format("%06d", Integer.parseInt(saleLine));
		String sql = "SELECT distinct  [Id]\r\n"
				+ "      ,[ProductionOrder]\r\n"
				+ "      ,[SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[ProductionOrderRPM]\r\n"
				+ "      ,[Volumn]\r\n"
				+ "      ,[Grade]\r\n"
				+ "      ,[UserStatusCal]\r\n"
				+ "      ,[UserStatusCalRP]\r\n"
				+ "      ,[DataStatus]\r\n"
				+ "      ,[ChangeDate]\r\n"
				+ "      ,[CreateDate]\r\n"
				+ "  FROM [PCMS].[dbo].[TEMP_UserStatusAuto] \r\n"
				+ "  WHERE ( GRADE IS NULL OR GRADE = '' ) and\r\n"
				+ "		ProductionOrder = '"+prdOrder+"' and \r\n"
				+ "		SaleOrder = '"+saleOrder+"' and\r\n"
				+ "		SaleLine = '"+saleLine+"' and\r\n"
				+ "		DataStatus = 'O'";
//		 System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genTempUserStatusAutoDetail(map));
		}
		return list;
	}
}
