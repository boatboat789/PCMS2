package th.co.wacoal.atech.pcms2.service;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	private boolean isCheck =false;
	@Autowired
	public TaskService(SORModel sorModel
			, BackGroundJobModel bgjModel
			) {
		this.sorModel = sorModel;
		this.bgjModel = bgjModel;
//		isCheck = true;
	} 
//	@Scheduled(fixedRate = 50000000)	
	@Scheduled(cron = "30 8/10 * * * *")
	public void sortBackGroundCustomer()
	{
		if(isCheck)System.out.println("Start sortBackGroundCustomer: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));  
		this.bgjModel.handlerERPAtechToWebAppCustomer();
		if(isCheck)System.out.println("End sortBackGroundCustomer: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
	}
//	@Scheduled(fixedRate = 50000000)	
	@Scheduled(cron = "30 8/10 * * * *")
	public void sortBackGroundProductionOrder()
	{
		if(isCheck)System.out.println("Start sortBackGroundProductionOrder: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		this.bgjModel.handlerERPAtechToWebAppProductionOrder();
		if(isCheck)System.out.println("End sortBackGroundProductionOrder: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
	}
//	@Scheduled(fixedRate = 50000000)	
	@Scheduled(cron = "30 8/10 * * * *")
	public void sortBackGroundSaleOrder()
	{
		if(isCheck)System.out.println("Start sortBackGroundSaleOrder: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		this.bgjModel.handlerERPAtechToWebAppSaleOrder();
		if(isCheck)System.out.println("End sortBackGroundSaleOrder: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
	}
	@Scheduled(cron = "30 4/10 * * * *")
	public void sortBackGroundAfterGetERPDataProcedure()
	{
		if(isCheck)System.out.println("Start sortBackGroundAfterGetERPDataProcedure: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		this.bgjModel.sortBackGroundAfterGetERPDataProcedure();
		if(isCheck)System.out.println("End sortBackGroundAfterGetERPDataProcedure: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
	}
	@Scheduled(cron = "0 0 1 * * *") 
	public void bgJobHandlerDataFromOrgatex()
	{
		this.sorModel.upSertSORToPCMS();
	}  
}
