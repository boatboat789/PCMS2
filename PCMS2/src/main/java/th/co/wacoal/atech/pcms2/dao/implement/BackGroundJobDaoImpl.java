package th.co.wacoal.atech.pcms2.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.BackGroundJobDao;
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
import th.co.wacoal.atech.pcms2.model.master.CustomerModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapCFMModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapGoodReceiveModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainBillBatchModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainProdModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainProdSaleModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainSaleModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapPOModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapPackingModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapReceipeModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapSaleInputModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapSaleModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapSubmitDateModel;
import th.co.wacoal.atech.pcms2.model.master.erp.atech.ERPAtechModel;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class BackGroundJobDaoImpl implements BackGroundJobDao {
	private Database database;
	private String message;

	@Autowired
	public BackGroundJobDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public void execUpsertToMainProd()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToMainProd] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertToTEMPUserStatusOnWebWithProdOrder(String prodOrder)
	{
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [spd_UpsertToTEMP_UserStatusOnWebWithProdOrder] ? ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.setString(1, prodOrder);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertToTEMPProdWorkDate()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToTEMP_ProdWorkDate] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertToTEMPUserStatusOnWeb()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToTEMP_UserStatusOnWeb] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void execHandlerCustomerDetail()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_HandlerCustomerDetail] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	@Override
	public void handlerERPAtechToWebApp()
	{

		ERPAtechModel erpaModel = new ERPAtechModel();

//		FromSapMainProdModel fsmpModel = new FromSapMainProdModel();
		CustomerModel cusModel = new CustomerModel();
		FromSapCFMModel fscfmModel = new FromSapCFMModel();
		FromSapGoodReceiveModel fsgrModel = new FromSapGoodReceiveModel();
		FromSapMainBillBatchModel fsmbbModel = new FromSapMainBillBatchModel();
		FromSapMainProdSaleModel fsmpsModel = new FromSapMainProdSaleModel();
		FromSapMainSaleModel fsmsModel = new FromSapMainSaleModel();
		FromSapPackingModel fspModel = new FromSapPackingModel();
		FromSapPOModel fspoModel = new FromSapPOModel();
		FromSapSaleModel fssModel = new FromSapSaleModel();
		FromSapSaleInputModel fssiModel = new FromSapSaleInputModel();
		FromSapSubmitDateModel fssdModel = new FromSapSubmitDateModel();
		FromSapReceipeModel fsrModel = new FromSapReceipeModel();
		System.out.println("Select: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));  

		ArrayList<CustomerDetail> cusList = erpaModel.getCustomerDetail();
		cusModel.upsertCustomerDetail(cusList);
		this.execHandlerCustomerDetail();
		System.out.println("execHandlerCustomerDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));
		
		// error ทางเค้ารอแก้ Invalid column name 'STATUS'. and volumn field don't have
//		ArrayList<FromErpMainProdDetail> frmpList = erpaModel.getFromErpMainProdDetail();
//		fsmpModel.upsertFromSapMainProdDetail(frmpList);

		// error CFMSendDate เป็น String
//		ArrayList<FromErpCFMDetail> frcfmList = erpaModel.getFromErpCFMDetail();
//		fscfmModel.upsertFromSapCFMDetail(frcfmList);
		
		ArrayList<FromErpGoodReceiveDetail> frgrList = erpaModel.getFromErpGoodReceiveDetail();
		fsgrModel.upsertFromSapGoodReceiveDetail(frgrList);
		System.out.println("upsertFromSapGoodReceiveDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));
		
		ArrayList<FromErpMainBillBatchDetail> frmbbList = erpaModel.getFromErpMainBillBatchDetail();
		fsmbbModel.upsertFromSapMainBillBatchDetail(frmbbList);
		System.out.println("upsertFromSapMainBillBatchDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));

		// error ทางเค้ารอแก้ Invalid column name 'STATUS'.
//		ArrayList<FromErpMainProdSaleDetail> frmpsList = erpaModel.getFromErpMainProdSaleDetail();
//		fsmpsModel.upsertFromSapMainProdSaleDetail(frmpsList);

		ArrayList<FromErpMainSaleDetail> frmsList = erpaModel.getFromErpMainSaleDetail();
		System.out.println("getFromErpMainSaleDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));
		fsmsModel.upsertFromSapMainSaleDetail(frmsList);
		System.out.println("upsertFromSapMainSaleDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));

		ArrayList<FromErpPackingDetail> frpList = erpaModel.getFromErpPackingDetail();
		fspModel.upsertFromSapPackingDetail(frpList);
		System.out.println("upsertFromSapPackingDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));
		
		ArrayList<FromErpPODetail> frpoList = erpaModel.getFromErpPODetail();
		fspoModel.upsertFromSapPODetail(frpoList);
		System.out.println("upsertFromSapPODetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));
		
		// ERROR - NO PROD ORDER
//		ArrayList<FromErpSaleDetail> frsList = erpaModel.getFromErpSaleDetail();
//		fssModel.upsertFromSapSaleDetail(frsList);
//		System.out.println("upsertFromSapSaleDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));
		
		//  พี่สไบแจ้งไม่ใช้งานแล้ว 14/02/2025
//		ArrayList<FromErpSaleInputDetail> frsiList = erpaModel.getFromErpSaleInputDetail();
//		fssiModel.upsertFromSapSaleInputDetail(frsiList);
//		System.out.println("upsertFromSapSaleInputDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));
		
		ArrayList<FromErpSubmitDateDetail> fesdList = erpaModel.getFromErpSubmitDateDetail();
		fssdModel.upsertFromSapSubmitDateDetail(fesdList);

		System.out.println("upsertFromSapSubmitDateDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		ArrayList<FromErpReceipeDetail> ferdList = erpaModel.getFromErpReceipeDetai();
		fsrModel.upsertFromSapReceipeDetail(ferdList);

		System.out.println("upsertFromSapReceipeDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		this.execUpsertToTEMPProdWorkDate();
		System.out.println("execUpsertToTEMPProdWorkDate: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		this.execUpsertToTEMPUserStatusOnWeb();
		System.out.println("After upsert: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));  

	}
}
