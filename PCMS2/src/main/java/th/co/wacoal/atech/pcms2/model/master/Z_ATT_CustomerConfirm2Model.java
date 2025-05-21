package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.Z_ATT_CustomerConfirm2Dao;
import th.co.wacoal.atech.pcms2.dao.master.implement.Z_ATT_CustomerConfirm2DaoImpl;
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class Z_ATT_CustomerConfirm2Model extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private Z_ATT_CustomerConfirm2Dao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public Z_ATT_CustomerConfirm2Model() {
		try {
			this.database = new Database(SqlPCMSInfo.getInstance());
			this.dao = new Z_ATT_CustomerConfirm2DaoImpl(this.database);
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

	public ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2DetailByProductionOrder(
			String prodOrder
			,String lotNubmer 
			,String replyDate
			,String custName 
			,String so 
			,String sendDate ) 
	{
		// TODO Auto-generated method stub
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = this.dao.getZ_ATT_CustomerConfirm2Detail(
				  prodOrder
				,  lotNubmer 
				,  replyDate
				,  custName 
				,  so 
				,  sendDate);
		return list;
	} 
	public ArrayList<Z_ATT_CustomerConfirm2Detail> getZ_ATT_CustomerConfirm2DetailById(ArrayList<Z_ATT_CustomerConfirm2Detail> poList) 
	{
		// TODO Auto-generated method stub
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = 
				this.dao.getZ_ATT_CustomerConfirm2DetailById( poList); 
		return list;
	}

	public String upsertZ_ATT_CustomerConfirm2Detail(ArrayList<Z_ATT_CustomerConfirm2Detail> zCustList)
	{
		// TODO Auto-generated method stub
		 String iconStatus = this.dao.upsertZ_ATT_CustomerConfirm2Detail(zCustList);
		 return iconStatus;
	} 
}
