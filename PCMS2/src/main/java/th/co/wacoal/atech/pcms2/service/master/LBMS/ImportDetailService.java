package th.co.wacoal.atech.pcms2.service.master.LBMS;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.LBMS.ImportDetailDao;
import th.co.wacoal.atech.pcms2.entities.LBMS.ImportDetail;

@Service
public class ImportDetailService { 
	private ImportDetailDao dao; 

	@Autowired
	public ImportDetailService(ImportDetailDao dao) { 
			this.dao = dao; 
	}

	public static String stringColumn()
	{
		return "[]";
	}

	public static String[] arrayColumn()
	{
		return "".replaceAll("'", "").split(",");
	} 
	public ArrayList<ImportDetail> getImportDetailByProductionOrder(String prodOrder)
	{
		// TODO Auto-generated method stub
		ArrayList<ImportDetail> list = this.dao.getImportDetailByProductionOrder(prodOrder);
		return list;
	}
}
