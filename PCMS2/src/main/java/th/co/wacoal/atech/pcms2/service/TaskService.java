package th.co.wacoal.atech.pcms2.service;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import th.co.wacoal.atech.pcms2.model.BackGroundJobModel;
import th.co.wacoal.atech.pcms2.model.SORModel;

@Configuration
@EnableScheduling
public class TaskService {
	@SuppressWarnings("unused")
	private String LOCAL_DIRECTORY;
	@SuppressWarnings("unused")
	private String FTP_DIRECTORY;
	@SuppressWarnings("unused") 
	private ServletContext context;
	private SORModel sorModel;
	private BackGroundJobModel bgjModel;

	@Autowired
	public TaskService(SORModel sorModel
			, BackGroundJobModel bgjModel
			) {
		this.sorModel = sorModel;
		this.bgjModel = bgjModel;
	}

//	@Scheduled(cron = "*/5 * * * * *")
//	public void test() {  
//		System.out.println("hi");
//	}
//	@Scheduled(fixedRate = 50000000)
////	@Scheduled(cron = "30 8/10 * * * *")
//	public void sortBackGroundTwos()
//	{
//		System.out.println("hi");
//	}
//	@Scheduled(fixedRate = 50000000)	
	@Scheduled(cron = "30 8/10 * * * *")
	public void sortBackGroundTwo()
	{
		this.bgjModel.handlerERPAtechToWebApp();
	}

	@Scheduled(cron = "0 0 1 * * *") 
	public void bgJobHandlerDataFromOrgatex()
	{
		this.sorModel.upSertSORToPCMS();
	} 
////	@Scheduled(cron = "30 8/10 * * * *")
//	public void sortBackGroundProcess()
//	{
////	   boolean bl_check = false; 
//		BackGroundJobModel bgjModel = new BackGroundJobModel();
//		bgjModel.execUpsertToTEMPProdWorkDate();
////	   if(bl_check) { System.out.println("execUpsertToTEMPProdWorkDate    :: " + Calendar.getInstance().getTime().toString()); }
//		bgjModel.execUpsertToTEMPUserStatusOnWeb();
////	   if(bl_check) { System.out.println("execUpsertToTEMPUserStatusOnWeb    :: " + Calendar.getInstance().getTime().toString()); }
//	} 
}
