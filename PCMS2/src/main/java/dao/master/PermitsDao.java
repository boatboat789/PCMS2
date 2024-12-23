package dao.master;

import java.util.ArrayList;

import entities.PermitDetail;

public interface PermitsDao {

	ArrayList<PermitDetail> getPermitsDetailByPermitId(String permitId);

	ArrayList<PermitDetail> getPermitsDetail();

}
