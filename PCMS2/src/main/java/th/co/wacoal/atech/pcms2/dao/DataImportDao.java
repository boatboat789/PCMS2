package th.co.wacoal.atech.pcms2.dao;

import th.co.wacoal.atech.pcms2.entities.UserDetail;

public interface DataImportDao {
   int loadDataFile(UserDetail var1, String var2);

   int loadDataFTP(UserDetail var1, String var2);

   int loadDataFTP(UserDetail var1);

   int loadDataSAP(UserDetail var1);
}
