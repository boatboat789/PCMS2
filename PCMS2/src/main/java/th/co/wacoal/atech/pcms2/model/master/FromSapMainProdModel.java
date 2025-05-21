package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainProdDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.FromSapMainProdDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapMainProdModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapMainProdDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSapMainProdModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new FromSapMainProdDaoImpl(this.database);
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

	public ArrayList<PCMSSecondTableDetail> getFromSapMainProdDetail(String prdOrderRP)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getFromSapMainProdDetail(prdOrderRP);
		return list;
	}

	public ArrayList<PCMSAllDetail> getUserStatusDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getUserStatusDetail();
		return list;
	}

	public String upsertFromSapMainProdDetail(ArrayList<FromErpMainProdDetail> paList)
	{
		// TODO Auto-generated method stub
		String iconStatus = this.dao.upsertFromSapMainProdDetail(paList);
		return iconStatus;
	}

}
