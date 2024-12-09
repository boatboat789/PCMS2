package dao.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.SOR_DUE_REPORTDao;
import entities.SOR_DUE_REPORTDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

public class SOR_DUE_REPORTDaoImpl implements SOR_DUE_REPORTDao {
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	@SuppressWarnings("unused")
	private String conType;

	public SOR_DUE_REPORTDaoImpl(Database database, String conType) {
		this.database = database;
		this.message = "";
		this.conType = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<SOR_DUE_REPORTDetail> getSORDueReportDetail() {
		ArrayList<SOR_DUE_REPORTDetail> list = null;
		String sql = ""
				+ " SELECT  [PO_DATE]\r\n"
				+ "      ,[PO_NO]\r\n"
				+ "      ,[SO_NO]\r\n"
				+ "      ,[SO_LINE]\r\n"
				+ "      ,[MATERIAL_CODE]\r\n"
				+ "      ,[REFERENCE_ID]\r\n"
				+ "      ,[GREIGE_UPDATED]\r\n"
				+ "      ,[CFM_UPDATED]	\r\n"
				+ "  FROM [SOR_IF_SAP].[dbo].[SOR_DUE_REPORT]\r\n"
				+ "  WHERE "
//				+ "		PO_DATE >= '2023-05-05'\r\n"
				+ "		PO_DATE >= CAST(GETDATE()-1 AS DATE) OR\r\n"
				+ "		[GREIGE_UPDATED] >= CAST(GETDATE()-1 AS DATE) OR\r\n"
				+ "		[CFM_UPDATED] >= CAST(GETDATE()-1 AS DATE) \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genSOR_DUE_REPORTDetail(map));
		}
		return list;
	}
}
