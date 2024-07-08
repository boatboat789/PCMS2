	package service;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import info.FtpSapInfo;
import info.SqlInfo;
import model.SORModel;
import th.in.totemplate.core.net.FtpReceive;
import th.in.totemplate.core.sql.Database;

@EnableAsync
public class BackGroundJob {
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY;
	private FtpTaskRunner ftr;
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
	@Async
//	@Scheduled(fixedDelay = 1000000000)
	@Scheduled(cron = "30 2/10 * * * *")
    public void sortBackGround1() {
//		System.out.println("start");
//		if(i== 0) {
			LOCAL_DIRECTORY = context.getRealPath("/") + context.getInitParameter("DIR_UPLOAD");
			FTP_DIRECTORY = context.getInitParameter("FTP_PATH");
			// Creating a File object
			File file = new File(LOCAL_DIRECTORY);
			// Creating the directory
			@SuppressWarnings("unused")
			boolean bool = file.mkdir();
			try {
				ftr = new FtpTaskRunner(new Database(SqlInfo.getInstance()), new FtpReceive(FtpSapInfo.getInstance(), FTP_DIRECTORY, LOCAL_DIRECTORY));
				ftr.loadFTP();
			} catch (Exception e) {
				e.printStackTrace();
			}
//			i+=1;
//		} 
	}
	@Async
	@Scheduled(cron = "0 0 1 * * *")
	public void sortBackGroundTwo() {
		SORModel model = new SORModel();
		model.upSertSORToPCMS();
	}
}