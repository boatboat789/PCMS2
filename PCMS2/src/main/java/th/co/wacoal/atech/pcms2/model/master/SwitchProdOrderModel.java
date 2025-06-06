package th.co.wacoal.atech.pcms2.model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.wacoal.atech.pcms2.dao.master.SwitchProdOrderDao;
import th.co.wacoal.atech.pcms2.dao.master.implement.SwitchProdOrderDaoImpl;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.SwitchProdOrderDetail;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class SwitchProdOrderModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private SwitchProdOrderDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	    @Autowired
	   public SwitchProdOrderModel() {
	      try {
	         this.database = new Database(SqlPCMSInfo.getInstance());
	         this.dao = new SwitchProdOrderDaoImpl(this.database );
	         this.uiColumns = arrayColumn();
	      } catch (SQLException | ClassNotFoundException var2) {
	         var2.printStackTrace();
	      }

	   }

	   public static String stringColumn() {
	      return "[]";
	   }

	   public static String[] arrayColumn() {
	      return "".replaceAll("'", "").split(",");
	   }

	   @Override
	public void destroy() {
	      this.database.close();
	      super.destroy();
	   }

	   public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrder(String prdOrder ) {
			// TODO Auto-generated method stub
			ArrayList<PCMSSecondTableDetail> list = this.dao.getSwitchProdOrderDetailByProdOrder(prdOrder);
			return list;
		}

		public PCMSSecondTableDetail updateSwitchProdOrderDetail(PCMSSecondTableDetail bean, String dataStatus) {
			// TODO Auto-generated method stub
			PCMSSecondTableDetail list = this.dao.updateSwitchProdOrderDetail(  bean,   dataStatus);
					return list;
		}

		public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderDetailByProdOrderForHandlerSwitchProd(
				String prdOrderSW) {
			// TODO Auto-generated method stub
						ArrayList<PCMSSecondTableDetail> list = this.dao.getSwitchProdOrderDetailByProdOrderForHandlerSwitchProd(  prdOrderSW);
								return list;
					}

	   public ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrdSW(String prodOrderSW) {
			// TODO Auto-generated method stub
			ArrayList<SwitchProdOrderDetail> list = this.dao.getSwitchProdOrderDetailByPrdSW(prodOrderSW);
			return list;
		}

	   public ArrayList<SwitchProdOrderDetail> getSwitchProdOrderDetailByPrd(String prodOrder) {
			// TODO Auto-generated method stub
			ArrayList<SwitchProdOrderDetail> list = this.dao.getSwitchProdOrderDetailByPrd(prodOrder);
			return list;
		}
	   public ArrayList<SwitchProdOrderDetail> getSWProdOrderDetailByPrd(String prodOrder) {
			// TODO Auto-generated method stub
			ArrayList<SwitchProdOrderDetail> list = this.dao.getSWProdOrderDetailByPrd(prodOrder);
			return list;
		}
	   public PCMSSecondTableDetail upsertSwitchProdOrder(PCMSSecondTableDetail bean, String dataStatus){
			// TODO Auto-generated method stub
		   PCMSSecondTableDetail list = this.dao.upsertSwitchProdOrder(bean,dataStatus);
			return list;
		}
}
