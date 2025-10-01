package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import th.co.wacoal.atech.pcms2.entities.ApiResponse; 
import th.co.wacoal.atech.pcms2.entities.SaleOrderLogDetail; 
import th.co.wacoal.atech.pcms2.model.master.FromSapMainSaleModel;
import th.co.wacoal.atech.pcms2.model.master.erp.atech.ERPAtechModel; 
 
    
@Controller
@RequestMapping(value = { "/Log/SaleOrderLog"  })
//@RequestMapping(value = { "/Log/ATT/ProdOperationLog" })
public class SaleOrderLogController {
	@SuppressWarnings("unused") 
	private ServletContext context;
	private FromSapMainSaleModel fsmpModel; 
	private ERPAtechModel erpAttModel;  
    @Autowired
    public SaleOrderLogController( ) { 
    	this.fsmpModel = new FromSapMainSaleModel();
    	this.erpAttModel = new ERPAtechModel();
    }
	@RequestMapping(  value = "/{dataType}",  method = RequestMethod.GET )
	public ModelAndView getModelAndView(HttpSession session,@PathVariable("dataType") String dataType) {
		ModelAndView mv = new ModelAndView();
		Gson g = new Gson();
		String user = (String) session.getAttribute("user");
		String titleName = "";
		if(dataType.equals("ERP365")) {titleName = "Log - ข้อมูล SaleOrder จาก View บน ERP365";}
		else {titleName = "Log - ข้อมูล SaleOrder ที่รับค่ามาอัพเดทแล้ว";}
		mv.setViewName("LogInformation/SaleOrderLog");
		mv.addObject("UserID", g.toJson(user));
		mv.addObject("titleName", g.toJson(titleName));
		mv.addObject("dataType", g.toJson(dataType));
//		mv.addObject("UserID", g.toJson());'Log - ข้อมูล Prod/Opertation ที่อัพเดทจาก ERP365'
		return mv;
	}  
	@RequestMapping(  value = "/{dataType}/getSaleOrderLogBySearch",  method = RequestMethod.GET )
	public ResponseEntity<ApiResponse<List<SaleOrderLogDetail>>> doGetProdOperationLogBySearch(HttpSession session,HttpServletRequest request, HttpServletResponse response   
			,@RequestParam("saleOrder") String saleOrder 
			,@RequestParam("changeDateStart") String changeDateStart 
			,@RequestParam("changeDateEnd") String changeDateEnd 
//			, @RequestBody List<SearchCriteria> list 
			,@PathVariable("dataType") String dataType) throws IOException {  
        ArrayList<SaleOrderLogDetail> resultList;
		if(dataType.equals("ERP365")) { 
        	resultList = erpAttModel.getFromErpMainSaleDetailWithRangeOfChangeDate( changeDateStart,changeDateEnd,saleOrder);
        }
        else { 
        	resultList = fsmpModel.getFromSapMainSaleDetailWithRangeOfChangeDate(changeDateStart,changeDateEnd,saleOrder);
        }
//	    boolean isSuccess = result.stream().allMatch(bean -> "I".equals(bean.getIconStatus()));
	    boolean isSuccess = true;
	    String status = isSuccess ? "success" : "error";
	    String message = isSuccess ? "บันทึกข้อมูลสำเร็จ" : "บางรายการบันทึกล้มเหลว";

	    ApiResponse<List<SaleOrderLogDetail>> apiResponse = new ApiResponse<>(status, message, resultList);
	    return ResponseEntity.ok(apiResponse);
	}
	
// POST - JSON
//	@RequestMapping(  value = "/{dataType}/getProductionOrderLogBySearch",  method = RequestMethod.POST )
//	public void doGetProdOperationLogBySearch(HttpSession session,HttpServletRequest request, HttpServletResponse response   
//			// ถ้าตั้งชื่อ Field ตรงก็ไม่ต้อง Gson ใช้ได้แค่กับ POST
//			 ,@RequestBody SearchCriteria searchCriteria  // Direct POJO mapping!
//			,@PathVariable("dataType") String dataType) throws IOException { 
//		Gson g = new Gson();
//		System.out.println(searchCriteria.toString());
////		// Define the Type for ArrayList of OrgatexDyeLotDetail
////        Type listType = new TypeToken<ArrayList<ProductionOrderLogDetail>>(){}.getType(); 
////        // Deserialize JSON directly to ArrayList
////        ArrayList<ProductionOrderLogDetail> poList = g.fromJson(data, listType); 
//        
//        
//        ArrayList<ProductionOrderLogDetail> list  = new ArrayList<ProductionOrderLogDetail>();;
////        if(dataType.equals("ERP365")) { 
////            list = erpAttModel.getDataFromERPDetail(poList.get(0).getProductionOrder(),poList.get(0).getChangeDate());
////        }
////        else { 
////            list = fsmpModel.getFromSapMainProdDetailWithRangeOfChangeDate(poList.get(0).getProductionOrder(),poList.get(0).getChangeDate());
////        }
//		response.setContentType("application/json");
//		PrintWriter out = response.getWriter();
//		out.println(g.toJson(list ));
//	}
	
//	//POST LIST
//	@RequestMapping(  value = "/{dataType}/getProductionOrderLogBySearch",  method = RequestMethod.POST )
//	public ResponseEntity<ApiResponse<List<ProductionOrderLogDetail>>> doGetProdOperationLogBySearch(HttpSession session,HttpServletRequest request, HttpServletResponse response   
//			// ถ้าตั้งชื่อ Field ตรงก็ไม่ต้อง Gson ใช้ได้แค่กับ POST
//			, @RequestBody List<SearchCriteria> list 
//			,@PathVariable("dataType") String dataType) throws IOException { 
//		Gson g = new Gson(); 
//		// Define the Type for ArrayList of OrgatexDyeLotDetail
////		Type listType = new TypeToken<ArrayList<ProductionOrderLogDetail>>(){}.getType(); 
////		// Deserialize JSON directly to ArrayList
////		ArrayList<ProductionOrderLogDetail> poList = g.fromJson(data, listType); 
//	    for (SearchCriteria sc : list) {
//	        System.out.println(sc.getProductionOrder());
//	    }
//
////	    boolean isSuccess = result.stream().allMatch(bean -> "I".equals(bean.getIconStatus()));
//	    boolean isSuccess = true;
//	    String status = isSuccess ? "success" : "error";
//	    String message = isSuccess ? "บันทึกข้อมูลสำเร็จ" : "บางรายการบันทึกล้มเหลว";
//
//	    ApiResponse<List<ProductionOrderLogDetail>> apiResponse = new ApiResponse<>(status, message, result);
//	    return ResponseEntity.ok(apiResponse);
//	}
	
//	public class SearchCriteria   {
//		private String productionOrder ;
//	    private String changeDateStart;
//	    private String changeDateEnd; 
//
////	    private List<String> typeCarList;
////	    private List<CarObject> carList;
//		public SearchCriteria() {
//			super();
//		}
//		public String getProductionOrder()
//		{
//			return productionOrder;
//		}
//		public void setProductionOrder(String productionOrder)
//		{
//			this.productionOrder = productionOrder;
//		}
//		public String getChangeDateStart()
//		{
//			return changeDateStart;
//		}
//		public void setChangeDateStart(String changeDateStart)
//		{
//			this.changeDateStart = changeDateStart;
//		}
//		public String getChangeDateEnd()
//		{
//			return changeDateEnd;
//		}
//		public void setChangeDateEnd(String changeDateEnd)
//		{
//			this.changeDateEnd = changeDateEnd;
//		}
//		@Override
//		public String toString()
//		{
//			return "SearchCriteria [changeDateStart=" + changeDateStart + ", changeDateEnd=" + changeDateEnd + "]";
//		}
//	    
//	    // getters and setters  
//	}
}
