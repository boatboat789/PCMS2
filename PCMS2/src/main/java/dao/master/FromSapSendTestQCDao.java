package dao.master;

import java.util.ArrayList;

import entities.SendTestQCDetail;

public interface FromSapSendTestQCDao {

	ArrayList<SendTestQCDetail> getFromSapSendTestQCByProductionOrder(String prodOrder);

}
