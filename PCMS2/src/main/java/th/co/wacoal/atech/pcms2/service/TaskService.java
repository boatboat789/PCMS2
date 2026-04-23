package th.co.wacoal.atech.pcms2.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.entities.SORDetail;
import th.co.wacoal.atech.pcms2.service.master.FromSORCFMService;

@Component
public class TaskService {
	private DataImportSORService dataImportSORService;
	private BackGroundJobService backGroundJobService;
	private boolean isCheck = false;
	private FromSORCFMService fromSORCFMService;
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConcurrentHashMap<String, Boolean> jobLocks = new ConcurrentHashMap<>();

	private void executeWithLock(String jobName, Runnable task)
	{
		if (jobLocks.putIfAbsent(jobName, true) == null) {
			try {
				task.run();
			} finally {
				jobLocks.remove(jobName);
			}
		}
	}

	@Autowired
	public TaskService(DataImportSORService dataImportSORService, BackGroundJobService backGroundJobService,
			FromSORCFMService fromSORCFMService) {
		this.dataImportSORService = dataImportSORService;
		this.backGroundJobService = backGroundJobService;
		this.fromSORCFMService = fromSORCFMService;
//		isCheck = true;
	}

//	@Scheduled(cron = "1 * * * * *")
	@Scheduled(cron = "0 13/20 * * * *")
	public void sortBackGroundAfterGetERPDataProcedure()
	{

		System.out.println("Start Date : " + new Date());
		executeWithLock("ERP_SYNC_JOB", () -> {

			handlerBackGroundZATTCustomerConfirm2();
			runAllSyncJobs();
			backGroundJobService.sortBackGroundAfterGetERPDataProcedure();

		});
		System.out.println("End Date : " + new Date());
	}

	public void handlerBackGroundZATTCustomerConfirm2()
	{
		if (isCheck)
			System.out.println("Start sortBackGroundZ_ATT_CustomerConfirm2: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		this.backGroundJobService.handlerBackGroundZ_ATT_CustomerConfirm2();
		if (isCheck)
			System.out.println("End sortBackGroundZ_ATT_CustomerConfirm2: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	public void runAllSyncJobs()
	{
		if (isCheck)
			System.out.println("=== Start runAllSyncJobs: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		sortBackGroundCustomer();
		sortBackGroundSaleOrder();
		sortBackGroundProductionOrder();

//	    this.backGroundJobService.execSumBillAndGoodReceive();
		if (isCheck)
			System.out.println("=== End runAllSyncJobs: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	// ---- ย้ายเป็น private method ----
	private void sortBackGroundCustomer()
	{
		if (isCheck)
			System.out.println("Start sortBackGroundCustomer: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		this.backGroundJobService.handlerERPAtechToWebAppCustomer();
		if (isCheck)
			System.out.println("End sortBackGroundCustomer: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	private void sortBackGroundProductionOrder()
	{
		if (isCheck)
			System.out.println(
					"Start sortBackGroundProductionOrder: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		this.backGroundJobService.handlerERPAtechToWebAppProductionOrder();
		if (isCheck)
			System.out.println(
					"End sortBackGroundProductionOrder: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	private void sortBackGroundSaleOrder()
	{
		if (isCheck)
			System.out
					.println("Start sortBackGroundSaleOrder: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		this.backGroundJobService.handlerERPAtechToWebAppSaleOrder();
		if (isCheck)
			System.out.println("End sortBackGroundSaleOrder: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	@Scheduled(cron = "0 0 1 * * *")
	public void bgJobHandlerDataFromOrgatex()
	{

		executeWithLock("ORGATEX_IMPORT", () -> {

			ArrayList<SORDetail> list = dataImportSORService.getList();
			fromSORCFMService.upSertFromSORCFMDetail(list);

		});
	}
}
