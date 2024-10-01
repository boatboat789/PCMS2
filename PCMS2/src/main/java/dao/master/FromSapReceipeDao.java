package dao.master ;

import java.util.ArrayList;

import entities.ReceipeDetail;

public interface FromSapReceipeDao {

	ArrayList<ReceipeDetail> getFromSapReceipeDetailByProductionOrder(String prodOrder);

}
