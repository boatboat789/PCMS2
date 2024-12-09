package dao.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.SOASDataImportChangeDetailDao;
import entities.CSO_SOASDetail;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

public class SOASDataImportChangeDetailDaoImpl implements SOASDataImportChangeDetailDao {
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	@SuppressWarnings("unused")
	private String conType;

	public SOASDataImportChangeDetailDaoImpl(Database database, String conType) {
		this.database = database;
		this.message = "";
		this.conType = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public ArrayList<CSO_SOASDetail> getSOASDataChangeDetail() {
		ArrayList<CSO_SOASDetail> list = null;
		String sql = ""
				+ "SELECT  SDH.[PURCH_NO]\r\n"
				+ "   ,SDH.[SALES_ORDER_NO]\r\n"
				+ "   ,SDI.[DOC_ITEM]\r\n"
				+ "   ,SDH.[DIVISION] \r\n"
				+ "   ,SDH.[SOLD_TO]  \r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CDIC.MATERIAL IS NOT NULL THEN CDIC.MATERIAL\r\n"
				+ "  	ELSE SDI.MATERIAL\r\n"
				+ "   END AS MATERIAL \r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CDIC.[ORD_QTY] IS NOT NULL THEN CDIC.[ORD_QTY]\r\n"
				+ "   	ELSE SDI.ORD_QTY\r\n"
				+ "   	END AS ORD_QTY \r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CDIC.[BASE_UNIT] IS NOT NULL THEN CDIC.[BASE_UNIT]\r\n"
				+ "   	ELSE SDI.[BASE_UNIT]\r\n"
				+ "   	END AS [BASE_UNIT]  \r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CDIC.[SHIPDATE] IS NOT NULL THEN CDIC.[SHIPDATE]\r\n"
				+ "   	ELSE SDI.[SHIPDATE]\r\n"
				+ "   	END AS [SHIPDATE]  \r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CDIC.[DUE_SUBMIT_DATE] IS NOT NULL THEN CDIC.[DUE_SUBMIT_DATE]\r\n"
				+ "   	ELSE SDI.[DUE_SUBMIT_DATE]\r\n"
				+ "   	END AS [DUE_SUBMIT_DATE]  \r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CDIC.[LAB_NO] IS NOT NULL THEN CDIC.[LAB_NO]\r\n"
				+ "   	ELSE SDI.[LAB_NO]\r\n"
				+ "   	END AS [LAB_NO]  \r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CDIC.[CUST_MAT] IS NOT NULL THEN CDIC.[CUST_MAT]\r\n"
				+ "   	ELSE SDI.[CUST_MAT]\r\n"
				+ "   	END AS [CUST_MAT]  \r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CDIC.[DUE_GREIGE] IS NOT NULL THEN CDIC.[DUE_GREIGE]\r\n"
				+ "   	ELSE SDI.[DUE_GREIGE]\r\n"
				+ "   	END AS [DUE_GREIGE]  \r\n"
				+ "   ,CDIC.[CHANGE_ORDER_ID]\r\n"
				+ "   ,CMCO.FIELD_CODE\r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CMCO.FIELD_CODE = 'SHIPDATE' THEN CONVERT(CHAR(10), CONVERT(datetime, OLD_VALUE), 120)\r\n"
				+ "   	ELSE OLD_VALUE\r\n"
				+ "   	END AS OLD_VALUE \r\n"
				+ "   ,CASE\r\n"
				+ "   	WHEN CMCO.FIELD_CODE = 'SHIPDATE' THEN CONVERT(CHAR(10), CONVERT(datetime, NEW_VALUE), 120)\r\n"
				+ "   	ELSE NEW_VALUE\r\n"
				+ "   	END AS NEW_VALUE \r\n"
				+ "   ,CDIC.[LASTCHANGED_ON]  \r\n"
				+ "  FROM [SOAS].[dbo].[SO_DOC_HEAD] as SDH\r\n"
				+ "  INNER JOIN [SOAS].[dbo].[CSO_DOC_ITEM_CHANGE] AS CDIC ON CDIC.[DOCSALES_ID] = SDH.[DOCSALES_ID] and CDIC.ACCEPT_CHANGED = 1 \r\n"
				+ "  LEFT JOIN [SOAS].[dbo].[SO_DOC_ITEM] AS SDI ON SDI.DOCSALES_ID = CDIC.DOCSALES_ID AND SDI.[DOC_ITEM] = CDIC.[DOC_ITEM]  \r\n"
				+ "  INNER JOIN [SOAS].[dbo].[CSO_MAS_CHANGE_ORDERS] AS CMCO ON CDIC.[CHANGE_ORDER_ID] = CMCO.[CHANGE_ORDER_ID] and CMCO.[FLAG_DEL] = 0\r\n"
				+ "  where \r\n"
				+ "    JOB_ID = '02' and -- CHANGE ORDER\r\n"
				+ "    ( TRANS_ID like 'S%' or TRANS_ID like 'F%' ) AND -- SALE CREATE REQUEST\r\n"
				+ "    [DOCSTEP_ID] = 500 AND --BKK APPROVED CHANGE\r\n"
				+ "    [DOCSTATUS_ID] = 2 AND-- CLOSED\r\n"
				+ "    [STEP_STATUS_ID] = 4 AND-- APPROVED\r\n"
				+ "    CDIC.ACCEPT_CHANGED = 1  AND\r\n"
				+ "    CDIC.[LASTCHANGED_ON] >= CAST(GETDATE() AS DATE) \r\n"
				+ "  order by CDIC.[LASTCHANGED_ON],SDH.[PURCH_NO],SDI.[DOC_ITEM],SDH.[SALES_ORDER_NO]  \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genCSO_SOASDetail(map));
		}
		return list;
	}
}
