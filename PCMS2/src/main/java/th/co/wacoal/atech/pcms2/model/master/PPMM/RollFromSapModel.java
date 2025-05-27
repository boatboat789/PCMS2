package th.co.wacoal.atech.pcms2.model.master.PPMM;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.RollFromSapDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.PPMM.RollFromSapDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PODetail;
import th.co.wacoal.atech.pcms2.info.SqlPPMMInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class RollFromSapModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private RollFromSapDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

	@Autowired
	public RollFromSapModel() {
		try {
			this.database = new Database(SqlPPMMInfo.getInstance());
			this.dao = new RollFromSapDaoImpl(this.database);
			this.uiColumns = arrayColumn();
		} catch (SQLException | ClassNotFoundException var2) {
			var2.printStackTrace();
		}

	}

	public static String stringColumn()
	{
		return "[]";
	}

	public static String[] arrayColumn()
	{
		return "".replaceAll("'", "").split(",");
	}

	@Override
	public void destroy()
	{
		this.database.close();
		super.destroy();
	}

	public ArrayList<PODetail> getRollFromSapDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<PODetail> list = this.dao.getRollFromSapDetailByProductionOrder(prodOrder);
		return list;
	}

//	public String upsertRollFromSapFromERPPODetail(ArrayList<FromErpPODetail> list)
//	{
//		// TODO Auto-generated method stub
//		String iconStatus = this.dao.upsertRollFromSapFromERPPODetail(list);
//		return iconStatus;
//	}

}
