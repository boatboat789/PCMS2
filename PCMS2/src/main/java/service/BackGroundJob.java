	package service;

import java.util.Calendar;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;

import model.BackGroundJobModel;
import model.SORModel;

@EnableAsync
public class BackGroundJob {
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY;
	private FtpTaskRunner ftr; 
	private boolean isTry = false ;  
	@Autowired
	private ServletContext context;
	public BackGroundJob() { /* TODO document why this constructor is empty */ }      
	
//	@Scheduled(cron = "30 2/10 * * * *")
//    public void sortBackGroundSO() { 
//		LOCAL_DIRECTORY = context.getRealPath("/") + context.getInitParameter("DIR_UPLOAD");
//		FTP_DIRECTORY = context.getInitParameter("FTP_PATH");
//		// Creating a File object
//		File file = new File(LOCAL_DIRECTORY);
//		// Creating the directory
//		@SuppressWarnings("unused")
//		boolean bool = file.mkdir();
//		try {
//			ftr = new FtpTaskRunner(new Database(SqlInfo.getInstance()), new FtpReceive(FtpSapInfo.getInstance(), FTP_DIRECTORY, LOCAL_DIRECTORY));
//			ftr.loadFTPCaseSO();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		if(isTry) {System.out.println("done");}
//	} 
//	@Scheduled(cron = "30 8/10 * * * *")
    public void sortBackGroundProcess() { 
	   boolean bl_check = false; 
	   BackGroundJobModel bgjModel = new BackGroundJobModel();
	   bgjModel.execUpsertToTEMPProdWorkDate();
	   if(bl_check) { System.out.println("execUpsertToTEMPProdWorkDate    :: " + Calendar.getInstance().getTime().toString()); }
	   bgjModel.execUpsertToTEMPUserStatusOnWeb();
	   if(bl_check) { System.out.println("execUpsertToTEMPUserStatusOnWeb    :: " + Calendar.getInstance().getTime().toString()); }
	} 
//	@Async
//	@Scheduled(cron = "0 0 1 * * *")
	public void sortBackGroundTwo() {
		SORModel model = new SORModel();
		model.upSertSORToPCMS();
	}
}