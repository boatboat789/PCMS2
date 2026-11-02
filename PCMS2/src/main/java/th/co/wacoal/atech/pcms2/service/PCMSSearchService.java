package th.co.wacoal.atech.pcms2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.PCMSSearchDao;

@Service
public class PCMSSearchService { 
	private PCMSSearchDao dao; 
	@Autowired
	public PCMSSearchService(PCMSSearchDao dao) { 
			this.dao = dao; 
	}

	public String handlerTempTableCustomerSearchList(List<String> customerNameList, List<String> customerShortNameList)
	{ 
		String list = this.dao.handlerTempTableCustomerSearchList(customerNameList, customerShortNameList);
		return list;
	}

	public String handlerTempTableUserStatusList(List<String> statuss )
	{ 
		String list = this.dao.handlerTempTableUserStatusList(statuss);
		return list;
	}
 

}
