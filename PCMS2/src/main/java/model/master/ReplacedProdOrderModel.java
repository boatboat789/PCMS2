package model.master;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import dao.master.ReplacedProdOrderDao;
import dao.master.implement.ReplacedProdOrderDaoImpl;
import entities.PCMSSecondTableDetail;
import entities.ReplacedProdOrderDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

public class ReplacedProdOrderModel extends HttpServlet {
	   private static final long serialVersionUID = 1L;
	   private Database database;
	   private ReplacedProdOrderDao dao;
	   @SuppressWarnings("unused")
	   private String[] uiColumns;
	   @SuppressWarnings("unused")
	   private static final String columns = "";

	   public ReplacedProdOrderModel() {
	      try {
	         this.database = new Database(SqlInfo.getInstance());
	         this.dao = new ReplacedProdOrderDaoImpl(this.database );
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

	   public ReplacedProdOrderDetail upsertReplacedProdOrder(ReplacedProdOrderDetail bean, String dataStatus) {
			// TODO Auto-generated method stub
		   ReplacedProdOrderDetail list = this.dao.upsertReplacedProdOrder(bean,dataStatus);
			return list;
		}

		public PCMSSecondTableDetail updateReplacedProdOrder(PCMSSecondTableDetail bean, String dataStatus){
			// TODO Auto-generated method stub
			PCMSSecondTableDetail list = this.dao.updateReplacedProdOrder(  bean,   dataStatus);
					return list;
		}
		public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdRP(String prodOrder){
			// TODO Auto-generated method stub
			ArrayList<ReplacedProdOrderDetail>  list = this.dao.getReplacedProdOrderDetailByPrdRP(  prodOrder);
					return list;
		}
		public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrd(String prodOrder){
			// TODO Auto-generated method stub
			ArrayList<ReplacedProdOrderDetail>  list = this.dao.getReplacedProdOrderDetailByPrd(  prodOrder);
					return list;
		}
//		public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMain(String prodOrder){
//			// TODO Auto-generated method stub
//			ArrayList<ReplacedProdOrderDetail>  list = this.dao.getReplacedProdOrderDetailByPrdMain(  prodOrder);
//					return list;
//		}

		public ArrayList<ReplacedProdOrderDetail> getReplacedProdOrderDetailByPrdMainAndSO(String prdOrder, String saleOrder,
				String saleLine){
		// TODO Auto-generated method stub
		ArrayList<ReplacedProdOrderDetail>  list = this.dao.getReplacedProdOrderDetailByPrdMainAndSO(  prdOrder,saleOrder,saleLine);
				return list;
	}
}
