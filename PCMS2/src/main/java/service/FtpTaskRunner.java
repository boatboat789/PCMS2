package service;

import java.util.Calendar;

import dao.DataImportDao;
import dao.implement.DataImportCFMDaoImpl;
import dao.implement.DataImportDyeingDaoImpl;
import dao.implement.DataImportFinishingDaoImpl;
import dao.implement.DataImportGoodReceiveDaoImpl;
import dao.implement.DataImportInspectDaoImpl;
import dao.implement.DataImportMainBillBatchDaoImpl;
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
   private DataImportDao importerGoodReceive;
   
   private DataImportDao importerBillBatch;
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
      this.importerGoodReceive = new DataImportGoodReceiveDaoImpl(database, ftp); 
      this.importerBillBatch = new DataImportMainBillBatchDaoImpl(database, ftp); 
   } 
   public void loadFTP() {
	   boolean bl_check = false;
//	   bl_check = true;
	   System.out.println("PCMS2 :: FTP run at :: " + Calendar.getInstance().getTime().toString());  
	   this.importerNC.loadDataFTP(this.user);	
	   if(bl_check == true) { System.out.println("importerNC :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerReceipe.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerReceipe :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerSubmitDate.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerSubmitDate :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerSaleInput.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerSaleInput :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerSale.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerSale :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerCFM.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerCFM :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerWaitTest.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerWaitTest :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerWorkInLab.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerWorkInLab :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerSendTestQC.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerSendTestQC :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerPacking.loadDataFTP(this.user); 
	   if(bl_check == true) { System.out.println("importerPacking :: " + Calendar.getInstance().getTime().toString()); } 	
	   this.importerInspect.loadDataFTP(this.user);  
	   if(bl_check == true) { System.out.println("importerInspect :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerFinishing.loadDataFTP(this.user);	
	   if(bl_check == true) { System.out.println("importerFinishing :: " + Calendar.getInstance().getTime().toString()); } 	
	   this.importerDyeing.loadDataFTP(this.user)	;	
	   if(bl_check == true) { System.out.println("importerDyeing :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerPreset.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerPreset :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerPO.loadDataFTP(this.user);      
	   if(bl_check == true) { System.out.println("importerPO :: " + Calendar.getInstance().getTime().toString()); } 
//---------------------MUST UNDER SAMEGROUP------------------------
	   this.importerMainSale.loadDataFTP(this.user);  
	   if(bl_check == true) { System.out.println("importerMainSale :: " + Calendar.getInstance().getTime().toString()); }  
	   this.importerGoodReceive.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerGoodReceive :: " + Calendar.getInstance().getTime().toString()); } 
//	   this.importerMainGrade.loadDataFTP(this.user);
	   this.importerBillBatch.loadDataFTP(this.user);  
	   if(bl_check == true) { System.out.println("importerBillBatch :: " + Calendar.getInstance().getTime().toString()); } 
	   this.importerMainProdSale.loadDataFTP(this.user);
	   if(bl_check == true) { System.out.println("importerMainProd :: " + Calendar.getInstance().getTime().toString()); } 
//-----------------------------------------------------------------
//---------------------MUST LAST-----------------------------------
	   this.importerMainProd.loadDataFTP(this.user);	//spd_UpsertToTEMP_ProdWorkDate  //spd_UpsertToTEMP_UserStatusOnWeb
	   if(bl_check == true) { System.out.println("importerMainProd :: " + Calendar.getInstance().getTime().toString()); } 
//-----------------------------------------------------------------
	   System.out.println("PCMS STOP FTP run at :: " + Calendar.getInstance().getTime().toString()); 
   } 
}
