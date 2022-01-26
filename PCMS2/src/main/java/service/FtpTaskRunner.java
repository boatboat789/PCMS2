package service;

import java.util.Calendar;

import dao.DataImportDao;
import dao.implement.DataImportCFMDaoImpl;
import dao.implement.DataImportDyeingDaoImpl;
import dao.implement.DataImportFinishingDaoImpl;
import dao.implement.DataImportInspectDaoImpl;
import dao.implement.DataImportMainGradeDaoImpl;
import dao.implement.DataImportMainProdDaoImpl;
import dao.implement.DataImportMainProdSaleDaoImpl;
import dao.implement.DataImportMainSaleDaoImpl;
import dao.implement.DataImportNCDaoImpl;
import dao.implement.DataImportPODaoImpl;
import dao.implement.DataImportPackingDaoImpl;
import dao.implement.DataImportPresetDaoImpl;
import dao.implement.DataImportReceipeDaoImpl;
import dao.implement.DataImportSaleDaoImpl;
import dao.implement.DataImportSaleInputDaoImpl;
import dao.implement.DataImportSendTestQCDaoImpl;
import dao.implement.DataImportSubmitDateDaoImpl;
import dao.implement.DataImportWaitTestDaoImpl;
import dao.implement.DataImportWorkInLabDaoImpl;
import entities.UserDetail;
//
//import dao.DataImportTwoDao;
//import dao.implement.DataImportTwoDaoImpl;
//import entities.PPMMUserDetail;
import th.in.totemplate.core.net.FtpReceive;
import th.in.totemplate.core.sql.Database;

public class FtpTaskRunner {
//{
   private FtpReceive ftp;
   private UserDetail user;  
   private DataImportDao importerNC;
   private DataImportDao importerReceipe;
   private DataImportDao importerSubmitDate;
   private DataImportDao importerSaleInput;
   private DataImportDao importerSale;
   private DataImportDao importerCFM;
   private DataImportDao importerWaitTest;
   private DataImportDao importerWorkInLab;
   private DataImportDao importerSendTestQC;
   private DataImportDao importerPacking;
   
   private DataImportDao importerInspect;
   private DataImportDao importerFinishing;
   private DataImportDao importerDyeing;
   private DataImportDao importerPreset;
   private DataImportDao importerPO;
   
   private DataImportDao importerMainProdSale;
   private DataImportDao importerMainProd;
   private DataImportDao importerMainSale;
   private DataImportDao importerMainGrade;
   public FtpTaskRunner(Database database, FtpReceive ftp) {
      this.ftp = ftp;
      this.user = new UserDetail("SYSTEM", ""); 
      this.importerNC = new DataImportNCDaoImpl(database, ftp); 
      this.importerReceipe = new DataImportReceipeDaoImpl(database, ftp); 
      this.importerSubmitDate = new DataImportSubmitDateDaoImpl(database, ftp); 
      this.importerSaleInput = new DataImportSaleInputDaoImpl(database, ftp); 
      this.importerSale = new DataImportSaleDaoImpl(database, ftp); 
      this.importerCFM = new DataImportCFMDaoImpl(database, ftp); 
      this.importerWaitTest = new DataImportWaitTestDaoImpl(database, ftp); 
      this.importerWorkInLab = new DataImportWorkInLabDaoImpl(database, ftp); 
      this.importerSendTestQC = new DataImportSendTestQCDaoImpl(database, ftp); 
      this.importerPacking = new DataImportPackingDaoImpl(database, ftp); 
      
      this.importerInspect = new DataImportInspectDaoImpl(database, ftp); 
      this.importerFinishing = new DataImportFinishingDaoImpl(database, ftp); 
      this.importerDyeing = new DataImportDyeingDaoImpl(database, ftp); 
      this.importerPreset = new DataImportPresetDaoImpl(database, ftp); 
      this.importerPO = new DataImportPODaoImpl(database, ftp); 
      
      this.importerMainProdSale = new DataImportMainProdSaleDaoImpl(database, ftp); 
      this.importerMainProd = new DataImportMainProdDaoImpl(database, ftp); 
      this.importerMainSale = new DataImportMainSaleDaoImpl(database, ftp); 
      this.importerMainGrade = new DataImportMainGradeDaoImpl(database, ftp); 
   } 
   public void loadFTP() {
	   System.out.println("PCMS START FTP run at :: " + Calendar.getInstance().getTime().toString()); 
//	   this.importerNC.loadDataFTP(this.user);
//	   this.importerReceipe.loadDataFTP(this.user);
//	   this.importerSubmitDate.loadDataFTP(this.user);
//	   this.importerSaleInput.loadDataFTP(this.user);
//	   this.importerSale.loadDataFTP(this.user);
//	   this.importerCFM.loadDataFTP(this.user);
//	   this.importerWaitTest.loadDataFTP(this.user);
//	   this.importerWorkInLab.loadDataFTP(this.user);
//	   this.importerSendTestQC.loadDataFTP(this.user);
//	   this.importerPacking.loadDataFTP(this.user); 
//	   this.importerInspect.loadDataFTP(this.user);  
//	   this.importerFinishing.loadDataFTP(this.user);
//	   this.importerDyeing.loadDataFTP(this.user);
//	   this.importerPreset.loadDataFTP(this.user);
//	   this.importerPO.loadDataFTP(this.user);  
//	   this.importerMainProdSale.loadDataFTP(this.user);
	   this.importerMainProd.loadDataFTP(this.user);
	   this.importerMainSale.loadDataFTP(this.user); 
	   this.importerMainGrade.loadDataFTP(this.user);
	   
	   System.out.println("PCMS STOP FTP run at :: " + Calendar.getInstance().getTime().toString()); 
   } 
}
