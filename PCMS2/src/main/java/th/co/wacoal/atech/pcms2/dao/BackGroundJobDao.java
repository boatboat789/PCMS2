package th.co.wacoal.atech.pcms2.dao;

public interface BackGroundJobDao {
	void execUpsertToMainProd();

	void execUpsertToTEMPProdWorkDate();

	void execUpsertToTEMPUserStatusOnWeb();

	void execUpsertToTEMPUserStatusOnWebWithProdOrder(String prodOrder);

	void handlerERPAtechToWebApp();

	void handlerERPAtechToWebAppProductionOrder();

	void handlerERPAtechToWebAppSaleOrder();

	void sortBackGroundAfterGetERPDataProcedure();

	void handlerERPAtechToWebAppCustomer();
}
