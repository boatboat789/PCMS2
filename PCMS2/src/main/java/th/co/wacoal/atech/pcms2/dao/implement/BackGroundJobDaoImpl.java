package th.co.wacoal.atech.pcms2.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;
import th.co.wacoal.atech.pcms2.service.master.CustomerService;
import th.co.wacoal.atech.pcms2.service.master.FromSapCFMService;
import th.co.wacoal.atech.pcms2.service.master.FromSapGoodReceiveService;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainBillBatchService;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainProdService;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainProdSaleService;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainSaleService;
import th.co.wacoal.atech.pcms2.service.master.FromSapPackingService;
import th.co.wacoal.atech.pcms2.service.master.FromSapSaleService;
import th.co.wacoal.atech.pcms2.service.master.FromSapSubmitDateService;
import th.co.wacoal.atech.pcms2.service.master.Z_ATT_CustomerConfirm2Service;
import th.co.wacoal.atech.pcms2.service.master.erp.atech.ERPAtechService;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class BackGroundJobDaoImpl implements BackGroundJobDao {
	private final Database database;
    private final ERPAtechService erpService;

    // Services เหล่านี้จะถูก inject โดย Spring (ต้องมี @Service ในคลาสนั้น ๆ)
    private final FromSapMainProdService fromSapMainProdService;
    private final FromSapCFMService fromSapCFMService;
    private final FromSapMainProdSaleService fromSapMainProdSaleService;
    private final FromSapPackingService fromSapPackingService;
    private final FromSapSubmitDateService fromSapSubmitDateService;
    private final FromSapGoodReceiveService fromSapGoodReceiveService;
    private final FromSapMainBillBatchService fromSapMainBillBatchService;
    private final FromSapMainSaleService fromSapMainSaleService;
    private final FromSapSaleService fromSapSaleService;
    private final CustomerService customerService;
    private final Z_ATT_CustomerConfirm2Service zattCustomerConfirm2Service;

    private String message;  // ถ้าใช้เป็น instance variable

    @Autowired
    public BackGroundJobDaoImpl(
            @Qualifier("pcmsDatabase") Database database,
            ERPAtechService erpService,
            
            FromSapMainProdService fromSapMainProdService,
            FromSapCFMService fromSapCFMService,
            FromSapMainProdSaleService fromSapMainProdSaleService,
            FromSapPackingService fromSapPackingService,
            FromSapSubmitDateService fromSapSubmitDateService,
            FromSapGoodReceiveService fromSapGoodReceiveService,
            FromSapMainBillBatchService fromSapMainBillBatchService,
            FromSapMainSaleService fromSapMainSaleService,
            FromSapSaleService fromSapSaleService,
            CustomerService customerService,
            Z_ATT_CustomerConfirm2Service zattCustomerConfirm2Service) {

        this.database = database;
        this.erpService = erpService;

        this.fromSapMainProdService = fromSapMainProdService;
        this.fromSapCFMService = fromSapCFMService;
        this.fromSapMainProdSaleService = fromSapMainProdSaleService;
        this.fromSapPackingService = fromSapPackingService;
        this.fromSapSubmitDateService = fromSapSubmitDateService;
        this.fromSapGoodReceiveService = fromSapGoodReceiveService;
        this.fromSapMainBillBatchService = fromSapMainBillBatchService;
        this.fromSapMainSaleService = fromSapMainSaleService;
        this.fromSapSaleService = fromSapSaleService;
        this.customerService = customerService;
        this.zattCustomerConfirm2Service = zattCustomerConfirm2Service;

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

	public void execHandlerCustomerConfirm2()
	{
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToZ_ATT_CustomerConfirm2] ";
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
		try {
			ArrayList<FromErpMainProdDetail> frmpList = erpService.getFromErpMainProdDetail();
			fromSapMainProdService.upsertFromSapMainProdDetail(frmpList);
			this.execUpsertToMainProd();
		} catch (Exception e) {
			System.err.println("Error processing Main Production Order: " + e.getMessage());
			e.printStackTrace();
		}

		// Handle FromSapCFMDetail
		try {
			ArrayList<FromErpCFMDetail> frcfmList = erpService.getFromErpCFMDetail();
			fromSapCFMService.upsertFromSapCFMDetail(frcfmList);
			this.execUpsertToCFM();
		} catch (Exception e) {
			System.err.println("Error processing CFM Detail: " + e.getMessage());
			e.printStackTrace();
		}

		// Handle FromSapMainProdSaleDetail
		try {
			ArrayList<FromErpMainProdSaleDetail> frmpsList = erpService.getFromErpMainProdSaleDetail();
			fromSapMainProdSaleService.upsertFromSapMainProdSaleDetail(frmpsList);
			this.execUpsertToMainProdSale();
		} catch (Exception e) {
			System.err.println("Error processing Main Production Sale Detail: " + e.getMessage());
			e.printStackTrace();
		}

		// Handle FromSapPackingDetail
		try {
			ArrayList<FromErpPackingDetail> frpList = erpService.getFromErpPackingDetail();
			fromSapPackingService.upsertFromSapPackingDetail(frpList);
			this.execUpsertToPacking();
		} catch (Exception e) {
			System.err.println("Error processing Packing Detail: " + e.getMessage());
			e.printStackTrace();
		}

		// Handle FromSapSubmitDateDetail
		try {
			ArrayList<FromErpSubmitDateDetail> fesdList = erpService.getFromErpSubmitDateDetail();
			fromSapSubmitDateService.upsertFromSapSubmitDateDetail(fesdList);
			this.execUpsertToSubmitDate();
		} catch (Exception e) {
			System.err.println("Error processing Submit Date Detail: " + e.getMessage());
			e.printStackTrace();
		}

		// Handle FromSapGoodReceiveDetail
		try {
			ArrayList<FromErpGoodReceiveDetail> frgrList = erpService.getFromErpGoodReceiveDetail();
			fromSapGoodReceiveService.upsertFromSapGoodReceiveDetail(frgrList);
			this.execUpsertToGoodReceive();
		} catch (Exception e) {
			System.err.println("Error processing Good Receive Detail: " + e.getMessage());
			e.printStackTrace();
		}

		// Handle FromSapMainBillBatchDetail
		try {
			ArrayList<FromErpMainBillBatchDetail> frmbbList = erpService.getFromErpMainBillBatchDetail();
			fromSapMainBillBatchService.upsertFromSapMainBillBatchDetail(frmbbList);
			this.execUpsertToMainBillBatch();
		} catch (Exception e) {
			System.err.println("Error processing Main Bill Batch Detail: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void handlerERPAtechToWebAppSaleOrder()
	{ 
		try {
			ArrayList<FromErpMainSaleDetail> frmsList = erpService.getFromErpMainSaleDetail();
			fromSapMainSaleService.upsertFromSapMainSaleDetail(frmsList);
			this.execUpsertToMainSale();
		} catch (Exception e) {
			System.err.println("Error processing Main Sale Detail: " + e.getMessage());
			e.printStackTrace();
		}
		try {
			ArrayList<FromErpSaleDetail> frsList = erpService.getFromErpSaleDetail();
			fromSapSaleService.upsertFromSapSaleDetail(frsList);
			this.execUpsertToSale();
		} catch (Exception e) {
			System.err.println("Error processing Sale Detail: " + e.getMessage());
			e.printStackTrace();
		}
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
		try { 
			ArrayList<CustomerDetail> cusList = erpService.getCustomerDetail();
			customerService.upsertCustomerDetail(cusList);
		} catch (Exception e) {
			System.err.println("Error handlerERPAtechToWebAppCustomer: " + e.getMessage());
			e.printStackTrace();
		}
//		this.execHandlerCustomerDetail();  
	}

	@Override
	public void handlerBackGroundZ_ATT_CustomerConfirm2()
	{
		try { 
			ArrayList<Z_ATT_CustomerConfirm2Detail> zCustList = erpService.getZ_ATT_CustomerConfirm2Detail();
			zattCustomerConfirm2Service.upsertZ_ATT_CustomerConfirm2Detail(zCustList);
			this.execHandlerCustomerConfirm2();
		} catch (Exception e) {
			System.err.println("Error handlerBackGroundZ_ATT_CustomerConfirm2: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
