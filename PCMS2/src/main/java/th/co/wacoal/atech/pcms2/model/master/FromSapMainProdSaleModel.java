package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainProdSaleDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.FromSapMainProdSaleDaoImpl;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapMainProdSaleModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapMainProdSaleDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSapMainProdSaleModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new FromSapMainProdSaleDaoImpl(this.database);
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
	public  String upsertFromSapMainProdSaleDetail( ArrayList<FromErpMainProdSaleDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapMainProdSaleDetail(paList );
		return iconStatus;
	}
}
