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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import th.co.wacoal.atech.pcms2.entities.ApiResponse;
import th.co.wacoal.atech.pcms2.entities.ProductionOrderLogDetail;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainProdService;
import th.co.wacoal.atech.pcms2.service.master.erp.atech.ERPAtechService; 
 
    
@Controller
@RequestMapping(value = { "/Log/ProductionOrderLog"  }) 
public class ProductionOrderLogController {
	@SuppressWarnings("unused") 
	private ServletContext context;
	private FromSapMainProdService fsmpModel;  
	private ERPAtechService erpService;  
    @Autowired
    public ProductionOrderLogController(ERPAtechService erpService,FromSapMainProdService fsmpModel ) { 
    	this.fsmpModel = fsmpModel;
    	this.erpService = erpService;
    }
//	@RequestMapping(method = { RequestMethod.GET })
	@RequestMapping(  value = "/{dataType}",  method = RequestMethod.GET )
	public ModelAndView getModelAndView(HttpSession session,@PathVariable("dataType") String dataType) {
		ModelAndView mv = new ModelAndView();
		Gson g = new Gson();
		String user = (String) session.getAttribute("user");
		String titleName = "";
		if(dataType.equals("ERP365")) {titleName = "Log - ข้อมูล ProductionOrder จาก View บน ERP365";}
		else {titleName = "Log - ข้อมูล ProductionOrder ที่รับค่ามาอัพเดทแล้ว";}
		mv.setViewName("LogInformation/ProductionOrderLog");
		mv.addObject("UserID", g.toJson(user));
		mv.addObject("titleName", g.toJson(titleName));
		mv.addObject("dataType", g.toJson(dataType)); 
		return mv;
	}   

	// GET - JSON
//	@RequestMapping(  value = "/{dataType}/getProductionOrderLogBySearch",  method = RequestMethod.GET )
//	public ResponseEntity<ApiResponse<List<ProductionOrderLogDetail>>> doGetProdOperationLogBySearch(HttpSession session,HttpServletRequest request, HttpServletResponse response   
//			,@RequestParam("productionOrder") String productionOrder 
//			,@RequestParam("changeDateStart") String changeDateStart 
//			,@RequestParam("changeDateEnd") String changeDateEnd 
////			, @RequestBody List<SearchCriteria> list 
//			,@PathVariable("dataType") String dataType) throws IOException { 
////		Gson g = new Gson();   
//        ArrayList<ProductionOrderLogDetail> resultList;
//		if(dataType.equals("ERP365")) { 
//        	resultList = erpAttModel.getFromErpMainProdDetailWithRangeOfChangeDate( changeDateStart,changeDateEnd,productionOrder);
//        }
//        else { 
//        	resultList = fsmpModel.getFromSapMainProdDetailWithRangeOfChangeDate(changeDateStart,changeDateEnd,productionOrder);
//        }
////	    boolean isSuccess = result.stream().allMatch(bean -> "I".equals(bean.getIconStatus()));
//	    boolean isSuccess = true;
//	    String status = isSuccess ? "success" : "error";
//	    String message = isSuccess ? "บันทึกข้อมูลสำเร็จ" : "บางรายการบันทึกล้มเหลว";
//
//	    ApiResponse<List<ProductionOrderLogDetail>> apiResponse = new ApiResponse<>(status, message, resultList);
//	    return ResponseEntity.ok(apiResponse);
//	}
	
// POST - JSON
	@RequestMapping(  value = "/{dataType}/getProductionOrderLogBySearch",  method = RequestMethod.POST )
	public ResponseEntity<ApiResponse<List<ProductionOrderLogDetail>>> doGetProdOperationLogBySearch(HttpSession session,HttpServletRequest request, HttpServletResponse response   
			// ถ้าตั้งชื่อ Field ตรงก็ไม่ต้อง Gson ใช้ได้แค่กับ POST
			 ,@RequestBody SearchCriteria bean  // Direct POJO mapping!
			,@PathVariable("dataType") String dataType) throws IOException {  
	    ArrayList<ProductionOrderLogDetail> resultList;    
	    String changeDateStart = bean.getChangeDateStart() ;
	    String changeDateEnd = bean.getChangeDateEnd();
	    String productionOrder = bean.getProductionOrder();
		if(dataType.equals("ERP365")) { 
	    	resultList = erpService.getFromErpMainProdDetailWithRangeOfChangeDate( changeDateStart,changeDateEnd,productionOrder);
	    }
	    else { 
	    	resultList = fsmpModel.getFromSapMainProdDetailWithRangeOfChangeDate(changeDateStart,changeDateEnd,productionOrder);
	    }
		response.setContentType("application/json");
	    boolean isSuccess = true;
	    String status = isSuccess ? "success" : "error";
	    String message = isSuccess ? "บันทึกข้อมูลสำเร็จ" : "บางรายการบันทึกล้มเหลว";

	    ApiResponse<List<ProductionOrderLogDetail>> apiResponse = new ApiResponse<>(status, message, resultList);
	    return ResponseEntity.ok(apiResponse);
	}
	
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
	public class SearchCriteria   {
		private String productionOrder ;
	    private String changeDateStart;
	    private String changeDateEnd; 

//	    private List<String> typeCarList;
//	    private List<CarObject> carList;
		public SearchCriteria() {
			super();
		}
		public String getProductionOrder()
		{
			return productionOrder;
		}
		public void setProductionOrder(String productionOrder)
		{
			this.productionOrder = productionOrder;
		}
		public String getChangeDateStart()
		{
			return changeDateStart;
		}
		public void setChangeDateStart(String changeDateStart)
		{
			this.changeDateStart = changeDateStart;
		}
		public String getChangeDateEnd()
		{
			return changeDateEnd;
		}
		public void setChangeDateEnd(String changeDateEnd)
		{
			this.changeDateEnd = changeDateEnd;
		}
		@Override
		public String toString()
		{
			return "SearchCriteria [changeDateStart=" + changeDateStart + ", changeDateEnd=" + changeDateEnd + "]";
		}
	    
	    // getters and setters  
	}
}
