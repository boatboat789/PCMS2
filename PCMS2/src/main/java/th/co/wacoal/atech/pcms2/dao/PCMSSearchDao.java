package th.co.wacoal.atech.pcms2.dao;

import java.util.List;

public interface PCMSSearchDao {

	String handlerTempTableCustomerSearchList(List<String> customerNameList, List<String> customerShortNameList);

	void handlerCloseTempTableCustomerSearchList();

}
