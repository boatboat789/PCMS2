package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.FromSapSaleDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.FromSapSaleDaoImpl;
import th.co.wacoal.atech.pcms2.entities.SaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;
import th.co.wacoal.atech.pcms2.info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapSaleModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapSaleDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSapSaleModel () {
		try {
			this.database = new Database(SqlInfo.getInstance());
			this.dao = new FromSapSaleDaoImpl(this.database);
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

	public ArrayList<SaleDetail> getFromSapSaleDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<SaleDetail> list = this.dao.getFromSapSaleDetailByProductionOrder(prodOrder);
		return list;
	}
	public String   upsertFromSapSaleDetail(ArrayList<FromErpSaleDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertFromSapSaleDetail(paList);
		return iconStatus;
	}
}
