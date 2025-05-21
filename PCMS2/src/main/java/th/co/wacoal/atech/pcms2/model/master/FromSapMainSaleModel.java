package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.FromSapMainSaleDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.FromSapMainSaleDaoImpl;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class FromSapMainSaleModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private FromSapMainSaleDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public FromSapMainSaleModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new FromSapMainSaleDaoImpl(this.database);
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

	public ArrayList<PCMSSecondTableDetail> getDivisionDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSSecondTableDetail> list = this.dao.getDivisionDetail();
		return list;
	}

	public ArrayList<PCMSAllDetail> getCustomerNameDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerNameDetail();
		return list;
	}

	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerShortNameDetail();
		return list;
	}

	public ArrayList<PCMSTableDetail> getSaleNumberDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSTableDetail> list = this.dao.getSaleNumberDetail();
		return list;
	}

	public ArrayList<PCMSAllDetail> getCustomerNameDetail(ArrayList<ConfigCustomerUserDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerNameDetail(poList);
		return list;
	}

	public ArrayList<PCMSAllDetail> getCustomerShortNameDetail(ArrayList<ConfigCustomerUserDetail> poList)
	{
		// TODO Auto-generated method stub
		ArrayList<PCMSAllDetail> list = this.dao.getCustomerShortNameDetail(poList);
		return list;
	}

	public  String upsertFromSapMainSaleDetail( ArrayList<FromErpMainSaleDetail> paList ){
		// TODO Auto-generated method stub
		String  iconStatus = this.dao.upsertFromSapMainSaleDetail(paList );
		return iconStatus;
	}
}
