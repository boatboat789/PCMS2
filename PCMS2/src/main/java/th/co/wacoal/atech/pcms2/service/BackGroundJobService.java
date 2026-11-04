package th.co.wacoal.atech.pcms2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.BackGroundJobDao;

@Component
public class BackGroundJobService {
	private BackGroundJobDao dao;

	@Autowired
	public BackGroundJobService(BackGroundJobDao dao) {
		this.dao = dao;
	}

	public void execUpsertToMainProd()
	{
		// TODO Auto-generated method stub
		this.dao.execUpsertToMainProd();
	}

	public void execUpsertToTEMPProdWorkDate()
	{
		// TODO Auto-generated method stub
		this.dao.execUpsertToTEMPProdWorkDate();
	}

	public void execUpsertToTEMPUserStatusOnWeb()
	{
		// TODO Auto-generated method stub
		this.dao.execUpsertToTEMPUserStatusOnWeb();
	}

	public void execUpsertToTEMPUserStatusOnWebWithProdOrder(String productionOrder)
	{
		// TODO Auto-generated method stub
		this.dao.execUpsertToTEMPUserStatusOnWebWithProdOrder(productionOrder);
	}

	public void handlerERPAtechToWebAppProductionOrder()
	{
		this.dao.handlerERPAtechToWebAppProductionOrder();
		// TODO Auto-generated method stub
	}

	public void runFullErpSyncWithDateRange(String fromDate, String toDate)
	{
		this.dao.runFullErpSyncWithDateRange(fromDate, toDate);
	}

	public void handlerERPAtechToWebAppSaleOrder()
	{
		this.dao.handlerERPAtechToWebAppSaleOrder();
		// TODO Auto-generated method stub
	}

	public void sortBackGroundAfterGetERPDataProcedure()
	{
		// TODO Auto-generated method stub
		this.dao.sortBackGroundAfterGetERPDataProcedure();
	}

	public void handlerERPAtechToWebAppCustomer()
	{
		// TODO Auto-generated method stub
		this.dao.handlerERPAtechToWebAppCustomer();

	}

	public void handlerBackGroundZ_ATT_CustomerConfirm2()
	{
		// TODO Auto-generated method stub
		this.dao.handlerBackGroundZ_ATT_CustomerConfirm2();

	}

	public void execSumBillAndGoodReceive()
	{
		this.dao.execSumBillAndGoodReceive();
	}

}
