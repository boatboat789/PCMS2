package th.co.wacoal.atech.pcms2.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.DataImportSORDao;
import th.co.wacoal.atech.pcms2.entities.SORDetail;

@Component
public class DataImportSORService { 
	   private DataImportSORDao dao; 

	    @Autowired
	   public DataImportSORService(DataImportSORDao dao) { 
	         this.dao = dao; 

	   } 
	   public ArrayList<SORDetail> getList(){
		   return this.dao.getList();
	   }


}
