package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.FromSapPackingDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.FromSapPackingDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapPackingModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapPackingDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSapPackingModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new FromSapPackingDaoImpl(this.database);
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

	public ArrayList<PackingDetail> getFromSapPackingDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<PackingDetail> list = this.dao.getFromSapPackingDetailByProductionOrder(prodOrder);
		return list;
	}
 
	public String upsertFromSapPackingDetail(ArrayList<FromErpPackingDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertFromSapPackingDetail(paList);
		return iconStatus;
		
	}
}
