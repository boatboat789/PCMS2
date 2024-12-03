package dao;

public interface BackGroundJobDao {
	void execUpsertToMainProd();

	void execUpsertToTEMPProdWorkDate();

	void execUpsertToTEMPUserStatusOnWeb();

	void execUpsertToTEMPUserStatusOnWebWithProdOrder(String prodOrder);

	void handlerERPAtechToWebApp();
}
