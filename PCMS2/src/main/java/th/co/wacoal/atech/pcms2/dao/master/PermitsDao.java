package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PermitDetail;

public interface PermitsDao {

	ArrayList<PermitDetail> getPermitsDetailByPermitId(String permitId);

	ArrayList<PermitDetail> getPermitsDetail();

}
