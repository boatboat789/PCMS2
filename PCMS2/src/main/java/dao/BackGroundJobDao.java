package dao;

public interface BackGroundJobDao { 
	void execUpsertToMainProd();

	void execUpsertToTEMPProdWorkDate();

	void execUpsertToTEMPUserStatusOnWeb();
}
