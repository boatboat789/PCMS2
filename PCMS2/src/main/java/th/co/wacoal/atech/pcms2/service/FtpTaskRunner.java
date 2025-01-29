package th.co.wacoal.atech.pcms2.service;

import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.in.totemplate.core.net.FtpReceive;
import th.in.totemplate.core.sql.Database;

public class FtpTaskRunner { 
   @SuppressWarnings("unused")
private UserDetail user;
   public FtpTaskRunner(Database database, FtpReceive ftp) {
      this.user = new UserDetail("SYSTEM", ""); 
   }
   public void loadFTPSapDetailOne() {
//		BackGroundJobModel bgjModel = new BackGroundJobModel();

//	   boolean bl_check = false;
//	   bl_check = true;
//	   System.out.println("PCMS2 loadFTPSapDetailOne :: FTP run at :: " + Calendar.getInstance().getTime().toString());
//	   // NEED WORK BEFORE importerMainProd
//	   this.importerMainProdSale.loadDataFTP(this.user);
//	   if(bl_check) { System.out.println("importerMainProdSale :: " + Calendar.getInstance().getTime().toString()); } 
 
   } 
}
