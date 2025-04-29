package th.co.wacoal.atech.pcms2.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpPackingDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSaleDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.FromErpSubmitDateDetail;
import th.co.wacoal.atech.pcms2.model.master.CustomerModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapCFMModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapGoodReceiveModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainBillBatchModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainProdModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainProdSaleModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainSaleModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapPackingModel;
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

	public void execUpsertToCFM()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToCFM] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void execUpsertToMainProdSale()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToMainProdSale] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void execUpsertToPacking()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToPacking]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void execUpsertToSale()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToSale]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void execUpsertToSubmitDate()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToSubmitDate]";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void execUpsertToGoodReceive()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToGoodReceive]";
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
			prepared.execute();

			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

 
	public void execUpsertToMainSale()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToMainSale] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
			prepared.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void execUpsertToMainBillBatch()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToMainBillBatch] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();

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

	}

	@Override
	public void handlerERPAtechToWebAppProductionOrder()
	{
		ERPAtechModel erpaModel = new ERPAtechModel();

		FromSapMainProdModel fsmpModel = new FromSapMainProdModel(); 
		FromSapCFMModel fscfmModel = new FromSapCFMModel();
		FromSapMainProdSaleModel fsmpsModel = new FromSapMainProdSaleModel();
		FromSapPackingModel fspModel = new FromSapPackingModel(); 
//		RollFromSapModel rfsModel = new RollFromSapModel();
		FromSapSubmitDateModel fssdModel = new FromSapSubmitDateModel();
		FromSapGoodReceiveModel fsgrModel = new FromSapGoodReceiveModel();
//		FromSapReceipeModel fsrModel = new FromSapReceipeModel();

		ArrayList<FromErpMainProdDetail> frmpList = erpaModel.getFromErpMainProdDetail();
		ArrayList<FromErpCFMDetail> frcfmList = erpaModel.getFromErpCFMDetail();
		ArrayList<FromErpMainProdSaleDetail> frmpsList = erpaModel.getFromErpMainProdSaleDetail();
		ArrayList<FromErpPackingDetail> frpList = erpaModel.getFromErpPackingDetail();
//		ArrayList<FromErpPODetail> rollList = erpaModel.getFromErpPODetail();
		ArrayList<FromErpSubmitDateDetail> fesdList = erpaModel.getFromErpSubmitDateDetail();
		ArrayList<FromErpGoodReceiveDetail> frgrList = erpaModel.getFromErpGoodReceiveDetail();
//		ArrayList<FromErpReceipeDetail> ferdList = erpaModel.getFromErpReceipeDetai();

		fsmpModel.upsertFromSapMainProdDetail(frmpList);
		this.execUpsertToMainProd();
		fscfmModel.upsertFromSapCFMDetail(frcfmList);
		this.execUpsertToCFM(); 
		fsmpsModel.upsertFromSapMainProdSaleDetail(frmpsList);
		this.execUpsertToMainProdSale();
		fspModel.upsertFromSapPackingDetail(frpList);
		this.execUpsertToPacking();
//		rfsModel.upsertRollFromSapFromERPPODetail(rollList); //  HANDLER FLAG ROLL NO IN METHOD ----- 
		fssdModel.upsertFromSapSubmitDateDetail(fesdList);
		this.execUpsertToSubmitDate();
		fsgrModel.upsertFromSapGoodReceiveDetail(frgrList);
		this.execUpsertToGoodReceive();
//		fsrModel.upsertFromSapReceipeDetail(ferdList);
	}

	@Override
	public void handlerERPAtechToWebAppSaleOrder()
	{
		// TODO Auto-generated method stub

		ERPAtechModel erpaModel = new ERPAtechModel();
		FromSapMainSaleModel fsmsModel = new FromSapMainSaleModel();
		FromSapMainBillBatchModel fsmbbModel = new FromSapMainBillBatchModel();
		FromSapSaleModel fssModel = new FromSapSaleModel(); 
		
		ArrayList<FromErpMainSaleDetail> frmsList = erpaModel.getFromErpMainSaleDetail();
		ArrayList<FromErpMainBillBatchDetail> frmbbList = erpaModel.getFromErpMainBillBatchDetail(); 
		ArrayList<FromErpSaleDetail> frsList = erpaModel.getFromErpSaleDetail();
		
		fsmsModel.upsertFromSapMainSaleDetail(frmsList);
		this.execUpsertToMainSale();
		fsmbbModel.upsertFromSapMainBillBatchDetail(frmbbList);
		this.execUpsertToMainBillBatch();
		fssModel.upsertFromSapSaleDetail(frsList);
		this.execUpsertToSale();
	}

	@Override
	public void sortBackGroundAfterGetERPDataProcedure()
	{

//		System.out.println("upsertFromSapReceipeDetail: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		this.execUpsertToTEMPProdWorkDate();
//		System.out.println("execUpsertToTEMPProdWorkDate: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		this.execUpsertToTEMPUserStatusOnWeb();
//		System.out.println("After upsert: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));  

	}

	@Override
	public void handlerERPAtechToWebAppCustomer()
	{
		ERPAtechModel erpaModel = new ERPAtechModel();   
		CustomerModel cusModel = new CustomerModel(); 
		ArrayList<CustomerDetail> cusList = erpaModel.getCustomerDetail(); 
		cusModel.upsertCustomerDetail(cusList);
//		this.execHandlerCustomerDetail(); 
		
	}
}
