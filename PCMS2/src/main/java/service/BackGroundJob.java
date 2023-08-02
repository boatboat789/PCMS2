	package service;

import java.io.File;
import java.sql.SQLException;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import info.FtpSapInfo;
import info.SqlInfo;
import model.SORModel; 
import model.BackGroundJobModel; 
import th.in.totemplate.core.net.FtpReceive;
import th.in.totemplate.core.sql.Database;

public class BackGroundJob { 
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY;
	private FtpTaskRunner ftr;
	@Autowired
	private ServletContext context; 
	public BackGroundJob() { /* TODO document why this constructor is empty */ } 
//	@Scheduled(fixedDelay = 10000)         
	@Scheduled(cron = "0 4/10 * * * *")      
	public void sortBackGround1() {	 
		SORModel model = new SORModel();
		BackGroundJobModel bgjModel = new BackGroundJobModel();
		model.upSertSORToPCMS(); 
		LOCAL_DIRECTORY = context.getRealPath("/") + context.getInitParameter("DIR_UPLOAD");
		FTP_DIRECTORY = context.getInitParameter("FTP_PATH");
		// Creating a File object
		File file = new File(LOCAL_DIRECTORY);
		// Creating the directory 
		boolean bool = file.mkdir();
		try {	
			ftr = new FtpTaskRunner(new Database(SqlInfo.getInstance()), new FtpReceive(FtpSapInfo.getInstance(), FTP_DIRECTORY, LOCAL_DIRECTORY));
			ftr.loadFTP();  
			bgjModel.execUpsertToMainProd();
			bgjModel.execUpsertToTEMPProdWorkDate();
			bgjModel.execUpsertToTEMPUserStatusOnWeb();  
		} catch (ClassNotFoundException | SQLException e) { 
			e.printStackTrace();
		}       
	}     
//	@Scheduled(fixedDelay = 10000)     
	@Scheduled(cron = "0 0 1 * * *")    
	public void sortBackGroundTwo() { 
		SORModel model = new SORModel();
		model.upSertSORToPCMS();
	}  
}