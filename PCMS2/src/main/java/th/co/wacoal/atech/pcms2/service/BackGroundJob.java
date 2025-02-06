package th.co.wacoal.atech.pcms2.service;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import th.co.wacoal.atech.pcms2.model.BackGroundJobModel;
import th.co.wacoal.atech.pcms2.model.SORModel;

@EnableAsync
public class BackGroundJob {
	@SuppressWarnings("unused")
	private String LOCAL_DIRECTORY;
	@SuppressWarnings("unused")
	private String FTP_DIRECTORY;
	@SuppressWarnings("unused")
	private boolean isTry = false;
	@SuppressWarnings("unused")
	@Autowired
	private ServletContext context;

	public BackGroundJob() {
		/* TODO document why this constructor is empty */ }

//	@Scheduled(fixedDelay = 1000000000)
//	@Scheduled(cron = "30 2/10 * * * *") 
	public void bgJobHandlerERPAtechToWebApp()
	{
		BackGroundJobModel model = new BackGroundJobModel();
		model.handlerERPAtechToWebApp();
	}

//	@Scheduled(cron = "30 8/10 * * * *")
	public void sortBackGroundProcess()
	{
//	   boolean bl_check = false; 
		BackGroundJobModel bgjModel = new BackGroundJobModel();
		bgjModel.execUpsertToTEMPProdWorkDate();
//	   if(bl_check) { System.out.println("execUpsertToTEMPProdWorkDate    :: " + Calendar.getInstance().getTime().toString()); }
		bgjModel.execUpsertToTEMPUserStatusOnWeb();
//	   if(bl_check) { System.out.println("execUpsertToTEMPUserStatusOnWeb    :: " + Calendar.getInstance().getTime().toString()); }
	}

//	@Async
//	@Scheduled(cron = "0 0 1 * * *")
	public void sortBackGroundTwo()
	{
		SORModel model = new SORModel();
		model.upSertSORToPCMS();
	}
}