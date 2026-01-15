package th.co.wacoal.atech.pcms2.service.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.wacoal.atech.pcms2.dao.master.PermitsDao;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;

@Service
public class PermitsService  { 
    private PermitsDao dao;

	@Autowired
	public PermitsService(PermitsDao dao) {
		this.dao = dao; 
	} 

	public ArrayList<PermitDetail> getEmployeePermitsDetailByPermitId(String userId , String permitId )
	{
		ArrayList<PermitDetail> list = this.dao.getEmployeePermitsDetailByPermitId(userId,permitId) ;
		return list;
	}
	public ArrayList<PermitDetail> getPermitsDetail( )
	{
		ArrayList<PermitDetail> list = this.dao.getPermitsDetail( ) ;
		return list;
	}

	public ArrayList<PermitDetail> getPermitsDetailByPermitId( String permitId )
	{
		ArrayList<PermitDetail> list = this.dao.getPermitsDetailByPermitId( permitId) ;
		return list;
	}
}
