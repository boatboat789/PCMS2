package th.co.wacoal.atech.pcms2.model.master.erp.atech;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.erp.atech.ERPAtechDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.erp.atech.ERPAtechDaoImpl;
import th.co.wacoal.atech.pcms2.entities.erp.atech.CustomerDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpCFMDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpGoodReceiveDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainBillBatchDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainProdSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpMainSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPODetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpReceipeDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleInputDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;
import th.co.wacoal.atech.pcms2.info.SqlAtechERPInfo; 
import th.in.totemplate.core.sql.Database;

@Component
public class ERPAtechModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	private ERPAtechDao dao;
	@SuppressWarnings("unused")
	private String[] uiColumns;
	@SuppressWarnings("unused")
	private static final String columns = "";

    @Autowired
	public ERPAtechModel() {
		try {
			this.database = new Database(SqlAtechERPInfo.getInstance());
			this.dao = new ERPAtechDaoImpl(this.database);
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

	public ArrayList<CustomerDetail> getCustomerDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<CustomerDetail> list = this.dao.getCustomerDetail();
		return list;
	}

	public ArrayList<FromErpCFMDetail> getFromErpCFMDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpCFMDetail> list = this.dao.getFromErpCFMDetail();

		return list;
	}

	public ArrayList<FromErpGoodReceiveDetail> getFromErpGoodReceiveDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpGoodReceiveDetail> list = this.dao.getFromErpGoodReceiveDetail();

		return list;
	}

	public ArrayList<FromErpMainBillBatchDetail> getFromErpMainBillBatchDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpMainBillBatchDetail> list = this.dao.getFromErpMainBillBatchDetail();
		return list;
	}

	public ArrayList<FromErpMainProdDetail> getFromErpMainProdDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpMainProdDetail> list = this.dao.getFromErpMainProdDetail();
		return list;
	}

	public ArrayList<FromErpMainProdSaleDetail> getFromErpMainProdSaleDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpMainProdSaleDetail> list = this.dao.getFromErpMainProdSaleDetail();
		return list;
	}

	public ArrayList<FromErpMainSaleDetail> getFromErpMainSaleDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpMainSaleDetail> list = this.dao.getFromErpMainSaleDetail();
		return list;
	}

	public ArrayList<FromErpPackingDetail> getFromErpPackingDetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpPackingDetail> list = this.dao.getFromErpPackingDetail();
		return list;
	}

	public ArrayList<FromErpPODetail> getFromErpPODetail()
	{
		// TODO Auto-generated method stub
		ArrayList<FromErpPODetail> list = this.dao.getFromErpPODetail();
		return list;
	}

	public ArrayList<FromErpSaleDetail> getFromErpSaleDetail()

	{
		// TODO Auto-generated method stub
		ArrayList<FromErpSaleDetail> list = this.dao.getFromErpSaleDetail();
		return list;
	}

//	public ArrayList<FromErpSaleInputDetail> getFromErpSaleInputDetail() 
//	{
//		// TODO Auto-generated method stub
//		ArrayList<FromErpSaleInputDetail> list = this.dao.getFromErpSaleInputDetail();
//		return list;
//	}

	public ArrayList<FromErpSubmitDateDetail> getFromErpSubmitDateDetail()

	{
		// TODO Auto-generated method stub
		ArrayList<FromErpSubmitDateDetail> list = this.dao.getFromErpSubmitDateDetail();
		return list;
	}
//	public ArrayList<FromErpReceipeDetail> getFromErpReceipeDetai()
// 
//	{
//		// TODO Auto-generated method stub
//		ArrayList<FromErpReceipeDetail> list = this.dao.getFromErpReceipeDetail();
//		return list;
//	} 
}
