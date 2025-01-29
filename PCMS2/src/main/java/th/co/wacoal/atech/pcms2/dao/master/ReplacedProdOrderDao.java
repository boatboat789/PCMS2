package th.co.wacoal.atech.pcms2.dao.master;

import java.util.ArrayList;

import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.ReplacedProdOrderDetail;
public interface ReplacedProdOrderDao {

	ReplacedProdOrderDetail upsertReplacedProdOrder(ReplacedProdOrderDetail bean, String dataStatus);

	PCMSSecondTableDetail updateReplacedProdOrder(PCMSSecondTableDetail bean, String dataStatus);

	ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdRP(String prodOrder);

	ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrd(String prodOrder);

	ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMain(String prodOrder);

	ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMainAndSO(String prdOrder, String saleOrder, String saleLine);



}
