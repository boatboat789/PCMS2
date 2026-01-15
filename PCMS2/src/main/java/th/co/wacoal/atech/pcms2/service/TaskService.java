package th.co.wacoal.atech.pcms2.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import th.co.wacoal.atech.pcms2.entities.SORDetail;
import th.co.wacoal.atech.pcms2.service.master.FromSORCFMService;

@Configuration
@EnableScheduling
public class TaskService {
	@SuppressWarnings("unused")
	private String LOCAL_DIRECTORY;
	@SuppressWarnings("unused")
	private String FTP_DIRECTORY;
	@SuppressWarnings("unused") 
	private ServletContext context;
	private DataImportSORService sorModel;
	private BackGroundJobService bgjModel;
	private boolean isCheck = false;
	private FromSORCFMService fscModel;
	@Autowired
	public TaskService(DataImportSORService sorModel
			, BackGroundJobService bgjModel
			, FromSORCFMService fscModel			) {
		this.sorModel = sorModel;
		this.bgjModel = bgjModel;
		this.fscModel = fscModel; 
//		isCheck = true;
	}  
//	@Scheduled(fixedRate = 50000000)	
	@Scheduled(cron = "0 6/10 * * * *")
	public void sortBackGroundAfterGetERPDataProcedure()
	{
		this.handlerBackGroundZATTCustomerConfirm2();
		if(isCheck)System.out.println("Start sortBackGroundAfterGetERPDataProcedure: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		this.bgjModel.sortBackGroundAfterGetERPDataProcedure();
		this.runAllSyncJobs();
		if(isCheck)System.out.println("End sortBackGroundAfterGetERPDataProcedure: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
	}  
	public void handlerBackGroundZATTCustomerConfirm2()
	{
		if(isCheck)System.out.println("Start sortBackGroundZ_ATT_CustomerConfirm2: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
		this.bgjModel.handlerBackGroundZ_ATT_CustomerConfirm2();
		if(isCheck)System.out.println("End sortBackGroundZ_ATT_CustomerConfirm2: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date())); 
	}  
	public void runAllSyncJobs() {
	    if(isCheck)System.out.println("=== Start runAllSyncJobs: " +  
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

	    sortBackGroundCustomer();
	    sortBackGroundProductionOrder();
	    sortBackGroundSaleOrder();

	    if(isCheck)System.out.println("=== End runAllSyncJobs: " +  
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	// ---- ย้ายเป็น private method ----
	private void sortBackGroundCustomer() {
	    if(isCheck)System.out.println("Start sortBackGroundCustomer: " +  
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));  
	    this.bgjModel.handlerERPAtechToWebAppCustomer();
	    if(isCheck)System.out.println("End sortBackGroundCustomer: " +  
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); 
	}

	private void sortBackGroundProductionOrder() {
	    if(isCheck)System.out.println("Start sortBackGroundProductionOrder: " +  
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); 
	    this.bgjModel.handlerERPAtechToWebAppProductionOrder();
	    if(isCheck)System.out.println("End sortBackGroundProductionOrder: " +  
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); 
	}

	private void sortBackGroundSaleOrder() {
	    if(isCheck)System.out.println("Start sortBackGroundSaleOrder: " +  
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); 
	    this.bgjModel.handlerERPAtechToWebAppSaleOrder();
	    if(isCheck)System.out.println("End sortBackGroundSaleOrder: " +  
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); 
	} 
	@Scheduled(cron = "0 0 1 * * *") 
//	@Scheduled(fixedRate = 50000000)	
	public void bgJobHandlerDataFromOrgatex()
	{ 
		ArrayList<SORDetail> list = sorModel.getList();
		fscModel.upSertFromSORCFMDetail(list);  
	}  
}
