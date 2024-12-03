	package service;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
public class BackGroundJob {
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY;
	private FtpTaskRunner ftr;
//	private boolean isTry = true ; 
	private boolean isTry = false ; 
//	private int i = 0;
	@Autowired
	private ServletContext context;
	public BackGroundJob() { /* TODO document why this constructor is empty */ }
//    @Bean(name = "PCMS2-BGJOB")
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(25);
//        executor.setThreadNamePrefix("PCMS2-BGJOB");
//        executor.initialize();
//        return executor;
//    } 
//	@Scheduled(cron = "30 2/10 * * * *")
//    public void sortBackGroundFTPSapOne() { 
//		LOCAL_DIRECTORY = context.getRealPath("/") + context.getInitParameter("DIR_UPLOAD");
//		FTP_DIRECTORY = context.getInitParameter("FTP_PATH");
//		// Creating a File object
//		File file = new File(LOCAL_DIRECTORY);
//		// Creating the directory
//		@SuppressWarnings("unused")
//		boolean bool = file.mkdir();
//		try {
//			ftr = new FtpTaskRunner(new Database(SqlInfo.getInstance()), new FtpReceive(FtpSapInfo.getInstance(), FTP_DIRECTORY, LOCAL_DIRECTORY));
//			ftr.loadFTPSapDetailOne();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		if(isTry) {System.out.println("done");}
//	} 
//	@Scheduled(cron = "30 2/10 * * * *")
//    public void sortBackGroundFTPSapTwo() {	 
//			LOCAL_DIRECTORY = context.getRealPath("/") + context.getInitParameter("DIR_UPLOAD");
//			FTP_DIRECTORY = context.getInitParameter("FTP_PATH");
//			// Creating a File object
//			File file = new File(LOCAL_DIRECTORY);
//			// Creating the directory
//			@SuppressWarnings("unused")
//			boolean bool = file.mkdir();
//			try {
//				ftr = new FtpTaskRunner(new Database(SqlInfo.getInstance()), new FtpReceive(FtpSapInfo.getInstance(), FTP_DIRECTORY, LOCAL_DIRECTORY));
//				ftr.loadFTPSapDetailTwo();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} 
//			if(isTry) {System.out.println("done");}
//	} 
//	@Scheduled(cron = "30 2/10 * * * *")
//    public void sortBackGroundFTPSapThree() { 
//		LOCAL_DIRECTORY = context.getRealPath("/") + context.getInitParameter("DIR_UPLOAD");
//		FTP_DIRECTORY = context.getInitParameter("FTP_PATH");
//		// Creating a File object
//		File file = new File(LOCAL_DIRECTORY);
//		// Creating the directory
//		@SuppressWarnings("unused")
//		boolean bool = file.mkdir();
//		try {
//			ftr = new FtpTaskRunner(new Database(SqlInfo.getInstance()), new FtpReceive(FtpSapInfo.getInstance(), FTP_DIRECTORY, LOCAL_DIRECTORY));
//			ftr.loadFTPSapDetailThree();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		if(isTry) {System.out.println("done");}
//	}
// 
//	@Scheduled(cron = "30 2/10 * * * *")
//    public void sortBackGroundFTPSapFour() { 
//		LOCAL_DIRECTORY = context.getRealPath("/") + context.getInitParameter("DIR_UPLOAD");
//		FTP_DIRECTORY = context.getInitParameter("FTP_PATH");
//		// Creating a File object
//		File file = new File(LOCAL_DIRECTORY);
//		// Creating the directory
//		@SuppressWarnings("unused")
//		boolean bool = file.mkdir();
//		try {
//			ftr = new FtpTaskRunner(new Database(SqlInfo.getInstance()), new FtpReceive(FtpSapInfo.getInstance(), FTP_DIRECTORY, LOCAL_DIRECTORY));
//			ftr.loadFTPSapDetailFour();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		if(isTry) {System.out.println("done");}
//	} 
//	@Scheduled(cron = "30 2/10 * * * *")
//    public void sortBackGroundProd() { 
//		LOCAL_DIRECTORY = context.getRealPath("/") + context.getInitParameter("DIR_UPLOAD");
//		FTP_DIRECTORY = context.getInitParameter("FTP_PATH");
//		// Creating a File object
//		File file = new File(LOCAL_DIRECTORY);
//		// Creating the directory
//		@SuppressWarnings("unused")
//		boolean bool = file.mkdir();
//		try {
//			ftr = new FtpTaskRunner(new Database(SqlInfo.getInstance()), new FtpReceive(FtpSapInfo.getInstance(), FTP_DIRECTORY, LOCAL_DIRECTORY));
//			ftr.loadFTPCaseRequiredProd();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		if(isTry) {System.out.println("done");}
//	} 
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
//    public void sortBackGroundProcess() { 
//	   boolean bl_check = false; 
//	   BackGroundJobModel bgjModel = new BackGroundJobModel();
//	   bgjModel.execUpsertToTEMPProdWorkDate();
//	   if(bl_check) { System.out.println("execUpsertToTEMPProdWorkDate    :: " + Calendar.getInstance().getTime().toString()); }
//	   bgjModel.execUpsertToTEMPUserStatusOnWeb();
//	   if(bl_check) { System.out.println("execUpsertToTEMPUserStatusOnWeb    :: " + Calendar.getInstance().getTime().toString()); }
// 
//	}
////	@Scheduled(fixedDelay = 1000000000)
//	@Async
//	@Scheduled(cron = "0 0 1 * * *")
//	public void sortBackGroundTwo() {
//		SORModel model = new SORModel();
//		model.upSertSORToPCMS();
//	}
}