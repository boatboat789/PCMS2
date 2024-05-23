package dao.master;

import java.util.ArrayList;

import entities.PCMSSecondTableDetail;
import entities.SwitchProdOrderDetail;
public interface SwitchProdOrderDao {

	ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrder(String prodOrder);

	PCMSSecondTableDetail updateSwitchProdOrderDetail(PCMSSecondTableDetail bean, String dataStatus);

	ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrderForHandlerSwitchProd(String prdOrderSW);

	ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrdSW(String prodOrder);

	ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrd(String prodOrder);

	ArrayList<SwitchProdOrderDetail> getSWProdOrderDetailByPrd(String prodOrder);

	PCMSSecondTableDetail upsertSwitchProdOrder(PCMSSecondTableDetail bean, String dataStatus);



}
