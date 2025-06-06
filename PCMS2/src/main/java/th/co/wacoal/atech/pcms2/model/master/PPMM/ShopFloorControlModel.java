package th.co.wacoal.atech.pcms2.model.master.PPMM;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.ShopFloorControlDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.PPMM.ShopFloorControlDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PPMM.ShopFloorControlDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class ShopFloorControlModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private ShopFloorControlDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public ShopFloorControlModel() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new ShopFloorControlDaoImpl(this.database);
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

	public ArrayList<ShopFloorControlDetail> getShopFloorControlDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<ShopFloorControlDetail> list = this.dao.getShopFloorControlDetailByProductionOrder(prodOrder);
		return list;
	}
}
