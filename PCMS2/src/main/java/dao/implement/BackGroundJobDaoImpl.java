	package dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.BackGroundJobDao;
import entities.erp.atech.CustomerDetail;
import entities.erp.atech.FromErpCFMDetail;
import entities.erp.atech.FromErpGoodReceiveDetail;
import entities.erp.atech.FromErpMainBillBatchDetail;
import entities.erp.atech.FromErpMainProdDetail;
import entities.erp.atech.FromErpMainProdSaleDetail;
import entities.erp.atech.FromErpMainSaleDetail;
import entities.erp.atech.FromErpPODetail;
import entities.erp.atech.FromErpPackingDetail;
import entities.erp.atech.FromErpSaleDetail;
import entities.erp.atech.FromErpSaleInputDetail;
import entities.erp.atech.FromErpSubmitDateDetail;
import model.master.CustomerModel;
import model.master.FromSapCFMModel;
import model.master.FromSapGoodReceiveModel;
import model.master.FromSapMainBillBatchModel;
import model.master.FromSapMainProdModel;
import model.master.FromSapMainProdSaleModel;
import model.master.FromSapMainSaleModel;
import model.master.FromSapPOModel;
import model.master.FromSapPackingModel;
import model.master.FromSapSaleInputModel;
import model.master.FromSapSaleModel;
import model.master.FromSapSubmitDateModel;
import model.master.erp.atech.ERPAtechModel;
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

	public String getMessage() {
		return this.message;
	}
	@Override
	public void execUpsertToMainProd() {
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
	public void execUpsertToTEMPUserStatusOnWebWithProdOrder(String prodOrder) {
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
	public void execUpsertToTEMPProdWorkDate() {
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
	public void execUpsertToTEMPUserStatusOnWeb() {
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

	@Override
	public void handlerERPAtechToWebApp()
	{
		
		ERPAtechModel erpaModel = new ERPAtechModel();
		
		FromSapMainProdModel fsmpModel = new FromSapMainProdModel();
		CustomerModel cusModel =  new CustomerModel();
		FromSapCFMModel fscfmModel = new FromSapCFMModel();
		FromSapGoodReceiveModel fsgrModel = new FromSapGoodReceiveModel();
		ArrayList<CustomerDetail> cusList =   erpaModel.getCustomerDetail();
		FromSapMainBillBatchModel fsmbbModel = new  FromSapMainBillBatchModel();
		FromSapMainProdSaleModel fsmpsModel = new  FromSapMainProdSaleModel();
		FromSapMainSaleModel fsmsModel = new  FromSapMainSaleModel();
		FromSapPackingModel fspModel = new  FromSapPackingModel();
		FromSapPOModel fspoModel = new  FromSapPOModel();
		FromSapSaleModel fssModel = new  FromSapSaleModel(); 
		FromSapSaleInputModel fssiModel = new  FromSapSaleInputModel();
		FromSapSubmitDateModel fssdModel = new  FromSapSubmitDateModel();
		 
		cusModel.upsertCustomerDetail(cusList);
		ArrayList<FromErpMainProdDetail> frmpList =  erpaModel.getFromErpMainProdDetail();
		fsmpModel.upsertFromSapMainProdDetail(frmpList); 
		ArrayList<FromErpCFMDetail> frcfmList = erpaModel.getFromErpCFMDetail();
		fscfmModel.upsertFromSapCFMDetail(frcfmList); 
		ArrayList<FromErpGoodReceiveDetail> frgrList =  erpaModel.getFromErpGoodReceiveDetail();
		fsgrModel.upsertFromSapGoodReceiveDetail(frgrList); 
		ArrayList<FromErpMainBillBatchDetail> frmbbList =  erpaModel.getFromErpMainBillBatchDetail();
		fsmbbModel.upsertFromSapMainBillBatchDetail(frmbbList); 
		ArrayList<FromErpMainProdSaleDetail> frmpsList =  erpaModel.getFromErpMainProdSaleDetail();
		fsmpsModel.upsertFromSapMainProdSaleDetail(frmpsList); 
		
		ArrayList<FromErpMainSaleDetail> frmsList =  erpaModel.getFromErpMainSaleDetail();
		fsmsModel.upsertFromSapMainSaleDetail(frmsList);
		
		
		ArrayList<FromErpPackingDetail> frpList =  erpaModel.getFromErpPackingDetail();
		fspModel.upsertFromSapPackingDetail(frpList);
		ArrayList<FromErpPODetail> frpoList =  erpaModel.getFromErpPODetail();  
		fspoModel.upsertFromSapPODetail(frpoList);
		ArrayList<FromErpSaleDetail> frsList =  erpaModel.getFromErpSaleDetail();
		fssModel.upsertFromSapSaleDetail(frsList) ;
		ArrayList<FromErpSaleInputDetail> frsiList =  erpaModel.getFromErpSaleInputDetail();
		fssiModel.upsertFromSapSaleInputDetail(frsiList);
		ArrayList<FromErpSubmitDateDetail> fesdList =  erpaModel.getFromErpSubmitDateDetail(); 
		fssdModel.upsertFromSapSubmitDateDetail(fesdList);
	}
}
